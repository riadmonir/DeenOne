<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - AUTHENTICATION, SESSION & SECURITY GUARD
 * ==============================================================================
 */

// Start output buffering immediately to prevent any header sent issues
if (!ob_get_level()) {
    ob_start();
}

// Load main configuration and database helper
require_once dirname(__DIR__) . '/config.php';
require_once dirname(__DIR__) . '/api/db.php';

// Configure hardened session security before session starts
if (session_status() === PHP_SESSION_NONE) {
    ini_set('session.cookie_httponly', 1);
    ini_set('session.use_only_cookies', 1);
    ini_set('session.cookie_samesite', 'Strict');
    if (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') {
        ini_set('session.cookie_secure', 1);
    }
    session_name(ADMIN_SESSION_NAME);
    session_start();
}
if (!headers_sent()) {
    header('Content-Type: text/html; charset=UTF-8');
}

// Load Localization & Islamic Engine
require_once __DIR__ . '/lang.php';

/**
 * Get client IP address reliably across Cloudflare, reverse proxies and direct connections
 */
function getAdminClientIp() {
    if (!empty($_SERVER['HTTP_CF_CONNECTING_IP'])) {
        return filter_var($_SERVER['HTTP_CF_CONNECTING_IP'], FILTER_VALIDATE_IP) ?: '127.0.0.1';
    }
    if (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
        $ips = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);
        $ip = trim($ips[0]);
        if (filter_var($ip, FILTER_VALIDATE_IP)) {
            return $ip;
        }
    }
    return $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
}

/**
 * Universal safe redirect helper that works with or without output buffering
 */
function redirect($url) {
    if (ob_get_length()) {
        ob_end_clean();
    }
    if (!headers_sent()) {
        header("Location: $url");
        exit();
    } else {
        echo "<script>window.location.href=" . json_encode($url) . ";</script>";
        echo "<noscript><meta http-equiv='refresh' content='0;url=" . htmlspecialchars($url) . "'></noscript>";
        exit();
    }
}

/**
 * Checks if current session is an authenticated admin with anti-hijacking validation
 */
function isAdminLoggedIn() {
    if (!isset($_SESSION['admin_logged_in']) || $_SESSION['admin_logged_in'] !== true || empty($_SESSION['admin_id'])) {
        return false;
    }
    
    // Anti-Session Hijacking verification
    $currentUaHash = hash('sha256', $_SERVER['HTTP_USER_AGENT'] ?? 'DEFAULT_UA');
    if (isset($_SESSION['admin_ua_hash']) && $_SESSION['admin_ua_hash'] !== $currentUaHash) {
        // User-agent mismatch detected; invalidate session
        $_SESSION = [];
        if (session_id()) session_destroy();
        return false;
    }
    
    return true;
}

/**
 * Enforces admin login; redirects to login page if unauthenticated
 */
function requireAdminLogin() {
    if (!isAdminLoggedIn()) {
        header('Location: index.php');
        exit();
    }
}

/**
 * Check brute-force login attempts (Max 5 failed attempts in 15 minutes)
 */
function checkBruteForceLockout($pdo, $ip) {
    try {
        $stmt = $pdo->prepare("SELECT COUNT(*) as failed_count, MAX(created_at) as last_attempt FROM admin_logs WHERE ip_address = ? AND action = 'LOGIN_FAILED' AND created_at > (NOW() - INTERVAL 15 MINUTE)");
        $stmt->execute([$ip]);
        $row = $stmt->fetch();
        
        $failedCount = (int)($row['failed_count'] ?? 0);
        $maxAttempts = 5;
        
        if ($failedCount >= $maxAttempts && !empty($row['last_attempt'])) {
            $lastTime = strtotime($row['last_attempt']);
            $lockoutExpire = $lastTime + (15 * 60);
            $remainingSeconds = max(0, $lockoutExpire - time());
            
            if ($remainingSeconds > 0) {
                return [
                    'is_locked' => true,
                    'failed_count' => $failedCount,
                    'remaining_seconds' => $remainingSeconds,
                    'remaining_minutes' => ceil($remainingSeconds / 60)
                ];
            }
        }
        
        return [
            'is_locked' => false,
            'failed_count' => $failedCount,
            'attempts_left' => max(0, $maxAttempts - $failedCount)
        ];
    } catch (Exception $e) {
        return ['is_locked' => false, 'failed_count' => 0, 'attempts_left' => 5];
    }
}

/**
 * Record a failed login attempt for rate limiting
 */
function recordFailedLoginAttempt($pdo, $ip, $username) {
    logAdminAction($pdo, 'LOGIN_FAILED', 'USER', $username, 'Failed login attempt for username: ' . $username, $ip);
}

/**
 * Reset failed login attempts upon successful login
 */
function clearFailedLoginAttempts($pdo, $ip) {
    try {
        $stmt = $pdo->prepare("DELETE FROM admin_logs WHERE ip_address = ? AND action = 'LOGIN_FAILED'");
        $stmt->execute([$ip]);
    } catch (Exception $e) {
        // Silently continue
    }
}

