package com.devflux.deenone.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenWidgetAdapter extends RecyclerView.Adapter<HomeScreenWidgetAdapter.WidgetViewHolder> {

    public interface OnWidgetAddListener {
        void onAddWidget(HomeScreenWidgetItem item);
    }

    private final Context context;
    private final List<HomeScreenWidgetItem> items = new ArrayList<>();
    private final OnWidgetAddListener listener;
    private final WidgetDataHelper.WidgetPrayerData sampleData;
    private final boolean isBn;

    public HomeScreenWidgetAdapter(Context context, OnWidgetAddListener listener) {
        this.context = context;
        this.listener = listener;
        this.sampleData = WidgetDataHelper.getWidgetData(context);
        this.isBn = LocaleManager.isBengali(context);
    }

    public void setItems(List<HomeScreenWidgetItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WidgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_screen_widget_card, parent, false);
        return new WidgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetViewHolder holder, int position) {
        HomeScreenWidgetItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class WidgetViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvSizeBadge;
        private final TextView tvDescription;
        private final TextView tvFreeBadge;
        private final FrameLayout framePreviewHolder;
        private final MaterialButton btnAddWidgetToHome;

        public WidgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvWidgetTitle);
            tvSizeBadge = itemView.findViewById(R.id.tvWidgetSizeBadge);
            tvDescription = itemView.findViewById(R.id.tvWidgetDescription);
            tvFreeBadge = itemView.findViewById(R.id.tvWidgetFreeBadge);
            framePreviewHolder = itemView.findViewById(R.id.frameWidgetPreviewHolder);
            btnAddWidgetToHome = itemView.findViewById(R.id.btnAddWidgetToHome);

            TouchAnimationUtil.attachTouchSpring(btnAddWidgetToHome);
        }

        public void bind(HomeScreenWidgetItem item) {
            tvTitle.setText(item.getTitle(isBn));
            tvSizeBadge.setText(item.getSizeBadge(isBn));
            tvDescription.setText(item.getDescription(isBn));

            if (tvFreeBadge != null) {
                tvFreeBadge.setText(isBn ? "১০০% ফ্রি" : "100% Free");
            }

            btnAddWidgetToHome.setText(isBn ? "হোমস্ক্রিনে যুক্ত করুন" : "Add to Home Screen");

            btnAddWidgetToHome.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddWidget(item);
                }
            });

            // Inflate preview view dynamically
            framePreviewHolder.removeAllViews();
            try {
                View previewView = LayoutInflater.from(context).inflate(item.layoutResId, framePreviewHolder, false);
                populatePreviewData(previewView, item);
                framePreviewHolder.addView(previewView);
            } catch (Exception ignored) {}
        }

        private void populatePreviewData(View view, HomeScreenWidgetItem item) {
            if (view == null || sampleData == null) return;

            // Common text views
            setTextIfFound(view, R.id.tvWidgetHijriDate, sampleData.hijriDateShort);
            setTextIfFound(view, R.id.tvWidgetGregorianDate, isBn ? sampleData.gregorianDateBengali : sampleData.gregorianDateShort);
            setTextIfFound(view, R.id.tvWidgetCurrentWaqt, sampleData.currentWaqtName);
            setTextIfFound(view, R.id.tvWidgetCurrentWaqtTime, sampleData.updatedTimeStr);
            setTextIfFound(view, R.id.tvWidgetWaqtName, sampleData.currentWaqtName);
            setTextIfFound(view, R.id.tvWidgetWaqtRange, sampleData.currentWaqtRange);
            setTextIfFound(view, R.id.tvWidgetBigCountdown, sampleData.currentWaqtCountdown);
            setTextIfFound(view, R.id.tvWidgetEndsAt, (isBn ? "শেষ সময়: " : "Ends at: ") + sampleData.currentWaqtEndTimeStr);
            setTextIfFound(view, R.id.tvWidgetCountdownTitle, sampleData.currentWaqtName + (isBn ? " ওয়াক্ত শেষ হতে বাকি" : " remaining"));
            setTextIfFound(view, R.id.tvWidgetTimeSmall, sampleData.updatedTimeStr);
            setTextIfFound(view, R.id.tvWidgetRemainingBadge, (isBn ? "বাকি " : "Left ") + sampleData.nextWaqtCountdown);
            setTextIfFound(view, R.id.tvWidgetCountdownBadge, (isBn ? "বাকি " : "Left ") + sampleData.nextWaqtCountdown);
            setTextIfFound(view, R.id.tvWidgetNextPrayerCountdown, (isBn ? "পরবর্তী ওয়াক্ত: " : "Next: ") + sampleData.nextWaqtName + " (" + sampleData.nextWaqtCountdown + (isBn ? " বাকি)" : " left)"));

            setTextIfFound(view, R.id.tvWidgetSunrise, (isBn ? "সূর্যোদয়: " : "Sunrise: ") + sampleData.sunriseTimeStr);
            setTextIfFound(view, R.id.tvWidgetSunset, (isBn ? "সূর্যাস্ত: " : "Sunset: ") + sampleData.sunsetTimeStr);
            setTextIfFound(view, R.id.tvWidgetSahri, (isBn ? "সাহরি: " : "Sahri: ") + sampleData.sehriTimeStr);
            setTextIfFound(view, R.id.tvWidgetIftar, (isBn ? "ইফতার: " : "Iftar: ") + sampleData.iftarTimeStr);

            setTextIfFound(view, R.id.tvWidgetIftarCountdown, sampleData.iftarCountdownStr);
            setTextIfFound(view, R.id.tvWidgetIftarEndTime, (isBn ? "ইফতার: " : "Iftar: ") + sampleData.iftarEndTimeStr);
            setTextIfFound(view, R.id.tvWidgetSahriCountdown, sampleData.sehriCountdownStr);
            setTextIfFound(view, R.id.tvWidgetSahriStartTime, (isBn ? "শেষ সময়: " : "Ends at: ") + sampleData.sehriStartTimeStr);

            setTextIfFound(view, R.id.tvWaqtFajrRange, sampleData.fajrRangeStr);
            setTextIfFound(view, R.id.tvWaqtDhuhrRange, sampleData.dhuhrRangeStr);
            setTextIfFound(view, R.id.tvWaqtAsrRange, sampleData.asrRangeStr);
            setTextIfFound(view, R.id.tvWaqtMaghribRange, sampleData.maghribRangeStr);
            setTextIfFound(view, R.id.tvWaqtIshaRange, sampleData.ishaRangeStr);

            setTextIfFound(view, R.id.tvWaqtFajr, sampleData.fajrTimeStr.replace(" AM", "").replace(" PM", ""));
            setTextIfFound(view, R.id.tvWaqtDhuhr, sampleData.dhuhrTimeStr.replace(" AM", "").replace(" PM", ""));
            setTextIfFound(view, R.id.tvWaqtAsr, sampleData.asrTimeStr.replace(" AM", "").replace(" PM", ""));
            setTextIfFound(view, R.id.tvWaqtMaghrib, sampleData.maghribTimeStr.replace(" AM", "").replace(" PM", ""));
            setTextIfFound(view, R.id.tvWaqtIsha, sampleData.ishaTimeStr.replace(" AM", "").replace(" PM", ""));

            setTextIfFound(view, R.id.tvWidgetAyahArabic, sampleData.dailyAyahArabic);
            setTextIfFound(view, R.id.tvWidgetAyahBengali, sampleData.dailyAyahBengali);
            setTextIfFound(view, R.id.tvWidgetAyahReference, sampleData.dailyAyahReference);

            setTextIfFound(view, R.id.tvWidgetHadithBengali, sampleData.dailyHadithBengali);
            setTextIfFound(view, R.id.tvWidgetHadithReference, sampleData.dailyHadithReference);

            setTextIfFound(view, R.id.tvWidgetZikrName, sampleData.tasbihZikrName);
            setTextIfFound(view, R.id.tvWidgetTasbihCount, String.valueOf(sampleData.tasbihCount));

            setTextIfFound(view, R.id.tvWidgetHijriMonth, sampleData.hijriMonth);
            setTextIfFound(view, R.id.tvWidgetHijriDay, sampleData.hijriDay);
            setTextIfFound(view, R.id.tvWidgetGregorianSmall, sampleData.gregorianDayOfWeek + " • " + (isBn ? sampleData.gregorianDateBengali : sampleData.gregorianDateShort));
        }

        private void setTextIfFound(View parent, int resId, String text) {
            if (parent == null || text == null) return;
            View v = parent.findViewById(resId);
            if (v instanceof TextView) {
                ((TextView) v).setText(text);
            }
        }
    }
}
