package com.devflux.deenone.features.hajj;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.DialogHajjTopicDetailBinding;
import com.devflux.deenone.features.hajj.model.HajjTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HajjTopicDetailDialog {

    public static void show(@NonNull Context context, @NonNull HajjTopicItem item, boolean isBn) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        DialogHajjTopicDetailBinding binding = DialogHajjTopicDetailBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        if (dialog.getWindow() != null) {
            View bottomSheetInternal = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheetInternal != null) {
                bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
            }
        }

        // Bind Icon & Title
        binding.ivDetailTopicIcon.setImageResource(item.getIconResId());
        binding.tvDetailTopicTitle.setText(item.getTitle(isBn));
        binding.tvDetailCategoryPill.setText(item.getCategory(isBn));

        boolean isNight = (context.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        com.devflux.deenone.features.hajj.adapter.HajjTopicAdapter.HajjPalette palette = com.devflux.deenone.features.hajj.adapter.HajjTopicAdapter.getPaletteForTopic(item.getId(), 0);
        binding.ivDetailTopicIcon.setImageTintList(android.content.res.ColorStateList.valueOf(palette.tintColor));
        android.graphics.drawable.GradientDrawable circleBg = new android.graphics.drawable.GradientDrawable();
        circleBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        circleBg.setColor(isNight ? palette.darkBgColor : palette.lightBgColor);
        binding.flTopicIconContainer.setBackground(circleBg);

        // Overview
        binding.tvLabelOverview.setText(isBn ? "সারসংক্ষেপ ও তাৎপর্য" : "Overview & Significance");
        binding.tvDetailOverview.setText(item.getOverview(isBn));

        // What to do
        String whatToDo = item.getWhatToDo(isBn);
        if (whatToDo != null && !whatToDo.trim().isEmpty()) {
            binding.cardWhatToDo.setVisibility(View.VISIBLE);
            binding.tvLabelWhatToDo.setText(isBn ? "করণীয় ও দিকনির্দেশনা" : "Key Guidelines & Actions");
            binding.tvDetailWhatToDo.setText(whatToDo);
        } else {
            binding.cardWhatToDo.setVisibility(View.GONE);
        }

        // What not to do
        String whatNotToDo = item.getWhatNotToDo(isBn);
        if (whatNotToDo != null && !whatNotToDo.trim().isEmpty()) {
            binding.cardWhatNotToDo.setVisibility(View.VISIBLE);
            binding.tvLabelWhatNotToDo.setText(isBn ? "সতর্কতা ও বর্জনীয় বিষয়" : "Prohibitions & Warnings");
            binding.tvDetailWhatNotToDo.setText(whatNotToDo);
        } else {
            binding.cardWhatNotToDo.setVisibility(View.GONE);
        }

        // Dua section
        String arabicDua = item.getArabicDua();
        if (arabicDua != null && !arabicDua.trim().isEmpty()) {
            binding.cardDuaSection.setVisibility(View.VISIBLE);
            binding.tvLabelDua.setText(isBn ? "মাসনূন দোয়া ও জিকির" : "Masnoon Dua & Remembrance");
            binding.tvDetailArabicDua.setText(arabicDua);
            binding.tvDetailTransliteration.setText(item.getTransliteration(isBn));
            binding.tvDetailTranslation.setText(item.getTranslation(isBn));
        } else {
            binding.cardDuaSection.setVisibility(View.GONE);
        }

        // Reference
        String ref = item.getReference();
        if (ref != null && !ref.trim().isEmpty()) {
            binding.cardReferenceSection.setVisibility(View.VISIBLE);
            binding.tvDetailReference.setText((isBn ? "সূত্র: " : "Reference: ") + ref);
        } else {
            binding.cardReferenceSection.setVisibility(View.GONE);
        }

        // Button labels
        binding.btnShareTopic.setText(isBn ? "শেয়ার করুন" : "Share Guide");
        binding.btnCloseDialog.setText(isBn ? "ঠিক আছে" : "Close");

        // Spring touch animations
        TouchAnimationUtil.attachTouchSpring(binding.btnDetailClose);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareTopic);
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseDialog);

        // Click listeners
        binding.btnDetailClose.setOnClickListener(v -> dialog.dismiss());
        binding.btnCloseDialog.setOnClickListener(v -> dialog.dismiss());

        binding.btnShareTopic.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("🕋 ").append(item.getTitle(isBn)).append("\n");
            sb.append("📌 ").append(item.getCategory(isBn)).append("\n\n");
            sb.append("📖 ").append(item.getOverview(isBn)).append("\n\n");

            if (whatToDo != null && !whatToDo.trim().isEmpty()) {
                sb.append("✅ ").append(isBn ? "করণীয়:\n" : "What To Do:\n").append(whatToDo).append("\n\n");
            }
            if (whatNotToDo != null && !whatNotToDo.trim().isEmpty()) {
                sb.append("⚠️ ").append(isBn ? "বর্জনীয়:\n" : "Prohibitions:\n").append(whatNotToDo).append("\n\n");
            }
            if (arabicDua != null && !arabicDua.trim().isEmpty()) {
                sb.append("🤲 ").append(isBn ? "দোয়া:\n" : "Dua:\n").append(arabicDua).append("\n");
                sb.append(item.getTranslation(isBn)).append("\n\n");
            }
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append("📚 ").append(isBn ? "সূত্র: " : "Reference: ").append(ref).append("\n\n");
            }
            sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, item.getTitle(isBn));
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            context.startActivity(Intent.createChooser(shareIntent, isBn ? "শেয়ার করুন" : "Share via"));
        });

        dialog.show();
    }
}
