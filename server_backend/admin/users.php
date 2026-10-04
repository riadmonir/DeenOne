<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - APP USER MANAGEMENT SUITE (ব্যবহারকারী ব্যবস্থাপনা)
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
requireAdminLogin();
$pdo = getDbConnection();

// Ensure admin users from admin_users exist in users table
try {
    $adminSyncCheck = $pdo->query("SELECT id, username, full_name, email, phone, gender, blood_group, password_hash, created_at FROM admin_users")->fetchAll(PDO::FETCH_ASSOC);
    foreach ($adminSyncCheck as $adm) {
        $email = $adm['email'];
        $uname = $adm['username'];
        $check = $pdo->prepare("SELECT COUNT(*) FROM users WHERE (email = ? AND ? IS NOT NULL AND ? != '') OR (username = ? AND ? IS NOT NULL AND ? != '')");
        $check->execute([$email, $email, $email, $uname, $uname, $uname]);
        if ($check->fetchColumn() == 0) {
            $uid = 'usr_admin_' . $adm['id'];
            $pass = $adm['password_hash'] ?: password_hash('admin123', PASSWORD_BCRYPT);
            $ins = $pdo->prepare("
                INSERT INTO users (user_id, name, username, email, phone, password_hash, blood_group, gender, district, avatar, total_points, quiz_points, daily_streak, created_at, last_active)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ঢাকা', 'avatar_1', 1250, 450, 15, ?, NOW())
            ");
            $ins->execute([
                $uid,
                $adm['full_name'] ?: 'Deen One Admin',
                $uname,
                !empty($email) ? $email : null,
                !empty($adm['phone']) ? $adm['phone'] : '+880 1700-000000',
                $pass,
                !empty($adm['blood_group']) ? $adm['blood_group'] : 'O+',
                !empty($adm['gender']) ? $adm['gender'] : 'male',
                $adm['created_at'] ?: date('Y-m-d H:i:s')
            ]);
        }
    }
} catch (Exception $e) {}

// Handle Ban/Unban Toggle
if (isset($_GET['toggle_ban'])) {
    $uid = trim($_GET['toggle_ban']);
    try {
        $pdo->prepare("UPDATE users SET is_banned = IF(is_banned=1, 0, 1) WHERE user_id = ?")->execute([$uid]);
        logAdminAction($pdo, 'TOGGLE_USER_BAN', 'users', $uid, 'ব্যান অবস্থা পরিবর্তন');
        setFlash('success', __('ব্যবহারকারীর ব্যান স্ট্যাটাস সফলভাবে পরিবর্তন করা হয়েছে।', 'User ban status updated successfully.'));
    } catch (Exception $e) {}
    redirect('users.php');
}

// Handle Delete User
if (isset($_GET['delete_user'])) {
    $uid = trim($_GET['delete_user']);
    try {
        $pdo->prepare("DELETE FROM users WHERE user_id = ?")->execute([$uid]);
        logAdminAction($pdo, 'DELETE_USER', 'users', $uid, 'ইউজার মুছে ফেলা হয়েছে');
        setFlash('success', __('ব্যবহারকারীর একাউন্ট সফলভাবে মুছে ফেলা হয়েছে।', 'User account deleted successfully.'));
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
    }
    redirect('users.php');
}

// Handle GET CSV Export (Full Database Users)
if (isset($_GET['action']) && $_GET['action'] === 'export_csv') {
    header('Content-Type: text/csv; charset=utf-8');
    header('Content-Disposition: attachment; filename="deenone_users_' . date('Ymd_His') . '.csv"');
    $output = fopen('php://output', 'w');
    fprintf($output, chr(0xEF).chr(0xBB).chr(0xBF)); // UTF-8 BOM

    fputcsv($output, ['User ID', 'Name', 'Username', 'Email', 'Phone', 'Blood Group', 'Gender', 'District', 'Quiz Points', 'Total Points', 'Daily Streak', 'Is Banned', 'Created At']);
    $stmt = $pdo->query("SELECT user_id, name, username, email, phone, blood_group, gender, district, quiz_points, total_points, daily_streak, is_banned, created_at FROM users ORDER BY created_at DESC");
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        fputcsv($output, [
            $row['user_id'], $row['name'], $row['username'], $row['email'], $row['phone'],
            $row['blood_group'], $row['gender'], $row['district'], $row['quiz_points'],
            $row['total_points'], $row['daily_streak'], $row['is_banned'], $row['created_at']
        ]);
    }
    fclose($output);
    exit;
}

