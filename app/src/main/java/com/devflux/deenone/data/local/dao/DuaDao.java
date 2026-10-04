package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.DuaEntity;

import java.util.List;

@Dao
public interface DuaDao {

    @Query("SELECT * FROM duas ORDER BY id ASC")
    LiveData<List<DuaEntity>> getAllDuas();

    @Query("SELECT * FROM duas WHERE category = :category ORDER BY id ASC")
    LiveData<List<DuaEntity>> getDuasByCategory(String category);

    @Query("SELECT * FROM duas WHERE isFavorite = 1 ORDER BY id DESC")
    LiveData<List<DuaEntity>> getFavoriteDuas();

    @Query("SELECT * FROM duas WHERE title LIKE '%' || :query || '%' OR bengaliMeaning LIKE '%' || :query || '%' OR englishMeaning LIKE '%' || :query || '%' OR transliteration LIKE '%' || :query || '%' OR arabic LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    LiveData<List<DuaEntity>> searchDuas(String query);

    @Query("UPDATE duas SET isFavorite = :isFav WHERE id = :id")
    void toggleFavorite(long id, boolean isFav);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DuaEntity> duas);

    @Update
    void updateDua(DuaEntity dua);

    @Query("SELECT COUNT(*) FROM duas")
    int getDuaCount();

    @Query("SELECT COUNT(*) FROM duas")
    LiveData<Integer> getDuaCountLive();

    @Query("SELECT DISTINCT arabic FROM duas WHERE arabic != ''")
    List<String> getAllExistingArabicTexts();

    @Query("SELECT DISTINCT title FROM duas")
    List<String> getAllExistingTitles();

    @Query("SELECT DISTINCT reference FROM duas WHERE reference != ''")
    List<String> getAllExistingReferences();

    @Query("DELETE FROM duas WHERE (transliteration IS NULL OR transliteration = '') AND (bengaliMeaning LIKE '%পরিচ্ছেদঃ%' OR title LIKE 'প্রামাণ্য মাসনুন দোয়া (%' OR reference LIKE '%কিতাবুদ দাওয়াত%' OR reference LIKE '%কিতাবুয যিকির%')")
    int cleanRawNarrativeDumps();
}
