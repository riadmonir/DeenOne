package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemSalahBasicsCardBinding;
import com.devflux.deenone.databinding.PageSalahBasicsRulesBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class SalahAzanQadaPageDialog {

    public static class StepItem {
        public final String title;
        public final String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public StepItem(String title, String previewSubtitle, String fullContent) {
            this.title = title;
            this.previewSubtitle = previewSubtitle;
            this.fullContent = fullContent;
            this.isExpanded = false;
        }
    }

    public static void show(@NonNull Context context) {
        if (!(context instanceof Activity)) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageSalahBasicsRulesBinding binding = PageSalahBasicsRulesBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);
        binding.tvHeaderTitle.setText(isBn ? "কাযা নামাযের জন্য আযান" : "Adhan for Missed (Qada) Prayers");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future options
        });

        List<StepItem> items = getStepItems(isBn);

        StepAdapter adapter = new StepAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
        private final Context context;
        private final List<StepItem> items;
        private final boolean isBn;

        public StepAdapter(Context context, List<StepItem> items, boolean isBn) {
            this.context = context;
            this.items = items;
            this.isBn = isBn;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahBasicsCardBinding binding = ItemSalahBasicsCardBinding.inflate(
                    LayoutInflater.from(context), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            StepItem item = items.get(position);
            holder.bind(item, isBn);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemSalahBasicsCardBinding binding;

            public ViewHolder(@NonNull ItemSalahBasicsCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                TouchAnimationUtil.attachTouchSpring(binding.layoutToggleExpand);

                View.OnClickListener toggleClick = v -> {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                        StepItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(StepItem item, boolean isBn) {
                binding.tvItemTitle.setText(item.title);

                if (item.isExpanded) {
                    binding.tvItemPreview.setVisibility(View.GONE);
                    binding.tvItemFullContent.setVisibility(View.VISIBLE);
                    binding.tvItemFullContent.setText(SalahContentFormatter.format(item.fullContent, context));
                    binding.tvToggleText.setText(isBn ? "সংক্ষিপ্ত করুন" : "Show Less");
                    binding.ivToggleChevron.setRotation(180f);
                } else {
                    binding.tvItemPreview.setVisibility(View.VISIBLE);
                    binding.tvItemPreview.setText(item.previewSubtitle);
                    binding.tvItemFullContent.setVisibility(View.GONE);
                    binding.tvToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "Read More");
                    binding.ivToggleChevron.setRotation(0f);
                }
            }
        }
    }

    public static List<StepItem> getStepItems(boolean isBn) {
        List<StepItem> list = new ArrayList<>();

        if (isBn) {
            // 1. কাযা নামাযের জন্য আযান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "কাযা নামাযের জন্য আযান",
                    "মসজিদে কেউ আযান না দিলে এবং কাযা হলে অসময়েও আযান-ইকামতের বিধান... [মুসলিম, আহমদ]",
                    "মসজিদে কেউ আযান না দিলে এবং শহরে বা গ্রামে থাকতে সকলের নামায কাযা হলে অথবা সফরে পুরো জামাআতের বা একাকীর নামায কাযা হলে অসময়েও আযান-ইকামত দিয়ে নামায পড়া কর্তব্য।\n\nএকদা মহানবী (ﷺ) সাহাবাসহ্ সফরে থাকাকালীন তাঁদের ফজরের নামায কাযা হয়ে যায়। সূর্য ওঠার পর তেজ হয়ে এলে ঐ স্থান ত্যাগ করে অন্য স্থানে গিয়ে বিলাল (রাঃ) আযান দেন। অতঃপর যথা নিয়মে ফজরের নামায আদায় করেন।\n\n[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]\n\nযেমন খন্দকের যুদ্ধের সময় একদা সকলের চার ওয়াক্তের নামায ক্বাযা হলে, এশার পর আযান দিয়ে যোহ্র, আসর, মাগরিব ও এশার নামায আদায় করেছিলেন।\n\n[আহমাদ, মুসনাদ প্রমুখ, ইর: ১/২৫৭]"
            ));

            // 2. অসময়ে আযান ও ইকামতের বিধান
            list.add(new StepItem(
                    "অসময়ে আযান ও ইকামতের বিধান",
                    "গ্রাম, শহর বা সফরে নামায কাযা হলে আযান ও ইকামত দিয়ে নামায আদায়ের বিধান...",
                    "মসজিদে কেউ আযান না দিলে এবং শহরে বা গ্রামে থাকতে সকলের নামায কাযা হলে অথবা সফরে পুরো জামাআতের বা একাকীর নামায কাযা হলে অসময়েও আযান-ইকামত দিয়ে নামায পড়া কর্তব্য।"
            ));

            // 3. সফরে ফজরের নামায কাযা ও বিলালের (রাঃ) আযান
            list.add(new StepItem(
                    "সফরে ফজরের নামায কাযা ও বিলালের (রাঃ) আযান",
                    "সূর্য ওঠার পর স্থান পরিবর্তন করে হযরত বিলালের আযান ও ফজরের নামায আদায়...",
                    "একদা মহানবী (ﷺ) সাহাবাসহ্ সফরে থাকাকালীন তাঁদের ফজরের নামায কাযা হয়ে যায়। সূর্য ওঠার পর তেজ হয়ে এলে ঐ স্থান ত্যাগ করে অন্য স্থানে গিয়ে বিলাল (রাঃ) আযান দেন। অতঃপর যথা নিয়মে ফজরের নামায আদায় করেন।\n\n[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]"
            ));

            // 4. খন্দকের যুদ্ধে চার ওয়াক্ত নামায কাযা ও আযান
            list.add(new StepItem(
                    "খন্দকের যুদ্ধে চার ওয়াক্ত নামায কাযা ও আযান",
                    "খন্দকের যুদ্ধে চার ওয়াক্ত নামায কাযা হলে এশার পর আযান দিয়ে ধারাবাহিক সালাত আদায়...",
                    "যেমন খন্দকের যুদ্ধের সময় একদা সকলের চার ওয়াক্তের নামায ক্বাযা হলে, এশার পর আযান দিয়ে যোহ্র, আসর, মাগরিব ও এশার নামায আদায় করেছিলেন।\n\n[আহমাদ, মুসনাদ প্রমুখ, ইর: ১/২৫৭]"
            ));

        } else {
            // English Mode
            // 1. Adhan for Missed (Qada) Prayers (Full Narrative)
            list.add(new StepItem(
                    "Adhan for Missed (Qada) Prayers",
                    "Calling Adhan and Iqamah at unscheduled times for missed prayers... [Muslim, Ahmad]",
                    "If no one calls the Adhan in the mosque and everyone in a town or village misses their prayer, or if the entire congregation or an individual misses their prayer during travel, it is a duty to proclaim Adhan and Iqamah even at an unscheduled time before offering the prayer.\n\nOnce, while traveling with the Companions, the Prophet (ﷺ) and they missed the Fajr prayer due to sleep. After sunrise, when the sun became warm, they departed from that location to another place where Bilal (RA) called the Adhan, and they then prayed the Fajr prayer according to the established manner.\n\n[Sahih Muslim 681]\n\nSimilarly, during the Battle of the Trench (Khandaq), when everyone missed four daily prayers, they called the Adhan after Isha time and sequentially performed Dhuhr, Asr, Maghrib, and Isha prayers.\n\n[Musnad Ahmad, Irwa al-Ghalil 1/257]"
            ));

            // 2. Rulings of Adhan & Iqamah for Missed Prayers
            list.add(new StepItem(
                    "Rulings of Adhan & Iqamah for Missed Prayers",
                    "Proclaiming Adhan and Iqamah even at unscheduled times when prayers are missed...",
                    "If prayer is missed in towns, villages, or during travel, it is a duty to proclaim Adhan and Iqamah before offering the missed prayer even outside standard prayer times."
            ));

            // 3. Fajr Missed During Travel & Bilal's Adhan
            list.add(new StepItem(
                    "Fajr Missed During Travel and Bilal's Adhan",
                    "Relocating after sunrise, calling Adhan, and performing missed Fajr prayer...",
                    "When the Prophet (ﷺ) and Companions missed Fajr during travel, they moved to another spot after sunrise, Bilal (RA) called the Adhan, and they prayed Fajr in full accordance with the Sunnah. [Sahih Muslim 681]"
            ));

            // 4. Battle of the Trench: Four Missed Prayers
            list.add(new StepItem(
                    "Battle of the Trench: Four Missed Prayers",
                    "Calling Adhan after Isha to perform Dhuhr, Asr, Maghrib, and Isha sequentially...",
                    "During the Battle of the Trench (Khandaq), when four prayers were missed, they called Adhan after Isha and performed Dhuhr, Asr, Maghrib, and Isha sequentially. [Musnad Ahmad, Irwa al-Ghalil 1/257]"
            ));
        }

        return list;
    }
}
