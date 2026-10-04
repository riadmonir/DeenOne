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
import com.devflux.deenone.features.ramadan.model.RozaFazayelMasayelItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready Page Dialog for detailed viewing of Fazayel & Masayel topics
 * (e.g. রমাদ্বান মানে কি?, সিয়ামের প্রকারভেদ, সুন্নাত ও নফল রোজা, etc.).
 * Follows Rule 16 standards:
 * - 60 FPS ultra-smooth scrolling, lag-free performance
 * - Expandable cards with 16dp radius
 * - Settings menu (Font size, Expand/Collapse, Copy, Share)
 * - Rule 7: STRICT ZERO touch animation on CardViews; touch spring ONLY on buttons
 */
public class RozaFazayelTopicDetailDialog {

    private static final String PREFS_NAME = "roza_topic_detail_prefs";
    private static final String KEY_FONT_SIZE = "roza_topic_detail_font_size";

    public static void show(Activity activity, RozaFazayelMasayelItem item) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed() || item == null) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title
        String displayTitle = item.getTitle(isBn);
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Touch Animation (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load and parse cards from the item
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        List<HajjHistoryCardItem> items = parseCardsFromItem(item);

        RozaTopicCardAdapter adapter = new RozaTopicCardAdapter(items, currentFontSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Reading Settings / Options Menu
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, displayTitle, items, adapter, isBn, prefs);
        });

        dialog.show();
    }

    private static List<HajjHistoryCardItem> parseCardsFromItem(RozaFazayelMasayelItem item) {
        List<HajjHistoryCardItem> cards = new ArrayList<>();
        String rawBn = item.getDetails(true);
        String rawEn = item.getDetails(false);
        String refBn = item.getReference(true);
        String refEn = item.getReference(false);

        if (rawBn == null || rawBn.trim().isEmpty()) {
            HajjHistoryCardItem card = new HajjHistoryCardItem(
                    1,
                    item.getTitle(true),
                    item.getTitle(false),
                    item.getPreview(true),
                    item.getPreview(false),
                    item.getDetails(true),
                    item.getDetails(false)
            );
            card.setExpanded(false); // Collapsed by default (সংক্ষিপ্ত)
            cards.add(card);
            return cards;
        }

        // Split by numbered headings e.g. "১. " or "1. "
        String[] partsBn = rawBn.split("(?m)(?=^[০-৯1-9]+[\\.\\।\\)])");
        String[] partsEn = (rawEn != null && !rawEn.trim().isEmpty())
                ? rawEn.split("(?m)(?=^[0-9]+[\\.\\)])")
                : new String[0];

        List<String> validPartsBn = new ArrayList<>();
        for (String p : partsBn) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty() && trimmed.matches("^[০-৯1-9]+[\\.\\।\\)][\\s\\S]*")) {
                validPartsBn.add(trimmed);
            }
        }

        List<String> validPartsEn = new ArrayList<>();
        for (String p : partsEn) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty() && trimmed.matches("^[0-9]+[\\.\\)][\\s\\S]*")) {
                validPartsEn.add(trimmed);
            }
        }

        if (!validPartsBn.isEmpty()) {
            for (int i = 0; i < validPartsBn.size(); i++) {
                String pBn = validPartsBn.get(i);
                String pEn = (i < validPartsEn.size()) ? validPartsEn.get(i) : pBn;

                ParsedCardData bnData = extractTitleAndContent(pBn);
                ParsedCardData enData = extractTitleAndContent(pEn);

                String contentBn = bnData.content;
                String contentEn = enData.content;

                // If this is the last card and there's a reference, append reference
                if (i == validPartsBn.size() - 1) {
                    if (refBn != null && !refBn.trim().isEmpty()) {
                        contentBn = contentBn + "<br><br><font color='#00897B'><b>রেফারেন্স:</b> " + refBn + "</font>";
                    }
                    if (refEn != null && !refEn.trim().isEmpty()) {
                        contentEn = contentEn + "<br><br><font color='#00897B'><b>Reference:</b> " + refEn + "</font>";
                    }
                }

                String cleanBn = contentBn.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
                String cleanEn = contentEn.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();

                String previewBn = cleanBn.length() > 85 ? cleanBn.substring(0, 85) + "..." : cleanBn;
                String previewEn = cleanEn.length() > 85 ? cleanEn.substring(0, 85) + "..." : cleanEn;

                HajjHistoryCardItem card = new HajjHistoryCardItem(
                        i + 1, bnData.title, enData.title, previewBn, previewEn, contentBn, contentEn
                );
                card.setExpanded(false); // Collapsed by default (সংক্ষিপ্ত)
                cards.add(card);
            }
        } else {
            String fullBn = rawBn;
            String fullEn = rawEn != null ? rawEn : rawBn;
            if (refBn != null && !refBn.trim().isEmpty()) {
                fullBn = fullBn + "<br><br><font color='#00897B'><b>রেফারেন্স:</b> " + refBn + "</font>";
            }
            if (refEn != null && !refEn.trim().isEmpty()) {
                fullEn = fullEn + "<br><br><font color='#00897B'><b>Reference:</b> " + refEn + "</font>";
            }
            HajjHistoryCardItem card = new HajjHistoryCardItem(
                    1,
                    item.getTitle(true),
                    item.getTitle(false),
                    item.getPreview(true),
                    item.getPreview(false),
                    fullBn,
                    fullEn
            );
            card.setExpanded(false); // Collapsed by default (সংক্ষিপ্ত)
            cards.add(card);
        }

        return cards;
    }

    private static class ParsedCardData {
        final String title;
        final String content;
        ParsedCardData(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    private static ParsedCardData extractTitleAndContent(String text) {
        if (text == null) return new ParsedCardData("", "");
        String p = text.trim();
        int newlinePos = p.indexOf('\n');
        String title;
        String content;

        if (newlinePos != -1) {
            String firstLine = p.substring(0, newlinePos).trim();
            String rest = p.substring(newlinePos).trim();
            int colonPos = firstLine.indexOf(':');
            if (colonPos != -1 && colonPos < firstLine.length() - 1) {
                title = firstLine.substring(0, colonPos).trim();
                String afterColon = firstLine.substring(colonPos + 1).trim();
                content = afterColon.isEmpty() ? rest : (afterColon + "\n\n" + rest);
            } else {
                title = firstLine;
                content = rest;
            }
        } else {
            int colonPos = p.indexOf(':');
            if (colonPos != -1) {
                title = p.substring(0, colonPos).trim();
                content = p.substring(colonPos + 1).trim();
            } else {
                title = p;
                content = p;
            }
        }

        if (title.endsWith(":")) {
            title = title.substring(0, title.length() - 1).trim();
        }

        return new ParsedCardData(title, content);
    }

    private static void showSettingsDialog(Activity activity, String pageTitle, List<HajjHistoryCardItem> items,
                                           RozaTopicCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All Cards",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All Cards",
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
                        shareAll(activity, pageTitle, items, isBn);
                    } else if (which == 4) {
                        copyAll(activity, pageTitle, items, isBn);
                    }
                })
                .show();
    }

    private static void showFontSizeDialog(Activity activity, RozaTopicCardAdapter adapter,
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

    private static void copyAll(Context context, String pageTitle, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(pageTitle).append(" ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(pageTitle, sb.toString().trim()));
            Toast.makeText(context, isBn ? "বিবরণ ক্লিপবোর্ডে কপি হয়েছে" : "Details copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAll(Context context, String pageTitle, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(pageTitle).append(" ===\n\n");
        for (HajjHistoryCardItem it : items) {
            sb.append("◆ ").append(it.getTitle(isBn)).append("\n");
            String raw = it.getFullContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
        }
        sb.append("DeenOne - দ্বীন ওয়ান ইসলামিক অ্যাপ");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, pageTitle);
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "বিবরণ শেয়ার করুন" : "Share Details"));
    }

    public static class RozaTopicCardAdapter extends RecyclerView.Adapter<RozaTopicCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public RozaTopicCardAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
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
            // STRICT Rule 7: ZERO touch animation on the card view itself!
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
