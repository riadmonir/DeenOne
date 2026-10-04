<?php
require_once __DIR__ . '/../api/db.php';
$pdo = getDbConnection();

$bn = '<p>দ্বীনওয়ান (DeenOne) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে দৃঢ় প্রতিশ্রুতিবদ্ধ। একটি খাঁটি, বিজ্ঞাপনমুক্ত ইসলামিক প্ল্যাটফর্ম হিসেবে ব্যবহারকারীর কোনো তথ্যের বাণিজ্যিক ব্যবহার বা অপব্যবহার করা হয় না।</p><h3>১. সংগৃহীত তথ্যাবলী</h3><p>সঠিক নামাজের সময়সূচি, কিবলা দিক এবং বিশুদ্ধ ইসলামিক লাইফস্টাইল ট্র্যাকিং নিশ্চিত করতে আমরা শুধুমাত্র প্রয়োজনীয় তথ্য সংগ্রহ করি:</p><ul><li><strong>প্রোফাইল তথ্য:</strong> নাম ও যোগাযোগ তথ্য (অ্যাকাউন্ট ব্যাকআপ ও মাল্টি-ডিভাইস ডেটা সিঙ্কের জন্য)।</li><li><strong>লোকেশন ডাটা:</strong> শুধুমাত্র সালাতের ওয়াক্ত ও কিবলা কম্পাসের দিক নির্ধারণে ডিভাইসে স্থানীয়ভাবে ব্যবহৃত হয়। এটি আমাদের সার্ভারে স্থায়ীভাবে জমা বা তৃতীয় পক্ষের সাথে ট্র্যাক করা হয় না।</li><li><strong>ইবাদত ও সালাত ট্র্যাকিং:</strong> দৈনিক সালাত, কুরআন তিলাওয়াত ও তাসবীহ জিকিরের পরিসংখ্যান (যা সম্পূর্ণ ব্যক্তিগত ও সুরক্ষিত)।</li><li><strong>রক্তদান নেটওয়ার্ক:</strong> স্বেচ্ছায় নিবন্ধিত রক্তদাতাদের রক্তের গ্রুপ, জেলা ও যোগাযোগের নম্বর শুধুমাত্র জরুরি রক্তপ্রয়োজনে সহমর্মী ভাইদের সুবিধার্থে প্রদর্শিত হয়।</li></ul><h3>২. সার্ভিস ও ডেটা নিরাপত্তা</h3><p>অ্যাপের সমস্ত যোগাযোগ আধুনিক SSL/TLS ২৫৬-বিট এনক্রিপশনের মাধ্যমে সুরক্ষিত।</p><h3>৩. অ্যাকাউন্ট ও ডেটা মুছে ফেলার অধিকার</h3><p>ব্যবহারকারী যেকোনো সময় অ্যাপ সেটিংস থেকে অথবা privacy@deenone.top এ যোগাযোগের মাধ্যমে অ্যাকাউন্ট ও সমস্ত ক্লাউড ডেটা স্থায়ীভাবে মুছে ফেলতে পারেন।</p>';

$en = '<p>DeenOne is strictly committed to protecting user privacy and ensuring top security standards for all personal data. We operate as an authentic, ad-free Islamic companion, adhering strictly to transparent data practices.</p><h3>1. Information We Collect</h3><p>To provide accurate prayer timings, Qibla direction, and Islamic lifestyle features, we collect only necessary data:</p><ul><li><strong>Profile Data:</strong> Name and contact details for multi-device sync and cloud backup.</li><li><strong>Location Data:</strong> Processed strictly locally on your device for accurate prayer times and Qibla compass. It is never stored on external servers or tracked by third parties.</li><li><strong>Worship Tracking:</strong> Daily Salah logs, Quran reading progress, and Tasbih counts stored with end-to-end security.</li><li><strong>Blood Donor Registry:</strong> Blood group, district, and contact numbers shared voluntarily for emergency blood requests.</li></ul><h3>2. Foreground & Media Services</h3><p>Android Foreground Media Services are utilized strictly to ensure uninterrupted Quran audio playback, sleep mode timer scheduling, and precise Adhan reminders.</p><h3>3. Data Security & Permanent Deletion</h3><p>All sensitive transmissions are secured using modern SSL/TLS 256-bit encryption. You may request permanent deletion of your account and all associated data at any time directly through app settings or by contacting privacy@deenone.top.</p>';

$stmt = $pdo->prepare("INSERT INTO app_settings (setting_key, setting_value) VALUES (?, ?) ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value)");
$stmt->execute(['privacy_policy_content', $bn]);
$stmt->execute(['privacy_policy_content_en', $en]);
$stmt->execute(['privacy_title_bn', 'প্রাইভেসি পলিসি']);
$stmt->execute(['privacy_title_en', 'PRIVACY POLICY']);
$stmt->execute(['privacy_updated_at_bn', 'সর্বশেষ হালনাগাদ: ২০২৬']);
$stmt->execute(['privacy_updated_at_en', 'Last update: September 2026']);
$stmt->execute(['privacy_contact_email', 'privacy@deenone.top']);
$stmt->execute(['privacy_policy_url', 'https://deenone.top/privacy']);
$stmt->execute(['privacy_organization_name', 'DeenOne Technologies & Foundation']);

echo "Settings updated successfully.\n";
