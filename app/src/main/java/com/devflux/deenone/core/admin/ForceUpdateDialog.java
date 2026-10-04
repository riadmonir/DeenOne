package com.devflux.deenone.core.admin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.DialogAppForceUpdateBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * ForceUpdateDialog — High-end Production-ready Dialog for Mandatory Force Updates
 * and Flexible App Updates remotely managed from the PHP Admin Panel.
 */
public class ForceUpdateDialog extends Dialog {

    private static ForceUpdateDialog activeDialog;

    public static void showIfNeeded(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        AdminRemoteConfigManager config = AdminRemoteConfigManager.getInstance(activity);
        int currentVersionCode = 1;
        try {
            currentVersionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (Exception ignored) {}

        boolean isForced = config.isForceUpdateRequired(currentVersionCode);
        boolean isAvailable = config.isUpdateAvailable(currentVersionCode);

        if (isForced) {
            show(activity, true);
        } else if (isAvailable) {
            // Optional update check with once-per-session limit
            android.content.SharedPreferences sp = activity.getSharedPreferences("deanone_update_prompt_prefs", Activity.MODE_PRIVATE);
            long lastPrompt = sp.getLong("last_optional_prompt_time", 0);
            long now = System.currentTimeMillis();
            if (now - lastPrompt > 86400000L) { // Prompt once a day
                sp.edit().putLong("last_optional_prompt_time", now).apply();
                show(activity, false);
            }
        }
    }

    public static void show(Activity activity, boolean isForced) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        if (activeDialog != null && activeDialog.isShowing()) {
            return;
        }

        ForceUpdateDialog dialog = new ForceUpdateDialog(activity, isForced);
        activeDialog = dialog;
        dialog.show();
    }

    public ForceUpdateDialog(@NonNull Activity activity, boolean isForced) {
        super(activity);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAppForceUpdateBinding binding = DialogAppForceUpdateBinding.inflate(LayoutInflater.from(activity));
        setContentView(binding.getRoot());

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        AdminRemoteConfigManager config = AdminRemoteConfigManager.getInstance(activity);
        boolean isBn = LocaleManager.isBengali(activity);

        String customTitle = config.getUpdateTitle();
        String customMsg = config.getUpdateMessage();
        String versionName = config.getLatestVersionName();
        String updateUrl = config.getUpdateUrl();

        if (customTitle != null && !customTitle.trim().isEmpty()) {
            binding.tvUpdateTitle.setText(customTitle);
        } else {
            binding.tvUpdateTitle.setText(isBn ? (isForced ? "নতুন সংস্করণ আপডেট আবশ্যক" : "নতুন সংস্করণ উপলব্ধ") : (isForced ? "Mandatory App Update" : "New Version Available"));
        }

        binding.tvVersionPill.setText(isBn ? ("ভার্সন: v" + versionName) : ("Version: v" + versionName));

        if (customMsg != null && !customMsg.trim().isEmpty()) {
            binding.tvUpdateMessage.setText(customMsg);
        } else {
            binding.tvUpdateMessage.setText(isBn
                    ? "গুরুত্বপূর্ণ নিরাপত্তা, বাগ সংশোধন ও নতুন ফিচারের জন্য অ্যাপটি এখনই আপডেট করুন।"
                    : "Please update the app now for critical security patches, bug fixes, and new features.");
        }

        binding.tvUpdateBtnText.setText(isBn ? "এখনই আপডেট করুন" : "Update Now");
        binding.btnUpdateLater.setText(isBn ? "পরে করব" : "Later");

        TouchAnimationUtil.attachTouchSpring(binding.btnUpdateNow);
        TouchAnimationUtil.attachTouchSpring(binding.btnUpdateLater);

        binding.btnUpdateNow.setOnClickListener(v -> {
            String targetUrl = updateUrl;
            if (targetUrl == null || targetUrl.trim().isEmpty()) {
                targetUrl = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
                activity.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName()));
                    activity.startActivity(webIntent);
                } catch (Exception ignored) {}
            }
        });

        if (isForced) {
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            binding.btnUpdateLater.setVisibility(View.GONE);
            setOnKeyListener((dialogInterface, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    activity.finishAffinity();
                    return true;
                }
                return false;
            });
        } else {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
            binding.btnUpdateLater.setVisibility(View.VISIBLE);
            binding.btnUpdateLater.setOnClickListener(v -> dismiss());
        }

        setOnDismissListener(d -> {
            if (activeDialog == this) {
                activeDialog = null;
            }
        });
    }
}
