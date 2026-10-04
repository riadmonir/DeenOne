<?php
/**
 * ==============================================================================
 * DEEN ONE REST API - GET HADITH CHAPTERS (সহীহ হাদিস অধ্যায়সমূহ)
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

try {
    $pdo = getDbConnection();

    // 1. Auto-create hadith_chapters table if not exists
    $pdo->exec("CREATE TABLE IF NOT EXISTS `hadith_chapters` (
        `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        `book_slug` VARCHAR(60) NOT NULL,
        `chapter_number` INT UNSIGNED NOT NULL,
        `title_bn` VARCHAR(255) NOT NULL,
        `title_en` VARCHAR(255) DEFAULT NULL,
        `title_ar` VARCHAR(255) DEFAULT NULL,
        `hadith_range_start` INT UNSIGNED DEFAULT 1,
        `hadith_range_end` INT UNSIGNED DEFAULT 1,
        `hadith_range_text` VARCHAR(100) DEFAULT NULL,
        `total_hadith` INT UNSIGNED DEFAULT 0,
        `display_order` INT UNSIGNED DEFAULT 0,
        `is_active` TINYINT(1) DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        UNIQUE KEY `uniq_book_chap` (`book_slug`, `chapter_number`),
        INDEX `idx_display_order` (`display_order`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

    // 2. Seed default chapters for Sahih Bukhari if table is empty or incomplete
    $count = (int)$pdo->query("SELECT COUNT(*) FROM hadith_chapters WHERE book_slug = 'bukhari'")->fetchColumn();
    if ($count < 41) {
        $seedChapters = [
            ['bukhari', 1, 'ওহীর সূচনা অধ্যায়', 'Revelation', 'بدء الوحي', 1, 7, '১ - ৭', 7, 1],
            ['bukhari', 2, 'ঈমান', 'Belief (Faith)', 'الإيمان', 8, 58, '৮ - ৫৮', 51, 2],
            ['bukhari', 3, 'ইলম', 'Knowledge', 'العلم', 59, 134, '৫৯ - ১৩৪', 76, 3],
            ['bukhari', 4, 'ওযু', 'Ablution (Wudu)', 'الوضوء', 135, 247, '১৩৫ - ২৪৭', 113, 4],
            ['bukhari', 5, 'গোসল', 'Bathing (Ghusl)', 'الغسل', 248, 293, '২৪৮ - ২৯৩', 46, 5],
            ['bukhari', 6, 'হায়েজ', 'Menses', 'الحيض', 294, 333, '২৯৪ - ৩৩৩', 40, 6],
            ['bukhari', 7, 'তায়াম্মুম', 'Tayammum', 'التيمم', 334, 348, '৩৩৪ - ৩৪৮', 15, 7],
            ['bukhari', 8, 'সালাত', 'Prayers (Salat)', 'الصلاة', 349, 520, '৩৪৯ - ৫২০', 172, 8],
            ['bukhari', 9, 'সালাতের ওয়াক্তসমূহ', 'Times of the Prayers', 'مواقيت الصلاة', 521, 602, '৫২১ - ৬০২', 82, 9],
            ['bukhari', 10, 'আজান', 'Call to Prayer', 'الأذান', 603, 875, '৬০৩ - ৮৭৫', 273, 10],
            ['bukhari', 11, 'জুমা', 'Friday Prayer', 'الجمعة', 876, 941, '৮৭৬ - ৯৪১', 66, 11],
            ['bukhari', 12, 'খাওফ (ভয় ভীতির সালাত)', 'Fear Prayer', 'صلاة الخوف', 942, 947, '৯৪২ - ৯৪৭', 6, 12],
            ['bukhari', 13, 'দুই ঈদ', 'The Two Festivals (Eids)', 'العيدين', 948, 989, '৯৪৮ - ৯৮৯', 42, 13],
            ['bukhari', 14, 'বিতর', 'Witr', 'الوتر', 990, 1004, '৯৯০ - ১০০৪', 15, 14],
            ['bukhari', 15, 'বৃষ্টির জন্য দোয়া', 'Invoking Allah for Rain (Istisqa)', 'الاستسقاء', 1005, 1039, '১০০৫ - ১০৩৯', 35, 15],
            ['bukhari', 16, 'সূর্যগ্রহণ', 'Eclipses', 'الكسوف', 1040, 1066, '১০৪০ - ১০৬৬', 27, 16],
            ['bukhari', 17, 'কুরআন তিলাওয়াতের সিজদা', 'Prostration During Quran Recital', 'سجود القرآن', 1067, 1079, '১০৬৭ - ১০৭৯', 13, 17],
            ['bukhari', 18, 'সালাতে কসর করা', 'Shortening the Prayers', 'تقصير الصلاة', 1080, 1119, '১০৮০ - ১১১৯', 40, 18],
            ['bukhari', 19, 'তাহাজ্জুদ', 'Prayer at Night (Tahajjud)', 'التهجد', 1120, 1187, '১১২০ - ১১৮৭', 68, 19],
            ['bukhari', 20, 'মক্কা ও মদীনার মসজিদে সালাতের মর্যাদা', 'Virtues of Prayer in Makkah & Madinah', 'فضل الصلاة في مسجد مكة والمدينة', 1188, 1197, '১১৮৮ - ১১৯৭', 10, 20],
            ['bukhari', 21, 'সালাতের সাথে সংশ্লিষ্ট কাজ', 'Actions while Praying', 'العمل في الصلاة', 1198, 1223, '১১৯৮ - ১২২৩', 26, 21],
            ['bukhari', 22, 'সাহু', 'Forgetfulness in Prayer (Sahu)', 'السهو', 1224, 1236, '১২২৪ - ১২৩৬', 13, 22],
            ['bukhari', 23, 'জানাজা', 'Funerals (Janaza)', 'الجنائز', 1237, 1394, '১২৩৭ - ১৩৯৪', 158, 23],
            ['bukhari', 24, 'যাকাত', 'Obligatory Charity Tax (Zakat)', 'الزكاة', 1395, 1512, '১৩৯৫ - ১৫১২', 118, 24],
            ['bukhari', 25, 'হজ্জ', 'Hajj (Pilgrimage)', 'الحج', 1513, 1772, '১৫১৩ - ১৭৭২', 260, 25],
            ['bukhari', 26, 'উমরাহ', 'Umrah (Minor Pilgrimage)', 'العمرة', 1773, 1805, '১৭৭৩ - ১৮০৫', 33, 26],
            ['bukhari', 27, 'পথে আটকে পড়া ও ইহরাম অবস্থায় শিকারকারীর বিধান', 'Muhsar & Hunting Penalty', 'المحصر وجزاء الصيد', 1806, 1820, '১৮০৬ - ১৮২০', 15, 27],
            ['bukhari', 28, 'ইহরাম অবস্থায় শিকার ও অনুরূপ কিছুর বদলা', 'Penalty of Hunting in Ihram', 'جزاء الصيد', 1821, 1866, '১৮২১ - ১৮৬৬', 46, 28],
            ['bukhari', 29, 'মদীনার ফজিলত', 'Virtues of Madinah', 'فضائل المدينة', 1867, 1890, '১৮৬৭ - ১৮৯০', 24, 29],
            ['bukhari', 30, 'সাওম', 'Fasting (Sawm)', 'الصوم', 1891, 2007, '১৮৯১ - ২০০৭', 117, 30],
            ['bukhari', 31, 'তারাবীহর সালাত', 'Tarawih Prayer', 'صلاة التراويح', 2008, 2013, '২০০৮ - ২০১৩', 6, 31],
            ['bukhari', 32, 'লাইলাতুল কদর এর ফজিলত', 'Virtues of Laylat al-Qadr', 'فضل ليلة القدر', 2014, 2024, '২০১৪ - ২০২৪', 11, 32],
            ['bukhari', 33, 'ইতিকাফ', 'Retiring to a Mosque (Itikaf)', 'الاعتكاف', 2025, 2046, '২০২৫ - ২০৪৬', 22, 33],
            ['bukhari', 34, 'ক্রয়-বিক্রয়', 'Sales and Trade', 'البيوع', 2047, 2238, '২০৪৭ - ২২৩৮', 192, 34],
            ['bukhari', 35, 'সলম (অগ্রিম ক্রয়-বিক্রয়)', 'Salam (Advance Purchase)', 'السلم', 2239, 2256, '২২৩৯ - ২২৫৬', 18, 35],
            ['bukhari', 36, 'শুফআ', 'Pre-emption (Shuf\'ah)', 'الشفعة', 2257, 2259, '২২৫৭ - ২২৫৯', 3, 36],
            ['bukhari', 37, 'ইজারা', 'Hiring and Leasing (Ijarah)', 'الإجارة', 2260, 2286, '২২৬০ - ২২৮৬', 27, 37],
            ['bukhari', 38, 'হাওয়ালাত', 'Transfer of Debt (Hawalah)', 'الحوالة', 2287, 2289, '২২৮৭ - ২২৮৯', 3, 38],
            ['bukhari', 39, 'যামিন হওয়া', 'Suretyship (Kafalah)', 'الكفالة', 2290, 2298, '২২৯০ - ২২৯৮', 9, 39],
            ['bukhari', 40, 'ওয়াকালাহ (প্রতিনিধিত্ব)', 'Representation (Wakalah)', 'الوكالة', 2299, 2319, '২২৯৯ - ২৩১৯', 21, 40],
            ['bukhari', 41, 'চাষাবাদ', 'Agriculture (Muzara\'ah)', 'المزارعة', 2320, 2350, '২৩২০ - ২৩৫০', 31, 41]
        ];

        $insStmt = $pdo->prepare("INSERT INTO hadith_chapters (book_slug, chapter_number, title_bn, title_en, title_ar, hadith_range_start, hadith_range_end, hadith_range_text, total_hadith, display_order, is_active) 
                                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
                                 ON DUPLICATE KEY UPDATE 
                                    title_bn = VALUES(title_bn),
                                    title_en = VALUES(title_en),
                                    title_ar = VALUES(title_ar),
                                    hadith_range_start = VALUES(hadith_range_start),
                                    hadith_range_end = VALUES(hadith_range_end),
                                    hadith_range_text = VALUES(hadith_range_text),
                                    total_hadith = VALUES(total_hadith),
                                    display_order = VALUES(display_order)");
        foreach ($seedChapters as $sc) {
            $insStmt->execute($sc);
        }
    }

    $bookSlug = trim($_GET['book_slug'] ?? 'bukhari');

    // Get Book Details
    $bookStmt = $pdo->prepare("SELECT * FROM hadith_books WHERE book_slug = ? LIMIT 1");
    $bookStmt->execute([$bookSlug]);
    $book = $bookStmt->fetch();

    $bookNameBn = $book ? $book['name_bn'] : 'সহীহ বুখারী';
    $bookNameEn = $book && !empty($book['name_en']) ? $book['name_en'] : 'Sahih Bukhari';
    $totalBookHadith = $book && (int)$book['total_hadith'] > 0 ? (int)$book['total_hadith'] : 7563;

    // Fetch Chapters
    $stmt = $pdo->prepare("SELECT * FROM hadith_chapters 
                           WHERE book_slug = ? AND is_active = 1 
                           ORDER BY display_order ASC, chapter_number ASC");
    $stmt->execute([$bookSlug]);
    $rows = $stmt->fetchAll();

    require_once __DIR__ . '/hadithbd_helper.php';

    function toBengaliNum($number) {
        return toBengaliDigit($number);
    }

    $chapters = [];
    if (!empty($rows)) {
        foreach ($rows as $r) {
            $start = (int)($r['hadith_range_start'] ?? $r['start_hadith'] ?? 1);
            $end = (int)($r['hadith_range_end'] ?? $r['end_hadith'] ?? 1);
            $rangeBn = !empty($r['hadith_range_text']) ? $r['hadith_range_text'] : (!empty($r['hadith_range_bn']) ? $r['hadith_range_bn'] : '');
            if (empty($rangeBn) || preg_match('/^[0-9\s\-]+$/', $rangeBn)) {
                $rangeBn = toBengaliNum($start) . ' - ' . toBengaliNum($end);
            }

            $chapters[] = [
                'id' => (int)$r['id'],
                'book_slug' => $r['book_slug'],
                'chapter_number' => (int)$r['chapter_number'],
                'chapter_number_bn' => toBengaliNum($r['chapter_number']),
                'title_bn' => $r['title_bn'],
                'title_en' => $r['title_en'],
                'title_ar' => $r['title_ar'],
                'hadith_range_start' => $start,
                'hadith_range_end' => $end,
                'hadith_range_text' => $rangeBn,
                'hadith_range_text_en' => $start . ' - ' . $end,
                'total_hadith' => (int)$r['total_hadith'] > 0 ? (int)$r['total_hadith'] : max(1, ($end - $start + 1)),
                'display_order' => (int)$r['display_order']
            ];
        }
    } else {
        // Fallback to hadithbd.db SQLite database for complete authentic chapters
        $hDb = getHadithDbPdo();
        $bId = getHadithDbBookId($bookSlug);
        if ($hDb && $bId !== null) {
            $hStmt = $hDb->prepare("
                SELECT s.SectionID, s.SectionBD, s.SectionEN,
                       COALESCE(MIN(m.HadithNo), 0) as start_no,
                       COALESCE(MAX(m.HadithNo), 0) as end_no,
                       COUNT(m.HadithID) as total_cnt
                FROM hadithsection s
                LEFT JOIN hadithmain m ON s.SectionID = m.SectionID AND m.BookID = s.BookID
                WHERE s.BookID = ?
                GROUP BY s.SectionID
                ORDER BY s.SectionID ASC
            ");
            $hStmt->execute([$bId]);
            $hRows = $hStmt->fetchAll();
            $chapIdx = 1;
            foreach ($hRows as $hr) {
                $rawTitle = $hr['SectionBD'];
                $cleanTitle = preg_replace('/^[০-৯0-9\/\.\s\-]+/u', '', $rawTitle);
                $cleanTitle = trim($cleanTitle);
                if (empty($cleanTitle)) $cleanTitle = $rawTitle;

                $start = (int)$hr['start_no'];
                $end = (int)$hr['end_no'];
                $cnt = (int)$hr['total_cnt'];
                $rangeBn = toBengaliNum($start) . ' - ' . toBengaliNum($end);

                $chapters[] = [
                    'id' => (int)$hr['SectionID'],
                    'book_slug' => $bookSlug,
                    'chapter_number' => $chapIdx,
                    'chapter_number_bn' => toBengaliNum($chapIdx),
                    'title_bn' => $cleanTitle,
                    'title_en' => !empty($hr['SectionEN']) ? trim($hr['SectionEN']) : $cleanTitle,
                    'title_ar' => '',
                    'hadith_range_start' => $start,
                    'hadith_range_end' => $end,
                    'hadith_range_text' => $rangeBn,
                    'hadith_range_text_en' => $start . ' - ' . $end,
                    'total_hadith' => $cnt > 0 ? $cnt : max(1, ($end - $start + 1)),
                    'display_order' => $chapIdx
                ];
                $chapIdx++;
            }
        }
    }

    echo json_encode([
        'success' => true,
        'status' => 'success',
        'book_slug' => $bookSlug,
        'book_name_bn' => $bookNameBn,
        'book_name_en' => $bookNameEn,
        'total_book_hadith' => $totalBookHadith,
        'total_book_hadith_bn' => toBengaliNum($totalBookHadith),
        'total_chapters' => count($chapters),
        'chapters' => $chapters,
        'data' => $chapters
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to fetch hadith chapters: ' . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}
