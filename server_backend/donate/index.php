<?php
/**
 * ==============================================================================
 * DEEN ONE - PUBLIC & IN-APP DONATION & VOLUNTARY CONTRIBUTION HUB
 * Responsive (PC Desktop + Mobile) Ultra-Realistic Islamic Contribution Suite
 * ==============================================================================
 */

require_once dirname(__DIR__) . '/config.php';
require_once dirname(__DIR__) . '/api/db.php';

$pdo = getDbConnection();

header('Cache-Control: public, max-age=3600, stale-while-revalidate=86400');

// Language detection (Default: 'bn' as requested by user; respects ?lang=en/bn)
$lang = strtolower(trim($_GET['lang'] ?? ($_COOKIE['deenone_donate_lang'] ?? 'bn')));
if (!in_array($lang, ['en', 'bn'])) {
    $lang = 'bn';
}

// Theme detection (?theme=dark|light, cookie, or auto)
$theme = strtolower(trim($_GET['theme'] ?? ($_COOKIE['deenone_donate_theme'] ?? 'auto')));
if (!in_array($theme, ['dark', 'light', 'auto'])) {
    $theme = 'auto';
}

// Fetch dynamic settings from database
$settingsStmt = $pdo->query("SELECT setting_key, setting_value FROM donation_settings");
$settings = [];
while ($row = $settingsStmt->fetch()) {
    $settings[$row['setting_key']] = $row['setting_value'];
}

// App Settings (WhatsApp, Privacy Policy & Terms)
$appSettingsStmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN ('support_whatsapp', 'app_name', 'privacy_policy_content', 'privacy_policy_url', 'terms_service_content', 'terms_service_url')");
$appSettings = [];
while ($row = $appSettingsStmt->fetch()) {
    $appSettings[$row['setting_key']] = $row['setting_value'];
}
$whatsappNumber = preg_replace('/[^0-9]/', '', $appSettings['support_whatsapp'] ?? '8801700000000');
$privacyPolicyContent = $appSettings['privacy_policy_content'] ?? 'DeenOne (দীন ওয়ান) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে প্রতিশ্রুতিবদ্ধ। আমরা ব্যবহারকারীর সালাত ট্র্যাকিং, কুরআন তিলাওয়াত ও কুইজ পয়েন্ট ক্লাউডে সিঙ্ক করার উদ্দেশ্যে শুধুমাত্র নাম ও ফোন নম্বর সংগ্রহ করি। কোনো প্রকার অননুমোদিত তৃতীয় পক্ষের সাথে ব্যবহারকারীর তথ্য শেয়ার করা হয় না। রক্তদাতা হিসেবে স্বেচ্ছায় নিবন্ধিতদের তথ্য শুধুমাত্র জরুরি প্রয়োজনে অন্য মুসলিম ভাইদের সহায়তায় ব্যবহৃত হয়।';
$termsServiceContent = $appSettings['terms_service_content'] ?? 'দীন ওয়ান একটি অলাভজনক ও উন্মুক্ত দ্বীনি সেবা। এখানে প্রদত্ত সকল অনুদান ও সদকাহ ঐচ্ছিক এবং তা সরাসরি বিশুদ্ধ দ্বীনি কন্টেন্ট গবেষণা, অ্যাপের আধুনিকায়ন এবং উচ্চগতির সার্ভার পরিচালনায় ব্যবহৃত হয়।';

// Registered Users Dynamic Count
$totalUsers = (int)$pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
$usersMode = $settings['stat_users_display_mode'] ?? 'auto';
if ($usersMode === 'auto') {
    if ($totalUsers > 1000000) {
        $usersCount = round($totalUsers / 1000000, 1) . 'M+';
    } elseif ($totalUsers > 1000) {
        $usersCount = round($totalUsers / 1000, 1) . 'K+';
    } else {
        $usersCount = (string)max(1, $totalUsers) . '+';
    }
} else {
    $usersCount = $settings['stat_users_custom'] ?? '3.2M+';
}

$yearsUsage = $settings['stat_years_usage'] ?? '907+';
$countries = $settings['stat_countries'] ?? '177+';
$usdRate = (float)($settings['usd_to_bdt_rate'] ?? 120);

// Helper for bilingual strings
function t($en, $bn) {
    global $lang;
    return $lang === 'bn' ? $bn : $en;
}

// Helper to render BBCode
function renderBBCode($text) {
    if (empty($text)) return '';
    $text = htmlspecialchars($text, ENT_QUOTES, 'UTF-8');
    $find = [
        '/\[b\](.*?)\[\/b\]/is',
        '/\[i\](.*?)\[\/i\]/is',
        '/\[u\](.*?)\[\/u\]/is',
        '/\[color=(.*?)\](.*?)\[\/color\]/is',
        '/\[size=(.*?)\](.*?)\[\/size\]/is',
        '/\[url=(.*?)\](.*?)\[\/url\]/is',
        '/\[badge=(.*?)\](.*?)\[\/badge\]/is'
    ];
    $replace = [
        '<strong>$1</strong>',
        '<em>$1</em>',
        '<u>$1</u>',
        '<span style="color:$1;">$2</span>',
        '<span style="font-size:$1;">$2</span>',
        '<a href="$1" target="_blank" rel="noopener noreferrer" style="color:var(--primary); text-decoration:underline;">$2</a>',
        '<span class="badge badge-$1">$2</span>'
    ];
    return nl2br(preg_replace($find, $replace, $text));
}

