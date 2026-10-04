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

public class SalahAzanLeavingMosquePageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "আযানের পর মসজিদ থেকে বের হওয়া" : "Leaving the Mosque After Adhan");

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
            // 1. আযানের পর মসজিদ থেকে বের হওয়া (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "আযানের পর মসজিদ থেকে বের হওয়া",
                    "বিনা ওজরে নামায না পড়ে বের হওয়ার নিষেধাজ্ঞা ও হাদীসের হুশিয়ারী... [আহমাদ, মুসলিম, ইবনে মাজাহ্]",
                    "আযান হয়ে গেলে বিনা ওজরে নামায না পড়ে মসজিদ থেকে বের হয়ে যাওয়া বৈধ নয়।\n\nমহানবী (ﷺ) বলেন, “মসজিদে অবস্থানকালে আযান হলেই তোমাদের কেউ যেন নামায না পড়া পর্যন্ত মসজিদ থেকে বের না হয়।\n\n[আহমাদ, মুসনাদ, মিশকাত ১০৭৪ নং]\n\nএক ব্যক্তি আযানের পর মসজিদ হতে বের হয়ে গেলে আবূ হুরাইরা তার প্রতি ইঙ্গিত করে বললেন, ‘এ লোকটা তো আবুল কাসেম (ﷺ) এর নাফরমানী করল।’\n\n[মুসলিম, আবূদাঊদ, সুনান ৫৩৬ নং, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, বায়হাকী]\n\nআল্লাহর রসূল (ﷺ) বলেন, “যে ব্যক্তির মসজিদে থাকা অবস্থায় আযান হয়, অতঃপর বিনা কোন প্রয়োজনে বের হয়ে যায় এবং ফিরে আসার ইচ্ছা না রাখে সে ব্যক্তি মুনাফিক।\n\n[ইবনে মাজাহ্, সুনান, সহিহ তারগিব ১৫৭নং]"
            ));

            // 2. আযানের পর বিনা ওজরে বের হওয়ার নিষেধাজ্ঞা
            list.add(new StepItem(
                    "আযানের পর বিনা ওজরে বের হওয়ার নিষেধাজ্ঞা",
                    "মসজিদে অবস্থানকালে আযান হলে নামায না পড়ে বের না হওয়ার নির্দেশ...",
                    "আযান হয়ে গেলে বিনা ওজরে নামায না পড়ে মসজিদ থেকে বের হয়ে যাওয়া বৈধ নয়।\n\nমহানবী (ﷺ) বলেন, “মসজিদে অবস্থানকালে আযান হলেই তোমাদের কেউ যেন নামায না পড়া পর্যন্ত মসজিদ থেকে বের না হয়।\n\n[আহমাদ, মুসনাদ, মিশকাত ১০৭৪ নং]"
            ));

            // 3. আবূ হুরাইরা (রাঃ)-এর সতর্কবার্তা ও আবুল কাসেমের (ﷺ) নাফরমানী
            list.add(new StepItem(
                    "আবূ হুরাইরা (রাঃ)-এর সতর্কবার্তা ও আবুল কাসেমের (ﷺ) নাফরমানী",
                    "আযানের পর বের হওয়া ব্যক্তিকে রাসুলুল্লাহর (ﷺ) নাফরমান হিসেবে গণ্য করার বর্ণনা...",
                    "এক ব্যক্তি আযানের পর মসজিদ হতে বের হয়ে গেলে আবূ হুরাইরা তার প্রতি ইঙ্গিত করে বললেন, ‘এ লোকটা তো আবুল কাসেম (ﷺ) এর নাফরমানী করল।’\n\n[মুসলিম, আবূদাঊদ, সুনান ৫৩৬ নং, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, বায়হাকী]"
            ));

            // 4. বিনা প্রয়োজনে বের হওয়া ও মুনাফেকীর হুশিয়ারী
            list.add(new StepItem(
                    "বিনা প্রয়োজনে বের হওয়া ও মুনাফেকীর হুশিয়ারী",
                    "আযানের পর অপ্রয়োজনে বের হওয়া এবং ফিরে না আসার নিয়তকারীদের সম্পর্কে হুশিয়ারী...",
                    "আল্লাহর রসূল (ﷺ) বলেন, “যে ব্যক্তির মসজিদে থাকা অবস্থায় আযান হয়, অতঃপর বিনা কোন প্রয়োজনে বের হয়ে যায় এবং ফিরে আসার ইচ্ছা না রাখে সে ব্যক্তি মুনাফিক।\n\n[ইবনে মাজাহ্, সুনান, সহিহ তারগিব ১৫৭নং]"
            ));

        } else {
            // English Mode
            // 1. Leaving the Mosque After Adhan (Full Narrative)
            list.add(new StepItem(
                    "Leaving the Mosque After Adhan",
                    "Prohibition of leaving the mosque after Adhan without valid excuse... [Ahmad, Muslim, Ibn Majah]",
                    "Once the Adhan has been proclaimed, it is not permissible to leave the mosque without a valid excuse before offering the prayer.\n\nThe Prophet (ﷺ) said: 'When the Adhan is called while any of you is in the mosque, let none of you leave until he has prayed.'\n\n[Musnad Ahmad, Mishkat 1074]\n\nWhen a man walked out of the mosque after the Adhan had been proclaimed, Abu Hurairah gestured towards him and said: 'As for this man, he has disobeyed Abul Qasim (ﷺ).'\n\n[Sahih Muslim, Sunan Abi Dawud 536, Tirmidhi, Ibn Majah, Darimi, Bayhaqi]\n\nThe Messenger of Allah (ﷺ) said: 'Whoever is in the mosque when the call to prayer is proclaimed, then leaves without a necessity and with no intention of returning, is a hypocrite (Munafiq).'\n\n[Sunan Ibn Majah, Sahih at-Targhib 157]"
            ));

            // 2. Prohibition of Leaving Without an Excuse
            list.add(new StepItem(
                    "Prohibition of Leaving Without an Excuse",
                    "Command not to depart until prayer is performed when present during Adhan...",
                    "The Prophet (ﷺ) said: 'When the Adhan is proclaimed while you are in the mosque, none of you should leave until he offers the prayer.' [Musnad Ahmad, Mishkat 1074]"
            ));

            // 3. Abu Hurairah's Statement on Disobeying Abul Qasim (ﷺ)
            list.add(new StepItem(
                    "Abu Hurairah's Statement on Disobeying Abul Qasim (ﷺ)",
                    "Warning that leaving after Adhan without excuse constitutes disobedience...",
                    "Abu Hurairah (RA) noted that anyone leaving the mosque after the Adhan has disobeyed Abul Qasim (the Prophet ﷺ). [Sahih Muslim, Sunan Abi Dawud 536]"
            ));

            // 4. Warning of Hypocrisy for Departing Without Need
            list.add(new StepItem(
                    "Warning of Hypocrisy for Departing Without Need",
                    "Prophetic warning regarding leaving without need and without intending to return...",
                    "The Messenger of Allah (ﷺ) said: 'Whoever is in the mosque when Adhan is called, then leaves without necessity and has no intention of returning, is a hypocrite.' [Sunan Ibn Majah, Sahih at-Targhib 157]"
            ));
        }

        return list;
    }
}
