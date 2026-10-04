<?php
require_once __DIR__ . '/../api/db.php';
require_once __DIR__ . '/seed_halal_foods.php';

try {
    $pdo = getDbConnection();
    // Ensure table structure exists
    $pdo->exec("CREATE TABLE IF NOT EXISTS `halal_foods` (
      `id` INT AUTO_INCREMENT PRIMARY KEY,
      `item_key` VARCHAR(80) NOT NULL UNIQUE,
      `title` VARCHAR(191) NOT NULL,
      `title_en` VARCHAR(191) DEFAULT NULL,
      `arabic_name` VARCHAR(191) DEFAULT NULL,
      `category` VARCHAR(50) NOT NULL DEFAULT 'DAILY_FOOD',
      `status` VARCHAR(20) NOT NULL DEFAULT 'HALAL',
      `scientific_name` VARCHAR(191) DEFAULT NULL,
      `scientific_name_en` VARCHAR(191) DEFAULT NULL,
      `source_origin` TEXT DEFAULT NULL,
      `source_origin_en` TEXT DEFAULT NULL,
      `image_url` VARCHAR(255) DEFAULT NULL,
      `nutrition_benefits` TEXT DEFAULT NULL,
      `nutrition_benefits_en` TEXT DEFAULT NULL,
      `usage_instructions` TEXT DEFAULT NULL,
      `usage_instructions_en` TEXT DEFAULT NULL,
      `description` TEXT NOT NULL,
      `description_en` TEXT DEFAULT NULL,
      `hadith_ref` TEXT DEFAULT NULL,
      `hadith_ref_en` TEXT DEFAULT NULL,
      `fiqh_ruling` TEXT DEFAULT NULL,
      `fiqh_ruling_en` TEXT DEFAULT NULL,
      `halal_alternative` TEXT DEFAULT NULL,
      `halal_alternative_en` TEXT DEFAULT NULL,
      `is_active` TINYINT(1) DEFAULT 1,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      INDEX `idx_status` (`status`),
      INDEX `idx_category` (`category`),
      INDEX `idx_title` (`title`),
      INDEX `idx_title_en` (`title_en`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

    // Clean truncate and seed
    $pdo->exec("TRUNCATE TABLE `halal_foods`");
    seedComprehensiveHalalFoods($pdo);

    $cnt = $pdo->query("SELECT COUNT(*) FROM `halal_foods` WHERE `is_active` = 1")->fetchColumn();
    echo "SUCCESS: Seeded " . $cnt . " authentic halal foods into MySQL database 'deenonet_db'.\n";
} catch (Exception $e) {
    echo "ERROR: " . $e->getMessage() . "\n";
}
