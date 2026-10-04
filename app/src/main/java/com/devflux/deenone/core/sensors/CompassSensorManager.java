package com.devflux.deenone.core.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassSensorManager implements SensorEventListener {

    public interface CompassListener {
        void onCompassRotationChanged(float azimuthDegrees, float qiblaBearing);
    }

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Sensor magnetometer;
    private CompassListener listener;

    private final float[] gravity = new float[3];
    private final float[] geomagnetic = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientation = new float[3];

    private float currentAzimuth = 0f;
    private float targetQiblaBearing = 0f;
    private static final float ALPHA = 0.15f; // Low-pass filter smoothing coefficient

    public CompassSensorManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        } else {
            accelerometer = null;
            magnetometer = null;
        }
    }

    public void setListener(CompassListener listener) {
        this.listener = listener;
    }

    public void setTargetQiblaBearing(float bearing) {
        this.targetQiblaBearing = bearing;
    }

    public void start() {
        if (sensorManager != null) {
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            }
            if (magnetometer != null) {
                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            applyLowPassFilter(event.values, gravity);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            applyLowPassFilter(event.values, geomagnetic);
        }

        boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientation);
            float azimuthInRadians = orientation[0];
            float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
            azimuthInDegrees = (azimuthInDegrees + 360) % 360;

            currentAzimuth = azimuthInDegrees;
            if (listener != null) {
                listener.onCompassRotationChanged(currentAzimuth, targetQiblaBearing);
            }
        }
    }

    private void applyLowPassFilter(float[] input, float[] output) {
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Optional sensor calibration notification
    }
}
