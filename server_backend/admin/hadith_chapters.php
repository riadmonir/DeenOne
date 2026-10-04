<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - HADITH CHAPTERS MANAGEMENT (হাদিস অধ্যায়সমূহ ব্যবস্থাপনা)
 * Complete Tailwind & Dual Light/Dark Mode Suite with CSV Export & CSV Import
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Helper function for numerals
function toBnNum($num) {
    return toLangNum($num);
}

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $expBook = trim($_GET['book'] ?? '');
    $whereExp = [];
    $paramsExp = [];
    if (!empty($expBook)) {
        $whereExp[] = "book_slug = ?";
        $paramsExp[] = $expBook;
    }
    $wSql = !empty($whereExp) ? "WHERE " . implode(" AND ", $whereExp) : "";

    $stmt = $pdo->prepare("SELECT id, book_slug, chapter_number, chapter_number_bn, title_bn, title_en, start_hadith, end_hadith, hadith_range, hadith_range_bn, total_hadith, display_order, is_active FROM hadith_chapters $wSql ORDER BY book_slug ASC, chapter_number ASC");
    $stmt->execute($paramsExp);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_hadith_chapters_' . (!empty($expBook) ? $expBook . '_' : '') . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, [
        'ID', 'Book_Slug', 'Chapter_Number', 'Chapter_Number_BN',
        'Title_BN', 'Title_EN', 'Start_Hadith', 'End_Hadith',
        'Hadith_Range', 'Hadith_Range_BN', 'Total_Hadith', 'Display_Order', 'Is_Active'
    ]);

    foreach ($rows as $r) {
        fputcsv($out, [
            $r['id'] ?? '',
            $r['book_slug'] ?? 'bukhari',
            $r['chapter_number'] ?? 1,
            $r['chapter_number_bn'] ?? '',
            $r['title_bn'] ?? '',
            $r['title_en'] ?? '',
            $r['start_hadith'] ?? 1,
            $r['end_hadith'] ?? 1,
            $r['hadith_range'] ?? '',
            $r['hadith_range_bn'] ?? '',
            $r['total_hadith'] ?? 0,
            $r['display_order'] ?? 0,
            $r['is_active'] ?? 1
        ]);
    }
    fclose($out);
    exit();
}

