<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - REAL-TIME MULTIPLAYER BATTLE ROOMS & MATCH HISTORY SUITE
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure all battle tables exist
$pdo->exec("CREATE TABLE IF NOT EXISTS `battle_rooms` (
  `room_code` VARCHAR(10) PRIMARY KEY,
  `host_player_id` VARCHAR(100) NOT NULL,
  `category_id` VARCHAR(60) NOT NULL,
  `category_title_bn` VARCHAR(120) NOT NULL,
  `total_players` INT UNSIGNED DEFAULT 2,
  `total_questions` INT UNSIGNED DEFAULT 10,
  `time_per_question_sec` INT UNSIGNED DEFAULT 15,
  `difficulty` VARCHAR(20) DEFAULT 'MEDIUM',
  `negative_marking` TINYINT(1) DEFAULT 0,
  `lifecycle_state` VARCHAR(40) DEFAULT 'WAITING_FOR_PLAYERS',
  `current_question_index` INT UNSIGNED DEFAULT 0,
  `questions_data` MEDIUMTEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_lifecycle` (`lifecycle_state`),
  INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

$pdo->exec("CREATE TABLE IF NOT EXISTS `battle_players` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `room_code` VARCHAR(10) NOT NULL,
  `player_id` VARCHAR(100) NOT NULL,
  `player_name` VARCHAR(100) NOT NULL,
  `avatar_resource` VARCHAR(80) DEFAULT 'avatar_1',
  `is_host` TINYINT(1) DEFAULT 0,
  `is_bot` TINYINT(1) DEFAULT 0,
  `is_ready` TINYINT(1) DEFAULT 1,
  `score` INT DEFAULT 0,
  `correct_answers` INT DEFAULT 0,
  `wrong_answers` INT DEFAULT 0,
  `status` VARCHAR(30) DEFAULT 'READY',
  `last_selected_option` INT DEFAULT -1,
  `last_heartbeat` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `uniq_room_player` (`room_code`, `player_id`),
  INDEX `idx_room_code` (`room_code`),
  CONSTRAINT `fk_player_room` FOREIGN KEY (`room_code`) REFERENCES `battle_rooms` (`room_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

$pdo->exec("CREATE TABLE IF NOT EXISTS `battle_history` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `room_code` VARCHAR(10) NOT NULL,
  `category_id` VARCHAR(60) NOT NULL,
  `winner_player_id` VARCHAR(100) DEFAULT NULL,
  `winner_name` VARCHAR(100) DEFAULT NULL,
  `total_participants` INT UNSIGNED DEFAULT 2,
  `final_scores_json` TEXT DEFAULT NULL,
  `finished_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_winner` (`winner_player_id`),
  INDEX `idx_finished` (`finished_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Handle CSV Export
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $exportType = $_GET['type'] ?? 'history';
    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_battle_' . $exportType . '_' . date('Y-m-d_His') . '.csv"');
    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    if ($exportType === 'rooms') {
        fputcsv($out, ['Room_Code', 'Host_Player_ID', 'Category_ID', 'Category_Title_BN', 'Total_Players', 'Total_Questions', 'Time_Sec', 'Lifecycle_State', 'Created_At']);
        $stmt = $pdo->query("SELECT * FROM battle_rooms ORDER BY created_at DESC");
        while ($r = $stmt->fetch(PDO::FETCH_ASSOC)) {
            fputcsv($out, [$r['room_code'], $r['host_player_id'], $r['category_id'], $r['category_title_bn'], $r['total_players'], $r['total_questions'], $r['time_per_question_sec'], $r['lifecycle_state'], $r['created_at']]);
        }
    } else {
        fputcsv($out, ['Match_ID', 'Room_Code', 'Category_ID', 'Winner_Player_ID', 'Winner_Name', 'Total_Participants', 'Final_Scores_JSON', 'Finished_At']);
        $stmt = $pdo->query("SELECT * FROM battle_history ORDER BY finished_at DESC");
        while ($r = $stmt->fetch(PDO::FETCH_ASSOC)) {
            fputcsv($out, [$r['id'], $r['room_code'], $r['category_id'], $r['winner_player_id'], $r['winner_name'], $r['total_participants'], $r['final_scores_json'], $r['finished_at']]);
        }
    }
    fclose($out);
    exit();
}

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Security token invalid.'));
        redirect('battle_rooms.php');
    }

    // 1. Delete Single Match History
    if ($action === 'delete_history') {
        $id = (int)($_POST['history_id'] ?? 0);
        if ($id > 0) {
            try {
                $stmt = $pdo->prepare("DELETE FROM battle_history WHERE id = ?");
                $stmt->execute([$id]);
                logAdminAction($pdo, 'DELETE_BATTLE_HISTORY', 'battle_history', (string)$id, 'ম্যাচ হিস্ট্রি মুছে ফেলা হয়েছে');
                setFlash('success', __('ম্যাচ হিস্ট্রি সফলভাবে মুছে ফেলা হয়েছে।', 'Match history deleted successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('মুছতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
            }
        }
        redirect('battle_rooms.php');
    }

    // 2. Bulk Delete Match History
    if ($action === 'bulk_delete_history') {
        $ids = $_POST['selected_history_ids'] ?? [];
        if (!empty($ids) && is_array($ids)) {
            $intIds = array_filter(array_map('intval', $ids));
            if (!empty($intIds)) {
                $placeholders = implode(',', array_fill(0, count($intIds), '?'));
                try {
                    $stmt = $pdo->prepare("DELETE FROM battle_history WHERE id IN ($placeholders)");
                    $stmt->execute($intIds);
                    $delCount = $stmt->rowCount();
                    logAdminAction($pdo, 'BULK_DELETE_BATTLE_HISTORY', 'battle_history', null, "মুছে ফেলা হিস্ট্রি সংখ্যা: $delCount");
                    setFlash('success', sprintf(__('সফলভাবে %d টি ম্যাচ হিস্ট্রি মুছে ফেলা হয়েছে।', 'Successfully deleted %d match history records.'), $delCount));
                } catch (Exception $e) {
                    setFlash('danger', __('বাল্ক মুছতে ব্যর্থ: ', 'Failed to bulk delete: ') . $e->getMessage());
                }
            }
        } else {
            setFlash('warning', __('কোনো হিস্ট্রি নির্বাচন করা হয়নি।', 'No history records selected.'));
        }
        redirect('battle_rooms.php');
    }

    // 3. Clear All Match History
    if ($action === 'clear_all_history') {
        try {
            $pdo->exec("TRUNCATE TABLE battle_history");
            logAdminAction($pdo, 'CLEAR_ALL_BATTLE_HISTORY', 'battle_history', null, 'সকল ব্যাটেল ম্যাচ হিস্ট্রি সম্পূর্ণ ক্লিয়ার করা হয়েছে');
            setFlash('success', __('সকল সমাপ্ত ব্যাটেল হিস্ট্রি সফলভাবে ক্লিয়ার করা হয়েছে!', 'All completed match history has been cleared!'));
        } catch (Exception $e) {
            setFlash('danger', __('হিস্ট্রি ক্লিয়ার করতে ব্যর্থ: ', 'Failed to clear history: ') . $e->getMessage());
        }
        redirect('battle_rooms.php');
    }

    // 4. Close / Terminate Active Room
    if ($action === 'close_room') {
        $roomCode = trim($_POST['room_code'] ?? '');
        if (!empty($roomCode)) {
            try {
                $stmt = $pdo->prepare("DELETE FROM battle_rooms WHERE room_code = ?");
                $stmt->execute([$roomCode]);
                logAdminAction($pdo, 'CLOSE_BATTLE_ROOM', 'battle_rooms', $roomCode, 'রুম বন্ধ করা হয়েছে: ' . $roomCode);
                setFlash('success', sprintf(__('ব্যাটেল রুম (%s) সফলভাবে বন্ধ করা হয়েছে।', 'Battle room (%s) closed successfully.'), $roomCode));
            } catch (Exception $e) {
                setFlash('danger', __('রুম বন্ধ করতে ব্যর্থ: ', 'Failed to close room: ') . $e->getMessage());
            }
        }
        redirect('battle_rooms.php');
    }

    // 5. CSV Import Match History
    if ($action === 'import_history_csv') {
        if (isset($_FILES['csv_file']) && $_FILES['csv_file']['error'] === UPLOAD_ERR_OK) {
            $file = $_FILES['csv_file']['tmp_name'];
            $handle = fopen($file, 'r');
            if ($handle !== false) {
                $header = fgetcsv($handle, 4000, ",");
                $imported = 0;
                $stmt = $pdo->prepare("INSERT INTO battle_history (room_code, category_id, winner_player_id, winner_name, total_participants, final_scores_json, finished_at) 
                                       VALUES (?, ?, ?, ?, ?, ?, NOW())");
                while (($data = fgetcsv($handle, 4000, ",")) !== false) {
                    if (count($data) >= 3) {
                        $rcode = !empty($data[1]) ? trim($data[1]) : (!empty($data[0]) ? trim($data[0]) : '789');
                        $catId = !empty($data[2]) ? trim($data[2]) : 'general_knowledge';
                        $wId = !empty($data[3]) ? trim($data[3]) : null;
                        $wName = !empty($data[4]) ? trim($data[4]) : 'Winner Player';
                        $totP = !empty($data[5]) ? intval($data[5]) : 2;
                        $scoresJson = !empty($data[6]) ? trim($data[6]) : null;

                        $stmt->execute([$rcode, $catId, $wId, $wName, $totP, $scoresJson]);
                        $imported++;
                    }
                }
                fclose($handle);
                logAdminAction($pdo, 'IMPORT_BATTLE_HISTORY_CSV', 'battle_history', null, "Imported $imported records from CSV");
                setFlash('success', sprintf(__('সফলভাবে %d টি ম্যাচ হিস্ট্রি ইমপোর্ট করা হয়েছে!', 'Successfully imported %d match history records!'), $imported));
            } else {
                setFlash('danger', __('CSV ফাইল রিড করতে ব্যর্থ।', 'Failed to read CSV file.'));
            }
        } else {
            setFlash('danger', __('সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
        }
        redirect('battle_rooms.php');
    }
}

// Auto-Maintenance: Archive COMPLETED rooms to battle_history and cleanup stale/cancelled rooms
try {
    // 1. Move any COMPLETED battle_rooms to battle_history if missing
    $completedStmt = $pdo->query("SELECT * FROM battle_rooms WHERE lifecycle_state = 'COMPLETED'");
    $completedRooms = $completedStmt->fetchAll(PDO::FETCH_ASSOC);
    foreach ($completedRooms as $cr) {
        $cCheck = $pdo->prepare("SELECT COUNT(*) FROM battle_history WHERE room_code = ?");
        $cCheck->execute([$cr['room_code']]);
        if ($cCheck->fetchColumn() == 0) {
            $pStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? ORDER BY score DESC, correct_answers DESC");
            $pStmt->execute([$cr['room_code']]);
            $pls = $pStmt->fetchAll(PDO::FETCH_ASSOC);
            $winner = !empty($pls) ? $pls[0] : null;
            $wId = $winner ? $winner['player_id'] : null;
            $wName = $winner ? $winner['player_name'] : null;
            $totP = max(1, count($pls));
            $sJson = json_encode($pls, JSON_UNESCAPED_UNICODE);
            
            $insH = $pdo->prepare("INSERT INTO battle_history (room_code, category_id, winner_player_id, winner_name, total_participants, final_scores_json, finished_at) VALUES (?, ?, ?, ?, ?, ?, NOW())");
            $insH->execute([$cr['room_code'], $cr['category_id'] ?? 'general_knowledge', $wId, $wName, $totP, $sJson]);
        }
        // Remove from active battle_rooms table
        $delCr = $pdo->prepare("DELETE FROM battle_rooms WHERE room_code = ?");
        $delCr->execute([$cr['room_code']]);
    }

    // 2. Remove stale/cancelled rooms (older than 15 minutes or explicitly CANCELLED)
    $pdo->exec("DELETE FROM battle_rooms WHERE lifecycle_state = 'CANCELLED' OR created_at < NOW() - INTERVAL 15 MINUTE");
} catch (Exception $e) {}

// Fetch Active Rooms (Waiting or Currently In Progress)
$activeRooms = [];
try {
    $rStmt = $pdo->query("SELECT * FROM battle_rooms WHERE lifecycle_state NOT IN ('COMPLETED', 'CANCELLED') ORDER BY created_at DESC");
    $rawRooms = $rStmt->fetchAll(PDO::FETCH_ASSOC);

    foreach ($rawRooms as $room) {
        $pStmt = $pdo->prepare("SELECT * FROM battle_players WHERE room_code = ? ORDER BY score DESC, is_host DESC, id ASC");
        $pStmt->execute([$room['room_code']]);
        $players = $pStmt->fetchAll(PDO::FETCH_ASSOC);
        
        $room['players'] = $players;
        $room['current_players_count'] = count($players);
        $activeRooms[] = $room;
    }
} catch (Exception $e) {}

// Fetch Complete Match History
$history = [];
$totalCompletedBattles = 0;
try {
    $totalCompletedBattles = (int)$pdo->query("SELECT COUNT(*) FROM battle_history")->fetchColumn();
    $hStmt = $pdo->query("SELECT * FROM battle_history ORDER BY finished_at DESC");
    $history = $hStmt->fetchAll(PDO::FETCH_ASSOC);
} catch (Exception $e) {}

$pageTitle = __('লাইভ নলেজ ব্যাটেল রুম মনিটর', 'Live Knowledge Battle Rooms Monitor');
$activeNav = 'battle_rooms';
require_once __DIR__ . '/header.php';
?>

<!-- Clean Top Header & Breadcrumbs Bar -->
<div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px; margin-bottom: 22px;">
  <div>
    <h2 style="font-size: 20px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
      <span style="color: var(--accent-gold);"><i class="fa-solid fa-bolt"></i></span>
      <span><?= __('ব্যাটেল রুম', 'Battle Rooms') ?></span>
    </h2>
  </div>

  <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
    <a href="battle_rooms.php?action=export_csv&type=history" class="btn btn-secondary btn-sm" style="display: inline-flex; align-items: center; gap: 6px;">
      <i class="fa-solid fa-file-arrow-down"></i>
      <span><?= __('CSV এক্সপোর্ট', 'Export CSV') ?></span>
    </a>
    <button type="button" class="btn btn-secondary btn-sm" onclick="openImportHistoryModal()" style="display: inline-flex; align-items: center; gap: 6px;">
      <i class="fa-solid fa-file-arrow-up"></i>
      <span><?= __('CSV ইমপোর্ট', 'Import CSV') ?></span>
    </button>
    <a href="battle_rooms.php" class="btn btn-secondary btn-sm" style="display: inline-flex; align-items: center; gap: 6px;">
      <i class="fa-solid fa-rotate"></i>
      <span><?= __('রিফ্রেশ', 'Refresh') ?></span>
    </a>
  </div>
</div>

<!-- ==============================================================================
     1. ACTIVE & WAITING BATTLE ROOMS (সক্রিয় ব্যাটেল রুম)
     ============================================================================== -->
<div class="card mb-5">
  <div class="card-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <span style="color: var(--primary);"><i class="fa-solid fa-swords"></i></span>
      <span><?= __('সক্রিয় ব্যাটেল রুম', 'Active Rooms') ?></span>
      <span class="badge badge-info" style="font-size: 11px;">
        <?= toLangNum(count($activeRooms)) ?> <?= __('টি সক্রিয়', 'Active') ?>
      </span>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 120px;"><?= __('রুম কোড', 'Room Code') ?></th>
          <th><?= __('ক্যাটাগরি', 'Category') ?></th>
          <th style="text-align: center;"><?= __('খেলোয়াড়', 'Players') ?></th>
          <th><?= __('খেলোয়াড় ও স্কোর', 'Players & Scores') ?></th>
          <th style="text-align: center;"><?= __('স্ট্যাটাস', 'Status') ?></th>
          <th><?= __('সময়', 'Time') ?></th>
          <th style="text-align: right; width: 180px;"><?= __('অ্যাকশন', 'Actions') ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($activeRooms)): ?>
          <tr>
            <td colspan="7" style="text-align: center; padding: 36px 16px; color: var(--text-dim);">
              <div style="font-size: 24px; margin-bottom: 6px;">⚔️</div>
              <div style="font-weight: 700; color: var(--text-muted);"><?= __('কোনো সক্রিয় ব্যাটেল রুম নেই', 'No active battle rooms') ?></div>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($activeRooms as $r): ?>
            <tr>
              <td>
                <span style="font-family: monospace; font-size: 15px; font-weight: 800; color: var(--primary); background: rgba(16, 185, 129, 0.1); padding: 4px 10px; border-radius: 6px; border: 1px solid rgba(16, 185, 129, 0.25);">
                  <?= htmlspecialchars($r['room_code']) ?>
                </span>
              </td>
              <td>
                <span style="font-weight: 700; color: var(--text-heading);">
                  <?= htmlspecialchars($r['category_title_bn'] ?: $r['category_id']) ?>
                </span>
              </td>
              <td style="text-align: center;">
                <span class="badge badge-info" style="font-size: 11.5px;">
                  <?= toLangNum($r['current_players_count']) ?> / <?= toLangNum($r['total_players']) ?>
                </span>
              </td>
              <td>
                <?php if (empty($r['players'])): ?>
                  <span style="color: var(--text-dim); font-size: 12.5px;"><?= __('কোনো খেলোয়াড় নেই', 'No players') ?></span>
                <?php else: ?>
                  <div style="display: flex; flex-wrap: wrap; gap: 6px;">
                    <?php foreach ($r['players'] as $p): ?>
                      <span style="display: inline-flex; align-items: center; gap: 5px; background: var(--hover-bg); border: 1px solid var(--border-color); padding: 3px 8px; border-radius: 6px; font-size: 12px;">
                        <span>👤</span>
                        <strong style="color: var(--text-heading);"><?= htmlspecialchars($p['player_name']) ?></strong>
                        <?php if ($p['is_host']): ?>
                          <span style="font-size: 9.5px; background: rgba(245, 158, 11, 0.15); color: #d97706; padding: 1px 4px; border-radius: 3px; font-weight: 800;">HOST</span>
                        <?php endif; ?>
                        <span style="color: var(--primary); font-weight: 700;">(<?= toLangNum($p['score']) ?> pts)</span>
                      </span>
                    <?php endforeach; ?>
                  </div>
                <?php endif; ?>
              </td>
              <td style="text-align: center;">
                <?php
                $state = $r['lifecycle_state'];
                $badgeClass = 'badge-warning';
                $stateLabel = __('অপেক্ষমাণ', 'Waiting');
                if ($state === 'LIVE' || $state === 'IN_PROGRESS') {
                    $badgeClass = 'badge-success';
                    $stateLabel = __('চলমান', 'In Battle');
                } elseif ($state === 'COMPLETED') {
                    $badgeClass = 'badge-secondary';
                    $stateLabel = __('সমাপ্ত', 'Completed');
                } elseif ($state === 'COUNTDOWN') {
                    $badgeClass = 'badge-info';
                    $stateLabel = __('কাউন্টডাউন', 'Countdown');
                }
                ?>
                <span class="badge <?= $badgeClass ?>">
                  <?= $stateLabel ?>
                </span>
              </td>
              <td style="font-size: 12px; color: var(--text-muted); white-space: nowrap;">
                <?= isBn() ? toLangNum(date('h:i A', strtotime($r['created_at']))) : date('h:i A', strtotime($r['created_at'])) ?>
              </td>
              <td style="text-align: right;">
                <div style="display: inline-flex; gap: 6px; align-items: center;">
                  <button type="button" class="btn btn-secondary btn-sm" onclick='openActiveRoomModal(<?= json_encode($r, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE) ?>)' title="<?= __('খেলোয়াড়দের বিবরণ ও স্কোর দেখুন', 'View player details and scores') ?>">
                    <i class="fa-solid fa-users-viewfinder"></i> <?= __('বিস্তারিত', 'Details') ?>
                  </button>
                  <form method="POST" action="battle_rooms.php" style="display: inline;" onsubmit="return confirm('<?= __('আপনি কি এই ব্যাটেল রুমটি বন্ধ করে দিতে চান?', 'Are you sure you want to terminate this battle room?') ?>');">
                    <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                    <input type="hidden" name="action" value="close_room">
                    <input type="hidden" name="room_code" value="<?= htmlspecialchars($r['room_code']) ?>">
                    <button type="submit" class="btn btn-danger btn-sm" title="<?= __('রুম বন্ধ করুন', 'Close Room') ?>">
                      <i class="fa-solid fa-power-off"></i>
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- ==============================================================================
     2. RECENT & LIFETIME COMPLETED MATCH HISTORY (সম্প্রতি সমাপ্ত ম্যাচ হিস্ট্রি)
     ============================================================================== -->
<div class="card">
  <div class="card-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <span style="color: var(--accent-gold);"><i class="fa-solid fa-trophy"></i></span>
      <span><?= __('ম্যাচ হিস্ট্রি', 'Match History') ?></span>
      <span class="badge badge-info" style="font-size: 11px;">
        <?= toLangNum($totalCompletedBattles) ?> <?= __('টি ম্যাচ', 'Matches') ?>
      </span>
    </div>

    <!-- Bulk History Actions -->
    <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
      <?php if (!empty($history)): ?>
        <button type="button" class="btn btn-danger btn-sm" onclick="submitBulkDeleteHistory()" style="display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-trash-can"></i> <?= __('নির্বাচিত মুছুন', 'Delete Selected') ?>
        </button>
        <form method="POST" action="battle_rooms.php" style="display: inline;" onsubmit="return confirm('<?= __('আপনি কি সমস্ত ম্যাচ হিস্ট্রি সম্পূর্ণ ক্লিয়ার করতে চান? এই প্রক্রিয়াটি অপরিবর্তনীয়।', 'Are you sure you want to clear all completed match history? This action cannot be undone.') ?>');">
          <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
          <input type="hidden" name="action" value="clear_all_history">
          <button type="submit" class="btn btn-secondary btn-sm" style="display: inline-flex; align-items: center; gap: 6px;">
            <i class="fa-solid fa-trash-arrow-up"></i> <?= __('সকল মুছুন', 'Clear All') ?>
          </button>
        </form>
      <?php endif; ?>
    </div>
  </div>

  <form id="bulkDeleteHistoryForm" method="POST" action="battle_rooms.php">
    <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
    <input type="hidden" name="action" value="bulk_delete_history">

    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 40px; text-align: center;">
              <input type="checkbox" id="selectAllHistoryCheckbox" onchange="toggleSelectAllHistory(this)" style="cursor: pointer;">
            </th>
            <th style="width: 80px;"><?= __('ম্যাচ ID', 'Match ID') ?></th>
            <th style="width: 120px;"><?= __('রুম কোড', 'Room Code') ?></th>
            <th><?= __('ক্যাটাগরি', 'Category') ?></th>
            <th><?= __('বিজয়ী', 'Winner') ?></th>
            <th style="text-align: center;"><?= __('অংশগ্রহণকারী', 'Players') ?></th>
            <th><?= __('সময়', 'Time') ?></th>
            <th style="text-align: right; width: 160px;"><?= __('অ্যাকশন', 'Actions') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($history)): ?>
            <tr>
              <td colspan="8" style="text-align: center; padding: 36px 16px; color: var(--text-dim);">
                <div style="font-size: 24px; margin-bottom: 6px;">🏆</div>
                <div style="font-weight: 700; color: var(--text-muted);"><?= __('কোনো সমাপ্ত ম্যাচ হিস্ট্রি নেই', 'No match history recorded') ?></div>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($history as $h): ?>
              <tr>
                <td style="text-align: center;">
                  <input type="checkbox" name="selected_history_ids[]" value="<?= (int)$h['id'] ?>" class="history-checkbox" style="cursor: pointer;">
                </td>
                <td>
                  <span style="font-weight: 700; color: var(--text-muted);">#<?= toLangNum($h['id']) ?></span>
                </td>
                <td>
                  <span style="font-family: monospace; font-size: 14px; font-weight: 700; color: var(--primary);">
                    <?= htmlspecialchars($h['room_code']) ?>
                  </span>
                </td>
                <td>
                  <span style="font-weight: 600; color: var(--text-heading);">
                    <?= htmlspecialchars($h['category_id']) ?>
                  </span>
                </td>
                <td>
                  <span style="display: inline-flex; align-items: center; gap: 6px; font-weight: 700; color: #d97706;">
                    <span>👑</span>
                    <span><?= htmlspecialchars($h['winner_name'] ?: __('অনির্ধারিত / ড্র', 'Draw / Undetermined')) ?></span>
                  </span>
                </td>
                <td style="text-align: center;">
                  <span class="badge badge-info" style="font-size: 11.5px;">
                    <?= toLangNum($h['total_participants']) ?> <?= __('জন', 'players') ?>
                  </span>
                </td>
                <td style="font-size: 12px; color: var(--text-muted); white-space: nowrap;">
                  <?= isBn() ? toLangNum(date('d M, h:i A', strtotime($h['finished_at']))) : date('d M, h:i A', strtotime($h['finished_at'])) ?>
                </td>
                <td style="text-align: right;">
                  <div style="display: inline-flex; gap: 6px; align-items: center;">
                    <button type="button" class="btn btn-secondary btn-sm" onclick='openMatchHistoryModal(<?= json_encode($h, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE) ?>)' title="<?= __('পূর্ণাঙ্গ স্কোর ও বিস্তারিত রিপোর্ট দেখুন', 'View full scores and report') ?>">
                      <i class="fa-solid fa-list-ol"></i> <?= __('স্কোর', 'Scores') ?>
                    </button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteSingleHistory(<?= (int)$h['id'] ?>)" title="<?= __('হিস্ট্রি মুছুন', 'Delete') ?>">
                      <i class="fa-solid fa-trash-can"></i>
                    </button>
                  </div>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </form>
</div>

<!-- ==============================================================================
     MODAL 1: ACTIVE ROOM DETAILED PLAYERS BREAKDOWN
     ============================================================================== -->
<div class="modal-backdrop" id="activeRoomModal">
  <div class="modal-window" style="max-width: 640px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <span>⚔️</span>
        <span><?= __('ব্যাটেল রুম বিস্তারিত', 'Room Details') ?></span>
        <span id="activeModalRoomCode" style="font-family: monospace; font-size: 14px; background: rgba(16, 185, 129, 0.15); color: var(--primary); padding: 2px 8px; border-radius: 4px;"></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('activeRoomModal')">&times;</button>
    </div>

    <div class="modal-body">
      <!-- Room Info Strip -->
      <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; margin-bottom: 18px; padding: 12px; background: var(--hover-bg); border-radius: 8px; border: 1px solid var(--border-color);">
        <div>
          <small style="color: var(--text-muted); font-size: 11px; display: block;"><?= __('ক্যাটাগরি', 'Category') ?></small>
          <strong id="activeModalCategory" style="font-size: 13px; color: var(--text-heading);"></strong>
        </div>
        <div>
          <small style="color: var(--text-muted); font-size: 11px; display: block;"><?= __('প্রশ্ন ও সময়', 'Questions & Time') ?></small>
          <strong id="activeModalConfig" style="font-size: 13px; color: var(--text-heading);"></strong>
        </div>
        <div>
          <small style="color: var(--text-muted); font-size: 11px; display: block;"><?= __('স্ট্যাটাস', 'Status') ?></small>
          <strong id="activeModalStatus" style="font-size: 13px; color: var(--primary);"></strong>
        </div>
      </div>

      <!-- Players List Table -->
      <h4 style="font-size: 14px; font-weight: 700; color: var(--text-heading); margin: 0 0 10px 0; display: flex; align-items: center; gap: 6px;">
        <span>👥</span>
        <span><?= __('খেলোয়াড়দের তালিকা', 'Players List') ?></span>
      </h4>

      <div class="table-responsive" style="border: 1px solid var(--border-color); border-radius: 8px;">
        <table class="table" style="margin-bottom: 0;">
          <thead>
            <tr>
              <th><?= __('খেলোয়াড়', 'Player') ?></th>
              <th style="text-align: center;"><?= __('পয়েন্ট', 'Score') ?></th>
              <th style="text-align: center; color: #10b981;"><?= __('সঠিক উত্তর', 'Correct') ?></th>
              <th style="text-align: center; color: #ef4444;"><?= __('ভুল উত্তর', 'Wrong') ?></th>
              <th style="text-align: center;"><?= __('স্ট্যাটাস', 'Status') ?></th>
            </tr>
          </thead>
          <tbody id="activeModalPlayersTbody">
          </tbody>
        </table>
      </div>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" onclick="closeModal('activeRoomModal')"><?= __('বন্ধ করুন', 'Close') ?></button>
    </div>
  </div>
</div>

<!-- ==============================================================================
     MODAL 2: COMPLETED MATCH HISTORY LEADERBOARD BREAKDOWN
     ============================================================================== -->
<div class="modal-backdrop" id="matchHistoryModal">
  <div class="modal-window" style="max-width: 620px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <span>🏆</span>
        <span><?= __('স্কোরবোর্ড', 'Scoreboard') ?></span>
        <span id="historyModalMatchId" style="color: var(--text-muted); font-size: 13px;"></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('matchHistoryModal')">&times;</button>
    </div>

    <div class="modal-body">
      <!-- Winner Banner -->
      <div style="padding: 14px; background: linear-gradient(135deg, rgba(245, 158, 11, 0.12), rgba(16, 185, 129, 0.08)); border: 1px solid rgba(245, 158, 11, 0.3); border-radius: 8px; margin-bottom: 16px; display: flex; align-items: center; justify-content: space-between;">
        <div style="display: flex; align-items: center; gap: 10px;">
          <span style="font-size: 28px;">👑</span>
          <div>
            <small style="font-size: 11px; font-weight: 700; color: #d97706; text-transform: uppercase;"><?= __('বিজয়ী', 'Winner') ?></small>
            <h3 id="historyModalWinnerName" style="margin: 0; font-size: 16px; font-weight: 800; color: var(--text-heading);"></h3>
          </div>
        </div>
        <div>
          <span id="historyModalFinishedAt" style="font-size: 12px; color: var(--text-muted);"></span>
        </div>
      </div>

      <!-- Breakdown Table -->
      <div class="table-responsive" style="border: 1px solid var(--border-color); border-radius: 8px;">
        <table class="table" style="margin-bottom: 0;">
          <thead>
            <tr>
              <th style="width: 50px; text-align: center;"><?= __('র‌্যাংক', 'Rank') ?></th>
              <th><?= __('খেলোয়াড়', 'Player') ?></th>
              <th style="text-align: center;"><?= __('পয়েন্ট', 'Score') ?></th>
              <th style="text-align: center; color: #10b981;"><?= __('সঠিক', 'Correct') ?></th>
              <th style="text-align: center; color: #ef4444;"><?= __('ভুল', 'Wrong') ?></th>
            </tr>
          </thead>
          <tbody id="historyModalScoresTbody">
          </tbody>
        </table>
      </div>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" onclick="closeModal('matchHistoryModal')"><?= __('বন্ধ করুন', 'Close') ?></button>
    </div>
  </div>
</div>

<!-- ==============================================================================
     MODAL 3: IMPORT BATTLE CSV
     ============================================================================== -->
<div class="modal-backdrop" id="importHistoryModal">
  <div class="modal-window" style="max-width: 500px;">
    <div class="modal-header">
      <div class="modal-title">📤 <?= __('হিস্ট্রি CSV ইমপোর্ট', 'Import CSV') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('importHistoryModal')">&times;</button>
    </div>
    <form method="POST" action="battle_rooms.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="import_history_csv">

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label"><?= __('CSV ফাইল নির্বাচন করুন', 'Select CSV File') ?> *</label>
          <input type="file" name="csv_file" class="form-control" accept=".csv" required>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('importHistoryModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">📥 <?= __('ইমপোর্ট করুন', 'Import') ?></button>
      </div>
    </form>
  </div>
</div>

<!-- Single Delete Form -->
<form id="singleDeleteHistoryForm" method="POST" action="battle_rooms.php" style="display: none;">
  <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
  <input type="hidden" name="action" value="delete_history">
  <input type="hidden" name="history_id" id="singleDeleteHistoryId" value="">
</form>

<script>
function openActiveRoomModal(room) {
  document.getElementById('activeModalRoomCode').textContent = '#' + (room.room_code || '');
  document.getElementById('activeModalCategory').textContent = room.category_title_bn || room.category_id || 'সাধারণ';
  document.getElementById('activeModalConfig').textContent = (room.total_questions || 10) + ' টি প্রশ্ন • ' + (room.time_per_question_sec || 15) + ' সে.';
  document.getElementById('activeModalStatus').textContent = room.lifecycle_state || 'WAITING';

  var tbody = document.getElementById('activeModalPlayersTbody');
  tbody.innerHTML = '';

  if (room.players && room.players.length > 0) {
    room.players.forEach(function(p) {
      var tr = document.createElement('tr');
      tr.innerHTML = `
        <td>
          <div style="display: flex; align-items: center; gap: 8px;">
            <span style="font-size: 18px;">👤</span>
            <div>
              <div style="font-weight: 700; color: var(--text-heading); display: flex; align-items: center; gap: 5px;">
                <span>${escapeHtml(p.player_name || 'Player')}</span>
                ${parseInt(p.is_host) === 1 ? '<span style="font-size: 9.5px; background: rgba(245, 158, 11, 0.15); color: #d97706; padding: 1px 4px; border-radius: 3px; font-weight: 800;">HOST</span>' : ''}
                ${parseInt(p.is_bot) === 1 ? '<span style="font-size: 9.5px; background: rgba(148, 163, 184, 0.2); color: var(--text-muted); padding: 1px 4px; border-radius: 3px; font-weight: 800;">BOT</span>' : ''}
              </div>
              <small style="color: var(--text-muted); font-size: 11px;">ID: ${escapeHtml(p.player_id || '')}</small>
            </div>
          </div>
        </td>
        <td style="text-align: center; font-weight: 800; color: var(--primary); font-size: 14px;">
          ${p.score || 0} pts
        </td>
        <td style="text-align: center; font-weight: 700; color: #10b981;">
          ✓ ${p.correct_answers || 0}
        </td>
        <td style="text-align: center; font-weight: 700; color: #ef4444;">
          ✗ ${p.wrong_answers || 0}
        </td>
        <td style="text-align: center;">
          <span class="badge ${parseInt(p.is_ready) === 1 ? 'badge-success' : 'badge-warning'}" style="font-size: 10.5px;">
            ${parseInt(p.is_ready) === 1 ? 'READY' : 'WAITING'}
          </span>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } else {
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color: var(--text-dim);"><?= __('কোনো খেলোয়াড় যুক্ত হয়নি', 'No players found') ?></td></tr>';
  }

  document.getElementById('activeRoomModal').classList.add('open');
}

function openMatchHistoryModal(historyItem) {
  document.getElementById('historyModalMatchId').textContent = '#Match ' + (historyItem.id || '');
  document.getElementById('historyModalWinnerName').textContent = historyItem.winner_name || '<?= __('অনির্ধারিত / ড্র', 'Draw / Undetermined') ?>';
  document.getElementById('historyModalFinishedAt').textContent = historyItem.finished_at || '';

  var tbody = document.getElementById('historyModalScoresTbody');
  tbody.innerHTML = '';

  var scores = [];
  if (historyItem.final_scores_json) {
    try {
      scores = JSON.parse(historyItem.final_scores_json);
    } catch(e) {
      scores = [];
    }
  }

  if (scores && scores.length > 0) {
    scores.forEach(function(p, idx) {
      var rankMedal = (idx === 0) ? '🥇' : ((idx === 1) ? '🥈' : ((idx === 2) ? '🥉' : '#' + (idx + 1)));
      var tr = document.createElement('tr');
      tr.innerHTML = `
        <td style="text-align: center; font-weight: 800; font-size: 15px;">
          ${rankMedal}
        </td>
        <td>
          <div style="font-weight: 700; color: var(--text-heading);">${escapeHtml(p.player_name || 'Player')}</div>
          <small style="color: var(--text-muted); font-size: 11px;">ID: ${escapeHtml(p.player_id || '')}</small>
        </td>
        <td style="text-align: center; font-weight: 800; color: var(--primary); font-size: 14px;">
          ${p.score || 0} pts
        </td>
        <td style="text-align: center; font-weight: 700; color: #10b981;">
          ✓ ${p.correct_answers || 0}
        </td>
        <td style="text-align: center; font-weight: 700; color: #ef4444;">
          ✗ ${p.wrong_answers || 0}
        </td>
      `;
      tbody.appendChild(tr);
    });
  } else {
    tbody.innerHTML = `
      <tr>
        <td style="text-align: center; font-weight: 800;">🥇</td>
        <td>
          <div style="font-weight: 700; color: var(--text-heading);">${escapeHtml(historyItem.winner_name || 'Winner')}</div>
          <small style="color: var(--text-muted); font-size: 11px;">ID: ${escapeHtml(historyItem.winner_player_id || '')}</small>
        </td>
        <td style="text-align: center; font-weight: 800; color: var(--primary);">-- pts</td>
        <td style="text-align: center; color: #10b981;">--</td>
        <td style="text-align: center; color: #ef4444;">--</td>
      </tr>
    `;
  }

  document.getElementById('matchHistoryModal').classList.add('open');
}

function openImportHistoryModal() {
  document.getElementById('importHistoryModal').classList.add('open');
}

function closeModal(modalId) {
  var m = document.getElementById(modalId);
  if (m) m.classList.remove('open');
}

function toggleSelectAllHistory(master) {
  var checkboxes = document.querySelectorAll('.history-checkbox');
  checkboxes.forEach(function(cb) {
    cb.checked = master.checked;
  });
}

function submitBulkDeleteHistory() {
  var checked = document.querySelectorAll('.history-checkbox:checked');
  if (checked.length === 0) {
    alert('<?= __('অনুগ্রহ করে মুছে ফেলার জন্য অন্তত একটি হিস্ট্রি সিলেক্ট করুন।', 'Please select at least one history record to delete.') ?>');
    return;
  }
  if (confirm('<?= __('আপনি কি নিশ্চিত যে নির্বাচিত হিস্ট্রিগুলো মুছে ফেলতে চান?', 'Are you sure you want to delete selected history records?') ?>')) {
    document.getElementById('bulkDeleteHistoryForm').submit();
  }
}

function deleteSingleHistory(id) {
  if (confirm('<?= __('আপনি কি এই ম্যাচ হিস্ট্রিটি মুছে ফেলতে চান?', 'Are you sure you want to delete this match history record?') ?>')) {
    document.getElementById('singleDeleteHistoryId').value = id;
    document.getElementById('singleDeleteHistoryForm').submit();
  }
}

function escapeHtml(str) {
  if (!str) return '';
  return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

window.addEventListener('click', function(e) {
  if (e.target.classList.contains('modal-backdrop')) {
    e.target.classList.remove('open');
  }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
