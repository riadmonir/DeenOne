package com.devflux.deenone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.devflux.deenone.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean hasNavigated = false;
    private Runnable navigateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Install AndroidX SplashScreen for flash-free Android 12+ cold start
        try {
            androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        } catch (Throwable ignored) {}

        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
        } catch (Throwable ignored) {}

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Safe Window Insets Handling
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutSplashRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
            );
            // Adjust crescent top margin for status bar / notch safe area
            if (binding.ivSplashCrescent.getLayoutParams() instanceof androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) {
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams lp =
                        (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) binding.ivSplashCrescent.getLayoutParams();
                lp.topMargin = systemBars.top + (int) (16 * getResources().getDisplayMetrics().density);
                binding.ivSplashCrescent.setLayoutParams(lp);
            }
            return insets;
        });

        // Preload Donation / Khedmat data and HTML in background on cold start
        try {
            com.devflux.deenone.features.donation.DonationCacheManager.preloadDonationData(this);
        } catch (Exception ignored) {}

        // 1-Tap Skip Support for optimal UX
        binding.layoutSplashRoot.setOnClickListener(v -> navigateToMain());

        // Check if animations are disabled via Accessibility (Reduce Motion)
        if (isReduceMotionEnabled()) {
            navigateToMain();
            return;
        }

        // Execute Animation Timeline
        startSplashAnimationTimeline();
    }

    private boolean isReduceMotionEnabled() {
        try {
            float durationScale = Settings.Global.getFloat(
                    getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1.0f
            );
            return durationScale == 0.0f;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private void startSplashAnimationTimeline() {
        FastOutSlowInInterpolator fastOutSlow = new FastOutSlowInInterpolator();
        DecelerateInterpolator decelerate = new DecelerateInterpolator();
        AccelerateDecelerateInterpolator smooth = new AccelerateDecelerateInterpolator();

        // 1. 0.0s -> 0.4s (400ms): Islamic Geometric Pattern fades in softly
        binding.ivSplashPattern.animate()
                .alpha(0.35f)
                .setDuration(400)
                .setInterpolator(smooth)
                .start();

        // 2. 0.3s -> 0.9s (600ms): Ambient glow, minimal crescent moon, and distant mosque silhouette appear
        binding.viewAmbientGlow.animate()
                .alpha(0.90f)
                .setStartDelay(300)
                .setDuration(600)
                .setInterpolator(smooth)
                .start();

        binding.ivSplashCrescent.animate()
                .alpha(0.85f)
                .setStartDelay(300)
                .setDuration(600)
                .setInterpolator(smooth)
                .start();

        binding.ivSplashMosque.animate()
                .alpha(0.70f)
                .setStartDelay(300)
                .setDuration(600)
                .setInterpolator(smooth)
                .start();

        // 3. 0.6s -> 1.2s (600ms): Official DeenOne Brand Logo fades in (0% -> 100%) and scales (95% -> 100%)
        binding.ivDeenOneLogo.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setStartDelay(600)
                .setDuration(600)
                .setInterpolator(fastOutSlow)
                .start();

        // 4. 1.0s -> 1.5s (500ms): App Name "DeenOne" smoothly fades in
        binding.tvSplashAppName.animate()
                .alpha(1.0f)
                .setStartDelay(1000)
                .setDuration(500)
                .setInterpolator(decelerate)
                .start();

        // 5. 1.3s -> 1.8s (500ms): Bengali Tagline “দিনকে সাজাও, দ্বীনকে জানো” fades in with subtle upward translation
        binding.tvSplashTagline.animate()
                .alpha(1.0f)
                .translationY(0f)
                .setStartDelay(1300)
                .setDuration(500)
                .setInterpolator(decelerate)
                .start();

        // 6. 1.8s -> 2.1s: Hold briefly and smoothly transition into MainActivity
        navigateRunnable = this::navigateToMain;
        handler.postDelayed(navigateRunnable, 2100);
    }

    private synchronized void navigateToMain() {
        if (hasNavigated) return;
        hasNavigated = true;

        if (navigateRunnable != null) {
            handler.removeCallbacks(navigateRunnable);
        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Smooth cross-fade transition without flash
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (navigateRunnable != null) {
            handler.removeCallbacks(navigateRunnable);
        }
        super.onDestroy();
    }
}
