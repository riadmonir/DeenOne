package com.devflux.deenone.features.hadith.model;

import java.util.List;

public class HadithReaderItem {

    public static final int TYPE_SECTION_HEADER = 1;
    public static final int TYPE_HADITH = 2;

    private int itemType;

    // Section Header Fields
    private String sectionTag;         // e.g. "১/১. অধ্যায়ঃ" or "1/1. Chapter:"
    private String sectionTitle;       // e.g. "আল্লাহর রসূল (ﷺ)-এর প্রতি কীভাবে ওহী শুরু হয়েছিল।"
    private String sectionArabicVerse; // "وَقَوْلُ اللَّهِ جَلَّ ذِكْرُهُ..."
    private String sectionTranslation; // "এ মর্মে আল্লাহ তা‘আলার বাণী..."

    // Hadith Item Fields
    private long id;
    private String bookSlug;
    private String bookNameBn;
    private String bookNameEn;
    private int hadithNumber;
    private String hadithNumberBn;
    private String gradeBn;
    private String gradeEn;
    private String arabicText;
    private String narratorBn;
    private String narratorEn;
    private String banglaText;
    private String englishText;
    private String footnoteBn;
    private String footnoteEn;
    private boolean isBookmarked;

    // Word by word list
    private List<WordToken> wordList;

    public static class WordToken {
        private String arabic;
        private String meaningBn;
        private String meaningEn;

        public WordToken(String arabic, String meaningBn, String meaningEn) {
            this.arabic = arabic;
            this.meaningBn = meaningBn;
            this.meaningEn = meaningEn;
        }

        public String getArabic() { return arabic; }
        public String getMeaning(boolean isBn) { return isBn ? meaningBn : meaningEn; }
    }

    // Factory method for section header
    public static HadithReaderItem createSectionHeader(
            String sectionTag,
            String sectionTitle,
            String sectionArabicVerse,
            String sectionTranslation
    ) {
        HadithReaderItem item = new HadithReaderItem();
        item.itemType = TYPE_SECTION_HEADER;
        item.sectionTag = sectionTag;
        item.sectionTitle = sectionTitle;
        item.sectionArabicVerse = sectionArabicVerse;
        item.sectionTranslation = sectionTranslation;
        return item;
    }

    // Factory method for hadith item
    public static HadithReaderItem createHadith(
            long id,
            String bookSlug,
            String bookNameBn,
            String bookNameEn,
            int hadithNumber,
            String hadithNumberBn,
            String gradeBn,
            String gradeEn,
            String arabicText,
            String narratorBn,
            String narratorEn,
            String banglaText,
            String englishText,
            String footnoteBn,
            String footnoteEn,
            List<WordToken> wordList
    ) {
        HadithReaderItem item = new HadithReaderItem();
        item.itemType = TYPE_HADITH;
        item.id = id;
        item.bookSlug = bookSlug;
        item.bookNameBn = bookNameBn;
        item.bookNameEn = bookNameEn;
        item.hadithNumber = hadithNumber;
        item.hadithNumberBn = hadithNumberBn;
        item.gradeBn = gradeBn;
        item.gradeEn = gradeEn;
        item.arabicText = arabicText;
        item.narratorBn = narratorBn;
        item.narratorEn = narratorEn;
        item.banglaText = banglaText;
        item.englishText = englishText;
        item.footnoteBn = footnoteBn;
        item.footnoteEn = footnoteEn;
        item.wordList = wordList;
        return item;
    }

    public int getItemType() { return itemType; }
    public String getSectionTag() { return sectionTag; }
    public String getSectionTitle() { return sectionTitle; }
    public String getSectionArabicVerse() { return sectionArabicVerse; }
    public String getSectionTranslation() { return sectionTranslation; }

    public long getId() { return id; }
    public String getBookSlug() { return bookSlug; }
    public String getBookName(boolean isBn) { return isBn ? bookNameBn : bookNameEn; }
    public int getHadithNumber() { return hadithNumber; }
    public String getHadithNumber(boolean isBn) { return isBn ? hadithNumberBn : String.valueOf(hadithNumber); }
    public String getGrade(boolean isBn) { return isBn ? gradeBn : gradeEn; }
    public String getArabicText() { return arabicText; }
    public String getNarrator(boolean isBn) { return isBn ? narratorBn : narratorEn; }
    public String getTranslation(boolean isBn) { return isBn ? banglaText : englishText; }
    public String getFootnote(boolean isBn) { return isBn ? footnoteBn : footnoteEn; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    public List<WordToken> getWordList() { return wordList; }
}
