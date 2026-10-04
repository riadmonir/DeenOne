<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - COMMON HEADER & SIDEBAR NAVIGATION (ADVANCED SUITE)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$adminUser = getAdminUser();
$flash = getFlash();

if (!isset($pageTitle)) {
    $pageTitle = 'অ্যাডমিন ড্যাশবোর্ড';
}
if (!isset($activeNav)) {
    $activeNav = 'dashboard';
}

$pdo = null;
try {
    $pdo = getDbConnection();
} catch (Throwable $e) {}

// Fetch quick pending counts for badges
$pendingReportsCount = 0;
$activeBattlesCount = 0;
$totalUsersCount = 0;
$totalQuestionsCount = 0;
$openBloodRequestsCount = 0;
$totalDonorsCount = 0;
$pendingScrapedHadithsCount = 0;
$pendingDonationsCount = 0;

if ($pdo) {
    try {
        $rStmt = $pdo->query("SELECT COUNT(*) FROM community_reports WHERE status = 'PENDING'");
        if ($rStmt) $pendingReportsCount = (int) $rStmt->fetchColumn();

        $bStmt = $pdo->query("SELECT COUNT(*) FROM battle_rooms WHERE lifecycle_state != 'COMPLETED'");
        if ($bStmt) $activeBattlesCount = (int) $bStmt->fetchColumn();

        $uStmt = $pdo->query("SELECT COUNT(*) FROM users");
        if ($uStmt) $totalUsersCount = (int) $uStmt->fetchColumn();

        $qStmt = $pdo->query("SELECT COUNT(*) FROM quiz_questions");
        if ($qStmt) $totalQuestionsCount = (int) $qStmt->fetchColumn();

        $bReqStmt = $pdo->query("SELECT COUNT(*) FROM blood_requests WHERE status = 'OPEN'");
        if ($bReqStmt) $openBloodRequestsCount = (int) $bReqStmt->fetchColumn();

        $bDonStmt = $pdo->query("SELECT COUNT(*) FROM blood_donors");
        if ($bDonStmt) $totalDonorsCount = (int) $bDonStmt->fetchColumn();

        $sHadithStmt = $pdo->query("SELECT COUNT(*) FROM hadith_scraped_staging WHERE status = 'pending'");
        if ($sHadithStmt) $pendingScrapedHadithsCount = (int) $sHadithStmt->fetchColumn();

        $donStmt = $pdo->query("SELECT COUNT(*) FROM donations WHERE status = 'PENDING'");
        if ($donStmt) $pendingDonationsCount = (int) $donStmt->fetchColumn();
    } catch (Throwable $e) {
        // Ignore badge count failures gracefully
    }
}

// Fetch Branding & App Settings
$appSettings = [];
if ($pdo) {
    try {
        $sStmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN ('app_name', 'app_tagline', 'admin_font_family', 'admin_bangla_font', 'admin_arabic_font', 'site_favicon_url')");
        if ($sStmt) {
            while ($row = $sStmt->fetch()) {
                $appSettings[$row['setting_key']] = $row['setting_value'];
            }
        }
    } catch (Throwable $e) {}
}

// Admin Typography Definitions & Dynamic Multi-Language Font Mapping (Defaulting to: Roboto & Kalpurush)
$selectedEnglishFontKey = $appSettings['admin_font_family'] ?? 'roboto';
$selectedBanglaFontKey = $appSettings['admin_bangla_font'] ?? 'kalpurush';
$selectedArabicFontKey = $appSettings['admin_arabic_font'] ?? 'amiri';

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

$fontFamilyMap = $englishFontMap;
$activeFontEnCss = $englishFontMap[$selectedEnglishFontKey] ?? $englishFontMap['roboto'];
$activeFontBnCss = $banglaFontMap[$selectedBanglaFontKey] ?? $banglaFontMap['kalpurush'];
$activeFontArCss = $arabicFontMap[$selectedArabicFontKey] ?? $arabicFontMap['amiri'];
$activeFontMainCss = "{$activeFontEnCss}, {$activeFontBnCss}, {$activeFontArCss}, -apple-system, BlinkMacSystemFont, sans-serif";

