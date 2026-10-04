<?php
/**
 * ==============================================================================
 * DEEN ONE - NAMAZ VISUAL STEPS DATABASE SEEDER
 * Creates `namaz_visual_steps` table and seeds all 12 Male and 10 Female steps.
 * ==============================================================================
 */

require_once __DIR__ . '/../api/db.php';

try {
    $pdo = getDbConnection();

    // 1. Create table
    $createTableSql = "
    CREATE TABLE IF NOT EXISTS `namaz_visual_steps` (
      `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
      `gender` enum('male','female') NOT NULL DEFAULT 'male',
      `step_number` int(10) NOT NULL,
      `step_title_bn` varchar(255) NOT NULL,
      `step_title_en` varchar(255) NOT NULL,
      `description_bn` text NOT NULL,
      `description_en` text NOT NULL,
      `dua_bn` text DEFAULT NULL,
      `dua_en` text DEFAULT NULL,
      `notes_bn` text DEFAULT NULL,
      `notes_en` text DEFAULT NULL,
      `image_url` varchar(500) NOT NULL,
      `vertical_bias` decimal(4,2) NOT NULL DEFAULT 0.50,
      `is_active` tinyint(1) NOT NULL DEFAULT 1,
      `display_order` int(10) NOT NULL DEFAULT 0,
      `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
      `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
      PRIMARY KEY (`id`),
      KEY `idx_gender_step` (`gender`, `step_number`),
      KEY `idx_is_active` (`is_active`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    ";
    $pdo->exec($createTableSql);

    // Truncate existing steps to re-seed cleanly
    $pdo->exec("TRUNCATE TABLE `namaz_visual_steps`");

    $steps = [
        // ==================== MALE STEPS (12 Steps) ====================
        [
            'gender' => 'male',
            'step_number' => 1,
            'step_title_bn' => 'কিয়াম ও নিয়ত',
            'step_title_en' => 'Qiyam & Intention',
            'description_bn' => 'নামাজ পড়ার আগে মন দিয়ে আল্লাহর কাছে নামাজের উদ্দেশ্য ঠিক করতে হবে। মনোযোগ সহকারে নামাজের জন্য নিয়ত করতে হবে।',
            'description_en' => 'Before starting prayer, sincerely set your intention in your heart purely for Allah.',
            'dua_bn' => null,
            'dua_en' => null,
            'notes_bn' => "উদাহরণ: 'আমি দুই রাকাত ফরজ নামাজ আদায় করছি আল্লাহর জন্য।' প্রাথমিক নিয়ত করে নামাজ শুরু করতে হয়। (হাদিস: সুনানে আবু দাউদ: ৭৫৮)",
            'notes_en' => "Example: 'I intend to offer two Rak'ahs of Fard prayer for Allah.' The intention is made in the heart. (Hadith: Sunan Abi Dawud: 758)",
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_1.jpg',
            'vertical_bias' => 0.08,
            'display_order' => 1
        ],
        [
            'gender' => 'male',
            'step_number' => 2,
            'step_title_bn' => 'তাকবীরে তাহরীমা',
            'step_title_en' => 'Takbeer-e-Tahreema',
            'description_bn' => 'কিবলামুখী হয়ে আপনার উভয় হাত কান বরাবর ওপরে তুলুন এবং বলুন:',
            'description_en' => 'Facing the Qiblah, raise both hands up to ear level and recite:',
            'dua_bn' => "আরবি: اللَّهُ أَكْبَرُ\n\nউচ্চারণ: আল্লাহু আকবার।\nঅর্থ: আল্লাহ সর্বশ্রেষ্ঠ।",
            'dua_en' => "Arabic: اللَّهُ أَكْبَرُ\n\nTransliteration: Allahu Akbar.\nMeaning: Allah is the Greatest.",
            'notes_bn' => 'আপনার নামাজ এখান থেকে শুরু হলো।',
            'notes_en' => 'Your prayer begins here.',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_2.jpg',
            'vertical_bias' => 0.02,
            'display_order' => 2
        ],
        [
            'gender' => 'male',
            'step_number' => 3,
            'step_title_bn' => 'হাত বাঁধা ও ছানা পাঠ',
            'step_title_en' => 'Folding Hands & Reciting Sana',
            'description_bn' => 'ডান হাত বাম হাতের ওপর রেখে নাভির ওপর বাঁধুন এবং দৃষ্টি সিজদার স্থানে রাখুন। তাকবীরে তাহরীমার পরেই পড়ুন ছানা:',
            'description_en' => 'Place your right hand over the left over your navel and keep your gaze at the place of prostration. Recite Sana:',
            'dua_bn' => "আরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।",
            'dua_en' => "Arabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
            'notes_bn' => "তা’য়াউজঃ আউ’যুবিল্লাহি মিনাশশাইত্বানির রাজীম\n\nতাসমিয়াহ্ঃ বিসমিল্লাহির রাহমানির রাহীম\n\nসুরা ফাতিহা পড়া ফরজ। (কুরআন: ১: ১-৭), প্রত্যেক নামাজে সুরা ফাতিহা পড়ুন এবং অন্য যেকোনো একটি সূরা তেলাওয়াত করুন।",
            'notes_en' => "Ta'awwudh: A'udhu billahi minash-shaytanir-rajim\n\nTasmiyah: Bismillahir-Rahmanir-Rahim\n\nReciting Surah Al-Fatiha is obligatory (Quran 1:1-7). Recite Surah Al-Fatiha in every Rak'ah followed by any other Surah.",
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_3.jpg',
            'vertical_bias' => 0.35,
            'display_order' => 3
        ],
        [
            'gender' => 'male',
            'step_number' => 4,
            'step_title_bn' => 'রুকু',
            'step_title_en' => 'Ruku',
            'description_bn' => 'এবার আল্লাহু আকবার বলে রুকুতে যান। রুকুর মুহূর্তে আপনার হাত আপনার হাঁটুতে এবং আপনার চোখের দৃষ্টি সিজদার স্থানে হওয়া উচিত। আপনার শরীরটি মাটির সাথে সমান্তরাল রাখুন।',
            'description_en' => "Say 'Allahu Akbar' and bow into Ruku. Place your hands on your knees and look at the place of Sajdah. Keep your back parallel to the ground.",
            'dua_bn' => "রুকুর দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রভুর পবিত্রতা বর্ণনা করছি।",
            'dua_en' => "Ruku Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
            'notes_bn' => 'রুকুতে এই দোয়া ৩, ৫, ৭ বা বিজোড় সংখ্যক বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)',
            'notes_en' => 'Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_4.jpg',
            'vertical_bias' => 0.30,
            'display_order' => 4
        ],
        [
            'gender' => 'male',
            'step_number' => 5,
            'step_title_bn' => 'কওমা - রুকু থেকে উঠে দাঁড়ানো',
            'step_title_en' => 'Qawmah',
            'description_bn' => 'রুকু থেকে উঠে দাঁড়াতে দাঁড়াতে পড়ুন:',
            'description_en' => 'Rise straight from Ruku into standing position (Qawmah) and recite:',
            'dua_bn' => "আরবিঃ سَمِعَ اللهُ لِمَنْ حَمِدَهُ\nউচ্চারণ: সামিয়াল্লাহু লিমান হামিদাহ\nঅর্থ: যে ব্যক্তি আল্লাহর প্রশংসা করে, আল্লাহ তা শুনে থাকেন।",
            'dua_en' => "Arabic: سَمِعَ اللهُ لِمَنْ حَمِدَهُ\nTransliteration: Sami' Allahu liman hamidah\nMeaning: Allah hears whoever praises Him.",
            'notes_bn' => "এরপর সোজা হয়ে দাঁড়িয়ে এই দোয়াটি পড়ুন:\nউচ্চারণ: রাব্বানা লাকাল হামদু হামদান কাসীরান তাইয়িবান মুবারাকান ফীহ্\n\nঅর্থ: হে আমাদের পরওয়ারদিগার! তোমারই জন্যে বহু পবিত্র প্রশংসা রয়েছে, যার মধ্যে বরকতও নিহিত আছে।",
            'notes_en' => "Then standing upright, recite:\nTransliteration: Rabbana lakal-hamd, hamdan katheeran tayyiban mubarakan feeh\n\nMeaning: Our Lord, to You belongs all praise, abundant, pure and blessed praise.",
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_5.jpg',
            'vertical_bias' => 0.12,
            'display_order' => 5
        ],
        [
            'gender' => 'male',
            'step_number' => 6,
            'step_title_bn' => 'সিজদা',
            'step_title_en' => 'Sajdah',
            'description_bn' => "এবার 'আল্লাহু আকবার' বলে ধীরে ধীরে মাটিতে সিজদায় যান এবং কপাল, নাক, হাত, হাঁটু ও পায়ের পাতা মাটিতে স্পর্শ করুন।",
            'description_en' => "Say 'Allahu Akbar' and prostrate into Sajdah, placing your forehead, nose, palms, knees and toes on the ground.",
            'dua_bn' => "সিজদার দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الأَعْلَى\nউচ্চারণ: সুবহানা রাব্বিয়াল আ'লা (৩ বার)\nঅর্থ: আমি আমার সর্বোচ্চ প্রভুর পবিত্রতা বর্ণনা করছি।",
            'dua_en' => "Sajdah Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
            'notes_bn' => 'সিজদায় এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)',
            'notes_en' => 'Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_6.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 6
        ],
        [
            'gender' => 'male',
            'step_number' => 7,
            'step_title_bn' => 'দুই সিজদার মধ্যবর্তী বৈঠক',
            'step_title_en' => 'Jalsah',
            'description_bn' => 'সিজদা থেকে উঠুন এবং কিছুক্ষণের জন্য শান্তভাবে বসুন ও এই দোয়াটি পড়ুন:',
            'description_en' => 'Rise from Sajdah and sit calmly between the two prostrations (Jalsah), then recite:',
            'dua_bn' => "দুই সিজদার মধ্যবর্তী দোয়াঃ\n\nআরবিঃ اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nউচ্চারণ: আল্লাহুম্মাগফির লী ওয়ারহামনী ওয়াজবুরনী ওয়াহদিনী ওয়া আফিনী ওয়ারযুক্বনী।\nঅর্থ: হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, রহম করুন, আমার অবস্থা সংশোধন করুন, সৎপথ দেখান, সুস্থতা দিন ও জীবিকা দান করুন।",
            'dua_en' => "Jalsah Dua:\n\nArabic: اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nTransliteration: Allahummagh-fir lee, warhamnee, wajburnee, wahdinee, wa 'aafinee, warzuqnee.\nMeaning: O Allah! Forgive me, have mercy on me, guide me, grant me well-being and provide for me.",
            'notes_bn' => 'এরপর দ্বিতীয় সিজদা করে দ্বিতীয় রাকাতের জন্য দাঁড়ান।',
            'notes_en' => "Perform the second Sajdah and stand up for the second Rak'ah.",
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_7.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 7
        ],
        [
            'gender' => 'male',
            'step_number' => 8,
            'step_title_bn' => 'তাশাহহুদ বা বৈঠক',
            'step_title_en' => 'Tashahhud',
            'description_bn' => 'প্রতি দুই রাকাতের শেষে এবং শেষ রাকাতে শান্তভাবে বসুন ও তাশাহহুদ পড়ুন:',
            'description_en' => 'Sit comfortably after every two Rak\'ahs and in the final sitting to recite Tashahhud:',
            'dua_bn' => "তাশাহহুদ:\n\nاَلتَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nউচ্চারণ: আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু ওয়াত-তৈয়্যিবাতু আসসালামু আলাইকা আইয়্যুহান নাবিয়্যু ওয়া রহমাতুল্লাহি ওয়া বারাকাতুহু, আসসালামু আলাইনা ওয়া আলা ইবাদিল্লাহিস সালিহীন, আশহাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু।",
            'dua_en' => "Tashahhud:\n\nاَلتَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nTransliteration: At-tahiyyatu lillahi was-salawatu wat-tayyibat. As-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh. As-salamu 'alayna wa 'ala 'ibadillahis-saliheen. Ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluh.",
            'notes_bn' => 'অর্থ: যাবতীয় সম্মান, উপাসনা ও পবিত্র বিষয় আল্লাহর জন্য। হে নবী! আপনার উপর শান্তি, আল্লাহর রহমত ও বরকত বর্ষিত হোক। শান্তি বর্ষিত হোক আমাদের উপর ও আল্লাহর সৎকর্মশীল বান্দাদের উপর। আমি সাক্ষ্য দিচ্ছি যে আল্লাহ ছাড়া কোনো উপাস্য নেই এবং মুহাম্মদ (সা.) তাঁর বান্দা ও রাসুল।',
            'notes_en' => 'Meaning: All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and Allah\'s mercy and blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no deity except Allah, and Muhammad is His slave and Messenger.',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_8.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 8
        ],
        [
            'gender' => 'male',
            'step_number' => 9,
            'step_title_bn' => 'শেষ বৈঠক - দরুদ ও দোয়া মাসূরা',
            'step_title_en' => 'Final Sitting',
            'description_bn' => 'শেষ রাকাতে তাশাহহুদের পর দরুদে ইবরাহিম ও দোয়ায়ে মাসূরা পড়ুন:',
            'description_en' => 'In the final sitting, recite Durood-e-Ibrahim followed by Dua Masura:',
            'dua_bn' => "দরুদঃ\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ\n\nদোআয়ে মাছুরাহঃ\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            'dua_en' => "Durood Ibrahim:\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ\n\nDua Masura:\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            'notes_bn' => 'দরুদ ও মাসূরা পড়া শেষে সালামের মাধ্যমে নামাজ সম্পন্ন করুন।',
            'notes_en' => 'After completing Durood and Dua Masura, finish your prayer with Tasleem (Salam).',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_8.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 9
        ],
        [
            'gender' => 'male',
            'step_number' => 10,
            'step_title_bn' => 'ডান দিকে সালাম ফিরানো',
            'step_title_en' => 'Salam to Right',
            'description_bn' => 'আপনার ডান দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে সালাম দিন:',
            'description_en' => 'Turn your face to the right shoulder and say:',
            'dua_bn' => "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ اَلسَّلَামُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্",
            'dua_en' => "Arabic: اَلسَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
            'notes_bn' => null,
            'notes_en' => null,
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_9.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 10
        ],
        [
            'gender' => 'male',
            'step_number' => 11,
            'step_title_bn' => 'বাম দিকে সালাম ফিরানো',
            'step_title_en' => 'Salam to Left',
            'description_bn' => 'এবং তারপরে বাম দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে আবার বলুন:',
            'description_en' => 'Then turn your face to the left shoulder and say:',
            'dua_bn' => "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ اَلسَّلَামُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্",
            'dua_en' => "Arabic: اَلسَّلَামُ عَلَيْكُمْ وَرَحْمَةُ اللهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
            'notes_bn' => 'এখানে আপনার নামাজ শেষ হলো।',
            'notes_en' => 'Your Salah is now completed.',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_10.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 11
        ],
        [
            'gender' => 'male',
            'step_number' => 12,
            'step_title_bn' => 'সমাপ্তি ও দোয়া',
            'step_title_en' => 'Completion & Dua',
            'description_bn' => 'সালাম ফেরানোর মাধ্যমে নামাজ পূর্ণ হলো। এরপর ৩ বার আস্তাগফিরুল্লাহ পাঠ ও মোনাজাত করা উত্তম।',
            'description_en' => 'After finishing Salah, it is Sunnah to seek forgiveness (Astaghfirullah 3 times) and supplicate.',
            'dua_bn' => "আরবি: أَسْتَغْفِرُ اللَّهَ ، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ\n\nউচ্চারণ: আস্তাগফিরুল্লাহ। আল্লাহুম্মা আনতাস সালামু ওয়া মিনকাস সালাম, তাবারাকতা ইয়া যাল জালালি ওয়াল ইকরাম।",
            'dua_en' => "Arabic: أَسْتَغْفِرُ اللَّهَ ، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ\n\nTransliteration: Astaghfirullah. Allahumma Antas-Salam wa minkas-Salam, tabarakta ya Dhal-Jalali wal-Ikram.",
            'notes_bn' => 'নামাজ সমাপ্ত হলো।',
            'notes_en' => 'Prayer finished.',
            'image_url' => 'uploads/namaz_learning/male/img_salah_male_step_11.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 12
        ],

        // ==================== FEMALE STEPS (10 Steps) ====================
        [
            'gender' => 'female',
            'step_number' => 1,
            'step_title_bn' => 'কিয়াম ও নিয়ত',
            'step_title_en' => 'Qiyam & Intention',
            'description_bn' => 'নামাজ পড়ার আগে মন দিয়ে আল্লাহর কাছে নামাজের উদ্দেশ্য ঠিক করতে হবে। মনোযোগ সহকারে নামাজের জন্য নিয়ত করতে হবে।',
            'description_en' => 'Before starting prayer, sincerely set your intention in your heart for the sake of Allah.',
            'dua_bn' => null,
            'dua_en' => null,
            'notes_bn' => "উদাহরণ: 'আমি দুই রাকাত ফরজ নামাজ আদায় করছি আল্লাহর জন্য।' প্রাথমিক নিয়ত করে নামাজ শুরু করতে হয়। (হাদিস: সুনানে আবু দাউদ: ৭৫৮)",
            'notes_en' => "Example: 'I intend to pray two Rak'ahs of Fard prayer for Allah.' The intention is made in the heart. (Hadith: Sunan Abi Dawud: 758)",
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_1.jpg',
            'vertical_bias' => 0.35,
            'display_order' => 1
        ],
        [
            'gender' => 'female',
            'step_number' => 2,
            'step_title_bn' => 'তাকবীরে তাহরীমা',
            'step_title_en' => 'Takbeer-e-Tahreema',
            'description_bn' => 'কিবলামুখী হয়ে তাকবীরে তাহরীমা বলার সময় উভয় হাত কাঁধ পর্যন্ত উঠান এবং বলুন:',
            'description_en' => 'Facing the Qiblah, raise both hands up to shoulder level and recite:',
            'dua_bn' => "আরবিঃ اَللهُ أَكْبَرُ\nউচ্চারণঃ আল্লাহু আকবর।",
            'dua_en' => "Arabic: اَللهُ أَكْبَرُ\nTransliteration: Allahu Akbar.",
            'notes_bn' => 'আপনার নামাজ এখান থেকে শুরু হলো।',
            'notes_en' => 'Your prayer begins here.',
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_2.jpg',
            'vertical_bias' => 0.08,
            'display_order' => 2
        ],
        [
            'gender' => 'female',
            'step_number' => 3,
            'step_title_bn' => 'হাত বাঁধা ও ছানা পাঠ',
            'step_title_en' => 'Folding Hands & Reciting Sana',
            'description_bn' => 'বুকের ওপর হাত বাঁধবেন। ডান হাত দিয়ে বাম হাত জড়িয়ে ধরবেন না বরং বাম হাতের পিঠের ওপর ডান হাত রেখে দিন এবং দৃষ্টি সিজদার স্থানে রাখুন।',
            'description_en' => 'Fold your hands on the chest. Place the right hand over the back of the left hand without gripping it and keep your gaze at the place of prostration.',
            'dua_bn' => "তাকবীরে তাহরীমার পরেই পড়ুন ছানা:\n\nআরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।",
            'dua_en' => "Recite Sana after Takbeer:\n\nArabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
            'notes_bn' => "তা’য়াউজঃ আউ’যুবিল্লাহি মিনাশশাইত্বানির রাজীম\n\nতাসমিয়াহ্ঃ বিসমিল্লাহির রাহমানির রাহীম\n\nসূরা ফাতিহা পড়া ফরজ। (কুরআন: ১: ১-৭), প্রত্যেক নামাজে সূরা ফাতিহা পড়ুন এবং অন্য যেকোনো একটি সূরা তেলাওয়াত করুন।",
            'notes_en' => "Ta'awwudh: A'udhu billahi minash-shaytanir-rajim\n\nTasmiyah: Bismillahir-Rahmanir-Rahim\n\nReciting Surah Al-Fatiha is obligatory (Quran 1:1-7). Recite Surah Al-Fatiha in every Rak'ah followed by any other Surah.",
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_3.jpg',
            'vertical_bias' => 0.08,
            'display_order' => 3
        ],
        [
            'gender' => 'female',
            'step_number' => 4,
            'step_title_bn' => 'রুকু',
            'step_title_en' => 'Ruku',
            'description_bn' => 'রুকুতে সামান্য নত হয়ে হাত হাঁটু পর্যন্ত রাখুন এবং আঙ্গুলগুলো মিলিয়ে হাঁটুর ওপর রাখুন।',
            'description_en' => 'Bow gently into Ruku with hands placed lightly on knees with fingers kept together.',
            'dua_bn' => "রুকুর দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রভুর পবিত্রতা বর্ণনা করছি।",
            'dua_en' => "Ruku Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
            'notes_bn' => 'রুকুতে এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)',
            'notes_en' => 'Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)',
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_4.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 4
        ],
        [
            'gender' => 'female',
            'step_number' => 5,
            'step_title_bn' => 'কওমা - রুকু থেকে উঠে দাঁড়ানো',
            'step_title_en' => 'Qawmah',
            'description_bn' => 'রুকু থেকে উঠে সোজা হয়ে দাঁড়াতে দাঁড়াতে পড়ুন:',
            'description_en' => 'Rise straight from Ruku into standing position (Qawmah) and recite:',
            'dua_bn' => "আরবিঃ سَمِعَ اللهُ لِمَنْ حَمِدَهُ رَبَّنَا وَلَكَ الْحَمْدُ\nউচ্চারণ: সামিয়াল্লাহু লিমান হামিদাহ, রব্বানা লাকাল হামদ।\nঅর্থ: যে ব্যক্তি আল্লাহর প্রশংসা করে, আল্লাহ তা শুনে থাকেন।",
            'dua_en' => "Arabic: سَمِعَ اللهُ لِمَنْ حَمِدَهُ رَبَّنَا وَلَكَ الْحَمْدُ\nTransliteration: Sami' Allahu liman hamidah, Rabbana wa lakal-hamd.\nMeaning: Allah hears whoever praises Him. Our Lord, to You belongs all praise.",
            'notes_bn' => "দাঁড়িয়ে দোয়া পড়ুন:\nউচ্চারণ: রাব্বানা লাকাল হামদু হামদান কাসীরান তাইয়িবান মুবারাকান ফীহ্\n\nঅর্থ: হে আমাদের প্রতিপালক! তোমারই জন্যে বহু পবিত্র প্রশংসা রয়েছে, যার মধ্যে বরকতও নিহিত আছে।",
            'notes_en' => "Standing upright, recite:\nTransliteration: Rabbana lakal-hamd, hamdan katheeran tayyiban mubarakan feeh\n\nMeaning: Our Lord, to You belongs all praise, abundant, pure and blessed praise.",
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_5.jpg',
            'vertical_bias' => 0.10,
            'display_order' => 5
        ],
        [
            'gender' => 'female',
            'step_number' => 6,
            'step_title_bn' => 'সিজদা',
            'step_title_en' => 'Sajdah',
            'description_bn' => "জড়োসড়ো হয়ে সিজদা করুন। সিজদাতে পেট উরুর সঙ্গে লাগিয়ে রাখুন এবং দুই পা ডান দিকে রাখুন। বলুন 'আল্লাহু আকবার' এবং সিজদায় যান।",
            'description_en' => "Prostrate closely in Sajdah with arms close to the body and both feet placed to the right side. Say 'Allahu Akbar' and prostrate.",
            'dua_bn' => "সিজদার দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الأَعْلَى\nউচ্চারণ: সুবহানা রাব্বিয়াল আ'লা (৩ বার)\nঅর্থ: আমি আমার সর্বোচ্চ প্রভুর পবিত্রতা বর্ণনা করছি।",
            'dua_en' => "Sajdah Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
            'notes_bn' => 'সিজদায় এই দোয়া ৩, ৫ বা ৭ বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)',
            'notes_en' => 'Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)',
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_6.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 6
        ],
        [
            'gender' => 'female',
            'step_number' => 7,
            'step_title_bn' => 'দুই সিজদার মধ্যবর্তী বৈঠক',
            'step_title_en' => 'Jalsah',
            'description_bn' => 'সিজদা থেকে উঠুন এবং কিছুক্ষণের জন্য শান্তভাবে বসুন ও এই দোয়াটি পড়ুন:',
            'description_en' => 'Rise from Sajdah and sit calmly between the two prostrations, then recite:',
            'dua_bn' => "দুই সিজদার মধ্যবর্তী দোয়াঃ\n\nআরবিঃ اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nউচ্চারণ: আল্লাহুম্মাগফির লী ওয়ারহামনী ওয়াজবুরনী ওয়াহদিনী ওয়া আফিনী ওয়ারযুক্বনী।\nঅর্থ: হে আল্লাহ! আপনি আমাকে ক্ষমা করুন, রহম করুন, আমার অবস্থা সংশোধন করুন, সৎপথ দেখান, সুস্থতা দিন ও জীবিকা দান করুন।",
            'dua_en' => "Jalsah Dua:\n\nArabic: اَللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاجْبُرْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي\nTransliteration: Allahummagh-fir lee, warhamnee, wajburnee, wahdinee, wa 'aafinee, warzuqnee.\nMeaning: O Allah! Forgive me, have mercy on me, guide me, grant me well-being and provide for me.",
            'notes_bn' => 'এরপর দ্বিতীয় সিজদা আদায় করে পরবর্তী রাকাতের জন্য দাঁড়ান।',
            'notes_en' => 'Perform the second Sajdah and rise for the next Rak\'ah.',
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_7.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 7
        ],
        [
            'gender' => 'female',
            'step_number' => 8,
            'step_title_bn' => 'তাশাহহুদ বা বৈঠক',
            'step_title_en' => 'Tashahhud & Durood',
            'description_bn' => 'বৈঠকের সময় শান্তভাবে বসে তাশাহহুদ, দরুদ ও দোয়া মাসূরা পাঠ করুন:',
            'description_en' => 'Sit calmly in Tashahhud and recite At-Tahiyyat, Durood Ibrahim and Dua Masura:',
            'dua_bn' => "তাশাহহুদ:\n\nاَلتَّحِيَّاتُ لِلهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nদরুদঃ\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ",
            'dua_en' => "Tashahhud:\n\nاَلتَّحِيَّاتُ لِلهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، اَلسَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ، اَلسَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nDurood:\n\nاَللّٰهُمَّ صَلِّ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ- اَللّٰهُمَّ بَارِكْ عَلٰى مُحَمَّدٍ وَّعَلٰى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلٰى إِبْرَاهِيمَ وَعَلٰى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَّجِيدٌ",
            'notes_bn' => "দো‘আয়ে মাছুরাহঃ\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            'notes_en' => "Dua Masura:\n\nاَللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّনُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_8.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 8
        ],
        [
            'gender' => 'female',
            'step_number' => 9,
            'step_title_bn' => 'ডান দিকে সালাম ফিরানো',
            'step_title_en' => 'Salam to Right',
            'description_bn' => 'আপনার ডান দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে সালাম দিন:',
            'description_en' => 'Turn your face to the right shoulder and say:',
            'dua_bn' => "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্",
            'dua_en' => "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
            'notes_bn' => null,
            'notes_en' => null,
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_9.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 9
        ],
        [
            'gender' => 'female',
            'step_number' => 10,
            'step_title_bn' => 'বাম দিকে সালাম ফিরানো ও সমাপ্তি',
            'step_title_en' => 'Salam to Left & Completion',
            'description_bn' => 'এবং তারপরে বাম দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে আবার বলুন:',
            'description_en' => 'Then turn your face to the left shoulder and say:',
            'dua_bn' => "সালাম ফিরাবার দোয়াঃ\n\nআরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্",
            'dua_en' => "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
            'notes_bn' => 'এখানে আপনার নামাজ সম্পন্ন হলো।',
            'notes_en' => 'Your Salah is now completed.',
            'image_url' => 'uploads/namaz_learning/female/img_salah_female_step_10.jpg',
            'vertical_bias' => 0.50,
            'display_order' => 10
        ]
    ];

    $insertStmt = $pdo->prepare("
        INSERT INTO `namaz_visual_steps` 
        (`gender`, `step_number`, `step_title_bn`, `step_title_en`, `description_bn`, `description_en`, `dua_bn`, `dua_en`, `notes_bn`, `notes_en`, `image_url`, `vertical_bias`, `is_active`, `display_order`)
        VALUES 
        (:gender, :step_number, :step_title_bn, :step_title_en, :description_bn, :description_en, :dua_bn, :dua_en, :notes_bn, :notes_en, :image_url, :vertical_bias, 1, :display_order)
    ");

    foreach ($steps as $st) {
        $insertStmt->execute([
            ':gender' => $st['gender'],
            ':step_number' => $st['step_number'],
            ':step_title_bn' => $st['step_title_bn'],
            ':step_title_en' => $st['step_title_en'],
            ':description_bn' => $st['description_bn'],
            ':description_en' => $st['description_en'],
            ':dua_bn' => $st['dua_bn'],
            ':dua_en' => $st['dua_en'],
            ':notes_bn' => $st['notes_bn'],
            ':notes_en' => $st['notes_en'],
            ':image_url' => $st['image_url'],
            ':vertical_bias' => $st['vertical_bias'],
            ':display_order' => $st['display_order']
        ]);
    }

    echo "Successfully seeded " . count($steps) . " Namaz Visual Steps into MySQL database!\n";

} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
    exit(1);
}
