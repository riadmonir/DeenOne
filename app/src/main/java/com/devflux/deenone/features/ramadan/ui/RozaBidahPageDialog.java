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
import com.devflux.deenone.features.ramadan.data.RozaBidahRepository;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * Production-ready Page Dialog for: রমাযানের কিছু বিদআত (Innovations in Ramadan).
 * Features:
 * - 60 FPS ultra-smooth scrolling, instant 0ms memory cache
 * - Reading settings: font scaling (13sp-19sp), expand/collapse all, copy all, share all
 * - RULE 7: STRICT ZERO touch animation on CardViews, spring touch animation ONLY on buttons
 */
public class RozaBidahPageDialog {

    private static final String PREFS_NAME = "roza_bidah_prefs";
    private static final String KEY_FONT_SIZE = "roza_bidah_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        String displayTitle = isBn ? "রমাযানের কিছু বিদআত" : "Innovations in Ramadan";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        List<HajjHistoryCardItem> items = RozaBidahRepository.getAllCards(activity, updatedItems -> {
            if (!activity.isFinishing() && !activity.isDestroyed() && binding.rvHajjHistoryCards.getAdapter() instanceof BidahAdapter) {
                ((BidahAdapter) binding.rvHajjHistoryCards.getAdapter()).updateData(updatedItems);
            }
        });

        // Default to collapsed (সংক্ষিপ্ত) matching Roza Dua and user requirement
        BidahAdapter adapter = new BidahAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> showSettingsDialog(activity, items, adapter, isBn, prefs));

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<HajjHistoryCardItem> items,
                                           BidahAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Cards",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Cards",
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "বিদআতসমূহ শেয়ার করুন" : "Share Innovations List",
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

    private static void showFontSizeDialog(Activity activity, BidahAdapter adapter,
                                           boolean isBn, SharedPreferences prefs) {
        String[] sizes = isBn ?
                new String[]{"ছোট (13sp)", "সাধারণ (14.5sp)", "মাঝারি (16sp)", "বড় (17.5sp)", "অনেক বড় (19sp)"} :
                new String[]{"Small (13sp)", "Normal (14.5sp)", "Medium (16sp)", "Large (17.5sp)", "Extra Large (19sp)"};

        float[] sizeValues = {13.0f, 14.5f, 16.0f, 17.5f, 19.0f};
        float current = adapter.getFontSize();
        int selectedIndex = 1;
        for (int i = 0; i < sizeValues.length; i++) {
            if (Math.abs(sizeValues[i] - current) < 0.2f) {
                selectedIndex = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ পরিবর্তন করুন" : "Adjust Font Size")
                .setSingleChoiceItems(sizes, selectedIndex, (d, which) -> {
                    float newSize = sizeValues[which];
                    prefs.edit().putFloat(KEY_FONT_SIZE, newSize).apply();
                    adapter.setFontSize(newSize);
                    d.dismiss();
                })
                .show();
    }

    private static void copyAll(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "রমাযানের কিছু বিদআত\n\n" : "Innovations in Ramadan\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append(it.getTitle(isBn)).append("\n\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Ramadan Innovations", sb.toString().trim()));
            Toast.makeText(activity, isBn ? "সম্পূর্ণ বিষয়বস্তু ক্লিপবোর্ডে কপি করা হয়েছে" : "Full content copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "রমাযানের কিছু বিদআত\n\n" : "Innovations in Ramadan\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append(it.getTitle(isBn)).append("\n\n");
            sb.append(it.getFullContent(isBn)).append("\n\n");
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "রমাযানের কিছু বিদআত" : "Innovations in Ramadan");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share Via"));
    }

    private static class BidahAdapter extends RecyclerView.Adapter<BidahAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public BidahAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
            this.items = items;
            this.fontSize = fontSize;
            this.isBn = isBn;
        }

        public void updateData(List<HajjHistoryCardItem> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        public float getFontSize() {
            return fontSize;
        }

        public void setFontSize(float fontSize) {
            this.fontSize = fontSize;
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
