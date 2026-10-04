<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - PRIVACY POLICY MANAGEMENT
 * Ultra-Premium, Human-Crafted Single Unified Editor for DeenOne Privacy Policy
 * Features:
 *   - Clean Page Title (with / prefix)
 *   - Auto Last Updated Date with manual override & today reset
 *   - Full Working WYSIWYG Policy Body Content (Working Format, Heading & Font Size)
 *   - Directly Saves to MySQL Database
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = null;
try {
    $pdo = getDbConnection();
} catch (Throwable $e) {}

$todayEn = 'Last update: ' . date('F j, Y');
$defaultTitleEn = 'PRIVACY POLICY';

// Default clean body
$defaultEn = <<<HTML
<p>This <strong>Privacy Policy</strong> explains how DeenOne ("<strong>DeenOne</strong>", "<strong>us</strong>" or "<strong>we</strong>"), collects, uses and protects your personal information, which we may collect from you or that you may provide when you visit any of our websites, including <a href="https://deenone.top">https://deenone.top</a> and other digital properties that link to this Privacy Policy (our "<strong>Website(s)</strong>") or otherwise use our Website(s) or applications (collectively referred to as services the "<strong>Services</strong>").</p>

<p>This Privacy Policy <strong>does not apply to :</strong></p>

<ul>
  <li><strong>Information we may collect from you offline;</strong> or information collected by any third-party, including through any application or content (including advertising) that may link to or be accessible from or through the Websites.</li>
  <li><strong>Payment Information Processed by third-parties.</strong> Certain Services may require users to provide payment information directly to third-party payment processors.</li>
  <li><strong>Social Media Integration.</strong> Users may link their account with third-party social media platforms. When accounts are linked, the social media platform may share certain data with DeenOne as per its privacy policy. If you are accessing our Services through a social media account, please refer to the social media provider's privacy policy for information regarding their data collection.</li>
</ul>

<h3>1. Information We Collect</h3>
<p>To deliver an authentic, distraction-free Islamic lifestyle experience and accurate prayer timings, DeenOne collects minimal necessary information:</p>
<ul>
  <li><strong>Account & Profile:</strong> Name, phone number or email for account authentication, streak progress, and cloud backup across devices.</li>
  <li><strong>Location Data:</strong> Accessed on-device strictly to calculate accurate prayer times, sunrise/sunset, and Qibla compass direction. Location coordinates are never sold or permanently stored on our servers.</li>
  <li><strong>Worship & Habits Activity:</strong> Daily Salah logs, Quran reading progress, and Tasbih counts stored with end-to-end security.</li>
  <li><strong>Blood Donor Registry:</strong> Blood group, district, and contact numbers shared voluntarily for emergency blood requests.</li>
</ul>

<h3>2. Data Security & Encryption</h3>
<p>All sensitive transmissions are secured using SSL/TLS 256-bit encryption. We never sell, rent, or trade user personal information to third-party commercial advertisers.</p>

<h3>3. Data Retention & Account Deletion</h3>
<p>You have full ownership of your data. You may request permanent deletion of your account and all associated data at any time directly through the app settings or by contacting <code>privacy@deenone.top</code>.</p>
HTML;

function cleanLegacyHeadersAdmin($html) {
    $html = preg_replace('/<h1[^>]*>.*?<\/h1>/si', '', $html);
    $html = preg_replace('/<p[^>]*>\s*(Last Updated|Last update|সর্বশেষ হালনাগাদ).*?<\/p>/si', '', $html);
    return trim($html);
}

// -----------------------------------------------------------------------------
// FORM SUBMISSION HANDLER
// -----------------------------------------------------------------------------
if ($_SERVER['REQUEST_METHOD'] === 'POST' && (!isset($_POST['action']) || $_POST['action'] === 'save_policy')) {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('privacy_policy.php');
    }

    $titleEn = trim($_POST['privacy_title_en'] ?? 'PRIVACY POLICY');
    $updatedEn = trim($_POST['privacy_updated_at_en'] ?? '');

    if (empty($updatedEn)) {
        $updatedEn = $todayEn;
    }

    $contentEn = cleanLegacyHeadersAdmin(trim($_POST['privacy_policy_content_en'] ?? ''));

    $keys = [
        'privacy_title_en' => $titleEn,
        'privacy_title_bn' => 'প্রাইভেসি পলিসি',
        'privacy_updated_at_en' => $updatedEn,
        'privacy_policy_content_en' => $contentEn,
        'privacy_policy_content' => $contentEn, // sync for universal fallback
    ];

    if ($pdo) {
        try {
            $stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value) 
                VALUES (:key, :val) 
                ON DUPLICATE KEY UPDATE setting_value = :val2");
            foreach ($keys as $k => $v) {
                $stmt->execute([':key' => $k, ':val' => $v, ':val2' => $v]);
            }

            logAdminAction($pdo, 'UPDATE_PRIVACY_POLICY', 'SETTING', 'privacy_policy', 'Updated Privacy Policy content');
            setFlash('success', __('প্রাইভেসি পলিসি সফলভাবে সংরক্ষিত হয়েছে!', 'Privacy Policy saved successfully!'));
        } catch (Throwable $e) {
            setFlash('danger', __('সংরক্ষণ ব্যর্থ হয়েছে: ', 'Failed to save: ') . $e->getMessage());
        }
    } else {
        setFlash('warning', __('ডাটাবেজ সংযোগ পাওয়া যায়নি।', 'Database connection not available.'));
    }

    redirect('privacy_policy.php');
}

