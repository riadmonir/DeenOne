<?php
/**
 * ==============================================================================
 * DEEN ONE ADMIN - GALLERY & MEDIA MANAGER (গ্যালারি ও ইমেজ অপ্টিমাইজেশন)
 * High-speed auto WebP conversion, 80%+ compression, edit, and asset management
 * ==============================================================================
 */
require_once __DIR__ . '/auth.php';
require_once __DIR__ . '/image_helper.php';
requireAdminLogin();
$pdo = getDbConnection();

// Auto-create gallery_images table if not exists
try {
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS `gallery_images` (
          `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
          `image_uid` VARCHAR(60) NOT NULL UNIQUE,
          `title` VARCHAR(200) NOT NULL,
          `category` VARCHAR(100) DEFAULT 'সাধারণ',
          `original_name` VARCHAR(255) DEFAULT NULL,
          `file_name` VARCHAR(255) NOT NULL,
          `file_path` VARCHAR(255) NOT NULL,
          `file_url` VARCHAR(255) NOT NULL,
          `original_size` BIGINT UNSIGNED DEFAULT 0,
          `compressed_size` BIGINT UNSIGNED DEFAULT 0,
          `saved_percent` DECIMAL(5,2) DEFAULT 0.00,
          `width` INT UNSIGNED DEFAULT 0,
          `height` INT UNSIGNED DEFAULT 0,
          `mime_type` VARCHAR(50) DEFAULT 'image/webp',
          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          INDEX `idx_gallery_cat` (`category`),
          INDEX `idx_gallery_created` (`created_at`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    ");
} catch (Exception $e) {}

// Ensure upload directory exists
$uploadDir = dirname(__DIR__) . '/uploads/gallery';
if (!is_dir($uploadDir)) {
    @mkdir($uploadDir, 0755, true);
}

/**
 * Auto-syncs all image files across all uploads/ subfolders into gallery_images table
 */
function syncAllUploadsToGallery($pdo) {
    $baseDir = dirname(__DIR__);
    $uploadsDir = $baseDir . '/uploads';
    if (!is_dir($uploadsDir)) return 0;

    $namazTitles = [
        'male' => [
            1 => 'কিয়াম ও নিয়ত',
            2 => 'তাকবীরে তাহরীমা',
            3 => 'হাত বাঁধা ও ছানা পাঠ',
            4 => 'রুকু',
            5 => 'কওমা - রুকু থেকে উঠে দাঁড়ানো',
            6 => 'প্রথম সিজদাহ',
            7 => 'দুই সিজদার মাঝের বৈঠক',
            8 => 'দ্বিতীয় সিজদাহ ও দ্বিতীয় রাকাত',
            9 => 'তাশাহহুদ ও শেষ বৈঠক',
            10 => 'সালাম ফিরানো',
            11 => 'সালাত পরবর্তী দোয়া ও মোনাজাত'
        ],
        'female' => [
            1 => 'কিয়াম ও নিয়ত',
            2 => 'তাকবীরে তাহরীমা',
            3 => 'বুকের উপর হাত বাঁধা ও ছানা',
            4 => 'রুকু',
            5 => 'কওমা - রুকু থেকে উঠে দাঁড়ানো',
            6 => 'প্রথম সিজদাহ',
            7 => 'দুই সিজদার মাঝের বৈঠক',
            8 => 'দ্বিতীয় সিজদাহ ও পরবর্তী রাকাত',
            9 => 'তাশাহহুদ ও শেষ বৈঠক',
            10 => 'সালাম ফিরানো'
        ]
    ];

    $dirIterator = new RecursiveDirectoryIterator($uploadsDir, RecursiveDirectoryIterator::SKIP_DOTS);
    $iterator = new RecursiveIteratorIterator($dirIterator);

    $synced = 0;
    foreach ($iterator as $file) {
        if (!$file->isFile()) continue;
        $ext = strtolower($file->getExtension());
        if (!in_array($ext, ['jpg', 'jpeg', 'png', 'webp', 'gif'])) continue;

        $fullPath = str_replace('\\', '/', $file->getPathname());
        $relUrl = ltrim(str_replace(str_replace('\\', '/', $baseDir), '', $fullPath), '/');

        // Check if already registered
        $stmt = $pdo->prepare("SELECT id FROM gallery_images WHERE file_url = ? OR file_path = ?");
        $stmt->execute([$relUrl, $fullPath]);
        if ($stmt->fetch()) continue;

        // Categorize based on folder
        $cat = 'সাধারণ';
        $title = pathinfo($file->getFilename(), PATHINFO_FILENAME);

        if (strpos($relUrl, 'namaz_learning/male') !== false) {
            $cat = 'নামাজ শিক্ষা (পুরুষ)';
            preg_match('/step_(\d+)/i', $file->getFilename(), $m);
            $stepNum = isset($m[1]) ? (int)$m[1] : 0;
            $stepName = $namazTitles['male'][$stepNum] ?? "ধাপ $stepNum";
            $title = "পুরুষের নামাজ - ধাপ $stepNum: $stepName";
        } elseif (strpos($relUrl, 'namaz_learning/female') !== false) {
            $cat = 'নামাজ শিক্ষা (মহিলা)';
            preg_match('/step_(\d+)/i', $file->getFilename(), $m);
            $stepNum = isset($m[1]) ? (int)$m[1] : 0;
            $stepName = $namazTitles['female'][$stepNum] ?? "ধাপ $stepNum";
            $title = "মহিলাদের নামাজ - ধাপ $stepNum: $stepName";
        } elseif (strpos($relUrl, 'halal_foods') !== false) {
            $cat = 'হালাল ফুড';
            $title = 'হালাল খাদ্য উপাদান';
        } elseif (strpos($relUrl, 'books') !== false) {
            $cat = 'বইয়ের প্রচ্ছদ';
            $title = 'ইসলামিক বই প্রচ্ছদ';
        } elseif (strpos($relUrl, 'avatars') !== false) {
            $cat = 'প্রোফাইল অবতার';
            $title = 'ইউজার অবতার';
        } elseif (strpos($relUrl, 'hajj_journey') !== false) {
            $cat = 'হজ যাত্রা';
            preg_match('/stage_(\d+)/i', $file->getFilename(), $m);
            $stageNum = isset($m[1]) ? (int)$m[1] : 0;
            $hajjTitles = [
                1 => 'ইহরাম', 2 => 'তাওয়াফুল কুদুম', 3 => 'সাফা ও মারওয়া',
                4 => 'মিনা', 5 => 'আরাফাত', 6 => 'মুজদালিফা',
                7 => 'জামরাত আল-আকাবা', 8 => 'আদহি (কোরবানি)', 9 => 'চুল কাটা বা কামানো',
                10 => 'তাওয়াফুল ইফাদাহ', 11 => 'জামরাতে পাথর নিক্ষেপ', 12 => 'বিদায়ী তাওয়াফ'
            ];
            $stageName = $hajjTitles[$stageNum] ?? "ধাপ $stageNum";
            $title = "হজ যাত্রা - ধাপ $stageNum: $stageName";
        } elseif (strpos($relUrl, 'gallery') !== false) {
            $cat = 'অ্যাপ ব্যানার';
        }

        $size = $file->getSize();
        $imgInfo = @getimagesize($fullPath);
        $w = $imgInfo[0] ?? 0;
        $h = $imgInfo[1] ?? 0;
        $mime = $imgInfo['mime'] ?? ('image/' . $ext);
        $uid = 'IMG_' . strtoupper(bin2hex(random_bytes(5)));

        try {
            $insStmt = $pdo->prepare("
                INSERT INTO gallery_images
                (image_uid, title, category, original_name, file_name, file_path, file_url, original_size, compressed_size, saved_percent, width, height, mime_type)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0.00, ?, ?, ?)
            ");
            $insStmt->execute([
                $uid,
                $title,
                $cat,
                $file->getFilename(),
                $file->getFilename(),
                $fullPath,
                $relUrl,
                $size,
                $size,
                $w,
                $h,
                $mime
            ]);
            $synced++;
        } catch (Exception $e) {}
    }
    return $synced;
}

// Auto-run initial sync if fewer than 5 items
try {
    $existingCount = (int)$pdo->query("SELECT COUNT(*) FROM gallery_images")->fetchColumn();
    if ($existingCount < 5) {
        syncAllUploadsToGallery($pdo);
    }
} catch (Exception $e) {}

// Handle Form Actions (Upload, Edit, Optimize, Sync)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? '';
    $csrfToken = $_POST['csrf_token'] ?? '';
    $isAjax = !empty($_POST['is_ajax']) || (!empty($_SERVER['HTTP_X_REQUESTED_WITH']) && strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) === 'xmlhttprequest');

    if (!verifyCsrfToken($csrfToken)) {
        if ($isAjax) {
            header('Content-Type: application/json; charset=UTF-8');
            echo json_encode(['success' => false, 'error' => __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.')]);
            exit();
        }
        setFlash('danger', __('নিরাপত্তা টোকেন অকার্যকর।', 'Invalid security token.'));
        redirect('gallery.php');
    }

    // 0. Sync Folders Action
    if ($action === 'sync_folders') {
        $count = syncAllUploadsToGallery($pdo);
        logAdminAction($pdo, 'SYNC_MEDIA', 'gallery_images', (string)$count, 'সকল মিডিয়া ফোল্ডার সিঙ্ক সম্পন্ন: ' . $count . 'টি নতুন ফাইল পাওয়া গেছে');
        setFlash('success', __('সকল মিডিয়া ফোল্ডার সফলভাবে সিঙ্ক হয়েছে! নতুন ছবি পাওয়া গেছে: ', 'All media folders synced successfully! New images found: ') . toLangNum($count));
        redirect('gallery.php');
    }

    // 1. Upload Action
    if ($action === 'upload') {
        $title = trim($_POST['title'] ?? '');
        $category = trim($_POST['category'] ?? 'সাধারণ');
        if (empty($category)) $category = 'সাধারণ';

        $imageUrl = trim($_POST['image_url'] ?? '');
        $uploadedFiles = $_FILES['images'] ?? null;
        $successCount = 0;
        $errorMessages = [];

        // 1.1 Process from Image URL if provided
        if (!empty($imageUrl)) {
            if (!filter_var($imageUrl, FILTER_VALIDATE_URL)) {
                $errorMessages[] = 'অবৈধ ইমেজ URL প্রদান করা হয়েছে।';
            } else {
                $tmpDownload = tempnam(sys_get_temp_dir(), 'deen_img_');
                $ch = curl_init($imageUrl);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
                curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                curl_setopt($ch, CURLOPT_TIMEOUT, 25);
                curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOne-Admin/1.0');
                $imgData = curl_exec($ch);
                $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                curl_close($ch);

                if ($imgData !== false && $httpCode >= 200 && $httpCode < 300 && strlen($imgData) > 0) {
                    file_put_contents($tmpDownload, $imgData);

                    $origName = basename(parse_url($imageUrl, PHP_URL_PATH) ?: 'image_url.jpg');
                    $origExt = strtolower(pathinfo($origName, PATHINFO_EXTENSION));
                    if (!in_array($origExt, ['jpg', 'jpeg', 'png', 'webp', 'gif'])) {
                        $origExt = 'jpg';
                    }
                    if ($origExt === 'jpeg') $origExt = 'jpg';

                    $uid = 'IMG_' . strtoupper(bin2hex(random_bytes(5)));
                    $fileName = 'gallery_' . time() . '_' . strtolower(bin2hex(random_bytes(3))) . '.' . $origExt;
                    $destPath = $uploadDir . '/' . $fileName;
                    $publicUrl = 'uploads/gallery/' . $fileName;

                    $itemTitle = !empty($title) ? $title : pathinfo($origName, PATHINFO_FILENAME);
                    if (empty($itemTitle) || $itemTitle === 'image_url') {
                        $itemTitle = 'ইমেজ ' . date('d M Y H:i');
                    }

                    @rename($tmpDownload, $destPath);
                    if (!file_exists($destPath)) {
                        @copy($tmpDownload, $destPath);
                        @unlink($tmpDownload);
                    }

                    $optResult = optimizeImageInPlace($destPath, ['max_dimension' => 1920, 'jpeg_quality' => 80]);
                    $mimeType = $optResult['mime_type'] ?? 'image/' . ($origExt === 'jpg' ? 'jpeg' : $origExt);

                    if ($optResult['success']) {
                        try {
                            $stmt = $pdo->prepare("
                                INSERT INTO gallery_images 
                                (image_uid, title, category, original_name, file_name, file_path, file_url, original_size, compressed_size, saved_percent, width, height, mime_type) 
                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            ");
                            $stmt->execute([
                                $uid,
                                $itemTitle,
                                $category,
                                $origName,
                                $fileName,
                                $destPath,
                                $publicUrl,
                                $optResult['original_size'],
                                $optResult['compressed_size'],
                                $optResult['saved_percent'],
                                $optResult['width'],
                                $optResult['height'],
                                $mimeType
                            ]);
                            $successCount++;
                        } catch (Exception $dbEx) {
                            $errorMessages[] = 'ডাটাবেজ ত্রুটি: ' . $dbEx->getMessage();
                        }
                    } else {
                        $errorMessages[] = ($optResult['error'] ?? 'ইমেজ অপ্টিমাইজেশন ব্যর্থ');
                    }
                } else {
                    $errorMessages[] = 'URL থেকে ছবি ডাউনলোড করতে ব্যর্থ হয়েছে (HTTP কোড: ' . $httpCode . ')';
                }
            }
        }

        // 1.2 Process file uploads if provided
        if (!empty($uploadedFiles['name'])) {
            $isMulti = is_array($uploadedFiles['name']);
            $count = $isMulti ? count($uploadedFiles['name']) : 1;

            for ($i = 0; $i < $count; $i++) {
                $origName = $isMulti ? $uploadedFiles['name'][$i] : $uploadedFiles['name'];
                $tmpName = $isMulti ? $uploadedFiles['tmp_name'][$i] : $uploadedFiles['tmp_name'];
                $error = $isMulti ? $uploadedFiles['error'][$i] : $uploadedFiles['error'];

                if ($error !== UPLOAD_ERR_OK || empty($tmpName) || !file_exists($tmpName)) {
                    continue;
                }

                $origExt = strtolower(pathinfo($origName, PATHINFO_EXTENSION));
                if (!in_array($origExt, ['jpg', 'jpeg', 'png', 'webp', 'gif'])) {
                    $origExt = 'jpg';
                }
                if ($origExt === 'jpeg') $origExt = 'jpg';

                $uid = 'IMG_' . strtoupper(bin2hex(random_bytes(5)));
                $fileName = 'gallery_' . time() . '_' . strtolower(bin2hex(random_bytes(3))) . '.' . $origExt;
                $destPath = $uploadDir . '/' . $fileName;
                $publicUrl = 'uploads/gallery/' . $fileName;

                $itemTitle = !empty($title) ? ($count > 1 ? "$title - " . ($i + 1) : $title) : pathinfo($origName, PATHINFO_FILENAME);

                if (!move_uploaded_file($tmpName, $destPath)) {
                    continue;
                }

                $optResult = optimizeImageInPlace($destPath, ['max_dimension' => 1920, 'jpeg_quality' => 80]);
                $mimeType = $optResult['mime_type'] ?? 'image/' . ($origExt === 'jpg' ? 'jpeg' : $origExt);

                if ($optResult['success']) {
                    try {
                        $stmt = $pdo->prepare("
                            INSERT INTO gallery_images 
                            (image_uid, title, category, original_name, file_name, file_path, file_url, original_size, compressed_size, saved_percent, width, height, mime_type) 
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ");
                        $stmt->execute([
                            $uid,
                            $itemTitle,
                            $category,
                            $origName,
                            $fileName,
                            $destPath,
                            $publicUrl,
                            $optResult['original_size'],
                            $optResult['compressed_size'],
                            $optResult['saved_percent'],
                            $optResult['width'],
                            $optResult['height'],
                            $mimeType
                        ]);
                        $successCount++;
                    } catch (Exception $dbEx) {
                        $errorMessages[] = 'ডাটাবেজ ত্রুটি: ' . $dbEx->getMessage();
                    }
                } else {
                    $errorMessages[] = ($optResult['error'] ?? 'ইমেজ অপ্টিমাইজেশন ব্যর্থ');
                }
            }
        }

        if ($successCount > 0) {
            logAdminAction($pdo, 'UPLOAD_GALLERY', 'gallery_images', (string)$successCount, $successCount . 'টি ছবি ইন-প্লেস অপ্টিমাইজেশনের মাধ্যমে সংরক্ষিত হয়েছে');
            setFlash('success', __($successCount . 'টি ছবি সফলভাবে অপ্টিমাইজ ও আপলোড সম্পন্ন হয়েছে!', $successCount . ' images optimized & uploaded successfully!'));
        } else {
            $msg = !empty($errorMessages) ? implode(', ', $errorMessages) : 'কোনো ছবি আপলোড করা সম্ভব হয়নি।';
            setFlash('danger', __('ত্রুটি: ', 'Error: ') . $msg);
        }
    }

    // 2. Edit Action (Title & Category/Tag)
    elseif ($action === 'edit') {
        $id = (int)($_POST['id'] ?? 0);
        $title = trim($_POST['title'] ?? '');
        $category = trim($_POST['category'] ?? 'সাধারণ');
        if (empty($category)) $category = 'সাধারণ';

        if ($id <= 0) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => __('অবৈধ ছবির আইডি।', 'Invalid image ID.')]);
                exit();
            }
            setFlash('danger', __('অবৈধ ছবির আইডি।', 'Invalid image ID.'));
            redirect('gallery.php');
        }

        if (empty($title)) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => __('ছবির শিরোনাম খালি রাখা যাবে না।', 'Image title cannot be empty.')]);
                exit();
            }
            setFlash('danger', __('ছবির শিরোনাম খালি রাখা যাবে না।', 'Image title cannot be empty.'));
            redirect('gallery.php');
        }

        try {
            $stmt = $pdo->prepare("UPDATE gallery_images SET title = ?, category = ? WHERE id = ?");
            $stmt->execute([$title, $category, $id]);

            logAdminAction($pdo, 'UPDATE_GALLERY', 'gallery_images', (string)$id, 'ছবির শিরোনাম ও ক্যাটাগরি আপডেট: ' . $title . ' [' . $category . ']');
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode([
                    'success' => true,
                    'message' => __('ছবির শিরোনাম ও ক্যাটাগরি সফলভাবে আপডেট করা হয়েছে।', 'Image title and category updated successfully.'),
                    'id' => $id,
                    'title' => $title,
                    'category' => $category
                ]);
                exit();
            }
            setFlash('success', __('ছবির শিরোনাম ও ক্যাটাগরি সফলভাবে আপডেট করা হয়েছে।', 'Image title and category updated successfully.'));
        } catch (Exception $e) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => $e->getMessage()]);
                exit();
            }
            setFlash('danger', __('আপডেট করতে ব্যর্থ: ', 'Update failed: ') . $e->getMessage());
        }
    }

    // 2.5 Delete Action via POST / AJAX
    elseif ($action === 'delete') {
        $id = (int)($_POST['id'] ?? 0);
        if ($id <= 0) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => __('অবৈধ ছবির আইডি।', 'Invalid image ID.')]);
                exit();
            }
            setFlash('danger', __('অবৈধ ছবির আইডি।', 'Invalid image ID.'));
            redirect('gallery.php');
        }

        try {
            $stmt = $pdo->prepare("SELECT file_path, file_url, title FROM gallery_images WHERE id = ?");
            $stmt->execute([$id]);
            $img = $stmt->fetch();

            if ($img) {
                if (!empty($img['file_path']) && file_exists($img['file_path'])) {
                    @unlink($img['file_path']);
                } elseif (!empty($img['file_url'])) {
                    $relPath = dirname(__DIR__) . '/' . ltrim($img['file_url'], '/');
                    if (file_exists($relPath)) {
                        @unlink($relPath);
                    }
                }
                $delStmt = $pdo->prepare("DELETE FROM gallery_images WHERE id = ?");
                $delStmt->execute([$id]);
                logAdminAction($pdo, 'DELETE_GALLERY', 'gallery_images', (string)$id, 'ছবি মুছে ফেলা হয়েছে: ' . $img['title']);
                
                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode(['success' => true, 'message' => __('ছবি সফলভাবে মুছে ফেলা হয়েছে।', 'Image deleted successfully.'), 'id' => $id]);
                    exit();
                }
                setFlash('success', __('ছবি সফলভাবে মুছে ফেলা হয়েছে।', 'Image deleted successfully.'));
            } else {
                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode(['success' => false, 'error' => __('ছবিটি ডাটাবেজে পাওয়া যায়নি।', 'Image not found.')]);
                    exit();
                }
                setFlash('danger', __('ছবিটি পাওয়া যায়নি।', 'Image not found.'));
            }
        } catch (Exception $e) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => $e->getMessage()]);
                exit();
            }
            setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
        }
    }

    // 3. Manual Optimize Action (WebP Conversion & Re-compression)
    elseif ($action === 'optimize') {
        $id = (int)($_POST['id'] ?? 0);
        if ($id <= 0) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => __('অবৈধ ছবির আইডি।', 'Invalid image ID.')]);
                exit();
            }
            setFlash('danger', __('অবৈধ ছবির আইডি।', 'Invalid image ID.'));
            redirect('gallery.php');
        }

        try {
            $stmt = $pdo->prepare("SELECT * FROM gallery_images WHERE id = ?");
            $stmt->execute([$id]);
            $img = $stmt->fetch();

            if (!$img) {
                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode(['success' => false, 'error' => __('ছবিটি পাওয়া যায়নি।', 'Image not found.')]);
                    exit();
                }
                setFlash('danger', __('ছবিটি পাওয়া যায়নি।', 'Image not found.'));
                redirect('gallery.php');
            }

            // Resolve source file
            $sourceFile = $img['file_path'];
            $tempCreated = false;

            if (empty($sourceFile) || !file_exists($sourceFile)) {
                // Try resolving relative path from file_url
                $relPath = dirname(__DIR__) . '/' . ltrim($img['file_url'], '/');
                if (file_exists($relPath)) {
                    $sourceFile = $relPath;
                } elseif (preg_match('/^https?:\/\//i', $img['file_url'])) {
                    // Remote download
                    $tmpDownload = tempnam(sys_get_temp_dir(), 'deen_opt_');
                    $ch = curl_init($img['file_url']);
                    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
                    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                    curl_setopt($ch, CURLOPT_TIMEOUT, 25);
                    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOne-Admin/1.0');
                    $imgData = curl_exec($ch);
                    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                    curl_close($ch);

                    if ($imgData !== false && $httpCode >= 200 && $httpCode < 300) {
                        file_put_contents($tmpDownload, $imgData);
                        $sourceFile = $tmpDownload;
                        $tempCreated = true;
                    }
                }
            }

            if (empty($sourceFile) || !file_exists($sourceFile)) {
                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode(['success' => false, 'error' => __('মূল ইমেজ ফাইল পাওয়া যায়নি বা অ্যাক্সেস করা সম্ভব হচ্ছে না।', 'Source image file not found or inaccessible.')]);
                    exit();
                }
                setFlash('danger', __('মূল ইমেজ ফাইল পাওয়া যায়নি বা অ্যাক্সেস করা সম্ভব হচ্ছে না।', 'Source image file not found or inaccessible.'));
                redirect('gallery.php');
            }

            // 3.1 Perform IN-PLACE Optimization (ZERO URL / Filename Mutation)
            $optResult = optimizeImageInPlace($sourceFile, [
                'max_dimension' => 1920,
                'jpeg_quality'  => 80,
                'png_level'     => 8,
                'webp_quality'  => 80
            ]);

            if ($tempCreated && file_exists($sourceFile) && realpath($sourceFile) !== realpath($img['file_path'])) {
                @unlink($sourceFile);
            }

            if ($optResult['success']) {
                $origSize = ($img['original_size'] > 0) ? max((int)$img['original_size'], (int)$optResult['original_size']) : $optResult['original_size'];
                $compressedSize = $optResult['compressed_size'];
                $savedBytes = max(0, $origSize - $compressedSize);
                $savedPercent = ($origSize > 0) ? round(($savedBytes / $origSize) * 100, 1) : 0;
                $mimeType = $optResult['mime_type'] ?? ($img['mime_type'] ?? 'image/jpeg');

                // IMMUTABLE URL POLICY: file_name, file_path, and file_url are preserved 100% untouched!
                $upStmt = $pdo->prepare("
                    UPDATE gallery_images 
                    SET original_size = ?, compressed_size = ?, saved_percent = ?, width = ?, height = ?, mime_type = ?
                    WHERE id = ?
                ");
                $upStmt->execute([
                    $origSize,
                    $compressedSize,
                    $savedPercent,
                    $optResult['width'],
                    $optResult['height'],
                    $mimeType,
                    $id
                ]);

                logAdminAction($pdo, 'OPTIMIZE_GALLERY', 'gallery_images', (string)$id, 'ছবি ইন-প্লেস অপ্টিমাইজ সম্পন্ন: ' . $img['title'] . ' (নতুন সাইজ: ' . formatBytes($compressedSize) . ', সেভ: ' . $savedPercent . '%)');

                $respMsg = !empty($optResult['already_optimized'])
                    ? __('ছবিটি ইতিমধ্যে সর্বোচ্চ অপ্টিমাইজড অবস্থায় রয়েছে!', 'Image is already fully optimized!')
                    : __('ছবিটি সফলভাবে একই URL-এ ইন-প্লেস অপ্টিমাইজ সম্পন্ন হয়েছে!', 'Image optimized in-place successfully with URL preserved!');

                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode([
                        'success' => true,
                        'message' => $respMsg,
                        'id' => $id,
                        'compressed_size' => formatBytes($compressedSize),
                        'saved_percent' => toLangNum($savedPercent),
                        'dimensions' => $optResult['width'] . '×' . $optResult['height']
                    ]);
                    exit();
                }

                setFlash('success', $respMsg . ' ' . __('সাইজ: ', 'Size: ') . formatBytes($compressedSize) . ' (' . __('সেভ: ', 'Saved: ') . toLangNum($savedPercent) . '%)');
            } else {
                if ($isAjax) {
                    header('Content-Type: application/json; charset=UTF-8');
                    echo json_encode(['success' => false, 'error' => ($optResult['error'] ?? 'অপ্টিমাইজেশন ব্যর্থ হয়েছে।')]);
                    exit();
                }
                setFlash('danger', __('অপ্টিমাইজেশন ব্যর্থ: ', 'Optimization failed: ') . ($optResult['error'] ?? 'অজানা সমস্যা'));
            }
        } catch (Exception $e) {
            if ($isAjax) {
                header('Content-Type: application/json; charset=UTF-8');
                echo json_encode(['success' => false, 'error' => $e->getMessage()]);
                exit();
            }
            setFlash('danger', __('ত্রুটি: ', 'Error: ') . $e->getMessage());
        }
    }

    redirect('gallery.php');
}

// Handle Delete Action
if (isset($_GET['delete'])) {
    $id = (int)$_GET['delete'];
    try {
        $stmt = $pdo->prepare("SELECT file_path, title FROM gallery_images WHERE id = ?");
        $stmt->execute([$id]);
        $img = $stmt->fetch();

        if ($img) {
            if (!empty($img['file_path']) && file_exists($img['file_path'])) {
                @unlink($img['file_path']);
            }
            $delStmt = $pdo->prepare("DELETE FROM gallery_images WHERE id = ?");
            $delStmt->execute([$id]);
            logAdminAction($pdo, 'DELETE_GALLERY', 'gallery_images', (string)$id, 'ছবি মুছে ফেলা হয়েছে: ' . $img['title']);
            setFlash('success', __('ছবি সফলভাবে মুছে ফেলা হয়েছে।', 'Image deleted successfully.'));
        }
    } catch (Exception $e) {
        setFlash('danger', __('মুছতে ব্যর্থ: ', 'Delete failed: ') . $e->getMessage());
    }
    redirect('gallery.php');
}

// Filters & Query
$selectedCat = trim($_GET['category'] ?? '');
$searchQuery = trim($_GET['q'] ?? '');

$where = [];
$params = [];

if (!empty($selectedCat) && $selectedCat !== 'all' && $selectedCat !== 'সকল') {
    $where[] = "category = ?";
    $params[] = $selectedCat;
}

if (!empty($searchQuery)) {
    $where[] = "(title LIKE ? OR original_name LIKE ? OR category LIKE ?)";
    $term = "%$searchQuery%";
    $params = array_merge($params, [$term, $term, $term]);
}

$whereSql = !empty($where) ? "WHERE " . implode(" AND ", $where) : "";

// Fetch distinct categories
$distinctCategories = ['অ্যাপ ব্যানার', 'ইসলামিক আর্ট', 'ফিচার আইকন', 'বইয়ের প্রচ্ছদ', 'নোটিশ ছবি', 'সাধারণ'];
try {
    $catStmt = $pdo->query("SELECT DISTINCT category FROM gallery_images WHERE category IS NOT NULL AND category != ''");
    $dbCats = $catStmt->fetchAll(PDO::FETCH_COLUMN);
    $distinctCategories = array_unique(array_merge($distinctCategories, $dbCats));
} catch (Exception $e) {}

// Stats calculations
$stats = [
    'total_count' => 0,
    'total_orig' => 0,
    'total_comp' => 0,
    'saved_bytes' => 0,
    'saved_percent' => 0
];
try {
    $statRow = $pdo->query("
        SELECT 
            COUNT(*) as total_count,
            COALESCE(SUM(original_size), 0) as total_orig,
            COALESCE(SUM(compressed_size), 0) as total_comp
        FROM gallery_images
    ")->fetch();
    if ($statRow) {
        $stats['total_count'] = (int)$statRow['total_count'];
        $stats['total_orig'] = (float)$statRow['total_orig'];
        $stats['total_comp'] = (float)$statRow['total_comp'];
        $stats['saved_bytes'] = max(0, $stats['total_orig'] - $stats['total_comp']);
        $stats['saved_percent'] = $stats['total_orig'] > 0 ? round(($stats['saved_bytes'] / $stats['total_orig']) * 100, 1) : 0;
    }
} catch (Exception $e) {}

// Fetch Images List
$images = [];
try {
    $stmt = $pdo->prepare("SELECT * FROM gallery_images $whereSql ORDER BY id DESC LIMIT 120");
    $stmt->execute($params);
    $images = $stmt->fetchAll();
} catch (Exception $e) {}

$pageTitle = __('গ্যালারি', 'Gallery');
$activeNav = 'gallery';
require_once __DIR__ . '/header.php';
?>

<!-- Content Header -->
<div class="content-header" style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px; margin-bottom:24px;">
    <div>
        <h1 class="page-title" style="margin:0; font-size:24px; font-weight:700; color:var(--text-heading, #0f172a); display:flex; align-items:center; gap:10px;">
            <i class="fa-solid fa-images text-purple-500"></i><?php echo __('গ্যালারি', 'Gallery'); ?>
        </h1>
    </div>
    <div style="display:flex; align-items:center; gap:12px; flex-wrap:wrap;">
        <form method="POST" action="gallery.php" style="margin:0;">
            <input type="hidden" name="csrf_token" value="<?php echo getCsrfToken(); ?>">
            <input type="hidden" name="action" value="sync_folders">
            <button type="submit" class="btn btn-secondary" style="display:flex; align-items:center; gap:8px; padding:10px 18px; font-weight:600; border-radius:10px;" title="<?php echo __('ফোল্ডার সিঙ্ক', 'Sync Folders'); ?>">
                <i class="fa-solid fa-arrows-rotate"></i><?php echo __('ফোল্ডার সিঙ্ক', 'Sync Folders'); ?>
            </button>
        </form>
        <button type="button" class="btn btn-primary" onclick="openUploadModal()" style="display:flex; align-items:center; gap:8px; padding:10px 20px; font-weight:600; border-radius:10px;">
            <i class="fa-solid fa-cloud-arrow-up"></i><?php echo __('নতুন ছবি', 'Upload Image'); ?>
        </button>
    </div>
</div>

<!-- Stats Banner Cards -->
<div class="stats-grid mb-4" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px;">
    <div class="stat-card card" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px; padding: 18px; display: flex; align-items: center; gap: 16px;">
        <div style="width: 48px; height: 48px; border-radius: 10px; background: rgba(139, 92, 246, 0.12); color: #8b5cf6; display: flex; align-items: center; justify-content: center; font-size: 22px;">
            <i class="fa-solid fa-photo-film"></i>
        </div>
        <div>
            <div style="font-size: 12.5px; color: var(--text-secondary); font-weight: 500;"><?php echo __('মোট ছবি', 'Total Images'); ?></div>
            <div style="font-size: 22px; font-weight: 700; color: var(--text-primary);"><?php echo toLangNum($stats['total_count']); ?></div>
        </div>
    </div>

    <div class="stat-card card" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px; padding: 18px; display: flex; align-items: center; gap: 16px;">
        <div style="width: 48px; height: 48px; border-radius: 10px; background: rgba(16, 185, 129, 0.12); color: #10b981; display: flex; align-items: center; justify-content: center; font-size: 22px;">
            <i class="fa-solid fa-bolt"></i>
        </div>
        <div>
            <div style="font-size: 12.5px; color: var(--text-secondary); font-weight: 500;"><?php echo __('অপ্টিমাইজড সাইজ', 'Optimized Size'); ?></div>
            <div style="font-size: 22px; font-weight: 700; color: #10b981;"><?php echo formatBytes($stats['total_comp']); ?></div>
        </div>
    </div>

    <div class="stat-card card" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px; padding: 18px; display: flex; align-items: center; gap: 16px;">
        <div style="width: 48px; height: 48px; border-radius: 10px; background: rgba(245, 158, 11, 0.12); color: #f59e0b; display: flex; align-items: center; justify-content: center; font-size: 22px;">
            <i class="fa-solid fa-hard-drive"></i>
        </div>
        <div>
            <div style="font-size: 12.5px; color: var(--text-secondary); font-weight: 500;"><?php echo __('স্টোরেজ সেভ', 'Storage Saved'); ?></div>
            <div style="font-size: 22px; font-weight: 700; color: #f59e0b;">
                <?php echo formatBytes($stats['saved_bytes']); ?> 
                <span style="font-size: 13px; font-weight: 600; color: #10b981;">• <?php echo toLangNum($stats['saved_percent']); ?>%</span>
            </div>
        </div>
    </div>
</div>

<!-- Filters Bar -->
<div class="card mb-4 p-3" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px;">
    <form method="GET" action="gallery.php" class="flex-wrap gap-3" style="display:flex; align-items:center;">
        <div style="flex: 1; min-width: 220px;">
            <input type="text" name="q" value="<?php echo htmlspecialchars($searchQuery); ?>" 
                   placeholder="<?php echo __('অনুসন্ধান...', 'Search...'); ?>" 
                   class="form-control">
        </div>
        <div style="min-width: 180px;">
            <select name="category" class="form-control" onchange="this.form.submit()">
                <option value="all"><?php echo __('সকল ক্যাটাগরি', 'All Categories'); ?></option>
                <?php foreach ($distinctCategories as $c): ?>
                    <option value="<?php echo htmlspecialchars($c); ?>" <?php echo $selectedCat === $c ? 'selected' : ''; ?>>
                        <?php echo htmlspecialchars($c); ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>
        <button type="submit" class="btn btn-secondary">
            <i class="fa-solid fa-filter mr-1"></i><?php echo __('ফিল্টার', 'Filter'); ?>
        </button>
        <?php if (!empty($searchQuery) || !empty($selectedCat)): ?>
            <a href="gallery.php" class="btn btn-outline"><?php echo __('রিসেট', 'Reset'); ?></a>
        <?php endif; ?>
    </form>
</div>

<style>
.gallery-grid {
    display: grid;
    grid-template-columns: repeat(4, minmax(0, 1fr));
    gap: 20px;
    width: 100%;
}
@media (max-width: 1280px) {
    .gallery-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
        gap: 16px;
    }
}
@media (max-width: 860px) {
    .gallery-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 14px;
    }
}
@media (max-width: 540px) {
    .gallery-grid {
        grid-template-columns: 1fr;
        gap: 12px;
    }
}
.gallery-item-card {
    background: var(--bg-card);
    border: 1px solid var(--border-card);
    border-radius: 12px;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    box-shadow: 0 1px 3px rgba(0,0,0,0.04);
    transition: transform 0.2s, box-shadow 0.2s;
}
.gallery-item-card:hover {
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

/* SINGLE LINE 4-BUTTON ACTION BAR */
.gallery-card-actions-single-line {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 5px;
    margin-top: 10px;
    align-items: center;
    width: 100%;
}
.gallery-single-btn {
    font-size: 11.5px;
    padding: 7px 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;
    border-radius: 7px;
    font-weight: 600;
    transition: all 0.2s;
    text-decoration: none;
    cursor: pointer;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    min-width: 0;
    box-sizing: border-box;
}
.gallery-single-btn i {
    font-size: 12px;
    flex-shrink: 0;
}
.gallery-single-btn .btn-label {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
@media (max-width: 400px) {
    .gallery-single-btn .btn-label {
        display: none;
    }
    .gallery-single-btn {
        padding: 7px 8px;
    }
}
</style>

<!-- Gallery Grid -->
<?php if (empty($images)): ?>
    <div class="card text-center py-5" style="background: var(--bg-card); border: 1px solid var(--border-card); border-radius: 12px; padding: 50px 20px;">
        <i class="fa-solid fa-images fa-3x mb-3 text-muted" style="color: var(--text-secondary); opacity: 0.5;"></i>
        <h4 style="color: var(--text-primary); margin-bottom: 6px;"><?php echo __('কোনো ছবি পাওয়া যায়নি', 'No Images Found'); ?></h4>
        <p class="text-muted" style="font-size: 13.5px;"><?php echo __('নতুন ছবি আপলোড করতে উপরের বাটনে ক্লিক করুন।', 'Click the button above to upload images.'); ?></p>
    </div>
<?php else: ?>
    <?php 
        $rawBackendBase = dirname(dirname($_SERVER['SCRIPT_NAME'] ?? '/admin/gallery.php'));
        $backendBase = rtrim(str_replace('\\', '/', $rawBackendBase), '/');
        if ($backendBase === '/' || $backendBase === '.' || $backendBase === '') $backendBase = '';
        $protocol = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on') ? 'https' : 'http';
        $host = $_SERVER['HTTP_HOST'] ?? '127.0.0.1:8000';
        $appBaseUrl = $protocol . '://' . $host . ($backendBase !== '' ? $backendBase : '');
    ?>
    <div class="gallery-grid">
        <?php foreach ($images as $img): 
            $isRemote = preg_match('/^https?:\\/\\//i', $img['file_url']);
            $thumbSrc = $isRemote ? $img['file_url'] : ('../' . ltrim($img['file_url'], '/'));
            $fullUrl = $isRemote ? $img['file_url'] : ($appBaseUrl . '/' . ltrim($img['file_url'], '/'));
        ?>
            <div class="gallery-item-card" id="card-img-<?php echo $img['id']; ?>">
                <!-- Thumbnail Preview -->
                <div style="position: relative; width: 100%; height: 180px; background: rgba(0,0,0,0.05); overflow: hidden; cursor: pointer;" onclick="openPreviewLightbox('<?php echo htmlspecialchars($thumbSrc); ?>', '<?php echo htmlspecialchars(addslashes($img['title'])); ?>')">
                    <img id="thumb-img-<?php echo $img['id']; ?>" src="<?php echo htmlspecialchars($thumbSrc); ?>" alt="<?php echo htmlspecialchars($img['title']); ?>" loading="lazy" style="width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s;">
                    <div style="position: absolute; top: 10px; right: 10px; display: flex; gap: 6px;">
                        <span id="badge-percent-<?php echo $img['id']; ?>" class="badge" style="background: rgba(16, 185, 129, 0.9); color: #fff; font-size: 11px; font-weight: 600; padding: 3px 8px; border-radius: 6px; backdrop-filter: blur(4px);">
                            WebP -<?php echo toLangNum($img['saved_percent']); ?>%
                        </span>
                    </div>
                    <div style="position: absolute; bottom: 10px; left: 10px;">
                        <span id="badge-dim-<?php echo $img['id']; ?>" class="badge" style="background: rgba(15, 23, 42, 0.75); color: #fff; font-size: 11px; padding: 2px 7px; border-radius: 5px; backdrop-filter: blur(4px);">
                            <?php echo $img['width'] . '×' . $img['height']; ?>
                        </span>
                    </div>
                </div>

                <!-- Details & Actions -->
                <div style="padding: 14px; display: flex; flex-direction: column; flex: 1; justify-content: space-between;">
                    <div>
                        <div id="title-text-<?php echo $img['id']; ?>" style="font-weight: 600; font-size: 14.5px; color: var(--text-primary); margin-bottom: 4px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="<?php echo htmlspecialchars($img['title']); ?>">
                            <?php echo htmlspecialchars($img['title']); ?>
                        </div>
                        <div style="display: flex; align-items: center; justify-content: space-between; font-size: 12px; color: var(--text-secondary); margin-bottom: 10px;">
                            <span id="cat-text-<?php echo $img['id']; ?>"><i class="fa-solid fa-tag mr-1 text-purple-400"></i><?php echo htmlspecialchars($img['category']); ?></span>
                            <span id="size-text-<?php echo $img['id']; ?>"><?php echo formatBytes($img['compressed_size']); ?></span>
                        </div>
                    </div>

                    <!-- 4 ACTION BUTTONS IN 1 SINGLE ROW -->
                    <div class="gallery-card-actions-single-line">
                        <!-- 1. Copy Link -->
                        <button type="button" class="gallery-single-btn btn btn-outline" onclick="copyDirectUrl('<?php echo htmlspecialchars($fullUrl); ?>', this)" title="<?php echo __('লিংক কপি করুন', 'Copy Link'); ?>">
                            <i class="fa-solid fa-copy"></i><span class="btn-label"><?php echo __('কপি', 'Copy'); ?></span>
                        </button>

                        <!-- 2. Optimize Button -->
                        <button type="button" class="gallery-single-btn btn" style="background: rgba(16, 185, 129, 0.12); color: #10b981; border: 1px solid rgba(16, 185, 129, 0.28);" onclick="triggerOptimize(<?php echo $img['id']; ?>, '<?php echo htmlspecialchars(addslashes($img['title'])); ?>', this)" title="<?php echo __('WebP অপ্টিমাইজ করুন', 'Optimize Image'); ?>">
                            <i class="fa-solid fa-wand-magic-sparkles"></i><span class="btn-label"><?php echo __('অপ্টিমাইজ', 'Optimize'); ?></span>
                        </button>

                        <!-- 3. Edit Button -->
                        <button type="button" class="gallery-single-btn btn btn-primary" style="background: var(--primary, #10b981); color: #fff; border: 1px solid var(--primary, #10b981);" onclick="openEditModal(<?php echo $img['id']; ?>, '<?php echo htmlspecialchars(addslashes($img['title'])); ?>', '<?php echo htmlspecialchars(addslashes($img['category'])); ?>', '<?php echo htmlspecialchars(addslashes($thumbSrc)); ?>', '<?php echo htmlspecialchars($img['width'] . '×' . $img['height']); ?>', '<?php echo htmlspecialchars(formatBytes($img['compressed_size'])); ?>')" title="<?php echo __('এডিট করুন', 'Edit Details'); ?>">
                            <i class="fa-solid fa-pen-to-square"></i><span class="btn-label"><?php echo __('এডিট', 'Edit'); ?></span>
                        </button>

                        <!-- 4. Delete Button -->
                        <button type="button" class="gallery-single-btn btn btn-danger" onclick="triggerDelete(<?php echo $img['id']; ?>, '<?php echo htmlspecialchars(addslashes($img['title'])); ?>', this)" title="<?php echo __('মুছে ফেলুন', 'Delete'); ?>">
                            <i class="fa-solid fa-trash"></i><span class="btn-label"><?php echo __('মুছুন', 'Delete'); ?></span>
                        </button>
                    </div>
                </div>
            </div>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<!-- Hidden Optimize Form Fallback -->
<form id="optimizeForm" method="POST" action="gallery.php" style="display: none;">
    <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
    <input type="hidden" name="action" value="optimize">
    <input type="hidden" name="id" id="optimizeImageId" value="0">
</form>

<!-- Edit Modal -->
<div id="editModal" class="modal" style="display:none; position: fixed; z-index: 99999; left: 0; top: 0; width: 100%; height: 100%; overflow-x: hidden; overflow-y: auto; background-color: rgba(15,23,42,0.65); backdrop-filter: blur(4px); -webkit-backdrop-filter: blur(4px); justify-content: center; align-items: center; padding: 20px;" onclick="if(event.target===this)closeEditModal()">
    <div class="modal-content" style="background: var(--bg-card); border-radius: 16px; width: 100%; max-width: 540px; max-height: 90vh; display: flex; flex-direction: column; border: 1px solid var(--border-color); box-shadow: 0 25px 50px -12px rgba(0,0,0,0.35); overflow: hidden; margin: auto;" onclick="event.stopPropagation()">
        
        <!-- Modal Header -->
        <div class="modal-header" style="display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--border-color); flex-shrink: 0; background: var(--bg-card);">
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--text-heading); display: flex; align-items: center; gap: 10px;">
                <i class="fa-solid fa-pen-to-square text-purple-500"></i><?php echo __('ছবি সম্পাদনা', 'Edit Image'); ?>
            </h3>
            <button type="button" class="modal-close-btn" onclick="closeEditModal()" title="<?php echo __('বন্ধ করুন', 'Close'); ?>" style="background: rgba(0,0,0,0.05); border: 1px solid var(--border-color); width: 34px; height: 34px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 20px; color: var(--text-muted); cursor: pointer; line-height: 1; transition: all 0.2s;">&times;</button>
        </div>

        <!-- Modal Body -->
        <div class="modal-body" style="padding: 22px 24px; overflow-y: auto; flex: 1 1 auto; max-height: calc(90vh - 140px);">
            <!-- Image Info Preview Box -->
            <div style="display: flex; gap: 14px; align-items: center; background: rgba(139, 92, 246, 0.05); border: 1px solid rgba(139, 92, 246, 0.15); padding: 12px; border-radius: 10px; margin-bottom: 18px;">
                <div style="width: 70px; height: 70px; border-radius: 8px; overflow: hidden; background: #000; flex-shrink: 0;">
                    <img id="editThumbPreview" src="" alt="Thumbnail" style="width: 100%; height: 100%; object-fit: cover;">
                </div>
                <div style="font-size: 13px; color: var(--text-secondary);">
                    <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 2px;" id="editInfoName">--</div>
                    <div><i class="fa-solid fa-expand mr-1 text-purple-400"></i> <span id="editInfoDim">--</span></div>
                    <div><i class="fa-solid fa-file mr-1 text-purple-400"></i> <span id="editInfoSize">--</span></div>
                </div>
            </div>

            <form method="POST" action="gallery.php" id="galleryEditForm">
                <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" id="editImageId" value="0">

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;">
                        <?php echo __('ছবির শিরোনাম *', 'Title *'); ?>
                    </label>
                    <input type="text" name="title" id="editTitleInput" class="form-control" required placeholder="<?php echo __('ছবির নাম লিখুন...', 'Enter image title...'); ?>">
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;">
                        <?php echo __('ক্যাটাগরি *', 'Category *'); ?>
                    </label>
                    <select name="category" id="editCategorySelect" class="form-control" required>
                        <?php foreach ($distinctCategories as $c): ?>
                            <option value="<?php echo htmlspecialchars($c); ?>">
                                <?php echo htmlspecialchars($c); ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>
            </form>
        </div>

        <!-- Modal Footer -->
        <div class="modal-footer" style="display: flex; justify-content: flex-end; align-items: center; gap: 12px; padding: 16px 24px; border-top: 1px solid var(--border-color); background: var(--bg-card); flex-shrink: 0;">
            <button type="button" class="btn btn-outline" onclick="closeEditModal()" style="padding: 9px 18px; border-radius: 8px; font-weight: 600;"><?php echo __('বাতিল', 'Cancel'); ?></button>
            <button type="submit" form="galleryEditForm" class="btn btn-primary" style="padding: 9px 22px; border-radius: 8px; font-weight: 600; display: flex; align-items: center; gap: 8px;">
                <i class="fa-solid fa-check"></i><?php echo __('সংরক্ষণ করুন', 'Save'); ?>
            </button>
        </div>
    </div>
</div>

<!-- Upload Modal -->
<div id="uploadModal" class="modal" style="display:none; position: fixed; z-index: 99999; left: 0; top: 0; width: 100%; height: 100%; overflow-x: hidden; overflow-y: auto; background-color: rgba(15,23,42,0.65); backdrop-filter: blur(4px); -webkit-backdrop-filter: blur(4px); justify-content: center; align-items: center; padding: 20px;" onclick="if(event.target===this)closeUploadModal()">
    <div class="modal-content" style="background: var(--bg-card); border-radius: 16px; width: 100%; max-width: 620px; max-height: 90vh; display: flex; flex-direction: column; border: 1px solid var(--border-color); box-shadow: 0 25px 50px -12px rgba(0,0,0,0.35); overflow: hidden; margin: auto;" onclick="event.stopPropagation()">
        
        <!-- Modal Header -->
        <div class="modal-header" style="display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--border-color); flex-shrink: 0; background: var(--bg-card);">
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--text-heading); display: flex; align-items: center; gap: 10px;">
                <i class="fa-solid fa-cloud-arrow-up text-purple-500"></i><?php echo __('নতুন ছবি', 'New Image'); ?>
            </h3>
            <button type="button" class="modal-close-btn" onclick="closeUploadModal()" title="<?php echo __('বন্ধ করুন', 'Close'); ?>" style="background: rgba(0,0,0,0.05); border: 1px solid var(--border-color); width: 34px; height: 34px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 20px; color: var(--text-muted); cursor: pointer; line-height: 1; transition: all 0.2s;">&times;</button>
        </div>

        <!-- Modal Body -->
        <div class="modal-body" style="padding: 22px 24px; overflow-y: auto; flex: 1 1 auto; max-height: calc(90vh - 140px);">
            <form method="POST" action="gallery.php" enctype="multipart/form-data" id="galleryUploadForm">
                <input type="hidden" name="csrf_token" value="<?php echo generateCsrfToken(); ?>">
                <input type="hidden" name="action" value="upload">

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ছবির শিরোনাম', 'Image Title'); ?></label>
                    <input type="text" name="title" class="form-control" placeholder="<?php echo __('উদাঃ রমাদান ব্যানার', 'e.g. Ramadan Banner'); ?>">
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ক্যাটাগরি *', 'Category *'); ?></label>
                    <select name="category" class="form-control" required>
                        <option value="অ্যাপ ব্যানার"><?php echo __('অ্যাপ ব্যানার', 'App Banner'); ?></option>
                        <option value="ইসলামিক আর্ট"><?php echo __('ইসলামিক আর্ট', 'Islamic Art'); ?></option>
                        <option value="ফিচার আইকন"><?php echo __('ফিচার আইকন', 'Feature Icon'); ?></option>
                        <option value="বইয়ের প্রচ্ছদ"><?php echo __('বইয়ের প্রচ্ছদ', 'Book Cover'); ?></option>
                        <option value="নোটিশ ছবি"><?php echo __('নোটিশ ছবি', 'Notice Image'); ?></option>
                        <option value="সাধারণ" selected><?php echo __('সাধারণ', 'General'); ?></option>
                    </select>
                </div>

                <!-- Upload Source Mode Tabs -->
                <div style="display: flex; gap: 8px; margin-bottom: 14px; background: rgba(0,0,0,0.04); padding: 4px; border-radius: 8px;">
                    <button type="button" id="tabUploadFile" class="btn btn-sm" style="flex: 1; border-radius: 6px; font-weight: 600; background: var(--primary, #10b981); color: #fff;" onclick="switchUploadTab('file')">
                        <i class="fa-solid fa-folder-open mr-1"></i><?php echo __('ফাইল আপলোড', 'Upload File'); ?>
                    </button>
                    <button type="button" id="tabUploadUrl" class="btn btn-sm" style="flex: 1; border-radius: 6px; font-weight: 600; background: transparent; color: var(--text-secondary);" onclick="switchUploadTab('url')">
                        <i class="fa-solid fa-link mr-1"></i><?php echo __('ইমেজ লিংক', 'Image URL'); ?>
                    </button>
                </div>

                <!-- 1. File Upload Section -->
                <div id="fileUploadSection" class="form-group mb-4">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;"><?php echo __('ছবি নির্বাচন করুন *', 'Select Images *'); ?></label>
                    <div id="dropZone" style="border: 2px dashed var(--border-color); border-radius: 10px; padding: 25px 15px; text-align: center; background: rgba(139, 92, 246, 0.03); cursor: pointer; transition: all 0.2s;" onclick="document.getElementById('imageFileInput').click()">
                        <i class="fa-solid fa-image fa-2x text-purple-400 mb-2" style="display: block;"></i>
                        <p style="margin: 0; font-size: 13.5px; font-weight: 600; color: var(--text-primary);"><?php echo __('ছবি সিলেক্ট করতে ক্লিক করুন অথবা ড্র্যাগ অ্যান্ড ড্রপ করুন', 'Click to browse or drag & drop images here'); ?></p>
                        <input type="file" name="images[]" id="imageFileInput" accept="image/jpeg,image/png,image/webp,image/gif" multiple style="display: none;" onchange="handleFileSelect(this)">
                    </div>
                    <div id="selectedFilesPreview" style="margin-top: 10px; display: flex; flex-wrap: wrap; gap: 8px;"></div>
                </div>

                <!-- 2. Image URL Section -->
                <div id="urlUploadSection" class="form-group mb-4" style="display: none;">
                    <label style="font-weight: 600; font-size: 13.5px; color: var(--text-primary); margin-bottom: 6px; display: block;">
                        <i class="fa-solid fa-globe text-purple-400 mr-1"></i><?php echo __('ইমেজ URL *', 'Image URL *'); ?>
                    </label>
                    <input type="url" name="image_url" id="imageUrlInput" class="form-control" placeholder="https://example.com/images/banner.jpg" style="font-size: 13.5px;">
                </div>
            </form>
        </div>

        <!-- Modal Footer -->
        <div class="modal-footer" style="display: flex; justify-content: flex-end; align-items: center; gap: 12px; padding: 16px 24px; border-top: 1px solid var(--border-color); background: var(--bg-card); flex-shrink: 0;">
            <button type="button" class="btn btn-outline" onclick="closeUploadModal()" style="padding: 9px 18px; border-radius: 8px; font-weight: 600;"><?php echo __('বাতিল', 'Cancel'); ?></button>
            <button type="submit" form="galleryUploadForm" class="btn btn-primary" id="uploadSubmitBtn" style="padding: 9px 22px; border-radius: 8px; font-weight: 600; display: flex; align-items: center; gap: 8px;">
                <i class="fa-solid fa-cloud-arrow-up"></i><?php echo __('আপলোড', 'Upload'); ?>
            </button>
        </div>
    </div>
</div>

<!-- Image Lightbox Preview Modal -->
<div id="lightboxModal" class="modal" style="display:none; position: fixed; z-index: 10000; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.85); backdrop-filter: blur(4px);" onclick="closeLightboxModal()">
    <div style="position: relative; max-width: 90%; max-height: 85vh; margin: 4% auto; text-align: center;" onclick="event.stopPropagation()">
        <img id="lightboxImg" src="" alt="Preview" style="max-width: 100%; max-height: 80vh; border-radius: 8px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);">
        <div id="lightboxCaption" style="color: #fff; margin-top: 12px; font-size: 15px; font-weight: 600;"></div>
        <button type="button" onclick="closeLightboxModal()" style="position: absolute; top: -35px; right: 0; background: none; border: none; font-size: 30px; color: #fff; cursor: pointer;">&times;</button>
    </div>
</div>

<script>
// Tab Switching in Modal
function switchUploadTab(mode) {
    var fileSec = document.getElementById('fileUploadSection');
    var urlSec = document.getElementById('urlUploadSection');
    var tabFile = document.getElementById('tabUploadFile');
    var tabUrl = document.getElementById('tabUploadUrl');
    var fileInput = document.getElementById('imageFileInput');
    var urlInput = document.getElementById('imageUrlInput');

    if (mode === 'url') {
        fileSec.style.display = 'none';
        urlSec.style.display = 'block';
        tabUrl.style.background = 'var(--primary, #10b981)';
        tabUrl.style.color = '#fff';
        tabFile.style.background = 'transparent';
        tabFile.style.color = 'var(--text-secondary)';
        fileInput.removeAttribute('required');
        urlInput.setAttribute('required', 'required');
    } else {
        fileSec.style.display = 'block';
        urlSec.style.display = 'none';
        tabFile.style.background = 'var(--primary, #10b981)';
        tabFile.style.color = '#fff';
        tabUrl.style.background = 'transparent';
        tabUrl.style.color = 'var(--text-secondary)';
        urlInput.removeAttribute('required');
    }
}

// Upload Modal Handlers
function openUploadModal() {
    var modal = document.getElementById('uploadModal');
    if (modal) modal.style.display = 'flex';
}
window.openUploadModal = openUploadModal;

function closeUploadModal() {
    var modal = document.getElementById('uploadModal');
    if (modal) modal.style.display = 'none';
    var form = document.getElementById('galleryUploadForm');
    if (form) form.reset();
    var preview = document.getElementById('selectedFilesPreview');
    if (preview) preview.innerHTML = '';
    switchUploadTab('file');
}
window.closeUploadModal = closeUploadModal;

// Edit Modal Handlers
function openEditModal(id, title, category, thumbUrl, dimensions, size) {
    document.getElementById('editImageId').value = id;
    document.getElementById('editTitleInput').value = title || '';
    
    var catSelect = document.getElementById('editCategorySelect');
    if (catSelect) {
        var found = false;
        for (var i = 0; i < catSelect.options.length; i++) {
            if (catSelect.options[i].value === category) {
                catSelect.selectedIndex = i;
                found = true;
                break;
            }
        }
        if (!found && category) {
            var opt = new Option(category, category, true, true);
            catSelect.add(opt);
        }
    }

    var thumb = document.getElementById('editThumbPreview');
    if (thumb) thumb.src = thumbUrl || '';

    var infoName = document.getElementById('editInfoName');
    if (infoName) infoName.innerText = title || '';

    var infoDim = document.getElementById('editInfoDim');
    if (infoDim) infoDim.innerText = dimensions || '1920×1080';

    var infoSize = document.getElementById('editInfoSize');
    if (infoSize) infoSize.innerText = size || '';

    var modal = document.getElementById('editModal');
    if (modal) modal.style.display = 'flex';
}
window.openEditModal = openEditModal;

function closeEditModal() {
    var modal = document.getElementById('editModal');
    if (modal) modal.style.display = 'none';
    var form = document.getElementById('galleryEditForm');
    if (form) form.reset();
}
window.closeEditModal = closeEditModal;

// Instant AJAX Optimize Trigger with Spinner
function triggerOptimize(id, title, btnElement) {
    var csrfToken = '<?php echo generateCsrfToken(); ?>';
    
    var origHtml = btnElement ? btnElement.innerHTML : '';
    if (btnElement) {
        btnElement.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>';
        btnElement.style.pointerEvents = 'none';
        btnElement.style.opacity = '0.75';
    }

    var formData = new FormData();
    formData.append('csrf_token', csrfToken);
    formData.append('action', 'optimize');
    formData.append('id', id);
    formData.append('is_ajax', '1');

    fetch('gallery.php', {
        method: 'POST',
        body: formData,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(function(res) {
        return res.json();
    })
    .then(function(data) {
        if (data && data.success) {
            if (btnElement) {
                btnElement.innerHTML = '<i class="fa-solid fa-check text-emerald-500"></i><span class="btn-label"><?php echo __('সফল!', 'Done!'); ?></span>';
                btnElement.style.background = 'rgba(16, 185, 129, 0.2)';
            }
            // Update Card Details Instantly
            var badgePercent = document.getElementById('badge-percent-' + id);
            if (badgePercent) badgePercent.innerText = 'WebP -' + data.saved_percent + '%';

            var badgeDim = document.getElementById('badge-dim-' + id);
            if (badgeDim) badgeDim.innerText = data.dimensions;

            var sizeText = document.getElementById('size-text-' + id);
            if (sizeText) sizeText.innerText = data.compressed_size;

            setTimeout(function() {
                if (btnElement) {
                    btnElement.innerHTML = origHtml;
                    btnElement.style.pointerEvents = 'auto';
                    btnElement.style.opacity = '1';
                    btnElement.style.background = 'rgba(16, 185, 129, 0.12)';
                }
            }, 2000);
        } else {
            alert((data && data.error) ? data.error : 'অপ্টিমাইজেশন ব্যর্থ হয়েছে।');
            if (btnElement) {
                btnElement.innerHTML = origHtml;
                btnElement.style.pointerEvents = 'auto';
                btnElement.style.opacity = '1';
            }
        }
    })
    .catch(function(err) {
        console.error('AJAX Optimize error, falling back to form submit', err);
        document.getElementById('optimizeImageId').value = id;
        document.getElementById('optimizeForm').submit();
    });
}
window.triggerOptimize = triggerOptimize;

// Instant AJAX Delete with Animated Card Removal
function triggerDelete(id, title, btnElement) {
    var confirmMsg = '<?php echo __('আপনি কি নিশ্চিত যে এই ছবিটি মুছে ফেলতে চান?', 'Are you sure you want to delete this image?'); ?>';
    if (title) {
        confirmMsg = '<?php echo __('আপনি কি নিশ্চিত যে "', 'Are you sure you want to delete "'); ?>' + title + '<?php echo __('" ছবিটি মুছে ফেলতে চান?', '"?'); ?>';
    }
    if (!confirm(confirmMsg)) {
        return;
    }

    var csrfToken = '<?php echo generateCsrfToken(); ?>';
    var origHtml = btnElement ? btnElement.innerHTML : '';
    if (btnElement) {
        btnElement.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>';
        btnElement.style.pointerEvents = 'none';
        btnElement.style.opacity = '0.75';
    }

    var formData = new FormData();
    formData.append('csrf_token', csrfToken);
    formData.append('action', 'delete');
    formData.append('id', id);
    formData.append('is_ajax', '1');

    fetch('gallery.php', {
        method: 'POST',
        body: formData,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(function(res) {
        return res.json();
    })
    .then(function(data) {
        if (data && data.success) {
            var card = document.getElementById('card-img-' + id);
            if (card) {
                card.style.transition = 'all 0.35s ease';
                card.style.opacity = '0';
                card.style.transform = 'scale(0.88)';
                setTimeout(function() {
                    card.remove();
                }, 350);
            }
        } else {
            alert((data && data.error) ? data.error : 'মুছতে ব্যর্থ হয়েছে।');
            if (btnElement) {
                btnElement.innerHTML = origHtml;
                btnElement.style.pointerEvents = 'auto';
                btnElement.style.opacity = '1';
            }
        }
    })
    .catch(function(err) {
        console.error('AJAX delete failed, fallback to GET', err);
        window.location.href = 'gallery.php?delete=' + id;
    });
}
window.triggerDelete = triggerDelete;

// AJAX Handler for Edit Form Submit
var editForm = document.getElementById('galleryEditForm');
if (editForm) {
    editForm.addEventListener('submit', function(e) {
        e.preventDefault();
        var submitBtn = document.querySelector('button[form="galleryEditForm"]');
        var origHtml = submitBtn ? submitBtn.innerHTML : '';
        if (submitBtn) {
            submitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> <?php echo __('সংরক্ষণ হচ্ছে...', 'Saving...'); ?>';
            submitBtn.disabled = true;
        }

        var formData = new FormData(editForm);
        formData.append('is_ajax', '1');

        fetch('gallery.php', {
            method: 'POST',
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(function(res) {
            return res.json();
        })
        .then(function(data) {
            if (submitBtn) {
                submitBtn.innerHTML = origHtml;
                submitBtn.disabled = false;
            }
            if (data && data.success) {
                var id = data.id;
                var titleText = document.getElementById('title-text-' + id);
                if (titleText) {
                    titleText.innerText = data.title;
                    titleText.setAttribute('title', data.title);
                }
                var catText = document.getElementById('cat-text-' + id);
                if (catText) {
                    catText.innerHTML = '<i class="fa-solid fa-tag mr-1 text-purple-400"></i>' + data.category;
                }
                closeEditModal();
            } else {
                alert((data && data.error) ? data.error : 'আপডেট ব্যর্থ হয়েছে।');
            }
        })
        .catch(function(err) {
            console.error('AJAX edit failed, fallback to submit', err);
            editForm.submit();
        });
    });
}

// Lightbox Preview
function openPreviewLightbox(url, title) {
    document.getElementById('lightboxImg').src = url;
    document.getElementById('lightboxCaption').innerText = title || '';
    document.getElementById('lightboxModal').style.display = 'block';
}
window.openPreviewLightbox = openPreviewLightbox;

function closeLightboxModal() {
    document.getElementById('lightboxModal').style.display = 'none';
}
window.closeLightboxModal = closeLightboxModal;

// Copy Direct URL with visual feedback
function copyDirectUrl(url, btn) {
    var onSuccess = function() {
        if (!btn) return;
        var origHtml = btn.innerHTML;
        btn.innerHTML = '<i class="fa-solid fa-check text-emerald-500"></i><span class="btn-label"><?php echo __('কপি!', 'Copied!'); ?></span>';
        btn.classList.add('btn-success');
        setTimeout(function() {
            btn.innerHTML = origHtml;
            btn.classList.remove('btn-success');
        }, 2000);
    };

    if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(url).then(onSuccess).catch(function() {
            fallbackCopy(url, onSuccess);
        });
    } else {
        fallbackCopy(url, onSuccess);
    }
}
function fallbackCopy(text, cb) {
    var tempInput = document.createElement('textarea');
    tempInput.value = text;
    tempInput.style.position = 'fixed';
    tempInput.style.left = '-9999px';
    document.body.appendChild(tempInput);
    tempInput.focus();
    tempInput.select();
    try {
        document.execCommand('copy');
        if (cb) cb();
    } catch (err) {
        console.error('Fallback copy failed', err);
    }
    document.body.removeChild(tempInput);
}
window.copyDirectUrl = copyDirectUrl;

// File Select Preview
function handleFileSelect(input) {
    var preview = document.getElementById('selectedFilesPreview');
    preview.innerHTML = '';
    if (input.files && input.files.length > 0) {
        for (var i = 0; i < input.files.length; i++) {
            var file = input.files[i];
            var pill = document.createElement('span');
            pill.style.cssText = 'background: rgba(139, 92, 246, 0.12); color: #8b5cf6; padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 500; display: inline-flex; align-items: center; gap: 5px;';
            pill.innerHTML = '<i class="fa-solid fa-file-image"></i> ' + file.name + ' (' + (file.size / 1024 / 1024).toFixed(1) + ' MB)';
            preview.appendChild(pill);
        }
    }
}

// Drag & drop support
var dropZone = document.getElementById('dropZone');
if (dropZone) {
    ['dragenter', 'dragover'].forEach(function(eventName) {
        dropZone.addEventListener(eventName, function(e) {
            e.preventDefault();
            e.stopPropagation();
            dropZone.style.borderColor = '#8b5cf6';
            dropZone.style.background = 'rgba(139, 92, 246, 0.08)';
        }, false);
    });

    ['dragleave', 'drop'].forEach(function(eventName) {
        dropZone.addEventListener(eventName, function(e) {
            e.preventDefault();
            e.stopPropagation();
            dropZone.style.borderColor = 'var(--border-card)';
            dropZone.style.background = 'rgba(139, 92, 246, 0.03)';
        }, false);
    });

    dropZone.addEventListener('drop', function(e) {
        var dt = e.dataTransfer;
        var files = dt.files;
        document.getElementById('imageFileInput').files = files;
        handleFileSelect(document.getElementById('imageFileInput'));
    }, false);
}

// ESC Key Listener for instant close
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeUploadModal();
        closeEditModal();
        closeLightboxModal();
    }
});

// Window Backdrop Click Listener
window.addEventListener('click', function(e) {
    var uploadModal = document.getElementById('uploadModal');
    if (e.target === uploadModal) {
        closeUploadModal();
    }
    var editModal = document.getElementById('editModal');
    if (e.target === editModal) {
        closeEditModal();
    }
});
</script>

<?php require_once __DIR__ . '/footer.php'; ?>

