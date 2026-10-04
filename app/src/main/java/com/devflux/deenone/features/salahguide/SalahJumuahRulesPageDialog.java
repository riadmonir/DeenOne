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

public class SalahJumuahRulesPageDialog {

    public static class JumuahRuleItem {
        public final String title;
        public final String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public JumuahRuleItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "জুম'আর সালাত আদায়ের নিয়ম" : "Rules of Jum'ah Prayer");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick options
        });

        List<JumuahRuleItem> items = getJumuahItems(isBn);

        JumuahAdapter adapter = new JumuahAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class JumuahAdapter extends RecyclerView.Adapter<JumuahAdapter.ViewHolder> {
        private final Context context;
        private final List<JumuahRuleItem> items;
        private final boolean isBn;

        public JumuahAdapter(Context context, List<JumuahRuleItem> items, boolean isBn) {
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
            JumuahRuleItem item = items.get(position);
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
                        JumuahRuleItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(JumuahRuleItem item, boolean isBn) {
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

    public static List<JumuahRuleItem> getJumuahItems(boolean isBn) {
        List<JumuahRuleItem> list = new ArrayList<>();

        if (isBn) {
            // ১. জুম'আর সালাতের বিধান ও হুকুম
            list.add(new JumuahRuleItem(
                    "জুম'আর সালাতের হুকুম ও বিধান",
                    "জুম'আর সালাত প্রত্যেক বয়স্ক পুরুষ ও জ্ঞানসম্পন্ন মুসলমানের উপরে জামা'আত সহ আদায় করা ‘ফরযে আয়েন'...",
                    "জুম'আর সালাত প্রত্যেক বয়স্ক পুরুষ ও জ্ঞানসম্পন্ন মুসলমানের উপরে জামা'আত সহ আদায় করা ‘ফরযে আয়েন'। তবে গোলাম, রোগী, মুসাফির, শিশু ও মহিলাদের উপরে জুম'আ ফরয নয়। বাহরায়েন বাসীর প্রতি এক লিখিত ফরমানে খলীফা ওমর (রাঃ) বলেন,‘তোমরা যেখানেই থাক, জুম'আ আদায় কর'। অতএব দু'জন মুসলমান কোন স্থানে থাকলেও তারা একত্রে জুম'আ আদায় করবে। একজনে খুৎবা দিবে। যদি খুৎবা দিতে অপারগ হয়, তাহলে দু'জনে একত্রে জুম'আর দু'রাক'আত সালাত আদায় করবে। কারাবন্দী অবস্থায় অনুমতি পেলে করবে, নইলে করবে না। আল্লাহ বলেন, তোমরা তোমাদের সাধ্যমত আল্লাহকে ভয় কর\n\n\n" +
                            "[সূরা তাগাবুন - ৬৪/১৬]"
            ));

            // ২. জুম'আর সালাতের প্রস্তুতি ও আদায়ের পদ্ধতি
            list.add(new JumuahRuleItem(
                    "জুম'আর সালাতের প্রস্তুতি ও আদায়ের পদ্ধতি",
                    "জুম'আর দিন সুন্দরভাবে গোসল করে সাধ্যমত উত্তম পোষাক ও সুগন্ধি লাগিয়ে আগেভাগে মসজিদে যেতে হবে...",
                    "পদ্ধতি:\n\n" +
                            "জুম'আর দিন সুন্দরভাবে গোসল করে সাধ্যমত উত্তম পোষাক ও সুগন্ধি লাগিয়ে আগেভাগে মসজিদে যেতে হবে। মসজিদে প্রবেশ করে সামনের কাতারের দিকে এগিয়ে যাবে। এবং বসার পূর্বে প্রথমে দু'রাক'আত ‘তাহিইয়াতুল মাসজিদ’আদায় করবে। অতঃপর খত্বীব মিম্বরে বসার আগ পর্যন্ত দুই রাক'আত করে যত খুশী নফলসালাতে মগ্ন থাকবে।\n\n" +
                            "[মুসলিম, মুত্তাফাক্ব ‘আলাইহ, আবুদাঊদ, মিশকাত - ১৩৫৮, ১৩৮৪, ৮৭]\n\n\n" +
                            "এরপর চুপচাপ মনোযোগ সহকারে খুৎবা শুনবে। খুৎবা চলা অবস্থায় মসজিদে প্রবেশ করলে কেবল দু'রাক'আত ‘তাহিইয়াতুল মাসজিদ’সংক্ষেপে আদায় করে বসে পড়বে। এবং খুৎবা শুনবে। খত্বীব সাহেবের খুৎবা শেষ হলে জুম'আর সালাতের জন্য ক্বিবলামুখী হয়ে দাঁড়াবে। এরপর ফরয সালাতের নিয়ম অনুযায়ী দো'য়া পড়বে, সূরা ফাতিহা পড়বে ও ইমাম সাহেবের ক্বিরয়াত শুনবে।\n\n\n" +
                            "এই সময় ইমাম সাহেব প্রথম রাক'আতে সূরায়ে ‘জুম'আ’ অথবা সূরায়ে ‘আ'লা' এবং দ্বিতীয় রাক'আতে সূরায়ে ‘মুনা-ফিকূন’অথবা সূরায়ে‘গা-শিয়াহ’পড়বেন।\n\n" +
                            "[মুসলিম, মিশকাত - ৮৩৯-৪০ ছালাতে ক্বিরাআত, অনুচ্ছেদ-১২]\n\n\n" +
                            "উল্লেখ্য যে, এই সময় উপরে উল্লিখিত সূরা ব্যতীত অন্য সূরাও পড়া যাবে।\n\n" +
                            "[আবুদাঊদ - ৮১৮, ৮২০, ৮৫৯]"
            ));

            // ৩. জুম'আর রাক'আত না পেলে কিভাবে সালাত পড়েবেন/পদ্ধতি
            list.add(new JumuahRuleItem(
                    "জুম'আর রাক'আত না পেলে কিভাবে সালাত পড়েবেন/পদ্ধতি",
                    "জুম'আর সালাত ইমামের সাথে এক রাক'আত পেলে বাকী আরেক রাক'আত যোগ করে পূরা পড়ে নিলে হয়ে যাবে...",
                    "জুম'আর রাক'আত না পেলে কিভাবে সালাত পড়েবেন/পদ্ধতি:\n\n\n" +
                            "•  জুম'আর সালাত ইমামের সাথে এক রাক'আত পেলে বাকী আরেক রাক'আত যোগ করে পূরা পড়ে নিলে হয়ে যাবে\n\n" +
                            "[মুত্তাফাক্ব ‘আলাইহ, মিশকাত - ১৪১২, ‘ছালাত’অধ্যায়-৪, ‘খুৎবা ও ছালাত’ অনুচ্ছেদ-৪৫]\n\n\n" +
                            "•  দ্বিতীয় রাক'আতের রুকু না পেলে এবং শেষ বৈঠকে যোগ দিলে চার রাক'আত পূর্ণ করে পড়তে হবে\n\n" +
                            "[বায়হাক্বী ৩/২০৪]\n\n\n" +
                            "•  অর্থাৎ জুম'আর নিয়তে সালাতে যোগদান করবে এবং যোহর হিসাবে শেষ করবে ‘এর মাধ্যমে জাম'আতে যোগদানের পুরো নেকী পাবে।\n\n" +
                            "[ফিক্বহুস সুন্নাহ ১/২৩৫]"
            ));

            // ৪. জুম'আর সালাতের পরে সুন্নাত সালাতের পদ্ধতি
            list.add(new JumuahRuleItem(
                    "জুম'আর সালাতের পরে সুন্নাত সালাতের পদ্ধতি",
                    "জুম'আর সালাতের পরে মসজিদে চার রাক'আত সুন্নাত সালাত আদায় করতে হবে অথবা বাড়িতে দুই রাক'আত...",
                    "জুম'আর সালাতের পরে সুন্নাত সালাতের পদ্ধতি\n\n\n" +
                            "•  চার রাক'আত:\n\n\n" +
                            "জুম'আর সালাতের পরে মসজিদে চার রাক'আত সুন্নাত সালাত আদায় করতে হবে। রাসূল (সঃ) বলেন -\n\n\n" +
                            "مَنْ كَانَ مِنْكُمْ مُصَلِّيًا بَعْدَ الْجُمُعَةِ فَلْيُصَلِّ أَرْبَعًا\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "মান কা-না মিনকুম মুসাল্লিয়ান বা'দাল জুম'আতে ফালইয়ুসাল্লি আরবায়ান\"।\n\n" +
                            "অর্থ:\n\n" +
                            "তোমাদের যে লোক জুমু’আর (ফরয সালাতের) পর সালাত আদায় করতে চায় সে যেন চার রাক্’আত সালাত আদায় করে নেয়।\n\n" +
                            "[মুসলিম, মিশকাত হা/১১৬৬ ‘সুন্নাত ছালাত সমূহ ও তার ফযীলত অনুচ্ছেদ-৩০]\n\n\n" +
                            "•  দুই রাক'আত:\n\n\n" +
                            "বাড়িত গিয়ে দু'রাকা'আত সালাত আদায় করা যায়। ইবনু উমার (রাঃ) হতে বর্ণিত আছে, তিনি জুম'আর (ফরয) সালাত শেষ করে বাড়িতে গিয়ে দুই রাক'আত নামায আদায় করতেন। তারপর তিনি বলতেন, রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম এটা করতে\n\n" +
                            "[তিরমিযী হা/৫২২-২৩ ‘জুম'আ অধ্যায়-৪, অনুচ্ছেদ-২৪]\n\n\n" +
                            "উল্লেখ্য যে, মসজিদেও চার বা দুই কিংবা দুই ও চার মোট ছয় রাক‘আত সুন্নাত ও নফল পড়া যায়। ইবনু ওমর (রাঃ) চার রাক‘আত সুন্নাত এক সালামে পড়তেন। তবে দুই সালামেও পড়া যায়।\n\n" +
                            "[মিরআত ৪/২৫৭-৫৮]"
            ));
        } else {
            // English Mode
            // 1. Ruling & Obligation
            list.add(new JumuahRuleItem(
                    "Obligation & Ruling of Jum'ah Prayer",
                    "Jum'ah prayer is an individual obligation (Fard 'Ayn) with congregation upon every adult, sane Muslim male...",
                    "Jum'ah prayer is an individual obligation (Fard 'Ayn) with congregation upon every adult, sane Muslim male. However, it is not obligatory upon enslaved persons, the sick, travelers, children, and women. In a written decree to the people of Bahrain, Caliph Umar (RA) said, 'Establish Jum'ah wherever you are.' Therefore, even if only two Muslims are in a place, they should offer Jum'ah together. One will deliver the Khutbah; if unable to deliver a Khutbah, the two should pray the two Rak'ahs of Jum'ah together. In prison, if permitted, one should observe it, otherwise not. Allah says: 'So fear Allah as much as you can.'\n\n\n" +
                            "[Surah At-Taghabun - 64:16]"
            ));

            // 2. Preparation & Method
            list.add(new JumuahRuleItem(
                    "Preparation & Method of Performing Jum'ah Prayer",
                    "On the day of Jum'ah, perform a proper bath (Ghusl), wear the best available clothes, apply fragrance, and head to the mosque early...",
                    "Method:\n\n" +
                            "On the day of Jum'ah, perform a proper bath (Ghusl), wear the best available clothes, apply fragrance, and head to the mosque early. Enter the mosque, advance toward the front rows, and before sitting down, offer two Rak'ahs of 'Tahiyyatul Masjid'. Thereafter, until the Khatib ascends the pulpit (Minbar), remain engaged in voluntary (Nafl) prayers in sets of two Rak'ahs as much as desired.\n\n" +
                            "[Muslim, Muttafaqun 'Alayh, Abu Dawud, Mishkat - 1358, 1384, 87]\n\n\n" +
                            "Then listen attentively and silently to the Khutbah. If entering the mosque while the Khutbah is in progress, perform two brief Rak'ahs of 'Tahiyyatul Masjid' and then sit down to listen to the Khutbah. When the Khatib finishes the Khutbah, stand facing the Qiblah for the congregational Jum'ah prayer. Then follow standard congregational prayer rules: recite the opening supplication, Surah Al-Fatihah, and listen attentively to the Imam's recitation.\n\n\n" +
                            "During this prayer, the Imam typically recites Surah Al-Jumu'ah or Surah Al-A'la in the first Rak'ah, and Surah Al-Munafiqun or Surah Al-Ghashiyah in the second Rak'ah.\n\n" +
                            "[Muslim, Mishkat - 839-40, Section on Recitation in Prayer, Ch-12]\n\n\n" +
                            "Note that reciting other Surahs besides these is also permissible.\n\n" +
                            "[Abu Dawud - 818, 820, 859]"
            ));

            // 3. Method if Rak'ah is Missed
            list.add(new JumuahRuleItem(
                    "Method if a Rak'ah of Jum'ah is Missed",
                    "If you catch one Rak'ah of Jum'ah with the Imam, add one more Rak'ah after the Imam completes the prayer to fulfill it...",
                    "Method if a Rak'ah of Jum'ah is Missed:\n\n\n" +
                            "•  If you catch one Rak'ah of Jum'ah with the Imam, add one more Rak'ah after the Imam completes the prayer to fulfill it.\n\n" +
                            "[Muttafaqun 'Alayh, Mishkat - 1412, 'Salah' Chapter 4, 'Khutbah and Salah' Section 45]\n\n\n" +
                            "•  If you do not catch the Ruku of the second Rak'ah and only join in the final Tashahhud sitting, you must complete four Rak'ahs (as Dhuhr).\n\n" +
                            "[Bayhaqi 3/204]\n\n\n" +
                            "•  That is, join the prayer with the intention of Jum'ah and complete it as Dhuhr; through this one attains the full reward of joining the congregation.\n\n" +
                            "[Fiqh-us-Sunnah 1/235]"
            ));

            // 4. Sunnah Prayer After Jum'ah
            list.add(new JumuahRuleItem(
                    "Method of Sunnah Prayer After Jum'ah",
                    "After Jum'ah prayer, offer four Rak'ahs of Sunnah in the mosque or two Rak'ahs at home...",
                    "Method of Sunnah Prayer After Jum'ah:\n\n\n" +
                            "•  Four Rak'ahs:\n\n\n" +
                            "After Jum'ah prayer, four Rak'ahs of Sunnah prayer should be offered in the mosque. The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "مَنْ كَانَ مِنْكُمْ مُصَلِّيًا بَعْدَ الْجُمُعَةِ فَلْيُصَلِّ أَرْبَعًا\n\n\n" +
                            "Transliteration:\n\n" +
                            "\"Man kaana minkum musalliyan ba'dal jumu'ati falyusalli arba'an\"\n\n" +
                            "Meaning:\n\n" +
                            "Whoever among you wishes to pray after Jum'ah, let him pray four Rak'ahs.\n\n" +
                            "[Muslim, Mishkat Hadith 1166, Section 30]\n\n\n" +
                            "•  Two Rak'ahs:\n\n\n" +
                            "Two Rak'ahs can be prayed at home. It is narrated from Ibn Umar (RA) that after finishing the obligatory Jum'ah prayer, he would go home and offer two Rak'ahs, saying that the Messenger of Allah (ﷺ) used to do so.\n\n" +
                            "[Tirmidhi Hadith 522-23, Chapter 4, Section 24]\n\n\n" +
                            "Note that in the mosque, four, two, or a combination of both (six Rak'ahs) of Sunnah and Nafl may be prayed. Ibn Umar (RA) used to pray four Rak'ahs of Sunnah with one Tasleem, though praying with two Tasleems is also permissible.\n\n" +
                            "[Mir'at 4/257-58]"
            ));
        }

        return list;
    }
}