// Fetch Existing Settings
$settings = [];
if ($pdo) {
    try {
        $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN (
            'privacy_title_en', 'privacy_updated_at_en', 'privacy_policy_content_en', 'privacy_contact_email', 'app_name'
        )");
        if ($stmt) {
            while ($row = $stmt->fetch()) {
                $settings[$row['setting_key']] = $row['setting_value'];
            }
        }
    } catch (Throwable $e) {}
}

$titleEn = !empty($settings['privacy_title_en']) ? $settings['privacy_title_en'] : $defaultTitleEn;
$updatedEn = !empty($settings['privacy_updated_at_en']) ? $settings['privacy_updated_at_en'] : $todayEn;
$rawContentEn = !empty($settings['privacy_policy_content_en']) ? $settings['privacy_policy_content_en'] : $defaultEn;
$contentEn = cleanLegacyHeadersAdmin($rawContentEn);

$pageTitle = __('প্রাইভেসি পলিসি', 'Privacy Policy');
$activeNav = 'privacy_policy';
require_once __DIR__ . '/header.php';
?>

<style>
.privacy-container {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  box-shadow: 0 4px 16px rgba(0,0,0,0.03);
  overflow: hidden;
  margin-bottom: 30px;
}

.meta-fields-grid {
  padding: 20px 24px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color);
}

@media (max-width: 768px) {
  .meta-fields-grid {
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 16px;
  }
}

.form-group-clean {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label-clean {
  font-size: 13px;
  font-weight: 700;
  color: var(--text-heading);
  display: flex;
  align-items: center;
  gap: 6px;
}

.input-with-action {
  display: flex;
  align-items: center;
  gap: 8px;
}

.input-clean {
  width: 100%;
  background: var(--hover-bg);
  border: 1px solid var(--border-color);
  color: var(--text-main);
  padding: 9px 14px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  outline: none;
  transition: all 0.15s ease;
}

.input-clean:focus {
  background: var(--bg-card);
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.12);
}

.editor-wrapper {
  background: var(--bg-card);
}

.editor-section-header {
  padding: 14px 24px 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* Toolbar styling */
.editor-toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  padding: 10px 20px;
  background: var(--hover-bg);
  border-top: 1px solid var(--border-color);
  border-bottom: 1px solid var(--border-color);
}

.toolbar-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 6px;
  color: var(--text-main);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.toolbar-btn:hover {
  background: var(--primary);
  color: #fff;
  border-color: var(--primary);
}

.toolbar-select {
  height: 32px;
  padding: 0 10px;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  color: var(--text-main);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  outline: none;
}

.toolbar-select:focus {
  border-color: var(--primary);
}

.color-dropdown {
  position: relative;
  display: inline-block;
}

.color-palette {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.2);
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 5px;
  margin-top: 4px;
}

.color-swatch {
  width: 24px;
  height: 24px;
  border-radius: 4px;
  border: 1px solid rgba(0,0,0,0.1);
  cursor: pointer;
}

.color-swatch:hover {
  transform: scale(1.15);
}

.editor-canvas {
  min-height: 480px;
  max-height: 700px;
  overflow-y: auto;
  padding: 24px 28px;
  font-size: 15px;
  line-height: 1.7;
  color: var(--text-main);
  outline: none;
}

.editor-canvas h2 {
  font-size: 20px;
  font-weight: 800;
  margin: 24px 0 12px;
  color: var(--text-heading);
}

.editor-canvas h3 {
  font-size: 17px;
  font-weight: 700;
  margin: 18px 0 8px;
  color: var(--text-heading);
}

.editor-canvas h4 {
  font-size: 15px;
  font-weight: 700;
  margin: 14px 0 6px;
  color: var(--text-heading);
}

