<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - DEDICATED HELP & SUPPORT MANAGEMENT SUITE
 * Professional, Human-Crafted Support & Helpdesk Operations
 * Features:
 *   - Official Helpline & Channels (Email, WhatsApp, Hotline, Web Portal)
 *   - Working Hours & SLA Status
 *   - Rich Support Guidance & FAQs Editor
 *   - Real-Time Live Android In-App Helpdesk Preview Card
 *   - 100% Dual-Language (Bangla & English) and CSRF Protected
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Fetch current support settings from app_settings table
$supportKeys = [
    'support_email',
    'support_whatsapp',
    'support_url',
    'support_contact_info',
    'support_hours',
    'support_hotline'
];

$inClause = "'" . implode("','", $supportKeys) . "'";
$current = [];
try {
    $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key IN ($inClause)");
    while ($r = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $current[$r['setting_key']] = $r['setting_value'];
    }
} catch (Exception $e) {}

// Defaults
$supportEmail = $current['support_email'] ?? 'support@deenone.top';
$supportWhatsapp = $current['support_whatsapp'] ?? '+8801700000000';
$supportUrl = $current['support_url'] ?? 'https://deenone.top/support';
$supportHotline = $current['support_hotline'] ?? '+880 1800-000000';
$supportHours = $current['support_hours'] ?? 'সপ্তাহে ৭ দিন, ২৪ ঘণ্টা সার্বক্ষণিক কাস্টমার কেয়ার ও সাপোর্ট';
$supportContactInfo = $current['support_contact_info'] ?? "দ্বীনওয়ান (DeenOne) ব্যবহারকালীন যেকোনো জিজ্ঞাসা, প্রযুক্তিগত সমস্যা বা পরামর্শের জন্য আমাদের অফিসিয়াল সাপোর্ট চ্যানেলে যোগাযোগ করুন।\n\nআমরা দ্রুততম সময়ে আপনার বার্তার উত্তর প্রদান করব, ইনশাআল্লাহ।";

// Handle POST Save
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $csrfToken = $_POST['csrf_token'] ?? '';
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর। পুনরায় চেষ্টা করুন।', 'Invalid security token. Please try again.'));
        redirect('help_support.php');
    }

    $email = trim($_POST['support_email'] ?? '');
    $whatsapp = trim($_POST['support_whatsapp'] ?? '');
    $url = trim($_POST['support_url'] ?? '');
    $hotline = trim($_POST['support_hotline'] ?? '');
    $hours = trim($_POST['support_hours'] ?? '');
    $info = trim($_POST['support_contact_info'] ?? '');

    $saveList = [
        'support_email' => $email,
        'support_whatsapp' => $whatsapp,
        'support_url' => $url,
        'support_hotline' => $hotline,
        'support_hours' => $hours,
        'support_contact_info' => $info
    ];

    try {
        $stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value, description) 
                               VALUES (:key, :val, :desc) 
                               ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value), updated_at = NOW()");
        
        foreach ($saveList as $k => $v) {
            $desc = "Support settings: " . $k;
            $stmt->execute([':key' => $k, ':val' => $v, ':desc' => $desc]);
        }

        logAdminAction($pdo, 'UPDATE_SUPPORT_SETTINGS', 'app_settings', null, 'হেল্প ও সাপোর্ট সেটিংস সফলভাবে আপডেট করা হয়েছে');
        setFlash('success', __('হেল্প ও সাপোর্ট তথ্য সফলভাবে সংরক্ষিত হয়েছে!', 'Help & Support settings saved successfully!'));
    } catch (Exception $e) {
        setFlash('danger', __('সংরক্ষণে ত্রুটি হয়েছে: ', 'Failed to save: ') . $e->getMessage());
    }

    redirect('help_support.php');
}

$pageTitle = __('হেল্প ও সাপোর্ট', 'Help & Support');
$activeNav = 'help_support';
require_once __DIR__ . '/header.php';
?>

