<?php
/**
 * ==============================================================================
 * DEEN ONE API - GET APP CONFIG & REMOTE SETTINGS
 * Consumed by Android App (AdminRemoteConfigManager & PhpMysqlBackendService)
 * ==============================================================================
 */
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

try {
    $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings");
    $settings = [];
    while ($row = $stmt->fetch()) {
        $settings[$row['setting_key']] = $row['setting_value'];
    }

    $adsEnabled = ($settings['ads_enabled'] ?? '1') === '1';
    $maintenance = ($settings['maintenance_mode'] ?? '0') === '1';
    $forceUpdate = ($settings['force_update'] ?? '0') === '1';
    $minVersion = (int)($settings['min_app_version'] ?? 1);
    $latestVersion = (int)($settings['latest_app_version'] ?? 1);
    $updateUrl = $settings['update_url'] ?? 'https://play.google.com/store/apps/details?id=com.devflux.deenone';

    $announcementActive = ($settings['announcement_active'] ?? '1') === '1';
    $announcementText = $settings['announcement_banner_text'] ?? 'দীন ওয়ান নলেজ ব্যাটেলে আপনাকে স্বাগতম!';
    $announcementLink = $settings['announcement_deep_link'] ?? 'feature_battle';
    $notifTitle = 'দীন ওয়ান বিশেষ বার্তা';
    $notifType = 'announcement';
    $notifPriority = 'high';
    $notifId = md5($announcementText);

    try {
        $nStmt = $pdo->query("SELECT id, title, body, notification_type, target_action, priority FROM push_notifications WHERE is_active = 1 ORDER BY id DESC LIMIT 1");
        $latestN = $nStmt->fetch();
        if ($latestN) {
            $notifId = 'pn_' . $latestN['id'];
            $notifTitle = $latestN['title'];
            $announcementText = $latestN['body'];
            $announcementLink = $latestN['target_action'];
            $notifType = $latestN['notification_type'];
            $notifPriority = strtolower($latestN['priority']);
        }
    } catch (Exception $e) {}

    $dailyAyah = $settings['daily_ayah_text'] ?? 'বলুন, আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করুন। (সূরা ত্বহা: ১১৪)';
    $dailyHadith = $settings['daily_hadith_text'] ?? 'যে ব্যক্তি ইলম অন্বেষণে কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সহজ করে দেন। (সহীহ মুসলিম)';

    $adMode = strtolower($settings['ad_mode'] ?? 'production');
    $isTestMode = ($adMode === 'test');

    // Official Google Test Ad Unit IDs (Google Play Safety Compliant)
    $testBannerId = 'ca-app-pub-3940256099942544/6300978111';
    $testInterstitialId = 'ca-app-pub-3940256099942544/1033173712';
    $testNativeId = 'ca-app-pub-3940256099942544/2247696110';
    $testRewardedId = 'ca-app-pub-3940256099942544/5224354917';
    $testAppOpenId = 'ca-app-pub-3940256099942544/9257395921';
    $testRewardedInterstitialId = 'ca-app-pub-3940256099942544/5354046379';

    // Production Configured Ad Unit IDs
    $prodAppId = $settings['admob_app_id'] ?? 'ca-app-pub-6495900750071718~9706674012';
    $prodBannerId = $settings['admob_banner_id'] ?? 'ca-app-pub-6495900750071718/7835288176';
    $prodInterstitialId = $settings['admob_interstitial_id'] ?? 'ca-app-pub-6495900750071718/8521769602';
    $prodNativeId = $settings['admob_native_id'] ?? 'ca-app-pub-6495900750071718/3896043165';
    $prodRewardedId = $settings['admob_rewarded_id'] ?? $testRewardedId;
    $prodAppOpenId = $settings['admob_app_open_id'] ?? $testAppOpenId;
    $prodRewardedInterstitialId = $settings['admob_rewarded_interstitial_id'] ?? $testRewardedInterstitialId;

    // Detect Client IP
    $clientIp = $_SERVER['REMOTE_ADDR'] ?? '';
    if (!empty($_SERVER['HTTP_CF_CONNECTING_IP'])) {
        $clientIp = trim($_SERVER['HTTP_CF_CONNECTING_IP']);
    } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
        $parts = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);
        $clientIp = trim($parts[0]);
    }

    $adminBlockAds = ($settings['admin_block_ads'] ?? '1') === '1';
    $adminBlockedIpsRaw = $settings['admin_blocked_ips'] ?? '127.0.0.1, ::1';
    $adminBlockedIps = array_filter(array_map('trim', preg_split('/[\r\n,]+/', $adminBlockedIpsRaw)));

    $isAdminDevice = false;
    if ($adminBlockAds && !empty($clientIp)) {
        if (in_array($clientIp, $adminBlockedIps, true)) {
            $isAdminDevice = true;
        }
    }

    // Strict Rule: If client is an Admin Device or IP, completely suppress all advertisements
    if ($isAdminDevice) {
        $adsEnabled = false;
    }

    // Strict Safety Rule: If Test Mode is ON, active IDs MUST be Google Test IDs.
    // If Production Mode is ON, active IDs MUST be Configured Production IDs.
    $activeBannerId = $isTestMode ? $testBannerId : $prodBannerId;
    $activeInterstitialId = $isTestMode ? $testInterstitialId : $prodInterstitialId;
    $activeNativeId = $isTestMode ? $testNativeId : $prodNativeId;
    $activeRewardedId = $isTestMode ? $testRewardedId : $prodRewardedId;
    $activeAppOpenId = $isTestMode ? $testAppOpenId : $prodAppOpenId;
    $activeRewardedInterstitialId = $isTestMode ? $testRewardedInterstitialId : $prodRewardedInterstitialId;

    $response = [
        'success' => true,
        'ads_enabled' => $adsEnabled,
        'is_admin_device' => $isAdminDevice,
        'maintenance_mode' => $maintenance,
        'maintenance_message' => $settings['maintenance_message'] ?? 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।',
        'force_update' => $forceUpdate,
        'min_version_code' => $minVersion,
        'latest_version_code' => $latestVersion,
        'latest_version_name' => $settings['latest_version_name'] ?? '1.1.0',
        'update_url' => $updateUrl,
        'update_title' => $settings['update_title'] ?? 'নতুন সংস্করণ আপডেট',
        'update_message' => $settings['update_message'] ?? 'দীন ওয়ান অ্যাপের নতুন সংস্করণ উপলব্ধ রয়েছে। উন্নত পারফরম্যান্স ও নতুন ফিচারের জন্য অনুগ্রহ করে এখনই আপডেট করুন।',
        'admob' => [
            'enabled' => $adsEnabled,
            'is_admin_device' => $isAdminDevice,
            'mode' => $adMode,
            'is_test_mode' => $isTestMode,
            'app_id' => $prodAppId,
            
            // Format Toggles (Guaranteed false if adsEnabled is false or on Admin Device)
            'banner_enabled' => $adsEnabled && (($settings['ad_banner_enabled'] ?? '1') === '1'),
            'interstitial_enabled' => $adsEnabled && (($settings['ad_interstitial_enabled'] ?? '1') === '1'),
            'native_enabled' => $adsEnabled && (($settings['ad_native_enabled'] ?? '1') === '1'),
            'rewarded_enabled' => $adsEnabled && (($settings['ad_rewarded_enabled'] ?? '1') === '1'),
            'app_open_enabled' => $adsEnabled && (($settings['ad_app_open_enabled'] ?? '0') === '1'),
            'rewarded_interstitial_enabled' => $adsEnabled && (($settings['ad_rewarded_interstitial_enabled'] ?? '0') === '1'),

            // Active Ad Unit IDs (Enforcing Test vs Production Safety)
            'banner_id' => $activeBannerId,
            'interstitial_id' => $activeInterstitialId,
            'native_id' => $activeNativeId,
            'rewarded_id' => $activeRewardedId,
            'app_open_id' => $activeAppOpenId,
            'rewarded_interstitial_id' => $activeRewardedInterstitialId,

            // Configured Production IDs for Admin reference
            'prod_banner_id' => $prodBannerId,
            'prod_interstitial_id' => $prodInterstitialId,
            'prod_native_id' => $prodNativeId,
            'prod_rewarded_id' => $prodRewardedId,
            'prod_app_open_id' => $prodAppOpenId,
            'prod_rewarded_interstitial_id' => $prodRewardedInterstitialId,

            // Interstitial Behavior Controls (With Google Policy Safety Thresholds)
            'frequency_interval' => max(2, (int)($settings['ad_frequency_interval'] ?? 5)),
            'interstitial_cooldown' => max(30, (int)($settings['ad_interstitial_cooldown'] ?? 60)),
            'interstitial_home_enabled' => ($settings['ad_interstitial_home_enabled'] ?? '0') === '1',

            // Banner Controls
            'banner_placement' => $settings['ad_banner_placement'] ?? 'bottom',

            // Rewarded Controls
            'reward_amount' => (int)($settings['ad_reward_amount'] ?? 10),

            // App Open Controls
            'app_open_cooldown' => (int)($settings['ad_app_open_cooldown'] ?? 14400),

            // Google AdMob Policy Compliance & Family/Islamic Safety
            'max_rating' => $settings['ad_content_rating'] ?? 'G',
            'test_device_id' => $settings['ad_test_device_id'] ?? ''
        ],
        'announcement' => [
            'id' => $notifId,
            'active' => $announcementActive,
            'title' => $notifTitle,
            'body' => $announcementText,
            'type' => $notifType,
            'priority' => $notifPriority,
            'deep_link' => $announcementLink
        ],
        'daily_content' => [
            'ayah' => $dailyAyah,
            'hadith' => $dailyHadith
        ],
        'legal' => [
            'privacy_policy' => $settings['privacy_policy_content'] ?? 'DeenOne (দীন ওয়ান) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে প্রতিশ্রুতিবদ্ধ। আমরা ব্যবহারকারীর সালাত ট্র্যাকিং, কুরআন তিলাওয়াত ও কুইজ পয়েন্ট ক্লাউডে সিঙ্ক করার উদ্দেশ্যে শুধুমাত্র নাম ও ফোন নম্বর সংগ্রহ করি। কোনো প্রকার অননুমোদিত তৃতীয় পক্ষের সাথে ব্যবহারকারীর তথ্য শেয়ার করা হয় না। রক্তদাতা হিসেবে স্বেচ্ছায় নিবন্ধিতদের তথ্য শুধুমাত্র জরুরি প্রয়োজনে অন্য মুসলিম ভাইদের সহায়তায় ব্যবহৃত হয়।',
            'privacy_policy_bn' => $settings['privacy_policy_content'] ?? 'DeenOne (দীন ওয়ান) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে প্রতিশ্রুতিবদ্ধ।',
            'privacy_policy_en' => $settings['privacy_policy_content_en'] ?? 'DeenOne is committed to ensuring the maximum security and confidentiality of users\' personal information.',
            'privacy_url' => $settings['privacy_policy_url'] ?? 'https://deenone.top/privacy',
            'privacy_updated_at' => $settings['privacy_policy_updated_at'] ?? '2026-09-24',
            'privacy_contact_email' => $settings['privacy_contact_email'] ?? 'privacy@deenone.top',
            'privacy_organization_name' => $settings['privacy_organization_name'] ?? 'DeenOne Technologies & Foundation',
            'privacy_data_deletion_info' => $settings['privacy_data_deletion_info'] ?? 'ব্যবহারকারী অ্যাপের সেটিংস থেকে অথবা privacy@deenone.top এ ইমেইল পাঠিয়ে যেকোনো সময় তাদের অ্যাকাউন্ট ও ডেটা স্থায়ীভাবে মুছে ফেলার আবেদন করতে পারেন।',
            'terms_service' => $settings['terms_service_content'] ?? 'DeenOne অ্যাপ্লিকেশনটি মুসলিম উম্মাহর দৈনন্দিন ইবাদত ও দ্বীনি শিক্ষার সহায়তায় বিনামূল্যে পরিচালিত একটি প্ল্যাটফর্ম। কুইজ ও নলেজ ব্যাটেল প্রতিযোগিতায় কোনো প্রকার অনৈতিক উপায় অবলম্বন বা বিভ্রান্তিকর তথ্য প্রচার সম্পূর্ণরূপে নিষিদ্ধ। অ্যাপের সকল ইসলামিক কনটেন্ট কুরআন ও সহীহ সুন্নাহর আলোকে সংকলিত।',
            'terms_title' => $settings['terms_title'] ?? 'ব্যবহারের শর্তাবলী',
            'terms_url' => $settings['terms_service_url'] ?? 'https://deenone.top/terms',
            'terms_updated_at' => $settings['terms_updated_at'] ?? '2026-09-28',
            'support_info' => $settings['support_contact_info'] ?? "দীন ওয়ান সংক্রান্ত যেকোনো জিজ্ঞাসা, পরামর্শ বা সহায়তার জন্য আমাদের অফিসিয়াল সাপোর্ট টিমের সাথে যোগাযোগ করুন।\n\nইমেইল: support@deenone.top\nহোয়াটসঅ্যাপ হেল্পলাইন: +880 1700-000000\nফেসবুক পেজ: fb.com/DeenOneApp\n\nআমরা দ্রুততম সময়ে আপনার প্রশ্নের উত্তর দেওয়ার চেষ্টা করব, ইনশাআল্লাহ।",
            'support_email' => $settings['support_email'] ?? 'support@deenone.top',
            'support_whatsapp' => $settings['support_whatsapp'] ?? '+8801700000000',
            'support_hotline' => $settings['support_hotline'] ?? '+880 1700-000000',
            'support_hours' => $settings['support_hours'] ?? 'শনি - বৃহস্পতি: সকাল ৯:০০ - রাত ৯:০০',
            'support_url' => $settings['support_url'] ?? 'https://deenone.top/support'
        ],
        'server_time' => date('Y-m-d H:i:s')
    ];

    sendJsonResponse($response);
} catch (Exception $e) {
    sendJsonResponse([
        'success' => false,
        'error' => 'Failed to load app config: ' . $e->getMessage()
    ], 500);
}
?>
