package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: রুকুর তাসবিহ (Tasbih and Supplications of Ruku).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahRukuDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. রুকুর তাসবিহ (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "রুকুর তাসবিহ",
            "Tasbih and Supplications of Ruku",
            "রুকূর জন্য হাদীছে অনেকগুলি দো‘আ এসেছে। তন্মধ্যে রুকূর জন্য বহুল প্রচলিত দো'আ হল - سُبْحَانَ رَبِّيَ الْعَظِيْمِ উচ্চারণ: সুবহা-না রব্বিয়াল ‘আযীম। অর্থ: মহা পবিত্র আমার প্রতিপালক যিনি মহান। [আবুদাঊদ, তিরমিযী, মিশকাত হা/৮৮১] এই দো‘আ তিনবার পড়...",
            "Many supplications have been narrated in authentic Hadiths for Ruku. Among them, the most widely recited Dua for Ruku is: سُبْحَانَ رَبِّيَ الْعَظِيْمِ Transliteration: Subhana Rabbiyal 'Azeem. Meaning: Glory be to my Lord, the Most Great...",
            "রুকূর জন্য হাদীছে অনেকগুলি দো‘আ এসেছে। তন্মধ্যে রুকূর জন্য বহুল প্রচলিত দো'আ হল -\n\n" +
            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহা-না রব্বিয়াল ‘আযীম।\n\n" +
            "অর্থ:\n\n" +
            "মহা পবিত্র আমার প্রতিপালক যিনি মহান।\n\n" +
            "[আবুদাঊদ, তিরমিযী, মিশকাত হা/৮৮১]\n\n" +
            "এই দো‘আ তিনবার পড়বে। বেশির কোন সংখ্যা নির্দিষ্ট নেই। যত বার খুশি পড়তে পারেন।\n\n" +
            "[আহমাদ, আবুদাঊদ হা/৮৮৫; ইবনু মাজাহ হা/৮৮৮; আলবানী, সিফাত, ১১৩ পৃঃ, ‘রুকূর দো‘আ সমূহ’ অনুচ্ছেদ, টীকা-২ ও ৩।]\n\n" +
            "উল্লেখ্য যে, ঊর্ধ্বে দশবার পড়ার হাদীসটি ‘যঈফ’।\n\n" +
            "[তিরমিযী, আবুদাঊদ, নাসাঈ, মিশকাত হা/৮৮০, ৮৮৩ ‘সালাত’ অধ্যায়-৪, ‘রুকূ‘অনুচ্ছেদ-১৩।]\n\n" +
            "তবে রাসূলুল্লাহ (ছাঃ) জীবনের শেষদিকে এসে রুকূ ও সিজদাতে এমনকি সালাতের বাইরে অধিকাংশ সময় নিম্নোক্ত দো‘আটি পড়তেন।-\n\n" +
            "سُبْحَانَكَ اللَّهُمَّ رَبَّنَا وَبِحَمْدِكَ، اَللَّهُمَّ اغْفِرْ لِيْ-\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহ-নাকা আল্লা-হুম্মা রব্বানা ওয়া বিহাম্দিকা, আল্লা হুম্মাগ্ফিরলী।\n\n" +
            "অর্থ:\n\n" +
            "হে আল্লাহ হে আমাদের প্রতিপালক! আপনার প্রশংসার সাথে আপনার পবিত্রতা ঘোষণা করছি। হে আল্লাহ! আপনি আমাকে ক্ষমা করুন!\n\n" +
            "[মুত্তাফাক্ব ‘আলাইহ, মিশকাত হা/৮৭১; নায়লুল আওত্বার ৩/১০৬।]\n\n" +
            "এতদ্ব্যতীত নিম্নেরুকূর অন্যান্য দো‘আ সমূহএকত্রে একই সময়ে কিংবা পৃথকভাবে বিভিন্ন সময়ে পড়া যায়। যেমন-\n\n" +
            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ-\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহানা রাব্বী ইয়াল আযীম ওয়াবেহামদীহী। (তিন বার)।-\n\n" +
            "سُبُّوْحٌ قُدُّوْسٌ رَبُّ الْمَلاَئِكَةِ وَالرُّوْحِ\n\n" +
            "উচ্চারণ:\n\n" +
            "সুব্বুহুন কুদ্দুসুন রাব্বুল মালা-ইকাতি ওয়ার রূহে। (মুসলিম)। -\n\n" +
            "اَللَّهُمَّ لَكَ رَكَعْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ، خَشَعَ لَكَ سَمْعِيْ وَبَصَرِيْ وَمُخِّيْ وَعَظْمِيْ وَعَصَبِيْ- (مسلم وغيره)\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা লাকা রকায়তু, ওয়াবিকা আ-মানাতু, ওয়া লাকা আ'সলামতু, খাশায়া লাকা সাম'ঈ ওয়া বাসরী ওয়া মুখ্খী ওয়া আজমী ওয়া আসবী।\n\n" +
            "[মুসলিম]\n\n" +
            "اَللَّهُمَّ لَكَ رَكَعْتُ وَبِكَ أَسْلَمْتُ وَعَلَيْكَ تَوَكَّلْتُ، اَنْتَ رَبِّى خَشَعَ سَمْعِىْ وَبَصَرِىْ وَدَمِىْ وَلَحْمِىْ وَعَظْمِىْ وَعَصَبِىْ للهِ رَبِّ الْعَالَمِيْنَ-\n\n" +
            "উচ্চারণ:\n\n" +
            "আল্লাহুম্মা লাকা রাকা'তু ওয়া বিকা আসলামতু ওয়া আলাইকা তাওয়াক্কালতু, আনতা রাব্বী খাশায়া সামঈ ওয়া বাসরী ওয়া দামী ওয়া লাহমী ওয়া আজমী ওয়া আসবিল্লাহি রাব্বীল আ-লামী-ন। -\n\n" +
            "سُبْحَانَ ذِي الْجَبَرُوْتِ وَالْمَلَكُوْتِ وَالْكِبْرِيَاءِ وَالْعَظَمَةِ\n\n" +
            "উচ্চারণ:\n\n" +
            "সুবহানাযি জিল জাবারুত, ওয়াল মালাকুত, ওয়াল কিবরিয়াই, ওয়াল আজমাহ।\n\n" +
            "[তিরমিজি,আবু দাউদ]",
            "Many supplications have been narrated in authentic Hadiths for Ruku. Among them, the most widely recited Dua for Ruku is:\n\n" +
            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ\n\n" +
            "Transliteration:\n\n" +
            "Subhana Rabbiyal 'Azeem.\n\n" +
            "Meaning:\n\n" +
            "\"Glory be to my Lord, the Most Great.\"\n\n" +
            "[Abu Dawud, Tirmidhi, Mishkat H/881]\n\n" +
            "This supplication should be recited three times. There is no fixed upper limit; one may recite it as many times as desired.\n\n" +
            "[Ahmad, Abu Dawud H/885; Ibn Majah H/888; Al-Albani, Sifat, p. 113, 'Duas of Ruku' Section, Notes 2 & 3]\n\n" +
            "Note that the Hadith mentioning an upper limit of ten times is 'Da'eef' (weak).\n\n" +
            "[Tirmidhi, Abu Dawud, Nasa'i, Mishkat H/880, 883 'Salah' Chapter 4, 'Ruku' Section 13]\n\n" +
            "However, towards the end of his life, the Messenger of Allah (ﷺ) frequently recited the following Dua during Ruku, Sujood, and even outside of Salah:\n\n" +
            "سُبْحَانَكَ اللَّهُمَّ رَبَّنَا وَبِحَمْدِكَ، اَللَّهُمَّ اغْفِرْ لِيْ-\n\n" +
            "Transliteration:\n\n" +
            "Subhanaka Allahumma Rabbana wa bihamdika, Allahummaghfirli.\n\n" +
            "Meaning:\n\n" +
            "\"Glory be to You, O Allah, our Lord, and all praise is due to You. O Allah, forgive me!\"\n\n" +
            "[Muttafaqun 'Alayh, Mishkat H/871; Naylul Awtar 3/106]\n\n" +
            "Additionally, other supplications of Ruku may be recited together or individually at different times, such as:\n\n" +
            "سُبْحَانَ رَبِّيَ الْعَظِيْمِ وَبِحَمْدِهِ-\n\n" +
            "Transliteration:\n\n" +
            "Subhana Rabbiyal 'Azeemi wa bihamdih. (Three times).\n\n" +
            "سُبُّوْحٌ قُدُّوْسٌ رَبُّ الْمَلاَئِكَةِ وَالرُّوْحِ\n\n" +
            "Transliteration:\n\n" +
            "Subbuhun Quddusun Rabbul-Mala'ikati war-Rooh. (Muslim).\n\n" +
            "اَللَّهُمَّ لَكَ رَكَعْتُ، وَبِكَ آمَنْتُ، وَلَكَ أَسْلَمْتُ، خَشَعَ لَكَ سَمْعِيْ وَبَصَرِيْ وَمُخِّيْ وَعَظْمِيْ وَعَصَبِيْ- (مسلم وغيره)\n\n" +
            "Transliteration:\n\n" +
            "Allahumma laka raka'tu, wa bika aamantu, wa laka aslamtu, khasha'a laka sam'ee wa basaree wa mukhkhee wa 'azmee wa 'asabee.\n\n" +
            "[Muslim]\n\n" +
            "اَللَّهُمَّ لَكَ رَكَعْتُ وَبِكَ أَسْلَمْتُ وَعَلَيْكَ تَوَكَّلْتُ، اَنْتَ رَبِّى خَشَعَ سَمْعِىْ وَبَصَرِىْ وَدَمِىْ وَلَحْمِىْ وَعَظْمِىْ وَعَصَبِىْ للهِ رَبِّ الْعَالَمِيْنَ-\n\n" +
            "Transliteration:\n\n" +
            "Allahumma laka raka'tu wa bika aslamtu wa 'alayka tawakkaltu, Anta Rabbee khasha'a sam'ee wa basaree wa damee wa lahmee wa 'azmee wa 'asabee lillahi Rabbil-'Aalameen.\n\n" +
            "سُبْحَانَ ذِي الْجَبَرُوْتِ وَالْمَلَكُوْتِ وَالْكِبْرِيَاءِ وَالْعَظَمَةِ\n\n" +
            "Transliteration:\n\n" +
            "Subhana Dhil-Jabarooti wal-Malakooti wal-Kibriyaa'i wal-'Azamah.\n\n" +
            "[Tirmidhi, Abu Dawud]"
        ));

        return list;
    }
}
