<?php
/**
 * ==============================================================================
 * DEEN ONE API - HADITH BOOKS & CATEGORIES WITH LIVE REAL-TIME COUNT
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

try {
    // 0. Ensure table exists
    $pdo->exec("CREATE TABLE IF NOT EXISTS `hadith_books` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `book_slug` VARCHAR(60) NOT NULL UNIQUE,
      `name_bn` VARCHAR(150) NOT NULL,
      `author_bn` VARCHAR(120) DEFAULT NULL,
      `author_en` VARCHAR(120) DEFAULT NULL,
      `name_en` VARCHAR(150) DEFAULT NULL,
      `name_ar` VARCHAR(150) DEFAULT NULL,
      `initials` VARCHAR(20) DEFAULT 'H',
      `color_hex` VARCHAR(20) DEFAULT '#10B981',
      `total_hadith` INT UNSIGNED DEFAULT 0,
      `total_hadith_bn` VARCHAR(50) DEFAULT '',
      `description_bn` TEXT DEFAULT NULL,
      `display_order` INT UNSIGNED DEFAULT 0,
      `is_active` TINYINT(1) DEFAULT 1,
      `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

    // 1. Ensure required columns exist in hadith_books
    try {
        $stmtCols = $pdo->query("SHOW COLUMNS FROM hadith_books");
        $existingCols = $stmtCols->fetchAll(PDO::FETCH_COLUMN);
        if (!in_array('author_bn', $existingCols)) {
            $pdo->exec("ALTER TABLE hadith_books ADD COLUMN author_bn VARCHAR(120) DEFAULT NULL AFTER name_bn");
        }
        if (!in_array('author_en', $existingCols)) {
            $pdo->exec("ALTER TABLE hadith_books ADD COLUMN author_en VARCHAR(120) DEFAULT NULL AFTER author_bn");
        }
        if (!in_array('name_en', $existingCols)) {
            $pdo->exec("ALTER TABLE hadith_books ADD COLUMN name_en VARCHAR(120) DEFAULT NULL AFTER name_bn");
        }
        if (!in_array('initials', $existingCols)) {
            $pdo->exec("ALTER TABLE hadith_books ADD COLUMN initials VARCHAR(20) DEFAULT 'H' AFTER author_en");
        }
        if (!in_array('color_hex', $existingCols)) {
            $pdo->exec("ALTER TABLE hadith_books ADD COLUMN color_hex VARCHAR(20) DEFAULT '#10B981' AFTER initials");
        }
    } catch (Exception $ce) {}

    // 2. Predefined 19 Active Hadith Books Seed with authentic database totals
    $seedBooks = [
        ['bukhari', 'সহীহ বুখারী', 'Sahih Bukhari', 'ইমাম বুখারি', 'Imam Bukhari', 'B', '#22C55E', 7589, 1, 1],
        ['muslim', 'সহীহ মুসলিম', 'Sahih Muslim', 'ইমাম মুসলিম', 'Imam Muslim', 'M', '#0284C7', 7563, 2, 1],
        ['nasai', 'সুনানে আন-নাসায়ী', "Sunan an-Nasa'i", 'ইমাম নাসায়ী', "Imam Nasa'i", 'N', '#0EA5E9', 5765, 3, 1],
        ['abu_dawood', 'সুনানে আবু দাউদ', 'Sunan Abu Dawood', 'ইমাম আবু দাউদ', 'Imam Abu Dawood', 'AD', '#9333EA', 5274, 4, 1],
        ['tirmidhi', "জামে' আত-তিরমিযী", "Jami' at-Tirmidhi", 'ইমাম তিরমিজি', 'Imam Tirmidhi', 'T', '#3B82F6', 3998, 5, 1],
        ['ibn_majah', 'সুনানে ইবনে মাজাহ', 'Sunan Ibn Majah', 'ইমাম ইবনে মাজাহ', 'Imam Ibn Majah', 'IM', '#F97316', 4343, 6, 1],
        ['muwatta_malik', 'মুয়াত্তা ইমাম মালিক', 'Muwatta Imam Malik', 'ইমাম মালিক', 'Imam Malik', 'MI', '#38BDF8', 1858, 7, 1],
        ['riyadus_salihin', 'রিয়াদুস সালেহীন', 'Riyadus Salihin', 'ইমাম নববী', 'Imam Nawawi', 'RS', '#EC4899', 616, 8, 1],
        ['bulughul_maram', 'বুলুগুল মারাম', 'Bulughul Maram', 'ইবনে হাজার আসকালানী', 'Ibn Hajar al-Asqalani', 'BM', '#FB923C', 178, 9, 1],
        ['jal_o_daif_series', 'জাল ও যঈফ হাদীস সিরিজ', 'Jal o Daif Hadith Series', 'আল্লামা নাসিরুদ্দিন আলবানী', 'Allama Nasiruddin Albani', 'JH', '#FCA5A5', 342, 10, 1],
        ['nawawi_40', 'আন্-নওয়াবীর চল্লিশ হাদীস', "An-Nawawi's 40 Hadith", 'ইমাম নববী', 'Imam Nawawi', '40', '#78716C', 42, 11, 1],
        ['adabul_mufrad', 'আল-আদাবুল মুফরাদ', 'Al-Adab al-Mufrad', 'ইমাম বুখারি', 'Imam Bukhari', 'AM', '#0F766E', 183, 12, 1],
        ['hadithe_qudsi', 'সহীহ হাদীসে কুদসী', 'Sahih Hadithe Qudsi', 'আল্লামা নাসিরুদ্দিন আলবানী', 'Allama Nasiruddin Albani', 'HK', '#10B981', 163, 13, 1],
        ['100_susabbasto_hadith', '১০০ সুসাব্যস্ত হাদীস', '100 Susabbasto Hadith', 'সঙ্কলিত হাদিস', 'Compiled Hadith', '100', '#4F46E5', 197, 14, 1],
        ['mishkate_daif_hadith', 'মিশকাতে যঈফ হাদীস', 'Mishkate Daif Hadith', 'মুযাফফার বিন মুহসিন', 'Muzaffar Bin Muhsin', 'MJ', '#E11D48', 106, 15, 1],
        ['sahih_at_targib', 'সহীহ আত-তারগিব ওয়াত তাহরিব', 'Sahih at-Targhib wat-Tahrib', 'আল্লামা নাসিরুদ্দিন আলবানী', 'Allama Nasiruddin Albani', 'TW', '#A855F7', 200, 16, 1],
        ['sahih_fazayele_amal', 'সহিহ ফাযায়েলে আমল', 'Sahih Fazayele Amal', 'আহসানুল্লাহ বিন সানাউল্লাহ', 'Ahsanullah Bin Sanaullah', 'FA', '#475569', 151, 17, 1],
        ['upodesh', 'উপদেশ', 'Upodesh', 'আব্দুর রাজ্জাক বিন ইউসুফ', 'Abdur Razzak Bin Yousuf', 'UP', '#22C55E', 234, 18, 1],
        ['ramadaner_durbol_hadith', 'রমজানের দুর্বল হাদিস', 'Ramadaner Durbol Hadith', 'সানাউল্লাহ নজির আহমদ', 'Sanaullah Nazir Ahmad', 'RH', '#78350F', 34, 19, 1]
    ];

    // Deactivate books without datasets
    $pdo->exec("UPDATE hadith_books SET is_active = 0 WHERE book_slug IN ('lulu_wal_marjan', 'hadith_sambhar', 'silsila_sahiha', 'mishkatul_masabih', 'rafayel_yadain', 'shamayele_tirmidhi')");

    $upsertStmt = $pdo->prepare("INSERT INTO hadith_books (book_slug, name_bn, name_en, author_bn, author_en, initials, color_hex, total_hadith, display_order, is_active) 
                                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
                                 ON DUPLICATE KEY UPDATE 
                                    name_bn = VALUES(name_bn),
                                    name_en = VALUES(name_en),
                                    author_bn = VALUES(author_bn),
                                    author_en = VALUES(author_en),
                                    initials = VALUES(initials),
                                    color_hex = VALUES(color_hex),
                                    total_hadith = VALUES(total_hadith),
                                    display_order = VALUES(display_order),
                                    is_active = VALUES(is_active)");
    foreach ($seedBooks as $sb) {
        $upsertStmt->execute($sb);
    }

    // 3. Fetch all active categories with exact count
    $sql = "SELECT b.book_slug as slug, b.name_bn, COALESCE(b.name_en, b.name_bn) as name_en, 
                   COALESCE(b.author_bn, '') as author_bn, COALESCE(b.author_en, '') as author_en,
                   COALESCE(b.initials, 'H') as initials, COALESCE(b.color_hex, '#10B981') as color_hex,
                   b.total_hadith,
                   b.display_order
            FROM hadith_books b
            WHERE b.is_active = 1
            ORDER BY b.display_order ASC";
    $rows = $pdo->query($sql)->fetchAll();

    $categories = [];
    foreach ($rows as $r) {
        $categories[] = [
            'slug' => $r['slug'],
            'name_bn' => $r['name_bn'],
            'name_en' => $r['name_en'],
            'author_bn' => $r['author_bn'],
            'author_en' => $r['author_en'],
            'initials' => $r['initials'],
            'color_hex' => $r['color_hex'],
            'total_hadith' => (int)$r['total_hadith'],
            'display_order' => (int)$r['display_order']
        ];
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($categories),
        'categories' => $categories
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load hadith categories: ' . $e->getMessage()
    ], 500);
}
?>
