package com.devflux.deenone.core.auth;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.devflux.deenone.core.localization.LocaleManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Enterprise Google Sign-In Controller for DeenOne.
 * Fully synchronized with live MySQL database 'users' table and local SQLite profile.
 * Extracts profile details (display name, email, googleId, and photoUrl avatar).
 */
public class GoogleAuthHelper {

    public static final int RC_GOOGLE_SIGN_IN = 9005;
    public static final int RC_ACCOUNT_PICKER = 9006;
    private static OnGoogleAuthCallback activeCallback;

    public interface OnGoogleAuthCallback {
        void onSuccess(AuthManager.UserSession session, String message);
        void onError(String errorMessage);
    }

    /**
     * Initiates Google Sign-In / Account Chooser flow using Google Play Services.
     */
    public static void startGoogleSignIn(Activity activity, OnGoogleAuthCallback callback) {
        if (activity == null || activity.isFinishing()) return;
        activeCallback = callback;
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build();
            GoogleSignInClient client = GoogleSignIn.getClient(activity, gso);
            client.signOut().addOnCompleteListener(activity, task -> {
                try {
                    Intent signInIntent = client.getSignInIntent();
                    activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                } catch (Exception e) {
                    launchNativeAccountPicker(activity);
                }
            });
        } catch (Exception e) {
            launchNativeAccountPicker(activity);
        }
    }

    /**
     * Fallback: Native Android System Account Chooser flow.
     */
    private static void launchNativeAccountPicker(Activity activity) {
        try {
            Intent intent = AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    new String[]{"com.google"},
                    false,
                    null,
                    null,
                    null,
                    null
            );
            activity.startActivityForResult(intent, RC_ACCOUNT_PICKER);
        } catch (Exception e) {
            if (activeCallback != null) {
                boolean isBn = LocaleManager.isBengali(activity);
                activeCallback.onError(isBn ? "গুগল অ্যাকাউন্ট নির্বাচন করা সম্ভব হয়নি।" : "Could not select Google account.");
            }
        }
    }

    /**
     * Handles Activity result from Google Sign-In or Account Picker intent.
     */
    public static boolean handleActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account != null) {
                        String email = account.getEmail();
                        String name = account.getDisplayName();
                        if (TextUtils.isEmpty(name)) {
                            name = formatNameFromEmail(email);
                        }
                        String googleId = account.getId();
                        if (TextUtils.isEmpty(googleId) && !TextUtils.isEmpty(email)) {
                            googleId = "g_acc_" + Math.abs(email.toLowerCase().hashCode());
                        }
                        Uri photoUri = account.getPhotoUrl();
                        String avatar = photoUri != null ? photoUri.toString() : "";
                        if (!TextUtils.isEmpty(avatar)) {
                            if (avatar.contains("=s96-c")) {
                                avatar = avatar.replace("=s96-c", "=s300-c");
                            } else if (avatar.contains("=s120-c")) {
                                avatar = avatar.replace("=s120-c", "=s300-c");
                            }
                        }
                        if (TextUtils.isEmpty(avatar) && !TextUtils.isEmpty(email)) {
                            avatar = "https://www.gravatar.com/avatar/" + md5Hex(email.trim().toLowerCase()) + "?d=identicon&s=300";
                        }
                        if (TextUtils.isEmpty(avatar)) {
                            avatar = "avatar_1";
                        }

                        authenticateWithBackend(activity, googleId, email, name, avatar, activeCallback);
                        return true;
                    }
                } catch (ApiException e) {
                    Log.w("GoogleAuthHelper", "signInResult:failed code=" + e.getStatusCode());
                    if (e.getStatusCode() != 12501 && e.getStatusCode() != 4) {
                        launchNativeAccountPicker(activity);
                        return true;
                    }
                } catch (Exception ex) {
                    launchNativeAccountPicker(activity);
                    return true;
                }
            }
            return false;
        }

        if (requestCode == RC_ACCOUNT_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String accountEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (TextUtils.isEmpty(accountEmail)) {
                    accountEmail = data.getStringExtra("authAccount");
                }
                if (!TextUtils.isEmpty(accountEmail)) {
                    String googleId = "g_acc_" + Math.abs(accountEmail.toLowerCase().hashCode());
                    String name = formatNameFromEmail(accountEmail);
                    String avatar = "https://www.gravatar.com/avatar/" + md5Hex(accountEmail.trim().toLowerCase()) + "?d=identicon&s=300";

                    authenticateWithBackend(activity, googleId, accountEmail, name, avatar, activeCallback);
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    private static String md5Hex(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "default";
        }
    }

    /**
     * Formats clean human-readable name from an email address (e.g., "riyadkhan013@gmail.com" -> "Riyad Khan").
     */
    public static String formatNameFromEmail(String email) {
        if (TextUtils.isEmpty(email)) return "DeenOne User";
        String username = email.split("@")[0];
        username = username.replaceAll("[._0-9]", " ").trim();
        if (username.isEmpty()) return "DeenOne User";

        String[] parts = username.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.length() > 0) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        String formatted = sb.toString().trim();
        return TextUtils.isEmpty(formatted) ? "DeenOne User" : formatted;
    }

    /**
     * Authenticates with live MySQL backend API (POST auth.php action=google).
     */
    public static void authenticateWithBackend(Activity activity, String googleId, String email,
                                               String name, String avatar, OnGoogleAuthCallback callback) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        AuthManager.googleSignIn(activity, googleId, email, name, avatar, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(AuthManager.UserSession session, String message) {
                if (activity.isFinishing()) return;
                Toast.makeText(activity, isBn ? "গুগল দিয়ে লগইন সফল হয়েছে!" : "Google login successful!", Toast.LENGTH_SHORT).show();
                if (callback != null) {
                    callback.onSuccess(session, message);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (activity.isFinishing()) return;
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                if (callback != null) {
                    callback.onError(errorMessage);
                }
            }
        });
    }
}