// Parse FAQs
$faqs = json_decode($settings['faqs_json'] ?? '[]', true) ?: [];
?>
<!DOCTYPE html>
<html lang="<?php echo $lang; ?>" <?php echo ($theme !== 'auto') ? 'data-theme="' . $theme . '"' : ''; ?>>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">
  <title><?php echo t('Support DeenOne - Voluntary Contribution', 'দীন ওয়ান খেদমত - প্রতিদিন সদকাহ ও অনুদান'); ?></title>
  
  <!-- Google Fonts: Inter, Outfit, Amiri -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Amiri:ital,wght@0,400;0,700;1,400&family=Inter:wght@400;500;600;700;800;900&family=Outfit:wght@500;600;700;800;900&display=swap" rel="stylesheet">
  
  <!-- FontAwesome 6 CDN -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

  <?php if ((($settings['google_recaptcha_enabled'] ?? '0') == '1') && !empty($settings['google_recaptcha_site_key'])): ?>
    <!-- Google reCAPTCHA -->
    <script src="https://www.google.com/recaptcha/api.js?hl=<?php echo $lang === 'bn' ? 'bn' : 'en'; ?>" async defer></script>
  <?php endif; ?>

  <style>
    /* ==========================================================================
       THEME TOKENS & COLOR SYSTEM (Light & Dark Mode)
       ========================================================================== */
    :root {
      --primary: #059669;
      --primary-dark: #047857;
      --primary-light: #10b981;
      --primary-soft: #ecfdf5;
      --primary-glow: rgba(16, 185, 129, 0.2);
      
      --accent-gold: #d97706;
      --accent-gold-soft: #fef3c7;
      --accent-blue: #0284c7;
      --accent-blue-soft: #e0f2fe;
      --accent-red: #e11d48;
      --accent-red-soft: #ffe4e6;
      
      --bg-page: #f8fafc;
      --bg-card: #ffffff;
      --bg-card-elevated: #f1f5f9;
      --bg-input: #f8fafc;
      
      --text-heading: #0f172a;
      --text-body: #334155;
      --text-muted: #64748b;
      
      --border-color: #e2e8f0;
      --border-focus: #059669;
      
      --chip-bg: #ffffff;
      --chip-border: #cbd5e1;
      --chip-text: #0f172a;
      --chip-active-bg: #065f46;
      --chip-active-text: #ffffff;
      
      --radius-sm: 8px;
      --radius-md: 14px;
      --radius-lg: 20px;
      --radius-xl: 26px;
      
      --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.04);
      --shadow-md: 0 8px 24px rgba(0, 0, 0, 0.06);
      --shadow-lg: 0 16px 40px rgba(5, 150, 105, 0.12);
      
      --hero-gradient: linear-gradient(180deg, #ffffff 0%, #f0fdf4 100%);
      --banner-gradient: linear-gradient(135deg, #064e3b 0%, #047857 100%);
      --card-bg-gradient: linear-gradient(180deg, #ffffff 0%, #fcfdfd 100%);
    }

    /* Dark Mode Theme Tokens */
    html[data-theme="dark"] {
      --primary: #10b981;
      --primary-dark: #059669;
      --primary-light: #34d399;
      --primary-soft: rgba(16, 185, 129, 0.14);
      --primary-glow: rgba(16, 185, 129, 0.3);
      
      --accent-gold: #fbbf24;
      --accent-gold-soft: rgba(245, 158, 11, 0.15);
      --accent-blue: #38bdf8;
      --accent-blue-soft: rgba(56, 189, 248, 0.15);
      --accent-red: #fb7185;
      --accent-red-soft: rgba(251, 113, 133, 0.15);
      
      --bg-page: #0b1120;
      --bg-card: #131e32;
      --bg-card-elevated: #1e293b;
      --bg-input: #0f172a;
      
      --text-heading: #f8fafc;
      --text-body: #cbd5e1;
      --text-muted: #94a3b8;
      
      --border-color: rgba(255, 255, 255, 0.08);
      --border-focus: #10b981;
      
      --chip-bg: #1e293b;
      --chip-border: rgba(255, 255, 255, 0.12);
      --chip-text: #f8fafc;
      --chip-active-bg: #10b981;
      --chip-active-text: #064e3b;
      
      --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.25);
      --shadow-md: 0 8px 24px rgba(0, 0, 0, 0.35);
      --shadow-lg: 0 16px 40px rgba(0, 0, 0, 0.5);
      
      --hero-gradient: linear-gradient(180deg, #131e32 0%, #0f172a 100%);
      --banner-gradient: linear-gradient(135deg, #064e3b 0%, #065f46 100%);
      --card-bg-gradient: linear-gradient(180deg, #131e32 0%, #111a2d 100%);
    }

    @media (prefers-color-scheme: dark) {
      html:not([data-theme="light"]) {
        --primary: #10b981;
        --primary-dark: #059669;
        --primary-light: #34d399;
        --primary-soft: rgba(16, 185, 129, 0.14);
        --primary-glow: rgba(16, 185, 129, 0.3);
        
        --accent-gold: #fbbf24;
        --accent-gold-soft: rgba(245, 158, 11, 0.15);
        --accent-blue: #38bdf8;
        --accent-blue-soft: rgba(56, 189, 248, 0.15);
        --accent-red: #fb7185;
        --accent-red-soft: rgba(251, 113, 133, 0.15);
        
        --bg-page: #0b1120;
        --bg-card: #131e32;
        --bg-card-elevated: #1e293b;
        --bg-input: #0f172a;
        
        --text-heading: #f8fafc;
        --text-body: #cbd5e1;
        --text-muted: #94a3b8;
        
        --border-color: rgba(255, 255, 255, 0.08);
        --border-focus: #10b981;
        
        --chip-bg: #1e293b;
        --chip-border: rgba(255, 255, 255, 0.12);
        --chip-text: #f8fafc;
        --chip-active-bg: #10b981;
        --chip-active-text: #064e3b;
        
        --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.25);
        --shadow-md: 0 8px 24px rgba(0, 0, 0, 0.35);
        --shadow-lg: 0 16px 40px rgba(0, 0, 0, 0.5);
        
        --hero-gradient: linear-gradient(180deg, #131e32 0%, #0f172a 100%);
        --banner-gradient: linear-gradient(135deg, #064e3b 0%, #065f46 100%);
        --card-bg-gradient: linear-gradient(180deg, #131e32 0%, #111a2d 100%);
      }
    }

    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      -webkit-tap-highlight-color: transparent;
    }

    body {
      font-family: 'Inter', system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
      background-color: var(--bg-page);
      color: var(--text-body);
      line-height: 1.55;
      -webkit-font-smoothing: antialiased;
      padding-bottom: 50px;
      transition: background-color 0.25s ease, color 0.25s ease;
    }

    /* Container for PC & Mobile */
    .page-wrapper {
      width: 100%;
      max-width: 1140px;
      margin: 0 auto;
      padding: 16px 14px;
    }

    @media (min-width: 768px) {
      .page-wrapper {
        padding: 24px 20px;
      }
    }

    /* ==========================================================================
       HERO & IMPACT SECTION
       ========================================================================== */
    .hero-banner {
      background: var(--hero-gradient);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-xl);
      padding: 24px 18px 20px;
      text-align: center;
      margin-bottom: 22px;
      box-shadow: var(--shadow-sm);
      position: relative;
      overflow: hidden;
    }

    .hero-title {
      font-family: 'Outfit', sans-serif;
      font-size: 20px;
      font-weight: 800;
      line-height: 1.35;
      color: var(--text-heading);
      margin-bottom: 8px;
      letter-spacing: -0.3px;
      max-width: 720px;
      margin-left: auto;
      margin-right: auto;
    }

    @media (min-width: 768px) {
      .hero-title {
        font-size: 26px;
        line-height: 1.3;
      }
    }

    .hero-subtitle {
      font-size: 13.5px;
      color: var(--text-muted);
      line-height: 1.55;
      max-width: 580px;
      margin: 0 auto 18px;
    }

    .impact-header-tag {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      font-size: 10.5px;
      font-weight: 800;
      color: var(--primary);
      text-transform: uppercase;
      letter-spacing: 1.2px;
      background: var(--primary-soft);
      padding: 3px 10px;
      border-radius: 20px;
      margin-bottom: 12px;
    }

    .impact-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 10px;
      max-width: 600px;
      margin: 0 auto;
    }

    @media (min-width: 768px) {
      .impact-grid {
        gap: 14px;
      }
    }

    .impact-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 12px 6px;
      text-align: center;
      box-shadow: var(--shadow-sm);
      transition: transform 0.2s ease, border-color 0.2s ease;
    }

    .impact-card:hover {
      border-color: var(--primary);
      transform: translateY(-2px);
    }

    .impact-val {
      font-family: 'Outfit', sans-serif;
      font-size: 19px;
      font-weight: 900;
      color: var(--primary);
      margin-bottom: 2px;
      letter-spacing: -0.4px;
    }

    @media (min-width: 768px) {
      .impact-val {
        font-size: 23px;
      }
    }

    .impact-lbl {
      font-size: 11px;
      font-weight: 700;
      color: var(--text-muted);
      line-height: 1.2;
    }

    /* ==========================================================================
       RESPONSIVE MAIN TWO-COLUMN GRID (PC & MOBILE)
       ========================================================================== */
    .main-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 22px;
    }

    @media (min-width: 1024px) {
      .main-grid {
        grid-template-columns: 1.15fr 0.85fr;
        gap: 28px;
        align-items: start;
      }

      .desktop-sticky-col {
        position: sticky;
        top: 20px;
      }
    }

    /* ==========================================================================
       DONATION / CONTRIBUTION BOX (Right Column)
       ========================================================================== */
    .donation-box {
      background: var(--card-bg-gradient);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-xl);
      padding: 22px 18px;
      box-shadow: var(--shadow-md);
      position: relative;
    }

    @media (min-width: 768px) {
      .donation-box {
        padding: 26px 22px;
      }
    }

    .amount-header-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 16px;
    }

    .amount-heading {
      font-family: 'Outfit', sans-serif;
      font-size: 17px;
      font-weight: 800;
      color: var(--text-heading);
    }

    .currency-switch-btn {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 5px 12px;
      background: var(--bg-card);
      border: 1.5px solid var(--border-color);
      border-radius: 20px;
      font-size: 12.5px;
      font-weight: 800;
      color: var(--primary);
      cursor: pointer;
      user-select: none;
      transition: all 0.2s ease;
    }

    .currency-switch-btn:hover {
      border-color: var(--primary);
      background: var(--primary-soft);
    }

    .chips-grid-layout {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 8px;
      margin-bottom: 18px;
    }

    .amt-chip {
      height: 44px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1.5px solid var(--chip-border);
      border-radius: 22px;
      font-size: 15px;
      font-weight: 800;
      color: var(--chip-text);
      background: var(--chip-bg);
      cursor: pointer;
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .amt-chip:hover {
      border-color: var(--primary);
    }

    .amt-chip.active {
      background: var(--chip-active-bg);
      border-color: var(--chip-active-bg);
      color: var(--chip-active-text);
      box-shadow: 0 4px 12px var(--primary-glow);
    }

    .custom-amt-group {
      margin-bottom: 18px;
    }

    .custom-amt-label {
      font-size: 12px;
      font-weight: 600;
      color: var(--text-muted);
      margin-bottom: 6px;
      display: block;
    }

    .custom-amt-field {
      position: relative;
      display: flex;
      align-items: center;
      background: var(--bg-input);
      border: 1.5px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 0 14px;
      height: 48px;
      transition: border-color 0.2s ease, box-shadow 0.2s ease;
    }

    .custom-amt-field:focus-within {
      border-color: var(--border-focus);
      box-shadow: 0 0 0 3px var(--primary-glow);
      background: var(--bg-card);
    }

    .curr-sym-prefix {
      font-size: 17px;
      font-weight: 800;
      color: var(--primary);
      margin-right: 8px;
    }

    .custom-amt-input {
      border: none;
      background: transparent;
      outline: none;
      font-size: 17px;
      font-weight: 800;
      color: var(--text-heading);
      width: 100%;
    }

    .terms-agreement-row {
      display: flex;
      align-items: flex-start;
      gap: 8px;
      margin-bottom: 20px;
      font-size: 12.5px;
      color: var(--text-muted);
      line-height: 1.45;
    }

    .terms-agreement-row input[type="checkbox"] {
      width: 17px;
      height: 17px;
      accent-color: var(--primary);
      margin-top: 2px;
      cursor: pointer;
    }

    .terms-agreement-row a {
      color: var(--primary);
      text-decoration: underline;
      font-weight: 700;
      cursor: pointer;
    }

    .btn-submit-action {
      width: 100%;
      height: 50px;
      background: linear-gradient(135deg, #059669 0%, #047857 100%);
      color: #ffffff;
      border: none;
      border-radius: var(--radius-md);
      font-family: 'Outfit', sans-serif;
      font-size: 16px;
      font-weight: 800;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      cursor: pointer;
      box-shadow: var(--shadow-lg);
      transition: all 0.2s ease;
    }

    .btn-submit-action:hover {
      background: linear-gradient(135deg, #047857 0%, #064e3b 100%);
      transform: translateY(-1px);
    }

    .btn-submit-action:active {
      transform: scale(0.99);
    }

    /* Security Guarantee Notice */
    .security-guarantee-card {
      margin-top: 18px;
      background: var(--primary-soft);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 14px;
    }

    .sec-badge-header {
      display: flex;
      align-items: center;
      gap: 7px;
      font-size: 12px;
      font-weight: 800;
      color: var(--primary);
      margin-bottom: 6px;
    }

    .sec-desc-text {
      font-size: 12px;
      color: var(--text-body);
      line-height: 1.5;
    }

    /* ==========================================================================
       PROBLEM - SOLUTION - RESULT COMPARISON SECTION (Left Column)
       ========================================================================== */
    .comp-section-wrap {
      margin-bottom: 24px;
    }

    .comp-sec-heading {
      font-family: 'Outfit', sans-serif;
      font-size: 22px;
      font-weight: 900;
      color: var(--text-heading);
      margin-bottom: 4px;
      letter-spacing: -0.3px;
    }

    .comp-sec-subheading {
      font-size: 13.5px;
      color: var(--text-muted);
      margin-bottom: 16px;
    }

    .comparison-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-lg);
      padding: 18px;
      margin-bottom: 14px;
      position: relative;
      box-shadow: var(--shadow-sm);
      transition: border-color 0.2s ease, transform 0.2s ease;
    }

    .comparison-card:hover {
      border-color: var(--border-focus);
    }

    .comp-top-badge-row {
      display: flex;
      justify-content: flex-end;
      margin-bottom: 10px;
    }

    .tag-badge {
      font-size: 10.5px;
      font-weight: 800;
      text-transform: uppercase;
      letter-spacing: 0.8px;
      padding: 3px 10px;
      border-radius: 12px;
      display: inline-flex;
      align-items: center;
      gap: 4px;
    }

    .tag-badge.red {
      background: var(--accent-red-soft);
      color: var(--accent-red);
      border: 1px solid rgba(225, 29, 72, 0.2);
    }

    .tag-badge.green {
      background: var(--primary-soft);
      color: var(--primary);
      border: 1px solid rgba(16, 185, 129, 0.2);
    }

    .tag-badge.blue {
      background: var(--accent-blue-soft);
      color: var(--accent-blue);
      border: 1px solid rgba(2, 132, 199, 0.2);
    }

    .comp-icon-wrapper {
      width: 42px;
      height: 42px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      margin-bottom: 12px;
    }

    .comp-icon-wrapper.red {
      background: var(--accent-red-soft);
      color: var(--accent-red);
    }

    .comp-icon-wrapper.green {
      background: var(--primary);
      color: #ffffff;
      box-shadow: 0 4px 12px var(--primary-glow);
    }

    .comp-icon-wrapper.blue {
      background: var(--accent-blue);
      color: #ffffff;
      box-shadow: 0 4px 12px rgba(2, 132, 199, 0.25);
    }

    .comp-item-title {
      font-family: 'Outfit', sans-serif;
      font-size: 17px;
      font-weight: 800;
      color: var(--text-heading);
      margin-bottom: 6px;
    }

    .comp-item-desc {
      font-size: 13.5px;
      color: var(--text-body);
      line-height: 1.6;
    }

    /* ==========================================================================
       ISLAMIC TESTIMONIAL & QURANIC INSPIRATION CARDS
       ========================================================================== */
    .testimonial-banner-card {
      background: var(--banner-gradient);
      border-radius: var(--radius-lg);
      padding: 22px 18px;
      color: #ffffff;
      box-shadow: var(--shadow-md);
      margin-bottom: 20px;
      position: relative;
    }

    .quote-mark {
      font-size: 28px;
      color: #34d399;
      line-height: 1;
      margin-bottom: 10px;
      opacity: 0.9;
    }

    .testimonial-body-text {
      font-size: 14px;
      line-height: 1.65;
      font-weight: 500;
      margin-bottom: 12px;
      color: #f0fdf4;
    }

    .testimonial-author-tag {
      font-size: 12.5px;
      font-weight: 700;
      color: #a7f3d0;
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .quran-ayah-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-lg);
      padding: 22px 18px;
      text-align: center;
      box-shadow: var(--shadow-sm);
      margin-bottom: 20px;
    }

    .quran-icon-round {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      margin: 0 auto 14px;
    }

    .arabic-ayah-text {
      font-family: 'Amiri', serif;
      font-size: 18px;
      line-height: 1.85;
      color: var(--primary);
      direction: rtl;
      margin-bottom: 12px;
    }

    .quran-translation-text {
      font-size: 14px;
      line-height: 1.6;
      color: var(--text-heading);
      font-style: italic;
      margin-bottom: 10px;
    }

    .quran-reference-badge {
      font-size: 12.5px;
      font-weight: 800;
      color: var(--accent-gold);
    }

    /* ==========================================================================
       FREQUENTLY ASKED QUESTIONS (FAQ) ACCORDION
       ========================================================================== */
    .faq-wrapper {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-lg);
      padding: 22px 18px;
      box-shadow: var(--shadow-sm);
    }

    .faq-title {
      font-family: 'Outfit', sans-serif;
      font-size: 19px;
      font-weight: 900;
      color: var(--text-heading);
      margin-bottom: 16px;
    }

    .faq-row-item {
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      margin-bottom: 10px;
      overflow: hidden;
      background: var(--bg-card);
      transition: border-color 0.2s ease;
    }

    .faq-row-item:hover {
      border-color: var(--primary);
    }

    .faq-query-btn {
      padding: 14px 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 10px;
      font-size: 14px;
      font-weight: 700;
      color: var(--text-heading);
      cursor: pointer;
      user-select: none;
    }

    .faq-chevron {
      font-size: 12px;
      color: var(--text-muted);
      transition: transform 0.25s ease;
    }

    .faq-row-item.active .faq-chevron {
      transform: rotate(180deg);
      color: var(--primary);
    }

    .faq-response-body {
      display: none;
      padding: 0 16px 14px;
      font-size: 13px;
      color: var(--text-body);
      line-height: 1.6;
      border-top: 1px solid var(--border-color);
      padding-top: 12px;
    }

    .faq-row-item.active .faq-response-body {
      display: block;
    }

    /* ==========================================================================
       FLOATING WHATSAPP FAB
       ========================================================================== */
    .whatsapp-fab-button {
      position: fixed;
      bottom: 24px;
      right: 24px;
      width: 52px;
      height: 52px;
      background: #25d366;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #ffffff;
      font-size: 28px;
      box-shadow: 0 8px 24px rgba(37, 211, 102, 0.4);
      z-index: 50;
      text-decoration: none;
      transition: transform 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
    }

    .whatsapp-fab-button:hover {
      transform: scale(1.1);
    }

    /* ==========================================================================
       REALISTIC PAYMENT MODAL / DRAWER
       ========================================================================== */
    .payment-backdrop-layer {
      display: none;
      position: fixed;
      inset: 0;
      background: rgba(15, 23, 42, 0.7);
      backdrop-filter: blur(5px);
      z-index: 100;
      align-items: flex-end;
      justify-content: center;
      padding: 0;
    }

    @media (min-width: 768px) {
      .payment-backdrop-layer {
        align-items: center;
        padding: 20px;
      }
    }

    .payment-modal-box {
      background: var(--bg-card);
      width: 100%;
      max-width: 520px;
      border-radius: 24px 24px 0 0;
      padding: 24px 20px;
      max-height: 90vh;
      overflow-y: auto;
      border: 1px solid var(--border-color);
      box-shadow: var(--shadow-lg);
      animation: slideUpAnimation 0.25s cubic-bezier(0.16, 1, 0.3, 1);
    }

    @media (min-width: 768px) {
      .payment-modal-box {
        border-radius: var(--radius-xl);
        animation: popModalAnimation 0.2s cubic-bezier(0.16, 1, 0.3, 1);
      }
    }

    @keyframes slideUpAnimation {
      from { transform: translateY(100%); opacity: 0; }
      to { transform: translateY(0); opacity: 1; }
    }

    @keyframes popModalAnimation {
      from { transform: scale(0.95); opacity: 0; }
      to { transform: scale(1); opacity: 1; }
    }

    .pm-top-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 16px;
      padding-bottom: 10px;
      border-bottom: 1px solid var(--border-color);
    }

    .pm-modal-title {
      font-family: 'Outfit', sans-serif;
      font-size: 18px;
      font-weight: 800;
      color: var(--text-heading);
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .pm-close-btn {
      background: var(--bg-input);
      border: 1px solid var(--border-color);
      width: 32px;
      height: 32px;
      border-radius: 50%;
      font-size: 15px;
      color: var(--text-heading);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: background 0.2s ease;
    }

    .pm-close-btn:hover {
      background: var(--border-color);
    }

    /* ==========================================================================
       VIBRANT, ANIMATED PAYMENT METHOD TABS (bKash, Nagad, Rocket, Bank, Binance, PayPal)
       ========================================================================== */
    .method-tab-strip {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 10px;
      margin-bottom: 16px;
    }

    @media (min-width: 520px) {
      .method-tab-strip {
        grid-template-columns: repeat(3, 1fr);
      }
    }

    .method-tab-item {
      padding: 8px 4px 6px;
      min-height: 58px;
      border: 1.5px solid var(--border-color);
      border-radius: var(--radius-md);
      background: var(--bg-card);
      cursor: pointer;
      text-align: center;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 4px;
      user-select: none;
      transition: all 0.25s ease;
    }

    .method-tab-item:hover {
      transform: translateY(-2px);
    }

    .method-tab-item:active {
      transform: scale(0.97);
    }

    .pm-logo-badge {
      width: 24px;
      height: 24px;
      border-radius: 6px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s ease;
      flex-shrink: 0;
    }

    .method-tab-item:hover .pm-logo-badge {
      transform: scale(1.05);
    }

    .pm-brand-svg {
      width: 15px;
      height: 15px;
      max-width: 15px;
      max-height: 15px;
      display: block;
    }

    .method-tab-name {
      font-size: 11px;
      font-weight: 700;
      letter-spacing: -0.2px;
      line-height: 1.1;
      transition: color 0.2s ease;
    }

    .method-active-pill {
      position: absolute;
      top: 4px;
      right: 4px;
      width: 14px;
      height: 14px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.95);
      color: #059669;
      font-size: 8px;
      display: none;
      align-items: center;
      justify-content: center;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
    }

    .method-tab-item.active .method-active-pill {
      display: flex;
      animation: popPill 0.28s cubic-bezier(0.34, 1.56, 0.64, 1);
    }

    @keyframes popPill {
      0% { transform: scale(0); opacity: 0; }
      100% { transform: scale(1); opacity: 1; }
    }

    /* 1. bKash Brand Styling */
    .method-tab-item.brand-bkash {
      background: rgba(226, 19, 110, 0.05);
      border-color: rgba(226, 19, 110, 0.25);
      color: #be1254;
    }
    .method-tab-item.brand-bkash .pm-logo-badge {
      background: rgba(226, 19, 110, 0.12);
      color: #e2136e;
    }
    .method-tab-item.brand-bkash:hover {
      border-color: #e2136e;
      box-shadow: 0 6px 18px rgba(226, 19, 110, 0.25);
    }
    .method-tab-item.brand-bkash.active {
      background: linear-gradient(135deg, #e2136e 0%, #b80f55 100%) !important;
      border-color: #e2136e !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(226, 19, 110, 0.45);
    }
    .method-tab-item.brand-bkash.active .pm-logo-badge {
      background: rgba(255, 255, 255, 0.22);
      color: #ffffff;
    }
    .method-tab-item.brand-bkash.active .method-active-pill {
      color: #e2136e;
    }

    /* 2. Nagad Brand Styling */
    .method-tab-item.brand-nagad {
      background: rgba(243, 112, 33, 0.05);
      border-color: rgba(243, 112, 33, 0.25);
      color: #c2410c;
    }
    .method-tab-item.brand-nagad .pm-logo-badge {
      background: rgba(243, 112, 33, 0.12);
      color: #f37021;
    }
    .method-tab-item.brand-nagad:hover {
      border-color: #ea1d25;
      box-shadow: 0 6px 18px rgba(234, 29, 37, 0.25);
    }
    .method-tab-item.brand-nagad.active {
      background: linear-gradient(135deg, #f7941d 0%, #ea1d25 100%) !important;
      border-color: #ea1d25 !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(234, 29, 37, 0.45);
    }
    .method-tab-item.brand-nagad.active .pm-logo-badge {
      background: rgba(255, 255, 255, 0.22);
      color: #ffffff;
    }
    .method-tab-item.brand-nagad.active .method-active-pill {
      color: #ea1d25;
    }

    /* 3. Rocket Brand Styling */
    .method-tab-item.brand-rocket {
      background: rgba(140, 52, 148, 0.05);
      border-color: rgba(140, 52, 148, 0.25);
      color: #7e22ce;
    }
    .method-tab-item.brand-rocket .pm-logo-badge {
      background: rgba(140, 52, 148, 0.12);
      color: #8c3494;
    }
    .method-tab-item.brand-rocket:hover {
      border-color: #8c3494;
      box-shadow: 0 6px 18px rgba(140, 52, 148, 0.25);
    }
    .method-tab-item.brand-rocket.active {
      background: linear-gradient(135deg, #8c3494 0%, #5f1c64 100%) !important;
      border-color: #8c3494 !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(140, 52, 148, 0.45);
    }
    .method-tab-item.brand-rocket.active .pm-logo-badge {
      background: rgba(255, 255, 255, 0.22);
      color: #ffffff;
    }
    .method-tab-item.brand-rocket.active .method-active-pill {
      color: #8c3494;
    }

    /* 4. Bank Brand Styling */
    .method-tab-item.brand-bank {
      background: rgba(5, 150, 105, 0.05);
      border-color: rgba(5, 150, 105, 0.25);
      color: #047857;
    }
    .method-tab-item.brand-bank .pm-logo-badge {
      background: rgba(5, 150, 105, 0.12);
      color: #059669;
    }
    .method-tab-item.brand-bank:hover {
      border-color: #059669;
      box-shadow: 0 6px 18px rgba(5, 150, 105, 0.25);
    }
    .method-tab-item.brand-bank.active {
      background: linear-gradient(135deg, #059669 0%, #047857 100%) !important;
      border-color: #059669 !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(5, 150, 105, 0.45);
    }
    .method-tab-item.brand-bank.active .pm-logo-badge {
      background: rgba(255, 255, 255, 0.22);
      color: #ffffff;
    }
    .method-tab-item.brand-bank.active .method-active-pill {
      color: #059669;
    }

    /* 5. Binance Brand Styling (Iconic Yellow with Dark High Contrast) */
    .method-tab-item.brand-binance {
      background: rgba(240, 185, 11, 0.09);
      border-color: rgba(240, 185, 11, 0.38);
      color: #92400e;
    }
    .method-tab-item.brand-binance .pm-logo-badge {
      background: rgba(240, 185, 11, 0.18);
      color: #d97706;
    }
    .method-tab-item.brand-binance:hover {
      border-color: #F0B90B;
      box-shadow: 0 6px 18px rgba(240, 185, 11, 0.35);
    }
    .method-tab-item.brand-binance.active {
      background: linear-gradient(135deg, #F0B90B 0%, #E2A700 100%) !important;
      border-color: #F0B90B !important;
      color: #181a20 !important;
      box-shadow: 0 8px 24px rgba(240, 185, 11, 0.5);
    }
    .method-tab-item.brand-binance.active .pm-logo-badge {
      background: rgba(24, 26, 32, 0.12);
      color: #181a20;
    }
    .method-tab-item.brand-binance.active .method-active-pill {
      background: #181a20;
      color: #F0B90B;
    }

    /* 6. PayPal Brand Styling (Iconic Dual Blue) */
    .method-tab-item.brand-paypal {
      background: rgba(0, 121, 193, 0.05);
      border-color: rgba(0, 121, 193, 0.25);
      color: #0369a1;
    }
    .method-tab-item.brand-paypal .pm-logo-badge {
      background: rgba(0, 121, 193, 0.12);
      color: #0079C1;
    }
    .method-tab-item.brand-paypal:hover {
      border-color: #0079C1;
      box-shadow: 0 6px 18px rgba(0, 121, 193, 0.25);
    }
    .method-tab-item.brand-paypal.active {
      background: linear-gradient(135deg, #0079C1 0%, #00457C 100%) !important;
      border-color: #0079C1 !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(0, 121, 193, 0.45);
    }
    .method-tab-item.brand-paypal.active .pm-logo-badge {
      background: rgba(255, 255, 255, 0.22);
      color: #ffffff;
    }
    .method-tab-item.brand-paypal.active .method-active-pill {
      color: #0079C1;
    }

    /* 7. Other Gateway */
    .method-tab-item.brand-other {
      background: rgba(100, 116, 139, 0.05);
      border-color: rgba(100, 116, 139, 0.25);
      color: #475569;
    }
    .method-tab-item.brand-other.active {
      background: linear-gradient(135deg, #475569 0%, #334155 100%) !important;
      border-color: #475569 !important;
      color: #ffffff !important;
      box-shadow: 0 8px 24px rgba(71, 85, 105, 0.45);
    }

    /* ==========================================================================
       DYNAMIC THEMED ACCOUNT DETAILS PANEL
       ========================================================================== */
    .account-details-panel {
      background: var(--bg-card);
      border: 1.5px solid var(--border-color);
      border-radius: var(--radius-lg);
      padding: 16px;
      margin-bottom: 18px;
      transition: all 0.28s ease;
      box-shadow: var(--shadow-sm);
    }

    .account-details-panel.theme-bkash {
      border-color: rgba(226, 19, 110, 0.35);
      background: linear-gradient(135deg, rgba(226, 19, 110, 0.03), rgba(226, 19, 110, 0.07));
    }
    .account-details-panel.theme-nagad {
      border-color: rgba(234, 29, 37, 0.35);
      background: linear-gradient(135deg, rgba(243, 112, 33, 0.03), rgba(234, 29, 37, 0.07));
    }
    .account-details-panel.theme-rocket {
      border-color: rgba(140, 52, 148, 0.35);
      background: linear-gradient(135deg, rgba(140, 52, 148, 0.03), rgba(140, 52, 148, 0.07));
    }
    .account-details-panel.theme-bank {
      border-color: rgba(5, 150, 105, 0.35);
      background: linear-gradient(135deg, rgba(5, 150, 105, 0.03), rgba(5, 150, 105, 0.07));
    }
    .account-details-panel.theme-binance {
      border-color: rgba(240, 185, 11, 0.45);
      background: linear-gradient(135deg, rgba(240, 185, 11, 0.05), rgba(240, 185, 11, 0.1));
    }
    .account-details-panel.theme-paypal {
      border-color: rgba(0, 121, 193, 0.35);
      background: linear-gradient(135deg, rgba(0, 121, 193, 0.03), rgba(0, 121, 193, 0.07));
    }

    .acc-info-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 10px;
      margin-bottom: 8px;
    }

    .acc-method-pill {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 4px 10px;
      border-radius: 16px;
      font-size: 12px;
      font-weight: 800;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    .acc-mini-logo {
      width: 14px;
      height: 14px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }

    .acc-mini-logo .pm-brand-svg {
      width: 13px;
      height: 13px;
      max-width: 13px;
      max-height: 13px;
    }

    .acc-type-badge {
      font-size: 10.5px;
      font-weight: 700;
      padding: 2px 8px;
      border-radius: 10px;
      background: rgba(0, 0, 0, 0.06);
      color: var(--text-heading);
    }

    .acc-instructions-text {
      font-size: 12.5px;
      color: var(--text-body);
      line-height: 1.5;
      margin-bottom: 10px;
    }

    .acc-number-text {
      font-family: 'Outfit', monospace;
      font-size: 17px;
      font-weight: 900;
      letter-spacing: 0.5px;
    }

    .account-copy-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: var(--bg-card);
      border: 1.5px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 10px 14px;
      margin-top: 4px;
      gap: 10px;
      box-shadow: var(--shadow-sm);
    }

    .copy-action-btn {
      color: #ffffff;
      border: none;
      border-radius: 8px;
      padding: 8px 14px;
      font-size: 12px;
      font-weight: 800;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      white-space: nowrap;
      transition: all 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }

    .copy-action-btn:hover {
      transform: translateY(-2px);
      filter: brightness(1.1);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
    }

    .copy-action-btn:active {
      transform: scale(0.96);
    }

    .paypal-me-btn {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 8px 14px;
      background: #eff6ff;
      color: #0079C1;
      border: 1.5px solid #bfdbfe;
      border-radius: 10px;
      font-size: 12.5px;
      font-weight: 800;
      text-decoration: none;
      margin-top: 8px;
      transition: all 0.2s ease;
    }

    .paypal-me-btn:hover {
      background: #dbeafe;
      transform: translateY(-1px);
    }

    /* ==========================================================================
       COLORFUL, EYE-CATCHING SUBMISSION FORM STYLES
       ========================================================================== */
    .donation-vibrant-form {
      margin-top: 4px;
    }

    .form-field-card {
      background: var(--bg-card);
      border: 1.5px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 9px 12px;
      margin-bottom: 9px;
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .form-field-card:focus-within {
      border-color: var(--primary);
      box-shadow: 0 4px 14px var(--primary-glow);
      transform: translateY(-1px);
    }

    .form-field-card.field-accent-amber { border-left: 3.5px solid #f59e0b; }
    .form-field-card.field-accent-blue  { border-left: 3.5px solid #0284c7; }
    .form-field-card.field-accent-emerald { border-left: 3.5px solid #10b981; }
    .form-field-card.field-accent-purple { border-left: 3.5px solid #8b5cf6; }
    .form-field-card.field-accent-teal { border-left: 3.5px solid #0d9488; }

    .form-field-lbl {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 5px;
      cursor: pointer;
    }

    .field-icon-badge {
      width: 20px;
      height: 20px;
      border-radius: 5px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 10px;
      flex-shrink: 0;
    }

    .field-icon-badge.badge-amber   { background: #fef3c7; color: #d97706; }
    .field-icon-badge.badge-blue    { background: #e0f2fe; color: #0284c7; }
    .field-icon-badge.badge-emerald { background: #ecfdf5; color: #059669; }
    .field-icon-badge.badge-purple  { background: #f3e8ff; color: #7e22ce; }
    .field-icon-badge.badge-teal    { background: #ccfbf1; color: #0f766e; }

    .field-lbl-title {
      flex: 1;
    }

    .form-field-input {
      width: 100%;
      height: 40px;
      border: 1.5px solid var(--border-color);
      border-radius: 8px;
      padding: 0 11px;
      font-size: 13.5px;
      font-weight: 600;
      color: var(--text-heading);
      background: var(--bg-input);
      outline: none;
      transition: all 0.2s ease;
    }

    .form-field-input:focus {
      border-color: var(--border-focus);
      background: var(--bg-card);
      box-shadow: 0 0 0 3px var(--primary-glow);
    }

    .form-field-input.input-trx {
      font-family: 'Outfit', monospace;
      font-size: 14px;
      font-weight: 800;
      letter-spacing: 0.5px;
    }

    .anonymous-choice-card {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: var(--bg-input);
      border: 1.5px dashed var(--border-color);
      border-radius: 8px;
      padding: 8px 12px;
      margin-bottom: 9px;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .anonymous-choice-card:hover {
      border-color: var(--primary);
      background: var(--primary-soft);
    }

    .anon-icon-badge {
      width: 24px;
      height: 24px;
      border-radius: 6px;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
    }

    .custom-checkbox-round {
      width: 18px;
      height: 18px;
      accent-color: var(--primary);
      cursor: pointer;
    }

    .captcha-styled-badge {
      background: linear-gradient(135deg, var(--bg-card), var(--bg-input));
      border: 1.5px solid var(--border-color);
      border-radius: 8px;
      padding: 7px 12px;
      font-family: 'Outfit', monospace;
      font-size: 16px;
      font-weight: 900;
      color: var(--primary-dark);
      letter-spacing: 2px;
      user-select: none;
      min-width: 100px;
      text-align: center;
      box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    .btn-refresh-captcha {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      background: var(--bg-input);
      border: 1px solid var(--border-color);
      border-radius: 6px;
      padding: 3px 8px;
      font-size: 11px;
      font-weight: 700;
      color: var(--text-muted);
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .btn-refresh-captcha:hover {
      border-color: var(--primary);
      color: var(--primary);
    }

    .btn-vibrant-submit {
      width: 100%;
      height: 46px;
      background: linear-gradient(135deg, #059669 0%, #10b981 50%, #047857 100%);
      background-size: 200% auto;
      color: #ffffff;
      border: none;
      border-radius: 14px;
      font-family: 'Outfit', sans-serif;
      font-size: 16px;
      font-weight: 800;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      cursor: pointer;
      box-shadow: 0 8px 24px rgba(16, 185, 129, 0.4);
      position: relative;
      overflow: hidden;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    }

    .btn-vibrant-submit:hover {
      background-position: right center;
      transform: translateY(-2px);
      box-shadow: 0 12px 28px rgba(16, 185, 129, 0.5);
    }

    .btn-vibrant-submit:active {
      transform: scale(0.98);
    }

    .btn-submit-shine {
      position: absolute;
      top: 0;
      left: -100%;
      width: 60%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.35), transparent);
      transform: skewX(-20deg);
      animation: btnShineSweep 3.5s infinite;
    }

    @keyframes btnShineSweep {
      0% { left: -100%; }
      25% { left: 140%; }
      100% { left: 140%; }
    }

    /* Toast Notification */
    .toast-popup {
      position: fixed;
      bottom: 80px;
      left: 50%;
      transform: translateX(-50%) translateY(20px);
      background: #0f172a;
      color: #ffffff;
      padding: 9px 18px;
      border-radius: 20px;
      font-size: 12.5px;
      font-weight: 600;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
      z-index: 200;
      opacity: 0;
      pointer-events: none;
      transition: all 0.25s ease;
    }

    .toast-popup.show {
      opacity: 1;
      transform: translateX(-50%) translateY(0);
    }

    /* Legal Policy Modal */
    .legal-modal-backdrop {
      display: none;
      position: fixed;
      inset: 0;
      background: rgba(15, 23, 42, 0.7);
      backdrop-filter: blur(4px);
      z-index: 150;
      align-items: center;
      justify-content: center;
      padding: 20px;
    }

    .legal-modal-card {
      background: var(--bg-card);
      width: 100%;
      max-width: 540px;
      border-radius: var(--radius-lg);
      padding: 24px;
      border: 1px solid var(--border-color);
      box-shadow: var(--shadow-lg);
      max-height: 80vh;
      overflow-y: auto;
    }
  </style>
</head>
<body>

<div class="page-wrapper">

  <!-- =========================================================================
       HERO & IMPACT STATISTICS (Compact & Refined)
       ========================================================================= -->
  <section class="hero-banner">
    <h1 class="hero-title">
      <?php echo t($settings['hero_title_en'] ?? 'Contribute Everyday, Support the Journey of Millions of Muslims', $settings['hero_title_bn'] ?? 'প্রতিদিন দান করুন, কোটি মুসলিমের দ্বীনি যাত্রায় সঙ্গী হোন'); ?>
    </h1>
    <p class="hero-subtitle">
      <?php echo t($settings['hero_subtitle_en'] ?? 'Your daily contribution will help more Muslims engage in Ibadah every day', $settings['hero_subtitle_bn'] ?? 'আপনার ক্ষুদ্র অনুদান দীন ওয়ান-কে কোটি মানুষের কাছে পৌঁছে দিতে সহায়তা করবে'); ?>
    </p>

    <!-- Impact Badge & Grid -->
    <div class="impact-header-tag">
      <i class="fa-solid fa-chart-line"></i>
      <span><?php echo t("LAST YEAR'S IMPACT", "গত বছরের সামগ্রিক প্রভাব"); ?></span>
    </div>

    <div class="impact-grid">
      <!-- Users -->
      <div class="impact-card">
        <div class="impact-val"><?php echo htmlspecialchars($usersCount); ?></div>
        <div class="impact-lbl"><?php echo t('Users', 'ব্যবহারকারী'); ?></div>
      </div>
      <!-- Years of Usage -->
      <div class="impact-card">
        <div class="impact-val"><?php echo htmlspecialchars($yearsUsage); ?></div>
        <div class="impact-lbl"><?php echo t('Years of Usage', 'ইবাদত সেবা সময়'); ?></div>
      </div>
      <!-- Countries -->
      <div class="impact-card">
        <div class="impact-val"><?php echo htmlspecialchars($countries); ?></div>
        <div class="impact-lbl"><?php echo t('Countries', 'দেশসমূহ'); ?></div>
      </div>
    </div>
  </section>

  <!-- =========================================================================
       MAIN TWO-COLUMN RESPONSIVE LAYOUT (PC & MOBILE)
       ========================================================================= -->
  <div class="main-grid">

    <!-- LEFT COLUMN: Problem-Solution Comparison, Testimonial, Quran, FAQs -->
    <div class="left-content-col">

      <!-- 1. Problem - Solution - Result Comparison -->
      <section class="comp-section-wrap">
        <h2 class="comp-sec-heading">
          <?php echo t('Strong Faith. A Better Life.', 'মজবুত ঈমান, সুন্দর জীবন।'); ?>
        </h2>
        <p class="comp-sec-subheading">
          <?php echo t('How DeenOne Shapes the Life of a Believer', 'দীন ওয়ান যেভাবে একজন মুমিনের জীবনকে বরকতময় করে'); ?>
        </p>

        <!-- Card 1: Problem -->
        <div class="comparison-card">
          <div class="comp-top-badge-row">
            <span class="tag-badge red">
              • <?php echo t($settings['sec_problem_badge_en'] ?? 'PROBLEM', $settings['sec_problem_badge_bn'] ?? 'সমস্যা'); ?>
            </span>
          </div>
          <div class="comp-icon-wrapper red">
            <i class="fa-regular fa-face-frown"></i>
          </div>
          <h3 class="comp-item-title">
            <?php echo t($settings['sec_problem_title_en'] ?? 'Missing Deeds', $settings['sec_problem_title_bn'] ?? 'দ্বীনি আমলের ঘাটতি ও অসচেতনতা'); ?>
          </h3>
          <p class="comp-item-desc">
            <?php echo renderBBCode(t($settings['sec_problem_desc_en'] ?? '', $settings['sec_problem_desc_bn'] ?? '')); ?>
          </p>
        </div>

        <!-- Card 2: Solution (DeenOne) -->
        <div class="comparison-card">
          <div class="comp-top-badge-row">
            <span class="tag-badge green">
              • <?php echo t($settings['sec_solution_badge_en'] ?? 'SOLUTION', $settings['sec_solution_badge_bn'] ?? 'সমাধান'); ?>
            </span>
          </div>
          <div class="comp-icon-wrapper green">
            <i class="fa-solid fa-shield-halved"></i>
          </div>
          <h3 class="comp-item-title">
            <?php echo t($settings['sec_solution_title_en'] ?? 'DeenOne', $settings['sec_solution_title_bn'] ?? 'দীন ওয়ান (DeenOne)'); ?>
          </h3>
          <p class="comp-item-desc">
            <?php echo renderBBCode(t($settings['sec_solution_desc_en'] ?? '', $settings['sec_solution_desc_bn'] ?? '')); ?>
          </p>
        </div>

        <!-- Card 3: Result -->
        <div class="comparison-card">
          <div class="comp-top-badge-row">
            <span class="tag-badge blue">
              • <?php echo t($settings['sec_result_badge_en'] ?? 'RESULT', $settings['sec_result_badge_bn'] ?? 'ফলাফল'); ?>
            </span>
          </div>
          <div class="comp-icon-wrapper blue">
            <i class="fa-regular fa-face-smile"></i>
          </div>
          <h3 class="comp-item-title">
            <?php echo t($settings['sec_result_title_en'] ?? 'Better Tomorrow', $settings['sec_result_title_bn'] ?? 'সুন্দর ও বরকতময় ভবিষ্যৎ'); ?>
          </h3>
          <p class="comp-item-desc">
            <?php echo renderBBCode(t($settings['sec_result_desc_en'] ?? '', $settings['sec_result_desc_bn'] ?? '')); ?>
          </p>
        </div>
      </section>

      <!-- 2. Testimonial Box -->
      <div class="testimonial-banner-card">
        <div class="quote-mark">
          <i class="fa-solid fa-quote-left"></i>
        </div>
        <div class="testimonial-body-text">
          "<?php echo htmlspecialchars(t($settings['testimonial_quote_en'] ?? '', $settings['testimonial_quote_bn'] ?? '')); ?>"
        </div>
        <div class="testimonial-author-tag">
          <i class="fa-regular fa-circle-user"></i>
          <span><?php echo htmlspecialchars(t($settings['testimonial_author_en'] ?? 'Md. Mahbub Alam, DeenOne App User', $settings['testimonial_author_bn'] ?? 'মোঃ মাহবুব আলম, দীন ওয়ান ব্যবহারকারী')); ?></span>
        </div>
      </div>

      <!-- 3. Quran Inspiration Card -->
      <div class="quran-ayah-card">
        <div class="quran-icon-round">
          <i class="fa-solid fa-book-quran"></i>
        </div>
        <?php if (!empty($settings['quran_verse_ar'])): ?>
          <div class="arabic-ayah-text">
            <?php echo htmlspecialchars($settings['quran_verse_ar']); ?>
          </div>
        <?php endif; ?>
        <div class="quran-translation-text">
          "<?php echo htmlspecialchars(t($settings['quran_verse_en'] ?? '', $settings['quran_verse_bn'] ?? '')); ?>"
        </div>
        <div class="quran-reference-badge">
          — <?php echo htmlspecialchars(t($settings['quran_verse_ref_en'] ?? 'Surah Al-Baqarah, 2:261', $settings['quran_verse_ref_bn'] ?? 'সূরা আল-বাক্বারাহ, ২:২৬১')); ?>
        </div>
      </div>

      <!-- 4. FAQs -->
      <section class="faq-wrapper">
        <h2 class="faq-title"><?php echo t('Frequently Asked Questions', 'সাধারণ জিজ্ঞাসাসমূহ'); ?></h2>

        <?php foreach ($faqs as $idx => $faq): ?>
          <div class="faq-row-item">
            <div class="faq-query-btn" onclick="toggleFaqAccordion(this.closest('.faq-row-item'))">
              <span><?php echo htmlspecialchars(t($faq['question_en'] ?? '', $faq['question_bn'] ?? '')); ?></span>
              <i class="fa-solid fa-chevron-down faq-chevron"></i>
            </div>
            <div class="faq-response-body">
              <?php echo renderBBCode(t($faq['answer_en'] ?? '', $faq['answer_bn'] ?? '')); ?>
            </div>
          </div>
        <?php endforeach; ?>
      </section>

    </div>

    <!-- RIGHT COLUMN: Choose Amount, Payment Triggers & Security Guarantee -->
    <div class="right-action-col desktop-sticky-col">

      <!-- Donation Box -->
      <div class="donation-box">
        <div class="amount-header-row">
          <span class="amount-heading"><?php echo t('Choose Amount', 'অনুদানের পরিমাণ নির্বাচন করুন'); ?></span>
          
          <!-- Currency Switcher -->
          <div class="currency-switch-btn" onclick="toggleCurrency()" title="<?php echo t('Switch Currency', 'মুদ্রা পরিবর্তন করুন'); ?>">
            <span id="currentCurrencyLabel">BDT</span>
            <i class="fa-solid fa-chevron-down" style="font-size: 11px;"></i>
          </div>
        </div>

        <!-- Chips Grid -->
        <div class="chips-grid-layout" id="chipsContainer">
          <!-- Populated dynamically via JS -->
        </div>

        <!-- Custom Amount Field -->
        <div class="custom-amt-group">
          <label class="custom-amt-label"><?php echo t('Or enter custom amount', 'অথবা নিজের ইচ্ছেমতো পরিমাণ লিখুন'); ?></label>
          <div class="custom-amt-field">
            <span class="curr-sym-prefix" id="currencySymbolPrefix">৳</span>
            <input type="number" id="customAmountInput" class="custom-amt-input" value="200" min="1" oninput="onCustomAmountChange(this.value)">
          </div>
        </div>

        <!-- Terms Checkbox with Active Modals -->
        <div class="terms-agreement-row">
          <input type="checkbox" id="acceptTermsCheckbox" checked>
          <label for="acceptTermsCheckbox">
            <?php echo t('I accept the <a href="javascript:void(0)" onclick="openLegalModal(\'terms\')">T&C</a> and <a href="javascript:void(0)" onclick="openLegalModal(\'privacy\')">Privacy Policy</a>', 'আমি <a href="javascript:void(0)" onclick="openLegalModal(\'terms\')">শর্তাবলী</a> এবং <a href="javascript:void(0)" onclick="openLegalModal(\'privacy\')">গোপনীয়তা নীতি</a> মেনে দান করছি'); ?>
          </label>
        </div>

        <!-- Main Donate Trigger Button -->
        <button type="button" class="btn-submit-action" id="btnMainDonate" onclick="openPaymentDrawer()">
          <i class="fa-solid fa-heart"></i>
          <span><?php echo t('Donate', 'অনুদান দিন'); ?></span>
        </button>

        <!-- Security Guarantee Notice -->
        <div class="security-guarantee-card">
          <div class="sec-badge-header">
            <i class="fa-solid fa-lock"></i>
            <span><?php echo t('100% Sincere, Transparent & Secure', '১০০% নিরাপদ ও স্বচ্ছ পেমেন্ট প্রসেসিং'); ?></span>
          </div>
          <div class="sec-desc-text">
            <?php echo t(
              'This is a voluntary contribution. Your contribution directly supports verified authentic Islamic content research, high-speed cloud servers, and continuous app maintenance without annoying commercial ads.',
              'এটি একটি ঐচ্ছিক সদকাহ ও অনুদান। আপনার অনুদান সরাসরি কোটি মুসলিমের কাছে বিজ্ঞাপনমুক্ত বিশুদ্ধ দ্বীনি সেবা পৌঁছে দেওয়ার কাজে ব্যবহৃত হয়।'
            ); ?>
          </div>
        </div>

      </div>

    </div>

  </div>

</div>

<!-- Floating WhatsApp Support Button -->
<a href="https://wa.me/<?php echo $whatsappNumber; ?>?text=<?php echo urlencode('আসসালামু আলাইকুম, দীন ওয়ান অ্যাপে ডোনেশন সংক্রান্ত বিষয়ে জানতে চাই।'); ?>" target="_blank" class="whatsapp-fab-button" title="WhatsApp Support">
  <i class="fa-brands fa-whatsapp"></i>
</a>

<!-- =========================================================================
     PAYMENT DRAWER / MODAL
     ========================================================================= -->
<div class="payment-backdrop-layer" id="paymentModalBackdrop">
  <div class="payment-modal-box">
    
    <div class="pm-top-header">
      <div class="pm-modal-title">
        <i class="fa-solid fa-hand-holding-heart" style="color: var(--primary);"></i>
        <span><?php echo t('Donation Details', 'অনুদান তথ্য'); ?></span>
      </div>
      <button type="button" class="pm-close-btn" onclick="closePaymentDrawer()">&times;</button>
    </div>

    <!-- Selected Amount Banner -->
    <div style="background: var(--banner-gradient); border-radius: var(--radius-md); padding: 10px 14px; color: #ffffff; display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px;">
      <div>
        <div style="font-size: 11px; text-transform: uppercase; letter-spacing: 0.5px; color: #a7f3d0; font-weight: 700;"><?php echo t('Amount', 'পরিমাণ'); ?></div>
        <div style="font-family: 'Outfit', sans-serif; font-size: 20px; font-weight: 900;" id="modalSelectedAmountDisplay">৳200 BDT</div>
      </div>
      <div style="text-align: right; font-size: 12px; color: #d1fae5;">
        <i class="fa-solid fa-shield-halved"></i> <?php echo t('May Allah reward you', 'আল্লাহ কবুল করুন'); ?>
      </div>
    </div>

    <!-- Dynamic Payment Method Tabs -->
    <div class="method-tab-strip" id="paymentMethodTabsContainer">
      <!-- Injected via JavaScript based on enabled methods in Admin Settings -->
    </div>

    <!-- Account Details Info Box -->
    <div class="account-details-panel" id="accountInfoBox">
      <!-- Populated via JavaScript -->
    </div>

    <!-- Submission Form -->
    <form id="donationSubmissionForm" onsubmit="handleDonationSubmit(event)" class="donation-vibrant-form">
      <input type="hidden" name="action" value="submit_donation">
      <input type="hidden" name="amount" id="formInputAmount" value="200">
      <input type="hidden" name="currency" id="formInputCurrency" value="BDT">
      <input type="hidden" name="payment_method" id="formInputMethod" value="BKASH">
      <input type="hidden" name="lang" value="<?php echo $lang; ?>">

      <!-- Field 1: Transaction ID -->
      <div class="form-field-card field-accent-amber">
        <label class="form-field-lbl" for="inputTrxId">
          <span class="field-icon-badge badge-amber"><i class="fa-solid fa-receipt"></i></span>
          <span class="field-lbl-title"><?php echo t('Transaction ID *', 'ট্রানজেকশন আইডি *'); ?></span>
        </label>
        <input type="text" name="transaction_id" id="inputTrxId" class="form-field-input input-trx" placeholder="<?php echo t('e.g. 9K8J7H6G5F', 'যেমন: 9K8J7H6G5F'); ?>" required autocomplete="off">
      </div>

      <!-- Field 2: Donor Name -->
      <div class="form-field-card field-accent-blue">
        <label class="form-field-lbl" for="inputDonorName">
          <span class="field-icon-badge badge-blue"><i class="fa-solid fa-user"></i></span>
          <span class="field-lbl-title"><?php echo t('Name (Optional)', 'নাম (ঐচ্ছিক)'); ?></span>
        </label>
        <input type="text" name="donor_name" id="inputDonorName" class="form-field-input" placeholder="<?php echo t('Your name', 'আপনার নাম'); ?>">
      </div>

      <!-- Field 3: Mobile or Email -->
      <div class="form-field-card field-accent-emerald">
        <label class="form-field-lbl" for="inputDonorPhone">
          <span class="field-icon-badge badge-emerald"><i class="fa-solid fa-phone"></i></span>
          <span class="field-lbl-title"><?php echo t('Mobile / Email (Optional)', 'মোবাইল / ইমেইল (ঐচ্ছিক)'); ?></span>
        </label>
        <input type="text" name="phone" id="inputDonorPhone" class="form-field-input" placeholder="<?php echo t('Phone or email', 'মোবাইল বা ইমেইল'); ?>">
      </div>

      <!-- Field 4: Special Note -->
      <div class="form-field-card field-accent-purple">
        <label class="form-field-lbl" for="inputDonorNote">
          <span class="field-icon-badge badge-purple"><i class="fa-solid fa-heart"></i></span>
          <span class="field-lbl-title"><?php echo t('Note (Optional)', 'মন্তব্য বা দোয়া (ঐচ্ছিক)'); ?></span>
        </label>
        <textarea name="donor_note" id="inputDonorNote" class="form-field-input" rows="2" style="height: auto; min-height: 44px; padding: 8px 12px; resize: vertical;" placeholder="<?php echo t('Write your note or prayer...', 'মন্তব্য বা দোয়া লিখুন...'); ?>"></textarea>
      </div>

      <!-- Anonymous Donation Option -->
      <label class="anonymous-choice-card" for="inputIsAnonymous">
        <div style="display: flex; align-items: center; gap: 8px;">
          <div class="anon-icon-badge"><i class="fa-solid fa-user-shield"></i></div>
          <div style="font-size: 13px; font-weight: 700; color: var(--text-heading);"><?php echo t('Keep donation anonymous', 'দান গোপন রাখুন'); ?></div>
        </div>
        <input type="checkbox" name="is_anonymous" id="inputIsAnonymous" value="1" class="custom-checkbox-round">
      </label>

      <!-- Anti-Spam Security Verification / Google reCAPTCHA -->
      <?php 
        $isGoogleRecaptcha = (($settings['google_recaptcha_enabled'] ?? '0') == '1') && !empty($settings['google_recaptcha_site_key']);
      ?>
      <?php if ($isGoogleRecaptcha): ?>
        <div class="form-field-group" style="display: flex; justify-content: center; margin-bottom: 10px;">
          <div class="g-recaptcha" data-sitekey="<?php echo htmlspecialchars($settings['google_recaptcha_site_key']); ?>"></div>
        </div>
      <?php endif; ?>

      <!-- Smart Anti-Spam Security Challenge -->
      <div class="form-field-card field-accent-teal" id="captchaFieldGroup">
        <label class="form-field-lbl" style="justify-content: space-between;">
          <span style="display: inline-flex; align-items: center; gap: 8px;">
            <span class="field-icon-badge badge-teal"><i class="fa-solid fa-shield-halved"></i></span>
            <span class="field-lbl-title"><?php echo t('Security Challenge *', 'নিরাপত্তা যাচাই *'); ?></span>
          </span>
          <button type="button" class="btn-refresh-captcha" onclick="refreshCaptchaChallenge()" title="<?php echo t('Refresh Challenge', 'নতুন ক্যাপচা'); ?>">
            <i class="fa-solid fa-arrows-rotate" id="captchaRefreshIcon"></i>
            <span><?php echo t('Refresh', 'নতুন'); ?></span>
          </button>
        </label>
        <div style="display: flex; gap: 10px; align-items: center; margin-top: 4px;">
          <div id="captchaChallengeBadge" class="captcha-styled-badge">
            ...
          </div>
          <input type="hidden" name="captcha_token" id="inputCaptchaToken" value="">
          <input type="text" name="captcha_answer" id="inputCaptchaAnswer" class="form-field-input" style="flex: 1; height: 40px; font-size: 15px; font-weight: 800; text-align: center;" placeholder="<?php echo t('Answer', 'উত্তর'); ?>" required autocomplete="off">
        </div>
      </div>

      <!-- Submit Button -->
      <button type="submit" class="btn-vibrant-submit" id="btnSubmitDonation">
        <span class="btn-submit-shine"></span>
        <i class="fa-solid fa-paper-plane" style="font-size: 15px;"></i>
        <span id="btnSubmitDonationText"><?php echo t('Submit Donation', 'সাবমিট করুন'); ?></span>
        <i class="fa-solid fa-chevron-right" style="font-size: 12px; opacity: 0.8;"></i>
      </button>
    </form>

  </div>
</div>

<!-- =========================================================================
     LEGAL POLICY MODAL (Privacy Policy & Terms)
     ========================================================================= -->
<div class="legal-modal-backdrop" id="legalModalBackdrop" onclick="if(event.target===this)closeLegalModal()">
  <div class="legal-modal-card">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid var(--border-color);">
      <h3 style="font-size: 17px; font-weight: 800; color: var(--text-heading);" id="legalModalTitle"></h3>
      <button type="button" class="pm-close-btn" onclick="closeLegalModal()">&times;</button>
    </div>
    <div style="font-size: 13.5px; line-height: 1.7; color: var(--text-body); white-space: pre-wrap;" id="legalModalContent"></div>
  </div>
</div>

<!-- Toast Popup -->
<div id="toastPopup" class="toast-popup"></div>

<script>
let currentCurrency = '<?php echo ($lang === "en") ? "USD" : "BDT"; ?>';
let selectedAmount = (currentCurrency === 'USD') ? 3 : 200;
let selectedMethod = 'BKASH';

const legalTexts = {
  privacy: {
    title: '<?php echo t("Privacy Policy", "গোপনীয়তা নীতি"); ?>',
    content: <?php echo json_encode($privacyPolicyContent, JSON_UNESCAPED_UNICODE); ?>
  },
  terms: {
    title: '<?php echo t("Terms & Conditions", "শর্তাবলী ও নিয়মাবলী"); ?>',
    content: <?php echo json_encode($termsServiceContent, JSON_UNESCAPED_UNICODE); ?>
  }
};

function openLegalModal(type) {
  const item = legalTexts[type] || legalTexts.privacy;
  document.getElementById('legalModalTitle').innerText = item.title;
  document.getElementById('legalModalContent').innerText = item.content;
  document.getElementById('legalModalBackdrop').style.display = 'flex';
}

function closeLegalModal() {
  document.getElementById('legalModalBackdrop').style.display = 'none';
}

const presetAmounts = {
  USD: <?php echo json_encode(json_decode($settings['preset_amounts_usd'] ?? '["2","3","5","10"]', true) ?: ["2", "3", "5", "10"]); ?>,
  BDT: <?php echo json_encode(json_decode($settings['preset_amounts_bdt'] ?? '["100","200","500","1000"]', true) ?: ["100", "200", "500", "1000"]); ?>
};

const paymentAccounts = {
  <?php if (($settings['bkash_enabled'] ?? '1') == '1'): ?>
  BKASH: {
    enabled: true,
    key: 'BKASH',
    tabName: '<?php echo t("bKash", "বিকাশ"); ?>',
    brandClass: 'brand-bkash',
    brandColor: '#e2136e',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><path d="M14 48 L44 32 L34 56 Z" fill="currentColor" opacity="0.95"/><path d="M44 32 L88 12 L60 52 Z" fill="currentColor"/><path d="M34 56 L60 52 L48 88 Z" fill="currentColor" opacity="0.92"/><path d="M60 52 L92 58 L72 78 Z" fill="currentColor" opacity="0.88"/><path d="M48 88 L66 94 L58 74 Z" fill="currentColor" opacity="0.82"/></svg>',
    title: '<?php echo t("bKash Account", "বিকাশ একাউন্ট"); ?>',
    number: '<?php echo htmlspecialchars($settings['bkash_number'] ?? '01700000000'); ?>',
    type: '<?php echo t("Personal / Send Money", "পার্সোনাল / সেন্ড মানি"); ?>',
    instructions: '<?php echo t($settings['bkash_instructions_en'] ?? "Send Money to the bKash number below and enter the Transaction ID in the form.", $settings['bkash_instructions_bn'] ?? "বিকাশ অ্যাপ থেকে নিচের নম্বরে সেন্ড মানি করুন এবং নিচে প্রাপ্ত ট্রানজেকশন আইডি লিখুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['nagad_enabled'] ?? '1') == '1'): ?>
  NAGAD: {
    enabled: true,
    key: 'NAGAD',
    tabName: '<?php echo t("Nagad", "নগদ"); ?>',
    brandClass: 'brand-nagad',
    brandColor: '#ea1d25',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><path d="M50 8C33 19 22 40 27 60c4 15 16 27 31 29 7 1 15-1 19-6 5-5 6-12 3-18-3-6-9-9-15-11-9-3-18-9-19-19-1-10 6-20 15-25 3-2 6-3 9-4-7-3-12-3-20-1z" fill="currentColor"/><circle cx="68" cy="34" r="14" fill="#ea1d25"/><path d="M44 48c2-9 9-16 18-17-2 10-10 18-18 17z" fill="#f7941d"/></svg>',
    title: '<?php echo t("Nagad Account", "নগদ একাউন্ট"); ?>',
    number: '<?php echo htmlspecialchars($settings['nagad_number'] ?? '01700000000'); ?>',
    type: '<?php echo t("Personal / Send Money", "পার্সোনাল / সেন্ড মানি"); ?>',
    instructions: '<?php echo t($settings['nagad_instructions_en'] ?? "Send Money to the Nagad number below and enter the Transaction ID in the form.", $settings['nagad_instructions_bn'] ?? "নগদ অ্যাপ থেকে নিচের নম্বরে সেন্ড মানি করুন এবং নিচে প্রাপ্ত ট্রানজেকশন আইডি লিখুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['rocket_enabled'] ?? '1') == '1'): ?>
  ROCKET: {
    enabled: true,
    key: 'ROCKET',
    tabName: '<?php echo t("Rocket", "রকেট"); ?>',
    brandClass: 'brand-rocket',
    brandColor: '#8c3494',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><path d="M68 12c-9 1-23 10-35 22-9 8-15 18-19 28-2 6-3 12-1 15 2 2 8 1 15-1 10-4 20-10 28-19 12-12 21-26 22-35 1-6-4-10-10-10z" fill="currentColor"/><circle cx="58" cy="38" r="7" fill="#ffffff" opacity="0.95"/><circle cx="58" cy="38" r="4" fill="#8c3494"/><path d="M22 66c-6 2-10 6-12 12-1 4 2 6 6 5 6-2 10-6 12-12-1-2-3-4-6-5z" fill="#f59e0b"/><path d="M38 78c-4 10-2 16 2 16 2 0 4-4 6-10-2-2-5-4-8-6z" fill="#ef4444"/><path d="M74 34c10-4 16-2 16 2 0 2-4 4-10 6-2-2-4-5-6-8z" fill="#d946ef"/></svg>',
    title: '<?php echo t("Rocket Account", "রকেট একাউন্ট"); ?>',
    number: '<?php echo htmlspecialchars($settings['rocket_number'] ?? '017000000000'); ?>',
    type: '<?php echo t("Personal", "পার্সোনাল"); ?>',
    instructions: '<?php echo t($settings['rocket_instructions_en'] ?? "Send money via Rocket and submit the Transaction ID in the form.", $settings['rocket_instructions_bn'] ?? "রকেট একাউন্টে টাকা পাঠিয়ে প্রাপ্ত ট্রানজেকশন আইডি নিচে লিখুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['bank_enabled'] ?? '1') == '1'): ?>
  BANK: {
    enabled: true,
    key: 'BANK',
    tabName: '<?php echo t("Bank", "ব্যাংক"); ?>',
    brandClass: 'brand-bank',
    brandColor: '#059669',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><path d="M50 12 L14 32 L14 38 L86 38 L86 32 Z" fill="currentColor"/><rect x="22" y="44" width="10" height="30" rx="2" fill="currentColor"/><rect x="38" y="44" width="10" height="30" rx="2" fill="currentColor"/><rect x="54" y="44" width="10" height="30" rx="2" fill="currentColor"/><rect x="70" y="44" width="10" height="30" rx="2" fill="currentColor"/><rect x="14" y="78" width="72" height="8" rx="2" fill="currentColor"/><polygon points="50,18 42,28 58,28" fill="#f59e0b"/></svg>',
    title: '<?php echo t("Bank Transfer", "ব্যাংক একাউন্ট"); ?>',
    bank_name: '<?php echo htmlspecialchars($settings['bank_name'] ?? 'Islami Bank Bangladesh PLC'); ?>',
    acc_name: '<?php echo htmlspecialchars($settings['bank_account_name'] ?? 'DeenOne'); ?>',
    number: '<?php echo htmlspecialchars($settings['bank_account_no'] ?? '2050123456789000'); ?>',
    branch: '<?php echo htmlspecialchars($settings['bank_branch'] ?? 'Dhanmondi Branch, Dhaka'); ?>',
    routing: '<?php echo htmlspecialchars($settings['bank_routing'] ?? '125271892'); ?>',
    swift: '<?php echo htmlspecialchars($settings['bank_swift'] ?? 'IBBLBDDH'); ?>',
    instructions: '<?php echo t($settings['bank_instructions_en'] ?? "Deposit or online transfer to the bank account below and enter transaction reference.", $settings['bank_instructions_bn'] ?? "ব্যাংক ট্রান্সফার বা ডিপোজিট করে রেফারেন্স স্লিপ নম্বর নিচে প্রদান করুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['binance_enabled'] ?? '1') == '1'): ?>
  BINANCE: {
    enabled: true,
    key: 'BINANCE',
    tabName: '<?php echo t("Binance Pay", "বাইনান্স পে"); ?>',
    brandClass: 'brand-binance',
    brandColor: '#d97706',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><polygon points="50,42 58,50 50,58 42,50" fill="currentColor"/><polygon points="50,14 62,26 50,38 38,26" fill="currentColor"/><polygon points="50,62 62,74 50,86 38,74" fill="currentColor"/><polygon points="26,38 38,50 26,62 14,50" fill="currentColor"/><polygon points="74,38 86,50 74,62 62,50" fill="currentColor"/></svg>',
    title: '<?php echo t("Binance Pay ID", "বাইনান্স পে আইডি"); ?>',
    binance_id: '<?php echo htmlspecialchars($settings['crypto_binance_pay_id'] ?? '88992211'); ?>',
    instructions: '<?php echo t($settings['binance_instructions_en'] ?? "Transfer via Binance Pay ID and submit the Pay Order ID / Reference in the form below.", $settings['binance_instructions_bn'] ?? "বাইনান্স অ্যাপ থেকে নিচের বাইন্যান্স পে আইডিতে সেন্ড করুন এবং প্রাপ্ত পে অর্ডার আইডি বা রেফারেন্স নিচে লিখুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['paypal_enabled'] ?? '1') == '1'): ?>
  PAYPAL: {
    enabled: true,
    key: 'PAYPAL',
    tabName: '<?php echo t("PayPal", "পেপ্যাল"); ?>',
    brandClass: 'brand-paypal',
    brandColor: '#0079C1',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><path d="M30 14h25c13 0 23 8 21 21-2 14-13 22-26 22h-9l-5 28h-15l14-71z" fill="#003087"/><path d="M41 26h24c13 0 22 8 20 21-2 14-13 22-26 22h-9l-5 26h-14l10-69z" fill="#0079C1"/><path d="M42 49l-3 18h9c11 0 19-7 21-18h-27z" fill="#00457c" opacity="0.6"/></svg>',
    title: '<?php echo t("PayPal Account", "পেপ্যাল একাউন্ট"); ?>',
    email: '<?php echo htmlspecialchars($settings['paypal_email'] ?? 'donate@deenone.top'); ?>',
    me_link: '<?php echo htmlspecialchars($settings['paypal_me_link'] ?? 'https://paypal.me/deenone'); ?>',
    instructions: '<?php echo t($settings['paypal_instructions_en'] ?? "Send donation via PayPal to our email or link and enter the Transaction ID.", $settings['paypal_instructions_bn'] ?? "নিচের পেপ্যাল অ্যাকাউন্টে অনুদান পাঠিয়ে প্রাপ্ত ট্রানজেকশন আইডি নিচে লিখুন।"); ?>'
  },
  <?php endif; ?>

  <?php if (($settings['custom_enabled'] ?? '0') == '1'): ?>
  OTHER: {
    enabled: true,
    key: 'OTHER',
    tabName: '<?php echo htmlspecialchars(t($settings['custom_title_en'] ?? 'Other', $settings['custom_title_bn'] ?? 'অন্যান্য')); ?>',
    brandClass: 'brand-other',
    brandColor: '#475569',
    logoSvg: '<svg viewBox="0 0 100 100" class="pm-brand-svg"><rect x="14" y="24" width="72" height="52" rx="8" fill="currentColor"/><rect x="14" y="36" width="72" height="10" fill="#1e293b"/><rect x="24" y="58" width="16" height="8" rx="2" fill="#f59e0b"/></svg>',
    title: '<?php echo htmlspecialchars(t($settings['custom_title_en'] ?? 'Other Gateway', $settings['custom_title_bn'] ?? 'অন্যান্য মাধ্যম')); ?>',
    details: '<?php echo htmlspecialchars(t($settings['custom_details_en'] ?? '', $settings['custom_details_bn'] ?? '')); ?>',
    instructions: '<?php echo t("Please follow the instructions and enter the payment reference.", "নিচের নির্দেশনা অনুসরণ করে পেমেন্ট রেফারেন্স আইডি লিখুন।"); ?>'
  }
  <?php endif; ?>
};

// Cache offline data in localStorage
try {
  localStorage.setItem('deenone_donation_cached_accounts', JSON.stringify(paymentAccounts));
  localStorage.setItem('deenone_donation_cached_rates', JSON.stringify({ usdRate: <?php echo $usdRate; ?>, presetAmounts }));
} catch(e) {}

document.addEventListener('DOMContentLoaded', function() {
  renderChips();
  renderMethodTabs();
  renderAccountBox();
  refreshCaptchaChallenge();

  // Bridge to Android local offline cache
  if (window.AndroidDonationBridge && typeof window.AndroidDonationBridge.savePageContent === 'function') {
    setTimeout(function() {
      try {
        window.AndroidDonationBridge.savePageContent(document.documentElement.outerHTML, '<?php echo $lang; ?>', '<?php echo $theme; ?>');
      } catch(e) {}
    }, 600);
  }
});

function toggleCurrency() {
  currentCurrency = (currentCurrency === 'USD') ? 'BDT' : 'USD';
  document.getElementById('currentCurrencyLabel').innerText = currentCurrency;
  document.getElementById('currencySymbolPrefix').innerText = (currentCurrency === 'USD') ? '$' : '৳';
  
  selectedAmount = (currentCurrency === 'USD') ? 3 : 200;
  document.getElementById('customAmountInput').value = selectedAmount;
  renderChips();
}

function renderChips() {
  const container = document.getElementById('chipsContainer');
  container.innerHTML = '';
  const list = presetAmounts[currentCurrency] || (currentCurrency === 'USD' ? ["2","3","5","10"] : ["100","200","500","1000"]);
  const symbol = (currentCurrency === 'USD') ? '$' : '৳';

  list.forEach(amt => {
    const chip = document.createElement('button');
    chip.type = 'button';
    chip.className = 'amt-chip' + (parseFloat(selectedAmount) === parseFloat(amt) ? ' active' : '');
    chip.innerText = symbol + amt;
    chip.onclick = function() {
      selectedAmount = parseFloat(amt);
      document.getElementById('customAmountInput').value = selectedAmount;
      renderChips();
    };
    container.appendChild(chip);
  });
}

function renderMethodTabs() {
  const container = document.getElementById('paymentMethodTabsContainer');
  container.innerHTML = '';
  const keys = Object.keys(paymentAccounts);

  if (keys.length === 0) {
    container.innerHTML = '<div style="font-size:12px;color:var(--text-muted);padding:10px;"><?php echo t("No active payment methods.", "কোনো পেমেন্ট মেথড সক্রিয় নেই।"); ?></div>';
    return;
  }

  if (!paymentAccounts[selectedMethod]) {
    selectedMethod = keys[0];
  }

  keys.forEach(key => {
    const item = paymentAccounts[key];
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.setAttribute('data-method', key);
    btn.className = `method-tab-item ${item.brandClass || ''}` + (selectedMethod === key ? ' active' : '');
    btn.innerHTML = `
      <div class="pm-logo-badge">
        ${item.logoSvg || `<i class="${item.icon || 'fa-solid fa-wallet'}"></i>`}
      </div>
      <span class="method-tab-name">${item.tabName || key}</span>
      <span class="method-active-pill"><i class="fa-solid fa-check"></i></span>
    `;
    btn.onclick = () => selectPaymentMethod(key);
    container.appendChild(btn);
  });

  document.getElementById('formInputMethod').value = selectedMethod;
}

function onCustomAmountChange(val) {
  selectedAmount = parseFloat(val) || 0;
  renderChips();
}

function toggleFaqAccordion(el) {
  if (!el) return;
  const isAlreadyActive = el.classList.contains('active');
  document.querySelectorAll('.faq-row-item.active').forEach(item => {
    if (item !== el) item.classList.remove('active');
  });
  if (isAlreadyActive) {
    el.classList.remove('active');
  } else {
    el.classList.add('active');
  }
}

function openPaymentDrawer() {
  const terms = document.getElementById('acceptTermsCheckbox').checked;
  if (!terms) {
    showToast('<?php echo t("Please accept the Terms & Conditions and Privacy Policy to proceed.", "অনুগ্রহ করে শর্তাবলী ও গোপনীয়তা নীতি টিক দিন।"); ?>');
    return;
  }

  if (selectedAmount <= 0) {
    showToast('<?php echo t("Please choose or enter a valid donation amount.", "অনুগ্রহ করে সঠিক অনুদানের পরিমাণ নির্বাচন করুন।"); ?>');
    return;
  }

  const symbol = (currentCurrency === 'USD') ? '$' : '৳';
  document.getElementById('modalSelectedAmountDisplay').innerText = symbol + selectedAmount + ' ' + currentCurrency;
  document.getElementById('formInputAmount').value = selectedAmount;
  document.getElementById('formInputCurrency').value = currentCurrency;

  document.getElementById('paymentModalBackdrop').style.display = 'flex';
  refreshCaptchaChallenge();
}

function closePaymentDrawer() {
  document.getElementById('paymentModalBackdrop').style.display = 'none';
}

function selectPaymentMethod(method) {
  selectedMethod = method;
  document.getElementById('formInputMethod').value = method;

  document.querySelectorAll('.method-tab-item').forEach(btn => {
    btn.classList.toggle('active', btn.getAttribute('data-method') === method);
  });

  renderAccountBox();
}

function renderAccountBox() {
  const box = document.getElementById('accountInfoBox');
  const info = paymentAccounts[selectedMethod];
  if (!info) {
    box.innerHTML = '';
    return;
  }

  // Dynamically update theme styling on box
  box.className = 'account-details-panel theme-' + selectedMethod.toLowerCase();

  if (selectedMethod === 'BKASH' || selectedMethod === 'NAGAD' || selectedMethod === 'ROCKET') {
    const brandColor = info.brandColor || 'var(--primary)';
    box.innerHTML = `
      <div class="acc-info-header">
        <div class="acc-method-pill" style="background:${info.brandColor}; color:#fff;">
          <span class="acc-mini-logo">${info.logoSvg}</span>
          <span>${info.title}</span>
        </div>
        <span class="acc-type-badge">${info.type || 'Personal'}</span>
      </div>
      <div class="acc-instructions-text">${info.instructions}</div>
      <div class="account-copy-item" style="border-color: ${brandColor}40;">
        <span class="acc-number-text" style="color: ${brandColor};">${info.number}</span>
        <button type="button" class="copy-action-btn" style="background: ${brandColor};" onclick="copyTextWithFeedback(this, '${info.number}')">
          <i class="fa-regular fa-copy"></i>
          <span><?php echo t('Copy', 'কপি করুন'); ?></span>
        </button>
      </div>
    `;
  } else if (selectedMethod === 'BANK') {
    const brandColor = '#059669';
    let swiftHtml = info.swift ? ` | SWIFT: <strong>${info.swift}</strong>` : '';
    box.innerHTML = `
      <div class="acc-info-header">
        <div class="acc-method-pill" style="background:${brandColor}; color:#fff;">
          <span class="acc-mini-logo">${info.logoSvg}</span>
          <span>${info.title}</span>
        </div>
        <span class="acc-type-badge" style="background:#ecfdf5;color:#047857;border:1px solid #a7f3d0;"><?php echo t('Bank Transfer', 'ব্যাংক ট্রান্সফার'); ?></span>
      </div>
      <div class="acc-instructions-text">${info.instructions}</div>
      <div style="font-size: 14px; font-weight: 800; color: #047857; margin-bottom: 2px;">${info.bank_name}</div>
      <div style="font-size: 12.5px; color: var(--text-heading); margin-bottom: 4px;"><strong><?php echo t('A/C Name:', 'হিসাবধারীর নাম:'); ?></strong> ${info.acc_name}</div>
      <div class="account-copy-item" style="border-color: ${brandColor}40;">
        <span class="acc-number-text" style="color: #047857;">${info.number}</span>
        <button type="button" class="copy-action-btn" style="background: ${brandColor};" onclick="copyTextWithFeedback(this, '${info.number}')">
          <i class="fa-regular fa-copy"></i>
          <span><?php echo t('Copy', 'কপি করুন'); ?></span>
        </button>
      </div>
      <div style="font-size: 12px; color: var(--text-muted); margin-top: 6px;">
        <span><strong><?php echo t('Branch:', 'শাখা:'); ?></strong> ${info.branch}</span> | 
        <span><strong><?php echo t('Routing:', 'রাউটিং:'); ?></strong> ${info.routing}</span>
        ${swiftHtml}
      </div>
    `;
  } else if (selectedMethod === 'BINANCE') {
    box.innerHTML = `
      <div class="acc-info-header">
        <div class="acc-method-pill" style="background:#F0B90B; color:#181a20; font-weight:900;">
          <span class="acc-mini-logo" style="color:#181a20;">${info.logoSvg}</span>
          <span>${info.title}</span>
        </div>
        <span class="acc-type-badge" style="background:#fef3c7;color:#b45309;border:1px solid #fde68a;">Binance Pay</span>
      </div>
      <div class="acc-instructions-text">${info.instructions}</div>
      <div class="account-copy-item" style="border-color: #F0B90B80; background: rgba(240, 185, 11, 0.08);">
        <span class="acc-number-text" style="color: #b45309;">${info.binance_id}</span>
        <button type="button" class="copy-action-btn" style="background: #F0B90B; color: #181a20; font-weight: 800;" onclick="copyTextWithFeedback(this, '${info.binance_id}')">
          <i class="fa-regular fa-copy"></i>
          <span><?php echo t('Copy', 'কপি করুন'); ?></span>
        </button>
      </div>
    `;
  } else if (selectedMethod === 'PAYPAL') {
    let meBtn = info.me_link ? `
      <a href="${info.me_link}" target="_blank" rel="noopener noreferrer" class="paypal-me-btn">
        <i class="fa-brands fa-paypal"></i>
        <span><?php echo t('Open PayPal Link ↗', 'পেপাল লিংক ওপেন করুন ↗'); ?></span>
      </a>
    ` : '';
    box.innerHTML = `
      <div class="acc-info-header">
        <div class="acc-method-pill" style="background:#0079C1; color:#fff;">
          <span class="acc-mini-logo">${info.logoSvg}</span>
          <span>${info.title}</span>
        </div>
        <span class="acc-type-badge" style="background:#eff6ff;color:#1d4ed8;border:1px solid #bfdbfe;">PayPal Official</span>
      </div>
      <div class="acc-instructions-text">${info.instructions}</div>
      <div class="account-copy-item" style="border-color: #0079C140;">
        <span class="acc-number-text" style="color: #0079C1; font-size: 14.5px;">${info.email}</span>
        <button type="button" class="copy-action-btn" style="background: #0079C1;" onclick="copyTextWithFeedback(this, '${info.email}')">
          <i class="fa-regular fa-copy"></i>
          <span><?php echo t('Copy', 'কপি করুন'); ?></span>
        </button>
      </div>
      ${meBtn}
    `;
  } else if (selectedMethod === 'OTHER') {
    box.innerHTML = `
      <div class="acc-info-header">
        <div class="acc-method-pill" style="background:#475569; color:#fff;">
          <span class="acc-mini-logo">${info.logoSvg}</span>
          <span>${info.title}</span>
        </div>
      </div>
      <div class="acc-instructions-text">${info.instructions}</div>
      <div style="font-size: 13.5px; color: var(--text-body); white-space: pre-wrap; line-height: 1.6; margin-top: 6px;">${info.details}</div>
    `;
  }
}

function showToast(msg) {
  const toast = document.getElementById('toastPopup');
  toast.innerText = msg;
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3000);
}

function copyText(str) {
  copyTextWithFeedback(null, str);
}

function copyTextWithFeedback(btn, text) {
  if (navigator.clipboard && navigator.clipboard.writeText) {
    navigator.clipboard.writeText(text).then(() => {
      onCopySuccess(btn);
    }).catch(() => {
      fallbackCopy(btn, text);
    });
  } else {
    fallbackCopy(btn, text);
  }
}

function fallbackCopy(btn, text) {
  const ta = document.createElement('textarea');
  ta.value = text;
  document.body.appendChild(ta);
  ta.select();
  document.execCommand('copy');
  document.body.removeChild(ta);
  onCopySuccess(btn);
}

function onCopySuccess(btn) {
  showToast('<?php echo t("Account details copied to clipboard!", "ক্লিপবোর্ডে কপি করা হয়েছে!"); ?>');
  if (btn) {
    const originalHtml = btn.innerHTML;
    btn.innerHTML = `<i class="fa-solid fa-check"></i> <span><?php echo t("Copied!", "কপি হয়েছে!"); ?></span>`;
    btn.style.filter = 'brightness(1.15)';
    setTimeout(() => {
      btn.innerHTML = originalHtml;
      btn.style.filter = '';
    }, 2000);
  }
}

async function refreshCaptchaChallenge() {
  const icon = document.getElementById('captchaRefreshIcon');
  if (icon) icon.classList.add('fa-spin');
  try {
    const res = await fetch('../api/donation.php?action=get_captcha&lang=<?php echo $lang; ?>');
    const data = await res.json();
    if (data.success) {
      document.getElementById('captchaChallengeBadge').innerText = data.question;
      document.getElementById('inputCaptchaToken').value = data.token;
      document.getElementById('inputCaptchaAnswer').value = '';
    }
  } catch (err) {
    console.error('Captcha load error:', err);
  } finally {
    if (icon) icon.classList.remove('fa-spin');
  }
}

async function handleDonationSubmit(e) {
  e.preventDefault();
  const btn = document.getElementById('btnSubmitDonation');
  const btnText = document.getElementById('btnSubmitDonationText');
  const original = btnText.innerText;

  btnText.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> <?php echo t("Processing...", "প্রসেসিং হচ্ছে..."); ?>';
  btn.disabled = true;

  const fd = new FormData(document.getElementById('donationSubmissionForm'));

  try {
    const res = await fetch('../api/donation.php', { method: 'POST', body: fd });
    const data = await res.json();

    if (data.success) {
      closePaymentDrawer();
      showToast('✓ ' + (data.message || data.message_en) + ' (Ref: ' + data.reference_code + ')');
      document.getElementById('donationSubmissionForm').reset();
      refreshCaptchaChallenge();
      if (typeof grecaptcha !== 'undefined') {
        try { grecaptcha.reset(); } catch(e) {}
      }
    } else {
      showToast('<?php echo t("Error: ", "ত্রুটি: "); ?>' + (data.error || '<?php echo t("Submission failed.", "জমা দেওয়া সম্ভব হয়নি।"); ?>'));
      refreshCaptchaChallenge();
      if (typeof grecaptcha !== 'undefined') {
        try { grecaptcha.reset(); } catch(e) {}
      }
    }
  } catch (err) {
    console.error(err);
    showToast('<?php echo t("Server request failed. Please check internet connection.", "সার্ভার রিকোয়েস্ট ব্যর্থ হয়েছে। ইন্টারনেট সংযোগ পরীক্ষা করুন।"); ?>');
  } finally {
    btnText.innerText = original;
    btn.disabled = false;
  }
}
</script>

</body>
</html>
