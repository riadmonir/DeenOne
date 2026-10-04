<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - REMOTE APP CONFIGURATION & SETTINGS SUITE (MODULAR EDITION)
 * Ultra-Premium, Human-Crafted Architecture & Management Studio
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once dirname(__DIR__) . '/api/mail_helper.php';
requireAdminLogin();
$pdo = getDbConnection();

// Active tab detection & Clean Redirects
$tabParam = $_GET['tab'] ?? 'general';
if ($tabParam === 'ads') {
    redirect('ads.php');
}
if ($tabParam === 'privacy') {
    redirect('privacy_policy.php');
}
if ($tabParam === 'terms') {
    redirect('terms_conditions.php');
}
if ($tabParam === 'support') {
    redirect('help_support.php');
}
if ($tabParam === 'branding') {
    redirect('app_settings.php?tab=general');
}

$validTabs = ['general', 'server', 'typography', 'home_screen', 'email'];
$activeTab = in_array($tabParam, $validTabs) ? $tabParam : 'general';

// Handle GET Master Database Export/Backup
if (isset($_GET['action']) && $_GET['action'] === 'export_database') {
    $tables = [];
    $stmt = $pdo->query("SHOW TABLES");
    while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
        $tables[] = $row[0];
    }

    $sqlDump = "-- ========================================================\n";
    $sqlDump .= "-- DEEN ONE PRODUCTION DATABASE BACKUP & MASTER DUMP\n";
    $sqlDump .= "-- Generated: " . date('Y-m-d H:i:s') . "\n";
    $sqlDump .= "-- ========================================================\n\n";
    $sqlDump .= "SET FOREIGN_KEY_CHECKS=0;\nSET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';\n\n";

    foreach ($tables as $table) {
        $safeTable = preg_replace('/[^a-zA-Z0-9_]/', '', $table);
        if (empty($safeTable)) continue;
        $createStmt = $pdo->query("SHOW CREATE TABLE `{$safeTable}`")->fetch(PDO::FETCH_ASSOC);
        $sqlDump .= "DROP TABLE IF EXISTS `{$safeTable}`;\n";
        $sqlDump .= $createStmt['Create Table'] . ";\n\n";

        $rows = $pdo->query("SELECT * FROM `{$safeTable}`")->fetchAll(PDO::FETCH_ASSOC);
        if (!empty($rows)) {
            $cols = array_keys($rows[0]);
            $colList = '`' . implode('`, `', $cols) . '`';
            
            foreach ($rows as $r) {
                $vals = array_map(function($v) use ($pdo) {
                    if ($v === null) return 'NULL';
                    return $pdo->quote($v);
                }, array_values($r));
                $sqlDump .= "INSERT INTO `{$safeTable}` ($colList) VALUES (" . implode(', ', $vals) . ");\n";
            }
            $sqlDump .= "\n";
        }
    }
    $sqlDump .= "SET FOREIGN_KEY_CHECKS=1;\n";

    header('Content-Type: application/sql; charset=utf-8');
    header('Content-Disposition: attachment; filename="deenone_db_backup_' . date('Ymd_His') . '.sql"');
    header('Content-Length: ' . strlen($sqlDump));
    echo $sqlDump;
    exit;
}

// Handle POST Save
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর। পুনরায় চেষ্টা করুন।', 'Invalid security token. Please try again.'));
        redirect('app_settings.php?tab=' . urlencode($activeTab));
    }

    $postedTab = $_POST['active_tab'] ?? $activeTab;
    if (in_array($postedTab, $validTabs)) {
        $activeTab = $postedTab;
    }

    // Handle Master Database Restore
    if (isset($_POST['restore_database'])) {
        if (isset($_FILES['sql_backup_file']) && $_FILES['sql_backup_file']['error'] === UPLOAD_ERR_OK) {
            $sqlContent = file_get_contents($_FILES['sql_backup_file']['tmp_name']);
            if (!empty($sqlContent)) {
                try {
                    $pdo->exec($sqlContent);
                    logAdminAction($pdo, 'RESTORE_DATABASE', 'app_settings', null, 'পুরো ডাটাবেজ ব্যাকআপ থেকে রিস্টোর করা হয়েছে');
                    setFlash('success', __('ডাটাবেজ সফলভাবে রিস্টোর ও সিঙ্ক করা হয়েছে!', 'Database successfully restored and synchronized!'));
                } catch (Exception $e) {
                    setFlash('danger', __('ডাটাবেজ রিস্টোর ব্যর্থ: ', 'Database restore failed: ') . $e->getMessage());
                }
            } else {
                setFlash('danger', __('ফাইলটি খালি অথবা অবৈধ।', 'The file is empty or invalid.'));
            }
        } else {
            setFlash('danger', __('সঠিক .sql ব্যাকআপ ফাইল নির্বাচন করুন।', 'Please select a valid .sql backup file.'));
        }
        redirect('app_settings.php?tab=server');
    }

    // Handle Test Email Dispatch
    if (isset($_POST['send_test_email']) || isset($_POST['send_login_test_email'])) {
        $testEmail = trim($_POST['test_recipient_email'] ?? '');
        if (filter_var($testEmail, FILTER_VALIDATE_EMAIL)) {
            $diagnostics = [];
            $isLoginAlertTest = isset($_POST['send_login_test_email']);
            
            if ($isLoginAlertTest) {
                $mailSent = sendLoginAlertEmail($testEmail, 'Admin Tester', [
                    'device_name' => 'Android (Samsung Galaxy S23)',
                    'ip_address' => $_SERVER['REMOTE_ADDR'] ?? '103.205.180.12',
                    'location' => 'Dhaka, Bangladesh',
                    'login_datetime' => date('d M Y, h:i A T')
                ], $pdo);
            } else {
                $testSubject = "DeenOne Deliverability & SMTP Verification";
                $testBody = "Assalamu Alaikum,\n\nThis is a verification test email sent from DeenOne Admin Panel Settings at " . date('Y-m-d H:i:s') . ".\nIf you received this in your Primary Inbox, your email deliverability (DMARC, SPF, Message-ID) is 100% properly configured!\n\nJazakAllahu Khairan,\nDeenOne Team\nYour Islamic Companion";
                $htmlBody = "<div style='font-family:sans-serif; background:#111b21; color:#fff; padding:24px; border-radius:12px; max-width:500px;'>"
                          . "<h2 style='color:#00d2aa; margin-top:0;'>DeenOne SMTP Verification</h2>"
                          . "<p>Assalamu Alaikum,</p>"
                          . "<p>This is a verification test email sent from DeenOne Admin Panel Settings at <strong>" . date('Y-m-d H:i:s') . "</strong>.</p>"
                          . "<p style='background:rgba(0,210,170,0.1); border-left:4px solid #00d2aa; padding:12px; border-radius:4px;'>If you received this in your <strong>Primary Inbox</strong>, your email deliverability (DMARC, SPF, Envelope Sender) is working properly!</p>"
                          . "<p style='color:#94a3b8; font-size:13px;'>JazakAllahu Khairan,<br>DeenOne Team<br><em>Your Islamic Companion</em></p>"
                          . "</div>";
                $mailSent = sendDeenOneEmail($testEmail, $testSubject, $testBody, $htmlBody, $pdo, $diagnostics);
            }

            if ($mailSent) {
                $diagText = !empty($diagnostics) ? ' (' . implode(' | ', $diagnostics) . ')' : '';
                setFlash('success', __('টেস্ট ইমেইল সফলভাবে পাঠানো হয়েছে! অনুগ্রহ করে ইনবক্স চেক করুন: ', 'Test email sent successfully! Please check your inbox: ') . htmlspecialchars($testEmail) . $diagText);
            } else {
                $diagText = !empty($diagnostics) ? '<br><small>লগ: ' . htmlspecialchars(implode(' | ', $diagnostics)) . '</small>' : '';
                setFlash('warning', __('টেস্ট ইমেইল প্রেরণে সমস্যা হয়েছে অথবা লোকাল এনভায়রনমেন্টে সিমুলেটেড হয়েছে: ', 'Test email simulated or could not be delivered: ') . htmlspecialchars($testEmail) . $diagText);
            }
        } else {
            setFlash('danger', __('সঠিক ইমেইল ঠিকানা প্রদান করুন।', 'Please provide a valid email address.'));
        }
        redirect('app_settings.php?tab=email');
    }

    // Handle Website Favicon Upload
    $siteFaviconUrl = trim($_POST['site_favicon_url'] ?? '');
    if (isset($_FILES['site_favicon_file']) && $_FILES['site_favicon_file']['error'] === UPLOAD_ERR_OK) {
        $fileTmp = $_FILES['site_favicon_file']['tmp_name'];
        $fileName = $_FILES['site_favicon_file']['name'];
        $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
        $allowedExts = ['ico', 'png', 'jpg', 'jpeg', 'svg', 'webp'];
        if (in_array($fileExt, $allowedExts)) {
            $uploadDir = dirname(__DIR__) . '/uploads/';
            if (!is_dir($uploadDir)) {
                @mkdir($uploadDir, 0755, true);
            }
            $newFileName = 'favicon_' . time() . '.' . $fileExt;
            $targetFile = $uploadDir . $newFileName;
            if (move_uploaded_file($fileTmp, $targetFile)) {
                $siteFaviconUrl = 'uploads/' . $newFileName;
            }
        }
    }
    if (empty($siteFaviconUrl)) {
        $siteFaviconUrl = 'uploads/deenone_icon.webp';
    }

    $keys = [
        // Website & Platform Details (Identity & SEO)
        'app_name' => trim($_POST['app_name'] ?? 'দীন ওয়ান'),
        'app_name_en' => trim($_POST['app_name_en'] ?? 'DeenOne'),
        'app_tagline' => trim($_POST['app_tagline'] ?? 'প্রযুক্তি ও খাঁটি ইসলামিক জ্ঞানের সমন্বয়'),
        'app_tagline_en' => trim($_POST['app_tagline_en'] ?? 'Serving Humanity Through Authentic Islamic Knowledge & Technology'),
        'site_meta_description' => trim($_POST['site_meta_description'] ?? 'DeenOne provides modern, 100% ad-free Islamic apps and open knowledge platforms including Authentic Hadith, Interactive Visual Hajj Guide, Quran, Prayer Times, and Emergency Blood Network.'),
        'site_favicon_url' => $siteFaviconUrl,

        // Social Links Configuration
        'apk_download_url' => trim($_POST['apk_download_url'] ?? 'downloads/deenone.apk'),
        'social_facebook_url' => trim($_POST['social_facebook_url'] ?? 'https://www.facebook.com/deenone.official'),
        'social_telegram_url' => trim($_POST['social_telegram_url'] ?? 'https://t.me/deenone'),
        'social_youtube_url' => trim($_POST['social_youtube_url'] ?? 'https://youtube.com/@deenone'),
        'social_github_url' => trim($_POST['social_github_url'] ?? 'https://github.com/riadmonir/DeenOne'),

        // Version Control & Remote App Update
        'min_app_version' => (string) max(1, (int)($_POST['min_app_version'] ?? 1)),
        'latest_app_version' => (string) max(1, (int)($_POST['latest_app_version'] ?? 1)),
        'latest_version_name' => trim($_POST['latest_version_name'] ?? '1.1.0'),
        'force_update' => isset($_POST['force_update']) ? '1' : '0',
        'update_url' => trim($_POST['update_url'] ?? 'https://play.google.com/store/apps/details?id=com.devflux.deenone'),
        'update_title' => trim($_POST['update_title'] ?? 'নতুন সংস্করণ আপডেট'),
        'update_message' => trim($_POST['update_message'] ?? 'দীন ওয়ান অ্যাপের নতুন সংস্করণ উপলব্ধ রয়েছে। উন্নত পারফরম্যান্স ও নতুন ফিচারের জন্য অনুগ্রহ করে এখনই আপডেট করুন।'),

        // Server Maintenance
        'maintenance_mode' => isset($_POST['maintenance_mode']) ? '1' : '0',
        'maintenance_message' => trim($_POST['maintenance_message'] ?? 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।'),

        // Typography & Font Family Suite (Default: Roboto & Kalpurush)
        'admin_font_family' => trim($_POST['admin_font_family'] ?? ($current['admin_font_family'] ?? 'roboto')),
        'admin_bangla_font' => trim($_POST['admin_bangla_font'] ?? ($current['admin_bangla_font'] ?? 'kalpurush')),
        'admin_arabic_font' => trim($_POST['admin_arabic_font'] ?? ($current['admin_arabic_font'] ?? 'amiri')),

        // Home Screen & Feeds
        'announcement_active' => isset($_POST['announcement_active']) ? '1' : '0',
        'announcement_banner_text' => trim($_POST['announcement_banner_text'] ?? ''),
        'announcement_deep_link' => trim($_POST['announcement_deep_link'] ?? 'feature_history'),
        'daily_ayah_text' => trim($_POST['daily_ayah_text'] ?? ''),
        'daily_hadith_text' => trim($_POST['daily_hadith_text'] ?? ''),

        // Email Setup
        'mail_driver' => trim($_POST['mail_driver'] ?? 'smtp'),
        'smtp_host' => trim($_POST['smtp_host'] ?? 'smtp.gmail.com'),
        'smtp_port' => trim($_POST['smtp_port'] ?? '587'),
        'smtp_username' => trim($_POST['smtp_username'] ?? 'support@deenone.top'),
        'smtp_password' => trim($_POST['smtp_password'] ?? ''),
        'smtp_encryption' => trim($_POST['smtp_encryption'] ?? 'tls'),
        'mail_from_name' => trim($_POST['mail_from_name'] ?? 'DeenOne Official'),
        'mail_from_address' => trim($_POST['mail_from_address'] ?? 'noreply@deenone.top'),

        'custom_server_active' => '1'
    ];

    try {
        $stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value, description) 
                               VALUES (?, ?, ?) 
                               ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)");

        foreach ($keys as $key => $val) {
            $stmt->execute([$key, $val, 'Managed via Admin Panel Settings Suite']);
        }

        logAdminAction($pdo, 'UPDATE_APP_SETTINGS', 'app_settings', null, 'রিমোট কনফিগারেশন আপডেট করা হয়েছে (ট্যাব: ' . $activeTab . ')');
        setFlash('success', __('সেটিংস সফলভাবে সংরক্ষিত হয়েছে!', 'Settings saved successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('সেটিংস সংরক্ষণে ত্রুটি: ', 'Error saving settings: ') . $e->getMessage());
    }

    redirect('app_settings.php?tab=' . urlencode($activeTab));
}

