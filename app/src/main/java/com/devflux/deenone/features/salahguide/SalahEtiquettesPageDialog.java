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

public class SalahEtiquettesPageDialog {

    public static class EtiquetteItem {
        public final String title;
        public final String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public EtiquetteItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "সালাতের আদবসমূহ" : "Etiquettes of Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick settings/options
        });

        List<EtiquetteItem> items = getEtiquetteItems(isBn);

        EtiquettesAdapter adapter = new EtiquettesAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class EtiquettesAdapter extends RecyclerView.Adapter<EtiquettesAdapter.ViewHolder> {
        private final Context context;
        private final List<EtiquetteItem> items;
        private final boolean isBn;

        public EtiquettesAdapter(Context context, List<EtiquetteItem> items, boolean isBn) {
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
            EtiquetteItem item = items.get(position);
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
                        EtiquetteItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(EtiquetteItem item, boolean isBn) {
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

    public static List<EtiquetteItem> getEtiquetteItems(boolean isBn) {
        List<EtiquetteItem> list = new ArrayList<>();

        if (isBn) {
            // ১. ওয়াক্ত হলে নামায আদায় করা
            list.add(new EtiquetteItem(
                    "ওয়াক্ত হলে নামায আদায় করা",
                    "প্রত্যেক সালাত নির্ধারিত ওয়াক্তের মধ্যে আদায় করা। তবে আউয়াল ওয়াক্তে অর্থাৎ শুরুর সময়ে আদায় করা আল্লাহ তাআলার কাছে অধিকতর পছন্দনীয়। তাকওয়ার দাবি হলো আগে নামায, পরে কাজ। আগে কাজ, পরে নামায নয়। যারা কাজকর্মকে বেশি গুরুত্ব দি...",
                    "প্রত্যেক সালাত নির্ধারিত ওয়াক্তের মধ্যে আদায় করা। তবে আউয়াল ওয়াক্তে অর্থাৎ শুরুর সময়ে আদায় করা আল্লাহ তাআলার কাছে অধিকতর পছন্দনীয়। তাকওয়ার দাবি হলো আগে নামায, পরে কাজ। আগে কাজ, পরে নামায নয়। যারা কাজকর্মকে বেশি গুরুত্ব দিয়ে নামায দেরিতে পড়াকে অভ্যাসে পরিণত করে তারা গাফেলদের অন্তর্ভুক্ত হয়ে যায়। তাদের সম্পর্কে আল্লাহ তাআলা বলেন,\n\n\n" +
                            "ঐসব নামাযীদের জন্য ওয়াইল বা ধ্বংস, যারা তাদের নামাযের ব্যাপারে গাফেল।\n\n" +
                            "[সূরা মাউন ৪-৫]\n\n\n" +
                            "আর গাফেল হলো ঐসব লোক, যারা পেছাতে পেছাতে নামায শেষ ওয়াক্তে পড়ে এবং হুকুম আহকাম গুলো ঠিকমতো আদায় করে না।"
            ));

            // ২. কিবলামুখী হওয়া
            list.add(new EtiquetteItem(
                    "কিবলামুখী হওয়া",
                    "পূর্ণ দেহসহ কিবলার দিকে মুখ করে দাঁড়ানো।",
                    "পূর্ণ দেহসহ কিবলার দিকে মুখ করে দাঁড়ানো।\n\n\n" +
                            "[সূরা বাকারা: ১৪৪, মুসলিম ৩৯৭]"
            ));

            // ৩. পাশের মুসল্লির পায়ের সাথে পা মিলিয়ে দাঁড়ানো
            list.add(new EtiquetteItem(
                    "পাশের মুসল্লির পায়ের সাথে পা মিলিয়ে দাঁড়ানো",
                    "জামায়াতে দাঁড়ানোর সময় সাহাবাগণ পরস্পর একে অপরের পায়ের সাথে পা ও কাধের সাথে কাঁধ মিলিয়ে কাতার সোজা করে মিশে মিশে দাঁড়াতেন। আনাস (রা) বলেন, আমাদের কেউ কেউ তার পাশের মুসল্লির কাধের সাথে কাঁধ এবং পায়ের সাথে পা মিশিয়ে দাঁড়া...",
                    "জামাআতে দাঁড়ানোর সময় সাহাবাগণ পরস্পর একে অপরের পায়ের সাথে পা ও কাধের সাথে কাঁধ মিলিয়ে কাতার সোজা করে মিশে মিশে দাঁড়াতেন। আনাস (রা) বলেন,\n\n\n" +
                            "আমাদের কেউ কেউ তার পাশের মুসল্লির কাধের সাথে কাঁধ এবং পায়ের সাথে পা মিশিয়ে দাঁড়াত।\n\n" +
                            "[বুখারী: ৭২৫, ইফা ৬৮৯]\n\n\n" +
                            "সাহাবীগণ দু’জনের মাঝে কোন ফাঁকা রাখতেন না। জামায়াতে নামাযের ক্ষেত্রে দু’জন মুসল্লির মাঝে কোন ফাঁকা রাখা জায়েয- এমন কোন হাদীস নেই; বরং ফাকা রাখতে নিষেধ করা হয়েছে। কেননা ফাকা থাকলে সেখানে শয়তান ঢুকে পড়ে। আপনার ডানে ও বামে শয়তান সাথে নিয়ে নামায পড়বেন- এ কেমন কথা! আনাস (রা) থেকে বর্ণিত, রাসূলুল্লাহ (স) বলেছেন,\n\n\n" +
                            "তোমরা তোমাদের কাতারে পরস্পর মিলে মিলে দাঁড়াও, একে অপরের নিকটবর্তী হও এবং ঘাড়গুলোকে সমানভাবে সোজা রেখে দাঁড়াও। সেই মহান সত্তার (আল্লাহর) কসম, যার হাতে আমার প্রাণ, আমি দেখতে পাই কাতারে কোথাও ফাঁকা থাকলে সেখানেই শয়তান ঢুকে পড়ে কালো ভেড়ার বাচ্চার আকৃতিতে।\n\n" +
                            "[আবু দাউদ- ৬৬৬]"
            ));

            // ৪. সামনের কাতার আগে পূর্ণ করা
            list.add(new EtiquetteItem(
                    "সামনের কাতার আগে পূর্ণ করা",
                    "যারা আগে আসবেন তারা সামনের কাতারে এগিয়ে বসবেন অন্যকে সামনের কাতারে যাওয়ার অনুরোধ না করে অধিক সওয়াব লাভের আশায় নিজেই সেই সুযোগ গ্রহণ করুন। আর মসজিদের গেটে বা দরজায় বা সিঁড়িতে বসে মুসল্লিদের চলাচলে বিঘ্ন ঘটানো যাবে...",
                    "যারা আগে আসবেন তারা সামনের কাতারে এগিয়ে বসবেন অন্যকে সামনের কাতারে যাওয়ার অনুরোধ না করে অধিক সওয়াব লাভের আশায় নিজেই সেই সুযোগ গ্রহণ করুন। আর মসজিদের গেটে বা দরজায় বা সিঁড়িতে বসে মুসল্লিদের চলাচলে বিঘ্ন ঘটানো যাবে না। আর যারা পরে আসবেন, তারাও অন্যদের ঘাড় ডিঙিয়ে সামনে এগিয়ে যাবেন না।"
            ));

            // ৫. সালাতের আহকাম-আরকান
            list.add(new EtiquetteItem(
                    "সালাতের আহকাম-আরকান",
                    "আহকাম শব্দটি বহুবচন, একবচনে হুকুম। এগুলো হলো সালাতের বাইরের ফরয সালাতের শর্তসমূহকে আহকাম বা আরকান বলা হয়ে থাকে। সালাতের আহকাম সাতটি...",
                    "আহকাম শব্দটি বহুবচন, একবচনে হুকুম। এগুলো হলো সালাতের বাইরের ফরয সালাতের শর্তসমূহকে আহকাম বা আরকান বলা হয়ে থাকে। সালাতের আহকাম সাতটি\n\n\n" +
                            "শরীর পাক (অর্থাৎ সালাতের আগে ওযু করে পবিত্র হতে হবে, আর গোসল ফরয হলে আগে গোসল করে নিতে হবে),\n" +
                            "পোশাক পাক।\n" +
                            "জায়গা পাক।\n" +
                            "সময় হওয়া।\n" +
                            "সতর ঢাকা।\n" +
                            "কিবলামুখী হওয়া।\n" +
                            "নিয়ত করা।\n\n\n" +
                            "অপরদিকে সালাত ফরয হওয়ার জন্য বিজ্ঞ ফকীহগণ আরও ৩টি শর্ত যোগ করেছেন।\n\n\n" +
                            "ইসলাম গ্রহণ।\n" +
                            "হুঁশ-জ্ঞান থাকা ও\n" +
                            "প্রাপ্তবয়স্ক হওয়া।"
            ));
        } else {
            // English Mode
            // 1. Performing Salah Upon its Appointed Time
            list.add(new EtiquetteItem(
                    "Performing Salah Upon its Appointed Time",
                    "Performing every prayer within its designated time. Performing Salah at the earliest time (Awwal Waqt) is most beloved to Allah Almighty. Piety requires prioritizing prayer before worldly affairs...",
                    "Performing every prayer within its designated time. Performing Salah at the earliest time (Awwal Waqt) is most beloved to Allah Almighty. Piety requires prioritizing prayer before worldly affairs, not delaying prayer for work. Those who prioritize worldly matters and make a habit of delaying prayer become among the heedless.\n\n\n" +
                            "Allah the Almighty states:\n\n" +
                            "\"So woe to those who pray, but are heedless of their prayer.\"\n\n" +
                            "[Surah Al-Ma'un: 4-5]\n\n\n" +
                            "The heedless are those who repeatedly postpone prayer until the very end of its time and fail to fulfill its prerequisites and pillars properly."
            ));

            // 2. Facing the Qiblah
            list.add(new EtiquetteItem(
                    "Facing the Qiblah",
                    "Standing and facing the direction of the Qiblah with the entire body.",
                    "Standing and facing the direction of the Qiblah with the entire body. Once standing in prayer, it is not permissible to turn one's chest or face away from the Qiblah without a valid excuse.\n\n\n" +
                            "Allah the Almighty states:\n\n" +
                            "\"So turn your face toward al-Masjid al-Haram. And wherever you may be, turn your faces toward it.\"\n\n" +
                            "[Surah Al-Baqarah: 144, Sahih al-Bukhari: 6251]"
            ));

            // 3. Aligning Feet and Shoulders with Adjacent Musallis
            list.add(new EtiquetteItem(
                    "Aligning Feet and Shoulders with Adjacent Musallis",
                    "When standing in congregation, the Companions used to stand shoulder-to-shoulder and foot-to-foot, straightening rows without leaving gaps. Anas (RA) narrated that each person joined their shoulder with their neighbour...",
                    "When standing in congregation, the Companions used to stand shoulder-to-shoulder and foot-to-foot, straightening rows without leaving gaps. Anas (RA) narrated:\n\n\n" +
                            "'Each one of us used to join his shoulder with the shoulder of his neighbour and his foot with his foot.'\n\n" +
                            "[Bukhari: 725, IFA: 689]\n\n\n" +
                            "The Companions left no gaps between two worshippers. There is no Hadith permitting leaving gaps between worshippers in congregational prayer; rather, leaving gaps is strictly forbidden because Satan enters the gaps.\n\n\n" +
                            "Anas (RA) narrated that the Messenger of Allah (ﷺ) said:\n\n" +
                            "\"Stand close together in your rows, keep them near to one another, and align your necks evenly. By Him in Whose Hand is my soul, I see the Shaytan entering through the gaps in the rows like small black lambs.\"\n\n" +
                            "[Abu Dawud: 666]"
            ));

            // 4. Filling the Front Rows First
            list.add(new EtiquetteItem(
                    "Filling the Front Rows First",
                    "Those who arrive early should move forward to fill the front rows. Rather than asking others to take the front row, take the initiative yourself for greater reward. Avoid obstructing pathways or entrances...",
                    "Those who arrive early should move forward to fill the front rows. Rather than asking others to take the front row, take the initiative yourself for greater reward. Avoid sitting at the entrance, gates, or staircases of the mosque where it obstructs the movement of fellow worshippers. And those who arrive later should not step over the necks of others to move forward."
            ));

            // 5. Ahkam and Arkan of Salah (Conditions & Pillars)
            list.add(new EtiquetteItem(
                    "Ahkam and Arkan of Salah (Conditions & Pillars)",
                    "Ahkam refers to the external prerequisites of Salah that must be met before starting. There are 7 Ahkam (prerequisites) and 6 Arkan (internal pillars) essential for the validity of prayer...",
                    "Ahkam refers to the external prerequisites of Salah that must be met before starting the prayer.\n\n\n" +
                            "The 7 Ahkam (Prerequisites of Salah):\n\n" +
                            "1. Purity of body (performing Wudu or Ghusl if required).\n" +
                            "2. Purity of clothing.\n" +
                            "3. Purity of the prayer location.\n" +
                            "4. Covering the Satr (for men, navel to knees; for women, entire body except face, hands to wrists, and feet).\n" +
                            "5. Facing the Qiblah.\n" +
                            "6. Ensuring the designated prayer time has entered.\n" +
                            "7. Sincere intention (Niyyah) in the heart for the specific prayer.\n\n\n" +
                            "The 6 Arkan (Internal Pillars of Salah):\n\n" +
                            "1. Takbirat al-Ihram (saying 'Allahu Akbar' to begin prayer).\n" +
                            "2. Qiyam (standing for those who are capable in obligatory prayers).\n" +
                            "3. Qira'at (reciting Surah Al-Fatihah and additional Surah or verses).\n" +
                            "4. Ruku (bowing).\n" +
                            "5. Two Sujood (prostrations) in each unit (Rak'ah).\n" +
                            "6. Final Tashahhud sitting.\n\n" +
                            "[Sahih al-Bukhari, Sahih Muslim, Fiqh Hanafi]"
            ));
        }

        return list;
    }
}
