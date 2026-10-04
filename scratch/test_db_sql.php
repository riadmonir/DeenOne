<?php
require_once __DIR__ . '/../server_backend/api/db.php';
try {
    $pdo = getDbConnection();
    echo "Connected successfully to MySQL.\n";
    $tables = $pdo->query("SHOW TABLES")->fetchAll(PDO::FETCH_COLUMN);
    echo "Tables in DB: " . implode(', ', $tables) . "\n";
    
    $donorCount = $pdo->query("SELECT COUNT(*) FROM blood_donors")->fetchColumn();
    $orgCount = $pdo->query("SELECT COUNT(*) FROM blood_organizations")->fetchColumn();
    echo "Blood Donors Count: $donorCount, Organizations Count: $orgCount\n";
} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
}
