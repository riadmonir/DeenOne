package com.devflux.deenone.features.kalima;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.model.SixKalimaItem;
import com.devflux.deenone.databinding.ItemKalimaCardBinding;

import java.util.ArrayList;
import java.util.List;

public class SixKalimaAdapter extends RecyclerView.Adapter<SixKalimaAdapter.ViewHolder> {

    public interface OnKalimaActionListener {
        void onPlayAudio(SixKalimaItem item);
    }

    private final List<SixKalimaItem> items = new ArrayList<>();
    private final OnKalimaActionListener actionListener;
    private int currentPlayingNumber = -1;
    private boolean isPlaying = false;

    public SixKalimaAdapter(OnKalimaActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setItems(List<SixKalimaItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setPlayingState(int number, boolean playing) {
        int oldNum = currentPlayingNumber;
        this.currentPlayingNumber = number;
        this.isPlaying = playing;

        for (int i = 0; i < items.size(); i++) {
            int num = items.get(i).getNumber();
            if (num == oldNum || num == currentPlayingNumber) {
                notifyItemChanged(i);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKalimaCardBinding binding = ItemKalimaCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SixKalimaItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemKalimaCardBinding binding;

        ViewHolder(ItemKalimaCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPlayKalimaAudio);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCopyKalima);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnShareKalima);
        }

        void bind(SixKalimaItem item) {
            Context context = binding.getRoot().getContext();
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

            binding.tvKalimaTitle.setText(isBn ? item.getTitleBangla() : item.getTitleEnglish());
            binding.tvKalimaSubtitle.setText(isBn ? item.getTitleEnglish() : item.getTitleBangla());
            binding.tvKalimaArabic.setText(item.getArabicText());
            binding.tvKalimaPronunciationBn.setText(isBn ? item.getPronunciationBn() : item.getPronunciationEn());
            binding.tvKalimaMeaningBn.setText(item.getMeaningBn());
            binding.tvKalimaMeaningEn.setText(item.getMeaningEn());
            binding.tvKalimaSignificance.setText(item.getShariahSignificance());

            binding.tvKalimaPronunciationLabel.setText(isBn ? "উচ্চারণ:" : "Pronunciation:");
            binding.tvKalimaMeaningBnLabel.setText(isBn ? "বাংলা অর্থ:" : "Bengali Meaning:");
            binding.tvKalimaMeaningEnLabel.setText(isBn ? "ইংরেজি অনুবাদ:" : "English Translation:");
            binding.tvKalimaSignificanceLabel.setText(isBn ? "শরিয়াহ তাৎপর্য ও ফজিলত:" : "Shariah Significance & Virtues:");

            // Audio Play State
            boolean thisIsPlaying = (currentPlayingNumber == item.getNumber() && isPlaying);
            binding.ivKalimaPlayIcon.setImageResource(thisIsPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            binding.btnPlayKalimaAudio.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, thisIsPlaying ? R.color.accent_mint : R.color.bg_card_secondary)
            );
            binding.ivKalimaPlayIcon.setColorFilter(
                    ContextCompat.getColor(context, thisIsPlaying ? R.color.bg_main : R.color.accent_mint)
            );

            binding.btnPlayKalimaAudio.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onPlayAudio(item);
                }
            });

            // Copy Action
            binding.btnCopyKalima.setOnClickListener(v -> {
                String copyText;
                if (isBn) {
                    copyText = item.getTitleBangla() + "\n\n"
                            + item.getArabicText() + "\n\n"
                            + "উচ্চারণ: " + item.getPronunciationBn() + "\n\n"
                            + "অর্থ: " + item.getMeaningBn() + "\n\n"
                            + "তাৎপর্য: " + item.getShariahSignificance() + "\n\n"
                            + "— দ্বীনওয়ান";
                } else {
                    copyText = item.getTitleEnglish() + "\n\n"
                            + item.getArabicText() + "\n\n"
                            + "Transliteration: " + item.getPronunciationEn() + "\n\n"
                            + "English Meaning: " + item.getMeaningEn() + "\n\n"
                            + "Bengali Meaning: " + item.getMeaningBn() + "\n\n"
                            + "Significance: " + item.getShariahSignificance() + "\n\n"
                            + "— DeenOne Islamic App";
                }
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("Kalima Text", copyText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, isBn ? "কালিমা কপি করা হয়েছে" : "Kalima copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            // Share Action
            binding.btnShareKalima.setOnClickListener(v -> {
                String shareText;
                if (isBn) {
                    shareText = item.getTitleBangla() + "\n\n"
                            + item.getArabicText() + "\n\n"
                            + "উচ্চারণ: " + item.getPronunciationBn() + "\n\n"
                            + "অর্থ: " + item.getMeaningBn() + "\n\n"
                            + "ইংরেজি অনুবাদ: " + item.getMeaningEn() + "\n\n"
                            + "তাৎপর্য: " + item.getShariahSignificance() + "\n\n"
                            + "— দ্বীনওয়ান ইসলামিক অ্যাপ";
                } else {
                    shareText = item.getTitleEnglish() + "\n\n"
                            + item.getArabicText() + "\n\n"
                            + "Transliteration: " + item.getPronunciationEn() + "\n\n"
                            + "English Meaning: " + item.getMeaningEn() + "\n\n"
                            + "Bengali Meaning: " + item.getMeaningBn() + "\n\n"
                            + "Significance: " + item.getShariahSignificance() + "\n\n"
                            + "— DeenOne Islamic App";
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                context.startActivity(Intent.createChooser(intent, isBn ? "কালিমা শেয়ার করুন" : "Share Kalima"));
            });
        }
    }
}
