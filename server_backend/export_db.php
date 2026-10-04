<?php
/**
 * ==============================================================================
 * DEEN ONE - PERMANENT DATABASE EXPORTER (UTF-8 NO BOM / CPANEL COMPATIBLE)
 * Exports MySQL database to server_backend/deenone_db.sql with 100% UTF-8
 * encoding compatibility for phpMyAdmin, MariaDB, and MySQL servers.
 * ==============================================================================
 */

$mysqldumpPath = 'C:\\xampp\\mysql\\bin\\mysqldump.exe';
if (!file_exists($mysqldumpPath)) {
    $mysqldumpPath = 'mysqldump';
}

$outputFile = __DIR__ . '/deenone_db.sql';
$cmd = sprintf(
    '"%s" -h 127.0.0.1 -P 3306 -u root --default-character-set=utf8mb4 --add-drop-table --skip-lock-tables --result-file="%s" deenonet_db',
    $mysqldumpPath,
    $outputFile
);

exec($cmd, $output, $returnCode);

if ($returnCode === 0 && file_exists($outputFile)) {
    // Verify pure UTF-8 without BOM
    $content = file_get_contents($outputFile);
    if (substr($content, 0, 3) === "\xEF\xBB\xBF") {
        $content = substr($content, 3);
        file_put_contents($outputFile, $content);
    }
    echo "SUCCESS: Database successfully exported to deenone_db.sql in pure UTF-8 (No BOM).\n";
    echo "File Size: " . number_format(filesize($outputFile)) . " bytes\n";
} else {
    echo "ERROR: Failed to export database (Exit Code: $returnCode).\n";
}
