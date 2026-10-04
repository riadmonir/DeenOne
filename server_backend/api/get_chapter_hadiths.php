<?php
/**
 * ==============================================================================
 * DEEN ONE REST API - GET CHAPTER HADITHS & SECTIONS (অধ্যায়ভিত্তিক পূর্ণ হাদীস)
 * ==============================================================================
 */
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/db.php';

$bookSlug = trim($_GET['book_slug'] ?? $_GET['book'] ?? 'bukhari');
$chapterNumber = max(1, (int)($_GET['chapter'] ?? $_GET['chapter_number'] ?? 1));

try {
    $pdo = getDbConnection();

    // 1. Auto-create hadith_sections table if not exists
    $pdo->exec("CREATE TABLE IF NOT EXISTS `hadith_sections` (
        `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        `book_slug` VARCHAR(60) NOT NULL,
        `chapter_number` INT UNSIGNED NOT NULL,
        `section_number` VARCHAR(30) NOT NULL,
        `section_tag_bn` VARCHAR(100) NOT NULL,
        `section_tag_en` VARCHAR(100) DEFAULT NULL,
        `section_title_bn` VARCHAR(255) NOT NULL,
        `section_title_en` VARCHAR(255) DEFAULT NULL,
        `arabic_verse` TEXT DEFAULT NULL,
        `verse_translation_bn` TEXT DEFAULT NULL,
        `verse_translation_en` TEXT DEFAULT NULL,
        `display_order` INT UNSIGNED DEFAULT 1,
        `is_active` TINYINT(1) DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        INDEX `idx_book_chap_order` (`book_slug`, `chapter_number`, `display_order`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

    // Check / Add missing columns to hadith_items if needed
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `hadith_number_bn` VARCHAR(50) DEFAULT '' AFTER `hadith_number`");
    } catch (Exception $ignored) {}
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `english_text` TEXT DEFAULT NULL AFTER `bangla_text`");
    } catch (Exception $ignored) {}
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `narrator_en` VARCHAR(255) DEFAULT NULL AFTER `narrator_bn`");
    } catch (Exception $ignored) {}
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `footnote_bn` TEXT DEFAULT NULL AFTER `reference`");
    } catch (Exception $ignored) {}
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `footnote_en` TEXT DEFAULT NULL AFTER `footnote_bn`");
    } catch (Exception $ignored) {}
    try {
        $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `words_json` LONGTEXT DEFAULT NULL AFTER `footnote_en`");
    } catch (Exception $ignored) {}

    // 2. Seed Default Bukhari Chapter 1 Sections if empty
    if ($bookSlug === 'bukhari' && $chapterNumber === 1) {
        $secCount = (int)$pdo->query("SELECT COUNT(*) FROM hadith_sections WHERE book_slug = 'bukhari' AND chapter_number = 1")->fetchColumn();
        if ($secCount == 0) {
            $stmtSec = $pdo->prepare("INSERT INTO hadith_sections 
                (book_slug, chapter_number, section_number, section_tag_bn, section_tag_en, section_title_bn, section_title_en, arabic_verse, verse_translation_bn, verse_translation_en, display_order, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
            
            // Section 1/1
            $stmtSec->execute([
                'bukhari',
                1,
                '১/১',
                '১/১. অধ্যায়ঃ',
                '1/1. Chapter:',
                'আল্লাহর রসূল (ﷺ)-এর প্রতি কীভাবে ওহী শুরু হয়েছিল।',
                "How the Divine Revelation started being revealed to Allah's Messenger (ﷺ).",
                'وَقَوْلُ اللَّهِ جَلَّ ذِكْرُهُ { إِنَّا أَوْحَيْنَا إِلَيْكَ كَمَا أَوْحَيْنَا إِلَىٰ نُوحٍ وَالنَّبِيِّينَ مِنْ بَعْدِهِ }',
                'এ মর্মে আল্লাহ তা‘আলার বাণী: “নিশ্চয় আমি আপনার প্রতি সেরূপ ওহী প্রেরণ করেছি, যেরূপ নূহ ও তাঁর পরবর্তী নবীদের প্রতি ওহী প্রেরণ করেছিলাম।” (সূরা আন-নিসা ৪/১৬৩)',
                'And the Statement of Allah the Almighty: "Indeed, We have revealed to you, [O Muhammad], as We revealed to Noah and the prophets after him." (Surah An-Nisa 4:163)',
                1
            ]);

            // Section 1/2
            $stmtSec->execute([
                'bukhari',
                1,
                '১/২',
                '১/২. অধ্যায়ঃ',
                '1/2. Chapter:',
                'ওয়াহীর সূচনাকালীন অবস্থা',
                'The State During Revelation',
                '',
                '',
                '',
                3
            ]);
        }

        // Seed/Update Hadiths 1-7 for Bukhari Chapter 1 if needed
        $hCount = (int)$pdo->query("SELECT COUNT(*) FROM hadith_items WHERE book_slug = 'bukhari' AND chapter_number = 1")->fetchColumn();
        if ($hCount < 7) {
            $words1 = json_encode([
                ["arabic" => "إِنَّمَا", "bn" => "নিশ্চয়ই", "en" => "Only"],
                ["arabic" => "الأَعْمَالُ", "bn" => "সকল কাজ", "en" => "the deeds"],
                ["arabic" => "بِالنِّيَّاتِ", "bn" => "নিয়তের ওপর নির্ভরশীল", "en" => "by intentions"],
                ["arabic" => "وَإِنَّمَا", "bn" => "এবং নিশ্চয়ই", "en" => "and surely"],
                ["arabic" => "لِكُلِّ امْرِئٍ", "bn" => "প্রত্যেক ব্যক্তির জন্য", "en" => "for every person"],
                ["arabic" => "مَا نَوَى", "bn" => "যা সে নিয়ত করেছে", "en" => "what he intended"],
                ["arabic" => "فَمَنْ كَانَتْ", "bn" => "অতএব যার হলো", "en" => "so whoever had"],
                ["arabic" => "هِجْرَتُهُ", "bn" => "তার হিজরত", "en" => "his emigration"],
                ["arabic" => "إِلَى دُنْيَا", "bn" => "দুনিয়ার উদ্দেশ্যে", "en" => "for the world"],
                ["arabic" => "يُصِيبُهَا", "bn" => "তা হাসিল করার", "en" => "to obtain it"],
                ["arabic" => "أَوْ إِلَى امْرَأَةٍ", "bn" => "অথবা কোন নারীর জন্য", "en" => "or for a woman"],
                ["arabic" => "يَنْكِحُهَا", "bn" => "তাকে বিয়ে করার", "en" => "to marry her"],
                ["arabic" => "فَهِجْرَتُهُ", "bn" => "তবে তার হিজরত", "en" => "then his emigration"],
                ["arabic" => "إِلَى مَا هَاجَرَ إِلَيْهِ", "bn" => "সে উদ্দেশ্যেই গণ্য হবে যেজন্য হিজরত করেছে", "en" => "was for what he emigrated for"]
            ], JSON_UNESCAPED_UNICODE);

            $words2 = json_encode([
                ["arabic" => "كَيْفَ", "bn" => "কীভাবে", "en" => "How"],
                ["arabic" => "يَأْتِيكَ", "bn" => "আপনার কাছে আসে", "en" => "comes to you"],
                ["arabic" => "الْوَحْيُ", "bn" => "ওহী", "en" => "the revelation"],
                ["arabic" => "أَحْيَانًا", "bn" => "কখনও কখনও", "en" => "sometimes"],
                ["arabic" => "مِثْلَ", "bn" => "মতো", "en" => "like"],
                ["arabic" => "صَلْصَلَةِ", "bn" => "ঝনঝনানি আওয়াজ", "en" => "ringing sound"],
                ["arabic" => "الْجَرَسِ", "bn" => "ঘণ্টার", "en" => "of the bell"],
                ["arabic" => "وَهُوَ أَشَدُّهُ", "bn" => "এবং তা সবচেয়ে কষ্টকর", "en" => "and it is hardest"],
                ["arabic" => "عَلَيَّ", "bn" => "আমার ওপর", "en" => "upon me"]
            ], JSON_UNESCAPED_UNICODE);

            $bukhariChapter1Hadiths = [
                [
                    'num' => 1,
                    'narrator_bn' => "‘আলক্বামাহ ইবনু ওয়াক্কাস আল-লায়সী (রহঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by 'Alqama bin Waqas Al-Laithi:",
                    'ar' => "حَدَّثَنَا الْحُمَيْدِيُّ عَبْدُ اللَّهِ بْنُ الزُّبَيْرِ ، قَالَ حَدَّثَنَا سُفْيَانُ ، قَالَ حَدَّثَنَا يَحْيَى بْنُ سَعِيدٍ الأَنْصَارِيُّ ، قَالَ أَخْبَرَنِي مُحَمَّدُ بْنُ إِبْرَاهِيمَ التَّيْمِيُّ ، أَنَّهُ سَمِعَ عَلْقَمَةَ بْنَ وَقَّاصٍ اللَّيْثِيَّ ، يَقُولُ سَمِعْتُ عُمَرَ بْنَ الْخَطَّابِ - رضي الله عنه - عَلَى الْمِنْبَرِ قَالَ سَمِعْتُ رَسُولَ اللَّهِ صلى الله عليه وسلم يَقُولُ \" إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى ، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ \"",
                    'bn' => "আমি উমর ইবনুল খাত্তাব (রাঃ)-কে মিম্বরের উপর দাঁড়িয়ে বলতে শুনেছিঃ আমি আল্লাহর রসূল (ﷺ)-কে বলতে শুনেছিঃ আমল (এর প্রাপ্য হবে) নিয়ত অনুযায়ী। আর মানুষ তার নিয়ত অনুযায়ী প্রতিফল পাবে।\n\nতাই যার হিজরত হবে দুনিয়া লাভের অথবা কোন মহিলাকে বিবাহ করার উদ্দেশ্যে- তবে তার হিজরত সে উদ্দেশ্যেই হবে, যে জন্যে, সে হিজরত করেছে।",
                    'en' => "I heard 'Umar bin Al-Khattab speaking from the pulpit saying, \"I heard Allah's Messenger (ﷺ) saying, 'The reward of deeds depends upon the intentions and every person will get the reward according to what he has intended.'\n\nSo whoever emigrated for worldly benefits or for a woman to marry, his emigration was for what he emigrated for.\"",
                    'fn_bn' => "(৫৪, ২৫২৯, ৩৮৯৮, ৫০৭০, ৬৬৮৯, ৬৯৫৩; মুসলিম ২৩/৪৫ হাঃ ১৯০৭, আহমাদ ১৬৮)\n( আধুনিক প্রকাশনী- ১, ইসলামিক ফাউন্ডেশন ১)",
                    'fn_en' => "(54, 2529, 3898, 5070, 6689, 6953; Muslim 23/45 H: 1907, Ahmad 168)\n(Modern Publication: 1, Islamic Foundation: 1)",
                    'words' => $words1
                ],
                [
                    'num' => 2,
                    'narrator_bn' => "উম্মুল মু’মিনীন ‘আয়েশা (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by Mother of the Believers 'Aisha (RA):",
                    'ar' => "حَدَّثَنَا عَبْدُ اللَّهِ بْنُ يُوسُفَ ، قَالَ أَخْبَرَنَا مَالِكٌ ، عَنْ هِشَامِ بْنِ عُرْوَةَ ، عَنْ أَبِيهِ ، عَنْ عَائِشَةَ أُمِّ الْمُؤْمِنِينَ رَضِيَ اللَّهُ عَنْهَا ، أَنَّ الْحَارِثَ بْنَ هِشَامٍ رَضِيَ اللَّهُ عَنْهُ سَأَلَ رَسُولَ اللَّهِ صلى الله عليه وسلم فَقَالَ : يَا رَسُولَ اللَّهِ ، كَيْفَ يَأْتِيكَ الْوَحْيُ ؟ فَقَالَ رَسُولُ اللَّهِ صلى الله عليه وسلم : \" أَحْيَانًا يَأْتِينِي مِثْلَ صَلْصَلَةِ الْجَرَسِ ، وَهُوَ أَشَدُّهُ عَلَيَّ ، فَيُفْصَمُ عَنِّي وَقَدْ وَعَيْتُ عَنْهُ مَا قَالَ ، وَأَحْيَانًا يَتَمَثَّلُ لِيَ الْمَلَكُ رَجُلاً فَيُكَلِّمُنِي فَأَعِي مَا يَقُولُ \" . قَالَتْ عَائِشَةُ رَضِيَ اللَّهُ عَنْهَا : وَلَقَدْ رَأَيْتُهُ يَنْزِلُ عَلَيْهِ الْوَحْيُ فِي الْيَوْمِ الشَّدِيدِ الْبَرْدِ ، فَيَفْصِمُ عَنْهُ وَإِنَّ جَبِينَهُ لَيَتَفَصَّدُ عَرَقًا .",
                    'bn' => "হারিস ইবনু হিশাম (রাঃ) আল্লাহর রসূল (ﷺ)-কে জিজ্ঞেস করলেন, ‘হে আল্লাহর রসূল! আপনার নিকট ওয়াহী কীভাবে আসে?’ আল্লাহর রসূল (ﷺ) বললেনঃ ‘কোন কোন সময় তা ঘণ্টার শব্দের ন্যায় আমার নিকট আসে। আর এটি আমার জন্য সবচেয়ে কষ্টদায়ক হয়। অতঃপর তা সমাপ্ত হতেই ফিরিশতা যা বলেন, আমি তা মুখস্থ করে নিই। আবার কখনো ফিরিশতা মানুষের আকৃতি ধারণ করে আমার সাথে কথা বলেন, তখন তিনি যা বলেন আমি তা আয়ত্ত করে নিই।’ ‘আয়েশা (রাঃ) বলেন, ‘আমি প্রচণ্ড শীতের দিনে তাঁর ওপর ওয়াহী নাযিল হতে দেখেছি। অতঃপর তা সমাপ্ত হতেই তাঁর কপাল থেকে ঘাম ঝরে পড়ত।’",
                    'en' => "Al-Harith bin Hisham asked Allah's Messenger (ﷺ), \"O Allah's Messenger! How is the Divine Inspiration revealed to you?\" Allah's Messenger (ﷺ) replied, \"Sometimes it is revealed like the ringing of a bell, this form of Inspiration is the hardest of all and then this state passes off after I have grasped what is inspired. Sometimes the Angel comes in the form of a man and talks to me and I grasp whatever he says.\" 'Aisha added: Verily I saw the Prophet (ﷺ) being inspired Divinely on a very cold day and noticed the Sweat dropping from his forehead.",
                    'fn_bn' => "(৩২১৫; মুসলিম ৪৩/২৩ হাঃ ২৩৩৩, আহমাদ ২৫৩৭১)\n(আধুনিক প্রকাশনী- ২, ইসলামিক ফাউন্ডেশন ২)",
                    'fn_en' => "(3215; Muslim 43/23 H: 2333, Ahmad 25371)\n(Modern Publication: 2, Islamic Foundation: 2)",
                    'words' => $words2
                ],
                [
                    'num' => 3,
                    'narrator_bn' => "উম্মুল মু’মিনীন ‘আয়েশা (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by Mother of the Believers 'Aisha (RA):",
                    'ar' => "حَدَّثَنَا يَحْيَى بْنُ بُكَيْرٍ ، قَالَ حَدَّثَنَا اللَّيْثُ ، عَنْ عُقَيْلٍ ، عَنِ ابْنِ شِهَابٍ ، عَنْ عُرْوَةَ بْنِ الزُّبَيْرِ ، عَنْ عَائِشَةَ أُمِّ الْمُؤْمِنِينَ أَنَّهَا قَالَتْ : أَوَّلُ مَا بُدِئَ بِهِ رَسُولُ اللَّهِ صلى الله عليه وسلم مِنَ الْوَحْيِ الرُّؤْيَا الصَّالِحَةُ فِي النَّوْمِ ، فَكَانَ لاَ يَرَى رُؤْيَا إِلاَّ جَاءَتْ مِثْلَ فَلَقِ الصُّبْحِ ، ثُمَّ حُبِّبَ إِلَيْهِ الْخَلاَءُ ، وَكَانَ يَخْلُو بِغَارِ حِرَاءٍ فَيَتَحَنَّثُ فِيهِ...",
                    'bn' => "আল্লাহর রসূল (ﷺ)-এর নিকট সর্বপ্রথম যে ওয়াহী আসে, তা ছিল নিদ্রাবস্থায় সত্য স্বপ্নরূপে। যে স্বপ্নই তিনি দেখতেন তা শুভ্র প্রভাতের ন্যায় সত্য প্রমাণিত হত। এরপর তাঁর নিকট নির্জনতা প্রিয় হয়ে ওঠে। তিনি হেরা গুহায় একাকী অবস্থান করতেন এবং সেখানে পরিবারবর্গের কাছে না এসে একাধারে কয়েক রাত ‘তাহান্নুছ’ অর্থাৎ ইবাদতে নিমগ্ন থাকতেন...",
                    'en' => "The commencement of the Divine Inspiration to Allah's Messenger (ﷺ) was in the form of good dreams which came true like bright daylight, and then the love of seclusion was bestowed upon him. He used to go in seclusion in the cave of Hira where he used to worship Allah alone continuously for many days...",
                    'fn_bn' => "(৪৯৫৩, ৪৯৫৪; মুসলিম ৪৩/৭৩ হাঃ ১৬০, আহমাদ ২৫৬১৩)\n(আধুনিক প্রকাশনী- ৩, ইসলামিক ফাউন্ডেশন ৩)",
                    'fn_en' => "(4953, 4954; Muslim 43/73 H: 160, Ahmad 25613)\n(Modern Publication: 3, Islamic Foundation: 3)",
                    'words' => '[]'
                ],
                [
                    'num' => 4,
                    'narrator_bn' => "জাবির ইবনু ‘আবদুল্লাহ (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by Jabir bin 'Abdullah (RA):",
                    'ar' => "حَدَّثَنَا مُوسَى بْنُ إِسْمَاعِيلَ ، قَالَ حَدَّثَنَا أَبُو عَوَانَةَ ، قَالَ حَدَّثَنَا يَحْيَى بْنُ أَبِي كَثِيرٍ ، عَنْ أَبِي سَلَمَةَ ، عَنْ جَابِرِ بْنِ عَبْدِ اللَّهِ رَضِيَ اللَّهُ عَنْهُمَا قَالَ : وَهُوَ يُحَدِّثُ عَنْ فَتْرَةِ الْوَحْيِ فَقَالَ فِي حَدِيثِهِ : \" بَيْنَا أَنَا أَمْشِي إِذْ سَمِعْتُ صَوْتًا مِنَ السَّمَاءِ ، فَرَفَعْتُ بَصَرِي فَإِذَا الْمَلَكُ الَّذِي جَاءَنِي بِحِرَاءٍ جَالِسٌ عَلَى كُرْسِيٍّ بَيْنَ السَّمَاءِ وَالأَرْضِ...\"",
                    'bn' => "তিনি ওয়াহী স্থগিত থাকা প্রসঙ্গে বর্ণনা করতে গিয়ে বলেন, আল্লাহর রসূল (ﷺ) তাঁর কথায় বললেনঃ ‘একদা আমি হেঁটে যাচ্ছিলাম, হঠাৎ আসমান থেকে একটি শব্দ শুনতে পেয়ে চোখ তুলে তাকালাম। দেখলাম, সেই ফিরিশতা যিনি হেরা গুহায় আমার নিকট এসেছিলেন, আসমান ও যমীনের মাঝে এক কুরসীতে বসে আছেন...’",
                    'en' => "While speaking of the period of interval in revelation, the Prophet (ﷺ) said, 'While I was walking, all of a sudden I heard a voice from the sky. I looked up and saw the same angel who had visited me at the cave of Hira sitting on a chair between the sky and the earth...'",
                    'fn_bn' => "(৩২৩৮, ৪৯২২, ৪৯২৪, ৪৯২৫, ৪৯২৬, ৪৯৫৭; মুসলিম ১/৭৩ হাঃ ১৬১, আহমাদ ১৪৬২৫)\n(আধুনিক প্রকাশনী- ৪, ইসলামিক ফাউন্ডেশন ৪)",
                    'fn_en' => "(3238, 4922, 4924, 4925, 4926, 4957; Muslim 1/73 H: 161, Ahmad 14625)\n(Modern Publication: 4, Islamic Foundation: 4)",
                    'words' => '[]'
                ],
                [
                    'num' => 5,
                    'narrator_bn' => "ইবনু ‘আব্বাস (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by Ibn 'Abbas (RA):",
                    'ar' => "حَدَّثَنَا عَبْدَانُ ، قَالَ أَخْبَرَنَا عَبْدُ اللَّهِ ، قَالَ أَخْبَرَنَا يُونُسُ ، عَنِ الزُّهْرِيِّ ، ح وَحَدَّثَنَا بِشْرُ بْنُ مُحَمَّدٍ ، قَالَ أَخْبَرَنَا عَبْدُ اللَّهِ ، قَالَ أَخْبَرَنَا يُونُسُ ، وَمَعْمَرٌ ، عَنِ الزُّهْرِيِّ ، نَحْوَهُ قَالَ أَخْبَرَنِي عُبَيْدُ اللَّهِ بْنُ عَبْدِ اللَّهِ ، عَنِ ابْنِ عَبَّاسٍ ، فِي قَوْلِهِ تَعَالَى : { لاَ تُحَرِّكْ بِهِ لِسَانَكَ لِتَعْجَلَ بِهِ } قَالَ : كَانَ رَسُولُ اللَّهِ صلى الله عليه وسلم يُعَالِجُ مِنَ التَّنْزِيلِ شِدَّةً ، وَكَانَ مِمَّا يُحَرِّكُ شَفَتَيْهِ...",
                    'bn' => "আল্লাহর বাণী: ‘তাড়াতাড়ি ওহী আয়ত্ত করার জন্য আপনি আপনার জিহ্বা নাড়বেন না।’ এ প্রসঙ্গে তিনি বলেন, আল্লাহর রসূল (ﷺ) ওহী নাযিল হওয়ার সময় তীব্র কষ্ট সহ্য করতেন এবং প্রায়ই তাঁর ওষ্ঠাধর নাড়তেন...",
                    'en' => "Regarding the Verse: 'Move not your tongue concerning (the Quran) to make haste therewith.' Ibn 'Abbas explained: Allah's Messenger (ﷺ) used to bear the revelation with great effort and used to move his lips rapidly...",
                    'fn_bn' => "(৪৯২৭, ৪৯২৮, ৪৯২৯, ৫০৪৩, ৭৫২৪; মুসলিম ৪/৩২ হাঃ ৪৪৮, আহমাদ ৩১৯২)\n(আধুনিক প্রকাশনী- ৫, ইসলামিক ফাউন্ডেশন ৫)",
                    'fn_en' => "(4927, 4928, 4929, 5043, 7524; Muslim 4/32 H: 448, Ahmad 3192)\n(Modern Publication: 5, Islamic Foundation: 5)",
                    'words' => '[]'
                ],
                [
                    'num' => 6,
                    'narrator_bn' => "ইবনু ‘আব্বাস (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by Ibn 'Abbas (RA):",
                    'ar' => "حَدَّثَنَا عَبْدَانُ ، قَالَ حَدَّثَنَا عَبْدُ اللَّهِ ، عَنْ يُونُسَ ، عَنِ الزُّهْرِيِّ ، ح وَحَدَّثَنَا بِشْرُ بْنُ مُحَمَّدٍ ، قَالَ حَدَّثَنَا عَبْدُ اللَّهِ ، عَنْ يُونُسَ ، وَمَعْمَرٍ ، عَنِ الزُّهْرِيِّ ، قَالَ أَخْبَرَنِي عُبَيْدُ اللَّهِ بْنُ عَبْدِ اللَّهِ ، عَنِ ابْنِ عَبَّاسٍ قَالَ : كَانَ رَسُولُ اللَّهِ صلى الله عليه وسلم أَجْوَدَ النَّاسِ ، وَكَانَ أَجْوَدُ مَا يَكُونُ فِي رَمَضَانَ حِينَ يَلْقَاهُ جِبْرِيلُ...",
                    'bn' => "আল্লাহর রসূল (ﷺ) সর্বাপেক্ষা দানশীল ছিলেন। রমযানে যখন জিবরীল (আঃ) তাঁর সাথে সাক্ষাৎ করতেন, তখন তিনি আরও অধিক দানশীল হতেন...",
                    'en' => "Allah's Messenger (ﷺ) was the most generous of all the people, and he used to reach the peak in generosity in the month of Ramadan when Jibril used to meet him...",
                    'fn_bn' => "(১৯০২, ৩২২০, ৩৫৫৪, ৪৯৯৭; মুসলিম ৪৩/১২ হাঃ ২৩০৮, আহমাদ ৩৪৩১)\n(আধুনিক প্রকাশনী- ৬, ইসলামিক ফাউন্ডেশন ৬)",
                    'fn_en' => "(1902, 3220, 3554, 4997; Muslim 43/12 H: 2308, Ahmad 3431)\n(Modern Publication: 6, Islamic Foundation: 6)",
                    'words' => '[]'
                ],
                [
                    'num' => 7,
                    'narrator_bn' => "‘আবদুল্লাহ ইবনু ‘আব্বাস (রাঃ) থেকে বর্ণিত:",
                    'narrator_en' => "Narrated by 'Abdullah bin 'Abbas (RA):",
                    'ar' => "حَدَّثَنَا أَبُو الْيَمَانِ الْحَكَمُ بْنُ نَافِعٍ ، قَالَ أَخْبَرَنَا شُعَيْبٌ ، عَنِ الزُّهْرِيِّ ، قَالَ أَخْبَرَنِي عُبَيْدُ اللَّهِ بْنُ عَبْدِ اللَّهِ بْنِ عُتْبَةَ بْنِ مَسْعُودٍ ، أَنَّ عَبْدَ اللَّهِ بْنَ عَبَّاسٍ أَخْبَرَهُ أَنَّ أَبَا سُفْيَانَ بْنَ حَرْبٍ أَخْبَرَهُ أَنَّ هِرَقْلَ أَرْسَلَ إِلَيْهِ فِي رَكْبٍ مِنْ قُرَيْشٍ...",
                    'bn' => "আবূ সুফিয়ান ইবনু হারব তাঁকে জানিয়েছেন যে, হিরাক্লিয়াস একদা কুরাইশদের এক কাফেলার মধ্যে তাঁকে ডেকে পাঠালেন। তিনি তাদের সামনে নবী (ﷺ)-এর নবুওয়াতের সত্যতার আলামতসমূহ নিয়ে বিস্তারিত জিজ্ঞাসাবাদ করেন...",
                    'en' => "Abu Sufyan bin Harb informed him that Heraclius had sent a messenger to him while he had been accompanying a caravan from Quraish. Heraclius asked detailed questions regarding the signs and truthfulness of the Prophet (ﷺ)...",
                    'fn_bn' => "(৫১, ২৬৮১, ২৯৪১, ২৯৫৩, ২৯৭৮, ৩১৭৪, ৪৫৫৩; মুসলিম ৩২/২৬ হাঃ ১৭৭৩, আহমাদ ২০২৪)\n(আধুনিক প্রকাশনী- ৭, ইসলামিক ফাউন্ডেশন ৭)",
                    'fn_en' => "(51, 2681, 2941, 2953, 2978, 3174, 4553; Muslim 32/26 H: 1773, Ahmad 2024)\n(Modern Publication: 7, Islamic Foundation: 7)",
                    'words' => '[]'
                ]
            ];

            $insH = $pdo->prepare("INSERT INTO hadith_items 
                (book_slug, chapter_number, hadith_number, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, footnote_bn, footnote_en, words_json, grade, reference)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'সহীহ (Sahih)', ?)
                ON DUPLICATE KEY UPDATE 
                    narrator_bn = VALUES(narrator_bn),
                    narrator_en = VALUES(narrator_en),
                    arabic_text = VALUES(arabic_text),
                    bangla_text = VALUES(bangla_text),
                    english_text = VALUES(english_text),
                    footnote_bn = VALUES(footnote_bn),
                    footnote_en = VALUES(footnote_en),
                    words_json = VALUES(words_json)");

            foreach ($bukhariChapter1Hadiths as $bh) {
                $ref = "সহীহ বুখারী: " . $bh['num'];
                $insH->execute([
                    'bukhari',
                    1,
                    $bh['num'],
                    $bh['narrator_bn'],
                    $bh['narrator_en'],
                    $bh['ar'],
                    $bh['bn'],
                    $bh['en'],
                    $bh['fn_bn'],
                    $bh['fn_en'],
                    $bh['words'],
                    $ref
                ]);
            }
        }
    }

    // 3. Query Sections and Hadiths
    $secStmt = $pdo->prepare("SELECT * FROM hadith_sections WHERE book_slug = ? AND chapter_number = ? AND is_active = 1 ORDER BY display_order ASC");
    $secStmt->execute([$bookSlug, $chapterNumber]);
    $sections = $secStmt->fetchAll();

    $hadithStmt = $pdo->prepare("SELECT h.*, b.name_bn as book_name_bn, b.name_en as book_name_en 
                                  FROM hadith_items h 
                                  LEFT JOIN hadith_books b ON h.book_slug = b.book_slug 
                                  WHERE h.book_slug = ? AND h.chapter_number = ? 
                                  ORDER BY h.hadith_number ASC");
    $hadithStmt->execute([$bookSlug, $chapterNumber]);
    $hadiths = $hadithStmt->fetchAll();

    // Bengali numbers helper
    function toBnNum($num) {
        $en = ['0','1','2','3','4','5','6','7','8','9'];
        $bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
        return str_replace($en, $bn, (string)$num);
    }

    // Build interleaved response items matching HadithReaderItem.java:
    // TYPE_SECTION_HEADER = 1
    // TYPE_HADITH = 2
    $items = [];

    // Check sections
    $secMap = [];
    foreach ($sections as $s) {
        $secMap[$s['display_order']] = $s;
    }

    $hadithIndex = 0;
    $totalHadiths = count($hadiths);

    // Section 1/1 at the very beginning (order 1)
    if (isset($secMap[1])) {
        $s1 = $secMap[1];
        $items[] = [
            'type' => 1,
            'section_tag' => $s1['section_tag_bn'],
            'section_title' => $s1['section_title_bn'],
            'section_arabic_verse' => $s1['arabic_verse'] ?? '',
            'section_translation' => $s1['verse_translation_bn'] ?? ''
        ];
    }

    // First Hadith (Hadith 1)
    if ($totalHadiths > 0) {
        $h1 = $hadiths[0];
        $words = !empty($h1['words_json']) ? json_decode($h1['words_json'], true) : [];
        $items[] = [
            'type' => 2,
            'id' => (int)$h1['id'],
            'book_slug' => $h1['book_slug'],
            'book_name_bn' => $h1['book_name_bn'] ?? 'সহীহ বুখারী',
            'book_name_en' => $h1['book_name_en'] ?? 'Sahih al-Bukhari',
            'hadith_number' => (int)$h1['hadith_number'],
            'hadith_number_bn' => toBnNum($h1['hadith_number']),
            'grade_bn' => 'সহিহ হাদিস',
            'grade_en' => 'Sahih Hadith',
            'arabic_text' => $h1['arabic_text'],
            'narrator_bn' => $h1['narrator_bn'],
            'narrator_en' => $h1['narrator_en'] ?? '',
            'bangla_text' => $h1['bangla_text'],
            'english_text' => $h1['english_text'] ?? '',
            'footnote_bn' => $h1['footnote_bn'] ?? '',
            'footnote_en' => $h1['footnote_en'] ?? '',
            'words' => $words
        ];
    }

    // Section 1/2 (order 3)
    if (isset($secMap[3])) {
        $s2 = $secMap[3];
        $items[] = [
            'type' => 1,
            'section_tag' => $s2['section_tag_bn'],
            'section_title' => $s2['section_title_bn'],
            'section_arabic_verse' => $s2['arabic_verse'] ?? '',
            'section_translation' => $s2['verse_translation_bn'] ?? ''
        ];
    }

    // Remaining Hadiths (Hadiths 2 to N)
    for ($i = 1; $i < $totalHadiths; $i++) {
        $h = $hadiths[$i];
        $words = !empty($h['words_json']) ? json_decode($h['words_json'], true) : [];
        $items[] = [
            'type' => 2,
            'id' => (int)$h['id'],
            'book_slug' => $h['book_slug'],
            'book_name_bn' => $h['book_name_bn'] ?? 'সহীহ বুখারী',
            'book_name_en' => $h['book_name_en'] ?? 'Sahih al-Bukhari',
            'hadith_number' => (int)$h['hadith_number'],
            'hadith_number_bn' => toBnNum($h['hadith_number']),
            'grade_bn' => 'সহিহ হাদিস',
            'grade_en' => 'Sahih Hadith',
            'arabic_text' => $h['arabic_text'],
            'narrator_bn' => $h['narrator_bn'],
            'narrator_en' => $h['narrator_en'] ?? '',
            'bangla_text' => $h['bangla_text'],
            'english_text' => $h['english_text'] ?? '',
            'footnote_bn' => $h['footnote_bn'] ?? '',
            'footnote_en' => $h['footnote_en'] ?? '',
            'words' => $words
        ];
    }

    // If no hadiths found in MySQL, load seamlessly from hadithbd.db SQLite database
    if ($totalHadiths == 0) {
        require_once __DIR__ . '/hadithbd_helper.php';
        $hDb = getHadithDbPdo();
        $bId = getHadithDbBookId($bookSlug);
        if ($hDb && $bId !== null) {
            // Find sections for this book
            $sStmt = $hDb->prepare("SELECT SectionID, SectionBD, SectionEN FROM hadithsection WHERE BookID = ? ORDER BY SectionID ASC");
            $sStmt->execute([$bId]);
            $sectionsList = $sStmt->fetchAll();

            $targetSecIndex = $chapterNumber - 1;
            if ($targetSecIndex >= 0 && $targetSecIndex < count($sectionsList)) {
                $targetSec = $sectionsList[$targetSecIndex];
                $secId = (int)$targetSec['SectionID'];
                $rawTitle = $targetSec['SectionBD'];
                $cleanTitle = trim(preg_replace('/^[০-৯0-9\/\.\s\-]+/u', '', $rawTitle));
                if (empty($cleanTitle)) $cleanTitle = $rawTitle;

                // Add section header
                $items[] = [
                    'type' => 1,
                    'section_tag' => toBnNum($chapterNumber) . '. অধ্যায়ঃ',
                    'section_title' => $cleanTitle,
                    'section_arabic_verse' => '',
                    'section_translation' => ''
                ];

                // Fetch hadiths from hadithmain
                $hStmt = $hDb->prepare("
                    SELECT HadithID, HadithNo, ArabicHadith, BanglaHadith, EnglishHadith, HadithNote, HadithStatus
                    FROM hadithmain
                    WHERE BookID = ? AND SectionID = ?
                    ORDER BY HadithNo ASC
                ");
                $hStmt->execute([$bId, $secId]);
                $hRows = $hStmt->fetchAll();

                $bInfoStmt = $pdo->prepare("SELECT name_bn, name_en FROM hadith_books WHERE book_slug = ? LIMIT 1");
                $bInfoStmt->execute([$bookSlug]);
                $bInfo = $bInfoStmt->fetch();
                $bNameBn = $bInfo['name_bn'] ?? 'সহীহ হাদিস';
                $bNameEn = $bInfo['name_en'] ?? 'Sahih Hadith';

                foreach ($hRows as $hr) {
                    list($narrator, $body) = extractNarratorAndText($hr['BanglaHadith'] ?? '');
                    $grade = getHadithGradeFromStatus($hr['HadithStatus'] ?? 1);
                    $hNum = (int)($hr['HadithNo'] ?? 1);

                    $items[] = [
                        'type' => 2,
                        'id' => (int)$hr['HadithID'],
                        'book_slug' => $bookSlug,
                        'book_name_bn' => $bNameBn,
                        'book_name_en' => $bNameEn,
                        'hadith_number' => $hNum,
                        'hadith_number_bn' => toBnNum($hNum),
                        'grade_bn' => $grade['grade_bn'],
                        'grade_en' => $grade['grade_en'],
                        'arabic_text' => trim((string)($hr['ArabicHadith'] ?? '')),
                        'narrator_bn' => $narrator,
                        'narrator_en' => '',
                        'bangla_text' => $body,
                        'english_text' => cleanHadithText($hr['EnglishHadith'] ?? ''),
                        'footnote_bn' => cleanHadithText($hr['HadithNote'] ?? ''),
                        'footnote_en' => '',
                        'words' => []
                    ];
                }
            }
        }
    }

    sendJsonResponse([
        'status' => 'success',
        'book_slug' => $bookSlug,
        'chapter_number' => $chapterNumber,
        'count' => count($items),
        'items' => $items
    ]);

} catch (Exception $e) {
    sendJsonResponse([
        'status' => 'error',
        'message' => 'Failed to load chapter hadiths: ' . $e->getMessage()
    ], 500);
}
