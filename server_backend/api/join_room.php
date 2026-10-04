<?php
/**
 * ==============================================================================
 * DEEN ONE - API: JOIN BATTLE ROOM
 * Endpoint: POST /api/join_room.php
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
$data = getRequestData();

if (empty($data)) {
    sendJsonResponse([
        'success' => false,
        'error' => 'অনুরোধে কোনো ডেটা পাওয়া যায়নি।'
    ], 400);
}

$roomCode = isset($data['room_code']) ? strtoupper(trim($data['room_code'])) : '';
if (empty($roomCode)) {
    sendJsonResponse([
        'success' => false,
        'error' => 'ব্যাটেল রুমের কোড দেওয়া হয়নি।'
    ], 400);
}

$player = isset($data['player']) ? $data['player'] : null;
if (!$player && (!empty($data['user_id']) || !empty($data['username']))) {
    $player = [
        'id' => $data['user_id'] ?? 'usr_guest',
        'name' => $data['username'] ?? 'খেলোয়াড়',
        'avatar_resource' => $data['avatar'] ?? $data['avatar_resource'] ?? 'avatar_1'
    ];
}

if (!$player || empty($player['id']) || empty($player['name'])) {
    sendJsonResponse([
        'success' => false,
        'error' => 'প্লেয়ারের তথ্য অনুপস্থিত।'
    ], 400);
}

$playerId = trim($player['id']);
$playerName = trim($player['name']);
$playerAvatar = isset($player['avatar_resource']) ? trim($player['avatar_resource']) : 'avatar_1';

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

    // 2. Validate room lifecycle
    if ($room['lifecycle_state'] === 'CANCELLED') {
        sendJsonResponse([
            'success' => false,
            'error' => 'এই ব্যাটেল রুমটি বাতিল করা হয়েছে।'
        ], 400);
    }

    if ($room['lifecycle_state'] === 'PODIUM_FINISHED') {
        sendJsonResponse([
            'success' => false,
            'error' => 'এই ব্যাটেলটি ইতিমধ্যে শেষ হয়ে গেছে।'
        ], 400);
    }

    // 3. Fetch existing players
    $playersStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? ORDER BY is_host DESC, id ASC");
    $playersStmt->execute([$roomCode]);
    $existingPlayers = $playersStmt->fetchAll();

    $isAlreadyInRoom = false;
    foreach ($existingPlayers as $p) {
        if (strcasecmp($p['player_id'], $playerId) === 0) {
            $isAlreadyInRoom = true;
            break;
        }
    }

    // 4. Capacity check
    if (!$isAlreadyInRoom) {
        if (count($existingPlayers) >= intval($room['total_players'])) {
            sendJsonResponse([
                'success' => false,
                'error' => 'রুমটি পূর্ণ হয়ে গেছে! অন্য কোড দিয়ে চেষ্টা করুন।'
            ], 400);
        }

        // Insert new player
        $insertPlayer = $pdo->prepare("
            INSERT INTO battle_players (
                room_code, player_id, player_name, avatar_resource,
                is_host, is_bot, is_ready, score, correct_answers, wrong_answers, status, last_heartbeat
            ) VALUES (
                ?, ?, ?, ?, 0, 0, 1, 0, 0, 0, 'READY', NOW()
            )
            ON DUPLICATE KEY UPDATE
                player_name = VALUES(player_name),
                avatar_resource = VALUES(avatar_resource),
                last_heartbeat = NOW()
        ");
        $insertPlayer->execute([$roomCode, $playerId, $playerName, $playerAvatar]);

        // Refresh player list
        $playersStmt->execute([$roomCode]);
        $existingPlayers = $playersStmt->fetchAll();

        // Update lifecycle if capacity reached
        if (count($existingPlayers) >= intval($room['total_players'])) {
            $updateLifecycle = $pdo->prepare("
                UPDATE battle_rooms 
                SET lifecycle_state = 'PLAYERS_JOINED' 
                WHERE room_code = ? AND lifecycle_state = 'WAITING_FOR_PLAYERS'
            ");
            $updateLifecycle->execute([$roomCode]);
            $room['lifecycle_state'] = 'PLAYERS_JOINED';
        }
    } else {
        // Update heartbeat
        $updateHeartbeat = $pdo->prepare("UPDATE battle_players SET last_heartbeat = NOW() WHERE room_code = ? AND player_id = ?");
        $updateHeartbeat->execute([$roomCode, $playerId]);
    }

    // 5. Format players response
    $formattedPlayers = [];
    $hostPlayerObj = null;

    foreach ($existingPlayers as $p) {
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
            'status' => $p['status']
        ];
        $formattedPlayers[] = $pObj;
        if ($p['is_host']) {
            $hostPlayerObj = $pObj;
        }
    }

    // Decode questions data
    $questionsDecoded = json_decode($room['questions_data'], true);
    if (!is_array($questionsDecoded)) {
        $questionsDecoded = [];
    }

    sendJsonResponse([
        'success' => true,
        'message' => 'রুমে সফলভাবে যোগদান সম্পন্ন হয়েছে।',
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
        'error' => 'রুমে যোগদানে ত্রুটি ঘটেছে: ' . $e->getMessage()
    ], 500);
}
?>
