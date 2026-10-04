package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemNamazShikhaMenuCardBinding;
import com.devflux.deenone.databinding.PageSalahDuaMenuBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SalahDuaMenuPageDialog {

    public static class DuaItem {
        final String title;
        final String topicId;

        public DuaItem(String title, String topicId) {
            this.title = title;
            this.topicId = topicId;
        }
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahDuaMenuBinding binding = PageSalahDuaMenuBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);
        binding.tvHeaderTitle.setText(isBn ? "নামাজের দোয়া" : "Salah Duas");

        // Back Button with Spring Touch
        binding.btnBackSalahDuaMenu.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahDuaMenu);

        // 11 Items from screenshot
        List<DuaItem> items = getDuaItems(isBn);

        DuaAdapter adapter = new DuaAdapter(activity, items);
        binding.rvSalahDuaChapters.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvSalahDuaChapters.setAdapter(adapter);

        // Settings Gear
        binding.btnSettingsSalahDuaMenu.setOnClickListener(v -> {
            showSettingsDialog(activity, items, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahDuaMenu);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<DuaItem> items, boolean isBn) {
        String[] options = isBn ? new String[]{
                "নামাজের দোয়াসমূহের তালিকা কপি করুন",
                "নামাজের দোয়া শেয়ার করুন"
        } : new String[]{
                "Copy list of Salah Duas",
                "Share Salah Duas"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "নামাজের দোয়া অপশনস" : "Salah Dua Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== নামাজের গুরুত্বপূর্ণ দোয়াসমূহ ===\n\n" : "=== Important Salah Duas ===\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append((i + 1)).append(". ").append(items.get(i).title).append("\n");
                        }
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText("Salah Duas", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "নামাজের দোয়ার তালিকা কপি করা হয়েছে" : "Salah Dua list copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "🤲 নামাজের গুরুত্বপূর্ণ মাসনূন দোয়াসমূহ — দ্বীনওয়ান\n\n" : "🤲 Important Masnoon Salah Duas — DeenOne\n\n");
                        sb.append(isBn ? "নামাজের প্রতিটি রুকনের বিশুদ্ধ আরবি তাসবীহ, উচ্চারণ ও অর্থ:\n\n" : "Authentic Arabic Tasbih, pronunciation and translation for every part of Salah:\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append("• ").append(items.get(i).title).append("\n");
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "নামাজের দোয়া" : "Salah Duas");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share"));
                    }
                })
                .show();
    }

    public static class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.ViewHolder> {
        private final Activity activity;
        private final List<DuaItem> items;

        public DuaAdapter(Activity activity, List<DuaItem> items) {
            this.activity = activity;
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemNamazShikhaMenuCardBinding binding = ItemNamazShikhaMenuCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DuaItem item = items.get(position);
            holder.binding.tvChapterTitle.setText(item.title);

            holder.binding.cardContainer.setOnClickListener(v -> {
                if ("dua_wudu".equals(item.topicId)) {
                    SalahWuduDuaPageDialog.show(activity);
                } else if ("dua_sana".equals(item.topicId)) {
                    SalahSanaDuaPageDialog.show(activity);
                } else if ("dua_waswasa".equals(item.topicId)) {
                    SalahWaswasaDuaPageDialog.show(activity);
                } else if ("dua_ruku".equals(item.topicId)) {
                    SalahRukuDuaPageDialog.show(activity);
                } else if ("dua_qawmah".equals(item.topicId)) {
                    SalahQawmahDuaPageDialog.show(activity);
                } else if ("dua_sajdah".equals(item.topicId)) {
                    SalahSajdahDuaPageDialog.show(activity);
                } else if ("dua_jalsah".equals(item.topicId)) {
                    SalahJalsahDuaPageDialog.show(activity);
                } else if ("dua_tashahhud".equals(item.topicId)) {
                    SalahTashahhudDuaPageDialog.show(activity);
                } else if ("dua_durood".equals(item.topicId)) {
                    SalahDuroodDuaPageDialog.show(activity);
                } else if ("dua_masura".equals(item.topicId)) {
                    SalahMasuraDuaPageDialog.show(activity);
                } else if ("dua_qunut".equals(item.topicId)) {
                    SalahQunutDuaPageDialog.show(activity);
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
            final ItemNamazShikhaMenuCardBinding binding;

            public ViewHolder(@NonNull ItemNamazShikhaMenuCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static List<DuaItem> getDuaItems(boolean isBn) {
        List<DuaItem> list = new ArrayList<>();
        if (isBn) {
            list.add(new DuaItem("অজুর দোয়া", "dua_wudu"));
            list.add(new DuaItem("সানা দোয়া", "dua_sana"));
            list.add(new DuaItem("নামাজে শয়তানের ধোঁকা থেকে বাঁচার দোয়া", "dua_waswasa"));
            list.add(new DuaItem("রুকুর তাসবিহ", "dua_ruku"));
            list.add(new DuaItem("রুকু থেকে উঠার পর দোয়া", "dua_qawmah"));
            list.add(new DuaItem("সিজদার তাসবিহ", "dua_sajdah"));
            list.add(new DuaItem("দুই সিজদার মাঝের দোয়া", "dua_jalsah"));
            list.add(new DuaItem("তাশাহহুদ", "dua_tashahhud"));
            list.add(new DuaItem("দুরুদ শরীফ", "dua_durood"));
            list.add(new DuaItem("দোয়া মাসুরা", "dua_masura"));
            list.add(new DuaItem("দোয়া কুনুত", "dua_qunut"));
        } else {
            list.add(new DuaItem("Dua for Wudu", "dua_wudu"));
            list.add(new DuaItem("Dua Sana", "dua_sana"));
            list.add(new DuaItem("Protection from Whispers in Salah", "dua_waswasa"));
            list.add(new DuaItem("Tasbih of Ruku", "dua_ruku"));
            list.add(new DuaItem("Dua after Rising from Ruku", "dua_qawmah"));
            list.add(new DuaItem("Tasbih of Sujood", "dua_sajdah"));
            list.add(new DuaItem("Dua between Two Sujoods", "dua_jalsah"));
            list.add(new DuaItem("Tashahhud", "dua_tashahhud"));
            list.add(new DuaItem("Durood Sharif", "dua_durood"));
            list.add(new DuaItem("Dua Masura", "dua_masura"));
            list.add(new DuaItem("Dua Qunut", "dua_qunut"));
        }
        return list;
    }
}
