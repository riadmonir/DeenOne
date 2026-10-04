package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageSalahHistoryDetailBinding;
import com.devflux.deenone.features.salahguide.data.SalahHistoryRepository;
import com.devflux.deenone.features.salahguide.model.SalahHistoryItem;
import com.devflux.deenone.features.salahguide.model.SalahHistorySection;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.card.MaterialCardView;

public class SalahHistoryDetailDialog {

    public static void show(Activity activity, String topicId) {
        if (activity == null || activity.isFinishing()) return;

        if ("history_five_waqts".equals(topicId)) {
            FiveWaqtHistoryPageDialog.show(activity);
            return;
        }

        if ("history_when_started".equals(topicId)) {
            WhenStartedHistoryPageDialog.show(activity);
            return;
        }

        if ("history_why_five_times".equals(topicId)) {
            WhyFiveTimesHistoryPageDialog.show(activity);
            return;
        }

        if ("history_fifty_to_five".equals(topicId)) {
            FiftyToFiveHistoryPageDialog.show(activity);
            return;
        }

        if ("history_fifty_reward".equals(topicId)) {
            FiftyRewardHistoryPageDialog.show(activity);
            return;
        }

        if ("history_prophets_salah".equals(topicId)) {
            ProphetsSalahHistoryPageDialog.show(activity);
            return;
        }

        SalahHistoryItem item = SalahHistoryRepository.getItem(topicId);
        if (item == null) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahHistoryDetailBinding binding = PageSalahHistoryDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);

        // Header Title and back button
        binding.tvHistoryDetailHeaderTitle.setText(item.getTitle());
        binding.btnBackHistoryDetail.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHistoryDetail);

        // Copy all button
        binding.btnCopyAllHistoryDetail.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append(item.getTitle()).append("\n\n");
            sb.append(item.getSummary()).append("\n\n");
            for (SalahHistorySection sec : item.getSections()) {
                sb.append("• ").append(sec.getHeading()).append("\n");
                if (!TextUtils.isEmpty(sec.getArabicText())) {
                    sb.append(sec.getArabicText()).append("\n");
                }
                sb.append(sec.getBengaliText()).append("\n");
                if (!TextUtils.isEmpty(sec.getReference())) {
                    sb.append(isBn ? "সূত্র: " : "Reference: ").append(sec.getReference()).append("\n");
                }
                sb.append("\n");
            }
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText(isBn ? "নামাজের ইতিহাস" : "Salah History", sb.toString().trim()));
                Toast.makeText(activity, isBn ? "সম্পূর্ণ ইতিহাস কপি করা হয়েছে" : "Full history copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyAllHistoryDetail);

        // Overview hero card
        binding.tvHistoryOverviewTitle.setText(item.getTitle());
        binding.badgeHistoryOverviewTag.setText(item.getBadge());
        binding.tvHistoryOverviewSummary.setText(item.getSummary());

        // Populate sections
        populateSections(activity, binding, item, isBn);

        dialog.show();
    }

    private static void populateSections(Context ctx, PageSalahHistoryDetailBinding binding, SalahHistoryItem item, boolean isBn) {
        binding.containerHistoryDetailSections.removeAllViews();
        if (ctx == null) return;

        for (SalahHistorySection section : item.getSections()) {
            MaterialCardView card = new MaterialCardView(ctx);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.bottomMargin = dpToPx(ctx, 12);
            card.setLayoutParams(cardParams);
            card.setRadius(dpToPx(ctx, 14));
            card.setCardElevation(0);
            card.setStrokeWidth(dpToPx(ctx, 1));
            card.setStrokeColor(ContextCompat.getColor(ctx, R.color.border_card));
            card.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.bg_card));

            LinearLayout body = new LinearLayout(ctx);
            body.setOrientation(LinearLayout.VERTICAL);
            body.setPadding(dpToPx(ctx, 16), dpToPx(ctx, 16), dpToPx(ctx, 16), dpToPx(ctx, 16));

            // Section Heading & Reference Row
            LinearLayout headerRow = new LinearLayout(ctx);
            headerRow.setOrientation(LinearLayout.HORIZONTAL);
            headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView tvHeading = new TextView(ctx);
            LinearLayout.LayoutParams headingParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tvHeading.setLayoutParams(headingParams);
            tvHeading.setText(section.getHeading());
            tvHeading.setTextSize(15.5f);
            tvHeading.setTypeface(null, android.graphics.Typeface.BOLD);
            tvHeading.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
            headerRow.addView(tvHeading);

            if (!TextUtils.isEmpty(section.getReference())) {
                TextView tvRef = new TextView(ctx);
                tvRef.setText(section.getReference());
                tvRef.setTextSize(11f);
                tvRef.setTextColor(ContextCompat.getColor(ctx, R.color.accent_mint));
                tvRef.setBackgroundResource(R.drawable.bg_badge_pill);
                tvRef.setPadding(dpToPx(ctx, 8), dpToPx(ctx, 3), dpToPx(ctx, 8), dpToPx(ctx, 3));
                headerRow.addView(tvRef);
            }
            body.addView(headerRow);

            // Arabic text calligraphic box (if available)
            if (!TextUtils.isEmpty(section.getArabicText())) {
                MaterialCardView arabicBox = new MaterialCardView(ctx);
                LinearLayout.LayoutParams arabParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                arabParams.topMargin = dpToPx(ctx, 10);
                arabicBox.setLayoutParams(arabParams);
                arabicBox.setRadius(dpToPx(ctx, 10));
                arabicBox.setCardElevation(0);
                arabicBox.setCardBackgroundColor(ContextCompat.getColor(ctx, R.color.bg_main));

                TextView tvArabic = new TextView(ctx);
                tvArabic.setText(section.getArabicText());
                tvArabic.setTextSize(16.5f);
                tvArabic.setLineSpacing(dpToPx(ctx, 4), 1f);
                tvArabic.setGravity(android.view.Gravity.RIGHT);
                tvArabic.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
                tvArabic.setPadding(dpToPx(ctx, 12), dpToPx(ctx, 10), dpToPx(ctx, 12), dpToPx(ctx, 10));
                arabicBox.addView(tvArabic);
                body.addView(arabicBox);
            }

            // Bengali Historical text
            if (!TextUtils.isEmpty(section.getBengaliText())) {
                TextView tvBengali = new TextView(ctx);
                LinearLayout.LayoutParams bengaliParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                bengaliParams.topMargin = dpToPx(ctx, 10);
                tvBengali.setLayoutParams(bengaliParams);
                tvBengali.setText(section.getBengaliText());
                tvBengali.setTextSize(13.5f);
                tvBengali.setLineSpacing(dpToPx(ctx, 3), 1f);
                tvBengali.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
                body.addView(tvBengali);
            }

            // Individual Section Click to Copy
            body.setOnClickListener(v -> {
                String copyText = section.getHeading() + "\n" +
                        (!TextUtils.isEmpty(section.getArabicText()) ? section.getArabicText() + "\n" : "") +
                        section.getBengaliText() + "\n" +
                        (!TextUtils.isEmpty(section.getReference()) ? (isBn ? "সূত্র: " : "Ref: ") + section.getReference() : "");
                ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("History Section", copyText.trim()));
                    Toast.makeText(ctx, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            card.addView(body);
            binding.containerHistoryDetailSections.addView(card);
        }
    }

    private static int dpToPx(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
