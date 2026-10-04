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

public class SalahAzanWomenPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "খাস মহিলা মহলে মহিলাদের আযান ও ইকামত" : "Adhan & Iqamah for Women in Exclusive Gatherings");

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
            // 1. খাস মহিলা মহলে মহিলাদের আযান ও ইকামত (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "খাস মহিলা মহলে মহিলাদের আযান ও ইকামত",
                    "মহিলাদের আযান-ইকামত সংক্রান্ত আসার, বাইহাকীর বর্ণনা ও তাহক্বীক্ব... [আলবানী, রওযাতুন নাদিয়্যাহ্]",
                    "হযরত আয়েশা (রাঃ) এর আযান ও ইকামত দেওয়ার ব্যাপারে বর্ণিত হাদীস সহীহ নয়। অবশ্য বাইহাকীতে আছে, আম্র বিন আবী সালামাহ্ বলেন, আমি সওবানকে জিজ্ঞাসা করলাম যে, ‘মেয়েরা কি ইকামত দিতে পারে?’ উত্তরে তিনি তাঁর পিতা হতে বর্ণনার কথা উল্লেখ করে বললেন, ‘মকহুল বলেছেন, যদি মহিলারা আযান-ইকামত দেয় তবে তা আফযল। আর যদি শুধু ইকামত দেয়, তবে তাও যথেষ্ট।’ সওবান বলেন, যুহ্রী উরওয়া হতে এবং তিনি হযরত আয়েশা (রাঃ) হতে বর্ণনা করেছেন যে, তিনি (আয়েশা) বলেছেন, ‘আমরা বিনা ইকামতেই নামায পড়তাম।\n\nইমাম বাইহাকী বলেন, প্রথমোক্ত আসারের সাথে -যদি এই আসার সহীহ হয় তাহলে উভয়ের মধ্যে- পরস্পর বিরোধিতা নেই। কারণ, হতে পারে যে, জায়েয বর্ণনার উদ্দেশ্যে তিনি উভয় প্রকারের আমল (কখনো এরুপ, কখনো ঐরুপ) করেছেন। আর আল্লাহই অধিক জানেন।’\n\nআল্লামা আলবানী বলেন, এ ব্যাপারে সঠিক অভিমত হল নবাব সিদ্দীক হাসান খানের; তিনি বলেছেন, আর প্রকাশ যে, মহিলারা আমলে পুরুষদের মতই। কারণ, মহিলারা পুরুষদের সহোদরা। পুরুষদেরকে যা করতে আদেশ হয়, সে আদেশ মহিলাদের উপরেও বর্তায়। পক্ষান্তরে তাদের পক্ষে আযান-ইকামত ওয়াজেব না হওয়ার ব্যাপারে কোন গ্রহণযোগ্য দলীল নেই। আযান না থাকার ব্যাপারে বর্ণিত হাদীসের সনদের কিছু বর্ণনাকারী পরিত্যক্ত; যাদের হাদীস দলীলযোগ্য নয়। সুতরাং মহিলাদেরকে সাধারণ এ নির্দেশ থেকে খারিজ করার মত কোন নির্ভরযোগ্য দলীল থাকলে উত্তম; নচেৎ ওরাও পুরুষদের মতই।\n\n[আর-রওযাতুন নাদিয়্যাহ্ ১/৭৯, সিলসিলাহ যায়ীফাহ, আলবানী ২/২৭১]"
            ));

            // 2. আয়েশা (রাঃ)-এর আসার ও বাইহাকীর বর্ণনা
            list.add(new StepItem(
                    "আয়েশা (রাঃ)-এর আসার ও বাইহাকীর বর্ণনা",
                    "মকহুলের ফতোয়া ও উরওয়া সূত্রে আয়েশা (রাঃ)-এর বর্ণনা...",
                    "হযরত আয়েশা (রাঃ) এর আযান ও ইকামত দেওয়ার ব্যাপারে বর্ণিত হাদীস সহীহ নয়। অবশ্য বাইহাকীতে আছে, আম্র বিন আবী সালামাহ্ বলেন, আমি সওবানকে জিজ্ঞাসা করলাম যে, ‘মেয়েরা কি ইকামত দিতে পারে?’ উত্তরে তিনি তাঁর পিতা হতে বর্ণনার কথা উল্লেখ করে বললেন, ‘মকহুল বলেছেন, যদি মহিলারা আযান-ইকামত দেয় তবে তা আফযল। আর যদি শুধু ইকামত দেয়, তবে তাও যথেষ্ট।’ সওবান বলেন, যুহ্রী উরওয়া হতে এবং তিনি হযরত আয়েশা (রাঃ) হতে বর্ণনা করেছেন যে, তিনি (আয়েশা) বলেছেন, ‘আমরা বিনা ইকামতেই নামায পড়তাম।"
            ));

            // 3. ইমাম বাইহাকীর পর্যালোচনা ও সমন্বয়
            list.add(new StepItem(
                    "ইমাম বাইহাকীর পর্যালোচনা ও সমন্বয়",
                    "উভয় বর্ণনার মধ্যে সমন্বয় ও জায়েয বর্ণনার বিশ্লেষণ...",
                    "ইমাম বাইহাকী বলেন, প্রথমোক্ত আসারের সাথে -যদি এই আসার সহীহ হয় তাহলে উভয়ের মধ্যে- পরস্পর বিরোধিতা নেই। কারণ, হতে পারে যে, জায়েয বর্ণনার উদ্দেশ্যে তিনি উভয় প্রকারের আমল (কখনো এরুপ, কখনো ঐরুপ) করেছেন। আর আল্লাহই অধিক জানেন।’"
            ));

            // 4. আল্লামা আলবানী ও নবাব সিদ্দীক হাসান খানের তাহক্বীক্ব
            list.add(new StepItem(
                    "আল্লামা আলবানী ও নবাব সিদ্দীক হাসান খানের তাহক্বীক্ব",
                    "মহিলারা আমলে পুরুষদের সহোদরা ও আযান-ইকামতের বিশ্লেষণ...",
                    "আল্লামা আলবানী বলেন, এ ব্যাপারে সঠিক অভিমত হল নবাব সিদ্দীক হাসান খানের; তিনি বলেছেন, আর প্রকাশ যে, মহিলারা আমলে পুরুষদের মতই। কারণ, মহিলারা পুরুষদের সহোদরা। পুরুষদেরকে যা করতে আদেশ হয়, সে আদেশ মহিলাদের উপরেও বর্তায়। পক্ষান্তরে তাদের পক্ষে আযান-ইকামত ওয়াজেব না হওয়ার ব্যাপারে কোন গ্রহণযোগ্য দলীল নেই। আযান না থাকার ব্যাপারে বর্ণিত হাদীসের সনদের কিছু বর্ণনাকারী পরিত্যক্ত; যাদের হাদীস দলীলযোগ্য নয়। সুতরাং মহিলাদেরকে সাধারণ এ নির্দেশ থেকে খারিজ করার মত কোন নির্ভরযোগ্য দলীল থাকলে উত্তম; নচেৎ ওরাও পুরুষদের মতই।\n\n[আর-রওযাতুন নাদিয়্যাহ্ ১/৭৯, সিলসিলাহ যায়ীফাহ, আলবানী ২/২৭১]"
            ));

        } else {
            // English Mode
            // 1. Adhan & Iqamah for Women in Exclusive Gatherings (Full Narrative)
            list.add(new StepItem(
                    "Adhan & Iqamah for Women in Exclusive Gatherings",
                    "Scholarly research regarding women proclaiming Adhan & Iqamah... [Albani, Rawdatun Nadiyyah]",
                    "The hadith narrated regarding Aisha (RA) calling Adhan and Iqamah is not authentic (Sahih). However, in al-Bayhaqi, Amr ibn Abi Salamah said: I asked Thawban, 'Can women proclaim Iqamah?' In reply, referring to a narration from his father, he said: 'Makhul said, if women call both Adhan and Iqamah it is more virtuous (Afdal). And if they only call Iqamah, that is also sufficient.' Thawban said: Zuhri narrated from Urwah, and he from Aisha (RA) that she (Aisha) said: 'We used to pray without Iqamah.'\n\nImam al-Bayhaqi states: 'There is no contradiction between this and the aforementioned narration—if both narrations are authentic. This is because it is possible that she practiced both ways (sometimes this way, sometimes that way) to demonstrate permissibility. And Allah knows best.'\n\nAllamah al-Albani says: 'The correct opinion in this regard is that of Nawab Siddiq Hasan Khan; who said: And it is apparent that women in deeds are like men. Because women are counterparts of men. Whatever men are commanded to do, that command applies to women as well. Conversely, there is no acceptable evidence that Adhan and Iqamah are not obligatory upon them. In the chains of narrations denying Adhan for women, some narrators are abandoned (Matruk) whose reports cannot be used as evidence. Therefore, if there is reliable evidence excluding women from this general command, that is fine; otherwise, they are just like men.'\n\n[Ar-Rawdatun Nadiyyah 1/79, Silsilah Da'ifah, al-Albani 2/271]"
            ));

            // 2. Narration of Aisha (RA) & al-Bayhaqi
            list.add(new StepItem(
                    "Narration of Aisha (RA) & al-Bayhaqi",
                    "Fatwa of Makhul and Urwah's narration from Aisha (RA)...",
                    "The hadith regarding Aisha (RA) giving Adhan and Iqamah is not authentic. However, in al-Bayhaqi, Makhul stated that if women call Adhan and Iqamah it is Afdal, and Iqamah alone is sufficient. Urwah narrated from Aisha (RA) that they prayed without Iqamah."
            ));

            // 3. Analysis & Reconciliation by Imam al-Bayhaqi
            list.add(new StepItem(
                    "Analysis & Reconciliation by Imam al-Bayhaqi",
                    "Reconciling narrations showing both actions to demonstrate permissibility...",
                    "Imam al-Bayhaqi explained that there is no contradiction if both reports are authentic, as Aisha (RA) may have acted in both manners to demonstrate permissibility. And Allah knows best."
            ));

            // 4. Research of Allamah al-Albani & Nawab Siddiq Hasan Khan
            list.add(new StepItem(
                    "Research of Allamah al-Albani & Nawab Siddiq Hasan Khan",
                    "Women are counterparts of men in Islamic rulings...",
                    "Allamah al-Albani upholds Nawab Siddiq Hasan Khan's view: women are counterparts of men in deeds. Unless authentic specific evidence exempts women from the general command of Adhan and Iqamah, the general ruling of prayer calls applies equally to them. [Ar-Rawdatun Nadiyyah 1/79, Silsilah Da'ifah 2/271]"
            ));
        }

        return list;
    }
}
