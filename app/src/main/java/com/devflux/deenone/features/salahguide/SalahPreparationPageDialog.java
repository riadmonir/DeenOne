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

public class SalahPreparationPageDialog {

    public static class PreparationItem {
        public final String title;
        public final String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public PreparationItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "সালাতের জন্য প্রস্তুতি" : "Preparation for Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick options
        });

        List<PreparationItem> items = getPreparationItems(isBn);

        PreparationAdapter adapter = new PreparationAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class PreparationAdapter extends RecyclerView.Adapter<PreparationAdapter.ViewHolder> {
        private final Context context;
        private final List<PreparationItem> items;
        private final boolean isBn;

        public PreparationAdapter(Context context, List<PreparationItem> items, boolean isBn) {
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
            PreparationItem item = items.get(position);
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
                        PreparationItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(PreparationItem item, boolean isBn) {
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
                    binding.tvToggleText.setText(isBn ? "বিস্তারিত" : "Read More");
                    binding.ivToggleChevron.setRotation(0f);
                }
            }
        }
    }

    public static List<PreparationItem> getPreparationItems(boolean isBn) {
        List<PreparationItem> list = new ArrayList<>();

        if (isBn) {
            // ১. অজুর সঠিক পদ্ধতি ও পানির ব্যবহার
            list.add(new PreparationItem(
                    "অজুর সঠিক পদ্ধতি ও পানির ব্যবহার",
                    "প্রথমে মনে মনে ওযুর নিয়ত করবে। কারণ নিয়ত ছাড়া কোন কর্মই শুদ্ধ হয় না। [বুখারী, মুসলিম, মিশকাত ১] বিসমিল্লাহ্’ বলে ওযু শুরু করবে। কারণ শুরুতে তা না বললে ওযু হয় না...",
                    "প্রথমে মনে মনে ওযুর নিয়ত করবে। কারণ নিয়ত ছাড়া কোন কর্মই শুদ্ধ হয় না।\n\n" +
                            "[বুখারী, মুসলিম, মিশকাত ১]\n\n\n" +
                            "বিসমিল্লাহ্’ বলে ওযু শুরু করবে। কারণ শুরুতে তা না বললে ওযু হয় না।\n\n" +
                            "[আবূদাঊদ, সুনান ৯২নং]\n\n\n" +
                            "তিনবার দুইহাত কব্জি পর্যন্ত ধুয়ে নেবে।হাতে ঘড়ি, চুড়ি, আংটি প্রভৃতি থাকলে তা হিলিয়ে তার তলে পানি পৌঁছাবে। আঙ্গুল দিয়ে আঙ্গুলের ফাঁকগুলো খেলাল করবে।\n\n" +
                            "[আবূদাঊদ, সুনান, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, মিশকাত ৪০৭নং]\n\n\n" +
                            "এরপর পানির পাত্রে হাত ডুবিয়ে পানি নিতে পারে।\n\n" +
                            "[বুখারী, মুসলিম, সহীহ ৩৯৪নং]\n\n\n" +
                            "প্রকাশ যে, নখে নখ পালিশ বা কোন প্রকার পুরু পেন্ট থাকলে তা তুলে না ফেলা পর্যন্ত ওযু হবে না। পক্ষান্তরে মেহেদী বা আলতা লেগে থাকা অবস্থায় ওযু-গোসল হয়ে যাবে। তারপর ডানহাতে পানি নিয়ে ৩ বার কুল্লি করবে। অতঃপর পানি নিয়ে নাকের গোড়ায় লাগিয়ে টেনে নিয়ে বামহাত দ্বারা নাক ঝাড়বে। এরুপ ৩ বার করবে। তবে রোযা অবস্থায় থাকলে সাবধানে নাকে পানি টানবে, যাতে গলার নিচে পানি না চলে যায়।\n\n" +
                            "[তিরমিযী, সুনান, নাসাঈ, সুনান ৮৯, মিশকাত ৪০৫, ৪১০নং]\n\n\n" +
                            "অবশ্য এক লোট পানিতেই একই সাথে অর্ধেক দিয়ে কুল্লি করে বাকি অর্ধেক দিয়ে নাক ঝাড়লেও চলে।\n\n" +
                            "[বুখারী, মুসলিম, মিশকাত ৩৯৪নং]\n\n\n" +
                            "অতঃপর মুখমণ্ডল (এক কান থেকে অপর কানের মধ্যবর্তী এবং কপালের চুলের গোড়া থেকে দাড়ির নিচের অংশ পর্যন্ত অঙ্গ) ৩ বার পানি লাগিয়ে দুইহাত দ্বারা ধৌত করবে।\n\n" +
                            "[বুখারী ১৪০নং]\n\n\n" +
                            "এক লোট পানি দাড়ির মাঝে দিয়ে দাড়ির ফাঁকে ফাঁকে আঙ্গুল চালিয়ে তা খেলাল করবে।\n\n" +
                            "[আবূদাঊদ, সুনান, মিশকাত ৪০৮নং]\n\n\n" +
                            "মহিলাদের কপালে টিপ থাকলে ছাড়িয়ে ফেলে (কপাল) ধুতে হবে। নচেৎ ওযু হবে না। অতঃপর প্রথমে ডানহাত আঙ্গুলের ডগা থেকে কনুই পর্যন্ত এবং তদনুরুপ বামহাত ৩ বার (প্রত্যেক বারে পুরোহাতে পানি ফিরিয়ে রগড়ে) ধৌত করবে। অতঃপর একবার মাথা মাসাহ্ করবে; নতুন পানি দ্বারা দুই হাতকে ভিজিয়ে আঙ্গুল গুলিকে মুখোমুখি করে মাথার সামনের দিক (যেখান থেকে চুল গজানো শুরু হয়েছে সেখান) থেকে পিছন দিক (গর্দানের যেখানে চুল শেষ হয়েছে সেখান) পর্যন্ত স্পর্শ করে পুনরায় সামনের দিকে নিয়ে এসে শুরুর জায়গা পর্যন্ত পূর্ণ মাথা মাসাহ্ করবে।\n\n" +
                            "[বুখারী, মুসলিম, মিশকাত ৩৯৪নং]\n\n\n" +
                            "মাথায় পাগড়ি থাকলে তার উপরেও মাসাহ্ করবে।\n\n" +
                            "[মুসলিম, মিশকাত ৩৯৯নং]\n\n\n" +
                            "অতঃপর আর নতুন পানি না নিয়ে ঐ হাতেই দুই কান মাসাহ্ করবে; শাহাদতের (তর্জনী) দুই আঙ্গুল দ্বারা দুই কানের ভিতর দিক এবং দুই বুড়ো আঙ্গুল দ্বারা দুই কানের পিঠ ও বাহির দিক মাসাহ্ করবে।\n\n" +
                            "[আবূদাঊদ, সুনান ৯৯, ১২৫নং]\n\n\n" +
                            "প্রকাশ যে, গর্দান মাসাহ্ করা বিধেয় নয়। বরং এটা বিদআত। অতঃপর প্রথমে ডান পা ও পরে বাম পা গাঁট পর্যন্ত ৩ বার করে রগড়ে ধোবে। কড়ে আঙ্গুল দ্বারা পায়ের আঙ্গুলের ফাঁকগুলো খেলাল করে রগড়ে ধৌত করবে।\n\n" +
                            "[আবূদাঊদ, সুনান, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, মিশকাত ৪০৭নং]\n\n\n" +
                            "এরপর হাতে পানি নিয়ে কাপড়ের উপর থেকে শরমগাহে ছিটিয়ে দেবে। বিশেষ করে পেশাব করার পর ওযু করলে এই আমল অধিকরুপে ব্যবহার্য। যেহেতু পেশাব করে তাহারতের পর দু-এক কাতরা পেশাব বের হওয়ার অসঅসা থাকে। সুতরাং পানি ছিটিয়ে দিলে ঐ অসঅসা দূর হয়ে যায়।\n\n" +
                            "[আবূদাঊদ, সুনান ১৫২-১৫৪, ইবনে মাজাহ্, সুনান ৩৭৪-৩৭৬নং]\n\n\n" +
                            "এই আমল খোদ জিবরাঈল (আঃ) মহানবী (ﷺ) কে শিক্ষা দিয়েছেন।\n\n" +
                            "[ইবনে মাজাহ্, সুনান, দারেমী]"
            ));

            // ২. তায়াম্মুম করে অজু করার পদ্ধতি
            list.add(new PreparationItem(
                    "তায়াম্মুম করে অজু করার পদ্ধতি",
                    "তায়াম্মুম তখন করা হয় যখন পানি পাওয়া যায় না বা পানি ব্যবহারের উপযোগী না হয়। এটি পানি দিয়ে অজু বা গোসলের পরিবর্তে করা হয়। তায়াম্মুমের ধাপসমূহ: নিয়ত করা: তায়াম্মুম করার আগে নিয়ত করা...",
                    "তায়াম্মুম তখন করা হয় যখন পানি পাওয়া যায় না বা পানি ব্যবহারের উপযোগী না হয়। এটি পানি দিয়ে অজু বা গোসলের পরিবর্তে করা হয়।\n\n\n" +
                            "তায়াম্মুমের ধাপসমূহ:\n\n" +
                            "নিয়ত করা: তায়াম্মুম করার আগে নিয়ত করা।\n" +
                            "মাটি বা ধূলার উপর হাত মারানো: পবিত্র মাটি বা ধূলার উপর দুই হাত মারানো।\n" +
                            "মুখ মুছা: একবার মুখ মুছা।\n" +
                            "হাত মুছা: একবার দুই হাতের উপরিভাগ মুছা।\n\n\n" +
                            "কুরআনের আলোকে:\n\n\n" +
                            "فَتَيَمَّمُوا صَعِيدًا طَيِّبًا\n\n" +
                            "অর্থ:\n" +
                            "তাহলে তোমরা পবিত্র মাটি দিয়ে তায়াম্মুম কর।\n\n" +
                            "[সূরা আন-নিসা, ৪:৪৩]"
            ));

            // ৩. অজুর নিয়ত
            list.add(new PreparationItem(
                    "অজুর নিয়ত",
                    "কুরআনে উল্লেখ: ...يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى",
                    "কুরআনে উল্লেখ:\n\n\n" +
                            "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ وَامْسَحُوا بِرُءُوسِكُمْ وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ইয়্যা আইয়্যুহাল্লাযিনা আমানু ইযা কুমতুম ইলাস সালাতি ফাগসিলু উজূহাকুম ওয়া আইদিয়াকুম ইলাল মারাফিকি ওয়ামসাহু বিরুউসিকুম ওয়া আরজুলাকুম ইলাল কা'বাইন\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে মুমিনগণ, যখন তোমরা নামাজের জন্য প্রস্তুত হও, তখন তোমাদের মুখমণ্ডল, হাত-কনুই পর্যন্ত ধুয়ে নাও এবং তোমাদের মাথা মাসাহ করো এবং পা-গোড়ালি পর্যন্ত ধুয়ে নাও।\n\n" +
                            "[সূরা আল-মায়েদা ৫:৬]"
            ));

            // ৪. অজুর ফরজ বিষয়গুলো
            list.add(new PreparationItem(
                    "অজুর ফরজ বিষয়গুলো",
                    "মুখমণ্ডল ধোয়া (غسل الوجه): ...يَا أَيُّهَا الَّذِينَ آمَنُوا",
                    "মুখমণ্ডল ধোয়া (غسل الوجه):\n\n\n" +
                            "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ইয়্যা আইয়্যুহাল্লাযিনা আমানু ইযা কুমতুম ইলাস সালাতি ফাগসিলু উজূহাকুম\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে মুমিনগণ, যখন তোমরা নামাজের জন্য প্রস্তুত হও, তখন তোমাদের মুখমণ্ডল ধুয়ে নাও।\n\n" +
                            "মুখমণ্ডল বলতে কপাল থেকে থুতনি এবং এক কানের লতি থেকে অন্য কানের লতি পর্যন্ত অংশ ধোয়া।\n\n" +
                            "[সূরা আল-মায়েদা ৫:৬]\n\n\n" +
                            "হাত ধোয়া (غسل اليدين):\n\n\n" +
                            "وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ওয়া আইদিয়াকুম ইলাল মারাফিকি\n\n\n" +
                            "অর্থ:\n\n" +
                            "এবং তোমাদের হাত-কনুই পর্যন্ত ধুয়ে নাও। হাতের আঙ্গুল থেকে কনুই পর্যন্ত ধোয়া।\n\n" +
                            "[সূরা আল-মায়েদা ৫:৬]\n\n\n" +
                            "মাথা মাসাহ করা (مسح الرأس):\n\n\n" +
                            "وَامْسَحُوا بِرُءُوسِكُمْ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ওয়ামসাহু বিরুউসিকুম\n\n\n" +
                            "অর্থ:\n\n" +
                            "এবং তোমাদের মাথা মাসাহ করো।\n\n" +
                            "ভেজা হাত দিয়ে মাথার এক চতুর্থাংশ মাসাহ করা।\n\n" +
                            "[সূরা আল-মায়েদা ৫:৬]\n\n\n" +
                            "পা ধোয়া (غسل الرجلين):\n\n\n" +
                            "وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ওয়া আরজুলাকুম ইলাল কা'বাইন\n\n\n" +
                            "অর্থ:\n\n" +
                            "এবং তোমাদের পা-গোড়ালি পর্যন্ত ধুয়ে নাও।\n\n" +
                            "পায়ের আঙ্গুল থেকে গোড়ালি পর্যন্ত ধোয়া।\n\n" +
                            "[সূরা আল-মায়েদা ৫:৬]"
            ));

            // ৫. অজুর ধাপসমূহ
            list.add(new PreparationItem(
                    "অজুর ধাপসমূহ",
                    "নিয়ত (ইচ্ছা): অজু শুরু করার আগে নিয়ত করতে হবে। নিয়ত মনের ইচ্ছা দ্বারা হবে, মুখে উচ্চারণ করা জরুরি নয়...",
                    "নিয়ত (ইচ্ছা):\n\n" +
                            "অজু শুরু করার আগে নিয়ত করতে হবে। নিয়ত মনের ইচ্ছা দ্বারা হবে, মুখে উচ্চারণ করা জরুরি নয়। তবে মুখে উচ্চারণ করাও সুন্নত।\n\n\n" +
                            "نَوَيْتُ أَنْ أَتَوَضَّأَ لِلصَّلَاةِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "নাওয়াইতু আন আতাওয়াজ্জা' লিস্সালাতি\n\n\n" +
                            "অর্থ:\n\n" +
                            "আমি নামাজের জন্য অজু করার নিয়ত করছি।\n\n\n" +
                            "বিসমিল্লাহ (আল্লাহর নামে শুরু করা):\n\n\n" +
                            "بِسْمِ اللهِ الرَّحْمَٰنِ الرَّحِيمِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "বিসমিল্লাহির রাহমানির রাহিম\n\n\n" +
                            "অর্থ:\n\n" +
                            "আল্লাহর নামে শুরু করছি যিনি পরম করুণাময়, অতি দয়ালু।"
            ));
        } else {
            // English Items
            list.add(new PreparationItem(
                    "Proper Method of Wudu & Water Usage",
                    "Make the intention in heart for Wudu. Because intention is...",
                    "Begin with sincere intention (Niyyah) in the heart and say 'Bismillah'. Wash both hands up to the wrists three times, ensuring moderate water usage without wastage."
            ));

            list.add(new PreparationItem(
                    "Method of Performing Tayammum",
                    "Tayammum is performed when water is unavailable or cannot be used. It is performed in place of Wudu or Ghusl with water. Steps of Tayammum: Making Intention (Niyyah)...",
                    "Tayammum is performed when water is unavailable or cannot be used. It is performed in place of Wudu or Ghusl with water.\n\n\n" +
                            "Steps of Tayammum:\n\n" +
                            "Making Intention (Niyyah): Sincere intention before performing Tayammum.\n" +
                            "Striking clean earth/dust: Striking both hands once on clean earth or dust.\n" +
                            "Wiping the face: Wiping over the face once with the palms.\n" +
                            "Wiping the hands: Wiping over the back of both hands once.\n\n\n" +
                            "In Light of the Quran:\n\n\n" +
                            "فَتَيَمَّمُوا صَعِيدًا طَيِّبًا\n\n" +
                            "Translation:\n" +
                            "\"Then perform Tayammum with clean earth.\"\n\n" +
                            "[Surah An-Nisa, 4:43]"
            ));

            list.add(new PreparationItem(
                    "Intention of Wudu",
                    "Mentioned in Quran: ...يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى",
                    "Mentioned in the Holy Quran:\n\n\n" +
                            "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ وَامْسَحُوا بِرُءُوسِكُمْ وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Ya ayyuhalladhina amanu idha qumtum ila-ssalati faghsilu wujuhakum wa aydiyakum ila-lmarafiqi wamsahu biru'usikum wa arjulakum ila-lka'bayn\n\n\n" +
                            "Translation:\n\n" +
                            "\"O you who have believed, when you rise to [perform] prayer, wash your faces and your forearms to the elbows and wipe over your heads and wash your feet to the ankles.\"\n\n" +
                            "[Surah Al-Ma'idah 5:6]"
            ));

            list.add(new PreparationItem(
                    "Obligatory Acts (Fard) of Wudu",
                    "Washing the face (غسل الوجه): ...يَا أَيُّهَا الَّذِينَ آمَنُوا",
                    "Washing the face (غسل الوجه):\n\n\n" +
                            "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا قُمْتُمْ إِلَى الصَّلَاةِ فَاغْسِلُوا وُجُوهَكُمْ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Ya ayyuhalladhina amanu idha qumtum ila-ssalati faghsilu wujuhakum\n\n\n" +
                            "Translation:\n\n" +
                            "\"O you who have believed, when you rise to [perform] prayer, wash your faces.\"\n\n" +
                            "Washing the face covers from the hairline of the forehead to the chin, and from one earlobe to the other.\n\n" +
                            "[Surah Al-Ma'idah 5:6]\n\n\n" +
                            "Washing the hands (غسل اليدين):\n\n\n" +
                            "وَأَيْدِيَكُمْ إِلَى الْمَرَافِقِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Wa aydiyakum ila-lmarafiq\n\n\n" +
                            "Translation:\n\n" +
                            "\"And your hands and forearms to the elbows.\"\n\n" +
                            "Washing from the fingertips up to and including both elbows.\n\n" +
                            "[Surah Al-Ma'idah 5:6]\n\n\n" +
                            "Wiping the head (مسح الرأس):\n\n\n" +
                            "وَامْسَحُوا بِرُءُوسِكُمْ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Wamsahu biru'usikum\n\n\n" +
                            "Translation:\n\n" +
                            "\"And wipe over your heads.\"\n\n" +
                            "Wiping over at least a quarter of the head with wet hands.\n\n" +
                            "[Surah Al-Ma'idah 5:6]\n\n\n" +
                            "Washing the feet (غسل الرجلين):\n\n\n" +
                            "وَأَرْجُلَكُمْ إِلَى الْكَعْبَيْنِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Wa arjulakum ila-lka'bayn\n\n\n" +
                            "Translation:\n\n" +
                            "\"And wash your feet to the ankles.\"\n\n" +
                            "Washing both feet from toes up to and including the ankles.\n\n" +
                            "[Surah Al-Ma'idah 5:6]"
            ));

            list.add(new PreparationItem(
                    "Steps of Wudu",
                    "Intention (Niyyah): Before starting Wudu, make the intention. Intention resides in the heart...",
                    "Intention (Niyyah):\n\n" +
                            "Before starting Wudu, one must make sincere intention. The intention is an act of the heart; verbal utterance is not mandatory, though permissible.\n\n\n" +
                            "نَوَيْتُ أَنْ أَتَوَضَّأَ لِلصَّلَاةِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Nawaytu an atawadda'a lis-salah\n\n\n" +
                            "Translation:\n\n" +
                            "\"I intend to perform Wudu for prayer.\"\n\n\n" +
                            "Bismillah (Beginning in the Name of Allah):\n\n\n" +
                            "بِسْمِ اللهِ الرَّحْمَٰنِ الرَّحِيمِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Bismillahir Rahmanir Rahim\n\n\n" +
                            "Translation:\n\n" +
                            "\"In the name of Allah, the Most Gracious, the Most Merciful.\""
            ));
        }

        return list;
    }
}
