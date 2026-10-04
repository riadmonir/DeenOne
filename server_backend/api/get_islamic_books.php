<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET AUTHENTIC ISLAMIC BOOKS (ইসলামিক বই ও কিতাবসমূহ)
 * Endpoint: GET /api/get_islamic_books.php
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
require_once __DIR__ . '/islamic_books_helper.php';

$category = trim($_GET['category'] ?? '');
$search = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = max(1, min(100, (int)($_GET['limit'] ?? 50)));
$offset = ($page - 1) * $limit;

try {
    // 1. Check SQLite master database first
    $sqlitePdo = getIslamicBooksDbPdo();
    if ($sqlitePdo !== null) {
        $where = ["1=1"];
        $params = [];

        if (!empty($category) && $category !== 'all' && $category !== 'সকল') {
            $where[] = "(category = ? OR category_bn = ? OR category_en = ?)";
            $params[] = $category;
            $params[] = $category;
            $params[] = $category;
        }

        if (!empty($search)) {
            $where[] = "(title LIKE ? OR title_en LIKE ? OR author LIKE ? OR author_en LIKE ?)";
            $term = "%$search%";
            $params[] = $term;
            $params[] = $term;
            $params[] = $term;
            $params[] = $term;
        }

        $whereSql = "WHERE " . implode(" AND ", $where);

        $countStmt = $sqlitePdo->prepare("SELECT COUNT(*) FROM books $whereSql");
        $countStmt->execute($params);
        $totalCount = (int)$countStmt->fetchColumn();

        $sql = "SELECT b.*, (SELECT COUNT(*) FROM chapters c WHERE c.book_id = b.id) as chapter_count 
                FROM books b 
                $whereSql 
                ORDER BY b.id ASC 
                LIMIT $limit OFFSET $offset";
        $stmt = $sqlitePdo->prepare($sql);
        $stmt->execute($params);
        $books = $stmt->fetchAll();

        sendJsonResponse([
            'success' => true,
            'source' => 'sqlite_database',
            'total' => $totalCount,
            'page' => $page,
            'limit' => $limit,
            'count' => count($books),
            'books' => $books
        ]);
    }

    // 2. Fallback to MySQL
    $pdo = getDbConnection();
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

    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM islamic_books $whereSql");
    $countStmt->execute($params);
    $totalCount = (int)$countStmt->fetchColumn();

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
        'source' => 'mysql_database',
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
