package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageSalahFiveWaqtsHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FiveWaqtHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "five_waqts_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahFiveWaqtsHistoryBinding binding = PageSalahFiveWaqtsHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "পাঁচ ওয়াক্ত নামাজের ইতিহাস" : "History of Five Daily Prayers");

        // Back Button
        binding.btnBackFiveWaqtsHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackFiveWaqtsHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsFiveWaqtsHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsFiveWaqtsHistory);

        // Card 1 Menu
        binding.btnMenuCardMiraj.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "মেরাজের রজনী ও নামাজের বিধান" : "Night of Miraj & Obligation of Salah", getMirajCardContent(binding, isBn), isBn);
        });

        // Card 2 Menu
        binding.btnMenuCardBeginning.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "নামাজের সূচনা যেভাবে হয়েছিল" : "How Prayer Began", getBeginningCardContent(binding, isBn), isBn);
        });

        // Card 3 Menu
        binding.btnMenuCardQuranHadith.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "কোরআন ও হাদিসে নামাজের গুরুত্ব" : "Importance of Salah in Quran & Hadith", getQuranHadithCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahFiveWaqtsHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        // Miraj Card
        bodyViews.add(binding.tvMirajCardSubtitle);
        bodyViews.add(binding.tvMirajIsraDesc);
        bodyViews.add(binding.tvMirajIsraAyah);
        bodyViews.add(binding.tvMirajBuraqDesc);
        bodyViews.add(binding.tvMirajBaitulMuqaddasDesc);
        bodyViews.add(binding.tvMirajSkyJourneyDesc);
        bodyViews.add(binding.tvMirajSevenSkiesList);
        bodyViews.add(binding.tvMirajSidratulMuntahaDesc);
        bodyViews.add(binding.tvMirajMeetingAllahDesc);
        bodyViews.add(binding.tvMirajOrderDesc1);
        bodyViews.add(binding.tvMirajOrderDesc2);

        // Beginning Card
        bodyViews.add(binding.tvBeginningPara1);
        bodyViews.add(binding.tvBeginningPara2);
        bodyViews.add(binding.tvBeginningPara3);
        bodyViews.add(binding.tvBeginningPara4);
        bodyViews.add(binding.tvBeginningPara5);
        bodyViews.add(binding.tvBeginningPara6);

        // Quran Hadith Card
        bodyViews.add(binding.tvQuranVerse1);
        bodyViews.add(binding.tvQuranVerse2);
        bodyViews.add(binding.tvQuranVerse3);
        bodyViews.add(binding.tvHadith1);
        bodyViews.add(binding.tvHadith2);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahFiveWaqtsHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
        String[] options = isBn ? new String[]{
                "ফন্ট সাইজ: ছোট (১২ sp)",
                "ফন্ট সাইজ: সাধারণ (১৪ sp)",
                "ফন্ট সাইজ: প্রমিত (১৬ sp)",
                "ফন্ট সাইজ: বড় (১৮ sp)",
                "ফন্ট সাইজ: বিশাল (২০ sp)",
                "সম্পূর্ণ নিবন্ধ কপি করুন",
                "সম্পূর্ণ নিবন্ধ শেয়ার করুন"
        } : new String[]{
                "Font Size: Small (12 sp)",
                "Font Size: Normal (14 sp)",
                "Font Size: Medium (16 sp)",
                "Font Size: Large (18 sp)",
                "Font Size: Extra Large (20 sp)",
                "Copy Entire Article",
                "Share Entire Article"
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
                        copyToClipboard(activity, isBn ? "পাঁচ ওয়াক্ত নামাজের ইতিহাস" : "History of Five Daily Prayers", getFullArticleContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "পাঁচ ওয়াক্ত নামাজের ইতিহাস" : "History of Five Daily Prayers", getFullArticleContent(binding, isBn), isBn);
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

    private static String getMirajCardContent(PageSalahFiveWaqtsHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvMirajCardTitle.getText()).append("\n");
        sb.append(binding.tvMirajCardSubtitle.getText()).append("\n\n");
        sb.append(isBn ? "ইসরা:\n" : "Isra:\n").append(binding.tvMirajIsraDesc.getText()).append("\n");
        sb.append(binding.tvRefIsra.getText()).append("\n").append(binding.tvMirajIsraAyah.getText()).append("\n\n");
        sb.append(isBn ? "বুরাকে চড়া:\n" : "Riding Buraq:\n").append(binding.tvMirajBuraqDesc.getText()).append("\n\n");
        sb.append(isBn ? "বায়তুল মুকাদ্দাসে নামাজ আদায়:\n" : "Prayer at Baitul Muqaddas:\n").append(binding.tvMirajBaitulMuqaddasDesc.getText()).append("\n\n");
        sb.append(isBn ? "মেরাজ - আকাশে ভ্রমণ:\n" : "Miraj - Journey to the Heavens:\n").append(binding.tvMirajSkyJourneyDesc.getText()).append("\n\n");
        sb.append(binding.tvMirajSevenSkiesList.getText()).append("\n\n");
        sb.append(isBn ? "সিদরাতুল মুনতাহা:\n" : "Sidratul Muntaha:\n").append(binding.tvMirajSidratulMuntahaDesc.getText()).append("\n\n");
        sb.append(isBn ? "আল্লাহর সাথে সাক্ষাৎ:\n" : "Meeting with Allah:\n").append(binding.tvMirajMeetingAllahDesc.getText()).append("\n\n");
        sb.append(isBn ? "নামাজের নির্দেশনা:\n" : "Command of Salah:\n").append(binding.tvMirajOrderDesc1.getText()).append("\n");
        sb.append(binding.tvRefMirajOrder.getText()).append("\n").append(binding.tvMirajOrderDesc2.getText());
        return sb.toString();
    }

    private static String getBeginningCardContent(PageSalahFiveWaqtsHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvBeginningCardTitle.getText()).append("\n\n");
        sb.append(binding.tvBeginningPara1.getText()).append("\n\n");
        sb.append(isBn ? "হজরত আবু যার (রা.)-এর বরাতে একটি হাদিসে বলা হয়েছে:\n" : "In a Hadith on the authority of Abu Dharr (RA):\n");
        sb.append(binding.tvBeginningPara2.getText()).append("\n\n");
        sb.append(binding.tvBeginningPara3.getText()).append("\n\n");
        sb.append(binding.tvBeginningPara4.getText()).append("\n\n");
        sb.append(binding.tvBeginningPara5.getText()).append("\n\n");
        sb.append(binding.tvBeginningPara6.getText());
        return sb.toString();
    }

    private static String getQuranHadithCardContent(PageSalahFiveWaqtsHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvQuranHadithCardTitle.getText()).append("\n\n");
        sb.append(isBn ? "কোরআন:\n" : "Quran:\n");
        sb.append(binding.tvQuranVerse1.getText()).append("\n").append(binding.tvRefBaqarah.getText()).append("\n\n");
        sb.append(binding.tvQuranVerse2.getText()).append("\n").append(binding.tvRefNisa.getText()).append("\n\n");
        sb.append(binding.tvQuranVerse3.getText()).append("\n").append(binding.tvRefMuminun.getText()).append("\n\n");
        sb.append(isBn ? "হাদিস:\n" : "Hadith:\n");
        sb.append(binding.tvHadith1.getText()).append("\n").append(binding.tvRefBukhariPillars.getText()).append("\n\n");
        sb.append(binding.tvHadith2.getText()).append("\n").append(binding.tvRefMuslimPillars.getText());
        return sb.toString();
    }

    private static String getFullArticleContent(PageSalahFiveWaqtsHistoryBinding binding, boolean isBn) {
        return (isBn ? "পাঁচ ওয়াক্ত নামাজের ইতিহাস\n\n" : "History of Five Daily Prayers\n\n") +
                getMirajCardContent(binding, isBn) + "\n\n" +
                "--------------------\n\n" +
                getBeginningCardContent(binding, isBn) + "\n\n" +
                "--------------------\n\n" +
                getQuranHadithCardContent(binding, isBn);
    }
}
