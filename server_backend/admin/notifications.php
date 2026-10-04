<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - PUSH NOTIFICATIONS & BROADCASTS MANAGER
 * পুশ নোটিফিকেশন, অ্যাপ আপডেট অ্যালার্ট ও ফিচার রিমাইন্ডার সেন্টার
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();
$adminUser = getAdminUser();

// Helper function to dispatch FCM Push Notification (if configured)
function sendFcmBroadcastNotification($pdo, $title, $body, $action, $priority, $imageUrl = null) {
    try {
        $stmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'fcm_server_key' LIMIT 1");
        $stmt->execute();
        $fcmKey = $stmt->fetchColumn();

        if (empty($fcmKey)) {
            return false; // FCM not yet configured, purely in-app broadcast
        }

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = [
            'to' => '/topics/all_users',
            'priority' => ($priority === 'CRITICAL' || $priority === 'HIGH') ? 'high' : 'normal',
            'notification' => [
                'title' => $title,
                'body' => $body,
                'sound' => 'default',
                'image' => !empty($imageUrl) ? $imageUrl : null
            ],
            'data' => [
                'title' => $title,
                'body' => $body,
                'type' => 'admin_announcement',
                'deep_link' => $action,
                'priority' => strtolower($priority),
                'timestamp' => (string)time()
            ]
        ];

        $headers = [
            'Authorization: key=' . trim($fcmKey),
            'Content-Type: application/json'
        ];

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);
        curl_close($ch);

        return true;
    } catch (Exception $e) {
        return false;
    }
}

// Handle Notification Dispatch (POST)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর। আবার চেষ্টা করুন।', 'Invalid security token. Please try again.'));
        redirect('notifications.php');
    }

    if ($action === 'send_push') {
        $title = trim($_POST['title'] ?? '');
        $body = trim($_POST['body'] ?? '');
        $notifType = trim($_POST['notification_type'] ?? 'ANNOUNCEMENT');
        $targetAction = trim($_POST['target_action'] ?? 'default');
        $targetUrl = trim($_POST['target_url'] ?? '');
        $imageUrl = trim($_POST['image_url'] ?? '');
        $priority = trim($_POST['priority'] ?? 'HIGH');
        $audience = trim($_POST['target_audience'] ?? 'ALL');
        $updateHomeScreen = isset($_POST['update_home_banner']) ? 1 : 0;

        if (empty($title) || empty($body)) {
            setFlash('danger', __('নোটিফিকেশনের শিরোনাম এবং বার্তা উভয়ই আবশ্যক!', 'Both notification title and message body are required!'));
        } else {
            try {
                // Deactivate older active APP_UPDATE notifications so only the latest update broadcast remains active
                if ($notifType === 'APP_UPDATE') {
                    $pdo->prepare("UPDATE push_notifications SET is_active = 0 WHERE notification_type = 'APP_UPDATE'")->execute();
                }

                // 1. Save record in push_notifications table
                $stmt = $pdo->prepare("INSERT INTO push_notifications (title, body, notification_type, target_action, target_url, image_url, priority, target_audience, fcm_status, sent_count, created_by) 
                                       VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'SENT', 1, ?)");
                $stmt->execute([
                    $title, 
                    $body, 
                    $notifType, 
                    $targetAction, 
                    !empty($targetUrl) ? $targetUrl : null, 
                    !empty($imageUrl) ? $imageUrl : null, 
                    $priority, 
                    $audience, 
                    $adminUser['username']
                ]);
                $notifId = $pdo->lastInsertId();

                // 2. If checked or if App Update, update app_settings so in-app banner & AdminRemoteConfigManager syncs immediately
                if ($updateHomeScreen || $notifType === 'APP_UPDATE' || $notifType === 'NEW_FEATURE') {
                    $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'announcement_banner_text'")->execute([$body]);
                    $pdo->prepare("UPDATE app_settings SET setting_value = '1' WHERE setting_key = 'announcement_active'")->execute();
                    $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'announcement_deep_link'")->execute([$targetAction]);
                }

                // 3. Dispatch to Firebase Cloud Messaging (if server key set)
                sendFcmBroadcastNotification($pdo, $title, $body, $targetAction, $priority, $imageUrl);

                logAdminAction($pdo, 'DISPATCH_NOTIFICATION', 'push_notifications', (string)$notifId, "পুশ নোটিফিকেশন প্রেরণ: $title ($notifType)");
                setFlash('success', __('🎉 পুশ নোটিফিকেশন সফলভাবে তৈরি ও সমস্ত অ্যাপ ব্যবহারকারীর নিকট প্রেরণ করা হয়েছে!', '🎉 Push notification successfully created and broadcast to all app users!'));
            } catch (Exception $e) {
                setFlash('danger', __('নোটিফিকেশন প্রেরণ ব্যর্থ: ', 'Notification dispatch failed: ') . $e->getMessage());
            }
        }
    }

    if ($action === 'save_fcm_key') {
        $fcmKey = trim($_POST['fcm_server_key'] ?? '');
        try {
            $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value, description) 
                           VALUES ('fcm_server_key', ?, 'Firebase Cloud Messaging Server API Key') 
                           ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)")->execute([$fcmKey]);
            logAdminAction($pdo, 'UPDATE_FCM_KEY', 'app_settings', 'fcm_server_key', 'FCM সার্ভার কি আপডেট করা হয়েছে');
            setFlash('success', __('FCM সার্ভার কী সফলভাবে সংরক্ষণ করা হয়েছে!', 'FCM Server Key saved successfully!'));
        } catch (Exception $e) {
            setFlash('danger', __('সংরক্ষণ ব্যর্থ: ', 'Save failed: ') . $e->getMessage());
        }
    }

    redirect('notifications.php');
}

