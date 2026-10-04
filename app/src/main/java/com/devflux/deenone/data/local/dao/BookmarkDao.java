package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.QuranBookmarkEntity;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Query("SELECT * FROM quran_bookmarks ORDER BY createdAt DESC")
    LiveData<List<QuranBookmarkEntity>> getAllBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmark(QuranBookmarkEntity bookmark);

    @Delete
    void deleteBookmark(QuranBookmarkEntity bookmark);

    @Query("DELETE FROM quran_bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    void removeBookmark(int surahNumber, int ayahNumber);
}
