package com.devflux.deenone.features.janaza;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemJanazaContentCardBinding;
import com.devflux.deenone.databinding.PageJanazaDetailViewerBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class JanazaDetailViewerDialog {

    private static final String PREFS_NAME = "janaza_detail_prefs";
    private static final String KEY_FONT_SIZE = "janaza_font_size";

    public static void show(Activity activity, String topicId) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageJanazaDetailViewerBinding binding = PageJanazaDetailViewerBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);
        JanazaDataProvider.JanazaTopic topic = JanazaDataProvider.getTopicById(topicId, isBn);

        // Header Title & Subtitle
        binding.tvDetailHeaderTitle.setText(topic.title);
        binding.tvTopicBadgeTag.setText(isBn ? "সহীহ সুন্নাহ ভিত্তিক" : "Authentic Sunnah");
        binding.tvTopicIntroSubtitle.setText(topic.subtitle);

        // Back button
        binding.btnBackDetail.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackDetail);

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);

        ContentAdapter adapter = new ContentAdapter(activity, topic.contentItems, savedSize, isBn);
        binding.rvDetailContentCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvDetailContentCards.setAdapter(adapter);

        // Font size settings button
        binding.btnFontSize.setOnClickListener(v -> {
            showFontSizeDialog(activity, adapter, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnFontSize);

        // Share topic button
        binding.btnShareDetail.setOnClickListener(v -> {
            shareFullTopic(activity, topic, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnShareDetail);

        // Copy topic button
        binding.btnCopyDetail.setOnClickListener(v -> {
            copyFullTopic(activity, topic, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnCopyDetail);

        dialog.show();
    }

    private static void showFontSizeDialog(Activity activity, ContentAdapter adapter, SharedPreferences prefs, boolean isBn) {
        String[] options = isBn ? new String[]{
                "ফন্ট সাইজ: ছোট (১২ sp)",
                "ফন্ট সাইজ: সাধারণ (১৪ sp)",
                "ফন্ট সাইজ: প্রমিত (১৬ sp)",
                "ফন্ট সাইজ: বড় (১৮ sp)",
                "ফন্ট সাইজ: বিশাল (২০ sp)"
        } : new String[]{
                "Font Size: Small (12 sp)",
                "Font Size: Regular (14 sp)",
                "Font Size: Medium (16 sp)",
                "Font Size: Large (18 sp)",
                "Font Size: Extra Large (20 sp)"
        };
        float[] sizes = {12.0f, 14.0f, 16.0f, 18.0f, 20.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Choose Font Size")
                .setItems(options, (d, which) -> {
                    float chosen = sizes[which];
                    adapter.setFontSize(chosen);
                    prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                })
                .show();
    }

    private static void copyFullTopic(Context context, JanazaDataProvider.JanazaTopic topic, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(topic.title).append("\n\n");
        sb.append(topic.intro).append("\n\n");
        for (JanazaDataProvider.JanazaContentItem item : topic.contentItems) {
            sb.append("━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("📌 ").append(item.heading).append("\n");
            if (item.arabic != null && !item.arabic.isEmpty()) {
                sb.append("\n").append(item.arabic).append("\n");
            }
            if (item.pronunciation != null && !item.pronunciation.isEmpty()) {
                sb.append("\n").append(isBn ? "উচ্চারণ: " : "Pronunciation: ").append(item.pronunciation).append("\n");
            }
            if (item.meaning != null && !item.meaning.isEmpty()) {
                sb.append("\n").append(isBn ? "অর্থ: " : "Meaning: ").append(item.meaning).append("\n");
            }
            if (item.explanation != null && !item.explanation.isEmpty()) {
                sb.append("\n").append(isBn ? "মাসায়েল ও বিবরণ:\n" : "Rules & Rulings:\n").append(item.explanation).append("\n");
            }
            if (item.reference != null && !item.reference.isEmpty()) {
                sb.append("\n").append(isBn ? "রেফারেন্স: " : "Reference: ").append(item.reference).append("\n");
            }
            sb.append("\n");
        }
        sb.append(isBn ? "— দ্বীনওয়ান অ্যাপ" : "— DeenOne App");

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(topic.title, sb.toString().trim()));
            Toast.makeText(context, isBn ? "সম্পূর্ণ অধ্যায় কপি করা হয়েছে" : "Topic copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareFullTopic(Context context, JanazaDataProvider.JanazaTopic topic, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append("📖 ").append(topic.title).append(" — ").append(isBn ? "দ্বীনওয়ান" : "DeenOne").append("\n\n");
        for (JanazaDataProvider.JanazaContentItem item : topic.contentItems) {
            sb.append("• ").append(item.heading).append("\n");
            if (item.arabic != null && !item.arabic.isEmpty()) {
                sb.append(item.arabic).append("\n");
            }
            if (item.meaning != null && !item.meaning.isEmpty()) {
                sb.append(isBn ? "অর্থ: " : "Meaning: ").append(item.meaning).append("\n");
            }
            if (item.explanation != null && !item.explanation.isEmpty()) {
                sb.append(item.explanation).append("\n");
            }
            if (item.reference != null && !item.reference.isEmpty()) {
                sb.append(isBn ? "দলিল: " : "Source: ").append(item.reference).append("\n");
            }
            sb.append("\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, topic.title);
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
        context.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
    }

    private static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
        private final Activity activity;
        private final List<JanazaDataProvider.JanazaContentItem> items;
        private float fontSizeSp;
        private final boolean isBn;

        public ContentAdapter(Activity activity, List<JanazaDataProvider.JanazaContentItem> items, float fontSizeSp, boolean isBn) {
            this.activity = activity;
            this.items = items;
            this.fontSizeSp = fontSizeSp;
            this.isBn = isBn;
        }

        public void setFontSize(float size) {
            this.fontSizeSp = size;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemJanazaContentCardBinding binding = ItemJanazaContentCardBinding.inflate(
                    LayoutInflater.from(activity), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JanazaDataProvider.JanazaContentItem item = items.get(position);
            holder.bind(item, fontSizeSp, isBn);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemJanazaContentCardBinding binding;

            public ViewHolder(@NonNull ItemJanazaContentCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(JanazaDataProvider.JanazaContentItem item, float fontSize, boolean isBn) {
                binding.tvCardHeading.setText(item.heading);

                // Arabic block
                if (item.arabic != null && !item.arabic.isEmpty()) {
                    binding.layoutArabicBox.setVisibility(View.VISIBLE);
                    binding.tvArabicText.setText(item.arabic);
                    binding.tvArabicText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize + 5.0f);
                } else {
                    binding.layoutArabicBox.setVisibility(View.GONE);
                }

                // Pronunciation block
                if (item.pronunciation != null && !item.pronunciation.isEmpty()) {
                    binding.layoutPronunciationBox.setVisibility(View.VISIBLE);
                    binding.tvPronunciationLabel.setText(isBn ? "উচ্চারণ:" : "Pronunciation:");
                    binding.tvPronunciationText.setText(item.pronunciation);
                    binding.tvPronunciationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                } else {
                    binding.layoutPronunciationBox.setVisibility(View.GONE);
                }

                // Meaning block
                if (item.meaning != null && !item.meaning.isEmpty()) {
                    binding.layoutMeaningBox.setVisibility(View.VISIBLE);
                    binding.tvMeaningLabel.setText(isBn ? "অর্থ:" : "Meaning:");
                    binding.tvMeaningText.setText(item.meaning);
                    binding.tvMeaningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                } else {
                    binding.layoutMeaningBox.setVisibility(View.GONE);
                }

                // Explanation / Masail block
                if (item.explanation != null && !item.explanation.isEmpty()) {
                    binding.layoutExplanationBox.setVisibility(View.VISIBLE);
                    binding.tvExplanationLabel.setText(isBn ? "আহকাম ও মাসায়েল:" : "Rules & Rulings:");
                    binding.tvExplanationText.setText(item.explanation);
                    binding.tvExplanationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                } else {
                    binding.layoutExplanationBox.setVisibility(View.GONE);
                }

                // Reference Citation
                if (item.reference != null && !item.reference.isEmpty()) {
                    binding.layoutReferenceBadge.setVisibility(View.VISIBLE);
                    binding.tvReferenceText.setText(item.reference);
                } else {
                    binding.layoutReferenceBadge.setVisibility(View.GONE);
                }

                // Copy single card
                binding.btnCopySingleCard.setOnClickListener(v -> {
                    copySingleCard(item, isBn);
                });

                // Share single card
                binding.btnShareSingleCard.setOnClickListener(v -> {
                    shareSingleCard(item, isBn);
                });
            }

            private void copySingleCard(JanazaDataProvider.JanazaContentItem item, boolean isBn) {
                StringBuilder sb = new StringBuilder();
                sb.append("📌 ").append(item.heading).append("\n\n");
                if (item.arabic != null && !item.arabic.isEmpty()) {
                    sb.append(item.arabic).append("\n\n");
                }
                if (item.pronunciation != null && !item.pronunciation.isEmpty()) {
                    sb.append(isBn ? "উচ্চারণ: " : "Pronunciation: ").append(item.pronunciation).append("\n\n");
                }
                if (item.meaning != null && !item.meaning.isEmpty()) {
                    sb.append(isBn ? "অর্থ: " : "Meaning: ").append(item.meaning).append("\n\n");
                }
                if (item.explanation != null && !item.explanation.isEmpty()) {
                    sb.append(item.explanation).append("\n\n");
                }
                if (item.reference != null && !item.reference.isEmpty()) {
                    sb.append(isBn ? "দলিল: " : "Source: ").append(item.reference).append("\n\n");
                }
                sb.append(isBn ? "— দ্বীনওয়ান" : "— DeenOne");

                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText(item.heading, sb.toString().trim()));
                    Toast.makeText(activity, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }

            private void shareSingleCard(JanazaDataProvider.JanazaContentItem item, boolean isBn) {
                StringBuilder sb = new StringBuilder();
                sb.append("📖 ").append(item.heading).append("\n\n");
                if (item.arabic != null && !item.arabic.isEmpty()) {
                    sb.append(item.arabic).append("\n\n");
                }
                if (item.meaning != null && !item.meaning.isEmpty()) {
                    sb.append(isBn ? "অর্থ: " : "Meaning: ").append(item.meaning).append("\n\n");
                }
                if (item.explanation != null && !item.explanation.isEmpty()) {
                    sb.append(item.explanation).append("\n\n");
                }
                if (item.reference != null && !item.reference.isEmpty()) {
                    sb.append(isBn ? "দলিল: " : "Source: ").append(item.reference).append("\n\n");
                }
                sb.append(isBn ? "— দ্বীনওয়ান অ্যাপ" : "— DeenOne App");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, item.heading);
                intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
            }
        }
    }
}
