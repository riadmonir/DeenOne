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

public class SalahIsharaRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইশারায় সালাত আদায়ের পদ্ধতি" : "Method of Gesture Prayer");

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
            // 1. ইশারায় সালাত আদায়ের বিধান
            list.add(new StepItem(
                    "ইশারায় সালাত আদায়ের বিধান",
                    "যদি কেউ শুয়ে সালাত আদায় করতে না পারে তবে ইশারায় সালাত আদায় করতে হবে...",
                    "যদি কেউ শুয়ে সালাত আদায় করতে না পারে তবে ইশারায় সালাত আদায় করতে হবে।"
            ));

            // 2. ইশারায় সালাতের ধাপসমূহ
            list.add(new StepItem(
                    "ইশারায় সালাতের ধাপসমূহ",
                    "নিয়ত, তাকবির, কিয়াম এবং রুকু ও সিজদা করার ধারাবাহিক নিয়ম...",
                    "ইশারায় সালাতের ধাপসমূহ:\n\n• নিয়ত (ইচ্ছা): সালাত শুরু করার আগে নিয়ত করতে হবে।\n• তাকবির (আল্লাহু আকবার): ইশারায় তাকবির দিয়ে সালাত শুরু করতে হবে।\n• কিয়াম: ইশারায় সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।\n• রুকু ও সিজদা: ইশারায় রুকু এবং সিজদা করতে হবে। রুকুর সময় কিছুটা কম ঝুঁকতে হবে এবং সিজদার সময় একটু বেশি ঝুঁকতে হবে।"
            ));

            // 3. ১. নিয়ত (ইচ্ছা)
            list.add(new StepItem(
                    "১. নিয়ত (ইচ্ছা)",
                    "সালাত শুরু করার আগে নিয়ত করতে হবে...",
                    "নিয়ত (ইচ্ছা):\n\nসালাত শুরু করার আগে নিয়ত করতে হবে।"
            ));

            // 4. ২. তাকবির (আল্লাহু আকবার)
            list.add(new StepItem(
                    "২. তাকবির (আল্লাহু আকবার)",
                    "ইশারায় তাকবির দিয়ে সালাত শুরু করতে হবে...",
                    "তাকবির (আল্লাহু আকবার):\n\nইশারায় তাকবির দিয়ে সালাত শুরু করতে হবে।"
            ));

            // 5. ৩. কিয়াম
            list.add(new StepItem(
                    "৩. কিয়াম",
                    "ইশারায় সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে...",
                    "কিয়াম:\n\nইশারায় সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।"
            ));

            // 6. ৪. রুকু ও সিজদা
            list.add(new StepItem(
                    "৪. রুকু ও সিজদা",
                    "ইশারায় রুকু এবং সিজদা করতে হবে। রুকুর সময় কিছুটা কম ঝুঁকতে হবে এবং সিজদার সময় একটু বেশি ঝুঁকতে হবে...",
                    "রুকু ও সিজদা:\n\nইশারায় রুকু এবং সিজদা করতে হবে। রুকুর সময় কিছুটা কম ঝুঁকতে হবে এবং সিজদার সময় একটু বেশি ঝুঁকতে হবে।"
            ));

        } else {
            // English Mode
            // 1. Rule of Gesture Prayer
            list.add(new StepItem(
                    "Rule of Gesture Prayer",
                    "If someone cannot pray while lying down, they must pray using gestures...",
                    "If someone cannot perform prayer while lying down, they must perform prayer using gestures."
            ));

            // 2. Stages of Gesture Prayer
            list.add(new StepItem(
                    "Stages of Gesture Prayer",
                    "Step-by-step methods of intention, Takbir, Qiyam, Ruku, and Sujood...",
                    "Stages of Gesture Prayer:\n\n• Niyyah (Intention): Intention must be made before starting the prayer.\n• Takbir (Allahu Akbar): The prayer must be initiated with Takbir using gestures.\n• Qiyam: Recite Surah Al-Fatihah and another Surah with gestures.\n• Ruku and Sujood: Perform Ruku and Sujood with gestures. Bow slightly less for Ruku and bow slightly more for Sujood."
            ));

            // 3. 1. Niyyah (Intention)
            list.add(new StepItem(
                    "1. Niyyah (Intention)",
                    "Intention must be made before starting the prayer...",
                    "Niyyah (Intention):\n\nIntention must be made before starting the prayer."
            ));

            // 4. 2. Takbir (Allahu Akbar)
            list.add(new StepItem(
                    "2. Takbir (Allahu Akbar)",
                    "The prayer must be initiated with Takbir using gestures...",
                    "Takbir (Allahu Akbar):\n\nThe prayer must be initiated with Takbir using gestures."
            ));

            // 5. 3. Qiyam
            list.add(new StepItem(
                    "3. Qiyam",
                    "Recite Surah Al-Fatihah and another Surah with gestures...",
                    "Qiyam:\n\nRecite Surah Al-Fatihah and another Surah with gestures."
            ));

            // 6. 4. Ruku and Sujood
            list.add(new StepItem(
                    "4. Ruku and Sujood",
                    "Perform Ruku and Sujood with gestures...",
                    "Ruku and Sujood:\n\nPerform Ruku and Sujood with gestures. Bow slightly less for Ruku and bow slightly more for Sujood."
            ));
        }

        return list;
    }
}
