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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemMinistryHajjCardBinding;
import com.devflux.deenone.databinding.PageMinistryHajjGuideBinding;
import com.devflux.deenone.features.hajj.data.MinistryHajjContentRepository;
import com.devflux.deenone.features.hajj.model.MinistryHajjGuideItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MinistryHajjGuidePageDialog {

    private static final String PREFS_NAME = "hajj_ministry_guide_prefs";
    private static final String KEY_FONT_SIZE = "ministry_hajj_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageMinistryHajjGuideBinding binding = PageMinistryHajjGuideBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (adaptive to language)
        binding.tvTitleMinistryHajj.setText(isBn ? "হজ ও উমরাহ সহায়িকা" : "Hajj & Umrah Guide");

        // Back Button with Spring
        binding.btnBackMinistryHajj.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackMinistryHajj);

        // Prepare 19 Items from screenshots
        List<MinistryHajjGuideItem> items = MinistryHajjContentRepository.getGuideItems();

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);

        MinistryHajjAdapter adapter = new MinistryHajjAdapter(items, savedSize, isBn);
        binding.rvMinistryHajjRules.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMinistryHajjRules.setAdapter(adapter);

        // Settings Gear Action Button
        binding.btnSettingsMinistryHajj.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsMinistryHajj);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, MinistryHajjAdapter adapter, List<MinistryHajjGuideItem> items, SharedPreferences prefs, boolean isBn) {
        String[] options;
        if (isBn) {
            options = new String[]{
                    "সবগুলো বিস্তারিত দেখুন",
                    "সবগুলো সংক্ষেপ করুন",
                    "ফন্ট সাইজ: ছোট (১২ sp)",
                    "ফন্ট সাইজ: সাধারণ (১৪ sp)",
                    "ফন্ট সাইজ: প্রমিত (১৬ sp)",
                    "ফন্ট সাইজ: বড় (১৮ sp)",
                    "ফন্ট সাইজ: বিশাল (২০ sp)",
                    "সম্পূর্ণ পাতা কপি করুন",
                    "সম্পূর্ণ পাতা শেয়ার করুন"
            };
        } else {
            options = new String[]{
                    "Expand All Topics",
                    "Collapse All Topics",
                    "Font Size: Small (12 sp)",
                    "Font Size: Normal (14 sp)",
                    "Font Size: Medium (16 sp)",
                    "Font Size: Large (18 sp)",
                    "Font Size: Extra Large (20 sp)",
                    "Copy All Content",
                    "Share All Content"
            };
        }
        float[] sizes = {12.0f, 14.0f, 16.0f, 18.0f, 20.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (MinistryHajjGuideItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (MinistryHajjGuideItem it : items) it.setExpanded(false);
                        adapter.notifyDataSetChanged();
                    } else if (which >= 2 && which <= 6) {
                        float chosen = sizes[which - 2];
                        adapter.setFontSize(chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 7) {
                        String title = isBn ? "হজ ও উমরাহ সহায়িকা (ধর্ম মন্ত্রনালয়)" : "Hajj & Umrah Guide (Ministry)";
                        copyToClipboard(activity, title, getFullContentText(items, isBn));
                    } else if (which == 8) {
                        String title = isBn ? "হজ ও উমরাহ সহায়িকা (ধর্ম মন্ত্রনালয়)" : "Hajj & Umrah Guide (Ministry)";
                        shareContent(activity, title, getFullContentText(items, isBn));
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            android.widget.Toast.makeText(context, isBn ? "মন্ত্রণালয় সহায়িকা কপি করা হয়েছে" : "Ministry guide copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    private static String getFullContentText(List<MinistryHajjGuideItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== হজ ও উমরাহ সহায়িকা (ধর্ম মন্ত্রনালয়) ===\n\n" : "=== Hajj & Umrah Guide (Ministry) ===\n\n");
        for (MinistryHajjGuideItem it : items) {
            sb.append("■ ").append(it.getTitle()).append("\n\n");
            sb.append(it.getFullContent()).append("\n\n");
            sb.append("------------------------------------\n\n");
        }
        return sb.toString();
    }

    public static class MinistryHajjAdapter extends RecyclerView.Adapter<MinistryHajjAdapter.ViewHolder> {
        private final List<MinistryHajjGuideItem> items;
        private float fontSize;
        private final boolean isBn;

        public MinistryHajjAdapter(List<MinistryHajjGuideItem> items, float fontSize, boolean isBn) {
            this.items = items;
            this.fontSize = fontSize;
            this.isBn = isBn;
        }

        public void setFontSize(float size) {
            this.fontSize = size;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMinistryHajjCardBinding binding = ItemMinistryHajjCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MinistryHajjGuideItem item = items.get(position);
            ItemMinistryHajjCardBinding b = holder.binding;

            b.tvMinistryItemTitle.setText(item.getTitle());
            b.tvMinistryItemPreview.setText(item.getPreview());
            b.tvMinistryItemFullContent.setText(item.getFullContent());

            b.tvMinistryItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            b.tvMinistryItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            if (item.isExpanded()) {
                b.tvMinistryItemPreview.setVisibility(View.GONE);
                b.tvMinistryItemFullContent.setVisibility(View.VISIBLE);
                b.tvMinistryToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
                b.ivMinistryToggleChevron.setRotation(180f);
            } else {
                b.tvMinistryItemPreview.setVisibility(View.VISIBLE);
                b.tvMinistryItemFullContent.setVisibility(View.GONE);
                b.tvMinistryToggleText.setText(isBn ? "বিস্তারিত" : "Read More");
                b.ivMinistryToggleChevron.setRotation(0f);
            }

            View.OnClickListener toggleClick = v -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(holder.getBindingAdapterPosition());
            };

            b.layoutMinistryToggleExpand.setOnClickListener(toggleClick);
            b.cardMinistryItem.setOnClickListener(toggleClick);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemMinistryHajjCardBinding binding;

            public ViewHolder(@NonNull ItemMinistryHajjCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
