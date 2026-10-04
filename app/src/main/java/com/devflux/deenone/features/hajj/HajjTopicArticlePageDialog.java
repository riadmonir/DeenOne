package com.devflux.deenone.features.hajj;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemHajjArticleCardBinding;
import com.devflux.deenone.databinding.PageHajjTopicArticleBinding;
import com.devflux.deenone.features.hajj.data.HajjArticleContentRepository;
import com.devflux.deenone.features.hajj.model.HajjArticleCardItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.core.text.HtmlCompat;

import com.devflux.deenone.core.backend.BackendConfigManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HajjTopicArticlePageDialog {

    private static final String PREFS_NAME = "hajj_article_prefs";
    private static final String KEY_FONT_SIZE = "article_font_size";

    public static void show(@NonNull Activity activity, int topicId, @NonNull String title) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjTopicArticleBinding binding = PageHajjTopicArticleBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (adaptive to language)
        String displayTitle = (title != null && !title.isEmpty()) ? title : (isBn ? "হজ" : "Hajj");
        binding.tvHajjArticleTitle.setText(displayTitle);

        // Back Button with Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjArticle);
        binding.btnBackHajjArticle.setOnClickListener(v -> dialog.dismiss());

        // Load Content Cards for this topic
        List<HajjArticleCardItem> cards = HajjArticleContentRepository.getArticleCardsForTopic(topicId);

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 15.0f);

        HajjArticleAdapter adapter = new HajjArticleAdapter(activity, cards, savedSize, isBn);
        binding.rvHajjArticleCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvHajjArticleCards.setAdapter(adapter);

        // Settings Gear Action Button
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsHajjArticle);
        binding.btnSettingsHajjArticle.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, displayTitle, cards, prefs, isBn);
        });

        // Asynchronous Background Sync with PHP REST API (Rule 10)
        syncOnlineArticlesInBackground(activity, adapter, cards, topicId);

        dialog.show();
    }

    public static void show(@NonNull Activity activity, @NonNull String title) {
        show(activity, 1, title);
    }

    private static void showSettingsDialog(Activity activity, HajjArticleAdapter adapter, String pageTitle, List<HajjArticleCardItem> cards, SharedPreferences prefs, boolean isBn) {
        String[] options;
        if (isBn) {
            options = new String[]{
                    "ফন্ট সাইজ: ছোট (১৩ sp)",
                    "ফন্ট সাইজ: সাধারণ (১৫ sp)",
                    "ফন্ট সাইজ: প্রমিত (১৭ sp)",
                    "ফন্ট সাইজ: বড় (১৯ sp)",
                    "ফন্ট সাইজ: বিশাল (২১ sp)",
                    "সম্পূর্ণ লেখা কপি করুন",
                    "সম্পূর্ণ লেখা শেয়ার করুন"
            };
        } else {
            options = new String[]{
                    "Font Size: Small (13 sp)",
                    "Font Size: Normal (15 sp)",
                    "Font Size: Medium (17 sp)",
                    "Font Size: Large (19 sp)",
                    "Font Size: Extra Large (21 sp)",
                    "Copy Entire Article",
                    "Share Entire Article"
            };
        }
        float[] sizes = {13.0f, 15.0f, 17.0f, 19.0f, 21.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which >= 0 && which <= 4) {
                        float chosen = sizes[which];
                        adapter.setFontSize(chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 5) {
                        copyToClipboard(activity, pageTitle, getFullContentText(pageTitle, cards, isBn));
                    } else if (which == 6) {
                        shareContent(activity, pageTitle, getFullContentText(pageTitle, cards, isBn));
                    }
                })
                .show();
    }

    private static void showCardActionMenu(Activity activity, HajjArticleCardItem card, boolean isBn) {
        String[] options = isBn
                ? new String[]{"কার্ডের লেখা কপি করুন", "কার্ডের লেখা শেয়ার করুন"}
                : new String[]{"Copy Card Text", "Share Card Text"};

        String cardTitle = card.getTitle(isBn);
        String rawContent = card.getContent(isBn);
        String cardContent = (rawContent != null && rawContent.contains("<") && rawContent.contains(">"))
                ? androidx.core.text.HtmlCompat.fromHtml(rawContent, androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                : (rawContent != null ? rawContent : "");

        new MaterialAlertDialogBuilder(activity)
                .setTitle(cardTitle)
                .setItems(options, (d, which) -> {
                    String fullCardText = cardTitle + "\n\n" + cardContent;
                    if (which == 0) {
                        copyToClipboard(activity, cardTitle, fullCardText);
                    } else if (which == 1) {
                        shareContent(activity, cardTitle, fullCardText);
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            android.widget.Toast.makeText(context, isBn ? "আর্টিকেল কপি করা হয়েছে" : "Article copied to clipboard", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
        context.startActivity(Intent.createChooser(sendIntent, title));
    }

    private static String getFullContentText(String title, List<HajjArticleCardItem> cards, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        for (HajjArticleCardItem it : cards) {
            sb.append("■ ").append(it.getTitle(isBn)).append("\n\n");
            String raw = it.getContent(isBn);
            String plain = (raw != null && raw.contains("<") && raw.contains(">"))
                    ? androidx.core.text.HtmlCompat.fromHtml(raw, androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                    : (raw != null ? raw : "");
            sb.append(plain).append("\n\n");
            sb.append("------------------------------------\n\n");
        }
        return sb.toString();
    }

    public static class HajjArticleAdapter extends RecyclerView.Adapter<HajjArticleAdapter.ViewHolder> {
        private final Activity activity;
        private final List<HajjArticleCardItem> items;
        private float fontSize;
        private final boolean isBn;

        public HajjArticleAdapter(Activity activity, List<HajjArticleCardItem> items, float fontSize, boolean isBn) {
            this.activity = activity;
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
            ItemHajjArticleCardBinding binding = ItemHajjArticleCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HajjArticleCardItem item = items.get(position);
            ItemHajjArticleCardBinding b = holder.binding;

            b.tvArticleCardTitle.setText(item.getTitle(isBn));
            String content = item.getContent(isBn);
            if (content != null) {
                String htmlFormatted = formatArticleHtml(content);
                b.tvArticleCardContent.setText(androidx.core.text.HtmlCompat.fromHtml(htmlFormatted, androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                b.tvArticleCardContent.setText("");
            }
            b.tvArticleCardContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            b.btnArticleCardMore.setOnClickListener(v -> {
                showCardActionMenu(activity, item, isBn);
            });
        }

        private String formatArticleHtml(String content) {
            if (content == null) return "";
            String[] lines = content.replace("\r\n", "\n").split("\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (containsArabic(line) && !line.contains("<big>")) {
                    sb.append("<big><b>").append(line.trim()).append("</b></big>");
                } else {
                    sb.append(line);
                }
                if (i < lines.length - 1) {
                    sb.append("<br>");
                }
            }
            String result = sb.toString();
            return result.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
        }

        private boolean containsArabic(String text) {
            if (text == null) return false;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if ((c >= 0x0600 && c <= 0x06FF) || (c >= 0x0750 && c <= 0x077F) || (c >= 0xFB50 && c <= 0xFDFF)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemHajjArticleCardBinding binding;

            public ViewHolder(@NonNull ItemHajjArticleCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static void syncOnlineArticlesInBackground(Activity activity, HajjArticleAdapter adapter, List<HajjArticleCardItem> cards, int topicId) {
        new Thread(() -> {
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(activity, "get_hajj_articles.php?topic_id=" + topicId);
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
                            List<HajjArticleCardItem> onlineCards = new ArrayList<>();
                            for (int i = 0; i < articlesArr.length(); i++) {
                                JSONObject obj = articlesArr.getJSONObject(i);
                                int id = obj.optInt("id", i + 1);
                                String titleBn = obj.optString("title_bn", "");
                                String titleEn = obj.optString("title_en", "");
                                String contentBn = obj.optString("content_bn", "");
                                String contentEn = obj.optString("content_en", "");

                                onlineCards.add(new HajjArticleCardItem(
                                    id, titleBn, titleEn, contentBn, contentEn
                                ));
                            }

                            activity.runOnUiThread(() -> {
                                cards.clear();
                                cards.addAll(onlineCards);
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
}
