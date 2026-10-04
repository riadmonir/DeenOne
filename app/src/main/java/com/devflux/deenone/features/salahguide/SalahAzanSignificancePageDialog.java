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

public class SalahAzanSignificancePageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "আযান ও তার মাহাত্ম" : "Significance & Virtues of Adhan");

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
            // 1. আযান ও তার মাহাত্ম (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "আযান ও তার মাহাত্ম",
                    "আযান ফরয এবং তা দেওয়া হল ফরযে কিফায়াহ্... [বুখারী, মুসলিম, কুরআন ৪১/৩৩]",
                    "আযান ফরয এবং তা দেওয়া হল ফ র্যে কিফায়াহ্। আল্লাহর রসূল (ﷺ) বলেন, নামাযের সময় উপস্থিত হলে তোমাদের একজন আযান দেবে এবং তোমাদের মধ্যে যে বড় সে ইমামতি করবে।\n\n[বুখারী ৬২৮নং, মুসলিম, নাসাঈ, সুনান, দারেমী, সুনান]\n\nআযান ইসলামের অন্যতম নিদর্শন ও প্রতীক। কোন গ্রাম বা শহরবাসী তা ত্যাগ করলে ইমাম (রাষ্ট্রপ্রধান) তাদের বিরুদ্ধে জিহাদ করবেন। যেমন মহানবী (ﷺ) অভিযানে গেলে কোন জনপদ থেকে আযানের ধ্বনি শুনলে তাদের উপর আক্রমণ করতেন না।\n\n[বুখারী ৬১০ নং, মুসলিম, সহীহ]\n\nসফরে একা থাকলে অথবা মসজিদ খুবই দূর হলে এবং আযান শুনতে না পাওয়া গেলে একাই আযান ও ইকামত দিয়ে নামায পড়া সুন্নত।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫৫]\n\nআযান দেওয়ায় (মুআযযেনের জন্য) রয়েছে বড় সওয়াব ও ফযীলত। মহান আল্লাহ বলেন, “সে ব্যক্তি অপেক্ষা আর কার কথা উৎকৃষ্ট, যে ব্যক্তি আল্লাহর দিকে মানুষকে আহ্বান করে, সৎকাজ করে এবং বলে আমি একজন ‘মুসলিম’ (আত্মসমর্পণকারী)?\n\n[কুরআন মাজীদ ৪১/৩৩]\n\nপ্রিয় নবী (ﷺ) বলেন, “লোকে যদি আযান ও প্রথম কাতারের মাহাত্ম জানত, অতঃপর তা লাভের জন্য লটারি করা ছাড়া আর অন্য কোন উপায় না পেত, তাহলে তারা লটারিই করত।”\n\n[বুখারী ৬১৫, মুসলিম, সহীহ ৪৩৭নং]\n\nআল্লাহ প্রথম কাতারের উপর রহ্মত বর্ষণ করেন এবং ফিরিশ্তাগণ তাদের জন্য ক্ষমা প্রার্থনা করে থাকেন। মুআযযিনকে তার আযানের আওয়াযের উচ্চতা অনুযায়ী ক্ষমা করা হয়। তার আযান শ্রবণকারী প্রত্যেক সরস বা নীরস বস্তু তার কথার সত্যায়ন করে থাকে। তার সাথে যারা নামায পড়ে তাদের সকলের নেকীর সমপরিমাণ তার নেকী লাভ হয়।\n\n[আহ্মদ, নাসাঈ, সহীহ তারগীব ২২৮নং]\n\nকিয়ামতের দিন মুআযযিনগণের গর্দান অন্যান্য লোকেদের চেয়ে লম্বা হবে।”\n\n[মুসলিম, সহীহ৩৮৭নং]\n\n“যে ব্যক্তি বারো বৎসর আযান দেবে তার জন্য জান্নাত ওয়াজেব হয়ে যাবে। আর প্রত্যেক দিন আযানের দরুন তার আমল নামায় ষাটটি নেকী লিপিবদ্ধ করা হবে এবং তার ইকামতের দরুন লিপিবদ্ধ হবে ত্রিশটি নেকী।\n\n[ইবনে মাজাহ্, দারাকুত্বনী,হাকেম, সহীহ তারগীব ২৪০নং]\n\nযে কোন মানুষ, জ্বিন বা অন্য কিছু মুআযযিনের আযানের শব্দ শুনতে পাবে, সেই মুআযযিনের জন্য কিয়ামতের দিন সাক্ষ্য প্রদান করবে।\n\n[বুখারী ৬০৯ নং]"
            ));

            // 2. আযানের বিধান ও ইসলামের নিদর্শন
            list.add(new StepItem(
                    "আযানের বিধান ও ইসলামের নিদর্শন",
                    "আযান ফরযে কিফায়াহ ও ইসলামের প্রতীক, সফরে একাকী হলেও আযান-ইকামত সুন্নত...",
                    "আযান ফরয এবং তা দেওয়া হল ফ র্যে কিফায়াহ্। আল্লাহর রসূল (ﷺ) বলেন, নামাযের সময় উপস্থিত হলে তোমাদের একজন আযান দেবে এবং তোমাদের মধ্যে যে বড় সে ইমামতি করবে।\n\n[বুখারী ৬২৮নং, মুসলিম, নাসাঈ, সুনান, দারেমী, সুনান]\n\nআযান ইসলামের অন্যতম নিদর্শন ও প্রতীক। কোন গ্রাম বা শহরবাসী তা ত্যাগ করলে ইমাম (রাষ্ট্রপ্রধান) তাদের বিরুদ্ধে জিহাদ করবেন। যেমন মহানবী (ﷺ) অভিযানে গেলে কোন জনপদ থেকে আযানের ধ্বনি শুনলে তাদের উপর আক্রমণ করতেন না।\n\n[বুখারী ৬১০ নং, মুসলিম, সহীহ]\n\nসফরে একা থাকলে অথবা মসজিদ খুবই দূর হলে এবং আযান শুনতে না পাওয়া গেলে একাই আযান ও ইকামত দিয়ে নামায পড়া সুন্নত।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫৫]"
            ));

            // 3. মুয়াযযিন ও আযান দেওয়ার মহা ফজিলত
            list.add(new StepItem(
                    "মুয়াযযিন ও আযান দেওয়ার মহা ফজিলত",
                    "আল্লাহর পথে আহ্বানকারীর মর্যাদা, লটারি করার হাদিস ও ক্ষমালাভের সুসংবাদ...",
                    "আযান দেওয়ায় (মুআযযেনের জন্য) রয়েছে বড় সওয়াব ও ফযীলত। মহান আল্লাহ বলেন, “সে ব্যক্তি অপেক্ষা আর কার কথা উৎকৃষ্ট, যে ব্যক্তি আল্লাহর দিকে মানুষকে আহ্বান করে, সৎকাজ করে এবং বলে আমি একজন ‘মুসলিম’ (আত্মসমর্পণকারী)?\n\n[কুরআন মাজীদ ৪১/৩৩]\n\nপ্রিয় নবী (ﷺ) বলেন, “লোকে যদি আযান ও প্রথম কাতারের মাহাত্ম জানত, অতঃপর তা লাভের জন্য লটারি করা ছাড়া আর অন্য কোন উপায় না পেত, তাহলে তারা লটারিই করত।”\n\n[বুখারী ৬১৫, মুসলিম, সহীহ ৪৩৭নং]\n\nআল্লাহ প্রথম কাতারের উপর রহ্মত বর্ষণ করেন এবং ফিরিশ্তাগণ তাদের জন্য ক্ষমা প্রার্থনা করে থাকেন। মুআযযিনকে তার আযানের আওয়াযের উচ্চতা অনুযায়ী ক্ষমা করা হয়। তার আযান শ্রবণকারী প্রত্যেক সরস বা নীরস বস্তু তার কথার সত্যায়ন করে থাকে। তার সাথে যারা নামায পড়ে তাদের সকলের নেকীর সমপরিমাণ তার নেকী লাভ হয়।\n\n[আহ্মদ, নাসাঈ, সহীহ তারগীব ২২৮নং]"
            ));

            // 4. কিয়ামতের ময়দানে মুয়াযযিনের মর্যাদা ও জান্নাতের সুসংবাদ
            list.add(new StepItem(
                    "কিয়ামতের ময়দানে মুয়াযযিনের মর্যাদা ও জান্নাতের সুসংবাদ",
                    "কিয়ামতের দিন লম্বা গর্দান, ১২ বছর আযান দিলে জান্নাত ওয়াজিব ও সর্ব সৃষ্টির সাক্ষ্যদান...",
                    "কিয়ামতের দিন মুআযযিনগণের গর্দান অন্যান্য লোকেদের চেয়ে লম্বা হবে।”\n\n[মুসলিম, সহীহ৩৮৭নং]\n\n“যে ব্যক্তি বারো বৎসর আযান দেবে তার জন্য জান্নাত ওয়াজেব হয়ে যাবে। আর প্রত্যেক দিন আযানের দরুন তার আমল নামায় ষাটটি নেকী লিপিবদ্ধ করা হবে এবং তার ইকামতের দরুন লিপিবদ্ধ হবে ত্রিশটি নেকী।\n\n[ইবনে মাজাহ্, দারাকুত্বনী,হাকেম, সহীহ তারগীব ২৪০নং]\n\nযে কোন মানুষ, জ্বিন বা অন্য কিছু মুআযযিনের আযানের শব্দ শুনতে পাবে, সেই মুআযযিনের জন্য কিয়ামতের দিন সাক্ষ্য প্রদান করবে।\n\n[বুখারী ৬০৯ নং]"
            ));

        } else {
            // English Mode
            // 1. Significance & Virtues of Adhan (Full Chronicle)
            list.add(new StepItem(
                    "Significance & Virtues of Adhan",
                    "Adhan is Fard al-Kifayah and a major symbol of Islam... [Bukhari, Muslim, Quran 41:33]",
                    "Adhan is an obligation and prescribed as Fard al-Kifayah (communal obligation). The Messenger of Allah (ﷺ) said: 'When the time for prayer arrives, let one of you proclaim the Adhan, and let the oldest among you lead the prayer.'\n\n[Bukhari 628, Muslim, Nasa'i, Darimi]\n\nAdhan is a prominent symbol and landmark of Islam. If the residents of a town abandon it, the Muslim authority is to take action. When the Prophet (ﷺ) raided, he would withhold attacking if he heard the Adhan from a locality.\n\n[Bukhari 610, Sahih Muslim]\n\nIf one is traveling alone or far from any mosque where Adhan cannot be heard, it is Sunnah to call Adhan and Iqamah individually before praying.\n\n[Fatawa Islamiyyah, Saudi Scholars Committee 1/255]\n\nImmense reward is promised for the Mu'adhin. Allah Almighty says: 'And who is better in speech than one who invites to Allah and does righteousness and says, Indeed, I am of the Muslims?'\n\n[Al-Quran 41:33]\n\nThe Prophet (ﷺ) said: 'If people knew what reward there is in the Adhan and the first row, and found no other way to get it except by drawing lots, they would draw lots.'\n\n[Bukhari 615, Sahih Muslim 437]\n\nAllah sends mercy upon the first row and angels pray for forgiveness for them. The Mu'adhin is forgiven to the extent of his voice. Everything moist or dry that hears it confirms his words, and he receives reward equivalent to all who pray with him.\n\n[Ahmad, Nasa'i, Sahih at-Targhib 228]\n\nOn the Day of Resurrection, the Mu'adhins will have the longest necks (holding highest distinction and honor).\n\n[Sahih Muslim 387]\n\nWhoever calls Adhan for twelve years, Paradise becomes guaranteed for him. Every day sixty good deeds are written for his Adhan and thirty for his Iqamah.\n\n[Ibn Majah, Daraqutni, Hakim, Sahih at-Targhib 240]\n\nEvery human, jinn, or creature that hears the voice of the Mu'adhin will bear witness for him on the Day of Judgment.\n\n[Bukhari 609]"
            ));

            // 2. Rulings of Adhan & Symbol of Islam
            list.add(new StepItem(
                    "Rulings of Adhan & Symbol of Islam",
                    "Adhan as Fard al-Kifayah, symbol of Islamic communities, and Sunnah while traveling...",
                    "Adhan is prescribed as Fard al-Kifayah. The Prophet (ﷺ) said: 'When the time for prayer comes, let one of you call the Adhan, and let the eldest lead.' [Bukhari 628, Muslim]\n\nIt is a paramount emblem of Islam. If a locality proclaims Adhan, it establishes Islamic identity. Proclaiming Adhan and Iqamah when traveling alone is an established Sunnah. [Fatawa Islamiyyah 1/255]"
            ));

            // 3. Virtues & Immense Rewards for the Mu'adhin
            list.add(new StepItem(
                    "Virtues & Immense Rewards for the Mu'adhin",
                    "Calling to Allah, drawing lots for Adhan, and forgiveness to the reach of voice...",
                    "Allah states: 'And who is better in speech than one who invites to Allah...' [Quran 41:33]\n\nThe Prophet (ﷺ) remarked that people would draw lots if they knew the immense virtue of Adhan and the first row [Bukhari 615, Muslim 437]. The Mu'adhin receives forgiveness according to the reach of his voice and equal reward for all who pray with him [Ahmad, Nasa'i 228]."
            ));

            // 4. Honor on the Day of Judgment & Guarantee of Paradise
            list.add(new StepItem(
                    "Honor on the Day of Judgment & Guarantee of Paradise",
                    "Distinguished honor on Judgment Day, twelve years of calling Adhan, and testimony of creation...",
                    "Mu'adhins will possess the longest necks (highest honor) on Judgment Day [Muslim 387].\n\nWhoever calls Adhan for 12 years earns Paradise [Ibn Majah, Hakim 240], and all creation that heard his voice will testify in his favor [Bukhari 609]."
            ));
        }

        return list;
    }
}
