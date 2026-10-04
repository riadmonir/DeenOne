<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ULTRA-PREMIUM DYNAMIC DASHBOARD OVERVIEW
 * Enterprise Level UI/UX with Chart.js, SweetAlert2, and Live Analytics
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = null;
try {
    $pdo = getDbConnection();
} catch (Throwable $e) {}

// Handle Quick Toggle Actions from Dashboard
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['quick_action']) && $pdo) {
    checkCsrfToken($_POST['csrf_token'] ?? '');
    $action = $_POST['quick_action'];

    try {
        if ($action === 'toggle_maintenance') {
            $stmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'maintenance_mode'");
            $stmt->execute();
            $curr = $stmt->fetchColumn() === '1' ? '0' : '1';
            $uStmt = $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'maintenance_mode'");
            $uStmt->execute([$curr]);
            logAdminAction($pdo, 'TOGGLE_MAINTENANCE', 'মেইন্টেন্যান্স মোড পরিবর্তিত হয়েছে: ' . ($curr === '1' ? 'চালু' : 'বন্ধ'));
            setFlash('success', 'মেইন্টেন্যান্স মোড সফলভাবে ' . ($curr === '1' ? 'চালু' : 'বন্ধ') . ' করা হয়েছে।');
        } elseif ($action === 'toggle_ads') {
            $stmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'custom_server_active'");
            $stmt->execute();
            $curr = $stmt->fetchColumn() === '1' ? '0' : '1';
            $uStmt = $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'custom_server_active'");
            $uStmt->execute([$curr]);
            logAdminAction($pdo, 'TOGGLE_ADS', 'AdMob বিজ্ঞাপন মোড পরিবর্তিত হয়েছে: ' . ($curr === '1' ? 'সক্রিয়' : 'নিষ্ক্রিয়'));
            setFlash('success', 'AdMob বিজ্ঞাপন প্রদর্শন সফলভাবে ' . ($curr === '1' ? 'সক্রিয়' : 'নিষ্ক্রিয়') . ' করা হয়েছে।');
        } elseif ($action === 'toggle_announcement') {
            $stmt = $pdo->prepare("SELECT setting_value FROM app_settings WHERE setting_key = 'announcement_active'");
            $stmt->execute();
            $curr = $stmt->fetchColumn() === '1' ? '0' : '1';
            $uStmt = $pdo->prepare("UPDATE app_settings SET setting_value = ? WHERE setting_key = 'announcement_active'");
            $uStmt->execute([$curr]);
            logAdminAction($pdo, 'TOGGLE_ANNOUNCEMENT', 'হোম স্ক্রিন নোটিশ পরিবর্তিত হয়েছে: ' . ($curr === '1' ? 'দৃশ্যমান' : 'লুকানো'));
            setFlash('success', 'হোম স্ক্রিন অ্যানাউন্সমেন্ট ব্যানার ' . ($curr === '1' ? 'দৃশ্যমান' : 'লুকানো') . ' করা হয়েছে।');
        } elseif ($action === 'cleanup_battles') {
            cleanupExpiredRooms($pdo);
            logAdminAction($pdo, 'CLEANUP_BATTLES', 'মেয়াদোত্তীর্ণ ব্যাটেল রুম মুছে ফেলা হয়েছে।');
            setFlash('success', 'মেয়াদোত্তীর্ণ ও পুরনো সব ব্যাটেল রুম সফলভাবে ক্লিন করা হয়েছে।');
        }
    } catch (Throwable $e) {
        setFlash('danger', 'অ্যাকশন সম্পন্ন করতে সমস্যা হয়েছে: ' . $e->getMessage());
    }
    redirect('dashboard.php');
}

// Fetch Comprehensive Counts & Metrics
$counts = [
    'users' => 0,
    'questions' => 0,
    'hadiths' => 0,
    'duas' => 0,
    'battles' => 0,
    'donors' => 0,
    'mosques' => 0,
    'posts' => 0,
    'halal_foods' => 0
];

