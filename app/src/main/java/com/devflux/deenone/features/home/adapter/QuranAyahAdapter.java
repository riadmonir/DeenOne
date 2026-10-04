package com.devflux.deenone.features.home.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranBismillahHelper;
import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.core.quran.QuranSettingsManager;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.databinding.ItemQuranAyahBinding;
import com.devflux.deenone.databinding.ItemQuranBismillahCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.QuranTransliterationUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * High-performance, 60 FPS Quran Ayah RecyclerView Adapter for DeenOne.
 * 
 * Features:
 * - Position 0: Canonical Bismillah Header Card for Surahs 2 to 114 (except Surah 9).
 * - Position 1..N: Ayah 1 to Ayah N strictly mapped by (surahNumber, ayahNumber).
 * - Dynamic font scaling: Arabic font & Translation/Pronunciation font size.
 * - Dynamic visibility: Pronunciation toggle & Translation/Tafsir toggle.
 * - Expandable Brief Tafsir box on user request.
 * - Rule 7 Compliance: STRICT ZERO touch animation on CardViews. Touch animation ONLY on Buttons.
 * - Rule 5 Compliance: Dual-Language Clean Mode without mixed brackets.
 */
public class QuranAyahAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_BISMILLAH = 0;
    public static final int VIEW_TYPE_AYAH = 1;

    public interface OnAyahActionListener {
        void onBookmarkToggle(QuranAyahEntity ayah);
        void onPlayAyah(QuranAyahEntity ayah, int position);
        void onWordByWord(QuranAyahEntity ayah, int position);
    }

    private List<QuranAyahEntity> ayahList = new ArrayList<>();
    private final Set<Integer> expandedAyahs = new HashSet<>();
    private int currentSurahNumber = 1;
    private float arabicFontSizeSp = QuranSettingsManager.DEFAULT_ARABIC_FONT_SIZE;
    private float translationFontSizeSp = QuranSettingsManager.DEFAULT_TRANSLATION_FONT_SIZE;
    private boolean showPronunciation = true;
    private boolean showTranslation = true;
    private QuranCdnAudioHelper.Reciter currentReciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
    private int activePlayingAyahNumber = -1;
    private boolean isPlaying = false;
    private final OnAyahActionListener listener;

    public QuranAyahAdapter(OnAyahActionListener listener) {
        this.listener = listener;
    }

    public void setAyahs(List<QuranAyahEntity> list) {
        this.ayahList = list != null ? list : new ArrayList<>();
        if (!this.ayahList.isEmpty()) {
            this.currentSurahNumber = this.ayahList.get(0).getSurahNumber();
        }
        this.expandedAyahs.clear();
        notifyDataSetChanged();
    }

    public void setSurahNumber(int surahNumber) {
        this.currentSurahNumber = surahNumber;
    }

    public boolean hasBismillahHeader() {
        return QuranBismillahHelper.shouldShowBismillahHeader(currentSurahNumber);
    }

    public QuranAyahEntity getAyahAt(int position) {
        if (hasBismillahHeader()) {
            if (position == 0) {
                return QuranBismillahHelper.createBismillahEntity(currentSurahNumber);
            }
            int realIndex = position - 1;
            if (realIndex >= 0 && realIndex < ayahList.size()) {
                return ayahList.get(realIndex);
            }
        } else {
            if (position >= 0 && position < ayahList.size()) {
                return ayahList.get(position);
            }
        }
        return null;
    }

    public int getPositionForAyahNumber(int ayahNumber) {
        if (ayahNumber == 0 && hasBismillahHeader()) {
            return 0;
        }
        if (hasBismillahHeader()) {
            return ayahNumber; // e.g. Ayah 1 is at index 1
        } else {
            return Math.max(0, ayahNumber - 1); // e.g. Ayah 1 is at index 0
        }
    }

    public void setArabicFontSize(float sizeSp) {
        this.arabicFontSizeSp = sizeSp;
        notifyDataSetChanged();
    }

    public void setTranslationFontSize(float sizeSp) {
        this.translationFontSizeSp = sizeSp;
        notifyDataSetChanged();
    }

    public void setShowPronunciation(boolean show) {
        this.showPronunciation = show;
        notifyDataSetChanged();
    }

    public void setShowTranslation(boolean show) {
        this.showTranslation = show;
        notifyDataSetChanged();
    }

    public void setReciter(QuranCdnAudioHelper.Reciter reciter) {
        this.currentReciter = reciter;
    }

    public void setActivePlayingAyah(int ayahNumber, boolean playing) {
        this.activePlayingAyahNumber = ayahNumber;
        this.isPlaying = playing;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasBismillahHeader() && position == 0) {
            return VIEW_TYPE_BISMILLAH;
        }
        return VIEW_TYPE_AYAH;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_BISMILLAH) {
            ItemQuranBismillahCardBinding binding = ItemQuranBismillahCardBinding.inflate(inflater, parent, false);
            return new BismillahViewHolder(binding);
        } else {
            ItemQuranAyahBinding binding = ItemQuranAyahBinding.inflate(inflater, parent, false);
            return new AyahViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BismillahViewHolder) {
            ((BismillahViewHolder) holder).bind();
        } else if (holder instanceof AyahViewHolder) {
            int ayahIndex = hasBismillahHeader() ? position - 1 : position;
            if (ayahIndex >= 0 && ayahIndex < ayahList.size()) {
                ((AyahViewHolder) holder).bind(ayahList.get(ayahIndex));
            }
        }
    }

    @Override
    public int getItemCount() {
        int baseCount = ayahList.size();
        if (baseCount == 0) return 0;
        return hasBismillahHeader() ? (baseCount + 1) : baseCount;
    }

    // ==========================================
    // 1. Dedicated Bismillah ViewHolder
    // ==========================================
    class BismillahViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranBismillahCardBinding binding;

        public BismillahViewHolder(@NonNull ItemQuranBismillahCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Rule 7: STRICT ZERO touch animation on CardViews. Touch animation ONLY on Buttons.
            TouchAnimationUtil.attachTouchSpring(binding.btnPlayBismillahAudio);
            TouchAnimationUtil.attachTouchSpring(binding.btnBismillahWordByWord);
            TouchAnimationUtil.attachTouchSpring(binding.btnCopyBismillah);
            TouchAnimationUtil.attachTouchSpring(binding.btnShareBismillah);
            TouchAnimationUtil.attachTouchSpring(binding.btnToggleBismillahTafsir);
        }

        public void bind() {
            Context context = binding.getRoot().getContext();
            boolean isBengali = LocaleManager.isBengali(context);
            boolean isBismillahActive = (activePlayingAyahNumber == 0 || (currentSurahNumber == 1 && activePlayingAyahNumber == 1));

            binding.tvBismillahBadge.setText(isBengali ? "বিসমিল্লাহ" : "Bismillah");
            binding.tvBismillahArabic.setText(QuranBismillahHelper.BISMILLAH_ARABIC);
            binding.tvBismillahArabic.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicFontSizeSp);

            // Pronunciation (Visibility controlled by Settings)
            if (showPronunciation) {
                binding.tvBismillahPronunciation.setVisibility(View.VISIBLE);
                String pronunciation = isBengali
                        ? ("উচ্চারণ: " + QuranBismillahHelper.BISMILLAH_PRONUNCIATION_BN)
                        : ("Pronunciation: " + QuranBismillahHelper.BISMILLAH_PRONUNCIATION_EN);
                binding.tvBismillahPronunciation.setText(pronunciation);
                binding.tvBismillahPronunciation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
            } else {
                binding.tvBismillahPronunciation.setVisibility(View.GONE);
            }

            // Tafsir / Translation Toggle & Box (Visibility controlled by Settings)
            if (showTranslation) {
                binding.btnToggleBismillahTafsir.setVisibility(View.VISIBLE);
                int bismillahKey = currentSurahNumber * 1000 + 0;
                boolean isBismillahExpanded = expandedAyahs.contains(bismillahKey);
                binding.tvToggleBismillahTafsirText.setText(isBengali ? "সংক্ষিপ্ত তাফসির দেখুন" : "View Brief Tafsir");
                binding.layoutBismillahTafsirBox.setVisibility(isBismillahExpanded ? View.VISIBLE : View.GONE);

                // Translation text
                if (isBengali) {
                    binding.tvBismillahBengaliTranslation.setVisibility(View.VISIBLE);
                    binding.tvBismillahBengaliTranslation.setText("অর্থ: " + QuranBismillahHelper.BISMILLAH_TRANSLATION_BN);
                    binding.tvBismillahBengaliTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
                    binding.tvBismillahEnglishTranslation.setVisibility(View.GONE);
                } else {
                    binding.tvBismillahEnglishTranslation.setVisibility(View.VISIBLE);
                    binding.tvBismillahEnglishTranslation.setText("Translation: " + QuranBismillahHelper.BISMILLAH_TRANSLATION_EN);
                    binding.tvBismillahEnglishTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
                    binding.tvBismillahBengaliTranslation.setVisibility(View.GONE);
                }

                binding.btnToggleBismillahTafsir.setOnClickListener(v -> {
                    int key = currentSurahNumber * 1000 + 0;
                    if (expandedAyahs.contains(key)) {
                        expandedAyahs.remove(key);
                        binding.layoutBismillahTafsirBox.setVisibility(View.GONE);
                    } else {
                        expandedAyahs.add(key);
                        binding.layoutBismillahTafsirBox.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                binding.btnToggleBismillahTafsir.setVisibility(View.GONE);
                binding.layoutBismillahTafsirBox.setVisibility(View.GONE);
                binding.btnToggleBismillahTafsir.setOnClickListener(null);
            }

            // Active Highlight
            if (isBismillahActive) {
                binding.cardBismillahItem.setStrokeColor(ContextCompat.getColor(context, R.color.accent_mint));
                binding.cardBismillahItem.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.8f, context.getResources().getDisplayMetrics()));
                binding.btnPlayBismillahAudio.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            } else {
                binding.cardBismillahItem.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
                binding.cardBismillahItem.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, context.getResources().getDisplayMetrics()));
                binding.btnPlayBismillahAudio.setImageResource(R.drawable.ic_audio_play);
            }

            // Actions
            binding.btnPlayBismillahAudio.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayAyah(QuranBismillahHelper.createBismillahEntity(currentSurahNumber), 0);
                }
            });

            binding.btnBismillahWordByWord.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onWordByWord(QuranBismillahHelper.createBismillahEntity(currentSurahNumber), 0);
                }
            });

            binding.btnCopyBismillah.setOnClickListener(v -> {
                String bismillahPronun = isBengali ? QuranBismillahHelper.BISMILLAH_PRONUNCIATION_BN : QuranBismillahHelper.BISMILLAH_PRONUNCIATION_EN;
                String bismillahTrans = isBengali ? QuranBismillahHelper.BISMILLAH_TRANSLATION_BN : QuranBismillahHelper.BISMILLAH_TRANSLATION_EN;

                StringBuilder copyBuilder = new StringBuilder();
                copyBuilder.append(QuranBismillahHelper.BISMILLAH_ARABIC).append("\n");
                copyBuilder.append(isBengali ? "উচ্চারণ: " : "Pronunciation: ").append(bismillahPronun).append("\n");
                copyBuilder.append(isBengali ? "অর্থ: " : "Translation: ").append(bismillahTrans);

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(isBengali ? "বিসমিল্লাহ" : "Bismillah", copyBuilder.toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, isBengali ? "বিসমিল্লাহ কপি করা হয়েছে" : "Bismillah copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            binding.btnShareBismillah.setOnClickListener(v -> {
                String bismillahPronun = isBengali ? QuranBismillahHelper.BISMILLAH_PRONUNCIATION_BN : QuranBismillahHelper.BISMILLAH_PRONUNCIATION_EN;
                String bismillahTrans = isBengali ? QuranBismillahHelper.BISMILLAH_TRANSLATION_BN : QuranBismillahHelper.BISMILLAH_TRANSLATION_EN;

                StringBuilder shareBuilder = new StringBuilder();
                shareBuilder.append(QuranBismillahHelper.BISMILLAH_ARABIC).append("\n\n");
                shareBuilder.append(isBengali ? "উচ্চারণ: " : "Pronunciation: ").append(bismillahPronun).append("\n\n");
                shareBuilder.append(isBengali ? "অর্থ: " : "Translation: ").append(bismillahTrans).append("\n\n");
                shareBuilder.append(isBengali ? "— পবিত্র কুরআন (দ্বীনওয়ান)" : "— Holy Quran (DeenOne)");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBuilder.toString());
                context.startActivity(Intent.createChooser(intent, isBengali ? "শেয়ার করুন" : "Share"));
            });
        }
    }

    // ==========================================
    // 2. Ayah Item ViewHolder
    // ==========================================
    class AyahViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranAyahBinding binding;

        public AyahViewHolder(@NonNull ItemQuranAyahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Rule 7: STRICT ZERO touch animation on CardViews. Touch animation ONLY on Buttons.
            TouchAnimationUtil.attachTouchSpring(binding.btnPlayAyahAudio);
            TouchAnimationUtil.attachTouchSpring(binding.btnAyahWordByWord);
            TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkAyah);
            TouchAnimationUtil.attachTouchSpring(binding.btnCopyAyahItem);
            TouchAnimationUtil.attachTouchSpring(binding.btnShareAyahItem);
            TouchAnimationUtil.attachTouchSpring(binding.btnToggleTafsir);
        }

        public void bind(QuranAyahEntity ayah) {
            Context context = binding.getRoot().getContext();
            boolean isBengali = LocaleManager.isBengali(context);
            boolean isThisAyahActive = (ayah.getAyahNumber() == activePlayingAyahNumber);

            binding.tvAyahNumberBadge.setText(isBengali ? ("আয়াত " + BengaliNumberUtil.toBengali(ayah.getAyahNumber())) : ("Ayah " + ayah.getAyahNumber()));

            // Authentic Arabic Uthmani text with Verse End Rosette Delimiter ۝
            String rawArabic = ayah.getTextArabic() != null ? ayah.getTextArabic().trim() : "";
            String cleanArabic = com.devflux.deenone.data.repository.QuranRepository.sanitizeArabicVerse(
                    ayah.getSurahNumber(), ayah.getAyahNumber(), rawArabic
            );
            String displayArabic = cleanArabic;
            if (!displayArabic.isEmpty() && !displayArabic.contains("\u06DD")) {
                displayArabic = displayArabic + " \u06DD" + BengaliNumberUtil.toArabicDigits(ayah.getAyahNumber());
            }
            binding.tvAyahArabicText.setText(displayArabic);
            binding.tvAyahArabicText.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicFontSizeSp);

            // Transliteration / Pronunciation (Line 2: under Arabic text - controlled by Settings)
            final String pronunciation = QuranTransliterationUtil.getPronunciation(cleanArabic, ayah.getTransliterationBengali(), isBengali);
            if (showPronunciation) {
                if (pronunciation != null && !pronunciation.trim().isEmpty()) {
                    binding.tvAyahPronunciation.setVisibility(View.VISIBLE);
                    binding.tvAyahPronunciation.setText((isBengali ? "উচ্চারণ: " : "Pronunciation: ") + pronunciation);
                    binding.tvAyahPronunciation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
                } else {
                    binding.tvAyahPronunciation.setVisibility(View.GONE);
                }
            } else {
                binding.tvAyahPronunciation.setVisibility(View.GONE);
            }

            // Translation & Expandable Tafsir Box (Line 3 - controlled by Settings)
            int ayahKey = ayah.getSurahNumber() * 1000 + ayah.getAyahNumber();
            boolean isExpanded = expandedAyahs.contains(ayahKey);
            boolean hasTranslation = false;

            if (showTranslation) {
                if (isBengali) {
                    String bnTranslation = ayah.getTranslationBengali();
                    if (bnTranslation != null && !bnTranslation.trim().isEmpty()) {
                        hasTranslation = true;
                        binding.tvAyahBengaliTranslation.setVisibility(View.VISIBLE);
                        binding.tvAyahBengaliTranslation.setText("অর্থ: " + bnTranslation.trim());
                        binding.tvAyahBengaliTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
                    } else {
                        binding.tvAyahBengaliTranslation.setVisibility(View.GONE);
                    }
                    binding.tvAyahEnglishTranslation.setVisibility(View.GONE);
                } else {
                    String enTranslation = ayah.getTranslationEnglish();
                    if (enTranslation != null && !enTranslation.trim().isEmpty()) {
                        hasTranslation = true;
                        binding.tvAyahEnglishTranslation.setVisibility(View.VISIBLE);
                        binding.tvAyahEnglishTranslation.setText("Translation: " + enTranslation.trim());
                        binding.tvAyahEnglishTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationFontSizeSp);
                    } else {
                        binding.tvAyahEnglishTranslation.setVisibility(View.GONE);
                    }
                    binding.tvAyahBengaliTranslation.setVisibility(View.GONE);
                }

                if (hasTranslation) {
                    binding.btnToggleTafsir.setVisibility(View.VISIBLE);
                    binding.tvToggleTafsirText.setText(isBengali ? "সংক্ষিপ্ত তাফসির দেখুন" : "View Brief Tafsir");
                    binding.layoutTafsirBox.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                    binding.btnToggleTafsir.setOnClickListener(v -> {
                        int key = ayah.getSurahNumber() * 1000 + ayah.getAyahNumber();
                        if (expandedAyahs.contains(key)) {
                            expandedAyahs.remove(key);
                            binding.layoutTafsirBox.setVisibility(View.GONE);
                        } else {
                            expandedAyahs.add(key);
                            binding.layoutTafsirBox.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    binding.btnToggleTafsir.setVisibility(View.GONE);
                    binding.layoutTafsirBox.setVisibility(View.GONE);
                    binding.btnToggleTafsir.setOnClickListener(null);
                }
            } else {
                binding.btnToggleTafsir.setVisibility(View.GONE);
                binding.layoutTafsirBox.setVisibility(View.GONE);
                binding.btnToggleTafsir.setOnClickListener(null);
            }

            binding.btnBookmarkAyah.setImageResource(ayah.isBookmarked() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
            binding.btnBookmarkAyah.setColorFilter(ContextCompat.getColor(context, ayah.isBookmarked() ? R.color.accent_mint : R.color.text_muted));

            // Active Playing Highlight & State
            if (isThisAyahActive) {
                binding.cardAyahItem.setStrokeColor(ContextCompat.getColor(context, R.color.accent_mint));
                binding.cardAyahItem.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.8f, context.getResources().getDisplayMetrics()));
                binding.cardAyahItem.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_card));
                binding.btnPlayAyahAudio.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
                binding.btnPlayAyahAudio.setColorFilter(ContextCompat.getColor(context, R.color.accent_mint));
            } else {
                binding.cardAyahItem.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
                binding.cardAyahItem.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, context.getResources().getDisplayMetrics()));
                binding.cardAyahItem.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_card));
                binding.btnPlayAyahAudio.setImageResource(R.drawable.ic_audio_play);
                binding.btnPlayAyahAudio.setColorFilter(ContextCompat.getColor(context, R.color.accent_mint));
            }

            // Play Ayah Audio
            binding.btnPlayAyahAudio.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayAyah(ayah, getAdapterPosition());
                }
            });

            // Word by Word Translation Modal
            binding.btnAyahWordByWord.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onWordByWord(ayah, getAdapterPosition());
                }
            });

            // Bookmark Toggle
            binding.btnBookmarkAyah.setOnClickListener(v -> {
                ayah.setBookmarked(!ayah.isBookmarked());
                notifyItemChanged(getAdapterPosition());
                if (listener != null) {
                    listener.onBookmarkToggle(ayah);
                }
                Toast.makeText(context, isBengali ? (ayah.isBookmarked() ? "বুকমার্ক যুক্ত করা হয়েছে" : "বুকমার্ক সরানো হয়েছে") : (ayah.isBookmarked() ? "Bookmark added" : "Bookmark removed"), Toast.LENGTH_SHORT).show();
            });

            // Copy Ayah
            binding.btnCopyAyahItem.setOnClickListener(v -> {
                String surahAyahRef = isBengali
                        ? ("[সূরা " + BengaliNumberUtil.toBengali(ayah.getSurahNumber()) + ", আয়াত " + BengaliNumberUtil.toBengali(ayah.getAyahNumber()) + "]")
                        : ("[Surah " + ayah.getSurahNumber() + ", Ayah " + ayah.getAyahNumber() + "]");
                
                StringBuilder copyBuilder = new StringBuilder();
                if (cleanArabic != null && !cleanArabic.trim().isEmpty()) {
                    copyBuilder.append(cleanArabic.trim()).append("\n");
                }
                if (pronunciation != null && !pronunciation.trim().isEmpty()) {
                    copyBuilder.append(isBengali ? "উচ্চারণ: " : "Pronunciation: ")
                            .append(pronunciation.trim()).append("\n");
                }
                if (isBengali) {
                    if (ayah.getTranslationBengali() != null && !ayah.getTranslationBengali().trim().isEmpty()) {
                        copyBuilder.append("অর্থ: ").append(ayah.getTranslationBengali().trim()).append("\n");
                    }
                } else {
                    if (ayah.getTranslationEnglish() != null && !ayah.getTranslationEnglish().trim().isEmpty()) {
                        copyBuilder.append("Translation: ").append(ayah.getTranslationEnglish().trim()).append("\n");
                    }
                }
                copyBuilder.append(surahAyahRef);

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(isBengali ? "পবিত্র কুরআন" : "Holy Quran", copyBuilder.toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, isBengali ? "আয়াত কপি করা হয়েছে" : "Ayah copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            // Share Ayah
            binding.btnShareAyahItem.setOnClickListener(v -> {
                StringBuilder shareBuilder = new StringBuilder();
                if (cleanArabic != null && !cleanArabic.trim().isEmpty()) {
                    shareBuilder.append(cleanArabic.trim()).append("\n\n");
                }
                if (pronunciation != null && !pronunciation.trim().isEmpty()) {
                    shareBuilder.append(isBengali ? "উচ্চারণ: " : "Pronunciation: ")
                            .append(pronunciation.trim()).append("\n\n");
                }
                if (isBengali) {
                    if (ayah.getTranslationBengali() != null && !ayah.getTranslationBengali().trim().isEmpty()) {
                        shareBuilder.append("অর্থ: ").append(ayah.getTranslationBengali().trim()).append("\n\n");
                    }
                    shareBuilder.append("— সূরা ").append(BengaliNumberUtil.toBengali(ayah.getSurahNumber()))
                            .append(", আয়াত ").append(BengaliNumberUtil.toBengali(ayah.getAyahNumber())).append(" (দ্বীনওয়ান)");
                } else {
                    if (ayah.getTranslationEnglish() != null && !ayah.getTranslationEnglish().trim().isEmpty()) {
                        shareBuilder.append("Translation: ").append(ayah.getTranslationEnglish().trim()).append("\n\n");
                    }
                    shareBuilder.append("— Surah ").append(ayah.getSurahNumber())
                            .append(", Ayah ").append(ayah.getAyahNumber()).append(" (DeenOne)");
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBuilder.toString());
                context.startActivity(Intent.createChooser(intent, isBengali ? "আয়াত শেয়ার করুন" : "Share Ayah"));
            });
        }
    }
}