// -----------------------------------------------------------------------------
// 2. CSV IMPORT HANDLER
// -----------------------------------------------------------------------------
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'import_csv') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('hadith_chapters.php');
    }

    $retBook = trim($_POST['book_slug'] ?? 'bukhari');

    if (!empty($_FILES['csv_file']['tmp_name']) && is_uploaded_file($_FILES['csv_file']['tmp_name'])) {
        $file = fopen($_FILES['csv_file']['tmp_name'], 'r');
        $bom = fread($file, 3);
        if ($bom !== "\xEF\xBB\xBF") {
            rewind($file);
        }

        $header = fgetcsv($file);
        $importedCount = 0;
        $updatedCount = 0;

        while (($row = fgetcsv($file)) !== false) {
            if (empty($row) || count($row) < 5) continue;

            $id = (int)($row[0] ?? 0);
            $bookSlug = strtolower(trim($row[1] ?? $retBook));
            $chapNum = max(1, (int)($row[2] ?? 1));
            $chapNumBn = trim($row[3] ?? '') ?: toBnNum($chapNum);
            $titleBn = trim($row[4] ?? '');
            $titleEn = trim($row[5] ?? '');
            $rangeStart = max(1, (int)($row[6] ?? 1));
            $rangeEnd = max($rangeStart, (int)($row[7] ?? $rangeStart));
            $hadithRange = trim($row[8] ?? '') ?: "$rangeStart - $rangeEnd";
            $hadithRangeBn = trim($row[9] ?? '') ?: (toBnNum($rangeStart) . ' - ' . toBnNum($rangeEnd));
            $totalHadith = max(0, ($rangeEnd - $rangeStart + 1));
            $displayOrder = (int)($row[11] ?? $chapNum);
            $isActive = isset($row[12]) ? (int)$row[12] : 1;

            if (empty($titleBn)) continue;

            // Check if exists
            $checkStmt = $pdo->prepare("SELECT id FROM hadith_chapters WHERE (book_slug = ? AND chapter_number = ?) OR (id > 0 AND id = ?)");
            $checkStmt->execute([$bookSlug, $chapNum, $id]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $uStmt = $pdo->prepare("UPDATE hadith_chapters SET 
                    book_slug = ?, chapter_number = ?, chapter_number_bn = ?, title_bn = ?, title_en = ?, 
                    hadith_range = ?, hadith_range_bn = ?, start_hadith = ?, end_hadith = ?, 
                    total_hadith = ?, display_order = ?, is_active = ? WHERE id = ?");
                $uStmt->execute([
                    $bookSlug, $chapNum, $chapNumBn, $titleBn, $titleEn,
                    $hadithRange, $hadithRangeBn, $rangeStart, $rangeEnd,
                    $totalHadith, $displayOrder, $isActive, $existing['id']
                ]);
                $updatedCount++;
            } else {
                $iStmt = $pdo->prepare("INSERT INTO hadith_chapters 
                    (book_slug, chapter_number, chapter_number_bn, title_bn, title_en, hadith_range, hadith_range_bn, start_hadith, end_hadith, total_hadith, display_order, is_active) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $iStmt->execute([
                    $bookSlug, $chapNum, $chapNumBn, $titleBn, $titleEn,
                    $hadithRange, $hadithRangeBn, $rangeStart, $rangeEnd,
                    $totalHadith, $displayOrder, $isActive
                ]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "হাদিস অধ্যায় CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " টি নতুন যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " টি আপডেট হয়েছে।";
        $msgEn = "Hadith Chapters CSV Import Completed: " . toLangNum($importedCount) . " new added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_HADITH_CHAPTERS', 'hadith_chapters', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('hadith_chapters.php?book=' . urlencode($retBook));
}

// Handle Action (Create, Update, Delete, Toggle Status)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন মেয়াদোত্তীর্ণ হয়েছে। আবার চেষ্টা করুন।');
        redirect('hadith_chapters.php');
    }

    if ($action === 'create' || $action === 'update') {
        $bookSlug = trim($_POST['book_slug'] ?? 'bukhari');
        $chapterNum = max(1, (int)($_POST['chapter_number'] ?? 1));
        $chapterNumBn = toBnNum($chapterNum);
        $titleBn = trim($_POST['title_bn'] ?? '');
        $titleEn = trim($_POST['title_en'] ?? '');
        $rangeStart = max(1, (int)($_POST['start_hadith'] ?? 1));
        $rangeEnd = max($rangeStart, (int)($_POST['end_hadith'] ?? $rangeStart));
        $hadithRange = "$rangeStart - $rangeEnd";
        $hadithRangeBn = trim($_POST['hadith_range_bn'] ?? '');
        if (empty($hadithRangeBn)) {
            $hadithRangeBn = toBnNum($rangeStart) . ' - ' . toBnNum($rangeEnd);
        }
        $displayOrder = (int)($_POST['display_order'] ?? $chapterNum);
        $isActive = isset($_POST['is_active']) ? 1 : 0;
        $totalHadith = max(0, ($rangeEnd - $rangeStart + 1));

        if (empty($titleBn)) {
            setFlash('danger', 'অধ্যায়ের বাংলা নাম আবশ্যক!');
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO hadith_chapters 
                        (book_slug, chapter_number, chapter_number_bn, title_bn, title_en, hadith_range, hadith_range_bn, start_hadith, end_hadith, total_hadith, display_order, is_active) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$bookSlug, $chapterNum, $chapterNumBn, $titleBn, $titleEn, $hadithRange, $hadithRangeBn, $rangeStart, $rangeEnd, $totalHadith, $displayOrder, $isActive]);
                    logAdminAction($pdo, 'CREATE_HADITH_CHAPTER', 'hadith_chapters', (string)$pdo->lastInsertId(), "অধ্যায় তৈরি: $titleBn (কিতাব: $bookSlug, নং: $chapterNum)");
                    setFlash('success', 'নতুন অধ্যায় সফলভাবে তৈরি করা হয়েছে!');
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE hadith_chapters 
                        SET book_slug = ?, chapter_number = ?, chapter_number_bn = ?, title_bn = ?, title_en = ?, hadith_range = ?, hadith_range_bn = ?, start_hadith = ?, end_hadith = ?, total_hadith = ?, display_order = ?, is_active = ? 
                        WHERE id = ?");
                    $stmt->execute([$bookSlug, $chapterNum, $chapterNumBn, $titleBn, $titleEn, $hadithRange, $hadithRangeBn, $rangeStart, $rangeEnd, $totalHadith, $displayOrder, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_HADITH_CHAPTER', 'hadith_chapters', (string)$id, "অধ্যায় আপডেট: $titleBn (নং: $chapterNum)");
                    setFlash('success', 'অধ্যায় তথ্য সফলভাবে হালনাগাদ করা হয়েছে!');
                }
            } catch (Exception $e) {
                setFlash('danger', 'ডাটাবেজ ত্রুটি: ' . $e->getMessage());
            }
        }
    } elseif ($action === 'toggle_status') {
        $id = (int)($_POST['id'] ?? 0);
        $current = (int)($_POST['current_status'] ?? 0);
        $newStatus = $current ? 0 : 1;
        try {
            $stmt = $pdo->prepare("UPDATE hadith_chapters SET is_active = ? WHERE id = ?");
            $stmt->execute([$newStatus, $id]);
            logAdminAction($pdo, 'TOGGLE_HADITH_CHAPTER_STATUS', 'hadith_chapters', (string)$id, "স্ট্যাটাস পরিবর্তন: " . ($newStatus ? 'সক্রিয়' : 'নিষ্ক্রিয়'));
            setFlash('success', 'অধ্যায় স্ট্যাটাস সফলভাবে পরিবর্তিত হয়েছে!');
        } catch (Exception $e) {
            setFlash('danger', 'ত্রুটি: ' . $e->getMessage());
        }
    }

    $retBook = !empty($_POST['book_slug']) ? '?book=' . urlencode($_POST['book_slug']) : '';
    redirect('hadith_chapters.php' . $retBook);
}

// Handle Delete
if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM hadith_chapters WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_HADITH_CHAPTER', 'hadith_chapters', (string)$id, 'অধ্যায় মুছে ফেলা হয়েছে');
        setFlash('success', 'অধ্যায় সফলভাবে মুছে ফেলা হয়েছে!');
    } catch (Exception $e) {
        setFlash('danger', 'মুছে ফেলতে ব্যর্থ: ' . $e->getMessage());
    }
    $retBook = !empty($_GET['book']) ? '?book=' . urlencode($_GET['book']) : '';
    redirect('hadith_chapters.php' . $retBook);
}

