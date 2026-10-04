<?php
/**
 * ==============================================================================
 * DEEN ONE API - GALLERY & MEDIA ASSETS
 * Endpoint: GET /api/gallery.php
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// Auto create table if not exists
try {
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS `gallery_images` (
          `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
          `image_uid` VARCHAR(60) NOT NULL UNIQUE,
          `title` VARCHAR(255) NOT NULL,
          `category` VARCHAR(100) DEFAULT 'অন্যান্য',
          `file_url` VARCHAR(500) NOT NULL,
          `file_path` VARCHAR(500) NOT NULL,
          `width` INT UNSIGNED DEFAULT 0,
          `height` INT UNSIGNED DEFAULT 0,
          `original_size` BIGINT UNSIGNED DEFAULT 0,
          `compressed_size` BIGINT UNSIGNED DEFAULT 0,
          `mime_type` VARCHAR(50) DEFAULT 'image/webp',
          `original_name` VARCHAR(255) DEFAULT '',
          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          INDEX `idx_gallery_cat` (`category`),
          INDEX `idx_gallery_created` (`created_at`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    ");
} catch (Exception $e) {}

$category = trim($_GET['category'] ?? '');
$search = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = max(1, min(100, (int)($_GET['limit'] ?? 30)));
$offset = ($page - 1) * $limit;

$where = [];
$params = [];

if (!empty($category) && $category !== 'all' && $category !== 'সকল') {
    $where[] = "category = ?";
    $params[] = $category;
}

if (!empty($search)) {
    $where[] = "(title LIKE ? OR category LIKE ? OR original_name LIKE ?)";
    $term = "%$search%";
    $params = array_merge($params, [$term, $term, $term]);
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

try {
    // Total count
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM gallery_images $whereSql");
    $countStmt->execute($params);
    $totalCount = (int)$countStmt->fetchColumn();

    // Fetch images
    $sql = "SELECT id, image_uid, title, category, file_url, width, height, compressed_size, mime_type, created_at 
            FROM gallery_images 
            $whereSql 
            ORDER BY id DESC 
            LIMIT $limit OFFSET $offset";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $images = $stmt->fetchAll();

    // Resolve base url
    $protocol = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on') ? "https" : "http";
    $host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
    $baseUrl = "$protocol://$host/";

    foreach ($images as &$img) {
        $rawUrl = $img['file_url'];
        if (preg_match('/^https?:\/\//i', $rawUrl)) {
            $img['full_url'] = $rawUrl;
        } else {
            $img['full_url'] = $baseUrl . ltrim($rawUrl, '/');
        }
    }

    sendJsonResponse([
        'success' => true,
        'total' => $totalCount,
        'page' => $page,
        'limit' => $limit,
        'count' => count($images),
        'images' => $images
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load gallery images: ' . $e->getMessage()
    ], 500);
}
?>
