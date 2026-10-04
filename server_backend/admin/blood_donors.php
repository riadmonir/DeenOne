<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ISLAMIC BLOOD DONOR NETWORK (রক্তদান নেটওয়ার্ক)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// -----------------------------------------------------------------------------
// 1. CSV EXPORT HANDLER
// -----------------------------------------------------------------------------
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $expGroup = trim($_GET['group'] ?? '');
    $expDistrict = trim($_GET['district'] ?? '');

    $whereExp = [];
    $paramsExp = [];
    if (!empty($expGroup)) {
        $whereExp[] = "blood_group = ?";
        $paramsExp[] = $expGroup;
    }
    if (!empty($expDistrict)) {
        $whereExp[] = "district LIKE ?";
        $paramsExp[] = "%$expDistrict%";
    }
    $wSql = !empty($whereExp) ? "WHERE " . implode(" AND ", $whereExp) : "";

    $stmt = $pdo->prepare("SELECT id, name, blood_group, district, upazila, address, phone_number, last_donation_date, total_donations, society, is_available, created_at FROM blood_donors $wSql ORDER BY id DESC");
    $stmt->execute($paramsExp);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: text/csv; charset=UTF-8');
    header('Content-Disposition: attachment; filename="deenone_blood_donors_' . date('Y-m-d_His') . '.csv"');
    header('Pragma: no-cache');
    header('Expires: 0');

    $out = fopen('php://output', 'w');
    fprintf($out, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($out, [
        'ID', 'Name', 'Blood_Group', 'District', 'Upazila',
        'Address', 'Phone_Number', 'Last_Donation_Date', 'Total_Donations',
        'Society', 'Is_Available', 'Created_At'
    ]);

    foreach ($rows as $r) {
        fputcsv($out, [
            $r['id'] ?? '',
            $r['name'] ?? '',
            $r['blood_group'] ?? 'O+',
            $r['district'] ?? '',
            $r['upazila'] ?? '',
            $r['address'] ?? '',
            $r['phone_number'] ?? '',
            $r['last_donation_date'] ?? '',
            $r['total_donations'] ?? 0,
            $r['society'] ?? '',
            $r['is_available'] ?? 1,
            $r['created_at'] ?? ''
        ]);
    }
    fclose($out);
    exit();
}

