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
import com.devflux.deenone.features.salahguide.data.MasailSahuSajdahContentRepository;
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
 * Production-ready Sahu Sajdah Masail Page Dialog.
 * 100% accurately matching design & verbatim content from provided screenshots.
 */
public class MasailSahuSajdahPageDialog {

    private static final String PREFS_NAME = "masail_sahu_sajdah_prefs";
    private static final String KEY_FONT_SIZE = "masail_sahu_sajdah_font_size";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjHistoryBinding binding = PageHajjHistoryBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "সাহু সেজদাহ" / "Sahu Sajdah")
        String displayTitle = isBn ? "সাহু সেজদাহ" : "Sahu Sajdah";
        binding.tvHajjHistoryTitle.setText(displayTitle);

        // Back Button
        binding.btnBackHajjHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjHistory);

        // Offline-first initial load from Repository
        List<HajjHistoryCardItem> items = new ArrayList<>(MasailSahuSajdahContentRepository.getSahuSajdahCards());

        // SharedPreferences for Font Size
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float currentFontSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);

        SahuSajdahCardAdapter adapter = new SahuSajdahCardAdapter(items, isBn, currentFontSize);
        binding.rvHajjHistoryCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjHistoryCards.setAdapter(adapter);

        // Settings / Options Menu
        binding.btnSettingsHajjHistory.setOnClickListener(v -> {
            showSettingsDialog(activity, items, adapter, isBn, prefs);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjHistory);

        // Asynchronous Online Sync (Topic ID 6 = সাহু সেজদাহ)
        syncWithServer(activity, items, adapter);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<HajjHistoryCardItem> items,
                                           SahuSajdahCardAdapter adapter, boolean isBn, SharedPreferences prefs) {
        String[] options = {
                isBn ? "লেখা বড় / ছোট করুন" : "Adjust Font Size",
                isBn ? "সমস্ত মাসাইল শেয়ার করুন" : "Share All Masail",
                isBn ? "ক্লিপবোর্ডে কপি করুন" : "Copy to Clipboard"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "সাহু সেজদাহ অপশনস" : "Sahu Sajdah Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        showFontSizeDialog(activity, adapter, isBn, prefs);
                    } else if (which == 1) {
                        shareAllMasail(activity, items, isBn);
                    } else if (which == 2) {
                        copyAllMasail(activity, items, isBn);
                    }
                })
                .show();
    }

    private static void showFontSizeDialog(Activity activity, SahuSajdahCardAdapter adapter,
                                           boolean isBn, SharedPreferences prefs) {
        String[] sizes = {
                isBn ? "ছোট (Small)" : "Small",
                isBn ? "স্বাভাবিক (Medium)" : "Medium",
                isBn ? "বড় (Large)" : "Large",
                isBn ? "অনেক বড় (Extra Large)" : "Extra Large"
        };
        float[] sizeValues = {12.5f, 14.5f, 17f, 19.5f};

        int currentSelection = 1;
        float currentSize = prefs.getFloat(KEY_FONT_SIZE, 14.5f);
        for (int i = 0; i < sizeValues.length; i++) {
            if (Math.abs(sizeValues[i] - currentSize) < 0.5f) {
                currentSelection = i;
                break;
            }
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
                .setSingleChoiceItems(sizes, currentSelection, (dialog, which) -> {
                    float selectedSize = sizeValues[which];
                    prefs.edit().putFloat(KEY_FONT_SIZE, selectedSize).apply();
                    adapter.setFontSize(selectedSize);
                    dialog.dismiss();
                })
                .show();
    }

    private static void shareAllMasail(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "📜 সাহু সেজদাহর জরুরি মাসায়েল — দ্বীনওয়ান\n\n" : "📜 Essential Sahu Sajdah Masail — DeenOne\n\n");
        for (int i = 0; i < items.size(); i++) {
            HajjHistoryCardItem item = items.get(i);
            sb.append((i + 1)).append(". ").append(item.getTitle(isBn)).append("\n");
            sb.append(item.getFullContent(isBn)).append("\n\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "সাহু সেজদাহর মাসায়েল" : "Sahu Sajdah Masail");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share Via"));
    }

    private static void copyAllMasail(Activity activity, List<HajjHistoryCardItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "=== সাহু সেজদাহর জরুরি মাসায়েল ===\n\n" : "=== Essential Sahu Sajdah Masail ===\n\n");
        for (int i = 0; i < items.size(); i++) {
            HajjHistoryCardItem item = items.get(i);
            sb.append((i + 1)).append(". ").append(item.getTitle(isBn)).append("\n");
            sb.append(item.getFullContent(isBn)).append("\n\n");
        }

        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "সাহু সেজদাহ মাসায়েল" : "Sahu Sajdah Masail", sb.toString().trim()));
        }
    }

    private static void syncWithServer(Activity activity, List<HajjHistoryCardItem> items, SahuSajdahCardAdapter adapter) {
        new Thread(() -> {
            try {
                String apiUrl = BackendConfigManager.getPhpApiEndpoint(activity, "get_masail_articles.php?topic_id=6");
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

    private static class SahuSajdahCardAdapter extends RecyclerView.Adapter<SahuSajdahCardAdapter.ViewHolder> {
        private final List<HajjHistoryCardItem> items;
        private final boolean isBn;
        private float fontSize;

        public SahuSajdahCardAdapter(List<HajjHistoryCardItem> items, boolean isBn, float fontSize) {
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

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjHistoryCardBinding binding;

            public ViewHolder(@NonNull ItemHajjHistoryCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
