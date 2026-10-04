<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET AUTHENTIC NAMAZ VISUAL GUIDE STEPS
 * Endpoint: GET /api/get_namaz_visual_steps.php
 * Supports: ?gender=male, ?gender=female, or all steps
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$gender = trim($_GET['gender'] ?? '');
$where = ["is_active = 1"];
$params = [];

if (!empty($gender) && in_array(strtolower($gender), ['male', 'female'])) {
    $where[] = "gender = ?";
    $params[] = strtolower($gender);
}

$whereSql = "WHERE " . implode(" AND ", $where);

try {
    // Dynamic Base URL Calculation
    $protocol = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
    $scriptDir = dirname(dirname($_SERVER['SCRIPT_NAME'] ?? ''));
    $scriptDir = str_replace('\\', '/', $scriptDir);
    if ($scriptDir === '/' || $scriptDir === '.' || strpos($scriptDir, ':') !== false) {
        $scriptDir = '';
    }
    $baseUrl = $protocol . '://' . $host . ($scriptDir !== '' ? ('/' . ltrim($scriptDir, '/')) : '');

    $sql = "SELECT id, gender, step_number, step_title_bn, step_title_en,
                   description_bn, description_en, dua_bn, dua_en, notes_bn, notes_en,
                   image_url, vertical_bias, is_active, display_order
            FROM namaz_visual_steps
            $whereSql
            ORDER BY gender ASC, display_order ASC, step_number ASC";
    
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    $steps = [];
    $maleSteps = [];
    $femaleSteps = [];

    foreach ($rows as $row) {
        $img = $row['image_url'];
        if (!empty($img) && !preg_match('/^https?:\/\//i', $img)) {
            $row['full_image_url'] = $baseUrl . '/' . ltrim($img, '/');
        } else {
            $row['full_image_url'] = $img;
        }
        $row['vertical_bias'] = (float)$row['vertical_bias'];
        $row['step_number'] = (int)$row['step_number'];

        $steps[] = $row;
        if ($row['gender'] === 'male') {
            $maleSteps[] = $row;
        } else {
            $femaleSteps[] = $row;
        }
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($steps),
        'male_count' => count($maleSteps),
        'female_count' => count($femaleSteps),
        'base_url' => $baseUrl,
        'steps' => $steps,
        'male_steps' => $maleSteps,
        'female_steps' => $femaleSteps
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load Namaz visual steps: ' . $e->getMessage()
    ], 500);
}
