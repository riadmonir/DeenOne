package com.devflux.deenone.features.marriage;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.model.MarriageTopicItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats Muslim Marriage topics into a clean, single-card verbatim text layout.
 * Ensures:
 * 1. 100% Verbatim content preservation (Zero cutting, zero rewriting).
 * 2. Clean line-by-line structure with appropriate paragraph spacing.
 * 3. References and footnotes placed inside the same card, highlighted with bold + accent teal.
 * 4. Arabic hadith lines and Quranic calligraphy highlighted with bold + accent mint.
 */
public class MuslimMarriageContentFormatter {

    // Matches bracketed references: [1], [২], [১], ([৫]), etc.
    private static final Pattern BRACKET_REF_PATTERN = Pattern.compile("(?:\\[[^\\]]+\\]|\\(\\s*\\[[^\\]]+\\]\\s*\\))");

    // Matches Arabic lines, hadiths and text passages
    private static final Pattern ARABIC_TEXT_PATTERN = Pattern.compile("[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF][\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF\\s\\.\\,\\:\\;\\(\\)\\[\\]\\-–—«»\"\'\\\\/]+[\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDFF\\uFE70-\\uFEFF]");
    private static final Pattern QURAN_CALLIGRAPHY_PATTERN = Pattern.compile("﴿[^﴾]+﴾");

    // Matches numbered bullet headings like "১। ...", "১- ...", "১. ..."
    private static final Pattern HEADER_LINE_PATTERN = Pattern.compile("(?m)^([০-৯\\d]+[\\.\\।\\-][^\n]+)");

    // Matches footnote header
    private static final Pattern REF_HEADER_PATTERN = Pattern.compile("(?m)^(টীকা:|রেফারেন্স ও টীকা:|References & Notes:|Notes:|টীকা)");

    public static CharSequence format(MarriageTopicItem item, Context context) {
        if (item == null) return "";
        boolean isBn = context == null || LocaleManager.isBengali(context);

        StringBuilder sb = new StringBuilder();

        // 1. Arabic Ayah / Hadith, Pronunciation, Meaning (if present)
        String arabic = item.getArabicAyatOrHadith();
        if (arabic != null && !arabic.trim().isEmpty()) {
            sb.append(arabic.trim()).append("\n\n");

            String pron = item.getArabicPronunciation();
            if (pron != null && !pron.trim().isEmpty()) {
                sb.append(isBn ? "উচ্চারণ: " : "Pronunciation: ")
                        .append(pron.trim()).append("\n\n");
            }

            String meaning = item.getArabicMeaning();
            if (meaning != null && !meaning.trim().isEmpty()) {
                sb.append(isBn ? "অর্থ: " : "Meaning: ")
                        .append(meaning.trim()).append("\n\n");
            }
        }

        // 2. Main Content (100% Verbatim line-by-line)
        String rawContent = isBn ? item.getContentBn() : item.getContentEn();
        if (rawContent != null && !rawContent.trim().isEmpty()) {
            // Replace internal split markers with clean double newline for smooth reading
            String cleaned = rawContent.replace("---SPLIT---", "\n\n");
            // Remove raw markdown '## ' prefix from section headers so it displays cleanly
            cleaned = cleaned.replaceAll("(?m)^##\\s*", "");
            sb.append(cleaned.trim()).append("\n\n");
        }

        // 3. References / Footnotes (inside the same single card, line-by-line)
        String refs = item.getReferences();
        if (refs != null && !refs.trim().isEmpty()) {
            sb.append("\n");
            sb.append(isBn ? "টীকা:\n" : "Notes:\n");

            String[] refLines = refs.split("\n");
            StringBuilder refSb = new StringBuilder();
            for (String line : refLines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                if (refSb.length() > 0) {
                    refSb.append("\n");
                }
                refSb.append(trimmed);
            }
            sb.append(refSb.toString().trim());
        }

        String fullText = sb.toString().trim();
        SpannableStringBuilder ssb = new SpannableStringBuilder(fullText);

        int refColor = context != null ? ContextCompat.getColor(context, R.color.accent_teal) : 0xFF0D9488;
        int arabicColor = context != null ? ContextCompat.getColor(context, R.color.accent_mint) : 0xFF16A34A;

        // 1. Highlight Arabic passages and Hadith quotes
        Matcher arabicMatcher = ARABIC_TEXT_PATTERN.matcher(fullText);
        while (arabicMatcher.find()) {
            int start = arabicMatcher.start();
            int end = arabicMatcher.end();
            ssb.setSpan(new ForegroundColorSpan(arabicColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 2. Highlight Quranic calligraphy (﴿...﴾)
        Matcher quranMatcher = QURAN_CALLIGRAPHY_PATTERN.matcher(fullText);
        while (quranMatcher.find()) {
            int start = quranMatcher.start();
            int end = quranMatcher.end();
            ssb.setSpan(new ForegroundColorSpan(arabicColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 3. Highlight all bracketed references [ ... ] with accent teal and BOLD
        Matcher matcher = BRACKET_REF_PATTERN.matcher(fullText);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ssb.setSpan(new ForegroundColorSpan(refColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 4. Apply BOLD to numbered bullet headings like "১। ...", "১- ...", "১. ..."
        Matcher headerMatcher = HEADER_LINE_PATTERN.matcher(fullText);
        while (headerMatcher.find()) {
            int start = headerMatcher.start();
            int end = headerMatcher.end();
            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 5. Apply BOLD + Accent Teal to "টীকা:" / "Notes:"
        Matcher refHeaderMatcher = REF_HEADER_PATTERN.matcher(fullText);
        while (refHeaderMatcher.find()) {
            int start = refHeaderMatcher.start();
            int end = refHeaderMatcher.end();
            ssb.setSpan(new ForegroundColorSpan(refColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return ssb;
    }
}
