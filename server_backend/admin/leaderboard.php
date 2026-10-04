<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - LEADERBOARD & GAMIFICATION SUITE (লিডারবোর্ড ও র‍্যাংক)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER (Direct Database Download)
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $searchExp = trim($_GET['search'] ?? '');
    $sortExp = trim($_GET['sort'] ?? 'points');

    $whereExp = [];
    $paramsExp = [];
    if (!empty($searchExp)) {
        $whereExp[] = "(name LIKE ? OR username LIKE ? OR user_id LIKE ? OR district LIKE ?)";
        $term = "%$searchExp%";
        $paramsExp = [$term, $term, $term, $term];
    }
    $wSql = !empty($whereExp) ? "WHERE " . implode(" AND ", $whereExp) : "";

    $orderSql = "ORDER BY total_points DESC, daily_streak DESC";
    if ($sortExp === 'battles_won') {
        $orderSql = "ORDER BY battles_won DESC, total_points DESC";
    } elseif ($sortExp === 'streak') {
        $orderSql = "ORDER BY daily_streak DESC, total_points DESC";
    } elseif ($sortExp === 'battles_played') {
        $orderSql = "ORDER BY battles_played DESC, total_points DESC";
    }

    $stmt = $pdo->prepare("SELECT user_id, name, username, email, phone, blood_group, district, total_points, quiz_points, battles_played, battles_won, daily_streak, last_streak_date, is_banned, last_active, created_at FROM users $wSql $orderSql");
    $stmt->execute($paramsExp);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_leaderboard_' . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, [
        'Rank', 'User_ID', 'Name', 'Username', 'Email', 'Phone',
        'Blood_Group', 'District', 'Total_Points', 'Quiz_Points',
        'Battles_Played', 'Battles_Won', 'Daily_Streak', 'Last_Streak_Date',
        'Is_Banned', 'Last_Active', 'Created_At'
    ]);

    $rank = 1;
    foreach ($rows as $r) {
        fputcsv($out, [
            $rank++,
            $r['user_id'] ?? '',
            $r['name'] ?? '',
            $r['username'] ?? '',
            $r['email'] ?? '',
            $r['phone'] ?? '',
            $r['blood_group'] ?? '',
            $r['district'] ?? '',
            $r['total_points'] ?? 0,
            $r['quiz_points'] ?? 0,
            $r['battles_played'] ?? 0,
            $r['battles_won'] ?? 0,
            $r['daily_streak'] ?? 0,
            $r['last_streak_date'] ?? '',
            $r['is_banned'] ?? 0,
            $r['last_active'] ?? '',
            $r['created_at'] ?? ''
        ]);
    }
    fclose($out);
    exit();
}

