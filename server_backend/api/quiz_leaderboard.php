<?php
/**
 * ==============================================================================
 * DEEN ONE API - QUIZ LEADERBOARD & RANK LIST (কুইজ র‍্যাংক তালিকা)
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$limit = max(1, min(100, (int)($_GET['limit'] ?? 50)));
$currentUserId = trim($_GET['user_id'] ?? '');

try {
    // 1. Ensure quiz_points column exists in users table
    try {
        $stmtCols = $pdo->query("SHOW COLUMNS FROM users");
        $existingCols = $stmtCols->fetchAll(PDO::FETCH_COLUMN);
        if (!in_array('quiz_points', $existingCols)) {
            $pdo->exec("ALTER TABLE users ADD COLUMN quiz_points INT UNSIGNED DEFAULT 0 AFTER total_points");
        }
    } catch (Exception $ce) {}

    // 2. Fetch top quiz competitors ordered by quiz_points DESC
    $stmt = $pdo->prepare("SELECT user_id, name, avatar, district, quiz_points 
                           FROM users 
                           WHERE is_banned = 0 
                           ORDER BY quiz_points DESC, last_active DESC 
                           LIMIT ?");
    $stmt->execute([$limit]);
    $rows = $stmt->fetchAll();

    $leaderboard = [];
    $userRank = 509; // Default fallback matching UI when user has 0 points
    $userFoundInTop = false;

    foreach ($rows as $idx => $r) {
        $rank = $idx + 1;
        $uid = $r['user_id'];
        $pts = (int)($r['quiz_points'] ?? 0);

        if (!empty($currentUserId) && $uid === $currentUserId) {
            $userRank = $rank;
            $userFoundInTop = true;
        }

        $leaderboard[] = [
            'rank' => $rank,
            'user_id' => $uid,
            'name' => !empty($r['name']) ? $r['name'] : 'ব্যবহারকারী',
            'district' => !empty($r['district']) ? $r['district'] : 'বাংলাদেশ',
            'avatar' => !empty($r['avatar']) ? $r['avatar'] : '',
            'quiz_points' => $pts
        ];
    }

    // 3. If current user is not in top limit, calculate their actual rank
    if (!empty($currentUserId) && !$userFoundInTop) {
        try {
            $uStmt = $pdo->prepare("SELECT quiz_points FROM users WHERE user_id = ? LIMIT 1");
            $uStmt->execute([$currentUserId]);
            $myRow = $uStmt->fetch();
            if ($myRow) {
                $myPoints = (int)$myRow['quiz_points'];
                if ($myPoints > 0) {
                    $rankStmt = $pdo->prepare("SELECT COUNT(*) + 1 AS actual_rank FROM users WHERE is_banned = 0 AND quiz_points > ?");
                    $rankStmt->execute([$myPoints]);
                    $rankRow = $rankStmt->fetch();
                    if ($rankRow && isset($rankRow['actual_rank'])) {
                        $userRank = (int)$rankRow['actual_rank'];
                    }
                }
            }
        } catch (Exception $re) {}
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($leaderboard),
        'user_rank' => $userRank,
        'leaderboard' => $leaderboard
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load quiz leaderboard: ' . $e->getMessage()
    ], 500);
}
?>
