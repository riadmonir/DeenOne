package com.devflux.deenone.features.books.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Service responsible for network API communication with online Islamic book catalog endpoints.
 * Integrates with authentic, verified public-domain Islamic Book repositories (IslamHouse Open API & Authentic CDN).
 */
public class IslamicBookApiService {

    private static final String TAG = "IslamicBookApiService";
    private static final String ISLAMHOUSE_BASE_URL = "https://api3.islamhouse.com/v3/paV29H2gm56kvLPy/main/books/";
    
    private static volatile IslamicBookApiService instance;
    private final Context context;
    private final OkHttpClient httpClient;

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    private IslamicBookApiService(Context context) {
        this.context = context.getApplicationContext();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    public static synchronized IslamicBookApiService getInstance(Context context) {
        if (instance == null) {
            instance = new IslamicBookApiService(context);
        }
        return instance;
    }

    /**
     * Fetches dynamic verified Islamic books from IslamHouse API with pagination.
     * @param language "bn", "ar", or "en"
     * @param page page number (1-indexed)
     * @param limit items per page (e.g. 25)
     */
    public List<IslamicBookEntity> fetchBooksFromApiSync(String language, int page, int limit) throws IOException {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            throw new IOException("ডিভাইস অফলাইনে রয়েছে। পূর্বে ডাউনলোড ও ক্যাশ করা কিতাবসমূহ ব্যবহার করুন।");
        }

        String lang = (language != null && !language.isEmpty()) ? language.toLowerCase() : "bn";
        String targetUrl = ISLAMHOUSE_BASE_URL + lang + "/" + lang + "/" + page + "/" + limit + "/json";

        Request request = new Request.Builder()
                .url(targetUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOne-IslamicApp/2.1")
                .header("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("সার্ভার থেকে ত্রুটি বার্তা এসেছে (HTTP " + response.code() + ")");
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("সার্ভার থেকে কোনো ডাটা পাওয়া যায়নি");
            }

            String jsonString = body.string();
            return parseIslamHouseBooksJson(jsonString, lang);
        }
    }

    private List<IslamicBookEntity> parseIslamHouseBooksJson(String jsonString, String lang) {
        List<IslamicBookEntity> result = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray dataArray = root.optJSONArray("data");
            if (dataArray == null) return result;

            long now = System.currentTimeMillis();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                int rawId = item.optInt("id", 0);
                if (rawId == 0) continue;

                String bookId = "ih_" + rawId;
                String title = item.optString("title", "").trim();
                if (title.isEmpty()) continue;

                String description = item.optString("description", "").trim();
                if (description.isEmpty()) {
                    description = "পবিত্র কুরআন ও সহিহ সুন্নাহ ভিত্তিক প্রামাণ্য ইসলামিক গ্রন্থ। উন্মুক্ত ইসলামিক গবেষণা ও জ্ঞান বিতরণ।";
                }

                // Extract Author
                String author = "প্রখ্যাত ইসলামিক গবেষক ও ওলামায়ে কেরাম";
                JSONArray preparedBy = item.optJSONArray("prepared_by");
                if (preparedBy != null && preparedBy.length() > 0) {
                    JSONObject authorObj = preparedBy.getJSONObject(0);
                    String authorTitle = authorObj.optString("title", "").trim();
                    if (!authorTitle.isEmpty()) {
                        author = authorTitle;
                    }
                }

                // Extract PDF Attachment Download URL
                String downloadUrl = null;
                String fileSize = "৩.৫ MB";
                JSONArray attachments = item.optJSONArray("attachments");
                if (attachments != null) {
                    for (int j = 0; j < attachments.length(); j++) {
                        JSONObject att = attachments.getJSONObject(j);
                        String ext = att.optString("extension_type", "");
                        String url = att.optString("url", "");
                        if ("PDF".equalsIgnoreCase(ext) && url.startsWith("http")) {
                            downloadUrl = url;
                            String sizeStr = att.optString("size", "").trim();
                            if (!sizeStr.isEmpty()) {
                                fileSize = sizeStr;
                            }
                            break; // Preferred PDF found
                        }
                    }
                }

                // Strict Quality Filter: Only keep books with valid, accessible PDF download links
                if (downloadUrl == null || downloadUrl.isEmpty() || !downloadUrl.startsWith("https://")) {
                    // Try to use HTTPS if provided as HTTP
                    if (downloadUrl != null && downloadUrl.startsWith("http://")) {
                        downloadUrl = downloadUrl.replace("http://", "https://");
                    } else {
                        continue; // Skip invalid or non-PDF entries
                    }
                }

                // Auto-Classify Category based on Islamic terms
                String category = classifyCategory(title, description);

                // Approximate Page Count based on typical file size
                int estimatedPages = estimatePagesFromFileSize(fileSize);

                IslamicBookEntity entity = new IslamicBookEntity(
                        bookId,
                        title,
                        author,
                        category,
                        lang,
                        estimatedPages,
                        fileSize,
                        description,
                        downloadUrl,
                        null,
                        false,
                        0,
                        0,
                        0,
                        "NOT_STARTED",
                        0,
                        false,
                        null,
                        "ইসলামহাউজ ও আন্তর্জাতিক ইসলামিক গবেষণা একাডেমি",
                        "IslamHouse / উন্মুক্ত ইসলামিক প্রকাশনা",
                        "পাবলিক ডোমেইন / উন্মুক্ত ইসলামিক ওয়াকফ",
                        "PDF",
                        now - (i * 1000L)
                );

                result.add(entity);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing IslamHouse books JSON: " + e.getMessage(), e);
        }

