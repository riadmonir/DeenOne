package com.devflux.deenone.features.audio.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetAudioPlayerModalBinding;
import com.devflux.deenone.features.audio.model.IslamicAudioItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

/**
 * AudioPlayerModalDialog — Ported directly from Flutter AudioPlayerModal with album art,
 * 10s skip/rewind, live seeking, playback speed control, sleep timer, and share.
 */
public class AudioPlayerModalDialog {

  private static FullScreenPageDialog activeDialog;

  public static void show(@NonNull Context context, @NonNull IslamicAudioItem item) {
    if (activeDialog != null && activeDialog.isShowing()) {
      activeDialog.dismiss();
    }

    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    activeDialog = dialog;

    BottomSheetAudioPlayerModalBinding binding = BottomSheetAudioPlayerModalBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    // Ensure playback starts immediately in foreground service
    IslamicAudioPlayerService.startPlayback(context, item);
    IslamicAudioPlayerService playerService = IslamicAudioPlayerService.getInstance();
    playerService.init(context);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Bind Basic Metadata
    String catName = item.getCategoryName() != null ? item.getCategoryName() : "";
    binding.tvAudioModalCategoryBadge.setText(item.getCategoryEmoji() + " " + catName);
    binding.tvAudioModalTitle.setText(item.getTitle().isEmpty() ? (isBn ? "ইসলামিক অডিও" : "Islamic Audio") : item.getTitle());
    binding.tvAudioModalSpeaker.setText(item.getSpeaker().isEmpty() ? (isBn ? "বিশ্ববিখ্যাত স্কলার" : "Islamic Scholar") : item.getSpeaker());
    binding.tvAudioModalTotalDuration.setText(formatDuration(item.getDurationSeconds()));

    Handler uiHandler = new Handler(Looper.getMainLooper());
    final boolean[] isUserSeeking = {false};

    // Periodic Progress Polling
    Runnable progressRunnable = new Runnable() {
      @Override
      public void run() {
        if (dialog.isShowing()) {
          if (playerService.isPlaying() && !isUserSeeking[0]) {
            int posSec = playerService.getCurrentPositionSeconds();
            int totalSec = playerService.getDurationSeconds();
            if (totalSec > 0) {
              binding.seekBarAudioModal.setMax(totalSec);
              binding.seekBarAudioModal.setProgress(posSec);
              binding.tvAudioModalCurrentTime.setText(formatDuration(posSec));
              binding.tvAudioModalTotalDuration.setText(formatDuration(totalSec));
            }
          }

          // Update Play / Pause Icon
          if (playerService.isLoading()) {
            binding.pbAudioModalLoading.setVisibility(View.VISIBLE);
            binding.ivAudioModalPlayIcon.setVisibility(View.GONE);
            binding.tvAudioModalStatus.setText(isBn ? "● লোড হচ্ছে..." : "● Loading...");
          } else if (playerService.isPlaying()) {
            binding.pbAudioModalLoading.setVisibility(View.GONE);
            binding.ivAudioModalPlayIcon.setVisibility(View.VISIBLE);
            binding.ivAudioModalPlayIcon.setImageResource(R.drawable.ic_pause);
            binding.tvAudioModalStatus.setText(isBn ? "● চলছে" : "● Playing");
          } else {
            binding.pbAudioModalLoading.setVisibility(View.GONE);
            binding.ivAudioModalPlayIcon.setVisibility(View.VISIBLE);
            binding.ivAudioModalPlayIcon.setImageResource(R.drawable.ic_play_arrow);
            binding.tvAudioModalStatus.setText(isBn ? "● স্থগিত আছে" : "● Paused");
          }

          uiHandler.postDelayed(this, 500);
        }
      }
    };

    uiHandler.post(progressRunnable);

    // Play/Pause Action
    binding.btnAudioModalPlayPause.setOnClickListener(v -> {
      if (playerService.isPlaying()) {
        playerService.pause();
      } else {
        playerService.playItem(item);
      }
    });

    // 10s Rewind
    binding.btnAudioModalRewind10.setOnClickListener(v -> {
      int cur = playerService.getCurrentPositionSeconds();
      playerService.seekTo(Math.max(0, cur - 10));
    });

    // 10s Forward
    binding.btnAudioModalForward10.setOnClickListener(v -> {
      int cur = playerService.getCurrentPositionSeconds();
      int total = playerService.getDurationSeconds();
      playerService.seekTo(Math.min(total, cur + 10));
    });

    // SeekBar Scrubber
    binding.seekBarAudioModal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          binding.tvAudioModalCurrentTime.setText(formatDuration(progress));
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        isUserSeeking[0] = true;
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        isUserSeeking[0] = false;
        playerService.seekTo(seekBar.getProgress());
      }
    });

    // Playback Speed Selector (0.75x, 1.0x, 1.25x, 1.5x, 1.75x, 2.0x)
    binding.btnAudioModalSpeed.setOnClickListener(v -> {
      String[] speeds = isBn
          ? new String[]{"০.৭৫x", "১.০x (স্বাভাবিক)", "১.২৫x", "১.৫x", "১.৭৫x", "২.০x"}
          : new String[]{"0.75x", "1.0x (Normal)", "1.25x", "1.5x", "1.75x", "2.0x"};
      float[] speedVals = {0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f};

      new MaterialAlertDialogBuilder(context)
          .setTitle(isBn ? "প্লেব্যাক গতি নির্বাচন করুন" : "Select Playback Speed")
          .setItems(speeds, (d, which) -> {
            float sel = speedVals[which];
            playerService.setPlaybackSpeed(sel);
            binding.tvAudioModalSpeedText.setText(sel + "x");
          })
          .show();
    });

    // Sleep Timer (15m, 30m, 45m, 60m)
    binding.btnAudioModalSleepTimer.setOnClickListener(v -> {
      String[] timerOptions = isBn
          ? new String[]{"১৫ মিনিট", "৩০ মিনিট", "৪৫ মিনিট", "৬০ মিনিট", "টাইমার বন্ধ করুন"}
          : new String[]{"15 Minutes", "30 Minutes", "45 Minutes", "60 Minutes", "Turn Off Timer"};
      int[] minutes = {15, 30, 45, 60, 0};

      new MaterialAlertDialogBuilder(context)
          .setTitle(isBn ? "অটো স্লিপ টাইমার" : "Auto Sleep Timer")
          .setItems(timerOptions, (d, which) -> {
            int min = minutes[which];
            if (min > 0) {
              playerService.setSleepTimer(min);
              binding.tvAudioModalSleepTimerText.setText(min + (isBn ? " মি." : " m"));
            } else {
              playerService.cancelSleepTimer();
              binding.tvAudioModalSleepTimerText.setText(isBn ? "স্লিপ টাইমার" : "Sleep Timer");
            }
          })
          .show();
    });

    // Share Audio
    binding.btnAudioModalShare.setOnClickListener(v -> {
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      shareIntent.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
      shareIntent.putExtra(Intent.EXTRA_TEXT, (isBn ? "শুনুন: " : "Listen: ") + item.getTitle() + "\n" +
          (isBn ? "বক্তা: " : "Speaker: ") + item.getSpeaker() + "\n" +
          (isBn ? "অডিও লিংক: " : "Audio Link: ") + item.getAudioUrl());
      context.startActivity(Intent.createChooser(shareIntent, isBn ? "ইসলামিক অডিও শেয়ার করুন" : "Share Islamic Audio"));
    });

    // Favorite Toggle
    final boolean[] isFav = {false};
    binding.btnAudioModalFavorite.setOnClickListener(v -> {
      isFav[0] = !isFav[0];
      binding.btnAudioModalFavorite.setImageResource(isFav[0] ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
      binding.btnAudioModalFavorite.setColorFilter(isFav[0] ? Color.parseColor("#EF4444") : Color.parseColor("#A8B7B2"), android.graphics.PorterDuff.Mode.SRC_IN);
    });

    binding.btnMinimizeAudioModal.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private static String formatDuration(int totalSeconds) {
    if (totalSeconds <= 0) return "০০:০০";
    int mins = totalSeconds / 60;
    int secs = totalSeconds % 60;
    int hours = mins / 60;
    mins = mins % 60;

    if (hours > 0) {
      return String.format(Locale.US, "%02d:%02d:%02d", hours, mins, secs);
    } else {
      return String.format(Locale.US, "%02d:%02d", mins, secs);
    }
  }
}