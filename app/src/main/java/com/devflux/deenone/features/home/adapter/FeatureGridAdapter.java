package com.devflux.deenone.features.home.adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemFeatureGridBinding;
import com.devflux.deenone.features.home.model.FeatureItem;

import java.util.ArrayList;
import java.util.List;

public class FeatureGridAdapter extends RecyclerView.Adapter<FeatureGridAdapter.FeatureViewHolder> {

    public interface OnFeatureClickListener {
        void onFeatureClick(FeatureItem item);
    }

    private List<FeatureItem> featureList;
    private final OnFeatureClickListener listener;

    public FeatureGridAdapter(List<FeatureItem> featureList, OnFeatureClickListener listener) {
        this.featureList = featureList != null ? new ArrayList<>(featureList) : new ArrayList<>();
        this.listener = listener;
    }

    public void setItems(List<FeatureItem> items) {
        this.featureList = items != null ? new ArrayList<>(items) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeatureGridBinding binding = ItemFeatureGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new FeatureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        FeatureItem item = featureList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeatureGridBinding binding;

        public FeatureViewHolder(ItemFeatureGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeatureItem item, OnFeatureClickListener listener) {
            android.content.Context ctx = binding.getRoot().getContext();
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(ctx);
            String title = getLocalizedFeatureTitle(item.getActionKey(), item.getTitle(), isBn);
            binding.tvFeatureTitle.setText(title);
            binding.ivFeatureIcon.setImageResource(item.getIconResId());

            boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;

            int tintColor;
            if (item.getIconTintInt() != 0) {
                tintColor = item.getIconTintInt();
                binding.ivFeatureIcon.setImageTintList(ColorStateList.valueOf(tintColor));
            } else {
                tintColor = ContextCompat.getColor(ctx, com.devflux.deenone.R.color.accent_mint);
                binding.ivFeatureIcon.setImageTintList(null);
            }

            // Circular background color
            GradientDrawable circleBg = new GradientDrawable();
            circleBg.setShape(GradientDrawable.OVAL);

            int bgColor;
            if (item.getCircleBgColorResId() != 0) {
                bgColor = ContextCompat.getColor(ctx, item.getCircleBgColorResId());
            } else if (isNight && item.getCircleBgColorInt() != 0) {
                bgColor = item.getCircleBgColorInt();
            } else {
                // In Light Mode or fallback: soft pastel tinted circular pill (14% alpha)
                bgColor = androidx.core.graphics.ColorUtils.setAlphaComponent(tintColor, 35);
            }
            circleBg.setColor(bgColor);
            binding.iconContainer.setBackground(circleBg);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeatureClick(item);
                }
            });
        }

        private static String getLocalizedFeatureTitle(String actionKey, String defaultTitle, boolean isBn) {
            if (actionKey == null) return defaultTitle;
            switch (actionKey) {
                case "action_salat":
                    return isBn ? "সালাত" : "Salah";
                case "action_namaz_shikha":
                    return isBn ? "নামাজ শিক্ষা" : "Prayer Guide";
                case "action_quran":
                    return isBn ? "কুরআন মাজিদ" : "Al Quran";
                case "action_audio":
                    return isBn ? "ইসলামিক অডিও হাব" : "Islamic Audio";
                case "action_books":
                    return isBn ? "ইসলামিক বই" : "Islamic Books";
                case "action_hadith":
                    return isBn ? "হাদিস শরিফ" : "Hadith Sharif";
                case "action_dua":
                    return isBn ? "দোয়া ভাণ্ডার" : "Dua Collection";
                case "action_amal":
                    return isBn ? "আমল ট্র্যাকার" : "Amal Tracker";
                case "action_quiz":
                    return isBn ? "কুইজ হাব" : "Quiz Hub";
                case "action_mosque":
                    return isBn ? "মসজিদ সন্ধান" : "Mosque Finder";
                case "action_safar":
                    return isBn ? "সফর মোড" : "Traveler Mode";
                case "action_jummah":
                    return isBn ? "জুম্মা মোড" : "Jummah Mode";
                case "action_eid":
                    return isBn ? "ঈদ মোড" : "Eid Mode";
                case "action_janaza":
                    return isBn ? "জানাযা গাইড" : "Janaza Guide";
                case "action_roza":
                    return isBn ? "রোজা ট্র্যাকার" : "Fasting Tracker";
                case "action_khatm":
                    return isBn ? "খতম প্ল্যানার" : "Khatam Planner";
                case "action_daily_ayah":
                    return isBn ? "আজকের আয়াত" : "Daily Verse";
                case "action_hajj":
                    return isBn ? "হজ ও উমরাহ" : "Hajj & Umrah";
                case "action_ramadan":
                    return isBn ? "রমজান মোড" : "Ramadan Mode";
                case "action_calendar":
                    return isBn ? "হিজরি ক্যালেন্ডার" : "Hijri Calendar";
                case "action_zakat":
                    return isBn ? "যাকাত ক্যালকুলেটর" : "Zakat Calculator";
                case "action_tasbih":
                    return isBn ? "তাসবিহ কাউন্টার" : "Tasbih Counter";
                case "action_azkar":
                    return isBn ? "দৈনিক আজকার" : "Daily Azkar";
                case "action_qibla":
                    return isBn ? "কিবলা কম্পাস" : "Qibla Compass";
                case "action_marriage":
                case "action_journey":
                    return isBn ? "মুসলিম বিবাহ" : "Muslim Marriage";
                case "action_faraid":
                    return isBn ? "উত্তরাধিকার বণ্টন" : "Inheritance";
                case "action_live_makkah":
                    return isBn ? "মক্কা-মদিনা লাইভ" : "Makkah Live";
                case "action_blood":
                    return isBn ? "উম্মাহ রক্তদান" : "Blood Donation";
                case "action_names":
                case "action_allah_names":
                    return isBn ? "আল্লাহর ৯৯ নাম" : "99 Names of Allah";
                case "action_six_kalima":
                    return isBn ? "৬ কালিমা" : "6 Kalimas";
                case "action_knowledge_battle":
                    return isBn ? "নলেজ ব্যাটেল" : "Knowledge Battle";
                case "action_halal_food":
                    return isBn ? "হালাল ফুড গাইড" : "Halal Food Guide";
                case "action_islamic_name":
                    return isBn ? "শিশুদের ইসলামিক নাম" : "Islamic Names";
                case "action_miracle":
                    return isBn ? "কুরআনের অলৌকিকতা" : "Quranic Miracles";
                case "action_compassion":
                    return isBn ? "দয়া ও মানবিকতা" : "Mercy & Compassion";
                default:
                    return defaultTitle;
            }
        }
    }
}
