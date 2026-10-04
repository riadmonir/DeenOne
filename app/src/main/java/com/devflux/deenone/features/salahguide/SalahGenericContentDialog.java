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
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageSalahGenericContentBinding;
import com.devflux.deenone.features.salahguide.data.NamazContentRepository;
import com.devflux.deenone.features.salahguide.model.GenericSalahSection;
import com.devflux.deenone.features.salahguide.model.GenericSalahTopic;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.card.MaterialCardView;

public class SalahGenericContentDialog {

    public static void show(Activity activity, String topicId) {
        if (activity == null || activity.isFinishing()) return;

        GenericSalahTopic topic = NamazContentRepository.getTopic(topicId);
        if (topic == null) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahGenericContentBinding binding = PageSalahGenericContentBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);

        binding.btnBackGenericContent.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackGenericContent);

        binding.tvGenericHeaderTitle.setText(topic.getTitle());
        binding.tvOverviewTitle.setText(topic.getTitle());
        binding.badgeOverviewTag.setText(topic.getBadge());
        binding.tvOverviewDescription.setText(topic.getOverviewDescription());

        populateSections(activity, binding, topic, isBn);

        dialog.show();
    }

    private static void populateSections(Context ctx, PageSalahGenericContentBinding binding, GenericSalahTopic topic, boolean isBn) {
        binding.containerDynamicContentItems.removeAllViews();
        if (ctx == null) return;

        for (GenericSalahSection section : topic.getSections()) {
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
            body.setPadding(dpToPx(ctx, 14), dpToPx(ctx, 14), dpToPx(ctx, 14), dpToPx(ctx, 14));

            // Section Title & Reference row
            LinearLayout headerRow = new LinearLayout(ctx);
            headerRow.setOrientation(LinearLayout.HORIZONTAL);
            headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView tvTitle = new TextView(ctx);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tvTitle.setLayoutParams(titleParams);
            tvTitle.setText(section.getTitle());
            tvTitle.setTextSize(15f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
            headerRow.addView(tvTitle);

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

            // Arabic Text Container (if available)
            if (!TextUtils.isEmpty(section.getArabic())) {
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
                tvArabic.setText(section.getArabic());
                tvArabic.setTextSize(17f);
                tvArabic.setLineSpacing(dpToPx(ctx, 4), 1f);
                tvArabic.setGravity(android.view.Gravity.RIGHT);
                tvArabic.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
                tvArabic.setPadding(dpToPx(ctx, 12), dpToPx(ctx, 10), dpToPx(ctx, 12), dpToPx(ctx, 10));
                arabicBox.addView(tvArabic);
                body.addView(arabicBox);
            }

            // Transliteration (উচ্চারণ)
            if (!TextUtils.isEmpty(section.getPronunciation())) {
                TextView tvPron = new TextView(ctx);
                LinearLayout.LayoutParams pronParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                pronParams.topMargin = dpToPx(ctx, 8);
                tvPron.setLayoutParams(pronParams);
                tvPron.setText((isBn ? "উচ্চারণ: " : "Pronunciation: ") + section.getPronunciation());
                tvPron.setTextSize(13f);
                tvPron.setLineSpacing(dpToPx(ctx, 2), 1f);
                tvPron.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
                body.addView(tvPron);
            }

            // Meaning (অর্থ)
            if (!TextUtils.isEmpty(section.getTranslation())) {
                TextView tvTrans = new TextView(ctx);
                LinearLayout.LayoutParams transParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                transParams.topMargin = dpToPx(ctx, 6);
                tvTrans.setLayoutParams(transParams);
                tvTrans.setText((isBn ? "অর্থ: " : "Translation: ") + section.getTranslation());
                tvTrans.setTextSize(13.5f);
                tvTrans.setLineSpacing(dpToPx(ctx, 2), 1f);
                tvTrans.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
                body.addView(tvTrans);
            }

            // Additional Explanation / Description
            if (!TextUtils.isEmpty(section.getDescription())) {
                TextView tvDesc = new TextView(ctx);
                LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                descParams.topMargin = dpToPx(ctx, 8);
                tvDesc.setLayoutParams(descParams);
                tvDesc.setText(section.getDescription());
                tvDesc.setTextSize(13f);
                tvDesc.setLineSpacing(dpToPx(ctx, 3), 1f);
                tvDesc.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
                body.addView(tvDesc);
            }

            // Quick Copy Action
            body.setOnClickListener(v -> {
                String copyText = section.getTitle() + "\n" +
                        (!TextUtils.isEmpty(section.getArabic()) ? section.getArabic() + "\n" : "") +
                        (!TextUtils.isEmpty(section.getTranslation()) ? (isBn ? "অর্থ: " : "Translation: ") + section.getTranslation() + "\n" : "") +
                        (!TextUtils.isEmpty(section.getReference()) ? (isBn ? "সূত্র: " : "Reference: ") + section.getReference() : "");
                ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText(isBn ? "নামাজের বিষয়বস্তু" : "Salah Content", copyText.trim()));
                    Toast.makeText(ctx, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            card.addView(body);
            binding.containerDynamicContentItems.addView(card);
        }
    }

    private static int dpToPx(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
