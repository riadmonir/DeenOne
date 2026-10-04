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
import com.devflux.deenone.databinding.PageSalahFiftyToFiveHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FiftyToFiveHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "fifty_to_five_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahFiftyToFiveHistoryBinding binding = PageSalahFiftyToFiveHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Dynamic Header and Card Content
        if (isBn) {
            binding.tvHeaderTitle.setText("পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস");
            binding.tvCardTitle.setText("পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস");
            binding.tvHistoryIntro.setText("মেরাজের রাতের ঘটনা ইসলামের ইতিহাসে একটি বিশেষ স্থান অধিকার করে আছে, কারণ এই রাতেই পঞ্চাশ ওয়াক্ত নামাজ ফরজ হয়েছিল এবং পরে তা ৫ ওয়াক্তে কমানো হয়। এখানে মেরাজের রাতে নামাজ ফরজ হওয়ার সম্পূর্ণ ইতিহাস তুলে ধরা হলো -");
            binding.tvSubheadingMirajEvent.setText("মেরাজের রাতের ঘটনা -");
            binding.tvMirajNightDesc.setText("মেরাজ হলো রাসুলুল্লাহ (সাঃ)-এর এক অলৌকিক ভ্রমণ, যা ইসরা এবং মেরাজ নামে দুই ভাগে বিভক্ত। ইসরা হলো মক্কা থেকে বায়তুল মুকাদ্দাস পর্যন্ত রাতের ভ্রমণ এবং মেরাজ হলো বায়তুল মুকাদ্দাস থেকে সাত আসমান হয়ে আল্লাহর দরবারে পৌঁছানো।");
            binding.tvIsraQuote.setText("\"পবিত্র এবং মহিমান্বিত তিনি, যিনি তাঁর বান্দাকে এক রাতে মসজিদুল হারাম থেকে মসজিদুল আকসা পর্যন্ত ভ্রমণ করিয়েছেন।\"");
            binding.tvIsraRef.setText("সূরা আল-ইসরা: ১৭:১");
            binding.tvSubheadingFiftyOrder.setText("পঞ্চাশ ওয়াক্ত নামাজের আদেশ -");
            binding.tvFiftyOrderDesc.setText("মেরাজের রাতে আল্লাহ তাআলার সাথে সাক্ষাতের সময়, রাসুলুল্লাহ (সাঃ)-এর উপর প্রথমে পঞ্চাশ ওয়াক্ত নামাজ ফরজ করা হয়। এই সময়ে রাসুলুল্লাহ (সাঃ) সপ্তম আসমানে আল্লাহর সাথে সাক্ষাৎ করেন এবং এই আদেশ পান।");
            binding.tvFiftyOrderQuote.setText("\"মেরাজের রাতে নবী মুহাম্মদ (সাঃ) আল্লাহর সাথে সাক্ষাৎ করেন এবং আল্লাহ তাকে পঞ্চাশ ওয়াক্ত নামাজের আদেশ দেন।\"");
            binding.tvFiftyOrderRef.setText("সহীহ বুখারী: ৩৪৯");
            binding.tvSubheadingReductionProcess.setText("নামাজ কমানোর প্রক্রিয়া -");
            binding.tvReductionProcessDesc1.setText("নবী মুহাম্মদ (সাঃ) পঞ্চাশ ওয়াক্ত নামাজের আদেশ পাওয়ার পর মুসা (আঃ)-এর পরামর্শে আল্লাহর কাছে প্রার্থনা করেন এবং ধাপে ধাপে নামাজের সংখ্যা কমানো হয়।");
            binding.tvReductionHadithQuote.setText("\"নবী মুহাম্মদ (সাঃ) বলেন, 'আমি ফিরে এসে মুসা (আঃ) এর কাছে গেলাম। তিনি বললেন, 'তোমার উম্মতের জন্য ৫০ ওয়াক্ত নামাজ অত্যন্ত কঠিন হবে। তোমার পালনকর্তার কাছে আবার যাও এবং কমানোর অনুরোধ করো।' আমি আল্লাহর কাছে আবার ফিরে গেলাম এবং আল্লাহ তাআলা তা ৪৫ ওয়াক্তে কমিয়ে দিলেন।'\"");
            binding.tvReductionRef.setText("সহীহ মুসলিম: ১৬২");
            binding.tvReductionProcessDesc2.setText("এই প্রক্রিয়া চলতে থাকে এবং প্রতি বারেই মুসা (আঃ)-এর পরামর্শে রাসুলুল্লাহ (সাঃ) আল্লাহর কাছে যান এবং আল্লাহ তাআলা তা কমিয়ে দেন। শেষ পর্যন্ত এটি ৫ ওয়াক্ত নামাজে নির্ধারিত হয়।");
            binding.tvRewardQuote.setText("\"শেষে আল্লাহ তাআলা বললেন, 'এই ৫ ওয়াক্ত নামাজ পঞ্চাশ ওয়াক্ত নামাজের সমান হবে এবং আমার কথা কখনও পরিবর্তিত হয় না।'\"");
            binding.tvRewardRef.setText("সহীহ বুখারী: ৩৪৯");
            binding.tvSubheadingRewardConclusion.setText("৫ ওয়াক্ত নামাজের প্রতিদান -");
            binding.tvRewardConclusionDesc.setText("আল্লাহ তাআলার করুণা ও দয়া এমন যে, তিনি ৫ ওয়াক্ত নামাজের মাধ্যমে পঞ্চাশ ওয়াক্ত নামাজের সওয়াব প্রদান করবেন। এটি আল্লাহর বিশেষ অনুগ্রহ, যা মুসলিমদের জন্য একটি বড় প্রেরণা।");
        } else {
            binding.tvHeaderTitle.setText("History of Fifty Prayers");
            binding.tvCardTitle.setText("History of Fifty Daily Prayers");
            binding.tvHistoryIntro.setText("The event of the Night of Miraj holds a sacred place in Islamic history, as fifty prayers were initially made obligatory that night before being reduced to five. Here is the full history of the prescription of prayer on Miraj -");
            binding.tvSubheadingMirajEvent.setText("The Event of the Night of Miraj -");
            binding.tvMirajNightDesc.setText("Miraj is the miraculous journey of Prophet Muhammad (PBUH), comprising Isra and Miraj. Isra is the nocturnal journey from Makkah to Bayt al-Maqdis, and Miraj is the ascension from Bayt al-Maqdis through the seven heavens into the presence of Allah.");
            binding.tvIsraQuote.setText("\"Exalted is He who took His Servant by night from al-Masjid al-Haram to al-Masjid al-Aqsa.\"");
            binding.tvIsraRef.setText("Surah Al-Isra: 17:1");
            binding.tvSubheadingFiftyOrder.setText("The Command of Fifty Prayers -");
            binding.tvFiftyOrderDesc.setText("During the meeting with Allah on the night of Miraj, fifty daily prayers were initially ordained upon Prophet Muhammad (PBUH) while in the seventh heaven.");
            binding.tvFiftyOrderQuote.setText("\"On the night of Miraj, Prophet Muhammad (PBUH) met Allah, and Allah ordained fifty prayers upon him.\"");
            binding.tvFiftyOrderRef.setText("Sahih al-Bukhari: 349");
            binding.tvSubheadingReductionProcess.setText("The Process of Prayer Reduction -");
            binding.tvReductionProcessDesc1.setText("After receiving the command of fifty prayers, Prophet Muhammad (PBUH), upon the counsel of Prophet Musa (AS), implored Allah repeatedly, gradually reducing the count.");
            binding.tvReductionHadithQuote.setText("\"Prophet Muhammad (PBUH) said, 'I returned and passed by Musa, who asked, 'Fifty prayers will be too burdensome for your followers. Return to your Lord and ask for a reduction.' I returned to Allah and He reduced it to forty-five.'\"");
            binding.tvReductionRef.setText("Sahih Muslim: 162");
            binding.tvReductionProcessDesc2.setText("This consultation continued, with Prophet Muhammad (PBUH) returning to Allah upon Musa's advice, until the obligation was finalized at five prayers.");
            binding.tvRewardQuote.setText("\"Finally Allah Almighty said, 'These five prayers will equal fifty in reward, and My word does not change.'\"");
            binding.tvRewardRef.setText("Sahih al-Bukhari: 349");
            binding.tvSubheadingRewardConclusion.setText("Reward of Five Daily Prayers -");
            binding.tvRewardConclusionDesc.setText("It is from Allah's magnificent compassion that He rewards five daily prayers with the value of fifty. This divine grace remains a profound source of motivation for every believer.");
        }

        // Back Button
        binding.btnBackFiftyToFiveHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackFiftyToFiveHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsFiftyToFiveHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsFiftyToFiveHistory);

        // Main Card Menu
        binding.btnMenuCardFiftyToFive.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস" : "History of Fifty Prayers", getCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahFiftyToFiveHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvHistoryIntro);
        bodyViews.add(binding.tvMirajNightDesc);
        bodyViews.add(binding.tvIsraQuote);
        bodyViews.add(binding.tvFiftyOrderDesc);
        bodyViews.add(binding.tvFiftyOrderQuote);
        bodyViews.add(binding.tvReductionProcessDesc1);
        bodyViews.add(binding.tvReductionHadithQuote);
        bodyViews.add(binding.tvReductionProcessDesc2);
        bodyViews.add(binding.tvRewardQuote);
        bodyViews.add(binding.tvRewardConclusionDesc);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahFiftyToFiveHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস" : "History of Fifty Prayers", getCardContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস" : "History of Fifty Prayers", getCardContent(binding, isBn), isBn);
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

    private static String getCardContent(PageSalahFiftyToFiveHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCardTitle.getText()).append("\n\n");
        sb.append(binding.tvHistoryIntro.getText()).append("\n\n");
        sb.append(binding.tvSubheadingMirajEvent.getText()).append("\n");
        sb.append(binding.tvMirajNightDesc.getText()).append("\n\n");
        sb.append(binding.tvIsraQuote.getText()).append("\n");
        sb.append(binding.tvIsraRef.getText()).append("\n\n");
        sb.append(binding.tvSubheadingFiftyOrder.getText()).append("\n");
        sb.append(binding.tvFiftyOrderDesc.getText()).append("\n\n");
        sb.append(binding.tvFiftyOrderQuote.getText()).append("\n");
        sb.append(binding.tvFiftyOrderRef.getText()).append("\n\n");
        sb.append(binding.tvSubheadingReductionProcess.getText()).append("\n");
        sb.append(binding.tvReductionProcessDesc1.getText()).append("\n\n");
        sb.append(binding.tvReductionHadithQuote.getText()).append("\n");
        sb.append(binding.tvReductionRef.getText()).append("\n\n");
        sb.append(binding.tvReductionProcessDesc2.getText()).append("\n\n");
        sb.append(binding.tvRewardQuote.getText()).append("\n");
        sb.append(binding.tvRewardRef.getText()).append("\n\n");
        sb.append(binding.tvSubheadingRewardConclusion.getText()).append("\n");
        sb.append(binding.tvRewardConclusionDesc.getText());
        return sb.toString();
    }
}
