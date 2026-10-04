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

public class SalahSittingRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "বসে বসে সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Sitting");

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
            // 1. হাদিসের আলোকে
            list.add(new StepItem(
                    "হাদিসের আলোকে",
                    "উক্ত হাদিস অনুযায়ী, অসুস্থ ব্যক্তির বসে সালাত আদায় করা সুন্নত...",
                    "হাদিসের আলোকে\n\nউক্ত হাদিস অনুযায়ী, অসুস্থ ব্যক্তির বসে সালাত আদায় করা সুন্নত।"
            ));

            // 2. ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "সালাত শুরু করার আগে নিয়ত করতে হবে...",
                    "নিয়ত (ইচ্ছা):\n\nসালাত শুরু করার আগে নিয়ত করতে হবে।"
            ));

            // 3. ২. তাকবির (আল্লাহু আকবার)
            list.add(new StepItem(
                    "২. তাকবির (আল্লাহু আকবার)",
                    "বসা অবস্থায় তাকবির দিয়ে সালাত শুরু করতে হবে...",
                    "তাকবির (আল্লাহু আকবার):\n\nবসা অবস্থায় তাকবির দিয়ে সালাত শুরু করতে হবে।"
            ));

            // 4. ৩. কিয়াম
            list.add(new StepItem(
                    "৩. কিয়াম",
                    "যদি সম্ভব হয়, বসে সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে...",
                    "কিয়াম:\n\nযদি সম্ভব হয়, বসে সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।"
            ));

            // 5. ৪. রুকু
            list.add(new StepItem(
                    "৪. রুকু",
                    "বসা অবস্থায় সামনের দিকে একটু ঝুঁকে রুকু করতে হবে...",
                    "রুকু:\n\nবসা অবস্থায় সামনের দিকে একটু ঝুঁকে রুকু করতে হবে।"
            ));

            // 6. ৫. সিজদা
            list.add(new StepItem(
                    "৫. সিজদা",
                    "বসা অবস্থায় মাটিতে সিজদা করতে হবে...",
                    "সিজদা:\n\nবসা অবস্থায় মাটিতে সিজদা করতে হবে।"
            ));

            // 7. বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি
            list.add(new StepItem(
                    "বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি",
                    "উক্ত হাদিস অনুযায়ী, যদি বসে সালাত আদায় করা সম্ভব না হয়...",
                    "বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি\n\nহাদিসের আলোকে-\n\nউক্ত হাদিস অনুযায়ী, যদি বসে সালাত আদায় করা সম্ভব না হয়, তবে শুয়ে ইশারায় সালাত আদায় করতে হবে।"
            ));

        } else {
            // English Mode
            // 1. According to the Hadith
            list.add(new StepItem(
                    "According to the Hadith",
                    "According to the mentioned Hadith, it is Sunnah for a sick person to pray while sitting...",
                    "According to the Hadith:\n\nAccording to the mentioned Hadith, it is Sunnah for a sick person to perform prayer while sitting."
            ));

            // 2. 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Intention must be made before starting the prayer...",
                    "Niyyah (Intention):\n\nIntention must be made before starting the prayer."
            ));

            // 3. 2. Takbir (Allahu Akbar)
            list.add(new StepItem(
                    "2. Takbir (Allahu Akbar)",
                    "The prayer must be initiated with Takbir while sitting...",
                    "Takbir (Allahu Akbar):\n\nThe prayer must be initiated with Takbir while in a sitting posture."
            ));

            // 4. 3. Qiyam
            list.add(new StepItem(
                    "3. Qiyam",
                    "If possible, recite Surah Al-Fatihah and another Surah while sitting...",
                    "Qiyam:\n\nIf possible, recite Surah Al-Fatihah and another Surah while sitting."
            ));

            // 5. 4. Ruku (Bowing)
            list.add(new StepItem(
                    "4. Ruku (Bowing)",
                    "Perform Ruku by leaning slightly forward while sitting...",
                    "Ruku:\n\nPerform Ruku by leaning slightly forward while in a sitting position."
            ));

            // 6. 5. Sujood (Prostration)
            list.add(new StepItem(
                    "5. Sujood (Prostration)",
                    "Perform Sujood on the ground from the sitting position...",
                    "Sujood:\n\nPerform Sujood directly on the ground from the sitting position."
            ));

            // 7. Method of Prayer while Lying in Bed
            list.add(new StepItem(
                    "Method of Prayer while Lying in Bed",
                    "According to the Hadith, if sitting is not possible, pray lying down with gestures...",
                    "Method of Prayer while Lying in Bed:\n\nAccording to the Hadith-\n\nAccording to the mentioned Hadith, if it is not possible to perform prayer while sitting, one should pray lying down using gestures."
            ));
        }

        return list;
    }
}
