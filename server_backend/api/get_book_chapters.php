<?php
/**
 * ==============================================================================
 * DEEN ONE REST API - GET BOOK CHAPTERS (ইসলামিক বইয়ের অধ্যায়সমূহ)
 * Endpoint: GET /api/get_book_chapters.php?book_id=book_01
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

$bookId = trim($_GET['book_id'] ?? $_GET['id'] ?? '');

if (empty($bookId)) {
    sendJsonResponse([
        'success' => false,
        'error' => 'book_id is required'
    ], 400);
}

try {
    // 1. Try SQLite Database first
    $sqlitePdo = getIslamicBooksDbPdo();
    if ($sqlitePdo !== null) {
        $bStmt = $sqlitePdo->prepare("SELECT * FROM books WHERE id = ? LIMIT 1");
        $bStmt->execute([$bookId]);
        $book = $bStmt->fetch();

        if ($book) {
            $chStmt = $sqlitePdo->prepare("SELECT id, book_id, chapter_index, title, content FROM chapters WHERE book_id = ? ORDER BY chapter_index ASC");
            $chStmt->execute([$bookId]);
            $chapters = $chStmt->fetchAll();

            sendJsonResponse([
                'success' => true,
                'source' => 'sqlite_database',
                'book' => $book,
                'total_chapters' => count($chapters),
                'chapters' => $chapters
            ]);
        }
    }

    // 2. Fallback to MySQL if table exists
    $mysqlPdo = getDbConnection();
    $mStmt = $mysqlPdo->prepare("SELECT * FROM islamic_books WHERE book_uid = ? OR id = ? LIMIT 1");
    $mStmt->execute([$bookId, $bookId]);
    $mBook = $mStmt->fetch();

    if ($mBook) {
        $mChStmt = $mysqlPdo->prepare("SELECT id, book_uid as book_id, chapter_index, title_bn as title, content_bn as content FROM islamic_book_chapters WHERE book_uid = ? ORDER BY chapter_index ASC");
        $mChStmt->execute([$mBook['book_uid'] ?? $bookId]);
        $mChapters = $mChStmt->fetchAll();

        sendJsonResponse([
            'success' => true,
            'source' => 'mysql_database',
            'book' => $mBook,
            'total_chapters' => count($mChapters),
            'chapters' => $mChapters
        ]);
    }

    sendJsonResponse([
        'success' => false,
        'error' => 'Book not found'
    ], 404);

} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load book chapters: ' . $e->getMessage()
    ], 500);
}
?>
