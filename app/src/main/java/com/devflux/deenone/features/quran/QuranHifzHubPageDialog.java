package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.HifzProgressManager;
import com.devflux.deenone.core.quran.QuranAudioCacheManager;
import com.devflux.deenone.core.quran.QuranBismillahHelper;
import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.core.quran.QuranPageDataHelper;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.repository.QuranRepository;
import com.devflux.deenone.databinding.ActivityQuranHifzHubBinding;
import com.devflux.deenone.databinding.BottomSheetHafeziAyahActionBinding;
import com.devflux.deenone.features.quran.adapter.HafeziMushafPageAdapter;
import com.devflux.deenone.features.quran.adapter.HifzIndexAdapter;
import com.devflux.deenone.features.quran.adapter.HifzMistakesAdapter;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
 * 60 FPS Production-Ready Hifz Hub & Hafezi Quran Screen (Dialog).
 * Complete implementation for Hafiz:
 * - 15-Line Hafezi Mushaf Page Mode (Pages 1 to 604)
 * - Self-Test Hifz Mask Mode (Blind Memorization Test)
 * - Sabaq, Sabaqi & Muraja'ah Dor Planner
 * - 30 Paras & 114 Surahs Directory
 * - Marked Mistakes & Mutashabihat Hub
 * - Audio Repetition Loop Player (3x, 5x, 10x, 20x)
 */
public class QuranHifzHubPageDialog extends FullScreenPageDialog {

    private final Activity activity;
    private final boolean isBn;
    private final ActivityQuranHifzHubBinding binding;
    private final HifzProgressManager hifzManager;
    private final QuranRepository quranRepository;

    private HafeziMushafPageAdapter mushafAdapter;
    private HifzIndexAdapter indexAdapter;
    private HifzMistakesAdapter mistakesAdapter;

    private int activeTab = 0; // 0=Mushaf, 1=Tracker, 2=Index, 3=Mistakes
    private int activePageIndex = 0; // 0-based, pageNumber = activePageIndex + 1

    // Audio Loop Engine
    private MediaPlayer mediaPlayer;
    private QuranAyahEntity activeLoopAyah;
    private int currentLoopTarget = 3;
    private int currentLoopIteration = 0;
    private boolean isLoopPlaying = false;
    private final Handler audioHandler = new Handler(Looper.getMainLooper());

    public static void open(Activity activity, int initialPage) {
        if (activity == null || activity.isFinishing()) return;
        QuranOfflineDownloadDialog.showIfNeeded(activity, () -> {
            QuranHifzHubPageDialog dialog = new QuranHifzHubPageDialog(activity, initialPage);
            dialog.show();
        });
    }

    public QuranHifzHubPageDialog(@NonNull Activity activity, int initialPage) {
        super(activity);
        this.activity = activity;
        this.isBn = LocaleManager.isBengali(activity);
        this.hifzManager = HifzProgressManager.getInstance(activity);
        this.quranRepository = QuranRepository.getInstance(activity);

        binding = ActivityQuranHifzHubBinding.inflate(LayoutInflater.from(activity));
        setContentView(binding.getRoot());

        int initialPageIndex = (initialPage >= 1 && initialPage <= 604) ? (initialPage - 1) : 0;
        this.activePageIndex = initialPageIndex;

        setupLocalizedUI();
        setupMushafViewPager();
        setupTrackerTab();
        setupIndexTab();
        setupMistakesTab();
        setupAudioLoopUI();
        setupNavigationAndListeners();

        // Switch to initial page
        binding.viewPagerMushaf.setCurrentItem(initialPageIndex, false);
        updateQuickJumpBar(initialPageIndex + 1);
    }

