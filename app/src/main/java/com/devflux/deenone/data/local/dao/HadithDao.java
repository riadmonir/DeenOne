package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.HadithEntity;

import java.util.List;

@Dao
public interface HadithDao {

    @Query("SELECT * FROM hadith_table ORDER BY id ASC")
    LiveData<List<HadithEntity>> getAllHadith();

    @Query("SELECT * FROM hadith_table WHERE (collectionId = :collectionId OR (:collectionId = 'abu_dawood' AND collectionId = 'abudawud') OR (:collectionId = 'abudawud' AND collectionId = 'abu_dawood') OR (:collectionId = 'nawawi_40' AND collectionId = 'nawawi40') OR (:collectionId = 'nawawi40' AND collectionId = 'nawawi_40')) ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> getHadithByCollection(String collectionId);

    @Query("SELECT * FROM hadith_table WHERE (collectionId = :collectionId OR (:collectionId = 'abu_dawood' AND collectionId = 'abudawud') OR (:collectionId = 'abudawud' AND collectionId = 'abu_dawood') OR (:collectionId = 'nawawi_40' AND collectionId = 'nawawi40') OR (:collectionId = 'nawawi40' AND collectionId = 'nawawi_40')) AND (arabicText LIKE '%' || :query || '%' OR banglaTranslation LIKE '%' || :query || '%' OR englishTranslation LIKE '%' || :query || '%' OR chapterTitle LIKE '%' || :query || '%' OR topicCategory LIKE '%' || :query || '%') ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> searchHadithInCollection(String collectionId, String query);

    @Query("SELECT * FROM hadith_table WHERE arabicText LIKE '%' || :query || '%' OR banglaTranslation LIKE '%' || :query || '%' OR englishTranslation LIKE '%' || :query || '%' OR chapterTitle LIKE '%' || :query || '%' OR topicCategory LIKE '%' || :query || '%'")
    LiveData<List<HadithEntity>> searchHadith(String query);

    @Query("SELECT * FROM hadith_table WHERE isBookmarked = 1 ORDER BY id DESC")
    LiveData<List<HadithEntity>> getBookmarkedHadith();

    @Query("SELECT * FROM hadith_table ORDER BY RANDOM() LIMIT 1")
    LiveData<HadithEntity> getRandomHadith();

    @Query("SELECT COUNT(*) FROM hadith_table")
    int getHadithCount();

    @Query("SELECT COUNT(*) FROM hadith_table")
    LiveData<Integer> getHadithCountLive();

    class CollectionCount {
        public String collectionId;
        public int count;
    }

    @Query("SELECT collectionId, COUNT(*) as count FROM hadith_table GROUP BY collectionId")
    List<CollectionCount> getAllCollectionCounts();

    @Query("SELECT COUNT(*) FROM hadith_table WHERE collectionId = :collectionId OR (:collectionId = 'abu_dawood' AND collectionId = 'abudawud') OR (:collectionId = 'abudawud' AND collectionId = 'abu_dawood') OR (:collectionId = 'nawawi_40' AND collectionId = 'nawawi40') OR (:collectionId = 'nawawi40' AND collectionId = 'nawawi_40')")
    int getHadithCountForCollection(String collectionId);

    @Query("SELECT DISTINCT hadithNumber FROM hadith_table WHERE collectionId = :collectionId")
    List<Integer> getExistingNumbersForCollection(String collectionId);

    @Query("SELECT * FROM hadith_table WHERE topicCategory = :topic ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> getHadithByTopic(String topic);

    @Query("SELECT * FROM hadith_table WHERE collectionId = :collectionId AND topicCategory = :topic ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> getHadithByCollectionAndTopic(String collectionId, String topic);

    @Query("SELECT * FROM hadith_table WHERE (collectionId = :collectionId OR (:collectionId = 'abu_dawood' AND collectionId = 'abudawud') OR (:collectionId = 'abudawud' AND collectionId = 'abu_dawood') OR (:collectionId = 'nawawi_40' AND collectionId = 'nawawi40') OR (:collectionId = 'nawawi40' AND collectionId = 'nawawi_40')) AND hadithNumber >= :startHadith AND hadithNumber <= :endHadith ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> getHadithByChapterRange(String collectionId, int startHadith, int endHadith);

    @Query("SELECT * FROM hadith_table WHERE (collectionId = :collectionId OR (:collectionId = 'abu_dawood' AND collectionId = 'abudawud') OR (:collectionId = 'abudawud' AND collectionId = 'abu_dawood') OR (:collectionId = 'nawawi_40' AND collectionId = 'nawawi40') OR (:collectionId = 'nawawi40' AND collectionId = 'nawawi_40')) AND hadithNumber >= :startHadith AND hadithNumber <= :endHadith AND (arabicText LIKE '%' || :query || '%' OR banglaTranslation LIKE '%' || :query || '%' OR englishTranslation LIKE '%' || :query || '%' OR chapterTitle LIKE '%' || :query || '%' OR topicCategory LIKE '%' || :query || '%') ORDER BY hadithNumber ASC")
    LiveData<List<HadithEntity>> searchHadithInChapterRange(String collectionId, int startHadith, int endHadith, String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HadithEntity> hadithList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(HadithEntity hadith);

    @Update
    void update(HadithEntity hadith);

    @Query("UPDATE hadith_table SET isBookmarked = :bookmarked WHERE id = :id")
    void updateBookmarkStatus(long id, boolean bookmarked);
}
