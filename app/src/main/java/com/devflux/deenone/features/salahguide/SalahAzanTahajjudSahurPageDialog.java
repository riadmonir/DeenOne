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

public class SalahAzanTahajjudSahurPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান" : "Adhan for Tahajjud and Sahur");

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
            // 1. তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান",
                    "ফজরের পূর্বে সেহ্রীর আযান, সহীহ হাদীসের প্রমাণ ও সুন্নাহ বনাম বিদআত... [বুখারী, মুসলিম]",
                    "মহানবী (ﷺ) বলেন, বিলাল রাতে (ফজরের পূর্বে) আযান দেয়। সুতরাং ইবনে উম্মে মাকতূম (ফজরের) আযান না দেওয়া পর্যন্ত তোমরা খাও ও পান কর।\n\n[বুখারী, মুসলিম, মিশকাত ৬৮০নং]\n\nউক্ত হাদীস দ্বারা প্রমাণিত হয় যে, ফজরের পূর্বে তাহাজ্জুদ ও সেহ্রীর আযান মহানবী (ﷺ) এর যুগে প্রচলিত ছিল এবং আজও পর্যন্ত সে সুন্নত মক্কা-মদ্বীনা সহ্ সঊদী আরবের প্রায় সকল স্থানে সেহ্রীর ঐ আযান (বিশেষ করে রমযানে) শুনতে পাওয়া যায়। পক্ষান্তরে আমাদের দেশে প্রায় সকল স্থানে ঐ সময়ে আযানের পরিবর্তে শোনা যায় কুরআন ও গজল পাঠ! সুতরাং এ কথা নিঃসন্দেহে বলা যায় যে, সুন্নতের জায়গা দখল করেছে মনগড়া বিদআত।\n\nঅনেকে বলে থাকেন, উভয় সময়ে আযান হলে লোকেরা গোলমালে পড়বে; সেটা সেহ্রীর না ফজরের আযান -এ নিয়ে সন্দেহে পড়বে। কিন্তু পৃথক পৃথক উভয় সময়ের জন্য নির্দিষ্ট দু’জন মুআযযিন আযান দিলে গোলমালের ভয় থাকে না। তা ছাড়া সেহ্রীর আযানে\n\nالصَّلاَةُ خَيْرٌ مِّنَ النَّوْم\n\nশব্দ থাকবে না। অতএব সকল প্রকার ওজর-আপত্তি ত্যাগ করে বিদআত বর্জন করতে এবং সুন্নাহর উপর আমল করতে আল্লাহ আমাদের তওফীক দিন। আমীন।"
            ));

            // 2. তাহাজ্জুদ ও সেহ্রীর আযানের হাদীস
            list.add(new StepItem(
                    "তাহাজ্জুদ ও সেহ্রীর আযানের হাদীস",
                    "বিলাল (রাঃ)-এর রাতের আযান ও ইবনে উম্মে মাকতূম (রাঃ)-এর আযান... [বুখারী, মুসলিম ৬৮০নং]",
                    "মহানবী (ﷺ) বলেন, বিলাল রাতে (ফজরের পূর্বে) আযান দেয়। সুতরাং ইবনে উম্মে মাকতূম (ফজরের) আযান না দেওয়া পর্যন্ত তোমরা খাও ও পান কর।\n\n[বুখারী, মুসলিম, মিশকাত ৬৮০নং]"
            ));

            // 3. সুন্নাত প্রমাণ ও প্রচলিত বিদআত বর্জন
            list.add(new StepItem(
                    "সুন্নাত প্রমাণ ও প্রচলিত বিদআত বর্জন",
                    "নবীযুগের আমল, মক্কা-মদীনায় প্রচলিত সুন্নত এবং কুরআন-গজল পাঠের বিদআত...",
                    "উক্ত হাদীস দ্বারা প্রমাণিত হয় যে, ফজরের পূর্বে তাহাজ্জুদ ও সেহ্রীর আযান মহানবী (ﷺ) এর যুগে প্রচলিত ছিল এবং আজও পর্যন্ত সে সুন্নত মক্কা-মদ্বীনা সহ্ সঊদী আরবের প্রায় সকল স্থানে সেহ্রীর ঐ আযান (বিশেষ করে রমযানে) শুনতে পাওয়া যায়। পক্ষান্তরে আমাদের দেশে প্রায় সকল স্থানে ঐ সময়ে আযানের পরিবর্তে শোনা যায় কুরআন ও গজল পাঠ! সুতরাং এ কথা নিঃসন্দেহে বলা যায় যে, সুন্নতের জায়গা দখল করেছে মনগড়া বিদআত।"
            ));

            // 4. গোলমাল নিরসন ও সেহ্রীর আযানের বৈশিষ্ট্য
            list.add(new StepItem(
                    "গোলমাল নিরসন ও সেহ্রীর আযানের বৈশিষ্ট্য",
                    "পৃথক মুআযযিন নির্ধারণ ও ‘আস-সালাতু খাইরুম মিনান নাওম’ বর্জন...",
                    "অনেকে বলে থাকেন, উভয় সময়ে আযান হলে লোকেরা গোলমালে পড়বে; সেটা সেহ্রীর না ফজরের আযান -এ নিয়ে সন্দেহে পড়বে। কিন্তু পৃথক পৃথক উভয় সময়ের জন্য নির্দিষ্ট দু’জন মুআযযিন আযান দিলে গোলমালের ভয় থাকে না। তা ছাড়া সেহ্রীর আযানে\n\nالصَّلاَةُ خَيْرٌ مِّنَ النَّوْم\n\nশব্দ থাকবে না। অতএব সকল প্রকার ওজর-আপত্তি ত্যাগ করে বিদআত বর্জন করতে এবং সুন্নাহর উপর আমল করতে আল্লাহ আমাদের তওফীক দিন। আমীন।"
            ));

        } else {
            // English Mode
            // 1. Adhan for Tahajjud and Sahur (Full Narrative)
            list.add(new StepItem(
                    "Adhan for Tahajjud and Sahur",
                    "Adhan before Fajr, prophetic evidence, and adhering to Sunnah... [Bukhari, Muslim]",
                    "The Prophet (ﷺ) said: 'Bilal calls the Adhan during the night (before Fajr). Therefore, continue eating and drinking until Ibn Umm Maktum calls the (Fajr) Adhan.'\n\n[Sahih Bukhari, Sahih Muslim, Mishkat 680]\n\nThis hadith proves that calling the Adhan before Fajr for Tahajjud and Sahur was practiced during the era of the Prophet (ﷺ), and to this day this Sunnah is heard throughout Saudi Arabia, including Makkah and Madinah (especially during Ramadan). In contrast, in our countries, instead of the Adhan at that time, recitations of the Quran and singing of Ghazals/nasheeds are often heard! Therefore, it can be said without doubt that man-made innovations (Bid'ah) have taken the place of the Sunnah.\n\nSome people argue that if Adhan is called at both times, people will get confused as to whether it is the Sahur Adhan or the Fajr Adhan. However, having two distinct Mu'adhins designated for the two different times eliminates any risk of confusion. Furthermore, the Sahur Adhan does not contain the phrase:\n\nالصَّلاَةُ خَيْرٌ مِّنَ النَّوْم\n\n(Prayer is better than sleep). Therefore, may Allah grant us the ability to abandon all excuses, shun innovations, and practice upon the authentic Sunnah. Ameen."
            ));

            // 2. Hadith on Adhan for Tahajjud and Sahur
            list.add(new StepItem(
                    "Hadith on Adhan for Tahajjud and Sahur",
                    "Bilal's call at night and Ibn Umm Maktum's Fajr call... [Bukhari, Muslim 680]",
                    "The Prophet (ﷺ) said: 'Bilal calls the Adhan during the night (before dawn). Therefore, eat and drink until Ibn Umm Maktum calls the Adhan.'\n\n[Sahih Bukhari, Sahih Muslim, Mishkat 680]"
            ));

            // 3. Prophetic Sunnah vs. Modern Innovations
            list.add(new StepItem(
                    "Prophetic Sunnah vs. Modern Innovations",
                    "Established practice in Makkah-Madinah and replacing Adhan with songs...",
                    "This hadith establishes that calling Adhan before Fajr for Tahajjud and Sahur is an authentic Sunnah practiced during the Prophet's time and preserved today in Makkah and Madinah. Replacing this Sunnah with playing songs or loudspeakers reciting poetry is an unauthorized innovation."
            ));

            // 4. Preventing Confusion & Distinct Characteristics
            list.add(new StepItem(
                    "Preventing Confusion & Distinct Characteristics",
                    "Assigning two Mu'adhins and omitting Tathwib in Sahur Adhan...",
                    "To avoid confusion between the two calls, two distinct Mu'adhins should be appointed. Furthermore, the Sahur Adhan does not include 'As-Salatu khayrum minan-nawm'. Muslims should abandon innovations and follow the Sunnah."
            ));
        }

        return list;
    }
}
