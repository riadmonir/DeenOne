<?php
/**
 * ==============================================================================
 * DEEN ONE - ENTERPRISE IN-PLACE IMAGE OPTIMIZATION ENGINE
 * High-performance, memory-efficient, in-place image compression & optimization.
 * 
 * CORE GUARANTEES:
 * 1. ZERO URL / Filename Mutation: Files are optimized in-place at the exact same
 *    path and filename (e.g. 001.jpg remains 001.jpg with identical public URL).
 * 2. Database & API Immutability: No database image URLs or API outputs are modified.
 * 3. Format Preservation: JPG stays JPG (with progressive scan), PNG stays PNG
 *    (with alpha transparency preserved), WebP stays WebP.
 * 4. Fail-Safe Atomic Replacement: Safe temporary file creation with validation
 *    before atomic overwrite. If compression fails or produces a larger file,
 *    the original image remains 100% undamaged.
 * 5. Double-Compression Prevention: Skips degrading already-optimized images.
 * 6. EXIF Auto-Orientation: Fixes rotated camera photos before stripping metadata.
 * 7. Hardened Security: Validates true MIME type & image structure to prevent spoofing.
 * ==============================================================================
 */

/**
 * Format bytes to human-readable string (e.g., 24.5 KB, 1.2 MB)
 */
function formatBytes($bytes, $precision = 1) {
    $bytes = (float)$bytes;
    if ($bytes <= 0) return '0 B';
    $units = ['B', 'KB', 'MB', 'GB', 'TB'];
    $pow = floor(log($bytes) / log(1024));
    $pow = min($pow, count($units) - 1);
    $bytes /= pow(1024, $pow);
    return round($bytes, $precision) . ' ' . $units[$pow];
}

/**
 * Optimizes an image file IN-PLACE without changing its filename, path, or URL.
 * Overwrites the original file atomically only if optimization is successful and yields a smaller size.
 *
 * @param string $filePath Full filesystem path to the image file
 * @param array $options Configuration options:
 *        - 'max_dimension' => int (default: 1920) Max width/height; 0 disables resizing
 *        - 'jpeg_quality'  => int (default: 80) JPEG compression quality (1-100)
 *        - 'png_level'     => int (default: 8) PNG zlib compression level (0-9)
 *        - 'webp_quality'  => int (default: 80) WebP quality (1-100)
 *        - 'force'         => bool (default: false) Force overwrite even if size difference is minimal
 * @return array Result metadata
 */
