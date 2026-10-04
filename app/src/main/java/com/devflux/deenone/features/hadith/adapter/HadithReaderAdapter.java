package com.devflux.deenone.features.hadith.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.ItemHadithReaderCardBinding;
import com.devflux.deenone.databinding.ItemHadithSectionHeaderBinding;
import com.devflux.deenone.features.hadith.model.HadithReaderItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class HadithReaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnHadithActionListener {
        void onMoreOptionsClick(HadithReaderItem item, View anchorView);
    }

    private List<HadithReaderItem> items = new ArrayList<>();
    private final OnHadithActionListener listener;
    private float arabicTextSizeSp = 19.5f;
    private float translationTextSizeSp = 14.5f;

    public HadithReaderAdapter(OnHadithActionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<HadithReaderItem> newItems) {
        this.items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setTextSizes(float arabicSp, float translationSp) {
        this.arabicTextSizeSp = arabicSp;
        this.translationTextSizeSp = translationSp;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == HadithReaderItem.TYPE_SECTION_HEADER) {
            ItemHadithSectionHeaderBinding binding = ItemHadithSectionHeaderBinding.inflate(inflater, parent, false);
            return new SectionHeaderViewHolder(binding);
        } else {
            ItemHadithReaderCardBinding binding = ItemHadithReaderCardBinding.inflate(inflater, parent, false);
            return new HadithCardViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HadithReaderItem item = items.get(position);
        if (holder instanceof SectionHeaderViewHolder) {
            ((SectionHeaderViewHolder) holder).bind(item);
        } else if (holder instanceof HadithCardViewHolder) {
            ((HadithCardViewHolder) holder).bind(item, position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // --- View Holder 1: Section Header ---
    public static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemHadithSectionHeaderBinding binding;

        public SectionHeaderViewHolder(ItemHadithSectionHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HadithReaderItem item) {
            Context context = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(context);

            String tag = item.getSectionTag() != null ? item.getSectionTag() : "";
            String title = item.getSectionTitle() != null ? item.getSectionTitle() : "";

            if (!tag.isEmpty() && !title.isEmpty()) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                ssb.append(tag);
                int tagEnd = ssb.length();
                ssb.setSpan(
                        new ForegroundColorSpan(ContextCompat.getColor(context, R.color.accent_mint)),
                        0, tagEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                ssb.append(" ").append(title);
                binding.tvSectionTitle.setText(ssb);
            } else {
                binding.tvSectionTitle.setText(tag.isEmpty() ? title : tag);
            }

            if (item.getSectionArabicVerse() != null && !item.getSectionArabicVerse().trim().isEmpty()) {
                binding.tvSectionArabicVerse.setVisibility(View.VISIBLE);
                binding.tvSectionArabicVerse.setText(item.getSectionArabicVerse().trim());
                binding.dividerSection.setVisibility(View.VISIBLE);
            } else {
                binding.tvSectionArabicVerse.setVisibility(View.GONE);
                binding.dividerSection.setVisibility(View.GONE);
            }

            if (item.getSectionTranslation() != null && !item.getSectionTranslation().trim().isEmpty()) {
                binding.tvSectionTranslation.setVisibility(View.VISIBLE);
                binding.tvSectionTranslation.setText(item.getSectionTranslation().trim());
            } else {
                binding.tvSectionTranslation.setVisibility(View.GONE);
            }
        }
    }

    // --- View Holder 2: Hadith Card ---
    public class HadithCardViewHolder extends RecyclerView.ViewHolder {
        private final ItemHadithReaderCardBinding binding;

        public HadithCardViewHolder(ItemHadithReaderCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnHadith3Dots);
        }

        public void bind(HadithReaderItem item, int position) {
            Context context = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(context);

            // Book initial and name
            String bookSlug = item.getBookSlug() != null ? item.getBookSlug() : "bukhari";
            String initial = "H";
            if (!bookSlug.isEmpty()) {
                initial = String.valueOf(bookSlug.charAt(0)).toUpperCase();
            }
            binding.tvHexagonLetter.setText(initial);
            binding.tvCardBookName.setText(item.getBookName(isBn));
            binding.tvCardHadithNum.setText((isBn ? "হাদিস: " : "Hadith: ") + item.getHadithNumber(isBn));

            // Grade badge
            String grade = item.getGrade(isBn);
            if (grade != null && !grade.isEmpty()) {
                binding.tvHadithGradeBadge.setVisibility(View.VISIBLE);
                binding.tvHadithGradeBadge.setText(grade);
            } else {
                binding.tvHadithGradeBadge.setVisibility(View.GONE);
            }

            // Text font size adjustments
            binding.tvHadithArabicFull.setTextSize(TypedValue.COMPLEX_UNIT_SP, arabicTextSizeSp);
            binding.tvHadithTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, translationTextSizeSp);

            // Arabic text
            String arabicText = item.getArabicText();
            if (arabicText != null && !arabicText.trim().isEmpty()) {
                binding.tvHadithArabicFull.setVisibility(View.VISIBLE);
                binding.tvHadithArabicFull.setText(arabicText.trim());
            } else {
                binding.tvHadithArabicFull.setVisibility(View.GONE);
            }

            // Narrator
            String narrator = item.getNarrator(isBn);
            if (narrator != null && !narrator.trim().isEmpty()) {
                binding.tvHadithNarrator.setVisibility(View.VISIBLE);
                binding.tvHadithNarrator.setText(narrator.trim());
            } else {
                binding.tvHadithNarrator.setVisibility(View.GONE);
            }

            // Localized Translation
            binding.tvHadithTranslation.setText(item.getTranslation(isBn));

            // Footnote
            String footnote = item.getFootnote(isBn);
            if (footnote != null && !footnote.trim().isEmpty()) {
                binding.layoutFootnoteBox.setVisibility(View.VISIBLE);
                binding.tvFootnoteLabel.setText(isBn ? "ফুটনোট:" : "Footnote:");
                binding.tvFootnoteContent.setText(footnote.trim());
            } else {
                binding.layoutFootnoteBox.setVisibility(View.GONE);
            }

            // 3-Dots Click
            binding.btnHadith3Dots.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreOptionsClick(item, binding.btnHadith3Dots);
                }
            });
        }
    }
}
