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

public class SalahVirtuesPageDialog {

    public static class VirtueItem {
        public String title;
        public String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public VirtueItem(String title, String previewSubtitle, String fullContent) {
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
        binding.tvHeaderTitle.setText(isBn ? "পাঁচ ওয়াক্ত নামাজের ফজিলত" : "Virtues of Five Daily Prayers");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for future quick settings/options
        });

        List<VirtueItem> items = getVirtueItems(isBn);

        VirtuesAdapter adapter = new VirtuesAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class VirtuesAdapter extends RecyclerView.Adapter<VirtuesAdapter.VirtueViewHolder> {
        private final Context context;
        private final List<VirtueItem> items;
        private final boolean isBn;

        public VirtuesAdapter(Context context, List<VirtueItem> items, boolean isBn) {
            this.context = context;
            this.items = items;
            this.isBn = isBn;
        }

        @NonNull
        @Override
        public VirtueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahBasicsCardBinding binding = ItemSalahBasicsCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new VirtueViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull VirtueViewHolder holder, int position) {
            holder.bind(items.get(position), isBn);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class VirtueViewHolder extends RecyclerView.ViewHolder {
            private final ItemSalahBasicsCardBinding binding;

            public VirtueViewHolder(ItemSalahBasicsCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                TouchAnimationUtil.attachTouchSpring(binding.layoutToggleExpand);

                View.OnClickListener toggleClick = v -> {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                        VirtueItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(VirtueItem item, boolean isBn) {
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

    public static List<VirtueItem> getVirtueItems(boolean isBn) {
        List<VirtueItem> list = new ArrayList<>();

        if (isBn) {
            // ১. ফজর নামাজ
            list.add(new VirtueItem(
                    "ফজর নামাজ",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন: যে ব্যক্তি ফজর ও আসরের নামাজ আদায় করবে, সে জান্নাতে প্রবেশ করবে। [বুখারি -৫৭৪]",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন\n\n\n" +
                            "مَنْ صَلَّى الْبَرْدَيْنِ دَخَلَ الْجَنَّةَ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "মান সাল্লাল বার্দাইনী দাখালাল জান্নাহ\n\n\n" +
                            "অর্থ:\n\n" +
                            "যে ব্যক্তি ফজর ও আসরের নামাজ আদায় করবে, সে জান্নাতে প্রবেশ করবে।\n\n" +
                            "[বুখারি -৫৭৪]"
            ));

            // ২. যোহর নামাজ
            list.add(new VirtueItem(
                    "যোহর নামাজ",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন: কিয়ামতের দিন বান্দার প্রথম হিসাব হবে তার নামাজের ব্যাপারে। [তিরমিজি - ৪১৩]",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন\n\n\n" +
                            "إِنَّ أَوَّلَ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ مِنْ عَمَلِهِ الصَّلَاةُ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "ইন্না আওয়্বালা মা ইউহাসাবু বিহিল 'আবদু ইয়াওমাল কিয়ামাতি মিন 'আমালিহিস সালাহ\n\n\n" +
                            "অর্থ:\n\n" +
                            "কিয়ামতের দিন বান্দার প্রথম হিসাব হবে তার নামাজের ব্যাপারে।\n\n" +
                            "[তিরমিজি - ৪১৩]"
            ));

            // ৩. আসর নামাজ
            list.add(new VirtueItem(
                    "আসর নামাজ",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন: যে ব্যক্তি আসরের নামাজ আদায় করবে, সে আল্লাহর নিরাপত্তায় থাকবে। [মুসলিম - ৬৩৬]",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন\n\n\n" +
                            "مَنْ صَلَّى الْعَصْرَ فَهُوَ فِي ذِمَّةِ اللَّهِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "মান সাল্লাল 'আসর ফাহুয়া ফী যিম্মাতিল্লাহ\n\n\n" +
                            "অর্থ:\n\n" +
                            "যে ব্যক্তি আসরের নামাজ আদায় করবে, সে আল্লাহর নিরাপত্তায় থাকবে।\n\n" +
                            "[মুসলিম - ৬৩৬]"
            ));

            // ৪. মাগরিব নামাজ
            list.add(new VirtueItem(
                    "মাগরিব নামাজ",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন: যে ব্যক্তি জামাতের সাথে মাগরিবের নামাজ আদায় করবে, তার জন্য একটি মাকবুল হজের সওয়াব আছে। [তিরমিজি - ৫৬৪]",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন\n\n\n" +
                            "مَنْ صَلَّى الْمَغْرِبَ فِي جَمَاعَةٍ كَانَ لَهُ أَجْرُ حَجَّةٍ مَبْرُورَةٍ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "মান সাল্লাল মাগরিবা ফী জামা'আতিন কানা লাহু আজরু হাজ্জাতিন মাবরুরা\n\n\n" +
                            "অর্থ:\n\n" +
                            "যে ব্যক্তি জামাতের সাথে মাগরিবের নামাজ আদায় করবে, তার জন্য একটি মাকবুল হজের সওয়াব আছে।\n\n" +
                            "[তিরমিজি - ৫৬৪]"
            ));

            // ৫. ইশা নামাজ
            list.add(new VirtueItem(
                    "ইশা নামাজ",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন: যে ব্যক্তি জামাতের সাথে ইশার নামাজ আদায় করবে, সে যেন অর্ধরাত নামাজ পড়ল।\" [মুসলিম - ৬৫৬]",
                    "রাসূলুল্লাহ (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেন\n\n\n" +
                            "مَنْ صَلَّى الْعِشَاءَ فِي جَمَاعَةٍ كَانَ كَقِيَامِ نِصْفِ اللَّيْلِ\n\n\n" +
                            "উচ্চারণ:\n\n" +
                            "মান সাল্লাল 'ইশা ফী জামা'আতিন কানা কাকিয়ামি নিসফিল লাইল\n\n\n" +
                            "অর্থ:\n\n" +
                            "যে ব্যক্তি জামাতের সাথে ইশার নামাজ আদায় করবে, সে যেন অর্ধরাত নামাজ পড়ল।\"\n\n" +
                            "[মুসলিম - ৬৫৬]"
            ));
        } else {
            // English Mode
            // 1. Fajr Prayer
            list.add(new VirtueItem(
                    "Fajr Prayer",
                    "The Messenger of Allah (ﷺ) said: Whoever prays the two cool prayers (Fajr and Asr) will enter Paradise. [Sahih al-Bukhari - 574]",
                    "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "مَنْ صَلَّى الْبَرْدَيْنِ دَخَلَ الْجَنَّةَ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Man sallas-bardayni dakhalal-jannah\n\n\n" +
                            "Meaning:\n\n" +
                            "Whoever prays the two cool prayers (Fajr and Asr) will enter Paradise.\n\n" +
                            "[Sahih al-Bukhari - 574]"
            ));

            // 2. Dhuhr Prayer
            list.add(new VirtueItem(
                    "Dhuhr Prayer",
                    "The Messenger of Allah (ﷺ) said: The first matter that the slave will be brought to account for on the Day of Judgment is prayer. [Jami` at-Tirmidhi - 413]",
                    "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "إِنَّ أَوَّلَ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ مِنْ عَمَلِهِ الصَّلَاةُ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Inna awwala ma yuhasabu bihil-'abdu yawmal-qiyamati min 'amalihi as-salah\n\n\n" +
                            "Meaning:\n\n" +
                            "The first of man's actions for which he will be called to account on the Day of Resurrection will be his prayer.\n\n" +
                            "[Jami` at-Tirmidhi - 413]"
            ));

            // 3. Asr Prayer
            list.add(new VirtueItem(
                    "Asr Prayer",
                    "The Messenger of Allah (ﷺ) said: Whoever performs the Asr prayer is under the protection and covenant of Allah. [Sahih Muslim - 636]",
                    "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "مَنْ صَلَّى الْعَصْرَ فَهُوَ فِي ذِمَّةِ اللَّهِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Man sallas-'asra fahuwa fee zimmatillah\n\n\n" +
                            "Meaning:\n\n" +
                            "Whoever performs the Asr prayer is under the protection and covenant of Allah.\n\n" +
                            "[Sahih Muslim - 636]"
            ));

            // 4. Maghrib Prayer
            list.add(new VirtueItem(
                    "Maghrib Prayer",
                    "The Messenger of Allah (ﷺ) said: Whoever prays Maghrib in congregation will have the reward of an accepted Hajj. [Jami` at-Tirmidhi - 564]",
                    "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "مَنْ صَلَّى الْمَغْرِبَ فِي جَمَاعَةٍ كَانَ لَهُ أَجْرُ حَجَّةٍ مَبْرُورَةٍ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Man sallas-maghriba fee jama'atin kana lahu ajru hajjatim-mabrurah\n\n\n" +
                            "Meaning:\n\n" +
                            "Whoever prays Maghrib in congregation will have the reward of an accepted (mabrur) Hajj.\n\n" +
                            "[Jami` at-Tirmidhi - 564]"
            ));

            // 5. Isha Prayer
            list.add(new VirtueItem(
                    "Isha Prayer",
                    "The Messenger of Allah (ﷺ) said: Whoever prays Isha in congregation, it is as if he spent half of the night in prayer. [Sahih Muslim - 656]",
                    "The Messenger of Allah (ﷺ) said:\n\n\n" +
                            "مَنْ صَلَّى الْعِشَاءَ فِي جَمَاعَةٍ كَانَ كَقِيَامِ نِصْفِ اللَّيْلِ\n\n\n" +
                            "Transliteration:\n\n" +
                            "Man sallas-'isha fee jama'atin kana kaqiyami nisfal-layl\n\n\n" +
                            "Meaning:\n\n" +
                            "Whoever prays Isha in congregation, it is as if he spent half of the night in prayer.\n\n" +
                            "[Sahih Muslim - 656]"
            ));
        }

        return list;
    }
}