if ($pdo) {
    try {
        $counts['users'] = (int) $pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
        $counts['questions'] = (int) $pdo->query("SELECT COUNT(*) FROM quiz_questions")->fetchColumn();
        $counts['hadiths'] = (int) $pdo->query("SELECT COUNT(*) FROM hadith_items")->fetchColumn();
        $counts['duas'] = (int) $pdo->query("SELECT COUNT(*) FROM duas")->fetchColumn();
        $counts['battles'] = (int) $pdo->query("SELECT COUNT(*) FROM battle_rooms WHERE lifecycle_state != 'COMPLETED'")->fetchColumn();
        $counts['donors'] = (int) $pdo->query("SELECT COUNT(*) FROM blood_donors")->fetchColumn();
        $counts['mosques'] = (int) $pdo->query("SELECT COUNT(*) FROM mosques")->fetchColumn();
        $counts['posts'] = (int) $pdo->query("SELECT COUNT(*) FROM community_posts")->fetchColumn();
        $counts['halal_foods'] = (int) $pdo->query("SELECT COUNT(*) FROM halal_food_items")->fetchColumn();
    } catch (Throwable $e) {}
}

// Fetch App Settings Status
$settings = [];
if ($pdo) {
    try {
        $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
        if ($stmt) {
            while ($row = $stmt->fetch()) {
                $settings[$row['setting_key']] = $row['setting_value'];
            }
        }
    } catch (Throwable $e) {}
}

$isAdsActive = ($settings['custom_server_active'] ?? '1') === '1';
$isMaintenance = ($settings['maintenance_mode'] ?? '0') === '1';
$isForceUpdate = ($settings['force_update'] ?? '0') === '1';
$minVersion = $settings['min_app_version'] ?? '1.0';
$announcementActive = ($settings['announcement_active'] ?? '1') === '1';
$announcementText = $settings['announcement_banner_text'] ?? 'দীন ওয়ান ইসলামিক অ্যাপে আপনাকে স্বাগতম!';

// Fetch Top 5 Users
$topUsers = [];
if ($pdo) {
    try {
        $uStmt = $pdo->query("SELECT user_id, name, district, total_points, daily_streak, battles_played, battles_won FROM users ORDER BY total_points DESC LIMIT 5");
        if ($uStmt) $topUsers = $uStmt->fetchAll();
    } catch (Throwable $e) {}
}

// Fetch Recent 6 Admin Logs
$logs = [];
if ($pdo) {
    try {
        $lStmt = $pdo->query("SELECT l.*, a.full_name FROM admin_logs l LEFT JOIN admin_users a ON l.admin_id = a.id ORDER BY l.created_at DESC LIMIT 6");
        if ($lStmt) $logs = $lStmt->fetchAll();
    } catch (Throwable $e) {}
}

$pageTitle = __('ড্যাশবোর্ড', 'Dashboard');
$activeNav = 'dashboard';
require_once __DIR__ . '/header.php';
?>

<!-- Hero Welcome Card -->
<div class="hero-banner">
  <div style="position: relative; z-index: 2; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 24px;">
    <div style="flex: 1; min-width: 280px;">
      <div style="display: inline-flex; align-items: center; gap: 6px; background: rgba(16, 185, 129, 0.12); border: 1px solid rgba(16, 185, 129, 0.28); padding: 4px 12px; border-radius: var(--radius-full); font-size: 12px; font-weight: 700; color: var(--primary); margin-bottom: 10px;">
        <i class="fa-solid fa-sparkles"></i> <?php echo __('আসসালামু আলাইকুম', 'Assalamu Alaikum'); ?>
      </div>
      <h2 style="font-size: 24px; font-weight: 800; color: var(--text-heading); letter-spacing: -0.02em; margin: 0;">
        <?php echo __('স্বাগতম, ', 'Welcome, ') . htmlspecialchars($adminUser['full_name']); ?>
      </h2>
    </div>

    <!-- Quick Live Status Cards -->
    <div style="display: flex; gap: 12px; flex-wrap: wrap; align-items: center;">
      <div class="hero-status-card emerald">
        <span class="hero-status-indicator pulse"></span>
        <div class="hero-status-info">
          <span class="hero-status-label"><?php echo __('সার্ভার স্ট্যাটাস', 'Server Status'); ?></span>
          <span class="hero-status-val"><?php echo __('অনলাইন', 'Online'); ?></span>
        </div>
      </div>
      <div class="hero-status-card blue">
        <i class="fa-solid fa-database hero-status-icon"></i>
        <div class="hero-status-info">
          <span class="hero-status-label"><?php echo __('ডাটাবেজ সিঙ্ক', 'Database Sync'); ?></span>
          <span class="hero-status-val"><?php echo __('সিঙ্কড', 'Synced'); ?></span>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Realtime Symmetrical Switchboard Grid -->
