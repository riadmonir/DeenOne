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
import com.devflux.deenone.databinding.PageSalahFiftyRewardHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FiftyRewardHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "fifty_reward_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahFiftyRewardHistoryBinding binding = PageSalahFiftyRewardHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Dynamic Header and Texts
        if (isBn) {
            binding.tvHeaderTitle.setText("পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব");
            binding.tvCardTitle.setText("পাঁচ ওয়াক্ত নামাজ পড়লে পঞ্চাশ ওয়াক্ত নামাজের সওয়াব পাওয়া যায় কিভাবে");
            binding.tvRewardIntro.setText("মেরাজের রাতে আল্লাহ তাআলা রাসুলুল্লাহ (সাঃ) কে প্রথমে ৫০ ওয়াক্ত নামাজের আদেশ দেন। তবে পরে মুসা (আঃ)-এর পরামর্শে রাসুলুল্লাহ (সাঃ) আল্লাহর কাছে নামাজের সংখ্যা কমানোর অনুরোধ করেন এবং শেষ পর্যন্ত এটি ৫ ওয়াক্তে নির্ধারিত হয়। তবে আল্লাহ তাআলা ৫ ওয়াক্ত নামাজ পড়ার ফলে ৫০ ওয়াক্ত নামাজের সওয়াব দেয়ার প্রতিশ্রুতি দেন।");
            binding.tvSubheadingHadithDesc.setText("হাদিসের বর্ণনা -");
            binding.tvHadithDesc.setText("এই বিষয়ে বিভিন্ন হাদিস বর্ণিত হয়েছে। সহীহ বুখারি ও মুসলিমে উল্লেখিত হাদিস থেকে বিষয়টি স্পষ্ট হয়ে ওঠে।");
            binding.tvRewardHadithQuote.setText("\"মেরাজের রাতে নবী মুহাম্মদ (সাঃ) আল্লাহর সাথে সাক্ষাৎ করেন এবং আল্লাহ তাকে ৫০ ওয়াক্ত নামাজের আদেশ দেন। পরবর্তীতে মুসা (আঃ)-এর পরামর্শে আল্লাহ তা ৫ ওয়াক্তে কমিয়ে দেন। আল্লাহ তাআলা বলেন, 'এই ৫ ওয়াক্ত নামাজ ৫০ ওয়াক্ত নামাজের সমান হবে এবং আমার কথা কখনও পরিবর্তিত হয় না।'\"");
            binding.tvHadithRef.setText("সহীহ বুখারী: ৩৪৯");
            binding.tvSubheadingRewardExplanation.setText("সওয়াবের ব্যাখ্যা -");
            binding.tvExplanationIntro.setText("আল্লাহর করুণা এবং দয়া এমন যে, তিনি ৫ ওয়াক্ত নামাজ পড়ার পরও ৫০ ওয়াক্ত নামাজের সওয়াব প্রদান করেন। এর ব্যাখ্যা নিম্নরূপ হতে পারে:");
            binding.tvGracePoint.setText("আল্লাহর অনুগ্রহ: আল্লাহর অনুগ্রহ অপরিসীম। তিনি তাঁর বান্দাদের উপর সহজসাধ্য ইবাদতের মাধ্যমে অধিক সওয়াব প্রদানের প্রতিশ্রুতি দেন।");
            binding.tvWisdomPoint.setText("মহান প্রজ্ঞা: আল্লাহ তাআলার মহত্ব এবং প্রজ্ঞা অনুযায়ী, তিনি তাঁর বান্দাদের অল্প ইবাদতের মাধ্যমে অধিক পুরস্কার প্রদান করতে সক্ষম।");
            binding.tvIntentionPoint.setText("নিয়তের মূল্য: ইসলামে নিয়ত বা উদ্দেশ্য খুবই গুরুত্বপূর্ণ। যখন একজন মুসলিম সঠিক নিয়তে ৫ ওয়াক্ত নামাজ আদায় করেন, আল্লাহ তার উদ্দেশ্য এবং ইবাদতের মূল্যায়ন করেন এবং তাকে অধিক সওয়াব প্রদান করেন।");
            binding.tvSubheadingConclusion.setText("উপসংহার -");
            binding.tvConclusionDesc.setText("৫ ওয়াক্ত নামাজের মাধ্যমে ৫০ ওয়াক্ত নামাজের সওয়াব পাওয়ার বিষয়টি আল্লাহ তাআলার এক বিশেষ করুণা এবং দয়া। এটি মুসলিমদের জন্য একটি বড় প্রেরণা এবং পুরস্কার। আল্লাহর আদেশ এবং প্রজ্ঞা অনুযায়ী, এই বিধান মুসলিম উম্মাহর জন্য একটি বড় নিয়ামত হিসেবে গণ্য হয়।");
        } else {
            binding.tvHeaderTitle.setText("Reward of Fifty Prayers by Praying Five");
            binding.tvCardTitle.setText("How Performing Five Prayers Yields the Reward of Fifty Prayers");
            binding.tvRewardIntro.setText("On the night of Miraj, Allah Almighty initially commanded Prophet Muhammad (PBUH) to perform fifty daily prayers. Later, on the advice of Prophet Musa (AS), the Prophet requested Allah to reduce the count until it was set to five. However, Allah promised the reward of fifty prayers for performing these five.");
            binding.tvSubheadingHadithDesc.setText("Hadith Narration -");
            binding.tvHadithDesc.setText("Various hadiths narrate this event. The authentic narrations in Sahih al-Bukhari and Muslim elucidate this clearly.");
            binding.tvRewardHadithQuote.setText("\"On the night of Miraj, Prophet Muhammad (PBUH) met Allah, and Allah ordained fifty prayers for him. Later, on Musa's advice, Allah reduced them to five. Allah declared: 'These five prayers will equal fifty in reward, and My word does not change.'\"");
            binding.tvHadithRef.setText("Sahih al-Bukhari: 349");
            binding.tvSubheadingRewardExplanation.setText("Explanation of the Reward -");
            binding.tvExplanationIntro.setText("Allah's grace and mercy are such that He grants the reward of fifty prayers for performing five. The wisdom includes:");
            binding.tvGracePoint.setText("Grace of Allah: Allah's bounty is boundless. He promises tremendous rewards for manageable acts of devotion from His servants.");
            binding.tvWisdomPoint.setText("Divine Wisdom: In accordance with His majesty and wisdom, Allah is able to bestow immense rewards for sincere devotion.");
            binding.tvIntentionPoint.setText("Value of Sincere Intention: In Islam, intention is paramount. When a believer performs five prayers with pure intention, Allah honors the devotion with multiplied reward.");
            binding.tvSubheadingConclusion.setText("Conclusion -");
            binding.tvConclusionDesc.setText("Receiving the reward of fifty prayers through five is a distinctive mercy and gift from Allah. It serves as great encouragement and a magnificent blessing for the Muslim Ummah.");
        }

        // Back Button
        binding.btnBackFiftyRewardHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackFiftyRewardHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsFiftyRewardHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsFiftyRewardHistory);

        // Main Card Menu
        binding.btnMenuCardFiftyReward.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব" : "Reward of Fifty Prayers by Praying Five", getCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahFiftyRewardHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvRewardIntro);
        bodyViews.add(binding.tvHadithDesc);
        bodyViews.add(binding.tvRewardHadithQuote);
        bodyViews.add(binding.tvExplanationIntro);
        bodyViews.add(binding.tvGracePoint);
        bodyViews.add(binding.tvWisdomPoint);
        bodyViews.add(binding.tvIntentionPoint);
        bodyViews.add(binding.tvConclusionDesc);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahFiftyRewardHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব" : "Reward of Fifty Prayers by Praying Five", getCardContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব" : "Reward of Fifty Prayers by Praying Five", getCardContent(binding, isBn), isBn);
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

    private static String getCardContent(PageSalahFiftyRewardHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCardTitle.getText()).append("\n\n");
        sb.append(binding.tvRewardIntro.getText()).append("\n\n");
        sb.append(binding.tvSubheadingHadithDesc.getText()).append("\n");
        sb.append(binding.tvHadithDesc.getText()).append("\n\n");
        sb.append(binding.tvRewardHadithQuote.getText()).append("\n");
        sb.append(binding.tvHadithRef.getText()).append("\n\n");
        sb.append(binding.tvSubheadingRewardExplanation.getText()).append("\n");
        sb.append(binding.tvExplanationIntro.getText()).append("\n\n");
        sb.append(binding.tvGracePoint.getText()).append("\n\n");
        sb.append(binding.tvWisdomPoint.getText()).append("\n\n");
        sb.append(binding.tvIntentionPoint.getText()).append("\n\n");
        sb.append(binding.tvSubheadingConclusion.getText()).append("\n");
        sb.append(binding.tvConclusionDesc.getText());
        return sb.toString();
    }
}