.editor-canvas p {
  margin-bottom: 16px;
}

.editor-canvas ul, .editor-canvas ol {
  margin: 12px 0 18px 24px;
}

.editor-canvas li {
  margin-bottom: 8px;
}

.editor-canvas strong {
  font-weight: 700;
  color: var(--text-heading);
}

.editor-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 16px 24px;
  border-top: 1px solid var(--border-color);
  background: var(--hover-bg);
}
</style>

<!-- Top Title & Live Link Header -->
<div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px; margin-bottom: 20px;">
  <div>
    <h2 style="font-size: 20px; font-weight: 800; color: var(--text-heading); margin: 0; display: flex; align-items: center; gap: 8px;">
      <span style="color: #0284c7; font-weight: 900;">/</span>
      <span><?php echo __('প্রাইভেসি পলিসি', 'Privacy Policy'); ?></span>
    </h2>
  </div>

  <div style="display: flex; align-items: center; flex-wrap: wrap; gap: 8px;">
    <!-- Live Preview Button -->
    <a href="../privacy/index.php" target="_blank" class="btn btn-secondary btn-sm" style="display: inline-flex; align-items: center; gap: 6px; font-weight: 700;">
      <i class="fa-solid fa-arrow-up-right-from-square"></i>
      <span><?php echo __('লাইভ প্রিভিউ', 'View Live Page'); ?></span>
    </a>
  </div>
</div>

