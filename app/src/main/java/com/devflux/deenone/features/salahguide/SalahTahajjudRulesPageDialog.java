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

public class SalahTahajjudRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "তাহাজ্জুত নামাজের ফজিলত" : "Virtues & Method of Tahajjud Prayer");

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
            // তাহাজ্জুত নামাজের গুরুত্ব ও ফজিলত
            list.add(new StepItem(
                    "তাহাজ্জুত নামাজের গুরুত্ব ও ফজিলত",
                    "তাহাজ্জুত নামাজ হলো রাতের বিশেষ নামাজ যা অত্যন্ত ফজিলতপূর্ণ। এটি ইসলামের অন্যতম গুরুত্বপূর্ণ নফল ইবাদত... [সূরা আল-ইসরাআ, ১৭:৭৯], [তিরমিজি, ৩৫৪৯]",
                    "তাহাজ্জুত নামাজ হলো রাতের বিশেষ নামাজ যা অত্যন্ত ফজিলতপূর্ণ। এটি ইসলামের অন্যতম গুরুত্বপূর্ণ নফল ইবাদত। তাহাজ্জুত নামাজের জন্য মুসলমানদের রাতের শেষ ভাগে ঘুম থেকে উঠা এবং আল্লাহর সাথে সম্পর্ক জোরদার করার চেষ্টা করা হয়।\n\n\n" +
                            "তাহাজ্জুত নামাজের গুরুত্ব ও ফজিলত-\n\n\n" +
                            "কুরআনের আলোকে\n\n" +
                            "আল্লাহ তাআলা বলেন:\n\n\n" +
                            "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَّكَ عَسَى أَن يَبْعَثَكَ رَبُّكَ مَقَامًا مَّحْمُودًا\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ওয়ামিনাল লাইলি ফাতাহাজ্জাদ বিহি নাফিলাতাল্লাকা আ'সা আয়্যাবআ'সাকা রাব্বুকা মাক্বামাম মাহমুদা\n\n\n" +
                            "অর্থ:\n\n" +
                            "\"আর রাতের একাংশে তাহাজ্জুদ পড়, এটি তোমার জন্য অতিরিক্ত (নফল)। হয়তো তোমার প্রভু তোমাকে মহিমান্বিত স্থানে উঠাবেন।\"\n\n" +
                            "[সূরা আল-ইসরাআ, ১৭:৭৯]\n\n\n" +
                            "হাদিসের আলোকে\n\n" +
                            "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেন\n\n\n" +
                            "عَلَيْكُمْ بِقِيَامِ اللَّيْلِ فَإِنَّهُ دَأْبُ الصَّالِحِينَ قَبْلَكُمْ، وَقُرْبَةٌ إِلَى اللَّهِ تَعَالَى، وَمَنْهَاةٌ عَنِ الإِثْمِ، وَتَكْفِيرٌ لِلسَّيِّئَاتِ، وَمَطْرَدَةٌ لِلدَّاءِ عَنِ الْجَسَدِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আলাইকুম্ বিকিয়ামিল্ লাইলি ফা-ইন্নাহু দা'বুস্ সালিহীনা কাবলাকুম্ ওয়া কুরবাতুন ইলাল্লাহি তা'আলা ওয়া মিনহাতুন আনিল্ ইথমি ওয়া তাকফিরুন্ লিস্সাইয়িয়াতি ওয়া মাতরাদাতুন লিদ্দাআই 'আনিল্ জাসাদ।\n\n\n" +
                            "অর্থ:\n\n" +
                            "তোমরা রাতের নামাজ পড়ো, কেননা এটি তোমাদের পূর্ববর্তী সৎকর্মশীলদের কাজ ছিল, এটি আল্লাহর নৈকট্য লাভের উপায়, পাপ থেকে বিরত রাখে, পাপ মুছে দেয় এবং দেহের রোগ-ব্যাধি দূর করে।\n\n" +
                            "[তিরমিজি, ৩৫৪৯]"
            ));

            // ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "তাহাজ্জুত নামাজ পড়ার জন্য রাতে ঘুম থেকে জেগে উঠা এবং নামাজ পড়ার নিয়ত করা।",
                    "নিয়ত (ইচ্ছা):\n\n" +
                            "তাহাজ্জুত নামাজ পড়ার জন্য রাতে ঘুম থেকে জেগে উঠা এবং নামাজ পড়ার নিয়ত করা।"
            ));

            // ২. ওযু করা
            list.add(new StepItem(
                    "২. ওযু করা",
                    "নামাজ শুরু করার আগে পবিত্রতার জন্য ওযু করা সুন্নত।",
                    "ওযু করা:\n\n" +
                            "নামাজ শুরু করার আগে পবিত্রতার জন্য ওযু করা সুন্নত।"
            ));

            // ৩. তাকবির (আল্লাহু আকবার বলা)
            list.add(new StepItem(
                    "৩. তাকবির (আল্লাহু আকবার বলা)",
                    "নামাজ শুরু করতে প্রথমে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে হাত বেঁধে দাঁড়ান।",
                    "তাকবির (আল্লাহু আকবার বলা):\n\n" +
                            "নামাজ শুরু করতে প্রথমে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে হাত বেঁধে দাঁড়ান।"
            ));

            // ৪. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
            list.add(new StepItem(
                    "৪. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা",
                    "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়: الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ...",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়।\n\n\n" +
                            "সুরা ফাতিহা:\n\n" +
                            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ\n\n" +
                            "আল্হাম্দু লিল্লা-হি রাব্বিল্ আ-লা-মী-ন\n\n" +
                            "\"সমস্ত প্রশংসা আল্লাহর জন্য, যিনি বিশ্বজগতের পালনকর্তা।\"\n\n\n" +
                            "الرَّحْمَٰنِ الرَّحِيمِ\n\n" +
                            "আর্রাহ্মা-নির্ রাহীম\n\n" +
                            "\"অতিশয় দয়ালু, পরম করুণাময়।\"\n\n\n" +
                            "مَالِكِ يَوْمِ الدِّينِ\n\n" +
                            "মা-লিকি ইয়াও-মিদ্দি-ন\n\n" +
                            "\"প্রতিফল দিনের মালিক।\"\n\n\n" +
                            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ\n\n" +
                            "ইয়্যা-কান্ নাআ-বুদু ওয়া ইয়্যা-কান্ নাস্তাঈ-ন\n\n" +
                            "\"আমরা শুধু তুমিই উপাসনা করি এবং শুধু তোমারই সাহায্য চাই।\"\n\n\n" +
                            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ\n\n" +
                            "ইহ্দিনা-স্ সিরা-তাল্ মু-স্তাকীম\n\n" +
                            "\"আমাদেরকে সরল পথ দেখাও,\"\n\n\n" +
                            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ\n\n" +
                            "সিরা-তাল্ লাযী-না আন্আ-ম্তা আ-লাইহিম গাইরিল্ মাগ্দু-বি আ-লাইহিম ওয়া লাদ্দ্বা-ল্লীন\n\n" +
                            "\"তাদের পথ যাদের প্রতি তুমি অনুগ্রহ করেছ, যারা অভিশপ্ত নয় এবং যারা পথভ্রষ্ট নয়।\""
            ));

            // ৫. সূরা পড়া
            list.add(new StepItem(
                    "৫. সূরা পড়া",
                    "ফাতিহার পরে কুরআনের কোনো একটি সূরা পড়ুন। উদাহরণস্বরূপ, সূরা ইখলাস: قُلْ هُوَ اللَّهُ أَحَدٌ...",
                    "সূরা পড়া:\n\n" +
                            "ফাতিহার পরে কুরআনের কোনো একটি সূরা পড়ুন। উদাহরণস্বরূপ, সূরা ইখলাস:\n\n\n" +
                            "قُلْ هُوَ اللَّهُ أَحَدٌ\n\n" +
                            "কুল হুওয়াল্লাহু আহাদ\n\n" +
                            "\"বল, তিনিই আল্লাহ্ এক।\"\n\n\n" +
                            "اللَّهُ الصَّمَدُ\n\n" +
                            "আল্লাহুস্ সামাদ\n\n" +
                            "\"আল্লাহ অমুখাপেক্ষী।\"\n\n\n" +
                            "لَمْ يَلِدْ وَلَمْ يُولَدْ\n\n" +
                            "লাম্ ইয়ালিদ্ ওয়া লাম্ ইউ-লাদ\n\n" +
                            "\"তিনি কাউকে জন্ম দেননি এবং কেউ তাকে জন্ম দেয়নি।\"\n\n\n" +
                            "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ\n\n" +
                            "ওয়া লাম্ ইয়াকুল্ লাহু কুফুয়ান্ আহাদ\n\n" +
                            "\"আর তার সমতুল্য কেউ নেই।\""
            ));

            // ৬. রুকু (ঝুঁকে থাকা)
            list.add(new StepItem(
                    "৬. রুকু (ঝুঁকে থাকা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আযীম\"...",
                    "রুকু (ঝুঁকে থাকা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে রুকুতে ঝুঁকে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আযীম\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) বলতে হয়, যার মানে \"আমার মহান প্রভু পবিত্র।\""
            ));

            // ৭. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৭. কিয়াম (দাঁড়ানো)",
                    "রুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" এবং \"রাব্বানা লাকা-ল হাম্দ\" বলতে হয়...",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "রুকু থেকে উঠার সময় \"সামিয়াল্লাহু লিমান হামিদা\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ) বলতে হয়, যার মানে \"আল্লাহ তাদের কথা শুনেন যারা তাকে প্রশংসা করে।\" এরপর \"রাব্বানা লাকা-ল হাম্দ\" (رَبَّنَا لَكَ الْحَمْدُ) বলতে হয়, যার মানে \"হে আমাদের প্রভু, সমস্ত প্রশংসা তোমার।\""
            ));

            // ৮. সিজদা (প্রথম সেজদা)
            list.add(new StepItem(
                    "৮. সিজদা (প্রথম সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে মাটিতে সিজদা করতে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আ'লা\"...",
                    "সিজদা (প্রথম সেজদা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে মাটিতে সিজদা করতে যান। এখানে তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলতে হয়, যার মানে \"আমার মহান প্রভু পবিত্র।\""
            ));

            // ৯. জলসা (দুই সেজদার মধ্যে বসা)
            list.add(new StepItem(
                    "৯. জলসা (দুই সেজদার মধ্যে বসা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন।",
                    "জলসা (দুই সেজদার মধ্যে বসা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে সেজদা থেকে উঠে বসেন।"
            ));

            // ১০. সিজদা (দ্বিতীয় সেজদা)
            list.add(new StepItem(
                    "১০. সিজদা (দ্বিতীয় সেজদা)",
                    "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" বলুন।",
                    "সিজদা (দ্বিতীয় সেজদা):\n\n" +
                            "\"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে আবার সেজদা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।"
            ));

            // ১১. দ্বিতীয় রাকাত
            list.add(new StepItem(
                    "১১. দ্বিতীয় রাকাত",
                    "দ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন।",
                    "দ্বিতীয় রাকাত:\n\n" +
                            "দ্বিতীয় রাকাতের জন্য দাঁড়ান এবং প্রথম রাকাতের মতই সম্পন্ন করুন।"
            ));

            // ১২. তাশাহহুদ (আত্তাহিয়্যাতু)
            list.add(new StepItem(
                    "১২. তাশাহহুদ (আত্তাহিয়্যাতু)",
                    "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন: التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ...",
                    "তাশাহহুদ (আত্তাহিয়্যাতু):\n\n" +
                            "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন:\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত্-তাইয়িবাতু, আস-সালামু আলাইকা আইয়্যুহান-নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আস-সালামু আলাইনা ওয়া আ’লা ইবাদিল্লাহিস-সালিহীন, আশহাদু আল-লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান ‘আবদুহু ওয়া রাসূলুহু\n\n\n" +
                            "অর্থ:\n\n" +
                            "সমস্ত সম্মান, সমস্ত দোয়া এবং সমস্ত পবিত্রতা আল্লাহর জন্য। হে নবী, আপনার প্রতি সালাম, আল্লাহর রহমত এবং বরকত। আমাদের এবং আল্লাহর নেক বান্দাদের প্রতি সালাম। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া আর কোন ইলাহ নেই এবং মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) আল্লাহর বান্দা এবং রাসূল।"
            ));

            // ১৩. দরুদ শরীফ
            list.add(new StepItem(
                    "১৩. দরুদ শরীফ",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ... (হে আল্লাহ! মুহাম্মাদ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম এবং তার পরিবারবর্গের উপর দোয়া করো...)",
                    "দরুদ শরীফ:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা সাল্লি আ’লা মুহাম্মাদিন ওয়া আ’লা আ-লি মুহাম্মাদিন কামা সাল্লাইতা আ’লা ইবরাহিমা ওয়া আ-লি ইবরাহিমা ইন্নাকা হামিদুম মজিদ\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং তার পরিবারবর্গের উপর দোয়া করো যেমন তুমি ইবরাহিম (আলাইহিস সালাম) এবং তার পরিবারবর্গের উপর দোয়া করেছো। তুমি প্রশংসিত ও মহিমান্বিত।"
            ));

            // ১৪. দু'আ
            list.add(new StepItem(
                    "১৪. দু'আ",
                    "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো: اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي...",
                    "দু'আ:\n\n" +
                            "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো:\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা ইন্নি যালামতু নাফসি যুলমান কাসিরান ওয়া লা ইয়াগফিরুজ্ যুনুবা ইল্লা আন্ত, ফাগফিরলি মাগফিরাতাম্ মিন্ আ'িন্দিকা ওয়ারহামনি ইন্নাকা আন্তাল্ গাফুরুর্ রহিম\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! আমি আমার নিজের প্রতি অনেক অত্যাচার করেছি এবং তুমি ছাড়া কেউ গুনাহ ক্ষমা করতে পারে না। তুমি তোমার পক্ষ থেকে আমাকে ক্ষমা করো এবং আমার প্রতি দয়া করো। নিশ্চয়ই তুমি মহা ক্ষমাশীল, পরম দয়ালু।"
            ));

            // ১৫. সালাম (নামাজ সমাপ্ত করা)
            list.add(new StepItem(
                    "১৫. সালাম (নামাজ সমাপ্ত করা)",
                    "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়: \"আস-সালামু আলাইকুম ওয়া রহমাতুল্লাহ\" (السلام عليكم ورحمة الله)...",
                    "সালাম (নামাজ সমাপ্ত করা):\n\n" +
                            "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়: \"আস-সালামু আলাইকুম ওয়া রহমাতুল্লাহ\" (السلام عليكم ورحمة الله) - \"আপনার প্রতি শান্তি ও আল্লাহর করুণা বর্ষিত হোক।\""
            ));
        } else {
            // English Mode
            // Virtues of Tahajjud Prayer
            list.add(new StepItem(
                    "Virtues & Importance of Tahajjud Prayer",
                    "Tahajjud prayer is a special night prayer of immense virtue. It is one of the most important voluntary acts of worship in Islam. [Surah Al-Isra, 17:79], [Jami` at-Tirmidhi, 3549]",
                    "Tahajjud prayer is a special night prayer of immense virtue. It is one of the most important voluntary acts of worship in Islam. Muslims wake up in the last part of the night to perform Tahajjud and strengthen their bond with Allah.\n\n\n" +
                            "Importance and Virtues of Tahajjud Prayer-\n\n\n" +
                            "In Light of the Quran\n\n" +
                            "Allah Almighty states:\n\n\n" +
                            "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَّكَ عَسَى أَن يَبْعَثَكَ رَبُّكَ مَقَامًا مَّحْمُودًا\n\n\n" +
                            "Transliteration:\n\n" +
                            "Wa minal-layli fatahajjad bihi naafilatan laka 'asaa an yab'athaka Rabbuka Maqaamam Mahmooda\n\n\n" +
                            "Meaning:\n\n" +
                            "\"And from [part of] the night, pray with it as additional [worship] for you; it is expected that your Lord will resurrect you to a praised station.\"\n\n" +
                            "[Surah Al-Isra, 17:79]\n\n\n" +
                            "In Light of the Hadith\n\n" +
                            "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "عَلَيْكُمْ بِقِيَامِ اللَّيْلِ فَإِنَّهُ دَأْبُ الصَّالِحِينَ قَبْلَكُمْ، وَقُرْبَةٌ إِلَى اللَّهِ تَعَالَى، وَمَنْهَاةٌ عَنِ الإِثْمِ، وَتَكْفِيرٌ لِلسَّيِّئَاتِ، وَمَطْرَدَةٌ لِلدَّاءِ عَنِ الْجَسَدِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "'Alaykum bi qiyaamil-layli fa-innahu da'bus-saaliheena qablakum, wa qurbatun ilallahi Ta'aala, wa manhaatun 'anil-ithmi, wa takfeerun lis-sayyi'aati, wa matradatun lid-daa'i 'anil-jasad.\n\n\n" +
                            "Meaning:\n\n" +
                            "Hold fast to the night prayer, for it was the habit of the righteous before you, a means of drawing close to Allah, a preventative from sin, an expiation for misdeeds, and a repelling of disease from the body.\n\n" +
                            "[Jami` at-Tirmidhi, 3549]"
            ));

            // 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Waking up from sleep at night to pray Tahajjud and intending in the heart.",
                    "Niyyah (Intention):\n\n" +
                            "Waking up from sleep during the night to perform Tahajjud prayer and making an intention in the heart to pray for the sake of Allah."
            ));

            // 2. Performing Wudu
            list.add(new StepItem(
                    "2. Performing Wudu",
                    "It is Sunnah to perform a thorough ablution (Wudu) for purity before starting prayer.",
                    "Performing Wudu:\n\n" +
                            "It is Sunnah to perform a thorough ablution (Wudu) for ritual purity before beginning the prayer."
            ));

            // 3. Takbir (Saying Allahu Akbar)
            list.add(new StepItem(
                    "3. Takbir (Saying Allahu Akbar)",
                    "Begin the prayer by saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and folding your hands.",
                    "Takbir (Saying Allahu Akbar):\n\n" +
                            "Begin the prayer by saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and fold your hands upon your chest/navel."
            ));

            // 4. Qiyam (Standing) & Surah Al-Fatihah
            list.add(new StepItem(
                    "4. Qiyam (Standing) & Surah Al-Fatihah",
                    "Stand at the beginning of prayer and recite Surah Al-Fatihah: الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ...",
                    "Qiyam (Standing):\n\n" +
                            "Stand at the beginning of prayer and recite Surah Al-Fatihah and another Surah from the Quran.\n\n\n" +
                            "Surah Al-Fatihah:\n\n" +
                            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ\n\n" +
                            "Alhamdu lillahi Rabbil-'Aalameen\n\n" +
                            "\"All praise is due to Allah, Lord of the worlds.\"\n\n\n" +
                            "الرَّحْمَٰنِ الرَّحِيمِ\n\n" +
                            "Ar-Rahmanir-Raheem\n\n" +
                            "\"The Entirely Merciful, the Especially Merciful.\"\n\n\n" +
                            "مَالِكِ يَوْمِ الدِّينِ\n\n" +
                            "Maliki Yawmid-Deen\n\n" +
                            "\"Sovereign of the Day of Recompense.\"\n\n\n" +
                            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ\n\n" +
                            "Iyyaka na'budu wa iyyaka nasta'een\n\n" +
                            "\"It is You we worship and You we ask for help.\"\n\n\n" +
                            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ\n\n" +
                            "Ihdinas-Siratal-Mustaqeem\n\n" +
                            "\"Guide us to the straight path,\"\n\n\n" +
                            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ\n\n" +
                            "Siratal-lazeena an'amta 'alayhim ghayril-maghdoobi 'alayhim wa lad-daalleen\n\n" +
                            "\"The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.\""
            ));

            // 5. Reciting a Surah
            list.add(new StepItem(
                    "5. Reciting a Surah",
                    "Recite a Surah after Al-Fatihah. For example, Surah Al-Ikhlas: قُلْ هُوَ اللَّهُ أَحَدٌ...",
                    "Reciting a Surah:\n\n" +
                            "Recite any Surah from the Quran after Al-Fatihah. For example, Surah Al-Ikhlas:\n\n\n" +
                            "قُلْ هُوَ اللَّهُ أَحَدٌ\n\n" +
                            "Qul huwal-lahu ahad\n\n" +
                            "\"Say, He is Allah, [who is] One.\"\n\n\n" +
                            "اللَّهُ الصَّمَدُ\n\n" +
                            "Allahus-Samad\n\n" +
                            "\"Allah, the Eternal Refuge.\"\n\n\n" +
                            "لَمْ يَلِدْ وَلَمْ يُولَدْ\n\n" +
                            "Lam yalid wa lam yoolad\n\n" +
                            "\"He neither begets nor is born,\"\n\n\n" +
                            "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ\n\n" +
                            "Wa lam yakul-lahu kufuwan ahad\n\n" +
                            "\"Nor is there to Him any equivalent.\""
            ));

            // 6. Ruku (Bowing)
            list.add(new StepItem(
                    "6. Ruku (Bowing)",
                    "Bow down saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and say \"Subhana Rabbiyal 'Azeem\" three times.",
                    "Ruku (Bowing):\n\n" +
                            "Bow down saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ). Here say \"Subhana Rabbiyal 'Azeem\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) three times, meaning \"Glory be to my Lord the Most Great.\""
            ));

            // 7. Qiyam (Rising from Ruku)
            list.add(new StepItem(
                    "7. Qiyam (Rising from Ruku)",
                    "Rise saying \"Sami' Allahu liman hamidah\" and \"Rabbana lakal-hamd\"...",
                    "Qiyam (Rising from Ruku):\n\n" +
                            "Rise from Ruku saying \"Sami' Allahu liman hamidah\" (سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ), meaning \"Allah hears whoever praises Him.\" Then say \"Rabbana lakal-hamd\" (رَبَّنَا لَكَ الْحَمْدُ), meaning \"Our Lord, to You belongs all praise.\""
            ));

            // 8. Sujud (First Prostration)
            list.add(new StepItem(
                    "8. Sujud (First Prostration)",
                    "Prostrate saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and say \"Subhana Rabbiyal A'la\" three times.",
                    "Sujud (First Prostration):\n\n" +
                            "Prostrate saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ). Here say \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times, meaning \"Glory be to my Lord the Most High.\""
            ));

            // 9. Jalsah (Sitting between Two Prostrations)
            list.add(new StepItem(
                    "9. Jalsah (Sitting between Two Prostrations)",
                    "Sit up from prostration saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ).",
                    "Jalsah (Sitting between Two Prostrations):\n\n" +
                            "Sit up from prostration saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ)."
            ));

            // 10. Sujud (Second Prostration)
            list.add(new StepItem(
                    "10. Sujud (Second Prostration)",
                    "Prostrate again saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and say \"Subhana Rabbiyal A'la\" three times.",
                    "Sujud (Second Prostration):\n\n" +
                            "Prostrate again saying \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and say \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times."
            ));

            // 11. Second Rak'ah
            list.add(new StepItem(
                    "11. Second Rak'ah",
                    "Stand up for the second rak'ah and complete it just like the first rak'ah.",
                    "Second Rak'ah:\n\n" +
                            "Stand up for the second rak'ah and complete it just like the first rak'ah."
            ));

            // 12. Tashahhud (Attahiyyatu)
            list.add(new StepItem(
                    "12. Tashahhud (Attahiyyatu)",
                    "Recite Tashahhud at the end of the second rak'ah: التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ...",
                    "Tashahhud (Attahiyyatu):\n\n" +
                            "Recite Tashahhud at the end of the second rak'ah:\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Attahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-nabiyyu wa rahmatullahi wa barakatuhu, as-salamu 'alayna wa 'ala 'ibadillahis-saliheen, ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluhu\n\n\n" +
                            "Meaning:\n\n" +
                            "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no god but Allah, and I bear witness that Muhammad is His slave and Messenger."
            ));

            // 13. Durood Sharif
            list.add(new StepItem(
                    "13. Durood Sharif",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ... (O Allah, send blessings upon Muhammad...)",
                    "Durood Sharif:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibraheema wa 'ala aali Ibraheema innaka Hameedum Majeed\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah! Send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Abraham and upon the family of Abraham. Indeed, You are Praiseworthy and Glorious."
            ));

            // 14. Du'a
            list.add(new StepItem(
                    "14. Du'a",
                    "Any supplication can be made after Tashahhud. A Sunnah supplication is: اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي...",
                    "Du'a:\n\n" +
                            "Any supplication can be made after Tashahhud. A Sunnah supplication is:\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma innee zalamtu nafsee zulman katheeran wa la yaghfiruz-zunuba illa Anta, faghfir lee maghfiratan min 'indika warhamnee innaka Antal-Ghafoorur-Raheem\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah! I have wronged myself greatly, and none forgives sins except You. So grant me forgiveness from You and have mercy on me. Indeed, You are the Forgiving, the Merciful."
            ));

            // 15. Salam (Concluding the Prayer)
            list.add(new StepItem(
                    "15. Salam (Concluding the Prayer)",
                    "Turn greetings to both sides saying \"As-Salamu 'Alaykum wa Rahmatullah\" (السلام عليكم ورحمة الله)...",
                    "Salam (Concluding the Prayer):\n\n" +
                            "Turn greetings to both sides at the end of prayer: \"As-Salamu 'Alaykum wa Rahmatullah\" (السلام عليكم ورحمة الله) - \"Peace and the mercy of Allah be upon you.\""
            ));
        }

        return list;
    }
}
