package com.devflux.deenone.features.quran;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.BanglaQuranAyahItem;
import com.devflux.deenone.core.quran.BanglaQuranManager;
import com.devflux.deenone.core.quran.BanglaQuranTranslator;
import com.devflux.deenone.core.quran.QuranAudioCacheManager;
import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.databinding.DialogBanglaQuranReaderBinding;
import com.devflux.deenone.features.quran.adapter.BanglaQuranAyahAdapter;
import com.devflux.deenone.service.QuranAudioService;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class BanglaQuranReaderDialog {

    private final Context context;
    private final FullScreenPageDialog dialog;
    private final DialogBanglaQuranReaderBinding binding;
    private final BanglaQuranManager manager;
    private final BanglaQuranAyahAdapter adapter;

    private QuranSurahEntity surahEntity;
    private QuranParaItem paraItem;
    private boolean isParaMode = false;
    private int initialAyah = 1;

    public BanglaQuranReaderDialog(Context context, QuranSurahEntity surah, int initialAyah) {
        this.context = context;
        this.surahEntity = surah;
        this.initialAyah = initialAyah;
        this.isParaMode = false;
        this.manager = BanglaQuranManager.getInstance(context);

        this.dialog = new FullScreenPageDialog(context);
        this.binding = DialogBanglaQuranReaderBinding.inflate(LayoutInflater.from(context));
        this.dialog.setContentView(binding.getRoot());

        this.adapter = new BanglaQuranAyahAdapter(new BanglaQuranAyahAdapter.OnAyahActionListener() {
            @Override
            public void onPlayAudio(BanglaQuranAyahItem ayah, int position) {
                playAyahAudio(ayah);
            }

            @Override
            public void onBookmarkToggle(BanglaQuranAyahItem ayah, int position) {
                // Handled in adapter
            }
        });

        initViews();
    }

    public BanglaQuranReaderDialog(Context context, QuranParaItem para) {
        this.context = context;
        this.paraItem = para;
        this.isParaMode = true;
        this.manager = BanglaQuranManager.getInstance(context);

        this.dialog = new FullScreenPageDialog(context);
        this.binding = DialogBanglaQuranReaderBinding.inflate(LayoutInflater.from(context));
        this.dialog.setContentView(binding.getRoot());

        this.adapter = new BanglaQuranAyahAdapter(new BanglaQuranAyahAdapter.OnAyahActionListener() {
            @Override
            public void onPlayAudio(BanglaQuranAyahItem ayah, int position) {
                playAyahAudio(ayah);
            }

            @Override
            public void onBookmarkToggle(BanglaQuranAyahItem ayah, int position) {
                // Handled in adapter
            }
        });

        initViews();
    }

    private void initViews() {
        // Touch spring strictly on action buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseReader);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderTranslator);
        TouchAnimationUtil.attachTouchSpring(binding.btnDownloadSurahAudio);
        TouchAnimationUtil.attachTouchSpring(binding.btnOpenQuranSettings);
        TouchAnimationUtil.attachTouchSpring(binding.btnMiniPlayerPlayPause);
        TouchAnimationUtil.attachTouchSpring(binding.btnMiniPlayerClose);

        binding.btnCloseReader.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView
        binding.rvBanglaAyahs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBanglaAyahs.setHasFixedSize(true);
        binding.rvBanglaAyahs.setItemViewCacheSize(20);
        binding.rvBanglaAyahs.setAdapter(adapter);

        // Apply initial settings
        applySettingsToAdapter();
        updateHeaderTitles();

        // Translator Selector Picker
        binding.btnReaderTranslator.setOnClickListener(v -> {
            BanglaQuranTranslatorDialog.show(context, selectedTranslator -> {
                adapter.setTranslator(selectedTranslator);
                updateHeaderTitles();
            });
        });

        // Settings Dialog
        binding.btnOpenQuranSettings.setOnClickListener(v -> {
            BanglaQuranSettingsBottomSheet.show(context, this::applySettingsToAdapter);
        });

        // Download Button
        setupDownloadButton();

        // Mini Player Controls Setup
        binding.btnMiniPlayerPlayPause.setOnClickListener(v -> {
            QuranAudioService svc = QuranAudioService.getInstance();
            if (svc != null) {
                svc.togglePlayPause();
            }
        });

        binding.btnMiniPlayerClose.setOnClickListener(v -> {
            binding.cardMiniPlayer.setVisibility(View.GONE);
            QuranAudioService svc = QuranAudioService.getInstance();
            if (svc != null) {
                svc.stopPlayback();
            }
        });

        // Setup Service Playback Listener
        setupPlaybackEventListener();

        // Load Content
        loadAyahs();
    }

    private void applySettingsToAdapter() {
        adapter.setTranslator(manager.getActiveTranslator());
        adapter.setArabicShown(manager.isArabicShown());
        adapter.setArabicFontScale(manager.getArabicFontScale());
        adapter.setBanglaFontSizeSp(manager.getBanglaFontSizeSp());
    }

    private void updateHeaderTitles() {
        boolean isBn = LocaleManager.isBengali(context);
        BanglaQuranTranslator translator = manager.getActiveTranslator();
        binding.tvReaderTranslatorName.setText(translator.getShortName(isBn) + " ▾");

        if (isParaMode && paraItem != null) {
            String title = isBn ? paraItem.getBengaliName() : ("Para " + paraItem.getJuzNumber());
            String range = isBn ? paraItem.getRangeBengali() : paraItem.getRangeEnglish();
            String sub = isBn
                    ? ("পারা " + BengaliNumberUtil.toBengali(paraItem.getJuzNumber()) + " • " + range)
                    : ("Juz " + paraItem.getJuzNumber() + " • " + range);
            binding.tvReaderTitle.setText(title);
            binding.tvReaderSubtitle.setText(sub);
        } else if (surahEntity != null) {
            String title = isBn ? surahEntity.getNameBengali() : surahEntity.getNameEnglish();
            String rev = isBn ? surahEntity.getRevelationTypeBengali() : surahEntity.getRevelationTypeEnglish();
            String ayahs = isBn ? (BengaliNumberUtil.toBengali(surahEntity.getNumberOfAyahs()) + " আয়াত") : (surahEntity.getNumberOfAyahs() + " Ayahs");
            String sub = ayahs + " • " + rev;
            binding.tvReaderTitle.setText(title);
            binding.tvReaderSubtitle.setText(sub);
        }
    }

    private void setupDownloadButton() {
        if (isParaMode || surahEntity == null) {
            binding.btnDownloadSurahAudio.setVisibility(View.GONE);
            return;
        }

        binding.btnDownloadSurahAudio.setVisibility(View.VISIBLE);
        updateDownloadIconState();

        binding.btnDownloadSurahAudio.setOnClickListener(v -> {
            binding.ivDownloadSurahIcon.setVisibility(View.GONE);
            binding.pbDownloadSurahAudio.setVisibility(View.GONE);
            binding.tvDownloadSurahPercent.setVisibility(View.VISIBLE);
            binding.tvDownloadSurahPercent.setText("0%");

            QuranAudioCacheManager.downloadSurahAudio(
                    context,
                    QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY,
                    surahEntity.getNumber(),
                    surahEntity.getNumberOfAyahs(),
                    new QuranAudioCacheManager.DownloadListener() {
                        @Override
                        public void onProgress(int downloadedAyahs, int totalAyahs, int progressPercent) {
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> {
                                    binding.tvDownloadSurahPercent.setText(progressPercent + "%");
                                });
                            }
                        }

                        @Override
                        public void onComplete(int totalAyahs) {
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> {
                                    updateDownloadIconState();
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            if (context instanceof Activity) {
                                ((Activity) context).runOnUiThread(() -> {
                                    updateDownloadIconState();
                                });
                            }
                        }
                    }
            );
        });
    }

    private void updateDownloadIconState() {
        if (surahEntity == null) return;
        boolean isDownloaded = QuranAudioCacheManager.isSurahAudioDownloaded(context, QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surahEntity.getNumber(), surahEntity.getNumberOfAyahs());
        binding.tvDownloadSurahPercent.setVisibility(View.GONE);
        binding.pbDownloadSurahAudio.setVisibility(View.GONE);
        binding.ivDownloadSurahIcon.setVisibility(View.VISIBLE);
        if (isDownloaded) {
            binding.ivDownloadSurahIcon.setImageResource(R.drawable.ic_check);
        } else {
            binding.ivDownloadSurahIcon.setImageResource(R.drawable.ic_download);
        }
    }

    private void setupPlaybackEventListener() {
        QuranAudioService.setPlaybackEventListener(new QuranAudioService.OnPlaybackEventListener() {
            @Override
            public void onAyahChanged(int surahNumber, int ayahNumber, String surahNameBn, boolean isPlaying) {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        boolean isBn = LocaleManager.isBengali(context);
                        binding.cardMiniPlayer.setVisibility(View.VISIBLE);
                        binding.ivMiniPlayerPlayIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);

                        String ayahStr = isBn ? BengaliNumberUtil.toBengali(ayahNumber) : String.valueOf(ayahNumber);
                        String surahStr = isBn ? BengaliNumberUtil.toBengali(surahNumber) : String.valueOf(surahNumber);
                        binding.tvMiniPlayerTitle.setText(isBn
                                ? ("সূরা " + surahStr + " • আয়াত " + ayahStr)
                                : ("Surah " + surahStr + " • Ayah " + ayahStr));

                        // Auto scroll to active Ayah if in current surah
                        if (surahEntity != null && surahEntity.getNumber() == surahNumber) {
                            int targetPos = Math.max(0, ayahNumber - 1);
                            if (targetPos < adapter.getItemCount()) {
                                LinearLayoutManager lm = (LinearLayoutManager) binding.rvBanglaAyahs.getLayoutManager();
                                if (lm != null) {
                                    int firstVisible = lm.findFirstVisibleItemPosition();
                                    int lastVisible = lm.findLastVisibleItemPosition();
                                    if (targetPos < firstVisible || targetPos > lastVisible) {
                                        binding.rvBanglaAyahs.smoothScrollToPosition(targetPos);
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onPlaybackStateChanged(boolean isPlaying) {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        binding.ivMiniPlayerPlayIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
                    });
                }
            }

            @Override
            public void onPlaybackStopped() {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        binding.cardMiniPlayer.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onProgressUpdate(long currentPositionMs, long durationMs) {
                // Not used in standard mini player
            }
        });
    }

    private void loadAyahs() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
        binding.rvBanglaAyahs.setVisibility(View.GONE);

        if (isParaMode && paraItem != null) {
            manager.getParaAyahs(paraItem.getJuzNumber(), ayahs -> {
                binding.layoutLoading.setVisibility(View.GONE);
                binding.rvBanglaAyahs.setVisibility(View.VISIBLE);
                adapter.setAyahs(ayahs);
            });
        } else if (surahEntity != null) {
            manager.getSurahAyahs(surahEntity.getNumber(), ayahs -> {
                binding.layoutLoading.setVisibility(View.GONE);
                binding.rvBanglaAyahs.setVisibility(View.VISIBLE);
                adapter.setAyahs(ayahs);

                if (initialAyah > 1 && ayahs != null && initialAyah <= ayahs.size()) {
                    binding.rvBanglaAyahs.post(() -> {
                        LinearLayoutManager lm = (LinearLayoutManager) binding.rvBanglaAyahs.getLayoutManager();
                        if (lm != null) {
                            lm.scrollToPositionWithOffset(initialAyah - 1, 0);
                        }
                    });
                }
            });
        }
    }

    private void playAyahAudio(BanglaQuranAyahItem ayah) {
        boolean isBn = LocaleManager.isBengali(context);
        binding.cardMiniPlayer.setVisibility(View.VISIBLE);

        String ayahNumStr = isBn ? BengaliNumberUtil.toBengali(ayah.getAyahNumber()) : String.valueOf(ayah.getAyahNumber());
        String surahNumStr = isBn ? BengaliNumberUtil.toBengali(ayah.getSurahNumber()) : String.valueOf(ayah.getSurahNumber());

        binding.tvMiniPlayerTitle.setText(isBn
                ? ("সূরা " + surahNumStr + " • আয়াত " + ayahNumStr)
                : ("Surah " + surahNumStr + " • Ayah " + ayahNumStr));

        Intent svcIntent = new Intent(context, QuranAudioService.class);
        svcIntent.setAction(QuranAudioService.ACTION_PLAY_AYAH);
        svcIntent.putExtra(QuranAudioService.EXTRA_SURAH_NUMBER, ayah.getSurahNumber());
        svcIntent.putExtra(QuranAudioService.EXTRA_AYAH_NUMBER, ayah.getAyahNumber());
        if (surahEntity != null) {
            svcIntent.putExtra(QuranAudioService.EXTRA_TOTAL_AYAHS, surahEntity.getNumberOfAyahs());
            svcIntent.putExtra(QuranAudioService.EXTRA_SURAH_NAME_EN, surahEntity.getNameEnglish());
            svcIntent.putExtra(QuranAudioService.EXTRA_SURAH_NAME_BN, surahEntity.getNameBengali());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(svcIntent);
        } else {
            context.startService(svcIntent);
        }
    }

    public void show() {
        dialog.show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
    }
}
