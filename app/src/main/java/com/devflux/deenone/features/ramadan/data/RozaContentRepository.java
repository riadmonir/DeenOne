package com.devflux.deenone.features.ramadan.data;

import java.util.HashMap;
import java.util.Map;

public class RozaContentRepository {

    public static class TopicContent {
        public final String id;
        public final String titleBn;
        public final String titleEn;
        public final String categoryBn;
        public final String categoryEn;
        public final String arabicDua;
        public final String transliterationBn;
        public final String transliterationEn;
        public final String translationBn;
        public final String translationEn;
        public final String detailsBn;
        public final String detailsEn;
        public final String referenceBn;
        public final String referenceEn;

        public TopicContent(
                String id, String titleBn, String titleEn, String categoryBn, String categoryEn,
                String arabicDua, String transliterationBn, String transliterationEn,
                String translationBn, String translationEn, String detailsBn, String detailsEn,
                String referenceBn, String referenceEn
        ) {
            this.id = id;
            this.titleBn = titleBn;
            this.titleEn = titleEn;
            this.categoryBn = categoryBn;
            this.categoryEn = categoryEn;
            this.arabicDua = arabicDua;
            this.transliterationBn = transliterationBn;
            this.transliterationEn = transliterationEn;
            this.translationBn = translationBn;
            this.translationEn = translationEn;
            this.detailsBn = detailsBn;
            this.detailsEn = detailsEn;
            this.referenceBn = referenceBn;
            this.referenceEn = referenceEn;
        }

        public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
        public String getCategory(boolean isBn) { return isBn ? categoryBn : categoryEn; }
        public String getTransliteration(boolean isBn) { return isBn ? transliterationBn : transliterationEn; }
        public String getTranslation(boolean isBn) { return isBn ? translationBn : translationEn; }
        public String getDetails(boolean isBn) { return isBn ? detailsBn : detailsEn; }
        public String getReference(boolean isBn) { return isBn ? referenceBn : referenceEn; }
    }

    private static final Map<String, TopicContent> CONTENT_MAP = new HashMap<>();

