package com.devflux.deenone.features.ramadan.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.fasting.FastingTrackerManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.ItemRamadanDayCircleBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class RamadanDaysGridAdapter extends RecyclerView.Adapter<RamadanDaysGridAdapter.DayViewHolder> {

    public interface OnDayStatusChangedListener {
        void onDayStatusChanged(int dayNumber, boolean completed, int totalCompleted);
    }

    private final Context context;
    private final boolean isBn;
    private final int currentRamadanDay;
    private final OnDayStatusChangedListener listener;

    public RamadanDaysGridAdapter(Context context, int currentRamadanDay, OnDayStatusChangedListener listener) {
        this.context = context;
        this.isBn = LocaleManager.isBengali(context);
        this.currentRamadanDay = currentRamadanDay;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRamadanDayCircleBinding binding = ItemRamadanDayCircleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new DayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        int dayNumber = position + 1;
        holder.bind(dayNumber);
    }

    @Override
    public int getItemCount() {
        return 30; // 30 Days of Ramadan
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        private final ItemRamadanDayCircleBinding binding;

        public DayViewHolder(ItemRamadanDayCircleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int dayNumber) {
            boolean isCompleted = FastingTrackerManager.getInstance().isRamadanDayCompleted(context, dayNumber);
            boolean isToday = (dayNumber == currentRamadanDay);

            String dayText = isBn ? BengaliNumberUtil.toBengali(dayNumber) : String.valueOf(dayNumber);
            binding.tvDayNumber.setText(dayText);

            if (isCompleted) {
                binding.layoutDayCircle.setBackgroundResource(R.drawable.bg_card_active);
                binding.layoutDayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0E3827")));
                binding.tvDayNumber.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
                binding.ivCheckIndicator.setVisibility(View.VISIBLE);
                binding.ivCheckIndicator.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
            } else if (isToday) {
                binding.layoutDayCircle.setBackgroundResource(R.drawable.bg_card_active);
                binding.layoutDayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#38260E")));
                binding.tvDayNumber.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
                binding.ivCheckIndicator.setVisibility(View.GONE);
            } else {
                binding.layoutDayCircle.setBackgroundResource(R.drawable.bg_circular_counter);
                binding.layoutDayCircle.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_badge_pill)));
                binding.tvDayNumber.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
                binding.ivCheckIndicator.setVisibility(View.GONE);
            }

            binding.layoutDayCircle.setOnClickListener(v -> {
                boolean newState = !FastingTrackerManager.getInstance().isRamadanDayCompleted(context, dayNumber);
                FastingTrackerManager.getInstance().setRamadanDayCompleted(context, dayNumber, newState);
                notifyItemChanged(getAdapterPosition());
                if (listener != null) {
                    int total = FastingTrackerManager.getInstance().getCompletedRamadanDaysCount(context);
                    listener.onDayStatusChanged(dayNumber, newState, total);
                }
            });
        }
    }
}
