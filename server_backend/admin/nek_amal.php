<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - NEK AMAL & TASBIH CHALLENGES SUITE (নেক আমল ও তাসবীহ চ্যালেঞ্জ)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Set Active Navigation identifier for sidebar
$activeNav = 'nek_amal';

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (verifyCsrfToken($csrfToken)) {
        if ($action === 'update_challenge') {
            $id = (int)($_POST['id'] ?? 0);
            $titleBn = trim($_POST['title_bn'] ?? '');
            $titleEn = trim($_POST['title_en'] ?? '');
            $meaningBn = trim($_POST['meaning_bn'] ?? '');
            $meaningEn = trim($_POST['meaning_en'] ?? '');
            $targetCount = max(1, (int)($_POST['target_count'] ?? 100));
            $rewardPoints = max(0, (int)($_POST['reward_points'] ?? 50));
            $isActive = !empty($_POST['is_active']) ? 1 : 0;

            if ($id > 0 && !empty($titleBn)) {
                try {
                    $stmt = $pdo->prepare("UPDATE tasbih_challenges SET 
                        title_bn = ?, title_en = ?, meaning_bn = ?, meaning_en = ?, 
                        target_count = ?, reward_points = ?, is_active = ? 
                        WHERE id = ?");
                    $stmt->execute([$titleBn, $titleEn, $meaningBn, $meaningEn, $targetCount, $rewardPoints, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_TASBIH_CHALLENGE', 'tasbih_challenges', $id, "আপডেট: $titleBn");
                    setFlash('success', __('তাসবীহ চ্যালেঞ্জ সফলভাবে আপডেট করা হয়েছে।', 'Tasbih challenge updated successfully.'));
                } catch (Exception $e) {
                    setFlash('danger', __('আপডেট ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
                }
            }
        }
    }
    redirect('nek_amal.php');
}

// -----------------------------------------------------------------------------
// Analytics & Statistics
// -----------------------------------------------------------------------------
$totalChallenges = 0;
$totalTasbihLogs = 0;
$totalCompletedRounds = 0;
$totalAmalLogs = 0;

try {
    $totalChallenges = (int)$pdo->query("SELECT COUNT(*) FROM tasbih_challenges")->fetchColumn();
    $totalTasbihLogs = (int)$pdo->query("SELECT COALESCE(SUM(tap_count), 0) FROM user_tasbih_logs")->fetchColumn();
    $totalCompletedRounds = (int)$pdo->query("SELECT COALESCE(SUM(completed_times), 0) FROM user_tasbih_logs")->fetchColumn();
    $totalAmalLogs = (int)$pdo->query("SELECT COUNT(*) FROM user_amal_records")->fetchColumn();
} catch (Exception $e) {}

// Active Tab
$tab = trim($_GET['tab'] ?? 'challenges');

// Data for Tab 1: Challenges
$challenges = [];
try {
    $challenges = $pdo->query("SELECT * FROM tasbih_challenges ORDER BY dhikr_index ASC")->fetchAll(PDO::FETCH_ASSOC);
} catch (Exception $e) {}

// Data for Tab 2: Tasbih Logs
$tasbihLogs = [];
$tasbihPage = max(1, (int)($_GET['page'] ?? 1));
$perPage = 25;
$tasbihOffset = ($tasbihPage - 1) * $perPage;
$totalTasbihRows = 0;

if ($tab === 'tasbih_logs') {
    $search = trim($_GET['search'] ?? '');
    $where = [];
    $params = [];
    if (!empty($search)) {
        $where[] = "(t.user_id LIKE ? OR t.dhikr_title LIKE ? OR u.name LIKE ? OR u.username LIKE ?)";
        $term = "%$search%";
        $params = [$term, $term, $term, $term];
    }
    $whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

    try {
        $cStmt = $pdo->prepare("SELECT COUNT(*) FROM user_tasbih_logs t LEFT JOIN users u ON t.user_id = u.user_id $whereSql");
        $cStmt->execute($params);
        $totalTasbihRows = (int)$cStmt->fetchColumn();

        $lStmt = $pdo->prepare("SELECT t.*, u.name as user_name, u.username, u.avatar, u.district 
                                FROM user_tasbih_logs t 
                                LEFT JOIN users u ON t.user_id = u.user_id 
                                $whereSql 
                                ORDER BY t.synced_at DESC 
                                LIMIT $perPage OFFSET $tasbihOffset");
        $lStmt->execute($params);
        $tasbihLogs = $lStmt->fetchAll(PDO::FETCH_ASSOC);
    } catch (Exception $e) {}
}

// Data for Tab 3: Amal Records
$amalRecords = [];
$amalPage = max(1, (int)($_GET['page'] ?? 1));
$amalOffset = ($amalPage - 1) * $perPage;
$totalAmalRows = 0;

if ($tab === 'amal_records') {
    $search = trim($_GET['search'] ?? '');
    $where = [];
    $params = [];
    if (!empty($search)) {
        $where[] = "(a.user_id LIKE ? OR a.amal_title LIKE ? OR a.amal_code LIKE ? OR u.name LIKE ? OR u.username LIKE ?)";
        $term = "%$search%";
        $params = [$term, $term, $term, $term, $term];
    }
    $whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

    try {
        $cStmt = $pdo->prepare("SELECT COUNT(*) FROM user_amal_records a LEFT JOIN users u ON a.user_id = u.user_id $whereSql");
        $cStmt->execute($params);
        $totalAmalRows = (int)$cStmt->fetchColumn();

        $lStmt = $pdo->prepare("SELECT a.*, u.name as user_name, u.username, u.avatar, u.district 
                                FROM user_amal_records a 
                                LEFT JOIN users u ON a.user_id = u.user_id 
                                $whereSql 
                                ORDER BY a.completed_at DESC 
                                LIMIT $perPage OFFSET $amalOffset");
        $lStmt->execute($params);
        $amalRecords = $lStmt->fetchAll(PDO::FETCH_ASSOC);
    } catch (Exception $e) {}
}

require_once __DIR__ . '/header.php';
?>

<div class="content-wrapper">
  <!-- Page Header -->
  <div class="page-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
    <div>
      <h1 class="page-title" style="font-size: 24px; font-weight: 700; color: var(--text-primary);">
        <i class="fa-solid fa-leaf text-emerald-500 mr-2"></i> <?php echo __('নেক আমল', 'Nek Amal'); ?>
      </h1>
    </div>
  </div>

  <!-- Summary Stats Row -->
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
    <div class="card p-4 flex items-center gap-4">
      <div class="w-12 h-12 rounded-xl bg-emerald-500/10 text-emerald-500 flex items-center justify-center text-xl">
        <i class="fa-solid fa-fingerprint"></i>
      </div>
      <div>
        <p class="text-xs text-gray-500 dark:text-gray-400 font-medium"><?php echo __('মোট তাসবীহ চ্যালেঞ্জ', 'Total Dhikr Challenges'); ?></p>
        <h3 class="text-2xl font-bold text-gray-800 dark:text-white"><?php echo toLangNum($totalChallenges); ?> <?php echo __('টি', ''); ?></h3>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-4">
      <div class="w-12 h-12 rounded-xl bg-blue-500/10 text-blue-500 flex items-center justify-center text-xl">
        <i class="fa-solid fa-hand-pointer"></i>
      </div>
      <div>
        <p class="text-xs text-gray-500 dark:text-gray-400 font-medium"><?php echo __('মোট তাসবীহ ট্যাপ কাউন্ট', 'Total Tasbih Taps'); ?></p>
        <h3 class="text-2xl font-bold text-gray-800 dark:text-white"><?php echo toLangNum($totalTasbihLogs); ?></h3>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-4">
      <div class="w-12 h-12 rounded-xl bg-amber-500/10 text-amber-500 flex items-center justify-center text-xl">
        <i class="fa-solid fa-circle-check"></i>
      </div>
      <div>
        <p class="text-xs text-gray-500 dark:text-gray-400 font-medium"><?php echo __('সম্পন্ন তাসবীহ চক্র (১০০x)', 'Completed 100x Rounds'); ?></p>
        <h3 class="text-2xl font-bold text-gray-800 dark:text-white"><?php echo toLangNum($totalCompletedRounds); ?></h3>
      </div>
    </div>

    <div class="card p-4 flex items-center gap-4">
      <div class="w-12 h-12 rounded-xl bg-purple-500/10 text-purple-500 flex items-center justify-center text-xl">
        <i class="fa-solid fa-clipboard-check"></i>
      </div>
      <div>
        <p class="text-xs text-gray-500 dark:text-gray-400 font-medium"><?php echo __('নেক আমল সম্পন্নের রেকর্ড', 'Amal Completions'); ?></p>
        <h3 class="text-2xl font-bold text-gray-800 dark:text-white"><?php echo toLangNum($totalAmalLogs); ?></h3>
      </div>
    </div>
  </div>

  <!-- Navigation Tabs -->
  <div class="flex border-b border-gray-200 dark:border-gray-700 mb-6 space-x-4">
    <a href="nek_amal.php?tab=challenges" class="py-3 px-4 text-sm font-semibold border-b-2 transition-colors <?php echo $tab === 'challenges' ? 'border-emerald-500 text-emerald-600 dark:text-emerald-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'; ?>">
      <i class="fa-solid fa-list-ol mr-2"></i> <?php echo __('তাসবীহ তালিকা', 'Tasbih List'); ?>
    </a>
    <a href="nek_amal.php?tab=tasbih_logs" class="py-3 px-4 text-sm font-semibold border-b-2 transition-colors <?php echo $tab === 'tasbih_logs' ? 'border-emerald-500 text-emerald-600 dark:text-emerald-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'; ?>">
      <i class="fa-solid fa-clock-rotate-left mr-2"></i> <?php echo __('তাসবীহ লগ', 'Tasbih Logs'); ?>
    </a>
    <a href="nek_amal.php?tab=amal_records" class="py-3 px-4 text-sm font-semibold border-b-2 transition-colors <?php echo $tab === 'amal_records' ? 'border-emerald-500 text-emerald-600 dark:text-emerald-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'; ?>">
      <i class="fa-solid fa-award mr-2"></i> <?php echo __('আমল রেকর্ড', 'Amal Records'); ?>
    </a>
  </div>

  <!-- Tab 1: Challenges Master List -->
  <?php if ($tab === 'challenges'): ?>
    <div class="card overflow-hidden">
      <div class="p-4 border-b border-gray-100 dark:border-gray-800 flex justify-between items-center">
        <h3 class="font-bold text-gray-800 dark:text-white flex items-center gap-2">
          <i class="fa-solid fa-hands-praying text-emerald-500"></i> <?php echo __('তাসবীহ তালিকা', 'Tasbih List'); ?>
        </h3>
      </div>
      <div class="overflow-x-auto">
        <table class="table w-full">
          <thead>
            <tr>
              <th class="w-12">#</th>
              <th><?php echo __('জিকির', 'Dhikr'); ?></th>
              <th><?php echo __('অর্থ ও রেফারেন্স', 'Meaning & Reference'); ?></th>
              <th class="text-center"><?php echo __('লক্ষ্য', 'Target'); ?></th>
              <th class="text-center"><?php echo __('পয়েন্ট', 'Points'); ?></th>
              <th class="text-center"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
              <th class="text-right"><?php echo __('পদক্ষেপ', 'Actions'); ?></th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($challenges as $c): ?>
              <tr>
                <td class="font-bold text-emerald-600 dark:text-emerald-400">
                  <?php echo toLangNum($c['dhikr_index'] + 1); ?>
                </td>
                <td>
                  <div class="font-bold text-gray-900 dark:text-white"><?php echo htmlspecialchars($c['title_bn']); ?></div>
                  <div class="text-xs text-gray-500 dark:text-gray-400 font-mono mt-0.5"><?php echo htmlspecialchars($c['title_en']); ?></div>
                </td>
                <td class="max-w-md">
                  <div class="text-xs text-gray-700 dark:text-gray-300 italic"><?php echo htmlspecialchars($c['meaning_bn']); ?></div>
                  <div class="text-xs text-gray-500 dark:text-gray-400 mt-1"><?php echo htmlspecialchars($c['meaning_en']); ?></div>
                </td>
                <td class="text-center font-bold text-gray-800 dark:text-white">
                  <span class="badge bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 px-2 py-1 rounded">
                    <?php echo toLangNum($c['target_count']); ?> <?php echo __('বার', 'times'); ?>
                  </span>
                </td>
                <td class="text-center font-bold text-amber-500">
                  +<?php echo toLangNum($c['reward_points']); ?> XP
                </td>
                <td class="text-center">
                  <?php if ($c['is_active']): ?>
                    <span class="badge bg-emerald-500 text-white text-xs px-2 py-0.5 rounded-full"><?php echo __('সক্রিয়', 'Active'); ?></span>
                  <?php else: ?>
                    <span class="badge bg-gray-400 text-white text-xs px-2 py-0.5 rounded-full"><?php echo __('নিষ্ক্রিয়', 'Inactive'); ?></span>
                  <?php endif; ?>
                </td>
                <td class="text-right">
                  <button type="button" class="btn btn-sm btn-secondary" onclick='openEditModal(<?php echo json_encode($c); ?>)'>
                    <i class="fa-solid fa-pen-to-square"></i>
                  </button>
                </td>
              </tr>
            <?php endforeach; ?>
          </tbody>
        </table>
      </div>
    </div>
  <?php endif; ?>

  <!-- Tab 2: User Live Tasbih Records -->
  <?php if ($tab === 'tasbih_logs'): ?>
    <div class="card overflow-hidden">
      <div class="p-4 border-b border-gray-100 dark:border-gray-800 flex flex-wrap justify-between items-center gap-4">
        <h3 class="font-bold text-gray-800 dark:text-white flex items-center gap-2">
          <i class="fa-solid fa-clock-rotate-left text-blue-500"></i> <?php echo __('ব্যবহারকারীদের লাইভ তাসবীহ লগ', 'User Live Tasbih Records'); ?>
        </h3>
        <form method="GET" class="flex gap-2">
          <input type="hidden" name="tab" value="tasbih_logs">
          <input type="text" name="search" value="<?php echo htmlspecialchars($_GET['search'] ?? ''); ?>" placeholder="<?php echo __('ইউজার আইডি, নাম বা জিকির খুঁজুন...', 'Search user, dhikr...'); ?>" class="input input-sm w-64">
          <button type="submit" class="btn btn-sm btn-primary"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
      </div>
      <div class="overflow-x-auto">
        <table class="table w-full">
          <thead>
            <tr>
              <th><?php echo __('ব্যবহারকারী', 'User'); ?></th>
              <th><?php echo __('তারিখ', 'Date'); ?></th>
              <th><?php echo __('জিকিরের নাম', 'Dhikr'); ?></th>
              <th class="text-center"><?php echo __('আজকের ট্যাপ', 'Taps Today'); ?></th>
              <th class="text-center"><?php echo __('সম্পন্ন চক্র (১০০x)', 'Done Rounds'); ?></th>
              <th class="text-center"><?php echo __('অর্জিত পয়েন্ট', 'Points'); ?></th>
              <th class="text-right"><?php echo __('সর্বশেষ সিঙ্ক', 'Last Synced'); ?></th>
            </tr>
          </thead>
          <tbody>
            <?php if (empty($tasbihLogs)): ?>
              <tr><td colspan="7" class="text-center py-8 text-gray-500"><?php echo __('কোনো রেকর্ড পাওয়া যায়নি', 'No records found'); ?></td></tr>
            <?php else: ?>
              <?php foreach ($tasbihLogs as $log): ?>
                <tr>
                  <td>
                    <div class="font-bold text-gray-900 dark:text-white"><?php echo htmlspecialchars($log['user_name'] ?? $log['user_id']); ?></div>
                    <div class="text-xs text-gray-500 font-mono"><?php echo htmlspecialchars($log['user_id']); ?></div>
                  </td>
                  <td class="text-sm font-medium"><?php echo htmlspecialchars($log['log_date']); ?></td>
                  <td class="font-medium text-emerald-600 dark:text-emerald-400 max-w-xs truncate">
                    <?php echo htmlspecialchars($log['dhikr_title']); ?>
                  </td>
                  <td class="text-center font-bold text-blue-600 dark:text-blue-400">
                    <?php echo toLangNum($log['tap_count']); ?> <?php echo __('বার', 'times'); ?>
                  </td>
                  <td class="text-center font-bold text-emerald-600 dark:text-emerald-400">
                    <?php echo toLangNum($log['completed_times']); ?> <?php echo __('বার', 'times'); ?>
                  </td>
                  <td class="text-center font-bold text-amber-500">
                    +<?php echo toLangNum($log['points_earned']); ?> XP
                  </td>
                  <td class="text-right text-xs text-gray-500">
                    <?php echo htmlspecialchars($log['synced_at']); ?>
                  </td>
                </tr>
              <?php endforeach; ?>
            <?php endif; ?>
          </tbody>
        </table>
      </div>
    </div>
  <?php endif; ?>

  <!-- Tab 3: Daily Amal Records -->
  <?php if ($tab === 'amal_records'): ?>
    <div class="card overflow-hidden">
      <div class="p-4 border-b border-gray-100 dark:border-gray-800 flex flex-wrap justify-between items-center gap-4">
        <h3 class="font-bold text-gray-800 dark:text-white flex items-center gap-2">
          <i class="fa-solid fa-award text-purple-500"></i> <?php echo __('দৈনিক নেক আমল সম্পন্নের ইতিহাস', 'Daily Amal Records'); ?>
        </h3>
        <form method="GET" class="flex gap-2">
          <input type="hidden" name="tab" value="amal_records">
          <input type="text" name="search" value="<?php echo htmlspecialchars($_GET['search'] ?? ''); ?>" placeholder="<?php echo __('ইউজার আইডি, নাম বা আমল খুঁজুন...', 'Search user, deed...'); ?>" class="input input-sm w-64">
          <button type="submit" class="btn btn-sm btn-primary"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
      </div>
      <div class="overflow-x-auto">
        <table class="table w-full">
          <thead>
            <tr>
              <th><?php echo __('ব্যবহারকারী', 'User'); ?></th>
              <th><?php echo __('তারিখ', 'Date'); ?></th>
              <th><?php echo __('আমলের কোড', 'Amal Code'); ?></th>
              <th><?php echo __('নেক আমলের শিরোনাম', 'Amal Title'); ?></th>
              <th class="text-center"><?php echo __('পয়েন্ট', 'Points'); ?></th>
              <th class="text-right"><?php echo __('সম্পন্নের সময়', 'Completed At'); ?></th>
            </tr>
          </thead>
          <tbody>
            <?php if (empty($amalRecords)): ?>
              <tr><td colspan="6" class="text-center py-8 text-gray-500"><?php echo __('কোনো রেকর্ড পাওয়া যায়নি', 'No records found'); ?></td></tr>
            <?php else: ?>
              <?php foreach ($amalRecords as $ar): ?>
                <tr>
                  <td>
                    <div class="font-bold text-gray-900 dark:text-white"><?php echo htmlspecialchars($ar['user_name'] ?? $ar['user_id']); ?></div>
                    <div class="text-xs text-gray-500 font-mono"><?php echo htmlspecialchars($ar['user_id']); ?></div>
                  </td>
                  <td class="text-sm font-medium"><?php echo htmlspecialchars($ar['log_date']); ?></td>
                  <td class="font-mono text-xs text-purple-600 dark:text-purple-400"><?php echo htmlspecialchars($ar['amal_code']); ?></td>
                  <td class="font-semibold text-gray-800 dark:text-gray-100"><?php echo htmlspecialchars($ar['amal_title']); ?></td>
                  <td class="text-center font-bold text-amber-500">+<?php echo toLangNum($ar['points']); ?> XP</td>
                  <td class="text-right text-xs text-gray-500"><?php echo htmlspecialchars($ar['completed_at']); ?></td>
                </tr>
              <?php endforeach; ?>
            <?php endif; ?>
          </tbody>
        </table>
      </div>
    </div>
  <?php endif; ?>

</div>

<!-- Edit Challenge Modal -->
<div id="editChallengeModal" class="modal-backdrop" style="display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.6); z-index: 999; align-items: center; justify-content: center;">
  <div class="modal-card bg-white dark:bg-gray-900 rounded-2xl p-6 max-w-lg w-full mx-4 shadow-2xl border border-gray-100 dark:border-gray-800 max-h-[90vh] overflow-y-auto">
    <div class="flex justify-between items-center pb-3 border-b border-gray-100 dark:border-gray-800 mb-4">
      <h3 class="font-bold text-lg text-gray-800 dark:text-white flex items-center gap-2">
        <i class="fa-solid fa-pen-to-square text-emerald-500"></i> <?php echo __('তাসবীহ সম্পাদনা', 'Edit Tasbih'); ?>
      </h3>
      <button type="button" onclick="closeEditModal()" class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200">
        <i class="fa-solid fa-xmark text-lg"></i>
      </button>
    </div>
    <form method="POST">
      <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
      <input type="hidden" name="action" value="update_challenge">
      <input type="hidden" id="edit_id" name="id" value="">

      <div class="space-y-4">
        <div>
          <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('বাংলা শিরোনাম', 'Bengali Title'); ?></label>
          <input type="text" id="edit_title_bn" name="title_bn" required class="input input-sm w-full">
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('ইংরেজি শিরোনাম', 'English Title'); ?></label>
          <input type="text" id="edit_title_en" name="title_en" required class="input input-sm w-full">
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('বাংলা অর্থ ও রেফারেন্স', 'Bengali Meaning'); ?></label>
          <textarea id="edit_meaning_bn" name="meaning_bn" rows="2" class="input input-sm w-full py-2"></textarea>
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('ইংরেজি অর্থ ও রেফারেন্স', 'English Meaning'); ?></label>
          <textarea id="edit_meaning_en" name="meaning_en" rows="2" class="input input-sm w-full py-2"></textarea>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('লক্ষ্য সংখ্যা', 'Target Count'); ?></label>
            <input type="number" id="edit_target_count" name="target_count" value="100" class="input input-sm w-full">
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 dark:text-gray-300 mb-1"><?php echo __('পয়েন্ট', 'Reward Points'); ?></label>
            <input type="number" id="edit_reward_points" name="reward_points" value="50" class="input input-sm w-full">
          </div>
        </div>
        <div class="flex items-center gap-2 pt-2">
          <input type="checkbox" id="edit_is_active" name="is_active" value="1" class="checkbox">
          <label for="edit_is_active" class="text-sm font-medium text-gray-700 dark:text-gray-300"><?php echo __('সক্রিয় রাখুন', 'Keep Active'); ?></label>
        </div>
      </div>

      <div class="flex justify-end gap-2 mt-6 pt-4 border-t border-gray-100 dark:border-gray-800">
        <button type="button" onclick="closeEditModal()" class="btn btn-sm btn-secondary"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-sm btn-primary"><?php echo __('সংরক্ষণ করুন', 'Save Changes'); ?></button>
      </div>
    </form>
  </div>
</div>

<script>
function openEditModal(challenge) {
  document.getElementById('edit_id').value = challenge.id;
  document.getElementById('edit_title_bn').value = challenge.title_bn;
  document.getElementById('edit_title_en').value = challenge.title_en;
  document.getElementById('edit_meaning_bn').value = challenge.meaning_bn;
  document.getElementById('edit_meaning_en').value = challenge.meaning_en;
  document.getElementById('edit_target_count').value = challenge.target_count;
  document.getElementById('edit_reward_points').value = challenge.reward_points;
  document.getElementById('edit_is_active').checked = challenge.is_active == 1;

  const modal = document.getElementById('editChallengeModal');
  modal.style.display = 'flex';
}

function closeEditModal() {
  document.getElementById('editChallengeModal').style.display = 'none';
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
