package com.devflux.deenone.features.prophets.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogProphetDetailBinding;
import com.devflux.deenone.features.prophets.model.ProphetStoryItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class ProphetDetailDialog {

    public static void show(@NonNull Activity activity, ProphetStoryItem item) {
        if (item == null || activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogProphetDetailBinding binding = DialogProphetDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        TouchAnimationUtil.attachTouchSpring(binding.btnCloseProphetDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareProphetStory);

        binding.btnCloseProphetDetail.setOnClickListener(v -> dialog.dismiss());

        // Language adaptations
        if (!isBn) {
            binding.tvDetailBarTitle.setText("Prophet's Biography");
            binding.tvLabelSummary.setText("Summary Profile");
            binding.tvLabelBiography.setText("Historical Events & Dawah");
            binding.tvLabelDua.setText("Special Quranic Dua");
            binding.tvLabelLessons.setText("Spiritual Lessons & Morals");
        }

        binding.tvDetailArabicName.setText(item.getArabicName());
        binding.tvDetailName.setText(isBn ? item.getBengaliName() : item.getEnglishName());
        binding.tvDetailTitle.setText(item.getTitle(isBn));
        binding.tvDetailEraPill.setText(item.getEraCategory());

        String mentionsText = isBn
                ? ("কুরআনে উল্লেখ: " + BengaliNumberUtil.toBengali(item.getQuranMentionCount()) + " বার")
                : ("Quranic Mentions: " + item.getQuranMentionCount() + " times");
        binding.tvDetailMentionsPill.setText(mentionsText);

        binding.tvDetailSurahRefs.setText(item.getSurahReferences());
        binding.tvDetailSummary.setText(item.getSummary(isBn));
        binding.tvDetailStory.setText(item.getDetailedStory(isBn));

        // Quranic Dua
        if (item.getQuranicDuaArabic() != null && !item.getQuranicDuaArabic().isEmpty()) {
            binding.cardQuranicDua.setVisibility(View.VISIBLE);
            binding.tvDetailDuaArabic.setText(item.getQuranicDuaArabic());
            binding.tvDetailDuaTransliteration.setText(item.getQuranicDuaTransliteration());
            binding.tvDetailDuaMeaning.setText(item.getQuranicDuaMeaning());
        } else {
            binding.cardQuranicDua.setVisibility(View.GONE);
        }

        binding.tvDetailLessons.setText(item.getKeyLessons(isBn));

        // Share Action
        binding.btnShareProphetStory.setOnClickListener(v -> {
            String shareText = (isBn ? item.getBengaliName() : item.getEnglishName()) + " - " + item.getTitle(isBn) + "\n\n"
                    + item.getSummary(isBn) + "\n\n"
                    + (item.getQuranicDuaArabic() != null ? (item.getQuranicDuaArabic() + "\n" + item.getQuranicDuaMeaning() + "\n\n") : "")
                    + (isBn ? "দ্বীনওয়ানের সাথে থাকুন।" : "Shared from DeenOne App.");

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share Prophet's Story"));
        });

        dialog.show();
    }
}
