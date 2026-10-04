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
import com.devflux.deenone.databinding.DialogRozaJumatulWidaDetailBinding;
import com.devflux.deenone.features.ramadan.model.RozaJumatulWidaItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * Production-ready full-screen Detail Dialog for individual Jumatul Wida topics.
 * Accurately implements the user screenshot with expandable card (বিস্তারিত ∨ / সংক্ষেপ করুন ∧),
 * 100% verbatim lines, dual-language mode, and spring touch on buttons ONLY.
 */
public class RozaJumatulWidaDetailDialog {

    public static void show(@NonNull Activity activity, @NonNull RozaJumatulWidaItem item) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogRozaJumatulWidaDetailBinding binding = DialogRozaJumatulWidaDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Spring touch physics on buttons ONLY (Rule 7: Strict zero touch animation on CardViews)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackJumatulWidaDetail);
        TouchAnimationUtil.attachTouchSpring(binding.layoutToggleDetailExpand);
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareDetail);

        // Back action
        binding.btnBackJumatulWidaDetail.setOnClickListener(v -> dialog.dismiss());

        // Title and Headings
        String title = item.getTitle(isBn);
        String topTitle = title;
        if (isBn) {
            if (topTitle.endsWith(" বিষয়ে")) {
                topTitle = topTitle.substring(0, topTitle.length() - " বিষয়ে".length()).trim();
            }
        } else {
            if (topTitle.startsWith("Regarding ")) {
                topTitle = topTitle.substring("Regarding ".length()).trim();
            }
        }
        binding.tvJumatulWidaDetailTitle.setText(topTitle);
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
                ClipData clip = ClipData.newPlainText("Jumatul Wida Topic", sb.toString());
                cm.setPrimaryClip(clip);
                Toast.makeText(activity, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
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

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share via"));
        });

        dialog.show();
    }

    private static void updateExpandState(DialogRozaJumatulWidaDetailBinding binding, boolean isExpanded, boolean isBn) {
        if (isExpanded) {
            binding.tvDetailPreview.setVisibility(View.GONE);
            binding.layoutExpandedDetail.setVisibility(View.VISIBLE);
            binding.tvToggleDetailText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            binding.ivToggleDetailChevron.setRotation(180f);
        } else {
            binding.tvDetailPreview.setVisibility(View.VISIBLE);
            binding.layoutExpandedDetail.setVisibility(View.GONE);
            binding.tvToggleDetailText.setText(isBn ? "বিস্তারিত" : "Read More");
            binding.ivToggleDetailChevron.setRotation(0f);
        }
    }
}
