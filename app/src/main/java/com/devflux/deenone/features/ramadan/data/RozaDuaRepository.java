package com.devflux.deenone.features.ramadan.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Roza & Ramadan Duas.
 * Built with the exact same architecture and model (HajjHistoryCardItem) as Hajj History & Tashahhud.
 */
public final class RozaDuaRepository {

    private RozaDuaRepository() {
        // Utility class
    }

    public static List<HajjHistoryCardItem> getAllDuas() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. তারাবিহ নামাজের চার রাকাত পরপর দোয়া
        list.add(new HajjHistoryCardItem(
                1,
                "তারাবিহ নামাজের চার রাকাত পরপর দোয়া",
                "Dua After Every Four Rak'ahs of Taraweeh",
                "سُبْحَانَ ذِي ٱلْمُلْكِ وَٱلْمَلَكُوتِ، سُبْحَانَ ذِي ٱلْعِزَّةِ وَٱلْجَبَرُوتِ، سُبْحَانَ ٱلْمَلِكِ ٱلْحَيِّ ٱلَّذِي لَا يَنَامُ وَلَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ، رَبُّنَا وَرَبُّ ٱلْمَلَائِكَةِ وَٱلرُّوحِ...",
                "Subhana dhil-mulki wal-malakoot, subhana dhil-izzati wal-jabaroot, subhanal-malikil-hayyil-ladhi la yanamu wa la yamoot...",
                "سُبْحَانَ ذِي ٱلْمُلْكِ وَٱلْمَلَكُوتِ، سُبْحَانَ ذِي ٱلْعِزَّةِ وَٱلْجَبَرُوتِ، سُبْحَانَ ٱلْمَلِكِ ٱلْحَيِّ ٱلَّذِي لَا يَنَامُ وَلَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ، رَبُّنَا وَرَبُّ ٱلْمَلَائِكَةِ وَٱلرُّوحِ، ٱللَّهُمَّ أَجِرْنَا مِنَ ٱلنَّارِ، يَا مُجِيرُ يَا مُجِيرُ يَا مُجِيرُ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "সুব্হা-না জিল্ মূলকি ওয়াল্ মালাকূত, সুব্হা-না জিল্ ইজ্জাতি ওয়াল্ জাবারূত, সুব্হা-নাল্ মালিকিল্ হাইয়িল্ লাযি লা ইয়ানা-মু ওয়ালা ইয়ামূত, সুব্বূহুন্ কুদ্দূসুন্, রাব্বুনা ওয়া রাব্বুল্ মালায়িকাতি ওয়ার্রূহ, আল্লাহুম্মা আজির্না মিনান্ নার, ইয়্যা মুজীরু! ইয়্যা মুজীরু! ইয়্যা মুজীরু!\n\n" +
                        "অর্থ:\n\n" +
                        "পরম মহিমাশালী এবং সার্বভৌমত্বের অধিকারী আল্লাহ পবিত্র। পরম মর্যাদাশীল ও ক্ষমতাধর আল্লাহ পবিত্র। সেই মহান বাদশাহ পবিত্র যিনি চিরঞ্জীব, যিনি ঘুমান না এবং মারা যান না। তিনি সর্বতোভাবে পবিত্র ও পূত-পবিত্র। তিনি আমাদের প্রতিপালক এবং ফেরেশতাদের ও জিবরাঈল (আলাইহিস সালাম)-এরও প্রতিপালক। হে আল্লাহ! আমাদের জাহান্নামের আগুন থেকে রক্ষা করুন। হে রক্ষাকারী! হে রক্ষাকারী! হে রক্ষাকারী!\n\n" +
                        "রেফারেন্স: প্রচলিত দোয়া ও ইসলামিক কিতাবাদি",
                "سُبْحَانَ ذِي ٱلْمُلْكِ وَٱلْمَلَكُوتِ، سُبْحَانَ ذِي ٱلْعِزَّةِ وَٱلْجَبَرُوتِ، سُبْحَانَ ٱلْمَلِكِ ٱلْحَيِّ ٱلَّذِي لَا يَنَامُ وَلَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ، رَبُّنَا وَرَبُّ ٱلْمَلَائِكَةِ وَٱلرُّوحِ، ٱللَّهُمَّ أَجِرْنَا مِنَ ٱلنَّارِ، يَا مُجِيرُ يَا مُجِيرُ يَا مُجِيرُ\n\n" +
                        "Transliteration:\n\n" +
                        "Subhana dhil-mulki wal-malakoot, subhana dhil-izzati wal-jabaroot, subhanal-malikil-hayyil-ladhi la yanamu wa la yamoot, subboohun quddoosun, rabbuna wa rabbul-mala'ikati war-rooh, Allahumma ajirna minan-nar, ya Mujeeru! ya Mujeeru! ya Mujeeru!\n\n" +
                        "Meaning:\n\n" +
                        "Glory be to the Possessor of the kingdom and sovereignty. Glory be to the Possessor of might and majesty. Glory be to the Sovereign King Who is Ever-Living, Who sleeps not and dies not. All-Glorious, All-Holy, our Lord and the Lord of the angels and Gabriel. O Allah! Save us from the fire of Hell. O Protector! O Protector! O Protector!\n\n" +
                        "Reference: Widely Practiced Islamic Supplication"
        ));

