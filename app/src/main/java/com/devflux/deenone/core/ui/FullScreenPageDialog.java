package com.devflux.deenone.core.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.theme.ThemeManager;

public class FullScreenPageDialog extends Dialog {

    private View attachedContentView;

    public FullScreenPageDialog(@NonNull Context context) {
        super(getSafeActivityContext(context), R.style.Theme_DeenOne_FullScreenPage);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            LocaleManager.applyLocale(context, LocaleManager.getSavedLanguage(context));
        } catch (Throwable ignored) {}
    }

    private static Context getSafeActivityContext(@NonNull Context context) {
        Context target = context;
        while (target instanceof ContextWrapper && !(target instanceof Activity)) {
            target = ((ContextWrapper) target).getBaseContext();
        }
        return target != null ? target : context;
    }

    @Override
    public void show() {
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }
        try {
            super.show();
        } catch (WindowManager.BadTokenException bte) {
            android.util.Log.e("FullScreenPageDialog", "Suppressed BadTokenException: " + bte.getMessage());
        } catch (Exception e) {
            android.util.Log.e("FullScreenPageDialog", "Error showing dialog: " + e.getMessage(), e);
        }
    }

    @Override
    public void dismiss() {
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                try {
                    super.dismiss();
                } catch (Exception ignored) {}
                return;
            }
        }
        try {
            super.dismiss();
        } catch (Exception e) {
            android.util.Log.e("FullScreenPageDialog", "Error dismissing dialog: " + e.getMessage(), e);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        super.setContentView(view);
        this.attachedContentView = view;
        applySystemBarInsets(view);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        this.attachedContentView = view;
        applySystemBarInsets(view);
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        this.attachedContentView = view;
        applySystemBarInsets(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Optimize window composition: disable wasteful background dimming and enforce hardware acceleration
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            window.setFormat(android.graphics.PixelFormat.TRANSLUCENT);

            // Edge to edge for full screen dialog
            WindowCompat.setDecorFitsSystemWindows(window, false);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            boolean isDark = ThemeManager.getSavedThemeMode(getContext()) == ThemeManager.THEME_DARK;
            androidx.core.view.WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
            if (controller != null) {
                controller.setAppearanceLightStatusBars(!isDark);
                controller.setAppearanceLightNavigationBars(!isDark);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.getAttributes().layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        }
        if (attachedContentView != null) {
            applySystemBarInsets(attachedContentView);
        }
    }

    private void applySystemBarInsets(View view) {
        if (view == null) return;
        final int initialPaddingLeft = view.getPaddingLeft();
        final int initialPaddingRight = view.getPaddingRight();
        final int initialPaddingTop = view.getPaddingTop();
        final int initialPaddingBottom = view.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets insetsValues = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() |
                    WindowInsetsCompat.Type.displayCutout() |
                    WindowInsetsCompat.Type.ime()
            );
            v.setPadding(
                    initialPaddingLeft,
                    insetsValues.top + initialPaddingTop,
                    initialPaddingRight,
                    insetsValues.bottom + initialPaddingBottom
            );
            return insets;
        });

        if (ViewCompat.isAttachedToWindow(view)) {
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    ViewCompat.requestApplyInsets(v);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {}
            });
        }
    }
}
