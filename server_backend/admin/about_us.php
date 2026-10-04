<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - ABOUT US & APP MISSION (আমাদের সম্পর্কে)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure table exists
$pdo->exec("CREATE TABLE IF NOT EXISTS `app_about_info` (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `app_name` VARCHAR(100) NOT NULL DEFAULT 'DeenOne',
  `tagline_bn` VARCHAR(255) NOT NULL DEFAULT 'আপনার দ্বীনি জীবনের বিশ্বস্ত নিত্যসঙ্গী',
  `tagline_en` VARCHAR(255) NOT NULL DEFAULT 'Your Trusted Companion for Islamic Life',
  `app_version` VARCHAR(50) NOT NULL DEFAULT 'v2.0.0',
  `version_badge_bn` VARCHAR(100) NOT NULL DEFAULT 'সর্বশেষ সংস্করণ',
  `version_badge_en` VARCHAR(100) NOT NULL DEFAULT 'Latest Version',
  `mission_title_bn` VARCHAR(150) NOT NULL DEFAULT 'আমাদের লক্ষ্য',
  `mission_title_en` VARCHAR(150) NOT NULL DEFAULT 'Our Mission',
  `mission_desc_bn` TEXT NOT NULL,
  `mission_desc_en` TEXT NOT NULL,
  `quote_text_bn` TEXT NOT NULL,
  `quote_text_en` TEXT NOT NULL,
  `pills_json` TEXT NOT NULL,
  `copyright_bn` VARCHAR(255) NOT NULL DEFAULT '© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত',
  `copyright_en` VARCHAR(255) NOT NULL DEFAULT '© 2026 DeenOne - All rights reserved',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

// Seed if empty
$chkAbout = $pdo->query("SELECT COUNT(*) FROM app_about_info")->fetchColumn();
if ($chkAbout == 0) {
    $pdo->exec("INSERT INTO `app_about_info` (`id`, `app_name`, `tagline_bn`, `tagline_en`, `app_version`, `version_badge_bn`, `version_badge_en`, `mission_title_bn`, `mission_title_en`, `mission_desc_bn`, `mission_desc_en`, `quote_text_bn`, `quote_text_en`, `pills_json`, `copyright_bn`, `copyright_en`) VALUES
    (1, 'DeenOne', 'আপনার দ্বীনি জীবনের বিশ্বস্ত নিত্যসঙ্গী', 'Your Trusted Companion for Islamic Life', 'v2.0.0', 'সর্বশেষ সংস্করণ', 'Latest Version', 'আমাদের লক্ষ্য', 'Our Mission',
    'দ্বীনওয়ান হলো একটি সর্বাধুনিক ও শতভাগ প্রামাণিক ইসলামিক জীবনধারা ট্র্যাকিং প্ল্যাটফর্ম, যা মুসলিম উম্মাহকে তাদের দৈনন্দিন সালাত, ইবাদত, আমল ও আধ্যাত্মিক অনুশীলনে সাহায্য করার জন্য তৈরি করা হয়েছে।',
    'DeenOne is a state-of-the-art authentic Islamic lifestyle platform, built to empower the Muslim Ummah in their daily prayers, worship, deeds, and spiritual elevation.',
    '“আমাদের উদ্দেশ্য হলো সর্বাধুনিক প্রযুক্তির মাধ্যমে উম্মাহর আধ্যাত্মিক ও দ্বীনি উন্নতিতে সহায়তা করা।”',
    '“Our objective is to assist the spiritual advancement of the Ummah through modern technology.”',
    '[\"আধ্যাত্মিকতা\", \"সুন্নাহ\", \"প্রযুক্তি\", \"উম্মাহ\"]',
    '© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত', '© 2026 DeenOne - All rights reserved')");
}

// Handle Form Submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন মেয়াদোত্তীর্ণ বা অকার্যকর।', 'Invalid or expired security token.'));
        redirect('about_us.php');
    }

    $appName = trim($_POST['app_name'] ?? 'DeenOne');
    $taglineBn = trim($_POST['tagline_bn'] ?? '');
    $taglineEn = trim($_POST['tagline_en'] ?? '');
    $appVersion = trim($_POST['app_version'] ?? 'v2.0.0');
    $versionBadgeBn = trim($_POST['version_badge_bn'] ?? 'সর্বশেষ সংস্করণ');
    $versionBadgeEn = trim($_POST['version_badge_en'] ?? 'Latest Version');
    $missionTitleBn = trim($_POST['mission_title_bn'] ?? 'আমাদের লক্ষ্য');
    $missionTitleEn = trim($_POST['mission_title_en'] ?? 'Our Mission');
    $missionDescBn = trim($_POST['mission_desc_bn'] ?? '');
    $missionDescEn = trim($_POST['mission_desc_en'] ?? '');
    $quoteTextBn = trim($_POST['quote_text_bn'] ?? '');
    $quoteTextEn = trim($_POST['quote_text_en'] ?? '');
    $copyrightBn = trim($_POST['copyright_bn'] ?? '© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত');
    $copyrightEn = trim($_POST['copyright_en'] ?? '© 2026 DeenOne - All rights reserved');

    $pillsRaw = trim($_POST['pills_csv'] ?? '');
    $pills = array_filter(array_map('trim', explode(',', $pillsRaw)));
    $pillsJson = json_encode(array_values($pills), JSON_UNESCAPED_UNICODE);

    try {
        $stmt = $pdo->prepare("UPDATE app_about_info SET
            app_name = ?, tagline_bn = ?, tagline_en = ?, app_version = ?,
            version_badge_bn = ?, version_badge_en = ?, mission_title_bn = ?,
            mission_title_en = ?, mission_desc_bn = ?, mission_desc_en = ?,
            quote_text_bn = ?, quote_text_en = ?, pills_json = ?,
            copyright_bn = ?, copyright_en = ? WHERE id = 1");
        $stmt->execute([
            $appName, $taglineBn, $taglineEn, $appVersion,
            $versionBadgeBn, $versionBadgeEn, $missionTitleBn,
            $missionTitleEn, $missionDescBn, $missionDescEn,
            $quoteTextBn, $quoteTextEn, $pillsJson,
            $copyrightBn, $copyrightEn
        ]);
        logAdminActivity($pdo, $_SESSION['admin_id'] ?? 1, 'UPDATE_ABOUT_INFO', 'app_about_info', '1', 'Updated app about details');
        setFlash('success', __('অ্যাপ সম্পর্কিত তথ্য সফলভাবে সংরক্ষিত হয়েছে!', 'App details updated successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('আপডেট ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
    }
    redirect('about_us.php');
}

// Fetch Current About Info
$aboutStmt = $pdo->query("SELECT * FROM app_about_info LIMIT 1");
$about = $aboutStmt->fetch();
$pills = [];
if (!empty($about['pills_json'])) {
    $pills = json_decode($about['pills_json'], true);
    if (!is_array($pills)) $pills = [];
}
$pillsCsv = implode(', ', $pills);

$pageTitle = __('আমাদের সম্পর্কে', 'About Us');
$activeNav = 'about_us';
require_once __DIR__ . '/header.php';
?>

<form method="POST" action="about_us.php">
  <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

  <div class="policy-card">
    <div class="policy-header">
      <div class="policy-title-row">
        <span class="policy-dot"></span>
        <span class="policy-title"><?php echo __('অ্যাপ পরিচিতি', 'App Info'); ?></span>
      </div>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label class="form-label"><?php echo __('অ্যাপের নাম', 'App Name'); ?> *</label>
        <input type="text" name="app_name" class="form-control" value="<?php echo htmlspecialchars($about['app_name'] ?? 'DeenOne'); ?>" required>
      </div>
      <div class="form-group">
        <label class="form-label"><?php echo __('ভার্সন নম্বর', 'Version Number'); ?> *</label>
        <input type="text" name="app_version" class="form-control" value="<?php echo htmlspecialchars($about['app_version'] ?? 'v2.0.0'); ?>" required>
      </div>
    </div>

    <div class="form-row" style="margin-top: 14px;">
      <div class="form-group">
        <label class="form-label"><?php echo __('বাংলা ট্যাগলাইন', 'Bengali Tagline'); ?> *</label>
        <input type="text" name="tagline_bn" class="form-control" value="<?php echo htmlspecialchars($about['tagline_bn'] ?? ''); ?>" required>
      </div>
      <div class="form-group">
        <label class="form-label"><?php echo __('ইংরেজি ট্যাগলাইন', 'English Tagline'); ?> *</label>
        <input type="text" name="tagline_en" class="form-control" value="<?php echo htmlspecialchars($about['tagline_en'] ?? ''); ?>" required>
      </div>
    </div>

    <div class="form-row" style="margin-top: 14px;">
      <div class="form-group">
        <label class="form-label"><?php echo __('বাংলা ভার্সন ব্যাজ', 'Bengali Version Badge'); ?></label>
        <input type="text" name="version_badge_bn" class="form-control" value="<?php echo htmlspecialchars($about['version_badge_bn'] ?? 'সর্বশেষ সংস্করণ'); ?>">
      </div>
      <div class="form-group">
        <label class="form-label"><?php echo __('ইংরেজি ভার্সন ব্যাজ', 'English Version Badge'); ?></label>
        <input type="text" name="version_badge_en" class="form-control" value="<?php echo htmlspecialchars($about['version_badge_en'] ?? 'Latest Version'); ?>">
      </div>
    </div>

    <div class="form-group" style="margin-top: 14px;">
      <label class="form-label"><?php echo __('বাংলা মিশন বিবরণ', 'Bengali Mission Description'); ?> *</label>
      <textarea name="mission_desc_bn" rows="3" class="form-control" required><?php echo htmlspecialchars($about['mission_desc_bn'] ?? ''); ?></textarea>
    </div>

    <div class="form-group" style="margin-top: 14px;">
      <label class="form-label"><?php echo __('ইংরেজি মিশন বিবরণ', 'English Mission Description'); ?> *</label>
      <textarea name="mission_desc_en" rows="3" class="form-control" required><?php echo htmlspecialchars($about['mission_desc_en'] ?? ''); ?></textarea>
    </div>

    <div class="form-row" style="margin-top: 14px;">
      <div class="form-group">
        <label class="form-label"><?php echo __('বাংলা মটো কোট', 'Bengali Motto Quote'); ?> *</label>
        <input type="text" name="quote_text_bn" class="form-control" value="<?php echo htmlspecialchars($about['quote_text_bn'] ?? ''); ?>" required>
      </div>
      <div class="form-group">
        <label class="form-label"><?php echo __('ইংরেজি মটো কোট', 'English Motto Quote'); ?> *</label>
        <input type="text" name="quote_text_en" class="form-control" value="<?php echo htmlspecialchars($about['quote_text_en'] ?? ''); ?>" required>
      </div>
    </div>

    <div class="form-group" style="margin-top: 14px;">
      <label class="form-label"><?php echo __('ট্যাগসমূহ', 'Tags'); ?></label>
      <input type="text" name="pills_csv" class="form-control" value="<?php echo htmlspecialchars($pillsCsv); ?>" placeholder="<?php echo __('উদাঃ আধ্যাত্মিকতা, সুন্নাহ, প্রযুক্তি, উম্মাহ', 'e.g. Spirituality, Sunnah, Tech, Ummah'); ?>">
    </div>

    <div class="form-row" style="margin-top: 14px;">
      <div class="form-group">
        <label class="form-label"><?php echo __('বাংলা কপিরাইট', 'Bengali Copyright'); ?></label>
        <input type="text" name="copyright_bn" class="form-control" value="<?php echo htmlspecialchars($about['copyright_bn'] ?? ''); ?>">
      </div>
      <div class="form-group">
        <label class="form-label"><?php echo __('ইংরেজি কপিরাইট', 'English Copyright'); ?></label>
        <input type="text" name="copyright_en" class="form-control" value="<?php echo htmlspecialchars($about['copyright_en'] ?? ''); ?>">
      </div>
    </div>
  </div>

  <!-- Bottom Save Action -->
  <div style="display: flex; justify-content: flex-end; margin-bottom: 30px;">
    <button type="submit" class="btn-save-settings">
      ✓ <?php echo __('পরিবর্তন সংরক্ষণ করুন', 'Save Changes'); ?>
    </button>
  </div>
</form>

<?php require_once __DIR__ . '/footer.php'; ?>
