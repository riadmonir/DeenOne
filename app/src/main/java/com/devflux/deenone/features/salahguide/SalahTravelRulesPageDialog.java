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

public class SalahTravelRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ভ্রমণ অবস্থায় সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Traveling");

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
            // 1. কুরআনের আলোকে
            list.add(new StepItem(
                    "কুরআনের আলোকে",
                    "আল্লাহ তাআলা বলেন: وَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ فَلَيْسَ عَلَيْكُمْ جُنَاحٌ... [সূরা আন-নিসা - ৪:১০১]",
                    "কুরআনের আলোকে:\n\nআল্লাহ তাআলা বলেন:\n\nوَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ فَلَيْسَ عَلَيْكُمْ جُنَاحٌ أَنْ تَقْصُرُوا مِنَ الصَّلَاةِ إِنْ خِفْتُمْ أَنْ يَفْتِنَكُمُ الَّذِينَ كَفَرُوا\n\nউচ্চারণ:\nওয়া ইযা দারাবতুম্ ফিল্ আর্দি ফা-লাইসা 'আলাইকুম্ জুনাহুন্ আন্ তাক্সুরু মিনাস্ সালাতি ইন্ খিফতুম্ আন্ ইয়াফ্তিনাকুমুল্ লাযীনা কাফারু।\n\nঅর্থ:\nআর যখন তোমরা যাত্রা করবে, তখন তোমাদের জন্য সালাত সংক্ষেপ করা কোন অপরাধ নয়, যদি তোমরা আশঙ্কা কর যে, কাফিররা তোমাদেরকে ফিতনায় ফেলবে।\n\n[সূরা আন-নিসা - ৪:১০১]"
            ));

            // 2. হাদিসের আলোকে
            list.add(new StepItem(
                    "হাদিসের আলোকে",
                    "ইবনে উমর (রাযি.) বলেন - كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ... [সহীহ বুখারি- ১০৯০]",
                    "হাদিসের আলোকে:\n\nইবনে উমর (রাযি.) বলেন -\n\nكَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَقْصُرُ فِي السَّفَرِ وَيَجْمَعُ بَيْنَ الصَّلَاتَيْنِ\n\nউচ্চারণ:\nকানা আন্ নাবিয়্যু সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম ইয়াক্সুরু ফিস্ সাফারি ওয়া ইয়াজ্মা'ু বাইনা সালাতাইন।\n\nঅর্থ:\nনবী সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম যাত্রার সময় সালাত সংক্ষেপ করতেন এবং দুই সালাত একত্রে আদায় করতেন।\n\n[সহীহ বুখারি- ১০৯০]"
            ));

            // 3. ভ্রমণ অবস্থায় সালাতের ধাপসমূহ
            list.add(new StepItem(
                    "ভ্রমণ অবস্থায় সালাতের ধাপসমূহ",
                    "নিয়ত, কসর, মাগরিব-ফজর ও দুই সালাত একত্রে আদায়ের নিয়ম...",
                    "ভ্রমণ অবস্থায় সালাতের ধাপসমূহ:\n\n• নিয়ত (ইচ্ছা): নামাজ সংক্ষেপ করার নিয়ত করতে হবে।\n• চার রাকাত ফরয নামাজকে দুই রাকাত পড়া: যোহর, আসর এবং এশার চার রাকাত ফরয নামাজকে দুই রাকাত করে আদায় করতে হবে।\n• মাগরিব ও ফজরের সালাত আগের মতই পড়া: মাগরিব তিন রাকাত এবং ফজর দুই রাকাত আগের মতই আদায় করতে হবে।\n• দুই সালাত একত্রে আদায় করা: যোহর-আসর এবং মাগরিব-এশা একত্রে আদায় করা যাবে।"
            ));

            // 4. ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "নামাজ সংক্ষেপ করার নিয়ত করতে হবে...",
                    "নিয়ত (ইচ্ছা):\n\nনামাজ সংক্ষেপ করার নিয়ত করতে হবে।"
            ));

            // 5. ২. চার রাকাত ফরয নামাজকে দুই রাকাত পড়া
            list.add(new StepItem(
                    "২. চার রাকাত ফরয নামাজকে দুই রাকাত পড়া",
                    "যোহর, আসর এবং এশার চার রাকাত ফরয নামাজকে দুই রাকাত করে আদায় করতে হবে...",
                    "চার রাকাত ফরয নামাজকে দুই রাকাত পড়া:\n\nযোহর, আসর এবং এশার চার রাকাত ফরয নামাজকে দুই রাকাত করে আদায় করতে হবে।"
            ));

            // 6. ৩. মাগরিব ও ফজরের সালাত আগের মতই পড়া
            list.add(new StepItem(
                    "৩. মাগরিব ও ফজরের সালাত আগের মতই পড়া",
                    "মাগরিব তিন রাকাত এবং ফজর দুই রাকাত আগের মতই আদায় করতে হবে...",
                    "মাগরিব ও ফজরের সালাত আগের মতই পড়া:\n\nমাগরিব তিন রাকাত এবং ফজর দুই রাকাত আগের মতই আদায় করতে হবে।"
            ));

            // 7. ৪. দুই সালাত একত্রে আদায় করা
            list.add(new StepItem(
                    "৪. দুই সালাত একত্রে আদায় করা",
                    "যোহর-আসর এবং মাগরিব-এশা একত্রে আদায় করা যাবে...",
                    "দুই সালাত একত্রে আদায় করা:\n\nযোহর-আসর এবং মাগরিব-এশা একত্রে আদায় করা যাবে।"
            ));

        } else {
            // English Mode
            // 1. According to the Quran
            list.add(new StepItem(
                    "According to the Quran",
                    "Allah the Almighty says: وَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ... [Surah An-Nisa, 4:101]",
                    "According to the Quran:\n\nAllah the Almighty says:\n\nوَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ فَلَيْسَ عَلَيْكُمْ جُنَاحٌ أَنْ تَقْصُرُوا مِنَ الصَّلَاةِ إِنْ خِفْتُمْ أَنْ يَفْتِنَكُمُ الَّذِينَ كَفَرُوا\n\nTransliteration:\nWa idha darabtum fil-ardi falaysa 'alaykum junahun an taqsuru minas-salati in khiftum an yaftinakumulladheena kafaroo.\n\nTranslation:\nAnd when you travel throughout the land, there is no blame upon you for shortening the prayer, [especially] if you fear that those who disbelieve may disrupt or harm you.\n\n[Surah An-Nisa, 4:101]"
            ));

            // 2. According to the Hadith
            list.add(new StepItem(
                    "According to the Hadith",
                    "Ibn Umar (RA) narrated: كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ... [Sahih al-Bukhari, 1090]",
                    "According to the Hadith:\n\nIbn Umar (may Allah be pleased with him) narrated -\n\nكَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَقْصُرُ فِي السَّفَرِ وَيَجْمَعُ بَيْنَ الصَّلَاتَيْنِ\n\nTransliteration:\nKana an-Nabiyyu sallallahu 'alayhi wa sallam yaqsuru fis-safari wa yajma'u baynas-salatayn.\n\nTranslation:\nThe Prophet (peace be upon him) used to shorten the prayers during travel and combine between two prayers.\n\n[Sahih al-Bukhari, 1090]"
            ));

            // 3. Stages of Prayer while Traveling
            list.add(new StepItem(
                    "Stages of Prayer while Traveling",
                    "Rules for Niyyah, shortening 4-Rak'ah prayers, Maghrib & Fajr, and combining prayers...",
                    "Stages of Prayer while Traveling:\n\n• Niyyah (Intention): Intention must be made to shorten the prayer.\n• Shortening 4-Rak'ah Fard Prayers to 2 Rak'ahs: The 4-Rak'ah Fard prayers of Dhuhr, Asr, and Isha are performed as 2 Rak'ahs.\n• Praying Maghrib and Fajr as Usual: Maghrib is performed as 3 Rak'ahs and Fajr as 2 Rak'ahs, just like regular prayers.\n• Combining Two Prayers: Dhuhr with Asr, and Maghrib with Isha can be combined during travel."
            ));

            // 4. 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Intention must be made to shorten the prayer...",
                    "Niyyah (Intention):\n\nIntention must be made to shorten the prayer."
            ));

            // 5. 2. Shortening 4-Rak'ah Fard Prayers
            list.add(new StepItem(
                    "2. Shortening 4-Rak'ah Fard Prayers",
                    "Perform Dhuhr, Asr, and Isha as 2 Rak'ahs...",
                    "Shortening 4-Rak'ah Fard Prayers to 2 Rak'ahs:\n\nThe 4-Rak'ah Fard prayers of Dhuhr, Asr, and Isha are performed as 2 Rak'ahs."
            ));

            // 6. 3. Praying Maghrib and Fajr as Usual
            list.add(new StepItem(
                    "3. Praying Maghrib and Fajr as Usual",
                    "Maghrib (3 Rak'ahs) and Fajr (2 Rak'ahs) remain unchanged...",
                    "Praying Maghrib and Fajr as Usual:\n\nMaghrib is performed as 3 Rak'ahs and Fajr as 2 Rak'ahs, just like regular prayers."
            ));

            // 7. 4. Combining Two Prayers
            list.add(new StepItem(
                    "4. Combining Two Prayers",
                    "Dhuhr-Asr and Maghrib-Isha can be combined...",
                    "Combining Two Prayers:\n\nDhuhr with Asr, and Maghrib with Isha can be combined during travel."
            ));
        }

        return list;
    }
}
