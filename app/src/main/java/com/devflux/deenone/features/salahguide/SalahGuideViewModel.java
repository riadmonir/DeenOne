package com.devflux.deenone.features.salahguide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devflux.deenone.data.repository.SalahGuideRepository;
import com.devflux.deenone.features.salahguide.model.SalahTopicItem;

import java.util.ArrayList;
import java.util.List;

public class SalahGuideViewModel extends ViewModel {

    private final SalahGuideRepository repository;
    private final MutableLiveData<List<SalahTopicItem>> filteredTopicsLiveData = new MutableLiveData<>();
    private final MutableLiveData<SalahTopicItem> selectedTopicLiveData = new MutableLiveData<>();

    private String currentCategory = "all";
    private String currentQuery = "";
    private List<SalahTopicItem> allTopics = new ArrayList<>();

    public SalahGuideViewModel() {
        this.repository = SalahGuideRepository.getInstance();
        loadTopics();
    }

    public void loadTopics() {
        loadTopics(true);
    }

    public void loadTopics(boolean isBn) {
        allTopics = repository.getAllTopics(isBn);
        if (!allTopics.isEmpty()) {
            selectedTopicLiveData.setValue(allTopics.get(0));
        }
        applyFilters();
    }

    public LiveData<List<SalahTopicItem>> getFilteredTopics() {
        return filteredTopicsLiveData;
    }

    public LiveData<SalahTopicItem> getSelectedTopic() {
        return selectedTopicLiveData;
    }

    public void selectTopic(SalahTopicItem item) {
        selectedTopicLiveData.setValue(item);
    }

    public void selectTopicById(String id) {
        for (SalahTopicItem item : allTopics) {
            if (item.getId().equalsIgnoreCase(id)) {
                selectedTopicLiveData.setValue(item);
                break;
            }
        }
    }

    public void setCategoryFilter(String category) {
        this.currentCategory = category != null ? category : "all";
        applyFilters();
    }

    public void setSearchQuery(String query) {
        this.currentQuery = query != null ? query.trim().toLowerCase() : "";
        applyFilters();
    }

    private void applyFilters() {
        List<SalahTopicItem> result = new ArrayList<>();
        for (SalahTopicItem item : allTopics) {
            boolean categoryMatch = currentCategory.equals("all") || item.getCategory().equalsIgnoreCase(currentCategory);
            boolean queryMatch = currentQuery.isEmpty()
                    || item.getTitle().toLowerCase().contains(currentQuery)
                    || item.getSubtitle().toLowerCase().contains(currentQuery)
                    || item.getRulingType().toLowerCase().contains(currentQuery)
                    || item.getRakahBreakdown().toLowerCase().contains(currentQuery);

            if (categoryMatch && queryMatch) {
                result.add(item);
            }
        }
        filteredTopicsLiveData.setValue(result);
        if (!result.isEmpty() && (selectedTopicLiveData.getValue() == null || !result.contains(selectedTopicLiveData.getValue()))) {
            selectedTopicLiveData.setValue(result.get(0));
        }
    }
}
