package com.devflux.deenone.features.ramadan.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.ItemHajjHistoryCardBinding;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class RozaDuaAdapter extends RecyclerView.Adapter<RozaDuaAdapter.ViewHolder> {

    private final List<HajjHistoryCardItem> items = new ArrayList<>();
    private final boolean isBn;
    private float fontSize = 14.5f;

    public RozaDuaAdapter(android.content.Context context, List<HajjHistoryCardItem> initialItems) {
        this.isBn = LocaleManager.isBengali(context);
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    public void setItems(List<HajjHistoryCardItem> newItems) {
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
        ItemHajjHistoryCardBinding binding = ItemHajjHistoryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HajjHistoryCardItem item = items.get(position);
        ItemHajjHistoryCardBinding b = holder.binding;

        b.tvHistoryItemTitle.setText(item.getTitle(isBn));
        b.tvHistoryItemPreview.setText(item.getPreview(isBn));

        String fullText = item.getFullContent(isBn);
        if (fullText != null) {
            if (fullText.contains("<") && fullText.contains(">")) {
                String htmlFormatted = fullText.replace("\r\n", "<br>").replace("\n", "<br>");
                htmlFormatted = htmlFormatted.replaceAll("(<br\\s*/?>\\s*){3,}", "<br><br>");
                b.tvHistoryItemFullContent.setText(HtmlCompat.fromHtml(htmlFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                b.tvHistoryItemFullContent.setText(fullText);
            }
        } else {
            b.tvHistoryItemFullContent.setText("");
        }

        b.tvHistoryItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        b.tvHistoryItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        if (item.isExpanded()) {
            b.tvHistoryItemPreview.setVisibility(View.GONE);
            b.tvHistoryItemFullContent.setVisibility(View.VISIBLE);
            b.tvHistoryToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
            b.ivHistoryToggleChevron.setRotation(180f);
        } else {
            b.tvHistoryItemPreview.setVisibility(View.VISIBLE);
            b.tvHistoryItemFullContent.setVisibility(View.GONE);
            b.tvHistoryToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "View Details");
            b.ivHistoryToggleChevron.setRotation(0f);
        }

        View.OnClickListener toggleClick = v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(holder.getBindingAdapterPosition());
        };

        b.layoutHistoryToggleExpand.setOnClickListener(toggleClick);
        TouchAnimationUtil.attachTouchSpring(b.layoutHistoryToggleExpand);

        b.cardHistoryItem.setOnClickListener(toggleClick);
        // Note: No touch spring on cardHistoryItem per explicit user directive!
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemHajjHistoryCardBinding binding;

        public ViewHolder(@NonNull ItemHajjHistoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
