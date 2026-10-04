package com.devflux.deenone.core.tasbih;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;

/**
 * CircularTasbihProgressView — Renders an ultra-smooth, responsive circular progress ring
 * for the Digital Tasbih Counter with micro-bounce animations.
 */
public class CircularTasbihProgressView extends View {

    private Paint trackPaint;
    private Paint progressPaint;
    private Paint fillPaint;
    private RectF bounds;

    private float progress = 0f; // 0.0 to 1.0
    private ValueAnimator progressAnimator;

    private float strokeWidthPx = 14f;

    public CircularTasbihProgressView(Context context) {
        super(context);
        init(context);
    }

    public CircularTasbihProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularTasbihProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        strokeWidthPx = getResources().getDisplayMetrics().density * 5.5f;

        int accentColor = ContextCompat.getColor(context, R.color.accent_mint);
        int trackColor = ContextCompat.getColor(context, R.color.border_card);

        // Track paint
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(strokeWidthPx);
        trackPaint.setColor(trackColor);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        // Progress paint
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidthPx);
        progressPaint.setColor(accentColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        // Fill paint for subtle background card
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.TRANSPARENT);

        bounds = new RectF();
    }

    public void updateThemeColors(Context context) {
        if (context == null) return;
        int accentColor = ContextCompat.getColor(context, R.color.accent_mint);
        int trackColor = ContextCompat.getColor(context, R.color.border_card);

        trackPaint.setColor(trackColor);
        progressPaint.setColor(accentColor);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float pad = strokeWidthPx / 2f + 4f;
        bounds.set(pad, pad, w - pad, h - pad);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw center fill if needed
        canvas.drawOval(bounds, fillPaint);

        // Draw track ring
        canvas.drawOval(bounds, trackPaint);

        // Draw progress arc
        if (progress > 0f) {
            float sweepAngle = Math.min(360f, progress * 360f);
            canvas.drawArc(bounds, -90f, sweepAngle, false, progressPaint);
        }
    }

    public void setProgress(int current, int target, boolean animate) {
        float targetProgress;
        if (target <= 0) {
            targetProgress = 1.0f; // Continuous mode
        } else {
            int effectiveCurrent = current % target;
            if (effectiveCurrent == 0 && current > 0) {
                targetProgress = 1.0f;
            } else {
                targetProgress = (float) effectiveCurrent / (float) target;
            }
        }

        targetProgress = Math.max(0f, Math.min(1f, targetProgress));

        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }

        if (animate) {
            progressAnimator = ValueAnimator.ofFloat(this.progress, targetProgress);
            progressAnimator.setDuration(180);
            progressAnimator.setInterpolator(new DecelerateInterpolator());
            progressAnimator.addUpdateListener(anim -> {
                this.progress = (float) anim.getAnimatedValue();
                invalidate();
            });
            progressAnimator.start();
        } else {
            this.progress = targetProgress;
            invalidate();
        }
    }
}
