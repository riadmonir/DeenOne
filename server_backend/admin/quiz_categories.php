<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - QUIZ & KNOWLEDGE BATTLE CATEGORIES (LEVEL 1)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Auto ensure schema columns exist
try {
    $cols = $pdo->query("SHOW COLUMNS FROM quiz_categories")->fetchAll(PDO::FETCH_COLUMN);
    if (!in_array('tag_bn', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `tag_bn` VARCHAR(100) DEFAULT 'সাধারণ জ্ঞান' AFTER `category_id`");
    }
    if (!in_array('tag_en', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `tag_en` VARCHAR(100) DEFAULT 'General Knowledge' AFTER `tag_bn`");
    }
    if (!in_array('title_en', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `title_en` VARCHAR(100) DEFAULT NULL AFTER `title_bn`");
    }
    if (!in_array('description_en', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `description_en` TEXT DEFAULT NULL AFTER `description_bn`");
    }
    if (!in_array('total_questions', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `total_questions` INT UNSIGNED DEFAULT 5 AFTER `description_en`");
    }
    if (!in_array('duration_minutes', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `duration_minutes` INT UNSIGNED DEFAULT 10 AFTER `total_questions`");
    }
    if (!in_array('points_per_question', $cols)) {
        $pdo->exec("ALTER TABLE quiz_categories ADD COLUMN `points_per_question` INT UNSIGNED DEFAULT 10 AFTER `duration_minutes`");
    }
} catch (Exception $ignored) {}

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $stmt = $pdo->query("SELECT id, category_id, tag_bn, tag_en, title_bn, title_en, description_bn, description_en, total_questions, duration_minutes, points_per_question, icon, display_order, is_active FROM quiz_categories ORDER BY display_order ASC, id ASC");
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_quiz_categories_' . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, ['ID', 'Category_ID', 'Tag_BN', 'Tag_EN', 'Title_BN', 'Title_EN', 'Description_BN', 'Description_EN', 'Total_Questions', 'Duration_Mins', 'Points_Per_Q', 'Icon', 'Display_Order', 'Is_Active']);

    foreach ($rows as $r) {
        fputcsv($out, [
            $r['id'] ?? '',
            $r['category_id'] ?? '',
            $r['tag_bn'] ?? 'সাধারণ জ্ঞান',
            $r['tag_en'] ?? 'General Knowledge',
            $r['title_bn'] ?? '',
            $r['title_en'] ?? '',
            $r['description_bn'] ?? '',
            $r['description_en'] ?? '',
            $r['total_questions'] ?? 5,
            $r['duration_minutes'] ?? 10,
            $r['points_per_question'] ?? 10,
            $r['icon'] ?? 'ic_quiz_general',
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
        redirect('quiz_categories.php');
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
            $catId = preg_replace('/[^a-z0-9_]/', '', strtolower(trim($row[1] ?? '')));
            $tagBn = trim($row[2] ?? 'সাধারণ জ্ঞান');
            $tagEn = trim($row[3] ?? 'General Knowledge');
            $titleBn = trim($row[4] ?? '');
            $titleEn = trim($row[5] ?? '');
            $descBn = trim($row[6] ?? '');
            $descEn = trim($row[7] ?? '');
            $totalQ = (int)($row[8] ?? 5);
            $durationMins = (int)($row[9] ?? 10);
            $pointsPerQ = (int)($row[10] ?? 10);
            $icon = trim($row[11] ?? 'ic_quiz_general');
            $order = (int)($row[12] ?? 0);
            $isActive = isset($row[13]) ? (int)$row[13] : 1;

            if (empty($catId) || empty($titleBn)) continue;

            $checkStmt = $pdo->prepare("SELECT id FROM quiz_categories WHERE category_id = ? OR (id > 0 AND id = ?)");
            $checkStmt->execute([$catId, $id]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $uStmt = $pdo->prepare("UPDATE quiz_categories SET category_id = ?, tag_bn = ?, tag_en = ?, title_bn = ?, title_en = ?, description_bn = ?, description_en = ?, total_questions = ?, duration_minutes = ?, points_per_question = ?, icon = ?, display_order = ?, is_active = ? WHERE id = ?");
                $uStmt->execute([$catId, $tagBn, $tagEn, $titleBn, $titleEn, $descBn, $descEn, $totalQ, $durationMins, $pointsPerQ, $icon, $order, $isActive, $existing['id']]);
                $updatedCount++;
            } else {
                $iStmt = $pdo->prepare("INSERT INTO quiz_categories (category_id, tag_bn, tag_en, title_bn, title_en, description_bn, description_en, total_questions, duration_minutes, points_per_question, icon, display_order, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $iStmt->execute([$catId, $tagBn, $tagEn, $titleBn, $titleEn, $descBn, $descEn, $totalQ, $durationMins, $pointsPerQ, $icon, $order, $isActive]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "কুইজ ক্যাটাগরি CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " টি নতুন যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " টি আপডেট হয়েছে।";
        $msgEn = "Quiz Categories CSV Import Completed: " . toLangNum($importedCount) . " new added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_QUIZ_CATEGORIES', 'quiz_categories', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('quiz_categories.php');
}

// Handle Action (Add, Edit, Delete)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর।');
        redirect('quiz_categories.php');
    }

    if ($action === 'create' || $action === 'update') {
        $categoryId = preg_replace('/[^a-z0-9_]/', '', strtolower(trim($_POST['category_id'] ?? '')));
        $tagBn = trim($_POST['tag_bn'] ?? 'সাধারণ জ্ঞান');
        $tagEn = trim($_POST['tag_en'] ?? 'General Knowledge');
        $titleBn = trim($_POST['title_bn'] ?? '');
        $titleEn = trim($_POST['title_en'] ?? '');
        $descBn = trim($_POST['description_bn'] ?? '');
        $descEn = trim($_POST['description_en'] ?? '');
        $totalQ = max(1, (int)($_POST['total_questions'] ?? 5));
        $durationMins = max(1, (int)($_POST['duration_minutes'] ?? 10));
        $pointsPerQ = max(1, (int)($_POST['points_per_question'] ?? 10));
        $icon = trim($_POST['icon'] ?? 'ic_quiz_general');
        $order = (int)($_POST['display_order'] ?? 0);
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($categoryId) || empty($titleBn)) {
            setFlash('danger', 'ক্যাটাগরি আইডি (ইংরেজি স্লাগ) এবং বাংলা নাম উভয়ই আবশ্যক।');
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO quiz_categories (category_id, tag_bn, tag_en, title_bn, title_en, description_bn, description_en, total_questions, duration_minutes, points_per_question, icon, display_order, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$categoryId, $tagBn, $tagEn, $titleBn, $titleEn, $descBn, $descEn, $totalQ, $durationMins, $pointsPerQ, $icon, $order, $isActive]);
                    logAdminAction($pdo, 'CREATE_QUIZ_CAT', 'quiz_categories', $categoryId, 'নতুন ক্যাটাগরি: ' . $titleBn);
                    setFlash('success', 'নতুন কুইজ ক্যাটাগরি সফলভাবে যুক্ত হয়েছে!');
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE quiz_categories SET category_id = ?, tag_bn = ?, tag_en = ?, title_bn = ?, title_en = ?, description_bn = ?, description_en = ?, total_questions = ?, duration_minutes = ?, points_per_question = ?, icon = ?, display_order = ?, is_active = ? WHERE id = ?");
                    $stmt->execute([$categoryId, $tagBn, $tagEn, $titleBn, $titleEn, $descBn, $descEn, $totalQ, $durationMins, $pointsPerQ, $icon, $order, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_QUIZ_CAT', 'quiz_categories', (string)$id, 'ক্যাটাগরি আপডেট: ' . $titleBn);
                    setFlash('success', 'ক্যাটাগরি সফলভাবে আপডেট করা হয়েছে!');
                }
            } catch (Exception $e) {
                setFlash('danger', 'অপারেশন ব্যর্থ: ' . $e->getMessage());
            }
        }
    }
    redirect('quiz_categories.php');
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM quiz_categories WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_QUIZ_CAT', 'quiz_categories', (string)$id, 'ক্যাটাগরি মুছে ফেলা হয়েছে');
        setFlash('success', 'ক্যাটাগরি সফলভাবে মুছে ফেলা হয়েছে।');
    } catch (Exception $e) {
        setFlash('danger', 'মুছে ফেলতে ব্যর্থ: ' . $e->getMessage());
    }
    redirect('quiz_categories.php');
}

if (isset($_GET['toggle'])) {
    $id = (int)$_GET['toggle'];
    try {
        $stmt = $pdo->prepare("UPDATE quiz_categories SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        setFlash('success', 'ক্যাটাগরি সক্রিয়তা স্ট্যাটাস পরিবর্তন করা হয়েছে।');
    } catch (Exception $e) {}
    redirect('quiz_categories.php');
}

// Fetch categories with question counts
$categories = [];
$totalQuestionsAll = 0;
$activeCatCount = 0;
try {
    $sql = "SELECT qc.*, COUNT(qq.id) as question_count 
            FROM quiz_categories qc 
            LEFT JOIN quiz_questions qq ON qc.category_id = qq.category_id 
            GROUP BY qc.id 
            ORDER BY qc.display_order ASC, qc.id ASC";
    $categories = $pdo->query($sql)->fetchAll();
    
    foreach ($categories as $cat) {
        $totalQuestionsAll += (int)$cat['question_count'];
        if (!empty($cat['is_active'])) {
            $activeCatCount++;
        }
    }
} catch (Exception $e) {}

$pageTitle = __('কুইজ ক্যাটাগরি', 'Quiz Categories');
$activeNav = 'quiz_categories';
require_once __DIR__ . '/header.php';
?>

<!-- Level Indicator & Quick Stats -->
<div class="mb-5">
  <div class="flex items-center justify-between flex-wrap gap-4 mb-4">
    <div>
      <h1 class="text-2xl font-black text-slate-900 dark:text-white tracking-tight">
        <?php echo __('কুইজ ক্যাটাগরি', 'Quiz Categories'); ?>
      </h1>
    </div>
    <div class="flex items-center gap-2 flex-wrap">
      <!-- CSV Export Button -->
      <a href="quiz_categories.php?action=export_csv" class="btn btn-secondary btn-sm" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?>">
        <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
        <span><?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?></span>
      </a>

      <!-- CSV Import Button -->
      <button type="button" class="btn btn-secondary btn-sm" onclick="openImportCategoriesCsvModal()" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?>">
        <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
        <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
      </button>

      <a href="quiz_questions.php" class="btn btn-secondary btn-sm">
        <i class="fa-solid fa-circle-question"></i>
        <span><?php echo __('সকল প্রশ্ন', 'All Questions') . ' (' . toLangNum($totalQuestionsAll) . ')'; ?></span>
      </a>
      <button type="button" class="btn btn-primary btn-sm shadow-md hover:shadow-lg transition-all" onclick="openAddCategoryModal()">
        <i class="fa-solid fa-plus-circle"></i>
        <span><?php echo __('নতুন ক্যাটাগরি', 'New Category'); ?></span>
      </button>
    </div>
  </div>

  <!-- KPI Cards -->
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
    <div class="card p-4 flex items-center gap-3 border-l-4 border-emerald-500">
      <div class="w-12 h-12 rounded-xl bg-emerald-100 dark:bg-emerald-950 flex items-center justify-center text-emerald-600 dark:text-emerald-400 text-xl font-bold">
        <i class="fa-solid fa-layer-group"></i>
      </div>
      <div>
        <div class="text-xs font-semibold text-slate-500 dark:text-slate-400"><?php echo __('মোট ক্যাটাগরি', 'Total Categories'); ?></div>
        <div class="text-2xl font-black text-slate-900 dark:text-white"><?php echo toLangNum(count($categories)) . (isEn() ? '' : ' টি'); ?></div>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-3 border-l-4 border-teal-500">
      <div class="w-12 h-12 rounded-xl bg-teal-100 dark:bg-teal-950 flex items-center justify-center text-teal-600 dark:text-teal-400 text-xl font-bold">
        <i class="fa-solid fa-circle-check"></i>
      </div>
      <div>
        <div class="text-xs font-semibold text-slate-500 dark:text-slate-400"><?php echo __('সক্রিয় ক্যাটাগরি', 'Active Categories'); ?></div>
        <div class="text-2xl font-black text-slate-900 dark:text-white"><?php echo toLangNum($activeCatCount) . (isEn() ? '' : ' টি'); ?></div>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-3 border-l-4 border-amber-500">
      <div class="w-12 h-12 rounded-xl bg-amber-100 dark:bg-amber-950 flex items-center justify-center text-amber-600 dark:text-amber-400 text-xl font-bold">
        <i class="fa-solid fa-circle-question"></i>
      </div>
      <div>
        <div class="text-xs font-semibold text-slate-500 dark:text-slate-400"><?php echo __('সংরক্ষিত প্রশ্ন', 'Saved Questions'); ?></div>
        <div class="text-2xl font-black text-slate-900 dark:text-white"><?php echo toLangNum($totalQuestionsAll) . (isEn() ? '' : ' টি'); ?></div>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-3 border-l-4 border-indigo-500">
      <div class="w-12 h-12 rounded-xl bg-indigo-100 dark:bg-indigo-950 flex items-center justify-center text-indigo-600 dark:text-indigo-400 text-xl font-bold">
        <i class="fa-solid fa-shield-halved"></i>
      </div>
      <div>
        <div class="text-xs font-semibold text-slate-500 dark:text-slate-400"><?php echo __('ডাটাবেজ সংযোগ', 'Database Connection'); ?></div>
        <?php
          $isDbOnline = false;
          try {
              if (isset($pdo) && $pdo) {
                  $pdo->query("SELECT 1");
                  $isDbOnline = true;
              }
          } catch(Throwable $t) {
              $isDbOnline = false;
          }
        ?>
        <div class="text-2xl font-black <?php echo $isDbOnline ? 'text-emerald-600 dark:text-emerald-400' : 'text-rose-600 dark:text-rose-400'; ?> flex items-center gap-2">
          <span class="inline-block w-2.5 h-2.5 rounded-full <?php echo $isDbOnline ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500'; ?>"></span>
          <span><?php echo $isDbOnline ? __('সক্রিয়', 'Connected') : __('অফলাইন', 'Offline'); ?></span>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Main Categories Table -->
<div class="card shadow-sm">
  <div class="card-header flex items-center justify-between flex-wrap gap-2">
    <div class="card-title flex items-center gap-2">
      <i class="fa-solid fa-list-check text-emerald-500"></i>
      <span><?php echo __('ক্যাটাগরি তালিকা', 'Categories'); ?> <span class="badge badge-info ms-2"><?php echo toLangNum(count($categories)); ?></span></span>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 60px;" class="text-center"><?php echo __('ক্রম', 'Order'); ?></th>
          <th><?php echo __('ক্যাটাগরি', 'Category'); ?></th>
          <th><?php echo __('স্লাগ', 'Slug'); ?></th>
          <th class="text-center" style="white-space: nowrap;"><?php echo __('প্রশ্ন', 'Questions'); ?></th>
          <th class="text-center" style="white-space: nowrap;"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th style="text-align: right; white-space: nowrap;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($categories)): ?>
          <tr>
            <td colspan="6" class="text-center py-12 text-slate-400">
              <div class="text-4xl mb-3">📂</div>
              <div class="font-bold text-base text-slate-600 dark:text-slate-300"><?php echo __('কোনো কুইজ ক্যাটাগরি পাওয়া যায়নি', 'No Quiz Categories Found'); ?></div>
              <p class="text-sm mt-1"><?php echo __('উপরের বাটনে ক্লিক করে নতুন ক্যাটাগরি যুক্ত করুন।', 'Click the button above to add a new category.'); ?></p>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($categories as $c): ?>
            <tr class="hover:bg-emerald-50/40 dark:hover:bg-slate-800/40 transition-colors">
              <td class="text-center">
                <span class="inline-flex items-center justify-center w-8 h-8 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 font-extrabold text-xs border border-slate-200 dark:border-slate-700">
                  #<?php echo toLangNum($c['display_order']); ?>
                </span>
              </td>
              <td>
                <div class="mb-1">
                  <span class="inline-block px-2 py-0.5 rounded text-[11px] font-bold bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-400">
                    <?php echo htmlspecialchars($c['tag_bn'] ?? 'সাধারণ জ্ঞান'); ?>
                  </span>
                </div>
                <a href="quiz_questions.php?category=<?php echo urlencode($c['category_id']); ?>" 
                   class="group block no-underline" title="<?php echo __('এই ক্যাটাগরির প্রশ্নমালা ওপেন করুন', 'Open this category questions'); ?>">
                  <div class="font-bold text-slate-900 dark:text-white text-base group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors flex items-center gap-2">
                    <span class="hover:underline"><?php echo htmlspecialchars($c['title_bn']); ?></span>
                    <i class="fa-solid fa-arrow-right text-xs text-emerald-500 opacity-60 group-hover:opacity-100 group-hover:translate-x-1 transition-all"></i>
                  </div>
                  <?php if (!empty($c['description_bn'])): ?>
                    <div class="text-xs text-slate-500 dark:text-slate-400 mt-0.5"><?php echo htmlspecialchars($c['description_bn']); ?></div>
                  <?php endif; ?>
                </a>
              </td>
              <td>
                <code class="px-2 py-0.5 rounded bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 text-xs font-mono border border-slate-200 dark:border-slate-700">
                  <?php echo htmlspecialchars($c['category_id']); ?>
                </code>
              </td>
              <td class="text-center" style="white-space: nowrap;">
                <a href="quiz_questions.php?category=<?php echo urlencode($c['category_id']); ?>" 
                   class="badge badge-info hover:scale-105 transition-transform" 
                   style="white-space: nowrap; display: inline-flex; align-items: center; gap: 5px; padding: 4px 10px; font-size: 12px; font-weight: 700; text-decoration: none;"
                   title="<?php echo __('এই ক্যাটাগরির প্রশ্নমালা ফিল্টার করুন', 'Filter questions'); ?>">
                  <i class="fa-solid fa-circle-question"></i>
                  <span style="white-space: nowrap;"><?php echo toLangNum($c['question_count']) . (isEn() ? ' Questions' : ' টি প্রশ্ন'); ?></span>
                </a>
              </td>
              <td class="text-center" style="white-space: nowrap;">
                <a href="quiz_categories.php?toggle=<?php echo $c['id']; ?>" 
                   class="badge <?php echo $c['is_active'] ? 'badge-success' : 'badge-secondary'; ?> transition-transform hover:scale-105" 
                   style="text-decoration: none; white-space: nowrap;" title="<?php echo __('স্ট্যাটাস পরিবর্তন করতে ক্লিক করুন', 'Toggle status'); ?>">
                  <?php if ($c['is_active']): ?>
                    <i class="fa-solid fa-circle-check"></i> <span><?php echo __('সক্রিয়', 'Active'); ?></span>
                  <?php else: ?>
                    <i class="fa-solid fa-circle-xmark"></i> <span><?php echo __('নিষ্ক্রিয়', 'Inactive'); ?></span>
                  <?php endif; ?>
                </a>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <div class="flex items-center justify-end gap-1.5" style="white-space: nowrap;">
                  <button type="button" class="btn btn-secondary btn-sm" 
                          data-item="<?php echo htmlspecialchars(json_encode($c, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP), ENT_QUOTES, 'UTF-8'); ?>"
                          onclick="editCategory(JSON.parse(this.dataset.item))" 
                          title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
                    <i class="fa-solid fa-pen-to-square text-blue-500"></i>
                    <span><?php echo __('এডিট', 'Edit'); ?></span>
                  </button>
                  <a href="quiz_categories.php?delete=<?php echo $c['id']; ?>" 
                     class="btn btn-danger btn-sm" title="<?php echo __('মুছুন', 'Delete'); ?>" 
                     onclick="return confirmAction(event, this.href, '<?php echo __('আপনি কি নিশ্চিতভাবে এই ক্যাটাগরিটি মুছে ফেলতে চান?', 'Are you sure you want to delete this category?'); ?>');">
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

<!-- Modal Add/Edit Category -->
<div class="modal-backdrop" id="addCategoryModal">
  <div class="modal-window" style="max-width: 650px;">
    <div class="modal-header">
      <div class="modal-title" id="catModalTitle">
        <i class="fa-solid fa-layer-group text-emerald-500 mr-2"></i>
        <span><?php echo __('নতুন ক্যাটাগরি', 'New Category'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('addCategoryModal')">&times;</button>
    </div>
    <form method="POST" action="quiz_categories.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="catAction" value="create">
      <input type="hidden" name="id" id="catId" value="">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('বাংলা ট্যাগ', 'Bengali Tag'); ?></label>
            <input type="text" name="tag_bn" id="inputCatTagBn" class="form-control" placeholder="যেমন: সাধারণ জ্ঞান" value="সাধারণ জ্ঞান">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইংরেজি ট্যাগ', 'English Tag'); ?></label>
            <input type="text" name="tag_en" id="inputCatTagEn" class="form-control" placeholder="e.g. General Knowledge" value="General Knowledge">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('বাংলা নাম', 'Bengali Name'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="title_bn" id="inputCatTitle" class="form-control" placeholder="যেমন: সাধারণ জ্ঞান / ইবাদত" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('স্লাগ', 'Slug'); ?> <span class="text-rose-500">*</span></label>
            <input type="text" name="category_id" id="inputCatSlug" class="form-control font-mono" placeholder="যেমন: general_knowledge / ibadah" required>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('সংক্ষিপ্ত বিবরণ', 'Description'); ?></label>
          <textarea name="description_bn" id="inputCatDesc" class="form-control" rows="2" placeholder="ক্যাটাগরির সংক্ষিপ্ত বিবরণ লিখুন..."></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('ক্রম', 'Order'); ?></label>
            <input type="number" name="display_order" id="inputCatOrder" class="form-control" value="<?php echo count($categories) + 1; ?>">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('আইকন', 'Icon'); ?></label>
            <input type="text" name="icon" id="inputCatIcon" class="form-control font-mono" value="ic_quiz_general">
          </div>
        </div>

        <div class="form-group mt-3">
          <label class="switch-label">
            <label class="switch">
              <input type="checkbox" name="is_active" id="inputCatActive" value="1" checked>
              <span class="slider"></span>
            </label>
            <span style="font-weight: 600;" class="text-sm"><?php echo __('সক্রিয় রাখুন', 'Keep Active'); ?></span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('addCategoryModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary" id="btnCatSubmit">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('সংরক্ষণ করুন', 'Save'); ?></span>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
function openAddCategoryModal() {
  document.getElementById('catModalTitle').innerHTML = '<i class="fa-solid fa-layer-group text-emerald-500 mr-2"></i><span><?php echo __('নতুন ক্যাটাগরি', 'New Category'); ?></span>';
  document.getElementById('catAction').value = 'create';
  document.getElementById('catId').value = '';
  document.getElementById('inputCatTagBn').value = 'সাধারণ জ্ঞান';
  document.getElementById('inputCatTagEn').value = 'General Knowledge';
  document.getElementById('inputCatTitle').value = '';
  document.getElementById('inputCatSlug').value = '';
  document.getElementById('inputCatDesc').value = '';
  document.getElementById('inputCatOrder').value = '<?php echo count($categories) + 1; ?>';
  document.getElementById('inputCatIcon').value = 'ic_quiz_general';
  document.getElementById('inputCatActive').checked = true;
  document.getElementById('btnCatSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span><?php echo __('সংরক্ষণ করুন', 'Save'); ?></span>';
  openModal('addCategoryModal');
}

function editCategory(cat) {
  document.getElementById('catModalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square text-emerald-500 mr-2"></i><span><?php echo __('ক্যাটাগরি সম্পাদনা', 'Edit Category'); ?></span>';
  document.getElementById('catAction').value = 'update';
  document.getElementById('catId').value = cat.id;
  document.getElementById('inputCatTagBn').value = cat.tag_bn || 'সাধারণ জ্ঞান';
  document.getElementById('inputCatTagEn').value = cat.tag_en || 'General Knowledge';
  document.getElementById('inputCatTitle').value = cat.title_bn;
  document.getElementById('inputCatSlug').value = cat.category_id;
  document.getElementById('inputCatDesc').value = cat.description_bn || '';
  document.getElementById('inputCatOrder').value = cat.display_order;
  document.getElementById('inputCatIcon').value = cat.icon || 'ic_quiz_general';
  document.getElementById('inputCatActive').checked = parseInt(cat.is_active) === 1;
  document.getElementById('btnCatSubmit').innerHTML = '<i class="fa-solid fa-floppy-disk"></i> <span><?php echo __('সংরক্ষণ করুন', 'Save'); ?></span>';
  openModal('addCategoryModal');
}

function openImportCategoriesCsvModal() {
  document.getElementById('importCategoriesCsvModal').style.display = 'flex';
}
function closeImportCategoriesCsvModal() {
  document.getElementById('importCategoriesCsvModal').style.display = 'none';
}
</script>

<!-- Modal: Import Quiz Categories CSV -->
<div id="importCategoriesCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 540px; box-shadow: var(--shadow-lg);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?php echo __('ক্যাটাগরি CSV ইমপোর্ট', 'Import Categories CSV'); ?></span>
      </h3>
      <button type="button" onclick="closeImportCategoriesCsvModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <form method="POST" action="quiz_categories.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?php echo __('CSV ফাইল নির্বাচন করুন', 'Select CSV File'); ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?php echo __('কলাম বিন্যাস:', 'Column Format:'); ?></strong><br>
        <code>ID, Category_ID, Tag_BN, Tag_EN, Title_BN, Title_EN, Description_BN, Description_EN, Total_Questions, Duration_Mins, Points_Per_Q, Icon, Display_Order, Is_Active</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportCategoriesCsvModal()" style="font-weight: 600;">
          <?php echo __('বাতিল', 'Cancel'); ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?php echo __('ইমপোর্ট করুন', 'Import'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
