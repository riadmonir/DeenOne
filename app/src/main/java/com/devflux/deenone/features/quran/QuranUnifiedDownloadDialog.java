package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranUnifiedDownloadManager;
import com.devflux.deenone.databinding.DialogQuranDownloadCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * QuranUnifiedDownloadDialog — 100% Visual Fidelity Unified Download Dialog for both
 * Quran Section (Quran Majeed) and Quran Sleep Section (Sleep Mode).
 *
 * Requirements:
 * 1. Checks global offline download status before showing.
 * 2. If user cancels ("বাতিল করুন"), marks prompt as dismissed so it is NEVER shown again
 *    on subsequent visits into either Quran Sleep Mode or Quran Majeed.
 * 3. If user clicks "ডাউনলোড", starts download in the background with notification controls
 *    (Pause/Resume/Cancel) and proceeds into the section immediately.
 */
public class QuranUnifiedDownloadDialog extends Dialog {

    private final DialogQuranDownloadCardBinding binding;
    private final boolean isBn;
    private final Runnable onProceedCallback;
    private QuranUnifiedDownloadManager.DownloadListener liveListener;

    /**
     * Shows the download dialog ONLY if not yet downloaded and the user has not previously dismissed it.
     */
    public static void showIfNeeded(Activity activity, Runnable onProceed) {
        showIfNeeded(activity, onProceed, onProceed);
    }

    public static void showIfNeeded(Activity activity, Runnable onCompleteOffline, Runnable onPlayOnline) {
        if (activity == null || activity.isFinishing()) return;

        if (QuranUnifiedDownloadManager.isAllSurahsDownloaded(activity)
                || QuranUnifiedDownloadManager.hasUserDismissedPrompt(activity)) {
            if (onCompleteOffline != null) onCompleteOffline.run();
            return;
        }

        QuranUnifiedDownloadDialog dialog = new QuranUnifiedDownloadDialog(activity, onCompleteOffline);
        dialog.show();
    }

    /**
     * Directly show the dialog (e.g., from an offline settings card or manual trigger).
     */
    public static void show(Activity activity, Runnable onProceed) {
        if (activity == null || activity.isFinishing()) return;
        QuranUnifiedDownloadDialog dialog = new QuranUnifiedDownloadDialog(activity, onProceed);
        dialog.show();
    }

    public QuranUnifiedDownloadDialog(@NonNull Context context, Runnable onProceed) {
        super(context);
        this.onProceedCallback = onProceed;
        this.isBn = LocaleManager.isBengali(context);

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
        bindOngoingDownloadIfActive();
    }

    private void setupLocalizedText() {
        binding.tvQuranDownloadTitle.setText(isBn ? "কুরআন অডিও ডাউনলোড" : "Download Quran Audio");
        binding.tvQuranDownloadSubtitle.setText(isBn
                ? "ইন্টারনেট ছাড়া সম্পূর্ণ ১১৪টি সূরা অফলাইনে শোনার জন্য অডিও ডাউনলোড করুন"
                : "Download complete 114 Surahs to listen offline without internet");
        binding.tvDownloadSize.setText(isBn ? "আকার: ১১৪টি পূর্ণাঙ্গ সূরা (~৫৭০ MB)" : "Size: 114 Complete Surahs (~570 MB)");
        binding.tvDownloadBtnText.setText(isBn ? "ডাউনলোড" : "Download");
        binding.btnCancelQuranDownload.setText(isBn ? "বাতিল করুন" : "Cancel");
    }

    private void setupListeners() {
        // Rule 7: Touch animation strictly on buttons
        TouchAnimationUtil.attachTouchSpring(binding.btnStartQuranDownload);
        TouchAnimationUtil.attachTouchSpring(binding.btnCancelQuranDownload);

        // Cancel / Dismiss: Mark as dismissed so prompt never appears again, then proceed directly
        binding.btnCancelQuranDownload.setOnClickListener(v -> {
            QuranUnifiedDownloadManager.markPromptDismissed(getContext());
            dismiss();
            if (onProceedCallback != null) {
                onProceedCallback.run();
            }
        });

        // Start Download: Mark prompt dismissed, start background download, dismiss and proceed immediately
        binding.btnStartQuranDownload.setOnClickListener(v -> {
            QuranUnifiedDownloadManager.markPromptDismissed(getContext());
            QuranUnifiedDownloadManager.getInstance().startFullDownload(getContext().getApplicationContext(), null);
            dismiss();
            if (onProceedCallback != null) {
                onProceedCallback.run();
            }
        });
    }

    private void bindOngoingDownloadIfActive() {
        QuranUnifiedDownloadManager manager = QuranUnifiedDownloadManager.getInstance();
        if (manager.isCurrentlyDownloading()) {
            binding.layoutDownloadProgress.setVisibility(View.VISIBLE);
            binding.btnStartQuranDownload.setEnabled(false);
            binding.btnStartQuranDownload.setAlpha(0.85f);
            binding.ivDownloadBtnIcon.setVisibility(View.GONE);

            liveListener = new QuranUnifiedDownloadManager.DownloadListener() {
                @Override
                public void onProgress(QuranUnifiedDownloadManager.DownloadProgress progress) {
                    if (binding == null) return;
                    binding.progressBarDownload.setProgress(progress.progressPercent);
                    String pctStr = isBn ? (BengaliNumberUtil.toBengali(progress.progressPercent) + "%") : (progress.progressPercent + "%");
                    binding.tvDownloadProgressPercent.setText(pctStr);
                    binding.tvDownloadBtnText.setText(isBn ? ("ডাউনলোড হচ্ছে... " + pctStr) : ("Downloading... " + pctStr));

                    String surahName = isBn ? progress.currentSurahBn : progress.currentSurahEn;
                    String doneStr = isBn ? BengaliNumberUtil.toBengali(progress.completedSurahs) : String.valueOf(progress.completedSurahs);
                    String totalStr = isBn ? BengaliNumberUtil.toBengali(progress.totalSurahs) : String.valueOf(progress.totalSurahs);
                    String status = isBn
                            ? ("সূরা: " + surahName + " (" + doneStr + "/" + totalStr + ")")
                            : ("Surah: " + surahName + " (" + doneStr + "/" + totalStr + ")");
                    binding.tvDownloadProgressStatus.setText(status);
                }

                @Override
                public void onSuccess() {
                    dismiss();
                    if (onProceedCallback != null) {
                        onProceedCallback.run();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    resetButtonUi(isBn ? "পুনরায় চেষ্টা করুন" : "Retry Download");
                }

                @Override
                public void onCancelled() {
                    resetButtonUi(isBn ? "ডাউনলোড" : "Download");
                }

                @Override
                public void onPaused() {
                    binding.tvDownloadProgressStatus.setText(isBn ? "ডাউনলোড স্থগিত আছে" : "Download Paused");
                }

                @Override
                public void onResumed() {
                    binding.tvDownloadProgressStatus.setText(isBn ? "ডাউনলোড চলছে..." : "Downloading...");
                }
            };
            manager.addListener(liveListener);
        }
    }

    private void resetButtonUi(String buttonText) {
        if (binding == null) return;
        binding.btnStartQuranDownload.setEnabled(true);
        binding.btnStartQuranDownload.setAlpha(1.0f);
        binding.ivDownloadBtnIcon.setVisibility(View.VISIBLE);
        binding.tvDownloadBtnText.setText(buttonText);
        binding.layoutDownloadProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (liveListener != null) {
            QuranUnifiedDownloadManager.getInstance().removeListener(liveListener);
            liveListener = null;
        }
    }
}
