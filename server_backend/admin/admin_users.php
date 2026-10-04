<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ADMIN USERS & ACCESS CONTROL SUITE
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

$currentAdminId = (int)($_SESSION['admin_id'] ?? 0);

// Handle POST (Add Admin, Edit Admin, or Reset Password)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ।', 'Invalid or expired security token.'));
        redirect('admin_users.php');
    }

    // 1. Create New Admin Account
    if ($action === 'create_admin') {
        $username = trim($_POST['username'] ?? '');
        $fullName = trim($_POST['full_name'] ?? '');
        $firstName = trim($_POST['first_name'] ?? '');
        $lastName = trim($_POST['last_name'] ?? '');
        $email = trim($_POST['email'] ?? '');
        $phone = trim($_POST['phone'] ?? '');
        $gender = trim($_POST['gender'] ?? 'male');
        $bloodGroup = trim($_POST['blood_group'] ?? 'O+');
        $role = trim($_POST['role'] ?? 'MODERATOR');
        $password = trim($_POST['password'] ?? '');

        if (empty($username) || empty($password)) {
            setFlash('danger', __('ইউজারনেম এবং পাসওয়ার্ড উভয়ই আবশ্যক।', 'Username and password are required.'));
        } else {
            try {
                // Check duplicate username
                $uCheck = $pdo->prepare("SELECT COUNT(*) FROM admin_users WHERE username = ?");
                $uCheck->execute([$username]);
                if ($uCheck->fetchColumn() > 0) {
                    throw new Exception(__('এই ইউজারনেমটি ইতিমধ্যে বিদ্যমান। অন্য একটি দিন।', 'This username already exists. Please choose another.'));
                }

                if (empty($fullName)) {
                    $fullName = trim($firstName . ' ' . $lastName) ?: $username;
                }

                $passHash = hash('sha256', $password);
                $stmt = $pdo->prepare("INSERT INTO admin_users (username, password_hash, full_name, first_name, last_name, email, phone, gender, blood_group, role, created_at) 
                                       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
                $stmt->execute([$username, $passHash, $fullName, $firstName, $lastName, !empty($email) ? $email : null, !empty($phone) ? $phone : null, $gender, $bloodGroup, $role]);
                
                logAdminAction($pdo, 'CREATE_ADMIN', 'admin_users', (string)$pdo->lastInsertId(), "নতুন অ্যাডমিন অ্যাকাউন্ট তৈরি: $username ($role)");
                setFlash('success', __('নতুন অ্যাডমিন অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে!', 'New admin account created successfully!'));
            } catch (Exception $e) {
                setFlash('danger', __('অ্যাকাউন্ট তৈরি ব্যর্থ: ', 'Account creation failed: ') . $e->getMessage());
            }
        }
    }

    // 2. Edit Admin Details (with optional password change)
    if ($action === 'edit_admin') {
        $id = (int)($_POST['id'] ?? 0);
        $username = trim($_POST['username'] ?? '');
        $fullName = trim($_POST['full_name'] ?? '');
        $firstName = trim($_POST['first_name'] ?? '');
        $lastName = trim($_POST['last_name'] ?? '');
        $email = trim($_POST['email'] ?? '');
        $phone = trim($_POST['phone'] ?? '');
        $gender = trim($_POST['gender'] ?? 'male');
        $bloodGroup = trim($_POST['blood_group'] ?? 'O+');
        $role = trim($_POST['role'] ?? 'MODERATOR');
        $newPassword = trim($_POST['new_password'] ?? '');

        if ($id > 0 && !empty($username)) {
            try {
                // Check username duplicate
                $uCheck = $pdo->prepare("SELECT COUNT(*) FROM admin_users WHERE username = ? AND id != ?");
                $uCheck->execute([$username, $id]);
                if ($uCheck->fetchColumn() > 0) {
                    throw new Exception(__('এই ইউজারনেমটি অন্য কোনো অ্যাডমিনের রয়েছে।', 'This username is already taken.'));
                }

                if (empty($fullName)) {
                    $fullName = trim($firstName . ' ' . $lastName) ?: $username;
                }

                $stmt = $pdo->prepare("UPDATE admin_users SET username = ?, full_name = ?, first_name = ?, last_name = ?, email = ?, phone = ?, gender = ?, blood_group = ?, role = ? WHERE id = ?");
                $stmt->execute([$username, $fullName, $firstName, $lastName, !empty($email) ? $email : null, !empty($phone) ? $phone : null, $gender, $bloodGroup, $role, $id]);

                if (!empty($newPassword) && strlen($newPassword) >= 6) {
                    $passHash = hash('sha256', $newPassword);
                    $pdo->prepare("UPDATE admin_users SET password_hash = ? WHERE id = ?")->execute([$passHash, $id]);
                }

                // If editing self, update session
                if ($id === $currentAdminId) {
                    $_SESSION['admin_username'] = $username;
                    $_SESSION['admin_full_name'] = $fullName;
                    $_SESSION['admin_role'] = $role;
                }

                logAdminAction($pdo, 'EDIT_ADMIN', 'admin_users', (string)$id, "অ্যাডমিন তথ্য আপডেট: $username ($role)");
                setFlash('success', __('অ্যাডমিন প্রোফাইল তথ্য সফলভাবে আপডেট করা হয়েছে।', 'Admin profile updated successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('আপডেট ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
            }
        }
    }

    // 3. Reset Admin Password Direct
    if ($action === 'reset_admin_password') {
        $id = (int)($_POST['id'] ?? 0);
        $newPassword = trim($_POST['new_password'] ?? '');

        if ($id > 0 && strlen($newPassword) >= 6) {
            try {
                $passHash = hash('sha256', $newPassword);
                $pdo->prepare("UPDATE admin_users SET password_hash = ? WHERE id = ?")->execute([$passHash, $id]);
                logAdminAction($pdo, 'RESET_ADMIN_PASSWORD', 'admin_users', (string)$id, 'পাসওয়ার্ড পরিবর্তন সম্পন্ন');
                setFlash('success', __('অ্যাডমিনের পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে!', 'Admin password has been changed successfully!'));
            } catch (Exception $e) {
                setFlash('danger', __('পাসওয়ার্ড পরিবর্তন ব্যর্থ: ', 'Password change failed: ') . $e->getMessage());
            }
        } else {
            setFlash('danger', __('পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে।', 'Password must be at least 6 characters long.'));
        }
    }

    redirect('admin_users.php');
}

// Handle Delete / Revoke Admin Access
if (isset($_GET['delete_admin'])) {
    $delId = (int)$_GET['delete_admin'];
    if ($delId === $currentAdminId) {
        setFlash('danger', __('আপনি নিজের সক্রিয় অ্যাডমিন অ্যাকাউন্ট মুছে ফেলতে পারবেন না!', 'You cannot delete your own active admin account!'));
    } else {
        try {
            $pdo->prepare("DELETE FROM admin_users WHERE id = ?")->execute([$delId]);
            logAdminAction($pdo, 'REVOKE_ADMIN_ACCESS', 'admin_users', (string)$delId, 'অ্যাডমিন এক্সেস বাতিল করা হয়েছে');
            setFlash('success', __('অ্যাডমিন এক্সেস সফলভাবে বাতিল করা হয়েছে। (ইউজার সাধারণ ব্যবহারকারী হিসেবে সক্রিয় থাকবেন)', 'Admin access revoked successfully. (The user remains active as a regular user)'));
        } catch (Exception $e) {
            setFlash('danger', __('বাতিল করতে ব্যর্থ: ', 'Revocation failed: ') . $e->getMessage());
        }
    }
    redirect('admin_users.php');
}

// Fetch stats
$totalAdmins = 0;
$superAdminsCount = 0;
$moderatorsCount = 0;
try {
    $totalAdmins = (int)$pdo->query("SELECT COUNT(*) FROM admin_users")->fetchColumn();
    $superAdminsCount = (int)$pdo->query("SELECT COUNT(*) FROM admin_users WHERE role = 'SUPERADMIN'")->fetchColumn();
    $moderatorsCount = (int)$pdo->query("SELECT COUNT(*) FROM admin_users WHERE role = 'MODERATOR'")->fetchColumn();
} catch (Exception $e) {}

// Fetch all admin accounts
$admins = [];
try {
    $admins = $pdo->query("SELECT id, username, full_name, first_name, last_name, email, phone, gender, blood_group, role, last_login, created_at FROM admin_users ORDER BY id ASC")->fetchAll();
} catch (Exception $e) {}

// Fetch registered users for one-click admin promotion
$registeredUsers = [];
try {
    $registeredUsers = $pdo->query("SELECT user_id, name, username, email, phone, gender, blood_group, district FROM users ORDER BY name ASC LIMIT 300")->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('অ্যাডমিনিস্ট্রেটর', 'Administrator');
$activeNav = 'admin_users';
require_once __DIR__ . '/header.php';
?>

<!-- ==============================================================================
     TOP STATS SUMMARY CARDS
     ============================================================================== -->
<div class="stats-grid" style="margin-bottom: 24px;">
  <!-- Total Admins -->
  <div class="stat-card">
    <div class="stat-icon-wrap amber">
      <i class="fa-solid fa-user-shield"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($totalAdmins); ?></h3>
      <p><?php echo __('মোট অ্যাডমিন', 'Total Admins'); ?></p>
    </div>
  </div>

  <!-- Super Admins -->
  <div class="stat-card">
    <div class="stat-icon-wrap emerald">
      <i class="fa-solid fa-crown"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($superAdminsCount); ?></h3>
      <p><?php echo __('সুপার অ্যাডমিন', 'Super Admins'); ?></p>
    </div>
  </div>

  <!-- Moderators -->
  <div class="stat-card">
    <div class="stat-icon-wrap blue">
      <i class="fa-solid fa-shield-halved"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($moderatorsCount); ?></h3>
      <p><?php echo __('মডারেটর', 'Moderators'); ?></p>
    </div>
  </div>

  <!-- Current Session -->
  <div class="stat-card">
    <div class="stat-icon-wrap purple">
      <i class="fa-solid fa-circle-check"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo htmlspecialchars($_SESSION['admin_username'] ?? 'admin'); ?></h3>
      <p><?php echo __('বর্তমান সেশন', 'Current Session'); ?></p>
    </div>
  </div>
</div>

<!-- ==============================================================================
     TOP ACTION BAR
     ============================================================================== -->
<div class="card" style="margin-bottom: 24px;">
  <div class="card-body" style="padding: 16px 20px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px;">
    <div style="font-weight: 700; font-size: 15px; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-users-gear" style="color: var(--primary);"></i>
      <span><?php echo __('অ্যাডমিন ও এক্সেস', 'Admin Access'); ?></span>
    </div>

    <div>
      <button type="button" class="btn btn-primary btn-sm" onclick="openModal('createAdminModal')">
        <i class="fa-solid fa-user-plus"></i> <?php echo __('নতুন অ্যাডমিন', 'Add Admin'); ?>
      </button>
    </div>
  </div>
</div>

<!-- ==============================================================================
     ADMINS DIRECTORY TABLE
     ============================================================================== -->
<div class="card">
  <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-shield" style="color: var(--primary);"></i>
      <span><?php echo __('অ্যাডমিন তালিকা', 'Admin List'); ?></span>
      <span class="badge badge-info" style="font-size: 11px;"><?php echo toLangNum(count($admins)); ?></span>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 50px; text-align: center;">#</th>
          <th><?php echo __('অ্যাডমিন', 'Admin'); ?></th>
          <th><?php echo __('রোল', 'Role'); ?></th>
          <th><?php echo __('যোগাযোগ', 'Contact'); ?></th>
          <th><?php echo __('সর্বশেষ লগইন', 'Last Login'); ?></th>
          <th><?php echo __('তারিখ', 'Date'); ?></th>
          <th style="text-align: right; width: 140px;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php foreach ($admins as $idx => $adm): ?>
          <tr>
            <!-- Serial -->
            <td style="text-align: center; font-weight: 700; color: var(--text-dim); font-size: 12px;">
              #<?php echo toLangNum($idx + 1); ?>
            </td>

            <!-- Admin Profile -->
            <td>
              <div style="display: flex; align-items: center; gap: 10px;">
                <div style="width: 36px; height: 36px; border-radius: 50%; background: rgba(16, 185, 129, 0.15); border: 1.5px solid var(--border-color); display: flex; align-items: center; justify-content: center; font-size: 15px; color: var(--primary-light); font-weight: 800; flex-shrink: 0;">
                  <?php echo mb_substr($adm['full_name'] ?: $adm['username'], 0, 1, 'UTF-8'); ?>
                </div>
                <div>
                  <div style="font-weight: 700; color: var(--text-heading); font-size: 13.5px;">
                    <?php echo htmlspecialchars($adm['full_name']); ?>
                    <?php if ($adm['id'] === $currentAdminId): ?>
                      <span class="badge badge-success" style="font-size: 10px; padding: 1px 6px; margin-left: 4px;"><?php echo __('আপনি', 'You'); ?></span>
                    <?php endif; ?>
                  </div>
                  <div style="font-size: 11px; color: var(--text-dim); font-family: monospace;">
                    @<?php echo htmlspecialchars($adm['username']); ?>
                  </div>
                </div>
              </div>
            </td>

            <!-- Role Badge -->
            <td>
              <span class="badge <?php echo $adm['role'] === 'SUPERADMIN' ? 'badge-warning' : 'badge-info'; ?>" style="font-size: 11px; padding: 3px 8px;">
                <i class="fa-solid <?php echo $adm['role'] === 'SUPERADMIN' ? 'fa-crown' : 'fa-shield-halved'; ?>" style="font-size: 10px;"></i>
                <?php echo htmlspecialchars($adm['role']); ?>
              </span>
            </td>

            <!-- Contact & Blood -->
            <td>
              <div style="font-size: 12.5px;">
                <?php if (!empty($adm['email'])): ?>
                  <div style="color: var(--text-main);">
                    <i class="fa-regular fa-envelope" style="font-size: 10px; color: var(--text-dim); margin-right: 4px;"></i>
                    <?php echo htmlspecialchars($adm['email']); ?>
                  </div>
                <?php endif; ?>
                <?php if (!empty($adm['phone'])): ?>
                  <div style="font-size: 11.5px; color: var(--text-dim);">
                    <i class="fa-solid fa-phone" style="font-size: 10px; margin-right: 4px;"></i>
                    <?php echo htmlspecialchars($adm['phone']); ?>
                  </div>
                <?php endif; ?>
                <?php if (!empty($adm['blood_group'])): ?>
                  <span class="badge badge-danger" style="font-size: 10px; padding: 1px 6px; margin-top: 2px; display: inline-flex; align-items: center; gap: 2px;">
                    🩸 <?php echo htmlspecialchars($adm['blood_group']); ?>
                  </span>
                <?php endif; ?>
              </div>
            </td>

            <!-- Last Login -->
            <td style="font-size: 11.5px; color: var(--text-dim); white-space: nowrap;">
              <?php echo !empty($adm['last_login']) ? (isEn() ? date('d M Y, h:i A', strtotime($adm['last_login'])) : toLangNum(date('d M Y, h:i A', strtotime($adm['last_login'])))) : __('কখনো নয়', 'Never'); ?>
            </td>

            <!-- Created Date -->
            <td style="font-size: 11.5px; color: var(--text-dim); white-space: nowrap;">
              <?php echo isEn() ? date('d M Y', strtotime($adm['created_at'])) : toLangNum(date('d-m-Y', strtotime($adm['created_at']))); ?>
            </td>

            <!-- Actions -->
            <td style="text-align: right;">
              <div style="display: flex; gap: 5px; justify-content: flex-end;">
                <button type="button" class="btn btn-secondary btn-sm" style="padding: 5px 8px; font-size: 11.5px;" title="<?php echo __('সম্পাদনা', 'Edit'); ?>" onclick='openEditAdminModal(<?php echo json_encode($adm, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)'>
                  <i class="fa-solid fa-pen"></i>
                </button>
                <button type="button" class="btn btn-secondary btn-sm" style="padding: 5px 8px; font-size: 11.5px;" title="<?php echo __('পাসওয়ার্ড পরিবর্তন', 'Reset Password'); ?>" onclick="openResetAdminPassModal(<?php echo (int)$adm['id']; ?>, '<?php echo htmlspecialchars(addslashes($adm['username'])); ?>')">
                  <i class="fa-solid fa-key"></i>
                </button>
                <?php if ($adm['id'] !== $currentAdminId): ?>
                  <a href="admin_users.php?delete_admin=<?php echo (int)$adm['id']; ?>" class="btn btn-danger btn-sm" style="padding: 5px 8px; font-size: 11.5px;" title="<?php echo __('এক্সেস বাতিল', 'Revoke Access'); ?>" onclick="return confirm('<?php echo __('এই ব্যবহারকারীর অ্যাডমিন এক্সেস বাতিল করতে চান? তিনি সাধারণ ব্যবহারকারী হিসেবে সক্রিয় থাকবেন।', 'Are you sure you want to revoke admin access for this account? They will remain active as a regular user.'); ?>');">
                    <i class="fa-solid fa-user-xmark"></i>
                  </a>
                <?php endif; ?>
              </div>
            </td>
          </tr>
        <?php endforeach; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- ==============================================================================
     MODAL: CREATE NEW ADMIN
     ============================================================================== -->
<div class="modal-backdrop" id="createAdminModal">
  <div class="modal-window" style="max-width: 600px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-user-plus" style="color: var(--primary);"></i>
        <span><?php echo __('নতুন অ্যাডমিন', 'New Admin'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('createAdminModal')">&times;</button>
    </div>
    <form method="POST" action="admin_users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="create_admin">

      <div class="modal-body" style="padding: 20px;">
        <!-- Fast User Picker to Promote Existing User -->
        <div class="form-group" style="background: var(--hover-bg); border: 1px dashed var(--border-color); border-radius: var(--radius-md); padding: 12px; margin-bottom: 16px;">
          <label class="form-label" style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px;">
            <span><i class="fa-solid fa-users" style="color: var(--primary);"></i> <?php echo __('নিবন্ধিত ইউজার', 'Registered User'); ?></span>
          </label>
          <select id="selectRegisteredUser" class="form-control" onchange="onSelectRegisteredUser(this)">
            <option value=""><?php echo __('-- ব্যবহারকারী নির্বাচন করুন --', '-- Select User --'); ?></option>
            <?php foreach ($registeredUsers as $regUser): ?>
              <option value="<?php echo htmlspecialchars(json_encode($regUser, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP)); ?>">
                <?php echo htmlspecialchars($regUser['name']); ?> (@<?php echo htmlspecialchars($regUser['username'] ?: $regUser['user_id']); ?>) — <?php echo htmlspecialchars($regUser['email'] ?: $regUser['phone'] ?: 'No contact'); ?>
              </option>
            <?php endforeach; ?>
          </select>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('ইউজারনেম', 'Username'); ?> *</label>
            <input type="text" name="username" id="createAdminUsername" class="form-control" placeholder="moderator1" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('নাম', 'Name'); ?> *</label>
            <input type="text" name="full_name" id="createAdminFullName" class="form-control" placeholder="<?php echo __('যেমন: আব্দুল্লাহ আল মামুন', 'e.g. Abdullah Al Mamun'); ?>" required>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('প্রথম নাম', 'First Name'); ?></label>
            <input type="text" name="first_name" id="createAdminFirstName" class="form-control" placeholder="Abdullah">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('শেষ নাম', 'Last Name'); ?></label>
            <input type="text" name="last_name" id="createAdminLastName" class="form-control" placeholder="Al Mamun">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('ইমেইল', 'Email'); ?></label>
            <input type="email" name="email" id="createAdminEmail" class="form-control" placeholder="admin@deenone.top">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ফোন', 'Phone'); ?></label>
            <input type="text" name="phone" id="createAdminPhone" class="form-control" placeholder="+880 1700-000000">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('রোল', 'Role'); ?> *</label>
            <select name="role" id="createAdminRole" class="form-control">
              <option value="MODERATOR"><?php echo __('মডারেটর', 'MODERATOR'); ?></option>
              <option value="SUPERADMIN"><?php echo __('সুপার অ্যাডমিন', 'SUPERADMIN'); ?></option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></label>
            <select name="blood_group" id="createAdminBlood" class="form-control">
              <?php foreach (['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] as $bg): ?>
                <option value="<?php echo $bg; ?>" <?php echo $bg === 'O+' ? 'selected' : ''; ?>><?php echo $bg; ?></option>
              <?php endforeach; ?>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('লিঙ্গ', 'Gender'); ?></label>
            <select name="gender" id="createAdminGender" class="form-control">
              <option value="male"><?php echo __('পুরুষ', 'Male'); ?></option>
              <option value="female"><?php echo __('নারী', 'Female'); ?></option>
            </select>
          </div>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><?php echo __('পাসওয়ার্ড', 'Password'); ?> *</label>
          <input type="password" name="password" id="createAdminPassword" class="form-control" placeholder="••••••••" required minlength="6">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('createAdminModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-check"></i> <?php echo __('সংরক্ষণ করুন', 'Save'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<!-- ==============================================================================
     MODAL: EDIT ADMIN DETAILS
     ============================================================================== -->
<div class="modal-backdrop" id="editAdminModal">
  <div class="modal-window" style="max-width: 620px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-user-pen" style="color: var(--primary);"></i>
        <span><?php echo __('অ্যাডমিন সম্পাদনা', 'Edit Admin'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('editAdminModal')">&times;</button>
    </div>

    <!-- Dual Tab Switcher -->
    <div style="display: flex; border-bottom: 1px solid var(--border-color); background: var(--hover-bg); padding: 6px 16px 0;">
      <button type="button" id="tabAdminPersonalBtn" class="tab-btn active" onclick="switchAdminEditTab('personal')" style="padding: 10px 16px; border: none; background: transparent; font-weight: 700; font-size: 13px; color: var(--primary); border-bottom: 2px solid var(--primary); cursor: pointer; display: flex; align-items: center; gap: 6px;">
        <i class="fa-regular fa-user"></i> <span><?php echo __('প্রোফাইল', 'Profile'); ?></span>
      </button>
      <button type="button" id="tabAdminPasswordBtn" class="tab-btn" onclick="switchAdminEditTab('password')" style="padding: 10px 16px; border: none; background: transparent; font-weight: 700; font-size: 13px; color: var(--text-dim); border-bottom: 2px solid transparent; cursor: pointer; display: flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-lock"></i> <span><?php echo __('পাসওয়ার্ড', 'Password'); ?></span>
      </button>
    </div>

    <form method="POST" action="admin_users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="edit_admin">
      <input type="hidden" name="id" id="editAdminId" value="">

      <div class="modal-body" style="padding: 20px;">
        
        <!-- TAB 1: PERSONAL INFORMATION -->
        <div id="tabAdminPersonalContent">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ইউজারনেম', 'Username'); ?> *</label>
              <input type="text" name="username" id="editAdminUsername" class="form-control" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('নাম', 'Name'); ?> *</label>
              <input type="text" name="full_name" id="editAdminFullName" class="form-control" required>
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('প্রথম নাম', 'First Name'); ?></label>
              <input type="text" name="first_name" id="editAdminFirstName" class="form-control">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('শেষ নাম', 'Last Name'); ?></label>
              <input type="text" name="last_name" id="editAdminLastName" class="form-control">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ইমেইল', 'Email'); ?></label>
              <input type="email" name="email" id="editAdminEmail" class="form-control">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ফোন', 'Phone'); ?></label>
              <input type="text" name="phone" id="editAdminPhone" class="form-control">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('রোল', 'Role'); ?> *</label>
              <select name="role" id="editAdminRole" class="form-control">
                <option value="MODERATOR"><?php echo __('মডারেটর', 'MODERATOR'); ?></option>
                <option value="SUPERADMIN"><?php echo __('সুপার অ্যাডমিন', 'SUPERADMIN'); ?></option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></label>
              <select name="blood_group" id="editAdminBlood" class="form-control">
                <?php foreach (['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] as $bg): ?>
                  <option value="<?php echo $bg; ?>"><?php echo $bg; ?></option>
                <?php endforeach; ?>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('লিঙ্গ', 'Gender'); ?></label>
              <select name="gender" id="editAdminGender" class="form-control">
                <option value="male"><?php echo __('পুরুষ', 'Male'); ?></option>
                <option value="female"><?php echo __('নারী', 'Female'); ?></option>
              </select>
            </div>
          </div>
        </div>

        <!-- TAB 2: CHANGE PASSWORD -->
        <div id="tabAdminPasswordContent" style="display: none;">
          <div class="form-group">
            <label class="form-label"><?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?></label>
            <input type="password" name="new_password" class="form-control" placeholder="<?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?>" minlength="6">
          </div>
        </div>

      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('editAdminModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ করুন', 'Save'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<!-- ==============================================================================
     MODAL: RESET ADMIN PASSWORD
     ============================================================================== -->
<div class="modal-backdrop" id="resetAdminPassModal">
  <div class="modal-window" style="max-width: 420px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-key" style="color: #f59e0b;"></i>
        <span><?php echo __('পাসওয়ার্ড পরিবর্তন', 'Change Password'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('resetAdminPassModal')">&times;</button>
    </div>
    <form method="POST" action="admin_users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="reset_admin_password">
      <input type="hidden" name="id" id="resetAdminPassId" value="">

      <div class="modal-body">
        <p style="font-size: 13px; color: var(--text-dim); margin-bottom: 14px;" id="resetAdminPassDesc"></p>
        <div class="form-group">
          <label class="form-label"><?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?> *</label>
          <input type="text" name="new_password" class="form-control" required minlength="6" placeholder="******">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('resetAdminPassModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-check"></i> <?php echo __('সংরক্ষণ করুন', 'Save'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
function switchAdminEditTab(tab) {
  const pContent = document.getElementById('tabAdminPersonalContent');
  const passContent = document.getElementById('tabAdminPasswordContent');
  const pBtn = document.getElementById('tabAdminPersonalBtn');
  const passBtn = document.getElementById('tabAdminPasswordBtn');

  if (tab === 'personal') {
    pContent.style.display = 'block';
    passContent.style.display = 'none';
    pBtn.style.color = 'var(--primary)';
    pBtn.style.borderBottomColor = 'var(--primary)';
    passBtn.style.color = 'var(--text-dim)';
    passBtn.style.borderBottomColor = 'transparent';
  } else {
    pContent.style.display = 'none';
    passContent.style.display = 'block';
    passBtn.style.color = 'var(--primary)';
    passBtn.style.borderBottomColor = 'var(--primary)';
    pBtn.style.color = 'var(--text-dim)';
    pBtn.style.borderBottomColor = 'transparent';
  }
}

function openEditAdminModal(adm) {
  document.getElementById('editAdminId').value = adm.id || '';
  document.getElementById('editAdminUsername').value = adm.username || '';
  document.getElementById('editAdminFullName').value = adm.full_name || '';
  document.getElementById('editAdminFirstName').value = adm.first_name || '';
  document.getElementById('editAdminLastName').value = adm.last_name || '';
  document.getElementById('editAdminEmail').value = adm.email || '';
  document.getElementById('editAdminPhone').value = adm.phone || '';
  document.getElementById('editAdminRole').value = adm.role || 'MODERATOR';
  document.getElementById('editAdminBlood').value = adm.blood_group || 'O+';
  document.getElementById('editAdminGender').value = adm.gender || 'male';
  switchAdminEditTab('personal');
  openModal('editAdminModal');
}

function openResetAdminPassModal(id, username) {
  document.getElementById('resetAdminPassId').value = id;
  document.getElementById('resetAdminPassDesc').textContent = 'অ্যাডমিন: @' + username + ' (ID #' + id + ')';
  openModal('resetAdminPassModal');
}

function onSelectRegisteredUser(select) {
  if (!select || !select.value) return;
  try {
    const user = JSON.parse(select.value);
    if (!user) return;
    
    document.getElementById('createAdminFullName').value = user.name || '';
    
    // Split name into first and last name if possible
    const nameParts = (user.name || '').trim().split(' ');
    if (nameParts.length > 1) {
      document.getElementById('createAdminFirstName').value = nameParts[0];
      document.getElementById('createAdminLastName').value = nameParts.slice(1).join(' ');
    } else {
      document.getElementById('createAdminFirstName').value = user.name || '';
      document.getElementById('createAdminLastName').value = '';
    }
    
    // Suggested username
    let uname = user.username || '';
    if (!uname && user.email) {
      uname = user.email.split('@')[0].replace(/[^a-zA-Z0-9_]/g, '');
    }
    if (!uname) {
      uname = 'admin_' + (user.user_id ? user.user_id.replace('usr_', '') : 'user');
    }
    document.getElementById('createAdminUsername').value = uname;
    
    document.getElementById('createAdminEmail').value = user.email || '';
    document.getElementById('createAdminPhone').value = user.phone || '';
    if (user.blood_group) {
      document.getElementById('createAdminBlood').value = user.blood_group;
    }
    const isFemale = (user.gender === 'female' || user.avatar === 'avatar_2');
    document.getElementById('createAdminGender').value = isFemale ? 'female' : 'male';
  } catch (e) {
    console.error('Failed to parse user info', e);
  }
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
