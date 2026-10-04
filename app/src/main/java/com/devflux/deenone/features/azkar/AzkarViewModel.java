package com.devflux.deenone.features.azkar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.AzkarDao;
import com.devflux.deenone.data.local.entity.AzkarEntity;

import java.util.List;

/**
 * AzkarViewModel — Manages Azkar data for the UI.
 *
 * Features:
 *  - Load all azkar from Room DB
 *  - Filter by category (morning/evening/after_salah/etc.)
 *  - Increment count for individual azkar
 *  - Reset all counts for a category
 */
public class AzkarViewModel extends AndroidViewModel {

    private final AzkarDao azkarDao;

    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("morning");

    private final LiveData<List<AzkarEntity>> filteredAzkar;

    public AzkarViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        azkarDao = db.azkarDao();

        // Switch the LiveData source whenever category changes
        filteredAzkar = Transformations.switchMap(selectedCategory, category ->
                azkarDao.getAzkarByCategory(category)
        );
    }

    public LiveData<List<AzkarEntity>> getFilteredAzkar() {
        return filteredAzkar;
    }

    public String getSelectedCategory() {
        return selectedCategory.getValue();
    }

    public void selectCategory(String category) {
        selectedCategory.setValue(category);
    }

    public void incrementCount(AzkarEntity azkar) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                azkarDao.incrementCount(azkar.getId())
        );
    }

    public void resetCategoryCount() {
        String category = selectedCategory.getValue();
        if (category != null) {
            AppDatabase.databaseWriteExecutor.execute(() ->
                    azkarDao.resetCategoryCount(category)
            );
        }
    }

    public void resetAllCounts() {
        AppDatabase.databaseWriteExecutor.execute(azkarDao::resetAllCounts);
    }
}
