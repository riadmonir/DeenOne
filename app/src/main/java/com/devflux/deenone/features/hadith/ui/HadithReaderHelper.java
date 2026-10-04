package com.devflux.deenone.features.hadith.ui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.BottomSheetHadithActionsBinding;
import com.devflux.deenone.databinding.DialogHadithDisplaySettingsBinding;
import com.devflux.deenone.features.hadith.adapter.HadithReaderAdapter;
import com.devflux.deenone.features.hadith.model.HadithReaderItem;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;

public class HadithReaderHelper {

    private static final String PREF_NAME = "hadith_reader_prefs";
    private static final String KEY_ARABIC_SP = "arabic_font_sp";
    private static final String KEY_TRANS_SP = "trans_font_sp";

    public static void showMoreOptionsBottomSheet(
            Context context,
            HadithReaderItem item,
            View anchorCardView,
            Runnable onBookmarkChanged
    ) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.DeenOneBottomSheetDialog);
        BottomSheetHadithActionsBinding binding = BottomSheetHadithActionsBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        String title = item.getBookName(isBn) + " • " + (isBn ? "হাদিস: " : "Hadith: ") + item.getHadithNumber(isBn);
        binding.tvActionSheetTitle.setText(title);

        if (!isBn) {
            binding.tvLabelCopyAll.setText("Copy Entire Hadith");
            binding.tvLabelCopyTranslation.setText("Copy English Translation");
            binding.tvLabelCopyArabic.setText("Copy Arabic Text");
            binding.tvLabelFavorite.setText("Save to Favorites");
            binding.tvLabelAddNote.setText("Add Personal Note");
            binding.tvLabelShareText.setText("Share Text");
            binding.tvLabelShareScreenshot.setText("Share Screenshot Card");
        }

        TouchAnimationUtil.attachTouchSpring(binding.actionCopyAll);
        TouchAnimationUtil.attachTouchSpring(binding.actionCopyTranslation);
        TouchAnimationUtil.attachTouchSpring(binding.actionCopyArabic);
        TouchAnimationUtil.attachTouchSpring(binding.actionToggleFavorite);
        TouchAnimationUtil.attachTouchSpring(binding.actionAddNote);
        TouchAnimationUtil.attachTouchSpring(binding.actionShareText);
        TouchAnimationUtil.attachTouchSpring(binding.actionShareScreenshot);

        // 1. Copy All
        binding.actionCopyAll.setOnClickListener(v -> {
            dialog.dismiss();
            StringBuilder sb = new StringBuilder();
            sb.append(item.getBookName(isBn)).append(" - ").append(isBn ? "হাদিস: " : "Hadith: ").append(item.getHadithNumber(isBn)).append("\n\n");
            if (item.getArabicText() != null) sb.append(item.getArabicText()).append("\n\n");
            if (item.getNarrator(isBn) != null) sb.append(item.getNarrator(isBn)).append("\n");
            if (item.getTranslation(isBn) != null) sb.append(item.getTranslation(isBn)).append("\n\n");
            if (item.getFootnote(isBn) != null) sb.append((isBn ? "ফুটনোট:\n" : "Footnote:\n")).append(item.getFootnote(isBn));

            copyToClipboard(context, "Hadith Full", sb.toString());
            Toast.makeText(context, isBn ? "সম্পূর্ণ হাদিস কপি করা হয়েছে" : "Full hadith copied", Toast.LENGTH_SHORT).show();
        });

        // 2. Copy Translation
        binding.actionCopyTranslation.setOnClickListener(v -> {
            dialog.dismiss();
            StringBuilder sb = new StringBuilder();
            if (item.getNarrator(isBn) != null) sb.append(item.getNarrator(isBn)).append("\n");
            if (item.getTranslation(isBn) != null) sb.append(item.getTranslation(isBn));

            copyToClipboard(context, "Hadith Translation", sb.toString());
            Toast.makeText(context, isBn ? "অনুবাদ কপি করা হয়েছে" : "Translation copied", Toast.LENGTH_SHORT).show();
        });

        // 3. Copy Arabic
        binding.actionCopyArabic.setOnClickListener(v -> {
            dialog.dismiss();
            if (item.getArabicText() != null) {
                copyToClipboard(context, "Hadith Arabic", item.getArabicText());
                Toast.makeText(context, isBn ? "আরবি পাঠ কপি করা হয়েছে" : "Arabic text copied", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Toggle Favorite
        binding.actionToggleFavorite.setOnClickListener(v -> {
            dialog.dismiss();
            boolean newFav = !item.isBookmarked();
            item.setBookmarked(newFav);
            if (onBookmarkChanged != null) onBookmarkChanged.run();
            Toast.makeText(context, isBn ? (newFav ? "ফেভারিটে যোগ করা হয়েছে" : "ফেভারিট থেকে সরানো হয়েছে") : (newFav ? "Added to Favorites" : "Removed from Favorites"), Toast.LENGTH_SHORT).show();
        });

        // 5. Add Note
        binding.actionAddNote.setOnClickListener(v -> {
            dialog.dismiss();
            showAddNoteDialog(context, item, isBn);
        });

        // 6. Share Text
        binding.actionShareText.setOnClickListener(v -> {
            dialog.dismiss();
            StringBuilder sb = new StringBuilder();
            sb.append("❖ ").append(item.getBookName(isBn)).append(" [").append(isBn ? "হাদিস: " : "Hadith: ").append(item.getHadithNumber(isBn)).append("] ❖\n\n");
            if (item.getArabicText() != null) sb.append(item.getArabicText()).append("\n\n");
            if (item.getNarrator(isBn) != null) sb.append(item.getNarrator(isBn)).append("\n");
            if (item.getTranslation(isBn) != null) sb.append(item.getTranslation(isBn)).append("\n\n");
            if (item.getFootnote(isBn) != null) sb.append((isBn ? "ফুটনোট:\n" : "Footnote:\n")).append(item.getFootnote(isBn)).append("\n\n");
            sb.append("— DeenOne App");

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            context.startActivity(Intent.createChooser(shareIntent, isBn ? "হাদিস শেয়ার করুন" : "Share Hadith"));
        });

        // 7. Share Screenshot Card
        binding.actionShareScreenshot.setOnClickListener(v -> {
            dialog.dismiss();
            shareViewAsImage(context, anchorCardView, title, isBn);
        });

        dialog.show();
    }

    public static void showDisplaySettingsBottomSheet(Context context, HadithReaderAdapter adapter) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.DeenOneBottomSheetDialog);
        DialogHadithDisplaySettingsBinding binding = DialogHadithDisplaySettingsBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        float currentArabicSp = prefs.getFloat(KEY_ARABIC_SP, 19.5f);
        float currentTransSp = prefs.getFloat(KEY_TRANS_SP, 14.5f);

        boolean isBn = LocaleManager.isBengali(context);
        if (!isBn) {
            binding.tvSettingsTitle.setText("Reading Settings");
            binding.tvLabelArabicSize.setText("Arabic Font Size");
            binding.tvLabelTranslationSize.setText("Translation Font Size");
        }

        final float[] arabicHolder = new float[]{currentArabicSp};
        final float[] transHolder = new float[]{currentTransSp};

        binding.tvArabicSizeValue.setText(String.format("%.1f sp", arabicHolder[0]));
        binding.tvTranslationSizeValue.setText(String.format("%.1f sp", transHolder[0]));

        binding.btnArabicMinus.setOnClickListener(v -> {
            if (arabicHolder[0] > 14.0f) {
                arabicHolder[0] -= 1.0f;
                binding.tvArabicSizeValue.setText(String.format("%.1f sp", arabicHolder[0]));
                prefs.edit().putFloat(KEY_ARABIC_SP, arabicHolder[0]).apply();
                adapter.setTextSizes(arabicHolder[0], transHolder[0]);
            }
        });

        binding.btnArabicPlus.setOnClickListener(v -> {
            if (arabicHolder[0] < 34.0f) {
                arabicHolder[0] += 1.0f;
                binding.tvArabicSizeValue.setText(String.format("%.1f sp", arabicHolder[0]));
                prefs.edit().putFloat(KEY_ARABIC_SP, arabicHolder[0]).apply();
                adapter.setTextSizes(arabicHolder[0], transHolder[0]);
            }
        });

        binding.btnTranslationMinus.setOnClickListener(v -> {
            if (transHolder[0] > 11.0f) {
                transHolder[0] -= 1.0f;
                binding.tvTranslationSizeValue.setText(String.format("%.1f sp", transHolder[0]));
                prefs.edit().putFloat(KEY_TRANS_SP, transHolder[0]).apply();
                adapter.setTextSizes(arabicHolder[0], transHolder[0]);
            }
        });

        binding.btnTranslationPlus.setOnClickListener(v -> {
            if (transHolder[0] < 26.0f) {
                transHolder[0] += 1.0f;
                binding.tvTranslationSizeValue.setText(String.format("%.1f sp", transHolder[0]));
                prefs.edit().putFloat(KEY_TRANS_SP, transHolder[0]).apply();
                adapter.setTextSizes(arabicHolder[0], transHolder[0]);
            }
        });

        dialog.show();
    }

    private static void showAddNoteDialog(Context context, HadithReaderItem item, boolean isBn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(isBn ? "ব্যক্তিগত নোট যুক্ত করুন" : "Add Personal Note");

        final EditText input = new EditText(context);
        input.setHint(isBn ? "আপনার ব্যক্তিগত উপলব্ধি বা নোট লিখুন..." : "Write your note here...");
        int padding = (int) (16 * context.getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        SharedPreferences notePrefs = context.getSharedPreferences("hadith_notes", Context.MODE_PRIVATE);
        String existingNote = notePrefs.getString("note_bukhari_" + item.getHadithNumber(), "");
        input.setText(existingNote);

        builder.setView(input);

        builder.setPositiveButton(isBn ? "সংরক্ষণ" : "Save", (d, which) -> {
            String note = input.getText().toString().trim();
            notePrefs.edit().putString("note_bukhari_" + item.getHadithNumber(), note).apply();
            Toast.makeText(context, isBn ? "নোট সংরক্ষিত হয়েছে" : "Note saved", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(isBn ? "বাতিল" : "Cancel", (d, which) -> d.dismiss());
        builder.show();
    }

    private static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    private static void shareViewAsImage(Context context, View view, String title, boolean isBn) {
        try {
            // Traverse up to CardView if anchor is button
            View card = view;
            while (card.getParent() instanceof View && card.getWidth() < 300) {
                card = (View) card.getParent();
            }

            Bitmap bitmap = Bitmap.createBitmap(card.getWidth(), card.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable bg = card.getBackground();
            if (bg != null) {
                bg.draw(canvas);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            card.draw(canvas);

            File cachePath = new File(context.getCacheDir(), "images");
            if (!cachePath.exists()) cachePath.mkdirs();
            File imageFile = new File(cachePath, "hadith_card_" + System.currentTimeMillis() + ".png");
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, isBn ? "হাদিস কার্ড শেয়ার করুন" : "Share Hadith Card"));
        } catch (Exception e) {
            Toast.makeText(context, isBn ? "ছবি তৈরিতে সমস্যা হয়েছে" : "Failed to generate card image", Toast.LENGTH_SHORT).show();
        }
    }
}
