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

public class MasailMenuPageDialog {

    public static class MasailItem {
        final String title;
        final String topicId;

        public MasailItem(String title, String topicId) {
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

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "মাসাইল" : "Masail");

        // Back Button with Spring Touch
        binding.btnBackMasailMenu.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackMasailMenu);

        // 7 Items with dual language support
        List<MasailItem> items = getMasailItems(isBn);

        MasailAdapter adapter = new MasailAdapter(activity, items);
        binding.rvMasailChapters.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMasailChapters.setAdapter(adapter);

        // Settings Gear
        binding.btnSettingsMasailMenu.setOnClickListener(v -> {
            showSettingsDialog(activity, items, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsMasailMenu);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<MasailItem> items, boolean isBn) {
        String[] options = {
                isBn ? "মাসাইলের সূচিপত্র কপি করুন" : "Copy Table of Contents",
                isBn ? "মাসাইল শেয়ার করুন" : "Share Masail"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "মাসাইল অপশনস" : "Masail Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== জরুরি নামাজ ও তাহারাত মাসাইল ===\n\n" : "=== Essential Salah & Purity Masail ===\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append((i + 1)).append(". ").append(items.get(i).title).append("\n");
                        }
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "মাসাইল সূচিপত্র" : "Masail Contents", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "সূচিপত্র ক্লিপবোর্ডে কপি করা হয়েছে" : "Table of contents copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "📜 জরুরি মাসাইল — দ্বীনওয়ান\n\n" : "📜 Essential Masail — DeenOne\n\n");
                        sb.append(isBn ? "আজান, ওজু, নামাজ, তারাবীহ, কাযা ও সাহু সেজদাহর নির্ভরযোগ্য মাসআলা-মাসায়েল:\n\n" : "Authentic rulings on Adhan, Wudu, Salah, Tarabi, Qadha, and Sahu Sajdah:\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append("• ").append(items.get(i).title).append("\n");
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "জরুরি মাসাইল" : "Essential Masail");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
                    }
                })
                .show();
    }

    public static class MasailAdapter extends RecyclerView.Adapter<MasailAdapter.ViewHolder> {
        private final Activity activity;
        private final List<MasailItem> items;

        public MasailAdapter(Activity activity, List<MasailItem> items) {
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
            MasailItem item = items.get(position);
            holder.binding.tvMasailTitle.setText(item.title);

            holder.binding.cardContainer.setOnClickListener(v -> {
                if ("masail_azan".equals(item.topicId)) {
                    MasailAzanPageDialog.show(activity);
                } else if ("masail_wudu".equals(item.topicId)) {
                    MasailWuduPageDialog.show(activity);
                } else if ("masail_namaz".equals(item.topicId) || "masail_salah".equals(item.topicId)) {
                    MasailSalahPageDialog.show(activity);
                } else if ("masail_tarabi".equals(item.topicId)) {
                    MasailTarabiPageDialog.show(activity);
                } else if ("masail_qadha".equals(item.topicId)) {
                    MasailQadhaPageDialog.show(activity);
                } else if ("masail_sahu_sajdah".equals(item.topicId)) {
                    MasailSahuSajdahPageDialog.show(activity);
                } else if ("masail_masjid".equals(item.topicId)) {
                    MasailMasjidPageDialog.show(activity);
                } else if ("masail_jumuah".equals(item.topicId)) {
                    JumuahMenuPageDialog.show(activity);
                } else {
                    SalahGenericContentDialog.show(activity, item.topicId);
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

    private static List<MasailItem> getMasailItems(boolean isBn) {
        List<MasailItem> list = new ArrayList<>();
        list.add(new MasailItem(isBn ? "আজান" : "Adhan", "masail_azan"));
        list.add(new MasailItem(isBn ? "অজু" : "Wudu", "masail_wudu"));
        list.add(new MasailItem(isBn ? "নামাজ" : "Salah", "masail_namaz"));
        list.add(new MasailItem(isBn ? "তারাবীহ" : "Tarabi", "masail_tarabi"));
        list.add(new MasailItem(isBn ? "কাযা নামাজ" : "Qadha Salah", "masail_qadha"));
        list.add(new MasailItem(isBn ? "সাহু সেজদাহ" : "Sahu Sajdah", "masail_sahu_sajdah"));
        list.add(new MasailItem(isBn ? "মসজিদ" : "Masjid", "masail_masjid"));
        list.add(new MasailItem(isBn ? "জুম'আ" : "Jumu'ah", "masail_jumuah"));
        return list;
    }
}
