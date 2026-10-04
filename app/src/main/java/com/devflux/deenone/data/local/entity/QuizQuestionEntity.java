package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_questions")
public class QuizQuestionEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctOptionIndex; // 0, 1, 2, 3
    private String explanation;
    private String referenceSource;
    private String category;
    private int difficulty; // 1=Beginner, 2=Intermediate, 3=Advanced

    public QuizQuestionEntity() {
    }

    @Ignore
    public QuizQuestionEntity(String question, String optionA, String optionB, String optionC,
                              String optionD, int correctOptionIndex, String explanation,
                              String referenceSource, String category, int difficulty) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.referenceSource = referenceSource;
        this.category = category;
        this.difficulty = difficulty;
    }

    @Ignore
    public QuizQuestionEntity(String question, String optionA, String optionB, String optionC,
                              String optionD, int correctOptionIndex, String explanation,
                              String category, int difficulty) {
        this(question, optionA, optionB, optionC, optionD, correctOptionIndex, explanation, "", category, difficulty);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getReferenceSource() { return referenceSource; }
    public void setReferenceSource(String referenceSource) { this.referenceSource = referenceSource; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
}
