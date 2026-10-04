<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - DONATION PAGE CUSTOMIZATION & BBCODE BUILDER
 * Configure contents, impact metrics, collapsible payment methods, FAQs & BBCode
 * ==============================================================================
 */

require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$pdo = getDbConnection();

// Active Tab from request or session
$activeTab = (int)($_POST['current_tab'] ?? ($_GET['tab'] ?? 1));
if ($activeTab < 1 || $activeTab > 6) {
    $activeTab = 1;
}

// Server-side instant translation helper for dual-language DB sync
if (!function_exists('serverSideFaqTranslate')) {
    function serverSideFaqTranslate($text, $sl = 'bn', $tl = 'en') {
        $clean = trim($text ?? '');
        if ($clean === '') return '';
        try {
            $url = 'https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=' . urlencode($sl) . '&tl=' . urlencode($tl) . '&q=' . urlencode($clean);
            $ctx = stream_context_create([
                'http' => [
                    'timeout' => 3,
                    'header' => "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)\r\n"
                ]
            ]);
            $res = @file_get_contents($url, false, $ctx);
            if ($res) {
                $json = json_decode($res, true);
                if (is_array($json) && !empty($json[0])) {
                    return is_array($json[0]) ? implode(' ', $json[0]) : (string)$json[0];
                }
            }
        } catch (Throwable $e) {}
        return $clean;
    }
}

// Handle Save Settings
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', 'নিরাপত্তা টোকেন অকার্যকর।');
        redirect('donation_settings.php?tab=' . $activeTab);
    }

    if ($action === 'save_settings') {
        try {
            $pdo->beginTransaction();

            $upd = $pdo->prepare("INSERT INTO donation_settings (setting_key, setting_value, setting_group) 
                VALUES (?, ?, ?) 
                ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value), setting_group = VALUES(setting_group)");

            // 1. Hero & Stats
            $heroGroup = [
                'hero_title_en' => trim($_POST['hero_title_en'] ?? ''),
                'hero_title_bn' => trim($_POST['hero_title_bn'] ?? ''),
                'hero_subtitle_en' => trim($_POST['hero_subtitle_en'] ?? ''),
                'hero_subtitle_bn' => trim($_POST['hero_subtitle_bn'] ?? ''),
                'stat_users_display_mode' => trim($_POST['stat_users_display_mode'] ?? 'auto'),
                'stat_users_custom' => trim($_POST['stat_users_custom'] ?? '3.2M+'),
                'stat_years_usage' => trim($_POST['stat_years_usage'] ?? '907+'),
                'stat_countries' => trim($_POST['stat_countries'] ?? '177+'),
                'usd_to_bdt_rate' => trim($_POST['usd_to_bdt_rate'] ?? '120'),
                'preset_amounts_usd' => json_encode(array_values(array_filter(explode(',', str_replace(['[',']','"',' '], '', $_POST['preset_amounts_usd'] ?? '2,3,5,10'))))),
                'preset_amounts_bdt' => json_encode(array_values(array_filter(explode(',', str_replace(['[',']','"',' '], '', $_POST['preset_amounts_bdt'] ?? '100,200,500,1000')))))
            ];
            foreach ($heroGroup as $k => $v) {
                $upd->execute([$k, $v, 'hero']);
            }

            // 2. Payment Accounts & Active/Inactive Toggles
            $paymentGroup = [
                'bkash_enabled' => isset($_POST['bkash_enabled']) && $_POST['bkash_enabled'] == '1' ? '1' : '0',
                'bkash_number' => trim($_POST['bkash_number'] ?? ''),
                'bkash_type' => trim($_POST['bkash_type'] ?? 'Personal / Send Money'),
                'bkash_instructions_bn' => trim($_POST['bkash_instructions_bn'] ?? ''),
                'bkash_instructions_en' => trim($_POST['bkash_instructions_en'] ?? ''),

                'nagad_enabled' => isset($_POST['nagad_enabled']) && $_POST['nagad_enabled'] == '1' ? '1' : '0',
                'nagad_number' => trim($_POST['nagad_number'] ?? ''),
                'nagad_type' => trim($_POST['nagad_type'] ?? 'Personal / Send Money'),
                'nagad_instructions_bn' => trim($_POST['nagad_instructions_bn'] ?? ''),
                'nagad_instructions_en' => trim($_POST['nagad_instructions_en'] ?? ''),

                'rocket_enabled' => isset($_POST['rocket_enabled']) && $_POST['rocket_enabled'] == '1' ? '1' : '0',
                'rocket_number' => trim($_POST['rocket_number'] ?? ''),
                'rocket_type' => trim($_POST['rocket_type'] ?? 'Personal'),
                'rocket_instructions_bn' => trim($_POST['rocket_instructions_bn'] ?? ''),
                'rocket_instructions_en' => trim($_POST['rocket_instructions_en'] ?? ''),

                'bank_enabled' => isset($_POST['bank_enabled']) && $_POST['bank_enabled'] == '1' ? '1' : '0',
                'bank_name' => trim($_POST['bank_name'] ?? ''),
                'bank_account_name' => trim($_POST['bank_account_name'] ?? ''),
                'bank_account_no' => trim($_POST['bank_account_no'] ?? ''),
                'bank_branch' => trim($_POST['bank_branch'] ?? ''),
                'bank_routing' => trim($_POST['bank_routing'] ?? ''),
                'bank_swift' => trim($_POST['bank_swift'] ?? ''),
                'bank_instructions_bn' => trim($_POST['bank_instructions_bn'] ?? ''),
                'bank_instructions_en' => trim($_POST['bank_instructions_en'] ?? ''),

                'binance_enabled' => isset($_POST['binance_enabled']) && $_POST['binance_enabled'] == '1' ? '1' : '0',
                'crypto_binance_pay_id' => trim($_POST['crypto_binance_pay_id'] ?? ''),
                'crypto_usdt_trc20' => trim($_POST['crypto_usdt_trc20'] ?? ''),
                'crypto_btc_address' => trim($_POST['crypto_btc_address'] ?? ''),
                'binance_instructions_bn' => trim($_POST['binance_instructions_bn'] ?? ''),
                'binance_instructions_en' => trim($_POST['binance_instructions_en'] ?? ''),

                'paypal_enabled' => isset($_POST['paypal_enabled']) && $_POST['paypal_enabled'] == '1' ? '1' : '0',
                'paypal_email' => trim($_POST['paypal_email'] ?? ''),
                'paypal_me_link' => trim($_POST['paypal_me_link'] ?? ''),
                'paypal_instructions_bn' => trim($_POST['paypal_instructions_bn'] ?? ''),
                'paypal_instructions_en' => trim($_POST['paypal_instructions_en'] ?? ''),

                'custom_enabled' => isset($_POST['custom_enabled']) && $_POST['custom_enabled'] == '1' ? '1' : '0',
                'custom_title_bn' => trim($_POST['custom_title_bn'] ?? ''),
                'custom_title_en' => trim($_POST['custom_title_en'] ?? ''),
                'custom_details_bn' => trim($_POST['custom_details_bn'] ?? ''),
                'custom_details_en' => trim($_POST['custom_details_en'] ?? ''),

                'google_recaptcha_enabled' => isset($_POST['google_recaptcha_enabled']) && $_POST['google_recaptcha_enabled'] == '1' ? '1' : '0',
                'google_recaptcha_site_key' => trim($_POST['google_recaptcha_site_key'] ?? ''),
                'google_recaptcha_secret_key' => trim($_POST['google_recaptcha_secret_key'] ?? '')
            ];
            foreach ($paymentGroup as $k => $v) {
                $upd->execute([$k, $v, 'payment']);
            }

            // 3. Comparison Cards
            $compGroup = [
                'sec_problem_badge_en' => trim($_POST['sec_problem_badge_en'] ?? 'PROBLEM'),
                'sec_problem_badge_bn' => trim($_POST['sec_problem_badge_bn'] ?? 'সমস্যা'),
                'sec_problem_title_en' => trim($_POST['sec_problem_title_en'] ?? ''),
                'sec_problem_title_bn' => trim($_POST['sec_problem_title_bn'] ?? ''),
                'sec_problem_desc_en' => trim($_POST['sec_problem_desc_en'] ?? ''),
                'sec_problem_desc_bn' => trim($_POST['sec_problem_desc_bn'] ?? ''),
                'sec_solution_badge_en' => trim($_POST['sec_solution_badge_en'] ?? 'SOLUTION'),
                'sec_solution_badge_bn' => trim($_POST['sec_solution_badge_bn'] ?? 'সমাধান'),
                'sec_solution_title_en' => trim($_POST['sec_solution_title_en'] ?? 'DeenOne'),
                'sec_solution_title_bn' => trim($_POST['sec_solution_title_bn'] ?? 'দীন ওয়ান'),
                'sec_solution_desc_en' => trim($_POST['sec_solution_desc_en'] ?? ''),
                'sec_solution_desc_bn' => trim($_POST['sec_solution_desc_bn'] ?? ''),
                'sec_result_badge_en' => trim($_POST['sec_result_badge_en'] ?? 'RESULT'),
                'sec_result_badge_bn' => trim($_POST['sec_result_badge_bn'] ?? 'ফলাফল'),
                'sec_result_title_en' => trim($_POST['sec_result_title_en'] ?? ''),
                'sec_result_title_bn' => trim($_POST['sec_result_title_bn'] ?? ''),
                'sec_result_desc_en' => trim($_POST['sec_result_desc_en'] ?? ''),
                'sec_result_desc_bn' => trim($_POST['sec_result_desc_bn'] ?? '')
            ];
            foreach ($compGroup as $k => $v) {
                $upd->execute([$k, $v, 'comparison']);
            }

            // 4. Testimonial & Quran Verse
            $inspireGroup = [
                'testimonial_quote_en' => trim($_POST['testimonial_quote_en'] ?? ''),
                'testimonial_quote_bn' => trim($_POST['testimonial_quote_bn'] ?? ''),
                'testimonial_author_en' => trim($_POST['testimonial_author_en'] ?? ''),
                'testimonial_author_bn' => trim($_POST['testimonial_author_bn'] ?? ''),
                'quran_verse_ar' => trim($_POST['quran_verse_ar'] ?? ''),
                'quran_verse_en' => trim($_POST['quran_verse_en'] ?? ''),
                'quran_verse_bn' => trim($_POST['quran_verse_bn'] ?? ''),
                'quran_verse_ref_en' => trim($_POST['quran_verse_ref_en'] ?? ''),
                'quran_verse_ref_bn' => trim($_POST['quran_verse_ref_bn'] ?? '')
            ];
            foreach ($inspireGroup as $k => $v) {
                $upd->execute([$k, $v, 'inspiration']);
            }

            // 5. FAQs Array with Multi-Language Auto-Sync
            $faqQuestionsEn = $_POST['faq_q_en'] ?? [];
            $faqQuestionsBn = $_POST['faq_q_bn'] ?? [];
            $faqAnswersEn = $_POST['faq_a_en'] ?? [];
            $faqAnswersBn = $_POST['faq_a_bn'] ?? [];

            $maxFaqs = max(count($faqQuestionsEn), count($faqQuestionsBn), count($faqAnswersEn), count($faqAnswersBn));
            $faqsData = [];
            for ($i = 0; $i < $maxFaqs; $i++) {
                $qBn = trim($faqQuestionsBn[$i] ?? '');
                $qEn = trim($faqQuestionsEn[$i] ?? '');
                $aBn = trim($faqAnswersBn[$i] ?? '');
                $aEn = trim($faqAnswersEn[$i] ?? '');

                if ($qBn !== '' || $qEn !== '') {
                    // Auto-fill missing translations so both languages are saved in DB
                    if ($qBn !== '' && $qEn === '') {
                        $qEn = serverSideFaqTranslate($qBn, 'bn', 'en');
                    } elseif ($qEn !== '' && $qBn === '') {
                        $qBn = serverSideFaqTranslate($qEn, 'en', 'bn');
                    }

                    if ($aBn !== '' && $aEn === '') {
                        $aEn = serverSideFaqTranslate($aBn, 'bn', 'en');
                    } elseif ($aEn !== '' && $aBn === '') {
                        $aBn = serverSideFaqTranslate($aEn, 'en', 'bn');
                    }

                    $faqsData[] = [
                        'question_en' => $qEn,
                        'question_bn' => $qBn,
                        'answer_en' => $aEn,
                        'answer_bn' => $aBn
                    ];
                }
            }
            $upd->execute(['faqs_json', json_encode($faqsData, JSON_UNESCAPED_UNICODE), 'faq']);

            $pdo->commit();
            logAdminAction($pdo, 'UPDATE_DONATION_SETTINGS', 'donation_settings', 'all', 'ডোনেশন পেজ সেটিংস ও কন্টেন্ট আপডেট সম্পন্ন');
            setFlash('success', 'ডোনেশন পেজের সকল কন্টেন্ট ও সেটিংস সফলভাবে আপডেট ও সংরক্ষিত হয়েছে!');
        } catch (Exception $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            setFlash('danger', 'সেটিংস আপডেট ব্যর্থ: ' . $e->getMessage());
        }

        redirect('donation_settings.php?tab=' . $activeTab);
    }
}

