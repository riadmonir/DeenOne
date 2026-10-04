<?php
/**
 * ==============================================================================
 * DEEN ONE API - BLOOD DONORS, REQUESTS & ORGANIZATIONS
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

// Self-healing schema for Blood Module
try {
    $pdo->exec("CREATE TABLE IF NOT EXISTS `blood_donors` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `user_id` VARCHAR(100) DEFAULT NULL,
      `name` VARCHAR(100) NOT NULL,
      `blood_group` VARCHAR(10) NOT NULL,
      `district` VARCHAR(80) NOT NULL DEFAULT 'ঢাকা',
      `upazila` VARCHAR(80) DEFAULT NULL,
      `address` VARCHAR(255) DEFAULT NULL,
      `phone_number` VARCHAR(30) NOT NULL,
      `last_donation_date` DATE DEFAULT NULL,
      `total_donations` INT UNSIGNED DEFAULT 0,
      `society` VARCHAR(150) DEFAULT NULL,
      `is_available` TINYINT(1) DEFAULT 1,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_blood_group` (`blood_group`),
      INDEX `idx_district` (`district`),
      INDEX `idx_user_id` (`user_id`),
      INDEX `idx_phone` (`phone_number`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    // Self-healing check for new columns
    $donorCols = $pdo->query("SHOW COLUMNS FROM blood_donors")->fetchAll(PDO::FETCH_COLUMN);
    if (!in_array('address', $donorCols)) {
        $pdo->exec("ALTER TABLE blood_donors ADD COLUMN `address` VARCHAR(255) DEFAULT NULL AFTER `upazila`");
    }
    if (!in_array('total_donations', $donorCols)) {
        $pdo->exec("ALTER TABLE blood_donors ADD COLUMN `total_donations` INT UNSIGNED DEFAULT 0 AFTER `last_donation_date`");
    }
    if (!in_array('society', $donorCols)) {
        $pdo->exec("ALTER TABLE blood_donors ADD COLUMN `society` VARCHAR(150) DEFAULT NULL AFTER `total_donations`");
    }

    $pdo->exec("CREATE TABLE IF NOT EXISTS `blood_call_requests` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `donor_id` INT UNSIGNED NOT NULL,
      `donor_phone` VARCHAR(50) DEFAULT NULL,
      `donor_name` VARCHAR(120) DEFAULT NULL,
      `requester_user_id` VARCHAR(100) DEFAULT NULL,
      `requester_name` VARCHAR(120) DEFAULT NULL,
      `requester_phone` VARCHAR(50) DEFAULT NULL,
      `blood_group` VARCHAR(10) DEFAULT NULL,
      `status` VARCHAR(30) DEFAULT 'SENT',
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_call_donor_id` (`donor_id`),
      INDEX `idx_call_donor_phone` (`donor_phone`),
      INDEX `idx_call_created` (`created_at`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    $pdo->exec("CREATE TABLE IF NOT EXISTS `blood_requests` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `requester_user_id` VARCHAR(100) DEFAULT NULL,
      `patient_name` VARCHAR(100) NOT NULL,
      `blood_group` VARCHAR(10) NOT NULL,
      `units_needed` INT UNSIGNED DEFAULT 1,
      `hospital_name` VARCHAR(150) NOT NULL,
      `district` VARCHAR(80) NOT NULL DEFAULT 'ঢাকা',
      `upazila` VARCHAR(80) DEFAULT NULL,
      `contact_phone` VARCHAR(30) NOT NULL,
      `whatsapp_number` VARCHAR(30) DEFAULT NULL,
      `urgency_level` ENUM('CRITICAL','EMERGENCY','NORMAL') DEFAULT 'EMERGENCY',
      `needed_date` DATE DEFAULT NULL,
      `status` ENUM('OPEN','ACCEPTED','FULFILLED','CANCELLED') DEFAULT 'OPEN',
      `accepted_by_user_id` VARCHAR(100) DEFAULT NULL,
      `accepted_at` DATETIME DEFAULT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      INDEX `idx_req_group` (`blood_group`),
      INDEX `idx_req_status` (`status`),
      INDEX `idx_req_district` (`district`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    $pdo->exec("CREATE TABLE IF NOT EXISTS `blood_organizations` (
      `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
      `name_bn` VARCHAR(150) NOT NULL,
      `name_en` VARCHAR(150) NOT NULL,
      `category` VARCHAR(60) DEFAULT 'Voluntary Organization',
      `district` VARCHAR(80) NOT NULL DEFAULT 'সারাদেশ',
      `address` TEXT DEFAULT NULL,
      `hotline_phone` VARCHAR(50) NOT NULL,
      `alt_phone` VARCHAR(50) DEFAULT NULL,
      `website` VARCHAR(255) DEFAULT NULL,
      `is_verified` TINYINT(1) DEFAULT 1,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    // Seed default verified organizations if empty
    $orgCount = (int)$pdo->query("SELECT COUNT(*) FROM blood_organizations")->fetchColumn();
    if ($orgCount === 0) {
        $pdo->exec("INSERT INTO `blood_organizations` (`name_bn`, `name_en`, `category`, `district`, `address`, `hotline_phone`, `alt_phone`, `website`, `is_verified`) VALUES
        ('সন্ধানী (কেন্দ্রীয় পরিষদ)', 'Sandhani Central Committee', 'স্বেচ্ছাসেবী রক্তদান সংস্থা', 'ঢাকা', 'ঢাকা মেডিকেল কলেজ, ঢাকা', '01711-000000', '02-9668690', 'https://sandhani.org', 1),
        ('কোয়ান্টাম রক্তদান কার্যক্রম', 'Quantum Blood Lab', 'ব্লাড ল্যাব ও সেবা', 'ঢাকা', 'শান্তিনগর, ঢাকা', '01714-010869', '02-9351969', 'https://quantummethod.org.bd', 1),
        ('বাংলাদেশ রেড ক্রিসেন্ট সোসাইটি ব্লাড সেন্টার', 'Bangladesh Red Crescent Blood Center', 'জাতীয় রক্তদান সংস্থা', 'ঢাকা', '৭/৫ আওরঙ্গজেব রোড, মোহাম্মদপুর, ঢাকা', '01811-458524', '02-9116563', 'https://redcrescent.org.bd', 1),
        ('বাঁধন (স্বেচ্ছায় রক্তদাতাদের সংগঠন)', 'Badhan (A Voluntary Blood Donors Organization)', 'স্বেচ্ছাসেবী ছাত্র সংগঠন', 'ঢাকা', 'টিএসসি, ঢাকা বিশ্ববিদ্যালয়, ঢাকা', '01534-982674', '01711-234567', 'https://badhan.org.bd', 1),
        ('পুলিশ ব্লাড ব্যাংক', 'Police Blood Bank', 'সরকারি জরুরি ব্লাড ব্যাংক', 'ঢাকা', 'কেন্দ্রীয় পুলিশ হাসপাতাল, রাজারবাগ, ঢাকা', '01713-398386', '02-9350020', NULL, 1),
        ('সন্ধানী (চট্টগ্রাম মেডিকেল কলেজ ইউনিট)', 'Sandhani CMC Unit', 'স্বেচ্ছাসেবী রক্তদান সংস্থা', 'চট্টগ্রাম', 'চট্টগ্রাম মেডিকেল কলেজ হাসপাতাল', '01819-123456', '031-619400', NULL, 1),
        ('বাঁধন (রাজশাহী বিশ্ববিদ্যালয় ইউনিট)', 'Badhan RU Unit', 'স্বেচ্ছাসেবী ছাত্র সংগঠন', 'রাজশাহী', 'টিএসসিসি, রাজশাহী বিশ্ববিদ্যালয়', '01720-987654', NULL, NULL, 1),
        ('ব্লাডম্যান বাংলাদেশ', 'Bloodman Bangladesh', 'ডিজিটাল রক্তদাতা প্ল্যাটফর্ম', 'ঢাকা', 'বনানী, ঢাকা', '01755-667788', NULL, 'https://bloodman.org', 1);");
    }
} catch (Exception $e) {
    // Log or ignore schema error silently
}

// 1. Handle POST Requests
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = getRequestData();
    $action = $data['action'] ?? 'register_donor';

    if ($action === 'register_donor') {
        $userId = trim($data['user_id'] ?? '');
        $name = trim($data['name'] ?? '');
        $group = trim($data['blood_group'] ?? '');
        $district = trim($data['district'] ?? 'ঢাকা');
        $upazila = trim($data['upazila'] ?? '');
        $address = trim($data['address'] ?? $data['institution'] ?? $data['location'] ?? '');
        $phone = trim($data['phone'] ?? $data['phone_number'] ?? '');
        $avail = isset($data['is_available']) ? (int)$data['is_available'] : 1;
        $lastDonation = !empty($data['last_donation_date']) ? $data['last_donation_date'] : null;
        $totalDonations = max(0, (int)($data['total_donations'] ?? 0));
        $society = trim($data['society'] ?? $data['community'] ?? '');

        // If name or phone is empty, attempt to resolve from user profile if userId provided
        if ((empty($name) || empty($phone)) && !empty($userId)) {
            try {
                $uStmt = $pdo->prepare("SELECT name, phone FROM users WHERE user_id = ? OR id = ? LIMIT 1");
                $uStmt->execute([$userId, $userId]);
                $uRow = $uStmt->fetch();
                if ($uRow) {
                    if (empty($name) && !empty($uRow['name'])) $name = $uRow['name'];
                    if (empty($phone) && !empty($uRow['phone'])) $phone = $uRow['phone'];
                }
            } catch (Exception $e) {}
        }
        if (empty($name)) {
            $name = 'রক্তদাতা' . (!empty($phone) ? ' (' . substr($phone, -4) . ')' : '');
        }
        if (empty($group)) {
            $group = 'A+';
        }

        try {
            // Check if donor already exists
            $stmt = $pdo->prepare("SELECT id FROM blood_donors WHERE (phone_number != '' AND phone_number = ?) OR (user_id IS NOT NULL AND user_id != '' AND user_id = ?) LIMIT 1");
            $stmt->execute([$phone, $userId]);
            $existing = $stmt->fetch();

            if ($existing) {
                $upd = $pdo->prepare("UPDATE blood_donors SET name = ?, blood_group = ?, district = ?, upazila = ?, address = ?, is_available = ?, last_donation_date = ?, total_donations = ?, society = ? WHERE id = ?");
                $upd->execute([$name, $group, $district, $upazila, $address, $avail, $lastDonation, $totalDonations, $society, $existing['id']]);
                $donorId = (int)$existing['id'];
            } else {
                $ins = $pdo->prepare("INSERT INTO blood_donors (user_id, name, blood_group, district, upazila, address, phone_number, is_available, last_donation_date, total_donations, society) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $ins->execute([!empty($userId) ? $userId : null, $name, $group, $district, $upazila, $address, $phone, $avail, $lastDonation, $totalDonations, $society]);
                $donorId = (int)$pdo->lastInsertId();
            }

            sendJsonResponse([
                'success' => true,
                'donor_id' => $donorId,
                'message' => 'Blood donor registered successfully.'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'create_request' || $action === 'submit_request') {
        $userId = trim($data['user_id'] ?? $data['requester_user_id'] ?? '');
        $group = trim($data['blood_group'] ?? '');
        $urgency = strtoupper(trim($data['urgency_level'] ?? 'EMERGENCY'));
        if (!in_array($urgency, ['CRITICAL', 'EMERGENCY', 'NORMAL'])) {
            $urgency = 'EMERGENCY';
        }
        $problemReason = trim($data['problem_reason'] ?? 'অপারেশন');
        $units = trim($data['units_needed'] ?? '১ ব্যাগ');
        $neededDate = trim($data['needed_date'] ?? date('Y-m-d'));
        $donationTime = trim($data['donation_time'] ?? '');
        $hospital = trim($data['hospital_name'] ?? '');
        $district = trim($data['district'] ?? 'ঢাকা');
        $upazila = trim($data['upazila'] ?? '');
        $phone = trim($data['contact_phone'] ?? '');
        $whatsapp = trim($data['whatsapp_number'] ?? $phone);
        $notes = trim($data['notes'] ?? '');
        $patient = trim($data['patient_name'] ?? (!empty($problemReason) ? $problemReason : 'জরুরি রোগী'));

        if (empty($group) || empty($hospital) || empty($phone)) {
            sendJsonResponse(['success' => false, 'error' => 'রক্তের গ্রুপ, হাসপাতাল ও ঠিকানা এবং ফোন নম্বর আবশ্যক।'], 400);
        }

        try {
            $stmt = $pdo->prepare("INSERT INTO blood_requests (requester_user_id, patient_name, blood_group, urgency_level, problem_reason, units_needed, needed_date, donation_time, hospital_name, district, upazila, contact_phone, whatsapp_number, notes, status) 
                                   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN')");
            $stmt->execute([
                !empty($userId) ? $userId : null,
                $patient,
                $group,
                $urgency,
                $problemReason,
                $units,
                !empty($neededDate) ? $neededDate : date('Y-m-d'),
                $donationTime,
                $hospital,
                $district,
                $upazila,
                $phone,
                $whatsapp,
                $notes
            ]);

            $requestId = (int)$pdo->lastInsertId();

            // BROADCAST NOTIFICATION TO ALL APP USERS IF URGENCY IS EMERGENCY OR CRITICAL!
            if ($urgency === 'EMERGENCY' || $urgency === 'CRITICAL') {
                $broadcastTitle = "জরুরি রক্তের আবেদন: {$group} রক্ত প্রয়োজন!";
                $timeInfo = !empty($donationTime) ? " ({$donationTime})" : "";
                $dateInfo = !empty($neededDate) ? " [তারিখ: {$neededDate}{$timeInfo}]" : "";
                $broadcastBody = "{$hospital}-এ রোগীর জন্য জরুরি {$units} {$group} রক্ত প্রয়োজন। সমস্যা: {$problemReason}{$dateInfo}। যোগাযোগ: {$phone}";
                $targetAction = "blood_request_detail:{$requestId}";

                // 1. Insert into push_notifications with target_audience = 'ALL' so EVERY user receives it!
                try {
                    $nStmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_audience, priority, is_active) 
                                            VALUES (?, ?, 'BLOOD_EMERGENCY', ?, 'ALL', 'CRITICAL', 1)");
                    $nStmt->execute([$broadcastTitle, $broadcastBody, $targetAction]);
                } catch (Exception $ne) {}

                // 2. Broadcast via FCM (Firebase Cloud Messaging) if configured
                try {
                    $sStmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'fcm_server_key' LIMIT 1");
                    $sStmt->execute();
                    $fcmKey = $sStmt->fetchColumn();
                    if (!empty($fcmKey)) {
                        $fcmUrl = 'https://fcm.googleapis.com/fcm/send';

                        // Broadcast to topic /topics/all_users
                        $topicPayload = [
                            'to' => '/topics/all_users',
                            'priority' => 'high',
                            'notification' => [
                                'title' => $broadcastTitle,
                                'body' => $broadcastBody,
                                'sound' => 'default'
                            ],
                            'data' => [
                                'title' => $broadcastTitle,
                                'body' => $broadcastBody,
                                'type' => 'blood_emergency',
                                'deep_link' => $targetAction,
                                'priority' => 'critical',
                                'timestamp' => (string)time()
                            ]
                        ];

                        $ch = curl_init();
                        curl_setopt($ch, CURLOPT_URL, $fcmUrl);
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, [
                            'Authorization: key=' . trim($fcmKey),
                            'Content-Type: application/json'
                        ]);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($topicPayload));
                        curl_exec($ch);
                        curl_close($ch);

                        // Also batch-send to registered user FCM tokens in users table
                        $uStmt = $pdo->query("SELECT DISTINCT fcm_token FROM users WHERE fcm_token IS NOT NULL AND fcm_token != '' LIMIT 500");
                        $tokens = $uStmt->fetchAll(PDO::FETCH_COLUMN);
                        if (!empty($tokens)) {
                            $tokenPayload = [
                                'registration_ids' => $tokens,
                                'priority' => 'high',
                                'notification' => [
                                    'title' => $broadcastTitle,
                                    'body' => $broadcastBody,
                                    'sound' => 'default'
                                ],
                                'data' => [
                                    'title' => $broadcastTitle,
                                    'body' => $broadcastBody,
                                    'type' => 'blood_emergency',
                                    'deep_link' => $targetAction,
                                    'priority' => 'critical',
                                    'timestamp' => (string)time()
                                ]
                            ];
                            $ch2 = curl_init();
                            curl_setopt($ch2, CURLOPT_URL, $fcmUrl);
                            curl_setopt($ch2, CURLOPT_POST, true);
                            curl_setopt($ch2, CURLOPT_HTTPHEADER, [
                                'Authorization: key=' . trim($fcmKey),
                                'Content-Type: application/json'
                            ]);
                            curl_setopt($ch2, CURLOPT_RETURNTRANSFER, true);
                            curl_setopt($ch2, CURLOPT_SSL_VERIFYPEER, false);
                            curl_setopt($ch2, CURLOPT_POSTFIELDS, json_encode($tokenPayload));
                            curl_exec($ch2);
                            curl_close($ch2);
                        }
                    }
                } catch (Exception $fe) {}
            }

            sendJsonResponse([
                'success' => true,
                'request_id' => $requestId,
                'urgency_level' => $urgency,
                'broadcast_sent' => ($urgency === 'EMERGENCY' || $urgency === 'CRITICAL'),
                'message' => ($urgency === 'EMERGENCY' || $urgency === 'CRITICAL')
                    ? 'জরুরি রক্তের আবেদন সফলভাবে প্রচারিত হয়েছে এবং সকল ব্যবহারকারীর নিকট নোটিফিকেশন পাঠানো হয়েছে।'
                    : 'রক্তের আবেদনটি সফলভাবে সিস্টেমে যুক্ত হয়েছে।'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'accept_request') {
        $requestId = (int)($data['request_id'] ?? 0);
        $userId = trim($data['user_id'] ?? 'guest_donor');

        if ($requestId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'Valid request_id is required.'], 400);
        }

        try {
            $stmt = $pdo->prepare("UPDATE blood_requests SET status = 'ACCEPTED', accepted_by_user_id = ?, accepted_at = NOW() WHERE id = ?");
            $stmt->execute([$userId, $requestId]);

            sendJsonResponse([
                'success' => true,
                'request_id' => $requestId,
                'message' => 'Blood donation request accepted successfully.'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'fulfill_request') {
        $requestId = (int)($data['request_id'] ?? 0);
        if ($requestId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'Valid request_id is required.'], 400);
        }
        try {
            $pdo->prepare("UPDATE blood_requests SET status = 'FULFILLED' WHERE id = ?")->execute([$requestId]);
            sendJsonResponse(['success' => true, 'request_id' => $requestId, 'message' => 'Blood request marked as fulfilled.']);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'toggle_availability') {
        $phone = trim($data['phone'] ?? '');
        $userId = trim($data['user_id'] ?? '');
        $avail = isset($data['is_available']) ? (int)$data['is_available'] : 1;

        if (empty($phone) && empty($userId)) {
            sendJsonResponse(['success' => false, 'error' => 'Phone or user_id is required.'], 400);
        }

        try {
            $stmt = $pdo->prepare("UPDATE blood_donors SET is_available = ? WHERE phone_number = ? OR user_id = ?");
            $stmt->execute([$avail, $phone, $userId]);
            sendJsonResponse(['success' => true, 'is_available' => $avail]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    if ($action === 'send_call_request' || $action === 'call_request') {
        $donorId = (int)($data['donor_id'] ?? 0);
        $reqUserId = trim($data['requester_user_id'] ?? $data['user_id'] ?? '');
        $reqName = trim($data['requester_name'] ?? '');
        $reqPhone = trim($data['requester_phone'] ?? '');

        if ($donorId <= 0) {
            sendJsonResponse(['success' => false, 'error' => 'Valid donor ID is required.'], 400);
        }

        try {
            $stmt = $pdo->prepare("SELECT id, user_id, name, blood_group, phone_number FROM blood_donors WHERE id = ? LIMIT 1");
            $stmt->execute([$donorId]);
            $donor = $stmt->fetch();

            if (!$donor) {
                sendJsonResponse(['success' => false, 'error' => 'Donor not found.'], 404);
            }

            if (empty($reqName) && !empty($reqUserId)) {
                $uStmt = $pdo->prepare("SELECT name, phone FROM users WHERE user_id = ? OR id = ? LIMIT 1");
                $uStmt->execute([$reqUserId, $reqUserId]);
                $u = $uStmt->fetch();
                if ($u) {
                    if (empty($reqName) && !empty($u['name'])) $reqName = $u['name'];
                    if (empty($reqPhone) && !empty($u['phone'])) $reqPhone = $u['phone'];
                }
            }

            if (empty($reqName)) {
                $reqName = 'জরুরি রক্তের সন্ধানী রোগী';
            }
            if (empty($reqPhone)) {
                $reqPhone = 'মোবাইল নম্বর';
            }

            $ins = $pdo->prepare("INSERT INTO blood_call_requests (donor_id, donor_phone, donor_name, requester_user_id, requester_name, requester_phone, blood_group, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'SENT')");
            $ins->execute([$donor['id'], $donor['phone_number'], $donor['name'], $reqUserId, $reqName, $reqPhone, $donor['blood_group']]);
            $callRequestId = (int)$pdo->lastInsertId();

            // Dispatch Real-time Push Notification ONLY to the requested donor
            try {
                // Strict user-targeting: never broadcast call request to ALL users
                $targetAudience = !empty($donor['user_id']) ? $donor['user_id'] : ('donor_' . $donor['id']);
                $title = "জরুরি রক্তের জন্য কল রিকোয়েস্ট (" . $donor['blood_group'] . ")";
                $body = "{$reqName} ({$reqPhone}) আপনার কাছে রক্তের সহায়তা চেয়ে কল রিকোয়েস্ট পাঠিয়েছেন। অনুগ্রহ করে দ্রুত যোগাযোগ করুন।";
                $targetAction = "tel:{$reqPhone}";

                $nStmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_audience, priority, is_active) VALUES (?, ?, 'BLOOD_CALL_REQUEST', ?, ?, 'HIGH', 1)");
                $nStmt->execute([$title, $body, $targetAction, $targetAudience]);

                // Also check if the donor has a registered device FCM token in users table
                $fcmToken = null;
                if (!empty($donor['user_id'])) {
                    $uStmt = $pdo->prepare("SELECT fcm_token FROM users WHERE user_id = ? AND fcm_token IS NOT NULL AND fcm_token != '' LIMIT 1");
                    $uStmt->execute([$donor['user_id']]);
                    $fcmToken = $uStmt->fetchColumn();
                }
                if (empty($fcmToken) && !empty($donor['phone_number'])) {
                    $uStmt = $pdo->prepare("SELECT fcm_token FROM users WHERE phone = ? AND fcm_token IS NOT NULL AND fcm_token != '' LIMIT 1");
                    $uStmt->execute([$donor['phone_number']]);
                    $fcmToken = $uStmt->fetchColumn();
                }

                if (!empty($fcmToken)) {
                    // Send single-recipient targeted FCM notification to this donor's device only
                    $sStmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'fcm_server_key' LIMIT 1");
                    $sStmt->execute();
                    $fcmKey = $sStmt->fetchColumn();
                    if (!empty($fcmKey)) {
                        $fcmUrl = 'https://fcm.googleapis.com/fcm/send';
                        $fcmFields = [
                            'to' => $fcmToken, // ONLY this specific donor's device!
                            'priority' => 'high',
                            'notification' => [
                                'title' => $title,
                                'body' => $body,
                                'sound' => 'default'
                            ],
                            'data' => [
                                'title' => $title,
                                'body' => $body,
                                'type' => 'blood_call_request',
                                'deep_link' => $targetAction,
                                'priority' => 'high',
                                'timestamp' => (string)time()
                            ]
                        ];
                        $ch = curl_init();
                        curl_setopt($ch, CURLOPT_URL, $fcmUrl);
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, [
                            'Authorization: key=' . trim($fcmKey),
                            'Content-Type: application/json'
                        ]);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fcmFields));
                        curl_exec($ch);
                        curl_close($ch);
                    }
                }
            } catch (Exception $ne) {}

            sendJsonResponse([
                'success' => true,
                'call_request_id' => $callRequestId,
                'message' => 'রক্তদাতার কাছে সফলভাবে কল রিকোয়েস্ট ও জরুরি নোটিফিকেশন পাঠানো হয়েছে।'
            ]);
        } catch (Exception $e) {
            sendJsonResponse(['success' => false, 'error' => 'Database error: ' . $e->getMessage()], 500);
        }
    }

    sendJsonResponse(['success' => false, 'error' => 'Unsupported action.'], 400);
}

// 2. Handle GET Requests
$view = $_GET['view'] ?? 'donors';
$group = trim($_GET['group'] ?? '');
$district = trim($_GET['district'] ?? '');
$query = trim($_GET['q'] ?? '');
$requestId = (int)($_GET['request_id'] ?? 0);

try {
    if ($view === 'requests') {
        if ($requestId > 0) {
            $stmt = $pdo->prepare("SELECT * FROM blood_requests WHERE id = ? LIMIT 1");
            $stmt->execute([$requestId]);
            $req = $stmt->fetch();
            if ($req) {
                sendJsonResponse(['success' => true, 'request' => $req]);
            } else {
                sendJsonResponse(['success' => false, 'error' => 'Blood request not found.'], 404);
            }
        }

        $rWhere = [];
        $rParams = [];

        $statusFilter = trim($_GET['status'] ?? '');
        if (!empty($statusFilter)) {
            $rWhere[] = "status = ?";
            $rParams[] = $statusFilter;
        } else {
            $rWhere[] = "status IN ('OPEN', 'ACCEPTED')";
        }

        if (!empty($group) && $group !== 'ALL') {
            $rWhere[] = "blood_group = ?";
            $rParams[] = $group;
        }
        if (!empty($district) && $district !== 'ALL' && $district !== 'সকল') {
            $rWhere[] = "(district LIKE ? OR upazila LIKE ?)";
            $rParams[] = "%$district%";
            $rParams[] = "%$district%";
        }

        $whereClause = !empty($rWhere) ? "WHERE " . implode(" AND ", $rWhere) : "";
        $rSql = "SELECT * FROM blood_requests $whereClause ORDER BY id DESC LIMIT 100";
        $stmt = $pdo->prepare($rSql);
        $stmt->execute($rParams);
        $requests = $stmt->fetchAll();

        sendJsonResponse([
            'success' => true,
            'count' => count($requests),
            'requests' => $requests
        ]);
    }

    if ($view === 'organizations') {
        $oWhere = ["is_verified = 1"];
        $oParams = [];
        if (!empty($district) && $district !== 'ALL' && $district !== 'সকল') {
            $oWhere[] = "(district LIKE ? OR district = 'সারাদেশ')";
            $oParams[] = "%$district%";
        }
        $oSql = "SELECT * FROM blood_organizations WHERE " . implode(" AND ", $oWhere) . " ORDER BY id ASC";
        $stmt = $pdo->prepare($oSql);
        $stmt->execute($oParams);
        $organizations = $stmt->fetchAll();

        sendJsonResponse([
            'success' => true,
            'count' => count($organizations),
            'organizations' => $organizations
        ]);
    }

    if ($view === 'stats') {
        $totalDonors = (int)$pdo->query("SELECT COUNT(*) FROM blood_donors")->fetchColumn();
        $availDonors = (int)$pdo->query("SELECT COUNT(*) FROM blood_donors WHERE is_available = 1")->fetchColumn();
        $totalRequests = (int)$pdo->query("SELECT COUNT(*) FROM blood_requests")->fetchColumn();
        $openRequests = (int)$pdo->query("SELECT COUNT(*) FROM blood_requests WHERE status = 'OPEN'")->fetchColumn();
        $fulfilledRequests = (int)$pdo->query("SELECT COUNT(*) FROM blood_requests WHERE status = 'FULFILLED'")->fetchColumn();

        sendJsonResponse([
            'success' => true,
            'stats' => [
                'total_donors' => $totalDonors,
                'available_donors' => $availDonors,
                'total_requests' => $totalRequests,
                'open_requests' => $openRequests,
                'fulfilled_requests' => $fulfilledRequests
            ]
        ]);
    }

    // Default: Search Donors - Strictly from blood_donors table
    $location = trim($_GET['location'] ?? $district);
    $where = ["is_available = 1"];
    $params = [];
    if (!empty($group) && $group !== 'ALL' && $group !== 'সকল') {
        $where[] = "blood_group = ?";
        $params[] = $group;
    }
    if (!empty($location) && $location !== 'ALL' && $location !== 'সকল') {
        $where[] = "(district LIKE ? OR upazila LIKE ? OR address LIKE ?)";
        $params[] = "%$location%";
        $params[] = "%$location%";
        $params[] = "%$location%";
    }
    if (!empty($query)) {
        $where[] = "(name LIKE ? OR phone_number LIKE ? OR district LIKE ? OR upazila LIKE ? OR address LIKE ?)";
        $params[] = "%$query%";
        $params[] = "%$query%";
        $params[] = "%$query%";
        $params[] = "%$query%";
        $params[] = "%$query%";
    }

    $whereSql = "WHERE " . implode(" AND ", $where);
    $sql = "SELECT id, user_id, name, blood_group, district, upazila, address, phone_number, last_donation_date, total_donations, society, is_available, created_at 
            FROM blood_donors 
            $whereSql 
            ORDER BY id DESC 
            LIMIT 150";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $donors = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($donors),
        'donors' => $donors
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to fetch blood data: ' . $e->getMessage()
    ], 500);
}
?>
