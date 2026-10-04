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

public class SalahIqamahAnswerPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইকামতের জওয়াব" : "Responding to Iqamah");

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
            // 1. ইকামতের জওয়াব (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "ইকামতের জওয়াব",
                    "ইকামতকে দ্বিতীয় আযান বলা, জওয়াব প্রদানের পদ্ধতি ও তাহক্বীক্ব... [মুসলিম, ফাতাওয়া ইসলামিয়্যাহ্, আলবানী]",
                    "ইকামতকে দ্বিতীয় আযান বলা হয়, তাই ইকামতও এক প্রকার আযান।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৪৯]\n\nসুতরাং এর জওয়াবও আযানের মতই। অবশ্য ‘হাইয়্যা আলাস সলা-হ্ ও ফালাহ্’ এর জওয়াবে ‘লাহাউলা অলা ক্বুওয়াতা ইল্লা বিল্লাহ্’ এবং শেষে (সময় পেলে) দরুদ ও অসীলার দুআ পাঠ করা বিধেয়। যেহেতু হাদীস শরীফে মুআযযিনের জওয়াব (তার মতই) বলতে এবং তার শেষে দরুদ ও অসীলার দুআ পড়তে আমাদেরকে আদেশ করা হয়েছে।\n\n[মুসলিম, মিশকাত ৬৫৭নং]\n\nউক্ত হাদীসের ভিত্তিতেই ‘ক্বাদ ক্বামাতিস স্বলাহ্ এর জওয়াবে ‘ক্বাদ ক্বামাতিস স্বলাহ্’ই বলতে হবে। নচেৎ এর জওয়াবে ‘আক্বামাহুল্লাহু অআদামাহা’ বলার হাদীস শুদ্ধ নয়। আর যয়ীফ হাদীসকে ভিত্তি করে শরীয়তের কোন আমল ও ইবাদত বৈধ নয়।\n\n[মিশকাত, আলবানীর টীকা ১/১২১]\n\nমতান্তরে যেহেতু ইকামতের জবাবে কোন স্পষ্ট সহীহ হাদীস নেই, তাই ইকামতের জবাব দেওয়া সুন্নত নয়।"
            ));

            // 2. ইকামত দ্বিতীয় আযান ও জওয়াবের বিধান
            list.add(new StepItem(
                    "ইকামত দ্বিতীয় আযান ও জওয়াবের বিধান",
                    "ইকামত আযানের অংশ এবং এর জওয়াব আযানের মতই প্রদানের বিধান... [ফাতাওয়া ইসলামিয়্যাহ্ ১/২৪৯]",
                    "ইকামতকে দ্বিতীয় আযান বলা হয়, তাই ইকামতও এক প্রকার আযান।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৪৯]"
            ));

            // 3. ইকামতের শব্দের জওয়াব ও দরুদ-অসীলাহ্
            list.add(new StepItem(
                    "ইকামতের শব্দের জওয়াব ও দরুদ-অসীলাহ্",
                    "হাইয়্যা আলাস সালাতের জওয়াব, দরুদ ও অসীলার দুআ পাঠ... [মুসলিম, মিশকাত ৬৫৭নং]",
                    "সুতরাং এর জওয়াবও আযানের মতই। অবশ্য ‘হাইয়্যা আলাস সলা-হ্ ও ফালাহ্’ এর জওয়াবে ‘লাহাউলা অলা ক্বুওয়াতা ইল্লা বিল্লাহ্’ এবং শেষে (সময় পেলে) দরুদ ও অসীলার দুআ পাঠ করা বিধেয়। যেহেতু হাদীস শরীফে মুআযযিনের জওয়াব (তার মতই) বলতে এবং তার শেষে দরুদ ও অসীলার দুআ পড়তে আমাদেরকে আদেশ করা হয়েছে।\n\n[মুসলিম, মিশকাত ৬৫৭নং]"
            ));

            // 4. ‘ক্বাদ ক্বামাতিস স্বলাহ্’-এর জওয়াব ও যয়ীফ হাদীস বর্জন
            list.add(new StepItem(
                    "‘ক্বাদ ক্বামাতিস স্বলাহ্’-এর জওয়াব ও যয়ীফ হাদীস বর্জন",
                    "ক্বাদ ক্বামাতিস স্বলাহ্-এর শুদ্ধ জওয়াব ও 'আক্বামাহুল্লাহ'-এর তাহক্বীক্ব... [মিশকাত, আলবানীর টীকা ১/১২১]",
                    "উক্ত হাদীসের ভিত্তিতেই ‘ক্বাদ ক্বামাতিস স্বলাহ্ এর জওয়াবে ‘ক্বাদ ক্বামাতিস স্বলাহ্’ই বলতে হবে। নচেৎ এর জওয়াবে ‘আক্বামাহুল্লাহু অআদামাহা’ বলার হাদীস শুদ্ধ নয়। আর যয়ীফ হাদীসকে ভিত্তি করে শরীয়তের কোন আমল ও ইবাদত বৈধ নয়।\n\n[মিশকাত, আলবানীর টীকা ১/১২১]"
            ));

            // 5. ইকামতের জওয়াব সংক্রান্ত ভিন্ন মত
            list.add(new StepItem(
                    "ইকামতের জওয়াব সংক্রান্ত ভিন্ন মত",
                    "ইকামতের জবাব সুন্নত না হওয়ার মতামত ও পর্যালোচনা...",
                    "মতান্তরে যেহেতু ইকামতের জবাবে কোন স্পষ্ট সহীহ হাদীস নেই, তাই ইকামতের জবাব দেওয়া সুন্নত নয়।"
            ));

        } else {
            // English Mode
            // 1. Responding to Iqamah (Full Narrative)
            list.add(new StepItem(
                    "Responding to Iqamah",
                    "Iqamah as the second Adhan, manner of responding, and scholarly analysis... [Muslim, Fatawa Islamiyyah, Albani]",
                    "Iqamah is called the second Adhan, so Iqamah is also a form of Adhan.\n\n[Fatawa Islamiyyah, Saudi Scholars Committee 1/249]\n\nTherefore, its response is just like that of the Adhan. Of course, in response to 'Hayya 'alas-Salah' and 'Hayya 'alal-Falah', one should say 'La hawla wa la quwwata illa billah', and at the end (if time permits), reciting Salawat (Durood) and the Dua of Wasilah is prescribed. For in the authentic Hadith, we are commanded to repeat after the Mu'adhin (the same as what he says) and to recite Durood and the Dua of Wasilah at the end.\n\n[Sahih Muslim, Mishkat 657]\n\nBased on this hadith, in response to 'Qad Qamatis-Salah', one should say 'Qad Qamatis-Salah' itself. The hadith mentioning 'Aqamahallahu wa adamaha' as a response is not authentic. And no act of worship (Ibadah) or ruling in Shariah is valid based on a weak (Da'if) hadith.\n\n[Mishkat, Albani's Footnotes 1/121]\n\nAccording to an alternative scholarly opinion, since there is no explicit authentic hadith regarding responding to the Iqamah, responding to the Iqamah is not Sunnah."
            ));

            // 2. Iqamah as the Second Adhan
            list.add(new StepItem(
                    "Iqamah as the Second Adhan",
                    "Iqamah as a form of Adhan requiring a response... [Fatawa Islamiyyah 1/249]",
                    "Iqamah is called the second Adhan, so Iqamah is also a form of Adhan. [Fatawa Islamiyyah, Saudi Scholars Committee 1/249]"
            ));

            // 3. Responding to Iqamah Phrases and Reciting Wasilah
            list.add(new StepItem(
                    "Responding to Iqamah Phrases and Reciting Wasilah",
                    "Saying 'La hawla wa la quwwata illa billah' and reciting Salawat... [Muslim, Mishkat 657]",
                    "Therefore, its response is just like that of the Adhan. In response to 'Hayya 'alas-Salah' and 'Hayya 'alal-Falah', one says 'La hawla wa la quwwata illa billah', and Durood and Dua of Wasilah may be recited. [Sahih Muslim, Mishkat 657]"
            ));

            // 4. Responding to 'Qad Qamatis-Salah' and Da'if Hadith
            list.add(new StepItem(
                    "Responding to 'Qad Qamatis-Salah' and Da'if Hadith",
                    "Saying 'Qad Qamatis-Salah' and weakness of 'Aqamahallahu wa adamaha'... [Mishkat 1/121]",
                    "In response to 'Qad Qamatis-Salah', one repeats 'Qad Qamatis-Salah'. The narration mentioning 'Aqamahallahu wa adamaha' is not authentic. [Mishkat, Albani's Footnotes 1/121]"
            ));

            // 5. Alternative Scholarly View on Iqamah Response
            list.add(new StepItem(
                    "Alternative Scholarly View on Iqamah Response",
                    "Scholarly perspective that responding to Iqamah is not an established Sunnah...",
                    "According to an alternative scholarly opinion, since there is no explicit authentic hadith regarding responding to the Iqamah, responding to the Iqamah is not Sunnah."
            ));
        }

        return list;
    }
}