// Format Bengali Current Date
$bnMonths = ["জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"];
$bnNumbers = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
$currentDay = str_replace(range(0, 9), $bnNumbers, date('d'));
$currentMonth = $bnMonths[(int)date('m') - 1];
$currentYear = str_replace(range(0, 9), $bnNumbers, date('Y'));
$gregorianDateBn = "{$currentDay} {$currentMonth} {$currentYear}";
$currentDateFormatted = isEn() ? date('d M, Y') : $gregorianDateBn;
$currentHijriFormatted = getIslamicHijriDate();
?>
<!DOCTYPE html>
<html lang="<?php echo getAdminLang(); ?>">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><?php echo htmlspecialchars($pageTitle); ?> | <?php echo __('দীন ওয়ান অ্যাডমিন হাব', 'DeenOne Admin Hub'); ?></title>
  
  <!-- Tailwind CSS Play CDN & Typography Extension -->
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      darkMode: 'class',
      theme: {
        extend: {
          fontFamily: {
            sans: ['var(--font-main)', 'Poppins', 'Kalpurush', 'Inter', 'Noto Sans Bengali', 'Arial', 'sans-serif'],
            bengali: ['var(--font-bengali)', 'Kalpurush', 'Noto Sans Bengali', 'Hind Siliguri', 'sans-serif'],
            arabic: ['var(--font-arabic)', 'Amiri', 'Scheherazade New', 'serif'],
            poppins: ['Poppins', 'Kalpurush', 'sans-serif'],
            kalpurush: ['Kalpurush', 'SolaimanLipi', 'Noto Sans Bengali', 'sans-serif'],
            inter: ['Inter', 'Kalpurush', 'sans-serif'],
            notobangla: ['Noto Sans Bengali', 'Inter', 'sans-serif'],
            notoserif: ['Noto Serif Bengali', 'Times New Roman', 'serif'],
            arial: ['Arial', 'Helvetica', 'sans-serif'],
            roboto: ['Roboto', 'sans-serif'],
            outfit: ['Outfit', 'sans-serif'],
            jakarta: ['Plus Jakarta Sans', 'sans-serif'],
            hindsiliguri: ['Hind Siliguri', 'sans-serif'],
            anek: ['Anek Bangla', 'sans-serif'],
          },
          colors: {
            brand: {
              50: '#ecfdf5',
              100: '#d1fae5',
              200: '#a7f3d0',
              300: '#6ee7b7',
              400: '#19C994',
              500: '#17B686',
              600: '#10AA7B',
              700: '#0A5740',
              800: '#064e3b',
              900: '#033327',
            },
            sunnah: {
              50: '#fffbeb',
              100: '#fef3c7',
              200: '#fde68a',
              300: '#fcd34d',
              400: '#fbbf24',
              500: '#f59e0b',
              600: '#d97706',
              700: '#b45309',
              800: '#92400e',
              900: '#78350f',
            },
            night: {
              deep: '#141A2E',
              main: '#1A2039',
              sidebar: '#151A2E',
              card: '#202746',
              cardhover: '#262E52',
              input: '#151A2E',
              border: 'rgba(255, 255, 255, 0.08)',
            }
          }
        }
      }
    };
  </script>
  <script>
    // Immediate Theme & Multi-Font Pre-Render Initialization
    (function() {
      function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
      }
      const savedTheme = localStorage.getItem('deenone_admin_theme') || localStorage.getItem('deenone_theme') || getCookie('deenone_admin_theme') || getCookie('deenone_theme') || 'light';
      if (savedTheme === 'dark') {
        document.documentElement.classList.add('dark');
        document.documentElement.setAttribute('data-theme', 'dark');
        if (document.body) document.body.classList.add('theme-dark');
      } else {
        document.documentElement.classList.remove('dark');
        document.documentElement.setAttribute('data-theme', 'light');
        if (document.body) document.body.classList.remove('theme-dark');
      }

      const enMap = {
        'poppins': "'Poppins', sans-serif",
        'inter': "'Inter', sans-serif",
        'plus_jakarta_sans': "'Plus Jakarta Sans', sans-serif",
        'roboto': "'Roboto', sans-serif",
        'outfit': "'Outfit', sans-serif",
        'arial': "Arial, Helvetica, sans-serif"
      };
      const bnMap = {
        'kalpurush': "'Kalpurush', 'SolaimanLipi', 'Noto Sans Bengali', sans-serif",
        'noto_sans_bengali': "'Noto Sans Bengali', sans-serif",
        'hind_siliguri': "'Hind Siliguri', sans-serif",
        'anek_bangla': "'Anek Bangla', sans-serif",
        'noto_serif_bengali': "'Noto Serif Bengali', serif"
      };
      const arMap = {
        'amiri': "'Amiri', serif",
        'scheherazade': "'Scheherazade New', 'Amiri', serif"
      };

      const savedEn = localStorage.getItem('deenone_admin_font');
      if (savedEn && enMap[savedEn]) {
        document.documentElement.style.setProperty('--font-english', enMap[savedEn]);
      }
      const savedBn = localStorage.getItem('deenone_admin_bangla_font');
      if (savedBn && bnMap[savedBn]) {
        document.documentElement.style.setProperty('--font-bengali', bnMap[savedBn]);
      }
      const savedAr = localStorage.getItem('deenone_admin_arabic_font');
      if (savedAr && arMap[savedAr]) {
        document.documentElement.style.setProperty('--font-arabic', arMap[savedAr]);
      }

      const activeEn = (savedEn && enMap[savedEn]) ? enMap[savedEn] : "<?php echo addslashes($activeFontEnCss); ?>";
      const activeBn = (savedBn && bnMap[savedBn]) ? bnMap[savedBn] : "<?php echo addslashes($activeFontBnCss); ?>";
      const activeAr = (savedAr && arMap[savedAr]) ? arMap[savedAr] : "<?php echo addslashes($activeFontArCss); ?>";
      const activeMain = `${activeEn}, ${activeBn}, ${activeAr}, -apple-system, BlinkMacSystemFont, sans-serif`;
      document.documentElement.style.setProperty('--font-main', activeMain);
    })();
  </script>

  <!-- Google Fonts Preconnect & Rich Multi-Font Loading Suite (Roboto, Inter, Noto Sans Bengali, Hind Siliguri, Anek Bangla, Plus Jakarta Sans, Outfit, Poppins, Amiri, Scheherazade New) -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Anek+Bangla:wght@400;500;600;700;800&family=Hind+Siliguri:wght@400;500;600;700&family=Inter:wght@400;500;600;700;800&family=Noto+Sans+Bengali:wght@400;500;600;700;800&family=Noto+Serif+Bengali:wght@400;600;700&family=Outfit:wght@400;500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Poppins:wght@400;500;600;700&family=Roboto:wght@300;400;500;700&family=Amiri:wght@400;700&family=Scheherazade+New:wght@400;700&display=swap">

  <!-- FontAwesome 6 Pro CDN with jsDelivr High Reliability Fallback -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.1/css/all.min.css">
  
  <!-- Animate.css CDN -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">

  <!-- Custom Core Design System with Dynamic Version Cache Buster & Safe Path -->
  <?php 
    $rawBase = dirname($_SERVER['SCRIPT_NAME'] ?? '/admin');
    $adminBase = rtrim(str_replace('\\', '/', $rawBase), '/');
    if ($adminBase === '' || $adminBase === '.') $adminBase = '';
    $cssFile = __DIR__ . '/assets/css/admin.css';
    $cssVersion = file_exists($cssFile) ? filemtime($cssFile) : time();
    $cssHref = ($adminBase !== '' ? $adminBase : '.') . '/assets/css/admin.css?v=' . $cssVersion;
  ?>
  <link rel="stylesheet" href="<?php echo htmlspecialchars($cssHref); ?>">
  
  <!-- Critical Dynamic Typography & Bulletproof Anti-FOUC Inline CSS (Higher Cascade Precedence) -->
  <style>
    @font-face {
      font-family: 'Kalpurush';
      font-style: normal;
      font-weight: 100 900;
      font-display: swap;
      src: url('../assets/fonts/kalpurush.woff2') format('woff2'),
           url('../assets/fonts/kalpurush.ttf') format('truetype'),
           url('/assets/fonts/kalpurush.woff2') format('woff2'),
           url('/assets/fonts/kalpurush.ttf') format('truetype'),
           url('https://fonts.maateen.me/kalpurush/Kalpurush-v0.258.woff2') format('woff2');
    }
    :root {
      --font-english: <?php echo $activeFontEnCss; ?>;
      --font-bengali: <?php echo $activeFontBnCss; ?>;
      --font-arabic: <?php echo $activeFontArCss; ?>;
      --font-main: var(--font-english), var(--font-bengali), var(--font-arabic), -apple-system, BlinkMacSystemFont, sans-serif;
    }
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
    html, body { min-height: 100vh; width: 100%; font-family: var(--font-main) !important; }
    input, button, select, textarea, table, th, td, h1, h2, h3, h4, h5, h6, p, span, a, div, label {
      font-family: inherit;
    }
    .font-bengali, .bangla-text { font-family: var(--font-bengali), var(--font-main) !important; }
    .font-arabic, .quran-text, .hadith-arabic, .arabic-text, [dir="rtl"] { font-family: var(--font-arabic), serif !important; }
    .font-english, .english-text { font-family: var(--font-english), sans-serif !important; }
    
    .admin-layout { display: flex; width: 100%; min-height: 100vh; position: relative; }
    .sidebar { width: 280px; flex-shrink: 0; position: fixed; top: 0; bottom: 0; left: 0; z-index: 100; transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1); }
    .main-wrapper { flex: 1; margin-left: 280px; width: calc(100% - 280px); min-width: 0; display: flex; flex-direction: column; min-height: 100vh; }
    .topbar { height: 70px; display: flex; align-items: center; justify-content: space-between; padding: 0 28px; position: sticky; top: 0; z-index: 90; }
    .content-body { flex: 1; padding: 24px; min-width: 0; }
    .nav-accordion-children, .topbar-dropdown-menu { display: none !important; }
    .nav-accordion-group.open .nav-accordion-children { display: flex !important; }
    .topbar-dropdown-menu.show { display: block !important; }

    @media (max-width: 1024px) {
      .sidebar { transform: translateX(-100%); }
      .sidebar.open { transform: translateX(0); box-shadow: 10px 0 50px rgba(0, 0, 0, 0.5); }
      .main-wrapper { margin-left: 0 !important; width: 100% !important; max-width: 100% !important; }
      .topbar { padding: 0 16px; height: 64px; }
      .content-body { padding: 18px 14px; }
    }
    @media (max-width: 640px) {
      .topbar { padding: 0 12px; height: 58px; }
      .content-body { padding: 14px 10px; }
    }
  </style>
  <?php 
    $adminFavicon = !empty($appSettings['site_favicon_url']) ? $appSettings['site_favicon_url'] : '';
    if (!empty($adminFavicon)) {
        if (!preg_match('~^https?://~i', $adminFavicon) && !str_starts_with($adminFavicon, '/')) {
            $adminFavicon = ($adminBase !== '' ? $adminBase : '.') . '/../' . ltrim($adminFavicon, '/');
        }
    }
  ?>
  <?php if (!empty($adminFavicon)): ?>
    <link rel="icon" href="<?php echo htmlspecialchars($adminFavicon); ?>">
  <?php else: ?>
    <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🌙</text></svg>">
  <?php endif; ?>