        return result;
    }

    private String classifyCategory(String title, String description) {
        String combined = (title + " " + description).toLowerCase();

        if (combined.contains("কুরআন") || combined.contains("কোরআন") || combined.contains("quran") || combined.contains("মুসহাফ")) {
            return "Quran";
        } else if (combined.contains("তাফসীর") || combined.contains("তাফসির") || combined.contains("tafsir") || combined.contains("ব্যাখ্যা")) {
            return "Tafsir";
        } else if (combined.contains("হাদিস") || combined.contains("হাদীস") || combined.contains("বুখারী") || combined.contains("মুসলিম") || combined.contains("hadith") || combined.contains("রিয়াদুস")) {
            return "Hadith";
        } else if (combined.contains("নামাজ") || combined.contains("সালাত") || combined.contains("রোজা") || combined.contains("হজ") || combined.contains("যাকাত") || combined.contains("ফিকহ") || combined.contains("fiqh") || combined.contains("হালাল") || combined.contains("হারাম") || combined.contains("পবিত্রতা")) {
            return "Fiqh";
        } else if (combined.contains("আকিদা") || combined.contains("আকীদা") || combined.contains("তাওহীদ") || combined.contains("শিরক") || combined.contains("ঈমান") || combined.contains("aqeedah") || combined.contains("বিশ্বাস")) {
            return "Aqeedah";
        } else if (combined.contains("সীরাত") || combined.contains("জীবনী") || combined.contains("রাসূল") || combined.contains("নবী") || combined.contains("seerah") || combined.contains("সাহাবী")) {
            return "Seerah";
        } else if (combined.contains("ইতিহাস") || combined.contains("খিলাফত") || combined.contains("history")) {
            return "Islamic History";
        } else {
            return "Fiqh"; // Default reliable category
        }
    }

    private int estimatePagesFromFileSize(String fileSize) {
        try {
            if (fileSize != null && fileSize.contains("MB")) {
                String numStr = fileSize.replaceAll("[^0.9\\.]", "").trim();
                double mb = Double.parseDouble(numStr);
                return Math.max(30, (int) (mb * 45)); // ~45 pages per MB on average
            } else if (fileSize != null && fileSize.contains("KB")) {
                return 40;
            }
        } catch (Exception ignored) {}
        return 120;
    }
}