<?php
/**
 * ==============================================================================
 * DEEN ONE (দ্বীন ওয়ান) - OFFICIAL PUBLIC PRIVACY POLICY
 * Clean, Headerless Full-Bleed Design Matching In-App WebView & Web Standards
 * Controlled via ?lang=bn|en and ?theme=dark|light URL parameters from App
 * ==============================================================================
 */

require_once dirname(__DIR__) . '/config.php';
require_once dirname(__DIR__) . '/api/db.php';

header('Cache-Control: public, max-age=1800, stale-while-revalidate=86400');

// Helper for formatting Bengali date
function formatBnDatePublic($timestamp = null) {
    $timestamp = $timestamp ?: time();
    $bnMonths = ["জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"];
    $bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
    $day = str_replace(range(0, 9), $bnDigits, date('j', $timestamp));
    $month = $bnMonths[(int)date('n', $timestamp) - 1];
    $year = str_replace(range(0, 9), $bnDigits, date('Y', $timestamp));
    return "{$day} {$month}, {$year}";
}

// Language detection (?lang=bn|en, default 'en')
$lang = strtolower(trim($_GET['lang'] ?? ($_COOKIE['deenone_privacy_lang'] ?? 'en')));
if (!in_array($lang, ['en', 'bn'])) {
    $lang = 'en';
}

// Theme detection (?theme=dark|light|auto, default 'light')
$theme = strtolower(trim($_GET['theme'] ?? ($_COOKIE['deenone_privacy_theme'] ?? 'light')));
if (!in_array($theme, ['dark', 'light', 'auto'])) {
    $theme = 'light';
}

$settings = [];
try {
    if (function_exists('getDbConnection')) {
        $pdo = getDbConnection();
        if ($pdo) {
            $keys = [
                'privacy_title_bn',
                'privacy_title_en',
                'privacy_updated_at_bn',
                'privacy_updated_at_en',
                'privacy_policy_content',
                'privacy_policy_content_en',
                'privacy_contact_email',
                'privacy_organization_name',
                'app_name'
            ];
            $inQuery = implode(',', array_fill(0, count($keys), '?'));
            $stmt = $pdo->prepare("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN ($inQuery)");
            $stmt->execute($keys);
            while ($row = $stmt->fetch()) {
                $settings[$row['setting_key']] = $row['setting_value'];
            }
        }
    }
} catch (Throwable $e) {}

$contactEmail = !empty($settings['privacy_contact_email']) ? $settings['privacy_contact_email'] : 'privacy@deenone.top';

// Titles
$titleBn = !empty($settings['privacy_title_bn']) ? $settings['privacy_title_bn'] : 'প্রাইভেসি পলিসি';
$titleEn = !empty($settings['privacy_title_en']) ? $settings['privacy_title_en'] : 'PRIVACY POLICY';

// Last Updated Dates
$updatedBn = !empty($settings['privacy_updated_at_bn']) ? $settings['privacy_updated_at_bn'] : ('সর্বশেষ হালনাগাদ: ' . formatBnDatePublic());
$updatedEn = !empty($settings['privacy_updated_at_en']) ? $settings['privacy_updated_at_en'] : ('Last update: ' . date('F j, Y'));

// Default Bodies (Clean HTML without H1 title and date, which are rendered separately)
$defaultBn = <<<HTML
<p>এই <strong>Privacy Policy</strong> ব্যাখ্যা করে কীভাবে DeenOne ("<strong>DeenOne</strong>", "<strong>আমরা</strong>" বা "<strong>আমাদের</strong>") আপনার ব্যক্তিগত তথ্য সংগ্রহ, ব্যবহার এবং সুরক্ষিত রাখে, যা আপনি আমাদের ওয়েবসাইট <a href="https://deenone.top">https://deenone.top</a> এবং সংশ্লিষ্ট ডিজিটাল সেবা ও মোবাইল অ্যাপ্লিকেশন (একত্রে "<strong>Services</strong>") ব্যবহারের সময় প্রদান করেন।</p>

