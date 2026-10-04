<?php
/**
 * ==============================================================================
 * DEEN ONE API - HALAL & HARAM FOODS ENCYCLOPEDIA (হালাল-হারাম খাদ্যকোষ API)
 * 100% Dual-Language (BN/EN/AR), Fast Data Transmission & Android Client Sync
 * ==============================================================================
 */
header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');

if (($_SERVER['REQUEST_METHOD'] ?? '') === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/db.php';
if (file_exists(__DIR__ . '/../services/HalalFoodSyncService.php')) {
    require_once __DIR__ . '/../services/HalalFoodSyncService.php';
}

$pdo = getDbConnection();
ensureHalalFoodsTableExists($pdo);

$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';

if ($method === 'POST') {
    handlePostRequest($pdo);
} else {
    handleGetRequest($pdo);
}

function handleGetRequest($pdo) {
    $search = trim($_GET['search'] ?? '');
    $category = trim($_GET['category'] ?? 'ALL');
    $status = trim($_GET['status'] ?? 'ALL');
    $limit = isset($_GET['limit']) ? min(1000, max(1, (int)$_GET['limit'])) : 500;
    $offset = isset($_GET['offset']) ? max(0, (int)$_GET['offset']) : 0;
    $since = isset($_GET['since']) ? (int)$_GET['since'] : 0;

    $where = ["is_active = 1"];
    $params = [];

    if ($category !== '' && strtoupper($category) !== 'ALL') {
        $where[] = "category = ?";
        $params[] = $category;
    }

    if ($status !== '' && strtoupper($status) !== 'ALL') {
        $where[] = "status = ?";
        $params[] = strtoupper($status);
    }

    if ($search !== '') {
        $cleanSearch = '%' . $search . '%';
        $where[] = "(title LIKE ? OR title_en LIKE ? OR arabic_name LIKE ? OR scientific_name LIKE ? OR description LIKE ? OR source_origin LIKE ? OR halal_alternative LIKE ?)";
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
        $params[] = $cleanSearch;
    }

    if ($since > 0) {
        $where[] = "UNIX_TIMESTAMP(updated_at) >= ?";
        $params[] = $since;
    }

    $whereClause = implode(" AND ", $where);

    // Total Count
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM halal_foods WHERE $whereClause");
    $countStmt->execute($params);
    $totalCount = (int)$countStmt->fetchColumn();

    // Data Fetch with Dual-Language fields
    $sql = "SELECT id, item_key, title, title_en, arabic_name, category, status, 
                   scientific_name, scientific_name_en, source_origin, source_origin_en,
                   image_url, nutrition_benefits, nutrition_benefits_en, 
                   usage_instructions, usage_instructions_en, description, description_en, 
                   hadith_ref, hadith_ref_en, fiqh_ruling, fiqh_ruling_en, 
                   halal_alternative, halal_alternative_en, UNIX_TIMESTAMP(updated_at) as updated_timestamp 
            FROM halal_foods 
            WHERE $whereClause 
            ORDER BY 
                CASE 
                    WHEN status = 'HARAM' THEN 1 
                    WHEN status = 'MUSHBOOH' THEN 2 
                    WHEN status = 'HALAL' THEN 3 
                    ELSE 4 
                END, 
                id ASC 
            LIMIT $limit OFFSET $offset";

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $items = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Categories summary
    $catStmt = $pdo->query("SELECT category, COUNT(*) as count FROM halal_foods WHERE is_active = 1 GROUP BY category");
    $categories = $catStmt ? $catStmt->fetchAll(PDO::FETCH_KEY_PAIR) : [];

    // Status summary
    $statStmt = $pdo->query("SELECT status, COUNT(*) as count FROM halal_foods WHERE is_active = 1 GROUP BY status");
    $statusCounts = $statStmt ? $statStmt->fetchAll(PDO::FETCH_KEY_PAIR) : [];

    echo json_encode([
        'success' => true,
        'auto_synced' => true,
        'total_count' => $totalCount,
        'count' => count($items),
        'limit' => $limit,
        'offset' => $offset,
        'categories_summary' => $categories,
        'status_summary' => $statusCounts,
        'items' => $items,
        'server_time' => time()
    ], JSON_UNESCAPED_UNICODE);
}

function handlePostRequest($pdo) {
    $raw = file_get_contents('php://input');
    $input = json_decode($raw, true) ?: $_POST;

    $action = trim($input['action'] ?? '');

    if ($action === 'check_ingredients') {
        $ingredientsText = trim($input['ingredients'] ?? '');
        if (empty($ingredientsText)) {
            echo json_encode(['success' => false, 'message' => 'ইনগ্রেডিয়েন্টস টেক্সট প্রদান করুন।']);
            return;
        }

        $matched = [];
        $words = preg_split('/[,;\n]+/', $ingredientsText);
        foreach ($words as $word) {
            $word = trim($word);
            if (mb_strlen($word) < 2) continue;

            $stmt = $pdo->prepare("SELECT id, item_key, title, title_en, status, category, halal_alternative, description 
                                   FROM halal_foods 
                                   WHERE is_active = 1 AND (title LIKE ? OR title_en LIKE ? OR scientific_name LIKE ?) 
                                   LIMIT 1");
            $like = '%' . $word . '%';
            $stmt->execute([$like, $like, $like]);
            $res = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($res) {
                $matched[] = [
                    'query' => $word,
                    'item' => $res
                ];
            }
        }

        echo json_encode([
            'success' => true,
            'matched_count' => count($matched),
            'results' => $matched
        ], JSON_UNESCAPED_UNICODE);
        return;
    }

    echo json_encode(['success' => false, 'message' => 'অজ্ঞাত অনুরোধ।']);
}

function ensureHalalFoodsTableExists($pdo) {
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

    // Check count and automatically seed if empty
    $count = (int)$pdo->query("SELECT COUNT(*) FROM halal_foods")->fetchColumn();
    if ($count === 0) {
        if (file_exists(__DIR__ . '/../data/seed_halal_foods.php')) {
            require_once __DIR__ . '/../data/seed_halal_foods.php';
            seedComprehensiveHalalFoods($pdo);
        }
    }
}
