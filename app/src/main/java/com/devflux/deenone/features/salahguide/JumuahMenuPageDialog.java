package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemMasailMenuCardBinding;
import com.devflux.deenone.databinding.PageMasailMenuBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready Jumu'ah Menu Page Dialog listing all 11 Jumu'ah sub-topics.
 */
public class JumuahMenuPageDialog {

    public static class JumuahMenuItem {
        public final String title;
        public final String topicId;

        public JumuahMenuItem(String title, String topicId) {
            this.title = title;
            this.topicId = topicId;
        }
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageMasailMenuBinding binding = PageMasailMenuBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title (Verbatim: "জুম'আ" / "Jumu'ah")
        binding.tvHeaderTitle.setText(isBn ? "জুম'আ" : "Jumu'ah");

        // Back Button with Spring Touch
        binding.btnBackMasailMenu.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackMasailMenu);

        // 11 Jumu'ah Topics
        List<JumuahMenuItem> items = getJumuahMenuItems(isBn);

        JumuahMenuAdapter adapter = new JumuahMenuAdapter(activity, items);
        binding.rvMasailChapters.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMasailChapters.setAdapter(adapter);

        // Settings Gear
        binding.btnSettingsMasailMenu.setOnClickListener(v -> {
            showSettingsDialog(activity, items, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsMasailMenu);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<JumuahMenuItem> items, boolean isBn) {
        String[] options = {
                isBn ? "সূচিপত্র কপি করুন" : "Copy Table of Contents",
                isBn ? "জুম'আ সূচিপত্র শেয়ার করুন" : "Share Jumu'ah Index"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "জুম'আ অপশনস" : "Jumu'ah Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== জুম'আ সূচিপত্র ===\n\n" : "=== Jumu'ah Index ===\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append((i + 1)).append(". ").append(items.get(i).title).append("\n");
                        }
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "জুম'আ সূচিপত্র" : "Jumu'ah Index", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "সূচিপত্র ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "🕌 জুম'আ সহায়িকা — দ্বীনওয়ান\n\n" : "🕌 Jumu'ah Guide — DeenOne\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append("• ").append(items.get(i).title).append("\n");
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "জুম'আ সহায়িকা" : "Jumu'ah Guide");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
                    }
                })
                .show();
    }

    public static class JumuahMenuAdapter extends RecyclerView.Adapter<JumuahMenuAdapter.ViewHolder> {
        private final Activity activity;
        private final List<JumuahMenuItem> items;

        public JumuahMenuAdapter(Activity activity, List<JumuahMenuItem> items) {
            this.activity = activity;
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMasailMenuCardBinding binding = ItemMasailMenuCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JumuahMenuItem item = items.get(position);
            holder.binding.tvMasailTitle.setText(item.title);

            holder.binding.cardContainer.setOnClickListener(v -> {
                if ("jumuah_namaz".equals(item.topicId)) {
                    JumuahNamazPageDialog.show(activity);
                } else if ("jumuah_history".equals(item.topicId)) {
                    JumuahHistoryPageDialog.show(activity);
                } else if ("jumuah_khutbah_language".equals(item.topicId)) {
                    JumuahKhutbahLanguagePageDialog.show(activity);
                } else if ("jumuah_friday_duties".equals(item.topicId)) {
                    JumuahFridayDutiesPageDialog.show(activity);
                } else if ("jumuah_sunnah_prayers".equals(item.topicId)) {
                    JumuahSunnahPrayersPageDialog.show(activity);
                } else if ("jumuah_prayer_time".equals(item.topicId)) {
                    JumuahPrayerTimePageDialog.show(activity);
                } else if ("jumuah_prayer_location".equals(item.topicId)) {
                    JumuahPrayerLocationPageDialog.show(activity);
                } else if ("jumuah_exemptions".equals(item.topicId)) {
                    JumuahExemptionsPageDialog.show(activity);
                } else if ("jumuah_missed_rakats".equals(item.topicId)) {
                    JumuahMissedRakatsPageDialog.show(activity);
                } else if ("jumuah_virtues".equals(item.topicId)) {
                    JumuahVirtuesPageDialog.show(activity);
                } else if ("jumuah_eid_day".equals(item.topicId)) {
                    JumuahEidDayPageDialog.show(activity);
                } else {
                    // MasailJumuahPageDialog fallback or specific sub-dialog
                    MasailJumuahPageDialog.show(activity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemMasailMenuCardBinding binding;

            public ViewHolder(@NonNull ItemMasailMenuCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static List<JumuahMenuItem> getJumuahMenuItems(boolean isBn) {
        List<JumuahMenuItem> list = new ArrayList<>();
        list.add(new JumuahMenuItem(isBn ? "জুম'আর নামায" : "Friday Prayer", "jumuah_namaz"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর নামাজের ইতিহাস" : "History of Jumu'ah Prayer", "jumuah_history"));
        list.add(new JumuahMenuItem(isBn ? "স্থানীয় ভাষায় জুম'আর খুতবা" : "Jumu'ah Khutbah in Local Language", "jumuah_khutbah_language"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর দিনে করণীয়" : "Sunnah Duties on Friday", "jumuah_friday_duties"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর আগে ও পরে সুন্নত" : "Sunnah Before & After Jumu'ah", "jumuah_sunnah_prayers"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর সময়" : "Time of Jumu'ah Prayer", "jumuah_prayer_time"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর স্থান" : "Location for Jumu'ah Prayer", "jumuah_prayer_location"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আহ যাদের উপর ফরয নয়" : "Those Exempt from Jumu'ah", "jumuah_exempt_persons"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর রাকআত ছুটে গেলে" : "Missing a Rak'ah of Jumu'ah", "jumuah_missed_rakat"));
        list.add(new JumuahMenuItem(isBn ? "জুম'আর দিনের ফযীলত ও বৈশিষ্ট্য" : "Virtues of Friday", "jumuah_friday_virtues"));
        list.add(new JumuahMenuItem(isBn ? "ঈদের দিন জুম'আ" : "When Eid Falls on Friday", "jumuah_eid_day"));
        return list;
    }
}
