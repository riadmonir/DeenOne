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

public class SalahAzanDuaPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "আযান ও ইকামতের মাঝে দুআ" : "Dua Between Adhan and Iqamah");

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
            // 1. আযান ও ইকামতের মাঝে দুআ (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "আযান ও ইকামতের মাঝে দুআ",
                    "দুআ কবুলের সময়, হাদীসের বাণী ও ইকামত ও জিহাদের কাতারে দুআ... [আহমাদ, আবূদাঊদ, হাকেম]",
                    "আযান হওয়ার পর এবং ইকামত হওয়ার পূর্বের সময়ে দুআ কবুল হয়ে থাকে। তাই এই সময় দুনিয়া ও আখেরাতের মঙ্গল আল্লাহর নিকট প্রার্থনা করা উচিৎ। মহানবী (ﷺ) বলেন, “আযান ও ইকামতের মাঝে দুআ রদ্দ্ করা হয় না। (অর্থাৎ মঞ্জুর করা হয়।)\n\n[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান ৫২১নং, তিরমিযী, সুনান]\n\nএক বর্ণনায় আছে, “সুতরাং তোমরা এ সময়ে দুআ কর।\n\n[জামে ৩৪০৫ নং]\n\nতিনি আরো বলেন, “দু’টি সময়ে দুআ (প্রার্থনা)কারীর দুআ রদ্দ্ হয় না; যখন নামাযের ইকামত হয় এবং জিহাদের কাতারে।\n\n[হাকেম, মুস্তাদরাক, মালেক, মুঅত্তা, সহিহ তারগিব ২৬০ নং]"
            ));

            // 2. আযান ও ইকামতের মাঝে দুআ কবুল হওয়ার হাদীস
            list.add(new StepItem(
                    "আযান ও ইকামতের মাঝে দুআ কবুল হওয়ার হাদীস",
                    "আযান ও ইকামতের মধ্যবর্তী সময়ে দুআ রদ্দ্ না হওয়ার সুসংবাদ... [আহমাদ, আবূদাঊদ ৫২১নং, তিরমিযী]",
                    "আযান হওয়ার পর এবং ইকামত হওয়ার পূর্বের সময়ে দুআ কবুল হয়ে থাকে। তাই এই সময় দুনিয়া ও আখেরাতের মঙ্গল আল্লাহর নিকট প্রার্থনা করা উচিৎ। মহানবী (ﷺ) বলেন, “আযান ও ইকামতের মাঝে দুআ রদ্দ্ করা হয় না। (অর্থাৎ মঞ্জুর করা হয়।)\n\n[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান ৫২১নং, তিরমিযী, সুনান]"
            ));

            // 3. এ সময়ে দুআ করার বিশেষ নির্দেশ
            list.add(new StepItem(
                    "এ সময়ে দুআ করার বিশেষ নির্দেশ",
                    "রাসূলুল্লাহর (ﷺ) নির্দেশ: “সুতরাং তোমরা এ সময়ে দুআ কর”... [জামে ৩৪০৫ নং]",
                    "এক বর্ণনায় আছে, “সুতরাং তোমরা এ সময়ে দুআ কর।\n\n[জামে ৩৪০৫ নং]"
            ));

            // 4. দুআ রদ্দ্ না হওয়ার দুটি বিশেষ মুহূর্ত
            list.add(new StepItem(
                    "দুআ রদ্দ্ না হওয়ার দুটি বিশেষ মুহূর্ত",
                    "ইকামত চলাকালীন ও জিহাদের কাতারে প্রার্থনা কবুল হওয়ার হাদীস... [হাকেম, মালেক, সহিহ তারগিব ২৬০ নং]",
                    "তিনি আরো বলেন, “দু’টি সময়ে দুআ (প্রার্থনা)কারীর দুআ রদ্দ্ হয় না; যখন নামাযের ইকামত হয় এবং জিহাদের কাতারে।\n\n[হাকেম, মুস্তাদরাক, মালেক, মুঅত্তা, সহিহ তারগিব ২৬০ নং]"
            ));

        } else {
            // English Mode
            // 1. Dua Between Adhan & Iqamah (Full Narrative)
            list.add(new StepItem(
                    "Dua Between Adhan and Iqamah",
                    "Acceptance of supplication, prophetic command, and moments when dua is not rejected... [Ahmad, Abu Dawud, Hakim]",
                    "Supplications are answered in the time between the Adhan and before the Iqamah. Therefore, one should ask Allah for good in this world and the Hereafter during this time. The Prophet (ﷺ) said: 'Supplication made between the Adhan and the Iqamah is not rejected (i.e. it is granted).'\n\n[Musnad Ahmad, Sunan Abi Dawud 521, Jami at-Tirmidhi]\n\nIn another narration: 'So supplicate during this time.'\n\n[Al-Jami' 3405]\n\nHe also said: 'In two moments the prayer of a supplicant is never rejected: when the Iqamah for prayer is called, and in the battlefield row of Jihad.'\n\n[Mustadrak al-Hakim, Muwatta Malik, Sahih at-Targhib 260]"
            ));

            // 2. Hadith on Dua Acceptance Between Adhan & Iqamah
            list.add(new StepItem(
                    "Hadith on Dua Acceptance Between Adhan & Iqamah",
                    "Prophetic glad tidings that supplication is not rejected between the two calls... [Ahmad, Abu Dawud 521]",
                    "Supplications are answered in the time between the Adhan and before the Iqamah. Therefore, one should ask Allah for good in this world and the Hereafter during this time. The Prophet (ﷺ) said: 'Supplication made between the Adhan and the Iqamah is not rejected (i.e. it is granted).'\n\n[Musnad Ahmad, Sunan Abi Dawud 521, Jami at-Tirmidhi]"
            ));

            // 3. Prophetic Instruction to Supplicate
            list.add(new StepItem(
                    "Prophetic Instruction to Supplicate",
                    "The explicit command of the Messenger of Allah (ﷺ) to make dua during this interval... [Al-Jami' 3405]",
                    "In another narration: 'So supplicate during this time.'\n\n[Al-Jami' 3405]"
            ));

            // 4. Two Moments When Supplication is Not Rejected
            list.add(new StepItem(
                    "Two Moments When Supplication is Not Rejected",
                    "Supplication during Iqamah and in the battlefield rows of Jihad... [Mustadrak al-Hakim, Muwatta Malik]",
                    "He also said: 'In two moments the prayer of a supplicant is never rejected: when the Iqamah for prayer is called, and in the battlefield row of Jihad.'\n\n[Mustadrak al-Hakim, Muwatta Malik, Sahih at-Targhib 260]"
            ));
        }

        return list;
    }
}
