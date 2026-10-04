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
import com.devflux.deenone.databinding.PageAzanDuaRulesBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AzanDuaRulesPageDialog {

    private static final String PREFS_NAME = "salah_azan_prefs";
    private static final String KEY_FONT_SIZE = "azan_dua_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAzanDuaRulesBinding binding = PageAzanDuaRulesBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan");
        binding.tvCardTitle.setText(isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan");

        binding.tvLabelPronunciation.setText(isBn ? "উচ্চারণ:" : "Pronunciation:");
        binding.tvPronunciation.setText(isBn
                ? "আল্লাহুম্মা রাব্বা হাজিহিদ দাওয়াতিত তাম্মাতি ওয়াছ ছালাতিল ক্বায়িমাহ, আতি মুহাম্মাদানিল ওয়াসিলাতা ওয়াল ফাদিলাহ, ওয়াবাআছহু মাক্বামাম্ মাহমুদানিল্লাজি ওয়া আত্তাহ।"
                : "Allahumma Rabba hadhihid-da'watit-tammah, was-salatil-qa'imah, ati Muhammadanil-wasilata wal-fadhilah, wab'ath-hu maqamam mahmudanilladhi wa'adtah.");

        binding.tvLabelMeaning.setText(isBn ? "অর্থ:" : "Meaning:");
        binding.tvMeaning.setText(isBn
                ? "হে আল্লাহ! এই পরিপূর্ণ আহবান ও আসন্ন ছালাতের তুমি মালিক। মুহাম্মাদ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামকে ওয়াসিলা ও সর্বোচ্চ মর্যাদার অধিকারী করুন। এবং তাঁকে সেই প্রশংসিত স্থানে অধিষ্ঠিত কর যার ওয়াদা তুমি করেছ।"
                : "O Allah! Lord of this perfect call and established prayer, grant Muhammad the station of Wasilah and high dignity, and raise him to the praised status that You promised him.");

        binding.tvHadithNarration.setText(isBn
                ? "জাবির বিন আব্দুল্লাহ (রা.) থেকে বর্ণিত, রাসুল (সা.) বলেছেন, যে ব্যক্তি আজান শুনে উল্লিখিত দোয়া পড়বে কিয়ামতের দিন তার জন্য আমার সুপারিশ থাকবে।"
                : "Narrated Jabir bin Abdullah (RA): The Messenger of Allah (pbuh) said: Whoever says this supplication after hearing the call to prayer, my intercession will be assured for him on the Day of Resurrection.");

        binding.tvHadithRef.setText(isBn ? "সূত্র: সহীহ বুখারী: ৬১৪" : "Reference: Sahih al-Bukhari: 614");

        // Back Button with Spring
        binding.btnBackAzanDuaRules.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackAzanDuaRules);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button
        binding.btnSettingsAzanDuaRules.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsAzanDuaRules);

        // Card Menu Button
        binding.btnMenuCardAzanDua.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan", getCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageAzanDuaRulesBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvPronunciation);
        bodyViews.add(binding.tvMeaning);
        bodyViews.add(binding.tvHadithNarration);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }

        // Scale Arabic proportionately
        if (binding.tvArabicDua != null) {
            binding.tvArabicDua.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp + 6.0f);
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageAzanDuaRulesBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan", getCardContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan", getCardContent(binding, isBn), isBn);
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

    private static String getCardContent(PageAzanDuaRulesBinding binding, boolean isBn) {
        return binding.tvCardTitle.getText() + "\n\n" +
                binding.tvArabicDua.getText() + "\n\n" +
                (isBn ? "উচ্চারণ:\n" : "Pronunciation:\n") + binding.tvPronunciation.getText() + "\n\n" +
                (isBn ? "অর্থ:\n" : "Meaning:\n") + binding.tvMeaning.getText() + "\n\n" +
                binding.tvHadithNarration.getText() + "\n" +
                binding.tvHadithRef.getText();
    }
}
