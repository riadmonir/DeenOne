<?php
/**
 * ==============================================================================
 * DEEN ONE API - AUTHENTICATION & USER ENGINE (PRODUCTION RESILIENT)
 * Supports: Registration (Email/Phone), Login (Email/Phone/Username), Google Auth,
 * Forgot Password, Reset Password, Profile Sync, Legal & App Settings.
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';
require_once __DIR__ . '/mail_helper.php';

$pdo = getDbConnection();

// Allow GET for legal/support queries, POST for mutations
$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
$data = getRequestData();
$action = trim($data['action'] ?? $_GET['action'] ?? 'login');

// ==============================================================================
// 0. SELF-HEALING SCHEMA MIGRATION (Zero "Unknown Column" SQL Errors Guaranteed)
// ==============================================================================
try {
    $stmt = $pdo->query("SHOW COLUMNS FROM users");
    $existingCols = $stmt->fetchAll(PDO::FETCH_COLUMN);

    $neededCols = [
        'user_id'          => "VARCHAR(100) PRIMARY KEY",
        'name'             => "VARCHAR(100) NOT NULL",
        'username'         => "VARCHAR(60) DEFAULT NULL",
        'email'            => "VARCHAR(120) DEFAULT NULL",
        'phone'            => "VARCHAR(30) DEFAULT NULL",
        'password_hash'    => "VARCHAR(255) DEFAULT NULL",
        'blood_group'      => "VARCHAR(10) DEFAULT NULL",
        'google_id'        => "VARCHAR(100) DEFAULT NULL",
        'reset_code'       => "VARCHAR(10) DEFAULT NULL",
        'reset_expires'    => "DATETIME DEFAULT NULL",
        'avatar'           => "VARCHAR(255) DEFAULT 'avatar_1'",
        'app_language'     => "VARCHAR(10) DEFAULT 'bn'",
        'theme_mode'       => "VARCHAR(20) DEFAULT 'dark'",
        'settings_json'    => "MEDIUMTEXT DEFAULT NULL",
        'district'         => "VARCHAR(80) DEFAULT NULL",
        'total_points'     => "INT UNSIGNED DEFAULT 50",
        'quiz_points'      => "INT UNSIGNED DEFAULT 0",
        'battles_played'   => "INT UNSIGNED DEFAULT 0",
        'battles_won'      => "INT UNSIGNED DEFAULT 0",
        'daily_streak'     => "INT UNSIGNED DEFAULT 1",
        'last_streak_date' => "DATE DEFAULT NULL",
        'fcm_token'        => "TEXT DEFAULT NULL",
        'is_banned'        => "TINYINT(1) DEFAULT 0",
        'last_active'      => "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
        'created_at'       => "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    ];

    foreach ($neededCols as $colName => $colDef) {
        if (!in_array($colName, $existingCols)) {
            try {
                $pdo->exec("ALTER TABLE users ADD COLUMN `{$colName}` {$colDef}");
            } catch (Exception $alterEx) {}
        }
    }

    // Ensure avatar and text columns can accommodate long Google OAuth profile URLs
    try {
        $pdo->exec("ALTER TABLE users MODIFY COLUMN `avatar` TEXT DEFAULT NULL");
        $pdo->exec("ALTER TABLE users MODIFY COLUMN `google_id` VARCHAR(191) DEFAULT NULL");
        $pdo->exec("ALTER TABLE users MODIFY COLUMN `email` VARCHAR(191) DEFAULT NULL");
        $pdo->exec("ALTER TABLE users MODIFY COLUMN `name` VARCHAR(191) NOT NULL DEFAULT 'DeenOne User'");
        $pdo->exec("ALTER TABLE users MODIFY COLUMN `username` VARCHAR(100) DEFAULT NULL");
    } catch (Exception $alterEx) {}

    // App Settings Table
    $pdo->exec("CREATE TABLE IF NOT EXISTS `app_settings` (
        `setting_key` VARCHAR(100) PRIMARY KEY,
        `setting_value` LONGTEXT DEFAULT NULL,
        `description` VARCHAR(255) DEFAULT NULL,
        `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

    // Blood Donors Table
    $pdo->exec("CREATE TABLE IF NOT EXISTS `blood_donors` (
        `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        `user_id` VARCHAR(100) DEFAULT NULL,
        `name` VARCHAR(100) NOT NULL,
        `blood_group` VARCHAR(10) NOT NULL,
        `district` VARCHAR(80) DEFAULT 'ঢাকা',
        `upazila` VARCHAR(80) DEFAULT NULL,
        `phone_number` VARCHAR(30) NOT NULL,
        `last_donation_date` DATE DEFAULT NULL,
        `is_available` TINYINT(1) DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        INDEX `idx_blood_group` (`blood_group`),
        INDEX `idx_district` (`district`),
        INDEX `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

} catch (Exception $e) {
    // Non-fatal migration check
}

/**
 * Generates a clean numeric User ID (e.g. 1001, 1002, 1003).
 */
