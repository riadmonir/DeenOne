package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.IslamicBookEntity;

import java.util.List;

@Dao
public interface IslamicBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(IslamicBookEntity book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IslamicBookEntity> books);

    @Update
    void update(IslamicBookEntity book);

    @Query("SELECT * FROM islamic_books ORDER BY addedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getAllBooks();

    @Query("SELECT * FROM islamic_books WHERE category = :category ORDER BY addedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getBooksByCategory(String category);

    @Query("SELECT * FROM islamic_books WHERE language = :language ORDER BY addedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getBooksByLanguage(String language);

    @Query("SELECT * FROM islamic_books WHERE isDownloaded = 1 ORDER BY addedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getDownloadedBooks();

    @Query("SELECT * FROM islamic_books WHERE isFavorite = 1 ORDER BY addedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getFavoriteBooks();

    @Query("SELECT * FROM islamic_books WHERE lastReadPage > 0 ORDER BY lastOpenedTimestamp DESC")
    LiveData<List<IslamicBookEntity>> getContinueReadingBooks();

    @Query("SELECT * FROM islamic_books WHERE id = :id LIMIT 1")
    LiveData<IslamicBookEntity> getBookById(String id);

    @Query("SELECT * FROM islamic_books WHERE id = :id LIMIT 1")
    IslamicBookEntity getBookByIdSync(String id);

    @Query("SELECT * FROM islamic_books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<IslamicBookEntity>> searchBooks(String query);

    @Query("UPDATE islamic_books SET downloadProgress = :progress, isDownloaded = :isDownloaded, localFilePath = :localPath WHERE id = :id")
    void updateDownloadStatus(String id, int progress, boolean isDownloaded, String localPath);

    @Query("UPDATE islamic_books SET lastReadPage = :page, readingPercentage = :percentage, readingStatus = :status, lastOpenedTimestamp = :timestamp WHERE id = :id")
    void updateReadingProgressDetailed(String id, int page, int percentage, String status, long timestamp);

    @Query("UPDATE islamic_books SET lastReadPage = :page WHERE id = :id")
    void updateLastReadPage(String id, int page);

    @Query("UPDATE islamic_books SET isFavorite = :isFav WHERE id = :id")
    void updateFavorite(String id, boolean isFav);

    @Query("SELECT COUNT(*) FROM islamic_books")
    int getBookCount();
}
