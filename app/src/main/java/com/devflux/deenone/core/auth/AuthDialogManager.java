package com.devflux.deenone.core.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogAuthForgotPasswordBinding;
import com.devflux.deenone.databinding.DialogAuthLoginBinding;
import com.devflux.deenone.databinding.DialogAuthRegisterBinding;
import com.devflux.deenone.databinding.DialogLegalInfoBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class AuthDialogManager {

    public interface OnAuthSuccessListener {
        void onAuthSuccess(AuthManager.UserSession session);
    }

    public static void showLoginDialog(Activity activity, OnAuthSuccessListener listener) {
        showLoginDialog(activity, null, listener);
    }

    public static void showLoginDialog(Activity activity, String prefilledIdentifier, OnAuthSuccessListener listener) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogAuthLoginBinding binding = DialogAuthLoginBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Dynamic Dual-Language Setup
        binding.btnCloseAuthLogin.setContentDescription(isBn ? "বন্ধ করুন" : "Close");

        // Attach touch animation ONLY on buttons (Strict Rule 7: Zero touch animation on CardViews)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseAuthLogin);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubmitLogin);
        TouchAnimationUtil.attachTouchSpring(binding.tvLoginForgotPassword);
        TouchAnimationUtil.attachTouchSpring(binding.tvSwitchToRegister);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthPrivacy);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthTerms);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthSupport);

        // Pre-fill identifier if provided (e.g. from password reset)
        if (prefilledIdentifier != null && !prefilledIdentifier.trim().isEmpty()) {
            binding.etLoginIdentifier.setText(prefilledIdentifier.trim());
            binding.etLoginPassword.requestFocus();
        }

        binding.btnCloseAuthLogin.setOnClickListener(v -> dialog.dismiss());

        // Password visibility toggle
        final boolean[] isPasswordVisible = {false};
        binding.ivLoginTogglePassword.setOnClickListener(v -> {
            isPasswordVisible[0] = !isPasswordVisible[0];
            if (isPasswordVisible[0]) {
                binding.etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etLoginPassword.setSelection(binding.etLoginPassword.getText().length());
        });

        // Forgot password link
        binding.tvLoginForgotPassword.setOnClickListener(v -> {
            dialog.dismiss();
            showForgotPasswordDialog(activity, listener);
        });

        // Switch to register link
        binding.tvSwitchToRegister.setOnClickListener(v -> {
            dialog.dismiss();
            showRegisterDialog(activity, listener);
        });

        // Footer: Dynamic Legal & Support Links (Managed from PHP Admin Panel)
        binding.tvAuthPrivacy.setOnClickListener(v -> showLegalInfoDialog(activity, "privacy"));
        binding.tvAuthTerms.setOnClickListener(v -> showLegalInfoDialog(activity, "terms"));
        binding.tvAuthSupport.setOnClickListener(v -> showLegalInfoDialog(activity, "support"));

        // Continue with Google One-Tap Login
        binding.btnGoogleLogin.setOnClickListener(v -> {
            GoogleAuthHelper.startGoogleSignIn(activity, new GoogleAuthHelper.OnGoogleAuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    dialog.dismiss();
                    if (listener != null) listener.onAuthSuccess(session);
                }

                @Override
                public void onError(String errorMessage) {
                    // Handled inside GoogleAuthHelper
                }
            });
        });

        // Submit Login button
        binding.btnSubmitLogin.setOnClickListener(v -> {
            String identifier = binding.etLoginIdentifier.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString().trim();

            if (identifier.isEmpty()) {
                binding.etLoginIdentifier.setError(isBn ? "ইমেইল অথবা ফোন নম্বর লিখুন" : "Enter email or phone number");
                binding.etLoginIdentifier.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                binding.etLoginPassword.setError(isBn ? "পাসওয়ার্ড লিখুন" : "Enter password");
                binding.etLoginPassword.requestFocus();
                return;
            }

            binding.btnSubmitLogin.setEnabled(false);
            binding.btnSubmitLogin.setText(isBn ? "যাচাই করা হচ্ছে..." : "Verifying...");

            AuthManager.login(activity, identifier, password, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (listener != null) listener.onAuthSuccess(session);
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnSubmitLogin.setEnabled(true);
                    binding.btnSubmitLogin.setText(isBn ? "লগইন করুন →" : "Login →");
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }

    public static void showRegisterDialog(Activity activity, OnAuthSuccessListener listener) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogAuthRegisterBinding binding = DialogAuthRegisterBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Attach touch animation ONLY on buttons (Strict Rule 7: Zero touch animation on CardViews)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseAuthRegister);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubmitRegister);
        TouchAnimationUtil.attachTouchSpring(binding.tvSwitchToLogin);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthPrivacy);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthTerms);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthSupport);

        binding.btnCloseAuthRegister.setOnClickListener(v -> dialog.dismiss());

        // Setup Blood Group dropdown
        String[] bloodGroups = isBn
                ? new String[]{"নির্বাচন করুন", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}
                : new String[]{"Select", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        binding.spinnerRegisterBloodGroup.setAdapter(bloodAdapter);

        // Password visibility toggles
        final boolean[] isPassVisible = {false};
        binding.ivRegisterTogglePassword.setOnClickListener(v -> {
            isPassVisible[0] = !isPassVisible[0];
            if (isPassVisible[0]) {
                binding.etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etRegisterPassword.setSelection(binding.etRegisterPassword.getText().length());
        });

        final boolean[] isConfirmPassVisible = {false};
        binding.ivRegisterToggleConfirmPassword.setOnClickListener(v -> {
            isConfirmPassVisible[0] = !isConfirmPassVisible[0];
            if (isConfirmPassVisible[0]) {
                binding.etRegisterConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etRegisterConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etRegisterConfirmPassword.setSelection(binding.etRegisterConfirmPassword.getText().length());
        });

        // Switch to login link
        binding.tvSwitchToLogin.setOnClickListener(v -> {
            dialog.dismiss();
            showLoginDialog(activity, listener);
        });

        // Footer: Dynamic Legal & Support Links
        binding.tvAuthPrivacy.setOnClickListener(v -> showLegalInfoDialog(activity, "privacy"));
        binding.tvAuthTerms.setOnClickListener(v -> showLegalInfoDialog(activity, "terms"));
        binding.tvAuthSupport.setOnClickListener(v -> showLegalInfoDialog(activity, "support"));

        // Continue with Google One-Tap Sign Up
        binding.btnGoogleRegister.setOnClickListener(v -> {
            GoogleAuthHelper.startGoogleSignIn(activity, new GoogleAuthHelper.OnGoogleAuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    dialog.dismiss();
                    if (listener != null) listener.onAuthSuccess(session);
                }

                @Override
                public void onError(String errorMessage) {
                    // Handled inside GoogleAuthHelper
                }
            });
        });

        // Submit Register button
        binding.btnSubmitRegister.setOnClickListener(v -> {
            String name = binding.etRegisterName.getText().toString().trim();
            String phone = binding.etRegisterPhone.getText().toString().trim();
            String email = binding.etRegisterEmail.getText().toString().trim();
            String password = binding.etRegisterPassword.getText().toString().trim();
            String confirmPass = binding.etRegisterConfirmPassword.getText().toString().trim();

            int selectedPos = binding.spinnerRegisterBloodGroup.getSelectedItemPosition();
            String bloodGroup = selectedPos > 0 ? bloodGroups[selectedPos] : "";

            if (name.isEmpty()) {
                binding.etRegisterName.setError(isBn ? "আপনার সম্পূর্ণ নাম লিখুন" : "Enter your full name");
                binding.etRegisterName.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                binding.etRegisterEmail.setError(isBn ? "ইমেইল ঠিকানা প্রদান করা বাধ্যতামূলক" : "Email address is mandatory");
                binding.etRegisterEmail.requestFocus();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etRegisterEmail.setError(isBn ? "সঠিক ইমেইল ঠিকানা প্রদান করুন" : "Please enter a valid email address");
                binding.etRegisterEmail.requestFocus();
                return;
            }
            if (password.isEmpty() || password.length() < 6) {
                binding.etRegisterPassword.setError(isBn ? "পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে" : "Password must be at least 6 characters");
                binding.etRegisterPassword.requestFocus();
                return;
            }
            if (!password.equals(confirmPass)) {
                binding.etRegisterConfirmPassword.setError(isBn ? "উভয় পাসওয়ার্ড একই হতে হবে" : "Both passwords must match");
                binding.etRegisterConfirmPassword.requestFocus();
                return;
            }

            binding.btnSubmitRegister.setEnabled(false);
            binding.btnSubmitRegister.setText(isBn ? "অ্যাকাউন্ট তৈরি হচ্ছে..." : "Creating account...");

            AuthManager.register(activity, name, phone, email, bloodGroup, password, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (listener != null) listener.onAuthSuccess(session);
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnSubmitRegister.setEnabled(true);
                    binding.btnSubmitRegister.setText(isBn ? "অ্যাকাউন্ট তৈরি করুন →" : "Create Account →");
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }

    public static void showForgotPasswordDialog(Activity activity, OnAuthSuccessListener listener) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogAuthForgotPasswordBinding binding = DialogAuthForgotPasswordBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Attach touch animation ONLY on buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseAuthForgot);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubmitForgotPassword);
        TouchAnimationUtil.attachTouchSpring(binding.btnVerifyOtpCode);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubmitResetPassword);
        TouchAnimationUtil.attachTouchSpring(binding.btnResendResetCode);
        TouchAnimationUtil.attachTouchSpring(binding.tvReturnToLogin);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthPrivacy);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthTerms);
        TouchAnimationUtil.attachTouchSpring(binding.tvAuthSupport);

        binding.btnCloseAuthForgot.setOnClickListener(v -> dialog.dismiss());

        binding.tvReturnToLogin.setOnClickListener(v -> {
            dialog.dismiss();
            showLoginDialog(activity, listener);
        });

        // Dynamic dual-language localization
        if (binding.tvForgotTitle != null) binding.tvForgotTitle.setText(isBn ? "পাসওয়ার্ড পুনরুদ্ধার" : "Reset Password");
        if (binding.tvForgotSubtitle != null) {
            binding.tvForgotSubtitle.setText(isBn 
                ? "আপনার অ্যাকাউন্টের নিবন্ধিত ইমেইল ঠিকানা প্রদান করুন। আমরা একটি ৬ ডিজিটের ভেরিফিকেশন কোড পাঠাব।"
                : "Enter your registered email address. We will send a 6-digit verification code.");
        }
        if (binding.tvForgotIdentifierLabel != null) binding.tvForgotIdentifierLabel.setText(isBn ? "নিবন্ধিত ইমেইল ঠিকানা" : "Registered Email Address");
        if (binding.btnSubmitForgotPassword != null) binding.btnSubmitForgotPassword.setText(isBn ? "ভেরিফিকেশন কোড পাঠান →" : "Send Verification Code →");
        if (binding.tvVerifyOtpTitle != null) binding.tvVerifyOtpTitle.setText(isBn ? "কোড যাচাইকরণ" : "Verify Code");
        if (binding.tvVerifyOtpSubtitle != null) {
            binding.tvVerifyOtpSubtitle.setText(isBn
                ? "আপনার ইমেইলে প্রেরিত ৬ ডিজিটের ওটিপি ভেরিফিকেশন কোডটি প্রবেশ করান।"
                : "Enter the 6-digit OTP verification code sent to your email.");
        }
        if (binding.tvForgotCountdownLabel != null) binding.tvForgotCountdownLabel.setText(isBn ? "কোডের মেয়াদ ও পুনঃঅনুরোধ সময়" : "Code Expiry & Resend Cooldown");
        if (binding.tvResetCodeLabel != null) binding.tvResetCodeLabel.setText(isBn ? "৬ ডিজিটের ওটিপি কোড" : "6-Digit OTP Code");
        if (binding.btnVerifyOtpCode != null) binding.btnVerifyOtpCode.setText(isBn ? "কোড যাচাই করুন →" : "Verify Code →");
        if (binding.btnResendResetCode != null) binding.btnResendResetCode.setText(isBn ? "পুনরায় কোড পাঠান" : "Resend Code");
        if (binding.tvResetTitle != null) binding.tvResetTitle.setText(isBn ? "নতুন পাসওয়ার্ড নির্ধারণ" : "Set New Password");
        if (binding.tvResetSubtitle != null) {
            binding.tvResetSubtitle.setText(isBn
                ? "আপনার অ্যাকাউন্টের জন্য নতুন ও শক্তিশালী পাসওয়ার্ড নির্ধারণ করুন।"
                : "Enter and confirm your new secure password.");
        }
        if (binding.tvResetNewPassLabel != null) binding.tvResetNewPassLabel.setText(isBn ? "নতুন পাসওয়ার্ড (ন্যূনতম ৬ অক্ষর)" : "New Password (min 6 chars)");
        if (binding.tvResetConfirmPassLabel != null) binding.tvResetConfirmPassLabel.setText(isBn ? "পাসওয়ার্ড নিশ্চিত করুন" : "Confirm Password");
        if (binding.btnSubmitResetPassword != null) binding.btnSubmitResetPassword.setText(isBn ? "পাসওয়ার্ড রিসেট সম্পন্ন করুন →" : "Complete Password Reset →");
        if (binding.tvReturnToLogin != null) binding.tvReturnToLogin.setText(isBn ? "লগইন পেজে ফিরে যান" : "Back to Login");

        // Footer: Dynamic Legal & Support Links
        binding.tvAuthPrivacy.setOnClickListener(v -> showLegalInfoDialog(activity, "privacy"));
        binding.tvAuthTerms.setOnClickListener(v -> showLegalInfoDialog(activity, "terms"));
        binding.tvAuthSupport.setOnClickListener(v -> showLegalInfoDialog(activity, "support"));

        // Step 3 Password visibility toggles
        final boolean[] isNewPassVisible = {false};
        binding.ivResetToggleNewPass.setOnClickListener(v -> {
            isNewPassVisible[0] = !isNewPassVisible[0];
            if (isNewPassVisible[0]) {
                binding.etResetNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etResetNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etResetNewPassword.setSelection(binding.etResetNewPassword.getText().length());
        });

        final boolean[] isConfirmResetPassVisible = {false};
        binding.ivResetToggleConfirmPass.setOnClickListener(v -> {
            isConfirmResetPassVisible[0] = !isConfirmResetPassVisible[0];
            if (isConfirmResetPassVisible[0]) {
                binding.etResetConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etResetConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.etResetConfirmPassword.setSelection(binding.etResetConfirmPassword.getText().length());
        });

        final android.os.CountDownTimer[] otpTimer = {null};
        final String[] verifiedCodeHolder = {""};

        Runnable start5MinuteCountdown = () -> {
            if (otpTimer[0] != null) {
                otpTimer[0].cancel();
            }
            binding.btnResendResetCode.setEnabled(false);
            binding.btnResendResetCode.setTextColor(activity.getColor(R.color.text_muted));

            otpTimer[0] = new android.os.CountDownTimer(300000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long totalSeconds = millisUntilFinished / 1000;
                    long minutes = totalSeconds / 60;
                    long seconds = totalSeconds % 60;
                    String timeFormatted = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds);
                    if (isBn) {
                        timeFormatted = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(timeFormatted);
                    }
                    binding.tvForgotCountdownTimer.setText(timeFormatted);
                }

                @Override
                public void onFinish() {
                    binding.tvForgotCountdownTimer.setText(isBn ? "০০:০০" : "00:00");
                    binding.btnResendResetCode.setEnabled(true);
                    binding.btnResendResetCode.setTextColor(activity.getColor(R.color.accent_mint));
                }
            }.start();
        };

        dialog.setOnDismissListener(d -> {
            if (otpTimer[0] != null) {
                otpTimer[0].cancel();
                otpTimer[0] = null;
            }
        });

        // =====================================================================
        // STEP 1: SEND VERIFICATION CODE
        // =====================================================================
        binding.btnSubmitForgotPassword.setOnClickListener(v -> {
            String identifier = binding.etForgotIdentifier.getText().toString().trim();
            if (identifier.isEmpty() || !identifier.contains("@")) {
                binding.etForgotIdentifier.setError(isBn ? "সঠিক ইমেইল ঠিকানা লিখুন" : "Enter a valid email address");
                binding.etForgotIdentifier.requestFocus();
                return;
            }

            binding.btnSubmitForgotPassword.setEnabled(false);
            binding.btnSubmitForgotPassword.setText(isBn ? "কোড পাঠানো হচ্ছে..." : "Sending code...");

            AuthManager.forgotPassword(activity, identifier, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    binding.btnSubmitForgotPassword.setEnabled(true);
                    binding.btnSubmitForgotPassword.setText(isBn ? "ভেরিফিকেশন কোড পাঠান →" : "Send Verification Code →");
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

                    // Transition to Step 2
                    binding.layoutForgotStep1.setVisibility(View.GONE);
                    binding.layoutForgotStep2.setVisibility(View.VISIBLE);
                    binding.layoutForgotStep3.setVisibility(View.GONE);
                    binding.tvForgotTargetIdentifier.setText(identifier);

                    start5MinuteCountdown.run();
                    binding.etResetCode.requestFocus();
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnSubmitForgotPassword.setEnabled(true);
                    binding.btnSubmitForgotPassword.setText(isBn ? "ভেরিফিকেশন কোড পাঠান →" : "Send Verification Code →");
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        // =====================================================================
        // STEP 2: VERIFY OTP CODE
        // =====================================================================
        binding.btnVerifyOtpCode.setOnClickListener(v -> {
            String code = binding.etResetCode.getText().toString().trim();
            if (code.length() < 6) {
                binding.etResetCode.setError(isBn ? "সঠিক ৬ ডিজিটের ওটিপি কোড লিখুন" : "Enter 6-digit OTP code");
                binding.etResetCode.requestFocus();
                return;
            }

            verifiedCodeHolder[0] = code;

            // Transition to Step 3: Set New Password
            binding.layoutForgotStep2.setVisibility(View.GONE);
            binding.layoutForgotStep3.setVisibility(View.VISIBLE);
            binding.etResetNewPassword.requestFocus();
        });

        // Resend Code action
        binding.btnResendResetCode.setOnClickListener(v -> {
            String identifier = binding.tvForgotTargetIdentifier.getText().toString().trim();
            binding.btnResendResetCode.setEnabled(false);
            AuthManager.forgotPassword(activity, identifier, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    start5MinuteCountdown.run();
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnResendResetCode.setEnabled(true);
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // =====================================================================
        // STEP 3: RESET PASSWORD WITH VERIFIED CODE
        // =====================================================================
        binding.btnSubmitResetPassword.setOnClickListener(v -> {
            String identifier = binding.tvForgotTargetIdentifier.getText().toString().trim();
            String code = verifiedCodeHolder[0];
            String newPassword = binding.etResetNewPassword.getText().toString().trim();
            String confirmPassword = binding.etResetConfirmPassword.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                binding.layoutForgotStep3.setVisibility(View.GONE);
                binding.layoutForgotStep2.setVisibility(View.VISIBLE);
                binding.etResetCode.setError(isBn ? "কোড পুনরায় যাচাই করুন" : "Please verify code again");
                return;
            }
            if (newPassword.isEmpty() || newPassword.length() < 6) {
                binding.etResetNewPassword.setError(isBn ? "নতুন পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে" : "Password must be at least 6 characters");
                binding.etResetNewPassword.requestFocus();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                binding.etResetConfirmPassword.setError(isBn ? "উভয় পাসওয়ার্ড একই হতে হবে" : "Both passwords must match");
                binding.etResetConfirmPassword.requestFocus();
                return;
            }

            binding.btnSubmitResetPassword.setEnabled(false);
            binding.btnSubmitResetPassword.setText(isBn ? "পাসওয়ার্ড আপডেট হচ্ছে..." : "Updating password...");

            AuthManager.resetPassword(activity, identifier, code, newPassword, new AuthManager.AuthCallback() {
                @Override
                public void onSuccess(AuthManager.UserSession session, String message) {
                    if (otpTimer[0] != null) {
                        otpTimer[0].cancel();
                        otpTimer[0] = null;
                    }
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    showLoginDialog(activity, identifier, listener);
                }

                @Override
                public void onError(String errorMessage) {
                    binding.btnSubmitResetPassword.setEnabled(true);
                    binding.btnSubmitResetPassword.setText(isBn ? "পাসওয়ার্ড রিসেট সম্পন্ন করুন →" : "Complete Password Reset →");
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }

    /**
     * Shows a beautifully designed, full-screen interactive modal displaying Privacy Policy,
     * Terms of Service, or Help & Support, synced dynamically with the PHP Admin Panel.
     */
    public static void showLegalInfoDialog(Activity activity, String type) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogLegalInfoBinding binding = DialogLegalInfoBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Attach touch animation ONLY on buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseLegal);
        TouchAnimationUtil.attachTouchSpring(binding.btnSupportEmail);
        TouchAnimationUtil.attachTouchSpring(binding.btnSupportWhatsApp);

        binding.btnCloseLegal.setOnClickListener(v -> dialog.dismiss());

        if ("privacy".equalsIgnoreCase(type)) {
            binding.tvLegalTitle.setText(isBn ? "গোপনীয়তা নীতি" : "Privacy Policy");
            binding.tvLegalHeaderBadge.setText("PRIVACY POLICY");
            binding.tvLegalSubtitle.setText(isBn ? "DeenOne ডেটা সুরক্ষা ও গোপনীয়তা প্রতিশ্রুতি" : "DeenOne Data Security & Privacy Commitment");
            binding.ivLegalIcon.setImageResource(R.drawable.ic_shield_check);
            binding.layoutSupportActions.setVisibility(View.GONE);
        } else if ("terms".equalsIgnoreCase(type)) {
            binding.tvLegalTitle.setText(isBn ? "ব্যবহারের শর্তাবলী" : "Terms of Service");
            binding.tvLegalHeaderBadge.setText("TERMS OF SERVICE");
            binding.tvLegalSubtitle.setText(isBn ? "DeenOne অ্যাপ্লিকেশন ব্যবহারের সাধারণ নিয়মাবলী" : "General Terms & Rules of Using DeenOne");
            binding.ivLegalIcon.setImageResource(R.drawable.ic_scale_justice);
            binding.layoutSupportActions.setVisibility(View.GONE);
        } else {
            binding.tvLegalTitle.setText(isBn ? "সাহায্য ও সাপোর্ট" : "Help & Support");
            binding.tvLegalHeaderBadge.setText("HELP & SUPPORT");
            binding.tvLegalSubtitle.setText(isBn ? "২৪/৭ কাস্টমার সহায়তা ও জিজ্ঞাসা" : "24/7 Customer Help & Inquiries");
            binding.ivLegalIcon.setImageResource(R.drawable.ic_help);
            binding.layoutSupportActions.setVisibility(View.VISIBLE);
        }

        binding.tvLegalContent.setText(isBn ? "নীতিমালা লোড হচ্ছে, অনুগ্রহ করে অপেক্ষা করুন..." : "Loading policy details, please wait...");

        AuthManager.fetchLegalInfo(activity, info -> {
            if (activity.isFinishing() || !dialog.isShowing()) return;

            if ("privacy".equalsIgnoreCase(type)) {
                binding.tvLegalContent.setText(info.privacyPolicy);
            } else if ("terms".equalsIgnoreCase(type)) {
                binding.tvLegalContent.setText(info.termsService);
            } else {
                binding.tvLegalContent.setText(info.supportInfo);

                binding.btnSupportEmail.setText("✉️ " + (isBn ? "ইমেইল: " : "Email: ") + info.supportEmail);
                binding.btnSupportEmail.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + info.supportEmail));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "DeenOne Support Inquiry");
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(activity, (isBn ? "ইমেইল অ্যাপ পাওয়া যায়নি: " : "No email app found: ") + info.supportEmail, Toast.LENGTH_LONG).show();
                    }
                });

                binding.btnSupportWhatsApp.setText("💬 " + (isBn ? "হোয়াটসঅ্যাপ: " : "WhatsApp: ") + info.supportWhatsapp);
                binding.btnSupportWhatsApp.setOnClickListener(v -> {
                    try {
                        String cleanNumber = info.supportWhatsapp.replaceAll("[^0-9+]", "");
                        if (cleanNumber.startsWith("+")) cleanNumber = cleanNumber.substring(1);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://wa.me/" + cleanNumber));
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(activity, (isBn ? "হোয়াটসঅ্যাপ চালু করা সম্ভব হয়নি: " : "Could not open WhatsApp: ") + info.supportWhatsapp, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dialog.show();
    }
}
