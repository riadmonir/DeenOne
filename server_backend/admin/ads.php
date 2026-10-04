<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - GOOGLE ADMOB ADS REMOTE CONFIGURATION (বিজ্ঞাপন)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Google Official Test Ad Unit IDs
$testAppId = 'ca-app-pub-3940256099942544~3347511713';
$testBannerId = 'ca-app-pub-3940256099942544/6300978111';
$testInterstitialId = 'ca-app-pub-3940256099942544/1033173712';
$testNativeId = 'ca-app-pub-3940256099942544/2247696110';
$testRewardedId = 'ca-app-pub-3940256099942544/5224354917';
$testAppOpenId = 'ca-app-pub-3940256099942544/9257395921';
$testRewardedInterstitialId = 'ca-app-pub-3940256099942544/5354046379';

// Default Production Ad Unit IDs (DeenOne Official)
$defaultProdAppId = 'ca-app-pub-6495900750071718~9706674012';
$defaultProdBannerId = 'ca-app-pub-6495900750071718/7835288176';
$defaultProdInterstitialId = 'ca-app-pub-6495900750071718/8521769602';
$defaultProdNativeId = 'ca-app-pub-6495900750071718/3896043165';
$defaultProdRewardedId = 'ca-app-pub-3940256099942544/5224354917';
$defaultProdAppOpenId = 'ca-app-pub-3940256099942544/9257395921';
$defaultProdRewardedInterstitialId = 'ca-app-pub-3940256099942544/5354046379';

// Detect Current Admin IP Address
$currentAdminIp = $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
if (!empty($_SERVER['HTTP_CF_CONNECTING_IP'])) {
    $currentAdminIp = trim($_SERVER['HTTP_CF_CONNECTING_IP']);
} elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
    $parts = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);
    $currentAdminIp = trim($parts[0]);
}

// Handle POST Save
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর। পুনরায় চেষ্টা করুন।', 'Invalid security token. Please try again.'));
        redirect('ads.php');
    }

    // Fetch existing settings for audit comparison
    $prev = [];
    try {
        $stmtPrev = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
        while ($row = $stmtPrev->fetch()) {
            $prev[$row['setting_key']] = $row['setting_value'];
        }
    } catch (Exception $e) {}

    // Form inputs
    $adMode = in_array($_POST['ad_mode'] ?? '', ['test', 'production']) ? $_POST['ad_mode'] : 'production';
    $newKeys = [
        'ads_enabled' => isset($_POST['ads_enabled']) ? '1' : '0',
        'ad_mode' => $adMode,
        'admob_app_id' => trim($_POST['admob_app_id'] ?? $defaultProdAppId),
        
        // Format Toggles
        'ad_banner_enabled' => isset($_POST['ad_banner_enabled']) ? '1' : '0',
        'ad_interstitial_enabled' => isset($_POST['ad_interstitial_enabled']) ? '1' : '0',
        'ad_native_enabled' => isset($_POST['ad_native_enabled']) ? '1' : '0',
        'ad_rewarded_enabled' => isset($_POST['ad_rewarded_enabled']) ? '1' : '0',
        'ad_app_open_enabled' => isset($_POST['ad_app_open_enabled']) ? '1' : '0',
        'ad_rewarded_interstitial_enabled' => isset($_POST['ad_rewarded_interstitial_enabled']) ? '1' : '0',

        // Unit IDs
        'admob_banner_id' => trim($_POST['admob_banner_id'] ?? ''),
        'admob_interstitial_id' => trim($_POST['admob_interstitial_id'] ?? ''),
        'admob_native_id' => trim($_POST['admob_native_id'] ?? ''),
        'admob_rewarded_id' => trim($_POST['admob_rewarded_id'] ?? ''),
        'admob_app_open_id' => trim($_POST['admob_app_open_id'] ?? ''),
        'admob_rewarded_interstitial_id' => trim($_POST['admob_rewarded_interstitial_id'] ?? ''),

        // Behavior Settings
        'ad_frequency_interval' => (string) max(1, (int)($_POST['ad_frequency_interval'] ?? 5)),
        'ad_interstitial_cooldown' => (string) max(0, (int)($_POST['ad_interstitial_cooldown'] ?? 60)),
        'ad_interstitial_home_enabled' => isset($_POST['ad_interstitial_home_enabled']) ? '1' : '0',
        'ad_banner_placement' => in_array($_POST['ad_banner_placement'] ?? '', ['bottom', 'top']) ? $_POST['ad_banner_placement'] : 'bottom',
        'ad_reward_amount' => (string) max(1, (int)($_POST['ad_reward_amount'] ?? 10)),
        'ad_app_open_cooldown' => (string) max(0, (int)($_POST['ad_app_open_cooldown'] ?? 14400)),

        // Admin Device Ad Suppression & IP Block
        'admin_block_ads' => isset($_POST['admin_block_ads']) ? '1' : '0',
        'admin_blocked_ips' => trim($_POST['admin_blocked_ips'] ?? '127.0.0.1, ::1'),

        // Google AdMob Policy Compliance & Family Protection
        'ad_content_rating' => in_array($_POST['ad_content_rating'] ?? '', ['G', 'PG', 'T', 'MA']) ? $_POST['ad_content_rating'] : 'G',
        'ad_test_device_id' => trim($_POST['ad_test_device_id'] ?? ''),
    ];

    try {
        $stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value, description) 
                               VALUES (?, ?, ?) 
                               ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)");

        $changes = [];
        foreach ($newKeys as $key => $val) {
            $oldVal = $prev[$key] ?? '';
            if ($oldVal !== $val) {
                $changes[] = "$key: '$oldVal' → '$val'";
            }
            $stmt->execute([$key, $val, 'AdMob Remote Configuration']);
        }

        // Audit Logging (Rule 15)
        if (!empty($changes)) {
            $changeSummary = implode(', ', array_slice($changes, 0, 8));
            if (count($changes) > 8) {
                $changeSummary .= ' (' . (count($changes) - 8) . ' আরও)';
            }
            logAdminAction($pdo, 'UPDATE_ADS_CONFIG', 'app_settings', 'admob', $changeSummary);
        }

        setFlash('success', __('বিজ্ঞাপন কনফিগারেশন সফলভাবে সংরক্ষিত হয়েছে!', 'AdMob configuration saved successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('সংরক্ষণে ত্রুটি: ', 'Error saving settings: ') . $e->getMessage());
    }

    redirect('ads.php');
}

