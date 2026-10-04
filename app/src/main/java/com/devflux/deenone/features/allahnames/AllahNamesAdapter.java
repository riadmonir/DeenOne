package com.devflux.deenone.features.allahnames;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.tasbih.TasbihFeedbackHelper;
import com.devflux.deenone.data.model.AllahNameItem;
import com.devflux.deenone.databinding.ItemAllahNameCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllahNamesAdapter extends RecyclerView.Adapter<AllahNamesAdapter.ViewHolder> {

    public interface OnNameActionListener {
        void onPlayAudio(AllahNameItem item);
    }

    private final List<AllahNameItem> items = new ArrayList<>();
    private final Set<Integer> expandedPositions = new HashSet<>();
    private final Map<Integer, Integer> dhikrCounts = new HashMap<>();
    private final OnNameActionListener actionListener;
    private int currentPlayingNumber = -1;
    private boolean isPlaying = false;

    public AllahNamesAdapter(OnNameActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setItems(List<AllahNameItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setPlayingState(int number, boolean playing) {
        int oldNumber = currentPlayingNumber;
        this.currentPlayingNumber = number;
        this.isPlaying = playing;

        for (int i = 0; i < items.size(); i++) {
            int num = items.get(i).getNumber();
            if (num == oldNumber || num == currentPlayingNumber) {
                notifyItemChanged(i);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllahNameCardBinding binding = ItemAllahNameCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllahNameItem item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAllahNameCardBinding binding;

        ViewHolder(ItemAllahNameCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPlayNameAudio);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnShareName);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnToggleNameDetails);
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnDhikrCountPill);
        }

        void bind(AllahNameItem item, int position) {
            Context context = binding.getRoot().getContext();
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

            // Number badge
            binding.tvNameNumberBadge.setText(isBn ? ("#" + BengaliNumberUtil.toBengali(item.getNumber())) : ("#" + item.getNumber()));

            // Category badge
            String catTitle = isBn ? "পবিত্র নাম" : "Divine Name";
            if ("MERCY".equalsIgnoreCase(item.getCategory())) {
                catTitle = isBn ? "রহমত ও ক্ষমা" : "Mercy & Grace";
            } else if ("MAJESTY".equalsIgnoreCase(item.getCategory())) {
                catTitle = isBn ? "ক্ষমতা ও মহত্ত্ব" : "Majesty & Power";
            } else if ("PROVISION".equalsIgnoreCase(item.getCategory())) {
                catTitle = isBn ? "রিযিক ও অনুগ্রহ" : "Provision & Bounty";
            } else if ("WISDOM".equalsIgnoreCase(item.getCategory())) {
                catTitle = isBn ? "জ্ঞান ও প্রজ্ঞা" : "Wisdom & Knowledge";
            } else if ("PURITY".equalsIgnoreCase(item.getCategory())) {
                catTitle = isBn ? "পবিত্রতা ও শান্তি" : "Purity & Peace";
            }
            binding.tvNameCategoryBadge.setText(catTitle);

            // Names & Meanings Clean Separation (Rule 5)
            binding.tvAllahNameArabic.setText(item.getNameArabic());
            if (isBn) {
                binding.tvAllahNameBengali.setVisibility(View.VISIBLE);
                binding.tvAllahNameBengali.setText(item.getNameBengali());
                binding.tvAllahNameEnglish.setVisibility(View.GONE);
                if (binding.tvNameDividerDot != null) binding.tvNameDividerDot.setVisibility(View.GONE);
                binding.tvAllahNameMeaningBn.setVisibility(View.VISIBLE);
                binding.tvAllahNameMeaningBn.setText(item.getMeaningBengali());
                binding.tvAllahNameMeaningEn.setVisibility(View.GONE);
                binding.tvExplanationHeading.setText("তাৎপর্য ও ব্যাখ্যা:");
                binding.tvFazilatHeading.setText("ফজিলত ও বরকত:");
                binding.tvDhikrLabel.setText("জিকির: ");
            } else {
                binding.tvAllahNameBengali.setVisibility(View.GONE);
                binding.tvAllahNameEnglish.setVisibility(View.VISIBLE);
                binding.tvAllahNameEnglish.setText(item.getNameEnglish());
                binding.tvAllahNameEnglish.setTextSize(16f);
                binding.tvAllahNameEnglish.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
                if (binding.tvNameDividerDot != null) binding.tvNameDividerDot.setVisibility(View.GONE);
                binding.tvAllahNameMeaningBn.setVisibility(View.GONE);
                binding.tvAllahNameMeaningEn.setVisibility(View.VISIBLE);
                binding.tvAllahNameMeaningEn.setText(item.getMeaningEnglish());
                binding.tvExplanationHeading.setText("Significance & Explanation:");
                binding.tvFazilatHeading.setText("Virtues & Blessings:");
                binding.tvDhikrLabel.setText("Dhikr: ");
            }

            // Explanation & Fazilat
            binding.tvNameExplanation.setText(item.getExplanationBn());
            binding.tvNameFazilat.setText(item.getFazilatBn());

            // Expand / Collapse State
            boolean isExpanded = expandedPositions.contains(position);
            binding.layoutNameDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            binding.tvToggleDetailsLabel.setText(isBn ? (isExpanded ? "সংক্ষিপ্ত বিবরণ দেখুন" : "ফজিলত ও তাৎপর্য দেখুন")
                    : (isExpanded ? "Hide Details" : "View Significance & Virtue"));
            binding.ivChevronDetails.setRotation(isExpanded ? 90f : 0f);

            binding.btnToggleNameDetails.setOnClickListener(v -> {
                if (expandedPositions.contains(position)) {
                    expandedPositions.remove(position);
                } else {
                    expandedPositions.add(position);
                }
                notifyItemChanged(position);
            });

            // Audio Play State
            boolean thisIsPlaying = (currentPlayingNumber == item.getNumber() && isPlaying);
            binding.ivPlayIcon.setImageResource(thisIsPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            binding.btnPlayNameAudio.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, thisIsPlaying ? R.color.accent_mint : R.color.bg_card_secondary)
            );
            binding.ivPlayIcon.setColorFilter(
                    ContextCompat.getColor(context, thisIsPlaying ? R.color.bg_main : R.color.accent_mint)
            );

            binding.btnPlayNameAudio.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onPlayAudio(item);
                }
            });

            // Share Action
            binding.btnShareName.setOnClickListener(v -> {
                String shareText;
                if (isBn) {
                    shareText = "আল্লাহর পবিত্র নাম:\n"
                            + item.getNameArabic() + " — " + item.getNameBengali() + "\n"
                            + "অর্থ: " + item.getMeaningBengali() + "\n\n"
                            + "তাৎপর্য: " + item.getExplanationBn() + "\n"
                            + "ফজিলত: " + item.getFazilatBn() + "\n\n"
                            + "— দ্বীনওয়ান";
                } else {
                    shareText = "Divine Name of Allah:\n"
                            + item.getNameArabic() + " — " + item.getNameEnglish() + "\n"
                            + "Meaning: " + item.getMeaningEnglish() + "\n\n"
                            + "Significance: " + item.getExplanationBn() + "\n"
                            + "Virtue: " + item.getFazilatBn() + "\n\n"
                            + "— DeenOne";
                }
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share Name of Allah"));
            });

            // Dhikr Counter Tap
            int count = dhikrCounts.containsKey(item.getNumber()) ? dhikrCounts.get(item.getNumber()) : 0;
            binding.tvDhikrCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));

            binding.btnDhikrCountPill.setOnClickListener(v -> {
                int newCount = count + 1;
                dhikrCounts.put(item.getNumber(), newCount);
                binding.tvDhikrCount.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
                TasbihFeedbackHelper.playTapFeedback(context, v);
            });
        }
    }
}