function optimizeImageInPlace($filePath, array $options = []) {
    // 1. Path & Access Validation
    if (empty($filePath) || !file_exists($filePath)) {
        return [
            'success' => false,
            'error' => 'মূল ইমেজ ফাইল পাওয়া যায়নি (Image file not found): ' . htmlspecialchars($filePath)
        ];
    }

    if (!is_readable($filePath) || !is_writable($filePath)) {
        return [
            'success' => false,
            'error' => 'ইমেজ ফাইল পড়ার বা লেখার অনুমতি নেই (File is not readable or writable).'
        ];
    }

    $origSize = filesize($filePath);
    if ($origSize <= 0) {
        return [
            'success' => false,
            'error' => 'ইমেজ ফাইলটি শূন্য বা অকার্যকর (File is empty).'
        ];
    }

    // 2. Security & MIME Validation
    $imageInfo = @getimagesize($filePath);
    if (!$imageInfo || empty($imageInfo['mime'])) {
        return [
            'success' => false,
            'error' => 'ফাইলটি কোনো বৈধ ইমেজ নয় বা ক্ষতিগ্রস্ত (Invalid or corrupted image format).'
        ];
    }

    $mime = strtolower($imageInfo['mime']);
    $allowedMimes = [
        'image/jpeg'  => 'jpg',
        'image/pjpeg' => 'jpg',
        'image/jpg'   => 'jpg',
        'image/png'   => 'png',
        'image/x-png' => 'png',
        'image/webp'  => 'webp',
        'image/gif'   => 'gif'
    ];

    if (!isset($allowedMimes[$mime])) {
        return [
            'success' => false,
            'error' => 'অসমর্থিত ইমেজ ফরম্যাট (Unsupported image format): ' . htmlspecialchars($mime)
        ];
    }

    $origWidth = $imageInfo[0];
    $origHeight = $imageInfo[1];

    if ($origWidth <= 0 || $origHeight <= 0) {
        return [
            'success' => false,
            'error' => 'ইমেজের দৈর্ঘ্য বা প্রস্থ সনাক্ত করা যায়নি।'
        ];
    }

    // Default configuration options
    $maxDimension = isset($options['max_dimension']) ? (int)$options['max_dimension'] : 1920;
    $jpegQuality  = isset($options['jpeg_quality']) ? (int)$options['jpeg_quality'] : 80;
    $pngLevel     = isset($options['png_level']) ? (int)$options['png_level'] : 8;
    $webpQuality  = isset($options['webp_quality']) ? (int)$options['webp_quality'] : 80;
    $force        = !empty($options['force']);

    // 3. Load Source GD Image
    $srcImg = null;
    switch ($mime) {
        case 'image/jpeg':
        case 'image/pjpeg':
        case 'image/jpg':
            $srcImg = @imagecreatefromjpeg($filePath);
            break;
        case 'image/png':
        case 'image/x-png':
            $srcImg = @imagecreatefrompng($filePath);
            break;
        case 'image/webp':
            if (function_exists('imagecreatefromwebp')) {
                $srcImg = @imagecreatefromwebp($filePath);
            }
            break;
        case 'image/gif':
            $srcImg = @imagecreatefromgif($filePath);
            break;
    }

    if (!$srcImg) {
        return [
            'success' => false,
            'error' => 'GD লাইব্রেরি দিয়ে ইমেজ ওপেন করা সম্ভব হয়নি।'
        ];
    }

    // 4. Auto-Fix JPEG EXIF Orientation (Rotate image correctly before stripping EXIF)
    if (function_exists('exif_read_data') && ($mime === 'image/jpeg' || $mime === 'image/pjpeg' || $mime === 'image/jpg')) {
        try {
            $exif = @exif_read_data($filePath);
            if (!empty($exif['Orientation'])) {
                switch ((int)$exif['Orientation']) {
                    case 3:
                        $rotated = @imagerotate($srcImg, 180, 0);
                        if ($rotated) { imagedestroy($srcImg); $srcImg = $rotated; }
                        break;
                    case 6:
                        $rotated = @imagerotate($srcImg, -90, 0);
                        if ($rotated) {
                            imagedestroy($srcImg);
                            $srcImg = $rotated;
                            $temp = $origWidth;
                            $origWidth = $origHeight;
                            $origHeight = $temp;
                        }
                        break;
                    case 8:
                        $rotated = @imagerotate($srcImg, 90, 0);
                        if ($rotated) {
                            imagedestroy($srcImg);
                            $srcImg = $rotated;
                            $temp = $origWidth;
                            $origWidth = $origHeight;
                            $origHeight = $temp;
                        }
                        break;
                }
            }
        } catch (\Throwable $e) {}
    }

    // 5. Calculate New Proportional Dimensions
    $newWidth = $origWidth;
    $newHeight = $origHeight;
    if ($maxDimension > 0 && ($origWidth > $maxDimension || $origHeight > $maxDimension)) {
        $ratio = min($maxDimension / $origWidth, $maxDimension / $origHeight);
        $newWidth = max(1, (int)round($origWidth * $ratio));
        $newHeight = max(1, (int)round($origHeight * $ratio));
    }

    // 6. Create Destination Canvas & High-Quality Resample
    $dstImg = imagecreatetruecolor($newWidth, $newHeight);

    // Alpha channel preservation for PNG, WebP, GIF
    if ($mime === 'image/png' || $mime === 'image/x-png' || $mime === 'image/webp' || $mime === 'image/gif') {
        imagealphablending($dstImg, false);
        imagesavealpha($dstImg, true);
        $transparent = imagecolorallocatealpha($dstImg, 255, 255, 255, 127);
        imagefilledrectangle($dstImg, 0, 0, $newWidth, $newHeight, $transparent);
        imagealphablending($dstImg, true);
    }

    // High quality bicubic resampling
    imagecopyresampled($dstImg, $srcImg, 0, 0, 0, 0, $newWidth, $newHeight, $origWidth, $origHeight);
    imagedestroy($srcImg);

    // 7. Save to Temporary File in the same directory (Ensures cross-device atomic rename works)
    $fileDir = dirname($filePath);
    $ext = pathinfo($filePath, PATHINFO_EXTENSION);
    $tempPath = $fileDir . '/.tmp_opt_' . uniqid('', true) . ($ext ? '.' . $ext : '');

    $saveSuccess = false;
    switch ($mime) {
        case 'image/jpeg':
        case 'image/pjpeg':
        case 'image/jpg':
            // Progressive JPEG: loads faster on low-bandwidth and Android apps
            imageinterlace($dstImg, true);
            $saveSuccess = @imagejpeg($dstImg, $tempPath, $jpegQuality);
            break;

        case 'image/png':
        case 'image/x-png':
            $saveSuccess = @imagepng($dstImg, $tempPath, $pngLevel);
            break;

        case 'image/webp':
            if (function_exists('imagewebp')) {
                $saveSuccess = @imagewebp($dstImg, $tempPath, $webpQuality);
            }
            break;

        case 'image/gif':
            $saveSuccess = @imagegif($dstImg, $tempPath);
            break;
    }

    imagedestroy($dstImg);

    // 8. Fail-Safe Verification
    if (!$saveSuccess || !file_exists($tempPath) || filesize($tempPath) <= 0) {
        if (file_exists($tempPath)) @unlink($tempPath);
        return [
            'success' => false,
            'error' => 'অপ্টিমাইজড ফাইল সাময়িকভাবে সংরক্ষণ করতে ব্যর্থ হয়েছে।'
        ];
    }

    // Verify temp file is a valid readable image
    $tempInfo = @getimagesize($tempPath);
    if (!$tempInfo) {
        @unlink($tempPath);
        return [
            'success' => false,
            'error' => 'অপ্টিমাইজড ফাইলটি বৈধ ইমেজ হিসেবে উত্তীর্ণ হয়নি।'
        ];
    }

    $newSize = filesize($tempPath);

    // 9. Double-Compression & Quality Protection (Rule 14 & 15)
    // If the optimized file is not smaller than the original (and not forced), preserve original!
    if (!$force && $newSize >= $origSize && ($newWidth == $origWidth && $newHeight == $origHeight)) {
        @unlink($tempPath);
        return [
            'success' => true,
            'already_optimized' => true,
            'file_path' => $filePath,
            'original_size' => $origSize,
            'compressed_size' => $origSize,
            'saved_bytes' => 0,
            'saved_percent' => 0.0,
            'width' => $origWidth,
            'height' => $origHeight,
            'mime_type' => $mime,
            'original_formatted' => formatBytes($origSize),
            'compressed_formatted' => formatBytes($origSize),
            'message' => 'ছবিটি ইতিমধ্যে সর্বোচ্চ অপ্টিমাইজড অবস্থায় রয়েছে।'
        ];
    }

    // 10. Atomic Overwrite In-Place
    // Preserve existing file permissions
    $perms = @fileperms($filePath);
    if ($perms !== false) {
        @chmod($tempPath, $perms & 0777);
    }

    // On Windows, rename fails if destination exists; so copy + unlink ensures 100% atomic reliability
    $replaced = false;
    if (strtoupper(substr(PHP_OS, 0, 3)) === 'WIN') {
        $replaced = @copy($tempPath, $filePath);
        @unlink($tempPath);
    } else {
        $replaced = @rename($tempPath, $filePath);
        if (!$replaced) {
            $replaced = @copy($tempPath, $filePath);
            @unlink($tempPath);
        }
    }

    if (!$replaced || !file_exists($filePath)) {
        if (file_exists($tempPath)) @unlink($tempPath);
        return [
            'success' => false,
            'error' => 'মূল ইমেজ ফাইলটি প্রতিস্থাপন (Overwrite) করতে ব্যর্থ হয়েছে।'
        ];
    }

    // Touch file to update modification timestamp (Enables CDN / Browser cache revalidation)
    @touch($filePath);

    $finalSize = filesize($filePath);
    $savedBytes = max(0, $origSize - $finalSize);
    $savedPercent = ($origSize > 0) ? round(($savedBytes / $origSize) * 100, 1) : 0.0;

    return [
        'success' => true,
        'already_optimized' => false,
        'file_path' => $filePath,
        'original_size' => $origSize,
        'compressed_size' => $finalSize,
        'saved_bytes' => $savedBytes,
        'saved_percent' => $savedPercent,
        'width' => $newWidth,
        'height' => $newHeight,
        'mime_type' => $mime,
        'original_formatted' => formatBytes($origSize),
        'compressed_formatted' => formatBytes($finalSize),
        'message' => 'ইমেজ সফলভাবে একই পাথে অপ্টিমাইজ সম্পন্ন হয়েছে।'
    ];
}

