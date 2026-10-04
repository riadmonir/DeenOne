<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - TERMS OF SERVICE & CONDITIONS SUITE
 * Ultra-Premium, Dedicated WYSIWYG Editor for Terms & Conditions
 * Features:
 *   - Clean Page Title
 *   - Auto & Custom Last Updated Date
 *   - Full Working WYSIWYG Editor (Heading, Bold, List, Clear Formatting)
 *   - External Web URL configuration
 *   - Live Mobile In-App Preview Card
 *   - Directly Saves to MySQL Database (app_settings)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

$todayEn = 'Last update: ' . date('F j, Y');
$defaultTitleEn = 'TERMS OF SERVICE';

// Default authentic terms
$defaultTermsEn = <<<HTML
<p>Welcome to <strong>DeenOne</strong> ("we", "our", or "us"). By accessing or using the DeenOne mobile application and associated web services (<a href="https://deenone.top">https://deenone.top</a>), you agree to be bound by these Terms of Service.</p>

<h3>1. Authentic Islamic Content & Non-Commercial Use</h3>
<p>DeenOne provides Quran verses, Sahih Hadiths, authentic Duas, Prayer times, Qibla direction, and Islamic educational tools. All content is compiled from verified Islamic sources strictly for personal, non-commercial worship and spiritual advancement.</p>

<h3>2. User Conduct & Community Guidelines</h3>
<p>In community discussions, knowledge battles, and quizzes, users must maintain polite, respectful, and Islamic decorum. Any hate speech, sectarian insults, harassment, or spreading of unverified religious assertions is strictly prohibited and subject to immediate account termination.</p>

<h3>3. Account Integrity & Security</h3>
<p>You are responsible for safeguarding your authentication credentials. You agree not to attempt reverse engineering, automated scraping, or denial of service attacks against DeenOne APIs.</p>

<h3>4. Modifications & Updates</h3>
<p>We may update these terms periodically. Continued use of the application after changes constitute acceptance of the updated terms.</p>
HTML;

// Handle POST Save
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর বা মেয়াদোত্তীর্ণ। পুনরায় চেষ্টা করুন।', 'Invalid or expired security token. Please try again.'));
        redirect('terms_conditions.php');
    }

    $title = trim($_POST['terms_title'] ?? 'TERMS OF SERVICE');
    $updatedAt = trim($_POST['terms_updated_at'] ?? $todayEn);
    $content = trim($_POST['terms_service_content'] ?? '');
    $url = trim($_POST['terms_service_url'] ?? 'https://deenone.top/terms');

    $saveList = [
        'terms_title' => $title,
        'terms_updated_at' => $updatedAt,
        'terms_service_content' => $content,
        'terms_service_url' => $url
    ];

    try {
        $stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value, description) 
                               VALUES (:key, :val, :desc) 
                               ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value), updated_at = NOW()");
        
        foreach ($saveList as $k => $v) {
            $desc = "Terms settings: " . $k;
            $stmt->execute([':key' => $k, ':val' => $v, ':desc' => $desc]);
        }

        logAdminAction($pdo, 'UPDATE_TERMS_CONDITIONS', 'app_settings', null, 'ব্যবহারের শর্তাবলী সফলভাবে আপডেট করা হয়েছে');
        setFlash('success', __('ব্যবহারের শর্তাবলী সফলভাবে সংরক্ষিত হয়েছে!', 'Terms of Service saved successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('সংরক্ষণ ব্যর্থ হয়েছে: ', 'Failed to save: ') . $e->getMessage());
    }

    redirect('terms_conditions.php');
}

// Fetch current terms
$current = [];
try {
    $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN (
        'terms_title', 'terms_updated_at', 'terms_service_content', 'terms_service_url'
    )");
    while ($r = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $current[$r['setting_key']] = $r['setting_value'];
    }
} catch (Exception $e) {}

$termsTitle = !empty($current['terms_title']) ? $current['terms_title'] : $defaultTitleEn;
$termsUpdatedAt = !empty($current['terms_updated_at']) ? $current['terms_updated_at'] : $todayEn;
$termsContent = !empty($current['terms_service_content']) ? $current['terms_service_content'] : $defaultTermsEn;
$termsUrl = !empty($current['terms_service_url']) ? $current['terms_service_url'] : 'https://deenone.top/terms';

$pageTitle = __('ব্যবহারের শর্তাবলী', 'Terms of Service');
$activeNav = 'terms_conditions';
require_once __DIR__ . '/header.php';
?>

