<?php
/**
 * ==============================================================================
 * DEEN ONE (দীন ওয়ান) - CONFIGURATION FILE
 * For Namecheap Shared Hosting & cPanel Environment
 * ==============================================================================
 */

// Prevent direct access to config file
if (basename($_SERVER['PHP_SELF']) == basename(__FILE__)) {
    header('HTTP/1.0 403 Forbidden');
    exit('Access Denied');
}

// -----------------------------------------------------------------------------
// 1. DATABASE CREDENTIALS (Auto-detect Localhost vs cPanel Production)
// -----------------------------------------------------------------------------
$isLocalEnv = (isset($_SERVER['HTTP_HOST']) && (strpos($_SERVER['HTTP_HOST'], 'localhost') !== false || strpos($_SERVER['HTTP_HOST'], '127.0.0.1') !== false || strpos($_SERVER['HTTP_HOST'], '192.168.') !== false)) || php_sapi_name() === 'cli-server' || php_sapi_name() === 'cli';

if ($isLocalEnv) {
    define('DB_HOST', 'localhost');
    define('DB_NAME', 'deenonet_db');
    define('DB_USER', 'root');
    define('DB_PASS', '');
    define('DB_CHARSET', 'utf8mb4');
    define('SITE_URL', 'http://127.0.0.1:8000/');
} else {
    define('DB_HOST', 'localhost');
    define('DB_NAME', 'deenonet_db');
    define('DB_USER', 'deenonet_riad');
    define('DB_PASS', '@Labib013rt');
    define('DB_CHARSET', 'utf8mb4');
    define('SITE_URL', 'https://deenone.top/');
}
define('APP_TIMEZONE', 'Asia/Dhaka');
date_default_timezone_set(APP_TIMEZONE);

// Admin session name
define('ADMIN_SESSION_NAME', 'deenone_admin_session');
define('API_SECRET_KEY', 'deenone_secure_secret_key');

// Battle room settings
define('BATTLE_ROOM_EXPIRE_HOURS', 2);   // Stale rooms auto-expire after 2 hours
define('HEARTBEAT_TIMEOUT_SECONDS', 30); // Player considered disconnected after 30s of no heartbeat

// -----------------------------------------------------------------------------
// 3. ERROR REPORTING & ENVIRONMENT
// -----------------------------------------------------------------------------
// Change to false in production if you wish to suppress PHP notices:
define('DEBUG_MODE', false);

if (DEBUG_MODE) {
    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
} else {
    ini_set('display_errors', 0);
    ini_set('display_startup_errors', 0);
    error_reporting(0);
}
?>