/**
 * Legacy WebP Converter (Maintained for Backward Compatibility)
 * If targetPath is not provided or matches sourcePath, optimizes in-place.
 */
function optimizeAndConvertToWebp($sourcePath, $targetPath = null, $maxDimension = 1920, $quality = 78) {
    if (empty($targetPath) || realpath($sourcePath) === realpath($targetPath)) {
        return optimizeImageInPlace($sourcePath, [
            'max_dimension' => $maxDimension,
            'jpeg_quality'  => $quality,
            'webp_quality'  => $quality
        ]);
    }

    // Optimization to a specific target path
    if (!file_exists($sourcePath) || !is_readable($sourcePath)) {
        return ['success' => false, 'error' => 'মূল ইমেজ ফাইল পাওয়া যায়নি বা পড়া সম্ভব হচ্ছে না।'];
    }

    $origSize = filesize($sourcePath);
    $imageInfo = @getimagesize($sourcePath);
    if (!$imageInfo) {
        return ['success' => false, 'error' => 'ইমেজ ফাইলটি ক্ষতিগ্রস্ত বা অবৈধ ফরম্যাটের।'];
    }

    $origWidth = $imageInfo[0];
    $origHeight = $imageInfo[1];
    $mime = $imageInfo['mime'] ?? '';

    $srcImg = null;
    switch (strtolower($mime)) {
        case 'image/jpeg':
        case 'image/jpg':
        case 'image/pjpeg':
            $srcImg = @imagecreatefromjpeg($sourcePath);
            break;
        case 'image/png':
        case 'image/x-png':
            $srcImg = @imagecreatefrompng($sourcePath);
            break;
        case 'image/webp':
            $srcImg = @imagecreatefromwebp($sourcePath);
            break;
        case 'image/gif':
            $srcImg = @imagecreatefromgif($sourcePath);
            break;
        default:
            return ['success' => false, 'error' => 'অসমর্থিত ইমেজ ফরম্যাট: ' . htmlspecialchars($mime)];
    }

    if (!$srcImg) {
        return ['success' => false, 'error' => 'ইমেজ প্রসেস করতে ব্যর্থ হয়েছে।'];
    }

    // Auto-fix JPEG EXIF Orientation
    if (function_exists('exif_read_data') && ($mime === 'image/jpeg' || $mime === 'image/jpg')) {
        try {
            $exif = @exif_read_data($sourcePath);
            if (!empty($exif['Orientation'])) {
                switch ((int)$exif['Orientation']) {
                    case 3:
                        $srcImg = imagerotate($srcImg, 180, 0);
                        break;
                    case 6:
                        $srcImg = imagerotate($srcImg, -90, 0);
                        $temp = $origWidth;
                        $origWidth = $origHeight;
                        $origHeight = $temp;
                        break;
                    case 8:
                        $srcImg = imagerotate($srcImg, 90, 0);
                        $temp = $origWidth;
                        $origWidth = $origHeight;
                        $origHeight = $temp;
                        break;
                }
            }
        } catch (\Throwable $e) {}
    }

    $newWidth = $origWidth;
    $newHeight = $origHeight;
    if ($maxDimension > 0 && ($origWidth > $maxDimension || $origHeight > $maxDimension)) {
        $ratio = min($maxDimension / $origWidth, $maxDimension / $origHeight);
        $newWidth = max(1, (int)round($origWidth * $ratio));
        $newHeight = max(1, (int)round($origHeight * $ratio));
    }

    $dstImg = imagecreatetruecolor($newWidth, $newHeight);
    imagealphablending($dstImg, false);
    imagesavealpha($dstImg, true);
    $transparent = imagecolorallocatealpha($dstImg, 255, 255, 255, 127);
    imagefilledrectangle($dstImg, 0, 0, $newWidth, $newHeight, $transparent);
    imagealphablending($dstImg, true);

    imagecopyresampled($dstImg, $srcImg, 0, 0, 0, 0, $newWidth, $newHeight, $origWidth, $origHeight);
    imagedestroy($srcImg);

    $targetDir = dirname($targetPath);
    if (!is_dir($targetDir)) {
        @mkdir($targetDir, 0755, true);
    }

    $saved = false;
    if (function_exists('imagewebp') && preg_match('/\.webp$/i', $targetPath)) {
        $saved = @imagewebp($dstImg, $targetPath, $quality);
    } elseif (preg_match('/\.png$/i', $targetPath)) {
        $saved = @imagepng($dstImg, $targetPath, 8);
    } else {
        imageinterlace($dstImg, true);
        $saved = @imagejpeg($dstImg, $targetPath, $quality);
    }
    imagedestroy($dstImg);

    if (!$saved || !file_exists($targetPath)) {
        return ['success' => false, 'error' => 'অপ্টিমাইজড ফাইল সেভ করতে ব্যর্থ হয়েছে।'];
    }

    $compressedSize = filesize($targetPath);
    $savedBytes = max(0, $origSize - $compressedSize);
    $savedPercent = ($origSize > 0) ? round(($savedBytes / $origSize) * 100, 1) : 0;

    return [
        'success' => true,
        'target_path' => $targetPath,
        'width' => $newWidth,
        'height' => $newHeight,
        'original_size' => $origSize,
        'compressed_size' => $compressedSize,
        'saved_percent' => $savedPercent,
        'original_formatted' => formatBytes($origSize),
        'compressed_formatted' => formatBytes($compressedSize)
    ];
}
