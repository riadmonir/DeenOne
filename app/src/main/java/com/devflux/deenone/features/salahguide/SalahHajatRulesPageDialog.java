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

public class SalahHajatRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "সালাতুল হাজত আদায়ের নিয়ম" : "Rules of Salat al-Hajat (Prayer of Need)");

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
            // 1. সালাতুল হাজতের সংজ্ঞা ও কুরআনের নির্দেশ
            list.add(new StepItem(
                    "সালাতুল হাজতের পরিচিতি ও তাৎপর্য",
                    "বিশেষ কোন বৈধ চাহিদা পূরণের জন্য আল্লাহর উদ্দেশ্যে যে দু' রাক'আত নফল সালাত আদায় করা হয়... [ইবনু মাজাহ হা/১৩৮৫, বাক্বারাহ ২/১৫৩]",
                    "বিশেষ কোন বৈধ চাহিদা পূরণের জন্য আল্লাহর উদ্দেশ্যে যে দু' রাক'আত নফল সালাত আদায় করা হয়, তাকে ‘সালাতুল হাজত’বলা হয়।\n\n[ইবনু মাজাহ হা/১৩৮৫, ছালাত অধ্যায়-২ অনুচ্ছেদ-১৮৯]\n\nসঙ্গতকোন প্রয়োজন পূরণের জন্য বান্দা স্বীয় প্রভুর নিকটে সবর ও সালাতের মাধ্যমে সাহায্য প্রার্থনা করবে’\n\n[বাক্বারাহ ২/১৫৩]"
            ));

            // 2. সালাতুল হাজতে পঠিত সারগর্ভ দো'আ
            list.add(new StepItem(
                    "সালাতুল হাজতে পঠিত সারগর্ভ দো'আ",
                    "رَبَّنَاۤ اٰتِنَا فِی الدُّنۡیَا حَسَنَۃً... [বুখারী হা/৪৫২২, ৬৩৮৯; মিশকাত হা/২৪৮৭, মুসলিম, মিশকাত হা/৮১৩]",
                    "এজন্য শেষ বৈঠকে তাশাহহুদের পর সালাম ফিরানোর পূর্বে আশু প্রয়োজনীয় বিষয়টির কথা নিয়তের মধ্যে এনে নিচের সারগর্ভ দো'আটি পাঠ করবে।\n\nرَبَّنَاۤ اٰتِنَا فِی الدُّنۡیَا حَسَنَۃً وَّ فِی الۡاٰخِرَۃِ حَسَنَۃً وَّ قِنَا عَذَابَ النَّارِ\n\nউচ্চারণ:\nরব্বানা আ-তিনা ফিদ্দুন্ইয়া হাসানাতাঁও ওয়া ফিল আ-খেরাতে হাসানাতাঁও ওয়া ক্বিনা আযা-বান্না-র) ।\n\nঅর্থ:\nহে আমাদের পালনকর্তা! আপনি আমাদেরকে দুনিয়াতে মঙ্গল দিন ও আখেরাতে মঙ্গল দিন এবং আমাদেরকে জাহান্নামের আযাব হতে রক্ষা করুন।\n\nহযরত আনাস (রাঃ) বলেন, রাসূলুল্লাহ (সাঃ) অধিকাংশ সময় এ দো'আটিই পড়তেন।\n\n[বুখারী হা/৪৫২২, ৬৩৮৯; মিশকাত হা/২৪৮৭, মুসলিম, মিশকাত হা/৮১৩]"
            ));

            // 3. সিজদায় দো'আ পাঠ ও সংকটে সালাতের বিধান
            list.add(new StepItem(
                    "সিজদায় দো'আ পাঠ ও সংকটে সালাতের বিধান",
                    "দো'আটি সিজদায় পড়লে দো'আর পূর্বে 'আল্লা-হুম্মা' যুক্ত করতে হবে... [আবুদাঊদ হা/১৩১৯]",
                    "দো'আটি সিজদায় পড়লে দো'আর পূর্বে 'আল্লা-হুম্মা' শব্দটি যুক্ত করতে হবে। কেননা রুকু-সিজদায় কুরআনী দো'আ পড়া চলে না। হুযায়ফা (রাঃ) বলেন, ‘রাসূলুল্লাহ (সাঃ) যখন কোন সংকটে পড়তেন, তখন সালাতে রত হতেন'।\n\n[আবুদাঊদ হা/১৩১৯ ‘সালাত’ অধ্যায়-২, অনুচ্ছেদ-৩১২]"
            ));

            // 4. বিবি সারা (আঃ)-এর সালাতের মাধ্যমে সাহায্য প্রাপ্তির ঘটনা
            list.add(new StepItem(
                    "হযরত সারা (আঃ)-এর সালাতের মাধ্যমে সাহায্য প্রাপ্তির ঘটনা",
                    "যখন তিনি অপহৃত হয়ে মিসরের লম্পট সম্রাটের নিকটে নীত হলেন... [বুখারী হা/২২১৭]",
                    "উক্ত বিষয়ে হযরত ইবরাহীম (আঃ)-এর স্ত্রী সারা’র ঘটনা স্মরণ করা যেতে পারে। যখন তিনি অপহৃত হয়ে মিসরের লম্পট সম্রাটের নিকটে নীত হলেন ও অত্যাচারী সম্রাট তার দিকে এগিয়ে গেল, তখন তিনি ওযূ করে সালাতে দাঁড়িয়ে আল্লাহর নিকটে আশ্রয় প্রার্থনা করে বলেছিলেন, ‘হে আল্লাহ! এই কাফেরকে তুমি আমার উপর বিজয়ী করোনা’। সঙ্গে সঙ্গে আল্লাহ তাঁর ডাকে সাড়া দিয়েছিলেন এবং উক্ত লম্পটের হাত-পা অবশ হয়ে পড়েছিল। তিন-তিনবার ব্যর্থ হয়ে অবশেষে সে বিবি সারা-কে সসম্মানে মুক্তি দেয় এবং বহুমূল্যবান উপঢৌকনাদি সহ তার খিদমতের জন্য হাজেরাকে তার সাথে ইবরাহীমের নিকট পাঠিয়ে দেয়।\n\n[বুখারী হা/২২১৭ ‘ক্রয়-বিক্রয়’ অধ্যায়-৩৪, অনুচ্ছেদ-১০০]"
            ));

        } else {
            // English Mode
            // 1. Definition and Significance of Salat al-Hajat
            list.add(new StepItem(
                    "Definition and Significance of Salat al-Hajat",
                    "The two-Rak'ah voluntary prayer performed for fulfilling legitimate needs... [Ibn Majah 1385, Al-Baqarah 2:153]",
                    "The two-Rak'ah voluntary prayer performed sincerely for the sake of Allah to fulfill any specific lawful need is called 'Salat al-Hajat' (Prayer of Need).\n\n[Sunan Ibn Majah, Hadith 1385, Book of Prayer, Chapter 189]\n\n'And seek help through patience and prayer...' \n\n[Surah Al-Baqarah, 2:153]"
            ));

            // 2. Comprehensive Dua in Salat al-Hajat
            list.add(new StepItem(
                    "Comprehensive Dua in Salat al-Hajat",
                    "رَبَّنَاۤ اٰتِنَا فِی الدُّنۡیَا حَسَنَۃً... [Bukhari 4522, 6389; Mishkat 2487, Muslim]",
                    "For this reason, in the final sitting (Tashahhud) before turning the Salam, one should bring the pressing need into intention and recite this comprehensive Dua:\n\nرَبَّنَاۤ اٰتِنَا فِی الدُّنۡیَا حَسَنَۃً وَّ فِی الۡاٰخِرَۃِ حَسَنَۃً وَّ قِنَا عَذَابَ النَّارِ\n\nTransliteration:\nRabbana aatina fid-dunya hasanatanw wa fil-aakhirati hasanatanw wa qina 'adhaban-naar.\n\nTranslation:\n'Our Lord! Grant us good in this world and good in the Hereafter, and save us from the torment of the Fire.'\n\nHazrat Anas (RA) narrated that the Messenger of Allah (peace be upon him) used to recite this Dua most frequently.\n\n[Sahih al-Bukhari 4522, 6389; Mishkat 2487, Sahih Muslim, Mishkat 813]"
            ));

            // 3. Reciting Dua in Sujud and Salah during Crisis
            list.add(new StepItem(
                    "Reciting Dua in Sujud and Salah during Crisis",
                    "When reciting in Sujud, prefix with 'Allahumma'... [Sunan Abi Dawud 1319]",
                    "If this Dua is recited during Sujud (prostration), the word 'Allahumma' should be prefixed, as Quranic verses are not recited directly in Ruku or Sujud as Quran recitation. Hudhayfah (RA) narrated: 'Whenever the Messenger of Allah (peace be upon him) faced any hardship or crisis, he would turn to Salah.'\n\n[Sunan Abi Dawud, Hadith 1319, Book of Prayer, Chapter 312]"
            ));

            // 4. Historical Event of Sarah (AS) Seeking Help through Salah
            list.add(new StepItem(
                    "Historical Event of Sarah (AS) Seeking Help through Salah",
                    "The miraculous incident of Sarah (AS) praying during distress... [Sahih al-Bukhari 2217]",
                    "In this regard, the historical event of Sarah (AS), wife of Prophet Ibrahim (AS), is a profound example. When she was abducted and brought before the tyrant king of Egypt, and the oppressor stepped toward her, she performed Wudu, stood in prayer, and sought refuge in Allah, saying: 'O Allah! Do not let this disbeliever overpower me.' Immediately, Allah answered her prayer, and the tyrant's limbs became paralyzed. After failing three times, he released Sarah with great honor and sent Hajar with her as a servant along with valuable gifts back to Ibrahim (AS).\n\n[Sahih al-Bukhari, Hadith 2217, Book of Sales, Chapter 100]"
            ));
        }

        return list;
    }
}