    static {
        // 1. রোজার দোয়া
        CONTENT_MAP.put("roza_dua", new TopicContent(
                "roza_dua",
                "রোজার দোয়া ও নিয়ত",
                "Duas of Fasting and Intention",
                "দোয়া ও নিয়ত",
                "Supplications",
                "نَوَيْتُ أَنْ أَصُومَ غَدًا عَنْ أَدَاءِ فَرْضِ رَمَضَانَ هَذِهِ السَّنَةِ لِلَّهِ تَعَالَى\n\nذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
                "নাওয়াইতু আন আসুমা গাদাম মিন শাহরি রামাদানাল মুবারাকি ফারদাল্লাকা ইয়া আল্লাহু ফাতাকাব্বাল মিন্নি।\n\nজাহাবায জামাউ ওয়াবতাল্লাতিল উরুকু ওয়া সাবাতাল আজরু ইনশাআল্লাহ।",
                "Nawaytu an asuma ghadan 'an adai fardi Ramadana hadhihis sanati lillahi Ta'ala.\n\nDhahaba adh-dhama'u wabtallatil 'urooqu wa thabatal ajru in sha Allah.",
                "হে আল্লাহ! আমি আগামীকাল তোমার সন্তুষ্টির উদ্দেশ্যে রমজান মাসের ফরজ রোজা রাখার নিয়ত করলাম। অতএব আমার পক্ষ থেকে তা কবুল করুন।\n\nপিপাসা দূর হলো, শিরা-উপশিরা সতেজ হলো এবং ইনশাআল্লাহ প্রতিদান নিশ্চিত হলো।",
                "O Allah, I intend to fast tomorrow to fulfill the obligation of this Ramadan for Your sake, so accept it from me.\n\nThe thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
                "১. নিয়তের স্থান অন্তর: মুখে উচ্চারণ করে নিয়ত করা বাধ্যতামূলক নয়, তবে অন্তরের সংকল্পই মূল নিয়ত।\n২. সেহরি খাওয়ার উদ্দেশ্যই মূলত রোজার নিয়ত গণ্য হয়।\n৩. ইফতারের সময় বিশুদ্ধ হাদিসে বর্ণিত দোয়া পাঠ করা সুন্নত। রাসুলুল্লাহ ﷺ ইফতারের পর এই দোয়া পাঠ করতেন।",
                "1. The place of intention (Niyyah) is in the heart. Verbal articulation is not mandatory.\n2. Waking up and eating Sehri inherently serves as intention for fasting.\n3. Reciting the authentic supplication immediately after breaking the fast is an established Sunnah.",
                "সহীহ সুনান আবু দাউদ ২৩৫৭, আল-আহকাম",
                "Sunan Abi Dawud 2357"
        ));

        // 2. রোজার বয়ান ভিডিও
        CONTENT_MAP.put("roza_bayan", new TopicContent(
                "roza_bayan",
                "রোজার বয়ান ও নসিহত",
                "Ramadan Islamic Lectures",
                "লেকচার ও বয়ান",
                "Lectures",
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
                "ইয়া আইয়্যুহাল্লাযিনা আমানু কুতিবা আলাইকুমুস সিয়ামু কামা কুতিবা আলাল্লাযিনা মিন কাবলিকুম লাআল্লাকুম তাত্তাকুন।",
                "Ya ayyuhalladhina amanu kutiba 'alaikumus-siyamu kama kutiba 'alalladhina min qablikum la'allakum tattaqun.",
                "হে মুমিনগণ! তোমাদের ওপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের ওপর; যেন তোমরা তাকওয়া অর্জন করতে পারো।",
                "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.",
                "রমজানের প্রতিটি দিনকে আত্মশুদ্ধি ও গুনাহ মাফের শ্রেষ্ঠ সুযোগ হিসেবে গ্রহণ করতে হবে। প্রখ্যাত আলেমদের নির্বাচিত রমজান নির্দেশিকা, তারাবীহ ও লাইলাতুল কদরের তাৎপর্য বিষয়ক দিকনির্দেশনা শুনুন এবং আমল করুন। মিথ্যা কথা, গীবত ও অন্যায় কাজ বর্জন না করলে শুধু না খেয়ে থাকায় কোনো সওয়াব নেই।",
                "Every day of Ramadan should be embraced as the greatest opportunity for spiritual purification. Guard your tongue, eyes, and actions against sins to attain the true objective of Taqwa.",
                "সূরা আল-বাকারা: ১৮৩, সহীহ বুখারী ১৯০৩",
                "Surah Al-Baqarah: 183, Sahih Bukhari 1903"
        ));

        // 3. রোজার ইসলামিক বই
        CONTENT_MAP.put("roza_books", new TopicContent(
                "roza_books",
                "রোজার ইসলামিক গ্রন্থাবলী",
                "Islamic Books on Fasting",
                "ইসলামিক গ্রন্থ",
                "Literature",
                "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ وَبَيِّنَاتٍ مِّنَ الْهُدَىٰ وَالْفُرْقَانِ",
                "শাহরু রামাদানাল্লাযি উনযিলা ফিহিল কুরআন হুদান লিন্নাসি ওয়া বাইয়্যিনাতিম মিনাল হুদা ওয়াল ফুরকান।",
                "Shahru Ramadanal-ladhi unzila feehil-Qur'anu hudal-linnasi wa bayyinatin minal-huda wal-furqan.",
                "রমজান মাস হলো সেই মাস, যাতে নাজিল করা হয়েছে কুরআন; যা মানবজাতির জন্য হেদায়েত এবং হেদায়েতের সুস্পষ্ট নিদর্শন ও সত্য-মিথ্যার পার্থক্যকারী।",
                "The month of Ramadan in which was revealed the Qur'an, a guidance for the people and clear proofs of guidance and criterion.",
                "রমজান ও সিয়াম সংক্রান্ত বিশুদ্ধ ফিকহ ও হাদিসের কিতাব অধ্যয়ন করা প্রত্যেক মুসলিমের জন্য অত্যন্ত জরুরি। 'কিতাবুস সাওম', 'রমজানের ফজিলত ও মাসায়েল', এবং 'সহীহ বুখারীর রোজা অধ্যায়' পাঠ করে আপনার রোজাকে পূর্ণাঙ্গ ও সহীহ সুন্নাহ মোতাবেক পরিচালিত করুন।",
                "Studying authentic Fiqh and Hadith literature concerning fasting is essential for every believer to avoid common mistakes and gain maximum rewards.",
                "সূরা আল-বাকারা: ১৮৫",
                "Surah Al-Baqarah: 185"
        ));

        // 4. খতমে কুরআন
        CONTENT_MAP.put("roza_khatm", new TopicContent(
                "roza_khatm",
                "খতমে কুরআন ও তিলাওয়াত পরিকল্পনা",
                "Quran Khatm Planner",
                "কুরআন খতম",
                "Quran Khatm",
                "اقْرَءُوا الْقُرْآنَ فَإِنَّهُ يَأْتِي يَوْمَ الْقِيَامَةِ شَفِيعًا لأَصْحَابِهِ",
                "ইকরাউল কুরআনা ফা ইন্নাহু ইয়াতি ইয়াওমাল কিয়ামাতী শাফিআল লি আসহাবিহী।",
                "Iqra'ul Qur'ana fa innahu ya'tee yawmal qiyamati shafee'an li ashabihi.",
                "তোমরা কুরআন তিলাওয়াত করো, কেননা কিয়ামতের দিন তা তার পাঠকদের জন্য সুপারিশকারী হিসেবে আগমন করবে।",
                "Recite the Qur'an, for it will come as an intercessor for its companions on the Day of Resurrection.",
                "রমজানে প্রতিদিন মাত্র ৪ পৃষ্ঠা তিলাওয়াত করলে মাসে ১ খতম সম্পন্ন হয়। প্রতিদিন ১ পারা তিলাওয়াত করলে পুরো রমজানে ১ খতম এবং ২ পারা তিলাওয়াত করলে ২ খতম করা সহজ হয়। ফরজ নামাজের পর ৪ পৃষ্ঠা করে তিলাওয়াত সম্পন্ন করুন।",
                "Reading just 4 pages after each of the 5 daily prayers allows you to effortlessly complete 1 full Khatm in 30 days.",
                "সহীহ মুসলিম ৮০৪",
                "Sahih Muslim 804"
        ));

        // 5. রোজা নিয়ে তথ্য
        CONTENT_MAP.put("roza_info", new TopicContent(
                "roza_info",
                "রোজা সংক্রান্ত মৌলিক তথ্য",
                "Fasting Principles and Facts",
                "মৌলিক তথ্য",
                "Fasting Facts",
                "الصَّوْمُ جُنَّةٌ فَلاَ يَرْفُثْ وَلاَ يَجْهَلْ",
                "আস-সাওমু জুন্নাতুন ফালা ইয়ারফুছ ওয়ালা ইয়াজহাল।",
                "As-sawmu junnatun fala yarfuth wala yajhal.",
                "রোজা হলো একটি ঢালস্বরূপ। অতএব রোজা রেখে কেউ যেন অশ্লীল কথা না বলে এবং মূর্খতাসুলভ আচরণ না করে।",
                "Fasting is a shield, so the one fasting should not use obscene language nor act foolishly.",
                "১. রোজা ইসলামের পাঁচটি রুকনের অন্যতম একটি স্তম্ভ।\n২. হিজরি দ্বিতীয় বর্ষে শাবান মাসে রোজা ফরজ হয়।\n৩. সুবহে সাদিক থেকে সূর্যাস্ত পর্যন্ত পানাহার, যৌন মিলন ও পাপকাজ থেকে আল্লাহর সন্তুষ্টির উদ্দেশ্যে বিরত থাকাই সিয়াম।\n৪. রোজা মানুষের আত্মসংযম বৃদ্ধি করে এবং অন্তরে তাকওয়া জাগ্রত করে।",
                "1. Fasting is one of the Five Pillars of Islam.\n2. It was ordained as an obligation in the second year of Hijrah.\n3. Abstaining from food, drink, and intimate relations from dawn until sunset solely for Allah's pleasure.",
                "সহীহ বুখারী ১৮৯৪",
                "Sahih Bukhari 1894"
        ));

        // 6. রোজার হাদিস
        CONTENT_MAP.put("roza_hadith", new TopicContent(
                "roza_hadith",
                "রোজার ফজিলতপূর্ণ সহীহ হাদিস",
                "Authentic Hadiths on Fasting",
                "সহীহ হাদিস",
                "Hadiths",
                "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                "মান সামা রামাদানা ঈমানান ওয়াহতিসাবান গুফিরা লাহু মা তাক্বাদ্দামা মিন যাম্বিহি।",
                "Man sama Ramadana eemanan wahtisaban ghufira lahu ma taqaddama min dhanbihi.",
                "যে ব্যক্তি ঈমানের সাথে ও সওয়াবের আশায় রমজানের রোজা রাখবে, তার পূর্ববর্তী সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে।",
                "Whoever fasts Ramadan out of faith and in the hope of reward, his past sins will be forgiven.",
                "১. জান্নাতে 'রাইয়্যান' নামক একটি বিশেষ দরজা রয়েছে, যা দিয়ে কেবল রোজাদারগণই প্রবেশ করবে (বুখারী ১৮৯৬)।\n২. আল্লাহ তাআলা বলেন: 'রোজা আমারই জন্য এবং আমি নিজে এর প্রতিদান দেব' (বুখারী ১৯০৪)।\n৩. রোজাদারের মুখের গন্ধ আল্লাহর কাছে মেশকের সুগন্ধির চেয়েও অধিক প্রিয়।",
                "1. In Paradise there is a gate called Ar-Rayyan through which only those who fast will enter.\n2. Allah states: 'Fasting is for Me, and I shall reward for it.'\n3. The breath of a fasting person is sweeter to Allah than the fragrance of musk.",
                "সহীহ বুখারী ৩৮, সহীহ মুসলিম ৭৬০",
                "Sahih Bukhari 38, Sahih Muslim 760"
        ));

        // 7. তারাবীহ
        CONTENT_MAP.put("roza_taraweeh", new TopicContent(
                "roza_taraweeh",
                "তারাবীহ সালাত ও কিয়ামুল লাইল",
                "Taraweeh Prayer Guide",
                "তারাবীহ সালাত",
                "Taraweeh",
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                "মান ক্বামা রামাদানা ঈমানান ওয়াহতিসাবান গুফিরা লাহু মা তাক্বাদ্দামা মিন যাম্বিহি।",
                "Man qama Ramadana eemanan wahtisaban ghufira lahu ma taqaddama min dhanbihi.",
                "যে ব্যক্তি ঈমানের সাথে ও সওয়াবের আশায় রমজানের রাতে নামাজে (তারাবীহতে) দাঁড়াবে, তার অতীতের সমস্ত গুনাহ ক্ষমা করা হবে।",
                "Whoever stands in prayer during the nights of Ramadan out of faith and in the hope of reward, his previous sins will be forgiven.",
                "১. তারাবীহ সালাত সুন্নাতে মুয়াক্কাদাহ। ইশার ফরজ ও সুন্নতের পর এবং বিতরের পূর্বে তারাবীহ আদায় করতে হয়।\n২. দুই রাকাত করে সালাম ফিরিয়ে প্রশান্তির সাথে ধীরস্থিরভাবে তিলাওয়াত করে তারাবীহ আদায় করা সুন্নত।\n৩. জামাতে ইমামের সাথে শেষ পর্যন্ত তারাবীহ সম্পন্ন করলে সারা রাত নফল নামাজ পড়ার সমপরিমাণ সওয়াব লাভ হয়।",
                "1. Taraweeh prayer is Sunnah Mu'akkadah performed after Isha and before Witr.\n2. It is prayed in units of two rak'ahs with tranquility.\n3. Praying with the congregation until the Imam finishes yields the reward of praying the entire night.",
                "সহীহ বুখারী ৩৭, তিরমিযী ৮০৬",
                "Sahih Bukhari 37, Jami' at-Tirmidhi 806"
        ));

        // 8. ইতিকাফ
        CONTENT_MAP.put("roza_itikaf", new TopicContent(
                "roza_itikaf",
                "ইতিকাফের শারয়ী বিধান",
                "Rules of Itikaf",
                "ইতিকাফ বিধান",
                "Itikaf",
                "أَنَّ النَّبِيَّ صلى الله عليه وسلم كَانَ يَعْتَكِفُ الْعَشْرَ الأَوَاخِرَ مِنْ رَمَضَانَ حَتَّى تَوَفَّاهُ اللَّهُ",
                "আন্নান নাবিয়্যা সাল্লাল্লাহু আলাইহি ওয়াসাল্লামা কানা ইয়াতাকিফুল আশরাল আওয়াহিরা মিন রামাদানা হাত্তা তাওয়াফ্ফাহুল্লাহ।",
                "Annan-Nabiyya sallallahu 'alayhi wa sallama kana ya'takiful-'ashral-awakhira min Ramadana hatta tawaffahullah.",
                "রাসুলুল্লাহ ﷺ ওফাত পর্যন্ত প্রতি বছর রমজানের শেষ দশকে ইতিকাফ করতেন।",
                "The Prophet ﷺ used to practice Itikaf in the last ten days of Ramadan until he passed away.",
                "১. রমজানের শেষ দশকে মসজিদে অবস্থান করা সুন্নাতে মুয়াক্কাদা আলাল কিফায়াহ।\n২. ২১তম রাতের সূর্য ডোবার পূর্বে মসজিদে প্রবেশ করতে হয় এবং ঈদের চাঁদ দেখা গেলে ইতিকাফ সমাপ্ত হয়।\n৩. মানবীয় প্রাকৃতিক প্রয়োজন (শৌচাগার, অজু, ফরজ গোসল) ব্যতীত মসজিদ থেকে বের হলে ইতিকাফ ভেঙে যায়।",
                "1. Itikaf during the last 10 days of Ramadan is Sunnah Mu'akkadah Kifayah.\n2. Enter before sunset of the 20th day and leave upon the sighting of the Eid moon.\n3. Leaving the mosque without genuine bodily necessity invalidates the Itikaf.",
                "সহীহ বুখারী ২০২৬, সহীহ মুসলিম ১১৭২",
                "Sahih Bukhari 2026, Sahih Muslim 1172"
        ));

        // 9. লাইলাতুল কদর
        CONTENT_MAP.put("roza_qadr", new TopicContent(
                "roza_qadr",
                "লাইলাতুল কদরের মহাত্ম্য ও দোয়া",
                "Virtues and Duas of Laylatul Qadr",
                "লাইলাতুল কদর",
                "Laylatul Qadr",
                "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
                "আল্লাহুম্মা ইন্নাকা আফুউন তুহিব্বুল আফওয়া ফা'ফু আন্নী।",
                "Allahumma innaka 'Afuwwun tuhibbul-'afwa fa'fu 'anni.",
                "হে আল্লাহ! আপনি ক্ষমাশীল, আপনি ক্ষমা করতে ভালোবাসেন; অতএব আমাকে ক্ষমা করে দিন।",
                "O Allah, You are Forgiving and love forgiveness, so forgive me.",
                "১. এই এক রাতের ইবাদত এক হাজার মাসের (৮৩ বছর ৪ মাস) ইবাদতের চেয়েও শ্রেষ্ঠ।\n২. রাসুলুল্লাহ ﷺ বলেছেন: 'তোমরা রমজানের শেষ দশকের বেজোড় রাতগুলোতে (২১, ২৩, ২৫, ২৭ ও ২৯তম রাত) লাইলাতুল কদর অনুসন্ধান করো।'\n৩. এ রাতে নফল সালাত, তওবা-ইস্তিগফার ও দান-সদকার বিশেষ ফজিলত রয়েছে।",
                "1. Worship on this night is superior to worship for a thousand months.\n2. Seek it in the odd-numbered nights of the last ten days of Ramadan.\n3. Keep your heart engaged in prayer, sincere repentance, and generous charity.",
                "সূরা আল-কদর: ৩, জামে আত-তিরমিযী ৩৫১৩",
                "Surah Al-Qadr: 3, Jami' at-Tirmidhi 3513"
        ));

        // 10. ফিতরা
        CONTENT_MAP.put("roza_fitra", new TopicContent(
                "roza_fitra",
                "সদকাতুল ফিতরের নিয়ম ও পরিমাণ",
                "Rules and Calculation of Sadaqatul Fitr",
                "সদকাতুল ফিতর",
                "Fitrah",
                "فَرَضَ رَسُولُ اللَّهِ صلى الله عليه وسلم زَكَاةَ الْفِطْرِ صَاعًا مِنْ تَمْرٍ أَوْ صَاعًا مِنْ شَعِيرٍ طُهْرَةً لِلصَّائِمِ",
                "ফারাযা রাসুলুল্লাহি সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম যাকাতাল ফিতরি সাআম মিন তামরিন আও সাআম মিন শাঈরিন তুহরাতাল লিস-সায়িম।",
                "Farada Rasulullahi sallallahu 'alayhi wa sallama zakatal-fitri sa'am min tamrin aw sa'am min sha'irin tuhratal-lis-sa'im.",
                "রাসুলুল্লাহ ﷺ রোজাদারের অনর্থক কথা ও অশ্লীলতার ত্রুটি দূর করার জন্য এবং মিসকিনদের খাদ্যের উদ্দেশ্যে ফিতরা ফরজ করেছেন।",
                "The Messenger of Allah ﷺ prescribed Sadaqatul Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the poor.",
                "১. ঈদের নামাজের আগেই ফিতরা আদায় করা ওয়াজিব। ঈদের নামাজের পর দিলে তা সাধারণ নফল সদকা হিসেবে গণ্য হবে।\n২. পরিবারের অপ্রাপ্তবয়স্ক সন্তানসহ প্রত্যেকের পক্ষ থেকে অভিভাবককে ফিতরা আদায় করতে হয়।\n৩. ইসলামিক ফাউন্ডেশন কর্তৃক নির্ধারিত গম, যব, খেজুর বা কিসমিসের সমপরিমাণ নগদ অর্থ দিয়ে ফিতরা আদায় করা বৈধ।",
                "1. Sadaqatul Fitr must be discharged before the Eid prayer.\n2. The head of the household is responsible for paying on behalf of all dependents.\n3. Cash equivalent to the market price of grain, dates, or raisins is fully permissible.",
                "সহীহ সুনান আবু দাউদ ১৬০৯",
                "Sunan Abi Dawud 1609"
        ));

        // 11. ঈদ
        CONTENT_MAP.put("roza_eid", new TopicContent(
                "roza_eid",
                "ঈদুল ফিতরের সুন্নত ও তাকবীর",
                "Eid-ul-Fitr Sunnahs and Takbirs",
                "ঈদের সুন্নত",
                "Eid Guide",
                "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ لاَ إِلَهَ إِلاَّ اللَّهُ وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ وَلِلَّهِ الْحَمْدُ",
                "আল্লাহু আকবার আল্লাহু আকবার লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার আল্লাহু আকবার ওয়া লিল্লাহিল হামদ।",
                "Allahu Akbar, Allahu Akbar, La ilaha illallah, Wallahu Akbar, Allahu Akbar, wa lillahil-hamd.",
                "আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ; আল্লাহ ব্যতীত কোনো উপাস্য নেই। আল্লাহ সর্বশ্রেষ্ঠ, আল্লাহ সর্বশ্রেষ্ঠ এবং সমস্ত প্রশংসা কেবল আল্লাহর জন্যই।",
                "Allah is the Greatest, Allah is the Greatest; there is no deity worthy of worship but Allah. Allah is the Greatest, Allah is the Greatest, and all praise belongs to Allah.",
                "১. ঈদের দিন সকালে গোসল করা, মিসওয়াক করা ও উত্তম পোশাক পরিধান করা সুন্নত।\n২. ঈদুল ফিতরের নামাজে যাওয়ার পূর্বে বিজোড় সংখ্যক মিষ্টি খেজুর বা খাবার গ্রহণ করা।\n৩. ঈদগাহে যাওয়ার সময় উচ্চকণ্ঠে তাকবীর পাঠ করা এবং এক পথ দিয়ে যাওয়া ও ভিন্ন পথ দিয়ে প্রত্যাবর্তন করা সুন্নত।",
                "1. Performing Ghusl, wearing one's finest clean clothes, and applying pleasant scent.\n2. Eating an odd number of dates before leaving for Eid-ul-Fitr prayer.\n3. Reciting Takbeer on the way and taking a different route when returning home.",
                "সহীহ বুখারী ৯৫৩, সুনান ইবনে মাজাহ ১৩০৭",
                "Sahih Bukhari 953, Sunan Ibn Majah 1307"
        ));

        // 12. ফসায়েল মাসায়েল
        CONTENT_MAP.put("roza_masayel", new TopicContent(
                "roza_masayel",
                "রোজার জরুরি ফাসায়েল ও মাসায়েল",
                "Essential Rulings and Masayel of Fasting",
                "মাসায়েল ও ফতোয়া",
                "Masayel",
                "يُرِيدُ اللَّهُ بِكُمُ الْيُسْرَ وَلَا يُرِيدُ بِكُمُ الْعُسْرَ",
                "ইউরিদুল্লাহু বিকুমুল ইউসরা ওয়ালা ইউরিদু বিকুমুল উসর।",
                "Yureedullahu bikumul-yusra wa la yureedu bikumul-'usr.",
                "আল্লাহ তোমাদের জন্য সহজ করতে চান এবং তিনি তোমাদের কষ্ট দিতে চান না।",
                "Allah intends for you ease and does not intend for you hardship.",
                "১. যেগুলোতে রোজা ভাঙে না: চোখে ড্রপ ব্যবহার, ইনসুলিন বা সাধারণ ইনজেকশন নেওয়া, অনিচ্ছাকৃত বমি হওয়া, স্বপ্নদোষ, মেসওয়াক করা বা রক্ত পরীক্ষা করা।\n২. যেগুলোতে কাজা ওয়াজিব হয়: নাকে ড্রপ পেটে গেলে, ভুলবশত সূর্যাস্তের পূর্বে ইফতার করে ফেললে।\n৩. যেগুলোতে কাজা ও কাফফারা উভয়ই ফরজ: জেনে-বুঝে ইচ্ছাকৃতভাবে দিনের বেলা পানাহার করলে বা স্ত্রী সহবাস করলে ১টি রোজার পরিবর্তে ৬০টি রোজা রাখতে হয়।",
                "1. Does not invalidate: Eye drops, routine medical injections, involuntary vomiting, wet dreams, miswak, and diagnostic blood tests.\n2. Requires Qadha only: Inhaling drops that reach the stomach, breaking fast prematurely based on inaccurate time.\n3. Requires Qadha and Kaffarah: Deliberate eating, drinking, or sexual intercourse during fasting daylight hours.",
                "সূরা আল-বাকারা: ১৮৫, ফাতাওয়া হিন্দিয়া ১/১৯৯",
                "Surah Al-Baqarah: 185, Fatawa Hindiyyah 1/199"
        ));

        // 13. রোজায় স্বাস্থ্য পরামর্শ
        CONTENT_MAP.put("roza_health", new TopicContent(
                "roza_health",
                "রোজায় স্বাস্থ্য ও পুষ্টি পরামর্শ",
                "Health and Nutrition Tips for Fasting",
                "স্বাস্থ্য পরামর্শ",
                "Health Tips",
                "كُلُوا وَاشْرَبُوا وَلَا تُسْرِفُوا ۚ إِنَّهُ لَا يُحِبُّ الْمُسْرِفِينَ",
                "কুলু ওয়াশরাবু ওয়ালা তুসরিফু, ইন্নাহু লা ইউহিব্বুল মুসরিফীন।",
                "Kulu washrabu wa la tusrifu, innahu la yuhibbul-musrifeen.",
                "তোমরা খাও এবং পান করো, কিন্তু অপচয় করো না; নিশ্চয়ই তিনি অপচয়কারীদের পছন্দ করেন না।",
                "Eat and drink, but be not excessive. Indeed, He likes not those who commit excess.",
                "১. সেহরিতে জটিল শর্করা (লাল চালের ভাত, ওটস, ডিম, দুধ) গ্রহণ করুন যা ধীরে শক্তি সরবরাহ করে।\n২. ইফতারে ভাজাপোড়া ও অতিরিক্ত তেলযুক্ত খাবার পরিহার করে তাজা ফলমূল, শরবত ও ডাবের পানি পান করুন।\n৩. ডায়াবেটিস ও কিডনি রোগীরা চিকিৎসকের পরামর্শ নিয়ে ওষুধের সময় সমন্বয় করে রোজা রাখুন।",
                "1. Consume complex carbohydrates and wholesome proteins at Sehri for sustained energy release.\n2. Break the fast gently with dates and hydrating fluids, avoiding heavy fried oily items.\n3. Individuals with chronic conditions such as diabetes should consult medical specialists for medication timing.",
                "সূরা আল-আ'রাফ: ৩১",
                "Surah Al-A'raf: 31"
        ));

        // 14. জুমাতুল বিদা
        CONTENT_MAP.put("roza_jumatul_wida", new TopicContent(
                "roza_jumatul_wida",
                "জুমাতুল বিদার ফজিলত ও তাৎপর্য",
                "Significance of Jumatul Wida",
                "জুমাতুল বিদা",
                "Jumatul Wida",
                "خَيْرُ يَوْمٍ طَلَعَتْ عَلَيْهِ الشَّمْسُ يَوْمُ الْجُمُعَةِ",
                "হাইরু ইয়াওমিন তালাআ'ত আলাইহিশ শামসু ইয়াওমুল জুমুআহ।",
                "Khayru yawmin tala'at 'alayhish-shamsu yawmul-Jumu'ah.",
                "সূর্য উদিত হওয়া দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।",
                "The best day on which the sun has risen is Friday.",
                "১. রমজানের শেষ শুক্রবারকে বিদায়ী জুমা বা জুমাতুল বিদা বলা হয়।\n২. এটি রোজা ও জুমার সমন্বয়ে এক সুমহান দিন, যেখানে বিশেষ তওবা ও ইস্তিগফার করা উচিত।\n৩. বিশেষ কোনো স্বতন্ত্র ফরজ নামাজ নেই, তবে জুমার নামাজে আগে আগে উপস্থিত হয়ে খুতবা শোনা ও মাগফিরাতের দোয়া করা অপরিহার্য।",
                "1. The final Friday of the sacred month of Ramadan is known as Jumatul Wida.\n2. It combines the sanctity of the weekly master of days with the blessed fast of Ramadan.\n3. Arrive early at the masjid, listen attentively to the sermon, and invoke Allah for mercy.",
                "সহীহ মুসলিম ৮৫৪",
                "Sahih Muslim 854"
        ));
    }

    public static TopicContent getTopic(String topicId) {
        return CONTENT_MAP.get(topicId);
    }
}
