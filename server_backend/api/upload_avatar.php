<?php
/**
 * ==============================================================================
 * DEEN ONE API - PROFILE AVATAR UPLOAD HANDLER
 * Endpoint: POST /api/upload_avatar.php
 * Supports GIF, PNG, JPG, JPEG, and WEBP formats via Multipart or Base64.
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    sendJsonResponse(['success' => false, 'error' => 'POST method required.'], 405);
}

$userId = trim($_POST['user_id'] ?? '');
$targetDir = dirname(__DIR__) . '/uploads/avatars';
if (!is_dir($targetDir)) {
    @mkdir($targetDir, 0755, true);
}

// 1. Check for multipart file upload
if (!empty($_FILES['avatar']) && $_FILES['avatar']['error'] === UPLOAD_ERR_OK) {
    $tmpName = $_FILES['avatar']['tmp_name'];
    $originalName = $_FILES['avatar']['name'] ?? 'avatar.jpg';
    
    // Determine extension from MIME or filename
    $ext = 'jpg';
    $mimeType = '';
    if (function_exists('finfo_open')) {
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mimeType = finfo_file($finfo, $tmpName);
        finfo_close($finfo);
    }
    if (empty($mimeType) && function_exists('mime_content_type')) {
        $mimeType = @mime_content_type($tmpName);
    }

    $mimeMap = [
        'image/jpeg' => 'jpg',
        'image/pjpeg' => 'jpg',
        'image/png'  => 'png',
        'image/gif'  => 'gif',
        'image/webp' => 'webp'
    ];

    if (!empty($mimeType) && isset($mimeMap[$mimeType])) {
        $ext = $mimeMap[$mimeType];
    } else {
        $origExt = strtolower(pathinfo($originalName, PATHINFO_EXTENSION));
        if (in_array($origExt, ['gif', 'png', 'jpg', 'jpeg', 'webp'])) {
            $ext = ($origExt === 'jpeg') ? 'jpg' : $origExt;
        } else {
            sendJsonResponse(['success' => false, 'error' => 'শুধুমাত্র GIF, PNG, JPG এবং WEBP ছবি সমর্থিত। (Only GIF, PNG, JPG, and WEBP are supported)'], 400);
        }
    }

    if (empty($userId)) {
        $userId = trim($_POST['user_id'] ?? 'usr_' . time());
    }

    $safeUserId = preg_replace('/[^a-zA-Z0-9_-]/', '', $userId);
    $filename = 'avatar_' . $safeUserId . '_' . time() . '.' . $ext;
    $targetPath = $targetDir . '/' . $filename;

    if (move_uploaded_file($tmpName, $targetPath)) {
        $relativeUrl = 'uploads/avatars/' . $filename;

        // Update database (users table and community feeds)
        try {
            $stmt = $pdo->prepare("UPDATE users SET avatar = ?, last_active = NOW() WHERE user_id = ?");
            $stmt->execute([$relativeUrl, $userId]);

            // Synchronize author avatar across community posts and comments
            $pdo->prepare("UPDATE community_posts SET author_avatar = ? WHERE user_id = ?")->execute([$relativeUrl, $userId]);
            $pdo->prepare("UPDATE community_comments SET author_avatar = ? WHERE user_id = ?")->execute([$relativeUrl, $userId]);
        } catch (Exception $e) {}

        sendJsonResponse([
            'success' => true,
            'avatar_url' => $relativeUrl,
            'format' => $ext,
            'message' => 'Profile picture uploaded successfully.'
        ]);
    } else {
        sendJsonResponse(['success' => false, 'error' => 'Failed to save uploaded image.'], 500);
    }
}

// 2. Check for JSON / Base64 upload
$data = getRequestData();
$userId = trim($data['user_id'] ?? $userId);
$rawBase64 = trim($data['avatar_base64'] ?? '');

if (!empty($rawBase64)) {
    $ext = 'jpg';
    if (strpos($rawBase64, 'data:image/png') !== false) {
        $ext = 'png';
    } elseif (strpos($rawBase64, 'data:image/gif') !== false) {
        $ext = 'gif';
    } elseif (strpos($rawBase64, 'data:image/webp') !== false) {
        $ext = 'webp';
    }

    $cleanBase64 = $rawBase64;
    if (strpos($cleanBase64, ',') !== false) {
        $parts = explode(',', $cleanBase64, 2);
        $cleanBase64 = $parts[1];
    }

    $decoded = base64_decode($cleanBase64);
    if ($decoded === false) {
        sendJsonResponse(['success' => false, 'error' => 'Invalid base64 image data.'], 400);
    }

    $safeUserId = preg_replace('/[^a-zA-Z0-9_-]/', '', !empty($userId) ? $userId : 'usr_' . time());
    $filename = 'avatar_' . $safeUserId . '_' . time() . '.' . $ext;
    $targetPath = $targetDir . '/' . $filename;

    if (file_put_contents($targetPath, $decoded) !== false) {
        $relativeUrl = 'uploads/avatars/' . $filename;

        // Update database
        try {
            $stmt = $pdo->prepare("UPDATE users SET avatar = ?, last_active = NOW() WHERE user_id = ?");
            $stmt->execute([$relativeUrl, $userId]);

            // Synchronize author avatar across community posts and comments
            $pdo->prepare("UPDATE community_posts SET author_avatar = ? WHERE user_id = ?")->execute([$relativeUrl, $userId]);
            $pdo->prepare("UPDATE community_comments SET author_avatar = ? WHERE user_id = ?")->execute([$relativeUrl, $userId]);
        } catch (Exception $e) {}

        sendJsonResponse([
            'success' => true,
            'avatar_url' => $relativeUrl,
            'format' => $ext,
            'message' => 'Profile picture uploaded successfully.'
        ]);
    } else {
        sendJsonResponse(['success' => false, 'error' => 'Failed to save base64 image.'], 500);
    }
}

sendJsonResponse(['success' => false, 'error' => 'No image file or base64 data provided.'], 400);