// -----------------------------------------------------------------------------
// 2. CSV IMPORT HANDLER (Bulk Update Points & Users in Database)
// -----------------------------------------------------------------------------
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'import_csv') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('leaderboard.php');
    }

    if (!empty($_FILES['csv_file']['tmp_name']) && is_uploaded_file($_FILES['csv_file']['tmp_name'])) {
        $file = fopen($_FILES['csv_file']['tmp_name'], 'r');
        $bom = fread($file, 3);
        if ($bom !== "\xEF\xBB\xBF") {
            rewind($file);
        }

        $header = fgetcsv($file);
        $importedCount = 0;
        $updatedCount = 0;

        while (($row = fgetcsv($file)) !== false) {
            if (empty($row) || count($row) < 3) continue;

            // Handle potential with or without rank column
            $offsetCol = is_numeric($row[0]) && !empty($row[1]) && strlen($row[1]) > 3 ? 1 : 0;

            $uid = trim($row[$offsetCol + 0] ?? '');
            $name = trim($row[$offsetCol + 1] ?? 'DeenOne User');
            $username = trim($row[$offsetCol + 2] ?? '');
            $email = trim($row[$offsetCol + 3] ?? '');
            $phone = trim($row[$offsetCol + 4] ?? '');
            $bloodGroup = trim($row[$offsetCol + 5] ?? '');
            $district = trim($row[$offsetCol + 6] ?? '');
            $totalPoints = max(0, (int)($row[$offsetCol + 7] ?? 0));
            $quizPoints = max(0, (int)($row[$offsetCol + 8] ?? 0));
            $battlesPlayed = max(0, (int)($row[$offsetCol + 9] ?? 0));
            $battlesWon = max(0, (int)($row[$offsetCol + 10] ?? 0));
            $dailyStreak = max(0, (int)($row[$offsetCol + 11] ?? 0));

            if (empty($uid) && empty($username) && empty($email)) continue;

            // Check if user exists
            $checkStmt = $pdo->prepare("SELECT user_id FROM users WHERE user_id = ? OR (username IS NOT NULL AND username != '' AND username = ?) OR (email IS NOT NULL AND email != '' AND email = ?)");
            $checkStmt->execute([$uid, $username, $email]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $uStmt = $pdo->prepare("UPDATE users SET 
                    name = IF(? != '', ?, name),
                    total_points = ?,
                    quiz_points = ?,
                    battles_played = ?,
                    battles_won = ?,
                    daily_streak = ?
                    WHERE user_id = ?");
                $uStmt->execute([
                    $name, $name,
                    $totalPoints, $quizPoints, $battlesPlayed, $battlesWon, $dailyStreak,
                    $existing['user_id']
                ]);
                $updatedCount++;
            } else {
                if (empty($uid)) {
                    $uid = 'usr_' . substr(md5(uniqid(mt_rand(), true)), 0, 12);
                }
                $iStmt = $pdo->prepare("INSERT INTO users 
                    (user_id, name, username, email, phone, blood_group, district, total_points, quiz_points, battles_played, battles_won, daily_streak, created_at) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
                $iStmt->execute([
                    $uid, $name, $username ?: null, $email ?: null, $phone ?: null, $bloodGroup ?: null, $district ?: null,
                    $totalPoints, $quizPoints, $battlesPlayed, $battlesWon, $dailyStreak
                ]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "লিডারবোর্ড CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " জন নতুন ইউজার যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " জনের পয়েন্ট আপডেট হয়েছে।";
        $msgEn = "Leaderboard CSV Import Completed: " . toLangNum($importedCount) . " new users added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_LEADERBOARD', 'users', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('leaderboard.php');
}

// Handle Adjust Points POST Action
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (verifyCsrfToken($csrfToken)) {
        if ($action === 'adjust_points') {
            $uid = trim($_POST['user_id'] ?? '');
            $points = max(0, (int)($_POST['points'] ?? 0));
            $reason = trim($_POST['reason'] ?? 'ম্যানুয়াল পয়েন্ট সমন্বয়');

            if (!empty($uid)) {
                try {
                    $pdo->prepare("UPDATE users SET total_points = ? WHERE user_id = ?")->execute([$points, $uid]);
                    logAdminAction($pdo, 'ADJUST_USER_POINTS', 'users', $uid, "পয়েন্ট পরিবর্তন: $points XP ($reason)");
                    setFlash('success', __('ব্যবহারকারীর পয়েন্ট সফলভাবে আপডেট করা হয়েছে।', 'User points updated successfully.'));
                } catch (Exception $e) {
                    setFlash('danger', __('পয়েন্ট আপডেট ব্যর্থ: ', 'Points update failed: ') . $e->getMessage());
                }
            }
        }
    }
    redirect('leaderboard.php');
}

// -----------------------------------------------------------------------------
// Overall Statistics & Gamification Analytics
// -----------------------------------------------------------------------------
$totalUsers = 0;
$totalPointsDistributed = 0;
$topScorer = null;
$topBattleWinner = null;
$topStreakUser = null;
$activePlayersCount = 0;

try {
    $totalUsers = (int)$pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
    $totalPointsDistributed = (int)$pdo->query("SELECT COALESCE(SUM(total_points), 0) FROM users")->fetchColumn();
    $activePlayersCount = (int)$pdo->query("SELECT COUNT(*) FROM users WHERE total_points > 0 OR battles_played > 0")->fetchColumn();

    // Top Scorer
    $tsStmt = $pdo->query("SELECT user_id, name, username, avatar, district, total_points, daily_streak, battles_won FROM users ORDER BY total_points DESC LIMIT 1");
    $topScorer = $tsStmt->fetch();

    // Top Battle Winner
    $tbStmt = $pdo->query("SELECT user_id, name, username, avatar, district, battles_won, battles_played, total_points FROM users ORDER BY battles_won DESC LIMIT 1");
    $topBattleWinner = $tbStmt->fetch();

    // Top Streak
    $tstStmt = $pdo->query("SELECT user_id, name, username, avatar, district, daily_streak, total_points FROM users ORDER BY daily_streak DESC LIMIT 1");
    $topStreakUser = $tstStmt->fetch();

} catch (Exception $e) {}

// Top 3 Podium Users
$topThree = [];
try {
    $podiumStmt = $pdo->query("SELECT user_id, name, username, avatar, district, total_points, daily_streak, battles_won, battles_played 
                               FROM users 
                               WHERE is_banned = 0 
                               ORDER BY total_points DESC 
                               LIMIT 3");
    $topThree = $podiumStmt->fetchAll();
} catch (Exception $e) {}

// Search & Sort Filtering
$search = trim($_GET['search'] ?? '');
$sortBy = trim($_GET['sort'] ?? 'points');
$page = max(1, (int)($_GET['page'] ?? 1));
$perPage = 25;
$offset = ($page - 1) * $perPage;

$where = [];
$params = [];

if (!empty($search)) {
    $where[] = "(name LIKE ? OR username LIKE ? OR user_id LIKE ? OR district LIKE ?)";
    $term = "%$search%";
    $params = [$term, $term, $term, $term];
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

// Sorting logic
$orderSql = "ORDER BY total_points DESC, daily_streak DESC";
if ($sortBy === 'battles_won') {
    $orderSql = "ORDER BY battles_won DESC, total_points DESC";
} elseif ($sortBy === 'streak') {
    $orderSql = "ORDER BY daily_streak DESC, total_points DESC";
} elseif ($sortBy === 'battles_played') {
    $orderSql = "ORDER BY battles_played DESC, total_points DESC";
}

$totalFiltered = 0;
$leaderboardUsers = [];
try {
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM users $whereSql");
    $countStmt->execute($params);
    $totalFiltered = (int)$countStmt->fetchColumn();

    $lSql = "SELECT user_id, name, username, email, phone, blood_group, district, avatar, total_points, quiz_points, battles_played, battles_won, daily_streak, is_banned, last_active 
             FROM users $whereSql 
             $orderSql 
             LIMIT $perPage OFFSET $offset";
    $lStmt = $pdo->prepare($lSql);
    $lStmt->execute($params);
    $leaderboardUsers = $lStmt->fetchAll();
} catch (Exception $e) {}

$totalPages = max(1, ceil($totalFiltered / $perPage));

$pageTitle = __('লিডারবোর্ড', 'Leaderboard');
$activeNav = 'leaderboard';
require_once __DIR__ . '/header.php';
?>

<!-- ==============================================================================
     TOP GAMIFICATION STATS SUMMARY CARDS
     ============================================================================== -->
<div class="stats-grid" style="margin-bottom: 24px;">
  <!-- Top Scorer -->
  <div class="stat-card">
    <div class="stat-icon-wrap amber">
      <i class="fa-solid fa-trophy"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo !empty($topScorer) ? toLangNum($topScorer['total_points']) . ' XP' : '0 XP'; ?></h3>
      <p><?php echo __('শীর্ষ পয়েন্ট', 'Top Scorer'); ?>: <strong><?php echo htmlspecialchars($topScorer['name'] ?? '—'); ?></strong></p>
    </div>
  </div>

  <!-- Total XP Distributed -->
  <div class="stat-card">
    <div class="stat-icon-wrap emerald">
      <i class="fa-solid fa-gem"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($totalPointsDistributed); ?></h3>
      <p><?php echo __('মোট পয়েন্ট', 'Total XP'); ?></p>
    </div>
  </div>

  <!-- Battle Champion -->
  <div class="stat-card">
    <div class="stat-icon-wrap red">
      <i class="fa-solid fa-shield-halved"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo !empty($topBattleWinner) ? toLangNum($topBattleWinner['battles_won']) . ' ' . __('জয়', 'Wins') : '0'; ?></h3>
      <p><?php echo __('ব্যাটেল চ্যাম্পিয়ন', 'Battle Champion'); ?>: <strong><?php echo htmlspecialchars($topBattleWinner['name'] ?? '—'); ?></strong></p>
    </div>
  </div>

  <!-- Highest Streak -->
  <div class="stat-card">
    <div class="stat-icon-wrap blue">
      <i class="fa-solid fa-fire-flame-curved"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo !empty($topStreakUser) ? toLangNum($topStreakUser['daily_streak']) . ' ' . __('দিন', 'Days') : '0'; ?></h3>
      <p><?php echo __('সর্বোচ্চ স্ট্রিক', 'Highest Streak'); ?>: <strong><?php echo htmlspecialchars($topStreakUser['name'] ?? '—'); ?></strong></p>
    </div>
  </div>
</div>

<!-- ==============================================================================
     TOP 3 CHAMPIONS PODIUM
     ============================================================================== -->
<?php if (count($topThree) >= 3): ?>
<div class="card" style="margin-bottom: 24px; background: linear-gradient(180deg, rgba(16, 185, 129, 0.04) 0%, transparent 100%);">
  <div class="card-header" style="border-bottom: none; padding-bottom: 0;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-crown" style="color: #f59e0b;"></i>
      <span><?php echo __('শীর্ষ ৩', 'Top 3'); ?></span>
    </div>
  </div>
  <div class="card-body" style="padding: 20px 24px 28px;">
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 20px; align-items: end;">
      
      <!-- Rank 2: Silver 🥈 -->
      <?php $u2 = $topThree[1]; ?>
      <div style="background: var(--hover-bg); border: 1.5px solid #94a3b8; border-radius: var(--radius-xl); padding: 22px 18px; text-align: center; position: relative;">
        <div style="position: absolute; top: -14px; left: 50%; transform: translateX(-50%); background: #94a3b8; color: #ffffff; font-weight: 800; font-size: 12px; padding: 3px 12px; border-radius: 20px; box-shadow: 0 4px 10px rgba(148, 163, 184, 0.4);">
          🥈 <?php echo __('২য় স্থান', '2nd Place'); ?>
        </div>
        <div style="width: 56px; height: 56px; border-radius: 50%; background: rgba(148, 163, 184, 0.2); border: 3px solid #94a3b8; margin: 10px auto 10px; display: flex; align-items: center; justify-content: center; font-size: 24px;">
          🥈
        </div>
        <h4 style="margin: 0 0 4px; font-size: 15px; font-weight: 700; color: var(--text-heading);"><?php echo htmlspecialchars($u2['name']); ?></h4>
        <div style="font-size: 12px; color: var(--text-dim); margin-bottom: 10px;">📍 <?php echo htmlspecialchars($u2['district'] ?? __('বাংলাদেশ', 'Bangladesh')); ?></div>
        <div style="background: var(--bg-card); border-radius: var(--radius-md); padding: 8px; font-size: 13px; font-weight: 700; color: var(--text-heading);">
          ⚡ <?php echo toLangNum($u2['total_points']); ?> XP <span style="font-size: 11px; color: #f97316; margin-left: 6px;">🔥 <?php echo toLangNum($u2['daily_streak']); ?>d</span>
        </div>
      </div>

      <!-- Rank 1: Gold 🥇 (Elevated Center) -->
      <?php $u1 = $topThree[0]; ?>
      <div style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.12), rgba(234, 179, 8, 0.05)); border: 2px solid #f59e0b; border-radius: var(--radius-xl); padding: 28px 20px; text-align: center; position: relative; box-shadow: 0 8px 24px rgba(245, 158, 11, 0.2);">
        <div style="position: absolute; top: -16px; left: 50%; transform: translateX(-50%); background: linear-gradient(135deg, #f59e0b, #d97706); color: #ffffff; font-weight: 800; font-size: 13px; padding: 4px 16px; border-radius: 20px; box-shadow: 0 4px 12px rgba(245, 158, 11, 0.5);">
          🥇 <?php echo __('১ম স্থান', '1st Place'); ?>
        </div>
        <div style="width: 68px; height: 68px; border-radius: 50%; background: rgba(245, 158, 11, 0.25); border: 3.5px solid #f59e0b; margin: 10px auto 12px; display: flex; align-items: center; justify-content: center; font-size: 32px;">
          👑
        </div>
        <h3 style="margin: 0 0 4px; font-size: 17px; font-weight: 800; color: var(--text-heading);"><?php echo htmlspecialchars($u1['name']); ?></h3>
        <div style="font-size: 12px; color: var(--text-dim); margin-bottom: 12px;">📍 <?php echo htmlspecialchars($u1['district'] ?? __('বাংলাদেশ', 'Bangladesh')); ?></div>
        <div style="background: var(--bg-card); border: 1px solid rgba(245, 158, 11, 0.3); border-radius: var(--radius-md); padding: 10px; font-size: 15px; font-weight: 800; color: #f59e0b;">
          🏆 <?php echo toLangNum($u1['total_points']); ?> XP 
          <span style="font-size: 12px; color: #f97316; margin-left: 8px;">🔥 <?php echo toLangNum($u1['daily_streak']); ?> <?php echo __('দিন', 'Days'); ?></span>
        </div>
      </div>

      <!-- Rank 3: Bronze 🥉 -->
      <?php $u3 = $topThree[2]; ?>
      <div style="background: var(--hover-bg); border: 1.5px solid #b45309; border-radius: var(--radius-xl); padding: 22px 18px; text-align: center; position: relative;">
        <div style="position: absolute; top: -14px; left: 50%; transform: translateX(-50%); background: #b45309; color: #ffffff; font-weight: 800; font-size: 12px; padding: 3px 12px; border-radius: 20px; box-shadow: 0 4px 10px rgba(180, 83, 9, 0.4);">
          🥉 <?php echo __('৩য় স্থান', '3rd Place'); ?>
        </div>
        <div style="width: 56px; height: 56px; border-radius: 50%; background: rgba(180, 83, 9, 0.2); border: 3px solid #b45309; margin: 10px auto 10px; display: flex; align-items: center; justify-content: center; font-size: 24px;">
          🥉
        </div>
        <h4 style="margin: 0 0 4px; font-size: 15px; font-weight: 700; color: var(--text-heading);"><?php echo htmlspecialchars($u3['name']); ?></h4>
        <div style="font-size: 12px; color: var(--text-dim); margin-bottom: 10px;">📍 <?php echo htmlspecialchars($u3['district'] ?? __('বাংলাদেশ', 'Bangladesh')); ?></div>
        <div style="background: var(--bg-card); border-radius: var(--radius-md); padding: 8px; font-size: 13px; font-weight: 700; color: var(--text-heading);">
          ⚡ <?php echo toLangNum($u3['total_points']); ?> XP <span style="font-size: 11px; color: #f97316; margin-left: 6px;">🔥 <?php echo toLangNum($u3['daily_streak']); ?>d</span>
        </div>
      </div>

    </div>
  </div>
</div>
<?php endif; ?>

<!-- ==============================================================================
     SEARCH & SORT FILTER TOOLBAR
     ============================================================================== -->
<div class="card" style="margin-bottom: 20px;">
  <div class="card-body" style="padding: 16px 20px;">
    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px;">
      
      <!-- Search & Sorting Filters -->
      <form method="GET" action="leaderboard.php" style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap; flex: 1;">
        <div style="flex: 1; min-width: 220px;">
          <input type="text" name="search" class="form-control" style="height: 38px; font-size: 13px;" placeholder="<?php echo __('অনুসন্ধান...', 'Search...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
        </div>

        <select name="sort" class="form-control" style="width: auto; height: 38px; font-size: 13px;">
          <option value="points" <?php echo $sortBy === 'points' ? 'selected' : ''; ?>>⚡ <?php echo __('সর্বাধিক পয়েন্ট', 'Highest Points'); ?></option>
          <option value="battles_won" <?php echo $sortBy === 'battles_won' ? 'selected' : ''; ?>>👑 <?php echo __('ব্যাটেল জয়', 'Battles Won'); ?></option>
          <option value="streak" <?php echo $sortBy === 'streak' ? 'selected' : ''; ?>>🔥 <?php echo __('দৈনিক স্ট্রিক', 'Daily Streak'); ?></option>
          <option value="battles_played" <?php echo $sortBy === 'battles_played' ? 'selected' : ''; ?>>⚔️ <?php echo __('ম্যাচ খেলা', 'Matches Played'); ?></option>
        </select>

        <button type="submit" class="btn btn-secondary btn-sm" style="height: 38px;">
          <i class="fa-solid fa-filter"></i> <?php echo __('ফিল্টার', 'Apply'); ?>
        </button>

        <?php if (!empty($search) || $sortBy !== 'points'): ?>
          <a href="leaderboard.php" class="btn btn-secondary btn-sm" style="height: 38px;"><?php echo __('রিসেট', 'Reset'); ?></a>
        <?php endif; ?>
      </form>

      <!-- CSV Export & Import -->
      <div style="display: flex; gap: 8px; align-items: center;">
        <a href="leaderboard.php?action=export_csv<?php echo !empty($search) ? '&search=' . urlencode($search) : ''; ?><?php echo !empty($sortBy) ? '&sort=' . urlencode($sortBy) : ''; ?>" class="btn btn-secondary btn-sm" style="height: 38px; font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?>">
          <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
          <span><?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?></span>
        </a>

        <button type="button" class="btn btn-secondary btn-sm" onclick="openImportLeaderboardCsvModal()" style="height: 38px; font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?>">
          <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
          <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
        </button>
      </div>

    </div>
  </div>
</div>

<!-- ==============================================================================
     FULL LEADERBOARD RANKINGS TABLE
     ============================================================================== -->
<div class="card">
  <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-ranking-star" style="color: #f59e0b;"></i>
      <span><?php echo __('র‌্যাঙ্কিং তালিকা', 'Rankings'); ?></span>
      <span class="badge badge-warning" style="font-size: 11px;"><?php echo toLangNum($totalFiltered); ?></span>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table" id="leaderboardTable">
      <thead>
        <tr>
          <th style="width: 70px; text-align: center;"><?php echo __('র‌্যাংক', 'Rank'); ?></th>
          <th><?php echo __('ইউজার', 'User'); ?></th>
          <th><?php echo __('পয়েন্ট', 'XP'); ?></th>
          <th><?php echo __('ব্যাটেল', 'Battle'); ?></th>
          <th><?php echo __('স্ট্রিক', 'Streak'); ?></th>
          <th><?php echo __('টিয়ার', 'Tier'); ?></th>
          <th><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th style="text-align: right; width: 100px;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($leaderboardUsers)): ?>
          <tr>
            <td colspan="8" style="text-align: center; color: var(--text-dim); padding: 36px 20px;">
              <div style="font-size: 32px; margin-bottom: 8px;">🏆</div>
              <div><?php echo __('কোনো ব্যবহারকারী পাওয়া যায়নি।', 'No leaderboard records found.'); ?></div>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($leaderboardUsers as $idx => $u): ?>
            <?php 
              $rankNum = $offset + $idx + 1;
              $xp = (int)$u['total_points'];
              
              // Tier Calculation
              if ($xp >= 2000) {
                  $tierName = __('গ্র্যান্ডমাস্টার', 'Grandmaster');
                  $tierClass = 'badge-warning';
                  $tierIcon = '💎';
              } elseif ($xp >= 1000) {
                  $tierName = __('মাস্টার', 'Master');
                  $tierClass = 'badge-success';
                  $tierIcon = '🌟';
              } elseif ($xp >= 500) {
                  $tierName = __('স্কলার', 'Scholar');
                  $tierClass = 'badge-info';
                  $tierIcon = '📜';
              } elseif ($xp >= 200) {
                  $tierName = __('এক্সপ্লোরার', 'Explorer');
                  $tierClass = 'badge-secondary';
                  $tierIcon = '🧭';
              } else {
                  $tierName = __('শিক্ষানবিস', 'Novice');
                  $tierClass = 'badge-secondary';
                  $tierIcon = '🌱';
              }

              $winRate = ($u['battles_played'] > 0) ? round(($u['battles_won'] / $u['battles_played']) * 100) : 0;
            ?>
            <tr>
              <!-- Rank Badge -->
              <td style="text-align: center;">
                <?php if ($rankNum === 1): ?>
                  <span class="badge badge-warning" style="font-weight: 800; font-size: 12px; padding: 4px 8px;">🥇 #১</span>
                <?php elseif ($rankNum === 2): ?>
                  <span class="badge badge-info" style="font-weight: 800; font-size: 12px; padding: 4px 8px;">🥈 #২</span>
                <?php elseif ($rankNum === 3): ?>
                  <span class="badge badge-secondary" style="font-weight: 800; font-size: 12px; padding: 4px 8px; border-color: #b45309; color: #b45309;">🥉 #৩</span>
                <?php else: ?>
                  <span style="font-weight: 700; color: var(--text-dim); font-size: 13px;">#<?php echo toLangNum($rankNum); ?></span>
                <?php endif; ?>
              </td>

              <!-- User & Location -->
              <td>
                <div style="display: flex; align-items: center; gap: 10px;">
                  <div style="width: 36px; height: 36px; border-radius: 50%; background: var(--hover-bg); border: 1.5px solid var(--border-color); display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0; overflow: hidden;">
                    <?php 
                      $avatarNum = str_replace('avatar_', '', $u['avatar'] ?? '1');
                      $avatarEmojis = ['1' => '🧔', '2' => '🧕', '3' => '👳', '4' => '👨', '5' => '🧓', '6' => '👴', '7' => '👦', '8' => '👧', '9' => '🌙', '10' => '⭐', '11' => '📖', '12' => '🕌'];
                      echo $avatarEmojis[$avatarNum] ?? '👤';
                    ?>
                  </div>
                  <div>
                    <div style="font-weight: 700; color: var(--text-heading); font-size: 13.5px;">
                      <?php echo htmlspecialchars($u['name']); ?>
                    </div>
                    <div style="font-size: 11px; color: var(--text-dim); display: flex; align-items: center; gap: 6px;">
                      <span>📍 <?php echo htmlspecialchars($u['district'] ?? __('বাংলাদেশ', 'Bangladesh')); ?></span>
                      <span>•</span>
                      <span style="font-family: monospace;"><?php echo htmlspecialchars($u['username'] ?: $u['user_id']); ?></span>
                    </div>
                  </div>
                </div>
              </td>

              <!-- Total Points (XP) -->
              <td>
                <div style="display: flex; align-items: center; gap: 6px;">
                  <span style="color: var(--primary-light); font-weight: 800; font-size: 14.5px;">
                    ⚡ <?php echo toLangNum($u['total_points']); ?> XP
                  </span>
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 3px 6px; font-size: 11px;" title="<?php echo __('পয়েন্ট সমন্বয় করুন', 'Adjust Points'); ?>" onclick="openAdjustPointsModal('<?php echo htmlspecialchars($u['user_id']); ?>', '<?php echo htmlspecialchars(addslashes($u['name'])); ?>', <?php echo (int)$u['total_points']; ?>)">
                    <i class="fa-solid fa-pen" style="font-size: 10px;"></i>
                  </button>
                </div>
              </td>

              <!-- Battle Record -->
              <td>
                <div style="font-size: 12.5px;">
                  <span style="color: var(--accent-gold); font-weight: 700;">👑 <?php echo toLangNum($u['battles_won']); ?> <?php echo __('জয়', 'Won'); ?></span>
                  <span style="font-size: 11px; color: var(--text-dim);"> / <?php echo toLangNum($u['battles_played']); ?> <?php echo __('ম্যাচ', 'Matches'); ?></span>
                </div>
                <?php if ($u['battles_played'] > 0): ?>
                  <div style="font-size: 10.5px; color: var(--text-dim);">
                    <?php echo __('জয়ের হার: ', 'Win Rate: ') . toLangNum($winRate) . '%'; ?>
                  </div>
                <?php endif; ?>
              </td>

              <!-- Daily Streak -->
              <td>
                <span style="color: #f97316; font-weight: 700; font-size: 13px; display: inline-flex; align-items: center; gap: 3px;">
                  🔥 <?php echo toLangNum($u['daily_streak']); ?> <?php echo __('দিন', 'Days'); ?>
                </span>
              </td>

              <!-- Tier Badge -->
              <td>
                <span class="badge <?php echo $tierClass; ?>" style="font-size: 11px; padding: 3px 8px;">
                  <?php echo $tierIcon . ' ' . $tierName; ?>
                </span>
              </td>

              <!-- Account Status -->
              <td>
                <span class="badge <?php echo ($u['is_banned'] ?? 0) ? 'badge-danger' : 'badge-success'; ?>" style="font-size: 10.5px;">
                  <?php echo ($u['is_banned'] ?? 0) ? __('🚫 ব্যানড', 'Banned') : __('✓ সক্রিয়', 'Active'); ?>
                </span>
              </td>

              <!-- Action Link -->
              <td style="text-align: right;">
                <a href="users.php?search=<?php echo urlencode($u['user_id']); ?>" class="btn btn-secondary btn-sm" style="padding: 4px 8px; font-size: 11.5px;" title="<?php echo __('ইউজার প্রোফাইল দেখুন', 'View Profile'); ?>">
                  <i class="fa-solid fa-arrow-up-right-from-square"></i>
                </a>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>

  <!-- Pagination -->
  <?php if ($totalPages > 1): ?>
    <div style="padding: 14px 20px; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
      <span style="font-size: 12.5px; color: var(--text-dim);">
        <?php echo __('পৃষ্ঠা ', 'Page ') . toLangNum($page) . __(' এর ', ' of ') . toLangNum($totalPages); ?> 
        (<?php echo __('মোট: ', 'Total: ') . toLangNum($totalFiltered); ?>)
      </span>
      <div style="display: flex; gap: 6px;">
        <?php if ($page > 1): ?>
          <a href="leaderboard.php?page=<?php echo $page - 1; ?>&search=<?php echo urlencode($search); ?>&sort=<?php echo urlencode($sortBy); ?>" class="btn btn-secondary btn-sm"><?php echo __('⬅️ পূর্ববর্তী', '⬅️ Previous'); ?></a>
        <?php endif; ?>
        <?php if ($page < $totalPages): ?>
          <a href="leaderboard.php?page=<?php echo $page + 1; ?>&search=<?php echo urlencode($search); ?>&sort=<?php echo urlencode($sortBy); ?>" class="btn btn-secondary btn-sm"><?php echo __('পরবর্তী ➡️', 'Next ➡️'); ?></a>
        <?php endif; ?>
      </div>
    </div>
  <?php endif; ?>
</div>

<!-- ==============================================================================
     MODAL: ADJUST USER POINTS (পয়েন্ট সমন্বয়)
     ============================================================================== -->
<div class="modal-backdrop" id="adjustPointsModal">
  <div class="modal-window" style="max-width: 420px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-pen-to-square" style="color: var(--primary);"></i>
        <span><?php echo __('পয়েন্ট সমন্বয়', 'Adjust Points'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('adjustPointsModal')">&times;</button>
    </div>
    <form method="POST" action="leaderboard.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="adjust_points">
      <input type="hidden" name="user_id" id="adjPointsUid" value="">

      <div class="modal-body" style="padding: 20px;">
        <p style="font-size: 13px; color: var(--text-dim); margin-bottom: 14px;" id="adjPointsUserDesc"></p>

        <div class="form-group">
          <label class="form-label"><?php echo __('পয়েন্ট', 'Points'); ?> *</label>
          <input type="number" name="points" id="adjPointsVal" class="form-control" required min="0" style="font-size: 16px; font-weight: 700;">
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('কারণ', 'Reason'); ?></label>
          <input type="text" name="reason" class="form-control" placeholder="<?php echo __('যেমন: বিশেষ কুইজ পুরস্কার', 'e.g. Special Quiz Reward'); ?>">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('adjustPointsModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ করুন', 'Save'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
function openAdjustPointsModal(uid, name, points) {
  document.getElementById('adjPointsUid').value = uid;
  document.getElementById('adjPointsVal').value = points;
  document.getElementById('adjPointsUserDesc').textContent = 'ইউজার: ' + name + ' (' + uid + ')';
  openModal('adjustPointsModal');
}

function openImportLeaderboardCsvModal() {
  document.getElementById('importLeaderboardCsvModal').style.display = 'flex';
}
function closeImportLeaderboardCsvModal() {
  document.getElementById('importLeaderboardCsvModal').style.display = 'none';
}
</script>

<!-- Modal: Import Leaderboard CSV -->
<div id="importLeaderboardCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 520px; box-shadow: var(--shadow-lg);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?php echo __('লিডারবোর্ড CSV ইমপোর্ট', 'Import CSV'); ?></span>
      </h3>
      <button type="button" onclick="closeImportLeaderboardCsvModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <form method="POST" action="leaderboard.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?php echo __('CSV ফাইল নির্বাচন করুন', 'Select CSV File'); ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?php echo __('সাপোর্টেড কলাম বিন্যাস:', 'Supported Column Format:'); ?></strong><br>
        <code>Rank, User_ID, Name, Username, Email, Phone, Blood_Group, District, Total_Points, Quiz_Points, Battles_Played, Battles_Won, Daily_Streak</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportLeaderboardCsvModal()" style="font-weight: 600;">
          <?php echo __('বাতিল', 'Cancel'); ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?php echo __('ইমপোর্ট করুন', 'Import'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
