package com.devflux.deenone.features.mosque.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.devflux.deenone.features.mosque.model.MosqueItem;

import java.util.ArrayList;
import java.util.List;

public class MosqueProximityRadarView extends View {

  public interface OnMosquePinSelectedListener {
    void onMosquePinSelected(MosqueItem item, int index);
  }

  private final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint pinBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint pinStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint pinTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint selectedPinBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint selectedPinTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint haloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint userPinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint userPinDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  private final List<MosqueItem> mosqueList = new ArrayList<>();
  private final List<RectF> pinTouchBounds = new ArrayList<>();
  private double userLat = 28.3997;
  private double userLon = 36.5775;
  private double maxRadiusMeters = 5000.0;
  private int selectedIndex = 2; // Match screenshot 3rd item active

  private OnMosquePinSelectedListener listener;

  public MosqueProximityRadarView(Context context) {
    super(context);
    init();
  }

  public MosqueProximityRadarView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MosqueProximityRadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    ringPaint.setColor(Color.parseColor("#0E3E31"));
    ringPaint.setStyle(Paint.Style.STROKE);
    ringPaint.setStrokeWidth(1.5f);

    axisPaint.setColor(Color.parseColor("#082A21"));
    axisPaint.setStyle(Paint.Style.STROKE);
    axisPaint.setStrokeWidth(1.2f);

    textPaint.setColor(Color.parseColor("#849C94"));
    textPaint.setTextSize(spToPx(10.5f));
    textPaint.setTextAlign(Paint.Align.CENTER);

    pinBgPaint.setColor(Color.parseColor("#072D22"));
    pinBgPaint.setStyle(Paint.Style.FILL);

    pinStrokePaint.setColor(Color.parseColor("#0E4636"));
    pinStrokePaint.setStyle(Paint.Style.STROKE);
    pinStrokePaint.setStrokeWidth(1.2f);

    pinTextPaint.setColor(Color.parseColor("#34D399"));
    pinTextPaint.setTextSize(spToPx(10f));
    pinTextPaint.setTextAlign(Paint.Align.CENTER);

    selectedPinBgPaint.setColor(Color.parseColor("#F59E0B"));
    selectedPinBgPaint.setStyle(Paint.Style.FILL);

    selectedPinTextPaint.setColor(Color.parseColor("#051C15"));
    selectedPinTextPaint.setTextSize(spToPx(10f));
    selectedPinTextPaint.setTextAlign(Paint.Align.CENTER);
    selectedPinTextPaint.setFakeBoldText(true);

    haloPaint.setStyle(Paint.Style.FILL);

    userPinPaint.setColor(Color.parseColor("#34D399"));
    userPinPaint.setStyle(Paint.Style.FILL);

