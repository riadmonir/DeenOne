<?php
/**
 * ==============================================================================
 * DEEN ONE (দ্বীন ওয়ান) - ABOUT US & TEAM MEMBERS API
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';

if ($method === 'GET') {
    try {
        // 1. Fetch About Info
        $aboutStmt = $pdo->query("SELECT * FROM app_about_info LIMIT 1");
        $aboutInfo = $aboutStmt->fetch();

        if (!$aboutInfo) {
            $aboutInfo = [
                'app_name' => 'DeenOne',
                'tagline_bn' => 'আপনার দ্বীনি জীবনের বিশ্বস্ত নিত্যসঙ্গী',
                'tagline_en' => 'Your Trusted Companion for Islamic Life',
                'app_version' => 'v2.0.0',
                'version_badge_bn' => 'সর্বশেষ সংস্করণ',
                'version_badge_en' => 'Latest Version',
                'mission_title_bn' => 'আমাদের লক্ষ্য',
                'mission_title_en' => 'Our Mission',
                'mission_desc_bn' => 'দ্বীনওয়ান (DeenOne) হলো একটি সর্বাধুনিক ও শতভাগ প্রামাণিক ইসলামিক জীবনধারা ট্র্যাকিং প্ল্যাটফর্ম, যা মুসলিম উম্মাহকে তাদের দৈনন্দিন সালাত, ইবাদত, আমল ও আধ্যাত্মিক অনুশীলনে সাহায্য করার জন্য তৈরি করা হয়েছে।',
                'mission_desc_en' => 'DeenOne is a state-of-the-art authentic Islamic lifestyle platform, built to empower the Muslim Ummah in their daily prayers, worship, deeds, and spiritual elevation.',
                'quote_text_bn' => '“আমাদের উদ্দেশ্য হলো সর্বাধুনিক প্রযুক্তির মাধ্যমে উম্মাহর আধ্যাত্মিক ও দ্বীনি উন্নতিতে সহায়তা করা।”',
                'quote_text_en' => '“Our objective is to assist the spiritual advancement of the Ummah through modern technology.”',
                'pills_json' => '["আধ্যাত্মিকতা", "সুন্নাহ", "প্রযুক্তি", "উম্মাহ"]',
                'copyright_bn' => '© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত',
                'copyright_en' => '© 2026 DeenOne - All rights reserved'
            ];
        }

        if (!empty($aboutInfo['pills_json'])) {
            $pills = json_decode($aboutInfo['pills_json'], true);
            $aboutInfo['pills'] = is_array($pills) ? $pills : ["আধ্যাত্মিকতা", "সুন্নাহ", "প্রযুক্তি", "উম্মাহ"];
        } else {
            $aboutInfo['pills'] = ["আধ্যাত্মিকতা", "সুন্নাহ", "প্রযুক্তি", "উম্মাহ"];
        }

        // 2. Fetch Team Members
        $teamStmt = $pdo->query("SELECT id, name_bn, name_en, role_bn, role_en, avatar_url, github_url, linkedin_url, website_url, email, display_order, is_active FROM app_team_members WHERE is_active = 1 ORDER BY display_order ASC, id ASC");
        $teamMembers = $teamStmt->fetchAll();

        // Fallback default member if table is empty
        if (empty($teamMembers)) {
            $teamMembers = [
                [
                    'id' => 1,
                    'name_bn' => 'রিয়াদ মনির',
                    'name_en' => 'Riad Monir',
                    'role_bn' => 'প্রতিষ্ঠাতা ও প্রধান ডেভেলপার',
                    'role_en' => 'Founder & Lead Developer',
                    'avatar_url' => '',
                    'github_url' => 'https://github.com/riadmonir',
                    'linkedin_url' => 'https://linkedin.com/in/riadmonir',
                    'website_url' => 'https://deenone.top',
                    'email' => 'support@deenone.top',
                    'display_order' => 1,
                    'is_active' => 1
                ]
            ];
        }

        sendJsonResponse([
            'success' => true,
            'about' => $aboutInfo,
            'team' => $teamMembers
        ]);
    } catch (Exception $e) {
        sendJsonResponse([
            'success' => false,
            'error' => 'Failed to load about us data: ' . $e->getMessage()
        ], 500);
    }
}

// POST endpoint for secure updates (API key verification)
$headers = getallheaders();
$apiKey = $headers['X-API-KEY'] ?? $headers['x-api-key'] ?? ($_GET['api_key'] ?? '');
if (defined('API_SECRET_KEY') && $apiKey !== API_SECRET_KEY) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Unauthorized API key'
    ], 401);
}

$input = getRequestData();
$action = $input['action'] ?? '';

try {
    if ($action === 'update_about') {
        $stmt = $pdo->prepare("UPDATE app_about_info SET 
            app_name = ?, tagline_bn = ?, tagline_en = ?, app_version = ?,
            version_badge_bn = ?, version_badge_en = ?, mission_title_bn = ?,
            mission_title_en = ?, mission_desc_bn = ?, mission_desc_en = ?,
            quote_text_bn = ?, quote_text_en = ?, pills_json = ?,
            copyright_bn = ?, copyright_en = ? WHERE id = 1");
        $stmt->execute([
            $input['app_name'] ?? 'DeenOne',
            $input['tagline_bn'] ?? '',
            $input['tagline_en'] ?? '',
            $input['app_version'] ?? 'v2.0.0',
            $input['version_badge_bn'] ?? 'সর্বশেষ সংস্করণ',
            $input['version_badge_en'] ?? 'Latest Version',
            $input['mission_title_bn'] ?? 'আমাদের লক্ষ্য',
            $input['mission_title_en'] ?? 'Our Mission',
            $input['mission_desc_bn'] ?? '',
            $input['mission_desc_en'] ?? '',
            $input['quote_text_bn'] ?? '',
            $input['quote_text_en'] ?? '',
            is_array($input['pills'] ?? null) ? json_encode($input['pills'], JSON_UNESCAPED_UNICODE) : ($input['pills_json'] ?? '[]'),
            $input['copyright_bn'] ?? '© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত',
            $input['copyright_en'] ?? '© 2026 DeenOne - All rights reserved'
        ]);
        sendJsonResponse(['success' => true, 'message' => 'About info updated']);
    }

    sendJsonResponse(['success' => false, 'error' => 'Unknown action: ' . $action], 400);
} catch (Exception $e) {
    sendJsonResponse(['success' => false, 'error' => $e->getMessage()], 500);
}
