package com.devflux.deenone.features.amal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.databinding.ItemDailyAmalCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.List;

public class DailyAmalAdapter extends RecyclerView.Adapter<DailyAmalAdapter.AmalViewHolder> {

  public interface OnAmalCompleteListener {
    void onToggleComplete(DailyAmalEntity item, int position);
  }

  private List<DailyAmalEntity> items;
  private final OnAmalCompleteListener completeListener;

  public DailyAmalAdapter(List<DailyAmalEntity> items, OnAmalCompleteListener completeListener) {
    this.items = items;
    this.completeListener = completeListener;
  }

  public void updateData(List<DailyAmalEntity> newItems) {
    this.items = newItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public AmalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemDailyAmalCardBinding binding = ItemDailyAmalCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new AmalViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull AmalViewHolder holder, int position) {
    DailyAmalEntity item = items.get(position);
    holder.bind(item, position);
  }

  @Override
  public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  public class AmalViewHolder extends RecyclerView.ViewHolder {
    private final ItemDailyAmalCardBinding binding;

    public AmalViewHolder(ItemDailyAmalCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(DailyAmalEntity item, int position) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(binding.getRoot().getContext());
      binding.tvAmalTitle.setText(item.getTitle());
      binding.tvAmalDescription.setText(item.getDescription());
      binding.tvAmalHowToPerform.setText(item.getHowToPerform());
      binding.tvAmalRelevantDua.setText(item.getRelevantDuaDhikr());
      binding.tvAmalReference.setText(item.getHadithReference() != null && !item.getHadithReference().isEmpty()
          ? (isBn ? ("সূত্র: " + item.getHadithReference()) : ("Ref: " + item.getHadithReference()))
          : "");

      binding.tvAmalHowToPerformLabel.setText(isBn ? "কীভাবে আমল করবেন:" : "How to perform:");
      binding.tvAmalRelevantDuaLabel.setText(isBn ? "প্রাসঙ্গিক দোয়া ও জিকির:" : "Relevant Dua and Dhikr:");

      binding.tvAmalXpBadge.setText("+" + (isBn ? BengaliNumberUtil.toBengali(item.getPoints()) : String.valueOf(item.getPoints())) + " XP");
      if (item.getTargetCount() > 1) {
        binding.tvAmalTargetCount.setText(isBn ? ("লক্ষ্য: " + BengaliNumberUtil.toBengali(item.getTargetCount()) + "বার") : ("Target: " + item.getTargetCount() + " times"));
        binding.tvAmalTargetCount.setVisibility(View.VISIBLE);
      } else {
        binding.tvAmalTargetCount.setVisibility(View.GONE);
      }

      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnToggleComplete);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnExpandDetails);

      // Completion state
      if (item.isCompleted()) {
        binding.btnToggleComplete.setText(isBn
            ? ("সম্পন্ন হয়েছে • +" + BengaliNumberUtil.toBengali(item.getPoints()) + " XP")
            : ("Completed • +" + item.getPoints() + " XP"));
        binding.btnToggleComplete.setBackgroundColor(androidx.core.content.ContextCompat.getColor(binding.getRoot().getContext(), R.color.bg_card_active));
        binding.btnToggleComplete.setTextColor(androidx.core.content.ContextCompat.getColor(binding.getRoot().getContext(), R.color.accent_mint));
        binding.btnToggleComplete.setEnabled(false);
        binding.btnToggleComplete.setOnClickListener(null);
      } else {
        binding.btnToggleComplete.setText(isBn ? "সম্পন্ন করুন" : "Complete Deed");
        binding.btnToggleComplete.setBackgroundColor(androidx.core.content.ContextCompat.getColor(binding.getRoot().getContext(), R.color.primary_green));
        binding.btnToggleComplete.setTextColor(androidx.core.content.ContextCompat.getColor(binding.getRoot().getContext(), R.color.bg_main));
        binding.btnToggleComplete.setOnClickListener(v -> {
          if (completeListener != null) {
            completeListener.onToggleComplete(item, position);
          }
        });
      }

      // Expand / Collapse
      binding.layoutExpandedDetails.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);
      binding.tvExpandArrow.setText(item.isExpanded() ? "▲" : "▼");
      binding.tvExpandLabel.setText(item.isExpanded()
          ? (isBn ? "বিস্তারিত গোপন করুন" : "Hide Details")
          : (isBn ? "কীভাবে আমল করবেন ও দলীল দেখুন" : "How to perform and reference"));

      binding.btnExpandDetails.setOnClickListener(v -> {
        item.setExpanded(!item.isExpanded());
        notifyItemChanged(position);
      });
    }
  }
}