// Handle GET Actions (Resend or Delete)
if (isset($_GET['resend'])) {
    $rId = (int)$_GET['resend'];
    try {
        $stmt = $pdo->prepare("SELECT * FROM push_notifications WHERE id = ?");
        $stmt->execute([$rId]);
        $notif = $stmt->fetch();

        if ($notif) {
            // Re-update app settings
            $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'announcement_banner_text'")->execute([$notif['body']]);
            $pdo->prepare("UPDATE app_settings SET setting_value = '1' WHERE setting_key = 'announcement_active'")->execute();
            $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'announcement_deep_link'")->execute([$notif['target_action']]);
            $pdo->prepare("UPDATE push_notifications SET sent_count = sent_count + 1, created_at = NOW() WHERE id = ?")->execute([$rId]);

            sendFcmBroadcastNotification($pdo, $notif['title'], $notif['body'], $notif['target_action'], $notif['priority'], $notif['image_url']);
            logAdminAction($pdo, 'RESEND_NOTIFICATION', 'push_notifications', (string)$rId, "পুনরায় নোটিফিকেশন পাঠানো: " . $notif['title']);
            setFlash('success', __('নোটিফিকেশনটি পুনরায় সফলভাবে ব্রডকাস্ট করা হয়েছে!', 'Notification successfully rebroadcast!'));
        }
    } catch (Exception $e) {
        setFlash('danger', __('পুনরায় পাঠাতে ব্যর্থ: ', 'Failed to rebroadcast: ') . $e->getMessage());
    }
    redirect('notifications.php');
}