// Handle POST actions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';

    if (verifyCsrfToken($csrfToken)) {
        
        // CSV Import Users
        if ($action === 'import_csv') {
            if (isset($_FILES['csv_file']) && $_FILES['csv_file']['error'] === UPLOAD_ERR_OK) {
                $file = $_FILES['csv_file']['tmp_name'];
                $handle = fopen($file, 'r');
                if ($handle !== false) {
                    $header = fgetcsv($handle, 4000, ",");
                    $imported = 0;
                    $stmt = $pdo->prepare("INSERT INTO users (user_id, name, username, email, phone, blood_group, gender, district, password_hash, app_language, theme_mode, created_at, last_active) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'bn', 'dark', NOW(), NOW())
                                           ON DUPLICATE KEY UPDATE name=VALUES(name), phone=VALUES(phone), blood_group=VALUES(blood_group), district=VALUES(district)");
                    $defPass = password_hash('user1234', PASSWORD_BCRYPT);
                    while (($data = fgetcsv($handle, 4000, ",")) !== false) {
                        if (count($data) >= 2) {
                            $uid = !empty($data[0]) ? trim($data[0]) : ('usr_' . bin2hex(random_bytes(5)));
                            $name = !empty($data[1]) ? trim($data[1]) : 'User';
                            $uname = !empty($data[2]) ? trim($data[2]) : ('user_' . substr(md5(uniqid()), 0, 8));
                            $email = !empty($data[3]) ? trim($data[3]) : null;
                            $phone = !empty($data[4]) ? trim($data[4]) : null;
                            $blood = !empty($data[5]) ? trim($data[5]) : null;
                            $gender = !empty($data[6]) ? trim($data[6]) : 'male';
                            $dist = !empty($data[7]) ? trim($data[7]) : 'ঢাকা';

                            $stmt->execute([$uid, $name, $uname, $email, $phone, $blood, $gender, $dist, $defPass]);
                            $imported++;
                        }
                    }
                    fclose($handle);
                    logAdminAction($pdo, 'IMPORT_USERS_CSV', 'users', null, "Imported $imported users from CSV");
                    setFlash('success', sprintf(__('সফলভাবে %d জন ব্যবহারকারীর তথ্য ইমপোর্ট করা হয়েছে!', 'Successfully imported %d users!'), $imported));
                } else {
                    setFlash('danger', __('CSV ফাইল খুলতে ব্যর্থ।', 'Failed to read CSV file.'));
                }
            } else {
                setFlash('danger', __('সঠিক CSV ফাইল নির্বাচন করুন।', 'Please select a valid CSV file.'));
            }
            redirect('users.php');
        }
        
        // 1. Create New User (Mandatory Unique Email)
        if ($action === 'create_user') {
            $name = trim($_POST['name'] ?? '');
            $username = trim($_POST['username'] ?? '');
            $phone = trim($_POST['phone'] ?? '');
            $email = trim($_POST['email'] ?? '');
            $password = trim($_POST['password'] ?? '');
            $bloodGroup = trim($_POST['blood_group'] ?? '');
            $district = trim($_POST['district'] ?? 'ঢাকা');
            $gender = trim($_POST['gender'] ?? 'male');
            $avatar = ($gender === 'female') ? 'avatar_2' : 'avatar_1';

            if (empty($name) || empty($email) || empty($password)) {
                setFlash('danger', __('নাম, ইমেইল এবং পাসওয়ার্ড প্রদান করা আবশ্যক।', 'Name, email and password are required.'));
            } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                setFlash('danger', __('একটি সঠিক ও বৈধ ইমেইল ঠিকানা প্রদান করুন।', 'Please provide a valid email address.'));
            } else {
                try {
                    // Check unique email (Strict 1 Account per Email Policy)
                    $eCheck = $pdo->prepare("SELECT COUNT(*) FROM users WHERE email = ?");
                    $eCheck->execute([$email]);
                    if ($eCheck->fetchColumn() > 0) {
                        throw new Exception(__('এই ইমেইলটি দিয়ে ইতিমধ্যে একটি অ্যাকাউন্ট রয়েছে। একটি ইমেইল দিয়ে একটির বেশি অ্যাকাউন্ট তৈরি সম্ভব নয়।', 'An account with this email already exists. Only one account per email is allowed.'));
                    }

                    $userId = 'usr_' . bin2hex(random_bytes(5));
                    if (!empty($username)) {
                        $cStmt = $pdo->prepare("SELECT COUNT(*) FROM users WHERE username = ?");
                        $cStmt->execute([$username]);
                        if ($cStmt->fetchColumn() > 0) {
                            throw new Exception(__('এই ইউজারনেমটি ইতিমধ্যে বিদ্যমান। অন্য একটি দিন।', 'This username already exists. Please choose another.'));
                        }
                    } else {
                        $username = 'user_' . substr(md5(uniqid()), 0, 8);
                    }

                    $passHash = password_hash($password, PASSWORD_BCRYPT);
                    $stmt = $pdo->prepare("INSERT INTO users (user_id, name, username, phone, email, password_hash, blood_group, gender, district, avatar, app_language, theme_mode, created_at, last_active) 
                                           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'bn', 'dark', NOW(), NOW())");
                    $stmt->execute([
                        $userId, 
                        $name, 
                        $username, 
                        !empty($phone) ? $phone : null, 
                        $email, 
                        $passHash, 
                        !empty($bloodGroup) ? $bloodGroup : null, 
                        $gender,
                        !empty($district) ? $district : null, 
                        $avatar
                    ]);

                    // Sync Blood Donor record
                    if (!empty($bloodGroup) && !empty($phone)) {
                        try {
                            $pdo->prepare("INSERT INTO blood_donors (user_id, name, blood_group, district, phone_number, is_available)
                                           VALUES (?, ?, ?, ?, ?, 1)
                                           ON DUPLICATE KEY UPDATE name=VALUES(name), blood_group=VALUES(blood_group), district=VALUES(district)")
                                ->execute([$userId, $name, $bloodGroup, !empty($district) ? $district : 'ঢাকা', $phone]);
                        } catch (Exception $be) {}
                    }

                    logAdminAction($pdo, 'CREATE_USER', 'users', $userId, "নতুন ইউজার তৈরি: $name ($username)");
                    setFlash('success', __('নতুন ব্যবহারকারী সফলভাবে যুক্ত করা হয়েছে।', 'New user added successfully.'));
                } catch (Exception $e) {
                    setFlash('danger', __('ব্যবহারকারী তৈরি ব্যর্থ: ', 'User creation failed: ') . $e->getMessage());
                }
            }
        }

        // 2. Edit User Info (with Unique Email Check)
        if ($action === 'edit_user') {
            $uid = trim($_POST['user_id'] ?? '');
            $name = trim($_POST['name'] ?? '');
            $username = trim($_POST['username'] ?? '');
            $phone = trim($_POST['phone'] ?? '');
            $email = trim($_POST['email'] ?? '');
            $bloodGroup = trim($_POST['blood_group'] ?? '');
            $district = trim($_POST['district'] ?? '');
            $gender = trim($_POST['gender'] ?? 'male');
            $avatar = ($gender === 'female') ? 'avatar_2' : 'avatar_1';
            $isBanned = isset($_POST['is_banned']) ? 1 : 0;
            $newPassword = trim($_POST['new_password'] ?? '');

            if (!empty($uid) && !empty($name)) {
                try {
                    if (!empty($username)) {
                        $uCheck = $pdo->prepare("SELECT COUNT(*) FROM users WHERE username = ? AND user_id != ?");
                        $uCheck->execute([$username, $uid]);
                        if ($uCheck->fetchColumn() > 0) {
                            throw new Exception(__('এই ইউজারনেমটি অন্য কোনো ব্যবহারকারীর রয়েছে।', 'This username is already taken.'));
                        }
                    }

                    if (!empty($email)) {
                        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                            throw new Exception(__('একটি সঠিক ও বৈধ ইমেইল ঠিকানা প্রদান করুন।', 'Please provide a valid email address.'));
                        }
                        $eCheck = $pdo->prepare("SELECT COUNT(*) FROM users WHERE email = ? AND user_id != ?");
                        $eCheck->execute([$email, $uid]);
                        if ($eCheck->fetchColumn() > 0) {
                            throw new Exception(__('এই ইমেইলটি অন্য একজন ব্যবহারকারীর অ্যাকাউন্টে নিবন্ধিত রয়েছে।', 'This email is already registered to another user account.'));
                        }
                    }

                    $stmt = $pdo->prepare("UPDATE users SET name = ?, username = ?, phone = ?, email = ?, blood_group = ?, gender = ?, district = ?, avatar = ?, is_banned = ? WHERE user_id = ?");
                    $stmt->execute([
                        $name, 
                        !empty($username) ? $username : null, 
                        !empty($phone) ? $phone : null, 
                        !empty($email) ? $email : null, 
                        !empty($bloodGroup) ? $bloodGroup : null, 
                        $gender,
                        !empty($district) ? $district : null, 
                        $avatar, 
                        $isBanned, 
                        $uid
                    ]);

                    if (!empty($newPassword) && strlen($newPassword) >= 6) {
                        $passHash = password_hash($newPassword, PASSWORD_BCRYPT);
                        $pdo->prepare("UPDATE users SET password_hash = ? WHERE user_id = ?")->execute([$passHash, $uid]);
                    }
                    
                    // Sync Blood Donor record
                    if (!empty($bloodGroup) && !empty($phone)) {
                        try {
                            $pdo->prepare("INSERT INTO blood_donors (user_id, name, blood_group, district, phone_number, is_available)
                                           VALUES (?, ?, ?, ?, ?, 1)
                                           ON DUPLICATE KEY UPDATE name=VALUES(name), blood_group=VALUES(blood_group), district=VALUES(district)")
                                ->execute([$uid, $name, $bloodGroup, !empty($district) ? $district : 'ঢাকা', $phone]);
                        } catch (Exception $be) {}
                    }

                    logAdminAction($pdo, 'EDIT_USER_INFO', 'users', $uid, "ইউজার প্রোফাইল সম্পাদনা: $name");
                    setFlash('success', __('ব্যবহারকারীর প্রোফাইল তথ্য সফলভাবে আপডেট করা হয়েছে।', 'User details updated successfully.'));
                } catch (Exception $e) {
                    setFlash('danger', __('আপডেট ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
                }
            }
        }

        // 3. Reset User Password
        if ($action === 'reset_password') {
            $uid = trim($_POST['user_id'] ?? '');
            $newPassword = trim($_POST['new_password'] ?? '');

            if (!empty($uid) && strlen($newPassword) >= 6) {
                try {
                    $newHash = password_hash($newPassword, PASSWORD_BCRYPT);
                    $pdo->prepare("UPDATE users SET password_hash = ?, reset_code = NULL, reset_expires = NULL WHERE user_id = ?")
                        ->execute([$newHash, $uid]);
                    logAdminAction($pdo, 'RESET_USER_PASSWORD', 'users', $uid, "পাসওয়ার্ড রিসেট সম্পন্ন");
                    setFlash('success', __('ব্যবহারকারীর পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে।', 'User password reset successfully.'));
                } catch (Exception $e) {
                    setFlash('danger', __('পাসওয়ার্ড পরিবর্তন ব্যর্থ: ', 'Password change failed: ') . $e->getMessage());
                }
            } else {
                setFlash('danger', __('পাসওয়ার্ড ন্যূনতম ৬ অক্ষরের হতে হবে।', 'Password must be at least 6 characters.'));
            }
        }

        // 4. Promote User to Admin Account Directly by Role (Zero Password Reset / Profile Edit required)
        if ($action === 'grant_admin_role' || $action === 'make_admin') {
            $uid = trim($_POST['user_id'] ?? '');
            $role = strtoupper(trim($_POST['role'] ?? 'MODERATOR'));
            $allowedRoles = ['SUPERADMIN', 'EDITOR', 'MODERATOR', 'VIEWER'];
            if (!in_array($role, $allowedRoles)) {
                $role = 'MODERATOR';
            }

            if (empty($uid)) {
                setFlash('danger', __('ব্যবহারকারী চিহ্নিত করা যায়নি।', 'User could not be identified.'));
            } else {
                try {
                    $uStmt = $pdo->prepare("SELECT * FROM users WHERE user_id = ? LIMIT 1");
                    $uStmt->execute([$uid]);
                    $u = $uStmt->fetch(PDO::FETCH_ASSOC);

                    if (!$u) {
                        throw new Exception(__('ব্যবহারকারী পাওয়া যায়নি।', 'User not found.'));
                    }

                    $username = trim($u['username'] ?? '');
                    if (empty($username)) {
                        if (!empty($u['email'])) {
                            $username = explode('@', $u['email'])[0];
                            $username = preg_replace('/[^a-zA-Z0-9_]/', '', $username);
                        }
                        if (empty($username)) {
                            $username = 'admin_' . str_replace('usr_', '', $u['user_id']);
                        }
                    }

                    $fullName = trim($u['name'] ?? $username);
                    $email = !empty($u['email']) ? trim($u['email']) : null;
                    $phone = !empty($u['phone']) ? trim($u['phone']) : null;
                    $gender = !empty($u['gender']) ? trim($u['gender']) : 'male';
                    $bloodGroup = !empty($u['blood_group']) ? trim($u['blood_group']) : 'O+';
                    $passwordHash = $u['password_hash'] ?? '';

                    if (empty($passwordHash)) {
                        $passwordHash = password_hash('admin123', PASSWORD_BCRYPT);
                    }

                    // Check if admin account already exists by username or email
                    $existingAdmin = null;
                    if (!empty($email)) {
                        $checkStmt = $pdo->prepare("SELECT id FROM admin_users WHERE email = ? OR username = ? LIMIT 1");
                        $checkStmt->execute([$email, $username]);
                        $existingAdmin = $checkStmt->fetch(PDO::FETCH_ASSOC);
                    } else {
                        $checkStmt = $pdo->prepare("SELECT id FROM admin_users WHERE username = ? LIMIT 1");
                        $checkStmt->execute([$username]);
                        $existingAdmin = $checkStmt->fetch(PDO::FETCH_ASSOC);
                    }

                    if ($existingAdmin) {
                        $upd = $pdo->prepare("UPDATE admin_users SET role = ?, full_name = ?, password_hash = ?, email = COALESCE(?, email), phone = COALESCE(?, phone), gender = ?, blood_group = ? WHERE id = ?");
                        $upd->execute([$role, $fullName, $passwordHash, $email, $phone, $gender, $bloodGroup, $existingAdmin['id']]);
                        $adminId = $existingAdmin['id'];
                    } else {
                        // Ensure unique username in admin_users
                        $unameCheck = $pdo->prepare("SELECT COUNT(*) FROM admin_users WHERE username = ?");
                        $unameCheck->execute([$username]);
                        if ($unameCheck->fetchColumn() > 0) {
                            $username = $username . '_' . substr(md5(uniqid()), 0, 4);
                        }

                        $ins = $pdo->prepare("INSERT INTO admin_users (username, password_hash, full_name, email, phone, gender, blood_group, role, created_at) 
                                               VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())");
                        $ins->execute([$username, $passwordHash, $fullName, $email, $phone, $gender, $bloodGroup, $role]);
                        $adminId = $pdo->lastInsertId();
                    }

                    logAdminAction($pdo, 'GRANT_ADMIN_ACCESS', 'admin_users', (string)$adminId, "ইউজারকে অ্যাডমিন রোলে নিযুক্ত করা হয়েছে: {$u['name']} ($username) -> $role");
                    
                    $roleNamesBn = [
                        'SUPERADMIN' => 'সুপার অ্যাডমিন',
                        'EDITOR' => 'এডিটর',
                        'MODERATOR' => 'মডারেটর',
                        'VIEWER' => 'ভিউয়ার'
                    ];
                    $roleNamesEn = [
                        'SUPERADMIN' => 'Super Admin',
                        'EDITOR' => 'Editor',
                        'MODERATOR' => 'Moderator',
                        'VIEWER' => 'Viewer'
                    ];
                    $roleLabel = isEn() ? ($roleNamesEn[$role] ?? $role) : ($roleNamesBn[$role] ?? $role);

                    setFlash('success', sprintf(__('সফলভাবে "%s"-কে %s হিসেবে অ্যাডমিন এক্সেস প্রদান করা হয়েছে!', 'Successfully granted %s access to "%s"!'), $fullName, $roleLabel));
                } catch (Exception $e) {
                    setFlash('danger', __('অ্যাডমিন এক্সেস দিতে ব্যর্থ: ', 'Failed to grant admin access: ') . $e->getMessage());
                }
            }
        }

        // 5. Revoke Admin Access (Demote Admin/Moderator to Regular User)
        if ($action === 'revoke_admin_role') {
            $uid = trim($_POST['user_id'] ?? '');
            if (empty($uid)) {
                setFlash('danger', __('ব্যবহারকারী চিহ্নিত করা যায়নি।', 'User could not be identified.'));
            } else {
                try {
                    $uStmt = $pdo->prepare("SELECT * FROM users WHERE user_id = ? LIMIT 1");
                    $uStmt->execute([$uid]);
                    $u = $uStmt->fetch(PDO::FETCH_ASSOC);

                    if (!$u) {
                        throw new Exception(__('ব্যবহারকারী পাওয়া যায়নি।', 'User not found.'));
                    }

                    $email = !empty($u['email']) ? trim($u['email']) : null;
                    $username = trim($u['username'] ?? '');

                    // Delete corresponding admin record from admin_users
                    $delStmt = $pdo->prepare("DELETE FROM admin_users WHERE (email = ? AND ? IS NOT NULL AND ? != '') OR (username = ? AND ? != '')");
                    $delStmt->execute([$email, $email, $email, $username, $username]);
                    $affected = $delStmt->rowCount();

                    if ($affected > 0) {
                        logAdminAction($pdo, 'REVOKE_ADMIN_ACCESS', 'users', $uid, "অ্যাডমিন এক্সেস বাতিল করে সাধারণ ইউজার করা হয়েছে: {$u['name']} ($username)");
                        setFlash('success', sprintf(__('সফলভাবে "%s"-এর অ্যাডমিন এক্সেস বাতিল করা হয়েছে এবং তাকে সাধারণ ইউজার করা হয়েছে।', 'Successfully revoked admin access for "%s" and reverted to regular user.'), $u['name']));
                    } else {
                        setFlash('info', __('এই ব্যবহারকারীর কোনো সক্রিয় অ্যাডমিন অ্যাকাউন্ট পাওয়া যায়নি।', 'No active admin account found for this user.'));
                    }
                } catch (Exception $e) {
                    setFlash('danger', __('অ্যাডমিন এক্সেস বাতিল করতে ব্যর্থ: ', 'Failed to revoke admin access: ') . $e->getMessage());
                }
            }
        }
    }
    redirect('users.php');
}

// -----------------------------------------------------------------------------
// Real-time Statistics Summary (Clickable & Real-time last 7 days active)
// -----------------------------------------------------------------------------
$totalUsersCount = 0;
$totalAdminsCount = 0;
$activeUsersCount = 0;
$bannedUsersCount = 0;
$bloodDonorsCount = 0;

try {
    $totalUsersCount = (int)$pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
    $totalAdminsCount = (int)$pdo->query("SELECT COUNT(*) FROM admin_users")->fetchColumn();
    
    // Real-time Active: last active within 7 days and not banned
    $activeUsersCount = (int)$pdo->query("SELECT COUNT(*) FROM users WHERE is_banned = 0 AND (last_active >= DATE_SUB(NOW(), INTERVAL 7 DAY) OR last_active IS NOT NULL)")->fetchColumn();
    $bannedUsersCount = (int)$pdo->query("SELECT COUNT(*) FROM users WHERE is_banned = 1")->fetchColumn();
    $bloodDonorsCount = (int)$pdo->query("SELECT COUNT(*) FROM users WHERE blood_group IS NOT NULL AND blood_group != ''")->fetchColumn();
} catch (Exception $e) {}

// Search & Filter & Pagination
$search = trim($_GET['search'] ?? '');
$filterStatus = trim($_GET['status'] ?? 'all');
$filterBlood = trim($_GET['blood'] ?? 'all');
$page = max(1, (int)($_GET['page'] ?? 1));
$perPage = 20;
$offset = ($page - 1) * $perPage;

$where = [];
$params = [];

if (!empty($search)) {
    $where[] = "(name LIKE ? OR username LIKE ? OR phone LIKE ? OR email LIKE ? OR user_id LIKE ? OR district LIKE ?)";
    $term = "%$search%";
    $params = [$term, $term, $term, $term, $term, $term];
}

if ($filterStatus === 'active') {
    $where[] = "is_banned = 0";
} elseif ($filterStatus === 'banned') {
    $where[] = "is_banned = 1";
} elseif ($filterStatus === 'recent') {
    $where[] = "is_banned = 0 AND last_active >= DATE_SUB(NOW(), INTERVAL 7 DAY)";
}

if ($filterBlood !== 'all' && !empty($filterBlood)) {
    $where[] = "blood_group = ?";
    $params[] = $filterBlood;
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

$filteredCount = 0;
$users = [];
try {
    $countStmt = $pdo->prepare("SELECT COUNT(*) FROM users $whereSql");
    $countStmt->execute($params);
    $filteredCount = (int)$countStmt->fetchColumn();

    $sql = "SELECT u.user_id, u.name, u.username, u.email, u.phone, u.blood_group, u.gender, u.district, u.avatar, u.app_language, u.theme_mode, 
                   u.total_points, u.quiz_points, u.battles_played, u.battles_won, u.daily_streak, u.last_streak_date, 
                   u.is_banned, u.last_active, u.created_at,
                   a.id AS admin_id, a.role AS admin_role,
                   (SELECT COALESCE(SUM(COALESCE(tap_count, count, 0)), 0) FROM user_tasbih_logs WHERE user_id = u.user_id) AS total_tasbih_count,
                   (SELECT COUNT(*) FROM user_amal_records WHERE user_id = u.user_id) AS completed_amal_count
            FROM users u
            LEFT JOIN admin_users a ON (
                (a.email = u.email AND u.email IS NOT NULL AND u.email != '')
                OR (a.username = u.username AND u.username IS NOT NULL AND u.username != '')
            )
            $whereSql 
            ORDER BY u.created_at DESC 
            LIMIT $perPage OFFSET $offset";
    $uStmt = $pdo->prepare($sql);
    $uStmt->execute($params);
    $users = $uStmt->fetchAll();
} catch (Exception $e) {}

$totalPages = max(1, ceil($filteredCount / $perPage));

$bangladeshDistricts = [
    'ঢাকা', 'চট্টগ্রাম', 'রাজশাহী', 'খুলনা', 'বরিশাল', 'সিলেট', 'রংপুর', 'ময়মনসিংহ',
    'বাগেরহাট', 'বান্দরবান', 'বরগুনা', 'ভোলা', 'বগুড়া', 'ব্রাহ্মণবাড়িয়া', 'চাঁদপুর', 'চাঁপাইনবাবগঞ্জ',
    'চুয়াডাঙ্গা', 'কুমিল্লা', 'কক্সবাজার', 'দিনাজপুর', 'ফরিদপুর', 'ফেনী', 'গাইবান্ধা', 'গাজীপুর',
    'গোপালগঞ্জ', 'হবিগঞ্জ', 'জামালপুর', 'যশোর', 'ঝালকাঠি', 'ঝিনাইদহ', 'জয়পুরহাট', 'খাগড়াছড়ি',
    'কুড়িগ্রাম', 'কুষ্টিয়া', 'লক্ষ্মীপুর', 'লালমনিরহাট', 'মাদারীপুর', 'মাগুরা', 'মানিকগঞ্জ', 'মেহেরপুর',
    'মৌলভীবাজার', 'মুন্সীগঞ্জ', 'নওগাঁ', 'নড়াইল', 'নারায়ণগঞ্জ', 'নরসিংদী', 'নাটোর', 'নেত্রকোণা',
    'নীলফামারী', 'নোয়াখালী', 'পাবনা', 'পঞ্চগড়', 'পটুয়াখালী', 'পিরোজপুর', 'রাজবাড়ী', 'রাঙ্গামাটি',
    'সাতক্ষীরা', 'শরীয়তপুর', 'শেরপুর', 'সিরাজগঞ্জ', 'সুনামগঞ্জ', 'টাঙ্গাইল', 'ঠাকুরগাঁও'
];

$pageTitle = __('ইউজার', 'Users');
$activeNav = 'users';
require_once __DIR__ . '/header.php';
?>

<!-- ==============================================================================
     TOP FUNCTIONAL & CLICKABLE STATS SUMMARY CARDS
     ============================================================================== -->
<div class="stats-grid" style="margin-bottom: 24px;">
  <!-- Total Users (Click to reset and view all users) -->
  <a href="users.php" class="stat-card" style="text-decoration: none; cursor: pointer;" title="<?php echo __('সকল নিবন্ধিত ব্যবহারকারী দেখুন', 'View All Registered Users'); ?>">
    <div class="stat-icon-wrap emerald">
      <i class="fa-solid fa-users"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($totalUsersCount); ?></h3>
      <p><?php echo __('মোট ইউজার', 'Total Users'); ?></p>
    </div>
  </a>

  <!-- Total Admins (Click leads to Admin Accounts section) -->
  <a href="admin_users.php" class="stat-card" style="text-decoration: none; cursor: pointer;" title="<?php echo __('অ্যাডমিন ও স্টাফ অ্যাকাউন্ট সেকশনে যান', 'Go to Admin Accounts section'); ?>">
    <div class="stat-icon-wrap amber">
      <i class="fa-solid fa-user-shield"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($totalAdminsCount); ?></h3>
      <p><?php echo __('অ্যাডমিন ↗', 'Admins ↗'); ?></p>
    </div>
  </a>

  <!-- Real-time Active Users (Click filters active users) -->
  <a href="users.php?status=active" class="stat-card" style="text-decoration: none; cursor: pointer;" title="<?php echo __('সক্রিয় ব্যবহারকারীদের তালিকা ফিল্টার করুন', 'Filter Active Users'); ?>">
    <div class="stat-icon-wrap blue">
      <i class="fa-solid fa-user-check"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($activeUsersCount); ?></h3>
      <p><?php echo __('সক্রিয় ইউজার', 'Active Users'); ?></p>
    </div>
  </a>

  <!-- Blood Donors (Click leads to Blood Donors section) -->
  <a href="blood_donors.php?tab=donors" class="stat-card" style="text-decoration: none; cursor: pointer;" title="<?php echo __('রক্তদাতা নেটওয়ার্কে যান', 'Go to Blood Donors Network'); ?>">
    <div class="stat-icon-wrap red">
      <i class="fa-solid fa-droplet"></i>
    </div>
    <div class="stat-details">
      <h3 class="counter-number"><?php echo toLangNum($bloodDonorsCount); ?></h3>
      <p><?php echo __('রক্তদাতা ↗', 'Blood Donors ↗'); ?></p>
    </div>
  </a>
</div>

<!-- ==============================================================================
     ACTION & SEARCH TOOLBAR
     ============================================================================== -->
<div class="card" style="margin-bottom: 20px;">
  <div class="card-body" style="padding: 16px 20px;">
    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 14px;">
      
      <!-- Search & Filters -->
      <form method="GET" action="users.php" style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap; flex: 1;">
        <div style="flex: 1; min-width: 220px;">
          <input type="text" name="search" class="form-control" style="height: 38px; font-size: 13px;" placeholder="<?php echo __('নাম, ইউজারনেম, ফোন, ইমেইল, জেলা...', 'Search name, username, phone, email, district...'); ?>" value="<?php echo htmlspecialchars($search); ?>">
        </div>

        <select name="status" class="form-control" style="width: auto; height: 38px; font-size: 13px;">
          <option value="all" <?php echo $filterStatus === 'all' ? 'selected' : ''; ?>><?php echo __('সব স্ট্যাটাস', 'All Status'); ?></option>
          <option value="active" <?php echo $filterStatus === 'active' ? 'selected' : ''; ?>><?php echo __('সক্রিয়', 'Active Only'); ?></option>
          <option value="banned" <?php echo $filterStatus === 'banned' ? 'selected' : ''; ?>><?php echo __('ব্যানড', 'Banned Only'); ?></option>
        </select>

        <select name="blood" class="form-control" style="width: auto; height: 38px; font-size: 13px;">
          <option value="all"><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></option>
          <?php foreach (['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] as $bg): ?>
            <option value="<?php echo $bg; ?>" <?php echo $filterBlood === $bg ? 'selected' : ''; ?>><?php echo $bg; ?></option>
          <?php endforeach; ?>
        </select>

        <button type="submit" class="btn btn-secondary btn-sm" style="height: 38px;">
          <i class="fa-solid fa-magnifying-glass"></i> <?php echo __('ফিল্টার', 'Filter'); ?>
        </button>

        <?php if (!empty($search) || $filterStatus !== 'all' || $filterBlood !== 'all'): ?>
          <a href="users.php" class="btn btn-secondary btn-sm" style="height: 38px;"><?php echo __('রিসেট', 'Reset'); ?></a>
        <?php endif; ?>
      </form>

      <!-- Primary Action Buttons -->
      <div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
        <a href="users.php?action=export_csv" class="btn btn-secondary btn-sm" style="height: 38px; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-file-arrow-down"></i> <?php echo __('CSV এক্সপোর্ট', 'Export CSV'); ?>
        </a>
        <button type="button" class="btn btn-secondary btn-sm" onclick="openImportModal()" style="height: 38px; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-file-arrow-up"></i> <?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?>
        </button>
        <button type="button" class="btn btn-primary btn-sm" onclick="openAddUserModal()" style="height: 38px; display: inline-flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-user-plus"></i> <?php echo __('নতুন ইউজার', 'Add User'); ?>
        </button>
      </div>

    </div>
  </div>
</div>

<!-- ==============================================================================
     CLEAN USER DIRECTORY TABLE
     ============================================================================== -->
<div class="card">
  <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
    <div class="card-title" style="display: flex; align-items: center; gap: 8px;">
      <i class="fa-solid fa-address-book" style="color: var(--primary);"></i>
      <span><?php echo __('ইউজার তালিকা', 'User Directory'); ?></span>
      <span class="badge badge-info" style="font-size: 11px;"><?php echo toLangNum($filteredCount); ?> <?php echo __('জন', 'Members'); ?></span>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table" id="usersTable">
      <thead>
        <tr>
          <th style="width: 50px; text-align: center;">#</th>
          <th><?php echo __('ইউজার', 'User'); ?></th>
          <th><?php echo __('যোগাযোগ', 'Contact'); ?></th>
          <th style="text-align: center;"><?php echo __('রক্ত', 'Blood'); ?></th>
          <th><?php echo __('জেলা', 'District'); ?></th>
          <th style="text-align: center;"><?php echo __('স্ট্যাটাস', 'Status'); ?></th>
          <th><?php echo __('সক্রিয়', 'Active'); ?></th>
          <th style="text-align: right; width: 160px;"><?php echo __('অ্যাকশন', 'Actions'); ?></th>
        </tr>
      </thead>
      <tbody>
        <?php if (empty($users)): ?>
          <tr>
            <td colspan="8" style="text-align: center; color: var(--text-dim); padding: 36px 20px;">
              <div style="font-size: 32px; margin-bottom: 8px;">🔍</div>
              <div><?php echo __('কোনো ব্যবহারকারী পাওয়া যায়নি।', 'No users found matching your search.'); ?></div>
            </td>
          </tr>
        <?php else: ?>
          <?php foreach ($users as $idx => $u): ?>
            <tr>
              <!-- Serial -->
              <td style="text-align: center; font-weight: 700; color: var(--text-dim); font-size: 12px;">
                <?php echo toLangNum($offset + $idx + 1); ?>
              </td>

              <!-- User Profile Identity (Clickable Name opens full Activity Modal) -->
              <td>
                <div style="display: flex; align-items: center; gap: 10px;">
                  <div onclick='openUserDetailsModal(<?php echo json_encode($u, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)' style="width: 38px; height: 38px; border-radius: 50%; background: var(--hover-bg); border: 1.5px solid var(--border-color); display: flex; align-items: center; justify-content: center; font-size: 17px; flex-shrink: 0; cursor: pointer; transition: transform 0.15s;" title="<?php echo __('সম্পূর্ণ প্রোফাইল ও অ্যাক্টিভিটি দেখুন', 'View Full Profile & Activity'); ?>">
                    <?php 
                      $isFemale = ($u['gender'] ?? '') === 'female' || ($u['avatar'] ?? '') === 'avatar_2';
                      echo $isFemale ? '🧕' : '🧔';
                    ?>
                  </div>
                  <div>
                    <div style="display: flex; align-items: center; gap: 6px; flex-wrap: wrap;">
                      <a href="javascript:void(0)" onclick='openUserDetailsModal(<?php echo json_encode($u, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)' style="font-weight: 700; color: var(--text-heading); font-size: 13.5px; text-decoration: none;" class="hover:underline" title="<?php echo __('ক্লিক করে সমস্ত বিবরণ ও অ্যাক্টিভিটি দেখুন', 'Click to view full details & activity'); ?>">
                        <?php echo htmlspecialchars($u['name']); ?>
                      </a>
                      <?php if (!empty($u['admin_role'])): ?>
                        <?php 
                          $roleLabel = [
                              'SUPERADMIN' => __('সুপার অ্যাডমিন', 'Super Admin'),
                              'EDITOR' => __('এডিটর', 'Editor'),
                              'MODERATOR' => __('মডারেটর', 'Moderator'),
                              'VIEWER' => __('ভিউয়ার', 'Viewer')
                          ][$u['admin_role']] ?? $u['admin_role'];
                        ?>
                        <span class="badge badge-warning" style="font-size: 10px; padding: 2px 7px; font-weight: 800; background: rgba(245, 158, 11, 0.15); color: var(--accent-gold); border: 1px solid rgba(245, 158, 11, 0.35);">
                          <i class="fa-solid fa-shield" style="font-size: 9px; margin-right: 3px;"></i><?php echo $roleLabel; ?>
                        </span>
                      <?php endif; ?>
                    </div>
                    <div style="font-size: 11px; color: var(--text-dim); display: flex; align-items: center; gap: 6px; margin-top: 2px;">
                      <span style="font-family: monospace;">@<?php echo htmlspecialchars($u['username'] ?: $u['user_id']); ?></span>
                      <span>•</span>
                      <span><?php echo !empty($u['created_at']) ? date('d M Y', strtotime($u['created_at'])) : ''; ?></span>
                    </div>
                  </div>
                </div>
              </td>

              <!-- Contact (Separated Column) -->
              <td>
                <div style="font-size: 12px;">
                  <?php if (!empty($u['phone'])): ?>
                    <div style="color: var(--text-main); font-weight: 600;">
                      <i class="fa-solid fa-phone" style="font-size: 10px; color: var(--text-dim); margin-right: 4px;"></i>
                      <?php echo htmlspecialchars($u['phone']); ?>
                    </div>
                  <?php endif; ?>
                  <?php if (!empty($u['email'])): ?>
                    <div style="font-size: 11px; color: var(--text-dim);">
                      <i class="fa-regular fa-envelope" style="font-size: 10px; margin-right: 4px;"></i>
                      <?php echo htmlspecialchars($u['email']); ?>
                    </div>
                  <?php endif; ?>
                  <?php if (empty($u['phone']) && empty($u['email'])): ?>
                    <span style="color: var(--text-dim); font-size: 11px;">—</span>
                  <?php endif; ?>
                </div>
              </td>

              <!-- Blood Group (Separated Column) -->
              <td style="text-align: center;">
                <?php if (!empty($u['blood_group'])): ?>
                  <span class="badge badge-danger" style="font-size: 11px; padding: 2.5px 8px; font-weight: 800;">
                    <i class="fa-solid fa-droplet text-red-500" style="margin-right: 3px; font-size: 10px;"></i><?php echo htmlspecialchars($u['blood_group']); ?>
                  </span>
                <?php else: ?>
                  <span style="color: var(--text-dim); font-size: 12px;">—</span>
                <?php endif; ?>
              </td>

              <!-- District -->
              <td>
                <span style="font-size: 12.5px; color: var(--text-main); display: inline-flex; align-items: center; gap: 4px;">
                  <i class="fa-solid fa-location-dot" style="color: var(--primary); font-size: 11px;"></i>
                  <?php echo htmlspecialchars($u['district'] ?? __('ঢাকা', 'Dhaka')); ?>
                </span>
              </td>

              <!-- Status Badge -->
              <td style="text-align: center;">
                <span class="badge <?php echo ($u['is_banned'] ?? 0) ? 'badge-danger' : 'badge-success'; ?>" style="font-size: 11px; padding: 3px 8px;">
                  <?php echo ($u['is_banned'] ?? 0) ? __('🚫 ব্যানড', '🚫 Banned') : __('✓ সক্রিয়', '✓ Active'); ?>
                </span>
              </td>

              <!-- Last Active -->
              <td style="font-size: 11.5px; color: var(--text-dim); white-space: nowrap;">
                <?php if (!empty($u['last_active'])): ?>
                  <div><?php echo date('d M, Y', strtotime($u['last_active'])); ?></div>
                  <div style="font-size: 10.5px;"><?php echo date('h:i A', strtotime($u['last_active'])); ?></div>
                <?php else: ?>
                  —
                <?php endif; ?>
              </td>

              <!-- Actions (View, Edit, Direct Ban/Unban Toggle, Make Admin, Reset Pass, Delete) -->
              <td style="text-align: right;">
                <div style="display: flex; gap: 4px; justify-content: flex-end;">
                  <!-- View Details -->
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 4px 7px; font-size: 11px;" title="<?php echo __('পূর্ণ বিবরণ ও অ্যাক্টিভিটি দেখুন', 'View Full Details'); ?>" onclick='openUserDetailsModal(<?php echo json_encode($u, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)'>
                    <i class="fa-solid fa-eye"></i>
                  </button>
                  
                  <!-- Edit Profile -->
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 4px 7px; font-size: 11px;" title="<?php echo __('প্রোফাইল তথ্য সম্পাদনা', 'Edit Profile'); ?>" onclick='openEditUserModal(<?php echo json_encode($u, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)'>
                    <i class="fa-solid fa-pen"></i>
                  </button>

                  <!-- Direct Ban/Unban Toggle Action Button -->
                  <a href="users.php?toggle_ban=<?php echo urlencode($u['user_id']); ?>" 
                     class="btn <?php echo ($u['is_banned'] ?? 0) ? 'btn-secondary' : 'btn-danger'; ?> btn-sm" 
                     style="padding: 4px 7px; font-size: 11px;" 
                     title="<?php echo ($u['is_banned'] ?? 0) ? __('আনব্যান / সক্রিয় করুন', 'Unban User') : __('ব্যান / ব্লক করুন', 'Ban User'); ?>"
                     onclick="return confirm('<?php echo ($u['is_banned'] ?? 0) ? __('ব্যবহারকারীকে আনব্যান করতে চান?', 'Do you want to unban this user?') : __('ব্যবহারকারীকে ব্যান / ব্লক করতে চান?', 'Are you sure you want to ban this user?'); ?>');">
                    <i class="fa-solid <?php echo ($u['is_banned'] ?? 0) ? 'fa-user-check' : 'fa-ban'; ?>"></i>
                  </a>

                  <!-- Make / Manage Admin / Moderator -->
                  <button type="button" 
                          class="btn btn-secondary btn-sm" 
                          style="padding: 4px 7px; font-size: 11px; <?php echo !empty($u['admin_role']) ? 'color: var(--accent-gold); background: rgba(245, 158, 11, 0.15); border-color: rgba(245, 158, 11, 0.4); font-weight: 700;' : 'color: var(--text-muted);'; ?>" 
                          title="<?php echo !empty($u['admin_role']) ? __('অ্যাডমিন ভূমিকা পরিচালনা বা বাতিল করুন', 'Manage or Revoke Admin Role') : __('অ্যাডমিন বা মডারেটর বানান', 'Make Admin / Moderator'); ?>" 
                          onclick='openMakeAdminModal(<?php echo json_encode($u, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP); ?>)'>
                    <i class="fa-solid fa-user-shield"></i>
                  </button>

                  <!-- Password Reset -->
                  <button type="button" class="btn btn-secondary btn-sm" style="padding: 4px 7px; font-size: 11px;" title="<?php echo __('পাসওয়ার্ড পরিবর্তন', 'Reset Password'); ?>" onclick="openResetPasswordModal('<?php echo htmlspecialchars($u['user_id']); ?>', '<?php echo htmlspecialchars(addslashes($u['name'])); ?>')">
                    <i class="fa-solid fa-key"></i>
                  </button>

                  <!-- Delete -->
                  <a href="users.php?delete_user=<?php echo urlencode($u['user_id']); ?>" class="btn btn-danger btn-sm" style="padding: 4px 7px; font-size: 11px;" title="<?php echo __('ইউজার মুছে ফেলুন', 'Delete User'); ?>" onclick="return confirm('<?php echo __('এই ইউজারের সমস্ত তথ্য চিরতরে মুছে ফেলতে চান?', 'Are you sure you want to permanently delete this user?'); ?>');">
                    <i class="fa-solid fa-trash-can"></i>
                  </a>
                </div>
              </td>
            </tr>
          <?php endforeach; ?>
        <?php endif; ?>
      </tbody>
    </table>
  </div>

  <!-- Pagination -->
  <?php if ($totalPages > 1): ?>
    <div style="padding: 14px 20px; border-top: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
      <span style="font-size: 12.5px; color: var(--text-dim);">
        <?php echo __('পৃষ্ঠা ', 'Page ') . toLangNum($page) . __(' এর ', ' of ') . toLangNum($totalPages); ?> 
        (<?php echo __('মোট: ', 'Total: ') . toLangNum($filteredCount); ?>)
      </span>
      <div style="display: flex; gap: 6px;">
        <?php if ($page > 1): ?>
          <a href="users.php?page=<?php echo $page - 1; ?>&search=<?php echo urlencode($search); ?>&status=<?php echo urlencode($filterStatus); ?>&blood=<?php echo urlencode($filterBlood); ?>" class="btn btn-secondary btn-sm"><i class="fa-solid fa-chevron-left" style="font-size: 10px; margin-right: 4px;"></i><?php echo __('পূর্ববর্তী', 'Previous'); ?></a>
        <?php endif; ?>
        <?php if ($page < $totalPages): ?>
          <a href="users.php?page=<?php echo $page + 1; ?>&search=<?php echo urlencode($search); ?>&status=<?php echo urlencode($filterStatus); ?>&blood=<?php echo urlencode($filterBlood); ?>" class="btn btn-secondary btn-sm"><?php echo __('পরবর্তী', 'Next'); ?> <i class="fa-solid fa-chevron-right" style="font-size: 10px; margin-left: 4px;"></i></a>
        <?php endif; ?>
      </div>
    </div>
  <?php endif; ?>
</div>

<!-- ==============================================================================
     MODAL: COMPREHENSIVE USER DETAILS & FULL ACTIVITY PROFILE
     ============================================================================== -->
<div class="modal-backdrop" id="userDetailsModal">
  <div class="modal-window" style="max-width: 640px; max-height: 90vh; overflow-y: auto;">
    <div class="modal-header" style="background: linear-gradient(135deg, rgba(16, 185, 129, 0.12), rgba(6, 78, 59, 0.2)); padding: 14px 20px;">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px; font-size: 15px;">
        <i class="fa-solid fa-id-card" style="color: var(--primary);"></i>
        <span><?php echo __('ইউজার প্রোফাইল', 'User Profile'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('userDetailsModal')">&times;</button>
    </div>

    <div class="modal-body" style="padding: 18px 20px;">
      
      <!-- User Hero Header Card (Compact & High Contrast) -->
      <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 14px 16px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px; margin-bottom: 16px;">
        <div style="display: flex; align-items: center; gap: 12px;">
          <div id="viewUserAvatarWrap" style="width: 44px; height: 44px; border-radius: 50%; background: var(--bg-card); border: 2px solid var(--primary); display: flex; align-items: center; justify-content: center; font-size: 18px; color: var(--primary); box-shadow: 0 2px 8px var(--primary-glow); flex-shrink: 0;">
            <i class="fa-solid fa-user"></i>
          </div>
          <div>
            <h3 id="viewUserName" style="margin: 0 0 2px; font-size: 15px; font-weight: 800; color: var(--text-heading);">—</h3>
            <div style="font-size: 11.5px; color: var(--text-dim); display: flex; align-items: center; gap: 6px;">
              <span id="viewUserUsername" style="font-family: monospace; color: var(--primary-light); font-weight: 600;">@username</span>
              <span>•</span>
              <span id="viewUserId" style="font-family: monospace; font-size: 10.5px;">usr_id</span>
            </div>
          </div>
        </div>

        <div>
          <span id="viewUserStatusBadge" class="badge badge-success" style="font-size: 11px; padding: 3px 8px;"><i class="fa-solid fa-check" style="margin-right: 3px; font-size: 9px;"></i>সক্রিয়</span>
        </div>
      </div>

      <!-- Section 1: Personal & Contact Information (Compact 3-col Grid) -->
      <div style="margin-bottom: 16px;">
        <h4 style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 10px; display: flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-user-gear" style="color: var(--primary); font-size: 11px;"></i>
          <span><?php echo __('যোগাযোগ তথ্য', 'Contact Info'); ?></span>
        </h4>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 10px;">
          <!-- Phone -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-solid fa-phone" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('ফোন', 'Phone'); ?></div>
            <div id="viewUserPhone" style="font-weight: 600; font-size: 12.5px; color: var(--text-heading); word-break: break-all;">—</div>
          </div>

          <!-- Email -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-regular fa-envelope" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('ইমেইল', 'Email'); ?></div>
            <div id="viewUserEmail" style="font-weight: 600; font-size: 12.5px; color: var(--text-heading); word-break: break-all;">—</div>
          </div>

          <!-- Blood Group -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-solid fa-droplet text-red-500" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></div>
            <div id="viewUserBlood" style="font-weight: 800; font-size: 12.5px; color: #ef4444;">—</div>
          </div>

          <!-- Gender & District -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-solid fa-user" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('লিঙ্গ ও জেলা', 'Gender & District'); ?></div>
            <div id="viewUserGenderDistrict" style="font-weight: 600; font-size: 12px; color: var(--text-heading);">—</div>
          </div>

          <!-- Joined Date -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-solid fa-calendar-days" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('নিবন্ধন', 'Registration'); ?></div>
            <div id="viewUserJoined" style="font-weight: 600; font-size: 12px; color: var(--text-heading);">—</div>
          </div>

          <!-- Last Active -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 12px;">
            <div style="font-size: 10.5px; color: var(--text-dim); margin-bottom: 2px;"><i class="fa-solid fa-clock" style="font-size: 10px; margin-right: 3px;"></i><?php echo __('সর্বশেষ সক্রিয়', 'Last Active'); ?></div>
            <div id="viewUserLastActive" style="font-weight: 600; font-size: 11.5px; color: var(--text-heading);">—</div>
          </div>
        </div>
      </div>

      <!-- Section 2: App Activity & Gamification Records (Compact 4-col Cards) -->
      <div>
        <h4 style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 10px; display: flex; align-items: center; gap: 6px;">
          <i class="fa-solid fa-chart-line" style="color: #f59e0b; font-size: 11px;"></i>
          <span><?php echo __('অ্যাক্টিভিটি ও রেকর্ড', 'Activity & Records'); ?></span>
        </h4>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 10px;">
          <!-- Total XP -->
          <div style="background: linear-gradient(135deg, rgba(16, 185, 129, 0.12), rgba(6, 78, 59, 0.06)); border: 1px solid rgba(16, 185, 129, 0.3); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: #10b981; margin-bottom: 3px;"><i class="fa-solid fa-bolt"></i></div>
            <div id="viewUserPoints" style="font-size: 15px; font-weight: 800; color: var(--primary-light);">0 XP</div>
            <div style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('মোট পয়েন্ট', 'Total Points'); ?></div>
          </div>

          <!-- Total Tasbih Count -->
          <div style="background: linear-gradient(135deg, rgba(59, 130, 246, 0.12), rgba(30, 58, 138, 0.06)); border: 1px solid rgba(59, 130, 246, 0.3); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: #3b82f6; margin-bottom: 3px;"><i class="fa-solid fa-hands-praying"></i></div>
            <div id="viewUserTasbihCount" style="font-size: 15px; font-weight: 800; color: #3b82f6;">0</div>
            <div style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('তাসবীহ', 'Tasbih'); ?></div>
          </div>

          <!-- Completed Good Deeds -->
          <div style="background: linear-gradient(135deg, rgba(168, 85, 247, 0.12), rgba(107, 33, 168, 0.06)); border: 1px solid rgba(168, 85, 247, 0.3); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: #a855f7; margin-bottom: 3px;"><i class="fa-solid fa-leaf"></i></div>
            <div id="viewUserAmalCount" style="font-size: 15px; font-weight: 800; color: #a855f7;">0 টি</div>
            <div style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('নেক আমল', 'Good Deeds'); ?></div>
          </div>

          <!-- Battle Record -->
          <div style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.12), rgba(180, 83, 9, 0.06)); border: 1px solid rgba(245, 158, 11, 0.3); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: var(--accent-gold); margin-bottom: 3px;"><i class="fa-solid fa-shield-halved"></i></div>
            <div id="viewUserBattles" style="font-size: 13px; font-weight: 800; color: var(--accent-gold);">0 জয় / 0 ম্যাচ</div>
            <div id="viewUserWinRate" style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('জয়ের হার: ', 'Win rate: '); ?>0%</div>
          </div>

          <!-- Daily Streak -->
          <div style="background: linear-gradient(135deg, rgba(249, 115, 22, 0.12), rgba(194, 65, 12, 0.06)); border: 1px solid rgba(249, 115, 22, 0.3); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: #f97316; margin-bottom: 3px;"><i class="fa-solid fa-fire"></i></div>
            <div id="viewUserStreak" style="font-size: 15px; font-weight: 800; color: #f97316;">0 দিন</div>
            <div style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('স্ট্রিক', 'Daily Streak'); ?></div>
          </div>

          <!-- Leaderboard Tier -->
          <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 8px; text-align: center;">
            <div style="font-size: 16px; color: #eab308; margin-bottom: 3px;"><i class="fa-solid fa-trophy"></i></div>
            <div id="viewUserTier" style="font-size: 12.5px; font-weight: 800; color: var(--text-heading);">—</div>
            <div style="font-size: 10.5px; color: var(--text-dim); margin-top: 1px;"><?php echo __('টিয়ার', 'Tier'); ?></div>
          </div>
        </div>
      </div>

    </div>

    <!-- Modal Footer Actions (Compact) -->
    <div class="modal-footer" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 8px; padding: 12px 20px;">
      <div style="display: flex; gap: 6px;">
        <button type="button" id="btnModalToggleBan" class="btn btn-secondary btn-sm" onclick="toggleCurrentUserBan()">
          <i class="fa-solid fa-ban"></i> <span id="btnModalToggleBanText"><?php echo __('ব্যান', 'Ban'); ?></span>
        </button>
        <button type="button" id="btnModalMakeAdmin" class="btn btn-secondary btn-sm" style="color: var(--accent-gold);" onclick="triggerMakeAdminFromDetails()" title="<?php echo __('এই ইউজারকে অ্যাডমিন বা মডারেটর বানান', 'Make Admin / Moderator'); ?>">
          <i class="fa-solid fa-user-shield"></i> <?php echo __('অ্যাডমিন', 'Make Admin'); ?>
        </button>
        <button type="button" id="btnModalResetPass" class="btn btn-secondary btn-sm" onclick="triggerPassResetFromDetails()">
          <i class="fa-solid fa-key"></i> <?php echo __('পাসওয়ার্ড', 'Password'); ?>
        </button>
      </div>

      <div style="display: flex; gap: 6px;">
        <button type="button" class="btn btn-primary btn-sm" onclick="triggerEditFromDetails()">
          <i class="fa-solid fa-pen-to-square"></i> <?php echo __('সম্পাদনা', 'Edit Profile'); ?>
        </button>
        <button type="button" class="btn btn-secondary btn-sm" onclick="closeModal('userDetailsModal')"><?php echo __('বন্ধ', 'Close'); ?></button>
      </div>
    </div>
  </div>
</div>

<!-- ==============================================================================
     MODAL: ADD NEW USER
     ============================================================================== -->
<div class="modal-backdrop" id="addUserModal">
  <div class="modal-window" style="max-width: 600px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-user-plus" style="color: var(--primary);"></i>
        <span><?php echo __('নতুন ইউজার', 'New User'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('addUserModal')">&times;</button>
    </div>
    <form method="POST" action="users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="create_user">

      <div class="modal-body" style="padding: 20px;">
        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('নাম', 'Name'); ?> *</label>
            <input type="text" name="name" class="form-control" placeholder="<?php echo __('যেমন: আব্দুল্লাহ আল মামুন', 'e.g. Abdullah Al Mamun'); ?>" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ইউজারনেম', 'Username'); ?></label>
            <input type="text" name="username" class="form-control" placeholder="<?php echo __('যেমন: abdullah99', 'e.g. abdullah99'); ?>">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('ইমেইল', 'Email'); ?> *</label>
            <input type="email" name="email" class="form-control" placeholder="user@example.com" required>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('ফোন', 'Phone'); ?></label>
            <input type="text" name="phone" class="form-control" placeholder="+880 1700-000000">
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('পাসওয়ার্ড', 'Password'); ?> *</label>
            <input type="password" name="password" class="form-control" placeholder="<?php echo __('ন্যূনতম ৬ অক্ষর', 'Min 6 characters'); ?>" required minlength="6">
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></label>
            <select name="blood_group" class="form-control">
              <option value=""><?php echo __('নির্বাচন করুন', 'Select Blood Group'); ?></option>
              <?php foreach (['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] as $bg): ?>
                <option value="<?php echo $bg; ?>"><?php echo $bg; ?></option>
              <?php endforeach; ?>
            </select>
          </div>
        </div>

        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
          <div class="form-group">
            <label class="form-label"><?php echo __('জেলা', 'District'); ?></label>
            <select name="district" class="form-control">
              <?php foreach ($bangladeshDistricts as $dst): ?>
                <option value="<?php echo $dst; ?>" <?php echo $dst === 'ঢাকা' ? 'selected' : ''; ?>><?php echo $dst; ?></option>
              <?php endforeach; ?>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label"><?php echo __('লিঙ্গ', 'Gender'); ?> *</label>
            <select name="gender" class="form-control">
              <option value="male" selected>👨 <?php echo __('পুরুষ', 'Male'); ?></option>
              <option value="female">🧕 <?php echo __('নারী', 'Female'); ?></option>
            </select>
          </div>
        </div>

      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('addUserModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-check"></i> <?php echo __('যোগ করুন', 'Add User'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<!-- ==============================================================================
     MODAL: EDIT USER PROFILE
     ============================================================================== -->
<div class="modal-backdrop" id="editUserModal">
  <div class="modal-window" style="max-width: 620px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-user-pen" style="color: var(--primary);"></i>
        <span><?php echo __('ইউজার সম্পাদনা', 'Edit User'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('editUserModal')">&times;</button>
    </div>

    <!-- Dual Tab Switcher (Matching profile.php) -->
    <div style="display: flex; border-bottom: 1px solid var(--border-color); background: var(--hover-bg); padding: 6px 16px 0;">
      <button type="button" id="tabUserPersonalBtn" class="tab-btn active" onclick="switchUserEditTab('personal')" style="padding: 10px 16px; border: none; background: transparent; font-weight: 700; font-size: 13px; color: var(--primary); border-bottom: 2px solid var(--primary); cursor: pointer; display: flex; align-items: center; gap: 6px;">
        <i class="fa-regular fa-user"></i> <span><?php echo __('প্রোফাইল তথ্য', 'Profile Info'); ?></span>
      </button>
      <button type="button" id="tabUserPasswordBtn" class="tab-btn" onclick="switchUserEditTab('password')" style="padding: 10px 16px; border: none; background: transparent; font-weight: 700; font-size: 13px; color: var(--text-dim); border-bottom: 2px solid transparent; cursor: pointer; display: flex; align-items: center; gap: 6px;">
        <i class="fa-solid fa-lock"></i> <span><?php echo __('পাসওয়ার্ড পরিবর্তন', 'Change Password'); ?></span>
      </button>
    </div>

    <form method="POST" action="users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="edit_user">
      <input type="hidden" name="user_id" id="editUserUid" value="">

      <div class="modal-body" style="padding: 20px;">
        
        <!-- TAB 1: PERSONAL INFORMATION -->
        <div id="tabUserPersonalContent">
          <!-- User ID Badge & Ban Toggle -->
          <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 10px 14px; margin-bottom: 16px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 8px;">
            <div style="font-size: 12px; color: var(--text-dim);">
              <?php echo __('ইউজার আইডি:', 'User ID:'); ?> <strong id="editUidDisplay" style="font-family: monospace; color: var(--primary-light);"></strong>
            </div>
            <label style="display: flex; align-items: center; gap: 6px; font-size: 12.5px; font-weight: 600; cursor: pointer; color: #ef4444;">
              <input type="checkbox" name="is_banned" id="editUserBanned" value="1">
              <span><?php echo __('অ্যাকাউন্ট ব্যান করুন', 'Ban Account'); ?></span>
            </label>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('নাম', 'Name'); ?> *</label>
              <input type="text" name="name" id="editUserName" class="form-control" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ইউজারনেম', 'Username'); ?></label>
              <input type="text" name="username" id="editUserUsername" class="form-control">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('ইমেইল', 'Email'); ?> *</label>
              <input type="email" name="email" id="editUserEmail" class="form-control" required>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('ফোন', 'Phone'); ?></label>
              <input type="text" name="phone" id="editUserPhone" class="form-control">
            </div>
          </div>

          <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px;">
            <div class="form-group">
              <label class="form-label"><?php echo __('রক্তের গ্রুপ', 'Blood Group'); ?></label>
              <select name="blood_group" id="editUserBlood" class="form-control">
                <option value=""><?php echo __('নির্বাচন করুন', 'Select'); ?></option>
                <?php foreach (['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] as $bg): ?>
                  <option value="<?php echo $bg; ?>"><?php echo $bg; ?></option>
                <?php endforeach; ?>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('জেলা', 'District'); ?></label>
              <select name="district" id="editUserDistrict" class="form-control">
                <?php foreach ($bangladeshDistricts as $dst): ?>
                  <option value="<?php echo $dst; ?>"><?php echo $dst; ?></option>
                <?php endforeach; ?>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label"><?php echo __('লিঙ্গ', 'Gender'); ?></label>
              <select name="gender" id="editUserGender" class="form-control">
                <option value="male">👨 <?php echo __('পুরুষ', 'Male'); ?></option>
                <option value="female">🧕 <?php echo __('নারী', 'Female'); ?></option>
              </select>
            </div>
          </div>
        </div>

        <!-- TAB 2: CHANGE PASSWORD -->
        <div id="tabUserPasswordContent" style="display: none;">
          <div style="background: rgba(16, 185, 129, 0.08); border: 1px solid rgba(16, 185, 129, 0.2); border-radius: var(--radius-md); padding: 12px 14px; margin-bottom: 16px; display: flex; align-items: center; gap: 10px;">
            <i class="fa-solid fa-circle-info" style="color: var(--primary); font-size: 15px;"></i>
            <span style="font-size: 12.5px; color: var(--text-main);">
              <?php echo __('পাসওয়ার্ড অপরিবর্তিত রাখতে খালি রাখুন', 'Leave blank to keep password unchanged'); ?>
            </span>
          </div>

          <div class="form-group">
            <label class="form-label"><?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?></label>
            <div class="input-group" style="position: relative;">
              <input type="password" name="new_password" id="editUserNewPassword" class="form-control" placeholder="<?php echo __('নতুন পাসওয়ার্ড লিখুন', 'Enter new password'); ?>" minlength="6">
              <button type="button" onclick="togglePassVisibility('editUserNewPassword', 'eyeIconUserPass')" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); background: none; border: none; color: var(--text-dim); cursor: pointer; padding: 4px;">
                <i id="eyeIconUserPass" class="fa-regular fa-eye"></i>
              </button>
            </div>
          </div>
        </div>

      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('editUserModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i> <?php echo __('সংরক্ষণ', 'Save'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<!-- ==============================================================================
     MODAL: GRANT / MANAGE / REVOKE ADMIN ACCESS
     ============================================================================== -->
<div class="modal-backdrop" id="makeAdminModal">
  <div class="modal-window" style="max-width: 520px;">
    <div class="modal-header" style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.12), rgba(180, 83, 9, 0.18)); padding: 16px 22px;">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px; font-size: 16px;">
        <i class="fa-solid fa-user-shield" style="color: var(--accent-gold);"></i>
        <span id="makeAdminModalTitle"><?php echo __('অ্যাডমিন এক্সেস', 'Admin Access'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('makeAdminModal')">&times;</button>
    </div>
    <form method="POST" action="users.php" id="makeAdminForm">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" id="makeAdminAction" value="grant_admin_role">
      <input type="hidden" name="user_id" id="makeAdminUid" value="">

      <div class="modal-body" style="padding: 22px;">
        <!-- User Info Identity Summary Card -->
        <div style="background: var(--hover-bg); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 14px 16px; margin-bottom: 16px; display: flex; align-items: center; gap: 14px;">
          <div id="makeAdminAvatar" style="font-size: 26px; width: 48px; height: 48px; border-radius: 50%; background: var(--bg-card); border: 2px solid var(--accent-gold); display: flex; align-items: center; justify-content: center; flex-shrink: 0; box-shadow: 0 2px 8px rgba(245, 158, 11, 0.2);">🧔</div>
          <div style="flex: 1; min-width: 0;">
            <div id="makeAdminName" style="font-weight: 800; font-size: 15px; color: var(--text-heading); word-break: break-all;">—</div>
            <div id="makeAdminSub" style="font-size: 12px; color: var(--text-dim); word-break: break-all; margin-top: 2px;">—</div>
          </div>
        </div>

        <!-- Current Role Alert Banner (When User Is Already Admin) -->
        <div id="makeAdminCurrentRoleBox" style="display: none; background: rgba(245, 158, 11, 0.1); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: var(--radius-md); padding: 12px 14px; margin-bottom: 16px;">
          <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px;">
            <div>
              <div style="font-size: 11px; color: var(--text-dim); text-transform: uppercase; font-weight: 700;"><?php echo __('বর্তমান পদবী:', 'Current Role:'); ?></div>
              <div id="makeAdminCurrentRoleText" style="font-weight: 800; font-size: 14px; color: var(--accent-gold); margin-top: 2px;">—</div>
            </div>
            <button type="button" class="btn btn-danger btn-sm" onclick="revokeAdminRoleSubmit()" style="padding: 5px 10px; font-size: 11.5px; font-weight: 700;">
              <i class="fa-solid fa-user-slash"></i> <?php echo __('বাতিল', 'Revoke'); ?>
            </button>
          </div>
        </div>

        <!-- Role Selection -->
        <div class="form-group mb-4">
          <label class="form-label" style="font-weight: 800; font-size: 13.5px; margin-bottom: 8px;">
            <i class="fa-solid fa-shield-halved text-amber-500 mr-1"></i>
            <span id="makeAdminRoleLabel"><?php echo __('দায়িত্ব ও রোল *', 'Role *'); ?></span>
          </label>
          <select name="role" id="makeAdminRole" class="form-control font-bold" style="padding: 12px 14px; font-size: 14px;" required>
            <option value="MODERATOR"><?php echo __('মডারেটর — রক্তদান ও ইউজার', 'Moderator — Donors & Users'); ?></option>
            <option value="EDITOR"><?php echo __('এডিটর — কনটেন্ট ও প্রকাশনা', 'Editor — Content'); ?></option>
            <option value="SUPERADMIN"><?php echo __('সুপার অ্যাডমিন — পূর্ণ এক্সেস', 'Super Admin — Full Access'); ?></option>
            <option value="VIEWER"><?php echo __('ভিউয়ার — দর্শন এক্সেস', 'Viewer — Read-only'); ?></option>
          </select>
        </div>

        <!-- Seamless Password & Account Sync Note -->
        <div style="background: rgba(16, 185, 129, 0.08); border: 1px solid rgba(16, 185, 129, 0.22); border-radius: var(--radius-md); padding: 12px 14px; display: flex; align-items: flex-start; gap: 10px;">
          <i class="fa-solid fa-circle-check" style="color: var(--primary); font-size: 16px; margin-top: 2px; flex-shrink: 0;"></i>
          <div style="font-size: 12px; color: var(--text-main); line-height: 1.5;">
            <?php echo __('ব্যবহারকারী তার বর্তমান লগইন তথ্য দিয়ে সরাসরি অ্যাডমিন প্যানেলে প্রবেশ করতে পারবেন।', 'User can log in to admin panel using existing credentials.'); ?>
          </div>
        </div>
      </div>

      <div class="modal-footer" style="padding: 16px 22px; display: flex; justify-content: space-between; align-items: center; gap: 10px;">
        <div>
          <button type="button" id="btnModalRevokeBottom" class="btn btn-danger btn-sm" onclick="revokeAdminRoleSubmit()" style="display: none; font-size: 12px;">
            <i class="fa-solid fa-user-xmark"></i> <?php echo __('এক্সেস বাতিল', 'Revoke Access'); ?>
          </button>
        </div>
        <div style="display: flex; gap: 8px;">
          <button type="button" class="btn btn-secondary" onclick="closeModal('makeAdminModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
          <button type="submit" id="btnMakeAdminSubmit" class="btn btn-primary" style="background: linear-gradient(135deg, #f59e0b, #d97706); border-color: #d97706;">
            <i class="fa-solid fa-check"></i> <span id="btnMakeAdminSubmitText"><?php echo __('নিশ্চিত করুন', 'Confirm Access'); ?></span>
          </button>
        </div>
      </div>
    </form>
  </div>
</div>

<!-- ==============================================================================
     MODAL: RESET PASSWORD
     ============================================================================== -->
<div class="modal-backdrop" id="resetPasswordModal">
  <div class="modal-window" style="max-width: 420px;">
    <div class="modal-header">
      <div class="modal-title" style="display: flex; align-items: center; gap: 8px;">
        <i class="fa-solid fa-key" style="color: #f59e0b;"></i>
        <span><?php echo __('পাসওয়ার্ড রিসেট', 'Reset Password'); ?></span>
      </div>
      <button type="button" class="btn-modal-close" onclick="closeModal('resetPasswordModal')">&times;</button>
    </div>
    <form method="POST" action="users.php">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="reset_password">
      <input type="hidden" name="user_id" id="resetPassUid" value="">

      <div class="modal-body">
        <p style="font-size: 13px; color: var(--text-dim); margin-bottom: 14px;" id="resetPassUserDesc"></p>
        <div class="form-group">
          <label class="form-label"><?php echo __('নতুন পাসওয়ার্ড', 'New Password'); ?> *</label>
          <input type="text" name="new_password" class="form-control" required minlength="6" placeholder="******">
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('resetPasswordModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-check"></i> <?php echo __('আপডেট', 'Update Password'); ?>
        </button>
      </div>
    </form>
  </div>
</div>

<script>
let currentUserObj = null;

function switchUserEditTab(tab) {
  const pContent = document.getElementById('tabUserPersonalContent');
  const passContent = document.getElementById('tabUserPasswordContent');
  const pBtn = document.getElementById('tabUserPersonalBtn');
  const passBtn = document.getElementById('tabUserPasswordBtn');

  if (tab === 'personal') {
    pContent.style.display = 'block';
    passContent.style.display = 'none';
    pBtn.style.color = 'var(--primary)';
    pBtn.style.borderBottomColor = 'var(--primary)';
    passBtn.style.color = 'var(--text-dim)';
    passBtn.style.borderBottomColor = 'transparent';
  } else {
    pContent.style.display = 'none';
    passContent.style.display = 'block';
    passBtn.style.color = 'var(--primary)';
    passBtn.style.borderBottomColor = 'var(--primary)';
    pBtn.style.color = 'var(--text-dim)';
    pBtn.style.borderBottomColor = 'transparent';
  }
}

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

function openAddUserModal() {
  openModal('addUserModal');
}

function openUserDetailsModal(user) {
  currentUserObj = user;
  
  const isFemale = (user.gender === 'female' || user.avatar === 'avatar_2');
  document.getElementById('viewUserAvatarWrap').textContent = isFemale ? '🧕' : '🧔';
  document.getElementById('viewUserName').textContent = user.name || '—';
  document.getElementById('viewUserUsername').textContent = '@' + (user.username || user.user_id || '—');
  document.getElementById('viewUserId').textContent = 'ID: ' + (user.user_id || '—');
  
  const isBanned = (parseInt(user.is_banned) === 1);
  const statusBadge = document.getElementById('viewUserStatusBadge');
  const toggleBanBtn = document.getElementById('btnModalToggleBan');
  const toggleBanText = document.getElementById('btnModalToggleBanText');
  
  if (isBanned) {
    statusBadge.className = 'badge badge-danger';
    statusBadge.textContent = '🚫 <?php echo __('ব্যানড', 'Banned'); ?>';
    toggleBanBtn.className = 'btn btn-secondary btn-sm';
    toggleBanText.textContent = '<?php echo __('আনব্যান করুন', 'Unban'); ?>';
  } else {
    statusBadge.className = 'badge badge-success';
    statusBadge.textContent = '✓ <?php echo __('সক্রিয়', 'Active'); ?>';
    toggleBanBtn.className = 'btn btn-danger btn-sm';
    toggleBanText.textContent = '<?php echo __('ব্যান করুন', 'Ban'); ?>';
  }

  // Admin button state in user details modal
  const makeAdminBtn = document.getElementById('btnModalMakeAdmin');
  if (user.admin_role) {
    makeAdminBtn.innerHTML = '<i class="fa-solid fa-user-shield"></i> <?php echo __('দায়িত্ব পরিবর্তন / বাতিল', 'Manage / Revoke Role'); ?>';
  } else {
    makeAdminBtn.innerHTML = '<i class="fa-solid fa-user-shield"></i> <?php echo __('অ্যাডমিন বানান', 'Make Admin'); ?>';
  }

  document.getElementById('viewUserPhone').textContent = user.phone || '—';
  document.getElementById('viewUserEmail').textContent = user.email || '—';
  document.getElementById('viewUserBlood').textContent = user.blood_group || '—';
  
  const genderName = isFemale ? '<?php echo __('নারী', 'Female'); ?>' : '<?php echo __('পুরুষ', 'Male'); ?>';
  const dist = user.district || '<?php echo __('ঢাকা', 'Dhaka'); ?>';
  document.getElementById('viewUserGenderDistrict').textContent = genderName + ' • ' + dist;
  
  const joinDate = user.created_at ? user.created_at.substring(0, 10) : '—';
  const lastActive = user.last_active || '—';
  document.getElementById('viewUserJoined').textContent = joinDate;
  document.getElementById('viewUserLastActive').textContent = lastActive;

  // Gamification stats
  const points = parseInt(user.total_points || 0);
  document.getElementById('viewUserPoints').textContent = points.toLocaleString() + ' XP';

  const tasbihCount = parseInt(user.total_tasbih_count || 0);
  document.getElementById('viewUserTasbihCount').textContent = tasbihCount.toLocaleString();

  const amalCount = parseInt(user.completed_amal_count || 0);
  document.getElementById('viewUserAmalCount').textContent = amalCount.toLocaleString() + ' <?php echo __('টি', ''); ?>';
  
  const won = parseInt(user.battles_won || 0);
  const played = parseInt(user.battles_played || 0);
  const winRate = played > 0 ? Math.round((won / played) * 100) : 0;
  document.getElementById('viewUserBattles').textContent = won + ' <?php echo __('জয়', 'Won'); ?> / ' + played + ' <?php echo __('ম্যাচ', 'Matches'); ?>';
  document.getElementById('viewUserWinRate').textContent = '<?php echo __('জয়ের হার: ', 'Win rate: '); ?>' + winRate + '%';
  
  const streak = parseInt(user.daily_streak || 0);
  document.getElementById('viewUserStreak').textContent = streak + ' <?php echo __('দিন', 'Days'); ?>';

  // Clean Tier calculation (No bracket pollution)
  let tier = '<?php echo __('শিক্ষানবিস', 'Novice'); ?>';
  if (points >= 2000) tier = '<?php echo __('গ্র্যান্ডমাস্টার', 'Grandmaster'); ?>';
  else if (points >= 1000) tier = '<?php echo __('মাস্টার', 'Master'); ?>';
  else if (points >= 500) tier = '<?php echo __('স্কলার', 'Scholar'); ?>';
  else if (points >= 200) tier = '<?php echo __('এক্সপ্লোরার', 'Explorer'); ?>';
  document.getElementById('viewUserTier').textContent = tier;

  openModal('userDetailsModal');
}

function triggerEditFromDetails() {
  closeModal('userDetailsModal');
  if (currentUserObj) {
    openEditUserModal(currentUserObj);
  }
}

function triggerMakeAdminFromDetails() {
  closeModal('userDetailsModal');
  if (currentUserObj) {
    openMakeAdminModal(currentUserObj);
  }
}

function triggerPassResetFromDetails() {
  closeModal('userDetailsModal');
  if (currentUserObj) {
    openResetPasswordModal(currentUserObj.user_id, currentUserObj.name);
  }
}

function openMakeAdminModal(user) {
  currentUserObj = user;
  document.getElementById('makeAdminUid').value = user.user_id || '';
  document.getElementById('makeAdminAction').value = 'grant_admin_role';
  
  const isFemale = (user.gender === 'female' || user.avatar === 'avatar_2');
  document.getElementById('makeAdminAvatar').innerHTML = isFemale ? '<i class="fa-solid fa-user-tie"></i>' : '<i class="fa-solid fa-user"></i>';
  document.getElementById('makeAdminName').textContent = user.name || '—';
  
  const subText = '@' + (user.username || user.user_id || '') + 
    (user.email ? ' • ' + user.email : (user.phone ? ' • ' + user.phone : ''));
  document.getElementById('makeAdminSub').textContent = subText;

  const currentRoleBox = document.getElementById('makeAdminCurrentRoleBox');
  const currentRoleText = document.getElementById('makeAdminCurrentRoleText');
  const btnBottomRevoke = document.getElementById('btnModalRevokeBottom');
  const modalTitle = document.getElementById('makeAdminModalTitle');
  const submitBtnText = document.getElementById('btnMakeAdminSubmitText');

  const roleNamesBn = {
    'SUPERADMIN': 'সুপার অ্যাডমিন',
    'EDITOR': 'এডিটর',
    'MODERATOR': 'মডারেটর',
    'VIEWER': 'ভিউয়ার'
  };
  const roleNamesEn = {
    'SUPERADMIN': 'Super Admin',
    'EDITOR': 'Editor',
    'MODERATOR': 'Moderator',
    'VIEWER': 'Viewer'
  };
  const isEnMode = <?php echo isEn() ? 'true' : 'false'; ?>;
  const roleNames = isEnMode ? roleNamesEn : roleNamesBn;

  if (user.admin_role) {
    currentRoleBox.style.display = 'block';
    btnBottomRevoke.style.display = 'inline-flex';
    currentRoleText.textContent = roleNames[user.admin_role] || user.admin_role;
    document.getElementById('makeAdminRole').value = user.admin_role;
    modalTitle.textContent = '<?php echo __('অ্যাডমিন দায়িত্ব পরিবর্তন বা বাতিল', 'Manage or Revoke Admin Role'); ?>';
    submitBtnText.textContent = '<?php echo __('দায়িত্ব ও রোল পরিবর্তন করুন', 'Update Admin Role'); ?>';
  } else {
    currentRoleBox.style.display = 'none';
    btnBottomRevoke.style.display = 'none';
    document.getElementById('makeAdminRole').value = 'MODERATOR';
    modalTitle.textContent = '<?php echo __('অ্যাডমিন এক্সেস প্রদান', 'Grant Admin Access'); ?>';
    submitBtnText.textContent = '<?php echo __('অ্যাডমিন এক্সেস নিশ্চিত করুন', 'Confirm Admin Access'); ?>';
  }
  
  openModal('makeAdminModal');
}

function revokeAdminRoleSubmit() {
  if (!currentUserObj || !currentUserObj.user_id) return;
  const confirmMsg = '<?php echo __('আপনি কি নিশ্চিত যে এই ব্যবহারকারীর অ্যাডমিন বা মডারেটর এক্সেস বাতিল করে সাধারণ ইউজার বানাতে চান?', 'Are you sure you want to revoke this user\'s admin privileges and demote them to a regular user?'); ?>';
  if (confirm(confirmMsg)) {
    document.getElementById('makeAdminAction').value = 'revoke_admin_role';
    document.getElementById('makeAdminForm').submit();
  }
}

function toggleCurrentUserBan() {
  if (currentUserObj && currentUserObj.user_id) {
    const isBanned = (parseInt(currentUserObj.is_banned) === 1);
    const msg = isBanned ? '<?php echo __('আপনি কি এই ব্যবহারকারীকে আনব্যান করতে চান?', 'Do you want to unban this user?'); ?>' : '<?php echo __('আপনি কি নিশ্চিতভাবে এই ব্যবহারকারীকে ব্যান / ব্লক করতে চান?', 'Are you sure you want to ban this user?'); ?>';
    if (confirm(msg)) {
      window.location.href = 'users.php?toggle_ban=' + encodeURIComponent(currentUserObj.user_id);
    }
  }
}

function openEditUserModal(user) {
  currentUserObj = user;
  document.getElementById('editUserUid').value = user.user_id || '';
  document.getElementById('editUidDisplay').textContent = user.user_id || '';
  document.getElementById('editUserName').value = user.name || '';
  document.getElementById('editUserUsername').value = user.username || '';
  document.getElementById('editUserPhone').value = user.phone || '';
  document.getElementById('editUserEmail').value = user.email || '';
  document.getElementById('editUserBlood').value = user.blood_group || '';
  document.getElementById('editUserDistrict').value = user.district || 'ঢাকা';
  const isFemale = (user.gender === 'female' || user.avatar === 'avatar_2');
  document.getElementById('editUserGender').value = isFemale ? 'female' : 'male';
  document.getElementById('editUserBanned').checked = (parseInt(user.is_banned) === 1);
  const passInp = document.getElementById('editUserNewPassword');
  if (passInp) passInp.value = '';
  switchUserEditTab('personal');
  openModal('editUserModal');
}

function openResetPasswordModal(uid, name) {
  document.getElementById('resetPassUid').value = uid;
  document.getElementById('resetPassUserDesc').textContent = '<?php echo __('ইউজার: ', 'User: '); ?>' + name + ' (' + uid + ')';
  openModal('resetPasswordModal');
}

function openImportModal() {
  openModal('importModal');
}
</script>

<!-- Modal: Import Users CSV -->
<div class="modal-backdrop" id="importModal">
  <div class="modal-window" style="max-width: 500px;">
    <div class="modal-header">
      <div class="modal-title"><i class="fa-solid fa-file-import mr-2"></i><?php echo __('CSV ইমপোর্ট', 'Import CSV'); ?></div>
      <button type="button" class="btn-modal-close" onclick="closeModal('importModal')">&times;</button>
    </div>
    <form method="POST" action="users.php" enctype="multipart/form-data">
      <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
      <input type="hidden" name="action" value="import_csv">

      <div class="modal-body">
        <p style="font-size: 13px; color: var(--text-muted); margin-top: 0;">
          <?php echo __('কলামসমূহ: User ID, Name, Username, Email, Phone, Blood Group, Gender, District', 'Columns: User ID, Name, Username, Email, Phone, Blood Group, Gender, District'); ?>
        </p>
        <div class="form-group">
          <label class="form-label"><?php echo __('CSV ফাইল', 'CSV File'); ?> *</label>
          <input type="file" name="csv_file" class="form-control" accept=".csv" required>
        </div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" onclick="closeModal('importModal')"><?php echo __('বাতিল', 'Cancel'); ?></button>
        <button type="submit" class="btn btn-primary"><i class="fa-solid fa-cloud-arrow-up mr-1"></i><?php echo __('ইমপোর্ট', 'Import'); ?></button>
      </div>
    </form>
  </div>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
