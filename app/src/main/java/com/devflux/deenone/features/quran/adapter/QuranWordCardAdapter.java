package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranWordItem;
import com.devflux.deenone.databinding.ItemQuranWordCardBinding;

import java.util.ArrayList;
import java.util.List;

public class QuranWordCardAdapter extends RecyclerView.Adapter<QuranWordCardAdapter.WordViewHolder> {

    private List<QuranWordItem> words = new ArrayList<>();

    public void setWords(List<QuranWordItem> words) {
        this.words = words != null ? words : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuranWordCardBinding binding = ItemQuranWordCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new WordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.bind(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranWordCardBinding binding;

        public WordViewHolder(@NonNull ItemQuranWordCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Rule 7: Strict ZERO touch animation on CardViews.
        }

        public void bind(QuranWordItem item) {
            Context context = binding.getRoot().getContext();
            boolean isBengali = LocaleManager.isBengali(context);

            binding.tvWordArabic.setText(item.getTextArabic() != null ? item.getTextArabic().trim() : "");
            binding.tvWordMeaning.setText(item.getMeaning(isBengali));
        }
    }
}
