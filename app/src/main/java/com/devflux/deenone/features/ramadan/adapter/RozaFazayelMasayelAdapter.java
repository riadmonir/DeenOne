package com.devflux.deenone.features.ramadan.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaFazayelMasayelRowBinding;
import com.devflux.deenone.features.ramadan.model.RozaFazayelMasayelItem;
import com.devflux.deenone.features.ramadan.ui.RozaAdditionalMasayelPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaAfterRamadanPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaArkanPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaBidahPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaDislikedActsPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaDutiesInRamadanPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaEtiquetteActsPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaFastingTypesPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaFazayelTopicDetailDialog;
import com.devflux.deenone.features.ramadan.ui.RozaFitraPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaForbiddenDaysPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaInvalidatorsPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaMoonSightingPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaPeopleCategoriesPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaPermissibleActsPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaSunnahNaflPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaVirtuesOfSawmPageDialog;
import com.devflux.deenone.features.ramadan.ui.RozaWeakHadithPageDialog;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * High-performance, lightweight RecyclerView Adapter for Roza Fazayel & Masayel (ফাযায়েল মাসায়েল).
 * Clean functional menu cards matching the user requirement:
 * - Shows serial rosette badge + topic title + chevron arrow button.
 * - Clicking card or chevron navigates into the topic's full detailed section page.
 * - STRICT Rule 7: ZERO touch animation on CardViews; touch spring ONLY on arrow button!
 */
public class RozaFazayelMasayelAdapter extends RecyclerView.Adapter<RozaFazayelMasayelAdapter.FazayelMasayelViewHolder> {

    private final List<RozaFazayelMasayelItem> items = new ArrayList<>();
    private final boolean isBn;

    public RozaFazayelMasayelAdapter(List<RozaFazayelMasayelItem> newItems, float fontSize, boolean isBn) {
        this.isBn = isBn;
        if (newItems != null) {
            this.items.addAll(newItems);
        }
    }

    public void setFontSize(float size) {
        // Main list displays functional rows; font scale preserved if needed
        notifyDataSetChanged();
    }

    public void updateData(List<RozaFazayelMasayelItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FazayelMasayelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaFazayelMasayelRowBinding binding = ItemRozaFazayelMasayelRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FazayelMasayelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FazayelMasayelViewHolder holder, int position) {
        RozaFazayelMasayelItem item = items.get(position);
        ItemRozaFazayelMasayelRowBinding b = holder.binding;

        // Title (Clean, no serial number)
        b.tvFazayelMasayelTitle.setText(item.getTitle(isBn));

        // Click handler to open the respective detailed section
        android.view.View.OnClickListener openSection = v -> {
            if (holder.itemView.getContext() instanceof Activity) {
                Activity act = (Activity) holder.itemView.getContext();
                openTopicSection(act, item);
            }
        };

        b.cardRozaFazayelMasayelItem.setOnClickListener(openSection);
        b.btnFazayelMasayelArrow.setOnClickListener(openSection);
    }

    public static void openTopicSection(Activity act, RozaFazayelMasayelItem item) {
        if (act == null || act.isFinishing() || act.isDestroyed() || item == null) return;

        String slug = item.getSlug() != null ? item.getSlug().toLowerCase().trim() : "";
        String titleBn = item.getTitleBn() != null ? item.getTitleBn() : "";

        if ("arkan".equals(slug) || titleBn.contains("আরকান")) {
            RozaArkanPageDialog.show(act);
            return;
        }
        if ("moon_sighting".equals(slug) || titleBn.contains("চাঁদ দেখা")) {
            RozaMoonSightingPageDialog.show(act);
            return;
        }
        if ("fitra_details".equals(slug) || titleBn.contains("ফিতরা")) {
            RozaFitraPageDialog.show(act);
            return;
        }
        if ("people_categories".equals(slug) || titleBn.contains("মানুষের শ্রেণী")) {
            RozaPeopleCategoriesPageDialog.show(act);
            return;
        }
        if ("invalidators_of_fast".equals(slug) || titleBn.contains("নষ্ট ও বাতিল")) {
            RozaInvalidatorsPageDialog.show(act);
            return;
        }
        if ("forbidden_fasting_days".equals(slug) || titleBn.contains("নিষিদ্ধ")) {
            RozaForbiddenDaysPageDialog.show(act);
            return;
        }
        if ("after_ramadan".equals(slug) || titleBn.contains("রমাযান পরে কি")) {
            RozaAfterRamadanPageDialog.show(act);
            return;
        }
        if ("duties_in_ramadan".equals(slug) || titleBn.contains("কর্তব্য")) {
            RozaDutiesInRamadanPageDialog.show(act);
            return;
        }
        if ("bidah_in_ramadan".equals(slug) || titleBn.contains("বিদআত")) {
            RozaBidahPageDialog.show(act);
            return;
        }
        if ("permissible_acts_fasting".equals(slug) || titleBn.contains("অবস্থায় যা বৈধ") || titleBn.contains("যা বৈধ")) {
            RozaPermissibleActsPageDialog.show(act);
            return;
        }
        if ("weak_hadiths_ramadan".equals(slug) || titleBn.contains("যয়ীফ ও জাল হাদীস")) {
            RozaWeakHadithPageDialog.show(act);
            return;
        }
        if ("additional_masayel".equals(slug) || titleBn.contains("আরো কিছু মাসায়েল")) {
            RozaAdditionalMasayelPageDialog.show(act);
            return;
        }
        if ("disliked_acts_fasting".equals(slug) || titleBn.contains("অপছন্দনীয়")) {
            RozaDislikedActsPageDialog.show(act);
            return;
        }
        if ("etiquette_of_fasting".equals(slug) || titleBn.contains("আদব")) {
            RozaEtiquetteActsPageDialog.show(act);
            return;
        }
        if ("virtues_of_sawm".equals(slug) || titleBn.contains("তাৎপর্য ও ফযীলত") || titleBn.contains("ফযীলত")) {
            RozaVirtuesOfSawmPageDialog.show(act);
            return;
        }
        if ("sunnah_nafl_fasting".equals(slug) || titleBn.contains("সুন্নত ও নফল") || titleBn.contains("সুন্নাত ও নফল") || titleBn.contains("নফল রোযা") || titleBn.contains("নফল রোজা")) {
            RozaSunnahNaflPageDialog.show(act);
            return;
        }
        if ("types_of_fasting_ramadan".equals(slug) || titleBn.contains("সিয়ামের প্রকারভেদ") || titleBn.contains("মাসের বৈশিষ্ট্য") || titleBn.contains("রোযার মাহাত্ম্য") || titleBn.contains("রোজার মাহাত্ম্য")) {
            RozaFastingTypesPageDialog.show(act);
            return;
        }

        // Generic / Fallback topic handler for any topic (e.g. রমাদ্বান মানে কি?, etc.)
        RozaFazayelTopicDetailDialog.show(act, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FazayelMasayelViewHolder extends RecyclerView.ViewHolder {
        final ItemRozaFazayelMasayelRowBinding binding;

        public FazayelMasayelViewHolder(ItemRozaFazayelMasayelRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // RULE 7: STRICT ZERO touch animation on CardView. Touch animation ONLY on arrow button!
            TouchAnimationUtil.attachTouchSpring(binding.btnFazayelMasayelArrow);
        }
    }
}

