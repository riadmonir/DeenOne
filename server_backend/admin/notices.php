<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - BROADCAST NOTICES & ANNOUNCEMENTS
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Handle Action (Add, Edit, Delete, Toggle)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('notices.php');
    }

    if ($action === 'create' || $action === 'update') {
        $title = trim($_POST['title'] ?? '');
        $message = trim($_POST['message'] ?? '');
        $type = $_POST['notice_type'] ?? 'INFO';
        $actionUrl = trim($_POST['action_url'] ?? '');
        $isActive = isset($_POST['is_active']) ? 1 : 0;

        if (empty($title) || empty($message)) {
            setFlash('danger', __('শিরোনাম ও বার্তার বিবরণ উভয়ই আবশ্যক।', 'Both title and message text are required.'));
        } else {
            try {
                if ($action === 'create') {
                    $stmt = $pdo->prepare("INSERT INTO app_notices (title, message, notice_type, action_url, is_active) VALUES (?, ?, ?, ?, ?)");
                    $stmt->execute([$title, $message, $type, $actionUrl, $isActive]);
                    logAdminAction($pdo, 'CREATE_NOTICE', 'app_notices', (string)$pdo->lastInsertId(), 'নতুন নোটিশ: ' . $title);
                    setFlash('success', __('নতুন নোটিশ সফলভাবে তৈরি হয়েছে!', 'Notice created successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE app_notices SET title = ?, message = ?, notice_type = ?, action_url = ?, is_active = ? WHERE id = ?");
                    $stmt->execute([$title, $message, $type, $actionUrl, $isActive, $id]);
                    logAdminAction($pdo, 'UPDATE_NOTICE', 'app_notices', (string)$id, 'নোটিশ আপডেট: ' . $title);
                    setFlash('success', __('নোটিশ তথ্য আপডেট করা হয়েছে!', 'Notice updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('অপারেশন ব্যর্থ: ', 'Operation failed: ') . $e->getMessage());
            }
        }
    }
    redirect('notices.php');
}

// Handle GET Actions (Delete, Toggle)
if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM app_notices WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_NOTICE', 'app_notices', (string)$id, 'নোটিশ মুছে ফেলা হয়েছে');
        setFlash('success', __('নোটিশ সফলভাবে মুছে ফেলা হয়েছে।', 'Notice deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছে ফেলতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
    }
    redirect('notices.php');
}

if (isset($_GET['toggle'])) {
    $id = (int)$_GET['toggle'];
    try {
        $stmt = $pdo->prepare("UPDATE app_notices SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        setFlash('success', __('নোটিশ স্ট্যাটাস পরিবর্তন করা হয়েছে।', 'Notice status updated.'));
    } catch (Exception $e) {}
    redirect('notices.php');
}

// Fetch All Notices
$notices = [];
try {
    $stmt = $pdo->query("SELECT * FROM app_notices ORDER BY created_at DESC");
    $notices = $stmt->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('নোটিশ', 'Notices');
$activeNav = 'notices';
require_once __DIR__ . '/header.php';
?>

<div class="card">
  <div class="card-header">
    <div class="card-title"><i class="fa-solid fa-bell"></i> <?php echo __('নোটিশ তালিকা', 'Notices'); ?></div>
    <div style="display: flex; gap: 10px;">
      <input type="text" id="searchNotice" class="form-control" placeholder="<?php echo __('নোটিশ খুঁজুন...', 'Search notices...'); ?>" style="width: 220px; padding: 6px 12px;" onkeyup="filterTable('searchNotice', 'noticeTable')">
      <button type="button" class="btn btn-primary btn-sm" onclick="openModal('addNoticeModal')">
        <i class="fa-solid fa-plus"></i> <?php echo __('নতুন নোটিশ', 'New Notice'); ?>
      </button>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table" id="noticeTable">
      <thead>
        <tr>
          <th style="width: 60px;"><?php echo __('আইডি', 'ID'); ?></th>
          <th><?php echo __('শিরোনাম ও বার্তা', 'Title & Message'); ?></th>
          <th><?php echo __('ধরণ', 'Type'); ?></th>
          <th><?php echo __('অ্যাকশন লিংক', 'Action URL'); ?></th>
          <th><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th><?php echo __('তৈরির তারিখ', 'Date Created'); ?></th>
          <th style="text-align: right;"><?php echo __('পদক্ষেপ', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($notices)): ?>
          <tr><td colspan="7" style="text-align: center; color: var(--text-dim);"><?php echo __('কোনো নোটিশ পাওয়া যায়নি।', 'No notices found.'); ?></td></tr>
        <?php else: ?>
          <?php foreach ($notices as $n): ?>
            <tr>
              <td>#<?php echo toLangNum($n['id']); ?></td>
              <td>
                <div style="font-weight: 600; margin-bottom: 3px;"><?php echo htmlspecialchars($n['title']); ?></div>
                <div style="font-size: 13px; color: var(--text-muted); max-width: 450px; line-height: 1.4;">
                  <?php echo htmlspecialchars($n['message']); ?>
                </div>
              </td>
              <td>
                <?php
                $badgeClass = 'badge-info';
                if ($n['notice_type'] === 'WARNING') $badgeClass = 'badge-danger';
                if ($n['notice_type'] === 'EVENT') $badgeClass = 'badge-warning';
                if ($n['notice_type'] === 'UPDATE') $badgeClass = 'badge-success';
                ?>
                <span class="badge <?php echo $badgeClass; ?>"><?php echo $n['notice_type']; ?></span>
              </td>
              <td style="font-size: 12px; color: var(--text-dim);">
                <?php echo !empty($n['action_url']) ? htmlspecialchars($n['action_url']) : '—'; ?>
              </td>
              <td>
                <a href="notices.php?toggle=<?php echo $n['id']; ?>" class="badge <?php echo $n['is_active'] ? 'badge-success' : 'badge-secondary'; ?>" style="text-decoration: none;">
                  <?php echo $n['is_active'] ? '✓ ' . __('সক্রিয়', 'Active') : '✕ ' . __('নিষ্ক্রিয়', 'Inactive'); ?>
                </a>
              </td>
              <td style="font-size: 12px; color: var(--text-dim); white-space: nowrap;">
                <?php echo isBn() ? toLangNum(date('d M Y, h:i A', strtotime($n['created_at']))) : date('d M Y, h:i A', strtotime($n['created_at'])); ?>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <button type="button" class="btn btn-secondary btn-sm" onclick="editNotice(<?php echo htmlspecialchars(json_encode($n)); ?>)">
                  <i class="fa-solid fa-pen-to-square" style="font-size: 11px; margin-right: 4px;"></i><?php echo __('সম্পাদনা', 'Edit'); ?>
                </button>
                <button type="button" class="btn btn-danger btn-sm" onclick="confirmDelete('notices.php?delete=<?php echo $n['id']; ?>', '<?php echo __('এই নোটিশটি মুছে ফেলতে চান?', 'Are you sure you want to delete this notice?'); ?>')">
                  <i class="fa-solid fa-trash-can" style="font-size: 11px; margin-right: 4px;"></i><?php echo __('ডিলিট', 'Delete'); ?>
                </button>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- Add/Edit Notice Modal -->
<div class="modal-backdrop" id="addNoticeModal">
  <div class="modal-window">
    <div class="modal-header">
      <div class="modal-title" id="noticeModalTitle"><i class="fa-solid fa-plus-circle" style="color: var(--primary); margin-right: 6px;"></i><?php echo __('নতুন নোটিশ', 'New Notice'); ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('addNoticeModal')">&times;</button>
    </div>
    <form method="POST" action="notices.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="noticeAction" value="create">
      <input type="hidden" name="id" id="noticeId" value="">

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label"><?php echo __('শিরোনাম', 'Title'); ?></label>
          <input type="text" name="title" id="inputNoticeTitle" class="form-control" placeholder="<?php echo __('যেমন: রমাদান বিশেষ কুইজ প্রতিযোগিতা', 'e.g. Ramadan Special Quiz'); ?>" required>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('ধরণ', 'Type'); ?></label>
            <select name="notice_type" id="inputNoticeType" class="form-control">
              <option value="INFO"><?php echo __('তথ্যবার্তা', 'Information'); ?></option>
              <option value="EVENT"><?php echo __('বিশেষ ইভেন্ট', 'Special Event'); ?></option>
              <option value="UPDATE"><?php echo __('অ্যাপ আপডেট', 'App Update'); ?></option>
              <option value="WARNING"><?php echo __('সতর্কবার্তা', 'Warning Alert'); ?></option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('অ্যাকশন লিংক', 'Action URL'); ?></label>
            <input type="text" name="action_url" id="inputNoticeUrl" class="form-control" placeholder="deenone://battle">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('বিবরণ', 'Message'); ?></label>
          <textarea name="message" id="inputNoticeMessage" class="form-control" rows="4" placeholder="<?php echo __('বার্তার বিবরণ লিখুন...', 'Enter message details...'); ?>" required></textarea>
        </div>

        <div class="form-group">
          <label class="switch-label">
            <label class="switch">
              <input type="checkbox" name="is_active" id="inputNoticeActive" value="1" checked>
              <span class="slider"></span>
            </label>
            <span style="font-weight: 600;"><?php echo __('সক্রিয় রাখুন', 'Active'); ?></span>
          </label>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('addNoticeModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary" id="btnNoticeSubmit"><i class="fa-solid fa-paper-plane mr-1"></i><?php echo __('সংরক্ষণ করুন', 'Save'); ?></button>
      </div>
    </form>
  </div>
</div>

<script>
const isNoticeEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

function editNotice(notice) {
  document.getElementById('noticeModalTitle').innerHTML = isNoticeEnglish ? '<i class="fa-solid fa-pen-to-square mr-2"></i>Edit Notice' : '<i class="fa-solid fa-pen-to-square mr-2"></i>নোটিশ সম্পাদনা';
  document.getElementById('noticeAction').value = 'update';
  document.getElementById('noticeId').value = notice.id;
  document.getElementById('inputNoticeTitle').value = notice.title;
  document.getElementById('inputNoticeType').value = notice.notice_type;
  document.getElementById('inputNoticeUrl').value = notice.action_url || '';
  document.getElementById('inputNoticeMessage').value = notice.message;
  document.getElementById('inputNoticeActive').checked = notice.is_active == 1;
  document.getElementById('btnNoticeSubmit').innerHTML = isNoticeEnglish ? '<i class="fa-solid fa-floppy-disk mr-1"></i>Save Changes' : '<i class="fa-solid fa-floppy-disk mr-1"></i>পরিবর্তন সংরক্ষণ করুন';
  openModal('addNoticeModal');
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
