package com.devflux.deenone.features.prayer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.ItemMonthlySalatRowBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

public class MonthlySalatReportAdapter extends RecyclerView.Adapter<MonthlySalatReportAdapter.ViewHolder> {

    public enum SalatStatus {
        NONE,
        PRAYED,
        QAZA,
        MISSED
    }

    public static class DaySalatRecord {
        public final String dateIso;
        public final String dayNumberBn;
        public final String dayNameBn;
        public final SalatStatus fajrStatus;
        public final SalatStatus dhuhrStatus;
        public final SalatStatus asrStatus;
        public final SalatStatus maghribStatus;
        public final SalatStatus ishaStatus;
        public final int percentage;
        public final boolean isFutureDate;

        public DaySalatRecord(String dateIso, String dayNumberBn, String dayNameBn,
                              SalatStatus fajrStatus, SalatStatus dhuhrStatus, SalatStatus asrStatus,
                              SalatStatus maghribStatus, SalatStatus ishaStatus,
                              int percentage, boolean isFutureDate) {
            this.dateIso = dateIso;
            this.dayNumberBn = dayNumberBn;
            this.dayNameBn = dayNameBn;
            this.fajrStatus = fajrStatus;
            this.dhuhrStatus = dhuhrStatus;
            this.asrStatus = asrStatus;
            this.maghribStatus = maghribStatus;
            this.ishaStatus = ishaStatus;
            this.percentage = percentage;
            this.isFutureDate = isFutureDate;
        }
    }

    private final List<DaySalatRecord> records = new ArrayList<>();

    public void setRecords(List<DaySalatRecord> newRecords) {
        records.clear();
        if (newRecords != null) {
            records.addAll(newRecords);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMonthlySalatRowBinding binding = ItemMonthlySalatRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DaySalatRecord item = records.get(position);
        holder.binding.tvDateNum.setText(item.dayNumberBn);
        holder.binding.tvDayName.setText(item.dayNameBn);

        // 1. Fajr
        holder.binding.ivRowFajr.setImageResource(getIconForStatus(item.fajrStatus));

        // 2. Dhuhr
        holder.binding.ivRowDhuhr.setImageResource(getIconForStatus(item.dhuhrStatus));

        // 3. Asr
        holder.binding.ivRowAsr.setImageResource(getIconForStatus(item.asrStatus));

        // 4. Maghrib
        holder.binding.ivRowMaghrib.setImageResource(getIconForStatus(item.maghribStatus));

        // 5. Isha
        holder.binding.ivRowIsha.setImageResource(getIconForStatus(item.ishaStatus));

        // 6. Daily Rate / Percentage (e.g. ১০০%, ৬০%, ৪০%, ২০%, ০%)
        // 6. Daily Rate / Percentage (e.g. ১০০%, ৬০%, ৪০%, ২০%, ০% in bn, 100%, 60% in en)
        android.content.Context ctx = holder.itemView.getContext();
        if (item.isFutureDate) {
            holder.binding.tvRowRate.setText("-");
            holder.binding.tvRowRate.setTextColor(
                    ContextCompat.getColor(ctx, R.color.text_secondary)
            );
        } else {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(ctx);
            holder.binding.tvRowRate.setText(isBn ? (BengaliNumberUtil.toBengali(item.percentage) + "%") : (item.percentage + "%"));
            if (item.percentage >= 80) {
                holder.binding.tvRowRate.setTextColor(ContextCompat.getColor(ctx, R.color.accent_mint));
            } else if (item.percentage >= 60) {
                holder.binding.tvRowRate.setTextColor(ContextCompat.getColor(ctx, R.color.accent_gold));
            } else if (item.percentage > 0) {
                holder.binding.tvRowRate.setTextColor(ContextCompat.getColor(ctx, R.color.accent_red));
            } else {
                holder.binding.tvRowRate.setTextColor(
                        ContextCompat.getColor(ctx, R.color.text_secondary)
                );
            }
        }
    }

    private int getIconForStatus(SalatStatus status) {
        if (status == null) return R.drawable.ic_salat_status_empty;
        switch (status) {
            case PRAYED:
                return R.drawable.ic_salat_status_prayed;
            case QAZA:
                return R.drawable.ic_salat_status_qaza;
            case MISSED:
                return R.drawable.ic_salat_status_missed;
            case NONE:
            default:
                return R.drawable.ic_salat_status_empty;
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMonthlySalatRowBinding binding;
        ViewHolder(ItemMonthlySalatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
