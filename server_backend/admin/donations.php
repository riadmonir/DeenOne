<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - DONATIONS & VOLUNTARY CONTRIBUTIONS MANAGEMENT HUB
 * Clean, Ultra-Smooth Transaction Verification, Filtering & Reporting
 * ==============================================================================
 */

require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$pdo = getDbConnection();

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER (Direct database download)
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $exportFilterStatus = trim($_GET['status'] ?? 'ALL');
    $exportFilterMethod = trim($_GET['method'] ?? 'ALL');
    $exportSearch = trim($_GET['search'] ?? '');

    $exportWhere = [];
    $exportParams = [];

    if ($exportFilterStatus !== 'ALL' && in_array($exportFilterStatus, ['PENDING', 'VERIFIED', 'REJECTED'])) {
        $exportWhere[] = "status = ?";
        $exportParams[] = $exportFilterStatus;
    }
    if ($exportFilterMethod !== 'ALL') {
        $exportWhere[] = "payment_method = ?";
        $exportParams[] = $exportFilterMethod;
    }
    if (!empty($exportSearch)) {
        $exportWhere[] = "(transaction_id LIKE ? OR reference_code LIKE ? OR donor_name LIKE ? OR phone LIKE ? OR email LIKE ?)";
        $like = "%$exportSearch%";
        $exportParams = array_merge($exportParams, [$like, $like, $like, $like, $like]);
    }
    $whereSql = !empty($exportWhere) ? "WHERE " . implode(" AND ", $exportWhere) : "";

    $stmt = $pdo->prepare("SELECT * FROM donations $whereSql ORDER BY id DESC");
    $stmt->execute($exportParams);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_donations_' . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, [
        'ID', 'Reference_Code', 'Donor_Name', 'Email', 'Phone',
        'Amount', 'Currency', 'Amount_BDT', 'Payment_Method', 'Transaction_ID',
        'Frequency', 'Status', 'Is_Anonymous', 'Admin_Notes', 'Donor_Note',
        'Verified_By', 'Verified_At', 'Created_At'
    ]);

    foreach ($rows as $r) {
        fputcsv($out, [
            $r['id'] ?? '',
            $r['reference_code'] ?? '',
            $r['donor_name'] ?? '',
            $r['email'] ?? '',
            $r['phone'] ?? '',
            $r['amount'] ?? 0,
            $r['currency'] ?? 'BDT',
            $r['amount_bdt'] ?? 0,
            $r['payment_method'] ?? 'OTHER',
            $r['transaction_id'] ?? '',
            $r['frequency'] ?? 'ONE_TIME',
            $r['status'] ?? 'PENDING',
            $r['is_anonymous'] ?? 0,
            $r['admin_notes'] ?? '',
            $r['donor_note'] ?? '',
            $r['verified_by'] ?? '',
            $r['verified_at'] ?? '',
            $r['created_at'] ?? ''
        ]);
    }
    fclose($out);
    exit();
}

