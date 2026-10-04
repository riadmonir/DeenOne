package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.HalalFoodEntity;

import java.util.List;

@Dao
public interface HalalDao {

    @Query("SELECT * FROM halal_ingredients ORDER BY code ASC")
    LiveData<List<HalalFoodEntity>> getAllIngredients();

    @Query("SELECT * FROM halal_ingredients WHERE code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    LiveData<List<HalalFoodEntity>> searchIngredients(String query);

    @Query("SELECT * FROM halal_ingredients WHERE status = :status ORDER BY code ASC")
    LiveData<List<HalalFoodEntity>> getByStatus(String status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HalalFoodEntity> list);

    @Query("SELECT COUNT(*) FROM halal_ingredients")
    int getIngredientCount();
}
