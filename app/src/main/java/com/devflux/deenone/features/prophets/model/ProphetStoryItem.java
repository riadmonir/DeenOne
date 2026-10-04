package com.devflux.deenone.features.prophets.model;

import java.io.Serializable;
import java.util.List;

public class ProphetStoryItem implements Serializable {
    private final int id;
    private final String arabicName;
    private final String bengaliName;
    private final String englishName;
    private final String titleBengali;
    private final String titleEnglish;
    private final String eraCategory;
    private final int quranMentionCount;
    private final String surahReferences;
    private final String summaryBengali;
    private final String summaryEnglish;
    private final String detailedStoryBengali;
    private final String detailedStoryEnglish;
    private final String quranicDuaArabic;
    private final String quranicDuaTransliteration;
    private final String quranicDuaMeaning;
    private final String keyLessonsBengali;
    private final String keyLessonsEnglish;

    public ProphetStoryItem(int id,
                            String arabicName,
                            String bengaliName,
                            String englishName,
                            String titleBengali,
                            String titleEnglish,
                            String eraCategory,
                            int quranMentionCount,
                            String surahReferences,
                            String summaryBengali,
                            String summaryEnglish,
                            String detailedStoryBengali,
                            String detailedStoryEnglish,
                            String quranicDuaArabic,
                            String quranicDuaTransliteration,
                            String quranicDuaMeaning,
                            String keyLessonsBengali,
                            String keyLessonsEnglish) {
        this.id = id;
        this.arabicName = arabicName;
        this.bengaliName = bengaliName;
        this.englishName = englishName;
        this.titleBengali = titleBengali;
        this.titleEnglish = titleEnglish;
        this.eraCategory = eraCategory;
        this.quranMentionCount = quranMentionCount;
        this.surahReferences = surahReferences;
        this.summaryBengali = summaryBengali;
        this.summaryEnglish = summaryEnglish;
        this.detailedStoryBengali = detailedStoryBengali;
        this.detailedStoryEnglish = detailedStoryEnglish;
        this.quranicDuaArabic = quranicDuaArabic;
        this.quranicDuaTransliteration = quranicDuaTransliteration;
        this.quranicDuaMeaning = quranicDuaMeaning;
        this.keyLessonsBengali = keyLessonsBengali;
        this.keyLessonsEnglish = keyLessonsEnglish;
    }

    public int getId() { return id; }
    public String getArabicName() { return arabicName; }
    public String getBengaliName() { return bengaliName; }
    public String getEnglishName() { return englishName; }
    public String getTitle(boolean isBn) { return isBn ? titleBengali : titleEnglish; }
    public String getEraCategory() { return eraCategory; }
    public int getQuranMentionCount() { return quranMentionCount; }
    public String getSurahReferences() { return surahReferences; }
    public String getSummary(boolean isBn) { return isBn ? summaryBengali : summaryEnglish; }
    public String getDetailedStory(boolean isBn) { return isBn ? detailedStoryBengali : detailedStoryEnglish; }
    public String getQuranicDuaArabic() { return quranicDuaArabic; }
    public String getQuranicDuaTransliteration() { return quranicDuaTransliteration; }
    public String getQuranicDuaMeaning() { return quranicDuaMeaning; }
    public String getKeyLessons(boolean isBn) { return isBn ? keyLessonsBengali : keyLessonsEnglish; }
}
