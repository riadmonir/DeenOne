<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - AUTHENTIC DUAS & SUPPLICATIONS (দু’আ ও যিকির)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Handle Action (Add, Edit, Delete)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('duas.php');
    }

    if ($action === 'create' || $action === 'update') {
        $catSlug = trim($_POST['category_slug'] ?? 'morning_evening');
        $titleBn = trim($_POST['title_bn'] ?? '');
        $arabicText = trim($_POST['arabic_text'] ?? '');
        $pronBn = trim($_POST['pronunciation_bn'] ?? '');
        $transBn = trim($_POST['translation_bn'] ?? '');
        $ref = trim($_POST['reference'] ?? '');
        $virtueBn = trim($_POST['virtue_bn'] ?? '');
        $audioUrl = trim($_POST['audio_url'] ?? '');

        if (empty($titleBn) || empty($arabicText) || empty($transBn)) {
            setFlash('danger', __('দু’আর শিরোনাম, আরবি মূল পাঠ এবং বাংলা অর্থ আবশ্যক।', 'Dua title, Arabic text, and Bengali translation are required.'));
        } else {
            try {
                if ($action === 'create') {
                    $uid = trim($_POST['dua_uid'] ?? '');
                    if (empty($uid)) {
                        $uid = 'DUA_' . strtoupper(bin2hex(random_bytes(4)));
                    }
                    $stmt = $pdo->prepare("INSERT INTO duas (dua_uid, category_slug, title_bn, arabic_text, pronunciation_bn, translation_bn, reference, virtue_bn, audio_url) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    $stmt->execute([$uid, $catSlug, $titleBn, $arabicText, $pronBn, $transBn, $ref, $virtueBn, $audioUrl]);
                    logAdminAction($pdo, 'CREATE_DUA', 'duas', $uid, 'নতুন দু’আ: ' . $titleBn);
                    setFlash('success', __('নতুন সহীহ দু’আ সফলভাবে যুক্ত হয়েছে!', 'New authentic Dua added successfully!'));
                } else {
                    $id = (int)($_POST['id'] ?? 0);
                    $stmt = $pdo->prepare("UPDATE duas SET category_slug = ?, title_bn = ?, arabic_text = ?, pronunciation_bn = ?, translation_bn = ?, reference = ?, virtue_bn = ?, audio_url = ? WHERE id = ?");
                    $stmt->execute([$catSlug, $titleBn, $arabicText, $pronBn, $transBn, $ref, $virtueBn, $audioUrl, $id]);
                    logAdminAction($pdo, 'UPDATE_DUA', 'duas', (string)$id, 'দু’আ আপডেট: ' . $titleBn);
                    setFlash('success', __('দু’আ তথ্য সফলভাবে আপডেট হয়েছে!', 'Dua details updated successfully!'));
                }
            } catch (Exception $e) {
                setFlash('danger', __('ডাটাবেস ত্রুটি: ', 'Database error: ') . $e->getMessage());
            }
        }
    }
    redirect('duas.php');
}

if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("DELETE FROM duas WHERE id = ?");
        $stmt->execute([$id]);
        logAdminAction($pdo, 'DELETE_DUA', 'duas', (string)$id, 'দু’আ মুছে ফেলা হয়েছে');
        setFlash('success', __('দু’আ সফলভাবে মুছে ফেলা হয়েছে।', 'Dua deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Failed to delete: ') . $e->getMessage());
    }
    redirect('duas.php');
}

// Fetch categories
$categories = [];
try {
    $categories = $pdo->query("SELECT * FROM dua_categories ORDER BY display_order ASC")->fetchAll();
} catch (Exception $e) {}

// Filtering & Search
$selectedCat = $_GET['category'] ?? '';
$search = trim($_GET['search'] ?? '');

