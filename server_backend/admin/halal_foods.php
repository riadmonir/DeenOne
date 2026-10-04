<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - HALAL & HARAM FOODS ENCYCLOPEDIA (হালাল খাদ্য ও উপাদান ব্যবস্থাপনা)
 * 100% Verbatim, Dual-Language (BN/EN/AR), Light/Dark Mode Native, File Upload & Auto-Translate
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();
require_once __DIR__ . '/../data/seed_halal_foods.php';

// Ensure table exists with all dual-language columns
$pdo->exec("CREATE TABLE IF NOT EXISTS `halal_foods` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `item_key` VARCHAR(80) NOT NULL UNIQUE,
  `title` VARCHAR(191) NOT NULL,
  `title_en` VARCHAR(191) DEFAULT NULL,
  `arabic_name` VARCHAR(191) DEFAULT NULL,
  `category` VARCHAR(50) NOT NULL DEFAULT 'DAILY_FOOD',
  `status` VARCHAR(20) NOT NULL DEFAULT 'HALAL',
  `scientific_name` VARCHAR(191) DEFAULT NULL,
  `scientific_name_en` VARCHAR(191) DEFAULT NULL,
  `source_origin` TEXT DEFAULT NULL,
  `source_origin_en` TEXT DEFAULT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `nutrition_benefits` TEXT DEFAULT NULL,
  `nutrition_benefits_en` TEXT DEFAULT NULL,
  `usage_instructions` TEXT DEFAULT NULL,
  `usage_instructions_en` TEXT DEFAULT NULL,
  `description` TEXT NOT NULL,
  `description_en` TEXT DEFAULT NULL,
  `hadith_ref` TEXT DEFAULT NULL,
  `hadith_ref_en` TEXT DEFAULT NULL,
  `fiqh_ruling` TEXT DEFAULT NULL,
  `fiqh_ruling_en` TEXT DEFAULT NULL,
  `halal_alternative` TEXT DEFAULT NULL,
  `halal_alternative_en` TEXT DEFAULT NULL,
  `is_active` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_status` (`status`),
  INDEX `idx_category` (`category`),
  INDEX `idx_title` (`title`),
  INDEX `idx_title_en` (`title_en`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Ensure clean seed data is synced
$countCurrent = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1")->fetchColumn();
if ($countCurrent < 15) {
    seedComprehensiveHalalFoods($pdo);
}

// Server-Side Quick Neural Translation Helper
function quickServerTranslate($text, $sl = 'bn', $tl = 'en') {
    if (empty($text)) return '';
    $url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=' . urlencode($sl) . '&tl=' . urlencode($tl) . '&q=' . urlencode($text);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 4);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');
    $res = curl_exec($ch);
    $code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    if ($code === 200 && !empty($res)) {
        $json = json_decode($res, true);
        if (is_array($json) && !empty($json[0])) {
            return is_array($json[0]) ? implode(' ', $json[0]) : (string)$json[0];
        }
    }
    return '';
}

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = trim($_POST['action'] ?? '');
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('halal_foods.php');
    }

    if ($action === 'create' || $action === 'update') {
        $id = (int)($_POST['id'] ?? 0);
        $title = trim($_POST['title'] ?? '');
        $category = trim($_POST['category'] ?? 'DAILY_FOOD');
        $status = trim($_POST['status'] ?? 'HALAL');
        $sciName = trim($_POST['scientific_name'] ?? '');
        $sourceOrigin = trim($_POST['source_origin'] ?? '');
        $imageUrl = trim($_POST['image_url'] ?? '');
        $benefits = trim($_POST['nutrition_benefits'] ?? '');
        $usage = trim($_POST['usage_instructions'] ?? '');
        $desc = trim($_POST['description'] ?? '');
        $hadith = trim($_POST['hadith_ref'] ?? '');
        $fiqh = trim($_POST['fiqh_ruling'] ?? '');
        $alt = trim($_POST['halal_alternative'] ?? '');

        // Handle Image File Upload if provided
        if (isset($_FILES['image_file']) && $_FILES['image_file']['error'] === UPLOAD_ERR_OK) {
            $uploadDir = __DIR__ . '/../uploads/halal_foods/';
            if (!is_dir($uploadDir)) {
                @mkdir($uploadDir, 0755, true);
            }
            $fileTmp = $_FILES['image_file']['tmp_name'];
            $fileName = $_FILES['image_file']['name'];
            $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
            $allowedExts = ['jpg', 'jpeg', 'png', 'webp', 'gif'];

            if (in_array($fileExt, $allowedExts)) {
                $newFileName = 'food_' . time() . '_' . rand(1000, 9999) . '.' . $fileExt;
                $destPath = $uploadDir . $newFileName;
                if (move_uploaded_file($fileTmp, $destPath)) {
                    $imageUrl = 'uploads/halal_foods/' . $newFileName;
                }
            }
        }

        // Automatic Backend Dual-Language & Arabic Translation Generation
        $titleEn = quickServerTranslate($title, 'bn', 'en');
        $arabicName = quickServerTranslate($title, 'bn', 'ar');
        $sciNameEn = $sciName;
        $sourceOriginEn = !empty($sourceOrigin) ? quickServerTranslate($sourceOrigin, 'bn', 'en') : '';
        $descEn = !empty($desc) ? quickServerTranslate($desc, 'bn', 'en') : '';
        $benefitsEn = !empty($benefits) ? quickServerTranslate($benefits, 'bn', 'en') : '';
        $usageEn = !empty($usage) ? quickServerTranslate($usage, 'bn', 'en') : '';
        $hadithEn = !empty($hadith) ? quickServerTranslate($hadith, 'bn', 'en') : '';
        $fiqhEn = !empty($fiqh) ? quickServerTranslate($fiqh, 'bn', 'en') : '';
        $altEn = !empty($alt) ? quickServerTranslate($alt, 'bn', 'en') : '';

        if ($title === '' || $desc === '') {
            setFlash('danger', __('খাদ্যের নাম ও বিবরণ আবশ্যক।', 'Food name and description are required.'));
        } else {
            if ($action === 'create') {
                $itemKey = 'food_' . time() . '_' . rand(100, 999);
                $stmt = $pdo->prepare("INSERT INTO halal_foods (
                    item_key, title, title_en, arabic_name, category, status, scientific_name, scientific_name_en,
                    source_origin, source_origin_en, image_url, nutrition_benefits, nutrition_benefits_en, 
                    usage_instructions, usage_instructions_en, description, description_en, hadith_ref, hadith_ref_en, 
                    fiqh_ruling, fiqh_ruling_en, halal_alternative, halal_alternative_en, is_active
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
                $stmt->execute([
                    $itemKey, $title, $titleEn, $arabicName, $category, $status, $sciName, $sciNameEn,
                    $sourceOrigin, $sourceOriginEn, $imageUrl, $benefits, $benefitsEn,
                    $usage, $usageEn, $desc, $descEn, $hadith, $hadithEn,
                    $fiqh, $fiqhEn, $alt, $altEn
                ]);
                logAdminAction($pdo, 'CREATE_HALAL_FOOD', 'halal_foods', (string)$pdo->lastInsertId(), "নতুন খাদ্য উপাদান যুক্ত: $title");
                setFlash('success', __('নতুন খাদ্য উপাদান সফলভাবে যুক্ত হয়েছে।', 'New food item added successfully.'));
            } else {
                $stmt = $pdo->prepare("UPDATE halal_foods SET 
                    title = ?, title_en = ?, arabic_name = ?, category = ?, status = ?, 
                    scientific_name = ?, scientific_name_en = ?, source_origin = ?, source_origin_en = ?, 
                    image_url = ?, nutrition_benefits = ?, nutrition_benefits_en = ?, 
                    usage_instructions = ?, usage_instructions_en = ?, description = ?, description_en = ?, 
                    hadith_ref = ?, hadith_ref_en = ?, fiqh_ruling = ?, fiqh_ruling_en = ?, 
                    halal_alternative = ?, halal_alternative_en = ? 
                    WHERE id = ?");
                $stmt->execute([
                    $title, $titleEn, $arabicName, $category, $status,
                    $sciName, $sciNameEn, $sourceOrigin, $sourceOriginEn,
                    $imageUrl, $benefits, $benefitsEn,
                    $usage, $usageEn, $desc, $descEn,
                    $hadith, $hadithEn, $fiqh, $fiqhEn,
                    $alt, $altEn, $id
                ]);
                logAdminAction($pdo, 'UPDATE_HALAL_FOOD', 'halal_foods', (string)$id, "খাদ্য উপাদান আপডেট: $title");
                setFlash('success', __('খাদ্য উপাদানের তথ্য সফলভাবে আপডেট হয়েছে।', 'Food item updated successfully.'));
            }
        }
        redirect('halal_foods.php');
    }

    if ($action === 'delete') {
        $id = (int)($_POST['id'] ?? 0);
        $stmt = $pdo->prepare("DELETE FROM halal_foods WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_HALAL_FOOD', 'halal_foods', (string)$id, "খাদ্য উপাদান ডিলিট ID: $id");
        setFlash('success', __('খাদ্য উপাদান সফলভাবে মুছে ফেলা হয়েছে।', 'Food item deleted successfully.'));
        redirect('halal_foods.php');
    }
}

// Fetch stats
$counts = [
    'total' => 0,
    'halal' => 0,
    'haram' => 0,
    'mushbooh' => 0,
    'sunnah' => 0,
    'daily' => 0
];
try {
    $counts['total'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1")->fetchColumn();
    $counts['halal'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1 AND status = 'HALAL'")->fetchColumn();
    $counts['haram'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1 AND status = 'HARAM'")->fetchColumn();
    $counts['mushbooh'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1 AND status = 'MUSHBOOH'")->fetchColumn();
    $counts['sunnah'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1 AND category = 'SUNNAH_FOOD'")->fetchColumn();
    $counts['daily'] = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods WHERE is_active = 1 AND category = 'DAILY_FOOD'")->fetchColumn();
} catch (Exception $e) {}

// Filtering
$q = trim($_GET['q'] ?? '');
$filterCategory = trim($_GET['category'] ?? 'ALL');
$filterStatus = trim($_GET['status'] ?? 'ALL');

$where = ["is_active = 1"];
$params = [];

if ($filterCategory !== '' && $filterCategory !== 'ALL') {
    $where[] = "category = ?";
    $params[] = $filterCategory;
}
if ($filterStatus !== '' && $filterStatus !== 'ALL') {
    $where[] = "status = ?";
    $params[] = $filterStatus;
}
if ($q !== '') {
    $where[] = "(title LIKE ? OR title_en LIKE ? OR arabic_name LIKE ? OR scientific_name LIKE ? OR description LIKE ? OR source_origin LIKE ?)";
    $params[] = "%$q%";
    $params[] = "%$q%";
    $params[] = "%$q%";
    $params[] = "%$q%";
    $params[] = "%$q%";
    $params[] = "%$q%";
}

$whereSql = implode(' AND ', $where);
$stmt = $pdo->prepare("SELECT * FROM halal_foods WHERE $whereSql ORDER BY id DESC");
$stmt->execute($params);
$items = $stmt->fetchAll(PDO::FETCH_ASSOC);

$pageTitle = __('হালাল খাদ্য ও উপাদান', 'Halal Food & Ingredients');
$activeNav = 'halal_foods';
require_once __DIR__ . '/header.php';
?>

<!-- Content Header -->
<div class="content-header" style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px; margin-bottom:24px;">
  <div>
    <h1 class="page-title" style="margin:0; font-size:24px; font-weight:700; color:var(--text-heading, #0f172a); display:flex; align-items:center; gap:10px;">
      <i class="fa-solid fa-utensils" style="color:var(--primary, #10b981);"></i>
      <?= __('হালাল খাদ্য', 'Halal Food') ?>
    </h1>
  </div>
  <div class="header-actions">
    <button type="button" class="btn btn-primary" onclick="openCreateModal()" style="display:inline-flex; align-items:center; gap:8px; padding:10px 20px; font-size:14px; font-weight:600; border-radius:10px; box-shadow:0 4px 12px rgba(16,185,129,0.25);">
      <i class="fa-solid fa-plus"></i>
      <?= __('নতুন খাদ্য', 'New Food') ?>
    </button>
  </div>
</div>

<!-- Stat Cards -->
<div class="stats-grid" style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:16px; margin-bottom:24px;">
  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(16,185,129,0.15); color:#10b981; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-utensils"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['total']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('মোট খাদ্য উপাদান', 'Total Food Items') ?></div>
  </div>

  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(34,197,94,0.15); color:#22c55e; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-circle-check"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['halal']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('অনুমোদিত হালাল', 'Approved Halal') ?></div>
  </div>

  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(239,68,68,0.15); color:#ef4444; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-ban"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['haram']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('নিষিদ্ধ হারাম', 'Prohibited Haram') ?></div>
  </div>

  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(245,158,11,0.15); color:#f59e0b; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-triangle-exclamation"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['mushbooh']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('সন্দেহজনক খাদ্য', 'Mushbooh Items') ?></div>
  </div>

  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(20,184,166,0.15); color:#14b8a6; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-leaf"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['sunnah']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('সুন্নাহ খাদ্য', 'Sunnah Food') ?></div>
  </div>

  <div class="stat-card" style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:14px; padding:18px;">
    <div class="stat-icon" style="background:rgba(59,130,246,0.15); color:#3b82f6; width:44px; height:44px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:18px; margin-bottom:12px;">
      <i class="fa-solid fa-bowl-rice"></i>
    </div>
    <div class="stat-value" style="font-size:26px; font-weight:700; color:var(--text-heading);"><?= toLangNum($counts['daily']) ?></div>
    <div class="stat-label" style="font-size:13px; color:var(--text-muted);"><?= __('নিত্যপ্রয়োজনীয় খাদ্য', 'Daily Essentials') ?></div>
  </div>
</div>

<!-- Filters Toolbar -->
<div class="card" style="margin-bottom: 24px; padding:18px; border-radius:14px;">
  <form method="GET" class="filter-bar" style="display:flex; gap:14px; flex-wrap:wrap; align-items:center;">
    <div style="flex:1; min-width:220px;">
      <input type="text" name="q" class="form-control" placeholder="<?= __('খাদ্যের নাম, বৈজ্ঞানিক নাম বা বিবরণ দিয়ে খুঁজুন...', 'Search by name, scientific name or description...') ?>" value="<?= htmlspecialchars($q) ?>">
    </div>
    <div style="width:200px;">
      <select name="category" class="form-control" onchange="this.form.submit()">
        <option value="ALL" <?= $filterCategory === 'ALL' ? 'selected' : '' ?>><?= __('সকল ক্যাটাগরি', 'All Categories') ?></option>
        <option value="SUNNAH_FOOD" <?= $filterCategory === 'SUNNAH_FOOD' ? 'selected' : '' ?>><?= __('সুন্নাহ খাদ্য', 'Sunnah Food') ?></option>
        <option value="HALAL_FOOD" <?= $filterCategory === 'HALAL_FOOD' ? 'selected' : '' ?>><?= __('হালাল খাদ্য ও উপাদান', 'Halal Food & Items') ?></option>
        <option value="DAILY_FOOD" <?= $filterCategory === 'DAILY_FOOD' ? 'selected' : '' ?>><?= __('নিত্যপ্রয়োজনীয় খাদ্য', 'Daily Essentials') ?></option>
        <option value="HARAM_FOOD" <?= $filterCategory === 'HARAM_FOOD' ? 'selected' : '' ?>><?= __('হারাম খাবার ও পানীয়', 'Haram Food') ?></option>
        <option value="MUSHBOOH_FOOD" <?= $filterCategory === 'MUSHBOOH_FOOD' ? 'selected' : '' ?>><?= __('সন্দেহজনক খাদ্য', 'Mushbooh Food') ?></option>
        <option value="BEVERAGE" <?= $filterCategory === 'BEVERAGE' ? 'selected' : '' ?>><?= __('পানীয় ও জুস', 'Beverages') ?></option>
        <option value="MEAT_POULTRY" <?= $filterCategory === 'MEAT_POULTRY' ? 'selected' : '' ?>><?= __('মাংস ও পোলট্রি', 'Meat & Poultry') ?></option>
        <option value="BAKERY_SWEETS" <?= $filterCategory === 'BAKERY_SWEETS' ? 'selected' : '' ?>><?= __('বেকারি ও মিষ্টান্ন', 'Bakery & Sweets') ?></option>
        <option value="COSMETICS" <?= $filterCategory === 'COSMETICS' ? 'selected' : '' ?>><?= __('কসমেটিকস ও ঔষধ', 'Cosmetics & Medicine') ?></option>
      </select>
    </div>
    <div style="width:170px;">
      <select name="status" class="form-control" onchange="this.form.submit()">
        <option value="ALL" <?= $filterStatus === 'ALL' ? 'selected' : '' ?>><?= __('সকল স্ট্যাটাস', 'All Statuses') ?></option>
        <option value="HALAL" <?= $filterStatus === 'HALAL' ? 'selected' : '' ?>><?= __('হালাল', 'Halal') ?></option>
        <option value="HARAM" <?= $filterStatus === 'HARAM' ? 'selected' : '' ?>><?= __('হারাম', 'Haram') ?></option>
        <option value="MUSHBOOH" <?= $filterStatus === 'MUSHBOOH' ? 'selected' : '' ?>><?= __('সন্দেহজনক', 'Mushbooh') ?></option>
      </select>
    </div>
    <button type="submit" class="btn btn-primary" style="padding:8px 18px;"><?= __('ফিল্টার করুন', 'Filter') ?></button>
    <?php if ($q !== '' || $filterCategory !== 'ALL' || $filterStatus !== 'ALL'): ?>
      <a href="halal_foods.php" class="btn btn-secondary" style="padding:8px 16px;"><?= __('রিসেট', 'Reset') ?></a>
    <?php endif; ?>
  </form>
</div>

<!-- Table Card -->
<div class="card" style="border-radius:14px; overflow:hidden;">
  <div class="card-header" style="padding:16px 20px; border-bottom:1px solid var(--border-color);">
    <h3 class="card-title" style="margin:0; font-size:16px; font-weight:700;">
      <?= __('খাদ্য ও উপাদান তালিকা (মোট ', 'Food Items List (Total ') . toLangNum(count($items)) . __(' টি)', ')') ?>
    </h3>
  </div>
  <div class="table-responsive">
    <table class="table" style="width:100%; margin:0;">
      <thead>
        <tr>
          <th style="width:60px;"><?= __('আইডি', 'ID') ?></th>
          <th><?= __('খাদ্যের নাম', 'Food Name') ?></th>
          <th><?= __('আরবি নাম', 'Arabic Name') ?></th>
          <th><?= __('ক্যাটাগরি', 'Category') ?></th>
          <th><?= __('স্ট্যাটাস', 'Status') ?></th>
          <th><?= __('বৈজ্ঞানিক নাম ও উৎস', 'Scientific Name & Origin') ?></th>
          <th><?= __('হাদিস ও ফিকহ রেফারেন্স', 'Hadith & Fiqh Reference') ?></th>
          <th><?= __('বিকল্প হালাল উপাদান', 'Halal Alternative') ?></th>
          <th style="text-align:right; min-width:140px;"><?= __('অ্যাকশন', 'Action') ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($items)): ?>
          <tr>
            <td colspan="9" style="text-align:center; padding:35px; color:var(--text-muted, #94a3b8);">
              <?= __('কোনো খাদ্য উপাদান পাওয়া যায়নি।', 'No food items found.') ?>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($items as $item): ?>
            <tr>
              <td style="font-weight:600; color:var(--text-muted);">#<?= toLangNum($item['id']) ?></td>
              <td>
                <div style="display:flex; align-items:center; gap:12px;">
                  <?php if (!empty($item['image_url'])): ?>
                    <img src="<?= htmlspecialchars((strpos($item['image_url'], 'http') === 0) ? $item['image_url'] : '../' . ltrim($item['image_url'], '/')) ?>" alt="" style="width:40px; height:40px; border-radius:10px; object-fit:cover; border:1px solid var(--border-color, #cbd5e1);" onerror="this.style.display='none'">
                  <?php endif; ?>
                  <div>
                    <div style="font-weight:700; color:var(--text-heading, #0f172a); font-size:14px;">
                      <?= htmlspecialchars($item['title']) ?>
                    </div>
                    <?php if (!empty($item['title_en'])): ?>
                      <div style="font-size:12px; color:var(--text-muted, #64748b);">
                        <?= htmlspecialchars($item['title_en']) ?>
                      </div>
                    <?php endif; ?>
                  </div>
                </div>
              </td>
              <td style="font-family:'Amiri', serif; font-size:16px; color:var(--text-heading);">
                <?= htmlspecialchars($item['arabic_name'] ?? '—') ?>
              </td>
              <td>
                <span class="badge" style="background:rgba(16,185,129,0.12); color:#10B981; border:1px solid rgba(16,185,129,0.25); font-weight:600;">
                  <?= htmlspecialchars($item['category']) ?>
                </span>
              </td>
              <td>
                <?php if ($item['status'] === 'HALAL'): ?>
                  <span class="badge" style="background:rgba(34,197,94,0.15); color:#22c55e; font-weight:bold; border:1px solid rgba(34,197,94,0.3);">
                    <i class="fa-solid fa-circle-check" style="margin-right:4px;"></i><?= __('হালাল', 'HALAL') ?>
                  </span>
                <?php elseif ($item['status'] === 'HARAM'): ?>
                  <span class="badge" style="background:rgba(239,68,68,0.15); color:#ef4444; font-weight:bold; border:1px solid rgba(239,68,68,0.3);">
                    <i class="fa-solid fa-ban" style="margin-right:4px;"></i><?= __('হারাম', 'HARAM') ?>
                  </span>
                <?php else: ?>
                  <span class="badge" style="background:rgba(245,158,11,0.15); color:#f59e0b; font-weight:bold; border:1px solid rgba(245,158,11,0.3);">
                    <i class="fa-solid fa-triangle-exclamation" style="margin-right:4px;"></i><?= __('সন্দেহজনক', 'MUSHBOOH') ?>
                  </span>
                <?php endif; ?>
              </td>
              <td style="max-width:180px; font-size:13px; color:var(--text-main);">
                <?php if (!empty($item['scientific_name'])): ?>
                  <div style="font-style:italic; font-weight:600; color:var(--text-heading);"><?= htmlspecialchars($item['scientific_name']) ?></div>
                <?php endif; ?>
                <div style="color:var(--text-muted);"><?= htmlspecialchars(mb_substr($item['source_origin'] ?? '—', 0, 35) . (mb_strlen($item['source_origin'] ?? '') > 35 ? '...' : '')) ?></div>
              </td>
              <td style="max-width:180px; font-size:13px; color:var(--text-muted);">
                <div><?= htmlspecialchars(mb_substr($item['hadith_ref'] ?? $item['fiqh_ruling'] ?? '—', 0, 40) . (mb_strlen($item['hadith_ref'] ?? '') > 40 ? '...' : '')) ?></div>
              </td>
              <td style="font-size:13px; color:#10B981; max-width:140px; font-weight:600;">
                <?= htmlspecialchars($item['halal_alternative'] ?? '—') ?>
              </td>
              <td style="text-align:right; white-space:nowrap;">
                <button type="button" class="btn btn-sm btn-secondary" onclick='openEditModal(<?= json_encode($item, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE) ?>)' style="margin-right:4px;">
                  <i class="fa-solid fa-pen-to-square"></i> <?= __('এডিট', 'Edit') ?>
                </button>
                <form method="POST" style="display:inline;" onsubmit="return confirm('<?= __('আপনি কি নিশ্চিত যে এই উপাদানটি মুছে ফেলতে চান?', 'Are you sure you want to delete this food item?') ?>');">
                  <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="<?= $item['id'] ?>">
                  <button type="submit" class="btn btn-sm btn-danger">
                    <i class="fa-solid fa-trash-can"></i> <?= __('ডিলিট', 'Delete') ?>
                  </button>
                </form>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- Add / Edit Modal (Clean, Native Theme, Reliable Close/Cancel/Save Buttons) -->
<div id="foodModal" class="modal" style="display:none; position:fixed; z-index:999999; left:0; top:0; width:100%; height:100%; overflow:auto; background:rgba(15,23,42,0.65); backdrop-filter:blur(5px);">
  <div style="background:var(--bg-card, #ffffff); border:1px solid var(--border-color, #e2e8f0); border-radius:18px; margin:30px auto; max-width:780px; padding:26px; color:var(--text-main, #1e293b); box-shadow:0 25px 50px -12px rgba(0,0,0,0.25);">
    
    <!-- Modal Header with guaranteed inline onclick & close -->
    <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--border-color, #e2e8f0); padding-bottom:16px; margin-bottom:20px;">
      <h3 id="modalTitle" style="margin:0; font-size:18px; font-weight:700; color:var(--text-heading, #0f172a); display:flex; align-items:center; gap:8px;">
        <i class="fa-solid fa-utensils" style="color:var(--primary, #10b981);"></i>
        <?= __('নতুন খাদ্য', 'New Food') ?>
      </h3>
      <button type="button" id="modalCloseBtn" onclick="closeFoodModal(event); document.getElementById('foodModal').style.display='none'; return false;" style="background:none; border:none; font-size:22px; cursor:pointer; color:var(--text-muted, #64748b); line-height:1; padding:6px 10px; border-radius:8px;" title="<?= __('বন্ধ করুন', 'Close') ?>">
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>

    <!-- Clean, Intuitive Form with zero bracket noise & single language input -->
    <form method="POST" id="foodForm" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" id="formAction" value="create">
      <input type="hidden" name="id" id="foodId" value="0">

      <!-- Row 1: Food Title, Category, Status -->
      <div style="display:grid; grid-template-columns: 2fr 1fr 1fr; gap:16px;">
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('খাদ্যের নাম', 'Food Name') ?> *
          </label>
          <input type="text" name="title" id="foodTitle" class="form-control" required placeholder="<?= __('যেমন: খাঁটি মধু বা কালোজিরা', 'e.g. Pure Honey or Black Seed') ?>">
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('ক্যাটাগরি', 'Category') ?> *
          </label>
          <select name="category" id="foodCategory" class="form-control" required>
            <option value="SUNNAH_FOOD"><?= __('সুন্নাহ খাদ্য', 'Sunnah Food') ?></option>
            <option value="HALAL_FOOD"><?= __('হালাল খাদ্য ও উপাদান', 'Halal Food') ?></option>
            <option value="DAILY_FOOD"><?= __('নিত্যপ্রয়োজনীয় খাদ্য', 'Daily Essentials') ?></option>
            <option value="BEVERAGE"><?= __('পানীয় ও জুস', 'Beverages') ?></option>
            <option value="MEAT_POULTRY"><?= __('মাংস ও পোলট্রি', 'Meat & Poultry') ?></option>
            <option value="BAKERY_SWEETS"><?= __('বেকারি ও মিষ্টান্ন', 'Bakery & Sweets') ?></option>
            <option value="HARAM_FOOD"><?= __('হারাম খাবার ও উপাদান', 'Haram Food') ?></option>
            <option value="MUSHBOOH_FOOD"><?= __('সন্দেহজনক খাদ্য', 'Mushbooh Food') ?></option>
            <option value="COSMETICS"><?= __('কসমেটিকস ও ঔষধ', 'Cosmetics & Medicine') ?></option>
          </select>
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('স্ট্যাটাস', 'Status') ?> *
          </label>
          <select name="status" id="foodStatus" class="form-control" required>
            <option value="HALAL"><?= __('হালাল', 'Halal') ?></option>
            <option value="HARAM"><?= __('হারাম', 'Haram') ?></option>
            <option value="MUSHBOOH"><?= __('সন্দেহজনক', 'Mushbooh') ?></option>
          </select>
        </div>
      </div>

      <!-- Row 2: Scientific Name & Source/Origin -->
      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px; margin-top:14px;">
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('বৈজ্ঞানিক নাম', 'Scientific Name') ?>
          </label>
          <input type="text" name="scientific_name" id="foodSciName" class="form-control" placeholder="<?= __('যেমন: Nigella Sativa', 'e.g. Nigella Sativa') ?>">
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('উৎস', 'Source') ?>
          </label>
          <input type="text" name="source_origin" id="foodSource" class="form-control" placeholder="<?= __('যেমন: প্রাকৃতিক মৌচাক / উদ্ভিজ্জ তেল', 'e.g. Natural beehive / Plant oil') ?>">
        </div>
      </div>

      <!-- Row 3: Image URL & Local Image File Upload -->
      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px; margin-top:14px;">
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('ছবির লিংক', 'Image URL') ?>
          </label>
          <input type="text" name="image_url" id="foodImageUrl" class="form-control" placeholder="https://... বা uploads/...">
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('ছবি আপলোড', 'Upload Image') ?>
          </label>
          <input type="file" name="image_file" id="foodImageFile" class="form-control" accept="image/jpeg,image/png,image/webp,image/gif">
        </div>
      </div>

      <!-- Row 4: Detailed Description -->
      <div class="form-group" style="margin-top:14px;">
        <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
          <?= __('বিবরণ', 'Description') ?> *
        </label>
        <textarea name="description" id="foodDesc" class="form-control" rows="3" required placeholder="<?= __('খাদ্যের বৈশিষ্ট্য, উপাদান ও ইসলামিক গুরুত্ব...', 'Food properties, details, and significance...') ?>"></textarea>
      </div>

      <!-- Row 5: Quran/Hadith Reference & Fiqh Ruling -->
      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px; margin-top:14px;">
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('রেফারেন্স', 'Reference') ?>
          </label>
          <input type="text" name="hadith_ref" id="foodHadith" class="form-control" placeholder="<?= __('যেমন: সহীহ বুখারী: ৫৬৮৪; সূরা আন-নাহল: ৬৯', 'e.g. Sahih Bukhari: 5684') ?>">
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('ফিকহি রায়', 'Fiqh Ruling') ?>
          </label>
          <input type="text" name="fiqh_ruling" id="foodFiqh" class="form-control" placeholder="<?= __('যেমন: চার মাযহাবের সর্বসম্মত ইজমা অনুযায়ী সম্পূর্ণ হালাল', 'e.g. Unanimously Halal by 4 Madhabs') ?>">
        </div>
      </div>

      <!-- Row 6: Halal Alternatives -->
      <div class="form-group" style="margin-top:14px;">
        <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
          <?= __('হালাল বিকল্প', 'Halal Alternative') ?>
        </label>
        <input type="text" name="halal_alternative" id="foodAlt" class="form-control" placeholder="<?= __('হারাম বা সন্দেহজনক হলে বিকল্প হালাল উপাদানের নাম...', 'Halal alternative if item is haram or doubtful...') ?>">
      </div>

      <!-- Row 7: Nutrition Benefits & Usage Instructions -->
      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px; margin-top:14px;">
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('উপকারিতা', 'Benefits') ?>
          </label>
          <textarea name="nutrition_benefits" id="foodBenefits" class="form-control" rows="2" placeholder="<?= __('রোগ প্রতিরোধ ক্ষমতা বৃদ্ধি, পুষ্টি ইত্যাদি...', 'Health benefits or nutritional value...') ?>"></textarea>
        </div>
        <div class="form-group">
          <label style="font-weight:600; font-size:13px; color:var(--text-heading); margin-bottom:6px; display:block;">
            <?= __('ব্যবহার বিধি', 'Usage') ?>
          </label>
          <textarea name="usage_instructions" id="foodUsage" class="form-control" rows="2" placeholder="<?= __('কীভাবে আহার বা ব্যবহার করতে হবে...', 'How to consume or apply...') ?>"></textarea>
        </div>
      </div>

      <!-- Modal Footer Action Buttons -->
      <div style="display:flex; justify-content:flex-end; align-items:center; gap:12px; margin-top:24px; border-top:1px solid var(--border-color, #e2e8f0); padding-top:16px;">
        <button type="button" class="btn btn-secondary" id="modalCancelBtn" onclick="closeFoodModal(event); document.getElementById('foodModal').style.display='none'; return false;" style="padding:9px 20px; font-size:14px; font-weight:600; border-radius:8px;">
          <?= __('বাতিল', 'Cancel') ?>
        </button>
        <button type="submit" class="btn btn-primary" id="saveBtn" style="padding:9px 24px; font-size:14px; font-weight:600; border-radius:8px;">
          <i class="fa-solid fa-floppy-disk" style="margin-right:6px;"></i>
          <?= __('সংরক্ষণ করুন', 'Save') ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
// Fail-safe Global Modal Functions for Food Modal
function openCreateModal() {
  document.getElementById('modalTitle').innerHTML = '<i class="fa-solid fa-utensils" style="color:var(--primary, #10b981);"></i> <?= __('নতুন খাদ্য', 'New Food') ?>';
  document.getElementById('formAction').value = 'create';
  document.getElementById('foodId').value = '0';
  document.getElementById('foodForm').reset();
  var modal = document.getElementById('foodModal');
  if (modal) {
    modal.style.display = 'block';
    modal.classList.add('open');
  }
}
window.openCreateModal = openCreateModal;

function openEditModal(item) {
  document.getElementById('modalTitle').innerHTML = '<i class="fa-solid fa-pen-to-square" style="color:var(--primary, #10b981);"></i> <?= __('খাদ্য সম্পাদনা', 'Edit Food') ?>';
  document.getElementById('formAction').value = 'update';
  document.getElementById('foodId').value = item.id;
  document.getElementById('foodTitle').value = item.title || '';
  document.getElementById('foodCategory').value = item.category || 'DAILY_FOOD';
  document.getElementById('foodStatus').value = item.status || 'HALAL';
  document.getElementById('foodSciName').value = item.scientific_name || '';
  document.getElementById('foodImageUrl').value = item.image_url || '';
  document.getElementById('foodSource').value = item.source_origin || '';
  document.getElementById('foodDesc').value = item.description || '';
  document.getElementById('foodHadith').value = item.hadith_ref || '';
  document.getElementById('foodFiqh').value = item.fiqh_ruling || '';
  document.getElementById('foodAlt').value = item.halal_alternative || '';
  document.getElementById('foodBenefits').value = item.nutrition_benefits || '';
  document.getElementById('foodUsage').value = item.usage_instructions || '';
  
  var modal = document.getElementById('foodModal');
  if (modal) {
    modal.style.display = 'block';
    modal.classList.add('open');
  }
}
window.openEditModal = openEditModal;

function closeFoodModal(e) {
  if (e && e.preventDefault) e.preventDefault();
  var modal = document.getElementById('foodModal');
  if (modal) {
    modal.style.display = 'none';
    modal.classList.remove('open');
  }
  document.body.style.overflow = '';
}
window.closeFoodModal = closeFoodModal;
window.closeModal = closeFoodModal;

// Modal Backdrop Click Listener
document.addEventListener('DOMContentLoaded', function() {
  var modal = document.getElementById('foodModal');
  if (modal) {
    modal.addEventListener('click', function(e) {
      if (e.target === modal) {
        closeFoodModal(e);
      }
    });
  }
});

// Keyboard ESC Listener
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape' || e.key === 'Esc') {
    closeFoodModal(e);
  }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
