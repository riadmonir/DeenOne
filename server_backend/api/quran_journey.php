<?php
/**
 * ==============================================================================
 * DEEN ONE API - QURAN JOURNEY (কুরআন যাত্রা)
 * Manages daily Quran journey lessons, user streak, Noor points, and cloud sync
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// 1. Handle POST: Sync User Progress (Noor Points, Streak, Level, Amol)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = getRequestData();
    $userId = trim($data['user_id'] ?? '');

    if (empty($userId)) {
        $userId = 'usr_' . substr(md5($_SERVER['REMOTE_ADDR'] ?? 'guest'), 0, 10);
    }

    $noorPoints = max(0, (int)($data['noor_points'] ?? 10));
    $streakDays = max(1, (int)($data['streak_days'] ?? 1));
    $currentLevel = trim($data['current_level'] ?? 'প্রাথমিক (৩ আয়াত/দিন)');
    $currentSurah = max(1, (int)($data['surah_number'] ?? ($data['current_surah'] ?? 1)));
    $currentAyah = max(1, (int)($data['ayah_number'] ?? ($data['current_ayah'] ?? 1)));
    $completedAyahs = trim($data['completed_ayahs'] ?? '');
    $amolDoneDate = !empty($data['amol_done_date']) ? $data['amol_done_date'] : date('Y-m-d');
    $lastCompletedDate = !empty($data['last_completed_date']) ? $data['last_completed_date'] : date('Y-m-d');
    $reminderEnabled = isset($data['reminder_enabled']) ? (int)$data['reminder_enabled'] : 1;

    try {
        $stmt = $pdo->prepare("INSERT INTO user_quran_journey_records 
            (user_id, noor_points, streak_days, current_level, current_surah, current_ayah, completed_ayahs, amol_done_date, last_completed_date, reminder_enabled, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                noor_points = VALUES(noor_points),
                streak_days = VALUES(streak_days),
                current_level = VALUES(current_level),
                current_surah = VALUES(current_surah),
                current_ayah = VALUES(current_ayah),
                completed_ayahs = VALUES(completed_ayahs),
                amol_done_date = VALUES(amol_done_date),
                last_completed_date = VALUES(last_completed_date),
                reminder_enabled = VALUES(reminder_enabled),
                updated_at = NOW()");

        $stmt->execute([$userId, $noorPoints, $streakDays, $currentLevel, $currentSurah, $currentAyah, $completedAyahs, $amolDoneDate, $lastCompletedDate, $reminderEnabled]);

        $countStmt = $pdo->query("SELECT COUNT(DISTINCT user_id) FROM user_quran_journey_records");
        $totalLearners = max(1, (int)$countStmt->fetchColumn());

        sendJsonResponse([
            'success' => true,
            'message' => 'কুরআন যাত্রার তথ্য সফলভাবে সিঙ্ক হয়েছে।',
            'data' => [
                'user_id' => $userId,
                'noor_points' => $noorPoints,
                'streak_days' => $streakDays,
                'current_level' => $currentLevel,
                'current_surah' => $currentSurah,
                'current_ayah' => $currentAyah,
                'amol_done_date' => $amolDoneDate,
                'last_completed_date' => $lastCompletedDate,
                'total_learners' => $totalLearners
            ]
        ]);
    } catch (PDOException $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// 2. Handle GET: Fetch Journey Lesson & Stats
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $userId = trim($_GET['user_id'] ?? '');
    $dayNumber = max(1, (int)($_GET['day'] ?? 1));

    try {
        // Fetch lesson
        $stmt = $pdo->prepare("SELECT * FROM quran_journey_lessons WHERE day_number = ? AND is_active = 1 LIMIT 1");
        $stmt->execute([$dayNumber]);
        $lesson = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$lesson) {
            // Fallback default day 1
            $stmt = $pdo->prepare("SELECT * FROM quran_journey_lessons WHERE day_number = 1 LIMIT 1");
            $stmt->execute();
            $lesson = $stmt->fetch(PDO::FETCH_ASSOC);
        }

        // Parse JSON fields
        if ($lesson) {
            $lesson['words'] = json_decode($lesson['words_json'] ?? '[]', true);
            $lesson['options'] = json_decode($lesson['options_json'] ?? '[]', true);
            unset($lesson['words_json'], $lesson['options_json']);
        }

        // Count genuine active learners
        $countStmt = $pdo->query("SELECT COUNT(DISTINCT user_id) FROM user_quran_journey_records");
        $dbCount = (int)$countStmt->fetchColumn();
        $totalLearners = max(1, $dbCount);

        $userData = null;
        if (!empty($userId)) {
            $uStmt = $pdo->prepare("SELECT * FROM user_quran_journey_records WHERE user_id = ? LIMIT 1");
            $uStmt->execute([$userId]);
            $userData = $uStmt->fetch(PDO::FETCH_ASSOC);
        }

        sendJsonResponse([
            'success' => true,
            'data' => [
                'total_learners' => $totalLearners,
                'lesson' => $lesson,
                'user_progress' => $userData
            ]
        ]);
    } catch (PDOException $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}