</head>
<body>
<div class="admin-layout">

  <!-- Toast Notification Container -->
  <div id="toastContainer" class="toast-container"></div>

  <!-- Left Navigation Sidebar -->
  <aside class="sidebar">
    <div class="sidebar-header" style="padding: 22px 24px 18px;">
      <div class="sidebar-brand-top">
        <a href="dashboard.php" style="display: flex; flex-direction: column; gap: 4px; text-decoration: none;">
          <h2 style="font-size: 26px; font-weight: 800; letter-spacing: -0.03em; line-height: 1.1; margin: 0; display: flex; align-items: center; gap: 2px;">
            <span style="color: var(--primary);">Deen</span><span style="color: var(--text-heading);">One</span>
          </h2>
          <div class="app-tag" style="align-self: flex-start; margin-top: 2px;"><?php echo __('অ্যাডমিন হাব', 'Admin Hub'); ?></div>
        </a>
      </div>
    </div>

    <nav class="sidebar-nav">
      <!-- Direct Item: Dashboard -->
      <a href="dashboard.php" class="nav-item <?php echo $activeNav === 'dashboard' ? 'active' : ''; ?>">
        <span class="icon icon-emerald"><i class="fa-solid fa-house"></i></span>
        <span class="nav-label"><?php echo __('ড্যাশবোর্ড', 'Dashboard'); ?></span>
      </a>

      <!-- Accordion: Notices (Dedicated Section below Dashboard) -->
      <?php $isNoticeOpen = in_array($activeNav, ['notifications', 'notices']); ?>
      <div class="nav-accordion-group <?php echo $isNoticeOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-amber"><i class="fa-solid fa-bell"></i></span>
          <span class="nav-label"><?php echo __('নোটিশ', 'Notices'); ?></span>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="notifications.php" class="nav-sub-item <?php echo $activeNav === 'notifications' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #f59e0b;"></span>
            <span><?php echo __('পুশ নোটিফিকেশন', 'Push Notifications'); ?></span>
          </a>
          <a href="notices.php" class="nav-sub-item <?php echo $activeNav === 'notices' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #f59e0b;"></span>
            <span><?php echo __('ব্রডকাস্ট নোটিশ', 'Broadcast Notices'); ?></span>
          </a>
        </div>
      </div>

      <!-- Accordion 1: Administrator & Security -->
      <?php $isAdminOpen = in_array($activeNav, ['admin_users']); ?>
      <div class="nav-accordion-group <?php echo $isAdminOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-indigo"><i class="fa-solid fa-user-shield"></i></span>
          <span class="nav-label"><?php echo __('অ্যাডমিন', 'Admin'); ?></span>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="admin_users.php" class="nav-sub-item <?php echo $activeNav === 'admin_users' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #6366f1;"></span>
            <span><?php echo __('অ্যাকাউন্ট ও লগ', 'Accounts & Logs'); ?></span>
          </a>
        </div>
      </div>

      <!-- Direct Item: Gallery (গ্যালারি) -->
      <a href="gallery.php" class="nav-item <?php echo $activeNav === 'gallery' ? 'active' : ''; ?>">
        <span class="icon icon-purple" style="color: #8b5cf6;"><i class="fa-solid fa-images"></i></span>
        <span class="nav-label"><?php echo __('গ্যালারি', 'Gallery'); ?></span>
      </a>

      <!-- Accordion: Hadith Management Suite -->
      <?php $isHadithOpen = in_array($activeNav, ['hadith_books', 'hadith_chapters', 'hadiths', 'hadith_scraper']); ?>
      <div class="nav-accordion-group <?php echo $isHadithOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-emerald"><i class="fa-solid fa-book-bookmark"></i></span>
          <span class="nav-label"><?php echo __('হাদিস', 'Hadith'); ?></span>
          <?php if ($pendingScrapedHadithsCount > 0): ?>
            <span class="nav-badge" style="background: #f59e0b; color: #fff; margin-left: auto; margin-right: 8px;"><?php echo toLangNum($pendingScrapedHadithsCount); ?></span>
          <?php endif; ?>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="hadith_books.php" class="nav-sub-item <?php echo in_array($activeNav, ['hadith_books', 'hadith_chapters']) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #10b981;"></span>
            <span><?php echo __('গ্রন্থ ও অধ্যায়', 'Books & Chapters'); ?></span>
          </a>
          <a href="hadiths.php" class="nav-sub-item <?php echo $activeNav === 'hadiths' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #10b981;"></span>
            <span><?php echo __('হাদিস তালিকা', 'Hadiths List'); ?></span>
          </a>
          <a href="hadith_scraper.php" class="nav-sub-item <?php echo $activeNav === 'hadith_scraper' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #f59e0b;"></span>
            <span style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
              <span><?php echo __('হাদিস স্ক্র্যাপার', 'Hadith Scraper'); ?></span>
              <?php if ($pendingScrapedHadithsCount > 0): ?>
                <span class="badge" style="background: #f59e0b; color: #fff; font-size: 10px; padding: 1px 6px; border-radius: 999px;"><?php echo toLangNum($pendingScrapedHadithsCount); ?></span>
              <?php endif; ?>
            </span>
          </a>
        </div>
      </div>

      <!-- Direct Item: Islamic Books & Library (Independent Section) -->
      <a href="islamic_books.php" class="nav-item <?php echo $activeNav === 'islamic_books' ? 'active' : ''; ?>">
        <span class="icon icon-purple"><i class="fa-solid fa-book-quran"></i></span>
        <span class="nav-label"><?php echo __('ইসলামিক বই', 'Islamic Books'); ?></span>
      </a>

      <!-- Direct Item: Islamic Audio (Renamed & Streamlined) -->
      <a href="audio_hub.php" class="nav-item <?php echo $activeNav === 'audio_hub' ? 'active' : ''; ?>">
        <span class="icon icon-cyan"><i class="fa-solid fa-microphone-lines"></i></span>
        <span class="nav-label"><?php echo __('ইসলামিক অডিও', 'Islamic Audio'); ?></span>
      </a>

      <!-- Direct Item: Masnoon Dua & Zikr -->
      <a href="duas.php" class="nav-item <?php echo $activeNav === 'duas' ? 'active' : ''; ?>">
        <span class="icon icon-teal"><i class="fa-solid fa-hands-praying"></i></span>
        <span class="nav-label"><?php echo __('মাসনূন দু’আ', 'Masnoon Dua'); ?></span>
      </a>

      <!-- Direct Item: Nek Amal & Tasbih -->
      <a href="nek_amal.php" class="nav-item <?php echo $activeNav === 'nek_amal' ? 'active' : ''; ?>">
        <span class="icon icon-emerald"><i class="fa-solid fa-leaf"></i></span>
        <span class="nav-label"><?php echo __('নেক আমল', 'Nek Amal'); ?></span>
      </a>

      <!-- Direct Item: Masail & Fatwa -->
      <a href="masail_articles.php" class="nav-item <?php echo $activeNav === 'masail_articles' ? 'active' : ''; ?>">
        <span class="icon icon-rose"><i class="fa-solid fa-book-open"></i></span>
        <span class="nav-label"><?php echo __('মাসাইল ও ফতোয়া', 'Masail & Fatwa'); ?></span>
      </a>

      <!-- Direct Item: Hajj Journey Guide (পবিত্র হজ যাত্রা) -->
      <a href="hajj_journey.php" class="nav-item <?php echo $activeNav === 'hajj_journey' ? 'active' : ''; ?>">
        <span class="icon icon-teal" style="color: #1E8787;"><i class="fa-solid fa-kaaba"></i></span>
        <span class="nav-label"><?php echo __('হজ যাত্রা', 'Hajj Journey'); ?></span>
      </a>

      <!-- Accordion 3: Quiz (Streamlined to Categories & Live Battles) -->
      <?php $isQuizOpen = in_array($activeNav, ['quiz_categories', 'quiz_questions', 'battle_rooms']); ?>
      <div class="nav-accordion-group <?php echo $isQuizOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-indigo"><i class="fa-solid fa-shield-halved"></i></span>
          <span class="nav-label"><?php echo __('কুইজ', 'Quiz'); ?></span>
          <?php if ($activeBattlesCount > 0): ?>
            <span class="nav-badge primary" style="margin-left: auto; margin-right: 8px;"><?php echo toLangNum($activeBattlesCount); ?></span>
          <?php endif; ?>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="quiz_categories.php" class="nav-sub-item <?php echo in_array($activeNav, ['quiz_categories', 'quiz_questions']) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #6366f1;"></span>
            <span><?php echo __('কুইজ ক্যাটাগরি', 'Quiz Categories'); ?></span>
          </a>
          <a href="battle_rooms.php" class="nav-sub-item <?php echo $activeNav === 'battle_rooms' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #6366f1;"></span>
            <span><?php echo __('লাইভ ব্যাটেল', 'Live Battles'); ?></span>
          </a>
        </div>
      </div>

      <!-- Accordion 4: Blood Donation Network -->
      <?php $isBloodOpen = in_array($activeNav, ['blood_donors', 'blood_requests', 'blood_organizations']); ?>
      <div class="nav-accordion-group <?php echo $isBloodOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-red"><i class="fa-solid fa-droplet"></i></span>
          <span class="nav-label"><?php echo __('রক্তদান', 'Blood Donation'); ?></span>
          <?php if ($openBloodRequestsCount > 0): ?>
            <span class="nav-badge danger" style="margin-left: auto; margin-right: 8px;"><?php echo toLangNum($openBloodRequestsCount); ?></span>
          <?php endif; ?>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="blood_donors.php?tab=donors" class="nav-sub-item <?php echo ($activeNav === 'blood_donors' && (!isset($_GET['tab']) || $_GET['tab'] === 'donors')) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #ef4444;"></span>
            <span><?php echo __('রক্তদাতা তালিকা', 'Donors List'); ?></span>
            <?php if ($totalDonorsCount > 0): ?>
              <span class="nav-badge" style="margin-left:auto;"><?php echo toLangNum($totalDonorsCount); ?></span>
            <?php endif; ?>
          </a>
          <a href="blood_donors.php?tab=requests" class="nav-sub-item <?php echo ($activeNav === 'blood_donors' && (isset($_GET['tab']) && $_GET['tab'] === 'requests')) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #ef4444;"></span>
            <span><?php echo __('রক্তের আবেদন', 'Blood Requests'); ?></span>
            <?php if ($openBloodRequestsCount > 0): ?>
              <span class="nav-badge danger" style="margin-left:auto;"><?php echo toLangNum($openBloodRequestsCount); ?></span>
            <?php endif; ?>
          </a>
          <a href="blood_organizations.php" class="nav-sub-item <?php echo $activeNav === 'blood_organizations' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #ef4444;"></span>
            <span><?php echo __('সংগঠন তালিকা', 'Organizations'); ?></span>
          </a>
        </div>
      </div>

      <!-- Direct Item: Leaderboard (Dedicated Section below Blood Donation) -->
      <a href="leaderboard.php" class="nav-item <?php echo $activeNav === 'leaderboard' ? 'active' : ''; ?>">
        <span class="icon icon-amber"><i class="fa-solid fa-trophy"></i></span>
        <span class="nav-label"><?php echo __('লিডারবোর্ড', 'Leaderboard'); ?></span>
      </a>

      <!-- Accordion: Donations & Contribution Suite -->
      <?php $isDonationOpen = in_array($activeNav, ['donations', 'donation_settings']); ?>
      <div class="nav-accordion-group <?php echo $isDonationOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-green"><i class="fa-solid fa-hand-holding-dollar"></i></span>
          <span class="nav-label"><?php echo __('অনুদান', 'Donations'); ?></span>
          <?php if ($pendingDonationsCount > 0): ?>
            <span class="nav-badge" style="background: #f59e0b; color: #fff; margin-left: auto; margin-right: 8px;"><?php echo toLangNum($pendingDonationsCount); ?></span>
          <?php endif; ?>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="donations.php" class="nav-sub-item <?php echo ($activeNav === 'donations' && basename($_SERVER['PHP_SELF']) === 'donations.php') ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #10b981;"></span>
            <span style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
              <span><?php echo __('লেনদেন ও ভেরিফাই', 'Transactions & Verify'); ?></span>
              <?php if ($pendingDonationsCount > 0): ?>
                <span class="badge" style="background: #f59e0b; color: #fff; font-size: 10px; padding: 1px 6px; border-radius: 999px;"><?php echo toLangNum($pendingDonationsCount); ?></span>
              <?php endif; ?>
            </span>
          </a>
          <a href="donation_settings.php" class="nav-sub-item <?php echo ($activeNav === 'donations' && basename($_SERVER['PHP_SELF']) === 'donation_settings.php') ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #0284c7;"></span>
            <span><?php echo __('পেজ সেটিংস', 'Page Settings'); ?></span>
          </a>
          <a href="../donate/index.php" target="_blank" class="nav-sub-item">
            <span class="sub-dot" style="background-color: #f59e0b;"></span>
            <span style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
              <span><?php echo __('লাইভ ডোনেশন পেজ', 'Live Donation Page'); ?></span>
              <i class="fa-solid fa-arrow-up-right-from-square" style="font-size: 10px; opacity: 0.7;"></i>
            </span>
          </a>
        </div>
      </div>

      <!-- Accordion 5: Ummah Network (Community Hub) -->
      <?php $isCommunityOpen = in_array($activeNav, ['community', 'community_feed', 'community_comments', 'community_reports']); ?>
      <div class="nav-accordion-group <?php echo $isCommunityOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-cyan"><i class="fa-solid fa-comments"></i></span>
          <span class="nav-label"><?php echo __('কমিউনিটি', 'Community'); ?></span>
          <?php if ($pendingReportsCount > 0): ?>
            <span class="nav-badge danger" style="margin-left: auto; margin-right: 8px;"><?php echo toLangNum($pendingReportsCount); ?></span>
          <?php endif; ?>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="community.php?tab=feed" class="nav-sub-item <?php echo ($activeNav === 'community' && (!isset($_GET['tab']) || $_GET['tab'] === 'feed' || $_GET['tab'] === 'posts')) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: var(--primary);"></span>
            <span><?php echo __('কমিউনিটি ফিড', 'Community Feed'); ?></span>
          </a>
          <a href="community.php?tab=comments" class="nav-sub-item <?php echo ($activeNav === 'community' && (isset($_GET['tab']) && $_GET['tab'] === 'comments')) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: var(--accent-blue);"></span>
            <span><?php echo __('কমেন্টসমূহ', 'Comments'); ?></span>
          </a>
          <a href="community.php?tab=reports" class="nav-sub-item <?php echo ($activeNav === 'community' && (isset($_GET['tab']) && $_GET['tab'] === 'reports')) ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #ef4444;"></span>
            <span><?php echo __('রিপোর্ট ও অভিযোগ', 'Reports'); ?></span>
            <?php if ($pendingReportsCount > 0): ?>
              <span class="nav-badge danger" style="margin-left:auto;"><?php echo toLangNum($pendingReportsCount); ?></span>
            <?php endif; ?>
          </a>
        </div>
      </div>

      <!-- Direct Item: Users (Independent Section) -->
      <a href="users.php" class="nav-item <?php echo $activeNav === 'users' ? 'active' : ''; ?>">
        <span class="icon icon-indigo"><i class="fa-solid fa-users"></i></span>
        <span class="nav-label"><?php echo __('ইউজার', 'Users'); ?></span>
        <?php if ($totalUsersCount > 0): ?>
          <span class="nav-badge" style="margin-left:auto;"><?php echo toLangNum($totalUsersCount); ?></span>
        <?php endif; ?>
      </a>

      <!-- Direct Item: Halal Food & Ingredients (হালাল খাদ্য ও উপাদান) -->
      <a href="halal_foods.php" class="nav-item <?php echo $activeNav === 'halal_foods' ? 'active' : ''; ?>">
        <span class="icon icon-teal"><i class="fa-solid fa-utensils"></i></span>
        <span class="nav-label"><?php echo __('হালাল খাদ্য', 'Halal Food'); ?></span>
      </a>

      <!-- Accordion 7: Settings (Positioned at the very bottom) -->
      <?php $isSettingsOpen = in_array($activeNav, ['app_settings', 'privacy_policy', 'terms_conditions', 'help_support', 'ads', 'about_us', 'team']); ?>
      <div class="nav-accordion-group <?php echo $isSettingsOpen ? 'open active-parent' : ''; ?>">
        <button type="button" class="nav-accordion-header" onclick="toggleAccordion(this)">
          <span class="icon icon-slate"><i class="fa-solid fa-gear"></i></span>
          <span class="nav-label"><?php echo __('সেটিংস', 'Settings'); ?></span>
          <i class="fa-solid fa-chevron-right nav-arrow"></i>
        </button>
        <div class="nav-accordion-children">
          <a href="app_settings.php" class="nav-sub-item <?php echo $activeNav === 'app_settings' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #64748b;"></span>
            <span><?php echo __('জেনারেল', 'General'); ?></span>
          </a>
          <a href="privacy_policy.php" class="nav-sub-item <?php echo $activeNav === 'privacy_policy' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #10b981;"></span>
            <span><?php echo __('প্রাইভেসি পলিসি', 'Privacy Policy'); ?></span>
          </a>
          <a href="terms_conditions.php" class="nav-sub-item <?php echo $activeNav === 'terms_conditions' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #3b82f6;"></span>
            <span><?php echo __('শর্তাবলী', 'Terms of Service'); ?></span>
          </a>
          <a href="help_support.php" class="nav-sub-item <?php echo $activeNav === 'help_support' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #f59e0b;"></span>
            <span><?php echo __('হেল্প ও সাপোর্ট', 'Help & Support'); ?></span>
          </a>
          <a href="ads.php" class="nav-sub-item <?php echo $activeNav === 'ads' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #64748b;"></span>
            <span><?php echo __('বিজ্ঞাপন', 'Advertisements'); ?></span>
          </a>
          <a href="about_us.php" class="nav-sub-item <?php echo $activeNav === 'about_us' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #64748b;"></span>
            <span><?php echo __('আমাদের সম্পর্কে', 'About Us'); ?></span>
          </a>
          <a href="team.php" class="nav-sub-item <?php echo $activeNav === 'team' ? 'active' : ''; ?>">
            <span class="sub-dot" style="background-color: #64748b;"></span>
            <span><?php echo __('টিম', 'Team'); ?></span>
          </a>
        </div>
      </div>

    </nav>
  </aside>
  <div class="sidebar-backdrop" id="sidebarBackdrop"></div>

  <!-- Right Main Viewport -->
  <div class="main-wrapper">
    
    <!-- Top Navigation Bar -->
    <header class="topbar">
      <div class="topbar-left">
        <button class="btn-mobile-toggle" id="btnMobileToggle" aria-label="Toggle Sidebar">
          <i class="fa-solid fa-bars toggle-icon"></i>
        </button>
        <div class="page-breadcrumb">
          <h1><?php echo htmlspecialchars($pageTitle); ?></h1>
        </div>
      </div>

      <div class="topbar-right">
        <!-- Spotlight Search Trigger -->
        <button type="button" class="btn-spotlight-search" onclick="openCommandPalette()" title="<?php echo __('দ্রুত সার্চ করুন (Ctrl + K)', 'Quick Search (Ctrl + K)'); ?>">
          <i class="fa-solid fa-magnifying-glass"></i> <span><?php echo __('দ্রুত খুঁজুন...', 'Quick Search...'); ?></span> <kbd>Ctrl K</kbd>
        </button>

        <!-- Gregorian Date Pill -->
        <div class="date-pill" title="<?php echo __('আজকের তারিখ', 'Current Date'); ?>">
          <i class="fa-solid fa-calendar-day"></i> <span><?php echo $currentDateFormatted; ?></span>
        </div>

        <!-- Islamic Hijri Date Pill -->
        <div class="date-pill islamic" title="<?php echo __('আজকের হিজরি তারিখ', 'Current Hijri Date'); ?>">
          <i class="fa-solid fa-moon" style="color: #f59e0b; margin-right: 4px;"></i> <span><?php echo $currentHijriFormatted; ?></span>
        </div>

        <!-- Live Server Clock & User Location -->
        <div class="server-clock" title="<?php echo __('বর্তমান স্থানীয় সময় ও লোকেশন', 'Current Local Time & Location'); ?>">
          <span class="pulse-dot"></span>
          <span id="liveClock">--:--:--</span>
        </div>

        <!-- Dual-Language Dropdown / Switcher (Compact) -->
        <div class="topbar-dropdown" id="langDropdownWrap">
          <button type="button" class="btn-topbar-pill" onclick="toggleTopDropdown('langDropdownMenu', event)" title="<?php echo __('ভাষা পরিবর্তন করুন', 'Switch Language'); ?>" style="padding: 5px 11px; font-size: 12px;">
            <i class="fa-solid fa-globe" style="font-size: 13px;"></i>
            <span><?php echo isEn() ? 'EN' : 'বাংলা'; ?></span>
            <i class="fa-solid fa-chevron-down dropdown-arrow" style="font-size: 9px; margin-left: 2px;"></i>
          </button>
          <div class="topbar-dropdown-menu" id="langDropdownMenu">
            <a href="<?php echo htmlspecialchars(getLangSwitchUrl('bn')); ?>" class="dropdown-item <?php echo !isEn() ? 'active' : ''; ?>">
              <span class="flag-badge" style="font-weight: 700; font-size: 10px; background: rgba(16,185,129,0.15); color: #059669; padding: 2px 5px; border-radius: 4px; margin-right: 6px;">BN</span> <span>বাংলা</span>
            </a>
            <a href="<?php echo htmlspecialchars(getLangSwitchUrl('en')); ?>" class="dropdown-item <?php echo isEn() ? 'active' : ''; ?>">
              <span class="flag-badge" style="font-weight: 700; font-size: 10px; background: rgba(59,130,246,0.15); color: #2563eb; padding: 2px 5px; border-radius: 4px; margin-right: 6px;">EN</span> <span>English</span>
            </a>
          </div>
        </div>

        <!-- Quick Link to Landing Page / Main Website -->
        <a href="../" target="_blank" class="btn-topbar-pill" title="<?php echo __('মূল ওয়েবসাইট দেখুন', 'View Main Website'); ?>" style="padding: 6px 13px; font-size: 12.5px; text-decoration: none; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-arrow-up-right-from-square" style="color: var(--primary); font-size: 11px;"></i>
          <span><?php echo __('মূল সাইট', 'Main Site'); ?></span>
        </a>

        <!-- Theme Mode Switcher -->
        <button type="button" id="themeToggleBtn" class="btn-theme-toggle" onclick="toggleTheme()" title="<?php echo __('থিম পরিবর্তন করুন', 'Toggle Theme'); ?>" style="width: 36px; height: 36px; border-radius: 50%; padding: 0; display: inline-flex; align-items: center; justify-content: center;">
          <i class="fa-solid fa-sun" id="themeToggleIcon" style="font-size: 15px; color: #f59e0b;"></i>
        </button>

        <!-- Admin Profile Dropdown with Avatar (FAR RIGHT) -->
        <div class="topbar-dropdown" id="profileDropdownWrap">
          <button type="button" class="btn-topbar-profile" onclick="toggleTopDropdown('profileDropdownMenu', event)" title="<?php echo htmlspecialchars($adminUser['full_name']); ?>">
            <div class="avatar-sm overflow-hidden flex items-center justify-center">
              <?php if (!empty($adminUser['avatar']) && file_exists(__DIR__ . '/' . $adminUser['avatar'])): ?>
                <img src="<?php echo htmlspecialchars($adminUser['avatar']); ?>?v=<?php echo time(); ?>" alt="Avatar" class="w-full h-full object-cover">
              <?php else: ?>
                <?php echo mb_substr(htmlspecialchars($adminUser['full_name'] ?: 'A'), 0, 1, 'UTF-8'); ?>
              <?php endif; ?>
            </div>
            <i class="fa-solid fa-chevron-down dropdown-arrow" style="font-size: 9px; margin-left: 2px;"></i>
          </button>
          <div class="topbar-dropdown-menu right-aligned" id="profileDropdownMenu">
            <div class="dropdown-user-header">
              <div class="u-name"><?php echo htmlspecialchars($adminUser['full_name']); ?></div>
              <div class="u-role"><i class="fa-solid fa-shield"></i> <?php echo htmlspecialchars($adminUser['role']); ?></div>
            </div>
            <div class="dropdown-divider"></div>
            <a href="profile.php" class="dropdown-item">
              <i class="fa-regular fa-user"></i> <span><?php echo __('প্রোফাইল', 'Profile'); ?></span>
            </a>
            <a href="app_settings.php" class="dropdown-item">
              <i class="fa-solid fa-gear"></i> <span><?php echo __('সেটিংস', 'Settings'); ?></span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="logout.php" class="dropdown-item text-danger" onclick="return confirmAction(event, 'logout.php', '<?php echo __('আপনি কি নিশ্চিত যে আপনি লগআউট করতে চান?', 'Are you sure you want to logout?'); ?>');">
              <i class="fa-solid fa-right-from-bracket"></i> <span><?php echo __('লগআউট', 'Logout'); ?></span>
            </a>
          </div>
        </div>
      </div>
    </header>

    <!-- Command Palette (Spotlight Search) Modal -->
    <div id="commandPaletteModal" class="command-palette-backdrop" style="display: none;">
      <div class="command-palette-box">
        <div class="palette-input-wrap">
          <i class="fa-solid fa-magnifying-glass" style="color: var(--primary-light);"></i>
          <input type="text" id="paletteSearchInput" placeholder="<?php echo __('যেকোনো পেইজ বা অ্যাকশন খুঁজুন...', 'Search any page or action...'); ?>" autofocus autocomplete="off">
          <kbd style="font-size: 11px; background: rgba(255,255,255,0.06); padding: 2px 6px; border-radius: 4px; color: var(--text-muted);">ESC</kbd>
        </div>
        <div class="palette-results" id="paletteResults">
          <a href="dashboard.php" class="palette-item">
            <span><i class="fa-solid fa-chart-pie" style="color:#10b981; margin-right:8px;"></i> <?php echo __('ড্যাশবোর্ড ওভারভিউ', 'Dashboard Overview'); ?></span>
            <span class="item-desc"><?php echo __('হোম ও লাইভ মেট্রিকস', 'Home & Live Metrics'); ?></span>
          </a>
          <a href="notifications.php" class="palette-item">
            <span><i class="fa-solid fa-paper-plane" style="color:#3b82f6; margin-right:8px;"></i> <?php echo __('পুশ নোটিফিকেশন পাঠান', 'Send Push Notifications'); ?></span>
            <span class="item-desc"><?php echo __('ব্রডকাস্ট ও আপডেট বার্তা', 'Broadcast & Updates'); ?></span>
          </a>
          <a href="app_settings.php" class="palette-item">
            <span><i class="fa-solid fa-sliders" style="color:#f59e0b; margin-right:8px;"></i> <?php echo __('রিমোট সেটিংস ও বিজ্ঞাপন', 'Remote Settings & Ads'); ?></span>
            <span class="item-desc"><?php echo __('অ্যাড ইউনিট ও মেইন্টেন্যান্স', 'Ad Units & Maintenance'); ?></span>
          </a>
          <a href="quiz_questions.php?action=new" class="palette-item">
            <span><i class="fa-solid fa-plus-circle" style="color:#10b981; margin-right:8px;"></i> <?php echo __('নতুন কুইজ প্রশ্ন যোগ করুন', 'Add New Quiz Question'); ?></span>
            <span class="item-desc"><?php echo __('প্রশ্নব্যাংক ক্রিয়েশন', 'Question Bank Creation'); ?></span>
          </a>
          <a href="hadiths.php?action=new" class="palette-item">
            <span><i class="fa-solid fa-book-quran" style="color:#8b5cf6; margin-right:8px;"></i> <?php echo __('নতুন হাদীস অন্তর্ভুক্ত করুন', 'Add New Hadith'); ?></span>
            <span class="item-desc"><?php echo __('সহীহ হাদীস এডিটর', 'Sahih Hadith Editor'); ?></span>
          </a>
          <a href="duas.php?action=new" class="palette-item">
            <span><i class="fa-solid fa-hands-praying" style="color:#10b981; margin-right:8px;"></i> <?php echo __('নতুন দু’আ ও যিকির যুক্ত করুন', 'Add New Dua & Zikr'); ?></span>
            <span class="item-desc"><?php echo __('মাসনূন দু’আ সম্ভার', 'Masnoon Dua Collection'); ?></span>
          </a>
          <a href="halal_foods.php" class="palette-item">
            <span><i class="fa-solid fa-utensils" style="color:#06b6d4; margin-right:8px;"></i> <?php echo __('হালাল খাদ্যকোষ ও উপাদান', 'Halal Food Guide & E-Codes'); ?></span>
            <span class="item-desc"><?php echo __('ফুড ইনগ্রেডিয়েন্টস চেকার', 'Food Ingredients Checker'); ?></span>
          </a>
          <a href="users.php" class="palette-item">
            <span><i class="fa-solid fa-users" style="color:#3b82f6; margin-right:8px;"></i> <?php echo __('নিবন্ধিত ইউজার তালিকা ও লিডারবোর্ড', 'Registered Users & Leaderboard'); ?></span>
            <span class="item-desc"><?php echo __('ইউজার তথ্য ও পয়েন্ট', 'User Details & Points'); ?></span>
          </a>
          <a href="blood_donors.php" class="palette-item">
            <span><i class="fa-solid fa-droplet" style="color:#ef4444; margin-right:8px;"></i> <?php echo __('রক্তদাতা নেটওয়ার্ক ডিরেক্টরি', 'Blood Donors Network'); ?></span>
            <span class="item-desc"><?php echo __('গ্রুপ ও জেলা ফিল্টার', 'Blood Group & District Filter'); ?></span>
          </a>
          <a href="mosques.php" class="palette-item">
            <span><i class="fa-solid fa-mosque" style="color:#f59e0b; margin-right:8px;"></i> <?php echo __('মসজিদ ও হালাল স্থানসমূহ', 'Mosques & Halal Places'); ?></span>
            <span class="item-desc"><?php echo __('লোকেশন ভেরিফিকেশন', 'Location Verification'); ?></span>
          </a>
          <a href="battle_rooms.php" class="palette-item">
            <span><i class="fa-solid fa-shield-halved" style="color:#3b82f6; margin-right:8px;"></i> <?php echo __('নলেজ ব্যাটেল রুমস', 'Knowledge Battle Rooms'); ?></span>
            <span class="item-desc"><?php echo __('লাইভ প্রতিযোগী রুম', 'Live Competitor Rooms'); ?></span>
          </a>
        </div>
        <div class="palette-footer">
          <span><?php echo __('নেভিগেট করতে ↑ ↓ বা ক্লিক করুন', 'Navigate using ↑ ↓ or click'); ?></span>
          <span><?php echo __('বন্ধ করতে ESC চাপুন', 'Press ESC to close'); ?></span>
        </div>
      </div>
    </div>

    <!-- Main Content Container -->
    <main class="content-body">
      <?php if ($flash): ?>
        <div class="alert alert-<?php echo htmlspecialchars($flash['type']); ?>">
          <span><?php echo htmlspecialchars($flash['message']); ?></span>
          <button type="button" class="alert-close">&times;</button>
        </div>
      <?php endif; ?>
