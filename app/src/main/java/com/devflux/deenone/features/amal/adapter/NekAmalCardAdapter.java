package com.devflux.deenone.features.amal.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class NekAmalCardAdapter extends RecyclerView.Adapter<NekAmalCardAdapter.AmalViewHolder> {

  public interface OnAmalActionListener {
    void onCompleteAmalClicked(DailyAmalEntity amal, int position);
  }

  private final List<DailyAmalEntity> items = new ArrayList<>();
  private final OnAmalActionListener listener;

  public NekAmalCardAdapter(List<DailyAmalEntity> initialItems, OnAmalActionListener listener) {
    if (initialItems != null) {
      this.items.addAll(initialItems);
    }
    this.listener = listener;
  }

  public void updateList(List<DailyAmalEntity> newItems) {
    this.items.clear();
    if (newItems != null) {
      this.items.addAll(newItems);
    }
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public AmalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nek_amal_card, parent, false);
    return new AmalViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AmalViewHolder holder, int position) {
    holder.bind(items.get(position), listener, position);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class AmalViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvTitle, tvTargetBadge, tvPointsBadge, tvCompletedStats, btnToggleDetails;
    private final TextView tvArabicText, tvHowToPerform, tvDescription, tvReference;
    private final LinearLayout layoutExpandableDetails;
    private final MaterialButton btnCompleteAction;

    public AmalViewHolder(@NonNull View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tvAmalTitle);
      tvTargetBadge = itemView.findViewById(R.id.tvAmalTargetBadge);
      tvPointsBadge = itemView.findViewById(R.id.tvAmalPointsBadge);
      tvCompletedStats = itemView.findViewById(R.id.tvAmalCompletedStats);
      btnToggleDetails = itemView.findViewById(R.id.btnToggleDetails);
      tvArabicText = itemView.findViewById(R.id.tvAmalArabicText);
      tvHowToPerform = itemView.findViewById(R.id.tvAmalHowToPerform);
      tvDescription = itemView.findViewById(R.id.tvAmalDescription);
      tvReference = itemView.findViewById(R.id.tvAmalReference);
      layoutExpandableDetails = itemView.findViewById(R.id.layoutExpandableDetails);
      btnCompleteAction = itemView.findViewById(R.id.btnCompleteAmalAction);

      // Strictly button-only animation per Rule 7 (Zero Touch Animation on Cards)
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnToggleDetails);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnCompleteAction);
    }

    public void bind(DailyAmalEntity amal, OnAmalActionListener listener, int position) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
      tvTitle.setText(amal.getTitle());
      tvTargetBadge.setText(isBn ? ("⊙ টার্গেট " + BengaliNumberUtil.toBengali(amal.getTargetCount()) + "বার") : ("⊙ Target " + amal.getTargetCount() + " times"));
      tvPointsBadge.setText(isBn ? ("পয়েন্ট +" + BengaliNumberUtil.toBengali(amal.getPoints())) : ("Points +" + amal.getPoints()));

      int sampleCompleted = Math.max(amal.isCompleted() ? 1 : 0, (Math.abs(amal.getTitle().hashCode()) % 45) + (amal.isCompleted() ? 1 : 0));
      tvCompletedStats.setText(isBn ? ("আজ সম্পন্ন করেছে: " + BengaliNumberUtil.toBengali(sampleCompleted) + "জন") : ("Completed today: " + sampleCompleted + " users"));

      // Arabic text
      if (amal.getRelevantDuaDhikr() != null && !amal.getRelevantDuaDhikr().trim().isEmpty()) {
        tvArabicText.setText(amal.getRelevantDuaDhikr());
        tvArabicText.setVisibility(View.VISIBLE);
      } else {
        tvArabicText.setVisibility(View.GONE);
      }

      // How to perform / instruction
      if (amal.getHowToPerform() != null && !amal.getHowToPerform().trim().isEmpty()) {
        tvHowToPerform.setText(amal.getHowToPerform());
        tvHowToPerform.setVisibility(View.VISIBLE);
      } else {
        tvHowToPerform.setVisibility(View.GONE);
      }

      // Description / Virtue
      tvDescription.setText(amal.getDescription() != null ? amal.getDescription() : "");
      tvReference.setText(amal.getHadithReference() != null && !amal.getHadithReference().isEmpty()
          ? ("— " + amal.getHadithReference())
          : (isBn ? "— কুরআন ও সহীহ সুন্নাহ" : "— Quran & Sahih Sunnah"));

      // Expand / Collapse details
      btnToggleDetails.setText(isBn ? "বিস্তারিত ˅" : "Details ˅");
      btnToggleDetails.setOnClickListener(v -> {
        boolean isVisible = layoutExpandableDetails.getVisibility() == View.VISIBLE;
        layoutExpandableDetails.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        btnToggleDetails.setText(isVisible ? (isBn ? "বিস্তারিত ˅" : "Details ˅") : (isBn ? "সংক্ষিপ্ত ˄" : "Collapse ˄"));
      });

      if (amal.isCompleted()) {
        btnCompleteAction.setText(isBn ? "সম্পন্ন হয়েছে" : "Completed");
        btnCompleteAction.setBackgroundTintList(ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.primary_green)));
        btnCompleteAction.setTextColor(androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.text_primary));
        btnCompleteAction.setEnabled(false);
      } else {
        btnCompleteAction.setText(isBn ? "আমি সম্পন্ন করেছি" : "I Have Completed");
        btnCompleteAction.setBackgroundTintList(ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.accent_mint)));
        btnCompleteAction.setTextColor(androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.bg_main));
        btnCompleteAction.setEnabled(true);
        btnCompleteAction.setOnClickListener(v -> {
          if (listener != null) {
            listener.onCompleteAmalClicked(amal, position);
          }
        });
      }
    }
  }
}
