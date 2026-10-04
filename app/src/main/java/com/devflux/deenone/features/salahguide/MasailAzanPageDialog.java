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
import com.devflux.deenone.features.salahguide.data.MasailAzanContentRepository;
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
 * Production-ready Azan Masail Page Dialog.
 * 100% accurately matching design & content from provided screenshot.
 */
public class MasailAzanPageDialog {

    private static final String PREFS_NAME = "masail_azan_prefs";
    private static final String KEY_FONT_SIZE = "masail_azan_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "আজান" / "Adhan")
        String displayTitle = isBn ? "আজান" : "Adhan";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button with Spring Physics
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());

        // Load 5 Azan cards from offline repository (Instant 0-delay load)
        List<HajjHistoryCardItem> items = new ArrayList<>(MasailAzanContentRepository.getAzanCards());

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        AzanCardAdapter adapter = new AzanCardAdapter(activity, items, isBn, savedSize);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings Button (Font Size, Expand All, Share, Copy)
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, isBn, prefs);
        });

        dialog.show();

        // Background Asynchronous Online Sync with PHP API
        syncWithServer(activity, adapter, items);
    }

    private static void showSettingsDialog(Activity activity, AzanCardAdapter adapter, List<HajjHistoryCardItem> items, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "ফন্ট সাইজ পরিবর্তন করুন" : "Change Font Size",
                isBn ? "সবগুলো কার্ড প্রসারিত করুন" : "Expand All Cards",
                isBn ? "সবগুলো কার্ড সংক্ষেপ করুন" : "Collapse All Cards",
                isBn ? "সব মাসআলা কপি করুন" : "Copy All Masail",
                isBn ? "আজানের মাসআলা শেয়ার করুন" : "Share Azan Masail"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "আজানের মাসাইল সেটিংস" : "Adhan Masail Settings")
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

    private static void showFontSizePicker(Activity activity, AzanCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
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
        sb.append(isBn ? "=== আজান সম্পর্কিত জরুরি মাসাইল ===\n\n" : "=== Essential Adhan Masail ===\n\n");
        for (HajjHistoryCardItem it : items) {
            String title = it.getTitle(isBn);
            String content = it.getFullContent(isBn);
            sb.append("• ").append(title).append("\n");
            sb.append(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()).append("\n\n");
        }
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Azan Masail", sb.toString().trim()));
            android.widget.Toast.makeText(activity, isBn ? "আজানের সব মাসাইল ক্লিপবোর্ডে কপি করা হয়েছে" : "All Adhan masail copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareAllContent(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "📢 আজান সম্পর্কিত জরুরি মাসাইল — দ্বীনওয়ান\n\n" : "📢 Essential Adhan Masail — DeenOne\n\n");
        for (HajjHistoryCardItem it : items) {
            String title = it.getTitle(isBn);
            String content = it.getFullContent(isBn);
            sb.append("📌 ").append(title).append("\n");
            sb.append(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()).append("\n\n");
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "আজানের মাসাইল" : "Adhan Masail");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
    }

    private static void syncWithServer(Activity activity, AzanCardAdapter adapter, List<HajjHistoryCardItem> items) {
        new Thread(() -> {
            try {
                String apiUrl = BackendConfigManager.getPhpApiEndpoint(activity, "get_masail_articles.php?topic_id=1");
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    if (json.optBoolean("success", false)) {
                        JSONArray arr = json.optJSONArray("articles");
                        if (arr != null && arr.length() > 0) {
                            List<HajjHistoryCardItem> serverList = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String contentBn = obj.optString("content_bn", "");
                                String contentEn = obj.optString("content_en", "");
                                String previewBn = contentBn.length() > 120 ? contentBn.substring(0, 120) + "..." : contentBn;
                                String previewEn = contentEn.length() > 120 ? contentEn.substring(0, 120) + "..." : contentEn;

                                serverList.add(new HajjHistoryCardItem(
                                        obj.optInt("id", i + 1),
                                        obj.optString("title_bn", ""),
                                        obj.optString("title_en", ""),
                                        previewBn,
                                        previewEn,
                                        contentBn,
                                        contentEn
                                ));
                            }

                            if (!serverList.isEmpty()) {
                                activity.runOnUiThread(() -> {
                                    items.clear();
                                    items.addAll(serverList);
                                    adapter.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception ignored) {
                // Offline fallback remains 100% active
            }
        }).start();
    }

    private static class AzanCardAdapter extends RecyclerView.Adapter<AzanCardAdapter.ViewHolder> {
        private final Activity activity;
        private final List<HajjHistoryCardItem> items;
        private final boolean isBn;
        private float fontSize;

        public AzanCardAdapter(Activity activity, List<HajjHistoryCardItem> items, boolean isBn, float fontSize) {
            this.activity = activity;
            this.items = items;
            this.isBn = isBn;
            this.fontSize = fontSize;
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
