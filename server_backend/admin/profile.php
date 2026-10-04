<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - USER PROFILE & SECURITY MANAGEMENT
 * 100% Verbatim UI matching screenshots:
 * - Personal Information Tab (Avatar upload, Name, Email, Phone, DOB, Gender)
 * - Change Password Tab (Current, New, Confirm Passwords with eye toggles)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/lang.php';
requireAdminLogin();

$pageTitle = __('অ্যাডমিন প্রোফাইল', 'Admin Profile');
$activeNav = 'settings';

$pdo = getDbConnection();
$adminUser = getAdminUser();
$adminId = $adminUser['id'];

$activeTab = $_GET['tab'] ?? 'personal';
if (!in_array($activeTab, ['personal', 'password'])) {
    $activeTab = 'personal';
}

$error = null;
$success = null;

// Handle Form Submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';
    
    if (!verifyCsrfToken($csrfToken)) {
        setFlash('danger', __('নিরাপত্তা টোকেন মেয়াদোত্তীর্ণ বা অকার্যকর। পুনরায় চেষ্টা করুন।', 'Security token expired or invalid. Please try again.'));
        redirect('profile.php?tab=' . urlencode($activeTab));
    }
    
    if ($action === 'update_profile') {
        $activeTab = 'personal';
        $firstName = trim($_POST['first_name'] ?? '');
        $lastName = trim($_POST['last_name'] ?? '');
        $email = trim($_POST['email'] ?? '');
        $phone = trim($_POST['phone'] ?? '');
        $dob = trim($_POST['dob'] ?? '');
        $gender = trim($_POST['gender'] ?? 'male');
        $bloodGroup = trim($_POST['blood_group'] ?? 'O+');
        
        if (empty($firstName)) {
            $error = __('প্রথম নাম প্রদান করা আবশ্যক।', 'First name is required.');
        } elseif (empty($email) || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $error = __('সঠিক ইমেইল ঠিকানা প্রদান করুন।', 'Please provide a valid email address.');
        } else {
            $fullName = trim($firstName . ' ' . $lastName);
            $avatarPath = $adminUser['avatar'] ?? null;
            
            // Handle Profile Photo Upload
            if (!empty($_FILES['avatar']['name']) && $_FILES['avatar']['error'] === UPLOAD_ERR_OK) {
                $fileTmp = $_FILES['avatar']['tmp_name'];
                $fileName = $_FILES['avatar']['name'];
                $fileSize = $_FILES['avatar']['size'];
                $fileExt = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
                $allowedExts = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
                
                if (!in_array($fileExt, $allowedExts)) {
                    $error = __('শুধুমাত্র JPG, PNG, GIF বা WEBP ছবি আপলোড করতে পারবেন।', 'Only JPG, PNG, GIF or WEBP images are allowed.');
                } elseif ($fileSize > 2 * 1024 * 1024) {
                    $error = __('ছবির আকার সর্বোচ্চ ২ মেগাবাইট হতে পারবে।', 'Image size must not exceed 2 MB.');
                } else {
                    $uploadDir = __DIR__ . '/assets/uploads/admins/';
                    if (!is_dir($uploadDir)) {
                        @mkdir($uploadDir, 0755, true);
                    }
                    $newFileName = 'admin_' . $adminId . '_' . time() . '.' . $fileExt;
                    $targetFilePath = $uploadDir . $newFileName;
                    
                    if (move_uploaded_file($fileTmp, $targetFilePath)) {
                        $avatarPath = 'assets/uploads/admins/' . $newFileName;
                    } else {
                        $error = __('ছবি আপলোড ব্যর্থ হয়েছে। ডিরেক্টরি পারমিশন চেক করুন।', 'Failed to upload image. Please check directory permissions.');
                    }
                }
            }
            
            if (!$error) {
                try {
                    $dobVal = !empty($dob) ? $dob : null;
                    $stmt = $pdo->prepare("UPDATE admin_users SET full_name = ?, first_name = ?, last_name = ?, email = ?, phone = ?, dob = ?, gender = ?, blood_group = ?, avatar = ? WHERE id = ?");
                    $stmt->execute([$fullName, $firstName, $lastName, $email, $phone, $dobVal, $gender, $bloodGroup, $avatarPath, $adminId]);
                    
                    $_SESSION['admin_full_name'] = $fullName;
                    $_SESSION['admin_email'] = $email;
                    
                    logAdminAction($pdo, 'UPDATE_PROFILE', 'USER', $adminId, "Updated profile info for: {$fullName} (Blood Group: {$bloodGroup})");
                    setFlash('success', __('ব্যক্তিগত তথ্য ও রক্তের গ্রুপ সফলভাবে আপডেট করা হয়েছে।', 'Personal information and blood group updated successfully.'));
                    redirect('profile.php?tab=personal');
                } catch (Exception $e) {
                    $error = __('তথ্য সংরক্ষণকালে ত্রুটি ঘটেছে: ', 'Error updating information: ') . $e->getMessage();
                }
            }
        }
    } elseif ($action === 'change_password') {
        $activeTab = 'password';
        $currentPassword = $_POST['current_password'] ?? '';
        $newPassword = $_POST['new_password'] ?? '';
        $confirmPassword = $_POST['confirm_password'] ?? '';
        
        if (empty($currentPassword)) {
            $error = __('বর্তমান পাসওয়ার্ড প্রদান করুন।', 'Please enter your current password.');
        } elseif (empty($newPassword)) {
            $error = __('নতুন পাসওয়ার্ড প্রদান করুন।', 'Please enter a new password.');
        } elseif (strlen($newPassword) < 6) {
            $error = __('নতুন পাসওয়ার্ড অন্তত ৬ (বা ৮) অক্ষরের হতে হবে।', 'New password must be at least 6 characters.');
        } elseif ($newPassword !== $confirmPassword) {
            $error = __('নতুন পাসওয়ার্ড ও নিশ্চিতকরণ পাসওয়ার্ড মেলেনি।', 'New password and confirm password do not match.');
        } else {
            try {
                $stmt = $pdo->prepare("SELECT password_hash FROM admin_users WHERE id = ?");
                $stmt->execute([$adminId]);
                $dbHash = $stmt->fetchColumn();
                
                $passValid = false;
                if ($dbHash) {
                    if (password_verify($currentPassword, $dbHash)) {
                        $passValid = true;
                    } elseif (hash('sha256', $currentPassword) === $dbHash) {
                        $passValid = true;
                    }
                }
                
                if (!$passValid) {
                    $error = __('বর্তমান পাসওয়ার্ডটি সঠিক নয়।', 'Current password is incorrect.');
                } else {
                    $newHash = password_hash($newPassword, PASSWORD_DEFAULT);
                    $uStmt = $pdo->prepare("UPDATE admin_users SET password_hash = ? WHERE id = ?");
                    $uStmt->execute([$newHash, $adminId]);
                    
                    logAdminAction($pdo, 'CHANGE_PASSWORD', 'USER', $adminId, "Admin password updated successfully");
                    setFlash('success', __('পাসওয়ার্ড সফলভাবে পরিবর্তিত হয়েছে।', 'Password has been updated successfully.'));
                    redirect('profile.php?tab=password');
                }
            } catch (Exception $e) {
                $error = __('পাসওয়ার্ড পরিবর্তনকালে ত্রুটি: ', 'Error updating password: ') . $e->getMessage();
            }
        }
    }
}

