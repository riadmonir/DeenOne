package com.devflux.deenone.features.marriage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.repository.MuslimMarriageRepository;
import com.devflux.deenone.features.marriage.adapter.MarriageTopicAdapter;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MuslimMarriagePageDialog {

    public static void show(@NonNull Context context) {
        if (!(context instanceof Activity)) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.page_muslim_marriage, null);
        dialog.setContentView(view);

        boolean isBn = LocaleManager.isBengali(context);

        // Header and Actions matching Screenshot
        FrameLayout btnClose = view.findViewById(R.id.btnCloseMarriage);
        TextView tvHeaderTitle = view.findViewById(R.id.tvMarriageHeaderTitle);
        FrameLayout btnSettings = view.findViewById(R.id.btnSettingsMarriage);
        RecyclerView rvTopics = view.findViewById(R.id.rvMarriageTopics);

        // Title
        tvHeaderTitle.setText(isBn ? "মুসলিম বিবাহ" : "Muslim Marriage");

        // Back action
        TouchAnimationUtil.attachTouchSpring(btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Setup Adapter
        MarriageTopicAdapter adapter = new MarriageTopicAdapter((item, position) -> {
            MuslimMarriageTopicDetailDialog.show(context, item.getId());
        });
        rvTopics.setLayoutManager(new LinearLayoutManager(context));
        rvTopics.setAdapter(adapter);
        adapter.setItems(MuslimMarriageRepository.getAllTopics());

        // Settings Button Action
        TouchAnimationUtil.attachTouchSpring(btnSettings);
        btnSettings.setOnClickListener(v -> {
            showSettingsDialog(context, adapter, isBn);
        });

        dialog.show();
    }

    private static void showSettingsDialog(@NonNull Context context, @NonNull MarriageTopicAdapter adapter, boolean isBn) {
        String[] options = new String[]{
                isBn ? "অধ্যায় অনুসন্ধান করুন" : "Search Chapters",
                isBn ? "ডার্ক / লাইট থিম পরিবর্তন" : "Toggle Theme",
                isBn ? "শেয়ার করুন" : "Share Guide"
        };

        new MaterialAlertDialogBuilder(context)
                .setTitle(isBn ? "বিকল্প ও সেটিংস" : "Options & Settings")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        showSearchDialog(context, adapter, isBn);
                    } else if (which == 1) {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).toggleAppTheme();
                        }
                    } else if (which == 2) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "মুসলিম বিবাহ - আদর্শ দাম্পত্য গাইড" : "Muslim Marriage Guide");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, isBn
                                ? "দ্বীনওয়ান (DeenOne) অ্যাপে মুসলিম বিবাহ ও সুন্নাহ ভিত্তিক আদর্শ দাম্পত্য গাইড পড়ুন।"
                                : "Read Muslim Marriage & Sunnah Conjugal Life Guide on DeenOne App.");
                        context.startActivity(Intent.createChooser(shareIntent, isBn ? "শেয়ার করুন" : "Share via"));
                    }
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void showSearchDialog(@NonNull Context context, @NonNull MarriageTopicAdapter adapter, boolean isBn) {
        android.widget.EditText input = new android.widget.EditText(context);
        input.setHint(isBn ? "অধ্যায় বা বিষয় লিখুন..." : "Search chapters...");
        input.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        input.setHintTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        input.setPadding(48, 36, 48, 36);

        new MaterialAlertDialogBuilder(context)
                .setTitle(isBn ? "অধ্যায় অনুসন্ধান" : "Search Chapters")
                .setView(input)
                .setPositiveButton(isBn ? "অনুসন্ধান" : "Search", (d, which) -> {
                    String query = input.getText().toString().trim();
                    if (query.isEmpty()) {
                        adapter.setItems(MuslimMarriageRepository.getAllTopics());
                    } else {
                        adapter.setItems(MuslimMarriageRepository.searchTopics(query));
                    }
                })
                .setNeutralButton(isBn ? "সকল অধ্যায় দেখুন" : "Show All", (d, which) -> {
                    adapter.setItems(MuslimMarriageRepository.getAllTopics());
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }
}

