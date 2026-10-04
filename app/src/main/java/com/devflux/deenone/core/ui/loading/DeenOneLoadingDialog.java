package com.devflux.deenone.core.ui.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;

/**
 * Universal full-screen / overlay DeenOne Loading Dialog.
 * Safe against BadTokenExceptions and Activity lifecycle destruction.
 */
public class DeenOneLoadingDialog extends Dialog {

    private static DeenOneLoadingDialog currentDialog;
    private final DeenOneLoadingView loadingView;

    public DeenOneLoadingDialog(@NonNull Context context) {
        super(getSafeActivityContext(context), R.style.Theme_DeenOne_FullScreenPage);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadingView = new DeenOneLoadingView(context);
        setContentView(loadingView);

        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private static Context getSafeActivityContext(@NonNull Context context) {
        Context target = context;
        while (target instanceof ContextWrapper && !(target instanceof Activity)) {
            target = ((ContextWrapper) target).getBaseContext();
        }
        return target != null ? target : context;
    }

    public static synchronized void showLoading(@NonNull Context context) {
        showLoading(context, null);
    }

    public static synchronized void showLoading(@NonNull Context context, String message) {
        try {
            dismissLoading();
            Context safeContext = getSafeActivityContext(context);
            if (safeContext instanceof Activity) {
                Activity act = (Activity) safeContext;
                if (act.isFinishing() || act.isDestroyed()) {
                    return;
                }
            }
            currentDialog = new DeenOneLoadingDialog(safeContext);
            if (message != null && !message.isEmpty()) {
                currentDialog.loadingView.setMessage(message);
            }
            currentDialog.show();
        } catch (Throwable ignored) {}
    }

    public static synchronized void dismissLoading() {
        try {
            if (currentDialog != null && currentDialog.isShowing()) {
                currentDialog.dismiss();
            }
        } catch (Throwable ignored) {
        } finally {
            currentDialog = null;
        }
    }

    public static synchronized boolean isLoadingShowing() {
        return currentDialog != null && currentDialog.isShowing();
    }
}
