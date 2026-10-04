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

public class SalahJumuahStepsPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "জুম'আ নামাজ শেখার ধাপসমূহ" : "Step-by-Step Jum'ah Prayer Guide");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick options
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
            // ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "নামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ: \"আমি দুই রাকাত জুম'আর ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\"",
                    "নিয়ত (ইচ্ছা):\n\n" +
                            "নামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ, \"আমি দুই রাকাত জুম'আর ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\""
            ));

            // ২. তাকবির (আল্লাহু আকবার বলা)
            list.add(new StepItem(
                    "২. তাকবির (আল্লাহু আকবার বলা)",
                    "নামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়...",
                    "তাকবির (আল্লাহু আকবার বলা):\n\n" +
                            "নামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়।"
            ));

            // ৩. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৩. কিয়াম (দাঁড়ানো)",
                    "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়...",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়।"
            ));

            // ৪. রুকু (ঝুঁকে থাকা)
            list.add(new StepItem(
                    "৪. রুকু (ঝুঁকে থাকা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান এবং তিনবার \"সুবহানা রাব্বিয়াল আযীম\"...",
                    "রুকু (ঝুঁকে থাকা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান এবং তিনবার \"সুবহানা রাব্বিয়াল আযীম\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) বলুন।"
            ));

            // ৫. কিয়াম (রুকু থেকে উঠা)
            list.add(new StepItem(
                    "৫. কিয়াম (রুকু থেকে উঠা)",
                    "রুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" এবং \"রাব্বানা লাকা-ল হাম্দ\" বলুন...",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "রুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ) এবং পরে \"রাব্বানা লাকা-ল হাম্দ\" (رَبَّنَا لَكَ الْحَمْدُ) বলুন।"
            ));

            // ৬. সিজদা (প্রথম সেজদা)
            list.add(new StepItem(
                    "৬. সিজদা (প্রথম সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সিজদায় যান এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\"...",
                    "সিজদা (প্রথম সেজদা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সিজদায় যান এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।"
            ));

            // ৭. জলসা (দুই সেজদার মধ্যে বসা)
            list.add(new StepItem(
                    "৭. জলসা (দুই সেজদার মধ্যে বসা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন...",
                    "জলসা (দুই সেজদার মধ্যে বসা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন।"
            ));

            // ৮. সিজদা (দ্বিতীয় সেজদা)
            list.add(new StepItem(
                    "৮. সিজদা (দ্বিতীয় সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\"...",
                    "সিজদা (দ্বিতীয় সেজদা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।"
            ));

            // ৯. দ্বিতীয় রাকাত
            list.add(new StepItem(
                    "৯. দ্বিতীয় রাকাত",
                    "দ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন...",
                    "দ্বিতীয় রাকাত:\n\n" +
                            "দ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন।"
            ));

            // ১০. তাশাহহুদ (আত্তাহিয়্যাতু)
            list.add(new StepItem(
                    "১০. তাশাহহুদ (আত্তাহিয়্যাতু)",
                    "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন: التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ...",
                    "তাশাহহুদ (আত্তাহিয়্যাতু):\n\n" +
                            "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন:\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত্-তাইয়িবাতু, আস-সালামু আলাইকা আইয়্যুহান-নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আস-সালামু আলাইনা ওয়া আ’লা ইবাদিল্লাহিস-সালিহীন, আশহাদু আল-লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান ‘আবদুহু ওয়া রাসূলুহু\n\n\n" +
                            "অর্থ:\n\n" +
                            "সমস্ত সম্মান, সমস্ত দোয়া এবং সমস্ত পবিত্রতা আল্লাহর জন্য। হে নবী, আপনার প্রতি সালাম, আল্লাহর রহমত এবং বরকত। আমাদের এবং আল্লাহর নেক বান্দাদের প্রতি সালাম। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া আর কোন ইলাহ নেই এবং মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) আল্লাহর বান্দা এবং রাসূল।"
            ));

            // ১১. দরুদ শরীফ
            list.add(new StepItem(
                    "১১. দরুদ শরীফ",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ...",
                    "দরুদ শরীফ:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা সাল্লি আ’লা মুহাম্মাদিন ওয়া আ’লা আ-লি মুহাম্মাদিন কামা সাল্লাইতা আ’লা ইবরাহিমা ওয়া আ-লি ইবরাহিমা ইন্নাকা হামিদুম মজিদ\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং তার পরিবারবর্গের উপর দোয়া করো যেমন তুমি ইবরাহিম (আলাইহিস সালাম) এবং তার পরিবারবর্গের উপর দোয়া করেছো। তুমি প্রশংসিত ও মহিমান্বিত।"
            ));

            // ১২. দু'আ
            list.add(new StepItem(
                    "১২. দু'আ (মাসুরা)",
                    "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো: اللَّهُمَّ إِنِّي ظَلَمْتُ...",
                    "দু'আ:\n\n" +
                            "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো:\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা ইন্নি যালামতু নাফসি যুলমান কাসিরান ওয়া লা ইয়াগফিরুজ্ যুনুবা ইল্লা আন্ত, ফাগফিরলি মাগফিরাতাম্ মিন্ আ'িন্দিকা ওয়ারহামনি ইন্নাকা আন্তাল্ গাফুরুর্ রহিম\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! আমি আমার নিজের প্রতি অনেক অত্যাচার করেছি এবং তুমি ছাড়া কেউ গুনাহ ক্ষমা করতে পারে না। তুমি তোমার পক্ষ থেকে আমাকে ক্ষমা করো এবং আমার প্রতি দয়া করো। নিশ্চয়ই তুমি মহা ক্ষমাশীল, পরম দয়ালু।"
            ));

            // ১৩. সালাম (নামাজ সমাপ্ত করা)
            list.add(new StepItem(
                    "১৩. সালাম (নামাজ সমাপ্ত করা)",
                    "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়: السلام عليكم ورحمة الله...",
                    "সালাম (নামাজ সমাপ্ত করা):\n\n" +
                            "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়\n\n\n" +
                            "السلام عليكم ورحمة الله\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আস-সালামু আলাইকুম ওয়া রহমাতুল্লাহ\n\n\n" +
                            "অর্থ:\n\n" +
                            "আপনার প্রতি শান্তি ও আল্লাহর করুণা বর্ষিত হোক।"
            ));
        } else {
            // English Mode
            // 1. Intention (Niyyah)
            list.add(new StepItem(
                    "1. Intention (Niyyah)",
                    "Form the intention before starting prayer. For example: \"I intend to offer two Rak'ahs of obligatory Jum'ah prayer for Allah.\"",
                    "Intention (Niyyah):\n\n" +
                            "Intention must be made before starting the prayer. For example: \"I intend to offer two Rak'ahs of obligatory (Fard) Jum'ah prayer for the sake of Allah.\""
            ));

            // 2. Takbir
            list.add(new StepItem(
                    "2. Takbir (Declaring Allahu Akbar)",
                    "Begin the prayer by declaring \"Allahu Akbar\" (اللَّهُ أَكْبَرُ)...",
                    "Takbir (Declaring Allahu Akbar):\n\n" +
                            "Begin the prayer by declaring \"Allahu Akbar\" (اللَّهُ أَكْبَرُ)."
            ));

            // 3. Qiyam (Standing)
            list.add(new StepItem(
                    "3. Qiyam (Standing)",
                    "Stand upright and recite Surah Al-Fatihah followed by another Surah from the Holy Quran...",
                    "Qiyam (Standing):\n\n" +
                            "Stand upright at the beginning of the prayer and recite Surah Al-Fatihah followed by another Surah from the Quran."
            ));

            // 4. Ruku (Bowing)
            list.add(new StepItem(
                    "4. Ruku (Bowing)",
                    "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and bow down, reciting \"Subhana Rabbiyal Azeem\" (3 times)...",
                    "Ruku (Bowing):\n\n" +
                            "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and bow into Ruku, saying \"Subhana Rabbiyal Azeem\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) three times."
            ));

            // 5. Rising from Ruku
            list.add(new StepItem(
                    "5. Qiyam (Rising from Ruku)",
                    "While rising from Ruku say \"Sami' Allahu liman hamidah\" followed by \"Rabbana lakal hamd\"...",
                    "Qiyam (Rising from Ruku):\n\n" +
                            "While rising from Ruku say \"Sami' Allahu liman hamidah\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ) and then say \"Rabbana lakal hamd\" (رَبَّنَا لَكَ الْحَمْدُ)."
            ));

            // 6. First Sujud
            list.add(new StepItem(
                    "6. Sujud (First Prostration)",
                    "Say \"Allahu Akbar\" and prostrate, reciting \"Subhana Rabbiyal A'la\" (3 times)...",
                    "Sujud (First Prostration):\n\n" +
                            "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and go down in prostration, reciting \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times."
            ));

            // 7. Jalsah (Sitting between Sujud)
            list.add(new StepItem(
                    "7. Jalsah (Sitting Between Prostrations)",
                    "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and sit up calmly between the two prostrations...",
                    "Jalsah (Sitting between two Prostrations):\n\n" +
                            "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and rise to sit up calmly from the first prostration."
            ));

            // 8. Second Sujud
            list.add(new StepItem(
                    "8. Sujud (Second Prostration)",
                    "Say \"Allahu Akbar\" and perform the second prostration, reciting \"Subhana Rabbiyal A'la\" (3 times)...",
                    "Sujud (Second Prostration):\n\n" +
                            "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ), perform the second prostration, and recite \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times."
            ));

            // 9. Second Rak'ah
            list.add(new StepItem(
                    "9. Second Rak'ah",
                    "Stand up for the second Rak'ah and perform it similarly to the first Rak'ah...",
                    "Second Rak'ah:\n\n" +
                            "Stand up for the second Rak'ah and complete it in the same manner as the first Rak'ah."
            ));

            // 10. Tashahhud
            list.add(new StepItem(
                    "10. Tashahhud (At-Tahiyyat)",
                    "At the end of the second Rak'ah, sit and recite Tashahhud: At-Tahiyyatu lillahi...",
                    "Tashahhud (At-Tahiyyat):\n\n" +
                            "At the conclusion of the second Rak'ah, recite Tashahhud:\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "At-Tahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-nabiyyu wa rahmatullahi wa barakatuhu, as-salamu 'alayna wa 'ala 'ibadillahis-salihin, ashhadu alla ilaha illallahu wa ashhadu anna Muhammadan 'abduhu wa rasuluh.\n\n\n" +
                            "Meaning:\n\n" +
                            "All compliments, prayers, and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I testify that there is no deity worthy of worship except Allah, and I testify that Muhammad is His servant and Messenger."
            ));

            // 11. Durood Sharif
            list.add(new StepItem(
                    "11. Durood Sharif",
                    "Recite Durood Ibrahim: Allahumma Salli 'ala Muhammad...",
                    "Durood Sharif:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma Salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hamidum Majeed.\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah, send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Ibrahim and upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious."
            ));

            // 12. Supplication (Dua)
            list.add(new StepItem(
                    "12. Supplication (Dua)",
                    "A Sunnah supplication: Allahumma inni zalamtu nafsi zulman katheera...",
                    "Supplication (Dua):\n\n" +
                            "A Sunnah supplication after Tashahhud and Durood:\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma inni zalamtu nafsi zulman katheeran wa la yaghfiruz-zunuba illa anta, faghfir li maghfiratan min 'indika warhamni, innaka antal Ghafurur-Raheem.\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah, I have wronged myself greatly, and none forgives sins except You. So grant me forgiveness from You and have mercy upon me. Indeed, You are the Forgiving, the Merciful."
            ));

            // 13. Tasleem (Concluding the Prayer)
            list.add(new StepItem(
                    "13. Tasleem (Concluding the Prayer)",
                    "Conclude the prayer by turning the head to both sides saying \"Assalamu 'Alaykum wa Rahmatullah\"...",
                    "Tasleem (Concluding the Prayer):\n\n" +
                            "At the conclusion of the prayer, turn your face to the right and then to the left saying:\n\n\n" +
                            "السلام عليكم ورحمة الله\n\n\n" +
                            "Transliteration:\n\n" +
                            "Assalamu 'Alaykum wa Rahmatullah\n\n\n" +
                            "Meaning:\n\n" +
                            "May the peace and mercy of Allah be upon you."
            ));
        }

        return list;
    }
}