        // 2. লাইলাতুল কদরের দোয়া
        list.add(new HajjHistoryCardItem(
                2,
                "লাইলাতুল কদরের দোয়া",
                "Dua for Laylatul Qadr",
                "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي উচ্চারণ: আল্লাহুম্মা ইন্নাকা আফুউউন, তুহিব্বুল আফওয়া, ফা’আফু আন্নি...",
                "Allahumma innaka 'afuwwun tuhibbul-'afwa fa'fu 'anni. O Allah! You are Forgiving, You love forgiveness...",
                "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা ইন্নাকা আফুউউন, তুহিব্বুল আফওয়া, ফা’আফু আন্নি।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! তুমি ক্ষমাশীল, তুমি ক্ষমা করতে ভালোবাসো, আমাকে ক্ষমা করে দাও।\n\n" +
                        "রেফারেন্স: সুনান তিরমিজি ৩৫১৩",
                "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma innaka 'afuwwun, tuhibbul-'afwa, fa'fu 'anni.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! You are Forgiving, You love forgiveness, so forgive me.\n\n" +
                        "Reference: Jami` at-Tirmidhi 3513"
        ));

        // 3. ইফতারের দোয়া
        list.add(new HajjHistoryCardItem(
                3,
                "ইফতারের দোয়া",
                "Dua for Iftar",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ উচ্চারণ: আল্লাহুম্মা লাকা ছুমতু...",
                "Allahumma laka sumtu wa 'ala rizqika aftartu wa birahmatika ya Arhamar-Rahimeen...",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা লাকা ছুমতু ওয়া আলা রিযক্বিকা আফতারতু ওয়া বিরাহমাতিকা ইয়া আরহামার রাহিমীন।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমি আপনার জন্য রোজা রেখেছি এবং আপনারই দেওয়া রিজিক দ্বারা ইফতার করেছি। হে পরম দয়ালু, আপনার রহমতের আশায়।\n\n" +
                        "রেফারেন্স: সুনান আবু দাউদ ২৩৫৮",
                "اللَّهُمَّ لَكَ صُمْتُ وَعَلَىٰ رِزْقِكَ أَفْطَرْتُ وَبِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma laka sumtu wa 'ala rizqika aftartu wa birahmatika ya Arhamar-Rahimeen.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! I fasted for You and I broke my fast with Your provision, and by Your mercy, O Most Merciful of the merciful.\n\n" +
                        "Reference: Sunan Abi Dawud 2358"
        ));

        // 4. ইফতারের পর আল্লাহর শুকরিয়া আদায়ের দোয়া
        list.add(new HajjHistoryCardItem(
                4,
                "ইফতারের পর আল্লাহর শুকরিয়া আদায়ের দোয়া",
                "Dua After Iftar (Expressing Gratitude)",
                "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ উচ্চারণ: যাহাবাজ্ জমা'উ...",
                "Dhahaba adh-dhama'u wabtallatil-'urooqu wa thabatal-ajru in sha Allah...",
                "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "যাহাবাজ্ জমা'উ, ওয়াবতাল্লাতিল উরূকু, ওয়াসাবাতাল অজরু ইন্ শা'আল্লাহ।\n\n" +
                        "অর্থ:\n\n" +
                        "তৃষ্ণা দূর হলো, শিরাগুলো সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান প্রতিষ্ঠিত হলো।\n\n" +
                        "রেফারেন্স: সুনান আবু দাউদ ২৩৫৭, সুনান ইবনে মাজাহ ১৭৫৩",
                "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ العُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ\n\n" +
                        "Transliteration:\n\n" +
                        "Dhahaba adh-dhama'u wabtallatil-'urooqu wa thabatal-ajru in sha Allah.\n\n" +
                        "Meaning:\n\n" +
                        "The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.\n\n" +
                        "Reference: Sunan Abi Dawud 2357, Sunan Ibn Majah 1753"
        ));

