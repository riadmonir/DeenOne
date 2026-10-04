<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET QUIZ & KNOWLEDGE BATTLE CATEGORIES
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

try {
    $sql = "SELECT qc.id, qc.category_id, qc.tag_bn, qc.tag_en, qc.title_bn, qc.title_en, 
                   qc.description_bn, qc.description_en, qc.total_questions, qc.duration_minutes, 
                   qc.points_per_question, qc.icon, qc.display_order, COUNT(qq.id) as saved_question_count 
            FROM quiz_categories qc 
            LEFT JOIN quiz_questions qq ON qc.category_id = qq.category_id 
            WHERE qc.is_active = 1 
            GROUP BY qc.id 
            ORDER BY qc.display_order ASC, qc.id ASC";
    $categories = $pdo->query($sql)->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($categories),
        'categories' => $categories
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load categories: ' . $e->getMessage()
    ], 500);
}
?>
