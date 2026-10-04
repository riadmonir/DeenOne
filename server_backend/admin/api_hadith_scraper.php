<?php
/**
 * DeenOne Admin - Smart Hadith Scraper, Parser & Staging API Hub
 * Handles parsing, crawling, auto-translating, and staging/approving Hadiths
 */

header('Content-Type: application/json; charset=utf-8');

require_once dirname(__DIR__) . '/config.php';
require_once dirname(__DIR__) . '/api/db.php';
require_once __DIR__ . '/auth.php';

// Security check: Must be authenticated admin
if (!isAdminLoggedIn()) {
    http_response_code(401);
    echo json_encode(['success' => false, 'error' => 'অননুমোদিত অনুরোধ। লগইন করুন।']);
    exit;
}

$pdo = getDbConnection();

$action = $_POST['action'] ?? ($_GET['action'] ?? '');

function toBengaliNumber($num) {
    $en = ['0','1','2','3','4','5','6','7','8','9'];
    $bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    return str_replace($en, $bn, (string)$num);
}

function parseBengaliNumber($str) {
    $bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    $en = ['0','1','2','3','4','5','6','7','8','9'];
    $numStr = str_replace($bn, $en, (string)$str);
    preg_match('/\d+/', $numStr, $matches);
    return !empty($matches[0]) ? (int)$matches[0] : 0;
}

/**
 * Clean and normalize text from HadithBD HTML or text
 */
function cleanHadithText($html) {
    if (empty($html)) return '';
    $text = preg_replace('/<br\s*\/?>/i', "\n", $html);
    $text = preg_replace('/<\/p>/i', "\n\n", $text);
    $text = strip_tags($text);
    $text = html_entity_decode($text, ENT_QUOTES | ENT_HTML5, 'UTF-8');
    $text = preg_replace("/\r\n|\r/", "\n", $text);
    $text = preg_replace("/[ \t]+/", " ", $text);
    $text = preg_replace("/\n{3,}/", "\n\n", $text);
    return trim($text);
}

/**
 * Extract Narrator and pure Hadith text for Bengali
 */
function extractNarratorAndTextBn($rawBn) {
    $clean = cleanHadithText($rawBn);
    // Strip leading number markers like "৭। ", "৮। ", "[ ৯ ] "
    $clean = preg_replace('/^[০-৯0-9]+\s*[\।\|\.\:\-]\s*/u', '', $clean);

    $narrator = '';
    $text = $clean;

    if (preg_match('/^(.+?(?:থেকে বর্ণিত|হতে বর্ণিত|বলেন|বলেছেন|বর্ণনা করেছেন|বর্ণনা করেন)[\:\,\।\s]*)(.*)$/us', $clean, $m)) {
        $candNarr = trim($m[1]);
        $remText = trim($m[2]);
        if (mb_strlen($candNarr) <= 250 && !empty($remText)) {
            $narrator = preg_replace('/[\s,।\:]+$/u', '', $candNarr) . ':';
            $text = $remText;
        }
    }

    return [$narrator, $text];
}

/**
 * Extract Narrator and pure Hadith text for English
 */
function extractNarratorAndTextEn($rawEn) {
    $clean = cleanHadithText($rawEn);
    $narrator = '';
    $text = $clean;

    if (preg_match('/^(Narrated\s+[^:]+:\s*)(.*)$/is', $clean, $m)) {
        $narrator = trim($m[1]);
        $text = trim($m[2]);
    }

    return [$narrator, $text];
}

/**
 * Fast Google Dict Translation helper
 */
function translateTextToEn($text) {
    if (empty(trim($text))) return '';
    $clean = trim($text);
    $url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=bn&tl=en&q=' . urlencode($clean);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 6);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36');
    $response = curl_exec($ch);
    curl_close($ch);

    if (!empty($response)) {
        $json = json_decode($response, true);
        if (is_array($json) && !empty($json[0])) {
            return is_array($json[0]) ? implode(' ', $json[0]) : (string)$json[0];
        }
    }
    return '';
}

/**
 * Get authentic HadithBD SQLite database connection
 */
function getHadithBdConnection() {
    $dbPath = dirname(__DIR__) . '/data/hadithbd.db';
    if (!file_exists($dbPath) || filesize($dbPath) < 10000000) {
        $dataDir = dirname(__DIR__) . '/data';
        if (!is_dir($dataDir)) {
            @mkdir($dataDir, 0755, true);
        }
        $mirrorUrl = "https://raw.githubusercontent.com/showrav017/HadithBD/master/hadithbd/src/main/assets/db2.db";
        $ch = curl_init($mirrorUrl);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_TIMEOUT, 60);
        $content = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);
        if ($httpCode === 200 && !empty($content)) {
            file_put_contents($dbPath, $content);
        }
    }
    if (!file_exists($dbPath)) {
        throw new Exception("HadithBD ডাটাবেজ ফাইল (hadithbd.db) পাওয়া যায়নি।");
    }
    $hbd = new PDO('sqlite:' . $dbPath);
    $hbd->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    return $hbd;
}

/**
 * Match HadithBD section by chapter number
 */
function findHadithBdSection($hbd, $bookId, $chapterNum) {
    $stmt = $hbd->prepare("SELECT SectionID, SectionBD, SectionEN FROM hadithsection WHERE BookID = ? ORDER BY SectionID ASC");
    $stmt->execute([$bookId]);
    $sections = $stmt->fetchAll(PDO::FETCH_ASSOC);

    $bnNums = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    $enNums = ['0','1','2','3','4','5','6','7','8','9'];

    foreach ($sections as $s) {
        $cleanTitle = str_replace($bnNums, $enNums, $s['SectionBD']);
        if (preg_match('/^(\d+)\s*[\/|\.\:\-]/u', $cleanTitle, $m)) {
            if ((int)$m[1] === (int)$chapterNum) {
                return $s;
            }
        }
    }

    if (isset($sections[$chapterNum - 1])) {
        return $sections[$chapterNum - 1];
    }

    return null;
}