        // 5. সেহরির দোয়া
        list.add(new HajjHistoryCardItem(
                5,
                "সেহরির দোয়া",
                "Dua for Sehri",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى...",
                "Nawaytu an asooma ghadan min shahri Ramadan al-mubaraki fardan laka...",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন আছুম্মা গাদাম মিন্ শাহরি রমাজানাল মুবারাকি ফারদাল্লাকা, ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি ইন্নিকা আনতাস্ সামিউল আলিম।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমি আগামীকাল পবিত্র মাহে রমজানের নির্ধারিত ফরজ রোজা রাখার নিয়ত করলাম। অতএব তুমি আমার রোযা তথা পানাহার থেকে বিরত থাকাকে কবুল কর, নিশ্চয়ই তুমি সর্বশ্রোতা ও সর্বজ্ঞানী।\n\n" +
                        "রেফারেন্স: ফিকহ শাস্ত্রের বহুল প্রচলিত দোয়া",
                "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمْضَانَ الْمُبَارَكِ فَرْضَا لَكَ يَا اللهُ فَتَقَبَّل مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an asooma ghadan min shahri Ramadan al-mubaraki fardan laka, ya Allahu fataqabbal minnee, innaka antas-Samee'ul-'Aleem.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! I intend to observe tomorrow's obligatory fast of the blessed month of Ramadan for You. Therefore accept from me, indeed You are the All-Hearing, All-Knowing.\n\n" +
                        "Reference: Widely Practiced in Fiqh Compilations"
        ));

        // 6. রোজার নিয়ত
        list.add(new HajjHistoryCardItem(
                6,
                "রোজার নিয়ত",
                "Niyyah (Intention) for Fasting",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي...",
                "Nawaytu an asooma ghadan min shahri Ramadan al-mubarak fardan laka ya Allahu...",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন আসূমা গাদান মিন শাহরি রামাদানাল মুবারাক ফারদাল্লাকা ইয়া আল্লাহু ফাতাকাব্বাল মিন্‌নি ইন্নাকা আনতাস সামীউল আলীম।\n\n" +
                        "অর্থ:\n\n" +
                        "আমি নিয়ত করলাম আগামীকাল রমজান মাসের ফরজ রোজা আপনার জন্য রাখবো, হে আল্লাহ। আপনি তা কবুল করুন। নিশ্চয়ই আপনি সর্বশ্রোতা, সর্বজ্ঞানী।\n\n" +
                        "রেফারেন্স: ফিকহ শাস্ত্র ও হাদিসের মূলনীতি",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ فَتَقَبَّلْ مِنِّي إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an asooma ghadan min shahri Ramadan al-mubarak fardan laka ya Allahu fataqabbal minnee innaka antas-Samee'ul-'Aleem.\n\n" +
                        "Meaning:\n\n" +
                        "I intend to observe tomorrow's obligatory fast of the blessed month of Ramadan for You, O Allah. Therefore accept from me, indeed You are the All-Hearing, All-Knowing.\n\n" +
                        "Reference: Principles of Fiqh & Sunnah"
        ));

        // 7. তারাবির নিয়ত
        list.add(new HajjHistoryCardItem(
                7,
                "তারাবির নিয়ত",
                "Niyyah (Intention) for Taraweeh Prayer",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى...",
                "Nawaytu an usalliya lillahi Ta'ala rak'atay salatit-Taraweehi...",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "নাওয়াইতু আন উসাল্লিয়া লিল্লাহি তা‘আলা রাকআতাই সালাতিত তারাবীহি সুন্নাতা রাসূলিল্লাহি তা‘আলা মুতাওয়াজ্জিহান ইলা জিহাতিল কা‘বাতিশ শরীফা, আল্লাহু আকবার।\n\n" +
                        "অর্থ:\n\n" +
                        "আমি আল্লাহ তা‘আলার জন্য দুই রাকাত তারাবির নামাজ (রাসূল ﷺ-এর সুন্নাহ) কাবামুখী হয়ে আদায়ের নিয়ত করছি, আল্লাহু আকবার।\n\n" +
                        "রেফারেন্স: ফিকহ শাস্ত্রের প্রচলিত নিয়ম",
                "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى رَكْعَتَيْ صَلَاةِ التَّرَاوِيحِ سُنَّةَ رَسُولِ اللَّهِ تَعَالَى مُتَوَجِّهًا إِلَى جِهَةِ الْكَعْبَةِ الشَّرِيفَةِ، اللَّهُ أَكْبَرُ\n\n" +
                        "Transliteration:\n\n" +
                        "Nawaytu an usalliya lillahi Ta'ala, rak'atay salatit-Taraweehi sunnatu Rasoolillahi Ta'ala, mutawajjihan ila jihatil-Ka'batish-Shareefati, Allahu Akbar.\n\n" +
                        "Meaning:\n\n" +
                        "Facing the Qiblah, I intend to perform two Rak'ahs of the Sunnah Taraweeh prayer for the sake of Allah the Almighty, following the Sunnah of the Messenger of Allah; Allahu Akbar.\n\n" +
                        "Reference: Standard Fiqh Practice"
        ));

