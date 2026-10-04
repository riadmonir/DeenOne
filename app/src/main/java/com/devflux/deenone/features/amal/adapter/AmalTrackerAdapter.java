package com.devflux.deenone.features.amal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemAmalTrackerCardBinding;
import com.devflux.deenone.features.amal.model.AmalGoalItem;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.List;

public class AmalTrackerAdapter extends RecyclerView.Adapter<AmalTrackerAdapter.AmalViewHolder> {

  public interface OnAmalActionListener {
    void onAmalComplete(AmalGoalItem item, int position);
  }

  private final List<AmalGoalItem> items;
  private final OnAmalActionListener listener;

  public AmalTrackerAdapter(List<AmalGoalItem> items, OnAmalActionListener listener) {
    this.items = items;
    this.listener = listener;
  }

  @NonNull
  @Override
  public AmalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemAmalTrackerCardBinding binding = ItemAmalTrackerCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new AmalViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull AmalViewHolder holder, int position) {
    AmalGoalItem item = items.get(position);
    holder.bind(item, position, listener);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public class AmalViewHolder extends RecyclerView.ViewHolder {
    private final ItemAmalTrackerCardBinding binding;

    public AmalViewHolder(ItemAmalTrackerCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(AmalGoalItem item, int position, OnAmalActionListener listener) {
      Context context = binding.getRoot().getContext();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      binding.tvAmalTitle.setText(item.getTitle());
      binding.ivAmalIcon.setImageResource(item.getIconResId());
      binding.tvTargetText.setText(item.getTarget());
      binding.tvPointsText.setText(isBn ? ("পয়েন্ট +" + BengaliNumberUtil.toBengali(item.getPoints())) : ("Points +" + item.getPoints()));
      binding.tvCommunityCount.setText(isBn ? ("আজ সম্পন্ন করেছে: " + BengaliNumberUtil.toBengali(item.getCompletedCount()) + "জন") : ("Completed today: " + item.getCompletedCount() + " users"));
      binding.tvAmalDescription.setText(item.getDescription());
      binding.tvDetailsLabel.setText(isBn ? "বিস্তারিত" : "Details");

      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnToggleDetails);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCompleteAmal);

      // Expanded details visibility
      binding.layoutAmalDetails.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);
      binding.tvDetailsChevron.setText(item.isExpanded() ? "∧" : "∨");

      binding.btnToggleDetails.setOnClickListener(v -> {
        item.setExpanded(!item.isExpanded());
        binding.layoutAmalDetails.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);
        binding.tvDetailsChevron.setText(item.isExpanded() ? "∧" : "∨");
      });

      // Completed vs Incomplete Button state
      if (item.isCompleted()) {
        binding.btnCompleteAmal.setText(isBn
            ? ("আলহামদুলিল্লাহ সম্পন্ন হয়েছে • +" + BengaliNumberUtil.toBengali(item.getPoints()) + " পয়েন্ট")
            : ("Alhamdulillah Completed • +" + item.getPoints() + " Points"));
        binding.btnCompleteAmal.setEnabled(false);
        binding.btnCompleteAmal.setAlpha(0.85f);
      } else {
        binding.btnCompleteAmal.setText(isBn ? "আমি সম্পন্ন করেছি" : "I Have Completed");
        binding.btnCompleteAmal.setEnabled(true);
        binding.btnCompleteAmal.setAlpha(1.0f);
      }

      binding.btnCompleteAmal.setOnClickListener(v -> {
        if (!item.isCompleted() && listener != null) {
          item.setCompleted(true);
          item.setCompletedCount(item.getCompletedCount() + 1);
          notifyItemChanged(position);
          listener.onAmalComplete(item, position);
        }
      });
    }
  }
}
