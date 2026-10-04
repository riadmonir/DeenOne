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

public class SalahWitrNiyyahPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "বিতর নামাজের নিয়ত" : "Niyyah and Method of Witr Prayer");

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
            // 1. বিতর নামাজের নিয়ত
            list.add(new StepItem(
                    "বিতর নামাজের নিয়ত",
                    "نويت أن أصلي ثلاث ركعات صلاة الوتر لله تعالى...",
                    "বিতর নামাজের নিয়ত:\n\nنويت أن أصلي ثلاث ركعات صلاة الوتر لله تعالى\n\nউচ্চারণ:\nনাওয়াইতু আন উসাল্লিয়া সালাতা ল-উইতির সালাসা রাকা'আতিন লিল্লাহি তাআলা।\"\n\nঅর্থ:\nআমি নিয়ত করলাম, তিন রাকাত বিতর ওয়াজিব নামাজ পড়ার জন্য কাবার দিকে মুখ করে, আল্লাহর জন্য।"
            ));

            // 2. বিতর নামাজ আদায়ের সংক্ষিপ্ত নিয়ম
            list.add(new StepItem(
                    "বিতর নামাজ আদায়ের সংক্ষিপ্ত নিয়ম",
                    "নিয়তের পর তাকবির দিয়ে শুরু... তৃতীয় রাকাতে কুনূতের দোয়া পড়ে সালাম ফিরিয়ে শেষ করা...",
                    "বিতর নামাজ আদায়ের সংক্ষিপ্ত নিয়ম:\n\nনিয়তের পর তাকবির (আল্লাহু আকবার) দিয়ে নামাজ শুরু করবেন। প্রথম দুই রাকাতে স্বাভাবিক ফরজ নামাজের মত কিরাআত করবেন। তৃতীয় রাকাতে সুরা পড়ার পর তাকবির দিয়ে হাত উঠিয়ে \"কুনূতের দোয়া\" পড়বেন। (যেমন: اللَّهُمَّ إِنَّا نَسْتَعِينُكَ...)\n\nএরপর রুকু ও সেজদা করে সালাম ফিরিয়ে নামাজ শেষ করবেন।"
            ));

            // 3. বিতর নামাজের ধারাবাহিক ধাপসমূহ
            list.add(new StepItem(
                    "বিতর নামাজের ধারাবাহিক ধাপসমূহ",
                    "১ম-২য় রাকাত, ৩য় রাকাতে তাকবীর ও দোয়ায়ে কুনূত, রুকু-সিজদা ও সালাম...",
                    "বিতর নামাজের ধারাবাহিক ধাপসমূহ:\n\n• ১ম ও ২য় রাকাত: নিয়তের পর তাকবির (আল্লাহু আকবার) দিয়ে নামাজ শুরু করবেন। প্রথম দুই রাকাতে স্বাভাবিক ফরজ নামাজের মত কিরাআত করবেন।\n• ৩য় রাকাত ও কুনূতের দোয়া: তৃতীয় রাকাতে সুরা পড়ার পর তাকবির দিয়ে হাত উঠিয়ে \"কুনূতের দোয়া\" পড়বেন। (যেমন: اللَّهُمَّ إِنَّا نَسْتَعِينُكَ...)\n• সমাপ্তি: এরপর রুকু ও সেজদা করে সালাম ফিরিয়ে নামাজ শেষ করবেন।"
            ));

        } else {
            // English Mode
            // 1. Niyyah for Witr Prayer
            list.add(new StepItem(
                    "Niyyah for Witr Prayer",
                    "نويت أن أصلي ثلاث ركعات صلاة الوتر لله تعالى... [Intention for 3 Rak'ahs Witr]",
                    "Niyyah for Witr Prayer:\n\nنويت أن أصلي ثلاث ركعات صلاة الوتر لله تعالى\n\nTransliteration:\nNawaytu an usalliya salatal-witri thalatha raka'atin lillahi ta'ala.\n\nTranslation:\n'I intend to perform three Rak'ahs of the Wajib Witr prayer, facing the Ka'bah, for the sake of Allah.'"
            ));

            // 2. Summary Method of Witr Prayer
            list.add(new StepItem(
                    "Summary Method of Witr Prayer",
                    "Start with Takbir... recite Surah & Dua Qunut in 3rd Rak'ah, then complete with Salam...",
                    "Summary Method of Performing Witr Prayer:\n\nAfter the intention, begin the prayer with Takbir (Allahu Akbar). In the first two Rak'ahs, recite Qur'an as in regular Fard prayers. In the third Rak'ah, after reciting Surah al-Fatihah and another Surah, say Takbir, raise your hands, and recite \"Dua Qunut\" (e.g., اللَّهُمَّ إِنَّا نَسْتَعِينُكَ...).\n\nThen perform Ruku and Sujud, and finish the prayer by offering Salam."
            ));

            // 3. Step-by-Step Stages of Witr Prayer
            list.add(new StepItem(
                    "Step-by-Step Stages of Witr Prayer",
                    "1st & 2nd Rak'ahs, 3rd Rak'ah Takbir & Dua Qunut, Ruku, Sujud & Salam...",
                    "Step-by-Step Stages of Witr Prayer:\n\n• 1st & 2nd Rak'ahs: Begin with Takbir (Allahu Akbar) after Niyyah. Recite as in regular prayers.\n• 3rd Rak'ah & Dua Qunut: In the 3rd Rak'ah, after recitation, raise hands with Takbir and recite \"Dua Qunut\" (e.g., Allahumma inna nasta'inuka...).\n• Completion: Perform Ruku and Sujud, then conclude the prayer with Salam."
            ));
        }

        return list;
    }
}
