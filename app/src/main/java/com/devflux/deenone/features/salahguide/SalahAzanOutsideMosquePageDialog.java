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

public class SalahAzanOutsideMosquePageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "মসজিদ ছাড়া অন্য স্থানে আযান" : "Adhan in Places Other Than Mosques");

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
            // 1. মসজিদ ছাড়া অন্য স্থানে আযান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "মসজিদ ছাড়া অন্য স্থানে আযান",
                    "ভয়, শত্রুতা, মসজিদ দূরে থাকা বা নির্জন প্রান্তরে আযান-ইকামতের বিধান... [বুখারী, মুসলিম, আবূদাঊদ]",
                    "ভয়, শত্রুতা প্রভৃতির কারণে মসজিদে যেতে বাধা থাকলে, মসজিদ বহু দূরে হলে (এবং আযান শুনতে না পেলে), সফরে কোন নির্জন প্রান্তরে থাকলে, যে জায়গায় থাকবে সেই জায়গাতেই নামাযের সময় হলে আযান-ইকামত দিয়ে নামায আদায় করতে হবে। একা হলে আযান ওয়াজেব না হলেও সুন্নত অবশ্যই বটে।\n\nমহানবী (ﷺ) বলেন, “যখন সফরে থাকবে, তখন তোমরা আযান দিও এবং ইকামত দিও। আর তোমাদের মধ্যে যে বড় সে ইমামতি করো।\n\n[বুখারী, মিশকাত ৬৮২নং]\n\nতাছাড়া আল্লাহর নবী (ﷺ) এবং সাহাবাগণ সফরে থাকলে ফাঁকা মাঠে আযান দিয়ে নামায পড়েছেন।\n\n[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]\n\nমহানবী (ﷺ) বলেন, “তোমার প্রতিপালক বিস্মিত হন পর্বত চূড়ায় সেই ছাগলের রাখালকে দেখে যে নামাযের জন্য আযান দিয়ে (সেখানেই) নামায আদায় করে; আল্লাহ আযযা অজাল্ল্ বলেন, “তোমরা আমার এই বান্দাকে লক্ষ্য কর, (এমন জায়গাতেও) আযান দিয়ে নামায কায়েম করছে! সে আমাকে ভয় করে। আমি তাকে ক্ষমা করে দিলাম এবং জান্নাতে প্রবেশ করালাম।\n\n[আবূদাঊদ, সুনান, নাসাঈ, সুনান, সহিহ তারগিব ২৩৯ নং]\n\nতিনি বলেন, “কোন ব্যক্তি যখন কোন বৃক্ষ-পানিহীন প্রান্তরে থাকে, অতঃপর সেখানে নামাযের সময় উপস্থিত হয়, তখন সে যেন ওযু করে। পানি না পেলে যেন তায়াম্মুম করে। অতঃপর সে যদি শুধু ইকামত দিয়ে নামায পড়ে, তাহলে তার সাথে তার সঙ্গী দুই ফিরিশ্তা নামায পড়েন। কিন্তু সে যদি আযান দিয়ে ও ইকামত দিয়ে নামায পড়ে, তাহলে তার পশ্চাতে আল্লাহর এত ফিরিশ্তা নামায পড়েন, যাদের দুই প্রান্ত নজরে আসে না!”\n\n[আব্দুর রাযযাক, মুসান্নাফ, সহিহ তারগিব ২৪১নং]\n\nআর একদা তিনি আব্দুল্লাহ বিন আব্দুর রহ্মানকে মরুভূমিতে ছাগপালে থাকাকালে নামাযের জন্য উচ্চশব্দে আযান দিতে আদেশ করেছিলেন।\n\n[বুখারী প্রমুখ, মিশকাত ৬৫৬নং]"
            ));

            // 2. সফর ও নির্জন প্রান্তরে আযানের বিধান ও আমল
            list.add(new StepItem(
                    "সফর ও নির্জন প্রান্তরে আযানের বিধান ও আমল",
                    "মসজিদ বহু দূরে বা একা থাকলে আযান-ইকামত দেওয়া সুন্নত, রাসুলুল্লাহ (ﷺ) ও সাহাবীগণের আমল...",
                    "ভয়, শত্রুতা প্রভৃতির কারণে মসজিদে যেতে বাধা থাকলে, মসজিদ বহু দূরে হলে (এবং আযান শুনতে না পেলে), সফরে কোন নির্জন প্রান্তরে থাকলে, যে জায়গায় থাকবে সেই জায়গাতেই নামাযের সময় হলে আযান-ইকামত দিয়ে নামায আদায় করতে হবে। একা হলে আযান ওয়াজেব না হলেও সুন্নত অবশ্যই বটে।\n\nমহানবী (ﷺ) বলেন, “যখন সফরে থাকবে, তখন তোমরা আযান দিও এবং ইকামত দিও। আর তোমাদের মধ্যে যে বড় সে ইমামতি করো।\n\n[বুখারী, মিশকাত ৬৮২নং]\n\nতাছাড়া আল্লাহর নবী (ﷺ) এবং সাহাবাগণ সফরে থাকলে ফাঁকা মাঠে আযান দিয়ে নামায পড়েছেন।\n\n[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]"
            ));

            // 3. পর্বত চূড়ায় রাখালের আযান ও আল্লাহর বিস্ময়
            list.add(new StepItem(
                    "পর্বত চূড়ায় রাখালের আযান ও আল্লাহর বিস্ময়",
                    "পর্বতের চূড়ায় আযান দিয়ে নামাজ আদায়কারী রাখালকে ক্ষমা ও জান্নাতে প্রবেশের সুসংবাদ...",
                    "মহানবী (ﷺ) বলেন, “তোমার প্রতিপালক বিস্মিত হন পর্বত চূড়ায় সেই ছাগলের রাখালকে দেখে যে নামাযের জন্য আযান দিয়ে (সেখানেই) নামায আদায় করে; আল্লাহ আযযা অজাল্ল্ বলেন, “তোমরা আমার এই বান্দাকে লক্ষ্য কর, (এমন জায়গাতেও) আযান দিয়ে নামায কায়েম করছে! সে আমাকে ভয় করে। আমি তাকে ক্ষমা করে দিলাম এবং জান্নাতে প্রবেশ করালাম।\n\n[আবূদাঊদ, সুনান, নাসাঈ, সুনান, সহিহ তারগিব ২৩৯ নং]"
            ));

            // 4. মরুপ্রান্তরে অগণিত ফিরিশতার জামাত ও উচ্চশব্দে আযান
            list.add(new StepItem(
                    "মরুপ্রান্তরে অগণিত ফিরিশতার জামাত ও উচ্চশব্দে আযান",
                    "বৃক্ষ-পানিহীন প্রান্তরে আযান দিলে দিগন্তজোড়া ফিরিশতাদের জামাত ও উচ্চশব্দে আযানের নির্দেশ...",
                    "তিনি বলেন, “কোন ব্যক্তি যখন কোন বৃক্ষ-পানিহীন প্রান্তরে থাকে, অতঃপর সেখানে নামাযের সময় উপস্থিত হয়, তখন সে যেন ওযু করে। পানি না পেলে যেন তায়াম্মুম করে। অতঃপর সে যদি শুধু ইকামত দিয়ে নামায পড়ে, তাহলে তার সাথে তার সঙ্গী দুই ফিরিশ্তা নামায পড়েন। কিন্তু সে যদি আযান দিয়ে ও ইকামত দিয়ে নামায পড়ে, তাহলে তার পশ্চাতে আল্লাহর এত ফিরিশ্তা নামায পড়েন, যাদের দুই প্রান্ত নজরে আসে না!”\n\n[আব্দুর রাযযাক, মুসান্নাফ, সহিহ তারগিব ২৪১নং]\n\nআর একদা তিনি আব্দুল্লাহ বিন আব্দুর রহ্মানকে মরুভূমিতে ছাগপালে থাকাকালে নামাযের জন্য উচ্চশব্দে আযান দিতে আদেশ করেছিলেন।\n\n[বুখারী প্রমুখ, মিশকাত ৬৫৬নং]"
            ));

        } else {
            // English Mode
            // 1. Adhan in Places Other Than Mosques (Full Narrative)
            list.add(new StepItem(
                    "Adhan in Places Other Than Mosques",
                    "Calling Adhan in remote wilderness, during travel, or when far from mosques... [Bukhari, Muslim, Abu Dawud]",
                    "If prevented from going to the mosque due to fear, hostility, or extreme distance where the call cannot be heard, or when in an open wilderness during travel, one should proclaim the Adhan and Iqamah wherever they are. Even if praying alone, calling Adhan is an established Sunnah.\n\nThe Prophet (ﷺ) said: 'When you are on a journey, proclaim the Adhan and Iqamah, and let the oldest among you lead the prayer.'\n\n[Bukhari, Mishkat 682]\n\nThe Prophet (ﷺ) and his companions regularly called Adhan and prayed in open fields during travel.\n\n[Sahih Muslim 681]\n\nThe Prophet (ﷺ) said: 'Your Lord is amazed by a shepherd high on a mountain peak who calls the Adhan and performs prayer. Allah Almighty says: Look at this servant of Mine, calling the Adhan and establishing prayer out of fear of Me; I have forgiven him and admitted him into Paradise.'\n\n[Sunan Abi Dawud, Nasa'i, Sahih at-Targhib 239]\n\nHe also said: 'When a person is in an uninhabited, barren wilderness and prayer time arrives, let him make Wudu (or Tayammum if no water is found). If he only offers Iqamah, his two companion angels pray with him. But if he calls both Adhan and Iqamah, countless rows of Allah's angels pray behind him, spanning beyond the horizon!'\n\n[Musannaf Abdur Razzaq, Sahih at-Targhib 241]\n\nHe also commanded Abdullah ibn Abdur-Rahman to raise his voice with Adhan when grazing flocks in the desert.\n\n[Bukhari, Mishkat 656]"
            ));

            // 2. Rulings of Adhan during Travel & Remote Places
            list.add(new StepItem(
                    "Rulings of Adhan during Travel and Remote Places",
                    "Sunnah to call Adhan and Iqamah individually when far from mosques...",
                    "When far from mosques or traveling in remote wilderness, calling Adhan and Iqamah is an established Sunnah. The Prophet (ﷺ) said: 'When traveling, call the Adhan and Iqamah, and let the eldest lead.' [Bukhari, Mishkat 682]"
            ));

            // 3. The Shepherd on the Mountain Peak
            list.add(new StepItem(
                    "The Shepherd on the Mountain Peak",
                    "Allah's amazement, forgiveness, and entry into Paradise for calling Adhan on mountains...",
                    "Allah praises the shepherd calling Adhan on mountain peaks: 'Look at My servant who calls Adhan and establishes prayer fearing Me; I have forgiven him and entered him into Paradise.' [Sunan Abi Dawud 239]"
            ));

            // 4. Boundless Rows of Angels in the Wilderness
            list.add(new StepItem(
                    "Boundless Rows of Angels in the Wilderness",
                    "Countless angels praying behind the one who proclaims Adhan in the desert...",
                    "When Adhan and Iqamah are proclaimed in barren wilderness, countless rows of angels pray behind the worshipper spanning both horizons [Abdur Razzaq 241]. Raising one's voice with Adhan in open lands is explicitly commanded [Bukhari 656]."
            ));
        }

        return list;
    }
}
