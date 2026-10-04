<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET QUIZ & BATTLE QUESTIONS
 * Dynamic Category-wise Quiz Questions with Anti-Repetition Filtering
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$category = trim($_GET['category_id'] ?? '');
$count = max(1, min(50, (int)($_GET['count'] ?? 5)));
$difficulty = trim($_GET['difficulty'] ?? '');
$excludeIds = trim($_GET['exclude_ids'] ?? '');

$where = [];
$params = [];

if (!empty($category) && $category !== 'default' && $category !== 'all') {
    // Comprehensive category alias mapping
    if ($category === 'iman' || $category === 'aqeedah_tawheed') {
        $where[] = "(category_id = 'iman' OR category_id = 'aqeedah_tawheed')";
    } else if ($category === 'seerat_un_nabi' || $category === 'seerah_nabawi' || $category === 'seerat') {
        $where[] = "(category_id = 'seerat_un_nabi' OR category_id = 'seerah_nabawi' OR category_id = 'seerat')";
    } else if ($category === 'hadith_sunnah' || $category === 'hadith_sciences' || $category === 'hadith') {
        $where[] = "(category_id = 'hadith_sunnah' OR category_id = 'hadith_sciences' OR category_id = 'hadith')";
    } else if ($category === 'fiqh_rulings' || $category === 'shariah_life' || $category === 'fiqh') {
        $where[] = "(category_id = 'fiqh_rulings' OR category_id = 'shariah_life' OR category_id = 'fiqh')";
    } else if ($category === 'ramadan_sawm' || $category === 'ramadan' || $category === 'sawm') {
        $where[] = "(category_id = 'ramadan_sawm' OR category_id = 'ramadan' OR category_id = 'sawm')";
    } else if ($category === 'hajj_umrah' || $category === 'hajj') {
        $where[] = "(category_id = 'hajj_umrah' OR category_id = 'hajj')";
    } else if ($category === 'zakat_charity' || $category === 'zakat') {
        $where[] = "(category_id = 'zakat_charity' OR category_id = 'zakat')";
    } else if ($category === 'quran_studies' || $category === 'quran') {
        $where[] = "(category_id = 'quran_studies' OR category_id = 'quran')";
    } else if ($category === 'ibadah' || $category === 'ibadat') {
        $where[] = "(category_id = 'ibadah' OR category_id = 'ibadat')";
    } else if ($category === 'rabiul_awwal' || $category === 'rabi_al_awwal') {
        $where[] = "(category_id = 'rabiul_awwal' OR category_id = 'rabi_al_awwal')";
    } else if ($category === 'dua_azkar' || $category === 'dua' || $category === 'dhikr') {
        $where[] = "(category_id = 'dua_azkar' OR category_id = 'dua' OR category_id = 'dhikr')";
    } else if ($category === 'islamic_akhlaq' || $category === 'akhlaq') {
        $where[] = "(category_id = 'islamic_akhlaq' OR category_id = 'akhlaq')";
    } else {
        $where[] = "category_id = ?";
        $params[] = $category;
    }
}

if (!empty($difficulty)) {
    $where[] = "difficulty = ?";
    $params[] = $difficulty;
}

if (!empty($excludeIds)) {
    $rawExList = explode(',', $excludeIds);
    $cleanExList = [];
    foreach ($rawExList as $item) {
        $item = trim($item);
        if (!empty($item)) {
            $cleanExList[] = $item;
        }
    }
    if (!empty($cleanExList)) {
        $placeholders = implode(',', array_fill(0, count($cleanExList), '?'));
        $where[] = "question_uid NOT IN ($placeholders)";
        foreach ($cleanExList as $item) {
            $params[] = $item;
        }
    }
}

$where[] = "is_active = 1";
$whereSql = "WHERE " . implode(" AND ", $where);

try {
    $stmt = $pdo->prepare("SELECT question_uid, category_id, question_bn, question_en, option_1, option_1_en, option_2, option_2_en, option_3, option_3_en, option_4, option_4_en, correct_option, explanation, explanation_en, reference, reference_en, difficulty 
                           FROM quiz_questions 
                           $whereSql 
                           ORDER BY RAND() 
                           LIMIT $count");
    $stmt->execute($params);
    $rows = $stmt->fetchAll();

    // Fallback if all questions were excluded
    if (empty($rows) && !empty($excludeIds)) {
        $fallbackWhere = [];
        $fallbackParams = [];
        if (!empty($category) && $category !== 'default' && $category !== 'all') {
            $fallbackWhere[] = $where[0];
            if (!empty($params) && count($params) > 0 && !empty($cleanExList)) {
                // Keep category params
                $fallbackParams = array_slice($params, 0, count($params) - count($cleanExList));
            }
        }
        $fallbackWhere[] = "is_active = 1";
        $fallbackWhereSql = "WHERE " . implode(" AND ", $fallbackWhere);

        $fallbackStmt = $pdo->prepare("SELECT question_uid, category_id, question_bn, question_en, option_1, option_1_en, option_2, option_2_en, option_3, option_3_en, option_4, option_4_en, correct_option, explanation, explanation_en, reference, reference_en, difficulty 
                                       FROM quiz_questions 
                                       $fallbackWhereSql 
                                       ORDER BY RAND() 
                                       LIMIT $count");
        $fallbackStmt->execute($fallbackParams);
        $rows = $fallbackStmt->fetchAll();
    }

    $questions = [];
    foreach ($rows as $r) {
        $questions[] = [
            'id' => $r['question_uid'],
            'category_id' => $r['category_id'],
            'question_text' => $r['question_bn'],
            'question_text_en' => $r['question_en'] ?? $r['question_bn'],
            'options' => [
                $r['option_1'],
                $r['option_2'],
                $r['option_3'],
                $r['option_4']
            ],
            'options_en' => [
                $r['option_1_en'] ?? $r['option_1'],
                $r['option_2_en'] ?? $r['option_2'],
                $r['option_3_en'] ?? $r['option_3'],
                $r['option_4_en'] ?? $r['option_4']
            ],
            'correct_option_index' => (int)$r['correct_option'],
            'explanation' => $r['explanation'] ?? '',
            'explanation_en' => $r['explanation_en'] ?? '',
            'reference' => $r['reference'] ?? '',
            'reference_en' => $r['reference_en'] ?? '',
            'difficulty' => $r['difficulty'] ?? 'MEDIUM'
        ];
    }

    sendJsonResponse([
        'success' => true,
        'category' => $category,
        'count' => count($questions),
        'questions' => $questions
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to fetch questions: ' . $e->getMessage()
    ], 500);
}
?>

