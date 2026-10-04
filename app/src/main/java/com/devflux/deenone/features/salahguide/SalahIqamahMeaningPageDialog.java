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

public class SalahIqamahMeaningPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "ইকামতের অর্থ" : "Meaning of Iqamah");

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
            // 1. ইকামতের অর্থ (পূর্ণাঙ্গ বিবরণ)
            list.add(new StepItem(
                    "ইকামতের অর্থ",
                    "আল্লাহ সর্বশক্তিমান, আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই...",
                    "আল্লাহ সর্বশক্তিমান\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত\n\n" +
                            "নামাজের জন্য এসো\n\n" +
                            "সাফল্যের জন্য এসো\n\n" +
                            "নামাজ আরম্ভ হলো\n\n" +
                            "আল্লাহ্ মহান\n\n" +
                            "আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই"
            ));

            // 2. ইকামতের বাক্যাবলীর বাংলা অর্থ
            list.add(new StepItem(
                    "ইকামতের বাক্যাবলীর বাংলা অর্থ",
                    "আল্লাহু আকবার, আশহাদু আল-লা ইলাহা ইল্লাল্লাহ, ক্বাদ ক্বামাতিস সালাহ...",
                    "ইকামতের প্রতিটি বাক্যের বাংলা অর্থ:\n\n" +
                            "• আল্লাহু আকবার: আল্লাহ সর্বশক্তিমান\n" +
                            "• আশহাদু আল-লা ইলাহা ইল্লাল্লাহ: আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই\n" +
                            "• আশহাদু আন্না মুহাম্মাদার রাসুলুল্লাহ: আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত\n" +
                            "• হাইয়া আলাস সালাহ: নামাজের জন্য এসো\n" +
                            "• হাইয়া আলাল ফালাহ: সাফল্যের জন্য এসো\n" +
                            "• ক্বাদ ক্বামাতিস সালাহ: নামাজ আরম্ভ হলো\n" +
                            "• আল্লাহু আকবার: আল্লাহ্ মহান\n" +
                            "• লা ইলাহা ইল্লাল্লাহ: আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই"
            ));

            // 3. ইকামতের বিশেষ বাক্য
            list.add(new StepItem(
                    "ইকামতের বিশেষ বাক্য",
                    "ক্বাদ ক্বামাতিস সালাহ (قد قامت الصلاة) - নামাজ আরম্ভ হলো...",
                    "ইকামত প্রদানের সময় বিশেষ বাক্য:\n\n" +
                            "ক্বাদ ক্বামাতিস সালাহ (قد قامت الصلاة) — \"নামাজ আরম্ভ হলো\""
            ));

        } else {
            // English Mode
            // 1. Meaning of Iqamah
            list.add(new StepItem(
                    "Meaning of Iqamah",
                    "Allah is Almighty, I testify that there is no god but Allah...",
                    "Allah is Almighty\n\n" +
                            "I testify that there is no god but Allah\n\n" +
                            "I testify that Muhammad is the Messenger sent by Allah\n\n" +
                            "Come to prayer\n\n" +
                            "Come to success\n\n" +
                            "The prayer has begun\n\n" +
                            "Allah is the Greatest\n\n" +
                            "There is no deity except Allah"
            ));

            // 2. Sentences and Meaning of Iqamah
            list.add(new StepItem(
                    "Sentences and Meaning of Iqamah",
                    "Allahu Akbar, Ash-hadu alla ilaha illallah, Qad Qamatis-Salah...",
                    "Meaning of each sentence of Iqamah:\n\n" +
                            "• Allahu Akbar: Allah is Almighty\n" +
                            "• Ash-hadu alla ilaha illallah: I testify that there is no god but Allah\n" +
                            "• Ash-hadu anna Muhammadar Rasulullah: I testify that Muhammad is the Messenger sent by Allah\n" +
                            "• Hayya 'alas-Salah: Come to prayer\n" +
                            "• Hayya 'alal-Falah: Come to success\n" +
                            "• Qad Qamatis-Salah: The prayer has begun\n" +
                            "• Allahu Akbar: Allah is the Greatest\n" +
                            "• La ilaha illallah: There is no deity except Allah"
            ));

            // 3. Special Sentence of Iqamah
            list.add(new StepItem(
                    "Special Sentence for Iqamah",
                    "Qad Qamatis-Salah - \"The prayer has begun\"...",
                    "Special sentence recited during Iqamah:\n\n" +
                            "Qad Qamatis-Salah (قد قامت الصلاة) — \"The prayer has begun\""
            ));
        }

        return list;
    }
}
