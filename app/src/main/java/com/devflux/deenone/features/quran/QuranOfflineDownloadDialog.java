package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.core.quran.QuranOfflineDownloadManager;
import com.devflux.deenone.databinding.DialogNoInternetCardBinding;
import com.devflux.deenone.databinding.DialogQuranDownloadCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * 100% Visual Fidelity Quran Offline Download Card Dialog.
 * Displays accurate dialog matching user specification:
 * - Circular Book Icon Header
 * - Pure Bengali/English Localized Headings & Subtitles (Zero Bracket Pollution)
 * - Size Pill Box ("আকার: ৬.৮৭ MB" / "Size: 6.87 MB")
 * - Interactive In-Button Smooth Download Progress
 * - "বাতিল করুন" (Cancel) bypasses download directly to Online Streaming Mode.
 */
public class QuranOfflineDownloadDialog extends Dialog {

    private final DialogQuranDownloadCardBinding binding;
    private final boolean isBn;
    private final Runnable onCompleteOfflineCallback;
    private final Runnable onPlayOnlineCallback;
    private final QuranOfflineDownloadManager downloadManager;

    /**
     * Show download prompt if full Quran ayahs are not yet stored in Room DB.
     */
    public static void showIfNeeded(Activity activity, Runnable onCompleteOffline) {
        showIfNeeded(activity, onCompleteOffline, null);
    }

    /**
     * Show download prompt if full Quran ayahs are not yet stored in Room DB.
     * If user selects Cancel (বাতিল), it triggers onPlayOnline to stream online.
     */
    public static void showIfNeeded(Activity activity, Runnable onCompleteOffline, Runnable onPlayOnline) {
        QuranUnifiedDownloadDialog.showIfNeeded(activity, onCompleteOffline, onPlayOnline);
    }