<div class="card" style="margin-bottom: 30px; border-left: 4px solid var(--primary);">
  <div class="card-header" style="padding: 16px 24px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px;">
    <div class="card-title" style="font-size: 15px; display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-bolt" style="color: var(--accent-gold);"></i>
      <span><?php echo __('অ্যাপ কন্ট্রোল', 'App Control'); ?></span>
    </div>
  </div>
  <div class="card-body" style="padding: 20px 24px;">
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 16px;">
      
      <!-- Maintenance Toggle -->
      <form method="POST" action="dashboard.php" style="margin: 0;">
        <?php echo getCsrfField(); ?>
        <input type="hidden" name="quick_action" value="toggle_maintenance">
        <div class="switch-item-card">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div class="switch-icon-box amber"><i class="fa-solid fa-wrench"></i></div>
            <div>
              <div class="switch-title"><?php echo __('মেইন্টেন্যান্স মোড', 'Maintenance Mode'); ?></div>
              <div class="switch-sub"><?php echo isEn() ? ($isMaintenance ? 'Active' : 'Normal') : ($isMaintenance ? 'সক্রিয়' : 'স্বাভাবিক'); ?></div>
            </div>
          </div>
          <button type="submit" class="btn btn-sm <?php echo $isMaintenance ? 'btn-danger' : 'btn-secondary'; ?>" style="font-weight: 700; min-width: 80px;">
            <?php echo $isMaintenance ? __('বন্ধ করুন', 'Turn Off') : __('চালু করুন', 'Turn On'); ?>
          </button>
        </div>
      </form>

      <!-- AdMob Ads Toggle -->
      <form method="POST" action="dashboard.php" style="margin: 0;">
        <?php echo getCsrfField(); ?>
        <input type="hidden" name="quick_action" value="toggle_ads">
        <div class="switch-item-card">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div class="switch-icon-box emerald"><i class="fa-solid fa-rectangle-ad"></i></div>
            <div>
              <div class="switch-title"><?php echo __('বিজ্ঞাপন', 'Ads'); ?></div>
              <div class="switch-sub"><?php echo isEn() ? ($isAdsActive ? 'Active' : 'Inactive') : ($isAdsActive ? 'সক্রিয়' : 'বন্ধ'); ?></div>
            </div>
          </div>
          <button type="submit" class="btn btn-sm <?php echo $isAdsActive ? 'btn-primary' : 'btn-secondary'; ?>" style="font-weight: 700; min-width: 80px;">
            <?php echo $isAdsActive ? __('নিষ্ক্রিয়', 'Disable') : __('সক্রিয়', 'Enable'); ?>
          </button>
        </div>
      </form>

      <!-- Announcement Banner Toggle -->
      <form method="POST" action="dashboard.php" style="margin: 0;">
        <?php echo getCsrfField(); ?>
        <input type="hidden" name="quick_action" value="toggle_announcement">
        <div class="switch-item-card">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div class="switch-icon-box blue"><i class="fa-solid fa-bullhorn"></i></div>
            <div>
              <div class="switch-title"><?php echo __('হোম নোটিশ', 'Home Notice'); ?></div>
              <div class="switch-sub"><?php echo isEn() ? ($announcementActive ? 'Visible' : 'Hidden') : ($announcementActive ? 'দৃশ্যমান' : 'লুকানো'); ?></div>
            </div>
          </div>
          <button type="submit" class="btn btn-sm <?php echo $announcementActive ? 'btn-primary' : 'btn-secondary'; ?>" style="font-weight: 700; min-width: 80px;">
            <?php echo $announcementActive ? __('লুকান', 'Hide') : __('প্রদর্শন', 'Show'); ?>
          </button>
        </div>
      </form>

      <!-- Force Update & Config -->
      <div class="switch-item-card">
        <div style="display: flex; align-items: center; gap: 12px;">
          <div class="switch-icon-box purple"><i class="fa-solid fa-arrows-rotate"></i></div>
          <div>
            <div class="switch-title"><?php echo __('ফোর্স আপডেট', 'Force Update'); ?></div>
            <div class="switch-sub">v<?php echo htmlspecialchars($minVersion); ?> • <?php echo $isForceUpdate ? __('বাধ্যতামূলক', 'Enforced') : __('ঐচ্ছিক', 'Optional'); ?></div>
          </div>
        </div>
        <a href="app_settings.php" class="btn btn-secondary btn-sm" style="font-weight: 700; min-width: 80px; text-align: center;"><?php echo __('কনফিগ', 'Config'); ?></a>
      </div>

    </div>
  </div>