// Fetch Current Settings from Database
$current = [];
try {
    $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
    while ($row = $stmt->fetch()) {
        $current[$row['setting_key']] = $row['setting_value'];
    }
} catch (Exception $e) {}

$adsEnabled = ($current['ads_enabled'] ?? '1') === '1';
$adMode = strtolower($current['ad_mode'] ?? 'production');
$isTestMode = ($adMode === 'test');

// Production Unit IDs
$appId = $current['admob_app_id'] ?? $defaultProdAppId;
$bannerId = $current['admob_banner_id'] ?? $defaultProdBannerId;
$interstitialId = $current['admob_interstitial_id'] ?? $defaultProdInterstitialId;
$nativeId = $current['admob_native_id'] ?? $defaultProdNativeId;
$rewardedId = $current['admob_rewarded_id'] ?? $defaultProdRewardedId;
$appOpenId = $current['admob_app_open_id'] ?? $defaultProdAppOpenId;
$rewardedInterstitialId = $current['admob_rewarded_interstitial_id'] ?? $defaultProdRewardedInterstitialId;

// Format Toggles
$bannerEnabled = ($current['ad_banner_enabled'] ?? '1') === '1';
$interstitialEnabled = ($current['ad_interstitial_enabled'] ?? '1') === '1';
$nativeEnabled = ($current['ad_native_enabled'] ?? '1') === '1';
$rewardedEnabled = ($current['ad_rewarded_enabled'] ?? '1') === '1';
$appOpenEnabled = ($current['ad_app_open_enabled'] ?? '0') === '1';
$rewardedInterstitialEnabled = ($current['ad_rewarded_interstitial_enabled'] ?? '0') === '1';

// Behavior Settings
$intervalClicks = $current['ad_frequency_interval'] ?? '5';
$interstitialCooldown = $current['ad_interstitial_cooldown'] ?? '60';
$interstitialHomeEnabled = ($current['ad_interstitial_home_enabled'] ?? '0') === '1';
$bannerPlacement = $current['ad_banner_placement'] ?? 'bottom';
$rewardAmount = $current['ad_reward_amount'] ?? '10';
$appOpenCooldown = $current['ad_app_open_cooldown'] ?? '14400';

// Admin IP & Device Exclusion Settings
$adminBlockAds = ($current['admin_block_ads'] ?? '1') === '1';
$adminBlockedIps = $current['admin_blocked_ips'] ?? '127.0.0.1, ::1';

// Google AdMob Policy & Compliance Settings
$adContentRating = $current['ad_content_rating'] ?? 'G';
$adTestDeviceId = $current['ad_test_device_id'] ?? '';

