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
import com.devflux.deenone.databinding.DialogRozaHealthAdviceDetailBinding;
import com.devflux.deenone.features.ramadan.model.RozaHealthAdviceItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * Production-ready Detail Dialog for individual Roza Health Advice topics.
 * 100% verbatim matching user screenshot with expandable card (বিস্তারিত ∨ / সংক্ষেপ করুন ∧),
 * clean dual-language mode, and touch physics on buttons ONLY.
 */
public class RozaHealthAdviceDetailDialog {

    public static void show(@NonNull Activity activity, @NonNull RozaHealthAdviceItem item) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogRozaHealthAdviceDetailBinding binding =
                DialogRozaHealthAdviceDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Spring touch physics on buttons ONLY (Rule 7: Strict zero touch animation on CardViews)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackRozaHealthAdviceDetail);
        TouchAnimationUtil.attachTouchSpring(binding.layoutToggleDetailExpand);
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareDetail);

        // Back action
        binding.btnBackRozaHealthAdviceDetail.setOnClickListener(v -> dialog.dismiss());

        // Header Title & Card Heading (Verbatim from Screenshot)
        String headerTitle = item.getHeaderTitle(isBn);
        String cardTitle = item.getCardTitle(isBn);

        binding.tvRozaHealthAdviceDetailTitle.setText(headerTitle);
        binding.tvDetailHeading.setText(cardTitle);

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

        // Initial expand state
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

        // Copy Action (Rule 8: allowed for clipboard confirmation only)
        binding.btnCopyDetail.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append(headerTitle).append("\n");
            sb.append(cardTitle).append("\n\n");
            sb.append(item.getDetails(isBn)).append("\n\n");
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append((isBn ? "রেফারেন্স: " : "Reference: ")).append(ref).append("\n\n");
            }
            sb.append(isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");

            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                ClipData clip = ClipData.newPlainText("Roza Health Advice", sb.toString());
                cm.setPrimaryClip(clip);
                Toast.makeText(activity, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        // Share Action
        binding.btnShareDetail.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append(headerTitle).append("\n");
            sb.append(cardTitle).append("\n\n");
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

    private static void updateExpandState(DialogRozaHealthAdviceDetailBinding binding, boolean isExpanded, boolean isBn) {
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
