package com.devflux.deenone.features.ramadan.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaInfoDetailBinding;
import com.devflux.deenone.features.ramadan.model.RozaInfoItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class RozaInfoDetailDialog {

    public static void show(@NonNull AppCompatActivity activity, @NonNull RozaInfoItem item) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaInfoDetailBinding binding = PageRozaInfoDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        TouchAnimationUtil.attachTouchSpring(binding.btnBackRozaInfoDetail);
        TouchAnimationUtil.attachTouchSpring(binding.layoutToggleDetailExpand);
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareDetail);

        binding.btnBackRozaInfoDetail.setOnClickListener(v -> dialog.dismiss());

        String title = item.getTitle(isBn);
        binding.tvRozaInfoDetailTitle.setText(title);
        binding.tvDetailHeading.setText(title);

        binding.tvDetailPreview.setText(item.getPreviewText(isBn));
        binding.tvDetailFullText.setText(item.getFullContent(isBn));

        String ref = item.getReference(isBn);
        if (ref != null && !ref.trim().isEmpty()) {
            binding.tvDetailReference.setVisibility(View.VISIBLE);
            binding.tvDetailReference.setText((isBn ? "রেফারেন্স: " : "Reference: ") + ref);
        } else {
            binding.tvDetailReference.setVisibility(View.GONE);
        }

        binding.tvCopyDetailText.setText(isBn ? "কপি" : "Copy");
        binding.tvShareDetailText.setText(isBn ? "শেয়ার" : "Share");

        // Initial expand state
        updateExpandState(binding, item.isExpanded(), isBn);

        binding.layoutToggleDetailExpand.setOnClickListener(v -> {
            boolean newState = !item.isExpanded();
            item.setExpanded(newState);
            binding.ivToggleDetailChevron.animate()
                    .rotation(newState ? 180f : 0f)
                    .setDuration(200)
                    .start();
            updateExpandState(binding, newState, isBn);
        });

        binding.btnCopyDetail.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                String copyContent = title + "\n\n"
                        + item.getFullContent(isBn)
                        + (ref != null && !ref.trim().isEmpty() ? "\n\n" + (isBn ? "রেফারেন্স: " : "Reference: ") + ref : "")
                        + "\n\n" + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
                ClipData clip = ClipData.newPlainText(title, copyContent);
                clipboard.setPrimaryClip(clip);
            }
        });

        binding.btnShareDetail.setOnClickListener(v -> {
            String shareContent = title + "\n\n"
                    + item.getFullContent(isBn)
                    + (ref != null && !ref.trim().isEmpty() ? "\n\n" + (isBn ? "রেফারেন্স: " : "Reference: ") + ref : "")
                    + "\n\n" + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share via"));
        });

        dialog.show();
    }

    private static void updateExpandState(PageRozaInfoDetailBinding binding, boolean isExpanded, boolean isBn) {
        if (isExpanded) {
            binding.layoutExpandedDetail.setVisibility(View.VISIBLE);
            binding.tvToggleDetailText.setText(isBn ? "সংক্ষিপ্ত" : "Collapse");
            binding.ivToggleDetailChevron.setRotation(180f);
        } else {
            binding.layoutExpandedDetail.setVisibility(View.GONE);
            binding.tvToggleDetailText.setText(isBn ? "বিস্তারিত" : "Details");
            binding.ivToggleDetailChevron.setRotation(0f);
        }
    }
}
