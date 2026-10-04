<?php
/**
 * ==============================================================================
 * DEEN ONE API - MOSQUES & HALAL DIRECTORY
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$type = $_GET['type'] ?? 'mosques';
$district = trim($_GET['district'] ?? '');

try {
    if ($type === 'halal') {
        $where = ["is_verified = 1"];
        $params = [];
        if (!empty($district)) {
            $where[] = "district LIKE ?";
            $params[] = "%$district%";
        }
        $whereSql = "WHERE " . implode(" AND ", $where);
        $sql = "SELECT id, name, category, district, address, phone, halal_status FROM halal_places $whereSql ORDER BY id DESC LIMIT 50";
        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);
        $places = $stmt->fetchAll();

        sendJsonResponse([
            'success' => true,
            'count' => count($places),
            'places' => $places
        ]);
    }

    // Default: Mosques
    $where = ["is_verified = 1"];
    $params = [];
    if (!empty($district)) {
        $where[] = "district LIKE ?";
        $params[] = "%$district%";
    }
    $whereSql = "WHERE " . implode(" AND ", $where);
    $sql = "SELECT id, name, district, address, latitude, longitude, jummah_time, facilities FROM mosques $whereSql ORDER BY id DESC LIMIT 100";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $mosques = $stmt->fetchAll();

    sendJsonResponse([
        'success' => true,
        'count' => count($mosques),
        'mosques' => $mosques
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load directory: ' . $e->getMessage()
    ], 500);
}
?>
