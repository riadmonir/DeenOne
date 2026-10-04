package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.FeatureWidgetEntity;

import java.util.List;

@Dao
public interface FeatureWidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(FeatureWidgetEntity feature);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FeatureWidgetEntity> features);

    @Query("SELECT * FROM feature_widgets WHERE isEnabled = 1 ORDER BY displayOrder ASC")
    LiveData<List<FeatureWidgetEntity>> getEnabledFeaturesLiveData();

    @Query("SELECT * FROM feature_widgets WHERE isEnabled = 1 ORDER BY displayOrder ASC")
    List<FeatureWidgetEntity> getEnabledFeaturesSync();

    @Query("SELECT * FROM feature_widgets ORDER BY displayOrder ASC")
    LiveData<List<FeatureWidgetEntity>> getAllFeaturesLiveData();

    @Query("SELECT * FROM feature_widgets WHERE featureId = :featureId LIMIT 1")
    FeatureWidgetEntity getFeatureById(String featureId);

    @Query("SELECT COUNT(*) FROM feature_widgets")
    int getFeatureCount();

    @Query("DELETE FROM feature_widgets WHERE featureId = :featureId")
    void deleteFeature(String featureId);

    @Query("UPDATE feature_widgets SET isEnabled = :isEnabled WHERE featureId = :featureId")
    void updateFeatureStatus(String featureId, boolean isEnabled);

    @Query("UPDATE feature_widgets SET displayOrder = :newOrder WHERE featureId = :featureId")
    void updateDisplayOrder(String featureId, int newOrder);
}
