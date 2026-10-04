<?php
/**
 * ==============================================================================
 * DEEN ONE - AUTHENTIC ISLAMIC GALLERY SEEDER
 * Seeds gallery images across various categories: App Banners, Islamic Art,
 * Holy Places, Quran Calligraphy, and Feature Icons.
 * ==============================================================================
 */
require_once __DIR__ . '/../api/db.php';

function seedGalleryImages($pdo) {
    // Ensure table structure exists with all required columns
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS `gallery_images` (
          `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
          `image_uid` VARCHAR(60) NOT NULL UNIQUE,
          `title` VARCHAR(255) NOT NULL,
          `category` VARCHAR(100) DEFAULT 'সাধারণ',
          `original_name` VARCHAR(255) DEFAULT '',
          `file_name` VARCHAR(255) DEFAULT '',
          `file_path` VARCHAR(500) NOT NULL,
          `file_url` VARCHAR(500) NOT NULL,
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

    $items = [
        [
            'uid' => 'IMG_KAABA_01',
            'title' => 'পবিত্র কাবা শরীফ ও মসজিদুল হারাম',
            'category' => 'ইসলামিক আর্ট',
            'file_url' => 'https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?w=1200&q=80',
            'width' => 1200,
            'height' => 800,
            'orig_size' => 1450000,
            'comp_size' => 245000,
            'saved' => 83.1
        ],
        [
            'uid' => 'IMG_NABAWY_02',
            'title' => 'মসজিদে নববী ও সবুজ গম্বুজ (মদিনা মুনাওয়ারা)',
            'category' => 'ইসলামিক আর্ট',
            'file_url' => 'https://images.unsplash.com/photo-1564769625905-50e93615e769?w=1200&q=80',
            'width' => 1200,
            'height' => 800,
            'orig_size' => 1890000,
            'comp_size' => 290000,
            'saved' => 84.6
        ],
        [
            'uid' => 'IMG_QURAN_03',
            'title' => 'পবিত্র কুরআনুল কারীম ও রেহাল',
            'category' => 'ইসলামিক আর্ট',
            'file_url' => 'https://images.unsplash.com/photo-1609599006353-e629aaabfeae?w=1200&q=80',
            'width' => 1200,
            'height' => 800,
            'orig_size' => 1320000,
            'comp_size' => 210000,
            'saved' => 84.1
        ],
        [
            'uid' => 'IMG_RAMADAN_04',
            'title' => 'মাহে রমাদান মোবারক স্পেশাল ব্যানার',
            'category' => 'অ্যাপ ব্যানার',
            'file_url' => 'https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?w=1200&q=80',
            'width' => 1200,
            'height' => 600,
            'orig_size' => 1650000,
            'comp_size' => 260000,
            'saved' => 84.2
        ],
        [
            'uid' => 'IMG_EID_05',
            'title' => 'ঈদ মোবারক শুভেচ্ছা ব্যানার',
            'category' => 'অ্যাপ ব্যানার',
            'file_url' => 'https://images.unsplash.com/photo-1519817650390-64a93db51149?w=1200&q=80',
            'width' => 1200,
            'height' => 600,
            'orig_size' => 1520000,
            'comp_size' => 230000,
            'saved' => 84.8
        ],
        [
            'uid' => 'IMG_AQSA_06',
            'title' => 'পবিত্র মসজিদুল আক্বসা (কুব্বাতুস সাখরাহ)',
            'category' => 'ইসলামিক আর্ট',
            'file_url' => 'https://images.unsplash.com/photo-1579618218290-25a26c483f9c?w=1200&q=80',
            'width' => 1200,
            'height' => 800,
            'orig_size' => 1780000,
            'comp_size' => 280000,
            'saved' => 84.3
        ],
        [
            'uid' => 'IMG_PRAYER_07',
            'title' => 'সালাত ও মোনাজাতের নান্দনিক দৃশ্য',
            'category' => 'ইসলামিক আর্ট',
            'file_url' => 'https://images.unsplash.com/photo-1564769625624-9b2f6797f1f9?w=1200&q=80',
            'width' => 1200,
            'height' => 800,
            'orig_size' => 1410000,
            'comp_size' => 220000,
            'saved' => 84.4
        ],
        [
            'uid' => 'IMG_TASBIH_08',
            'title' => 'যিকির ও তাসবীহ আর্ট',
            'category' => 'ফিচার আইকন',
            'file_url' => 'https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?w=800&q=80',
            'width' => 800,
            'height' => 800,
            'orig_size' => 950000,
            'comp_size' => 150000,
            'saved' => 84.2
        ]
    ];

    $stmt = $pdo->prepare("
        INSERT INTO gallery_images 
        (image_uid, title, category, original_name, file_name, file_path, file_url, original_size, compressed_size, saved_percent, width, height, mime_type)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'image/webp')
        ON DUPLICATE KEY UPDATE 
            title = VALUES(title),
            category = VALUES(category),
            file_url = VALUES(file_url),
            original_size = VALUES(original_size),
            compressed_size = VALUES(compressed_size),
            saved_percent = VALUES(saved_percent),
            width = VALUES(width),
            height = VALUES(height);
    ");

    foreach ($items as $item) {
        $stmt->execute([
            $item['uid'],
            $item['title'],
            $item['category'],
            $item['uid'] . '.jpg',
            $item['uid'] . '.webp',
            $item['file_url'],
            $item['file_url'],
            $item['orig_size'],
            $item['comp_size'],
            $item['saved'],
            $item['width'],
            $item['height']
        ]);
    }
}

// If run directly
if (php_sapi_name() === 'cli' || !isset($_SERVER['HTTP_HOST'])) {
    $pdo = getDbConnection();
    seedGalleryImages($pdo);
    $cnt = $pdo->query("SELECT count(*) FROM gallery_images")->fetchColumn();
    echo "SUCCESS: Seeded gallery_images table. Total count: $cnt\n";
}