/**
 * Returns current admin user details array
 */
function getAdminUser() {
    static $cachedUser = null;
    if ($cachedUser !== null) {
        return $cachedUser;
    }
    $adminId = $_SESSION['admin_id'] ?? 0;
    if ($adminId) {
        try {
            $pdo = getDbConnection();
            $stmt = $pdo->prepare("SELECT id, username, full_name, first_name, last_name, email, phone, dob, gender, blood_group, avatar, role FROM admin_users WHERE id = ?");
            $stmt->execute([$adminId]);
            $user = $stmt->fetch(PDO::FETCH_ASSOC);
            if ($user) {
                if (empty($user['first_name']) && !empty($user['full_name'])) {
                    $parts = explode(' ', trim($user['full_name']), 2);
                    $user['first_name'] = $parts[0] ?? '';
                    $user['last_name'] = $parts[1] ?? '';
                }
                $cachedUser = $user;
                return $user;
            }
        } catch (Exception $e) {
            // fallback to session
        }
    }
    return [
        'id' => $_SESSION['admin_id'] ?? 0,
        'username' => $_SESSION['admin_username'] ?? 'admin',
        'full_name' => $_SESSION['admin_full_name'] ?? 'Administrator',
        'first_name' => 'Deen One',
        'last_name' => 'Administrator',
        'email' => $_SESSION['admin_email'] ?? 'admin@deenone.top',
        'phone' => '+880 1700-000000',
        'dob' => '',
        'gender' => 'male',
        'blood_group' => 'O+',
        'avatar' => '',
        'role' => $_SESSION['admin_role'] ?? 'SUPERADMIN'
    ];
}

/**
 * Generate or get CSRF token
 */
function getCsrfToken() {
    if (empty($_SESSION['csrf_token'])) {
        $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf_token'];
}

/**
 * Generate or get CSRF token (alias for compatibility)
 */
function generateCsrfToken() {
    return getCsrfToken();
}

/**
 * Verify submitted CSRF token
 */
function verifyCsrfToken($token) {
    if (empty($token) || empty($_SESSION['csrf_token'])) {
        return false;
    }
    return hash_equals($_SESSION['csrf_token'], $token);
}

/**
 * Enforce CSRF token or redirect with error
 */
function checkCsrfToken($token) {
    if (!verifyCsrfToken($token)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। অনুগ্রহ করে আবার চেষ্টা করুন।');
        redirect($_SERVER['REQUEST_URI'] ?? 'dashboard.php');
    }
}

/**
 * Sets a flash message for next page view
 */
function setFlash($type, $message) {
    $_SESSION['flash_message'] = [
        'type' => $type, // 'success', 'danger', 'warning'
        'message' => $message
    ];
}

/**
 * Retrieves and clears flash message
 */
function getFlash() {
    if (isset($_SESSION['flash_message'])) {
        $msg = $_SESSION['flash_message'];
        unset($_SESSION['flash_message']);
        return $msg;
    }
    return null;
}

/**
 * Log admin action into audit table (alias: logAdminActivity)
 */
function logAdminAction($pdo, $adminIdOrAction, $actionOrType = null, $targetTypeOrId = null, $targetIdOrDetails = null, $detailsOrIp = null) {
    try {
        if (is_numeric($adminIdOrAction)) {
            $adminId = $adminIdOrAction;
            $action = $actionOrType;
            $targetType = $targetTypeOrId;
            $targetId = $targetIdOrDetails;
            $details = $detailsOrIp;
        } else {
            $adminId = $_SESSION['admin_id'] ?? null;
            $action = $adminIdOrAction;
            $targetType = $actionOrType;
            $targetId = $targetTypeOrId;
            $details = $targetIdOrDetails;
        }
        $ip = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
        $stmt = $pdo->prepare("INSERT INTO admin_logs (admin_id, action, target_type, target_id, details, ip_address) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->execute([$adminId, $action, $targetType, $targetId, $details, $ip]);
    } catch (Exception $e) {
        // Silently skip logging errors
    }
}

if (!function_exists('logAdminActivity')) {
    function logAdminActivity($pdo, $adminId, $action, $targetType = null, $targetId = null, $details = null) {
        logAdminAction($pdo, $adminId, $action, $targetType, $targetId, $details);
    }
}

/**
 * Clean up expired battle rooms
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

function csrfField() {
    return '<input type="hidden" name="csrf_token" value="' . htmlspecialchars(getCsrfToken()) . '">';
}

function getCsrfField() {
    return csrfField();
}

if (!function_exists('toBnNum')) {
    function toBnNum($num) {
        $bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
        return strtr((string)$num, ['0'=>$bn[0],'1'=>$bn[1],'2'=>$bn[2],'3'=>$bn[3],'4'=>$bn[4],'5'=>$bn[5],'6'=>$bn[6],'7'=>$bn[7],'8'=>$bn[8],'9'=>$bn[9]]);
    }
}
?>
