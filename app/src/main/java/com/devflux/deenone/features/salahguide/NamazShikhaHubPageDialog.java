package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageNamazShikhaHubBinding;
import com.devflux.deenone.features.prayer.SalahTrackerPageDialog;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class NamazShikhaHubPageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageNamazShikhaHubBinding binding = PageNamazShikhaHubBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);

        // Preload and offline-cache all Namaz visual guide step images with Glide
        SalahVisualGuideManager.preloadAllVisualSteps(activity);

        // Dynamic Localization
        binding.tvNamazShikhaTitle.setText(isBn ? "নামাজ শিক্ষা" : "Salah Guide");
        binding.tvCardNamazShikhaMainTitle.setText(isBn ? "নামাজ শিক্ষা" : "Salah Guide");
        binding.tvCardSalahVisualTitle.setText(isBn ? "ভিজুয়াল গাইড" : "Visual Guide");
        binding.tvCardVariousSalahRulesTitle.setText(isBn ? "নামাজের নিয়ম" : "Salah Rules");
        binding.tvCardSalahTrackerTitle.setText(isBn ? "নামাজ ট্র্যাকার" : "Salah Tracker");

        binding.tvItemSalahHistoryTitle.setText(isBn ? "নামাজের ইতিহাস" : "History of Salah");
        binding.tvItemSalahRakatsTitle.setText(isBn ? "নামাজের রাকাতসমূহ" : "Salah Rakats");
        binding.tvItemAzanIqamahTitle.setText(isBn ? "আজান ও ইকামত" : "Adhan & Iqamah");
        binding.tvItemPurityWuduTitle.setText(isBn ? "পাক পবিত্রতা" : "Purity & Wudu");
        binding.tvItemSalahDuaTitle.setText(isBn ? "নামাজের দোয়া" : "Salah Duas");
        binding.tvItemEssentialSurahsTitle.setText(isBn ? "প্রয়োজনীয় সূরা" : "Essential Surahs");
        binding.tvItemMunajatTitle.setText(isBn ? "মোনাজাত" : "Munajat");
        binding.tvItemJumuahTitle.setText(isBn ? "জুম'আ" : "Jumu'ah");
        binding.tvItemNamazBookTitle.setText(isBn ? "নামাজ শিক্ষা বই" : "Salah Book");
        binding.tvItemSalahMasailTitle.setText(isBn ? "মাসাইল" : "Masail");

        // Back button
        binding.btnBackNamazShikha.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackNamazShikha);

        // 1. Top 4 Grid Cards
        binding.cardNamazShikhaMain.setOnClickListener(v -> {
            NamazShikhaMenuPageDialog.show(activity);
        });

        binding.cardSalahVisual.setOnClickListener(v -> {
            SalahVisualGuideDialog.show(activity);
        });

        binding.cardVariousSalahRules.setOnClickListener(v -> {
            VariousSalahRulesPageDialog.show(activity);
        });

        binding.cardSalahTracker.setOnClickListener(v -> {
            SalahTrackerPageDialog.show(activity);
        });

        // 2. 10 Full-Width List Cards
        binding.itemSalahHistory.setOnClickListener(v -> {
            SalahHistoryPageDialog.show(activity);
        });

        binding.itemSalahRakats.setOnClickListener(v -> {
            SalahRakatTableDialog.show(activity);
        });

        binding.itemAzanIqamah.setOnClickListener(v -> {
            AzanIqamahPageDialog.show(activity);
        });

        binding.itemPurityWudu.setOnClickListener(v -> {
            PurityMenuPageDialog.show(activity);
        });

        binding.itemSalahDua.setOnClickListener(v -> {
            SalahDuaMenuPageDialog.show(activity);
        });

        binding.itemEssentialSurahs.setOnClickListener(v -> {
            EssentialSurahsPageDialog.show(activity);
        });

        binding.itemMunajat.setOnClickListener(v -> {
            SalahGenericContentDialog.show(activity, "munajat");
        });

        binding.itemJumuah.setOnClickListener(v -> {
            JumuahMenuPageDialog.show(activity);
        });

        binding.itemNamazBook.setOnClickListener(v -> {
            SalahBooksListPageDialog.show(activity);
        });

        binding.itemSalahMasail.setOnClickListener(v -> {
            MasailMenuPageDialog.show(activity);
        });

        // Apply Vibrant Islamic Color System to all 14 Cards
        boolean isNight = (activity.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        styleIcon(binding.containerIconNamazShikhaMain, binding.ivIconNamazShikhaMain, "#34D399", "#0E3827", "#E6F9F0", isNight);
        styleIcon(binding.containerIconSalahVisual, binding.ivIconSalahVisual, "#22D3EE", "#103338", "#E0F7FA", isNight);
        styleIcon(binding.containerIconVariousSalahRules, binding.ivIconVariousSalahRules, "#FBBF24", "#382810", "#FEF3C7", isNight);
        styleIcon(binding.containerIconSalahTracker, binding.ivIconSalahTracker, "#60A5FA", "#102738", "#E0F2FE", isNight);

        styleIcon(binding.containerIconSalahHistory, binding.ivIconSalahHistory, "#FB923C", "#381F0A", "#FFEDD5", isNight);
        styleIcon(binding.containerIconSalahRakats, binding.ivIconSalahRakats, "#818CF8", "#102B38", "#EEF2FF", isNight);
        styleIcon(binding.containerIconAzanIqamah, binding.ivIconAzanIqamah, "#F59E0B", "#38260E", "#FEF3C7", isNight);
        styleIcon(binding.containerIconPurityWudu, binding.ivIconPurityWudu, "#2DD4BF", "#103833", "#CCFBF1", isNight);
        styleIcon(binding.containerIconSalahDua, binding.ivIconSalahDua, "#F472B6", "#381028", "#FCE7F3", isNight);
        styleIcon(binding.containerIconEssentialSurahs, binding.ivIconEssentialSurahs, "#10B981", "#0E3827", "#D1FAE5", isNight);
        styleIcon(binding.containerIconMunajat, binding.ivIconMunajat, "#C084FC", "#291238", "#F3E8FF", isNight);
        styleIcon(binding.containerIconJumuah, binding.ivIconJumuah, "#34D399", "#0F3A2C", "#DCFCE7", isNight);
        styleIcon(binding.containerIconNamazBook, binding.ivIconNamazBook, "#2DD4BF", "#103833", "#CCFBF1", isNight);
        styleIcon(binding.containerIconSalahMasail, binding.ivIconSalahMasail, "#35D99B", "#122E2B", "#D1FAE5", isNight);

        dialog.show();
    }

    private static void styleIcon(android.widget.FrameLayout container, android.widget.ImageView icon, String tintHex, String darkBgHex, String lightBgHex, boolean isNight) {
        if (container == null || icon == null) return;
        int tintColor = android.graphics.Color.parseColor(tintHex);
        int bgColor = isNight ? android.graphics.Color.parseColor(darkBgHex) : android.graphics.Color.parseColor(lightBgHex);

        icon.setImageTintList(android.content.res.ColorStateList.valueOf(tintColor));

        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        bg.setColor(bgColor);
        container.setBackground(bg);
    }
}