    /**
     * Show network connection prompt matching the exact card design.
     */
    public static void showNoInternetDialog(Activity activity, Runnable onRetry) {
        if (activity == null || activity.isFinishing()) return;

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogNoInternetCardBinding b = DialogNoInternetCardBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(b.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        boolean isBn = LocaleManager.isBengali(activity);
        b.tvNoInternetTitle.setText(isBn ? "ইন্টারনেট সংযোগ প্রয়োজন" : "Internet Connection Required");
        b.tvNoInternetSubtitle.setText(isBn
                ? "ইন্টারনেট সংযোগ বন্ধ রয়েছে। অনলাইন থেকে লোড করতে ইন্টারনেট চালু করুন অথবা অফলাইন ডাটা ডাউনলোড করুন।"
                : "Internet connection is unavailable. Please enable mobile data/Wi-Fi or download offline data.");
        b.tvRetryBtnText.setText(isBn ? "পুনরায় চেষ্টা করুন" : "Retry Connection");
        b.btnCloseNoInternet.setText(isBn ? "বাতিল করুন" : "Cancel");

        TouchAnimationUtil.attachTouchSpring(b.btnRetryInternet);
        TouchAnimationUtil.attachTouchSpring(b.btnCloseNoInternet);

        b.btnRetryInternet.setOnClickListener(v -> {
            dialog.dismiss();
            if (NetworkConnectivityHelper.isOnline(activity)) {
                if (onRetry != null) onRetry.run();
            } else {
                try {
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                } catch (Exception e) {
                    try {
                        activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    } catch (Exception ignored) {}
                }
            }
        });

        b.btnCloseNoInternet.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public QuranOfflineDownloadDialog(@NonNull Context context, Runnable onCompleteOffline, Runnable onPlayOnline) {
        super(context);
        this.onCompleteOfflineCallback = onCompleteOffline;
        this.onPlayOnlineCallback = onPlayOnline;
        this.isBn = LocaleManager.isBengali(context);
        this.downloadManager = QuranOfflineDownloadManager.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogQuranDownloadCardBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        setupLocalizedText();
        setupListeners();
    }

    private void setupLocalizedText() {
        binding.tvQuranDownloadTitle.setText(isBn ? "কুরআন ডাউনলোড করুন" : "Download Quran");
        binding.tvQuranDownloadSubtitle.setText(isBn
                ? "কুরআন পড়ার জন্য প্রথমে ডাটাবেস ডাউনলোড করতে হবে"
                : "Database download required for reading and listening");
        binding.tvDownloadSize.setText(isBn ? "আকার: ৬.৮৭ MB" : "Size: 6.87 MB");
        binding.tvDownloadBtnText.setText(isBn ? "ডাউনলোড" : "Download");
        binding.btnCancelQuranDownload.setText(isBn ? "বাতিল করুন" : "Cancel");
    }

    private void setupListeners() {
        // Rule 7: Touch animation strictly on buttons
        TouchAnimationUtil.attachTouchSpring(binding.btnStartQuranDownload);
        TouchAnimationUtil.attachTouchSpring(binding.btnCancelQuranDownload);

        // Cancel / Skip to Online Playback Mode
        binding.btnCancelQuranDownload.setOnClickListener(v -> {
            if (downloadManager.isCurrentlyDownloading()) {
                downloadManager.cancelDownload();
            }
            dismiss();
            if (onPlayOnlineCallback != null) {
                onPlayOnlineCallback.run();
            }
        });

        // Start Download
        binding.btnStartQuranDownload.setOnClickListener(v -> startDownload());
    }

    private void startDownload() {
        binding.layoutDownloadProgress.setVisibility(View.VISIBLE);
        binding.btnStartQuranDownload.setEnabled(false);
        binding.btnStartQuranDownload.setAlpha(0.85f);
        binding.ivDownloadBtnIcon.setVisibility(View.GONE);

        String initialStatus = isBn ? "ডাউনলোড প্রস্তুতি চলছে..." : "Preparing download...";
        binding.tvDownloadProgressStatus.setText(initialStatus);
        binding.tvDownloadProgressPercent.setText(isBn ? "০%" : "0%");
        binding.tvDownloadBtnText.setText(isBn ? "ডাউনলোড হচ্ছে... ০%" : "Downloading... 0%");
        binding.progressBarDownload.setProgress(0);

        downloadManager.startFullQuranDownload(getContext(), new QuranOfflineDownloadManager.DownloadListener() {
            @Override
            public void onProgress(int completedSurahs, int totalSurahs, int progressPercent, String currentSurahBn, String currentSurahEn) {
                binding.progressBarDownload.setProgress(progressPercent);
                String pctStr = isBn ? (BengaliNumberUtil.toBengali(progressPercent) + "%") : (progressPercent + "%");
                binding.tvDownloadProgressPercent.setText(pctStr);
                binding.tvDownloadBtnText.setText(isBn ? ("ডাউনলোড হচ্ছে... " + pctStr) : ("Downloading... " + pctStr));

                String surahName = isBn ? currentSurahBn : currentSurahEn;
                String doneStr = isBn ? BengaliNumberUtil.toBengali(completedSurahs) : String.valueOf(completedSurahs);
                String totalStr = isBn ? BengaliNumberUtil.toBengali(totalSurahs) : String.valueOf(totalSurahs);

                String status = isBn
                        ? ("সূরা: " + surahName + " (" + doneStr + "/" + totalStr + ")")
                        : ("Surah: " + surahName + " (" + doneStr + "/" + totalStr + ")");
                binding.tvDownloadProgressStatus.setText(status);
            }

            @Override
            public void onSuccess() {
                dismiss();
                if (onCompleteOfflineCallback != null) {
                    onCompleteOfflineCallback.run();
                }
            }

            @Override
            public void onError(String errorMessage) {
                binding.btnStartQuranDownload.setEnabled(true);
                binding.btnStartQuranDownload.setAlpha(1.0f);
                binding.ivDownloadBtnIcon.setVisibility(View.VISIBLE);
                binding.tvDownloadBtnText.setText(isBn ? "পুনরায় ডাউনলোড করুন" : "Retry Download");
                binding.layoutDownloadProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled() {
                binding.btnStartQuranDownload.setEnabled(true);
                binding.btnStartQuranDownload.setAlpha(1.0f);
                binding.ivDownloadBtnIcon.setVisibility(View.VISIBLE);
                binding.tvDownloadBtnText.setText(isBn ? "ডাউনলোড" : "Download");
                binding.layoutDownloadProgress.setVisibility(View.GONE);
            }
        });
    }
}
