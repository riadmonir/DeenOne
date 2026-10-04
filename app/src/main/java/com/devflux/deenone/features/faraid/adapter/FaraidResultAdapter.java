package com.devflux.deenone.features.faraid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.faraid.model.HeirShareResult;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FaraidResultAdapter extends RecyclerView.Adapter<FaraidResultAdapter.ViewHolder> {

    public interface OnDalilClickListener {
        void onDalilClick(HeirShareResult item);
    }

    private final Context context;
    private final List<HeirShareResult> items = new ArrayList<>();
    private final OnDalilClickListener listener;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public FaraidResultAdapter(Context context, List<HeirShareResult> items, OnDalilClickListener listener) {
        this.context = context;
        if (items != null) {
            this.items.addAll(items);
        }
        this.listener = listener;
    }

    public void updateData(List<HeirShareResult> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setItems(List<HeirShareResult> newItems) {
        updateData(newItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faraid_heir_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HeirShareResult item = items.get(position);
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        holder.tvHeirTitle.setText(item.getLocalizedRelationTitle(context));
        holder.tvShareFraction.setText(item.getShareFractionLabel(isBn));
        if (holder.tvHeirPercentage != null) {
            String pctStr = isBn ? BengaliNumberUtil.toBengali(String.format("%.2f", item.getPercentage())) : String.format("%.2f", item.getPercentage());
            holder.tvHeirPercentage.setText(pctStr + "%");
        }

        if (holder.tvTotalCategoryAmountLabel != null) {
            holder.tvTotalCategoryAmountLabel.setText(isBn ? "সর্বমোট প্রাপ্য অর্থ" : "Total Category Share");
        }
        if (holder.tvIndividualAmountLabel != null) {
            holder.tvIndividualAmountLabel.setText(isBn ? "প্রতিজন পাবেন অর্থ" : "Per Person Share");
        }
        if (holder.tvTotalPropertyLabel != null) {
            holder.tvTotalPropertyLabel.setText(isBn ? "প্রাপ্য জমি / সম্পত্তি" : "Total Property Share");
        }
        if (holder.tvIndividualPropertyLabel != null) {
            holder.tvIndividualPropertyLabel.setText(isBn ? "প্রতিজন পাবেন জমি" : "Per Person Property");
        }
        if (holder.tvCalculationFormulaTitle != null) {
            holder.tvCalculationFormulaTitle.setText(isBn ? "গণনা সূত্র ও বিশ্লেষণ:" : "Formula & Calculation:");
        }
        if (holder.tvMadhabNote != null) {
            holder.tvMadhabNote.setText(item.getMadhabConsensusNote(isBn));
        }
        if (holder.tvBangladeshLawNote != null) {
            holder.tvBangladeshLawNote.setText(item.getBangladeshLawNote(isBn));
        }

        // Money Share (নগদ অর্থ / ত্যাজ্যবিত্ত বণ্টন)
        if (holder.layoutMoneyShareSection != null) {
            if (item.getTotalCategoryAmount() > 0) {
                holder.layoutMoneyShareSection.setVisibility(View.VISIBLE);
                holder.tvTotalCategoryAmount.setText(item.getTotalCategoryAmountFormatted(isBn));
                if (item.getCount() > 1) {
                    if (holder.layoutIndividualAmount != null) holder.layoutIndividualAmount.setVisibility(View.VISIBLE);
                    holder.tvIndividualAmount.setText(item.getIndividualAmountFormatted(isBn));
                } else {
                    if (holder.layoutIndividualAmount != null) holder.layoutIndividualAmount.setVisibility(View.GONE);
                }
            } else {
                holder.layoutMoneyShareSection.setVisibility(View.GONE);
            }
        } else {
            holder.tvTotalCategoryAmount.setText(item.getTotalCategoryAmountFormatted(isBn));
            if (item.getCount() > 1) {
                holder.tvIndividualAmount.setVisibility(View.VISIBLE);
                holder.tvIndividualAmount.setText(item.getIndividualAmountFormatted(isBn));
            } else {
                holder.tvIndividualAmount.setVisibility(View.GONE);
            }
        }

        // Transparency (Prescribed Share, Legal Reason, Calculation Formula)
        if (holder.tvOriginalPrescribedShare != null) {
            holder.tvOriginalPrescribedShare.setText((isBn ? "মূল নির্ধারিত অংশ: " : "Prescribed Share: ") + item.getOriginalPrescribedShare(isBn));
        }
        if (holder.tvLegalReason != null) {
            holder.tvLegalReason.setText((isBn ? "প্রাপ্তির কারণ: " : "Legal Basis: ") + item.getLegalReason(isBn));
        }
        if (holder.tvCalculationFormula != null) {
            holder.tvCalculationFormula.setText((isBn ? "গণনার সূত্র: " : "Calculation Formula: ") + item.getCalculationFormula(isBn));
        }

        // Land & Real Estate Property Share (স্থাবর সম্পত্তি / জমি বণ্টন)
        if (holder.layoutPropertyShareBox != null) {
            if (item.getTotalPropertyAmount() > 0) {
                holder.layoutPropertyShareBox.setVisibility(View.VISIBLE);
                if (holder.tvTotalPropertyShare != null) {
                    holder.tvTotalPropertyShare.setText(item.getTotalPropertyFormatted(isBn));
                }
                if (holder.layoutIndividualProperty != null && holder.tvIndividualPropertyShare != null) {
                    if (item.getCount() > 1) {
                        holder.layoutIndividualProperty.setVisibility(View.VISIBLE);
                        holder.tvIndividualPropertyShare.setText(item.getIndividualPropertyFormatted(isBn));
                    } else {
                        holder.layoutIndividualProperty.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.layoutPropertyShareBox.setVisibility(View.GONE);
            }
        }

        holder.tvDalilReference.setText(item.getDalilReference(isBn));

        if (item.getArabicDalil() != null && !item.getArabicDalil().isEmpty()) {
            if (holder.layoutDalilBox != null) holder.layoutDalilBox.setVisibility(View.VISIBLE);
            holder.tvArabicDalil.setText(item.getArabicDalil());

            if (item.getDalilPronunciation(isBn) != null && !item.getDalilPronunciation(isBn).isEmpty()) {
                holder.tvDalilPronunciation.setVisibility(View.VISIBLE);
                holder.tvDalilPronunciation.setText((isBn ? "উচ্চারণ: " : "Pronunciation: ") + item.getDalilPronunciation(isBn));
            } else {
                holder.tvDalilPronunciation.setVisibility(View.GONE);
            }

            if (item.getDalilTranslation(isBn) != null && !item.getDalilTranslation(isBn).isEmpty()) {
                holder.tvDalilTranslation.setVisibility(View.VISIBLE);
                holder.tvDalilTranslation.setText((isBn ? "অর্থ: \"" : "Translation: \"") + item.getDalilTranslation(isBn) + "\"");
            } else {
                holder.tvDalilTranslation.setVisibility(View.GONE);
            }
        } else {
            if (holder.layoutDalilBox != null) holder.layoutDalilBox.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDalilClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeirTitle, tvShareFraction, tvHeirPercentage, tvTotalCategoryAmount, tvIndividualAmount;
        TextView tvTotalCategoryAmountLabel, tvIndividualAmountLabel, tvTotalPropertyLabel, tvIndividualPropertyLabel;
        TextView tvCalculationFormulaTitle, tvOriginalPrescribedShare, tvLegalReason, tvCalculationFormula;
        TextView tvDalilReference, tvArabicDalil, tvDalilPronunciation, tvDalilTranslation;
        TextView tvMadhabNote, tvBangladeshLawNote;
        View layoutDalilBox, layoutMoneyShareSection, layoutIndividualAmount;

        View layoutPropertyShareBox, layoutIndividualProperty;
        TextView tvTotalPropertyShare, tvIndividualPropertyShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeirTitle = itemView.findViewById(R.id.tvHeirTitle);
            tvShareFraction = itemView.findViewById(R.id.tvShareFraction);
            tvHeirPercentage = itemView.findViewById(R.id.tvHeirPercentage);
            tvTotalCategoryAmount = itemView.findViewById(R.id.tvTotalCategoryAmount);
            tvIndividualAmount = itemView.findViewById(R.id.tvIndividualAmount);
            tvTotalCategoryAmountLabel = itemView.findViewById(R.id.tvTotalCategoryAmountLabel);
            tvIndividualAmountLabel = itemView.findViewById(R.id.tvIndividualAmountLabel);
            layoutMoneyShareSection = itemView.findViewById(R.id.layoutMoneyShareSection);
            layoutIndividualAmount = itemView.findViewById(R.id.layoutIndividualAmount);
            tvCalculationFormulaTitle = itemView.findViewById(R.id.tvCalculationFormulaTitle);
            tvOriginalPrescribedShare = itemView.findViewById(R.id.tvOriginalPrescribedShare);
            tvLegalReason = itemView.findViewById(R.id.tvLegalReason);
            tvCalculationFormula = itemView.findViewById(R.id.tvCalculationFormula);
            tvDalilReference = itemView.findViewById(R.id.tvDalilReference);
            tvArabicDalil = itemView.findViewById(R.id.tvArabicDalil);
            tvDalilPronunciation = itemView.findViewById(R.id.tvDalilPronunciation);
            tvDalilTranslation = itemView.findViewById(R.id.tvDalilTranslation);
            tvMadhabNote = itemView.findViewById(R.id.tvMadhabNote);
            tvBangladeshLawNote = itemView.findViewById(R.id.tvBangladeshLawNote);
            layoutDalilBox = itemView.findViewById(R.id.layoutDalilBox);

            layoutPropertyShareBox = itemView.findViewById(R.id.layoutPropertyShareBox);
            layoutIndividualProperty = itemView.findViewById(R.id.layoutIndividualProperty);
            tvTotalPropertyLabel = itemView.findViewById(R.id.tvTotalPropertyLabel);
            tvIndividualPropertyLabel = itemView.findViewById(R.id.tvIndividualPropertyLabel);
            tvTotalPropertyShare = itemView.findViewById(R.id.tvTotalPropertyShare);
            tvIndividualPropertyShare = itemView.findViewById(R.id.tvIndividualPropertyShare);
        }
    }
}