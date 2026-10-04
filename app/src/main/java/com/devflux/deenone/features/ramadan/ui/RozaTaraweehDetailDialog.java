package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogRozaTaraweehDetailBinding;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * Production-ready full-screen Detail Dialog for individual Taraweeh topics.
 * Accurately implements the user screenshot with expandable card (বিস্তারিত ∨ / সংক্ষেপ করুন ∧),
 * 100% verbatim lines, dual-language mode, and smooth spring touches.
 */
public class RozaTaraweehDetailDialog {

    public static void show(@NonNull Activity activity, @NonNull RozaTaraweehItem item) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        if ("taraweeh_masail".equals(item.getSlug())) {
            RozaTaraweehMasailPageDialog.show(activity);
            return;
        }

        if ("taraweeh_for_women".equals(item.getSlug())) {
            RozaTaraweehWomenPageDialog.show(activity);
            return;
        }

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogRozaTaraweehDetailBinding binding = DialogRozaTaraweehDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Spring touch physics
        TouchAnimationUtil.attachTouchSpring(binding.btnBackTaraweehDetail);
        TouchAnimationUtil.attachTouchSpring(binding.layoutToggleDetailExpand);
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareDetail);

        // Back action
        binding.btnBackTaraweehDetail.setOnClickListener(v -> dialog.dismiss());

        // Title and Headings
        String title = item.getTitle(isBn);
        binding.tvTaraweehDetailTitle.setText(title);
        binding.tvDetailHeading.setText(title);

        // Preview & Full Text
        binding.tvDetailPreview.setText(item.getPreview(isBn));
        binding.tvDetailFullText.setText(item.getDetails(isBn));

        // Reference
        String ref = item.getReference(isBn);
        if (ref != null && !ref.trim().isEmpty()) {
            binding.tvDetailReference.setVisibility(View.VISIBLE);
            binding.tvDetailReference.setText((isBn ? "রেফারেন্স: " : "Reference: ") + ref);
        } else {
            binding.tvDetailReference.setVisibility(View.GONE);
        }

        binding.tvCopyDetailText.setText(isBn ? "কপি" : "Copy");
        binding.tvShareDetailText.setText(isBn ? "শেয়ার" : "Share");

        // Initial state
        updateExpandState(binding, item.isExpanded(), isBn);

        // Toggle Expand / Collapse
        binding.layoutToggleDetailExpand.setOnClickListener(v -> {
            boolean newState = !item.isExpanded();
            item.setExpanded(newState);
            binding.ivToggleDetailChevron.animate()
                    .rotation(newState ? 180f : 0f)
                    .setDuration(200)
                    .start();
            updateExpandState(binding, newState, isBn);
        });

        // Copy Action (Rule 8: allowed for clipboard copy confirmation)
        binding.btnCopyDetail.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append(title).append("\n\n");
            sb.append(item.getDetails(isBn)).append("\n\n");
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append((isBn ? "রেফারেন্স: " : "Reference: ")).append(ref).append("\n\n");
            }
            sb.append(isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");

            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                ClipData clip = ClipData.newPlainText(title, sb.toString().trim());
                cm.setPrimaryClip(clip);
                Toast.makeText(activity, isBn ? "ক্লিপবোর্ডে কপি সম্পন্ন হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        // Share Action
        binding.btnShareDetail.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append(title).append("\n\n");
            sb.append(item.getDetails(isBn)).append("\n\n");
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append((isBn ? "রেফারেন্স: " : "Reference: ")).append(ref).append("\n\n");
            }
            sb.append(isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
            activity.startActivity(Intent.createChooser(shareIntent, isBn ? "শেয়ার করুন" : "Share via"));
        });

        dialog.show();
    }

    private static void updateExpandState(DialogRozaTaraweehDetailBinding binding, boolean isExpanded, boolean isBn) {
        if (isExpanded) {
            binding.tvDetailPreview.setVisibility(View.GONE);
            binding.layoutExpandedDetail.setVisibility(View.VISIBLE);
            binding.tvToggleDetailText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            binding.ivToggleDetailChevron.setRotation(180f);
        } else {
            binding.tvDetailPreview.setVisibility(View.VISIBLE);
            binding.layoutExpandedDetail.setVisibility(View.GONE);
            binding.tvToggleDetailText.setText(isBn ? "বিস্তারিত" : "Details");
            binding.ivToggleDetailChevron.setRotation(0f);
        }
    }
}
