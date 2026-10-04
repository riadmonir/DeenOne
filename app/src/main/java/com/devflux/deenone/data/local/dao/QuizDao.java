package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.QuizQuestionEntity;
import com.devflux.deenone.data.local.entity.QuizResultEntity;

import java.util.List;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM quiz_questions ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<QuizQuestionEntity>> getRandomQuestions(int limit);

    @Query("SELECT * FROM quiz_questions WHERE category = :category ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<QuizQuestionEntity>> getQuestionsByCategory(String category, int limit);

    @Query("SELECT * FROM quiz_questions WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<QuizQuestionEntity>> getQuestionsByDifficulty(int difficulty, int limit);

    @Query("SELECT * FROM quiz_questions WHERE category = :category AND difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<QuizQuestionEntity>> getQuestionsByCategoryAndDifficulty(String category, int difficulty, int limit);

    @Query("SELECT * FROM quiz_questions ORDER BY RANDOM() LIMIT 10")
    LiveData<List<QuizQuestionEntity>> getDailyQuiz();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizQuestionEntity> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertResult(QuizResultEntity result);

    @Query("SELECT * FROM quiz_results ORDER BY completedTimestamp DESC LIMIT 20")
    LiveData<List<QuizResultEntity>> getQuizHistory();

    @Query("SELECT COUNT(*) FROM quiz_questions")
    int getQuestionCount();

    @Query("SELECT SUM(score) FROM quiz_results")
    LiveData<Integer> getTotalScore();

    @Query("SELECT COUNT(*) FROM quiz_results WHERE score > 0")
    LiveData<Integer> getTotalQuizzesPlayedCount();

    @Query("SELECT COUNT(*) FROM quiz_results WHERE score > 0")
    int getTotalQuizzesPlayedCountSync();

    @Query("SELECT COUNT(*) FROM quiz_results WHERE dateString = :dateString AND score > 0")
    LiveData<Integer> getDailyQuizzesPlayedCount(String dateString);

    @Query("SELECT COUNT(*) FROM quiz_results WHERE dateString = :dateString AND score > 0")
    int getDailyQuizzesPlayedCountSync(String dateString);

    @Query("SELECT COALESCE(SUM(score), 0) FROM quiz_results WHERE dateString = :dateString")
    LiveData<Integer> getEarnedPointsForDate(String dateString);

    @Query("SELECT COALESCE(SUM(score), 0) FROM quiz_results WHERE dateString = :dateString")
    int getEarnedPointsForDateSync(String dateString);

    @Query("DELETE FROM quiz_results")
    void deleteAllResultsSync();

    @Query("SELECT * FROM quiz_questions")
    List<QuizQuestionEntity> getAllQuestionsSync();

    @Query("SELECT * FROM quiz_questions WHERE category = :category")
    List<QuizQuestionEntity> getQuestionsByCategorySync(String category);

    @Query("SELECT * FROM quiz_questions WHERE question = :questionText LIMIT 1")
    QuizQuestionEntity findByQuestionText(String questionText);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOrIgnore(List<QuizQuestionEntity> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(QuizQuestionEntity question);

    @Query("DELETE FROM quiz_questions WHERE id = :id")
    void deleteQuestionById(long id);
}
