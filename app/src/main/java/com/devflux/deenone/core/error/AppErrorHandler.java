package com.devflux.deenone.core.error;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.devflux.deenone.MainActivity;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class AppErrorHandler {

    private static final String TAG = "AppErrorHandler";

    public enum ErrorCategory {
        NO_INTERNET,
        TIMEOUT,
        SERVER_ERROR,
        INVALID_RESPONSE,
        LOCATION_PERMISSION_DENIED,
        GPS_DISABLED,
        NOTIFICATION_PERMISSION_DENIED,
        UNKNOWN
    }

    public static class AppError {
        public final ErrorCategory category;
        public final String userMessage;
        public final String technicalDetails;

        public AppError(ErrorCategory category, String userMessage, String technicalDetails) {
            this.category = category;
            this.userMessage = userMessage;
            this.technicalDetails = technicalDetails;
        }
    }

    /**
     * Converts any network or parsing exception to a safe, user-friendly AppError
     */
    public static AppError parseNetworkError(Throwable t) {
        if (t == null) {
            return new AppError(ErrorCategory.UNKNOWN, "অপ্রত্যাশিত একটি ত্রুটি হয়েছে।", "Null throwable");
        }

        String details = t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName();
        Log.e(TAG, "Network error intercepted: " + details, t);

        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return new AppError(
                    ErrorCategory.NO_INTERNET,
                    "ইন্টারনেট সংযোগ পাওয়া যায়নি। অনুগ্রহ করে আপনার নেটওয়ার্ক চেক করুন।",
                    details
            );
        } else if (t instanceof SocketTimeoutException) {
            return new AppError(
                    ErrorCategory.TIMEOUT,
                    "সার্ভার সাড়া দিতে বিলম্ব হচ্ছে (টাইমআউট)। কিছুক্ষণ পর পুনরায় চেষ্টা করুন।",
                    details
            );
        } else if (t instanceof com.google.gson.JsonSyntaxException || t instanceof org.json.JSONException) {
            return new AppError(
                    ErrorCategory.INVALID_RESPONSE,
                    "সার্ভার থেকে প্রাপ্ত তথ্য প্রক্রিয়াকরণ করা সম্ভব হয়নি।",
                    details
            );
        } else if (t instanceof IOException) {
            return new AppError(
                    ErrorCategory.SERVER_ERROR,
                    "সার্ভারের সাথে যোগাযোগে বিঘ্ন ঘটেছে।",
                    details
            );
        } else {
            return new AppError(
                    ErrorCategory.UNKNOWN,
                    "তথ্য লোড করতে সমস্যা হয়েছে। ক্যাশড তথ্য প্রদর্শিত হচ্ছে।",
                    details
            );
        }
    }

    private static long lastRestartAttemptTime = 0;

    /**
     * Global uncaught exception protection shield
     */
    public static void installGlobalCrashProtection(Context appContext) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e(TAG, "FATAL: Uncaught exception in thread [" + thread.getName() + "]: " + throwable.getMessage(), throwable);

            // If it's a background worker thread, prevent killing the entire app process
            if (thread != android.os.Looper.getMainLooper().getThread()) {
                Log.w(TAG, "Suppressed uncaught exception on background thread [" + thread.getName() + "]. App continues running safely.");
                return;
            }

            long now = System.currentTimeMillis();
            if (now - lastRestartAttemptTime > 5000) {
                lastRestartAttemptTime = now;
                try {
                    Intent intent = new Intent(appContext, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    appContext.startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "Could not restart after crash: " + e.getMessage());
                }
            }

            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });
    }

    /**
     * Safe runner that suppresses runtime exceptions in async background tasks
     */
    public static void safeExecute(Runnable task) {
        if (task == null) return;
        try {
            task.run();
        } catch (Throwable t) {
            Log.e(TAG, "Suppressed background task exception: " + t.getMessage(), t);
        }
    }
}
