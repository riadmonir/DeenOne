# -*- coding: utf-8 -*-
import subprocess
import os

php_script = os.path.join(os.path.dirname(__file__), "run_seed.php")
with open(php_script, "w", encoding="utf-8") as f:
    f.write("""<?php
require_once __DIR__ . '/../api/db.php';
require_once __DIR__ . '/seed_halal_foods.php';
$pdo = getDbConnection();
$pdo->exec('TRUNCATE TABLE halal_foods');
seedComprehensiveHalalFoods($pdo);
echo 'SUCCESS: Halal foods table truncated and re-seeded cleanly!';
""")

cmd = ["C:\\xampp\\php\\php.exe", php_script]
res = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8")
print(res.stdout)
if res.stderr:
    print("ERR:", res.stderr)

if os.path.exists(php_script):
    os.remove(php_script)