// Fetch Current Settings from Database
$current = [];
try {
    $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
    while ($row = $stmt->fetch()) {
        $current[$row['setting_key']] = $row['setting_value'];
    }
} catch (Exception $e) {}

if (!function_exists('adminAssetUrl')) {
    function adminAssetUrl($url) {
        if (empty($url)) return '';
        if (preg_match('~^https?://~i', $url) || str_starts_with($url, '/')) return $url;
        return '../' . ltrim($url, '/');
    }
}

// Defaults
$siteFaviconUrl = $current['site_favicon_url'] ?? 'uploads/deenone_icon.webp';
$apkDownloadUrl = $current['apk_download_url'] ?? 'downloads/deenone.apk';
$socialFacebook = $current['social_facebook_url'] ?? 'https://www.facebook.com/deenone.official';
$socialTelegram = $current['social_telegram_url'] ?? 'https://t.me/deenone';
$socialYoutube = $current['social_youtube_url'] ?? 'https://youtube.com/@deenone';
$socialGithub = $current['social_github_url'] ?? 'https://github.com/riadmonir/DeenOne';

$appName = $current['app_name'] ?? 'দীন ওয়ান';
$appNameEn = $current['app_name_en'] ?? 'DeenOne';
$appTagline = $current['app_tagline'] ?? 'প্রযুক্তি ও খাঁটি ইসলামিক জ্ঞানের সমন্বয়';
$appTaglineEn = $current['app_tagline_en'] ?? 'Serving Humanity Through Authentic Islamic Knowledge & Technology';
$siteMetaDescription = $current['site_meta_description'] ?? 'DeenOne provides modern, 100% ad-free Islamic apps and open knowledge platforms including Authentic Hadith, Interactive Visual Hajj Guide, Quran, Prayer Times, and Emergency Blood Network.';

$adminFontFamily = $current['admin_font_family'] ?? 'roboto';
$adminBanglaFont = $current['admin_bangla_font'] ?? 'kalpurush';
$adminArabicFont = $current['admin_arabic_font'] ?? 'amiri';

$maintenanceMode = ($current['maintenance_mode'] ?? '0') === '1';
$maintenanceMsg = $current['maintenance_message'] ?? 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।';

$minVersion = $current['min_app_version'] ?? '1';
$latestVersion = $current['latest_app_version'] ?? '1';
$latestVersionName = $current['latest_version_name'] ?? '1.1.0';
$forceUpdate = ($current['force_update'] ?? '0') === '1';
$updateUrl = $current['update_url'] ?? 'https://play.google.com/store/apps/details?id=com.devflux.deenone';
$updateTitle = $current['update_title'] ?? 'নতুন সংস্করণ আপডেট';
$updateMessage = $current['update_message'] ?? 'দীন ওয়ান অ্যাপের নতুন সংস্করণ উপলব্ধ রয়েছে। উন্নত পারফরম্যান্স ও নতুন ফিচারের জন্য অনুগ্রহ করে এখনই আপডেট করুন।';

$mailDriver = $current['mail_driver'] ?? 'smtp';
$smtpHost = $current['smtp_host'] ?? 'smtp.gmail.com';
$smtpPort = $current['smtp_port'] ?? '587';
$smtpUsername = $current['smtp_username'] ?? 'support@deenone.top';
$smtpPassword = $current['smtp_password'] ?? '';
$smtpEncryption = $current['smtp_encryption'] ?? 'tls';
$mailFromName = $current['mail_from_name'] ?? 'DeenOne Official';
$mailFromAddress = $current['mail_from_address'] ?? 'noreply@deenone.top';

