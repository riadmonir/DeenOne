package com.devflux.deenone.utils;

import java.util.HashMap;
import java.util.Map;

public class QuranTransliterationUtil {

    private static final Map<String, String> COMMON_ARABIC_TO_BN = new HashMap<>();
    private static final Map<String, String> COMMON_ARABIC_TO_EN = new HashMap<>();

    static {
        // Common Bismillah and Surah Al-Fatiha
        COMMON_ARABIC_TO_BN.put("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "বিসমিল্লাহির রাহমানির রাহিম");
        COMMON_ARABIC_TO_EN.put("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "Bismillaahir-Rahmaanir-Raheem");

        COMMON_ARABIC_TO_BN.put("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "আলহামদু লিল্লাহি রাব্বিল আলামিন");
        COMMON_ARABIC_TO_EN.put("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-hamdu lillaahi Rabbil-'aalameen");

        COMMON_ARABIC_TO_BN.put("الرَّحْمَٰنِ الرَّحِيمِ", "আর-রাহমানির রাহিম");
        COMMON_ARABIC_TO_EN.put("الرَّحْمَٰنِ الرَّحِيمِ", "Ar-Rahmaanir-Raheem");

        COMMON_ARABIC_TO_BN.put("مَالِكِ يَوْمِ الدِّينِ", "মালিকি ইয়াওমিদ্দিন");
        COMMON_ARABIC_TO_EN.put("مَالِكِ يَوْمِ الدِّينِ", "Maaliki Yawmid-Deen");

        COMMON_ARABIC_TO_BN.put("إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাস্তাঈন");
        COMMON_ARABIC_TO_EN.put("إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaaka na'budu wa iyyaaka nasta'een");

        COMMON_ARABIC_TO_BN.put("اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "ইহদিনাস সিরাতাল মুস্তাক্বিম");
        COMMON_ARABIC_TO_EN.put("اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinas-Siraatal-Mustaqeem");

        COMMON_ARABIC_TO_BN.put("صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "সিরাতাল্লাযিনা আন'আমতা আলাইহিম, গাইরিল মাগদুবি আলাইহিম ওয়ালাদ্দাল্লিন");
        COMMON_ARABIC_TO_EN.put("صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Siraatalladheena an'amta 'alayhim, ghayril-maghdoobi 'alayhim wa lad-daalleen");

        // Muqatta'at Letters (Surah Al-Baqarah, Aal-E-Imran, Maryam, Ya-Sin, etc.)
        String[] alifLamMeemAr = {"الٓمٓ", "الم", "الۤمۤ", "الٓم"};
        for (String ar : alifLamMeemAr) {
            COMMON_ARABIC_TO_BN.put(ar, "আলিফ-লাম-মীম");
            COMMON_ARABIC_TO_EN.put(ar, "Alif-Laaam-Meeem");
        }
        COMMON_ARABIC_TO_BN.put("الٓمٓصٓ", "আলিফ-লাম-মীম-সোয়াদ");
        COMMON_ARABIC_TO_BN.put("المص", "আলিফ-লাম-মীম-সোয়াদ");
        COMMON_ARABIC_TO_EN.put("الٓمٓصٓ", "Alif-Laaam-Meeem-Saad");
        COMMON_ARABIC_TO_EN.put("المص", "Alif-Laaam-Meeem-Saad");

        COMMON_ARABIC_TO_BN.put("الٓر", "আলিফ-লাম-রা");
        COMMON_ARABIC_TO_BN.put("الر", "আলিফ-লাম-রা");
        COMMON_ARABIC_TO_EN.put("الٓر", "Alif-Laaam-Raa");
        COMMON_ARABIC_TO_EN.put("الر", "Alif-Laaam-Raa");

        COMMON_ARABIC_TO_BN.put("الٓمٓر", "আলিফ-লাম-মীম-রা");
        COMMON_ARABIC_TO_BN.put("المر", "আলিফ-লাম-মীম-রা");
        COMMON_ARABIC_TO_EN.put("الٓمٓر", "Alif-Laaam-Meeem-Raa");
        COMMON_ARABIC_TO_EN.put("المر", "Alif-Laaam-Meeem-Raa");

        COMMON_ARABIC_TO_BN.put("كٓهيعٓصٓ", "কাফ-হা-ইয়া-আইন-সোয়াদ");
        COMMON_ARABIC_TO_BN.put("كهيعص", "কাফ-হা-ইয়া-আইন-সোয়াদ");
        COMMON_ARABIC_TO_EN.put("كٓهيعٓصٓ", "Kaaf-Haa-Yaa-'Ayn-Saad");
        COMMON_ARABIC_TO_EN.put("كهيعص", "Kaaf-Haa-Yaa-'Ayn-Saad");

        COMMON_ARABIC_TO_BN.put("طـٰه", "ত্বা-হা");
        COMMON_ARABIC_TO_BN.put("طه", "ত্বা-হা");
        COMMON_ARABIC_TO_EN.put("طـٰه", "Taa-Haa");
        COMMON_ARABIC_TO_EN.put("طه", "Taa-Haa");

        COMMON_ARABIC_TO_BN.put("طسٓمٓ", "ত্বা-সীন-মীম");
        COMMON_ARABIC_TO_BN.put("طسم", "ত্বা-সীন-মীম");
        COMMON_ARABIC_TO_EN.put("طسٓمٓ", "Taa-Seeem-Meeem");
        COMMON_ARABIC_TO_EN.put("طسم", "Taa-Seeem-Meeem");

        COMMON_ARABIC_TO_BN.put("طسٓ", "ত্বা-সীন");
        COMMON_ARABIC_TO_BN.put("طس", "ত্বা-সীন");
        COMMON_ARABIC_TO_EN.put("طسٓ", "Taa-Seeen");
        COMMON_ARABIC_TO_EN.put("طس", "Taa-Seeen");

        COMMON_ARABIC_TO_BN.put("يسٓ", "ইয়া-সীন");
        COMMON_ARABIC_TO_BN.put("يس", "ইয়া-সীন");
        COMMON_ARABIC_TO_EN.put("يسٓ", "Yaa-Seeen");
        COMMON_ARABIC_TO_EN.put("يس", "Yaa-Seeen");

        COMMON_ARABIC_TO_BN.put("صٓ", "সোয়াদ");
        COMMON_ARABIC_TO_BN.put("ص", "সোয়াদ");
        COMMON_ARABIC_TO_EN.put("صٓ", "Saad");
        COMMON_ARABIC_TO_EN.put("ص", "Saad");

        COMMON_ARABIC_TO_BN.put("حمٓ", "হা-মীম");
        COMMON_ARABIC_TO_BN.put("حم", "হা-মীম");
        COMMON_ARABIC_TO_EN.put("حمٓ", "Haa-Meeem");
        COMMON_ARABIC_TO_EN.put("حم", "Haa-Meeem");

        COMMON_ARABIC_TO_BN.put("حمٓ عسٓقٓ", "হা-মীম, আইন-সীন-ক্বাফ");
        COMMON_ARABIC_TO_BN.put("حم عسق", "হা-মীম, আইন-সীন-ক্বাফ");
        COMMON_ARABIC_TO_EN.put("حمٓ عسٓقٓ", "Haa-Meeem, 'Ayn-Seeen-Qaaf");
        COMMON_ARABIC_TO_EN.put("حم عسق", "Haa-Meeem, 'Ayn-Seeen-Qaaf");

        COMMON_ARABIC_TO_BN.put("قٓ", "ক্বাফ");
        COMMON_ARABIC_TO_BN.put("ق", "ক্বাফ");
        COMMON_ARABIC_TO_EN.put("قٓ", "Qaaf");
        COMMON_ARABIC_TO_EN.put("ق", "Qaaf");

        COMMON_ARABIC_TO_BN.put("نٓ", "নূন");
        COMMON_ARABIC_TO_BN.put("ن", "নূন");
        COMMON_ARABIC_TO_EN.put("نٓ", "Noon");
        COMMON_ARABIC_TO_EN.put("ن", "Noon");
    }

    public static String getPronunciation(String arabicText, String storedBnTransliteration, boolean isBengali) {
        if (arabicText == null) arabicText = "";
        String cleanArabic = arabicText.replaceAll("[\u06DD\u06E2\u06D6-\u06DC\u06DF-\u06E1\u06E3-\u06EA٠-٩0-9]", "").trim();

        if (isBengali) {
            if (storedBnTransliteration != null && !storedBnTransliteration.trim().isEmpty()) {
                return storedBnTransliteration.trim();
            }
            if (COMMON_ARABIC_TO_BN.containsKey(cleanArabic)) {
                return COMMON_ARABIC_TO_BN.get(cleanArabic);
            }
            return transliterateArabicToBengali(cleanArabic);
        } else {
            if (COMMON_ARABIC_TO_EN.containsKey(cleanArabic)) {
                return COMMON_ARABIC_TO_EN.get(cleanArabic);
            }
            if (storedBnTransliteration != null && !storedBnTransliteration.trim().isEmpty()) {
                return transliterateBengaliToEnglish(storedBnTransliteration.trim());
            }
            return transliterateArabicToEnglish(cleanArabic);
        }
    }

    public static String transliterateArabicToBengali(String arabic) {
        if (arabic == null || arabic.trim().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        String[] words = arabic.split("\\s+");
        for (int w = 0; w < words.length; w++) {
            if (w > 0) sb.append(" ");
            sb.append(transliterateWordToBengali(words[w]));
        }
        return sb.toString().replaceAll("্ ", " ").replaceAll("্$", "").trim();
    }

    private static String transliterateWordToBengali(String word) {
        if (word == null || word.isEmpty()) return "";
        if (word.equals("اللَّهِ") || word.equals("اللَّهُ") || word.equals("اللَّهَ") || word.equals("لِلَّهِ")) {
            return word.startsWith("لِ") ? "লিল্লাহ" : "আল্লাহ";
        }
        if (word.startsWith("الرَّحْمَٰنِ")) return "আর-রাহমান";
        if (word.startsWith("الرَّحِيمِ")) return "আর-রাহিম";

        StringBuilder sb = new StringBuilder();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);

            // Harakat / Tashkeel
            if (c == '\u064E') { // Fathah
                sb.append("া");
            } else if (c == '\u0650') { // Kasrah
                sb.append("ি");
            } else if (c == '\u064F') { // Dammah
                sb.append("ু");
            } else if (c == '\u064B') { // Fathatan
                sb.append("ান");
            } else if (c == '\u064D') { // Kasratan
                sb.append("িন");
            } else if (c == '\u064C') { // Dammatan
                sb.append("ুন");
            } else if (c == '\u0652') { // Sukun
                sb.append("্");
            } else if (c == '\u0651') { // Shaddah
                if (sb.length() > 0) {
                    char prev = sb.charAt(sb.length() - 1);
                    if (prev != 'া' && prev != 'ি' && prev != 'ু' && prev != '্') {
                        sb.append(prev);
                    }
                }
            } else if (c == '\u0670' || c == '\u0653') { // Dagger Alif / Maddah
                sb.append("া");
            } else {
                // Arabic Consonants
                switch (c) {
                    case '\u0621': // Hamza
                    case '\u0623': // Alif with Hamza Above
                    case '\u0625': // Alif with Hamza Below
                    case '\u0622': // Alif Maddah
                    case '\u0671': // Alif Wasla
                    case '\u0627': // Alif
                        if (sb.length() == 0) sb.append("আ");
                        else sb.append("া");
                        break;
                    case '\u0628': sb.append("ব"); break; // Baa
                    case '\u062A': sb.append("ত"); break; // Taa
                    case '\u062B': sb.append("ছ"); break; // Thaa
                    case '\u062C': sb.append("জ"); break; // Jeem
                    case '\u062D': sb.append("হ"); break; // Haa
                    case '\u062E': sb.append("খ"); break; // Khaa
                    case '\u062F': sb.append("দ"); break; // Daal
                    case '\u0630': sb.append("য"); break; // Thaal
                    case '\u0631': sb.append("র"); break; // Raa
                    case '\u0632': sb.append("য"); break; // Zaa
                    case '\u0633': sb.append("স"); break; // Seen
                    case '\u0634': sb.append("শ"); break; // Sheen
                    case '\u0635': sb.append("স"); break; // Saad
                    case '\u0636': sb.append("দ"); break; // Daad
                    case '\u0637': sb.append("ত"); break; // Taa
                    case '\u0638': sb.append("য"); break; // Zaa
                    case '\u0639': sb.append("'আ"); break; // 'Ayn
                    case '\u063A': sb.append("গ"); break; // Ghayn
                    case '\u0641': sb.append("ফ"); break; // Faa
                    case '\u0642': sb.append("ক"); break; // Qaaf
                    case '\u0643': sb.append("ক"); break; // Kaaf
                    case '\u0644': sb.append("ল"); break; // Laam
                    case '\u0645': sb.append("ম"); break; // Meem
                    case '\u0646': sb.append("ন"); break; // Noon
                    case '\u0647': sb.append("হ"); break; // Haa
                    case '\u0648': sb.append("ওয়া"); break; // Waaw
                    case '\u064A': // Yaa
                    case '\u0649': // Alif Maqsurah
                        if (sb.length() == 0) sb.append("ই");
                        else sb.append("য়");
                        break;
                    case '\u0629': sb.append("হ"); break; // Taa Marbutah
                }
            }
        }
        return sb.toString();
    }

    public static String transliterateArabicToEnglish(String arabic) {
        if (arabic == null || arabic.trim().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        String[] words = arabic.split("\\s+");
        for (int w = 0; w < words.length; w++) {
            if (w > 0) sb.append(" ");
            sb.append(transliterateWordToEnglish(words[w]));
        }
        return sb.toString().trim();
    }

    private static String transliterateWordToEnglish(String word) {
        if (word == null || word.isEmpty()) return "";
        if (word.equals("اللَّهِ") || word.equals("اللَّهُ") || word.equals("اللَّهَ")) return "Allah";
        if (word.equals("لِلَّهِ")) return "Lillah";

        StringBuilder sb = new StringBuilder();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);

            // Harakat / Tashkeel
            if (c == '\u064E') { // Fathah
                sb.append("a");
            } else if (c == '\u0650') { // Kasrah
                sb.append("i");
            } else if (c == '\u064F') { // Dammah
                sb.append("u");
            } else if (c == '\u064B') { // Fathatan
                sb.append("an");
            } else if (c == '\u064D') { // Kasratan
                sb.append("in");
            } else if (c == '\u064C') { // Dammatan
                sb.append("un");
            } else if (c == '\u0652') { // Sukun
                // stop
            } else if (c == '\u0651') { // Shaddah
                if (sb.length() > 0) {
                    char prev = sb.charAt(sb.length() - 1);
                    if (Character.isLetter(prev) && prev != 'a' && prev != 'i' && prev != 'u') {
                        sb.append(prev);
                    }
                }
            } else if (c == '\u0670' || c == '\u0653') { // Dagger Alif / Maddah
                sb.append("aa");
            } else {
                switch (c) {
                    case '\u0621':
                    case '\u0623':
                    case '\u0625':
                    case '\u0622':
                    case '\u0671':
                    case '\u0627':
                        if (sb.length() == 0) sb.append("A");
                        else sb.append("a");
                        break;
                    case '\u0628': sb.append("b"); break;
                    case '\u062A': sb.append("t"); break;
                    case '\u062B': sb.append("th"); break;
                    case '\u062C': sb.append("j"); break;
                    case '\u062D': sb.append("h"); break;
                    case '\u062E': sb.append("kh"); break;
                    case '\u062F': sb.append("d"); break;
                    case '\u0630': sb.append("dh"); break;
                    case '\u0631': sb.append("r"); break;
                    case '\u0632': sb.append("z"); break;
                    case '\u0633': sb.append("s"); break;
                    case '\u0634': sb.append("sh"); break;
                    case '\u0635': sb.append("s"); break;
                    case '\u0636': sb.append("d"); break;
                    case '\u0637': sb.append("t"); break;
                    case '\u0638': sb.append("z"); break;
                    case '\u0639': sb.append("'a"); break;
                    case '\u063A': sb.append("gh"); break;
                    case '\u0641': sb.append("f"); break;
                    case '\u0642': sb.append("q"); break;
                    case '\u0643': sb.append("k"); break;
                    case '\u0644': sb.append("l"); break;
                    case '\u0645': sb.append("m"); break;
                    case '\u0646': sb.append("n"); break;
                    case '\u0647': sb.append("h"); break;
                    case '\u0648': sb.append("w"); break;
                    case '\u064A':
                    case '\u0649':
                        if (sb.length() == 0) sb.append("Y");
                        else sb.append("y");
                        break;
                    case '\u0629': sb.append("h"); break;
                }
            }
        }
        String res = sb.toString();
        if (!res.isEmpty()) {
            res = Character.toUpperCase(res.charAt(0)) + res.substring(1);
        }
        return res;
    }

    public static String transliterateBengaliToEnglish(String bn) {
        if (bn == null || bn.trim().isEmpty()) return "";
        return bn
                .replace("বিসমিল্লাহির রাহমানির রাহিম", "Bismillahir Rahmanir Raheem")
                .replace("আলহামদু লিল্লাহি রাব্বিল আলামিন", "Al-hamdu lillahi Rabbil 'Aalameen")
                .replace("আর-রাহমানির রাহিম", "Ar-Rahmanir Raheem")
                .replace("মালিকি ইয়াওমিদ্দিন", "Maaliki Yawmid-Deen")
                .replace("ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাস্তাঈন", "Iyyaka na'budu wa iyyaka nasta'een")
                .replace("ইহদিনাস সিরাতাল মুস্তাক্বিম", "Ihdinas-Siraatal-Mustaqeem")
                .replace("সিরাতাল্লাযিনা আন'আমতা আলাইহিম, গাইরিল মাগদুবি আলাইহিম ওয়ালাদ্দাল্লিন", "Siraatalladheena an'amta 'alayhim, ghayril-maghdoobi 'alayhim wa lad-daalleen")
                .replace("আল্লাহু", "Allahu")
                .replace("আল্লাহ", "Allah")
                .replace("রাব্বিল", "Rabbil")
                .replace("আলামিন", "'Aalameen")
                .replace("রাহিম", "Raheem")
                .replace("রাহমান", "Rahman");
    }
}
