package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranJourneyManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class QuranJourneyWordAdapter extends RecyclerView.Adapter<QuranJourneyWordAdapter.WordViewHolder> {

    private final List<QuranJourneyManager.JourneyWord> words = new ArrayList<>();
    private final boolean isBn;

    public QuranJourneyWordAdapter(Context context, List<QuranJourneyManager.JourneyWord> initialWords) {
        this.isBn = LocaleManager.isBengali(context);
        if (initialWords != null) {
            this.words.addAll(initialWords);
        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_journey_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        QuranJourneyManager.JourneyWord word = words.get(position);

        holder.tvWordSerial.setText(isBn ? BengaliNumberUtil.toBengali(word.serial) : String.valueOf(word.serial));
        holder.tvWordArabic.setText(word.arabic);

        if (word.isRevealed) {
            holder.tvWordMeaning.setText(isBn ? word.meaningBengali : word.meaningEnglish);
            holder.tvWordMeaning.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_mint));
            holder.ivEyeToggle.setImageResource(R.drawable.ic_eye);
            holder.ivEyeToggle.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent_mint));
        } else {
            holder.tvWordMeaning.setText(isBn ? "ট্যাপ করুন" : "Tap to reveal");
            holder.tvWordMeaning.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary));
            holder.ivEyeToggle.setImageResource(R.drawable.ic_eye_off);
            holder.ivEyeToggle.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary));
        }
        holder.itemView.setOnClickListener(v -> {
            word.isRevealed = !word.isRevealed;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<QuranJourneyManager.JourneyWord> newWords) {
        words.clear();
        if (newWords != null) {
            words.addAll(newWords);
        }
        notifyDataSetChanged();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        final TextView tvWordSerial;
        final ImageView ivEyeToggle;
        final TextView tvWordArabic;
        final TextView tvWordMeaning;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWordSerial = itemView.findViewById(R.id.tvWordSerial);
            ivEyeToggle = itemView.findViewById(R.id.ivEyeToggle);
            tvWordArabic = itemView.findViewById(R.id.tvWordArabic);
            tvWordMeaning = itemView.findViewById(R.id.tvWordMeaning);
        }
    }
}
