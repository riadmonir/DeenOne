<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET BATTLE MATCH HISTORY
 * Endpoint: GET /api/get_battle_history.php
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$limit = isset($_GET['limit']) ? intval($_GET['limit']) : 50;
if ($limit < 1) $limit = 50;
if ($limit > 100) $limit = 100;

$userId = isset($_GET['user_id']) ? trim($_GET['user_id']) : '';

try {
    if (!empty($userId)) {
        // Query history where user was winner or in participants
        $stmt = $pdo->prepare("
            SELECT * FROM battle_history 
            WHERE winner_player_id = ? OR final_scores_json LIKE ?
            ORDER BY finished_at DESC 
            LIMIT ?
        ");
        $likePattern = '%' . $userId . '%';
        $stmt->bindValue(1, $userId, PDO::PARAM_STR);
        $stmt->bindValue(2, $likePattern, PDO::PARAM_STR);
        $stmt->bindValue(3, $limit, PDO::PARAM_INT);
        $stmt->execute();
    } else {
        $stmt = $pdo->prepare("SELECT * FROM battle_history ORDER BY finished_at DESC LIMIT ?");
        $stmt->bindValue(1, $limit, PDO::PARAM_INT);
        $stmt->execute();
    }

    $history = $stmt->fetchAll(PDO::FETCH_ASSOC);

    sendJsonResponse([
        'success' => true,
        'count' => count($history),
        'history' => $history
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load battle history: ' . $e->getMessage()
    ], 500);
}
?>