// Fetch Books
$books = [];
try {
    $books = $pdo->query("SELECT * FROM hadith_books ORDER BY display_order ASC")->fetchAll();
} catch (Exception $e) {}

// Filtering & Fetching Chapters
$selectedBook = $_GET['book'] ?? 'bukhari';
$search = trim($_GET['search'] ?? '');

$where = ["c.book_slug = ?"];
$params = [$selectedBook];

if (!empty($search)) {
    $where[] = "(c.title_bn LIKE ? OR c.title_en LIKE ? OR c.hadith_range LIKE ? OR c.hadith_range_bn LIKE ? OR c.chapter_number = ?)";
    $term = "%$search%";
    $params[] = $term;
    $params[] = $term;
    $params[] = $term;
    $params[] = $term;
    $params[] = is_numeric($search) ? (int)$search : -1;
}
$whereSql = "WHERE " . implode(" AND ", $where);

$chapters = [];
try {
    $sql = "SELECT c.*, 
                   (SELECT COUNT(*) FROM hadith_items WHERE book_slug = c.book_slug AND chapter_number = c.chapter_number) as live_hadith_count
            FROM hadith_chapters c 
            $whereSql 
            ORDER BY c.chapter_number ASC";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $chapters = $stmt->fetchAll();
} catch (Exception $e) {}

