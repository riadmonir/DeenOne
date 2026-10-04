package com.devflux.deenone.features.prophets.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.ItemProphetExpandableCardBinding;
import com.devflux.deenone.features.prophets.model.ProphetOverviewTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class ProphetOverviewAdapter extends RecyclerView.Adapter<ProphetOverviewAdapter.ViewHolder> {

    private final List<ProphetOverviewTopicItem> items = new ArrayList<>();
    private final boolean isBn;
    private float fontSize = 14.5f;

    public ProphetOverviewAdapter(android.content.Context context, List<ProphetOverviewTopicItem> initialItems) {
        this.isBn = LocaleManager.isBengali(context);
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    public void setItems(List<ProphetOverviewTopicItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setFontSize(float size) {
        this.fontSize = size;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProphetExpandableCardBinding binding = ItemProphetExpandableCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProphetOverviewTopicItem item = items.get(position);
        ItemProphetExpandableCardBinding b = holder.binding;

        b.tvCardTitle.setText(item.getTitle(isBn));

        String content = item.getContent(isBn);
        String preview = extractPreview(content);
        b.tvCardPreview.setText(preview);

        if (content != null) {
            if (content.contains("<") && content.contains(">")) {
                String htmlFormatted = content.replace("\r\n", "<br>").replace("\n", "<br>");
                htmlFormatted = htmlFormatted.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
                b.tvCardFullContent.setText(HtmlCompat.fromHtml(htmlFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                b.tvCardFullContent.setText(content);
            }
        } else {
            b.tvCardFullContent.setText("");
        }

        b.tvCardPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        b.tvCardFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        if (item.isExpanded()) {
            b.tvCardPreview.setVisibility(View.GONE);
            b.tvCardFullContent.setVisibility(View.VISIBLE);
            b.tvToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            b.ivToggleChevron.setRotation(180f);
        } else {
            b.tvCardPreview.setVisibility(View.VISIBLE);
            b.tvCardFullContent.setVisibility(View.GONE);
            b.tvToggleText.setText(isBn ? "বিস্তারিত" : "View Details");
            b.ivToggleChevron.setRotation(0f);
        }

        View.OnClickListener toggleClick = v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(holder.getBindingAdapterPosition());
        };

        b.layoutToggleExpand.setOnClickListener(toggleClick);
        TouchAnimationUtil.attachTouchSpring(b.layoutToggleExpand);

        b.cardExpandableItem.setOnClickListener(toggleClick);
        // Strict Rule 7: ZERO touch animation on cardExpandableItem!
    }

    private String extractPreview(String content) {
        if (content == null) return "";
        String clean = content.replaceAll("<[^>]*>", "").trim();
        int firstNewline = clean.indexOf("\n");
        if (firstNewline > 0 && firstNewline < 160) {
            int secondNewline = clean.indexOf("\n", firstNewline + 1);
            if (secondNewline > 0 && secondNewline < 220) {
                return clean.substring(0, secondNewline).trim() + "...";
            }
            return clean.substring(0, firstNewline).trim() + "...";
        }
        if (clean.length() > 160) {
            return clean.substring(0, 155) + "...";
        }
        return clean;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProphetExpandableCardBinding binding;

        public ViewHolder(@NonNull ItemProphetExpandableCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
