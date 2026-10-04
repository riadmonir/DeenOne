package com.devflux.deenone.features.prayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.prayer.PrayerSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemPrayerCalculationMethodBinding;
import com.devflux.deenone.databinding.PagePrayerTimeCalculationBinding;

import java.util.ArrayList;
import java.util.List;

public class PrayerTimeCalculationPageDialog {

    public interface OnCalculationMethodSavedListener {
        void onMethodSaved(boolean isAuto, PrayerSettingsManager.CalculationMethod method);
    }

    public static void show(Context context) {
        show(context, null);
    }

    public static void show(Context context, OnCalculationMethodSavedListener listener) {
        if (context == null) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PagePrayerTimeCalculationBinding binding =
                PagePrayerTimeCalculationBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBengali = LocaleManager.isBengali(context);

        // Header & text localization
        if (isBengali) {
            binding.tvPrayerCalcTitle.setText("নামাজের ওয়াক্ত গণনা");
            binding.tvCalcMethodHeading.setText("নামাজের সময় গণনা পদ্ধতি");
            binding.tvCalcMethodDesc1.setText("বিশ্বজুড়ে বিভিন্ন ইসলামিক সংস্থা নামাজের সময় নির্ধারণের জন্য বিভিন্ন সৌর কোণ ব্যবহার করে। ভৌগোলিক অবস্থানের কারণে সূর্যের অবস্থান পরিবর্তনের সাথে সাথে এই কোণগুলি পরিবর্তিত হয়।");
            binding.tvCalcMethodDesc2.setText("সর্বাধিক নির্ভুল নামাজের সময়ের জন্য আপনার অঞ্চলের সাথে সবচেয়ে উপযুক্ত গণনা পদ্ধতিটি নির্বাচন করুন।");
            binding.btnCancelPrayerCalc.setText("বাতিল");
            binding.btnSavePrayerCalc.setText("সংরক্ষণ করুন");
        } else {
            binding.tvPrayerCalcTitle.setText("Prayer time calculation");
            binding.tvCalcMethodHeading.setText("Prayer Time Calculation Method");
            binding.tvCalcMethodDesc1.setText("Different Islamic organizations around the world use different sun angle calculations to determine prayer times. These angles vary by geographic region because the sun's position changes based on your location.");
            binding.tvCalcMethodDesc2.setText("Select the calculation method that best matches your region for the most accurate prayer times.");
            binding.btnCancelPrayerCalc.setText("Cancel");
            binding.btnSavePrayerCalc.setText("Save");
        }

        binding.btnBackPrayerCalc.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelPrayerCalc.setOnClickListener(v -> dialog.dismiss());

        // Resolve current settings
        boolean initialIsAuto = PrayerSettingsManager.isAutoMethod(context);
        PrayerSettingsManager.CalculationMethod initialMethod = PrayerSettingsManager.getRawSavedCalculationMethod(context);
        PrayerSettingsManager.AutoRecommendation rec = PrayerSettingsManager.getRecommendedMethodForCurrentLocation(context);

        // State holder for selection
        final boolean[] selectedIsAuto = {initialIsAuto};
        final PrayerSettingsManager.CalculationMethod[] selectedMethod = {initialMethod};

        // Prepare list items
        List<MethodRowItem> items = new ArrayList<>();

        // 1. Automatic Item (First)
        String autoCountry = isBengali ? rec.countryNameBn : rec.countryName;
        String autoMethodName = isBengali ? rec.recommendedMethod.displayNameBn : rec.recommendedMethod.displayName;
        String autoSubtitle = isBengali
                ? "নির্বাচিত দেশ: " + autoCountry + ", প্রস্তাবিত পদ্ধতি: " + autoMethodName
                : "Selected country: " + autoCountry + ", Recommended method: " + autoMethodName;

        items.add(new MethodRowItem(
                true,
                null,
                isBengali ? "স্বয়ংক্রিয় - প্রস্তাবিত" : "Automatic - Recommended",
                autoSubtitle
        ));

        // 2. All Defined Methods
        PrayerSettingsManager.CalculationMethod[] methods = PrayerSettingsManager.CalculationMethod.values();
        for (PrayerSettingsManager.CalculationMethod m : methods) {
            items.add(new MethodRowItem(
                    false,
                    m,
                    isBengali ? m.displayNameBn : m.displayName,
                    null
            ));
        }

        CalculationMethodAdapter adapter = new CalculationMethodAdapter(items, selectedIsAuto[0], selectedMethod[0], (isAuto, method) -> {
            selectedIsAuto[0] = isAuto;
            if (!isAuto && method != null) {
                selectedMethod[0] = method;
            }
        });

        binding.rvCalculationMethods.setLayoutManager(new LinearLayoutManager(context));
        binding.rvCalculationMethods.setAdapter(adapter);

        // Save Action
        binding.btnSavePrayerCalc.setOnClickListener(v -> {
            if (selectedIsAuto[0]) {
                PrayerSettingsManager.setAutoMethod(context, true);
            } else if (selectedMethod[0] != null) {
                PrayerSettingsManager.setCalculationMethod(context, selectedMethod[0]);
            }

            // Immediately broadcast app-wide changes
            PrayerSettingsManager.notifyPrayerSettingsChanged(context);

            // Notify MainActivity ViewModel to refresh live calculations
            if (context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                if (activity.getViewModel() != null) {
                    activity.getViewModel().updateRealTimeCalculations();
                }
            }

            if (listener != null) {
                listener.onMethodSaved(selectedIsAuto[0], selectedMethod[0]);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private static class MethodRowItem {
        final boolean isAuto;
        final PrayerSettingsManager.CalculationMethod method;
        final String title;
        final String subtitle;

        MethodRowItem(boolean isAuto, PrayerSettingsManager.CalculationMethod method, String title, String subtitle) {
            this.isAuto = isAuto;
            this.method = method;
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    private static class CalculationMethodAdapter extends RecyclerView.Adapter<CalculationMethodAdapter.MethodViewHolder> {

        interface OnItemClickListener {
            void onItemClicked(boolean isAuto, PrayerSettingsManager.CalculationMethod method);
        }

        private final List<MethodRowItem> items;
        private boolean isAutoSelected;
        private PrayerSettingsManager.CalculationMethod selectedMethod;
        private final OnItemClickListener listener;

        CalculationMethodAdapter(List<MethodRowItem> items, boolean isAutoSelected, PrayerSettingsManager.CalculationMethod selectedMethod, OnItemClickListener listener) {
            this.items = items;
            this.isAutoSelected = isAutoSelected;
            this.selectedMethod = selectedMethod;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemPrayerCalculationMethodBinding binding =
                    ItemPrayerCalculationMethodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MethodViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MethodViewHolder holder, int position) {
            MethodRowItem item = items.get(position);
            holder.binding.tvMethodTitle.setText(item.title);

            if (item.subtitle != null && !item.subtitle.trim().isEmpty()) {
                holder.binding.tvMethodSubtitle.setVisibility(View.VISIBLE);
                holder.binding.tvMethodSubtitle.setText(item.subtitle);
            } else {
                holder.binding.tvMethodSubtitle.setVisibility(View.GONE);
            }

            boolean isChecked;
            if (item.isAuto) {
                isChecked = isAutoSelected;
            } else {
                isChecked = !isAutoSelected && (item.method == selectedMethod);
            }

            holder.binding.ivMethodRadio.setImageResource(
                    isChecked ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected
            );

            holder.binding.rootMethodItem.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                MethodRowItem clicked = items.get(pos);

                isAutoSelected = clicked.isAuto;
                if (!clicked.isAuto && clicked.method != null) {
                    selectedMethod = clicked.method;
                }
                notifyDataSetChanged();

                if (listener != null) {
                    listener.onItemClicked(isAutoSelected, selectedMethod);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class MethodViewHolder extends RecyclerView.ViewHolder {
            final ItemPrayerCalculationMethodBinding binding;

            MethodViewHolder(ItemPrayerCalculationMethodBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