$where = [];
$params = [];
if (!empty($selectedCat)) {
    $where[] = "d.category_slug = ?";
    $params[] = $selectedCat;
}
if (!empty($search)) {
    $where[] = "(d.title_bn LIKE ? OR d.translation_bn LIKE ? OR d.reference LIKE ?)";
    $term = "%$search%";
    $params[] = $term;
    $params[] = $term;
    $params[] = $term;
}
$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

$duas = [];
try {
    $sql = "SELECT d.*, c.name_bn as cat_name 
            FROM duas d 
            LEFT JOIN dua_categories c ON d.category_slug = c.category_slug 
            $whereSql 
            ORDER BY d.id DESC";
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $duas = $stmt->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('মাসনূন দু’আ', 'Masnun Duas');
$activeNav = 'duas';
require_once __DIR__ . '/header.php';
?>

<!-- Search Toolbar -->
<div class="card" style="margin-bottom: 20px;">
  <div class="card-body" style="padding: 16px 20px;">
    <form method="GET" action="duas.php" style="display: flex; gap: 14px; align-items: center; flex-wrap: wrap;">
      <div style="flex: 1; min-width: 220px;">
        <input type="text" name="search" class="form-control" placeholder="<?php echo __('দু’আর নাম, অর্থ বা রেফারেন্স দিয়ে খুঁজুন...', 'Search by title, meaning, or reference...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
      </div>
      <div style="width: 240px;">
        <select name="category" class="form-control" onchange="this.form.submit()">
          <option value="">-- <?php echo __('সকল দু’আ ক্যাটাগরি', 'All Dua Categories'); ?> --</option>
          <?php foreach ($categories as $cat): ?>
            <option value="<?php echo htmlspecialchars($cat['category_slug']); ?>" <?php echo $selectedCat === $cat['category_slug'] ? 'selected' : ''; ?>>
              <?php echo htmlspecialchars($cat['name_bn']); ?>
            </option>
          <?php endforeach; ?>
        </select>
      </div>
      <button type="submit" class="btn btn-secondary">🔍 <?php echo __('ফিল্টার', 'Filter'); ?></button>
      <?php if (!empty($selectedCat) || !empty($search)): ?>
        <a href="duas.php" class="btn btn-secondary"><?php echo __('রিসেট', 'Reset'); ?></a>
      <?php endif; ?>
      <button type="button" class="btn btn-primary" style="margin-left: auto;" onclick="openAddDuaModal()">
        <i class="fa-solid fa-plus"></i> <?php echo __('নতুন দু’আ', 'New Dua'); ?>
      </button>
    </form>
  </div>
</div>

<!-- Duas Table -->
<div class="card">
  <div class="card-header">
    <div class="card-title">
      <i class="fa-solid fa-hands-praying"></i> <?php echo __('দু’আ তালিকা', 'Dua List'); ?> 
      <span class="badge badge-info" style="margin-left: 8px;">
        <?php echo toLangNum(count($duas)) . ' ' . __('টি রেকর্ড', 'records'); ?>
      </span>
    </div>
  </div>
  <div class="table-responsive">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 50px;"><?php echo __('আইডি', 'ID'); ?></th>
          <th><?php echo __('দু’আর নাম ও ক্যাটাগরি', 'Dua Title & Category'); ?></th>
          <th><?php echo __('আরবি পাঠ ও উচ্চারণ', 'Arabic Text & Pronunciation'); ?></th>
          <th><?php echo __('বাংলা অর্থ ও ফজিলত', 'Bengali Meaning & Virtue'); ?></th>
          <th><?php echo __('সহীহ রেফারেন্স', 'Authentic Reference'); ?></th>
          <th style="text-align: right;"><?php echo __('পদক্ষেপ', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($duas)): ?>
          <tr><td colspan="6" style="text-align: center; color: var(--text-dim);"><?php echo __('কোনো দু’আ পাওয়া যায়নি।', 'No Duas found.'); ?></td></tr>
        <?php else: ?>
          <?php foreach ($duas as $d): ?>
            <tr>
              <td>#<?php echo toLangNum($d['id']); ?></td>
              <td>
                <div style="font-weight: 700; font-size: 15px; margin-bottom: 4px;"><?php echo htmlspecialchars($d['title_bn']); ?></div>
                <span class="badge badge-info"><?php echo htmlspecialchars($d['cat_name'] ?? $d['category_slug']); ?></span>
              </td>
              <td style="max-width: 320px;">
                <div class="arabic-input" style="font-size: 17px; margin-bottom: 6px; color: var(--primary-light);">
                  <?php echo htmlspecialchars(mb_substr($d['arabic_text'], 0, 90)) . '...'; ?>
                </div>
                <?php if (!empty($d['pronunciation_bn'])): ?>
                  <div style="font-size: 12px; color: var(--text-muted); line-height: 1.4;">
                    <?php echo __('উচ্চারণ:', 'Pronunciation:'); ?> <?php echo htmlspecialchars(mb_substr($d['pronunciation_bn'], 0, 70)) . '...'; ?>
                  </div>
                <?php endif; ?>
              </td>
              <td style="font-size: 13px; max-width: 300px; line-height: 1.4;">
                <div style="color: var(--text-main); margin-bottom: 4px;">
                  <?php echo htmlspecialchars(mb_substr($d['translation_bn'], 0, 110)) . '...'; ?>
                </div>
                <?php if (!empty($d['virtue_bn'])): ?>
                  <div style="font-size: 11px; color: var(--accent-gold);">
                    ⭐ <?php echo htmlspecialchars(mb_substr($d['virtue_bn'], 0, 60)); ?>
                  </div>
                <?php endif; ?>
              </td>
              <td style="font-size: 12px; color: var(--accent-gold); white-space: nowrap;">
                📖 <?php echo htmlspecialchars($d['reference'] ?? __('সহীহ হাদীস', 'Authentic Hadith')); ?>
              </td>
              <td style="text-align: right; white-space: nowrap;">
                <button type="button" class="btn btn-secondary btn-sm" onclick="editDua(<?php echo htmlspecialchars(json_encode($d)); ?>)">
                  ✏️ <?php echo __('সম্পাদনা', 'Edit'); ?>
                </button>
                <button type="button" class="btn btn-danger btn-sm" onclick="confirmDelete('duas.php?delete=<?php echo $d['id']; ?>', '<?php echo __('এই দু’আটি মুছে ফেলতে চান?', 'Are you sure you want to delete this Dua?'); ?>')">
                  🗑️ <?php echo __('ডিলিট', 'Delete'); ?>
                </button>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>
</div>

<!-- Add/Edit Dua Modal -->
<div class="modal-backdrop" id="duaModal">
  <div class="modal-window" style="max-width: 680px;">
    <div class="modal-header">
      <div class="modal-title" id="duaModalTitle"><i class="fa-solid fa-plus"></i> <?php echo __('নতুন দু’আ', 'New Dua'); ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('duaModal')">&times;</button>
    </div>
    <form method="POST" action="duas.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="duaAction" value="create">
      <input type="hidden" name="id" id="duaId" value="">

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('শিরোনাম', 'Title'); ?></label>
            <input type="text" name="title_bn" id="inputDuaTitle" class="form-control" placeholder="<?php echo __('যেমন: সকালের সাইয়্যিদুল ইস্তিগফার', 'e.g. Sayyidul Istighfar'); ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ক্যাটাগরি', 'Category'); ?></label>
            <select name="category_slug" id="inputDuaCat" class="form-control">
              <?php foreach ($categories as $cat): ?>
                <option value="<?php echo htmlspecialchars($cat['category_slug']); ?>">
                  <?php echo htmlspecialchars($cat['name_bn']); ?>
                </option>
              <?php endforeach; ?>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('আরবি পাঠ', 'Arabic Text'); ?></label>
          <textarea name="arabic_text" id="inputDuaArabic" class="form-control arabic-input" rows="3" placeholder="اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ..." required></textarea>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('উচ্চারণ', 'Pronunciation'); ?></label>
          <textarea name="pronunciation_bn" id="inputDuaPron" class="form-control" rows="2" placeholder="<?php echo __('আল্লাহুম্মা আনতা রব্বী...', 'Allahumma anta rabbi...'); ?>"></textarea>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('বাংলা অর্থ', 'Bengali Meaning'); ?></label>
          <textarea name="translation_bn" id="inputDuaTrans" class="form-control" rows="3" placeholder="<?php echo __('হে আল্লাহ! আপনি আমার প্রতিপালক...', 'O Allah! You are my Lord...'); ?>" required></textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label"><?php echo __('রেফারেন্স', 'Reference'); ?></label>
            <input type="text" name="reference" id="inputDuaRef" class="form-control" placeholder="<?php echo __('যেমন: সহীহ বুখারী: ৬৩০৬', 'e.g. Sahih al-Bukhari: 6306'); ?>">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ফজিলত', 'Virtue'); ?></label>
            <input type="text" name="virtue_bn" id="inputDuaVirtue" class="form-control" placeholder="<?php echo __('যেমন: দিনে পাঠ করে মারা গেলে জান্নাতী', 'e.g. Reciting by day brings Paradise'); ?>">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label"><?php echo __('অডিও লিংক', 'Audio Link'); ?></label>
          <input type="url" name="audio_url" id="inputDuaAudio" class="form-control" placeholder="https://example.com/audio/dua.mp3">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('duaModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary" id="btnDuaSubmit"><?php echo __('সংরক্ষণ করুন', 'Save'); ?></button>
      </div>
    </form>
  </div>
</div>

<script>
const isDuaEnglish = <?php echo isEn() ? 'true' : 'false'; ?>;

function openAddDuaModal() {
  document.getElementById('duaModalTitle').innerHTML = isDuaEnglish ? '<i class="fa-solid fa-plus"></i> New Dua' : '<i class="fa-solid fa-plus"></i> নতুন দু’আ';
  document.getElementById('duaAction').value = 'create';
  document.getElementById('duaId').value = '';
  document.getElementById('inputDuaTitle').value = '';
  document.getElementById('inputDuaArabic').value = '';
  document.getElementById('inputDuaPron').value = '';
  document.getElementById('inputDuaTrans').value = '';
  document.getElementById('inputDuaRef').value = '';
  document.getElementById('inputDuaVirtue').value = '';
  document.getElementById('inputDuaAudio').value = '';
  document.getElementById('btnDuaSubmit').textContent = isDuaEnglish ? 'Save' : 'সংরক্ষণ করুন';
  openModal('duaModal');
}

function editDua(d) {
  document.getElementById('duaModalTitle').innerHTML = isDuaEnglish ? '<i class="fa-solid fa-pen-to-square"></i> Edit Dua' : '<i class="fa-solid fa-pen-to-square"></i> দু’আ সম্পাদনা';
  document.getElementById('duaAction').value = 'update';
  document.getElementById('duaId').value = d.id;
  document.getElementById('inputDuaTitle').value = d.title_bn;
  document.getElementById('inputDuaCat').value = d.category_slug;
  document.getElementById('inputDuaArabic').value = d.arabic_text;
  document.getElementById('inputDuaPron').value = d.pronunciation_bn || '';
  document.getElementById('inputDuaTrans').value = d.translation_bn;
  document.getElementById('inputDuaRef').value = d.reference || '';
  document.getElementById('inputDuaVirtue').value = d.virtue_bn || '';
  document.getElementById('inputDuaAudio').value = d.audio_url || '';
  document.getElementById('btnDuaSubmit').textContent = isDuaEnglish ? 'Save Changes' : 'পরিবর্তন সংরক্ষণ';
  openModal('duaModal');
}

if (new URLSearchParams(window.location.search).get('action') === 'new') {
  openAddDuaModal();
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
