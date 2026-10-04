<?php
/**
 * ==============================================================================
 * DEEN ONE - LOCAL GALLERY IMAGES SEEDER
 * Generates real, optimized WebP graphic files in uploads/gallery/
 * and seeds the gallery_images database table with 100% local, authentic assets.
 * ==============================================================================
 */
require_once dirname(__DIR__) . '/api/db.php';
$pdo = getDbConnection();

// Ensure upload directory exists
$uploadDir = dirname(__DIR__) . '/uploads/gallery';
if (!is_dir($uploadDir)) {
    @mkdir($uploadDir, 0755, true);
}

// Helper to create a rich Islamic themed gradient graphic with geometric shapes and text
function createIslamicWebpImage($destPath, $titleText, $subText, $arabicText, $bgColor1, $bgColor2, $accentColor, $width = 1200, $height = 675) {
    $im = imagecreatetruecolor($width, $height);
    imagealphablending($im, true);
    imagesavealpha($im, true);

    // Linear gradient background
    for ($y = 0; $y < $height; $y++) {
        $ratio = $y / $height;
        $r = (int)($bgColor1[0] * (1 - $ratio) + $bgColor2[0] * $ratio);
        $g = (int)($bgColor1[1] * (1 - $ratio) + $bgColor2[1] * $ratio);
        $b = (int)($bgColor1[2] * (1 - $ratio) + $bgColor2[2] * $ratio);
        $col = imagecolorallocate($im, $r, $g, $b);
        imageline($im, 0, $y, $width, $y, $col);
    }

    // Draw decorative border & Islamic arches
    $gold = imagecolorallocate($im, $accentColor[0], $accentColor[1], $accentColor[2]);
    $goldAlpha = imagecolorallocatealpha($im, $accentColor[0], $accentColor[1], $accentColor[2], 90);
    $white = imagecolorallocate($im, 255, 255, 255);
    $whiteMuted = imagecolorallocatealpha($im, 240, 245, 255, 30);
    $glow = imagecolorallocatealpha($im, 255, 255, 255, 110);

    // Outer frame
    imagesetthickness($im, 3);
    imagerectangle($im, 30, 30, $width - 30, $height - 30, $goldAlpha);
    imagerectangle($im, 40, 40, $width - 40, $height - 40, $gold);
    imagerectangle($im, 50, 50, $width - 50, $height - 50, $goldAlpha);

    // Decorative corner diamonds
    $corners = [
        [40, 40],
        [$width - 40, 40],
        [40, $height - 40],
        [$width - 40, $height - 40]
    ];
    foreach ($corners as $c) {
        imagefilledellipse($im, $c[0], $c[1], 16, 16, $gold);
        imageellipse($im, $c[0], $c[1], 26, 26, $white);
    }

    // Center circular medallion / star pattern
    $cx = (int)($width / 2);
    $cy = (int)($height / 2) - 30;
    
    // Ambient soft glow circles
    imagefilledellipse($im, $cx, $cy, 380, 380, $glow);
    imageellipse($im, $cx, $cy, 300, 300, $goldAlpha);
    imageellipse($im, $cx, $cy, 280, 280, $gold);
    imageellipse($im, $cx, $cy, 260, 260, $goldAlpha);

    // Islamic Crescent shape in center
    $crescentSize = 90;
    imagefilledellipse($im, $cx, $cy - 20, $crescentSize, $crescentSize, $gold);
    // Cutout to make crescent
    $cutoutColor = imagecolorallocate($im, (int)($bgColor1[0] * 0.4 + $bgColor2[0] * 0.6), (int)($bgColor1[1] * 0.4 + $bgColor2[1] * 0.6), (int)($bgColor1[2] * 0.4 + $bgColor2[2] * 0.6));
    imagefilledellipse($im, $cx + 22, $cy - 28, $crescentSize - 16, $crescentSize - 16, $cutoutColor);

    // Star next to crescent
    imagefilledellipse($im, $cx + 38, $cy - 38, 14, 14, $gold);

    // Fallback built-in string drawing for crisp readability
    $tag = "DEEN ONE ISLAMIC APP";
    imagestring($im, 5, $cx - (strlen($tag) * 4.5), 80, $tag, $gold);
    
    if (!empty($arabicText)) {
        imagestring($im, 5, $cx - (strlen($arabicText) * 4.5), $cy + 65, $arabicText, $white);
    }

    imagestring($im, 5, $cx - (strlen($titleText) * 4.5), $height - 110, $titleText, $white);
    imagestring($im, 4, $cx - (strlen($subText) * 3.7), $height - 75, $subText, $gold);

    // Save as high-quality WebP
    imagewebp($im, $destPath, 85);
    imagedestroy($im);
}

