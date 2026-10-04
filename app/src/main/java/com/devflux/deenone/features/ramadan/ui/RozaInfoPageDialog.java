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
import com.devflux.deenone.features.ramadan.data.RozaInfoRepository;
import com.devflux.deenone.features.ramadan.model.RozaInfoItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: রোজা নিয়ে তথ্য (Information on Fasting).
 * Built with the exact same architecture, card views, and expandable behavior as Hajj History.
 */
public class RozaInfoPageDialog {

    private static final String PREFS_NAME = "roza_info_prefs";
    private static final String KEY_FONT_SIZE = "roza_info_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title
        String displayTitle = isBn ? "রোজা নিয়ে তথ্য" : "Information on Fasting";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load Items from Repository
        List<RozaInfoItem> items = RozaInfoRepository.getRozaInfoList();

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        RozaInfoCardAdapter adapter = new RozaInfoCardAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings / Options Menu
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, items, adapter, isBn, prefs);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<RozaInfoItem> items,
                                           RozaInfoCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Topics",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Topics",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "বিবরণ শেয়ার করুন" : "Share All Topics",
                isBn ? "ক্লিপবোর্ডে কপি করুন" : "Copy All Topics"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (RozaInfoItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (RozaInfoItem it : items) it.setExpanded(false);
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

    private static void showFontSizeDialog(Activity activity, RozaInfoCardAdapter adapter,
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

    private static void copyAll(Context context, List<RozaInfoItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== রোজা নিয়ে তথ্য ===\n\n" : "=== Information on Fasting ===\n\n");
        for (RozaInfoItem it : items) {
            sb.append("📌 ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            String ref = it.getReference(isBn);
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append(isBn ? "রেফারেন্স: " : "Reference: ").append(ref.trim()).append("\n\n");
            }
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "রোজা নিয়ে তথ্য" : "Information on Fasting", sb.toString().trim()));
            Toast.makeText(context, isBn ? "সকল তথ্য ক্লিপবোর্ডে কপি করা হয়েছে" : "All content copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, List<RozaInfoItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "📖 রোজা নিয়ে তথ্য — দ্বীন ওয়ান\n\n" : "📖 Information on Fasting — DeenOne\n\n");
        for (RozaInfoItem it : items) {
            sb.append("📌 ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            String ref = it.getReference(isBn);
            if (ref != null && !ref.trim().isEmpty()) {
                sb.append(isBn ? "রেফারেন্স: " : "Reference: ").append(ref.trim()).append("\n\n");
            }
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "রোজা নিয়ে তথ্য" : "Information on Fasting");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "তথ্য শেয়ার করুন" : "Share Information"));
    }

    public static class RozaInfoCardAdapter extends RecyclerView.Adapter<RozaInfoCardAdapter.ViewHolder> {
        private final List<RozaInfoItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaInfoCardAdapter(List<RozaInfoItem> items, float fontSize, boolean isBn) {
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
            RozaInfoItem item = items.get(position);
            ItemHajjHistoryCardBinding b = holder.binding;

            b.tvHistoryItemTitle.setText(item.getTitle(isBn));
            b.tvHistoryItemPreview.setText(item.getPreviewText(isBn));

            String fullText = item.getFullContent(isBn);
            String ref = item.getReference(isBn);
            if (ref != null && !ref.trim().isEmpty()) {
                fullText = fullText + "\n\n<b>" + (isBn ? "রেফারেন্স: " : "Reference: ") + "</b>" + ref.trim();
            }

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
            // No touch spring on card view per user directive
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
