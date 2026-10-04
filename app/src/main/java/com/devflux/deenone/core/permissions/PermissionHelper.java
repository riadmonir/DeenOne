package com.devflux.deenone.core.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    public static final String[] REQUIRED_PERMISSIONS;

    static {
        List<String> perms = new ArrayList<>();
        perms.add(Manifest.permission.ACCESS_FINE_LOCATION);
        perms.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perms.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        REQUIRED_PERMISSIONS = perms.toArray(new String[0]);
    }

    public static boolean hasAllPermissions(Context context) {
        for (String perm : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasNotificationPermission(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return androidx.core.app.NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    public static boolean canScheduleExactAlarms(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            return alarmManager != null && alarmManager.canScheduleExactAlarms();
        }
        return true;
    }

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        if (context == null) return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm != null && pm.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return true;
    }

    public static void requestIgnoreBatteryOptimizations(android.app.Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (!isIgnoringBatteryOptimizations(activity)) {
                    android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(android.net.Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                try {
                    android.content.Intent fallback = new android.content.Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    activity.startActivity(fallback);
                } catch (Exception ignored) {}
            }
        }
    }

    public static void requestAppPermissions(ActivityResultLauncher<String[]> launcher) {
        launcher.launch(REQUIRED_PERMISSIONS);
    }
}
