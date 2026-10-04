package com.devflux.deenone.features.ramadan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.ramadan.model.RozaScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class RozaScheduleAdapter extends RecyclerView.Adapter<RozaScheduleAdapter.ViewHolder> {

    private final List<RozaScheduleItem> items = new ArrayList<>();

    public void setItems(List<RozaScheduleItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roza_schedule_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RozaScheduleItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layoutRozaRow;
        private final TextView tvRowHijriDate;
        private final TextView tvRowGregorianDate;
        private final TextView tvRowSehriTime;
        private final TextView tvRowIftarTime;
        private final TextView tvTodayBadge;
        private final View rowDivider;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutRozaRow = itemView.findViewById(R.id.layoutRozaRow);
            tvRowHijriDate = itemView.findViewById(R.id.tvRowHijriDate);
            tvRowGregorianDate = itemView.findViewById(R.id.tvRowGregorianDate);
            tvRowSehriTime = itemView.findViewById(R.id.tvRowSehriTime);
            tvRowIftarTime = itemView.findViewById(R.id.tvRowIftarTime);
            tvTodayBadge = itemView.findViewById(R.id.tvTodayBadge);
            rowDivider = itemView.findViewById(R.id.rowDivider);
        }

        void bind(RozaScheduleItem item) {
            Context context = itemView.getContext();
            tvRowHijriDate.setText(item.getHijriDayAndMonth());
            tvRowGregorianDate.setText(item.getGregorianDayAndDate());
            tvRowSehriTime.setText(item.getSehriTime());
            tvRowIftarTime.setText(item.getIftarTime());

            if (item.isToday()) {
                int activeBgColor = ContextCompat.getColor(context, R.color.bg_card_active);
                int activeBorderColor = ContextCompat.getColor(context, R.color.border_active);
                float density = context.getResources().getDisplayMetrics().density;

                GradientDrawable activeBg = new GradientDrawable();
                activeBg.setColor(activeBgColor);
                activeBg.setStroke((int) (1.2f * density), activeBorderColor);
                activeBg.setCornerRadius(14f * density);
                layoutRozaRow.setBackground(activeBg);

                if (tvTodayBadge != null) {
                    tvTodayBadge.setVisibility(View.VISIBLE);
                }
                tvRowHijriDate.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
                tvRowSehriTime.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
                tvRowIftarTime.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
            } else {
                layoutRozaRow.setBackgroundColor(Color.TRANSPARENT);
                if (tvTodayBadge != null) {
                    tvTodayBadge.setVisibility(View.GONE);
                }
                tvRowHijriDate.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                tvRowSehriTime.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                tvRowIftarTime.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            }
        }
    }
}
