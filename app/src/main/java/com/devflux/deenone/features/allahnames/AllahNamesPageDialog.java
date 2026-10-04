package com.devflux.deenone.features.allahnames;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.AllahNamesAudioPlayer;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.model.AllahNameItem;
import com.devflux.deenone.data.repository.AllahNamesRepository;
import com.devflux.deenone.databinding.BottomSheetAllahNamesBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

public class AllahNamesPageDialog {

    public static void show(@NonNull Context context) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        BottomSheetAllahNamesBinding binding =
                BottomSheetAllahNamesBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        // 1. Navigation & Header
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseAllahNames);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNamesThemeToggle);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNamesNotification);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnContinuousPlayPause);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPlayerNext);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPlayerPrev);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnClearSearch);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipAllNames);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMercy);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMajesty);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipProvision);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipWisdom);

        binding.btnCloseAllahNames.setOnClickListener(v -> dialog.dismiss());

        int currentTheme = ThemeManager.getSavedThemeMode(context);
        if (currentTheme == ThemeManager.THEME_LIGHT) {
            binding.ivNamesThemeIcon.setImageResource(R.drawable.ic_moon);
        } else {
            binding.ivNamesThemeIcon.setImageResource(R.drawable.ic_sun);
        }

        binding.btnNamesThemeToggle.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).toggleAppTheme();
                dialog.dismiss();
            } else {
                int nextMode = (currentTheme == ThemeManager.THEME_DARK) ? ThemeManager.THEME_LIGHT : ThemeManager.THEME_DARK;
                ThemeManager.setThemeMode(context, nextMode);
                dialog.dismiss();
            }
        });

        binding.btnNamesNotification.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showNotificationHistorySheet();
            }
        });

        // 2. Setup RecyclerView
        AllahNamesAdapter adapter = new AllahNamesAdapter(item -> {
            AllahNamesAudioPlayer.getInstance().playName(context, item, false);
        });
        binding.rvAllahNames.setLayoutManager(new LinearLayoutManager(context));
        binding.rvAllahNames.setAdapter(adapter);

        // State holder for active category
        final String[] activeCategory = {"ALL"};

        // Initial Data Load
        List<AllahNameItem> allNames = AllahNamesRepository.getAllNames();
        adapter.setItems(allNames);
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        if (isBn) {
            binding.tvAllahNamesTitle.setText("আল্লাহর ৯৯টি গুণবাচক নাম");
            binding.tvAllahNamesSubtitle.setText("আসমাউল হুসনা • আরবি, বাংলা অর্থ ও ফজিলত");
            binding.etSearchNames.setHint("নাম, অর্থ বা ফজিলত অনুসন্ধান করুন...");
            binding.chipAllNames.setText("সবগুলো");
            binding.chipMercy.setText("রহমত ও ক্ষমা");
            binding.chipMajesty.setText("মহিমা ও পরাক্রম");
            binding.chipProvision.setText("রিজিক ও সৃষ্টি");
            binding.chipWisdom.setText("জ্ঞান ও ন্যায়বিচার");
            binding.tvTotalNamesBadge.setText(BengaliNumberUtil.toBengali(allNames.size()) + "টি নাম");
        } else {
            binding.tvAllahNamesTitle.setText("99 Beautiful Names of Allah");
            binding.tvAllahNamesSubtitle.setText("Asmaul Husna • Arabic, Meaning & Virtues");
            binding.etSearchNames.setHint("Search by name, meaning or virtue...");
            binding.chipAllNames.setText("All Names");
            binding.chipMercy.setText("Mercy & Grace");
            binding.chipMajesty.setText("Majesty & Power");
            binding.chipProvision.setText("Provision & Bounty");
            binding.chipWisdom.setText("Wisdom & Justice");
            binding.tvTotalNamesBadge.setText(allNames.size() + " Names");
        }

        // 3. Audio Player Callbacks
        AllahNamesAudioPlayer.getInstance().setCallback(new AllahNamesAudioPlayer.AudioPlayerCallback() {
            @Override
            public void onPlaybackStateChanged(boolean isPlaying, int currentNumber) {
                adapter.setPlayingState(currentNumber, isPlaying);
                binding.ivContinuousPlayIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);

                if (currentNumber > 0 && currentNumber <= allNames.size()) {
                    AllahNameItem currentItem = allNames.get(currentNumber - 1);
                    binding.tvPlayerStatusTitle.setText(currentItem.getNameArabic() + " — " + (isBn ? currentItem.getNameBengali() : currentItem.getNameEnglish()));
                    binding.tvPlayerCurrentName.setText(isBn ? currentItem.getMeaningBengali() : currentItem.getMeaningEnglish());
                } else {
                    binding.tvPlayerStatusTitle.setText(isBn ? "একসাথে সবগুলো নাম শুনুন" : "Listen to All Divine Names");
                    binding.tvPlayerCurrentName.setText(isBn ? "১ থেকে ৯৯ নাম ধারাবাহিক তিলাওয়াত" : "Continuous recitation from 1 to 99");
                }
            }

            @Override
            public void onProgressUpdate(int currentSec, int totalSec) {}

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Continuous Player Bar Click Listeners
        binding.btnContinuousPlayPause.setOnClickListener(v -> {
            if (AllahNamesAudioPlayer.getInstance().isPlaying()) {
                AllahNamesAudioPlayer.getInstance().stop();
            } else {
                int startNum = AllahNamesAudioPlayer.getInstance().getCurrentPlayingNumber();
                if (startNum < 1 || startNum > 99) startNum = 1;
                AllahNamesAudioPlayer.getInstance().playName(context, allNames.get(startNum - 1), true);
            }
        });

        binding.btnPlayerNext.setOnClickListener(v -> AllahNamesAudioPlayer.getInstance().playNext(context));
        binding.btnPlayerPrev.setOnClickListener(v -> AllahNamesAudioPlayer.getInstance().playPrevious(context));

        // 5. Search TextWatcher with Debounce
        Handler searchHandler = new Handler(Looper.getMainLooper());
        Runnable searchRunnable = () -> {
            String query = binding.etSearchNames.getText().toString().trim();
            List<AllahNameItem> filtered = AllahNamesRepository.searchNames(query);
            if (!"ALL".equals(activeCategory[0])) {
                List<AllahNameItem> catFiltered = new ArrayList<>();
                for (AllahNameItem item : filtered) {
                    if (item.getCategory().equalsIgnoreCase(activeCategory[0])) {
                        catFiltered.add(item);
                    }
                }
                adapter.setItems(catFiltered);
                binding.tvTotalNamesBadge.setText(isBn ? (BengaliNumberUtil.toBengali(catFiltered.size()) + "টি নাম") : (catFiltered.size() + " Names"));
            } else {
                adapter.setItems(filtered);
                binding.tvTotalNamesBadge.setText(isBn ? (BengaliNumberUtil.toBengali(filtered.size()) + "টি নাম") : (filtered.size() + " Names"));
            }
        };

        binding.etSearchNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btnClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearch.setOnClickListener(v -> {
            binding.etSearchNames.setText("");
            filterByCategory(context, binding, adapter, activeCategory[0]);
        });

        // 6. Filter Chips Click Listeners
        View.OnClickListener chipListener = v -> {
            int id = v.getId();
            resetChipStyles(context, binding);
            if (id == R.id.chipAllNames) {
                activeCategory[0] = "ALL";
                setChipActive(context, binding.chipAllNames);
            } else if (id == R.id.chipMercy) {
                activeCategory[0] = "MERCY";
                setChipActive(context, binding.chipMercy);
            } else if (id == R.id.chipMajesty) {
                activeCategory[0] = "MAJESTY";
                setChipActive(context, binding.chipMajesty);
            } else if (id == R.id.chipProvision) {
                activeCategory[0] = "PROVISION";
                setChipActive(context, binding.chipProvision);
            } else if (id == R.id.chipWisdom) {
                activeCategory[0] = "WISDOM";
                setChipActive(context, binding.chipWisdom);
            }
            filterByCategory(context, binding, adapter, activeCategory[0]);
        };

        binding.chipAllNames.setOnClickListener(chipListener);
        binding.chipMercy.setOnClickListener(chipListener);
        binding.chipMajesty.setOnClickListener(chipListener);
        binding.chipProvision.setOnClickListener(chipListener);
        binding.chipWisdom.setOnClickListener(chipListener);

        dialog.setOnDismissListener(d -> AllahNamesAudioPlayer.getInstance().stop());
        dialog.show();
    }

    private static void filterByCategory(Context context,
                                         BottomSheetAllahNamesBinding binding,
                                         AllahNamesAdapter adapter,
                                         String category) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String query = binding.etSearchNames.getText().toString().trim();
        List<AllahNameItem> base = AllahNamesRepository.searchNames(query);
        if ("ALL".equalsIgnoreCase(category)) {
            adapter.setItems(base);
            binding.tvTotalNamesBadge.setText(isBn ? (BengaliNumberUtil.toBengali(base.size()) + "টি নাম") : (base.size() + " Names"));
        } else {
            List<AllahNameItem> filtered = new ArrayList<>();
            for (AllahNameItem item : base) {
                if (item.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(item);
                }
            }
            adapter.setItems(filtered);
            binding.tvTotalNamesBadge.setText(isBn ? (BengaliNumberUtil.toBengali(filtered.size()) + "টি নাম") : (filtered.size() + " Names"));
        }
    }

    private static void resetChipStyles(Context context, BottomSheetAllahNamesBinding binding) {
        int inactiveBg = R.drawable.bg_card_secondary;
        int inactiveColor = ContextCompat.getColor(context, R.color.text_secondary);

        TextView[] chips = {
                binding.chipAllNames,
                binding.chipMercy,
                binding.chipMajesty,
                binding.chipProvision,
                binding.chipWisdom
        };

        for (TextView chip : chips) {
            chip.setBackgroundResource(inactiveBg);
            chip.setTextColor(inactiveColor);
        }
    }

    private static void setChipActive(Context context, TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_card_active);
        chip.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
    }
}