// Fetch all current settings
$settingsStmt = $pdo->query("SELECT setting_key, setting_value FROM donation_settings");
$s = [];
while ($row = $settingsStmt->fetch()) {
    $s[$row['setting_key']] = $row['setting_value'];
}

$faqs = json_decode($s['faqs_json'] ?? '[]', true) ?: [];

$pageTitle = __('পেজ সেটিংস', 'Page Settings');
$activeNav = 'donations';
require_once __DIR__ . '/header.php';
?>

<style>
.tab-btn {
  padding: 10px 18px;
  font-size: 13.5px;
  font-weight: 700;
  border-radius: var(--radius-md);
  cursor: pointer;
  border: 1px solid transparent;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}
.tab-btn.active {
  background: var(--primary);
  color: #ffffff;
}
.tab-btn:not(.active) {
  background: var(--hover-bg);
  color: var(--text-muted);
  border-color: var(--border-color);
}
.bbcode-toolbar {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
  background: var(--hover-bg);
  padding: 8px 12px;
  border-radius: var(--radius-sm) var(--radius-sm) 0 0;
  border: 1px solid var(--border-color);
  border-bottom: none;
}
.bbcode-btn {
  padding: 4px 10px;
  background: #ffffff;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  color: var(--text-heading);
  cursor: pointer;
  transition: all 0.15s ease;
}
.bbcode-btn:hover {
  background: #f1f5f9;
  border-color: var(--primary);
  color: var(--primary);
}
.faq-builder-row {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 18px;
  margin-bottom: 16px;
  position: relative;
}
.custom-switch {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 26px;
  margin: 0;
  vertical-align: middle;
}
.custom-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}
.custom-switch-slider {
  position: absolute;
  cursor: pointer;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: #cbd5e1;
  transition: .25s ease;
  border-radius: 26px;
}
.custom-switch-slider:before {
  position: absolute;
  content: "";
  height: 20px;
  width: 20px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .25s ease;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0,0,0,0.25);
}
.custom-switch input:checked + .custom-switch-slider {
  background-color: #10b981;
}
.custom-switch input:checked + .custom-switch-slider:before {
  transform: translateX(22px);
}
.payment-method-card {
  border: 1.5px solid var(--border-color);
  border-radius: var(--radius-lg);
  margin-bottom: 18px;
  background: var(--bg-card);
  overflow: hidden;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.payment-method-card.active-method {
  border-color: rgba(16, 185, 129, 0.4);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}
.payment-method-header {
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  user-select: none;
}
.payment-method-body {
  padding: 20px;
  border-top: 1px solid var(--border-color);
  background: var(--bg-card);
}
</style>

<div class="content-body">

  <!-- Header Banner -->
  <div class="hero-banner" style="margin-bottom: 24px;">
    <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 14px;">
      <div>
        <h2 style="font-size: 24px; font-weight: 800; color: var(--text-heading);">
          <?php echo __('পেজ সেটিংস', 'Page Settings'); ?>
        </h2>
      </div>

      <div style="display: flex; gap: 10px;">
        <a href="donations.php" class="btn btn-secondary">
          <i class="fa-solid fa-arrow-left"></i>
          <span><?php echo __('ফিরে যান', 'Back'); ?></span>
        </a>
        <a href="../donate/index.php" target="_blank" class="btn btn-primary">
          <i class="fa-solid fa-arrow-up-right-from-square"></i>
          <span><?php echo __('লাইভ প্রিভিউ', 'Live Preview'); ?></span>
        </a>
      </div>
    </div>
  </div>

  <!-- Tabs Navigation -->
  <div style="display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 24px;">
    <button type="button" class="tab-btn <?php echo $activeTab === 1 ? 'active' : ''; ?>" id="tabNav1" onclick="switchSettingsTab(1)">
      <i class="fa-solid fa-bullhorn"></i> <?php echo __('হেডার ও পরিসংখ্যান', 'Hero & Impact'); ?>
    </button>
    <button type="button" class="tab-btn <?php echo $activeTab === 2 ? 'active' : ''; ?>" id="tabNav2" onclick="switchSettingsTab(2)">
      <i class="fa-solid fa-credit-card"></i> <?php echo __('পেমেন্ট অ্যাকাউন্ট', 'Payment Accounts'); ?>
    </button>
    <button type="button" class="tab-btn <?php echo $activeTab === 3 ? 'active' : ''; ?>" id="tabNav3" onclick="switchSettingsTab(3)">
      <i class="fa-solid fa-columns"></i> <?php echo __('ফিচার কার্ড', 'Feature Cards'); ?>
    </button>
    <button type="button" class="tab-btn <?php echo $activeTab === 4 ? 'active' : ''; ?>" id="tabNav4" onclick="switchSettingsTab(4)">
      <i class="fa-solid fa-quote-left"></i> <?php echo __('আয়াত ও বাণী', 'Quran & Quote'); ?>
    </button>
    <button type="button" class="tab-btn <?php echo $activeTab === 5 ? 'active' : ''; ?>" id="tabNav5" onclick="switchSettingsTab(5)">
      <i class="fa-solid fa-circle-question"></i> <?php echo __('সাধারণ জিজ্ঞাসা', 'FAQ'); ?>
    </button>
    <button type="button" class="tab-btn <?php echo $activeTab === 6 ? 'active' : ''; ?>" id="tabNav6" onclick="switchSettingsTab(6)">
      <i class="fa-solid fa-code"></i> <?php echo __('বিবি কোড', 'BBCode'); ?>
    </button>
  </div>

  <form method="POST" action="donation_settings.php" id="formDonationSettings">
    <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
    <input type="hidden" name="action" value="save_settings">
    <input type="hidden" name="current_tab" id="currentTabInput" value="<?php echo $activeTab; ?>">

    <!-- =======================================================================
         TAB 1: HERO & IMPACT STATS
         ======================================================================= -->
    <div id="tabSection1" class="tab-section" style="<?php echo $activeTab === 1 ? 'display: block;' : 'display: none;'; ?>">
      <div class="card mb-5">
        <div class="card-header">
          <div class="card-title"><i class="fa-solid fa-bullhorn text-emerald-500 mr-2"></i><?php echo __('হেডার ব্যানার', 'Hero Banner'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা শিরোনাম', 'Title (Bengali)'); ?></label>
              <textarea name="hero_title_bn" id="heroTitleBn" class="form-control font-bold" rows="2"><?php echo htmlspecialchars($s['hero_title_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি শিরোনাম', 'Title (English)'); ?></label>
              <textarea name="hero_title_en" id="heroTitleEn" class="form-control font-bold" rows="2"><?php echo htmlspecialchars($s['hero_title_en'] ?? ''); ?></textarea>
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা সাব-টাইটেল', 'Subtitle (Bengali)'); ?></label>
              <textarea name="hero_subtitle_bn" id="heroSubtitleBn" class="form-control" rows="2"><?php echo htmlspecialchars($s['hero_subtitle_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি সাব-টাইটেল', 'Subtitle (English)'); ?></label>
              <textarea name="hero_subtitle_en" id="heroSubtitleEn" class="form-control" rows="2"><?php echo htmlspecialchars($s['hero_subtitle_en'] ?? ''); ?></textarea>
            </div>
          </div>
        </div>
      </div>

      <div class="card mb-5">
        <div class="card-header">
          <div class="card-title"><i class="fa-solid fa-chart-simple text-blue-500 mr-2"></i><?php echo __('পরিসংখ্যান কাউন্টার', 'Impact Statistics'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ব্যবহারকারী মোড', 'Users Stat Mode'); ?></label>
              <select name="stat_users_display_mode" class="form-control">
                <option value="auto" <?php echo ($s['stat_users_display_mode'] ?? 'auto') === 'auto' ? 'selected' : ''; ?>><?php echo __('ডাটাবেজ থেকে স্বয়ংক্রিয়', 'Auto from Users Table'); ?></option>
                <option value="custom" <?php echo ($s['stat_users_display_mode'] ?? 'auto') === 'custom' ? 'selected' : ''; ?>><?php echo __('কাস্টম মান দেখান', 'Custom Value'); ?></option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('কাস্টম সংখ্যা', 'Custom Count'); ?></label>
              <input type="text" name="stat_users_custom" class="form-control font-bold" value="<?php echo htmlspecialchars($s['stat_users_custom'] ?? '3.2M+'); ?>" placeholder="3.2M+">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('সেবার সময়কাল', 'Years of Usage'); ?></label>
              <input type="text" name="stat_years_usage" class="form-control font-bold" value="<?php echo htmlspecialchars($s['stat_years_usage'] ?? '907+'); ?>" placeholder="907+">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('মোট দেশ', 'Total Countries'); ?></label>
              <input type="text" name="stat_countries" class="form-control font-bold" value="<?php echo htmlspecialchars($s['stat_countries'] ?? '177+'); ?>" placeholder="177+">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ডলার রেট', 'Conversion Rate'); ?></label>
              <input type="number" step="0.1" name="usd_to_bdt_rate" class="form-control font-bold" value="<?php echo htmlspecialchars($s['usd_to_bdt_rate'] ?? '120'); ?>" placeholder="120">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ডলার প্রিসেট', 'Preset USD'); ?></label>
              <input type="text" name="preset_amounts_usd" class="form-control" value="<?php echo htmlspecialchars(implode(',', json_decode($s['preset_amounts_usd'] ?? '["2","3","5","10"]', true) ?: ["2","3","5","10"])); ?>" placeholder="2,3,5,10">
            </div>
          </div>

          <div class="form-group">
            <label class="form-label"><?php echo __('টাকা প্রিসেট', 'Preset BDT'); ?></label>
            <input type="text" name="preset_amounts_bdt" class="form-control" value="<?php echo htmlspecialchars(implode(',', json_decode($s['preset_amounts_bdt'] ?? '["100","200","500","1000"]', true) ?: ["100","200","500","1000"])); ?>" placeholder="100,200,500,1000">
          </div>
        </div>
      </div>
    </div>

    <!-- =======================================================================
         TAB 2: PAYMENT ACCOUNTS & COLLAPSIBLE ACTIVE TOGGLES
         ======================================================================= -->
    <div id="tabSection2" class="tab-section" style="<?php echo $activeTab === 2 ? 'display: block;' : 'display: none;'; ?>">
      
      <!-- 1. bKash Card -->
      <?php $isBkashOn = ($s['bkash_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isBkashOn ? 'active-method' : ''; ?>" id="cardBkash">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(225,29,72,0.06), rgba(244,63,94,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #e11d48; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-mobile-screen-button"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('বিকাশ অ্যাকাউন্ট', 'bKash Account'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="bkashStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isBkashOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isBkashOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="bkash_enabled" id="bkashSwitch" value="1" <?php echo $isBkashOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Bkash', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="bkashBody" style="<?php echo $isBkashOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বিকাশ নম্বর *', 'bKash Number *'); ?></label>
              <input type="text" name="bkash_number" class="form-control font-bold" value="<?php echo htmlspecialchars($s['bkash_number'] ?? '01700000000'); ?>" placeholder="017XXXXXXXX">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('বিকাশ একাউন্ট ধরন', 'bKash Account Type'); ?></label>
              <select name="bkash_type" class="form-control font-bold">
                <option value="Personal / Send Money" <?php echo ($s['bkash_type'] ?? '') === 'Personal / Send Money' ? 'selected' : ''; ?>><?php echo isEn() ? 'Personal / Send Money' : 'পার্সোনাল / সেন্ড মানি'; ?></option>
                <option value="Merchant / Payment" <?php echo ($s['bkash_type'] ?? '') === 'Merchant / Payment' ? 'selected' : ''; ?>><?php echo isEn() ? 'Merchant / Payment' : 'মার্চেন্ট / পেমেন্ট'; ?></option>
                <option value="Agent / Cash In" <?php echo ($s['bkash_type'] ?? '') === 'Agent / Cash In' ? 'selected' : ''; ?>><?php echo isEn() ? 'Agent / Cash In' : 'এজেন্ট / ক্যাশ ইন'; ?></option>
              </select>
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (BN)'); ?></label>
              <input type="text" name="bkash_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['bkash_instructions_bn'] ?? 'বিকাশ অ্যাপ থেকে Send Money করে নিচের বক্সে TrxID লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (EN)'); ?></label>
              <input type="text" name="bkash_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['bkash_instructions_en'] ?? 'Send Money via bKash App and submit the TrxID below'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 2. Nagad Card -->
      <?php $isNagadOn = ($s['nagad_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isNagadOn ? 'active-method' : ''; ?>" id="cardNagad">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(234,88,12,0.06), rgba(249,115,22,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #ea580c; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-wallet"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('নগদ অ্যাকাউন্ট', 'Nagad Account'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="nagadStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isNagadOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isNagadOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="nagad_enabled" id="nagadSwitch" value="1" <?php echo $isNagadOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Nagad', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="nagadBody" style="<?php echo $isNagadOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('নগদ নম্বর *', 'Nagad Number *'); ?></label>
              <input type="text" name="nagad_number" class="form-control font-bold" value="<?php echo htmlspecialchars($s['nagad_number'] ?? '01700000000'); ?>" placeholder="017XXXXXXXX">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('নগদ একাউন্ট ধরন', 'Nagad Account Type'); ?></label>
              <select name="nagad_type" class="form-control font-bold">
                <option value="Personal / Send Money" <?php echo ($s['nagad_type'] ?? '') === 'Personal / Send Money' ? 'selected' : ''; ?>><?php echo isEn() ? 'Personal / Send Money' : 'পার্সোনাল / সেন্ড মানি'; ?></option>
                <option value="Merchant / Payment" <?php echo ($s['nagad_type'] ?? '') === 'Merchant / Payment' ? 'selected' : ''; ?>><?php echo isEn() ? 'Merchant / Payment' : 'মার্চেন্ট / পেমেন্ট'; ?></option>
                <option value="Agent / Cash In" <?php echo ($s['nagad_type'] ?? '') === 'Agent / Cash In' ? 'selected' : ''; ?>><?php echo isEn() ? 'Agent / Cash In' : 'এজেন্ট / ক্যাশ ইন'; ?></option>
              </select>
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (BN)'); ?></label>
              <input type="text" name="nagad_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['nagad_instructions_bn'] ?? 'নগদ অ্যাপ থেকে Send Money করে নিচের বক্সে TrxID লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (EN)'); ?></label>
              <input type="text" name="nagad_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['nagad_instructions_en'] ?? 'Send Money via Nagad App and submit the TrxID below'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 3. Rocket Card -->
      <?php $isRocketOn = ($s['rocket_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isRocketOn ? 'active-method' : ''; ?>" id="cardRocket">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(147,51,234,0.06), rgba(168,85,247,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #9333ea; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-paper-plane"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('রকেট অ্যাকাউন্ট', 'Rocket Account'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="rocketStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isRocketOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isRocketOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="rocket_enabled" id="rocketSwitch" value="1" <?php echo $isRocketOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Rocket', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="rocketBody" style="<?php echo $isRocketOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('রকেট নম্বর *', 'Rocket Number *'); ?></label>
              <input type="text" name="rocket_number" class="form-control font-bold" value="<?php echo htmlspecialchars($s['rocket_number'] ?? '017000000000'); ?>" placeholder="017XXXXXXXXX">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('রকেট ধরন', 'Rocket Account Type'); ?></label>
              <select name="rocket_type" class="form-control font-bold">
                <option value="Personal" <?php echo ($s['rocket_type'] ?? '') === 'Personal' ? 'selected' : ''; ?>><?php echo isEn() ? 'Personal' : 'পার্সোনাল'; ?></option>
                <option value="Merchant" <?php echo ($s['rocket_type'] ?? '') === 'Merchant' ? 'selected' : ''; ?>><?php echo isEn() ? 'Merchant' : 'মার্চেন্ট'; ?></option>
                <option value="Agent" <?php echo ($s['rocket_type'] ?? '') === 'Agent' ? 'selected' : ''; ?>><?php echo isEn() ? 'Agent' : 'এজেন্ট'; ?></option>
              </select>
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (BN)'); ?></label>
              <input type="text" name="rocket_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['rocket_instructions_bn'] ?? 'রকেট একাউন্টে টাকা পাঠিয়ে TrxID লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (EN)'); ?></label>
              <input type="text" name="rocket_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['rocket_instructions_en'] ?? 'Send money via Rocket and submit TrxID'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 4. Bank Details -->
      <?php $isBankOn = ($s['bank_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isBankOn ? 'active-method' : ''; ?>" id="cardBank">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(2,132,199,0.06), rgba(30,58,138,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #0284c7; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-building-columns"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('ব্যাংক অ্যাকাউন্ট', 'Bank Transfer'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="bankStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isBankOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isBankOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="bank_enabled" id="bankSwitch" value="1" <?php echo $isBankOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Bank', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="bankBody" style="<?php echo $isBankOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ব্যাংকের নাম *', 'Bank Name *'); ?></label>
              <input type="text" name="bank_name" class="form-control font-bold" value="<?php echo htmlspecialchars($s['bank_name'] ?? 'Islami Bank Bangladesh PLC'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('হিসাবধারীর নাম *', 'Account Holder Name *'); ?></label>
              <input type="text" name="bank_account_name" class="form-control font-bold" value="<?php echo htmlspecialchars($s['bank_account_name'] ?? 'DeenOne Foundation'); ?>">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1.2fr 1fr 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('হিসাব নম্বর *', 'Account Number *'); ?></label>
              <input type="text" name="bank_account_no" class="form-control font-bold" value="<?php echo htmlspecialchars($s['bank_account_no'] ?? '2050123456789000'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('শাখা', 'Branch'); ?></label>
              <input type="text" name="bank_branch" class="form-control" value="<?php echo htmlspecialchars($s['bank_branch'] ?? 'Dhanmondi Branch, Dhaka'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('রাউটিং নম্বর', 'Routing Number'); ?></label>
              <input type="text" name="bank_routing" class="form-control" value="<?php echo htmlspecialchars($s['bank_routing'] ?? '125271892'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('সুইফট কোড', 'SWIFT Code'); ?></label>
              <input type="text" name="bank_swift" class="form-control" value="<?php echo htmlspecialchars($s['bank_swift'] ?? 'IBBLBDDH'); ?>" placeholder="IBBLBDDH">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (BN)'); ?></label>
              <input type="text" name="bank_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['bank_instructions_bn'] ?? 'ব্যাংকে টাকা ডিপোজিট বা ট্রান্সফার করে ট্রানজ্যাকশন স্লিপ নম্বর লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (EN)'); ?></label>
              <input type="text" name="bank_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['bank_instructions_en'] ?? 'Transfer to bank and submit the deposit/slip reference number'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 5. Binance Pay Details -->
      <?php $isBinanceOn = ($s['binance_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isBinanceOn ? 'active-method' : ''; ?>" id="cardBinance">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(245,158,11,0.06), rgba(217,119,6,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #f59e0b; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-coins"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('বাইনান্স পে', 'Binance Pay'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="binanceStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isBinanceOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isBinanceOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="binance_enabled" id="binanceSwitch" value="1" <?php echo $isBinanceOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Binance', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="binanceBody" style="<?php echo $isBinanceOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-group">
            <label class="form-label"><?php echo __('বাইনান্স পে আইডি *', 'Binance Pay ID *'); ?></label>
            <input type="text" name="crypto_binance_pay_id" class="form-control font-bold" value="<?php echo htmlspecialchars($s['crypto_binance_pay_id'] ?? '88992211'); ?>" placeholder="Binance Pay ID / UID">
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (Bengali)'); ?></label>
              <input type="text" name="binance_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['binance_instructions_bn'] ?? 'বাইনান্স অ্যাপ থেকে নিচের বাইন্যান্স পে আইডিতে সেন্ড করুন এবং প্রাপ্ত পে অর্ডার আইডি বা রেফারেন্স নিচে লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (English)'); ?></label>
              <input type="text" name="binance_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['binance_instructions_en'] ?? 'Transfer via Binance Pay ID and submit the Pay Order ID / Reference below'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 6. PayPal Card -->
      <?php $isPaypalOn = ($s['paypal_enabled'] ?? '1') == '1'; ?>
      <div class="payment-method-card <?php echo $isPaypalOn ? 'active-method' : ''; ?>" id="cardPaypal">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(37,99,235,0.06), rgba(59,130,246,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #2563eb; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-brands fa-paypal"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('পেপাল অ্যাকাউন্ট', 'PayPal Account'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="paypalStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isPaypalOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isPaypalOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="paypal_enabled" id="paypalSwitch" value="1" <?php echo $isPaypalOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Paypal', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="paypalBody" style="<?php echo $isPaypalOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('পেপাল ইমেইল *', 'PayPal Email *'); ?></label>
              <input type="email" name="paypal_email" class="form-control font-bold" value="<?php echo htmlspecialchars($s['paypal_email'] ?? 'donate@deenone.top'); ?>" placeholder="donate@deenone.top">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('PayPal.Me লিংক', 'PayPal.Me Link'); ?></label>
              <input type="text" name="paypal_me_link" class="form-control font-bold" value="<?php echo htmlspecialchars($s['paypal_me_link'] ?? 'https://paypal.me/deenone'); ?>" placeholder="https://paypal.me/username">
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা নির্দেশনা', 'Instructions (BN)'); ?></label>
              <input type="text" name="paypal_instructions_bn" class="form-control" value="<?php echo htmlspecialchars($s['paypal_instructions_bn'] ?? 'পেপালে অনুদান পাঠিয়ে Transaction ID নিচের বক্সে লিখুন'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি নির্দেশনা', 'Instructions (EN)'); ?></label>
              <input type="text" name="paypal_instructions_en" class="form-control" value="<?php echo htmlspecialchars($s['paypal_instructions_en'] ?? 'Send donation via PayPal and enter the Transaction ID'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- 7. Custom / Other Card -->
      <?php $isCustomOn = ($s['custom_enabled'] ?? '0') == '1'; ?>
      <div class="payment-method-card <?php echo $isCustomOn ? 'active-method' : ''; ?>" id="cardCustom">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(16,185,129,0.06), rgba(20,184,166,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #10b981; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-circle-nodes"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('অন্যান্য মাধ্যম', 'Other Gateway'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="customStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isCustomOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isCustomOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="custom_enabled" id="customSwitch" value="1" <?php echo $isCustomOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Custom', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="customBody" style="<?php echo $isCustomOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা মাধ্যমের নাম', 'Method Name (BN)'); ?></label>
              <input type="text" name="custom_title_bn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['custom_title_bn'] ?? 'আন্তর্জাতিক কার্ড / স্ট্রাইপ'); ?>" placeholder="যেমন: ক্রেডিট কার্ড বা স্ট্রাইপ">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি মাধ্যমের নাম', 'Method Name (EN)'); ?></label>
              <input type="text" name="custom_title_en" class="form-control font-bold" value="<?php echo htmlspecialchars($s['custom_title_en'] ?? 'International Card / Stripe'); ?>" placeholder="e.g. Credit Card or Stripe">
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা বিবরণ', 'Details (BN)'); ?></label>
              <textarea name="custom_details_bn" class="form-control" rows="2" placeholder="পেমেন্ট লিংক বা নির্দেশনা লিখুন"><?php echo htmlspecialchars($s['custom_details_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি বিবরণ', 'Details (EN)'); ?></label>
              <textarea name="custom_details_en" class="form-control" rows="2" placeholder="Payment link or instructions"><?php echo htmlspecialchars($s['custom_details_en'] ?? ''); ?></textarea>
            </div>
          </div>
        </div>
      </div>

      <!-- 8. Google reCAPTCHA & Security Card -->
      <?php $isRecaptchaOn = ($s['google_recaptcha_enabled'] ?? '0') == '1'; ?>
      <div class="payment-method-card <?php echo $isRecaptchaOn ? 'active-method' : ''; ?>" id="cardRecaptcha">
        <div class="payment-method-header" style="background: linear-gradient(135deg, rgba(59,130,246,0.06), rgba(99,102,241,0.09));">
          <div style="display: flex; align-items: center; gap: 12px;">
            <div style="width: 38px; height: 38px; border-radius: 10px; background: #3b82f6; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18px;">
              <i class="fa-solid fa-shield-halved"></i>
            </div>
            <div>
              <div style="font-weight: 800; font-size: 15px; color: var(--text-heading);"><?php echo __('গুগল রিক্যাপচা', 'Google reCAPTCHA'); ?></div>
            </div>
          </div>
          <div style="display: flex; align-items: center; gap: 14px;">
            <span id="recaptchaStatusLabel" style="font-size: 12.5px; font-weight: 800; color: <?php echo $isRecaptchaOn ? '#059669' : '#94a3b8'; ?>;">
              <?php echo $isRecaptchaOn ? __('সক্রিয়', 'Active') : __('নিষ্ক্রিয়', 'Inactive'); ?>
            </span>
            <label class="custom-switch" onclick="event.stopPropagation();">
              <input type="checkbox" name="google_recaptcha_enabled" id="recaptchaSwitch" value="1" <?php echo $isRecaptchaOn ? 'checked' : ''; ?> onchange="togglePaymentCardBody('Recaptcha', this.checked)">
              <span class="custom-switch-slider"></span>
            </label>
          </div>
        </div>
        <div class="payment-method-body" id="recaptchaBody" style="<?php echo $isRecaptchaOn ? 'display: block;' : 'display: none;'; ?>">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('গুগল রিক্যাপচা সাইট কি', 'reCAPTCHA Site Key'); ?></label>
              <input type="text" name="google_recaptcha_site_key" class="form-control font-mono" value="<?php echo htmlspecialchars($s['google_recaptcha_site_key'] ?? ''); ?>" placeholder="6LeIx0cD...">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('গুগল রিক্যাপচা সিক্রেট কি', 'reCAPTCHA Secret Key'); ?></label>
              <input type="password" name="google_recaptcha_secret_key" class="form-control font-mono" value="<?php echo htmlspecialchars($s['google_recaptcha_secret_key'] ?? ''); ?>" placeholder="6LeIx0cD...">
            </div>
          </div>
        </div>
      </div>

    </div>

    <!-- =======================================================================
         TAB 3: COMPARISON CARDS
         ======================================================================= -->
    <div id="tabSection3" class="tab-section" style="<?php echo $activeTab === 3 ? 'display: block;' : 'display: none;'; ?>">
      
      <!-- Card 1: Problem -->
      <div class="card mb-5">
        <div class="card-header" style="background: rgba(239, 68, 68, 0.06);">
          <div class="card-title"><i class="fa-regular fa-face-frown text-rose-500 mr-2"></i><?php echo __('সমস্যা কার্ড', 'Problem Card'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা শিরোনাম', 'Title (BN)'); ?></label>
              <input type="text" name="sec_problem_title_bn" id="probTitleBn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_problem_title_bn'] ?? ''); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি শিরোনাম', 'Title (EN)'); ?></label>
              <input type="text" name="sec_problem_title_en" id="probTitleEn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_problem_title_en'] ?? ''); ?>">
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা বিবরণ', 'Description (BN)'); ?></label>
              <textarea name="sec_problem_desc_bn" id="probDescBn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_problem_desc_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি বিবরণ', 'Description (EN)'); ?></label>
              <textarea name="sec_problem_desc_en" id="probDescEn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_problem_desc_en'] ?? ''); ?></textarea>
            </div>
          </div>
        </div>
      </div>

      <!-- Card 2: Solution -->
      <div class="card mb-5">
        <div class="card-header" style="background: rgba(16, 185, 129, 0.06);">
          <div class="card-title"><i class="fa-solid fa-shield-halved text-emerald-500 mr-2"></i><?php echo __('সমাধান কার্ড', 'Solution Card'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা শিরোনাম', 'Title (BN)'); ?></label>
              <input type="text" name="sec_solution_title_bn" id="solTitleBn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_solution_title_bn'] ?? 'দীন ওয়ান'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি শিরোনাম', 'Title (EN)'); ?></label>
              <input type="text" name="sec_solution_title_en" id="solTitleEn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_solution_title_en'] ?? 'DeenOne'); ?>">
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা বিবরণ', 'Description (BN)'); ?></label>
              <textarea name="sec_solution_desc_bn" id="solDescBn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_solution_desc_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি বিবরণ', 'Description (EN)'); ?></label>
              <textarea name="sec_solution_desc_en" id="solDescEn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_solution_desc_en'] ?? ''); ?></textarea>
            </div>
          </div>
        </div>
      </div>

      <!-- Card 3: Result -->
      <div class="card mb-5">
        <div class="card-header" style="background: rgba(2, 132, 199, 0.06);">
          <div class="card-title"><i class="fa-regular fa-face-smile text-sky-500 mr-2"></i><?php echo __('ফলাফল কার্ড', 'Result Card'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা শিরোনাম', 'Title (BN)'); ?></label>
              <input type="text" name="sec_result_title_bn" id="resTitleBn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_result_title_bn'] ?? ''); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি শিরোনাম', 'Title (EN)'); ?></label>
              <input type="text" name="sec_result_title_en" id="resTitleEn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['sec_result_title_en'] ?? ''); ?>">
            </div>
          </div>
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা বিবরণ', 'Description (BN)'); ?></label>
              <textarea name="sec_result_desc_bn" id="resDescBn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_result_desc_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি বিবরণ', 'Description (EN)'); ?></label>
              <textarea name="sec_result_desc_en" id="resDescEn" class="form-control" rows="3"><?php echo htmlspecialchars($s['sec_result_desc_en'] ?? ''); ?></textarea>
            </div>
          </div>
        </div>
      </div>

    </div>

    <!-- =======================================================================
         TAB 4: QURAN & TESTIMONIAL
         ======================================================================= -->
    <div id="tabSection4" class="tab-section" style="<?php echo $activeTab === 4 ? 'display: block;' : 'display: none;'; ?>">
      
      <!-- Quran Ayah -->
      <div class="card mb-5">
        <div class="card-header">
          <div class="card-title"><i class="fa-solid fa-book-quran text-emerald-500 mr-2"></i><?php echo __('কুরআনের আয়াত', 'Quran Verse'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-group">
            <label class="form-label"><?php echo __('মূল আরবি আয়াত', 'Arabic Verse'); ?></label>
            <textarea name="quran_verse_ar" class="form-control" rows="3" style="font-family: 'Amiri', serif; font-size: 18px; direction: rtl;"><?php echo htmlspecialchars($s['quran_verse_ar'] ?? ''); ?></textarea>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা অনুবাদ', 'Bengali Translation'); ?></label>
              <textarea name="quran_verse_bn" id="quranTransBn" class="form-control" rows="3"><?php echo htmlspecialchars($s['quran_verse_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি অনুবাদ', 'English Translation'); ?></label>
              <textarea name="quran_verse_en" id="quranTransEn" class="form-control" rows="3"><?php echo htmlspecialchars($s['quran_verse_en'] ?? ''); ?></textarea>
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা রেফারেন্স', 'Reference (BN)'); ?></label>
              <input type="text" name="quran_verse_ref_bn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['quran_verse_ref_bn'] ?? 'সূরা আল-বাক্বারাহ, ২:২৬১'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি রেফারেন্স', 'Reference (EN)'); ?></label>
              <input type="text" name="quran_verse_ref_en" class="form-control font-bold" value="<?php echo htmlspecialchars($s['quran_verse_ref_en'] ?? 'Surah Al-Baqarah, 2:261'); ?>">
            </div>
          </div>
        </div>
      </div>

      <!-- Testimonial -->
      <div class="card mb-5">
        <div class="card-header">
          <div class="card-title"><i class="fa-solid fa-quote-left text-blue-500 mr-2"></i><?php echo __('প্রশংসাপত্র', 'Testimonial'); ?></div>
        </div>
        <div class="card-body p-5">
          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা বাণী', 'Quote Text (BN)'); ?></label>
              <textarea name="testimonial_quote_bn" id="testQuoteBn" class="form-control" rows="3"><?php echo htmlspecialchars($s['testimonial_quote_bn'] ?? ''); ?></textarea>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি বাণী', 'Quote Text (EN)'); ?></label>
              <textarea name="testimonial_quote_en" id="testQuoteEn" class="form-control" rows="3"><?php echo htmlspecialchars($s['testimonial_quote_en'] ?? ''); ?></textarea>
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('বাংলা পরিচয়', 'Author (BN)'); ?></label>
              <input type="text" name="testimonial_author_bn" class="form-control font-bold" value="<?php echo htmlspecialchars($s['testimonial_author_bn'] ?? 'মোঃ মাহবুব আলম, দীন ওয়ান ব্যবহারকারী'); ?>">
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইংরেজি পরিচয়', 'Author (EN)'); ?></label>
              <input type="text" name="testimonial_author_en" class="form-control font-bold" value="<?php echo htmlspecialchars($s['testimonial_author_en'] ?? 'Md. Mahbub Alam, DeenOne App User'); ?>">
            </div>
          </div>
        </div>
      </div>

    </div>

    <!-- =======================================================================
         TAB 5: FAQ BUILDER
         ======================================================================= -->
    <div id="tabSection5" class="tab-section" style="<?php echo $activeTab === 5 ? 'display: block;' : 'display: none;'; ?>">
      <div class="card mb-5">
        <div class="card-header" style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;">
          <div style="display: flex; align-items: center; gap: 14px; flex-wrap: wrap;">
            <div class="card-title" style="margin-bottom: 0;">
              <i class="fa-solid fa-circle-question text-emerald-500 mr-2"></i><?php echo __('সাধারণ জিজ্ঞাসা', 'FAQ Builder'); ?>
            </div>
            <!-- Interactive Language View Mode -->
            <div class="faq-lang-switch-pills" style="display: inline-flex; background: var(--hover-bg, #f1f5f9); padding: 3px; border-radius: 999px; border: 1px solid var(--border-color);">
              <button type="button" class="btn-faq-lang-pill" id="faqLangBtnBn" onclick="switchFaqLangMode('bn')" style="border: none; padding: 5px 14px; border-radius: 999px; font-size: 12px; font-weight: 700; cursor: pointer; transition: all 0.2s; <?php echo isBn() ? 'background: #059669; color: #fff;' : 'background: transparent; color: var(--text-body);'; ?>">
                <i class="fa-solid fa-language mr-1"></i> <?php echo __('বাংলা', 'Bengali'); ?>
              </button>
              <button type="button" class="btn-faq-lang-pill" id="faqLangBtnEn" onclick="switchFaqLangMode('en')" style="border: none; padding: 5px 14px; border-radius: 999px; font-size: 12px; font-weight: 700; cursor: pointer; transition: all 0.2s; <?php echo isEn() ? 'background: #059669; color: #fff;' : 'background: transparent; color: var(--text-body);'; ?>">
                <i class="fa-solid fa-globe mr-1"></i> English
              </button>
            </div>
            <span id="faqLiveTransBadge" style="font-size: 11.5px; color: var(--text-muted); display: inline-flex; align-items: center; gap: 4px;">
              <i class="fa-solid fa-wand-magic-sparkles" style="color: #10b981;"></i>
              <span id="faqLiveTransBadgeText"><?php echo isBn() ? 'বাংলায় লিখলে স্বয়ংক্রিয়ভাবে ইংরেজিতে ব্যাকএন্ডে অনুবাদ ও ডেটাবেজে সংরক্ষিত হবে' : 'Typing in English auto-translates to Bengali in the background'; ?></span>
            </span>
          </div>
          <button type="button" class="btn btn-primary btn-sm" onclick="addNewFaqRow()">
            <i class="fa-solid fa-plus"></i> <?php echo __('নতুন প্রশ্ন', 'Add FAQ'); ?>
          </button>
        </div>
        <div class="card-body p-5" id="faqBuilderContainer">
          <?php foreach ($faqs as $i => $faq): ?>
            <div class="faq-builder-row" id="faqRow_<?php echo $i; ?>" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: 12px; padding: 16px; margin-bottom: 14px;">
              <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; padding-bottom: 8px; border-bottom: 1px solid var(--border-color);">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <span style="font-weight: 800; font-size: 13px; color: var(--primary);">FAQ #<?php echo $i + 1; ?></span>
                  <span class="faq-row-mode-badge" style="font-size: 11px; padding: 2px 8px; border-radius: 6px; background: rgba(16,185,129,0.1); color: #059669; font-weight: 700;">
                    <?php echo isBn() ? 'বাংলা মোড' : 'English Mode'; ?>
                  </span>
                </div>
                <button type="button" class="btn btn-danger btn-sm" onclick="removeFaqRow('faqRow_<?php echo $i; ?>')"><i class="fa-solid fa-trash"></i></button>
              </div>

              <!-- Bengali Fields (Visible in Bengali Mode) -->
              <div class="faq-lang-group faq-group-bn" style="<?php echo isBn() ? 'display: block;' : 'display: none;'; ?>">
                <div class="form-group" style="margin-bottom: 12px;">
                  <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
                    <span><?php echo __('বাংলা প্রশ্ন', 'Bengali Question'); ?> <strong style="color: #ef4444;">*</strong></span>
                    <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> ব্যাকএন্ডে ইংলিশ হয়ে সেভ হবে</span>
                  </label>
                  <input type="text" name="faq_q_bn[]" id="faq_q_bn_<?php echo $i; ?>" class="form-control font-bold" value="<?php echo htmlspecialchars($faq['question_bn'] ?? ''); ?>" placeholder="<?php echo __('প্রশ্ন লিখুন...', 'Enter question...'); ?>">
                </div>
                <div class="form-group" style="margin-bottom: 0;">
                  <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
                    <span><?php echo __('বাংলা উত্তর', 'Bengali Answer'); ?> <strong style="color: #ef4444;">*</strong></span>
                    <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> ব্যাকএন্ডে ইংলিশ হয়ে সেভ হবে</span>
                  </label>
                  <textarea name="faq_a_bn[]" id="faq_a_bn_<?php echo $i; ?>" class="form-control" rows="3" placeholder="<?php echo __('বিস্তারিত উত্তর লিখুন...', 'Enter answer...'); ?>"><?php echo htmlspecialchars($faq['answer_bn'] ?? ''); ?></textarea>
                </div>
              </div>

              <!-- English Fields (Visible in English Mode) -->
              <div class="faq-lang-group faq-group-en" style="<?php echo isEn() ? 'display: block;' : 'display: none;'; ?>">
                <div class="form-group" style="margin-bottom: 12px;">
                  <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
                    <span><?php echo __('English Question', 'ইংরেজি প্রশ্ন'); ?> <strong style="color: #ef4444;">*</strong></span>
                    <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> Auto-translates to Bengali</span>
                  </label>
                  <input type="text" name="faq_q_en[]" id="faq_q_en_<?php echo $i; ?>" class="form-control font-bold" value="<?php echo htmlspecialchars($faq['question_en'] ?? ''); ?>" placeholder="Enter question in English...">
                </div>
                <div class="form-group" style="margin-bottom: 0;">
                  <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
                    <span><?php echo __('English Answer', 'ইংরেজি উত্তর'); ?> <strong style="color: #ef4444;">*</strong></span>
                    <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> Auto-translates to Bengali</span>
                  </label>
                  <textarea name="faq_a_en[]" id="faq_a_en_<?php echo $i; ?>" class="form-control" rows="3" placeholder="Enter answer in English..."><?php echo htmlspecialchars($faq['answer_en'] ?? ''); ?></textarea>
                </div>
              </div>
            </div>
          <?php endforeach; ?>
        </div>
      </div>
    </div>

    <!-- =======================================================================
         TAB 6: BBCODE GUIDE & LIVE TESTER
         ======================================================================= -->
    <div id="tabSection6" class="tab-section" style="<?php echo $activeTab === 6 ? 'display: block;' : 'display: none;'; ?>">
      <div class="card mb-5">
        <div class="card-header">
          <div class="card-title"><i class="fa-solid fa-code text-emerald-500 mr-2"></i><?php echo __('বিবি কোড গাইড', 'BBCode Guide'); ?></div>
        </div>
        <div class="card-body p-5">
          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 14px; margin-bottom: 24px;">
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[b]বোল্ড টেক্সট[/b]</strong> → <strong>বোল্ড টেক্সট</strong>
            </div>
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[i]ইটালিক টেক্সট[/i]</strong> → <em>ইটালিক টেক্সট</em>
            </div>
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[color=#10b981]সবুজ রং[/color]</strong> → <span style="color: #10b981; font-weight:700;">সবুজ রং</span>
            </div>
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[size=18px]বড় শিরোনাম[/size]</strong> → <span style="font-size: 18px; font-weight:700;">বড় শিরোনাম</span>
            </div>
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[url=https://example.com]লিংক[/url]</strong> → <a href="#" style="color:#10b981; text-decoration:underline;">লিংক</a>
            </div>
            <div style="background: var(--hover-bg); padding: 12px; border-radius: var(--radius-sm); font-size: 13px; border: 1px solid var(--border-color);">
              <strong>[badge=success]অনুমোদিত[/badge]</strong> → <span class="badge badge-success">অনুমোদিত</span>
            </div>
          </div>

          <!-- Live BBCode Sandbox -->
          <div style="border: 1px solid var(--border-color); border-radius: var(--radius-md); overflow: hidden;">
            <div class="bbcode-toolbar">
              <span style="font-size: 12px; font-weight: 700; color: var(--text-muted); margin-right: 6px;">BBCode Toolbar:</span>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'b')">Bold</button>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'i')">Italic</button>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'color', '#059669')">Green</button>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'color', '#d97706')">Gold</button>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'size', '18px')">Large Size</button>
              <button type="button" class="bbcode-btn" onclick="insertBBCode('sandboxInput', 'url', 'https://deenone.top')">Link</button>
            </div>
            <textarea id="sandboxInput" class="form-control" rows="4" style="border-radius: 0; border: none; border-top: 1px solid var(--border-color);" placeholder="এখানে বিবি কোড লিখে নিচে লাইভ ফলাফল দেখুন..." oninput="updateBBCodePreview(this.value)">[b]সুবহানাল্লাহ![/b] দীন ওয়ান অ্যাপের মাধ্যমে [color=#059669]প্রতিদিন নেক আমল[/color] বৃদ্ধি করুন। [size=16px][url=https://deenone.top]আমাদের ওয়েবসাইট ভিজিট করুন[/url][/size]</textarea>
            <div style="background: var(--bg-card); padding: 16px; border-top: 1px dashed var(--border-color);">
              <div style="font-size: 11px; font-weight: 800; color: var(--text-muted); text-transform: uppercase; margin-bottom: 6px;">লাইভ প্রিভিউ ফলাফল:</div>
              <div id="sandboxPreview" style="font-size: 14.5px; line-height: 1.6; color: var(--text-heading);"></div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <!-- Floating Save Bar -->
    <div style="position: sticky; bottom: 20px; z-index: 30; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 16px 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); display: flex; align-items: center; justify-content: space-between; gap: 16px;">
      <div style="font-size: 13px; color: var(--text-muted);">
        <i class="fa-solid fa-cloud-arrow-up text-emerald-500 mr-1"></i> <?php echo __('সকল পরিবর্তন সরাসরি ডাটাবেজে সংরক্ষিত হবে।', 'All changes will be saved to live database.'); ?>
      </div>
      <div style="display: flex; gap: 12px;">
        <button type="submit" class="btn btn-primary px-8" style="padding: 12px 28px; font-size: 15px; font-weight: 800;">
          <i class="fa-solid fa-floppy-disk"></i>
          <span><?php echo __('সংরক্ষণ করুন', 'Save Settings'); ?></span>
        </button>
      </div>
    </div>

  </form>

</div>

<script>
function switchSettingsTab(idx) {
  document.getElementById('currentTabInput').value = idx;
  for (let i = 1; i <= 6; i++) {
    const sec = document.getElementById('tabSection' + i);
    const nav = document.getElementById('tabNav' + i);
    if (sec && nav) {
      if (i === idx) {
        sec.style.display = 'block';
        nav.classList.add('active');
      } else {
        sec.style.display = 'none';
        nav.classList.remove('active');
      }
    }
  }
}

function togglePaymentCardBody(methodName, isChecked) {
  const card = document.getElementById('card' + methodName);
  const body = document.getElementById(methodName.toLowerCase() + 'Body');
  const label = document.getElementById(methodName.toLowerCase() + 'StatusLabel');

  if (card) {
    card.classList.toggle('active-method', isChecked);
  }
  if (body) {
    body.style.display = isChecked ? 'block' : 'none';
  }
  if (label) {
    label.innerText = isChecked ? '<?php echo __("সক্রিয়", "Active"); ?>' : '<?php echo __("নিষ্ক্রিয়", "Inactive"); ?>';
    label.style.color = isChecked ? '#059669' : '#94a3b8';
  }
}

let currentFaqLangMode = '<?php echo isEn() ? "en" : "bn"; ?>';
let faqIndexCounter = <?php echo count($faqs) + 10; ?>;

function switchFaqLangMode(mode) {
  currentFaqLangMode = mode;
  const isBangla = (mode === 'bn');

  const btnBn = document.getElementById('faqLangBtnBn');
  const btnEn = document.getElementById('faqLangBtnEn');
  if (btnBn && btnEn) {
    if (isBangla) {
      btnBn.style.background = '#059669';
      btnBn.style.color = '#ffffff';
      btnEn.style.background = 'transparent';
      btnEn.style.color = 'var(--text-body)';
    } else {
      btnEn.style.background = '#059669';
      btnEn.style.color = '#ffffff';
      btnBn.style.background = 'transparent';
      btnBn.style.color = 'var(--text-body)';
    }
  }

  const badgeText = document.getElementById('faqLiveTransBadgeText');
  if (badgeText) {
    badgeText.innerText = isBangla
      ? 'বাংলায় লিখলে স্বয়ংক্রিয়ভাবে ইংরেজিতে ব্যাকএন্ডে অনুবাদ ও ডেটাবেজে সংরক্ষিত হবে'
      : 'Typing in English auto-translates to Bengali in the background';
  }

  document.querySelectorAll('.faq-group-bn').forEach(function(el) {
    el.style.display = isBangla ? 'block' : 'none';
  });
  document.querySelectorAll('.faq-group-en').forEach(function(el) {
    el.style.display = isBangla ? 'none' : 'block';
  });
  document.querySelectorAll('.faq-row-mode-badge').forEach(function(el) {
    el.innerText = isBangla ? 'বাংলা মোড' : 'English Mode';
  });
}

function bindFaqAutoTranslate(idx) {
  const qBn = document.getElementById('faq_q_bn_' + idx);
  const qEn = document.getElementById('faq_q_en_' + idx);
  const aBn = document.getElementById('faq_a_bn_' + idx);
  const aEn = document.getElementById('faq_a_en_' + idx);

  if (typeof setupLiveTranslate === 'function') {
    if (qBn && qEn) {
      setupLiveTranslate(qBn, qEn, { sl: 'bn', tl: 'en' });
      setupLiveTranslate(qEn, qBn, { sl: 'en', tl: 'bn' });
    }
    if (aBn && aEn) {
      setupLiveTranslate(aBn, aEn, { sl: 'bn', tl: 'en' });
      setupLiveTranslate(aEn, aBn, { sl: 'en', tl: 'bn' });
    }
  }
}

function addNewFaqRow() {
  faqIndexCounter++;
  const idx = faqIndexCounter;
  const isBangla = (currentFaqLangMode === 'bn');
  const container = document.getElementById('faqBuilderContainer');
  const div = document.createElement('div');
  div.className = 'faq-builder-row';
  div.id = 'faqRow_' + idx;
  div.style.cssText = 'background: var(--bg-card); border: 1px solid var(--border-color); border-radius: 12px; padding: 16px; margin-bottom: 14px;';

  div.innerHTML = `
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; padding-bottom: 8px; border-bottom: 1px solid var(--border-color);">
      <div style="display: flex; align-items: center; gap: 8px;">
        <span style="font-weight: 800; font-size: 13px; color: var(--primary);"><?php echo __('নতুন প্রশ্ন', 'New FAQ'); ?></span>
        <span class="faq-row-mode-badge" style="font-size: 11px; padding: 2px 8px; border-radius: 6px; background: rgba(16,185,129,0.1); color: #059669; font-weight: 700;">
          ${isBangla ? 'বাংলা মোড' : 'English Mode'}
        </span>
      </div>
      <button type="button" class="btn btn-danger btn-sm" onclick="removeFaqRow('faqRow_${idx}')"><i class="fa-solid fa-trash"></i></button>
    </div>

    <!-- Bengali Fields -->
    <div class="faq-lang-group faq-group-bn" style="${isBangla ? 'display: block;' : 'display: none;'}">
      <div class="form-group" style="margin-bottom: 12px;">
        <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
          <span><?php echo __('বাংলা প্রশ্ন', 'Bengali Question'); ?> <strong style="color: #ef4444;">*</strong></span>
          <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> ব্যাকএন্ডে ইংলিশ হয়ে সেভ হবে</span>
        </label>
        <input type="text" name="faq_q_bn[]" id="faq_q_bn_${idx}" class="form-control font-bold" placeholder="<?php echo __('প্রশ্ন লিখুন...', 'Enter question...'); ?>">
      </div>
      <div class="form-group" style="margin-bottom: 0;">
        <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
          <span><?php echo __('বাংলা উত্তর', 'Bengali Answer'); ?> <strong style="color: #ef4444;">*</strong></span>
          <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> ব্যাকএন্ডে ইংলিশ হয়ে সেভ হবে</span>
        </label>
        <textarea name="faq_a_bn[]" id="faq_a_bn_${idx}" class="form-control" rows="3" placeholder="<?php echo __('বিস্তারিত উত্তর লিখুন...', 'Enter answer...'); ?>"></textarea>
      </div>
    </div>

    <!-- English Fields -->
    <div class="faq-lang-group faq-group-en" style="${isBangla ? 'display: none;' : 'display: block;'}">
      <div class="form-group" style="margin-bottom: 12px;">
        <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
          <span><?php echo __('English Question', 'ইংরেজি প্রশ্ন'); ?> <strong style="color: #ef4444;">*</strong></span>
          <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> Auto-translates to Bengali</span>
        </label>
        <input type="text" name="faq_q_en[]" id="faq_q_en_${idx}" class="form-control font-bold" placeholder="Enter question in English...">
      </div>
      <div class="form-group" style="margin-bottom: 0;">
        <label class="form-label" style="display: flex; justify-content: space-between; align-items: center;">
          <span><?php echo __('English Answer', 'ইংরেজি উত্তর'); ?> <strong style="color: #ef4444;">*</strong></span>
          <span style="font-size: 11px; color: #059669; font-weight: 600;"><i class="fa-solid fa-cloud-arrow-up"></i> Auto-translates to Bengali</span>
        </label>
        <textarea name="faq_a_en[]" id="faq_a_en_${idx}" class="form-control" rows="3" placeholder="Enter answer in English..."></textarea>
      </div>
    </div>
  `;

  container.appendChild(div);
  bindFaqAutoTranslate(idx);
}

function removeFaqRow(rowId) {
  const row = document.getElementById(rowId);
  if (row) row.remove();
}

function insertBBCode(targetId, tag, param) {
  const textarea = document.getElementById(targetId);
  const start = textarea.selectionStart;
  const end = textarea.selectionEnd;
  const text = textarea.value;
  const selected = text.substring(start, end) || 'Text';

  let openTag = `[${tag}]`;
  let closeTag = `[/${tag}]`;

  if (param) {
    openTag = `[${tag}=${param}]`;
  }

  const replacement = openTag + selected + closeTag;
  textarea.value = text.substring(0, start) + replacement + text.substring(end);
  updateBBCodePreview(textarea.value);
}

function updateBBCodePreview(str) {
  let html = str
    .replace(/\[b\](.*?)\[\/b\]/gi, '<strong>$1</strong>')
    .replace(/\[i\](.*?)\[\/i\]/gi, '<em>$1</em>')
    .replace(/\[color=(.*?)\](.*?)\[\/color\]/gi, '<span style="color:$1;">$2</span>')
    .replace(/\[size=(.*?)\](.*?)\[\/size\]/gi, '<span style="font-size:$1;">$2</span>')
    .replace(/\[url=(.*?)\](.*?)\[\/url\]/gi, '<a href="$1" target="_blank" style="color:#10b981; text-decoration:underline;">$2</a>')
    .replace(/\[badge=(.*?)\](.*?)\[\/badge\]/gi, '<span class="badge badge-$1">$2</span>')
    .replace(/\n/g, '<br>');

  document.getElementById('sandboxPreview').innerHTML = html;
}

document.addEventListener('DOMContentLoaded', function() {
  const sb = document.getElementById('sandboxInput');
  if (sb) updateBBCodePreview(sb.value);

  // Auto-translate bindings for main settings
  if (typeof setupLiveTranslate === 'function') {
    setupLiveTranslate('heroTitleBn', 'heroTitleEn');
    setupLiveTranslate('heroSubtitleBn', 'heroSubtitleEn');
    setupLiveTranslate('probTitleBn', 'probTitleEn');
    setupLiveTranslate('probDescBn', 'probDescEn');
    setupLiveTranslate('solTitleBn', 'solTitleEn');
    setupLiveTranslate('solDescBn', 'solDescEn');
    setupLiveTranslate('resTitleBn', 'resTitleEn');
    setupLiveTranslate('resDescBn', 'resDescEn');
    setupLiveTranslate('quranTransBn', 'quranTransEn');
    setupLiveTranslate('testQuoteBn', 'testQuoteEn');
  }

  // Bind live auto-translation for all initial FAQ rows
  const existingFaqCount = <?php echo count($faqs); ?>;
  for (let i = 0; i < existingFaqCount; i++) {
    bindFaqAutoTranslate(i);
  }
  switchFaqLangMode(currentFaqLangMode);
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
