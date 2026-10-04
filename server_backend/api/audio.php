<?php
/**
 * ==============================================================================
 * DEEN ONE API - ISLAMIC AUDIO HUB (ইসলামিক অডিও হাব)
 * Strict Category Separation: Waz, Bayan, Lectures, Dua, Azkar, Tafsir, Seerah
 * (CRITICAL: Quran recitations are strictly excluded)
 * ==============================================================================
 */
header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With, X-API-KEY');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/../config/database.php';

$pdo = getDbConnection();

// Ensure islamic_audios table exists
ensureIslamicAudioTableExists($pdo);

$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'POST') {
    handlePostRequest($pdo);
} else {
    handleGetRequest($pdo);
}

function ensureIslamicAudioTableExists($pdo) {
    $sql = "CREATE TABLE IF NOT EXISTS `islamic_audios` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `audio_id` VARCHAR(100) NOT NULL UNIQUE,
      `title` VARCHAR(255) NOT NULL,
      `speaker` VARCHAR(150) NOT NULL,
      `category` VARCHAR(60) NOT NULL DEFAULT 'waz',
      `duration_seconds` INT UNSIGNED NOT NULL DEFAULT 0,
      `audio_url` TEXT NOT NULL,
      `description` TEXT DEFAULT NULL,
      `reference` VARCHAR(255) DEFAULT NULL,
      `source` VARCHAR(100) DEFAULT 'DeenOne Studio',
      `is_active` TINYINT(1) DEFAULT 1,
      `display_order` INT UNSIGNED DEFAULT 1,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_audio_cat` (`category`),
      INDEX `idx_audio_active` (`is_active`),
      INDEX `idx_audio_order` (`display_order`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";
    $pdo->exec($sql);
}

function handleGetRequest($pdo) {
    $search = trim($_GET['search'] ?? '');
    $category = trim($_GET['category'] ?? 'ALL');
    $limit = isset($_GET['limit']) ? min(500, max(1, (int)$_GET['limit'])) : 100;
    $offset = isset($_GET['offset']) ? max(0, (int)$_GET['offset']) : 0;

    $where = ["is_active = 1"];
    $params = [];

    if ($category !== '' && strtoupper($category) !== 'ALL') {
        $where[] = "category = ?";
        $params[] = strtolower($category);
    }

    if ($search !== '') {
        $where[] = "(title LIKE ? OR speaker LIKE ? OR description LIKE ? OR reference LIKE ?)";
        $searchTerm = "%" . $search . "%";
        $params[] = $searchTerm;
        $params[] = $searchTerm;
        $params[] = $searchTerm;
        $params[] = $searchTerm;
    }

    $whereClause = implode(" AND ", $where);
    $stmt = $pdo->prepare("SELECT * FROM islamic_audios WHERE $whereClause ORDER BY display_order ASC, id DESC LIMIT $limit OFFSET $offset");
    $stmt->execute($params);
    $audios = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Format for app
    $items = [];
    foreach ($audios as $row) {
        $items[] = [
            'id' => $row['audio_id'],
            'title' => $row['title'],
            'speaker' => $row['speaker'],
            'category' => $row['category'],
            'duration' => (int)$row['duration_seconds'],
            'audioUrl' => $row['audio_url'],
            'description' => $row['description'] ?? '',
            'reference' => $row['reference'] ?? '',
            'source' => $row['source'] ?? 'DeenOne Studio'
        ];
    }

    echo json_encode([
        'status' => 'success',
        'count' => count($items),
        'data' => $items
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
}

function handlePostRequest($pdo) {
    $raw = file_get_contents('php://input');
    $data = json_decode($raw, true);

    if (!$data || empty($data['title']) || empty($data['audio_url'])) {
        echo json_encode(['status' => 'error', 'message' => 'Title and Audio URL are required']);
        return;
    }

    $audioId = $data['audio_id'] ?? ('aud_' . uniqid());
    $title = trim($data['title']);
    $speaker = trim($data['speaker'] ?? 'DeenOne Scholar');
    $category = strtolower(trim($data['category'] ?? 'waz'));
    $duration = (int)($data['duration_seconds'] ?? 0);
    $audioUrl = trim($data['audio_url']);
    $description = trim($data['description'] ?? '');
    $reference = trim($data['reference'] ?? '');
    $source = trim($data['source'] ?? 'DeenOne Studio');
    $isActive = isset($data['is_active']) ? (int)$data['is_active'] : 1;

    $stmt = $pdo->prepare("INSERT INTO islamic_audios 
        (audio_id, title, speaker, category, duration_seconds, audio_url, description, reference, source, is_active)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
        title=VALUES(title), speaker=VALUES(speaker), category=VALUES(category),
        duration_seconds=VALUES(duration_seconds), audio_url=VALUES(audio_url),
        description=VALUES(description), reference=VALUES(reference),
        source=VALUES(source), is_active=VALUES(is_active)");

    $stmt->execute([$audioId, $title, $speaker, $category, $duration, $audioUrl, $description, $reference, $source, $isActive]);

    echo json_encode(['status' => 'success', 'message' => 'Audio saved successfully', 'audio_id' => $audioId]);
}
