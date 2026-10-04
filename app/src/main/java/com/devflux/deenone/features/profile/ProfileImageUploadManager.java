package com.devflux.deenone.features.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.backend.IBackendService;
import com.devflux.deenone.core.backend.model.UserProfile;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileImageUploadManager {

    private static final String PREF_PROFILE = "user_profile_prefs";
    private static final String KEY_USER_ID = "profile_user_id";
    private static final String KEY_LEGACY_UID = "user_uid";
    private static final String KEY_USER_AVATAR_URI = "user_avatar_uri";
    private static final String KEY_USER_AVATAR_PRESET = "user_avatar_preset";
    private static final int MAX_IMAGE_DIMENSION = 512;
    private static final int COMPRESSION_QUALITY = 85;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnPhotoUpdatedListener {
        void onPhotoUpdated(String newImagePath, Bitmap circularBitmap);
    }

    public static String getActiveUserId(Context context) {
        if (context == null) return "usr_main";
        com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(context);
        if (session != null && !session.userId.isEmpty() && !"usr_guest".equals(session.userId)) {
            return session.userId;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, prefs.getString(KEY_LEGACY_UID, "usr_main"));
    }

    public static void startImageSelectionFlow(Activity activity, OnPhotoUpdatedListener listener) {
        String[] options = {
                "গ্যালারি / ফাইল ম্যানেজার থেকে ছবি নির্বাচন",
                "প্রিসেট ইসলামিক এভাটার পছন্দ করুন",
                "কাস্টম ছবি মুছে ফেলুন (রিসেট)"
        };

        new AlertDialog.Builder(activity)
                .setTitle("প্রোফাইল ছবি পরিবর্তন")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        launchGalleryPicker(activity);
                    } else if (which == 1) {
                        showPresetAvatarDialog(activity, listener);
                    } else {
                        resetToDefaultAvatar(activity, listener);
                    }
                })
                .setNegativeButton("বাতিল", null)
                .show();
    }

    public static final int RC_PICK_IMAGE = 9008;

    public static void launchGalleryPicker(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimeTypes = {"image/jpeg", "image/png", "image/webp", "image/jpg", "image/gif"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            activity.startActivityForResult(Intent.createChooser(intent, "প্রোফাইল ছবি নির্বাচন করুন"), RC_PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(activity, "গ্যালারি ওপেন করা যায়নি: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean validateImageUri(Context context, Uri uri) {
        if (uri == null) return false;
        try (InputStream is = context.getContentResolver().openInputStream(uri)) {
            if (is == null) return false;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            return options.outWidth > 0 && options.outHeight > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String detectMimeType(Context context, Uri uri) {
        if (context == null || uri == null) return "image/jpeg";
        String type = null;
        try {
            type = context.getContentResolver().getType(uri);
        } catch (Exception ignored) {}
        if (type == null) {
            String uriStr = uri.toString().toLowerCase();
            if (uriStr.endsWith(".gif")) type = "image/gif";
            else if (uriStr.endsWith(".png")) type = "image/png";
            else if (uriStr.endsWith(".webp")) type = "image/webp";
            else if (uriStr.endsWith(".jpg") || uriStr.endsWith(".jpeg")) type = "image/jpeg";
        }
        return type != null ? type.toLowerCase() : "image/jpeg";
    }

    public static void processAndUploadImage(Activity activity, Uri imageUri, OnPhotoUpdatedListener listener) {
        if (activity == null || activity.isFinishing() || imageUri == null) return;

        // 1. Image Validation
        if (!validateImageUri(activity, imageUri)) {
            new AlertDialog.Builder(activity)
                    .setTitle("অকার্যকর ছবি")
                    .setMessage("নির্বাচিত ফাইলটি একটি বৈধ ছবি নয় অথবা ফাইলটি পড়া যাচ্ছে না। অনুগ্রহ করে অন্য একটি ছবি নির্বাচন করুন।")
                    .setPositiveButton("ঠিক আছে", null)
                    .show();
            return;
        }

        String userId = getActiveUserId(activity);

        // 2. Show Preview & Crop Confirmation Dialog
        showCropPreviewDialog(activity, imageUri, userId, listener);
    }

    private static void showCropPreviewDialog(Activity activity, Uri imageUri, String userId, OnPhotoUpdatedListener listener) {
        FrameLayout container = new FrameLayout(activity);
        container.setPadding(32, 24, 32, 16);

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tvTitle = new TextView(activity);
        tvTitle.setText("ছবি প্রিভিউ ও ফ্রেম যাচাই");
        tvTitle.setTextColor(Color.parseColor("#34D399"));
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setPadding(0, 0, 0, 16);
        layout.addView(tvTitle);

        // Circular Frame Preview
        FrameLayout frame = new FrameLayout(activity);
        int frameSize = dpToPx(activity, 150);
        LinearLayout.LayoutParams frameLp = new LinearLayout.LayoutParams(frameSize, frameSize);
        frameLp.gravity = Gravity.CENTER_HORIZONTAL;
        frame.setLayoutParams(frameLp);
        frame.setBackgroundResource(R.drawable.circle_progress_bg);
        frame.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FBC02D")));

        ImageView ivPreview = new ImageView(activity);
        int imgSize = dpToPx(activity, 140);
        FrameLayout.LayoutParams imgLp = new FrameLayout.LayoutParams(imgSize, imgSize, Gravity.CENTER);
        ivPreview.setLayoutParams(imgLp);
        ivPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Load preview safely
        executor.execute(() -> {
            try (InputStream is = activity.getContentResolver().openInputStream(imageUri)) {
                Bitmap previewBitmap = BitmapFactory.decodeStream(is);
                if (previewBitmap != null) {
                    Bitmap circular = getCircularCroppedBitmap(previewBitmap, 300);
                    mainHandler.post(() -> ivPreview.setImageBitmap(circular));
                }
            } catch (Exception ignored) {}
        });

        frame.addView(ivPreview);
        layout.addView(frame);

        TextView tvHint = new TextView(activity);
        tvHint.setText("ছবিটি স্বয়ংক্রিয়ভাবে সর্বোত্তম কোয়ালিটি ও সাইজে অপ্টিমাইজ ও কম্প্রেস করে আপলোড করা হবে।");
        tvHint.setTextColor(Color.parseColor("#8FA69D"));
        tvHint.setTextSize(12);
        tvHint.setGravity(Gravity.CENTER);
        tvHint.setPadding(16, 16, 16, 0);
        layout.addView(tvHint);

        container.addView(layout);

        new AlertDialog.Builder(activity)
                .setView(container)
                .setPositiveButton("ক্রপ ও আপলোড করুন", (d, which) -> {
                    executeCompressionAndUpload(activity, imageUri, userId, listener);
                })
                .setNegativeButton("বাতিল", null)
                .show();
    }

    private static void executeCompressionAndUpload(Activity activity, Uri imageUri, String userId, OnPhotoUpdatedListener listener) {
        // Show Loading / Upload Progress Dialog
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("প্রোফাইল ছবি আপলোড");
        progressDialog.setMessage("ছবি কম্প্রেস ও ডাটাবেজ স্টোরেজে আপলোড করা হচ্ছে...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        executor.execute(() -> {
            try {
                File avatarDir = new File(activity.getFilesDir(), "profile_avatars");
                if (!avatarDir.exists()) avatarDir.mkdirs();

                String mimeType = detectMimeType(activity, imageUri);
                boolean isGif = mimeType.contains("gif");
                boolean isPng = mimeType.contains("png");
                boolean isWebp = mimeType.contains("webp");

                File savedFile;
                Bitmap finalCircular;

                if (isGif) {
                    // For GIF: Copy raw bytes directly to preserve animation frames
                    savedFile = new File(avatarDir, "avatar_" + userId + ".gif");
                    try (InputStream in = activity.getContentResolver().openInputStream(imageUri);
                         FileOutputStream fos = new FileOutputStream(savedFile)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                        fos.flush();
                    }

                    Bitmap firstFrame = BitmapFactory.decodeFile(savedFile.getAbsolutePath());
                    finalCircular = firstFrame != null ? getCircularCroppedBitmap(firstFrame, MAX_IMAGE_DIMENSION) : null;
                } else {
                    // 1. Decode and Downsample Image
                    Bitmap optimalBitmap = decodeAndDownsampleImage(activity, imageUri, MAX_IMAGE_DIMENSION);
                    if (optimalBitmap == null) {
                        mainHandler.post(() -> {
                            safeDismissDialog(progressDialog, activity);
                            Toast.makeText(activity, "ছবি প্রসেস করা সম্ভব হয়নি", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    // Square Center Crop
                    Bitmap croppedSquare = cropToSquare(optimalBitmap);
                    finalCircular = getCircularCroppedBitmap(croppedSquare, MAX_IMAGE_DIMENSION);

                    String ext = isPng ? ".png" : (isWebp ? ".webp" : ".jpg");
                    savedFile = new File(avatarDir, "avatar_" + userId + ext);
                    try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                        if (isPng) {
                            croppedSquare.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } else if (isWebp) {
                            croppedSquare.compress(Bitmap.CompressFormat.WEBP, COMPRESSION_QUALITY, fos);
                        } else {
                            croppedSquare.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, fos);
                        }
                        fos.flush();
                    }
                }

                // 2. Upload to Database Storage
                final File fileToUpload = savedFile;
                final Bitmap circularForCallback = finalCircular;
                BackendRepository backend = BackendRepository.getInstance(activity);
                backend.uploadProfileImage(userId, fileToUpload, new IBackendService.BackendCallback<String>() {
                    @Override
                    public void onSuccess(String uploadedUrlOrPath) {
                        // 3. Update Database User Profile
                        SharedPreferences profilePrefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
                        String savedAvatar = uploadedUrlOrPath != null ? uploadedUrlOrPath : fileToUpload.getAbsolutePath();
                        profilePrefs.edit()
                                .putString(KEY_USER_AVATAR_URI, savedAvatar)
                                .putString(com.devflux.deenone.core.auth.AuthManager.KEY_AVATAR, savedAvatar)
                                .putInt(KEY_USER_AVATAR_PRESET, -1)
                                .apply();

                        com.devflux.deenone.core.auth.AuthManager.updateUserAvatar(activity, savedAvatar);

                        String currentName = profilePrefs.getString(com.devflux.deenone.core.auth.AuthManager.KEY_FULL_NAME,
                                profilePrefs.getString("user_full_name", "দ্বীনওয়ান ব্যবহারকারী"));

                        UserProfile updatedProfile = new UserProfile(
                                userId,
                                currentName,
                                "", "",
                                savedAvatar, -1,
                                com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(activity),
                                profilePrefs.getLong("user_joined_timestamp", System.currentTimeMillis()),
                                profilePrefs.getString("user_location", "Asia/Dhaka"),
                                true
                        );
                        backend.updateUserProfile(updatedProfile, null);

                        mainHandler.post(() -> {
                            safeDismissDialog(progressDialog, activity);
                            Toast.makeText(activity, "প্রোফাইল ছবি সফলভাবে আপলোড ও আপডেট হয়েছে!", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onPhotoUpdated(savedAvatar, circularForCallback);
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Offline or network fallback -> Keep local cache
                        SharedPreferences profilePrefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
                        profilePrefs.edit()
                                .putString(KEY_USER_AVATAR_URI, fileToUpload.getAbsolutePath())
                                .putString(com.devflux.deenone.core.auth.AuthManager.KEY_AVATAR, fileToUpload.getAbsolutePath())
                                .putInt(KEY_USER_AVATAR_PRESET, -1)
                                .apply();

                        com.devflux.deenone.core.auth.AuthManager.updateUserAvatar(activity, fileToUpload.getAbsolutePath());

                        mainHandler.post(() -> {
                            safeDismissDialog(progressDialog, activity);
                            Toast.makeText(activity, "ছবি লোকাল স্টোরেজে সংরক্ষিত হয়েছে (অফলাইন মোড)", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onPhotoUpdated(fileToUpload.getAbsolutePath(), circularForCallback);
                            }
                        });
                    }
                });

            } catch (Throwable e) {
                mainHandler.post(() -> {
                    safeDismissDialog(progressDialog, activity);
                    Toast.makeText(activity, "ছবি প্রক্রিয়াকরণ ত্রুটি: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private static void safeDismissDialog(ProgressDialog dialog, Activity activity) {
        try {
            if (dialog != null && dialog.isShowing() && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                dialog.dismiss();
            }
        } catch (Exception ignored) {}
    }

    private static Bitmap decodeAndDownsampleImage(Context context, Uri uri, int reqSize) {
        try (InputStream is1 = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is1, null, options);

            options.inSampleSize = calculateInSampleSize(options, reqSize, reqSize);
            options.inJustDecodeBounds = false;

            try (InputStream is2 = context.getContentResolver().openInputStream(uri)) {
                return BitmapFactory.decodeStream(is2, null, options);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap cropToSquare(Bitmap srcBmp) {
        if (srcBmp == null) return null;
        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();
        int newDim = Math.min(width, height);

        int cropX = (width - newDim) / 2;
        int cropY = (height - newDim) / 2;

        return Bitmap.createBitmap(srcBmp, cropX, cropY, newDim, newDim);
    }

    public static Bitmap getCircularCroppedBitmap(Bitmap bitmap, int diameter) {
        if (bitmap == null) return null;
        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return output;
    }

    private static void showPresetAvatarDialog(Activity activity, OnPhotoUpdatedListener listener) {
        String[] presetNames = {
                "দ্বীনি শিক্ষার্থী (ডিফল্ট)",
                "নূরানী মিনার এম্বলম",
                "মসজিদুল হারাম স্টাইল",
                "ঈমানী তারকা প্রতীক",
                "রূহানী আলো ও স্পার্কল",
                "মুনাজাত ও বন্দেগী আর্ট"
        };

        new AlertDialog.Builder(activity)
                .setTitle("প্রিসেট ইসলামিক এভাটার নির্বাচন")
                .setItems(presetNames, (d, index) -> {
                    String presetName = "avatar_" + (index + 1);
                    SharedPreferences prefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
                    prefs.edit()
                            .putString(KEY_USER_AVATAR_URI, presetName)
                            .putString(com.devflux.deenone.core.auth.AuthManager.KEY_AVATAR, presetName)
                            .putInt(KEY_USER_AVATAR_PRESET, index)
                            .apply();

                    com.devflux.deenone.core.auth.AuthManager.updateUserAvatar(activity, presetName);

                    String userId = getActiveUserId(activity);
                    int totalPoints = com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(activity);
                    UserProfile profile = new UserProfile(
                            userId, prefs.getString("user_full_name", "ব্যবহারকারী"),
                            "", "", presetName, index, totalPoints,
                            prefs.getLong("user_joined_timestamp", System.currentTimeMillis()),
                            prefs.getString("user_location", "Asia/Dhaka"), false
                    );
                    BackendRepository.getInstance(activity).updateUserProfile(profile, null);

                    Toast.makeText(activity, "এভাটার সফলভাবে পরিবর্তন করা হয়েছে", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onPhotoUpdated(presetName, null);
                    }
                })
                .setNegativeButton("বাতিল", null)
                .show();
    }

    private static void resetToDefaultAvatar(Activity activity, OnPhotoUpdatedListener listener) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_USER_AVATAR_URI, "avatar_1")
                .putString(com.devflux.deenone.core.auth.AuthManager.KEY_AVATAR, "avatar_1")
                .putInt(KEY_USER_AVATAR_PRESET, 0)
                .apply();

        com.devflux.deenone.core.auth.AuthManager.updateUserAvatar(activity, "avatar_1");

        String userId = getActiveUserId(activity);
        int totalPoints = com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(activity);
        UserProfile profile = new UserProfile(
                userId, prefs.getString("user_full_name", "ব্যবহারকারী"),
                "", "", "avatar_1", 0, totalPoints,
                prefs.getLong("user_joined_timestamp", System.currentTimeMillis()),
                prefs.getString("user_location", "Asia/Dhaka"), false
        );
        BackendRepository.getInstance(activity).updateUserProfile(profile, null);

        Toast.makeText(activity, "ডিফল্ট এভাটার সক্রিয় করা হয়েছে", Toast.LENGTH_SHORT).show();
        if (listener != null) {
            listener.onPhotoUpdated("avatar_1", null);
        }
    }

    private static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void loadAvatarIntoImageView(Context context, ImageView imageView, String avatarUriStr, int fallbackResId) {
        if (imageView == null) return;
        if (context == null) {
            imageView.setImageTintList(null);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(fallbackResId > 0 ? fallbackResId : R.drawable.ic_user_circle_avatar);
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);

        if (avatarUriStr == null || avatarUriStr.trim().isEmpty() || "avatar_1".equals(avatarUriStr) || "default".equalsIgnoreCase(avatarUriStr)) {
            avatarUriStr = prefs.getString(KEY_USER_AVATAR_URI, prefs.getString(com.devflux.deenone.core.auth.AuthManager.KEY_AVATAR, null));
        }

        int presetIndex = prefs.getInt("user_avatar_preset", -1);
        if (presetIndex >= 0 && (avatarUriStr == null || avatarUriStr.trim().isEmpty() || "avatar_1".equals(avatarUriStr) || "default".equalsIgnoreCase(avatarUriStr))) {
            int[] presets = {
                R.drawable.ic_user_circle_avatar,
                R.drawable.ic_deenone_emblem,
                R.drawable.ic_feat_mosque,
                R.drawable.ic_star,
                R.drawable.ic_sparkle,
                R.drawable.ic_hands_praying
            };
            if (presetIndex < presets.length) {
                imageView.setImageTintList(null);
                imageView.setPadding(0, 0, 0, 0);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(presets[presetIndex]);
                return;
            }
        }

        if (avatarUriStr == null || avatarUriStr.trim().isEmpty() || "avatar_1".equals(avatarUriStr)) {
            imageView.setImageTintList(null);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(fallbackResId > 0 ? fallbackResId : R.drawable.ic_user_circle_avatar);
            return;
        }

        if (avatarUriStr.startsWith("avatar_")) {
            imageView.setImageTintList(null);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_user_circle_avatar);
            return;
        }

        // 1. Check local file
        File file = new File(avatarUriStr);
        if (file.exists() && file.length() > 0) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    Bitmap circular = getCircularCroppedBitmap(bitmap, 250);
                    imageView.setImageTintList(null);
                    imageView.setPadding(0, 0, 0, 0);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(circular);
                    return;
                }
            } catch (Exception ignored) {}
        }

        // 2. Check content:// or file:// URI
        if (avatarUriStr.startsWith("content://") || avatarUriStr.startsWith("file://")) {
            try {
                Uri uri = Uri.parse(avatarUriStr);
                InputStream is = context.getContentResolver().openInputStream(uri);
                if (is != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    if (bitmap != null) {
                        Bitmap circular = getCircularCroppedBitmap(bitmap, 250);
                        imageView.setImageTintList(null);
                        imageView.setPadding(0, 0, 0, 0);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(circular);
                        return;
                    }
                }
            } catch (Exception ignored) {}
        }

        // 3. Remote URL
        final String remoteUrl;
        if (avatarUriStr.startsWith("http://") || avatarUriStr.startsWith("https://")) {
            remoteUrl = avatarUriStr;
        } else if (avatarUriStr.startsWith("uploads/")) {
            remoteUrl = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(context, "").replace("/api/", "/") + avatarUriStr;
        } else {
            remoteUrl = null;
        }

        if (remoteUrl != null) {
            File avatarDir = new File(context.getFilesDir(), "profile_avatars");
            if (!avatarDir.exists()) avatarDir.mkdirs();
            File diskCacheFile = new File(avatarDir, "cache_" + Math.abs(remoteUrl.hashCode()) + ".img");

            if (diskCacheFile.exists() && diskCacheFile.length() > 0) {
                try {
                    Bitmap cachedBmp = BitmapFactory.decodeFile(diskCacheFile.getAbsolutePath());
                    if (cachedBmp != null) {
                        Bitmap circular = getCircularCroppedBitmap(cachedBmp, 250);
                        imageView.setImageTintList(null);
                        imageView.setPadding(0, 0, 0, 0);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(circular);
                        return;
                    }
                } catch (Exception ignored) {}
            }

            imageView.setImageTintList(null);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(fallbackResId > 0 ? fallbackResId : R.drawable.ic_user_circle_avatar);

            executor.execute(() -> {
                try {
                    java.net.URL url = new java.net.URL(remoteUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "DeenOne-App/1.0 (Android)");
                    conn.setInstanceFollowRedirects(true);
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        try (InputStream is = conn.getInputStream();
                             FileOutputStream fos = new FileOutputStream(diskCacheFile)) {
                            byte[] buffer = new byte[8192];
                            int read;
                            while ((read = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, read);
                            }
                            fos.flush();
                        }
                        Bitmap bmp = BitmapFactory.decodeFile(diskCacheFile.getAbsolutePath());
                        if (bmp != null) {
                            Bitmap circular = getCircularCroppedBitmap(bmp, 250);
                            mainHandler.post(() -> {
                                if (imageView != null) {
                                    imageView.setImageTintList(null);
                                    imageView.setPadding(0, 0, 0, 0);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    imageView.setImageBitmap(circular);
                                }
                            });
                        }
                    }
                } catch (Exception ignored) {}
            });
            return;
        }

        imageView.setImageTintList(null);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(fallbackResId > 0 ? fallbackResId : R.drawable.ic_user_circle_avatar);
    }
}
