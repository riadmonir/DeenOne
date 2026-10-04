package com.devflux.deenone.features.hajj;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageHajjGuideBinding;
import com.devflux.deenone.features.hajj.adapter.HajjTopicAdapter;
import com.devflux.deenone.features.hajj.data.HajjContentRepository;
import com.devflux.deenone.features.hajj.model.HajjTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class HajjGuidePageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjGuideBinding binding = PageHajjGuideBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHajjPageTitle.setText(isBn ? "হজ এবং উমরাহ" : "Hajj & Umrah");

        // Back Button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjGuide);
        binding.btnBackHajjGuide.setOnClickListener(v -> dialog.dismiss());

        // Theme Toggle Button
        TouchAnimationUtil.attachTouchSpring(binding.btnThemeToggle);
        binding.btnThemeToggle.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleAppTheme();
                dialog.dismiss();
            }
        });

        // Top Banner Card (ধর্ম মন্ত্রনালয় সহায়িকা)
        HajjTopicItem bannerItem = HajjContentRepository.getBannerItem();
        binding.tvBannerTitle.setText(bannerItem.getTitle(isBn));
        binding.cardHajjMinistryBanner.setOnClickListener(v -> {
            MinistryHajjGuidePageDialog.show(activity);
        });

        boolean isNight = (activity.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        binding.ivBannerKaabaIcon.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FBBF24")));
        android.graphics.drawable.GradientDrawable kaabaBg = new android.graphics.drawable.GradientDrawable();
        kaabaBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        kaabaBg.setColor(isNight ? android.graphics.Color.parseColor("#382810") : android.graphics.Color.parseColor("#FEF3C7"));
        binding.flBannerKaabaContainer.setBackground(kaabaBg);

        // 3-Column Topic Grid Setup
        List<HajjTopicItem> topics = HajjContentRepository.getGridTopics();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        binding.rvHajjTopics.setLayoutManager(gridLayoutManager);
        HajjTopicAdapter adapter = new HajjTopicAdapter(activity, topics, isBn);
        binding.rvHajjTopics.setAdapter(adapter);

        dialog.show();
    }
}