        // 8. ইফতারের আগের মুহূর্তের দোয়া
        list.add(new HajjHistoryCardItem(
                8,
                "ইফতারের আগের মুহূর্তের দোয়া",
                "Dua Just Before Iftar",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ الَّتِي وَسِعَتْ كُلَّ شَيْءٍ أَنْ تَغْفِرَ لِي উচ্চারণ: আল্লাহুম্মা ইন্নি আসআলুকা...",
                "Allahumma innee as'aluka bi-rahmatikal-latee wasi'at kulla shay'in an taghfira lee...",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ الَّتِي وَسِعَتْ كُلَّ شَيْءٍ أَنْ تَغْفِرَ لِي\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা ইন্নি আসআলুকা বিরাহমাতিকাল্লাতি ওসিআত কুল্লা শাই’ ইন আন তাগফিরা লি।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমি তোমার সেই রহমতের উসিলায় তোমার কাছে প্রার্থনা করছি যা সবকিছুকে পরিবেষ্টন করে রেখেছে, যেন তুমি আমাকে ক্ষমা করে দাও।\n\n" +
                        "রেফারেন্স: সুনান ইবনে মাজাহ ১৭৫৩",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ الَّتِي وَسِعَتْ كُلَّ شَيْءٍ أَنْ تَغْفِرَ لِي\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma innee as'aluka bi-rahmatikal-latee wasi'at kulla shay'in an taghfira lee.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! I ask You by Your mercy which encompasses all things, that You forgive me.\n\n" +
                        "Reference: Sunan Ibn Majah 1753"
        ));

        // 9. নতুন চাঁদ দেখে পড়ার দোয়া
        list.add(new HajjHistoryCardItem(
                9,
                "নতুন চাঁদ দেখে পড়ার দোয়া",
                "Dua upon Sighting the Crescent Moon",
                "اللَّهُمَّ أَهِلَّهُ عَلَيْنَا بِالْأَمْنِ وَالْإِيمَانِ، وَالسَّلَامَةِ وَالْإِسْلَامِ، وَالتَّوْفِيقِ لِمَا تُحِبُّ وَتَرْضَى...",
                "Allahumma ahillahu 'alayna bil-amni wal-iman, was-salamati wal-Islam...",
                "اللَّهُمَّ أَهِلَّهُ عَلَيْنَا بِالْأَمْنِ وَالْإِيمَانِ، وَالسَّلَامَةِ وَالْإِسْلَامِ، وَالتَّوْفِيقِ لِمَا تُحِبُّ وَتَرْضَى، رَبِّي وَرَبُّكَ اللَّهُ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা আহিল্লাহু ‘আলাইনা বিল-আমনি ওয়াল-ইমানি, ওয়াস-সালামাতি ওয়াল-ইসলামি, ওয়াত-তাওফিকি লিমা তুহিব্বু ওয়া তারদা, রাব্বি ওয়া রাব্বুকাল্লাহ।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! এই চাঁদকে আমাদের জন্য নিরাপত্তা, ঈমান, শান্তি ও ইসলামসহ উদিত করুন। এবং আমাদের সেই কাজের তাওফিক দিন যা আপনি ভালোবাসেন ও যা আপনাকে সন্তুষ্ট করে। (হে চাঁদ!) আমার এবং তোমার প্রভু হলেন আল্লাহ।\n\n" +
                        "রেফারেন্স: সুনান তিরমিজি ৩৪৫১",
                "اللَّهُمَّ أَهِلَّهُ عَلَيْنَا بِالْأَمْنِ وَالْإِيمَانِ، وَالسَّلَامَةِ وَالْإِسْلَامِ، وَالتَّوْفِيقِ لِمَا تُحِبُّ وَتَرْضَى، رَبِّي وَرَبُّكَ اللَّهُ\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma ahillahu 'alayna bil-amni wal-iman, was-salamati wal-Islam, wat-tawfeeqi lima tuhibbu wa tarda, rabbee wa rabbuk-Allah.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! Let this crescent moon appear over us with security, faith, safety, and Islam. And grant us the ability to do what You love and what pleases You. My Lord and your Lord is Allah.\n\n" +
                        "Reference: Jami` at-Tirmidhi 3451"
        ));