    private void setupLocalizedUI() {
        binding.tvHifzMainTitle.setText(isBn ? "হাফেজী কুরআন ও হিফজ হাব" : "Hafezi Quran & Hifz Hub");
        binding.tvHifzMainSubtitle.setText(isBn ? "১৫-লাইন পৃষ্ঠা মোড ও মুরাজাআ ট্র্যাকার" : "15-Line Page Mode & Muraja'ah Tracker");

        binding.tabMushaf.setText(isBn ? "হাফেজী কুরআন" : "Hafezi Mushaf");
        binding.tabTracker.setText(isBn ? "হিফজ ট্র্যাকার" : "Hifz Tracker");
        binding.tabIndex.setText(isBn ? "সূচিপত্র" : "Directory");
        binding.tabMistakes.setText(isBn ? "ভুল রিভিশন" : "Mistakes");

        binding.tvTrackerHeader.setText(isBn ? "হিফজ অগ্রগতি মিটার" : "Hifz Progress Meter");
        binding.tvSabaqCardTitle.setText(isBn ? "আজকের সবক (নতুন মুখস্থ)" : "Today's Sabaq (New Lesson)");
        binding.btnOpenSabaqPage.setText(isBn ? "পৃষ্ঠায় যান" : "Go to Page");
        binding.btnCompleteSabaq.setText(isBn ? "✓ সম্পন্ন হয়েছে" : "✓ Mark Complete");

        binding.tvSabaqiCardTitle.setText(isBn ? "সবকী (সাম্প্রতিক পাঠ রিভিশন)" : "Sabaqi (Recent Revision)");
        binding.tvSabaqiCardDesc.setText(isBn
                ? "বিগত ৩-৫ দিনের নতুন পড়া একসাথে শুনানো ও রিভিশন"
                : "Revision of recently memorized lessons (last 3-5 days)");
        binding.btnCompleteSabaqi.setText(isBn ? "সবকী রিভিশন সম্পন্ন ✓" : "Sabaqi Complete ✓");

        binding.tvMurajaahCardTitle.setText(isBn ? "মুরাজাআ / মনজিল দৌড় (৩০ পারা রিভিশন)" : "Muraja'ah / Dor (30 Para Revision)");
        binding.btnCompleteMurajaah.setText(isBn ? "আজকের মুরাজাআ সম্পন্ন করুন ✓" : "Complete Muraja'ah ✓");

        binding.btnSubFilterParas.setText(isBn ? "৩০ পারা সূচিপত্র" : "30 Paras Directory");
        binding.btnSubFilterSurahs.setText(isBn ? "১১৪ সূরা সূচিপত্র" : "114 Surahs Directory");

        binding.tvMistakesTabHeader.setText(isBn
                ? "পড়ার সময় যে যে আয়াতে ভুল হয়েছিল বা আটকে গেছেন, সেগুলো এখানে সংরক্ষিত থাকে যাতে দ্রুত রিভিশন দেওয়া যায়।"
                : "Verses where mistakes occurred during recitation are saved here for quick focused revision.");

        binding.btnLoopPill3x.setText(isBn ? "৩ বার" : "3 Times");
        binding.btnLoopPill5x.setText(isBn ? "৫ বার" : "5 Times");
        binding.btnLoopPill10x.setText(isBn ? "১০ বার" : "10 Times");
        binding.btnLoopPill20x.setText(isBn ? "২০ বার" : "20 Times");
    }

