package com.devflux.deenone.features.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;

public class NextPrayerCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_SUNRISE_SUNSET = 0;
    public static final int TYPE_NEXT_PRAYER = 1;
    public static final int TYPE_QUIZ_HUB = 2;
    public static final int TYPE_QURAN_LEARNING = 3;

    private String sunriseTime = "6:04 am";
    private String sunsetTime = "7:12 pm";

    private String nextPrayerName = "আসর";
    private String nextPrayerTime = "5:18 pm";
    private String nextPrayerCountdown = "03:41:40";

    public interface OnCarouselClickListener {
        void onSunriseSunsetClick();
        void onNextPrayerClick();
        void onQuizHubClick();
        void onQuranLearningClick();
    }

    private final OnCarouselClickListener listener;

    public NextPrayerCarouselAdapter(OnCarouselClickListener listener) {
        this.listener = listener;
    }

    public void updateSunriseSunset(String sunrise, String sunset) {
        this.sunriseTime = sunrise != null ? sunrise : "6:04 am";
        this.sunsetTime = sunset != null ? sunset : "7:12 pm";
        notifyItemChanged(TYPE_SUNRISE_SUNSET);
    }

    public void updateNextPrayer(String name, String time, String countdown) {
        this.nextPrayerName = name != null ? name : "আসর";
        this.nextPrayerTime = time != null ? time : "5:18 pm";
        this.nextPrayerCountdown = countdown != null ? countdown : "03:41:40";
        notifyItemChanged(TYPE_NEXT_PRAYER);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_SUNRISE_SUNSET:
                View v0 = inflater.inflate(R.layout.item_carousel_sunrise_sunset, parent, false);
                return new SunriseSunsetViewHolder(v0);
            case TYPE_NEXT_PRAYER:
                View v1 = inflater.inflate(R.layout.item_carousel_next_prayer, parent, false);
                return new NextPrayerViewHolder(v1);
            case TYPE_QUIZ_HUB:
                View v2 = inflater.inflate(R.layout.item_carousel_quiz_hub, parent, false);
                return new QuizHubViewHolder(v2);
            case TYPE_QURAN_LEARNING:
            default:
                View v3 = inflater.inflate(R.layout.item_carousel_quran_learning, parent, false);
                return new QuranLearningViewHolder(v3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SunriseSunsetViewHolder) {
            ((SunriseSunsetViewHolder) holder).bind(sunriseTime, sunsetTime, listener);
        } else if (holder instanceof NextPrayerViewHolder) {
            ((NextPrayerViewHolder) holder).bind(nextPrayerName, nextPrayerTime, nextPrayerCountdown, listener);
        } else if (holder instanceof QuizHubViewHolder) {
            ((QuizHubViewHolder) holder).bind(listener);
        } else if (holder instanceof QuranLearningViewHolder) {
            ((QuranLearningViewHolder) holder).bind(listener);
        }
    }

    public static class SunriseSunsetViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSunriseLabel;
        private final TextView tvSunrise;
        private final TextView tvSunsetLabel;
        private final TextView tvSunset;

        public SunriseSunsetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSunriseLabel = itemView.findViewById(R.id.tvSunriseLabel);
            tvSunrise = itemView.findViewById(R.id.tvSunriseTime);
            tvSunsetLabel = itemView.findViewById(R.id.tvSunsetLabel);
            tvSunset = itemView.findViewById(R.id.tvSunsetTime);
        }

        public void bind(String sunrise, String sunset, OnCarouselClickListener listener) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
            if (tvSunriseLabel != null) tvSunriseLabel.setText(isBn ? "সূর্যোদয়" : "Sunrise");
            if (tvSunsetLabel != null) tvSunsetLabel.setText(isBn ? "সূর্যাস্ত" : "Sunset");
            if (tvSunrise != null) tvSunrise.setText(sunrise);
            if (tvSunset != null) tvSunset.setText(sunset);
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSunriseSunsetClick();
            });
        }
    }

    public static class NextPrayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNextPrayerHeader;
        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvCountdownLabel;
        private final TextView tvCountdown;

        public NextPrayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNextPrayerHeader = itemView.findViewById(R.id.tvNextPrayerHeader);
            tvName = itemView.findViewById(R.id.tvCarouselNextPrayerName);
            tvTime = itemView.findViewById(R.id.tvCarouselNextPrayerTime);
            tvCountdownLabel = itemView.findViewById(R.id.tvCarouselNextPrayerCountdownLabel);
            tvCountdown = itemView.findViewById(R.id.tvCarouselNextPrayerCountdown);
        }

        public void bind(String name, String time, String countdown, OnCarouselClickListener listener) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
            String localizedName = com.devflux.deenone.utils.PrayerCalculator.getWaqtName(name, isBn);
            if (tvNextPrayerHeader != null) tvNextPrayerHeader.setText(isBn ? "পরবর্তী ওয়াক্ত" : "Next Prayer");
            if (tvName != null) tvName.setText(localizedName);
            if (tvTime != null) tvTime.setText(time);
            if (tvCountdownLabel != null) {
                tvCountdownLabel.setText(isBn ? (localizedName + " শুরু হতে বাকি") : ("Time until " + localizedName));
            }
            if (tvCountdown != null) tvCountdown.setText(countdown);
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onNextPrayerClick();
            });
        }
    }

    public static class QuizHubViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvSubtitle;

        public QuizHubViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCarouselQuizTitle);
            tvSubtitle = itemView.findViewById(R.id.tvCarouselQuizSubtitle);
        }

        public void bind(OnCarouselClickListener listener) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
            if (tvTitle != null) tvTitle.setText(isBn ? "ইসলামিক কুইজ হাব" : "Islamic Quiz Hub");
            if (tvSubtitle != null) {
                tvSubtitle.setText(isBn ? "কুইজ খেলুন এবং আপনার জ্ঞান বৃদ্ধি করুন..." : "Play quizzes and expand your knowledge...");
            }
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onQuizHubClick();
            });
        }
    }

    public static class QuranLearningViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvSubtitle;

        public QuranLearningViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCarouselQuranTitle);
            tvSubtitle = itemView.findViewById(R.id.tvCarouselQuranSubtitle);
        }

        public void bind(OnCarouselClickListener listener) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
            if (tvTitle != null) tvTitle.setText(isBn ? "সহজ কুরআন শিক্ষা" : "Learn Quran Easily");
            if (tvSubtitle != null) {
                tvSubtitle.setText(isBn ? "সহীহ কুরআন তিলাওয়াত ও তাজবীদ শিখুন..." : "Learn proper Quran recitation and Tajweed...");
            }
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onQuranLearningClick();
            });
        }
    }
}