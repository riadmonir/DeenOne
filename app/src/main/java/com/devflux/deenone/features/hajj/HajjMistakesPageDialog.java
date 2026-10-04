package com.devflux.deenone.features.hajj;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemHajjMistakesCardBinding;
import com.devflux.deenone.databinding.PageHajjMistakesBinding;
import com.devflux.deenone.features.hajj.data.HajjMistakesContentRepository;
import com.devflux.deenone.features.hajj.model.HajjMistakesCardItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HajjMistakesPageDialog {

    private static final String PREFS_NAME = "hajj_mistakes_prefs";
    private static final String KEY_FONT_SIZE = "hajj_mistakes_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjMistakesBinding binding = PageHajjMistakesBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (adaptive to language)
        String displayTitle = isBn ? "ভুল ত্রুটি" : "Common Mistakes & Errors";
        binding.tvMistakesTitle.setText(displayTitle);

        // Back Button with Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnBackMistakes);
        binding.btnBackMistakes.setOnClickListener(v -> dialog.dismiss());

        // Prepare Cards from Repository
        List<HajjMistakesCardItem> items = HajjMistakesContentRepository.getMistakesCards();

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        HajjMistakesAdapter adapter = new HajjMistakesAdapter(items, savedSize, isBn);
        binding.rvMistakesCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMistakesCards.setAdapter(adapter);

        // Settings Gear Action Button
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsMistakes);
        binding.btnSettingsMistakes.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, displayTitle, prefs, isBn);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, HajjMistakesAdapter adapter, List<HajjMistakesCardItem> items, String pageTitle, SharedPreferences prefs, boolean isBn) {
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
                        for (HajjMistakesCardItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (HajjMistakesCardItem it : items) it.setExpanded(false);
                        adapter.notifyDataSetChanged();
                    } else if (which >= 2 && which <= 5) {
                        float chosen = sizes[which - 2];
                        adapter.setFontSize(chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 6) {
                        copyToClipboard(activity, pageTitle, getFullText(pageTitle, items, isBn));
                    } else if (which == 7) {
                        shareContent(activity, pageTitle, getFullText(pageTitle, items, isBn));
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            cm.setPrimaryClip(clip);
        }
    }

    private static void shareContent(Context context, String subject, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, subject));
    }

    private static String getFullText(String title, List<HajjMistakesCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("🕋 ").append(title).append("\n\n");
        for (int i = 0; i < items.size(); i++) {
            HajjMistakesCardItem it = items.get(i);
            sb.append(i + 1).append(". ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }
        sb.append("— DeenOne Islamic App");
        return sb.toString();
    }

    // ==========================================
    // ADAPTER CLASS
    // ==========================================
    private static class HajjMistakesAdapter extends RecyclerView.Adapter<HajjMistakesAdapter.ViewHolder> {
        private final List<HajjMistakesCardItem> list;
        private float fontSize;
        private final boolean isBn;

        public HajjMistakesAdapter(List<HajjMistakesCardItem> list, float fontSize, boolean isBn) {
            this.list = list;
            this.fontSize = fontSize;
            this.isBn = isBn;
        }

        public void setFontSize(float newSize) {
            this.fontSize = newSize;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemHajjMistakesCardBinding binding = ItemHajjMistakesCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HajjMistakesCardItem item = list.get(position);

            holder.binding.tvMistakesItemTitle.setText(item.getTitle(isBn));
            holder.binding.tvMistakesItemPreview.setText(item.getPreview(isBn));

            // Dynamic Content with HTML styling
            String fullContent = formatHtml(item.getFullContent(isBn));
            holder.binding.tvMistakesItemFullContent.setText(HtmlCompat.fromHtml(fullContent, HtmlCompat.FROM_HTML_MODE_LEGACY));

            // Apply font size
            holder.binding.tvMistakesItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            holder.binding.tvMistakesItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize - 0.5f);

            // Expansion State
            boolean expanded = item.isExpanded();
            holder.binding.tvMistakesItemFullContent.setVisibility(expanded ? View.VISIBLE : View.GONE);
            holder.binding.tvMistakesItemPreview.setVisibility(expanded ? View.GONE : View.VISIBLE);
            holder.binding.tvMistakesToggleText.setText(expanded ? (isBn ? "সংক্ষেপ" : "Collapse") : (isBn ? "বিস্তারিত" : "Details"));
            holder.binding.ivMistakesToggleChevron.setRotation(expanded ? 180f : 0f);

            // Click listener on toggle row and card
            View.OnClickListener toggleListener = v -> {
                boolean nextState = !item.isExpanded();
                item.setExpanded(nextState);
                holder.binding.tvMistakesItemFullContent.setVisibility(nextState ? View.VISIBLE : View.GONE);
                holder.binding.tvMistakesItemPreview.setVisibility(nextState ? View.GONE : View.VISIBLE);
                holder.binding.tvMistakesToggleText.setText(nextState ? (isBn ? "সংক্ষেপ" : "Collapse") : (isBn ? "বিস্তারিত" : "Details"));
                holder.binding.ivMistakesToggleChevron.animate().rotation(nextState ? 180f : 0f).setDuration(200).start();
            };

            TouchAnimationUtil.attachTouchSpring(holder.binding.layoutMistakesToggleExpand);
            holder.binding.layoutMistakesToggleExpand.setOnClickListener(toggleListener);

            holder.binding.cardMistakesItem.setOnClickListener(toggleListener);
        }

        private String formatHtml(String text) {
            if (text == null) return "";
            String[] lines = text.split("\n");
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    sb.append("<br><br>");
                } else if (containsArabic(trimmed)) {
                    sb.append("<big><b>").append(trimmed).append("</b></big><br>");
                } else {
                    sb.append(trimmed).append("<br>");
                }
            }
            return sb.toString().replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
        }

        private boolean containsArabic(String text) {
            if (text == null) return false;
            for (char c : text.toCharArray()) {
                if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ARABIC) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjMistakesCardBinding binding;

            public ViewHolder(@NonNull ItemHajjMistakesCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