<p>এই Privacy Policy <strong>নিম্নোক্ত ক্ষেত্রসমূহে প্রযোজ্য নয় :</strong></p>

<ul>
  <li><strong>অফলাইনে সংগৃহীত তথ্য;</strong> অথবা কোনো অননুমোদিত তৃতীয় পক্ষের অ্যাপ্লিকেশন বা কন্টেন্টের (বিজ্ঞাপন সহ) মাধ্যমে সংগৃহীত তথ্য যা আমাদের প্ল্যাটফর্মের সাথে লিংক করা থাকতে পারে।</li>
  <li><strong>তৃতীয় পক্ষের পেমেন্ট প্রসেসর দ্বারা প্রক্রিয়াকৃত তথ্য।</strong> কিছু সেবার জন্য ব্যবহারকারীকে সরাসরি তৃতীয় পক্ষের বিশ্বস্ত পেমেন্ট প্রসেসরের মাধ্যমে পেমেন্ট তথ্য প্রদান করতে হতে পারে।</li>
  <li><strong>সোশ্যাল মিডিয়া ইন্টিগ্রেশন।</strong> ব্যবহারকারী চাইলে তাদের সোশ্যাল মিডিয়া অ্যাকাউন্ট যুক্ত করতে পারেন। অ্যাকাউন্ট যুক্ত থাকলে সংশ্লিষ্ট প্ল্যাটফর্মের গোপনীয়তা নীতি অনুযায়ী তথ্য শেয়ার হতে পারে।</li>
</ul>

<h3>১. আমরা কী কী তথ্য সংগ্রহ করি</h3>
<p>সঠিক নামাজের সময়সূচি, কিবলা দিক এবং বিশুদ্ধ ইসলামিক লাইফস্টাইল ট্র্যাকিং নিশ্চিত করতে আমরা শুধুমাত্র প্রয়োজনীয় তথ্য সংগ্রহ করি:</p>
<ul>
  <li><strong>প্রোফাইল তথ্য:</strong> নাম, ইমেইল বা ফোন নম্বর (অ্যাকাউন্ট ব্যাকআপ ও ডেটা সিঙ্কের জন্য)।</li>
  <li><strong>লোকেশন ডাটা:</strong> শুধুমাত্র সালাতের ওয়াক্ত ও কিবলা কম্পাসের দিক নির্ধারণে ডিভাইসে ব্যবহৃত হয়। এটি সার্ভারে স্থায়ীভাবে জমা বা ট্র্যাক করা হয় না।</li>
  <li><strong>ইবাদত ও সালাত ট্র্যাকিং:</strong> দৈনিক সালাত, কুরআন তিলাওয়াত ও তাসবীহ জিকিরের পরিসংখ্যান (যা ১০০% ব্যক্তিগত ও সুরক্ষিত)।</li>
  <li><strong>রক্তদান নেটওয়ার্ক:</strong> স্বেচ্ছায় নিবন্ধিত রক্তদাতাদের রক্তের গ্রুপ, জেলা ও যোগাযোগের নম্বর শুধুমাত্র জরুরি প্রয়োজনে প্রদর্শিত হয়।</li>
</ul>

<h3>২. তথ্যের নিরাপত্তা ও সুরক্ষা</h3>
<p>আপনার সমস্ত তথ্য আধুনিক SSL/TLS এনক্রিপশন প্রোটোকলের মাধ্যমে সুরক্ষিত থাকে। বাণিজ্যিক বিজ্ঞাপনদাতার কাছে তথ্য বিক্রি বা হস্তান্তর সম্পূর্ণ নিষিদ্ধ।</p>

<h3>৩. অ্যাকাউন্ট ও ডেটা স্থায়ীভাবে মুছে ফেলা (Data Deletion)</h3>
<p>ব্যবহারকারী যেকোনো সময় অ্যাপের প্রোফাইল সেটিংস থেকে অথবা <code>{$contactEmail}</code> এ যোগাযোগ করে তার সমস্ত সংরক্ষিত ডেটা স্থায়ীভাবে মুছে ফেলতে পারেন।</p>
HTML;

