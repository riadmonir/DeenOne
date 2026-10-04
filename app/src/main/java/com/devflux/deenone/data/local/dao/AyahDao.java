package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.QuranAyahEntity;

import java.util.List;

@Dao
public interface AyahDao {

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    LiveData<List<QuranAyahEntity>> getAyahsForSurah(int surahNumber);

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    List<QuranAyahEntity> getAyahsForSurahSync(int surahNumber);

    @Query("SELECT * FROM quran_ayahs WHERE isBookmarked = 1 ORDER BY surahNumber ASC, ayahNumber ASC")
    LiveData<List<QuranAyahEntity>> getBookmarkedAyahs();

    @Query("SELECT * FROM quran_ayahs WHERE isFavorite = 1 ORDER BY surahNumber ASC, ayahNumber ASC")
    LiveData<List<QuranAyahEntity>> getFavoriteAyahs();

    @Query("SELECT * FROM quran_ayahs WHERE translationBengali LIKE '%' || :query || '%' OR translationEnglish LIKE '%' || :query || '%' OR textArabic LIKE '%' || :query || '%' LIMIT 100")
    LiveData<List<QuranAyahEntity>> searchAyahs(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAyahs(List<QuranAyahEntity> ayahs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAyah(QuranAyahEntity ayah);

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber LIMIT 1")
    QuranAyahEntity getAyahSync(int surahNumber, int ayahNumber);

    @Query("UPDATE quran_ayahs SET isBookmarked = :isBookmarked WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    void setAyahBookmarked(int surahNumber, int ayahNumber, boolean isBookmarked);

    @Query("UPDATE quran_ayahs SET isFavorite = :isFavorite WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    void setAyahFavorite(int surahNumber, int ayahNumber, boolean isFavorite);

    @Query("SELECT * FROM quran_ayahs WHERE pageNumber = :pageNumber ORDER BY surahNumber ASC, ayahNumber ASC")
    LiveData<List<QuranAyahEntity>> getAyahsForPage(int pageNumber);

    @Query("SELECT * FROM quran_ayahs WHERE pageNumber = :pageNumber ORDER BY surahNumber ASC, ayahNumber ASC")
    List<QuranAyahEntity> getAyahsForPageSync(int pageNumber);

    @Query("SELECT * FROM quran_ayahs WHERE juzNumber = :juzNumber ORDER BY surahNumber ASC, ayahNumber ASC")
    LiveData<List<QuranAyahEntity>> getAyahsForJuz(int juzNumber);

    @Query("SELECT * FROM quran_ayahs WHERE juzNumber = :juzNumber ORDER BY surahNumber ASC, ayahNumber ASC")
    List<QuranAyahEntity> getAyahsForJuzSync(int juzNumber);

    @Query("SELECT * FROM quran_ayahs WHERE (surahNumber > :startSurah OR (surahNumber = :startSurah AND ayahNumber >= :startAyah)) AND (surahNumber < :endSurah OR (surahNumber = :endSurah AND ayahNumber <= :endAyah)) ORDER BY surahNumber ASC, ayahNumber ASC")
    List<QuranAyahEntity> getAyahsInRangeSync(int startSurah, int startAyah, int endSurah, int endAyah);

    @Query("SELECT COUNT(*) FROM quran_ayahs")
    int getTotalAyahCount();
}
