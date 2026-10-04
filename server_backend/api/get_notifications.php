<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET ACTIVE PUSH NOTIFICATIONS & BROADCASTS
 * Consumed by Android Client for In-App Notifications and Sync
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$limit = max(1, min(50, (int)($_GET['limit'] ?? 20)));
$userId = trim($_GET['user_id'] ?? '');
$sinceId = isset($_GET['since_id']) ? (int)$_GET['since_id'] : 0;

try {
    if ($sinceId > 0) {
        // Incremental sync: Return ONLY the single latest notification after since_id
        if (!empty($userId)) {
            $stmt = $pdo->prepare("SELECT id, title, body, notification_type, target_action, target_url, image_url, priority, target_audience, created_at 
                                   FROM push_notifications 
                                   WHERE is_active = 1 AND id > ? AND (target_audience = 'ALL' OR target_audience = ?) 
                                   ORDER BY id DESC 
                                   LIMIT 1");
            $stmt->execute([$sinceId, $userId]);
        } else {
            $stmt = $pdo->prepare("SELECT id, title, body, notification_type, target_action, target_url, image_url, priority, target_audience, created_at 
                                   FROM push_notifications 
                                   WHERE is_active = 1 AND id > ? AND target_audience = 'ALL' 
                                   ORDER BY id DESC 
                                   LIMIT 1");
            $stmt->execute([$sinceId]);
        }
    } else {
        // Full list or history
        if (!empty($userId)) {
            $stmt = $pdo->prepare("SELECT id, title, body, notification_type, target_action, target_url, image_url, priority, target_audience, created_at 
                                   FROM push_notifications 
                                   WHERE is_active = 1 AND (target_audience = 'ALL' OR target_audience = ?) 
                                   ORDER BY id DESC 
                                   LIMIT ?");
            $stmt->execute([$userId, $limit]);
        } else {
            $stmt = $pdo->prepare("SELECT id, title, body, notification_type, target_action, target_url, image_url, priority, target_audience, created_at 
                                   FROM push_notifications 
                                   WHERE is_active = 1 AND target_audience = 'ALL' 
                                   ORDER BY id DESC 
                                   LIMIT ?");
            $stmt->execute([$limit]);
        }
    }
    $notifications = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($notifications),
        'notifications' => $notifications
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load notifications: ' . $e->getMessage()
    ], 500);
}
?>
