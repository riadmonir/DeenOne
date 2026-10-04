<?php
/**
 * DeenOne Real-Time Auto-Translate Backend API Proxy
 * 100% Free, Zero API Key / Zero Cost Neural Machine Translation
 */

header('Content-Type: application/json; charset=utf-8');
header('Cache-Control: public, max-age=86400'); // Cache translations for 24 hours

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['success' => false, 'error' => 'Method not allowed']);
    exit;
}

$rawInput = file_get_contents('php://input');
$input = json_decode($rawInput, true);

if (!$input && !empty($_POST)) {
    $input = $_POST;
}

$text = trim($input['text'] ?? '');
$sl = trim($input['sl'] ?? 'bn'); // Source language: Bengali
$tl = trim($input['tl'] ?? 'en'); // Target language: English

if (empty($text)) {
    echo json_encode(['success' => true, 'translated' => '']);
    exit;
}

/**
 * Strategy 1: Google Dict Chrome Client API (Ultra-fast, zero blocks)
 */
function translateViaGoogleDict($text, $sl, $tl) {
    $url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=' . urlencode($sl) . '&tl=' . urlencode($tl) . '&q=' . urlencode($text);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 6);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36');
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode === 200 && !empty($response)) {
        $json = json_decode($response, true);
        if (is_array($json) && !empty($json[0])) {
            return is_array($json[0]) ? implode(' ', $json[0]) : (string)$json[0];
        }
    }
    return null;
}

/**
 * Strategy 2: MyMemory Free Translation Engine (Solid Fallback)
 */
function translateViaMyMemory($text, $sl, $tl) {
    $url = 'https://api.mymemory.translated.net/get?q=' . urlencode($text) . '&langpair=' . urlencode($sl) . '|' . urlencode($tl);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 6);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0');
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode === 200 && !empty($response)) {
        $json = json_decode($response, true);
        if (!empty($json['responseData']['translatedText'])) {
            return html_entity_decode($json['responseData']['translatedText'], ENT_QUOTES, 'UTF-8');
        }
    }
    return null;
}

// Execute with multi-tier failover
$translated = translateViaGoogleDict($text, $sl, $tl);
if (empty($translated)) {
    $translated = translateViaMyMemory($text, $sl, $tl);
}

if ($translated !== null && trim($translated) !== '') {
    echo json_encode([
        'success' => true,
        'original' => $text,
        'translated' => trim($translated)
    ], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode([
        'success' => false,
        'error' => 'Translation service temporarily unreachable',
        'translated' => ''
    ]);
}