$announcementActive = ($current['announcement_active'] ?? '1') === '1';
$announcementText = $current['announcement_banner_text'] ?? 'দীন ওয়ান ইসলামিক নলেজ ব্যাটেলে আপনাকে স্বাগতম!';
$announcementLink = $current['announcement_deep_link'] ?? 'feature_history';

$dailyAyah = $current['daily_ayah_text'] ?? 'বলুন, আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করুন। (সূরা ত্বহা: ১১৪)';
$dailyHadith = $current['daily_hadith_text'] ?? 'যে ব্যক্তি ইলম অন্বেষণে কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সহজ করে দেন। (সহীহ মুসলিম)';

$pageTitle = __('জেনারেল সেটিংস', 'General Settings');
$activeNav = 'app_settings';
require_once __DIR__ . '/header.php';
?>

<div class="page-container" style="max-width: 1200px; margin: 0 auto; padding-bottom: 50px;">

  <!-- Header Banner -->
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 22px; flex-wrap: wrap; gap: 16px;">
    <div style="display: flex; align-items: center; gap: 10px;">
      <span style="display: inline-flex; align-items: center; justify-content: center; width: 38px; height: 38px; border-radius: 10px; background: rgba(16, 185, 129, 0.12); color: #10b981; font-size: 18px;">
        <i class="fa-solid fa-sliders"></i>
      </span>
      <h1 style="font-size: 22px; font-weight: 800; color: var(--text-heading); margin: 0;">
        <?php echo __('অ্যাপ সেটিংস', 'App Settings'); ?>
      </h1>
    </div>

    <!-- Live Status Indicators -->
    <div style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
      <span class="badge" style="background: <?php echo $maintenanceMode ? 'rgba(239, 68, 68, 0.15)' : 'rgba(16, 185, 129, 0.15)'; ?>; color: <?php echo $maintenanceMode ? '#ef4444' : '#059669'; ?>; font-weight: 700; padding: 6px 12px; border-radius: 20px; font-size: 12px; display: inline-flex; align-items: center; gap: 6px;">
        <i class="fa-solid <?php echo $maintenanceMode ? 'fa-triangle-exclamation' : 'fa-circle-check'; ?>" style="font-size: 10px;"></i>
        <?php echo $maintenanceMode ? __('মেইনটেন্যান্স চালু', 'Maintenance Active') : __('সার্ভার সচল', 'Online'); ?>
      </span>
      <span class="badge" style="background: rgba(59, 130, 246, 0.15); color: #2563eb; font-weight: 600; padding: 6px 12px; border-radius: 20px; font-size: 12px; display: inline-flex; align-items: center; gap: 6px;">
        <i class="fa-brands fa-android" style="font-size: 11px;"></i> <?php echo __('ভার্সন: ', 'Version: ') . 'v' . htmlspecialchars($latestVersion); ?>
      </span>
    </div>
  </div>

  <!-- Top Modular Settings Navigation Bar -->
  <div class="settings-nav-bar" style="margin-bottom: 24px;">
    <button type="button" class="settings-nav-tab <?php echo $activeTab === 'general' ? 'active' : ''; ?>" data-tab="general" onclick="switchTab('general', this)">
      <i class="fa-solid fa-sliders"></i> <?php echo __('জেনারেল', 'General'); ?>
    </button>
    <button type="button" class="settings-nav-tab <?php echo $activeTab === 'server' ? 'active' : ''; ?>" data-tab="server" onclick="switchTab('server', this)">
      <i class="fa-solid fa-server"></i> <?php echo __('সার্ভার ও সিস্টেম', 'Server & System'); ?>
    </button>
    <button type="button" class="settings-nav-tab <?php echo $activeTab === 'typography' ? 'active' : ''; ?>" data-tab="typography" onclick="switchTab('typography', this)">
      <i class="fa-solid fa-font"></i> <?php echo __('টাইপোগ্রাফি', 'Typography'); ?>
    </button>
    <button type="button" class="settings-nav-tab <?php echo $activeTab === 'home_screen' ? 'active' : ''; ?>" data-tab="home_screen" onclick="switchTab('home_screen', this)">
      <i class="fa-solid fa-mobile-screen"></i> <?php echo __('হোম স্ক্রিন', 'Home Screen'); ?>
    </button>
    <button type="button" class="settings-nav-tab <?php echo $activeTab === 'email' ? 'active' : ''; ?>" data-tab="email" onclick="switchTab('email', this)">
      <i class="fa-solid fa-envelope"></i> <?php echo __('ইমেইল', 'Email'); ?>
    </button>
  </div>

  <form id="settingsMainForm" method="POST" action="app_settings.php" enctype="multipart/form-data">
    <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
    <input type="hidden" name="active_tab" id="active_tab_input" value="<?php echo htmlspecialchars($activeTab); ?>">

    <!-- ========================================================================= -->
    <!-- TAB 1: GENERAL SETTINGS (Website Identity, Favicon, Social Links)         -->
    <!-- ========================================================================= -->
    <div id="tab-general" class="tab-pane-content" style="<?php echo $activeTab === 'general' ? 'display:block;' : 'display:none;'; ?>">
      
      <!-- 1. Website & Platform Details (Identity & SEO) -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-globe" style="color: var(--primary); font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('ওয়েবসাইট পরিচিতি ও টাইটেল', 'Website Identity & Title'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(16, 185, 129, 0.12); color: #059669; font-weight: 700; font-size: 11.5px; padding: 4px 10px; border-radius: 6px;">
            <?php echo __('লাইভ মেটা সিঙ্ক', 'Live Meta Sync'); ?>
          </span>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 18px; margin-bottom: 16px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('ওয়েবসাইটের নাম (বাংলা)', 'Website Name (Bengali)'); ?>
            </label>
            <input type="text" name="app_name" class="form-control" value="<?php echo htmlspecialchars($appName); ?>" placeholder="দীন ওয়ান" required>
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('ওয়েবসাইটের নাম (English)', 'Website Name (English)'); ?>
            </label>
            <input type="text" name="app_name_en" class="form-control" value="<?php echo htmlspecialchars($appNameEn); ?>" placeholder="DeenOne" required>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 18px; margin-bottom: 16px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('ওয়েবসাইট টাইটেল / ট্যাগলাইন (বাংলা)', 'Website Title / Tagline (Bengali)'); ?>
            </label>
            <input type="text" name="app_tagline" class="form-control" value="<?php echo htmlspecialchars($appTagline); ?>" placeholder="প্রযুক্তি ও খাঁটি ইসলামিক জ্ঞানের সমন্বয়">
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('ওয়েবসাইট টাইটেল / ট্যাগলাইন (English)', 'Website Title / Tagline (English)'); ?>
            </label>
            <input type="text" name="app_tagline_en" class="form-control" value="<?php echo htmlspecialchars($appTaglineEn); ?>" placeholder="Serving Humanity Through Authentic Islamic Knowledge & Technology">
          </div>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label" style="font-weight: 600; font-size: 13px;">
            <?php echo __('ওয়েবসাইট মেটা বিবরণী (SEO Description)', 'Website Meta Description (SEO)'); ?>
          </label>
          <textarea name="site_meta_description" class="form-control" rows="3" style="line-height: 1.6;" placeholder="DeenOne provides modern, 100% ad-free Islamic apps and open knowledge platforms..."><?php echo htmlspecialchars($siteMetaDescription); ?></textarea>
          <small style="color: var(--text-muted); font-size: 11px; margin-top: 4px; display: block;">
            <?php echo __('সার্চ ইঞ্জিন (Google) এবং সোশ্যাল মিডিয়া প্রিভিউতে এই বিবরণী প্রদর্শিত হবে।', 'This description will be displayed in search engines (Google) and social media share previews.'); ?>
          </small>
        </div>
      </div>

      <!-- 2. Website Favicon -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-gem" style="color: #f59e0b; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('ওয়েবসাইট ফেভিকন (Website Favicon)', 'Website Favicon'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(245, 158, 11, 0.12); color: #d97706; font-weight: 700; font-size: 11.5px; padding: 4px 10px; border-radius: 6px;">
            <?php echo __('ব্রাউজার ট্যাব আইকন', 'Browser Tab Icon'); ?>
          </span>
        </div>

        <div style="display: flex; align-items: center; gap: 24px; margin-bottom: 20px; padding: 18px; background: var(--hover-bg); border-radius: var(--radius-md); border: 1px solid var(--border-color); flex-wrap: wrap;">
          <div style="width: 60px; height: 60px; border-radius: 12px; background: var(--bg-card); border: 2px solid #f59e0b; display: flex; align-items: center; justify-content: center; overflow: hidden; box-shadow: 0 4px 12px rgba(245, 158, 11, 0.15); flex-shrink: 0;">
            <?php if (!empty($siteFaviconUrl)): ?>
              <img id="faviconPreviewImg" src="<?php echo htmlspecialchars(adminAssetUrl($siteFaviconUrl)); ?>" alt="Favicon" style="width: 36px; height: 36px; object-fit: contain;">
              <i id="faviconPreviewIcon" class="fa-solid fa-moon" style="display: none; font-size: 24px; color: #f59e0b;"></i>
            <?php else: ?>
              <img id="faviconPreviewImg" src="" alt="Favicon" style="display: none; width: 36px; height: 36px; object-fit: contain;">
              <i id="faviconPreviewIcon" class="fa-solid fa-moon" style="font-size: 24px; color: #f59e0b;"></i>
            <?php endif; ?>
          </div>
          <div style="flex: 1; min-width: 240px;">
            <h4 style="font-size: 15px; font-weight: 700; color: var(--text-heading); margin-bottom: 6px;"><?php echo __('ফেভিকন প্রিভিউ ও আপলোড', 'Favicon Preview & Upload'); ?></h4>
            <p style="font-size: 12px; color: var(--text-muted); margin: 0 0 10px 0;">
              <?php echo __('ব্রাউজার ট্যাবে প্রদর্শিত ছোট আইকন (সুপারিশকৃত সাইজ: 32x32 বা 64x64 পিক্সেল .ico, .png, .webp)।', 'Small icon displayed in browser tabs (Recommended size: 32x32 or 64x64 px .ico, .png, .webp).'); ?>
            </p>
            <div style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
              <label class="btn-topbar-pill" style="cursor: pointer; font-size: 12px; padding: 6px 14px; background: #f59e0b; color: #fff; border-color: #f59e0b;">
                <i class="fa-solid fa-cloud-arrow-up"></i> <span><?php echo __('ফেভিকন আপলোড', 'Upload Favicon'); ?></span>
                <input type="file" name="site_favicon_file" id="site_favicon_file" accept=".ico,.png,.webp,.svg,image/*" style="display: none;" onchange="previewSelectedFavicon(this)">
              </label>
              <span style="font-size: 11px; color: var(--text-dim);">ICO / PNG / WEBP / SVG</span>
            </div>
          </div>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('ফেভিকন ইউআরএল / ফাইল পাথ', 'Favicon URL / File Path'); ?></label>
          <input type="text" name="site_favicon_url" id="site_favicon_url_input" class="form-control" value="<?php echo htmlspecialchars($siteFaviconUrl); ?>" placeholder="uploads/deenone_icon.webp or https://..." oninput="previewUrlFavicon(this.value)">
        </div>
      </div>

      <!-- 3. Social Links Configuration (Verbatim section name: "সোশ্যাল লিংক কনফিগারেশন") -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-share-nodes" style="color: #10b981; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('সোশ্যাল লিংক কনফিগারেশন', 'Social Links Configuration'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(16, 185, 129, 0.12); color: #059669; font-weight: 700; font-size: 11.5px; padding: 4px 10px; border-radius: 6px;">
            <?php echo __('লাইভ সিঙ্ক', 'Live Sync'); ?>
          </span>
        </div>

        <div class="form-group" style="margin-bottom: 18px;">
          <label class="form-label" style="font-weight: 600; font-size: 13px;">
            <i class="fa-solid fa-file-arrow-down" style="color: #10b981; margin-right: 6px;"></i>
            <?php echo __('সরাসরি APK ডাউনলোড লিংক', 'Direct APK Download URL'); ?>
          </label>
          <div style="display: flex; gap: 8px;">
            <input type="text" name="apk_download_url" class="form-control" value="<?php echo htmlspecialchars($apkDownloadUrl); ?>" placeholder="downloads/deenone.apk or https://...">
            <a href="<?php echo htmlspecialchars($apkDownloadUrl); ?>" target="_blank" class="btn-topbar-pill" style="font-size: 11.5px; white-space: nowrap; text-decoration: none; padding: 6px 12px; display: inline-flex; align-items: center; gap: 5px;">
              <i class="fa-solid fa-arrow-up-right-from-square"></i> <?php echo __('লিংক চেক', 'Test Link'); ?>
            </a>
          </div>
          <small style="color: var(--text-muted); font-size: 11px; margin-top: 4px; display: block;">
            <?php echo __('ওয়েবসাইটে সরাসরি APK ডাউনলোড বাটনগুলোতে এই লিংক কার্যকর হবে।', 'This URL will be applied to direct APK download buttons across the website.'); ?>
          </small>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 16px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <i class="fa-brands fa-facebook" style="color: #1877f2; margin-right: 6px;"></i>
              <?php echo __('ফেসবুক পেজ লিংক', 'Facebook URL'); ?>
            </label>
            <input type="url" name="social_facebook_url" class="form-control" value="<?php echo htmlspecialchars($socialFacebook); ?>" placeholder="https://facebook.com/...">
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <i class="fa-brands fa-telegram" style="color: #229ed9; margin-right: 6px;"></i>
              <?php echo __('টেলিগ্রাম চ্যানেল লিংক', 'Telegram URL'); ?>
            </label>
            <input type="url" name="social_telegram_url" class="form-control" value="<?php echo htmlspecialchars($socialTelegram); ?>" placeholder="https://t.me/...">
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <i class="fa-brands fa-youtube" style="color: #ff0000; margin-right: 6px;"></i>
              <?php echo __('ইউটিউব চ্যানেল লিংক', 'YouTube URL'); ?>
            </label>
            <input type="url" name="social_youtube_url" class="form-control" value="<?php echo htmlspecialchars($socialYoutube); ?>" placeholder="https://youtube.com/...">
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <i class="fa-brands fa-github" style="color: var(--text-heading); margin-right: 6px;"></i>
              <?php echo __('গিটহাব রিপোজিটরি লিংক', 'GitHub URL'); ?>
            </label>
            <input type="url" name="social_github_url" class="form-control" value="<?php echo htmlspecialchars($socialGithub); ?>" placeholder="https://github.com/...">
          </div>
        </div>
      </div>

      <!-- Bottom Save Action -->
      <div style="display: flex; justify-content: flex-end; margin-bottom: 30px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সেভ করুন', 'Save Settings'); ?>
        </button>
      </div>
    </div>

    <!-- ========================================================================= -->
    <!-- TAB 2: SERVER & SYSTEM SUITE (Version Control, Maintenance, DB Backup)   -->
    <!-- ========================================================================= -->
    <div id="tab-server" class="tab-pane-content" style="<?php echo $activeTab === 'server' ? 'display:block;' : 'display:none;'; ?>">
      
      <!-- 1. Version Control & Remote App Update -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-code-branch" style="color: #3b82f6; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('ভার্সন কন্ট্রোল', 'Version Control'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(59, 130, 246, 0.12); color: #2563eb; font-weight: 700; font-size: 11.5px; padding: 4px 10px; border-radius: 6px;">
            Build: <?php echo htmlspecialchars($latestVersion); ?>
          </span>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 18px; margin-bottom: 16px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('মিনিমাম ভার্সন কোড', 'Minimum Version Code'); ?>
            </label>
            <input type="number" name="min_app_version" class="form-control" value="<?php echo htmlspecialchars($minVersion); ?>" min="1" required>
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('লেটেস্ট ভার্সন কোড', 'Latest Version Code'); ?>
            </label>
            <input type="number" name="latest_app_version" class="form-control" value="<?php echo htmlspecialchars($latestVersion); ?>" min="1" required>
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('ভার্সন নাম (উদা: 1.1.0)', 'Version Name (e.g. 1.1.0)'); ?>
            </label>
            <input type="text" name="latest_version_name" class="form-control" value="<?php echo htmlspecialchars($latestVersionName); ?>" placeholder="1.1.0">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 240px 1fr; gap: 18px; align-items: center; background: var(--hover-bg); padding: 14px 18px; border-radius: var(--radius-sm); border: 1px solid var(--border-color); margin-bottom: 16px;">
          <div class="form-group" style="margin-bottom: 0;">
            <label class="switch-label" style="margin: 0; cursor: pointer; display: flex; align-items: center; gap: 10px;">
              <label class="switch" style="margin: 0;">
                <input type="checkbox" name="force_update" value="1" <?php echo $forceUpdate ? 'checked' : ''; ?>>
                <span class="slider"></span>
              </label>
              <span style="font-weight: 700; font-size: 13.5px; color: var(--text-heading);"><?php echo __('ফোর্স আপডেট', 'Force Update'); ?></span>
            </label>
          </div>

          <div class="form-group" style="margin-bottom: 0;">
            <label class="form-label" style="font-weight: 600; font-size: 12.5px; margin-bottom: 4px;">
              <?php echo __('প্লে-স্টোর লিংক', 'Play Store URL'); ?>
            </label>
            <div style="display: flex; gap: 8px;">
              <input type="url" name="update_url" class="form-control" value="<?php echo htmlspecialchars($updateUrl); ?>" placeholder="https://play.google.com/store/apps/details?id=com.devflux.deenone">
              <a href="<?php echo htmlspecialchars($updateUrl); ?>" target="_blank" class="btn-topbar-pill" style="font-size: 11.5px; white-space: nowrap; text-decoration: none; padding: 6px 12px; display: inline-flex; align-items: center; gap: 5px;">
                <i class="fa-solid fa-arrow-up-right-from-square"></i> <?php echo __('লিংক চেক', 'Test Link'); ?>
              </a>
            </div>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 2fr; gap: 18px;">
          <div class="form-group" style="margin-bottom: 0;">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('আপডেট শিরোনাম', 'Update Dialog Title'); ?>
            </label>
            <input type="text" name="update_title" class="form-control" value="<?php echo htmlspecialchars($updateTitle); ?>" placeholder="<?php echo __('নতুন সংস্করণ আপডেট', 'New Version Available'); ?>">
          </div>
          <div class="form-group" style="margin-bottom: 0;">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('আপডেট বিবরণী / চেঞ্জলগ', 'Update Message / Changelog'); ?>
            </label>
            <input type="text" name="update_message" class="form-control" value="<?php echo htmlspecialchars($updateMessage); ?>" placeholder="<?php echo __('উন্নত পারফরম্যান্স ও নতুন ফিচারের জন্য অনুগ্রহ করে এখনই আপডেট করুন।', 'Please update now for better performance and new features.'); ?>">
          </div>
        </div>
      </div>

      <!-- 2. Server Maintenance Mode -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-server" style="color: #ef4444; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('সার্ভার মেইনটেন্যান্স', 'Server Maintenance'); ?>
            </h3>
          </div>

          <label class="switch-label" style="margin: 0; cursor: pointer; display: flex; align-items: center; gap: 10px;">
            <label class="switch" style="margin: 0;">
              <input type="checkbox" name="maintenance_mode" id="maintenance_toggle" value="1" <?php echo $maintenanceMode ? 'checked' : ''; ?> onchange="updateMaintenanceStatus(this.checked)">
              <span class="slider"></span>
            </label>
            <span id="maintenanceBadge" class="badge" style="background: <?php echo $maintenanceMode ? 'rgba(239, 68, 68, 0.15)' : 'rgba(16, 185, 129, 0.15)'; ?>; color: <?php echo $maintenanceMode ? '#ef4444' : '#059669'; ?>; font-weight: 700; padding: 4px 10px; border-radius: 6px; font-size: 12px;">
              <?php echo $maintenanceMode ? __('মেইনটেন্যান্স চালু', 'Active') : __('স্বাভাবিক চালু', 'Inactive'); ?>
            </span>
          </label>
        </div>

        <div class="form-group" style="margin-bottom: 14px;">
          <label class="form-label" style="font-weight: 600; font-size: 13px; display: flex; justify-content: space-between; align-items: center;">
            <span><?php echo __('মেইনটেন্যান্স বার্তা', 'Notice Message'); ?></span>
            <span style="font-size: 11px; color: var(--text-muted);"><?php echo __('কুইক টেমপ্লেট:', 'Quick Templates:'); ?></span>
          </label>
          <textarea name="maintenance_message" id="maintenance_message_input" class="form-control" rows="3" style="line-height: 1.6;"><?php echo htmlspecialchars($maintenanceMsg); ?></textarea>
        </div>

        <!-- Quick Pre-filled Message Templates -->
        <div style="display: flex; gap: 8px; flex-wrap: wrap;">
          <button type="button" class="btn-topbar-pill" onclick="setMaintenanceTemplate('সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।')" style="font-size: 11px; padding: 4px 10px;">
            <?php echo __('রুটিন কাজ', 'Routine Maintenance'); ?>
          </button>
          <button type="button" class="btn-topbar-pill" onclick="setMaintenanceTemplate('ডাটাবেজ অপ্টিমাইজেশন ও সার্ভার আপগ্রেড চলছে। সাময়িক অসুবিধার জন্য আমরা আন্তরিকভাবে দুঃখিত।')" style="font-size: 11px; padding: 4px 10px;">
            <?php echo __('আপগ্রেড', 'Upgrade'); ?>
          </button>
          <button type="button" class="btn-topbar-pill" onclick="setMaintenanceTemplate('নতুন আকর্ষণীয় ফিচার সংযুক্তির কাজ চলছে। কিছুক্ষণের মধ্যেই অ্যাপ পুনরায় সচল হবে, ইনশাআল্লাহ।')" style="font-size: 11px; padding: 4px 10px;">
            <?php echo __('নতুন ফিচার', 'New Feature'); ?>
          </button>
        </div>
      </div>

      <!-- 3. Master Database Backup & Restore -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-database" style="color: var(--primary); font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('ডাটাবেজ ব্যাকআপ ও রিস্টোর', 'Database Backup & Restore'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(16, 185, 129, 0.12); color: #059669; font-weight: 700; font-size: 11.5px; padding: 4px 10px; border-radius: 6px;">
            ৫০ টেবিল সিঙ্কড
          </span>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px;">
          <!-- Download Card -->
          <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 16px; display: flex; flex-direction: column; justify-content: space-between; gap: 12px;">
            <div style="font-weight: 700; font-size: 14px; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
              <i class="fa-solid fa-cloud-arrow-down" style="color: #3b82f6;"></i> <?php echo __('ব্যাকআপ ডাউনলোড', 'Download Backup'); ?>
            </div>
            <a href="app_settings.php?action=export_database" class="btn-topbar-pill" style="background: #2563eb; color: #fff; border-color: #2563eb; text-decoration: none; padding: 10px 16px; font-size: 12.5px; font-weight: 700; text-align: center; display: inline-flex; align-items: center; justify-content: center; gap: 8px;">
              <i class="fa-solid fa-download"></i> <?php echo __('SQL ব্যাকআপ ডাউনলোড', 'Download Backup'); ?>
            </a>
          </div>

          <!-- Restore Card -->
          <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 16px; display: flex; flex-direction: column; justify-content: space-between; gap: 12px;">
            <div style="font-weight: 700; font-size: 14px; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
              <i class="fa-solid fa-cloud-arrow-up" style="color: #f59e0b;"></i> <?php echo __('ডাটাবেজ রিস্টোর', 'Restore Database'); ?>
            </div>
            <button type="button" class="btn-topbar-pill" onclick="openRestoreDbModal()" style="background: var(--bg-card); color: var(--text-heading); border-color: var(--border-color); padding: 10px 16px; font-size: 12.5px; font-weight: 700; text-align: center; display: inline-flex; align-items: center; justify-content: center; gap: 8px;">
              <i class="fa-solid fa-upload" style="color: #f59e0b;"></i> <?php echo __('ফাইল আপলোড ও রিস্টোর', 'Choose File & Restore'); ?>
            </button>
          </div>
        </div>
      </div>

      <!-- Bottom Save Action -->
      <div style="display: flex; justify-content: flex-end; margin-bottom: 30px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সেভ করুন', 'Save Settings'); ?>
        </button>
      </div>
    </div>

    <!-- ========================================================================= -->
    <!-- TAB 3: TYPOGRAPHY & MULTI-LANGUAGE FONT STUDIO -->
    <!-- ========================================================================= -->
    <div id="tab-typography" class="tab-pane-content" style="<?php echo $activeTab === 'typography' ? 'display:block;' : 'display:none;'; ?>">
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-font" style="color: #8b5cf6; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('টাইপোগ্রাফি', 'Typography'); ?>
            </h3>
          </div>
          <span class="badge" style="background: rgba(139, 92, 246, 0.15); color: #8b5cf6; font-weight: 700; font-size: 11px; padding: 4px 10px; border-radius: 6px;">
            লাইভ প্রিভিউ
          </span>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 18px; margin-bottom: 20px;">
          <!-- English Font -->
          <div class="form-group" style="background: var(--hover-bg); padding: 14px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <label class="form-label" style="display: flex; align-items: center; justify-content: space-between; font-weight: 700; font-size: 13px;">
              <span><i class="fa-solid fa-font" style="color: #3b82f6; margin-right: 6px;"></i><?php echo __('ইংরেজি ফন্ট', 'English Font'); ?></span>
              <span class="badge" style="background: rgba(59,130,246,0.15); color: #2563eb; font-size: 10.5px; padding: 2px 6px; border-radius: 4px;" id="labelCurrentEnglish"><?php echo ucfirst(str_replace('_', ' ', $adminFontFamily)); ?></span>
            </label>
            <select name="admin_font_family" id="select_admin_font_family" class="form-control" onchange="setAdminFont(this.value, true);" style="margin-top: 8px;">
              <option value="roboto" <?php echo $adminFontFamily === 'roboto' ? 'selected' : ''; ?>>Roboto (Google Fonts - Clean & Balanced)</option>
              <option value="inter" <?php echo $adminFontFamily === 'inter' ? 'selected' : ''; ?>>Inter (Google Fonts)</option>
              <option value="plus_jakarta_sans" <?php echo $adminFontFamily === 'plus_jakarta_sans' ? 'selected' : ''; ?>>Plus Jakarta Sans (Google Fonts)</option>
              <option value="poppins" <?php echo $adminFontFamily === 'poppins' ? 'selected' : ''; ?>>Poppins (Google Fonts)</option>
              <option value="outfit" <?php echo $adminFontFamily === 'outfit' ? 'selected' : ''; ?>>Outfit (Google Fonts)</option>
              <option value="arial" <?php echo $adminFontFamily === 'arial' ? 'selected' : ''; ?>>Arial (System)</option>
            </select>
          </div>

          <!-- Bengali Font -->
          <div class="form-group" style="background: var(--hover-bg); padding: 14px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <label class="form-label" style="display: flex; align-items: center; justify-content: space-between; font-weight: 700; font-size: 13px;">
              <span><i class="fa-solid fa-language" style="color: #10b981; margin-right: 6px;"></i><?php echo __('বাংলা ফন্ট', 'Bengali Font'); ?></span>
              <span class="badge" style="background: rgba(16,185,129,0.15); color: #059669; font-size: 10.5px; padding: 2px 6px; border-radius: 4px;" id="labelCurrentBangla"><?php echo ucfirst(str_replace('_', ' ', $adminBanglaFont)); ?></span>
            </label>
            <select name="admin_bangla_font" id="select_admin_bangla_font" class="form-control" onchange="setAdminBanglaFont(this.value, true);" style="margin-top: 8px;">
              <option value="kalpurush" <?php echo $adminBanglaFont === 'kalpurush' ? 'selected' : ''; ?>>Kalpurush / SolaimanLipi</option>
              <option value="hind_siliguri" <?php echo $adminBanglaFont === 'hind_siliguri' ? 'selected' : ''; ?>>Hind Siliguri (Google Fonts)</option>
              <option value="noto_sans_bengali" <?php echo $adminBanglaFont === 'noto_sans_bengali' ? 'selected' : ''; ?>>Noto Sans Bengali (Google Fonts)</option>
              <option value="anek_bangla" <?php echo $adminBanglaFont === 'anek_bangla' ? 'selected' : ''; ?>>Anek Bangla (Google Fonts)</option>
              <option value="noto_serif_bengali" <?php echo $adminBanglaFont === 'noto_serif_bengali' ? 'selected' : ''; ?>>Noto Serif Bengali (Google Fonts)</option>
            </select>
          </div>

          <!-- Arabic Font -->
          <div class="form-group" style="background: var(--hover-bg); padding: 14px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <label class="form-label" style="display: flex; align-items: center; justify-content: space-between; font-weight: 700; font-size: 13px;">
              <span><i class="fa-solid fa-kaaba" style="color: #f59e0b; margin-right: 6px;"></i><?php echo __('আরবি ফন্ট', 'Arabic Font'); ?></span>
              <span class="badge" style="background: rgba(245,158,11,0.15); color: #d97706; font-size: 10.5px; padding: 2px 6px; border-radius: 4px;" id="labelCurrentArabic"><?php echo ucfirst(str_replace('_', ' ', $adminArabicFont)); ?></span>
            </label>
            <select name="admin_arabic_font" id="select_admin_arabic_font" class="form-control" onchange="setAdminArabicFont(this.value, true);" style="margin-top: 8px;">
              <option value="amiri" <?php echo $adminArabicFont === 'amiri' ? 'selected' : ''; ?>>Amiri (আমিরী)</option>
              <option value="scheherazade" <?php echo $adminArabicFont === 'scheherazade' ? 'selected' : ''; ?>>Scheherazade New (শেহেরজাদ)</option>
            </select>
          </div>
        </div>

        <!-- Real-Time Interactive Live Specimen Cards -->
        <div style="background: var(--hover-bg); border-radius: var(--radius-md); border: 1px solid var(--border-color); padding: 18px;">
          <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 12px; display: flex; align-items: center; justify-content: space-between;">
            <span>⚡ <?php echo __('লাইভ প্রিভিউ', 'Live Preview'); ?></span>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 14px;">
            <!-- English Live Box -->
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 16px;">
              <div style="font-size: 11px; font-weight: 700; color: #3b82f6; margin-bottom: 8px; display: flex; align-items: center; gap: 6px;">
                <i class="fa-solid fa-circle-check"></i> <?php echo __('ইংরেজি', 'English'); ?>
              </div>
              <div id="previewBoxEnglish" style="font-family: var(--font-english); font-size: 14px; color: var(--text-primary); line-height: 1.6;">
                DeenOne Platform. The quick brown fox jumps over the lazy dog. 0123456789
              </div>
            </div>

            <!-- Bengali Live Box -->
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 16px;">
              <div style="font-size: 11px; font-weight: 700; color: #10b981; margin-bottom: 8px; display: flex; align-items: center; gap: 6px;">
                <i class="fa-solid fa-circle-check"></i> <?php echo __('বাংলা', 'Bengali'); ?>
              </div>
              <div id="previewBoxBangla" style="font-family: var(--font-bengali); font-size: 15px; color: var(--text-primary); line-height: 1.6;">
                দীন ওয়ান - আপনার প্রতিদিনের সহীহ ইসলামিক জীবনসঙ্গী। ১২৩৪৫৬৭৮৯০
              </div>
            </div>

            <!-- Arabic Live Box -->
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 16px;">
              <div style="font-size: 11px; font-weight: 700; color: #f59e0b; margin-bottom: 8px; display: flex; align-items: center; gap: 6px;">
                <i class="fa-solid fa-circle-check"></i> <?php echo __('আরবি', 'Arabic'); ?>
              </div>
              <div id="previewBoxArabic" dir="rtl" style="font-family: var(--font-arabic); font-size: 20px; color: var(--text-primary); line-height: 1.7; text-align: right;">
                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ — إِنَّ الدِّينَ عِندَ اللَّهِ الْإِسْلَامُ
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Bottom Save Action -->
      <div style="display: flex; justify-content: flex-end; margin-bottom: 30px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সেভ করুন', 'Save Settings'); ?>
        </button>
      </div>
    </div>

    <!-- ========================================================================= -->
    <!-- TAB 4: HOME SCREEN LAYOUT & WIDGET CONFIGURATION -->
    <!-- ========================================================================= -->
    <div id="tab-home_screen" class="tab-pane-content" style="<?php echo $activeTab === 'home_screen' ? 'display:block;' : 'display:none;'; ?>">

      <div style="display: grid; grid-template-columns: 1fr 360px; gap: 24px; align-items: start;">
        
        <!-- Left: Configuration Form -->
        <div style="display: flex; flex-direction: column; gap: 20px;">
          
          <!-- Widget 1: Top Announcement Bar -->
          <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px;">
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; flex-wrap: wrap; gap: 10px;">
              <div style="display: flex; align-items: center; gap: 10px;">
                <i class="fa-solid fa-bullhorn" style="color: #f59e0b; font-size: 18px;"></i>
                <h3 style="font-size: 15.5px; font-weight: 700; color: var(--text-heading); margin: 0;">
                  <?php echo __('নোটিশ ব্যানার', 'Notice Banner'); ?>
                </h3>
              </div>

              <label class="switch-label" style="margin: 0; cursor: pointer; display: flex; align-items: center; gap: 8px;">
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);"><?php echo __('সক্রিয়:', 'Active:'); ?></span>
                <label class="switch" style="margin: 0;">
                  <input type="checkbox" name="announcement_active" id="inputAnnouncementActive" value="1" <?php echo $announcementActive ? 'checked' : ''; ?> onchange="updateHomePreview()">
                  <span class="slider"></span>
                </label>
              </label>
            </div>

            <div class="form-group" style="margin-bottom: 14px;">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <?php echo __('ব্যানার টেক্সট', 'Banner Text'); ?>
              </label>
              <textarea name="announcement_banner_text" id="inputAnnouncementText" class="form-control" rows="2" oninput="updateHomePreview()"><?php echo htmlspecialchars($announcementText); ?></textarea>
            </div>

            <div class="form-group" style="margin-bottom: 0;">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <i class="fa-solid fa-link" style="color: #3b82f6; margin-right: 6px;"></i> <?php echo __('ক্লিক অ্যাকশন', 'Click Action'); ?>
              </label>
              <select name="announcement_deep_link" class="form-control">
                <option value="feature_battle" <?php echo $announcementLink === 'feature_battle' ? 'selected' : ''; ?>><?php echo __('নলেজ ব্যাটেল', 'Knowledge Battle'); ?></option>
                <option value="feature_blood" <?php echo $announcementLink === 'feature_blood' ? 'selected' : ''; ?>><?php echo __('রক্তদান নেটওয়ার্ক', 'Blood Network'); ?></option>
                <option value="feature_ramadan" <?php echo $announcementLink === 'feature_ramadan' ? 'selected' : ''; ?>><?php echo __('রমাদান ক্যালেন্ডার', 'Ramadan Calendar'); ?></option>
                <option value="feature_hadith" <?php echo $announcementLink === 'feature_hadith' ? 'selected' : ''; ?>><?php echo __('সহীহ হাদিস', 'Sahih Hadith'); ?></option>
                <option value="feature_duas" <?php echo $announcementLink === 'feature_duas' ? 'selected' : ''; ?>><?php echo __('দু’আ ও জিকির', 'Masnoon Dua'); ?></option>
                <option value="feature_history" <?php echo $announcementLink === 'feature_history' ? 'selected' : ''; ?>><?php echo __('নোটিশ বোর্ড', 'Notice Board'); ?></option>
              </select>
            </div>
          </div>

          <!-- Widget 2: Daily Islamic Ayah & Hadith Highlights -->
          <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px;">
            <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;">
              <i class="fa-solid fa-book-open-reader" style="color: #10b981; font-size: 18px;"></i>
              <h3 style="font-size: 15.5px; font-weight: 700; color: var(--text-heading); margin: 0;">
                <?php echo __('দৈনিক আয়াত ও হাদিস', 'Daily Ayah & Hadith'); ?>
              </h3>
            </div>

            <div class="form-group" style="margin-bottom: 16px;">
              <label class="form-label" style="font-weight: 600; font-size: 13px; display: flex; align-items: center; justify-content: space-between;">
                <span><i class="fa-solid fa-kaaba" style="color: #f59e0b; margin-right: 6px;"></i> <?php echo __('আজকের আয়াত', 'Daily Ayah'); ?></span>
                <span class="badge" style="background: rgba(245,158,11,0.15); color: #d97706; font-size: 10px;">কুরআন</span>
              </label>
              <textarea name="daily_ayah_text" id="inputDailyAyah" class="form-control" rows="3" style="line-height: 1.6;" oninput="updateHomePreview()"><?php echo htmlspecialchars($dailyAyah); ?></textarea>
            </div>

            <div class="form-group" style="margin-bottom: 0;">
              <label class="form-label" style="font-weight: 600; font-size: 13px; display: flex; align-items: center; justify-content: space-between;">
                <span><i class="fa-solid fa-book-bookmark" style="color: #10b981; margin-right: 6px;"></i> <?php echo __('আজকের হাদিস', 'Daily Hadith'); ?></span>
                <span class="badge" style="background: rgba(16,185,129,0.15); color: #059669; font-size: 10px;">সহীহ হাদিস</span>
              </label>
              <textarea name="daily_hadith_text" class="form-control" rows="3" style="line-height: 1.6;"><?php echo htmlspecialchars($dailyHadith); ?></textarea>
            </div>
          </div>

          <!-- Widget 3: Core Home Modules Overview -->
          <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px;">
            <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 14px;">
              <i class="fa-solid fa-layer-group" style="color: #6366f1; font-size: 18px;"></i>
              <h3 style="font-size: 15.5px; font-weight: 700; color: var(--text-heading); margin: 0;">
                <?php echo __('হোম মডিউল', 'Home Modules'); ?>
              </h3>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 10px;">
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #10b981;"><i class="fa-solid fa-clock"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">সালাত সময়</span>
              </div>
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #3b82f6;"><i class="fa-solid fa-list-check"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">আমল চেকলিস্ট</span>
              </div>
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #f59e0b;"><i class="fa-solid fa-book-quran"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">কুরআন মাজীদ</span>
              </div>
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #8b5cf6;"><i class="fa-solid fa-trophy"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">নলেজ ব্যাটেল</span>
              </div>
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #ef4444;"><i class="fa-solid fa-droplet"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">রক্তদান নেটওয়ার্ক</span>
              </div>
              <div style="padding: 10px 12px; border-radius: 8px; background: var(--hover-bg); border: 1px solid var(--border-color); display: flex; align-items: center; gap: 10px;">
                <span style="color: #06b6d4;"><i class="fa-solid fa-hands-praying"></i></span>
                <span style="font-size: 12.5px; font-weight: 600; color: var(--text-heading);">দোয়া ও জিকির</span>
              </div>
            </div>
          </div>

        </div>

        <!-- Right: Real-time Android Screen Mockup -->
        <div>
          <div style="position: sticky; top: 90px; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 18px;">
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; border-bottom: 1px solid var(--border-color); padding-bottom: 8px;">
              <span style="font-size: 12px; font-weight: 700; color: var(--primary); text-transform: uppercase;">
                <i class="fa-solid fa-mobile-screen"></i> <?php echo __('মোবাইল প্রিভিউ', 'Mobile Preview'); ?>
              </span>
              <span class="badge" style="font-size: 10px; background: rgba(16,185,129,0.15); color: #059669;">লাইভ</span>
            </div>

            <!-- Mobile Frame -->
            <div style="background: #060d17; border-radius: 24px; padding: 14px; border: 4px solid #1e293b; box-shadow: 0 10px 25px rgba(0,0,0,0.4); color: #f1f5f9;">
              
              <!-- Mobile App Header -->
              <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; padding: 0 4px;">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <div style="width: 28px; height: 28px; border-radius: 7px; background: #10b981; display: flex; align-items: center; justify-content: center; font-size: 12px; color: #fff;">
                    🌙
                  </div>
                  <div>
                    <div style="font-size: 12px; font-weight: 800; color: #fff;">দীন ওয়ান</div>
                    <div style="font-size: 9px; color: #10b981;">আজকের সালাত: যোহর</div>
                  </div>
                </div>
                <div style="display: flex; gap: 8px; font-size: 13px; color: #94a3b8;">
                  <i class="fa-solid fa-bell"></i>
                  <i class="fa-solid fa-circle-user"></i>
                </div>
              </div>

              <!-- Announcement Banner Preview -->
              <div id="homePreviewBanner" style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.15), rgba(245, 158, 11, 0.05)); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: 10px; padding: 10px; margin-bottom: 12px; display: <?php echo $announcementActive ? 'block' : 'none'; ?>;">
                <div style="display: flex; align-items: center; gap: 6px; margin-bottom: 4px;">
                  <span style="background: #f59e0b; color: #000; font-size: 9px; font-weight: 800; padding: 1px 5px; border-radius: 4px;">নোটিশ</span>
                  <span style="font-size: 10px; color: #fcd34d; font-weight: 600;">ঘোষণা</span>
                </div>
                <div id="homePreviewBannerText" style="font-size: 11px; color: #fef3c7; line-height: 1.4;">
                  <?php echo htmlspecialchars($announcementText); ?>
                </div>
              </div>

              <!-- Daily Quran Ayah Preview Card -->
              <div style="background: #111e38; border: 1px solid #1e335f; border-radius: 12px; padding: 12px; margin-bottom: 10px;">
                <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px;">
                  <span style="font-size: 10.5px; font-weight: 700; color: #10b981;">📖 আজকের আয়াত</span>
                  <span style="font-size: 9px; color: #64748b;">দৈনিক বাণী</span>
                </div>
                <div id="homePreviewAyahText" style="font-size: 11px; color: #e2e8f0; line-height: 1.5;">
                  <?php echo htmlspecialchars($dailyAyah); ?>
                </div>
              </div>

              <!-- Quick Dummy Widget -->
              <div style="background: #111e38; border: 1px solid #1e335f; border-radius: 12px; padding: 10px; display: flex; align-items: center; justify-content: space-between;">
                <div style="font-size: 11px; font-weight: 600; color: #94a3b8;">🕌 পরবর্তী ওয়াক্ত: আসর</div>
                <span style="font-size: 10px; font-weight: 700; color: #3b82f6;">০৪:১৫ PM</span>
              </div>

            </div>
          </div>
        </div>

      </div>

      <!-- Bottom Save Action -->
      <div style="display: flex; justify-content: flex-end; margin-top: 24px; margin-bottom: 30px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সেভ করুন', 'Save Settings'); ?>
        </button>
      </div>
    </div>

    <!-- ========================================================================= -->
    <!-- TAB 5: EMAIL SETUP & SMTP GATEWAY -->
    <!-- ========================================================================= -->
    <div id="tab-email" class="tab-pane-content" style="<?php echo $activeTab === 'email' ? 'display:block;' : 'display:none;'; ?>">
      
      <!-- 1. SMTP Server Gateway -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;">
          <i class="fa-solid fa-envelope-circle-check" style="color: #3b82f6; font-size: 18px;"></i>
          <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
            <?php echo __('এসএমটিপি সেটিংস', 'SMTP Settings'); ?>
          </h3>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px; margin-bottom: 14px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('মেইল ড্রাইভার', 'Mail Driver'); ?></label>
            <select name="mail_driver" class="form-control">
              <option value="smtp" <?php echo $mailDriver === 'smtp' ? 'selected' : ''; ?>>SMTP (Recommended)</option>
              <option value="sendmail" <?php echo $mailDriver === 'sendmail' ? 'selected' : ''; ?>>PHP mail() / Sendmail</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('এসএমটিপি হোস্ট', 'SMTP Host'); ?></label>
            <input type="text" name="smtp_host" class="form-control" value="<?php echo htmlspecialchars($smtpHost); ?>" placeholder="smtp.gmail.com">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px; margin-bottom: 14px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('এসএমটিপি পোর্ট', 'SMTP Port'); ?></label>
            <input type="number" name="smtp_port" class="form-control" value="<?php echo htmlspecialchars($smtpPort); ?>" placeholder="587">
          </div>
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('এনক্রিপশন প্রোটোকল', 'Encryption'); ?></label>
            <select name="smtp_encryption" class="form-control">
              <option value="tls" <?php echo $smtpEncryption === 'tls' ? 'selected' : ''; ?>>TLS (Port 587)</option>
              <option value="ssl" <?php echo $smtpEncryption === 'ssl' ? 'selected' : ''; ?>>SSL (Port 465)</option>
              <option value="none" <?php echo $smtpEncryption === 'none' ? 'selected' : ''; ?>>None</option>
            </select>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px; margin-bottom: 14px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('এসএমটিপি ইউজারনেম', 'SMTP Username'); ?></label>
            <input type="text" name="smtp_username" class="form-control" value="<?php echo htmlspecialchars($smtpUsername); ?>" placeholder="support@deenone.top">
          </div>
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('এসএমটিপি পাসওয়ার্ড', 'SMTP Password'); ?></label>
            <div style="position: relative;">
              <input type="password" name="smtp_password" id="smtp_pass_input" class="form-control" value="<?php echo htmlspecialchars($smtpPassword); ?>" placeholder="••••••••••••">
              <button type="button" onclick="togglePassVisibility('smtp_pass_input', this)" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); background: none; border: none; cursor: pointer; font-size: 14px; color: var(--text-muted);">👁️</button>
            </div>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px;">
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('প্রেরকের নাম', 'From Sender Name'); ?></label>
            <input type="text" name="mail_from_name" class="form-control" value="<?php echo htmlspecialchars($mailFromName); ?>" placeholder="DeenOne Official">
          </div>
          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('প্রেরকের ইমেইল', 'From Sender Email'); ?></label>
            <input type="email" name="mail_from_address" class="form-control" value="<?php echo htmlspecialchars($mailFromAddress); ?>" placeholder="noreply@deenone.top">
          </div>
        </div>
      </div>

      <!-- 2. Test Email Dispatcher -->
      <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 22px;">
        <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;">
          <i class="fa-solid fa-paper-plane" style="color: #10b981; font-size: 18px;"></i>
          <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
            <?php echo __('টেস্ট ইমেইল', 'Test Email'); ?>
          </h3>
        </div>

        <div>
          <label class="form-label" style="font-weight: 600; font-size: 13px;"><?php echo __('টেস্ট ইমেইল ঠিকানা', 'Recipient Email'); ?></label>
          <input type="email" name="test_recipient_email" class="form-control" placeholder="yourname@gmail.com" style="max-width: 480px; margin-bottom: 14px;">
        </div>

        <div style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
          <button type="submit" name="send_test_email" value="1" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 10px 18px; font-size: 13px;">
            <i class="fa-solid fa-paper-plane"></i> <?php echo __('টেস্ট ইমেইল পাঠান', 'Send Test Email'); ?>
          </button>

          <button type="submit" name="send_login_test_email" value="1" class="btn-topbar-pill" style="background: #0284c7; color: #fff; border-color: #0284c7; padding: 10px 18px; font-size: 13px;">
            <i class="fa-solid fa-shield-halved"></i> <?php echo __('লগইন অ্যালার্ট টেস্ট', 'Test Login Alert'); ?>
          </button>
        </div>
      </div>

      <!-- Bottom Save Action -->
      <div style="display: flex; justify-content: flex-end; margin-bottom: 30px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সেভ করুন', 'Save Settings'); ?>
        </button>
      </div>
    </div>

  </form>

