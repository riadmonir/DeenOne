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
import com.devflux.deenone.databinding.PagePurityMenuBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PurityMenuPageDialog {

    public static class PurityItem {
        final String title;
        final String topicId;

        public PurityItem(String title, String topicId) {
            this.title = title;
            this.topicId = topicId;
        }
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PagePurityMenuBinding binding = PagePurityMenuBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);
        binding.tvHeaderTitle.setText(isBn ? "পাক পবিত্রতা" : "Purity & Taharah");

        // Back Button with Spring Touch
        binding.btnBackPurityMenu.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackPurityMenu);

        // 4 Items from screenshot
        List<PurityItem> items = getPurityItems(isBn);

        PurityAdapter adapter = new PurityAdapter(activity, items);
        binding.rvPurityChapters.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvPurityChapters.setAdapter(adapter);

        // Settings Gear
        binding.btnSettingsPurityMenu.setOnClickListener(v -> {
            showSettingsDialog(activity, items, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsPurityMenu);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<PurityItem> items, boolean isBn) {
        String[] options = isBn ? new String[]{
                "পাক পবিত্রতার সূচিপত্র কপি করুন",
                "পাক পবিত্রতার নিয়ম শেয়ার করুন"
        } : new String[]{
                "Copy Purity contents list",
                "Share Purity rules"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পাক পবিত্রতা অপশনস" : "Purity Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== পাক পবিত্রতা সূচিপত্র ===\n\n" : "=== Purity & Taharah Index ===\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append((i + 1)).append(". ").append(items.get(i).title).append("\n");
                        }
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText("Purity Index", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "পাক পবিত্রতার সূচিপত্র কপি করা হয়েছে" : "Purity index copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "💧 পাক পবিত্রতা — দ্বীনওয়ান\n\n" : "💧 Purity & Taharah — DeenOne\n\n");
                        sb.append(isBn ? "সালাতের জন্য শরীর, কাপড় ও মনকে পবিত্র রাখার সঠিক ইসলামিক বিধানাবলী:\n\n" : "Islamic rulings for purifying body, clothes and heart before prayer:\n\n");
                        for (int i = 0; i < items.size(); i++) {
                            sb.append("• ").append(items.get(i).title).append("\n");
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "পাক পবিত্রতা" : "Purity & Taharah");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share"));
                    }
                })
                .show();
    }

    public static class PurityAdapter extends RecyclerView.Adapter<PurityAdapter.ViewHolder> {
        private final Activity activity;
        private final List<PurityItem> items;

        public PurityAdapter(Activity activity, List<PurityItem> items) {
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
            PurityItem item = items.get(position);
            holder.binding.tvChapterTitle.setText(item.title);

            holder.binding.cardContainer.setOnClickListener(v -> {
                if ("purity_istinja".equals(item.topicId)) {
                    SalahIstinjaPageDialog.show(activity);
                } else if ("purity_ghusl".equals(item.topicId)) {
                    SalahGhuslPageDialog.show(activity);
                } else if ("purity_tayammum".equals(item.topicId)) {
                    SalahTayammumPageDialog.show(activity);
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

    private static List<PurityItem> getPurityItems(boolean isBn) {
        List<PurityItem> list = new ArrayList<>();
        if (isBn) {
            list.add(new PurityItem("ইস্তিঞ্জা", "purity_istinja"));
            list.add(new PurityItem("অজু", "purity_wudu"));
            list.add(new PurityItem("গোসল", "purity_ghusl"));
            list.add(new PurityItem("তায়াম্মুম", "purity_tayammum"));
        } else {
            list.add(new PurityItem("Istinja (Cleanliness)", "purity_istinja"));
            list.add(new PurityItem("Wudu (Ablution)", "purity_wudu"));
            list.add(new PurityItem("Ghusl (Full Bath)", "purity_ghusl"));
            list.add(new PurityItem("Tayammum (Dry Ablution)", "purity_tayammum"));
        }
        return list;
    }
}
