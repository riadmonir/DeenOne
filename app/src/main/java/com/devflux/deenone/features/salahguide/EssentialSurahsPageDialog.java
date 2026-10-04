package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.databinding.PageEssentialSurahsBinding;
import com.devflux.deenone.features.salahguide.adapter.EssentialSurahsAdapter;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EssentialSurahsPageDialog {

    // The 11 essential Surahs for Salah (Surah Al-Fatihah + Last 10 Surahs)
    private static final List<Integer> ESSENTIAL_SURAH_NUMBERS = Arrays.asList(
            1, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114
    );

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageEssentialSurahsBinding binding = PageEssentialSurahsBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Localization
        binding.tvEssentialSurahsTitle.setText(isBn ? "প্রয়োজনীয় সূরা" : "Essential Surahs");
        binding.etSearchEssentialSurahs.setHint(isBn ? "প্রয়োজনীয় সূরা খুঁজুন..." : "Search essential surahs...");

        // Touch feedback
        TouchAnimationUtil.attachTouchSpring(binding.btnBackEssentialSurahs);
        TouchAnimationUtil.attachTouchSpring(binding.btnSearchEssentialSurahs);
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsEssentialSurahs);

        // Back button
        binding.btnBackEssentialSurahs.setOnClickListener(v -> dialog.dismiss());

        // Search Toggle Button
        binding.btnSearchEssentialSurahs.setOnClickListener(v -> {
            boolean isVisible = binding.layoutSearchBarContainer.getVisibility() == View.VISIBLE;
            binding.layoutSearchBarContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            if (!isVisible) {
                binding.etSearchEssentialSurahs.requestFocus();
            } else {
                binding.etSearchEssentialSurahs.setText("");
            }
        });

        // Settings Gear Icon
        binding.btnSettingsEssentialSurahs.setOnClickListener(v -> showSettingsDialog(activity));

        // Load Essential Surahs List
        List<QuranSurahEntity> masterList = loadEssentialSurahs(activity);
        List<QuranSurahEntity> currentDisplayList = new ArrayList<>(masterList);

        // RecyclerView setup with 100% DeenOne Quran UX
        binding.rvEssentialSurahs.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvEssentialSurahs.setHasFixedSize(true);
        binding.rvEssentialSurahs.setItemViewCacheSize(15);

        EssentialSurahsAdapter adapter = new EssentialSurahsAdapter(
                surah -> {
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).showSurahReaderBottomSheet(surah);
                    }
                },
                (surah, position) -> {
                    boolean newFavorite = !surah.isFavorite();
                    surah.setFavorite(newFavorite);
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        try {
                            AppDatabase.getInstance(activity).surahDao().updateFavorite(surah.getNumber(), newFavorite);
                        } catch (Exception ignored) {}
                    });
                    binding.rvEssentialSurahs.post(() -> {
                        if (binding.rvEssentialSurahs.getAdapter() != null) {
                            binding.rvEssentialSurahs.getAdapter().notifyItemChanged(position);
                        }
                    });
                }
        );
        adapter.setSurahs(currentDisplayList);
        binding.rvEssentialSurahs.setAdapter(adapter);

        // Search Filter Logic
        binding.etSearchEssentialSurahs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                binding.btnClearSearchEssentialSurahs.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

                List<QuranSurahEntity> filtered = new ArrayList<>();
                for (QuranSurahEntity item : masterList) {
                    boolean matches = String.valueOf(item.getNumber()).contains(query)
                            || (item.getNameBengali() != null && item.getNameBengali().toLowerCase().contains(query))
                            || (item.getNameEnglish() != null && item.getNameEnglish().toLowerCase().contains(query))
                            || (item.getNameArabic() != null && item.getNameArabic().contains(query))
                            || (item.getMeaningBengali() != null && item.getMeaningBengali().toLowerCase().contains(query))
                            || (item.getMeaningEnglish() != null && item.getMeaningEnglish().toLowerCase().contains(query));

                    if (matches) {
                        filtered.add(item);
                    }
                }
                adapter.setSurahs(filtered);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearchEssentialSurahs.setOnClickListener(v -> {
            binding.etSearchEssentialSurahs.setText("");
        });

        dialog.show();
    }

    private static List<QuranSurahEntity> loadEssentialSurahs(Activity activity) {
        List<QuranSurahEntity> allSurahs = QuranSurahDataSeeder.get114Surahs();
        List<QuranSurahEntity> essentials = new ArrayList<>();

        for (int num : ESSENTIAL_SURAH_NUMBERS) {
            if (num >= 1 && num <= allSurahs.size()) {
                QuranSurahEntity item = allSurahs.get(num - 1);
                // Sync favorite status if stored in Room
                try {
                    QuranSurahEntity dbItem = AppDatabase.getInstance(activity).surahDao().getSurahByNumberSync(num);
                    if (dbItem != null) {
                        item.setFavorite(dbItem.isFavorite());
                    }
                } catch (Exception ignored) {}
                essentials.add(item);
            }
        }
        return essentials;
    }

    private static void showSettingsDialog(Activity activity) {
        boolean isBn = LocaleManager.isBengali(activity);

        String[] optionsBn = {
                "ক্বারী নির্বাচন (তিলাওয়াতকারী)",
                "নামাজে কিরাত মেলানোর নিয়মাবলী",
                "আরবি ফন্ট ও দৃশ্যমানতা সেটিংস"
        };
        String[] optionsEn = {
                "Select Reciter (Audio Qari)",
                "Rules of Surah Recitation in Salah",
                "Arabic Font & Display Settings"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "প্রয়োজনীয় সূরা সেটিংস" : "Essential Surahs Settings")
                .setItems(isBn ? optionsBn : optionsEn, (d, which) -> {
                    if (which == 0) {
                        showReciterPicker(activity, isBn);
                    } else if (which == 1) {
                        showSalahRecitationRules(activity, isBn);
                    } else if (which == 2) {
                        showFontSettings(activity, isBn);
                    }
                })
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }

    private static void showReciterPicker(Activity activity, boolean isBn) {
        QuranCdnAudioHelper.Reciter[] reciters = QuranCdnAudioHelper.Reciter.values();
        String[] names = new String[reciters.length];
        for (int i = 0; i < reciters.length; i++) {
            names[i] = reciters[i].displayName;
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ক্বারী নির্বাচন করুন" : "Select Reciter")
                .setItems(names, (dialog, which) -> {
                    activity.getSharedPreferences("quran_prefs", Activity.MODE_PRIVATE)
                            .edit()
                            .putString("selected_reciter_id", reciters[which].name())
                            .apply();
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void showSalahRecitationRules(Activity activity, boolean isBn) {
        String messageBn = "১. সালাতের প্রতিটি রাকাতে সূরা আল-ফাতিহা পাঠ করা ওয়াজিব।\n\n"
                + "২. প্রথম ও দ্বিতীয় রাকাতে সূরা ফাতিহার পর কুরআন থেকে যেকোনো একটি সূরা বা অন্তত ৩টি ছোট আয়াত মেলানো ওয়াজিব।\n\n"
                + "৩. প্রথম রাকাতে পঠিত সূরার পরের কোনো সূরা দ্বিতীয় রাকাতে পড়া উত্তম (যেমন ১ম রাকাতে সূরা ফীল পড়লে ২য় রাকাতে কুরাইশ বা মাউন পড়া)।\n\n"
                + "৪. উল্টো ক্রমে সূরা পড়া (যেমন ১ম রাকাতে সূরা নাস এবং ২য় রাকাতে সূরা ফালাক) মাকরুহ তানযীহি।\n\n"
                + "৫. ৩য় ও ৪র্থ রাকাতে (ফরজ সালাতে) শুধুমাত্র সূরা ফাতিহা পাঠ করা সুন্নাত।";

        String messageEn = "1. Reciting Surah Al-Fatihah in every Rakat is obligatory (Wajib).\n\n"
                + "2. Joining an additional Surah or at least 3 verses after Surah Fatihah in the first two Rakats is Wajib.\n\n"
                + "3. It is Sunnah to maintain Quranic sequence (e.g. Surah Al-Fil in 1st Rakat, then Surah Quraysh or Ma'un in 2nd Rakat).\n\n"
                + "4. Reciting in reverse order is disliked (Makruh Tanzihi).\n\n"
                + "5. In the 3rd and 4th Rakats of Fard prayers, only Surah Al-Fatihah is recited.";

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "সালাতে কিরাত মেলানোর নিয়ম" : "Rules of Recitation in Salah")
                .setMessage(isBn ? messageBn : messageEn)
                .setPositiveButton(isBn ? "বুঝতে পেরেছি" : "Got It", null)
                .show();
    }

    private static void showFontSettings(Activity activity, boolean isBn) {
        String[] sizesBn = {"সাধারণ হরফ", "মাঝারি হরফ", "বড় হরফ"};
        String[] sizesEn = {"Regular Font", "Medium Font", "Large Font"};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "আরবি হরফের আকার" : "Arabic Font Size")
                .setItems(isBn ? sizesBn : sizesEn, (dialog, which) -> {
                    float size = which == 0 ? 22f : (which == 1 ? 26f : 30f);
                    activity.getSharedPreferences("quran_prefs", Activity.MODE_PRIVATE)
                            .edit()
                            .putFloat("quran_arabic_font_size", size)
                            .apply();
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }
}
