package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.HifzProgressManager;
import com.devflux.deenone.core.quran.QuranBismillahHelper;
import com.devflux.deenone.core.quran.QuranPageDataHelper;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.AyahDao;
import com.devflux.deenone.data.local.dao.SurahDao;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.repository.QuranRepository;
import com.devflux.deenone.databinding.ItemHafeziMushafPageBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 60 FPS, High-Performance Hafezi Mushaf 15-Line Page Adapter for ViewPager2.
 * Supports 604 Canonical Pages with Hifz Self-Test Mask Mode and Mistakes Highlight.
 */
public class HafeziMushafPageAdapter extends RecyclerView.Adapter<HafeziMushafPageAdapter.PageViewHolder> {

    public interface OnAyahInteractListener {
        void onAyahClick(QuranAyahEntity ayah, int pageNumber);
    }

    private final Context context;
    private final AyahDao ayahDao;
    private final SurahDao surahDao;
    private final HifzProgressManager hifzManager;
    private final OnAyahInteractListener interactListener;
    private final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private boolean isMaskMode = false;
    private final Map<Integer, Boolean> pageMaskRevealedMap = new HashMap<>();
    private final Map<Integer, List<QuranAyahEntity>> pageAyahsCache = new HashMap<>();

    public HafeziMushafPageAdapter(Context context, OnAyahInteractListener interactListener) {
        this.context = context;
        this.interactListener = interactListener;
        AppDatabase db = AppDatabase.getInstance(context);
        this.ayahDao = db.ayahDao();
        this.surahDao = db.surahDao();
        this.hifzManager = HifzProgressManager.getInstance(context);
        this.isMaskMode = hifzManager.isMaskMode();
    }

    public void setMaskMode(boolean maskMode) {
        this.isMaskMode = maskMode;
        pageMaskRevealedMap.clear();
        notifyDataSetChanged();
    }

    public boolean isMaskMode() {
        return isMaskMode;
    }

    @Override
    public int getItemCount() {
        return QuranPageDataHelper.TOTAL_PAGES;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHafeziMushafPageBinding binding = ItemHafeziMushafPageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        int pageNumber = position + 1;
        holder.bind(pageNumber);
    }

    class PageViewHolder extends RecyclerView.ViewHolder {
        private final ItemHafeziMushafPageBinding binding;

        public PageViewHolder(@NonNull ItemHafeziMushafPageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int pageNumber) {
            boolean isBn = LocaleManager.isBengali(context);
            QuranPageDataHelper.PageInfo pageInfo = QuranPageDataHelper.getPageInfo(pageNumber);

            // Page Number Badge
            binding.tvPageNumberBadge.setText(isBn ? BengaliNumberUtil.toBengali(pageNumber) : String.valueOf(pageNumber));

            // Juz (Para) Header
            int juzNum = pageInfo.juzNumber;
            List<QuranParaItem> allParas = QuranParaItem.getAll30Paras();
            String juzName = (juzNum >= 1 && juzNum <= allParas.size())
                    ? (isBn ? ("পারা " + BengaliNumberUtil.toBengali(juzNum) + ": " + allParas.get(juzNum - 1).getBengaliName())
                            : ("Para " + juzNum + ": " + allParas.get(juzNum - 1).getEnglishName()))
                    : ("পারা " + juzNum);
            binding.tvPageHeaderJuz.setText(juzName);

            // Manzil & Ruku Footer
            int manzil = getManzilForPage(pageNumber);
            binding.tvPageFooterManzil.setText(isBn ? ("মঞ্জিল " + BengaliNumberUtil.toBengali(manzil)) : ("Manzil " + manzil));
            binding.tvPageFooterRuku.setText(isBn ? ("পারা " + BengaliNumberUtil.toBengali(juzNum)) : ("Juz " + juzNum));

            // Mask Mode State
            boolean isRevealed = pageMaskRevealedMap.containsKey(pageNumber) && Boolean.TRUE.equals(pageMaskRevealedMap.get(pageNumber));
            if (isMaskMode && !isRevealed) {
                binding.tvMaskModeTapHint.setVisibility(View.VISIBLE);
                binding.tvMaskModeTapHint.setText(isBn ? "👁️ হিফজ মাস্ক মোড সক্রিয় • ট্যাপ করে আয়াত দেখুন" : "👁️ Hifz Mask Mode Active • Tap to reveal ayahs");
                binding.tvPageArabicText.setTextColor(Color.TRANSPARENT);
                binding.tvPageArabicText.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_card_secondary));
            } else {
                binding.tvMaskModeTapHint.setVisibility(isMaskMode ? View.VISIBLE : View.GONE);
                if (isMaskMode) {
                    binding.tvMaskModeTapHint.setText(isBn ? "✓ আয়াত দৃশ্যমান • ট্যাপ করে আবার লুকান" : "✓ Ayahs Revealed • Tap to hide again");
                }
                binding.tvPageArabicText.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                binding.tvPageArabicText.setBackgroundColor(Color.TRANSPARENT);
            }

