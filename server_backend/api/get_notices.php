<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET ACTIVE NOTICES & BROADCASTS
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

try {
    $stmt = $pdo->query("SELECT id, title, message, notice_type, action_url, created_at 
                         FROM app_notices 
                         WHERE is_active = 1 
                         ORDER BY id DESC");
    $notices = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($notices),
        'notices' => $notices
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load notices: ' . $e->getMessage()
    ], 500);
}
?>