try {
    switch ($action) {

        // ---------------------------------------------------------------------
        // 1. PARSE RAW HTML / TEXT DIRECTLY COPIED FROM HADITHBD (hadithbd.com/hadith/)
        // ---------------------------------------------------------------------
        case 'parse_raw_content':
            $rawContent = trim($_POST['raw_content'] ?? '');
            $bookSlug = trim($_POST['book_slug'] ?? 'bukhari');
            $chapterNumber = (int)($_POST['chapter_number'] ?? 1);
            $sourceUrl = 'https://www.hadithbd.com/hadith/';

            if (empty($rawContent)) {
                echo json_encode(['success' => false, 'error' => 'কোনো টেক্সট বা HTML কন্টেন্ট দেওয়া হয়নি।']);
                exit;
            }

            // Fetch chapter info if available
            $cStmt = $pdo->prepare("SELECT title_bn, title_en FROM hadith_chapters WHERE book_slug = ? AND chapter_number = ? LIMIT 1");
            $cStmt->execute([$bookSlug, $chapterNumber]);
            $cRow = $cStmt->fetch();
            $chapterTitleBn = $cRow['title_bn'] ?? ('অধ্যায় ' . toBengaliNumber($chapterNumber));
            $chapterTitleEn = $cRow['title_en'] ?? ('Chapter ' . $chapterNumber);

            $bookNamesBn = [
                'bukhari' => 'সহীহ বুখারী',
                'muslim' => 'সহীহ মুসলিম',
                'abu_dawood' => 'সুনানে আবু দাউদ',
                'nasai' => 'সুনান আন-নাসায়ী',
                'ibn_majah' => 'সুনানে ইবনে মাজাহ',
                'tirmidhi' => 'জামে আত-তিরমিযী'
            ];
            $bookNameBn = $bookNamesBn[$bookSlug] ?? 'সহীহ হাদিস';

            $extractedItems = [];

            // Pattern A: JSON format
            $jsonData = json_decode($rawContent, true);
            if (is_array($jsonData)) {
                foreach ($jsonData as $item) {
                    if (!empty($item['bangla_text']) || !empty($item['arabic_text']) || !empty($item['text']) || !empty($item['BanglaHadith'])) {
                        $hadithNum = (int)($item['hadith_number'] ?? ($item['HadithNo'] ?? ($item['id'] ?? count($extractedItems) + 1)));
                        $rawBn = $item['bangla_text'] ?? ($item['BanglaHadith'] ?? ($item['bn'] ?? ($item['text'] ?? '')));
                        $rawEn = $item['english_text'] ?? ($item['EnglishHadith'] ?? ($item['en'] ?? ''));
                        $rawAr = $item['arabic_text'] ?? ($item['ArabicHadith'] ?? ($item['ar'] ?? ''));

                        list($narrBn, $textBn) = extractNarratorAndTextBn($rawBn);
                        if (!empty($item['narrator_bn'])) $narrBn = trim($item['narrator_bn']);

                        list($narrEn, $textEn) = extractNarratorAndTextEn($rawEn);
                        if (!empty($item['narrator_en'])) $narrEn = trim($item['narrator_en']);

                        if (empty($textEn) && !empty($textBn)) $textEn = translateTextToEn($textBn);
                        if (empty($narrEn) && !empty($narrBn)) $narrEn = translateTextToEn($narrBn);

                        $fnBn = trim($item['footnote_bn'] ?? ($item['HadithNote'] ?? ($item['note'] ?? '')));
                        if (empty($fnBn)) $fnBn = "(ইসলামিক ফাউন্ডেশন- {$hadithNum})";
                        $fnEn = trim($item['footnote_en'] ?? '');
                        if (empty($fnEn) && !empty($fnBn)) $fnEn = translateTextToEn($fnBn);

                        $extractedItems[] = [
                            'hadith_number' => $hadithNum,
                            'narrator_bn' => mb_substr($narrBn, 0, 250),
                            'narrator_en' => mb_substr($narrEn, 0, 250),
                            'arabic_text' => trim($rawAr),
                            'bangla_text' => $textBn,
                            'english_text' => $textEn,
                            'footnote_bn' => $fnBn,
                            'footnote_en' => $fnEn,
                            'grade' => trim($item['grade'] ?? 'সহীহ'),
                            'reference' => trim($item['reference'] ?? ($bookNameBn . ': ' . $hadithNum))
                        ];
                    }
                }
            }

            // Pattern B: HTML DOM Extraction from HadithBD
            if (empty($extractedItems) && (strpos($rawContent, '<') !== false && strpos($rawContent, '>') !== false)) {
                $cleanHtml = preg_replace('/<script\b[^>]*>(.*?)<\/script>/is', '', $rawContent);
                $cleanHtml = preg_replace('/<style\b[^>]*>(.*?)<\/style>/is', '', $cleanHtml);

                $dom = new DOMDocument();
                libxml_use_internal_errors(true);
                @$dom->loadHTML('<?xml encoding="UTF-8">' . $cleanHtml);
                libxml_clear_errors();

                $xpath = new DOMXPath($dom);

                // Find candidate hadith containers
                $hadithNodes = $xpath->query("//*[contains(@class, 'hadith') or contains(@class, 'box') or contains(@class, 'detail') or contains(@class, 'item') or contains(@class, 'card')]");

                $validNodes = [];
                if ($hadithNodes && $hadithNodes->length > 0) {
                    foreach ($hadithNodes as $node) {
                        $sub = $xpath->query(".//*[contains(@class, 'hadith') or contains(@class, 'box') or contains(@class, 'detail') or contains(@class, 'item') or contains(@class, 'card')]", $node);
                        if ($sub && $sub->length > 0) {
                            continue; // skip outer wrapper to prevent nested duplication
                        }
                        $text = trim($node->textContent);
                        if (mb_strlen($text) >= 40) {
                            $validNodes[] = $node;
                        }
                    }
                }

                if (empty($validNodes) && $hadithNodes && $hadithNodes->length > 0) {
                    foreach ($hadithNodes as $node) {
                        $text = trim($node->textContent);
                        if (mb_strlen($text) >= 50) {
                            $validNodes[] = $node;
                            break;
                        }
                    }
                }

                $seen = [];
                foreach ($validNodes as $hNode) {
                    $nodeText = trim($hNode->textContent);
                    $hash = md5($nodeText);
                    if (isset($seen[$hash])) continue;
                    $seen[$hash] = true;

                    // Extract ALL Arabic text without truncation
                    preg_match_all('/[\x{0600}-\x{06FF}\x{0750}-\x{077F}\x{08A0}-\x{08FF}\x{FB50}-\x{FDFF}\x{FE70}-\x{FEFF}\s\.\:\,\;\(\)\"\'«»]+(?:\n[\x{0600}-\x{06FF}\x{0750}-\x{077F}\x{08A0}-\x{08FF}\x{FB50}-\x{FDFF}\x{FE70}-\x{FEFF}\s\.\:\,\;\(\)\"\'«»]+)*/u', $nodeText, $arMatches);
                    $arabicText = '';
                    if (!empty($arMatches[0])) {
                        $arParts = array_filter(array_map('trim', $arMatches[0]), function($p) {
                            return mb_strlen($p) > 5;
                        });
                        $arabicText = implode(" ", $arParts);
                    }

                    // Extract Hadith Number
                    $hadithNum = 0;
                    if (preg_match('/(?:হাদীস|হাদিস|Hadith|No|নম্বর|নং|#)[\s\:\#\-]*([০-৯0-9]+)/iu', $nodeText, $numMatch)) {
                        $hadithNum = parseBengaliNumber($numMatch[1]);
                    } elseif (preg_match('/\[\s*([০-৯0-9]+)\s*\]/u', $nodeText, $numMatch)) {
                        $hadithNum = parseBengaliNumber($numMatch[1]);
                    }
                    if ($hadithNum === 0) {
                        $hadithNum = count($extractedItems) + 1;
                    }

                    // Extract Footnote
                    $footnoteBn = '';
                    if (preg_match('/(?:\((?:আধুনিক|ইসলামিক|মুসলিম|আহমাদ|বুখারী|তিরমিজী|ইফা)[^\)]+\))/u', $nodeText, $fnM)) {
                        $footnoteBn = trim($fnM[0]);
                    } elseif (preg_match('/(?:তাখরীজ|ফুটনোট|টীকা|তাহকীক|মান|পাবলিকেশন)[\s\:]+([^\n\r]+)/iu', $nodeText, $fnM)) {
                        $footnoteBn = trim($fnM[0]);
                    }
                    if (empty($footnoteBn)) {
                        $footnoteBn = "(ইসলামিক ফাউন্ডেশন- {$hadithNum})";
                    }

                    // Clean Bangla text
                    $cleanNodeText = $nodeText;
                    if (!empty($arabicText)) {
                        $cleanNodeText = str_replace($arabicText, '', $cleanNodeText);
                    }
                    if (!empty($footnoteBn)) {
                        $cleanNodeText = str_replace($footnoteBn, '', $cleanNodeText);
                    }

                    list($narrBn, $textBn) = extractNarratorAndTextBn($cleanNodeText);

                    if (!empty($textBn) || !empty($arabicText)) {
                        $narrEn = !empty($narrBn) ? translateTextToEn($narrBn) : '';
                        $textEn = !empty($textBn) ? translateTextToEn($textBn) : '';
                        $fnEn = !empty($footnoteBn) ? translateTextToEn($footnoteBn) : '';

                        $extractedItems[] = [
                            'hadith_number' => $hadithNum,
                            'narrator_bn' => mb_substr($narrBn, 0, 250),
                            'narrator_en' => mb_substr($narrEn, 0, 250),
                            'arabic_text' => $arabicText,
                            'bangla_text' => $textBn,
                            'english_text' => $textEn,
                            'footnote_bn' => $footnoteBn,
                            'footnote_en' => $fnEn,
                            'grade' => 'সহীহ',
                            'reference' => $bookNameBn . ': ' . $hadithNum
                        ];
                    }
                }
            }

            // Pattern C: Plain Text Parser (Splits by Hadith Number Patterns)
            if (empty($extractedItems)) {
                $blocks = preg_split('/(?=(?:হাদিস\s*(?:নম্বর|নং|no|#|\:)|হাদীস\s*(?:নম্বর|নং|no|#|\:)|Hadith\s*(?:No|#|\:)|\[\s*[০-৯0-9]+\s*\])\s*[\:\.\-]?\s*[০-৯0-9]+)/iu', $rawContent);

                if (count($blocks) <= 1) {
                    $blocks = [$rawContent];
                }

                foreach ($blocks as $blk) {
                    $blkTrim = trim($blk);
                    if (mb_strlen($blkTrim) < 25) continue;

                    // Arabic
                    preg_match_all('/[\x{0600}-\x{06FF}\x{0750}-\x{077F}\x{08A0}-\x{08FF}\x{FB50}-\x{FDFF}\x{FE70}-\x{FEFF}\s\.\:\,\;\(\)\"\'«»]+/u', $blkTrim, $arMatches);
                    $arabicText = '';
                    if (!empty($arMatches[0])) {
                        $arParts = array_filter(array_map('trim', $arMatches[0]), function($p) { return mb_strlen($p) > 5; });
                        $arabicText = implode(" ", $arParts);
                    }

                    // Hadith Number
                    $hadithNum = 0;
                    if (preg_match('/(?:হাদীস|হাদিস|Hadith|No|নম্বর|নং|#)[\s\:\#\-]*([০-৯0-9]+)/iu', $blkTrim, $numMatch)) {
                        $hadithNum = parseBengaliNumber($numMatch[1]);
                    } elseif (preg_match('/\[\s*([০-৯0-9]+)\s*\]/u', $blkTrim, $numMatch)) {
                        $hadithNum = parseBengaliNumber($numMatch[1]);
                    } elseif (preg_match('/^[০-৯0-9]+/u', $blkTrim, $numMatch)) {
                        $hadithNum = parseBengaliNumber($numMatch[0]);
                    }
                    if ($hadithNum === 0) {
                        $hadithNum = count($extractedItems) + 1;
                    }

                    // Footnote
                    $footnoteBn = '';
                    if (preg_match('/(?:\((?:আধুনিক|ইসলামিক|মুসলিম|আহমাদ|বুখারী|তিরমিজী|ইফা)[^\)]+\))/u', $blkTrim, $fnM)) {
                        $footnoteBn = trim($fnM[0]);
                    } elseif (preg_match('/(?:তাখরীজ|ফুটনোট|টীকা|তাহকীক|মান|পাবলিকেশন)[\s\:]+([^\n\r]+)/iu', $blkTrim, $fnM)) {
                        $footnoteBn = trim($fnM[0]);
                    }
                    if (empty($footnoteBn)) {
                        $footnoteBn = "(ইসলামিক ফাউন্ডেশন- {$hadithNum})";
                    }

                    // Clean Bangla
                    $cleanBlk = $blkTrim;
                    if (!empty($arabicText)) $cleanBlk = str_replace($arabicText, '', $cleanBlk);
                    if (!empty($footnoteBn)) $cleanBlk = str_replace($footnoteBn, '', $cleanBlk);

                    list($narrBn, $textBn) = extractNarratorAndTextBn($cleanBlk);

                    if (!empty($textBn) || !empty($arabicText)) {
                        $narrEn = !empty($narrBn) ? translateTextToEn($narrBn) : '';
                        $textEn = !empty($textBn) ? translateTextToEn($textBn) : '';
                        $fnEn = !empty($footnoteBn) ? translateTextToEn($footnoteBn) : '';

                        $extractedItems[] = [
                            'hadith_number' => $hadithNum,
                            'narrator_bn' => mb_substr($narrBn, 0, 250),
                            'narrator_en' => mb_substr($narrEn, 0, 250),
                            'arabic_text' => $arabicText,
                            'bangla_text' => $textBn,
                            'english_text' => $textEn,
                            'footnote_bn' => $footnoteBn,
                            'footnote_en' => $fnEn,
                            'grade' => 'সহীহ',
                            'reference' => $bookNameBn . ': ' . $hadithNum
                        ];
                    }
                }
            }

            if (empty($extractedItems)) {
                echo json_encode(['success' => false, 'error' => 'প্রদত্ত কন্টেন্ট থেকে কোনো বৈধ হাদিস বা টেক্সট এক্সট্র্যাক্ট করা সম্ভব হয়নি।']);
                exit;
            }

            // Insert into staging table
            $insStmt = $pdo->prepare("INSERT INTO hadith_scraped_staging 
                (source_url, book_slug, chapter_number, chapter_title_bn, chapter_title_en, hadith_number, hadith_number_bn, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, grade, reference, footnote_bn, footnote_en, status) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending')");

            $savedCount = 0;
            foreach ($extractedItems as $item) {
                $insStmt->execute([
                    $sourceUrl,
                    $bookSlug,
                    $chapterNumber,
                    $chapterTitleBn,
                    $chapterTitleEn,
                    $item['hadith_number'],
                    toBengaliNumber($item['hadith_number']),
                    $item['narrator_bn'],
                    $item['narrator_en'],
                    $item['arabic_text'],
                    $item['bangla_text'],
                    $item['english_text'],
                    $item['grade'],
                    $item['reference'],
                    $item['footnote_bn'],
                    $item['footnote_en']
                ]);
                $savedCount++;
            }

            echo json_encode([
                'success' => true,
                'message' => "সফলভাবে হাদিসবিডি (hadithbd.com/hadith/) থেকে {$savedCount}টি হাদিস স্ক্র্যাপ করে ‘পেন্ডিং কিউ’-তে যুক্ত করা হয়েছে।",
                'count' => $savedCount,
                'items' => $extractedItems
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 2. 1-CLICK CHAPTER SCRAPER DIRECTLY FROM HADITHBD (hadithbd.com/hadith/)
        // ---------------------------------------------------------------------
        case 'crawl_online_chapter':
            $bookSlug = trim($_POST['book_slug'] ?? 'bukhari');
            $chapterNumber = (int)($_POST['chapter_number'] ?? 1);
            $sourceUrl = 'https://www.hadithbd.com/hadith/';

            // Fetch chapter info from MySQL
            $cStmt = $pdo->prepare("SELECT title_bn, title_en, start_hadith, end_hadith FROM hadith_chapters WHERE book_slug = ? AND chapter_number = ? LIMIT 1");
            $cStmt->execute([$bookSlug, $chapterNumber]);
            $cRow = $cStmt->fetch();

            $chapterTitleBn = $cRow['title_bn'] ?? ('অধ্যায় ' . toBengaliNumber($chapterNumber));
            $chapterTitleEn = $cRow['title_en'] ?? ('Chapter ' . $chapterNumber);

            $bookMap = [
                'bukhari' => 1,
                'muslim' => 2,
                'abu_dawood' => 4,
                'nasai' => 6,
                'ibn_majah' => 9,
                'tirmidhi' => 11,
                'riyad_us_saliheen' => 3,
                'hadith_qudsi' => 13,
                'forty_hadith' => 14,
                'ramadan_hadith' => 15
            ];

            $bookNamesBn = [
                'bukhari' => 'সহীহ বুখারী',
                'muslim' => 'সহীহ মুসলিম',
                'abu_dawood' => 'সুনানে আবু দাউদ',
                'nasai' => 'সুনান আন-নাসায়ী',
                'ibn_majah' => 'সুনানে ইবনে মাজাহ',
                'tirmidhi' => 'জামে আত-তিরমিযী'
            ];

            $bookId = $bookMap[$bookSlug] ?? 1;
            $bookNameBn = $bookNamesBn[$bookSlug] ?? 'সহীহ হাদিস';

            $hbd = getHadithBdConnection();
            $sec = findHadithBdSection($hbd, $bookId, $chapterNumber);

            if (!$sec) {
                echo json_encode([
                    'success' => false,
                    'error' => "হাদিসবিডি-তে '{$bookNameBn}'-এর অধ্যায় #{$chapterNumber} পাওয়া যায়নি।"
                ], JSON_UNESCAPED_UNICODE);
                exit;
            }

            if (!empty($sec['SectionBD'])) {
                $secCleanTitle = preg_replace('/^[০-৯0-9]+\s*[\/|\.\:\-]\s*/u', '', $sec['SectionBD']);
                if (!empty($secCleanTitle)) {
                    $chapterTitleBn = trim($secCleanTitle);
                }
            }

            $secId = $sec['SectionID'];
            $hStmt = $hbd->prepare("SELECT HadithID, HadithNo, ArabicHadith, BanglaHadith, EnglishHadith, HadithNote, HadithStatus FROM hadithmain WHERE BookID = ? AND SectionID = ? ORDER BY HadithID ASC");
            $hStmt->execute([$bookId, $secId]);
            $rawHadiths = $hStmt->fetchAll(PDO::FETCH_ASSOC);

            if (empty($rawHadiths)) {
                echo json_encode([
                    'success' => false,
                    'error' => "হাদিসবিডি-তে অধ্যায় #{$chapterNumber}-এর কোনো হাদিস পাওয়া যায়নি।"
                ], JSON_UNESCAPED_UNICODE);
                exit;
            }

            $hadithList = [];
            foreach ($rawHadiths as $h) {
                list($narrBn, $textBn) = extractNarratorAndTextBn($h['BanglaHadith']);
                list($narrEn, $textEn) = extractNarratorAndTextEn($h['EnglishHadith']);

                $hNum = (int)$h['HadithNo'];
                if ($hNum <= 0) {
                    $hNum = count($hadithList) + 1;
                }

                if (empty($textEn) && !empty($textBn)) {
                    $textEn = $textBn;
                }
                if (empty($narrEn) && !empty($narrBn)) {
                    $narrEn = $narrBn;
                }

                $arabicText = trim($h['ArabicHadith'] ?? '');
                $footnoteBn = trim($h['HadithNote'] ?? '');
                if (empty($footnoteBn)) {
                    $footnoteBn = "(ইসলামিক ফাউন্ডেশন- {$hNum})";
                    $footnoteEn = "(Islamic Foundation- {$hNum})";
                } else {
                    $footnoteEn = "(HadithBD Note- {$hNum})";
                }

                $grade = 'সহীহ';
                if (!empty($h['HadithStatus'])) {
                    $st = (int)$h['HadithStatus'];
                    if ($st === 2) $grade = 'হাসান';
                    elseif ($st === 3) $grade = 'যঈফ';
                }

                $hadithList[] = [
                    'hadith_number' => $hNum,
                    'narrator_bn' => mb_substr($narrBn, 0, 250),
                    'narrator_en' => mb_substr($narrEn, 0, 250),
                    'arabic_text' => $arabicText,
                    'bangla_text' => $textBn,
                    'english_text' => $textEn,
                    'footnote_bn' => $footnoteBn,
                    'footnote_en' => $footnoteEn,
                    'grade' => $grade,
                    'reference' => $bookNameBn . ': ' . $hNum
                ];
            }

            // Remove any existing stale pending items for this chapter to prevent duplicates
            $pdo->prepare("DELETE FROM hadith_scraped_staging WHERE book_slug = ? AND chapter_number = ? AND status = 'pending'")
                ->execute([$bookSlug, $chapterNumber]);

            // Insert into staging table
            $insStmt = $pdo->prepare("INSERT INTO hadith_scraped_staging 
                (source_url, book_slug, chapter_number, chapter_title_bn, chapter_title_en, hadith_number, hadith_number_bn, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, grade, reference, footnote_bn, footnote_en, status) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending')");

            $saved = 0;
            foreach ($hadithList as $item) {
                $insStmt->execute([
                    $sourceUrl,
                    $bookSlug,
                    $chapterNumber,
                    $chapterTitleBn,
                    $chapterTitleEn,
                    $item['hadith_number'],
                    toBengaliNumber($item['hadith_number']),
                    $item['narrator_bn'],
                    $item['narrator_en'],
                    $item['arabic_text'],
                    $item['bangla_text'],
                    $item['english_text'],
                    $item['grade'],
                    $item['reference'],
                    $item['footnote_bn'],
                    $item['footnote_en']
                ]);
                $saved++;
            }

            echo json_encode([
                'success' => true,
                'message' => "হাদিসবিডি (hadithbd.com/hadith/) থেকে অধ্যায় #{$chapterNumber}-এর সর্বমোট {$saved}টি প্রামাণ্য হাদিস সফলভাবে স্ক্র্যাপ করে ‘পেন্ডিং কিউ’-তে যুক্ত করা হয়েছে।",
                'count' => $saved
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 3. APPROVE SINGLE HADITH (Move from Staging to Live hadith_items)
        // ---------------------------------------------------------------------
        case 'approve_single':
            $stagingId = (int)($_POST['staging_id'] ?? 0);
            if ($stagingId <= 0) {
                echo json_encode(['success' => false, 'error' => 'অবৈধ আইডি।']);
                exit;
            }

            $sStmt = $pdo->prepare("SELECT * FROM hadith_scraped_staging WHERE id = ?");
            $sStmt->execute([$stagingId]);
            $staged = $sStmt->fetch();

            if (!$staged) {
                echo json_encode(['success' => false, 'error' => 'স্ক্র্যাপড হাদিসটি পাওয়া যায়নি।']);
                exit;
            }

            $pdo->beginTransaction();

            // Insert or update into hadith_items
            $insLive = $pdo->prepare("INSERT INTO hadith_items 
                (book_slug, chapter_number, hadith_number, hadith_number_bn, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, grade, reference, footnote_bn, footnote_en, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
                ON DUPLICATE KEY UPDATE
                narrator_bn = VALUES(narrator_bn),
                narrator_en = VALUES(narrator_en),
                arabic_text = VALUES(arabic_text),
                bangla_text = VALUES(bangla_text),
                english_text = VALUES(english_text),
                grade = VALUES(grade),
                reference = VALUES(reference),
                footnote_bn = VALUES(footnote_bn),
                footnote_en = VALUES(footnote_en),
                is_active = 1");

            $insLive->execute([
                $staged['book_slug'],
                $staged['chapter_number'],
                $staged['hadith_number'],
                $staged['hadith_number_bn'] ?: toBengaliNumber($staged['hadith_number']),
                $staged['narrator_bn'],
                $staged['narrator_en'],
                $staged['arabic_text'],
                $staged['bangla_text'],
                $staged['english_text'],
                $staged['grade'],
                $staged['reference'],
                $staged['footnote_bn'],
                $staged['footnote_en']
            ]);

            // Ensure chapter exists in hadith_chapters
            $cCheck = $pdo->prepare("SELECT id, start_hadith, end_hadith, total_hadith FROM hadith_chapters WHERE book_slug = ? AND chapter_number = ?");
            $cCheck->execute([$staged['book_slug'], $staged['chapter_number']]);
            $chap = $cCheck->fetch();

            $hNum = (int)$staged['hadith_number'];
            if ($chap) {
                $newStart = ($chap['start_hadith'] > 0) ? min($chap['start_hadith'], $hNum) : $hNum;
                $newEnd = max($chap['end_hadith'], $hNum);
                $newTotal = ($newEnd - $newStart + 1);
                $rangeBn = toBengaliNumber($newStart) . ' - ' . toBengaliNumber($newEnd);
                $rangeEn = $newStart . ' - ' . $newEnd;

                $updChap = $pdo->prepare("UPDATE hadith_chapters SET start_hadith = ?, end_hadith = ?, total_hadith = ?, hadith_range = ?, hadith_range_bn = ? WHERE id = ?");
                $updChap->execute([$newStart, $newEnd, $newTotal, $rangeEn, $rangeBn, $chap['id']]);
            } else {
                $chapTitleBn = $staged['chapter_title_bn'] ?: ('অধ্যায় ' . toBengaliNumber($staged['chapter_number']));
                $chapTitleEn = $staged['chapter_title_en'] ?: ('Chapter ' . $staged['chapter_number']);
                $rangeBn = toBengaliNumber($hNum) . ' - ' . toBengaliNumber($hNum);
                $rangeEn = $hNum . ' - ' . $hNum;

                $insChap = $pdo->prepare("INSERT INTO hadith_chapters (book_slug, chapter_number, chapter_number_bn, title_bn, title_en, hadith_range, hadith_range_bn, start_hadith, end_hadith, total_hadith, display_order, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?, 1)");
                $insChap->execute([
                    $staged['book_slug'],
                    $staged['chapter_number'],
                    toBengaliNumber($staged['chapter_number']),
                    $chapTitleBn,
                    $chapTitleEn,
                    $rangeEn,
                    $rangeBn,
                    $hNum,
                    $hNum,
                    $staged['chapter_number']
                ]);
            }

            // Mark staging item as approved
            $updStaging = $pdo->prepare("UPDATE hadith_scraped_staging SET status = 'approved' WHERE id = ?");
            $updStaging->execute([$stagingId]);

            // Recalculate book's real live hadith count
            $updBook = $pdo->prepare("UPDATE hadith_books SET total_hadith = (SELECT COUNT(*) FROM hadith_items WHERE book_slug = ?) WHERE book_slug = ?");
            $updBook->execute([$staged['book_slug'], $staged['book_slug']]);

            $pdo->commit();

            logAdminAction($pdo, 'APPROVE_HADITH', 'hadith_items', (string)$staged['hadith_number'], "হাদিস অনুমোদন: {$staged['book_slug']} #{$staged['hadith_number']}");

            echo json_encode([
                'success' => true,
                'message' => "হাদিস #{$staged['hadith_number']} সফলভাবে অনুমোদিত হয়ে মূল লাইভ ডাটাবেজে যুক্ত হয়েছে।",
                'book_slug' => $staged['book_slug'],
                'chapter_number' => $staged['chapter_number'],
                'hadith_number' => $staged['hadith_number'],
                'live_url' => "hadiths.php?book=" . urlencode($staged['book_slug']) . "&chapter=" . $staged['chapter_number'] . "&search=" . urlencode($staged['hadith_number'])
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 4. BULK APPROVE CHAPTER (Move all pending items in a chapter to Live)
        // ---------------------------------------------------------------------
        case 'bulk_approve_chapter':
            $bookSlug = trim($_POST['book_slug'] ?? '');
            $chapterNumber = (int)($_POST['chapter_number'] ?? 0);

            if (empty($bookSlug) || $chapterNumber <= 0) {
                echo json_encode(['success' => false, 'error' => 'গ্রন্থ ও অধ্যায় সঠিকভাবে নির্বাচন করুন।']);
                exit;
            }

            $stagedStmt = $pdo->prepare("SELECT * FROM hadith_scraped_staging WHERE book_slug = ? AND chapter_number = ? AND status = 'pending' ORDER BY hadith_number ASC");
            $stagedStmt->execute([$bookSlug, $chapterNumber]);
            $pendingList = $stagedStmt->fetchAll();

            if (empty($pendingList)) {
                echo json_encode(['success' => false, 'error' => 'এই অধ্যায়ে অনুমোদনের মতো কোনো পেন্ডিং হাদিস নেই।']);
                exit;
            }

            $pdo->beginTransaction();

            $insLive = $pdo->prepare("INSERT INTO hadith_items 
                (book_slug, chapter_number, hadith_number, hadith_number_bn, narrator_bn, narrator_en, arabic_text, bangla_text, english_text, grade, reference, footnote_bn, footnote_en, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
                ON DUPLICATE KEY UPDATE
                narrator_bn = VALUES(narrator_bn),
                narrator_en = VALUES(narrator_en),
                arabic_text = VALUES(arabic_text),
                bangla_text = VALUES(bangla_text),
                english_text = VALUES(english_text),
                grade = VALUES(grade),
                reference = VALUES(reference),
                footnote_bn = VALUES(footnote_bn),
                footnote_en = VALUES(footnote_en),
                is_active = 1");

            $minNum = 999999;
            $maxNum = 0;

            foreach ($pendingList as $staged) {
                $hNum = (int)$staged['hadith_number'];
                if ($hNum < $minNum) $minNum = $hNum;
                if ($hNum > $maxNum) $maxNum = $hNum;

                $insLive->execute([
                    $staged['book_slug'],
                    $staged['chapter_number'],
                    $staged['hadith_number'],
                    $staged['hadith_number_bn'] ?: toBengaliNumber($staged['hadith_number']),
                    $staged['narrator_bn'],
                    $staged['narrator_en'],
                    $staged['arabic_text'],
                    $staged['bangla_text'],
                    $staged['english_text'],
                    $staged['grade'],
                    $staged['reference'],
                    $staged['footnote_bn'],
                    $staged['footnote_en']
                ]);
            }

            // Update Chapter range and totals
            $cCheck = $pdo->prepare("SELECT id, start_hadith, end_hadith FROM hadith_chapters WHERE book_slug = ? AND chapter_number = ?");
            $cCheck->execute([$bookSlug, $chapterNumber]);
            $chap = $cCheck->fetch();

            if ($chap) {
                $finalStart = ($chap['start_hadith'] > 0) ? min($chap['start_hadith'], $minNum) : $minNum;
                $finalEnd = max($chap['end_hadith'], $maxNum);
                $finalTotal = ($finalEnd - $finalStart + 1);
                $rangeBn = toBengaliNumber($finalStart) . ' - ' . toBengaliNumber($finalEnd);
                $rangeEn = $finalStart . ' - ' . $finalEnd;

                $updChap = $pdo->prepare("UPDATE hadith_chapters SET start_hadith = ?, end_hadith = ?, total_hadith = ?, hadith_range = ?, hadith_range_bn = ? WHERE id = ?");
                $updChap->execute([$finalStart, $finalEnd, $finalTotal, $rangeEn, $rangeBn, $chap['id']]);
            } else {
                $firstStaged = $pendingList[0];
                $chapTitleBn = $firstStaged['chapter_title_bn'] ?: ('অধ্যায় ' . toBengaliNumber($chapterNumber));
                $chapTitleEn = $firstStaged['chapter_title_en'] ?: ('Chapter ' . $chapterNumber);
                $rangeBn = toBengaliNumber($minNum) . ' - ' . toBengaliNumber($maxNum);
                $rangeEn = $minNum . ' - ' . $maxNum;
                $totalH = ($maxNum - $minNum + 1);

                $insChap = $pdo->prepare("INSERT INTO hadith_chapters (book_slug, chapter_number, chapter_number_bn, title_bn, title_en, hadith_range, hadith_range_bn, start_hadith, end_hadith, total_hadith, display_order, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
                $insChap->execute([
                    $bookSlug,
                    $chapterNumber,
                    toBengaliNumber($chapterNumber),
                    $chapTitleBn,
                    $chapTitleEn,
                    $rangeEn,
                    $rangeBn,
                    $minNum,
                    $maxNum,
                    $totalH,
                    $chapterNumber
                ]);
            }

            // Mark all staged as approved
            $updStaging = $pdo->prepare("UPDATE hadith_scraped_staging SET status = 'approved' WHERE book_slug = ? AND chapter_number = ? AND status = 'pending'");
            $updStaging->execute([$bookSlug, $chapterNumber]);

            // Recalculate book's real live hadith count
            $updBook = $pdo->prepare("UPDATE hadith_books SET total_hadith = (SELECT COUNT(*) FROM hadith_items WHERE book_slug = ?) WHERE book_slug = ?");
            $updBook->execute([$bookSlug, $bookSlug]);

            $pdo->commit();

            $approvedCount = count($pendingList);
            logAdminAction($pdo, 'BULK_APPROVE_HADITHS', 'hadith_items', "{$bookSlug}_{$chapterNumber}", "অধ্যায় #{$chapterNumber}-এর {$approvedCount}টি হাদিস এক ক্লিকে অনুমোদন সম্পন্ন");

            echo json_encode([
                'success' => true,
                'message' => "অভিনন্দন! অধ্যায় #{$chapterNumber}-এর সর্বমোট {$approvedCount}টি হাদিস সফলভাবে অনুমোদিত হয়ে দীন ওয়ান লাইভ ডাটাবেজে যুক্ত হয়েছে।",
                'count' => $approvedCount,
                'book_slug' => $bookSlug,
                'chapter_number' => $chapterNumber,
                'live_url' => "hadiths.php?book=" . urlencode($bookSlug) . "&chapter=" . $chapterNumber
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 5. UPDATE STAGED HADITH (Edit before approval)
        // ---------------------------------------------------------------------
        case 'update_staged':
            $stagingId = (int)($_POST['id'] ?? 0);
            if ($stagingId <= 0) {
                echo json_encode(['success' => false, 'error' => 'অবৈধ আইডি।']);
                exit;
            }

            $narratorBn = trim($_POST['narrator_bn'] ?? '');
            $narratorEn = trim($_POST['narrator_en'] ?? '');
            $arabicText = trim($_POST['arabic_text'] ?? '');
            $banglaText = trim($_POST['bangla_text'] ?? '');
            $englishText = trim($_POST['english_text'] ?? '');
            $footnoteBn = trim($_POST['footnote_bn'] ?? '');
            $footnoteEn = trim($_POST['footnote_en'] ?? '');
            $reference = trim($_POST['reference'] ?? '');
            $grade = trim($_POST['grade'] ?? 'সহীহ');

            $upd = $pdo->prepare("UPDATE hadith_scraped_staging SET 
                narrator_bn = ?, narrator_en = ?, arabic_text = ?, bangla_text = ?, english_text = ?, 
                footnote_bn = ?, footnote_en = ?, reference = ?, grade = ? WHERE id = ?");
            $upd->execute([$narratorBn, $narratorEn, $arabicText, $banglaText, $englishText, $footnoteBn, $footnoteEn, $reference, $grade, $stagingId]);

            echo json_encode([
                'success' => true,
                'message' => 'স্ক্র্যাপড হাদিসের তথ্য সফলভাবে আপডেট হয়েছে।'
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 6. DELETE / REJECT STAGED HADITH
        // ---------------------------------------------------------------------
        case 'delete_staged':
            $stagingId = (int)($_POST['staging_id'] ?? 0);
            if ($stagingId <= 0) {
                echo json_encode(['success' => false, 'error' => 'অবৈধ আইডি।']);
                exit;
            }

            $del = $pdo->prepare("DELETE FROM hadith_scraped_staging WHERE id = ?");
            $del->execute([$stagingId]);

            echo json_encode([
                'success' => true,
                'message' => 'স্ক্র্যাপড হাদিসটি পেন্ডিং কিউ থেকে মুছে ফেলা হয়েছে।'
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 7. BULK DELETE / CLEAR PENDING FOR A CHAPTER
        // ---------------------------------------------------------------------
        case 'bulk_delete_chapter':
            $bookSlug = trim($_POST['book_slug'] ?? '');
            $chapterNumber = (int)($_POST['chapter_number'] ?? 0);
            if (empty($bookSlug) || $chapterNumber <= 0) {
                echo json_encode(['success' => false, 'error' => 'গ্রন্থ ও অধ্যায় সঠিকভাবে নির্বাচন করুন।']);
                exit;
            }

            $delStmt = $pdo->prepare("DELETE FROM hadith_scraped_staging WHERE book_slug = ? AND chapter_number = ? AND status = 'pending'");
            $delStmt->execute([$bookSlug, $chapterNumber]);
            $deletedCount = $delStmt->rowCount();

            echo json_encode([
                'success' => true,
                'message' => "অধ্যায় #{$chapterNumber}-এর মোট {$deletedCount}টি পেন্ডিং হাদিস মুছে ফেলা হয়েছে।",
                'count' => $deletedCount
            ], JSON_UNESCAPED_UNICODE);
            break;

        default:
            echo json_encode(['success' => false, 'error' => 'অজ্ঞাত অ্যাকশন।']);
            break;
    }
} catch (Exception $e) {
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }
    echo json_encode([
        'success' => false,
        'error' => 'ডাটাবেজ এরর: ' . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}
