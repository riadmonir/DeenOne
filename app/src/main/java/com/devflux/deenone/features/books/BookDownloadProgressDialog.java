package com.devflux.deenone.features.books;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.DialogBookDownloadProgressBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BookDownloadProgressDialog {

    public interface OnDownloadCompleteListener {
        void onComplete(IslamicBookEntity book);
    }

    public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, OnDownloadCompleteListener onComplete) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        DialogBookDownloadProgressBinding binding =
                DialogBookDownloadProgressBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        boolean isBn = LocaleManager.isBengali(context);

        binding.tvProgressTitle.setText(isBn ? "ডাটাবেস ডাউনলোড হচ্ছে" : "Downloading Database");
        binding.tvProgressSubtitle.setText(isBn ? "নির্বাচিত ডাটাবেসের ডাউনলোড অগ্রগতি" : "Download progress of selected database");
        binding.tvProgressFieldLabel.setText(isBn ? "ডাটাবেস" : "Database");
        binding.tvProgressBookTitle.setText(book.getTitle() != null ? book.getTitle() : "");
        binding.btnDismissProgress.setText(isBn ? "বন্ধ করুন" : "Close");

        TouchAnimationUtil.attachTouchSpring(binding.btnDismissProgress);

        final boolean[] isDone = {false};

        // Smooth animated progress bar from 0 to 100%
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1200);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            binding.progressBarBookDownload.setProgress(progress);
            String pctText = isBn ? (BengaliNumberUtil.toBengali(progress) + "%") : (progress + "%");
            binding.tvProgressPercentText.setText(pctText);

            if (progress >= 100) {
                isDone[0] = true;
                binding.tvProgressStatusText.setText(isBn ? "সম্পন্ন" : "Completed");
                binding.ivProgressStatusIcon.setImageResource(R.drawable.ic_check_circle);
            } else {
                binding.tvProgressStatusText.setText(isBn ? "ডাউনলোড হচ্ছে..." : "Downloading...");
            }
        });

        binding.btnDismissProgress.setOnClickListener(v -> {
            dialog.dismiss();
            if (isDone[0] && onComplete != null) {
                onComplete.onComplete(book);
            }
        });

        dialog.show();
        animator.start();

        // Auto mark downloaded in background
        com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
            book.setDownloaded(true);
            book.setDownloadProgress(100);
            com.devflux.deenone.data.local.AppDatabase.getInstance(context)
                    .islamicBookDao()
                    .updateDownloadStatus(book.getId(), 100, true, "");
        });
    }
}