// Sample Gallery Dataset
$galleryData = [
    [
        'uid' => 'IMG_KAABA_01',
        'file_name' => 'kaaba_makkah_01.webp',
        'title' => 'পবিত্র কাবা শরীফ ও মসজিদুল হারাম',
        'category' => 'অ্যাপ ব্যানার',
        'arabic' => 'Bismillahir Rahmanir Rahim',
        'sub' => 'Holy Kaaba & Masjid al-Haram - Makkah',
        'bg1' => [15, 23, 42],
        'bg2' => [6, 78, 59],
        'accent' => [245, 158, 11]
    ],
    [
        'uid' => 'IMG_NABAWI_02',
        'file_name' => 'masjid_nabawi_02.webp',
        'title' => 'মসজিদে নববী ও সবুজ গম্বুজ',
        'category' => 'অ্যাপ ব্যানার',
        'arabic' => 'Al-Masjid an-Nabawi',
        'sub' => 'The Prophet Mosque - Madinah Munawwarah',
        'bg1' => [6, 78, 59],
        'bg2' => [4, 120, 87],
        'accent' => [251, 191, 36]
    ],
    [
        'uid' => 'IMG_QURAN_03',
        'file_name' => 'quran_kareem_03.webp',
        'title' => 'পবিত্র কুরআনুল কারীম ও রেহাল',
        'category' => 'ইসলামিক আর্ট',
        'arabic' => 'Al-Quran Al-Kareem',
        'sub' => 'The Noble Holy Quran & Guidance',
        'bg1' => [19, 78, 74],
        'bg2' => [15, 118, 110],
        'accent' => [253, 224, 71]
    ],
    [
        'uid' => 'IMG_RAMADAN_04',
        'file_name' => 'ramadan_kareem_04.webp',
        'title' => 'মাহে রমাদান মোবারক স্পেশাল ব্যানার',
        'category' => 'অ্যাপ ব্যানার',
        'arabic' => 'Ramadan Mubarak',
        'sub' => 'The Blessed Month of Mercy & Forgiveness',
        'bg1' => [30, 27, 75],
        'bg2' => [67, 56, 202],
        'accent' => [245, 158, 11]
    ],
    [
        'uid' => 'IMG_EID_05',
        'file_name' => 'eid_mubarak_05.webp',
        'title' => 'পবিত্র ঈদ মোবারক শুভেচ্ছা ব্যানার',
        'category' => 'অ্যাপ ব্যানার',
        'arabic' => 'Eid Mubarak & Greetings',
        'sub' => 'Celebration of Joy & Brotherhood',
        'bg1' => [88, 28, 135],
        'bg2' => [126, 34, 206],
        'accent' => [251, 191, 36]
    ],
    [
        'uid' => 'IMG_TASBIH_06',
        'file_name' => 'tasbih_dhikr_06.webp',
        'title' => 'তাসবীহ ও যিকির আর্ট',
        'category' => 'ইসলামিক আর্ট',
        'arabic' => 'SubhanAllah wa Bihamdihi',
        'sub' => 'Remembrance of Allah & Daily Zikr',
        'bg1' => [12, 74, 96],
        'bg2' => [14, 116, 144],
        'accent' => [253, 224, 71]
    ],
    [
        'uid' => 'IMG_DUA_07',
        'file_name' => 'dua_munajat_07.webp',
        'title' => 'মাসনূন দু’আ ও দৈনন্দিন মুনাজাত',
        'category' => 'বইয়ের প্রচ্ছদ',
        'arabic' => 'Rabbana Atina fid-Dunya Hasanah',
        'sub' => 'Daily Masnoon Supplications & Munajat',
        'bg1' => [15, 23, 42],
        'bg2' => [30, 41, 59],
        'accent' => [52, 211, 153]
    ],
    [
        'uid' => 'IMG_GEOMETRY_08',
        'file_name' => 'islamic_geometry_08.webp',
        'title' => 'ইসলামিক ক্যালিগ্রাফি ও জ্যামিতিক নকশা',
        'category' => 'ফিচার আইকন',
        'arabic' => 'La Ilaha Illallah Muhammadur Rasulullah',
        'sub' => 'Islamic Geometric Pattern & Ornament',
        'bg1' => [24, 24, 27],
        'bg2' => [63, 63, 70],
        'accent' => [245, 158, 11]
    ]
];

// Clean table and insert local items
$pdo->exec("TRUNCATE TABLE gallery_images");

$stmt = $pdo->prepare("
    INSERT INTO gallery_images 
    (image_uid, title, category, original_name, file_name, file_path, file_url, original_size, compressed_size, saved_percent, width, height, mime_type) 
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'image/webp')
");

$seededCount = 0;
foreach ($galleryData as $item) {
    $filePath = $uploadDir . '/' . $item['file_name'];
    $publicUrl = 'uploads/gallery/' . $item['file_name'];

    // Generate local WebP file
    createIslamicWebpImage(
        $filePath,
        $item['title'],
        $item['sub'],
        $item['arabic'],
        $item['bg1'],
        $item['bg2'],
        $item['accent'],
        1200,
        675
    );

    $fileSize = filesize($filePath);
    $mockOrigSize = (int)($fileSize * 4.2); // ~76% savings ratio
    $savedPercent = round((($mockOrigSize - $fileSize) / $mockOrigSize) * 100, 1);

    $stmt->execute([
        $item['uid'],
        $item['title'],
        $item['category'],
        str_replace('.webp', '.jpg', $item['file_name']),
        $item['file_name'],
        $filePath,
        $publicUrl,
        $mockOrigSize,
        $fileSize,
        $savedPercent,
        1200,
        675
    ]);
    $seededCount++;
}

echo "Successfully seeded {$seededCount} authentic local WebP images into uploads/gallery/ and gallery_images table!\n";
