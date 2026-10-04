package com.devflux.deenone.features.quiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devflux.deenone.data.local.entity.QuizQuestionEntity;
import com.devflux.deenone.data.local.entity.QuizResultEntity;
import com.devflux.deenone.data.repository.QuizRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizViewModel extends AndroidViewModel {

    private final QuizRepository repository;

    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("all");
    private final MutableLiveData<Integer> selectedDifficulty = new MutableLiveData<>(0); // 0=All, 1=Beginner, 2=Intermediate, 3=Advanced
    private final MutableLiveData<Boolean> isDailyQuiz = new MutableLiveData<>(false);

    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> currentScore = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isQuizFinished = new MutableLiveData<>(false);

    private final LiveData<List<QuizQuestionEntity>> questionsLiveData;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        this.repository = new QuizRepository(application);

        this.questionsLiveData = Transformations.switchMap(selectedCategory, cat -> {
            Integer diff = selectedDifficulty.getValue();
            int diffVal = (diff != null) ? diff : 0;
            if (Boolean.TRUE.equals(isDailyQuiz.getValue())) {
                return repository.getDailyQuiz();
            } else if (diffVal > 0) {
                return repository.getQuestionsByDifficulty(diffVal, 10);
            } else {
                return repository.getQuestionsByCategory(cat, 10);
            }
        });
    }

    public LiveData<List<QuizQuestionEntity>> getQuestions() {
        return questionsLiveData;
    }

    public LiveData<Integer> getCurrentIndex() {
        return currentIndex;
    }

    public LiveData<Integer> getCurrentScore() {
        return currentScore;
    }

    public LiveData<Boolean> getIsQuizFinished() {
        return isQuizFinished;
    }

    public LiveData<List<QuizResultEntity>> getQuizHistory() {
        return repository.getQuizHistory();
    }

    public void setCategory(String category) {
        isDailyQuiz.setValue(false);
        selectedCategory.setValue(category);
        resetSession();
    }

    public void setDifficulty(int difficulty) {
        isDailyQuiz.setValue(false);
        selectedDifficulty.setValue(difficulty);
        selectedCategory.setValue(selectedCategory.getValue()); // trigger reload
        resetSession();
    }

    public void startDailyQuiz() {
        isDailyQuiz.setValue(true);
        selectedCategory.setValue("all");
        resetSession();
    }

    public void resetSession() {
        currentIndex.setValue(0);
        currentScore.setValue(0);
        isQuizFinished.setValue(false);
    }

    public void answerQuestion(boolean isCorrect) {
        if (isCorrect) {
            Integer s = currentScore.getValue();
            currentScore.setValue((s != null ? s : 0) + 10);
        }
    }

    public void nextQuestion(int totalQuestions) {
        Integer cur = currentIndex.getValue();
        int next = (cur != null ? cur : 0) + 1;
        if (next >= totalQuestions) {
            isQuizFinished.setValue(true);
            saveResult(totalQuestions);
        } else {
            currentIndex.setValue(next);
        }
    }

    private void saveResult(int totalQuestions) {
        int finalScore = currentScore.getValue() != null ? currentScore.getValue() : 0;
        String cat = selectedCategory.getValue() != null ? selectedCategory.getValue() : "General";
        int diff = selectedDifficulty.getValue() != null ? selectedDifficulty.getValue() : 1;
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        boolean isDaily = Boolean.TRUE.equals(isDailyQuiz.getValue());

        QuizResultEntity result = new QuizResultEntity(
                finalScore, totalQuestions, cat, diff,
                System.currentTimeMillis(), todayDate, isDaily
        );
        repository.saveQuizResult(result);
    }
}