// Find selected book name
$currentBookName = isEn() ? 'Sahih Bukhari' : 'সহীহ বুখারী';
foreach ($books as $b) {
    if ($b['book_slug'] === $selectedBook) {
        $currentBookName = isEn() ? ($b['name_en'] ?: $b['name_bn']) : $b['name_bn'];
        break;
    }
}

$pageTitle = __('অধ্যায়সমূহ', 'Chapters') . ' - ' . $currentBookName;
$activeNav = 'hadith_chapters';
require_once __DIR__ . '/header.php';
?>

<!-- Clean, Compact Breadcrumb & Page Header -->
<div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px; margin-bottom: 20px;">
  <div>
    <div style="display: flex; align-items: center; gap: 8px; font-size: 13px; font-weight: 700; color: var(--text-muted); margin-bottom: 4px;">
      <a href="hadith_books.php" style="color: var(--primary); text-decoration: none; display: flex; align-items: center; gap: 5px;">
        <i class="fa-solid fa-book-bookmark"></i> <span><?php echo __('গ্রন্থ তালিকা', 'Books'); ?></span>
      </a>
      <span>/</span>
      <span style="color: var(--text-heading);"><?php echo htmlspecialchars($currentBookName); ?></span>
    </div>
    <h2 style="font-size: 20px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
      <span style="color: #0284c7; font-weight: 900;">/</span>
      <span><?php echo htmlspecialchars($currentBookName); ?></span>
      <span class="badge badge-info" style="font-size: 12px; padding: 2px 8px;"><?php echo toLangNum(count($chapters)) . ' ' . __('টি অধ্যায়', 'Chapters'); ?></span>
    </h2>
  </div>
  <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
    <!-- CSV Export Button -->
    <a href="hadith_chapters.php?action=export_csv&book=<?php echo urlencode($selectedBook); ?>" class="btn btn-secondary btn-sm" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('অধ্যায়সমূহ CSV ফরম্যাটে ডাউনলোড করুন', 'Download chapters in CSV format'); ?>">
      <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
      <span><?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?></span>
    </a>

    <!-- CSV Import Button -->
    <button type="button" class="btn btn-secondary btn-sm" onclick="openImportChaptersModal()" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV ফাইল থেকে অধ্যায় ডাটাবেজে ইমপোর্ট করুন', 'Import chapters from CSV file'); ?>">
      <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
      <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
    </button>

    <a href="hadith_books.php" class="btn btn-secondary btn-sm" style="font-weight: 700;">
      <i class="fa-solid fa-arrow-left"></i> <span><?php echo __('গ্রন্থ তালিকা', 'Books List'); ?></span>
    </a>
    <button type="button" class="btn btn-primary btn-sm" onclick="openAddModal()" style="font-weight: 700;">
      <i class="fa-solid fa-plus"></i> <span><?php echo __('নতুন অধ্যায়', 'New Chapter'); ?></span>
    </button>
  </div>
</div>

<!-- Clean Search & Filter Bar -->
<div class="card mb-6">
  <div class="card-body p-4">
    <form method="GET" action="hadith_chapters.php" class="flex flex-col sm:flex-row gap-3 items-center">
      <input type="hidden" name="book" value="<?php echo htmlspecialchars($selectedBook); ?>">
      <div class="relative flex-1 w-full">
        <input type="text" name="search" class="form-control" placeholder="<?php echo __('অধ্যায়ের নাম বা রেঞ্জ লিখে খুঁজুন...', 'Search by chapter name or range...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
      </div>
      <div class="flex items-center gap-2 w-full sm:w-auto">
        <button type="submit" class="btn btn-primary btn-sm">
          <i class="fa-solid fa-magnifying-glass"></i>
          <span><?php echo __('খুঁজুন', 'Search'); ?></span>
        </button>
        <?php if (!empty($search)): ?>
          <a href="hadith_chapters.php?book=<?php echo urlencode($selectedBook); ?>" class="btn btn-secondary btn-sm">
            <span><?php echo __('রিসেট', 'Reset'); ?></span>
          </a>
        <?php endif; ?>
      </div>
    </form>
  </div>
