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
import com.devflux.deenone.databinding.PageSalahProphetsHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProphetsSalahHistoryPageDialog {

    private static final String PREFS_NAME = "salah_history_prefs";
    private static final String KEY_FONT_SIZE = "prophets_salah_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahProphetsHistoryBinding binding = PageSalahProphetsHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header and Card Titles
        String title = isBn ? "কোন নবীর উপর কোন নামাজ ফরজ হয়েছিল" : "Which Prayer Was Prescribed to Which Prophet";
        binding.tvHeaderTitle.setText(title);
        binding.tvCardTitle.setText(title);

        // Dynamic Dual-Language Content
        if (!isBn) {
            binding.tvProphetsIntro.setText("Throughout Islamic history, various forms of prayer were prescribed to different Prophets. However, the obligation of the five daily prayers was specifically ordained for the Prophet Muhammad (PBUH) and his Ummah. The Quran and Hadith mention the prescription of prayer upon earlier Prophets, though not in the same detailed format. Below is an account of the prayer commands given to several Prophets:");

            binding.tvSubheadingIbrahim.setText("Ibrahim (AS) -");
            binding.tvIbrahimDesc.setText("Prayer was ordained upon Prophet Ibrahim (AS). The Quran narrates that he and his descendants established regular prayer.");
            binding.tvIbrahimQuote.setText("\"O our Lord, I have settled some of my descendants in an uncultivated valley near Your sacred House, our Lord, that they may establish prayer.\"");
            binding.tvRefIbrahim.setText("Surah Ibrahim: 14:37");

            binding.tvSubheadingIsmail.setText("Ismail (AS) -");
            binding.tvIsmailDesc.setText("Prophet Ismail (AS), son of Ibrahim (AS), was also commanded to establish prayer.");
            binding.tvIsmailQuote.setText("\"And mention in the Book, Ismail. Indeed, he was true to his promise, and he was a messenger and a prophet. And he used to enjoin on his people prayer and zakah and was to his Lord pleasing.\"");
            binding.tvRefIsmail.setText("Surah Maryam: 19:54-55");

            binding.tvSubheadingMusa.setText("Musa (AS) -");
            binding.tvMusaDesc.setText("Prophet Musa (AS) was also commanded to establish prayer directly by Allah.");
            binding.tvMusaQuote.setText("\"Indeed, I am Allah. There is no deity except Me, so worship Me and establish prayer for My remembrance.\"");
            binding.tvRefMusa.setText("Surah Taha: 20:14");

            binding.tvSubheadingIsa.setText("Isa (AS) -");
            binding.tvIsaDesc.setText("Prophet Isa (AS) was also blessed and instructed to observe prayer.");
            binding.tvIsaQuote.setText("\"He said, 'Indeed, I am the servant of Allah. He has given me the Scripture and made me a prophet. And He has made me blessed wherever I am and has enjoined upon me prayer and zakah as long as I remain alive.'\"");
            binding.tvRefIsa.setText("Surah Maryam: 19:30-31");

            binding.tvSubheadingMuhammad.setText("Muhammad (PBUH) -");
            binding.tvMuhammadDesc.setText("On the night of Miraj, the five daily prayers were made obligatory upon Prophet Muhammad (PBUH) and the entire Muslim Ummah.");
            binding.tvMuhammadQuote.setText("\"On the night of Miraj, Prophet Muhammad (PBUH) met Allah, and fifty prayers were initially prescribed. Later, upon the counsel of Musa (AS), Allah reduced it to five daily prayers with the reward of fifty.\"");
            binding.tvRefMuhammad.setText("Sahih Bukhari: 349");
        }

        // Back Button
        binding.btnBackProphetsSalahHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackProphetsSalahHistory);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button (Font Size + Reader Actions)
        binding.btnSettingsProphetsSalahHistory.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsProphetsSalahHistory);

        // Main Card Menu
        binding.btnMenuCardProphetsSalah.setOnClickListener(v -> {
            showCardActionDialog(activity, title, getCardContent(binding, isBn), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageSalahProphetsHistoryBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvProphetsIntro);
        bodyViews.add(binding.tvIbrahimDesc);
        bodyViews.add(binding.tvIbrahimQuote);
        bodyViews.add(binding.tvIsmailDesc);
        bodyViews.add(binding.tvIsmailQuote);
        bodyViews.add(binding.tvMusaDesc);
        bodyViews.add(binding.tvMusaQuote);
        bodyViews.add(binding.tvIsaDesc);
        bodyViews.add(binding.tvIsaQuote);
        bodyViews.add(binding.tvMuhammadDesc);
        bodyViews.add(binding.tvMuhammadQuote);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageSalahProphetsHistoryBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "কোন নবীর উপর কোন নামাজ ফরজ হয়েছিল" : "Which Prayer Was Prescribed to Which Prophet", getCardContent(binding, isBn), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "কোন নবীর উপর কোন নামাজ ফরজ হয়েছিল" : "Which Prayer Was Prescribed to Which Prophet", getCardContent(binding, isBn), isBn);
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

    private static String getCardContent(PageSalahProphetsHistoryBinding binding, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(binding.tvCardTitle.getText()).append("\n\n");
        sb.append(binding.tvProphetsIntro.getText()).append("\n\n");

        sb.append(binding.tvSubheadingIbrahim.getText()).append("\n");
        sb.append(binding.tvIbrahimDesc.getText()).append("\n\n");
        sb.append(binding.tvIbrahimQuote.getText()).append("\n");
        sb.append(binding.tvRefIbrahim.getText()).append("\n\n");

        sb.append(binding.tvSubheadingIsmail.getText()).append("\n");
        sb.append(binding.tvIsmailDesc.getText()).append("\n\n");
        sb.append(binding.tvIsmailQuote.getText()).append("\n");
        sb.append(binding.tvRefIsmail.getText()).append("\n\n");

        sb.append(binding.tvSubheadingMusa.getText()).append("\n");
        sb.append(binding.tvMusaDesc.getText()).append("\n\n");
        sb.append(binding.tvMusaQuote.getText()).append("\n");
        sb.append(binding.tvRefMusa.getText()).append("\n\n");

        sb.append(binding.tvSubheadingIsa.getText()).append("\n");
        sb.append(binding.tvIsaDesc.getText()).append("\n\n");
        sb.append(binding.tvIsaQuote.getText()).append("\n");
        sb.append(binding.tvRefIsa.getText()).append("\n\n");

        sb.append(binding.tvSubheadingMuhammad.getText()).append("\n");
        sb.append(binding.tvMuhammadDesc.getText()).append("\n\n");
        sb.append(binding.tvMuhammadQuote.getText()).append("\n");
        sb.append(binding.tvRefMuhammad.getText());

        return sb.toString();
    }
}
