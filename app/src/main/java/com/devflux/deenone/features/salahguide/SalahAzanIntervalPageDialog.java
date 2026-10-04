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

public class SalahAzanIntervalPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "আযান ও ইকামতের মাঝে ব্যবধান" : "Interval Between Adhan and Iqamah");

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
            // 1. আযান ও ইকামতের মাঝে ব্যবধান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "আযান ও ইকামতের মাঝে ব্যবধান",
                    "আযান ও ইকামতের বিরতি, ওযুর সময় ও মাগরিবের জামাআতের পূর্ববর্তী নামায... [বুখারী, মুসলিম]",
                    "আযান ও ইকামতের মাঝে কতটা বিরতি থাকবে সে ব্যাপারে হাদীস শরীফে কোন স্পষ্ট ইঙ্গিত ও উল্লেখ পাওয়া যায় না। তবে আযান হয় জামাআত ডাকার জন্য। আর এটাই স্বাভাবিক যে, আযানের পর অনেকে ওযু করবে। সুতরাং ওযু করার মত সময় দিতে হবে। তাছাড়া ফরয নামাযের পূর্বে যে সুন্নাতে রাতেবাহ্ বা মুআক্কাদাহ আছে তাও পড়ার জন্য সময় দিতে হবে। মহানবী (ﷺ) বলেন, প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে। এইরুপ তিনবার বলার পর শেষে বললেন, “যে চাইবে তার জন্য।\n\n[বুখারী, মুসলিম, মিশকাত ৬৬২নং]\n\nমাগরেবের আযানের পরেও সত্বর জামাআত শুরু করা উচিৎ নয়। যদিও সময় সংকীর্ণ তবুও জামাআত হওয়ার পূর্বে নামায আছে। সুতরাং যার সেই নামায পড়ার ইচ্ছা তাকে সেই নামায পড়তে সময় দেওয়া উচিৎ।\n\nআনাস (রাঃ) বলেন, আমরা মদ্বীনায় ছিলাম। মুআযযিন যখন মাগরেবের আযান দিত, তখন লোকেরা প্রতিযোগিতার সাথে মসজিদের খাম্বাগুলোর পশ্চাতে ২ রাকআত নামায পড়তে লেগে যেত। এমনকি যদি কোন অজানা লোক এসে মসজিদে প্রবেশ করত, তাহলে এত লোকের নামায পড়া দেখে সে মনে করত, হয়তো মাগরেবের জামাআত হয়ে গেছে। (এবং ওরা পরের সুন্নত পড়ছে।)\n\n[মুসলিম, মিশকাত ১১৮০ নং]"
            ));

            // 2. আযান ও ইকামতের বিরতির সাধারণ নীতিমালা ও হাদীস
            list.add(new StepItem(
                    "আযান ও ইকামতের বিরতির সাধারণ নীতিমালা ও হাদীস",
                    "ওযু ও সুন্নাতে রাতেবাহ পড়ার পর্যাপ্ত সময় প্রদানের বিধান... [বুখারী, মুসলিম, মিশকাত ৬৬২নং]",
                    "আযান ও ইকামতের মাঝে কতটা বিরতি থাকবে সে ব্যাপারে হাদীস শরীফে কোন স্পষ্ট ইঙ্গিত ও উল্লেখ পাওয়া যায় না। তবে আযান হয় জামাআত ডাকার জন্য। আর এটাই স্বাভাবিক যে, আযানের পর অনেকে ওযু করবে। সুতরাং ওযু করার মত সময় দিতে হবে। তাছাড়া ফরয নামাযের পূর্বে যে সুন্নাতে রাতেবাহ্ বা মুআক্কাদাহ আছে তাও পড়ার জন্য সময় দিতে হবে। মহানবী (ﷺ) বলেন, প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে। এইরুপ তিনবার বলার পর শেষে বললেন, “যে চাইবে তার জন্য।\n\n[বুখারী, মুসলিম, মিশকাত ৬৬২নং]"
            ));

            // 3. মাগরিবের আযানের পর জামাআত শুরু করার বিধান
            list.add(new StepItem(
                    "মাগরিবের আযানের পর জামাআত শুরু করার বিধান",
                    "মাগরিবের জামাআত অবিলম্বে শুরু না করে নামাযের ইচ্ছা পোষণকারীকে সময় দেওয়া...",
                    "মাগরেবের আযানের পরেও সত্বর জামাআত শুরু করা উচিৎ নয়। যদিও সময় সংকীর্ণ তবুও জামাআত হওয়ার পূর্বে নামায আছে। সুতরাং যার সেই নামায পড়ার ইচ্ছা তাকে সেই নামায পড়তে সময় দেওয়া উচিৎ।"
            ));

            // 4. মাগরিবের আযানের পর সাহাবীদের দুই রাকআত নামাযের আমল
            list.add(new StepItem(
                    "মাগরিবের আযানের পর সাহাবীদের দুই রাকআত নামাযের আমল",
                    "হযরত আনাস (রাঃ)-এর বর্ণনা ও সাহাবীদের দুই রাকআত পড়ার প্রতিযোগিতা... [মুসলিম, মিশকাত ১১৮০ নং]",
                    "আনাস (রাঃ) বলেন, আমরা মদ্বীনায় ছিলাম। মুআযযিন যখন মাগরেবের আযান দিত, তখন লোকেরা প্রতিযোগিতার সাথে মসজিদের খাম্বাগুলোর পশ্চাতে ২ রাকআত নামায পড়তে লেগে যেত। এমনকি যদি কোন অজানা লোক এসে মসজিদে প্রবেশ করত, তাহলে এত লোকের নামায পড়া দেখে সে মনে করত, হয়তো মাগরেবের জামাআত হয়ে গেছে। (এবং ওরা পরের সুন্নত পড়ছে।)\n\n[মুসলিম, মিশকাত ১১৮০ নং]"
            ));

        } else {
            // English Mode
            // 1. Interval Between Adhan & Iqamah (Full Narrative)
            list.add(new StepItem(
                    "Interval Between Adhan and Iqamah",
                    "Interval duration, time for Wudu, Sunnah prayers and Maghrib Sunnah before congregation... [Bukhari, Muslim]",
                    "There is no explicit specification in the authentic Hadith regarding the exact duration of the interval between Adhan and Iqamah. However, Adhan is called to invite people to the congregational prayer. It is natural that many will perform ablution (Wudu) after the Adhan. Therefore, sufficient time must be given for performing Wudu. Furthermore, time must also be provided to perform the Sunnah Ratibah (regular confirmed Sunnah prayers) before the obligatory (Fard) prayer. The Prophet (ﷺ) said: 'Between every two calls (Adhan and Iqamah) there is a prayer.' He repeated this three times, and on the third time added: 'For whoever wishes.'\n\n[Sahih Bukhari, Sahih Muslim, Mishkat 662]\n\nEven after the Maghrib Adhan, the congregation should not be rushed immediately. Although the time is relatively narrow, there is still prayer before the congregational prayer begins. Therefore, whoever wishes to offer that prayer should be given sufficient time to pray.\n\nAnas (RA) narrated: We were in Madinah. When the Mu'adhin called the Adhan for Maghrib, the people would rush eagerly to the pillars of the mosque to pray two Rak'ahs (Sunnah), so much so that if a stranger entered the mosque, seeing so many people praying, he would think that the congregational Maghrib prayer had already taken place (and they were offering the post-Fard Sunnah).\n\n[Sahih Muslim, Mishkat 1180]"
            ));

            // 2. General Guidelines & Hadith on Adhan-Iqamah Interval
            list.add(new StepItem(
                    "General Guidelines & Hadith on Adhan-Iqamah Interval",
                    "Providing sufficient time for Wudu and Sunnah Ratibah before Fard... [Bukhari, Muslim]",
                    "There is no explicit specification in the authentic Hadith regarding the exact duration of the interval between Adhan and Iqamah. However, Adhan is called to invite people to the congregational prayer. It is natural that many will perform ablution (Wudu) after the Adhan. Therefore, sufficient time must be given for performing Wudu. Furthermore, time must also be provided to perform the Sunnah Ratibah (regular confirmed Sunnah prayers) before the obligatory (Fard) prayer. The Prophet (ﷺ) said: 'Between every two calls (Adhan and Iqamah) there is a prayer.' He repeated this three times, and on the third time added: 'For whoever wishes.'\n\n[Sahih Bukhari, Sahih Muslim, Mishkat 662]"
            ));

            // 3. Rules on Starting the Maghrib Congregation
            list.add(new StepItem(
                    "Rules on Starting the Maghrib Congregation",
                    "Allowing worshippers time to pray before starting the Maghrib Fard...",
                    "Even after the Maghrib Adhan, the congregation should not be rushed immediately. Although the time is relatively narrow, there is still prayer before the congregational prayer begins. Therefore, whoever wishes to offer that prayer should be given sufficient time to pray."
            ));

            // 4. Companions' Practice of Praying 2 Rak'ahs After Maghrib Adhan
            list.add(new StepItem(
                    "Companions' Practice of Praying 2 Rak'ahs After Maghrib Adhan",
                    "Narration of Anas (RA) on Companions praying behind mosque pillars... [Muslim, Mishkat 1180]",
                    "Anas (RA) narrated: We were in Madinah. When the Mu'adhin called the Adhan for Maghrib, the people would rush eagerly to the pillars of the mosque to pray two Rak'ahs (Sunnah), so much so that if a stranger entered the mosque, seeing so many people praying, he would think that the congregational Maghrib prayer had already taken place (and they were offering the post-Fard Sunnah).\n\n[Sahih Muslim, Mishkat 1180]"
            ));
        }

        return list;
    }
}
