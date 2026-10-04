<?php
/**
 * ==============================================================================
 * DEEN ONE - OFFICIAL LANDING PAGE & PORTAL
 * Serving Humanity Through Authentic Islamic Knowledge & Technology
 * ==============================================================================
 */

// Load configuration and DB if available
$configPath = __DIR__ . '/config.php';
if (file_exists($configPath)) {
    require_once $configPath;
}

if (!defined('SITE_URL')) {
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? 'https://' : 'http://';
    $host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
    define('SITE_URL', $protocol . $host . '/');
}

header('Cache-Control: public, max-age=3600, stale-while-revalidate=86400');

// Language detection (Default 'bn' as per project rules; supports 'en')
$lang = strtolower(trim($_GET['lang'] ?? ($_COOKIE['deenone_lang'] ?? 'bn')));
if (!in_array($lang, ['en', 'bn'])) {
    $lang = 'bn';
}

// Theme detection (Default 'light' with persistent dark mode toggle)
$theme = strtolower(trim($_GET['theme'] ?? ($_COOKIE['deenone_theme'] ?? 'light')));
if (!in_array($theme, ['dark', 'light'])) {
    $theme = 'light';
}

// Dynamic registered user count or fallback
$totalUsersDisplay = '1+';
$hadithCountDisplay = '+';
$hajjStagesDisplay = '12';
$surahsDisplay = '114';

// Attempt DB query gracefully if PDO is available
$settings = [];
try {
    $dbPath = __DIR__ . '/api/db.php';
    if (file_exists($dbPath)) {
        require_once $dbPath;
        if (function_exists('getDbConnection')) {
            $pdo = getDbConnection();
            if ($pdo) {
                $userCount = (int)$pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
                if ($userCount > 1000) {
                    $totalUsersDisplay = number_format($userCount) . '+';
                }
                $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
                while ($row = $stmt->fetch()) {
                    $settings[$row['setting_key']] = $row['setting_value'];
                }
            }
        }
    }
} catch (Exception $e) {
    // Graceful fallback to static numbers
}

// Configurable URLs & Assets with robust fallbacks
$siteFavicon = !empty($settings['site_favicon_url']) ? $settings['site_favicon_url'] : (SITE_URL . 'uploads/deenone_icon.webp');
if (!preg_match('~^https?://~i', $siteFavicon) && !str_starts_with($siteFavicon, '/')) {
    $siteFavicon = SITE_URL . $siteFavicon;
}

$landingLogo = !empty($settings['landing_logo_url']) ? $settings['landing_logo_url'] : (!empty($settings['app_logo_url']) ? $settings['app_logo_url'] : (SITE_URL . 'uploads/deenone_icon.webp'));
if (!preg_match('~^https?://~i', $landingLogo) && !str_starts_with($landingLogo, '/')) {
    $landingLogo = SITE_URL . $landingLogo;
}

$playStoreUrl = !empty($settings['update_url']) ? $settings['update_url'] : 'https://play.google.com/store/apps/details?id=com.devflux.deenone';
$apkDownloadUrl = !empty($settings['apk_download_url']) ? $settings['apk_download_url'] : (SITE_URL . 'downloads/deenone.apk');
if (!preg_match('~^https?://~i', $apkDownloadUrl) && !str_starts_with($apkDownloadUrl, '/')) {
    $apkDownloadUrl = SITE_URL . $apkDownloadUrl;
}

$socialFacebook = !empty($settings['social_facebook_url']) ? $settings['social_facebook_url'] : 'https://www.facebook.com/deenone.official';
$socialTelegram = !empty($settings['social_telegram_url']) ? $settings['social_telegram_url'] : 'https://t.me/deenone';
$socialYoutube = !empty($settings['social_youtube_url']) ? $settings['social_youtube_url'] : 'https://youtube.com/@deenone';
$socialGithub = !empty($settings['social_github_url']) ? $settings['social_github_url'] : 'https://github.com/riadmonir/DeenOne';

$siteNameBn = !empty($settings['app_name']) ? $settings['app_name'] : 'দীনওয়ান';
$siteNameEn = !empty($settings['app_name_en']) ? $settings['app_name_en'] : 'DeenOne';
$siteTaglineBn = !empty($settings['app_tagline']) ? $settings['app_tagline'] : 'প্রযুক্তি ও খাঁটি ইসলামিক জ্ঞানের সমন্বয়';
$siteTaglineEn = !empty($settings['app_tagline_en']) ? $settings['app_tagline_en'] : 'Serving Humanity Through Authentic Islamic Knowledge & Technology';
$siteMetaDesc = !empty($settings['site_meta_description']) ? $settings['site_meta_description'] : 'DeenOne provides modern, 100% ad-free Islamic apps and open knowledge platforms including Authentic Hadith, Interactive Visual Hajj Guide, Quran, Prayer Times, and Emergency Blood Network.';

// Dynamic Multi-Language Typography Suite synced with Admin Settings (Default: Roboto & Kalpurush)
$selectedEnFontKey = $settings['admin_font_family'] ?? 'roboto';
$selectedBnFontKey = $settings['admin_bangla_font'] ?? 'kalpurush';
$selectedArFontKey = $settings['admin_arabic_font'] ?? 'amiri';

$englishFontMap = [
    'roboto' => "'Roboto', sans-serif",
    'inter' => "'Inter', sans-serif",
    'plus_jakarta_sans' => "'Plus Jakarta Sans', sans-serif",
    'poppins' => "'Poppins', sans-serif",
    'outfit' => "'Outfit', sans-serif",
    'arial' => "Arial, Helvetica, sans-serif",
];

$banglaFontMap = [
    'kalpurush' => "'Kalpurush', 'SolaimanLipi', 'Noto Sans Bengali', sans-serif",
    'noto_sans_bengali' => "'Noto Sans Bengali', sans-serif",
    'hind_siliguri' => "'Hind Siliguri', sans-serif",
    'anek_bangla' => "'Anek Bangla', sans-serif",
    'noto_serif_bengali' => "'Noto Serif Bengali', serif",
];

$arabicFontMap = [
    'amiri' => "'Amiri', serif",
    'scheherazade' => "'Scheherazade New', 'Amiri', serif",
];

$landingFontEnCss = $englishFontMap[$selectedEnFontKey] ?? $englishFontMap['roboto'];
$landingFontBnCss = $banglaFontMap[$selectedBnFontKey] ?? $banglaFontMap['kalpurush'];
$landingFontArCss = $arabicFontMap[$selectedArFontKey] ?? $arabicFontMap['amiri'];

