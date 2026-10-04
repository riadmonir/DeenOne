package com.devflux.deenone.core.quiz;

import java.io.Serializable;

public class QuizItem implements Serializable {
    public final String id;
    public final String question;
    public final String[] options;
    public final int correctIndex; // 0 to 3
    public final String category;  // e.g. "কুরআন", "হাদিস", "সীরাত", "ফিকহ", "সাহাবা", "ইতিহাস", "আখলাক"
    public final String contextTag;// "general", "jummah", "ramadan", "eid"
    public final String reference; // e.g. "সহীহ বুখারী: ১৩৮২", "সূরা আল-বাক্বারাহ: ১৮৩"
    public final String explanation;
    public String availableFromDate; // "yyyy-MM-dd" for calendar day release
    public String source;           // "Islamic Foundation / Sahih Sittah / Classical Madhhab Jurisprudence"
    public String difficulty;       // "easy", "medium", "hard"

    public QuizItem(String id, String question, String[] options, int correctIndex,
                    String category, String contextTag, String reference, String explanation) {
        this(id, question, options, correctIndex, category, contextTag, reference, explanation, "", "Verified Islamic Database", "medium");
    }

    public QuizItem(String id, String question, String[] options, int correctIndex,
                    String category, String contextTag, String reference, String explanation,
                    String availableFromDate, String source, String difficulty) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
        this.category = category;
        this.contextTag = contextTag;
        this.reference = reference;
        this.explanation = explanation;
        this.availableFromDate = availableFromDate != null ? availableFromDate : "";
        this.source = source != null ? source : "Verified Islamic Database";
        this.difficulty = difficulty != null ? difficulty : "medium";
    }
}