<form id="privacyForm" method="POST" action="privacy_policy.php" onsubmit="syncEditorContent()">
  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
  <input type="hidden" name="action" value="save_policy">

  <div class="privacy-container">
    <!-- Title and Last Update Section -->
    <div class="meta-fields-grid">
      
      <!-- Heading / Page Title -->
      <div class="form-group-clean">
        <label class="form-label-clean" for="inputTitleEn">
          <i class="fa-solid fa-heading" style="color: #0284c7;"></i>
          <span><?php echo __('পৃষ্ঠার শিরোনাম', 'Page Heading'); ?></span>
        </label>
        <input type="text" name="privacy_title_en" id="inputTitleEn" class="input-clean" value="<?php echo htmlspecialchars($titleEn); ?>" placeholder="PRIVACY POLICY">
      </div>

      <!-- Last Updated Date -->
      <div class="form-group-clean">
        <label class="form-label-clean" for="inputUpdatedEn">
          <i class="fa-regular fa-calendar-check" style="color: var(--primary);"></i>
          <span><?php echo __('হালনাগাদ তারিখ', 'Last Updated Date'); ?></span>
        </label>
        <div class="input-with-action">
          <input type="text" name="privacy_updated_at_en" id="inputUpdatedEn" class="input-clean" value="<?php echo htmlspecialchars($updatedEn); ?>" placeholder="Last update: <?php echo date('F j, Y'); ?>">
          <button type="button" class="btn btn-secondary btn-sm" onclick="setTodayDate()" title="<?php echo __('আজকের তারিখ সেট করুন', 'Set Today\'s Date'); ?>" style="white-space: nowrap; height: 38px;">
            <i class="fa-solid fa-arrows-rotate"></i> <?php echo __('Today', 'Today'); ?>
          </button>
        </div>
      </div>

    </div>

    <!-- Main Content & WYSIWYG Editor -->
    <div class="editor-wrapper">
      <div class="editor-section-header">
        <div style="font-size: 13.5px; font-weight: 700; color: var(--text-heading); display: flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-align-left" style="color: #8b5cf6;"></i>
          <span><?php echo __('মূল বিবরণ', 'Main Content'); ?></span>
        </div>
      </div>

      <!-- Rich Toolbar -->
      <div class="editor-toolbar">
        <!-- Paragraph / Heading Selection -->
        <select class="toolbar-select" id="formatSelect" onfocus="saveSelection()" onchange="applyFormatBlock(this.value); this.selectedIndex=0;" title="<?php echo __('ফরম্যাট / হেডিং', 'Format / Heading'); ?>">
          <option value="" selected disabled><?php echo __('ফরম্যাট', 'Format'); ?></option>
          <option value="p"><?php echo __('সাধারণ টেক্সট', 'Paragraph'); ?></option>
          <option value="h2"><?php echo __('বড় হেডিং', 'Heading 2'); ?></option>
          <option value="h3"><?php echo __('মাঝারি হেডিং', 'Heading 3'); ?></option>
          <option value="h4"><?php echo __('ছোট হেডিং', 'Heading 4'); ?></option>
        </select>

        <!-- Font Size Selection -->
        <select class="toolbar-select" id="sizeSelect" onfocus="saveSelection()" onchange="applyFontSize(this.value); this.selectedIndex=0;" title="<?php echo __('ফন্ট সাইজ', 'Font Size'); ?>">
          <option value="" selected disabled><?php echo __('সাইজ', 'Size'); ?></option>
          <option value="1">10px</option>
          <option value="2">13px</option>
          <option value="3">15px (Normal)</option>
          <option value="4">18px (Medium)</option>
          <option value="5">24px (Large)</option>
          <option value="6">32px (X-Large)</option>
        </select>

        <span style="width: 1px; height: 20px; background: var(--border-color); margin: 0 4px;"></span>

        <!-- Basic Formatting Buttons -->
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('bold')" title="Bold (বোল্ড)"><b>B</b></button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('italic')" title="Italic (ইটালিক)"><i>I</i></button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('underline')" title="Underline (আন্ডারলাইন)"><u>U</u></button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('strikeThrough')" title="Strikethrough"><s>S</s></button>

        <span style="width: 1px; height: 20px; background: var(--border-color); margin: 0 4px;"></span>

        <!-- Text Color Picker -->
        <div class="color-dropdown">
          <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="toggleColorPopup('textColorPopup')" title="<?php echo __('টেক্সট কালার', 'Text Color'); ?>">
            <span style="font-weight: 800; border-bottom: 3px solid #0284c7; line-height: 1;">A</span>
          </button>
          <div class="color-palette" id="textColorPopup" style="display: none;">
            <button type="button" class="color-swatch" style="background:#000000;" onmousedown="event.preventDefault()" onclick="applyTextColor('#000000')"></button>
            <button type="button" class="color-swatch" style="background:#0284c7;" onmousedown="event.preventDefault()" onclick="applyTextColor('#0284c7')"></button>
            <button type="button" class="color-swatch" style="background:#10b981;" onmousedown="event.preventDefault()" onclick="applyTextColor('#10b981')"></button>
            <button type="button" class="color-swatch" style="background:#f59e0b;" onmousedown="event.preventDefault()" onclick="applyTextColor('#f59e0b')"></button>
            <button type="button" class="color-swatch" style="background:#ef4444;" onmousedown="event.preventDefault()" onclick="applyTextColor('#ef4444')"></button>
            <button type="button" class="color-swatch" style="background:#8b5cf6;" onmousedown="event.preventDefault()" onclick="applyTextColor('#8b5cf6')"></button>
            <button type="button" class="color-swatch" style="background:#475569;" onmousedown="event.preventDefault()" onclick="applyTextColor('#475569')"></button>
            <button type="button" class="color-swatch" style="background:#047857;" onmousedown="event.preventDefault()" onclick="applyTextColor('#047857')"></button>
            <button type="button" class="color-swatch" style="background:#1e40af;" onmousedown="event.preventDefault()" onclick="applyTextColor('#1e40af')"></button>
            <button type="button" class="color-swatch" style="background:#b91c1c;" onmousedown="event.preventDefault()" onclick="applyTextColor('#b91c1c')"></button>
          </div>
        </div>

        <!-- Highlight Background Color -->
        <div class="color-dropdown">
          <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="toggleColorPopup('hlColorPopup')" title="<?php echo __('হাইলাইট কালার', 'Highlight Color'); ?>">
            <i class="fa-solid fa-highlighter" style="color: #f59e0b; font-size: 12px;"></i>
          </button>
          <div class="color-palette" id="hlColorPopup" style="display: none;">
            <button type="button" class="color-swatch" style="background:#fef08a;" onmousedown="event.preventDefault()" onclick="applyHlColor('#fef08a')"></button>
            <button type="button" class="color-swatch" style="background:#d1fae5;" onmousedown="event.preventDefault()" onclick="applyHlColor('#d1fae5')"></button>
            <button type="button" class="color-swatch" style="background:#e0f2fe;" onmousedown="event.preventDefault()" onclick="applyHlColor('#e0f2fe')"></button>
            <button type="button" class="color-swatch" style="background:#ffe4e6;" onmousedown="event.preventDefault()" onclick="applyHlColor('#ffe4e6')"></button>
            <button type="button" class="color-swatch" style="background:#f3e8ff;" onmousedown="event.preventDefault()" onclick="applyHlColor('#f3e8ff')"></button>
          </div>
        </div>

        <span style="width: 1px; height: 20px; background: var(--border-color); margin: 0 4px;"></span>

        <!-- Lists and Links -->
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('insertUnorderedList')" title="Bullet List (বুলেট তালিকা)">:&#8801;</button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('insertOrderedList')" title="Numbered List (নাম্বার তালিকা)">1.</button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="insertCustomLink()" title="Link (লিঙ্ক)">🔗</button>

        <span style="width: 1px; height: 20px; background: var(--border-color); margin: 0 4px;"></span>

        <!-- Alignments -->
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('justifyLeft')" title="Align Left"><i class="fa-solid fa-align-left" style="font-size: 11px;"></i></button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('justifyCenter')" title="Align Center"><i class="fa-solid fa-align-center" style="font-size: 11px;"></i></button>
        <button type="button" class="toolbar-btn" onmousedown="event.preventDefault()" onclick="execFormat('justifyRight')" title="Align Right"><i class="fa-solid fa-align-right" style="font-size: 11px;"></i></button>
      </div>

      <!-- The Editable Canvas (English Policy Content) -->
      <div class="editor-canvas" id="activeEditorCanvas" contenteditable="true">
        <?php echo $contentEn; ?>
      </div>
    </div>

    <!-- Hidden Storage Fields -->
    <textarea name="privacy_policy_content_en" id="storedContentEn" style="display: none;"><?php echo htmlspecialchars($contentEn); ?></textarea>

    <!-- Footer Action Bar -->
    <div class="editor-footer">
      <button type="submit" class="btn btn-primary" style="padding: 10px 28px; font-weight: 700; font-size: 14px;">
        <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ করুন', 'Save Changes'); ?>
      </button>
    </div>
  </div>
