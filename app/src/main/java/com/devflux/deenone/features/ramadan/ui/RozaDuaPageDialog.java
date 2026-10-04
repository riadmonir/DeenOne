package com.devflux.deenone.features.ramadan.ui;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemHajjHistoryCardBinding;
import com.devflux.deenone.databinding.PageHajjHistoryBinding;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.features.ramadan.data.RozaDuaRepository;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: রোজার দোয়া (Fasting Duas).
 * 100% accurately matching the Hajj History & Tashahhud card layout and architecture.
 */
public class RozaDuaPageDialog {

    private static final String PREFS_NAME = "roza_dua_prefs";
    private static final String KEY_FONT_SIZE = "roza_dua_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "রোজার দোয়া" / "Fasting Duas")
        String displayTitle = isBn ? "রোজার দোয়া" : "Fasting Duas";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load Dua Cards from Repository
        List<HajjHistoryCardItem> items = RozaDuaRepository.getAllDuas();

        // SharedPreferences for Font Size
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        RozaDuaCardAdapter adapter = new RozaDuaCardAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings / Options Menu
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, items, adapter, isBn, prefs);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<HajjHistoryCardItem> items,
                                           RozaDuaCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Duas",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Duas",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "দোয়ার বিবরণ শেয়ার করুন" : "Share All Duas",
                isBn ? "ক্লিপবোর্ডে কপি করুন" : "Copy All Duas"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (HajjHistoryCardItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (HajjHistoryCardItem it : items) it.setExpanded(false);
                        adapter.notifyDataSetChanged();
                    } else if (which == 2) {
                        showFontSizeDialog(activity, adapter, isBn, prefs);
                    } else if (which == 3) {
                        shareAll(activity, items, isBn);
                    } else if (which == 4) {
                        copyAll(activity, items, isBn);
                    }
                })
                .show();
    }

    private static void showFontSizeDialog(Activity activity, RozaDuaCardAdapter adapter,
                                           boolean isBn, SharedPreferences prefs) {
        String[] sizes = {
                isBn ? "ছোট (১৩ sp)" : "Small (13 sp)",
                isBn ? "স্বাভাবিক (১৫ sp)" : "Normal (15 sp)",
                isBn ? "বড় (১৭ sp)" : "Large (17 sp)",
                isBn ? "অনেক বড় (১৯ sp)" : "Extra Large (19 sp)"
        };
        float[] sizeValues = {13.0f, 15.0f, 17.0f, 19.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
                .setItems(sizes, (dialog, which) -> {
                    float selectedSize = sizeValues[which];
                    adapter.setFontSize(selectedSize);
                    prefs.edit().putFloat(KEY_FONT_SIZE, selectedSize).apply();
                })
                .show();
    }

    private static void copyAll(Context context, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== রোজার দোয়া ===\n\n" : "=== Fasting Duas ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("🕋 ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "রোজার দোয়া" : "Fasting Duas", sb.toString().trim()));
            Toast.makeText(context, isBn ? "সকল দোয়া ক্লিপবোর্ডে কপি করা হয়েছে" : "All duas copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "🤲 রোজার দোয়া — দ্বীন ওয়ান\n\n" : "🤲 Fasting Duas — DeenOne\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("🕋 ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "রোজার দোয়া" : "Fasting Duas");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "দোয়া শেয়ার করুন" : "Share Duas"));
    }

    public static class RozaDuaCardAdapter extends RecyclerView.Adapter<RozaDuaCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaDuaCardAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
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
            ItemHajjHistoryCardBinding binding = ItemHajjHistoryCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HajjHistoryCardItem item = items.get(position);
            ItemHajjHistoryCardBinding b = holder.binding;

            b.tvHistoryItemTitle.setText(item.getTitle(isBn));
            b.tvHistoryItemPreview.setText(item.getPreview(isBn));

            String fullText = item.getFullContent(isBn);
            if (fullText != null) {
                if (fullText.contains("<") && fullText.contains(">")) {
                    String htmlFormatted = fullText.replace("\r\n", "<br>").replace("\n", "<br>");
                    htmlFormatted = htmlFormatted.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
                    b.tvHistoryItemFullContent.setText(HtmlCompat.fromHtml(htmlFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    b.tvHistoryItemFullContent.setText(fullText);
                }
            } else {
                b.tvHistoryItemFullContent.setText("");
            }

            b.tvHistoryItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            b.tvHistoryItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            if (item.isExpanded()) {
                b.tvHistoryItemPreview.setVisibility(View.GONE);
                b.tvHistoryItemFullContent.setVisibility(View.VISIBLE);
                b.tvHistoryToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
                b.ivHistoryToggleChevron.setRotation(180f);
            } else {
                b.tvHistoryItemPreview.setVisibility(View.VISIBLE);
                b.tvHistoryItemFullContent.setVisibility(View.GONE);
                b.tvHistoryToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "View Details");
                b.ivHistoryToggleChevron.setRotation(0f);
            }

            View.OnClickListener toggleClick = v -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(holder.getBindingAdapterPosition());
            };

            b.layoutHistoryToggleExpand.setOnClickListener(toggleClick);
            TouchAnimationUtil.attachTouchSpring(b.layoutHistoryToggleExpand);

            b.cardHistoryItem.setOnClickListener(toggleClick);
            // Strictly following user directive: No touch spring animation on the card view itself!
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjHistoryCardBinding binding;

            public ViewHolder(@NonNull ItemHajjHistoryCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
