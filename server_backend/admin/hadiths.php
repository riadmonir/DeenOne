<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - SAHIH HADITH COLLECTIONS (সহীহ হাদীস ও অধ্যায় ভূমিকা কালেকশন)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure hadith_sections table and hadith_items columns exist
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

try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `hadith_number_bn` VARCHAR(50) DEFAULT '' AFTER `hadith_number`");
} catch (Exception $e) {}
try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `english_text` TEXT DEFAULT NULL AFTER `bangla_text`");
} catch (Exception $e) {}
try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `narrator_en` VARCHAR(255) DEFAULT NULL AFTER `narrator_bn`");
} catch (Exception $e) {}
try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `footnote_bn` TEXT DEFAULT NULL AFTER `reference`");
} catch (Exception $e) {}
try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `footnote_en` TEXT DEFAULT NULL AFTER `footnote_bn`");
} catch (Exception $e) {}
try {
    $pdo->exec("ALTER TABLE `hadith_items` ADD COLUMN `words_json` LONGTEXT DEFAULT NULL AFTER `footnote_en`");
} catch (Exception $e) {}

// -----------------------------------------------------------------------------
// ACTION HANDLERS (Create, Update, Delete)
// -----------------------------------------------------------------------------

// Handle Action (Add, Edit, Delete Hadiths & Sections)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর।');
        redirect('hadiths.php');
    }

    // 1. Hadith Item Actions
    if ($action === 'create' || $action === 'update') {
        $bookSlug = trim($_POST['book_slug'] ?? 'bukhari');
        $chapNum = max(1, (int)($_POST['chapter_number'] ?? 1));
        $hadithNum = max(1, (int)($_POST['hadith_number'] ?? 1));
        $narratorBn = trim($_POST['narrator_bn'] ?? '');
        $narratorEn = trim($_POST['narrator_en'] ?? '');
        $arabicText = trim($_POST['arabic_text'] ?? '');
        $banglaText = trim($_POST['bangla_text'] ?? '');
        $englishText = trim($_POST['english_text'] ?? '');
        $footnoteBn = trim($_POST['footnote_bn'] ?? '');
        $footnoteEn = trim($_POST['footnote_en'] ?? '');
        $grade = trim($_POST['grade'] ?? (isEn() ? 'Sahih' : 'সহীহ'));
        $ref = trim($_POST['reference'] ?? '');

        if (empty($banglaText) || empty($arabicText)) {
            setFlash('danger', 'হাদীসের আরবি পাঠ এবং বাংলা অনুবাদ উভয়ই প্রদান করতে হবে।');
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO hadith_items 
                        (book_slug, chapter_number, hadith_number, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, footnote_bn, footnote_en, grade, reference) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$bookSlug, $chapNum, $hadithNum, $narratorBn, $narratorEn, $arabicText, $banglaText, $englishText, $footnoteBn, $footnoteEn, $grade, $ref]);
                    logAdminAction($pdo, 'CREATE_HADITH', 'hadith_items', (string)$pdo->lastInsertId(), "$bookSlug হাদীস নং: $hadithNum");
                    setFlash('success', "নতুন হাদীস (#$hadithNum) সফলভাবে যোগ করা হয়েছে!");
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE hadith_items 
                        SET book_slug = ?, chapter_number = ?, hadith_number = ?, narrator_bn = ?, narrator_en = ?, arabic_text = ?, bangla_text = ?, english_text = ?, footnote_bn = ?, footnote_en = ?, grade = ?, reference = ? 
                        WHERE id = ?");
                    $stmt->execute([$bookSlug, $chapNum, $hadithNum, $narratorBn, $narratorEn, $arabicText, $banglaText, $englishText, $footnoteBn, $footnoteEn, $grade, $ref, $id]);
                    logAdminAction($pdo, 'UPDATE_HADITH', 'hadith_items', (string)$id, "$bookSlug হাদীস আপডেট নং: $hadithNum");
                    setFlash('success', "হাদীস (#$hadithNum) তথ্য সফলভাবে আপডেট করা হয়েছে!");
                }
            } catch (Exception $e) {
                setFlash('danger', 'ডাটাবেস ত্রুটি: ' . $e->getMessage());
            }
        }
    }

    // 2. Section Header Actions (Chapter Description & Quran Intro)
    if ($action === 'create_section' || $action === 'update_section') {
        $bookSlug = trim($_POST['sec_book_slug'] ?? 'bukhari');
        $chapNum = max(1, (int)($_POST['sec_chapter_number'] ?? 1));
        $secNumber = trim($_POST['sec_number'] ?? '১/১');
        $secTagBn = trim($_POST['sec_tag_bn'] ?? '');
        $secTagEn = trim($_POST['sec_tag_en'] ?? '');
        $secTitleBn = trim($_POST['sec_title_bn'] ?? '');
        $secTitleEn = trim($_POST['sec_title_en'] ?? '');
        $arVerse = trim($_POST['sec_arabic_verse'] ?? '');
        $vTransBn = trim($_POST['sec_verse_translation_bn'] ?? '');
        $vTransEn = trim($_POST['sec_verse_translation_en'] ?? '');
        $displayOrder = max(1, (int)($_POST['sec_display_order'] ?? 1));

        if (empty($secTagBn)) {
            setFlash('danger', 'সেকশন ট্যাগ (যেমন: ১/১. অধ্যায়ঃ) প্রদান করতে হবে।');
        } else {
            try {
                if ($action === 'create_section') {
                    $stmt = $pdo->prepare("INSERT INTO hadith_sections 
                        (book_slug, chapter_number, section_number, section_tag_bn, section_tag_en, section_title_bn, section_title_en, arabic_verse, verse_translation_bn, verse_translation_en, display_order, is_active)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
                    $stmt->execute([$bookSlug, $chapNum, $secNumber, $secTagBn, $secTagEn, $secTitleBn, $secTitleEn, $arVerse, $vTransBn, $vTransEn, $displayOrder]);
                    logAdminAction($pdo, 'CREATE_HADITH_SECTION', 'hadith_sections', (string)$pdo->lastInsertId(), "$bookSlug সেকশন: $secNumber");
                    setFlash('success', 'অধ্যায় পরিচিতি / সেকশন ভূমিকা সফলভাবে যুক্ত হয়েছে!');
                } else {
                    $secId = (int)($_POST['sec_id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE hadith_sections 
                        SET book_slug = ?, chapter_number = ?, section_number = ?, section_tag_bn = ?, section_tag_en = ?, section_title_bn = ?, section_title_en = ?, arabic_verse = ?, verse_translation_bn = ?, verse_translation_en = ?, display_order = ?
                        WHERE id = ?");
                    $stmt->execute([$bookSlug, $chapNum, $secNumber, $secTagBn, $secTagEn, $secTitleBn, $secTitleEn, $arVerse, $vTransBn, $vTransEn, $displayOrder, $secId]);
                    logAdminAction($pdo, 'UPDATE_HADITH_SECTION', 'hadith_sections', (string)$secId, "$bookSlug সেকশন আপডেট: $secNumber");
                    setFlash('success', 'অধ্যায় পরিচিতি তথ্য সফলভাবে আপডেট করা হয়েছে!');
                }
            } catch (Exception $e) {
                setFlash('danger', 'ডাটাবেস ত্রুটি: ' . $e->getMessage());
            }
        }
    }

    $retParams = [];
    if (!empty($_POST['book_slug'])) {
        $retParams[] = 'book=' . urlencode($_POST['book_slug']);
    } elseif (!empty($_POST['sec_book_slug'])) {
        $retParams[] = 'book=' . urlencode($_POST['sec_book_slug']);
    }
    if (!empty($_POST['chapter_number'])) {
        $retParams[] = 'chapter=' . (int)$_POST['chapter_number'];
    } elseif (!empty($_POST['sec_chapter_number'])) {
        $retParams[] = 'chapter=' . (int)$_POST['sec_chapter_number'];
    }
    $retQuery = !empty($retParams) ? '?' . implode('&', $retParams) : '';
    redirect('hadiths.php' . $retQuery);
}

// Delete Hadith
if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM hadith_items WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_HADITH', 'hadith_items', (string)$id, 'হাদীস মুছে ফেলা হয়েছে');
        setFlash('success', 'হাদীস সফলভাবে মুছে ফেলা হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'মুছতে ব্যর্থ: ' . $e->getMessage());
    }
    $retParams = [];
    if (!empty($_GET['book'])) $retParams[] = 'book=' . urlencode($_GET['book']);
    if (!empty($_GET['chapter'])) $retParams[] = 'chapter=' . (int)$_GET['chapter'];
    $retQuery = !empty($retParams) ? '?' . implode('&', $retParams) : '';
    redirect('hadiths.php' . $retQuery);
}

// Delete Section
if (isset($_GET['delete_section'])) {
    $id = (int)$_GET['delete_section'];
    try {
        $stmt = $pdo->prepare("DELETE FROM hadith_sections WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_HADITH_SECTION', 'hadith_sections', (string)$id, 'সেকশন মুছে ফেলা হয়েছে');
        setFlash('success', 'অধ্যায় সেকশন সফলভাবে মুছে ফেলা হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'মুছতে ব্যর্থ: ' . $e->getMessage());
    }
    $retParams = [];
    if (!empty($_GET['book'])) $retParams[] = 'book=' . urlencode($_GET['book']);
    if (!empty($_GET['chapter'])) $retParams[] = 'chapter=' . (int)$_GET['chapter'];
    $retQuery = !empty($retParams) ? '?' . implode('&', $retParams) : '';
    redirect('hadiths.php' . $retQuery);
}

// Fetch Books
$books = [];
try {
    $books = $pdo->query("SELECT * FROM hadith_books ORDER BY display_order ASC")->fetchAll();
} catch (Exception $e) {}

// Calculate next hadith numbers for each book & chapter automatically (Max + 1)
$nextHadithMap = [];
$nextHadithByChapMap = [];
try {
    $maxStmt = $pdo->query("SELECT book_slug, COALESCE(MAX(hadith_number), 0) as max_num FROM hadith_items GROUP BY book_slug");
    while ($row = $maxStmt->fetch()) {
        $nextHadithMap[$row['book_slug']] = ((int)$row['max_num']) + 1;
    }
    $maxChapStmt = $pdo->query("SELECT book_slug, chapter_number, COALESCE(MAX(hadith_number), 0) as max_num FROM hadith_items GROUP BY book_slug, chapter_number");
    while ($row = $maxChapStmt->fetch()) {
        $key = $row['book_slug'] . '_' . $row['chapter_number'];
        $nextHadithByChapMap[$key] = ((int)$row['max_num']) + 1;
    }
} catch (Exception $e) {}

// Filtering & Pagination
$selectedBook = $_GET['book'] ?? 'bukhari';
$selectedChapter = !empty($_GET['chapter']) ? (int)$_GET['chapter'] : 0;
$search = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$perPage = 20;
$offset = ($page - 1) * $perPage;

// Fetch Chapters for Selected Book
$chaptersList = [];
if (!empty($selectedBook)) {
    try {
        $cStmt = $pdo->prepare("SELECT chapter_number, title_bn, hadith_range, hadith_range_bn FROM hadith_chapters WHERE book_slug = ? ORDER BY chapter_number ASC");
        $cStmt->execute([$selectedBook]);
        $chaptersList = $cStmt->fetchAll();
    } catch (Exception $e) {}
}

// Resolve Current Book Name & Chapter Title
$currentBookName = 'সহীহ বুখারী';
foreach ($books as $b) {
    if ($b['book_slug'] === $selectedBook) {
        $currentBookName = $b['name_bn'];
        break;
    }
}
$currentChapterTitle = '';
if ($selectedChapter > 0) {
    foreach ($chaptersList as $chap) {
        if ((int)$chap['chapter_number'] === $selectedChapter) {
            $currentChapterTitle = $chap['title_bn'];
            break;
        }
    }
}

// Fetch Sections for Selected Book / Chapter
$sections = [];
try {
    $secWhere = ["book_slug = ?"];
    $secParams = [$selectedBook];
    if ($selectedChapter > 0) {
        $secWhere[] = "chapter_number = ?";
        $secParams[] = $selectedChapter;
    }
    $secSql = "SELECT * FROM hadith_sections WHERE " . implode(" AND ", $secWhere) . " ORDER BY chapter_number ASC, display_order ASC";
    $sStmt = $pdo->prepare($secSql);
    $sStmt->execute($secParams);
    $sections = $sStmt->fetchAll();
} catch (Exception $e) {}

$where = [];
$params = [];
if (!empty($selectedBook)) {
    $where[] = "h.book_slug = ?";
    $params[] = $selectedBook;
}
if ($selectedChapter > 0) {
    $where[] = "h.chapter_number = ?";
    $params[] = $selectedChapter;
}
if (!empty($search)) {
    $where[] = "(h.bangla_text LIKE ? OR h.narrator_bn LIKE ? OR h.reference LIKE ? OR h.arabic_text LIKE ? OR h.hadith_number = ? OR h.hadith_number_bn LIKE ?)";
    $term = "%$search%";
    $params[] = $term;
    $params[] = $term;
    $params[] = $term;
    $params[] = $term;
    $params[] = is_numeric($search) ? (int)$search : 0;
    $params[] = $term;
}
$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

$totalHadiths = 0;
$hadiths = [];
try {
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM hadith_items h $whereSql");
    $countStmt->execute($params);
    $totalHadiths = (int)$countStmt->fetchColumn();

    $sql = "SELECT h.*, b.name_bn as book_name, c.title_bn as chapter_title 
            FROM hadith_items h 
            LEFT JOIN hadith_books b ON h.book_slug = b.book_slug 
            LEFT JOIN hadith_chapters c ON (h.book_slug = c.book_slug AND h.chapter_number = c.chapter_number) 
            $whereSql 
            ORDER BY h.hadith_number ASC 
            LIMIT $perPage OFFSET $offset";
    $hStmt = $pdo->prepare($sql);
    $hStmt->execute($params);
    $hadiths = $hStmt->fetchAll();
} catch (Exception $e) {}

$totalPages = max(1, ceil($totalHadiths / $perPage));

$pageTitle = __('হাদিস তালিকা', 'Hadiths');
$activeNav = 'hadiths';
require_once __DIR__ . '/header.php';
?>

<!-- Breadcrumb Navigation Bar (Level 3: Hadith Items) -->
<div class="flex items-center justify-between flex-wrap gap-3 mb-5">
  <div class="flex items-center gap-2 text-sm font-semibold text-slate-600 dark:text-slate-300">
    <a href="hadith_books.php" class="text-emerald-600 dark:text-emerald-400 hover:underline flex items-center gap-1.5">
      <i class="fa-solid fa-book-bookmark"></i>
      <span><?php echo __('গ্রন্থ তালিকা', 'Books'); ?></span>
    </a>
    <span class="text-slate-400"><i class="fa-solid fa-chevron-right text-xs"></i></span>
    <a href="hadith_chapters.php?book=<?php echo urlencode($selectedBook); ?>" class="text-emerald-600 dark:text-emerald-400 hover:underline">
      <span><?php echo htmlspecialchars($currentBookName) . ' ' . __('অধ্যায়সমূহ', 'Chapters'); ?></span>
    </a>
    <?php if ($selectedChapter > 0): ?>
    <span class="text-slate-400"><i class="fa-solid fa-chevron-right text-xs"></i></span>
    <span class="text-slate-800 dark:text-slate-100 font-bold">
      <?php echo __('অধ্যায়', 'Chapter') . ' ' . toLangNum($selectedChapter) . ($currentChapterTitle ? ': ' . htmlspecialchars($currentChapterTitle) : '') . ' ' . __('হাদিস তালিকা', 'Hadith List'); ?>
    </span>
    <?php endif; ?>
  </div>
  <div class="flex items-center gap-2">
    <a href="hadith_scraper.php?tab=pending&book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $selectedChapter; ?>" class="btn btn-secondary btn-sm flex items-center gap-1.5" style="border-color: rgba(245, 158, 11, 0.4); color: #d97706;" title="<?php echo __('স্মার্ট স্ক্র্যাপার ও পেন্ডিং কিউতে যান', 'Go to Smart Scraper & Pending Queue'); ?>">
      <i class="fa-solid fa-spider"></i>
      <span><?php echo __('স্ক্র্যাপার', 'Scraper'); ?></span>
    </a>
    <a href="hadith_chapters.php?book=<?php echo urlencode($selectedBook); ?>" class="btn btn-secondary btn-sm flex items-center gap-1.5">
      <i class="fa-solid fa-arrow-left"></i>
      <span><?php echo __('অধ্যায় তালিকা', 'Chapters'); ?></span>
    </a>
  </div>
</div>

<!-- Unified Active Chapter & Search Control Panel -->
<div class="card mb-5" style="border: 1px solid rgba(16, 185, 129, 0.28); box-shadow: 0 4px 14px rgba(16, 185, 129, 0.06);">
  <div class="card-body p-4">
    
    <!-- Top Row: Active Chapter Context & Primary Action Button -->
    <div class="flex items-center justify-between flex-wrap gap-3 pb-3.5 mb-3.5" style="border-bottom: 1px solid var(--border-color);">
      <div class="flex items-center gap-3">
        <?php if ($selectedChapter > 0): ?>
          <div class="w-10 h-10 rounded-xl bg-emerald-600 text-white flex items-center justify-center font-extrabold text-base shadow-sm">
            <?php echo toLangNum($selectedChapter); ?>
          </div>
          <div>
            <div class="text-[11px] text-emerald-700 dark:text-emerald-400 font-extrabold uppercase tracking-wider flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
              <span><?php echo __('বর্তমান সক্রিয় অধ্যায়', 'Active Chapter'); ?></span>
              <span class="text-slate-400">·</span>
              <span class="text-slate-600 dark:text-slate-300 font-bold"><?php echo toLangNum($totalHadiths) . ' ' . (isEn() ? 'Hadiths' : 'টি হাদিস'); ?></span>
            </div>
            <h3 class="text-base font-extrabold text-slate-900 dark:text-white mt-0.5">
              <?php echo htmlspecialchars($currentChapterTitle ?: (isEn() ? 'Chapter ' . toLangNum($selectedChapter) : 'অধ্যায় ' . toLangNum($selectedChapter))); ?>
            </h3>
          </div>
        <?php else: ?>
          <div class="w-10 h-10 rounded-xl bg-emerald-100 dark:bg-emerald-950 text-emerald-700 dark:text-emerald-300 flex items-center justify-center font-extrabold text-lg">
            <i class="fa-solid fa-book-bookmark"></i>
          </div>
          <div>
            <div class="text-[11px] text-emerald-700 dark:text-emerald-400 font-extrabold uppercase tracking-wider flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
              <span><?php echo htmlspecialchars($currentBookName); ?></span>
              <span class="text-slate-400">·</span>
              <span class="text-slate-600 dark:text-slate-300 font-bold"><?php echo toLangNum($totalHadiths) . ' ' . (isEn() ? 'Hadiths' : 'টি হাদিস'); ?></span>
            </div>
            <h3 class="text-base font-extrabold text-slate-900 dark:text-white mt-0.5">
              <?php echo __('সকল হাদিস', 'All Hadiths'); ?>
            </h3>
          </div>
        <?php endif; ?>
      </div>

      <!-- Action Buttons -->
      <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
        <button type="button" class="btn btn-primary" onclick="openAddHadithModal()" style="display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; font-size: 13.5px; font-weight: 700;">
          <i class="fa-solid fa-circle-plus"></i>
          <span><?php echo __('নতুন হাদিস', 'New Hadith'); ?></span>
        </button>
      </div>
    </div>

    <!-- Bottom Row: Unified Search & Filters Form -->
    <form method="GET" action="hadiths.php" style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
      <div style="flex: 1; min-width: 220px; position: relative;">
        <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); color: var(--text-muted); font-size: 13px;"></i>
        <input type="text" name="search" class="form-control" style="padding-left: 34px; font-size: 13px;" placeholder="<?php echo __('হাদীসের অর্থ, বর্ণনাকারী, আরবি বা রেফারেন্স দিয়ে খুঁজুন...', 'Search by text, narrator, Arabic or reference...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
      </div>
      <div style="width: 190px;">
        <select name="book" class="form-control font-semibold" style="font-size: 13px;" onchange="this.form.submit()">
          <?php foreach ($books as $b): ?>
            <option value="<?php echo htmlspecialchars($b['book_slug']); ?>" <?php echo $selectedBook === $b['book_slug'] ? 'selected' : ''; ?>>
              <?php echo htmlspecialchars(isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn']); ?>
            </option>
          <?php endforeach; ?>
        </select>
      </div>
      <?php if (!empty($chaptersList)): ?>
      <div style="width: 220px;">
        <select name="chapter" class="form-control" style="font-size: 13px;" onchange="this.form.submit()">
          <option value="0"><?php echo __('— সকল অধ্যায় —', '— All Chapters —'); ?></option>
          <?php foreach ($chaptersList as $chap): ?>
            <option value="<?php echo $chap['chapter_number']; ?>" <?php echo $selectedChapter === (int)$chap['chapter_number'] ? 'selected' : ''; ?>>
              <?php echo __('অধ্যায় ', 'Chapter ') . toLangNum($chap['chapter_number']); ?>: <?php echo htmlspecialchars($chap['title_bn']); ?>
            </option>
          <?php endforeach; ?>
        </select>
      </div>
      <?php endif; ?>
      <button type="submit" class="btn btn-primary" style="padding: 7px 14px; font-size: 13px;">
        <i class="fa-solid fa-magnifying-glass"></i>
        <span><?php echo __('খুঁজুন', 'Search'); ?></span>
      </button>
      <?php if (!empty($selectedBook) || $selectedChapter > 0 || !empty($search)): ?>
        <a href="hadiths.php?book=<?php echo urlencode($selectedBook); ?>" class="btn btn-secondary" style="padding: 7px 12px; font-size: 13px;" title="<?php echo __('ফিল্টার রিসেট করুন', 'Reset Filters'); ?>">
          <i class="fa-solid fa-rotate-left"></i>
          <span><?php echo __('রিসেট', 'Reset'); ?></span>
        </a>
      <?php endif; ?>
    </form>

  </div>
</div>

<!-- Sahih Hadiths Directory Table -->
<div class="card shadow-sm">
  <div class="card-header flex items-center justify-between flex-wrap gap-2">
    <div class="card-title flex items-center gap-2">
      <i class="fa-solid fa-book-open text-emerald-500"></i>
      <span><?php echo __('সহীহ হাদীস কালেকশন তালিকা', 'Sahih Hadith Collection List'); ?> (<?php echo __('মোট: ', 'Total: ') . toBnNum($totalHadiths) . ' ' . (isEn() ? '' : 'টি'); ?>)</span>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 75px;" class="text-center"><?php echo __('হাদীস নং', 'Hadith No'); ?></th>
          <th><?php echo __('গ্রন্থ ও বর্ণনাকারী', 'Book & Narrator'); ?></th>
          <th><?php echo __('আরবি মূল বাণী', 'Arabic Text'); ?></th>
          <th><?php echo __('বাংলা অনুবাদ ও ফুটনোট', 'Bengali & Footnote'); ?></th>
          <th style="width: 140px;"><?php echo __('মান ও রেফারেন্স', 'Grade & Reference'); ?></th>
          <th style="text-align: right; width: 110px;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($hadiths)): ?>
          <tr>
            <td colspan="6" class="text-center py-12 text-slate-400">
              <div class="text-4xl mb-3">📖</div>
              <div class="font-bold text-base text-slate-600 dark:text-slate-300"><?php echo __('কোনো হাদীস পাওয়া যায়নি', 'No Hadiths Found'); ?></div>
              <p class="text-sm mt-1"><?php echo __('নতুন হাদীস যুক্ত করতে উপরের বাটনে ক্লিক করুন।', 'Click the button above to add a new hadith.'); ?></p>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($hadiths as $h): ?>
            <tr>
              <td class="text-center">
                <span class="badge badge-info" style="font-size: 13.5px; font-weight: 800; padding: 4px 8px;">
                  #<?php echo toLangNum($h['hadith_number']); ?>
                </span>
              </td>
              <td>
                <div style="font-weight: 700; color: var(--primary); font-size: 13.5px;">
                  <?php echo htmlspecialchars($h['book_name'] ?? $h['book_slug']); ?>
                </div>
                <?php if (!empty($h['chapter_title'])): ?>
                  <div style="font-size: 11.5px; color: var(--text-dim); font-weight: 600; margin-top: 2px;">
                    <?php echo __('অধ্যায় ', 'Chapter ') . toLangNum($h['chapter_number']); ?>: <?php echo htmlspecialchars($h['chapter_title']); ?>
                  </div>
                <?php elseif (!empty($h['chapter_number'])): ?>
                  <div style="font-size: 11.5px; color: var(--text-dim); margin-top: 2px;">
                    <?php echo __('অধ্যায় নং ', 'Chapter No ') . toLangNum($h['chapter_number']); ?>
                  </div>
                <?php endif; ?>
                <div style="font-size: 12px; color: var(--text-muted); margin-top: 4px;">
                  <i class="fa-regular fa-user" style="font-size: 10px; margin-right: 4px;"></i>
                  <?php echo !empty($h['narrator_bn']) ? htmlspecialchars($h['narrator_bn']) : __('বর্ণনাকারী সাহাবী', 'Companion Narrator'); ?>
                </div>
              </td>
              <td style="max-width: 320px;">
                <div class="arabic-input" style="font-size: 16px; color: var(--text-main); line-height: 1.7; font-family: 'Amiri', 'Traditional Arabic', serif;">
                  <?php echo htmlspecialchars(mb_substr($h['arabic_text'], 0, 95)) . (mb_strlen($h['arabic_text']) > 95 ? '...' : ''); ?>
                </div>
              </td>
              <td style="max-width: 340px; font-size: 13px; line-height: 1.5;">
                <div style="color: var(--text-main);"><?php echo htmlspecialchars(mb_substr($h['bangla_text'], 0, 110)) . (mb_strlen($h['bangla_text']) > 110 ? '...' : ''); ?></div>
                <?php if (!empty($h['footnote_bn'])): ?>
                  <div style="margin-top: 6px; font-size: 11px; color: var(--accent-gold); background: rgba(217, 119, 6, 0.08); border: 1px solid rgba(217, 119, 6, 0.2); padding: 4px 8px; border-radius: 6px;">
                    <strong><?php echo __('ফুটনোট:', 'Footnote:'); ?></strong> <?php echo htmlspecialchars(mb_substr($h['footnote_bn'], 0, 75)) . (mb_strlen($h['footnote_bn']) > 75 ? '...' : ''); ?>
                  </div>
                <?php endif; ?>
              </td>
              <td>
                <span class="badge badge-success" style="font-size: 11px; font-weight: 700;"><?php echo htmlspecialchars($h['grade']); ?></span>
                <?php if (!empty($h['reference'])): ?>
                  <div style="font-size: 11px; color: var(--accent-gold); margin-top: 4px; font-weight: 600;">
                    <i class="fa-solid fa-bookmark" style="font-size: 9px; margin-right: 3px;"></i>
                    <?php echo htmlspecialchars($h['reference']); ?>
                  </div>
                <?php endif; ?>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <div style="display: inline-flex; align-items: center; justify-content: flex-end; gap: 4px;">
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 5px 8px;" onclick="editHadith(<?php echo htmlspecialchars(json_encode($h)); ?>)" title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
                    <i class="fa-solid fa-pen-to-square text-blue-500"></i>
                  </button>
                  <button type="button" class="btn btn-danger btn-sm" style="padding: 5px 8px;" onclick="confirmDelete('hadiths.php?delete=<?php echo $h['id']; ?>&book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $selectedChapter; ?>', '<?php echo __('এই হাদীসটি মুছে ফেলতে চান?', 'Are you sure you want to delete this hadith?'); ?>')" title="<?php echo __('মুছুন', 'Delete'); ?>">
                    <i class="fa-solid fa-trash-can"></i>
                  </button>
                </div>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>

  <?php if ($totalPages > 1): ?>
    <div style="padding: 14px 20px; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
      <span style="font-size: 12.5px; color: var(--text-dim);">
        <?php echo __('পৃষ্ঠা ', 'Page ') . toLangNum($page) . __(' এর ', ' of ') . toLangNum($totalPages); ?>
        (<?php echo __('মোট: ', 'Total: ') . toLangNum($totalHadiths); ?>)
      </span>
      <div style="display: flex; gap: 6px;">
        <?php if ($page > 1): ?>
          <a href="hadiths.php?page=<?php echo $page - 1; ?>&book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $selectedChapter; ?>&search=<?php echo urlencode($search); ?>" class="btn btn-secondary btn-sm"><?php echo __('⬅️ পূর্ববর্তী', '⬅️ Previous'); ?></a>
        <?php endif; ?>
        <?php if ($page < $totalPages): ?>
          <a href="hadiths.php?page=<?php echo $page + 1; ?>&book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $selectedChapter; ?>&search=<?php echo urlencode($search); ?>" class="btn btn-secondary btn-sm"><?php echo __('পরবর্তী ➡️', 'Next ➡️'); ?></a>
        <?php endif; ?>
      </div>
    </div>
  <?php endif; ?>
</div>

<!-- Modal Add/Edit Hadith (Spacious & Comfortable Editing) -->
<div class="modal-backdrop" id="hadithModal">
  <div class="modal-window" style="max-width: 840px;">
    <div class="modal-header" style="background: linear-gradient(135deg, rgba(16, 185, 129, 0.12), rgba(6, 78, 59, 0.2));">
      <div class="modal-title" id="hModalTitle" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-book-open" style="color: var(--primary);"></i>
        <span><?php echo __('নতুন হাদীস যুক্ত করুন', 'Add New Hadith'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('hadithModal')">&times;</button>
    </div>
    <form method="POST" action="hadiths.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="hAction" value="create">
      <input type="hidden" name="id" id="hId" value="">

      <div class="modal-body" style="max-height: 78vh; overflow-y: auto; padding: 20px;">

        <!-- Row 1: Auto-locked Book & Chapter + Auto Hadith No & Sahih Grade -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-3.5 mb-4">
          <!-- Auto-locked Book -->
          <div>
            <label class="form-label mb-1.5"><?php echo __('হাদীস গ্রন্থ', 'Hadith Book'); ?></label>
            <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 9px 12px; font-weight: 700; color: var(--primary); font-size: 13.5px; display: flex; align-items: center; gap: 8px;">
              <i class="fa-solid fa-book-bookmark"></i>
              <span id="displayHBookName"><?php echo htmlspecialchars($currentBookName); ?></span>
            </div>
            <input type="hidden" name="book_slug" id="inputHBook" value="<?php echo htmlspecialchars($selectedBook); ?>">
          </div>

          <!-- Auto-locked Chapter -->
          <div>
            <label class="form-label mb-1.5"><?php echo __('অধ্যায়', 'Chapter'); ?></label>
            <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 9px 12px; font-weight: 700; color: var(--text-heading); font-size: 13.5px; display: flex; align-items: center; gap: 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
              <i class="fa-solid fa-folder-tree text-emerald-500"></i>
              <span id="displayHChapName" style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                <?php echo __('অধ্যায় ', 'Chapter ') . toLangNum($selectedChapter > 0 ? $selectedChapter : 1) . ($currentChapterTitle ? ': ' . htmlspecialchars($currentChapterTitle) : ''); ?>
              </span>
            </div>
            <input type="hidden" name="chapter_number" id="inputHChap" value="<?php echo $selectedChapter > 0 ? $selectedChapter : 1; ?>">
          </div>

          <!-- Auto Hadith Number & Sahih Grade (Auto-set) -->
          <div>
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px;">
              <label class="form-label mb-0"><?php echo __('হাদীস নম্বর', 'Hadith No'); ?> <span class="text-rose-500">*</span></label>
              <span class="badge badge-success text-[11px] font-bold" id="displayHGrade"><?php echo isEn() ? 'Sahih' : 'সহীহ'; ?></span>
              <input type="hidden" name="grade" id="inputHGrade" value="<?php echo isEn() ? 'Sahih' : 'সহীহ'; ?>">
            </div>
            <input type="number" name="hadith_number" id="inputHNum" class="form-control font-extrabold text-base" value="1" required min="1" oninput="onHadithNumberManualChange()">
          </div>
        </div>

        <!-- Row 2: Narrators -->
        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('বাংলা বর্ণনাকারী সাহাবী', 'Bengali Narrator'); ?></label>
            <input type="text" name="narrator_bn" id="inputHNarratorBn" class="form-control" placeholder="<?php echo __('যেমন: ‘আলক্বামাহ ইবনু ওয়াক্কাস আল-লায়সী (রহঃ) থেকে বর্ণিত:', 'e.g. Narrated by Alqama...'); ?>">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইংরেজি বর্ণনাকারী', 'English Narrator'); ?></label>
            <input type="text" name="narrator_en" id="inputHNarratorEn" class="form-control" placeholder="e.g. Narrated by 'Alqama bin Waqas Al-Laithi:">
          </div>
        </div>

        <!-- Arabic Text (Extra Large & High Contrast) -->
        <div class="form-group">
          <label class="form-label" style="display: flex; align-items: center; justify-content: space-between;">
            <span><i class="fa-solid fa-align-right" style="color: var(--primary);"></i> <?php echo __('আরবি মূল পাঠ', 'Arabic Text with Tashkeel'); ?> *</span>
            <span style="font-size: 11px; color: var(--text-dim);"><?php echo __('বিশুদ্ধ আরবি ফন্ট ও হারাকাত (অনুবাদ হবে না)', 'Original Arabic'); ?></span>
          </label>
          <textarea name="arabic_text" id="inputHArabic" class="form-control arabic-input" rows="5" style="font-size: 18px; line-height: 1.8; padding: 12px 14px; font-family: 'Amiri', 'Traditional Arabic', serif;" placeholder="إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ..." required></textarea>
        </div>

        <!-- Bengali Translation (Large & Comfortable) -->
        <div class="form-group">
          <label class="form-label">
            <i class="fa-solid fa-language" style="color: var(--primary);"></i> <?php echo __('বাংলা অনুবাদ', 'Bengali Translation'); ?> *
          </label>
          <textarea name="bangla_text" id="inputHBangla" class="form-control" rows="5" style="font-size: 14.5px; line-height: 1.6; padding: 12px 14px;" placeholder="<?php echo __('নিশ্চয় সকল কাজের ফলাফল নিয়তের উপর নির্ভরশীল...', 'Enter Bengali translation...'); ?>" required></textarea>
        </div>

        <!-- English Translation (Spacious) -->
        <div class="form-group">
          <label class="form-label">
            <i class="fa-solid fa-globe" style="color: var(--primary);"></i> <?php echo __('ইংরেজি অনুবাদ', 'English Translation'); ?>
          </label>
          <textarea name="english_text" id="inputHEnglish" class="form-control" rows="4" style="font-size: 14px; line-height: 1.5; padding: 12px 14px;" placeholder="The reward of deeds depends upon the intentions..."></textarea>
        </div>

        <!-- Footnote & Takhrij (Bengali & English) -->
        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('বাংলা ফুটনোট ও তাখরীজ', 'Bengali Footnotes & Takhrij'); ?></label>
            <textarea name="footnote_bn" id="inputHFootnoteBn" class="form-control" rows="3" style="font-size: 13px;" placeholder="<?php echo __('(৫৪, ২৫২৯, ৩৮৯৮, ৫০৭০, ৬৬৮৯... আধুনিক প্রকাশনী- ১)', 'e.g. Bukhari 54, 2529...'); ?>"></textarea>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইংরেজি ফুটনোট ও তাখরীজ', 'English Footnotes & Takhrij'); ?></label>
            <textarea name="footnote_en" id="inputHFootnoteEn" class="form-control" rows="3" style="font-size: 13px;" placeholder="e.g. (54, 2529, 3898... Modern Publication: 1)"></textarea>
          </div>
        </div>

        <!-- Book Reference -->
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('রেফারেন্স', 'Book Reference'); ?></label>
          <input type="text" name="reference" id="inputHRef" class="form-control" placeholder="<?php echo __('যেমন: সহীহ বুখারী: ১', 'e.g. Sahih Bukhari: 1'); ?>">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('hadithModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary px-6" id="btnHSubmit">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('হাদীস সংরক্ষণ করুন', 'Save Hadith'); ?></span>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
const nextHadithMap = <?php echo json_encode($nextHadithMap); ?>;
const nextHadithByChapMap = <?php echo json_encode($nextHadithByChapMap); ?>;
const bookNameMap = <?php 
  $bMap = [];
  foreach ($books as $b) {
    $bMap[$b['book_slug']] = isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn'];
  }
  echo json_encode($bMap);
?>;
const chapNameMap = <?php 
  $cMap = [];
  foreach ($chaptersList as $c) {
    $cMap[$c['chapter_number']] = (isEn() ? 'Chapter ' . $c['chapter_number'] : 'অধ্যায় ' . toBnNum($c['chapter_number'])) . ($c['title_bn'] ? ': ' . (isEn() ? ($c['title_en'] ?: $c['title_bn']) : $c['title_bn']) : '');
  }
  echo json_encode($cMap);
?>;

// Real-time live auto-translations on typing (Zero buttons needed, instantaneous feel)
document.addEventListener('DOMContentLoaded', function() {
  setupLiveTranslate('inputHNarratorBn', 'inputHNarratorEn');
  setupLiveTranslate('inputHBangla', 'inputHEnglish');
  setupLiveTranslate('inputHFootnoteBn', 'inputHFootnoteEn');
});

function onHadithNumberManualChange() {
  const num = document.getElementById('inputHNum').value;
  const book = document.getElementById('inputHBook').value;
  let bookPrefix = 'সহীহ বুখারী: ';
  if (book === 'muslim') bookPrefix = 'সহীহ মুসলিম: ';
  else if (book === 'nasai') bookPrefix = 'সুনানে আন-নাসায়ী: ';
  else if (book === 'abudawood') bookPrefix = 'সুনানে আবু দাউদ: ';
  else if (book === 'tirmidhi') bookPrefix = 'জামে আত-তিরমিযী: ';
  else if (book === 'ibnmajah') bookPrefix = 'সুনানে ইবনে মাজাহ: ';
  else bookPrefix = (bookNameMap[book] || book) + ': ';
  if (num) {
    document.getElementById('inputHRef').value = bookPrefix + num;
  }
}

function openAddHadithModal() {
  document.getElementById('hModalTitle').innerHTML = '<i class="fa-solid fa-book-open" style="color: var(--primary);"></i> <span><?php echo __('নতুন হাদীস যুক্ত করুন', 'Add New Hadith'); ?></span>';
  document.getElementById('hAction').value = 'create';
  document.getElementById('hId').value = '';
  
  const urlParams = new URLSearchParams(window.location.search);
  const curBook = urlParams.get('book') || '<?php echo $selectedBook; ?>';
  const curChap = parseInt(urlParams.get('chapter') || '<?php echo $selectedChapter > 0 ? $selectedChapter : 1; ?>', 10);

  document.getElementById('inputHBook').value = curBook;
  document.getElementById('inputHChap').value = curChap;
  document.getElementById('inputHGrade').value = '<?php echo isEn() ? "Sahih" : "সহীহ"; ?>';
  document.getElementById('displayHGrade').textContent = '<?php echo isEn() ? "Sahih" : "সহীহ"; ?>';

  document.getElementById('displayHBookName').textContent = bookNameMap[curBook] || curBook;
  document.getElementById('displayHChapName').textContent = chapNameMap[curChap] || ('<?php echo __('অধ্যায় ', 'Chapter '); ?>' + curChap);

  // Auto set next sequential hadith number!
  const chapKey = curBook + '_' + curChap;
  const nextNum = nextHadithByChapMap[chapKey] || nextHadithMap[curBook] || 1;
  document.getElementById('inputHNum').value = nextNum;

  let bookPrefix = 'সহীহ বুখারী: ';
  if (curBook === 'muslim') bookPrefix = 'সহীহ মুসলিম: ';
  else if (curBook === 'nasai') bookPrefix = 'সুনানে আন-নাসায়ী: ';
  else if (curBook === 'abudawood') bookPrefix = 'সুনানে আবু দাউদ: ';
  else if (curBook === 'tirmidhi') bookPrefix = 'জামে আত-তিরমিযী: ';
  else if (curBook === 'ibnmajah') bookPrefix = 'সুনানে ইবনে মাজাহ: ';
  else bookPrefix = curBook + ': ';

  document.getElementById('inputHRef').value = bookPrefix + nextNum;

  document.getElementById('inputHNarratorBn').value = '';
  document.getElementById('inputHNarratorEn').value = '';
  document.getElementById('inputHArabic').value = '';
  document.getElementById('inputHBangla').value = '';
  document.getElementById('inputHEnglish').value = '';
  document.getElementById('inputHFootnoteBn').value = '';
  document.getElementById('inputHFootnoteEn').value = '';
  document.getElementById('btnHSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span><?php echo __('হাদীস সংরক্ষণ করুন', 'Save Hadith'); ?></span>';
  openModal('hadithModal');
}

function editHadith(h) {
  document.getElementById('hModalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square" style="color: var(--primary);"></i> <span><?php echo __('হাদীস সম্পাদনা করুন (#', 'Edit Hadith (#'); ?>' + h.hadith_number + ')</span>';
  document.getElementById('hAction').value = 'update';
  document.getElementById('hId').value = h.id;
  document.getElementById('inputHBook').value = h.book_slug;
  document.getElementById('inputHNum').value = h.hadith_number;
  document.getElementById('inputHChap').value = h.chapter_number;
  document.getElementById('inputHGrade').value = h.grade || '<?php echo isEn() ? "Sahih" : "সহীহ"; ?>';
  document.getElementById('displayHGrade').textContent = h.grade || '<?php echo isEn() ? "Sahih" : "সহীহ"; ?>';

  document.getElementById('displayHBookName').textContent = bookNameMap[h.book_slug] || h.book_name || h.book_slug;
  document.getElementById('displayHChapName').textContent = chapNameMap[h.chapter_number] || (h.chapter_title ? ('<?php echo __('অধ্যায় ', 'Chapter '); ?>' + h.chapter_number + ': ' + h.chapter_title) : ('<?php echo __('অধ্যায় ', 'Chapter '); ?>' + h.chapter_number));

  document.getElementById('inputHNarratorBn').value = h.narrator_bn || '';
  document.getElementById('inputHNarratorEn').value = h.narrator_en || '';
  document.getElementById('inputHArabic').value = h.arabic_text;
  document.getElementById('inputHBangla').value = h.bangla_text;
  document.getElementById('inputHEnglish').value = h.english_text || '';
  document.getElementById('inputHFootnoteBn').value = h.footnote_bn || '';
  document.getElementById('inputHFootnoteEn').value = h.footnote_en || '';
  document.getElementById('inputHRef').value = h.reference || '';
  document.getElementById('btnHSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span><?php echo __('পরিবর্তন সংরক্ষণ করুন', 'Save Changes'); ?></span>';
  openModal('hadithModal');
}

</script>

<?php require_once __DIR__ . '/footer.php'; ?>

