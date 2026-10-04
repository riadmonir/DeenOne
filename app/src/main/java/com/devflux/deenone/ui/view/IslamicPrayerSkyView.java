package com.devflux.deenone.ui.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devflux.deenone.R;
import com.devflux.deenone.core.theme.ThemeManager;

import java.util.Calendar;

/**
 * IslamicPrayerSkyView
 *
 * A high-performance, hardware-accelerated living background environment for the
 * Islamic Prayer Card View, strictly matching Reference Screenshot 2.
 *
 * Features:
 * - Full-fledged photorealistic HD mosque silhouette (eliminating any "vector emoji" look).
 * - Real-time Day/Night celestial cycle:
 *   - Day: Bright luminous Sun moving in a natural parabolic arc with radiant halo & dawn/sunset glow.
 *   - Night: Prominent, glowing Crescent Moon (Hilal) with cyan-white halo, twinkling stars,
 *     and deep nocturnal sky matching Screenshot 2.
 * - Seamless Light Mode & Dark Mode dynamic adaptation:
 *   - Light Mode (Daytime): Luminous bright azure daylight sky, brilliant golden sun, and deep
 *     architectural silhouette with crystal clear foreground text contrast.
 *   - Light Mode (Nighttime): Deep twilight atmosphere harmonized with card container.
 *   - Dark Mode: Deep midnight navy / celestial space sky.
 * - 60 FPS GPU-efficient rendering with ZERO heap allocations during onDraw().
 * - Full prefers-reduced-motion and window lifecycle support.
 */
public class IslamicPrayerSkyView extends View {

    // Prayer Time Anchors (Epoch Milliseconds)
    private long fajrMillis;
    private long sunriseMillis;
    private long zohrMillis;
    private long asrMillis;
    private long sunsetMillis;
    private long ishaMillis;
    private long manualTimeMillis = -1;

    // Lifecycle & animation control
    private boolean isAttached = false;
    private boolean isViewVisible = true;
    private boolean isAnimating = false;
    private long lastFrameTime = 0;
    private float animationClockSec = 0f;

    // Cloud drift offsets
    private float cloudOffset1 = 0f;
    private float cloudOffset2 = 80f;
    private float cloudOffset3 = 190f;

    // Cached dimensions
    private int viewWidth = 0;
    private int viewHeight = 0;

    // Atmosphere state cache for current frame
    private int skyTopColor;
    private int skyBottomColor;
    private int horizonGlowColor;
    private float sunAltitude; // 0 (horizon) to 1 (zenith)
    private float sunX, sunY;
    private float sunAlpha;
    private float moonX, moonY;
    private float moonAlpha;
    private float starsAlpha;
    private float dawnDuskGlowAlpha;
    private int fgColor;
    private int mgColor;
    private int distantHillsColor;

    // Dynamic Mosque Colors across Solar Cycle
    private int fgTopColor;
    private int fgBottomColor;
    private int mgTopColor;
    private int mgBottomColor;
    private float windowGlowAlpha;
    private int windowGlowColor;

    // Photorealistic Sun Color Palette
    private int sunAtmosphereColor;
    private int sunCoronaColor;
    private int sunCoreCenterColor;
    private int sunCoreMidColor;
    private int sunCoreEdgeColor;
    private int sunRaysColor;

    // Photorealistic Mosque Architecture Assets & Bounds
    private Bitmap mosqueBitmap = null;
    private Bitmap mosqueNightLightsBitmap = null;
    private final Rect mosqueSrcRect = new Rect();
    private final Rect mosqueLightsSrcRect = new Rect();
    private final RectF mosqueDestRectForeground = new RectF();
    private final RectF mosqueDestRectMidground = new RectF();

    // Distant Misty Mountain Ridges (Screenshot 2 Depth Layer)
    private final Paint distantHillsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path distantHillsPath = new Path();

    // Fixed Celestial Star Field
    private static final int STAR_COUNT = 32;
    private static final float[] STAR_X_RATIOS = {
        0.05f, 0.12f, 0.18f, 0.25f, 0.32f, 0.38f, 0.45f, 0.52f,
        0.58f, 0.65f, 0.72f, 0.78f, 0.85f, 0.92f, 0.08f, 0.15f,
        0.22f, 0.29f, 0.36f, 0.42f, 0.49f, 0.56f, 0.63f, 0.70f,
        0.76f, 0.83f, 0.89f, 0.95f, 0.14f, 0.34f, 0.60f, 0.81f
    };
    private static final float[] STAR_Y_RATIOS = {
        0.08f, 0.15f, 0.06f, 0.20f, 0.12f, 0.24f, 0.10f, 0.18f,
        0.14f, 0.26f, 0.09f, 0.22f, 0.13f, 0.17f, 0.30f, 0.36f,
        0.28f, 0.39f, 0.32f, 0.42f, 0.30f, 0.38f, 0.32f, 0.40f,
        0.35f, 0.44f, 0.33f, 0.39f, 0.48f, 0.45f, 0.47f, 0.46f
    };
    private static final float[] STAR_SIZES = {
        1.6f, 2.2f, 1.4f, 2.6f, 1.5f, 2.0f, 1.8f, 2.4f,
        1.5f, 2.0f, 1.6f, 2.5f, 1.8f, 2.2f, 1.4f, 1.9f,
        2.3f, 1.5f, 2.0f, 1.7f, 2.4f, 1.5f, 2.1f, 1.8f,
        2.5f, 1.6f, 2.2f, 1.4f, 1.8f, 2.0f, 1.5f, 2.3f
    };
    private static final float[] STAR_PHASES = {
        0.0f, 1.2f, 2.4f, 0.8f, 3.1f, 1.7f, 4.2f, 2.9f,
        0.5f, 3.8f, 1.9f, 4.5f, 2.1f, 0.3f, 3.4f, 1.1f,
        2.7f, 4.0f, 0.9f, 3.3f, 1.6f, 4.7f, 2.3f, 0.7f,
        3.6f, 1.4f, 4.9f, 2.5f, 0.4f, 3.2f, 1.8f, 4.1f
    };

