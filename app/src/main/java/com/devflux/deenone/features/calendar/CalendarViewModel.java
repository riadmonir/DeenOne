package com.devflux.deenone.features.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.data.repository.IslamicCalendarRepository;
import com.devflux.deenone.features.calendar.model.IslamicEventItem;
import com.devflux.deenone.features.calendar.model.IslamicMonthItem;
import com.devflux.deenone.utils.HijriCalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarViewModel extends AndroidViewModel {

    private final IslamicCalendarRepository repository;
    private final MutableLiveData<HijriCalendarUtil.HijriDateResult> todayHijriLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> todayGregorianLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IslamicEventItem>> filteredEventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IslamicMonthItem>> monthsLiveData = new MutableLiveData<>();

    private String currentCategory = "all";
    private String searchQuery = "";
    private List<IslamicEventItem> allEvents = new ArrayList<>();

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        this.repository = IslamicCalendarRepository.getInstance();
        refreshCalendarData();
    }

    public void refreshCalendarData() {
        Calendar cal = Calendar.getInstance();
        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(cal);
        todayHijriLiveData.setValue(hijri);
        todayGregorianLiveData.setValue(HijriCalendarUtil.getRealGregorianDateBengali(cal));

        allEvents = repository.getSignificantIslamicEvents(cal);
        monthsLiveData.setValue(repository.get12IslamicMonths());

        applyFilters();
    }

    public LiveData<HijriCalendarUtil.HijriDateResult> getTodayHijriDate() {
        return todayHijriLiveData;
    }

    public LiveData<String> getTodayGregorianDate() {
        return todayGregorianLiveData;
    }

    public LiveData<List<IslamicEventItem>> getFilteredEvents() {
        return filteredEventsLiveData;
    }

    public LiveData<List<IslamicMonthItem>> getMonths() {
        return monthsLiveData;
    }

    public void setCategory(String category) {
        this.currentCategory = category != null ? category : "all";
        applyFilters();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query != null ? query.trim().toLowerCase() : "";
        applyFilters();
    }

    private void applyFilters() {
        List<IslamicEventItem> result = new ArrayList<>();
        for (IslamicEventItem item : allEvents) {
            boolean catMatch = currentCategory.equals("all") || item.getCategory().equalsIgnoreCase(currentCategory);
            boolean queryMatch = searchQuery.isEmpty()
                    || item.getTitle().toLowerCase().contains(searchQuery)
                    || item.getHijriDateString().toLowerCase().contains(searchQuery)
                    || item.getImportanceBadge().toLowerCase().contains(searchQuery)
                    || item.getDescription().toLowerCase().contains(searchQuery);

            if (catMatch && queryMatch) {
                result.add(item);
            }
        }
        filteredEventsLiveData.setValue(result);
    }
}
