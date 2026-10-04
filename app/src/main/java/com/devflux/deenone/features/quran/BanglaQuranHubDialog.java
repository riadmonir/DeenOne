package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.BanglaQuranAyahItem;
import com.devflux.deenone.core.quran.BanglaQuranManager;
import com.devflux.deenone.core.quran.BanglaQuranTranslator;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.repository.QuranRepository;
import com.devflux.deenone.databinding.DialogBanglaQuranHubBinding;
import com.devflux.deenone.features.home.adapter.QuranAyahAdapter;
import com.devflux.deenone.features.home.adapter.QuranParaAdapter;
import com.devflux.deenone.features.home.adapter.QuranSurahAdapter;
import com.devflux.deenone.features.quran.adapter.BanglaQuranAyahAdapter;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class BanglaQuranHubDialog {

    private final Context context;
    private final FullScreenPageDialog dialog;
    private final DialogBanglaQuranHubBinding binding;
    private final BanglaQuranManager manager;

    private final List<QuranSurahEntity> allSurahs = new ArrayList<>();
    private final List<QuranParaItem> allParas = QuranParaItem.getAll30Paras();

    private QuranSurahAdapter surahAdapter;
    private QuranParaAdapter paraAdapter;

    private QuranSurahAdapter bookmarkedSurahAdapter;
    private QuranParaAdapter bookmarkedParaAdapter;
    private BanglaQuranAyahAdapter bookmarkedAyahAdapter;

    private int activeTab = 0; // 0 = Surahs, 1 = Paras, 2 = Bookmarks
    private int activeBookmarkSubFilter = 0; // 0 = All, 1 = Surahs, 2 = Paras, 3 = Ayahs
    private String currentSearchQuery = "";

    public BanglaQuranHubDialog(Context context) {
        this.context = context;
        this.manager = BanglaQuranManager.getInstance(context);

        this.dialog = new FullScreenPageDialog(context);
        this.binding = DialogBanglaQuranHubBinding.inflate(LayoutInflater.from(context));
        this.dialog.setContentView(binding.getRoot());

        initViews();
    }

    private void initViews() {
        boolean isBn = LocaleManager.isBengali(context);

        // Titles & Subtitles
        binding.tvBanglaQuranTitle.setText(isBn ? "কুরআন বাংলা" : "Bangla Quran");
        binding.tvBanglaQuranSubtitle.setText(isBn
                ? "তানজিল প্রজেক্ট (Tanzil.net) অনুমোদিত বিশুদ্ধ বাংলা অনুবাদ"
                : "Authentic Bengali Translations from Tanzil Project");

        binding.tabBanglaSurahs.setText(isBn ? "১১৪ টি সূরা" : "114 Surahs");
        binding.tabBanglaParas.setText(isBn ? "৩০ টি পারা" : "30 Paras");
        binding.tabBanglaBookmarks.setText(isBn ? "বুকমার্ক" : "Bookmarks");
        binding.etSearchBanglaQuran.setHint(isBn ? "বাংলায় সূরা বা পারা খুঁজুন..." : "Search Surah or Para...");

        updateTranslatorPill();

        // Touch spring strictly on action buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseBanglaQuran);
        TouchAnimationUtil.attachTouchSpring(binding.btnSelectBanglaTranslator);
        TouchAnimationUtil.attachTouchSpring(binding.btnDownloadBanglaQuran);
        TouchAnimationUtil.attachTouchSpring(binding.btnBanglaQuranThemeToggle);
        TouchAnimationUtil.attachTouchSpring(binding.tabBanglaSurahs);
        TouchAnimationUtil.attachTouchSpring(binding.tabBanglaParas);
        TouchAnimationUtil.attachTouchSpring(binding.tabBanglaBookmarks);
        TouchAnimationUtil.attachTouchSpring(binding.chipBookmarkAll);
        TouchAnimationUtil.attachTouchSpring(binding.chipBookmarkSurahs);
        TouchAnimationUtil.attachTouchSpring(binding.chipBookmarkParas);
        TouchAnimationUtil.attachTouchSpring(binding.chipBookmarkAyahs);

        binding.btnCloseBanglaQuran.setOnClickListener(v -> dialog.dismiss());

        // Translator Selector Pill
        binding.btnSelectBanglaTranslator.setOnClickListener(v -> {
            BanglaQuranTranslatorDialog.show(context, selectedTranslator -> {
                updateTranslatorPill();
                if (bookmarkedAyahAdapter != null) {
                    bookmarkedAyahAdapter.setTranslator(selectedTranslator);
                }
            });
        });

        // Theme Toggle
        binding.btnBanglaQuranThemeToggle.setOnClickListener(v -> {
            if (context instanceof com.devflux.deenone.MainActivity) {
                ((com.devflux.deenone.MainActivity) context).toggleAppTheme();
                dialog.dismiss();
            }
        });

        // Offline Download Button Setup
        setupBatchDownloadButton();

        // Setup Adapters
        setupSurahAdapter();
        setupParaAdapter();
        setupBookmarkAdapters();

        // Tab Navigation Listeners
        binding.tabBanglaSurahs.setOnClickListener(v -> switchTab(0));
        binding.tabBanglaParas.setOnClickListener(v -> switchTab(1));
        binding.tabBanglaBookmarks.setOnClickListener(v -> switchTab(2));

        // Bookmark Sub-Filter Chips Listeners
        binding.chipBookmarkAll.setOnClickListener(v -> switchBookmarkSubFilter(0));
        binding.chipBookmarkSurahs.setOnClickListener(v -> switchBookmarkSubFilter(1));
        binding.chipBookmarkParas.setOnClickListener(v -> switchBookmarkSubFilter(2));
        binding.chipBookmarkAyahs.setOnClickListener(v -> switchBookmarkSubFilter(3));

        // Search Input Listener
        binding.etSearchBanglaQuran.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s != null ? s.toString().trim().toLowerCase() : "";
                applySearchFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load 114 Surahs from DB
        loadSurahsData();
    }

    private void updateTranslatorPill() {
        boolean isBn = LocaleManager.isBengali(context);
        BanglaQuranTranslator translator = manager.getActiveTranslator();
        binding.tvActiveTranslatorPill.setText(translator.getShortName(isBn) + " ▾");
    }

    private void setupSurahAdapter() {
        surahAdapter = new QuranSurahAdapter(
                surah -> new BanglaQuranReaderDialog(context, surah, 1).show(),
                (surah, position) -> {
                    boolean newState = manager.toggleSurahBookmark(surah.getNumber());
                    surah.setFavorite(newState);
                    surahAdapter.notifyItemChanged(position);
                    refreshBookmarks();
                }
        );

        binding.rvBanglaSurahs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBanglaSurahs.setHasFixedSize(true);
        binding.rvBanglaSurahs.setItemViewCacheSize(20);
        binding.rvBanglaSurahs.setAdapter(surahAdapter);
    }

    private void setupParaAdapter() {
        paraAdapter = new QuranParaAdapter(
                para -> new BanglaQuranReaderDialog(context, para).show(),
                (para, position) -> {
                    manager.toggleParaBookmark(para.getJuzNumber());
                    paraAdapter.notifyItemChanged(position);
                    refreshBookmarks();
                }
        );

        binding.rvBanglaParas.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBanglaParas.setHasFixedSize(true);
        binding.rvBanglaParas.setItemViewCacheSize(20);
        binding.rvBanglaParas.setAdapter(paraAdapter);
    }

    private void setupBookmarkAdapters() {
        // Bookmarked Surahs
        bookmarkedSurahAdapter = new QuranSurahAdapter(
                surah -> new BanglaQuranReaderDialog(context, surah, 1).show(),
                (surah, position) -> {
                    manager.toggleSurahBookmark(surah.getNumber());
                    refreshBookmarks();
                }
        );
        binding.rvBookmarkedSurahs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBookmarkedSurahs.setAdapter(bookmarkedSurahAdapter);

        // Bookmarked Paras
        bookmarkedParaAdapter = new QuranParaAdapter(
                para -> new BanglaQuranReaderDialog(context, para).show(),
                (para, position) -> {
                    manager.toggleParaBookmark(para.getJuzNumber());
                    refreshBookmarks();
                }
        );
        binding.rvBookmarkedParas.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBookmarkedParas.setAdapter(bookmarkedParaAdapter);

        // Bookmarked Ayahs
        bookmarkedAyahAdapter = new BanglaQuranAyahAdapter(new BanglaQuranAyahAdapter.OnAyahActionListener() {
            @Override
            public void onPlayAudio(BanglaQuranAyahItem ayah, int position) {
                // Open reader at this ayah
                QuranSurahEntity targetSurah = getSurahByNumber(ayah.getSurahNumber());
                if (targetSurah != null) {
                    new BanglaQuranReaderDialog(context, targetSurah, ayah.getAyahNumber()).show();
                }
            }

            @Override
            public void onBookmarkToggle(BanglaQuranAyahItem ayah, int position) {
                refreshBookmarks();
            }
        });
        bookmarkedAyahAdapter.setTranslator(manager.getActiveTranslator());
        bookmarkedAyahAdapter.setArabicShown(manager.isArabicShown());
        bookmarkedAyahAdapter.setArabicFontScale(manager.getArabicFontScale());
        bookmarkedAyahAdapter.setBanglaFontSizeSp(manager.getBanglaFontSizeSp());

        binding.rvBookmarkedAyahs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBookmarkedAyahs.setAdapter(bookmarkedAyahAdapter);
    }

    private void loadSurahsData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<QuranSurahEntity> list = AppDatabase.getInstance(context).surahDao().getAllSurahsSync();
            if (list != null && !list.isEmpty()) {
                allSurahs.clear();
                for (QuranSurahEntity s : list) {
                    s.setFavorite(manager.isSurahBookmarked(s.getNumber()));
                    allSurahs.add(s);
                }

                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        surahAdapter.setSurahs(new ArrayList<>(allSurahs));
                        paraAdapter.setParas(new ArrayList<>(allParas));
                        refreshBookmarks();
                    });
                }
            }
        });
    }

    private void switchTab(int index) {
        activeTab = index;

        // Update tab styles
        binding.tabBanglaSurahs.setBackgroundResource(index == 0 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.tabBanglaSurahs.setTextColor(context.getColor(index == 0 ? R.color.accent_mint : R.color.text_secondary));

        binding.tabBanglaParas.setBackgroundResource(index == 1 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.tabBanglaParas.setTextColor(context.getColor(index == 1 ? R.color.accent_mint : R.color.text_secondary));

        binding.tabBanglaBookmarks.setBackgroundResource(index == 2 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.tabBanglaBookmarks.setTextColor(context.getColor(index == 2 ? R.color.accent_mint : R.color.text_secondary));

        // Switch visible view
        binding.rvBanglaSurahs.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        binding.rvBanglaParas.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        binding.layoutBanglaBookmarksContainer.setVisibility(index == 2 ? View.VISIBLE : View.GONE);

        if (index == 2) {
            refreshBookmarks();
        } else {
            applySearchFilter();
        }
    }

    private void switchBookmarkSubFilter(int filterIndex) {
        activeBookmarkSubFilter = filterIndex;

        binding.chipBookmarkAll.setBackgroundResource(filterIndex == 0 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.chipBookmarkAll.setTextColor(context.getColor(filterIndex == 0 ? R.color.accent_mint : R.color.text_secondary));

        binding.chipBookmarkSurahs.setBackgroundResource(filterIndex == 1 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.chipBookmarkSurahs.setTextColor(context.getColor(filterIndex == 1 ? R.color.accent_mint : R.color.text_secondary));

        binding.chipBookmarkParas.setBackgroundResource(filterIndex == 2 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.chipBookmarkParas.setTextColor(context.getColor(filterIndex == 2 ? R.color.accent_mint : R.color.text_secondary));

        binding.chipBookmarkAyahs.setBackgroundResource(filterIndex == 3 ? R.drawable.bg_tab_active : R.drawable.bg_badge_pill);
        binding.chipBookmarkAyahs.setTextColor(context.getColor(filterIndex == 3 ? R.color.accent_mint : R.color.text_secondary));

        applyBookmarkVisibility();
    }

    private void applyBookmarkVisibility() {
        int surahCount = bookmarkedSurahAdapter.getItemCount();
        int paraCount = bookmarkedParaAdapter.getItemCount();
        int ayahCount = bookmarkedAyahAdapter.getItemCount();

        boolean showSurahs = (activeBookmarkSubFilter == 0 || activeBookmarkSubFilter == 1) && surahCount > 0;
        boolean showParas = (activeBookmarkSubFilter == 0 || activeBookmarkSubFilter == 2) && paraCount > 0;
        boolean showAyahs = (activeBookmarkSubFilter == 0 || activeBookmarkSubFilter == 3) && ayahCount > 0;

        binding.layoutBookmarkedSurahsSection.setVisibility(showSurahs ? View.VISIBLE : View.GONE);
        binding.layoutBookmarkedParasSection.setVisibility(showParas ? View.VISIBLE : View.GONE);
        binding.layoutBookmarkedAyahsSection.setVisibility(showAyahs ? View.VISIBLE : View.GONE);

        boolean isTotalEmpty = (surahCount == 0 && paraCount == 0 && ayahCount == 0);
        binding.layoutBanglaBookmarksEmpty.setVisibility(isTotalEmpty ? View.VISIBLE : View.GONE);
        binding.nsvBanglaBookmarksContent.setVisibility(isTotalEmpty ? View.GONE : View.VISIBLE);
    }

    private void refreshBookmarks() {
        // Bookmarked Surahs
        List<QuranSurahEntity> bSurahs = new ArrayList<>();
        for (QuranSurahEntity s : allSurahs) {
            if (manager.isSurahBookmarked(s.getNumber())) {
                s.setFavorite(true);
                bSurahs.add(s);
            }
        }
        bookmarkedSurahAdapter.setSurahs(bSurahs);

        // Bookmarked Paras
        List<QuranParaItem> bParas = new ArrayList<>();
        for (QuranParaItem p : allParas) {
            if (manager.isParaBookmarked(p.getJuzNumber())) {
                bParas.add(p);
            }
        }
        bookmarkedParaAdapter.setParas(bParas);

        // Bookmarked Ayahs
        manager.getBookmarkedAyahs(ayahs -> {
            bookmarkedAyahAdapter.setAyahs(ayahs);
            applyBookmarkVisibility();
        });
    }

    private void applySearchFilter() {
        if (currentSearchQuery.isEmpty()) {
            surahAdapter.setSurahs(new ArrayList<>(allSurahs));
            paraAdapter.setParas(new ArrayList<>(allParas));
            return;
        }

        // Filter Surahs
        List<QuranSurahEntity> filteredSurahs = new ArrayList<>();
        for (QuranSurahEntity s : allSurahs) {
            String nameBn = s.getNameBengali() != null ? s.getNameBengali().toLowerCase() : "";
            String nameEn = s.getNameEnglish() != null ? s.getNameEnglish().toLowerCase() : "";
            String numStr = String.valueOf(s.getNumber());
            String numBn = BengaliNumberUtil.toBengali(s.getNumber());

            if (nameBn.contains(currentSearchQuery) || nameEn.contains(currentSearchQuery)
                    || numStr.contains(currentSearchQuery) || numBn.contains(currentSearchQuery)) {
                filteredSurahs.add(s);
            }
        }
        surahAdapter.setSurahs(filteredSurahs);

        // Filter Paras
        List<QuranParaItem> filteredParas = new ArrayList<>();
        boolean isBn = LocaleManager.isBengali(context);
        for (QuranParaItem p : allParas) {
            String nameBn = p.getBengaliName() != null ? p.getBengaliName().toLowerCase() : "";
            String nameEn = p.getEnglishName() != null ? p.getEnglishName().toLowerCase() : "";
            String range = (isBn ? p.getRangeBengali() : p.getRangeEnglish());
            range = range != null ? range.toLowerCase() : "";
            String numStr = String.valueOf(p.getJuzNumber());
            String numBn = BengaliNumberUtil.toBengali(p.getJuzNumber());

            if (nameBn.contains(currentSearchQuery) || nameEn.contains(currentSearchQuery)
                    || range.contains(currentSearchQuery) || numStr.contains(currentSearchQuery)
                    || numBn.contains(currentSearchQuery)) {
                filteredParas.add(p);
            }
        }
        paraAdapter.setParas(filteredParas);
    }

    private void setupBatchDownloadButton() {
        boolean isAllDownloaded = manager.isAllQuranDownloaded();
        if (isAllDownloaded) {
            binding.ivDownloadQuranIcon.setVisibility(View.VISIBLE);
            binding.ivDownloadQuranIcon.setImageResource(R.drawable.ic_check_circle);
            binding.pbDownloadQuran.setVisibility(View.GONE);
            binding.tvDownloadQuranPercent.setVisibility(View.GONE);
        } else {
            binding.ivDownloadQuranIcon.setVisibility(View.VISIBLE);
            binding.ivDownloadQuranIcon.setImageResource(R.drawable.ic_download);
            binding.pbDownloadQuran.setVisibility(View.GONE);
            binding.tvDownloadQuranPercent.setVisibility(View.GONE);
        }

        binding.btnDownloadBanglaQuran.setOnClickListener(v -> {
            if (manager.isBatchDownloading()) {
                Toast.makeText(context, "ডাউনলোড ইতিমধ্যে ব্যাকগ্রাউন্ডে চলছে...", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.ivDownloadQuranIcon.setVisibility(View.GONE);
            binding.pbDownloadQuran.setVisibility(View.GONE);
            binding.tvDownloadQuranPercent.setVisibility(View.VISIBLE);
            binding.tvDownloadQuranPercent.setText("0%");

            manager.downloadAllBanglaQuranOffline(new BanglaQuranManager.DownloadListener() {
                @Override
                public void onProgress(int current, int total, int percentage, String surahNameBn, String surahNameEn) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> {
                            binding.tvDownloadQuranPercent.setText(percentage + "%");
                        });
                    }
                }

                @Override
                public void onSuccess() {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> {
                            binding.tvDownloadQuranPercent.setVisibility(View.GONE);
                            binding.pbDownloadQuran.setVisibility(View.GONE);
                            binding.ivDownloadQuranIcon.setVisibility(View.VISIBLE);
                            binding.ivDownloadQuranIcon.setImageResource(R.drawable.ic_check_circle);
                            Toast.makeText(context, "১১৪ টি সূরার বাংলা অনুবাদ সফলভাবে ডাউনলোড হয়েছে", Toast.LENGTH_LONG).show();
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> {
                            binding.tvDownloadQuranPercent.setVisibility(View.GONE);
                            binding.pbDownloadQuran.setVisibility(View.GONE);
                            binding.ivDownloadQuranIcon.setVisibility(View.VISIBLE);
                            binding.ivDownloadQuranIcon.setImageResource(R.drawable.ic_download);
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        });
                    }
                }

                @Override
                public void onCancelled() {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> {
                            binding.tvDownloadQuranPercent.setVisibility(View.GONE);
                            binding.pbDownloadQuran.setVisibility(View.GONE);
                            binding.ivDownloadQuranIcon.setVisibility(View.VISIBLE);
                            binding.ivDownloadQuranIcon.setImageResource(R.drawable.ic_download);
                        });
                    }
                }
            });
        });
    }

    private QuranSurahEntity getSurahByNumber(int surahNum) {
        for (QuranSurahEntity s : allSurahs) {
            if (s.getNumber() == surahNum) return s;
        }
        return null;
    }

    public void show() {
        dialog.show();
    }
}
