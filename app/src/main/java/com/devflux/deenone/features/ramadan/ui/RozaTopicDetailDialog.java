package com.devflux.deenone.features.ramadan.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogRozaTopicDetailBinding;
import com.devflux.deenone.features.ramadan.data.RozaContentRepository;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class RozaTopicDetailDialog {

    public static void show(@NonNull AppCompatActivity activity, String topicId) {
        RozaContentRepository.TopicContent content = RozaContentRepository.getTopic(topicId);
        if (content == null) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogRozaTopicDetailBinding binding = DialogRozaTopicDetailBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        TouchAnimationUtil.attachTouchSpring(binding.btnBackTopicDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnShareTopicDetail);

        binding.btnBackTopicDetail.setOnClickListener(v -> dialog.dismiss());

        binding.tvTopicDetailTitle.setText(content.getTitle(isBn));
        binding.tvTopicDetailCategory.setText(content.getCategory(isBn));

        if (content.arabicDua != null && !content.arabicDua.trim().isEmpty()) {
            binding.boxArabic.setVisibility(View.VISIBLE);
            binding.tvTopicArabicDua.setText(content.arabicDua);
        } else {
            binding.boxArabic.setVisibility(View.GONE);
        }

        if (content.getTransliteration(isBn) != null && !content.getTransliteration(isBn).trim().isEmpty()) {
            binding.boxTransliteration.setVisibility(View.VISIBLE);
            binding.tvTopicTransliteration.setText(content.getTransliteration(isBn));
        } else {
            binding.boxTransliteration.setVisibility(View.GONE);
        }

        if (content.getTranslation(isBn) != null && !content.getTranslation(isBn).trim().isEmpty()) {
            binding.boxTranslation.setVisibility(View.VISIBLE);
            binding.tvTopicTranslation.setText(content.getTranslation(isBn));
        } else {
            binding.boxTranslation.setVisibility(View.GONE);
        }

        binding.tvTopicDetails.setText(content.getDetails(isBn));
        binding.tvTopicReference.setText((isBn ? "রেফারেন্স: " : "Reference: ") + content.getReference(isBn));

        binding.btnShareTopicDetail.setOnClickListener(v -> {
            String shareText = content.getTitle(isBn) + "\n\n"
                    + (content.arabicDua != null ? content.arabicDua + "\n\n" : "")
                    + (content.getTranslation(isBn) != null ? content.getTranslation(isBn) + "\n\n" : "")
                    + content.getDetails(isBn) + "\n\n"
                    + (isBn ? "রেফারেন্স: " : "Reference: ") + content.getReference(isBn) + "\n\n"
                    + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share"));
        });

        dialog.show();
    }
}
