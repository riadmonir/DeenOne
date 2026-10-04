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
import com.devflux.deenone.features.ramadan.data.RozaInvalidatorsRepository;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: যাতে রোযা নষ্ট ও বাতিল হয় (Things That Invalidate the Fast).
 * 100% accurately matching the Hajj History, Jumatul Wida, Roza Dua & Roza Arkan card layout and architecture (Rule 16).
 */
public class RozaInvalidatorsPageDialog {

    private static final String PREFS_NAME = "roza_invalidators_prefs";
    private static final String KEY_FONT_SIZE = "roza_invalidators_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "যাতে রোযা নষ্ট ও বাতিল হয়" / "Things That Invalidate the Fast")
        String displayTitle = isBn ? "যাতে রোযা নষ্ট ও বাতিল হয়" : "Things That Invalidate the Fast";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load Cards from Repository (Instant cached, with async REST API refresh)
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        List<HajjHistoryCardItem> items = RozaInvalidatorsRepository.getAllCards(activity, updatedItems -> {
            if (!activity.isFinishing() && !activity.isDestroyed() && binding.rvHajjHistoryCards.getAdapter() instanceof RozaInvalidatorsCardAdapter) {
                ((RozaInvalidatorsCardAdapter) binding.rvHajjHistoryCards.getAdapter()).updateData(updatedItems);
            }
        });

        RozaInvalidatorsCardAdapter adapter = new RozaInvalidatorsCardAdapter(items, currentFontSize, isBn);
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
                                           RozaInvalidatorsCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Cards",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Cards",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "রোজা ভঙ্গের কারণসমূহ শেয়ার করুন" : "Share All Nullifiers",
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

    private static void showFontSizeDialog(Activity activity, RozaInvalidatorsCardAdapter adapter,
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
        sb.append(isBn ? "=== যাতে রোযা নষ্ট ও বাতিল হয় ===\n\n" : "=== Things That Invalidate the Fast ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Invalidators of Fast", sb.toString().trim()));
            Toast.makeText(context, isBn ? "সম্পূর্ণ বিবরণ কপি হয়েছে" : "All nullifiers copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== যাতে রোযা নষ্ট ও বাতিল হয় ===\n\n" : "=== Things That Invalidate the Fast ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "যাতে রোযা নষ্ট ও বাতিল হয়" : "Things That Invalidate the Fast");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "রোজা ভঙ্গের কারণসমূহ শেয়ার করুন" : "Share Fast Nullifiers"));
    }

    public static class RozaInvalidatorsCardAdapter extends RecyclerView.Adapter<RozaInvalidatorsCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaInvalidatorsCardAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
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
