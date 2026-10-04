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

public class SalahAzanJinnFearPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "জিন-ভূতের ভয়ে আযান" : "Adhan When Frightened by Spirits or Jinn");

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
            // 1. জিন-ভূতের ভয়ে আযান (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "জিন-ভূতের ভয়ে আযান",
                    "শয়তান জিন ভয় দেখালে আযান দিলে পলায়নের হাদীস ও সুহাইলের ঘটনা... [মুসলিম ৩৮৯নং]",
                    "শয়তান জিন মানুষকে ভয় দেখায়। ভয় পেয়ে আযান দিলে জিন বা শয়তান বা ভূত সব পালিয়ে যায়।\n\nসুহাইল বলেন, একদা আমার আব্বা আমাকে বনী হারেসায় পাঠান। আমার সঙ্গে ছিল এক সঙ্গী। এক বাগান হতে কে যেন নাম ধরে আমার সঙ্গীকে ডাক দিল। আমার সঙ্গী বাগানে খুঁজে দেখল; কিন্তু কাউকে দেখতে পেল না। ফিরে এলে আব্বার নিকট সে কথা উল্লেখ করলাম। আব্বা বললেন, যদি জানতাম যে, তুমি এই দেখতে পাবে, তাহলে তোমাকে পাঠাতাম না। তবে শোন! যখন (এই ধরনের) কোন শব্দ শুনবে, তখন নামাযের মত আযান দিও। কারণ, আমি আবূ হুরাইরা (রাঃ) কে আল্লাহর রসূল (ﷺ) হতে হাদীস বর্ণনা করতে শুনেছি, তিনি বলেছেন, “নামাযের আযান দেওয়া হলে শয়তান পাদতে পাদতে পালিয়ে যায়!”\n\n[মুসলিম, সহীহ ৩৮৯নং]"
            ));

            // 2. ভয় পেয়ে আযান দিলে জিন-শয়তান পলায়ন
            list.add(new StepItem(
                    "ভয় পেয়ে আযান দিলে জিন-শয়তান পলায়ন",
                    "শয়তান জিন ভয় দেখালে আযান ধ্বনির মাধ্যমে তা বিতাড়িত করার বিধান...",
                    "শয়তান জিন মানুষকে ভয় দেখায়। ভয় পেয়ে আযান দিলে জিন বা শয়তান বা ভূত সব পালিয়ে যায়।"
            ));

            // 3. সুহাইলের পিতার উপদেশ ও বনী হারেসার ঘটনা
            list.add(new StepItem(
                    "সুহাইলের পিতার উপদেশ ও বনী হারেসার ঘটনা",
                    "বাগানে অদৃশ্য আওয়াজ শোনা এবং সুহাইলের পিতার আযান দেওয়ার নির্দেশ...",
                    "সুহাইল বলেন, একদা আমার আব্বা আমাকে বনী হারেসায় পাঠান। আমার সঙ্গে ছিল এক সঙ্গী। এক বাগান হতে কে যেন নাম ধরে আমার সঙ্গীকে ডাক দিল। আমার সঙ্গী বাগানে খুঁজে দেখল; কিন্তু কাউকে দেখতে পেল না। ফিরে এলে আব্বার নিকট সে কথা উল্লেখ করলাম। আব্বা বললেন, যদি জানতাম যে, তুমি এই দেখতে পাবে, তাহলে তোমাকে পাঠাতাম না। তবে শোন! যখন (এই ধরনের) কোন শব্দ শুনবে, তখন নামাযের মত আযান দিও।"
            ));

            // 4. আযান ধ্বনিতে শয়তানের পলায়ন সংক্রান্ত হাদিস
            list.add(new StepItem(
                    "আযান ধ্বনিতে শয়তানের পলায়ন সংক্রান্ত হাদিস",
                    "আবু হুরাইরা (রাঃ) বর্ণিত হাদিস: আযান দিলে শয়তান পলায়ন করে...",
                    "কারণ, আমি আবূ হুরাইরা (রাঃ) কে আল্লাহর রসূল (ﷺ) হতে হাদীস বর্ণনা করতে শুনেছি, তিনি বলেছেন, “নামাযের আযান দেওয়া হলে শয়তান পাদতে পাদতে পালিয়ে যায়!”\n\n[মুসলিম, সহীহ ৩৮৯নং]"
            ));

        } else {
            // English Mode
            // 1. Adhan When Frightened by Spirits or Jinn (Full Narrative)
            list.add(new StepItem(
                    "Adhan When Frightened by Spirits or Jinn",
                    "Proclaiming Adhan to repel evil jinn and devils... [Sahih Muslim 389]",
                    "Devil jinn frighten humans. When someone becomes afraid and proclaims the Adhan, jinn, devils, or evil spirits all flee away.\n\nSuhail said: Once my father sent me to Banu Harithah. With me was a companion. From an orchard, someone called out my companion's name. My companion searched the garden, but could see no one. When we returned, I mentioned this to my father. My father said: 'If I had known you would experience this, I would not have sent you. But listen! Whenever you hear any such sound, proclaim the Adhan just like the call to prayer. For I heard Abu Hurairah (RA) narrating from the Messenger of Allah (ﷺ) that he said: When the call to prayer (Adhan) is proclaimed, Satan flees while breaking wind!'\n\n[Sahih Muslim 389]"
            ));

            // 2. Fleeing of Jinn & Devils Upon Hearing Adhan
            list.add(new StepItem(
                    "Fleeing of Jinn & Devils Upon Hearing Adhan",
                    "Calling Adhan to expel devils and fearful hallucinations...",
                    "Devil jinn attempt to frighten humans. When one proclaims the Adhan upon experiencing fear, jinn and devils flee immediately."
            ));

            // 3. The Story of Banu Harithah Orchard & Father's Advice
            list.add(new StepItem(
                    "The Story of Banu Harithah Orchard & Father's Advice",
                    "Hearing unseen voices in the garden and the instruction to proclaim Adhan...",
                    "Suhail related that while visiting Banu Harithah, an unseen voice called his companion's name. His father instructed him to proclaim Adhan whenever hearing such supernatural sounds."
            ));

            // 4. Prophetic Hadith: Satan Flees Upon Adhan
            list.add(new StepItem(
                    "Prophetic Hadith: Satan Flees Upon Adhan",
                    "Hadith of Abu Hurairah (RA): Satan flees breaking wind when Adhan is called...",
                    "The Messenger of Allah (ﷺ) said: 'When the call to prayer (Adhan) is proclaimed, Satan turns his back and flees while breaking wind so as not to hear the Adhan.' [Sahih Muslim 389]"
            ));
        }

        return list;
    }
}