// Helper for initial server-side text
function t($bn, $en) {
    global $lang;
    return $lang === 'en' ? $en : $bn;
}
?>
<!DOCTYPE html>
<html lang="<?php echo $lang; ?>" data-theme="<?php echo $theme; ?>" data-lang="<?php echo $lang; ?>">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">
  <title><?php echo htmlspecialchars(t($siteNameBn . ' - ' . $siteTaglineBn, $siteNameEn . ' - ' . $siteTaglineEn)); ?></title>
  <meta name="description" content="<?php echo htmlspecialchars($siteMetaDesc); ?>">
  <meta name="keywords" content="DeenOne, Islamic App, Sahih Hadith, Hajj Guide, Quran Mazid, Prayer Times, Azan, Dua & Ruqyah, Blood Donation, Islamic Knowledge">
  <meta name="author" content="DeenOne">
  
  <!-- Open Graph / Facebook / WhatsApp -->
  <meta property="og:type" content="website">
  <meta property="og:url" content="<?php echo SITE_URL; ?>">
  <meta property="og:title" content="<?php echo htmlspecialchars(t($siteNameBn . ' - ' . $siteTaglineBn, $siteNameEn . ' - ' . $siteTaglineEn)); ?>">
  <meta property="og:description" content="<?php echo htmlspecialchars($siteMetaDesc); ?>">
  <meta property="og:image" content="<?php echo htmlspecialchars($landingLogo); ?>">

  <!-- Favicon -->
  <link rel="icon" href="<?php echo htmlspecialchars($siteFavicon); ?>">
  <link rel="apple-touch-icon" href="<?php echo htmlspecialchars($siteFavicon); ?>">

  <!-- Client-Side Instant Font & Theme Pre-Render Initialization -->
  <script>
    (function() {
      const enMap = {
        'roboto': "'Roboto', sans-serif",
        'inter': "'Inter', sans-serif",
        'plus_jakarta_sans': "'Plus Jakarta Sans', sans-serif",
        'poppins': "'Poppins', sans-serif",
        'outfit': "'Outfit', sans-serif",
        'arial': "Arial, Helvetica, sans-serif"
      };
      const bnMap = {
        'kalpurush': "'Kalpurush', 'SolaimanLipi', 'Noto Sans Bengali', sans-serif",
        'hind_siliguri': "'Hind Siliguri', sans-serif",
        'noto_sans_bengali': "'Noto Sans Bengali', sans-serif",
        'anek_bangla': "'Anek Bangla', sans-serif",
        'noto_serif_bengali': "'Noto Serif Bengali', serif"
      };
      const arMap = {
        'amiri': "'Amiri', serif",
        'scheherazade': "'Scheherazade New', 'Amiri', serif"
      };
      const savedEn = localStorage.getItem('deenone_admin_font');
      if (savedEn && enMap[savedEn]) {
        document.documentElement.style.setProperty('--font-en', enMap[savedEn]);
      }
      const savedBn = localStorage.getItem('deenone_admin_bangla_font');
      if (savedBn && bnMap[savedBn]) {
        document.documentElement.style.setProperty('--font-bn', bnMap[savedBn]);
      }
      const savedAr = localStorage.getItem('deenone_admin_arabic_font');
      if (savedAr && arMap[savedAr]) {
        document.documentElement.style.setProperty('--font-arabic', arMap[savedAr]);
      }
    })();
  </script>

  <!-- DNS Prefetch & Preconnect for High Performance -->
  <link rel="dns-prefetch" href="https://fonts.googleapis.com">
  <link rel="dns-prefetch" href="https://fonts.gstatic.com">
  <link rel="dns-prefetch" href="https://cdnjs.cloudflare.com">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Amiri:wght@400;700&family=Inter:wght@400;500;600;700&family=Noto+Sans+Bengali:wght@400;500;600;700&family=Poppins:wght@400;500;600;700&family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

  <!-- FontAwesome 6 CDN -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

  <style>
    /* ==========================================================================
       LOCAL & CDN FONT-FACE: KALPURUSH (FOR BENGALI)
       ========================================================================== */
    @font-face {
      font-family: 'Kalpurush';
      font-style: normal;
      font-weight: 100 900;
      font-display: swap;
      src: url('<?php echo SITE_URL; ?>assets/fonts/kalpurush.woff2') format('woff2'),
           url('<?php echo SITE_URL; ?>assets/fonts/kalpurush.ttf') format('truetype'),
           url('https://fonts.maateen.me/kalpurush/Kalpurush-v0.258.woff2') format('woff2');
    }

    /* ==========================================================================
       DESIGN SYSTEM & THEME TOKENS (Matching IRD Aesthetic)
       ========================================================================== */
    :root {
      /* Brand Colors */
      --primary: #17B686;
      --primary-hover: #10AA7B;
      --primary-active: #0A5740;
      --primary-soft: #DDEEE7;
      --primary-glow: rgba(23, 182, 134, 0.22);
      
      --accent-gold: #F59E0B;
      --accent-blue: #0284C7;
      --accent-teal: #0D9488;
      --accent-red: #E11D48;

      /* Light Theme (IRD Default) */
      --bg-page: #FFFFFF;
      --bg-section: #F4FCF9;
      --bg-section-alt: #F5F9FD;
      --bg-card: #FFFFFF;
      --bg-card-hover: #FAFCFB;
      --bg-input: #F8FAFC;
      --border-color: #F1F1F1;
      --border-strong: #E2E8F0;
      --border-focus: #17B686;
      
      --text-heading: #374246;
      --text-body: #6D7B81;
      --text-muted: #9AAEC0;
      
      --arch-bg: #E7F0F8;
      --arch-glow: rgba(231, 240, 248, 0.6);
      --footer-bg: #EEF9F8;
      --footer-title: #374246;
      --download-badge-bg: #DDEEE7;
      --tag-badge-bg: #E7F0F8;
      --tag-badge-color: #374246;
      
      --nav-bg: rgba(255, 255, 255, 0.92);
      --nav-border: #F1F1F1;
      
      --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.04);
      --shadow-card: 0 10px 25px rgba(0, 0, 0, 0.04);
      --shadow-card-hover: 0 18px 40px rgba(0, 0, 0, 0.08);
      --shadow-mockup: 35px 54px 88px rgba(125, 149, 169, 0.18);
      
      --radius-sm: 8px;
      --radius-md: 12px;
      --radius-lg: 18px;
      --radius-xl: 24px;
      --radius-full: 9999px;
      
      /* Typography Tokens: Dynamically Synced with Admin Panel Settings */
      --font-bn: <?php echo $landingFontBnCss; ?>;
      --font-en: <?php echo $landingFontEnCss; ?>;
      --font-arabic: <?php echo $landingFontArCss; ?>;
    }

    /* Dark Mode (IRD Dark Navy Style) */
    html[data-theme="dark"] {
      --primary: #17B686;
      --primary-hover: #19C994;
      --primary-active: #129E73;
      --primary-soft: rgba(23, 182, 134, 0.16);
      --primary-glow: rgba(23, 182, 134, 0.35);

      --bg-page: #1A2039;
      --bg-section: #202742;
      --bg-section-alt: #242C48;
      --bg-card: #2E3655;
      --bg-card-hover: #353E61;
      --bg-input: #1D233D;
      --border-color: rgba(255, 255, 255, 0.08);
      --border-strong: rgba(255, 255, 255, 0.14);
      
      --text-heading: #FFFFFF;
      --text-body: #D3E1E7;
      --text-muted: #8187A0;
      
      --arch-bg: #2B3352;
      --arch-glow: rgba(43, 51, 82, 0.8);
      --footer-bg: #141A2E;
      --footer-title: #D3E1E7;
      --download-badge-bg: #2F3A62;
      --tag-badge-bg: #3C4669;
      --tag-badge-color: #FFFFFF;
      
      --nav-bg: rgba(32, 39, 66, 0.92);
      --nav-border: rgba(255, 255, 255, 0.08);
      
      --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.25);
      --shadow-card: 0 10px 25px rgba(0, 0, 0, 0.25);
      --shadow-card-hover: 0 18px 40px rgba(0, 0, 0, 0.35);
      --shadow-mockup: 35px 54px 88px rgba(0, 0, 0, 0.45);
    }

    /* Base Reset */
    *, *::before, *::after {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    html {
      scroll-behavior: smooth;
      font-size: 16px;
    }

    body {
      font-family: var(--font-bn);
      background-color: var(--bg-page);
      color: var(--text-body);
      line-height: 1.6;
      overflow-x: hidden;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      transition: background-color 0.3s ease, color 0.3s ease;
    }

    /* Bengali Mode Typography: Kalpurush */
    html[data-lang="bn"] body,
    html[data-lang="bn"] button,
    html[data-lang="bn"] input,
    html[data-lang="bn"] select,
    html[data-lang="bn"] textarea,
    html[data-lang="bn"] h1,
    html[data-lang="bn"] h2,
    html[data-lang="bn"] h3,
    html[data-lang="bn"] h4,
    html[data-lang="bn"] h5,
    html[data-lang="bn"] p,
    html[data-lang="bn"] span,
    html[data-lang="bn"] a,
    .bn-only {
      font-family: var(--font-bn);
    }

    /* English Mode Typography: Poppins */
    html[data-lang="en"] body,
    html[data-lang="en"] button,
    html[data-lang="en"] input,
    html[data-lang="en"] select,
    html[data-lang="en"] textarea,
    html[data-lang="en"] h1,
    html[data-lang="en"] h2,
    html[data-lang="en"] h3,
    html[data-lang="en"] h4,
    html[data-lang="en"] h5,
    html[data-lang="en"] p,
    html[data-lang="en"] span,
    html[data-lang="en"] a,
    .en-only {
      font-family: var(--font-en);
    }

    /* Brand Logo, Numbers and English labels */
    .brand-logo,
    .btn-download-pill .pill-meta,
    .stat-number,
    .badge-grade {
      font-family: var(--font-en);
    }

    a {
      color: inherit;
      text-decoration: none;
      transition: color 0.2s ease;
    }

    img {
      max-width: 100%;
      height: auto;
      display: block;
    }

    button {
      font-family: inherit;
      cursor: pointer;
      border: none;
      background: none;
      outline: none;
    }

    /* Container */
    .container {
      width: 100%;
      max-width: 1320px;
      margin-left: auto;
      margin-right: auto;
      padding-left: 20px;
      padding-right: 20px;
    }
    @media (min-width: 768px) {
      .container { padding-left: 36px; padding-right: 36px; }
    }
    @media (min-width: 1280px) {
      .container { padding-left: 24px; padding-right: 24px; }
    }

    /* ==========================================================================
       STICKY BLUR NAVBAR (Header)
       ========================================================================== */
    .navbar {
      position: sticky;
      top: 0;
      z-index: 1000;
      background: var(--nav-bg);
      -webkit-backdrop-filter: blur(14px);
      backdrop-filter: blur(14px);
      border-bottom: 1px solid var(--nav-border);
      padding: 14px 0;
      transition: background-color 0.3s ease, border-color 0.3s ease;
    }

    .nav-inner {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .brand-logo {
      display: inline-flex;
      align-items: center;
      gap: 0;
      font-size: 22px;
      font-weight: 800;
      color: var(--text-heading);
      letter-spacing: -0.5px;
      text-decoration: none;
      white-space: nowrap;
    }

    .brand-logo .brand-green {
      color: var(--primary);
      display: inline;
    }

    .brand-logo .brand-white {
      color: var(--text-heading);
      display: inline;
    }

    /* Nav Links */
    .nav-menu {
      display: none;
      align-items: center;
      gap: 24px;
      list-style: none;
    }
    @media (min-width: 1024px) {
      .nav-menu { display: flex; }
    }

    .nav-item {
      position: relative;
    }

    .nav-link {
      font-size: 15px;
      font-weight: 600;
      color: var(--text-heading);
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 6px 4px;
      transition: color 0.2s ease;
    }

    .nav-link:hover, .nav-link.active {
      color: var(--primary);
    }

    /* Dropdown */
    .nav-dropdown {
      position: absolute;
      top: 100%;
      left: -12px;
      min-width: 250px;
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      box-shadow: var(--shadow-card-hover);
      padding: 10px;
      display: none;
      flex-direction: column;
      gap: 4px;
      z-index: 1010;
      animation: fadeInDropdown 0.2s ease-out;
    }

    @keyframes fadeInDropdown {
      from { opacity: 0; transform: translateY(6px); }
      to { opacity: 1; transform: translateY(0); }
    }

    .nav-item:hover .nav-dropdown {
      display: flex;
    }

    .dropdown-link {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 9px 12px;
      border-radius: var(--radius-sm);
      font-size: 14px;
      font-weight: 600;
      color: var(--text-heading);
      transition: background-color 0.15s ease, color 0.15s ease;
    }

    .dropdown-link:hover {
      background: var(--primary-soft);
      color: var(--primary);
    }

    .dropdown-icon {
      width: 28px;
      height: 28px;
      border-radius: 6px;
      background: var(--bg-input);
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--primary);
      font-size: 13px;
    }

    /* Nav Actions Right */
    .nav-actions {
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .social-links {
      display: none;
      align-items: center;
      gap: 8px;
      margin-right: 6px;
    }
    @media (min-width: 1280px) {
      .social-links { display: flex; }
    }

    .social-icon-btn {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--text-muted);
      font-size: 14px;
      background: var(--bg-input);
      border: 1px solid var(--border-color);
      transition: all 0.2s ease;
    }

    .social-icon-btn:hover {
      color: var(--primary);
      border-color: var(--primary);
      transform: translateY(-2px);
    }

    /* Icon button for theme & lang */
    .btn-icon-toggle {
      width: 38px;
      height: 38px;
      border-radius: var(--radius-md);
      background: var(--bg-input);
      border: 1px solid var(--border-color);
      color: var(--text-heading);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 15px;
      font-weight: 700;
      transition: all 0.2s ease;
    }

    .btn-icon-toggle:hover {
      background: var(--primary-soft);
      color: var(--primary);
      border-color: var(--primary);
    }

    /* Support Us CTA Button */
    .btn-support-nav {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: var(--primary);
      color: #FFFFFF;
      font-size: 14.5px;
      font-weight: 700;
      padding: 9px 20px;
      border-radius: var(--radius-sm);
      transition: all 0.2s ease;
      box-shadow: 0 4px 14px var(--primary-glow);
    }

    .btn-support-nav:hover {
      background: var(--primary-hover);
      transform: translateY(-1px);
    }

    /* Mobile Responsive Tweaks for Header */
    @media (max-width: 640px) {
      .brand-logo { font-size: 17px; gap: 8px; }
      .brand-icon { width: 32px; height: 32px; }
      .nav-actions { gap: 6px; }
      .btn-icon-toggle { width: 34px; height: 34px; font-size: 13px; }
      .btn-support-nav { display: none; } /* In drawer & hero on mobile */
    }

    @media (max-width: 480px) {
      .container { padding-left: 12px; padding-right: 12px; }
      .brand-logo { font-size: 15px; gap: 6px; }
      .brand-icon { width: 28px; height: 28px; border-radius: 8px; }
      .nav-actions { gap: 5px; flex-shrink: 0; }
      .btn-icon-toggle { width: 30px; height: 30px; font-size: 11.5px; border-radius: 6px; }
      .btn-hamburger { width: 32px; height: 32px; font-size: 14px; border-radius: 6px; }
    }

    /* Mobile Hamburger */
    .btn-hamburger {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 36px;
      height: 36px;
      border-radius: var(--radius-md);
      background: var(--bg-input);
      border: 1px solid var(--border-color);
      color: var(--text-heading);
      font-size: 16px;
    }
    @media (min-width: 1024px) {
      .btn-hamburger { display: none; }
    }

    /* Mobile Drawer */
    .mobile-drawer {
      position: fixed;
      top: 0;
      right: -100%;
      width: 82%;
      max-width: 360px;
      height: 100vh;
      background: var(--bg-card);
      z-index: 2000;
      padding: 24px;
      box-shadow: -10px 0 30px rgba(0, 0, 0, 0.2);
      transition: right 0.3s cubic-bezier(0.16, 1, 0.3, 1);
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .mobile-drawer.open {
      right: 0;
    }

    .drawer-backdrop {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.5);
      z-index: 1999;
      display: none;
      backdrop-filter: blur(4px);
    }

    .drawer-backdrop.open {
      display: block;
    }

    .drawer-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 16px;
      border-bottom: 1px solid var(--border-color);
    }

    .drawer-links {
      display: flex;
      flex-direction: column;
      gap: 6px;
      list-style: none;
    }

    .drawer-link {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 14px;
      border-radius: var(--radius-md);
      font-size: 15px;
      font-weight: 700;
      color: var(--text-heading);
      background: var(--bg-input);
    }

    .drawer-link:hover {
      background: var(--primary-soft);
      color: var(--primary);
    }

    /* ==========================================================================
       HERO SECTION (Bold Typography & Floating 3D Mobile Displays)
       ========================================================================== */
    .hero-section {
      position: relative;
      padding-top: 36px;
      padding-bottom: 60px;
      overflow: hidden;
    }
    @media (min-width: 768px) {
      .hero-section { padding-top: 50px; padding-bottom: 90px; }
    }
    @media (min-width: 1280px) {
      .hero-section { padding-top: 70px; padding-bottom: 110px; }
    }

    .hero-grid {
      display: grid;
      grid-template-columns: 1fr;
      align-items: center;
      gap: 40px;
    }
    @media (min-width: 1024px) {
      .hero-grid {
        grid-template-columns: 1.15fr 0.85fr;
        gap: 40px;
      }
    }

    /* Eyebrow badge */
    .eyebrow-badge {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      font-weight: 700;
      color: var(--primary);
      background: var(--primary-soft);
      padding: 6px 14px;
      border-radius: var(--radius-full);
      margin-bottom: 18px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .hero-title {
      font-size: clamp(1.75rem, 3.2vw, 42px);
      font-weight: 700;
      line-height: 1.25;
      color: var(--text-heading);
      margin-bottom: 18px;
      letter-spacing: -0.3px;
    }

    .hero-subtitle {
      font-size: clamp(0.95rem, 1.15vw, 16px);
      color: var(--text-body);
      line-height: 1.7;
      margin-bottom: 28px;
      max-width: 620px;
      font-weight: 400;
    }

    /* Hero Checkmarks Row */
    .hero-features-list {
      display: grid;
      grid-template-columns: 1fr;
      gap: 12px;
      margin-bottom: 32px;
    }
    @media (min-width: 640px) {
      .hero-features-list {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .hero-feat-item {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 14.5px;
      font-weight: 600;
      color: var(--text-heading);
    }

    .feat-icon-star {
      width: 22px;
      height: 22px;
      border-radius: 50%;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 11px;
      flex-shrink: 0;
    }

    /* Hero CTA Row (Available on...) */
    .hero-cta-title {
      font-size: 14px;
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 14px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .hero-cta-group {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 14px;
    }

    /* Download Pills (Google Play & APK) */
    .btn-download-pill {
      display: inline-flex;
      align-items: center;
      gap: 12px;
      background: var(--download-badge-bg);
      color: var(--text-heading);
      padding: 10px 18px;
      border-radius: var(--radius-md);
      transition: all 0.25s ease;
      min-width: 170px;
    }

    .btn-download-pill:hover {
      box-shadow: var(--shadow-card);
      transform: translateY(-2px);
    }

    .btn-download-pill .pill-icon {
      font-size: 26px;
      color: var(--primary);
    }

    .btn-download-pill .pill-meta {
      display: flex;
      flex-direction: column;
      line-height: 1.15;
    }

    .btn-download-pill .pill-sub {
      font-size: 10px;
      text-transform: uppercase;
      color: var(--text-body);
      font-weight: 600;
    }

    .btn-download-pill .pill-title {
      font-size: 14px;
      font-weight: 800;
      color: var(--text-heading);
    }

    /* Hero Right Graphic with Mosque Arch */
    .hero-graphic-wrap {
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      max-width: 440px;
      margin: 0 auto;
    }

    .mosque-arch-bg {
      position: absolute;
      inset: -20px -20px 0 -20px;
      z-index: 1;
      display: flex;
      justify-content: center;
      pointer-events: none;
    }

    .mosque-arch-svg {
      width: 100%;
      height: 100%;
      max-height: 600px;
      fill: var(--arch-bg);
      transition: fill 0.3s ease;
    }

    /* Floating Phone Device */
    .phone-mockup-frame {
      position: relative;
      z-index: 10;
      width: 100%;
      max-width: 320px;
      background: var(--bg-card);
      border: 8px solid var(--text-heading);
      border-radius: 38px;
      box-shadow: var(--shadow-mockup);
      overflow: hidden;
      animation: gentleFloat 5s ease-in-out infinite;
      transition: all 0.3s ease;
    }

    @keyframes gentleFloat {
      0%, 100% { transform: translateY(0); }
      50% { transform: translateY(-12px); }
    }

    /* Screen inside Phone */
    .mockup-screen {
      background: var(--bg-page);
      display: flex;
      flex-direction: column;
      min-height: 520px;
    }

    .mockup-status-bar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 10px 16px 6px;
      font-size: 11px;
      font-weight: 700;
      color: var(--text-muted);
      background: var(--bg-card);
      border-bottom: 1px solid var(--border-color);
    }

    .mockup-appbar {
      background: var(--bg-card);
      border-bottom: 1px solid var(--border-color);
      padding: 9px 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .mockup-brand {
      font-size: 18px;
      font-weight: 800;
      letter-spacing: -0.5px;
      display: flex;
      align-items: center;
    }

    .mockup-brand-deen {
      color: var(--primary);
    }

    .mockup-brand-one {
      color: var(--text-heading);
    }

    .mockup-appbar-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .mockup-action-btn {
      width: 26px;
      height: 26px;
      border-radius: 50%;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 11px;
    }

    .mockup-body {
      padding: 12px 14px;
      display: flex;
      flex-direction: column;
      gap: 9px;
      flex: 1;
    }

    .mockup-prayer-hero {
      background: linear-gradient(135deg, var(--primary) 0%, #047857 100%);
      color: #FFFFFF;
      border-radius: 14px;
      padding: 11px 13px;
      box-shadow: 0 4px 14px var(--primary-glow);
    }

    .mockup-prayer-top {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      margin-bottom: 4px;
    }

    .mockup-prayer-label {
      font-size: 9.5px;
      opacity: 0.9;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.3px;
    }

    .mockup-prayer-waqt {
      font-size: 14.5px;
      font-weight: 800;
      letter-spacing: -0.2px;
    }

    .mockup-prayer-badge {
      background: rgba(255, 255, 255, 0.22);
      -webkit-backdrop-filter: blur(4px);
      backdrop-filter: blur(4px);
      font-size: 9px;
      font-weight: 700;
      padding: 2px 7px;
      border-radius: var(--radius-full);
      border: 1px solid rgba(255, 255, 255, 0.3);
    }

    .mockup-prayer-sub {
      font-size: 10px;
      opacity: 0.92;
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .mockup-card-item {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: 13px;
      padding: 9px 11px;
      display: flex;
      align-items: center;
      gap: 11px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
    }

    .mockup-card-icon {
      width: 32px;
      height: 32px;
      border-radius: 9px;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      flex-shrink: 0;
    }

    .mockup-card-text {
      flex: 1;
      min-width: 0;
    }

    .mockup-card-name {
      font-size: 12px;
      font-weight: 800;
      color: var(--text-heading);
      line-height: 1.25;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .mockup-card-desc {
      font-size: 10px;
      color: var(--text-muted);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .mockup-badge-active {
      font-size: 9px;
      font-weight: 700;
      padding: 2px 7px;
      border-radius: var(--radius-full);
      background: var(--primary-soft);
      color: var(--primary);
      flex-shrink: 0;
    }

    /* Floating badges beside phone */
    .floating-chip {
      position: absolute;
      z-index: 20;
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 8px 14px;
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.12);
      display: flex;
      align-items: center;
      gap: 10px;
      animation: floatBadge 4s ease-in-out infinite alternate;
    }

    .floating-chip.top-left {
      top: 15%;
      left: -20px;
    }

    .floating-chip.bottom-right {
      bottom: 12%;
      right: -20px;
      animation-delay: 1.5s;
    }

    @keyframes floatBadge {
      0% { transform: translateY(0); }
      100% { transform: translateY(-8px); }
    }

    .chip-icon {
      width: 28px;
      height: 28px;
      border-radius: 50%;
      background: var(--primary);
      color: #FFFFFF;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
    }

    .chip-text {
      font-size: 12px;
      font-weight: 800;
      color: var(--text-heading);
      line-height: 1.2;
    }

    .chip-sub {
      font-size: 10px;
      color: var(--text-muted);
    }

    /* ==========================================================================
       USER & CONTENT STATISTICS (Impact Counter Bar)
       ========================================================================== */
    .stats-section {
      position: relative;
      padding: 50px 0;
      background: var(--bg-section);
      border-top: 1px solid var(--border-color);
      border-bottom: 1px solid var(--border-color);
      transition: background-color 0.3s ease;
    }

    .stats-container-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: 28px;
      padding: 40px 30px;
      box-shadow: var(--shadow-card);
    }
    @media (min-width: 768px) {
      .stats-container-card { padding: 50px 40px; }
    }

    .section-tag-center {
      text-align: center;
      margin-bottom: 12px;
    }

    .section-pill-tag {
      display: inline-block;
      font-size: 13px;
      font-weight: 700;
      color: var(--tag-badge-color);
      background: var(--tag-badge-bg);
      padding: 6px 18px;
      border-radius: var(--radius-full);
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .stats-main-heading {
      text-align: center;
      font-size: clamp(1.4rem, 2.2vw, 30px);
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 36px;
      letter-spacing: -0.3px;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
    }
    @media (min-width: 1024px) {
      .stats-grid {
        grid-template-columns: repeat(4, 1fr);
        gap: 24px;
      }
    }

    .stat-item-box {
      background: var(--bg-section);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 24px 16px;
      text-align: center;
      transition: transform 0.25s ease, box-shadow 0.25s ease;
    }

    .stat-item-box:hover {
      transform: translateY(-4px);
      box-shadow: var(--shadow-sm);
    }

    .stat-number {
      font-size: clamp(1.6rem, 2.2vw, 28px);
      font-weight: 700;
      color: var(--text-heading);
      line-height: 1.1;
      margin-bottom: 6px;
    }

    .stat-label-sub {
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      color: var(--text-muted);
      font-weight: 600;
      margin-bottom: 2px;
    }

    .stat-label-title {
      font-size: 13px;
      font-weight: 650;
      color: var(--text-heading);
    }

    /* ==========================================================================
       OUR PROJECTS SHOWCASE (Alternating Detailed Feature Cards)
       ========================================================================== */
    .projects-section {
      padding: 70px 0;
    }
    @media (min-width: 768px) {
      .projects-section { padding: 100px 0; }
    }

    .section-header-block {
      text-align: center;
      max-width: 720px;
      margin: 0 auto 60px;
    }

    .section-title {
      font-size: clamp(1.5rem, 2.4vw, 34px);
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 14px;
      letter-spacing: -0.3px;
    }

    .section-desc {
      font-size: 15.5px;
      color: var(--text-body);
      line-height: 1.6;
    }

    /* Project Rows (Alternating Layout) */
    .project-row {
      display: grid;
      grid-template-columns: 1fr;
      align-items: center;
      gap: 40px;
      margin-bottom: 90px;
    }
    @media (min-width: 1024px) {
      .project-row {
        grid-template-columns: 1.1fr 0.9fr;
        gap: 60px;
      }
      .project-row.reversed {
        grid-template-columns: 0.9fr 1.1fr;
      }
      .project-row.reversed .project-content-col {
        order: 2;
      }
      .project-row.reversed .project-visual-col {
        order: 1;
      }
    }

    .project-title {
      font-size: clamp(1.35rem, 2vw, 28px);
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 8px;
    }

    .project-tagline {
      font-size: 15px;
      font-weight: 600;
      color: var(--primary);
      margin-bottom: 16px;
    }

    .project-desc {
      font-size: 15px;
      color: var(--text-body);
      line-height: 1.7;
      margin-bottom: 28px;
    }

    /* Sub-features grid inside project */
    .project-features-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 14px;
      margin-bottom: 28px;
    }
    @media (min-width: 640px) {
      .project-features-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .subfeat-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 16px;
      transition: all 0.25s ease;
      box-shadow: var(--shadow-sm);
    }

    .subfeat-card:hover {
      box-shadow: var(--shadow-card);
      border-color: var(--primary);
      transform: translateY(-2px);
    }

    .subfeat-icon {
      font-size: 20px;
      color: var(--primary);
      margin-bottom: 10px;
    }

    .subfeat-title {
      font-size: 14.5px;
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 4px;
    }

    .subfeat-desc {
      font-size: 12.5px;
      color: var(--text-body);
      line-height: 1.5;
    }

    .project-action-row {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 14px;
    }

    .btn-project-cta {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: var(--primary);
      color: #FFFFFF;
      font-size: 14px;
      font-weight: 700;
      padding: 10px 22px;
      border-radius: var(--radius-sm);
      transition: all 0.2s ease;
      box-shadow: 0 4px 12px var(--primary-glow);
    }

    .btn-project-cta:hover {
      background: var(--primary-hover);
      transform: translateY(-1px);
    }

    .btn-project-outline {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      background: var(--bg-card);
      border: 1.5px solid var(--border-strong);
      color: var(--text-heading);
      font-size: 14px;
      font-weight: 700;
      padding: 9px 20px;
      border-radius: var(--radius-sm);
      transition: all 0.2s ease;
    }

    .btn-project-outline:hover {
      border-color: var(--primary);
      color: var(--primary);
    }

    /* Project Visual Presentation */
    .project-visual-card {
      position: relative;
      background: var(--bg-section);
      border: 1px solid var(--border-color);
      border-radius: 28px;
      padding: 24px;
      display: flex;
      justify-content: center;
      align-items: center;
      overflow: hidden;
      min-height: 380px;
    }

    .project-preview-mock {
      width: 100%;
      max-width: 360px;
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-lg);
      box-shadow: var(--shadow-card-hover);
      overflow: hidden;
    }

    .preview-mock-header {
      background: var(--primary);
      color: #FFFFFF;
      padding: 14px 18px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .preview-mock-title {
      font-size: 15px;
      font-weight: 800;
    }

    .preview-mock-body {
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .preview-item {
      background: var(--bg-page);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 12px;
    }

    .preview-badge-row {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 6px;
    }

    .badge-grade {
      font-size: 10px;
      font-weight: 800;
      padding: 2px 8px;
      border-radius: var(--radius-full);
      background: #D1FAE5;
      color: #065F46;
    }

    .preview-item-arabic {
      font-family: var(--font-arabic);
      font-size: 18px;
      color: var(--text-heading);
      direction: rtl;
      line-height: 1.6;
      margin-bottom: 6px;
    }

    .preview-item-trans {
      font-size: 12.5px;
      color: var(--text-body);
      line-height: 1.5;
    }

    /* Hajj & Salah Stage Visual Thumbnail in Mockup */
    .hajj-stage-banner,
    .salah-stage-banner {
      width: 100%;
      height: 155px;
      object-fit: cover;
      border-radius: var(--radius-md);
      margin-bottom: 10px;
    }

    /* Ummah Community Post Preview Mockup */
    .community-feed-mock {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }

    .community-card-box {
      background: var(--bg-page);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-md);
      padding: 12px;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .community-user-header {
      display: flex;
      align-items: center;
      gap: 9px;
    }

    .community-avatar-circle {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      background: linear-gradient(135deg, var(--primary) 0%, #047857 100%);
      color: #FFFFFF;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
      font-weight: 700;
      flex-shrink: 0;
    }

    .community-user-meta {
      flex: 1;
      min-width: 0;
    }

    .community-author-name {
      font-size: 12.5px;
      font-weight: 800;
      color: var(--text-heading);
      line-height: 1.2;
    }

    .community-post-location {
      font-size: 10px;
      color: var(--text-muted);
    }

    .community-post-content {
      font-size: 12px;
      color: var(--text-body);
      line-height: 1.5;
    }

    .community-interactions-bar {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-top: 6px;
      border-top: 1px solid var(--border-color);
      font-size: 10.5px;
      font-weight: 700;
      color: var(--text-muted);
    }

    .community-interact-item {
      display: flex;
      align-items: center;
      gap: 4px;
    }

    .community-interact-item.active {
      color: var(--primary);
    }

    /* ==========================================================================
       CORE VALUES & WHY DEENONE (Integrity & Standards)
       ========================================================================== */
    .values-section {
      padding: 70px 0;
      background: var(--bg-section);
      border-top: 1px solid var(--border-color);
      border-bottom: 1px solid var(--border-color);
    }

    .values-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 20px;
    }
    @media (min-width: 640px) {
      .values-grid { grid-template-columns: repeat(2, 1fr); }
    }
    @media (min-width: 1024px) {
      .values-grid { grid-template-columns: repeat(4, 1fr); }
    }

    .value-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--radius-lg);
      padding: 28px 20px;
      text-align: center;
      transition: all 0.25s ease;
    }

    .value-card:hover {
      box-shadow: var(--shadow-card-hover);
      transform: translateY(-4px);
    }

    .value-icon-box {
      width: 52px;
      height: 52px;
      border-radius: 14px;
      background: var(--primary-soft);
      color: var(--primary);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      margin: 0 auto 16px;
    }

    .value-card-title {
      font-size: 16px;
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 8px;
    }

    .value-card-desc {
      font-size: 13.5px;
      color: var(--text-body);
      line-height: 1.6;
    }

    /* ==========================================================================
       BE A PART OF SADAQAH JARIYAH (Signature Banner)
       ========================================================================== */
    .cta-sadaqah-section {
      padding: 80px 0;
      text-align: center;
      position: relative;
      overflow: hidden;
      background: var(--bg-page);
    }

    .cta-card-box {
      position: relative;
      z-index: 10;
      max-width: 780px;
      margin: 0 auto;
    }

    .cta-headline {
      font-size: clamp(1.6rem, 2.8vw, 36px);
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 18px;
      line-height: 1.25;
      letter-spacing: -0.4px;
    }

    .cta-desc {
      font-size: 15.5px;
      color: var(--text-body);
      line-height: 1.7;
      margin-bottom: 30px;
    }

    .btn-cta-donate {
      display: inline-flex;
      align-items: center;
      gap: 10px;
      background: var(--primary);
      color: #FFFFFF;
      font-size: 15px;
      font-weight: 700;
      padding: 13px 34px;
      border-radius: var(--radius-sm);
      transition: all 0.25s ease;
      box-shadow: 0 8px 24px var(--primary-glow);
    }

    .btn-cta-donate:hover {
      background: var(--primary-hover);
      transform: translateY(-2px);
      box-shadow: 0 12px 30px var(--primary-glow);
    }

    /* ==========================================================================
       RICH MULTI-COLUMN FOOTER (Structured 4-Column Layout)
       ========================================================================== */
    .site-footer {
      background: var(--footer-bg);
      border-top: 1px solid var(--border-color);
      padding-top: 60px;
      padding-bottom: 30px;
      position: relative;
      transition: background-color 0.3s ease;
    }

    .footer-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 36px;
      margin-bottom: 48px;
    }
    @media (min-width: 640px) {
      .footer-grid { grid-template-columns: repeat(2, 1fr); gap: 32px; }
    }
    @media (min-width: 1024px) {
      .footer-grid { grid-template-columns: 1.8fr 1.2fr 1.2fr 1.4fr; gap: 36px; }
    }

    .footer-col {
      display: flex;
      flex-direction: column;
    }

    .footer-col-title {
      font-size: 16px;
      font-weight: 700;
      color: var(--footer-title);
      margin-bottom: 20px;
      letter-spacing: -0.2px;
      position: relative;
      padding-bottom: 8px;
    }
    .footer-col-title::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 24px;
      height: 2px;
      background: var(--primary);
      border-radius: 2px;
    }

    .footer-bio-text {
      font-size: 14px;
      color: var(--text-body);
      line-height: 1.65;
      margin-bottom: 20px;
    }

    .footer-download-label {
      font-size: 13px;
      font-weight: 700;
      color: var(--text-heading);
      margin-bottom: 10px;
    }

    .footer-download-btns {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
    }

    .footer-links-list {
      list-style: none;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .footer-link-item a {
      font-size: 14px;
      color: var(--text-body);
      transition: color 0.2s ease, transform 0.2s ease;
      display: inline-flex;
      align-items: center;
      gap: 6px;
    }

    .footer-link-item a:hover {
      color: var(--primary);
      transform: translateX(4px);
    }

    .footer-contact-info {
      font-size: 13.5px;
      color: var(--text-body);
      line-height: 1.6;
      margin-bottom: 16px;
    }

    .footer-contact-info a {
      color: var(--primary);
      font-weight: 700;
      text-decoration: underline;
    }

    .footer-social-row {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-top: 12px;
    }

    .footer-bottom-bar {
      border-top: 1px solid var(--border-color);
      padding-top: 24px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
      text-align: center;
      font-size: 13px;
      color: var(--text-muted);
    }
    @media (min-width: 768px) {
      .footer-bottom-bar {
        flex-direction: row;
        text-align: left;
      }
    }

    .footer-credits {
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .footer-credits .heart-icon {
      color: #EF4444;
    }

    /* Utilitarian text dual-language support */
    [data-lang="bn"] .en-only { display: none !important; }
    [data-lang="en"] .bn-only { display: none !important; }
  </style>
</head>
<body>

  <!-- =========================================================================
       STICKY NAVBAR (Header with Logo, Menu, Language & Theme Switchers)
       ========================================================================= -->
  <header class="navbar">
    <div class="container nav-inner">
      
      <!-- Brand Logo -->
      <a href="<?php echo SITE_URL; ?>" class="brand-logo" aria-label="DeenOne"><span class="brand-green">Deen</span><span class="brand-white">One</span></a>

      <!-- Desktop Navigation Menu -->
      <ul class="nav-menu">
        <li class="nav-item">
          <a href="#home" class="nav-link active">
            <span class="bn-only">হোম</span>
            <span class="en-only">Home</span>
          </a>
        </li>

        <!-- Our Projects Dropdown -->
        <li class="nav-item">
          <a href="#projects" class="nav-link">
            <span class="bn-only">আমাদের প্রজেক্টসমূহ</span>
            <span class="en-only">Our Projects</span>
            <i class="fa-solid fa-chevron-down" style="font-size: 10px;"></i>
          </a>
          <div class="nav-dropdown">
            <a href="#app-deenone" class="dropdown-link">
              <div class="dropdown-icon"><i class="fa-solid fa-mobile-screen"></i></div>
              <div>
                <div class="bn-only">দীনওয়ান অ্যাপ</div>
                <div class="en-only">DeenOne App</div>
              </div>
            </a>
            <a href="#al-hadith" class="dropdown-link">
              <div class="dropdown-icon"><i class="fa-solid fa-book-quran"></i></div>
              <div>
                <div class="bn-only">আল-হাদিস ডাটাবেজ</div>
                <div class="en-only">Al Hadith Database</div>
              </div>
            </a>
            <a href="#visual-salah" class="dropdown-link">
              <div class="dropdown-icon" style="color: #0D9488;"><i class="fa-solid fa-person-praying"></i></div>
              <div>
                <div class="bn-only">সচিত্র সালাত গাইড</div>
                <div class="en-only">Visual Salah Guide</div>
              </div>
            </a>
            <a href="#visual-hajj" class="dropdown-link">
              <div class="dropdown-icon"><i class="fa-solid fa-kaaba"></i></div>
              <div>
                <div class="bn-only">ভিজ্যুয়াল হজ্ব গাইড</div>
                <div class="en-only">Visual Hajj Guide</div>
              </div>
            </a>
            <a href="#ummah-network" class="dropdown-link">
              <div class="dropdown-icon" style="color: #6366F1;"><i class="fa-solid fa-users"></i></div>
              <div>
                <div class="bn-only">উম্মাহ নেটওয়ার্ক</div>
                <div class="en-only">Ummah Network</div>
              </div>
            </a>
            <a href="#blood-network" class="dropdown-link">
              <div class="dropdown-icon" style="color: #E11D48;"><i class="fa-solid fa-droplet"></i></div>
              <div>
                <div class="bn-only">রক্তদান নেটওয়ার্ক</div>
                <div class="en-only">Blood Network</div>
              </div>
            </a>
          </div>
        </li>

        <li class="nav-item">
          <a href="#stats" class="nav-link">
            <span class="bn-only">পরিসংখ্যান</span>
            <span class="en-only">Statistics</span>
          </a>
        </li>

        <li class="nav-item">
          <a href="#why-deenone" class="nav-link">
            <span class="bn-only">কেন দীনওয়ান</span>
            <span class="en-only">Why DeenOne</span>
          </a>
        </li>

        <li class="nav-item">
          <a href="<?php echo SITE_URL; ?>donate/" class="nav-link" target="_blank">
            <span class="bn-only">সদকাহ ও দান</span>
            <span class="en-only">Sadaqah</span>
          </a>
        </li>
      </ul>

      <!-- Nav Actions Right -->
      <div class="nav-actions">
        <!-- Social Icons -->
        <div class="social-links">
          <a href="https://www.facebook.com/" target="_blank" class="social-icon-btn" aria-label="Facebook">
            <i class="fa-brands fa-facebook-f"></i>
          </a>
          <a href="https://t.me/" target="_blank" class="social-icon-btn" aria-label="Telegram">
            <i class="fa-brands fa-telegram"></i>
          </a>
          <a href="https://youtube.com/" target="_blank" class="social-icon-btn" aria-label="YouTube">
            <i class="fa-brands fa-youtube"></i>
          </a>
        </div>

        <!-- Language Switcher Button -->
        <button id="langToggleBtn" class="btn-icon-toggle" aria-label="Toggle Language" title="Toggle Language">
          <span class="bn-only">EN</span>
          <span class="en-only">বাং</span>
        </button>

        <!-- Theme Switcher Button -->
        <button id="themeToggleBtn" class="btn-icon-toggle" aria-label="Toggle Theme" title="Toggle Dark/Light Mode">
          <i class="fa-solid fa-moon"></i>
        </button>

        <!-- Support Us CTA -->
        <a href="<?php echo SITE_URL; ?>donate/" class="btn-support-nav">
          <i class="fa-solid fa-heart"></i>
          <span class="bn-only">সহায়তা করুন</span>
          <span class="en-only">Support Us</span>
        </a>

        <!-- Hamburger for Mobile -->
        <button id="hamburgerBtn" class="btn-hamburger" aria-label="Open Mobile Menu">
          <i class="fa-solid fa-bars"></i>
        </button>
      </div>

    </div>
  </header>

  <!-- Mobile Drawer Backdrop & Menu -->
  <div id="drawerBackdrop" class="drawer-backdrop"></div>
  <aside id="mobileDrawer" class="mobile-drawer" aria-label="Mobile Navigation">
    <div class="drawer-header">
      <a href="<?php echo SITE_URL; ?>" class="brand-logo" style="font-size: 19px;" aria-label="DeenOne"><span class="brand-green">Deen</span><span class="brand-white">One</span></a>
      <button id="closeDrawerBtn" style="font-size: 20px; color: var(--text-heading);">
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>

    <ul class="drawer-links">
      <li>
        <a href="#home" class="drawer-link drawer-nav-link">
          <i class="fa-solid fa-house" style="color: var(--primary);"></i>
          <span class="bn-only">হোম</span>
          <span class="en-only">Home</span>
        </a>
      </li>
      <li>
        <a href="#projects" class="drawer-link drawer-nav-link">
          <i class="fa-solid fa-layer-group" style="color: var(--primary);"></i>
          <span class="bn-only">আমাদের প্রজেক্টসমূহ</span>
          <span class="en-only">Our Projects</span>
        </a>
      </li>
      <li>
        <a href="#stats" class="drawer-link drawer-nav-link">
          <i class="fa-solid fa-chart-line" style="color: var(--primary);"></i>
          <span class="bn-only">পরিসংখ্যান</span>
          <span class="en-only">Statistics</span>
        </a>
      </li>
      <li>
        <a href="#why-deenone" class="drawer-link drawer-nav-link">
          <i class="fa-solid fa-shield-halved" style="color: var(--primary);"></i>
          <span class="bn-only">কেন দীনওয়ান</span>
          <span class="en-only">Why DeenOne</span>
        </a>
      </li>
      <li>
        <a href="<?php echo SITE_URL; ?>donate/" class="drawer-link" target="_blank">
          <i class="fa-solid fa-hand-holding-heart" style="color: var(--primary);"></i>
          <span class="bn-only">সদকাহ ও অনুদান</span>
          <span class="en-only">Support & Sadaqah</span>
        </a>
      </li>
    </ul>

    <div style="margin-top: auto; padding-top: 20px; border-top: 1px solid var(--border-color);">
      <a href="<?php echo htmlspecialchars($apkDownloadUrl); ?>" class="btn-support-nav" style="width: 100%; justify-content: center; margin-bottom: 12px;" download="deenone.apk">
        <i class="fa-solid fa-download"></i>
        <span class="bn-only">এপিকে ডাউনলোড করুন</span>
        <span class="en-only">Download APK</span>
      </a>
      <p style="font-size: 11px; text-align: center; color: var(--text-muted);">
        100% Free & Open Islamic Technology
      </p>
    </div>
  </aside>

  <!-- =========================================================================
       HERO SECTION (Bold Statement, Islamic Arch Silhouette & Floating Phone)
       ========================================================================= -->
  <section id="home" class="hero-section">
    <div class="container">
      <div class="hero-grid">

        <!-- Left Column: Copywriting & CTAs -->
        <div class="hero-copy-col">
          <div class="eyebrow-badge">
            <i class="fa-solid fa-gem"></i>
            <span class="bn-only">দ্বীন প্রচারে উন্মুক্ত প্রযুক্তি</span>
            <span class="en-only">What We Do?</span>
          </div>

          <h1 class="hero-title">
            <span class="bn-only">প্রযুক্তি ও খাঁটি ইসলামী জ্ঞানের সমন্বয়ে দ্বীনের প্রচার</span>
            <span class="en-only">We Design and Develop Authentic Islamic Resources</span>
          </h1>

          <p class="hero-subtitle">
            <span class="bn-only">দীনওয়ান বিশ্বব্যাপী উম্মাহর সেবায় ১০০% বিজ্ঞাপনহীন, আধুনিক ও বিশুদ্ধ ইসলামিক অ্যাপ্লিকেশন তৈরি করছে। একমাত্র আল্লাহর সন্তুষ্টির উদ্দেশ্যে বিশুদ্ধ কুরআন ও সহীহ সুন্নাহর ভিত্তিতে পরিচালিত।</span>
            <span class="en-only">DeenOne is providing modern Islamic apps for the benefit of Mankind, expecting rewards from Allah Subhana wa Ta'ala alone and adhering strictly to the authentic Quran and Sahih Sunnah.</span>
          </p>

          <!-- Key Checklist Features -->
          <div class="hero-features-list">
            <div class="hero-feat-item">
              <div class="feat-icon-star"><i class="fa-solid fa-check"></i></div>
              <div>
                <span class="bn-only">১০০% বিজ্ঞাপনমুক্ত অ্যাপ্লিকেশন</span>
                <span class="en-only">100% Ads Free Application</span>
              </div>
            </div>
            <div class="hero-feat-item">
              <div class="feat-icon-star"><i class="fa-solid fa-check"></i></div>
              <div>
                <span class="bn-only">তাহকীককৃত সহীহ হাদিসের নিশ্চয়তা</span>
                <span class="en-only">Authenticated Sahih Hadith Collection</span>
              </div>
            </div>
            <div class="hero-feat-item">
              <div class="feat-icon-star"><i class="fa-solid fa-check"></i></div>
              <div>
                <span class="bn-only">ল্যাগ-মুক্ত আল্ট্রা-স্মুথ ৬০ FPS ডিজাইন</span>
                <span class="en-only">Lag-free Smooth 60 FPS Experience</span>
              </div>
            </div>
            <div class="hero-feat-item">
              <div class="feat-icon-star"><i class="fa-solid fa-check"></i></div>
              <div>
                <span class="bn-only">সম্পূর্ণ অফলাইন রিডিং প্রযুক্তি</span>
                <span class="en-only">Offline-First Smart Architecture</span>
              </div>
            </div>
          </div>

          <!-- "Available On" Download Buttons -->
          <div>
            <div class="hero-cta-title">
              <span class="bn-only">অ্যাপ ডাউনলোড করুন</span>
              <span class="en-only">We are available on</span>
            </div>
            <div class="hero-cta-group">
              <!-- Direct APK Download Button -->
              <a href="<?php echo htmlspecialchars($apkDownloadUrl); ?>" class="btn-download-pill" download="deenone.apk" title="Download DeenOne Direct APK">
                <i class="fa-brands fa-android pill-icon"></i>
                <div class="pill-meta">
                  <span class="pill-sub bn-only">সরাসরি ইন্সটল</span>
                  <span class="pill-sub en-only">DIRECT DOWNLOAD</span>
                  <span class="pill-title">Android APK</span>
                </div>
              </a>

              <!-- Google Play -->
              <a href="<?php echo htmlspecialchars($playStoreUrl); ?>" target="_blank" class="btn-download-pill" title="Google Play Store">
                <i class="fa-brands fa-google-play pill-icon"></i>
                <div class="pill-meta">
                  <span class="pill-sub">GET IT ON</span>
                  <span class="pill-title">Google Play</span>
                </div>
              </a>

              <!-- Support Us Button -->
              <a href="<?php echo SITE_URL; ?>donate/" class="btn-project-cta" style="height: 48px; border-radius: 12px; padding: 0 20px;">
                <i class="fa-solid fa-heart"></i>
                <span class="bn-only">সদকাহ দিন</span>
                <span class="en-only">Support Us</span>
              </a>
            </div>
          </div>
        </div>

        <!-- Right Column: Visual Mockup with Mosque Silhouette -->
        <div class="hero-graphic-wrap">
          <!-- Mosque Arch Silhouette SVG -->
          <div class="mosque-arch-bg">
            <svg class="mosque-arch-svg" viewBox="0 0 430 745" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M387.383 173.238C387.383 122.501 339.751 113.075 320.178 103.071C224.872 54.3574 216.117 11.2695 215.492 0.562744C214.866 11.2695 206.111 54.3574 110.806 103.071C91.2323 113.075 43.5916 122.501 43.5916 173.238C43.5916 173.238 0.982423 173.681 0.982423 222.157V721.346C0.981356 724.384 1.59595 727.392 2.79113 730.199C3.9863 733.006 5.73863 735.557 7.94802 737.705C10.1574 739.854 12.7806 741.558 15.6677 742.721C18.5548 743.884 21.6492 744.482 24.7743 744.482H406.209C409.334 744.482 412.428 743.884 415.315 742.721C418.202 741.558 420.826 739.854 423.035 737.705C425.244 735.557 426.997 733.006 428.192 730.199C429.387 727.392 430.002 724.384 430.001 721.346V222.181C430.001 173.681 387.383 173.238 387.383 173.238Z"></path>
            </svg>
          </div>

          <!-- Floating Badges beside phone -->
          <div class="floating-chip top-left">
            <div class="chip-icon"><i class="fa-solid fa-person-praying"></i></div>
            <div>
              <div class="chip-text bn-only">১১টি সালাত ও ১২টি হজ্ব ধাপ</div>
              <div class="chip-text en-only">Visual Guides</div>
              <div class="chip-sub">HD Real Visuals</div>
            </div>
          </div>

          <div class="floating-chip bottom-right">
            <div class="chip-icon" style="background: #059669;"><i class="fa-solid fa-check-double"></i></div>
            <div>
              <div class="chip-text bn-only">তাহকীককৃত সহীহ হাদিস</div>
              <div class="chip-text en-only">Sahih Hadith Engine</div>
              <div class="chip-sub">Graded & Verified</div>
            </div>
          </div>

          <!-- Realistic Phone Mockup Matching DeenOne Mobile App -->
          <div class="phone-mockup-frame">
            <div class="mockup-screen">
              <!-- Status bar -->
              <div class="mockup-status-bar">
                <span>05:30</span>
                <span><i class="fa-solid fa-wifi"></i> <i class="fa-solid fa-battery-full"></i></span>
              </div>

              <!-- Top App Header matching DeenOne Mobile App -->
              <div class="mockup-appbar">
                <div class="mockup-brand">
                  <span class="mockup-brand-deen">Deen</span><span class="mockup-brand-one">One</span>
                </div>
                <div class="mockup-appbar-actions">
                  <span class="mockup-action-btn"><i class="fa-solid fa-bell"></i></span>
                  <span class="mockup-action-btn"><i class="fa-solid fa-compass"></i></span>
                </div>
              </div>

              <!-- Content Cards inside Mockup -->
              <div class="mockup-body">
                <!-- Hero Prayer Tracker Card -->
                <div class="mockup-prayer-hero">
                  <div class="mockup-prayer-top">
                    <div>
                      <div class="mockup-prayer-label bn-only">পরবর্তী সালাত</div>
                      <div class="mockup-prayer-label en-only">Next Prayer</div>
                      <div class="mockup-prayer-waqt bn-only">আসর • ০৪:৪৫ PM</div>
                      <div class="mockup-prayer-waqt en-only">Asr • 04:45 PM</div>
                    </div>
                    <div class="mockup-prayer-badge bn-only">৪২ মিনিট বাকি</div>
                    <div class="mockup-prayer-badge en-only">in 42 mins</div>
                  </div>
                  <div class="mockup-prayer-sub bn-only"><i class="fa-solid fa-location-dot"></i> ঢাকা, বাংলাদেশ • জিপিএস সিঙ্ক</div>
                  <div class="mockup-prayer-sub en-only"><i class="fa-solid fa-location-dot"></i> Dhaka, Bangladesh • GPS Calibrated</div>
                </div>

                <!-- Card 1: Today's Hadith -->
                <div class="mockup-card-item">
                  <div class="mockup-card-icon" style="color: #059669; background: #D1FAE5;"><i class="fa-solid fa-book-open"></i></div>
                  <div class="mockup-card-text">
                    <div class="mockup-card-name bn-only">আজকের সহীহ হাদিস</div>
                    <div class="mockup-card-name en-only">Hadith of the Day</div>
                    <div class="mockup-card-desc">সহীহ বুখারী • হাদীস নং ১</div>
                  </div>
                  <span class="mockup-badge-active">সহীহ</span>
                </div>

                <!-- Card 2: Interactive Salah Guide -->
                <div class="mockup-card-item">
                  <div class="mockup-card-icon" style="color: #0D9488; background: #CCFBF1;"><i class="fa-solid fa-person-praying"></i></div>
                  <div class="mockup-card-text">
                    <div class="mockup-card-name bn-only">সচিত্র সালাত গাইড</div>
                    <div class="mockup-card-name en-only">Visual Salah Guide</div>
                    <div class="mockup-card-desc bn-only">১১টি পর্যায় • পুরুষ ও মহিলা</div>
                    <div class="mockup-card-desc en-only">11 Stages • Male & Female</div>
                  </div>
                  <i class="fa-solid fa-chevron-right" style="font-size: 11px; color: var(--text-muted);"></i>
                </div>

                <!-- Card 3: Interactive Hajj Journey -->
                <div class="mockup-card-item">
                  <div class="mockup-card-icon" style="color: #D97706; background: #FEF3C7;"><i class="fa-solid fa-kaaba"></i></div>
                  <div class="mockup-card-text">
                    <div class="mockup-card-name bn-only">সচিত্র হজ্ব সফর (১২ ধাপ)</div>
                    <div class="mockup-card-name en-only">Visual Hajj Journey</div>
                    <div class="mockup-card-desc bn-only">ময়দানে আরাফাত অবস্থান ও দু'আ</div>
                    <div class="mockup-card-desc en-only">Day of Arafah & Duas</div>
                  </div>
                  <i class="fa-solid fa-chevron-right" style="font-size: 11px; color: var(--text-muted);"></i>
                </div>

                <!-- Card 4: Ummah Community Network -->
                <div class="mockup-card-item">
                  <div class="mockup-card-icon" style="color: #6366F1; background: #EEF2FF;"><i class="fa-solid fa-users"></i></div>
                  <div class="mockup-card-text">
                    <div class="mockup-card-name bn-only">উম্মাহ নেটওয়ার্ক হাব</div>
                    <div class="mockup-card-name en-only">Ummah Network Hub</div>
                    <div class="mockup-card-desc bn-only">দ্বীনি আলোচনা ও আমল শেয়ারিং</div>
                    <div class="mockup-card-desc en-only">Authentic Islamic Community</div>
                  </div>
                  <span class="mockup-badge-active" style="background: #EEF2FF; color: #6366F1;">Live</span>
                </div>

                <!-- Card 5: Emergency Blood Network -->
                <div class="mockup-card-item">
                  <div class="mockup-card-icon" style="color: #E11D48; background: #FFE4E6;"><i class="fa-solid fa-droplet"></i></div>
                  <div class="mockup-card-text">
                    <div class="mockup-card-name bn-only">জরুরি রক্তদান নেটওয়ার্ক</div>
                    <div class="mockup-card-name en-only">Blood Donor Network</div>
                    <div class="mockup-card-desc bn-only">৬৪ জেলায় ভেরিফায়েড রক্তদাতা</div>
                    <div class="mockup-card-desc en-only">Emergency Donors Directory</div>
                  </div>
                  <span class="mockup-badge-active" style="background: #FFE4E6; color: #E11D48;">Live</span>
                </div>
              </div>

            </div>
          </div>
        </div>

      </div>
    </div>
  </section>

  <!-- =========================================================================
       USER & CONTENT STATISTICS (Impact Metrics Counter Bar)
       ========================================================================= -->
  <section id="stats" class="stats-section">
    <div class="container">
      <div class="stats-container-card">
        
        <div class="section-tag-center">
          <span class="section-pill-tag">
            <span class="bn-only">অগ্রগতি ও পরিসংখ্যান</span>
            <span class="en-only">USER & KNOWLEDGE STATISTICS</span>
          </span>
        </div>

        <h2 class="stats-main-heading">
          <span class="bn-only">বিশ্বব্যাপী উম্মাহর নির্ভরযোগ্য দ্বীনি প্ল্যাটফর্ম</span>
          <span class="en-only">Serving Millions of Muslims Worldwide</span>
        </h2>

        <div class="stats-grid">
          <!-- Stat 1: Hadith Collection -->
          <div class="stat-item-box">
            <div class="stat-number"><?php echo htmlspecialchars($hadithCountDisplay); ?></div>
            <div class="stat-label-sub bn-only">হাদিস ভাণ্ডার</div>
            <div class="stat-label-sub en-only">COLLECTION</div>
            <div class="stat-label-title bn-only">তাহকীককৃত হাদিস</div>
            <div class="stat-label-title en-only">Verified Hadiths</div>
          </div>

          <!-- Stat 2: Quran Surahs -->
          <div class="stat-item-box">
            <div class="stat-number"><?php echo htmlspecialchars($surahsDisplay); ?></div>
            <div class="stat-label-sub bn-only">কুরআন মাজীদ</div>
            <div class="stat-label-sub en-only">QURAN MAJEED</div>
            <div class="stat-label-title bn-only">সূরা ও বিশুদ্ধ তাফসীর</div>
            <div class="stat-label-title en-only">Surahs & Tafsir</div>
          </div>

          <!-- Stat 3: Visual Hajj Stages -->
          <div class="stat-item-box">
            <div class="stat-number"><?php echo htmlspecialchars($hajjStagesDisplay); ?></div>
            <div class="stat-label-sub bn-only">সচিত্র হজ্ব সফর</div>
            <div class="stat-label-sub en-only">HAJJ GUIDE</div>
            <div class="stat-label-title bn-only">ধারাবাহিক ভিজ্যুয়াল ধাপ</div>
            <div class="stat-label-title en-only">Interactive Stages</div>
          </div>

          <!-- Stat 4: 100% Free & Ad Free -->
          <div class="stat-item-box">
            <div class="stat-number">100%</div>
            <div class="stat-label-sub bn-only">উন্মুক্ত সেবা</div>
            <div class="stat-label-sub en-only">OPEN SERVICE</div>
            <div class="stat-label-title bn-only">বিজ্ঞাপনমুক্ত ও ফ্রন্টলাইন</div>
            <div class="stat-label-title en-only">Free & Zero Ads</div>
          </div>
        </div>

      </div>
    </div>
  </section>

  <!-- =========================================================================
       OUR PROJECTS SHOWCASE (Detailed Multi-Section Highlights)
       ========================================================================= -->
  <section id="projects" class="projects-section">
    <div class="container">
      
      <!-- Section Header -->
      <div class="section-header-block">
        <div class="section-tag-center">
          <span class="section-pill-tag">
            <span class="bn-only">আমাদের প্রজেক্টসমূহ</span>
            <span class="en-only">OUR PROJECTS</span>
          </span>
        </div>
        <h2 class="section-title">
          <span class="bn-only">উম্মাহর কল্যাণে নির্মিত ডিজিটাল সল্যুশন</span>
          <span class="en-only">We Build What Empowers the Ummah</span>
        </h2>
        <p class="section-desc">
          <span class="bn-only">দীনওয়ান প্রতিটি সেবায় বিশুদ্ধতা, নির্ভরযোগ্যতা ও আধুনিক ভিজ্যুয়াল অভিজ্ঞতার সর্বোচ্চ সমন্বয় নিশ্চিত করে।</span>
          <span class="en-only">DeenOne focuses on authentic knowledge delivery, modern lightweight architecture, and complete freedom from commercial ads.</span>
        </p>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 1: DeenOne All-in-One App
           ------------------------------------------------------------------- -->
      <div id="app-deenone" class="project-row">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">দীনওয়ান - অল-ইন-ওয়ান ইসলামিক অ্যাপ</span>
            <span class="en-only">DeenOne - Complete Islamic Companion</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">দৈনন্দিন ইবাদতের সার্বক্ষণিক ডিজিটাল সফরসঙ্গী</span>
            <span class="en-only">Your Daily Prayer, Tracking & Knowledge Ecosystem</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">একটি পূর্ণাঙ্গ ও আধুনিক অ্যাপ্লিকেশন যেখানে রয়েছে স্বয়ংক্রিয় আযানসহ নামাজের নির্ভুল সময়সূচি, দৈনন্দিন সালাত ট্র্যাকার, জরুরি রক্তদাতা সন্ধান এবং কুইজের মাধ্যমে ইসলামিক জ্ঞান চর্চা।</span>
            <span class="en-only">An all-in-one mobile companion featuring high-precision GPS prayer times, automated Azan, synchronized prayer logging, blood donation network, and an interactive Islamic quiz engine.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-compass subfeat-icon"></i>
              <div class="subfeat-title bn-only">জিপিএস ভিত্তিক নির্ভুল সময়সূচি</div>
              <div class="subfeat-title en-only">Precise GPS Prayer Times</div>
              <div class="subfeat-desc bn-only">যেকোনো স্থান থেকে স্বয়ংক্রিয়ভাবে আসর, মাগরিব ও সেহরি-ইফতারের নির্ভুল সময়।</div>
              <div class="subfeat-desc en-only">Accurate prayer calculations with offline high-precision latitude adjustment.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-list-check subfeat-icon"></i>
              <div class="subfeat-title bn-only">দৈনন্দিন সালাত ট্র্যাকার</div>
              <div class="subfeat-title en-only">Daily Salah Tracker</div>
              <div class="subfeat-desc bn-only">ওয়াক্ত ও জামা'আতের সাথে সালাত আদায় ক্লাউডে সিঙ্ক ও ধারাবাহিক রিপোর্ট।</div>
              <div class="subfeat-desc en-only">Track your daily prayers with cloud backup, streak history, and monthly insights.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-shield-virus subfeat-icon"></i>
              <div class="subfeat-title bn-only">১০০% অফলাইন ও লাইটওয়েট</div>
              <div class="subfeat-title en-only">100% Offline Capable</div>
              <div class="subfeat-desc bn-only">ইন্টারনেট ছাড়াও অ্যাপ্লিকেশনের সকল মূল ফিচার নিরবচ্ছিন্নভাবে ব্যবহার উপযোগী।</div>
              <div class="subfeat-desc en-only">Works entirely without an active internet connection on modern smartphones.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-language subfeat-icon"></i>
              <div class="subfeat-title bn-only">বাংলা ও ইংরেজি মোড</div>
              <div class="subfeat-title en-only">Bilingual Interface</div>
              <div class="subfeat-desc bn-only">বিশুদ্ধ বাংলা এবং ইংরেজি ইন্টারফেসের মধ্যে তাৎক্ষণিক পরিবর্তন সুবিধা।</div>
              <div class="subfeat-desc en-only">Seamless one-tap language switching between pure Bengali and English.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo htmlspecialchars($apkDownloadUrl); ?>" class="btn-project-cta" download="deenone.apk">
              <i class="fa-solid fa-download"></i>
              <span class="bn-only">এপিকে ডাউনলোড</span>
              <span class="en-only">Download APK</span>
            </a>
            <a href="<?php echo htmlspecialchars($playStoreUrl); ?>" target="_blank" class="btn-project-outline">
              <i class="fa-brands fa-google-play"></i>
              <span>Google Play</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header">
                <span class="preview-mock-title">DeenOne Core App</span>
                <span class="badge-grade">Version 2.4</span>
              </div>
              <div class="preview-mock-body">
                <div class="preview-item">
                  <div class="preview-badge-row">
                    <span style="font-size: 11px; font-weight: 700; color: var(--primary);">Prayer Time</span>
                    <span style="font-size: 11px; font-weight: 700;">Dhaka, BD</span>
                  </div>
                  <div style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin-bottom: 4px;">Maghrib: 05:48 PM</div>
                  <div style="font-size: 11.5px; color: var(--text-muted);">Adhan in 14 minutes • GPS Calibrated</div>
                </div>

                <div class="preview-item">
                  <div class="preview-badge-row">
                    <span style="font-size: 11px; font-weight: 700; color: #D97706;">Daily Supplication</span>
                    <span class="badge-grade">Hisnul Muslim</span>
                  </div>
                  <div class="preview-item-arabic">اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا</div>
                  <div class="preview-item-trans">"হে আল্লাহ, আমি আপনার নিকট উপকারী জ্ঞান ও পবিত্র রিজিক কামনা করছি।"</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 2: Al Hadith Digital Database
           ------------------------------------------------------------------- -->
      <div id="al-hadith" class="project-row reversed">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">আল-হাদিস (Al Hadith) - সহীহ হাদিস সার্চ ইঞ্জিন</span>
            <span class="en-only">Al Hadith - Rigorous Digital Hadith Database</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">তাহকীক ও সনদের নির্ভরযোগ্য মানদণ্ডসহ সহীহ হাদিস</span>
            <span class="en-only">49,000+ Verified Hadiths with Multi-Publication References</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">সহীহ বুখারী, সহীহ মুসলিম, সুনানে আবু দাউদ, জামে আত-তিরমিযী সহ নির্ভরযোগ্য হাদিস গ্রন্থসমূহের এক বিশাল ডিজিটাল সংকলন। এতে রয়েছে প্রতিটি হাদিসের তাহকীককৃত মানদণ্ড (সহীহ, হাসান, যয়ীফ), নির্ভুল আরবি ও বাংলা অনুবাদ এবং দ্রুততম সার্চ ইঞ্জিন।</span>
            <span class="en-only">A comprehensive digital encyclopedia containing 49,000+ hadiths from Sahih Bukhari, Sahih Muslim, Abu Dawud, Tirmidhi, Nasai, and more. Features authentic scholarly gradings, instant bilingual search, and chapter navigation.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-certificate subfeat-icon"></i>
              <div class="subfeat-title bn-only">সনদ ও তাহকীক মানদণ্ড</div>
              <div class="subfeat-title en-only">Rigorous Authentication</div>
              <div class="subfeat-desc bn-only">প্রতিটি হাদিসের সাথে সহীহ, হাসান বা যয়ীফ মানদণ্ড স্পষ্ট অক্ষরে উল্লিখিত।</div>
              <div class="subfeat-desc en-only">Every narration clearly displays its verified grading by renowned Muhadditheen.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-magnifying-glass subfeat-icon"></i>
              <div class="subfeat-title bn-only">আল্ট্রা-ফাস্ট সার্চ ইঞ্জিন</div>
              <div class="subfeat-title en-only">High-Speed Search Engine</div>
              <div class="subfeat-desc bn-only">আরবি মূল শব্দ, বাংলা ও ইংরেজি শব্দ বা হাদিস নম্বর দিয়ে চোখের পলকে রেজাল্ট।</div>
              <div class="subfeat-desc en-only">Instant exact-match and keyword search across all hadith chapters and books.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-book-bookmark subfeat-icon"></i>
              <div class="subfeat-title bn-only">একাধিক কিতাবের ক্রস-রেফারেন্স</div>
              <div class="subfeat-title en-only">Cross-Book References</div>
              <div class="subfeat-desc bn-only">তাহকীক শায়খ আলবানী ও দারুসসালাম সহ আন্তর্জাতিক প্রকাশনার রেফারেন্স নম্বর।</div>
              <div class="subfeat-desc en-only">Parallel reference numbers across major classical and modern publications.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-share-nodes subfeat-icon"></i>
              <div class="subfeat-title bn-only">সহজ কপি ও শেয়ারিং</div>
              <div class="subfeat-title en-only">One-Tap Sharing</div>
              <div class="subfeat-desc bn-only">আরবি ইবারত, বাংলা অনুবাদ ও তাখরীজসহ সোশ্যাল মিডিয়ায় সহজেই শেয়ার করার সুবিধা।</div>
              <div class="subfeat-desc en-only">Cleanly formatted text sharing with Arabic, translation, and complete citation.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo SITE_URL; ?>downloads/deenone.apk" class="btn-project-cta" download="deenone.apk">
              <i class="fa-solid fa-book-quran"></i>
              <span class="bn-only">হাদিস লাইব্রেরি ডাউনলোড করুন</span>
              <span class="en-only">Explore Hadith Library</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header" style="background: #047857;">
                <span class="preview-mock-title">সহীহ বুখারী • কিতাবুল ঈমান</span>
                <span class="badge-grade">সহীহ হাদিস</span>
              </div>
              <div class="preview-mock-body">
                <div class="preview-item">
                  <div class="preview-badge-row">
                    <span style="font-size: 11px; font-weight: 700; color: var(--primary);">হাদিস নং: ১</span>
                    <span class="badge-grade" style="background: #D1FAE5; color: #065F46;">সহীহ (বুখারী)</span>
                  </div>
                  <div class="preview-item-arabic">إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى</div>
                  <div class="preview-item-trans">"নিশ্চয়ই প্রতিটি কাজের ফলাফল নিয়তের উপর নির্ভরশীল এবং প্রত্যেক ব্যক্তি তার নিয়তের অনুরূপ ফল পাবে।"</div>
                  <div style="font-size: 10.5px; color: var(--text-muted); margin-top: 6px;">রেফারেন্স: সহীহুল বুখারী (ইফা) • অধ্যায়: ওহীর সূচনা</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 3: Interactive Visual Salah Guide
           ------------------------------------------------------------------- -->
      <div id="visual-salah" class="project-row">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">সচিত্র সালাত ও নামাজ শিক্ষা (পুরুষ ও মহিলা গাইড)</span>
            <span class="en-only">Interactive Visual Salah Guide (Male & Female)</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">বাস্তব অঙ্গভঙ্গি ও ধারাবাহিক নিয়মসহ সহীহ নামাজ নির্দেশিকা</span>
            <span class="en-only">Step-by-Step Prayer Guide with Verified Masnoon Postures</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">তাকবীরে তাহরীমা থেকে সালাম পর্যন্ত নামাজের প্রতিটি রুকন ও অঙ্গভঙ্গির নিখুঁত সচিত্র উপস্থাপনা। এতে রয়েছে পুরুষ ও মহিলাদের জন্য সহীহ সুন্নাহ মোতাবেক পৃথক নির্দেশনা, বিশুদ্ধ মাসনূন দু'আ, বাংলা উচ্চারণ, অর্থ ও হাদিসের তাহকীককৃত রেফারেন্স।</span>
            <span class="en-only">A complete illustrated step-by-step prayer companion covering every posture from Takbeer to Tasleem. Features separate authentic Sunnah guides for men and women, verified Masnoon supplications with Arabic audio, transliteration, translation, and Sahih Hadith references.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-person-praying subfeat-icon" style="color: #0D9488;"></i>
              <div class="subfeat-title bn-only">পুরুষ ও মহিলাদের পৃথক গাইড</div>
              <div class="subfeat-title en-only">Dedicated Gender Modes</div>
              <div class="subfeat-desc bn-only">সহীহ হাদিস ও সুন্নাহর ভিত্তিতে পুরুষ ও মহিলা উভয়ের নামাজের সঠিক অঙ্গভঙ্গি।</div>
              <div class="subfeat-desc en-only">Distinct and authentic posture guides for both men and women as per Sunnah.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-image subfeat-icon" style="color: #0D9488;"></i>
              <div class="subfeat-title bn-only">হাই-রেজ্যুলেশন সচিত্র ধাপ</div>
              <div class="subfeat-title en-only">HD Illustrated Stages</div>
              <div class="subfeat-desc bn-only">কিয়াম, রুকু, সিজদা ও বৈঠকের প্রতিটি ভঙ্গি স্পষ্ট ছবির মাধ্যমে দৃশ্যমান।</div>
              <div class="subfeat-desc en-only">High-definition visual demonstrations for Qiyam, Ruku, Sujood, and Tashahhud.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-book-open-reader subfeat-icon" style="color: #0D9488;"></i>
              <div class="subfeat-title bn-only">বিশুদ্ধ মাসনূন দু'আ ও অর্থ</div>
              <div class="subfeat-title en-only">Masnoon Duas & Tafsir</div>
              <div class="subfeat-desc bn-only">ছানা, রুকুর তাসবীহ, তাশাহহুদ, দরূদ ও দু'আ মাসূরার বিশুদ্ধ উচ্চারণ ও বাংলা অর্থ।</div>
              <div class="subfeat-desc en-only">Verified supplications for each posture with Arabic, transliteration, and translation.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-wifi-slash subfeat-icon" style="color: #0D9488;"></i>
              <div class="subfeat-title bn-only">১০০% অফলাইন সুবিধা</div>
              <div class="subfeat-title en-only">100% Offline Access</div>
              <div class="subfeat-desc bn-only">ইন্টারনেট সংযোগ ছাড়াই সকল ছবি, দু'আ ও নির্দেশিকা অনায়াসে পড়ার নিশ্চয়তা।</div>
              <div class="subfeat-desc en-only">Fully cached locally so you can learn prayer rules anywhere without the internet.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo SITE_URL; ?>downloads/deenone.apk" class="btn-project-cta" style="background: #0D9488; box-shadow: 0 4px 14px rgba(13, 148, 136, 0.3);" download="deenone.apk">
              <i class="fa-solid fa-person-praying"></i>
              <span class="bn-only">সালাত গাইডটি পান (অ্যাপ)</span>
              <span class="en-only">Get Visual Salah Guide</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card with Real Uploaded Salah Step Photo -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header" style="background: #0D9488;">
                <span class="preview-mock-title bn-only">ধাপ ৩: হাত বাঁধা ও ছানা পাঠ</span>
                <span class="preview-mock-title en-only">Step 3: Folding Hands & Sana</span>
                <span class="badge-grade" style="background: #CCFBF1; color: #0F766E;">পুরুষ ও মহিলা</span>
              </div>
              <div class="preview-mock-body">
                <!-- Real Male Step 3 Image from uploads -->
                <img src="<?php echo SITE_URL; ?>uploads/namaz_learning/male/img_salah_male_step_3.jpg" alt="Visual Salah Step" class="salah-stage-banner" loading="lazy" decoding="async">
                <div class="preview-item">
                  <div class="preview-badge-row">
                    <span style="font-size: 11px; font-weight: 700; color: #0D9488;">নামাজের প্রারম্ভিক দু'আ (ছানা)</span>
                    <span class="badge-grade">আবু দাউদ ৭৭৬</span>
                  </div>
                  <div class="preview-item-arabic">سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ</div>
                  <div class="preview-item-trans">"হে আল্লাহ! আপনার সপ্রশংস পবিত্রতা ঘোষণা করছি, আপনার নাম বরকতময়, আপনার মর্যাদা অতি উচ্চ এবং আপনি ব্যতীত কোনো সত্য উপাস্য নেই।"</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 4: Interactive Visual Hajj & Umrah Journey
           ------------------------------------------------------------------- -->
      <div id="visual-hajj" class="project-row reversed">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">সচিত্র হজ্ব ও উমরাহ গাইড - ১২টি ধারাবাহিক ধাপ</span>
            <span class="en-only">Interactive Visual Hajj & Umrah Guide</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">বাস্তব আলোকচিত্র ও ধারাবাহিক ধাপসহ হজ্বের পূর্ণাঙ্গ নির্দেশিকা</span>
            <span class="en-only">12 Chronological Stages with Authentic Real HD Photos</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">ইহরাম গ্রহণ থেকে শুরু করে তাওয়াফে বিদা পর্যন্ত রাসূলুল্লাহর (ﷺ) বিদায় হজ্জের পুঙ্খানুপুঙ্খ বিবরণ। উচ্চ রেজ্যুলেশনের বাস্তব আলোকচিত্র, প্রতিটি পর্বের মাসনূন দু'আ, মানচিত্র এবং অফলাইন গ্লাইড ক্যাশিং সমন্বয়ে প্রস্তুত।</span>
            <span class="en-only">Follow the Prophet's (ﷺ) pilgrimage step-by-step with 12 interactive stages. Includes real high-resolution photographs, authentic supplications, spatial orientation maps, and complete offline capability.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-route subfeat-icon"></i>
              <div class="subfeat-title bn-only">১২টি ধারাবাহিক পর্যায়</div>
              <div class="subfeat-title en-only">12 Step-by-Step Stages</div>
              <div class="subfeat-desc bn-only">মীকাত, মিনা, আরাফাহ, মুযদালিফাহ ও জামারাতের সকল বিধান ক্রমানুসারে সাজানো।</div>
              <div class="subfeat-desc en-only">From Miqat, Mina, Day of Arafah to Muzdalifah and Jamarat stoning in order.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-camera subfeat-icon"></i>
              <div class="subfeat-title bn-only">বাস্তব আলোকচিত্র ও ম্যাপ</div>
              <div class="subfeat-title en-only">Real HD Photography</div>
              <div class="subfeat-desc bn-only">পবিত্র মক্কা ও মাশায়েরের আসল ছবি যা হাজীদের পথচলাকে স্পষ্ট ও সহজ করে তোলে।</div>
              <div class="subfeat-desc en-only">Authentic field photos and maps guiding pilgrims through key historical locations.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-hands-praying subfeat-icon"></i>
              <div class="subfeat-title bn-only">বিশুদ্ধ মাসনূন দু'আ ও অর্থ</div>
              <div class="subfeat-title en-only">Authentic Masnoon Duas</div>
              <div class="subfeat-desc bn-only">প্রতিটি স্থানের জন্য হাদিস বর্ণিত নির্দিষ্ট দু'আ আরবি, বাংলা উচ্চারণ ও অর্থসহ।</div>
              <div class="subfeat-desc en-only">Supplications verified from Sunnah for every ritual with translations.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-cloud-arrow-down subfeat-icon"></i>
              <div class="subfeat-title bn-only">রোমিং ছাড়া অফলাইন ব্যবহার</div>
              <div class="subfeat-title en-only">Zero Roaming Data Needed</div>
              <div class="subfeat-desc bn-only">সৌদি আরবে ইন্টারনেট বা রোমিং ডাটা ছাড়াই সকল ছবি ও নির্দেশিকা সচল থাকে।</div>
              <div class="subfeat-desc en-only">Fully cached locally so pilgrims don't need roaming data while performing rituals.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo SITE_URL; ?>downloads/deenone.apk" class="btn-project-cta" download="deenone.apk">
              <i class="fa-solid fa-kaaba"></i>
              <span class="bn-only">হজ্ব গাইডটি পান (অ্যাপ)</span>
              <span class="en-only">Get Visual Hajj Companion</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card with Real Uploaded Hajj Photo -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header" style="background: #0F766E;">
                <span class="preview-mock-title bn-only">পর্যায় ৪: মায়দানে আরাফাত</span>
                <span class="preview-mock-title en-only">Stage 4: Day of Arafah</span>
                <span class="badge-grade">৯ই জিলহজ্জ</span>
              </div>
              <div class="preview-mock-body">
                <!-- Real Stage 4 Image from uploads -->
                <img src="<?php echo SITE_URL; ?>uploads/hajj_journey/img_hajj_journey_stage_4.png" alt="Arafat Hajj Stage" class="hajj-stage-banner" loading="lazy" decoding="async">
                <div class="preview-item">
                  <div class="preview-badge-row">
                    <span style="font-size: 11px; font-weight: 700; color: #0F766E;">আরাফাত দিবসের শ্রেষ্ঠ দু'আ</span>
                    <span class="badge-grade">তিরমিযী ৩৫৮৫</span>
                  </div>
                  <div class="preview-item-arabic">لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ</div>
                  <div class="preview-item-trans">"আল্লাহ ব্যতীত কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো শরিক নেই। রাজত্ব একমাত্র তাঁরই।"</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 5: Ummah Network & Community Hub
           ------------------------------------------------------------------- -->
      <div id="ummah-network" class="project-row">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">উম্মাহ নেটওয়ার্ক ও কমিউনিটি হাব (Ummah Network)</span>
            <span class="en-only">Ummah Network & Community Hub</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">মুসলিম উম্মাহর পারস্পরিক দ্বীনি সহযোগিতা, আমল ও উৎসাহের মেলবন্ধন</span>
            <span class="en-only">A Safe, Moderated Spiritual Platform for Knowledge & Brotherhood</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">বিশ্বব্যাপী মুসলিম ভাই ও বোনেদের মধ্যে খাঁটি দ্বীনি জ্ঞানের আদান-প্রদান, নেক আমলের অনুপ্রেরণা এবং জরুরি সহায়তায় পাশে দাঁড়ানোর এক নিরাপদ ডিজিটাল মিলনমেলা। বাণিজ্যিক বিজ্ঞাপন ও অপসংস্কৃতিমুক্ত এক বিশুদ্ধ ইসলামিক কমিউনিটি।</span>
            <span class="en-only">A verified, ad-free Islamic social feed connecting Muslims worldwide. Share authentic reflections, learn daily Masnoon deeds, seek verified religious knowledge, and support fellow believers in a spiritually uplifting, strictly moderated space.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-users subfeat-icon" style="color: #6366F1;"></i>
              <div class="subfeat-title bn-only">দ্বীনি আলোচনা ও জিজ্ঞাসা</div>
              <div class="subfeat-title en-only">Authentic Q&A & Advice</div>
              <div class="subfeat-desc bn-only">কুরআন ও সহীহ সুন্নাহর আলোকে প্রয়োজনীয় মাসআলা ও দ্বীনি বিষয়ে খোলামেলা আলোচনা।</div>
              <div class="subfeat-desc en-only">Ask questions and discuss everyday Islamic life guided by authentic Sunnah.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-heart-pulse subfeat-icon" style="color: #6366F1;"></i>
              <div class="subfeat-title bn-only">নেক আমল ও অনুপ্রেরণা</div>
              <div class="subfeat-title en-only">Daily Good Deeds Feed</div>
              <div class="subfeat-desc bn-only">দৈনন্দিন কুরআন তিলাওয়াত, নফল রোজা ও জিকিরের অনুপ্রেরণামূলক পোস্ট শেয়ারিং।</div>
              <div class="subfeat-desc en-only">Inspire and motivate one another with daily habits, fasting reminders, and Dhikr.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-shield-halved subfeat-icon" style="color: #6366F1;"></i>
              <div class="subfeat-title bn-only">১০০% নিরাপদ ও পরিচ্ছন্ন পরিবেশ</div>
              <div class="subfeat-title en-only">Safe & Spam-Free Network</div>
              <div class="subfeat-desc bn-only">কোনো অনৈতিক বিজ্ঞাপন, ট্রল বা বিভ্রান্তিকর কনটেন্টমুক্ত সার্বক্ষণিক মডারেটেড প্ল্যাটফর্ম।</div>
              <div class="subfeat-desc en-only">Strictly moderated and family-safe environment with zero ads and zero toxicity.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-cloud-arrow-up subfeat-icon" style="color: #6366F1;"></i>
              <div class="subfeat-title bn-only">তাৎক্ষণিক ক্লাউড সিঙ্ক</div>
              <div class="subfeat-title en-only">Instant Multi-Device Sync</div>
              <div class="subfeat-desc bn-only">আপনার পোস্ট, লাইক ও সেভ করা ইসলামিক রিসোর্স সব ডিভাইসে স্বয়ংক্রিয় সিঙ্ক।</div>
              <div class="subfeat-desc en-only">Saved posts, favorites, and discussions seamlessly synchronized across your devices.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo SITE_URL; ?>downloads/deenone.apk" class="btn-project-cta" style="background: #6366F1; box-shadow: 0 4px 14px rgba(99, 102, 241, 0.3);" download="deenone.apk">
              <i class="fa-solid fa-users"></i>
              <span class="bn-only">উম্মাহ নেটওয়ার্কে যোগ দিন</span>
              <span class="en-only">Join Ummah Network</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card with Interactive Community Mockup -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header" style="background: #6366F1;">
                <span class="preview-mock-title bn-only">উম্মাহ কমিউনিটি হাব</span>
                <span class="preview-mock-title en-only">Ummah Community Hub</span>
                <span class="badge-grade" style="background: #EEF2FF; color: #4F46E5;">সচল • Live</span>
              </div>
              <div class="preview-mock-body">
                <!-- Post 1 -->
                <div class="community-card-box">
                  <div class="community-user-header">
                    <div class="community-avatar-circle">তা</div>
                    <div class="community-user-meta">
                      <div class="community-author-name">তানভীর আহমেদ</div>
                      <div class="community-post-location bn-only">ঢাকা • আমল ও নসীহাহ • আজ বাদ মাগরিব</div>
                      <div class="community-post-location en-only">Dhaka • Good Deeds • Today</div>
                    </div>
                  </div>
                  <div class="community-post-content bn-only">
                    "রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি প্রতিদিন ১২ রাকাত সুন্নাত সালাত নিয়মিত আদায় করবে, তার জন্য জান্নাতে একটি ঘর নির্মাণ করা হবে।' (সহীহ মুসলিম)"
                  </div>
                  <div class="community-post-content en-only">
                    "The Messenger of Allah (ﷺ) said: 'Whoever prays twelve voluntary Rak'ahs daily, a house will be built for him in Paradise.' (Sahih Muslim)"
                  </div>
                  <div class="community-interactions-bar">
                    <div class="community-interact-item active">
                      <i class="fa-solid fa-heart"></i>
                      <span>৪৫ সুবহানআল্লাহ</span>
                    </div>
                    <div class="community-interact-item">
                      <i class="fa-solid fa-comment"></i>
                      <span>১২ মন্তব্য</span>
                    </div>
                    <div class="community-interact-item">
                      <i class="fa-solid fa-share-nodes"></i>
                      <span>শেয়ার</span>
                    </div>
                  </div>
                </div>

                <!-- Post 2 -->
                <div class="community-card-box" style="padding: 10px 12px;">
                  <div class="community-user-header">
                    <div class="community-avatar-circle" style="background: #059669;">মা</div>
                    <div class="community-user-meta">
                      <div class="community-author-name">মাহমুদুল হাসান</div>
                      <div class="community-post-location bn-only">চট্টগ্রাম • কুরআন তিলাওয়াত</div>
                      <div class="community-post-location en-only">Chittagong • Quran Circle</div>
                    </div>
                  </div>
                  <div class="community-post-content bn-only" style="font-size: 11.5px;">
                    "আলহামদুলিল্লাহ, আজকের সূরা কাহাফ তিলাওয়াত ও জুমার আমল সম্পন্ন। আল্লাহ সবার আমল কবুল করুন।"
                  </div>
                  <div class="community-post-content en-only" style="font-size: 11.5px;">
                    "Alhamdulillah, completed Surah Al-Kahf recitation today. May Allah accept our deeds."
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- -------------------------------------------------------------------
           PROJECT 6: Emergency Blood Donation Network
           ------------------------------------------------------------------- -->
      <div id="blood-network" class="project-row reversed">
        <!-- Text & Subfeatures -->
        <div class="project-content-col">
          <h3 class="project-title">
            <span class="bn-only">জরুরি রক্তদান নেটওয়ার্ক (Blood Network)</span>
            <span class="en-only">Emergency Blood Donor Directory</span>
          </h3>
          <p class="project-tagline">
            <span class="bn-only">জীবন বাঁচাতে নিঃস্বার্থ রক্তদাতাদের সাথে তাৎক্ষণিক সংযোগ</span>
            <span class="en-only">Connecting Patients with Verified Donors Across Bangladesh</span>
          </p>
          <p class="project-desc">
            <span class="bn-only">হাসপাতালে মুমূর্ষু রোগীর জরুরি প্রয়োজনে রক্ত সংগ্রহ করা এক জটিল পরীক্ষা। দীনওয়ান প্ল্যাটফর্মে ৬৪ জেলার প্রত্যন্ত অঞ্চল পর্যন্ত রক্তদাতারা স্বেচ্ছায় নিবন্ধিত হয়ে মানুষের জীবন রক্ষায় অবদান রাখছেন।</span>
            <span class="en-only">A free humanitarian directory bridging the gap between critical patients and voluntary blood donors. Filter by district, blood group, and contact verified donors directly without middle-men.</span>
          </p>

          <div class="project-features-grid">
            <div class="subfeat-card">
              <i class="fa-solid fa-map-pin subfeat-icon" style="color: #E11D48;"></i>
              <div class="subfeat-title bn-only">৬৪ জেলায় সার্বক্ষণিক সচল</div>
              <div class="subfeat-title en-only">64 Districts Network</div>
              <div class="subfeat-desc bn-only">জেলা ও উপজেলা ভিত্তিক রক্তদাতা খোঁজার উন্নত ফিল্টারিং সিস্টেম।</div>
              <div class="subfeat-desc en-only">Granular search allowing you to find donors in your specific sub-district.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-phone subfeat-icon" style="color: #E11D48;"></i>
              <div class="subfeat-title bn-only">সরাসরি ফোন কল ও যোগাযোগ</div>
              <div class="subfeat-title en-only">Direct One-Tap Calling</div>
              <div class="subfeat-desc bn-only">জরুরি প্রয়োজনে কোনো বিলম্ব ছাড়াই সরাসরি রক্তদাতার সাথে ফোনে কথা বলা।</div>
              <div class="subfeat-desc en-only">Call or WhatsApp verified donors instantly during life-threatening emergencies.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-user-plus subfeat-icon" style="color: #E11D48;"></i>
              <div class="subfeat-title bn-only">স্বেচ্ছায় রক্তদাতা নিবন্ধন</div>
              <div class="subfeat-title en-only">Easy Donor Registration</div>
              <div class="subfeat-desc bn-only">যেকোনো সুস্থ মুসলিম ভাই বা বোন সহজেই রক্তদাতা হিসেবে নাম নথিভুক্ত করতে পারেন।</div>
              <div class="subfeat-desc en-only">Register as a voluntary donor and update your last donation date easily.</div>
            </div>

            <div class="subfeat-card">
              <i class="fa-solid fa-hand-holding-heart subfeat-icon" style="color: #E11D48;"></i>
              <div class="subfeat-title bn-only">১০০% ফ্রি মানবিক খেদমত</div>
              <div class="subfeat-title en-only">100% Free & Open</div>
              <div class="subfeat-desc bn-only">কোনো চার্জ বা ফি নেই, সম্পূর্ণ নিঃস্বার্থ মানবিক ও ইসলামিক কল্যাণমূলক উদ্যোগ।</div>
              <div class="subfeat-desc en-only">Completely non-profit and dedicated strictly for saving human lives.</div>
            </div>
          </div>

          <div class="project-action-row">
            <a href="<?php echo SITE_URL; ?>downloads/deenone.apk" class="btn-project-cta" style="background: #E11D48; box-shadow: 0 4px 14px rgba(225, 29, 72, 0.3);" download="deenone.apk">
              <i class="fa-solid fa-droplet"></i>
              <span class="bn-only">রক্তদাতা নেটওয়ার্কে যুক্ত হোন</span>
              <span class="en-only">Join Blood Network</span>
            </a>
          </div>
        </div>

        <!-- Visual Preview Card -->
        <div class="project-visual-col">
          <div class="project-visual-card">
            <div class="project-preview-mock">
              <div class="preview-mock-header" style="background: #E11D48;">
                <span class="preview-mock-title">জরুরি রক্তদাতা সন্ধান</span>
                <span class="badge-grade" style="background: #FFE4E6; color: #E11D48;">Verified</span>
              </div>
              <div class="preview-mock-body">
                <div class="preview-item" style="border-left: 4px solid #E11D48;">
                  <div class="preview-badge-row">
                    <span style="font-size: 15px; font-weight: 800; color: #E11D48;">O+ (পজিটিভ)</span>
                    <span style="font-size: 11px; font-weight: 700; color: var(--primary);">রক্তদানে প্রস্তুত</span>
                  </div>
                  <div style="font-size: 13.5px; font-weight: 800; color: var(--text-heading);">মোহাম্মদ আব্দুল্লাহ</div>
                  <div style="font-size: 11.5px; color: var(--text-body);">মিরপুর-১০, ঢাকা • শেষ দান: ৪ মাস আগে</div>
                </div>

                <div class="preview-item" style="border-left: 4px solid #E11D48;">
                  <div class="preview-badge-row">
                    <span style="font-size: 15px; font-weight: 800; color: #E11D48;">B+ (পজিটিভ)</span>
                    <span style="font-size: 11px; font-weight: 700; color: var(--primary);">রক্তদানে প্রস্তুত</span>
                  </div>
                  <div style="font-size: 13.5px; font-weight: 800; color: var(--text-heading);">মুশফিকুর রহমান</div>
                  <div style="font-size: 11.5px; color: var(--text-body);">ধানমন্ডি, ঢাকা • শেষ দান: ৬ মাস আগে</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </section>

  <!-- =========================================================================
       CORE VALUES & WHY DEENONE (Integrity, Authenticity, 60 FPS Performance)
       ========================================================================= -->
  <section id="why-deenone" class="values-section">
    <div class="container">
      
      <div class="section-tag-center">
        <span class="section-pill-tag">
          <span class="bn-only">আমাদের প্রতিশ্রুতি ও মানদণ্ড</span>
          <span class="en-only">WHY DEENONE?</span>
        </span>
      </div>

      <h2 class="stats-main-heading">
        <span class="bn-only">যে কারণে কোটি মুসলিমের প্রথম পছন্দ দীনওয়ান</span>
        <span class="en-only">Built Upon Integrity, Authenticity & Technology</span>
      </h2>

      <div class="values-grid">
        <!-- Card 1: Zero Commercial Ads -->
        <div class="value-card">
          <div class="value-icon-box">
            <i class="fa-solid fa-rectangle-xmark"></i>
          </div>
          <h3 class="value-card-title bn-only">বিজ্ঞাপনহীন স্বচ্ছতা</h3>
          <h3 class="value-card-title en-only">100% Ads Free</h3>
          <p class="value-card-desc bn-only">
            ইবাদত ও কুরআন-হাদিস অধ্যয়নে কোনো বাণিজ্যিক ব্যানার, পপ-আপ বা অপ্রাসঙ্গিক বিজ্ঞাপন নেই।
          </p>
          <p class="value-card-desc en-only">
            No disruptive commercial banners, interstitial pop-ups, or third-party tracking scripts.
          </p>
        </div>

        <!-- Card 2: Authentic Sources -->
        <div class="value-card">
          <div class="value-icon-box">
            <i class="fa-solid fa-certificate"></i>
          </div>
          <h3 class="value-card-title bn-only">বিশুদ্ধতার নিশ্চয়তা</h3>
          <h3 class="value-card-title en-only">Authentic Sources</h3>
          <p class="value-card-desc bn-only">
            কুরআন ও সহীহ সুন্নাহর ভিত্তিতে স্বীকৃত মুহাদিসগণের তাহকীক ও বিশ্বস্ত প্রকাশনীর রেফারেন্স।
          </p>
          <p class="value-card-desc en-only">
            Grounded in Quran and Sunnah, adhering strictly to verifiable tahqeeq by recognized scholars.
          </p>
        </div>

        <!-- Card 3: Lag-free 60 FPS Performance -->
        <div class="value-card">
          <div class="value-icon-box">
            <i class="fa-solid fa-bolt-lightning"></i>
          </div>
          <h3 class="value-card-title bn-only">ল্যাগ-মুক্ত ৬০ FPS গতি</h3>
          <h3 class="value-card-title en-only">Lag-free 60 FPS UI</h3>
          <p class="value-card-desc bn-only">
            স্মার্ট ক্যাশিং ও অপ্টিমাইজড মেমোরি ম্যানেজমেন্টের মাধ্যমে লো-এন্ড ফোনেও সুপার-ফাস্ট রেসপন্স।
          </p>
          <p class="value-card-desc en-only">
            Engineered with smart lazy loading and memory recycling to deliver instantaneous, buttery-smooth interactions.
          </p>
        </div>

        <!-- Card 4: Absolute Privacy -->
        <div class="value-card">
          <div class="value-icon-box">
            <i class="fa-solid fa-user-shield"></i>
          </div>
          <h3 class="value-card-title bn-only">সর্বোচ্চ তথ্য সুরক্ষা</h3>
          <h3 class="value-card-title en-only">Absolute Privacy</h3>
          <p class="value-card-desc bn-only">
            আপনার ব্যক্তিগত তথ্যের পূর্ণ নিরাপত্তা। কোনো প্রকার থার্ড-পার্টি ট্র্যাকিং বা ডাটা শেয়ারিং নিষিদ্ধ।
          </p>
          <p class="value-card-desc en-only">
            Your personal worship activity and location remain private. Zero data monetization.
          </p>
        </div>
      </div>

    </div>
  </section>

  <!-- =========================================================================
       BE A PART OF SADAQAH JARIYAH (Inspiring CTA Banner)
       ========================================================================= -->
  <section class="cta-sadaqah-section">
    <div class="container">
      <div class="cta-card-box">
        
        <h2 class="cta-headline">
          <span class="bn-only">সদকায়ে জারিয়ার অংশীদার হোন</span>
          <span class="en-only">Be a Part of Sadaqah Jariyah</span>
        </h2>

        <p class="cta-desc">
          <span class="bn-only">দীনওয়ান একটি উন্মুক্ত ও অলাভজনক দ্বীনি খেদমত। আপনার সামান্য আন্তরিক সদকাহ কোটি মানুষের কাছে কুরআন ও সহীহ সুন্নাহর আলো পৌঁছে দেওয়ার পথকে সুগম করে। দ্বীনি গবেষণা, সার্বক্ষণিক সার্ভার পরিচালনা ও আধুনিকায়নে আপনার হাত প্রসারিত করুন।</span>
          <span class="en-only">DeenOne is a non-profit open Islamic initiative. Your voluntary contribution directly supports high-speed server operations, scholarly research, and continuous app development for the global Muslim community.</span>
        </p>

        <div>
          <a href="<?php echo SITE_URL; ?>donate/" class="btn-cta-donate" target="_blank">
            <i class="fa-solid fa-heart"></i>
            <span class="bn-only">আমি সাহায্য করতে চাই</span>
            <span class="en-only">I Want to Support</span>
          </a>
        </div>

      </div>
    </div>
  </section>

  <!-- =========================================================================
       FOOTER (Structured 4-Column Responsive Layout)
       ========================================================================= -->
  <footer class="site-footer">
    <div class="container">
      <div class="footer-grid">

        <!-- Column 1: Brand & Mission -->
        <div class="footer-col">
          <a href="<?php echo SITE_URL; ?>" class="brand-logo" aria-label="DeenOne" style="margin-bottom: 16px;"><span class="brand-green">Deen</span><span class="brand-white">One</span></a>
          <p class="footer-bio-text bn-only">
            দীনওয়ান প্রযুক্তি ও বিশুদ্ধ জ্ঞানের সমন্বয়ে মানবজাতির কল্যাণে কাজ করে যাচ্ছে, একমাত্র আল্লাহর সন্তুষ্টির উদ্দেশ্যে।
          </p>
          <p class="footer-bio-text en-only">
            DeenOne provides open Islamic technology and verified knowledge for the benefit of humanity, seeking rewards from Allah alone.
          </p>

          <div class="footer-download-label bn-only">অ্যাপ ডাউনলোড করুন:</div>
          <div class="footer-download-label en-only">Download Our Apps:</div>
          <div class="footer-download-btns">
            <a href="<?php echo htmlspecialchars($apkDownloadUrl); ?>" class="btn-download-pill" style="min-width: unset; padding: 6px 14px;" download="deenone.apk">
              <i class="fa-brands fa-android" style="font-size: 18px; color: var(--primary);"></i>
              <span style="font-size: 12px; font-weight: 800;">APK File</span>
            </a>
            <a href="<?php echo htmlspecialchars($playStoreUrl); ?>" target="_blank" class="btn-download-pill" style="min-width: unset; padding: 6px 14px;">
              <i class="fa-brands fa-google-play" style="font-size: 16px; color: var(--primary);"></i>
              <span style="font-size: 12px; font-weight: 800;">Play Store</span>
            </a>
          </div>
        </div>

        <!-- Column 2: Our Projects -->
        <div class="footer-col">
          <h4 class="footer-col-title">
            <span class="bn-only">প্রজেক্টসমূহ</span>
            <span class="en-only">Our Projects</span>
          </h4>
          <ul class="footer-links-list">
            <li class="footer-link-item"><a href="#app-deenone"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">দীনওয়ান অ্যাপ</span><span class="en-only">DeenOne App</span></a></li>
            <li class="footer-link-item"><a href="#al-hadith"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">আল-হাদিস ডাটাবেজ</span><span class="en-only">Al Hadith Database</span></a></li>
            <li class="footer-link-item"><a href="#visual-salah"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">সচিত্র সালাত গাইড</span><span class="en-only">Visual Salah Guide</span></a></li>
            <li class="footer-link-item"><a href="#visual-hajj"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">ভিজ্যুয়াল হজ্ব গাইড</span><span class="en-only">Visual Hajj Guide</span></a></li>
            <li class="footer-link-item"><a href="#ummah-network"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">উম্মাহ নেটওয়ার্ক</span><span class="en-only">Ummah Network</span></a></li>
            <li class="footer-link-item"><a href="#blood-network"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">রক্তদান নেটওয়ার্ক</span><span class="en-only">Blood Network</span></a></li>
          </ul>
        </div>

        <!-- Column 3: Important Links -->
        <div class="footer-col">
          <h4 class="footer-col-title">
            <span class="bn-only">গুরুত্বপূর্ণ লিংক</span>
            <span class="en-only">Quick Links</span>
          </h4>
          <ul class="footer-links-list">
            <li class="footer-link-item"><a href="#home"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">হোম</span><span class="en-only">Home</span></a></li>
            <li class="footer-link-item"><a href="#stats"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">পরিসংখ্যান</span><span class="en-only">Statistics</span></a></li>
            <li class="footer-link-item"><a href="#why-deenone"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">কেন দীনওয়ান</span><span class="en-only">Why DeenOne</span></a></li>
            <li class="footer-link-item"><a href="<?php echo SITE_URL; ?>donate/" target="_blank"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">সদকাহ ও দান</span><span class="en-only">Sadaqah & Support</span></a></li>
            <li class="footer-link-item"><a href="<?php echo SITE_URL; ?>privacy/" target="_blank"><i class="fa-solid fa-angle-right" style="font-size: 11px; color: var(--primary);"></i> <span class="bn-only">গোপনীয়তা নীতি</span><span class="en-only">Privacy Policy</span></a></li>
          </ul>
        </div>

        <!-- Column 4: Contact & Social -->
        <div class="footer-col">
          <h4 class="footer-col-title">
            <span class="bn-only">যোগাযোগ ও সোশ্যাল</span>
            <span class="en-only">Contact & Social</span>
          </h4>
          <p class="footer-contact-info">
            <span class="bn-only">যেকোনো জিজ্ঞাসা, কন্টেন্টে ভুল সংশোধন কিংবা পরামর্শের জন্য আমাদের ইমেইল করতে পারেন:</span>
            <span class="en-only">For queries, content feedback, or suggestions, reach us at:</span>
            <br>
            <a href="mailto:support@deenone.top">support@deenone.top</a>
          </p>

          <div class="footer-social-row">
            <a href="<?php echo htmlspecialchars($socialFacebook); ?>" target="_blank" class="social-icon-btn" aria-label="Facebook">
              <i class="fa-brands fa-facebook-f"></i>
            </a>
            <a href="<?php echo htmlspecialchars($socialTelegram); ?>" target="_blank" class="social-icon-btn" aria-label="Telegram">
              <i class="fa-brands fa-telegram"></i>
            </a>
            <a href="<?php echo htmlspecialchars($socialYoutube); ?>" target="_blank" class="social-icon-btn" aria-label="YouTube">
              <i class="fa-brands fa-youtube"></i>
            </a>
            <a href="<?php echo htmlspecialchars($socialGithub); ?>" target="_blank" class="social-icon-btn" aria-label="GitHub">
              <i class="fa-brands fa-github"></i>
            </a>
          </div>
        </div>

      </div>

      <!-- Bottom Bar -->
      <div class="footer-bottom-bar">
        <div>
          Copyright &copy; <?php echo date('Y'); ?> <strong>DeenOne</strong>. All Rights Reserved.
        </div>
        <div class="footer-credits">
          <span>Dedicated for the pleasure of Allah alone</span>
          <i class="fa-solid fa-heart heart-icon"></i>
        </div>
      </div>

    </div>
  </footer>

  <!-- =========================================================================
       INTERACTIVE JAVASCRIPT (Language Switcher, Theme Switcher & Mobile Menu)
       ========================================================================= -->
  <script>
    (function() {
      // 1. Theme Toggle System
      const themeToggleBtn = document.getElementById('themeToggleBtn');
      const htmlEl = document.documentElement;

      function setTheme(newTheme) {
        htmlEl.setAttribute('data-theme', newTheme);
        localStorage.setItem('deenone_theme', newTheme);
        document.cookie = "deenone_theme=" + newTheme + "; path=/; max-age=31536000";
        
        // Update toggle icon
        if (themeToggleBtn) {
          const icon = themeToggleBtn.querySelector('i');
          if (icon) {
            icon.className = (newTheme === 'dark') ? 'fa-solid fa-sun' : 'fa-solid fa-moon';
          }
        }
      }

      // Initialize theme icon
      const currentTheme = htmlEl.getAttribute('data-theme') || 'light';
      setTheme(currentTheme);

      if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', function() {
          const activeTheme = htmlEl.getAttribute('data-theme') || 'light';
          const nextTheme = (activeTheme === 'dark') ? 'light' : 'dark';
          setTheme(nextTheme);
        });
      }

      // 2. Language Toggle System
      const langToggleBtn = document.getElementById('langToggleBtn');

      function setLanguage(newLang) {
        htmlEl.setAttribute('data-lang', newLang);
        htmlEl.setAttribute('lang', newLang);
        localStorage.setItem('deenone_lang', newLang);
        document.cookie = "deenone_lang=" + newLang + "; path=/; max-age=31536000";
      }

      if (langToggleBtn) {
        langToggleBtn.addEventListener('click', function() {
          const activeLang = htmlEl.getAttribute('data-lang') || 'bn';
          const nextLang = (activeLang === 'bn') ? 'en' : 'bn';
          setLanguage(nextLang);
        });
      }

      // 3. Mobile Navigation Drawer
      const hamburgerBtn = document.getElementById('hamburgerBtn');
      const closeDrawerBtn = document.getElementById('closeDrawerBtn');
      const mobileDrawer = document.getElementById('mobileDrawer');
      const drawerBackdrop = document.getElementById('drawerBackdrop');
      const drawerNavLinks = document.querySelectorAll('.drawer-nav-link');

      function openDrawer() {
        if (mobileDrawer) mobileDrawer.classList.add('open');
        if (drawerBackdrop) drawerBackdrop.classList.add('open');
        document.body.style.overflow = 'hidden';
      }

      function closeDrawer() {
        if (mobileDrawer) mobileDrawer.classList.remove('open');
        if (drawerBackdrop) drawerBackdrop.classList.remove('open');
        document.body.style.overflow = '';
      }

      if (hamburgerBtn) hamburgerBtn.addEventListener('click', openDrawer);
      if (closeDrawerBtn) closeDrawerBtn.addEventListener('click', closeDrawer);
      if (drawerBackdrop) drawerBackdrop.addEventListener('click', closeDrawer);

      drawerNavLinks.forEach(function(link) {
        link.addEventListener('click', closeDrawer);
      });

      // 4. Smooth Anchor Scrolling & Active Link Highlighting
      document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function(e) {
          const targetId = this.getAttribute('href');
          if (targetId && targetId !== '#') {
            const targetEl = document.querySelector(targetId);
            if (targetEl) {
              e.preventDefault();
              targetEl.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
          }
        });
      });

    })();
  </script>
</body>
</html>
