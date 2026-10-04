package com.devflux.deenone.core.ui.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;

/**
 * ==============================================================================
 * DEEN ONE - ANIMATED LOADING DRAWABLE (GLIDE PLACEHOLDER & VIEW DRAWABLE)
 * ==============================================================================
 * Renders the official DeenOne "SYNCING DEENONE" circular spinner animation
 * with 60 FPS performance, zero GC allocations in draw, and theme-adaptive colors.
 */
public class DeenOneLoadingDrawable extends Drawable implements Animatable {

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF arcBounds = new RectF();
    private float currentAngle = 0f;
    private float centerDotScale = 1.0f;

    private ValueAnimator rotateAnimator;
    private ValueAnimator pulseAnimator;

    private final String text;
    private boolean showText = true;
    private boolean showBackground = true;

    public DeenOneLoadingDrawable(@NonNull Context context) {
        this(context, true, true);
    }

    public DeenOneLoadingDrawable(@NonNull Context context, boolean showText, boolean showBackground) {
        this.showText = showText;
        this.showBackground = showBackground;

        int bgColor = ContextCompat.getColor(context, R.color.bg_card);
        int accentColor = ContextCompat.getColor(context, R.color.accent_mint);
        int trackColor = (accentColor & 0x00FFFFFF) | 0x33000000;

        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);

        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setColor(trackColor);

        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(accentColor);

        centerDotPaint.setStyle(Paint.Style.FILL);
        centerDotPaint.setColor(accentColor);

        boolean isBn = LocaleManager.isBengali(context);
        this.text = isBn ? "দিনওয়ান সিঙ্ক হচ্ছে..." : "SYNCING DEENONE";

        textPaint.setColor(accentColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
        textPaint.setLetterSpacing(0.20f);

        setupAnimators();
        startAnimators();
    }

    private void startAnimators() {
        if (rotateAnimator != null && !rotateAnimator.isRunning()) {
            rotateAnimator.start();
        }
        if (pulseAnimator != null && !pulseAnimator.isRunning()) {
            pulseAnimator.start();
        }
    }

    private void setupAnimators() {
        rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.setDuration(1100);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotateAnimator.addUpdateListener(animation -> {
            currentAngle = (float) animation.getAnimatedValue();
            invalidateSelf();
        });

        pulseAnimator = ValueAnimator.ofFloat(0.75f, 1.15f);
        pulseAnimator.setDuration(900);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulseAnimator.addUpdateListener(animation -> centerDotScale = (float) animation.getAnimatedValue());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.width() <= 0 || bounds.height() <= 0) return;

        // 1. Draw Background
        if (showBackground) {
            canvas.drawRect(bounds, bgPaint);
        }

        float cx = bounds.exactCenterX();
        float cy = bounds.exactCenterY();
        float minDim = Math.min(bounds.width(), bounds.height());

        // Adjust dimensions based on available size
        float spinnerRadius = Math.max(16f, minDim * 0.18f);
        float strokeWidth = Math.max(2.5f, spinnerRadius * 0.14f);

        trackPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStrokeWidth(strokeWidth);

        // Position spinner center slightly higher if text is shown
        float spinnerCy = showText && minDim > 100 ? cy - (spinnerRadius * 0.45f) : cy;

        arcBounds.set(cx - spinnerRadius, spinnerCy - spinnerRadius, cx + spinnerRadius, spinnerCy + spinnerRadius);

        // 2. Draw Subtle Track Ring
        canvas.drawCircle(cx, spinnerCy, spinnerRadius, trackPaint);

        // 3. Draw Rotating Arc (95 degrees)
        canvas.drawArc(arcBounds, currentAngle, 95f, false, arcPaint);

        // 4. Draw Center Pulsing Dot
        float dotRadius = spinnerRadius * 0.22f;
        canvas.drawCircle(cx, spinnerCy, dotRadius * centerDotScale, centerDotPaint);

        // 5. Draw "SYNCING DEENONE" Text
        if (showText && minDim > 100) {
            float textSize = Math.max(10f, Math.min(13f, minDim * 0.045f));
            textPaint.setTextSize(textSize);
            float textY = spinnerCy + spinnerRadius + (textSize * 1.8f);
            canvas.drawText(text, cx, textY, textPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        bgPaint.setAlpha(alpha);
        trackPaint.setAlpha(alpha);
        arcPaint.setAlpha(alpha);
        centerDotPaint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        arcPaint.setColorFilter(colorFilter);
        centerDotPaint.setColorFilter(colorFilter);
        textPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        if (rotateAnimator != null && !rotateAnimator.isRunning()) {
            rotateAnimator.start();
        }
        if (pulseAnimator != null && !pulseAnimator.isRunning()) {
            pulseAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
        if (pulseAnimator != null) {
            pulseAnimator.cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return (rotateAnimator != null && rotateAnimator.isRunning()) ||
                (pulseAnimator != null && pulseAnimator.isRunning());
    }
}