</div>

<!-- Chapters Table View -->
<div class="card">
  <div class="card-header">
    <div class="card-title">
      <span class="badge badge-success"><?php echo toLangNum(count($chapters)); ?></span>
      <span><?php echo htmlspecialchars($currentBookName); ?> - <?php echo __('অধ্যায় তালিকা', 'Chapter List'); ?></span>
    </div>
    <span class="text-xs text-slate-500 dark:text-slate-400"><?php echo __('হাদিস রেঞ্জ ও ক্রম অনুযায়ী সাজানো', 'Arranged by hadith range and sequence'); ?></span>
  </div>

  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 90px;" class="text-center"><?php echo __('অধ্যায় নং', 'Chapter No'); ?></th>
          <th><?php echo __('অধ্যায়ের নাম', 'Chapter Title'); ?></th>
          <th class="text-center"><?php echo __('হাদিসের রেঞ্জ', 'Hadith Range'); ?></th>
          <th class="text-center"><?php echo __('হাদিস সংখ্যা', 'Total Hadiths'); ?></th>
          <th class="text-center"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th class="text-right" style="width: 220px;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($chapters)): ?>
          <tr>
            <td colspan="6" class="text-center py-12 text-slate-400">
              <div class="text-4xl mb-3">📂</div>
              <div class="font-bold text-base text-slate-600 dark:text-slate-300"><?php echo __('কোনো অধ্যায় পাওয়া যায়নি', 'No chapters found'); ?></div>
              <p class="text-sm mt-1"><?php echo __('উপরের বাটনে ক্লিক করে নতুন অধ্যায় যুক্ত করুন।', 'Click the button above to add a new chapter.'); ?></p>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($chapters as $c): ?>
            <tr>
              <td class="text-center">
                <span class="inline-flex items-center justify-center w-9 h-9 rounded-full bg-emerald-100 dark:bg-emerald-950 text-emerald-700 dark:text-emerald-300 font-extrabold text-sm border border-emerald-200 dark:border-emerald-800">
                  <?php echo toLangNum($c['chapter_number']); ?>
                </span>
              </td>
              <td>
                <a href="hadiths.php?book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $c['chapter_number']; ?>" 
                   class="group block no-underline" title="<?php echo __('অধ্যায়ের হাদিসসমূহ ওপেন করুন', 'Open chapter hadiths'); ?>">
                  <div class="font-bold text-slate-900 dark:text-white text-base group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors flex items-center gap-2">
                    <span class="hover:underline">
                      <?php echo htmlspecialchars(isEn() ? ($c['title_en'] ?: $c['title_bn']) : $c['title_bn']); ?>
                    </span>
                    <i class="fa-solid fa-arrow-right text-xs text-emerald-500 opacity-60 group-hover:opacity-100 group-hover:translate-x-1 transition-all"></i>
                  </div>
                  <?php if (isEn() && !empty($c['title_bn'])): ?>
                    <div class="text-xs text-slate-500 dark:text-slate-400 mt-0.5"><?php echo htmlspecialchars($c['title_bn']); ?></div>
                  <?php elseif (!isEn() && !empty($c['title_en'])): ?>
                    <div class="text-xs text-slate-500 dark:text-slate-400 mt-0.5"><?php echo htmlspecialchars($c['title_en']); ?></div>
                  <?php endif; ?>
                </a>
              </td>
              <td class="text-center">
                <span class="inline-flex items-center px-3 py-1 rounded-full text-xs font-bold bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700">
                  <?php 
                    $rangeText = isEn() 
                      ? (toLangNum($c['start_hadith']) . ' - ' . toLangNum($c['end_hadith']))
                      : ($c['hadith_range_bn'] ?: (toLangNum($c['start_hadith']) . ' - ' . toLangNum($c['end_hadith'])));
                    echo htmlspecialchars($rangeText);
                  ?>
                </span>
              </td>
              <td class="text-center">
                <span class="badge badge-info">
                  <?php echo toLangNum($c['live_hadith_count'] > 0 ? $c['live_hadith_count'] : $c['total_hadith']); ?> <?php echo __('টি', ''); ?>
                </span>
              </td>
              <td class="text-center">
                <form method="POST" action="hadith_chapters.php" style="display: inline;">
                  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
                  <input type="hidden" name="action" value="toggle_status">
                  <input type="hidden" name="id" value="<?php echo $c['id']; ?>">
                  <input type="hidden" name="current_status" value="<?php echo $c['is_active']; ?>">
                  <input type="hidden" name="book_slug" value="<?php echo htmlspecialchars($selectedBook); ?>">
                  <button type="submit" class="border-0 bg-transparent cursor-pointer p-0">
                    <?php if ($c['is_active']): ?>
                      <span class="badge badge-success"><i class="fa-solid fa-circle-check"></i> <?php echo __('সক্রিয়', 'Active'); ?></span>
                    <?php else: ?>
                      <span class="badge badge-secondary"><i class="fa-solid fa-circle-xmark"></i> <?php echo __('নিষ্ক্রিয়', 'Inactive'); ?></span>
                    <?php endif; ?>
                  </button>
                </form>
              </td>
              <td class="text-right" style="white-space: nowrap;">
                <div style="display: inline-flex; align-items: center; justify-content: flex-end; gap: 6px; white-space: nowrap;">
                  <a href="hadiths.php?book=<?php echo urlencode($selectedBook); ?>&chapter=<?php echo $c['chapter_number']; ?>" 
                     class="btn btn-primary btn-sm" style="font-size: 12px; padding: 6px 12px; display: inline-flex; align-items: center; gap: 6px; white-space: nowrap;" title="<?php echo __('এই অধ্যায়ের হাদিসসমূহ দেখুন ও পরিচালনা করুন', 'View and manage hadiths of this chapter'); ?>">
                    <i class="fa-solid fa-book-open"></i>
                    <span><?php echo __('হাদিসসমূহ', 'Hadiths'); ?></span>
                    <span class="badge" style="background: rgba(255,255,255,0.25); color: #fff; padding: 1px 6px; font-size: 11px; border-radius: 9999px;"><?php echo toLangNum($c['live_hadith_count']); ?></span>
                    <i class="fa-solid fa-arrow-right" style="font-size: 10px;"></i>
                  </a>
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 6px 9px;" onclick='openEditModal(<?php echo json_encode($c, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)' title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
                    <i class="fa-solid fa-pen-to-square text-blue-500"></i>
                  </button>
                  <a href="hadith_chapters.php?delete=<?php echo $c['id']; ?>&book=<?php echo urlencode($selectedBook); ?>" 
                     class="btn btn-danger btn-sm" style="padding: 6px 9px;" title="<?php echo __('মুছুন', 'Delete'); ?>" onclick="return confirmAction(event, this.href, '<?php echo __('আপনি কি নিশ্চিতভাবে এই অধ্যায়টি মুছে ফেলতে চান?', 'Are you sure you want to delete this chapter?'); ?>');">
                    <i class="fa-solid fa-trash-can"></i>
                  </a>
                </div>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- Modal: Add / Edit Chapter -->
