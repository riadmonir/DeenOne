package com.devflux.deenone.core.ui.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;

/**
 * Premium 60 FPS DeenOne Islamic Syncing Loader.
 * Features:
 *  1. Outer subtle track ring with theme-aware accent color.
 *  2. Continuous smooth 360-degree rotating arc.
 *  3. Center pulsing subtle dot.
 *  4. Zero garbage-collection allocation in onDraw().
 */
public class DeenOneLoadingIndicatorView extends View {

    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF arcBounds = new RectF();

    private float currentAngle = 0f;
    private float centerDotScale = 1.0f;

    private ValueAnimator rotateAnimator;
    private ValueAnimator pulseAnimator;

    private int primaryColor;
    private int trackColor;

    public DeenOneLoadingIndicatorView(Context context) {
        super(context);
        init(context, null);
    }

    public DeenOneLoadingIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeenOneLoadingIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        primaryColor = ContextCompat.getColor(context, R.color.accent_mint);
        trackColor = (primaryColor & 0x00FFFFFF) | 0x33000000; // ~20% alpha track

        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setColor(trackColor);

        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(primaryColor);

        centerDotPaint.setStyle(Paint.Style.FILL);
        centerDotPaint.setColor(primaryColor);

        setupAnimators();
    }

    private void setupAnimators() {
        rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.setDuration(1100);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotateAnimator.addUpdateListener(animation -> {
            currentAngle = (float) animation.getAnimatedValue();
            invalidate();
        });

        pulseAnimator = ValueAnimator.ofFloat(0.75f, 1.15f);
        pulseAnimator.setDuration(900);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulseAnimator.addUpdateListener(animation -> {
            centerDotScale = (float) animation.getAnimatedValue();
        });
    }

    public void setAccentColor(int color) {
        this.primaryColor = color;
        this.trackColor = (color & 0x00FFFFFF) | 0x33000000;
        trackPaint.setColor(trackColor);
        arcPaint.setColor(primaryColor);
        centerDotPaint.setColor(primaryColor);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float strokeWidth = Math.max(3f, Math.min(w, h) * 0.075f);
        trackPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStrokeWidth(strokeWidth);

        float padding = strokeWidth * 1.2f;
        arcBounds.set(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(arcBounds.width(), arcBounds.height()) / 2f;

        if (radius <= 0) return;

        // 1. Draw Background Track Ring
        canvas.drawCircle(cx, cy, radius, trackPaint);

        // 2. Draw Rotating Arc (90 degrees sweep with rounded caps)
        canvas.drawArc(arcBounds, currentAngle, 95f, false, arcPaint);

        // 3. Draw Center Pulsing Dot
        float baseDotRadius = radius * 0.22f;
        canvas.drawCircle(cx, cy, baseDotRadius * centerDotScale, centerDotPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    public void startAnimation() {
        if (rotateAnimator != null && !rotateAnimator.isRunning()) {
            rotateAnimator.start();
        }
        if (pulseAnimator != null && !pulseAnimator.isRunning()) {
            pulseAnimator.start();
        }
    }

    public void stopAnimation() {
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
        if (pulseAnimator != null) {
            pulseAnimator.cancel();
        }
    }
}
