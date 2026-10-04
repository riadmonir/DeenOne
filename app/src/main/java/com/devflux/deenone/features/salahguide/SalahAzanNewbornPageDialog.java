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

public class SalahAzanNewbornPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "সন্তান ভূমিষ্ঠ হলে আযান" : "Adhan Upon the Birth of a Child");

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
            // 1. সন্তান ভূমিষ্ঠ হলে আযান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "সন্তান ভূমিষ্ঠ হলে আযান",
                    "নবজাতকের কানে আযানের হাদীস, হুকুম ও জাল হাদীসের তাহক্বীক্ব... [আবূদাঊদ, তিরমিযী, আলবানী]",
                    "আবূ রাফে (রাঃ) বলেন, আমি আল্লাহর রসূল (ﷺ) কে দেখেছি, ফাতেমা (রাঃ) হাসান বিন আলীকে প্রসব করলে তিনি তাঁর (হাসানের) কানে নামাযের আযান দিলেন।\n\n[আবূদাঊদ, সুনান ৫১০৫, তিরমিযী, সুনান ১৫৬৬, মিশকাত ৪১৫৭ নং]\n\nসুতরাং ছেলে-মেয়ে সকলের কানে ঐ সময় নামাযের জন্য আযান দেওয়ার মতই আযান দেওয়া সুন্নত। (মতান্তরে হাদীসটি যয়ীফ, অতএব এ সময় আযান সুন্নত নয়।) পক্ষান্তরে ডান কানে আযান এবং বাম কানে ইকামত দিলে ‘উম্মুস সিবয়্যান (ভূত,পেত) বা এক প্রকার রোগ কোন ক্ষতি করতে না পারারহাদীসটি জাল।\n\n[সিলসিলাহ যায়ীফাহ, আলবানী ৩২১নং, জামে ৫৮৮১, ইরওয়াউল গালীল, আলবানী ১১৭৪নং]"
            ));

            // 2. হাসান (রাঃ)-এর কানে রাসুলুল্লাহর (ﷺ) আযান
            list.add(new StepItem(
                    "হাসান (রাঃ)-এর কানে রাসুলুল্লাহর (ﷺ) আযান",
                    "হযরত আবু রাফে (রাঃ) হতে বর্ণিত নবজাতক হাসানের কানে আযানের বর্ণনা...",
                    "আবূ রাফে (রাঃ) বলেন, আমি আল্লাহর রসূল (ﷺ) কে দেখেছি, ফাতেমা (রাঃ) হাসান বিন আলীকে প্রসব করলে তিনি তাঁর (হাসানের) কানে নামাযের আযান দিলেন।\n\n[আবূদাঊদ, সুনান ৫১০৫, তিরমিযী, সুনান ১৫৬৬, মিশকাত ৪১৫৭ নং]"
            ));

            // 3. নবজাতকের কানে আযানের হুকুম ও তাহক্বীক্ব
            list.add(new StepItem(
                    "নবজাতকের কানে আযানের হুকুম ও তাহক্বীক্ব",
                    "ছেলে-মেয়ে উভয়ের কানে আযান দেওয়ার সুন্নত ও আলেমদের পর্যালোচনা...",
                    "সুতরাং ছেলে-মেয়ে সকলের কানে ঐ সময় নামাযের জন্য আযান দেওয়ার মতই আযান দেওয়া সুন্নত। (মতান্তরে হাদীসটি যয়ীফ, অতএব এ সময় আযান সুন্নত নয়।)"
            ));

            // 4. ডান কানে আযান ও বাম কানে ইকামত সংক্রান্ত জাল হাদিস
            list.add(new StepItem(
                    "ডান কানে আযান ও বাম কানে ইকামত সংক্রান্ত জাল হাদিস",
                    "উম্মুস সিবয়্যান রোগ মুক্তির জন্য ডান কানে আযান ও বাম কানে ইকামতের হাদিস জাল...",
                    "পক্ষান্তরে ডান কানে আযান এবং বাম কানে ইকামত দিলে ‘উম্মুস সিবয়্যান (ভূত,পেত) বা এক প্রকার রোগ কোন ক্ষতি করতে না পারারহাদীসটি জাল।\n\n[সিলসিলাহ যায়ীফাহ, আলবানী ৩২১নং, জামে ৫৮৮১, ইরওয়াউল গালীল, আলবানী ১১৭৪নং]"
            ));

        } else {
            // English Mode
            // 1. Adhan Upon the Birth of a Child (Full Narrative)
            list.add(new StepItem(
                    "Adhan Upon the Birth of a Child",
                    "Hadith on calling Adhan for newborns and research on fabricated reports... [Abu Dawud, Tirmidhi, Albani]",
                    "Abu Rafi (RA) said: I saw the Messenger of Allah (ﷺ) give the call to prayer (Adhan) in the ear of Hasan ibn Ali when Fatimah gave birth to him.\n\n[Sunan Abi Dawud 5105, Jami at-Tirmidhi 1566, Mishkat 4157]\n\nTherefore, calling Adhan in the ear of both boys and girls at that time just like the regular call to prayer is Sunnah. (According to other scholars, this hadith is Da'if/weak, hence calling Adhan at this time is not Sunnah.) On the other hand, the narration stating that calling Adhan in the right ear and Iqamah in the left ear protects the child from Ummus-Sibyan (evil spirit/affliction) is fabricated (Mawdu').\n\n[Silsilah Da'ifah, al-Albani 321, Al-Jami' 5881, Irwa al-Ghalil, al-Albani 1174]"
            ));

            // 2. The Prophet (ﷺ) Calling Adhan for Newborn Hasan (RA)
            list.add(new StepItem(
                    "The Prophet (ﷺ) Calling Adhan for Newborn Hasan (RA)",
                    "Narration of Abu Rafi regarding Adhan given in the ear of Hasan (RA)...",
                    "Abu Rafi (RA) said: I saw the Messenger of Allah (ﷺ) give the call to prayer (Adhan) in the ear of Hasan ibn Ali when Fatimah gave birth to him. [Sunan Abi Dawud 5105, Jami at-Tirmidhi 1566, Mishkat 4157]"
            ));

            // 3. Rulings & Scholarly Views on Newborn Adhan
            list.add(new StepItem(
                    "Rulings & Scholarly Views on Newborn Adhan",
                    "Sunnah of calling Adhan for male and female newborns and scholarly authentication...",
                    "Calling Adhan in the ear of male and female newborns is considered Sunnah based on this narration (some scholars categorize the narration as weak, deeming it non-Sunnah)."
            ));

            // 4. Fabricated Hadith on Right Ear Adhan & Left Ear Iqamah
            list.add(new StepItem(
                    "Fabricated Hadith on Right Ear Adhan & Left Ear Iqamah",
                    "Research showing the narration on protection from Ummus-Sibyan is fabricated...",
                    "The narration claiming that calling Adhan in the right ear and Iqamah in the left ear prevents Ummus-Sibyan (ailments/spirits) is fabricated (Mawdu'). [Silsilah Da'ifah 321, Al-Jami' 5881, Irwa al-Ghalil 1174]"
            ));
        }

        return list;
    }
}
