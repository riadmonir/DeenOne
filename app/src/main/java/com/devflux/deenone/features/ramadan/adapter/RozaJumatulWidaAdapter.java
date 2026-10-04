package com.devflux.deenone.features.ramadan.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaJumatulWidaRowBinding;
import com.devflux.deenone.features.ramadan.model.RozaJumatulWidaItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * High-performance, lightweight RecyclerView Adapter for Roza Jumatul Wida (জুমাতুল বিদা).
 * Follows the Hajj History standard with expandable cards, dynamic font scaling,
 * button-only spring touch animation, and STRICT ZERO touch animation on CardViews (Rule 7).
 */
public class RozaJumatulWidaAdapter extends RecyclerView.Adapter<RozaJumatulWidaAdapter.JumatulWidaViewHolder> {

    private final List<RozaJumatulWidaItem> items = new ArrayList<>();
    private float fontSize;
    private final boolean isBn;

    public RozaJumatulWidaAdapter(List<RozaJumatulWidaItem> newItems, float fontSize, boolean isBn) {
        this.fontSize = fontSize;
        this.isBn = isBn;
        if (newItems != null) {
            this.items.addAll(newItems);
        }
    }

    public void setFontSize(float size) {
        this.fontSize = size;
        notifyDataSetChanged();
    }

    public void updateData(List<RozaJumatulWidaItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JumatulWidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaJumatulWidaRowBinding binding = ItemRozaJumatulWidaRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new JumatulWidaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JumatulWidaViewHolder holder, int position) {
        RozaJumatulWidaItem item = items.get(position);
        ItemRozaJumatulWidaRowBinding b = holder.binding;

        // Title (17sp bold text_primary)
        b.tvJumatulWidaTitle.setText(item.getTitle(isBn));

        // Preview text
        b.tvJumatulWidaPreview.setText(item.getPreview(isBn));
        b.tvJumatulWidaPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // Detailed Full Content
        String fullText = item.getDetails(isBn);
        if (fullText != null) {
            if (fullText.contains("<") && fullText.contains(">")) {
                String htmlFormatted = fullText.replace("\r\n", "<br>").replace("\n", "<br>");
                htmlFormatted = htmlFormatted.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
                b.tvJumatulWidaFullContent.setText(HtmlCompat.fromHtml(htmlFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                b.tvJumatulWidaFullContent.setText(fullText);
            }
        } else {
            b.tvJumatulWidaFullContent.setText("");
        }
        b.tvJumatulWidaFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // Reference
        String ref = item.getReference(isBn);
        if (ref != null && !ref.trim().isEmpty()) {
            b.tvJumatulWidaReference.setText(ref);
        } else {
            b.tvJumatulWidaReference.setText("");
        }

        // Expand / Collapse State
        if (item.isExpanded()) {
            b.tvJumatulWidaPreview.setVisibility(View.GONE);
            b.tvJumatulWidaFullContent.setVisibility(View.VISIBLE);
            if (ref != null && !ref.trim().isEmpty()) {
                b.tvJumatulWidaReference.setVisibility(View.VISIBLE);
            } else {
                b.tvJumatulWidaReference.setVisibility(View.GONE);
            }
            b.tvJumatulWidaToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            b.ivJumatulWidaToggleChevron.setRotation(180f);
        } else {
            b.tvJumatulWidaPreview.setVisibility(View.VISIBLE);
            b.tvJumatulWidaFullContent.setVisibility(View.GONE);
            b.tvJumatulWidaReference.setVisibility(View.GONE);
            b.tvJumatulWidaToggleText.setText(isBn ? "বিস্তারিত" : "Read More");
            b.ivJumatulWidaToggleChevron.setRotation(0f);
        }

        View.OnClickListener toggleClick = v -> {
            item.setExpanded(!item.isExpanded());
            int adapterPos = holder.getBindingAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                notifyItemChanged(adapterPos);
            }
        };

        b.layoutJumatulWidaToggle.setOnClickListener(toggleClick);
        b.cardJumatulWidaItem.setOnClickListener(toggleClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class JumatulWidaViewHolder extends RecyclerView.ViewHolder {
        final ItemRozaJumatulWidaRowBinding binding;

        public JumatulWidaViewHolder(ItemRozaJumatulWidaRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // RULE 7: STRICT ZERO touch animation on CardView. Touch animation ONLY on toggle button!
            TouchAnimationUtil.attachTouchSpring(binding.layoutJumatulWidaToggle);
        }
    }
}
