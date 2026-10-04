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
import com.devflux.deenone.databinding.PageAzanSunnahMustahabBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AzanSunnahMustahabPageDialog {

    private static final String PREFS_NAME = "salah_azan_prefs";
    private static final String KEY_FONT_SIZE = "azan_sunnah_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAzanSunnahMustahabBinding binding = PageAzanSunnahMustahabBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan");
        binding.tvCardTitle.setText(isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan");

        binding.tvPoint1.setText(isBn
                ? "• মসজিদের বাইরে উঁচু জায়গায় দাঁড়িয়ে আজান দেয়া।"
                : "• Calling the Adhan from a raised place outside the mosque.");

        binding.tvPoint2.setText(isBn
                ? "• যথাসম্ভব উচ্চস্বরে আজান দেয়া দরকার। তবে একা একা নামাজ আদায়ের জন্য স্বাভাবিকভাবে আজান দিলেও চলবে।"
                : "• Calling the Adhan in a loud voice as much as possible. However, if praying alone, calling it normally is sufficient.");

        binding.tvPoint3.setText(isBn
                ? "• আজানের সময় শাহাদাত আঙ্গুল দ্বারা উভয় কানের ছিদ্র বন্ধ করে রাখা মুস্তাহাব।"
                : "• It is recommended (Mustahab) to place the index fingers into the ear canals during Adhan.");

        binding.tvPoint4.setText(isBn
                ? "• মদ ও গুন্নাহ আদায়পূর্বক আজানের শব্দগুলো লম্বা করে থেমে থেমে বলা সুন্নত, যাতে প্রত্যেক বাক্য উচ্চারণের পর শ্রোতারা জবাব দিতে পারে।"
                : "• It is Sunnah to pronounce the words of Adhan distinctly with elongation and pauses, allowing listeners to respond after each sentence.");

        binding.tvPoint5.setText(isBn
                ? "• 'হাইয়্যা আলাস সালাহ' বলার সময় ডান দিকে ও 'হাইয়্যা আলাল ফালাহ' বলার সময় বাম দিকে মুখ ফিরানো সুন্নত।"
                : "• It is Sunnah to turn the face to the right when saying 'Hayya 'alas-Salah' and to the left when saying 'Hayya 'alal-Falah'.");

        binding.tvPoint6.setText(isBn
                ? "• আজান ও ইকামতের সময় কিবলার দিকে ফিরে থাকা সুন্নত।"
                : "• Facing the Qiblah during Adhan and Iqamah is Sunnah.");

        binding.tvPoint7.setText(isBn
                ? "• আজান দেয়ার সময় 'হদসে আকবর' বা বড় নাপাকি—যেমন যৌনকারণে বীর্যপাত হওয়া বা স্বপ্নদোষ হওয়া—থেকে মুক্ত ও পবিত্র হওয়া সুন্নত। এ ধরনের নাপাকি দূর করতে নিয়মানুযায়ী গোসল করা জরুরি, তবে অজু-গোসলের পানি না থাকলে তায়াম্মুম দ্বারাও পবিত্রতা অর্জন করা যায়। গোসল ফরজ অবস্থায় আজান দেওয়া মাকরুহে তাহরীমী।"
                : "• Being pure from major impurity (Hadath Akbar) when giving the Adhan is Sunnah. Purifying through ritual bath (Ghusl) is obligatory, or through Tayammum if water is unavailable. Delivering the Adhan in a state of major ritual impurity is Makruh Tahrimi.");

        binding.tvPoint8.setText(isBn
                ? "• ফরজে আইন—অর্থাৎ যে আমল প্রত্যেক বালেগ ও বিবেকবান নর-নারীর ওপর সমানভাবে ফরজ, যেমন পাঁচ ওয়াক্ত নামাজ—ব্যতীত অন্য কোনো নামাজের জন্য আজান ও ইকামত দিতে হয় না। যেমন জানাজার নামাজ, বিতর নামাজ, ঈদের নামাজ, কুসুফ-খুসুফের নামাজ ও ইস্তেস্কার নামাজ।"
                : "• Adhan and Iqamah are not prescribed for any prayers other than the five daily obligatory prayers (Fard 'Ayn). For instance, Funeral (Janazah), Witr, Eid, Solar/Lunar Eclipse, and Rain-seeking (Istisqa) prayers have no Adhan or Iqamah.");

        binding.tvPoint9.setText(isBn
                ? "• আজান বা ইকামত দেয়ার সময় কথা বলা নিষেধ।"
                : "• Speaking during the delivery of Adhan or Iqamah is prohibited.");

        // Back Button with Spring
        binding.btnBackSunnahMustahab.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackSunnahMustahab);

        // Reader Font Size Management
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);
        applyFontSize(binding, savedSize);

        // Settings Action Button
        binding.btnSettingsSunnahMustahab.setOnClickListener(v -> {
            showReaderSettingsDialog(activity, binding, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSunnahMustahab);

        // Card Menu Button
        binding.btnMenuCardSunnahMustahab.setOnClickListener(v -> {
            showCardActionDialog(activity, isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan", getCardContent(binding), isBn);
        });

        dialog.show();
    }

    private static void applyFontSize(PageAzanSunnahMustahabBinding binding, float bodySp) {
        List<TextView> bodyViews = new ArrayList<>();
        bodyViews.add(binding.tvPoint1);
        bodyViews.add(binding.tvPoint2);
        bodyViews.add(binding.tvPoint3);
        bodyViews.add(binding.tvPoint4);
        bodyViews.add(binding.tvPoint5);
        bodyViews.add(binding.tvPoint6);
        bodyViews.add(binding.tvPoint7);
        bodyViews.add(binding.tvPoint8);
        bodyViews.add(binding.tvPoint9);

        for (TextView tv : bodyViews) {
            if (tv != null) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySp);
            }
        }
    }

    private static void showReaderSettingsDialog(Activity activity, PageAzanSunnahMustahabBinding binding, SharedPreferences prefs, boolean isBn) {
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
                        copyToClipboard(activity, isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan", getCardContent(binding), isBn);
                    } else if (which == 6) {
                        shareContent(activity, isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan", getCardContent(binding), isBn);
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

    private static String getCardContent(PageAzanSunnahMustahabBinding binding) {
        return binding.tvCardTitle.getText() + "\n\n" +
                binding.tvPoint1.getText() + "\n\n" +
                binding.tvPoint2.getText() + "\n\n" +
                binding.tvPoint3.getText() + "\n\n" +
                binding.tvPoint4.getText() + "\n\n" +
                binding.tvPoint5.getText() + "\n\n" +
                binding.tvPoint6.getText() + "\n\n" +
                binding.tvPoint7.getText() + "\n\n" +
                binding.tvPoint8.getText() + "\n\n" +
                binding.tvPoint9.getText();
    }
}