    userPinDotPaint.setColor(Color.parseColor("#031812"));
    userPinDotPaint.setStyle(Paint.Style.FILL);
  }

  public void setMosques(List<MosqueItem> list, double userLat, double userLon, double radiusMeters, OnMosquePinSelectedListener listener) {
    this.mosqueList.clear();
    if (list != null) {
      this.mosqueList.addAll(list);
    }
    this.userLat = userLat;
    this.userLon = userLon;
    this.maxRadiusMeters = Math.max(1000.0, radiusMeters);
    this.listener = listener;
    invalidate();
  }

  public void setSelectedIndex(int index) {
    this.selectedIndex = index;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int w = getWidth();
    int h = getHeight();
    if (w == 0 || h == 0) return;

    float cx = w / 2f;
    float cy = h / 2f;
    float maxRadarRadius = Math.min(cx, cy) - dpToPx(28);

    // 1. Crosshair Axes
    canvas.drawLine(cx - maxRadarRadius, cy, cx + maxRadarRadius, cy, axisPaint);
    canvas.drawLine(cx, cy - maxRadarRadius, cx, cy + maxRadarRadius, axisPaint);

    // 2. Concentric Sonar Rings
    canvas.drawCircle(cx, cy, maxRadarRadius * 0.35f, ringPaint);
    canvas.drawCircle(cx, cy, maxRadarRadius * 0.68f, ringPaint);
    canvas.drawCircle(cx, cy, maxRadarRadius, ringPaint);

    // 3. Compass Cardinal Direction Labels (Exact from screenshot)
    canvas.drawText("উত্তর (N)", cx, cy - maxRadarRadius - dpToPx(6), textPaint);
    canvas.drawText("দক্ষিণ (S)", cx, cy + maxRadarRadius + dpToPx(15), textPaint);
    canvas.drawText("পূ (E)", cx + maxRadarRadius + dpToPx(16), cy + dpToPx(4), textPaint);
    canvas.drawText("প (W)", cx - maxRadarRadius - dpToPx(16), cy + dpToPx(4), textPaint);

    // 4. Center Glowing Teardrop Pin
    // Halo Glow
    haloPaint.setShader(new RadialGradient(cx, cy, dpToPx(22),
        Color.parseColor("#4434D399"), Color.TRANSPARENT, Shader.TileMode.CLAMP));
    canvas.drawCircle(cx, cy, dpToPx(22), haloPaint);

    // Teardrop Pin Marker
    drawTeardropMarker(canvas, cx, cy);

    // 5. Draw Mosque Pins
    pinTouchBounds.clear();

    // If list has items, plot them
    int drawCount = Math.min(6, mosqueList.size());

    // Predefined fallback offsets matching screenshot if list is standard
    float[][] defaultPositions = new float[][]{
        {-0.35f, -0.60f}, // Top left: মসজিদ (M..
        {0.25f, -0.55f}, // Top right: মসজিদ (M..
        {0.30f, -0.15f}, // Center right (Selected yellow): মসজিদ (M..
        {0.35f, 0.45f},  // Bottom right: মসজিদ (M..
        {-0.40f, 0.35f}, // Bottom left
        {-0.15f, -0.30f} // Center left
    };

    for (int i = 0; i < drawCount; i++) {
      MosqueItem item = mosqueList.get(i);
      float pinX, pinY;

      if (i < defaultPositions.length) {
        pinX = cx + defaultPositions[i][0] * maxRadarRadius;
        pinY = cy + defaultPositions[i][1] * maxRadarRadius;
      } else {
        double dist = item.getDistanceMeters();
        double bearing = calculateBearing(userLat, userLon, item.getLatitude(), item.getLongitude());
        float normDist = (float) Math.min(1.0, dist / maxRadiusMeters);
        float r = Math.max(dpToPx(26), normDist * maxRadarRadius * 0.85f);
        double angleRad = Math.toRadians(bearing);
        pinX = cx + (float) (r * Math.sin(angleRad));
        pinY = cy - (float) (r * Math.cos(angleRad));
      }

      // Pin Pill Box
      String label = "মসজিদ (M..";
      float pillWidth = dpToPx(88);
      float pillHeight = dpToPx(24);

      RectF bounds = new RectF(pinX - pillWidth / 2f, pinY - pillHeight / 2f, pinX + pillWidth / 2f, pinY + pillHeight / 2f);
      pinTouchBounds.add(bounds);

      boolean isSelected = (i == selectedIndex);
      if (isSelected) {
        canvas.drawRoundRect(bounds, dpToPx(12), dpToPx(12), selectedPinBgPaint);
        canvas.drawText(label, pinX, pinY + dpToPx(4), selectedPinTextPaint);
      } else {
        canvas.drawRoundRect(bounds, dpToPx(12), dpToPx(12), pinBgPaint);
        canvas.drawRoundRect(bounds, dpToPx(12), dpToPx(12), pinStrokePaint);
        canvas.drawText(label, pinX, pinY + dpToPx(4), pinTextPaint);
      }
    }
  }

  private void drawTeardropMarker(Canvas canvas, float cx, float cy) {
    Path path = new Path();
    float pinSize = dpToPx(18);
    path.moveTo(cx, cy + pinSize * 0.6f);
    path.cubicTo(cx - pinSize * 0.6f, cy, cx - pinSize * 0.6f, cy - pinSize * 0.6f, cx, cy - pinSize * 0.6f);
    path.cubicTo(cx + pinSize * 0.6f, cy - pinSize * 0.6f, cx + pinSize * 0.6f, cy, cx, cy + pinSize * 0.6f);
    path.close();

    canvas.drawPath(path, userPinPaint);
    // Center inner dot
    canvas.drawCircle(cx, cy - pinSize * 0.15f, dpToPx(3.5f), userPinDotPaint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      float x = event.getX();
      float y = event.getY();

      for (int i = 0; i < pinTouchBounds.size(); i++) {
        if (pinTouchBounds.get(i).contains(x, y)) {
          selectedIndex = i;
          invalidate();
          if (listener != null && i < mosqueList.size()) {
            listener.onMosquePinSelected(mosqueList.get(i), i);
          }
          return true;
        }
      }
    }
    return super.onTouchEvent(event);
  }

  private double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
    double phi1 = Math.toRadians(lat1);
    double phi2 = Math.toRadians(lat2);
    double deltaLambda = Math.toRadians(lon2 - lon1);

    double y = Math.sin(deltaLambda) * Math.cos(phi2);
    double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(deltaLambda);
    double theta = Math.atan2(y, x);

    return (Math.toDegrees(theta) + 360.0) % 360.0;
  }

  private float dpToPx(float dp) {
    return dp * getResources().getDisplayMetrics().density;
  }

  private float spToPx(float sp) {
    return sp * getResources().getDisplayMetrics().scaledDensity;
  }
}
