<?php
/**
 * DeenOne Real-Time Knowledge Battle Backend API
 * Handles all MySQL Database transactions, Room Creation, Join Validation,
 * Live Synchronized State, Answer Validation, and Reconnection Management.
 */

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// -------------------------------------------------------------
// Database Configuration
// -------------------------------------------------------------
$DB_HOST = 'localhost';
$DB_NAME = 'deenone_battle_db';
$DB_USER = 'root';
$DB_PASS = '';

try {
    $pdo = new PDO("mysql:host=$DB_HOST;dbname=$DB_NAME;charset=utf8mb4", $DB_USER, $DB_PASS, [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES => false,
    ]);
} catch (PDOException $e) {
    echo json_encode([
        'status' => 'ERROR',
        'message' => 'Database connection failed: ' . $e->getMessage()
    ]);
    exit;
}

$action = $_GET['action'] ?? ($_POST['action'] ?? '');
$rawInput = file_get_contents('php://input');
$data = json_decode($rawInput, true) ?? $_POST;

switch ($action) {
    // ---------------------------------------------------------
    // 1. ACTION: create_battle
    // ---------------------------------------------------------
    case 'create_battle':
        $userId = $data['user_id'] ?? '';
        $username = $data['username'] ?? 'Anonymous';
        $mode = $data['mode'] ?? '1v1';
        $maxPlayers = intval($data['max_players'] ?? 2);
        $qCount = intval($data['question_count'] ?? 10);
        $qTime = intval($data['question_time'] ?? 15);
        $categoryId = $data['category_id'] ?? 'salat_taharah';
        $difficulty = $data['difficulty'] ?? 'MEDIUM';
        $negMarking = intval($data['negative_marking'] ?? 0);

        if (empty($userId)) {
            echo json_encode(['status' => 'ERROR', 'message' => 'Missing user_id']);
            exit;
        }

        // Upsert User
        $stmtUser = $pdo->prepare("INSERT INTO users (user_id, username) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?");
        $stmtUser->execute([$userId, $username, $username]);

        // Generate 6-character Unique Code
        $code = generateUniqueBattleCode($pdo);
        $battleId = 'BAT_' . bin2hex(random_bytes(8));

        $stmtBattle = $pdo->prepare("
            INSERT INTO battles (battle_id, battle_code, creator_id, mode, max_players, question_count, question_time, category_id, difficulty, status, negative_marking)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'WAITING_FOR_PLAYERS', ?)
        ");
        $stmtBattle->execute([$battleId, $code, $userId, $mode, $maxPlayers, $qCount, $qTime, $categoryId, $difficulty, $negMarking]);

        // Add Host Player
        $bpId = 'BP_' . bin2hex(random_bytes(8));
        $sessionToken = 'SES_' . bin2hex(random_bytes(16));
        $stmtBp = $pdo->prepare("
            INSERT INTO battle_players (battle_player_id, battle_id, user_id, ready_status, connection_status, session_token)
            VALUES (?, ?, ?, 'READY', 'JOINED', ?)
        ");
        $stmtBp->execute([$bpId, $battleId, $userId, $sessionToken]);

        echo json_encode([
            'status' => 'SUCCESS',
            'battle_id' => $battleId,
            'battle_code' => $code,
            'session_token' => $sessionToken,
            'lifecycle_state' => 'WAITING_FOR_PLAYERS'
        ]);
        break;

    // ---------------------------------------------------------
    // 2. ACTION: join_battle
    // ---------------------------------------------------------
    case 'join_battle':
        $code = strtoupper(trim($data['battle_code'] ?? ''));
        $userId = $data['user_id'] ?? '';
        $username = $data['username'] ?? 'Guest';

        if (empty($code) || empty($userId)) {
            echo json_encode(['status' => 'ERROR', 'message' => 'Invalid parameters']);
            exit;
        }

        // Upsert User
        $stmtUser = $pdo->prepare("INSERT INTO users (user_id, username) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?");
        $stmtUser->execute([$userId, $username, $username]);

        // Find Room
        $stmtFind = $pdo->prepare("SELECT * FROM battles WHERE battle_code = ?");
        $stmtFind->execute([$code]);
        $battle = $stmtFind->fetch();

        if (!$battle) {
            echo json_encode(['status' => 'INVALID_CODE', 'message' => 'ব্যাটেল কোডটি সঠিক নয়!']);
            exit;
        }

        if (!in_array($battle['status'], ['CREATED', 'WAITING_FOR_PLAYERS', 'PLAYERS_JOINED', 'READY_CHECK'])) {
            echo json_encode(['status' => 'ALREADY_STARTED', 'message' => 'ব্যাটেল ইতোমধ্যে শুরু হয়ে গেছে!']);
            exit;
        }

        // Check Player Count
        $stmtCount = $pdo->prepare("SELECT COUNT(*) as cnt FROM battle_players WHERE battle_id = ?");
        $stmtCount->execute([$battle['battle_id']]);
        $currentCount = intval($stmtCount->fetch()['cnt']);

        if ($currentCount >= $battle['max_players']) {
            echo json_encode(['status' => 'ROOM_FULL', 'message' => 'রুমের খেলোয়াড় সংখ্যা পূর্ণ!']);
            exit;
        }

        // Add Player
        $bpId = 'BP_' . bin2hex(random_bytes(8));
        $sessionToken = 'SES_' . bin2hex(random_bytes(16));
        $stmtJoin = $pdo->prepare("
            INSERT INTO battle_players (battle_player_id, battle_id, user_id, ready_status, connection_status, session_token)
            VALUES (?, ?, ?, 'READY', 'JOINED', ?)
            ON DUPLICATE KEY UPDATE connection_status = 'RECONNECTED'
        ");
        $stmtJoin->execute([$bpId, $battle['battle_id'], $userId, $sessionToken]);

        echo json_encode([
            'status' => 'SUCCESS',
            'battle' => $battle,
            'session_token' => $sessionToken,
            'message' => 'সফলভাবে যোগ দেওয়া হয়েছে'
        ]);
        break;

    // ---------------------------------------------------------
    // 3. ACTION: submit_answer
    // ---------------------------------------------------------
    case 'submit_answer':
        $battleId = $data['battle_id'] ?? '';
        $questionId = $data['question_id'] ?? '';
        $userId = $data['user_id'] ?? '';
        $selectedAnswer = intval($data['selected_answer'] ?? -1);
        $responseTimeMs = intval($data['response_time_ms'] ?? 0);

        if (empty($battleId) || empty($questionId) || empty($userId)) {
            echo json_encode(['status' => 'ERROR', 'message' => 'Missing submission data']);
            exit;
        }

        // Double submit check
        $stmtCheck = $pdo->prepare("SELECT answer_id FROM answers WHERE battle_id = ? AND question_id = ? AND user_id = ?");
        $stmtCheck->execute([$battleId, $questionId, $userId]);
        if ($stmtCheck->fetch()) {
            echo json_encode(['status' => 'DUPLICATE_SUBMISSION', 'message' => 'Answer already submitted']);
            exit;
        }

        // Query Correct Answer
        $stmtQ = $pdo->prepare("SELECT correct_answer, explanation, source FROM questions WHERE question_id = ?");
        $stmtQ->execute([$questionId]);
        $qData = $stmtQ->fetch();

        $isCorrect = ($qData && intval($qData['correct_answer']) === $selectedAnswer) ? 1 : 0;
        $pointsDelta = $isCorrect ? 10 : 0;

        // Record Answer
        $ansId = 'ANS_' . bin2hex(random_bytes(8));
        $stmtAns = $pdo->prepare("
            INSERT INTO answers (answer_id, battle_id, question_id, user_id, selected_answer, is_correct, response_time_ms, points_delta)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ");
        $stmtAns->execute([$ansId, $battleId, $questionId, $userId, $selectedAnswer, $isCorrect, $responseTimeMs, $pointsDelta]);

        // Update Player Score
        if ($isCorrect) {
            $stmtUpd = $pdo->prepare("UPDATE battle_players SET score = score + ?, correct_answers = correct_answers + 1 WHERE battle_id = ? AND user_id = ?");
        } else {
            $stmtUpd = $pdo->prepare("UPDATE battle_players SET wrong_answers = wrong_answers + 1 WHERE battle_id = ? AND user_id = ?");
        }
        $stmtUpd->execute([$pointsDelta, $battleId, $userId]);

        echo json_encode([
            'status' => 'VALIDATED',
            'is_correct' => ($isCorrect === 1),
            'correct_option_index' => intval($qData['correct_answer'] ?? 0),
            'points_delta' => $pointsDelta,
            'explanation' => $qData['explanation'] ?? '',
            'source' => $qData['source'] ?? ''
        ]);
        break;

    default:
        echo json_encode(['status' => 'READY', 'service' => 'DeenOne Knowledge Battle PHP/MySQL Backend']);
        break;
}

function generateUniqueBattleCode($pdo) {
    $chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    for ($attempt = 0; $attempt < 10; $attempt++) {
        $code = '';
        for ($i = 0; $i < 6; $i++) {
            $code .= $chars[random_int(0, strlen($chars) - 1)];
        }
        $stmt = $pdo->prepare("SELECT battle_id FROM battles WHERE battle_code = ?");
        $stmt->execute([$code]);
        if (!$stmt->fetch()) {
            return $code;
        }
    }
    return 'D' . substr(strval(time()), -5);
}
?>
