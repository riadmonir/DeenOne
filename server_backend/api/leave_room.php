<?php
/**
 * ==============================================================================
 * DEEN ONE - API: LEAVE BATTLE ROOM
 * Endpoint: POST /api/leave_room.php
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
$data = getRequestData();

if (empty($data) || empty($data['room_code']) || empty($data['player_id'])) {
    sendJsonResponse([
        'success' => false,
        'error' => 'ব্যাটেল রুম কোড বা প্লেয়ার আইডি দেওয়া হয়নি।'
    ], 400);
}

$roomCode = strtoupper(trim($data['room_code']));
$playerId = trim($data['player_id']);

try {
    // Check if player is host
    $pStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? AND player_id = ?");
    $pStmt->execute([$roomCode, $playerId]);
    $player = $pStmt->fetch();

    if ($player && $player['is_host']) {
        // Host leaving cancels the room
        $cancelStmt = $pdo->prepare("UPDATE battle_rooms SET lifecycle_state = 'CANCELLED' WHERE room_code = ?");
        $cancelStmt->execute([$roomCode]);

        $delPlayers = $pdo->prepare("DELETE FROM battle_players WHERE room_code = ?");
        $delPlayers->execute([$roomCode]);

        sendJsonResponse([
            'success' => true,
            'message' => 'হোস্ট প্রস্থান করায় ব্যাটেল রুম বাতিল হয়েছে।'
        ]);
    } else {
        // Non-host player leaves
        $delP = $pdo->prepare("DELETE FROM battle_players WHERE room_code = ? AND player_id = ?");
        $delP->execute([$roomCode, $playerId]);

        // Check remaining players
        $cntStmt = $pdo->prepare("SELECT COUNT(*) FROM battle_players WHERE room_code = ?");
        $cntStmt->execute([$roomCode]);
        $remaining = $cntStmt->fetchColumn();

        if ($remaining <= 0) {
            $delRoom = $pdo->prepare("DELETE FROM battle_rooms WHERE room_code = ?");
            $delRoom->execute([$roomCode]);
        }

        sendJsonResponse([
            'success' => true,
            'message' => 'সফলভাবে রুম থেকে প্রস্থান করা হয়েছে।'
        ]);
    }
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'প্রস্থানে ত্রুটি: ' . $e->getMessage()
    ], 500);
}
?>
