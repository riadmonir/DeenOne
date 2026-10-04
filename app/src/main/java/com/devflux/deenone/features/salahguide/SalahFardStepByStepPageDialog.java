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

public class SalahFardStepByStepPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "পাঁচ ওয়াক্ত ফরজ সালাত আদায়ের সঠিক পদ্ধতি" : "Proper Method of Five Daily Fard Prayers");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future options
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
            // ১. নিয়ত (নিয়্যত করা)
            list.add(new StepItem(
                    "১. নিয়ত (নিয়্যত করা)",
                    "নামাজ শুরু করার পূর্বে মনে মনে নির্দিষ্ট সালাতের নিয়ত করা জরুরি। মুখে পড়া সুন্নত...",
                    "নিয়ত (নিয়্যত করা):\n\n" +
                            "নামাজ শুরু করার পূর্বে মনে মনে নির্দিষ্ট সালাতের নিয়ত করা জরুরি। মুখে পড়া সুন্নত।\n\n\n" +
                            "উদাহরণ (ফজরের জন্য):\n\n" +
                            "নিয়ত করলাম আমি দুই রাকাআত ফরজ নামাজ আদায় করব ফজরের, কিবলামুখী হয়ে, এই ইমামের পেছনে/একাকী, আল্লাহর জন্য।"
            ));

            // ২. তাকবীরে তাহরিমা
            list.add(new StepItem(
                    "২. তাকবীরে তাহরিমা",
                    "নিয়ত শেষে বলবেন: \"আল্লাহু আকবার\" এর মাধ্যমে নামাজ শুরু হয়...",
                    "তাকবীরে তাহরিমা:\n\n" +
                            "নিয়ত শেষে বলবেন:\n\n" +
                            " \"আল্লাহু আকবার\"\n\n" +
                            "এর মাধ্যমে নামাজ শুরু হয়।\n\n\n" +
                            "[বুখারি: ৭৩৮]"
            ));

            // ৩. সানা দোয়া পড়া (ঈস্তেফতা)
            list.add(new StepItem(
                    "৩. সানা দোয়া পড়া (ঈস্তেফতা)",
                    "\"সুবহানাকাল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়ালা ইলাহা গাইরুকা\"...",
                    "সানা দোয়া পড়া (ঈস্তেফতা):\n\n" +
                            "\"সুবহানাকাল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়ালা ইলাহা গাইরুকা\"\n\n\n" +
                            "[তিরমিজি: ২৪৩]"
            ));

            // ৪. আউজু ও বিসমিল্লাহ
            list.add(new StepItem(
                    "৪. আউজু ও বিসমিল্লাহ",
                    "\"আউজুবিল্লাহি মিনাশ শাইতানির রাজিম\" ও \"বিসমিল্লাহির রাহমানির রাহিম\"...",
                    "আউজু ও বিসমিল্লাহ:\n\n" +
                            "\"আউজুবিল্লাহি মিনাশ শাইতানির রাজিম\"\n\n" +
                            "\"বিসমিল্লাহির রাহমানির রাহিম\""
            ));

            // ৫. সূরা ফাতিহা পাঠ
            list.add(new StepItem(
                    "৫. সূরা ফাতিহা পাঠ",
                    "সূরা ফাতিহা পড়া প্রত্যেক রাকাআতে ফরজ...",
                    "সূরা ফাতিহা পাঠ:\n\n" +
                            "সূরা ফাতিহা পড়া প্রত্যেক রাকাআতে ফরজ।\n\n\n" +
                            "[সহিহ বুখারি: ৭৫৬]"
            ));

            // ৬. কোনো সূরা বা আয়াত পাঠ (২ রাকাআতের মধ্যে)
            list.add(new StepItem(
                    "৬. কোনো সূরা বা আয়াত পাঠ (২ রাকাআতের মধ্যে)",
                    "প্রথম দুই রাকাআতে সূরা ফাতিহার পর একটি সূরা (যেমন সূরা ইখলাস) পড়বেন...",
                    "কোনো সূরা বা আয়াত পাঠ (২ রাকাআতের মধ্যে):\n\n" +
                            "প্রথম দুই রাকাআতে সূরা ফাতিহার পর একটি সূরা (যেমন সূরা ইখলাস) পড়বেন।\n\n\n" +
                            "[সহিহ মুসলিম: ৩৯৪]"
            ));

            // ৭. রুকুতে যাওয়া
            list.add(new StepItem(
                    "৭. রুকুতে যাওয়া",
                    "বলবেন: \"আল্লাহু আকবার\", রুকুতে: \"সুবহানা রব্বিয়াল আজিম\" (৩ বার)...",
                    "রুকুতে যাওয়া:\n\n" +
                            "বলবেন: \"আল্লাহু আকবার\"\n\n" +
                            "রুকুতে: \"সুবহানা রব্বিয়াল আজিম\" (৩ বার)\n\n\n" +
                            "[তিরমিজি: ২৬২]"
            ));

            // ৮. রুকু থেকে উঠা
            list.add(new StepItem(
                    "৮. রুকু থেকে উঠা",
                    "বলবেন: \"সামি আল্লাহু \", \"রাব্বানা লাকাল হামদ\"...",
                    "রুকু থেকে উঠা:\n\n" +
                            "বলবেন:\n\n" +
                            "\"সামি আল্লাহু \"\n\n" +
                            "\"রাব্বানা লাকাল হামদ\"\n\n\n" +
                            "[বুখারি: ৭৯৪]"
            ));

            // ৯. সিজদাহ
            list.add(new StepItem(
                    "৯. সিজদাহ",
                    "বলবেন: \"আল্লাহু আকবার\", এরপর সিজদাহতে: \"সুবহানা রাব্বিয়াল আ'লা\" (৩ বার)...",
                    "সিজদাহ:\n\n" +
                            "বলবেন: \"আল্লাহু আকবার\", এরপর সিজদাহ\n\n" +
                            "সিজদাহতে: \"সুবহানা রাব্বিয়াল আ'লা\" (৩ বার)\n\n\n" +
                            "[তিরমিজি: ২৬২]"
            ));

            // ১০. দুই সিজদার মাঝে বসা
            list.add(new StepItem(
                    "১০. দুই সিজদার মাঝে বসা",
                    "বলবেন: \"রব্বিগ্ফিরলি, রব্বিগ্ফিরলি\"...",
                    "দুই সিজদার মাঝে বসা:\n\n" +
                            "বলবেন:\n\n" +
                            "\"রব্বিগ্ফিরলি, রব্বিগ্ফিরলি\"\n\n\n" +
                            "[আবু দাউদ: ৮৫০]"
            ));

            // ১১. দ্বিতীয় সিজদাহ ও পরবর্তী রাকাআতে উঠা
            list.add(new StepItem(
                    "১১. দ্বিতীয় সিজদাহ ও পরবর্তী রাকাআতে উঠা",
                    "দ্বিতীয় সিজদাহ সম্পন্ন করে পরবর্তী রাকাআতের জন্য দাঁড়ানো...",
                    "দ্বিতীয় সিজদাহ ও পরবর্তী রাকাআতে উঠা:\n\n" +
                            "প্রথম সিজদাহ ও দুই সিজদার মধ্যবর্তী বৈঠকের পর \"আল্লাহু আকবার\" বলে দ্বিতীয় সিজদাহ আদায় করবেন এবং সিজদাহর তাসবীহ পাঠ শেষে পরবর্তী রাকাআতের জন্য সোজা হয়ে দাঁড়াবেন।"
            ));

            // ১২. তাশাহহুদ/আত্তাহিয়্যাত পড়া (দুই রাকাআতের পর বসে)
            list.add(new StepItem(
                    "১২. তাশাহহুদ/আত্তাহিয়্যাত পড়া (দুই রাকাআতের পর বসে)",
                    "\"আত্তাহিয়্যাতু লিল্লাহি... ওয়া আশহাদু আল লা ইলাহা ইল্লাল্লাহু...\"...",
                    "তাশাহহুদ/আত্তাহিয়্যাত পড়া (দুই রাকাআতের পর বসে):\n\n" +
                            "\"আত্তাহিয়্যাতু লিল্লাহি... ওয়া আশহাদু আল লা ইলাহা ইল্লাল্লাহু...\"\n\n\n" +
                            "[সহিহ মুসলিম: ৪০৩]"
            ));

            // ১৩. দুরুদ শরীফ ও দোয়া (শেষ বৈঠকে)
            list.add(new StepItem(
                    "১৩. দুরুদ শরীফ ও দোয়া (শেষ বৈঠকে)",
                    "\"আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ...\"...",
                    "দুরুদ শরীফ ও দোয়া (শেষ বৈঠকে):\n\n" +
                            "\"আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ...\"\n\n\n" +
                            "[বুখারি: ৩৩৭০]"
            ));

            // ১৪. সালাম ফিরানো
            list.add(new StepItem(
                    "১৪. সালাম ফিরানো",
                    "\"আসসালামু আলাইকুম ওয়া রহমাতুল্লাহ\" (ডানে ও বামে)...",
                    "সালাম ফিরানো:\n\n" +
                            "\"আসসালামু আলাইকুম ওয়া রহমাতুল্লাহ\" (ডানে ও বামে)\n\n\n" +
                            "[তিরমিজি: ২৯৫]"
            ));
        } else {
            // English Mode
            // 1. Intention (Niyyah)
            list.add(new StepItem(
                    "1. Intention (Niyyah)",
                    "Before beginning the prayer, it is essential to form a sincere intention in the heart...",
                    "Intention (Niyyah):\n\n" +
                            "Before beginning prayer, it is essential to intend the specific prayer in the heart. Uttering it is Sunnah.\n\n\n" +
                            "Example (for Fajr):\n\n" +
                            "I intend to offer two Rak'ahs of obligatory (Fard) prayer of Fajr, facing the Qiblah, behind this Imam / individually, for Allah."
            ));

            // 2. Takbir Tahrimah
            list.add(new StepItem(
                    "2. Takbir Tahrimah",
                    "After the intention say: \"Allahu Akbar\" to commence the prayer...",
                    "Takbir Tahrimah:\n\n" +
                            "After forming the intention, say:\n\n" +
                            " \"Allahu Akbar\"\n\n" +
                            "Through this declaration the prayer begins.\n\n\n" +
                            "[Bukhari: 738]"
            ));

            // 3. Reciting Thana (Istiftah)
            list.add(new StepItem(
                    "3. Reciting Thana (Istiftah)",
                    "\"Subhanakallahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruka\"...",
                    "Reciting Thana (Istiftah):\n\n" +
                            "\"Subhanakallahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruka\"\n\n\n" +
                            "[Tirmidhi: 243]"
            ));

            // 4. Ta'awwudh and Basmalah
            list.add(new StepItem(
                    "4. Ta'awwudh and Basmalah",
                    "\"A'udhu billahi minash-shaytanir-rajim\" and \"Bismillahir-Rahmanir-Rahim\"...",
                    "Ta'awwudh and Basmalah:\n\n" +
                            "\"A'udhu billahi minash-shaytanir-rajim\"\n\n" +
                            "\"Bismillahir-Rahmanir-Rahim\""
            ));

            // 5. Reciting Surah Al-Fatihah
            list.add(new StepItem(
                    "5. Reciting Surah Al-Fatihah",
                    "Reciting Surah Al-Fatihah is obligatory (Fard) in every Rak'ah...",
                    "Reciting Surah Al-Fatihah:\n\n" +
                            "Reciting Surah Al-Fatihah is obligatory (Fard) in every single Rak'ah.\n\n\n" +
                            "[Sahih Bukhari: 756]"
            ));

            // 6. Reciting an additional Surah or Verses
            list.add(new StepItem(
                    "6. Reciting an Additional Surah / Verses",
                    "In the first two Rak'ahs, recite an additional Surah after Surah Al-Fatihah...",
                    "Reciting an Additional Surah or Verses (in the first 2 Rak'ahs):\n\n" +
                            "In the first two Rak'ahs, recite a Surah (such as Surah Al-Ikhlas) after Surah Al-Fatihah.\n\n\n" +
                            "[Sahih Muslim: 394]"
            ));

            // 7. Bowing (Ruku)
            list.add(new StepItem(
                    "7. Bowing (Ruku)",
                    "Say: \"Allahu Akbar\", in Ruku recite: \"Subhana Rabbiyal Azeem\" (3 times)...",
                    "Going into Ruku (Bowing):\n\n" +
                            "Say: \"Allahu Akbar\"\n\n" +
                            "In Ruku: \"Subhana Rabbiyal Azeem\" (3 times)\n\n\n" +
                            "[Tirmidhi: 262]"
            ));

            // 8. Rising from Ruku
            list.add(new StepItem(
                    "8. Rising from Ruku",
                    "Say: \"Sami' Allahu liman hamidah\", \"Rabbana lakal hamd\"...",
                    "Rising from Ruku:\n\n" +
                            "Say:\n\n" +
                            "\"Sami' Allahu liman hamidah\"\n\n" +
                            "\"Rabbana lakal hamd\"\n\n\n" +
                            "[Bukhari: 794]"
            ));

            // 9. Prostration (Sujud)
            list.add(new StepItem(
                    "9. Prostration (Sujud)",
                    "Say: \"Allahu Akbar\", then prostrate and recite: \"Subhana Rabbiyal A'la\" (3 times)...",
                    "Prostration (Sujud):\n\n" +
                            "Say: \"Allahu Akbar\", then go down in prostration\n\n" +
                            "In Sujud: \"Subhana Rabbiyal A'la\" (3 times)\n\n\n" +
                            "[Tirmidhi: 262]"
            ));

            // 10. Sitting between two Sujud
            list.add(new StepItem(
                    "10. Sitting Between Two Sujud",
                    "Say: \"Rabbighfirli, Rabbighfirli\"...",
                    "Sitting between two Sujud:\n\n" +
                            "Say:\n\n" +
                            "\"Rabbighfirli, Rabbighfirli\"\n\n\n" +
                            "[Abu Dawud: 850]"
            ));

            // 11. Second Sujud and Rising
            list.add(new StepItem(
                    "11. Second Sujud and Rising for Next Rak'ah",
                    "Perform the second Sujud and rise upright for the next Rak'ah...",
                    "Second Sujud and Rising for Next Rak'ah:\n\n" +
                            "Perform the second Sujud saying \"Allahu Akbar\", recite the Tasbeeh of Sujud, and then rise upright for the next Rak'ah."
            ));

            // 12. Tashahhud / At-Tahiyyat
            list.add(new StepItem(
                    "12. Tashahhud / At-Tahiyyat",
                    "\"At-Tahiyyatu lillahi... wa ashhadu alla ilaha illallah...\"...",
                    "Tashahhud / At-Tahiyyat (Sitting after two Rak'ahs):\n\n" +
                            "\"At-Tahiyyatu lillahi... wa ashhadu alla ilaha illallah...\"\n\n\n" +
                            "[Sahih Muslim: 403]"
            ));

            // 13. Durood Sharif and Supplication
            list.add(new StepItem(
                    "13. Durood Sharif & Dua (Final Sitting)",
                    "\"Allahumma Salli 'ala Muhammad...\"...",
                    "Durood Sharif and Supplication (in the Final Sitting):\n\n" +
                            "\"Allahumma Salli 'ala Muhammad...\"\n\n\n" +
                            "[Bukhari: 3370]"
            ));

            // 14. Tasleem (Ending Prayer)
            list.add(new StepItem(
                    "14. Tasleem (Concluding the Prayer)",
                    "\"Assalamu 'Alaykum wa Rahmatullah\" (turning head to the right and left)...",
                    "Tasleem (Concluding the Prayer):\n\n" +
                            "\"Assalamu 'Alaykum wa Rahmatullah\" (to the right and left)\n\n\n" +
                            "[Tirmidhi: 295]"
            ));
        }

        return list;
    }
}
