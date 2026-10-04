<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - CORE TEAM & DEVELOPER MANAGEMENT (টিম)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure table exists
$pdo->exec("CREATE TABLE IF NOT EXISTS `app_team_members` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `name_bn` VARCHAR(150) NOT NULL,
  `name_en` VARCHAR(150) NOT NULL,
  `role_bn` VARCHAR(150) NOT NULL,
  `role_en` VARCHAR(150) NOT NULL,
  `avatar_url` VARCHAR(255) DEFAULT NULL,
  `github_url` VARCHAR(255) DEFAULT NULL,
  `linkedin_url` VARCHAR(255) DEFAULT NULL,
  `website_url` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(150) DEFAULT NULL,
  `display_order` INT UNSIGNED NOT NULL DEFAULT 1,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_order` (`display_order`),
  INDEX `idx_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Seed if empty
$chkTeam = $pdo->query("SELECT COUNT(*) FROM app_team_members")->fetchColumn();
if ($chkTeam == 0) {
    $pdo->exec("INSERT INTO `app_team_members` (`id`, `name_bn`, `name_en`, `role_bn`, `role_en`, `avatar_url`, `github_url`, `linkedin_url`, `website_url`, `email`, `display_order`, `is_active`) VALUES
    (1, 'রিয়াদ মনির', 'Riad Monir', 'প্রতিষ্ঠাতা ও প্রধান ডেভেলপার', 'Founder & Lead Developer', NULL, 'https://github.com/riadmonir', 'https://linkedin.com/in/riadmonir', 'https://deenone.top', 'support@deenone.top', 1, 1)");
}

// Handle Form Submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন মেয়াদোত্তীর্ণ বা অকার্যকর।', 'Invalid or expired security token.'));
        redirect('team.php');
    }

    if ($action === 'create_team_member' || $action === 'update_team_member') {
        $id = (int)($_POST['member_id'] ?? 0);
        $nameBn = trim($_POST['name_bn'] ?? '');
        $nameEn = trim($_POST['name_en'] ?? '');
        $roleBn = trim($_POST['role_bn'] ?? '');
        $roleEn = trim($_POST['role_en'] ?? '');
        $github = trim($_POST['github_url'] ?? '');
        $linkedin = trim($_POST['linkedin_url'] ?? '');
        $website = trim($_POST['website_url'] ?? '');
        $email = trim($_POST['email'] ?? '');
        $displayOrder = (int)($_POST['display_order'] ?? 1);
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($nameBn) || empty($roleBn)) {
            setFlash('danger', __('ডেভেলপারের বাংলা নাম ও পদবী আবশ্যক!', 'Developer name and role in Bengali are required!'));
            redirect('team.php');
        }

        // Avatar upload handling
        $avatarUrl = null;
        if (!empty($_FILES['avatar_file']['name']) && $_FILES['avatar_file']['error'] === UPLOAD_ERR_OK) {
            $allowedExts = ['jpg', 'jpeg', 'png', 'webp'];
            $fileExt = strtolower(pathinfo($_FILES['avatar_file']['name'], PATHINFO_EXTENSION));
            if (in_array($fileExt, $allowedExts)) {
                $newFileName = 'avatar_' . time() . '_' . rand(1000, 9999) . '.' . $fileExt;
                $targetDir = __DIR__ . '/assets/uploads/';
                if (!file_exists($targetDir)) {
                    @mkdir($targetDir, 0755, true);
                }
                $targetPath = $targetDir . $newFileName;
                if (move_uploaded_file($_FILES['avatar_file']['tmp_name'], $targetPath)) {
                    $avatarUrl = 'assets/uploads/' . $newFileName;
                }
            }
        }

        try {
            if ($action === 'create_team_member') {
                $stmt = $pdo->prepare("INSERT INTO app_team_members 
                    (name_bn, name_en, role_bn, role_en, avatar_url, github_url, linkedin_url, website_url, email, display_order, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                $stmt->execute([
                    $nameBn, $nameEn, $roleBn, $roleEn, $avatarUrl,
                    $github, $linkedin, $website, $email, $displayOrder, $isActive
                ]);
                logAdminActivity($pdo, $_SESSION['admin_id'] ?? 1, 'CREATE_TEAM_MEMBER', 'app_team_members', (string)$pdo->lastInsertId(), 'Added ' . $nameBn);
                setFlash('success', __('নতুন টিম সদস্য সফলভাবে যোগ করা হয়েছে!', 'New team member added successfully!'));
            } else {
                if ($avatarUrl !== null) {
                    $stmt = $pdo->prepare("UPDATE app_team_members SET
                        name_bn = ?, name_en = ?, role_bn = ?, role_en = ?,
                        avatar_url = ?, github_url = ?, linkedin_url = ?,
                        website_url = ?, email = ?, display_order = ?, is_active = ?
                        WHERE id = ?");
                    $stmt->execute([
                        $nameBn, $nameEn, $roleBn, $roleEn,
                        $avatarUrl, $github, $linkedin,
                        $website, $email, $displayOrder, $isActive, $id
                    ]);
                } else {
                    $stmt = $pdo->prepare("UPDATE app_team_members SET
                        name_bn = ?, name_en = ?, role_bn = ?, role_en = ?,
                        github_url = ?, linkedin_url = ?,
                        website_url = ?, email = ?, display_order = ?, is_active = ?
                        WHERE id = ?");
                    $stmt->execute([
                        $nameBn, $nameEn, $roleBn, $roleEn,
                        $github, $linkedin,
                        $website, $email, $displayOrder, $isActive, $id
                    ]);
                }
                logAdminActivity($pdo, $_SESSION['admin_id'] ?? 1, 'UPDATE_TEAM_MEMBER', 'app_team_members', (string)$id, 'Updated ' . $nameBn);
                setFlash('success', __('টিম সদস্যের তথ্য সফলভাবে আপডেট করা হয়েছে!', 'Team member updated successfully!'));
            }
        } catch (Exception $e) {
            setFlash('danger', __('অপারেশন ব্যর্থ: ', 'Operation failed: ') . $e->getMessage());
        }
        redirect('team.php');
    }
}

// Handle GET Actions
if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM app_team_members WHERE id = ?");
        $stmt->execute([$id]);
        logAdminActivity($pdo, $_SESSION['admin_id'] ?? 1, 'DELETE_TEAM_MEMBER', 'app_team_members', (string)$id, 'Deleted member id ' . $id);
        setFlash('success', __('সদস্য তালিকা থেকে মুছে ফেলা হয়েছে!', 'Team member deleted successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছে ফেলা সম্ভব হয়নি: ', 'Delete failed: ') . $e->getMessage());
    }
    redirect('team.php');
}

if (isset($_GET['toggle'])) {
    $id = (int)$_GET['toggle'];
    try {
        $stmt = $pdo->prepare("UPDATE app_team_members SET is_active = 1 - is_active WHERE id = ?");
        $stmt->execute([$id]);
        logAdminActivity($pdo, $_SESSION['admin_id'] ?? 1, 'TOGGLE_TEAM_MEMBER', 'app_team_members', (string)$id, 'Toggled status');
        setFlash('success', __('সদস্যের দৃশ্যমান স্ট্যাটাস পরিবর্তন করা হয়েছে!', 'Member status updated successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('স্ট্যাটাস পরিবর্তন ব্যর্থ: ', 'Status toggle failed: ') . $e->getMessage());
    }
    redirect('team.php');
}

// Fetch All Team Members
$teamStmt = $pdo->query("SELECT * FROM app_team_members ORDER BY display_order ASC, id ASC");
$teamMembers = $teamStmt->fetchAll();

$pageTitle = __('টিম', 'Team');
$activeNav = 'team';
require_once __DIR__ . '/header.php';
?>

<div class="policy-card">
  <div class="policy-header" style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 14px;">
    <div>
      <div class="policy-title-row">
        <span class="policy-dot"></span>
        <span class="policy-title"><?php echo __('টিম সদস্য', 'Team Members'); ?></span>
        <span class="badge bg-mint text-dark fw-bold" style="background: var(--primary); color: #fff; font-size: 11.5px; padding: 3px 8px; border-radius: 12px;">
          <?php echo toLangNum(count($teamMembers)); ?> <?php echo __('জন সদস্য', 'Members'); ?>
        </span>
      </div>
    </div>
    <div>
      <button type="button" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 8px 18px; font-size: 13px;" data-bs-toggle="modal" data-bs-target="#addMemberModal">
        <i class="fa-solid fa-user-plus"></i> <span><?php echo __('সদস্য যোগ করুন', 'Add Member'); ?></span>
      </button>
    </div>
  </div>

  <!-- Developer Profile Cards Grid -->
  <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 18px; margin-top: 20px;">
    <?php if (empty($teamMembers)): ?>
      <div style="grid-column: 1 / -1; text-align: center; padding: 40px; color: var(--text-muted);">
        <i class="fa-solid fa-user-group" style="font-size: 36px; margin-bottom: 12px; opacity: 0.5;"></i>
        <p><?php echo __('এখনো কোনো ডেভেলপার বা সদস্য যোগ করা হয়নি।', 'No developers or team members added yet.'); ?></p>
      </div>
    <?php else: ?>
      <?php foreach ($teamMembers as $m): ?>
        <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 18px; display: flex; flex-direction: column; justify-content: space-between; transition: all 0.25s ease;">
          <div>
            <!-- Top Row: Avatar & Details -->
            <div style="display: flex; gap: 14px; align-items: center; margin-bottom: 14px;">
              <div style="width: 52px; height: 52px; border-radius: 50%; overflow: hidden; border: 2px solid var(--primary); background: var(--bg-card); display: flex; align-items: center; justify-content: center; flex-shrink: 0; box-shadow: 0 2px 8px var(--primary-glow);">
                <?php if (!empty($m['avatar_url']) && file_exists(__DIR__ . '/' . $m['avatar_url'])): ?>
                  <img src="<?php echo htmlspecialchars($m['avatar_url']); ?>?v=<?php echo time(); ?>" alt="Avatar" style="width: 100%; height: 100%; object-fit: cover;">
                <?php elseif (!empty($m['avatar_url']) && filter_var($m['avatar_url'], FILTER_VALIDATE_URL)): ?>
                  <img src="<?php echo htmlspecialchars($m['avatar_url']); ?>" alt="Avatar" style="width: 100%; height: 100%; object-fit: cover;">
                <?php else: ?>
                  <span style="font-weight: 800; font-size: 16px; color: var(--primary);"><?php echo mb_substr($m['name_bn'], 0, 1, 'UTF-8'); ?></span>
                <?php endif; ?>
              </div>
              <div style="flex: 1; min-width: 0;">
                <h4 style="font-size: 15px; font-weight: 800; color: var(--text-heading); margin-bottom: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                  <?php echo htmlspecialchars(isEn() ? ($m['name_en'] ?: $m['name_bn']) : $m['name_bn']); ?>
                </h4>
                <p style="font-size: 12.5px; color: var(--primary); font-weight: 600; margin-bottom: 4px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                  <?php echo htmlspecialchars(isEn() ? ($m['role_en'] ?: $m['role_bn']) : $m['role_bn']); ?>
                </p>
                <div style="display: flex; align-items: center; gap: 6px;">
                  <?php if ($m['is_active']): ?>
                    <span style="font-size: 10.5px; font-weight: 700; color: #10b981; background: rgba(16, 185, 129, 0.12); padding: 2px 8px; border-radius: 10px; border: 1px solid rgba(16, 185, 129, 0.25);">
                      ● <?php echo __('সক্রিয়', 'Active'); ?>
                    </span>
                  <?php else: ?>
                    <span style="font-size: 10.5px; font-weight: 700; color: #94a3b8; background: rgba(148, 163, 184, 0.12); padding: 2px 8px; border-radius: 10px; border: 1px solid rgba(148, 163, 184, 0.25);">
                      ○ <?php echo __('নিষ্ক্রিয়', 'Inactive'); ?>
                    </span>
                  <?php endif; ?>
                  <span style="font-size: 11px; color: var(--text-dim);">#<?php echo toLangNum($m['display_order']); ?></span>
                </div>
              </div>
            </div>

            <!-- Social Links Row -->
            <div style="display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 14px; padding-top: 8px; border-top: 1px dashed var(--border-color);">
              <?php if (!empty($m['github_url'])): ?>
                <a href="<?php echo htmlspecialchars($m['github_url']); ?>" target="_blank" class="btn-topbar-pill" style="padding: 3px 9px; font-size: 11.5px;" title="GitHub">
                  <i class="fa-brands fa-github"></i> <span>GitHub</span>
                </a>
              <?php endif; ?>
              <?php if (!empty($m['linkedin_url'])): ?>
                <a href="<?php echo htmlspecialchars($m['linkedin_url']); ?>" target="_blank" class="btn-topbar-pill" style="padding: 3px 9px; font-size: 11.5px; color: #0284c7;" title="LinkedIn">
                  <i class="fa-brands fa-linkedin"></i> <span>LinkedIn</span>
                </a>
              <?php endif; ?>
              <?php if (!empty($m['website_url'])): ?>
                <a href="<?php echo htmlspecialchars($m['website_url']); ?>" target="_blank" class="btn-topbar-pill" style="padding: 3px 9px; font-size: 11.5px;" title="Website">
                  <i class="fa-solid fa-globe"></i> <span>Web</span>
                </a>
              <?php endif; ?>
              <?php if (!empty($m['email'])): ?>
                <a href="mailto:<?php echo htmlspecialchars($m['email']); ?>" class="btn-topbar-pill" style="padding: 3px 9px; font-size: 11.5px; color: #f59e0b;" title="Email">
                  <i class="fa-solid fa-envelope"></i> <span>Email</span>
                </a>
              <?php endif; ?>
            </div>
          </div>

          <!-- Card Bottom Actions -->
          <div style="display: flex; justify-content: space-between; align-items: center; padding-top: 12px; border-top: 1px solid var(--border-color);">
            <button type="button" class="btn-topbar-pill" style="padding: 5px 12px; font-size: 12px;" onclick="editMember(<?php echo htmlspecialchars(json_encode($m)); ?>)">
              <i class="fa-solid fa-pen"></i> <span><?php echo __('এডিট', 'Edit'); ?></span>
            </button>
            
            <div style="display: flex; gap: 8px;">
              <a href="team.php?toggle=<?php echo $m['id']; ?>" class="btn-topbar-pill" style="padding: 5px 10px; font-size: 12px;" title="<?php echo __('স্ট্যাটাস পরিবর্তন করুন', 'Toggle Status'); ?>">
                <i class="fa-solid fa-power-off" style="color: <?php echo $m['is_active'] ? '#10b981' : '#94a3b8'; ?>;"></i>
              </a>
              <?php if ($m['id'] != 1): ?>
                <a href="team.php?delete=<?php echo $m['id']; ?>" class="btn-topbar-pill text-danger" style="padding: 5px 10px; font-size: 12px;" onclick="return confirm('<?php echo __('আপনি কি নিশ্চিত যে এই সদস্যকে মুছে ফেলতে চান?', 'Are you sure you want to delete this member?'); ?>')" title="<?php echo __('মুছুন', 'Delete'); ?>">
                  <i class="fa-solid fa-trash"></i>
                </a>
              <?php endif; ?>
            </div>
          </div>
        </div>
      <?php endforeach; ?>
    <?php endif; ?>
  </div>
</div>

<!-- ========================================================================= -->
<!-- MODAL: ADD TEAM MEMBER (With Smooth Max-Height & Vertical Scroll) -->
<!-- ========================================================================= -->
<div class="modal fade" id="addMemberModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
    <div class="modal-content" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); box-shadow: var(--shadow-lg); max-height: 90vh;">
      <form method="POST" action="team.php" enctype="multipart/form-data">
        <input type="hidden" name="action" value="create_team_member">
        <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

        <div class="modal-header" style="border-bottom: 1px solid var(--border-color); padding: 16px 20px;">
          <h5 class="modal-title" style="font-size: 16px; font-weight: 800; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-user-plus" style="color: var(--primary);"></i> <?php echo __('সদস্য যোগ করুন', 'Add Member'); ?>
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" style="filter: invert(1) grayscale(100%) brightness(200%);"></button>
        </div>
        
        <div class="modal-body" style="padding: 20px; max-height: calc(85vh - 130px); overflow-y: auto;">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নাম', 'Bengali Name'); ?> *</label>
              <input type="text" name="name_bn" class="form-control" required placeholder="উদাঃ রিয়াদ মনির">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নাম', 'English Name'); ?> *</label>
              <input type="text" name="name_en" class="form-control" required placeholder="e.g. Riad Monir">
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা পদবী', 'Bengali Role'); ?> *</label>
              <input type="text" name="role_bn" class="form-control" placeholder="উদাঃ প্রতিষ্ঠাতা ও প্রধান ডেভেলপার" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি পদবী', 'English Role'); ?> *</label>
              <input type="text" name="role_en" class="form-control" placeholder="e.g. Founder & Lead Developer" required>
            </div>
          </div>

          <div class="form-group" style="margin-top: 14px;">
            <label class="form-label"><?php echo __('প্রোফাইল ছবি আপলোড', 'Profile Photo Upload'); ?></label>
            <input type="file" name="avatar_file" class="form-control" accept="image/*">
            <small class="text-muted" style="font-size: 11px; display: block; margin-top: 4px;"><?php echo __('ফাঁকা রাখলে স্বয়ংক্রিয়ভাবে DeenOne-এর অফিশিয়াল লোগো ব্যবহৃত হবে।', 'Default official logo is used if left blank.'); ?></small>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('গিটহাব লিংক', 'GitHub URL'); ?></label>
              <input type="url" name="github_url" class="form-control" placeholder="https://github.com/username">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('লিংকডইন লিংক', 'LinkedIn URL'); ?></label>
              <input type="url" name="linkedin_url" class="form-control" placeholder="https://linkedin.com/in/username">
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ওয়েবসাইট বা পোর্টফোলিও লিংক', 'Website / Portfolio'); ?></label>
              <input type="url" name="website_url" class="form-control" placeholder="https://example.com">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইমেইল ঠিকানা', 'Email Address'); ?></label>
              <input type="email" name="email" class="form-control" placeholder="developer@deenone.top">
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ডিসপ্লে ক্রম', 'Display Order'); ?></label>
              <input type="number" name="display_order" class="form-control" value="2" min="1">
            </div>
            <div class="form-group" style="display: flex; align-items: center; padding-top: 24px;">
              <label class="switch-label">
                <label class="switch">
                  <input type="checkbox" name="is_active" value="1" checked>
                  <span class="slider"></span>
                </label>
                <span style="font-weight: 700; font-size: 13.5px;"><?php echo __('প্রোফাইল সক্রিয় রাখুন', 'Keep Profile Active'); ?></span>
              </label>
            </div>
          </div>
        </div>

        <div class="modal-footer" style="border-top: 1px solid var(--border-color); padding: 14px 20px; display: flex; justify-content: flex-end; gap: 10px;">
          <button type="button" class="btn-topbar-pill" data-bs-dismiss="modal"><?php echo __('বাতিল', 'Cancel'); ?></button>
          <button type="submit" class="btn-save-settings" style="padding: 8px 20px; font-size: 13px;">
            ✓ <?php echo __('সদস্য যোগ করুন', 'Add Member'); ?>
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- ========================================================================= -->
<!-- MODAL: EDIT TEAM MEMBER (With Smooth Max-Height & Vertical Scroll) -->
<!-- ========================================================================= -->
<div class="modal fade" id="editMemberModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
    <div class="modal-content" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); box-shadow: var(--shadow-lg); max-height: 90vh;">
      <form method="POST" action="team.php" enctype="multipart/form-data">
        <input type="hidden" name="action" value="update_team_member">
        <input type="hidden" name="member_id" id="editMemberId">
        <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

        <div class="modal-header" style="border-bottom: 1px solid var(--border-color); padding: 16px 20px;">
          <h5 class="modal-title" style="font-size: 16px; font-weight: 800; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-user-pen" style="color: var(--primary);"></i> <?php echo __('সদস্য সম্পাদনা', 'Edit Member'); ?>
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" style="filter: invert(1) grayscale(100%) brightness(200%);"></button>
        </div>
        
        <div class="modal-body" style="padding: 20px; max-height: calc(85vh - 130px); overflow-y: auto;">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নাম', 'Bengali Name'); ?> *</label>
              <input type="text" name="name_bn" id="editNameBn" class="form-control" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নাম', 'English Name'); ?> *</label>
              <input type="text" name="name_en" id="editNameEn" class="form-control" required>
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা পদবী', 'Bengali Role'); ?> *</label>
              <input type="text" name="role_bn" id="editRoleBn" class="form-control" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি পদবী', 'English Role'); ?> *</label>
              <input type="text" name="role_en" id="editRoleEn" class="form-control" required>
            </div>
          </div>

          <div class="form-group" style="margin-top: 14px;">
            <label class="form-label"><?php echo __('নতুন ছবি আপলোড', 'Upload New Photo'); ?></label>
            <input type="file" name="avatar_file" class="form-control" accept="image/*">
            <small class="text-muted" style="font-size: 11px; display: block; margin-top: 4px;"><?php echo __('বর্তমান ছবি অপরিবর্তিত রাখতে ফাঁকা রাখুন।', 'Leave blank to retain current photo.'); ?></small>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('গিটহাব লিংক', 'GitHub URL'); ?></label>
              <input type="url" name="github_url" id="editGithub" class="form-control">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('লিংকডইন লিংক', 'LinkedIn URL'); ?></label>
              <input type="url" name="linkedin_url" id="editLinkedin" class="form-control">
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ওয়েবসাইট বা পোর্টফোলিও লিংক', 'Website / Portfolio'); ?></label>
              <input type="url" name="website_url" id="editWebsite" class="form-control">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইমেইল ঠিকানা', 'Email Address'); ?></label>
              <input type="email" name="email" id="editEmail" class="form-control">
            </div>
          </div>

          <div class="form-row" style="margin-top: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ডিসপ্লে ক্রম', 'Display Order'); ?></label>
              <input type="number" name="display_order" id="editDisplayOrder" class="form-control" min="1">
            </div>
            <div class="form-group" style="display: flex; align-items: center; padding-top: 24px;">
              <label class="switch-label">
                <label class="switch">
                  <input type="checkbox" name="is_active" id="editActiveSwitch" value="1">
                  <span class="slider"></span>
                </label>
                <span style="font-weight: 700; font-size: 13.5px;"><?php echo __('প্রোফাইল সক্রিয় রাখুন', 'Keep Profile Active'); ?></span>
              </label>
            </div>
          </div>
        </div>

        <div class="modal-footer" style="border-top: 1px solid var(--border-color); padding: 14px 20px; display: flex; justify-content: flex-end; gap: 10px;">
          <button type="button" class="btn-topbar-pill" data-bs-dismiss="modal"><?php echo __('বাতিল', 'Cancel'); ?></button>
          <button type="submit" class="btn-save-settings" style="padding: 8px 20px; font-size: 13px;">
            ✓ <?php echo __('আপডেট সংরক্ষণ করুন', 'Save Updates'); ?>
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
function editMember(member) {
  document.getElementById('editMemberId').value = member.id;
  document.getElementById('editNameBn').value = member.name_bn || '';
  document.getElementById('editNameEn').value = member.name_en || '';
  document.getElementById('editRoleBn').value = member.role_bn || '';
  document.getElementById('editRoleEn').value = member.role_en || '';
  document.getElementById('editGithub').value = member.github_url || '';
  document.getElementById('editLinkedin').value = member.linkedin_url || '';
  document.getElementById('editWebsite').value = member.website_url || '';
  document.getElementById('editEmail').value = member.email || '';
  document.getElementById('editDisplayOrder').value = member.display_order || 1;
  document.getElementById('editActiveSwitch').checked = (parseInt(member.is_active) === 1);

  var modal = new bootstrap.Modal(document.getElementById('editMemberModal'));
  modal.show();
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