</form>

<script>
let savedSelectionRange = null;

function saveSelection() {
  const sel = window.getSelection();
  if (sel && sel.rangeCount > 0) {
    const range = sel.getRangeAt(0);
    const canvas = document.getElementById('activeEditorCanvas');
    if (canvas && (canvas === range.commonAncestorContainer || canvas.contains(range.commonAncestorContainer))) {
      savedSelectionRange = range.cloneRange();
    }
  }
}

function restoreSelection() {
  const canvas = document.getElementById('activeEditorCanvas');
  if (!canvas) return;
  canvas.focus();
  if (savedSelectionRange) {
    const sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(savedSelectionRange);
  }
}

const editorCanvas = document.getElementById('activeEditorCanvas');
if (editorCanvas) {
  editorCanvas.addEventListener('mouseup', saveSelection);
  editorCanvas.addEventListener('keyup', saveSelection);
  editorCanvas.addEventListener('touchend', saveSelection);
  editorCanvas.addEventListener('focus', saveSelection);
}

function execFormat(cmd, val = null) {
  restoreSelection();
  document.execCommand(cmd, false, val);
  saveSelection();
  document.getElementById('activeEditorCanvas').focus();
}

function applyFormatBlock(tag) {
  if (!tag) return;
  restoreSelection();
  try {
    document.execCommand('formatBlock', false, tag);
  } catch (e) {
    try {
      document.execCommand('formatBlock', false, '<' + tag + '>');
    } catch (err) {}
  }
  saveSelection();
  document.getElementById('activeEditorCanvas').focus();
}

function applyFontSize(size) {
  if (!size) return;
  restoreSelection();
  document.execCommand('fontSize', false, size);
  saveSelection();
  document.getElementById('activeEditorCanvas').focus();
}

function toggleColorPopup(id) {
  const el = document.getElementById(id);
  const isShown = el.style.display === 'grid';
  document.querySelectorAll('.color-palette').forEach(p => p.style.display = 'none');
  if (!isShown) el.style.display = 'grid';
}

function applyTextColor(color) {
  execFormat('foreColor', color);
  document.getElementById('textColorPopup').style.display = 'none';
}

function applyHlColor(color) {
  execFormat('hiliteColor', color);
  document.getElementById('hlColorPopup').style.display = 'none';
}

function insertCustomLink() {
  restoreSelection();
  const url = prompt('<?php echo __('লিঙ্কের URL লিখুন:', 'Enter Link URL:'); ?>', 'https://');
  if (url && url.trim() !== '') {
    execFormat('createLink', url.trim());
  }
}

function setTodayDate() {
  document.getElementById('inputUpdatedEn').value = '<?php echo $todayEn; ?>';
}

function syncEditorContent() {
  const canvasHtml = document.getElementById('activeEditorCanvas').innerHTML;
  document.getElementById('storedContentEn').value = canvasHtml;
}

// Close color popups on outside click
document.addEventListener('click', function(e) {
  if (!e.target.closest('.color-dropdown')) {
    document.querySelectorAll('.color-palette').forEach(p => p.style.display = 'none');
  }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
