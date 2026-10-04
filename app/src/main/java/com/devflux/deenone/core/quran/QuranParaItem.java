package com.devflux.deenone.core.quran;

import java.util.ArrayList;
import java.util.List;

public class QuranParaItem {

    private final int juzNumber;
    private final String arabicName;
    private final String bengaliName;
    private final String englishName;
    private final int startSurahNumber;
    private final int startAyahNumber;
    private final int endSurahNumber;
    private final int endAyahNumber;
    private final String rangeBengali;
    private final String rangeEnglish;

    public QuranParaItem(int juzNumber, String arabicName, String bengaliName, String englishName,
                          int startSurahNumber, int startAyahNumber, int endSurahNumber, int endAyahNumber,
                          String rangeBengali, String rangeEnglish) {
        this.juzNumber = juzNumber;
        this.arabicName = arabicName;
        this.bengaliName = bengaliName;
        this.englishName = englishName;
        this.startSurahNumber = startSurahNumber;
        this.startAyahNumber = startAyahNumber;
        this.endSurahNumber = endSurahNumber;
        this.endAyahNumber = endAyahNumber;
        this.rangeBengali = rangeBengali;
        this.rangeEnglish = rangeEnglish;
    }

    public int getJuzNumber() { return juzNumber; }
    public String getArabicName() { return arabicName; }
    public String getBengaliName() { return bengaliName; }
    public String getEnglishName() { return englishName; }
    public int getStartSurahNumber() { return startSurahNumber; }
    public int getStartAyahNumber() { return startAyahNumber; }
    public int getEndSurahNumber() { return endSurahNumber; }
    public int getEndAyahNumber() { return endAyahNumber; }
    public String getRangeBengali() { return rangeBengali; }
    public String getRangeEnglish() { return rangeEnglish; }

    public List<Integer> getSurahNumbers() {
        List<Integer> list = new ArrayList<>();
        for (int i = startSurahNumber; i <= endSurahNumber; i++) {
            list.add(i);
        }
        return list;
    }

