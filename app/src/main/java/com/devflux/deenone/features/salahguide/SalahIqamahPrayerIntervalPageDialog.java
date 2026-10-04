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

public class SalahIqamahPrayerIntervalPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইকামত ও নামায শুরু করার মাঝে ব্যবধান" : "Interval Between Iqamah and Starting Prayer");

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
            // 1. ইকামত ও নামায শুরু করার মাঝে ব্যবধান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "ইকামত ও নামায শুরু করার মাঝে ব্যবধান",
                    "ইমামকে না দেখে না দাঁড়ানো, শান্তভাব বজায় রাখা, জরুরতে বিরতি ও কথাবার্তার বিধান... [বুখারী, ফাতাওয়া ইসলামিয়্যাহ্]",
                    "আল্লাহর রসূল (ﷺ) বলেন, “নামাযের ইকামত হয়ে গেলে তোমরা আমাকে না দেখা পর্যন্ত (নামাযের জন্য) দাঁড়াও না।”\n\n[বুখারী ৬৩৭নং]\n\nযেমন ইকামত হয়ে গেলে তাড়াহুড়ো করে দাঁড়ানোও উচিৎ নয়। কারণ উক্তহাদীসের এক বর্ণনায় তিনি বলেন, “তোমাদের মাঝে যেন ধীরতা ও শান্তভাব থাকে। যেহেতু রাজাধিরাজের দরবারে কোন প্রকারের হৈ-হুল্লোড় ও তাড়াহুড়ো চলে না। বলা বাহুল্য এই দরবারে থাকবে শত আদব, শত বিনয়, ধীরতা ও স্থিরতা।\n\nহুমাইদ বলেন, আমি সাবেত আল-বুনানীকে ইকামতের পর কথাবার্তা বলার বৈধতার ব্যাপারে প্রশ্ন করলে তিনি আনাস (রাঃ) কর্তৃক বর্ণিত হাদীস শুনালেন; ‘একদা নামাযের ইকামত হয়ে গেলে এক ব্যক্তি নবী (ﷺ) কে নামাযে প্রবেশ করতে আটকে রেখেছিল।\n\n[বুখারী ৬৪৩নং]\n\nমসজিদের এক প্রান্তে গোপনে কথা বলতে লাগলে উপস্থিত মুসল্লীগণ ঘুমে ঢলে পড়েছিল। একদা নামাযের ইকামত হয়ে গেলে মুসল্লীগণ কাতার সোজা করে দাঁড়িয়ে গিয়েছিল। আল্লাহর রসূল (ﷺ) হুজরা হতে বের হয়ে যখন ইমামতির জায়গায় এলেন, তখন তাঁর মনে পড়ল যে, তিনি নাপাকীর গোসল করেননি। তিনি সকলের উদ্দেশ্যে বললেন, “তোমরা স্বস্থানে দন্ডায়মান থাক।” অতঃপর তিনি হুজরায় ফিরে গিয়ে গোসল করলেন। তিনি যখন বের হয়ে এলেন, তখন তাঁর মাথা হতে পানি টপকাচ্ছিল। এরপর তিনি ইমামতি করে নামায পড়লেন।\n\nউক্ত হাদীস থেকে এ কথা বুঝা যায় যে, প্রয়োজনে ইকামত ও নামাযের মাঝে বেশ কিছু সময় বিরতি হলে কোন ক্ষতি হয় না। পরন্তু ইকামত ফিরিয়ে বলতে হয় না।\n\nইকামত হওয়ার পর কোন জরুরী কথা, নামায ও কাতার বিষয়ক কথা বলা বৈধ। তবে নামাযের প্রস্তুতি নেওয়ার পর কোন পার্থিব কথা বলা উচিৎ নয়।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫১]\n\nইকামত শুরু হলে এবং ইমাম উপস্থিত থাকলে প্রত্যেকে নিজের সুবিধামত উঠে নামাযের জন্য দন্ডায়মান হবে। ইকামতের শুরুতে, মাঝে বা শেষে, যে কোন সময়ে দাঁড়ালেই চলবে। তবে এ কথার খেয়াল অবশ্যই রাখা উচিৎ, যাতে ইমামের সাথে তকবীরে তাহ্রীমা ছুটে না যায়।\n\n[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৩/১০]"
            ));

            // 2. ইমামকে না দেখে না দাঁড়ানো ও ধীরস্থিরতা বজায় রাখা
            list.add(new StepItem(
                    "ইমামকে না দেখে না দাঁড়ানো ও ধীরস্থিরতা বজায় রাখা",
                    "ইমাম না আসা পর্যন্ত না দাঁড়ানো ও শান্তভাব বজায় রাখার নির্দেশ... [বুখারী ৬৩৭নং]",
                    "আল্লাহর রসূল (ﷺ) বলেন, “নামাযের ইকামত হয়ে গেলে তোমরা আমাকে না দেখা পর্যন্ত (নামাযের জন্য) দাঁড়াও না।”\n\n[বুখারী ৬৩৭নং]\n\nযেমন ইকামত হয়ে গেলে তাড়াহুড়ো করে দাঁড়ানোও উচিৎ নয়। কারণ উক্তহাদীসের এক বর্ণনায় তিনি বলেন, “তোমাদের মাঝে যেন ধীরতা ও শান্তভাব থাকে। যেহেতু রাজাধিরাজের দরবারে কোন প্রকারের হৈ-হুল্লোড় ও তাড়াহুড়ো চলে না। বলা বাহুল্য এই দরবারে থাকবে শত আদব, শত বিনয়, ধীরতা ও স্থিরতা।"
            ));

            // 3. ইকামতের পর কথাবার্তা ও রাসুলুল্লাহর (ﷺ) গোসলের ঘটনা
            list.add(new StepItem(
                    "ইকামতের পর কথাবার্তা ও রাসুলুল্লাহর (ﷺ) গোসলের ঘটনা",
                    "হুমাইদ ও আনাস (রাঃ)-এর হাদীস এবং স্বস্থানে দাঁড়িয়ে থাকার নির্দেশ... [বুখারী ৬৪৩নং]",
                    "হুমাইদ বলেন, আমি সাবেত আল-বুনানীকে ইকামতের পর কথাবার্তা বলার বৈধতার ব্যাপারে প্রশ্ন করলে তিনি আনাস (রাঃ) কর্তৃক বর্ণিত হাদীস শুনালেন; ‘একদা নামাযের ইকামত হয়ে গেলে এক ব্যক্তি নবী (ﷺ) কে নামাযে প্রবেশ করতে আটকে রেখেছিল।\n\n[বুখারী ৬৪৩নং]\n\nমসজিদের এক প্রান্তে গোপনে কথা বলতে লাগলে উপস্থিত মুসল্লীগণ ঘুমে ঢলে পড়েছিল। একদা নামাযের ইকামত হয়ে গেলে মুসল্লীগণ কাতার সোজা করে দাঁড়িয়ে গিয়েছিল। আল্লাহর রসূল (ﷺ) হুজরা হতে বের হয়ে যখন ইমামতির জায়গায় এলেন, তখন তাঁর মনে পড়ল যে, তিনি নাপাকীর গোসল করেননি। তিনি সকলের উদ্দেশ্যে বললেন, “তোমরা স্বস্থানে দন্ডায়মান থাক।” অতঃপর তিনি হুজরায় ফিরে গিয়ে গোসল করলেন। তিনি যখন বের হয়ে এলেন, তখন তাঁর মাথা হতে পানি টপকাচ্ছিল। এরপর তিনি ইমামতি করে নামায পড়লেন।"
            ));

            // 4. ইকামতের পর বিরতি ও জরুরী কথা বলার বিধান
            list.add(new StepItem(
                    "ইকামতের পর বিরতি ও জরুরী কথা বলার বিধান",
                    "প্রয়োজনে বিরতি হওয়া, ইকামত পুনরায় না দেওয়া ও পার্থিব কথা বর্জন... [ফাতাওয়া ইসলামিয়্যাহ্ ১/২৫১]",
                    "উক্ত হাদীস থেকে এ কথা বুঝা যায় যে, প্রয়োজনে ইকামত ও নামাযের মাঝে বেশ কিছু সময় বিরতি হলে কোন ক্ষতি হয় না। পরন্তু ইকামত ফিরিয়ে বলতে হয় না।\n\nইকামত হওয়ার পর কোন জরুরী কথা, নামায ও কাতার বিষয়ক কথা বলা বৈধ। তবে নামাযের প্রস্তুতি নেওয়ার পর কোন পার্থিব কথা বলা উচিৎ নয়।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫১]"
            ));

            // 5. ইকামতের সময় নামাযের জন্য দাঁড়ানোর সঠিক মুহূর্ত
            list.add(new StepItem(
                    "ইকামতের সময় নামাযের জন্য দাঁড়ানোর সঠিক মুহূর্ত",
                    "শুরুতে, মাঝে বা শেষে সুবিধামতো দাঁড়ানো ও তাকবীরে তাহরীমা রক্ষা... [আলমুমতে শারহে ফিক্হ ৩/১০]",
                    "ইকামত শুরু হলে এবং ইমাম উপস্থিত থাকলে প্রত্যেকে নিজের সুবিধামত উঠে নামাযের জন্য দন্ডায়মান হবে। ইকামতের শুরুতে, মাঝে বা শেষে, যে কোন সময়ে দাঁড়ালেই চলবে। তবে এ কথার খেয়াল অবশ্যই রাখা উচিৎ, যাতে ইমামের সাথে তকবীরে তাহ্রীমা ছুটে না যায়।\n\n[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৩/১০]"
            ));

        } else {
            // English Mode
            // 1. Interval Between Iqamah & Prayer (Full Narrative)
            list.add(new StepItem(
                    "Interval Between Iqamah and Starting Prayer",
                    "Not standing until seeing the Imam, maintaining tranquility, necessary delay and speaking... [Bukhari, Fatawa Islamiyyah]",
                    "The Messenger of Allah (ﷺ) said: 'When the Iqamah for prayer is called, do not stand (for prayer) until you see me.'\n\n[Sahih Bukhari 637]\n\nLikewise, one should not rush hurriedly upon hearing the Iqamah, for in a narration of this hadith he said: 'You must maintain tranquility and composure.' For in the court of the King of Kings, no noise, commotion, or hasty rushing is appropriate. It is needless to say that in this court there should be utter decorum, deep humility, serenity, and calmness.\n\nHumaid reported: I asked Thabit al-Bunani regarding the permissibility of speaking after the Iqamah, and he narrated the hadith of Anas (RA): 'Once the Iqamah for prayer had been called, a man detained the Prophet (ﷺ) before he entered into prayer.'\n\n[Sahih Bukhari 643]\n\nHe spoke to him privately at one corner of the mosque until the worshippers began dozing off due to sleep. Once, after the Iqamah had been called and the worshippers had straightened their rows, the Messenger of Allah (ﷺ) came out from his chamber to lead the prayer. When he reached the place of leading prayer, he remembered that he had not performed the obligatory bath (Ghusl for purification). He said to everyone: 'Remain standing in your places.' He then returned to his chamber, took a bath, and emerged with water droplets falling from his head. He then led the congregation in prayer.\n\nFrom this hadith it is understood that if there is a necessary interval of time between Iqamah and prayer, it does not harm the validity of prayer, nor does the Iqamah need to be repeated.\n\nAfter the Iqamah, it is permissible to speak regarding necessary matters, or matters related to prayer and rows. However, once preparation for prayer is underway, one should not engage in worldly conversation.\n\n[Fatawa Islamiyyah, Saudi Scholars Committee 1/251]\n\nWhen the Iqamah starts and the Imam is present, everyone may stand up for prayer according to their convenience. Standing at the beginning, middle, or end of the Iqamah is completely permissible. However, one should ensure not to miss the opening Takbir (Takbir at-Tahrimah) with the Imam.\n\n[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 3/10]"
            ));

            // 2. Not Standing Until Seeing the Imam & Maintaining Tranquility
            list.add(new StepItem(
                    "Not Standing Until Seeing the Imam & Maintaining Tranquility",
                    "Prophetic instruction to wait until seeing the Imam and maintain serenity... [Bukhari 637]",
                    "The Messenger of Allah (ﷺ) said: 'When the Iqamah for prayer is called, do not stand until you see me.' [Sahih Bukhari 637]\n\nOne should not rush frantically; as the Prophet (ﷺ) instructed to maintain peace, tranquility, and respect in the Divine presence."
            ));

            // 3. Speaking After Iqamah & The Prophet's Ghusl Incident
            list.add(new StepItem(
                    "Speaking After Iqamah & The Prophet's Ghusl Incident",
                    "Hadith of Anas (RA) and the Prophet (ﷺ) taking Ghusl while congregation waited... [Bukhari 643]",
                    "Anas (RA) reported that once after the Iqamah was proclaimed, a man spoke privately with the Prophet (ﷺ) until worshippers dozed off. In another incident, the Prophet (ﷺ) told the worshippers to remain in their rows while he took a bath of purification, then returned and led the prayer. [Sahih Bukhari 643]"
            ));

            // 4. Validity of Gap & Permissibility of Necessary Speech
            list.add(new StepItem(
                    "Validity of Gap & Permissibility of Necessary Speech",
                    "Gaps due to necessity do not invalidate Iqamah, avoiding worldly talk... [Fatawa Islamiyyah 1/251]",
                    "A necessary delay between Iqamah and prayer does not necessitate repeating the Iqamah. Speaking about necessary matters or row straightening is valid, while worldly talk should be avoided. [Fatawa Islamiyyah, Saudi Scholars Committee 1/251]"
            ));

            // 5. Moment of Standing Up During Iqamah
            list.add(new StepItem(
                    "Moment of Standing Up During Iqamah",
                    "Standing at the start, middle, or end while catching Takbir Tahrimah... [Al-Mumti' 3/10]",
                    "Worshippers may stand at their convenience at the start, middle, or end of Iqamah, ensuring they catch Takbir Tahrimah with the Imam. [Al-Mumti', Ibn Uthaymeen 3/10]"
            ));
        }

        return list;
    }
}
