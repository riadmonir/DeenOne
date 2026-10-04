<?php
/**
 * ==============================================================================
 * DEEN ONE API - USER PROFILE & POINTS SYNCHRONIZATION
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// 1. Handle POST Sync
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = getRequestData();
    $userId = trim($data['user_id'] ?? '');
    $name = trim($data['name'] ?? $data['user_name'] ?? '');
    $avatar = isset($data['avatar']) ? trim($data['avatar']) : (isset($data['avatar_url']) ? trim($data['avatar_url']) : null);
    $avatarExplicitlySent = ($avatar !== null && $avatar !== '') ? 1 : 0;
    $insertAvatar = ($avatarExplicitlySent === 1) ? $avatar : 'avatar_1';

    $district = trim($data['district'] ?? $data['timezone'] ?? '');
    $phone = trim($data['phone'] ?? '');
    $points = isset($data['points']) ? (int)$data['points'] : null;
    $quizPoints = isset($data['quiz_points']) ? (int)$data['quiz_points'] : null;
    $streak = isset($data['streak']) ? (int)$data['streak'] : null;
    $fcm = trim($data['fcm_token'] ?? '');

    $appLanguage = trim($data['app_language'] ?? $data['language'] ?? '');
    $themeMode = trim($data['theme_mode'] ?? $data['theme'] ?? '');

    if (empty($userId) || empty($name)) {
        sendJsonResponse(['success' => false, 'error' => 'User ID and Name are required.'], 400);
    }

    try {
        // Ensure columns exist
        try {
            $stmtCols = $pdo->query("SHOW COLUMNS FROM users");
            $existingCols = $stmtCols->fetchAll(PDO::FETCH_COLUMN);
            if (!in_array('app_language', $existingCols)) {
                $pdo->exec("ALTER TABLE users ADD COLUMN app_language VARCHAR(10) DEFAULT 'bn' AFTER avatar");
            }
            if (!in_array('theme_mode', $existingCols)) {
                $pdo->exec("ALTER TABLE users ADD COLUMN theme_mode VARCHAR(20) DEFAULT 'dark' AFTER app_language");
            }
            if (!in_array('quiz_points', $existingCols)) {
                $pdo->exec("ALTER TABLE users ADD COLUMN quiz_points INT UNSIGNED DEFAULT 0 AFTER total_points");
            }
        } catch (Exception $ce) {}

        // Upsert user with safeguarded avatar preservation
        $sql = "INSERT INTO users (user_id, name, avatar, district, phone, total_points, quiz_points, daily_streak, fcm_token, app_language, theme_mode, last_active) 
                VALUES (?, ?, ?, ?, ?, COALESCE(?, 0), COALESCE(?, 0), COALESCE(?, 1), ?, COALESCE(NULLIF(?, ''), 'bn'), COALESCE(NULLIF(?, ''), 'dark'), NOW()) 
                ON DUPLICATE KEY UPDATE 
                    name = VALUES(name),
                    avatar = CASE 
                        WHEN ? = 1 AND VALUES(avatar) IS NOT NULL AND VALUES(avatar) != '' AND VALUES(avatar) != 'avatar_1' THEN VALUES(avatar)
                        WHEN ? = 1 AND VALUES(avatar) = 'avatar_1' AND (avatar IS NULL OR avatar = '' OR avatar LIKE 'avatar_%') THEN 'avatar_1'
                        ELSE avatar 
                    END,
                    district = COALESCE(VALUES(district), district),
                    phone = COALESCE(VALUES(phone), phone),
                    total_points = GREATEST(total_points, COALESCE(VALUES(total_points), total_points)),
                    quiz_points = GREATEST(quiz_points, COALESCE(VALUES(quiz_points), quiz_points)),
                    daily_streak = GREATEST(daily_streak, COALESCE(VALUES(daily_streak), daily_streak)),
                    fcm_token = COALESCE(VALUES(fcm_token), fcm_token),
                    app_language = COALESCE(NULLIF(VALUES(app_language), ''), app_language),
                    theme_mode = COALESCE(NULLIF(VALUES(theme_mode), ''), theme_mode),
                    last_active = NOW()";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            $userId, $name, $insertAvatar, $district, $phone, $points, $quizPoints, $streak, 
            !empty($fcm) ? $fcm : null, $appLanguage, $themeMode,
            $avatarExplicitlySent, $avatarExplicitlySent
        ]);

        if ($avatarExplicitlySent === 1 && !empty($avatar) && $avatar !== 'avatar_1') {
            try {
                $pdo->prepare("UPDATE community_posts SET author_avatar = ? WHERE user_id = ?")->execute([$avatar, $userId]);
                $pdo->prepare("UPDATE community_comments SET author_avatar = ? WHERE user_id = ?")->execute([$avatar, $userId]);
            } catch (Exception $e) {}
        }

        sendJsonResponse([
            'success' => true,
            'message' => 'User synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// 2. Handle GET Profile
$userId = trim($_GET['user_id'] ?? '');
if (empty($userId)) {
    sendJsonResponse(['success' => false, 'error' => 'User ID required.'], 400);
}

try {
    $stmt = $pdo->prepare("SELECT user_id, name, email, phone, avatar, district, total_points, battles_played, battles_won, daily_streak, is_banned, app_language, theme_mode, last_active, created_at 
                           FROM users 
                           WHERE user_id = ? 
                           LIMIT 1");
    $stmt->execute([$userId]);
    $user = $stmt->fetch();

    if ($user) {
        sendJsonResponse([
            'success' => true,
            'user' => $user
        ]);
    } else {
        sendJsonResponse(['success' => false, 'error' => 'User not found.'], 404);
    }
} catch (Exception $e) {
    sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
}
?>
