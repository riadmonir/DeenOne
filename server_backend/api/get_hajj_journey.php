<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET HAJJ JOURNEY STAGES
 * Endpoint: GET /api/get_hajj_journey.php
 * Returns authentic 12 stages of Hajj Journey with full dynamic image URLs.
 * ==============================================================================
 */
header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

require_once __DIR__ . '/db.php';

try {
    $pdo = getDbConnection();

    // Dynamic Base URL Calculation
    $protocol = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
    $scriptDir = dirname(dirname($_SERVER['SCRIPT_NAME'] ?? ''));
    $scriptDir = str_replace('\\', '/', $scriptDir);
    if ($scriptDir === '/' || $scriptDir === '.' || strpos($scriptDir, ':') !== false) {
        $scriptDir = '';
    }
    $baseUrl = $protocol . '://' . $host . ($scriptDir !== '' ? ('/' . ltrim($scriptDir, '/')) : '');

    $sql = "SELECT id, stage_number, stage_number_bn, stage_number_en,
                   title_bn, title_en, description_bn, description_en,
                   image_url, star_color_hex, details_bn, details_en,
                   dua_arabic, dua_pronunciation_bn, dua_pronunciation_en,
                   dua_meaning_bn, dua_meaning_en, reference,
                   is_active, display_order
            FROM hajj_journey_stages
            WHERE is_active = 1
            ORDER BY display_order ASC, stage_number ASC";

    $stmt = $pdo->query($sql);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    $stages = [];
    foreach ($rows as $row) {
        $img = $row['image_url'];
        if (!empty($img) && !preg_match('/^https?:\/\//i', $img)) {
            $row['full_image_url'] = $baseUrl . '/' . ltrim($img, '/');
        } else {
            $row['full_image_url'] = $img;
        }
        $row['stage_number'] = (int)$row['stage_number'];
        $stages[] = $row;
    }

    echo json_encode([
        'status' => 'success',
        'code' => 200,
        'count' => count($stages),
        'stages' => $stages
    ], JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'status' => 'error',
        'code' => 500,
        'message' => 'Failed to retrieve Hajj Journey stages: ' . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}
