package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetSalahGuideBinding;
import com.devflux.deenone.features.salahguide.adapter.SalahStepAdapter;
import com.devflux.deenone.features.salahguide.model.SalahTopicItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class SalahGuidePageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        BottomSheetSalahGuideBinding binding = BottomSheetSalahGuideBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        SalahGuideViewModel salahVm = (activity instanceof ViewModelStoreOwner)
                ? new ViewModelProvider((ViewModelStoreOwner) activity).get(SalahGuideViewModel.class)
                : new SalahGuideViewModel();

        SalahStepAdapter adapter = new SalahStepAdapter();
        binding.rvSalahSteps.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvSalahSteps.setAdapter(adapter);

        binding.btnCloseSalahSheet.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseSalahSheet);

        int currentTheme = ThemeManager.getSavedThemeMode(activity);
        if (currentTheme == ThemeManager.THEME_LIGHT) {
            binding.ivSalahThemeIcon.setImageResource(R.drawable.ic_moon);
        } else {
            binding.ivSalahThemeIcon.setImageResource(R.drawable.ic_sun);
        }

        binding.btnSalahThemeToggle.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleAppTheme();
                dialog.dismiss();
            } else {
                int nextMode = (currentTheme == ThemeManager.THEME_DARK) ? ThemeManager.THEME_LIGHT : ThemeManager.THEME_DARK;
                ThemeManager.setThemeMode(activity, nextMode);
                dialog.dismiss();
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSalahThemeToggle);

        binding.btnSalahNotification.setOnClickListener(v -> {
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).showNotificationHistorySheet();
            }
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSalahNotification);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
        salahVm.loadTopics(isBn);

        if (isBn) {
            binding.tvSalahSubheaderTitle.setText("পরিপূর্ণ নামাজ শিক্ষা ও গাইড");
            binding.tvSalahSubheaderSubtitle.setText("সহীহ হাদিস ও সুন্নাহর আলোকে");
            binding.tvStepsSectionHeader.setText("ধারাবাহিক নিয়ম, মাসনূন কিরাত ও দোয়া:");
            binding.etSalahGuideSearch.setHint("নামাজ, দোয়া বা কিরাত দিয়ে খুঁজুন...");
            binding.chipSalahAll.setText("সব বিষয়");
            binding.chipSalahPurity.setText("ওজু ও শর্ত");
            binding.chipSalahFardh.setText("৫ ওয়াক্ত ফরজ");
            binding.chipSalahSunnah.setText("সুন্নাত ও বিতর");
            binding.chipSalahNafl.setText("নফল ও জানাযা");
            binding.chipSalahSteps.setText("রুকন ও তাসবীহ");
        } else {
            binding.tvSalahSubheaderTitle.setText("Complete Prayer Guide & Learning");
            binding.tvSalahSubheaderSubtitle.setText("In the Light of Sahih Hadith & Sunnah");
            binding.tvStepsSectionHeader.setText("Step-by-Step Procedure, Masnoon Recitation & Supplications:");
            binding.etSalahGuideSearch.setHint("Search by prayer, dua, or recitation...");
            binding.chipSalahAll.setText("All Topics");
            binding.chipSalahPurity.setText("Purity & Conditions");
            binding.chipSalahFardh.setText("5 Fardh Prayers");
            binding.chipSalahSunnah.setText("Sunnah & Witr");
            binding.chipSalahNafl.setText("Nafl & Janazah");
            binding.chipSalahSteps.setText("Salah Steps");
        }

        if (activity instanceof ViewModelStoreOwner) {
            salahVm.getSelectedTopic().observe((androidx.lifecycle.LifecycleOwner) activity, topic -> {
                if (topic == null) return;
                binding.tvTopicOverviewTitle.setText(topic.getTitle());
                binding.tvTopicRulingBadge.setText(topic.getRulingType());
                binding.tvTopicRakahBadge.setText(topic.getRakahBreakdown());
                binding.tvTopicTimingBadge.setText(topic.getTimingInfo());
                binding.tvTopicMainDesc.setText(topic.getMainDescription());
                adapter.setItems(topic.getSteps());

                for (int i = 0; i < binding.layoutTopicTabsContainer.getChildCount(); i++) {
                    android.view.View v = binding.layoutTopicTabsContainer.getChildAt(i);
                    if (v instanceof TextView) {
                        TextView tab = (TextView) v;
                        Object tag = tab.getTag();
                        boolean isSelected = tag != null && tag.equals(topic.getId());
                        tab.setBackgroundResource(isSelected ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
                        tab.setTextColor(ContextCompat.getColor(activity, isSelected ? R.color.bg_main : R.color.text_primary));
                        tab.setTypeface(null, isSelected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
                    }
                }
            });

            salahVm.getFilteredTopics().observe((androidx.lifecycle.LifecycleOwner) activity, list -> {
                binding.layoutTopicTabsContainer.removeAllViews();
                binding.tvSalahTopicCountBadge.setText(isBn ? "" + BengaliNumberUtil.toBengali(list.size()) + "টি বিষয়" : list.size() + " Topics");

                SalahTopicItem current = salahVm.getSelectedTopic().getValue();

                for (SalahTopicItem item : list) {
                    TextView tab = new TextView(activity);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, dpToPx(activity, 8), 0);
                    tab.setLayoutParams(params);
                    tab.setPadding(dpToPx(activity, 14), dpToPx(activity, 8), dpToPx(activity, 14), dpToPx(activity, 8));
                    tab.setTextSize(12f);
                    tab.setText(item.getTitle());
                    tab.setTag(item.getId());

                    boolean isSelected = current != null && current.getId().equals(item.getId());
                    tab.setBackgroundResource(isSelected ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
                    tab.setTextColor(ContextCompat.getColor(activity, isSelected ? R.color.bg_main : R.color.text_primary));
                    tab.setTypeface(null, isSelected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

                    tab.setOnClickListener(v -> salahVm.selectTopic(item));
                    TouchAnimationUtil.attachTouchSpring(tab);
                    binding.layoutTopicTabsContainer.addView(tab);
                }
            });
        }

        binding.chipSalahAll.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("all");
        });
        binding.chipSalahPurity.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("purity");
        });
        binding.chipSalahFardh.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("fardh");
        });
        binding.chipSalahSunnah.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("sunnah_wajib");
        });
        binding.chipSalahNafl.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("nafl_janazah");
        });
        binding.chipSalahSteps.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) salahVm.setCategoryFilter("steps");
        });

        binding.etSalahGuideSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                salahVm.setSearchQuery(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        dialog.show();
    }

    private static int dpToPx(Activity activity, int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