            // Click listener for Mask Mode Toggle
            binding.tvPageArabicText.setOnClickListener(v -> {
                if (isMaskMode) {
                    boolean currentRev = pageMaskRevealedMap.containsKey(pageNumber) && Boolean.TRUE.equals(pageMaskRevealedMap.get(pageNumber));
                    pageMaskRevealedMap.put(pageNumber, !currentRev);
                    notifyItemChanged(pageNumber - 1);
                }
            });
            binding.tvMaskModeTapHint.setOnClickListener(v -> {
                if (isMaskMode) {
                    boolean currentRev = pageMaskRevealedMap.containsKey(pageNumber) && Boolean.TRUE.equals(pageMaskRevealedMap.get(pageNumber));
                    pageMaskRevealedMap.put(pageNumber, !currentRev);
                    notifyItemChanged(pageNumber - 1);
                }
            });

            // Async load Ayahs for this page
            List<QuranAyahEntity> cachedAyahs = pageAyahsCache.get(pageNumber);
            if (cachedAyahs != null && !cachedAyahs.isEmpty()) {
                renderAyahs(pageInfo, cachedAyahs);
            } else {
                binding.tvPageArabicText.setText(isBn ? "পৃষ্ঠার আয়াত লোড হচ্ছে..." : "Loading page verses...");
                backgroundExecutor.execute(() -> {
                    List<QuranAyahEntity> ayahs = ayahDao.getAyahsForPageSync(pageNumber);
                    if (ayahs == null || ayahs.isEmpty()) {
                        // Fallback by range query
                        ayahs = ayahDao.getAyahsInRangeSync(
                                pageInfo.startSurah, pageInfo.startAyah,
                                pageInfo.endSurah, pageInfo.endAyah
                        );
                    }

                    List<QuranAyahEntity> finalAyahs = ayahs;
                    mainHandler.post(() -> {
                        if (finalAyahs != null && !finalAyahs.isEmpty()) {
                            pageAyahsCache.put(pageNumber, finalAyahs);
                            renderAyahs(pageInfo, finalAyahs);
                        } else {
                            binding.tvPageArabicText.setText(isBn
                                    ? "এই পৃষ্ঠার আয়াত অফলাইনে সংরক্ষিত নেই। উপরে ডাউনলোড বাটনে ক্লিক করুন।"
                                    : "Ayahs for this page are not yet downloaded offline. Tap download above.");
                        }
                    });
                });
            }
        }

        private void renderAyahs(QuranPageDataHelper.PageInfo pageInfo, List<QuranAyahEntity> ayahs) {
            if (ayahs == null || ayahs.isEmpty()) return;
            boolean isBn = LocaleManager.isBengali(context);

            QuranAyahEntity firstAyah = ayahs.get(0);
            int primarySurahNum = firstAyah.getSurahNumber();

            // Fetch Surah Meta
            backgroundExecutor.execute(() -> {
                QuranSurahEntity surah = surahDao.getSurahByNumberSync(primarySurahNum);
                mainHandler.post(() -> {
                    if (surah != null) {
                        binding.tvPageHeaderSurah.setText(isBn ? surah.getNameBengali() : surah.getNameEnglish());

                        // Check if a Surah starts on this page
                        boolean isSurahStart = (pageInfo.startAyah == 1 || firstAyah.getAyahNumber() == 1);
                        if (isSurahStart) {
                            binding.layoutSurahHeaderBanner.setVisibility(View.VISIBLE);
                            binding.tvSurahBannerArabic.setText("سورة " + surah.getNameArabic());
                            String meta = (isBn ? surah.getNameBengali() : surah.getNameEnglish())
                                    + " • " + (surah.getRevelationType() != null ? surah.getRevelationType() : "")
                                    + " • " + (isBn ? BengaliNumberUtil.toBengali(surah.getNumberOfAyahs()) : surah.getNumberOfAyahs()) + (isBn ? " আয়াত" : " Verses");
                            binding.tvSurahBannerMeta.setText(meta);

                            if (QuranBismillahHelper.shouldShowBismillahHeader(primarySurahNum)) {
                                binding.tvBismillahBanner.setVisibility(View.VISIBLE);
                                binding.tvBismillahBanner.setText(QuranBismillahHelper.BISMILLAH_ARABIC);
                            } else {
                                binding.tvBismillahBanner.setVisibility(View.GONE);
                            }
                        } else {
                            binding.layoutSurahHeaderBanner.setVisibility(View.GONE);
                            binding.tvBismillahBanner.setVisibility(View.GONE);
                        }
                    }
                });
            });

            // Build Spannable String for 15-Line Mushaf continuous flow
            StringBuilder sb = new StringBuilder();
            Map<Integer, int[]> spanRangeMap = new HashMap<>();

            for (int i = 0; i < ayahs.size(); i++) {
                QuranAyahEntity ayah = ayahs.get(i);
                int startPos = sb.length();

                String rawArabic = ayah.getTextArabic() != null ? ayah.getTextArabic().trim() : "";
                String cleanArabic = QuranRepository.sanitizeArabicVerse(ayah.getSurahNumber(), ayah.getAyahNumber(), rawArabic);

                sb.append(cleanArabic);
                sb.append(" \u06DD");
                sb.append(BengaliNumberUtil.toArabicDigits(ayah.getAyahNumber()));
                sb.append(" ");

                int endPos = sb.length();
                spanRangeMap.put(i, new int[]{startPos, endPos});
            }

            SpannableString spannable = new SpannableString(sb.toString());

            for (int i = 0; i < ayahs.size(); i++) {
                final QuranAyahEntity ayah = ayahs.get(i);
                int[] range = spanRangeMap.get(i);
                if (range == null) continue;

                int s = range[0];
                int e = range[1];

                // Check if marked as Mistake / Mutashabihat
                boolean isMistake = hifzManager.isAyahMarkedMistake(ayah.getSurahNumber(), ayah.getAyahNumber());
                if (isMistake && !isMaskMode) {
                    spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.accent_gold)),
                            s, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // Interactive ClickableSpan
                if (!isMaskMode) {
                    spannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            if (interactListener != null) {
                                interactListener.onAyahClick(ayah, pageInfo.pageNumber);
                            }
                        }

                        @Override
                        public void updateDrawState(@NonNull TextPaint ds) {
                            ds.setUnderlineText(false);
                        }
                    }, s, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            binding.tvPageArabicText.setText(spannable);
            if (!isMaskMode) {
                binding.tvPageArabicText.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        private int getManzilForPage(int page) {
            if (page <= 86) return 1;
            if (page <= 176) return 2;
            if (page <= 261) return 3;
            if (page <= 366) return 4;
            if (page <= 452) return 5;
            if (page <= 534) return 6;
            return 7;
        }
    }
}
