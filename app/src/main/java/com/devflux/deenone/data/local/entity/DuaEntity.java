package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "duas")
public class DuaEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String category;         // Morning Dua, Evening Dua, Protection, Travel, etc.
    private String title;            // ঘুম থেকে ওঠার দোয়া
    private String arabic;           // الْحَمْدُ لِلَّهِ الَّذِي...
    private String transliteration;  // আলহামদু লিল্লাহিল্লাজি...
    private String bengaliMeaning;   // সমস্ত প্রশংসা আল্লাহর...
    private String englishMeaning;   // All praise is for Allah who gave us life...
    private String urduMeaning;      // تمام تعریفیں اللہ کے لیے ہیں...
    private String whenToRead;       // ঘুম থেকে জাগ্রত হওয়ার সাথে সাথে
    private String whyToRead;        // জীবনের শুকরিয়া আদায় ও সুন্নাহ পালন
    private String reference;        // সহীহ বুখারী: ৬৩১২, সহীহ মুসলিম: ২৭১১
    private boolean isFavorite;

    public DuaEntity() {
    }

    @Ignore
    public DuaEntity(String category, String title, String arabic, String transliteration,
                     String bengaliMeaning, String englishMeaning, String urduMeaning,
                     String whenToRead, String whyToRead, String reference, boolean isFavorite) {
        this.category = category;
        this.title = title;
        this.arabic = arabic;
        this.transliteration = transliteration;
        this.bengaliMeaning = bengaliMeaning;
        this.englishMeaning = englishMeaning;
        this.urduMeaning = urduMeaning;
        this.whenToRead = whenToRead;
        this.whyToRead = whyToRead;
        this.reference = reference;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public DuaEntity(String category, String title, String arabic, String transliteration,
                     String bengaliMeaning, String reference, boolean isFavorite) {
        this(category, title, arabic, transliteration, bengaliMeaning, "", "", "", "", reference, isFavorite);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArabic() { return arabic; }
    public void setArabic(String arabic) { this.arabic = arabic; }

    public String getTransliteration() { return transliteration; }
    public void setTransliteration(String transliteration) { this.transliteration = transliteration; }

    public String getBengaliMeaning() { return bengaliMeaning; }
    public void setBengaliMeaning(String bengaliMeaning) { this.bengaliMeaning = bengaliMeaning; }

    public String getEnglishMeaning() { return englishMeaning; }
    public void setEnglishMeaning(String englishMeaning) { this.englishMeaning = englishMeaning; }

    public String getUrduMeaning() { return urduMeaning; }
    public void setUrduMeaning(String urduMeaning) { this.urduMeaning = urduMeaning; }

    public String getWhenToRead() { return whenToRead; }
    public void setWhenToRead(String whenToRead) { this.whenToRead = whenToRead; }

    public String getWhyToRead() { return whyToRead; }
    public void setWhyToRead(String whyToRead) { this.whyToRead = whyToRead; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    // =========================================================================
    // Dual-Language Clean Display Helpers (Rule 5)
    // =========================================================================
    public String getDisplayTitle(boolean isBn) {
        if (isBn || title == null) return title != null ? title : "";
        if (title.contains("সকালের আশ্রয় ও তাওহীদের দোয়া") || title.contains("সকালের দোয়া")) return "Morning Refuge & Tawheed Dua";
        if (title.contains("সন্ধ্যার নিরাপত্তা ও আশ্রয়ের দোয়া") || title.contains("সন্ধ্যার দোয়া")) return "Evening Protection & Refuge Dua";
        if (title.contains("ঘুমানোর পূর্বের সুন্নাহ দোয়া") || title.contains("ঘুমানোর দোয়া")) return "Sunnah Dua Before Sleeping";
        if (title.contains("ঘুম থেকে ওঠার দোয়া")) return "Dua Upon Waking Up";
        if (title.contains("খাওয়ার পূর্বের দোয়া") || title.contains("খাওয়ার দোয়া")) return "Dua Before Eating";
        if (title.contains("খাওয়ার পরের দোয়া")) return "Dua After Eating";
        if (title.contains("ঘর থেকে বের হওয়ার দোয়া")) return "Dua When Leaving Home";
        if (title.contains("ঘরে প্রবেশের দোয়া")) return "Dua When Entering Home";
        if (title.contains("মসজিদে প্রবেশের দোয়া")) return "Dua When Entering Mosque";
        if (title.contains("মসজিদ থেকে বের হওয়ার দোয়া")) return "Dua When Leaving Mosque";
        if (title.contains("সালাতের দোয়া") || title.contains("সালাত সম্পর্কিত")) return "Dua Related to Salah";
        if (title.contains("ক্ষমা ও তওবা") || title.contains("ক্ষমা প্রার্থনা")) return "Dua for Forgiveness & Repentance";
        if (title.contains("সুরক্ষা ও হেফাজত")) return "Dua for Protection & Safety";
        if (title.contains("দুশ্চিন্তা ও পেরেশানি")) return "Dua for Anxiety & Relief";
        if (title.contains("পিতামাতা")) return "Dua for Parents";
        if (title.contains("রিজিক ও বরকত") || title.contains("রিজিক")) return "Dua for Sustenance & Barakah";
        if (title.contains("রোগমুক্তি ও সুস্থতা") || title.contains("সুস্থতা")) return "Dua for Healing & Good Health";
        if (title.contains("সফর ও ভ্রমণ") || title.contains("সফরের দোয়া")) return "Dua for Travel & Journey";
        if (title.contains("রমজান ও রোজা") || title.contains("ইফতারের দোয়া") || title.contains("সেহরির দোয়া")) return "Dua for Ramadan & Fasting";
        if (title.contains("হজ")) return "Dua for Hajj";
        if (title.contains("উমরাহ")) return "Dua for Umrah";
        if (title.contains("দৈনন্দিন জীবন")) return "Dua for Daily Life";
        if (title.contains("রব্বানা")) return "Rabbana Dua";
        if (category != null && !category.isEmpty()) return category + " Dua";
        return title;
    }

    public String getDisplayCategory(boolean isBn) {
        if (category == null) return isBn ? "মাসনুন দোয়া" : "Masnoon Dua";
        if (isBn) {
            switch (category.trim()) {
                case "Morning Dua": return "সকালের দোয়া";
                case "Evening Dua": return "সন্ধ্যার দোয়া";
                case "Before Sleeping": return "ঘুমানোর দোয়া";
                case "Waking Up": case "After Waking": return "ঘুম থেকে ওঠার দোয়া";
                case "Before Eating": case "Eating": return "খাওয়ার দোয়া";
                case "After Eating": return "খাওয়ার পরের দোয়া";
                case "Leaving Home": return "ঘর থেকে বের হওয়ার দোয়া";
                case "Entering Home": return "ঘরে প্রবেশের দোয়া";
                case "Entering Mosque": case "Mosque": return "মসজিদে প্রবেশের দোয়া";
                case "Leaving Mosque": return "মসজিদ থেকে বের হওয়ার দোয়া";
                case "Salah-related Duas": case "Salah": return "সালাতের দোয়া";
                case "Forgiveness": return "ক্ষমা ও তওবা";
                case "Protection": return "সুরক্ষা ও হেফাজত";
                case "Anxiety/Worry": case "Anxiety & Worry": return "দুশ্চিন্তা ও পেরেশানি";
                case "Parents": return "পিতামাতা";
                case "Rizq": case "Rizq & Blessing": return "রিজিক ও বরকত";
                case "Health": case "Health & Healing": return "রোগমুক্তি ও সুস্থতা";
                case "Guidance": return "হেদায়াত";
                case "Travel": return "সফর ও ভ্রমণ";
                case "Ramadan": case "Ramadan & Fasting": return "রমজান ও রোজা";
                case "Hajj": return "হজ";
                case "Umrah": return "উমরাহ";
                case "Daily Life": return "দৈনন্দিন জীবন";
                case "Rabbana": return "রব্বানা দোয়া";
                case "Favorites": return "প্রিয় দোয়া";
                default: return category;
            }
        } else {
            return category;
        }
    }

    public String getDisplayMeaning(boolean isBn) {
        if (isBn) {
            return (bengaliMeaning != null && !bengaliMeaning.trim().isEmpty()) ? bengaliMeaning.trim() : (englishMeaning != null ? englishMeaning.trim() : "");
        } else {
            return (englishMeaning != null && !englishMeaning.trim().isEmpty()) ? englishMeaning.trim() : (bengaliMeaning != null ? bengaliMeaning.trim() : "");
        }
    }

    public String getDisplayWhenToRead(boolean isBn) {
        if (isBn) {
            return whenToRead != null ? whenToRead.trim() : "";
        }
        if (whenToRead == null || whenToRead.trim().isEmpty()) return "";
        if ("Morning Dua".equalsIgnoreCase(category)) return "After Fajr prayer until sunrise";
        if ("Evening Dua".equalsIgnoreCase(category)) return "After Asr prayer until Maghrib/Isha";
        if ("Before Sleeping".equalsIgnoreCase(category)) return "Immediately before sleeping";
        if ("Waking Up".equalsIgnoreCase(category) || "After Waking".equalsIgnoreCase(category)) return "Immediately upon waking up";
        if ("Before Eating".equalsIgnoreCase(category)) return "Before starting meal";
        if ("After Eating".equalsIgnoreCase(category)) return "Immediately after completing meal";
        if ("Travel".equalsIgnoreCase(category)) return "Upon boarding conveyance or embarking on a journey";
        if ("Protection".equalsIgnoreCase(category)) return "Morning and evening for general safety";
        if ("Anxiety/Worry".equalsIgnoreCase(category) || "Anxiety & Worry".equalsIgnoreCase(category)) return "During times of distress and anxiety";
        if ("Forgiveness".equalsIgnoreCase(category)) return "Any time during the day and night";
        if ("Parents".equalsIgnoreCase(category)) return "In every prayer and supplication for parents";
        if ("Rizq".equalsIgnoreCase(category)) return "During Tahajjud, morning, and after obligatory prayers";
        if ("Health".equalsIgnoreCase(category)) return "During illness and when visiting the sick";
        if ("Guidance".equalsIgnoreCase(category)) return "When seeking clarity and right direction";
        if ("Ramadan".equalsIgnoreCase(category)) return "At the time of Iftar and during Fasting";
        if ("Hajj".equalsIgnoreCase(category) || "Umrah".equalsIgnoreCase(category)) return "During sacred pilgrimage rituals";
        if ("Salah-related Duas".equalsIgnoreCase(category)) return "During and immediately following Salah";
        return "At any blessed time or during supplication";
    }

    public String getDisplayWhyToRead(boolean isBn) {
        if (isBn) {
            return whyToRead != null ? whyToRead.trim() : "";
        }
        if (whyToRead == null || whyToRead.trim().isEmpty()) return "";
        if ("Morning Dua".equalsIgnoreCase(category)) return "Protection from all harm throughout the day and earning Allah's mercy";
        if ("Evening Dua".equalsIgnoreCase(category)) return "Protection from nightly evils, danger, and peace of mind";
        if ("Before Sleeping".equalsIgnoreCase(category)) return "Peaceful sleep, protection from evil dreams, and Sunnah revival";
        if ("Waking Up".equalsIgnoreCase(category) || "After Waking".equalsIgnoreCase(category)) return "Expressing gratitude to Allah for granting life and a blessed new day";
        if ("Before Eating".equalsIgnoreCase(category)) return "Brings divine blessing (Barakah) in food and prevents Satan's share";
        if ("After Eating".equalsIgnoreCase(category)) return "Gratitude to Allah and forgiveness of past minor sins";
        if ("Travel".equalsIgnoreCase(category)) return "Safe travel, protection on the road, and safe return to family";
        if ("Protection".equalsIgnoreCase(category)) return "Shield against envy, harm, calamities, and evil whispers";
        if ("Anxiety/Worry".equalsIgnoreCase(category) || "Anxiety & Worry".equalsIgnoreCase(category)) return "Relief from distress, grief, debts, and overwhelming feelings";
        if ("Forgiveness".equalsIgnoreCase(category)) return "Cleansing of sins, spiritual peace, and earning Jannah";
        if ("Parents".equalsIgnoreCase(category)) return "Fulfilling filial duty, immense reward, and divine mercy for parents";
        if ("Rizq".equalsIgnoreCase(category)) return "Abundant halal provision, debt clearance, and prosperity";
        if ("Health".equalsIgnoreCase(category)) return "Shifa (healing) from physical and spiritual sickness";
        if ("Guidance".equalsIgnoreCase(category)) return "Steadfastness upon the true path and sound judgment";
        if ("Ramadan".equalsIgnoreCase(category)) return "Multiplied rewards of fasting and accepted prayers";
        if ("Hajj".equalsIgnoreCase(category) || "Umrah".equalsIgnoreCase(category)) return "Acceptance of pilgrimage and complete expiation of sins";
        if ("Salah-related Duas".equalsIgnoreCase(category)) return "Perfection of prayer and nearness to Allah";
        return "Adhering to the Prophetic Sunnah and obtaining divine rewards";
    }

    public String getDisplayReference(boolean isBn) {
        if (isBn || reference == null) return reference != null ? reference : "";
        String ref = reference;
        ref = ref.replace("সহীহ বুখারী", "Sahih Bukhari")
                 .replace("সহীহ মুসলিম", "Sahih Muslim")
                 .replace("সুনান আবু দাউদ", "Sunan Abi Dawud")
                 .replace("তিরমিযী", "Jami' at-Tirmidhi")
                 .replace("সুনান তিরমিযী", "Jami' at-Tirmidhi")
                 .replace("ইবনে মাজাহ", "Sunan Ibn Majah")
                 .replace("নাসায়ী", "Sunan an-Nasa'i")
                 .replace("মুসনাদে আহমাদ", "Musnad Ahmad")
                 .replace("হিসনুল মুসলিম", "Hisnul Muslim");
        StringBuilder sb = new StringBuilder();
        for (char c : ref.toCharArray()) {
            if (c >= '০' && c <= '৯') {
                sb.append((char) ('0' + (c - '০')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
