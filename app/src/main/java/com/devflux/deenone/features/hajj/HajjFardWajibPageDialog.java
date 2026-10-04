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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemHajjHistoryCardBinding;
import com.devflux.deenone.databinding.PageHajjHistoryBinding;
import com.devflux.deenone.features.hajj.data.HajjFardWajibContentRepository;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HajjFardWajibPageDialog {

    private static final String PREFS_NAME = "hajj_fard_wajib_prefs";
    private static final String KEY_FONT_SIZE = "hajj_fard_wajib_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (adaptive to language: "ফরজ ও ওয়াজিব" / "Fard & Wajib")
        String displayTitle = isBn ? "ফরজ ও ওয়াজিব" : "Fard & Wajib";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Physics
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load 11 Fard & Wajib cards from Repository
        List<HajjHistoryCardItem> items = HajjFardWajibContentRepository.getFardWajibCards();

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        FardWajibExpandableAdapter adapter = new FardWajibExpandableAdapter(items, savedSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings Gear Action Button
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, displayTitle, prefs, isBn);
        });

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, FardWajibExpandableAdapter adapter, List<HajjHistoryCardItem> items, String pageTitle, SharedPreferences prefs, boolean isBn) {
        String[] options;
        if (isBn) {
            options = new String[]{
                    "সবগুলো বিস্তারিত দেখুন",
                    "সবগুলো সংক্ষেপ করুন",
                    "ফন্ট সাইজ: ছোট (১৩ sp)",
                    "ফন্ট সাইজ: সাধারণ (১৫ sp)",
                    "ফন্ট সাইজ: প্রমিত (১৭ sp)",
                    "ফন্ট সাইজ: বড় (১৯ sp)",
                    "সমস্ত বিধান কপি করুন",
                    "সমস্ত বিধান শেয়ার করুন"
            };
        } else {
            options = new String[]{
                    "Expand All Rules",
                    "Collapse All Rules",
                    "Font Size: Small (13 sp)",
                    "Font Size: Normal (15 sp)",
                    "Font Size: Medium (17 sp)",
                    "Font Size: Large (19 sp)",
                    "Copy All Rules",
                    "Share All Rules"
            };
        }
        float[] sizes = {13.0f, 15.0f, 17.0f, 19.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফরজ ও ওয়াজিব সেটিংস" : "Fard & Wajib Settings")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (HajjHistoryCardItem it : items) it.setExpanded(true);
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (HajjHistoryCardItem it : items) it.setExpanded(false);
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
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            boolean isBn = LocaleManager.isBengali(context);
            Toast.makeText(context, isBn ? "হজের বিধান কপি করা হয়েছে" : "Hajj rulings copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    private static String getFullText(String pageTitle, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(pageTitle).append(" ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("📖 ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            sb.append("------------------------------------\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");
        return sb.toString();
    }

    public static class FardWajibExpandableAdapter extends RecyclerView.Adapter<FardWajibExpandableAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public FardWajibExpandableAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
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
                b.ivHistoryToggleChevron.animate().rotation(180f).setDuration(200).start();
            } else {
                b.tvHistoryItemPreview.setVisibility(View.VISIBLE);
                b.tvHistoryItemFullContent.setVisibility(View.GONE);
                b.tvHistoryToggleText.setText(isBn ? "বিস্তারিত" : "Details");
                b.ivHistoryToggleChevron.animate().rotation(0f).setDuration(200).start();
            }

            View.OnClickListener toggleListener = v -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(holder.getBindingAdapterPosition());
            };

            b.layoutHistoryToggleExpand.setOnClickListener(toggleListener);
            b.cardHistoryItem.setOnClickListener(toggleListener);

            b.cardHistoryItem.setOnLongClickListener(v -> {
                String plain = HtmlCompat.fromHtml(item.getFullContent(isBn), HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim();
                copyToClipboard(v.getContext(), item.getTitle(isBn), item.getTitle(isBn) + "\n\n" + plain);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjHistoryCardBinding binding;

            ViewHolder(ItemHajjHistoryCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                TouchAnimationUtil.attachTouchSpring(binding.layoutHistoryToggleExpand);
            }
        }
    }
}
