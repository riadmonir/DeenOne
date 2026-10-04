<?php
/**
 * ==============================================================================
 * DEEN ONE - HAJJ JOURNEY DATABASE SEEDER
 * Creates `hajj_journey_stages` table and seeds all 12 Hajj Journey stages.
 * ==============================================================================
 */

require_once __DIR__ . '/../api/db.php';

try {
    $pdo = getDbConnection();

    // 1. Create table
    $createTableSql = "
    CREATE TABLE IF NOT EXISTS `hajj_journey_stages` (
      `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
      `stage_number` int(10) NOT NULL,
      `stage_number_bn` varchar(10) NOT NULL,
      `stage_number_en` varchar(10) NOT NULL,
      `title_bn` varchar(255) NOT NULL,
      `title_en` varchar(255) NOT NULL,
      `description_bn` text NOT NULL,
      `description_en` text NOT NULL,
      `image_url` varchar(500) NOT NULL,
      `star_color_hex` varchar(20) NOT NULL DEFAULT '#2FB68E',
      `details_bn` text DEFAULT NULL,
      `details_en` text DEFAULT NULL,
      `dua_arabic` text DEFAULT NULL,
      `dua_pronunciation_bn` text DEFAULT NULL,
      `dua_pronunciation_en` text DEFAULT NULL,
      `dua_meaning_bn` text DEFAULT NULL,
      `dua_meaning_en` text DEFAULT NULL,
      `reference` text DEFAULT NULL,
      `is_active` tinyint(1) NOT NULL DEFAULT 1,
      `display_order` int(10) NOT NULL DEFAULT 0,
      `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
      `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
      PRIMARY KEY (`id`),
      UNIQUE KEY `idx_stage_number` (`stage_number`),
      KEY `idx_is_active` (`is_active`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    ";
    $pdo->exec($createTableSql);

    // Truncate existing steps to re-seed cleanly
    $pdo->exec("TRUNCATE TABLE `hajj_journey_stages`");

    $stages = [
        [
            'stage_number' => 1,
            'stage_number_bn' => '০১',
            'stage_number_en' => '01',
            'title_bn' => 'ইহরাম',
            'title_en' => 'Ihram',
            'description_bn' => 'হজের যাত্রা শুরু করুন একটি শুদ্ধ নিয়তের মাধ্যমে। ইহরাম অবস্থায় প্রবেশের জন্য নিয়ত করুন এবং তলবিয়াহ পাঠ করে হজের নিয়মাবলীতে নিজেকে যুক্ত করুন। এরপর ইহরামের কাপড় পরিধান করুন।',
            'description_en' => 'Begin your sacred pilgrimage journey with a pure and sincere intention. Make the Niyyah to enter the state of Ihram, recite the Talbiyah to devote yourself to the rites of Hajj, and put on the prescribed Ihram garments.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_1.png',
            'star_color_hex' => '#EBCA28',
            'details_bn' => "ইহরামের আহকাম ও করণীয়:\n• শারীরিক পরিচ্ছন্নতা অর্জন করুন, গোসল বা অজু করুন।\n• পুরুষরা দুটি সেলাইবিহীন সাদা চাদর পরিধান করবেন (ইজার ও রিদা)। নারীরা তাদের স্বাভাবিক শালীন পোশাক পরিধান করবেন।\n• দুই রাকাত নফল সালাত আদায় করুন।\n• মনে মনে বা মুখে হজের স্পষ্ট নিয়ত করুন।\n• নিয়তের পরপরই তালবিয়াহ পাঠ শুরু করুন।",
            'details_en' => "Ihram Guidelines & Protocols:\n• Perform personal hygiene, Ghusl (ritual bath) or Wudu.\n• Men wear two unstitched white sheets (Izar and Rida). Women wear modest, loose clothing with hijab.\n• Offer two Rak'ahs of Nafl prayer.\n• Make sincere intention for Hajj.\n• Begin reciting the Talbiyah earnestly upon setting intention.",
            'dua_arabic' => 'لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لاَ شَرِيكَ لَكَ',
            'dua_pronunciation_bn' => "লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুল্ক, লা শারীকা লাক।",
            'dua_pronunciation_en' => "Labbayk Allahumma labbayk, labbayka la shareeka laka labbayk, innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.",
            'dua_meaning_bn' => 'আমি আপনার দরবারে হাজির হে আল্লাহ! আমি হাজির! আপনার কোনো শরিক নেই, আমি হাজির! নিশ্চয় সমস্ত প্রশংসা, নিয়ামত ও সার্বভৌমত্ব আপনারই; আপনার কোনো শরিক নেই।',
            'dua_meaning_en' => 'Here I am, O Allah, here I am. Here I am, You have no partner, here I am. Verily all praise, grace, and sovereignty belong to You. You have no partner.',
            'reference' => 'সহীহ বুখারী: ১৫৪৯, সহীহ মুসলিম: ১১৮৪',
            'display_order' => 1
        ],
        [
            'stage_number' => 2,
            'stage_number_bn' => '০২',
            'stage_number_en' => '02',
            'title_bn' => 'তাওয়াফুল কুদুম',
            'title_en' => 'Tawaf al-Qudum',
            'description_bn' => 'মক্কায় প্রবেশের পর, ৭ চক্কর দিয়ে তাওয়াফুল কুদুম সম্পন্ন করুন। এরপর মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক\'আত সুন্নত নামাজ পড়ুন। তবে যদি সেখানে ভিড় থাকে বা নামাজে অসুবিধা হয়, তাহলে মসজিদুল হারামের অন্য কোনো স্থানে নামাজ পড়া যাবে।',
            'description_en' => 'Upon arriving in Makkah, complete the Tawaf al-Qudum (Arrival Circumambulation) by making 7 circuits around the Ka\'bah. Afterwards, pray two Rak\'ahs of Sunnah prayer behind Maqam Ibrahim. However, if crowded, you may pray anywhere inside the Sacred Mosque.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_2.png',
            'star_color_hex' => '#A3DE7C',
            'details_bn' => "তাওয়াফুল কুদুমের নিয়মাবলী:\n• হাজরে আসওয়াদের কোণা থেকে তাওয়াফ শুরু ও শেষ করুন।\n• পুরুষরা প্রথম তিন চক্করে 'রমল' (বীরদর্পে দ্রুত হাঁটা) করবেন এবং ডান কাঁধ উন্মুক্ত রাখবেন (ইজতিবা)।\n• প্রতি চক্করে রুকনে ইয়ামানী অতিক্রমকালে 'রাব্বানা আতিনা ফিদ-দুনিয়া' দোয়া পাঠ করা সুন্নত।\n• সাত চক্কর শেষে মাকামে ইবরাহীমের পেছনে অথবা মসজিদের যেকোনো সুবিধাজনক স্থানে দুই রাকাত সালাত আদায় করুন।",
            'details_en' => "Tawaf al-Qudum Protocols:\n• Begin and conclude each circuit at the Black Stone (Hajar al-Aswad).\n• For men, perform Ramal (brisk, purposeful pace) during the first three circuits and Idtiba (uncovering the right shoulder).\n• Recite 'Rabbana Atina fid-Dunya' between the Yemeni Corner and Hajar al-Aswad.\n• Pray two Rak'ahs behind Maqam Ibrahim or anywhere feasible in Masjid al-Haram.",
            'dua_arabic' => 'رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ',
            'dua_pronunciation_bn' => "রব্বানা আতিনা ফিদ্ দুনয়া হাসানাতাও ওয়া ফিল আখিরাতি হাসানাতাও ওয়া ক্বিনা 'আযাবান নার।",
            'dua_pronunciation_en' => "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
            'dua_meaning_bn' => 'হে আমাদের প্রতিপালক! আমাদের ইহকালে কল্যাণ দান করুন এবং পরকালেও কল্যাণ দান করুন, আর আমাদেরকে জাহান্নামের আজাব থেকে রক্ষা করুন।',
            'dua_meaning_en' => 'Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the punishment of the Fire.',
            'reference' => 'সূরা আল-বাকারা: ২০১, সুনানে আবু দাউদ: ১৮৯২',
            'display_order' => 2
        ],
        [
            'stage_number' => 3,
            'stage_number_bn' => '০৩',
            'stage_number_en' => '03',
            'title_bn' => 'সাফা ও মারওয়া',
            'title_en' => 'Safa & Marwa',
            'description_bn' => 'সাফা ও মারওয়া পাহাড়ের মধ্যে সাঈ করুন, সাতবার হাঁটুন, শুরু করুন সাফা থেকে এবং শেষ করুন মারওয়া পর্যন্ত। সাঈটি তাওয়াফের পরেই করা উচিত।',
            'description_en' => 'Perform Sa\'i between the hills of Safa and Marwa by walking 7 laps, commencing at Safa and culminating at Marwa. Sa\'i should ideally be performed following Tawaf.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_3.png',
            'star_color_hex' => '#2FB68E',
            'details_bn' => "সাঈর বিধান ও আদব:\n• সাফা পাহাড়ে উঠে ক্বিবলামুখী হয়ে তাকবীর, তাহলীল ও দোয়া করুন।\n• সাফা থেকে মারওয়ায় পৌঁছালে ১ চক্কর গণনা হবে। মারওয়া থেকে সাফায় এলে ২য় চক্কর হবে। এভাবে মোট ৭ চক্কর পূর্ণ হলে সাঈ সম্পন্ন হবে মারওয়ায়।\n• দুই সবুজ বাতির মধ্যবর্তী অংশে পুরুষদের দ্রুত কদমে দৌড়ানো সুন্নত।\n• সাঈ চলাকালীন কুরআন তিলাওয়াত ও ইস্তিগফার অব্যাহত রাখুন।",
            'details_en' => "Sa'i Protocols & Etiquettes:\n• Ascend Mount Safa, face the Qiblah, and make Takbeer, Tahleel, and heartfelt supplication.\n• Walking from Safa to Marwa constitutes 1 lap; Marwa to Safa is lap 2. The 7th lap finishes at Marwa.\n• Men should jog briskly between the two green light markers.\n• Spend the entire Sa'i in constant Dhikr, Quranic recitation, and sincere Istighfar.",
            'dua_arabic' => 'إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ ۖ فَمَنْ حَجَّ الْبَيْتَ أَوِ اعْتَمَرَ فَلَا جُنَاحَ عَلَيْهِ أَن يَطَّوَّفَ بِهِمَا',
            'dua_pronunciation_bn' => "ইন্নাস সাফা ওয়াল মারওয়াতা মিন শা'আইরিল্লাহ, ফামান হাজ্জাল বাইতা আওয়ি'তামারা ফালা জুনা-হা 'আলাইহি আই ইয়াত্তাওওয়াফা বিহিমা।",
            'dua_pronunciation_en' => "Innas-Safa wal-Marwata min sha'a'irillah, faman hajjal-bayta awi'tamara fala junaha 'alayhi ay yattawwafa bihima.",
            'dua_meaning_bn' => 'নিশ্চয়ই সাফা ও মারওয়া আল্লাহর নিদর্শনসমূহের অন্তর্ভুক্ত। অতএব যে ব্যক্তি বায়তুল্লাহর হজ বা উমরাহ পালন করে, উভয়ের মাঝে প্রদক্ষিণ (সাঈ) করাতে তার কোনো পাপ নেই।',
            'dua_meaning_en' => 'Indeed, Safa and Marwah are among the symbols of Allah. So whoever makes Hajj to the House or performs Umrah, there is no blame upon him for walking between them.',
            'reference' => 'সূরা আল-বাকারা: ১৫৮, সহীহ মুসলিম: ১২১৮',
            'display_order' => 3
        ],
        [
            'stage_number' => 4,
            'stage_number_bn' => '০৪',
            'stage_number_en' => '04',
            'title_bn' => 'মিনা',
            'title_en' => 'Mina',
            'description_bn' => '৮ই জিলহজ্জের সকালে মক্কা থেকে মিনা রওনা হন এবং সেখানেই রাত যাপন করুন। এই সময়টুকু দোয়া ও ইবাদতে ব্যস্ত থাকুন, যাতে আগামীকাল আরাফাত দিবসের প্রস্তুতি নিতে পারেন।',
            'description_en' => 'On the morning of the 8th of Dhul Hijjah, depart from Makkah to Mina and spend the night there. Dedicate these hours to supplication and worship to prepare spiritually for the Day of Arafah tomorrow.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_4.png',
            'star_color_hex' => '#2FB68E',
            'details_bn' => "মিনায় অবস্থানের নিয়মাবলী (৮ই জিলহজ - ইয়াওমুত তারবিয়াহ):\n• জোহরের পূর্বেই মিনায় পৌঁছানোর চেষ্টা করুন।\n• মিনায় জোহর, আসর, মাগরিব, ইশা এবং ৯ই জিলহজের ফজর—এই পাঁচ ওয়াক্ত নামাজ কসর সহকারে আদায় করুন।\n• এই রাতে মিনায় অবস্থান করা সুন্নাত মুয়াক্কাদা।\n• বেশি বেশি তালবিয়া, তাওবাহ এবং ইস্তিগফারে রত থাকুন।",
            'details_en' => "Rites of Mina (8th Dhul Hijjah - Yawm at-Tarwiyah):\n• Aim to arrive in Mina before Dhuhr prayer.\n• Pray five obligatory prayers in Mina: Dhuhr, Asr, Maghrib, Isha, and Fajr of the 9th Dhul Hijjah, shortening 4-Rak'ah prayers.\n• Staying overnight in Mina on this night is an established Sunnah.\n• Engage consistently in Talbiyah, sincere repentance, and heartfelt contemplation.",
            'dua_arabic' => 'اللَّهُمَّ إِنِّي أَسْأَلُكَ رِضَاكَ وَالْجَنَّةَ وَأَعُوذُ بِكَ مِنْ سَخَطِكَ وَالنَّارِ',
            'dua_pronunciation_bn' => "আল্লাহুম্মা ইন্নী আসআলুকা রিদাকা ওয়াল জান্নাহ, ওয়া আ'ঊযু বিকা মিন সাখাত্বিকা ওয়ান নার।",
            'dua_pronunciation_en' => "Allahumma inni as'aluka ridaka wal-jannah, wa a'oodhu bika min sakhatika wan-nar.",
            'dua_meaning_bn' => 'হে আল্লাহ! আমি আপনার সন্তুষ্টি এবং জান্নাত প্রার্থনা করছি, আর আপনার ক্রোধ এবং জাহান্নামের আগুন থেকে আশ্রয় প্রার্থনা করছি।',
            'dua_meaning_en' => 'O Allah, I ask You for Your good pleasure and Paradise, and I seek refuge in You from Your wrath and the Fire.',
            'reference' => 'জামে আত-তিরমিযী: ২৫৫৮',
            'display_order' => 4
        ],
        [
            'stage_number' => 5,
            'stage_number_bn' => '০৫',
            'stage_number_en' => '05',
            'title_bn' => 'আরাফাত',
            'title_en' => 'Arafat',
            'description_bn' => '৯ই জিলহজ্জের সকালে মিনা থেকে আরাফাতে রওনা হন এবং সূর্যাস্ত পর্যন্ত সেখানে অবস্থান করুন। এই দিনটি "হজই আরাফাত" নামে পরিচিত। সূর্যাস্তের পূর্বে আরাফাত ত্যাগ করা উচিত নয়; এটি হজের শর্ত।',
            'description_en' => 'On the morning of the 9th of Dhul Hijjah, journey from Mina to the plains of Arafat and remain there until sunset. This sacred day embodies the essence of pilgrimage (\'Hajj is Arafah\'). Do not depart before sunset as remaining is an essential condition.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_5.png',
            'star_color_hex' => '#A3DE7C',
            'details_bn' => "উকুফে আরাফার শর্ত ও ফজিলত (৯ই জিলহজ - মূল হজ):\n• ৯ই জিলহজ দুপুর (জাওয়াল) থেকে সূর্যাস্ত পর্যন্ত আরাফাত ময়দানে অবস্থান করা হজের প্রধানতম ফরজ রুকন।\n• জোহর ও আসরের সালাত একত্রে জাম'আ তাকদীম করে আদায় করুন।\n• সূর্যাস্ত পর্যন্ত হাত তুলে ক্বিবলামুখী হয়ে অবিরাম রোনাজারি, ক্ষমা প্রার্থনা ও দোয়া করুন।\n• সূর্যাস্তের পূর্বে কোনো অবস্থাতেই আরাফাত ময়দানের সীমানা ত্যাগ করা যাবে না।",
            'details_en' => "Wuquf at Arafah Rites & Virtues (9th Dhul Hijjah - Core of Hajj):\n• Standing in the plain of Arafat from midday (Zawal) until sunset is the paramount obligatory pillar of Hajj.\n• Combine and shorten Dhuhr and Asr prayers together at the time of Dhuhr.\n• Spend the precious afternoon facing the Qiblah with raised hands, begging for forgiveness and mercy.\n• Pilgrims must strictly remain within Arafah boundaries until complete sunset.",
            'dua_arabic' => 'لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ',
            'dua_pronunciation_bn' => "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু, ওয়া হুয়া 'আলা কুল্লি শাইয়িন ক্বাদীর।",
            'dua_pronunciation_en' => "La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamdu, wa huwa 'ala kulli shay'in qadeer.",
            'dua_meaning_bn' => 'একমাত্র আল্লাহ ব্যতীত সত্য কোনো উপাস্য নেই, তাঁর কোনো শরিক নেই, রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসাও তাঁরই, আর তিনি সর্ববিষয়ে সর্বশক্তিমান।',
            'dua_meaning_en' => 'There is no deity worthy of worship except Allah alone, with no partner. His is the sovereignty and all praise, and He is over all things competent.',
            'reference' => 'জামে আত-তিরমিযী: ৩৫৮৫, মুসনাদে আহমাদ: ৬৯৬১',
            'display_order' => 5
        ],
        [
            'stage_number' => 6,
            'stage_number_bn' => '০৬',
            'stage_number_en' => '06',
            'title_bn' => 'মুজদালিফা',
            'title_en' => 'Muzdalifah',
            'description_bn' => '৯ই জিলহজ্জের সূর্যাস্তের পর মিনা থেকে মুজদালিফার উদ্দেশ্যে রওনা হন। সেখানে পৌঁছে মাগরিব ও ইশা নামাজ একত্রে কসর করে আদায় করুন। আগামী তিন দিন শয়তানকে পাথর ছোঁড়ার জন্য কমপক্ষে ৭০টি ছোট পাথর সংগ্রহ করুন। প্রতিদিন ২১টি করে পাথর প্রয়োজন।',
            'description_en' => 'After sunset on the 9th of Dhul Hijjah, depart towards Muzdalifah. Upon arrival, perform Maghrib and Isha prayers together shortened. Collect at least 70 small pebbles for the next three days of stoning the Jamarat (21 pebbles required per day).',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_6.png',
            'star_color_hex' => '#EBCA28',
            'details_bn' => "মুজদালিফায় অবস্থানের বিধানাবলী:\n• আরাফাত থেকে মাগরিব না পড়ে সরাসরি মুজদালিফায় এসে মাগরিব ও ইশা এক আজান ও দুই ইকামতে আদায় করুন।\n• মুজদালিফায় খোলা আকাশের নিচে রাত যাপন করা ওয়াজিব বা সুন্নাতে মুয়াক্কাদা।\n• জামরাতে পাথর মারার জন্য ছোলা বা মটরদানার সমান ৭০টি কঙ্কর সংগ্রহ করুন ও পরিষ্কার করে নিন।\n• ফজরের সালাত প্রথম ওয়াক্তে আদায় করে সূর্যোদয়ের পূর্ব পর্যন্ত মশ'আরুল হারামে দাঁড়িয়ে দোয়া করুন।",
            'details_en' => "Muzdalifah Rites & Directives:\n• Depart Arafat without praying Maghrib; combine Maghrib and Isha at Muzdalifah with one Adhan and two Iqamahs.\n• Spending the night under the open sky in Muzdalifah is an obligatory rite (Wajib).\n• Gather 70 chickpea-sized pebbles and rinse them carefully for stoning at the Jamarat.\n• Perform Fajr at dawn and make earnest supplication at al-Mash'ar al-Haram until sunrise.",
            'dua_arabic' => 'فَإِذَا أَفَضْتُم مِّنْ عَرَفَاتٍ فَاذْكُرُوا اللَّهَ عِندَ الْمَشْعَرِ الْحَرَامِ ۖ وَاذْكُرُوهُ كَمَا هَدَاكُمْ',
            'dua_pronunciation_bn' => "ফা ইযা আফাদতুম মিন 'আরাফাতিন ফাযকুরুল্লাহা 'ইন্দাল মাশ'আরিল হারাম, ওয়াযকুরূহু কামা হাদাকুম।",
            'dua_pronunciation_en' => "Fa-idha afadtum min 'Arafatin fadh-kurullaha 'indal-Mash'aril-Haram, wadh-kuroohu kama hadakum.",
            'dua_meaning_bn' => 'অতঃপর যখন তোমরা আরাফাত থেকে প্রত্যাবর্তন করবে, তখন মাশ\'আরুল হারামের কাছে এসে আল্লাহকে স্মরণ করো এবং তাঁকে স্মরণ করো যেভাবে তিনি তোমাদের পথ প্রদর্শন করেছেন।',
            'dua_meaning_en' => 'Then when you depart from Arafat, remember Allah at al-Mash\'ar al-Haram. And remember Him as He has guided you.',
            'reference' => 'সূরা আল-বাকারা: ১৯৮, সহীহ মুসলিম: ১২১৮',
            'display_order' => 6
        ],
        [
            'stage_number' => 7,
            'stage_number_bn' => '০৭',
            'stage_number_en' => '07',
            'title_bn' => 'জামরাত আল-আকাবায় পাথর নিক্ষেপ',
            'title_en' => 'Stoning Jamarat al-Aqabah',
            'description_bn' => '১০ই জিলহজ্জ (ঈদের দিন) সকালে মিনা ফিরে এসে জামরাত আল-আকাবাতে ৭টি পাথর নিক্ষেপ করুন। প্রতি পাথর নিক্ষেপের সময় "আল্লাহু আকবার" বলুন।',
            'description_en' => 'On the morning of the 10th of Dhul Hijjah (Eid Day), return to Mina and cast 7 pebbles at Jamarat al-Aqabah (the Big Pillar). Recite \'Allahu Akbar\' with each pebble thrown.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_7.png',
            'star_color_hex' => '#A3DE7C',
            'details_bn' => "১০ই জিলহজ রমি করার নিয়ম:\n• এ দিন শুধুমাত্র বড় স্তম্ভ (জামরাত আল-আকাবাহ)-তে ৭টি পাথর নিক্ষেপ করতে হবে।\n• প্রথম পাথর নিক্ষেপের সাথে সাথে পূর্বের তালবিয়া পাঠ বন্ধ করবেন।\n• প্রতিটি কঙ্কর নিক্ষেপকালে ডান হাত তুলে সজোরে 'আল্লাহু আকবার' বলুন।\n• পাথর সরাসরি হাউজের ভেতরে পতিত হতে হবে। অন্যকে আঘাত না করে শান্তভাবে আমলটি করুন।",
            'details_en' => "Stoning Rites on the 10th of Dhul Hijjah:\n• On this day, stone only the largest pillar (Jamarat al-Aqabah) with 7 pebbles.\n• Cease reciting the Talbiyah upon throwing the very first pebble.\n• Raise your right hand with each throw, proclaiming 'Allahu Akbar' clearly.\n• Ensure each pebble lands safely inside the basin without jostling others.",
            'dua_arabic' => 'بِسْمِ اللَّهِ، اللَّهُ أَكْبَرُ، رَغْمًا لِلشَّيْطَانِ وَرِضًا لِلرَّحْمَنِ',
            'dua_pronunciation_bn' => "বিসমিল্লাহি, আল্লাহু আকবার, রাগমান লিশ-শাইত্বানি ওয়া রিদান লির-রাহমান।",
            'dua_pronunciation_en' => "Bismillahi, Allahu Akbar, raghman lish-shaytani wa ridan lir-Rahman.",
            'dua_meaning_bn' => 'আল্লাহর নামে, আল্লাহ মহান! শয়তানের অবমাননাস্বরূপ এবং পরম করুণাময় আল্লাহর সন্তুষ্টির উদ্দেশ্যে।',
            'dua_meaning_en' => 'In the name of Allah, Allah is the Greatest! In defiance of Satan and seeking the pleasure of the Most Merciful.',
            'reference' => 'মুসনাদে আহমাদ: ১৫১৮, সুনানে বায়হাকী: ৯৬২৬',
            'display_order' => 7
        ],
        [
            'stage_number' => 8,
            'stage_number_bn' => '০৮',
            'stage_number_en' => '08',
            'title_bn' => 'আদহি (কোরবানির পশু)',
            'title_en' => 'Sacrificial Animal (Adhi)',
            'description_bn' => 'আল্লাহর নৈকট্য লাভের উদ্দেশ্যে কোরবানির পশু উৎসর্গ করুন। কোরবানির মাংসের এক-তৃতীয়াংশ গরিবদের মধ্যে বিতরণ করা সুন্নত।',
            'description_en' => 'Sacrifice a prescribed animal seeking nearness and acceptance from Allah. It is Sunnah to distribute one-third of the sacrificial meat among the needy.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_8.png',
            'star_color_hex' => '#1E8787',
            'details_bn' => "কোরবানি (দমে শোকর) সংক্রান্ত বিধান:\n• তামাত্তু ও ক্বেরান হজ আদায়কারীদের জন্য কোরবানি করা ওয়াজিব।\n• পাথর নিক্ষেপের পর এবং চুল কাটার পূর্বে কোরবানি সম্পন্ন করা মুস্তাহাব।\n• বর্তমানে সৌদি সরকারের অনুমোদিত 'আদাহী' ইসলামিক ডেভেলপমেন্ট ব্যাংক কুপনের মাধ্যমে কোরবানি সম্পন্ন করা নিরাপদ ও সুশৃঙ্খল।",
            'details_en' => "Qurbani (Hady) Directives:\n• Offering animal sacrifice is obligatory (Wajib) for pilgrims performing Tamattu and Qiran.\n• It is recommended to sacrifice after casting pebbles and before cutting/shaving hair.\n• Utilizing the authorized Saudi Adahi (Islamic Development Bank) voucher ensures hygienic distribution to the world's destitute.",
            'dua_arabic' => 'إِنَّ صَلَاتِي وَنُسُكِي وَمَحْيَايَ وَمَمَاتِي لِلَّهِ رَبِّ الْعَالَمِينَ ۝ لَا شَرِيكَ لَهُ',
            'dua_pronunciation_bn' => "ইন্না সালাতী ওয়া নুসুকী ওয়া মাহ্ইয়ায়া ওয়া মামাতী লিল্লাহি রব্বিল 'আলামীন, লা শারীকা লাহ।",
            'dua_pronunciation_en' => "Inna salati wa nusuki wa mahyaya wa mamati lillahi Rabbil-'Alameen, la shareeka lah.",
            'dua_meaning_bn' => 'নিশ্চয় আমার সালাত, আমার কোরবানি, আমার জীবন ও আমার মরণ একমাত্র বিশ্বজগতের প্রতিপালক আল্লাহর জন্য, যাঁর কোনো শরিক নেই।',
            'dua_meaning_en' => 'Indeed, my prayer, my rites of sacrifice, my living and my dying are for Allah, Lord of the worlds, with no partner.',
            'reference' => 'সূরা আল-আন\'আম: ১৬২-১৬৩',
            'display_order' => 8
        ],
        [
            'stage_number' => 9,
            'stage_number_bn' => '০৯',
            'stage_number_en' => '09',
            'title_bn' => 'চুল কাটা বা কামানো',
            'title_en' => 'Shaving or Trimming Hair',
            'description_bn' => '১০ই জিলহজ্জের ঈদের দিনে জামরাত আল-আকাবাতে পাথর নিক্ষেপ ও কোরবানি শেষে আপনার চুল কাটা বা কামানো করুন। পুরুষদের জন্য পুরো মাথা কামানো (হলক) সুন্নত। মহিলাদের জন্য মাথা কামানো নিষিদ্ধ; তারা অবশ্যই চুল কেটে (তাকসীর) বের হবেন।',
            'description_en' => 'On the day of Eid (10th Dhul Hijjah), after casting pebbles and offering sacrifice, shave or trim your hair. Shaving the entire head (Halq) is superior Sunnah for men. Shaving is prohibited for women; they must trim a fingertip\'s length (Taqseer) from their hair.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_9.png',
            'star_color_hex' => '#1E8787',
            'details_bn' => "চুল কাটা ও তাহাল্লুল সংক্রান্ত বিধান:\n• পুরুষদের জন্য মাথা কামানো (হলক) সর্বোত্তম; তবে চতুর্দিক থেকে সমানভাবে চুল ছোট করলেও চলবে।\n• মহিলারা চুলের প্রান্ত থেকে আঙুলের এক কর (প্রায় ১ ইঞ্চি) পরিমাণ কেটে নেবেন।\n• চুল কাটা সম্পন্ন হওয়ার মাধ্যমে হাজী সাহেব 'তাহাল্লুলে আসগর' (প্রথম হালাল) অর্জন করেন; ফলে স্ত্রী মিলন ব্যতীত ইহরামের সমস্ত নিষেধাজ্ঞা উঠে যায়।",
            'details_en' => "Halq/Taqseer Rites & First Deconsecration (Tahallul al-Asghar):\n• Complete head shaving (Halq) is three times more rewarded for men, though uniform trimming is valid.\n• Women gather their hair tips and trim approximately one fingertip length (approx 1 inch).\n• Upon completion, the pilgrim attains partial deconsecration; all Ihram prohibitions are lifted except marital relations.",
            'dua_arabic' => 'اللَّهُمَّ اغْفِرْ لِلْمُحَلِّقِينَ، اللَّهُمَّ ارْحَمِ الْمُحَلِّقِينَ، وَلِلْمُقَصِّرِينَ',
            'dua_pronunciation_bn' => "আল্লাহুম্মাগফির লিল মুহাল্লিক্বীন, আল্লাহুম্মারহামিল মুহাল্লিক্বীন, ওয়া লিল মুক্বাসসিরীন।",
            'dua_pronunciation_en' => "Allahummagh-fir lil-muhalliqeen, Allahummar-hamil-muhalliqeen, wa lil-muqassireen.",
            'dua_meaning_bn' => 'হে আল্লাহ! যারা মাথা কামিয়েছে তাদের ক্ষমা করুন, হে আল্লাহ! যারা মাথা কামিয়েছে তাদের ওপর রহম করুন, এবং যারা চুল ছোট করেছে তাদেরও ক্ষমা করুন।',
            'dua_meaning_en' => 'O Allah, forgive those who shave their heads. O Allah, have mercy on those who shave their heads, and also forgive those who trim their hair.',
            'reference' => 'সহীহ বুখারী: ১৭২৭, সহীহ মুসলিম: ১৩০১',
            'display_order' => 9
        ],
        [
            'stage_number' => 10,
            'stage_number_bn' => '১০',
            'stage_number_en' => '10',
            'title_bn' => 'তাওয়াফুল ইফাদাহ',
            'title_en' => 'Tawaf al-Ifadah',
            'description_bn' => '১০ই জিলহজ্জ মক্কায় ফিরে এসে ৭ চক্কর দিয়ে কা\'বা তাওয়াফ করুন। এরপর, সম্ভব হলে, মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক\'আত সুন্নত নামাজ পড়ুন।',
            'description_en' => 'On the 10th of Dhul Hijjah, return to Makkah to circumambulate the Ka\'bah 7 times. Afterwards, if feasible, pray two Rak\'ahs of Sunnah prayer behind Maqam Ibrahim.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_10.png',
            'star_color_hex' => '#2FB68E',
            'details_bn' => "তাওয়াফুল জিয়ারাহ/ইফাদাহ-এর আহকাম:\n• এটি হজের অন্যতম ফরজ রুকন। এটি না করলে কারো হজ পূর্ণ হবে না।\n• ১০ই জিলহজ থেকে ১২ই জিলহজের সূর্যাস্তের পূর্ব পর্যন্ত যেকোনো সময় এটি আদায় করা ওয়াজিব।\n• তাওয়াফ শেষে মাকামে ইবরাহীমে দুই রাকাত সালাত আদায় করুন এবং জমজমের পানি পান করুন।\n• তামাত্তু হজকারী এবং যারা পূর্বে হজের সাঈ করেননি, তারা তাওয়াফুল ইফাদাহর পর সাফা-মারওয়ায় সাঈ করবেন।\n• এই তাওয়াফের পর 'তাহাল্লুলে আকবর' সম্পন্ন হয় এবং স্ত্রী সহবাস সহ সব কিছু হালাল হয়ে যায়।",
            'details_en' => "Tawaf al-Ifadah (Tawaf az-Ziyarah) Directives:\n• This is an indispensable pillar of Hajj without which the pilgrimage remains incomplete.\n• Must be performed between 10th Dhul Hijjah and before sunset of 12th Dhul Hijjah.\n• Pray two Rak'ahs behind Maqam Ibrahim and drink pure Zamzam water to fulfillment.\n• Perform Sa'i between Safa and Marwa if not performed during Arrival Tawaf.\n• Upon completing this Tawaf, full deconsecration (Tahallul al-Akbar) is achieved.",
            'dua_arabic' => 'ثُمَّ لْيَقْضُوا تَفَثَهُمْ وَلْيُوفُوا نُذُورَهُمْ وَلْيَطَّوَّفُوا بِالْبَيْتِ الْعَتِيقِ',
            'dua_pronunciation_bn' => "সুম্মা লিয়াক্বদূ তাফাসা-হুম ওয়াল ইয়ূফূ নুযূরাহুম ওয়াল ইয়াত্বাত্বাওওয়াফূ বিল বাইতিল 'আতীক্ব।",
            'dua_pronunciation_en' => "Thumma lyaqdoo tafathahum wal-yoofoo nudhoorahum wal-yattawwafoo bil-Baytil-'Ateeq.",
            'dua_meaning_bn' => 'অতঃপর তারা যেন তাদের শারীরিক ময়লা দূর করে, তাদের মানত পূর্ণ করে এবং এই প্রাচীন ঘরের (পবিত্র কাবা) তাওয়াফ সম্পন্ন করে।',
            'dua_meaning_en' => 'Then let them end their untidiness and fulfill their vows and perform Tawaf around the Ancient House.',
            'reference' => 'সূরা আল-হাজ্জ: ২৯, সহীহ মুসলিম: ১২১৮',
            'display_order' => 10
        ],
        [
            'stage_number' => 11,
            'stage_number_bn' => '১১',
            'stage_number_en' => '11',
            'title_bn' => 'জামরাতে পাথর নিক্ষেপ',
            'title_en' => 'Stoning at Jamarat',
            'description_bn' => '১১, ১২ ও ১৩ জিলহজ্জ মিনা ফিরে এসে জামরাতের তিনটি স্তম্ভে (জামরাত আল-আকাবাহ, জামরাত আল-উস্তা, ও জামরাত আল-সুগরা) ৭টি করে পাথর নিক্ষেপ করুন। পাথর নিক্ষেপের সময় "আল্লাহু আকবার" বলুন।',
            'description_en' => 'On the 11th, 12th, and optionally 13th of Dhul Hijjah, return to Mina and cast 7 pebbles at each of the three Jamarat pillars (al-Aqabah, al-Wusta, and al-Sughra). Proclaim \'Allahu Akbar\' with each throw.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_11.png',
            'star_color_hex' => '#2FB68E',
            'details_bn' => "আইয়ামে তাশরীক্বে পাথর নিক্ষেপের নিয়মাবলী:\n• ১১ ও ১২ই জিলহজ সূর্য পশ্চিমে ঢলে যাওয়ার (জাওয়াল/দুপুর) পর পাথর নিক্ষেপের ওয়াক্ত শুরু হয়।\n• ধারাবাহিকতা রক্ষা করা ওয়াজিব: প্রথমে ছোট জামরাত (সুগরা), এরপর মধ্যম জামরাত (উস্তা), এবং শেষে বড় জামরাত (আকাবাহ)।\n• প্রতিটি স্তম্ভে ৭টি করে মোট ২১টি পাথর নিক্ষেপ করতে হবে।\n• ছোট ও মধ্যম জামরাতে রমি শেষে কিবলামুখী হয়ে দীর্ঘ সময় দুই হাত তুলে প্রাণখুলে দোয়া করা সুন্নত।",
            'details_en' => "Stoning Directives during Ayyam at-Tashreeq:\n• On the 11th and 12th of Dhul Hijjah, the stoning window begins after midday (Zawal).\n• Order is mandatory: begin with Small Jamarat (Sughra), then Middle (Wusta), concluding with Large (Aqabah).\n• Cast 7 pebbles at each pillar (totaling 21 pebbles daily).\n• After stoning the Small and Middle Jamarat, step aside facing the Qiblah for prolonged supplication.",
            'dua_arabic' => 'اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا، وَذَنْبًا مَغْفُورًا، وَسَعْيًا مَشْكُورًا',
            'dua_pronunciation_bn' => "আল্লাহুম্মাজ'আলহু হাজ্জাম মাবরূরা, ওয়া যামবাম মাগফূরা, ওয়া সা'ইয়াম মাশকূরা।",
            'dua_pronunciation_en' => "Allahummaj-'alhu hajjan mabrooran, wa dhanban maghfooran, wa sa'yan mashkooran.",
            'dua_meaning_bn' => 'হে আল্লাহ! আমার এই হজকে মাবরুর (কবুল) হজ বানিয়ে দিন, সমস্ত পাপ ক্ষমা করে দিন এবং প্রচেষ্টাকে পুরস্কৃত করুন।',
            'dua_meaning_en' => 'O Allah, make this an accepted pilgrimage (Hajj Mabrur), forgiven sin, and appreciated effort.',
            'reference' => 'মুসান্নাফ ইবনে আবি শায়বা: ১৩২২৮',
            'display_order' => 11
        ],
        [
            'stage_number' => 12,
            'stage_number_bn' => '১২',
            'stage_number_en' => '12',
            'title_bn' => 'বিদায়ী তাওয়াফ',
            'title_en' => 'Farewell Tawaf',
            'description_bn' => 'মক্কা ত্যাগের পূর্বে তাওয়াফুল বিদা করুন। এটি ৭ চক্কর দিয়ে কা\'বা তাওয়াফ করা। এ সময় "বিসমিল্লাহ, আল্লাহু আকবার" বলা সুন্নত। শেষে মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক\'আত সুন্নত নামাজ পড়া সুন্নত।',
            'description_en' => 'Before departing from the holy city of Makkah, perform the Farewell Circumambulation (Tawaf al-Wida). Circumambulate the Ka\'bah 7 circuits, reciting \'Bismillah, Allahu Akbar\'. Conclude by offering two Rak\'ahs of Sunnah prayer behind Maqam Ibrahim.',
            'image_url' => 'uploads/hajj_journey/img_hajj_journey_stage_12.png',
            'star_color_hex' => '#1E8787',
            'details_bn' => "বিদায়ী তাওয়াফ (তাওয়াফুল বিদা)-এর বিধান:\n• বহিরাগত সমস্ত হাজীর জন্য মক্কা ত্যাগ করার ঠিক পূর্বে এটি আদায় করা ওয়াজিব।\n• এতে রমল বা ইজতিবা নেই এবং এর পরে সাঈ করতে হয় না।\n• সাত চক্কর শেষে মাকামে ইবরাহীমে দুই রাকাত সালাত আদায় করুন।\n• মুলতাযামে বুক-কপাল ঠেকিয়ে অশ্রুসজল নয়নে কা'বা শরীফের উদ্দেশ্যে শেষ সালাম ও দোয়া পেশ করুন এবং বিদায়ের মুহূর্তে বেশি বেশি দরুদ পড়ুন।",
            'details_en' => "Farewell Tawaf (Tawaf al-Wida) Protocols:\n• Obligatory (Wajib) for all out-of-town pilgrims just prior to leaving Makkah.\n• There is no Ramal, Idtiba, or Sa'i following this circumambulation.\n• Pray two Rak'ahs of Sunnah behind Maqam Ibrahim and drink Zamzam with gratitude.\n• Stand humbly at the Multazam, pouring out your soul in farewell supplications, wishing to return to Allah's Sacred House.",
            'dua_arabic' => 'اللَّهُمَّ لَا تَجْعَلْ هَذَا آخِرَ الْعَهْدِ بِبَيْتِكَ الْحَرَامِ، وَإِنْ جَعَلْتَهُ فَاعْوِضْنِي عَنْهُ الْجَنَّةَ',
            'dua_pronunciation_bn' => "আল্লাহুম্মা লা তাজ'আল হাযা আখিরাল 'আহদি বি-বাইতিকাল হারাম, ওয়া ইন জা'আলতাহু ফা'বিদনী 'আনহুল জান্নাহ।",
            'dua_pronunciation_en' => "Allahumma la taj'al hadha akhiral-'ahdi bi-baytikal-haram, wa in ja'altahu fa-'awidnee 'anhul-jannah.",
            'dua_meaning_bn' => 'হে আল্লাহ! আপনার সম্মানিত পবিত্র ঘরের সাথে এটিই যেন আমার শেষ সাক্ষাৎ না হয়; আর যদি শেষ সময় হয়েই থাকে, তবে এর বিনিময়ে আমাকে জান্নাত দান করবেন।',
            'dua_meaning_en' => 'O Allah, do not make this my final visit to Your Sacred House; and if You have decreed it so, compensate me with Paradise in return.',
            'reference' => 'সহীহ বুখারী: ১৭৫৫, সহীহ মুসলিম: ১৩২৮',
            'display_order' => 12
        ]
    ];

    $insertSql = "
    INSERT INTO `hajj_journey_stages`
    (`stage_number`, `stage_number_bn`, `stage_number_en`, `title_bn`, `title_en`,
     `description_bn`, `description_en`, `image_url`, `star_color_hex`, `details_bn`, `details_en`,
     `dua_arabic`, `dua_pronunciation_bn`, `dua_pronunciation_en`, `dua_meaning_bn`, `dua_meaning_en`,
     `reference`, `is_active`, `display_order`)
    VALUES
    (:stage_number, :stage_number_bn, :stage_number_en, :title_bn, :title_en,
     :description_bn, :description_en, :image_url, :star_color_hex, :details_bn, :details_en,
     :dua_arabic, :dua_pronunciation_bn, :dua_pronunciation_en, :dua_meaning_bn, :dua_meaning_en,
     :reference, 1, :display_order)
    ";

    $stmt = $pdo->prepare($insertSql);

    foreach ($stages as $stage) {
        $stmt->execute([
            ':stage_number' => $stage['stage_number'],
            ':stage_number_bn' => $stage['stage_number_bn'],
            ':stage_number_en' => $stage['stage_number_en'],
            ':title_bn' => $stage['title_bn'],
            ':title_en' => $stage['title_en'],
            ':description_bn' => $stage['description_bn'],
            ':description_en' => $stage['description_en'],
            ':image_url' => $stage['image_url'],
            ':star_color_hex' => $stage['star_color_hex'],
            ':details_bn' => $stage['details_bn'],
            ':details_en' => $stage['details_en'],
            ':dua_arabic' => $stage['dua_arabic'],
            ':dua_pronunciation_bn' => $stage['dua_pronunciation_bn'],
            ':dua_pronunciation_en' => $stage['dua_pronunciation_en'],
            ':dua_meaning_bn' => $stage['dua_meaning_bn'],
            ':dua_meaning_en' => $stage['dua_meaning_en'],
            ':reference' => $stage['reference'],
            ':display_order' => $stage['display_order']
        ]);
    }

    echo "SUCCESS: hajj_journey_stages table created and " . count($stages) . " stages seeded successfully!\n";

} catch (Exception $e) {
    echo "ERROR: " . $e->getMessage() . "\n";
    exit(1);
}
