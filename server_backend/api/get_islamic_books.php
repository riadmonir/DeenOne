<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET AUTHENTIC ISLAMIC BOOKS (ইসলামিক বই ও কিতাবসমূহ)
 * Endpoint: GET /api/get_islamic_books.php
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$category = trim($_GET['category'] ?? '');
$search = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = max(1, min(50, (int)($_GET['limit'] ?? 20)));
$offset = ($page - 1) * $limit;

$where = ["is_active = 1"];
$params = [];

if (!empty($category) && $category !== 'all' && $category !== 'সকল') {
    $where[] = "(category_bn = ? OR category_en = ?)";
    $params[] = $category;
    $params[] = $category;
}

if (!empty($search)) {
    $where[] = "(title_bn LIKE ? OR title_en LIKE ? OR author_bn LIKE ? OR author_en LIKE ?)";
    $term = "%$search%";
    $params = array_merge($params, [$term, $term, $term, $term]);
}

$whereSql = "WHERE " . implode(" AND ", $where);

try {
    // Total count
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM islamic_books $whereSql");
    $countStmt->execute($params);
    $totalCount = (int)$countStmt->fetchColumn();

    // Books list
    $sql = "SELECT id, book_uid, title_bn, title_en, title_ar, author_bn, author_en, 
                   category_bn, category_en, description_bn, description_en, cover_image_url, 
                   pdf_url, file_size_bytes, total_pages, download_count, read_count, created_at 
            FROM islamic_books 
            $whereSql 
            ORDER BY id ASC 
            LIMIT $limit OFFSET $offset";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $books = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'total' => $totalCount,
        'page' => $page,
        'limit' => $limit,
        'count' => count($books),
        'books' => $books
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load Islamic books: ' . $e->getMessage()
    ], 500);
}
?>
