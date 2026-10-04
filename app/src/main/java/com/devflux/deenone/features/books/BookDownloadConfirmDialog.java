package com.devflux.deenone.features.books;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.DialogBookDownloadConfirmBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BookDownloadConfirmDialog {

    public interface OnConfirmListener {
        void onConfirmed(IslamicBookEntity book);
    }

    public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, OnConfirmListener listener) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogBookDownloadConfirmBinding binding =
                DialogBookDownloadConfirmBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        boolean isBn = LocaleManager.isBengali(context);

        // Header & Body Text
        binding.tvDownloadConfirmTitle.setText(isBn ? "বই ডাউনলোড করুন" : "Download Book");
        String bookTitle = book.getTitle() != null ? book.getTitle() : "";
        String msg = isBn
                ? ("আপনি কি ডাউনলোড করতে চান \"" + bookTitle + "\"? এটি বইটি আপনার ডিভাইসে ডাউনলোড করবে।")
                : ("Do you want to download \"" + bookTitle + "\"? This will download the book to your device.");
        binding.tvDownloadConfirmBody.setText(msg);

        binding.btnDownloadCancel.setText(isBn ? "না" : "No");
        binding.btnDownloadConfirm.setText(isBn ? "হ্যাঁ" : "Yes");

        // Touch spring strictly on action buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnDownloadCancel);
        TouchAnimationUtil.attachTouchSpring(binding.btnDownloadConfirm);

        binding.btnDownloadCancel.setOnClickListener(v -> dialog.dismiss());

        binding.btnDownloadConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.onConfirmed(book);
            }
        });

        dialog.show();
    }
}
