package com.devflux.deenone.features.kalima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.ArabicVoiceAudioEngine;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.model.SixKalimaItem;
import com.devflux.deenone.data.repository.SixKalimaRepository;
import com.devflux.deenone.databinding.BottomSheetSixKalimaBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

public class SixKalimaPageDialog {

    private static int currentPlayingKalimaNumber = -1;
    private static boolean isContinuousPlay = false;

    public static void show(@NonNull Context context) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        BottomSheetSixKalimaBinding binding =
                BottomSheetSixKalimaBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        // 1. Navigation & Theme Toggle
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseSixKalima);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnKalimaThemeToggle);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnKalimaNotification);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnKalimaContinuousPlayPause);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnKalimaPlayerNext);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnKalimaPlayerPrev);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalimaAll);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima1);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima2);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima3);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima4);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima5);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabKalima6);

        binding.btnCloseSixKalima.setOnClickListener(v -> dialog.dismiss());

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        // Localize Tab texts
        binding.tabKalimaAll.setText(isBn ? "সকল কালিমা" : "All Kalimas");
        binding.tabKalima1.setText(isBn ? "১. তাইয়্যিবাহ" : "1. Tayyibah");
        binding.tabKalima2.setText(isBn ? "২. শাহাদাত" : "2. Shahadat");
        binding.tabKalima3.setText(isBn ? "৩. তামজীদ" : "3. Tamjeed");
        binding.tabKalima4.setText(isBn ? "৪. তাওহীদ" : "4. Tawheed");
        binding.tabKalima5.setText(isBn ? "৫. ইসতিগফার" : "5. Astaghfar");
        binding.tabKalima6.setText(isBn ? "৬. রাদ্দে কুফর" : "6. Radde Kufr");

        binding.tvKalimaCountBadge.setText(isBn ? "৬টি কালিমা" : "6 Kalimas");
        binding.tvKalimaPlayerStatusTitle.setText(isBn ? "একসাথে ৬টি কালিমা শুনুন" : "Listen to all 6 Kalimas");
        binding.tvKalimaPlayerCurrentName.setText(isBn ? "১ থেকে ৬ কালিমা ধারাবাহিক তিলাওয়াত" : "Continuous recitation 1 to 6");

        int currentTheme = ThemeManager.getSavedThemeMode(context);
        if (currentTheme == ThemeManager.THEME_LIGHT) {
            binding.ivKalimaThemeIcon.setImageResource(R.drawable.ic_moon);
        } else {
            binding.ivKalimaThemeIcon.setImageResource(R.drawable.ic_sun);
        }

        binding.btnKalimaThemeToggle.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).toggleAppTheme();
                dialog.dismiss();
            }
        });

        binding.btnKalimaNotification.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showNotificationHistorySheet();
            }
        });

        // 2. Setup RecyclerView
        List<SixKalimaItem> allKalimas = SixKalimaRepository.getAllKalimas();
        final SixKalimaAdapter[] adapterHolder = new SixKalimaAdapter[1];

        SixKalimaAdapter adapter = new SixKalimaAdapter(item -> {
            playKalimaAudio(context, item, adapterHolder[0], binding, false);
        });
        adapterHolder[0] = adapter;

        binding.rvSixKalimas.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSixKalimas.setAdapter(adapter);
        adapter.setItems(allKalimas);

        // 3. Continuous Player Bar Click Listeners
        binding.btnKalimaContinuousPlayPause.setOnClickListener(v -> {
            if (ArabicVoiceAudioEngine.getInstance(context).isPlaying()) {
                stopKalimaAudio(context, adapter, binding);
            } else {
                int startNum = (currentPlayingKalimaNumber >= 1 && currentPlayingKalimaNumber <= 6) ? currentPlayingKalimaNumber : 1;
                playKalimaAudio(context, allKalimas.get(startNum - 1), adapter, binding, true);
            }
        });

        binding.btnKalimaPlayerNext.setOnClickListener(v -> {
            int nextNum = (currentPlayingKalimaNumber >= 1 && currentPlayingKalimaNumber < 6) ? currentPlayingKalimaNumber + 1 : 1;
            playKalimaAudio(context, allKalimas.get(nextNum - 1), adapter, binding, true);
        });

        binding.btnKalimaPlayerPrev.setOnClickListener(v -> {
            int prevNum = (currentPlayingKalimaNumber > 1 && currentPlayingKalimaNumber <= 6) ? currentPlayingKalimaNumber - 1 : 6;
            playKalimaAudio(context, allKalimas.get(prevNum - 1), adapter, binding, true);
        });

        // 4. Tab Pills Click Listeners
        View.OnClickListener tabListener = v -> {
            int id = v.getId();
            resetTabStyles(context, binding);
            if (id == R.id.tabKalimaAll) {
                setTabActive(context, binding.tabKalimaAll);
                adapter.setItems(allKalimas);
            } else if (id == R.id.tabKalima1) {
                setTabActive(context, binding.tabKalima1);
                showSingleKalima(adapter, 1);
            } else if (id == R.id.tabKalima2) {
                setTabActive(context, binding.tabKalima2);
                showSingleKalima(adapter, 2);
            } else if (id == R.id.tabKalima3) {
                setTabActive(context, binding.tabKalima3);
                showSingleKalima(adapter, 3);
            } else if (id == R.id.tabKalima4) {
                setTabActive(context, binding.tabKalima4);
                showSingleKalima(adapter, 4);
            } else if (id == R.id.tabKalima5) {
                setTabActive(context, binding.tabKalima5);
                showSingleKalima(adapter, 5);
            } else if (id == R.id.tabKalima6) {
                setTabActive(context, binding.tabKalima6);
                showSingleKalima(adapter, 6);
            }
        };

        binding.tabKalimaAll.setOnClickListener(tabListener);
        binding.tabKalima1.setOnClickListener(tabListener);
        binding.tabKalima2.setOnClickListener(tabListener);
        binding.tabKalima3.setOnClickListener(tabListener);
        binding.tabKalima4.setOnClickListener(tabListener);
        binding.tabKalima5.setOnClickListener(tabListener);
        binding.tabKalima6.setOnClickListener(tabListener);

        dialog.setOnDismissListener(d -> stopKalimaAudio(context, adapter, binding));
        dialog.show();
    }

    private static void showSingleKalima(SixKalimaAdapter adapter, int number) {
        SixKalimaItem item = SixKalimaRepository.getKalimaByNumber(number);
        List<SixKalimaItem> single = new ArrayList<>();
        single.add(item);
        adapter.setItems(single);
    }

    private static void playKalimaAudio(Context context,
                                        SixKalimaItem item,
                                        SixKalimaAdapter adapter,
                                        BottomSheetSixKalimaBinding binding,
                                        boolean continuous) {
        if (item == null) return;
        isContinuousPlay = continuous;

        if (currentPlayingKalimaNumber == item.getNumber() && ArabicVoiceAudioEngine.getInstance(context).isPlaying()) {
            stopKalimaAudio(context, adapter, binding);
            return;
        }

        stopKalimaAudio(context, adapter, binding);
        currentPlayingKalimaNumber = item.getNumber();

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (binding != null) {
            binding.tvKalimaPlayerStatusTitle.setText(isBn ? item.getTitleBangla() : item.getTitleEnglish());
            binding.tvKalimaPlayerCurrentName.setText(isBn ? item.getPronunciationBn() : item.getPronunciationEn());
        }

        ArabicVoiceAudioEngine.getInstance(context).playArabic(context, item.getArabicText(), "kalima_" + item.getNumber(), new ArabicVoiceAudioEngine.PlaybackCallback() {
            @Override
            public void onStart(String identifier) {
                if (adapter != null) adapter.setPlayingState(currentPlayingKalimaNumber, true);
                if (binding != null) {
                    binding.ivKalimaContinuousPlayIcon.setImageResource(R.drawable.ic_pause);
                }
            }

            @Override
            public void onDone(String identifier) {
                if (isContinuousPlay && currentPlayingKalimaNumber > 0 && currentPlayingKalimaNumber < 6) {
                    int nextNum = currentPlayingKalimaNumber + 1;
                    SixKalimaItem nextItem = SixKalimaRepository.getKalimaByNumber(nextNum);
                    playKalimaAudio(context, nextItem, adapter, binding, true);
                } else {
                    if (adapter != null) adapter.setPlayingState(currentPlayingKalimaNumber, false);
                    currentPlayingKalimaNumber = -1;
                    if (binding != null) {
                        boolean isBnLang = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
                        binding.ivKalimaContinuousPlayIcon.setImageResource(R.drawable.ic_play_arrow);
                        binding.tvKalimaPlayerStatusTitle.setText(isBnLang ? "একসাথে ৬টি কালিমা শুনুন" : "Listen to all 6 Kalimas");
                        binding.tvKalimaPlayerCurrentName.setText(isBnLang ? "১ থেকে ৬ কালিমা ধারাবাহিক তিলাওয়াত" : "Continuous recitation 1 to 6");
                    }
                }
            }

            @Override
            public void onError(String identifier, String errorReason) {
                if (adapter != null) adapter.setPlayingState(currentPlayingKalimaNumber, false);
                currentPlayingKalimaNumber = -1;
                if (binding != null) {
                    binding.ivKalimaContinuousPlayIcon.setImageResource(R.drawable.ic_play_arrow);
                }
                Toast.makeText(context, errorReason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void stopKalimaAudio(Context context, SixKalimaAdapter adapter, BottomSheetSixKalimaBinding binding) {
        if (context != null) {
            ArabicVoiceAudioEngine.getInstance(context).stop();
        }
        if (adapter != null) {
            adapter.setPlayingState(-1, false);
        }
        if (binding != null) {
            binding.ivKalimaContinuousPlayIcon.setImageResource(R.drawable.ic_play_arrow);
        }
        currentPlayingKalimaNumber = -1;
    }

    private static void resetTabStyles(Context context, BottomSheetSixKalimaBinding binding) {
        int inactiveBg = R.drawable.bg_card_secondary;
        int inactiveText = ContextCompat.getColor(context, R.color.text_secondary);

        TextView[] tabs = {
                binding.tabKalimaAll,
                binding.tabKalima1,
                binding.tabKalima2,
                binding.tabKalima3,
                binding.tabKalima4,
                binding.tabKalima5,
                binding.tabKalima6
        };

        for (TextView tab : tabs) {
            tab.setBackgroundResource(inactiveBg);
            tab.setTextColor(inactiveText);
        }
    }

    private static void setTabActive(Context context, TextView activeTab) {
        activeTab.setBackgroundResource(R.drawable.bg_card_active);
        activeTab.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
    }
}
