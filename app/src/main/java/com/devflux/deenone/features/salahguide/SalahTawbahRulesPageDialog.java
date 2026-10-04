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

public class SalahTawbahRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "সালাতুত তাওবা বা ইস্তিগফারের সালাত" : "Rules of Salatut Tawbah (Prayer of Repentance)");

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
            // 1. সালাতুত তাওবাহ-এর পরিচিতি ও ফজিলত
            list.add(new StepItem(
                    "সালাতুত তাওবাহ-এর পরিচিতি ও ফজিলত",
                    "অনুতপ্ত হয়ে ক্ষমা প্রার্থনার জন্য বিশেষভাবে যে নফল সালাত আদায় করা হয়... [আবুদাঊদ, তিরমিযী, মিশকাত হা/১৩২৪]",
                    "অনুতপ্ত হয়ে ক্ষমা প্রার্থনার জন্য বিশেষভাবে যে নফল সালাত আদায় করা হয়, তাকে ‘সালাতুত তাওবাহ’ বলা হয়। আবুবকর (রাঃ) হতে বর্ণিত তিনি বলেন, আমি রাসূলুল্লাহ (সাঃ)- কে বলতে শুনেছি যে, কোন লোক যদি গোনাহ করে। অতঃপর উঠে দাঁড়ায় ও পবিত্রতা অর্জন করে এবং দু’রাক‘আত সালাত আদায় করে। অতঃপর আল্লাহর নিকটে ক্ষমা প্রার্থনা করে। আল্লাহ তাকে ক্ষমা করে দেন। আবুদাঊদ, নাসাঈ, ইবনু মাজাহ, বায়হাক্বী, তিরমিযী, হাদীছ হাসান; ফিক্বহুস সুন্নাহ\n\n[১/১৫৯; মিশকাত হা/১৩২৪ ‘ঐচ্ছিক ছালাত’ অনুচ্ছেদ-৩৯; আলে ইমরান ৩/১৩৫]"
            ));

            // 2. সালাতের রাকাত ও পদ্ধতি
            list.add(new StepItem(
                    "সালাতের রাকাত ও পদ্ধতি সম্পর্কিত হাদিস",
                    "উক্ত সালাত দুই বা চার রাক‘আত ফরয কিংবা নফল পূর্ণ ওযূ ও সুন্দর রুকূ-সিজদা সহকারে... [ত্বাবারাণী কাবীর, আহমাদ হা/২৭৫৮৬]",
                    "ত্বাবারাণী কাবীরে ‘হাসান’ সনদে আবুদ্দারদা (রাঃ) হতে ‘মরফূ’ সূত্রে বর্ণিত হয়েছে যে, উক্ত সালাত দুই বা চার রাক‘আত ফরয কিংবা নফল পূর্ণ ওযূ ও সুন্দর রুকূ-সিজদা সহকারে হতে হবে।\n\n[ত্বাবারাণী কাবীর, আহমাদ হা/২৭৫৮৬; ছহীহাহ হা/৩৩৯৮; ছহীহ আত-তারগীব হা/২৩০]"
            ));

            // 3. তওবার বিশেষ দো‘আ
            list.add(new StepItem(
                    "তওবার বিশেষ দো‘আ",
                    "أَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ... [তিরমিযী, আবুদাঊদ, মিশকাত হা/২৩৫৩]",
                    "তওবার জন্য নিম্নের দো‘আটি বিশেষভাবে সিজদায় ও শেষ বৈঠকে সালাম ফিরানোর পূর্বে পাঠ করা উচিত।-\n\nأَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ\n\nউচ্চারণ:\nআস্তাগফিরুল্লা-হাল্লাযী লা ইলা-হা ইল্লা হুওয়াল হাইয়ুল ক্বাইয়ূমু ওয়া আত‚বু ইলাইহে।\n\nঅর্থ:\nআমি ক্ষমা প্রার্থনা করছি সেই আল্লাহর নিকটে যিনি ব্যতীত কোন উপাস্য নেই। যিনি চিরঞ্জীব ও বিশ্ব চরাচরের ধারক এবং তাঁর দিকেই আমি ফিরে যাচ্ছি বা তওবা করছি।\n\n[তিরমিযী, আবুদাঊদ, মিশকাত হা/২৩৫৩ ‘দো‘আ সমূহ’ অধ্যায়-৯, ‘ক্ষমা প্রার্থনা ওতওবা করা’ অনুচ্ছেদ-৪]"
            ));

            // 4. সাইয়েদুল ইস্তেগফার
            list.add(new StepItem(
                    "সাইয়েদুল ইস্তেগফার",
                    "اَللَّهُمَّ أَنْتَ رَبِّىْ لآ إِلهَ إلاَّ أَنْتَ خَلَقْتَنِىْ... সর্বশ্রেষ্ঠ তওবা ও ক্ষমা প্রার্থনার দো'আ",
                    "‘সাইয়েদুল ইস্তেগফার’ দো'আটিও এর সাথে যোগ করা ভাল।\n\nاَللَّهُمَّ أَنْتَ رَبِّىْ لآ إِلهَ إلاَّ أَنْتَ خَلَقْتَنِىْ وَأَنَا عَبْدُكَ وَأَنَا عَلى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوْذُبِكَ مِنْ شَرِّمَا صَنَعْتُ، أبُوْءُ لَكَ بِنِعْمَتِكَ عَلَىَّ وَأَبُوْءُ بِذَنْبِىْ فَاغْفِرْلِىْ، فَإِنَّهُ لاَيَغْفِرُ الذُّنُوْبَ إِلاَّ أَنْتَ\n\nউচ্চারণ:\nআল্লা-হুম্মা আনতা রব্বী লা ইলা-হা ইল্লা আনতা খালাক্বতানী, ওয়া আনা ‘আবদুকা ওয়া আনা ‘আলা ‘আহদিকা ওয়া ওয়া‘দিকা মাসতাত্বা‘তু, আ‘ঊযুবিকা মিন শার্রি মা ছানা‘তু। আবূউ লাকা বিনি‘মাতিকা ‘আলাইয়া ওয়া আবূউ বিযাম্বী ফাগফিরলী ফাইন্নাহূ লা ইয়াগফিরুয্ যুনূবা ইল্লা আনতা।\n\nঅর্থ :\nহে আল্লাহ! তুমি আমার পালনকর্তা। তুমি ব্যতীত কোন উপাস্য নেই। তুমি আমাকে সৃষ্টি করেছ। আমি তোমার দাস। আমি আমার সাধ্যমত তোমার নিকটে দেওয়া অঙ্গীকারে ও প্রতিশ্রুতিতে দৃঢ় আছি। আমি আমার কৃতকর্মের অনিষ্ট হ’তে তোমার নিকটে আশ্রয় প্রার্থনা করছি। আমি আমার উপরে তোমার দেওয়া অনুগ্রহকে স্বীকার করছি এবং আমি আমার গোনাহের স্বীকৃতি দিচ্ছি। অতএব তুমি আমাকে ক্ষমা কর। কেননা তুমি ব্যতীত পাপসমূহ ক্ষমা করার কেউ নেই’।"
            ));

        } else {
            // English Mode
            // 1. Definition and Significance of Salatut Tawbah
            list.add(new StepItem(
                    "Definition and Significance of Salatut Tawbah",
                    "The voluntary prayer performed with remorse to seek forgiveness... [Abu Dawud, Tirmidhi, Mishkat 1324]",
                    "The voluntary prayer performed specifically with remorse to seek forgiveness from Allah is called 'Salatut Tawbah' (Prayer of Repentance).\n\nAbu Bakr (RA) narrated: 'I heard the Messenger of Allah (peace be upon him) say: Whenever a person commits a sin, then rises, purifies himself (performs Wudu), performs two Rak'ahs of prayer, and then asks Allah for forgiveness, Allah forgives him.'\n\n[Sunan Abi Dawud, Sunan an-Nasa'i, Sunan Ibn Majah, Sunan al-Bayhaqi, Jami' at-Tirmidhi, Hasan Hadith; Fiqh-us-Sunnah 1/159; Mishkat, Hadith 1324; Surah Ali 'Imran, 3:135]"
            ));

            // 2. Rak'ahs and Method of Prayer
            list.add(new StepItem(
                    "Rak'ahs and Method of Prayer",
                    "Performing 2 or 4 Rak'ahs with complete Wudu and proper bowings... [Tabarani Kabir, Ahmad 27586]",
                    "It has been narrated on the authority of Abu Darda (RA) in Tabarani Kabir with a Hasan chain as a Marfu' narration that this prayer can be two or four Rak'ahs, whether Fard or Nafl, performed with complete Wudu and sincere, beautiful Ruku and Sujud.\n\n[Tabarani Kabir; Musnad Ahmad, Hadith 27586; Silsilah Sahihah, Hadith 3398; Sahih at-Targhib, Hadith 230]"
            ));

            // 3. Special Dua for Repentance
            list.add(new StepItem(
                    "Special Dua for Repentance",
                    "أَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ... [Jami' at-Tirmidhi, Abu Dawud, Mishkat 2353]",
                    "The following Dua should especially be recited during Sujud and in the final sitting before turning Salam for repentance:\n\nأَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ\n\nTransliteration:\nAstaghfirullahalladhee laa ilaaha illaa huwal-Hayyul-Qayyoomu wa atoobu ilayh.\n\nTranslation:\n'I seek the forgiveness of Allah, there is no deity worthy of worship except Him, the Ever-Living, the Sustainer of all existence, and I turn to Him in repentance.'\n\n[Jami' at-Tirmidhi, Sunan Abi Dawud; Mishkat al-Masabih, Hadith 2353, Book of Supplications, Chapter on Seeking Forgiveness and Repentance]"
            ));

            // 4. Sayyidul Istighfar (The Master Supplication for Forgiveness)
            list.add(new StepItem(
                    "Sayyidul Istighfar (The Master Supplication)",
                    "اَللَّهُمَّ أَنْتَ رَبِّىْ لآ إِلهَ إلاَّ أَنْتَ... The most comprehensive repentance prayer",
                    "It is also highly recommended to recite 'Sayyidul Istighfar' (The Master Supplication for Forgiveness):\n\nاَللَّهُمَّ أَنْتَ رَبِّىْ لآ إِلهَ إلاَّ أَنْتَ خَلَقْتَنِىْ وَأَنَا عَبْدُكَ وَأَنَا عَلى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوْذُبِكَ مِنْ شَرِّمَا صَنَعْتُ، أبُوْءُ لَكَ بِنِعْمَتِكَ عَلَىَّ وَأَبُوْءُ بِذَنْبِىْ فَاغْفِرْلِىْ، فَإِنَّهُ لاَيَغْفِرُ الذُّنُوْبَ إِلاَّ أَنْتَ\n\nTransliteration:\nAllahumma Anta Rabbi, laa ilaaha illaa Anta, khalaqtanee wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'oodhu bika min sharri ma sana'tu, aboo'u laka bini'matika 'alayya wa aboo'u bidhambee, faghfir lee, fa-innahu laa yaghfirudh-dhunooba illaa Anta.\n\nTranslation:\n'O Allah! You are my Lord. There is no deity worthy of worship except You. You created me and I am Your slave. I abide by Your covenant and Your promise as best as I can. I seek refuge in You from the evil of what I have done. I acknowledge before You Your blessings upon me, and I confess my sins to You. So forgive me, for none can forgive sins except You.'\n\n[Sahih al-Bukhari, Hadith 6306]"
            ));
        }

        return list;
    }
}