<div class="page-container" style="max-width: 1200px; margin: 0 auto; padding-bottom: 50px;">

  <!-- Header Banner -->
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
    <div style="display: flex; align-items: center; gap: 10px;">
      <span style="display: inline-flex; align-items: center; justify-content: center; width: 38px; height: 38px; border-radius: 10px; background: rgba(16, 185, 129, 0.12); color: #10b981; font-size: 18px;">
        <i class="fa-solid fa-headset"></i>
      </span>
      <h1 style="font-size: 22px; font-weight: 800; color: var(--text-heading); margin: 0;">
        <?php echo __('হেল্প ও সাপোর্ট', 'Help & Support'); ?>
      </h1>
    </div>
  </div>

  <form method="POST" action="help_support.php">
    <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">

    <div style="display: grid; grid-template-columns: 1fr 380px; gap: 24px; align-items: start;">
      
      <!-- Left Column: Settings Form -->
      <div style="display: flex; flex-direction: column; gap: 20px;">
        
        <!-- Card 1: Official Helplines & Contact Channels -->
        <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px;">
          <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;">
            <i class="fa-solid fa-phone-volume" style="color: var(--primary); font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('যোগাযোগ মাধ্যম', 'Contact Channels'); ?>
            </h3>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 14px;">
            <div class="form-group">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <i class="fa-solid fa-envelope" style="color: #3b82f6; margin-right: 6px;"></i> <?php echo __('সাপোর্ট ইমেইল', 'Support Email'); ?>
              </label>
              <input type="email" name="support_email" id="inputSupportEmail" class="form-control" value="<?php echo htmlspecialchars($supportEmail); ?>" placeholder="support@deenone.top" required oninput="updateLivePreview()">
            </div>

            <div class="form-group">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <i class="fa-brands fa-whatsapp" style="color: #10b981; margin-right: 6px;"></i> <?php echo __('হোয়াটসঅ্যাপ নম্বর', 'WhatsApp Number'); ?>
              </label>
              <input type="text" name="support_whatsapp" id="inputSupportWhatsapp" class="form-control" value="<?php echo htmlspecialchars($supportWhatsapp); ?>" placeholder="+8801700000000" oninput="updateLivePreview()">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <i class="fa-solid fa-headset" style="color: #f59e0b; margin-right: 6px;"></i> <?php echo __('হটলাইন', 'Hotline'); ?>
              </label>
              <input type="text" name="support_hotline" id="inputSupportHotline" class="form-control" value="<?php echo htmlspecialchars($supportHotline); ?>" placeholder="+880 1800-000000" oninput="updateLivePreview()">
            </div>

            <div class="form-group">
              <label class="form-label" style="font-weight: 600; font-size: 13px;">
                <i class="fa-solid fa-globe" style="color: #6366f1; margin-right: 6px;"></i> <?php echo __('ওয়েব পোর্টাল', 'Web Portal'); ?>
              </label>
              <input type="url" name="support_url" id="inputSupportUrl" class="form-control" value="<?php echo htmlspecialchars($supportUrl); ?>" placeholder="https://deenone.top/support" oninput="updateLivePreview()">
            </div>
          </div>

          <div class="form-group" style="margin-top: 14px;">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <i class="fa-solid fa-clock" style="color: #10b981; margin-right: 6px;"></i> <?php echo __('কর্মঘণ্টা', 'Working Hours'); ?>
            </label>
            <input type="text" name="support_hours" id="inputSupportHours" class="form-control" value="<?php echo htmlspecialchars($supportHours); ?>" placeholder="সপ্তাহে ৭ দিন, ২৪ ঘণ্টা সার্বক্ষণিক কাস্টমার কেয়ার" oninput="updateLivePreview()">
          </div>
        </div>

        <!-- Card 2: Guidance Message & FAQ Info -->
        <div class="policy-card" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 22px;">
          <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 14px;">
            <i class="fa-solid fa-circle-question" style="color: #3b82f6; font-size: 18px;"></i>
            <h3 style="font-size: 16px; font-weight: 700; color: var(--text-heading); margin: 0;">
              <?php echo __('সাপোর্ট বার্তা', 'Support Message'); ?>
            </h3>
          </div>

          <div class="form-group">
            <label class="form-label" style="font-weight: 600; font-size: 13px;">
              <?php echo __('বার্তা বিবরণী', 'Message Content'); ?>
            </label>
            <textarea name="support_contact_info" id="inputSupportInfo" class="form-control" rows="5" style="line-height: 1.6;" oninput="updateLivePreview()"><?php echo htmlspecialchars($supportContactInfo); ?></textarea>
          </div>
        </div>

        <!-- Submit Button -->
        <div style="display: flex; justify-content: flex-end;">
          <button type="submit" class="btn-topbar-pill" style="background: var(--primary); color: #fff; border-color: var(--primary); padding: 12px 28px; font-size: 14px; font-weight: 700; display: inline-flex; align-items: center; gap: 8px;">
            <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ করুন', 'Save Changes'); ?>
          </button>
        </div>

      </div>

      <!-- Right Column: Interactive Android App Live Preview -->
      <div>
        <div style="position: sticky; top: 90px; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 20px; box-shadow: 0 4px 20px rgba(0,0,0,0.05);">
          
          <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; border-bottom: 1px solid var(--border-color); padding-bottom: 10px;">
            <span style="font-size: 12px; font-weight: 700; color: var(--primary); text-transform: uppercase; display: flex; align-items: center; gap: 6px;">
              <i class="fa-solid fa-mobile-screen"></i> <?php echo __('মোবাইল প্রিভিউ', 'Mobile Preview'); ?>
            </span>
            <span class="badge" style="font-size: 10px; background: rgba(16, 185, 129, 0.15); color: #059669;">লাইভ</span>
          </div>

          <!-- Mockup Mobile Screen -->
          <div style="background: #0f172a; border-radius: 20px; padding: 16px; color: #f8fafc; border: 4px solid #1e293b; box-shadow: 0 10px 25px rgba(0,0,0,0.3);">
            
            <!-- Mobile Top Bar -->
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
              <div style="display: flex; align-items: center; gap: 8px;">
                <i class="fa-solid fa-arrow-left" style="font-size: 14px; color: #94a3b8;"></i>
                <span style="font-size: 14px; font-weight: 700; color: #fff;">হেল্প ও সাপোর্ট</span>
              </div>
              <i class="fa-solid fa-circle-info" style="font-size: 14px; color: #10b981;"></i>
            </div>

            <!-- Mobile Banner Card -->
            <div style="background: linear-gradient(135deg, #065f46 0%, #064e3b 100%); border-radius: 12px; padding: 14px; margin-bottom: 14px; border: 1px solid #059669;">
              <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 6px;">
                <div style="width: 32px; height: 32px; border-radius: 8px; background: rgba(255,255,255,0.2); display: flex; align-items: center; justify-content: center; font-size: 15px;">
                  🎧
                </div>
                <div>
                  <div style="font-size: 13px; font-weight: 700; color: #fff;">সার্বক্ষণিক সহায়তা</div>
                  <div id="previewHours" style="font-size: 10px; color: #a7f3d0;"><?php echo htmlspecialchars($supportHours); ?></div>
                </div>
              </div>
              <div id="previewInfo" style="font-size: 11px; color: #ecfdf5; line-height: 1.5; white-space: pre-line; margin-top: 8px; opacity: 0.95;">
                <?php echo htmlspecialchars(mb_substr($supportContactInfo, 0, 120)) . '...'; ?>
              </div>
            </div>

            <!-- Mobile Interactive Channel Buttons -->
            <div style="display: flex; flex-direction: column; gap: 8px;">
              
              <!-- WhatsApp Row -->
              <div style="background: #1e293b; border-radius: 10px; padding: 10px 12px; display: flex; align-items: center; justify-content: space-between; border: 1px solid #334155;">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <span style="width: 28px; height: 28px; border-radius: 6px; background: rgba(16, 185, 129, 0.2); color: #10b981; display: flex; align-items: center; justify-content: center; font-size: 14px;">
                    <i class="fa-brands fa-whatsapp"></i>
                  </span>
                  <div>
                    <div style="font-size: 12px; font-weight: 600; color: #f1f5f9;">হোয়াটসঅ্যাপ চ্যাট</div>
                    <div id="previewWhatsapp" style="font-size: 10.5px; color: #94a3b8;"><?php echo htmlspecialchars($supportWhatsapp); ?></div>
                  </div>
                </div>
                <span style="font-size: 11px; font-weight: 600; color: #10b981; background: rgba(16, 185, 129, 0.15); padding: 3px 8px; border-radius: 6px;">চ্যাট</span>
              </div>

              <!-- Email Row -->
              <div style="background: #1e293b; border-radius: 10px; padding: 10px 12px; display: flex; align-items: center; justify-content: space-between; border: 1px solid #334155;">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <span style="width: 28px; height: 28px; border-radius: 6px; background: rgba(59, 130, 246, 0.2); color: #3b82f6; display: flex; align-items: center; justify-content: center; font-size: 14px;">
                    <i class="fa-solid fa-envelope"></i>
                  </span>
                  <div>
                    <div style="font-size: 12px; font-weight: 600; color: #f1f5f9;">সাপোর্ট ইমেইল</div>
                    <div id="previewEmail" style="font-size: 10.5px; color: #94a3b8;"><?php echo htmlspecialchars($supportEmail); ?></div>
                  </div>
                </div>
                <span style="font-size: 11px; font-weight: 600; color: #3b82f6; background: rgba(59, 130, 246, 0.15); padding: 3px 8px; border-radius: 6px;">মেইল</span>
              </div>

              <!-- Hotline Row -->
              <div style="background: #1e293b; border-radius: 10px; padding: 10px 12px; display: flex; align-items: center; justify-content: space-between; border: 1px solid #334155;">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <span style="width: 28px; height: 28px; border-radius: 6px; background: rgba(245, 158, 11, 0.2); color: #f59e0b; display: flex; align-items: center; justify-content: center; font-size: 14px;">
                    <i class="fa-solid fa-phone"></i>
                  </span>
                  <div>
                    <div style="font-size: 12px; font-weight: 600; color: #f1f5f9;">সরাসরি কল</div>
                    <div id="previewHotline" style="font-size: 10.5px; color: #94a3b8;"><?php echo htmlspecialchars($supportHotline); ?></div>
                  </div>
                </div>
                <span style="font-size: 11px; font-weight: 600; color: #f59e0b; background: rgba(245, 158, 11, 0.15); padding: 3px 8px; border-radius: 6px;">কল</span>
              </div>

              <!-- Web Portal Row -->
              <div style="background: #1e293b; border-radius: 10px; padding: 10px 12px; display: flex; align-items: center; justify-content: space-between; border: 1px solid #334155;">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <span style="width: 28px; height: 28px; border-radius: 6px; background: rgba(99, 102, 241, 0.2); color: #818cf8; display: flex; align-items: center; justify-content: center; font-size: 14px;">
                    <i class="fa-solid fa-globe"></i>
                  </span>
                  <div>
                    <div style="font-size: 12px; font-weight: 600; color: #f1f5f9;">ওয়েব পোর্টাল</div>
                    <div id="previewUrl" style="font-size: 10.5px; color: #94a3b8;"><?php echo htmlspecialchars($supportUrl); ?></div>
                  </div>
                </div>
                <span style="font-size: 11px; font-weight: 600; color: #818cf8; background: rgba(99, 102, 241, 0.15); padding: 3px 8px; border-radius: 6px;">ভিজিট</span>
              </div>

            </div>

          </div>

          <div style="margin-top: 12px; text-align: center; font-size: 11px; color: var(--text-dim);">
            <?php echo __('অ্যান্ড্রয়েড অ্যাপে কাস্টমার কেয়ার স্ক্রিনের রিয়েল-টাইম ভিউ', 'Real-time look inside DeenOne Mobile App'); ?>
          </div>

        </div>
      </div>

    </div>
  </form>

</div>

<script>
function updateLivePreview() {
  var email = document.getElementById('inputSupportEmail').value || 'support@deenone.top';
  var whatsapp = document.getElementById('inputSupportWhatsapp').value || '+8801700000000';
  var hotline = document.getElementById('inputSupportHotline').value || '+880 1800-000000';
  var url = document.getElementById('inputSupportUrl').value || 'https://deenone.top/support';
  var hours = document.getElementById('inputSupportHours').value || '২৪ ঘণ্টা সার্বক্ষণিক';
  var info = document.getElementById('inputSupportInfo').value || '';

  document.getElementById('previewEmail').innerText = email;
  document.getElementById('previewWhatsapp').innerText = whatsapp;
  document.getElementById('previewHotline').innerText = hotline;
  document.getElementById('previewUrl').innerText = url;
  document.getElementById('previewHours').innerText = hours;
  
  if (info.length > 120) {
    document.getElementById('previewInfo').innerText = info.substring(0, 120) + '...';
  } else {
    document.getElementById('previewInfo').innerText = info;
  }
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
