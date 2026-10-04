<?php
/**
 * ==============================================================================
 * DEEN ONE - API: SUBMIT PLAYER ANSWER & SCORE UPDATE
 * Endpoint: POST /api/submit_answer.php
 * ==============================================================================
 */

require_once __DIR__ . '/db.php';

$pdo = getDbConnection();
$data = getRequestData();

if (empty($data) || empty($data['room_code']) || empty($data['player_id'])) {
    sendJsonResponse([
        'success' => false,
        'error' => 'প্রয়োজনীয় ফিল্ডগুলো পূরণ করা হয়নি।'
    ], 400);
}

$roomCode = strtoupper(trim($data['room_code']));
$playerId = trim($data['player_id']);
$questionIndex = isset($data['question_index']) ? intval($data['question_index']) : 0;
$selectedOption = isset($data['selected_option']) ? intval($data['selected_option']) : -1;
$isCorrect = !empty($data['is_correct']) ? 1 : 0;
$pointsEarned = isset($data['points_earned']) ? intval($data['points_earned']) : ($isCorrect ? 10 : 0);

try {
    // 1. Fetch current player record
    $playerStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? AND player_id = ?");
    $playerStmt->execute([$roomCode, $playerId]);
    $player = $playerStmt->fetch();

    if (!$player) {
        sendJsonResponse([
            'success' => false,
            'error' => 'প্লেয়ার পাওয়া যায়নি।'
        ], 404);
    }

    // 2. Update player's score and statistics
    $correctInc = $isCorrect ? 1 : 0;
    $wrongInc = (!$isCorrect && $selectedOption >= 0) ? 1 : 0;

    $updateStmt = $pdo->prepare("
        UPDATE battle_players 
        SET score = GREATEST(0, score + ?),
            correct_answers = correct_answers + ?,
            wrong_answers = wrong_answers + ?,
            last_selected_option = ?,
            last_heartbeat = NOW()
        WHERE room_code = ? AND player_id = ?
    ");
    $updateStmt->execute([$pointsEarned, $correctInc, $wrongInc, $selectedOption, $roomCode, $playerId]);

    // 3. Log answer in battle_rounds
    $roundStmt = $pdo->prepare("
        INSERT INTO battle_rounds (
            room_code, round_number, player_id, selected_option, is_correct, points_earned, created_at
        ) VALUES (?, ?, ?, ?, ?, ?, NOW())
    ");
    $roundStmt->execute([$roomCode, $questionIndex, $playerId, $selectedOption, $isCorrect, $pointsEarned]);

    // 4. Return new score
    $playerStmt->execute([$roomCode, $playerId]);
    $updatedPlayer = $playerStmt->fetch();

    sendJsonResponse([
        'success' => true,
        'message' => 'উত্তর সফলভাবে রেকর্ড করা হয়েছে।',
        'current_score' => intval($updatedPlayer['score']),
        'correct_answers' => intval($updatedPlayer['correct_answers']),
        'wrong_answers' => intval($updatedPlayer['wrong_answers'])
    ]);

} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'উত্তর সংরক্ষণ করতে ব্যর্থ: ' . $e->getMessage()
    ], 500);
}
?>
