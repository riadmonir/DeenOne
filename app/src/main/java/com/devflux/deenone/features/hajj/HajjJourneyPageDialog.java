package com.devflux.deenone.features.hajj;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageHajjJourneyBinding;
import com.devflux.deenone.features.hajj.adapter.HajjJourneyTimelineAdapter;
import com.devflux.deenone.features.hajj.audio.HajjJourneyAudioManager;
import com.devflux.deenone.features.hajj.data.HajjJourneyRepository;
import com.devflux.deenone.features.hajj.model.HajjJourneyStage;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class HajjJourneyPageDialog {

    private static ObjectAnimator bar1Anim;
    private static ObjectAnimator bar2Anim;
    private static ObjectAnimator bar3Anim;

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageHajjJourneyBinding binding = PageHajjJourneyBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHajjJourneyTitle.setText(isBn ? "হজ যাত্রা" : "Hajj Journey");

        // Back Button
        TouchAnimationUtil.attachTouchSpring(binding.btnBackHajjJourney);
        binding.btnBackHajjJourney.setOnClickListener(v -> dialog.dismiss());

        // Top Summary Card
        binding.tvSummaryMain.setText(isBn ? HajjJourneyRepository.SUMMARY_CARD_BN : HajjJourneyRepository.SUMMARY_CARD_EN);
        binding.tvSummaryExtended.setText(isBn ? HajjJourneyRepository.SUMMARY_EXTENDED_BN : HajjJourneyRepository.SUMMARY_EXTENDED_EN);
        binding.tvToggleSummary.setText(isBn ? "বিস্তারিত" : "Details");

        final boolean[] isSummaryExpanded = {false};
        TouchAnimationUtil.attachTouchSpring(binding.btnToggleSummary);
        binding.btnToggleSummary.setOnClickListener(v -> {
            isSummaryExpanded[0] = !isSummaryExpanded[0];
            binding.layoutSummaryExpanded.setVisibility(isSummaryExpanded[0] ? View.VISIBLE : View.GONE);
            binding.ivToggleSummaryChevron.animate()
                    .rotation(isSummaryExpanded[0] ? 180f : 0f)
                    .setDuration(250)
                    .start();
        });

        // 12 Timeline Stages RecyclerView & Automatic Background Preloader / Sync
        HajjJourneyRepository.preloadAllJourneyStages(activity);
        List<HajjJourneyStage> stages = HajjJourneyRepository.getJourneyStages(activity);
        binding.rvHajjStages.setLayoutManager(new LinearLayoutManager(activity));
        HajjJourneyTimelineAdapter adapter = new HajjJourneyTimelineAdapter(activity, stages, isBn);
        binding.rvHajjStages.setAdapter(adapter);

        // Audio Manager Integration
        HajjJourneyAudioManager audioManager = HajjJourneyAudioManager.getInstance();
        TouchAnimationUtil.attachTouchSpring(binding.btnHajjAudioFab);

        HajjJourneyAudioManager.PlaybackStateListener audioListener = isPlaying -> {
            activity.runOnUiThread(() -> {
                updateAudioFabUi(binding, isPlaying);
            });
        };

        audioManager.addListener(audioListener);

        binding.btnHajjAudioFab.setOnClickListener(v -> {
            audioManager.togglePlayPause(activity);
        });

        // Autoplay once upon entry (respecting non-interruption rule)
        audioManager.triggerAutoplayOnEntry(activity);

        // Lifecycle cleanup when dialog is dismissed
        dialog.setOnDismissListener(d -> {
            audioManager.removeListener(audioListener);
            stopWaveAnimation();
            audioManager.stopAndReleaseWithFadeOut();
        });

        dialog.show();
    }

    private static void updateAudioFabUi(PageHajjJourneyBinding binding, boolean isPlaying) {
        if (isPlaying) {
            binding.ivHajjAudioIcon.setVisibility(View.GONE);
            binding.layoutAudioWaveIndicator.setVisibility(View.VISIBLE);
            startWaveAnimation(binding);
        } else {
            binding.ivHajjAudioIcon.setVisibility(View.VISIBLE);
            binding.layoutAudioWaveIndicator.setVisibility(View.GONE);
            stopWaveAnimation();
        }
    }

    private static void startWaveAnimation(PageHajjJourneyBinding binding) {
        stopWaveAnimation();

        bar1Anim = ObjectAnimator.ofFloat(binding.waveBar1, "scaleY", 0.3f, 1.2f, 0.4f);
        bar1Anim.setDuration(600);
        bar1Anim.setRepeatCount(ValueAnimator.INFINITE);
        bar1Anim.setRepeatMode(ValueAnimator.REVERSE);
        bar1Anim.setInterpolator(new AccelerateDecelerateInterpolator());

        bar2Anim = ObjectAnimator.ofFloat(binding.waveBar2, "scaleY", 0.4f, 1.4f, 0.5f);
        bar2Anim.setDuration(450);
        bar2Anim.setRepeatCount(ValueAnimator.INFINITE);
        bar2Anim.setRepeatMode(ValueAnimator.REVERSE);
        bar2Anim.setInterpolator(new AccelerateDecelerateInterpolator());

        bar3Anim = ObjectAnimator.ofFloat(binding.waveBar3, "scaleY", 0.5f, 1.1f, 0.3f);
        bar3Anim.setDuration(700);
        bar3Anim.setRepeatCount(ValueAnimator.INFINITE);
        bar3Anim.setRepeatMode(ValueAnimator.REVERSE);
        bar3Anim.setInterpolator(new AccelerateDecelerateInterpolator());

        bar1Anim.start();
        bar2Anim.start();
        bar3Anim.start();
    }

    private static void stopWaveAnimation() {
        if (bar1Anim != null) {
            bar1Anim.cancel();
            bar1Anim = null;
        }
        if (bar2Anim != null) {
            bar2Anim.cancel();
            bar2Anim = null;
        }
        if (bar3Anim != null) {
            bar3Anim.cancel();
            bar3Anim = null;
        }
    }
}
