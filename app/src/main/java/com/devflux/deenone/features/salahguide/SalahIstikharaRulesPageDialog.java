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

public class SalahIstikharaRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইস্তেখারার সালাত আদায়ের পদ্ধতি" : "Method of Istikhara Prayer");

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
            // 1. সালাতুল ইস্তেখারার গুরুত্ব ও তাৎপর্য
            list.add(new StepItem(
                    "ইস্তেখারার সালাতের গুরুত্ব ও তাৎপর্য",
                    "ইস্তেখারা শব্দের অর্থ হল আল্লাহর কাছে ভালোমন্দের জ্ঞান প্রার্থনা করা...",
                    "ইস্তেখারা শব্দের অর্থ হল আল্লাহর কাছে ভালোমন্দের জ্ঞান প্রার্থনা করা। কোন গুরুত্বপূর্ণ কাজের পূর্বে আল্লাহর কাছে পরামর্শ নেওয়ার জন্য ইস্তেখারার সালাত আদায় করা হয়।"
            ));

            // 2. ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "ইস্তেখারার সালাত আদায়ের জন্য নিয়ত করুন। উদাহরণস্বরূপ, \"আমি ইস্তেখারার দুই রাকাত সালাত আদায় করছি আল্লাহর উদ্দেশ্যে।\"",
                    "নিয়ত (ইচ্ছা):\n\nইস্তেখারার সালাত আদায়ের জন্য নিয়ত করুন। উদাহরণস্বরূপ, \"আমি ইস্তেখারার দুই রাকাত সালাত আদায় করছি আল্লাহর উদ্দেশ্যে।\""
            ));

            // 3. ২. প্রথম তাকবির (আল্লাহু আকবার বলা)
            list.add(new StepItem(
                    "২. প্রথম তাকবির (আল্লাহু আকবার বলা)",
                    "নামাজ শুরু করতে প্রথমে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে হাত বেঁধে দাঁড়ান...",
                    "প্রথম তাকবির (আল্লাহু আকবার বলা):\n\nনামাজ শুরু করতে প্রথমে \"আল্লাহু আকবার\" (اللَّهُ أَكْبَرُ) বলে হাত বেঁধে দাঁড়ান।"
            ));

            // 4. ৩. কিয়াম (দাঁড়ানো)
            list.add(new StepItem(
                    "৩. কিয়াম (দাঁড়ানো)",
                    "নামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়...",
                    "কিয়াম (দাঁড়ানো):\n\nনামাজের প্রথমে দাঁড়িয়ে সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়।"
            ));

            // 5. ৪. রুকু (ঝুঁকে থাকা)
            list.add(new StepItem(
                    "৪. রুকু (ঝুঁকে থাকা)",
                    "\"আল্লাহু আকবার\" বলে রুকুতে যান এবং \"সুবহানা রাব্বিয়াল আযীম\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) পড়ুন...",
                    "রুকু (ঝুঁকে থাকা):\n\n\"আল্লাহু আকবার\" বলে রুকুতে যান এবং \"সুবহানা রাব্বিয়াল আযীম\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ) পড়ুন।"
            ));

            // 6. ৫. সিজদা
            list.add(new StepItem(
                    "৫. সিজদা",
                    "রুকু থেকে উঠার পরে সিজদা করুন এবং \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) পড়ুন...",
                    "সিজদা:\n\nরুকু থেকে উঠার পরে সিজদা করুন এবং \"সুবহানা রাব্বিয়াল আ'লা\" (سُبْحَانَ رَبِّيَ الْأَعْلَى) পড়ুন।"
            ));

            // 7. ৬. দুই রাকাত নামাজ আদায়
            list.add(new StepItem(
                    "৬. দুই রাকাত নামাজ আদায়",
                    "দুই রাকাত নামাজ আদায়ের পরে ইস্তেখারার দু'আ পড়ুন...",
                    "দুই রাকাত নামাজ আদায়:\n\nদুই রাকাত নামাজ আদায়ের পরে ইস্তেখারার দু'আ পড়ুন"
            ));

            // 8. ৭. ইস্তেখারার দু'আ
            list.add(new StepItem(
                    "৭. ইস্তেখারার দু'আ",
                    "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ...",
                    "ইস্তেখারার দু'আ:\n\nاللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلَا أَقْدِرُ، وَتَعْلَمُ وَلَا أَعْلَمُ، وَأَنْتَ عَلَّامُ الْغُيُوبِ. اللَّهُمَّ إِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ خَيْرٌ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاقْدُرْهُ لِي وَيَسِّرْهُ لِي ثُمَّ بَارِكْ লِي فِيهِ. وَإِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ شَرٌّ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاصْرِفْهُ عَنِّي وَاصْرِفْنِي عَنْهُ وَاقْدُرْ لِيَ الْخَيْرَ حَيْثُ كَانَ ثُمَّ أَرْضِنِي\n\nউচ্চারণ:\nআল্লাহুম্মা ইন্নি আস্তাখিরুকা বিইল্মিকা ওয়া আস্তাকদিরুকা বিখুদরাতিকা ওয়া আসআলুকা মিন ফাদলিকা আল্ আযীমি, ফা-ইন্নাকা তাক্দিরু ওয়ালা আক্দিরু, ওয়া তা'লামু ওয়ালা আ'লামু, ওয়ান্তা আ'ল্লামুল্ গুযুব। আল্লাহুম্মা ইন্ কুন্তা তা'লামু আন্না হাযাল্ আম্রা খাইরুন্ লি ফি দীনি ওয়া মা'আশি ওয়া আ'কিবাতি আম্রি, ফাক্দুর্হু লি ওয়া ইয়াস্সির্হু লি সুম্মা বারিক্লি ফিহি। ওয়া ইন্ কুন্তা তা'লামু আন্না হাযাল্ আম্রা শাররুন্ লি ফি দীনি ওয়া মা'আশি ওয়া আ'কিবাতি আম্রি, ফাসরিফ্হু 'আন্নি ওয়া আস্রিফ্নি 'আন্হু ওয়া ক্দুর্লি আল্ খাইরো হাইথু কান সুম্মা আর্দ্বিনি।\n\nঅর্থ:\nহে আল্লাহ! আমি আপনার জ্ঞান দ্বারা আপনার কাছে ভালোর জন্য প্রার্থনা করছি এবং আপনার শক্তি দ্বারা ক্ষমা চাইছি এবং আপনার মহান করুণার জন্য প্রার্থনা করছি, কেননা আপনি সক্ষম আর আমি অক্ষম, আপনি জানেন আর আমি জানি না, আর আপনি সকল গায়ব জানেন। হে আল্লাহ! আপনি যদি জানেন যে এই কাজটি আমার জন্য ভালো, আমার দ্বীন, জীবন এবং পরকালের জন্য, তবে তা আমার জন্য নির্ধারণ করুন এবং সহজ করুন এবং এতে বরকত দান করুন। আর আপনি যদি জানেন যে এই কাজটি আমার জন্য খারাপ, আমার দ্বীন, জীবন এবং পরকালের জন্য, তবে তা আমার থেকে দূরে সরিয়ে দিন এবং আমাকে তা থেকে দূরে রাখুন এবং আমার জন্য ভালো নির্ধারণ করুন, যেখানেই তা হোক এবং আমাকে সন্তুষ্ট করুন।".replace("লِي", "لِي")
            ));

        } else {
            // English mode
            // 1. Significance of Salat al-Istikhara
            list.add(new StepItem(
                    "Significance of Salat al-Istikhara",
                    "The word 'Istikhara' means seeking the knowledge of good and bad from Allah...",
                    "The word 'Istikhara' means seeking the knowledge of good and bad from Allah. The Istikhara prayer is performed before any important matter or decision to seek counsel and guidance from Allah."
            ));

            // 2. 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Make the intention to perform the Istikhara prayer. For example: \"I am performing two Rak'ahs of Salat al-Istikhara for the sake of Allah.\"",
                    "Niyyah (Intention):\n\nMake the intention to perform the Istikhara prayer. For example: \"I am performing two Rak'ahs of Salat al-Istikhara for the sake of Allah.\""
            ));

            // 3. 2. First Takbir (Saying Allahu Akbar)
            list.add(new StepItem(
                    "2. First Takbir (Saying Allahu Akbar)",
                    "To begin the prayer, first say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and fold your hands...",
                    "First Takbir (Saying Allahu Akbar):\n\nTo begin the prayer, first say \"Allahu Akbar\" (اللَّهُ أَكْبَرُ) and fold your hands while standing."
            ));

            // 4. 3. Qiyam (Standing)
            list.add(new StepItem(
                    "3. Qiyam (Standing)",
                    "At the beginning of prayer, stand and recite Surah Al-Fatihah followed by another Surah...",
                    "Qiyam (Standing):\n\nAt the beginning of prayer, stand and recite Surah Al-Fatihah followed by another Surah from the Quran."
            ));

            // 5. 4. Ruku (Bowing)
            list.add(new StepItem(
                    "4. Ruku (Bowing)",
                    "Say \"Allahu Akbar\" and bow down in Ruku, and recite \"Subhana Rabbiyal Azeem\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ)...",
                    "Ruku (Bowing):\n\nSay \"Allahu Akbar\" and bow down in Ruku, and recite \"Subhana Rabbiyal Azeem\" (سُبْحَانَ رَبِّيَ الْعَظِيمِ)."
            ));

            // 6. 5. Sujood (Prostration)
            list.add(new StepItem(
                    "5. Sujood (Prostration)",
                    "After rising from Ruku, prostrate in Sujood and recite \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى)...",
                    "Sujood (Prostration):\n\nAfter rising from Ruku, prostrate in Sujood and recite \"Subhana Rabbiyal A'la\" (سُبْحَانَ رَبِّيَ الْأَعْلَى)."
            ));

            // 7. 6. Performing Two Rak'ahs
            list.add(new StepItem(
                    "6. Performing Two Rak'ahs",
                    "After completing the two Rak'ahs of prayer, recite the Dua of Istikhara...",
                    "Performing Two Rak'ahs:\n\nAfter completing the two Rak'ahs of prayer, recite the Dua of Istikhara."
            ));

            // 8. 7. Dua of Istikhara
            list.add(new StepItem(
                    "7. Dua of Istikhara",
                    "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ...",
                    "Dua of Istikhara:\n\nاللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلَا أَقْدِرُ، وَتَعْلَمُ وَلَا أَعْلَمُ، وَأَنْتَ عَلَّامُ الْغُيُوبِ. اللَّهُمَّ إِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ خَيْرٌ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاقْدُرْهُ لِي وَيَسِّرْهُ لِي ثُمَّ بَارِكْ لِي فِيهِ. وَإِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ شَرٌّ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاصْرِفْهُ عَنِّي وَاصْرِفْنِي عَنْهُ وَاقْدُرْ لِيَ الْخَيْرَ حَيْثُ كَانَ ثُمَّ أَرْضِنِي\n\nTransliteration:\nAllahumma inni astakhiruka bi'ilmika wa astaqdiruka biqudratika wa as'aluka min fadlikal 'azeem, fa-innaka taqdiru wa la aqdir, wa ta'lamu wa la a'lam, wa anta 'allamul ghuyub. Allahumma in kunta ta'lamu anna hadhal amra khayrun li fee deeni wa ma'ashi wa 'aqibati amri faqdurhu li wa yassirhu li thumma barik li feeh. Wa in kunta ta'lamu anna hadhal amra sharrun li fee deeni wa ma'ashi wa 'aqibati amri fasrifhu 'anni wasrifni 'anhu waqdur liyal khayra haythu kana thumma ardini.\n\nTranslation:\nO Allah! I seek Your guidance through Your knowledge and seek ability through Your power, and I ask You of Your immense bounty. For You are capable and I am not, You know and I do not know, and You are the Knower of the unseen. O Allah! If You know that this matter is good for me in my religion, my livelihood and the outcome of my affair, then decree it for me, facilitate it for me, and then bless me in it. And if You know that this matter is evil for me in my religion, my livelihood and the outcome of my affair, then turn it away from me and turn me away from it, and decree for me what is good wherever it may be, and make me pleased with it."
            ));
        }

        return list;
    }
}
