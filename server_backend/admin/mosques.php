<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - MOSQUES & HALAL PLACES DIRECTORY (মসজিদ ও হালাল ডিরেক্টরি)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Handle Action (Add/Edit Mosque or Halal Place)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('mosques.php');
    }

    if ($action === 'create_mosque' || $action === 'update_mosque') {
        $name = trim($_POST['name'] ?? '');
        $district = trim($_POST['district'] ?? 'ঢাকা');
        $address = trim($_POST['address'] ?? '');
        $lat = !empty($_POST['latitude']) ? (float)$_POST['latitude'] : null;
        $lng = !empty($_POST['longitude']) ? (float)$_POST['longitude'] : null;
        $jummah = trim($_POST['jummah_time'] ?? '১:৩০ PM');
        $facilities = trim($_POST['facilities'] ?? '');
        $verified = isset($_POST['is_verified']) ? 1 : 0;

        if (empty($name) || empty($address)) {
            setFlash('danger', __('মসজিদের নাম ও ঠিকানা উভয়ই প্রদান করতে হবে।', 'Both mosque name and address are required.'));
        } else {
            try {
                if ($action === 'create_mosque') {
                    $stmt = $pdo->prepare("INSERT INTO mosques (name, district, address, latitude, longitude, jummah_time, facilities, is_verified) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$name, $district, $address, $lat, $lng, $jummah, $facilities, $verified]);
                    logAdminAction($pdo, 'CREATE_MOSQUE', 'mosques', (string)$pdo->lastInsertId(), "মসজিদ যুক্ত: $name");
                    setFlash('success', __('নতুন মসজিদ সফলভাবে যুক্ত হয়েছে!', 'New mosque added successfully!'));
                } else {
                    $id = (int)$_POST['id'];
                    $stmt = $pdo->prepare("UPDATE mosques SET name = ?, district = ?, address = ?, latitude = ?, longitude = ?, jummah_time = ?, facilities = ?, is_verified = ? WHERE id = ?");
                    $stmt->execute([$name, $district, $address, $lat, $lng, $jummah, $facilities, $verified, $id]);
                    logAdminAction($pdo, 'UPDATE_MOSQUE', 'mosques', (string)$id, "মসজিদ আপডেট: $name");
                    setFlash('success', __('মসজিদ তথ্য আপডেট করা হয়েছে!', 'Mosque information updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('অপারেশন ব্যর্থ: ', 'Operation failed: ') . $e->getMessage());
            }
        }
    }

    if ($action === 'create_halal') {
        $name = trim($_POST['name'] ?? '');
        $cat = trim($_POST['category'] ?? 'Restaurant');
        $district = trim($_POST['district'] ?? 'ঢাকা');
        $address = trim($_POST['address'] ?? '');
        $phone = trim($_POST['phone'] ?? '');
        $halalStatus = trim($_POST['halal_status'] ?? '100% Halal Certified');
        $verified = isset($_POST['is_verified']) ? 1 : 0;

        if (!empty($name)) {
            try {
                $stmt = $pdo->prepare("INSERT INTO halal_places (name, category, district, address, phone, halal_status, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?)");
                $stmt->execute([$name, $cat, $district, $address, $phone, $halalStatus, $verified]);
                logAdminAction($pdo, 'CREATE_HALAL_PLACE', 'halal_places', (string)$pdo->lastInsertId(), "হালাল স্থান যুক্ত: $name");
                setFlash('success', __('নতুন হালাল স্থান সফলভাবে তালিকাভুক্ত হয়েছে!', 'New halal establishment listed successfully!'));
            } catch (Exception $e) {
                setFlash('danger', __('সংরক্ষণ ব্যর্থ: ', 'Save failed: ') . $e->getMessage());
            }
        }
    }

    $retTab = ($action === 'create_halal') ? '?tab=halal' : '';
    redirect('mosques.php' . $retTab);
}

// GET Delete & Toggle Actions
if (isset($_GET['delete_mosque'])) {
    $id = (int)$_GET['delete_mosque'];
    try {
        $pdo->prepare("DELETE FROM mosques WHERE id = ?")->execute([$id]);
        logAdminAction($pdo, 'DELETE_MOSQUE', 'mosques', (string)$id, 'মসজিদ মুছে ফেলা হয়েছে');
        setFlash('success', __('মসজিদ সফলভাবে মুছে ফেলা হয়েছে।', 'Mosque deleted successfully.'));
    } catch (Exception $e) {}
    redirect('mosques.php');
}

if (isset($_GET['toggle_mosque'])) {
    $id = (int)$_GET['toggle_mosque'];
    try {
        $pdo->prepare("UPDATE mosques SET is_verified = IF(is_verified=1, 0, 1) WHERE id = ?")->execute([$id]);
        setFlash('success', __('মসজিদের ভেরিফিকেশন স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Mosque verification status updated.'));
    } catch (Exception $e) {}
    redirect('mosques.php');
}

if (isset($_GET['delete_halal'])) {
    $id = (int)$_GET['delete_halal'];
    try {
        $pdo->prepare("DELETE FROM halal_places WHERE id = ?")->execute([$id]);
        logAdminAction($pdo, 'DELETE_HALAL_PLACE', 'halal_places', (string)$id, 'হালাল স্থান মুছে ফেলা হয়েছে');
        setFlash('success', __('হালাল প্রতিষ্ঠান সফলভাবে মুছে ফেলা হয়েছে।', 'Halal establishment deleted successfully.'));
    } catch (Exception $e) {}
    redirect('mosques.php?tab=halal');
}

if (isset($_GET['toggle_halal'])) {
    $id = (int)$_GET['toggle_halal'];
    try {
        $pdo->prepare("UPDATE halal_places SET is_verified = IF(is_verified=1, 0, 1) WHERE id = ?")->execute([$id]);
        setFlash('success', __('হালাল প্রতিষ্ঠানের ভেরিফিকেশন স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Halal establishment verification status updated.'));
    } catch (Exception $e) {}
    redirect('mosques.php?tab=halal');
}

$tab = $_GET['tab'] ?? 'mosques';

// Fetch Mosques
$mosques = [];
try {
    $mosques = $pdo->query("SELECT * FROM mosques ORDER BY id DESC")->fetchAll();
} catch (Exception $e) {}

// Fetch Halal Places
$halalPlaces = [];
try {
    $halalPlaces = $pdo->query("SELECT * FROM halal_places ORDER BY id DESC")->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('মসজিদ ডিরেক্টরি', 'Mosques Directory');
$activeNav = 'mosques';
require_once __DIR__ . '/header.php';
?>

<!-- Tab Navigation -->
<div style="display: flex; gap: 10px; margin-bottom: 20px; align-items: center; flex-wrap: wrap;">
  <a href="mosques.php?tab=mosques" class="btn <?= $tab === 'mosques' ? 'btn-primary' : 'btn-secondary' ?>">
    <i class="fa-solid fa-mosque" style="margin-right: 6px;"></i><?= __('মসজিদ', 'Mosques') ?> (<?= toLangNum(count($mosques)) ?>)
  </a>
  <a href="mosques.php?tab=halal" class="btn <?= $tab === 'halal' ? 'btn-primary' : 'btn-secondary' ?>">
    <i class="fa-solid fa-utensils" style="margin-right: 6px;"></i><?= __('হালাল স্থান', 'Halal Places') ?> (<?= toLangNum(count($halalPlaces)) ?>)
  </a>
  <?php if ($tab === 'mosques'): ?>
    <button type="button" class="btn btn-primary" style="margin-left: auto;" onclick="openAddMosqueModal()">
      <i class="fa-solid fa-plus-circle" style="margin-right: 4px;"></i><?= __('নতুন মসজিদ', 'New Mosque') ?>
    </button>
  <?php else: ?>
    <button type="button" class="btn btn-primary" style="margin-left: auto;" onclick="openModal('addHalalModal')">
      <i class="fa-solid fa-plus-circle" style="margin-right: 4px;"></i><?= __('নতুন স্থান', 'New Place') ?>
    </button>
  <?php endif; ?>
</div>

<?php if ($tab === 'mosques'): ?>
  <!-- Mosques Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title"><i class="fa-solid fa-mosque" style="color: var(--primary); margin-right: 6px;"></i><?= __('মসজিদ তালিকা', 'Mosque List') ?></div>
      <input type="text" id="searchMosque" class="form-control" placeholder="<?= __('অনুসন্ধান...', 'Search...') ?>" style="width: 220px; padding: 6px 12px;" onkeyup="filterTable('searchMosque', 'mosqueTable')">
    </div>
    <div class="table-responsive">
      <table class="table" id="mosqueTable">
        <thead>
          <tr>
            <th style="width: 50px;"><?= __('আইডি', 'ID') ?></th>
            <th><?= __('মসজিদ', 'Mosque') ?></th>
            <th><?= __('ঠিকানা', 'Address') ?></th>
            <th><?= __('জুম’আ', 'Jummah') ?></th>
            <th><?= __('সুযোগ-সুবিধা', 'Facilities') ?></th>
            <th><?= __('ভেরিফিকেশন', 'Verification') ?></th>
            <th style="text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($mosques)): ?>
            <tr><td colspan="7" style="text-align: center; color: var(--text-dim);"><?= __('কোনো মসজিদ পাওয়া যায়নি।', 'No mosques found.') ?></td></tr>
          <?php else: ?>
            <?php foreach ($mosques as $m): ?>
              <tr>
                <td>#<?= toLangNum($m['id']) ?></td>
                <td>
                  <strong><?= htmlspecialchars($m['name']) ?></strong>
                  <?php if (!empty($m['latitude']) && !empty($m['longitude'])): ?>
                    <div style="font-size: 11px; color: var(--accent-blue);">
                      📍 <?= $m['latitude'] ?>, <?= $m['longitude'] ?>
                    </div>
                  <?php endif; ?>
                </td>
                <td style="max-width: 260px; font-size: 13px;">
                  <span class="badge badge-info"><?= htmlspecialchars($m['district']) ?></span>
                  <div style="color: var(--text-muted); margin-top: 3px;"><?= htmlspecialchars($m['address']) ?></div>
                </td>
                <td style="white-space: nowrap; font-weight: 600; color: var(--primary-light);">
                  🕐 <?= htmlspecialchars($m['jummah_time']) ?>
                </td>
                <td style="max-width: 240px; font-size: 12px; color: var(--text-muted);">
                  <?= htmlspecialchars($m['facilities']) ?>
                </td>
                <td>
                  <a href="mosques.php?toggle_mosque=<?= $m['id'] ?>" class="badge <?= $m['is_verified'] ? 'badge-success' : 'badge-secondary' ?>" style="text-decoration: none;">
                    <?= $m['is_verified'] ? __('✓ যাচাইকৃত', '✓ Verified') : __('✕ অপেক্ষমাণ', '✕ Pending') ?>
                  </a>
                </td>
                <td style="text-align: right; white-space: nowrap;">
                  <button type="button" class="btn btn-secondary btn-sm" onclick="editMosque(<?= htmlspecialchars(json_encode($m)) ?>)">
                    ✏️ <?= __('সম্পাদনা', 'Edit') ?>
                  </button>
                  <a href="mosques.php?delete_mosque=<?= $m['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirm('<?= __('এই মসজিদটি মুছে ফেলতে চান?', 'Are you sure you want to delete this mosque?') ?>');">
                    <?= __('মুছুন', 'Delete') ?>
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
  <!-- Halal Places Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">🥗 <?= __('হালাল স্থান তালিকা', 'Halal Places List') ?></div>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th><?= __('আইডি', 'ID') ?></th>
            <th><?= __('প্রতিষ্ঠান', 'Establishment') ?></th>
            <th><?= __('ঠিকানা', 'Address') ?></th>
            <th><?= __('ফোন', 'Phone') ?></th>
            <th><?= __('সনদ', 'Status') ?></th>
            <th><?= __('ভেরিফিকেশন', 'Verification') ?></th>
            <th style="text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($halalPlaces)): ?>
            <tr><td colspan="7" style="text-align: center; color: var(--text-dim);"><?= __('কোনো প্রতিষ্ঠান পাওয়া যায়নি।', 'No establishments found.') ?></td></tr>
          <?php else: ?>
            <?php foreach ($halalPlaces as $hp): ?>
              <tr>
                <td>#<?= toLangNum($hp['id']) ?></td>
                <td>
                  <strong><?= htmlspecialchars($hp['name']) ?></strong>
                  <div style="font-size: 11px; color: var(--text-muted);"><?= htmlspecialchars($hp['category']) ?></div>
                </td>
                <td>
                  <span class="badge badge-info"><?= htmlspecialchars($hp['district']) ?></span>
                  <div style="font-size: 12px; color: var(--text-muted); margin-top: 2px;"><?= htmlspecialchars($hp['address']) ?></div>
                </td>
                <td style="font-size: 13px;">
                  <?= !empty($hp['phone']) ? '📞 ' . htmlspecialchars($hp['phone']) : '—' ?>
                </td>
                <td>
                  <span class="badge badge-success"><?= htmlspecialchars($hp['halal_status']) ?></span>
                </td>
                <td>
                  <a href="mosques.php?tab=halal&toggle_halal=<?= $hp['id'] ?>" class="badge <?= $hp['is_verified'] ? 'badge-success' : 'badge-secondary' ?>" style="text-decoration:none;" title="<?= __('ক্লিক করে স্ট্যাটাস পরিবর্তন করুন', 'Click to toggle status') ?>">
                    <?= $hp['is_verified'] ? __('✓ সার্টিফাইড', '✓ Certified') : __('যাচাইাধীন', 'Pending') ?>
                  </a>
                </td>
                <td style="text-align: right; white-space: nowrap;">
                  <a href="mosques.php?tab=halal&delete_halal=<?= $hp['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirm('<?= __('এই প্রতিষ্ঠানটি মুছে ফেলতে চান?', 'Are you sure you want to delete this establishment?') ?>');">
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
<?php endif; ?>

<!-- Add/Edit Mosque Modal -->
<div class="modal-backdrop" id="mosqueModal">
  <div class="modal-window">
    <div class="modal-header">
      <div class="modal-title" id="mModalTitle"><?= __('নতুন মসজিদ', 'New Mosque') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('mosqueModal')">&times;</button>
    </div>
    <form method="POST" action="mosques.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" id="mAction" value="create_mosque">
      <input type="hidden" name="id" id="mId" value="">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('মসজিদের নাম', 'Mosque Name') ?> *</label>
            <input type="text" name="name" id="inputMName" class="form-control" placeholder="<?= __('যেমন: বায়তুল মোকাররম জাতীয় মসজিদ', 'e.g. Baitul Mukarram National Mosque') ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('জেলা', 'District') ?> *</label>
            <input type="text" name="district" id="inputMDistrict" class="form-control" placeholder="<?= __('যেমন: ঢাকা', 'e.g. Dhaka') ?>" required>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('পূর্ণ ঠিকানা', 'Full Address') ?> *</label>
          <textarea name="address" id="inputMAddress" class="form-control" rows="2" placeholder="<?= __('পল্টন, ঢাকা-১০০০', 'Paltan, Dhaka-1000') ?>" required></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('অক্ষাংশ', 'Latitude') ?></label>
            <input type="number" step="0.000001" name="latitude" id="inputMLat" class="form-control" placeholder="23.7297">
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('দ্রাঘিমাংশ', 'Longitude') ?></label>
            <input type="number" step="0.000001" name="longitude" id="inputMLng" class="form-control" placeholder="90.4131">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('জুম’আর জামাত সময়', 'Jummah Prayer Time') ?></label>
            <input type="text" name="jummah_time" id="inputMJummah" class="form-control" value="১:৩০ PM">
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('সুযোগ-সুবিধা', 'Facilities') ?></label>
            <input type="text" name="facilities" id="inputMFacilities" class="form-control" placeholder="<?= __('অজু খানা, মহিলাদের নামাজের ব্যবস্থা', 'Ablution area, women prayer space') ?>">
          </div>
        </div>

        <div class="form-group">
          <label class="switch-label">
            <label class="switch">
              <input type="checkbox" name="is_verified" id="inputMVerified" value="1" checked>
              <span class="slider"></span>
            </label>
            <span style="font-weight: 600;"><?= __('যাচাইকৃত ব্যাজ', 'Verified Badge') ?></span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('mosqueModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary" id="btnMSubmit">💾 <?= __('সংরক্ষণ করুন', 'Save') ?></button>
      </div>
    </form>
  </div>
</div>

<!-- Add Halal Place Modal -->
<div class="modal-backdrop" id="addHalalModal">
  <div class="modal-window">
    <div class="modal-header">
      <div class="modal-title"><?= __('নতুন হালাল স্থান', 'New Halal Place') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('addHalalModal')">&times;</button>
    </div>
    <form method="POST" action="mosques.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="create_halal">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('প্রতিষ্ঠানের নাম', 'Establishment Name') ?> *</label>
            <input type="text" name="name" class="form-control" placeholder="<?= __('যেমন: আল-কাদেরিয়া রেস্টুরেন্ট', 'e.g. Al-Kaderia Restaurant') ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('ক্যাটাগরি', 'Category') ?></label>
            <input type="text" name="category" class="form-control" value="Restaurant">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('জেলা', 'District') ?> *</label>
            <input type="text" name="district" class="form-control" value="ঢাকা" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('ফোন নম্বর', 'Phone Number') ?></label>
            <input type="tel" name="phone" class="form-control" placeholder="017XXXXXXXX">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('ঠিকানা', 'Address') ?> *</label>
          <input type="text" name="address" class="form-control" placeholder="<?= __('মিরপুর-১০, ঢাকা', 'Mirpur-10, Dhaka') ?>" required>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('addHalalModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">💾 <?= __('সংরক্ষণ করুন', 'Save') ?></button>
      </div>
    </form>
  </div>
</div>

<script>
const mosqueLang = {
  addTitle: <?= json_encode(__('➕ নতুন মসজিদ', '➕ New Mosque')) ?>,
  editTitle: <?= json_encode(__('✏️ মসজিদ সম্পাদনা', '✏️ Edit Mosque')) ?>,
  btnSave: <?= json_encode(__('💾 সংরক্ষণ করুন', '💾 Save')) ?>,
  btnUpdate: <?= json_encode(__('💾 সংরক্ষণ করুন', '💾 Save')) ?>
};

function openAddMosqueModal() {
  document.getElementById('mModalTitle').textContent = mosqueLang.addTitle;
  document.getElementById('mAction').value = 'create_mosque';
  document.getElementById('mId').value = '';
  document.getElementById('inputMName').value = '';
  document.getElementById('inputMDistrict').value = 'ঢাকা';
  document.getElementById('inputMAddress').value = '';
  document.getElementById('inputMLat').value = '';
  document.getElementById('inputMLng').value = '';
  document.getElementById('inputMJummah').value = '১:৩০ PM';
  document.getElementById('inputMFacilities').value = '';
  document.getElementById('inputMVerified').checked = true;
  document.getElementById('btnMSubmit').textContent = mosqueLang.btnSave;
  openModal('mosqueModal');
}

function editMosque(m) {
  document.getElementById('mModalTitle').textContent = mosqueLang.editTitle;
  document.getElementById('mAction').value = 'update_mosque';
  document.getElementById('mId').value = m.id;
  document.getElementById('inputMName').value = m.name;
  document.getElementById('inputMDistrict').value = m.district;
  document.getElementById('inputMAddress').value = m.address;
  document.getElementById('inputMLat').value = m.latitude || '';
  document.getElementById('inputMLng').value = m.longitude || '';
  document.getElementById('inputMJummah').value = m.jummah_time;
  document.getElementById('inputMFacilities').value = m.facilities || '';
  document.getElementById('inputMVerified').checked = (m.is_verified == 1);
  document.getElementById('btnMSubmit').textContent = mosqueLang.btnUpdate;
  openModal('mosqueModal');
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
