<?php
/**
 * ==============================================================================
 * DEEN ONE API - QAZA SALAT TRACKER & SYNC
 * Manages persistent lifetime Qaza prayer counts and real-time cloud synchronization
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// 1. Handle POST (Save / Update Qaza Records)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = getRequestData();
    $userId = trim($data['user_id'] ?? '');

    if (empty($userId)) {
        sendJsonResponse(['success' => false, 'error' => 'user_id is required.'], 400);
    }

    $fajr = max(0, (int)($data['fajr_qaza'] ?? 0));
    $dhuhr = max(0, (int)($data['dhuhr_qaza'] ?? 0));
    $asr = max(0, (int)($data['asr_qaza'] ?? 0));
    $maghrib = max(0, (int)($data['maghrib_qaza'] ?? 0));
    $isha = max(0, (int)($data['isha_qaza'] ?? 0));
    $witr = max(0, (int)($data['witr_qaza'] ?? 0));
    $fasting = max(0, (int)($data['fasting_qaza'] ?? 0));
    $total = $fajr + $dhuhr + $asr + $maghrib + $isha + $witr;

    try {
        $stmt = $pdo->prepare("INSERT INTO user_qaza_records 
            (user_id, fajr_qaza, dhuhr_qaza, asr_qaza, maghrib_qaza, isha_qaza, witr_qaza, fasting_qaza, total_qaza, last_updated) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                fajr_qaza = VALUES(fajr_qaza),
                dhuhr_qaza = VALUES(dhuhr_qaza),
                asr_qaza = VALUES(asr_qaza),
                maghrib_qaza = VALUES(maghrib_qaza),
                isha_qaza = VALUES(isha_qaza),
                witr_qaza = VALUES(witr_qaza),
                fasting_qaza = VALUES(fasting_qaza),
                total_qaza = VALUES(total_qaza),
                last_updated = NOW()");

        $stmt->execute([$userId, $fajr, $dhuhr, $asr, $maghrib, $isha, $witr, $fasting, $total]);

        sendJsonResponse([
            'success' => true,
            'message' => 'কাযা নামাজ ও রোজার রেকর্ড ক্লাউডে সফলভাবে সিঙ্ক হয়েছে।',
            'data' => [
                'user_id' => $userId,
                'fajr_qaza' => $fajr,
                'dhuhr_qaza' => $dhuhr,
                'asr_qaza' => $asr,
                'maghrib_qaza' => $maghrib,
                'isha_qaza' => $isha,
                'witr_qaza' => $witr,
                'fasting_qaza' => $fasting,
                'total_qaza' => $total
            ]
        ]);
    } catch (PDOException $e) {
        sendJsonResponse([
            'success' => false,
            'error' => 'Database error: ' . $e->getMessage()
        ], 500);
    }
}

// 2. Handle GET (Fetch Qaza Records by user_id)
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $userId = trim($_GET['user_id'] ?? '');

    if (empty($userId)) {
        sendJsonResponse(['success' => false, 'error' => 'user_id is required.'], 400);
    }

    try {
        $stmt = $pdo->prepare("SELECT * FROM user_qaza_records WHERE user_id = ?");
        $stmt->execute([$userId]);
        $record = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($record) {
            sendJsonResponse([
                'success' => true,
                'data' => [
                    'user_id' => $record['user_id'],
                    'fajr_qaza' => (int)$record['fajr_qaza'],
                    'dhuhr_qaza' => (int)$record['dhuhr_qaza'],
                    'asr_qaza' => (int)$record['asr_qaza'],
                    'maghrib_qaza' => (int)$record['maghrib_qaza'],
                    'isha_qaza' => (int)$record['isha_qaza'],
                    'witr_qaza' => (int)$record['witr_qaza'],
                    'fasting_qaza' => (int)($record['fasting_qaza'] ?? 0),
                    'total_qaza' => (int)$record['total_qaza'],
                    'last_updated' => $record['last_updated']
                ]
            ]);
        } else {
            sendJsonResponse([
                'success' => true,
                'data' => [
                    'user_id' => $userId,
                    'fajr_qaza' => 0,
                    'dhuhr_qaza' => 0,
                    'asr_qaza' => 0,
                    'maghrib_qaza' => 0,
                    'isha_qaza' => 0,
                    'witr_qaza' => 0,
                    'fasting_qaza' => 0,
                    'total_qaza' => 0,
                    'last_updated' => null
                ]
            ]);
        }
    } catch (PDOException $e) {
        sendJsonResponse([
            'success' => false,
            'error' => 'Database error: ' . $e->getMessage()
        ], 500);
    }
}

sendJsonResponse(['error' => 'Method not allowed.'], 405);
