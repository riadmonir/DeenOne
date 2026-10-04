<?php
/**
 * ==============================================================================
 * DEEN ONE - API: GET ROOM DETAILS & REAL-TIME POLLING
 * Endpoint: GET/POST /api/get_room.php?room_code=847291&player_id=player_123
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$roomCode = isset($_GET['room_code']) ? strtoupper(trim($_GET['room_code'])) : '';
$playerId = isset($_GET['player_id']) ? trim($_GET['player_id']) : '';

if (empty($roomCode)) {
    $data = getRequestData();
    if (!empty($data['room_code'])) {
        $roomCode = strtoupper(trim($data['room_code']));
    }
    if (!empty($data['player_id'])) {
        $playerId = trim($data['player_id']);
    }
}

if (empty($roomCode)) {
    sendJsonResponse([
        'success' => false,
        'error' => 'ব্যাটেল রুম কোড দেওয়া হয়নি।'
    ], 400);
}

try {
    // 1. Fetch room
    $roomStmt = $pdo->prepare("SELECT * FROM battle_rooms WHERE room_code = ?");
    $roomStmt->execute([$roomCode]);
    $room = $roomStmt->fetch();

    if (!$room) {
        sendJsonResponse([
            'success' => false,
            'error' => 'ভুল কোড! কোনো সক্রিয় ব্যাটেল রুম পাওয়া যায়নি।'
        ], 404);
    }

    // 2. Update requesting player's heartbeat if provided
    if (!empty($playerId)) {
        $hbStmt = $pdo->prepare("UPDATE battle_players SET last_heartbeat = NOW() WHERE room_code = ? AND player_id = ?");
        $hbStmt->execute([$roomCode, $playerId]);
    }

    // 3. Detect and mark disconnected players (heartbeat older than HEARTBEAT_TIMEOUT_SECONDS)
    $timeoutSec = defined('HEARTBEAT_TIMEOUT_SECONDS') ? HEARTBEAT_TIMEOUT_SECONDS : 30;
    $discStmt = $pdo->prepare("
        UPDATE battle_players 
        SET status = 'DISCONNECTED' 
        WHERE room_code = ? 
          AND status != 'DISCONNECTED' 
          AND is_bot = 0 
          AND last_heartbeat < (NOW() - INTERVAL ? SECOND)
    ");
    $discStmt->execute([$roomCode, $timeoutSec]);

    // 4. Fetch all players
    $playersStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? ORDER BY is_host DESC, score DESC, id ASC");
    $playersStmt->execute([$roomCode]);
    $players = $playersStmt->fetchAll();

    $formattedPlayers = [];
    $hostPlayerObj = null;

    foreach ($players as $p) {
        $pObj = [
            'id' => $p['player_id'],
            'name' => $p['player_name'],
            'avatar_resource' => $p['avatar_resource'],
            'is_host' => (bool)$p['is_host'],
            'is_bot' => (bool)$p['is_bot'],
            'is_ready' => (bool)$p['is_ready'],
            'score' => intval($p['score']),
            'correct_answers' => intval($p['correct_answers']),
            'wrong_answers' => intval($p['wrong_answers']),
            'status' => $p['status'],
            'current_selected_option' => intval($p['last_selected_option'])
        ];
        $formattedPlayers[] = $pObj;
        if ($p['is_host']) {
            $hostPlayerObj = $pObj;
        }
    }

    // 5. Decode questions
    $questionsDecoded = json_decode($room['questions_data'], true);
    if (!is_array($questionsDecoded)) {
        $questionsDecoded = [];
    }

    sendJsonResponse([
        'success' => true,
        'room_code' => $roomCode,
        'room' => [
            'room_code' => $roomCode,
            'category_id' => $room['category_id'],
            'category_title_bn' => $room['category_title_bn'],
            'total_players' => intval($room['total_players']),
            'total_questions' => intval($room['total_questions']),
            'time_per_question_sec' => intval($room['time_per_question_sec']),
            'difficulty' => $room['difficulty'],
            'negative_marking' => (bool)$room['negative_marking'],
            'lifecycle_state' => $room['lifecycle_state'],
            'current_question_index' => intval($room['current_question_index']),
            'host_player' => $hostPlayerObj,
            'players' => $formattedPlayers,
            'questions' => $questionsDecoded
        ]
    ]);

} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'রুম ডেটা লোড করতে ব্যর্থ: ' . $e->getMessage()
    ], 500);
}
?>