// -----------------------------------------------------------------------------
// 2. CSV IMPORT HANDLER
// -----------------------------------------------------------------------------
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'import_csv') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('blood_donors.php');
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

            $id = (int)($row[0] ?? 0);
            $name = trim($row[1] ?? '');
            $group = strtoupper(trim($row[2] ?? 'O+'));
            $district = trim($row[3] ?? 'ঢাকা');
            $upazila = trim($row[4] ?? '');
            $address = trim($row[5] ?? '');
            $phone = trim($row[6] ?? '');
            $lastDonation = !empty($row[7]) ? trim($row[7]) : null;
            $totalDonations = max(0, (int)($row[8] ?? 0));
            $society = trim($row[9] ?? '');
            $avail = isset($row[10]) ? (int)$row[10] : 1;

            if (empty($name) || empty($phone)) continue;

            // Check if donor exists by phone or ID
            $checkStmt = $pdo->prepare("SELECT id FROM blood_donors WHERE phone_number = ? OR (id > 0 AND id = ?)");
            $checkStmt->execute([$phone, $id]);
            $existing = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($existing) {
                $uStmt = $pdo->prepare("UPDATE blood_donors SET 
                    name = ?, blood_group = ?, district = ?, upazila = ?, address = ?, 
                    phone_number = ?, last_donation_date = ?, total_donations = ?, society = ?, is_available = ? 
                    WHERE id = ?");
                $uStmt->execute([
                    $name, $group, $district, $upazila, $address,
                    $phone, $lastDonation, $totalDonations, $society, $avail, $existing['id']
                ]);
                $updatedCount++;
            } else {
                $iStmt = $pdo->prepare("INSERT INTO blood_donors 
                    (name, blood_group, district, upazila, address, phone_number, last_donation_date, total_donations, society, is_available, created_at) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
                $iStmt->execute([
                    $name, $group, $district, $upazila, $address,
                    $phone, $lastDonation, $totalDonations, $society, $avail
                ]);
                $importedCount++;
            }
        }
        fclose($file);

        $msgBn = "রক্তদাতা CSV ইমপোর্ট সম্পন্ন: " . toLangNum($importedCount) . " টি নতুন যুক্ত হয়েছে এবং " . toLangNum($updatedCount) . " টি আপডেট হয়েছে।";
        $msgEn = "Blood Donors CSV Import Completed: " . toLangNum($importedCount) . " new added, " . toLangNum($updatedCount) . " updated.";
        logAdminAction($pdo, 'IMPORT_CSV_BLOOD_DONORS', 'blood_donors', '0', "CSV Import: $importedCount inserted, $updatedCount updated");
        setFlash('success', __($msgBn, $msgEn));
    } else {
        setFlash('danger', __('অনুগ্রহ করে একটি সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
    }
    redirect('blood_donors.php');
}

// Handle POST (Add Donor, Edit Donor, or Add Request)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('blood_donors.php');
    }

    if ($action === 'create_donor' || $action === 'update_donor') {
        $name = trim($_POST['name'] ?? '');
        $group = trim($_POST['blood_group'] ?? 'O+');
        $district = trim($_POST['district'] ?? 'ঢাকা');
        $upazila = trim($_POST['upazila'] ?? '');
        $address = trim($_POST['address'] ?? '');
        $phone = trim($_POST['phone_number'] ?? '');
        $lastDonation = !empty($_POST['last_donation_date']) ? $_POST['last_donation_date'] : null;
        $totalDonations = max(0, (int)($_POST['total_donations'] ?? 0));
        $society = trim($_POST['society'] ?? '');
        $avail = isset($_POST['is_available']) ? 1 : 0;

        if (empty($name) || empty($phone)) {
            setFlash('danger', __('রক্তদাতার নাম ও ফোন নম্বর উভয়ই আবশ্যক।', 'Both donor name and phone number are required.'));
        } else {
            try {
                if ($action === 'create_donor') {
                    $stmt = $pdo->prepare("INSERT INTO blood_donors (name, blood_group, district, upazila, address, phone_number, last_donation_date, total_donations, society, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$name, $group, $district, $upazila, $address, $phone, $lastDonation, $totalDonations, $society, $avail]);
                    logAdminAction($pdo, 'CREATE_DONOR', 'blood_donors', (string)$pdo->lastInsertId(), "রক্তদাতা যুক্ত: $name ($group)");
                    setFlash('success', __('নতুন রক্তদাতা সফলভাবে তালিকাভুক্ত হয়েছেন!', 'New blood donor listed successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE blood_donors SET name = ?, blood_group = ?, district = ?, upazila = ?, address = ?, phone_number = ?, last_donation_date = ?, total_donations = ?, society = ?, is_available = ? WHERE id = ?");
                    $stmt->execute([$name, $group, $district, $upazila, $address, $phone, $lastDonation, $totalDonations, $society, $avail, $id]);
                    logAdminAction($pdo, 'UPDATE_DONOR', 'blood_donors', (string)$id, "রক্তদাতা আপডেট: $name ($group)");
                    setFlash('success', __('রক্তদাতার তথ্য সফলভাবে আপডেট করা হয়েছে!', 'Donor information updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('সংরক্ষণ ব্যর্থ: ', 'Save failed: ') . $e->getMessage());
            }
        }
    }
    redirect('blood_donors.php');
}

// GET Actions
if (isset($_GET['delete_donor'])) {
    $id = (int)$_GET['delete_donor'];
    try {
        $pdo->prepare("DELETE FROM blood_donors WHERE id = ?")->execute([$id]);
        logAdminAction($pdo, 'DELETE_DONOR', 'blood_donors', (string)$id, 'রক্তদাতা মুছে ফেলা হয়েছে');
        setFlash('success', __('রক্তদাতা সফলভাবে মুছে ফেলা হয়েছে।', 'Blood donor deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
    }
    redirect('blood_donors.php');
}

if (isset($_GET['toggle_donor'])) {
    $id = (int)$_GET['toggle_donor'];
    try {
        $pdo->prepare("UPDATE blood_donors SET is_available = IF(is_available=1, 0, 1) WHERE id = ?")->execute([$id]);
        setFlash('success', __('রক্তদাতার সহজলভ্যতা স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Donor availability status updated.'));
    } catch (Exception $e) {}
    redirect('blood_donors.php');
}

if (isset($_GET['update_req_status'])) {
    $id = (int)$_GET['update_req_status'];
    $status = $_GET['status'] ?? 'FULFILLED';
    try {
        $pdo->prepare("UPDATE blood_requests SET status = ? WHERE id = ?")->execute([$status, $id]);
        setFlash('success', __('রক্তের আবেদনের অবস্থা আপডেট করা হয়েছে।', 'Blood request status updated.'));
    } catch (Exception $e) {}
    redirect('blood_donors.php?tab=requests');
}

$tab = $_GET['tab'] ?? 'donors';
$selectedGroup = $_GET['group'] ?? '';
$selectedDistrict = trim($_GET['district'] ?? '');

// Filter Donors
$dWhere = [];
$dParams = [];
if (!empty($selectedGroup)) {
    $dWhere[] = "blood_group = ?";
    $dParams[] = $selectedGroup;
}
if (!empty($selectedDistrict)) {
    $dWhere[] = "district LIKE ?";
    $dParams[] = "%$selectedDistrict%";
}
$dWhereSql = !empty($dWhere) ? "WHERE " . implode(" AND ", $dWhere) : "";

$donors = [];
try {
    $dSql = "SELECT * FROM blood_donors $dWhereSql ORDER BY id DESC";
    $dStmt = $pdo->prepare($dSql);
    $dStmt->execute($dParams);
    $donors = $dStmt->fetchAll();
} catch (Exception $e) {}

// Fetch Requests
$requests = [];
try {
    $requests = $pdo->query("SELECT * FROM blood_requests ORDER BY id DESC LIMIT 50")->fetchAll();
} catch (Exception $e) {}

// Fetch Call Requests
$callRequests = [];
try {
    $callRequests = $pdo->query("SELECT * FROM blood_call_requests ORDER BY id DESC LIMIT 100")->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('রক্তদাতা তালিকা', 'Blood Donors');
$activeNav = 'blood_donors';
require_once __DIR__ . '/header.php';
?>

<!-- Tab Controls & Master Actions -->
<div style="display: flex; gap: 8px; margin-bottom: 20px; align-items: center; flex-wrap: wrap;">
  <a href="blood_donors.php?tab=donors" class="btn <?= $tab === 'donors' ? 'btn-primary' : 'btn-secondary' ?> btn-sm" style="font-weight: 700;">
    🩸 <?= __('রক্তদাতা', 'Donors') ?> (<?= toLangNum(count($donors)) ?>)
  </a>
  <a href="blood_donors.php?tab=requests" class="btn <?= $tab === 'requests' ? 'btn-primary' : 'btn-secondary' ?> btn-sm" style="font-weight: 700;">
    🚨 <?= __('রক্তের আবেদন', 'Requests') ?> (<?= toLangNum(count($requests)) ?>)
  </a>
  <a href="blood_donors.php?tab=calls" class="btn <?= $tab === 'calls' ? 'btn-primary' : 'btn-secondary' ?> btn-sm" style="font-weight: 700;">
    📞 <?= __('কল রিকোয়েস্ট', 'Calls') ?> (<?= toLangNum(count($callRequests)) ?>)
  </a>

  <div style="margin-left: auto; display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
    <!-- CSV Export Button -->
    <a href="blood_donors.php?action=export_csv<?= !empty($selectedGroup) ? '&group=' . urlencode($selectedGroup) : '' ?><?= !empty($selectedDistrict) ? '&district=' . urlencode($selectedDistrict) : '' ?>" class="btn btn-secondary btn-sm" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?= __('রক্তদাতা তালিকা CSV ফরম্যাটে ডাউনলোড করুন', 'Download Blood Donors in CSV format') ?>">
      <i class="fa-solid fa-file-export" style="color: #10b981;"></i>
      <span><?= __('CSV এক্সপোর্ট', 'Export CSV') ?></span>
    </a>

    <!-- CSV Import Button -->
    <button type="button" class="btn btn-secondary btn-sm" onclick="openImportDonorsCsvModal()" style="font-weight: 700; display: inline-flex; align-items: center; gap: 6px;" title="<?= __('CSV ফাইল থেকে রক্তদাতা ডাটাবেজে ইমপোর্ট করুন', 'Import blood donors from CSV') ?>">
      <i class="fa-solid fa-file-import" style="color: #3b82f6;"></i>
      <span><?= __('CSV ইমপোর্ট', 'Import CSV') ?></span>
    </button>

    <button type="button" class="btn btn-primary btn-sm" onclick="openAddDonorModal()" style="font-weight: 700;">
      <i class="fa-solid fa-plus"></i> <?= __('নতুন রক্তদাতা', 'New Donor') ?>
    </button>
  </div>
</div>

<?php if ($tab === 'donors'): ?>
  <!-- Donors Filter Toolbar -->
  <div class="card" style="margin-bottom: 20px;">
    <div class="card-body" style="padding: 16px 20px;">
      <form method="GET" action="blood_donors.php" style="display: flex; gap: 14px; align-items: center; flex-wrap: wrap;">
        <input type="hidden" name="tab" value="donors">
        <div style="width: 200px;">
          <select name="group" class="form-control" onchange="this.form.submit()">
            <option value=""><?= __('সকল রক্তের গ্রুপ', 'All Blood Groups') ?></option>
            <?php foreach (['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'] as $grp): ?>
              <option value="<?= $grp ?>" <?= $selectedGroup === $grp ? 'selected' : '' ?>><?= $grp ?></option>
            <?php endforeach; ?>
          </select>
        </div>
        <div style="width: 220px;">
          <input type="text" name="district" class="form-control" placeholder="<?= __('জেলা দিয়ে খুঁজুন...', 'Filter by district...') ?>" value="<?= htmlspecialchars($selectedDistrict) ?>">
        </div>
        <button type="submit" class="btn btn-secondary">🔍 <?= __('ফিল্টার', 'Filter') ?></button>
        <?php if (!empty($selectedGroup) || !empty($selectedDistrict)): ?>
          <a href="blood_donors.php" class="btn btn-secondary"><?= __('রিসেট', 'Reset') ?></a>
        <?php endif; ?>
      </form>
    </div>
  </div>

  <!-- Donors Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">🩸 <?= __('রক্তদাতাদের তালিকা (মোট: ', 'Blood Donors List (Total: ') . toLangNum(count($donors)) . __(' জন)', ')') ?></div>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 50px;"><?= __('আইডি', 'ID') ?></th>
            <th><?= __('রক্তের গ্রুপ', 'Blood Group') ?></th>
            <th><?= __('রক্তদাতার নাম', 'Donor Name') ?></th>
            <th><?= __('মোবাইল নম্বর', 'Phone Number') ?></th>
            <th><?= __('জেলা ও উপজেলা', 'District & Upazila') ?></th>
            <th><?= __('সহজলভ্যতা', 'Availability') ?></th>
            <th><?= __('নিবন্ধনের তারিখ', 'Registration Date') ?></th>
            <th style="text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($donors)): ?>
            <tr><td colspan="8" style="text-align: center; color: var(--text-dim);"><?= __('কোনো রক্তদাতার সন্ধান পাওয়া যায়নি।', 'No blood donors found.') ?></td></tr>
          <?php else: ?>
            <?php foreach ($donors as $d): ?>
              <tr>
                <td>#<?= toLangNum($d['id']) ?></td>
                <td>
                  <span class="badge badge-danger" style="font-size: 14px; font-weight: 800; padding: 4px 10px;">
                    <?= htmlspecialchars($d['blood_group']) ?>
                  </span>
                </td>
                <td>
                  <strong><?= htmlspecialchars($d['name']) ?></strong>
                  <?php if (!empty($d['society'])): ?>
                    <div style="margin-top: 2px;"><span class="badge badge-info" style="font-size: 11px;">🏢 <?= htmlspecialchars($d['society']) ?></span></div>
                  <?php endif; ?>
                </td>
                <td>
                  <a href="tel:<?= htmlspecialchars($d['phone_number']) ?>" style="color: var(--primary-light); text-decoration: none; font-weight: 600;">
                    📞 <?= htmlspecialchars($d['phone_number']) ?>
                  </a>
                </td>
                <td>
                  <div><?= htmlspecialchars($d['district']) ?><?= !empty($d['upazila']) ? ', ' . htmlspecialchars($d['upazila']) : '' ?></div>
                  <?php if (!empty($d['address'])): ?>
                    <div style="font-size: 11px; color: var(--text-dim); margin-top: 2px;">📍 <?= htmlspecialchars($d['address']) ?></div>
                  <?php endif; ?>
                </td>
                <td>
                  <a href="blood_donors.php?toggle_donor=<?= $d['id'] ?>" class="badge <?= $d['is_available'] ? 'badge-success' : 'badge-secondary' ?>" style="text-decoration: none;">
                    <?= $d['is_available'] ? __('✓ রক্তদানে প্রস্তুত', '✓ Available') : __('✕ সাময়িক অনুপলব্ধ', '✕ Unavailable') ?>
                  </a>
                </td>
                <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                  <div><?= isEn() ? date('d M Y', strtotime($d['created_at'])) : toLangNum(date('d-m-Y', strtotime($d['created_at']))) ?></div>
                  <?php if (!empty($d['total_donations'])): ?>
                    <div style="font-size: 11px; color: var(--primary-light); margin-top: 2px;">🩸 <?= toLangNum((int)$d['total_donations']) ?> <?= __('বার দান', 'donations') ?></div>
                  <?php endif; ?>
                  <?php if (!empty($d['last_donation_date'])): ?>
                    <div style="font-size: 10px; color: var(--text-dim);"><?= __('সর্বশেষ: ', 'Last: ') . (isEn() ? date('d M Y', strtotime($d['last_donation_date'])) : toLangNum(date('d-m-Y', strtotime($d['last_donation_date'])))) ?></div>
                  <?php endif; ?>
                </td>
                <td style="text-align: right; white-space: nowrap;">
                  <button type="button" class="btn btn-secondary btn-sm" onclick='openEditDonorModal(<?= htmlspecialchars(json_encode($d), ENT_QUOTES, "UTF-8") ?>)'>
                    ✏️ <?= __('সম্পাদনা', 'Edit') ?>
                  </button>
                  <a href="blood_donors.php?delete_donor=<?= $d['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirm('<?= __('এই রক্তদাতার তথ্য মুছে ফেলতে চান?', 'Are you sure you want to delete this donor?') ?>');">
                    🗑️ <?= __('মুছুন', 'Delete') ?>
                  </a>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div>

<?php else: ?>
  <!-- Emergency Blood Requests Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">🚨 <?= __('জরুরি রক্তের আবেদনের তালিকা', 'Emergency Blood Requests List') ?></div>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th><?= __('আইডি', 'ID') ?></th>
            <th><?= __('রক্তের গ্রুপ ও ইউনিট', 'Blood Group & Units') ?></th>
            <th><?= __('রোগীর নাম ও হাসপাতাল', 'Patient & Hospital') ?></th>
            <th><?= __('জেলা বা এলাকা', 'District or Area') ?></th>
            <th><?= __('যোগাযোগের ফোন', 'Contact Phone') ?></th>
            <th><?= __('প্রয়োজনের তারিখ ও জরুরি অবস্থা', 'Needed Date & Urgency') ?></th>
            <th><?= __('স্ট্যাটাস', 'Status') ?></th>
            <th style="text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($requests)): ?>
            <tr><td colspan="8" style="text-align: center; color: var(--text-dim);"><?= __('বর্তমানে কোনো জরুরি রক্তের আবেদন নেই।', 'No emergency blood requests currently.') ?></td></tr>
          <?php else: ?>
            <?php foreach ($requests as $req): ?>
              <tr>
                <td>#<?= toLangNum($req['id']) ?></td>
                <td>
                  <span class="badge badge-danger" style="font-size: 13px; font-weight: 800;">
                    <?= htmlspecialchars($req['blood_group']) ?>
                  </span>
                  <div style="font-size: 12px; color: var(--text-muted); margin-top: 2px;">
                    <?= is_numeric($req['units_needed']) ? toLangNum($req['units_needed']) . ' ' . __('ব্যাগ', 'Bags') : htmlspecialchars($req['units_needed']) ?>
                  </div>
                </td>
                <td>
                  <strong><?= htmlspecialchars(!empty($req['patient_name']) ? $req['patient_name'] : ($req['problem_reason'] ?? 'জরুরি রোগী')) ?></strong>
                  <?php if (!empty($req['problem_reason'])): ?>
                    <span class="badge badge-info" style="font-size: 11px; margin-left: 4px;"><?= htmlspecialchars($req['problem_reason']) ?></span>
                  <?php endif; ?>
                  <div style="font-size: 12px; color: var(--text-muted);"><?= htmlspecialchars($req['hospital_name']) ?></div>
                  <?php if (!empty($req['notes'])): ?>
                    <div style="font-size: 11px; color: var(--text-dim); margin-top: 3px; font-style: italic;">📝 <?= htmlspecialchars($req['notes']) ?></div>
                  <?php endif; ?>
                </td>
                <td><?= htmlspecialchars($req['district']) ?><?= !empty($req['upazila']) ? ', ' . htmlspecialchars($req['upazila']) : '' ?></td>
                <td>
                  <a href="tel:<?= htmlspecialchars($req['contact_phone']) ?>" style="color: var(--primary-light); text-decoration: none;">
                    📞 <?= htmlspecialchars($req['contact_phone']) ?>
                  </a>
                </td>
                <td>
                  <div style="font-size: 12px; font-weight: 600;">
                    <?= isEn() ? date('d M Y', strtotime($req['needed_date'])) : toLangNum(date('d-m-Y', strtotime($req['needed_date']))) ?>
                    <?= !empty($req['donation_time']) ? '<span style="color: var(--text-dim);"> (' . htmlspecialchars($req['donation_time']) . ')</span>' : '' ?>
                  </div>
                  <span class="badge <?= ($req['urgency_level'] === 'EMERGENCY' || $req['urgency_level'] === 'CRITICAL') ? 'badge-danger' : 'badge-secondary' ?>">
                    <?= ($req['urgency_level'] === 'EMERGENCY' || $req['urgency_level'] === 'CRITICAL') ? __('জরুরি', 'EMERGENCY') : __('সাধারণ', 'NORMAL') ?>
                  </span>
                </td>
                <td>
                  <?php
                  $sClass = 'badge-warning';
                  $sLabel = __('অপেক্ষমাণ', 'OPEN');
                  if ($req['status'] === 'ACCEPTED') {
                      $sClass = 'badge-info';
                      $sLabel = __('গৃহীত', 'ACCEPTED');
                  } elseif ($req['status'] === 'FULFILLED') {
                      $sClass = 'badge-success';
                      $sLabel = __('সংগৃহীত', 'FULFILLED');
                  } elseif ($req['status'] === 'CANCELLED') {
                      $sClass = 'badge-secondary';
                      $sLabel = __('বাতিলকৃত', 'CANCELLED');
                  }
                  ?>
                  <span class="badge <?= $sClass ?>"><?= $sLabel ?></span>
                </td>
                <td style="text-align: right; white-space: nowrap;">
                  <?php if ($req['status'] === 'OPEN'): ?>
                    <a href="blood_donors.php?update_req_status=<?= $req['id'] ?>&status=ACCEPTED" class="btn btn-secondary btn-sm">
                      🤝 <?= __('গ্রহণ', 'Accept') ?>
                    </a>
                    <a href="blood_donors.php?update_req_status=<?= $req['id'] ?>&status=FULFILLED" class="btn btn-primary btn-sm">
                      ✓ <?= __('সংগৃহীত', 'Fulfilled') ?>
                    </a>
                    <a href="blood_donors.php?update_req_status=<?= $req['id'] ?>&status=CANCELLED" class="btn btn-danger btn-sm">
                      ✕ <?= __('বাতিল', 'Cancel') ?>
                    </a>
                  <?php elseif ($req['status'] === 'ACCEPTED'): ?>
                    <a href="blood_donors.php?update_req_status=<?= $req['id'] ?>&status=FULFILLED" class="btn btn-primary btn-sm">
                      ✓ <?= __('সংগৃহীত', 'Fulfilled') ?>
                    </a>
                    <a href="blood_donors.php?update_req_status=<?= $req['id'] ?>&status=CANCELLED" class="btn btn-secondary btn-sm">
                      ✕ <?= __('বাতিল', 'Cancel') ?>
                    </a>
                  <?php else: ?>
                    <span style="font-size: 12px; color: var(--text-dim);"><?= __('সম্পন্ন', 'Closed') ?></span>
                  <?php endif; ?>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div>
<?php endif; ?>

<?php if ($tab === 'calls'): ?>
  <!-- Call Requests Table -->
  <div class="card">
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th><?= __('রক্তদাতা', 'Donor') ?></th>
            <th><?= __('গ্রুপ', 'Group') ?></th>
            <th><?= __('আবেদনকারী / রোগী', 'Requester / Patient') ?></th>
            <th><?= __('যোগাযোগ নম্বর', 'Contact Phone') ?></th>
            <th><?= __('স্ট্যাটাস', 'Status') ?></th>
            <th><?= __('তারিখ ও সময়', 'Date & Time') ?></th>
            <th><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($callRequests)): ?>
            <tr>
              <td colspan="8" style="text-align: center; color: var(--text-dim); padding: 30px;">
                <?= __('কোনো কল রিকোয়েস্টের রেকর্ড পাওয়া যায়নি।', 'No call requests recorded yet.') ?>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($callRequests as $call): ?>
              <tr>
                <td>#<?= $call['id'] ?></td>
                <td>
                  <strong><?= htmlspecialchars($call['donor_name'] ?: 'ডোনার #' . $call['donor_id']) ?></strong><br>
                  <small style="color: var(--text-dim);"><?= htmlspecialchars($call['donor_phone'] ?: '-') ?></small>
                </td>
                <td>
                  <span class="badge" style="background: rgba(239, 68, 68, 0.15); color: #ef4444; font-weight: bold;">
                    <?= htmlspecialchars($call['blood_group'] ?: '-') ?>
                  </span>
                </td>
                <td>
                  <strong><?= htmlspecialchars($call['requester_name'] ?: 'রোগীর স্বজন') ?></strong><br>
                  <small style="color: var(--text-dim);"><?= htmlspecialchars($call['requester_user_id'] ?: '-') ?></small>
                </td>
                <td>
                  <?php if (!empty($call['requester_phone'])): ?>
                    <a href="tel:<?= htmlspecialchars($call['requester_phone']) ?>" style="color: var(--accent-mint); font-weight: bold;">
                      📞 <?= htmlspecialchars($call['requester_phone']) ?>
                    </a>
                  <?php else: ?>
                    <span style="color: var(--text-dim);">-</span>
                  <?php endif; ?>
                </td>
                <td>
                  <span class="badge" style="background: rgba(16, 185, 129, 0.15); color: #10b981;">
                    <?= htmlspecialchars($call['status'] ?: 'SENT') ?>
                  </span>
                </td>
                <td style="font-size: 13px; color: var(--text-dim);">
                  <?= date('d M Y, h:i A', strtotime($call['created_at'])) ?>
                </td>
                <td>
                  <?php if (!empty($call['requester_phone'])): ?>
                    <a href="tel:<?= htmlspecialchars($call['requester_phone']) ?>" class="btn btn-primary btn-sm">
                      📞 <?= __('কল করুন', 'Call') ?>
                    </a>
                  <?php endif; ?>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div>
<?php endif; ?>

<!-- Add/Edit Donor Modal -->
<div class="modal-backdrop" id="donorModal">
  <div class="modal-window">
    <div class="modal-header">
      <div class="modal-title" id="donorModalTitle"><?= __('নতুন রক্তদাতা তালিকাভুক্ত করুন', 'Add New Blood Donor') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('donorModal')">&times;</button>
    </div>
    <form method="POST" action="blood_donors.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" id="donorFormAction" value="create_donor">
      <input type="hidden" name="id" id="donorId" value="">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('রক্তদাতার পূর্ণ নাম', 'Donor Full Name') ?> *</label>
            <input type="text" name="name" id="inpDonorName" class="form-control" placeholder="<?= __('যেমন: মুহাম্মদ হাসান', 'e.g. Muhammad Hasan') ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('রক্তের গ্রুপ', 'Blood Group') ?> *</label>
            <select name="blood_group" id="inpDonorGroup" class="form-control">
              <?php foreach (['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'] as $grp): ?>
                <option value="<?= $grp ?>"><?= $grp ?></option>
              <?php endforeach; ?>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('মোবাইল নম্বর', 'Phone Number') ?> *</label>
            <input type="tel" name="phone_number" id="inpDonorPhone" class="form-control" placeholder="017XXXXXXXX" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('জেলা', 'District') ?> *</label>
            <input type="text" name="district" id="inpDonorDistrict" class="form-control" placeholder="<?= __('যেমন: ঢাকা', 'e.g. Dhaka') ?>" required>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('উপজেলা বা এলাকা', 'Upazila or Area') ?></label>
            <input type="text" name="upazila" id="inpDonorUpazila" class="form-control" placeholder="<?= __('যেমন: মিরপুর / ধানমন্ডি', 'e.g. Mirpur / Dhanmondi') ?>">
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('বর্তমান ঠিকানা / শিক্ষা প্রতিষ্ঠান', 'Current Address / Institution') ?></label>
            <input type="text" name="address" id="inpDonorAddress" class="form-control" placeholder="<?= __('যেমন: উত্তরা, ঢাকা বা ঢাকা বিশ্ববিদ্যালয়', 'e.g. Uttara, Dhaka or Dhaka University') ?>">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('সর্বশেষ রক্তদানের তারিখ (ঐচ্ছিক)', 'Last Donation Date (Optional)') ?></label>
            <input type="date" name="last_donation_date" id="inpDonorLastDate" class="form-control">
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('মোট রক্তদানের সংখ্যা', 'Total Blood Donations') ?></label>
            <input type="number" name="total_donations" id="inpDonorTotalDonations" class="form-control" placeholder="0" min="0">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('রক্তদাতা সোসাইটি / কমিউনিটি (ঐচ্ছিক)', 'Blood Donor Society / Community (Optional)') ?></label>
          <input type="text" name="society" id="inpDonorSociety" class="form-control" placeholder="<?= __('যেমন: সন্ধানী / বাঁধন / কোনো সোসাইটি নয়', 'e.g. Sandhani / Badhan / None') ?>">
        </div>

        <div class="form-group">
          <label class="switch-label">
            <label class="switch">
              <input type="checkbox" name="is_available" id="inpDonorAvail" value="1" checked>
              <span class="slider"></span>
            </label>
            <span style="font-weight: 600;"><?= __('রক্তদাতা বর্তমানে রক্তদানে প্রস্তুত ও সক্রিয়', 'Donor is currently available and ready to donate') ?></span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('donorModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary" id="btnDonorSubmit">💾 <?= __('রক্তদাতা সংরক্ষণ করুন', 'Save Blood Donor') ?></button>
      </div>
    </form>
  </div>
</div>

<script>
const donorLang = {
  addTitle: <?= json_encode(__('➕ নতুন রক্তদাতা তালিকাভুক্ত করুন', '➕ Add New Blood Donor')) ?>,
  editTitle: <?= json_encode(__('✏️ রক্তদাতার তথ্য সম্পাদনা', '✏️ Edit Blood Donor Information')) ?>,
  btnSave: <?= json_encode(__('💾 রক্তদাতা সংরক্ষণ করুন', '💾 Save Blood Donor')) ?>,
  btnUpdate: <?= json_encode(__('💾 পরিবর্তন সংরক্ষণ করুন', '💾 Update Blood Donor')) ?>
};

function openAddDonorModal() {
  document.getElementById('donorModalTitle').innerText = donorLang.addTitle;
  document.getElementById('donorFormAction').value = 'create_donor';
  document.getElementById('donorId').value = '';
  document.getElementById('inpDonorName').value = '';
  document.getElementById('inpDonorGroup').value = 'O+';
  document.getElementById('inpDonorPhone').value = '';
  document.getElementById('inpDonorDistrict').value = 'ঢাকা';
  document.getElementById('inpDonorUpazila').value = '';
  document.getElementById('inpDonorAddress').value = '';
  document.getElementById('inpDonorLastDate').value = '';
  document.getElementById('inpDonorTotalDonations').value = '0';
  document.getElementById('inpDonorSociety').value = '';
  document.getElementById('inpDonorAvail').checked = true;
  document.getElementById('btnDonorSubmit').innerText = donorLang.btnSave;
  openModal('donorModal');
}

function openEditDonorModal(d) {
  document.getElementById('donorModalTitle').innerText = donorLang.editTitle;
  document.getElementById('donorFormAction').value = 'update_donor';
  document.getElementById('donorId').value = d.id;
  document.getElementById('inpDonorName').value = d.name || '';
  document.getElementById('inpDonorGroup').value = d.blood_group || 'O+';
  document.getElementById('inpDonorPhone').value = d.phone_number || '';
  document.getElementById('inpDonorDistrict').value = d.district || '';
  document.getElementById('inpDonorUpazila').value = d.upazila || '';
  document.getElementById('inpDonorAddress').value = d.address || '';
  document.getElementById('inpDonorLastDate').value = d.last_donation_date || '';
  document.getElementById('inpDonorTotalDonations').value = d.total_donations || '0';
  document.getElementById('inpDonorSociety').value = d.society || '';
  document.getElementById('inpDonorAvail').checked = (d.is_available == 1);
  document.getElementById('btnDonorSubmit').innerText = donorLang.btnUpdate;
  openModal('donorModal');
}

function openImportDonorsCsvModal() {
  document.getElementById('importDonorsCsvModal').style.display = 'flex';
}
function closeImportDonorsCsvModal() {
  document.getElementById('importDonorsCsvModal').style.display = 'none';
}
</script>

<!-- Modal: Import Blood Donors CSV -->
<div id="importDonorsCsvModal" style="display: none; position: fixed; inset: 0; background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px); z-index: 1000; align-items: center; justify-content: center;">
  <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 28px; width: 100%; max-width: 520px; box-shadow: var(--shadow-lg);">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <h3 style="font-size: 18px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-file-csv" style="color: #3b82f6;"></i>
        <span><?= __('রক্তদাতা CSV ডাটাবেজ ইমপোর্ট', 'Import Blood Donors from CSV') ?></span>
      </h3>
      <button type="button" onclick="closeImportDonorsCsvModal()" style="background: none; border: none; font-size: 18px; color: var(--text-muted); cursor: pointer;">✕</button>
    </div>

    <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 18px; line-height: 1.5;">
      <?= __('একটি বৈধ CSV ফাইল আপলোড করে রক্তদাতাদের তালিকা এক ক্লিকে MySQL ডাটাবেজে যুক্ত বা আপডেট করুন।', 'Upload a valid CSV file to bulk import or update blood donors in MySQL database.') ?>
    </p>

    <form method="POST" action="blood_donors.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="import_csv">

      <div style="margin-bottom: 18px;">
        <label style="display: block; font-size: 13px; font-weight: 700; color: var(--text-heading); margin-bottom: 8px;">
          <?= __('CSV ফাইল নির্বাচন করুন (.csv)', 'Select CSV File (.csv)') ?>
        </label>
        <input type="file" name="csv_file" accept=".csv" required style="width: 100%; padding: 10px; background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: 8px; color: var(--text-main);">
      </div>

      <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; color: var(--text-main);">
        <strong style="color: #2563eb;"><?= __('সাপোর্টেড কলাম বিন্যাস:', 'Supported Column Format:') ?></strong><br>
        <code>ID, Name, Blood_Group, District, Upazila, Address, Phone_Number, Last_Donation_Date, Total_Donations, Society, Is_Available</code>
      </div>

      <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px;">
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeImportDonorsCsvModal()" style="font-weight: 600;">
          <?= __('বাতিল', 'Cancel') ?>
        </button>
        <button type="submit" class="btn btn-primary btn-sm" style="font-weight: 700; padding: 8px 18px;">
          <i class="fa-solid fa-cloud-arrow-up"></i> <?= __('ইমপোর্ট ও সিঙ্ক করুন', 'Import & Sync') ?>
        </button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
