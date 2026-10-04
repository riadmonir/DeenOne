<?php
/**
 * ==============================================================================
 * DEEN ONE - API: UPDATE ROOM STATE (LIFECYCLE & QUESTION PROGRESSION)
 * Endpoint: POST /api/update_room.php
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
$data = getRequestData();

if (empty($data) || empty($data['room_code'])) {
    sendJsonResponse([
        'success' => false,
        'error' => 'ব্যাটেল রুম কোড অনুপস্থিত।'
    ], 400);
}

$roomCode = strtoupper(trim($data['room_code']));
$playerId = isset($data['player_id']) ? trim($data['player_id']) : '';

try {
    $roomStmt = $pdo->prepare("SELECT * FROM battle_rooms WHERE room_code = ?");
    $roomStmt->execute([$roomCode]);
    $room = $roomStmt->fetch();

    if (!$room) {
        sendJsonResponse([
            'success' => false,
            'error' => 'ব্যাটেল রুম পাওয়া যায়নি।'
        ], 404);
    }

    $updates = [];
    $params = [];

    if (isset($data['lifecycle_state'])) {
        $updates[] = "lifecycle_state = ?";
        $params[] = trim($data['lifecycle_state']);
    }

    if (isset($data['current_question_index'])) {
        $updates[] = "current_question_index = ?";
        $params[] = intval($data['current_question_index']);
    }

    if (!empty($updates)) {
        $params[] = $roomCode;
        $sql = "UPDATE battle_rooms SET " . implode(', ', $updates) . ", updated_at = NOW() WHERE room_code = ?";
        $updateStmt = $pdo->prepare($sql);
        $updateStmt->execute($params);
    }

    // Auto-record to battle_history when match finishes (COMPLETED)
    if (isset($data['lifecycle_state']) && strtoupper(trim($data['lifecycle_state'])) === 'COMPLETED') {
        try {
            $checkHistory = $pdo->prepare("SELECT COUNT(*) FROM battle_history WHERE room_code = ?");
            $checkHistory->execute([$roomCode]);
            if ($checkHistory->fetchColumn() == 0) {
                $playersStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? ORDER BY score DESC, correct_answers DESC");
                $playersStmt->execute([$roomCode]);
                $players = $playersStmt->fetchAll(PDO::FETCH_ASSOC);

                $winner = !empty($players) ? $players[0] : null;
                $winnerId = $winner ? $winner['player_id'] : null;
                $winnerName = $winner ? $winner['player_name'] : null;
                $totalPart = max(1, count($players));
                $scoresJson = json_encode($players, JSON_UNESCAPED_UNICODE);

                $insHist = $pdo->prepare("
                    INSERT INTO battle_history (room_code, category_id, winner_player_id, winner_name, total_participants, final_scores_json, finished_at)
                    VALUES (?, ?, ?, ?, ?, ?, NOW())
                ");
                $insHist->execute([$roomCode, $room['category_id'] ?? 'general_knowledge', $winnerId, $winnerName, $totalPart, $scoresJson]);
            }
        } catch (Exception $he) {}
    }

    // Reset selected options when moving to next question if requested
    if (isset($data['reset_player_selections']) && $data['reset_player_selections']) {
        $resetStmt = $pdo->prepare("UPDATE battle_players SET last_selected_option = -1 WHERE room_code = ?");
        $resetStmt->execute([$roomCode]);
    }

    sendJsonResponse([
        'success' => true,
        'message' => 'রুম স্টেট সফলভাবে আপডেট হয়েছে।'
    ]);

} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'আপডেট করতে ব্যর্থ: ' . $e->getMessage()
    ], 500);
}
?>