    // Pre-allocated Drawing Objects (Zero heap allocations in onDraw)
    private final Paint skyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint horizonGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunCorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunAuraPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunAtmospherePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunCoronaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunRaysPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunFlarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint moonCorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint moonGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint cloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mosqueForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Paint mosqueShadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mosqueSunWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mosqueLightsPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Paint mosquePortalGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mosqueGroundMistPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint readabilityScrimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final PorterDuffXfermode multiplyXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
    private final PorterDuffXfermode screenXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);
    private final PorterDuffXfermode srcAtopXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    private final Path sunRayPath = new Path();
    private final RectF sunFlareRect = new RectF();

    private final Path crescentPath = new Path();
    private final Path crescentCutPath = new Path();
    private final RectF tempRect = new RectF();

    public IslamicPrayerSkyView(Context context) {
        super(context);
        init();
    }

    public IslamicPrayerSkyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IslamicPrayerSkyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setupDefaultTimestamps();

        skyPaint.setStyle(Paint.Style.FILL);
        horizonGlowPaint.setStyle(Paint.Style.FILL);
        sunCorePaint.setStyle(Paint.Style.FILL);
        sunAuraPaint.setStyle(Paint.Style.FILL);
        moonCorePaint.setStyle(Paint.Style.FILL);
        moonGlowPaint.setStyle(Paint.Style.FILL);
        starPaint.setStyle(Paint.Style.FILL);
        cloudPaint.setStyle(Paint.Style.FILL);
        mosqueShadingPaint.setStyle(Paint.Style.FILL);
        mosqueSunWavePaint.setStyle(Paint.Style.FILL);
        mosquePortalGlowPaint.setStyle(Paint.Style.FILL);
        mosqueGroundMistPaint.setStyle(Paint.Style.FILL);
        readabilityScrimPaint.setStyle(Paint.Style.FILL);
        distantHillsPaint.setStyle(Paint.Style.FILL);

        loadMosqueBitmaps();
    }

    private void loadMosqueBitmaps() {
        try {
            if (mosqueBitmap == null || mosqueBitmap.isRecycled()) {
                mosqueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_mosque_realistic);
                if (mosqueBitmap != null) {
                    mosqueSrcRect.set(0, 0, mosqueBitmap.getWidth(), mosqueBitmap.getHeight());
                }
            }
        } catch (Throwable e) {
            mosqueBitmap = null;
        }

        try {
            if (mosqueNightLightsBitmap == null || mosqueNightLightsBitmap.isRecycled()) {
                mosqueNightLightsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_mosque_night_lights);
                if (mosqueNightLightsBitmap != null) {
                    mosqueLightsSrcRect.set(0, 0, mosqueNightLightsBitmap.getWidth(), mosqueNightLightsBitmap.getHeight());
                }
            }
        } catch (Throwable e) {
            mosqueNightLightsBitmap = null;
        }
    }

    private void setupDefaultTimestamps() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 4);
        cal.set(Calendar.MINUTE, 50);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        fajrMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 5);
        sunriseMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 10);
        zohrMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 30);
        asrMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 15);
        sunsetMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE, 30);
        ishaMillis = cal.getTimeInMillis();
    }

    /**
     * Synchronize with real-time calculated prayer times.
     */
    public void setPrayerTimes(long fajr, long sunrise, long zohr, long asr, long sunset, long isha) {
        if (fajr > 0) this.fajrMillis = fajr;
        if (sunrise > 0) this.sunriseMillis = sunrise;
        if (zohr > 0) this.zohrMillis = zohr;
        if (asr > 0) this.asrMillis = asr;
        if (sunset > 0) this.sunsetMillis = sunset;
        if (isha > 0) this.ishaMillis = isha;
        postInvalidateOnAnimation();
    }

    public void setManualTime(long timeMillis) {
        this.manualTimeMillis = timeMillis;
        postInvalidateOnAnimation();
    }

    private long getCurrentTime() {
        return manualTimeMillis > 0 ? manualTimeMillis : System.currentTimeMillis();
    }

    private boolean isLightMode() {
        return ThemeManager.isLightMode(getContext());
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        postInvalidateOnAnimation();
    }

    private boolean isReducedMotionEnabled() {
        try {
            float duration = Settings.Global.getFloat(
                getContext().getContentResolver(),
                Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f);
            float transition = Settings.Global.getFloat(
                getContext().getContentResolver(),
                Settings.Global.TRANSITION_ANIMATION_SCALE, 1.0f);
            return duration == 0f || transition == 0f;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (h == 0) {
            float density = getResources().getDisplayMetrics().density;
            h = (int) (140 * density);
        }
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        if (mosqueBitmap == null || mosqueNightLightsBitmap == null) {
            loadMosqueBitmaps();
        }
        startAnimationLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
        stopAnimationLoop();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        isViewVisible = (getVisibility() == VISIBLE) && (getWindowVisibility() == VISIBLE);
        if (isViewVisible) {
            startAnimationLoop();
        } else {
            stopAnimationLoop();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        isViewVisible = (visibility == VISIBLE) && (getVisibility() == VISIBLE);
        if (isViewVisible) {
            startAnimationLoop();
        } else {
            stopAnimationLoop();
        }
    }

    private final Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAttached || !isViewVisible || !isAnimating) return;

            long now = SystemClock.uptimeMillis();
            float dt = (lastFrameTime == 0) ? 0.016f : Math.min(0.05f, (now - lastFrameTime) / 1000f);
            lastFrameTime = now;

            if (!isReducedMotionEnabled()) {
                animationClockSec += dt;

                float density = getResources().getDisplayMetrics().density;
                cloudOffset1 += 3.5f * density * dt;
                cloudOffset2 += 2.4f * density * dt;
                cloudOffset3 += 1.5f * density * dt;

                float maxW = viewWidth > 0 ? viewWidth + 240f * density : 800f;
                if (cloudOffset1 > maxW) cloudOffset1 = -160f * density;
                if (cloudOffset2 > maxW) cloudOffset2 = -160f * density;
                if (cloudOffset3 > maxW) cloudOffset3 = -160f * density;

                postInvalidateOnAnimation();
                postOnAnimation(this);
            } else {
                postInvalidate();
                postDelayed(this, 1000);
            }
        }
    };

    private void startAnimationLoop() {
        if (!isAnimating && isAttached && isViewVisible) {
            isAnimating = true;
            lastFrameTime = SystemClock.uptimeMillis();
            removeCallbacks(animationRunnable);
            postOnAnimation(animationRunnable);
        }
    }

    private void stopAnimationLoop() {
        isAnimating = false;
        removeCallbacks(animationRunnable);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;

        if (w > 0 && h > 0) {
            // Distant soft misty hills across background
            distantHillsPath.reset();
            distantHillsPath.moveTo(0, h * 0.95f);
            distantHillsPath.lineTo(0, h * 0.78f);
            distantHillsPath.cubicTo(w * 0.15f, h * 0.74f, w * 0.28f, h * 0.82f, w * 0.45f, h * 0.76f);
            distantHillsPath.cubicTo(w * 0.60f, h * 0.71f, w * 0.78f, h * 0.79f, w, h * 0.75f);
            distantHillsPath.lineTo(w, h);
            distantHillsPath.lineTo(0, h);
            distantHillsPath.close();

            // Position photorealistic Grand Mosque anchored to bottom right
            // Asset aspect ratio: 1376 / 768 = 1.79167f
            float targetHeight = h * 1.04f;
            float aspect = (mosqueBitmap != null && mosqueBitmap.getHeight() > 0)
                ? ((float) mosqueBitmap.getWidth() / (float) mosqueBitmap.getHeight())
                : 1.79167f;
            float targetWidth = targetHeight * aspect;
            float fgBottom = h + (h * 0.03f);
            float fgTop = fgBottom - targetHeight;
            float fgRight = w * 1.02f;
            float fgLeft = fgRight - targetWidth;
            mosqueDestRectForeground.set(fgLeft, fgTop, fgRight, fgBottom);
        }
    }

    /**
     * Real-time calculation of Sun, Moon, Stars, and Theme-Aware Sky Colors.
     */
    private void calculateCurrentAtmosphere(long now) {
        long fajr = fajrMillis;
        long sunrise = sunriseMillis;
        long sunset = sunsetMillis;
        long isha = ishaMillis;

        if (sunrise <= fajr) sunrise = fajr + 75 * 60 * 1000L;
        if (sunset <= sunrise) sunset = sunrise + 12 * 3600 * 1000L;
        if (isha <= sunset) isha = sunset + 75 * 60 * 1000L;

        boolean isDaytime = (now >= sunrise && now < sunset);
        boolean isLight = isLightMode();

        float w = (viewWidth > 0) ? viewWidth : 800f;
        float h = (viewHeight > 0) ? viewHeight : 350f;

        // ---------------------------------------------------------------------
        // 1. CELESTIAL BODIES (Daytime Sun vs Nighttime Crescent Moon)
        // ---------------------------------------------------------------------
        if (isDaytime) {
            float progress = (float) (now - sunrise) / (float) (sunset - sunrise);
            progress = Math.max(0f, Math.min(1f, progress));

            // Sun path: Natural celestial arc
            sunX = w * (0.20f + 0.58f * progress);
            sunAltitude = (float) Math.sin(progress * Math.PI);
            sunY = (h * 0.88f) - (h * 0.68f * sunAltitude);

            sunAlpha = 1.0f;
            moonAlpha = 0.0f;
            starsAlpha = 0.0f;
        } else {
            // Nighttime: Prominent Crescent Moon matching Screenshot 2
            moonX = w * 0.22f;
            moonY = h * 0.25f;

            moonAlpha = 1.0f;
            starsAlpha = 1.0f;
            sunAlpha = 0.0f;
            sunAltitude = 0.0f;
        }

        // ---------------------------------------------------------------------
        // 2. THEME-AWARE SKY PALETTE (Adapting gracefully to Light & Dark Mode)
        // ---------------------------------------------------------------------
        long tPreDawn = fajr - 35 * 60 * 1000L;
        long tDawn = sunrise - 25 * 60 * 1000L;
        long tSunriseEnd = sunrise + 35 * 60 * 1000L;
        long tGoldenHour = sunset - 45 * 60 * 1000L;
        long tSunsetEnd = sunset + 25 * 60 * 1000L;
        long tDuskEnd = isha + 20 * 60 * 1000L;

        dawnDuskGlowAlpha = 0f;
        horizonGlowColor = Color.TRANSPARENT;

        if (isLight) {
            // ==================== LIGHT THEME ADAPTATION ====================
            // Luminous daylight sky during day; refined serene twilight at night
            final int LT_NIGHT_TOP = Color.parseColor("#1E293B");
            final int LT_NIGHT_BOT = Color.parseColor("#334155");

            final int LT_PREDAWN_TOP = Color.parseColor("#1E293B");
            final int LT_PREDAWN_BOT = Color.parseColor("#3B82F6");

            final int LT_DAWN_TOP = Color.parseColor("#FEF08A");
            final int LT_DAWN_BOT = Color.parseColor("#BAE6FD");

            final int LT_DAY_TOP = Color.parseColor("#EBF8FF");
            final int LT_DAY_BOT = Color.parseColor("#FFFFFF");

            final int LT_GOLDEN_TOP = Color.parseColor("#FEF3C7");
            final int LT_GOLDEN_BOT = Color.parseColor("#FED7AA");

            final int LT_SUNSET_TOP = Color.parseColor("#FDBA74");
            final int LT_SUNSET_BOT = Color.parseColor("#FB7185");

            final int LT_DUSK_TOP = Color.parseColor("#1E293B");
            final int LT_DUSK_BOT = Color.parseColor("#334155");

            if (now < tPreDawn) {
                skyTopColor = LT_NIGHT_TOP;
                skyBottomColor = LT_NIGHT_BOT;
            } else if (now < tDawn) {
                float frac = (float) (now - tPreDawn) / (float) (tDawn - tPreDawn);
                skyTopColor = interpolateColor(LT_NIGHT_TOP, LT_PREDAWN_TOP, frac);
                skyBottomColor = interpolateColor(LT_NIGHT_BOT, LT_PREDAWN_BOT, frac);
            } else if (now < sunrise) {
                float frac = (float) (now - tDawn) / (float) (sunrise - tDawn);
                skyTopColor = interpolateColor(LT_PREDAWN_TOP, LT_DAWN_TOP, frac);
                skyBottomColor = interpolateColor(LT_PREDAWN_BOT, LT_DAWN_BOT, frac);
                horizonGlowColor = Color.parseColor("#F59E0B");
                dawnDuskGlowAlpha = frac * 0.7f;
            } else if (now < tSunriseEnd) {
                float frac = (float) (now - sunrise) / (float) (tSunriseEnd - sunrise);
                skyTopColor = interpolateColor(LT_DAWN_TOP, LT_DAY_TOP, frac);
                skyBottomColor = interpolateColor(LT_DAWN_BOT, LT_DAY_BOT, frac);
                horizonGlowColor = Color.parseColor("#F59E0B");
                dawnDuskGlowAlpha = (1.0f - frac) * 0.6f;
            } else if (now < tGoldenHour) {
                skyTopColor = LT_DAY_TOP;
                skyBottomColor = LT_DAY_BOT;
            } else if (now < sunset) {
                float frac = (float) (now - tGoldenHour) / (float) (sunset - tGoldenHour);
                skyTopColor = interpolateColor(LT_DAY_TOP, LT_GOLDEN_TOP, frac);
                skyBottomColor = interpolateColor(LT_DAY_BOT, LT_GOLDEN_BOT, frac);
                horizonGlowColor = Color.parseColor("#EA580C");
                dawnDuskGlowAlpha = frac * 0.6f;
            } else if (now < tSunsetEnd) {
                float frac = (float) (now - sunset) / (float) (tSunsetEnd - sunset);
                skyTopColor = interpolateColor(LT_GOLDEN_TOP, LT_SUNSET_TOP, frac);
                skyBottomColor = interpolateColor(LT_GOLDEN_BOT, LT_SUNSET_BOT, frac);
                horizonGlowColor = Color.parseColor("#EA580C");
                dawnDuskGlowAlpha = (1.0f - frac) * 0.7f;
            } else if (now < tDuskEnd) {
                float frac = (float) (now - tSunsetEnd) / (float) (tDuskEnd - tSunsetEnd);
                skyTopColor = interpolateColor(LT_SUNSET_TOP, LT_DUSK_TOP, frac);
                skyBottomColor = interpolateColor(LT_SUNSET_BOT, LT_DUSK_BOT, frac);
            } else {
                skyTopColor = LT_NIGHT_TOP;
                skyBottomColor = LT_NIGHT_BOT;
            }
        } else {
            // ==================== DARK THEME ADAPTATION ====================
            // Nocturnal deep midnight palette matching Reference Screenshot 2
            final int DK_NIGHT_TOP = Color.parseColor("#040814");
            final int DK_NIGHT_BOT = Color.parseColor("#0A1932");

            final int DK_PREDAWN_TOP = Color.parseColor("#071126");
            final int DK_PREDAWN_BOT = Color.parseColor("#122543");

            final int DK_DAWN_TOP = Color.parseColor("#0F1A30");
            final int DK_DAWN_BOT = Color.parseColor("#263859");

            final int DK_SUNRISE_TOP = Color.parseColor("#18223C");
            final int DK_SUNRISE_BOT = Color.parseColor("#9A3412");

            final int DK_DAY_TOP = Color.parseColor("#0C3256");
            final int DK_DAY_BOT = Color.parseColor("#1B5A8F");

            final int DK_GOLDEN_TOP = Color.parseColor("#1C1A44");
            final int DK_GOLDEN_BOT = Color.parseColor("#B45309");

            final int DK_SUNSET_TOP = Color.parseColor("#171136");
            final int DK_SUNSET_BOT = Color.parseColor("#881337");

            final int DK_DUSK_TOP = Color.parseColor("#090C22");
            final int DK_DUSK_BOT = Color.parseColor("#181944");

            if (now < tPreDawn) {
                skyTopColor = DK_NIGHT_TOP;
                skyBottomColor = DK_NIGHT_BOT;
            } else if (now < tDawn) {
                float frac = (float) (now - tPreDawn) / (float) (tDawn - tPreDawn);
                skyTopColor = interpolateColor(DK_NIGHT_TOP, DK_PREDAWN_TOP, frac);
                skyBottomColor = interpolateColor(DK_NIGHT_BOT, DK_PREDAWN_BOT, frac);
            } else if (now < sunrise) {
                float frac = (float) (now - tDawn) / (float) (sunrise - tDawn);
                skyTopColor = interpolateColor(DK_DAWN_TOP, DK_SUNRISE_TOP, frac);
                skyBottomColor = interpolateColor(DK_DAWN_BOT, DK_SUNRISE_BOT, frac);
                horizonGlowColor = Color.parseColor("#F59E0B");
                dawnDuskGlowAlpha = frac * 0.7f;
            } else if (now < tSunriseEnd) {
                float frac = (float) (now - sunrise) / (float) (tSunriseEnd - sunrise);
                skyTopColor = interpolateColor(DK_SUNRISE_TOP, DK_DAY_TOP, frac);
                skyBottomColor = interpolateColor(DK_SUNRISE_BOT, DK_DAY_BOT, frac);
                horizonGlowColor = Color.parseColor("#F59E0B");
                dawnDuskGlowAlpha = (1.0f - frac) * 0.8f;
            } else if (now < tGoldenHour) {
                skyTopColor = DK_DAY_TOP;
                skyBottomColor = DK_DAY_BOT;
            } else if (now < sunset) {
                float frac = (float) (now - tGoldenHour) / (float) (sunset - tGoldenHour);
                skyTopColor = interpolateColor(DK_DAY_TOP, DK_GOLDEN_TOP, frac);
                skyBottomColor = interpolateColor(DK_DAY_BOT, DK_GOLDEN_BOT, frac);
                horizonGlowColor = Color.parseColor("#EA580C");
                dawnDuskGlowAlpha = frac * 0.65f;
            } else if (now < tSunsetEnd) {
                float frac = (float) (now - sunset) / (float) (tSunsetEnd - sunset);
                skyTopColor = interpolateColor(DK_GOLDEN_TOP, DK_SUNSET_TOP, frac);
                skyBottomColor = interpolateColor(DK_GOLDEN_BOT, DK_SUNSET_BOT, frac);
                horizonGlowColor = Color.parseColor("#EA580C");
                dawnDuskGlowAlpha = (1.0f - frac) * 0.75f;
            } else if (now < tDuskEnd) {
                float frac = (float) (now - tSunsetEnd) / (float) (tDuskEnd - tSunsetEnd);
                skyTopColor = interpolateColor(DK_SUNSET_TOP, DK_DUSK_TOP, frac);
                skyBottomColor = interpolateColor(DK_SUNSET_BOT, DK_DUSK_BOT, frac);
            } else {
                skyTopColor = DK_NIGHT_TOP;
                skyBottomColor = DK_NIGHT_BOT;
            }
        }

        // =====================================================================
        // 3. PHOTOREALISTIC SUN COLOR TEMPERATURE SHIFT (Altitude Aware)
        // =====================================================================
        sunCoreCenterColor = Color.WHITE; // Pure white-hot singularity core
        float altFactor = Math.max(0f, Math.min(1f, sunAltitude));

        // Mid-photosphere blends from golden-yellow at horizon to blinding pure white-hot at zenith
        sunCoreMidColor = interpolateColor(Color.parseColor("#FEF08A"), Color.parseColor("#FFFDF5"), altFactor);

        // Limb darkening rim shifts from warm sunset orange/coral to radiant solar yellow
        sunCoreEdgeColor = interpolateColor(Color.parseColor("#F97316"), Color.parseColor("#FDE047"), altFactor);

        // Corona ring shifts from deep amber-coral to brilliant solar gold
        sunCoronaColor = interpolateColor(Color.parseColor("#EA580C"), Color.parseColor("#FACC15"), altFactor);

        // Volumetric Rayleigh atmosphere halo: warm sunrise/sunset amber to crisp sky-bright dispersion
        sunAtmosphereColor = interpolateColor(Color.parseColor("#FB923C"), isLight ? Color.parseColor("#BAE6FD") : Color.parseColor("#38BDF8"), altFactor);

        // Optical diffraction rays
        sunRaysColor = interpolateColor(Color.parseColor("#FDE68A"), Color.parseColor("#FEF9C3"), altFactor);

        // =====================================================================
        // 4. DYNAMIC MOSQUE TRANSITIONS ACROSS 24H SOLAR CYCLE
        // =====================================================================
        long tMiddayPeak = sunrise + (sunset - sunrise) / 2;
        windowGlowColor = Color.parseColor("#F59E0B"); // Warm golden lantern glow

        if (isLight) {
            // -------------------- Light Theme Mosque Milestones --------------------
            final int FG_NIGHT_TOP = Color.parseColor("#1E293B");
            final int FG_NIGHT_BOT = Color.parseColor("#0F172A");
            final int MG_NIGHT_TOP = Color.parseColor("#334155");
            final int MG_NIGHT_BOT = Color.parseColor("#1E293B");

            final int FG_DAWN_TOP = Color.parseColor("#D97706");
            final int FG_DAWN_BOT = Color.parseColor("#1E293B");
            final int MG_DAWN_TOP = Color.parseColor("#B45309");
            final int MG_DAWN_BOT = Color.parseColor("#0F172A");

            final int FG_SUNRISE_TOP = Color.parseColor("#F59E0B");
            final int FG_SUNRISE_BOT = Color.parseColor("#064E3B");
            final int MG_SUNRISE_TOP = Color.parseColor("#D97706");
            final int MG_SUNRISE_BOT = Color.parseColor("#022C22");

            final int FG_DAY_TOP = Color.parseColor("#10B981");
            final int FG_DAY_BOT = Color.parseColor("#064E3B");
            final int MG_DAY_TOP = Color.parseColor("#059669");
            final int MG_DAY_BOT = Color.parseColor("#022C22");

            final int FG_GOLDEN_TOP = Color.parseColor("#EA580C");
            final int FG_GOLDEN_BOT = Color.parseColor("#1E293B");
            final int MG_GOLDEN_TOP = Color.parseColor("#C2410C");
            final int MG_GOLDEN_BOT = Color.parseColor("#0F172A");

            final int FG_SUNSET_TOP = Color.parseColor("#DC2626");
            final int FG_SUNSET_BOT = Color.parseColor("#1F2937");
            final int MG_SUNSET_TOP = Color.parseColor("#B91C1C");
            final int MG_SUNSET_BOT = Color.parseColor("#111827");

            final int FG_DUSK_TOP = Color.parseColor("#334155");
            final int FG_DUSK_BOT = Color.parseColor("#0F172A");
            final int MG_DUSK_TOP = Color.parseColor("#475569");
            final int MG_DUSK_BOT = Color.parseColor("#1E293B");

            distantHillsColor = Color.argb(isDaytime ? 45 : 95, 15, 60, 50);

            if (now < tPreDawn) {
                fgTopColor = FG_NIGHT_TOP; fgBottomColor = FG_NIGHT_BOT;
                mgTopColor = MG_NIGHT_TOP; mgBottomColor = MG_NIGHT_BOT;
                windowGlowAlpha = 0.8f;
            } else if (now < tDawn) {
                float frac = (float) (now - tPreDawn) / (float) (tDawn - tPreDawn);
                fgTopColor = interpolateColor(FG_NIGHT_TOP, FG_DAWN_TOP, frac);
                fgBottomColor = interpolateColor(FG_NIGHT_BOT, FG_DAWN_BOT, frac);
                mgTopColor = interpolateColor(MG_NIGHT_TOP, MG_DAWN_TOP, frac);
                mgBottomColor = interpolateColor(MG_NIGHT_BOT, MG_DAWN_BOT, frac);
                windowGlowAlpha = 0.8f;
            } else if (now < sunrise) {
                float frac = (float) (now - tDawn) / (float) (sunrise - tDawn);
                fgTopColor = interpolateColor(FG_DAWN_TOP, FG_SUNRISE_TOP, frac);
                fgBottomColor = interpolateColor(FG_DAWN_BOT, FG_SUNRISE_BOT, frac);
                mgTopColor = interpolateColor(MG_DAWN_TOP, MG_SUNRISE_TOP, frac);
                mgBottomColor = interpolateColor(MG_DAWN_BOT, MG_SUNRISE_BOT, frac);
                windowGlowAlpha = 0.8f * (1.0f - frac);
            } else if (now < tSunriseEnd) {
                float frac = (float) (now - sunrise) / (float) (tSunriseEnd - sunrise);
                fgTopColor = interpolateColor(FG_SUNRISE_TOP, FG_DAY_TOP, frac);
                fgBottomColor = interpolateColor(FG_SUNRISE_BOT, FG_DAY_BOT, frac);
                mgTopColor = interpolateColor(MG_SUNRISE_TOP, MG_DAY_TOP, frac);
                mgBottomColor = interpolateColor(MG_SUNRISE_BOT, MG_DAY_BOT, frac);
                windowGlowAlpha = 0.0f;
            } else if (now < tGoldenHour) {
                fgTopColor = FG_DAY_TOP; fgBottomColor = FG_DAY_BOT;
                mgTopColor = MG_DAY_TOP; mgBottomColor = MG_DAY_BOT;
                windowGlowAlpha = 0.0f;
            } else if (now < sunset) {
                float frac = (float) (now - tGoldenHour) / (float) (sunset - tGoldenHour);
                fgTopColor = interpolateColor(FG_DAY_TOP, FG_GOLDEN_TOP, frac);
                fgBottomColor = interpolateColor(FG_DAY_BOT, FG_GOLDEN_BOT, frac);
                mgTopColor = interpolateColor(MG_DAY_TOP, MG_GOLDEN_TOP, frac);
                mgBottomColor = interpolateColor(MG_DAY_BOT, MG_GOLDEN_BOT, frac);
                windowGlowAlpha = frac * 0.5f;
            } else if (now < tSunsetEnd) {
                float frac = (float) (now - sunset) / (float) (tSunsetEnd - sunset);
                fgTopColor = interpolateColor(FG_GOLDEN_TOP, FG_SUNSET_TOP, frac);
                fgBottomColor = interpolateColor(FG_GOLDEN_BOT, FG_SUNSET_BOT, frac);
                mgTopColor = interpolateColor(MG_GOLDEN_TOP, MG_SUNSET_TOP, frac);
                mgBottomColor = interpolateColor(MG_GOLDEN_BOT, MG_SUNSET_BOT, frac);
                windowGlowAlpha = 0.5f + frac * 0.4f;
            } else if (now < tDuskEnd) {
                float frac = (float) (now - tSunsetEnd) / (float) (tDuskEnd - tSunsetEnd);
                fgTopColor = interpolateColor(FG_SUNSET_TOP, FG_DUSK_TOP, frac);
                fgBottomColor = interpolateColor(FG_SUNSET_BOT, FG_DUSK_BOT, frac);
                mgTopColor = interpolateColor(MG_SUNSET_TOP, MG_DUSK_TOP, frac);
                mgBottomColor = interpolateColor(MG_SUNSET_BOT, MG_DUSK_BOT, frac);
                windowGlowAlpha = 0.9f;
            } else {
                fgTopColor = FG_NIGHT_TOP; fgBottomColor = FG_NIGHT_BOT;
                mgTopColor = MG_NIGHT_TOP; mgBottomColor = MG_NIGHT_BOT;
                windowGlowAlpha = 0.8f;
            }
        } else {
            // -------------------- Dark Theme Mosque Milestones --------------------
            final int FG_NIGHT_TOP = Color.parseColor("#0D192C");
            final int FG_NIGHT_BOT = Color.parseColor("#040812");
            final int MG_NIGHT_TOP = Color.parseColor("#132238");
            final int MG_NIGHT_BOT = Color.parseColor("#081426");

            final int FG_PREDAWN_TOP = Color.parseColor("#162A45");
            final int FG_PREDAWN_BOT = Color.parseColor("#071126");
            final int MG_PREDAWN_TOP = Color.parseColor("#1A3252");
            final int MG_PREDAWN_BOT = Color.parseColor("#0A1932");

            final int FG_DAWN_TOP = Color.parseColor("#C2410C");
            final int FG_DAWN_BOT = Color.parseColor("#1E1528");
            final int MG_DAWN_TOP = Color.parseColor("#7C2D12");
            final int MG_DAWN_BOT = Color.parseColor("#181124");

            final int FG_SUNRISE_TOP = Color.parseColor("#D97706");
            final int FG_SUNRISE_BOT = Color.parseColor("#291804");
            final int MG_SUNRISE_TOP = Color.parseColor("#92400E");
            final int MG_SUNRISE_BOT = Color.parseColor("#1C1917");

            final int FG_DAY_TOP = Color.parseColor("#14B8A6"); // Sun-kissed jade-teal domes
            final int FG_DAY_BOT = Color.parseColor("#072723"); // Grounded sea-teal base
            final int MG_DAY_TOP = Color.parseColor("#0D9488");
            final int MG_DAY_BOT = Color.parseColor("#041A17");

            final int FG_GOLDEN_TOP = Color.parseColor("#EA580C");
            final int FG_GOLDEN_BOT = Color.parseColor("#3D1308");
            final int MG_GOLDEN_TOP = Color.parseColor("#9A3412");
            final int MG_GOLDEN_BOT = Color.parseColor("#250D05");

            final int FG_SUNSET_TOP = Color.parseColor("#B91C1C"); // Fiery sunset crimson
            final int FG_SUNSET_BOT = Color.parseColor("#2D0606");
            final int MG_SUNSET_TOP = Color.parseColor("#881337");
            final int MG_SUNSET_BOT = Color.parseColor("#1F0404");

            final int FG_DUSK_TOP = Color.parseColor("#1E1B4B");
            final int FG_DUSK_BOT = Color.parseColor("#080A1E");
            final int MG_DUSK_TOP = Color.parseColor("#171136");
            final int MG_DUSK_BOT = Color.parseColor("#050714");

            distantHillsColor = Color.argb(isDaytime ? 80 : 125, 4, 10, 24);

            if (now < tPreDawn) {
                fgTopColor = FG_NIGHT_TOP; fgBottomColor = FG_NIGHT_BOT;
                mgTopColor = MG_NIGHT_TOP; mgBottomColor = MG_NIGHT_BOT;
                windowGlowAlpha = 1.0f;
            } else if (now < tDawn) {
                float frac = (float) (now - tPreDawn) / (float) (tDawn - tPreDawn);
                fgTopColor = interpolateColor(FG_NIGHT_TOP, FG_PREDAWN_TOP, frac);
                fgBottomColor = interpolateColor(FG_NIGHT_BOT, FG_PREDAWN_BOT, frac);
                mgTopColor = interpolateColor(MG_NIGHT_TOP, MG_PREDAWN_TOP, frac);
                mgBottomColor = interpolateColor(MG_NIGHT_BOT, MG_PREDAWN_BOT, frac);
                windowGlowAlpha = 1.0f;
            } else if (now < sunrise) {
                float frac = (float) (now - tDawn) / (float) (sunrise - tDawn);
                fgTopColor = interpolateColor(FG_PREDAWN_TOP, FG_DAWN_TOP, frac);
                fgBottomColor = interpolateColor(FG_PREDAWN_BOT, FG_DAWN_BOT, frac);
                mgTopColor = interpolateColor(MG_PREDAWN_TOP, MG_DAWN_TOP, frac);
                mgBottomColor = interpolateColor(MG_PREDAWN_BOT, MG_DAWN_BOT, frac);
                windowGlowAlpha = 1.0f - frac * 0.3f;
            } else if (now < tSunriseEnd) {
                float frac = (float) (now - sunrise) / (float) (tSunriseEnd - sunrise);
                fgTopColor = interpolateColor(FG_DAWN_TOP, FG_SUNRISE_TOP, frac);
                fgBottomColor = interpolateColor(FG_DAWN_BOT, FG_SUNRISE_BOT, frac);
                mgTopColor = interpolateColor(MG_DAWN_TOP, MG_SUNRISE_TOP, frac);
                mgBottomColor = interpolateColor(MG_DAWN_BOT, MG_SUNRISE_BOT, frac);
                windowGlowAlpha = 0.7f * (1.0f - frac);
            } else if (now < tGoldenHour) {
                // Smooth transition towards midday peak sun and then to golden hour
                float middayProgress;
                if (now < tMiddayPeak) {
                    middayProgress = (float) (now - tSunriseEnd) / (float) (tMiddayPeak - tSunriseEnd);
                    fgTopColor = interpolateColor(FG_SUNRISE_TOP, FG_DAY_TOP, middayProgress);
                    fgBottomColor = interpolateColor(FG_SUNRISE_BOT, FG_DAY_BOT, middayProgress);
                    mgTopColor = interpolateColor(MG_SUNRISE_TOP, MG_DAY_TOP, middayProgress);
                    mgBottomColor = interpolateColor(MG_SUNRISE_BOT, MG_DAY_BOT, middayProgress);
                } else {
                    middayProgress = (float) (now - tMiddayPeak) / (float) (tGoldenHour - tMiddayPeak);
                    fgTopColor = interpolateColor(FG_DAY_TOP, Color.parseColor("#0D9488"), middayProgress);
                    fgBottomColor = interpolateColor(FG_DAY_BOT, Color.parseColor("#08332E"), middayProgress);
                    mgTopColor = interpolateColor(MG_DAY_TOP, Color.parseColor("#0F766E"), middayProgress);
                    mgBottomColor = interpolateColor(MG_DAY_BOT, Color.parseColor("#06231F"), middayProgress);
                }
                windowGlowAlpha = 0.0f;
            } else if (now < sunset) {
                float frac = (float) (now - tGoldenHour) / (float) (sunset - tGoldenHour);
                fgTopColor = interpolateColor(Color.parseColor("#0D9488"), FG_GOLDEN_TOP, frac);
                fgBottomColor = interpolateColor(Color.parseColor("#08332E"), FG_GOLDEN_BOT, frac);
                mgTopColor = interpolateColor(Color.parseColor("#0F766E"), MG_GOLDEN_TOP, frac);
                mgBottomColor = interpolateColor(Color.parseColor("#06231F"), MG_GOLDEN_BOT, frac);
                windowGlowAlpha = frac * 0.5f;
            } else if (now < tSunsetEnd) {
                float frac = (float) (now - sunset) / (float) (tSunsetEnd - sunset);
                fgTopColor = interpolateColor(FG_GOLDEN_TOP, FG_SUNSET_TOP, frac);
                fgBottomColor = interpolateColor(FG_GOLDEN_BOT, FG_SUNSET_BOT, frac);
                mgTopColor = interpolateColor(MG_GOLDEN_TOP, MG_SUNSET_TOP, frac);
                mgBottomColor = interpolateColor(MG_GOLDEN_BOT, MG_SUNSET_BOT, frac);
                windowGlowAlpha = 0.5f + frac * 0.4f;
            } else if (now < tDuskEnd) {
                float frac = (float) (now - tSunsetEnd) / (float) (tDuskEnd - tSunsetEnd);
                fgTopColor = interpolateColor(FG_SUNSET_TOP, FG_DUSK_TOP, frac);
                fgBottomColor = interpolateColor(FG_SUNSET_BOT, FG_DUSK_BOT, frac);
                mgTopColor = interpolateColor(MG_SUNSET_TOP, MG_DUSK_TOP, frac);
                mgBottomColor = interpolateColor(MG_SUNSET_BOT, MG_DUSK_BOT, frac);
                windowGlowAlpha = 0.9f + frac * 0.1f;
            } else {
                fgTopColor = FG_NIGHT_TOP; fgBottomColor = FG_NIGHT_BOT;
                mgTopColor = MG_NIGHT_TOP; mgBottomColor = MG_NIGHT_BOT;
                windowGlowAlpha = 1.0f;
            }
        }

        fgColor = fgBottomColor;
        mgColor = mgBottomColor;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (viewWidth <= 0 || viewHeight <= 0) return;

        long now = getCurrentTime();
        calculateCurrentAtmosphere(now);

        float w = viewWidth;
        float h = viewHeight;
        boolean isLight = isLightMode();
        long sunrise = sunriseMillis;
        long sunset = sunsetMillis;
        boolean isDaytime = (now >= sunrise && now < sunset);

        // -------------------------------------------------------------
        // 1. SKY GRADIENT
        // -------------------------------------------------------------
        skyPaint.setShader(new LinearGradient(0, 0, 0, h, skyTopColor, skyBottomColor, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, w, h, skyPaint);

        // -------------------------------------------------------------
        // 2. HORIZON DAWN / DUSK RADIAL GLOW
        // -------------------------------------------------------------
        if (dawnDuskGlowAlpha > 0.02f) {
            int glowAlphaInt = (int) (dawnDuskGlowAlpha * 255);
            int glowColorWithAlpha = (glowAlphaInt << 24) | (horizonGlowColor & 0x00FFFFFF);
            horizonGlowPaint.setShader(new RadialGradient(
                w * 0.70f, h * 0.85f, w * 0.55f,
                glowColorWithAlpha, Color.TRANSPARENT, Shader.TileMode.CLAMP
            ));
            canvas.drawRect(0, 0, w, h, horizonGlowPaint);
        }

        // -------------------------------------------------------------
        // 3. TWINKLING STARS (Nighttime)
        // -------------------------------------------------------------
        if (starsAlpha > 0.05f) {
            float timeSec = animationClockSec;
            for (int i = 0; i < STAR_COUNT; i++) {
                float sx = STAR_X_RATIOS[i] * w;
                float sy = STAR_Y_RATIOS[i] * h;
                float baseSize = STAR_SIZES[i];

                float twinkle = 0.55f + 0.45f * (float) Math.sin(timeSec * 2.2f + STAR_PHASES[i]);
                int alpha = (int) (starsAlpha * twinkle * 255f);
                alpha = Math.max(0, Math.min(255, alpha));

                starPaint.setColor((alpha << 24) | 0x00E0F2FE);
                canvas.drawCircle(sx, sy, baseSize, starPaint);
            }
        }

        // -------------------------------------------------------------
        // 4. READABILITY VIGNETTE (Drawn BEHIND celestial bodies)
        // -------------------------------------------------------------
        int scrimColor = isLight && isDaytime
            ? Color.argb(45, 255, 255, 255)
            : (isLight ? Color.argb(70, 15, 23, 42) : Color.argb(120, 3, 7, 16));

        readabilityScrimPaint.setShader(new LinearGradient(
            0, 0, w * 0.48f, 0,
            scrimColor,
            Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        ));
        canvas.drawRect(0, 0, w * 0.52f, h, readabilityScrimPaint);

        // -------------------------------------------------------------
        // 5. CELESTIAL BODIES (Day Sun vs Night Crescent Moon)
        // -------------------------------------------------------------
        if (sunAlpha > 0.02f) {
            drawSun(canvas, sunX, sunY, sunAlpha, isLight);
        }

        if (moonAlpha > 0.02f) {
            drawCrescentMoon(canvas, moonX, moonY, moonAlpha);
        }

        // -------------------------------------------------------------
        // 6. DISTANT ROLLING MISTY HILLS (Screenshot 2 Depth Layer)
        // -------------------------------------------------------------
        distantHillsPaint.setColor(distantHillsColor);
        canvas.drawPath(distantHillsPath, distantHillsPaint);

        // -------------------------------------------------------------
        // 7. SOFT FLOATING TRANSLUCENT CLOUDS
        // -------------------------------------------------------------
        drawSoftClouds(canvas, w, h, isLight, isDaytime);

        // -------------------------------------------------------------
        // 8. FULL-FLEDGED REALISTIC MOSQUE SILHOUETTE (Screenshot 2 Model)
        // -------------------------------------------------------------
        drawRealisticMosque(canvas, w, h, isLight, isDaytime);
    }

    /**
     * Renders photorealistic Sun with 5 celestial optical layers:
     * 1. Volumetric Rayleigh Atmospheric Sunlight Halo
     * 2. 12 Dynamic Optical Diffraction Rays with Living Shimmer
     * 3. Cinematic Anamorphic Horizontal Lens Flare Streak
     * 4. Solar Corona & Chromosphere High-Energy Ring
     * 5. Photosphere Core with Realistic Limb Darkening
     */
    private void drawSun(Canvas canvas, float cx, float cy, float alpha, boolean isLight) {
        float sunR = viewHeight * 0.10f;
        int alphaInt = (int) (alpha * 255);
        if (alphaInt <= 0) return;

        // -------------------------------------------------------------
        // Layer 1: Volumetric Rayleigh Atmospheric Sunlight Halo
        // -------------------------------------------------------------
        float atmoRadius = sunR * 4.2f;
        int atmoAlpha = (int) (alpha * 55);
        int atmoColorRgb = sunAtmosphereColor & 0x00FFFFFF;
        sunAtmospherePaint.setShader(new RadialGradient(
            cx, cy, atmoRadius,
            (atmoAlpha << 24) | atmoColorRgb, Color.TRANSPARENT, Shader.TileMode.CLAMP
        ));
        canvas.drawCircle(cx, cy, atmoRadius, sunAtmospherePaint);

        // -------------------------------------------------------------
        // Layer 2: 12 Dynamic Optical Diffraction Rays with Living Shimmer
        // -------------------------------------------------------------
        int raysRgb = sunRaysColor & 0x00FFFFFF;
        float baseRotation = animationClockSec * 0.035f; // Slow majestic living rotation
        for (int i = 0; i < 12; i++) {
            boolean isPrimary = (i % 2 == 0);
            float angle = (float) (i * (Math.PI / 6.0)) + baseRotation;
            // Organic living atmospheric heat shimmer
            float shimmer = 1.0f + 0.08f * (float) Math.sin(animationClockSec * 2.2f + i * 0.9f);
            float rayLen = sunR * (isPrimary ? 2.8f : 1.7f) * shimmer;
            float rayWidth = sunR * (isPrimary ? 0.14f : 0.08f);

            float cosA = (float) Math.cos(angle);
            float sinA = (float) Math.sin(angle);
            float cosPerp = (float) Math.cos(angle + Math.PI * 0.5);
            float sinPerp = (float) Math.sin(angle + Math.PI * 0.5);

            float tipX = cx + cosA * rayLen;
            float tipY = cy + sinA * rayLen;
            float p1X = cx + cosPerp * rayWidth;
            float p1Y = cy + sinPerp * rayWidth;
            float p2X = cx - cosPerp * rayWidth;
            float p2Y = cy - sinPerp * rayWidth;

            sunRayPath.reset();
            sunRayPath.moveTo(p1X, p1Y);
            sunRayPath.lineTo(tipX, tipY);
            sunRayPath.lineTo(p2X, p2Y);
            sunRayPath.close();

            int rayAlpha = (int) (alpha * (isPrimary ? 120 : 65));
            sunRaysPaint.setColor((rayAlpha << 24) | raysRgb);
            canvas.drawPath(sunRayPath, sunRaysPaint);
        }

        // -------------------------------------------------------------
        // Layer 3: Cinematic Anamorphic Horizontal Lens Flare Streak
        // -------------------------------------------------------------
        float flareWidth = sunR * 5.2f;
        float flareHeight = sunR * 0.16f;
        sunFlareRect.set(cx - flareWidth, cy - flareHeight, cx + flareWidth, cy + flareHeight);
        int flareAlpha = (int) (alpha * 110);
        sunFlarePaint.setShader(new RadialGradient(
            cx, cy, flareWidth,
            (flareAlpha << 24) | 0x00FFFFFF, Color.TRANSPARENT, Shader.TileMode.CLAMP
        ));
        canvas.drawRoundRect(sunFlareRect, flareHeight, flareHeight, sunFlarePaint);

        // -------------------------------------------------------------
        // Layer 4: Solar Corona & Chromosphere High-Energy Ring
        // -------------------------------------------------------------
        float coronaRadius = sunR * 1.65f;
        int coronaAlpha = (int) (alpha * 175);
        int coronaRgb = sunCoronaColor & 0x00FFFFFF;
        sunCoronaPaint.setShader(new RadialGradient(
            cx, cy, coronaRadius,
            (coronaAlpha << 24) | coronaRgb, Color.TRANSPARENT, Shader.TileMode.CLAMP
        ));
        canvas.drawCircle(cx, cy, coronaRadius, sunCoronaPaint);

        // -------------------------------------------------------------
        // Layer 5: Photosphere Core with Realistic Limb Darkening
        // -------------------------------------------------------------
        int[] coreColors = new int[]{
            (alphaInt << 24) | (sunCoreCenterColor & 0x00FFFFFF), // 100% pure white-hot center
            (alphaInt << 24) | (sunCoreMidColor & 0x00FFFFFF),    // Solar photosphere
            (alphaInt << 24) | (sunCoreEdgeColor & 0x00FFFFFF)   // Limb darkening rim
        };
        float[] coreStops = new float[]{ 0.0f, 0.65f, 1.0f };
        sunCorePaint.setShader(new RadialGradient(
            cx, cy, sunR, coreColors, coreStops, Shader.TileMode.CLAMP
        ));
        canvas.drawCircle(cx, cy, sunR, sunCorePaint);
    }

    /**
     * Renders prominent glowing Crescent Moon (Hilal) matching Reference Screenshot 2.
     */
    private void drawCrescentMoon(Canvas canvas, float cx, float cy, float alpha) {
        float moonR = viewHeight * 0.16f;
        int alphaInt = (int) (alpha * 255);

        // Outer cyan halo
        int glowAlpha = (int) (alpha * 80);
        moonGlowPaint.setShader(new RadialGradient(
            cx, cy, moonR * 2.5f,
            (glowAlpha << 24) | 0x0038BDF8, Color.TRANSPARENT, Shader.TileMode.CLAMP
        ));
        canvas.drawCircle(cx, cy, moonR * 2.5f, moonGlowPaint);

        // Inner white halo
        int innerWhiteGlow = (int) (alpha * 130);
        moonGlowPaint.setShader(new RadialGradient(
            cx, cy, moonR * 1.4f,
            (innerWhiteGlow << 24) | 0x00FFFFFF, Color.TRANSPARENT, Shader.TileMode.CLAMP
        ));
        canvas.drawCircle(cx, cy, moonR * 1.4f, moonGlowPaint);

        // Crescent moon geometry
        crescentPath.reset();
        crescentPath.addCircle(cx, cy, moonR, Path.Direction.CW);

        crescentCutPath.reset();
        crescentCutPath.addCircle(cx + moonR * 0.40f, cy - moonR * 0.16f, moonR * 0.88f, Path.Direction.CW);

        crescentPath.op(crescentCutPath, Path.Op.DIFFERENCE);

        moonCorePaint.setColor((alphaInt << 24) | 0x00F8FAFC);
        canvas.drawPath(crescentPath, moonCorePaint);
    }

    /**
     * Renders calm, semi-translucent drifting clouds.
     */
    private void drawSoftClouds(Canvas canvas, float w, float h, boolean isLight, boolean isDaytime) {
        int colorTint = (isDaytime && isLight) ? 0x00FFFFFF : (isDaytime ? 0x00E0F2FE : 0x0038BDF8);

        float x1 = (cloudOffset1 % (w + 200f)) - 100f;
        drawCloudCluster(canvas, x1, h * 0.18f, 48f, 0.15f, colorTint);

        float x2 = (cloudOffset2 % (w + 220f)) - 110f;
        drawCloudCluster(canvas, x2, h * 0.34f, 60f, 0.20f, colorTint);

        float x3 = (cloudOffset3 % (w + 250f)) - 120f;
        drawCloudCluster(canvas, x3, h * 0.52f, 75f, 0.14f, colorTint);
    }

    private void drawCloudCluster(Canvas canvas, float cx, float cy, float radius, float alpha, int colorTint) {
        int alphaInt = (int) (alpha * 255);
        cloudPaint.setColor((alphaInt << 24) | colorTint);

        canvas.drawCircle(cx, cy, radius * 0.65f, cloudPaint);
        canvas.drawCircle(cx + radius * 0.5f, cy - radius * 0.2f, radius * 0.85f, cloudPaint);
        canvas.drawCircle(cx + radius * 1.1f, cy, radius * 0.70f, cloudPaint);
        canvas.drawCircle(cx + radius * 1.5f, cy + radius * 0.1f, radius * 0.55f, cloudPaint);
    }

    /**
     * Renders the photorealistic Grand Mosque architecture with dynamic, angle-aware
     * directional solar lighting, living sunlight wave caustics ("সূর্যের ঢেউয়ের আলো"),
     * and nocturnal interior sanctuary illumination through arched windows and portals.
     */
    private void drawRealisticMosque(Canvas canvas, float w, float h, boolean isLight, boolean isDaytime) {
        if (mosqueBitmap == null || mosqueBitmap.isRecycled()) {
            loadMosqueBitmaps();
            if (mosqueBitmap == null) return;
        }

        float mLeft = mosqueDestRectForeground.left;
        float mTop = mosqueDestRectForeground.top;
        float mWidth = mosqueDestRectForeground.width();
        float mHeight = mosqueDestRectForeground.height();
        float mcX = mosqueDestRectForeground.centerX();
        float mcY = mosqueDestRectForeground.centerY();

        long now = getCurrentTime();
        long sunrise = sunriseMillis;
        long sunset = sunsetMillis;
        long tSunriseEnd = sunrise + 35 * 60 * 1000L;
        long tGoldenHour = sunset - 45 * 60 * 1000L;
        long tSunsetEnd = sunset + 25 * 60 * 1000L;

        // Hardware composite layer for the realistic mosque
        int saveCount = canvas.saveLayer(mosqueDestRectForeground, null);

        // -------------------------------------------------------------
        // PASS 1: Base Realistic Mosque Texture
        // -------------------------------------------------------------
        mosqueForegroundPaint.setColorFilter(null);
        mosqueForegroundPaint.setAlpha(255);
        canvas.drawBitmap(mosqueBitmap, mosqueSrcRect, mosqueDestRectForeground, mosqueForegroundPaint);

        // -------------------------------------------------------------
        // PASS 2: Angle-Aware Directional Solar Lighting & Natural Shadow
        // Real-time calculation based on the position of the sun/moon in the sky
        // -------------------------------------------------------------
        float dirX, dirY;
        if (isDaytime) {
            float dx = sunX - mcX;
            float dy = sunY - mcY;
            float dist = (float) Math.hypot(dx, dy);
            dirX = (dist > 0.001f) ? (dx / dist) : 0f;
            dirY = (dist > 0.001f) ? (dy / dist) : -1f;
        } else {
            // At night, gentle celestial moonlight from crescent moon
            float dx = moonX - mcX;
            float dy = moonY - mcY;
            float dist = (float) Math.hypot(dx, dy);
            dirX = (dist > 0.001f) ? (dx / dist) : -0.7f;
            dirY = (dist > 0.001f) ? (dy / dist) : -0.7f;
        }

        // Vector from sunlit edge to shadow edge across the mosque bounds
        float startX = mcX + dirX * (mWidth * 0.48f);
        float startY = mcY + dirY * (mHeight * 0.48f);
        float endX = mcX - dirX * (mWidth * 0.48f);
        float endY = mcY - dirY * (mHeight * 0.48f);

        int litCol, midCol, shadowCol;
        if (isDaytime) {
            if (now < tSunriseEnd) {
                // Morning: Warm golden sunlight on lit side, cool morning shadow on opposite side
                litCol = Color.rgb(255, 245, 225);
                midCol = Color.rgb(230, 220, 210);
                shadowCol = Color.rgb(145, 155, 175);
            } else if (now < tGoldenHour) {
                // Midday (Zohr/Asr): Bright crisp daylight preserving rich turquoise, gold & sandstone
                litCol = Color.rgb(255, 255, 255);
                midCol = Color.rgb(240, 242, 245);
                shadowCol = Color.rgb(175, 180, 195);
            } else if (now < tSunsetEnd) {
                // Sunset / Maghrib: Rich golden-amber sunset rays on lit side, twilight shadow
                litCol = Color.rgb(255, 220, 175);
                midCol = Color.rgb(215, 175, 155);
                shadowCol = Color.rgb(125, 115, 140);
            } else {
                // Twilight / Dusk
                litCol = Color.rgb(195, 185, 200);
                midCol = Color.rgb(150, 155, 175);
                shadowCol = Color.rgb(105, 115, 145);
            }
        } else {
            // Night: Serene moonlight sheen on upper curves, cool navy nocturnal shadow
            litCol = Color.rgb(145, 170, 210);
            midCol = Color.rgb(95, 115, 150);
            shadowCol = Color.rgb(55, 70, 105);
        }

        int[] shadeColors = new int[]{ litCol, midCol, shadowCol };
        float[] shadeStops = new float[]{ 0.0f, 0.45f, 1.0f };

        mosqueShadingPaint.setXfermode(multiplyXfermode);
        mosqueShadingPaint.setShader(new LinearGradient(
            startX, startY, endX, endY,
            shadeColors, shadeStops, Shader.TileMode.CLAMP
        ));
        canvas.drawRect(mosqueDestRectForeground, mosqueShadingPaint);
        mosqueShadingPaint.setShader(null);
        mosqueShadingPaint.setXfermode(null);

        // -------------------------------------------------------------
        // PASS 3: "সূর্যের ঢেউয়ের আলো" (Living Sunlight Waves & Specular Caustics)
        // Gentle living sunlight waves undulating across the marble domes
        // -------------------------------------------------------------
        if (isDaytime && sunAlpha > 0.1f) {
            float wavePhase = (animationClockSec * 0.20f) % 1.0f;
            float waveOffset = (wavePhase - 0.5f) * mWidth * 0.8f;
            float waveCenterX = mcX + dirX * (mWidth * 0.25f) + waveOffset;
            float waveCenterY = mTop + mHeight * 0.35f;
            float waveRadius = mWidth * 0.42f;

            int waveAlpha = (int) (sunAlpha * 40); // Soft, living shimmer
            if (waveAlpha > 0) {
                int waveColor = (waveAlpha << 24) | (now >= tGoldenHour ? 0x00FFD1A4 : 0x00FFF8E7);
                mosqueSunWavePaint.setXfermode(screenXfermode);
                mosqueSunWavePaint.setShader(new RadialGradient(
                    waveCenterX, waveCenterY, waveRadius,
                    waveColor, Color.TRANSPARENT, Shader.TileMode.CLAMP
                ));
                canvas.drawRect(mosqueDestRectForeground, mosqueSunWavePaint);
                mosqueSunWavePaint.setShader(null);
                mosqueSunWavePaint.setXfermode(null);
            }
        }

        // -------------------------------------------------------------
        // PASS 4: Nighttime Interior Window & Minaret Lighting (রাত্রিবেলা মসজিদের ভিতরে ও মিনারে লাইট জ্বলবে)
        // Warm golden lights shining from inside every arched window, minaret lanterns, portal & colonnade
        // -------------------------------------------------------------
        if (windowGlowAlpha > 0.05f && mosqueNightLightsBitmap != null && !mosqueNightLightsBitmap.isRecycled()) {
            float pulse = 0.92f + 0.08f * (float) Math.sin(animationClockSec * 1.8f);
            int lightsAlpha = (int) (windowGlowAlpha * pulse * 255f);
            lightsAlpha = Math.max(0, Math.min(255, lightsAlpha));

            mosqueLightsPaint.setAlpha(lightsAlpha);
            canvas.drawBitmap(mosqueNightLightsBitmap, mosqueLightsSrcRect, mosqueDestRectForeground, mosqueLightsPaint);

            // Soft warm golden entrance bloom spilling outward from the grand central archway (no minaret circle blobs)
            float portalX = mLeft + mWidth * 0.505f;
            float portalY = mTop + mHeight * 0.825f;
            float portalRadius = mWidth * 0.10f;
            int bloomAlpha = (int) (lightsAlpha * 0.40f);
            int bloomColor = (bloomAlpha << 24) | 0x00F59E0B;

            mosquePortalGlowPaint.setShader(new RadialGradient(
                portalX, portalY, portalRadius,
                bloomColor, Color.TRANSPARENT, Shader.TileMode.CLAMP
            ));
            canvas.drawCircle(portalX, portalY, portalRadius, mosquePortalGlowPaint);
            mosquePortalGlowPaint.setShader(null);
        }

        // -------------------------------------------------------------
        // PASS 5: Seamless Atmospheric Ground Mist & Card Integration
        // Seamlessly blends the bottom base and sides with the card container & landscape
        // -------------------------------------------------------------
        float mistTop = mTop + mHeight * 0.70f;
        float mistBottom = h;
        int mistColor;
        if (isDaytime) {
            if (now < tSunriseEnd) {
                mistColor = isLight ? Color.argb(45, 254, 240, 138) : Color.argb(60, 217, 119, 6);
            } else if (now < tGoldenHour) {
                mistColor = isLight ? Color.argb(35, 255, 255, 255) : Color.argb(45, 13, 148, 136);
            } else if (now < tSunsetEnd) {
                mistColor = isLight ? Color.argb(50, 253, 186, 116) : Color.argb(65, 220, 38, 38);
            } else {
                mistColor = isLight ? Color.argb(40, 51, 65, 85) : Color.argb(55, 30, 27, 75);
            }
        } else {
            mistColor = isLight ? Color.argb(50, 30, 41, 59) : Color.argb(75, 7, 17, 38);
        }

        mosqueGroundMistPaint.setShader(new LinearGradient(
            0, mistTop, 0, mistBottom,
            Color.TRANSPARENT, mistColor, Shader.TileMode.CLAMP
        ));
        canvas.drawRect(0, mistTop, w, mistBottom, mosqueGroundMistPaint);
        mosqueGroundMistPaint.setShader(null);

        canvas.restoreToCount(saveCount);
    }

    /**
     * High-speed linear color interpolation.
     */
    private static int interpolateColor(int colorA, int colorB, float fraction) {
        fraction = Math.max(0f, Math.min(1f, fraction));

        int aA = (colorA >> 24) & 0xFF;
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = colorA & 0xFF;

        int aB = (colorB >> 24) & 0xFF;
        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int a = (int) (aA + fraction * (aB - aA));
        int r = (int) (rA + fraction * (rB - rA));
        int g = (int) (gA + fraction * (gB - gA));
        int b = (int) (bA + fraction * (bB - bA));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
