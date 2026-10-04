<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ISLAMIC AUDIO HUB (ইসলামিক অডিও হাব)
 * Strict Category Separation: Waz, Bayan, Lectures, Dua, Azkar, Tafsir, Seerah
 * (CRITICAL: Quran recitations are strictly excluded)
 * Fully compliant with Rule 5 Dual-Language and Rule 13 Circular Icon Policy
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure table exists
$pdo->exec("CREATE TABLE IF NOT EXISTS `islamic_audios` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `audio_id` VARCHAR(100) NOT NULL UNIQUE,
  `title` VARCHAR(255) NOT NULL,
  `speaker` VARCHAR(150) NOT NULL,
  `category` VARCHAR(60) NOT NULL DEFAULT 'waz',
  `duration_seconds` INT UNSIGNED NOT NULL DEFAULT 0,
  `audio_url` TEXT NOT NULL,
  `description` TEXT DEFAULT NULL,
  `reference` VARCHAR(255) DEFAULT NULL,
  `source` VARCHAR(100) DEFAULT 'DeenOne Studio',
  `is_active` TINYINT(1) DEFAULT 1,
  `display_order` INT UNSIGNED DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_audio_cat` (`category`),
  INDEX `idx_audio_active` (`is_active`),
  INDEX `idx_audio_order` (`display_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Security token invalid.'));
        redirect('audio_hub.php');
    }

    if ($action === 'create' || $action === 'update') {
        $title = trim($_POST['title'] ?? '');
        $speaker = trim($_POST['speaker'] ?? '');
        $category = strtolower(trim($_POST['category'] ?? 'waz'));
        $duration = (int)($_POST['duration_seconds'] ?? 0);
        $audioUrl = trim($_POST['audio_url'] ?? '');
        $description = trim($_POST['description'] ?? '');
        $reference = trim($_POST['reference'] ?? '');
        $source = trim($_POST['source'] ?? 'DeenOne Studio');
        $isActive = isset($_POST['is_active']) ? 1 : 0;
        $displayOrder = (int)($_POST['display_order'] ?? 1);

        if (empty($title) || empty($audioUrl) || empty($speaker)) {
            setFlash('danger', __('অডিও শিরোনাম, বক্তা বা স্কলার এবং অডিও লিংক আবশ্যক।', 'Audio title, speaker/scholar and audio URL are required.'));
        } else {
            try {
                if ($action === 'create') {
                    $audioId = 'aud_' . strtolower($category) . '_' . bin2hex(random_bytes(3));
                    $stmt = $pdo->prepare("INSERT INTO islamic_audios 
                        (audio_id, title, speaker, category, duration_seconds, audio_url, description, reference, source, is_active, display_order)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$audioId, $title, $speaker, $category, $duration, $audioUrl, $description, $reference, $source, $isActive, $displayOrder]);
                    logAdminAction($pdo, 'CREATE_AUDIO', 'islamic_audios', $audioId, 'Created: ' . $title);
                    setFlash('success', __('নতুন ইসলামিক অডিও সফলভাবে যুক্ত হয়েছে!', 'New Islamic audio added successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE islamic_audios SET 
                        title = ?, speaker = ?, category = ?, duration_seconds = ?, audio_url = ?, description = ?, reference = ?, source = ?, is_active = ?, display_order = ?
                        WHERE id = ?");
                    $stmt->execute([$title, $speaker, $category, $duration, $audioUrl, $description, $reference, $source, $isActive, $displayOrder, $id]);
                    logAdminAction($pdo, 'UPDATE_AUDIO', 'islamic_audios', (string)$id, 'Updated: ' . $title);
                    setFlash('success', __('ইসলামিক অডিও তথ্য সফলভাবে আপডেট হয়েছে!', 'Islamic audio updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('ডাটাবেস ত্রুটি: ', 'Database error: ') . $e->getMessage());
            }
        }
    } elseif ($action === 'toggle_status') {
        $id = (int)($_POST['id'] ?? 0);
        $stmt = $pdo->prepare("UPDATE islamic_audios SET is_active = IF(is_active=1, 0, 1) WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'TOGGLE_AUDIO_STATUS', 'islamic_audios', (string)$id, 'Status toggled');
        setFlash('success', __('অডিওর সক্রিয়তা স্ট্যাটাস পরিবর্তিত হয়েছে!', 'Audio status toggled successfully!'));
    } elseif ($action === 'delete') {
        $id = (int)($_POST['id'] ?? 0);
        $stmt = $pdo->prepare("DELETE FROM islamic_audios WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_AUDIO', 'islamic_audios', (string)$id, 'Deleted audio');
        setFlash('success', __('অডিও রেকর্ড সফলভাবে মুছে ফেলা হয়েছে!', 'Audio record deleted successfully!'));
    }
    redirect('audio_hub.php');
}

// Categories list bilingual
$categories = [
    'waz' => ['bn' => 'ওয়াজ ও নসিহত', 'en' => 'Waz & Advice'],
    'bayan' => ['bn' => 'হৃদয়গ্রাহী বয়ান', 'en' => 'Heart-touching Bayan'],
    'lectures' => ['bn' => 'ইসলামিক লেকচার', 'en' => 'Islamic Lectures'],
    'dua' => ['bn' => 'দোয়া ও মুনাজাত', 'en' => 'Dua & Munajat'],
    'azkar' => ['bn' => 'সকাল-সন্ধ্যার আজকার', 'en' => 'Morning & Evening Azkar'],
    'tafsir' => ['bn' => 'কুরআন তাফসির লেকচার', 'en' => 'Quran Tafsir Lectures'],
    'hadith' => ['bn' => 'হাদিস দরস ও আলোচনা', 'en' => 'Hadith Dars & Discussions'],
    'seerah' => ['bn' => 'সীরাতুন্নবী', 'en' => 'Prophetic Seerah'],
    'fiqh' => ['bn' => 'ফিকহ ও মাসায়েল', 'en' => 'Fiqh & Masail'],
    'aqeedah' => ['bn' => 'আকিদা ও বিশ্বাস', 'en' => 'Aqeedah & Belief']
];

// Search & Filter
$search = trim($_GET['search'] ?? '');
$filterCategory = trim($_GET['category'] ?? '');
$where = ["1=1"];
$params = [];

if ($search !== '') {
    $where[] = "(title LIKE ? OR speaker LIKE ? OR description LIKE ? OR reference LIKE ?)";
    $st = "%$search%";
    $params = array_merge($params, [$st, $st, $st, $st]);
}
if ($filterCategory !== '' && isset($categories[$filterCategory])) {
    $where[] = "category = ?";
    $params[] = $filterCategory;
}

$whereClause = implode(" AND ", $where);
$stmt = $pdo->prepare("SELECT * FROM islamic_audios WHERE $whereClause ORDER BY display_order ASC, id DESC");
$stmt->execute($params);
$audioList = $stmt->fetchAll(PDO::FETCH_ASSOC);

$totalAudios = count($audioList);

$pageTitle = __('ইসলামিক অডিও হাব', 'Islamic Audio Hub');
$activeNav = 'audio_hub';
require_once __DIR__ . '/header.php';
?>

<div class="content-header">
  <div class="header-left">
    <h1>
      <span class="icon-circle" style="background: rgba(16, 185, 129, 0.15); color: var(--accent-mint); width: 42px; height: 42px; display: inline-flex; align-items: center; justify-content: center; border-radius: 50%; margin-right: 8px;">
        <i class="fa-solid fa-headphones"></i>
      </span>
      <?php echo __('অডিও হাব', 'Audio Hub'); ?>
    </h1>
  </div>
  <div class="header-right">
    <button class="btn btn-primary" onclick="openAddModal()">
      <i class="fa-solid fa-plus"></i> <?php echo __('নতুন অডিও', 'New Audio'); ?>
    </button>
  </div>
</div>

<!-- Category Stat Badges in Frame -->
<div class="card mb-4" style="padding: 16px;">
  <div style="font-size: 13px; font-weight: 600; color: var(--text-muted); margin-bottom: 12px; display: flex; align-items: center; justify-content: space-between;">
    <span><i class="fa-solid fa-layer-group" style="color: var(--accent-teal); margin-right: 6px;"></i> <?php echo __('ক্যাটাগরি', 'Categories'); ?></span>
    <span class="badge badge-teal"><?php echo __('মোট ট্র্যাক: ', 'Total Tracks: ') . toLangNum($totalAudios); ?></span>
  </div>
  <div style="display: flex; flex-wrap: wrap; gap: 10px; width: 100%; overflow-x: hidden;">
    <?php foreach ($categories as $catKey => $catData): 
        $cStmt = $pdo->prepare("SELECT COUNT(*) FROM islamic_audios WHERE category = ? AND is_active = 1");
        $cStmt->execute([$catKey]);
        $catCount = (int)$cStmt->fetchColumn();
        $isActiveFilter = ($filterCategory === $catKey);
    ?>
    <a href="audio_hub.php?category=<?php echo urlencode($catKey); ?>" 
       style="text-decoration: none; display: inline-flex; align-items: center; gap: 8px; padding: 8px 14px; border-radius: 20px; font-size: 13px; font-weight: 500; transition: all 0.2s ease; <?php echo $isActiveFilter ? 'background: var(--accent-mint); color: #064e3b; font-weight: 700; box-shadow: 0 2px 8px rgba(16,185,129,0.3);' : 'background: var(--bg-card); color: var(--text-primary); border: 1px solid var(--border-color);'; ?>">
      <span><?php echo __($catData['bn'], $catData['en']); ?></span>
      <span style="display: inline-flex; align-items: center; justify-content: center; min-width: 20px; height: 20px; border-radius: 10px; font-size: 11px; padding: 0 6px; <?php echo $isActiveFilter ? 'background: #064e3b; color: #fff;' : 'background: rgba(16, 185, 129, 0.15); color: var(--accent-mint);'; ?>">
        <?php echo toLangNum($catCount); ?>
      </span>
    </a>
    <?php endforeach; ?>
    <?php if ($filterCategory !== ''): ?>
      <a href="audio_hub.php" class="btn btn-sm btn-outline" style="border-radius: 20px; padding: 8px 14px;">
        <i class="fa-solid fa-rotate-left"></i> <?php echo __('সবগুলো দেখুন', 'View All'); ?>
      </a>
    <?php endif; ?>
  </div>
</div>

<!-- Search Bar -->
<div class="filter-card mb-4">
  <form method="GET" action="audio_hub.php" class="filter-form" style="display: flex; gap: 12px; flex-wrap: wrap; width: 100%;">
    <div class="form-group" style="flex: 2; min-width: 240px;">
      <input type="text" name="search" class="form-control" placeholder="<?php echo __('শিরোনাম, বক্তা বা বিষয় অনুসন্ধান...', 'Search title, speaker or topic...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
    </div>
    <div class="form-group" style="flex: 1; min-width: 180px;">
      <select name="category" class="form-control">
        <option value=""><?php echo __('সকল ক্যাটাগরি', 'All Categories'); ?></option>
        <?php foreach ($categories as $catKey => $catData): ?>
          <option value="<?php echo $catKey; ?>" <?php echo $filterCategory === $catKey ? 'selected' : ''; ?>>
            <?php echo __($catData['bn'], $catData['en']); ?>
          </option>
        <?php endforeach; ?>
      </select>
    </div>
    <button type="submit" class="btn btn-secondary"><i class="fa-solid fa-magnifying-glass"></i> <?php echo __('ফিল্টার', 'Filter'); ?></button>
    <?php if ($search !== '' || $filterCategory !== ''): ?>
      <a href="audio_hub.php" class="btn btn-outline"><?php echo __('রিসেট', 'Reset'); ?></a>
    <?php endif; ?>
  </form>
</div>

<!-- Table Card -->
<div class="card">
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 50px;"><?php echo __('ক্রম', 'SL'); ?></th>
          <th><?php echo __('শিরোনাম ও বিবরণ', 'Title & Description'); ?></th>
          <th><?php echo __('বক্তা বা স্কলার', 'Speaker or Scholar'); ?></th>
          <th><?php echo __('ক্যাটাগরি', 'Category'); ?></th>
          <th><?php echo __('সময়কাল', 'Duration'); ?></th>
          <th><?php echo __('উৎস', 'Source'); ?></th>
          <th><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th style="width: 140px; text-align: right;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($audioList)): ?>
          <tr>
            <td colspan="8" class="text-center py-4" style="color: var(--text-muted);">
              <i class="fa-solid fa-music-slash fa-2x mb-2"></i><br>
              <?php echo __('কোনো ইসলামিক অডিও ট্র্যাক পাওয়া যায়নি।', 'No Islamic audio tracks found.'); ?>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($audioList as $idx => $audio): ?>
            <tr>
              <td><?php echo toLangNum($audio['display_order']); ?></td>
              <td>
                <strong><?php echo htmlspecialchars($audio['title']); ?></strong>
                <?php if (!empty($audio['description'])): ?>
                  <div style="font-size: 12px; color: var(--text-muted); margin-top: 2px;"><?php echo htmlspecialchars($audio['description']); ?></div>
                <?php endif; ?>
                <?php if (!empty($audio['reference'])): ?>
                  <div style="font-size: 11px; color: var(--accent-mint); margin-top: 2px;"><i class="fa-solid fa-book-bookmark"></i> <?php echo htmlspecialchars($audio['reference']); ?></div>
                <?php endif; ?>
              </td>
              <td>
                <span class="badge" style="background: rgba(52, 211, 153, 0.12); color: var(--accent-mint); font-weight: 500;">
                  <i class="fa-solid fa-user" style="font-size: 10px; margin-right: 4px;"></i><?php echo htmlspecialchars($audio['speaker']); ?>
                </span>
              </td>
              <td>
                <?php 
                  $cat = $audio['category'];
                  echo isset($categories[$cat]) ? __($categories[$cat]['bn'], $categories[$cat]['en']) : htmlspecialchars($cat);
                ?>
              </td>
              <td>
                <?php 
                  $sec = (int)$audio['duration_seconds'];
                  $min = floor($sec / 60);
                  $remSec = $sec % 60;
                  $timeStr = sprintf("%02d:%02d", $min, $remSec);
                  echo toLangNum($timeStr) . ' ' . __('মি.', 'min');
                ?>
              </td>
              <td><span style="font-size: 12px; color: var(--text-muted);"><?php echo htmlspecialchars($audio['source']); ?></span></td>
              <td>
                <form method="POST" action="audio_hub.php" style="display:inline;">
                  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
                  <input type="hidden" name="action" value="toggle_status">
                  <input type="hidden" name="id" value="<?php echo $audio['id']; ?>">
                  <button type="submit" class="badge <?php echo $audio['is_active'] ? 'badge-success' : 'badge-danger'; ?>" style="border:none; cursor:pointer;">
                    <?php echo $audio['is_active'] ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
                  </button>
                </form>
              </td>
              <td style="text-align: right;">
                <button class="btn btn-sm btn-outline" onclick='openEditModal(<?php echo json_encode($audio, JSON_HEX_APOS | JSON_HEX_QUOT); ?>)' title="<?php echo __('সম্পাদনা', 'Edit'); ?>">
                  <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <form method="POST" action="audio_hub.php" style="display:inline;" onsubmit="return confirm('<?php echo __('আপনি কি নিশ্চিত এই অডিও ট্র্যাক মুছে ফেলতে চান?', 'Are you sure you want to delete this audio track?'); ?>');">
                  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="<?php echo $audio['id']; ?>">
                  <button type="submit" class="btn btn-sm btn-danger" title="<?php echo __('মুছে ফেলুন', 'Delete'); ?>"><i class="fa-solid fa-trash"></i></button>
                </form>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- Add / Edit Modal -->
<div id="audioModal" class="modal" style="display: none;">
  <div class="modal-dialog" style="max-width: 600px;">
    <div class="modal-content">
      <div class="modal-header">
        <h3 id="modalTitle"><?php echo __('নতুন অডিও', 'New Audio'); ?></h3>
        <button type="button" class="close" onclick="closeModal()">&times;</button>
      </div>
      <form method="POST" action="audio_hub.php">
        <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
        <input type="hidden" name="action" id="formAction" value="create">
        <input type="hidden" name="id" id="audioId" value="">

        <div class="modal-body">
          <div class="form-group mb-3">
            <label><?php echo __('শিরোনাম', 'Title'); ?> <span class="text-danger">*</span></label>
            <input type="text" name="title" id="inpTitle" class="form-control" required placeholder="<?php echo __('শিরোনাম লিখুন...', 'Enter audio title...'); ?>">
          </div>

          <div class="form-row" style="display:flex; gap: 15px; flex-wrap: wrap;">
            <div class="form-group mb-3" style="flex:1; min-width: 220px;">
              <label><?php echo __('বক্তা', 'Speaker'); ?> <span class="text-danger">*</span></label>
              <input type="text" name="speaker" id="inpSpeaker" class="form-control" required placeholder="<?php echo __('বক্তার নাম...', 'Speaker name...'); ?>">
            </div>
            <div class="form-group mb-3" style="flex:1; min-width: 220px;">
              <label><?php echo __('ক্যাটাগরি', 'Category'); ?> <span class="text-danger">*</span></label>
              <select name="category" id="inpCategory" class="form-control" required>
                <?php foreach ($categories as $k => $v): ?>
                  <option value="<?php echo $k; ?>"><?php echo __($v['bn'], $v['en']); ?></option>
                <?php endforeach; ?>
              </select>
            </div>
          </div>

          <div class="form-group mb-3">
            <label><?php echo __('অডিও লিংক', 'Audio URL'); ?> <span class="text-danger">*</span></label>
            <input type="url" name="audio_url" id="inpAudioUrl" class="form-control" required placeholder="https://cdn.islamicapi.org/audio/track.mp3">
          </div>

          <div class="form-row" style="display:flex; gap: 15px; flex-wrap: wrap;">
            <div class="form-group mb-3" style="flex:1; min-width: 220px;">
              <label><?php echo __('সময়কাল সেকেন্ড', 'Duration Seconds'); ?></label>
              <input type="number" name="duration_seconds" id="inpDuration" class="form-control" placeholder="1200" value="0">
            </div>
            <div class="form-group mb-3" style="flex:1; min-width: 220px;">
              <label><?php echo __('উৎস', 'Source'); ?></label>
              <input type="text" name="source" id="inpSource" class="form-control" placeholder="DeenOne Studio" value="DeenOne Studio">
            </div>
          </div>

          <div class="form-group mb-3">
            <label><?php echo __('বিবরণ', 'Description'); ?></label>
            <textarea name="description" id="inpDescription" class="form-control" rows="3" placeholder="<?php echo __('আলোচনার সারসংক্ষেপ...', 'Audio content summary...'); ?>"></textarea>
          </div>

          <div class="form-group mb-3">
            <label><?php echo __('রেফারেন্স', 'Reference'); ?></label>
            <input type="text" name="reference" id="inpReference" class="form-control" placeholder="<?php echo __('সহীহ বুখারী, রিয়াদুস সালেহীন ইত্যাদি', 'Sahih Bukhari, Riyad us Saliheen etc'); ?>">
          </div>

          <div class="form-row" style="display:flex; gap: 15px; align-items: center; flex-wrap: wrap;">
            <div class="form-group mb-3" style="flex:1; min-width: 220px;">
              <label><?php echo __('প্রদর্শন ক্রম', 'Display Order'); ?></label>
              <input type="number" name="display_order" id="inpOrder" class="form-control" value="1">
            </div>
            <div class="form-group mb-3" style="flex:1; min-width: 220px; padding-top: 15px;">
              <label style="cursor: pointer; display: flex; align-items: center; gap: 8px;">
                <input type="checkbox" name="is_active" id="inpIsActive" value="1" checked> 
                <span><?php echo __('সক্রিয় রাখুন', 'Keep Active'); ?></span>
              </label>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" onclick="closeModal()"><?php echo __('বাতিল', 'Cancel'); ?></button>
          <button type="submit" class="btn btn-primary" id="submitBtn"><?php echo __('সংরক্ষণ করুন', 'Save'); ?></button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
var txtAddModalTitle = <?php echo json_encode(__('নতুন অডিও', 'New Audio')); ?>;
var txtEditModalTitle = <?php echo json_encode(__('অডিও সম্পাদনা', 'Edit Audio')); ?>;

function openAddModal() {
  document.getElementById('modalTitle').innerText = txtAddModalTitle;
  document.getElementById('formAction').value = 'create';
  document.getElementById('audioId').value = '';
  document.getElementById('inpTitle').value = '';
  document.getElementById('inpSpeaker').value = '';
  document.getElementById('inpCategory').value = 'waz';
  document.getElementById('inpAudioUrl').value = '';
  document.getElementById('inpDuration').value = '0';
  document.getElementById('inpSource').value = 'DeenOne Studio';
  document.getElementById('inpDescription').value = '';
  document.getElementById('inpReference').value = '';
  document.getElementById('inpOrder').value = '1';
  document.getElementById('inpIsActive').checked = true;
  document.getElementById('audioModal').style.display = 'block';
}

function openEditModal(audio) {
  document.getElementById('modalTitle').innerText = txtEditModalTitle;
  document.getElementById('formAction').value = 'update';
  document.getElementById('audioId').value = audio.id;
  document.getElementById('inpTitle').value = audio.title || '';
  document.getElementById('inpSpeaker').value = audio.speaker || '';
  document.getElementById('inpCategory').value = audio.category || 'waz';
  document.getElementById('inpAudioUrl').value = audio.audio_url || '';
  document.getElementById('inpDuration').value = audio.duration_seconds || '0';
  document.getElementById('inpSource').value = audio.source || 'DeenOne Studio';
  document.getElementById('inpDescription').value = audio.description || '';
  document.getElementById('inpReference').value = audio.reference || '';
  document.getElementById('inpOrder').value = audio.display_order || '1';
  document.getElementById('inpIsActive').checked = (audio.is_active == 1);
  document.getElementById('audioModal').style.display = 'block';
}

function closeModal() {
  document.getElementById('audioModal').style.display = 'none';
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
