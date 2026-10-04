<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET MASAIL ARTICLES & RULINGS (100% PRODUCTION READY)
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$topic_id = isset($_GET['topic_id']) ? intval($_GET['topic_id']) : 0;
$search = trim($_GET['search'] ?? '');

$where = ["is_active = 1"];
$params = [];

if ($topic_id > 0) {
    $where[] = "topic_id = ?";
    $params[] = $topic_id;
}
if (!empty($search)) {
    $where[] = "(title_bn LIKE ? OR title_en LIKE ? OR content_bn LIKE ? OR content_en LIKE ?)";
    $term = "%$search%";
    $params = array_merge($params, [$term, $term, $term, $term]);
}

$whereSql = "WHERE " . implode(" AND ", $where);

try {
    $sql = "SELECT id, topic_id, topic_title, title_bn, title_en, content_bn, content_en, display_order 
            FROM masail_articles 
            $whereSql 
            ORDER BY display_order ASC, id ASC";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $articles = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($articles),
        'topic_id' => $topic_id,
        'articles' => $articles
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load masail articles: ' . $e->getMessage()
    ], 500);
}
?>
