<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - BLOOD DONOR ORGANIZATIONS (রক্তদাতা সংগঠন ও সংস্থা)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Handle POST actions (Create / Update / Delete)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('blood_organizations.php');
    }

    if ($action === 'create_org' || $action === 'update_org') {
        $nameBn = trim($_POST['name_bn'] ?? '');
        $nameEn = trim($_POST['name_en'] ?? '');
        $category = trim($_POST['category'] ?? 'স্বেচ্ছাসেবী রক্তদান সংস্থা');
        $district = trim($_POST['district'] ?? 'ঢাকা');
        $address = trim($_POST['address'] ?? '');
        $hotline = trim($_POST['hotline_phone'] ?? '');
        $altPhone = trim($_POST['alt_phone'] ?? '');
        $website = trim($_POST['website'] ?? '');
        $verified = isset($_POST['is_verified']) ? 1 : 0;

        if (empty($nameBn) || empty($hotline)) {
            setFlash('danger', __('সংগঠনের নাম ও হটলাইন নম্বর আবশ্যক।', 'Organization name and hotline phone are required.'));
        } else {
            try {
                if ($action === 'create_org') {
                    $stmt = $pdo->prepare("INSERT INTO blood_organizations (name_bn, name_en, category, district, address, hotline_phone, alt_phone, website, is_verified) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$nameBn, !empty($nameEn) ? $nameEn : $nameBn, $category, $district, $address, $hotline, $altPhone, $website, $verified]);
                    logAdminAction($pdo, 'CREATE_BLOOD_ORG', 'blood_organizations', (string)$pdo->lastInsertId(), "সংস্থা যুক্ত: $nameBn");
                    setFlash('success', __('নতুন রক্তদাতা সংস্থা সফলভাবে যুক্ত হয়েছে!', 'New blood organization added successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE blood_organizations SET name_bn = ?, name_en = ?, category = ?, district = ?, address = ?, hotline_phone = ?, alt_phone = ?, website = ?, is_verified = ? WHERE id = ?");
                    $stmt->execute([$nameBn, !empty($nameEn) ? $nameEn : $nameBn, $category, $district, $address, $hotline, $altPhone, $website, $verified, $id]);
                    logAdminAction($pdo, 'UPDATE_BLOOD_ORG', 'blood_organizations', (string)$id, "সংস্থা আপডেট: $nameBn");
                    setFlash('success', __('সংস্থার তথ্য সফলভাবে আপডেট হয়েছে!', 'Organization updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('সংরক্ষণ ব্যর্থ: ', 'Save failed: ') . $e->getMessage());
            }
        }
    }
    redirect('blood_organizations.php');
}

// GET Delete action
if (isset($_GET['delete_org'])) {
    $id = (int)$_GET['delete_org'];
    try {
        $pdo->prepare("DELETE FROM blood_organizations WHERE id = ?")->execute([$id]);
        logAdminAction($pdo, 'DELETE_BLOOD_ORG', 'blood_organizations', (string)$id, 'সংস্থা মুছে ফেলা হয়েছে');
        setFlash('success', __('সংস্থাটি সফলভাবে মুছে ফেলা হয়েছে।', 'Organization deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
    }
    redirect('blood_organizations.php');
}

// Fetch list
$organizations = [];
try {
    $organizations = $pdo->query("SELECT * FROM blood_organizations ORDER BY id ASC")->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('সংগঠন তালিকা', 'Organizations');
$activeNav = 'blood_organizations';
require_once __DIR__ . '/header.php';
?>

<!-- Header Toolbar -->
<div style="display: flex; gap: 10px; margin-bottom: 20px; align-items: center; justify-content: space-between; flex-wrap: wrap;">
  <div>
    <h2 style="margin: 0; font-size: 20px; font-weight: 700; color: var(--text-main);">
      <i class="fa-solid fa-building-ngo"></i> <?= __('সংগঠন তালিকা', 'Organizations') ?>
    </h2>
  </div>
  <button type="button" class="btn btn-primary" onclick="openAddOrgModal()">
    <i class="fa-solid fa-plus"></i> <?= __('নতুন সংগঠন', 'New Organization') ?>
  </button>
</div>

<!-- Organizations Table -->
<div class="card">
  <div class="card-header">
    <div class="card-title">
      <i class="fa-solid fa-list"></i> <?= __('সংগঠন তালিকা', 'Organizations') ?> 
      <span class="badge badge-info" style="margin-left: 8px;"><?= toLangNum(count($organizations)) ?></span>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 50px;"><?= __('আইডি', 'ID') ?></th>
          <th><?= __('সংগঠনের নাম', 'Organization Name') ?></th>
          <th><?= __('ক্যাটাগরি ও জেলা', 'Category & District') ?></th>
          <th><?= __('হটলাইন ও ফোন', 'Hotline & Phone') ?></th>
          <th><?= __('ঠিকানা ও ওয়েবসাইট', 'Address & Website') ?></th>
          <th><?= __('স্ট্যাটাস', 'Status') ?></th>
          <th style="text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($organizations)): ?>
          <tr><td colspan="7" style="text-align: center; color: var(--text-dim);"><?= __('কোনো সংস্থা তালিকাভুক্ত নেই।', 'No organizations listed.') ?></td></tr>
        <?php else: ?>
          <?php foreach ($organizations as $org): ?>
            <tr>
              <td>#<?= toLangNum($org['id']) ?></td>
              <td>
                <strong style="color: var(--text-main); font-size: 14px;"><?= htmlspecialchars($org['name_bn']) ?></strong>
                <div style="font-size: 12px; color: var(--text-muted);"><?= htmlspecialchars($org['name_en']) ?></div>
              </td>
              <td>
                <span class="badge badge-secondary"><?= htmlspecialchars($org['category']) ?></span>
                <div style="font-size: 12px; color: var(--primary-light); font-weight: 600; margin-top: 3px;">📍 <?= htmlspecialchars($org['district']) ?></div>
              </td>
              <td>
                <a href="tel:<?= htmlspecialchars($org['hotline_phone']) ?>" style="color: #ef4444; font-weight: bold; text-decoration: none;">
                  📞 <?= htmlspecialchars($org['hotline_phone']) ?>
                </a>
                <?php if (!empty($org['alt_phone'])): ?>
                  <div style="font-size: 11px; color: var(--text-muted);">বিকল্প: <?= htmlspecialchars($org['alt_phone']) ?></div>
                <?php endif; ?>
              </td>
              <td>
                <div style="font-size: 12px; color: var(--text-main); max-width: 200px;"><?= htmlspecialchars($org['address']) ?></div>
                <?php if (!empty($org['website'])): ?>
                  <a href="<?= htmlspecialchars($org['website']) ?>" target="_blank" style="font-size: 11px; color: var(--primary-light); text-decoration: underline;">
                    🌐 <?= htmlspecialchars($org['website']) ?>
                  </a>
                <?php endif; ?>
              </td>
              <td>
                <span class="badge <?= $org['is_verified'] ? 'badge-success' : 'badge-warning' ?>">
                  <?= $org['is_verified'] ? __('✓ ভেরিফাইড', '✓ Verified') : __('অপেক্ষমাণ', 'Pending') ?>
                </span>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <button type="button" class="btn btn-secondary btn-sm" onclick='openEditOrgModal(<?= htmlspecialchars(json_encode($org), ENT_QUOTES, "UTF-8") ?>)'>
                  ✏️ <?= __('সম্পাদনা', 'Edit') ?>
                </button>
                <a href="blood_organizations.php?delete_org=<?= $org['id'] ?>" class="btn btn-danger btn-sm" onclick="return confirm('<?= __('এই সংস্থাটির তথ্য মুছে ফেলতে চান?', 'Are you sure you want to delete this organization?') ?>');">
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

<!-- Add/Edit Org Modal -->
<div class="modal-backdrop" id="orgModal">
  <div class="modal-window">
    <div class="modal-header">
      <div class="modal-title" id="orgModalTitle"><?= __('নতুন সংগঠন যুক্ত করুন', 'Add New Blood Organization') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('orgModal')">&times;</button>
    </div>
    <form method="POST" action="blood_organizations.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" id="orgFormAction" value="create_org">
      <input type="hidden" name="id" id="orgId" value="">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('সংগঠনের নাম (বাংলা)', 'Organization Name (Bengali)') ?> *</label>
            <input type="text" name="name_bn" id="inpOrgNameBn" class="form-control" placeholder="<?= __('যেমন: সন্ধানী (কেন্দ্রীয় পরিষদ)', 'e.g. Sandhani') ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('সংগঠনের ইংরেজি নাম', 'Organization Name (English)') ?></label>
            <input type="text" name="name_en" id="inpOrgNameEn" class="form-control" placeholder="<?= __('e.g. Sandhani Central Committee', 'e.g. Sandhani') ?>">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('ক্যাটাগরি / ধরন', 'Category') ?></label>
            <select name="category" id="inpOrgCategory" class="form-control">
              <option value="স্বেচ্ছাসেবী রক্তদান সংস্থা"><?= __('স্বেচ্ছাসেবী রক্তদান সংস্থা', 'Voluntary Blood Donors') ?></option>
              <option value="ব্লাড ল্যাব ও সেবা"><?= __('ব্লাড ল্যাব ও সেবা', 'Blood Lab & Services') ?></option>
              <option value="জাতীয় রক্তদান সংস্থা"><?= __('জাতীয় রক্তদান সংস্থা', 'National Blood Organization') ?></option>
              <option value="স্বেচ্ছাসেবী ছাত্র সংগঠন"><?= __('স্বেচ্ছাসেবী ছাত্র সংগঠন', 'Student Volunteer Club') ?></option>
              <option value="সরকারি ব্লাড ব্যাংক"><?= __('সরকারি ব্লাড ব্যাংক', 'Government Blood Bank') ?></option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('জেলা / এলাকা', 'District / Area') ?></label>
            <input type="text" name="district" id="inpOrgDistrict" class="form-control" value="ঢাকা" placeholder="<?= __('যেমন: ঢাকা / চট্টগ্রাম / সারাদেশ', 'e.g. Dhaka / Nationwide') ?>">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?= __('হটলাইন ফোন নম্বর', 'Hotline Phone') ?> *</label>
            <input type="text" name="hotline_phone" id="inpOrgHotline" class="form-control" placeholder="<?= __('যেমন: 01711-000000', 'e.g. 01711-000000') ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?= __('বিকল্প ফোন / টেলিফোন', 'Alternative Phone') ?></label>
            <input type="text" name="alt_phone" id="inpOrgAltPhone" class="form-control" placeholder="<?= __('যেমন: 02-9668690', 'e.g. 02-9668690') ?>">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('পূর্ণাঙ্গ ঠিকানা', 'Full Address') ?></label>
          <input type="text" name="address" id="inpOrgAddress" class="form-control" placeholder="<?= __('যেমন: ঢাকা মেডিকেল কলেজ, ঢাকা', 'e.g. Dhaka Medical College, Dhaka') ?>">
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('অফিসিয়াল ওয়েবসাইট / ফেসবুক পেজ', 'Website / Page URL') ?></label>
          <input type="url" name="website" id="inpOrgWebsite" class="form-control" placeholder="https://sandhani.org">
        </div>

        <div class="form-group" style="margin-top: 10px;">
          <label style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
            <input type="checkbox" name="is_verified" id="inpOrgVerified" value="1" checked style="width: 18px; height: 18px;">
            <span style="font-weight: 600; color: var(--text-main);"><?= __('ভেরিফাইড ও নির্ভরযোগ্য সংস্থা হিসেবে প্রদর্শন করুন', 'Display as verified and trusted organization') ?></span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('orgModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">💾 <?= __('সংরক্ষণ করুন', 'Save Organization') ?></button>
      </div>
    </form>
  </div>
</div>

<script>
function openAddOrgModal() {
  document.getElementById('orgFormAction').value = 'create_org';
  document.getElementById('orgId').value = '';
  document.getElementById('inpOrgNameBn').value = '';
  document.getElementById('inpOrgNameEn').value = '';
  document.getElementById('inpOrgCategory').value = 'স্বেচ্ছাসেবী রক্তদান সংস্থা';
  document.getElementById('inpOrgDistrict').value = 'ঢাকা';
  document.getElementById('inpOrgHotline').value = '';
  document.getElementById('inpOrgAltPhone').value = '';
  document.getElementById('inpOrgAddress').value = '';
  document.getElementById('inpOrgWebsite').value = '';
  document.getElementById('inpOrgVerified').checked = true;
  document.getElementById('orgModalTitle').innerText = '<?= __('নতুন সংগঠন যুক্ত করুন', 'Add New Blood Organization') ?>';
  openModal('orgModal');
}

function openEditOrgModal(org) {
  document.getElementById('orgFormAction').value = 'update_org';
  document.getElementById('orgId').value = org.id;
  document.getElementById('inpOrgNameBn').value = org.name_bn || '';
  document.getElementById('inpOrgNameEn').value = org.name_en || '';
  document.getElementById('inpOrgCategory').value = org.category || 'স্বেচ্ছাসেবী রক্তদান সংস্থা';
  document.getElementById('inpOrgDistrict').value = org.district || 'ঢাকা';
  document.getElementById('inpOrgHotline').value = org.hotline_phone || '';
  document.getElementById('inpOrgAltPhone').value = org.alt_phone || '';
  document.getElementById('inpOrgAddress').value = org.address || '';
  document.getElementById('inpOrgWebsite').value = org.website || '';
  document.getElementById('inpOrgVerified').checked = (parseInt(org.is_verified) === 1);
  document.getElementById('orgModalTitle').innerText = '<?= __('সংগঠনের তথ্য সম্পাদনা', 'Edit Blood Organization') ?>';
  openModal('orgModal');
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
