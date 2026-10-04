<?php
/**
 * ==============================================================================
 * DEEN ONE (দীন ওয়ান) - DATABASE HELPER & API UTILITIES
 * ==============================================================================
 */

// Enable CORS for Android App & Web requests
if (!headers_sent()) {
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');
}

// Handle preflight OPTIONS request
if (($_SERVER['REQUEST_METHOD'] ?? '') === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Load main configuration
$configPath = dirname(__DIR__) . '/config.php';
if (!file_exists($configPath)) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'error' => 'Configuration file config.php missing.'
    ], JSON_UNESCAPED_UNICODE);
    exit();
}
require_once $configPath;

/**
 * Returns a singleton PDO connection
 */
function getDbConnection() {
    static $pdo = null;
    if ($pdo === null) {
        $options = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
            PDO::ATTR_TIMEOUT            => 2,
            PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES " . DB_CHARSET
        ];

        $credentialPairs = [
            ['user' => DB_USER, 'pass' => DB_PASS]
        ];
        // If testing locally, also allow local root fallback if production credentials fail
        if (in_array(DB_HOST, ['localhost', '127.0.0.1', '::1'])) {
            $credentialPairs[] = ['user' => 'root', 'pass' => ''];
        }

        $candidates = array_unique([DB_NAME, 'deenonet_db', 'deenone_db', 'deenone']);
        $lastErr = null;

        foreach ($credentialPairs as $cred) {
            foreach ($candidates as $candidateDb) {
                try {
                    $dsn = "mysql:host=" . DB_HOST . ";dbname=" . $candidateDb . ";charset=" . DB_CHARSET;
                    $pdo = new PDO($dsn, $cred['user'], $cred['pass'], $options);
                    break 2;
                } catch (PDOException $e) {
                    $lastErr = $e;
                }
            }
        }

        if ($pdo === null) {
            throw new PDOException('Database connection failed: ' . ($lastErr ? $lastErr->getMessage() : 'Unknown database'));
        }
    }
    return $pdo;
}

/**
 * Helper to safely parse JSON or Form POST input
 */
function getRequestData() {
    $raw = file_get_contents('php://input');
    if (!empty($raw)) {
        $json = json_decode($raw, true);
        if (is_array($json)) {
            return $json;
        }
    }
    return $_POST;
}

/**
 * Helper to send JSON response and exit
 */
function sendJsonResponse($data, $statusCode = 200) {
    if (!headers_sent()) {
        header('Content-Type: application/json; charset=UTF-8');
    }
    http_response_code($statusCode);
    echo json_encode($data, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
    exit();
}

/**
 * Clean up expired rooms older than 20 minutes or cancelled
 */
if (!function_exists('cleanupExpiredRooms')) {
    function cleanupExpiredRooms($pdo) {
        try {
            $stmt = $pdo->prepare("DELETE FROM battle_rooms WHERE lifecycle_state = 'CANCELLED' OR (lifecycle_state = 'WAITING_FOR_PLAYERS' AND created_at < (NOW() - INTERVAL 20 MINUTE))");
            $stmt->execute();
        } catch (Exception $e) {
            // Silently ignore cleanup errors
        }
    }
}

/**
 * Ensures user exists in users table and updates last_active
 */
function syncUser($pdo, $userId, $name, $avatar = 'avatar_1', $district = null, $phone = null) {
    if (empty($userId) || empty($name)) return;
    try {
        $stmt = $pdo->prepare("INSERT INTO users (user_id, name, avatar, district, phone, last_active)
            VALUES (?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE 
                name = VALUES(name),
                avatar = IF(VALUES(avatar) IS NOT NULL, VALUES(avatar), avatar),
                district = IF(VALUES(district) IS NOT NULL, VALUES(district), district),
                phone = IF(VALUES(phone) IS NOT NULL, VALUES(phone), phone),
                last_active = NOW()");
        $stmt->execute([$userId, $name, $avatar, $district, $phone]);
    } catch (Exception $e) {
        // Silently ignore user sync error
    }
}

/**
 * Awards points and updates battle stats
 */
function awardUserPoints($pdo, $userId, $pointsToAdd, $wonBattle = false) {
    if (empty($userId) || $pointsToAdd < 0) return;
    try {
        $wonInc = $wonBattle ? 1 : 0;
        $stmt = $pdo->prepare("UPDATE users SET 
            total_points = total_points + ?,
            battles_played = battles_played + 1,
            battles_won = battles_won + ?,
            last_active = NOW()
            WHERE user_id = ?");
        $stmt->execute([$pointsToAdd, $wonInc, $userId]);
    } catch (Exception $e) {
        // Silently ignore
    }
}

if (!function_exists('logAdminActivity')) {
    function logAdminActivity($pdo, $adminId, $action, $targetType = null, $targetId = null, $details = null) {
        try {
            $ip = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
            $stmt = $pdo->prepare("INSERT INTO admin_logs (admin_id, action, target_type, target_id, details, ip_address)
                VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->execute([$adminId, $action, $targetType, $targetId, $details, $ip]);
        } catch (Exception $e) {
            // Silently ignore
        }
    }
}
?>