function generateNumericUserId($pdo) {
    try {
        $stmt = $pdo->query("SELECT MAX(CAST(user_id AS UNSIGNED)) AS max_id FROM users WHERE user_id REGEXP '^[0-9]+$'");
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $maxId = !empty($row['max_id']) ? (int)$row['max_id'] : 0;
        return (string)($maxId < 1000 ? 1001 : ($maxId + 1));
    } catch (Exception $e) {
        return (string)rand(1001, 9999);
    }
}

// -----------------------------------------------------------------------------
// 1. REGISTER NEW USER (নতুন অ্যাকাউন্ট তৈরি)
// -----------------------------------------------------------------------------
if ($action === 'register') {
    if ($method !== 'POST') {
        sendJsonResponse(['success' => false, 'error' => 'POST request required for registration.'], 405);
    }

    $name = trim($data['name'] ?? '');
    $email = trim($data['email'] ?? '');
    $phone = trim($data['phone'] ?? '');
    $username = trim($data['username'] ?? '');
    $bloodGroup = trim($data['blood_group'] ?? '');
    $password = trim($data['password'] ?? '');

    if (empty($name)) {
        sendJsonResponse(['success' => false, 'error' => 'অনুগ্রহ করে আপনার সম্পূর্ণ নাম লিখুন।'], 400);
    }

    // Email is strictly mandatory and must be valid
    if (empty($email) || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
        sendJsonResponse(['success' => false, 'error' => 'রেজিস্ট্রেশনের জন্য একটি সঠিক ও বৈধ ইমেইল ঠিকানা প্রদান করা বাধ্যতামূলক।'], 400);
    }

    if (empty($password) || strlen($password) < 6) {
        sendJsonResponse(['success' => false, 'error' => 'পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে।'], 400);
    }

    try {
        // Check if email already registered (Strict Single Account Per Email Policy)
        $stmt = $pdo->prepare("SELECT user_id FROM users WHERE email IS NOT NULL AND email != '' AND email = ? LIMIT 1");
        $stmt->execute([$email]);
        if ($stmt->fetch()) {
            sendJsonResponse(['success' => false, 'error' => 'এই ইমেইল ঠিকানাটি দিয়ে ইতিমধ্যে একটি অ্যাকাউন্ট রয়েছে। একটি ইমেইল দিয়ে কেবলমাত্র একটি অ্যাকাউন্ট তৈরি করা সম্ভব।'], 409);
        }

        // Check if phone already registered (if provided)
        if (!empty($phone)) {
            $pStmt = $pdo->prepare("SELECT user_id FROM users WHERE phone IS NOT NULL AND phone != '' AND phone = ? LIMIT 1");
            $pStmt->execute([$phone]);
            if ($pStmt->fetch()) {
                sendJsonResponse(['success' => false, 'error' => 'এই ফোন নম্বরটি দিয়ে ইতিমধ্যে একটি অ্যাকাউন্ট রয়েছে।'], 409);
            }
        }

        // Derive username if not explicitly given
        if (empty($username)) {
            if (!empty($email)) {
                $username = strtolower(explode('@', $email)[0]);
            } else {
                $username = 'user_' . substr($phone, -4);
            }
        }

        // Check username uniqueness and make unique if needed
        $chkU = $pdo->prepare("SELECT user_id FROM users WHERE username = ? LIMIT 1");
        $chkU->execute([$username]);
        if ($chkU->fetch()) {
            $username = $username . rand(10, 99);
        }

        $userId = generateNumericUserId($pdo);
        $passwordHash = password_hash($password, PASSWORD_BCRYPT);
        $avatar = 'avatar_' . rand(1, 6);
        $authToken = bin2hex(random_bytes(24));

        $sql = "INSERT INTO users (user_id, name, username, phone, email, password_hash, blood_group, avatar, total_points, quiz_points, daily_streak, is_banned, last_active, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 50, 0, 1, 0, NOW(), NOW())";
        $ins = $pdo->prepare($sql);
        $ins->execute([
            $userId,
            $name,
            $username,
            !empty($phone) ? $phone : null,
            !empty($email) ? $email : null,
            $passwordHash,
            !empty($bloodGroup) ? $bloodGroup : null,
            $avatar
        ]);

        // Register in blood donors table if blood group is provided
        if (!empty($bloodGroup) && !empty($phone)) {
            try {
                $bSql = "INSERT INTO blood_donors (user_id, name, blood_group, phone_number, is_available) 
                         VALUES (?, ?, ?, ?, 1)
                         ON DUPLICATE KEY UPDATE blood_group = VALUES(blood_group), name = VALUES(name)";
                $bStmt = $pdo->prepare($bSql);
                $bStmt->execute([$userId, $name, $bloodGroup, $phone]);
            } catch (Exception $be) {}
        }

        // Dispatch Welcome Email
        if (!empty($email)) {
            sendWelcomeEmail($email, $name, $pdo);
        }

        sendJsonResponse([
            'success' => true,
            'message' => 'অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে! স্বাগতম বোনাস ৫০ পয়েন্ট যোগ করা হয়েছে।',
            'user' => [
                'user_id'     => $userId,
                'name'        => $name,
                'username'    => $username,
                'phone'       => $phone,
                'email'       => $email,
                'blood_group' => $bloodGroup,
                'points'      => 50,
                'avatar'      => $avatar,
                'token'       => $authToken
            ]
        ], 201);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'রেজিস্ট্রেশন ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 2. USER LOGIN (ইমেইল / ফোন / ইউজারনেম এবং পাসওয়ার্ড দিয়ে লগইন)
// -----------------------------------------------------------------------------
if ($action === 'login') {
    if ($method !== 'POST') {
        sendJsonResponse(['success' => false, 'error' => 'POST request required for login.'], 405);
    }

    $identifier = trim($data['identifier'] ?? $data['phone'] ?? $data['email'] ?? $data['username'] ?? '');
    $password = trim($data['password'] ?? '');

    if (empty($identifier) || empty($password)) {
        sendJsonResponse(['success' => false, 'error' => 'ইমেইল / ফোন নম্বর এবং পাসওয়ার্ড প্রদান করুন।'], 400);
    }

    try {
        $stmt = $pdo->prepare("SELECT * FROM users 
                               WHERE (phone IS NOT NULL AND phone != '' AND phone = ?) 
                                  OR (email IS NOT NULL AND email != '' AND email = ?)
                                  OR (username IS NOT NULL AND username != '' AND username = ?)
                                  OR user_id = ? 
                               LIMIT 1");
        $stmt->execute([$identifier, $identifier, $identifier, $identifier]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$user) {
            sendJsonResponse(['success' => false, 'error' => 'এই তথ্য দিয়ে কোনো অ্যাকাউন্ট পাওয়া যায়নি। প্রথমে অ্যাকাউন্ট তৈরি করুন।'], 404);
        }

        if ((int)($user['is_banned'] ?? 0) === 1) {
            sendJsonResponse(['success' => false, 'error' => 'আপনার অ্যাকাউন্টটি সাময়িকভাবে স্থগিত করা হয়েছে। এডমিনের সাথে যোগাযোগ করুন।'], 403);
        }

        // Verify password
        if (empty($user['password_hash']) || !password_verify($password, $user['password_hash'])) {
            sendJsonResponse(['success' => false, 'error' => 'ভুল পাসওয়ার্ড! অনুগ্রহ করে পুনরায় সঠিক পাসওয়ার্ড লিখুন।'], 401);
        }

        // Update last active timestamp
        $pdo->prepare("UPDATE users SET last_active = NOW() WHERE user_id = ?")->execute([$user['user_id']]);

        // Fetch user qaza records if table exists
        $qazaData = null;
        try {
            $qStmt = $pdo->prepare("SELECT fajr_qaza, dhuhr_qaza, asr_qaza, maghrib_qaza, isha_qaza, witr_qaza, fasting_qaza, total_qaza FROM user_qaza_records WHERE user_id = ? LIMIT 1");
            $qStmt->execute([$user['user_id']]);
            $qRow = $qStmt->fetch(PDO::FETCH_ASSOC);
            if ($qRow) {
                $qazaData = [
                    'fajr'    => (int)$qRow['fajr_qaza'],
                    'dhuhr'   => (int)$qRow['dhuhr_qaza'],
                    'asr'     => (int)$qRow['asr_qaza'],
                    'maghrib' => (int)$qRow['maghrib_qaza'],
                    'isha'    => (int)$qRow['isha_qaza'],
                    'witr'    => (int)$qRow['witr_qaza'],
                    'fasting' => (int)$qRow['fasting_qaza'],
                    'total'   => (int)$qRow['total_qaza']
                ];
            }
        } catch (Exception $ignored) {}

        $authToken = bin2hex(random_bytes(24));

        // Dispatch New Device Sign-In Alert Email (Verbatim Template)
        if (!empty($user['email'])) {
            triggerLoginAlertNotification(
                $user['email'],
                $user['name'] ?? $user['username'] ?? 'Believer',
                $user['district'] ?? '',
                $data['device_name'] ?? '',
                $pdo
            );
        }

        // Fetch lifetime stats
        $totalPrayed = 0;
        $totalAmals = 0;
        $totalQuizzes = 0;
        try {
            $prStmt = $pdo->prepare("SELECT COUNT(DISTINCT CONCAT(prayer_date, '_', prayer_name)) FROM user_prayer_logs WHERE user_id = ? AND is_prayed = 1 AND is_qaza = 0 AND prayer_name IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')");
            $prStmt->execute([$user['user_id']]);
            $totalPrayed = intval($prStmt->fetchColumn() ?: 0);

            $amStmt = $pdo->prepare("SELECT COALESCE(SUM((fajr>0)+(dhuhr>0)+(asr>0)+(maghrib>0)+(isha>0)+(tahajjud>0)+(dhuha>0)+(quran_minutes>0)+(sadaqah>0)+(fasting>0)+(dhikr_count>0)), 0) FROM user_amal_logs WHERE user_id = ?");
            $amStmt->execute([$user['user_id']]);
            $totalAmals = intval($amStmt->fetchColumn() ?: 0);

            $qzStmt = $pdo->prepare("SELECT COUNT(*) FROM user_quiz_results WHERE user_id = ? AND (correct_count > 0 OR score > 0 OR points_earned > 0)");
            $qzStmt->execute([$user['user_id']]);
            $totalQuizzes = intval($qzStmt->fetchColumn() ?: 0);
        } catch (Exception $ignored) {}

        sendJsonResponse([
            'success' => true,
            'message' => 'লগইন সফল হয়েছে!',
            'user' => [
                'user_id'       => $user['user_id'],
                'name'          => $user['name'] ?? 'DeenOne User',
                'username'      => $user['username'] ?? '',
                'phone'         => $user['phone'] ?? '',
                'email'         => $user['email'] ?? '',
                'blood_group'   => $user['blood_group'] ?? '',
                'points'        => (int)($user['total_points'] ?? 50),
                'quiz_points'   => (int)($user['quiz_points'] ?? 0),
                'daily_streak'  => (int)($user['daily_streak'] ?? 1),
                'total_prayers' => $totalPrayed,
                'total_amals'   => $totalAmals,
                'total_quizzes' => $totalQuizzes,
                'avatar'        => $user['avatar'] ?? 'avatar_1',
                'district'      => $user['district'] ?? '',
                'app_language'  => $user['app_language'] ?? 'bn',
                'theme_mode'    => $user['theme_mode'] ?? 'dark',
                'settings_json' => $user['settings_json'] ?? null,
                'qaza'          => $qazaData,
                'token'         => $authToken
            ]
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'লগইন ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 3. GOOGLE SIGN-IN / AUTH (গুগল সাইন-ইন / রেজিস্ট্রেশন)
// -----------------------------------------------------------------------------
if ($action === 'google') {
    $googleId = trim($data['google_id'] ?? '');
    $email = trim($data['email'] ?? '');
    $name = trim($data['name'] ?? '');
    $username = trim($data['username'] ?? '');
    $avatar = trim($data['avatar'] ?? '');

    // Defense-in-depth: normalize field lengths to prevent SQL truncation errors
    if (strlen($name) > 190) $name = mb_substr($name, 0, 190);
    if (strlen($email) > 190) $email = mb_substr($email, 0, 190);
    if (strlen($googleId) > 190) $googleId = mb_substr($googleId, 0, 190);
    if (strlen($username) > 90) $username = mb_substr($username, 0, 90);

    if (empty($googleId) && empty($email)) {
        sendJsonResponse(['success' => false, 'error' => 'গুগল ক্রেডেনশিয়াল পাওয়া যায়নি।'], 400);
    }

    try {
        $stmt = $pdo->prepare("SELECT * FROM users 
                               WHERE (google_id IS NOT NULL AND google_id != '' AND google_id = ?) 
                                  OR (email IS NOT NULL AND email != '' AND email = ?) 
                               LIMIT 1");
        $stmt->execute([$googleId, $email]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        $authToken = bin2hex(random_bytes(24));

        if ($user) {
            if ((int)($user['is_banned'] ?? 0) === 1) {
                sendJsonResponse(['success' => false, 'error' => 'আপনার অ্যাকাউন্টটি স্থগিত রয়েছে।'], 403);
            }

            $updFields = ["last_active = NOW()"];
            $updParams = [];

            if (!empty($googleId)) {
                $updFields[] = "google_id = ?";
                $updParams[] = $googleId;
            }
            if (!empty($avatar) && $avatar !== 'avatar_1') {
                if (empty($user['avatar']) || preg_match('/^avatar_\d+$/', $user['avatar'])) {
                    $updFields[] = "avatar = ?";
                    $updParams[] = $avatar;
                    $user['avatar'] = $avatar;
                }
            }
            if (!empty($name) && (empty($user['name']) || $user['name'] === 'DeenOne User')) {
                $updFields[] = "name = ?";
                $updParams[] = $name;
            }

            $updParams[] = $user['user_id'];
            $pdo->prepare("UPDATE users SET " . implode(", ", $updFields) . " WHERE user_id = ?")->execute($updParams);

            // Fetch user qaza records if table exists
            $qazaData = null;
            try {
                $qStmt = $pdo->prepare("SELECT fajr_qaza, dhuhr_qaza, asr_qaza, maghrib_qaza, isha_qaza, witr_qaza, fasting_qaza, total_qaza FROM user_qaza_records WHERE user_id = ? LIMIT 1");
                $qStmt->execute([$user['user_id']]);
                $qRow = $qStmt->fetch(PDO::FETCH_ASSOC);
                if ($qRow) {
                    $qazaData = [
                        'fajr'    => (int)$qRow['fajr_qaza'],
                        'dhuhr'   => (int)$qRow['dhuhr_qaza'],
                        'asr'     => (int)$qRow['asr_qaza'],
                        'maghrib' => (int)$qRow['maghrib_qaza'],
                        'isha'    => (int)$qRow['isha_qaza'],
                        'witr'    => (int)$qRow['witr_qaza'],
                        'fasting' => (int)$qRow['fasting_qaza'],
                        'total'   => (int)$qRow['total_qaza']
                    ];
                }
            } catch (Exception $ignored) {}

            // Dispatch New Device Sign-In Alert Email (Verbatim Template)
            $userMail = $user['email'] ?? $email;
            if (!empty($userMail)) {
                triggerLoginAlertNotification(
                    $userMail,
                    !empty($name) ? $name : ($user['name'] ?? $user['username'] ?? 'Believer'),
                    $user['district'] ?? '',
                    $data['device_name'] ?? '',
                    $pdo
                );
            }

            // Fetch lifetime stats
            $totalPrayed = 0;
            $totalAmals = 0;
            $totalQuizzes = 0;
            try {
                $prStmt = $pdo->prepare("SELECT COUNT(DISTINCT CONCAT(prayer_date, '_', prayer_name)) FROM user_prayer_logs WHERE user_id = ? AND is_prayed = 1 AND is_qaza = 0 AND prayer_name IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')");
                $prStmt->execute([$user['user_id']]);
                $totalPrayed = intval($prStmt->fetchColumn() ?: 0);

                $amStmt = $pdo->prepare("SELECT COALESCE(SUM((fajr>0)+(dhuhr>0)+(asr>0)+(maghrib>0)+(isha>0)+(tahajjud>0)+(dhuha>0)+(quran_minutes>0)+(sadaqah>0)+(fasting>0)+(dhikr_count>0)), 0) FROM user_amal_logs WHERE user_id = ?");
                $amStmt->execute([$user['user_id']]);
                $totalAmals = intval($amStmt->fetchColumn() ?: 0);

                $qzStmt = $pdo->prepare("SELECT COUNT(*) FROM user_quiz_results WHERE user_id = ? AND (correct_count > 0 OR score > 0 OR points_earned > 0)");
                $qzStmt->execute([$user['user_id']]);
                $totalQuizzes = intval($qzStmt->fetchColumn() ?: 0);
            } catch (Exception $ignored) {}

            sendJsonResponse([
                'success' => true,
                'message' => 'গুগল লগইন সফল হয়েছে!',
                'user' => [
                    'user_id'       => $user['user_id'],
                    'name'          => !empty($name) ? $name : ($user['name'] ?? 'DeenOne User'),
                    'username'      => $user['username'] ?? '',
                    'phone'         => $user['phone'] ?? '',
                    'email'         => $user['email'] ?? $email,
                    'blood_group'   => $user['blood_group'] ?? '',
                    'points'        => (int)($user['total_points'] ?? 50),
                    'quiz_points'   => (int)($user['quiz_points'] ?? 0),
                    'daily_streak'  => (int)($user['daily_streak'] ?? 1),
                    'total_prayers' => $totalPrayed,
                    'total_amals'   => $totalAmals,
                    'total_quizzes' => $totalQuizzes,
                    'avatar'        => !empty($user['avatar']) ? $user['avatar'] : (!empty($avatar) ? $avatar : 'avatar_1'),
                    'district'      => $user['district'] ?? '',
                    'app_language'  => $user['app_language'] ?? 'bn',
                    'theme_mode'    => $user['theme_mode'] ?? 'dark',
                    'settings_json' => $user['settings_json'] ?? null,
                    'qaza'          => $qazaData,
                    'token'         => $authToken
                ]
            ]);
        } else {
            $userId = generateNumericUserId($pdo);
            $userAvatar = !empty($avatar) ? $avatar : 'avatar_1';
            $userName = !empty($name) ? $name : 'DeenOne User';
            $userUsername = !empty($username) ? $username : (explode('@', $email)[0] ?? 'user_' . $userId);

            // Ensure username uniqueness
            $chk = $pdo->prepare("SELECT user_id FROM users WHERE username = ? LIMIT 1");
            $chk->execute([$userUsername]);
            if ($chk->fetch()) {
                $userUsername = $userUsername . rand(10, 99);
            }

            $ins = $pdo->prepare("INSERT INTO users (user_id, name, username, email, google_id, avatar, total_points, quiz_points, daily_streak, app_language, theme_mode, created_at, last_active) 
                                 VALUES (?, ?, ?, ?, ?, ?, 50, 0, 1, 'bn', 'dark', NOW(), NOW())");
            $ins->execute([$userId, $userName, $userUsername, $email, $googleId, $userAvatar]);

            // Dispatch Welcome Email and Login Alert
            if (!empty($email)) {
                sendWelcomeEmail($email, $userName, $pdo);
                triggerLoginAlertNotification($email, $userName, '', $data['device_name'] ?? '', $pdo);
            }

            sendJsonResponse([
                'success' => true,
                'message' => 'গুগল অ্যাকাউন্ট দিয়ে সফলভাবে যুক্ত হয়েছেন!',
                'user' => [
                    'user_id'       => $userId,
                    'name'          => $userName,
                    'username'      => $userUsername,
                    'phone'         => '',
                    'email'         => $email,
                    'blood_group'   => '',
                    'points'        => 50,
                    'quiz_points'   => 0,
                    'daily_streak'  => 1,
                    'avatar'        => $userAvatar,
                    'district'      => '',
                    'app_language'  => 'bn',
                    'theme_mode'    => 'dark',
                    'settings_json' => null,
                    'qaza'          => null,
                    'token'         => $authToken
                ]
            ], 201);
        }
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'গুগল লগইন ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 4. FORGOT PASSWORD (পাসওয়ার্ড পুনরুদ্ধার - ৬ ডিজিটের কোড তৈরি)
// -----------------------------------------------------------------------------
if ($action === 'forgot_password') {
    $identifier = trim($data['identifier'] ?? $data['phone'] ?? $data['email'] ?? $data['username'] ?? '');

    if (empty($identifier)) {
        sendJsonResponse(['success' => false, 'error' => 'আপনার ইমেইল অথবা ফোন নম্বর প্রদান করুন।'], 400);
    }

    try {
        $stmt = $pdo->prepare("SELECT user_id, name, phone, email, reset_expires FROM users 
                               WHERE (phone IS NOT NULL AND phone != '' AND phone = ?) 
                                  OR (email IS NOT NULL AND email != '' AND email = ?) 
                                  OR (username IS NOT NULL AND username != '' AND username = ?)
                                  OR user_id = ?
                               LIMIT 1");
        $stmt->execute([$identifier, $identifier, $identifier, $identifier]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$user) {
            sendJsonResponse(['success' => false, 'error' => 'এই তথ্য দিয়ে কোনো অ্যাকাউন্ট পাওয়া যায়নি। সঠিক ইমেইল বা ফোন নম্বর লিখুন।'], 404);
        }

        // Strict 5-Minute Rate Limit (Cooldown)
        if (!empty($user['reset_expires'])) {
            $chkCooldown = $pdo->prepare("SELECT TIMESTAMPDIFF(SECOND, NOW(), reset_expires) AS sec_left FROM users WHERE user_id = ?");
            $chkCooldown->execute([$user['user_id']]);
            $cdRow = $chkCooldown->fetch(PDO::FETCH_ASSOC);
            $secLeft = (int)($cdRow['sec_left'] ?? 0);
            
            // If reset_expires was set to 15 min (900 sec), 5 min cooldown means sec_left > 600
            if ($secLeft > 600) {
                $waitSec = $secLeft - 600;
                $waitMin = ceil($waitSec / 60);
                sendJsonResponse([
                    'success' => false,
                    'error' => "৫ মিনিটের মধ্যে শুধুমাত্র একবার কোড পাঠানো যাবে। অনুগ্রহ করে {$waitMin} মিনিট পর পুনরায় চেষ্টা করুন।",
                    'cooldown_seconds' => $waitSec
                ], 429);
            }
        }

        // Generate 6-digit verification code with 15-minute expiration
        $code = (string)rand(111111, 999999);
        $upd = $pdo->prepare("UPDATE users SET reset_code = ?, reset_expires = DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE user_id = ?");
        $upd->execute([$code, $user['user_id']]);

        // Dispatch email using official DeenOne password reset template
        if (!empty($user['email'])) {
            $userName = !empty($user['name']) ? $user['name'] : (!empty($user['username']) ? $user['username'] : 'Believer');
            sendPasswordResetEmail($user['email'], $userName, $code, $pdo);
        }

        sendJsonResponse([
            'success' => true,
            'message' => "আপনার অ্যাকাউন্টের ইমেইলে ৬ ডিজিটের ভেরিফিকেশন কোড পাঠানো হয়েছে। অনুগ্রহ করে ইনবক্স চেক করুন।"
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'পাসওয়ার্ড রিসেট ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 5. RESET PASSWORD WITH VERIFICATION CODE (নতুন পাসওয়ার্ড নির্ধারণ)
// -----------------------------------------------------------------------------
if ($action === 'reset_password') {
    $identifier = trim($data['identifier'] ?? '');
    $code = trim($data['code'] ?? '');
    $newPassword = trim($data['new_password'] ?? '');

    if (empty($identifier) || empty($code) || empty($newPassword)) {
        sendJsonResponse(['success' => false, 'error' => 'সকল তথ্য (ফোন/ইমেইল, ভেরিফিকেশন কোড ও নতুন পাসওয়ার্ড) সঠিকভাবে পূরণ করুন।'], 400);
    }

    if (strlen($newPassword) < 6) {
        sendJsonResponse(['success' => false, 'error' => 'নতুন পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে।'], 400);
    }

    try {
        $stmt = $pdo->prepare("SELECT user_id FROM users 
                               WHERE ((phone IS NOT NULL AND phone = ?) OR (email IS NOT NULL AND email = ?) OR (username IS NOT NULL AND username = ?) OR user_id = ?) 
                                 AND reset_code = ? 
                                 AND reset_expires > NOW() 
                               LIMIT 1");
        $stmt->execute([$identifier, $identifier, $identifier, $identifier, $code]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$user) {
            sendJsonResponse(['success' => false, 'error' => 'ভেরিফিকেশন কোডটি ভুল অথবা এর ১৫ মিনিটের মেয়াদ শেষ হয়ে গেছে। পুনরায় কোড পাঠান।'], 400);
        }

        $newHash = password_hash($newPassword, PASSWORD_BCRYPT);
        $upd = $pdo->prepare("UPDATE users SET password_hash = ?, reset_code = NULL, reset_expires = NULL WHERE user_id = ?");
        $upd->execute([$newHash, $user['user_id']]);

        sendJsonResponse([
            'success' => true,
            'message' => 'পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে! এখন নতুন পাসওয়ার্ড দিয়ে লগইন করুন।'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'পাসওয়ার্ড পরিবর্তন ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 6. UPDATE USER PROFILE (প্রোফাইল তথ্য আপডেট)
// -----------------------------------------------------------------------------
if ($action === 'update_profile') {
    $userId = trim($data['user_id'] ?? '');
    $name = trim($data['name'] ?? '');
    $username = trim($data['username'] ?? '');
    $email = trim($data['email'] ?? '');
    $phone = trim($data['phone'] ?? '');
    $bloodGroup = trim($data['blood_group'] ?? '');
    $district = trim($data['district'] ?? '');
    $avatar = trim($data['avatar'] ?? '');

    if (empty($userId)) {
        sendJsonResponse(['success' => false, 'error' => 'ইউজার আইডি আবশ্যক।'], 400);
    }

    try {
        $fields = ["last_active = NOW()"];
        $params = [];

        if (!empty($name)) {
            $fields[] = "name = ?";
            $params[] = $name;
        }
        if (isset($data['username'])) {
            $cleanedUsername = trim($data['username']);
            if (!empty($cleanedUsername)) {
                $chk = $pdo->prepare("SELECT user_id FROM users WHERE username = ? AND user_id != ? LIMIT 1");
                $chk->execute([$cleanedUsername, $userId]);
                if ($chk->fetch()) {
                    sendJsonResponse(['success' => false, 'error' => 'এই ইউজারনেমটি ইতিমধ্যে ব্যবহৃত হয়েছে। অন্য একটি ইউজারনেম নির্বাচন করুন।'], 409);
                }
                $fields[] = "username = ?";
                $params[] = $cleanedUsername;
            }
        }
        if (!empty($email)) {
            $fields[] = "email = ?";
            $params[] = $email;
        }
        if (!empty($phone)) {
            $fields[] = "phone = ?";
            $params[] = $phone;
        }
        if (!empty($bloodGroup)) {
            $fields[] = "blood_group = ?";
            $params[] = $bloodGroup;
        }
        if (!empty($district)) {
            $fields[] = "district = ?";
            $params[] = $district;
        }
        if (!empty($avatar)) {
            $fields[] = "avatar = ?";
            $params[] = $avatar;
        }

        $params[] = $userId;
        $sql = "UPDATE users SET " . implode(", ", $fields) . " WHERE user_id = ?";
        $pdo->prepare($sql)->execute($params);

        sendJsonResponse([
            'success' => true,
            'message' => 'প্রোফাইল তথ্য সফলভাবে আপডেট করা হয়েছে।'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'প্রোফাইল আপডেট ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// -----------------------------------------------------------------------------
// 7. GET LEGAL & SUPPORT INFO (প্রাইভেসি, শর্তাবলী ও সাপোর্ট)
// -----------------------------------------------------------------------------
if ($action === 'legal' || $action === 'get_legal') {
    try {
        $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN ('privacy_policy_content', 'privacy_policy_content_en', 'privacy_policy_url', 'privacy_policy_updated_at', 'privacy_contact_email', 'privacy_organization_name', 'privacy_data_deletion_info', 'terms_service_content', 'terms_service_url', 'support_contact_info', 'support_email', 'support_whatsapp', 'support_url')");
        $settings = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $settings[$row['setting_key']] = $row['setting_value'];
        }

        sendJsonResponse([
            'success' => true,
            'privacy_policy' => $settings['privacy_policy_content'] ?? 'DeenOne ব্যবহারকারীদের তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা রক্ষা করতে প্রতিশ্রুতিবদ্ধ। সালাত ট্র্যাকিং, কুরআন তিলাওয়াত ও কুইজ পয়েন্ট ক্লাউডে সিঙ্ক করার উদ্দেশ্যে শুধুমাত্র নাম ও ফোন নম্বর সংগ্রহ করা হয়। কোনো প্রকার অননুমোদিত তৃতীয় পক্ষের সাথে ব্যবহারকারীর তথ্য শেয়ার করা হয় না। রক্তদাতা হিসেবে স্বেচ্ছায় নিবন্ধিতদের তথ্য শুধুমাত্র জরুরি প্রয়োজনে অন্য মুসলিম ভাইদের সহায়তায় ব্যবহৃত হয়।',
            'privacy_policy_bn' => $settings['privacy_policy_content'] ?? 'DeenOne ব্যবহারকারীদের তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা রক্ষা করতে প্রতিশ্রুতিবদ্ধ।',
            'privacy_policy_en' => $settings['privacy_policy_content_en'] ?? 'DeenOne is committed to ensuring the maximum security and confidentiality of users\' personal information.',
            'privacy_url' => $settings['privacy_policy_url'] ?? 'https://deenone.top/privacy',
            'privacy_updated_at' => $settings['privacy_policy_updated_at'] ?? '2026-09-24',
            'privacy_contact_email' => $settings['privacy_contact_email'] ?? 'privacy@deenone.top',
            'privacy_organization_name' => $settings['privacy_organization_name'] ?? 'DeenOne Technologies & Foundation',
            'privacy_data_deletion_info' => $settings['privacy_data_deletion_info'] ?? 'ব্যবহারকারী অ্যাপের সেটিংস থেকে অথবা privacy@deenone.top এ ইমেইল পাঠিয়ে যেকোনো সময় তাদের অ্যাকাউন্ট ও ক্লাউডে সংরক্ষিত সমস্ত তথ্য স্থায়ীভাবে মুছে ফেলার আবেদন করতে পারেন।',
            'terms_service' => $settings['terms_service_content'] ?? 'DeenOne অ্যাপ্লিকেশনটি মুসলিম উম্মাহর দৈনন্দিন ইবাদত ও দ্বীনি শিক্ষার সহায়তায় বিনামূল্যে পরিচালিত একটি ইসলামিক প্ল্যাটফর্ম। কুইজ ও নলেজ ব্যাটেল প্রতিযোগিতায় কোনো প্রকার অনৈতিক উপায় অবলম্বন বা বিভ্রান্তিকর তথ্য প্রচার সম্পূর্ণরূপে নিষিদ্ধ। অ্যাপের সকল ইসলামিক কনটেন্ট কুরআন ও সহীহ সুন্নাহর আলোকে সংকলিত।',
            'terms_url' => $settings['terms_service_url'] ?? 'https://deenone.top/terms',
            'support_info' => $settings['support_contact_info'] ?? "দ্বীন ওয়ান সংক্রান্ত যেকোনো জিজ্ঞাসা, পরামর্শ বা সহায়তার জন্য আমাদের অফিসিয়াল সাপোর্ট টিমের সাথে যোগাযোগ করুন।\n\nইমেইল: support@deenone.top\nহোয়াটসঅ্যাপ হেল্পলাইন: +880 1700-000000\nফেসবুক পেজ: fb.com/DeenOneApp\n\nআমরা দ্রুততম সময়ে আপনার প্রশ্নের উত্তর দেওয়ার চেষ্টা করব, ইনশাআল্লাহ।",
            'support_email' => $settings['support_email'] ?? 'support@deenone.top',
            'support_whatsapp' => $settings['support_whatsapp'] ?? '+8801700000000',
            'support_url' => $settings['support_url'] ?? 'https://deenone.top/support'
        ]);
    } catch (Exception $e) {
        sendJsonResponse(['success' => false, 'error' => 'তথ্য লোড করতে ত্রুটি: ' . $e->getMessage()], 500);
    }
}

// Fallback for invalid actions
sendJsonResponse(['success' => false, 'error' => 'অকার্যকর অনুরোধ (Invalid Action: ' . htmlspecialchars($action) . ').'], 400);