// Refresh user data
$adminUser = getAdminUser();
$gender = $adminUser['gender'] ?? 'male';

require_once __DIR__ . '/header.php';
?>

<div class="max-w-6xl mx-auto pb-16 space-y-6">

  <?php if ($error): ?>
    <div class="bg-red-50 dark:bg-red-950/40 border-l-4 border-red-500 text-red-700 dark:text-red-300 p-4 rounded-xl shadow-sm text-sm flex items-center justify-between">
      <div class="flex items-center space-x-2">
        <i class="fa-solid fa-circle-exclamation text-red-500"></i>
        <span><?php echo htmlspecialchars($error); ?></span>
      </div>
    </div>
  <?php endif; ?>

  <!-- Top Hero Banner with Avatar, User Info & Tabs -->
  <div class="bg-gradient-to-r from-emerald-900 via-emerald-800 to-teal-800 rounded-3xl p-6 sm:p-8 text-white shadow-lg relative overflow-hidden">
    <!-- Subtle geometric background overlay -->
    <div class="absolute inset-0 opacity-10 pointer-events-none" style="background-image: radial-gradient(circle at 20px 20px, white 2px, transparent 0); background-size: 32px 32px;"></div>
    
    <div class="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
      
      <!-- User Info & Avatar -->
      <div class="flex items-center space-x-4">
        <div class="relative">
          <div class="w-18 h-18 sm:w-20 sm:h-20 rounded-full border-4 border-white/30 bg-emerald-700/60 flex items-center justify-center overflow-hidden shadow-inner">
            <?php if (!empty($adminUser['avatar']) && file_exists(__DIR__ . '/' . $adminUser['avatar'])): ?>
              <img src="<?php echo htmlspecialchars($adminUser['avatar']); ?>?v=<?php echo time(); ?>" alt="Avatar" class="w-full h-full object-cover">
            <?php else: ?>
              <svg class="w-12 h-12 text-emerald-200" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
              </svg>
            <?php endif; ?>
          </div>
          <!-- Online status indicator -->
          <span class="absolute bottom-1 right-1 w-4 h-4 bg-emerald-400 border-2 border-emerald-900 rounded-full shadow-sm" title="<?php echo __('অনলাইন সক্রিয়', 'Online Active'); ?>"></span>
        </div>
        
        <div>
          <h1 class="text-xl sm:text-2xl font-bold text-white tracking-wide">
            <?php echo htmlspecialchars($adminUser['full_name'] ?: 'Deen One Administrator'); ?>
          </h1>
          <p class="text-sm text-emerald-200/90 font-medium">
            <?php echo htmlspecialchars($adminUser['email'] ?: 'admin@deenone.top'); ?>
          </p>
          <span class="inline-flex items-center gap-1.5 px-2.5 py-0.5 mt-1.5 text-xs font-semibold bg-white/15 text-emerald-100 rounded-full border border-white/20">
            <i class="fa-solid fa-shield text-[10px] text-amber-300"></i>
            <?php echo htmlspecialchars($adminUser['role']); ?>
          </span>
        </div>
      </div>

      <!-- Navigation Tabs (Right Side of Banner) -->
      <div class="flex items-center gap-2 bg-emerald-950/40 p-1.5 rounded-2xl border border-white/10 backdrop-blur-sm self-start md:self-auto">
        <a href="profile.php?tab=personal" 
           class="px-4 py-2.5 rounded-xl text-sm font-semibold transition-all flex items-center gap-2 <?php echo $activeTab === 'personal' ? 'bg-white text-emerald-900 shadow-md' : 'text-emerald-100 hover:text-white hover:bg-white/10'; ?>">
          <i class="fa-regular fa-user"></i>
          <span><?php echo __('প্রোফাইল', 'Profile'); ?></span>
        </a>
        
        <a href="profile.php?tab=password" 
           class="px-4 py-2.5 rounded-xl text-sm font-semibold transition-all flex items-center gap-2 <?php echo $activeTab === 'password' ? 'bg-white text-emerald-900 shadow-md' : 'text-emerald-100 hover:text-white hover:bg-white/10'; ?>">
          <i class="fa-solid fa-lock"></i>
          <span><?php echo __('পাসওয়ার্ড', 'Password'); ?></span>
        </a>
      </div>

    </div>
  </div>

  <!-- TAB 1: PERSONAL INFORMATION -->
  <?php if ($activeTab === 'personal'): ?>
    <div class="bg-white dark:bg-night-card rounded-3xl border border-gray-100 dark:border-night-border p-6 sm:p-8 shadow-sm transition-all">
      
      <!-- Card Header -->
      <div class="mb-6 border-b border-gray-100 dark:border-night-border pb-4">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white tracking-tight">
          <?php echo __('প্রোফাইল তথ্য', 'Profile Info'); ?>
        </h2>
      </div>

      <form method="POST" action="profile.php?tab=personal" enctype="multipart/form-data" class="space-y-6">
        <?php echo csrfField(); ?>
        <input type="hidden" name="action" value="update_profile">

        <!-- Profile Photo Section -->
        <div class="flex items-center space-x-5 p-4 bg-gray-50/70 dark:bg-night-sidebar/40 rounded-2xl border border-gray-100 dark:border-night-border">
          <div class="relative group cursor-pointer" onclick="document.getElementById('avatarFileInput').click();">
            <div class="w-18 h-18 sm:w-20 sm:h-20 rounded-full bg-emerald-100 dark:bg-emerald-950/60 border-2 border-emerald-500/40 flex items-center justify-center overflow-hidden shadow-sm">
              <img id="avatarPreview" 
                   src="<?php echo !empty($adminUser['avatar']) && file_exists(__DIR__ . '/' . $adminUser['avatar']) ? htmlspecialchars($adminUser['avatar']) . '?v=' . time() : 'data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 24 24\' fill=\'%23059669\'><path d=\'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z\'/></svg>'; ?>" 
                   alt="Profile Photo" 
                   class="w-full h-full object-cover">
            </div>
            <!-- Edit Pencil Badge -->
            <button type="button" class="absolute bottom-0 right-0 w-6 h-6 bg-emerald-700 hover:bg-emerald-800 text-white rounded-full flex items-center justify-center shadow-md border-2 border-white dark:border-night-card transition-transform group-hover:scale-110">
              <i class="fa-solid fa-pen text-[10px]"></i>
            </button>
          </div>
          
          <div>
            <h3 class="text-sm font-bold text-gray-800 dark:text-gray-200">
              <?php echo __('প্রোফাইল ছবি', 'Profile photo'); ?>
            </h3>
            <p class="text-xs text-gray-500 dark:text-gray-400 mt-0.5">
              <?php echo __('JPG, PNG বা GIF · সর্বোচ্চ ২ মেগাবাইট', 'JPG, PNG or GIF · Max 2 MB'); ?>
            </p>
            <input type="file" id="avatarFileInput" name="avatar" accept="image/jpeg,image/png,image/gif,image/webp" class="hidden" onchange="previewAvatar(this)">
          </div>
        </div>

        <!-- 2-Column Name Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
              <?php echo __('প্রথম নাম', 'First Name'); ?> <span class="text-red-500">*</span>
            </label>
            <input type="text" name="first_name" required value="<?php echo htmlspecialchars($adminUser['first_name'] ?? ''); ?>" 
                   placeholder="<?php echo __('প্রথম নাম লিখুন', 'First Name'); ?>"
                   class="w-full px-4 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
              <?php echo __('শেষ নাম', 'Last Name'); ?> <span class="text-red-500">*</span>
            </label>
            <input type="text" name="last_name" required value="<?php echo htmlspecialchars($adminUser['last_name'] ?? ''); ?>" 
                   placeholder="<?php echo __('শেষ নাম লিখুন', 'Last Name'); ?>"
                   class="w-full px-4 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
          </div>
        </div>

        <!-- Email Address -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
            <?php echo __('ইমেইল', 'Email'); ?> <span class="text-red-500">*</span>
          </label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
              <i class="fa-regular fa-envelope"></i>
            </span>
            <input type="email" name="email" required value="<?php echo htmlspecialchars($adminUser['email'] ?? ''); ?>" 
                   placeholder="<?php echo __('ইমেইল ঠিকানা লিখুন', 'admin@demo.com'); ?>"
                   class="w-full pl-10 pr-4 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
          </div>
        </div>

        <!-- Phone & Date of Birth Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
          <div>
            <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
              <?php echo __('ফোন', 'Phone'); ?>
            </label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
                <i class="fa-solid fa-phone"></i>
              </span>
              <input type="tel" name="phone" value="<?php echo htmlspecialchars($adminUser['phone'] ?? ''); ?>" 
                     placeholder="+880 1700-000000"
                     class="w-full pl-10 pr-4 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
            </div>
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
              <?php echo __('জন্ম তারিখ', 'Date of Birth'); ?>
            </label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
                <i class="fa-regular fa-calendar"></i>
              </span>
              <input type="date" name="dob" value="<?php echo htmlspecialchars($adminUser['dob'] ?? ''); ?>" 
                     class="w-full pl-10 pr-4 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
            </div>
          </div>
        </div>

        <!-- Gender Selection (Interactive Pill Buttons) -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-2 uppercase tracking-wider">
            <?php echo __('লিঙ্গ', 'Gender'); ?>
          </label>
          <input type="hidden" name="gender" id="selectedGenderInput" value="<?php echo htmlspecialchars($gender); ?>">
          
          <div class="flex flex-wrap gap-3">
            <button type="button" onclick="selectGender('male')" id="genderBtn-male"
                    class="gender-pill px-5 py-2.5 rounded-xl border text-sm font-semibold flex items-center gap-2 transition-all <?php echo $gender === 'male' ? 'border-emerald-600 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-800 dark:text-emerald-300 shadow-sm ring-1 ring-emerald-500' : 'border-gray-200 dark:border-night-border text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-night-sidebar'; ?>">
              <i class="fa-solid fa-person text-base"></i>
              <span><?php echo __('পুরুষ', 'Male'); ?></span>
            </button>
            
            <button type="button" onclick="selectGender('female')" id="genderBtn-female"
                    class="gender-pill px-5 py-2.5 rounded-xl border text-sm font-semibold flex items-center gap-2 transition-all <?php echo $gender === 'female' ? 'border-emerald-600 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-800 dark:text-emerald-300 shadow-sm ring-1 ring-emerald-500' : 'border-gray-200 dark:border-night-border text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-night-sidebar'; ?>">
              <i class="fa-solid fa-person-dress text-base"></i>
              <span><?php echo __('মহিলা', 'Female'); ?></span>
            </button>
            
            <button type="button" onclick="selectGender('other')" id="genderBtn-other"
                    class="gender-pill px-5 py-2.5 rounded-xl border text-sm font-semibold flex items-center gap-2 transition-all <?php echo $gender === 'other' ? 'border-emerald-600 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-800 dark:text-emerald-300 shadow-sm ring-1 ring-emerald-500' : 'border-gray-200 dark:border-night-border text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-night-sidebar'; ?>">
              <i class="fa-regular fa-circle-dot text-base"></i>
              <span><?php echo __('অন্যান্য', 'Other'); ?></span>
            </button>
          </div>
        </div>

        <!-- Blood Group Selection -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-2 uppercase tracking-wider flex items-center gap-1.5">
            <i class="fa-solid fa-droplet text-red-500"></i>
            <span><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></span>
          </label>
          <input type="hidden" name="blood_group" id="selectedBloodGroupInput" value="<?php echo htmlspecialchars($adminUser['blood_group'] ?? 'O+'); ?>">
          
          <div class="flex flex-wrap gap-2.5">
            <?php 
            $bloodGroups = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];
            $currentBg = $adminUser['blood_group'] ?? 'O+';
            foreach ($bloodGroups as $bg): 
              $isActive = ($currentBg === $bg);
              $bgId = 'bgBtn-' . str_replace('+', 'pos', str_replace('-', 'neg', $bg));
            ?>
              <button type="button" onclick="selectBloodGroup('<?php echo $bg; ?>')" id="<?php echo $bgId; ?>"
                      class="blood-pill px-4 py-2.5 rounded-xl border text-sm font-bold flex items-center gap-1.5 transition-all <?php echo $isActive ? 'border-red-500 bg-red-50 dark:bg-red-950/50 text-red-700 dark:text-red-300 shadow-sm ring-1 ring-red-400' : 'border-gray-200 dark:border-night-border text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-night-sidebar'; ?>">
                <i class="fa-solid fa-droplet text-xs <?php echo $isActive ? 'text-red-500 animate-pulse' : 'text-gray-400'; ?>"></i>
                <span><?php echo $bg; ?></span>
              </button>
            <?php endforeach; ?>
          </div>
        </div>

        <!-- Submit Button -->
        <div class="pt-4 border-t border-gray-100 dark:border-night-border">
          <button type="submit" class="btn-primary-action px-6 py-3 bg-emerald-800 hover:bg-emerald-900 dark:bg-emerald-700 dark:hover:bg-emerald-600 text-white font-bold text-sm rounded-xl shadow-md hover:shadow-lg transition-all flex items-center gap-2">
            <i class="fa-solid fa-check"></i>
            <span><?php echo __('সংরক্ষণ', 'Save'); ?></span>
          </button>
        </div>

      </form>
    </div>
  <?php endif; ?>

  <!-- TAB 2: CHANGE PASSWORD -->
  <?php if ($activeTab === 'password'): ?>
    <div class="bg-white dark:bg-night-card rounded-3xl border border-gray-100 dark:border-night-border p-6 sm:p-8 shadow-sm transition-all">
      
      <!-- Card Header -->
      <div class="mb-6 border-b border-gray-100 dark:border-night-border pb-4">
        <h2 class="text-xl font-bold text-gray-900 dark:text-white tracking-tight">
          <?php echo __('পাসওয়ার্ড', 'Password'); ?>
        </h2>
      </div>

      <!-- Info Alert Box -->
      <div class="mb-6 bg-emerald-50 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-800/80 rounded-2xl p-4 flex items-start gap-3">
        <i class="fa-solid fa-circle-info text-emerald-600 dark:text-emerald-400 text-base mt-0.5"></i>
        <p class="text-xs sm:text-sm text-emerald-900 dark:text-emerald-200 leading-relaxed font-medium">
          <?php echo __('ন্যূনতম ৮ অক্ষর, সংখ্যা ও প্রতীকের সমন্বয়ে পাসওয়ার্ড দিন।', 'Use at least 8 characters with numbers and symbols.'); ?>
        </p>
      </div>

      <form method="POST" action="profile.php?tab=password" class="space-y-6">
        <?php echo csrfField(); ?>
        <input type="hidden" name="action" value="change_password">

        <!-- Current Password -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
            <?php echo __('বর্তমান পাসওয়ার্ড', 'Current Password'); ?> <span class="text-red-500">*</span>
          </label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
              <i class="fa-solid fa-lock"></i>
            </span>
            <input type="password" id="currentPassInput" name="current_password" required 
                   placeholder="<?php echo __('বর্তমান পাসওয়ার্ড লিখুন', 'Enter current password'); ?>"
                   class="w-full pl-10 pr-11 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
            <button type="button" onclick="togglePassVisibility('currentPassInput', 'currentPassEye')" class="absolute inset-y-0 right-0 flex items-center pr-3.5 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors">
              <i id="currentPassEye" class="fa-regular fa-eye"></i>
            </button>
          </div>
        </div>

        <!-- New Password -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
            <?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?> <span class="text-red-500">*</span>
          </label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
              <i class="fa-solid fa-lock"></i>
            </span>
            <input type="password" id="newPassInput" name="new_password" required 
                   placeholder="<?php echo __('নতুন পাসওয়ার্ড লিখুন', 'Enter new password'); ?>"
                   class="w-full pl-10 pr-11 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
            <button type="button" onclick="togglePassVisibility('newPassInput', 'newPassEye')" class="absolute inset-y-0 right-0 flex items-center pr-3.5 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors">
              <i id="newPassEye" class="fa-regular fa-eye"></i>
            </button>
          </div>
        </div>

        <!-- Confirm New Password -->
        <div>
          <label class="block text-xs font-bold text-gray-700 dark:text-gray-300 mb-1.5 uppercase tracking-wider">
            <?php echo __('পাসওয়ার্ড নিশ্চিতকরণ', 'Confirm Password'); ?> <span class="text-red-500">*</span>
          </label>
          <div class="relative">
            <span class="absolute inset-y-0 left-0 flex items-center pl-3.5 pointer-events-none text-gray-400">
              <i class="fa-solid fa-lock"></i>
            </span>
            <input type="password" id="confirmPassInput" name="confirm_password" required 
                   placeholder="<?php echo __('নতুন পাসওয়ার্ড পুনরায় লিখুন', 'Re-enter new password'); ?>"
                   class="w-full pl-10 pr-11 py-3 bg-gray-50 dark:bg-night-sidebar border border-gray-200 dark:border-night-border rounded-xl text-gray-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all">
            <button type="button" onclick="togglePassVisibility('confirmPassInput', 'confirmPassEye')" class="absolute inset-y-0 right-0 flex items-center pr-3.5 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors">
              <i id="confirmPassEye" class="fa-regular fa-eye"></i>
            </button>
          </div>
        </div>

        <!-- Submit Button -->
        <div class="pt-4 border-t border-gray-100 dark:border-night-border">
          <button type="submit" class="btn-primary-action px-6 py-3 bg-emerald-800 hover:bg-emerald-900 dark:bg-emerald-700 dark:hover:bg-emerald-600 text-white font-bold text-sm rounded-xl shadow-md hover:shadow-lg transition-all flex items-center gap-2">
            <i class="fa-solid fa-check"></i>
            <span><?php echo __('আপডেট', 'Update Password'); ?></span>
          </button>
        </div>

      </form>
    </div>
  <?php endif; ?>

