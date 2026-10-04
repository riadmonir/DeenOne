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

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemHajjHistoryCardBinding;
import com.devflux.deenone.databinding.PageHajjHistoryBinding;
import com.devflux.deenone.features.hajj.data.HajjMiqatContentRepository;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HajjMiqatPageDialog {

    private static final String PREFS_NAME = "hajj_miqat_prefs";
    private static final String KEY_FONT_SIZE = "hajj_miqat_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "মীকাত" in Bengali, "Miqat" in English)
        String displayTitle = isBn ? "মীকাত" : "Miqat";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Physics
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Instant Offline-First: 6 Miqat cards loaded from Repository (Verbatim from Screenshot)
        List<HajjHistoryCardItem> items = new ArrayList<>(HajjMiqatContentRepository.getMiqatCards());

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        MiqatExpandableAdapter adapter = new MiqatExpandableAdapter(items, savedSize, isBn);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings Gear Action Button with Spring Physics
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, displayTitle, prefs, isBn);
        });

        // Asynchronous Background Sync with PHP REST API (Topic 16: Miqat)
        syncOnlineArticlesInBackground(activity, adapter, items, isBn);

        dialog.show();
    }

    private static void syncOnlineArticlesInBackground(Activity activity, MiqatExpandableAdapter adapter, List<HajjHistoryCardItem> items, boolean isBn) {
        new Thread(() -> {
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(activity, "get_hajj_articles.php?topic_id=16");
                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                String apiKey = BackendConfigManager.getPhpApiKey(activity);
                if (apiKey != null && !apiKey.isEmpty()) {
                    conn.setRequestProperty("X-API-KEY", apiKey);
                }

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject response = new JSONObject(sb.toString());
                    if (response.optBoolean("success", false) && response.has("articles")) {
                        JSONArray articlesArr = response.getJSONArray("articles");
                        if (articlesArr.length() > 0) {
                            List<HajjHistoryCardItem> onlineItems = new ArrayList<>();
                            for (int i = 0; i < articlesArr.length(); i++) {
                                JSONObject obj = articlesArr.getJSONObject(i);
                                int id = obj.optInt("id", i + 1);
                                String titleBn = obj.optString("title_bn", "");
                                String titleEn = obj.optString("title_en", "");
                                String contentBn = obj.optString("content_bn", "");
                                String contentEn = obj.optString("content_en", "");

                                // Auto-extract preview matching screenshot style
                                String previewBn = contentBn.length() > 60 ? contentBn.substring(0, 60).replaceAll("\n", " ").trim() + "..." : contentBn;
                                String previewEn = contentEn.length() > 60 ? contentEn.substring(0, 60).replaceAll("\n", " ").trim() + "..." : contentEn;

                                onlineItems.add(new HajjHistoryCardItem(
                                    id, titleBn, titleEn, previewBn, previewEn, contentBn, contentEn
                                ));
                            }

                            activity.runOnUiThread(() -> {
                                items.clear();
                                items.addAll(onlineItems);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception ignored) {
                // Offline fallback remains active without disruption
            }
        }).start();
    }

    private static void showSettingsDialog(Activity activity, MiqatExpandableAdapter adapter, List<HajjHistoryCardItem> items, String pageTitle, SharedPreferences prefs, boolean isBn) {
        String[] options;
        if (isBn) {
            options = new String[]{
                    "সবগুলো বিস্তারিত দেখুন",
                    "সবগুলো সংক্ষেপ করুন",
                    "ফন্ট সাইজ: ছোট (১৩ sp)",
                    "ফন্ট সাইজ: সাধারণ (১৫ sp)",
                    "ফন্ট সাইজ: প্রমিত (১৭ sp)",
                    "ফন্ট সাইজ: বড় (১৯ sp)",
                    "সমস্ত মীকাতের তথ্য কপি করুন",
                    "সমস্ত মীকাতের তথ্য শেয়ার করুন"
            };
        } else {
            options = new String[]{
                    "Expand All Details",
                    "Collapse All Details",
                    "Font Size: Small (13 sp)",
                    "Font Size: Normal (15 sp)",
                    "Font Size: Medium (17 sp)",
                    "Font Size: Large (19 sp)",
                    "Copy All Miqat Info",
                    "Share All Miqat Info"
            };
        }
        float[] sizes = {13.0f, 15.0f, 17.0f, 19.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "মীকাত সেটিংস ও অপশন" : "Miqat Settings & Options")
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
                        copyToClipboard(activity, pageTitle, getFullMiqatText(pageTitle, items, isBn));
                    } else if (which == 7) {
                        shareContent(activity, pageTitle, getFullMiqatText(pageTitle, items, isBn));
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        boolean isBn = LocaleManager.isBengali(context);
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            android.widget.Toast.makeText(context, isBn ? "মীকাতের বিবরণ কপি করা হয়েছে" : "Miqat details copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    private static String getFullMiqatText(String pageTitle, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(pageTitle).append(" ===\n\n");
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
        return sb.toString();
    }

    public static class MiqatExpandableAdapter extends RecyclerView.Adapter<MiqatExpandableAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public MiqatExpandableAdapter(List<HajjHistoryCardItem> items, float fontSize, boolean isBn) {
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
                b.tvHistoryToggleText.setText(isBn ? "বিস্তারিত" : "Details");
                b.ivHistoryToggleChevron.setRotation(0f);
            }

            View.OnClickListener toggleClick = v -> {
                item.setExpanded(!item.isExpanded());
                notifyItemChanged(holder.getBindingAdapterPosition());
            };

            b.layoutHistoryToggleExpand.setOnClickListener(toggleClick);
            b.cardHistoryItem.setOnClickListener(toggleClick);
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
                TouchAnimationUtil.attachTouchSpring(binding.layoutHistoryToggleExpand);
            }
        }
    }
}