        // 10. রজব ও শা’বান মাসের দোয়া
        list.add(new HajjHistoryCardItem(
                10,
                "রজব ও শা’বান মাসের দোয়া",
                "Dua for Rajab and Sha'ban",
                "اللَّهُمَّ بَارِكْ لَنَا فِي رَجَبَ وَشَعْبَانَ وَبَلِّغْنَا رَمَضَانَ উচ্চারণ: আল্লাহুম্মা বারিক লানা ফী রজাবা...",
                "Allahumma baarik lana fee Rajaba wa Sha'bana wa ballighna Ramadana...",
                "اللَّهُمَّ بَارِكْ لَنَا فِي رَجَبَ وَشَعْبَانَ وَبَلِّغْنَا رَمَضَانَ\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা বারিক লানা ফী রজাবা ওয়া শা‘বানা ওয়া বাল্লিগনা রমাযান\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমাদের জন্য রজব ও শা’বান মাসকে বরকতময় করুন এবং আমাদেরকে রমজান পর্যন্ত পৌঁছে দিন।\n\n" +
                        "রেফারেন্স: মুসনাদে আহমাদ ২৩৪৬",
                "اللَّهُمَّ بَارِكْ لَنَا فِي رَجَبَ وَشَعْبَانَ وَبَلِّغْنَا رَمَضَانَ\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma baarik lana fee Rajaba wa Sha'bana wa ballighna Ramadana.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! Make the months of Rajab and Sha'ban blessed for us, and let us reach Ramadan.\n\n" +
                        "Reference: Musnad Ahmad 2346"
        ));

        // 11. রমজান মাসে পড়ার দোয়া
        list.add(new HajjHistoryCardItem(
                11,
                "রমজান মাসে পড়ার দোয়া",
                "Dua for the Month of Ramadan",
                "اللّهُمَّ بَارِكْ لَنَا فِي رَمَضَانَ وَأَعِنَّا عَلَى صِيَامِهِ وَقِيَامِهِ وَتَقَبَّلْهُ مِنَّا...",
                "Allahumma barik lana fee Ramadana, wa a'inna 'ala siyamihi wa qiyamihi...",
                "اللّهُمَّ بَارِكْ لَنَا فِي رَمَضَانَ وَأَعِنَّا عَلَى صِيَامِهِ وَقِيَامِهِ وَتَقَبَّلْهُ مِنَّا\n\n" +
                        "উচ্চারণ:\n\n" +
                        "আল্লাহুম্মা বারিক লানা ফি রমাদান, ওয়া আ’ইন্না আলা সিয়ামিহি ওয়া কিয়ামিহি, ওয়া তাক্বব্বালহু মিন্না।\n\n" +
                        "অর্থ:\n\n" +
                        "হে আল্লাহ! আমাদের জন্য রমজান মাস বরকতময় করুন, আমাদের রোজা ও নামাজ আদায়ে সাহায্য করুন এবং আমাদের থেকে তা কবুল করুন।\n\n" +
                        "রেফারেন্স: হিলয়াতুল আওলিয়া ও প্রচলিত নেক দোয়া",
                "اللّهُمَّ بَارِكْ لَنَا فِي رَمَضَانَ وَأَعِنَّا عَلَى صِيَامِهِ وَقِيَامِهِ وَتَقَبَّلْهُ مِنَّا\n\n" +
                        "Transliteration:\n\n" +
                        "Allahumma barik lana fee Ramadana, wa a'inna 'ala siyamihi wa qiyamihi, wa taqabbalhu minna.\n\n" +
                        "Meaning:\n\n" +
                        "O Allah! Bless the month of Ramadan for us, assist us in fasting and prayer, and accept it from us.\n\n" +
                        "Reference: Hilyat al-Awliya & Islamic Supplications"
        ));

        return list;
    }
}
