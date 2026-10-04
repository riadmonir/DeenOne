package com.devflux.deenone.features.marriage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.model.MarriageTopicItem;
import com.devflux.deenone.data.repository.MuslimMarriageRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MuslimMarriageTopicDetailDialog {

    private static final String PREFS_NAME = "marriage_reader_prefs";
    private static final String KEY_FONT_SIZE = "key_marriage_font_size";
    private static final float DEFAULT_FONT_SIZE = 14.5f;

    public static void show(@NonNull Context context, int topicId) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_marriage_topic_detail, null);
        dialog.setContentView(view);

        bindTopicData(context, dialog, view, topicId);
        dialog.show();
    }

    private static void bindTopicData(@NonNull Context context,
                                      @NonNull FullScreenPageDialog dialog,
                                      @NonNull View view,
                                      int topicId) {
        MarriageTopicItem item = MuslimMarriageRepository.getTopicById(topicId);
        List<MarriageTopicItem> allTopics = MuslimMarriageRepository.getAllTopics();
        boolean isBn = LocaleManager.isBengali(context);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Top bar views
        FrameLayout btnBack = view.findViewById(R.id.btnBackDetail);
        FrameLayout btnFontSize = view.findViewById(R.id.btnDetailFontSize);
        TextView tvCategoryHeader = view.findViewById(R.id.tvDetailCategoryHeader);
        TextView tvChapterHeader = view.findViewById(R.id.tvDetailChapterHeader);

        // Single Card views
        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        FrameLayout btnCardMoreMenu = view.findViewById(R.id.btnCardMoreMenu);
        TextView tvMainContent = view.findViewById(R.id.tvDetailMainContent);

        // Chapter navigation buttons
        TextView btnPrev = view.findViewById(R.id.btnPrevChapter);
        TextView btnNext = view.findViewById(R.id.btnNextChapter);

        TouchAnimationUtil.attachTouchSpring(btnBack);
        TouchAnimationUtil.attachTouchSpring(btnFontSize);
        if (btnCardMoreMenu != null) {
        }
        TouchAnimationUtil.attachTouchSpring(btnPrev);
        TouchAnimationUtil.attachTouchSpring(btnNext);

        btnBack.setOnClickListener(v -> dialog.dismiss());

        // Apply persisted font size
        float savedFontSize = prefs.getFloat(KEY_FONT_SIZE, DEFAULT_FONT_SIZE);
        applyFontSize(view, savedFontSize);

        // Top bar settings button strictly opens Font Size dialog
        btnFontSize.setOnClickListener(v -> showFontSizeDialog(context, view, prefs, isBn));

        // Card 3-dot overflow menu: Copy & Share
        if (btnCardMoreMenu != null) {
            btnCardMoreMenu.setOnClickListener(v -> showCardActionDialog(context, item, isBn));
        }

        // Format header
        String chapterNum = isBn ? ("অধ্যায় " + BengaliNumberUtil.toBengali(item.getId()))
                : ("Chapter " + item.getId());

        tvCategoryHeader.setText(isBn ? item.getCategoryBn() : item.getCategoryEn());
        tvChapterHeader.setText(chapterNum);
        tvTitle.setText(isBn ? item.getTitleBn() : item.getTitleEn());

        // Set 100% Verbatim Formatted Text in SINGLE CARD
        tvMainContent.setText(MuslimMarriageContentFormatter.format(item, context));

        // Previous / Next Chapter navigation
        int currentIndex = -1;
        for (int i = 0; i < allTopics.size(); i++) {
            if (allTopics.get(i).getId() == topicId) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex > 0) {
            int prevId = allTopics.get(currentIndex - 1).getId();
            btnPrev.setVisibility(View.VISIBLE);
            btnPrev.setText(isBn ? "পূর্ববর্তী অধ্যায়" : "Previous Chapter");
            btnPrev.setOnClickListener(v -> bindTopicData(context, dialog, view, prevId));
        } else {
            btnPrev.setVisibility(View.INVISIBLE);
        }

        if (currentIndex < allTopics.size() - 1) {
            int nextId = allTopics.get(currentIndex + 1).getId();
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setText(isBn ? "পরবর্তী অধ্যায়" : "Next Chapter");
            btnNext.setOnClickListener(v -> bindTopicData(context, dialog, view, nextId));
        } else {
            btnNext.setVisibility(View.INVISIBLE);
        }
    }

    private static void applyFontSize(@NonNull View view, float sizeSp) {
        TextView tvMainContent = view.findViewById(R.id.tvDetailMainContent);
        if (tvMainContent != null) {
            tvMainContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        }
    }

    private static void showCardActionDialog(@NonNull Context context,
                                             @NonNull MarriageTopicItem item,
                                             boolean isBn) {
        String[] options = isBn ? new String[]{
                "কার্ডের লেখা কপি করুন",
                "শেয়ার করুন"
        } : new String[]{
                "Copy Card Content",
                "Share"
        };

        String title = isBn ? item.getTitleBn() : item.getTitleEn();

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setItems(options, (dialog, which) -> {
                    CharSequence content = MuslimMarriageContentFormatter.format(item, context);
                    if (which == 0) {
                        copyToClipboard(context, title, content.toString(), isBn);
                    } else if (which == 1) {
                        shareText(context, title, content.toString(), isBn);
                    }
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void showFontSizeDialog(@NonNull Context context,
                                           @NonNull View view,
                                           @NonNull SharedPreferences prefs,
                                           boolean isBn) {
        String[] sizes = isBn ? new String[]{
                "খুব ছোট (১২ sp)",
                "ছোট (১৪ sp)",
                "স্বাভাবিক (১৬ sp)",
                "বড় (১৮ sp)",
                "অনেক বড় (২০ sp)"
        } : new String[]{
                "Very Small (12 sp)",
                "Small (14 sp)",
                "Normal (16 sp)",
                "Large (18 sp)",
                "Extra Large (20 sp)"
        };

        float[] sizeValues = new float[]{12.0f, 14.0f, 16.0f, 18.0f, 20.0f};
        float currentSize = prefs.getFloat(KEY_FONT_SIZE, DEFAULT_FONT_SIZE);

        int selectedIndex = 1;
        float minDiff = Float.MAX_VALUE;
        for (int i = 0; i < sizeValues.length; i++) {
            float diff = Math.abs(sizeValues[i] - currentSize);
            if (diff < minDiff) {
                minDiff = diff;
                selectedIndex = i;
            }
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(isBn ? "পড়ার ফন্ট সাইজ" : "Reading Font Size")
                .setSingleChoiceItems(sizes, selectedIndex, (dialog, which) -> {
                    float newSize = sizeValues[which];
                    prefs.edit().putFloat(KEY_FONT_SIZE, newSize).apply();
                    applyFontSize(view, newSize);
                    dialog.dismiss();
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void copyToClipboard(@NonNull Context context, String label, String text, boolean isBn) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text));
            Toast.makeText(context, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareText(@NonNull Context context, String subject, String text, boolean isBn) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, isBn ? "শেয়ার করুন" : "Share via"));
    }
}
