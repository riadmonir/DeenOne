<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - HADITH BOOKS & CATEGORIES MANAGEMENT (হাদিস ক্যাটাগরি ও গ্রন্থ সম্ভার)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$pdo = getDbConnection();

// Ensure hadith_books table exists
try {
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
} catch (Exception $e) {}

// Ensure all required columns exist in hadith_books
try {
    $stmtCols = $pdo->query("SHOW COLUMNS FROM hadith_books");
    $existingCols = $stmtCols->fetchAll(PDO::FETCH_COLUMN);
    if (!in_array('name_en', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN name_en VARCHAR(150) DEFAULT NULL AFTER name_bn");
    }
    if (!in_array('author_bn', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN author_bn VARCHAR(120) DEFAULT NULL AFTER name_en");
    }
    if (!in_array('author_en', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN author_en VARCHAR(120) DEFAULT NULL AFTER author_bn");
    }
    if (!in_array('name_ar', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN name_ar VARCHAR(150) DEFAULT NULL AFTER author_en");
    }
    if (!in_array('initials', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN initials VARCHAR(20) DEFAULT 'H' AFTER name_ar");
    }
    if (!in_array('color_hex', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN color_hex VARCHAR(20) DEFAULT '#10B981' AFTER initials");
    }
    if (!in_array('total_hadith_bn', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN total_hadith_bn VARCHAR(50) DEFAULT '' AFTER total_hadith");
    }
    if (!in_array('description_bn', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN description_bn TEXT DEFAULT NULL AFTER total_hadith_bn");
    }
    if (!in_array('display_order', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN display_order INT UNSIGNED DEFAULT 0 AFTER description_bn");
    }
    if (!in_array('is_active', $existingCols)) {
        $pdo->exec("ALTER TABLE hadith_books ADD COLUMN is_active TINYINT(1) DEFAULT 1 AFTER display_order");
    }
} catch (Exception $e) {}

// Helper function for Bengali numerals
function toBnNum($num) {
    $bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    return str_replace(range(0, 9), $bnDigits, (string)$num);
}

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $searchExp = trim($_GET['search'] ?? '');
    $whereExp = [];
    $paramsExp = [];
    if (!empty($searchExp)) {
        $whereExp[] = "(name_bn LIKE ? OR name_en LIKE ? OR author_bn LIKE ? OR book_slug LIKE ?)";
        $term = "%$searchExp%";
        $paramsExp = [$term, $term, $term, $term];
    }
    $wSql = !empty($whereExp) ? "WHERE " . implode(" AND ", $whereExp) : "";

    $stmt = $pdo->prepare("SELECT id, book_slug, name_bn, name_en, name_ar, author_bn, author_en, initials, color_hex, total_hadith, total_hadith_bn, description_bn, display_order, is_active FROM hadith_books $wSql ORDER BY display_order ASC, id ASC");
    $stmt->execute($paramsExp);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_hadith_books_' . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, [
        'ID', 'Book_Slug', 'Name_BN', 'Name_EN', 'Name_AR',
        'Author_BN', 'Author_EN', 'Initials', 'Color_Hex',
        'Total_Hadith', 'Total_Hadith_BN', 'Description_BN', 'Display_Order', 'Is_Active'
    ]);

    foreach ($rows as $r) {
        fputcsv($out, [
            $r['id'] ?? '',
            $r['book_slug'] ?? '',
            $r['name_bn'] ?? '',
            $r['name_en'] ?? '',
            $r['name_ar'] ?? '',
            $r['author_bn'] ?? '',
            $r['author_en'] ?? '',
            $r['initials'] ?? 'H',
            $r['color_hex'] ?? '#10B981',
            $r['total_hadith'] ?? 0,
            $r['total_hadith_bn'] ?? '',
            $r['description_bn'] ?? '',
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
        redirect('hadith_books.php');
    }

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
            if (empty($row) || count($row) < 3) continue;

            $id = (int)($row[0] ?? 0);
            $bookSlug = strtolower(trim($row[1] ?? ''));
            $bookSlug = preg_replace('/[^a-z0-9_]/', '_', $bookSlug);
            $nameBn = trim($row[2] ?? '');
            $nameEn = trim($row[3] ?? '');
            $nameAr = trim($row[4] ?? '');
            $authorBn = trim($row[5] ?? '');
            $authorEn = trim($row[6] ?? '');
            $initials = trim($row[7] ?? 'H');
            $colorHex = trim($row[8] ?? '#10B981');
            $totalHadith = max(0, (int)($row[9] ?? 0));
            $totalHadithBn = !empty($row[10]) ? trim($row[10]) : toBnNum($totalHadith);
            $descriptionBn = trim($row[11] ?? '');
            $displayOrder = max(0, (int)($row[12] ?? 0));
            $isActive = isset($row[13]) ? (int)$row[13] : 1;

            if (empty($bookSlug) || empty($nameBn)) continue;

            // Check duplicate by slug or ID
            $checkStmt = $pdo->prepare("SELECT id FROM hadith_books WHERE book_slug = ? OR (id > 0 AND id = ?)");
            $checkStmt->execute([$bookSlug, $id]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $uStmt = $pdo->prepare("UPDATE hadith_books SET 
                    book_slug = ?, name_bn = ?, name_en = ?, name_ar = ?, author_bn = ?, author_en = ?, 
                    initials = ?, color_hex = ?, total_hadith = ?, total_hadith_bn = ?, description_bn = ?, 
                    display_order = ?, is_active = ? WHERE id = ?");
                $uStmt->execute([
                    $bookSlug, $nameBn, $nameEn, $nameAr, $authorBn, $authorEn,
                    $initials, $colorHex, $totalHadith, $totalHadithBn, $descriptionBn,
                    $displayOrder, $isActive, $existing['id']
                ]);
                $updatedCount++;
            } else {
                $iStmt = $pdo->prepare("INSERT INTO hadith_books 
                    (book_slug, name_bn, name_en, name_ar, author_bn, author_en, initials, color_hex, total_hadith, total_hadith_bn, description_bn, display_order, is_active) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $iStmt->execute([
                    $bookSlug, $nameBn, $nameEn, $nameAr, $authorBn, $authorEn,
                    $initials, $colorHex, $totalHadith, $totalHadithBn, $descriptionBn,
                    $displayOrder, $isActive
                ]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "হাদিস গ্রন্থ CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " টি নতুন যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " টি আপডেট হয়েছে।";
        $msgEn = "Hadith Books CSV Import Completed: " . toLangNum($importedCount) . " new added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_HADITH_BOOKS', 'hadith_books', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('hadith_books.php');
}

// Handle Form Submissions (Create / Update / Status Toggle)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। অনুগ্রহ করে পুনরায় চেষ্টা করুন।');
        redirect('hadith_books.php');
    }

    if ($action === 'create' || $action === 'update') {
        $nameBn = trim($_POST['name_bn'] ?? '');
        $nameEn = trim($_POST['name_en'] ?? '');
        $nameAr = trim($_POST['name_ar'] ?? '');
        $authorBn = trim($_POST['author_bn'] ?? '');
        $authorEn = trim($_POST['author_en'] ?? '');
        $bookSlug = strtolower(trim($_POST['book_slug'] ?? ''));
        // Clean slug to only letters, numbers, and underscores
        $bookSlug = preg_replace('/[^a-z0-9_]/', '_', $bookSlug);
        $totalHadith = max(0, (int)($_POST['total_hadith'] ?? 0));
        $totalHadithBn = toBnNum($totalHadith);
        $descriptionBn = trim($_POST['description_bn'] ?? '');
        $displayOrder = max(0, (int)($_POST['display_order'] ?? 0));
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($nameBn) || empty($bookSlug)) {
            setFlash('danger', 'হাদিস গ্রন্থের বাংলা নাম এবং ইউনিক স্লাগ উভয়ই প্রদান করা আবশ্যক।');
        } else {
            try {
                if ($action === 'create') {
                    // Check duplicate slug
                    $checkStmt = $pdo->prepare("SELECT COUNT(*) FROM hadith_books WHERE book_slug = ?");
                    $checkStmt->execute([$bookSlug]);
                    if ((int)$checkStmt->fetchColumn() > 0) {
                        setFlash('danger', "'{$bookSlug}' স্লাগের কিতাব ইতিমধ্যে বিদ্যমান। অনুগ্রহ করে ভিন্ন স্লাগ ব্যবহার করুন।");
                    } else {
                        $stmt = $pdo->prepare("INSERT INTO hadith_books 
                            (book_slug, name_bn, author_bn, author_en, name_en, name_ar, total_hadith, total_hadith_bn, description_bn, display_order, is_active) 
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        $stmt->execute([$bookSlug, $nameBn, $authorBn, $authorEn, $nameEn, $nameAr, $totalHadith, $totalHadithBn, $descriptionBn, $displayOrder, $isActive]);
                        logAdminAction($pdo, 'CREATE_HADITH_BOOK', 'hadith_books', (string)$pdo->lastInsertId(), "নতুন হাদিস গ্রন্থ যুক্ত: $nameBn ($bookSlug)");
                        setFlash('success', "নতুন হাদিস ক্যাটাগরি / গ্রন্থ ‘{$nameBn}’ সফলভাবে তৈরি করা হয়েছে!");
                    }
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE hadith_books 
                        SET name_bn = ?, author_bn = ?, author_en = ?, name_en = ?, name_ar = ?, total_hadith = ?, total_hadith_bn = ?, description_bn = ?, display_order = ?, is_active = ? 
                        WHERE id = ?");
                    $stmt->execute([$nameBn, $authorBn, $authorEn, $nameEn, $nameAr, $totalHadith, $totalHadithBn, $descriptionBn, $displayOrder, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_HADITH_BOOK', 'hadith_books', (string)$id, "হাদিস গ্রন্থ আপডেট: $nameBn");
                    setFlash('success', "হাদিস গ্রন্থ ‘{$nameBn}’ সফলভাবে আপডেট করা হয়েছে!");
                }
            } catch (Exception $e) {
                setFlash('danger', 'ডাটাবেস সংরক্ষণ ত্রুটি: ' . $e->getMessage());
            }
        }
    }

    if ($action === 'toggle_status') {
        $id = (int)($_POST['id'] ?? 0);
        try {
            $stmt = $pdo->prepare("UPDATE hadith_books SET is_active = CASE WHEN is_active = 1 THEN 0 ELSE 1 END WHERE id = ?");
            $stmt->execute([$id]);
            logAdminAction($pdo, 'TOGGLE_HADITH_BOOK_STATUS', 'hadith_books', (string)$id, "স্ট্যাটাস পরিবর্তন");
            setFlash('success', 'হাদিস গ্রন্থের দৃশ্যমানতা স্ট্যাটাস পরিবর্তিত হয়েছে!');
        } catch (Exception $e) {
            setFlash('danger', 'স্ট্যাটাস পরিবর্তন ব্যর্থ: ' . $e->getMessage());
        }
    }

    redirect('hadith_books.php');
}

// Handle Delete
if (isset($_GET['delete'])) {
    $deleteId = (int)$_GET['delete'];
    $token = $_GET['token'] ?? '';
    if (!verifyCsrfToken($token)) {
        setFlash('danger', 'নিরাপত্তা টোকেন সঠিক নয়।');
    } else {
        try {
            // Find book slug
            $bStmt = $pdo->prepare("SELECT book_slug, name_bn FROM hadith_books WHERE id = ?");
            $bStmt->execute([$deleteId]);
            $bRow = $bStmt->fetch();
            if ($bRow) {
                $delSlug = $bRow['book_slug'];
                // Check if chapters or hadiths exist
                $cCheck = $pdo->prepare("SELECT COUNT(*) FROM hadith_chapters WHERE book_slug = ?");
                $cCheck->execute([$delSlug]);
                $chapCount = (int)$cCheck->fetchColumn();

                if ($chapCount > 0) {
                    setFlash('warning', "গ্রন্থ ‘{$bRow['name_bn']}’-এর অধীনে {$chapCount}টি অধ্যায় রয়েছে। প্রথমে অধ্যায়সমূহ মুছে ফেলুন অথবা গ্রন্থটি নিষ্ক্রিয় করে রাখুন।");
                } else {
                    $dStmt = $pdo->prepare("DELETE FROM hadith_books WHERE id = ?");
                    $dStmt->execute([$deleteId]);
                    logAdminAction($pdo, 'DELETE_HADITH_BOOK', 'hadith_books', (string)$deleteId, "গ্রন্থ মুছে ফেলা: {$bRow['name_bn']}");
                    setFlash('success', "হাদিস গ্রন্থ ‘{$bRow['name_bn']}’ সফলভাবে মুছে ফেলা হয়েছে।");
                }
            }
        } catch (Exception $e) {
            setFlash('danger', 'মুছতে ব্যর্থ: ' . $e->getMessage());
        }
    }
    redirect('hadith_books.php');
}

// Search & Filter
$search = trim($_GET['search'] ?? '');
$searchParam = "%$search%";

$whereSql = "";
$params = [];
if (!empty($search)) {
    $whereSql = "WHERE (b.name_bn LIKE ? OR b.name_en LIKE ? OR b.name_ar LIKE ? OR b.book_slug LIKE ? OR b.author_bn LIKE ?)";
    $params = [$searchParam, $searchParam, $searchParam, $searchParam, $searchParam];
}

// Fetch all books with real live counts of chapters and hadiths
$booksQuery = "SELECT b.*, 
    (SELECT COUNT(*) FROM hadith_chapters c WHERE c.book_slug = b.book_slug) AS chapter_count,
    (SELECT COUNT(*) FROM hadith_items h WHERE h.book_slug = b.book_slug) AS real_hadith_count
    FROM hadith_books b 
    $whereSql 
    ORDER BY b.display_order ASC, b.id ASC";

$stmt = $pdo->prepare($booksQuery);
$stmt->execute($params);
$books = $stmt->fetchAll();

// Global Stats
$totalBooksCount = count($books);
$totalChaptersGlobal = 0;
$totalHadithsGlobal = 0;
try {
    $totalChaptersGlobal = (int)$pdo->query("SELECT COUNT(*) FROM hadith_chapters")->fetchColumn();
    $totalHadithsGlobal = (int)$pdo->query("SELECT COUNT(*) FROM hadith_items")->fetchColumn();
} catch (Exception $e) {}

$pageTitle = __('হাদিস গ্রন্থ ও ক্যাটাগরি ব্যবস্থাপনা', 'Hadith Books & Categories Management');
$activeNav = 'hadith_books';
require_once __DIR__ . '/header.php';
?>

<style>
.hadith-book-interactive-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(16, 185, 129, 0.12), 0 4px 10px rgba(0, 0, 0, 0.05);
  border-color: rgba(16, 185, 129, 0.45);
}
</style>

<div class="content-body">

  <!-- Flash Message -->
  <?php if ($flash): ?>
    <div class="alert alert-<?php echo htmlspecialchars($flash['type']); ?>">
      <div style="display:flex; align-items:center; gap:10px;">
        <i class="fa-solid <?php echo $flash['type'] === 'success' ? 'fa-circle-check' : ($flash['type'] === 'warning' ? 'fa-triangle-exclamation' : 'fa-circle-xmark'); ?>"></i>
        <span><?php echo htmlspecialchars($flash['message']); ?></span>
      </div>
      <button type="button" class="alert-close" onclick="this.parentElement.remove();">&times;</button>
    </div>
  <?php endif; ?>

  <!-- Hero Banner -->
  <div class="hero-banner">
    <div style="max-width: 780px;">
      <h2 style="font-size: 26px; font-weight: 800; color: var(--text-heading); margin-bottom: 0;">
        <?php echo __('হাদিস গ্রন্থ', 'Hadith Books'); ?>
      </h2>
    </div>
  </div>

  <!-- Stats Grid -->
  <div class="stats-grid">
    <div class="stat-card">
      <div class="stat-icon-wrap emerald">
        <i class="fa-solid fa-book-bookmark"></i>
      </div>
      <div class="stat-content">
        <h3><?php echo toLangNum($totalBooksCount); ?></h3>
        <p><?php echo __('মোট গ্রন্থ', 'Total Books'); ?></p>
      </div>
    </div>

    <div class="stat-card">
      <div class="stat-icon-wrap amber">
        <i class="fa-solid fa-folder-tree"></i>
      </div>
      <div class="stat-content">
        <h3><?php echo toLangNum($totalChaptersGlobal); ?></h3>
        <p><?php echo __('মোট অধ্যায়', 'Total Chapters'); ?></p>
      </div>
    </div>

    <div class="stat-card">
      <div class="stat-icon-wrap blue">
        <i class="fa-solid fa-book-open"></i>
      </div>
      <div class="stat-content">
        <h3><?php echo toLangNum($totalHadithsGlobal); ?></h3>
        <p><?php echo __('মোট হাদিস', 'Total Hadiths'); ?></p>
      </div>
    </div>
  </div>

  <!-- Action Bar & Search -->
  <div class="card" style="margin-bottom: 24px;">
    <div class="card-body" style="padding: 18px 24px;">
      <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 16px;">
        
        <!-- Search Filter Form -->
        <form method="GET" action="hadith_books.php" style="display: flex; align-items: center; gap: 10px; flex: 1; max-width: 480px;">
          <div style="position: relative; width: 100%;">
            <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 14px; top: 50%; transform: translateY(-50%); color: var(--text-muted); font-size: 14px;"></i>
            <input type="text" name="search" class="form-control" style="padding-left: 38px;" placeholder="<?php echo __('গ্রন্থের নাম, লেখক বা স্লাগ দিয়ে খুঁজুন...', 'Search by book title, author or slug...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
          </div>
          <button type="submit" class="btn btn-secondary">
            <span><?php echo __('ফিল্টার', 'Filter'); ?></span>
          </button>
          <?php if (!empty($search)): ?>
            <a href="hadith_books.php" class="btn btn-secondary" title="<?php echo __('রিসেট', 'Reset'); ?>">
              <i class="fa-solid fa-rotate-left"></i>
            </a>
          <?php endif; ?>
        </form>

        <!-- Action Buttons -->
        <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
          <!-- CSV Export Button -->
          <a href="hadith_books.php?action=export_csv<?php echo !empty($search) ? '&search=' . urlencode($search) : ''; ?>" class="btn btn-secondary" style="font-weight: 700;" title="<?php echo __('সকল হাদিস গ্রন্থ CSV ফরম্যাটে ডাউনলোড করুন', 'Download all Hadith books in CSV format'); ?>">
            <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
            <span><?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?></span>
          </a>

          <!-- CSV Import Button -->
          <button type="button" class="btn btn-secondary" onclick="openImportBooksCsvModal()" style="font-weight: 700;" title="<?php echo __('CSV ফাইল থেকে হাদিস গ্রন্থ ডাটাবেজে ইমপোর্ট করুন', 'Import Hadith books from CSV'); ?>">
            <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
            <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
          </button>

          <!-- Add New Book Button -->
          <button type="button" class="btn btn-primary" onclick="openAddBookModal()">
            <i class="fa-solid fa-plus-circle"></i>
            <span><?php echo __('নতুন গ্রন্থ', 'New Book'); ?></span>
          </button>
        </div>

      </div>
    </div>
  </div>

  <!-- Books Grid -->
  <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(360px, 1fr)); gap: 22px;">
    <?php if (empty($books)): ?>
      <div class="card" style="grid-column: 1 / -1; text-align: center; padding: 48px 20px;">
        <div style="font-size: 48px; margin-bottom: 14px;">📚</div>
        <h3 style="font-size: 18px; font-weight: 700; color: var(--text-heading); margin-bottom: 6px;">কোনো হাদিস গ্রন্থ পাওয়া যায়নি</h3>
        <p style="font-size: 13.5px; color: var(--text-muted); margin-bottom: 20px;">নতুন হাদিস গ্রন্থ যুক্ত করতে উপরের বাটনে ক্লিক করুন।</p>
        <button type="button" class="btn btn-primary" onclick="openAddBookModal()">
          <i class="fa-solid fa-plus"></i> প্রথম গ্রন্থ যোগ করুন
        </button>
      </div>
    <?php else: ?>
      <?php foreach ($books as $b): ?>
        <div class="card hadith-book-interactive-card" 
             style="margin-bottom: 0; display: flex; flex-direction: column; justify-content: space-between; position: relative; border-top: 4px solid var(--primary); cursor: pointer; transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);"
             onclick="if (!event.target.closest('button, a, form, input, select')) { window.location.href='hadith_chapters.php?book=<?php echo urlencode($b['book_slug']); ?>'; }">
          
          <div class="card-body" style="padding: 24px;">
            
            <!-- Top Header & Badges -->
            <div style="display: flex; align-items: flex-start; justify-content: space-between; gap: 12px; margin-bottom: 14px;">
              <div style="display: flex; align-items: center; gap: 12px;">
                <div style="width: 48px; height: 48px; border-radius: 14px; background: rgba(16, 185, 129, 0.12); border: 1px solid rgba(16, 185, 129, 0.25); display: flex; align-items: center; justify-content: center; font-size: 22px; color: var(--primary); flex-shrink: 0;">
                  <i class="fa-solid fa-book-quran"></i>
                </div>
                <div>
                  <h3 style="font-size: 17.5px; font-weight: 800; color: var(--text-heading); line-height: 1.3; margin: 0;">
                    <?php echo htmlspecialchars(isEn() && !empty($b['name_en']) ? $b['name_en'] : $b['name_bn']); ?>
                  </h3>
                  <?php if (!isEn() && !empty($b['name_en'])): ?>
                    <span style="font-size: 12px; color: var(--text-muted); font-weight: 600; display: block; margin-top: 2px;">
                      <?php echo htmlspecialchars($b['name_en']); ?>
                    </span>
                  <?php endif; ?>
                </div>
              </div>

              <!-- Status Toggle Form -->
              <form method="POST" action="hadith_books.php" style="margin: 0;" onclick="event.stopPropagation();">
                <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                <input type="hidden" name="action" value="toggle_status">
                <input type="hidden" name="id" value="<?php echo $b['id']; ?>">
                <button type="submit" style="background: none; border: none; cursor: pointer; padding: 0;" title="<?php echo $b['is_active'] ? __('নিষ্ক্রিয় করতে ক্লিক করুন', 'Click to Deactivate') : __('সক্রিয় করতে ক্লিক করুন', 'Click to Activate'); ?>">
                  <?php if ($b['is_active']): ?>
                    <span class="badge badge-success"><i class="fa-solid fa-circle-check"></i> <?php echo __('সক্রিয়', 'Active'); ?></span>
                  <?php else: ?>
                    <span class="badge badge-secondary"><i class="fa-solid fa-eye-slash"></i> <?php echo __('লুকায়িত', 'Hidden'); ?></span>
                  <?php endif; ?>
                </button>
              </form>
            </div>

            <!-- Arabic Calligraphy Name -->
            <?php if (!empty($b['name_ar'])): ?>
              <div style="font-family: var(--font-arabic); font-size: 20px; color: var(--accent-gold); direction: rtl; text-align: right; margin-bottom: 12px; font-weight: 700; line-height: 1.4;">
                <?php echo htmlspecialchars($b['name_ar']); ?>
              </div>
            <?php endif; ?>

            <!-- Author & Slug -->
            <div style="font-size: 12.5px; color: var(--text-muted); margin-bottom: 16px; display: flex; flex-direction: column; gap: 5px;">
              <?php 
                $authorDisplay = isEn() && !empty($b['author_en']) ? $b['author_en'] : (!empty($b['author_bn']) ? $b['author_bn'] : '');
                if (!empty($authorDisplay)): 
              ?>
                <div style="display: flex; align-items: center;">
                  <i class="fa-solid fa-feather" style="color: var(--primary); margin-right: 7px; width: 14px;"></i>
                  <span><?php echo __('সংকলক: ', 'Compiler: '); ?><strong><?php echo htmlspecialchars($authorDisplay); ?></strong></span>
                </div>
              <?php endif; ?>
              <div style="display: flex; align-items: center;">
                <i class="fa-solid fa-tag" style="color: var(--text-dim); margin-right: 7px; width: 14px;"></i>
                <span><?php echo __('স্লাগ: ', 'Slug: '); ?><code style="background: var(--hover-bg); padding: 2px 7px; border-radius: 4px; font-size: 11.5px; color: var(--primary-dark); font-weight: 700;"><?php echo htmlspecialchars($b['book_slug']); ?></code></span>
              </div>
            </div>

            <!-- Description if available -->
            <?php if (!empty($b['description_bn'])): ?>
              <p style="font-size: 12.5px; color: var(--text-muted); line-height: 1.5; margin-bottom: 16px; background: var(--hover-bg); padding: 8px 12px; border-radius: 8px; border: 1px solid var(--border-color);">
                <?php echo htmlspecialchars(mb_strimwidth($b['description_bn'], 0, 110, '...')); ?>
              </p>
            <?php endif; ?>

            <!-- Live Counts Stats Badges -->
            <div style="display: flex; gap: 10px; margin-bottom: 8px;">
              <div style="flex: 1; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 10px; text-align: center;">
                <div style="font-size: 11px; color: var(--text-muted); font-weight: 700; text-transform: uppercase;"><?php echo __('অধ্যায় সংখ্যা', 'Chapters'); ?></div>
                <div style="font-size: 18px; font-weight: 800; color: var(--primary-dark); margin-top: 2px;">
                  <?php echo toLangNum($b['chapter_count']) . (isEn() ? '' : 'টি'); ?>
                </div>
              </div>
              <div style="flex: 1; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 10px; text-align: center;">
                <div style="font-size: 11px; color: var(--text-muted); font-weight: 700; text-transform: uppercase;"><?php echo __('হাদিস সংখ্যা', 'Hadiths'); ?></div>
                <div style="font-size: 18px; font-weight: 800; color: var(--accent-gold); margin-top: 2px;">
                  <?php echo toLangNum($b['real_hadith_count'] > 0 ? $b['real_hadith_count'] : $b['total_hadith']) . (isEn() ? '' : 'টি'); ?>
                </div>
              </div>
            </div>

          </div>

          <!-- Card Footer Action Buttons -->
          <div style="padding: 14px 20px; background: var(--hover-bg); border-top: 1px solid var(--border-color); display: flex; align-items: center; justify-content: space-between; gap: 10px;" onclick="event.stopPropagation();">
            
            <!-- Primary Action: Navigate to Level 2 (Chapters) -->
            <a href="hadith_chapters.php?book=<?php echo urlencode($b['book_slug']); ?>" class="btn btn-primary" style="flex: 1; padding: 8px 14px; font-size: 13px; display: inline-flex; align-items: center; justify-content: center; gap: 6px; white-space: nowrap;">
              <i class="fa-solid fa-folder-open"></i>
              <span><?php echo __('অধ্যায়সমূহ দেখুন', 'View Chapters') . ' (' . toLangNum($b['chapter_count']) . ')'; ?></span>
              <i class="fa-solid fa-arrow-right" style="font-size: 11px;"></i>
            </a>

            <!-- Edit Button -->
            <button type="button" class="btn btn-secondary btn-sm" title="<?php echo __('সম্পাদনা করুন', 'Edit Book'); ?>" 
                    style="padding: 8px 12px;"
                    data-book="<?php echo htmlspecialchars(json_encode($b, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP), ENT_QUOTES, 'UTF-8'); ?>" 
                    onclick="openEditBookModal(JSON.parse(this.dataset.book))">
              <i class="fa-solid fa-pen-to-square text-blue-500"></i>
            </button>

            <!-- Delete Button -->
            <a href="hadith_books.php?delete=<?php echo $b['id']; ?>&token=<?php echo generateCsrfToken(); ?>" class="btn btn-danger btn-sm" style="padding: 8px 12px;" title="<?php echo __('মুছে ফেলুন', 'Delete'); ?>" onclick="return confirmAction(event, this.href, '<?php echo __('আপনি কি নিশ্চিত যে এই গ্রন্থটি মুছে ফেলতে চান?', 'Are you sure you want to delete this book?'); ?>');">
              <i class="fa-solid fa-trash"></i>
            </a>

          </div>

        </div>
      <?php endforeach; ?>
    <?php endif; ?>
  </div>

</div>

<!-- ==============================================================================
     ADD / EDIT HADITH BOOK MODAL
     ============================================================================== -->
<div id="bookModal" class="modal-backdrop" style="display: none;">
  <div class="modal-window" style="max-width: 620px;">
    <div class="modal-header">
      <h3 class="modal-title" id="bookModalTitle">
        <i class="fa-solid fa-book-bookmark" style="color: var(--primary);"></i> নতুন হাদিস গ্রন্থ / ক্যাটাগরি
      </h3>
      <button type="button" class="btn-modal-close" onclick="closeModal('bookModal')">&times;</button>
    </div>

    <form method="POST" action="hadith_books.php">
      <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
      <input type="hidden" name="action" id="bookFormAction" value="create">
      <input type="hidden" name="id" id="bookId" value="0">

      <div class="modal-body">
        
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">গ্রন্থের বাংলা নাম <span style="color: var(--accent-red);">*</span></label>
            <input type="text" name="name_bn" id="bookNameBn" class="form-control" placeholder="যেমন: সহীহ বুখারী" required>
          </div>
          <div class="form-group">
            <label class="form-label">ইংরেজি নাম</label>
            <input type="text" name="name_en" id="bookNameEn" class="form-control" placeholder="যেমন: Sahih al-Bukhari" oninput="autoGenerateSlug(this.value)">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">আরবি নাম</label>
            <input type="text" name="name_ar" id="bookNameAr" class="form-control arabic-input" style="font-size: 17px; height: 46px;" placeholder="যেমন: صحيح البخاري">
          </div>
          <div class="form-group">
            <label class="form-label">ইউনিক স্লাগ <span style="color: var(--accent-red);">*</span></label>
            <input type="text" name="book_slug" id="bookSlug" class="form-control" placeholder="যেমন: bukhari" required>
            <span class="form-hint">শুধুমাত্র ছোট হাতের ইংরেজি ও আন্ডারস্কোর (bukhari, muslim, nasai ইত্যাদি)।</span>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('সংকলকের নাম', 'Author Name'); ?></label>
            <input type="text" name="author_bn" id="bookAuthorBn" class="form-control" placeholder="যেমন: ইমাম বুখারি (রহ.)">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('সংকলকের ইংরেজি নাম', 'Author English Name'); ?></label>
            <input type="text" name="author_en" id="bookAuthorEn" class="form-control" placeholder="যেমন: Imam Bukhari">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('মোট হাদিস সংখ্যা', 'Total Hadiths'); ?></label>
            <input type="number" name="total_hadith" id="bookTotalHadith" class="form-control" placeholder="যেমন: 7563" value="0">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('প্রদর্শনের ক্রম', 'Display Order'); ?></label>
            <input type="number" name="display_order" id="bookDisplayOrder" class="form-control" placeholder="যেমন: 1" value="0">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('বিবরণ', 'Description'); ?></label>
          <textarea name="description_bn" id="bookDescriptionBn" class="form-control" rows="3" placeholder="হাদিস গ্রন্থের গুরুত্ব, বৈশিষ্ট্য বা সংক্ষিপ্ত পরিচিতি লিখুন..."></textarea>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="switch-label">
            <div class="switch">
              <input type="checkbox" name="is_active" id="bookIsActive" value="1" checked>
              <span class="slider"></span>
            </div>
            <div>
              <strong style="color: var(--text-heading); font-size: 13.5px;"><?php echo __('সক্রিয় ও দৃশ্যমান রাখুন', 'Active and Visible'); ?></strong>
              <div style="font-size: 12px; color: var(--text-muted);"><?php echo __('অফ থাকলে অ্যাপে এই গ্রন্থটি প্রদর্শিত হবে না', 'Will be hidden in app if disabled'); ?></div>
            </div>
          </label>
        </div>

      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('bookModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i>
          <span id="bookSubmitBtnText"><?php echo __('সংরক্ষণ করুন', 'Save'); ?></span>
        </button>
      </div>

    </form>
  </div>
</div>

<script>
// Real-time live translations as user types (Zero button clicks needed)
document.addEventListener('DOMContentLoaded', function() {
  setupLiveTranslate('bookNameBn', 'bookNameEn', {
    onTranslate: function(tr) {
      if (document.getElementById('bookFormAction').value === 'create') {
        autoGenerateSlug(tr);
      }
    }
  });

  setupLiveTranslate('bookAuthorBn', 'bookAuthorEn');
});

function openAddBookModal() {
  document.getElementById('bookFormAction').value = 'create';
  document.getElementById('bookId').value = '0';
  document.getElementById('bookModalTitle').innerHTML = '<i class="fa-solid fa-book-bookmark" style="color: var(--primary);"></i> <?php echo __('নতুন গ্রন্থ', 'New Book'); ?>';
  document.getElementById('bookSubmitBtnText').innerText = '<?php echo __('সংরক্ষণ করুন', 'Save'); ?>';

  document.getElementById('bookNameBn').value = '';
  document.getElementById('bookNameEn').value = '';
  document.getElementById('bookNameAr').value = '';
  document.getElementById('bookSlug').value = '';
  document.getElementById('bookSlug').readOnly = false;
  document.getElementById('bookAuthorBn').value = '';
  document.getElementById('bookAuthorEn').value = '';
  document.getElementById('bookTotalHadith').value = '0';
  document.getElementById('bookDisplayOrder').value = '<?php echo count($books) + 1; ?>';
  document.getElementById('bookDescriptionBn').value = '';
  document.getElementById('bookIsActive').checked = true;

  openModal('bookModal');
}

function openEditBookModal(book) {
  document.getElementById('bookFormAction').value = 'update';
  document.getElementById('bookId').value = book.id;
  document.getElementById('bookModalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square" style="color: var(--primary);"></i> <?php echo __('গ্রন্থ সম্পাদনা', 'Edit Book'); ?>';
  document.getElementById('bookSubmitBtnText').innerText = '<?php echo __('সংরক্ষণ করুন', 'Save'); ?>';

  document.getElementById('bookNameBn').value = book.name_bn || '';
  document.getElementById('bookNameEn').value = book.name_en || '';
  document.getElementById('bookNameAr').value = book.name_ar || '';
  document.getElementById('bookSlug').value = book.book_slug || '';
  document.getElementById('bookSlug').readOnly = true; // Protect slug on edit
  document.getElementById('bookAuthorBn').value = book.author_bn || '';
  document.getElementById('bookAuthorEn').value = book.author_en || '';
  document.getElementById('bookTotalHadith').value = book.total_hadith || 0;
  document.getElementById('bookDisplayOrder').value = book.display_order || 0;
  document.getElementById('bookDescriptionBn').value = book.description_bn || '';
  document.getElementById('bookIsActive').checked = (parseInt(book.is_active) === 1);

  openModal('bookModal');
}

function autoGenerateSlug(text) {
  if (document.getElementById('bookFormAction').value === 'create') {
    const slugInput = document.getElementById('bookSlug');
    if (!slugInput.dataset.manualEdited) {
      let cleanText = text.toLowerCase()
        .replace(/^(sahih\s+al-|sahih\s+)/i, '')
        .replace(/^(sunan\s+an-|sunan\s+at-|sunan\s+abi\s+|sunan\s+)/i, '')
        .replace(/^(jami\s+at-|jami\s+)/i, '');
      if (!cleanText.trim()) cleanText = text.toLowerCase();
      const generated = cleanText
        .replace(/[^a-z0-9]/g, '_')
        .replace(/_+/g, '_')
        .replace(/^_|_$/g, '');
      slugInput.value = generated || 'hadith_book';
    }
  }
}

document.getElementById('bookSlug').addEventListener('input', function() {
  this.dataset.manualEdited = 'true';
});

function openImportBooksCsvModal() {
  document.getElementById('importBooksCsvModal').style.display = 'flex';
}
function closeImportBooksCsvModal() {
  document.getElementById('importBooksCsvModal').style.display = 'none';
}
</script>

<!-- Modal: Import Hadith Books CSV -->
<div id="importBooksCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 520px; box-shadow: var(--shadow-lg);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?php echo __('হাদিস গ্রন্থ CSV ডাটাবেজ ইমপোর্ট', 'Import Hadith Books from CSV'); ?></span>
      </h3>
      <button type="button" onclick="closeImportBooksCsvModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 18px; line-height: 1.5;">
      <?php echo __('একটি বৈধ CSV ফাইল আপলোড করে সকল হাদিস গ্রন্থ ও ক্যাটাগরি এক ক্লিকে MySQL ডাটাবেজে যুক্ত বা আপডেট করুন।', 'Upload a valid CSV file to bulk import or update hadith books in MySQL database.'); ?>
    </p>

    <form method="POST" action="hadith_books.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?php echo __('CSV ফাইল নির্বাচন করুন (.csv)', 'Select CSV File (.csv)'); ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?php echo __('সাপোর্টেড কলাম বিন্যাস:', 'Supported Column Format:'); ?></strong><br>
        <code>ID, Book_Slug, Name_BN, Name_EN, Name_AR, Author_BN, Author_EN, Initials, Color_Hex, Total_Hadith, Total_Hadith_BN, Description_BN, Display_Order, Is_Active</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportBooksCsvModal()" style="font-weight: 600;">
          <?php echo __('বাতিল', 'Cancel'); ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?php echo __('ইমপোর্ট ও সিঙ্ক করুন', 'Import & Sync'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