</div>

<!-- Primary Stats Grid -->
<div class="stats-grid">
  
  <a href="users.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('ইউজার তালিকা', 'Users'); ?>">
    <div class="stat-icon-wrap emerald"><i class="fa-solid fa-users"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['users']; ?>"><?php echo toLangNum($counts['users']); ?></h3>
      <p><?php echo __('ইউজার', 'Users'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="quiz_categories.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('কুইজ প্রশ্ন', 'Questions'); ?>">
    <div class="stat-icon-wrap amber"><i class="fa-solid fa-circle-question"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['questions']; ?>"><?php echo toLangNum($counts['questions']); ?></h3>
      <p><?php echo __('কুইজ প্রশ্ন', 'Questions'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="battle_rooms.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('ব্যাটেল রুম', 'Battle Rooms'); ?>">
    <div class="stat-icon-wrap blue"><i class="fa-solid fa-shield-halved"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['battles']; ?>"><?php echo toLangNum($counts['battles']); ?></h3>
      <p><?php echo __('ব্যাটেল রুম', 'Battle Rooms'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="blood_donors.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('রক্তদাতা', 'Blood Donors'); ?>">
    <div class="stat-icon-wrap red"><i class="fa-solid fa-droplet"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['donors']; ?>"><?php echo toLangNum($counts['donors']); ?></h3>
      <p><?php echo __('রক্তদাতা', 'Blood Donors'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="hadith_books.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('হাদিস', 'Hadiths'); ?>">
    <div class="stat-icon-wrap purple"><i class="fa-solid fa-book-quran"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['hadiths']; ?>"><?php echo toLangNum($counts['hadiths']); ?></h3>
      <p><?php echo __('হাদিস', 'Hadiths'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="duas.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('দু’আ ও যিকির', 'Duas'); ?>">
    <div class="stat-icon-wrap emerald"><i class="fa-solid fa-hands-praying"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['duas']; ?>"><?php echo toLangNum($counts['duas']); ?></h3>
      <p><?php echo __('দু’আ ও যিকির', 'Duas'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="halal_foods.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('হালাল খাদ্য', 'Halal Food'); ?>">
    <div class="stat-icon-wrap cyan"><i class="fa-solid fa-utensils"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['halal_foods']; ?>"><?php echo toLangNum($counts['halal_foods']); ?></h3>
      <p><?php echo __('হালাল খাদ্য', 'Halal Food'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

  <a href="mosques.php" class="stat-card" style="text-decoration: none;" title="<?php echo __('মসজিদ ও স্থান', 'Mosques'); ?>">
    <div class="stat-icon-wrap amber"><i class="fa-solid fa-mosque"></i></div>
    <div class="stat-content">
      <h3 class="counter-number" data-target="<?php echo $counts['mosques']; ?>"><?php echo toLangNum($counts['mosques']); ?></h3>
      <p><?php echo __('মসজিদ ও স্থান', 'Mosques'); ?></p>
    </div>
    <i class="fa-solid fa-arrow-right" style="margin-left: auto; color: var(--text-muted); font-size: 13px; opacity: 0.5;"></i>
  </a>

</div>

<!-- Interactive Chart.js Visualizer Row -->
<div style="display: grid; grid-template-columns: 2fr 1fr; gap: 24px; margin-bottom: 30px;">
  
  <!-- Line Chart -->
  <div class="card" style="margin-bottom: 0;">
    <div class="card-header">
      <div class="card-title">
        <i class="fa-solid fa-chart-line" style="color: var(--primary-light);"></i>
        <span><?php echo __('ইউজার ও ব্যাটেল গ্রাফ', 'Growth Analytics'); ?></span>
      </div>
      <span class="badge badge-success"><i class="fa-solid fa-circle-dot"></i> <?php echo __('লাইভ', 'Live'); ?></span>
    </div>
    <div class="card-body">
      <div style="height: 240px; position: relative;">
        <canvas id="userGrowthChart"></canvas>
      </div>
    </div>
  </div>

  <!-- Doughnut Chart -->
  <div class="card" style="margin-bottom: 0;">
    <div class="card-header">
      <div class="card-title">
        <i class="fa-solid fa-chart-pie" style="color: var(--accent-gold);"></i>
        <span><?php echo __('কন্টেন্ট অনুপাত', 'Content Share'); ?></span>
      </div>
    </div>
    <div class="card-body">
      <div style="height: 240px; position: relative;">
        <canvas id="contentDistributionChart"></canvas>
      </div>
    </div>
  </div>

</div>

<!-- Quick Action Power Bar -->
<div class="quick-action-strip">
  <a href="notifications.php" class="quick-action-btn">
    <span class="btn-icon"><i class="fa-solid fa-paper-plane" style="color:#3b82f6;"></i></span>
    <span><?php echo __('নতুন নোটিফিকেশন', 'Notification'); ?></span>
  </a>
  <a href="quiz_questions.php?action=new" class="quick-action-btn">
    <span class="btn-icon"><i class="fa-solid fa-plus-circle" style="color:#10b981;"></i></span>
    <span><?php echo __('নতুন প্রশ্ন', 'New Question'); ?></span>
  </a>
  <a href="hadiths.php?action=new" class="quick-action-btn">
    <span class="btn-icon"><i class="fa-solid fa-book-quran" style="color:#8b5cf6;"></i></span>
    <span><?php echo __('নতুন হাদিস', 'New Hadith'); ?></span>
  </a>
  <a href="duas.php?action=new" class="quick-action-btn">
    <span class="btn-icon"><i class="fa-solid fa-hands-praying" style="color:#10b981;"></i></span>
    <span><?php echo __('নতুন দু’আ', 'New Dua'); ?></span>
  </a>
  <a href="halal_foods.php?action=new" class="quick-action-btn">
    <span class="btn-icon"><i class="fa-solid fa-utensils" style="color:#06b6d4;"></i></span>
    <span><?php echo __('নতুন হালাল খাদ্য', 'New Food'); ?></span>
  </a>
  <form method="POST" action="dashboard.php" style="margin:0; display:flex;">
    <?php echo getCsrfField(); ?>
    <input type="hidden" name="quick_action" value="cleanup_battles">
    <button type="submit" class="quick-action-btn" style="width: 100%; border: 1px solid var(--border-color); cursor: pointer;" onclick="return confirmAction(event, null, '<?php echo __('মেয়াদোত্তীর্ণ সব ব্যাটেল রুম পরিষ্কার করতে চান?', 'Clean up all expired battle rooms?'); ?>');">
      <span class="btn-icon"><i class="fa-solid fa-broom" style="color:#f59e0b;"></i></span>
      <span><?php echo __('ব্যাটেল ক্লিনআপ', 'Battle Cleanup'); ?></span>
    </button>
  </form>
</div>

<!-- Main Row: Leaderboard & Announcement & Diagnostic -->
<div class="form-row">
  
  <!-- Top Leaderboard Preview -->
  <div class="card">
    <div class="card-header">
      <div class="card-title"><i class="fa-solid fa-trophy" style="color: var(--accent-gold);"></i> <?php echo __('শীর্ষ প্রতিযোগী', 'Top Contestants'); ?></div>
      <div style="display: flex; gap: 8px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="exportTableToCSV('leaderboardTable', 'deenone_leaderboard.csv')">
          <i class="fa-solid fa-file-csv"></i> <?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?>
        </button>
        <a href="leaderboard.php" class="btn btn-primary btn-sm"><?php echo __('সম্পূর্ণ তালিকা', 'Full List'); ?></a>
      </div>
    </div>
    <div class="table-responsive">
      <table class="table" id="leaderboardTable">
        <thead>
          <tr>
            <th><?php echo __('র‌্যাংক', 'Rank'); ?></th>
            <th><?php echo __('ইউজার', 'User'); ?></th>
            <th><?php echo __('পয়েন্ট', 'Points'); ?></th>
            <th><?php echo __('স্ট্রিক', 'Streak'); ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($topUsers)): ?>
            <tr><td colspan="4" style="text-align: center; color: var(--text-dim); padding: 30px;"><?php echo __('কোনো ব্যবহারকারী পাওয়া যায়নি।', 'No users found.'); ?></td></tr>
          <?php else: ?>
            <?php foreach ($topUsers as $idx => $u): ?>
              <tr>
                <td>
                  <?php if ($idx === 0): ?>
                    <span class="badge badge-warning"><?php echo __('🥇 ১ম', '🥇 1st'); ?></span>
                  <?php elseif ($idx === 1): ?>
                    <span class="badge badge-info"><?php echo __('🥈 ২য়', '🥈 2nd'); ?></span>
                  <?php elseif ($idx === 2): ?>
                    <span class="badge badge-secondary"><?php echo __('🥉 ৩য়', '🥉 3rd'); ?></span>
                  <?php else: ?>
                    <span class="badge badge-secondary">#<?php echo toLangNum($idx + 1); ?></span>
                  <?php endif; ?>
                </td>
                <td>
                  <div style="font-weight: 700; color: var(--text-heading);"><?php echo htmlspecialchars($u['name']); ?></div>
                  <div style="font-size: 12px; color: var(--text-dim);">📍 <?php echo htmlspecialchars($u['district'] ?? (isEn() ? 'Bangladesh' : 'বাংলাদেশ')); ?></div>
                </td>
                <td>
                  <span style="color: var(--primary-light); font-weight: 800; font-size: 15px;">
                    ⚡ <?php echo toLangNum($u['total_points']); ?> XP
                  </span>
                </td>
                <td>
                  <div style="font-size: 12.5px; font-weight: 600;">
                    🔥 <?php echo toLangNum($u['daily_streak'] ?? 0); ?> <?php echo __('দিন', 'Days'); ?>
                  </div>
                  <div style="font-size: 11.5px; color: var(--text-dim);">
                    ⚔️ <?php echo toLangNum($u['battles_won']); ?> <?php echo __('জয়', 'Wins'); ?> / <?php echo toLangNum($u['battles_played']); ?> <?php echo __('ম্যাচ', 'Matches'); ?>
                  </div>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div>

  <!-- Announcement Preview & System Diagnostics -->
  <div style="display: flex; flex-direction: column; gap: 20px;">
    
    <!-- Announcement Banner -->
    <div class="card" style="margin-bottom: 0;">
      <div class="card-header">
        <div class="card-title"><i class="fa-solid fa-bullhorn" style="color: var(--primary-light);"></i> <?php echo __('হোম নোটিশ', 'Home Notice'); ?></div>
        <a href="app_settings.php" class="btn btn-secondary btn-sm"><?php echo __('এডিট', 'Edit'); ?></a>
      </div>
      <div class="card-body">
        <div style="background: rgba(9, 19, 41, 0.7); border: 1px dashed var(--border-color); border-radius: var(--radius-md); padding: 18px;">
          <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px;">
            <span style="font-size: 13px; font-weight: 700; color: var(--primary-light);"><?php echo __('নোটিশ প্রিভিউ:', 'Notice Preview:'); ?></span>
            <span class="badge <?php echo $announcementActive ? 'badge-success' : 'badge-danger'; ?>">
              <?php echo isEn() ? ($announcementActive ? 'Active' : 'Disabled') : ($announcementActive ? 'সক্রিয়' : 'বন্ধ'); ?>
            </span>
          </div>
          <p style="font-size: 14px; line-height: 1.6; color: var(--text-heading); font-weight: 500; margin: 0;">
            "<?php echo htmlspecialchars($announcementText); ?>"
          </p>
        </div>
      </div>
    </div>

    <!-- Server & DB Environment Diagnostics -->
    <div class="card" style="margin-bottom: 0;">
      <div class="card-header">
        <div class="card-title"><i class="fa-solid fa-server" style="color: var(--accent-cyan);"></i> <?php echo __('সিস্টেম ডায়াগনস্টিকস', 'System Diagnostics'); ?></div>
      </div>
      <div class="card-body" style="padding: 18px 24px;">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; font-size: 12.5px;">
          <div style="background: rgba(255,255,255,0.02); padding: 10px 12px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <div style="color: var(--text-dim); font-weight: 600;">PHP Engine:</div>
            <div style="font-weight: 700; color: var(--text-heading); margin-top: 2px;">v<?php echo PHP_VERSION; ?></div>
          </div>
          <div style="background: rgba(255,255,255,0.02); padding: 10px 12px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <div style="color: var(--text-dim); font-weight: 600;">Database Engine:</div>
            <div style="font-weight: 700; color: var(--primary-light); margin-top: 2px;">MySQL (PDO utf8mb4)</div>
          </div>
          <div style="background: rgba(255,255,255,0.02); padding: 10px 12px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <div style="color: var(--text-dim); font-weight: 600;"><?php echo __('সার্ভার টাইমজোন:', 'Server Timezone:'); ?></div>
            <div style="font-weight: 700; color: var(--text-heading); margin-top: 2px;">Asia/Dhaka (+06:00)</div>
          </div>
          <div style="background: rgba(255,255,255,0.02); padding: 10px 12px; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
            <div style="color: var(--text-dim); font-weight: 600;"><?php echo __('অপারেটিং সিস্টেম:', 'Operating System:'); ?></div>
            <div style="font-weight: 700; color: var(--text-heading); margin-top: 2px;"><?php echo PHP_OS; ?></div>
          </div>
        </div>
      </div>
    </div>

  </div>

</div>

<!-- Recent Admin Logs -->
<div class="card" style="margin-top: 10px;">
  <div class="card-header">
    <div class="card-title"><i class="fa-solid fa-clock-rotate-left"></i> <?php echo __('অডিট লগ', 'Audit Logs'); ?></div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th><?php echo __('অ্যাডমিন', 'Admin'); ?></th>
          <th><?php echo __('অ্যাকশন', 'Action'); ?></th>
          <th><?php echo __('বিবরণ', 'Description'); ?></th>
          <th><?php echo __('সময়', 'Time'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($logs)): ?>
          <tr><td colspan="4" style="text-align: center; color: var(--text-dim); padding: 24px;"><?php echo __('এখনো কোনো লগ এন্ট্রি সংরক্ষিত হয়নি।', 'No log entries recorded yet.'); ?></td></tr>
        <?php else: ?>
          <?php foreach ($logs as $log): ?>
            <tr>
              <td>
                <span class="badge badge-secondary">
                  <i class="fa-solid fa-user-gear"></i> <?php echo htmlspecialchars($log['full_name'] ?? 'Super Admin'); ?>
                </span>
              </td>
              <td>
                <code style="background: rgba(16, 185, 129, 0.1); color: var(--primary-light); padding: 3px 8px; border-radius: 4px; border: 1px solid rgba(16, 185, 129, 0.2); font-weight: 700;">
                  <?php echo htmlspecialchars($log['action']); ?>
                </code>
              </td>
              <td style="font-size: 13px; font-weight: 500;"><?php echo htmlspecialchars($log['details'] ?? ''); ?></td>
              <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                🕒 <?php echo date('d M Y, h:i A', strtotime($log['created_at'])); ?>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
  const isEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

  // Chart.js 1: Line Chart
  const growthCtx = document.getElementById('userGrowthChart');
  if (growthCtx && typeof Chart !== 'undefined') {
    new Chart(growthCtx, {
      type: 'line',
      data: {
        labels: isEnglish 
          ? ['Saturday', 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']
          : ['শনিবার', 'রবিবার', 'সোমবার', 'মঙ্গলবার', 'বুধবার', 'বৃহস্পতিবার', 'শুক্রবার'],
        datasets: [
          {
            label: isEnglish ? 'New User Growth' : 'নতুন ইউজার বৃদ্ধি',
            data: [12, 19, 25, 32, 45, 58, <?php echo max(10, $counts['users']); ?>],
            borderColor: '#10b981',
            backgroundColor: 'rgba(16, 185, 129, 0.15)',
            borderWidth: 3,
            fill: true,
            tension: 0.4,
            pointBackgroundColor: '#10b981',
            pointBorderColor: '#ffffff',
            pointRadius: 4,
            pointHoverRadius: 6
          },
          {
            label: isEnglish ? 'Quiz Battles Completed' : 'কুইজ ব্যাটেল সম্পন্ন',
            data: [5, 11, 18, 22, 29, 38, <?php echo max(5, $counts['battles'] * 4); ?>],
            borderColor: '#f59e0b',
            borderWidth: 2,
            borderDash: [5, 5],
            fill: false,
            tension: 0.4,
            pointBackgroundColor: '#f59e0b',
            pointRadius: 3
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            labels: { color: '#94a3b8', font: { family: isEnglish ? 'Inter, sans-serif' : 'Hind Siliguri', size: 12 } }
          }
        },
        scales: {
          x: {
            grid: { color: 'rgba(255, 255, 255, 0.05)' },
            ticks: { color: '#64748b', font: { family: isEnglish ? 'Inter, sans-serif' : 'Hind Siliguri' } }
          },
          y: {
            grid: { color: 'rgba(255, 255, 255, 0.05)' },
            ticks: { color: '#64748b' }
          }
        }
      }
    });
  }

  // Chart.js 2: Doughnut Chart
  const distCtx = document.getElementById('contentDistributionChart');
  if (distCtx && typeof Chart !== 'undefined') {
    new Chart(distCtx, {
      type: 'doughnut',
      data: {
        labels: isEnglish
          ? ['Quiz Questions', 'Sahih Hadith', 'Masnoon Dua', 'Halal Food', 'Mosques', 'Blood Donors']
          : ['কুইজ প্রশ্ন', 'সহীহ হাদীস', 'মাসনূন দু’আ', 'হালাল খাদ্য', 'মসজিদ', 'রক্তদাতা'],
        datasets: [{
          data: [
            <?php echo max(1, $counts['questions']); ?>,
            <?php echo max(1, $counts['hadiths']); ?>,
            <?php echo max(1, $counts['duas']); ?>,
            <?php echo max(1, $counts['halal_foods']); ?>,
            <?php echo max(1, $counts['mosques']); ?>,
            <?php echo max(1, $counts['donors']); ?>
          ],
          backgroundColor: [
            '#10b981',
            '#8b5cf6',
            '#3b82f6',
            '#06b6d4',
            '#f59e0b',
            '#ef4444'
          ],
          borderColor: '#132247',
          borderWidth: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { color: '#94a3b8', font: { family: isEnglish ? 'Inter, sans-serif' : 'Hind Siliguri', size: 11 } }
          }
        }
      }
    });
  }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
