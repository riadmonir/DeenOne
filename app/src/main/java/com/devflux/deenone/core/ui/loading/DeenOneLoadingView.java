package com.devflux.deenone.core.ui.loading;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;

/**
 * Universal DeenOne Loading Component matching the official design.
 * Easily embeddable in any XML layout or programmatically added.
 */
public class DeenOneLoadingView extends FrameLayout {

    private LinearLayout layoutLoadingContainer;
    private DeenOneLoadingIndicatorView indicatorLoading;
    private TextView tvLoadingMessage;

    public DeenOneLoadingView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DeenOneLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeenOneLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_deenone_loading, this, true);
        layoutLoadingContainer = findViewById(R.id.layoutLoadingContainer);
        indicatorLoading = findViewById(R.id.indicatorLoading);
        tvLoadingMessage = findViewById(R.id.tvLoadingMessage);

        boolean isBn = LocaleManager.isBengali(context);
        if (tvLoadingMessage != null) {
            tvLoadingMessage.setText(isBn ? "দিনওয়ান সিঙ্ক হচ্ছে..." : "SYNCING DEENONE");
        }
    }

    public void setMessage(String message) {
        if (tvLoadingMessage != null && message != null) {
            tvLoadingMessage.setText(message);
        }
    }

    public void setMessage(@StringRes int resId) {
        if (tvLoadingMessage != null && getContext() != null) {
            tvLoadingMessage.setText(resId);
        }
    }

    public void setTransparentBackground(boolean isTransparent) {
        if (layoutLoadingContainer != null) {
            if (isTransparent) {
                layoutLoadingContainer.setBackgroundColor(Color.TRANSPARENT);
            } else {
                layoutLoadingContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_main));
            }
        }
    }

    public void setAccentColor(int color) {
        if (indicatorLoading != null) {
            indicatorLoading.setAccentColor(color);
        }
        if (tvLoadingMessage != null) {
            tvLoadingMessage.setTextColor(color);
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
        if (indicatorLoading != null) {
            indicatorLoading.startAnimation();
        }
    }

    public void hide() {
        if (indicatorLoading != null) {
            indicatorLoading.stopAnimation();
        }
        setVisibility(View.GONE);
    }
}
