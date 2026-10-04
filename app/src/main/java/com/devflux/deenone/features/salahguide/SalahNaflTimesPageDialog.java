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

public class SalahNaflTimesPageDialog {

    public static class NaflItem {
        public String title;
        public String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public NaflItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "নফল সালাতের সঠিক সময়" : "Proper Timings for Nafl Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick settings/options
        });

        List<NaflItem> items = getNaflItems(isBn);

        NaflAdapter adapter = new NaflAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class NaflAdapter extends RecyclerView.Adapter<NaflAdapter.ViewHolder> {
        private final Context context;
        private final List<NaflItem> items;
        private final boolean isBn;

        public NaflAdapter(Context context, List<NaflItem> items, boolean isBn) {
            this.context = context;
            this.items = items;
            this.isBn = isBn;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahBasicsCardBinding binding = ItemSalahBasicsCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(items.get(position), isBn);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemSalahBasicsCardBinding binding;

            public ViewHolder(ItemSalahBasicsCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                TouchAnimationUtil.attachTouchSpring(binding.layoutToggleExpand);

                View.OnClickListener toggleClick = v -> {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                        NaflItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(NaflItem item, boolean isBn) {
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

    public static List<NaflItem> getNaflItems(boolean isBn) {
        List<NaflItem> list = new ArrayList<>();

        if (isBn) {
            // ১. নফল নামাযের প্রকারভেদ
            list.add(new NaflItem(
                    "নফল নামাযের প্রকারভেদ",
                    "ফরয নামায ছাড়া অন্যান্য নামায দুই প্রকার। প্রথম প্রকার হল সেই নামায, যা (সময় ও রাকআত সংখ্যা দ্বারা) নির্দিষ্ট নয়। এ নামায নিষিদ্ধ সময় ছাড়া যে কোন সময়ে অনির্দিষ্ট রাকআতে পড়া যায়। আর দ্বিতীয় প্রকার হল সেই নামায, যা (সময় ও রাকআত সংখ্যা দ্বারা)...",
                    "ফরয নামায ছাড়া অন্যান্য নামায দুই প্রকার। প্রথম প্রকার হল সেই নামায, যা (সময় ও রাকআত সংখ্যা দ্বারা) নির্দিষ্ট নয়। এ নামায নিষিদ্ধ সময় ছাড়া যে কোন সময়ে অনির্দিষ্ট রাকআতে পড়া যায়। আর দ্বিতীয় প্রকার হল সেই নামায, যা (সময় ও রাকআত সংখ্যা দ্বারা) নির্দিষ্ট। যে নামাযের নির্দিষ্ট সময় ও রাকআত সংখ্যা মহানবী (ﷺ) কর্তৃক প্রমাণিত আছে। এই শ্রেণীর নামায আবার দুই প্রকার; সুন্নাতে মুআক্কাদাহ ও গায়র মুআক্কাদাহ।"
            ));

            // ২. নফল নামায এর বিবরণ
            list.add(new NaflItem(
                    "নফল নামায এর বিবরণ",
                    "যে নামায পড়া বাধ্যতামূলক নয়, যা ত্যাগ করলে গুনাহ হয় না কিন্তু পড়লে সওয়াব হয় সেই শ্রেণীর নামাযের বড় মাহাত্ম রয়েছে শরীয়তে। রসূল (ﷺ) বলেন, “কিয়ামতের দিন বান্দার নিকট থেকে তার আমলসমূহের মধ্যে যে আমলের হিসাব সর্বাগ্রে নেওয়া হবে, তা ...",
                    "যে নামায পড়া বাধ্যতামূলক নয়, যা ত্যাগ করলে গুনাহ হয় না কিন্তু পড়লে সওয়াব হয় সেই শ্রেণীর নামাযের বড় মাহাত্ম রয়েছে শরীয়তে।\n\n" +
                            "রসূল (ﷺ) বলেন, “কিয়ামতের দিন বান্দার নিকট থেকে তার আমলসমূহের মধ্যে যে আমলের হিসাব সর্বাগ্রে নেওয়া হবে, তা হল নামায। নামায ঠিক হলে সে পরিত্রাণ ও সফলতা লাভ করবে। নচেৎ (নামায ঠিক না হলে) ধ্বংস ও ক্ষতিগ্রস্ত হবে। সুতরাং (হিসাবের সময়) ফরয নামাযে কোন কমতি দেখা গেলে আল্লাহ তাবারাকা অতাআলা ফিরিশ্তাদের উদ্দেশ্যে বলবেন, ‘দেখ, আমার বান্দার কোন নফল (নামায) আছে কি না।’ অতএব তার নফল নামায দ্বারা ফরয নামাযের ঘাটতি পূরণ করা হবে। অতঃপর আরো সকল আমলের হিসাব অনুরুপ গ্রহণ করা হবে।”\n\n" +
                            "[আবূদাঊদ, সুনান ৭৭০, তিরমিযী, সুনান ৩৩৭, সইবনে মাজাহ্, সুনান ১১৭নং, সহিহ তারগিব - ১/১৮৫]\n\n\n" +
                            "মহানবী (ﷺ) বলেন, আল্লাহ বলেন, “যে ব্যক্তি আমার ওলীর বিরুদ্ধে শত্রুতা পোষণ করবে, আমি তার বিরুদ্ধে যুদ্ধ ঘোষণা করব। বান্দা যা কিছু দিয়ে আমার নৈকট্য লাভ করে থাকে তার মধ্যে আমার নিকট প্রিয়তম হল সেই ইবাদত, যা আমি তার উপর ফরয করেছি। আর সে নফল ইবাদত দ্বারা আমার নৈকট্য অর্জন করতে থাকে। পরিশেষে আমি তাকে ভালোবাসি। অতঃপর আমি তার শোনার কান হয়ে যাই, তার দেখার চোখ হয়ে যাই, তার ধরার হাত হয়ে যাই, তার চলার পা হয়ে যাই! সে আমার কাছে কিছু চাইলে আমি অবশ্যই তাকে তা দান করি। সে আমার কাছে আশ্রয় প্রার্থনা করলে আমি অবশ্যই তাকে আশ্রয় দিয়ে থাকি। আর আমি যে কাজ করি তাতে কোন দ্বিধা করি না -যতটা দ্বিধা করি এজন মুমিনের জীবন সম্পর্কে; কারণ, সে মরণকে অপছন্দ করে। আর আমি তার (বেঁচে থেকে) কষ্ট পাওয়াকে অপছন্দ করি।”\n\n" +
                            "[বুখারী ৬৫০২নং]\n\n\n" +
                            "উল্লেখ্য যে, আল্লাহ বান্দার কান, চোখ, হাত ও পা হওয়ার অর্থ হল, বান্দা আল্লাহর সন্তুষ্টি মতেই এ সবকে ব্যবহার করে। যাতে ব্যবহার করলে তিনি অসন্তুষ্ট, তাতে সে ঐ সকল অঙ্গকে ব্যবহার করে না।"
            ));

            // ৩. নফল নামায ঘরে পড়া ভাল
            list.add(new NaflItem(
                    "নফল নামায ঘরে পড়া ভাল",
                    "ফরয নামায বিধিবদ্ধ হয়েছে দ্বীনের প্রচার ও তার প্রতীকের বহিঃপ্রকাশ ঘটানোর উদ্দেশ্যে। তাই ফরয নামায প্রকাশ্যভাবে লোক মাঝে প্রতিষ্ঠা করা হয়। পক্ষান্তরে নফল নামায বিধিবদ্ধ হয়েছে নিছক মহান আল্লাহর সন্তুষ্টি কামনা করে তাঁর নৈকট্য লাভ করার লক্ষ্যে। সুত...",
                    "ফরয নামায বিধিবদ্ধ হয়েছে দ্বীনের প্রচার ও তার প্রতীকের বহিঃপ্রকাশ ঘটানোর উদ্দেশ্যে। তাই ফরয নামায প্রকাশ্যভাবে লোক মাঝে প্রতিষ্ঠা করা হয়। পক্ষান্তরে নফল নামায বিধিবদ্ধ হয়েছে নিছক মহান আল্লাহর সন্তুষ্টি কামনা করে তাঁর নৈকট্য লাভ করার লক্ষ্যে। সুতরাং নফল নামায যত গুপ্ত হবে, তত লোকচক্ষের দৃষ্টি আকর্ষণ তথা ‘রিয়া’ থেকে অধিক দূর ও পবিত্র হবে।\n\n" +
                            "[ফাইযুল ক্বাদীর ৪/২২০]\n\n\n" +
                            "আর সে জন্যই নফল নামায স্বগৃহে গোপনে পড়া উত্তম। তাছাড়া নফল নামায ঘরে পড়লে নামাযের তরীকা ও গুরুত্ব পরিবার-পরিজনের কাছে প্রকাশ পায়। আর এ জন্য হুকুম হল,\n\n\n" +
                            "তোমরা ঘরে নামায পড় এবং তা কবর বানিয়ে নিও না।\n\n" +
                            "[বুখারী ৪৩২, মুসলিম, সহীহ ৭৭৭, আবূদাঊদ, সুনান ১৪৪৮, তিরমিযী, সুনান, নাসাঈ, সুনান, জামে ৩৭৮৪নং]\n\n\n" +
                            "অর্থাৎ, কবরে বা কবরস্থানে যেমন নামায নেই বা হয় না সেইরুপ নিজের ঘরকেও নামাযহীন করে রেখো না। মহানবী (ﷺ) আরো বলেন, “তোমরা স্বগৃহে নামায পড় এবং তাতে নফল পড়তে ছেড়ো না।” [সিলসিলাহ সহীহাহ, আলবানী ১৯১০, জামে ৩৭৮৬নং]\n\n\n" +
                            "আল্লাহর রসূল (ﷺ) বলেন,\n\n" +
                            "তোমাদের কেউযখন মসজিদে (ফরয) নামায সম্পন্ন করে তখন তার উচিৎ, সে যেন তার নামাযের কিছু অংশ (সুন্নত নামায) নিজের বাড়ির জন্য রাখে। কারণ বাড়িতে পড়া ঐ কিছু নামাযের মধ্যে আল্লাহ কল্যাণ নিহিত রেখেছেন।\n\n" +
                            "[মুসলিম, সহীহ ৭৭৮নং]\n\n\n" +
                            "মহানবী (ﷺ) বলেন,\n\n" +
                            "হে মানবসকল! তোমরা স্বগৃহে নামায আদায় কর। যেহেতু ফরয নামায ছাড়া মানুষের শ্রেষ্ঠতম নামায হল তার স্বগৃহে পড়া নামায।\n\n" +
                            "[নাসাঈ, সুনান, ইবনে খুযাইমাহ্, সহীহ, সহিহ তারগিব ৪৩৭]\n\n\n" +
                            "নবী মুবাশ্শির (ﷺ) বলেন,\n\n" +
                            "যেখানে লোকে দেখতে পায় সেখানে মানুষের নফল নামায অপেক্ষা যেখানে লোকে দেখতে পায় না সেখানের নামায ২৫ টি নামাযের বরাবর।\n\n" +
                            "[আবূ য়্যা’লা, জামে ৩৮২১]\n\n\n" +
                            "আল্লাহর রসূল (ﷺ) বলেন,\n\n" +
                            "লোকচক্ষুর সম্মুখে (নফল) নামায পড়া অপেক্ষা মানুষের স্বগৃহে নামায পড়ার ফযীলত ঠিক সেইরুপ, যেরুপ নফল নামায অপেক্ষা ফরয নামাযের ফযীলত বহুগুণে অধিক।\n\n" +
                            "[বায়হাকী, সহিহ তারগিব ৪৩৮]\n\n\n" +
                            "এমন কি মদ্বীনাবাসীর জন্যও মসজিদে নববীতে নফল নামায পড়ার চাইতে নিজ নিজ ঘরে পড়া বেশী উত্তম।\n\n" +
                            "[আবূদাঊদ, সুনান, জামে ৩৮১৪]"
            ));

            // ৪. নফল নামাযে লম্বা কিয়াম করা উত্তম
            list.add(new NaflItem(
                    "নফল নামাযে লম্বা কিয়াম করা উত্তম",
                    "নফল নামায সাধারণত: একার নামায। তাই তাতে ইচ্ছামত লম্বা ক্বিরাআত করা যায়। বরং এই নামাযে কিয়াম লম্বা করা মুস্তাহাব। মহানবী (ﷺ)-কে জিজ্ঞাসা করা হল যে, সবচেয়ে উত্তম নামায কি? উত্তরে তিনি বললেন, “লম্বা কিয়াম বিশিষ্ট নামায।” [আবূদাঊদ...",
                    "নফল নামায সাধারণত: একার নামায। তাই তাতে ইচ্ছামত লম্বা ক্বিরাআত করা যায়। বরং এই নামাযে কিয়াম লম্বা করা মুস্তাহাব। মহানবী (ﷺ)-কে জিজ্ঞাসা করা হল যে, সবচেয়ে উত্তম নামায কি? উত্তরে তিনি বললেন, “লম্বা কিয়াম বিশিষ্ট নামায।”\n\n" +
                            "[আবূদাঊদ, সুনান ১৪৪৯]\n\n\n" +
                            "আর এ কথা বিদিত যে, মহানবী (ﷺ) তাহাজ্জুদের নামাযে এত লম্বা কিয়াম করতেন যে, তাতে তাঁর পা ফুলে যেত। সাহাবাগণ বলেছিলেন, আল্লাহ আপনার আগের-পরের সকল গুনাহ মাফ করে দিয়েছেন তবুও আপনি কেন অনুরুপ নামায পড়েন? উত্তরে তিনি বলেছিলেন, “আমি কি আল্লাহর কৃতজ্ঞ বান্দা হ্ব না?”\n\n" +
                            "[বুখারী, মুসলিম, আহমাদ, মুসনাদ, তিরমিযী, সুনান, নাসাঈ, সুনান ইবনে মাজাহ্, সুনান, মিশকাত ১২২০]"
            ));
        } else {
            // English Mode
            // 1. Types of Nafl Salah
            list.add(new NaflItem(
                    "Types of Nafl Prayers",
                    "Prayers other than the obligatory ones are of two kinds. The first kind consists of prayers that are unrestricted by time or number of Rak'ahs. The second kind includes prayers specified by time and Rak'ahs...",
                    "Prayers other than the obligatory (Fard) prayers fall into two categories:\n\n" +
                            "1. Unrestricted Nafl (Nafl Mutlaq):\n" +
                            "Prayers that are not restricted to specific times or number of Rak'ahs. They can be performed at any permissible time in any number of Rak'ahs.\n\n" +
                            "2. Restricted/Specified Nafl (Nafl Muqayyad):\n" +
                            "Prayers specified by fixed times and Rak'ahs, such as Sunnah Mu'akkadah, Tahajjud, Duha (Chasht), Ishraq, Tahiyyatul Wudu, and Tahiyyatul Masjid."
            ));

            // 2. Description and Virtue of Nafl Salah
            list.add(new NaflItem(
                    "Significance and Virtues of Nafl Salah",
                    "Prayers that are voluntary carry immense status in Shariah. The Messenger of Allah (ﷺ) said: The first action for which a servant will be held accountable on the Day of Resurrection is prayer...",
                    "Prayers that are voluntary, which are not obligatory and carry no sin if omitted but earn great reward when performed, hold immense significance in Shariah.\n\n" +
                            "The Messenger of Allah (ﷺ) said:\n\n" +
                            "\"The first matter that the slave will be brought to account for from his deeds on the Day of Judgment is prayer. If his prayer is sound, he will find salvation and success; if it is ruined, he will fail and be lost. If there is any deficiency in his obligatory prayers, Allah, the Blessed and Exalted, will say to the angels: 'See if My servant has any voluntary prayers.' Then the deficiency in his obligatory prayers will be completed from his voluntary prayers. Then the rest of his deeds will be reckoned in like manner.\"\n\n" +
                            "[Sunan Abi Dawud: 770, Jami` at-Tirmidhi: 337, Sunan Ibn Majah: 117, Sahih at-Targhib 1/185]\n\n\n" +
                            "The Prophet (ﷺ) said that Allah stated:\n\n" +
                            "\"Whoever shows enmity to a pious worshipper of Mine, I declare war against him. My servant grows not near to Me with anything more beloved to Me than the duties I have enjoined upon him. My servant continues to draw near to Me with voluntary devotions until I love him. When I love him, I become his hearing with which he hears, his sight with which he sees, his hand with which he strikes, and his foot with which he walks. If he asks of Me, I surely give it to him; and if he seeks My refuge, I surely protect him. And I do not hesitate about anything as much as I hesitate about taking the soul of a believer: he dislikes death, and I dislike hurting him.\"\n\n" +
                            "[Sahih al-Bukhari: 6502]\n\n\n" +
                            "Note: Allah becoming the servant's hearing, sight, hand, and foot means that the servant uses these faculties solely in accordance with Allah's pleasure and abstains from using them in what displeases Him."
            ));

            // 3. Performing Nafl Prayers at Home
            list.add(new NaflItem(
                    "Virtue of Praying Nafl at Home",
                    "Obligatory prayers are ordained to be established publicly. Conversely, voluntary prayers are legislated purely for seeking Allah's closeness. It is therefore superior to offer them at home...",
                    "Obligatory prayers are instituted for the public manifestation and proclamation of the faith in congregation. In contrast, voluntary prayers are designed for pure intimacy and devotion with Allah in private.\n\n" +
                            "[Fayd al-Qadir: 4/220]\n\n\n" +
                            "Therefore, offering voluntary prayers in one's home is superior. Furthermore, praying at home demonstrates the manner and importance of prayer to the family members. Hence, it is commanded:\n\n\n" +
                            "\"Perform prayer in your houses and do not make them like graves.\"\n\n" +
                            "[Sahih al-Bukhari: 432, Sahih Muslim: 777, Sunan Abi Dawud: 1448, Jami` at-Tirmidhi, Sunan an-Nasa'i, Sahih al-Jami`: 3784]\n\n\n" +
                            "Meaning, just as there is no prayer in graves, do not leave your homes devoid of prayer. The Prophet (ﷺ) also said: \"Pray in your homes and do not abandon voluntary prayers therein.\" [Silsilah Sahihah: 1910, Sahih al-Jami`: 3786]\n\n\n" +
                            "The Messenger of Allah (ﷺ) said:\n\n" +
                            "\"When one of you finishes his prayer in the mosque, he should reserve a portion of his prayer for his house, for Allah will place good in his home by virtue of his prayer.\"\n\n" +
                            "[Sahih Muslim: 778]\n\n\n" +
                            "The Prophet (ﷺ) said:\n\n" +
                            "\"O people, perform your prayers in your homes, for the best of all prayers of a person is in his house, except for the obligatory prayers.\"\n\n" +
                            "[Sunan an-Nasa'i, Sahih Ibn Khuzaymah, Sahih at-Targhib: 437]\n\n\n" +
                            "The Prophet (ﷺ) said:\n\n" +
                            "\"A person's voluntary prayer where people cannot see him is equivalent to twenty-five prayers where people see him.\"\n\n" +
                            "[Abu Ya'la, Sahih al-Jami`: 3821]\n\n\n" +
                            "The Messenger of Allah (ﷺ) said:\n\n" +
                            "\"The virtue of a person's voluntary prayer in his home over offering it before the eyes of people is like the superiority of obligatory prayers over voluntary prayers.\"\n\n" +
                            "[Al-Bayhaqi, Sahih at-Targhib: 438]\n\n\n" +
                            "Even for the residents of Madinah, offering voluntary prayers in their homes is superior to praying them in the Prophet's Mosque (Al-Masjid an-Nabawi).\n\n" +
                            "[Sunan Abi Dawud, Sahih al-Jami`: 3814]"
            ));

            // 4. Preferability of Long Standing (Qiyam) in Nafl
            list.add(new NaflItem(
                    "Excellence of Lengthening Qiyam in Nafl",
                    "Nafl prayer is typically performed individually, allowing freedom to lengthen the recitation. Indeed, lengthening the standing (Qiyam) is highly recommended...",
                    "Voluntary prayer is typically offered individually, providing complete freedom to prolong the Quranic recitation. Indeed, extending the duration of Qiyam is highly recommended (Mustahabb).\n\n" +
                            "The Prophet (ﷺ) was asked: \"Which prayer is best?\"\n\n" +
                            "He replied:\n\n" +
                            "\"The one with prolonged standing (Qiyam).\"\n\n" +
                            "[Sunan Abi Dawud: 1449]\n\n\n" +
                            "And it is well-known that the Prophet (ﷺ) used to lengthen his standing in Tahajjud prayer to the extent that his feet would swell. When the Companions said to him: \"Allah has forgiven your past and future sins, yet why do you pray like this?\", he replied:\n\n" +
                            "\"Should I not be a grateful slave?\"\n\n" +
                            "[Sahih al-Bukhari, Sahih Muslim, Musnad Ahmad, Jami` at-Tirmidhi, Sunan an-Nasa'i, Sunan Ibn Majah, Mishkat al-Masabih: 1220]"
            ));
        }

        return list;
    }
}
