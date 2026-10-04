<?php
/**
 * ==============================================================================
 * DEEN ONE API - DONATION & VOLUNTARY CONTRIBUTION HUB
 * Handles public data fetching, impact metrics & transaction submissions
 * ==============================================================================
 */

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once dirname(__DIR__) . '/config.php';
require_once __DIR__ . '/db.php';

$pdo = getDbConnection();

$action = $_GET['action'] ?? ($_POST['action'] ?? 'get_page_data');

try {
    switch ($action) {

        // ---------------------------------------------------------------------
        // 1. GET PAGE CONFIGURATION, IMPACT STATS & PAYMENT METHODS
        // ---------------------------------------------------------------------
        case 'get_page_data':
            $lang = strtolower(trim($_GET['lang'] ?? 'en'));
            if (!in_array($lang, ['en', 'bn'])) {
                $lang = 'en';
            }

            // Fetch all settings
            $settingsStmt = $pdo->query("SELECT setting_key, setting_value FROM donation_settings");
            $settings = [];
            while ($row = $settingsStmt->fetch()) {
                $settings[$row['setting_key']] = $row['setting_value'];
            }

            // Dynamic User Count Calculation
            $totalRegisteredUsers = (int)$pdo->query("SELECT COUNT(*) FROM users")->fetchColumn();
            $usersDisplayMode = $settings['stat_users_display_mode'] ?? 'auto';
            
            if ($usersDisplayMode === 'auto') {
                if ($totalRegisteredUsers > 1000000) {
                    $usersCountFormatted = round($totalRegisteredUsers / 1000000, 1) . 'M+';
                } elseif ($totalRegisteredUsers > 1000) {
                    $usersCountFormatted = round($totalRegisteredUsers / 1000, 1) . 'K+';
                } else {
                    $usersCountFormatted = (string)max(1, $totalRegisteredUsers) . '+';
                }
            } else {
                $usersCountFormatted = $settings['stat_users_custom'] ?? '3.2M+';
            }

            // Parse FAQs
            $faqsRaw = $settings['faqs_json'] ?? '[]';
            $faqsParsed = json_decode($faqsRaw, true) ?: [];

            $formattedFaqs = [];
            foreach ($faqsParsed as $f) {
                $formattedFaqs[] = [
                    'question' => $lang === 'bn' ? ($f['question_bn'] ?? $f['question_en']) : ($f['question_en'] ?? $f['question_bn']),
                    'answer' => $lang === 'bn' ? ($f['answer_bn'] ?? $f['answer_en']) : ($f['answer_en'] ?? $f['answer_bn'])
                ];
            }

            // Response payload tailored to current language
            $responseData = [
                'success' => true,
                'lang' => $lang,
                'hero' => [
                    'title' => $lang === 'bn' ? ($settings['hero_title_bn'] ?? 'প্রতিদিন দান করুন, কোটি মুসলিমের দ্বীনি যাত্রায় সঙ্গী হোন') : ($settings['hero_title_en'] ?? 'Contribute Everyday, Support the Journey of Millions of Muslims'),
                    'subtitle' => $lang === 'bn' ? ($settings['hero_subtitle_bn'] ?? 'আপনার ক্ষুদ্র অনুদান দীন ওয়ান-কে কোটি মানুষের কাছে পৌঁছে দিতে সহায়তা করবে') : ($settings['hero_subtitle_en'] ?? 'Your daily contribution will help more Muslims engage in Ibadah every day')
                ],
                'impact_stats' => [
                    'users' => $usersCountFormatted,
                    'users_raw' => $totalRegisteredUsers,
                    'years_usage' => $settings['stat_years_usage'] ?? '907+',
                    'countries' => $settings['stat_countries'] ?? '177+'
                ],
                'currency' => [
                    'usd_to_bdt_rate' => (float)($settings['usd_to_bdt_rate'] ?? 120),
                    'preset_usd' => json_decode($settings['preset_amounts_usd'] ?? '["2","3","5","10"]', true) ?: ["2", "3", "5", "10"],
                    'preset_bdt' => json_decode($settings['preset_amounts_bdt'] ?? '["100","200","500","1000"]', true) ?: ["100", "200", "500", "1000"]
                ],
                'payment_methods' => [
                    'bkash' => [
                        'enabled' => ($settings['bkash_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'বিকাশ' : 'bKash',
                        'number' => $settings['bkash_number'] ?? '01700000000',
                        'type' => $lang === 'bn' ? 'পার্সোনাল / সেন্ড মানি' : 'Personal / Send Money',
                        'instructions' => $lang === 'bn' ? ($settings['bkash_instructions_bn'] ?? 'বিকাশ অ্যাপ থেকে সেন্ড মানি করে নিচের বক্সে ট্রানজেকশন আইডি লিখুন') : ($settings['bkash_instructions_en'] ?? 'Send Money via bKash App and submit the TrxID below')
                    ],
                    'nagad' => [
                        'enabled' => ($settings['nagad_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'নগদ' : 'Nagad',
                        'number' => $settings['nagad_number'] ?? '01700000000',
                        'type' => $lang === 'bn' ? 'পার্সোনাল / সেন্ড মানি' : 'Personal / Send Money',
                        'instructions' => $lang === 'bn' ? ($settings['nagad_instructions_bn'] ?? 'নগদ অ্যাপ থেকে সেন্ড মানি করে নিচের বক্সে ট্রানজেকশন আইডি লিখুন') : ($settings['nagad_instructions_en'] ?? 'Send Money via Nagad App and submit the TrxID below')
                    ],
                    'rocket' => [
                        'enabled' => ($settings['rocket_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'রকেট' : 'Rocket',
                        'number' => $settings['rocket_number'] ?? '017000000000',
                        'type' => $lang === 'bn' ? 'পার্সোনাল' : 'Personal',
                        'instructions' => $lang === 'bn' ? ($settings['rocket_instructions_bn'] ?? 'রকেট অ্যাকাউন্টে টাকা পাঠিয়ে ট্রানজেকশন আইডি লিখুন') : ($settings['rocket_instructions_en'] ?? 'Send money via Rocket and submit TrxID')
                    ],
                    'bank' => [
                        'enabled' => ($settings['bank_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'ব্যাংক ট্রান্সফার' : 'Bank Transfer',
                        'bank_name' => $settings['bank_name'] ?? 'Islami Bank Bangladesh PLC',
                        'account_name' => $settings['bank_account_name'] ?? 'DeenOne Islamic Foundation',
                        'account_no' => $settings['bank_account_no'] ?? '2050123456789000',
                        'branch' => $settings['bank_branch'] ?? 'Dhanmondi Branch, Dhaka',
                        'routing' => $settings['bank_routing'] ?? '125271892',
                        'swift' => $settings['bank_swift'] ?? 'IBBLBDDH',
                        'instructions' => $lang === 'bn' ? ($settings['bank_instructions_bn'] ?? 'ব্যাংকে টাকা ডিপোজিট বা ট্রান্সফার করে ট্রানজ্যাকশন স্লিপ নম্বর লিখুন') : ($settings['bank_instructions_en'] ?? 'Transfer to bank and submit the deposit/slip reference number')
                    ],
                    'binance' => [
                        'enabled' => ($settings['binance_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'বাইনান্স পে' : 'Binance Pay',
                        'binance_pay_id' => $settings['crypto_binance_pay_id'] ?? '88992211',
                        'instructions' => $lang === 'bn' ? ($settings['binance_instructions_bn'] ?? 'বাইনান্স পে আইডিতে সেন্ড করে রেফারেন্স বা পে অর্ডার আইডি সাবমিট করুন') : ($settings['binance_instructions_en'] ?? 'Transfer via Binance Pay ID and submit the Pay Order ID / Reference')
                    ],
                    'paypal' => [
                        'enabled' => ($settings['paypal_enabled'] ?? '1') == '1',
                        'name' => $lang === 'bn' ? 'পেপ্যাল' : 'PayPal',
                        'email' => $settings['paypal_email'] ?? 'donate@deenone.top',
                        'me_link' => $settings['paypal_me_link'] ?? 'https://paypal.me/deenone',
                        'instructions' => $lang === 'bn' ? ($settings['paypal_instructions_bn'] ?? 'পেপ্যালে অনুদান পাঠিয়ে ট্রানজেকশন আইডি নিচের বক্সে লিখুন') : ($settings['paypal_instructions_en'] ?? 'Send donation via PayPal and enter the Transaction ID')
                    ],
                    'custom' => [
                        'enabled' => ($settings['custom_enabled'] ?? '0') == '1',
                        'name' => $lang === 'bn' ? ($settings['custom_title_bn'] ?? 'অন্যান্য মাধ্যম') : ($settings['custom_title_en'] ?? 'Other Gateway'),
                        'details' => $lang === 'bn' ? ($settings['custom_details_bn'] ?? '') : ($settings['custom_details_en'] ?? '')
                    ]
                ],
                'comparison' => [
                    'heading' => $lang === 'bn' ? 'মজবুত ঈমান, সুন্দর জীবন' : 'Strong Faith. A Better Life.',
                    'subheading' => $lang === 'bn' ? 'দীন ওয়ান যেভাবে একজন মুমিনের জীবনকে বরকতময় করে তোলে' : 'How DeenOne Shapes the Life of a Believer',
                    'problem' => [
                        'badge' => $lang === 'bn' ? ($settings['sec_problem_badge_bn'] ?? 'সমস্যা') : ($settings['sec_problem_badge_en'] ?? 'PROBLEM'),
                        'title' => $lang === 'bn' ? ($settings['sec_problem_title_bn'] ?? 'দ্বীনি আমলের ঘাটতি') : ($settings['sec_problem_title_en'] ?? 'Missing Deeds'),
                        'desc' => $lang === 'bn' ? ($settings['sec_problem_desc_bn'] ?? '') : ($settings['sec_problem_desc_en'] ?? '')
                    ],
                    'solution' => [
                        'badge' => $lang === 'bn' ? ($settings['sec_solution_badge_bn'] ?? 'সমাধান') : ($settings['sec_solution_badge_en'] ?? 'SOLUTION'),
                        'title' => $lang === 'bn' ? ($settings['sec_solution_title_bn'] ?? 'দীন ওয়ান') : ($settings['sec_solution_title_en'] ?? 'DeenOne'),
                        'desc' => $lang === 'bn' ? ($settings['sec_solution_desc_bn'] ?? '') : ($settings['sec_solution_desc_en'] ?? '')
                    ],
                    'result' => [
                        'badge' => $lang === 'bn' ? ($settings['sec_result_badge_bn'] ?? 'ফলাফল') : ($settings['sec_result_badge_en'] ?? 'RESULT'),
                        'title' => $lang === 'bn' ? ($settings['sec_result_title_bn'] ?? 'সুন্দর ও বরকতময় ভবিষ্যৎ') : ($settings['sec_result_title_en'] ?? 'Better Tomorrow'),
                        'desc' => $lang === 'bn' ? ($settings['sec_result_desc_bn'] ?? '') : ($settings['sec_result_desc_en'] ?? '')
                    ]
                ],
                'testimonial' => [
                    'quote' => $lang === 'bn' ? ($settings['testimonial_quote_bn'] ?? '') : ($settings['testimonial_quote_en'] ?? ''),
                    'author' => $lang === 'bn' ? ($settings['testimonial_author_bn'] ?? '') : ($settings['testimonial_author_en'] ?? '')
                ],
                'quran_inspiration' => [
                    'verse_ar' => $settings['quran_verse_ar'] ?? '',
                    'translation' => $lang === 'bn' ? ($settings['quran_verse_bn'] ?? '') : ($settings['quran_verse_en'] ?? ''),
                    'reference' => $lang === 'bn' ? ($settings['quran_verse_ref_bn'] ?? '') : ($settings['quran_verse_ref_en'] ?? '')
                ],
                'faqs' => $formattedFaqs
            ];

            echo json_encode($responseData, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
            break;

        // ---------------------------------------------------------------------
        // 2. GET DYNAMIC ANTI-SPAM SECURITY CAPTCHA
        // ---------------------------------------------------------------------
        case 'get_captcha':
            $num1 = rand(2, 9);
            $num2 = rand(1, 9);
            $sum = $num1 + $num2;
            $token = hash_hmac('sha256', (string)$sum, 'DeenOne_Secret_Key_2026');
            $reqLang = strtolower(trim($_GET['lang'] ?? 'bn'));
            $bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
            $enDigits = ['0','1','2','3','4','5','6','7','8','9'];
            
            $question = ($reqLang === 'bn')
                ? str_replace($enDigits, $bnDigits, (string)$num1) . ' + ' . str_replace($enDigits, $bnDigits, (string)$num2) . ' = ?'
                : "$num1 + $num2 = ?";

            echo json_encode([
                'success' => true,
                'question' => $question,
                'token' => $token
            ], JSON_UNESCAPED_UNICODE);
            break;

        // ---------------------------------------------------------------------
        // 3. SUBMIT VOLUNTARY CONTRIBUTION / DONATION RECORD
        // ---------------------------------------------------------------------
        case 'submit_donation':
            $reqLang = strtolower(trim($_POST['lang'] ?? ($_GET['lang'] ?? 'bn')));
            $donorName = trim($_POST['donor_name'] ?? '');
            $email = trim($_POST['email'] ?? '');
            $phone = trim($_POST['phone'] ?? '');
            $amount = (float)($_POST['amount'] ?? 0);
            $currency = strtoupper(trim($_POST['currency'] ?? 'USD'));
            $paymentMethod = strtoupper(trim($_POST['payment_method'] ?? 'BKASH'));
            $trxId = trim($_POST['transaction_id'] ?? '');
            $frequency = strtoupper(trim($_POST['frequency'] ?? 'ONE_TIME'));
            $isAnonymous = !empty($_POST['is_anonymous']) ? 1 : 0;
            $donorNote = trim($_POST['donor_note'] ?? ($_POST['notes'] ?? ''));

            // 1. Validate Amount
            if ($amount <= 0) {
                $err = ($reqLang === 'bn') ? 'অনুগ্রহ করে সঠিক অনুদানের পরিমাণ লিখুন।' : 'Please enter a valid donation amount.';
                echo json_encode(['success' => false, 'error' => $err], JSON_UNESCAPED_UNICODE);
                exit;
            }

            // 2. Validate TrxID
            if (empty($trxId)) {
                $err = ($reqLang === 'bn') ? 'ট্রানজেকশন আইডি বা রেফারেন্স নম্বর প্রদান করা আবশ্যক।' : 'Transaction ID or reference number is required.';
                echo json_encode(['success' => false, 'error' => $err], JSON_UNESCAPED_UNICODE);
                exit;
            }

            // 3. Verify Anti-Spam Security CAPTCHA
            $captchaToken = trim($_POST['captcha_token'] ?? '');
            $captchaAnswer = trim($_POST['captcha_answer'] ?? '');
            $bnDigits = ['০','১','২','৩','৪','৫','৬','৭','৮','৯'];
            $enDigits = ['0','1','2','3','4','5','6','7','8','9'];
            $captchaAnswerEn = str_replace($bnDigits, $enDigits, $captchaAnswer);

            $expectedToken = hash_hmac('sha256', (string)((int)$captchaAnswerEn), 'DeenOne_Secret_Key_2026');

            if (empty($captchaToken) || $captchaToken !== $expectedToken) {
                $err = ($reqLang === 'bn') ? 'নিরাপত্তা ক্যাপচা সঠিক হয়নি। অনুগ্রহ করে পুনরায় চেষ্টা করুন।' : 'Security CAPTCHA verification failed. Please try again.';
                echo json_encode(['success' => false, 'error' => $err], JSON_UNESCAPED_UNICODE);
                exit;
            }

            // 4. Verify Google reCAPTCHA (if configured and token submitted)
            $recaptchaResponse = $_POST['g-recaptcha-response'] ?? '';
            $gSettingStmt = $pdo->query("SELECT setting_key, setting_value FROM donation_settings WHERE setting_key IN ('google_recaptcha_enabled', 'google_recaptcha_secret_key')");
            $gSettings = [];
            while ($r = $gSettingStmt->fetch()) {
                $gSettings[$r['setting_key']] = $r['setting_value'];
            }
            if (($gSettings['google_recaptcha_enabled'] ?? '0') == '1' && !empty($gSettings['google_recaptcha_secret_key']) && !empty($recaptchaResponse)) {
                $verifyUrl = 'https://www.google.com/recaptcha/api/siteverify';
                $postData = http_build_query([
                    'secret' => $gSettings['google_recaptcha_secret_key'],
                    'response' => $recaptchaResponse,
                    'remoteip' => $_SERVER['REMOTE_ADDR'] ?? ''
                ]);
                $ctx = stream_context_create([
                    'http' => [
                        'method' => 'POST',
                        'header' => "Content-Type: application/x-www-form-urlencoded\r\n",
                        'content' => $postData,
                        'timeout' => 5
                    ]
                ]);
                $result = @file_get_contents($verifyUrl, false, $ctx);
                if ($result) {
                    $resObj = json_decode($result, true);
                    if (empty($resObj['success'])) {
                        $err = ($reqLang === 'bn') ? 'Google reCAPTCHA যাচাই ব্যর্থ হয়েছে।' : 'Google reCAPTCHA verification failed.';
                        echo json_encode(['success' => false, 'error' => $err], JSON_UNESCAPED_UNICODE);
                        exit;
                    }
                }
            }

            if (empty($donorName)) {
                $donorName = $isAnonymous 
                    ? (($reqLang === 'bn') ? 'গোপন দাতা' : 'Anonymous Donor')
                    : (($reqLang === 'bn') ? 'সম্মানিত দাতা' : 'Honorable Donor');
            }

            // Calculate BDT equivalent if in USD
            $rateStmt = $pdo->query("SELECT setting_value FROM donation_settings WHERE setting_key = 'usd_to_bdt_rate'");
            $rate = (float)($rateStmt->fetchColumn() ?: 120);
            
            $amountBdt = ($currency === 'USD') ? ($amount * $rate) : $amount;

            // Check for duplicate TrxID
            $dupStmt = $pdo->prepare("SELECT id FROM donations WHERE transaction_id = ? LIMIT 1");
            $dupStmt->execute([$trxId]);
            if ($dupStmt->fetch()) {
                $err = ($reqLang === 'bn') ? 'এই ট্রানজেকশন আইডিটি ইতোমধ্যে জমা দেওয়া হয়েছে। ভেরিফিকেশনের জন্য অপেক্ষা করুন।' : 'This transaction ID has already been submitted. Please wait for verification.';
                echo json_encode(['success' => false, 'error' => $err], JSON_UNESCAPED_UNICODE);
                exit;
            }

            $ins = $pdo->prepare("INSERT INTO donations 
                (donor_name, email, phone, amount, currency, amount_bdt, payment_method, transaction_id, frequency, status, is_anonymous, admin_notes, donor_note)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', ?, ?, ?)");

            $ins->execute([
                $donorName,
                $email ?: null,
                $phone ?: null,
                $amount,
                $currency,
                $amountBdt,
                $paymentMethod,
                $trxId,
                $frequency,
                $isAnonymous,
                $donorNote ?: null,
                $donorNote ?: null
            ]);

            $donationId = $pdo->lastInsertId();

            echo json_encode([
                'success' => true,
                'donation_id' => $donationId,
                'reference_code' => 'DN-' . str_pad($donationId, 6, '0', STR_PAD_LEFT),
                'message' => 'আলহামদুলিল্লাহ! আপনার অনুদানের তথ্য সফলভাবে গৃহীত হয়েছে। আমাদের অ্যাডমিন টিম ট্রানজেকশন যাচাই করে স্ট্যাটাস আপডেট করবে। জাযাকাল্লাহু খাইরান!',
                'message_en' => 'Alhamdulillah! Your contribution details have been received successfully. Our team will verify and update the status shortly. Jazakallahu Khair!'
            ], JSON_UNESCAPED_UNICODE);
            break;

        default:
            echo json_encode(['success' => false, 'error' => 'অজ্ঞাত অনুরোধ']);
            break;
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['success' => false, 'error' => 'সার্ভার ত্রুটি: ' . $e->getMessage()]);
}