$defaultEn = <<<HTML
<p>This <strong>Privacy Policy</strong> explains how DeenOne ("<strong>DeenOne</strong>", "<strong>us</strong>" or "<strong>we</strong>"), collects, uses and protects your personal information, which we may collect from you or that you may provide when you visit any of our websites, including <a href="https://deenone.top">https://deenone.top</a> and other digital properties that link to this Privacy Policy (our "<strong>Website(s)</strong>") or otherwise use our Website(s) or applications (collectively referred to as services the "<strong>Services</strong>").</p>

<p>This Privacy Policy <strong>does not apply to :</strong></p>

<ul>
  <li><strong>Information we may collect from you offline;</strong> or information collected by any third-party, including through any application or content (including advertising) that may link to or be accessible from or through the Websites.</li>
  <li><strong>Payment Information Processed by third-parties.</strong> Certain Services may require users to provide payment information directly to third-party payment processors.</li>
  <li><strong>Social Media Integration.</strong> Users may link their account with third-party social media platforms. When accounts are linked, the social media platform may share certain data with DeenOne as per its privacy policy. If you are accessing our Services through a social media account, please refer to the social media provider's privacy policy for information regarding their data collection.</li>
</ul>

<h3>1. Information We Collect</h3>
<p>To deliver an authentic, distraction-free Islamic lifestyle experience and accurate prayer timings, DeenOne collects minimal necessary information:</p>
<ul>
  <li><strong>Account & Profile:</strong> Name, phone number or email for account authentication, streak progress, and cloud backup across devices.</li>
  <li><strong>Location Data:</strong> Accessed on-device strictly to calculate accurate prayer times, sunrise/sunset, and Qibla compass direction. Location coordinates are never sold or permanently stored on our servers.</li>
  <li><strong>Worship & Habits Activity:</strong> Daily Salah logs, Quran reading progress, and Tasbih counts stored with end-to-end security.</li>
  <li><strong>Blood Donor Registry:</strong> Blood group, district, and contact numbers shared voluntarily for emergency blood requests.</li>
</ul>

<h3>2. Data Security & Encryption</h3>
<p>All sensitive transmissions are secured using SSL/TLS 256-bit encryption. We never sell, rent, or trade user personal information to third-party commercial advertisers.</p>

<h3>3. Data Retention & Account Deletion</h3>
<p>You have full ownership of your data. You may request permanent deletion of your account and all associated data at any time directly through the app settings or by contacting <code>{$contactEmail}</code>.</p>
HTML;

// Clean out any legacy duplicated H1 or date paragraph from custom saved content if present
function cleanLegacyHeaders($html) {
    $html = preg_replace('/<h1[^>]*>.*?<\/h1>/si', '', $html);
    $html = preg_replace('/<p[^>]*>\s*(Last Updated|Last update|সর্বশেষ হালনাগাদ).*?<\/p>/si', '', $html);
    return trim($html);
}

$rawContentBn = !empty($settings['privacy_policy_content']) ? $settings['privacy_policy_content'] : $defaultBn;
$rawContentEn = !empty($settings['privacy_policy_content_en']) ? $settings['privacy_policy_content_en'] : $defaultEn;

$contentBn = cleanLegacyHeaders($rawContentBn);
$contentEn = cleanLegacyHeaders($rawContentEn);

