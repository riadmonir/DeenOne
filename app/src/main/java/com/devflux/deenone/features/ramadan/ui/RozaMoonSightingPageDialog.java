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
import com.devflux.deenone.features.ramadan.data.RozaMoonSightingRepository;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: চাঁদ দেখা সংক্রান্ত বিষয়াবলী (Matters Regarding Moon Sighting).
 * 100% matches screenshot top bar ("← চাঁদ দেখা সংক্রান্ত বিষয়াবলী"), cyan badge cards, and verbatim text.
 * Follows Rule 16 standard and Rule 7 (Zero touch animation on CardViews).
 */
public class RozaMoonSightingPageDialog {

    private static final String PREFS_NAME = "roza_moon_sighting_prefs";
    private static final String KEY_FONT_SIZE = "roza_moon_sighting_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim from Screenshot: "চাঁদ দেখা সংক্রান্ত বিষয়াবলী" / "Matters Regarding Moon Sighting")
        String displayTitle = isBn ? "চাঁদ দেখা সংক্রান্ত বিষয়াবলী" : "Matters Regarding Moon Sighting";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load Moon Sighting Cards from Repository
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        List<HajjHistoryCardItem> items = RozaMoonSightingRepository.getAllCards(activity, updatedItems -> {
            if (!activity.isFinishing() && !activity.isDestroyed() && binding.rvHajjHistoryCards.getAdapter() instanceof RozaMoonSightingCardAdapter) {
                ((RozaMoonSightingCardAdapter) binding.rvHajjHistoryCards.getAdapter()).updateData(updatedItems);
            }
        });

        RozaMoonSightingCardAdapter adapter = new RozaMoonSightingCardAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Reading Settings / Options Menu
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, items, adapter, isBn, prefs);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<HajjHistoryCardItem> items,
                                           RozaMoonSightingCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Cards",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Cards",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "চাঁদ দেখার বিধান শেয়ার করুন" : "Share All Rulings",
                isBn ? "ক্লিপবোর্ডে কপি করুন" : "Copy All to Clipboard"
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

    private static void showFontSizeDialog(Activity activity, RozaMoonSightingCardAdapter adapter,
                                           boolean isBn, SharedPreferences prefs) {
        String[] sizes = {
                isBn ? "ছোট (১৩ sp)" : "Small (13 sp)",
                isBn ? "সাধারণ (১৫ sp)" : "Medium (15 sp)",
                isBn ? "বড় (১৭ sp)" : "Large (17 sp)",
                isBn ? "অতিরিক্ত বড় (১৯ sp)" : "Extra Large (19 sp)"
        };
        float[] values = {13.0f, 15.0f, 17.0f, 19.0f};

        float current = prefs.getFloat(KEY_FONT_SIZE, 14.5f);
        int checkedItem = 1;
        if (current <= 13.5f) checkedItem = 0;
        else if (current <= 15.5f) checkedItem = 1;
        else if (current <= 17.5f) checkedItem = 2;
        else checkedItem = 3;

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
                .setSingleChoiceItems(sizes, checkedItem, (dialog, which) -> {
                    float selectedSize = values[which];
                    prefs.edit().putFloat(KEY_FONT_SIZE, selectedSize).apply();
                    adapter.setFontSize(selectedSize);
                    dialog.dismiss();
                })
                .show();
    }

    private static void copyAll(Context context, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== চাঁদ দেখা সংক্রান্ত বিষয়াবলী ===\n\n" : "=== Matters Regarding Moon Sighting ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Moon Sighting Details", sb.toString().trim()));
            Toast.makeText(context, isBn ? "সম্পূর্ণ বিবরণ কপি হয়েছে" : "Moon sighting details copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== চাঁদ দেখা সংক্রান্ত বিষয়াবলী ===\n\n" : "=== Matters Regarding Moon Sighting ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "চাঁদ দেখা সংক্রান্ত বিষয়াবলী" : "Matters Regarding Moon Sighting");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "চাঁদ দেখার বিবরণ শেয়ার করুন" : "Share Moon Sighting Details"));
    }

    public static class RozaMoonSightingCardAdapter extends RecyclerView.Adapter<RozaMoonSightingCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaMoonSightingCardAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
            this.items = items;
            this.fontSize = fontSize;
            this.isBn = isBn;
        }

        public void setFontSize(float size) {
            this.fontSize = size;
            notifyDataSetChanged();
        }

        public void updateData(List<HajjHistoryCardItem> newItems) {
            this.items.clear();
            if (newItems != null) {
                this.items.addAll(newItems);
            }
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
            // Strictly following user directive (Rule 7): No touch spring animation on the card view itself!
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
