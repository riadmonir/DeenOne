package com.devflux.deenone.features.salahguide;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalahContentFormatter {

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");

    /**
     * Formats raw text so references and key labels are highlighted cleanly, safely, and compatibly.
     */
    public static CharSequence format(String rawText, Context context) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "";
        }

        if (context == null) {
            return rawText;
        }

        try {
            int highlightColor = ContextCompat.getColor(context, R.color.accent_teal);
            SpannableStringBuilder ssb = new SpannableStringBuilder(rawText);
            Matcher matcher = REFERENCE_PATTERN.matcher(rawText);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                ssb.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ssb;
        } catch (Exception e) {
            return rawText;
        }
    }
}

