<?php
/**
 * ==============================================================================
 * DEEN ONE - API: CREATE BATTLE ROOM
 * Endpoint: POST /api/create_room.php
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
cleanupExpiredRooms($pdo);

$data = getRequestData();

if (empty($data)) {
    sendJsonResponse([
        'success' => false,
        'error' => 'অনুরোধে কোনো ডেটা পাওয়া যায়নি।'
    ], 400);
}

// 1. Generate or validate room code
$roomCode = isset($data['room_code']) ? strtoupper(trim($data['room_code'])) : '';
if (empty($roomCode)) {
    // Generate a 6-digit unique numeric code
    $attempts = 0;
    do {
        $roomCode = str_pad(mt_rand(100000, 999999), 6, '0', STR_PAD_LEFT);
        $checkStmt = $pdo->prepare("SELECT COUNT(*) FROM battle_rooms WHERE room_code = ?");
        $checkStmt->execute([$roomCode]);
        $exists = $checkStmt->fetchColumn() > 0;
        $attempts++;
    } while ($exists && $attempts < 10);
}

// 2. Validate host player
$host = isset($data['host_player']) ? $data['host_player'] : null;
if (!$host && (!empty($data['user_id']) || !empty($data['username']))) {
    $host = [
        'id' => $data['user_id'] ?? 'usr_host',
        'name' => $data['username'] ?? 'হোস্ট',
        'avatar_resource' => $data['avatar'] ?? $data['avatar_resource'] ?? 'avatar_1'
    ];
}

if (!$host || empty($host['id']) || empty($host['name'])) {
    sendJsonResponse([
        'success' => false,
        'error' => 'হোস্ট প্লেয়ারের তথ্য অনুপস্থিত।'
    ], 400);
}

$hostId = trim($host['id']);
$hostName = trim($host['name']);
$hostAvatar = isset($host['avatar_resource']) ? trim($host['avatar_resource']) : 'avatar_1';

// 3. Extract battle settings
$categoryId = isset($data['category_id']) ? trim($data['category_id']) : 'general_knowledge';
$categoryTitleBn = isset($data['category_title_bn']) ? trim($data['category_title_bn']) : 'সাধারণ জ্ঞান (ইসলাম)';
$totalPlayers = isset($data['total_players']) ? intval($data['total_players']) : 2;
if ($totalPlayers < 2) $totalPlayers = 2;
if ($totalPlayers > 4) $totalPlayers = 4;

$totalQuestions = isset($data['total_questions']) ? intval($data['total_questions']) : 10;
$timePerQuestion = isset($data['time_per_question_sec']) ? intval($data['time_per_question_sec']) : 15;
$difficulty = isset($data['difficulty']) ? trim($data['difficulty']) : 'MEDIUM';
$negativeMarking = !empty($data['negative_marking']) ? 1 : 0;

// 4. Questions payload (compact question IDs + permutations or ad-hoc array)
$questionsData = isset($data['questions']) ? (is_string($data['questions']) ? $data['questions'] : json_encode($data['questions'], JSON_UNESCAPED_UNICODE)) : '[]';

try {
    $pdo->beginTransaction();

    // Check if room already exists; if so, replace/clean
    $delRoom = $pdo->prepare("DELETE FROM battle_rooms WHERE room_code = ?");
    $delRoom->execute([$roomCode]);

    // Insert into battle_rooms
    $roomStmt = $pdo->prepare("
        INSERT INTO battle_rooms (
            room_code, host_player_id, category_id, category_title_bn,
            total_players, total_questions, time_per_question_sec, difficulty,
            negative_marking, lifecycle_state, current_question_index, questions_data, created_at
        ) VALUES (
            ?, ?, ?, ?, ?, ?, ?, ?, ?, 'WAITING_FOR_PLAYERS', 0, ?, NOW()
        )
    ");
    $roomStmt->execute([
        $roomCode, $hostId, $categoryId, $categoryTitleBn,
        $totalPlayers, $totalQuestions, $timePerQuestion, $difficulty,
        $negativeMarking, $questionsData
    ]);

    // Insert host into battle_players
    $playerStmt = $pdo->prepare("
        INSERT INTO battle_players (
            room_code, player_id, player_name, avatar_resource,
            is_host, is_bot, is_ready, score, correct_answers, wrong_answers, status
        ) VALUES (
            ?, ?, ?, ?, 1, 0, 1, 0, 0, 0, 'READY'
        )
    ");
    $playerStmt->execute([$roomCode, $hostId, $hostName, $hostAvatar]);

    $pdo->commit();

    sendJsonResponse([
        'success' => true,
        'message' => 'ব্যাটেল রুম সফলভাবে তৈরি করা হয়েছে।',
        'room_code' => $roomCode,
        'room' => [
            'room_code' => $roomCode,
            'category_id' => $categoryId,
            'category_title_bn' => $categoryTitleBn,
            'total_players' => $totalPlayers,
            'total_questions' => $totalQuestions,
            'time_per_question_sec' => $timePerQuestion,
            'difficulty' => $difficulty,
            'lifecycle_state' => 'WAITING_FOR_PLAYERS',
            'current_question_index' => 0,
            'players' => [
                [
                    'id' => $hostId,
                    'name' => $hostName,
                    'avatar_resource' => $hostAvatar,
                    'is_host' => true,
                    'is_ready' => true,
                    'score' => 0,
                    'correct_answers' => 0,
                    'wrong_answers' => 0,
                    'status' => 'READY'
                ]
            ]
        ]
    ]);
} catch (Exception $e) {
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }
    sendJsonResponse([
        'success' => false,
        'error' => 'রুম তৈরি করতে সমস্যা হয়েছে: ' . $e->getMessage()
    ], 500);
}
?>