    private void setupMushafViewPager() {
        mushafAdapter = new HafeziMushafPageAdapter(activity, (ayah, pageNumber) -> {
            showAyahActionBottomSheet(ayah, pageNumber);
        });

        binding.viewPagerMushaf.setAdapter(mushafAdapter);
        binding.viewPagerMushaf.setOffscreenPageLimit(2);
        binding.viewPagerMushaf.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                activePageIndex = position;
                int pageNum = position + 1;
                updateQuickJumpBar(pageNum);

                String floatingStr = isBn
                        ? (BengaliNumberUtil.toBengali(pageNum) + " / " + BengaliNumberUtil.toBengali(604))
                        : (pageNum + " / 604");
                binding.tvFloatingPageNumber.setText(floatingStr);
            }
        });
    }

    private void updateQuickJumpBar(int pageNumber) {
        QuranPageDataHelper.PageInfo info = QuranPageDataHelper.getPageInfo(pageNumber);
        int juzNum = info.juzNumber;
        List<QuranParaItem> allParas = QuranParaItem.getAll30Paras();
        String paraName = (juzNum >= 1 && juzNum <= allParas.size())
                ? (isBn ? ("পারা " + BengaliNumberUtil.toBengali(juzNum)) : ("Para " + juzNum))
                : ("পারা " + juzNum);
        binding.btnJumpPara.setText(paraName + " ⌵");

        AppDatabase.databaseWriteExecutor.execute(() -> {
            QuranSurahEntity surah = AppDatabase.getInstance(activity).surahDao().getSurahByNumberSync(info.startSurah);
            binding.getRoot().post(() -> {
                if (surah != null) {
                    String sName = isBn ? surah.getNameBengali() : surah.getNameEnglish();
                    binding.btnJumpSurah.setText(sName + " ⌵");
                }
            });
        });

        binding.btnJumpPage.setText((isBn ? ("পৃষ্ঠা " + BengaliNumberUtil.toBengali(pageNumber)) : ("Page " + pageNumber)) + " ⌵");
    }

    private void setupTrackerTab() {
        refreshTrackerProgressUI();

        binding.btnOpenSabaqPage.setOnClickListener(v -> {
            int page = hifzManager.getSabaqPage();
            switchTab(0);
            binding.viewPagerMushaf.setCurrentItem(page - 1, true);
        });

        binding.btnCompleteSabaq.setOnClickListener(v -> {
            boolean done = hifzManager.isTodaySabaqDone();
            hifzManager.setTodaySabaqDone(!done);
            refreshTrackerProgressUI();
            Toast.makeText(activity, isBn ? "আজকের সবক সম্পন্ন হিসেবে চিহ্নিত হয়েছে!" : "Today's Sabaq marked complete!", Toast.LENGTH_SHORT).show();
        });

        binding.btnCompleteSabaqi.setOnClickListener(v -> {
            boolean done = hifzManager.isTodaySabaqiDone();
            hifzManager.setTodaySabaqiDone(!done);
            refreshTrackerProgressUI();
            Toast.makeText(activity, isBn ? "আজকের সবকী রিভিশন সম্পন্ন!" : "Today's Sabaqi marked complete!", Toast.LENGTH_SHORT).show();
        });

        binding.btnCompleteMurajaah.setOnClickListener(v -> {
            hifzManager.completeTodayMurajaah();
            refreshTrackerProgressUI();
            Toast.makeText(activity, isBn ? "আজকের মুরাজাআ সম্পন্ন হয়েছে!" : "Muraja'ah complete!", Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshTrackerProgressUI() {
        int streak = hifzManager.getStreakDays();
        binding.tvTrackerStreak.setText(isBn ? ("🔥 " + BengaliNumberUtil.toBengali(streak) + " দিন ধারাবাহিক") : ("🔥 " + streak + " Days Streak"));

        int memorizedParas = hifzManager.getMemorizedParasCount();
        int percent = (memorizedParas * 100) / 30;
        binding.progressBarOverallHifz.setProgress(percent);
        binding.tvTrackerParasDone.setText(isBn
                ? (BengaliNumberUtil.toBengali(memorizedParas) + " / ৩০ পারা সম্পূর্ণ")
                : (memorizedParas + " / 30 Paras Complete"));
        binding.tvTrackerPercent.setText(isBn ? (BengaliNumberUtil.toBengali(percent) + "%") : (percent + "%"));

        int sabaqPara = hifzManager.getSabaqPara();
        int sabaqPage = hifzManager.getSabaqPage();
        binding.tvSabaqCardDesc.setText(isBn
                ? ("পারা " + BengaliNumberUtil.toBengali(sabaqPara) + " • পৃষ্ঠা " + BengaliNumberUtil.toBengali(sabaqPage))
                : ("Para " + sabaqPara + " • Page " + sabaqPage));

        boolean sabaqDone = hifzManager.isTodaySabaqDone();
        binding.btnCompleteSabaq.setText(sabaqDone ? (isBn ? "✓ সম্পন্ন হয়েছে" : "✓ Completed") : (isBn ? "সম্পন্ন করুন" : "Mark Done"));

        boolean sabaqiDone = hifzManager.isTodaySabaqiDone();
        binding.btnCompleteSabaqi.setText(sabaqiDone ? (isBn ? "সবকী সম্পন্ন ✓" : "Sabaqi Done ✓") : (isBn ? "সবকী রিভিশন সম্পন্ন করুন" : "Complete Sabaqi"));

        int currentMurajaahPara = hifzManager.getMurajaahCurrentPara();
        List<QuranParaItem> paras = QuranParaItem.getAll30Paras();
        String pName = (currentMurajaahPara >= 1 && currentMurajaahPara <= paras.size())
                ? (isBn ? paras.get(currentMurajaahPara - 1).getBengaliName() : paras.get(currentMurajaahPara - 1).getEnglishName())
                : "";
        binding.tvMurajaahCardDesc.setText(isBn
                ? ("আজকের রিভিশন: পারা " + BengaliNumberUtil.toBengali(currentMurajaahPara) + " (" + pName + ")")
                : ("Today's Revision: Para " + currentMurajaahPara + " (" + pName + ")"));
    }

    private void setupIndexTab() {
        binding.rvHifzIndex.setLayoutManager(new LinearLayoutManager(activity));
        indexAdapter = new HifzIndexAdapter(targetPage -> {
            switchTab(0);
            binding.viewPagerMushaf.setCurrentItem(targetPage - 1, true);
        });
        binding.rvHifzIndex.setAdapter(indexAdapter);

        // Load Surahs into index
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<QuranSurahEntity> surahs = AppDatabase.getInstance(activity).surahDao().getAllSurahs().getValue();
            if (surahs == null || surahs.isEmpty()) {
                surahs = QuranSurahDataSeeder.get114Surahs();
            }
            List<QuranSurahEntity> finalSurahs = surahs;
            binding.getRoot().post(() -> indexAdapter.setSurahs(finalSurahs));
        });

        binding.btnSubFilterParas.setOnClickListener(v -> {
            indexAdapter.setMode(HifzIndexAdapter.MODE_PARAS);
            binding.btnSubFilterParas.setBackgroundResource(R.drawable.bg_badge_pill_teal);
            binding.btnSubFilterParas.setTextColor(ContextCompat.getColor(activity, R.color.bg_main));
            binding.btnSubFilterSurahs.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnSubFilterSurahs.setTextColor(ContextCompat.getColor(activity, R.color.accent_mint));
        });

        binding.btnSubFilterSurahs.setOnClickListener(v -> {
            indexAdapter.setMode(HifzIndexAdapter.MODE_SURAHS);
            binding.btnSubFilterSurahs.setBackgroundResource(R.drawable.bg_badge_pill_teal);
            binding.btnSubFilterSurahs.setTextColor(ContextCompat.getColor(activity, R.color.bg_main));
            binding.btnSubFilterParas.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnSubFilterParas.setTextColor(ContextCompat.getColor(activity, R.color.accent_mint));
        });
    }

    private void setupMistakesTab() {
        binding.rvHifzMistakes.setLayoutManager(new LinearLayoutManager(activity));
        mistakesAdapter = new HifzMistakesAdapter(new HifzMistakesAdapter.OnMistakeActionListener() {
            @Override
            public void onPlayLoop(HifzProgressManager.HifzMistakeItem item) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    QuranAyahEntity ayah = AppDatabase.getInstance(activity).ayahDao().getAyahSync(item.surahNumber, item.ayahNumber);
                    binding.getRoot().post(() -> {
                        if (ayah != null) {
                            startAudioLoop(ayah, 3);
                        }
                    });
                });
            }

            @Override
            public void onJumpPage(int pageNumber) {
                switchTab(0);
                binding.viewPagerMushaf.setCurrentItem(pageNumber - 1, true);
            }

            @Override
            public void onRemove(HifzProgressManager.HifzMistakeItem item) {
                hifzManager.toggleAyahMistake(item.surahNumber, item.ayahNumber, item.surahNameBn, item.surahNameEn, item.textArabic, item.note);
                refreshMistakesList();
            }
        });
        binding.rvHifzMistakes.setAdapter(mistakesAdapter);
        refreshMistakesList();
    }

    private void refreshMistakesList() {
        List<HifzProgressManager.HifzMistakeItem> list = hifzManager.getAllMarkedMistakes();
        mistakesAdapter.setMistakes(list);
    }

    private void setupNavigationAndListeners() {
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHifzHub);
        TouchAnimationUtil.attachTouchSpring(binding.btnToggleMaskMode);
        TouchAnimationUtil.attachTouchSpring(binding.btnTopAudioLoop);
        TouchAnimationUtil.attachTouchSpring(binding.btnTopMistakesList);
        TouchAnimationUtil.attachTouchSpring(binding.btnJumpPara);
        TouchAnimationUtil.attachTouchSpring(binding.btnJumpSurah);
        TouchAnimationUtil.attachTouchSpring(binding.btnJumpPage);
        TouchAnimationUtil.attachTouchSpring(binding.btnPrevPage);
        TouchAnimationUtil.attachTouchSpring(binding.btnNextPage);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubFilterParas);
        TouchAnimationUtil.attachTouchSpring(binding.btnSubFilterSurahs);
        TouchAnimationUtil.attachTouchSpring(binding.btnOpenSabaqPage);
        TouchAnimationUtil.attachTouchSpring(binding.btnCompleteSabaq);
        TouchAnimationUtil.attachTouchSpring(binding.btnCompleteSabaqi);
        TouchAnimationUtil.attachTouchSpring(binding.btnCompleteMurajaah);

        binding.btnBackHifzHub.setOnClickListener(v -> dismiss());

        // Mask Mode Toggle
        binding.btnToggleMaskMode.setOnClickListener(v -> {
            boolean newMode = !mushafAdapter.isMaskMode();
            mushafAdapter.setMaskMode(newMode);
            hifzManager.setMaskMode(newMode);
            binding.ivMaskIcon.setImageResource(newMode ? R.drawable.ic_eye_off : R.drawable.ic_eye);
            Toast.makeText(activity, newMode
                    ? (isBn ? "হিফজ মাস্ক মোড চালু হয়েছে • আয়াত লুকানো আছে" : "Mask Mode Enabled • Ayahs are hidden")
                    : (isBn ? "হিফজ মাস্ক মোড বন্ধ হয়েছে" : "Mask Mode Disabled"), Toast.LENGTH_SHORT).show();
        });

        // Top Audio Loop
        binding.btnTopAudioLoop.setOnClickListener(v -> {
            if (binding.layoutHifzAudioLoopBar.getVisibility() == View.VISIBLE) {
                stopAudioLoop();
                binding.layoutHifzAudioLoopBar.setVisibility(View.GONE);
            } else {
                binding.layoutHifzAudioLoopBar.setVisibility(View.VISIBLE);
            }
        });

        // Top Mistakes Button
        binding.btnTopMistakesList.setOnClickListener(v -> switchTab(3));

        // Prev & Next Floating Page Buttons
        binding.btnPrevPage.setOnClickListener(v -> {
            if (activePageIndex > 0) {
                binding.viewPagerMushaf.setCurrentItem(activePageIndex - 1, true);
            }
        });

        binding.btnNextPage.setOnClickListener(v -> {
            if (activePageIndex < 603) {
                binding.viewPagerMushaf.setCurrentItem(activePageIndex + 1, true);
            }
        });

        // Quick Jump Dialogs
        binding.btnJumpPara.setOnClickListener(v -> showParaJumpDialog());
        binding.btnJumpSurah.setOnClickListener(v -> showSurahJumpDialog());
        binding.btnJumpPage.setOnClickListener(v -> showPageJumpDialog());

        // Tab Navigation Clicks
        binding.tabMushaf.setOnClickListener(v -> switchTab(0));
        binding.tabTracker.setOnClickListener(v -> switchTab(1));
        binding.tabIndex.setOnClickListener(v -> switchTab(2));
        binding.tabMistakes.setOnClickListener(v -> switchTab(3));
    }

    private void switchTab(int tabIndex) {
        this.activeTab = tabIndex;

        binding.tabMushaf.setBackgroundResource(tabIndex == 0 ? R.drawable.bg_hadith_segmented_active : 0);
        binding.tabMushaf.setTextColor(ContextCompat.getColor(activity, tabIndex == 0 ? R.color.bg_main : R.color.text_secondary));

        binding.tabTracker.setBackgroundResource(tabIndex == 1 ? R.drawable.bg_hadith_segmented_active : 0);
        binding.tabTracker.setTextColor(ContextCompat.getColor(activity, tabIndex == 1 ? R.color.bg_main : R.color.text_secondary));

        binding.tabIndex.setBackgroundResource(tabIndex == 2 ? R.drawable.bg_hadith_segmented_active : 0);
        binding.tabIndex.setTextColor(ContextCompat.getColor(activity, tabIndex == 2 ? R.color.bg_main : R.color.text_secondary));

        binding.tabMistakes.setBackgroundResource(tabIndex == 3 ? R.drawable.bg_hadith_segmented_active : 0);
        binding.tabMistakes.setTextColor(ContextCompat.getColor(activity, tabIndex == 3 ? R.color.bg_main : R.color.text_secondary));

        binding.containerMushafTab.setVisibility(tabIndex == 0 ? View.VISIBLE : View.GONE);
        binding.containerTrackerTab.setVisibility(tabIndex == 1 ? View.VISIBLE : View.GONE);
        binding.containerIndexTab.setVisibility(tabIndex == 2 ? View.VISIBLE : View.GONE);
        binding.containerMistakesTab.setVisibility(tabIndex == 3 ? View.VISIBLE : View.GONE);

        if (tabIndex == 1) refreshTrackerProgressUI();
        if (tabIndex == 3) refreshMistakesList();
    }

    private void showParaJumpDialog() {
        List<QuranParaItem> paras = QuranParaItem.getAll30Paras();
        String[] items = new String[paras.size()];
        for (int i = 0; i < paras.size(); i++) {
            QuranParaItem p = paras.get(i);
            int startPage = QuranPageDataHelper.getPageForAyah(p.getStartSurahNumber(), p.getStartAyahNumber());
            items[i] = (isBn ? ("পারা " + BengaliNumberUtil.toBengali(p.getJuzNumber()) + ": " + p.getBengaliName() + " (পৃষ্ঠা " + BengaliNumberUtil.toBengali(startPage) + ")")
                    : ("Para " + p.getJuzNumber() + ": " + p.getEnglishName() + " (Page " + startPage + ")"));
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পারা নির্বাচন করুন" : "Select Para")
                .setItems(items, (d, which) -> {
                    QuranParaItem p = paras.get(which);
                    int startPage = QuranPageDataHelper.getPageForAyah(p.getStartSurahNumber(), p.getStartAyahNumber());
                    switchTab(0);
                    binding.viewPagerMushaf.setCurrentItem(startPage - 1, true);
                })
                .show();
    }

    private void showSurahJumpDialog() {
        List<QuranSurahEntity> surahs = QuranSurahDataSeeder.get114Surahs();
        String[] items = new String[surahs.size()];
        for (int i = 0; i < surahs.size(); i++) {
            QuranSurahEntity s = surahs.get(i);
            int page = QuranPageDataHelper.getPageForAyah(s.getNumber(), 1);
            items[i] = (isBn ? (BengaliNumberUtil.toBengali(s.getNumber()) + ". " + s.getNameBengali() + " (" + s.getNameArabic() + " - পৃষ্ঠা " + BengaliNumberUtil.toBengali(page) + ")")
                    : (s.getNumber() + ". " + s.getNameEnglish() + " (" + s.getNameArabic() + " - Page " + page + ")"));
        }

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "সূরা নির্বাচন করুন" : "Select Surah")
                .setItems(items, (d, which) -> {
                    QuranSurahEntity s = surahs.get(which);
                    int page = QuranPageDataHelper.getPageForAyah(s.getNumber(), 1);
                    switchTab(0);
                    binding.viewPagerMushaf.setCurrentItem(page - 1, true);
                })
                .show();
    }

    private void showPageJumpDialog() {
        final EditText input = new EditText(activity);
        input.setHint(isBn ? "১ থেকে ৬০৪ এর মধ্যে পৃষ্ঠা নম্বর লিখুন" : "Enter page number (1 to 604)");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(ContextCompat.getColor(activity, R.color.text_primary));

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পৃষ্ঠায় যান" : "Jump to Page")
                .setView(input)
                .setPositiveButton(isBn ? "যান" : "Go", (dialog, which) -> {
                    String str = input.getText().toString().trim();
                    try {
                        int page = Integer.parseInt(str);
                        if (page >= 1 && page <= 604) {
                            switchTab(0);
                            binding.viewPagerMushaf.setCurrentItem(page - 1, true);
                        } else {
                            Toast.makeText(activity, isBn ? "অনুগ্রহ করে ১ থেকে ৬০৪ এর মধ্যে নম্বর দিন" : "Please enter between 1 and 604", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ignored) {}
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private void showAyahActionBottomSheet(QuranAyahEntity ayah, int pageNumber) {
        if (ayah == null) return;
        BottomSheetDialog sheet = new BottomSheetDialog(activity);
        BottomSheetHafeziAyahActionBinding sheetBinding = BottomSheetHafeziAyahActionBinding.inflate(LayoutInflater.from(activity));
        sheet.setContentView(sheetBinding.getRoot());

        QuranPageDataHelper.PageInfo pInfo = QuranPageDataHelper.getPageInfo(pageNumber);
        String sTitle = isBn
                ? ("সূরা " + ayah.getSurahNumber() + ": আয়াত " + BengaliNumberUtil.toBengali(ayah.getAyahNumber()))
                : ("Surah " + ayah.getSurahNumber() + ": Ayah " + ayah.getAyahNumber());
        sheetBinding.tvAyahActionTitle.setText(sTitle);
        sheetBinding.tvAyahActionSubtitle.setText(isBn
                ? ("পারা " + BengaliNumberUtil.toBengali(pInfo.juzNumber) + " • পৃষ্ঠা " + BengaliNumberUtil.toBengali(pageNumber))
                : ("Para " + pInfo.juzNumber + " • Page " + pageNumber));

        String rawArabic = ayah.getTextArabic() != null ? ayah.getTextArabic().trim() : "";
        String cleanArabic = QuranRepository.sanitizeArabicVerse(ayah.getSurahNumber(), ayah.getAyahNumber(), rawArabic);
        sheetBinding.tvAyahActionArabic.setText(cleanArabic + " ۝" + BengaliNumberUtil.toArabicDigits(ayah.getAyahNumber()));

        String translation = isBn ? ayah.getTranslationBengali() : ayah.getTranslationEnglish();
        sheetBinding.tvAyahActionTranslation.setText((isBn ? "অর্থ: " : "Translation: ") + (translation != null ? translation : ""));

        boolean isMistake = hifzManager.isAyahMarkedMistake(ayah.getSurahNumber(), ayah.getAyahNumber());
        sheetBinding.btnActionToggleMistake.setText(isMistake
                ? (isBn ? "✓ চিহ্নিত ভুলটি তালিকা থেকে সরান" : "✓ Remove from Mistakes")
                : (isBn ? "⚠️ ভুল / মুতাশাবিহাত হিসেবে চিহ্নিত করুন" : "⚠️ Mark as Mistake / Mutashabihat"));

        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseAyahAction);
        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnActionPlayLoop);
        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnActionToggleMistake);
        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnActionWordByWord);
        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnActionBookmark);
        TouchAnimationUtil.attachTouchSpring(sheetBinding.btnActionCopy);

        sheetBinding.btnCloseAyahAction.setOnClickListener(v -> sheet.dismiss());

        sheetBinding.btnActionPlayLoop.setOnClickListener(v -> {
            sheet.dismiss();
            startAudioLoop(ayah, 3);
        });

        sheetBinding.btnActionToggleMistake.setOnClickListener(v -> {
            sheet.dismiss();
            hifzManager.toggleAyahMistake(ayah.getSurahNumber(), ayah.getAyahNumber(),
                    "সূরা " + ayah.getSurahNumber(), "Surah " + ayah.getSurahNumber(),
                    cleanArabic, "");
            mushafAdapter.notifyDataSetChanged();
            refreshMistakesList();
            Toast.makeText(activity, isMistake ? (isBn ? "মুছে ফেলা হয়েছে" : "Removed") : (isBn ? "ভুল হিসেবে চিহ্নিত হয়েছে" : "Marked as Mistake"), Toast.LENGTH_SHORT).show();
        });

        sheetBinding.btnActionWordByWord.setOnClickListener(v -> {
            sheet.dismiss();
            QuranWordByWordDialog.show(activity, ayah.getSurahNumber(), "সূরা " + ayah.getSurahNumber(), "Surah " + ayah.getSurahNumber(), ayah.getAyahNumber());
        });

        sheetBinding.btnActionBookmark.setOnClickListener(v -> {
            sheet.dismiss();
            quranRepository.setAyahBookmarked(ayah.getSurahNumber(), ayah.getAyahNumber(), !ayah.isBookmarked(), "সূরা " + ayah.getSurahNumber(), cleanArabic);
            Toast.makeText(activity, isBn ? "বুকমার্কে যোগ করা হয়েছে" : "Bookmarked", Toast.LENGTH_SHORT).show();
        });

        sheetBinding.btnActionCopy.setOnClickListener(v -> {
            sheet.dismiss();
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cd = ClipData.newPlainText("Ayah", cleanArabic + "\n" + (translation != null ? translation : ""));
            if (cm != null) cm.setPrimaryClip(cd);
            Toast.makeText(activity, isBn ? "কপি করা হয়েছে" : "Copied", Toast.LENGTH_SHORT).show();
        });

        sheet.show();
    }

    private void setupAudioLoopUI() {
        TouchAnimationUtil.attachTouchSpring(binding.btnLoopPlayPause);
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseLoopBar);
        TouchAnimationUtil.attachTouchSpring(binding.btnLoopPill3x);
        TouchAnimationUtil.attachTouchSpring(binding.btnLoopPill5x);
        TouchAnimationUtil.attachTouchSpring(binding.btnLoopPill10x);
        TouchAnimationUtil.attachTouchSpring(binding.btnLoopPill20x);

        binding.btnCloseLoopBar.setOnClickListener(v -> {
            stopAudioLoop();
            binding.layoutHifzAudioLoopBar.setVisibility(View.GONE);
        });

        binding.btnLoopPlayPause.setOnClickListener(v -> {
            if (isLoopPlaying) {
                pauseAudioLoop();
            } else {
                resumeAudioLoop();
            }
        });

        binding.btnLoopPill3x.setOnClickListener(v -> setLoopTargetCount(3));
        binding.btnLoopPill5x.setOnClickListener(v -> setLoopTargetCount(5));
        binding.btnLoopPill10x.setOnClickListener(v -> setLoopTargetCount(10));
        binding.btnLoopPill20x.setOnClickListener(v -> setLoopTargetCount(20));
    }

    private void setLoopTargetCount(int target) {
        this.currentLoopTarget = target;
        binding.btnLoopPill3x.setBackgroundResource(target == 3 ? R.drawable.bg_badge_pill_teal : R.drawable.bg_badge_pill);
        binding.btnLoopPill3x.setTextColor(ContextCompat.getColor(activity, target == 3 ? R.color.bg_main : R.color.accent_mint));

        binding.btnLoopPill5x.setBackgroundResource(target == 5 ? R.drawable.bg_badge_pill_teal : R.drawable.bg_badge_pill);
        binding.btnLoopPill5x.setTextColor(ContextCompat.getColor(activity, target == 5 ? R.color.bg_main : R.color.accent_mint));

        binding.btnLoopPill10x.setBackgroundResource(target == 10 ? R.drawable.bg_badge_pill_teal : R.drawable.bg_badge_pill);
        binding.btnLoopPill10x.setTextColor(ContextCompat.getColor(activity, target == 10 ? R.color.bg_main : R.color.accent_mint));

        binding.btnLoopPill20x.setBackgroundResource(target == 20 ? R.drawable.bg_badge_pill_teal : R.drawable.bg_badge_pill);
        binding.btnLoopPill20x.setTextColor(ContextCompat.getColor(activity, target == 20 ? R.color.bg_main : R.color.accent_mint));

        updateLoopStatusText();
    }

    private void updateLoopStatusText() {
        String counter = isBn
                ? ("পুনরাবৃত্তি: " + BengaliNumberUtil.toBengali(currentLoopIteration) + " / " + BengaliNumberUtil.toBengali(currentLoopTarget) + " বার")
                : ("Loop: " + currentLoopIteration + " / " + currentLoopTarget + " Times");
        binding.tvLoopCounterStatus.setText(counter);
    }

    private void startAudioLoop(QuranAyahEntity ayah, int loopCount) {
        if (ayah == null) return;
        this.activeLoopAyah = ayah;
        this.currentLoopTarget = loopCount;
        this.currentLoopIteration = 1;
        setLoopTargetCount(loopCount);

        String title = isBn
                ? ("সূরা " + ayah.getSurahNumber() + ": আয়াত " + BengaliNumberUtil.toBengali(ayah.getAyahNumber()) + " • অডিও লুপ")
                : ("Surah " + ayah.getSurahNumber() + ": Ayah " + ayah.getAyahNumber() + " • Audio Loop");
        binding.tvLoopAyahTitle.setText(title);
        binding.layoutHifzAudioLoopBar.setVisibility(View.VISIBLE);

        playAyahAudioUrl(ayah);
    }

    private void playAyahAudioUrl(QuranAyahEntity ayah) {
        stopAudioLoop();
        Uri playbackUri = QuranAudioCacheManager.getAyahPlaybackUri(
                activity, QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, ayah.getSurahNumber(), ayah.getAyahNumber()
        );

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
            mediaPlayer.setDataSource(activity, playbackUri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isLoopPlaying = true;
                binding.ivLoopPlayPause.setImageResource(R.drawable.ic_pause);
                updateLoopStatusText();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                if (currentLoopIteration < currentLoopTarget) {
                    currentLoopIteration++;
                    updateLoopStatusText();
                    audioHandler.postDelayed(() -> {
                        if (mediaPlayer != null) {
                            try {
                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            } catch (Exception ignored) {
                                playAyahAudioUrl(activeLoopAyah);
                            }
                        }
                    }, 500);
                } else {
                    isLoopPlaying = false;
                    binding.ivLoopPlayPause.setImageResource(R.drawable.ic_play_arrow);
                    updateLoopStatusText();
                }
            });
        } catch (Exception e) {
            isLoopPlaying = false;
            binding.ivLoopPlayPause.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    private void pauseAudioLoop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isLoopPlaying = false;
            binding.ivLoopPlayPause.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    private void resumeAudioLoop() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isLoopPlaying = true;
            binding.ivLoopPlayPause.setImageResource(R.drawable.ic_pause);
        } else if (activeLoopAyah != null) {
            playAyahAudioUrl(activeLoopAyah);
        }
    }

    private void stopAudioLoop() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        isLoopPlaying = false;
    }

    @Override
    public void dismiss() {
        stopAudioLoop();
        super.dismiss();
    }
}