if (isset($_GET['delete_notif'])) {
    $delId = (int)$_GET['delete_notif'];
    try {
        $pdo->prepare("DELETE FROM push_notifications WHERE id = ?")->execute([$delId]);
        logAdminAction($pdo, 'DELETE_NOTIFICATION', 'push_notifications', (string)$delId, 'নোটিফিকেশন লগ মুছে ফেলা হয়েছে');
        setFlash('success', __('নোটিফিকেশন হিস্ট্রি থেকে সফলভাবে মুছে ফেলা হয়েছে।', 'Notification successfully deleted from history.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
    }
    redirect('notifications.php');
}

// Fetch Current Settings & Notifications List
$fcmKey = '';
try {
    $stmt = $pdo->query("SELECT setting_value FROM app_settings WHERE setting_key = 'fcm_server_key'");
    $fcmKey = $stmt->fetchColumn() ?: '';
} catch (Exception $e) {}

$notifications = [];
try {
    $notifications = $pdo->query("SELECT * FROM push_notifications ORDER BY id DESC LIMIT 50")->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('নোটিফিকেশন', 'Notifications');
$activeNav = 'notifications';
require_once __DIR__ . '/header.php';
?>

<!-- Quick Preset Broadcast Templates Dropdown Toolbar -->
<div class="card" style="margin-bottom: 20px; border-left: 4px solid var(--primary);">
  <div class="card-body" style="padding: 14px 20px; display: flex; align-items: center; justify-content: space-between; gap: 14px; flex-wrap: wrap;">
    <div style="display: flex; align-items: center; gap: 8px;">
      <span style="font-size: 16px;">⚡</span>
      <span style="font-weight: 700; font-size: 14px; color: var(--text-primary);"><?php echo __('নোটিফিকেশন টেমপ্লেট', 'Notification Template'); ?></span>
    </div>
    <div style="flex: 1; max-width: 360px; min-width: 240px;">
      <select id="templatePresetSelect" class="form-control" onchange="applyPresetFromDropdown(this.value)" style="height: 38px; font-weight: 600; cursor: pointer;">
        <option value="">-- <?php echo __('টেমপ্লেট নির্বাচন করুন', 'Select Template'); ?> --</option>
        <option value="update">🚀 <?php echo __('অ্যাপ আপডেট', 'App Update'); ?></option>
        <option value="battle">⚔️ <?php echo __('নলেজ ব্যাটেল', 'Knowledge Battle'); ?></option>
        <option value="friday">🕌 <?php echo __("জুম'আ মুবারক", 'Jummah Mubarak'); ?></option>
        <option value="blood">🩸 <?php echo __('রক্তের আবেদন', 'Blood Request'); ?></option>
        <option value="quran">📖 <?php echo __('কুরআন তিলাওয়াত', 'Quran Recitation'); ?></option>
        <option value="hadith">📜 <?php echo __('হাদিস রিমাইন্ডার', 'Hadith Reminder'); ?></option>
      </select>
    </div>
  </div>
</div>

<div class="form-row" style="margin-bottom: 28px;">
  <!-- Compose Notification Box -->
  <div class="card" style="flex: 1.6; margin-bottom: 0;">
    <div class="card-header">
      <div class="card-title">🔔 <?php echo __('নতুন নোটিফিকেশন', 'New Notification'); ?></div>
    </div>
    <div class="card-body">
      <form method="POST" action="notifications.php" id="pushForm" onsubmit="return handlePushSubmit(this);">
        <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
        <input type="hidden" name="action" value="send_push">

        <div class="form-group">
          <label class="form-label"><?php echo __('শিরোনাম', 'Title'); ?> <span style="color: var(--accent-red);">*</span></label>
          <input type="text" name="title" id="notifTitle" class="form-control" placeholder="<?php echo __('যেমন: নতুন আপডেট ভার্সন এসেছে! এখনই আপডেট করুন', 'e.g. New update version available! Update now'); ?>" required>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('বার্তা', 'Message'); ?> <span style="color: var(--accent-red);">*</span></label>
          <textarea name="body" id="notifBody" class="form-control" rows="3" placeholder="<?php echo __('প্রিয় ভাই ও বোনেরা, দীন ওয়ান অ্যাপে নতুন ফিচার এবং দ্রুততর পারফরম্যান্স যুক্ত হয়েছে...', 'Dear brothers and sisters, new features and faster performance have arrived in DeenOne...'); ?>" required></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('ক্যাটাগরি', 'Category'); ?></label>
            <select name="notification_type" id="notifType" class="form-control">
              <option value="APP_UPDATE"><?php echo __('🚀 অ্যাপ আপডেট', '🚀 App Update'); ?></option>
              <option value="NEW_FEATURE"><?php echo __('✨ নতুন ফিচার ঘোষণা', '✨ New Feature Announcement'); ?></option>
              <option value="ANNOUNCEMENT" selected><?php echo __('📢 সাধারণ ইসলামিক বার্তা', '📢 General Islamic Message'); ?></option>
              <option value="QUIZ_BATTLE"><?php echo __('⚔️ নলেজ ব্যাটেল ইভেন্ট', '⚔️ Knowledge Battle Event'); ?></option>
              <option value="BLOOD_EMERGENCY"><?php echo __('🩸 জরুরি রক্তদান আবেদন', '🩸 Emergency Blood Request'); ?></option>
              <option value="RAMADAN_SPECIAL"><?php echo __('🌙 মাহে রমাদান স্পেশাল', '🌙 Ramadan Mubarak Special'); ?></option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label"><?php echo __('টার্গেট স্ক্রিন', 'Target Screen'); ?></label>
            <select name="target_action" id="targetAction" class="form-control">
              <option value="app_update"><?php echo __('🚀 প্লে স্টোর অথবা অ্যাপ আপডেট পেজ', '🚀 Play Store / App Update Page'); ?></option>
              <option value="action_knowledge_battle"><?php echo __('⚔️ ইসলামিক নলেজ ব্যাটেল', '⚔️ Islamic Knowledge Battle'); ?></option>
              <option value="action_quran"><?php echo __('📖 কুরআন মাজিদ হাব', '📖 Holy Quran Hub'); ?></option>
              <option value="action_hadith"><?php echo __('📜 সহীহ হাদিস শরীফ', '📜 Sahih Hadith Collection'); ?></option>
              <option value="action_dua"><?php echo __('🤲 সহীহ দু’আ ও যিকির', '🤲 Authentic Duas & Azkar'); ?></option>
              <option value="action_blood"><?php echo __('🩸 ইসলামিক রক্তদান নেটওয়ার্ক', '🩸 Islamic Blood Donor Network'); ?></option>
              <option value="action_quiz"><?php echo __('❓ দৈনিক কুইজ প্রতিযোগিতা', '❓ Daily Quiz Competition'); ?></option>
              <option value="action_mosque"><?php echo __('🕌 মসজিদ ও জামাত সন্ধান', '🕌 Mosque & Jamat Finder'); ?></option>
              <option value="action_amal"><?php echo __('✅ দৈনিক আমল ট্র্যাকার', '✅ Daily Amal Tracker'); ?></option>
              <option value="action_tasbih"><?php echo __('📿 ডিজিটাল তাসবিহ কাউন্টার', '📿 Digital Tasbih Counter'); ?></option>
              <option value="action_roza"><?php echo __('🌙 রোজা ও রমজান ট্র্যাকার', '🌙 Fasting & Ramadan Tracker'); ?></option>
              <option value="action_zakat"><?php echo __('💰 যাকাত ক্যালকুলেটর', '💰 Zakat Calculator'); ?></option>
              <option value="external_url"><?php echo __('🌐 বাহ্যিক ওয়েবসাইট লিংক', '🌐 External Website Link'); ?></option>
              <option value="default"><?php echo __('📱 সাধারণ হোমস্ক্রিন', '📱 Default Home Screen'); ?></option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('প্রায়োরিটি', 'Priority'); ?></label>
            <select name="priority" id="notifPriority" class="form-control">
              <option value="HIGH" selected><?php echo __('উচ্চ - সাউন্ড ও পপআপসহ প্রদর্শিত হবে', 'High - Audio Sound & Heads-up Display'); ?></option>
              <option value="CRITICAL"><?php echo __('সর্বোচ্চ অ্যালার্ট - বাধ্যতামূলক দৃষ্টি আকর্ষণ', 'Critical - Mandatory System Alert'); ?></option>
              <option value="NORMAL"><?php echo __('সাধারণ - স্ট্যান্ডার্ড ট্রে নোটিফিকেশন', 'Normal - Standard Tray Notification'); ?></option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label"><?php echo __('অডিয়েন্স', 'Audience'); ?></label>
            <select name="target_audience" id="notifAudience" class="form-control">
              <option value="ALL" selected><?php echo __('সকল সক্রিয় ইউজার', 'All Active Users'); ?></option>
              <option value="DISTRICT_DHAKA"><?php echo __('ঢাকা অঞ্চল', 'Dhaka Region'); ?></option>
              <option value="DISTRICT_CHITTAGONG"><?php echo __('চট্টগ্রাম অঞ্চল', 'Chittagong Region'); ?></option>
              <option value="DISTRICT_SYLHET"><?php echo __('সিলেট অঞ্চল', 'Sylhet Region'); ?></option>
            </select>
          </div>
        </div>

        <div class="form-group" id="urlGroup" style="display: none;">
          <label class="form-label"><?php echo __('ওয়েবসাইট লিংক', 'Website URL'); ?></label>
          <input type="url" name="target_url" id="notifUrl" class="form-control" placeholder="https://example.com/update">
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('ব্যানার ছবি', 'Banner Image'); ?></label>
          <input type="url" name="image_url" id="notifImage" class="form-control" placeholder="https://domain.com/banner.jpg">
        </div>

        <div class="form-group">
          <label class="switch-label">
            <label class="switch">
              <input type="checkbox" name="update_home_banner" value="1" checked>
              <span class="slider"></span>
            </label>
            <span><?php echo __('হোমস্ক্রিন ব্যানারে প্রদর্শন করুন', 'Show on Home Banner'); ?></span>
          </label>
        </div>

        <button type="submit" class="btn btn-primary" style="padding: 12px 28px; font-size: 15px; font-weight: 700; width: 100%;">
          🚀 <?php echo __('পাঠান', 'Send'); ?>
        </button>
      </form>
    </div>
  </div>

  <!-- Live Phone Preview Card & FCM Config -->
  <div style="flex: 1; display: flex; flex-direction: column; gap: 20px;">
    <!-- Phone Notification Mockup -->
    <div class="card" style="margin-bottom: 0;">
      <div class="card-header">
        <div class="card-title">📱 <?php echo __('মোবাইল স্ক্রিন প্রিভিউ', 'Mobile Screen Preview'); ?></div>
      </div>
      <div class="card-body" style="background: #0f172a; border-radius: 12px; padding: 18px;">
        <div style="background: rgba(30, 41, 59, 0.95); border: 1px solid rgba(255,255,255,0.1); border-radius: 12px; padding: 14px; box-shadow: 0 10px 25px rgba(0,0,0,0.5);">
          <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 6px;">
            <div style="width: 20px; height: 20px; border-radius: 4px; background: var(--primary); display: flex; align-items: center; justify-content: center; font-size: 11px; color: #fff;">
              ☪
            </div>
            <span style="font-size: 12px; font-weight: 700; color: #f8fafc;">DeenOne</span>
            <span style="font-size: 10px; color: #94a3b8; margin-left: auto;"><?php echo __('এইমাত্র', 'Just now'); ?></span>
          </div>
          <div id="previewTitle" style="font-size: 14px; font-weight: 700; color: #ffffff; margin-bottom: 4px;">
            <?php echo __('নতুন আপডেট ভার্সন এসেছে!', 'New update version is available!'); ?>
          </div>
          <div id="previewBody" style="font-size: 12px; color: #cbd5e1; line-height: 1.5;">
            <?php echo __('দীন ওয়ান অ্যাপের নতুন ভার্সন রিলিজ হয়েছে। এখনই আপডেট করে নতুন ফিচার উপভোগ করুন।', 'A new version of DeenOne is released. Update now to enjoy new features.'); ?>
          </div>
          <div style="margin-top: 10px; display: flex; align-items: center; gap: 8px;">
            <span class="badge badge-info" id="previewBadge" style="font-size: 11px;">🚀 <?php echo __('প্লে স্টোর অথবা অ্যাপ আপডেট পেজ', 'Play Store / App Update Page'); ?></span>
          </div>
        </div>
      </div>
    </div>

    <!-- FCM Cloud Settings -->
    <div class="card" style="margin-bottom: 0;">
      <div class="card-header">
        <div class="card-title" style="font-size: 14px;">☁️ <?php echo __('FCM সেটিংস', 'FCM Settings'); ?></div>
      </div>
      <div class="card-body">
        <form method="POST" action="notifications.php">
          <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
          <input type="hidden" name="action" value="save_fcm_key">

          <div class="form-group">
            <label class="form-label" style="font-size: 12px;"><?php echo __('FCM সার্ভার কী', 'FCM Server API Key'); ?></label>
            <input type="password" name="fcm_server_key" class="form-control" placeholder="AAAA... Firebase Server Key" value="<?php echo htmlspecialchars($fcmKey); ?>">
          </div>

          <button type="submit" class="btn btn-secondary btn-sm" style="width: 100%;">
            💾 <?php echo __('সংরক্ষণ করুন', 'Save'); ?>
          </button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Notification History Log Table -->
<div class="card">
  <div class="card-header">
    <div class="card-title">📜 <?php echo __('নোটিফিকেশন হিস্ট্রি', 'History'); ?></div>
    <span class="badge badge-info"><?php echo toLangNum(count($notifications)); ?></span>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 50px;"><?php echo __('আইডি', 'ID'); ?></th>
          <th><?php echo __('বার্তা', 'Message'); ?></th>
          <th><?php echo __('ক্যাটাগরি', 'Category'); ?></th>
          <th><?php echo __('টার্গেট', 'Target'); ?></th>
          <th><?php echo __('অডিয়েন্স', 'Audience'); ?></th>
          <th><?php echo __('অবস্থা', 'Status'); ?></th>
          <th><?php echo __('তারিখ', 'Date'); ?></th>
          <th style="text-align: right;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($notifications)): ?>
          <tr><td colspan="8" style="text-align: center; color: var(--text-dim);"><?php echo __('কোনো পুশ নোটিফিকেশন হিস্ট্রি পাওয়া যায়নি।', 'No push notification history found.'); ?></td></tr>
        <?php else: ?>
          <?php foreach ($notifications as $n): ?>
            <tr>
              <td>#<?php echo toLangNum($n['id']); ?></td>
              <td>
                <strong style="color: #ffffff;"><?php echo htmlspecialchars($n['title']); ?></strong>
                <div style="font-size: 12px; color: var(--text-muted); margin-top: 3px; max-width: 320px; line-height: 1.4;">
                  <?php echo htmlspecialchars(mb_substr($n['body'], 0, 100)); ?>...
                </div>
              </td>
              <td>
                <span class="badge badge-primary" style="font-size: 11px; font-weight: 700;">
                  <?php echo htmlspecialchars($n['notification_type']); ?>
                </span>
                <div style="margin-top: 4px;">
                  <?php
                  $pClass = 'badge-secondary';
                  if ($n['priority'] === 'CRITICAL') $pClass = 'badge-danger';
                  if ($n['priority'] === 'HIGH') $pClass = 'badge-warning';
                  ?>
                  <span class="badge <?php echo $pClass; ?>" style="font-size: 10px;"><?php echo $n['priority']; ?></span>
                </div>
              </td>
              <td>
                <code style="background: rgba(16, 185, 129, 0.1); color: var(--primary-light); padding: 3px 6px; border-radius: 4px; font-size: 12px;">
                  <?php echo htmlspecialchars($n['target_action']); ?>
                </code>
              </td>
              <td style="font-size: 12px;">
                <span class="badge badge-secondary"><?php echo htmlspecialchars($n['target_audience']); ?></span>
              </td>
              <td>
                <span class="badge badge-success">✓ <?php echo $n['fcm_status']; ?></span>
                <div style="font-size: 11px; color: var(--text-dim); margin-top: 2px;">
                  <?php echo __('প্রেরক:', 'Sender:'); ?> <?php echo htmlspecialchars($n['created_by']); ?>
                </div>
              </td>
              <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                <?php echo isBn() ? toLangNum(date('d M Y, h:i A', strtotime($n['created_at']))) : date('d M Y, h:i A', strtotime($n['created_at'])); ?>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <a href="notifications.php?resend=<?php echo $n['id']; ?>" class="btn btn-secondary btn-sm" onclick="return confirm('<?php echo __('এই নোটিফিকেশনটি পুনরায় সমস্ত ইউজারকে পাঠাতে চান?', 'Do you want to rebroadcast this notification to all users?'); ?>');" title="<?php echo __('আবার পাঠান', 'Resend'); ?>">
                  🔄 <?php echo __('পুনরায় পাঠান', 'Resend'); ?>
                </a>
                <a href="notifications.php?delete_notif=<?php echo $n['id']; ?>" class="btn btn-danger btn-sm" onclick="return confirm('<?php echo __('এই নোটিফিকেশন হিস্ট্রি মুছে ফেলতে চান?', 'Do you want to delete this notification record?'); ?>');" title="<?php echo __('মুছে ফেলুন', 'Delete'); ?>">
                  <?php echo __('মুছুন', 'Delete'); ?>
                </a>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<script>
const isEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

// Dynamic Preview Listener
const titleInput = document.getElementById('notifTitle');
const bodyInput = document.getElementById('notifBody');
const actionSelect = document.getElementById('targetAction');
const prevTitle = document.getElementById('previewTitle');
const prevBody = document.getElementById('previewBody');
const prevBadge = document.getElementById('previewBadge');
const urlGroup = document.getElementById('urlGroup');

if (titleInput) {
  titleInput.addEventListener('input', () => {
    prevTitle.textContent = titleInput.value.trim() || (isEnglish ? 'Notification Title' : 'নোটিফিকেশনের শিরোনাম');
  });
}
if (bodyInput) {
  bodyInput.addEventListener('input', () => {
    prevBody.textContent = bodyInput.value.trim() || (isEnglish ? 'Notification message body will appear here...' : 'বার্তা অথবা কনটেন্ট এখানে প্রদর্শিত হবে...');
  });
}
if (actionSelect) {
  actionSelect.addEventListener('change', () => {
    const selectedText = actionSelect.options[actionSelect.selectedIndex].text;
    prevBadge.textContent = selectedText;
    if (actionSelect.value === 'external_url') {
      urlGroup.style.display = 'block';
    } else {
      urlGroup.style.display = 'none';
    }
  });
}

// Form Double Submit Guard
let isPushSubmitting = false;
function handlePushSubmit(form) {
  if (isPushSubmitting) return false;
  const btn = form.querySelector('button[type="submit"]');
  if (btn) {
    btn.disabled = true;
    btn.innerHTML = '⏳ ' + (isEnglish ? 'Sending...' : 'পাঠানো হচ্ছে...');
  }
  isPushSubmitting = true;
  return true;
}

// Preset Fill Function from Dropdown
function applyPresetFromDropdown(type) {
  if (!type) return;
  if (type === 'update') {
    document.getElementById('notifTitle').value = isEnglish ? 'New App Update Available! 🚀' : 'নতুন আপডেট ভার্সন এসেছে! 🚀';
    document.getElementById('notifBody').value = isEnglish ? 'A brand new version of DeenOne is available. Update now from Google Play Store.' : 'দীন ওয়ান অ্যাপের নতুন ভার্সন এসেছে। গুগল প্লে স্টোর থেকে এখনই আপডেট করে নিন।';
    document.getElementById('notifType').value = 'APP_UPDATE';
    document.getElementById('targetAction').value = 'app_update';
    document.getElementById('notifPriority').value = 'CRITICAL';
  } else if (type === 'battle') {
    document.getElementById('notifTitle').value = isEnglish ? '⚔️ Live Knowledge Battle Started!' : '⚔️ লাইভ নলেজ ব্যাটেল শুরু!';
    document.getElementById('notifBody').value = isEnglish ? 'Participate in exciting Islamic quiz battles and secure your place on the leaderboard.' : 'ইসলামিক কুইজ প্রতিযোগিতায় অংশ নিয়ে লিডারবোর্ডে নিজের অবস্থান গড়ুন।';
    document.getElementById('notifType').value = 'QUIZ_BATTLE';
    document.getElementById('targetAction').value = 'action_knowledge_battle';
    document.getElementById('notifPriority').value = 'HIGH';
  } else if (type === 'friday') {
    document.getElementById('notifTitle').value = isEnglish ? 'Jummah Mubarak! 🕌' : 'পবিত্র জুম’আ মুবারক! 🕌';
    document.getElementById('notifBody').value = isEnglish ? 'Recite Surah Al-Kahf and send abundant blessings upon the Prophet ﷺ on this blessed Friday.' : 'পবিত্র জুম’আর দিনে সূরা কাহাফ তিলাওয়াত করুন এবং বেশি বেশি দরূদ পাঠ করুন।';
    document.getElementById('notifType').value = 'ANNOUNCEMENT';
    document.getElementById('targetAction').value = 'action_quran';
    document.getElementById('notifPriority').value = 'HIGH';
  } else if (type === 'blood') {
    document.getElementById('notifTitle').value = isEnglish ? '🚨 Emergency Blood Request' : '🚨 জরুরি রক্তের আবেদন';
    document.getElementById('notifBody').value = isEnglish ? 'Blood is urgently required for a critical patient. Step forward to help save a life.' : 'জরুরি ভিত্তিতে রক্ত প্রয়োজন। রক্তদানে এগিয়ে এসে জীবন রক্ষায় সহায়তা করুন।';
    document.getElementById('notifType').value = 'BLOOD_EMERGENCY';
    document.getElementById('targetAction').value = 'action_blood';
    document.getElementById('notifPriority').value = 'CRITICAL';
  } else if (type === 'quran') {
    document.getElementById('notifTitle').value = isEnglish ? 'Daily Quran Recitation 📖' : 'দৈনিক কুরআন তিলাওয়াত 📖';
    document.getElementById('notifBody').value = isEnglish ? 'Recite a few verses with meaning daily and enlighten your heart.' : 'প্রতিদিন অন্তত কয়েকটি আয়াত অর্থসহ তিলাওয়াত করে অন্তরকে আলোকিত রাখুন।';
    document.getElementById('notifType').value = 'NEW_FEATURE';
    document.getElementById('targetAction').value = 'action_quran';
    document.getElementById('notifPriority').value = 'NORMAL';
  } else if (type === 'hadith') {
    document.getElementById('notifTitle').value = isEnglish ? '📜 Sahih Hadith Reminder' : '📜 সহীহ হাদিস রিমাইন্ডার';
    document.getElementById('notifBody').value = isEnglish ? 'The Prophet ﷺ said: Whoever guides someone to goodness will have a reward like the one who did it.' : 'রাসূলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি সৎকাজের পথ দেখায়, সে কাজ সম্পাদনকারীর সমান সওয়াব পাবে।';
    document.getElementById('notifType').value = 'ANNOUNCEMENT';
    document.getElementById('targetAction').value = 'action_hadith';
    document.getElementById('notifPriority').value = 'NORMAL';
  }

  // Trigger preview update
  if (prevTitle) prevTitle.textContent = document.getElementById('notifTitle').value;
  if (prevBody) prevBody.textContent = document.getElementById('notifBody').value;
  if (prevBadge && actionSelect) prevBadge.textContent = actionSelect.options[actionSelect.selectedIndex].text;
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
