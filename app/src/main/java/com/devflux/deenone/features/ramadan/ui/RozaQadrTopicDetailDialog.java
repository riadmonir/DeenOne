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
import com.devflux.deenone.features.ramadan.model.RozaQadrTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Detailed Page Dialog for individual Laylatul Qadr topics.
 * 100% matches screenshot standards with verbatim text and uniform card architecture:
 * - Clean card heading without colored background pill
 * - Default collapsed state with 2-line preview and "বিস্তারিত ⌵" toggle
 * - Expandable full content showing complete verbatim Islamic text
 * - Reading settings: Font scaling (13sp-19sp), copy, share, expand/collapse
 * - STRICT RULE 7: ZERO touch animation on CardViews, touch animation ONLY on buttons.
 */
public class RozaQadrTopicDetailDialog {

    private static final String PREFS_NAME = "roza_qadr_detail_prefs";
    private static final String KEY_FONT_SIZE = "roza_qadr_detail_font_size";

    public static void show(Activity activity, RozaQadrTopicItem item) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed() || item == null) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim topic title)
        String displayTitle = item.getTitle(isBn);
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Touch Animation (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Setup Single card list for the topic
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        List<HajjHistoryCardItem> items = new ArrayList<>();
        String fullBn = item.getDetails(true);
        if (item.getReferenceBn() != null && !item.getReferenceBn().trim().isEmpty() && !fullBn.contains(item.getReferenceBn())) {
            fullBn = fullBn + "\n\n[" + item.getReferenceBn() + "]";
        }

        String fullEn = item.getDetails(false);
        if (item.getReferenceEn() != null && !item.getReferenceEn().trim().isEmpty() && !fullEn.contains(item.getReferenceEn())) {
            fullEn = fullEn + "\n\n[" + item.getReferenceEn() + "]";
        }

        HajjHistoryCardItem cardItem = new HajjHistoryCardItem(
                item.getId(),
                item.getCardTitle(true),
                item.getCardTitle(false),
                item.getPreview(true),
                item.getPreview(false),
                fullBn,
                fullEn
        );
        cardItem.setExpanded(false); // Collapsed by default
        items.add(cardItem);

        RozaQadrCardAdapter adapter = new RozaQadrCardAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Reading Settings / Options Menu
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, displayTitle, items, adapter, isBn, prefs);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, String title, List<HajjHistoryCardItem> items,
                                           RozaQadrCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "বিস্তারিত দেখুন" : "Expand All",
                isBn ? "সংক্ষেপ করুন" : "Collapse All",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "বিবরণ শেয়ার করুন" : "Share Details",
                isBn ? "ক্লিপবোর্ডে কপি করুন" : "Copy to Clipboard"
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
                        shareAll(activity, title, items, isBn);
                    } else if (which == 4) {
                        copyAll(activity, title, items, isBn);
                    }
                })
                .show();
    }

    private static void showFontSizeDialog(Activity activity, RozaQadrCardAdapter adapter,
                                           boolean isBn, SharedPreferences prefs) {
        String[] sizes = {
                isBn ? "ছোট (১৩ sp)" : "Small (13 sp)",
                isBn ? "ডিফল্ট (১৫ sp)" : "Default (15 sp)",
                isBn ? "বড় (১৭ sp)" : "Large (17 sp)",
                isBn ? "খুব বড় (১৯ sp)" : "Extra Large (19 sp)"
        };
        float[] values = {13f, 15f, 17f, 19f};

        float current = adapter.getFontSize();
        int selectedIndex = 1;
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i] - current) < 0.5f) {
                selectedIndex = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
                .setSingleChoiceItems(sizes, selectedIndex, (dialog, which) -> {
                    float newSize = values[which];
                    adapter.setFontSize(newSize);
                    prefs.edit().putFloat(KEY_FONT_SIZE, newSize).apply();
                    dialog.dismiss();
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void copyAll(Context context, String title, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Laylatul Qadr Details", sb.toString().trim()));
            Toast.makeText(context, isBn ? "সম্পূর্ণ বিবরণ কপি হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, String title, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "বিবরণ শেয়ার করুন" : "Share Details"));
    }

    public static class RozaQadrCardAdapter extends RecyclerView.Adapter<RozaQadrCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaQadrCardAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
            this.items = items;
            this.fontSize = fontSize;
            this.isBn = isBn;
        }

        public void setFontSize(float size) {
            this.fontSize = size;
            notifyDataSetChanged();
        }

        public float getFontSize() {
            return fontSize;
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
                b.tvHistoryToggleText.setText(isBn ? "সংক্ষেপ" : "Collapse");
                b.ivHistoryToggleChevron.setRotation(180f);
            } else {
                b.tvHistoryItemPreview.setVisibility(View.VISIBLE);
                b.tvHistoryItemFullContent.setVisibility(View.GONE);
                b.tvHistoryToggleText.setText(isBn ? "বিস্তারিত" : "View Details");
                b.ivHistoryToggleChevron.setRotation(0f);
            }

            View.OnClickListener toggleClick = v -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(holder.getBindingAdapterPosition());
            };

            b.layoutHistoryToggleExpand.setOnClickListener(toggleClick);
            TouchAnimationUtil.attachTouchSpring(b.layoutHistoryToggleExpand);

            b.cardHistoryItem.setOnClickListener(toggleClick);
            // STRICT Rule 7: ZERO touch animation on CardView!
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjHistoryCardBinding binding;

            public ViewHolder(ItemHajjHistoryCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