    public static List<QuranParaItem> getAll30Paras() {
        List<QuranParaItem> list = new ArrayList<>();
        list.add(new QuranParaItem(1, "الم", "আলিফ লাম মীম", "Alif Lam Meem", 1, 1, 2, 141, "আল-ফাতিহা ১:১ - আল-বাকারা ২:১৪১", "Al-Fatihah 1:1 - Al-Baqarah 2:141"));
        list.add(new QuranParaItem(2, "سَيَقُولُ", "সায়াকুল", "Sayaqool", 2, 142, 2, 252, "আল-বাকারা ২:১৪২ - ২:২৫২", "Al-Baqarah 2:142 - 2:252"));
        list.add(new QuranParaItem(3, "تِلْكَ الرُّسُلُ", "তিলকার রুসুল", "Tilkar Rusul", 2, 253, 3, 92, "আল-বাকারা ২:২৫৩ - আলে ইমরান ৩:৯২", "Al-Baqarah 2:253 - Aal-Imran 3:92"));
        list.add(new QuranParaItem(4, "لَنْ تَنَالُوا", "লান তানা-লু", "Lan Tanaaloo", 3, 93, 4, 23, "আলে ইমরান ৩:৯৩ - আন-নিসা ৪:২৩", "Aal-Imran 3:93 - An-Nisa 4:23"));
        list.add(new QuranParaItem(5, "وَالْمُحْصَنَاتُ", "ওয়াল মুহসানাত", "Wal Muhsanat", 4, 24, 4, 147, "আন-নিসা ৪:২৪ - ৪:১৪৭", "An-Nisa 4:24 - 4:147"));
        list.add(new QuranParaItem(6, "لَا يُحِبُّ اللَّهُ", "লা ইউহিব্বুল্লাহ", "La Yuhibbullah", 4, 148, 5, 81, "আন-নিসা ৪:১৪৮ - আল-মায়িদাহ ৫:৮১", "An-Nisa 4:148 - Al-Ma'idah 5:81"));
        list.add(new QuranParaItem(7, "وَإِذَا سَمِعُوا", "ওয়া ইজা সামিউ", "Wa Iza Sami'oo", 5, 82, 6, 110, "আল-মায়িদাহ ৫:৮২ - আল-আনআম ৬:১১০", "Al-Ma'idah 5:82 - Al-An'am 6:110"));
        list.add(new QuranParaItem(8, "وَلَوْ أَنَّنَا", "ওয়া লাউ আন্নানা", "Wa Law Annana", 6, 111, 7, 87, "আল-আনআম ৬:১১১ - আল-আরাফ ৭:৮৭", "Al-An'am 6:111 - Al-A'raf 7:87"));
        list.add(new QuranParaItem(9, "قَالَ الْمَلَأُ", "ক্বালা আল-মালাউ", "Qalal Mala'u", 7, 88, 8, 40, "আল-আরাফ ৭:৮৮ - আল-আনফাল ৮:৪০", "Al-A'raf 7:88 - Al-Anfal 8:40"));
        list.add(new QuranParaItem(10, "وَاعْلَمُوا", "ওয়া'লামু", "Wa A'lamoo", 8, 41, 9, 92, "আল-আনফাল ৮:৪১ - আত-তাওবাহ ৯:৯২", "Al-Anfal 8:41 - At-Tawbah 9:92"));
        list.add(new QuranParaItem(11, "يَعْتَذِرُونَ", "ইয়া'তাযিরূন", "Ya'taziroona", 9, 93, 11, 5, "আত-তাওবাহ ৯:৯৩ - হূদ ১১:৫", "At-Tawbah 9:93 - Hud 11:5"));
        list.add(new QuranParaItem(12, "وَمَا مِنْ دَابَّةٍ", "ওয়ামা মিন দা-ব্বাহ", "Wa Mamin Da'abbah", 11, 6, 12, 52, "হূদ ১১:৬ - ইউসুফ ১২:৫২", "Hud 11:6 - Yusuf 12:52"));
        list.add(new QuranParaItem(13, "وَمَا أُبَرِّئُ", "ওয়ামা উবাররিউ", "Wa Ma Ubarri'u", 12, 53, 14, 52, "ইউসুফ ১২:৫৩ - ইব্রাহিম ১৪:৫২", "Yusuf 12:53 - Ibrahim 14:52"));
        list.add(new QuranParaItem(14, "رُبَمَا", "রুবামা", "Rubama", 15, 1, 16, 128, "আল-হিজর ১৫:১ - আন-নাহল ১৬:১২৮", "Al-Hijr 15:1 - An-Nahl 16:128"));
        list.add(new QuranParaItem(15, "سُبْحَانَ الَّذِي", "সুবহানাল্লাজি", "Subhanallazi", 17, 1, 18, 74, "আল-ইসরা ১৭:১ - আল-কাহফ ১৮:৭৪", "Al-Isra 17:1 - Al-Kahf 18:74"));
        list.add(new QuranParaItem(16, "قَالَ أَلَمْ", "ক্বালা আলাম", "Qala Alam", 18, 75, 20, 135, "আল-কাহফ ১৮:৭৫ - ত্বা-হা ২০:১৩৫", "Al-Kahf 18:75 - Ta-Ha 20:135"));
        list.add(new QuranParaItem(17, "اقْتَرَبَ", "ইক্বতারাবা", "Iqtaraba", 21, 1, 22, 78, "আল-আম্বিয়া ২১:১ - আল-হাজ্জ ২২:৭৮", "Al-Anbiya 21:1 - Al-Hajj 22:78"));
        list.add(new QuranParaItem(18, "قَدْ أَفْلَحَ", "ক্বাদ আফলাহা", "Qad Aflaha", 23, 1, 25, 20, "আল-মুমিনুন ২৩:১ - আল-ফুরকান ২৫:২০", "Al-Mu'minun 23:1 - Al-Furqan 25:20"));
        list.add(new QuranParaItem(19, "وَقَالَ الَّذِينَ", "ওয়া ক্বালা আল্লাজিনা", "Wa Qalallazina", 25, 21, 27, 55, "আল-ফুরকান ২৫:২১ - আন-নামল ২৭:৫৫", "Al-Furqan 25:21 - An-Naml 27:55"));
        list.add(new QuranParaItem(20, "أَمَّنْ خَلَقَ", "আম্মান খালাকা", "Amman Khalaq", 27, 56, 29, 45, "আন-নামল ২৭:৫৬ - আল-আনকাবুত ২৯:৪৫", "An-Naml 27:56 - Al-Ankabut 29:45"));
        list.add(new QuranParaItem(21, "اتْلُ مَا أُوحِيَ", "উতলু মা উহিয়া", "Utlu Ma Oohiya", 29, 46, 33, 30, "আল-আনকাবুত ২৯:৪৬ - আল-আহযাব ৩৩:৩০", "Al-Ankabut 29:46 - Al-Ahzab 33:30"));
        list.add(new QuranParaItem(22, "وَمَنْ يَقْنُتْ", "ওয়ামান ইয়াকনুত", "Wa Man Yaqnut", 33, 31, 36, 27, "আল-আহযাব ৩৩:৩১ - ইয়াসিন ৩৬:২৭", "Al-Ahzab 33:31 - Yaseen 36:27"));
        list.add(new QuranParaItem(23, "وَمَا لِيَ", "ওয়ামা লিয়া", "Wa Maliya", 36, 28, 39, 31, "ইয়াসিন ৩৬:২৮ - আজ-জুমার ৩৯:৩১", "Yaseen 36:28 - Az-Zumar 39:31"));
        list.add(new QuranParaItem(24, "فَمَنْ أَظْلَمُ", "ফামান আজলামু", "Faman Azlamu", 39, 32, 41, 46, "আজ-জুমার ৩৯:৩২ - ফুসসিলাত ৪১:৪৬", "Az-Zumar 39:32 - Fussilat 41:46"));
        list.add(new QuranParaItem(25, "إِلَيْهِ يُرَدُّ", "ইলাইহি ইউরাদ্দু", "Ilayhi Yuraddu", 41, 47, 45, 37, "ফুসসিলাত ৪১:৪৭ - আল-জাসিয়াহ ৪৫:৩৭", "Fussilat 41:47 - Al-Jathiyah 45:37"));
        list.add(new QuranParaItem(26, "حم", "হা-মীম", "Ha Meem", 46, 1, 51, 30, "আল-আহকাফ ৪৬:১ - আজ-জারিয়াত ৫১:৩০", "Al-Ahqaf 46:1 - Adh-Dhariyat 51:30"));
        list.add(new QuranParaItem(27, "قَالَ فَمَا خَطْبُكُمْ", "ক্বালা ফামা খাতবুকুম", "Qala Fama Khatbukum", 51, 31, 57, 29, "আজ-জারিয়াত ৫১:৩১ - আল-হাদীদ ৫৭:২৯", "Adh-Dhariyat 51:31 - Al-Hadid 57:29"));
        list.add(new QuranParaItem(28, "قَدْ سَمِعَ", "ক্বাদ সামিয়া", "Qad Sami'allah", 58, 1, 66, 12, "আল-মুজাদালাহ ৫৮:১ - আত-তাহরীম ৬৬:১২", "Al-Mujadilah 58:1 - At-Tahrim 66:12"));
        list.add(new QuranParaItem(29, "تَبَارَكَ الَّذِي", "তাবারাকাল্লাজি", "Tabarakallazi", 67, 1, 77, 50, "সূরা আল-মুলক ৬৭:১ - আল-মুরসালাত ৭৭:৫০", "Al-Mulk 67:1 - Al-Mursalat 77:50"));
        list.add(new QuranParaItem(30, "عَمَّ يَتَسَاءَلُونَ", "আম্মা ইয়াতাসা-আলুন", "Amma Yatasa'aloon", 78, 1, 114, 6, "আন-নাবা ৭৮:১ - আন-নাস ১১৪:৬", "An-Naba 78:1 - An-Nas 114:6"));
        return list;
    }

    public static QuranParaItem getParaByNumber(int juzNumber) {
        List<QuranParaItem> all = getAll30Paras();
        if (juzNumber >= 1 && juzNumber <= all.size()) {
            return all.get(juzNumber - 1);
        }
        return all.get(0);
    }
}
