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
import com.devflux.deenone.databinding.PageSalahWhyFiveTimesHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class WhyFiveTimesHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "why_five_times_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahWhyFiveTimesHistoryBinding binding = PageSalahWhyFiveTimesHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header and Card Titles
        String title = isBn ? "নামাজের জন্য পাঁচটি সময় কেন নির্ধারিত" : "Why Five Prayer Times Are Set";
        String card1Title = isBn ? "যে কারণে পাঁচ ওয়াক্ত নামাজ পাঁচটি সময়ে ভাগ করা" : "Reasons Why Prayers Are Divided into Five Times";
        String card2Title = isBn ? "কোরআন ও হাদিসের নির্দেশনা" : "Guidance from the Quran & Hadith";

        binding.tvHeaderTitle.setText(title);
        binding.tvCard1Title.setText(card1Title);
        binding.tvCard2Title.setText(card2Title);

        // Dynamic Dual-Language Content
        if (!isBn) {
            binding.tvReasonsIntro.setText("There is profound divine wisdom and purpose behind Allah designating five distinct times for prayer. Each time is set for specific reasons and contexts in human life. Below is an explanation of the reasons and significance of the five prayer times:");

            binding.tvSubheadingFajr.setText("1. Fajr -");
            binding.tvFajrTime.setText("Time: Before dawn until sunrise.");
            binding.tvSubheadingFajrReason.setText("Reasons:");
            binding.tvFajrReason1.setText("• Remembering Allah at the beginning of a new day and seeking His help.");
            binding.tvFajrReason2.setText("• A Muslim starts their day with worship, keeping them conscious and upright throughout the day.");

            binding.tvSubheadingDhuhr.setText("2. Dhuhr -");
            binding.tvDhuhrTime.setText("Time: At noon, when the sun passes the meridian.");
            binding.tvSubheadingDhuhrReason.setText("Reasons:");
            binding.tvDhuhrReason1.setText("• Reminds of Allah amidst the peak of daytime worldly engagements.");
            binding.tvDhuhrReason2.setText("• Reinforces a Muslim's devotion, integrity, and responsibility in daily affairs.");

            binding.tvSubheadingAsr.setText("3. Asr -");
            binding.tvAsrTime.setText("Time: Late afternoon until shortly before sunset.");
            binding.tvSubheadingAsrReason.setText("Reasons:");
            binding.tvAsrReason1.setText("• Remembrance of Allah in the latter part of the day, bringing rest and patience after hours of toil.");
            binding.tvAsrReason2.setText("• Preserves a Muslim's faith and spiritual vigor before nightfall.");

            binding.tvSubheadingMaghrib.setText("4. Maghrib -");
            binding.tvMaghribTime.setText("Time: Immediately after sunset.");
            binding.tvSubheadingMaghribReason.setText("Reasons:");
            binding.tvMaghribReason1.setText("• Expressing gratitude to Allah as the daylight concludes.");
            binding.tvMaghribReason2.setText("• Remembering Allah before gathering with family and starting nightly rest.");

            binding.tvSubheadingIsha.setText("5. Isha -");
            binding.tvIshaTime.setText("Time: After twilight disappears into total darkness.");
            binding.tvSubheadingIshaReason.setText("Reasons:");
            binding.tvIshaReason1.setText("• Remembering Allah and seeking His protection before sleep.");
            binding.tvIshaReason2.setText("• Guides a Muslim away from nightly temptations and towards righteousness.");

            binding.tvSubheadingWisdom.setText("• Wisdom of Islam");
            binding.tvIslamWisdomDesc.setText("• Allah ordained these five prayer times aligned with the natural rhythm of human daily life. These moments instill remembrance of Allah, self-purification, and patience throughout the day. Each prayer helps keep a Muslim's life disciplined, honest, and spiritually pure.");

            binding.tvGuidanceIntro.setText("The Quran and Hadith mention the various prescribed times for the five daily prayers.");
            binding.tvGuidanceQuote1.setText("\"And establish prayer at the two ends of the day and at the approach of the night.\"");
            binding.tvGuidanceRef1.setText("Surah Hud: 11:114");

            binding.tvGuidanceQuote2.setText("\"Establish prayer from the decline of the sun until the darkness of the night and recite the Quran at dawn.\"");
            binding.tvGuidanceRef2.setText("Surah Al-Isra: 17:78");

            binding.tvGuidanceQuote3.setText("\"Narrated by Abu Hurairah (RA), the Messenger of Allah (PBUH) said: 'If there was a river at the door of one of you in which he bathed five times a day, would any of his filth remain?' They said: 'None of his filth would remain.' He said: 'That is the example of the five prayers through which Allah washes away sins.'\"");
            binding.tvGuidanceRef3.setText("Sahih Muslim: 626");
        }

        // Back Button
        binding.btnBackWhyFiveTimesHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackWhyFiveTimesHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsWhyFiveTimesHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsWhyFiveTimesHistory);

        // Card 1 Menu
        binding.btnMenuCardWhyFiveTimes.setOnClickListener(v -> {
            showCardActionDialog(activity, card1Title, getReasonsCardContent(binding, isBn), isBn);
        });

        // Card 2 Menu
        binding.btnMenuCardGuidance.setOnClickListener(v -> {
            showCardActionDialog(activity, card2Title, getGuidanceCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahWhyFiveTimesHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvReasonsIntro);
        bodyViews.add(binding.tvFajrTime);
        bodyViews.add(binding.tvFajrReason1);
        bodyViews.add(binding.tvFajrReason2);

        bodyViews.add(binding.tvDhuhrTime);
        bodyViews.add(binding.tvDhuhrReason1);
        bodyViews.add(binding.tvDhuhrReason2);

        bodyViews.add(binding.tvAsrTime);
        bodyViews.add(binding.tvAsrReason1);
        bodyViews.add(binding.tvAsrReason2);

        bodyViews.add(binding.tvMaghribTime);
        bodyViews.add(binding.tvMaghribReason1);
        bodyViews.add(binding.tvMaghribReason2);

        bodyViews.add(binding.tvIshaTime);
        bodyViews.add(binding.tvIshaReason1);
        bodyViews.add(binding.tvIshaReason2);
        bodyViews.add(binding.tvIslamWisdomDesc);

        bodyViews.add(binding.tvGuidanceIntro);
        bodyViews.add(binding.tvGuidanceQuote1);
        bodyViews.add(binding.tvGuidanceQuote2);
        bodyViews.add(binding.tvGuidanceQuote3);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahWhyFiveTimesHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "নামাজের জন্য পাঁচটি সময় কেন নির্ধারিত" : "Why Five Prayer Times Are Set", getFullArticleContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "নামাজের জন্য পাঁচটি সময় কেন নির্ধারিত" : "Why Five Prayer Times Are Set", getFullArticleContent(binding, isBn), isBn);
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

    private static String getReasonsCardContent(PageSalahWhyFiveTimesHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCard1Title.getText()).append("\n\n");
        sb.append(binding.tvReasonsIntro.getText()).append("\n\n");

        sb.append(binding.tvSubheadingFajr.getText()).append("\n");
        sb.append(binding.tvFajrTime.getText()).append("\n");
        sb.append(binding.tvSubheadingFajrReason.getText()).append("\n");
        sb.append(binding.tvFajrReason1.getText()).append("\n");
        sb.append(binding.tvFajrReason2.getText()).append("\n\n");

        sb.append(binding.tvSubheadingDhuhr.getText()).append("\n");
        sb.append(binding.tvDhuhrTime.getText()).append("\n");
        sb.append(binding.tvSubheadingDhuhrReason.getText()).append("\n");
        sb.append(binding.tvDhuhrReason1.getText()).append("\n");
        sb.append(binding.tvDhuhrReason2.getText()).append("\n\n");

        sb.append(binding.tvSubheadingAsr.getText()).append("\n");
        sb.append(binding.tvAsrTime.getText()).append("\n");
        sb.append(binding.tvSubheadingAsrReason.getText()).append("\n");
        sb.append(binding.tvAsrReason1.getText()).append("\n");
        sb.append(binding.tvAsrReason2.getText()).append("\n\n");

        sb.append(binding.tvSubheadingMaghrib.getText()).append("\n");
        sb.append(binding.tvMaghribTime.getText()).append("\n");
        sb.append(binding.tvSubheadingMaghribReason.getText()).append("\n");
        sb.append(binding.tvMaghribReason1.getText()).append("\n");
        sb.append(binding.tvMaghribReason2.getText()).append("\n\n");

        sb.append(binding.tvSubheadingIsha.getText()).append("\n");
        sb.append(binding.tvIshaTime.getText()).append("\n");
        sb.append(binding.tvSubheadingIshaReason.getText()).append("\n");
        sb.append(binding.tvIshaReason1.getText()).append("\n");
        sb.append(binding.tvIshaReason2.getText()).append("\n\n");

        sb.append(binding.tvSubheadingWisdom.getText()).append("\n");
        sb.append(binding.tvIslamWisdomDesc.getText());

        return sb.toString();
    }

    private static String getGuidanceCardContent(PageSalahWhyFiveTimesHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCard2Title.getText()).append("\n\n");
        sb.append(binding.tvGuidanceIntro.getText()).append("\n\n");
        sb.append(binding.tvGuidanceQuote1.getText()).append("\n").append(binding.tvGuidanceRef1.getText()).append("\n\n");
        sb.append(binding.tvGuidanceQuote2.getText()).append("\n").append(binding.tvGuidanceRef2.getText()).append("\n\n");
        sb.append(binding.tvGuidanceQuote3.getText()).append("\n").append(binding.tvGuidanceRef3.getText());
        return sb.toString();
    }

    private static String getFullArticleContent(PageSalahWhyFiveTimesHistoryBinding binding, boolean isBn) {
        return binding.tvHeaderTitle.getText() + "\n\n" +
                getReasonsCardContent(binding, isBn) + "\n\n" +
                "--------------------\n\n" +
                getGuidanceCardContent(binding, isBn);
    }
}
