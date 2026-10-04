package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranJourneyManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class QuranJourneyRevisionAdapter extends RecyclerView.Adapter<QuranJourneyRevisionAdapter.RevisionViewHolder> {

    public interface OnPlayRevisionListener {
        void onPlayRevision(QuranJourneyManager.RevisionAyah item, int position);
    }

    private final List<QuranJourneyManager.RevisionAyah> items = new ArrayList<>();
    private final OnPlayRevisionListener listener;
    private final boolean isBn;
    private int currentPlayingPosition = -1;

    public QuranJourneyRevisionAdapter(Context context, List<QuranJourneyManager.RevisionAyah> initialItems, OnPlayRevisionListener listener) {
        this.isBn = LocaleManager.isBengali(context);
        this.listener = listener;
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    @NonNull
    @Override
    public RevisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_journey_revision, parent, false);
        return new RevisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevisionViewHolder holder, int position) {
        QuranJourneyManager.RevisionAyah item = items.get(position);

        String dayText;
        if (isBn) {
            String[] bnOrdinals = {"১ম", "২য়", "৩য়", "৪র্থ", "৫ম", "৬ষ্ঠ", "৭ম"};
            int idx = item.dayNumber - 1;
            dayText = (idx >= 0 && idx < bnOrdinals.length ? bnOrdinals[idx] : BengaliNumberUtil.toBengali(item.dayNumber)) + " দিন";
        } else {
            dayText = "Day " + item.dayNumber;
        }
        holder.tvRevisionDay.setText(dayText);
        holder.tvRevisionArabic.setText(item.arabicText);

        boolean isPlaying = (position == currentPlayingPosition);
        holder.ivPlayRevisionIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);

        TouchAnimationUtil.attachTouchSpring(holder.btnPlayRevisionAudio);
        holder.btnPlayRevisionAudio.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayRevision(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setCurrentPlayingPosition(int position) {
        int old = currentPlayingPosition;
        currentPlayingPosition = position;
        if (old != -1) notifyItemChanged(old);
        if (currentPlayingPosition != -1) notifyItemChanged(currentPlayingPosition);
    }

    public int getCurrentPlayingPosition() {
        return currentPlayingPosition;
    }

    static class RevisionViewHolder extends RecyclerView.ViewHolder {
        final TextView tvRevisionDay;
        final TextView tvRevisionArabic;
        final FrameLayout btnPlayRevisionAudio;
        final ImageView ivPlayRevisionIcon;

        RevisionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRevisionDay = itemView.findViewById(R.id.tvRevisionDay);
            tvRevisionArabic = itemView.findViewById(R.id.tvRevisionArabic);
            btnPlayRevisionAudio = itemView.findViewById(R.id.btnPlayRevisionAudio);
            ivPlayRevisionIcon = itemView.findViewById(R.id.ivPlayRevisionIcon);
        }
    }
}
