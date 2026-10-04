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

public class SalahLyingBedRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "বিছানায় শুয়ে কিভাবে পড়ব" : "How to Pray while Lying on Bed");

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
            // 1. শুয়ে সালাত আদায়ের মূল বিধান ও হাদিস
            list.add(new StepItem(
                    "শুয়ে সালাত আদায়ের মূল বিধান ও হাদিস",
                    "ইসলামে শুয়ে সারাত আদায়ের প্রসঙ্গ জায়েজ করা হয়েছে... [বুখারী হা/১১১৭; মিশকাত হা/১২৪৮]",
                    "ইসলামে শুয়ে সারাত আদায়ের প্রসঙ্গ জায়েজ করা হয়েছে। তবে সেটি ইচ্ছাকৃত ভাবে শুয়ে সালাত পড়লে তার হক্ব আদায় হবে না। মুমিন ব্যক্তি অপারগ হলে আল্লাহর সুন্দর বিধান মতে সে শুয়েও সালাত পড়তে পারে। হাদীসে এসেছে রাসূল (সাঃ) বলেছেন-\n\nصَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَى جَنْبٍ\n\nঅর্থ:\nতুমি দাঁড়িয়ে সালাত আদায় করবে, তাতে সক্ষম না হলে বসে এবং বসতে সক্ষম না হলে শুয়ে সালাত আদায় করবে’।\n\n[বুখারী হা/১১১৭; মিশকাত হা/১২৪৮]"
            ));

            // 2. শুয়ে সালাতের অপরিহার্য শর্তাবলী
            list.add(new StepItem(
                    "শুয়ে সালাতের অপরিহার্য শর্তাবলী",
                    "ইচ্ছাকৃতভাবে না পড়ে অপারগতায় আল্লাহর দেওয়া ছাড় গ্রহণ...",
                    "শুয়ে সালাতের অপরিহার্য শর্তাবলী:\n\n• ইচ্ছাকৃতভাবে শুয়ে সালাত না পড়া: ইচ্ছাকৃত ভাবে শুয়ে সালাত পড়লে তার হক্ব আদায় হবে না।\n• অপারগতায় আল্লাহর বিধান গ্রহণ করা: মুমিন ব্যক্তি অপারগ হলে আল্লাহর সুন্দর বিধান মতে সে শুয়েও সালাত পড়তে পারে।\n• অসুস্থতায় ক্রমিক স্তর অনুসরণ: সুস্থ অবস্থায় দাঁড়িয়ে, সক্ষম না হলে বসে, এবং বসতে সক্ষম না হলে শুয়ে সালাত আদায় করা।"
            ));

            // 3. রাসূলুল্লাহ (সাঃ)-এর হাদিসের নির্দেশনা
            list.add(new StepItem(
                    "রাসূলুল্লাহ (সাঃ)-এর হাদিসের নির্দেশনা",
                    "صَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا... [বুখারী হা/১১১৭]",
                    "রাসূলুল্লাহ (সাঃ) বলেছেন:\n\nصَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَى جَنْبٍ\n\nঅর্থ:\n‘তুমি দাঁড়িয়ে সালাত আদায় করবে, তাতে সক্ষম না হলে বসে এবং বসতে সক্ষম না হলে শুয়ে সালাত আদায় করবে’।\n\n[বুখারী হা/১১১৭; মিশকাত হা/১২৪৮]"
            ));

        } else {
            // English Mode
            // 1. Rules and Hadith of Praying while Lying Down
            list.add(new StepItem(
                    "Rules and Hadith of Praying while Lying Down",
                    "Performing prayer while lying down is permissible when unable... [Bukhari 1117, Mishkat 1248]",
                    "In Islam, praying while lying down is permissible in cases of genuine physical inability. However, doing so deliberately without reason will not fulfill the prayer's obligation. If a believer is physically unable, by Allah's gracious dispensation, they may pray while lying down. In the Hadith, the Messenger of Allah (peace be upon him) said:\n\nصَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَى جَنْبٍ\n\nTranslation:\n'Pray standing; if you are unable, then sitting; and if you are unable, then lying on your side.'\n\n[Sahih al-Bukhari, Hadith 1117; Mishkat al-Masabih, Hadith 1248]"
            ));

            // 2. Essential Conditions for Praying while Lying Down
            list.add(new StepItem(
                    "Essential Conditions for Praying while Lying Down",
                    "Guidelines on genuine physical inability and Divine dispensations...",
                    "Essential Conditions for Praying while Lying Down:\n\n• Not Praying Lying Down Intentionally: Deliberately praying while lying down without a genuine health reason invalidates the duty.\n• Utilizing Divine Dispensation in Inability: When physically unable, a believer may gratefully utilize the dispensation provided by Allah.\n• Following the Step-by-Step Levels: Standing first; if unable, then sitting; if unable to sit, then lying down."
            ));

            // 3. Guidance from the Prophetic Hadith
            list.add(new StepItem(
                    "Guidance from the Prophetic Hadith",
                    "صَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا... [Sahih al-Bukhari 1117]",
                    "The Messenger of Allah (peace be upon him) said:\n\nصَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَى جَنْبٍ\n\nTranslation:\n'Pray standing; if you are unable, then sitting; and if you are unable, then lying on your side.'\n\n[Sahih al-Bukhari, Hadith 1117; Mishkat al-Masabih, Hadith 1248]"
            ));
        }

        return list;
    }
}