<div class="page-container" style="max-width: 1200px; margin: 0 auto; padding-bottom: 50px;">

  <!-- Header Banner -->
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
    <div style="display: flex; align-items: center; gap: 10px;">
      <span style="display: inline-flex; align-items: center; justify-content: center; width: 38px; height: 38px; border-radius: 10px; background: rgba(59, 130, 246, 0.12); color: #3b82f6; font-size: 18px;">
        <i class="fa-solid fa-file-contract"></i>
      </span>
      <h1 style="font-size: 22px; font-weight: 800; color: var(--text-heading); margin: 0;">
        <?php echo __('ব্যবহারের শর্তাবলী', 'Terms of Service'); ?>
      </h1>
    </div>

    <div>
      <a href="<?php echo htmlspecialchars($termsUrl); ?>" target="_blank" class="btn-topbar-pill" style="font-size: 12px; padding: 6px 12px; text-decoration: none; display: inline-flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-arrow-up-right-from-square"></i> <?php echo __('ওয়েব পেজ দেখুন', 'Live Web View'); ?>
      </a>
    </div>
  </div>

  <form method="POST" action="terms_conditions.php" onsubmit="syncTermsWysiwyg()">
    <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

    <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px; margin-bottom: 24px;">
      
      <!-- Meta Fields (Title, Updated Date, Web URL) -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 16px; margin-bottom: 20px;">
        <div class="form-group">
          <label class="form-label" style="font-weight: 600; font-size: 13px;">
            <?php echo __('পৃষ্ঠার শিরোনাম', 'Document Title'); ?>
          </label>
          <input type="text" name="terms_title" class="form-control" value="<?php echo htmlspecialchars($termsTitle); ?>" required>
        </div>

        <div class="form-group">
          <label class="form-label" style="font-weight: 600; font-size: 13px;">
            <?php echo __('হালনাগাদ তারিখ', 'Last Updated'); ?>
          </label>
          <div style="display: flex; gap: 8px;">
            <input type="text" name="terms_updated_at" id="inputTermsUpdated" class="form-control" value="<?php echo htmlspecialchars($termsUpdatedAt); ?>">
            <button type="button" class="btn-topbar-pill" onclick="document.getElementById('inputTermsUpdated').value='<?php echo $todayEn; ?>';" style="font-size: 11px; white-space: nowrap;">
              <?php echo __('আজকের তারিখ', 'Set Today'); ?>
            </button>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label" style="font-weight: 600; font-size: 13px;">
            <?php echo __('ওয়েব লিংক', 'External Web URL'); ?>
          </label>
          <input type="url" name="terms_service_url" class="form-control" value="<?php echo htmlspecialchars($termsUrl); ?>" placeholder="https://deenone.top/terms">
        </div>
      </div>

      <!-- Rich WYSIWYG Content Area -->
      <div class="form-group">
        <label class="form-label" style="font-weight: 600; font-size: 13px; margin-bottom: 8px;">
          <?php echo __('শর্তাবলী', 'Terms of Service Content'); ?>
        </label>
        
        <div class="wysiwyg-container" style="border: 1px solid var(--border-color); border-radius: var(--radius-sm); overflow: hidden;">
          <div class="wysiwyg-toolbar" style="background: var(--hover-bg); padding: 8px; border-bottom: 1px solid var(--border-color); display: flex; gap: 6px; flex-wrap: wrap;">
            <select class="wysiwyg-select" onchange="applyTermsFormat('formatBlock', this.value); this.selectedIndex=0;" style="padding: 4px 8px; border-radius: 4px; border: 1px solid var(--border-color); font-size: 12px; background: var(--bg-card); color: var(--text-primary);">
              <option value="" selected disabled>Style</option>
              <option value="p">Paragraph</option>
              <option value="h2">Heading 2</option>
              <option value="h3">Heading 3</option>
              <option value="h4">Heading 4</option>
            </select>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('bold')" title="Bold" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;"><b>B</b></button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('italic')" title="Italic" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;"><i>I</i></button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('underline')" title="Underline" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;"><u>U</u></button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsLink()" title="Link" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;">🔗</button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('insertUnorderedList')" title="Bullet List" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;">• List</button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('insertOrderedList')" title="Numbered List" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;">1. List</button>
            <button type="button" class="wysiwyg-btn" onclick="applyTermsFormat('removeFormat')" title="Clear" style="padding: 4px 10px; border-radius: 4px; border: 1px solid var(--border-color); background: var(--bg-card); cursor: pointer;">Tx</button>
          </div>

          <div id="termsWysiwygEditor" contenteditable="true" style="min-height: 280px; padding: 18px; outline: none; line-height: 1.7; font-size: 14px; color: var(--text-primary); background: var(--bg-card);">
            <?php echo $termsContent; ?>
          </div>
          <textarea name="terms_service_content" id="terms_service_content_input" style="display: none;"><?php echo htmlspecialchars($termsContent); ?></textarea>
        </div>
      </div>

      <!-- Action Button -->
      <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
        <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ করুন', 'Save Changes'); ?>
        </button>
      </div>

    </div>
  </form>

</div>

<script>
function applyTermsFormat(cmd, val) {
  document.execCommand(cmd, false, val || null);
}

function applyTermsLink() {
  var url = prompt("Enter web link URL (https://...):");
  if (url) {
    document.execCommand("createLink", false, url);
  }
}

function syncTermsWysiwyg() {
  var editor = document.getElementById('termsWysiwygEditor');
  var input = document.getElementById('terms_service_content_input');
  if (editor && input) {
    input.value = editor.innerHTML;
  }
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
