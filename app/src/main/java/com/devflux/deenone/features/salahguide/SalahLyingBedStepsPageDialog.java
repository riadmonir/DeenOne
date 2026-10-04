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

public class SalahLyingBedStepsPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Lying in Bed");

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
            // 1. বিছানায় শুয়ে সালাত আদায়ের পূর্ণ পদ্ধতি
            list.add(new StepItem(
                    "বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি",
                    "প্রথমত: সালাতের জন্য নিয়ত করবেন... দ্বিতীয়ত: মাথার নিচে বালিশ দিয়ে মাথা উঁচু করে রুকু-সিজদার ইশারা...",
                    "প্রথমত: সালাতের জন্য নিয়ত করবেন। এরপর ক্বিবলার দিকে পা দিয়ে চিত হয়ে শুয়ে যেতে। যদি এটি সম্ভব না হয় তবে, উত্তর-দক্ষিণ হয়ে মাথা উত্তর দিকে রেখে ডান কাতে শুয়ে অথবা মাথা দক্ষিণ দিকে রেখে বাম কাতে শুয়ে যেতে হবে। অর্থাৎ যেদিকেই শুয়ে থাক না কেন ক্বিবলার দিকে মুখ ফিরিয়ে সালাতের কিয়াম অংশের কাজ শুরু করতে হবে। দ্বিতীয়ত: মাথার নিচে বালিশ দিয়ে মাথা যতটুকু সম্ভব উঁচু করে নিতে হবে এবং মাথার ইশারার মাধ্যমে রুকু-সিজদা করে নামাজ আদায় করা। এক্ষেত্রে রুকুর ইশারার চেয়ে সিজদার ইশারায় একটু বেশি ঝুঁকতে হবে। এছাড়া সালাতের অন্যান্য কাজগুলো ইশারার মাধ্যেমে সম্পন্ন করে সালাত শেষ করতে হবে।"
            ));

            // 2. ১ম ধাপ: নিয়ত ও ক্বিবলামুখী শোয়ার নিয়ম
            list.add(new StepItem(
                    "১ম ধাপ: নিয়ত ও ক্বিবলামুখী শোয়ার নিয়ম",
                    "সালাতের নিয়ত করে ক্বিবলার দিকে মুখ ফিরিয়ে কিয়াম অংশের কাজ শুরু করা...",
                    "প্রথমত: সালাতের জন্য নিয়ত করবেন। এরপর ক্বিবলার দিকে পা দিয়ে চিত হয়ে শুয়ে যেতে। যদি এটি সম্ভব না হয় তবে, উত্তর-দক্ষিণ হয়ে মাথা উত্তর দিকে রেখে ডান কাতে শুয়ে অথবা মাথা দক্ষিণ দিকে রেখে বাম কাতে শুয়ে যেতে হবে। অর্থাৎ যেদিকেই শুয়ে থাক না কেন ক্বিবলার দিকে মুখ ফিরিয়ে সালাতের কিয়াম অংশের কাজ শুরু করতে হবে।"
            ));

            // 3. ২য় ধাপ: মাথা উঁচু করা ও ইশারায় রুকু-সিজদা
            list.add(new StepItem(
                    "২য় ধাপ: মাথা উঁচু করা ও ইশারায় রুকু-সিজদা",
                    "মাথার নিচে বালিশ দিয়ে মাথা উঁচু করা এবং রুকুর চেয়ে সিজদায় বেশি ঝুঁকে সালাত শেষ করা...",
                    "দ্বিতীয়ত: মাথার নিচে বালিশ দিয়ে মাথা যতটুকু সম্ভব উঁচু করে নিতে হবে এবং মাথার ইশারার মাধ্যমে রুকু-সিজদা করে নামাজ আদায় করা। এক্ষেত্রে রুকুর ইশারার চেয়ে সিজদার ইশারায় একটু বেশি ঝুঁকতে হবে। এছাড়া সালাতের অন্যান্য কাজগুলো ইশারার মাধ্যেমে সম্পন্ন করে সালাত শেষ করতে হবে।"
            ));

        } else {
            // English Mode
            // 1. Complete Method of Prayer while Lying in Bed
            list.add(new StepItem(
                    "Method of Prayer while Lying in Bed",
                    "First, make intention and align with Qiblah... Second, elevate head and gesture Ruku-Sujud...",
                    "First: Form the intention (Niyyah) for Salah. Then lie on your back with your feet facing the Qiblah. If this is not possible, lie on your right side with your head facing north, or on your left side with your head facing south—meaning in whichever direction you lie, face towards the Qiblah to begin the Qiyam portion of the prayer. Second: Place a pillow under the head to elevate it as much as comfortably possible, and perform the prayer by gesturing Ruku and Sujud with head movements. In this case, bend the head slightly more for Sujud than for Ruku. In addition, complete all other actions of the prayer through gestures to conclude the Salah."
            ));

            // 2. Step 1: Intention & Body Alignment with Qiblah
            list.add(new StepItem(
                    "Step 1: Intention & Qiblah Alignment",
                    "Form intention and face towards Qiblah while lying on back or side...",
                    "First: Form the intention (Niyyah) for Salah. Then lie on your back with your feet facing the Qiblah. If this is not possible, lie on your right side with your head facing north, or on your left side with your head facing south—meaning in whichever direction you lie, face towards the Qiblah to begin the Qiyam portion of the prayer."
            ));

            // 3. Step 2: Elevating Head, Gesturing Ruku & Sujud
            list.add(new StepItem(
                    "Step 2: Head Elevation, Ruku & Sujud",
                    "Elevate head with a pillow and gesture Ruku and deeper Sujud...",
                    "Second: Place a pillow under the head to elevate it as much as comfortably possible, and perform the prayer by gesturing Ruku and Sujud with head movements. In this case, bend the head slightly more for Sujud than for Ruku. In addition, complete all other actions of the prayer through gestures to conclude the Salah."
            ));
        }

        return list;
    }
}
