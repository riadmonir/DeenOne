package com.devflux.deenone.features.ramadan.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaHealthAdviceRowBinding;
import com.devflux.deenone.features.ramadan.model.RozaHealthAdviceItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * High-performance, lightweight RecyclerView Adapter for Roza Health Advice (রোজায় স্বাস্থ্য পরামর্শ).
 * Follows the Hajj History standard with expandable cards, dynamic font scaling,
 * button-only spring touch animation, and STRICT ZERO touch animation on CardViews (Rule 7).
 */
public class RozaHealthAdviceAdapter extends RecyclerView.Adapter<RozaHealthAdviceAdapter.HealthAdviceViewHolder> {

    private final List<RozaHealthAdviceItem> items = new ArrayList<>();
    private float fontSize;
    private final boolean isBn;

    public RozaHealthAdviceAdapter(List<RozaHealthAdviceItem> newItems, float fontSize, boolean isBn) {
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

    public void updateData(List<RozaHealthAdviceItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HealthAdviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaHealthAdviceRowBinding binding = ItemRozaHealthAdviceRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new HealthAdviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthAdviceViewHolder holder, int position) {
        RozaHealthAdviceItem item = items.get(position);
        ItemRozaHealthAdviceRowBinding b = holder.binding;

        // Title (17sp bold text_primary)
        b.tvRozaHealthAdviceTitle.setText(item.getHeaderTitle(isBn));

        // Preview text
        b.tvRozaHealthAdvicePreview.setText(item.getPreview(isBn));
        b.tvRozaHealthAdvicePreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // Detailed Full Content
        String fullText = item.getDetails(isBn);
        if (fullText != null) {
            if (fullText.contains("<") && fullText.contains(">")) {
                String htmlFormatted = fullText.replace("\r\n", "<br>").replace("\n", "<br>");
                htmlFormatted = htmlFormatted.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
                b.tvRozaHealthAdviceFullContent.setText(HtmlCompat.fromHtml(htmlFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                b.tvRozaHealthAdviceFullContent.setText(fullText);
            }
        } else {
            b.tvRozaHealthAdviceFullContent.setText("");
        }
        b.tvRozaHealthAdviceFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        // Reference (Islamic / Medical reference)
        String ref = item.getReference(isBn);
        if (ref != null && !ref.trim().isEmpty()) {
            b.tvRozaHealthAdviceReference.setText(ref);
        } else {
            b.tvRozaHealthAdviceReference.setText("");
        }

        // Expand / Collapse State
        if (item.isExpanded()) {
            b.tvRozaHealthAdvicePreview.setVisibility(View.GONE);
            b.tvRozaHealthAdviceFullContent.setVisibility(View.VISIBLE);
            if (ref != null && !ref.trim().isEmpty()) {
                b.tvRozaHealthAdviceReference.setVisibility(View.VISIBLE);
            } else {
                b.tvRozaHealthAdviceReference.setVisibility(View.GONE);
            }
            b.tvRozaHealthAdviceToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            b.ivRozaHealthAdviceToggleChevron.setRotation(180f);
        } else {
            b.tvRozaHealthAdvicePreview.setVisibility(View.VISIBLE);
            b.tvRozaHealthAdviceFullContent.setVisibility(View.GONE);
            b.tvRozaHealthAdviceReference.setVisibility(View.GONE);
            b.tvRozaHealthAdviceToggleText.setText(isBn ? "বিস্তারিত" : "Read More");
            b.ivRozaHealthAdviceToggleChevron.setRotation(0f);
        }

        View.OnClickListener toggleClick = v -> {
            item.setExpanded(!item.isExpanded());
            int adapterPos = holder.getBindingAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                notifyItemChanged(adapterPos);
            }
        };

        b.layoutRozaHealthAdviceToggle.setOnClickListener(toggleClick);
        b.cardRozaHealthAdviceItem.setOnClickListener(toggleClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HealthAdviceViewHolder extends RecyclerView.ViewHolder {
        final ItemRozaHealthAdviceRowBinding binding;

        public HealthAdviceViewHolder(ItemRozaHealthAdviceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // RULE 7: STRICT ZERO touch animation on CardView. Touch animation ONLY on toggle button!
            TouchAnimationUtil.attachTouchSpring(binding.layoutRozaHealthAdviceToggle);
        }
    }
}
