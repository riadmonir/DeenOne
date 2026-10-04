package com.devflux.deenone.features.salahguide;

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
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.features.salahguide.data.MasailTarabiContentRepository;
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

/**
 * Production-ready Tarabi Masail Page Dialog.
 * 100% accurately matching design & content from provided screenshot.
 */
public class MasailTarabiPageDialog {

    private static final String PREFS_NAME = "masail_tarabi_prefs";
    private static final String KEY_FONT_SIZE = "masail_tarabi_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "তারাবীহ" / "Tarabi")
        String displayTitle = isBn ? "তারাবীহ" : "Tarabi";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Physics
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load 6 Tarabi cards from offline repository (Instant 0-delay load)
        List<HajjHistoryCardItem> items = new ArrayList<>(MasailTarabiContentRepository.getTarabiCards());

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        TarabiCardAdapter adapter = new TarabiCardAdapter(items, isBn, savedSize);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings Button (Font Size, Expand All, Share, Copy)
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, isBn, prefs);
        });

        dialog.show();

        // Background Asynchronous Online Sync with PHP API (topic_id=4)
        syncWithServer(activity, adapter, items);
    }

    private static void showSettingsDialog(Activity activity, TarabiCardAdapter adapter, List<HajjHistoryCardItem> items, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "ফন্ট সাইজ পরিবর্তন করুন" : "Change Font Size",
                isBn ? "সবগুলো কার্ড প্রসারিত করুন" : "Expand All Cards",
                isBn ? "সবগুলো কার্ড সংক্ষেপ করুন" : "Collapse All Cards",
                isBn ? "সব মাসআলা কপি করুন" : "Copy All Masail",
                isBn ? "তারাবীহর মাসআলা শেয়ার করুন" : "Share Tarabi Masail"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "তারাবীহর মাসাইল সেটিংস" : "Tarabi Masail Settings")
                .setItems(options, (d, which) -> {
                    switch (which) {
                        case 0:
                            showFontSizePicker(activity, adapter, isBn, prefs);
                            break;
                        case 1:
                            for (HajjHistoryCardItem it : items) it.setExpanded(true);
                            adapter.notifyDataSetChanged();
                            break;
                        case 2:
                            for (HajjHistoryCardItem it : items) it.setExpanded(false);
                            adapter.notifyDataSetChanged();
                            break;
                        case 3:
                            copyAllContent(activity, items, isBn);
                            break;
                        case 4:
                            shareAllContent(activity, items, isBn);
                            break;
                    }
                })
                .show();
    }

    private static void showFontSizePicker(Activity activity, TarabiCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] sizes = isBn ? new String[]{"ছোট (১৩sp)", "স্বাভাবিক (১৪.৫sp)", "বড় (১৬sp)", "অতিরিক্ত বড় (১৮sp)"}
                : new String[]{"Small (13sp)", "Normal (14.5sp)", "Large (16sp)", "Extra Large (18sp)"};
        float[] sizeValues = {13.0f, 14.5f, 16.0f, 18.0f};

        float current = prefs.getFloat(KEY_FONT_SIZE, 14.5f);
        int selectedIndex = 1;
        for (int i = 0; i < sizeValues.length; i++) {
            if (Math.abs(sizeValues[i] - current) < 0.2f) {
                selectedIndex = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
                .setSingleChoiceItems(sizes, selectedIndex, (d, which) -> {
                    float newSize = sizeValues[which];
                    prefs.edit().putFloat(KEY_FONT_SIZE, newSize).apply();
                    adapter.setFontSize(newSize);
                    d.dismiss();
                })
                .show();
    }

    private static void copyAllContent(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== তারাবীহ সম্পর্কিত জরুরি মাসাইল ===\n\n" : "=== Essential Tarabi Masail ===\n\n");
        for (HajjHistoryCardItem it : items) {
            String title = it.getTitle(isBn);
            String content = it.getFullContent(isBn);
            sb.append("• ").append(title).append("\n");
            sb.append(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()).append("\n\n");
        }
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Tarabi Masail", sb.toString().trim()));
            android.widget.Toast.makeText(activity, isBn ? "তারাবীহর সব মাসাইল ক্লিপবোর্ডে কপি করা হয়েছে" : "All Tarabi masail copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAllContent(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "🌙 তারাবীহ সম্পর্কিত জরুরি মাসাইল — দ্বীনওয়ান\n\n" : "🌙 Essential Tarabi Masail — DeenOne\n\n");
        for (HajjHistoryCardItem it : items) {
            String title = it.getTitle(isBn);
            String content = it.getFullContent(isBn);
            sb.append("📌 ").append(title).append("\n");
            sb.append(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()).append("\n\n");
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "তারাবীহর জরুরি মাসাইল" : "Essential Tarabi Masail");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
    }

    private static void syncWithServer(Activity activity, TarabiCardAdapter adapter, List<HajjHistoryCardItem> items) {
        new Thread(() -> {
            try {
                String apiUrl = BackendConfigManager.getPhpApiEndpoint(activity, "get_masail_articles.php?topic_id=4");
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    if (json.optBoolean("success", false)) {
                        JSONArray array = json.optJSONArray("articles");
                        if (array != null && array.length() > 0) {
                            List<HajjHistoryCardItem> serverItems = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                int id = obj.optInt("id", i + 1);
                                String titleBn = obj.optString("title_bn", "");
                                String titleEn = obj.optString("title_en", "");
                                String contentBn = obj.optString("content_bn", "");
                                String contentEn = obj.optString("content_en", "");

                                String plainBn = HtmlCompat.fromHtml(contentBn, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim();
                                String previewBn = plainBn.length() > 140 ? plainBn.substring(0, 140) + "..." : plainBn;

                                String plainEn = HtmlCompat.fromHtml(contentEn, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim();
                                String previewEn = plainEn.length() > 140 ? plainEn.substring(0, 140) + "..." : plainEn;

                                serverItems.add(new HajjHistoryCardItem(
                                        id, titleBn, titleEn, previewBn, previewEn, contentBn, contentEn
                                ));
                            }

                            activity.runOnUiThread(() -> {
                                items.clear();
                                items.addAll(serverItems);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
            } catch (Exception ignored) {
                // Offline fallback remains completely active
            }
        }).start();
    }

    private static class TarabiCardAdapter extends RecyclerView.Adapter<TarabiCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private final boolean isBn;
        private float fontSize;

        public TarabiCardAdapter(List<HajjHistoryCardItem> items, boolean isBn, float fontSize) {
            this.items = items;
            this.isBn = isBn;
            this.fontSize = fontSize;
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

            String title = item.getTitle(isBn);
            String preview = item.getPreview(isBn);
            String fullContent = item.getFullContent(isBn);

            holder.binding.tvHistoryItemTitle.setText(title);
            holder.binding.tvHistoryItemPreview.setText(preview);
            holder.binding.tvHistoryItemFullContent.setText(HtmlCompat.fromHtml(fullContent, HtmlCompat.FROM_HTML_MODE_LEGACY));

            holder.binding.tvHistoryItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            holder.binding.tvHistoryItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            boolean isExpanded = item.isExpanded();
            holder.binding.tvHistoryItemFullContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.binding.tvHistoryItemPreview.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            if (isExpanded) {
                holder.binding.tvHistoryToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
                holder.binding.ivHistoryToggleChevron.setRotation(180f);
            } else {
                holder.binding.tvHistoryToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "View Details");
                holder.binding.ivHistoryToggleChevron.setRotation(0f);
            }

            View.OnClickListener toggleClick = v -> {
                boolean nextState = !item.isExpanded();
                item.setExpanded(nextState);
                notifyItemChanged(holder.getAdapterPosition());
            };

            holder.binding.layoutHistoryToggleExpand.setOnClickListener(toggleClick);
            TouchAnimationUtil.attachTouchSpring(holder.binding.layoutHistoryToggleExpand);

            holder.binding.cardHistoryItem.setOnClickListener(toggleClick);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjHistoryCardBinding binding;

            public ViewHolder(@NonNull ItemHajjHistoryCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
