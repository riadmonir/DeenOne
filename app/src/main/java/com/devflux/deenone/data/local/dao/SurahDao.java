package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import java.util.List;

@Dao
public interface SurahDao {

    @Query("SELECT * FROM quran_surahs ORDER BY number ASC")
    LiveData<List<QuranSurahEntity>> getAllSurahs();

    @Query("SELECT * FROM quran_surahs ORDER BY number ASC")
    List<QuranSurahEntity> getAllSurahsSync();

    @Query("SELECT * FROM quran_surahs WHERE number = :surahNumber LIMIT 1")
    LiveData<QuranSurahEntity> getSurahByNumber(int surahNumber);

    @Query("SELECT * FROM quran_surahs WHERE number = :surahNumber LIMIT 1")
    QuranSurahEntity getSurahByNumberSync(int surahNumber);

    @Query("SELECT * FROM quran_surahs WHERE isFavorite = 1 ORDER BY number ASC")
    LiveData<List<QuranSurahEntity>> getFavoriteSurahs();

    @Query("SELECT * FROM quran_surahs WHERE nameBengali LIKE '%' || :query || '%' OR nameEnglish LIKE '%' || :query || '%' OR nameArabic LIKE '%' || :query || '%' OR meaningBengali LIKE '%' || :query || '%' OR number = :numberQuery ORDER BY number ASC")
    LiveData<List<QuranSurahEntity>> searchSurahs(String query, int numberQuery);

    @Query("SELECT * FROM quran_surahs WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT 1")
    LiveData<QuranSurahEntity> getLastReadSurah();

    @Query("SELECT * FROM quran_surahs WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT 1")
    QuranSurahEntity getLastReadSurahSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSurahs(List<QuranSurahEntity> surahs);

    @Update
    void updateSurah(QuranSurahEntity surah);

    @Query("UPDATE quran_surahs SET readingProgressAyah = :ayahNumber, lastReadTimestamp = :timestamp WHERE number = :surahNumber")
    void updateLastReadPosition(int surahNumber, int ayahNumber, long timestamp);

    @Query("UPDATE quran_surahs SET isFavorite = :isFavorite WHERE number = :surahNumber")
    void updateFavorite(int surahNumber, boolean isFavorite);

    @Query("SELECT COUNT(*) FROM quran_surahs")
    int getSurahCount();
}
