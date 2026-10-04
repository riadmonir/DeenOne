<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - DEENONE COMMUNITY MANAGEMENT (FEED, COMMENTS, REPORTS)
 * Fully compliant with Rule 5 Dual-Language and Rule 13 Circular Icon Policy
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure all community tables exist
$pdo->exec("CREATE TABLE IF NOT EXISTS `community_posts` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `user_id` VARCHAR(100) DEFAULT NULL,
  `author_name` VARCHAR(150) NOT NULL,
  `author_avatar` VARCHAR(100) DEFAULT 'avatar_1',
  `content` TEXT NOT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `likes_count` INT UNSIGNED NOT NULL DEFAULT 0,
  `comments_count` INT UNSIGNED NOT NULL DEFAULT 0,
  `is_pinned` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_comm_pin` (`is_pinned`),
  INDEX `idx_comm_created` (`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

$pdo->exec("CREATE TABLE IF NOT EXISTS `community_comments` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT UNSIGNED NOT NULL,
  `user_id` VARCHAR(100) DEFAULT NULL,
  `author_name` VARCHAR(100) NOT NULL,
  `author_avatar` VARCHAR(80) DEFAULT 'avatar_1',
  `comment_text` TEXT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_comment_post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

$pdo->exec("CREATE TABLE IF NOT EXISTS `community_reports` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT UNSIGNED DEFAULT NULL,
  `comment_id` INT UNSIGNED DEFAULT NULL,
  `reporter_user_id` VARCHAR(100) DEFAULT NULL,
  `reporter_name` VARCHAR(100) DEFAULT 'বেনামী ইউজার',
  `reason` VARCHAR(255) NOT NULL,
  `details` TEXT DEFAULT NULL,
  `status` ENUM('PENDING','RESOLVED','DISMISSED') NOT NULL DEFAULT 'PENDING',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_rep_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Handle GET CSV Export
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    $tabExport = $_GET['tab'] ?? 'feed';
    header('Content-Type: text/csv; charset=utf-8');
    header('Content-Disposition: attachment; filename="deenone_community_' . $tabExport . '_' . date('Ymd_His') . '.csv"');
    $output = fopen('php://output', 'w');
    fprintf($output, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    if ($tabExport === 'comments') {
        fputcsv($output, ['ID', 'Post ID', 'User ID', 'Author Name', 'Comment Text', 'Created At']);
        $stmt = $pdo->query("SELECT id, post_id, user_id, author_name, comment_text, created_at FROM community_comments ORDER BY id DESC");
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            fputcsv($output, [$row['id'], $row['post_id'], $row['user_id'], $row['author_name'], $row['comment_text'], $row['created_at']]);
        }
    } else {
        fputcsv($output, ['ID', 'User ID', 'Author Name', 'Author Avatar', 'Content', 'Image URL', 'Likes Count', 'Comments Count', 'Is Pinned', 'Created At']);
        $stmt = $pdo->query("SELECT id, user_id, author_name, author_avatar, content, image_url, likes_count, comments_count, is_pinned, created_at FROM community_posts ORDER BY id DESC");
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            fputcsv($output, [$row['id'], $row['user_id'], $row['author_name'], $row['author_avatar'], $row['content'], $row['image_url'], $row['likes_count'], $row['comments_count'], $row['is_pinned'], $row['created_at']]);
        }
    }
    fclose($output);
    exit;
}

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';
    $currentTab = $_POST['current_tab'] ?? 'feed';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Security token invalid.'));
        redirect('community.php?tab=' . urlencode($currentTab));
    }

    // CSV Import
    if ($action === 'import_csv') {
        if (isset($_FILES['csv_file']) && $_FILES['csv_file']['error'] === UPLOAD_ERR_OK) {
            $file = $_FILES['csv_file']['tmp_name'];
            $handle = fopen($file, 'r');
            if ($handle !== false) {
                $header = fgetcsv($handle, 4000, ",");
                $imported = 0;
                $stmt = $pdo->prepare("INSERT INTO community_posts (author_name, author_avatar, content, image_url, likes_count, comments_count, is_pinned, created_at) 
                                       VALUES (?, ?, ?, ?, ?, ?, ?, NOW())");
                while (($data = fgetcsv($handle, 4000, ",")) !== false) {
                    if (count($data) >= 3) {
                        $author = !empty($data[2]) ? $data[2] : (!empty($data[1]) ? $data[1] : 'DeenOne Member');
                        $content = !empty($data[4]) ? $data[4] : (!empty($data[2]) ? $data[2] : $data[0]);
                        $avatar = !empty($data[3]) ? $data[3] : 'avatar_1';
                        $img = !empty($data[5]) ? $data[5] : null;
                        $likes = isset($data[6]) ? (int)$data[6] : 0;
                        $comments = isset($data[7]) ? (int)$data[7] : 0;
                        $pinned = isset($data[8]) ? (int)$data[8] : 0;

                        if (!empty(trim($content))) {
                            $stmt->execute([$author, $avatar, $content, $img, $likes, $comments, $pinned]);
                            $imported++;
                        }
                    }
                }
                fclose($handle);
                logAdminAction($pdo, 'IMPORT_COMMUNITY_CSV', 'community_posts', null, "Imported $imported posts from CSV");
                setFlash('success', sprintf(__('সফলভাবে %d টি কমিউনিটি পোস্ট ইমপোর্ট করা হয়েছে!', 'Successfully imported %d community posts!'), $imported));
            } else {
                setFlash('danger', __('CSV ফাইল রিড করতে ব্যর্থ।', 'Failed to read CSV file.'));
            }
        } else {
            setFlash('danger', __('সঠিক CSV ফাইল সিলেক্ট করুন।', 'Please select a valid CSV file.'));
        }
        redirect('community.php?tab=' . urlencode($currentTab));
    }

    // 1. Create Official Admin Post
    if ($action === 'create_admin_post') {
        $content = trim($_POST['content'] ?? '');
        $isPinned = isset($_POST['is_pinned']) ? 1 : 0;

        if (!empty($content)) {
            try {
                $authorName = __('দীন ওয়ান অফিশিয়াল টিম', 'DeenOne Official Team');
                $stmt = $pdo->prepare("INSERT INTO community_posts (user_id, author_name, author_avatar, content, likes_count, comments_count, is_pinned) 
                                       VALUES ('admin_official', ?, 'avatar_1', ?, 0, 0, ?)");
                $stmt->execute([$authorName, $content, $isPinned]);
                logAdminAction($pdo, 'CREATE_COMMUNITY_POST', 'community_posts', (string)$pdo->lastInsertId(), 'Official Post');
                setFlash('success', __('অফিশিয়াল পোস্ট সফলভাবে প্রকাশিত হয়েছে!', 'Official post published successfully!'));
            } catch (Exception $e) {
                setFlash('danger', __('পোস্ট করতে ব্যর্থ: ', 'Failed to publish post: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=feed');
    }

    // 2. Edit Post Content
    elseif ($action === 'edit_post') {
        $id = (int)($_POST['id'] ?? 0);
        $title = trim($_POST['title'] ?? '');
        $content = trim($_POST['content'] ?? '');
        $category = trim($_POST['category'] ?? '');
        $isPinned = isset($_POST['is_pinned']) ? 1 : 0;

        if ($id > 0 && !empty($content)) {
            try {
                $sql = "UPDATE community_posts SET content = ?, is_pinned = ?";
                $params = [$content, $isPinned];
                if (!empty($title)) {
                    $sql .= ", title = ?";
                    $params[] = $title;
                }
                if (!empty($category)) {
                    $sql .= ", category = ?";
                    $params[] = $category;
                }
                $sql .= " WHERE id = ?";
                $params[] = $id;

                $stmt = $pdo->prepare($sql);
                $stmt->execute($params);
                logAdminAction($pdo, 'EDIT_COMMUNITY_POST', 'community_posts', (string)$id, 'Edited post content');
                setFlash('success', __('পোস্টের বিবরণ সফলভাবে আপডেট করা হয়েছে।', 'Post updated successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('আপডেট ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=feed');
    }

    // 3. Delete Post
    elseif ($action === 'delete_post') {
        $id = (int)($_POST['id'] ?? 0);
        if ($id > 0) {
            try {
                $stmt = $pdo->prepare("DELETE FROM community_posts WHERE id = ?");
                $stmt->execute([$id]);
                // Delete associated comments & reports
                $pdo->prepare("DELETE FROM community_comments WHERE post_id = ?")->execute([$id]);
                $pdo->prepare("DELETE FROM community_reports WHERE post_id = ?")->execute([$id]);
                logAdminAction($pdo, 'DELETE_COMMUNITY_POST', 'community_posts', (string)$id, 'Deleted post & associated data');
                setFlash('success', __('পোস্টটি এবং এর সমস্ত কমেন্ট সফলভাবে মুছে ফেলা হয়েছে।', 'Post and associated comments deleted successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('মুছে ফেলতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=feed');
    }

    // 4. Toggle Pin
    elseif ($action === 'toggle_pin') {
        $id = (int)($_POST['id'] ?? 0);
        if ($id > 0) {
            try {
                $pdo->prepare("UPDATE community_posts SET is_pinned = IF(is_pinned=1, 0, 1) WHERE id = ?")->execute([$id]);
                setFlash('success', __('পোস্টের পিন স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Pin status toggled successfully.'));
            } catch (Exception $e) {}
        }
        redirect('community.php?tab=feed');
    }

    // 5. Delete Comment
    elseif ($action === 'delete_comment') {
        $commentId = (int)($_POST['comment_id'] ?? 0);
        $postId = (int)($_POST['post_id'] ?? 0);
        if ($commentId > 0) {
            try {
                $pdo->prepare("DELETE FROM community_comments WHERE id = ?")->execute([$commentId]);
                if ($postId > 0) {
                    $pdo->prepare("UPDATE community_posts SET comments_count = GREATEST(0, comments_count - 1) WHERE id = ?")->execute([$postId]);
                }
                logAdminAction($pdo, 'DELETE_COMMUNITY_COMMENT', 'community_comments', (string)$commentId, 'Deleted comment');
                setFlash('success', __('কমেন্টটি সফলভাবে মুছে ফেলা হয়েছে।', 'Comment deleted successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('কমেন্ট মুছতে ব্যর্থ: ', 'Failed to delete comment: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=comments' . ($postId > 0 ? '&post_id=' . $postId : ''));
    }

    // 6. Add Admin Comment
    elseif ($action === 'add_admin_comment') {
        $postId = (int)($_POST['post_id'] ?? 0);
        $commentText = trim($_POST['comment_text'] ?? '');
        if ($postId > 0 && !empty($commentText)) {
            try {
                $authorName = __('দীন ওয়ান অফিশিয়াল টিম', 'DeenOne Official Team');
                $stmt = $pdo->prepare("INSERT INTO community_comments (post_id, user_id, author_name, author_avatar, comment_text) VALUES (?, 'admin_official', ?, 'avatar_1', ?)");
                $stmt->execute([$postId, $authorName, $commentText]);
                $pdo->prepare("UPDATE community_posts SET comments_count = comments_count + 1 WHERE id = ?")->execute([$postId]);
                logAdminAction($pdo, 'ADD_ADMIN_COMMENT', 'community_comments', (string)$pdo->lastInsertId(), "Post #$postId Comment");
                setFlash('success', __('অফিশিয়াল কমেন্ট সফলভাবে যুক্ত করা হয়েছে।', 'Official comment added successfully.'));
            } catch (Exception $e) {
                setFlash('danger', __('কমেন্ট যোগ করতে ব্যর্থ: ', 'Failed to add comment: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=comments&post_id=' . $postId);
    }

    // 7. Resolve or Dismiss Report
    elseif ($action === 'resolve_report') {
        $repId = (int)($_POST['report_id'] ?? 0);
        $status = $_POST['status'] ?? 'RESOLVED';
        if ($repId > 0 && in_array($status, ['RESOLVED', 'DISMISSED', 'PENDING'])) {
            try {
                $pdo->prepare("UPDATE community_reports SET status = ? WHERE id = ?")->execute([$status, $repId]);
                logAdminAction($pdo, 'UPDATE_COMMUNITY_REPORT', 'community_reports', (string)$repId, "Report status: $status");
                setFlash('success', __('রিপোর্টের স্ট্যাটাস আপডেট করা হয়েছে।', 'Report status updated successfully.'));
            } catch (Exception $e) {}
        }
        redirect('community.php?tab=reports');
    }

    // 8. Delete Reported Post & Resolve Report
    elseif ($action === 'delete_reported_post') {
        $repId = (int)($_POST['report_id'] ?? 0);
        $postId = (int)($_POST['post_id'] ?? 0);
        if ($postId > 0) {
            try {
                $pdo->prepare("DELETE FROM community_posts WHERE id = ?")->execute([$postId]);
                $pdo->prepare("DELETE FROM community_comments WHERE post_id = ?")->execute([$postId]);
                $pdo->prepare("UPDATE community_reports SET status = 'RESOLVED' WHERE id = ?")->execute([$repId]);
                logAdminAction($pdo, 'DELETE_REPORTED_POST', 'community_posts', (string)$postId, "Report #$repId resolved via delete");
                setFlash('success', __('রিপোর্টকৃত পোস্টটি মুছে ফেলা হয়েছে এবং রিপোর্ট সমাধান করা হয়েছে।', 'Reported post deleted and report resolved.'));
            } catch (Exception $e) {
                setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
            }
        }
        redirect('community.php?tab=reports');
    }
}

// Active Tab determination
$tab = $_GET['tab'] ?? 'feed';
if ($tab === 'posts') $tab = 'feed';

// Query authoritative live statistics across DeenOne Community
$totalPosts = 0;
$totalComments = 0;
$totalLikes = 0;
$totalReports = 0;
$pendingReports = 0;
$resolvedReports = 0;

try {
    $totalPosts = (int)$pdo->query("SELECT COUNT(*) FROM community_posts")->fetchColumn();
    $totalComments = (int)$pdo->query("SELECT COUNT(*) FROM community_comments")->fetchColumn();
    $totalLikes = (int)$pdo->query("SELECT IFNULL(SUM(likes_count), 0) FROM community_posts")->fetchColumn();
    $totalReports = (int)$pdo->query("SELECT COUNT(*) FROM community_reports")->fetchColumn();
    $pendingReports = (int)$pdo->query("SELECT COUNT(*) FROM community_reports WHERE status = 'PENDING'")->fetchColumn();
    $resolvedReports = (int)$pdo->query("SELECT COUNT(*) FROM community_reports WHERE status = 'RESOLVED'")->fetchColumn();
} catch (Exception $e) {}

// Setup Page Header
$pageTitle = __('দীন ওয়ান কমিউনিটি হাব', 'DeenOne Community Hub');
$activeNav = 'community';
require_once __DIR__ . '/header.php';
?>

<!-- Header Toolbar & Title -->
<div style="display: flex; gap: 14px; margin-bottom: 24px; align-items: center; justify-content: space-between; flex-wrap: wrap;">
  <div>
    <h2 style="margin: 0; font-size: 22px; font-weight: 800; color: var(--text-heading); display: flex; align-items: center; gap: 10px;">
      <span style="display: inline-flex; width: 36px; height: 36px; border-radius: 10px; background: rgba(16, 185, 129, 0.12); color: var(--primary); align-items: center; justify-content: center; font-size: 18px;">
        <i class="fa-solid fa-comments"></i>
      </span>
      <span><?= __('কমিউনিটি', 'Community') ?></span>
    </h2>
  </div>
  
  <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
    <a href="community.php?action=export_csv&tab=<?= urlencode($tab) ?>" class="btn btn-secondary btn-sm" style="height: 38px; display: inline-flex; align-items: center; gap: 6px;">
      <i class="fa-solid fa-file-arrow-down"></i> <?= __('CSV এক্সপোর্ট', 'Export CSV') ?>
    </a>
    <button type="button" class="btn btn-secondary btn-sm" onclick="openImportModal()" style="height: 38px; display: inline-flex; align-items: center; gap: 6px;">
      <i class="fa-solid fa-file-arrow-up"></i> <?= __('CSV ইমপোর্ট', 'Import CSV') ?>
    </button>
    <?php if ($tab === 'feed'): ?>
      <button type="button" class="btn btn-primary btn-sm" onclick="openCreatePostModal()" style="height: 38px;">
        <i class="fa-solid fa-plus"></i> <?= __('নতুন পোস্ট', 'New Post') ?>
      </button>
    <?php elseif ($tab === 'comments'): ?>
      <button type="button" class="btn btn-primary btn-sm" onclick="openAddCommentModal()" style="height: 38px;">
        <i class="fa-solid fa-plus"></i> <?= __('নতুন কমেন্ট', 'New Comment') ?>
      </button>
    <?php endif; ?>
  </div>
</div>

<style>
.stat-tab-card {
  position: relative;
  text-decoration: none !important;
  color: inherit !important;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  padding: 20px 22px;
}
.stat-tab-card:hover {
  transform: translateY(-3px);
  border-color: var(--primary);
  box-shadow: var(--shadow-md), 0 0 16px rgba(16, 185, 129, 0.12);
}
.stat-tab-card.active {
  border-color: var(--primary) !important;
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.08), var(--bg-card)) !important;
  box-shadow: 0 0 0 2px var(--primary), var(--shadow-md) !important;
}
.stat-tab-card.active::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--primary);
  opacity: 1 !important;
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
}
.stat-tab-indicator {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 11.5px;
  font-weight: 700;
  color: var(--primary);
  background: rgba(16, 185, 129, 0.12);
  border: 1px solid rgba(16, 185, 129, 0.25);
  padding: 4px 10px;
  border-radius: 20px;
  letter-spacing: 0.3px;
  white-space: nowrap;
}
html.dark .stat-tab-card.active {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.16), rgba(19, 34, 71, 0.95)) !important;
  border-color: var(--primary-light) !important;
  box-shadow: 0 0 0 2px var(--primary), 0 8px 24px rgba(0, 0, 0, 0.4) !important;
}
html.dark .stat-tab-indicator {
  color: var(--primary-light);
  background: rgba(16, 185, 129, 0.2);
  border-color: rgba(16, 185, 129, 0.4);
}
</style>

<!-- 4 Core Metric Stats Strip & Interactive Clickable Tabs (Total Posts, Total Comments, Total Likes, Total Reports) -->
<div class="stats-grid" style="margin-bottom: 26px;">
  <a href="community.php?tab=feed" class="stat-card stat-tab-card <?= ($tab === 'feed') ? 'active' : '' ?>" title="<?= __('কমিউনিটি ফিড ও পোস্টসমূহ দেখুন', 'View Community Feed & Posts') ?>">
    <div style="display: flex; align-items: center; gap: 16px;">
      <div class="stat-icon-wrap emerald"><i class="fa-solid fa-newspaper"></i></div>
      <div class="stat-content">
        <h3><?= toLangNum($totalPosts) ?></h3>
        <p><?= __('মোট পোস্ট', 'Total Posts') ?></p>
      </div>
    </div>
    <?php if ($tab === 'feed'): ?>
      <span class="stat-tab-indicator"><i class="fa-solid fa-circle-check"></i> <?= __('সক্রিয়', 'Active') ?></span>
    <?php endif; ?>
  </a>

  <a href="community.php?tab=comments" class="stat-card stat-tab-card <?= ($tab === 'comments') ? 'active' : '' ?>" title="<?= __('কমিউনিটি কমেন্টসমূহ দেখুন', 'View Community Comments') ?>">
    <div style="display: flex; align-items: center; gap: 16px;">
      <div class="stat-icon-wrap blue"><i class="fa-solid fa-comment-dots"></i></div>
      <div class="stat-content">
        <h3><?= toLangNum($totalComments) ?></h3>
        <p><?= __('মোট কমেন্ট', 'Total Comments') ?></p>
      </div>
    </div>
    <?php if ($tab === 'comments'): ?>
      <span class="stat-tab-indicator"><i class="fa-solid fa-circle-check"></i> <?= __('সক্রিয়', 'Active') ?></span>
    <?php endif; ?>
  </a>

  <div class="stat-card stat-tab-card" style="cursor: default;" title="<?= __('মোট লাইকের সংখ্যা', 'Total Likes Count') ?>">
    <div style="display: flex; align-items: center; gap: 16px;">
      <div class="stat-icon-wrap red"><i class="fa-solid fa-heart"></i></div>
      <div class="stat-content">
        <h3><?= toLangNum($totalLikes) ?></h3>
        <p><?= __('মোট লাইক', 'Total Likes') ?></p>
      </div>
    </div>
  </div>

  <a href="community.php?tab=reports" class="stat-card stat-tab-card <?= ($tab === 'reports') ? 'active' : '' ?>" title="<?= __('কমিউনিটি রিপোর্ট ও অভিযোগ দেখুন', 'View Community Reports & Complaints') ?>">
    <div style="display: flex; align-items: center; gap: 16px;">
      <div class="stat-icon-wrap amber"><i class="fa-solid fa-triangle-exclamation"></i></div>
      <div class="stat-content">
        <h3><?= toLangNum($totalReports) ?></h3>
        <p><?= __('মোট রিপোর্ট', 'Total Reports') ?></p>
      </div>
    </div>
    <?php if ($tab === 'reports'): ?>
      <span class="stat-tab-indicator"><i class="fa-solid fa-circle-check"></i> <?= __('সক্রিয়', 'Active') ?></span>
    <?php elseif ($pendingReports > 0): ?>
      <span class="badge badge-danger" style="font-size: 11px;"><?= toLangNum($pendingReports) ?> <?= __('নতুন', 'New') ?></span>
    <?php endif; ?>
  </a>
</div>

<!-- =========================================================================
     TAB 1: COMMUNITY FEED & POSTS MANAGEMENT
     ========================================================================= -->
<?php if ($tab === 'feed'): ?>
  <?php
    $feedSearch = trim($_GET['search'] ?? '');
    $filterStatus = $_GET['status'] ?? 'all';
    $feedPage = max(1, (int)($_GET['page'] ?? 1));
    $limit = 15;
    $offset = ($feedPage - 1) * $limit;

    $where = [];
    $params = [];

    if (!empty($feedSearch)) {
        $where[] = "(content LIKE ? OR author_name LIKE ? OR user_id LIKE ?)";
        $params[] = "%$feedSearch%";
        $params[] = "%$feedSearch%";
        $params[] = "%$feedSearch%";
    }

    if ($filterStatus === 'pinned') {
        $where[] = "is_pinned = 1";
    }

    $whereSql = !empty($where) ? 'WHERE ' . implode(' AND ', $where) : '';

    $totalFeedFiltered = 0;
    try {
        $countStmt = $pdo->prepare("SELECT COUNT(*) FROM community_posts $whereSql");
        $countStmt->execute($params);
        $totalFeedFiltered = (int)$countStmt->fetchColumn();
    } catch (Exception $e) {}

    $feedTotalPages = ceil(max(1, $totalFeedFiltered) / $limit);

    $postsList = [];
    try {
        $stmt = $pdo->prepare("SELECT * FROM community_posts $whereSql ORDER BY is_pinned DESC, id DESC LIMIT $limit OFFSET $offset");
        $stmt->execute($params);
        $postsList = $stmt->fetchAll();
    } catch (Exception $e) {}
  ?>

  <!-- Search & Filter Card -->
  <div class="card" style="margin-bottom: 24px;">
    <div class="card-body" style="padding: 18px 24px;">
      <form method="GET" action="community.php" style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
        <input type="hidden" name="tab" value="feed">
        <div style="flex: 1; min-width: 220px; position: relative;">
          <input type="text" name="search" value="<?= htmlspecialchars($feedSearch) ?>" class="form-control" placeholder="<?= __('পোস্টের টেক্সট, লেখকের নাম বা আইডি খুঁজুন...', 'Search post content, author name, or ID...') ?>">
        </div>
        <div style="min-width: 160px;">
          <select name="status" class="form-control" onchange="this.form.submit()">
            <option value="all" <?= ($filterStatus === 'all') ? 'selected' : '' ?>><?= __('সকল পোস্ট', 'All Posts') ?></option>
            <option value="pinned" <?= ($filterStatus === 'pinned') ? 'selected' : '' ?>><?= __('📌 পিনকৃত পোস্ট', 'Pinned Posts') ?></option>
          </select>
        </div>
        <button type="submit" class="btn btn-secondary">
          🔍 <?= __('অনুসন্ধান', 'Search') ?>
        </button>
        <?php if (!empty($feedSearch) || $filterStatus !== 'all'): ?>
          <a href="community.php?tab=feed" class="btn btn-secondary" title="<?= __('ফিল্টার রিসেট', 'Reset Filter') ?>">✕</a>
        <?php endif; ?>
      </form>
    </div>
  </div>

  <!-- Posts Table Card -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">
        <i class="fa-solid fa-list-check" style="color: var(--primary);"></i>
        <span><?= __('কমিউনিটি পোস্ট তালিকা', 'Community Posts List') ?> (<?= __('মোট: ', 'Total: ') . toLangNum($totalFeedFiltered) . __(' টি', ')') ?></span>
      </div>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 60px;"><?= __('আইডি', 'ID') ?></th>
            <th style="min-width: 180px;"><?= __('লেখক / ব্যবহারকারী', 'Author / User') ?></th>
            <th><?= __('পোস্টের বিবরণ ও কন্টেন্ট', 'Post Content') ?></th>
            <th style="width: 130px; text-align: center;"><?= __('লাইক ও কমেন্ট', 'Engagement') ?></th>
            <th style="width: 110px; text-align: center;"><?= __('পিন স্ট্যাটাস', 'Pin Status') ?></th>
            <th style="width: 140px;"><?= __('তারিখ ও সময়', 'Date & Time') ?></th>
            <th style="width: 130px; text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($postsList)): ?>
            <tr>
              <td colspan="7" style="text-align: center; padding: 36px; color: var(--text-dim);">
                <div style="font-size: 36px; margin-bottom: 8px;">📭</div>
                <div><?= __('কোনো পোস্ট খুঁজে পাওয়া যায়নি।', 'No community posts found.') ?></div>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($postsList as $p): ?>
              <tr style="<?= $p['is_pinned'] ? 'background: rgba(245, 158, 11, 0.04);' : '' ?>">
                <td>
                  <strong style="color: var(--text-dim);">#<?= toLangNum($p['id']) ?></strong>
                </td>
                <td>
                  <div style="display: flex; align-items: center; gap: 10px;">
                    <div class="avatar-sm" style="width: 34px; height: 34px; font-size: 13px;">
                      <?= mb_substr(htmlspecialchars($p['author_name']), 0, 1, 'UTF-8') ?>
                    </div>
                    <div>
                      <div style="font-weight: 700; color: var(--text-heading); font-size: 13.5px;">
                        <?= htmlspecialchars($p['author_name']) ?>
                        <?php if ($p['user_id'] === 'admin_official'): ?>
                          <span class="badge badge-primary" style="font-size: 10px; margin-left: 4px;"><?= __('অফিশিয়াল', 'Official') ?></span>
                        <?php endif; ?>
                      </div>
                      <div style="font-size: 11.5px; color: var(--text-dim);">
                        ID: <?= htmlspecialchars($p['user_id'] ?? '') ?>
                      </div>
                    </div>
                  </div>
                </td>
                <td>
                  <div style="font-size: 13.5px; color: var(--text-main); line-height: 1.6; max-width: 500px; max-height: 90px; overflow-y: auto;">
                    <?= nl2br(htmlspecialchars($p['content'])) ?>
                  </div>
                </td>
                <td style="text-align: center;">
                  <div style="display: inline-flex; gap: 12px; align-items: center; font-size: 12.5px;">
                    <span style="color: #ef4444; font-weight: 700;" title="<?= __('লাইক সংখ্যা', 'Likes') ?>">
                      ❤️ <?= toLangNum($p['likes_count']) ?>
                    </span>
                    <a href="community.php?tab=comments&post_id=<?= $p['id'] ?>" style="color: var(--accent-blue); font-weight: 700; text-decoration: none;" title="<?= __('কমেন্ট দেখুন ও পরিচালনা করুন', 'View & Manage Comments') ?>">
                      💬 <?= toLangNum($p['comments_count']) ?>
                    </a>
                  </div>
                </td>
                <td style="text-align: center;">
                  <?php if ($p['is_pinned']): ?>
                    <span class="badge badge-warning" style="font-size: 11px;">📌 <?= __('পিনকৃত', 'Pinned') ?></span>
                  <?php else: ?>
                    <span style="font-size: 12px; color: var(--text-dim);">-</span>
                  <?php endif; ?>
                </td>
                <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                  <?= isEn() ? date('d M Y, h:i A', strtotime($p['created_at'])) : toLangNum(date('d M Y, h:i A', strtotime($p['created_at']))) ?>
                </td>
                <td style="text-align: right;">
                  <div style="display: flex; gap: 6px; justify-content: flex-end; flex-wrap: wrap;">
                    <!-- Toggle Pin Form -->
                    <form method="POST" action="community.php" style="display: inline;">
                      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                      <input type="hidden" name="action" value="toggle_pin">
                      <input type="hidden" name="id" value="<?= $p['id'] ?>">
                      <input type="hidden" name="current_tab" value="feed">
                      <button type="submit" class="btn btn-secondary btn-sm" title="<?= $p['is_pinned'] ? __('আনপিন করুন', 'Unpin') : __('পিন করুন', 'Pin to top') ?>">
                        <?= $p['is_pinned'] ? '📌' : '📍' ?>
                      </button>
                    </form>

                    <!-- Edit Modal Trigger -->
                    <button type="button" class="btn btn-secondary btn-sm" title="<?= __('পোস্ট সম্পাদনা', 'Edit Post') ?>" onclick='openEditPostModal(<?= json_encode($p) ?>)'>
                      ✏️
                    </button>

                    <!-- Delete Form -->
                    <form method="POST" action="community.php" style="display: inline;" onsubmit="return confirm('<?= __('আপনি কি নিশ্চিত যে এই পোস্টটি মুছে ফেলতে চান?', 'Are you sure you want to delete this post?') ?>');">
                      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                      <input type="hidden" name="action" value="delete_post">
                      <input type="hidden" name="id" value="<?= $p['id'] ?>">
                      <input type="hidden" name="current_tab" value="feed">
                      <button type="submit" class="btn btn-danger btn-sm" title="<?= __('পোস্ট মুছুন', 'Delete Post') ?>">
                        🗑️
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

    <!-- Feed Pagination -->
    <?php if ($feedTotalPages > 1): ?>
      <div style="padding: 16px 24px; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
        <span style="font-size: 13px; color: var(--text-dim);">
          <?= __('পৃষ্ঠা ', 'Page ') . toLangNum($feedPage) . __(' এর ', ' of ') . toLangNum($feedTotalPages) ?>
        </span>
        <div style="display: flex; gap: 6px;">
          <?php if ($feedPage > 1): ?>
            <a href="community.php?tab=feed&page=<?= $feedPage - 1 ?>&search=<?= urlencode($feedSearch) ?>&status=<?= urlencode($filterStatus) ?>" class="btn btn-secondary btn-sm"><?= __('⬅️ পূর্ববর্তী', '⬅️ Prev') ?></a>
          <?php endif; ?>
          <?php if ($feedPage < $feedTotalPages): ?>
            <a href="community.php?tab=feed&page=<?= $feedPage + 1 ?>&search=<?= urlencode($feedSearch) ?>&status=<?= urlencode($filterStatus) ?>" class="btn btn-secondary btn-sm"><?= __('পরবর্তী ➡️', 'Next ➡️') ?></a>
          <?php endif; ?>
        </div>
      </div>
    <?php endif; ?>
  </div>

<!-- =========================================================================
     TAB 2: COMMUNITY COMMENTS MANAGEMENT
     ========================================================================= -->
<?php elseif ($tab === 'comments'): ?>
  <?php
    $commentSearch = trim($_GET['search'] ?? '');
    $filterPostId = (int)($_GET['post_id'] ?? 0);
    $commentPage = max(1, (int)($_GET['page'] ?? 1));
    $commentLimit = 20;
    $commentOffset = ($commentPage - 1) * $commentLimit;

    $cWhere = [];
    $cParams = [];

    if ($filterPostId > 0) {
        $cWhere[] = "c.post_id = ?";
        $cParams[] = $filterPostId;
    }

    if (!empty($commentSearch)) {
        $cWhere[] = "(c.comment_text LIKE ? OR c.author_name LIKE ? OR c.user_id LIKE ?)";
        $cParams[] = "%$commentSearch%";
        $cParams[] = "%$commentSearch%";
        $cParams[] = "%$commentSearch%";
    }

    $cWhereSql = !empty($cWhere) ? 'WHERE ' . implode(' AND ', $cWhere) : '';

    $totalCommentsFiltered = 0;
    try {
        $cCountStmt = $pdo->prepare("SELECT COUNT(*) FROM community_comments c $cWhereSql");
        $cCountStmt->execute($cParams);
        $totalCommentsFiltered = (int)$cCountStmt->fetchColumn();
    } catch (Exception $e) {}

    $commentTotalPages = ceil(max(1, $totalCommentsFiltered) / $commentLimit);

    $commentsList = [];
    try {
        $cStmt = $pdo->prepare("SELECT c.*, p.content AS post_content, p.author_name AS post_author 
                                FROM community_comments c 
                                LEFT JOIN community_posts p ON c.post_id = p.id 
                                $cWhereSql 
                                ORDER BY c.id DESC 
                                LIMIT $commentLimit OFFSET $commentOffset");
        $cStmt->execute($cParams);
        $commentsList = $cStmt->fetchAll();
    } catch (Exception $e) {}
  ?>

  <!-- Search & Post Filter -->
  <div class="card" style="margin-bottom: 24px;">
    <div class="card-body" style="padding: 18px 24px;">
      <form method="GET" action="community.php" style="display: flex; gap: 12px; align-items: center; flex-wrap: wrap;">
        <input type="hidden" name="tab" value="comments">
        <div style="flex: 1; min-width: 220px;">
          <input type="text" name="search" value="<?= htmlspecialchars($commentSearch) ?>" class="form-control" placeholder="<?= __('কমেন্টের টেক্সট, লেখক বা আইডি দিয়ে খুঁজুন...', 'Search comment text, author name or ID...') ?>">
        </div>
        <div style="width: 160px;">
          <input type="number" name="post_id" value="<?= $filterPostId > 0 ? $filterPostId : '' ?>" class="form-control" placeholder="<?= __('পোস্ট আইডি (যেমন: 12)', 'Post ID (e.g. 12)') ?>">
        </div>
        <button type="submit" class="btn btn-secondary">
          🔍 <?= __('অনুসন্ধান', 'Search') ?>
        </button>
        <?php if (!empty($commentSearch) || $filterPostId > 0): ?>
          <a href="community.php?tab=comments" class="btn btn-secondary" title="<?= __('ফিল্টার রিসেট', 'Reset Filter') ?>">✕</a>
        <?php endif; ?>
      </form>
    </div>
  </div>

  <!-- Comments Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">
        <i class="fa-solid fa-comment-dots" style="color: var(--accent-blue);"></i>
        <span><?= __('কমিউনিটি কমেন্ট তালিকা', 'Community Comments List') ?> (<?= __('মোট: ', 'Total: ') . toLangNum($totalCommentsFiltered) . __(' টি', ')') ?></span>
      </div>
      <?php if ($filterPostId > 0): ?>
        <span class="badge badge-info"><?= __('পোস্ট #', 'Post #') . toLangNum($filterPostId) . __(' এর কমেন্টসমূহ', ' Comments') ?></span>
      <?php endif; ?>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 60px;"><?= __('আইডি', 'ID') ?></th>
            <th style="width: 180px;"><?= __('পোস্ট রেফারেন্স', 'Post Ref') ?></th>
            <th style="min-width: 160px;"><?= __('কমেন্টকারী', 'Commenter') ?></th>
            <th><?= __('কমেন্টের বিবরণ', 'Comment Text') ?></th>
            <th style="width: 140px;"><?= __('তারিখ ও সময়', 'Date & Time') ?></th>
            <th style="width: 100px; text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($commentsList)): ?>
            <tr>
              <td colspan="6" style="text-align: center; padding: 36px; color: var(--text-dim);">
                <div style="font-size: 36px; margin-bottom: 8px;">💬</div>
                <div><?= __('কোনো কমেন্ট পাওয়া যায়নি।', 'No comments found.') ?></div>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($commentsList as $c): ?>
              <tr>
                <td><strong style="color: var(--text-dim);">#<?= toLangNum($c['id']) ?></strong></td>
                <td>
                  <a href="community.php?tab=feed&search=<?= urlencode((string)$c['post_id']) ?>" style="font-weight: 700; color: var(--primary); text-decoration: none;" title="<?= __('মূল পোস্ট দেখুন', 'View Original Post') ?>">
                    📄 <?= __('পোস্ট #', 'Post #') . toLangNum($c['post_id']) ?>
                  </a>
                  <?php if (!empty($c['post_content'])): ?>
                    <div style="font-size: 11.5px; color: var(--text-dim); max-width: 170px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-top: 2px;">
                      <?= htmlspecialchars(mb_substr($c['post_content'], 0, 30)) ?>...
                    </div>
                  <?php endif; ?>
                </td>
                <td>
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <div class="avatar-sm" style="width: 30px; height: 30px; font-size: 12px;">
                      <?= mb_substr(htmlspecialchars($c['author_name']), 0, 1, 'UTF-8') ?>
                    </div>
                    <div>
                      <div style="font-weight: 700; color: var(--text-heading); font-size: 13px;">
                        <?= htmlspecialchars($c['author_name']) ?>
                      </div>
                      <div style="font-size: 11px; color: var(--text-dim);">
                        <?= htmlspecialchars($c['user_id']) ?>
                      </div>
                    </div>
                  </div>
                </td>
                <td>
                  <div style="font-size: 13.5px; color: var(--text-main); line-height: 1.6;">
                    <?= nl2br(htmlspecialchars($c['comment_text'])) ?>
                  </div>
                </td>
                <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                  <?= isEn() ? date('d M Y, h:i A', strtotime($c['created_at'])) : toLangNum(date('d M Y, h:i A', strtotime($c['created_at']))) ?>
                </td>
                <td style="text-align: right;">
                  <form method="POST" action="community.php" style="display: inline;" onsubmit="return confirm('<?= __('আপনি কি নিশ্চিত যে এই কমেন্টটি মুছে ফেলতে চান?', 'Are you sure you want to delete this comment?') ?>');">
                    <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                    <input type="hidden" name="action" value="delete_comment">
                    <input type="hidden" name="comment_id" value="<?= $c['id'] ?>">
                    <input type="hidden" name="post_id" value="<?= $c['post_id'] ?>">
                    <input type="hidden" name="current_tab" value="comments">
                    <button type="submit" class="btn btn-danger btn-sm" title="<?= __('কমেন্ট মুছুন', 'Delete Comment') ?>">
                      🗑️
                    </button>
                  </form>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>

    <!-- Comments Pagination -->
    <?php if ($commentTotalPages > 1): ?>
      <div style="padding: 16px 24px; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
        <span style="font-size: 13px; color: var(--text-dim);">
          <?= __('পৃষ্ঠা ', 'Page ') . toLangNum($commentPage) . __(' এর ', ' of ') . toLangNum($commentTotalPages) ?>
        </span>
        <div style="display: flex; gap: 6px;">
          <?php if ($commentPage > 1): ?>
            <a href="community.php?tab=comments&page=<?= $commentPage - 1 ?>&search=<?= urlencode($commentSearch) ?>&post_id=<?= $filterPostId ?>" class="btn btn-secondary btn-sm"><?= __('⬅️ পূর্ববর্তী', '⬅️ Prev') ?></a>
          <?php endif; ?>
          <?php if ($commentPage < $commentTotalPages): ?>
            <a href="community.php?tab=comments&page=<?= $commentPage + 1 ?>&search=<?= urlencode($commentSearch) ?>&post_id=<?= $filterPostId ?>" class="btn btn-secondary btn-sm"><?= __('পরবর্তী ➡️', 'Next ➡️') ?></a>
          <?php endif; ?>
        </div>
      </div>
    <?php endif; ?>
  </div>

<!-- =========================================================================
     TAB 3: COMMUNITY REPORTS & COMPLAINTS MANAGEMENT
     ========================================================================= -->
<?php elseif ($tab === 'reports'): ?>
  <?php
    $reportStatusFilter = $_GET['report_status'] ?? 'all';
    $rWhere = [];
    $rParams = [];

    if ($reportStatusFilter === 'pending') {
        $rWhere[] = "r.status = 'PENDING'";
    } elseif ($reportStatusFilter === 'resolved') {
        $rWhere[] = "r.status = 'RESOLVED'";
    } elseif ($reportStatusFilter === 'dismissed') {
        $rWhere[] = "r.status = 'DISMISSED'";
    }

    $rWhereSql = !empty($rWhere) ? 'WHERE ' . implode(' AND ', $rWhere) : '';

    $reportsList = [];
    try {
        $rStmt = $pdo->prepare("SELECT r.*, p.content AS post_content, p.author_name AS post_author, p.user_id AS post_user_id 
                                FROM community_reports r 
                                LEFT JOIN community_posts p ON r.post_id = p.id 
                                $rWhereSql 
                                ORDER BY (r.status = 'PENDING') DESC, r.id DESC");
        $rStmt->execute($rParams);
        $reportsList = $rStmt->fetchAll();
    } catch (Exception $e) {}
  ?>

  <!-- Reports Filter Bar -->
  <div class="card" style="margin-bottom: 24px;">
    <div class="card-body" style="padding: 16px 24px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;">
      <div style="display: flex; gap: 8px; flex-wrap: wrap;">
        <a href="community.php?tab=reports&report_status=all" class="btn <?= ($reportStatusFilter === 'all') ? 'btn-primary' : 'btn-secondary' ?> btn-sm">
          <?= __('সকল রিপোর্ট', 'All Reports') ?>
        </a>
        <a href="community.php?tab=reports&report_status=pending" class="btn <?= ($reportStatusFilter === 'pending') ? 'btn-primary' : 'btn-secondary' ?> btn-sm">
          ⏳ <?= __('অপেক্ষমাণ', 'Pending') ?> (<?= toLangNum($pendingReports) ?>)
        </a>
        <a href="community.php?tab=reports&report_status=resolved" class="btn <?= ($reportStatusFilter === 'resolved') ? 'btn-primary' : 'btn-secondary' ?> btn-sm">
          ✓ <?= __('সমাধানকৃত', 'Resolved') ?> (<?= toLangNum($resolvedReports) ?>)
        </a>
        <a href="community.php?tab=reports&report_status=dismissed" class="btn <?= ($reportStatusFilter === 'dismissed') ? 'btn-primary' : 'btn-secondary' ?> btn-sm">
          ✕ <?= __('বাতিলকৃত', 'Dismissed') ?>
        </a>
      </div>

      <div style="font-size: 13px; color: var(--text-muted);">
        <?= __('মোট রিপোর্ট:', 'Total Reports:') ?> <strong><?= toLangNum(count($reportsList)) ?></strong>
      </div>
    </div>
  </div>

  <!-- Reports Table -->
  <div class="card">
    <div class="card-header">
      <div class="card-title">
        <i class="fa-solid fa-shield-halved" style="color: #ef4444;"></i>
        <span><?= __('ইউজার রিপোর্ট ও অভিযোগ তালিকা', 'User Reports & Complaints List') ?></span>
      </div>
    </div>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 60px;"><?= __('আইডি', 'ID') ?></th>
            <th style="width: 140px;"><?= __('রিপোর্টার', 'Reporter') ?></th>
            <th style="width: 180px;"><?= __('অভিযোগের কারণ', 'Reason') ?></th>
            <th><?= __('রিপোর্টকৃত পোস্ট ও বিষয়বস্তু', 'Reported Post & Content') ?></th>
            <th style="width: 120px;"><?= __('স্ট্যাটাস', 'Status') ?></th>
            <th style="width: 140px;"><?= __('তারিখ ও সময়', 'Date & Time') ?></th>
            <th style="width: 180px; text-align: right;"><?= __('অ্যাকশন', 'Action') ?></th>
          </tr>
        </thead>
        <tbody>
          <?php if (empty($reportsList)): ?>
            <tr>
              <td colspan="7" style="text-align: center; padding: 36px; color: var(--text-dim);">
                <div style="font-size: 36px; margin-bottom: 8px;">🛡️</div>
                <div><?= __('কোনো রিপোর্ট বা অভিযোগ নেই।', 'No reports or complaints found.') ?></div>
              </td>
            </tr>
          <?php else: ?>
            <?php foreach ($reportsList as $r): ?>
              <tr style="<?= ($r['status'] === 'PENDING') ? 'background: rgba(239, 68, 68, 0.04);' : '' ?>">
                <td><strong style="color: var(--text-dim);">#<?= toLangNum($r['id']) ?></strong></td>
                <td>
                  <div style="font-weight: 700; color: var(--text-heading); font-size: 13px;">
                    ID: <?= htmlspecialchars($r['reporter_user_id']) ?>
                  </div>
                </td>
                <td>
                  <span class="badge badge-danger" style="font-size: 12px; white-space: normal; text-align: left; line-height: 1.4;">
                    ⚠️ <?= htmlspecialchars($r['reason']) ?>
                  </span>
                </td>
                <td>
                  <?php if (!empty($r['post_id'])): ?>
                    <div style="font-size: 12px; margin-bottom: 4px;">
                      <a href="community.php?tab=feed&search=<?= urlencode((string)$r['post_id']) ?>" style="font-weight: 700; color: var(--primary); text-decoration: none;">
                        📄 <?= __('পোস্ট #', 'Post #') . toLangNum($r['post_id']) ?>
                      </a>
                      <?php if (!empty($r['post_author'])): ?>
                        <span style="color: var(--text-dim);">— <?= htmlspecialchars($r['post_author']) ?> (<?= htmlspecialchars($r['post_user_id']) ?>)</span>
                      <?php endif; ?>
                    </div>
                    <div style="font-size: 13px; color: var(--text-main); line-height: 1.5; background: var(--hover-bg); padding: 8px 12px; border-radius: 8px; border: 1px solid var(--border-color);">
                      <?= !empty($r['post_content']) ? nl2br(htmlspecialchars($r['post_content'])) : '<em style="color: var(--text-dim);">' . __('পোস্টটি ইতিমধ্যে মুছে ফেলা হয়েছে', 'Post already deleted') . '</em>' ?>
                    </div>
                  <?php else: ?>
                    <em style="color: var(--text-dim);"><?= __('সাধারণ অভিযোগ', 'General Report') ?></em>
                  <?php endif; ?>
                </td>
                <td>
                  <?php if ($r['status'] === 'PENDING'): ?>
                    <span class="badge badge-warning">⏳ <?= __('অপেক্ষমাণ', 'Pending') ?></span>
                  <?php elseif ($r['status'] === 'RESOLVED'): ?>
                    <span class="badge badge-success">✓ <?= __('সমাধানকৃত', 'Resolved') ?></span>
                  <?php else: ?>
                    <span class="badge badge-secondary">✕ <?= __('বাতিলকৃত', 'Dismissed') ?></span>
                  <?php endif; ?>
                </td>
                <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                  <?= isEn() ? date('d M Y, h:i A', strtotime($r['created_at'])) : toLangNum(date('d M Y, h:i A', strtotime($r['created_at']))) ?>
                </td>
                <td style="text-align: right;">
                  <div style="display: flex; gap: 6px; justify-content: flex-end; flex-wrap: wrap;">
                    <?php if ($r['status'] === 'PENDING'): ?>
                      <!-- Resolve Button -->
                      <form method="POST" action="community.php" style="display: inline;">
                        <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                        <input type="hidden" name="action" value="resolve_report">
                        <input type="hidden" name="report_id" value="<?= $r['id'] ?>">
                        <input type="hidden" name="status" value="RESOLVED">
                        <input type="hidden" name="current_tab" value="reports">
                        <button type="submit" class="btn btn-primary btn-sm" title="<?= __('সমাধান হিসেবে চিহ্নিত করুন', 'Mark as Resolved') ?>">
                          ✓
                        </button>
                      </form>

                      <!-- Dismiss Button -->
                      <form method="POST" action="community.php" style="display: inline;">
                        <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                        <input type="hidden" name="action" value="resolve_report">
                        <input type="hidden" name="report_id" value="<?= $r['id'] ?>">
                        <input type="hidden" name="status" value="DISMISSED">
                        <input type="hidden" name="current_tab" value="reports">
                        <button type="submit" class="btn btn-secondary btn-sm" title="<?= __('অভিযোগ বাতিল করুন', 'Dismiss Report') ?>">
                          ✕
                        </button>
                      </form>

                      <?php if (!empty($r['post_id'])): ?>
                        <!-- Delete Post & Resolve Form -->
                        <form method="POST" action="community.php" style="display: inline;" onsubmit="return confirm('<?= __('আপনি কি নিশ্চিত যে এই রিপোর্টকৃত পোস্টটি মুছে ফেলতে চান?', 'Are you sure you want to delete this reported post?') ?>');">
                          <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                          <input type="hidden" name="action" value="delete_reported_post">
                          <input type="hidden" name="report_id" value="<?= $r['id'] ?>">
                          <input type="hidden" name="post_id" value="<?= $r['post_id'] ?>">
                          <input type="hidden" name="current_tab" value="reports">
                          <button type="submit" class="btn btn-danger btn-sm" title="<?= __('পোস্ট মুছে সমাধান করুন', 'Delete Post & Resolve') ?>">
                            🗑️
                          </button>
                        </form>
                      <?php endif; ?>
                    <?php else: ?>
                      <form method="POST" action="community.php" style="display: inline;">
                        <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
                        <input type="hidden" name="action" value="resolve_report">
                        <input type="hidden" name="report_id" value="<?= $r['id'] ?>">
                        <input type="hidden" name="status" value="PENDING">
                        <input type="hidden" name="current_tab" value="reports">
                        <button type="submit" class="btn btn-secondary btn-sm" title="<?= __('পুনরায় পেন্ডিং করুন', 'Reopen as Pending') ?>">
                          🔄
                        </button>
                      </form>
                    <?php endif; ?>
                  </div>
                </td>
              </tr>
            <?php endforeach; ?>
          <?php endif; ?>
        </tbody>
      </table>
    </div>
  </div>
<?php endif; ?>

<!-- =========================================================================
     MODALS
     ========================================================================= -->

<!-- Modal 1: Create Official Post -->
<div class="modal-backdrop" id="createPostModal">
  <div class="modal-window" style="max-width: 560px;">
    <div class="modal-header">
      <div class="modal-title">➕ <?= __('অফিশিয়াল পোস্ট তৈরি করুন', 'Create Official Post') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('createPostModal')">&times;</button>
    </div>
    <form method="POST" action="community.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="create_admin_post">
      <input type="hidden" name="current_tab" value="feed">

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label"><?= __('পোস্টের বিবরণ ও কন্টেন্ট', 'Post Content') ?> *</label>
          <textarea name="content" class="form-control" rows="6" required placeholder="<?= __('দ্বীনি নসিহত, ঘোষণা বা জরুরি বার্তা লিখুন...', 'Write Islamic advice, announcement, or important message...') ?>"></textarea>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="switch-label">
            <span class="switch">
              <input type="checkbox" name="is_pinned" value="1">
              <span class="slider"></span>
            </span>
            <span style="font-size: 14px; font-weight: 700; color: var(--text-heading);">
              📌 <?= __('ফিডের শীর্ষে পিন করে রাখুন', 'Pin to top of feed') ?>
            </span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('createPostModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">🚀 <?= __('পোস্ট প্রকাশ করুন', 'Publish Post') ?></button>
      </div>
    </form>
  </div>
</div>

<!-- Modal 2: Edit Post -->
<div class="modal-backdrop" id="editPostModal">
  <div class="modal-window" style="max-width: 560px;">
    <div class="modal-header">
      <div class="modal-title">✏️ <?= __('পোস্ট সম্পাদনা ও মডারেশন', 'Edit Post & Moderate') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('editPostModal')">&times;</button>
    </div>
    <form method="POST" action="community.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="edit_post">
      <input type="hidden" name="id" id="editPostId" value="">
      <input type="hidden" name="current_tab" value="feed">

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label"><?= __('পোস্টের বিবরণ ও কন্টেন্ট', 'Post Content') ?> *</label>
          <textarea name="content" id="editPostContent" class="form-control" rows="6" required></textarea>
        </div>

        <div class="form-group" style="margin-bottom: 0;">
          <label class="switch-label">
            <span class="switch">
              <input type="checkbox" name="is_pinned" id="editPostPinned" value="1">
              <span class="slider"></span>
            </span>
            <span style="font-size: 14px; font-weight: 700; color: var(--text-heading);">
              📌 <?= __('ফিডের শীর্ষে পিন করে রাখুন', 'Pin to top of feed') ?>
            </span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('editPostModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">💾 <?= __('পরিবর্তন সংরক্ষণ', 'Save Changes') ?></button>
      </div>
    </form>
  </div>
</div>

<!-- Modal 3: Add Admin Comment -->
<div class="modal-backdrop" id="addCommentModal">
  <div class="modal-window" style="max-width: 520px;">
    <div class="modal-header">
      <div class="modal-title">💬 <?= __('অফিশিয়াল কমেন্ট যোগ করুন', 'Add Official Comment') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('addCommentModal')">&times;</button>
    </div>
    <form method="POST" action="community.php">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="add_admin_comment">
      <input type="hidden" name="current_tab" value="comments">

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label"><?= __('টার্গেট পোস্ট আইডি', 'Target Post ID') ?> *</label>
          <input type="number" name="post_id" class="form-control" required min="1" placeholder="<?= __('যেমন: ৫', 'e.g. 5') ?>">
        </div>

        <div class="form-group">
          <label class="form-label"><?= __('কমেন্টের বিবরণ', 'Comment Text') ?> *</label>
          <textarea name="comment_text" class="form-control" rows="4" required placeholder="<?= __('অফিশিয়াল মন্তব্য বা উত্তর লিখুন...', 'Write official comment or response...') ?>"></textarea>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('addCommentModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">💬 <?= __('কমেন্ট পোস্ট করুন', 'Post Comment') ?></button>
      </div>
    </form>
  </div>
</div>

<!-- Modal 4: Import CSV -->
<div class="modal-backdrop" id="importModal">
  <div class="modal-window" style="max-width: 500px;">
    <div class="modal-header">
      <div class="modal-title">📤 <?= __('কমিউনিটি পোস্ট CSV ইমপোর্ট', 'Import Community Posts CSV') ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('importModal')">&times;</button>
    </div>
    <form method="POST" action="community.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?= getCsrfToken() ?>">
      <input type="hidden" name="action" value="import_csv">
      <input type="hidden" name="current_tab" value="<?= htmlspecialchars($tab) ?>">

      <div class="modal-body">
        <p style="font-size: 13px; color: var(--text-muted); margin-top: 0;">
          <?= __('সঠিক ফরম্যাটের CSV ফাইল আপলোড করুন। কলামগুলো হলো: ID, User ID, Author Name, Author Avatar, Content, Image URL, Likes Count, Comments Count, Is Pinned', 'Upload a CSV file with columns: ID, User ID, Author Name, Author Avatar, Content, Image URL, Likes Count, Comments Count, Is Pinned') ?>
        </p>
        <div class="form-group">
          <label class="form-label"><?= __('CSV ফাইল নির্বাচন করুন', 'Select CSV File') ?> *</label>
          <input type="file" name="csv_file" class="form-control" accept=".csv" required>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('importModal')"><?= __('বাতিল', 'Cancel') ?></button>
        <button type="submit" class="btn btn-primary">📥 <?= __('ইমপোর্ট শুরু করুন', 'Start Import') ?></button>
      </div>
    </form>
  </div>
</div>

<script>
function openCreatePostModal() {
  document.getElementById('createPostModal').classList.add('open');
}

function openAddCommentModal() {
  document.getElementById('addCommentModal').classList.add('open');
}

function openImportModal() {
  document.getElementById('importModal').classList.add('open');
}

function openEditPostModal(post) {
  document.getElementById('editPostId').value = post.id;
  document.getElementById('editPostContent').value = post.content;
  document.getElementById('editPostPinned').checked = (parseInt(post.is_pinned) === 1);
  document.getElementById('editPostModal').classList.add('open');
}

function closeModal(modalId) {
  document.getElementById(modalId).classList.remove('open');
}

window.addEventListener('click', function(e) {
  if (e.target.classList.contains('modal-backdrop')) {
    e.target.classList.remove('open');
  }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
