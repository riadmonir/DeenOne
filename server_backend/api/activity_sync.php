<?php
/**
 * ==============================================================================
 * DEEN ONE API - REAL-TIME ACTIVITY & USER DATA SYNCHRONIZATION
 * Endpoint: /api/activity_sync.php
 * Handles real-time sync for:
 *   1. Namaz / Salah Tracker (user_prayer_logs)
 *   2. Nek Amal Tracker (user_amal_logs)
 *   3. Quiz Participation & Score (user_quiz_results)
 *   4. Continuous Streak & Calendar (daily_streak, last_streak_date)
 *   5. Full Cloud Restore on Login (restore_activities)
 *   6. User Nickname & Name Updates (update_nickname)
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
$data = getRequestData();
$action = trim($data['action'] ?? $_GET['action'] ?? 'restore_activities');
$userId = trim($data['user_id'] ?? $_GET['user_id'] ?? '');

if (empty($userId) || $userId === 'guest' || $userId === 'usr_guest') {
    sendJsonResponse(['success' => false, 'error' => 'Valid user_id is required for activity sync.'], 400);
}

// -----------------------------------------------------------------------------
// 1. SYNC PRAYER / NAMAZ LOG (নামাজ ট্র্যাকার সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_prayer') {
    $prayerName = trim($data['prayer_name'] ?? '');
    $prayerDate = trim($data['prayer_date'] ?? date('Y-m-d'));
    $isPrayed = !empty($data['is_prayed']) ? 1 : 0;
    $isJamah = !empty($data['is_jamah']) ? 1 : 0;
    $isQaza = !empty($data['is_qaza']) ? 1 : 0;
    $status = trim($data['status'] ?? ($isJamah ? 'JAMAAT' : ($isQaza ? 'QAZA' : ($isPrayed ? 'EKAKI' : 'NONE'))));
    $points = isset($data['points']) ? intval($data['points']) : 0;

    if (empty($prayerName)) {
        sendJsonResponse(['success' => false, 'error' => 'Prayer name is required.'], 400);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO user_prayer_logs 
            (user_id, prayer_name, prayer_date, is_prayed, is_jamah, is_qaza, status, points, synced_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                is_prayed = VALUES(is_prayed),
                is_jamah = VALUES(is_jamah),
                is_qaza = VALUES(is_qaza),
                status = VALUES(status),
                points = VALUES(points),
                synced_at = NOW()");
        $stmt->execute([$userId, $prayerName, $prayerDate, $isPrayed, $isJamah, $isQaza, $status, $points]);

        // Recalculate and update user streak and active status
        $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Prayer log synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 1b. SYNC BATCH PRAYER LOGS (একাধিক বা অফলাইন/প্রি-লগইন নামাজ একসাথে সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_batch_prayers') {
    $prayers = $data['prayers'] ?? [];
    if (!is_array($prayers) || empty($prayers)) {
        sendJsonResponse(['success' => true, 'synced_count' => 0]);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO user_prayer_logs 
            (user_id, prayer_name, prayer_date, is_prayed, is_jamah, is_qaza, status, points, synced_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                is_prayed = VALUES(is_prayed),
                is_jamah = VALUES(is_jamah),
                is_qaza = VALUES(is_qaza),
                status = VALUES(status),
                points = VALUES(points),
                synced_at = NOW()");

        $count = 0;
        foreach ($prayers as $p) {
            $prayerName = trim($p['prayer_name'] ?? '');
            $prayerDate = trim($p['prayer_date'] ?? '');
            if (!empty($prayerName) && !empty($prayerDate)) {
                $isPrayed = !empty($p['is_prayed']) ? 1 : 0;
                $isJamah = !empty($p['is_jamah']) ? 1 : 0;
                $isQaza = !empty($p['is_qaza']) ? 1 : 0;
                $status = trim($p['status'] ?? ($isJamah ? 'JAMAAT' : ($isQaza ? 'QAZA' : ($isPrayed ? 'EKAKI' : 'NONE'))));
                $points = isset($p['points']) ? intval($p['points']) : 0;
                $stmt->execute([$userId, $prayerName, $prayerDate, $isPrayed, $isJamah, $isQaza, $status, $points]);
                $count++;
            }
        }

        $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);

        sendJsonResponse([
            'success' => true,
            'synced_count' => $count,
            'message' => 'Batch prayers synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 2. SYNC NEK AMAL LOG (নেক আমল ট্র্যাকার সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_amal') {
    $logDate = trim($data['log_date'] ?? date('Y-m-d'));
    $fajr = !empty($data['fajr']) ? 1 : 0;
    $dhuhr = !empty($data['dhuhr']) ? 1 : 0;
    $asr = !empty($data['asr']) ? 1 : 0;
    $maghrib = !empty($data['maghrib']) ? 1 : 0;
    $isha = !empty($data['isha']) ? 1 : 0;
    $tahajjud = !empty($data['tahajjud']) ? 1 : 0;
    $dhuha = !empty($data['dhuha']) ? 1 : 0;
    $quranMinutes = isset($data['quran_minutes']) ? max(0, intval($data['quran_minutes'])) : 0;
    $sadaqah = !empty($data['sadaqah']) ? 1 : 0;
    $fasting = !empty($data['fasting']) ? 1 : 0;
    $dhikrCount = isset($data['dhikr_count']) ? max(0, intval($data['dhikr_count'])) : 0;
    $score = isset($data['score']) ? max(0, intval($data['score'])) : 0;

    try {
        $stmt = $pdo->prepare("INSERT INTO user_amal_logs 
            (user_id, log_date, fajr, dhuhr, asr, maghrib, isha, tahajjud, dhuha, quran_minutes, sadaqah, fasting, dhikr_count, score, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                fajr = VALUES(fajr),
                dhuhr = VALUES(dhuhr),
                asr = VALUES(asr),
                maghrib = VALUES(maghrib),
                isha = VALUES(isha),
                tahajjud = VALUES(tahajjud),
                dhuha = VALUES(dhuha),
                quran_minutes = VALUES(quran_minutes),
                sadaqah = VALUES(sadaqah),
                fasting = VALUES(fasting),
                dhikr_count = VALUES(dhikr_count),
                score = VALUES(score),
                updated_at = NOW()");
        $stmt->execute([
            $userId, $logDate, $fajr, $dhuhr, $asr, $maghrib, $isha, $tahajjud, $dhuha,
            $quranMinutes, $sadaqah, $fasting, $dhikrCount, $score
        ]);

        $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Amal log synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 2b. SYNC BATCH AMAL LOGS (একাধিক বা অফলাইন/প্রি-লগইন নেক আমল একসাথে সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_batch_amal') {
    $amals = $data['amals'] ?? [];
    if (!is_array($amals) || empty($amals)) {
        sendJsonResponse(['success' => true, 'synced_count' => 0]);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO user_amal_logs 
            (user_id, log_date, fajr, dhuhr, asr, maghrib, isha, tahajjud, dhuha, quran_minutes, sadaqah, fasting, dhikr_count, score, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                fajr = VALUES(fajr),
                dhuhr = VALUES(dhuhr),
                asr = VALUES(asr),
                maghrib = VALUES(maghrib),
                isha = VALUES(isha),
                tahajjud = VALUES(tahajjud),
                dhuha = VALUES(dhuha),
                quran_minutes = VALUES(quran_minutes),
                sadaqah = VALUES(sadaqah),
                fasting = VALUES(fasting),
                dhikr_count = VALUES(dhikr_count),
                score = VALUES(score),
                updated_at = NOW()");

        $count = 0;
        foreach ($amals as $a) {
            $logDate = trim($a['log_date'] ?? '');
            if (!empty($logDate)) {
                $fajr = !empty($a['fajr']) ? 1 : 0;
                $dhuhr = !empty($a['dhuhr']) ? 1 : 0;
                $asr = !empty($a['asr']) ? 1 : 0;
                $maghrib = !empty($a['maghrib']) ? 1 : 0;
                $isha = !empty($a['isha']) ? 1 : 0;
                $tahajjud = !empty($a['tahajjud']) ? 1 : 0;
                $dhuha = !empty($a['dhuha']) ? 1 : 0;
                $quranMinutes = isset($a['quran_minutes']) ? max(0, intval($a['quran_minutes'])) : 0;
                $sadaqah = !empty($a['sadaqah']) ? 1 : 0;
                $fasting = !empty($a['fasting']) ? 1 : 0;
                $dhikrCount = isset($a['dhikr_count']) ? max(0, intval($a['dhikr_count'])) : 0;
                $score = isset($a['score']) ? max(0, intval($a['score'])) : 0;
                $stmt->execute([
                    $userId, $logDate, $fajr, $dhuhr, $asr, $maghrib, $isha, $tahajjud, $dhuha,
                    $quranMinutes, $sadaqah, $fasting, $dhikrCount, $score
                ]);
                $count++;
            }
        }

        $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);

        sendJsonResponse([
            'success' => true,
            'synced_count' => $count,
            'message' => 'Batch amals synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 3. SYNC QUIZ RESULT & POINTS (কুইজ ফলাফল ও স্কোর সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_quiz') {
    $categoryId = trim($data['category_id'] ?? 'general_islamic');
    $score = isset($data['score']) ? intval($data['score']) : 0;
    $totalQuestions = isset($data['total_questions']) ? intval($data['total_questions']) : 1;
    $correctCount = isset($data['correct_count']) ? intval($data['correct_count']) : 0;
    $pointsEarned = isset($data['points_earned']) ? intval($data['points_earned']) : ($correctCount * 10);

    try {
        $stmt = $pdo->prepare("INSERT INTO user_quiz_results 
            (user_id, category_id, score, total_questions, correct_count, points_earned, completed_at)
            VALUES (?, ?, ?, ?, ?, ?, NOW())");
        $stmt->execute([$userId, $categoryId, $score, $totalQuestions, $correctCount, $pointsEarned]);

        // Update user's aggregate points and quiz points in users table
        $upd = $pdo->prepare("UPDATE users SET 
            quiz_points = quiz_points + ?, 
            total_points = total_points + ?,
            last_active = NOW()
            WHERE user_id = ?");
        $upd->execute([$pointsEarned, $pointsEarned, $userId]);

        // Return latest points
        $pStmt = $pdo->prepare("SELECT total_points, quiz_points FROM users WHERE user_id = ?");
        $pStmt->execute([$userId]);
        $pts = $pStmt->fetch();

        sendJsonResponse([
            'success' => true,
            'message' => 'Quiz result synced successfully.',
            'total_points' => intval($pts['total_points'] ?? 0),
            'quiz_points' => intval($pts['quiz_points'] ?? 0)
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 4. SYNC STREAK & CONTINUOUS CALENDAR (ধারাবাহিকতা ও ক্যালেন্ডার স্ট্রিক)
// -----------------------------------------------------------------------------
if ($action === 'sync_streak') {
    $streak = isset($data['daily_streak']) ? intval($data['daily_streak']) : 1;
    $streakDate = trim($data['last_streak_date'] ?? date('Y-m-d'));

    try {
        $stmt = $pdo->prepare("UPDATE users SET daily_streak = ?, last_streak_date = ?, last_active = NOW() WHERE user_id = ?");
        $stmt->execute([$streak, $streakDate, $userId]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Streak synced successfully.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 4b. SYNC TASBIH / DHIKR CHALLENGE (তাসবীহ ও জিকির চ্যালেঞ্জ সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_tasbih') {
    $dhikrIndex = isset($data['dhikr_index']) ? intval($data['dhikr_index']) : 0;
    $dhikrTitle = trim($data['dhikr_title'] ?? '');
    $tapCount = isset($data['tap_count']) ? max(0, intval($data['tap_count'])) : 0;
    $completedTimes = isset($data['completed_times']) ? max(0, intval($data['completed_times'])) : 0;
    $pointsEarned = isset($data['points_earned']) ? max(0, intval($data['points_earned'])) : 0;
    $logDate = trim($data['log_date'] ?? date('Y-m-d'));

    if (empty($dhikrTitle)) {
        $dhikrTitle = 'Tasbih Dhikr #' . ($dhikrIndex + 1);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO user_tasbih_logs 
            (user_id, log_date, dhikr_index, dhikr_title, tap_count, completed_times, points_earned, synced_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                dhikr_title = VALUES(dhikr_title),
                tap_count = VALUES(tap_count),
                completed_times = VALUES(completed_times),
                points_earned = VALUES(points_earned),
                synced_at = NOW()");
        $stmt->execute([$userId, $logDate, $dhikrIndex, $dhikrTitle, $tapCount, $completedTimes, $pointsEarned]);

        // If new points were earned, update user's lifetime points
        if ($pointsEarned > 0) {
            $pdo->prepare("UPDATE users SET total_points = total_points + ?, last_active = NOW() WHERE user_id = ?")
                ->execute([$pointsEarned, $userId]);
        } else {
            $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);
        }

        // Update dhikr count in user_amal_logs
        $totalTapsToday = $tapCount + ($completedTimes * 100);
        $pdo->prepare("INSERT INTO user_amal_logs (user_id, log_date, dhikr_count, updated_at) 
            VALUES (?, ?, ?, NOW()) 
            ON DUPLICATE KEY UPDATE dhikr_count = GREATEST(dhikr_count, VALUES(dhikr_count)), updated_at = NOW()")
            ->execute([$userId, $logDate, $totalTapsToday]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Tasbih log synced successfully.',
            'dhikr_index' => $dhikrIndex,
            'points_earned' => $pointsEarned
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 4c. SYNC INDIVIDUAL AMAL RECORD (নির্দিষ্ট নেক আমল সম্পন্নের রেকর্ড সিঙ্ক)
// -----------------------------------------------------------------------------
if ($action === 'sync_amal_record') {
    $amalCode = trim($data['amal_code'] ?? '');
    $amalTitle = trim($data['amal_title'] ?? '');
    $points = isset($data['points']) ? max(0, intval($data['points'])) : 50;
    $logDate = trim($data['log_date'] ?? date('Y-m-d'));

    if (empty($amalCode)) {
        sendJsonResponse(['success' => false, 'error' => 'amal_code is required.'], 400);
    }

    try {
        $stmt = $pdo->prepare("INSERT INTO user_amal_records 
            (user_id, log_date, amal_code, amal_title, points, completed_at)
            VALUES (?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                amal_title = VALUES(amal_title),
                points = VALUES(points),
                completed_at = NOW()");
        $stmt->execute([$userId, $logDate, $amalCode, $amalTitle, $points]);

        // Increment user lifetime points
        if ($points > 0) {
            $pdo->prepare("UPDATE users SET total_points = total_points + ?, last_active = NOW() WHERE user_id = ?")
                ->execute([$points, $userId]);
        } else {
            $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$userId]);
        }

        sendJsonResponse([
            'success' => true,
            'message' => 'Amal record synced successfully.',
            'amal_code' => $amalCode,
            'points' => $points
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 5. UPDATE NICKNAME / USERNAME ACROSS DATABASE (ইউজারনেম ও ডাকনাম পরিবর্তন)
// -----------------------------------------------------------------------------
if ($action === 'update_nickname') {
    $name = trim($data['name'] ?? '');
    $username = trim($data['username'] ?? '');

    if (empty($name) && empty($username)) {
        sendJsonResponse(['success' => false, 'error' => 'Name or Nickname cannot be empty.'], 400);
    }

    try {
        $updFields = ["last_active = NOW()"];
        $params = [];

        if (!empty($name)) {
            $updFields[] = "name = ?";
            $params[] = $name;
        }
        if (!empty($username)) {
            // Check username uniqueness if changing username
            $chk = $pdo->prepare("SELECT user_id FROM users WHERE username = ? AND user_id != ? LIMIT 1");
            $chk->execute([$username, $userId]);
            if ($chk->fetch()) {
                sendJsonResponse(['success' => false, 'error' => 'এই ইউজারনেমটি অন্য কেউ ব্যবহার করছেন। অন্য একটি নির্বাচন করুন।'], 409);
            }
            $updFields[] = "username = ?";
            $params[] = $username;
        }

        $params[] = $userId;
        $pdo->prepare("UPDATE users SET " . implode(", ", $updFields) . " WHERE user_id = ?")->execute($params);

        // Also update author_name in community_posts and comments so community shows updated name
        $displayName = !empty($username) ? $username : $name;
        if (!empty($displayName)) {
            $pdo->prepare("UPDATE community_posts SET author_name = ? WHERE user_id = ?")->execute([$displayName, $userId]);
            $pdo->prepare("UPDATE community_comments SET author_name = ? WHERE user_id = ?")->execute([$displayName, $userId]);
            $pdo->prepare("UPDATE blood_donors SET name = ? WHERE user_id = ?")->execute([$displayName, $userId]);
        }

        sendJsonResponse([
            'success' => true,
            'name' => $name,
            'username' => $username,
            'message' => 'নাম ও ইউজারনেম সফলভাবে আপডেট করা হয়েছে।'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 6. RESTORE ALL USER ACTIVITIES ON LOGIN / SYNC (ডাটাবেজ থেকে সকল ডাটা রিস্টোর)
// -----------------------------------------------------------------------------
if ($action === 'restore_activities') {
    try {
        // 1. User Profile & Summary
        $uStmt = $pdo->prepare("SELECT user_id, name, username, email, phone, avatar, district, total_points, quiz_points, daily_streak, last_streak_date, created_at FROM users WHERE user_id = ? LIMIT 1");
        $uStmt->execute([$userId]);
        $user = $uStmt->fetch(PDO::FETCH_ASSOC);

        if (!$user) {
            sendJsonResponse(['success' => false, 'error' => 'User not found.'], 404);
        }

        // 2. Prayer Logs (Past 365 Days)
        $pStmt = $pdo->prepare("SELECT prayer_name, prayer_date, is_prayed, is_jamah, is_qaza, status, points, synced_at 
                                FROM user_prayer_logs 
                                WHERE user_id = ? 
                                ORDER BY prayer_date DESC, id DESC 
                                LIMIT 730");
        $pStmt->execute([$userId]);
        $prayerLogs = $pStmt->fetchAll(PDO::FETCH_ASSOC);

        // Prayer Summary (Total Unique Fard Prayers Prayed On-Time Lifetime)
        $prStmt = $pdo->prepare("SELECT COUNT(DISTINCT CONCAT(prayer_date, '_', prayer_name)) as total_prayed 
                                 FROM user_prayer_logs 
                                 WHERE user_id = ? 
                                   AND is_prayed = 1 
                                   AND is_qaza = 0 
                                   AND prayer_name IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') 
                                   AND status IN ('JAMAAT', 'EKAKI', 'DERI')");
        $prStmt->execute([$userId]);
        $prRow = $prStmt->fetch(PDO::FETCH_ASSOC);
        $totalPrayed = intval($prRow['total_prayed'] ?? 0);

        // 3. Amal Logs (Past 365 Days)
        $aStmt = $pdo->prepare("SELECT log_date, fajr, dhuhr, asr, maghrib, isha, tahajjud, dhuha, quran_minutes, sadaqah, fasting, dhikr_count, score, updated_at 
                                FROM user_amal_logs 
                                WHERE user_id = ? 
                                ORDER BY log_date DESC 
                                LIMIT 365");
        $aStmt->execute([$userId]);
        $amalLogs = $aStmt->fetchAll(PDO::FETCH_ASSOC);

        // Amal Summary (Total Deeds Completed Lifetime)
        $aSummaryStmt = $pdo->prepare("SELECT COUNT(*) as total_days, 
            COALESCE(SUM((fajr>0)+(dhuhr>0)+(asr>0)+(maghrib>0)+(isha>0)+(tahajjud>0)+(dhuha>0)+(quran_minutes>0)+(sadaqah>0)+(fasting>0)+(dhikr_count>0)), 0) as total_deeds 
            FROM user_amal_logs WHERE user_id = ?");
        $aSummaryStmt->execute([$userId]);
        $aSumRow = $aSummaryStmt->fetch(PDO::FETCH_ASSOC);
        $totalDeeds = intval($aSumRow['total_deeds'] ?? 0);

        // 4. Quiz Results Count and aggregate (Only won/correct quizzes)
        $qStmt = $pdo->prepare("SELECT COUNT(*) as total_played, COALESCE(SUM(points_earned), 0) as total_earned, COALESCE(SUM(correct_count), 0) as total_correct 
                                FROM user_quiz_results 
                                WHERE user_id = ? 
                                  AND (correct_count > 0 OR score > 0 OR points_earned > 0)");
        $qStmt->execute([$userId]);
        $quizSummary = $qStmt->fetch(PDO::FETCH_ASSOC);
        $totalQuizzes = intval($quizSummary['total_played'] ?? 0);

        // 5. Tasbih Logs (Past 365 Days)
        $tStmt = $pdo->prepare("SELECT log_date, dhikr_index, dhikr_title, tap_count, completed_times, points_earned, synced_at 
                                FROM user_tasbih_logs 
                                WHERE user_id = ? 
                                ORDER BY log_date DESC 
                                LIMIT 365");
        $tStmt->execute([$userId]);
        $tasbihLogs = $tStmt->fetchAll(PDO::FETCH_ASSOC);

        // 6. Amal Records (Today / Recent)
        $arStmt = $pdo->prepare("SELECT log_date, amal_code, amal_title, points, completed_at 
                                 FROM user_amal_records 
                                 WHERE user_id = ? 
                                 ORDER BY log_date DESC, completed_at DESC 
                                 LIMIT 100");
        $arStmt->execute([$userId]);
        $amalRecords = $arStmt->fetchAll(PDO::FETCH_ASSOC);

        $user['total_prayers'] = $totalPrayed;
        $user['total_amals'] = $totalDeeds;
        $user['total_quizzes'] = $totalQuizzes;

        sendJsonResponse([
            'success' => true,
            'user' => $user,
            'prayer_logs' => $prayerLogs,
            'prayer_summary' => [
                'total_prayed' => $totalPrayed
            ],
            'amal_logs' => $amalLogs,
            'amal_summary' => [
                'total_deeds' => $totalDeeds,
                'total_days' => intval($aSumRow['total_days'] ?? 0)
            ],
            'quiz_summary' => [
                'total_played' => $totalQuizzes,
                'total_earned' => intval($quizSummary['total_earned'] ?? 0),
                'total_correct' => intval($quizSummary['total_correct'] ?? 0)
            ],
            'tasbih_logs' => $tasbihLogs,
            'amal_records' => $amalRecords,
            'message' => 'User activities successfully retrieved from database.'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
    }
}

sendJsonResponse(['success' => false, 'error' => 'Invalid action.'], 400);
