<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - UNIVERSAL DUAL-LANGUAGE & ISLAMIC LOCALIZATION ENGINE
 * ==============================================================================
 * Implements Pure Bengali (bn) and Pure English (en) clean localization
 * with zero bracket pollution, persistent session/cookie storage, and
 * authentic Islamic calendar (Hijri) integration.
 */

if (session_status() === PHP_SESSION_NONE) {
    if (defined('ADMIN_SESSION_NAME')) {
        session_name(ADMIN_SESSION_NAME);
    }
    session_start();
}

/**
 * Handle language switch request via URL parameter (?lang=bn or ?lang=en)
 */
if (isset($_GET['lang'])) {
    $reqLang = strtolower(trim($_GET['lang']));
    if ($reqLang === 'en' || $reqLang === 'bn') {
        $_SESSION['admin_lang'] = $reqLang;
        setcookie('deenone_admin_lang', $reqLang, time() + (86400 * 90), '/');
    }
}

/**
 * Retrieve current active language code ('bn' or 'en')
 */
function getAdminLang() {
    if (isset($_GET['lang'])) {
        $reqLang = strtolower(trim($_GET['lang']));
        if ($reqLang === 'en' || $reqLang === 'bn') {
            return $reqLang;
        }
    }
    if (!empty($_SESSION['admin_lang'])) {
        return $_SESSION['admin_lang'] === 'en' ? 'en' : 'bn';
    }
    if (!empty($_COOKIE['deenone_admin_lang'])) {
        return $_COOKIE['deenone_admin_lang'] === 'en' ? 'en' : 'bn';
    }
    return 'bn'; // Default: Pure Bengali
}

/**
 * Convenience helper to check if active language is English
 */
function isEn() {
    return getAdminLang() === 'en';
}

/**
 * Convenience helper to check if active language is Bengali
 */
function isBn() {
    return getAdminLang() === 'bn';
}

/**
 * Universal Dual-Language Translation Helper
 * Rule 5 Compliance: Pure language output, zero bracket mixing.
 *
 * @param string $bn Bengali version
 * @param string $en English version
 * @return string
 */
function __($bn, $en) {
    return (getAdminLang() === 'en') ? $en : $bn;
}

/**
 * Format numbers according to active language (Bengali digits vs English digits)
 */
function toLangNum($num) {
    $strNum = (string)$num;
    if (getAdminLang() === 'bn') {
        $bn = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
        return strtr($strNum, ['0'=>$bn[0],'1'=>$bn[1],'2'=>$bn[2],'3'=>$bn[3],'4'=>$bn[4],'5'=>$bn[5],'6'=>$bn[6],'7'=>$bn[7],'8'=>$bn[8],'9'=>$bn[9]]);
    }
    return $strNum;
}

/**
 * Generates URL to switch to a specific target language while preserving all other query parameters
 */
function getLangSwitchUrl($targetLang) {
    $params = $_GET;
    $params['lang'] = $targetLang;
    $cleanPath = strtok($_SERVER['REQUEST_URI'] ?? 'dashboard.php', '?');
    return $cleanPath . '?' . http_build_query($params);
}

/**
 * Algorithmic Gregorian to Hijri Date Conversion
 */
function getGregorianToHijri($time = null) {
    if (!$time) $time = time();
    $d = (int)date('j', $time);
    $m = (int)date('n', $time);
    $y = (int)date('Y', $time);

    if (($y > 1582) || (($y == 1582) && ($m > 10)) || (($y == 1582) && ($m == 10) && ($d > 14))) {
        $jd = (int)((1461 * ($y + 4800 + (int)(($m - 14) / 12))) / 4) +
              (int)((367 * ($m - 2 - 12 * ((int)(($m - 14) / 12)))) / 12) -
              (int)((3 * (int)((($y + 4900 + (int)(($m - 14) / 12)) / 100))) / 4) +
              $d - 32075;
    } else {
        $jd = 367 * $y - (int)((7 * ($y + 5001 + (int)(($m - 9) / 7))) / 4) +
              (int)((275 * $m) / 9) + $d + 1729777;
    }

    $l = $jd - 1948440 + 10632;
    $n = (int)(($l - 1) / 10631);
    $l = $l - 10631 * $n + 354;
    $j = ((int)((10985 - $l) / 5316)) * ((int)((50 * $l) / 17719)) +
         ((int)($l / 5670)) * ((int)((43 * $l) / 15238));
    $l = $l - ((int)((30 - $j) / 15)) * ((int)((17719 * $j) / 50)) -
         ((int)($j / 16)) * ((int)((15238 * $j) / 43)) + 29;
    $m = (int)((24 * $l) / 709);
    $d = $l - (int)((709 * $m) / 24);
    $y = 30 * $n + $j - 30;

    return ['day' => $d, 'month' => $m, 'year' => $y];
}

/**
 * Returns formatted Hijri Date string in current language
 */
function getIslamicHijriDate() {
    $h = getGregorianToHijri();
    $lang = getAdminLang();

    $bnMonths = [
        1 => 'মুহাররম', 2 => 'সফর', 3 => 'রবিউল আউয়াল', 4 => 'রবিউস সানি',
        5 => 'জমাদিউল আউয়াল', 6 => 'জমাদিউস সানি', 7 => 'রজব', 8 => 'শাবান',
        9 => 'রমজান', 10 => 'শাওয়াল', 11 => 'জিলক্বদ', 12 => 'জিলহজ্জ'
    ];
    $enMonths = [
        1 => 'Muharram', 2 => 'Safar', 3 => "Rabi' al-Awwal", 4 => "Rabi' al-Thani",
        5 => 'Jumada al-Awwal', 6 => 'Jumada al-Thani', 7 => 'Rajab', 8 => "Sha'ban",
        9 => 'Ramadan', 10 => 'Shawwal', 11 => "Dhu al-Qi'dah", 12 => 'Dhu al-Hijjah'
    ];

    if ($lang === 'en') {
        $mName = $enMonths[$h['month']] ?? "Rabi' al-Awwal";
        return "{$h['day']} {$mName}, {$h['year']} AH";
    } else {
        $dayBn = toLangNum($h['day']);
        $yearBn = toLangNum($h['year']);
        $mName = $bnMonths[$h['month']] ?? 'রবিউল আউয়াল';
        return "{$dayBn} {$mName}, {$yearBn} হিজরি";
    }
}

/**
 * Returns formatted Gregorian Date string in current language
 */
function getGregorianCurrentDate() {
    $lang = getAdminLang();
    if ($lang === 'en') {
        return date('d F, Y');
    }

    $bnMonths = ["জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"];
    $day = toLangNum((int)date('d'));
    $month = $bnMonths[(int)date('m') - 1];
    $year = toLangNum((int)date('Y'));
    return "{$day} {$month}, {$year}";
}
