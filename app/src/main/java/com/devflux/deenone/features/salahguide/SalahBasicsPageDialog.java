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

public class SalahBasicsPageDialog {

    public static class SalahBasicsItem {
        public final String title;
        public final String previewSubtitle;
        public final String fullContent;
        public boolean isExpanded;

        public SalahBasicsItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "সালাত বিষয়ক প্রাথমিক ধারণা" : "Basic Rules & Guidelines of Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick options
        });

        List<SalahBasicsItem> items = getSalahBasicsItems(isBn);

        SalahBasicsAdapter adapter = new SalahBasicsAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class SalahBasicsAdapter extends RecyclerView.Adapter<SalahBasicsAdapter.ViewHolder> {
        private final Context context;
        private final List<SalahBasicsItem> items;
        private final boolean isBn;

        public SalahBasicsAdapter(Context context, List<SalahBasicsItem> items, boolean isBn) {
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
            SalahBasicsItem item = items.get(position);
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
                        SalahBasicsItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(SalahBasicsItem item, boolean isBn) {
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

    public static List<SalahBasicsItem> getSalahBasicsItems(boolean isBn) {
        List<SalahBasicsItem> list = new ArrayList<>();

        if (isBn) {
            // ১. কোন ওয়াক্তে কত রাকাত সালাত
            list.add(new SalahBasicsItem(
                    "কোন ওয়াক্তে কত রাকাত সালাত",
                    "ফজর নামাজ: রাকাত: ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত ফরজ। সময়: সুবহে সাদিক থেকে সূর্যোদয় পর্যন্ত। [মুসলিম - ১৪৫৬, ৯৬৬] যোহর নামাজ: রাকাত: ৪ রাকাত সুন্নতে মুয়াক্কাদা, ৪ রাকাত ফরজ, ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত নফল। সময়: সূর্য মধ্যাকাশে ঢলে যাওয়ার পর থেকে...",
                    "ফজর নামাজ:\n\n" +
                            "রাকাত: ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত ফরজ।\n\n" +
                            "সময়: সুবহে সাদিক থেকে সূর্যোদয় পর্যন্ত।\n\n" +
                            "[মুসলিম - ১৪৫৬, ৯৬৬]\n\n\n" +
                            "যোহর নামাজ:\n\n" +
                            "রাকাত: ৪ রাকাত সুন্নতে মুয়াক্কাদা, ৪ রাকাত ফরজ, ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত নফল।\n\n" +
                            "সময়: সূর্য মধ্যাকাশে ঢলে যাওয়ার পর থেকে আছর পর্যন্ত।\n\n" +
                            "[বুখারি - ৭১৭, ৫০৬, মুসলিম - ৯৬৯]\n\n\n" +
                            "আছর নামাজ:\n\n" +
                            "রাকাত: ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা, ৪ রাকাত ফরজ।\n\n" +
                            "সময়: যোহরের সালাতের সময় শেষ হওয়ার পর থেকে পশ্চিম আকাশে সূর্য হলুদ বর্ণ ধারণ করা পর্যন্ত।\n\n" +
                            "[বুখারি - ৭১৭, ৫৪৫]\n\n\n" +
                            "মাগরিব নামাজ:\n\n" +
                            "রাকাত: ৩ রাকাত ফরজ, ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত নফল।\n\n" +
                            "সময়: সূর্যাস্তের পর থেকে এশার আগে পর্যন্ত।\n\n" +
                            "[মুসলিম - ৯৬৯, দারাকুতনি - ১০৬৬]\n\n\n" +
                            "এশা নামাজ:\n\n" +
                            "রাকাত: ৪ রাকাত সুন্নতে গায়রে মুয়াক্কাদা, ৪ রাকাত ফরজ, ২ রাকাত সুন্নতে মুয়াক্কাদা, ২ রাকাত নফল, ৩ রাকাত বিতর (ওয়াজিব), ২ রাকাত নফল।\n\n" +
                            "সময়: মাগরিবের পর থেকে রাতের এক তৃতীয়াংশ পর্যন্ত।\n\n" +
                            "[মুসলিম - ৯৬৯, সহিহ বুখারি - ৫০৮, ৫৩৮, ৯৬৯]\n\n\n" +
                            "*জুম'আ:\n\n" +
                            "জুম‘আর সালাত ২ রাক‘আত ফরয। তার পূর্বে মসজিদে প্রবেশের পর বসার পূর্বে কমপক্ষে ২ রাক‘আত ‘তাহিইয়াতুল মাসজিদ’ এবং জুম‘আ শেষে ৪ অথবা ২ রাক‘আত সুন্নাত। উপরে বর্ণিত সবগুলিই রাসূলু্ল্লাহ (সাঃ)-এর নিয়মিত আমল দ্বারা নির্ধারিত এবং সহীহ হাদীস সমূহ দ্বারা প্রমাণিত\n\n" +
                            "[সহীহ ইবনু খুযায়মা‘সালাত’অধ্যায়, ২ অনুচ্ছেদ নাসাঈ ‘সালাত’অধ্যায়-৫,অনুচ্ছেদ-৩]।"
            ));

            // ২. সালাত কী এবং আমরা কেন সালাত আদায় করবো
            list.add(new SalahBasicsItem(
                    "সালাত কী এবং আমরা কেন সালাত আদায় করবো",
                    "নামাজ (সালাত) ইসলামের পাঁচটি স্তম্ভের মধ্যে একটি এবং মুসলিমদের জীবনে অত্যন্ত গুরুত্বপূর্ণ। নামাজ আল্লাহর সাথে সংযোগ স্থাপন এবং আত্মিক পবিত্রতা অর্জনের একটি উপায়। কুরআনে নামাজের উল্লেখ: নামাজের প্রয়োজনীয়তা এবং এর পদ্ধতি কুরআনে বহুবার উল্লেখ করা হয়েছে: ...",
                    "নামাজ (সালাত) ইসলামের পাঁচটি স্তম্ভের মধ্যে একটি এবং মুসলিমদের জীবনে অত্যন্ত গুরুত্বপূর্ণ। নামাজ আল্লাহর সাথে সংযোগ স্থাপন এবং আত্মিক পবিত্রতা অর্জনের একটি উপায়।\n\n\n" +
                            "কুরআনে নামাজের উল্লেখ:\n\n" +
                            "নামাজের প্রয়োজনীয়তা এবং এর পদ্ধতি কুরআনে বহুবার উল্লেখ করা হয়েছে:\n\n\n" +
                            "أَقِيمُواْ ٱلصَّلَوٰةَ وَءَاتُواْ ٱلزَّكَوٰةَ وَٱرْكَعُواْ مَعَ ٱلرَّٰكِعِينَ\n\n" +
                            "উচ্চারণ:\n" +
                            "আকীমুস সালাত ওয়া আতোউ যাকাত ওয়ারকা‘উ মা‘আর রাকি‘ঈন\n\n" +
                            "অর্থ:\n" +
                            "নামাজ কায়েম কর, যাকাত দাও এবং নামাজ আদায়কারীদের সাথে রুকু কর।\n\n" +
                            "[সূরা আল-বাকারা ২:৪৩]\n\n\n" +
                            "إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَٰبًۭا مَّوْقُوتًۭا\n\n" +
                            "উচ্চারণ:\n" +
                            "ইন্নাস সালাত কানা আলাল মু’মিনিনা কিতাবাম মাওকুত\n\n" +
                            "অর্থ:\n" +
                            "নিশ্চয়ই নামাজ মুসলমানদের জন্য নির্দিষ্ট সময়ের ফরজ\n\n" +
                            "[সূরা আন-নিসা ৪:১০৩]\n\n\n" +
                            "নামাজ ইসলামের একটি গুরুত্বপূর্ণ স্তম্ভ এবং মুসলিমদের দৈনন্দিন জীবনের অন্যতম অংশ। নামাজের ইতিহাস এবং এর প্রবর্তন মুসলিমদের ইবাদতের একটি মৌলিক অংশ হিসেবে গুরুত্বপূর্ণ। আল্লাহ আমাদের সবাইকে সঠিকভাবে নামাজ পড়ার তাওফিক দান করুন।"
            ));

            // ৩. সালাত যেকারণে আল্লহর সবচেয়ে প্রিয় আমল
            list.add(new SalahBasicsItem(
                    "সালাত যেকারণে আল্লহর সবচেয়ে প্রিয় আমল",
                    "ঈমানের পর সবচেয়ে গুরুত্বপূর্ণ বিষয় হল নামাজ। রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম আল্লাহর পক্ষ থেকে বার বার নামাজের তাগিদ পেয়েছেন। কুরআনে পাকে আল্লাহ তাআলা বিভিন্ন জায়গায় সরাসরি ৮২ বার সালাত শব্দ উল্লেখ করে নামাজের গুরুত্ব তুলে ধরেছেন হজরত আবদুল্লাহ ইব...",
                    "ঈমানের পর সবচেয়ে গুরুত্বপূর্ণ বিষয় হল নামাজ। রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম আল্লাহর পক্ষ থেকে বার বার নামাজের তাগিদ পেয়েছেন। কুরআনে পাকে আল্লাহ তাআলা বিভিন্ন জায়গায় সরাসরি ৮২ বার সালাত শব্দ উল্লেখ করে নামাজের গুরুত্ব তুলে ধরেছেন\n\n\n" +
                            "হজরত আবদুল্লাহ ইবনে মাসউদ রাদিয়াল্লাহু আনহু থেকে বর্ণিত তিনি বলেন:\n\n" +
                            "আমি রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামকে জিজ্ঞাসা করলাম- (হে আল্লাহর রাসুল!) আল্লাহর কাছে সবচেয়ে বেশি প্রিয় আমল কোনটি? রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বললেন, ‘নামাজ’।"
            ));

            // ৪. কোন দিকে মুখ করে সালাত আদায় করা হয়
            list.add(new SalahBasicsItem(
                    "কোন দিকে মুখ করে সালাত আদায় করা হয়",
                    "ইসলামের দ্বিতীয় কিবলা হলো কা'বা শরীফ। এই ক্বিবলা হলো বর্তমান মুসলমানদের ক্বিবলা, অর্থাৎ যে দিকে মুখ করে নামাজ পড়ে বা সালাত আদায় করে, পৃথিবীর যে স্থান থেকে কাবা যে দিকে মুসলমানগণ ঠিক সে দিকে মুখ করে নামাজ পরেন। হজ্জ এবং উমরা পালনের সময় মুসলমানগণ কাবা...",
                    "ইসলামের দ্বিতীয় কিবলা হলো কা'বা শরীফ। এই ক্বিবলা হলো বর্তমান মুসলমানদের ক্বিবলা, অর্থাৎ যে দিকে মুখ করে নামাজ পড়ে বা সালাত আদায় করে, পৃথিবীর যে স্থান থেকে কাবা যে দিকে মুসলমানগণ ঠিক সে দিকে মুখ করে নামাজ পরেন। হজ্জ এবং উমরা পালনের সময় মুসলমানগণ কাবাকে ঘিরে তাওয়াফ বা প্রদক্ষিণ করেন।"
            ));

            // ৫. কখন থেকে সালাত আদায় ফরয হয়
            list.add(new SalahBasicsItem(
                    "কখন থেকে সালাত আদায় ফরয হয়",
                    "একজন মানুষের জীবনে কখন থেকে সালাত ফরয হয়? বিষয়টি জানা জরুরী। কেননা দিন দিন আমাদের বয়স বৃদ্ধি হলেও সালাত আদায়ের পরিমাণ বৃদ্ধি হয়নি। অনেক ক্ষেত্রে বিষয়টি এড়িয়ে যায়। যা একজন মুসলিম হিসেবে কখন কাম্য নয়! চলুন, জেনে নেওয়া যাক কখন থেকে সালাত আদায় করতে হয়...",
                    "একজন মানুষের জীবনে কখন থেকে সালাত ফরয হয়? বিষয়টি জানা জরুরী। কেননা দিন দিন আমাদের বয়স বৃদ্ধি হলেও সালাত আদায়ের পরিমাণ বৃদ্ধি হয়নি। অনেক ক্ষেত্রে বিষয়টি এড়িয়ে যায়। যা একজন মুসলিম হিসেবে কখন কাম্য নয়! চলুন, জেনে নেওয়া যাক কখন থেকে সালাত আদায় করতে হয়। প্রথমিক কথা হচ্ছে, ব্যাক্তি কয়েকটি বিষয় পূর্ণ করলে তার উপর সালাত ফরয হওয়া\n\n\n" +
                            "মুসলিম হওয়া:\n\n" +
                            "সালাত ফরয হওয়ার জন্য প্রথম শর্ত বা আবশ্যক বিষয় হল ব্যক্তিকে মুসলিম হতে হবে। কেননা অবিশ্বাসী বা অমুসলিমদের উপর এই বিধান নয়। পবিত্র কুরআনে আল্লাহ বলেন...\n\n\n" +
                            "وَ مَنۡ یَّبۡتَغِ غَیۡرَ الۡاِسۡلَامِ دِیۡনًا فَلَنۡ یُّقۡবَلَ مِنۡ\n\n" +
                            "অর্থ:\n" +
                            "আর যে ইসলাম ছাড়া অন্য কোন দীন চায় তবে তার কাছ থেকে তা কখনো গ্রহণ করা হবে না।\n\n" +
                            "[আলে ইমরান- ৩/৮৫; তওবা - ৯/১৭]\n\n\n" +
                            "জ্ঞানসম্পন্ন হওয়া:\n\n" +
                            "কোন পাগল বা যে ব্যক্তির পূর্ণ জ্ঞান নেই তার উপর সালাত ফরয নয়। যদি ব্যক্তি জ্ঞানবান হন তবে তার উপর সালাত ফরয হবে। রাসূল (সাঃ) বলেন -\n\n\n" +
                            "رُفِعَ الْقَلَمُ عَنْ ثَلَاثَةٍ: عَنِ النَّائِمِ حَتَّى يَسْتَيْقِظَ وَعَنِ الصَّبِيِّ حَتَّى يَبْلُغَ وَعَنِ الْمَعْتُوهِ حَتَّى يعقل\n\n" +
                            "অর্থ:\n" +
                            "তিন ব্যক্তি (কিয়ামত দিবসে) দায়-দায়িত্বমুক্ত।\n\n" +
                            "ঘুমন্ত ব্যক্তি জেগে না ওঠা পর্যন্ত।\n" +
                            "অপ্রাপ্তবয়স্ক বালেগ না হওয়া পর্যন্ত এবং\n" +
                            "নির্বোধ ব্যক্তি (বুদ্ধি-বিবেচনার) জ্ঞান ফিরে না আসা পর্যন্ত।\n\n" +
                            "[তিরমিযী, আবুদাঊদ, মিশকাত - ৩২৮৭]\n\n\n" +
                            "বয়ঃপ্রাপ্ত হওয়া:\n\n" +
                            "মুসলিম ব্যক্তি প্রাপ্ত বয়স্ক হলে তার উপর সালাত ফরয হয়। কেননা শিশুর উপর থেকে কলম উঠিয়ে নেওয়া হয়েছে। অর্থ্যৎ তার পাপ লিখা হবে না। সেজন্য বালেগ ব্যক্তির উপর এটি ফরয। যা ছোট বয়স থেকেই অভ্যাস করতে হয়। রাসূল (সাঃ) বলেন,\n\n\n" +
                            "مُرُوا أَوْلَادَكُمْ بِالصَّلَاةِ وَهُمْ أَبْنَاءُ سَبْعِ سِنِينَ وَاضْرِبُوهُمْ عَلَيْهَا وَهُمْ أَبْنَاءُ عَشْرٍ سِنِين وَفَرِّقُوا بَيْنَهُمْ فِي الْمَضَاجِعِ\n\n" +
                            "অর্থ:\n" +
                            "যখন তোমার সন্তানদের বয়স সাত বছরে পৌঁছবে তখন তাদেরকে সালাত আদায়ের জন্য নির্দেশ দিবে। আর তাদের শাস্তি দিবে যখন তারা দশ বছরে পৌঁছবে এবং তাদের ঘুমানোর স্থান পৃথক করে দিবে।\n\n" +
                            "[আহমাদ, আবুদাঊদ, তিরমিযী, মিশকাত হা/৫৭২; নায়ল ২/২২ পৃঃ ]"
            ));

            // ৬. ৫ ওয়াক্ত সালাত যেভাবে এসেছে
            list.add(new SalahBasicsItem(
                    "৫ ওয়াক্ত সালাত যেভাবে এসেছে",
                    "পাঁচ ওয়াক্ত সালাত বা নামাজ কিভাবে ফরজ হলো, তার বিস্তারিত আলোচনা ইসলামের অন্যতম বিশেষ ঘটনা মেরাজের রাত-এর সাথে যুক্ত। মেরাজের ঘটনা নবী মুহাম্মদ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের জীবনের অন্যতম গুরুত্বপূর্ণ ঘটনা, যেখানে পাঁচ ওয়াক্ত নামাজ মুসলিমদের উপর ফরজ করা হ...",
                    "পাঁচ ওয়াক্ত সালাত বা নামাজ কিভাবে ফরজ হলো, তার বিস্তারিত আলোচনা ইসলামের অন্যতম বিশেষ ঘটনা মেরাজের রাত-এর সাথে যুক্ত। মেরাজের ঘটনা নবী মুহাম্মদ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের জীবনের অন্যতম গুরুত্বপূর্ণ ঘটনা, যেখানে পাঁচ ওয়াক্ত নামাজ মুসলিমদের উপর ফরজ করা হয়। এখানে সালাত ফরজ হওয়ার প্রেক্ষাপট ও ঘটনার ব্যাখ্যা দেওয়া হলো:\n\n\n" +
                            "পাঁচ ওয়াক্ত সালাত যেভাবে ফরজ হলো, মেরাজের রাতের পটভূমি:\n\n" +
                            "মেরাজের ঘটনা হিজরতের আগে নবীজির জীবনে ঘটে। এটি ছিল ১০ম নবুয়ত বর্ষ, যা দুঃখের বছর (আমুল হুজন) নামে পরিচিত। এই বছর নবীজি (সাঃ)-এর প্রিয় স্ত্রী খাদিজা (রাঃ) এবং চাচা আবু তালিব ইন্তেকাল করেন। আল্লাহ নবীজি (সাঃ)-কে সান্ত্বনা দেওয়ার জন্য এবং উম্মতের জন্য বিশেষ দান হিসেবে মেরাজের ঘটনা ঘটান।\n\n\n" +
                            "মেরাজের শুরু:\n\n" +
                            "মেরাজের রাত ছিল রজব মাসের ২৭ তারিখ। নবীজি (সাঃ) কাবা শরিফে অবস্থান করছিলেন। আল্লাহর আদেশে ফেরেশতা জিবরাইল (আঃ) এসে তাঁকে বুরাক নামক এক বিশেষ বাহনে মসজিদুল হারাম (মক্কা) থেকে মসজিদুল আকসা (জেরুজালেম) নিয়ে যান। এই অংশকে ইসরা বলা হয়। এখানে নবীজি (সাঃ) নবীদের নিয়ে জামাতে ইমামতি করেন।\n\n\n" +
                            "সিদরাতুল মুনতাহায় গমন (মেরাজ):\n\n" +
                            "মসজিদুল আকসা থেকে ফেরেশতা জিবরাইল (আঃ) নবীজি (সাঃ)-কে সিদরাতুল মুনতাহা (সপ্তম আসমানের উপরের সীমা) নিয়ে যান। সেখান থেকে নবীজি (সাঃ) একমাত্র ব্যক্তি যিনি আরশে আজিমে (আল্লাহর কাছে) পৌঁছান।\n\n\n" +
                            "সালাত ফরজ হওয়া:\n\n" +
                            "আল্লাহ তাআলা নবীজি (সাঃ)-এর সঙ্গে সরাসরি কথা বলেন এবং মুসলিমদের জন্য ৫০ ওয়াক্ত সালাত ফরজ করেন। এরপর নবীজি (সাঃ) হযরত মূসা (আঃ)-এর সঙ্গে দেখা করেন। মূসা (আঃ) নবীজি (সাঃ)-কে বলেন, “আপনার উম্মত এই ৫০ ওয়াক্ত সালাত আদায় করতে পারবে না। আপনার রবের কাছে গিয়ে কমানোর জন্য অনুরোধ করুন।” নবীজি (সাঃ) বারবার আল্লাহর কাছে ফিরে গিয়ে সালাতের সংখ্যা কমানোর অনুরোধ করেন। অবশেষে ৫ ওয়াক্ত সালাত নির্ধারণ করা হয়, তবে ৫০ ওয়াক্তের সওয়াবই থাকবে।\n\n\n" +
                            "পাঁচ ওয়াক্ত সালাতের গুরুত্ব:\n\n" +
                            "মেরাজের রাতেই সালাতের গুরুত্ব বোঝানো হয়। এটি এমন একটি ইবাদত, যা সরাসরি আল্লাহর আদেশে ফরজ করা হয়েছে। নামাজ হলো মুসলমানের জন্য আল্লাহর সঙ্গে সম্পর্ক স্থাপনের মাধ্যম।\n\n\n" +
                            "কুরআনে সালাতের নির্দেশ:\n\n\n" +
                            "إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَـٰبًۭا مَّوْقُوتًۭا\n\n" +
                            "অর্থ:\n" +
                            "নিশ্চয়ই নামাজ মুসলমানদের জন্য নির্ধারিত সময়ে ফরজ করা হয়েছে।\n\n" +
                            "[সূরা আন-নিসা, ৪:১০৩]\n\n\n" +
                            "أَوَّلُ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ الصَّلَاةُ، فَإِنْ صَلَحَتْ، صَلَحَ سَائِرُ عَمَلِهِ، وَإِنْ فَسَدَتْ، فَسَدَ سَائِرُ عَمَلِهِ\n\n" +
                            "অর্থ:\n" +
                            "কিয়ামতের দিন বান্দার প্রথম হিসাব হবে সালাত সম্পর্কে। যদি সালাত সঠিক হয়, তবে বাকি আমলও সঠিক হবে। আর যদি সালাত নষ্ট হয়, তবে বাকি আমলও নষ্ট হবে।\n\n" +
                            "[তিরমিজি, হাদিস: ৪১৩]\n\n\n" +
                            "নামাজ হলো জান্নাতের চাবি এবং মুসলমানের জীবনের অন্যতম প্রধান ইবাদত। এটি আত্মশুদ্ধি, ধৈর্য, এবং আল্লাহর প্রতি আনুগত্যের সর্বোচ্চ প্রকাশ। হাদিসে রাসূলুল্লাহ (সাঃ) বলেছেন:\n\n\n" +
                            "مِفْتَاحُ الْجَنَّةِ الصَّلَاةُ\n\n" +
                            "অর্থ:\n" +
                            "জান্নাতের চাবি হলো নামাজ।\n\n" +
                            "[সহীহ মুসলিম, হাদিস: ৪৮৭]"
            ));

            // ৭. আযানের উত্তরে আমাদের যা বলা উচিত
            list.add(new SalahBasicsItem(
                    "আযানের উত্তরে আমাদের যা বলা উচিত",
                    "আযানের উত্তরে আমাদের যা বলা উচিত। রাসূলুল্লাহ (সঃ) আযানের উত্তর প্রসঙ্গে বলেন, إِذَا سَمِعْتُمُ الْمُؤَذِّنَ فَقُولُوا مِثْلَ مَا يَقُولُ অর্থ: তোমরা মুয়াযযিনের আযান শুনলে উত্তরে সে শব্দগুলোরই পুনরাবৃত্তি করবে...",
                    "আযানের উত্তরে আমাদের যা বলা উচিত। রাসূলুল্লাহ (সঃ) আযানের উত্তর প্রসঙ্গে বলেন:\n\n\n" +
                            "إِذَا سَمِعْتُمُ الْمُؤَذِّنَ فَقُولُوا مِثْلَ مَا يَقُولُ\n\n" +
                            "অর্থ:\n" +
                            "তোমরা মুয়াযযিনের আযান শুনলে উত্তরে সে শব্দগুলোরই পুনরাবৃত্তি করবে।\n\n" +
                            "[মুসলিম, মিশকাত হা/৬৫৭ ‘সালাত’ অধ্যায়-৪]\n\n\n" +
                            "উমার (রাঃ) হতে বর্ণিত। তিনি বলেন, রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম বলেছেনঃ\n\n" +
                            "আযানের উত্তরে যা বলবেন,\n\n\n" +
                            "وَعَنْ عُمَرَ قَالَ: قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «إِذَا قَالَ الْمُؤَذِّنُ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ فَقَالَ أَحَدُكُمُ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ ثُمَّ قَالَ أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ قَالَ أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ ثُمَّ قَالَ أَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ قَالَ أَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ ثُمَّ قَالَ حَيَّ عَلَى الصَّلَاةِ قَالَ لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ ثُمَّ قَالَ حَيَّ عَلَى الْفَلَاحِ قَالَ لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ ثُمَّ قَالَ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ قَالَ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ ثُمَّ قَالَ لَا إِلَهَ إِلَّا اللَّهُ قَالَ لَا إِلَهَ إِلَّا اللَّهُ مِنْ قَلْبِهِ دخل الْجنَّة\n\n" +
                            "অর্থ:\n" +
                            "মুয়ায্যিন যখন ’’আল্লা-হু আকবার’’ বলে তখন তোমাদের কেউ যদি (উত্তরে) অন্তর থেকে বলে, ’’আল্লা-হু আকবার’’ ’’আল্লা-হু আকবার’’,\n" +
                            "এরপর মুয়ায্যিন যখন বলেন, ’’আশহাদু আল্লা- ইলা-হা ইল্লাল্লা-হ’’ সেও বলে, ’’আশ্হাদু আল্লা- ইলা-হা ইল্লাল্লা-হ’’।\n" +
                            "অতঃপর মুয়ায্যিন যখন বলে, ’’আশহাদু আন্না মুহাম্মাদার্ রসূলুল্ল-হ’’, সেও বলে ’’আশ্হাদু আন্না মুহাম্মাদার্ রসূলুল্ল-হ’’।\n" +
                            "তারপর মুয়ায্যিন যখন বলে, ’’হাইয়্যা ’আলাস্ সলা-হ্’’, সে তখন বলে, ’’লা- হাওলা ওয়ালা- ক্যুওয়াতা ইল্লা- বিল্লা-হ’’;\n" +
                            "পরে মুয়ায্যিন যখন বলে, ’’আল্লা-হু আকবার ’আল্লা-হু আকবার’’, সেও বলে, ’’আল্লা-হু আকবার, আল্লা-হু আকবার’’\n" +
                            "এরপর মুয়ায্যিন যখন বলে, ’’লা- ইলা-হা ইল্লাল্লা-হ,’’ সেও বলে ’’লা- ইলা-হা ইল্লাল্লা-হ’, [মুসলিম, মিশকাত হা/৬৫৮]\n\n\n" +
                            "যারা জবাব দেবেন না:\n\n" +
                            "নামাজরত, পানাহারে মগ্ন, মলমূত্র ত্যাগকারী ও স্ত্রীর সঙ্গে শারিরিক সম্পর্কে লিপ্ত ব্যক্তি আজানের জবাব দেবে না। ঋতুকালীন সময়ে নারীরাও আজানের উত্তর দেওয়া থেকে বিরত থাকবে।অনেক ফিকাহবিদদের মতে আজানের পরপর যদি উল্লিখিত কাজ থেকে অবসর হওয়া যায়, তাহলে সঙ্গে সঙ্গে আজানের জবাব দিয়ে দেওয়া উত্তম। কেউ কোরআন তেলাওয়াতে থাকলে সাময়িক তেলাওয়াত বন্ধ রেখে আজানের জবাব দেওয়া উত্তম।"
            ));
        } else {
            // English Items
            list.add(new SalahBasicsItem(
                    "Rakats and Timings of Five Daily Prayers",
                    "Fajr Prayer: Rakats: 2 Sunnah Muakkadah, 2 Fard. Time: From dawn until sunrise. [Muslim: 1456, 966] Dhuhr Prayer: Rakats: 4 Sunnah Muakkadah, 4 Fard, 2 Sunnah Muakkadah, 2 Nafl. Time: After the sun passes its zenith until Asr...",
                    "Fajr Prayer:\n\n" +
                            "Rakats: 2 Sunnah Muakkadah, 2 Fard.\n\n" +
                            "Time: From Subh Sadiq (dawn) until sunrise.\n\n" +
                            "[Muslim: 1456, 966]\n\n\n" +
                            "Dhuhr Prayer:\n\n" +
                            "Rakats: 4 Sunnah Muakkadah, 4 Fard, 2 Sunnah Muakkadah, 2 Nafl.\n\n" +
                            "Time: From the sun declining from zenith until Asr.\n\n" +
                            "[Bukhari: 717, 506, Muslim: 969]\n\n\n" +
                            "Asr Prayer:\n\n" +
                            "Rakats: 4 Sunnah Ghair Muakkadah, 4 Fard.\n\n" +
                            "Time: After Dhuhr prayer finishes until before the sun sets in the western sky.\n\n" +
                            "[Bukhari: 717, 545]\n\n\n" +
                            "Maghrib Prayer:\n\n" +
                            "Rakats: 3 Fard, 2 Sunnah Muakkadah, 2 Nafl.\n\n" +
                            "Time: From sunset until before Isha.\n\n" +
                            "[Muslim: 969, Daraqutni: 1066]\n\n\n" +
                            "Isha Prayer:\n\n" +
                            "Rakats: 4 Sunnah Ghair Muakkadah, 4 Fard, 2 Sunnah Muakkadah, 2 Nafl, 3 Witr (Wajib), 2 Nafl.\n\n" +
                            "Time: After Maghrib until the first third of the night.\n\n" +
                            "[Muslim: 969, Sahih Bukhari: 508, 538, 969]\n\n\n" +
                            "*Jumu'ah:\n\n" +
                            "Jumu'ah prayer is 2 Rakats Fard. Before it, upon entering the mosque and before sitting down, at least 2 Rakats 'Tahiyyatul Masjid', and after Jumu'ah 4 or 2 Rakats Sunnah. All the above are established by the regular practice of the Messenger of Allah (PBUH) and proven by Sahih Hadiths.\n\n" +
                            "[Sahih Ibn Khuzaymah 'Salah' chapter, Section 2; Nasa'i 'Salah' chapter-5, Section-3]."
            ));

            list.add(new SalahBasicsItem(
                    "What is Salah and Why We Must Pray",
                    "Salah (prayer) is one of the five pillars of Islam and essential in a Muslim's life. It establishes direct connection with Allah and purifies the soul. In the Quran, the commandment of Salah is mentioned numerous times...",
                    "Salah is the second pillar of Islam and the spiritual lifeline of a believer:\n\n" +
                            "1. Direct Connection with Allah:\nSalah is a direct dialogue between the servant and the Creator. Standing in prayer, the servant praises Allah, seeks guidance, and surrenders fully to the Lord.\n\n" +
                            "2. Quranic Commandment:\n'And establish prayer and give zakah and bow with those who bow.' (Surah Al-Baqarah: 43)\n\n" +
                            "3. Restraint from Sins:\n'Indeed, prayer prohibits immorality and wrongdoing.' (Surah Al-Ankabut: 45)\n\n" +
                            "4. Key to Paradise:\nThe Messenger of Allah (peace be upon him) said: 'Salah is the key to Paradise.' (Tirmidhi: 4)"
            ));

            list.add(new SalahBasicsItem(
                    "Why Salah is the Most Beloved Deed to Allah",
                    "After faith (Iman), Salah is the most critical obligation. The Messenger of Allah (PBUH) received repeated divine commandments regarding prayer. In the Quran, Allah directly mentions the word Salah 82 times...",
                    "After faith (Iman), Salah is the most critical obligation. The Messenger of Allah (peace be upon him) received repeated divine commandments regarding prayer. In the Holy Quran, Allah Almighty directly mentions the word Salah 82 times, highlighting the supreme importance of prayer.\n\n\n" +
                            "Narrated by Hazrat Abdullah ibn Mas'ud (RA):\n\n" +
                            "I asked the Messenger of Allah (peace be upon him): '(O Messenger of Allah!) Which deed is most beloved to Allah?' The Messenger of Allah (peace be upon him) replied, 'Prayer (Salah).'"
            ));

            list.add(new SalahBasicsItem(
                    "The Direction of Facing in Salah (Qibla)",
                    "The Holy Kaaba in Makkah is the second and permanent Qibla of Muslims. Facing toward the Kaaba is a fundamental condition for the validity of Salah from any corner of the globe...",
                    "Facing the Qibla (Al-Masjid Al-Haram in Makkah) is an obligatory prerequisite for Salah:\n\n" +
                            "1. Current Universal Qibla:\nThe Holy Ka'aba in Makkah is the universal direction of prayer for Muslims worldwide.\n\n" +
                            "2. Divine Command in Quran:\n'So turn your face toward al-Masjid al-Haram. And wherever you [believers] are, turn your faces toward it [in prayer].' (Surah Al-Baqarah: 144)\n\n" +
                            "3. History of Qibla Shift:\nEarly Muslims initially prayed facing Bayt al-Maqdis (Jerusalem). Approximately 16–17 months after Hijrah, the Qibla was permanently turned toward the Ka'aba.\n\n" +
                            "4. When Qibla is Unknown:\nIf a traveler cannot ascertain the direction, he exercises best judgment (Ijtihad) and prays in the direction deemed most probable."
            ));

            list.add(new SalahBasicsItem(
                    "When Salah Becomes Obligatory",
                    "When does Salah become obligatory in a person's life? It is essential to know this. As age increases, the performance of prayer must be established. A person becomes obligated to pray once certain conditions are fulfilled...",
                    "When does Salah become obligatory in a person's life? It is essential to know this. Although our age increases day by day, our dedication to performing Salah often does not increase accordingly. In many cases, this matter is neglected—which is never acceptable for a Muslim! Let us learn from when Salah must be performed. Primarily, when a person fulfills certain conditions, Salah becomes obligatory upon them:\n\n\n" +
                            "Being a Muslim:\n\n" +
                            "The first and foremost prerequisite for Salah to be obligatory is that the person must be a Muslim. This obligation does not apply to non-believers. In the Holy Quran, Allah Almighty states:\n\n\n" +
                            "وَ مَنۡ یَّبۡتَغِ غَیۡرَ الۡاِسۡلَامِ دِیۡنًا فَلَنۡ یُّقۡবَلَ مِنۡ\n\n" +
                            "Translation:\n" +
                            "And whoever desires other than Islam as religion - never will it be accepted from him.\n\n" +
                            "[Surah Ali 'Imran: 3:85; At-Tawbah: 9:17]\n\n\n" +
                            "Being of Sound Mind (Sane):\n\n" +
                            "Salah is not obligatory upon an insane person or someone who lacks sound intellect. If a person possesses sound mental faculty, Salah becomes obligatory upon them. The Messenger of Allah (peace be upon him) said:\n\n\n" +
                            "رُفِعَ الْقَلَمُ عَنْ ثَلَاثَةٍ: عَنِ النَّائِمِ حَتَّى يَسْتَيْقِظَ وَعَنِ الصَّبِيِّ حَتَّى يَبْلُغَ وَعَنِ الْمَعْتُوهِ حَتَّى يعقل\n\n" +
                            "Translation:\n" +
                            "Three persons are exempt from accountability (on the Day of Judgment):\n\n" +
                            "The sleeping person until he wakes up,\n" +
                            "The child until he reaches puberty, and\n" +
                            "The insane person until he regains his sanity/intellect.\n\n" +
                            "[Tirmidhi, Abu Dawood, Mishkat: 3287]\n\n\n" +
                            "Attaining Puberty (Maturity):\n\n" +
                            "When a Muslim person attains the age of puberty (maturity), Salah becomes strictly obligatory upon them. This is because the pen of accountability is lifted from children, meaning sins are not recorded against them. Therefore, it is obligatory upon mature individuals, which should be practiced from early childhood. The Messenger of Allah (peace be upon him) said:\n\n\n" +
                            "مُرُوا أَوْلَادَكُمْ بِالصَّلَاةِ وَهُمْ أَبْنَاءُ سَبْعِ سِنِينَ وَاضْرِبُوهُمْ عَلَيْهَا وَهُمْ أَبْنَاءُ عَشْرٍ سِنِين وَفَرِّقُوا بَيْنَهُمْ فِي الْمَضَاجِعِ\n\n" +
                            "Translation:\n" +
                            "Command your children to pray when they reach seven years of age, and discipline them for [missing] it when they reach ten years of age, and separate them in their sleeping places.\n\n" +
                            "[Ahmad, Abu Dawood, Tirmidhi, Mishkat: 572; Nayl al-Awtar 2/22]"
            ));

            list.add(new SalahBasicsItem(
                    "How the 5 Daily Prayers Were Ordained",
                    "The detailed account of how the five daily prayers became obligatory is directly connected with one of the most momentous events in Islam—the Night of Miraj (Ascension)...",
                    "The detailed account of how the five daily prayers became obligatory is directly connected with one of the most momentous events in Islam—the Night of Miraj (Ascension). The event of Miraj is one of the most pivotal milestones in the life of Prophet Muhammad (peace be upon him), where the five daily prayers were ordained upon the Muslims. Here is the context and explanation of how Salah was ordained:\n\n\n" +
                            "How Five Daily Prayers Were Ordained - Background of the Night of Miraj:\n\n" +
                            "The event of Miraj took place prior to the Hijrah. It occurred in the 10th year of Prophethood, known as the 'Year of Sorrow' (Amul Huzn). In this year, the Prophet's beloved wife Khadijah (RA) and his uncle Abu Talib passed away. Allah caused the event of Miraj to comfort the Prophet (peace be upon him) and as a special divine gift for his Ummah.\n\n\n" +
                            "Beginning of Miraj (Al-Isra):\n\n" +
                            "The night of Miraj took place on the 27th night of Rajab. The Prophet (peace be upon him) was resting near the Kaaba. By Allah's command, Angel Jibreel (AS) arrived and took him on a heavenly mount called Buraq from Al-Masjid Al-Haram (Makkah) to Al-Masjid Al-Aqsa (Jerusalem). This journey is called Isra. There, the Prophet (peace be upon him) led all the previous Prophets in congregational prayer.\n\n\n" +
                            "Ascension to Sidratul Muntaha (Al-Miraj):\n\n" +
                            "From Al-Masjid Al-Aqsa, Angel Jibreel (AS) accompanied the Prophet (peace be upon him) up to Sidratul Muntaha (the utmost boundary above the Seventh Heaven). From there, the Prophet (peace be upon him) was the only creation to reach the Divine Presence at the Glorious Throne (Arsh).\n\n\n" +
                            "Ordination of Salah:\n\n" +
                            "Allah Almighty communicated directly with the Prophet (peace be upon him) and initially prescribed 50 prayers per day for Muslims. Afterward, the Prophet met Prophet Musa (AS). Prophet Musa (AS) advised: 'Your Ummah will not be able to bear 50 prayers daily. Return to your Lord and ask for a reduction.' The Prophet returned to Allah repeatedly requesting reduction. Eventually, it was established as 5 daily prayers, while retaining the full spiritual reward of 50 prayers.\n\n\n" +
                            "Importance of the Five Daily Prayers:\n\n" +
                            "The paramount importance of Salah was established on the Night of Miraj. It is an act of worship prescribed directly through divine speech without intermediate revelation. Prayer is the believer's direct lifeline and connection with Allah.\n\n\n" +
                            "Divine Commandment in Quran:\n\n\n" +
                            "إِنَّ ٱلصَّلَوٰةَ كَانَتْ عَلَى ٱلْمُؤْمِنِينَ كِتَـٰبًۭا مَّوْقُوتًۭا\n\n" +
                            "Translation:\n" +
                            "Indeed, prayer has been decreed upon the believers a decree of specified times.\n\n" +
                            "[Surah An-Nisa: 4:103]\n\n\n" +
                            "أَوَّلُ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ الصَّلَاةُ، فَإِنْ صَلَحَتْ، صَلَحَ سَائِرُ عَمَلِهِ، وَإِنْ فَسَدَتْ، فَسَدَ سَائِرُ عَمَلِهِ\n\n" +
                            "Translation:\n" +
                            "The first matter that the servant will be brought to account for on the Day of Judgment is the prayer. If it is sound, then the rest of his deeds will be sound. And if it is corrupt, then the rest of his deeds will be corrupt.\n\n" +
                            "[Tirmidhi, Hadith: 413]\n\n\n" +
                            "Salah is the key to Paradise and the primary cornerstone of a Muslim's life. It embodies self-purification, patience, and supreme submission to Allah. The Messenger of Allah (peace be upon him) stated:\n\n\n" +
                            "مِفْتَاحُ الْجَنَّةِ الصَّلَاةُ\n\n" +
                            "Translation:\n" +
                            "The key to Paradise is prayer.\n\n" +
                            "[Sahih Muslim, Hadith: 487]"
            ));

            list.add(new SalahBasicsItem(
                    "What We Should Say in Response to Adhan",
                    "What we should say in response to the Adhan. Regarding responding to the Adhan, the Messenger of Allah (peace be upon him) said: When you hear the Mu'adhin, say what he says...",
                    "What we should say in response to the Adhan. Regarding responding to the Adhan, the Messenger of Allah (peace be upon him) said:\n\n\n" +
                            "إِذَا سَمِعْتُمُ الْمُؤَذِّنَ فَقُولُوا مِثْلَ مَا يَقُولُ\n\n" +
                            "Translation:\n" +
                            "When you hear the Mu'adhin (caller to prayer), say similar to what he says.\n\n" +
                            "[Muslim, Mishkat Hadith: 657 'Salah' Chapter-4]\n\n\n" +
                            "Narrated by Umar (RA): He said that the Messenger of Allah (peace be upon him) said:\n\n" +
                            "What to say in response to Adhan:\n\n\n" +
                            "وَعَنْ عُمَرَ قَالَ: قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «إِذَا قَالَ الْمُؤَذِّنُ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ فَقَالَ أَحَدُكُمُ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ ثُمَّ قَالَ أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ قَالَ أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ ثُمَّ قَالَ أَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ قَالَ أَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ ثُمَّ قَالَ حَيَّ عَلَى الصَّلَاةِ قَالَ لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ ثُمَّ قَالَ حَيَّ عَلَى الْفَلَاحِ قَالَ لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ ثُمَّ قَالَ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ قَالَ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ ثُمَّ قَالَ لَا إِلَهَ إِلَّا اللَّهُ قَالَ لَا إِلَهَ إِلَّا اللَّهُ مِنْ قَلْبِهِ دخل الْجنَّة\n\n" +
                            "Translation:\n" +
                            "When the Mu'adhin says 'Allahu Akbar, Allahu Akbar' and one of you says from the heart 'Allahu Akbar, Allahu Akbar';\n" +
                            "Then when he says 'Ashhadu alla ilaha illallah' and he replies 'Ashhadu alla ilaha illallah';\n" +
                            "Then when he says 'Ashhadu anna Muhammadar Rasulullah' and he replies 'Ashhadu anna Muhammadar Rasulullah';\n" +
                            "Then when he says 'Hayya 'alas-Salah' and he replies 'La hawla wa la quwwata illa billah';\n" +
                            "Then when he says 'Hayya 'alal-Falah' and he replies 'La hawla wa la quwwata illa billah';\n" +
                            "Then when he says 'Allahu Akbar, Allahu Akbar' and he replies 'Allahu Akbar, Allahu Akbar';\n" +
                            "Then when he says 'La ilaha illallah' and he replies sincerely from his heart 'La ilaha illallah'—he shall enter Paradise.\n\n" +
                            "[Muslim, Mishkat Hadith: 658]\n\n\n" +
                            "Who Should Not Respond:\n\n" +
                            "A person who is engaged in prayer, actively eating or drinking, relieving oneself, or engaging in marital intimacy should not respond to the Adhan. Women during menstruation should also refrain from responding to the Adhan. According to many jurists, if one finishes the aforementioned activities immediately after the Adhan, it is recommended to give the response right away. If someone is reciting the Quran, it is recommended to pause recitation temporarily and respond to the Adhan."
            ));
        }

        return list;
    }
}
