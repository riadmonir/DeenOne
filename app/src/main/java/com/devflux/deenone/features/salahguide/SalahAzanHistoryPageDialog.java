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

public class SalahAzanHistoryPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "আজানের প্রারম্ভিক ইতিহাস" : "Initial History of Adhan");

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
            // 1. আজানের প্রারম্ভিক ইতিহাস (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "আজানের প্রারম্ভিক ইতিহাস",
                    "মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন... [ফাতহুল বারী, বুখারী, আবুদাঊদ]",
                    "মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন। অতঃপর মদ্বীনায় হিজরত করলে হিজরী ১ম (মতান্তরে ২য়) সনে আযান ফরয হয়।\n\n[ফাতহুল বারী, ইবনে হাজার ২/৭৮]\n\nসকল মুসলমানকে একত্রে সমবেত করে জামাআতবদ্ধভাবে নামায পড়ার জন্য এমন এক জিনিসের প্রয়োজন ছিল, যা শুনে বা দেখে তাঁরা জমা হতে পারতেন। এ জন্যে তাঁরা পূর্ব থেকেই মসজিদে উপস্থিত হয়ে নামাযের অপেক্ষা করতেন। এ মর্মে তাঁরা একদিন পরামর্শ করলেন, কেউ বললেন নাসারাদের ঘন্টার মত আমরাও ঘন্টা ব্যবহার করব। কেউ কেউ বললেন, বরং ইয়াহুদীদের শৃঙ্গের মত শৃঙ্গ ব্যবহার করব। হযরত উমার (রাঃ) বললেন, ‘বরং নামাযের প্রতি আহ্বান করার জন্য একটি লোককে (গলি-গলি) পাঠিয়ে দিলে কেমন হয়?’ কিন্তু মহানবী (ﷺ) বললেন, “হে বিলাল! ওঠ, নামাযের জন্য আহ্বান কর।\n\n[বুখারী ৬০৪, মুসলিম, সহীহ]\n\nকেউ বললেন, ‘নামাযের সময় মসজিদে একটি পতাকা উত্তোলন করা হোক। লোকেরা তা দেখে একে অপরকে নামাযের সময় জানিয়ে দেবে। কিন্তু মহানবী (ﷺ) এ সব পছন্দ করলেন না।\n\n[আবূদাঊদ, সুনান ৪৯৮নং]\n\nপরিশেষে তিনি একটি ঘন্টা নির্মাণের আদেশ দিলেন। এই অবসরে আব্দুল্লাহ বিন যায়দ (রাঃ) স্বপ্নে দেখলেন, এক ব্যক্তি ঘন্টা হাতে যাচ্ছে। আব্দুল্লাহ বলেন, আমি তাকে বললাম, হে আল্লাহর বান্দা! ঘন্টাটি বিক্রয় করবে?’ লোকটি বলল, ‘এটা নিয়ে কি করবে?’ আমি বললাম, ‘ওটা দিয়ে লোকেদেরকে নামাযের জন্য আহ্বান করব। লোকটি বলল, আমি তোমাকে এর চাইতে উত্তম জিনিসের কথা বলে দেব না কি? আমি বললাম অবশ্যই।\n\nতখন ঐ ব্যক্তি আব্দুল্লাহকে আযান ও ইকামত শিখিয়ে দিল। অতঃপর সকাল হলে তিনি রসূল (ﷺ) এর নিকট উপস্থিত হয়ে স্বপ্নের কথা খুলে বললেন। সব কিছু শুনে মহানবী (ﷺ) বললেন, “ইনশাআল্লাহ! এটি সত্য স্বপ্ন। অতএব তুমি বিলালের সাথে দাঁড়াও এবং স্বপ্নে যেমন (আযান) শুনেছ ঠিক তেমনি বিলালকে শুনাও; সে ঐ সব বলে আযান দিক। কারণ, বিলালের আওয়াজ তোমার চেয়ে উচ্চ।”\n\nসাহাবী আব্দুল্লাহ বিন যায়েদ (রাঃ) সর্বপ্রথম পূর্বরাতে স্বপ্নে দেখা আযানের কালেমা সমূহ সকালে এসে রাসূলুল্লাহ (সাঃ)-এর নিকটে বর্ণনা করেন। পরে বেলালের কণ্ঠে একই আযান ধ্বনি শুনে হযরত ওমর (রাঃ) বাড়ী থেকে বেরিয়ে চাদর ঘেঁষতে ঘেঁষতে ছুটে এসে রাসূলুল্লাহ (সাঃ)-কে বলেন –\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\nঅর্থ:\nযিনি আপনাকে ‘সত্য’ সহকারে প্রেরণ করেছেন, তাঁর কসম করে বলছি আমিও অনুরূপ স্বপ্ন দেখেছি’। একথা শুনে রাসূলুল্লাহ (সাঃ) ‘ফালিল−ল্লা-হিল হাম্দ’বলে আল্লাহর প্রশংসা করেন।\n\n[আবুদাঊদ হা/৪৯৫; মিশকাত হা/৬৫০]\n\nএকটি বর্ণনা মতে ঐ রাতে ১১ জন সাহাবী একই আযানের স্বপ্ন দেখেন’।\n\n[মিরক্বাত শরহ মিশকাত ‘আযান’ অনুচ্ছেদ ২/১৪৯ পৃঃ]\n\nউল্লেখ্য যে, ওমর ফারূক (রাঃ) ২০ দিন পূর্বে উক্ত স্বপ্ন দেখেছিলেন। কিন্তু আব্দুল্লাহ বিন যায়েদ আগেই বলেছে দেখে লজ্জায় তিনি নিজের কথা প্রকাশ করেননি।\n\n[আবুদাঊদ (আওনুল মা‘বূদ সহ) হা/৪৯৪ ‘আযানের সূচনা’ অনুচ্ছেদ]"
            ));

            // 2. আযান ফরজ হওয়ার প্রেক্ষাপট ও সাহাবীগণের পরামর্শ
            list.add(new StepItem(
                    "আযান ফরজ হওয়ার প্রেক্ষাপট ও সাহাবীগণের পরামর্শ",
                    "মক্কা থেকে মদিনায় হিজরতের পর জামাতে সমবেত হওয়ার বিষয়ে সাহাবীগণের পরামর্শ ও ঘন্টার প্রস্তাব...",
                    "মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন। অতঃপর মদ্বীনায় হিজরত করলে হিজরী ১ম (মতান্তরে ২য়) সনে আযান ফরয হয়।\n\n[ফাতহুল বারী, ইবনে হাজার ২/৭৮]\n\nসকল মুসলমানকে একত্রে সমবেত করে জামাআতবদ্ধভাবে নামায পড়ার জন্য এমন এক জিনিসের প্রয়োজন ছিল, যা শুনে বা দেখে তাঁরা জমা হতে পারতেন। এ জন্যে তাঁরা পূর্ব থেকেই মসজিদে উপস্থিত হয়ে নামাযের অপেক্ষা করতেন। এ মর্মে তাঁরা একদিন পরামর্শ করলেন, কেউ বললেন নাসারাদের ঘন্টার মত আমরাও ঘন্টা ব্যবহার করব। কেউ কেউ বললেন, বরং ইয়াহুদীদের শৃঙ্গের মত শৃঙ্গ ব্যবহার করব। হযরত উমার (রাঃ) বললেন, ‘বরং নামাযের প্রতি আহ্বান করার জন্য একটি লোককে (গলি-গলি) পাঠিয়ে দিলে কেমন হয়?’ কিন্তু মহানবী (ﷺ) বললেন, “হে বিলাল! ওঠ, নামাযের জন্য আহ্বান কর।\n\n[বুখারী ৬০৪, মুসলিম, সহীহ]\n\nকেউ বললেন, ‘নামাযের সময় মসজিদে একটি পতাকা উত্তোলন করা হোক। লোকেরা তা দেখে একে অপরকে নামাযের সময় জানিয়ে দেবে। কিন্তু মহানবী (ﷺ) এ সব পছন্দ করলেন না।\n\n[আবূদাঊদ, সুনান ৪৯৮নং]"
            ));

            // 3. আব্দুল্লাহ বিন যায়দ (রাঃ)-এর স্বপ্ন ও রাসুলুল্লাহ (ﷺ)-এর স্বীকৃতি
            list.add(new StepItem(
                    "আব্দুল্লাহ বিন যায়দ (রাঃ)-এর স্বপ্ন ও রাসুলুল্লাহ (ﷺ)-এর স্বীকৃতি",
                    "ঘন্টা হাতে ব্যক্তির কাছ থেকে আযান-ইকামত শিক্ষা ও বিলাল (রাঃ)-কে দিয়ে আযান দেওয়ানোর নির্দেশ...",
                    "পরিশেষে তিনি একটি ঘন্টা নির্মাণের আদেশ দিলেন। এই অবসরে আব্দুল্লাহ বিন যায়দ (রাঃ) স্বপ্নে দেখলেন, এক ব্যক্তি ঘন্টা হাতে যাচ্ছে। আব্দুল্লাহ বলেন, আমি তাকে বললাম, হে আল্লাহর বান্দা! ঘন্টাটি বিক্রয় করবে?’ লোকটি বলল, ‘এটা নিয়ে কি করবে?’ আমি বললাম, ‘ওটা দিয়ে লোকেদেরকে নামাযের জন্য আহ্বান করব। লোকটি বলল, আমি তোমাকে এর চাইতে উত্তম জিনিসের কথা বলে দেব না কি? আমি বললাম অবশ্যই।\n\nতখন ঐ ব্যক্তি আব্দুল্লাহকে আযান ও ইকামত শিখিয়ে দিল। অতঃপর সকাল হলে তিনি রসূল (ﷺ) এর নিকট উপস্থিত হয়ে স্বপ্নের কথা খুলে বললেন। সব কিছু শুনে মহানবী (ﷺ) বললেন, “ইনশাআল্লাহ! এটি সত্য স্বপ্ন। অতএব তুমি বিলালের সাথে দাঁড়াও এবং স্বপ্নে যেমন (আযান) শুনেছ ঠিক তেমনি বিলালকে শুনাও; সে ঐ সব বলে আযান দিক। কারণ, বিলালের আওয়াজ তোমার চেয়ে উচ্চ।”"
            ));

            // 4. হযরত ওমর (রাঃ) ও অন্যান্য সাহাবীগণের স্বপ্ন
            list.add(new StepItem(
                    "হযরত ওমর (রাঃ) ও অন্যান্য সাহাবীগণের স্বপ্ন",
                    "ওমর (রাঃ)-এর ছুটে এসে স্বপ্নের সাক্ষ্য দান, ১১ জন সাহাবীর স্বপ্ন ও আল্লাহর প্রশংসা...",
                    "সাহাবী আব্দুল্লাহ বিন যায়েদ (রাঃ) সর্বপ্রথম পূর্বরাতে স্বপ্নে দেখা আযানের কালেমা সমূহ সকালে এসে রাসূলুল্লাহ (সাঃ)-এর নিকটে বর্ণনা করেন। পরে বেলালের কণ্ঠে একই আযান ধ্বনি শুনে হযরত ওমর (রাঃ) বাড়ী থেকে বেরিয়ে চাদর ঘেঁষতে ঘেঁষতে ছুটে এসে রাসূলুল্লাহ (সাঃ)-কে বলেন –\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\nঅর্থ:\nযিনি আপনাকে ‘সত্য’ সহকারে প্রেরণ করেছেন, তাঁর কসম করে বলছি আমিও অনুরূপ স্বপ্ন দেখেছি’। একথা শুনে রাসূলুল্লাহ (সাঃ) ‘ফালিল−ল্লা-হিল হাম্দ’বলে আল্লাহর প্রশংসা করেন।\n\n[আবুদাঊদ হা/৪৯৫; মিশকাত হা/৬৫০]\n\nএকটি বর্ণনা মতে ঐ রাতে ১১ জন সাহাবী একই আযানের স্বপ্ন দেখেন’।\n\n[মিরক্বাত শরহ মিশকাত ‘আযান’ অনুচ্ছেদ ২/১৪৯ পৃঃ]\n\nউল্লেখ্য যে, ওমর ফারূক (রাঃ) ২০ দিন পূর্বে উক্ত স্বপ্ন দেখেছিলেন। কিন্তু আব্দুল্লাহ বিন যায়েদ আগেই বলেছে দেখে লজ্জায় তিনি নিজের কথা প্রকাশ করেননি।\n\n[আবুদাঊদ (আওনুল মা‘বূদ সহ) হা/৪৯৪ ‘আযানের সূচনা’ অনুচ্ছেদ]"
            ));

        } else {
            // English Mode
            // 1. Initial History of Adhan (Full Chronicle)
            list.add(new StepItem(
                    "Initial History of Adhan",
                    "During the stay in Makkah, Muslims prayed without Adhan. After migration to Madinah, Adhan was ordained in 1st/2nd AH...",
                    "During their time in Makkah, the Prophet (ﷺ) and Muslims offered prayers without Adhan. After migrating to Madinah, Adhan was ordained in the 1st (or 2nd) year of Hijrah.\n\n[Fath al-Bari, Ibn Hajar 2/78]\n\nA unified method was needed to call Muslims to congregate for prayer. Companions discussed using a bell like Christians or a horn like Jews. Umar (RA) suggested sending a person through streets to call, but the Prophet (ﷺ) said: 'O Bilal! Stand up and call for prayer.'\n\n[Bukhari 604, Sahih Muslim]\n\nSome suggested raising a flag over the mosque when prayer time entered, but the Prophet (ﷺ) disliked these options.\n\n[Sunan Abi Dawud 498]\n\nEventually, when a bell was ordered, companion Abdullah ibn Zayd (RA) had a true dream where a man taught him the words of Adhan and Iqamah. The Prophet (ﷺ) approved it, saying: 'In sha Allah, it is a true dream. Stand with Bilal and teach him, for his voice is louder than yours.'\n\nUpon hearing Bilal proclaim it, Umar (RA) rushed to the Prophet (ﷺ) testifying:\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\n'By Him Who sent you with the truth, I have seen the exact same dream!' The Prophet (ﷺ) praised Allah saying 'Alhamdulillah.'\n\n[Sunan Abi Dawud 495; Mishkat 650]\n\nAccording to reports, 11 companions saw the same dream that night [Mirqat Sharh Mishkat 2/149]. Umar (RA) had seen it 20 days prior but had felt shy to mention it before Abdullah spoke [Sunan Abi Dawud 494]."
            ));

            // 2. Context & Consultation of the Companions
            list.add(new StepItem(
                    "Context & Consultation of the Companions",
                    "Discussions on bells, horns, and flags before the divine institution of Adhan...",
                    "During their time in Makkah, the Prophet (ﷺ) and Muslims offered prayers without Adhan. After migrating to Madinah, Adhan was ordained in the 1st (or 2nd) year of Hijrah.\n\n[Fath al-Bari, Ibn Hajar 2/78]\n\nA unified method was needed to call Muslims to congregate for prayer. Companions discussed using a bell like Christians or a horn like Jews. Umar (RA) suggested sending a person through streets to call, but the Prophet (ﷺ) said: 'O Bilal! Stand up and call for prayer.'\n\n[Bukhari 604, Sahih Muslim]\n\nSome suggested raising a flag over the mosque, but the Prophet (ﷺ) disliked these options.\n\n[Sunan Abi Dawud 498]"
            ));

            // 3. The Dream of Abdullah ibn Zayd (RA) & Bilal (RA)
            list.add(new StepItem(
                    "The Dream of Abdullah ibn Zayd (RA) & Bilal (RA)",
                    "Learning the sentences of Adhan & Iqamah and instructing Bilal (RA) to call it...",
                    "Companion Abdullah ibn Zayd (RA) had a true dream where a man taught him the words of Adhan and Iqamah. In the morning, he informed the Messenger of Allah (ﷺ), who said: 'In sha Allah, it is a true dream. Stand with Bilal and teach him, for his voice is louder and higher than yours.' Bilal (RA) then called out the Adhan for the first time in Islamic history."
            ));

            // 4. Confirmation by Umar (RA) & Other Companions
            list.add(new StepItem(
                    "Confirmation by Umar (RA) & Other Companions",
                    "Umar (RA) testifying to the same dream and 11 companions witnessing it...",
                    "Upon hearing Bilal proclaim the Adhan, Umar (RA) rushed out and said:\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\n'By the One Who sent you with the truth, I saw the exact same dream!' The Prophet (ﷺ) said 'Alhamdulillah.'\n\n[Sunan Abi Dawud 495; Mishkat 650]\n\nReports state that 11 companions saw the same dream that night [Mirqat 2/149], and Umar (RA) had seen it 20 days prior [Abi Dawud 494]."
            ));
        }

        return list;
    }
}
