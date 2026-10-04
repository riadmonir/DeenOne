<?php
$lines = file('server_backend/deenone_db.sql');
echo "Total lines: " . count($lines) . "\n";

$issues = [];
foreach ($lines as $num => $line) {
    // Check for \"' (escaped double quote before closing single quote)
    if (preg_match('/\\\\"[\\\\\']+\s*[,;\)]/', $line)) {
        $issues[] = ($num + 1) . ": " . trim($line);
    }
}

echo "Found " . count($issues) . " occurrences of \\\"' or similar:\n";
foreach ($issues as $iss) {
    echo $iss . "\n";
}
