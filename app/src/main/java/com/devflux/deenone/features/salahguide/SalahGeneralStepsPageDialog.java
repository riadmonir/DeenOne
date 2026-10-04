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

public class SalahGeneralStepsPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "নামাজের ধাপসমূহ" : "Step-by-Step Stages of Salah");

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
            // 1. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "নামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ, \"আমি ফজরের দুই রাকাত ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\"",
                    "নিয়ত (ইচ্ছা):\n\nনামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ, \"আমি ফজরের দুই রাকাত ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\""
            ));

            // 2. তাকবির (আল্লাহু আকবার বলা)
            list.add(new StepItem(
                    "২. তাকবির (আল্লাহু আকবার বলা)",
                    "নামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়...",
                    "তাকবির (আল্লাহু আকবার বলা):\n\nনামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়। এ সময় হাত কানের লতি পর্যন্ত তুলে কান্ধের পাশে রেখে ছেড়ে দিতে হয়।"
            ));

            // 3. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
            list.add(new StepItem(
                    "৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা",
                    "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়...",
                    "কিয়াম (দাঁড়ানো):\n\nনামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়।\n\nসুরা ফাতিহা:\n\nالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ\n\nউচ্চারণ:\nআল্হাম্দু লিল্লা-হি রাব্বিল্ আ-লা-মী-ন\n\nঅর্থ:\n\"সমস্ত প্রশংসা আল্লাহর জন্য, যিনি বিশ্বজগতের পালনকর্তা।\"\n\nالرَّحْمَٰنِ الرَّحِيمِ\n\nউচ্চারণ:\nআর্রাহ্মা-নির্ রাহীম\n\nঅর্থ:\n\"অতিশয় দয়ালু, পরম করুণাময়।\"\n\nمَالِكِ يَوْمِ الدِّينِ\n\nউচ্চারণ:\nমা-লিকি ইয়াও-মিদ্দি-ন\n\nঅর্থ:\n\"প্রতিফল দিনের মালিক।\"\n\nإِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ\n\nউচ্চারণ:\nইয়্যা-কান্ নাআ-বুদু ওয়া ইয়্যা-কান্ নাস্তাঈ-ন\n\nঅর্থ:\n\"আমরা শুধু তুমিই উপাসনা করি এবং শুধু তোমারই সাহায্য চাই।\"\n\nاهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ\n\nউচ্চারণ:\nইহ্দিনা-স্ সিরা-তাল্ মু-স্তাকীম\n\nঅর্থ:\n\"আমাদেরকে সরল পথ দেখাও,\"\n\nصِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ\n\nউচ্চারণ:\nসিরা-তাল্ লাযী-না আন্আ-ম্তা আ-লাইহিম গাইরিল্ মাগ্দু-বি আ-লাইহিম ওয়া লাদ্দ্বা-ল্লীন\n\nঅর্থ:\n\"তাদের পথ যাদের প্রতি তুমি অনুগ্রহ করেছ, যারা অভিশপ্ত নয় এবং যারা পথভ্রষ্ট নয়।\""
            ));

            // 4. সূরা পড়া
            list.add(new StepItem(
                    "৪. সূরা পড়া",
                    "ফাতিহার পরে কুরআনের কোনো একটি সূরা পড়ুন। উদাহরণস্বরূপ, সূরা ইখলাস...",
                    "সূরা পড়া:\n\nফাতিহার পরে কুরআনের কোনো একটি সূরা পড়ুন। উদাহরণস্বরূপ, সূরা ইখলাস:\n\nقُلْ هُوَ اللَّهُ أَحَدٌ\n\nউচ্চারণ:\nকুল হুওয়াল্লাহু আহাদ\n\nঅর্থ:\n\"বল, তিনিই আল্লাহ্ এক।\"\n\nاللَّهُ الصَّمَدُ\n\nউচ্চারণ:\nআল্লাহুস্ সামাদ\n\nঅর্থ:\n\"আল্লাহ অমুখাপেক্ষী।\"\n\nلَمْ יَلِدْ وَلَمْ يُولَدْ\n\nউচ্চারণ:\nলাম্ ইয়ালিদ্ ওয়া লাম্ ইউ-লাদ\n\nঅর্থ:\n\"তিনি কাউকে জন্ম দেননি এবং কেউ তাকে জন্ম দেয়নি।\"\n\nوَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ\n\nউচ্চারণ:\nওয়া লাম্ ইয়াকুল্ লাহু কুফুয়ান্ আহাদ\n\nঅর্থ:\n\"আর তার সমতুল্য কেউ নেই।\""
            ));

            // 5. রুকু (ঝুঁকে থাকা)
            list.add(new StepItem(
                    "৫. রুকু (ঝুঁকে থাকা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান...",
                    "রুকু (ঝুঁকে থাকা):\n\n\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আযীম\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) বলতে হয়, যার মানে \"আমার মহান প্রভু পবিত্র।\""
            ));

            // 6. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৬. কিয়াম (দাঁড়ানো)",
                    "রুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ) বলতে হয়...",
                    "কিয়াম (দাঁড়ানো):\n\nরুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ) বলতে হয়, যার মানে \"আল্লাহ তাদের কথা শুনেন যারা তাকে প্রশংসা করে।\" এরপর \"রাব্বানা লাকা-ল হাম্দ\" (رَبَّنَا لَكَ الْحَمْدُ) বলতে হয়, যার মানে \"হে আমাদের প্রভু, সমস্ত প্রশংসা তোমার।\""
            ));

            // 7. সিজদা (প্রথম সেজদা)
            list.add(new StepItem(
                    "৭. সিজদা (প্রথম সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে মাটিতে সিজদা করতে যান...",
                    "সিজদা (প্রথম সেজদা):\n\n\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে মাটিতে সিজদা করতে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলতে হয়, যার মানে \"আমার মহান প্রভু পবিত্র।\""
            ));

            // 8. জলসা (দুই সেজদার মধ্যে বসা)
            list.add(new StepItem(
                    "৮. জলসা (দুই সেজদার মধ্যে বসা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন...",
                    "জলসা (দুই সেজদার মধ্যে বসা):\n\n\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন।"
            ));

            // 9. সিজদা (দ্বিতীয় সেজদা)
            list.add(new StepItem(
                    "৯. সিজদা (দ্বিতীয় সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন...",
                    "সিজদা (দ্বিতীয় সেজদা):\n\n\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।"
            ));

            // 10. দ্বিতীয় রাকাত
            list.add(new StepItem(
                    "১০. দ্বিতীয় রাকাত",
                    "দ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন...",
                    "দ্বিতীয় রাকাত:\n\nদ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন।"
            ));

            // 11. তাশাহহুদ (আত্তাহিয়্যাতু)
            list.add(new StepItem(
                    "১১. তাশাহহুদ (আত্তাহিয়্যাতু)",
                    "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন...",
                    "তাশাহহুদ (আত্তাহিয়্যাতু):\n\nদ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন:\n\nالتَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nউচ্চারণ:\nআত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত্-তাইয়িবাতু, আস-সালামু আলাইকা আইয়্যুহান-নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আস-সালামু আলাইনা ওয়া আ’লা ইবাদিল্লাহিস-সালিহীন, আশহাদু আল-লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান ‘আবদুহু ওয়া রাসূলুহু\n\nঅর্থ:\nসমস্ত সম্মান, সমস্ত দোয়া এবং সমস্ত পবিত্রতা আল্লাহর জন্য। হে নবী, আপনার প্রতি সালাম, আল্লাহর রহমত এবং বরকত। আমাদের এবং আল্লাহর নেক বান্দাদের প্রতি সালাম। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া আর কোন ইলাহ নেই এবং মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) আল্লাহর বান্দা এবং রাসূল।"
            ));

            // 12. দরুদ শরীফ
            list.add(new StepItem(
                    "১২. দরুদ শরীফ",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ...",
                    "দরুদ শরীফ:\n\nاللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\nউচ্চারণ:\nআল্লাহুম্মা সাল্লি আ’লা মুহাম্মাদিন ওয়া আ’লা আ-লি মুহাম্মাদিন কামা সাল্লাইতা আ’লা ইবরাহিমা ওয়া আ-লি ইবরাহিমা ইন্নাকা হামিদুম মজিদ\n\nঅর্থ:\nহে আল্লাহ! মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং তার পরিবারবর্গের উপর দোয়া করো যেমন তুমি ইবরাহিম (আলাইহিস সালাম) এবং তার পরিবারবর্গের উপর দোয়া করেছো। তুমি প্রশংসিত ও মহিমান্বিত।"
            ));

            // 13. দু'আ
            list.add(new StepItem(
                    "১৩. দু'আ",
                    "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো...",
                    "দু'আ:\n\nতাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো:\n\nاللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\nউচ্চারণ:\nআল্লাহুম্মা ইন্নি যালামতু নাফসি যুলমান কাসিরান ওয়া লা ইয়াগফিরুজ্ যুনুবা ইল্লা আন্ত, ফাগফিরলি মাগফিরাতাম্ মিন্ আ'িন্দিকা ওয়ারহামনি ইন্নাকা আন্তাল্ গাফুরুর্ রহিম\n\nঅর্থ:\nহে আল্লাহ! আমি আমার নিজের প্রতি অনেক অত্যাচার করেছি এবং তুমি ছাড়া কেউ গুনাহ ক্ষমা করতে পারে না। তুমি তোমার পক্ষ থেকে আমাকে ক্ষমা করো এবং আমার প্রতি দয়া করো। নিশ্চয়ই তুমি মহা ক্ষমাশীল, পরম দয়ালু।"
            ));

            // 14. সালাম (নামাজ সমাপ্ত করা)
            list.add(new StepItem(
                    "১৪. সালাম (নামাজ সমাপ্ত করা)",
                    "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়: আস-সালামু আলাইকুম ওয়া রহমাতুল্লাহ...",
                    "সালাম (নামাজ সমাপ্ত করা):\n\nনামাজের শেষে দুই দিকে সালাম ফিরাতে হয়:\n\nالسلام عليكم ورحمة الله\n\nউচ্চারণ:\nআস-সালামু আলাইকুম ওয়া রহমাতুল্লাহ\n\nঅর্থ:\nআপনার প্রতি শান্তি ও আল্লাহর করুণা বর্ষিত হোক।"
            ));

        } else {
            // English mode
            list.add(new StepItem(
                    "1. Intention (Niyyah)",
                    "Before starting prayer, intention must be made in the heart...",
                    "Intention (Niyyah):\n\nBefore starting prayer, intention must be made in the heart. For example, \"I intend to perform two Rakahs of Fard prayer of Fajr for the sake of Allah.\""
            ));

            list.add(new StepItem(
                    "2. Takbir (Saying Allahu Akbar)",
                    "To begin prayer, say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ)...",
                    "Takbir (Saying Allahu Akbar):\n\nTo begin prayer, say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ). At this time, raise hands up to the earlobes/shoulders and then fold hands."
            ));

            list.add(new StepItem(
                    "3. Qiyam (Standing) & Surah Al-Fatihah",
                    "At first standing in prayer, recite Surah Al-Fatihah and another Surah...",
                    "Qiyam (Standing):\n\nAt first standing in prayer, recite Surah Al-Fatihah and another Surah from the Quran.\n\nSurah Al-Fatihah:\n\nالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ\n\nTransliteration:\nAlhamdu lillahi rabbil 'alameen\n\nMeaning:\n\"All praise is due to Allah, Lord of all the worlds.\"\n\nالرَّحْمَٰنِ الرَّحِيمِ\n\nTransliteration:\nAr-Rahmanir-Raheem\n\nMeaning:\n\"The Entirely Merciful, the Especially Merciful.\"\n\nمَالِكِ يَوْمِ الدِّينِ\n\nTransliteration:\nMaliki yawmid-deen\n\nMeaning:\n\"Sovereign of the Day of Recompense.\"\n\nإِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ\n\nTransliteration:\nIyyaka na'budu wa iyyaka nasta'een\n\nMeaning:\n\"It is You we worship and You we ask for help.\"\n\nاهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ\n\nTransliteration:\nIhdinas-siratal-mustaqeem\n\nMeaning:\n\"Guide us to the straight path.\"\n\nصِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ\n\nTransliteration:\nSiratal-ladheena an'amta 'alayhim ghayril-maghdubi 'alayhim wa lad-daalleen\n\nMeaning:\n\"The path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.\""
            ));

            list.add(new StepItem(
                    "4. Reciting a Surah",
                    "After Fatihah, recite any Surah from Quran. For example, Surah Al-Ikhlas...",
                    "Reciting a Surah:\n\nAfter Fatihah, recite any Surah from the Quran. For example, Surah Al-Ikhlas:\n\nقُلْ هُوَ اللَّهُ أَحَدٌ\n\nTransliteration:\nQul huwallahu ahad\n\nMeaning:\n\"Say, He is Allah, [who is] One.\"\n\nاللَّهُ الصَّمَدُ\n\nTransliteration:\nAllahus-samad\n\nMeaning:\n\"Allah, the Eternal Refuge.\"\n\nلَمْ يَلِدْ وَلَمْ يُولَدْ\n\nTransliteration:\nLam yalid wa lam yoolad\n\nMeaning:\n\"He neither begets nor is born.\"\n\nوَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ\n\nTransliteration:\nWa lam yakul-lahu kufuwan ahad\n\nMeaning:\n\"Nor is there to Him any equivalent.\""
            ));

            list.add(new StepItem(
                    "5. Ruku (Bowing)",
                    "Say \"Allahu Akbar\" and bow down in Ruku...",
                    "Ruku (Bowing):\n\nSay \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and bow down into Ruku. Here say three times: \"Subhana Rabbiyal Azeem\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ), meaning \"Glory is to my Lord, the Magnificent.\""
            ));

            list.add(new StepItem(
                    "6. Qiyam (Standing Straight)",
                    "While rising from Ruku say \"Sami Allahu Liman Hamidah\"...",
                    "Qiyam (Standing Straight):\n\nWhile rising from Ruku say \"Sami Allahu Liman Hamidah\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ), meaning \"Allah hears those who praise Him.\" Then say \"Rabbana Lakal Hamd\" (رَبَّنَا لَكَ الْحَمْدُ), meaning \"Our Lord, to You belongs all praise.\""
            ));

            list.add(new StepItem(
                    "7. Sujud (First Prostration)",
                    "Say \"Allahu Akbar\" and prostrate on the ground...",
                    "Sujud (First Prostration):\n\nSay \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and prostrate on the ground. Here say three times: \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى), meaning \"Glory is to my Lord, the Most High.\""
            ));

            list.add(new StepItem(
                    "8. Jalsah (Sitting Between Prostrations)",
                    "Say \"Allahu Akbar\" and sit up from prostration...",
                    "Jalsah (Sitting Between Prostrations):\n\nSay \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and sit up calmly between the two prostrations."
            ));

            list.add(new StepItem(
                    "9. Sujud (Second Prostration)",
                    "Say \"Allahu Akbar\" and perform the second prostration...",
                    "Sujud (Second Prostration):\n\nSay \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and perform the second prostration, reciting \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times."
            ));

            list.add(new StepItem(
                    "10. Second Rakah",
                    "Stand up for the second Rakah and perform it like the first...",
                    "Second Rakah:\n\nStand up for the second Rakah and perform it like the first Rakah."
            ));

            list.add(new StepItem(
                    "11. Tashahhud (Attahiyyatu)",
                    "At the end of the second Rakah, sit and recite Tashahhud...",
                    "Tashahhud (Attahiyyatu):\n\nAt the end of the second Rakah, sit and recite Tashahhud:\n\nالتَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nTransliteration:\nAttahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-nabiyyu wa rahmatullahi wa barakatuh, as-salamu 'alayna wa 'ala 'ibadillahis-saliheen, ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluh\n\nMeaning:\nAll compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no god but Allah, and I bear witness that Muhammad is His servant and His Messenger."
            ));

            list.add(new StepItem(
                    "12. Durood Sharif",
                    "Recite Salawat on the Prophet (peace be upon him)...",
                    "Durood Sharif:\n\nاللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\nTransliteration:\nAllahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibrahima wa 'ala aali Ibrahima innaka Hameedum-Majeed\n\nMeaning:\nO Allah, bestow Your favor upon Muhammad and upon the family of Muhammad as You bestowed favor upon Ibrahim and upon the family of Ibrahim. Indeed, You are Praiseworthy and Glorious."
            ));

            list.add(new StepItem(
                    "13. Dua Masura",
                    "After Tashahhud and Durood, a Sunnah Dua can be recited...",
                    "Dua Masura:\n\nAfter Tashahhud and Durood, any Sunnah Dua can be recited:\n\nاللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\nTransliteration:\nAllahumma innee zalamtu nafsee zulman katheeran wa la yaghfiruz-zunuba illa anta faghfir lee maghfiratan min 'indika warhamnee innaka antal-Ghafoorur-Raheem\n\nMeaning:\nO Allah, I have wronged myself greatly and none forgives sins except You, so forgive me with a forgiveness from You and have mercy on me. Indeed, You are the Forgiving, the Merciful."
            ));

            list.add(new StepItem(
                    "14. Salam (Concluding the Prayer)",
                    "Conclude prayer by turning face right and left with Salam...",
                    "Salam (Concluding the Prayer):\n\nConclude prayer by turning face to both right and left sides saying:\n\nالسلام عليكم ورحمة الله\n\nTransliteration:\nAs-Salamu 'Alaykum wa Rahmatullah\n\nMeaning:\nPeace and mercy of Allah be upon you."
            ));
        }

        return list;
    }
}
