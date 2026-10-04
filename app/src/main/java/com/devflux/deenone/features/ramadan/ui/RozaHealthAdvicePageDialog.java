package com.devflux.deenone.features.ramadan.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageRozaHealthAdviceBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaHealthAdviceAdapter;
import com.devflux.deenone.features.ramadan.data.RozaHealthAdviceRepository;
import com.devflux.deenone.features.ramadan.model.RozaHealthAdviceItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: রোজা:- রোজায় স্বাস্থ্য পরামর্শ (Health Advice Section).
 * 100% matches Hajj History design standard:
 * - Top header bar with back button, 19sp title, and reading settings gear button.
 * - Settings menu for font size (13, 15, 17, 19 sp), Expand all, Collapse all, Copy all, Share all.
 * - Expandable cards with 16dp radius, 17sp bold title, dynamic font size preview/full content.
 * - Button-only touch spring animation (STRICT ZERO touch animation on CardViews - Rule 7).
 * - Ultra-fast 60 FPS performance and offline-first cache.
 */
public class RozaHealthAdvicePageDialog {

    private static final String PREFS_NAME = "roza_health_advice_prefs";
    private static final String KEY_FONT_SIZE = "roza_health_advice_font_size";

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaHealthAdviceBinding binding = PageRozaHealthAdviceBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "রোজায় স্বাস্থ্য পরামর্শ" / "Health Tips for Fasting")
        String title = isBn ? "রোজায় স্বাস্থ্য পরামর্শ" : "Health Tips for Fasting";
        binding.tvRozaHealthAdviceHeaderTitle.setText(title);

        // Spring touch on back button ONLY (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackRozaHealthAdvice);
        binding.btnBackRozaHealthAdvice.setOnClickListener(v -> dialog.dismiss());

        // Preferences for font scaling
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        // Setup RecyclerView
        binding.rvRozaHealthAdviceList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvRozaHealthAdviceList.setHasFixedSize(true);
        binding.rvRozaHealthAdviceList.setItemViewCacheSize(10);

        List<RozaHealthAdviceItem> initialItems = RozaHealthAdviceRepository.getTopics(activity, updatedItems -> {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    RozaHealthAdviceAdapter adapter = (RozaHealthAdviceAdapter) binding.rvRozaHealthAdviceList.getAdapter();
                    if (adapter != null) {
                        adapter.updateData(updatedItems);
                    }
                }
            });
        });

        RozaHealthAdviceAdapter adapter = new RozaHealthAdviceAdapter(initialItems, savedSize, isBn);
        binding.rvRozaHealthAdviceList.setAdapter(adapter);

        // Settings Action Button (Reading settings dialog)
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsRozaHealthAdvice);
        binding.btnSettingsRozaHealthAdvice.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, initialItems, title, prefs, isBn);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, RozaHealthAdviceAdapter adapter,
                                           List<RozaHealthAdviceItem> items, String pageTitle,
                                           SharedPreferences prefs, boolean isBn) {
        String[] options;
        if (isBn) {
            options = new String[]{
                    "সবগুলো বিস্তারিত দেখুন",
                    "সবগুলো সংক্ষেপ করুন",
                    "ফন্ট সাইজ: ছোট (১৩ sp)",
                    "ফন্ট সাইজ: সাধারণ (১৫ sp)",
                    "ফন্ট সাইজ: প্রমিত (১৭ sp)",
                    "ফন্ট সাইজ: বড় (১৯ sp)",
                    "সম্পূর্ণ পাতা কপি করুন",
                    "সম্পূর্ণ পাতা শেয়ার করুন"
            };
        } else {
            options = new String[]{
                    "Expand All Topics",
                    "Collapse All Topics",
                    "Font Size: Small (13 sp)",
                    "Font Size: Normal (15 sp)",
                    "Font Size: Medium (17 sp)",
                    "Font Size: Large (19 sp)",
                    "Copy All Content",
                    "Share All Content"
            };
        }
        float[] sizes = {13.0f, 15.0f, 17.0f, 19.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (RozaHealthAdviceItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (RozaHealthAdviceItem it : items) it.setExpanded(false);
                        adapter.notifyDataSetChanged();
                    } else if (which >= 2 && which <= 5) {
                        float chosen = sizes[which - 2];
                        adapter.setFontSize(chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 6) {
                        copyToClipboard(activity, pageTitle, getFullAdviceText(pageTitle, items, isBn));
                    } else if (which == 7) {
                        shareContent(activity, pageTitle, getFullAdviceText(pageTitle, items, isBn));
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        boolean isBn = LocaleManager.isBengali(context);
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            Toast.makeText(context, isBn ? "স্বাস্থ্য পরামর্শ কপি করা হয়েছে" : "Health advice copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    private static String getFullAdviceText(String pageTitle, List<RozaHealthAdviceItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(pageTitle).append(" ===\n\n");
        for (RozaHealthAdviceItem it : items) {
            sb.append("🩺 ").append(it.getHeaderTitle(isBn)).append("\n\n");
            String raw = it.getDetails(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            String ref = it.getReference(isBn);
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append(isBn ? "রেফারেন্স: " : "Reference: ").append(ref).append("\n\n");
            }
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");
        return sb.toString();
    }
}