// -----------------------------------------------------------------------------
// 2. CSV IMPORT HANDLER (Upload CSV -> Sync into MySQL Database)
// -----------------------------------------------------------------------------
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'import_csv') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('donations.php');
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
            if (empty($row) || count($row) < 5) continue;

            $id = (int)($row[0] ?? 0);
            $refCode = trim($row[1] ?? '');
            $donorName = trim($row[2] ?? 'Anonymous Donor');
            $email = trim($row[3] ?? '');
            $phone = trim($row[4] ?? '');
            $amount = (float)($row[5] ?? 0);
            $currency = strtoupper(trim($row[6] ?? 'BDT'));
            $amountBdt = (float)($row[7] ?? 0);
            $paymentMethod = strtoupper(trim($row[8] ?? 'OTHER'));
            $trxId = trim($row[9] ?? '');
            $frequency = strtoupper(trim($row[10] ?? 'ONE_TIME'));
            $status = strtoupper(trim($row[11] ?? 'VERIFIED'));
            $isAnonymous = (int)($row[12] ?? 0);
            $adminNotes = trim($row[13] ?? 'CSV Import');
            $donorNote = trim($row[14] ?? '');
            $verifiedBy = trim($row[15] ?? 'Admin CSV');
            $verifiedAt = !empty($row[16]) ? trim($row[16]) : ($status === 'VERIFIED' ? date('Y-m-d H:i:s') : null);
            $createdAt = !empty($row[17]) ? trim($row[17]) : date('Y-m-d H:i:s');

            if ($amount <= 0 && empty($trxId)) continue;
            if (empty($trxId)) {
                $trxId = 'CSV-' . strtoupper(substr(md5(uniqid(mt_rand(), true)), 0, 8));
            }
            if (empty($refCode)) {
                $refCode = 'DN-' . strtoupper(substr(md5(uniqid(mt_rand(), true)), 0, 8));
            }
            if ($amountBdt <= 0) {
                $amountBdt = ($currency === 'USD') ? ($amount * 120) : $amount;
            }

            // Check if exists by TrxID or ID
            $checkStmt = $pdo->prepare("SELECT id FROM donations WHERE transaction_id = ? OR (id > 0 AND id = ?)");
            $checkStmt->execute([$trxId, $id]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $updateStmt = $pdo->prepare("UPDATE donations SET 
                    donor_name = ?, email = ?, phone = ?, amount = ?, currency = ?, 
                    amount_bdt = ?, payment_method = ?, frequency = ?, status = ?, 
                    is_anonymous = ?, admin_notes = ?, donor_note = ?, verified_by = ?, 
                    verified_at = ? WHERE id = ?");
                $updateStmt->execute([
                    $donorName, $email, $phone, $amount, $currency,
                    $amountBdt, $paymentMethod, $frequency, $status,
                    $isAnonymous, $adminNotes, $donorNote, $verifiedBy,
                    $verifiedAt, $existing['id']
                ]);
                $updatedCount++;
            } else {
                $insertStmt = $pdo->prepare("INSERT INTO donations 
                    (reference_code, donor_name, email, phone, amount, currency, amount_bdt, payment_method, transaction_id, frequency, status, is_anonymous, admin_notes, donor_note, verified_by, verified_at, created_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $insertStmt->execute([
                    $refCode, $donorName, $email, $phone, $amount, $currency, $amountBdt, $paymentMethod, $trxId, $frequency, $status, $isAnonymous, $adminNotes, $donorNote, $verifiedBy, $verifiedAt, $createdAt
                ]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " টি নতুন যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " টি আপডেট হয়েছে।";
        $msgEn = "CSV Import Completed: " . toLangNum($importedCount) . " new added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_DONATIONS', 'donations', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('donations.php');
}

// Handle Status Updates, Verification, Manual Entry, and Deletion
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর। পুনরায় চেষ্টা করুন।');
        redirect('donations.php');
    }

    if ($action === 'verify_status') {
        $id = (int)($_POST['id'] ?? 0);
        $status = strtoupper(trim($_POST['status'] ?? ''));
        $adminNotes = trim($_POST['admin_notes'] ?? '');

        if ($id > 0 && in_array($status, ['VERIFIED', 'REJECTED', 'PENDING'])) {
            $stmt = $pdo->prepare("UPDATE donations SET status = ?, admin_notes = ?, verified_at = IF(? = 'VERIFIED', NOW(), verified_at) WHERE id = ?");
            $stmt->execute([$status, $adminNotes, $status, $id]);

            $statusText = ($status === 'VERIFIED') ? __('অনুমোদিত', 'Verified') : (($status === 'REJECTED') ? __('বাতিল', 'Rejected') : __('পেন্ডিং', 'Pending'));
            logAdminAction($pdo, 'UPDATE_DONATION_STATUS', 'donations', $id, "অনুদানের স্ট্যাটাস পরিবর্তন: $statusText");
            setFlash('success', "ট্রানজেকশন #$id সফলভাবে $statusText করা হয়েছে।");
        }
    } elseif ($action === 'create_manual') {
        $donorName = trim($_POST['donor_name'] ?? '');
        $phone = trim($_POST['phone'] ?? '');
        $email = trim($_POST['email'] ?? '');
        $amount = (float)($_POST['amount'] ?? 0);
        $currency = strtoupper(trim($_POST['currency'] ?? 'BDT'));
        $paymentMethod = strtoupper(trim($_POST['payment_method'] ?? 'OTHER'));
        $transactionId = trim($_POST['transaction_id'] ?? '');
        $status = strtoupper(trim($_POST['status'] ?? 'VERIFIED'));
        $adminNotes = trim($_POST['admin_notes'] ?? 'ম্যানুয়াল এডমিন এন্ট্রি');

        if ($amount > 0 && !empty($transactionId)) {
            // Check for duplicate TrxID
            $dupCheck = $pdo->prepare("SELECT id FROM donations WHERE transaction_id = ?");
            $dupCheck->execute([$transactionId]);
            if ($dupCheck->fetch()) {
                setFlash('danger', __('এই ট্রানজেকশন আইডি দিয়ে ইতোমধ্যে অনুদান রেকর্ড বিদ্যমান রয়েছে।', 'A donation record already exists with this transaction ID.'));
            } else {
                $refCode = 'DN-' . strtoupper(substr(md5(uniqid(mt_rand(), true)), 0, 8));
                $amountBdt = ($currency === 'USD') ? ($amount * 120) : $amount;

                $stmt = $pdo->prepare("INSERT INTO donations 
                    (reference_code, donor_name, phone, email, amount, currency, amount_bdt, payment_method, transaction_id, status, is_anonymous, admin_notes, verified_at, created_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, IF(? = 'VERIFIED', NOW(), NULL), NOW())");
                
                $stmt->execute([
                    $refCode,
                    !empty($donorName) ? $donorName : 'Anonymous Donor',
                    $phone,
                    $email,
                    $amount,
                    $currency,
                    $amountBdt,
                    $paymentMethod,
                    $transactionId,
                    $status,
                    $adminNotes,
                    $status
                ]);

                $newId = $pdo->lastInsertId();
                logAdminAction($pdo, 'CREATE_MANUAL_DONATION', 'donations', $newId, "ম্যানুয়ালি অনুদান যুক্ত করা হয়েছে: $amount $currency ($transactionId)");
                setFlash('success', "নতুন অনুদান রেকর্ড #$refCode সফলভাবে তৈরি হয়েছে।");
            }
        } else {
            setFlash('danger', 'অনুগ্রহ করে পরিমাণ ও ট্রানজেকশন আইডি সঠিকভাবে পূরণ করুন।');
        }
    } elseif ($action === 'delete') {
        $id = (int)($_POST['id'] ?? 0);
        if ($id > 0) {
            $stmt = $pdo->prepare("DELETE FROM donations WHERE id = ?");
            $stmt->execute([$id]);
            logAdminAction($pdo, 'DELETE_DONATION', 'donations', $id, "অনুদান রেকর্ড মুছে ফেলা হয়েছে");
            setFlash('success', __('অনুদান রেকর্ডটি সফলভাবে মুছে ফেলা হয়েছে।', 'Donation record deleted successfully.'));
        }
    } elseif ($action === 'bulk_approve') {
        $rawIds = $_POST['selected_ids'] ?? '';
        $idList = is_array($rawIds) ? $rawIds : explode(',', (string)$rawIds);
        $ids = array_filter(array_map('intval', $idList), function($v) { return $v > 0; });
        if (!empty($ids)) {
            $placeholders = implode(',', array_fill(0, count($ids), '?'));
            $stmt = $pdo->prepare("UPDATE donations SET status = 'VERIFIED', verified_at = NOW() WHERE id IN ($placeholders)");
            $stmt->execute(array_values($ids));
            $count = count($ids);
            logAdminAction($pdo, 'BULK_APPROVE_DONATIONS', 'donations', 0, "বাল্ক অনুদান অনুমোদন: $count টি");
            setFlash('success', __('নির্বাচিত ' . toLangNum($count) . 'টি অনুদান সফলভাবে অনুমোদন করা হয়েছে।', 'Successfully approved ' . toLangNum($count) . ' selected donations.'));
        } else {
            setFlash('danger', __('কোনো অনুদান নির্বাচন করা হয়নি।', 'No donations were selected.'));
        }
    } elseif ($action === 'bulk_delete') {
        $rawIds = $_POST['selected_ids'] ?? '';
        $idList = is_array($rawIds) ? $rawIds : explode(',', (string)$rawIds);
        $ids = array_filter(array_map('intval', $idList), function($v) { return $v > 0; });
        if (!empty($ids)) {
            $placeholders = implode(',', array_fill(0, count($ids), '?'));
            $stmt = $pdo->prepare("DELETE FROM donations WHERE id IN ($placeholders)");
            $stmt->execute(array_values($ids));
            $count = count($ids);
            logAdminAction($pdo, 'BULK_DELETE_DONATIONS', 'donations', 0, "বাল্ক অনুদান মুছে ফেলা হয়েছে: $count টি");
            setFlash('success', __('নির্বাচিত ' . toLangNum($count) . 'টি অনুদান সফলভাবে মুছে ফেলা হয়েছে।', 'Successfully deleted ' . toLangNum($count) . ' selected donations.'));
        } else {
            setFlash('danger', __('কোনো অনুদান নির্বাচন করা হয়নি।', 'No donations were selected.'));
        }
    }

    redirect('donations.php');
}

// Filters & Pagination
$filterStatus = trim($_GET['status'] ?? 'ALL');
$filterMethod = trim($_GET['method'] ?? 'ALL');
$searchQuery = trim($_GET['search'] ?? '');
$page = max(1, (int)($_GET['page'] ?? 1));
$limit = 25;
$offset = ($page - 1) * $limit;

$whereClauses = [];
$params = [];

if ($filterStatus !== 'ALL' && in_array($filterStatus, ['PENDING', 'VERIFIED', 'REJECTED'])) {
    $whereClauses[] = "status = ?";
    $params[] = $filterStatus;
}

if ($filterMethod !== 'ALL') {
    $whereClauses[] = "payment_method = ?";
    $params[] = $filterMethod;
}

if (!empty($searchQuery)) {
    $whereClauses[] = "(transaction_id LIKE ? OR reference_code LIKE ? OR donor_name LIKE ? OR phone LIKE ? OR email LIKE ?)";
    $like = "%$searchQuery%";
    $params = array_merge($params, [$like, $like, $like, $like, $like]);
}

$whereSql = !empty($whereClauses) ? "WHERE " . implode(" AND ", $whereClauses) : "";

// Count Total
$countStmt = $pdo->prepare("SELECT COUNT(*) FROM donations $whereSql");
$countStmt->execute($params);
$totalRecords = (int)$countStmt->fetchColumn();
$totalPages = ceil($totalRecords / $limit);

// Fetch Records
$dataStmt = $pdo->prepare("SELECT * FROM donations $whereSql ORDER BY id DESC LIMIT $limit OFFSET $offset");
$dataStmt->execute($params);
$donations = $dataStmt->fetchAll();

// Summary Stats
$statsStmt = $pdo->query("SELECT 
    COUNT(*) as total_count,
    SUM(CASE WHEN status = 'VERIFIED' THEN amount_bdt ELSE 0 END) as total_verified_bdt,
    SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as total_pending,
    SUM(CASE WHEN status = 'VERIFIED' THEN 1 ELSE 0 END) as total_verified,
    SUM(CASE WHEN status = 'VERIFIED' AND DATE(created_at) = CURDATE() THEN amount_bdt ELSE 0 END) as today_verified_bdt
    FROM donations");
$stats = $statsStmt->fetch();

$totalCollectedBdt = (float)($stats['total_verified_bdt'] ?? 0);
$totalPendingCount = (int)($stats['total_pending'] ?? 0);
$totalVerifiedCount = (int)($stats['total_verified'] ?? 0);
$todayCollectedBdt = (float)($stats['today_verified_bdt'] ?? 0);

$pageTitle = __('অনুদান ব্যবস্থাপনা', 'Donation Management');
$activeNav = 'donations';
require_once __DIR__ . '/header.php';
?>

<style>
.method-pill {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 3px 9px;
  border-radius: 9999px;
  font-size: 11px;
  font-weight: 800;
  white-space: nowrap;
}
.method-pill.bkash { background: rgba(225, 29, 72, 0.1); color: #e11d48; border: 1px solid rgba(225, 29, 72, 0.25); }
.method-pill.nagad { background: rgba(234, 88, 12, 0.1); color: #ea580c; border: 1px solid rgba(234, 88, 12, 0.25); }
.method-pill.rocket { background: rgba(147, 51, 234, 0.1); color: #9333ea; border: 1px solid rgba(147, 51, 234, 0.25); }
.method-pill.bank { background: rgba(2, 132, 199, 0.1); color: #0284c7; border: 1px solid rgba(2, 132, 199, 0.25); }
.method-pill.binance, .method-pill.crypto { background: rgba(245, 158, 11, 0.1); color: #d97706; border: 1px solid rgba(245, 158, 11, 0.25); }
.method-pill.paypal { background: rgba(37, 99, 235, 0.1); color: #2563eb; border: 1px solid rgba(37, 99, 235, 0.25); }
.method-pill.other { background: rgba(100, 116, 139, 0.1); color: #64748b; border: 1px solid rgba(100, 116, 139, 0.25); }

/* Stat Metric Cards (Top 4 Summary Grid) */
.stat-metric-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 18px 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: transform 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}
.stat-metric-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}
.stat-icon-circle {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}
.stat-icon-circle.emerald { background: rgba(16, 185, 129, 0.12); color: #059669; border: 1px solid rgba(16, 185, 129, 0.25); }
.stat-icon-circle.amber { background: rgba(245, 158, 11, 0.12); color: #d97706; border: 1px solid rgba(245, 158, 11, 0.25); }
.stat-icon-circle.blue { background: rgba(2, 132, 199, 0.12); color: #0284c7; border: 1px solid rgba(2, 132, 199, 0.25); }

/* Donation Data Table Styling */
.donation-table {
  width: 100%;
  border-collapse: collapse;
}
.donation-table th {
  background: var(--table-header-bg);
  padding: 13px 16px;
  font-weight: 800;
  font-size: 12px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  border-bottom: 1px solid var(--border-color);
  white-space: nowrap;
}
.donation-table td {
  padding: 14px 16px;
  border-bottom: 1px solid var(--border-color);
  color: var(--text-main);
  vertical-align: middle;
}
.donation-table tbody tr {
  transition: background-color 0.15s ease;
}
.donation-table tbody tr:hover {
  background-color: var(--table-row-hover);
}
.donation-table tbody tr.selected-row {
  background-color: rgba(16, 185, 129, 0.09) !important;
}
.bulk-action-bar {
  animation: fadeInDown 0.2s ease-out;
}
@keyframes fadeInDown {
  from { opacity: 0; transform: translateY(-6px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>

<div class="content-body">

  <!-- Header Banner -->
  <div class="hero-banner" style="margin-bottom: 22px;">
    <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 14px;">
      <div>
        <h2 style="font-size: 24px; font-weight: 800; color: var(--text-heading);">
          <?php echo __('অনুদান লেনদেন', 'Donations'); ?>
        </h2>
      </div>

      <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
        <!-- CSV Export Button -->
        <a href="donations.php?action=export_csv<?php echo $filterStatus !== 'ALL' ? '&status=' . urlencode($filterStatus) : ''; ?><?php echo $filterMethod !== 'ALL' ? '&method=' . urlencode($filterMethod) : ''; ?><?php echo !empty($searchQuery) ? '&search=' . urlencode($searchQuery) : ''; ?>" class="btn btn-secondary btn-sm" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('সকল অনুদান ডেটা CSV ফরম্যাটে ডাউনলোড করুন', 'Download all donation records in CSV format'); ?>">
          <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
          <span><?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?></span>
        </a>

        <!-- CSV Import Button -->
        <button type="button" class="btn btn-secondary btn-sm" onclick="openImportDonationsCsvModal()" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?php echo __('CSV ফাইল থেকে অনুদান ডাটাবেজে ইমপোর্ট করুন', 'Import donation records from CSV file'); ?>">
          <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
          <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
        </button>

        <a href="donation_settings.php" class="btn btn-secondary btn-sm" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-sliders"></i>
          <span><?php echo __('সেটিংস', 'Settings'); ?></span>
        </a>

        <button type="button" class="btn btn-primary btn-sm" onclick="openManualModal()" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-plus"></i>
          <span><?php echo __('ম্যানুয়াল এন্ট্রি', 'Manual Entry'); ?></span>
        </button>
      </div>
    </div>
  </div>

  <!-- Compact Stat Cards (4 Grid) -->
  <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 22px;">
    
    <!-- 1. Total Verified Collected -->
    <div class="stat-metric-card" style="border-left: 4px solid #059669;">
      <div class="stat-icon-circle emerald">
        <i class="fa-solid fa-vault"></i>
      </div>
      <div>
        <div style="font-family: 'Outfit', sans-serif; font-size: 20px; font-weight: 900; color: #059669;">
          ৳<?php echo toLangNum(number_format($totalCollectedBdt, 2)); ?>
        </div>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-heading); margin-top: 2px;">
          <?php echo __('মোট সংগ্রহ', 'Total Collected'); ?>
        </div>
      </div>
    </div>

    <!-- 2. Pending Verification -->
    <a href="donations.php?status=PENDING" class="stat-metric-card" style="border-left: 4px solid #d97706; text-decoration: none;">
      <div class="stat-icon-circle amber">
        <i class="fa-solid fa-clock-rotate-left"></i>
      </div>
      <div>
        <div style="font-family: 'Outfit', sans-serif; font-size: 20px; font-weight: 900; color: #d97706;">
          <?php echo toLangNum($totalPendingCount); ?>
        </div>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-heading); margin-top: 2px;">
          <?php echo __('অপেক্ষমাণ', 'Pending'); ?>
        </div>
      </div>
    </a>

    <!-- 3. Verified Donors -->
    <a href="donations.php?status=VERIFIED" class="stat-metric-card" style="border-left: 4px solid #0284c7; text-decoration: none;">
      <div class="stat-icon-circle blue">
        <i class="fa-solid fa-circle-check"></i>
      </div>
      <div>
        <div style="font-family: 'Outfit', sans-serif; font-size: 20px; font-weight: 900; color: #0284c7;">
          <?php echo toLangNum($totalVerifiedCount); ?>
        </div>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-heading); margin-top: 2px;">
          <?php echo __('অনুমোদিত', 'Verified'); ?>
        </div>
      </div>
    </a>

    <!-- 4. Today's Collections -->
    <div class="stat-metric-card" style="border-left: 4px solid #10b981;">
      <div class="stat-icon-circle emerald">
        <i class="fa-solid fa-calendar-day"></i>
      </div>
      <div>
        <div style="font-family: 'Outfit', sans-serif; font-size: 20px; font-weight: 900; color: var(--text-heading);">
          ৳<?php echo toLangNum(number_format($todayCollectedBdt, 2)); ?>
        </div>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-heading); margin-top: 2px;">
          <?php echo __('আজকের সংগ্রহ', "Today's Collections"); ?>
        </div>
      </div>
    </div>

  </div>

  <!-- Filter & Search Bar -->
  <div class="card mb-4">
    <div class="card-body p-4">
      <form method="GET" action="donations.php" style="display: flex; align-items: center; flex-wrap: wrap; gap: 12px; justify-content: space-between;">
        
        <div style="display: flex; align-items: center; flex-wrap: wrap; gap: 10px; flex-grow: 1;">
          <!-- Status Filter -->
          <select name="status" class="form-control" style="width: auto; font-weight: 700;" onchange="this.form.submit()">
            <option value="ALL" <?php echo $filterStatus === 'ALL' ? 'selected' : ''; ?>><?php echo __('সকল স্ট্যাটাস', 'All Status'); ?></option>
            <option value="PENDING" <?php echo $filterStatus === 'PENDING' ? 'selected' : ''; ?>>⏳ <?php echo __('পেন্ডিং', 'Pending'); ?> (<?php echo $totalPendingCount; ?>)</option>
            <option value="VERIFIED" <?php echo $filterStatus === 'VERIFIED' ? 'selected' : ''; ?>>✓ <?php echo __('অনুমোদিত', 'Verified'); ?></option>
            <option value="REJECTED" <?php echo $filterStatus === 'REJECTED' ? 'selected' : ''; ?>>✕ <?php echo __('বাতিল', 'Rejected'); ?></option>
          </select>

          <!-- Method Filter -->
          <select name="method" class="form-control" style="width: auto; font-weight: 700;" onchange="this.form.submit()">
            <option value="ALL" <?php echo $filterMethod === 'ALL' ? 'selected' : ''; ?>><?php echo __('সকল মাধ্যম', 'All Methods'); ?></option>
            <option value="BKASH" <?php echo $filterMethod === 'BKASH' ? 'selected' : ''; ?>><?php echo __('বিকাশ', 'bKash'); ?></option>
            <option value="NAGAD" <?php echo $filterMethod === 'NAGAD' ? 'selected' : ''; ?>><?php echo __('নগদ', 'Nagad'); ?></option>
            <option value="ROCKET" <?php echo $filterMethod === 'ROCKET' ? 'selected' : ''; ?>><?php echo __('রকেট', 'Rocket'); ?></option>
            <option value="BANK" <?php echo $filterMethod === 'BANK' ? 'selected' : ''; ?>><?php echo __('ব্যাংক ট্রান্সফার', 'Bank Transfer'); ?></option>
            <option value="BINANCE" <?php echo $filterMethod === 'BINANCE' ? 'selected' : ''; ?>><?php echo __('বাইনান্স ও ক্রিপ্টো', 'Binance & Crypto'); ?></option>
            <option value="PAYPAL" <?php echo $filterMethod === 'PAYPAL' ? 'selected' : ''; ?>><?php echo __('পেপ্যাল', 'PayPal'); ?></option>
          </select>

          <!-- Search Input -->
          <div style="position: relative; flex-grow: 1; max-width: 320px;">
            <input type="text" name="search" class="form-control" value="<?php echo htmlspecialchars($searchQuery); ?>" placeholder="<?php echo __('TrxID, দাতার নাম বা ফোন খুঁজুন...', 'Search TrxID, donor, phone...'); ?>">
          </div>

          <button type="submit" class="btn btn-primary" style="padding: 8px 16px;">
            <i class="fa-solid fa-magnifying-glass"></i>
          </button>
          
          <?php if ($filterStatus !== 'ALL' || $filterMethod !== 'ALL' || !empty($searchQuery)): ?>
            <a href="donations.php" class="btn btn-secondary" title="রিসেট">
              <i class="fa-solid fa-rotate-left"></i>
            </a>
          <?php endif; ?>
        </div>

        <div style="font-size: 13px; font-weight: 700; color: var(--text-muted);">
          <?php echo __('মোট রেকর্ড:', 'Total:'); ?> <span style="color: var(--primary); font-weight: 900;"><?php echo toLangNum($totalRecords); ?></span>
        </div>
      </form>
    </div>
  </div>

  <!-- Bulk Action Bar -->
  <div id="bulkActionBar" class="bulk-action-bar" style="display: none; margin-bottom: 16px; background: linear-gradient(135deg, rgba(16,185,129,0.08), rgba(6,95,70,0.05)); border: 1px solid rgba(16,185,129,0.25); border-radius: var(--radius-lg); padding: 12px 18px; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px; box-shadow: 0 4px 14px rgba(0,0,0,0.05);">
    <div style="display: flex; align-items: center; gap: 12px;">
      <span class="badge" style="background: #10b981; color: #fff; font-size: 13px; font-weight: 800; padding: 5px 12px; border-radius: 9999px;">
        <i class="fa-solid fa-check-double mr-1"></i> <span id="selectedCountText">0</span> <?php echo __('টি নির্বাচিত', 'selected'); ?>
      </span>
      <span style="font-size: 13px; color: var(--text-heading); font-weight: 700;">
        <?php echo __('নির্বাচিত রেকর্ডগুলোর উপর বাল্ক অ্যাকশন:', 'Bulk actions on selected:'); ?>
      </span>
    </div>
    <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
      <button type="button" class="btn btn-primary" onclick="confirmBulkAction('approve')" style="background: #10b981; border-color: #10b981; font-weight: 800; padding: 8px 16px; display: inline-flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-circle-check"></i>
        <span><?php echo __('অনুমোদন করুন', 'Approve'); ?></span>
      </button>
      <button type="button" class="btn btn-danger" onclick="confirmBulkAction('delete')" style="font-weight: 800; padding: 8px 16px; display: inline-flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-trash-can"></i>
        <span><?php echo __('মুছে ফেলুন', 'Delete'); ?></span>
      </button>
      <button type="button" class="btn btn-secondary" onclick="clearAllSelection()" style="padding: 8px 14px; font-weight: 700;">
        <i class="fa-solid fa-xmark"></i>
        <span><?php echo __('বাতিল', 'Cancel'); ?></span>
      </button>
    </div>
  </div>

  <!-- Hidden Form for Bulk Action Submission -->
  <form id="bulkActionForm" method="POST" action="donations.php" style="display: none;">
    <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
    <input type="hidden" name="action" id="bulkActionType" value="">
    <input type="hidden" name="selected_ids" id="bulkSelectedIds" value="">
  </form>

  <!-- Clean Streamlined Table -->
  <div class="card mb-5">
    <div class="table-responsive">
      <table class="table donation-table">
        <colgroup>
          <col style="width: 46px;">
          <col style="width: 65px;">
          <col style="min-width: 160px;">
          <col style="width: 130px;">
          <col style="width: 120px;">
          <col style="min-width: 180px;">
          <col style="width: 120px;">
          <col style="width: 125px;">
          <col style="width: 125px;">
        </colgroup>
        <thead>
          <tr>
            <th style="text-align: center; width: 46px;">
              <input type="checkbox" id="selectAllCheckbox" onchange="toggleSelectAll(this.checked)" style="cursor: pointer; width: 17px; height: 17px; accent-color: #10b981;" title="<?php echo __('সবগুলো নির্বাচন করুন', 'Select All'); ?>">
            </th>
            <th style="text-align: center; width: 65px;"><?php echo __('আইডি', 'ID'); ?></th>
            <th style="min-width: 160px;"><?php echo __('দাতা', 'Donor'); ?></th>
            <th style="width: 130px;"><?php echo __('পরিমাণ', 'Amount'); ?></th>
            <th style="text-align: center; width: 120px;"><?php echo __('মাধ্যম', 'Method'); ?></th>
            <th style="min-width: 180px;"><?php echo __('ট্রানজেকশন আইডি', 'TrxID'); ?></th>
            <th style="text-align: center; width: 120px;"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
            <th style="width: 125px;"><?php echo __('সময়', 'Date'); ?></th>
            <th style="text-align: right; width: 125px;"><?php echo __('অ্যাকশন', 'Action'); ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($donations)): ?>
            <tr>
              <td colspan="9" style="text-align: center; padding: 40px 20px;">
                <div style="font-size: 36px; margin-bottom: 8px;">🕊️</div>
                <div style="font-size: 15px; font-weight: 800; color: var(--text-heading);"><?php echo __('কোনো অনুদান রেকর্ড পাওয়া যায়নি', 'No Donation Records Found'); ?></div>
                <div style="font-size: 13px; color: var(--text-muted); margin-top: 4px;"><?php echo __('ফিল্টার পরিবর্তন করুন অথবা নতুন ম্যানুয়াল এন্ট্রি যোগ করুন।', 'Adjust filters or add a new manual entry.'); ?></div>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($donations as $d): ?>
              <tr id="row_donation_<?php echo $d['id']; ?>">
                <!-- 0. Selection Checkbox -->
                <td style="text-align: center; width: 46px;">
                  <input type="checkbox" class="donation-checkbox" value="<?php echo $d['id']; ?>" onchange="updateSelectedCount()" style="cursor: pointer; width: 17px; height: 17px; accent-color: #10b981;">
                </td>

                <!-- 1. ID / Ref -->
                <td style="text-align: center; font-weight: 800; color: var(--primary); font-size: 13px;">
                  #<?php echo $d['id']; ?>
                </td>

                <!-- 2. Donor Name & Phone -->
                <td>
                  <div style="font-weight: 800; color: var(--text-heading); font-size: 13.5px; display: flex; align-items: center; gap: 6px;">
                    <span><?php echo htmlspecialchars($d['donor_name']); ?></span>
                    <?php if ($d['is_anonymous']): ?>
                      <span class="badge" style="background: rgba(100,116,139,0.1); color: #475569; font-size: 10px; padding: 1px 5px;" title="<?php echo __('গোপন দান', 'Anonymous'); ?>">
                        <i class="fa-solid fa-user-secret"></i>
                      </span>
                    <?php endif; ?>
                  </div>
                  <?php if (!empty($d['phone']) || !empty($d['email'])): ?>
                    <div style="font-size: 11.5px; color: var(--text-muted); margin-top: 2px;">
                      <?php echo htmlspecialchars($d['phone'] ?: $d['email']); ?>
                    </div>
                  <?php endif; ?>
                </td>

                <!-- 3. Amount -->
                <td>
                  <div style="font-family: 'Outfit', sans-serif; font-weight: 800; font-size: 15px; color: #059669;">
                    <?php echo ($d['currency'] === 'USD' ? '$' : '৳') . toLangNum(number_format($d['amount'], 2)); ?>
                  </div>
                  <?php if ($d['currency'] === 'USD' && !empty($d['amount_bdt'])): ?>
                    <div style="font-size: 11px; color: var(--text-muted); margin-top: 1px;">
                      ≈ ৳<?php echo toLangNum(number_format($d['amount_bdt'], 2)); ?>
                    </div>
                  <?php endif; ?>
                </td>

                <!-- 4. Payment Method -->
                <td style="text-align: center;">
                  <?php 
                    $m = strtoupper($d['payment_method']);
                    $badgeClass = 'other';
                    $icon = 'fa-solid fa-credit-card';
                    if (str_contains($m, 'BKASH')) { $badgeClass = 'bkash'; $icon = 'fa-solid fa-mobile-screen'; }
                    elseif (str_contains($m, 'NAGAD')) { $badgeClass = 'nagad'; $icon = 'fa-solid fa-wallet'; }
                    elseif (str_contains($m, 'ROCKET')) { $badgeClass = 'rocket'; $icon = 'fa-solid fa-paper-plane'; }
                    elseif (str_contains($m, 'BANK')) { $badgeClass = 'bank'; $icon = 'fa-solid fa-building-columns'; }
                    elseif (str_contains($m, 'BINANCE') || str_contains($m, 'CRYPTO') || str_contains($m, 'BITCOIN')) { $badgeClass = 'binance'; $icon = 'fa-brands fa-bitcoin'; }
                    elseif (str_contains($m, 'PAYPAL')) { $badgeClass = 'paypal'; $icon = 'fa-brands fa-paypal'; }
                  ?>
                  <span class="method-pill <?php echo $badgeClass; ?>">
                    <i class="<?php echo $icon; ?>"></i>
                    <span><?php echo htmlspecialchars($d['payment_method']); ?></span>
                  </span>
                </td>

                <!-- 5. TrxID with 1-click Copy -->
                <td>
                  <div style="display: inline-flex; align-items: center; gap: 6px; background: var(--hover-bg); padding: 3px 8px; border-radius: 6px; font-family: monospace; font-size: 12px; font-weight: 800; color: var(--text-heading);">
                    <span><?php echo htmlspecialchars($d['transaction_id']); ?></span>
                    <button type="button" style="background:none; border:none; color: var(--primary); cursor:pointer;" onclick="navigator.clipboard.writeText('<?php echo htmlspecialchars($d['transaction_id']); ?>'); alert('TrxID কপি হয়েছে!');" title="<?php echo __('কপি করুন', 'Copy'); ?>">
                      <i class="fa-regular fa-copy"></i>
                    </button>
                  </div>
                  <?php if (!empty($d['donor_note'])): ?>
                    <div style="font-size: 11px; color: #059669; margin-top: 3px; display: flex; align-items: flex-start; gap: 4px; background: rgba(5,150,105,0.08); padding: 2px 6px; border-radius: 4px;">
                      <i class="fa-solid fa-comment-dots" style="margin-top: 2px;"></i>
                      <span><strong><?php echo __('দাতার বার্তা:', 'Donor Note:'); ?></strong> <?php echo htmlspecialchars($d['donor_note']); ?></span>
                    </div>
                  <?php endif; ?>
                  <?php if (!empty($d['admin_notes']) && $d['admin_notes'] !== ($d['donor_note'] ?? '')): ?>
                    <div style="font-size: 11px; color: var(--text-muted); margin-top: 3px; display: flex; align-items: flex-start; gap: 4px;">
                      <i class="fa-regular fa-note-sticky text-amber-500" style="margin-top: 2px;"></i>
                      <span><?php echo htmlspecialchars($d['admin_notes']); ?></span>
                    </div>
                  <?php endif; ?>
                </td>

                <!-- 6. Status -->
                <td style="text-align: center;">
                  <?php if ($d['status'] === 'VERIFIED'): ?>
                    <span class="badge badge-success" style="font-weight: 800; font-size: 11px; padding: 3px 8px;">
                      <i class="fa-solid fa-check"></i> <?php echo __('অনুমোদিত', 'Verified'); ?>
                    </span>
                  <?php elseif ($d['status'] === 'PENDING'): ?>
                    <span class="badge badge-warning" style="background: rgba(245, 158, 11, 0.12); color: #d97706; border: 1px solid rgba(245, 158, 11, 0.25); font-weight: 800; font-size: 11px; padding: 3px 8px;">
                      <i class="fa-solid fa-clock"></i> <?php echo __('পেন্ডিং', 'Pending'); ?>
                    </span>
                  <?php else: ?>
                    <span class="badge badge-danger" style="font-weight: 800; font-size: 11px; padding: 3px 8px;">
                      <i class="fa-solid fa-xmark"></i> <?php echo __('বাতিল', 'Rejected'); ?>
                    </span>
                  <?php endif; ?>
                </td>

                <!-- 7. Date & Time -->
                <td style="font-size: 12px; color: var(--text-muted); white-space: nowrap;">
                  <div style="font-weight: 600; color: var(--text-main);"><?php echo date('d M Y', strtotime($d['created_at'])); ?></div>
                  <div style="font-size: 11px; color: var(--text-dim);"><?php echo date('h:i A', strtotime($d['created_at'])); ?></div>
                </td>

                <!-- 8. Actions -->
                <td style="text-align: right; white-space: nowrap;">
                  <div style="display: inline-flex; align-items: center; justify-content: flex-end; gap: 4px;">
                    <?php if ($d['status'] === 'PENDING'): ?>
                      <!-- 1-Click Verify -->
                      <form method="POST" action="donations.php" style="display:inline;" onsubmit="return confirm('এই অনুদানটি অনুমোদন করতে চান?');">
                        <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                        <input type="hidden" name="action" value="verify_status">
                        <input type="hidden" name="id" value="<?php echo $d['id']; ?>">
                        <input type="hidden" name="status" value="VERIFIED">
                        <button type="submit" class="btn btn-primary btn-sm" style="background: #10b981; border-color: #10b981; padding: 5px 8px;" title="<?php echo __('অনুমোদন করুন', 'Verify'); ?>">
                          <i class="fa-solid fa-check"></i>
                        </button>
                      </form>
                    <?php endif; ?>

                    <button type="button" class="btn btn-secondary btn-sm" style="padding: 5px 8px;" onclick="openEditModal(<?php echo htmlspecialchars(json_encode($d)); ?>)" title="নোট ও স্ট্যাটাস সম্পাদনা">
                      <i class="fa-solid fa-pen-to-square text-blue-500"></i>
                    </button>

                    <form method="POST" action="donations.php" style="display:inline;" onsubmit="return confirm('আপনি কি নিশ্চিত যে এই রেকর্ডটি মুছে ফেলতে চান?');">
                      <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="id" value="<?php echo $d['id']; ?>">
                      <button type="submit" class="btn btn-danger btn-sm" style="padding: 5px 8px;" title="মুছুন">
                        <i class="fa-solid fa-trash"></i>
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

    <!-- Pagination -->
    <?php if ($totalPages > 1): ?>
      <div class="card-footer" style="display: flex; align-items: center; justify-content: space-between; padding: 14px 20px;">
        <div style="font-size: 13px; color: var(--text-muted);">
          <?php echo __('পৃষ্ঠা:', 'Page:'); ?> <?php echo toLangNum($page); ?> / <?php echo toLangNum($totalPages); ?>
        </div>
        <div style="display: flex; gap: 6px;">
          <?php for ($p = 1; $p <= $totalPages; $p++): ?>
            <a href="donations.php?page=<?php echo $p; ?>&status=<?php echo urlencode($filterStatus); ?>&method=<?php echo urlencode($filterMethod); ?>&search=<?php echo urlencode($searchQuery); ?>" class="btn btn-sm <?php echo $p === $page ? 'btn-primary' : 'btn-secondary'; ?>" style="min-width: 32px; text-align: center;">
              <?php echo toLangNum($p); ?>
            </a>
          <?php endfor; ?>
        </div>
      </div>
    <?php endif; ?>
  </div>

</div>

<!-- =========================================================================
     MANUAL DONATION ENTRY MODAL
     ========================================================================= -->
<div class="modal-backdrop" id="manualModalBackdrop" style="display: none; position: fixed; inset: 0; background: rgba(15,23,42,0.6); backdrop-filter: blur(4px); z-index: 100; align-items: center; justify-content: center; padding: 20px;">
  <div class="modal-card" style="background: var(--bg-card); width: 100%; max-width: 540px; border-radius: var(--radius-xl); padding: 24px; border: 1px solid var(--border-color); box-shadow: var(--shadow-lg);">
    
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 18px; padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-hand-holding-dollar text-emerald-500"></i>
        <span><?php echo __('নতুন এন্ট্রি', 'Manual Entry'); ?></span>
      </h3>
      <button type="button" class="btn btn-secondary btn-sm" onclick="closeManualModal()">&times;</button>
    </div>

    <form method="POST" action="donations.php">
      <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
      <input type="hidden" name="action" value="create_manual">

      <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 14px;">
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('দাতার নাম', 'Donor Name'); ?></label>
          <input type="text" name="donor_name" class="form-control" placeholder="যেমন: আব্দুল্লাহ">
        </div>
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('মোবাইল নম্বর', 'Phone'); ?></label>
          <input type="text" name="phone" class="form-control" placeholder="017XXXXXXXX">
        </div>
      </div>

      <div class="form-row" style="display: grid; grid-template-columns: 1.2fr 1fr; gap: 14px; margin-bottom: 14px;">
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('অনুদানের পরিমাণ *', 'Amount *'); ?></label>
          <input type="number" step="0.01" name="amount" class="form-control font-bold" required placeholder="500">
        </div>
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('কারেন্সি', 'Currency'); ?></label>
          <select name="currency" class="form-control font-bold">
            <option value="BDT"><?php echo __('টাকা', 'BDT'); ?></option>
            <option value="USD"><?php echo __('ডলার', 'USD'); ?></option>
          </select>
        </div>
      </div>

      <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 14px;">
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('পেমেন্ট মাধ্যম *', 'Payment Method *'); ?></label>
          <select name="payment_method" class="form-control font-bold" required>
            <option value="BKASH"><?php echo __('বিকাশ', 'bKash'); ?></option>
            <option value="NAGAD"><?php echo __('নগদ', 'Nagad'); ?></option>
            <option value="ROCKET"><?php echo __('রকেট', 'Rocket'); ?></option>
            <option value="BANK"><?php echo __('ব্যাংক ট্রান্সফার', 'Bank Transfer'); ?></option>
            <option value="BINANCE"><?php echo __('বাইনান্স ও ক্রিপ্টো', 'Binance & Crypto'); ?></option>
            <option value="PAYPAL">PayPal</option>
            <option value="OTHER"><?php echo __('অন্যান্য গেটওয়ে', 'Other Gateway'); ?></option>
          </select>
        </div>
        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('স্ট্যাটাস', 'Status'); ?></label>
          <select name="status" class="form-control font-bold">
            <option value="VERIFIED"><?php echo __('✓ অনুমোদিত', '✓ Verified'); ?></option>
            <option value="PENDING"><?php echo __('⏳ পেন্ডিং', '⏳ Pending'); ?></option>
          </select>
        </div>
      </div>

      <div class="form-group mb-4">
        <label class="form-label"><?php echo __('ট্রানজেকশন আইডি *', 'Transaction ID *'); ?></label>
        <input type="text" name="transaction_id" class="form-control font-bold" required placeholder="<?php echo __('যেমন: 9K8J7H6G5F', 'e.g. 9K8J7H6G5F'); ?>">
      </div>

      <div class="form-group mb-4">
        <label class="form-label"><?php echo __('এডমিন নোট', 'Admin Notes'); ?></label>
        <textarea name="admin_notes" class="form-control" rows="2" placeholder="<?php echo __('নোট বা রেফারেন্স লিখুন...', 'Write reference or notes...'); ?>"></textarea>
      </div>

      <div style="display: flex; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary" onclick="closeManualModal()"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary"><?php echo __('সংরক্ষণ করুন', 'Save Record'); ?></button>
      </div>
    </form>

  </div>
</div>

<!-- =========================================================================
     EDIT / VIEW NOTES MODAL
     ========================================================================= -->
<div class="modal-backdrop" id="editModalBackdrop" style="display: none; position: fixed; inset: 0; background: rgba(15,23,42,0.6); backdrop-filter: blur(4px); z-index: 100; align-items: center; justify-content: center; padding: 20px;">
  <div class="modal-card" style="background: var(--bg-card); width: 100%; max-width: 480px; border-radius: var(--radius-xl); padding: 24px; border: 1px solid var(--border-color); box-shadow: var(--shadow-lg);">
    
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 18px; padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
      <h3 style="font-size: 17px; font-weight: 800; color: var(--text-heading);">
        <i class="fa-solid fa-pen-to-square text-blue-500 mr-1.5"></i>
        <span><?php echo __('নোট ও স্ট্যাটাস', 'Edit Status'); ?></span>
      </h3>
      <button type="button" class="btn btn-secondary btn-sm" onclick="closeEditModal()">&times;</button>
    </div>

    <form method="POST" action="donations.php">
      <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
      <input type="hidden" name="action" value="verify_status">
      <input type="hidden" name="id" id="editModalId" value="0">

      <div class="form-group mb-4">
        <label class="form-label"><?php echo __('স্ট্যাটাস', 'Status'); ?></label>
        <select name="status" id="editModalStatus" class="form-control font-bold">
          <option value="VERIFIED"><?php echo __('✓ অনুমোদিত', '✓ Verified'); ?></option>
          <option value="PENDING"><?php echo __('⏳ পেন্ডিং', '⏳ Pending'); ?></option>
          <option value="REJECTED"><?php echo __('✕ বাতিল', '✕ Rejected'); ?></option>
        </select>
      </div>

      <div class="form-group mb-4">
        <label class="form-label"><?php echo __('এডমিন নোট', 'Admin Notes'); ?></label>
        <textarea name="admin_notes" id="editModalNotes" class="form-control" rows="3" placeholder="যাচাইকরণ নোট লিখুন..."></textarea>
      </div>

      <div style="display: flex; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary" onclick="closeEditModal()"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary"><?php echo __('আপডেট করুন', 'Update Status'); ?></button>
      </div>
    </form>

  </div>
</div>

<!-- Floating Bottom Bulk Bar -->
<div id="floatingBulkBar" style="display: none; position: fixed; bottom: 26px; left: 50%; transform: translateX(-50%); z-index: 999; background: var(--bg-card); border: 2px solid #10b981; box-shadow: 0 10px 30px rgba(0,0,0,0.18); border-radius: 50px; padding: 10px 22px; align-items: center; gap: 14px; animation: fadeInUp 0.2s ease-out;">
  <span class="badge" style="background: #10b981; color: #fff; font-size: 13px; font-weight: 800; padding: 5px 12px; border-radius: 9999px;">
    <span id="floatingSelectedCount">0</span> <?php echo __('টি নির্বাচিত', 'selected'); ?>
  </span>
  <button type="button" class="btn btn-primary btn-sm" onclick="confirmBulkAction('approve')" style="background: #10b981; border-color: #10b981; font-weight: 800; border-radius: 20px; padding: 7px 16px; display: inline-flex; align-items: center; gap: 6px;">
    <i class="fa-solid fa-circle-check"></i>
    <span><?php echo __('অনুমোদন', 'Approve'); ?></span>
  </button>
  <button type="button" class="btn btn-danger btn-sm" onclick="confirmBulkAction('delete')" style="font-weight: 800; border-radius: 20px; padding: 7px 16px; display: inline-flex; align-items: center; gap: 6px;">
    <i class="fa-solid fa-trash-can"></i>
    <span><?php echo __('ডিলিট', 'Delete'); ?></span>
  </button>
  <button type="button" class="btn btn-secondary btn-sm" onclick="clearAllSelection()" style="border-radius: 20px; padding: 7px 12px;" title="<?php echo __('নির্বাচন বাতিল', 'Clear'); ?>">
    <i class="fa-solid fa-xmark"></i>
  </button>
</div>

<script>
const currentAdminLang = '<?php echo getAdminLang(); ?>';

function toAdminLangNum(num) {
  if (currentAdminLang !== 'bn') return String(num);
  const bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
  return String(num).replace(/[0-9]/g, d => bnDigits[d]);
}

function toggleSelectAll(isChecked) {
  const checkboxes = document.querySelectorAll('.donation-checkbox');
  checkboxes.forEach(cb => {
    cb.checked = isChecked;
    const row = document.getElementById('row_donation_' + cb.value);
    if (row) {
      row.classList.toggle('selected-row', isChecked);
    }
  });
  updateSelectedCount();
}

function updateSelectedCount() {
  const checkboxes = document.querySelectorAll('.donation-checkbox');
  const checked = document.querySelectorAll('.donation-checkbox:checked');
  const count = checked.length;
  const total = checkboxes.length;

  checkboxes.forEach(cb => {
    const row = document.getElementById('row_donation_' + cb.value);
    if (row) {
      row.classList.toggle('selected-row', cb.checked);
    }
  });

  const selectAll = document.getElementById('selectAllCheckbox');
  if (selectAll) {
    selectAll.checked = (total > 0 && count === total);
    selectAll.indeterminate = (count > 0 && count < total);
  }

  const countFormatted = toAdminLangNum(count);
  const countText = document.getElementById('selectedCountText');
  const floatingCount = document.getElementById('floatingSelectedCount');
  if (countText) countText.innerText = countFormatted;
  if (floatingCount) floatingCount.innerText = countFormatted;

  const bulkBar = document.getElementById('bulkActionBar');
  const floatingBar = document.getElementById('floatingBulkBar');
  if (count > 0) {
    if (bulkBar) bulkBar.style.display = 'flex';
    if (floatingBar) floatingBar.style.display = 'flex';
  } else {
    if (bulkBar) bulkBar.style.display = 'none';
    if (floatingBar) floatingBar.style.display = 'none';
  }
}

function clearAllSelection() {
  const selectAll = document.getElementById('selectAllCheckbox');
  if (selectAll) {
    selectAll.checked = false;
    selectAll.indeterminate = false;
  }
  const checkboxes = document.querySelectorAll('.donation-checkbox');
  checkboxes.forEach(cb => {
    cb.checked = false;
    const row = document.getElementById('row_donation_' + cb.value);
    if (row) row.classList.remove('selected-row');
  });
  updateSelectedCount();
}

function confirmBulkAction(actionType) {
  const checked = document.querySelectorAll('.donation-checkbox:checked');
  if (checked.length === 0) {
    alert(currentAdminLang === 'bn' ? 'অনুগ্রহ করে অন্তত একটি অনুদান নির্বাচন করুন।' : 'Please select at least one donation.');
    return;
  }

  const ids = Array.from(checked).map(cb => cb.value);
  const countFormatted = toAdminLangNum(ids.length);

  let confirmMsg = '';
  if (actionType === 'approve') {
    confirmMsg = currentAdminLang === 'bn'
      ? `আপনি কি নিশ্চিত যে নির্বাচিত ${countFormatted}টি অনুদান একসাথেই অনুমোদন করতে চান?`
      : `Are you sure you want to approve the selected ${countFormatted} donations?`;
  } else if (actionType === 'delete') {
    confirmMsg = currentAdminLang === 'bn'
      ? `সতর্কতা: আপনি কি নিশ্চিত যে নির্বাচিত ${countFormatted}টি অনুদান রেকর্ড স্থায়ীভাবে মুছে ফেলতে চান? এটি আর ফিরিয়ে আনা যাবে না।`
      : `Warning: Are you sure you want to permanently delete the selected ${countFormatted} donation records? This action cannot be undone.`;
  }

  if (confirm(confirmMsg)) {
    document.getElementById('bulkActionType').value = 'bulk_' + actionType;
    document.getElementById('bulkSelectedIds').value = ids.join(',');
    document.getElementById('bulkActionForm').submit();
  }
}

function openManualModal() {
  document.getElementById('manualModalBackdrop').style.display = 'flex';
}
function closeManualModal() {
  document.getElementById('manualModalBackdrop').style.display = 'none';
}

function openEditModal(d) {
  document.getElementById('editModalId').value = d.id;
  document.getElementById('editModalStatus').value = d.status;
  document.getElementById('editModalNotes').value = d.admin_notes || '';
  document.getElementById('editModalBackdrop').style.display = 'flex';
}
function openImportDonationsCsvModal() {
  document.getElementById('importDonationsCsvModal').style.display = 'flex';
}
function closeImportDonationsCsvModal() {
  document.getElementById('importDonationsCsvModal').style.display = 'none';
}
</script>

<!-- Modal: Import Donations CSV -->
<div id="importDonationsCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 520px; box-shadow: var(--shadow-lg); animation: fadeInDown 0.2s ease-out;">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></span>
      </h3>
      <button type="button" onclick="closeImportDonationsCsvModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <form method="POST" action="donations.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?php echo __('CSV ফাইল নির্বাচন', 'Select CSV File'); ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required class="input-clean" style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?php echo __('সাপোর্টেড কলাম বিন্যাস:', 'Supported Column Format:'); ?></strong><br>
        <code>ID, Reference_Code, Donor_Name, Email, Phone, Amount, Currency, Amount_BDT, Payment_Method, Transaction_ID, Frequency, Status, Is_Anonymous, Admin_Notes, Donor_Note, Verified_By, Verified_At, Created_At</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportDonationsCsvModal()" style="font-weight: 600;">
          <?php echo __('বাতিল', 'Cancel'); ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?php echo __('ইমপোর্ট ও সিঙ্ক করুন', 'Import & Sync'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
