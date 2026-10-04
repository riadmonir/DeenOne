package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageAzanAnswerBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AzanAnswerPageDialog {

    private static final String PREFS_NAME = "salah_azan_prefs";
    private static final String KEY_FONT_SIZE = "azan_answer_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAzanAnswerBinding binding = PageAzanAnswerBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "আজানের উত্তর" : "Responding to Adhan");

        // Back Button with Spring
        binding.btnBackAzanAnswer.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackAzanAnswer);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button
        binding.btnSettingsAzanAnswer.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsAzanAnswer);

        // Card 1 Menu Button
        binding.btnMenuCard1.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "আজানের উত্তর দেওয়ার নিয়ম" : "Rules of Responding to Adhan", getCard1Content(binding, isBn), isBn);
        });

        // Card 2 Menu Button
        binding.btnMenuCard2.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "যারা জবাব দেবেন না" : "Those Who Should Not Respond", getCard2Content(binding), isBn);
        });

        // Card 3 Menu Button
        binding.btnMenuCard3.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "আজানের সময় কথা-কাজ না করা" : "Ceasing Talk and Work During Adhan", getCard3Content(binding), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageAzanAnswerBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        // Card 1 views
        bodyViews.add(binding.tvIntroRule);
        bodyViews.add(binding.tvPrompt1);
        bodyViews.add(binding.tvPronunciationShahadah);
        bodyViews.add(binding.tvMeaningShahadah);
        bodyViews.add(binding.tvWarningBidah);
        bodyViews.add(binding.tvHayyaAlaAnswer);
        bodyViews.add(binding.tvPronunciationHawqala);
        bodyViews.add(binding.tvMeaningHawqala);
        bodyViews.add(binding.tvFajrRule);
        bodyViews.add(binding.tvPromptDurood);
        bodyViews.add(binding.tvPronunciationWasilah);
        bodyViews.add(binding.tvMeaningWasilah);
        bodyViews.add(binding.tvUnprovenPhrases);
        bodyViews.add(binding.tvDuroodHadith);
        bodyViews.add(binding.tvBidahLoud);
        bodyViews.add(binding.tvBismillahBidah);
        bodyViews.add(binding.tvGeneralRules);
        bodyViews.add(binding.tvQuranPause);
        bodyViews.add(binding.tvEatingRule);

        // Card 2 views
        bodyViews.add(binding.tvCard2Body1);
        bodyViews.add(binding.tvCard2Body2);

        // Card 3 views
        bodyViews.add(binding.tvCard3Body);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }

        // Scale Arabic proportionately
        float arabicSp = bodySp + 5.0f;
        if (binding.tvArabicShahadah != null) binding.tvArabicShahadah.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp);
        if (binding.tvArabicHawqala != null) binding.tvArabicHawqala.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp + 1.0f);
        if (binding.tvArabicWasilah != null) binding.tvArabicWasilah.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp);
    }

    private static void showReaderSettingsDialog(Activity activity, PageAzanAnswerBinding binding, SharedPreferences prefs, boolean isBn) {
        String[] options = {
                isBn ? "ফন্ট সাইজ: ছোট (১২ sp)" : "Font: Small (12 sp)",
                isBn ? "ফন্ট সাইজ: সাধারণ (১৪ sp)" : "Font: Normal (14 sp)",
                isBn ? "ফন্ট সাইজ: প্রমিত (১৬ sp)" : "Font: Medium (16 sp)",
                isBn ? "ফন্ট সাইজ: বড় (১৮ sp)" : "Font: Large (18 sp)",
                isBn ? "ফন্ট সাইজ: বিশাল (২০ sp)" : "Font: Extra Large (20 sp)",
                isBn ? "সম্পূর্ণ বিষয়বস্তু কপি করুন" : "Copy All Content",
                isBn ? "সম্পূর্ণ বিষয়বস্তু শেয়ার করুন" : "Share All Content"
        };
        float[] sizes = {12.0f, 14.0f, 16.0f, 18.0f, 20.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which >= 0 && which < sizes.length) {
                        float chosen = sizes[which];
                        applyFontSize(binding, chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 5) {
                        copyToClipboard(activity, isBn ? "আজানের উত্তর" : "Responding to Adhan", getFullPageContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "আজানের উত্তর" : "Responding to Adhan", getFullPageContent(binding, isBn), isBn);
                    }
                })
                .show();
    }

    private static void showCardActionDialog(Activity activity, String cardTitle, String content, boolean isBn) {
        String[] options = {
                isBn ? "কার্ডের লেখা কপি করুন" : "Copy Card Content",
                isBn ? "শেয়ার করুন" : "Share"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(cardTitle)
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        copyToClipboard(activity, cardTitle, content, isBn);
                    } else if (which == 1) {
                        shareContent(activity, cardTitle, content, isBn);
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text, boolean isBn) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            Toast.makeText(context, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text, boolean isBn) {
        String fullShare = text.trim() + "\n\n" + (isBn ? "— দ্বীনওয়ান" : "— DeenOne");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullShare);
        context.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share via"));
    }

    private static String getCard1Content(PageAzanAnswerBinding binding, boolean isBn) {
        return binding.tvCard1Title.getText() + "\n\n" +
                binding.tvIntroRule.getText() + "\n" +
                binding.tvRef1.getText() + "\n\n" +
                binding.tvPrompt1.getText() + "\n\n" +
                binding.tvArabicShahadah.getText() + "\n\n" +
                (isBn ? "উচ্চারণ:\n" : "Pronunciation:\n") + binding.tvPronunciationShahadah.getText() + "\n\n" +
                binding.tvMeaningShahadah.getText() + "\n" +
                binding.tvRef2.getText() + "\n\n" +
                binding.tvWarningBidah.getText() + "\n" +
                binding.tvRefBidah.getText() + "\n\n" +
                binding.tvHayyaAlaAnswer.getText() + "\n\n" +
                binding.tvArabicHawqala.getText() + "\n\n" +
                (isBn ? "উচ্চারণ:\n" : "Pronunciation:\n") + binding.tvPronunciationHawqala.getText() + "\n\n" +
                binding.tvMeaningHawqala.getText() + "\n" +
                binding.tvRefHawqala.getText() + "\n\n" +
                binding.tvFajrRule.getText() + "\n" +
                binding.tvRefFajr.getText() + "\n\n" +
                binding.tvPromptDurood.getText() + "\n\n" +
                binding.tvArabicWasilah.getText() + "\n\n" +
                (isBn ? "উচ্চারণ:\n" : "Pronunciation:\n") + binding.tvPronunciationWasilah.getText() + "\n\n" +
                binding.tvMeaningWasilah.getText() + "\n" +
                binding.tvRefWasilah.getText() + "\n\n" +
                binding.tvUnprovenPhrases.getText() + "\n\n" +
                binding.tvDuroodHadith.getText() + "\n" +
                binding.tvRefDuroodHadith.getText() + "\n\n" +
                binding.tvBidahLoud.getText() + "\n" +
                binding.tvRefBidahLoud.getText() + "\n\n" +
                binding.tvBismillahBidah.getText() + "\n" +
                binding.tvRefBismillah.getText() + "\n\n" +
                binding.tvGeneralRules.getText() + "\n\n" +
                binding.tvQuranPause.getText() + "\n" +
                binding.tvRefQuranPause.getText() + "\n\n" +
                binding.tvEatingRule.getText() + "\n" +
                binding.tvRefEating.getText();
    }

    private static String getCard2Content(PageAzanAnswerBinding binding) {
        return binding.tvCard2Title.getText() + "\n\n" +
                binding.tvCard2Body1.getText() + "\n\n" +
                binding.tvCard2Body2.getText() + "\n" +
                binding.tvCard2Ref.getText();
    }

    private static String getCard3Content(PageAzanAnswerBinding binding) {
        return binding.tvCard3Title.getText() + "\n\n" +
                binding.tvCard3Body.getText();
    }

    private static String getFullPageContent(PageAzanAnswerBinding binding, boolean isBn) {
        return getCard1Content(binding, isBn) + "\n\n--------------------\n\n" +
                getCard2Content(binding) + "\n\n--------------------\n\n" +
                getCard3Content(binding);
    }
}
