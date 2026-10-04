package com.devflux.deenone.core.halal;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.ItemHalalFoodCardBinding;
import com.devflux.deenone.utils.AsyncImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HalalFoodAdapter extends RecyclerView.Adapter<HalalFoodAdapter.ViewHolder> {

  public interface OnHalalItemClickListener {
    void onItemClick(HalalFoodItem item);
  }

  private final List<HalalFoodItem> items = new ArrayList<>();
  private final OnHalalItemClickListener listener;

  public HalalFoodAdapter(OnHalalItemClickListener listener) {
    this.listener = listener;
  }

  public void updateData(List<HalalFoodItem> newItems) {
    this.items.clear();
    if (newItems != null) {
      this.items.addAll(newItems);
    }
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemHalalFoodCardBinding binding = ItemHalalFoodCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    HalalFoodItem item = items.get(position);
    holder.bind(item, listener);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final ItemHalalFoodCardBinding binding;

    ViewHolder(ItemHalalFoodCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(HalalFoodItem item, OnHalalItemClickListener listener) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(binding.getRoot().getContext());

      binding.tvHalalTitle.setText(item.getTitle(isBn));
      
      if (item.getArabicName() != null && !item.getArabicName().isEmpty()) {
        binding.tvHalalArabicName.setVisibility(View.VISIBLE);
        binding.tvHalalArabicName.setText(item.getArabicName());
      } else {
        binding.tvHalalArabicName.setVisibility(View.GONE);
      }

      String source = item.getSourceOrigin(isBn);
      binding.tvHalalScientificName.setText((isBn ? "উৎস: " : "Source: ") + (source != null ? source : ""));
      binding.tvHalalDescription.setText(item.getDescription(isBn));
      binding.tvHalalReference.setText(item.getReference(isBn));
      binding.tvHalalCardAction.setText(isBn ? "বিস্তারিত " : "Details ");

      // Load Image Thumbnail asynchronously with memory & disk cache
      AsyncImageLoader.getInstance(binding.getRoot().getContext()).loadImage(
          binding.ivHalalFoodThumb,
          item.getImageUrl(),
          R.drawable.bg_card_secondary
      );

      // Status Styling
      String status = item.getStatus();
      if ("HARAM".equalsIgnoreCase(status)) {
        binding.tvHalalStatusBadge.setText(isBn ? "হারাম" : "HARAM");
        binding.tvHalalStatusBadge.setTextColor(Color.parseColor("#EF4444"));
        binding.tvHalalStatusBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E0808")));
      } else if ("MUSHBOOH".equalsIgnoreCase(status)) {
        binding.tvHalalStatusBadge.setText(isBn ? "সন্দেহজনক" : "DOUBTFUL");
        binding.tvHalalStatusBadge.setTextColor(Color.parseColor("#F59E0B"));
        binding.tvHalalStatusBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E1C05")));
      } else {
        binding.tvHalalStatusBadge.setText(isBn ? "হালাল" : "HALAL");
        binding.tvHalalStatusBadge.setTextColor(Color.parseColor("#10B981"));
        binding.tvHalalStatusBadge.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#052E22")));
      }

      // Category Pill
      String cat = item.getCategory();
      if ("SUNNAH_FOOD".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText(isBn ? "সুন্নাহ খাদ্য" : "Sunnah Food");
      } else if ("HARAM_FOOD".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText(isBn ? "বর্জনীয়" : "Forbidden");
      } else if ("E_CODE".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText("E-Code");
      } else if ("ANIMAL_BIRDS".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText(isBn ? "পশু-পাখি" : "Animals & Birds");
      } else if ("AQUATIC".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText(isBn ? "সামুদ্রিক খাদ্য" : "Aquatic Food");
      } else if ("ETIQUETTE".equalsIgnoreCase(cat)) {
        binding.tvHalalCategoryPill.setText(isBn ? "খাওয়ার আদব" : "Eating Etiquette");
      } else {
        binding.tvHalalCategoryPill.setText(isBn ? "মূল নীতিমালা" : "General Rules");
      }

      binding.getRoot().setOnClickListener(v -> {
        if (listener != null) {
          listener.onItemClick(item);
        }
      });
    }
  }
}