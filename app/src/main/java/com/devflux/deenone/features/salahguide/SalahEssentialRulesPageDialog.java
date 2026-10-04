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

public class SalahEssentialRulesPageDialog {

    public static class EssentialRuleItem {
        public final String title;
        public final String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public EssentialRuleItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "সঠিক ভাবে সালাত আদায়ের জন্য যা জানা জরুরি" : "Essential Rules of Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick options
        });

        List<EssentialRuleItem> items = getEssentialRuleItems(isBn);

        EssentialRulesAdapter adapter = new EssentialRulesAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class EssentialRulesAdapter extends RecyclerView.Adapter<EssentialRulesAdapter.ViewHolder> {
        private final Context context;
        private final List<EssentialRuleItem> items;
        private final boolean isBn;

        public EssentialRulesAdapter(Context context, List<EssentialRuleItem> items, boolean isBn) {
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
            EssentialRuleItem item = items.get(position);
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
                        EssentialRuleItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(EssentialRuleItem item, boolean isBn) {
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

    public static List<EssentialRuleItem> getEssentialRuleItems(boolean isBn) {
        List<EssentialRuleItem> list = new ArrayList<>();

        if (isBn) {
            // ১. কাতার সোজা করার নিয়ম
            list.add(new EssentialRuleItem(
                    "কাতার সোজা করার নিয়ম",
                    "কাতার সোজা করার নিয়ম হলো, সকল নামাযী টাখনু এবং পায়ের গোড়ালি পেছনের দিক থেকে বরাবর করে দাঁড়ালে কাতার সোজা হবে। সামনের দিক থেকে আঙ্গুল বরাবর করার প্রয়োজন নেই। কেননা, পা লম্বা বা বেঁটে হওয়ার কারণে পায়ের অগ্রভাগ আগে পি...",
                    "কাতার সোজা করার নিয়ম হলো, সকল নামাযী টাখনু এবং পায়ের গোড়ালি পেছনের দিক থেকে বরাবর করে দাঁড়ালে কাতার সোজা হবে। সামনের দিক থেকে আঙ্গুল বরাবর করার প্রয়োজন নেই। কেননা, পা লম্বা বা বেঁটে হওয়ার কারণে পায়ের অগ্রভাগ আগে পিছে থাকলে তাতে কোনো অসুবিধা নেই।\n\n" +
                            "[আল বাহরুর রায়েক: খণ্ড-১, পৃষ্ঠা-৩৫৩]\n\n\n" +
                            "জামাতের নামাযে টাখনু টাখনুর সাথে এবং কাঁধ কাঁধের সাথে মিলিয়ে দাঁড়ালে কাতার সোজা হবে। টাখনু এবং পায়ের গোড়ালি বরাবর সমান করে দাঁড়াবে। সামনের দিক থেকে আঙ্গুল বরাবর সমান করার প্রয়োজন নেই।"
            ));

            // ২. কাতারে জায়গা না থাকলে
            list.add(new EssentialRuleItem(
                    "কাতারে জায়গা না থাকলে",
                    "যদি কাতারে জায়গা না থাকে, তাহলে ইমামের রুকু করা পর্যন্ত অপেক্ষা করবে। যদি কেউ এসে যায়, তাহলে তাকে নিয়ে ইমামের বরাবর পেছনের কাতারে দাঁড়াবে। যদি কেউ না আসে, তাহলে একাকীই ইমামের বরাবর পেছনের কাতারে দাঁড়িয়ে যাবে। [ফাতাওয়া শা...",
                    "যদি কাতারে জায়গা না থাকে, তাহলে ইমামের রুকু করা পর্যন্ত অপেক্ষা করবে। যদি কেউ এসে যায়, তাহলে তাকে নিয়ে ইমামের বরাবর পেছনের কাতারে দাঁড়াবে। যদি কেউ না আসে, তাহলে একাকীই ইমামের বরাবর পেছনের কাতারে দাঁড়িয়ে যাবে।\n\n" +
                            "[ফাতাওয়া শামী: খণ্ড-১, পৃষ্ঠা-৫৬৮, আল বাহরুর রায়েক: খণ্ড-১, পৃষ্ঠা-৬১৭, খণ্ড-১, পৃষ্ঠা- ৩৫৩]"
            ));

            // ৩. কাতারে দাঁড়ানোর নিয়ম
            list.add(new EssentialRuleItem(
                    "কাতারে দাঁড়ানোর নিয়ম",
                    "লোকদের উচিত যখন জামাতের নামাযের জন্য কাতারে দাঁড়াবে, তখন মিলেমিশে দাঁড়াবে এবং খালি জায়গা পূরণ করে দাঁড়াবে। একজন অপর জনের সাথে মিলে পরস্পরে কাঁধ বরাবর করে কাতারে দাঁড়াবে...",
                    "লোকদের উচিত যখন জামাতের নামাযের জন্য কাতারে দাঁড়াবে, তখন মিলেমিশে দাঁড়াবে এবং খালি জায়গা পূরণ করে দাঁড়াবে। একজন অপর জনের সাথে মিলে পরস্পরে কাঁধ বরাবর করে কাতারে দাঁড়াবে।\n\n" +
                            "[মুহীতে বুরহানী: খণ্ড-২, পৃষ্ঠা-২০২, আদ দুররুল মুখতার মা'আ রদ: খণ্ড-২, পৃষ্ঠা-৫৬৮, আল বাহরুর রায়েক: খণ্ড-১, পৃষ্ঠা-৬১৮-৬১৯, আবু দাউদ: খণ্ড-১, পৃষ্ঠা-১০৬]"
            ));

            // ৪. সালাতের জন্য হাত বাধার নিয়ম
            list.add(new EssentialRuleItem(
                    "সালাতের জন্য হাত বাধার নিয়ম",
                    "রাসূলুল্লাহ (স) তাঁর নামাযে বুকের উপর হাত রাখতেন। তাঁর সাহাবী ওয়াইল বিন হুজুর (রা) বর্ণনা করেন, তিনি নবী (স)-এর সাথে সালাত আদায় করেছেন...",
                    "রাসূলুল্লাহ (স) তাঁর নামাযে বুকের উপর হাত রাখতেন। তাঁর সাহাবী ওয়াইল বিন হুজুর (রা) থেকে বর্ণিত, তিনি বলেন,\n\n" +
                            "\"আমি নবী (স)-এর সাথে সালাত আদায় করেছি। (আমি দেখেছি) তিনি (স.) তাঁর বুকের উপর ডান হাত বাম হাতের উপর রেখেছেন।\"\n\n" +
                            "[সহীহ ইবনু খুযাইমা, পৃষ্ঠা ২, আবু দাউদ: ৭৫৯, আলবানী]\n\n" +
                            "অপর এক সাহাবী সাহল ইবনে সা'দ (রা) থেকে বর্ণিত, তিনি বলেন:\n" +
                            "\"সালাতে লোকদেরকে ডান হাত বাম হাতের বাহুর উপর রাখার নির্দেশ দেওয়া হতো।\"\n\n" +
                            "[বুখারী: ৭৪০, ইফা: ৭০৪]\n\n" +
                            "আরবীতে ‘কাফ্ফ’ শব্দের অর্থ হলো কব্জি পর্যন্ত হাত, আর ‘যিরা’ অর্থ হলো হাতের আঙুলের অগ্রভাগ থেকে কনুই পর্যন্ত পূর্ণ এক হাত বা বাহু। অতএব, উক্ত হাদীসের মর্ম ও নির্দেশ অনুযায়ী ডান হাতের আঙুলগুলো বাম কনুইয়ের কাছাকাছি থাকবে।\n\n" +
                            "[আবু দাউদ: ৭৫৯ আলবানী]\n\n" +
                            "কেউ কেউ বলেছেন, নাভির নিচে হাত বাঁধা সুন্নাত। তবে নাভির নিচে হাত বাঁধার হাদীসগুলো দুর্বল।\n\n" +
                            "[আলবানীর যঈফ আবু দাউদ: ৭৫৬, ৭৫৮]\n\n" +
                            "অতএব, সহীহ হাদীস বাদ দিয়ে এর বিপরীতে দুর্বল হাদীস আমল করা জায়েয নেই। আবার কেউ কেউ বলেন, নারীরা বুকে বাঁধবে আর পুরুষেরা বাঁধবে নাভির নিচে। এ বিষয়ে আল্লাহর রাসূল (স) যেখানে কোনো পার্থক্য করেননি সেখানে আলেমগণ কীভাবে পার্থক্য করেন? স্বতঃসিদ্ধ বিষয় হলো, এ হাদীসের নির্দেশ পুরুষ ও নারী সকলের জন্য সমানভাবে প্রযোজ্য। অর্থাৎ সহীহ হাদীসের আলোকে প্রমাণিত যে, বুকের উপর হাত রাখা নারী-পুরুষ সকলের জন্যই সুন্নাত।"
            ));

            // ৫. সঠিকভাবে রুকু করার নিয়ম
            list.add(new EssentialRuleItem(
                    "সঠিকভাবে রুকু করার নিয়ম",
                    "‘রুকূ’ অর্থ মাথা ঝুঁকানো; পারিভাষিক অর্থ, ‘শারঈ তরীকায় আল্লাহর সম্মুখে মাথা ঝুঁকানো’। সালাতে রুকূ করা আবশ্যক একটি ফরজ রুকন...",
                    "‘রুকূ’ অর্থ মাথা ঝুঁকানো; পারিভাষিক অর্থ, ‘শারঈ তরীকায় আল্লাহর সম্মুখে মাথা ঝুঁকানো’। সালাতে রুকূ করা আবশ্যক একটি ফরজ কাজ, যা ছুটে গেলে সালাত বাতিল হয়ে যাবে। ক্বিরাআত শেষে মহাপ্রভু আল্লাহর সম্মুখে শ্রদ্ধার সাথে মাথা ও পিঠ ঝুঁকিয়ে রুকূতে যেতে হয়।\n\n" +
                            "রুকূর সঠিক পদ্ধতি ও নিয়মাবলী:\n" +
                            "• রুকূতে যাওয়ার সময় ‘আল্লাহু আকবার’ বলে তাকবীরের সাথে দুই হাত কাঁধ পর্যন্ত সোজাভাবে উঠাবে।\n" +
                            "• অতঃপর দুই হাতের আঙুলগুলো খোলা রেখে দুই হাঁটুর উপরে ভর দিয়ে রুকূ করবে। এসময় বাহুকে খুঁটির মতো শক্ত করে টানটান করে রাখতে হবে।\n" +
                            "• অর্ধনমিত অবস্থায় মাথা উপরের দিকে বেশি উঠিয়েও নয় আবার নীচু করেও নয়; বরং পিঠ বরাবর সোজা করে রাখতে হবে।\n" +
                            "• দৃষ্টি থাকবে সিজদার স্থানে।\n" +
                            "• হাঁটু একদম সোজা অবস্থায় থাকবে।\n\n" +
                            "[বায়হাক্বী, হাকেম, ইবনু মাজাহ: ৮৭২]"
            ));

            // ৬. রুকুতে গিয়ে যা পড়তে হয়
            list.add(new EssentialRuleItem(
                    "রুকুতে গিয়ে যা পড়তে হয়",
                    "রুকুর জন্য হাদিসে একাধিক মাসনুন দোয়া বর্ণিত হয়েছে। তন্মধ্যে বহুল প্রচলিত দোয়া হলো: ‘সুবহানা রব্বিয়াল ‘আযীম’...",
                    "রুকূর জন্য হাদীসে একাধিক মাসনূন দো‘আ বর্ণিত হয়েছে।\n\n" +
                            "১. বহুল প্রচলিত মূল দো‘আ:\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-না রব্বিয়াল ‘আযীম।\n\n" +
                            "অর্থ:\n" +
                            "মহা পবিত্র আমার প্রতিপালক, যিনি অতি মহান।\n\n" +
                            "[আবুদাঊদ, তিরমিযী, মিশকাত হা/৮৮১]\n\n" +
                            "এই দো‘আ কমপক্ষে তিনবার পড়বে। বেশির কোনো নির্দিষ্ট সীমা নেই—যতবার খুশি পড়তে পারেন।\n" +
                            "[আহমাদ, আবুদাঊদ হা/৮৮৫, ইবনু মাজাহ হা/৮৮৮, আলবানী: সিফাতুস সালাহ, পৃ. ১১৩]\n\n" +
                            "(উল্লেখ্য যে, সর্বোচ্চ দশবার পড়ার হাদীসটি ‘যঈফ’)।\n" +
                            "[তিরমিযী, আবুদাঊদ, নাসাঈ, মিশকাত হা/৮৮০, ৮৮৩]\n\n" +
                            "— — —\n\n" +
                            "২. রাসূলুল্লাহ (সা.)-এর জীবনের শেষদিকের বিশেষ দো‘আ:\n" +
                            "রাসূলুল্লাহ (সা.) জীবনের শেষভাগে রুকূ ও সিজদায় অধিকাংশ সময় এই দো‘আটি পড়তেন:\n\n" +
                            "سُبْحَانَكَ اللَّهُمَّ رَبَّنَا وَبِحَمْدِكَ، اَللَّهُمَّ اغْفِرْ لِيْ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-নাকা আল্লা-হুম্মা রব্বানা ওয়া বিহামদিকা, আল্লা-হুম্মাগফির লী।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ, হে আমাদের প্রতিপালক! আপনার প্রশংসার সাথে আপনার পবিত্রতা ঘোষণা করছি। হে আল্লাহ! আপনি আমাকে ক্ষমা করুন।\n\n" +
                            "[সহীহ বুখারী, সহীহ মুসলিম, মিশকাত হা/৮৭১, নায়লুল আওত্বার ৩/১০৬]\n\n" +
                            "— — —\n\n" +
                            "৩. রুকূর অন্যান্য মাসনূন দো‘আ সমূহ:\n\n" +
                            "(ক)\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-না রব্বিয়াল ‘আযীমি ওয়া বিহামদিহী (তিন বার)।\n" +
                            "[আবু দাউদ: ৮৭০]\n\n" +
                            "(খ)\n" +
                            "سُبُّوْحٌ قُدُّوْسٌ رَبُّ الْمَلَائِكَةِ وَالرُّوْحِ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুব্বূহুন কুদ্দূসুন রব্বুল মালা-ইকাতি ওয়ার রূহ।\n\n" +
                            "অর্থ:\n" +
                            "সকল ত্রুটি থেকে পরম পবিত্র ও মহিমান্বিত, যিনি ফেরেশতা ও জিবরাঈল (আ.)-এর প্রতিপালক।\n" +
                            "[সহীহ মুসলিম: ৪৮৭]\n\n" +
                            "(গ)\n" +
                            "اَللَّهُمَّ لَكَ رَكَعْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ، خَشَعَ لَكَ سَمْعِيْ وَبَصَرِيْ وَمُخِّيْ وَعَظْمِيْ وَعَصَبِيْ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মা লাকা রকা'তু, ওয়া বিকা আ-মানতু, ওয়া লাকা আসলামতু, খাশা'আ লাকা সাম'ঈ ওয়া বাসরী ওয়া মুখ্খী ওয়া 'আজমী ওয়া 'আসাবী।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আপনার জন্যই রুকূ করলাম, আপনার প্রতি ঈমান আনলাম এবং আপনার নিকট আত্মসমর্পণ করলাম। আপনারই সম্মুখে বিনম্র হলো আমার শ্রবণশক্তি, দৃষ্টিশক্তি, মস্তিষ্ক, অস্থি ও স্নায়ুমণ্ডলী।\n" +
                            "[সহীহ মুসলিম: ৭৭১]\n\n" +
                            "(ঘ)\n" +
                            "اَللَّهُمَّ لَكَ رَكَعْتُ وَبِكَ أَسْلَمْتُ وَعَلَيْكَ تَوَكَّلْتُ، أَنْتَ رَبِّي خَشَعَ سَمْعِيْ وَبَصَرِيْ وَدَمِيْ وَلَحْمِيْ وَعَظْمِيْ وَعَصَبِيْ لِلَّهِ رَبِّ الْعَالَمِيْنَ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মা লাকা রকা'তু ওয়া বিকা আসলামতু ওয়া 'আলাইকা তাওয়াক্কালতু, আনতা রব্বী, খাশা'আ সাম'ঈ ওয়া বাসরী ওয়া দামী ওয়া লাহমী ওয়া 'আজমী ওয়া 'আসাবী লিল্লাহি রব্বিল 'আলামীন।\n" +
                            "[সুনানুন নাসাঈ: ১০৫১]\n\n" +
                            "(ঙ)\n" +
                            "سُبْحَانَ ذِي الْجَبَرُوْتِ وَالْمَلَكُوْتِ وَالْكِبْرِيَاءِ وَالْعَظْمَةِ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-না যিল জাবারূতি ওয়াল মালাকূতি ওয়াল কিবরিয়া-ই ওয়াল ‘আযমাহ।\n\n" +
                            "অর্থ:\n" +
                            "মহা পবিত্র তিনি, যিনি অপার পরাক্রম, সুবিশাল সাম্রাজ্য, অহংকার ও মহান শ্রেষ্ঠত্বের অধিকারী।\n" +
                            "[আবু দাউদ: ৮৭৩, নাসাঈ: ১০৪৯, মিশকাত: ৮৭৫]"
            ));

            // ৭. সঠিকভাবে সিজদা করার নিয়ম
            list.add(new EssentialRuleItem(
                    "সঠিকভাবে সিজদা করার নিয়ম",
                    "নবী করীম (সা.) সিজদারত অবস্থায় হাতের তালু মাটিতে বিছিয়ে সাতটি অঙ্গের উপর ভর দিয়ে সিজদা করতেন...",
                    "নবী করীম (সা.) সিজদারত অবস্থায় হাতের তালু মাটিতে বিছিয়ে রাখতেন।\n" +
                            "[সহীহ বুখারী: ৮২৮, ইফা: ৭৯০, আধুনিক: ৭৮২]\n\n" +
                            "রাসূলুল্লাহ (সা.) সাতটি অঙ্গের উপর ভর দিয়ে সিজদা করতেন:\n" +
                            "[সহীহ বুখারী: ৮১২, সহীহ মুসলিম: ৪৯০]\n\n" +
                            "১. কপাল ও নাক (তিনি হাত দিয়ে নাকের প্রতি ইশারা করে নাককে কপালের অন্তর্ভুক্ত করেন)\n" +
                            "[সহীহ বুখারী: ৮১২, ইফা: ৭৭৫, আধুনিক: ৭৬৭]\n" +
                            "২. উভয় হাত\n" +
                            "৩. উভয় হাঁটু\n" +
                            "৪. উভয় পায়ের আঙুলসমূহের অগ্রভাগ।\n\n" +
                            "সিজদার সঠিক পদ্ধতি ও নিয়মাবলী:\n" +
                            "• কপালের মতো নাকও মাটিতে স্থিরভাবে রাখতে হবে। তিনি (সা.) হাতের আঙুলগুলো সোজা ও একত্রিত করে কিবলামুখী করে রাখতেন এবং দুই পায়ের গোড়ালি একত্রে ভালোভাবে মিলিয়ে রাখতেন।\n" +
                            "[সহীহ ইবনে খুযাইমা: ৬৫৪]\n\n" +
                            "• সিজদার সময় মুখমণ্ডল দুই হাতের মধ্যবর্তী স্থানে কাঁধ বা কান বরাবর থাকবে এবং কব্জি থেকে কনুই পর্যন্ত বাহু যমীন থেকে উপরে উঠিয়ে রাখবে। সাবধান! কুকুরের মতো কনুই পর্যন্ত হাত দুটো যমীনে বিছিয়ে দেওয়া যাবে না।\n" +
                            "[সহীহ বুখারী: ৮২২, আধুনিক: ৭৭৬]\n\n" +
                            "• দুই পায়ের আঙুলে ভর করে হাঁটু দাঁড় করিয়ে রাখবে। কনুই ও বগল ফাঁকা থাকবে। সিজদা সুন্দর ও শান্ত হবে এবং পিঠ সোজা থাকবে। আল্লাহ ঐ বান্দার সালাতের দিকে তাকান না, যে ব্যক্তি তার সালাতে রুকু ও সিজদায় নিজের মেরুদণ্ড সোজা করে না।\n" +
                            "[মুসনাদে আহমাদ: ২/৫২৫, আল-মু'জামুল কাবীর (ত্বাবারানী): ৮/৩৩৮]\n\n" +
                            "• রাসূলুল্লাহ (সা.) পেট থেকে উরু এতটুকু পরিমাণ দূরে ও ফাঁকা রাখতেন, যাতে উক্ত ফাঁকা অংশ দিয়ে একটি ছাগলছানা যাতায়াত করতে পারে।\n" +
                            "[সহীহ মুসলিম: ৪৯৬]\n\n" +
                            "• এছাড়া দুই উরুর মাঝখানে পরিমিত ফাঁকা থাকবে। পুরুষ ও মহিলা উভয়েই ঠিক একই পদ্ধতিতে রাসূলুল্লাহ (সা.)-এর সুন্নাহ অনুযায়ী সিজদা করবে। মেয়েদের আলাদা বা বুক-পেট মাটির সাথে লেপ্টে সিজদা করার কোনো সহীহ হাদীস নেই; বরং এমনভাবে বিছিয়ে সিজদা করাকে রাসূলুল্লাহ (সা.) কুকুরের বসার সাথে তুলনা করে নিষেধ করেছেন। হযরত আনাস বিন মালেক (রা.) বর্ণিত হাদীসে রাসূলুল্লাহ (সা.) ইরশাদ করেন:\n\n" +
                            "\"সিজদার সময় তোমাদের কেউই যেন দুই বাহু যমীনে বিছিয়ে না দেয়, যেমনভাবে বিছিয়ে দেয় কুকুর।\"\n\n" +
                            "[সহীহ বুখারী: ৮২২, ইফা: ৭৮৪, আধুনিক: ৭৭৬]"
            ));

            // ৮. সিজদায় গিয়ে যা পড়তে হয়
            list.add(new EssentialRuleItem(
                    "সিজদায় গিয়ে যা পড়তে হয়",
                    "সিজদা হলো দোয়া কবুলের সর্বোত্তম সময়। রাসূলুল্লাহ (সা.) ইরশাদ করেছেন: বান্দা স্বীয় প্রভুর সর্বাধিক নিকটে পৌঁছে যায়, যখন সে সিজদায় রত হয়...",
                    "সিজদা হলো দো‘আ কবুলের সর্বোত্তম সময়। হযরত আবু হুরায়রা (রা.) বর্ণিত, রাসূলুল্লাহ (সা.) ইরশাদ করেন:\n\n" +
                            "أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ\n\n" +
                            "অর্থ:\n" +
                            "\"বান্দা স্বীয় প্রভুর সর্বাধিক নিকটে পৌঁছে যায় যখন সে সিজদায় রত থাকে। অতএব তোমরা ঐ সময় বেশি বেশি দো‘আ করো।\"\n\n" +
                            "অন্য বর্ণনায় এসেছে: \"তোমরা দো‘আয় সাধ্যমতো চেষ্টা করো। আশা করা যায়, তোমাদের দো‘আ কবুল হবে।\"\n" +
                            "[সহীহ মুসলিম: ৪৮২, মিশকাত হা/৮৯৪, অনুচ্ছেদ-১৪]\n\n" +
                            "তিনি আরও বলেন, রুকূ ও সিজদাতে কমপক্ষে তিনবার তাসবীহ পাঠ করবে।\n" +
                            "[ইবনু মাজাহ হা/৮৮৮]\n\n" +
                            "— — —\n\n" +
                            "১. সিজদার বহুল প্রচলিত মূল তাসবীহ:\n" +
                            "سُبْحَانَ رَبِّىَ الْأَعْلَى\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-না রব্বিয়াল আ'লা।\n\n" +
                            "অর্থ:\n" +
                            "মহা পবিত্র আমার প্রতিপালক, যিনি সর্বোচ্চ।\n\n" +
                            "(এই দো‘আটি কমপক্ষে ৩ বার পাঠ করা সুন্নাত, তবে তিনবারের বেশি বিজোড় সংখ্যায় যত বেশি ইচ্ছা পড়া যায়)।\n" +
                            "[আবু দাউদ: ৮৭০, তিরমিযী: ২৬১]\n\n" +
                            "— — —\n\n" +
                            "২. সিজদার অন্যান্য সহীহ মাসনূন দো‘আ সমূহ:\n\n" +
                            "(ক) গুনাহ মাফের দো‘আ:\n" +
                            "اَللَّهُمَّ اغْفِرْ لِيْ ذَنْبِيْ كُلَّهُ دِقَّهُ وَجِلَّهُ وَ أَوَّلَهُ وَآخِرَهُ وَعَلاَنِيَتَهُ وَسِرَّهُ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মাগফির লী যানবী কুল্লাহু দিক্কাহূ ওয়া জিল্লাহূ ওয়া আওওয়ালাহূ ওয়া আ-খিরাহূ ওয়া 'আলানিয়াতাহূ ওয়া সির্রাহু।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আপনি আমার সমস্ত গুনাহ ক্ষমা করে দিন—ছোট ও বড়, আগের ও পরের, প্রকাশ্য ও গোপনীয়।\n" +
                            "[সহীহ মুসলিম: ৪৮৩]\n\n" +
                            "(খ)\n" +
                            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ لآ إِلَهَ إِلَّا أَنْتَ\n\n" +
                            "উচ্চারণ:\n" +
                            "সুবহা-নাকাল্লা-হুম্মা ওয়া বিহামদিকা লা-ইলা-হা ইল্লা আনতা।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আপনার প্রশংসার সাথে আপনার পবিত্রতা ঘোষণা করছি, আপনি ছাড়া কোনো সত্য উপাস্য নেই।\n" +
                            "[সহীহ মুসলিম: ৪৮৫]\n\n" +
                            "(গ)\n" +
                            "اَللَّهُمَّ اغْفِرْ لِيْ مَا أَسْرَرْتُ وَمَا أَعْلَنْتُ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মাগফির লী মা-আসরারতু ওয়া মা-আ'লানতু।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আমি যা গোপনে করেছি এবং যা প্রকাশ্যে করেছি—সবই আপনি ক্ষমা করে দিন।\n" +
                            "[নাসাঈ: ১১২৪, আহমাদ: ৯৬১]\n\n" +
                            "(ঘ) আল্লাহর সন্তুষ্টি ও আশ্রয়ের দো‘আ:\n" +
                            "اَللَّهُمَّ إنِّيْ أَعُوْذُ بِرِضَاكَ مِنْ سَخَطِكَ، وَأَعُوْذُ بِمُعَافَاتِكَ مِنْ عُقُوْبَتِكَ، وَأَعُوْذُ بِكَ مِنْكَ، لاَ أُحْصِيْ ثَنَاءً عَلَيْكَ، أَنْتَ كَمَا أَثْنَيْتَ عَلَى نَفْسِكَ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মা ইন্নী আ'ঊযু বিরিদ্বা-কা মিন সাখাত্বিকা, ওয়া আ'ঊযু বিমু'আ-ফা-তিকা মিন 'উকূবাতিকা, ওয়া আ'ঊযু বিকা মিনকা, লা- উহসী ছানা-আন 'আলাইকা, আনতা কামা- আছনায়তা 'আলা নাফসিকা।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আমি আপনার অসন্তুষ্টি থেকে আপনার সন্তুষ্টির আশ্রয় চাই, আপনার শাস্তি থেকে আপনার ক্ষমার আশ্রয় চাই এবং আপনার থেকে আপনারই আশ্রয় চাই। আপনার প্রশংসা গণনা করে শেষ করা আমার পক্ষে সম্ভব নয়; আপনি তেমনই যেমন আপনি নিজের প্রশংসা করেছেন।\n" +
                            "[সহীহ মুসলিম: ৪৮৬, আবু দাউদ: ৮৭৯, তিরমিযী: ৩৫৬৬]\n\n" +
                            "(ঙ)\n" +
                            "اَللَّهُمَّ لَكَ سَجَدْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ وَ أَنْتَ رَبِّىْ، سَجَدَ وَجْهِيَ لِلَّذِيْ خَلَقَهُ وَصَوَّرَهُ فَأَحْسَنَ صُوَرَهُ وَشَقَّ سَمْعَهُ وَبَصَرَهُ، فَتَبَارَكَ اللهُ أَحْسَنُ الْخَالِقِيْنَ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মা লাকা সাজাদতু, ওয়া বিকা আ-মানতু, ওয়া লাকা আসলামতু, ওয়া আনতা রব্বী, সাজাদা ওয়াজহিয়া লিল্লাযী খলাক্বাহূ ওয়া সওওয়ারাহূ ফা-আহসানা সুওয়ারাহূ ওয়া শাক্বক্বা সাম'আহূ ওয়া বাসারাহূ, ফাতাবা-রকাল্লা-হু আহসানুল খালিক্বীন।\n\n" +
                            "অর্থ:\n" +
                            "হে আল্লাহ! আমি আপনার জন্যই সিজদা করলাম, আপনার প্রতি ঈমান আনলাম, আপনার কাছেই আত্মসমর্পণ করলাম এবং আপনিই আমার প্রতিপালক। আমার মুখমণ্ডল সিজদায় অবনত হলো তাঁরই জন্য যিনি একে সৃষ্টি করেছেন, সুন্দর রূপ দান করেছেন এবং এতে শ্রবণ ও দৃষ্টিশক্তি স্থাপন করেছেন। পরম বরকতময় আল্লাহ, যিনি সর্বোত্তম সৃষ্টিকর্তা।\n" +
                            "[সহীহ মুসলিম: ৭৭১, তিরমিযী: ৩৪২১]"
            ));

            // ৯. দুই সিজদার মাঝখানে যে দোয়া পড়তে হয়
            list.add(new EssentialRuleItem(
                    "দুই সিজদার মাঝখানে যে দোয়া পড়তে হয়",
                    "দুই সিজদার মাঝখানে অত্যন্ত গুরুত্বপূর্ণ মাসনুন দোয়া রয়েছে, যা ইহকালীন ও পরকালীন কল্যাণে পরিপূর্ণ...",
                    "দুই সিজদার মাঝখানে অত্যন্ত গুরুত্বপূর্ণ মাসনূন দো‘আ রয়েছে। এই দো‘আটি এতটাই সারগর্ভপূর্ণ যে, যদি এটি কবুল হয় তবে বান্দার ইহকাল ও পরকালের জীবন সবদিক দিয়ে পরিপূর্ণ হয়ে যায়।\n\n" +
                            "দুই সিজদার মাঝখানে বসার সঠিক নিয়ম:\n" +
                            "একটি সিজদা আদায়ের পর সোজা হয়ে শান্তভাবে বসতে হবে। রাসূলুল্লাহ (সা.) ইরশাদ করেন:\n\n" +
                            "وَيَرْفَعُ رَأْسَهُ حَتَّى يَسْتَوِيَ قَاعِدًا\n\n" +
                            "অর্থ:\n" +
                            "\"(প্রথম সিজদার পর মাথা তুলে এমন সোজা ও স্থির হয়ে বসবে) যাতে শরীরের প্রতিটি হাড় ও জোড়াসমূহ স্ব-স্ব স্থানে যথারীতি স্বাভাবিক অবস্থায় ফিরে আসে।\"\n" +
                            "[সুনান আবী দাউদ: ৮৫৭]\n\n" +
                            "— — —\n\n" +
                            "দুই সিজদার মাঝখানে পড়ার দুটি সহীহ দো‘আ রয়েছে (যেকোনো একটি পাঠ করা যায়):\n\n" +
                            "১. প্রথম দো‘আ (সংক্ষিপ্ত ও সহজ):\n" +
                            "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\n\n" +
                            "উচ্চারণ:\n" +
                            "রব্বিগফির লী, রব্বিগফির লী।\n\n" +
                            "অর্থ:\n" +
                            "\"হে আমার রব! আপনি আমাকে ক্ষমা করুন। হে আমার রব! আপনি আমাকে ক্ষমা করুন।\"\n" +
                            "[সুনান আবী দাউদ: ১/২৩১, ইবনু মাজাহ: ৮৯৭]\n\n" +
                            "— — —\n\n" +
                            "২. দ্বিতীয় দো‘আ (অতীব বরকতময় ও সারগর্ভ):\n" +
                            "اللَّهُمَّ اغْفِرْ لِيْ، وَارْحَمْنِيْ، وَاهْدِنِيْ، وَاجْبُرْنِيْ، وَعَافِنِيْ، وَارْزُقْنِيْ، وَارْفَعْنِيْ\n\n" +
                            "উচ্চারণ:\n" +
                            "আল্লা-হুম্মাগফির লী, ওয়ারহামনী, ওয়াহদিনী, ওয়াজবুরনী, ওয়া 'আ-ফিনী, ওয়ারযুক্বনী, ওয়ারফা'নী।\n\n" +
                            "অর্থ:\n" +
                            "\"হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, আমার প্রতি দয়া করুন, আমাকে সঠিক পথে পরিচালিত করুন, আমার সমস্ত অভাব ও ক্ষতি পূরণ করে দিন, আমাকে শারীরিক ও আত্মিক সুস্থতা/নিরাপত্তা দান করুন, আমাকে হালাল রিযিক দান করুন এবং আমার মর্যাদা বৃদ্ধি করুন।\"\n\n" +
                            "[সুনান আবী দাউদ: ৮৫০, জামে' তিরমিযী: ২৮৪, ২৮৫, সুনান ইবনু মাজাহ: ৮৯৮, মিশকাত: ৭৪৬]"
            ));

            // ১০. সালাম ফেরানোর সঠিক নিয়ম ও এরপর যা করণীয়
            list.add(new EssentialRuleItem(
                    "সালাম ফেরানোর সঠিক নিয়ম ও এরপর যা করণীয়",
                    "অতঃপর ডানে ও বামে ‘আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ’ বলে সালাম ফিরিয়ে সালাত সমাপ্ত করবে...",
                    "অতঃপর ডানে ও বামে ‘আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ’ বলে সালাম ফিরিয়ে সালাত সমাপ্ত করবে।\n\n" +
                            "সালাম ফেরানোর সঠিক পদ্ধতি ও জামাতের নিয়ম:\n" +
                            "সালাম দুদিকে ফেরালেও মূলত একে সালাতের সমাপ্তির একটি অবিচ্ছেদ্য কাজ ধরা হয়। সেজন্য একদল ফকীহর মতে, সবচেয়ে বিশুদ্ধ ও সঠিক পদ্ধতি হলো—ইমাম সাহেবের দ্বিতীয় সালাম ফেরানো শেষ হলে মুক্তাদিরা প্রথম সালাম শুরু করবে। অতঃপর দ্বিতীয় সালাম ফেরাবে।\n\n" +
                            "মক্কা মুকাররমা ও মদীনা মুনাওয়ারার মসজিদুল হারাম ও মাসজিদুন নববীসহ সে দেশের মসজিদগুলোতে মুসল্লিরা এভাবেই সুন্নাহ মোতাবেক সালাম ফেরায়।\n\n" +
                            "সালাম ফেরানোর পর করণীয় আমল:\n" +
                            "সালাম ফেরানোর পর ৩ বার \"আস্তাগফিরুল্লাহ\" বলা এবং মাসনূন জিকির, দো‘আ ও আয়াতুল কুরসী পাঠ করা সুন্নাত।"
            ));

            // ১১. সালাতের আরকান কয়টি ও কী কী?
            list.add(new EssentialRuleItem(
                    "সালাতের আরকান কয়টি ও কী কী?",
                    "একবচনে রুকন ও বহুবচনে আরকান। এগুলো হলো নামাযের ভেতরের ফরয। এর সংখ্যা নিয়েও আলেমগণের মতভেদ রয়েছে...",
                    "একবচনে রুকন ও বহুবচনে আরকান। এগুলো হলো নামাযের ভেতরের ফরয। এর সংখ্যা নিয়েও আলেমগণের মতভেদ রয়েছে। তবে বিশুদ্ধ দলীল-প্রমাণের ভিত্তিতে বিজ্ঞ ফকীহগণের মতে, সালাতের রুকন ১০টি:\n\n" +
                            "১. দাঁড়িয়ে সালাত আদায় (ফরয সালাতে সক্ষম অবস্থায়)\n" +
                            "২. তাকবীরে তাহরীমা (প্রথম তাকবীর)\n" +
                            "৩. সূরা ফাতিহা পাঠ (প্রত্যেক রাকাআতে)\n" +
                            "৪. রুকু করা এবং রুকু থেকে উঠা।\n" +
                            "৫. সিজদা এবং সিজদা থেকে উঠা।\n" +
                            "৬. দুই সিজদার মধ্যবর্তী বৈঠক।\n" +
                            "৭. শেষ বৈঠক ও তাশাহহুদ (আত্তাহিয়্যাতু) পড়া।\n" +
                            "৮. রুকনগুলো ধীরস্থিরভাবে আদায় করা।\n" +
                            "৯. রুকন আদায়ে ধারাবাহিকতা রক্ষা করা (অর্থাৎ ক্রমধারা অনুযায়ী একের পর এক রুকনগুলো আদায় করা )\n" +
                            "১০. সালাম ফেরানো (ডানে ও বামে)। এর কোন একটা ফরয ইচ্ছায় বা ভুলে বাদ পড়লে সালাত বাতিল হয়ে যাবে。\n\n" +
                            "[সহীহ বুখারী]"
            ));
        } else {
            // English Items
            list.add(new EssentialRuleItem(
                    "Rules for Straightening Rows",
                    "Straightening the prayer rows is achieved by aligning ankles and heels from the back. It is not necessary to align toes from the front...",
                    "The rule for straightening prayer rows is that all worshippers must align their ankles and heels from behind to keep the row straight. There is no need to align toes from the front, because varying foot lengths naturally make toes protrude differently without causing any flaw in the row.\n\n" +
                            "[Al-Bahrur Raiq: Vol-1, Page-353]\n\n\n" +
                            "In congregational prayer, joining ankles to ankles and shoulders to shoulders keeps the row straight. Align ankles and heels evenly; there is no necessity to match toes from the front."
            ));

            list.add(new EssentialRuleItem(
                    "When No Space in the Row",
                    "If there is no space in the existing row, wait until the Imam goes into Ruku. If another worshipper joins, stand with them behind the Imam...",
                    "If there is no space available in the existing row, one should wait until the Imam goes into Ruku. If another worshipper arrives, join together to form a new row directly behind the Imam. If no one arrives, stand individually directly behind the Imam.\n\n" +
                            "[Fatawa Shami: Vol-1, Page-568; Al-Bahrur Raiq: Vol-1, Page-617, Vol-1, Page-353]"
            ));

            list.add(new EssentialRuleItem(
                    "Rules of Standing in Rows",
                    "When standing in prayer rows for congregation, worshippers should stand closely together, fill empty gaps, and align shoulder to shoulder...",
                    "When standing in prayer rows for congregational Salah, worshippers should stand close together and fill any empty gaps immediately. Each person should stand alongside one another, aligning shoulders evenly in the row.\n\n" +
                            "[Al-Muheet al-Burhani: Vol-2, Page-202, Ad-Durrul Mukhtar ma'a Radd: Vol-2, Page-568, Al-Bahrur Raiq: Vol-1, Page-618-619, Sunan Abi Dawood: Vol-1, Page-106]"
            ));

            list.add(new EssentialRuleItem(
                    "Rules of Folding Hands in Salah",
                    "The Messenger of Allah (peace be upon him) placed his hands upon his chest during prayer. Wa'il ibn Hujr (RA) narrated...",
                    "The Messenger of Allah (peace be upon him) placed his hands upon his chest in prayer. His companion Wa'il ibn Hujr (RA) narrated:\n\n" +
                            "\"I prayed with the Prophet (peace be upon him) and observed that he placed his right hand over his left hand upon his chest.\"\n\n" +
                            "[Sahih Ibn Khuzaymah, Page 2; Sunan Abi Dawood: 759, Albani]\n\n" +
                            "Another companion, Sahl ibn Sa'd (RA) narrated:\n" +
                            "\"People were commanded to place the right hand over the left forearm in prayer.\"\n\n" +
                            "[Sahih Bukhari: 740, IFA: 704]\n\n" +
                            "In Arabic, 'Kaff' refers to the hand up to the wrist, while 'Dhira' means the entire forearm from fingertips to elbow. Therefore, according to the essence and instruction of the hadith, the fingers of the right hand should reach near the left elbow.\n\n" +
                            "[Sunan Abi Dawood: 759, Albani]\n\n" +
                            "Some have held that placing hands below the navel is Sunnah; however, the narrations supporting placing hands below the navel are Da'eef (weak).\n\n" +
                            "[Da'eef Abi Dawood (Albani): 756, 758]\n\n" +
                            "Therefore, relying on weak narrations over authentic (Sahih) hadiths is not permissible. Additionally, some differentiate that women should place hands on their chest while men below the navel; however, where the Prophet (PBUH) made no distinction, no such division exists. The established ruling applies equally to both men and women—placing hands upon the chest is Sunnah for both."
            ));

            list.add(new EssentialRuleItem(
                    "Proper Method of Performing Ruku",
                    "Ruku literally means bowing down; terminologically, bowing before Allah in the prescribed Shar'i manner. It is a mandatory pillar of Salah...",
                    "‘Ruku’ literally means bowing down; terminologically, it means ‘bowing before Allah in the prescribed Shar'i manner’. Ruku is an essential obligatory pillar (Rukn) of Salah, without which the prayer is rendered invalid. After completing recitation, one must bow with profound humility, lowering the head and back before Allah Almighty.\n\n" +
                            "Proper Method and Steps of Ruku:\n" +
                            "• When going into Ruku, say 'Allahu Akbar' (Takbir) while raising both hands straight up to shoulder level.\n" +
                            "• Then place the hands firmly upon both knees with fingers spread apart, gripping the knees firmly. Keep the arms straight and sturdy like pillars.\n" +
                            "• While bowing, the head should neither be raised high nor lowered down; rather, it should be kept level and straight in line with the back.\n" +
                            "• The gaze should be directed toward the place of prostration (Sujud).\n" +
                            "• Keep both knees completely straight without bending.\n\n" +
                            "[Al-Bayhaqi, Al-Hakim, Sunan Ibn Majah: 872]"
            ));

            list.add(new EssentialRuleItem(
                    "Supplications to Recite in Ruku",
                    "Multiple authentic supplications are prescribed in the Sunnah for Ruku. The most common is: 'Subhana Rabbiyal Azeem'...",
                    "Multiple authentic supplications are prescribed in the Sunnah to recite during Ruku.\n\n" +
                            "1. The Primary Supplication:\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ\n\n" +
                            "Transliteration:\n" +
                            "Subhana Rabbiyal 'Azeem\n\n" +
                            "Translation:\n" +
                            "\"Glory be to my Lord, the Almighty.\"\n\n" +
                            "[Abu Dawood, Tirmidhi, Mishkat: 881]\n\n" +
                            "Recite this at least three times. There is no specific maximum limit—one may recite it as many times as desired.\n" +
                            "[Ahmad, Abu Dawood: 885, Ibn Majah: 888, Albani: Sifat as-Salah, p. 113]\n\n" +
                            "(Note: The narration specifying a maximum limit of ten times is Da'eef/weak).\n" +
                            "[Tirmidhi, Abu Dawood, An-Nasa'i, Mishkat: 880, 883]\n\n" +
                            "— — —\n\n" +
                            "2. Supplication Frequently Recited in Later Life:\n" +
                            "Toward the latter part of his blessed life, the Messenger of Allah (peace be upon him) frequently recited this in Ruku and Sujud:\n\n" +
                            "سُبْحَانَكَ اللَّهُمَّ رَبَّنَا وَبِحَمْدِكَ، اَللَّهُمَّ اغْفِرْ لِيْ\n\n" +
                            "Transliteration:\n" +
                            "Subhanaka Allahumma Rabbana wa bihamdika, Allahummaghfir li\n\n" +
                            "Translation:\n" +
                            "\"Glory be to You, O Allah, our Lord, and praise be to You. O Allah, forgive me.\"\n\n" +
                            "[Sahih Bukhari, Sahih Muslim, Mishkat: 871, Nayl al-Awtar: 3/106]\n\n" +
                            "— — —\n\n" +
                            "3. Other Authentic Supplications for Ruku:\n\n" +
                            "(a)\n" +
                            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ\n\n" +
                            "Transliteration:\n" +
                            "Subhana Rabbiyal 'Azeemi wa bihamdihi (3 times)\n" +
                            "[Sunan Abi Dawood: 870]\n\n" +
                            "(b)\n" +
                            "سُبُّوْحٌ قُدُّوْسٌ رَبُّ الْمَلَائِكَةِ وَالرُّوْحِ\n\n" +
                            "Transliteration:\n" +
                            "Subbuhun Quddusun Rabbul-Mala'ikati war-Rooh\n\n" +
                            "Translation:\n" +
                            "\"Perfect and Holy is the Lord of the angels and the Spirit (Jibreel).\"\n" +
                            "[Sahih Muslim: 487]\n\n" +
                            "(c)\n" +
                            "اَللَّهُمَّ لَكَ رَكَعْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ، خَشَعَ لَكَ سَمْعِيْ وَبَصَرِيْ وَمُخِّيْ وَعَظْمِيْ وَعَصَبِيْ\n\n" +
                            "Transliteration:\n" +
                            "Allahumma laka raka'tu, wa bika aamantu, wa laka aslamtu, khasha'a laka sam'i wa basari wa mukhkhi wa 'adhmi wa 'asabi\n\n" +
                            "Translation:\n" +
                            "\"O Allah, to You I have bowed, in You I have believed, and to You I have submitted. My hearing, sight, mind, bones, and sinews are humbled before You.\"\n" +
                            "[Sahih Muslim: 771]\n\n" +
                            "(d)\n" +
                            "اَللَّهُمَّ لَكَ رَكَعْتُ وَبِكَ أَسْلَمْتُ وَعَلَيْكَ تَوَكَّلْتُ، أَنْتَ رَبِّي خَشَعَ سَمْعِيْ وَبَصَرِيْ وَدَمِيْ وَلَحْمِيْ وَعَظْمِيْ وَعَصَبِيْ لِلَّهِ رَبِّ الْعَالَمِيْنَ\n\n" +
                            "Transliteration:\n" +
                            "Allahumma laka raka'tu wa bika aslamtu wa 'alayka tawakkaltu, Anta Rabbi, khasha'a sam'i wa basari wa dami wa lahmi wa 'adhmi wa 'asabi lillahi Rabbil-'Alameen\n" +
                            "[Sunan an-Nasa'i: 1051]\n\n" +
                            "(e)\n" +
                            "سُبْحَانَ ذِي الْجَبَرُوْتِ وَالْمَلَكُوْتِ وَالْكِبْرِيَاءِ وَالْعَظْمَةِ\n\n" +
                            "Transliteration:\n" +
                            "Subhana Dhil-Jabarooti wal-Malakooti wal-Kibriya'i wal-'Adhamah\n\n" +
                            "Translation:\n" +
                            "\"Glory be to the Possessor of ultimate power, dominion, grandeur, and majesty.\"\n" +
                            "[Abu Dawood: 873, An-Nasa'i: 1049, Mishkat: 875]"
            ));

            list.add(new EssentialRuleItem(
                    "Proper Method of Prostration (Sujud)",
                    "The Prophet (peace be upon him) laid his palms flat on the ground and prostrated upon seven bodily limbs...",
                    "The Prophet (peace be upon him) kept the palms of his hands flat upon the ground during prostration.\n" +
                            "[Sahih Bukhari: 828, IFA: 790, Modern: 782]\n\n" +
                            "The Messenger of Allah (peace be upon him) prostrated upon seven bodily limbs:\n" +
                            "[Sahih Bukhari: 812, Sahih Muslim: 490]\n\n" +
                            "1. The forehead and the nose (he gestured toward his nose with his hand, including it with the forehead)\n" +
                            "[Sahih Bukhari: 812, IFA: 775, Modern: 767]\n" +
                            "2. Both hands (palms flat)\n" +
                            "3. Both knees\n" +
                            "4. The tips/toes of both feet.\n\n" +
                            "Proper Steps and Rules of Sujud:\n" +
                            "• Just like the forehead, the nose must be placed firmly upon the ground. The Prophet (PBUH) kept his fingers joined straight, directed towards the Qiblah, and held both heels joined closely together.\n" +
                            "[Sahih Ibn Khuzaymah: 654]\n\n" +
                            "• The face should rest between the hands, aligned level with the shoulders or ears, and the forearms from wrists to elbows should be lifted well above the ground. Caution: Never rest forearms flat on the ground like a dog.\n" +
                            "[Sahih Bukhari: 822, Modern: 776]\n\n" +
                            "• Rest upon the toes of both feet with the knees kept upright. Keep elbows apart and detached from the flanks/armpits. Prostration should be calm and serene with the spine straight. Allah does not look at the prayer of a person who does not straighten their spine in Ruku and Sujud.\n" +
                            "[Musnad Ahmad: 2/525, At-Tabarani: Al-Mu'jam al-Kabir 8/338]\n\n" +
                            "• The Messenger of Allah (peace be upon him) kept his abdomen separated from his thighs enough that a small lamb or kid could pass through the gap.\n" +
                            "[Sahih Muslim: 496]\n\n" +
                            "• A moderate gap should also remain between both thighs. Both men and women should prostrate in this exact same manner following the Sunnah. There is no authentic hadith instructing women to prostrate differently or flatten themselves against the floor; rather, flattening oneself on the ground was prohibited by the Prophet (PBUH) and likened to the posture of a dog. Anas ibn Malik (RA) reported that the Prophet (PBUH) said:\n\n" +
                            "\"Be moderate in prostration, and none of you should spread out his forearms on the ground like a dog.\"\n\n" +
                            "[Sahih Bukhari: 822, IFA: 784, Modern: 776]"
            ));

            list.add(new EssentialRuleItem(
                    "Supplications to Recite in Sujud",
                    "Sujud is the supreme moment for acceptance of prayers. The Prophet (peace be upon him) said: The servant is nearest to his Lord when in prostration...",
                    "Prostration (Sujud) is the most precious moment for the acceptance of supplication. Abu Hurairah (RA) narrated that the Messenger of Allah (peace be upon him) said:\n\n" +
                            "أَقْرَبُ مَا يَكُوْنُ الْعَبْدُ مِنْ رَّبِّهِ وَهُوَ سَاجِدٌ فَأَكْثِرُوا الدُّعَاءَ\n\n" +
                            "Translation:\n" +
                            "\"The closest a servant comes to his Lord is when he is prostrating, so make abundant supplication.\"\n\n" +
                            "In another narration: \"Exert your utmost effort in supplication, for it is most worthy to be answered for you.\"\n" +
                            "[Sahih Muslim: 482, Mishkat: 894]\n\n" +
                            "He (peace be upon him) also said to recite the tasbeeh in Ruku and Sujud at least three times.\n" +
                            "[Sunan Ibn Majah: 888]\n\n" +
                            "— — —\n\n" +
                            "1. The Primary Supplication for Sujud:\n" +
                            "سُبْحَانَ رَبِّىَ الْأَعْلَى\n\n" +
                            "Transliteration:\n" +
                            "Subhana Rabbiyal A'la\n\n" +
                            "Translation:\n" +
                            "\"Glory be to my Lord, the Most High.\"\n\n" +
                            "(Reciting this at least 3 times is Sunnah, and it may be recited more times in odd numbers).\n" +
                            "[Abu Dawood: 870, Tirmidhi: 261]\n\n" +
                            "— — —\n\n" +
                            "2. Other Authentic Supplications for Sujud:\n\n" +
                            "(a) Seeking Complete Forgiveness:\n" +
                            "اَللَّهُمَّ اغْفِرْ لِيْ ذَنْبِيْ كُلَّهُ دِقَّهُ وَجِلَّهُ وَ أَوَّلَهُ وَآخِرَهُ وَعَلاَنِيَتَهُ وَسِرَّهُ\n\n" +
                            "Transliteration:\n" +
                            "Allahummaghfir li dhanbi kullahu diqqahu wa jillahu wa awwalahu wa aakhirahu wa 'alaniyatahu wa sirrahu\n\n" +
                            "Translation:\n" +
                            "\"O Allah, forgive all my sins, the minor and the major, the first and the last, the open and the secret.\"\n" +
                            "[Sahih Muslim: 483]\n\n" +
                            "(b)\n" +
                            "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ لآ إِلَهَ إِلَّا أَنْتَ\n\n" +
                            "Transliteration:\n" +
                            "Subhanaka Allahumma wa bihamdika la ilaha illa Anta\n\n" +
                            "Translation:\n" +
                            "\"Glory and praise be to You, O Allah; there is no deity worthy of worship except You.\"\n" +
                            "[Sahih Muslim: 485]\n\n" +
                            "(c)\n" +
                            "اَللَّهُمَّ اغْفِرْ لِيْ مَا أَসْرَرْتُ وَمَا أَعْلَنْتُ\n\n" +
                            "Transliteration:\n" +
                            "Allahummaghfir li ma asrartu wa ma a'lantu\n\n" +
                            "Translation:\n" +
                            "\"O Allah, forgive for me that which I have concealed and that which I have declared.\"\n" +
                            "[Sunan an-Nasa'i: 1124, Musnad Ahmad: 961]\n\n" +
                            "(d) Seeking Allah's Pleasure & Refuge:\n" +
                            "اَللَّهُمَّ إنِّيْ أَعُوْذُ بِرِضَاكَ مِنْ سَخَطِكَ، وَأَعُوْذُ بِمُعَافَاتِكَ مِنْ عُقُوْبَتِكَ، وَأَعُوْذُ بِكَ مِنْكَ، لاَ أُحْصِيْ ثَنَاءً عَلَيْكَ، أَنْتَ كَمَا أَثْنَيْتَ عَلَى نَفْسِكَ\n\n" +
                            "Transliteration:\n" +
                            "Allahumma inni a'oodhu bi-ridaka min sakhatika, wa a'oodhu bi-mu'afatika min 'uqoobatika, wa a'oodhu bika minka, la uhsi thana'an 'alayka, Anta kama athnayta 'ala nafsik\n\n" +
                            "Translation:\n" +
                            "\"O Allah, I seek refuge in Your pleasure from Your wrath, in Your forgiveness from Your punishment, and I seek refuge in You from You. I cannot praise You enough; You are as You have praised Yourself.\"\n" +
                            "[Sahih Muslim: 486, Abu Dawood: 879, Tirmidhi: 3566]\n\n" +
                            "(e)\n" +
                            "اَللَّهُمَّ لَكَ سَجَدْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَসْلَمْتُ وَ أَنْتَ رَبِّىْ، سَجَدَ وَجْهِيَ لِلَّذِيْ خَلَقَهُ وَصَوَّরَهُ فَأَحْسَنَ صُوَرَهُ وَشَقَّ سَمْعَهُ وَبَصَرَهُ، فَتَبَارَكَ اللهُ أَحْسَنُ الْخَالِقِيْنَ\n\n" +
                            "Transliteration:\n" +
                            "Allahumma laka sajadtu, wa bika aamantu, wa laka aslamtu, wa Anta Rabbi, sajada wajhiya lilladhi khalaqahu wa sawwarahu fa-ahsana suwarahu wa shaqqa sam'ahu wa basarahu, fatabarakallahu ahsanul-khaliqeen\n\n" +
                            "Translation:\n" +
                            "\"O Allah, to You I have prostrated, in You I have believed, to You I have submitted, and You are my Lord. My face has prostrated to the One Who created it and formed it into the best shape, and brought forth its hearing and sight. Blessed is Allah, the best of creators.\"\n" +
                            "[Sahih Muslim: 771, Tirmidhi: 3421]"
            ));

            list.add(new EssentialRuleItem(
                    "Supplication Between Two Prostrations",
                    "There is an immensely comprehensive and blessed supplication prescribed between the two prostrations (Jalsah)...",
                    "There is an immensely comprehensive and blessed supplication prescribed between the two prostrations (Jalsah) that covers all aspects of this life and the Hereafter.\n\n" +
                            "Proper Manner of Sitting Between Two Prostrations:\n" +
                            "After the first prostration, one must rise and sit upright with tranquility. The Messenger of Allah (peace be upon him) said:\n\n" +
                            "وَيَرْفَعُ رَأْسَهُ حَتَّى يَسْتَوِيَ قَاعِدًا\n\n" +
                            "Translation:\n" +
                            "\"(He raised his head from prostration and sat upright) until every bone and joint returned to its proper place.\"\n" +
                            "[Sunan Abi Dawood: 857]\n\n" +
                            "— — —\n\n" +
                            "Two Authentic Supplications Between Two Prostrations (either may be recited):\n\n" +
                            "1. First Supplication (Short & Concise):\n" +
                            "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\n\n" +
                            "Transliteration:\n" +
                            "Rabbighfir li, Rabbighfir li\n\n" +
                            "Translation:\n" +
                            "\"O my Lord, forgive me! O my Lord, forgive me!\"\n" +
                            "[Sunan Abi Dawood: 1/231, Sunan Ibn Majah: 897]\n\n" +
                            "— — —\n\n" +
                            "2. Second Supplication (Comprehensive & Blessed):\n" +
                            "اللَّهُمَّ اغْفِرْ لِيْ، وَارْحَمْنِيْ، وَاهْدِنِيْ، وَاجْبُرْنِيْ، وَعَافِنِيْ، وَارْزُقْنِيْ، وَارْفَعْنِيْ\n\n" +
                            "Transliteration:\n" +
                            "Allahummaghfir li, warhamni, wahdini, wajburni, wa 'afini, warzuqni, warfa'ni\n\n" +
                            "Translation:\n" +
                            "\"O Allah, forgive me, have mercy on me, guide me, mend my shortcomings, grant me well-being/safety, provide for me, and elevate my status.\"\n\n" +
                            "[Sunan Abi Dawood: 850, Jami' at-Tirmidhi: 284, 285, Sunan Ibn Majah: 898, Mishkat: 746]"
            ));

            list.add(new EssentialRuleItem(
                    "Rules of Concluding with Salam & Aftermath",
                    "Conclude the prayer by turning the head to the right and left, saying 'Assalamu Alaikum wa Rahmatullah'...",
                    "Conclude the prayer by turning the face to the right and then to the left, saying 'Assalamu Alaikum wa Rahmatullah'.\n\n" +
                            "Proper Method of Concluding Salam in Congregation:\n" +
                            "Although Salam is turned to both sides, it is considered a single unified concluding act of Salah. Therefore, according to prominent jurists, the most authentic and sound method is for followers in congregation to begin their first Salam only after the Imam completely finishes his second Salam, followed then by turning their second Salam.\n\n" +
                            "This precise method is uniformly practiced by worshippers in Masjid al-Haram in Makkah, Al-Masjid an-Nabawi in Madinah, and mosques worldwide.\n\n" +
                            "Sunnah Adhkar After Salam:\n" +
                            "Immediately after concluding Salam, it is Sunnah to say \"Astaghfirullah\" three times, followed by authentic morning/evening and post-prayer Adhkar including Ayat al-Kursi."
            ));

            list.add(new EssentialRuleItem(
                    "The Pillars (Arkan) of Salah",
                    "In singular 'Rukn' and plural 'Arkan'. These are the obligatory internal elements (Farz) of prayer. According to authentic proofs, there are 10 pillars of Salah...",
                    "In singular 'Rukn' and plural 'Arkan'. These are the internal obligatory elements (Farz) of prayer. While there are differences of opinion among scholars regarding their count, based on authentic evidence and the consensus of learned jurists, the pillars of Salah are 10:\n\n" +
                            "1. Standing in Salah (during obligatory prayers when physically able)\n" +
                            "2. Takbirat al-Ihram (the opening Takbeer)\n" +
                            "3. Recitation of Surah Al-Fatihah (in every rak'ah)\n" +
                            "4. Performing Ruku and rising upright from Ruku.\n" +
                            "5. Performing Sujud (prostration) and rising upright from Sujud.\n" +
                            "6. Sitting between the two prostrations.\n" +
                            "7. Final sitting and reciting Tashahhud (At-Tahiyyat).\n" +
                            "8. Performing all pillars calmly and with tranquility (Ta'deel al-Arkan).\n" +
                            "9. Maintaining sequential order in performing the pillars (i.e. performing them one after another in prescribed order)\n" +
                            "10. Concluding with Salam (to the right and left). If any of these obligatory pillars is omitted intentionally or by mistake, the Salah becomes invalid.\n\n" +
                            "[Sahih Bukhari]"
            ));
        }

        return list;
    }
}
