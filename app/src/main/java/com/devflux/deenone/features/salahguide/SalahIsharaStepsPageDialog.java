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

public class SalahIsharaStepsPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইশারায় সালাত পড়ার পদ্ধতি" : "Step-by-Step Gesture Prayer Guide");

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
                    "নামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ, \"আমি ফজরের দুই রাকাত ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\"",
                    "নিয়ত (ইচ্ছা):\n\n" +
                            "নামাজ শুরু করার আগে নিয়ত করতে হয়। উদাহরণস্বরূপ, \"আমি ফজরের দুই রাকাত ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।\""
            ));

            // ২. তাকবির (আল্লাহু আকবার বলা)
            list.add(new StepItem(
                    "২. তাকবির (আল্লাহু আকবার বলা)",
                    "নামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়। এ সময় মাথা বা চোখের ইশারা করতে হবে।",
                    "তাকবির (আল্লাহু আকবার বলা):\n\n" +
                            "নামাজ শুরু করতে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলতে হয়। এ সময় মাথা বা চোখের ইশারা করতে হবে।"
            ));

            // ৩. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৩. কিয়াম (দাঁড়ানো)",
                    "যদি দাঁড়াতে না পারেন তবে বসে পড়তে হবে। যদি বসেও না পারেন তবে শুয়ে পড়ুন।",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "যদি দাঁড়াতে না পারেন তবে বসে পড়তে হবে। যদি বসেও না পারেন তবে শুয়ে পড়ুন।"
            ));

            // ৪. সূরা ফাতিহা এবং অন্য সূরা পড়া
            list.add(new StepItem(
                    "৪. সূরা ফাতিহা এবং অন্য সূরা পড়া",
                    "যদি সম্ভব হয় তাহলে মুখে পড়ুন, না পারলে মনে মনে পড়ুন।",
                    "সূরা ফাতিহা এবং অন্য সূরা পড়া:\n\n" +
                            "যদি সম্ভব হয় তাহলে মুখে পড়ুন, না পারলে মনে মনে পড়ুন।"
            ));

            // ৫. রুকু (ঝুঁকে থাকা)
            list.add(new StepItem(
                    "৫. রুকু (ঝুঁকে থাকা)",
                    "রুকুর জন্য মাথা বা চোখের ইশারা করুন। রুকুর দোয়া হলো: سُبْحَانَ رَبِّيَ الْعَظِيمِ [মুসলিম- ৭৫২]",
                    "রুকু (ঝুঁকে থাকা):\n\n" +
                            "রুকুর জন্য মাথা বা চোখের ইশারা করুন। রুকুর দোয়া হলো:\n\n\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيمِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "সুবহানা রাব্বিয়াল আযীম\n\n\n" +
                            "অর্থ:\n\n" +
                            "আমার মহান প্রভু পবিত্র।\n\n" +
                            "[মুসলিম- ৭৫২]"
            ));

            // ৬. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৬. কিয়াম (দাঁড়ানো)",
                    "রুকু থেকে উঠার সময় মাথা বা চোখের ইশারা করুন এবং বলুন سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ...",
                    "কিয়াম (দাঁড়ানো):\n\n" +
                            "রুকু থেকে উঠার সময় মাথা বা চোখের ইশারা করুন এবং বলুন\n\n\n" +
                            "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "সামিয়াল্লাহু লিমান হামিদা\n\n\n" +
                            "অর্থ:\n\n" +
                            "আল্লাহ তাদের কথা শুনেন যারা তাকে প্রশংসা করে।\n\n\n" +
                            "رَبَّنَا لَكَ الْحَمْدُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "রাব্বানা লাকা-ল হাম্দ\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আমাদের প্রভু, সমস্ত প্রশংসা তোমার।"
            ));

            // ৭. সিজদা (প্রথম সেজদা)
            list.add(new StepItem(
                    "৭. সিজদা (প্রথম সেজদা)",
                    "সিজদার জন্য মাথা বা চোখের ইশারা করুন। সিজদার দোয়া হলো: سُبْحَانَ رَبِّيَ الْأَعْلَى [মুসলিম - ৭৫২]",
                    "সিজদা (প্রথম সেজদা):\n\n" +
                            "সিজদার জন্য মাথা বা চোখের ইশারা করুন। সিজদার দোয়া হলো\n\n\n" +
                            "سُبْحَانَ رَبِّيَ الْأَعْلَى\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "সুবহানা রাব্বিয়াল আ'লা\n\n\n" +
                            "অর্থ:\n\n" +
                            "আমার মহান প্রভু পবিত্র।\n\n" +
                            "[মুসলিম - ৭৫২]"
            ));

            // ৮. জলসা (দুই সেজদার মধ্যে বসা)
            list.add(new StepItem(
                    "৮. জলসা (দুই সেজদার মধ্যে বসা)",
                    "সিজদা থেকে উঠে বসার জন্য মাথা বা চোখের ইশারা করুন।",
                    "জলসা (দুই সেজদার মধ্যে বসা):\n\n" +
                            "সিজদা থেকে উঠে বসার জন্য মাথা বা চোখের ইশারা করুন।"
            ));

            // ৯. সিজদা (দ্বিতীয় সেজদা)
            list.add(new StepItem(
                    "৯. সিজদা (দ্বিতীয় সেজদা)",
                    "আবার সিজদার জন্য মাথা বা চোখের ইশারা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।",
                    "সিজদা (দ্বিতীয় সেজদা):\n\n" +
                            "আবার সিজদার জন্য মাথা বা চোখের ইশারা করুন এবং তিনবার \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) বলুন।"
            ));

            // ১০. দ্বিতীয় রাকাত
            list.add(new StepItem(
                    "১০. দ্বিতীয় রাকাত",
                    "দ্বিতীয় রাকাতের জন্য মাথা বা চোখের ইশারা করুন এবং প্রথম রাকাতের মতই সম্পন্ন করুন।",
                    "দ্বিতীয় রাকাত:\n\n" +
                            "দ্বিতীয় রাকাতের জন্য মাথা বা চোখের ইশারা করুন এবং প্রথম রাকাতের মতই সম্পন্ন করুন।"
            ));

            // ১১. তাশাহহুদ (আত্তাহিয়্যাতু)
            list.add(new StepItem(
                    "১১. তাশাহহুদ (আত্তাহিয়্যাতু)",
                    "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন - التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ...",
                    "তাশাহহুদ (আত্তাহিয়্যাতু):\n\n" +
                            "দ্বিতীয় রাকাতের শেষে তাশাহহুদ পড়ুন -\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত্-তাইয়িবাতু, আস-সালামু আলাইকা আইয়্যুহান-নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আস-সালামু আলাইনা ওয়া আ’লা ইবাদিল্লাহিস-সালিহীন, আশহাদু আল-লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান ‘আবদুহু ওয়া রাসূলুহু\n\n\n" +
                            "অর্থ:\n\n" +
                            "সমস্ত সম্মান, সমস্ত দোয়া এবং সমস্ত পবিত্রতা আল্লাহর জন্য। হে নবী, আপনার প্রতি সালাম, আল্লাহর রহমত এবং বরকত। আমাদের এবং আল্লাহর নেক বান্দাদের প্রতি সালাম। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া আর কোন ইলাহ নেই এবং মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) আল্লাহর বান্দা এবং রাসূল।"
            ));

            // ১২. দরুদ শরীফ
            list.add(new StepItem(
                    "১২. দরুদ শরীফ",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ... (হে আল্লাহ! মুহাম্মাদ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম এবং তার পরিবারবর্গের উপর দোয়া করো...)",
                    "দরুদ শরীফ:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা সাল্লি আ’লা মুহাম্মাদিন ওয়া আ’লা আ-লি মুহাম্মাদিন কামা সাল্লাইতা আ’লা ইবরাহিমা ওয়া আ-লি ইবরাহিমা ইন্নাকা হামিদুম মজিদ\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! মুহাম্মাদ (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) এবং তার পরিবারবর্গের উপর দোয়া করো যেমন তুমি ইবরাহিম (আলাইহিস সালাম) এবং তার পরিবারবর্গের উপর দোয়া করেছো। তুমি প্রশংসিত ও মহিমান্বিত।"
            ));

            // ১৩. দু'আ
            list.add(new StepItem(
                    "১৩. দু'আ",
                    "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো: اللَّهُمَّ إِنِّই ظَلَمْتُ نَفْسِي...",
                    "দু'আ:\n\n" +
                            "তাশাহহুদের পরে কোনো দু'আ করা যেতে পারে। একটি সুন্নত দু'আ হলো\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "আল্লাহুম্মা ইন্নি যালামতু নাফসি যুলমান কাসিরান ওয়া লা ইয়াগফিরুজ্ যুনুবা ইল্লা আন্ত, ফাগফিরলি মাগফিরাতাম্ মিন্ আ'িন্দিকা ওয়ারহামনি ইন্নাকা আন্তাল্ গাফুরুর্ রহিম\n\n\n" +
                            "অর্থ:\n\n" +
                            "হে আল্লাহ! আমি আমার নিজের প্রতি অনেক অত্যাচার করেছি এবং তুমি ছাড়া কেউ গুনাহ ক্ষমা করতে পারে না। তুমি তোমার পক্ষ থেকে আমাকে ক্ষমা করো এবং আমার প্রতি দয়া করো। নিশ্চয়ই তুমি মহা ক্ষমাশীল, পরম দয়ালু।"
            ));

            // ১৪. সালাম (নামাজ সমাপ্ত করা)
            list.add(new StepItem(
                    "১৪. সালাম (নামাজ সমাপ্ত করা)",
                    "নামাজের শেষে দুই দিকে সালাম ফিরাতে হয়: السلام عليكم ورحمة الله",
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
            // 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Before starting prayer, one must intend in the heart. For example, 'I intend to pray two rak'ahs of obligatory Fajr prayer for the sake of Allah.'",
                    "Niyyah (Intention):\n\n" +
                            "Before starting prayer, one must make an intention in the heart. For example, \"I intend to pray the two rak'ahs of obligatory Fajr prayer for the sake of Allah.\""
            ));

            // 2. Takbir (Saying Allahu Akbar)
            list.add(new StepItem(
                    "2. Takbir (Saying Allahu Akbar)",
                    "Say 'Allahu Akbar' (اللَّهُ أَكْبَرُ) to begin the prayer. At this time, gesture with your head or eyes.",
                    "Takbir (Saying Allahu Akbar):\n\n" +
                            "Say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) to begin the prayer. At this time, gesture with your head or eyes."
            ));

            // 3. Qiyam (Standing)
            list.add(new StepItem(
                    "3. Qiyam (Standing)",
                    "If you cannot stand, you must pray sitting. If you cannot pray sitting, pray lying down.",
                    "Qiyam (Standing):\n\n" +
                            "If you cannot stand, you must pray sitting. If you cannot pray sitting, pray lying down."
            ));

            // 4. Reciting Surah Al-Fatihah and Another Surah
            list.add(new StepItem(
                    "4. Reciting Surah Al-Fatihah and Another Surah",
                    "If possible, recite with your tongue; otherwise, recite quietly in your mind.",
                    "Reciting Surah Al-Fatihah and Another Surah:\n\n" +
                            "If possible, recite with your tongue; otherwise, recite quietly in your mind."
            ));

            // 5. Ruku (Bowing)
            list.add(new StepItem(
                    "5. Ruku (Bowing)",
                    "Gesture with your head or eyes for bowing. Dua: سُبْحَانَ رَبِّيَ الْعَظِيمِ [Sahih Muslim - 752]",
                    "Ruku (Bowing):\n\n" +
                            "Gesture with your head or eyes for bowing. The supplication for Ruku is:\n\n\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيمِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Subhana Rabbiyal 'Azeem\n\n\n" +
                            "Meaning:\n\n" +
                            "Glory be to my Lord the Most Great.\n\n" +
                            "[Sahih Muslim - 752]"
            ));

            // 6. Qiyam (Rising from Ruku)
            list.add(new StepItem(
                    "6. Qiyam (Rising from Ruku)",
                    "When rising from Ruku, gesture with head or eyes and say Sami' Allahu liman hamidah...",
                    "Qiyam (Rising from Ruku):\n\n" +
                            "When rising from Ruku, gesture with your head or eyes and say:\n\n\n" +
                            "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Sami' Allahu liman hamidah\n\n\n" +
                            "Meaning:\n\n" +
                            "Allah hears whoever praises Him.\n\n\n" +
                            "رَبَّنَا لَكَ الْحَمْدُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Rabbana lakal-hamd\n\n\n" +
                            "Meaning:\n\n" +
                            "Our Lord, to You belongs all praise."
            ));

            // 7. Sujud (First Prostration)
            list.add(new StepItem(
                    "7. Sujud (First Prostration)",
                    "Gesture with head or eyes for prostration. Dua: سُبْحَانَ رَبِّيَ الْأَعْلَى [Sahih Muslim - 752]",
                    "Sujud (First Prostration):\n\n" +
                            "Gesture with your head or eyes for prostration. The supplication for Sujud is:\n\n\n" +
                            "سُبْحَانَ رَبِّيَ الْأَعْلَى\n\n\n" +
                            "Transliteration:\n\n" +
                            "Subhana Rabbiyal A'la\n\n\n" +
                            "Meaning:\n\n" +
                            "Glory be to my Lord the Most High.\n\n" +
                            "[Sahih Muslim - 752]"
            ));

            // 8. Jalsah (Sitting between Prostrations)
            list.add(new StepItem(
                    "8. Jalsah (Sitting between Prostrations)",
                    "Gesture with your head or eyes to sit up from prostration.",
                    "Jalsah (Sitting between Prostrations):\n\n" +
                            "Gesture with your head or eyes to sit up between the two prostrations."
            ));

            // 9. Sujud (Second Prostration)
            list.add(new StepItem(
                    "9. Sujud (Second Prostration)",
                    "Gesture again with head or eyes for the second prostration and say 'Subhana Rabbiyal A'la' three times.",
                    "Sujud (Second Prostration):\n\n" +
                            "Gesture again with your head or eyes for the second prostration and say \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) three times."
            ));

            // 10. Second Rak'ah
            list.add(new StepItem(
                    "10. Second Rak'ah",
                    "Gesture with your head or eyes for the second rak'ah and complete it just like the first rak'ah.",
                    "Second Rak'ah:\n\n" +
                            "Gesture with your head or eyes for the second rak'ah and complete it just like the first rak'ah."
            ));

            // 11. Tashahhud (Attahiyyatu)
            list.add(new StepItem(
                    "11. Tashahhud (Attahiyyatu)",
                    "At the end of the second rak'ah, recite Tashahhud: التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ...",
                    "Tashahhud (Attahiyyatu):\n\n" +
                            "At the end of the second rak'ah, recite Tashahhud -\n\n\n" +
                            "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Attahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-nabiyyu wa rahmatullahi wa barakatuhu, as-salamu 'alayna wa 'ala 'ibadillahis-saliheen, ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluhu\n\n\n" +
                            "Meaning:\n\n" +
                            "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no god but Allah, and I bear witness that Muhammad is His slave and Messenger."
            ));

            // 12. Durood Sharif
            list.add(new StepItem(
                    "12. Durood Sharif",
                    "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ... (O Allah, send blessings upon Muhammad...)",
                    "Durood Sharif:\n\n\n" +
                            "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibraheema wa 'ala aali Ibraheema innaka Hameedum Majeed\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah! Send blessings upon Muhammad and upon the family of Muhammad, as You sent blessings upon Abraham and upon the family of Abraham. Indeed, You are Praiseworthy and Glorious."
            ));

            // 13. Du'a
            list.add(new StepItem(
                    "13. Du'a",
                    "Any supplication can be made after Tashahhud. A Sunnah supplication is: اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي...",
                    "Du'a:\n\n" +
                            "Any supplication can be made after Tashahhud. A Sunnah supplication is:\n\n\n" +
                            "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Allahumma innee zalamtu nafsee zulman katheeran wa la yaghfiruz-zunuba illa Anta, faghfir lee maghfiratan min 'indika warhamnee innaka Antal-Ghafoorur-Raheem\n\n\n" +
                            "Meaning:\n\n" +
                            "O Allah! I have wronged myself greatly, and none forgives sins except You. So grant me forgiveness from You and have mercy on me. Indeed, You are the Forgiving, the Merciful."
            ));

            // 14. Salam (Concluding Prayer)
            list.add(new StepItem(
                    "14. Salam (Concluding Prayer)",
                    "At the end of prayer, turn greetings to both sides with gestures: السلام عليكم ورحمة الله",
                    "Salam (Concluding Prayer):\n\n" +
                            "At the end of prayer, turn greetings to both sides with gestures:\n\n\n" +
                            "السلام عليكم ورحمة الله\n\n\n" +
                            "Transliteration:\n\n" +
                            "As-Salamu 'Alaykum wa Rahmatullah\n\n\n" +
                            "Meaning:\n\n" +
                            "Peace and the mercy of Allah be upon you."
            ));
        }

        return list;
    }
}
