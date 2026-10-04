<?php
/**
 * ==============================================================================
 * DEEN ONE API - LEADERBOARD & XP RANKINGS
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$limit = max(1, min(100, (int)($_GET['limit'] ?? 25)));

try {
    $stmt = $pdo->prepare("SELECT user_id, name, avatar, district, total_points, battles_won, daily_streak 
                           FROM users 
                           WHERE is_banned = 0 
                           ORDER BY total_points DESC, battles_won DESC 
                           LIMIT ?");
    $stmt->execute([$limit]);
    $rows = $stmt->fetchAll();

    $leaderboard = [];
    foreach ($rows as $idx => $r) {
        $leaderboard[] = [
            'rank' => $idx + 1,
            'user_id' => $r['user_id'],
            'user_name' => $r['name'],
            'avatar_url' => $r['avatar'] ?? 'avatar_1',
            'timezone' => $r['district'] ?? 'ঢাকা',
            'points' => (int)$r['total_points'],
            'streak_days' => max(1, (int)$r['daily_streak']),
            'tier_title' => ((int)$r['total_points'] > 2000 ? 'মাস্টার' : ((int)$r['total_points'] > 800 ? 'অগ্রবর্তী' : 'নবাগত'))
        ];
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($leaderboard),
        'leaderboard' => $leaderboard
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load leaderboard: ' . $e->getMessage()
    ], 500);
}
?>
