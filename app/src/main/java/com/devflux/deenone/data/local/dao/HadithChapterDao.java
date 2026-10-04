package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.HadithChapterEntity;

import java.util.List;

@Dao
public interface HadithChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HadithChapterEntity chapter);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HadithChapterEntity> chapters);

    @Update
    void update(HadithChapterEntity chapter);

    @Query("SELECT * FROM hadith_chapters_table WHERE bookSlug = :bookSlug AND isActive = 1 ORDER BY displayOrder ASC, chapterNumber ASC")
    LiveData<List<HadithChapterEntity>> getChaptersByBook(String bookSlug);

    @Query("SELECT * FROM hadith_chapters_table WHERE bookSlug = :bookSlug AND isActive = 1 ORDER BY displayOrder ASC, chapterNumber ASC")
    List<HadithChapterEntity> getChaptersByBookSync(String bookSlug);

    @Query("SELECT * FROM hadith_chapters_table WHERE bookSlug = :bookSlug AND isActive = 1 AND (titleBn LIKE '%' || :query || '%' OR titleEn LIKE '%' || :query || '%' OR chapterNumberBn LIKE '%' || :query || '%') ORDER BY displayOrder ASC, chapterNumber ASC")
    LiveData<List<HadithChapterEntity>> searchChaptersInBook(String bookSlug, String query);

    @Query("SELECT * FROM hadith_chapters_table WHERE bookSlug = :bookSlug AND chapterNumber = :chapterNumber LIMIT 1")
    HadithChapterEntity getChapterByNumber(String bookSlug, int chapterNumber);

    @Query("SELECT COUNT(*) FROM hadith_chapters_table WHERE bookSlug = :bookSlug AND isActive = 1")
    int getChapterCount(String bookSlug);

    @Query("DELETE FROM hadith_chapters_table WHERE bookSlug = :bookSlug")
    void deleteByBook(String bookSlug);
}
