package com.devflux.deenone.features.mosque;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.data.repository.MosqueRepository;
import com.devflux.deenone.features.mosque.model.MosqueItem;

import java.util.List;

public class MosqueViewModel extends AndroidViewModel {

    private final MosqueRepository repository;
    private final MutableLiveData<List<MosqueItem>> nearbyMosquesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> activeFilterLiveData = new MutableLiveData<>("all");
    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>("");

    private LocationProvider.Coordinates currentCoordinates;

    public MosqueViewModel(@NonNull Application application) {
        super(application);
        this.repository = MosqueRepository.getInstance();
        this.currentCoordinates = LocationProvider.getSavedOrCurrentLocation(application);
        refreshMosques();
    }

    public LiveData<List<MosqueItem>> getNearbyMosques() {
        return nearbyMosquesLiveData;
    }

    public LiveData<String> getActiveFilter() {
        return activeFilterLiveData;
    }

    public void setLocation(LocationProvider.Coordinates coordinates) {
        if (coordinates != null) {
            this.currentCoordinates = coordinates;
            refreshMosques();
        }
    }

    public LocationProvider.Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setFilter(String filterType) {
        activeFilterLiveData.setValue(filterType);
        refreshMosques();
    }

    public void setSearchQuery(String query) {
        searchQueryLiveData.setValue(query);
        refreshMosques();
    }

    public void refreshMosques() {
        if (currentCoordinates == null) {
            currentCoordinates = LocationProvider.getSavedOrCurrentLocation(getApplication());
        }
        String filter = activeFilterLiveData.getValue() != null ? activeFilterLiveData.getValue() : "all";
        String query = searchQueryLiveData.getValue() != null ? searchQueryLiveData.getValue() : "";

        // 1. Immediate local verified list
        List<MosqueItem> list = repository.getNearbyMosques(
                currentCoordinates.latitude,
                currentCoordinates.longitude,
                filter,
                query
        );
        nearbyMosquesLiveData.setValue(list);

        // 2. Async Live OSM Overpass query for real-time surroundings
        repository.fetchLiveNearbyMosquesAsync(
                currentCoordinates.latitude,
                currentCoordinates.longitude,
                filter,
                query,
                liveList -> {
                    if (liveList != null && !liveList.isEmpty()) {
                        nearbyMosquesLiveData.setValue(liveList);
                    }
                }
        );
    }

    public void launchDirections(Context context, MosqueItem mosque, boolean isWalking) {
        repository.launchDirections(context, mosque, isWalking);
    }

    public void launchMapSearch(Context context) {
        if (currentCoordinates == null) {
            currentCoordinates = LocationProvider.getSavedOrCurrentLocation(getApplication());
        }
        repository.launchMapSearch(context, currentCoordinates.latitude, currentCoordinates.longitude);
    }
}
