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

public class SalahIqamahRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইকামত" : "Rules of Iqamah");

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
            // 1. ইকামত (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "ইকামত",
                    "ইকামতের বাক্যসমূহ, প্রসিদ্ধ রূপ, বিভিন্ন বর্ণনা ও ভুলে না দেওয়ার বিধান... [বুখারী, মুসলিম, আবূদাঊদ]",
                    "যেমন দুই মুআযযিনের আযান দুই রকম ছিল, তেমনি উভয়ের ইকামতও ছিল দুই রকম; জোড় এবং বিজোড়। বিলাল (রাঃ) কে আযান ডবল ডবল শব্দে এবং ইকামত ‘ক্বাদ ক্বা-মাতিস সলা-হ্ ছাড়া (অন্যান্য) বাক্যাবলীকে একক একক শব্দে বলতে আদেশ করা হয়েছিল।\n\n[বুখারী, মুসলিম, সহীহ প্রমুখ, মিশকাত ৬৪১ নং]\n\nসুতরাং বিলালের উক্ত হাদীসের ভিত্তিতে ইকামত হবে ৯টি বাক্যে; ‘ক্বাদ ক্বামাতিস সালাহ্ ২বার এবং বাকী হবে ১ বার করে।\n\n[আলমুমতে, শারহে ফিক্হ, ইবনে উষাইমীন ২/৫৯]\n\nকিন্তু আব্দুল্লাহ বিন যায়দকে স্বপ্নে শিখানো হয়েছিল নিম্নরুপ ইকামত, আর এটাই প্রসিদ্ধ:-\n\nاَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله، أَشْهَدُ أَنَّ مُحَمَّداً رَّسُوْلُ الله، حَيَّ عَلَى الصَّلاَة،\n\nحَيَّ عَلَى الْفَلاَح، قَدْ قَامَتِ الصَّلاَة، قَدْ قَامَتِ الصَّلاَة، اَللهُ أَكْبَر اَللهُ أَكْبَر، لاَ إِلهَ إِلاَّ الله،\n\nআল্লাহু আকবার ২ বার। আশহাদু আল লা ইলাহা ইল্লাল্লাহ্ ১ বার। আশহাদু আন্না মুহাম্মাদার রাসূলুল্লাহ্ ১ বার। হাইয়্যা আলাস স্বলাহ্ ১ বার। হাইয়্যা আলাল ফালাহ্ ১ বার। ক্বাদ ক্বামাতিস স্বলাহ্ (অর্থাৎ নামায প্রতিষ্ঠা বা শুরু হল) ২ বার। আল্লাহু আকবার ২বার এবং লা ইলাহা ইল্লাল্লাহ্ ১ বার।\n\n[আবূদাঊদ, সুনান ৪৯৯, দারেমী, সুনান ১১৭১, ইবনে খুযাইমাহ্, সহীহ ৩৭০, ইবনে হিব্বান, সহীহ ১৬৭১নং, বায়হাকী ১/৩৯১]\n\nউল্লেখ্য যে, যারা মুআযযিন আবূ মাহ্যূরার মত তারজী’ আযান দেয়, তাদের উচিৎ তাঁর মতই ইকামত দেওয়া। তিনি বলেন, ‘মহানবী (ﷺ) তাঁকে আযানের ১৯টি এবং ইকামতের ১৭টি বাক্য শিখিয়েছেন।’\n\n[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান, তিরমিযী, সুনান, নাসাঈ, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, মিশকাত ৬৪৪নং]\n\nসুতরাং তাঁর ইকামত ছিল বিলাল (রাঃ) এর আযানের মতই। তবে তাতে ‘হাইয়্যা আলাল ফালাহ্ এর পর অতিরিক্ত ছিল ‘ক্বাদ ক্বামাতিস স্বলাহ্ ২ বার।\n\n[আবূদাঊদ, সুনান ৫০২নং]\n\nইমাম ইবনে তাইমিয়্যাহ্ (রহঃ) বলেন, আহলে হাদীস ও তাঁদের সমর্থকদের নিকট সঠিক সিদ্ধান্ত এই যে, মহানবী (ﷺ) হতে যা কিছু শুদ্ধভাবে প্রমাণিত আছে তার প্রত্যেকটার উপর আমল করতে হবে। আর তাঁরা ঐ আমলের কোনটিকেও অপছন্দ করেন না। কেননা, আযান ও ইকামতের পদ্ধতি একাধিক হওয়ার ব্যাপারটা ক্বিরাআত, তাশাহহুদ প্রভৃতির পদ্ধতি একাধিক হওয়ার মতই।’\n\n[মাজমূউ ফাতাওয়া ২২/৩৩৫, ২২/৬৬]\n\nসুতরাং উভয় প্রকারই আযান ও ইকামত আমলযোগ্য। আর বৈধ নয় এ নিয়ে কাদা ছুঁড়াছুঁড়ি।\n\nপ্রকাশ থাকে যে, ভুলে ইকামত না দিয়ে (একাকী অথবা জামাআতী) নামায পড়ে ফেললে নামাযের কোন ক্ষতি হয় না। ইকামত নামায হতে পৃথক জিনিস। অতএব ঐ ভুলের জন্য সহু সিজদা বিধেয় নয়।\n\n[তুহ্ফাতুল ইখওয়ান, ইবনে বায ৭৮পৃ:]"
            ));

            // 2. বিলাল (রাঃ)-এর ইকামত ও বাক্য সংখ্যা
            list.add(new StepItem(
                    "বিলাল (রাঃ)-এর ইকামত ও বাক্য সংখ্যা",
                    "ইকামত বিজোড় শব্দে একক একক বলার বিধান... [বুখারী, মুসলিম, মিশকাত ৬৪১ নং; আলমুমতে ২/৫৯]",
                    "যেমন দুই মুআযযিনের আযান দুই রকম ছিল, তেমনি উভয়ের ইকামতও ছিল দুই রকম; জোড় এবং বিজোড়। বিলাল (রাঃ) কে আযান ডবল ডবল শব্দে এবং ইকামত ‘ক্বাদ ক্বা-মাতিস সলা-হ্ ছাড়া (অন্যান্য) বাক্যাবলীকে একক একক শব্দে বলতে আদেশ করা হয়েছিল।\n\n[বুখারী, মুসলিম, সহীহ প্রমুখ, মিশকাত ৬৪১ নং]\n\nসুতরাং বিলালের উক্ত হাদীসের ভিত্তিতে ইকামত হবে ৯টি বাক্যে; ‘ক্বাদ ক্বামাতিস সালাহ্ ২বার এবং বাকী হবে ১ বার করে।\n\n[আলমুমতে, শারহে ফিক্হ, ইবনে উষাইমীন ২/৫৯]"
            ));

            // 3. প্রসিদ্ধ ইকামত ও বাক্য বিন্যাস
            list.add(new StepItem(
                    "প্রসিদ্ধ ইকামত ও বাক্য বিন্যাস",
                    "আব্দুল্লাহ বিন যায়দ (রাঃ)-এর স্বপ্নে প্রাপ্ত প্রসিদ্ধ ইকামতের শব্দাবলি... [আবূদাঊদ ৪৯৯, দারেমী ১১৭১, বায়হাকী]",
                    "কিন্তু আব্দুল্লাহ বিন যায়দকে স্বপ্নে শিখানো হয়েছিল নিম্নরুপ ইকামত, আর এটাই প্রসিদ্ধ:-\n\nاَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله، أَشْهَدُ أَنَّ مُحَمَّداً رَّسُوْلُ الله، حَيَّ عَلَى الصَّلاَة،\n\nحَيَّ عَلَى الْفَلاَح، قَدْ قَامَتِ الصَّلاَة، قَدْ قَامَتِ الصَّلاَة، اَللهُ أَكْبَر اَللهُ أَكْبَر، لاَ إِلهَ إِلاَّ الله،\n\nআল্লাহু আকবার ২ বার। আশহাদু আল লা ইলাহা ইল্লাল্লাহ্ ১ বার। আশহাদু আন্না মুহাম্মাদার রাসূলুল্লাহ্ ১ বার। হাইয়্যা আলাস স্বলাহ্ ১ বার। হাইয়্যা আলাল ফালাহ্ ১ বার। ক্বাদ ক্বামাতিস স্বলাহ্ (অর্থাৎ নামায প্রতিষ্ঠা বা শুরু হল) ২ বার। আল্লাহু আকবার ২বার এবং লা ইলাহা ইল্লাল্লাহ্ ১ বার।\n\n[আবূদাঊদ, সুনান ৪৯৯, দারেমী, সুনান ১১৭১, ইবনে খুযাইমাহ্, সহীহ ৩৭০, ইবনে হিব্বান, সহীহ ১৬৭১নং, বায়হাকী ১/৩৯১]"
            ));

            // 4. আবূ মাহ্যূরাহ (রাঃ)-এর ১৭ বাক্যের ইকামত
            list.add(new StepItem(
                    "আবূ মাহ্যূরাহ (রাঃ)-এর ১৭ বাক্যের ইকামত",
                    "তারজী’ আযানের মুআযযিনের ১৭ বাক্যের ইকামত... [আহমাদ, আবূদাঊদ ৫০২, তিরমিযী, মিশকাত ৬৪৪নং]",
                    "উল্লেখ্য যে, যারা মুআযযিন আবূ মাহ্যূরার মত তারজী’ আযান দেয়, তাদের উচিৎ তাঁর মতই ইকামত দেওয়া। তিনি বলেন, ‘মহানবী (ﷺ) তাঁকে আযানের ১৯টি এবং ইকামতের ১৭টি বাক্য শিখিয়েছেন।’\n\n[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান, তিরমিযী, সুনান, নাসাঈ, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, মিশকাত ৬৪৪নং]\n\nসুতরাং তাঁর ইকামত ছিল বিলাল (রাঃ) এর আযানের মতই। তবে তাতে ‘হাইয়্যা আলাল ফালাহ্ এর পর অতিরিক্ত ছিল ‘ক্বাদ ক্বামাতিস স্বলাহ্ ২ বার।\n\n[আবূদাঊদ, সুনান ৫০২নং]"
            ));

            // 5. ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ)-এর ফাতওয়া ও আমলযোগ্যতা
            list.add(new StepItem(
                    "ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ)-এর ফাতওয়া ও আমলযোগ্যতা",
                    "একাধিক প্রমাণিত পদ্ধতির গ্রহণযোগ্যতা ও মতভেদ পরিহার... [মাজমূউ ফাতাওয়া ২২/৩৩৫, ২২/৬৬]",
                    "ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ) বলেন, আহলে হাদীস ও তাঁদের সমর্থকদের নিকট সঠিক সিদ্ধান্ত এই যে, মহানবী (ﷺ) হতে যা কিছু শুদ্ধভাবে প্রমাণিত আছে তার প্রত্যেকটার উপর আমল করতে হবে। আর তাঁরা ঐ আমলের কোনটিকেও অপছন্দ করেন না। কেননা, আযান ও ইকামতের পদ্ধতি একাধিক হওয়ার ব্যাপারটা ক্বিরাআত, তাশাহহুদ প্রভৃতির পদ্ধতি একাধিক হওয়ার মতই।’\n\n[মাজমূউ ফাতাওয়া ২২/৩৩৫, ২২/৬৬]\n\nসুতরাং উভয় প্রকারই আযান ও ইকামত আমলযোগ্য। আর বৈধ নয় এ নিয়ে কাদা ছুঁড়াছুঁড়ি।"
            ));

            // 6. ভুলে ইকামত না দিয়ে নামায পড়ার বিধান ও সহু সিজদা
            list.add(new StepItem(
                    "ভুলে ইকামত না দিয়ে নামায পড়ার বিধান ও সহু সিজদা",
                    "ইকামত ছাড়া নামায পড়া এবং সহু সিজদার বিধান... [তুহ্ফাতুল ইখওয়ান, ইবনে বায ৭৮পৃ:]",
                    "প্রকাশ থাকে যে, ভুলে ইকামত না দিয়ে (একাকী অথবা জামাআতী) নামায পড়ে ফেললে নামাযের কোন ক্ষতি হয় না। ইকামত নামায হতে পৃথক জিনিস। অতএব ঐ ভুলের জন্য সহু সিজদা বিধেয় নয়।\n\n[তুহ্ফাতুল ইখওয়ান, ইবনে বায ৭৮পৃ:]"
            ));

        } else {
            // English Mode
            // 1. Rules of Iqamah (Full Narrative)
            list.add(new StepItem(
                    "Rules of Iqamah",
                    "Forms of Iqamah, famous wording, multiple authentic variations, and praying without Iqamah... [Bukhari, Muslim, Abu Dawud]",
                    "Just as the Adhan of the two Mu'adhins differed, so did their Iqamah; even and odd. Bilal (RA) was commanded to call the phrases of Adhan in pairs (doubled) and the phrases of Iqamah singly (odd), except for 'Qad Qamatis-Salah'.\n\n[Sahih Bukhari, Sahih Muslim, Mishkat 641]\n\nTherefore, based on Bilal's hadith, the Iqamah consists of 9 phrases: 'Qad Qamatis-Salah' twice, and the rest once each.\n\n[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 2/59]\n\nHowever, the Iqamah taught to Abdullah ibn Zayd in his dream is as follows, and this is the most widespread and well-known:-\n\nاَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله، أَشْهَدُ أَنَّ مُحَمَّداً رَّسُوْلُ الله، حَيَّ عَلَى الصَّلاَة،\n\nحَيَّ عَلَى الْفَلاَح، قَدْ قَامَتِ الصَّلاَة، قَدْ قَامَتِ الصَّلاَة، اَللهُ أَكْبَر اَللهُ أَكْبَر، لاَ إِلهَ إِلاَّ الله،\n\nAllahu Akbar 2 times. Ash-hadu alla ilaha illallah 1 time. Ash-hadu anna Muhammadar Rasulullah 1 time. Hayya 'alas-Salah 1 time. Hayya 'alal-Falah 1 time. Qad Qamatis-Salah (prayer is established/started) 2 times. Allahu Akbar 2 times, and La ilaha illallah 1 time.\n\n[Sunan Abi Dawud 499, Sunan ad-Darimi 1171, Sahih Ibn Khuzaymah 370, Sahih Ibn Hibban 1671, Bayhaqi 1/391]\n\nIt is noteworthy that those who call the Tarjee' Adhan like Mu'adhin Abu Mahdhurah should also call the Iqamah like his. He reported that the Prophet (ﷺ) taught him 19 phrases for Adhan and 17 phrases for Iqamah.\n\n[Musnad Ahmad, Sunan Abi Dawud, Jami at-Tirmidhi, Sunan an-Nasa'i, Sunan Ibn Majah, Sunan ad-Darimi, Mishkat 644]\n\nThus, his Iqamah was similar to Bilal's Adhan, except that after 'Hayya 'alal-Falah', it included 'Qad Qamatis-Salah' twice.\n\n[Sunan Abi Dawud 502]\n\nImam Ibn Taymiyyah (RA) stated: 'The correct view among the Ahl al-Hadith and their supporters is that whatever has been authentically transmitted from the Prophet (ﷺ) should be acted upon, and they do not dislike any of those practices. This is because the existence of multiple methods for Adhan and Iqamah is like the multiple authentic methods of recitation (Qira'at) and Tashahhud.'\n\n[Majmu' al-Fatawa 22/335, 22/66]\n\nTherefore, both types of Adhan and Iqamah are valid and actionable, and disputes or hostility regarding them are not permissible.\n\nIt should be noted that if someone mistakenly offers prayer (individually or in congregation) without calling the Iqamah, the validity of the prayer is not affected. Iqamah is distinct from the prayer itself; hence Sajdah Sahw is not required for this omission.\n\n[Tuhfat al-Ikhwan, Ibn Baz p. 78]"
            ));

            // 2. Bilal's Iqamah and Number of Phrases
            list.add(new StepItem(
                    "Bilal's Iqamah and Number of Phrases",
                    "Calling the phrases of Iqamah singly (odd)... [Bukhari, Muslim, Mishkat 641]",
                    "Just as the Adhan of the two Mu'adhins differed, so did their Iqamah; even and odd. Bilal (RA) was commanded to call the phrases of Adhan in pairs (doubled) and the phrases of Iqamah singly (odd), except for 'Qad Qamatis-Salah'. [Sahih Bukhari, Sahih Muslim, Mishkat 641]\n\nBased on this, Bilal's Iqamah has 9 phrases: 'Qad Qamatis-Salah' twice and the rest once. [Al-Mumti', Ibn Uthaymeen 2/59]"
            ));

            // 3. Famous Iqamah and Wording
            list.add(new StepItem(
                    "Famous Iqamah and Wording",
                    "The widespread 11-phrase Iqamah taught to Abdullah ibn Zayd... [Abu Dawud 499, Darimi, Bayhaqi]",
                    "The famous Iqamah taught to Abdullah ibn Zayd in his dream consists of Allahu Akbar (2x), Ash-hadu alla ilaha illallah (1x), Ash-hadu anna Muhammadar Rasulullah (1x), Hayya 'alas-Salah (1x), Hayya 'alal-Falah (1x), Qad Qamatis-Salah (2x), Allahu Akbar (2x), La ilaha illallah (1x). [Sunan Abi Dawud 499, Sunan ad-Darimi 1171, Sahih Ibn Khuzaymah 370, Sahih Ibn Hibban 1671, Bayhaqi 1/391]"
            ));

            // 4. Abu Mahdhurah's 17-Phrase Iqamah
            list.add(new StepItem(
                    "Abu Mahdhurah's 17-Phrase Iqamah",
                    "The 17-phrase Iqamah taught by the Prophet (ﷺ)... [Ahmad, Abu Dawud 502, Tirmidhi, Mishkat 644]",
                    "Abu Mahdhurah stated that the Messenger of Allah (ﷺ) taught him 19 phrases for Adhan and 17 phrases for Iqamah. [Musnad Ahmad, Sunan Abi Dawud, Jami at-Tirmidhi, Nasa'i, Ibn Majah, Darimi, Mishkat 644, Abu Dawud 502]"
            ));

            // 5. Fatwa of Imam Ibn Taymiyyah
            list.add(new StepItem(
                    "Fatwa of Imam Ibn Taymiyyah",
                    "Acceptability of multiple authentic sunnahs and avoiding disputes... [Majmu' al-Fatawa 22/335]",
                    "Imam Ibn Taymiyyah (RA) stated that all authentically proven variations from the Prophet (ﷺ) are valid to practice, analogous to different authentic Qira'at and forms of Tashahhud. [Majmu' al-Fatawa 22/335, 22/66]"
            ));

            // 6. Prayer Without Iqamah and Sajdah Sahw
            list.add(new StepItem(
                    "Prayer Without Iqamah and Sajdah Sahw",
                    "Validity of prayer if Iqamah was forgotten... [Tuhfat al-Ikhwan, Ibn Baz p. 78]",
                    "If one forgets to call the Iqamah and prays (individually or in congregation), the prayer remains completely valid, and Sajdah Sahw is not required as Iqamah is distinct from the prayer itself. [Tuhfat al-Ikhwan, Ibn Baz p. 78]"
            ));
        }

        return list;
    }
}
