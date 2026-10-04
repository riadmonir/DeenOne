<?php
/**
 * ==============================================================================
 * DEEN ONE - ISLAMIC BOOKS SQLITE DATABASE BRIDGE HELPER
 * ==============================================================================
 * Bridges PHP REST APIs with database/islamic_books.db
 * Supports local cache and automatic fetch from GitHub CDN / jsdelivr CDN.
 */

function getIslamicBooksDbPdo() {
    static $bDb = null;
    if ($bDb !== null) {
        return $bDb;
    }

    $rootDir = dirname(__DIR__, 2);
    $primaryDbPath = $rootDir . '/database/islamic_books.db';
    $primaryGzPath = $rootDir . '/database/islamic_books.db.gz';
    $legacyDbPath  = __DIR__ . '/../data/islamic_books.db';

    $dbPath = $primaryDbPath;

    if (file_exists($primaryDbPath) && filesize($primaryDbPath) > 50000) {
        $dbPath = $primaryDbPath;
    } elseif (file_exists($legacyDbPath) && filesize($legacyDbPath) > 50000) {
        $dbPath = $legacyDbPath;
    } else {
        $targetDir = dirname($primaryDbPath);
        if (!is_dir($targetDir)) {
            @mkdir($targetDir, 0755, true);
        }

        // Auto extract from GZ if available
        if (file_exists($primaryGzPath) && filesize($primaryGzPath) > 10000) {
            $uncompressed = @gzdecode(file_get_contents($primaryGzPath));
            if ($uncompressed !== false && strlen($uncompressed) > 50000) {
                @file_put_contents($primaryDbPath, $uncompressed);
                $dbPath = $primaryDbPath;
            }
        }

        // Auto-fetch from GitHub CDN if still missing
        if (!file_exists($dbPath) || filesize($dbPath) < 20000) {
            $githubUrls = [
                'https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/islamic_books.db.gz',
                'https://cdn.jsdelivr.net/gh/riadmonir/DeenOne@main/database/islamic_books.db.gz',
                'https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/islamic_books.db',
                'https://cdn.jsdelivr.net/gh/riadmonir/DeenOne@main/database/islamic_books.db'
            ];
            foreach ($githubUrls as $url) {
                $ctx = stream_context_create([
                    'http' => [
                        'timeout' => 30,
                        'user_agent' => 'DeenOne-Books-Server/1.0'
                    ]
                ]);
                $content = @file_get_contents($url, false, $ctx);
                if ($content !== false && strlen($content) > 20000) {
                    if (str_ends_with($url, '.gz')) {
                        $decomp = @gzdecode($content);
                        if ($decomp !== false) {
                            @file_put_contents($primaryDbPath, $decomp);
                            $dbPath = $primaryDbPath;
                            break;
                        }
                    } else {
                        @file_put_contents($primaryDbPath, $content);
                        $dbPath = $primaryDbPath;
                        break;
                    }
                }
            }
        }
    }

    if (!file_exists($dbPath)) {
        return null;
    }

    try {
        $bDb = new PDO('sqlite:' . $dbPath);
        $bDb->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $bDb->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        return $bDb;
    } catch (Exception $e) {
        return null;
    }
}
?>
