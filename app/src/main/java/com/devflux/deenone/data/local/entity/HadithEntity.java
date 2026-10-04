package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "hadith_table")
public class HadithEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String collectionId;     // bukhari, muslim, tirmidhi, abudawud, nasai, ibnmajah
    private String collectionName;   // সহীহ আল-বুখারী (Sahih al-Bukhari)
    private int hadithNumber;        // 1
    private String bookName;         // কিতাবুল ওহী / Book of Revelation
    private String chapterTitle;     // নিয়তের ওপর কাজের ফলাফল নির্ভরশীল
    private String narrator;         // হযরত উমর ইবনুল খাত্তাব (রা.)
    private String arabicText;
    private String banglaTranslation;
    private String englishTranslation;
    private String urduTranslation;
    private String grade;            // সহীহ (Sahih)
    private String sourceReference;  // সহীহ বুখারী: ১
    private boolean isBookmarked;
    private String topicCategory;    // নিয়ত ও ইখলাস

    public HadithEntity() {
    }

    @Ignore
    public HadithEntity(
            String collectionId,
            String collectionName,
            int hadithNumber,
            String bookName,
            String chapterTitle,
            String narrator,
            String arabicText,
            String banglaTranslation,
            String englishTranslation,
            String urduTranslation,
            String grade,
            String sourceReference,
            boolean isBookmarked,
            String topicCategory
    ) {
        this.collectionId = collectionId;
        this.collectionName = collectionName;
        this.hadithNumber = hadithNumber;
        this.bookName = bookName;
        this.chapterTitle = chapterTitle;
        this.narrator = narrator;
        this.arabicText = arabicText;
        this.banglaTranslation = banglaTranslation;
        this.englishTranslation = englishTranslation;
        this.urduTranslation = urduTranslation;
        this.grade = grade;
        this.sourceReference = sourceReference;
        this.isBookmarked = isBookmarked;
        this.topicCategory = topicCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public int getHadithNumber() {
        return hadithNumber;
    }

    public void setHadithNumber(int hadithNumber) {
        this.hadithNumber = hadithNumber;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public String getArabicText() {
        return arabicText;
    }

    public void setArabicText(String arabicText) {
        this.arabicText = arabicText;
    }

    public String getBanglaTranslation() {
        return banglaTranslation;
    }

    public void setBanglaTranslation(String banglaTranslation) {
        this.banglaTranslation = banglaTranslation;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public void setEnglishTranslation(String englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public String getUrduTranslation() {
        return urduTranslation;
    }

    public void setUrduTranslation(String urduTranslation) {
        this.urduTranslation = urduTranslation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public String getTopicCategory() {
        return topicCategory;
    }

    public void setTopicCategory(String topicCategory) {
        this.topicCategory = topicCategory;
    }

    // =========================================================================
    // Universal Dual-Language Display Helpers (Rule 5: Zero Bracket Pollution)
    // =========================================================================
    public String getDisplayCollectionName(boolean isBn) {
        String col = collectionId != null ? collectionId.toLowerCase() : "";
        if (isBn) {
            switch (col) {
                case "bukhari": return "সহীহ আল-বুখারী";
                case "muslim": return "সহীহ মুসলিম";
                case "tirmidhi": return "জামে আত-তিরমিযী";
                case "abudawud": return "সুনানে আবু দাউদ";
                case "nasai": return "সুনানে নাসাঈ";
                case "ibnmajah": return "সুনানে ইবনে মাজাহ";
                case "nawawi40": return "ইমাম নববীর ৪০ হাদিস";
                default:
                    if (collectionName != null && collectionName.contains("(")) {
                        return collectionName.substring(0, collectionName.indexOf("(")).trim();
                    }
                    return collectionName != null ? collectionName : "সহীহ হাদিস";
            }
        } else {
            switch (col) {
                case "bukhari": return "Sahih al-Bukhari";
                case "muslim": return "Sahih Muslim";
                case "tirmidhi": return "Jami at-Tirmidhi";
                case "abudawud": return "Sunan Abi Dawud";
                case "nasai": return "Sunan an-Nasa'i";
                case "ibnmajah": return "Sunan Ibn Majah";
                case "nawawi40": return "Al-Nawawi's 40 Hadith";
                default:
                    if (collectionName != null && collectionName.contains("(") && collectionName.contains(")")) {
                        int start = collectionName.indexOf("(") + 1;
                        int end = collectionName.indexOf(")");
                        if (end > start) {
                            return collectionName.substring(start, end).trim();
                        }
                    }
                    return collectionName != null ? collectionName : "Sahih Hadith";
            }
        }
    }

    public String getDisplayChapterTitle(boolean isBn) {
        if (isBn) {
            if (chapterTitle != null && chapterTitle.contains("(")) {
                return chapterTitle.substring(0, chapterTitle.indexOf("(")).trim();
            }
            return chapterTitle != null ? chapterTitle : "";
        } else {
            String title = chapterTitle != null ? chapterTitle : "";
            if (title.contains("নিয়ত") || title.contains("নিয়ত")) {
                return "Actions are Judged by Intentions";
            } else if (title.contains("জিবরীল") || title.contains("ঈমান, ইসলাম")) {
                return "The Hadith of Jibril: Faith, Islam and Ihsan";
            } else if (title.contains("কল্যাণ") || title.contains("পছন্দ")) {
                return "Loving for One's Brother What One Loves for Oneself";
            } else if (title.contains("হাসিমুখে") || title.contains("সাদাকা")) {
                return "Smiling at Your Brother is Charity";
            } else if (title.contains("দয়াশীল") || title.contains("রহমত")) {
                return "Allah's Mercy upon the Merciful";
            } else if (title.contains("মায়ের সেবা") || title.contains("জান্নাত")) {
                return "Serving One's Mother and the Closeness of Paradise";
            } else if (title.contains("দ্বীনি ইলম") || title.contains("জ্ঞান")) {
                return "Obligation of Seeking Sacred Knowledge";
            } else if (title.contains("কুরআন শিক্ষা") || title.contains("শ্রেষ্ঠত্ব")) {
                return "Excellence of Learning and Teaching the Quran";
            }
            if (bookName != null && bookName.contains("(") && bookName.contains(")")) {
                int start = bookName.indexOf("(") + 1;
                int end = bookName.indexOf(")");
                if (end > start) {
                    return bookName.substring(start, end).trim();
                }
            }
            return title;
        }
    }

    public String getDisplayNarrator(boolean isBn) {
        if (narrator == null) return "";
        if (isBn) {
            return narrator;
        } else {
            String n = narrator;
            if (n.contains("উমর ইবনুল খাত্তাব")) return "Umar ibn al-Khattab (RA)";
            if (n.contains("আবু হুরায়রা")) return "Abu Hurairah (RA)";
            if (n.contains("আনাস ইবনে মালিক") || n.contains("আনাস")) return "Anas ibn Malik (RA)";
            if (n.contains("আব্দুল্লাহ ইবনে আমর")) return "Abdullah ibn Amr (RA)";
            if (n.contains("আব্দুল্লাহ ইবনে উমর")) return "Abdullah ibn Umar (RA)";
            if (n.contains("আব্দুল্লাহ ইবনে আব্বাস")) return "Abdullah ibn Abbas (RA)";
            if (n.contains("মুআবিয়া ইবনে জাহেমা")) return "Muawiyah ibn Jahima (RA)";
            if (n.contains("উসমান ইবনে আফফান")) return "Uthman ibn Affan (RA)";
            if (n.contains("আলী ইবনে আবি তালিব")) return "Ali ibn Abi Talib (RA)";
            if (n.contains("আবু জর আল-গিফারী") || n.contains("আবু জর")) return "Abu Dharr al-Ghifari (RA)";
            if (n.contains("আয়েশা")) return "Aisha bint Abi Bakr (RA)";
            return n.replace("হযরত ", "").replace("(রা.)", "(RA)");
        }
    }

    public String getDisplayGrade(boolean isBn) {
        if (isBn) {
            if (grade == null || grade.isEmpty()) return "সহীহ";
            if (grade.contains("মুত্তাফাকুন")) return "সহীহ - মুত্তাফাকুন আলাইহি";
            if (grade.contains("হাসান") && grade.contains("সহীহ")) return "হাসান সহীহ";
            if (grade.contains("হাসান")) return "হাসান";
            if (grade.contains("দইফ") || grade.contains("যঈফ")) return "যঈফ";
            return "সহীহ";
        } else {
            if (grade == null || grade.isEmpty()) return "Sahih";
            String gLower = grade.toLowerCase();
            if (gLower.contains("muttafaq") || grade.contains("মুত্তাফাকুন")) return "Sahih - Muttafaqun Alayh";
            if ((gLower.contains("hasan") && gLower.contains("sahih")) || (grade.contains("হাসান") && grade.contains("সহীহ"))) return "Hasan Sahih";
            if (gLower.contains("hasan") || grade.contains("হাসান")) return "Hasan";
            if (gLower.contains("daif") || gLower.contains("da'if") || grade.contains("দইফ") || grade.contains("যঈফ")) return "Da'if";
            return "Sahih";
        }
    }

    public String getDisplaySourceReference(boolean isBn) {
        if (sourceReference == null) return "";
        if (isBn) {
            return sourceReference;
        } else {
            return sourceReference
                    .replace("সহীহ বুখারী", "Sahih Bukhari")
                    .replace("সহীহ মুসলিম", "Sahih Muslim")
                    .replace("জামে তিরমিযী", "Jami at-Tirmidhi")
                    .replace("সুনান আবু দাউদ", "Sunan Abi Dawud")
                    .replace("সুনানে আবু দাউদ", "Sunan Abi Dawud")
                    .replace("সুনান আন-নাসায়ী", "Sunan an-Nasa'i")
                    .replace("সুনানে নাসাঈ", "Sunan an-Nasa'i")
                    .replace("সুনান ইবনে মাজাহ", "Sunan Ibn Majah")
                    .replace("সুনানে ইবনে মাজাহ", "Sunan Ibn Majah")
                    .replace("মুসনাদ আহমদ", "Musnad Ahmad")
                    .replace("সহীহ ইবনে হিব্বান", "Sahih Ibn Hibban")
                    .replace("সিলসিলাতুস সাহীহাহ", "Silsilah as-Sahihah");
        }
    }

    public String getDisplayTranslation(boolean isBn) {
        if (isBn) {
            return (banglaTranslation != null && !banglaTranslation.trim().isEmpty())
                    ? banglaTranslation
                    : (englishTranslation != null ? englishTranslation : "");
        } else {
            return (englishTranslation != null && !englishTranslation.trim().isEmpty())
                    ? englishTranslation
                    : (banglaTranslation != null ? banglaTranslation : "");
        }
    }
}
