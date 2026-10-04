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
import com.devflux.deenone.databinding.PageSalahWhenStartedHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class WhenStartedHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "when_started_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahWhenStartedHistoryBinding binding = PageSalahWhenStartedHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header and Card Titles
        String title = isBn ? "মুসলমানরা কখন থেকে নামাজ পড়া শুরু করে" : "When Muslims Started Praying";
        binding.tvHeaderTitle.setText(title);
        binding.tvCardTitle.setText(title);

        // Dynamic Dual-Language Content
        if (!isBn) {
            binding.tvIntroDesc.setText("At what point in a person's life does Salah become obligatory? It is vital to know this, as our age increases day by day while prayer habits often get neglected—something that should never happen for a Muslim. Let us understand when Salah becomes obligatory. In summary, once an individual fulfills certain conditions, Salah becomes strictly obligatory upon them.");
            binding.tvConditionIntro.setText("For example -");

            binding.tvConditionMuslimTitle.setText("Being a Muslim: The primary prerequisite for prayer to become obligatory is being a Muslim, as this divine command is not addressed to non-believers. In the Holy Quran, Allah says:");
            binding.tvMeaningLabel1.setText("Meaning:");
            binding.tvAyahMeaning1.setText("And whoever desires other than Islam as religion - never will it be accepted from him.");
            binding.tvAyahRef1.setText("Surah Aal-e-Imran: 3:85, Surah At-Tawbah: 9:17");

            binding.tvConditionIntellectTitle.setText("Being of Sound Mind: Prayer is not obligatory upon someone who is insane or lacks sound mental capacity. When a person possesses sound intellect, prayer becomes obligatory. The Messenger of Allah (PBUH) said:");
            binding.tvMeaningLabel2.setText("Meaning:");
            binding.tvHadithMeaning1.setText("Three persons are relieved of accountability:\n• The sleeper until he wakes up,\n• The child until he reaches puberty, and\n• The insane until he regains sanity.");
            binding.tvHadithRef1.setText("Tirmidhi, Abu Dawud, Mishkat: 3287");

            binding.tvConditionPubertyTitle.setText("Reaching the Age of Puberty: Salah becomes obligatory upon a Muslim upon reaching puberty, since the pen is lifted from a child and sins are not recorded until maturity. However, it must be practiced from childhood. The Messenger of Allah (PBUH) said:");
            binding.tvMeaningLabel3.setText("Meaning:");
            binding.tvHadithMeaning2.setText("Command your children to pray when they reach seven years of age, and discipline them for neglecting it when they reach ten years, and separate their beds.");
            binding.tvHadithRef2.setText("Musnad Ahmad, Sunan Abu Dawud");
        }

        // Back Button
        binding.btnBackWhenStartedHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackWhenStartedHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsWhenStartedHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsWhenStartedHistory);

        // Main Card Menu
        binding.btnMenuCardWhenStarted.setOnClickListener(v -> {
            showCardActionDialog(activity, title, getCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahWhenStartedHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvIntroDesc);
        bodyViews.add(binding.tvConditionIntro);
        bodyViews.add(binding.tvConditionMuslimTitle);
        bodyViews.add(binding.tvAyahMeaning1);
        bodyViews.add(binding.tvConditionIntellectTitle);
        bodyViews.add(binding.tvHadithMeaning1);
        bodyViews.add(binding.tvConditionPubertyTitle);
        bodyViews.add(binding.tvHadithMeaning2);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }

        // Arabic texts scale proportionally
        float arabicSp = bodySp + 6.0f;
        if (binding.tvAyahArabic1 != null) binding.tvAyahArabic1.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp);
        if (binding.tvHadithArabic1 != null) binding.tvHadithArabic1.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp);
        if (binding.tvHadithArabic2 != null) binding.tvHadithArabic2.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicSp);
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahWhenStartedHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
        String[] options = isBn ? new String[]{
                "ফন্ট সাইজ: ছোট (১২ sp)",
                "ফন্ট সাইজ: সাধারণ (১৪ sp)",
                "ফন্ট সাইজ: প্রমিত (১৬ sp)",
                "ফন্ট সাইজ: বড় (১৮ sp)",
                "ফন্ট সাইজ: বিশাল (২০ sp)",
                "সম্পূর্ণ বিষয়বস্তু কপি করুন",
                "সম্পূর্ণ বিষয়বস্তু শেয়ার করুন"
        } : new String[]{
                "Font Size: Small (12 sp)",
                "Font Size: Normal (14 sp)",
                "Font Size: Medium (16 sp)",
                "Font Size: Large (18 sp)",
                "Font Size: Extra Large (20 sp)",
                "Copy Entire Content",
                "Share Entire Content"
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
                        copyToClipboard(activity, isBn ? "মুসলমানরা কখন থেকে নামাজ পড়া শুরু করে" : "When Muslims Started Praying", getCardContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "মুসলমানরা কখন থেকে নামাজ পড়া শুরু করে" : "When Muslims Started Praying", getCardContent(binding, isBn), isBn);
                    }
                })
                .show();
    }

    private static void showCardActionDialog(Activity activity, String cardTitle, String content, boolean isBn) {
        String[] options = isBn ? new String[]{
                "কার্ডের লেখা কপি করুন",
                "শেয়ার করুন"
        } : new String[]{
                "Copy Card Text",
                "Share"
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

    private static String getCardContent(PageSalahWhenStartedHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCardTitle.getText()).append("\n\n");
        sb.append(binding.tvIntroDesc.getText()).append("\n\n");
        sb.append(binding.tvConditionIntro.getText()).append("\n");
        sb.append(binding.tvConditionMuslimTitle.getText()).append("\n\n");
        sb.append(binding.tvAyahArabic1.getText()).append("\n\n");
        sb.append(binding.tvMeaningLabel1.getText()).append("\n").append(binding.tvAyahMeaning1.getText()).append("\n");
        sb.append(binding.tvAyahRef1.getText()).append("\n\n");
        sb.append(binding.tvConditionIntellectTitle.getText()).append("\n\n");
        sb.append(binding.tvHadithArabic1.getText()).append("\n\n");
        sb.append(binding.tvMeaningLabel2.getText()).append("\n").append(binding.tvHadithMeaning1.getText()).append("\n");
        sb.append(binding.tvHadithRef1.getText()).append("\n\n");
        sb.append(binding.tvConditionPubertyTitle.getText()).append("\n\n");
        sb.append(binding.tvHadithArabic2.getText()).append("\n\n");
        sb.append(binding.tvMeaningLabel3.getText()).append("\n").append(binding.tvHadithMeaning2.getText()).append("\n");
        sb.append(binding.tvHadithRef2.getText());
        return sb.toString();
    }
}
