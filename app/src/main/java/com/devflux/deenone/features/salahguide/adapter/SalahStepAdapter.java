package com.devflux.deenone.features.salahguide.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemSalahStepCardBinding;
import com.devflux.deenone.features.salahguide.model.SalahStepItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class SalahStepAdapter extends RecyclerView.Adapter<SalahStepAdapter.StepViewHolder> {

  private final List<SalahStepItem> items = new ArrayList<>();

  public void setItems(List<SalahStepItem> newItems) {
    items.clear();
    if (newItems != null) {
      items.addAll(newItems);
    }
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemSalahStepCardBinding binding = ItemSalahStepCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new StepViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
    holder.bind(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class StepViewHolder extends RecyclerView.ViewHolder {
    private final ItemSalahStepCardBinding binding;

    StepViewHolder(ItemSalahStepCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(SalahStepItem step) {
      Context context = binding.getRoot().getContext();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      binding.tvStepNumberBadge.setText(isBn ? BengaliNumberUtil.toBengali(step.getStepNumber()) : String.valueOf(step.getStepNumber()));
      binding.tvStepTitle.setText(step.getStepTitle());
      binding.tvStepInstruction.setText(step.getInstruction());

      if (step.hasArabic()) {
        binding.layoutArabicRecitation.setVisibility(View.VISIBLE);
        binding.tvStepArabic.setText(step.getArabicText());
        binding.tvStepTransliteration.setText((isBn ? "উচ্চারণ: " : "Pronunciation: ") + step.getTransliteration());
        binding.tvStepTranslation.setText((isBn ? "অর্থ: " : "Meaning: ") + step.getTranslation());
      } else {
        binding.layoutArabicRecitation.setVisibility(View.GONE);
      }

      if (step.getReference() != null && !step.getReference().trim().isEmpty()) {
        binding.layoutStepReference.setVisibility(View.VISIBLE);
        binding.tvStepReference.setText(step.getReference());
      } else {
        binding.layoutStepReference.setVisibility(View.GONE);
      }

      // Copy Step Text
      binding.btnCopyStep.setOnClickListener(v -> {
        StringBuilder sb = new StringBuilder();
        sb.append("").append(step.getStepTitle()).append("\n\n");
        sb.append(step.getInstruction()).append("\n\n");
        if (step.hasArabic()) {
          sb.append(step.getArabicText()).append("\n\n");
          sb.append(isBn ? "উচ্চারণ: " : "Pronunciation: ").append(step.getTransliteration()).append("\n");
          sb.append(isBn ? "অর্থ: " : "Meaning: ").append(step.getTranslation()).append("\n\n");
        }
        if (step.getReference() != null && !step.getReference().isEmpty()) {
          sb.append(isBn ? "রেফারেন্স: " : "Reference: ").append(step.getReference());
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(isBn ? "নামাজ শিক্ষা" : "Salah Guide", sb.toString().trim());
        if (cm != null) {
          cm.setPrimaryClip(clip);
          Toast.makeText(context, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
      });

      // Share Step Text
      binding.btnShareStep.setOnClickListener(v -> {
        StringBuilder sb = new StringBuilder();
        sb.append("").append(step.getStepTitle()).append("\n\n");
        sb.append(step.getInstruction()).append("\n\n");
        if (step.hasArabic()) {
          sb.append(step.getArabicText()).append("\n\n");
          sb.append(isBn ? "উচ্চারণ: " : "Pronunciation: ").append(step.getTransliteration()).append("\n");
          sb.append(isBn ? "অর্থ: " : "Meaning: ").append(step.getTranslation()).append("\n\n");
        }
        if (step.getReference() != null && !step.getReference().isEmpty()) {
          sb.append(isBn ? "রেফারেন্স: " : "Reference: ").append(step.getReference()).append("\n\n");
        }
        sb.append(isBn ? "— দ্বীনওয়ান" : "— DeenOne");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        context.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
      });

      TouchAnimationUtil.attachTouchSpring(binding.btnCopyStep);
      TouchAnimationUtil.attachTouchSpring(binding.btnShareStep);
    }
  }
}
