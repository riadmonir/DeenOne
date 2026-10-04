<?php
/**
 * ==============================================================================
 * DEEN ONE - HADITHBD SQLITE DATABASE BRIDGE HELPER
 * ==============================================================================
 * Bridges PHP REST APIs with server_backend/data/hadithbd.db
 */

function getHadithDbPdo() {
    static $hDb = null;
    if ($hDb !== null) {
        return $hDb;
    }
    $dbPath = __DIR__ . '/../data/hadithbd.db';
    
    // Auto-fetch/extract from local GZ or GitHub CDN if uncompressed database is not present
    if (!file_exists($dbPath) || filesize($dbPath) < 1000000) {
        $dir = dirname($dbPath);
        if (!is_dir($dir)) {
            @mkdir($dir, 0755, true);
        }
        $gzPath = $dir . '/hadithbd.db.gz';
        if (file_exists($gzPath) && filesize($gzPath) > 5000000) {
            $uncompressed = @gzdecode(file_get_contents($gzPath));
            if ($uncompressed !== false && strlen($uncompressed) > 10000000) {
                @file_put_contents($dbPath, $uncompressed);
            }
        }
        
        if (!file_exists($dbPath) || filesize($dbPath) < 1000000) {
            $githubUrls = [
                'https://raw.githubusercontent.com/riadmonir/DeenOne/main/server_backend/data/hadithbd.db.gz',
                'https://cdn.jsdelivr.net/gh/riadmonir/DeenOne@main/server_backend/data/hadithbd.db.gz',
                'https://raw.githubusercontent.com/riadmonir/DeenOne/main/server_backend/data/hadithbd.db',
                'https://cdn.jsdelivr.net/gh/riadmonir/DeenOne@main/server_backend/data/hadithbd.db'
            ];
            foreach ($githubUrls as $url) {
                $ctx = stream_context_create([
                    'http' => [
                        'timeout' => 45,
                        'user_agent' => 'DeenOne-Server/1.0'
                    ]
                ]);
                $content = @file_get_contents($url, false, $ctx);
                if ($content !== false && strlen($content) > 1000000) {
                    if (str_ends_with($url, '.gz')) {
                        $decomp = @gzdecode($content);
                        if ($decomp !== false) {
                            @file_put_contents($dbPath, $decomp);
                            break;
                        }
                    } else {
                        @file_put_contents($dbPath, $content);
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
        $hDb = new PDO('sqlite:' . $dbPath);
        $hDb->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $hDb->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        return $hDb;
    } catch (Exception $e) {
        return null;
    }
}

function getHadithDbBookId($bookSlug) {
    $normalized = strtolower(trim((string)$bookSlug));
    $map = [
        'bukhari' => 1,
        'muslim' => 2,
        'nasai' => 3,
        'abu_dawood' => 4,
        'abudawud' => 4,
        'tirmidhi' => 5,
        'ibn_majah' => 6,
        'ibnmajah' => 6,
        'muwatta_malik' => 7,
        'malik' => 7,
        'riyadus_salihin' => 8,
        'bulughul_maram' => 9,
        'lulu_wal_marjan' => 10,
        'hadith_sambhar' => 11,
        'silsila_sahiha' => 12,
        'jal_o_daif_series' => 13,
        'mishkatul_masabih' => 14,
        'nawawi_40' => 15,
        'nawawi40' => 15,
        'adabul_mufrad' => 16,
        'rafayel_yadain' => 17,
        'hadithe_qudsi' => 18,
        '100_susabbasto_hadith' => 19,
        'mishkate_daif_hadith' => 20,
        'shamayele_tirmidhi' => 21,
        'sahih_at_targib' => 22,
        'sahih_fazayele_amal' => 23,
        'upodesh' => 24,
        'ramadaner_durbol_hadith' => 25
    ];
    return $map[$normalized] ?? null;
}

function getHadithGradeFromStatus($statusId) {
    switch ((int)$statusId) {
        case 1:
            return ['grade_bn' => 'সহিহ হাদিস', 'grade_en' => 'Sahih Hadith'];
        case 2:
            return ['grade_bn' => 'হাসান হাদিস', 'grade_en' => 'Hasan Hadith'];
        case 3:
            return ['grade_bn' => 'যঈফ হাদিস', 'grade_en' => "Da'if Hadith"];
        case 4:
            return ['grade_bn' => 'জাল হাদিস', 'grade_en' => 'Mawdu (Fabricated)'];
        case 5:
            return ['grade_bn' => 'সহিহ/যঈফ', 'grade_en' => 'Mixed'];
        case 7:
            return ['grade_bn' => 'মুনকার হাদিস', 'grade_en' => 'Munkar Hadith'];
        case 8:
            return ['grade_bn' => 'মুরাসাল হাদিস', 'grade_en' => 'Mursal Hadith'];
        default:
            return ['grade_bn' => 'সহিহ হাদিস', 'grade_en' => 'Sahih Hadith'];
    }
}

function cleanHadithText($html) {
    if (empty($html)) return '';
    $text = preg_replace('/<br\s*\/?>/i', "\n", $html);
    $text = preg_replace('/<\/p>/i', "\n\n", $text);
    $text = strip_tags($text);
    $text = html_entity_decode($text, ENT_QUOTES | ENT_HTML5, 'UTF-8');
    // Normalize excessive newlines
    $text = preg_replace("/\n{3,}/", "\n\n", trim($text));
    return $text;
}

function extractNarratorAndText($rawBangla) {
    $clean = cleanHadithText($rawBangla);
    // Remove leading number like "১। " or "1. "
    $clean = preg_replace('/^[০-৯0-9]+\।?\s*/u', '', $clean);
    
    $narrator = '';
    $text = $clean;
    
    // Check if starts with narrator attribution before "থেকে বর্ণিত" or "হতে বর্ণিত"
    if (preg_match('/^(.*?(?:থেকে বর্ণিত|হতে বর্ণিত)[,:]?)\s*(.*)$/us', $clean, $matches)) {
        $potentialNarrator = trim($matches[1]);
        if (mb_strlen($potentialNarrator, 'UTF-8') <= 180) {
            $narrator = $potentialNarrator;
            $text = trim($matches[2]);
        }
    }
    return [$narrator, $text];
}

function toBengaliDigit($number) {
    $bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    $enDigits = ['0','1','2','3','4','5','6','7','8','9'];
    return str_replace($enDigits, $bnDigits, (string)$number);
}
