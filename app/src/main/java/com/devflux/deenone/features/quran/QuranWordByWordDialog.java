package com.devflux.deenone.features.quran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranWordCacheManager;
import com.devflux.deenone.core.quran.QuranWordItem;
import com.devflux.deenone.databinding.BottomSheetQuranWordByWordBinding;
import com.devflux.deenone.databinding.ItemQuranWordCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class QuranWordByWordDialog {

    public static void show(Context context, int surahNumber, String surahNameBn, String surahNameEn, int ayahNumber) {
        if (context == null) return;

        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.DeenOneBottomSheetDialog);
        BottomSheetQuranWordByWordBinding binding = BottomSheetQuranWordByWordBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        boolean isBengali = LocaleManager.isBengali(context);
        final String currentLang = isBengali ? "bn" : "en";

        // Rule 5: Dual-Language Clean Mode (No mixed language / bracket text)
        if (isBengali) {
            binding.tvWbwHeaderTitle.setText("শব্দে শব্দে অর্থ");
            if (ayahNumber == 0) {
                binding.tvWbwSurahAyahRef.setText("বিসমিল্লাহির রাহমানির রাহিম");
            } else {
                String surahTitle = (surahNameBn != null && !surahNameBn.isEmpty()) ? surahNameBn : ("সূরা " + BengaliNumberUtil.toBengali(surahNumber));
                binding.tvWbwSurahAyahRef.setText(surahTitle + " • আয়াত " + BengaliNumberUtil.toBengali(ayahNumber));
            }
            binding.tvWbwEmptyTitle.setText("এই সূরার শব্দার্থ এখনো নামানো হয়নি।");
            binding.tvWbwEmptySubtitle.setText("একবার নামালে পুরো সূরাই অফলাইনে পড়া যাবে।");
            binding.tvDownloadWbwText.setText("শব্দার্থ নামান");
        } else {
            binding.tvWbwHeaderTitle.setText("Word by Word");
            if (ayahNumber == 0) {
                binding.tvWbwSurahAyahRef.setText("Bismillaahir-Rahmaanir-Raheem");
            } else {
                String surahTitle = (surahNameEn != null && !surahNameEn.isEmpty()) ? surahNameEn : ("Surah " + surahNumber);
                binding.tvWbwSurahAyahRef.setText(surahTitle + " • Ayah " + ayahNumber);
            }
            binding.tvWbwEmptyTitle.setText("Word meanings for this Surah have not been downloaded yet.");
            binding.tvWbwEmptySubtitle.setText("Once downloaded, the entire Surah can be read offline.");
            binding.tvDownloadWbwText.setText("Download Word Meanings");
        }

        // Rule 7: Touch animation ONLY on Buttons. Strict ZERO touch animation on CardViews.
        TouchAnimationUtil.attachTouchSpring(binding.btnDownloadWbwNow);

        Runnable showWordsView = () -> {
            List<QuranWordItem> cachedWords = QuranWordCacheManager.getAyahWords(context, surahNumber, ayahNumber, currentLang);
            if (!cachedWords.isEmpty()) {
                renderWordsToFlexbox(context, binding, cachedWords, isBengali);
            } else {
                binding.llWbwNotDownloadedContainer.setVisibility(View.VISIBLE);
                binding.nsvWbwWordsContainer.setVisibility(View.GONE);
            }
        };

        boolean isDownloaded = (ayahNumber == 0) || QuranWordCacheManager.isSurahWordDataDownloaded(context, surahNumber, currentLang);
        if (isDownloaded) {
            showWordsView.run();
        } else {
            binding.llWbwNotDownloadedContainer.setVisibility(View.VISIBLE);
            binding.nsvWbwWordsContainer.setVisibility(View.GONE);
            binding.pbDownloadWbw.setVisibility(View.GONE);
            binding.llDownloadWbwContent.setVisibility(View.VISIBLE);
        }

        binding.btnDownloadWbwNow.setOnClickListener(v -> {
            binding.pbDownloadWbw.setVisibility(View.VISIBLE);
            binding.llDownloadWbwContent.setVisibility(View.VISIBLE);
            binding.btnDownloadWbwNow.setEnabled(false);
            binding.tvDownloadWbwText.setText(isBengali ? "ডাউনলোড হচ্ছে ০%" : "Downloading 0%");

            QuranWordCacheManager.downloadSurahWords(context, surahNumber, ayahNumber, currentLang, new QuranWordCacheManager.WbwDownloadListener() {
                @Override
                public void onProgress(int progressPercent) {
                    binding.getRoot().post(() -> {
                        binding.pbDownloadWbw.setVisibility(View.VISIBLE);
                        binding.llDownloadWbwContent.setVisibility(View.VISIBLE);
                        if (isBengali) {
                            binding.tvDownloadWbwText.setText("ডাউনলোড হচ্ছে " + BengaliNumberUtil.toBengali(progressPercent) + "%");
                        } else {
                            binding.tvDownloadWbwText.setText("Downloading " + progressPercent + "%");
                        }
                    });
                }

                @Override
                public void onSuccess(int downloadedSurah, String language, List<QuranWordItem> ayahWords) {
                    binding.pbDownloadWbw.setVisibility(View.GONE);
                    binding.llDownloadWbwContent.setVisibility(View.VISIBLE);
                    binding.btnDownloadWbwNow.setEnabled(true);
                    binding.tvDownloadWbwText.setText(isBengali ? "শব্দার্থ নামান" : "Download Word Meanings");

                    if (ayahWords != null && !ayahWords.isEmpty()) {
                        renderWordsToFlexbox(context, binding, ayahWords, isBengali);
                    } else {
                        showWordsView.run();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    binding.pbDownloadWbw.setVisibility(View.GONE);
                    binding.llDownloadWbwContent.setVisibility(View.VISIBLE);
                    binding.btnDownloadWbwNow.setEnabled(true);
                    binding.tvDownloadWbwText.setText(isBengali ? "শব্দার্থ নামান" : "Download Word Meanings");
                    showWordsView.run();
                }
            });
        });

        dialog.show();
    }

    private static void renderWordsToFlexbox(Context context, BottomSheetQuranWordByWordBinding binding, List<QuranWordItem> words, boolean isBengali) {
        if (context == null || binding == null || words == null) return;
        binding.fblWbwWordCards.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);

        for (QuranWordItem word : words) {
            ItemQuranWordCardBinding wordBinding = ItemQuranWordCardBinding.inflate(inflater, binding.fblWbwWordCards, false);

            wordBinding.tvWordArabic.setText(word.getTextArabic() != null ? word.getTextArabic().trim() : "");
            String meaning = word.getMeaning(isBengali);
            wordBinding.tvWordMeaning.setText(meaning != null ? meaning.trim() : "");

            // Rule 7: ZERO touch animation on CardViews
            binding.fblWbwWordCards.addView(wordBinding.getRoot());
        }

        binding.llWbwNotDownloadedContainer.setVisibility(View.GONE);
        binding.nsvWbwWordsContainer.setVisibility(View.VISIBLE);
    }
}
