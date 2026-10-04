package com.devflux.deenone.features.quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizSessionResult implements Serializable {

    public static class QuestionReviewItem implements Serializable {
        private final String questionBn;
        private final String questionEn;
        private final String correctAnswerBn;
        private final String correctAnswerEn;
        private final String userAnswerBn;
        private final String userAnswerEn;
        private final boolean isCorrect;
        private final String explanationBn;
        private final String explanationEn;

        public QuestionReviewItem(String questionBn, String questionEn,
                                  String correctAnswerBn, String correctAnswerEn,
                                  String userAnswerBn, String userAnswerEn,
                                  boolean isCorrect,
                                  String explanationBn, String explanationEn) {
            this.questionBn = questionBn;
            this.questionEn = questionEn;
            this.correctAnswerBn = correctAnswerBn;
            this.correctAnswerEn = correctAnswerEn;
            this.userAnswerBn = userAnswerBn;
            this.userAnswerEn = userAnswerEn;
            this.isCorrect = isCorrect;
            this.explanationBn = explanationBn;
            this.explanationEn = explanationEn;
        }

        public String getQuestionBn() { return questionBn; }
        public String getQuestionEn() { return questionEn; }
        public String getCorrectAnswerBn() { return correctAnswerBn; }
        public String getCorrectAnswerEn() { return correctAnswerEn; }
        public String getUserAnswerBn() { return userAnswerBn; }
        public String getUserAnswerEn() { return userAnswerEn; }
        public boolean isCorrect() { return isCorrect; }
        public String getExplanationBn() { return explanationBn; }
        public String getExplanationEn() { return explanationEn; }
    }

    private final String categoryId;
    private final String categoryTitleBn;
    private final String categoryTitleEn;
    private final int correctCount;
    private final int totalQuestions;
    private final int pointsEarned;
    private final int accuracyPercentage;
    private final long completedTimestamp;
    private final List<QuestionReviewItem> reviewItems;

    public QuizSessionResult(String categoryId, String categoryTitleBn, String categoryTitleEn,
                             int correctCount, int totalQuestions, int pointsEarned,
                             long completedTimestamp, List<QuestionReviewItem> reviewItems) {
        this.categoryId = categoryId;
        this.categoryTitleBn = categoryTitleBn;
        this.categoryTitleEn = categoryTitleEn;
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.pointsEarned = pointsEarned;
        this.accuracyPercentage = totalQuestions > 0 ? (int) Math.round(((double) correctCount / totalQuestions) * 100) : 0;
        this.completedTimestamp = completedTimestamp > 0 ? completedTimestamp : System.currentTimeMillis();
        this.reviewItems = reviewItems != null ? reviewItems : new ArrayList<>();
    }

    public String getCategoryId() { return categoryId; }
    public String getCategoryTitleBn() { return categoryTitleBn; }
    public String getCategoryTitleEn() { return categoryTitleEn; }
    public int getCorrectCount() { return correctCount; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getPointsEarned() { return pointsEarned; }
    public int getAccuracyPercentage() { return accuracyPercentage; }
    public long getCompletedTimestamp() { return completedTimestamp; }
    public List<QuestionReviewItem> getReviewItems() { return reviewItems; }
}
