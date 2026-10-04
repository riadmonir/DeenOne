package com.devflux.deenone.features.ramadan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.fasting.FastingTrackerManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.ItemRozaCalendarDayBinding;
import com.devflux.deenone.features.ramadan.model.RozaCalendarDay;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class RozaCalendarAdapter extends RecyclerView.Adapter<RozaCalendarAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClicked(RozaCalendarDay day);
    }

    private final List<RozaCalendarDay> days = new ArrayList<>();
    private final OnDayClickListener listener;

    public RozaCalendarAdapter(OnDayClickListener listener) {
        this.listener = listener;
    }

    public void setDays(List<RozaCalendarDay> newDays) {
        days.clear();
        if (newDays != null) {
            days.addAll(newDays);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaCalendarDayBinding binding = ItemRozaCalendarDayBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new DayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private final ItemRozaCalendarDayBinding binding;

        DayViewHolder(ItemRozaCalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Rule 7: Zero touch animation on list/grid items
        }

        void bind(RozaCalendarDay day) {
            Context context = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(context);
            float density = context.getResources().getDisplayMetrics().density;

            // Day Number formatting
            String dayStr = isBn ? BengaliNumberUtil.toBengali(day.dayNumber) : String.valueOf(day.dayNumber);
            binding.tvDayNumber.setText(dayStr);

            if (!day.isCurrentMonth) {
                // Outside Month: Muted display
                binding.tvDayNumber.setTextColor(ContextCompat.getColor(context, R.color.text_muted));
                binding.layoutDayCircle.setAlpha(0.35f);
                binding.layoutDayCircle.setBackground(null);
                binding.viewFastingDot.setVisibility(View.GONE);
            } else {
                binding.layoutDayCircle.setAlpha(1.0f);

                GradientDrawable circleDrawable = new GradientDrawable();
                circleDrawable.setShape(GradientDrawable.OVAL);

                int textColor;
                int mintColor = ContextCompat.getColor(context, R.color.accent_mint);
                int redColor = ContextCompat.getColor(context, R.color.accent_red);
                int textPrimary = ContextCompat.getColor(context, R.color.text_primary);
                int badgePillBg = ContextCompat.getColor(context, R.color.bg_badge_pill);

                if (day.fastingStatus == FastingTrackerManager.STATUS_FASTED) {
                    // 1. FASTED: Mint circular border with soft mint tint
                    circleDrawable.setColor(Color.parseColor("#2010B981"));
                    circleDrawable.setStroke((int) (2.0f * density), mintColor);
                    textColor = mintColor;

                    if (day.isSelected) {
                        circleDrawable.setColor(mintColor);
                        circleDrawable.setStroke((int) (2.5f * density), Color.parseColor("#F59E0B")); // Gold selection ring
                        textColor = Color.WHITE;
                    }
                } else if (day.fastingStatus == FastingTrackerManager.STATUS_NOT_FASTED) {
                    // 2. NOT FASTED: Red circular border with soft red tint
                    circleDrawable.setColor(Color.parseColor("#20EF4444"));
                    circleDrawable.setStroke((int) (2.0f * density), redColor);
                    textColor = redColor;

                    if (day.isSelected) {
                        circleDrawable.setColor(redColor);
                        circleDrawable.setStroke((int) (2.5f * density), Color.parseColor("#F59E0B")); // Gold selection ring
                        textColor = Color.WHITE;
                    }
                } else {
                    // 3. UNMARKED: Clean transparent or soft badge pill if selected
                    if (day.isSelected) {
                        circleDrawable.setColor(badgePillBg);
                        circleDrawable.setStroke((int) (2.0f * density), Color.parseColor("#F59E0B")); // Gold selection ring
                        textColor = textPrimary;
                    } else if (day.isToday) {
                        circleDrawable.setColor(badgePillBg);
                        circleDrawable.setStroke((int) (1.2f * density), ContextCompat.getColor(context, R.color.border_card));
                        textColor = mintColor;
                    } else {
                        circleDrawable.setColor(Color.TRANSPARENT);
                        textColor = day.isFriday ? redColor : textPrimary;
                    }
                }

                binding.layoutDayCircle.setBackground(circleDrawable);
                binding.tvDayNumber.setTextColor(textColor);
                binding.viewFastingDot.setVisibility(View.GONE);
            }

            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDayClicked(day);
                }
            });
        }
    }
}
