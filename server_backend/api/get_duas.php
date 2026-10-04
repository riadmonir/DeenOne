<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET AUTHENTIC DUAS & SUPPLICATIONS
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$category = trim($_GET['category'] ?? '');
$search = trim($_GET['search'] ?? '');

$where = [];
$params = [];

if (!empty($category)) {
    $where[] = "d.category_slug = ?";
    $params[] = $category;
}
if (!empty($search)) {
    $where[] = "(d.title_bn LIKE ? OR d.translation_bn LIKE ? OR d.reference LIKE ?)";
    $term = "%$search%";
    $params = array_merge($params, [$term, $term, $term]);
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

try {
    $sql = "SELECT d.dua_uid, d.category_slug, c.name_bn as category_name, d.title_bn, d.arabic_text, d.pronunciation_bn, d.translation_bn, d.reference, d.virtue_bn, d.audio_url 
            FROM duas d 
            LEFT JOIN dua_categories c ON d.category_slug = c.category_slug 
            $whereSql 
            ORDER BY d.id ASC";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $duas = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($duas),
        'duas' => $duas
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load duas: ' . $e->getMessage()
    ], 500);
}
?>