// Fetch Recent Audit Logs
$auditLogs = [];
try {
    $stmtLogs = $pdo->query("SELECT l.*, u.name as admin_name 
                             FROM admin_logs l 
                             LEFT JOIN admin_users u ON l.admin_id = u.id 
                             WHERE l.action = 'UPDATE_ADS_CONFIG' OR l.action LIKE '%ADS%'
                             ORDER BY l.id DESC LIMIT 8");
    $auditLogs = $stmtLogs->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('বিজ্ঞাপন', 'Advertisements');
$activeNav = 'ads';
require_once __DIR__ . '/header.php';
?>

<style>
/* Sleek Compact Mode Selector Pill Group */
.ad-mode-pill-container {
  display: inline-flex;
  align-items: center;
  background: var(--bg-main);
  border: 1px solid var(--border-color);
  border-radius: 999px;
  padding: 3px;
  gap: 4px;
}
.ad-mode-pill-btn {
  padding: 6px 14px;
  border-radius: 999px;
  font-size: 12.5px;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border: 1px solid transparent;
  color: var(--text-muted);
  background: transparent;
  transition: all 0.18s ease-in-out;
  user-select: none;
}
.ad-mode-pill-btn input[type="radio"] {
  display: none;
}
.ad-mode-pill-btn.active-prod {
  background: rgba(16, 185, 129, 0.16);
  color: var(--primary);
  border-color: rgba(16, 185, 129, 0.4);
}
.ad-mode-pill-btn.active-test {
  background: rgba(245, 158, 11, 0.16);
  color: var(--accent-gold);
  border-color: rgba(245, 158, 11, 0.4);
}

/* Format Card & Collapsible Body Styling */
.format-card-box {
  background: var(--bg-input);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  overflow: hidden;
  transition: border-color 0.2s ease;
}
.format-card-box.active-card {
  border-color: rgba(16, 185, 129, 0.35);
}
.format-card-header {
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.format-card-body {
  padding: 0 16px 16px 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
}
</style>

<form method="POST" action="ads.php">
  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

  <!-- TOP SUMMARY & STATUS STRIP (Compact & Clean without hardcoded App ID) -->
  <div style="display: flex; gap: 12px; margin-bottom: 18px; flex-wrap: wrap;">
    <!-- Global Status -->
    <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px 18px; display: flex; align-items: center; gap: 12px; flex: 1; min-width: 200px;">
      <i class="fa-solid fa-signal" style="color: <?php echo $adsEnabled ? 'var(--primary)' : 'var(--text-muted)'; ?>; font-size: 16px;"></i>
      <div>
        <div style="font-size: 11px; color: var(--text-muted); font-weight: 600; text-transform: uppercase;"><?php echo __('গ্লোবাল স্ট্যাটাস', 'Global Status'); ?></div>
        <div style="font-size: 14px; font-weight: 700; color: <?php echo $adsEnabled ? 'var(--primary)' : 'var(--text-muted)'; ?>;">
          <?php echo $adsEnabled ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Disabled'); ?>
        </div>
      </div>
    </div>

    <!-- Ad Mode -->
    <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px 18px; display: flex; align-items: center; gap: 12px; flex: 1; min-width: 200px;">
      <i class="fa-solid fa-shield-halved" style="color: <?php echo $isTestMode ? 'var(--accent-gold)' : 'var(--primary)'; ?>; font-size: 16px;"></i>
      <div>
        <div style="font-size: 11px; color: var(--text-muted); font-weight: 600; text-transform: uppercase;"><?php echo __('বিজ্ঞাপন মোড', 'Ad Mode'); ?></div>
        <div style="font-size: 14px; font-weight: 700; color: <?php echo $isTestMode ? 'var(--accent-gold)' : 'var(--primary)'; ?>;">
          <?php echo $isTestMode ? __('টেস্ট মোড', 'Test Mode') : __('লাইভ প্রোডাকশন', 'Live Production'); ?>
        </div>
      </div>
    </div>

    <!-- Admin Device Ad Exclusion Status -->
    <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px 18px; display: flex; align-items: center; gap: 12px; flex: 1.2; min-width: 240px;">
      <i class="fa-solid fa-user-shield" style="color: <?php echo $adminBlockAds ? 'var(--accent-blue)' : 'var(--text-muted)'; ?>; font-size: 16px;"></i>
      <div>
        <div style="font-size: 11px; color: var(--text-muted); font-weight: 600; text-transform: uppercase;"><?php echo __('অ্যাডমিন সুরক্ষা', 'Admin Device Ad Filter'); ?></div>
        <div style="font-size: 14px; font-weight: 700; color: <?php echo $adminBlockAds ? 'var(--accent-blue)' : 'var(--text-muted)'; ?>;">
          <?php echo $adminBlockAds ? __('অ্যাডমিন ডিভাইসে ব্লক সক্রিয়', 'Admin Blocking Active') : __('ব্লক নিষ্ক্রিয়', 'Disabled'); ?>
        </div>
      </div>
    </div>
  </div>

  <!-- CARD 1: মাস্টার নিয়ন্ত্রণ ও মোড নির্বাচন (Sleek Compact Buttons) -->
  <div class="policy-card" style="margin-bottom: 20px;">
    <div class="policy-header">
      <div class="policy-title-row" style="justify-content: space-between;">
        <div style="display: flex; align-items: center; gap: 8px;">
          <span class="policy-dot" style="background: var(--primary);"></span>
          <span class="policy-title"><?php echo __('মাস্টার নিয়ন্ত্রণ', 'Master Control'); ?></span>
        </div>
        <label class="switch-label">
          <span style="font-size: 13.5px; font-weight: 700;"><?php echo __('বিজ্ঞাপন চালু:', 'Ads Active:'); ?></span>
          <label class="switch">
            <input type="checkbox" name="ads_enabled" value="1" <?php echo $adsEnabled ? 'checked' : ''; ?>>
            <span class="slider"></span>
          </label>
        </label>
      </div>
    </div>

    <!-- Ad Mode Selector: Sleek Inline Pill Buttons -->
    <div style="margin-top: 14px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;">
      <div>
        <div style="font-weight: 700; font-size: 13.5px; color: var(--text-primary);"><?php echo __('বিজ্ঞাপন পরিবেশন মোড', 'Ad Serving Mode'); ?></div>
        <div style="font-size: 12px; color: var(--text-muted);"><?php echo __('নিরাপদ টেস্ট মোড অথবা লাইভ প্রোডাকশন নির্বাচন করুন', 'Select Safe Test Mode or Live Production'); ?></div>
      </div>

      <div class="ad-mode-pill-container">
        <!-- Live Production Pill Button -->
        <label class="ad-mode-pill-btn <?php echo !$isTestMode ? 'active-prod' : ''; ?>" id="pill_mode_prod" onclick="selectAdMode('production')">
          <input type="radio" name="ad_mode" value="production" <?php echo !$isTestMode ? 'checked' : ''; ?>>
          <i class="fa-solid fa-bolt" style="font-size: 11px;"></i>
          <span><?php echo __('লাইভ প্রোডাকশন', 'Live Production'); ?></span>
        </label>

        <!-- Official Test Pill Button -->
        <label class="ad-mode-pill-btn <?php echo $isTestMode ? 'active-test' : ''; ?>" id="pill_mode_test" onclick="selectAdMode('test')">
          <input type="radio" name="ad_mode" value="test" <?php echo $isTestMode ? 'checked' : ''; ?>>
          <i class="fa-solid fa-vial" style="font-size: 11px;"></i>
          <span><?php echo __('অফিসিয়াল টেস্ট', 'Official Test'); ?></span>
        </label>
      </div>
    </div>

    <!-- AdMob App ID Input Field (Editable standard input) -->
    <div class="form-group" style="margin-top: 16px;">
      <label class="form-label" style="font-size: 12.5px; font-weight: 600;"><?php echo __('AdMob অ্যাপ আইডি', 'AdMob App ID'); ?></label>
      <input type="text" name="admob_app_id" id="admob_app_id" class="form-control" value="<?php echo htmlspecialchars($appId); ?>" placeholder="ca-app-pub-6495900750071718~9706674012">
      <div style="font-size: 11.5px; color: var(--text-muted); margin-top: 4px;">
        <?php echo __('অ্যাপ্লিকেশনের মূল Google AdMob App ID কনফিগারেশন।', 'Google AdMob App ID configured for this build.'); ?>
      </div>
    </div>
  </div>

  <!-- CARD 2: বিজ্ঞাপন ইউনিট ও ফরম্যাট কনফিগারেশন (Collapsible on Toggle) -->
  <div class="policy-card" style="margin-bottom: 20px;">
    <div class="policy-header">
      <div class="policy-title-row" style="justify-content: space-between;">
        <div style="display: flex; align-items: center; gap: 8px;">
          <span class="policy-dot" style="background: var(--accent-gold);"></span>
          <span class="policy-title"><?php echo __('ফরম্যাট ও ইউনিট আইডি', 'Formats & Unit IDs'); ?></span>
        </div>
        <div style="display: flex; gap: 8px;">
          <button type="button" class="btn-topbar-pill" style="font-size: 11.5px; padding: 4px 10px;" onclick="loadTestPreset()">
            <i class="fa-solid fa-vial"></i> <?php echo __('টেস্ট আইডি', 'Test IDs'); ?>
          </button>
          <button type="button" class="btn-topbar-pill" style="font-size: 11.5px; padding: 4px 10px;" onclick="loadProdPreset()">
            <i class="fa-solid fa-bolt"></i> <?php echo __('প্রোডাকশন আইডি', 'Production IDs'); ?>
          </button>
        </div>
      </div>
    </div>

    <!-- 1. Banner Ad Section -->
    <div class="format-card-box <?php echo $bannerEnabled ? 'active-card' : ''; ?>" id="card_box_banner">
      <div class="format-card-header">
        <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-rectangle-ad" style="color: var(--primary);"></i>
          <?php echo __('ব্যানার বিজ্ঞাপন', 'Banner Ad'); ?>
          <span id="badge_banner" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $bannerEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
            <?php echo $bannerEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
          </span>
        </span>
        <label class="switch">
          <input type="checkbox" name="ad_banner_enabled" id="ad_banner_enabled" value="1" <?php echo $bannerEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('banner')">
          <span class="slider"></span>
        </label>
      </div>

      <div class="format-card-body" id="body_banner" style="<?php echo $bannerEnabled ? '' : 'display: none;'; ?>">
        <div class="form-row" style="margin-top: 10px;">
          <div class="form-group" style="flex: 2;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('ব্যানার ইউনিট আইডি', 'Banner Unit ID'); ?></label>
            <input type="text" name="admob_banner_id" id="admob_banner_id" class="form-control" value="<?php echo htmlspecialchars($bannerId); ?>" placeholder="ca-app-pub-6495900750071718/7835288176">
          </div>
          <div class="form-group" style="flex: 1;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('প্লেসমেন্ট', 'Placement'); ?></label>
            <select name="ad_banner_placement" class="form-control">
              <option value="bottom" <?php echo $bannerPlacement === 'bottom' ? 'selected' : ''; ?>><?php echo __('নিচে', 'Bottom'); ?></option>
              <option value="top" <?php echo $bannerPlacement === 'top' ? 'selected' : ''; ?>><?php echo __('উপরে', 'Top'); ?></option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- 2. Interstitial Ad Section -->
    <div class="format-card-box <?php echo $interstitialEnabled ? 'active-card' : ''; ?>" id="card_box_interstitial">
      <div class="format-card-header">
        <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-mobile-screen-button" style="color: var(--accent-gold);"></i>
          <?php echo __('ইন্টারস্টিশিয়াল বিজ্ঞাপন', 'Interstitial Ad'); ?>
          <span id="badge_interstitial" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $interstitialEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
            <?php echo $interstitialEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
          </span>
        </span>
        <label class="switch">
          <input type="checkbox" name="ad_interstitial_enabled" id="ad_interstitial_enabled" value="1" <?php echo $interstitialEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('interstitial')">
          <span class="slider"></span>
        </label>
      </div>

      <div class="format-card-body" id="body_interstitial" style="<?php echo $interstitialEnabled ? '' : 'display: none;'; ?>">
        <div class="form-group" style="margin-top: 10px;">
          <label class="form-label" style="font-size: 12px;"><?php echo __('ইন্টারস্টিশিয়াল ইউনিট আইডি', 'Interstitial Unit ID'); ?></label>
          <input type="text" name="admob_interstitial_id" id="admob_interstitial_id" class="form-control" value="<?php echo htmlspecialchars($interstitialId); ?>" placeholder="ca-app-pub-6495900750071718/8521769602">
        </div>
        <div class="form-row" style="margin-top: 10px;">
          <div class="form-group">
            <label class="form-label" style="font-size: 12px;"><?php echo __('ব্যবধান ট্রানজিশন সংখ্যা', 'Impression Interval'); ?></label>
            <input type="number" name="ad_frequency_interval" class="form-control" value="<?php echo htmlspecialchars($intervalClicks); ?>" min="1" max="50">
          </div>
          <div class="form-group">
            <label class="form-label" style="font-size: 12px;"><?php echo __('কুলডাউন সময় (সেকেন্ড)', 'Cooldown (Seconds)'); ?></label>
            <input type="number" name="ad_interstitial_cooldown" class="form-control" value="<?php echo htmlspecialchars($interstitialCooldown); ?>" min="0" max="600">
          </div>
          <div class="form-group">
            <label class="form-label" style="font-size: 12px;"><?php echo __('হোমপেজ ট্রানজিশন', 'Home Navigation'); ?></label>
            <div style="display: flex; align-items: center; height: 38px;">
              <label class="switch">
                <input type="checkbox" name="ad_interstitial_home_enabled" value="1" <?php echo $interstitialHomeEnabled ? 'checked' : ''; ?>>
                <span class="slider"></span>
              </label>
              <span style="font-size: 12px; color: var(--text-muted); margin-left: 8px;"><?php echo __('ট্রিগার সক্রিয়', 'Trigger Enabled'); ?></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 3. Native Advanced Ad Section -->
    <div class="format-card-box <?php echo $nativeEnabled ? 'active-card' : ''; ?>" id="card_box_native">
      <div class="format-card-header">
        <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-shapes" style="color: var(--accent-mint);"></i>
          <?php echo __('নেটিভ অ্যাডভান্সড বিজ্ঞাপন', 'Native Advanced Ad'); ?>
          <span id="badge_native" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $nativeEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
            <?php echo $nativeEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
          </span>
        </span>
        <label class="switch">
          <input type="checkbox" name="ad_native_enabled" id="ad_native_enabled" value="1" <?php echo $nativeEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('native')">
          <span class="slider"></span>
        </label>
      </div>

      <div class="format-card-body" id="body_native" style="<?php echo $nativeEnabled ? '' : 'display: none;'; ?>">
        <div class="form-group" style="margin-top: 10px;">
          <label class="form-label" style="font-size: 12px;"><?php echo __('নেটিভ ইউনিট আইডি', 'Native Unit ID'); ?></label>
          <input type="text" name="admob_native_id" id="admob_native_id" class="form-control" value="<?php echo htmlspecialchars($nativeId); ?>" placeholder="ca-app-pub-6495900750071718/3896043165">
        </div>
      </div>
    </div>

    <!-- 4. Rewarded Video Ad Section -->
    <div class="format-card-box <?php echo $rewardedEnabled ? 'active-card' : ''; ?>" id="card_box_rewarded">
      <div class="format-card-header">
        <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-gift" style="color: #ec4899;"></i>
          <?php echo __('রিওয়ার্ডেড ভিডিও বিজ্ঞাপন', 'Rewarded Video Ad'); ?>
          <span id="badge_rewarded" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $rewardedEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
            <?php echo $rewardedEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
          </span>
        </span>
        <label class="switch">
          <input type="checkbox" name="ad_rewarded_enabled" id="ad_rewarded_enabled" value="1" <?php echo $rewardedEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('rewarded')">
          <span class="slider"></span>
        </label>
      </div>

      <div class="format-card-body" id="body_rewarded" style="<?php echo $rewardedEnabled ? '' : 'display: none;'; ?>">
        <div class="form-row" style="margin-top: 10px;">
          <div class="form-group" style="flex: 2;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('রিওয়ার্ডেড ইউনিট আইডি', 'Rewarded Unit ID'); ?></label>
            <input type="text" name="admob_rewarded_id" id="admob_rewarded_id" class="form-control" value="<?php echo htmlspecialchars($rewardedId); ?>" placeholder="ca-app-pub-3940256099942544/5224354917">
          </div>
          <div class="form-group" style="flex: 1;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('রিওয়ার্ড পয়েন্ট', 'Reward Points'); ?></label>
            <input type="number" name="ad_reward_amount" class="form-control" value="<?php echo htmlspecialchars($rewardAmount); ?>" min="1" max="1000">
          </div>
        </div>
      </div>
    </div>

    <!-- 5. App Open & Rewarded Interstitial Formats -->
    <div class="form-row">
      <!-- App Open Ad -->
      <div class="format-card-box <?php echo $appOpenEnabled ? 'active-card' : ''; ?>" id="card_box_app_open" style="flex: 1;">
        <div class="format-card-header">
          <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-door-open" style="color: var(--accent-blue);"></i>
            <?php echo __('অ্যাপ ওপেন বিজ্ঞাপন', 'App Open Ad'); ?>
            <span id="badge_app_open" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $appOpenEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
              <?php echo $appOpenEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
            </span>
          </span>
          <label class="switch">
            <input type="checkbox" name="ad_app_open_enabled" id="ad_app_open_enabled" value="1" <?php echo $appOpenEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('app_open')">
            <span class="slider"></span>
          </label>
        </div>

        <div class="format-card-body" id="body_app_open" style="<?php echo $appOpenEnabled ? '' : 'display: none;'; ?>">
          <div class="form-group" style="margin-top: 10px;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('অ্যাপ ওপেন ইউনিট আইডি', 'App Open Unit ID'); ?></label>
            <input type="text" name="admob_app_open_id" id="admob_app_open_id" class="form-control" value="<?php echo htmlspecialchars($appOpenId); ?>" placeholder="ca-app-pub-3940256099942544/9257395921">
          </div>
          <div class="form-group" style="margin-top: 10px;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('কুলডাউন (সেকেন্ড)', 'Cooldown (Seconds)'); ?></label>
            <input type="number" name="ad_app_open_cooldown" class="form-control" value="<?php echo htmlspecialchars($appOpenCooldown); ?>" min="0">
          </div>
        </div>
      </div>

      <!-- Rewarded Interstitial Ad -->
      <div class="format-card-box <?php echo $rewardedInterstitialEnabled ? 'active-card' : ''; ?>" id="card_box_rewarded_interstitial" style="flex: 1;">
        <div class="format-card-header">
          <span style="font-weight: 700; font-size: 13.5px; color: var(--text-primary); display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-trophy" style="color: var(--accent-gold);"></i>
            <?php echo __('রিওয়ার্ডেড ইন্টারস্টিশিয়াল', 'Rewarded Interstitial'); ?>
            <span id="badge_rewarded_interstitial" class="badge" style="font-size: 10.5px; padding: 2px 6px; <?php echo $rewardedInterstitialEnabled ? 'background: rgba(16,185,129,0.15); color: var(--primary);' : 'background: rgba(148,163,184,0.15); color: var(--text-muted);'; ?>">
              <?php echo $rewardedInterstitialEnabled ? __('সক্রিয়', 'Active') : __('বন্ধ', 'Off'); ?>
            </span>
          </span>
          <label class="switch">
            <input type="checkbox" name="ad_rewarded_interstitial_enabled" id="ad_rewarded_interstitial_enabled" value="1" <?php echo $rewardedInterstitialEnabled ? 'checked' : ''; ?> onchange="toggleFormatContainer('rewarded_interstitial')">
            <span class="slider"></span>
          </label>
        </div>

        <div class="format-card-body" id="body_rewarded_interstitial" style="<?php echo $rewardedInterstitialEnabled ? '' : 'display: none;'; ?>">
          <div class="form-group" style="margin-top: 10px;">
            <label class="form-label" style="font-size: 12px;"><?php echo __('ইউনিট আইডি', 'Unit ID'); ?></label>
            <input type="text" name="admob_rewarded_interstitial_id" id="admob_rewarded_interstitial_id" class="form-control" value="<?php echo htmlspecialchars($rewardedInterstitialId); ?>" placeholder="ca-app-pub-3940256099942544/5354046379">
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- CARD 3: অ্যাডমিন ডিভাইস ও আইপি সুরক্ষা (Admin Ad Suppression) -->
  <div class="policy-card" style="margin-bottom: 20px;">
    <div class="policy-header">
      <div class="policy-title-row" style="justify-content: space-between;">
        <div style="display: flex; align-items: center; gap: 8px;">
          <span class="policy-dot" style="background: var(--accent-blue);"></span>
          <span class="policy-title"><?php echo __('অ্যাডমিন ডিভাইস ও আইপি সুরক্ষা', 'Admin Device & IP Ad Filter'); ?></span>
        </div>
        <label class="switch-label">
          <span style="font-size: 13.5px; font-weight: 700;"><?php echo __('অ্যাডমিন বিজ্ঞাপন ব্লক:', 'Block Admin Ads:'); ?></span>
          <label class="switch">
            <input type="checkbox" name="admin_block_ads" id="admin_block_ads" value="1" <?php echo $adminBlockAds ? 'checked' : ''; ?> onchange="toggleAdminBlockBody()">
            <span class="slider"></span>
          </label>
        </label>
      </div>
    </div>

    <div id="body_admin_block" style="<?php echo $adminBlockAds ? '' : 'display: none;'; ?>">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px; margin-bottom: 8px; flex-wrap: wrap; gap: 8px;">
        <div style="font-size: 12.5px; color: var(--text-muted);">
          <?php echo __('আপনার বর্তমান আইপি:', 'Your Current IP:'); ?> 
          <code style="background: var(--bg-main); padding: 2px 6px; border-radius: 4px; font-family: monospace; color: var(--primary); font-weight: 700;">
            <?php echo htmlspecialchars($currentAdminIp); ?>
          </code>
        </div>
        <button type="button" class="btn-topbar-pill" style="font-size: 11.5px; padding: 4px 10px;" onclick="addCurrentAdminIp('<?php echo htmlspecialchars($currentAdminIp); ?>')">
          <i class="fa-solid fa-plus"></i> <?php echo __('বর্তমান আইপি যুক্ত করুন', 'Add Current IP'); ?>
        </button>
      </div>

      <div class="form-group">
        <label class="form-label" style="font-size: 12px;"><?php echo __('অ্যাডমিন আইপি তালিকা (কমা দ্বারা পৃথক করুন)', 'Admin IP Whitelist (Comma-separated)'); ?></label>
        <textarea name="admin_blocked_ips" id="admin_blocked_ips" class="form-control" rows="2" placeholder="127.0.0.1, ::1, 103.xxx.xxx.xxx"><?php echo htmlspecialchars($adminBlockedIps); ?></textarea>
        <div style="font-size: 11.5px; color: var(--text-muted); margin-top: 4px;">
          <?php echo __('তালিকাভুক্ত আইপি বা অ্যাডমিন ডিভাইস থেকে অ্যাপে ঢুকলে কোনো বিজ্ঞাপন প্রদর্শিত বা লোড হবে না।', 'Devices connecting from these IPs will never receive or see any ads.'); ?>
        </div>
      </div>
    </div>
  </div>

  <!-- CARD 4: গুগল পলিসি ও সুরক্ষা বিধান (Google Policy & Compliance) -->
  <div class="policy-card" style="margin-bottom: 20px;">
    <div class="policy-header">
      <div class="policy-title-row">
        <span class="policy-dot" style="background: var(--accent-mint);"></span>
        <span class="policy-title"><?php echo __('গুগল পলিসি ও পারিবারিক সুরক্ষা', 'Google Policy & Family Protection'); ?></span>
      </div>
    </div>

    <div class="form-row" style="margin-top: 10px;">
      <div class="form-group" style="flex: 1;">
        <label class="form-label" style="font-size: 12px;"><?php echo __('সর্বোচ্চ কনটেন্ট রেটিং', 'Max Ad Content Rating'); ?></label>
        <select name="ad_content_rating" class="form-control">
          <option value="G" <?php echo $adContentRating === 'G' ? 'selected' : ''; ?>><?php echo __('G - সাধারণ দর্শক (ইসলামিক ও পারিবারিক উপযুক্ত)', 'G - General Audience (Recommended)'); ?></option>
          <option value="PG" <?php echo $adContentRating === 'PG' ? 'selected' : ''; ?>><?php echo __('PG - পিতামাতার তত্ত্বাবধান', 'PG - Parental Guidance'); ?></option>
          <option value="T" <?php echo $adContentRating === 'T' ? 'selected' : ''; ?>><?php echo __('T - কিশোর', 'T - Teen'); ?></option>
        </select>
        <div style="font-size: 11.5px; color: var(--text-muted); margin-top: 4px;">
          <?php echo __('ইসলামিক অ্যাপ হিসেবে "G" রেটিং আবশ্যক যাতে কোনো অননুমোদিত বিজ্ঞাপন না আসে।', 'Mandatory G rating to block sensitive or adult ads.'); ?>
        </div>
      </div>

      <div class="form-group" style="flex: 1;">
        <label class="form-label" style="font-size: 12px;"><?php echo __('টেস্ট ডিভাইস আইডি', 'Test Device ID'); ?></label>
        <input type="text" name="ad_test_device_id" class="form-control" value="<?php echo htmlspecialchars($adTestDeviceId); ?>" placeholder="e.g. 33BE2250B43518CCDA7DE426D04EE231">
        <div style="font-size: 11.5px; color: var(--text-muted); margin-top: 4px;">
          <?php echo __('ইনভ্যালিড ট্রাফিক নিষেধাজ্ঞা এড়াতে পরীক্ষক ডিভাইসের হ্যাশড আইডি।', 'Hashed device ID to prevent accidental self-clicks and invalid traffic.'); ?>
        </div>
      </div>
    </div>

    <!-- Safety Tips Box -->
    <div style="background: rgba(16, 185, 129, 0.06); border: 1px solid rgba(16, 185, 129, 0.2); border-radius: 8px; padding: 12px 16px; margin-top: 12px;">
      <div style="font-weight: 700; font-size: 12.5px; color: var(--primary); margin-bottom: 6px; display: flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-shield-halved"></i> <?php echo __('গুগল অ্যাডমব স্থায়ী নীতিমালা ও নিরাপত্তা বিধান', 'Google AdMob Permanent Policy Rules'); ?>
      </div>
      <ul style="margin: 0; padding-left: 18px; font-size: 12px; color: var(--text-muted); line-height: 1.6;">
        <li><?php echo __('১. নিজের অ্যাপের বিজ্ঞাপনে ক্লিক করা সম্পূর্ণ নিষিদ্ধ (Invalid Traffic নিষেধাজ্ঞা)।', '1. Never click own ads to prevent permanent account suspension.'); ?></li>
        <li><?php echo __('২. অ্যাপ বন্ধ বা ব্যাক প্রেস করার সময় ইন্টারস্টিশিয়াল বিজ্ঞাপন দেখানো গুগলের কঠোর নীতিবিরুদ্ধ।', '2. Interstitials are strictly prohibited upon app exit or back press.'); ?></li>
        <li><?php echo __('৩. বাটনের সাথে ব্যানারের সংঘর্ষ এড়াতে উপযুক্ত নিরাপদ দূরত্ব কোডে নিশ্চিত করা আছে।', '3. Safe padding is enforced around banners to prevent accidental clicks.'); ?></li>
        <li><?php echo __('৪. রিওয়ার্ডেড বিজ্ঞাপন সম্পূর্ণ ব্যবহারকারীর সম্মতিক্রমে ইন-অ্যাপ পয়েন্ট অর্জনে প্লে হয়।', '4. Rewarded ads are 100% opt-in for in-app points.'); ?></li>
      </ul>
    </div>
  </div>

  <!-- Bottom Save Action -->
  <div style="display: flex; justify-content: flex-end; margin-bottom: 24px;">
    <button type="submit" class="btn-save-settings">
      ✓ <?php echo __('বিজ্ঞাপন কনফিগারেশন সংরক্ষণ', 'Save Ad Configuration'); ?>
    </button>
  </div>
</form>

<!-- CARD 4: সাম্প্রতিক অডিট লগ -->
<div class="policy-card" style="margin-bottom: 30px;">
  <div class="policy-header">
    <div class="policy-title-row">
      <span class="policy-dot" style="background: var(--primary);"></span>
      <span class="policy-title"><?php echo __('সাম্প্রতিক অডিট লগ', 'Recent Audit Logs'); ?></span>
    </div>
  </div>

  <?php if (empty($auditLogs)): ?>
    <div style="text-align: center; padding: 24px; color: var(--text-muted); font-size: 13px;">
      <?php echo __('কোনো সাম্প্রতিক অডিট লগ পাওয়া যায়নি।', 'No recent audit logs found.'); ?>
    </div>
  <?php else: ?>
    <div style="overflow-x: auto;">
      <table class="table-custom" style="width: 100%; font-size: 12.5px;">
        <thead>
          <tr style="border-bottom: 1px solid var(--border-color); text-align: left;">
            <th style="padding: 10px;"><?php echo __('তারিখ ও সময়', 'Date & Time'); ?></th>
            <th style="padding: 10px;"><?php echo __('অ্যাডমিন', 'Admin'); ?></th>
            <th style="padding: 10px;"><?php echo __('আইপি', 'IP Address'); ?></th>
            <th style="padding: 10px;"><?php echo __('পরিবর্তনের বিবরণ', 'Details'); ?></th>
          </tr>
        </thead>
        <tbody>
          <?php foreach ($auditLogs as $log): ?>
            <tr style="border-bottom: 1px solid var(--border-color);">
              <td style="padding: 10px; color: var(--text-muted); white-space: nowrap;">
                <?php echo htmlspecialchars($log['created_at']); ?>
              </td>
              <td style="padding: 10px; font-weight: 600;">
                <?php echo htmlspecialchars($log['admin_name'] ?? 'Admin #' . $log['admin_id']); ?>
              </td>
              <td style="padding: 10px; font-family: monospace; font-size: 11.5px; color: var(--text-muted);">
                <?php echo htmlspecialchars($log['ip_address']); ?>
              </td>
              <td style="padding: 10px; color: var(--text-primary);">
                <?php echo htmlspecialchars($log['details'] ?? ''); ?>
              </td>
            </tr>
          <?php endforeach; ?>
        </tbody>
      </table>
    </div>
  <?php endif; ?>
</div>

<script>
// Select Ad Mode & Toggle Pill Button Styling
function selectAdMode(mode) {
  var prodPill = document.getElementById('pill_mode_prod');
  var testPill = document.getElementById('pill_mode_test');
  var prodRadio = prodPill.querySelector('input[type="radio"]');
  var testRadio = testPill.querySelector('input[type="radio"]');

  if (mode === 'test') {
    testRadio.checked = true;
    prodRadio.checked = false;
    testPill.classList.add('active-test');
    prodPill.classList.remove('active-prod');
  } else {
    prodRadio.checked = true;
    testRadio.checked = false;
    prodPill.classList.add('active-prod');
    testPill.classList.remove('active-test');
  }
}

// Collapsible Format Card Toggling
function toggleFormatContainer(formatKey) {
  var chk = document.getElementById('ad_' + formatKey + '_enabled');
  var body = document.getElementById('body_' + formatKey);
  var box = document.getElementById('card_box_' + formatKey);
  var badge = document.getElementById('badge_' + formatKey);

  if (!chk || !body) return;

  if (chk.checked) {
    body.style.display = 'block';
    if (box) box.classList.add('active-card');
    if (badge) {
      badge.textContent = '<?php echo __('সক্রিয়', 'Active'); ?>';
      badge.style.background = 'rgba(16,185,129,0.15)';
      badge.style.color = 'var(--primary)';
    }
  } else {
    body.style.display = 'none';
    if (box) box.classList.remove('active-card');
    if (badge) {
      badge.textContent = '<?php echo __('বন্ধ', 'Off'); ?>';
      badge.style.background = 'rgba(148,163,184,0.15)';
      badge.style.color = 'var(--text-muted)';
    }
  }
}

// Admin Block Body Toggling
function toggleAdminBlockBody() {
  var chk = document.getElementById('admin_block_ads');
  var body = document.getElementById('body_admin_block');
  if (chk && body) {
    body.style.display = chk.checked ? 'block' : 'none';
  }
}

// Add Current Admin IP to Whitelist Input
function addCurrentAdminIp(ip) {
  if (!ip) return;
  var input = document.getElementById('admin_blocked_ips');
  if (!input) return;
  var val = input.value.trim();
  if (val.length === 0) {
    input.value = ip;
  } else {
    var ips = val.split(',').map(function(s) { return s.trim(); });
    if (ips.indexOf(ip) === -1) {
      input.value = val + ', ' + ip;
    }
  }
}

// Load Official Test Presets
function loadTestPreset() {
  selectAdMode('test');
  document.getElementById('admob_app_id').value = '<?php echo $testAppId; ?>';
  document.getElementById('admob_banner_id').value = '<?php echo $testBannerId; ?>';
  document.getElementById('admob_interstitial_id').value = '<?php echo $testInterstitialId; ?>';
  document.getElementById('admob_native_id').value = '<?php echo $testNativeId; ?>';
  document.getElementById('admob_rewarded_id').value = '<?php echo $testRewardedId; ?>';
  document.getElementById('admob_app_open_id').value = '<?php echo $testAppOpenId; ?>';
  document.getElementById('admob_rewarded_interstitial_id').value = '<?php echo $testRewardedInterstitialId; ?>';
}

// Load Official Production Presets
function loadProdPreset() {
  selectAdMode('production');
  document.getElementById('admob_app_id').value = '<?php echo $defaultProdAppId; ?>';
  document.getElementById('admob_banner_id').value = '<?php echo $defaultProdBannerId; ?>';
  document.getElementById('admob_interstitial_id').value = '<?php echo $defaultProdInterstitialId; ?>';
  document.getElementById('admob_native_id').value = '<?php echo $defaultProdNativeId; ?>';
  document.getElementById('admob_rewarded_id').value = '<?php echo $defaultProdRewardedId; ?>';
  document.getElementById('admob_app_open_id').value = '<?php echo $defaultProdAppOpenId; ?>';
  document.getElementById('admob_rewarded_interstitial_id').value = '<?php echo $defaultProdRewardedInterstitialId; ?>';
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
