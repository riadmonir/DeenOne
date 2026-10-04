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

public class SalahSickPersonRulesPageDialog {

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
        binding.tvHeaderTitle.setText(isBn ? "অসুস্থ ব্যক্তির সালাত আদায়ের পদ্ধতি" : "Method of Prayer for the Sick");

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
            // 1. কুরআনের আলোকে
            list.add(new StepItem(
                    "কুরআনের আলোকে",
                    "আল্লাহ তাআলা বলেন: فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ... [সূরা আত-তাগাবুন- ৬৪:১৬]",
                    "কুরআনের আলোকে:\n\nআল্লাহ তাআলা বলেন:\n\nفَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ\n\nউচ্চারণ:\nফাত্তাকুল্লাহা মা-স্তাতাতুম্\n\nঅর্থ:\nতোমরা আল্লাহকে যতটা সম্ভব ভয় করো。\n\n[সূরা আত-তাগাবুন- ৬৪:১৬]".replace("তোমরা আল্লাহকে যতটা সম্ভব ভয় করো。", "তোমরা আল্লাহকে যতটা সম্ভব ভয় করো।")
            ));

            // 2. হাদিসের আলোকে
            list.add(new StepItem(
                    "হাদিসের আলোকে",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেন - صَلِّ قَائِمًا... [সহীহ বুখারি- ১১১৭]",
                    "হাদিসের আলোকে:\n\nরাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেন -\n\nصَلِّ قَائِمًا فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَىٰ جَنْبٍ\n\nউচ্চারণ:\nসাল্লি ক্বায়িমান ফা-ইন্ লাম্ তাস্তাতি' ফা-ক্বা-ইদান, ফা-ইন্ লাম্ তাস্তাতি' ফা-আ'লা জান্বিন্।\n\nঅর্থ:\nতুমি দাঁড়িয়ে সালাত আদায় কর, যদি সক্ষম না হও তবে বসে, আর যদি সক্ষম না হও তবে শুয়ে।\n\n[সহীহ বুখারি- ১১১৭]"
            ));

            // 3. অসুস্থ ব্যক্তির সালাতের ধাপসমূহ
            list.add(new StepItem(
                    "অসুস্থ ব্যক্তির সালাতের ধাপসমূহ",
                    "দাঁড়িয়ে, বসে এবং শুয়ে সালাত আদায়ের ধারাবাহিক ধাপসমূহ...",
                    "অসুস্থ ব্যক্তির সালাতের ধাপসমূহ:\n\n• দাঁড়িয়ে: যদি সম্ভব হয়, দাঁড়িয়ে সালাত আদায় করুন।\n• বসে: যদি দাঁড়াতে সক্ষম না হন, তবে বসে সালাত আদায় করুন।\n• শুয়ে: যদি বসতেও সক্ষম না হন, তবে শুয়ে সালাত আদায় করুন।"
            ));

            // 4. ১. দাঁড়িয়ে সালাত আদায়
            list.add(new StepItem(
                    "১. দাঁড়িয়ে সালাত আদায়",
                    "যদি সম্ভব হয়, দাঁড়িয়ে সালাত আদায় করুন...",
                    "দাঁড়িয়ে:\n\nযদি সম্ভব হয়, দাঁড়িয়ে সালাত আদায় করুন।"
            ));

            // 5. ২. বসে সালাত আদায়
            list.add(new StepItem(
                    "২. বসে সালাত আদায়",
                    "যদি দাঁড়াতে সক্ষম না হন, তবে বসে সালাত আদায় করুন...",
                    "বসে:\n\nযদি দাঁড়াতে সক্ষম না হন, তবে বসে সালাত আদায় করুন।"
            ));

            // 6. ৩. শুয়ে সালাত আদায়
            list.add(new StepItem(
                    "৩. শুয়ে সালাত আদায়",
                    "যদি বসতেও সক্ষম না হন, তবে শুয়ে সালাত আদায় করুন...",
                    "শুয়ে:\n\nযদি বসতেও সক্ষম না হন, তবে শুয়ে সালাত আদায় করুন।"
            ));

        } else {
            // English Mode
            // 1. According to the Quran
            list.add(new StepItem(
                    "According to the Quran",
                    "Allah the Almighty says: فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ... [Surah At-Taghabun, 64:16]",
                    "According to the Quran:\n\nAllah the Almighty says:\n\nفَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ\n\nTransliteration:\nFattaqullaha mastata'tum\n\nTranslation:\nSo fear Allah as much as you are able.\n\n[Surah At-Taghabun, 64:16]"
            ));

            // 2. According to the Hadith
            list.add(new StepItem(
                    "According to the Hadith",
                    "The Messenger of Allah (peace be upon him) said: صَلِّ قَائِمًا... [Sahih al-Bukhari, 1117]",
                    "According to the Hadith:\n\nThe Messenger of Allah (peace be upon him) said -\n\nصَلِّ قَائِمًا فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَىٰ جَنْبٍ\n\nTransliteration:\nSalli qa'iman fa-in lam tastati' fa-qa'idan, fa-in lam tastati' fa-'ala janb.\n\nTranslation:\nPray standing; if you are unable, then sitting; and if you are unable, then lying on your side.\n\n[Sahih al-Bukhari, 1117]"
            ));

            // 3. Stages of Prayer for the Sick Person
            list.add(new StepItem(
                    "Stages of Prayer for the Sick Person",
                    "Step-by-step methods of prayer: standing, sitting, and lying down...",
                    "Stages of Prayer for the Sick Person:\n\n• Standing: If possible, perform the prayer standing.\n• Sitting: If unable to stand, perform the prayer sitting.\n• Lying Down: If unable to sit, perform the prayer lying down."
            ));

            // 4. 1. Praying while Standing
            list.add(new StepItem(
                    "1. Praying while Standing",
                    "If possible, perform the prayer standing...",
                    "Standing:\n\nIf possible, perform the prayer standing."
            ));

            // 5. 2. Praying while Sitting
            list.add(new StepItem(
                    "2. Praying while Sitting",
                    "If unable to stand, perform the prayer sitting...",
                    "Sitting:\n\nIf unable to stand, perform the prayer sitting."
            ));

            // 6. 3. Praying while Lying Down
            list.add(new StepItem(
                    "3. Praying while Lying Down",
                    "If unable to sit, perform the prayer lying down...",
                    "Lying Down:\n\nIf unable to sit, perform the prayer lying down."
            ));
        }

        return list;
    }
}
