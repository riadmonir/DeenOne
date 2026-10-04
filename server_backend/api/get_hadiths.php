<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET SAHIH HADITHS
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$book = trim($_GET['book'] ?? '');
$hadithNo = (int)($_GET['number'] ?? 0);
$random = isset($_GET['random']) ? (int)$_GET['random'] : 0;
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = max(1, min(50, (int)($_GET['limit'] ?? 20)));
$offset = ($page - 1) * $limit;

try {
    if ($random === 1) {
        $stmt = $pdo->query("SELECT h.*, b.name_bn as book_name FROM hadith_items h LEFT JOIN hadith_books b ON h.book_slug = b.book_slug ORDER BY RAND() LIMIT 1");
        $hadith = $stmt->fetch();
        sendJsonResponse([
            'success' => true,
            'hadith' => $hadith
        ]);
    }

    $where = [];
    $params = [];
    if (!empty($book)) {
        $where[] = "h.book_slug = ?";
        $params[] = $book;
    }
    if ($hadithNo > 0) {
        $where[] = "h.hadith_number = ?";
        $params[] = $hadithNo;
    }

    $whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

    $sql = "SELECT h.*, b.name_bn as book_name 
            FROM hadith_items h 
            LEFT JOIN hadith_books b ON h.book_slug = b.book_slug 
            $whereSql 
            ORDER BY h.hadith_number ASC 
            LIMIT $limit OFFSET $offset";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $items = $stmt->fetchAll(PDO::FETCH_ASSOC);
    if (empty($items)) {
        require_once __DIR__ . '/hadithbd_helper.php';
        $hDb = getHadithDbPdo();
        $bId = getHadithDbBookId($book);
        if ($hDb && $bId !== null) {
            $hWhere = ["m.BookID = ?"];
            $hParams = [$bId];
            if ($hadithNo > 0) {
                $hWhere[] = "m.HadithNo = ?";
                $hParams[] = $hadithNo;
            }
            $hWhereSql = "WHERE " . implode(" AND ", $hWhere);
            $hSql = "SELECT m.HadithID, m.HadithNo, m.ArabicHadith, m.BanglaHadith, m.EnglishHadith, m.HadithNote, m.HadithStatus
                     FROM hadithmain m
                     $hWhereSql
                     ORDER BY m.HadithNo ASC
                     LIMIT $limit OFFSET $offset";
            $hStmt = $hDb->prepare($hSql);
            $hStmt->execute($hParams);
            $hRows = $hStmt->fetchAll();

            $bInfoStmt = $pdo->prepare("SELECT name_bn, name_en FROM hadith_books WHERE book_slug = ? LIMIT 1");
            $bInfoStmt->execute([$book]);
            $bInfo = $bInfoStmt->fetch();
            $bNameBn = $bInfo['name_bn'] ?? 'সহীহ হাদিস';

            foreach ($hRows as $hr) {
                list($narrator, $body) = extractNarratorAndText($hr['BanglaHadith'] ?? '');
                $grade = getHadithGradeFromStatus($hr['HadithStatus'] ?? 1);
                $items[] = [
                    'id' => (int)$hr['HadithID'],
                    'book_slug' => $book,
                    'book_name' => $bNameBn,
                    'hadith_number' => (int)$hr['HadithNo'],
                    'arabic_text' => trim((string)($hr['ArabicHadith'] ?? '')),
                    'bangla_text' => $body,
                    'english_text' => cleanHadithText($hr['EnglishHadith'] ?? ''),
                    'narrator_bn' => $narrator,
                    'grade' => $grade['grade_bn'],
                    'reference' => cleanHadithText($hr['HadithNote'] ?? ''),
                    'footnote_bn' => cleanHadithText($hr['HadithNote'] ?? '')
                ];
            }
        }
    }

    sendJsonResponse([
        'success' => true,
        'count' => count($items),
        'page' => $page,
        'hadiths' => $items
    ]);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load hadiths: ' . $e->getMessage()
    ], 500);
}
?>