</div>

<!-- Restore DB Modal -->
<div id="restoreDbModal" style="display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.6); z-index: 9999; align-items: center; justify-content: center; backdrop-filter: blur(4px);">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 24px; max-width: 480px; width: 90%; box-shadow: 0 10px 30px rgba(0,0,0,0.4);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-triangle-exclamation" style="color: #ef4444;"></i> <?php echo __('ডাটাবেজ রিস্টোর নিশ্চিতকরণ', 'Confirm Database Restore'); ?>
      </h3>
      <button type="button" onclick="closeRestoreDbModal()" style="background: none; border: none; font-size: 18px; cursor: pointer; color: var(--text-muted);">&times;</button>
    </div>
    
    <p style="font-size: 13px; color: var(--text-muted); line-height: 1.6; margin-bottom: 18px;">
      <?php echo __('সতর্কবার্তা: রিস্টোর করলে বর্তমান ডাটাবেজের সকল টেবিল প্রতিস্থাপিত হবে। সঠিক .sql ব্যাকআপ ফাইল নির্বাচন করুন।', 'Warning: Restoring will overwrite all current database tables. Please select a valid .sql backup file.'); ?>
    </p>

    <form method="POST" action="app_settings.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="active_tab" value="server">
      <div class="form-group" style="margin-bottom: 20px;">
        <input type="file" name="sql_backup_file" accept=".sql" class="form-control" required>
      </div>
      <div style="display: flex; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn-topbar-pill" onclick="closeRestoreDbModal()"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" name="restore_database" value="1" class="btn-topbar-pill" style="background: #ef4444; color: #fff; border-color: #ef4444;">
          <i class="fa-solid fa-upload"></i> <?php echo __('হ্যাঁ, রিস্টোর করুন', 'Yes, Restore'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
function switchTab(tabId, el) {
  var btn = el || document.querySelector('.settings-nav-tab[data-tab="' + tabId + '"]');
  document.querySelectorAll('.settings-nav-tab').forEach(function(b) {
    b.classList.remove('active');
  });
  if (btn) btn.classList.add('active');

  document.querySelectorAll('.tab-pane-content').forEach(function(pane) {
    pane.style.display = 'none';
  });

  var activePane = document.getElementById('tab-' + tabId);
  if (activePane) {
    activePane.style.display = 'block';
  }

  var tabInput = document.getElementById('active_tab_input');
  if (tabInput) {
    tabInput.value = tabId;
  }

  if (history.pushState) {
    var newurl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?tab=' + encodeURIComponent(tabId);
    window.history.pushState({path:newurl}, '', newurl);
  }
}

function updateMaintenanceStatus(isActive) {
  var badge = document.getElementById('maintenanceBadge');
  if (badge) {
    if (isActive) {
      badge.style.background = 'rgba(239, 68, 68, 0.15)';
      badge.style.color = '#ef4444';
      badge.innerText = 'মেইনটেন্যান্স চালু (Active)';
    } else {
      badge.style.background = 'rgba(16, 185, 129, 0.15)';
      badge.style.color = '#059669';
      badge.innerText = 'স্বাভাবিক চালু (Offline)';
    }
  }
}

function setMaintenanceTemplate(text) {
  var input = document.getElementById('maintenance_message_input');
  if (input) {
    input.value = text;
  }
}

function updateHomePreview() {
  var isBannerActive = document.getElementById('inputAnnouncementActive').checked;
  var bannerText = document.getElementById('inputAnnouncementText').value;
  var ayahText = document.getElementById('inputDailyAyah').value;

  var bannerEl = document.getElementById('homePreviewBanner');
  var bannerTextEl = document.getElementById('homePreviewBannerText');
  var ayahEl = document.getElementById('homePreviewAyahText');

  if (bannerEl) {
    bannerEl.style.display = isBannerActive ? 'block' : 'none';
  }
  if (bannerTextEl) {
    bannerTextEl.innerText = bannerText;
  }
  if (ayahEl) {
    ayahEl.innerText = ayahText;
  }
}

function formatAdminPreviewUrl(url) {
  if (!url) return '';
  url = url.trim();
  if (url.startsWith('http://') || url.startsWith('https://') || url.startsWith('/') || url.startsWith('data:') || url.startsWith('../')) {
    return url;
  }
  return '../' + url;
}

function previewSelectedFavicon(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function(e) {
      var img = document.getElementById('faviconPreviewImg');
      var icon = document.getElementById('faviconPreviewIcon');
      if (img) {
        img.src = e.target.result;
        img.style.display = 'block';
      }
      if (icon) {
        icon.style.display = 'none';
      }
    };
    reader.readAsDataURL(input.files[0]);
  }
}

function previewUrlFavicon(url) {
  var img = document.getElementById('faviconPreviewImg');
  var icon = document.getElementById('faviconPreviewIcon');
  if (url && url.trim().length > 0) {
    if (img) {
      img.src = formatAdminPreviewUrl(url);
      img.style.display = 'block';
    }
    if (icon) {
      icon.style.display = 'none';
    }
  } else {
    if (img) img.style.display = 'none';
    if (icon) icon.style.display = 'block';
  }
}

function togglePassVisibility(inputId, btn) {
  var input = document.getElementById(inputId);
  if (input) {
    if (input.type === 'password') {
      input.type = 'text';
      btn.innerText = '🙈';
    } else {
      input.type = 'password';
      btn.innerText = '👁️';
    }
  }
}

function openRestoreDbModal() {
  var modal = document.getElementById('restoreDbModal');
  if (modal) modal.style.display = 'flex';
}

function closeRestoreDbModal() {
  var modal = document.getElementById('restoreDbModal');
  if (modal) modal.style.display = 'none';
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