</div>

<script>
// Client-side Avatar instant preview
function previewAvatar(input) {
  if (input.files && input.files[0]) {
    const reader = new FileReader();
    reader.onload = function(e) {
      document.getElementById('avatarPreview').src = e.target.result;
    };
    reader.readAsDataURL(input.files[0]);
  }
}

// Password show/hide toggle
function togglePassVisibility(inputId, iconId) {
  const input = document.getElementById(inputId);
  const icon = document.getElementById(iconId);
  if (!input || !icon) return;
  
  if (input.type === 'password') {
    input.type = 'text';
    icon.classList.remove('fa-eye');
    icon.classList.add('fa-eye-slash');
  } else {
    input.type = 'password';
    icon.classList.remove('fa-eye-slash');
    icon.classList.add('fa-eye');
  }
}

// Interactive Gender Pill selector
function selectGender(val) {
  document.getElementById('selectedGenderInput').value = val;
  const pills = ['male', 'female', 'other'];
  pills.forEach(p => {
    const btn = document.getElementById('genderBtn-' + p);
    if (!btn) return;
    if (p === val) {
      btn.className = 'gender-pill px-5 py-2.5 rounded-xl border text-sm font-semibold flex items-center gap-2 transition-all border-emerald-600 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-800 dark:text-emerald-300 shadow-sm ring-1 ring-emerald-500';
    } else {
      btn.className = 'gender-pill px-5 py-2.5 rounded-xl border text-sm font-semibold flex items-center gap-2 transition-all border-gray-200 dark:border-night-border text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-night-sidebar';
    }
  });
}

// Interactive Blood Group selector
function selectBloodGroup(val) {
  document.getElementById('selectedBloodGroupInput').value = val;
  const groups = ['A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'];
  groups.forEach(g => {
    const idSuffix = g.replace('+', 'pos').replace('-', 'neg');
    const btn = document.getElementById('bgBtn-' + idSuffix);
    if (!btn) return;
    const icon = btn.querySelector('i');
    if (g === val) {
      btn.className = 'blood-pill px-4 py-2.5 rounded-xl border text-sm font-bold flex items-center gap-1.5 transition-all border-red-500 bg-red-50 dark:bg-red-950/50 text-red-700 dark:text-red-300 shadow-sm ring-1 ring-red-400';
      if (icon) {
        icon.className = 'fa-solid fa-droplet text-xs text-red-500 animate-pulse';
      }
    } else {
      btn.className = 'blood-pill px-4 py-2.5 rounded-xl border text-sm font-bold flex items-center gap-1.5 transition-all border-gray-200 dark:border-night-border text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-night-sidebar';
      if (icon) {
        icon.className = 'fa-solid fa-droplet text-xs text-gray-400';
      }
    }
  });
}
</script>

<?php require_once __DIR__ . '/footer.php'; ?>
