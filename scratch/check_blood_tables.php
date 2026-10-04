<?php
require_once __DIR__ . '/../server_backend/api/db.php';
$pdo = getDbConnection();
$tables = $pdo->query('SHOW TABLES')->fetchAll(PDO::FETCH_COLUMN);
echo "TABLES:\n" . implode(", ", $tables) . "\n\n";

foreach (['blood_donors', 'blood_requests', 'blood_organizations', 'blood_guidelines'] as $t) {
    if (in_array($t, $tables)) {
        echo "TABLE: $t\n";
        $cols = $pdo->query("DESCRIBE $t")->fetchAll(PDO::FETCH_ASSOC);
        foreach ($cols as $c) {
            echo "  " . $c['Field'] . " (" . $c['Type'] . ")\n";
        }
    } else {
        echo "TABLE $t DOES NOT EXIST\n";
    }
}
