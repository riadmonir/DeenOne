package com.devflux.deenone.features.qibla.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class QiblaCompassView extends View {

  public interface OnQiblaAlignmentListener {
    void onAlignmentStateChanged(boolean isFacingQibla, float deltaDegrees, String instructionText);
  }

  private Paint bgPaint;
  private Paint ringPaint;
  private Paint tickMajorPaint;
  private Paint tickMinorPaint;
  private Paint textRedPaint;
  private Paint textGreenPaint;
  private Paint topIndicatorPaint;
  private Paint centerPivotOuterPaint;
  private Paint centerPivotInnerPaint;
  private Paint needlePaint;
  private Paint needleGlowPaint;
  private Paint kaabaBadgePaint;
  private Paint kaabaIconPaint;

  private float currentAzimuth = 0f;
  private float targetQiblaBearing = 0f;
  private boolean isAligned = false;
  private OnQiblaAlignmentListener alignmentListener;

  private final Path kaabaPath = new Path();

  public QiblaCompassView(Context context) {
    super(context);
    init();
  }

  public QiblaCompassView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public QiblaCompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setLayerType(LAYER_TYPE_SOFTWARE, null); // For smooth shadows and glow effects

    bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bgPaint.setStyle(Paint.Style.FILL);

    ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ringPaint.setStyle(Paint.Style.STROKE);
    ringPaint.setStrokeWidth(3f);
    ringPaint.setColor(Color.parseColor("#0E4434"));

    tickMajorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    tickMajorPaint.setStyle(Paint.Style.STROKE);
    tickMajorPaint.setStrokeWidth(3.5f);
    tickMajorPaint.setColor(Color.parseColor("#34D399"));
    tickMajorPaint.setStrokeCap(Paint.Cap.ROUND);

    tickMinorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    tickMinorPaint.setStyle(Paint.Style.STROKE);
    tickMinorPaint.setStrokeWidth(1.8f);
    tickMinorPaint.setColor(Color.parseColor("#0E4434"));
    tickMinorPaint.setStrokeCap(Paint.Cap.ROUND);

    textRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textRedPaint.setColor(Color.parseColor("#EF4444"));
    textRedPaint.setTextSize(36f);
    textRedPaint.setFakeBoldText(true);
    textRedPaint.setTextAlign(Paint.Align.CENTER);

    textGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textGreenPaint.setColor(Color.parseColor("#6EE7B7"));
    textGreenPaint.setTextSize(32f);
    textGreenPaint.setFakeBoldText(true);
    textGreenPaint.setTextAlign(Paint.Align.CENTER);

    topIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    topIndicatorPaint.setStyle(Paint.Style.FILL);
    topIndicatorPaint.setColor(Color.parseColor("#10B981"));
    topIndicatorPaint.setShadowLayer(8f, 0, 0, Color.parseColor("#34D399"));

    centerPivotOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    centerPivotOuterPaint.setStyle(Paint.Style.FILL);
    centerPivotOuterPaint.setColor(Color.parseColor("#F59E0B"));

    centerPivotInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    centerPivotInnerPaint.setStyle(Paint.Style.FILL);
    centerPivotInnerPaint.setColor(Color.parseColor("#FFFFFF"));

    needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    needlePaint.setStyle(Paint.Style.STROKE);
    needlePaint.setStrokeWidth(6f);
    needlePaint.setColor(Color.parseColor("#F59E0B"));
    needlePaint.setStrokeCap(Paint.Cap.ROUND);

    needleGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    needleGlowPaint.setStyle(Paint.Style.STROKE);
    needleGlowPaint.setStrokeWidth(12f);
    needleGlowPaint.setColor(Color.parseColor("#40F59E0B"));
    needleGlowPaint.setStrokeCap(Paint.Cap.ROUND);

    kaabaBadgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    kaabaBadgePaint.setStyle(Paint.Style.FILL);
    kaabaBadgePaint.setColor(Color.parseColor("#F59E0B"));
    kaabaBadgePaint.setShadowLayer(16f, 0, 0, Color.parseColor("#80F59E0B"));

    kaabaIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    kaabaIconPaint.setStyle(Paint.Style.FILL);
    kaabaIconPaint.setColor(Color.parseColor("#041A14"));
  }

  public void setAlignmentListener(OnQiblaAlignmentListener listener) {
    this.alignmentListener = listener;
  }

  public void setBearing(float azimuth, float qiblaBearing) {
    this.currentAzimuth = azimuth;
    this.targetQiblaBearing = qiblaBearing;

    // Calculate delta (shortest turn angle)
    float diff = (targetQiblaBearing - currentAzimuth + 360f) % 360f;
    if (diff > 180f) diff -= 360f; // Range [-180, +180]

    boolean newlyAligned = Math.abs(diff) <= 3.5f;
    if (newlyAligned != isAligned) {
      isAligned = newlyAligned;
      updatePaintsForAlignment(isAligned);
    }

    if (alignmentListener != null) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(getContext());
      String instruction;
      if (isAligned) {
        instruction = isBn ? "কিবলামুখী (সঠিক দিক)" : "Facing Qibla (Aligned)";
      } else if (diff > 0) {
        int deg = Math.round(diff);
        instruction = isBn ? ("↻ আর " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(deg) + "° ডানে ঘুরুন")
                           : ("↻ Turn " + deg + "° Right");
      } else {
        int deg = Math.round(Math.abs(diff));
        instruction = isBn ? ("↺ আর " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(deg) + "° বামে ঘুরুন")
                           : ("↺ Turn " + deg + "° Left");
      }
      alignmentListener.onAlignmentStateChanged(isAligned, diff, instruction);
    }

    invalidate();
  }

  private void updatePaintsForAlignment(boolean aligned) {
    if (aligned) {
      centerPivotOuterPaint.setColor(Color.parseColor("#10B981"));
      needlePaint.setColor(Color.parseColor("#10B981"));
      needleGlowPaint.setColor(Color.parseColor("#5010B981"));
      kaabaBadgePaint.setColor(Color.parseColor("#10B981"));
      kaabaBadgePaint.setShadowLayer(20f, 0, 0, Color.parseColor("#10B981"));
    } else {
      centerPivotOuterPaint.setColor(Color.parseColor("#F59E0B"));
      needlePaint.setColor(Color.parseColor("#F59E0B"));
      needleGlowPaint.setColor(Color.parseColor("#40F59E0B"));
      kaabaBadgePaint.setColor(Color.parseColor("#F59E0B"));
      kaabaBadgePaint.setShadowLayer(16f, 0, 0, Color.parseColor("#80F59E0B"));
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int width = getWidth();
    int height = getHeight();
    float cx = width / 2f;
    float cy = height / 2f;
    float radius = Math.min(cx, cy) - 24f;

    if (radius <= 0) return;

    // 1. Background Radial Gradient Disc
    RadialGradient bgGradient = new RadialGradient(
        cx, cy, radius,
        new int[]{Color.parseColor("#041F16"), Color.parseColor("#02120D")},
        new float[]{0.3f, 1.0f},
        Shader.TileMode.CLAMP
    );
    bgPaint.setShader(bgGradient);
    canvas.drawCircle(cx, cy, radius, bgPaint);

    // 2. Outer Ring
    canvas.drawCircle(cx, cy, radius, ringPaint);
    canvas.drawCircle(cx, cy, radius * 0.88f, ringPaint);

    // 3. Draw Rotating Compass Dial (ticks & cardinal directions)
    canvas.save();
    // Dial rotates with device azimuth so North stays pointing to physical North
    canvas.rotate(-currentAzimuth, cx, cy);

    // Draw 360-degree ticks
    for (int i = 0; i < 360; i += 10) {
      canvas.save();
      canvas.rotate(i, cx, cy);
      if (i % 90 == 0) {
        // Cardinal ticks
        canvas.drawLine(cx, cy - radius + 4f, cx, cy - radius + 24f, tickMajorPaint);
      } else if (i % 30 == 0) {
        // Major 30-degree ticks
        canvas.drawLine(cx, cy - radius + 4f, cx, cy - radius + 18f, tickMajorPaint);
      } else {
        // Minor 10-degree ticks
        canvas.drawLine(cx, cy - radius + 4f, cx, cy - radius + 12f, tickMinorPaint);
      }
      canvas.restore();
    }

    // Draw Cardinal Letters (N, E, S, W)
    // North 'N' (Red)
    drawRotatedText(canvas, "N", textRedPaint, 0, cx, cy);
    // East 'E' (Mint)
    drawRotatedText(canvas, "E", textGreenPaint, 90, cx, cy);
    // South 'S' (Mint)
    drawRotatedText(canvas, "S", textGreenPaint, 180, cx, cy);
    // West 'W' (Mint)
    drawRotatedText(canvas, "W", textGreenPaint, 270, cx, cy);

    canvas.restore(); // Restore dial rotation

    // 4. Fixed Top Indicator Dot (12 o'clock heading marker)
    canvas.drawCircle(cx, cy - radius - 8f, 10f, topIndicatorPaint);

    // 5. Draw Qibla Needle (Rotates towards relative Qibla angle)
    canvas.save();
    float relativeQiblaAngle = targetQiblaBearing - currentAzimuth;
    canvas.rotate(relativeQiblaAngle, cx, cy);

    float needleLength = radius * 0.68f;

    // Draw Needle Glow & Line from center to Kaaba badge
    canvas.drawLine(cx, cy, cx, cy - needleLength, needleGlowPaint);
    canvas.drawLine(cx, cy, cx, cy - needleLength, needlePaint);

    // Draw Kaaba Badge at tip
    float badgeRadius = 26f;
    float badgeCenterY = cy - needleLength;
    canvas.drawCircle(cx, badgeCenterY, badgeRadius, kaabaBadgePaint);

    // Draw Mosque/Kaaba Silhouette on badge
    drawMosqueIcon(canvas, cx, badgeCenterY, badgeRadius * 0.55f);

    canvas.restore(); // Restore needle rotation

    // 6. Center Pivot (Amber circle + White center dot)
    canvas.drawCircle(cx, cy, 14f, centerPivotOuterPaint);
    canvas.drawCircle(cx, cy, 6f, centerPivotInnerPaint);
  }

  private void drawRotatedText(Canvas canvas, String text, Paint paint, float angle, float cx, float cy) {
    canvas.save();
    canvas.rotate(angle, cx, cy);
    canvas.drawText(text, cx, cy - (Math.min(cx, cy) - 24f) * 0.72f, paint);
    canvas.restore();
  }

  private void drawMosqueIcon(Canvas canvas, float cx, float cy, float size) {
    kaabaPath.reset();
    // Dome and minaret silhouette
    float top = cy - size;
    float bottom = cy + size * 0.8f;
    float left = cx - size * 0.75f;
    float right = cx + size * 0.75f;

    // Base building
    kaabaPath.moveTo(left, bottom);
    kaabaPath.lineTo(right, bottom);
    kaabaPath.lineTo(right, cy - size * 0.2f);
    // Center dome curve
    kaabaPath.quadTo(cx, top - size * 0.4f, left, cy - size * 0.2f);
    kaabaPath.close();

    // Small door arch
    float doorWidth = size * 0.35f;
    float doorHeight = size * 0.45f;
    kaabaPath.addRect(cx - doorWidth / 2f, bottom - doorHeight, cx + doorWidth / 2f, bottom, Path.Direction.CW);

    canvas.drawPath(kaabaPath, kaabaIconPaint);
  }
}