<div class="modal-backdrop" id="chapterModal">
  <div class="modal-window">
    <form method="POST" action="hadith_chapters.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="modalAction" value="create">
      <input type="hidden" name="id" id="chapterId" value="0">
      <input type="hidden" name="book_slug" id="modalBookSlug" value="<?php echo htmlspecialchars($selectedBook); ?>">

      <div class="modal-header">
        <div class="modal-title" id="modalTitle">
          <i class="fa-solid fa-layer-group text-emerald-500 mr-2"></i><span><?php echo __('নতুন অধ্যায় তৈরি করুন', 'Create New Chapter'); ?></span>
        </div>
        <button type="button" class="btn-modal-close" onclick="closeModal('chapterModal')">&times;</button>
      </div>

      <div class="modal-body">
        <!-- Locked Current Hadith Book Display -->
        <div class="form-group">
          <label class="form-label"><?php echo __('হাদিস গ্রন্থ', 'Hadith Book'); ?></label>
          <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 14px; font-weight: 700; color: var(--primary); font-size: 14px; display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-book-bookmark"></i>
            <span id="modalBookNameDisplay"><?php echo htmlspecialchars($currentBookName); ?></span>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-3 mb-4">
          <div>
            <label class="form-label"><?php echo __('অধ্যায় নং', 'Chapter No'); ?> <span class="text-rose-500">*</span></label>
            <input type="number" name="chapter_number" id="modalChapterNum" class="form-control font-bold" min="1" value="<?php echo count($chapters) + 1; ?>" required>
          </div>
          <div>
            <label class="form-label"><?php echo __('শুরু হাদিস নং', 'Start Hadith No'); ?></label>
            <input type="number" name="start_hadith" id="modalRangeStart" class="form-control" min="1" value="1" oninput="autoGenerateRange()">
          </div>
          <div>
            <label class="form-label"><?php echo __('শেষ হাদিস নং', 'End Hadith No'); ?></label>
            <input type="number" name="end_hadith" id="modalRangeEnd" class="form-control" min="1" value="7" oninput="autoGenerateRange()">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('অধ্যায়ের নাম বাংলা', 'Chapter Title Bengali'); ?> <span class="text-rose-500">*</span></label>
          <input type="text" name="title_bn" id="modalTitleBn" class="form-control" placeholder="<?php echo __('যেমন: ওহীর সূচনা অধ্যায়, ঈমান, সালাত', 'e.g. Revelation, Belief, Prayers'); ?>" required>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('অধ্যায়ের নাম English', 'Chapter Title English'); ?></label>
          <input type="text" name="title_en" id="modalTitleEn" class="form-control" placeholder="e.g. Revelation, Belief, Prayers">
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('হাদিস রেঞ্জ টেক্সট বাংলা', 'Hadith Range Text Bengali'); ?></label>
          <input type="text" name="hadith_range_bn" id="modalRangeBn" class="form-control" placeholder="<?php echo __('যেমন: ১ - ৭ বা ৮ - ৫৮', 'e.g. 1 - 7 or 8 - 58'); ?>">
          <div class="form-hint"><?php echo __('শুরু ও শেষ নম্বর দিলে স্বয়ংক্রিয়ভাবে তৈরি হয়', 'Generated automatically from start and end numbers'); ?></div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <div class="form-group">
            <label class="form-label"><?php echo __('ডিসপ্লে ক্রম', 'Display Order'); ?></label>
            <input type="number" name="display_order" id="modalDisplayOrder" class="form-control" value="1" min="0">
          </div>
          <div class="form-group flex items-center pt-6">
            <label class="switch-label">
              <span class="switch">
                <input type="checkbox" name="is_active" id="modalIsActive" checked>
                <span class="slider"></span>
              </span>
              <span class="font-bold text-sm text-slate-800 dark:text-slate-200"><?php echo __('অ্যাপে সক্রিয় রাখুন', 'Keep Active in App'); ?></span>
            </label>
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('chapterModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary px-6" id="btnChapterSubmit">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('সংরক্ষণ করুন', 'Save Changes'); ?></span>
        </button>
      </div>
    </form>
  </div>
</div>

<!-- Modal: Import Hadith Chapters CSV -->
<div id="importChaptersCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 540px; box-shadow: var(--shadow-lg);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?php echo __('হাদিস অধ্যায় CSV ডাটাবেজ ইমপোর্ট', 'Import Hadith Chapters from CSV'); ?></span>
      </h3>
      <button type="button" onclick="closeImportChaptersModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 18px; line-height: 1.5;">
      <?php echo __('একটি বৈধ CSV ফাইল আপলোড করে হাদিস অধ্যায়সমূহ এক ক্লিকে MySQL ডাটাবেজে যুক্ত বা আপডেট করুন।', 'Upload a valid CSV file to bulk import or update hadith chapters in MySQL database.'); ?>
    </p>

    <form method="POST" action="hadith_chapters.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">
      <input type="hidden" name="book_slug" value="<?php echo htmlspecialchars($selectedBook); ?>">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?php echo __('CSV ফাইল নির্বাচন করুন (.csv)', 'Select CSV File (.csv)'); ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?php echo __('সাপোর্টেড কলাম বিন্যাস:', 'Supported Column Format:'); ?></strong><br>
        <code>ID, Book_Slug, Chapter_Number, Chapter_Number_BN, Title_BN, Title_EN, Start_Hadith, End_Hadith, Hadith_Range, Hadith_Range_BN, Total_Hadith, Display_Order, Is_Active</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportChaptersModal()" style="font-weight: 600;">
          <?php echo __('বাতিল', 'Cancel'); ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?php echo __('ইমপোর্ট ও সিঙ্ক করুন', 'Import & Sync'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
const isEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

function toBnDigits(num) {
  if (isEnglish) return String(num);
  const bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
  return String(num).replace(/[0-9]/g, d => bn[d]);
}

function autoGenerateRange() {
  const s = document.getElementById('modalRangeStart').value;
  const e = document.getElementById('modalRangeEnd').value;
  if (s && e) {
    document.getElementById('modalRangeBn').value = toBnDigits(s) + ' - ' + toBnDigits(e);
  }
}

function openAddModal() {
  document.getElementById('modalAction').value = 'create';
  document.getElementById('modalTitle').innerHTML = '<i class="fa-solid fa-layer-group text-emerald-500 mr-2"></i><span>' + (isEnglish ? 'Create New Chapter' : 'নতুন অধ্যায় তৈরি করুন') + '</span>';
  document.getElementById('chapterId').value = '0';
  document.getElementById('modalTitleBn').value = '';
  document.getElementById('modalTitleEn').value = '';
  document.getElementById('modalChapterNum').value = '<?php echo count($chapters) + 1; ?>';
  document.getElementById('modalRangeStart').value = '1';
  document.getElementById('modalRangeEnd').value = '7';
  document.getElementById('modalDisplayOrder').value = '<?php echo count($chapters) + 1; ?>';
  autoGenerateRange();
  document.getElementById('modalIsActive').checked = true;
  document.getElementById('btnChapterSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span>' + (isEnglish ? 'Add Chapter' : 'অধ্যায় যোগ করুন') + '</span>';
  openModal('chapterModal');
}

function openEditModal(c) {
  document.getElementById('modalAction').value = 'update';
  document.getElementById('modalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square text-emerald-500 mr-2"></i><span>' + (isEnglish ? 'Edit Chapter #' : 'অধ্যায় সম্পাদনা করুন #') + c.chapter_number + '</span>';
  document.getElementById('chapterId').value = c.id;
  document.getElementById('modalBookSlug').value = c.book_slug;
  document.getElementById('modalChapterNum').value = c.chapter_number;
  document.getElementById('modalTitleBn').value = c.title_bn;
  document.getElementById('modalTitleEn').value = c.title_en || '';
  document.getElementById('modalRangeStart').value = c.start_hadith || 1;
  document.getElementById('modalRangeEnd').value = c.end_hadith || 1;
  document.getElementById('modalRangeBn').value = c.hadith_range_bn || (toBnDigits(c.start_hadith || 1) + ' - ' + toBnDigits(c.end_hadith || 1));
  document.getElementById('modalDisplayOrder').value = c.display_order || c.chapter_number;
  document.getElementById('modalIsActive').checked = parseInt(c.is_active) === 1;
  document.getElementById('btnChapterSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span>' + (isEnglish ? 'Save Changes' : 'পরিবর্তন সংরক্ষণ') + '</span>';
  openModal('chapterModal');
}

function openImportChaptersModal() {
  document.getElementById('importChaptersCsvModal').style.display = 'flex';
}
function closeImportChaptersModal() {
  document.getElementById('importChaptersCsvModal').style.display = 'none';
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