$activeTitle = ($lang === 'bn') ? $titleBn : $titleEn;
$activeUpdated = ($lang === 'bn') ? $updatedBn : $updatedEn;
$activeContent = ($lang === 'bn') ? $contentBn : $contentEn;
?>
<!DOCTYPE html>
<html lang="<?= htmlspecialchars($lang) ?>" data-theme="<?= htmlspecialchars($theme) ?>">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">
    <title><?= htmlspecialchars($activeTitle) ?> - DeenOne</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&family=Hind+Siliguri:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        :root {
            --bg-page: #ffffff;
            --text-primary: #000000;
            --text-secondary: #222222;
            --text-muted: #666666;
            --slash-color: #0284c7;
            --link-color: #0284c7;
            --border-color: #e5e7eb;
            --code-bg: #f3f4f6;
        }

        [data-theme="dark"] {
            --bg-page: #0f172a;
            --text-primary: #f8fafc;
            --text-secondary: #cbd5e1;
            --text-muted: #94a3b8;
            --slash-color: #38bdf8;
            --link-color: #38bdf8;
            --border-color: #334155;
            --code-bg: #1e293b;
        }

        @media (prefers-color-scheme: dark) {
            [data-theme="auto"] {
                --bg-page: #0f172a;
                --text-primary: #f8fafc;
                --text-secondary: #cbd5e1;
                --text-muted: #94a3b8;
                --slash-color: #38bdf8;
                --link-color: #38bdf8;
                --border-color: #334155;
                --code-bg: #1e293b;
            }
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            -webkit-tap-highlight-color: transparent;
        }

        body {
            background-color: var(--bg-page);
            color: var(--text-secondary);
            font-family: 'Inter', 'Hind Siliguri', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            font-size: 15px;
            line-height: 1.65;
            min-height: 100vh;
            padding: 24px 20px 60px;
        }

        .content-container {
            max-width: 900px;
            width: 100%;
            margin: 0 auto;
        }

        /* Clean Page Header */
        .policy-header-wrap {
            margin-bottom: 24px;
        }

        .policy-title {
            font-size: 26px;
            font-weight: 800;
            letter-spacing: -0.3px;
            color: var(--text-primary);
            text-transform: uppercase;
            margin-bottom: 8px;
            line-height: 1.25;
        }

        .policy-title .slash {
            color: var(--slash-color);
            font-weight: 900;
            margin-right: 4px;
        }

        .policy-updated {
            font-size: 14px;
            color: var(--text-muted);
            font-weight: 500;
        }

        /* Policy Body Content */
        .policy-content {
            font-size: 15px;
            line-height: 1.7;
            color: var(--text-secondary);
        }

        .policy-content p {
            margin-bottom: 20px;
        }

        .policy-content strong, .policy-content b {
            font-weight: 700;
            color: var(--text-primary);
        }

        .policy-content a {
            color: var(--link-color);
            text-decoration: underline;
            text-underline-offset: 2px;
        }

        .policy-content ul, .policy-content ol {
            margin: 16px 0 24px 20px;
        }

        .policy-content li {
            margin-bottom: 16px;
            padding-left: 6px;
            line-height: 1.65;
        }

        .policy-content h2, .policy-content h3, .policy-content h4 {
            color: var(--text-primary);
            font-weight: 800;
            margin-top: 32px;
            margin-bottom: 14px;
            letter-spacing: -0.3px;
        }

        .policy-content h2 {
            font-size: 20px;
        }

        .policy-content h3 {
            font-size: 17px;
        }

        .policy-content h4 {
            font-size: 15px;
        }

        .policy-content code {
            font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
            background: var(--code-bg);
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 13.5px;
            color: var(--text-primary);
        }

        .policy-content blockquote {
            border-left: 3px solid var(--slash-color);
            padding-left: 16px;
            margin: 20px 0;
            color: var(--text-muted);
            font-style: italic;
        }

        @media (max-width: 640px) {
            body {
                padding: 18px 16px 40px;
            }
            .policy-title {
                font-size: 22px;
            }
            .policy-updated {
                font-size: 13.5px;
                margin-bottom: 20px;
            }
        }
    </style>
</head>
<body>
    <main class="content-container">
        <header class="policy-header-wrap">
            <h1 class="policy-title"><span class="slash">/</span><?= htmlspecialchars($activeTitle) ?></h1>
            <p class="policy-updated"><?= htmlspecialchars($activeUpdated) ?></p>
        </header>
        <article class="policy-content">
            <?= $activeContent ?>
        </article>
    </main>

    <script>
        (function() {
            try {
                if (window.AndroidPrivacyBridge && document.documentElement) {
                    window.AndroidPrivacyBridge.savePageContent(document.documentElement.outerHTML, "<?= $lang ?>", "<?= $theme ?>");
                }
            } catch(e) {}
        })();
    </script>
</body>
</html>
