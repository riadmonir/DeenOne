package com.devflux.deenone.features.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.AzkarEntity;
import com.google.android.material.button.MaterialButton;

/**
 * AzkarAdapter — RecyclerView adapter for the Azkar feature.
 *
 * Displays: Arabic text, transliteration, Bengali meaning, reference, benefit, count.
 * Supports: increment count via "পড়লাম" button.
 */
public class AzkarAdapter extends ListAdapter<AzkarEntity, AzkarAdapter.AzkarViewHolder> {

  public interface OnAzkarActionListener {
    void onIncrementCount(AzkarEntity azkar);
  }

  private final OnAzkarActionListener listener;

  public AzkarAdapter(OnAzkarActionListener listener) {
    super(DIFF_CALLBACK);
    this.listener = listener;
  }

  private static final DiffUtil.ItemCallback<AzkarEntity> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<AzkarEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull AzkarEntity oldItem, @NonNull AzkarEntity newItem) {
          return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AzkarEntity oldItem, @NonNull AzkarEntity newItem) {
          return oldItem.getCurrentCount() == newItem.getCurrentCount()
              && oldItem.getArabic().equals(newItem.getArabic());
        }
      };

  @NonNull
  @Override
  public AzkarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_azkar, parent, false);
    return new AzkarViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AzkarViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  class AzkarViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvArabic;
    private final TextView tvTransliteration;
    private final TextView tvBengali;
    private final TextView tvReference;
    private final TextView tvTargetCount;
    private final TextView tvBenefit;
    private final TextView tvCurrentCount;
    private final MaterialButton btnIncrement;
    private final View layoutBenefit;

    AzkarViewHolder(@NonNull View itemView) {
      super(itemView);
      tvArabic = itemView.findViewById(R.id.tvAzkarArabic);
      tvTransliteration = itemView.findViewById(R.id.tvAzkarTransliteration);
      tvBengali = itemView.findViewById(R.id.tvAzkarBengali);
      tvReference = itemView.findViewById(R.id.tvAzkarReference);
      tvTargetCount = itemView.findViewById(R.id.tvAzkarTargetCount);
      tvBenefit = itemView.findViewById(R.id.tvAzkarBenefit);
      tvCurrentCount = itemView.findViewById(R.id.tvAzkarCurrentCount);
      btnIncrement = itemView.findViewById(R.id.btnAzkarIncrement);
      layoutBenefit = itemView.findViewById(R.id.layoutBenefit);

      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnIncrement);
    }

    void bind(AzkarEntity azkar) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
      tvArabic.setText(azkar.getArabic());

      // Transliteration
      if (azkar.getTransliteration() != null && !azkar.getTransliteration().isEmpty()) {
        tvTransliteration.setVisibility(View.VISIBLE);
        tvTransliteration.setText(azkar.getTransliteration());
      } else {
        tvTransliteration.setVisibility(View.GONE);
      }

      String meaning = isBn ? azkar.getBengaliMeaning()
                            : (azkar.getEnglishMeaning() != null && !azkar.getEnglishMeaning().trim().isEmpty() ? azkar.getEnglishMeaning() : azkar.getBengaliMeaning());
      tvBengali.setText(meaning);
      tvReference.setText(azkar.getReference());

      // Count badge
      if (azkar.getTargetCount() > 1) {
        tvTargetCount.setVisibility(View.VISIBLE);
        tvTargetCount.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(azkar.getTargetCount()) + " বার") : (azkar.getTargetCount() + " times"));
      } else {
        tvTargetCount.setVisibility(View.GONE);
      }

      // Benefit
      if (azkar.getBenefit() != null && !azkar.getBenefit().isEmpty()) {
        layoutBenefit.setVisibility(View.VISIBLE);
        tvBenefit.setText(azkar.getBenefit());
      } else {
        layoutBenefit.setVisibility(View.GONE);
      }

      // Current count
      tvCurrentCount.setText(isBn
          ? ("পড়েছি: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(azkar.getCurrentCount()) + " বার")
          : ("Recited: " + azkar.getCurrentCount() + " times"));

      // Completed visual feedback
      if (azkar.getTargetCount() > 0 && azkar.getCurrentCount() >= azkar.getTargetCount()) {
        btnIncrement.setText(isBn ? "সম্পন্ন" : "Completed");
        btnIncrement.setAlpha(0.6f);
      } else {
        btnIncrement.setText(isBn ? "পড়লাম" : "Count");
        btnIncrement.setAlpha(1.0f);
      }

      btnIncrement.setOnClickListener(v -> {
        if (listener != null) listener.onIncrementCount(azkar);
      });
    }
  }
}
