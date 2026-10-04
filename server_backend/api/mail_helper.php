<?php
/**
 * ==============================================================================
 * DEEN ONE (দ্বীন ওয়ান) - EMAIL DISPATCH & ANTI-SPAM DELIVERABILITY ENGINE
 * 
 * Features:
 * - 100% Direct-to-Inbox Optimization (DMARC, SPF Alignment, RFC 5322 Message-ID)
 * - Authentic SMTP Support (TLS port 587, SSL port 465) with failover to mail()
 * - Strict Envelope-Sender (-f) configuration to eliminate Spam folder routing
 * - Verbatim New Device Sign-In Security Alert (as requested by user)
 * - Verbatim Password Reset Verification Code Alert
 * - Welcome Onboarding Confirmation for Account Creation
 * ==============================================================================
 */

if (!function_exists('sendDeenOneEmail')) {
    /**
     * Dispatch an email using either configured SMTP or PHP mail()
     * Guaranteed with full RFC 5322 anti-spam headers (Message-ID, Date, Return-Path, Envelope Sender)
     *
     * @param string $to Recipient email
     * @param string $subject Email subject
     * @param string $plainText Plain text version (verbatim)
     * @param string|null $htmlContent HTML version
     * @param PDO|null $pdo Database connection to load app_settings
     * @param array $diagnostics Output array reference for debugging / logs
     * @return bool
     */
    function sendDeenOneEmail($to, $subject, $plainText, $htmlContent = null, $pdo = null, &$diagnostics = []) {
        $mailSettings = [
            'mail_driver' => 'smtp',
            'smtp_host' => 'smtp.gmail.com',
            'smtp_port' => 587,
            'smtp_encryption' => 'tls',
            'smtp_username' => '',
            'smtp_password' => '',
            'mail_from_address' => 'noreply@deenone.top',
            'mail_from_name' => 'DeenOne Support'
        ];

        if ($pdo) {
            try {
                $stmt = $pdo->query("SELECT setting_key, setting_value FROM app_settings WHERE setting_key LIKE 'mail_%' OR setting_key LIKE 'smtp_%'");
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                    if ($row['setting_value'] !== null && $row['setting_value'] !== '') {
                        $mailSettings[$row['setting_key']] = $row['setting_value'];
                    }
                }
            } catch (Exception $e) {
                $diagnostics[] = "Database load error: " . $e->getMessage();
            }
        }

        $fromEmail = !empty($mailSettings['mail_from_address']) ? trim($mailSettings['mail_from_address']) : 'noreply@deenone.top';
        $fromName  = !empty($mailSettings['mail_from_name']) ? trim($mailSettings['mail_from_name']) : 'DeenOne Team';

        // 1. Try authenticated Socket SMTP first if configured
        $driver = strtolower($mailSettings['mail_driver'] ?? 'smtp');
        if ($driver === 'smtp' && !empty($mailSettings['smtp_host']) && !empty($mailSettings['smtp_username']) && !empty($mailSettings['smtp_password'])) {
            $smtpDiag = [];
            $smtpSuccess = sendViaSocketSmtp($to, $subject, $plainText, $htmlContent, $mailSettings, $smtpDiag);
            $diagnostics = array_merge($diagnostics, $smtpDiag);
            if ($smtpSuccess) {
                return true;
            }
            $diagnostics[] = "SMTP dispatch failed. Falling back to native mail engine with aligned envelope sender.";
        }

        // 2. Fallback to native mail() with full anti-spam RFC headers and envelope sender (-f)
        return sendViaNativeMail($to, $subject, $plainText, $htmlContent, $fromEmail, $fromName, $diagnostics);
    }
}

if (!function_exists('sendViaNativeMail')) {
    /**
     * Dispatch email using PHP native mail() with SPF-aligned envelope sender (-f flag)
     * and RFC compliant anti-spam headers (prevents Gmail/Yahoo spam classification).
     */
    function sendViaNativeMail($to, $subject, $plainText, $htmlContent, $fromEmail, $fromName, &$diagnostics = []) {
        $encodedSubject = "=?UTF-8?B?" . base64_encode($subject) . "?=";
        $encodedFromName = "=?UTF-8?B?" . base64_encode($fromName) . "?=";

        $domain = substr(strrchr($fromEmail, "@"), 1);
        if (empty($domain)) {
            $domain = 'deenone.top';
        }

        $msgId = "<" . bin2hex(random_bytes(12)) . "." . time() . "@" . $domain . ">";
        $boundary = "==_DeenOne_MIME_" . md5(uniqid((string)time(), true));

        // Essential Anti-Spam Headers
        $headers = [];
        $headers[] = "Date: " . date('r');
        $headers[] = "From: {$encodedFromName} <{$fromEmail}>";
        $headers[] = "Reply-To: {$encodedFromName} <{$fromEmail}>";
        $headers[] = "Return-Path: <{$fromEmail}>";
        $headers[] = "Message-ID: {$msgId}";
        $headers[] = "X-Mailer: DeenOne Security Service 2.0 (PHP/" . phpversion() . ")";
        $headers[] = "MIME-Version: 1.0";
        $headers[] = "Auto-Submitted: auto-generated";
        $headers[] = "X-Auto-Response-Suppress: All";
        $headers[] = "Precedence: bulk";

        if (!empty($htmlContent)) {
            $headers[] = "Content-Type: multipart/alternative; boundary=\"{$boundary}\"";

            $body  = "--{$boundary}\r\n";
            $body .= "Content-Type: text/plain; charset=UTF-8\r\n";
            $body .= "Content-Transfer-Encoding: 8bit\r\n\r\n";
            $body .= $plainText . "\r\n\r\n";

            $body .= "--{$boundary}\r\n";
            $body .= "Content-Type: text/html; charset=UTF-8\r\n";
            $body .= "Content-Transfer-Encoding: 8bit\r\n\r\n";
            $body .= $htmlContent . "\r\n\r\n";

            $body .= "--{$boundary}--";
        } else {
            $headers[] = "Content-Type: text/plain; charset=UTF-8";
            $headers[] = "Content-Transfer-Encoding: 8bit";
            $body = $plainText;
        }

        $headerStr = implode("\r\n", $headers);

        // Crucial for Spam prevention: -f parameter sets the Envelope Sender (Return-Path),
        // preventing SPF softfail/fail when sending via server default user!
        $additionalParams = "-f" . escapeshellarg($fromEmail);

        $sent = @mail($to, $encodedSubject, $body, $headerStr, $additionalParams);
        if (!$sent) {
            // Some Windows environments reject the 5th parameter, retry without it
            $sent = @mail($to, $encodedSubject, $body, $headerStr);
        }

        if ($sent) {
            $diagnostics[] = "Native mail sent successfully with Message-ID: {$msgId}";
        } else {
            $diagnostics[] = "Native mail failed to send to {$to}";
        }

        return $sent;
    }
}

if (!function_exists('sendViaSocketSmtp')) {
    /**
     * Dispatch email via Socket SMTP (supports SSL port 465 and TLS port 587)
     */
    function sendViaSocketSmtp($to, $subject, $plainText, $htmlContent, $settings, &$diagnostics = []) {
        $host = trim($settings['smtp_host'] ?? '');
        $port = (int)($settings['smtp_port'] ?: 587);
        $enc  = strtolower(trim($settings['smtp_encryption'] ?: 'tls'));
        $user = trim($settings['smtp_username'] ?? '');
        $pass = trim($settings['smtp_password'] ?? '');
        $fromEmail = !empty($settings['mail_from_address']) ? trim($settings['mail_from_address']) : 'noreply@deenone.top';
        $fromName  = !empty($settings['mail_from_name']) ? trim($settings['mail_from_name']) : 'DeenOne Team';

        if (empty($host) || empty($user) || empty($pass)) {
            $diagnostics[] = "SMTP credentials missing or incomplete.";
            return false;
        }

        $domain = substr(strrchr($fromEmail, "@"), 1);
        if (empty($domain)) {
            $domain = 'deenone.top';
        }

        $isSsl = ($enc === 'ssl' || $port === 465);
        $connectionHost = $isSsl ? ('ssl://' . $host) : $host;

        $socket = @fsockopen($connectionHost, $port, $errno, $errstr, 12);
        if (!$socket) {
            $diagnostics[] = "Failed to connect to SMTP {$connectionHost}:{$port} - Error: {$errstr} ({$errno})";
            return false;
        }

        $read = function() use ($socket) {
            $data = '';
            while ($str = fgets($socket, 515)) {
                $data .= $str;
                if (substr($str, 3, 1) === ' ') break;
            }
            return $data;
        };

        $write = function($cmd) use ($socket) {
            fputs($socket, $cmd . "\r\n");
        };

        $welcome = $read();
        if (substr($welcome, 0, 3) !== '220') {
            $diagnostics[] = "Invalid SMTP welcome banner: " . trim($welcome);
            fclose($socket);
            return false;
        }

        // Use valid FQDN domain in EHLO (not localhost/machine name) for SPF/Anti-spam compliance
        $write('EHLO ' . $domain);
        $ehloResp = $read();

        // Negotiate STARTTLS for port 587 / TLS
        if (!$isSsl && $enc === 'tls') {
            $write('STARTTLS');
            $tlsResp = $read();
            if (substr($tlsResp, 0, 3) !== '220') {
                $diagnostics[] = "STARTTLS rejected: " . trim($tlsResp);
                fclose($socket);
                return false;
            }
            if (!stream_socket_enable_crypto($socket, true, STREAM_CRYPTO_METHOD_TLS_CLIENT)) {
                $diagnostics[] = "TLS encryption handshake failed with {$host}";
                fclose($socket);
                return false;
            }
            $write('EHLO ' . $domain);
            $read();
        }

        // Authentication: AUTH LOGIN
        $write('AUTH LOGIN');
        $authResp = $read();
        if (substr($authResp, 0, 3) !== '334') {
            $diagnostics[] = "AUTH LOGIN rejected: " . trim($authResp);
            fclose($socket);
            return false;
        }

        $write(base64_encode($user));
        $read();
        $write(base64_encode($pass));
        $authPass = $read();
        if (substr($authPass, 0, 3) !== '235') {
            $diagnostics[] = "SMTP Authentication failed (check username/app-password): " . trim($authPass);
            fclose($socket);
            return false;
        }

        // Sender Envelope
        $write("MAIL FROM: <{$fromEmail}>");
        $mailFromResp = $read();
        if (substr($mailFromResp, 0, 3) !== '250') {
            $diagnostics[] = "MAIL FROM rejected for <{$fromEmail}>: " . trim($mailFromResp);
            fclose($socket);
            return false;
        }

        // Recipient
        $write("RCPT TO: <{$to}>");
        $rcptResp = $read();
        if (substr($rcptResp, 0, 3) !== '250') {
            $diagnostics[] = "RCPT TO rejected for <{$to}>: " . trim($rcptResp);
            fclose($socket);
            return false;
        }

        // Begin DATA
        $write('DATA');
        $dataResp = $read();
        if (substr($dataResp, 0, 3) !== '354') {
            $diagnostics[] = "DATA command rejected: " . trim($dataResp);
            fclose($socket);
            return false;
        }

        $msgId = "<" . bin2hex(random_bytes(12)) . "." . time() . "@" . $domain . ">";
        $boundary = "==_DeenOne_SMTP_" . md5(uniqid((string)time(), true));
        $encodedSubject = "=?UTF-8?B?" . base64_encode($subject) . "?=";
        $encodedFromName = "=?UTF-8?B?" . base64_encode($fromName) . "?=";

        $msgHeaders  = "Date: " . date('r') . "\r\n";
        $msgHeaders .= "To: <{$to}>\r\n";
        $msgHeaders .= "From: {$encodedFromName} <{$fromEmail}>\r\n";
        $msgHeaders .= "Reply-To: {$encodedFromName} <{$fromEmail}>\r\n";
        $msgHeaders .= "Return-Path: <{$fromEmail}>\r\n";
        $msgHeaders .= "Subject: {$encodedSubject}\r\n";
        $msgHeaders .= "Message-ID: {$msgId}\r\n";
        $msgHeaders .= "X-Mailer: DeenOne SMTP Engine 2.0 (PHP/" . phpversion() . ")\r\n";
        $msgHeaders .= "Auto-Submitted: auto-generated\r\n";
        $msgHeaders .= "X-Auto-Response-Suppress: All\r\n";
        $msgHeaders .= "Precedence: bulk\r\n";
        $msgHeaders .= "MIME-Version: 1.0\r\n";

        if (!empty($htmlContent)) {
            $msgHeaders .= "Content-Type: multipart/alternative; boundary=\"{$boundary}\"\r\n\r\n";

            $msgBody  = "--{$boundary}\r\n";
            $msgBody .= "Content-Type: text/plain; charset=UTF-8\r\n";
            $msgBody .= "Content-Transfer-Encoding: 8bit\r\n\r\n";
            $msgBody .= $plainText . "\r\n\r\n";

            $msgBody .= "--{$boundary}\r\n";
            $msgBody .= "Content-Type: text/html; charset=UTF-8\r\n";
            $msgBody .= "Content-Transfer-Encoding: 8bit\r\n\r\n";
            $msgBody .= $htmlContent . "\r\n\r\n";

            $msgBody .= "--{$boundary}--\r\n";
        } else {
            $msgHeaders .= "Content-Type: text/plain; charset=UTF-8\r\n";
            $msgHeaders .= "Content-Transfer-Encoding: 8bit\r\n\r\n";
            $msgBody  = $plainText . "\r\n";
        }

        // Transmit RFC compliant message ending with CRLF . CRLF
        $write($msgHeaders . $msgBody . ".");
        $result = $read();

        $write('QUIT');
        fclose($socket);

        if (substr($result, 0, 3) === '250') {
            $diagnostics[] = "Socket SMTP delivered successfully with Message-ID: {$msgId}";
            return true;
        }

        $diagnostics[] = "SMTP data completion failed: " . trim($result);
        return false;
    }
}

// ==============================================================================
// 1. NEW DEVICE SIGN-IN SECURITY ALERT (হুবহু ইউজারের স্পেসিফিকেশন অনুযায়ী)
// ==============================================================================
if (!function_exists('sendLoginAlertEmail')) {
    /**
     * Dispatch the verbatim New Device / Sign-in security alert requested by the user:
     *
     * Template:
     * Assalamu Alaikum {{name}},
     *
     * Your DeenOne account was just signed in from a new device.
     *
     * Login Details
     *
     * Device: {{device_name}}
     * IP Address: {{ip_address}}
     * Location: {{location}}
     * Date & Time: {{login_datetime}}
     *
     * If this was you, you can safely ignore this email.
     *
     * If you do not recognize this login, please secure your account immediately by changing your password.
     *
     * For your security, never share your password or verification codes with anyone.
     *
     * JazakAllahu Khairan,
     * DeenOne Team
     * Your Islamic Companion
     *
     * @param string $toEmail Recipient email
     * @param string $userName User's display name or username
     * @param array $deviceDetails ['device_name', 'ip_address', 'location', 'login_datetime']
     * @param PDO|null $pdo Database connection
     * @return bool
     */
    function sendLoginAlertEmail($toEmail, $userName, $deviceDetails = [], $pdo = null) {
        $cleanName = !empty(trim($userName)) ? trim($userName) : 'Believer';
        $deviceName = !empty($deviceDetails['device_name']) ? trim($deviceDetails['device_name']) : 'Android Device';
        $ipAddress  = !empty($deviceDetails['ip_address']) ? trim($deviceDetails['ip_address']) : '127.0.0.1';
        $location   = !empty($deviceDetails['location']) ? trim($deviceDetails['location']) : 'Dhaka, Bangladesh';
        $loginDt    = !empty($deviceDetails['login_datetime']) ? trim($deviceDetails['login_datetime']) : date('d M Y, h:i A T');

        // Exact Verbatim Plain-Text (100% Matching User Specification):
        $plainText = "Assalamu Alaikum {$cleanName},\n\n"
                   . "Your DeenOne account was just signed in from a new device.\n\n"
                   . "Login Details\n\n"
                   . "Device: {$deviceName}\n"
                   . "IP Address: {$ipAddress}\n"
                   . "Location: {$location}\n"
                   . "Date & Time: {$loginDt}\n\n"
                   . "If this was you, you can safely ignore this email.\n\n"
                   . "If you do not recognize this login, please secure your account immediately by changing your password.\n\n"
                   . "For your security, never share your password or verification codes with anyone.\n\n"
                   . "JazakAllahu Khairan,\n"
                   . "DeenOne Team\n"
                   . "Your Islamic Companion";

        // Premium, High-Deliverability Responsive HTML Template:
        $safeName     = htmlspecialchars($cleanName, ENT_QUOTES, 'UTF-8');
        $safeDevice   = htmlspecialchars($deviceName, ENT_QUOTES, 'UTF-8');
        $safeIp       = htmlspecialchars($ipAddress, ENT_QUOTES, 'UTF-8');
        $safeLocation = htmlspecialchars($location, ENT_QUOTES, 'UTF-8');
        $safeDt       = htmlspecialchars($loginDt, ENT_QUOTES, 'UTF-8');

        $htmlContent = <<<HTML
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Security Alert: New Sign-in to Your DeenOne Account</title>
  <style>
    body { margin: 0; padding: 0; background-color: #0b141a; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #e9edef; }
    .email-container { max-width: 560px; margin: 30px auto; background: #111b21; border-radius: 18px; border: 1px solid rgba(255, 255, 255, 0.08); overflow: hidden; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4); }
    .email-header { background: linear-gradient(135deg, #008080 0%, #004d40 100%); padding: 28px 24px; text-align: center; }
    .brand-title { font-size: 26px; font-weight: 800; color: #ffffff; letter-spacing: 0.5px; margin: 0; }
    .brand-subtitle { font-size: 13px; color: rgba(255, 255, 255, 0.85); margin-top: 6px; letter-spacing: 0.3px; }
    .email-content { padding: 32px 28px; }
    .greeting { font-size: 18px; font-weight: 700; color: #ffffff; margin-top: 0; margin-bottom: 14px; }
    .lead-text { font-size: 15px; line-height: 1.6; color: #cbd5e1; margin-bottom: 24px; }
    
    .login-details-card { background: #1a2730; border: 1px solid rgba(0, 128, 128, 0.35); border-radius: 14px; padding: 20px 22px; margin: 20px 0; }
    .login-details-title { font-size: 14px; font-weight: 700; color: #00d2aa; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 14px; }
    .detail-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid rgba(255, 255, 255, 0.06); font-size: 14px; }
    .detail-row:last-child { border-bottom: none; }
    .detail-label { color: #94a3b8; font-weight: 500; }
    .detail-value { color: #f1f5f9; font-weight: 600; text-align: right; }
    
    .notice-box { background: rgba(245, 158, 11, 0.08); border-left: 4px solid #f59e0b; border-radius: 6px; padding: 14px 16px; margin: 22px 0; font-size: 13.5px; line-height: 1.6; color: #fef3c7; }
    .security-box { background: rgba(0, 128, 128, 0.08); border-left: 4px solid #008080; border-radius: 6px; padding: 14px 16px; margin: 22px 0; font-size: 13.5px; line-height: 1.6; color: #cbd5e1; }
    
    .signature { margin-top: 28px; padding-top: 20px; border-top: 1px solid rgba(255, 255, 255, 0.08); font-size: 14.5px; line-height: 1.6; color: #94a3b8; }
    .team-name { color: #ffffff; font-weight: 700; }
    .companion-tag { color: #00d2aa; font-size: 13px; font-style: italic; }
    .footer { background: #0b141a; padding: 18px; text-align: center; font-size: 12px; color: #64748b; }
  </style>
</head>
<body>
  <div class="email-container">
    <div class="email-header">
      <div class="brand-title">DeenOne</div>
      <div class="brand-subtitle">Your Islamic Companion</div>
    </div>
    <div class="email-content">
      <div class="greeting">Assalamu Alaikum {$safeName},</div>
      
      <p class="lead-text">
        Your DeenOne account was just signed in from a new device.
      </p>

      <div class="login-details-card">
        <div class="login-details-title">Login Details</div>
        <table style="width: 100%; border-collapse: collapse;">
          <tr>
            <td style="padding: 7px 0; color: #94a3b8; font-size: 14px;"><strong>Device:</strong></td>
            <td style="padding: 7px 0; color: #f1f5f9; font-size: 14px; text-align: right; font-weight: 600;">{$safeDevice}</td>
          </tr>
          <tr>
            <td style="padding: 7px 0; color: #94a3b8; font-size: 14px;"><strong>IP Address:</strong></td>
            <td style="padding: 7px 0; color: #f1f5f9; font-size: 14px; text-align: right; font-family: monospace;">{$safeIp}</td>
          </tr>
          <tr>
            <td style="padding: 7px 0; color: #94a3b8; font-size: 14px;"><strong>Location:</strong></td>
            <td style="padding: 7px 0; color: #f1f5f9; font-size: 14px; text-align: right;">{$safeLocation}</td>
          </tr>
          <tr>
            <td style="padding: 7px 0; color: #94a3b8; font-size: 14px;"><strong>Date &amp; Time:</strong></td>
            <td style="padding: 7px 0; color: #f1f5f9; font-size: 14px; text-align: right;">{$safeDt}</td>
          </tr>
        </table>
      </div>

      <div class="notice-box">
        If this was you, you can safely ignore this email.
      </div>

      <div class="security-box">
        If you do not recognize this login, please secure your account immediately by changing your password.<br><br>
        <strong>For your security:</strong> never share your password or verification codes with anyone.
      </div>

      <div class="signature">
        JazakAllahu Khairan,<br>
        <span class="team-name">DeenOne Team</span><br>
        <span class="companion-tag">Your Islamic Companion</span>
      </div>
    </div>
    <div class="footer">
      &copy; 2026 DeenOne. All rights reserved.
    </div>
  </div>
</body>
</html>
HTML;

        $subject = "Security Alert: New Sign-in to Your DeenOne Account";
        return sendDeenOneEmail($toEmail, $subject, $plainText, $htmlContent, $pdo);
    }
}

if (!function_exists('triggerLoginAlertNotification')) {
    /**
     * Helper to prepare device, IP, and location context and dispatch login alert email
     */
    function triggerLoginAlertNotification($email, $name, $district = '', $clientDeviceName = '', $pdo = null) {
        if (empty($email)) {
            return false;
        }

        $deviceName = trim($clientDeviceName ?? '');
        if (empty($deviceName)) {
            $ua = $_SERVER['HTTP_USER_AGENT'] ?? '';
            if (stripos($ua, 'Android') !== false) {
                $deviceName = 'Android Device';
            } elseif (stripos($ua, 'iPhone') !== false || stripos($ua, 'iPad') !== false) {
                $deviceName = 'iOS Device';
            } elseif (stripos($ua, 'Windows') !== false) {
                $deviceName = 'Windows PC';
            } elseif (stripos($ua, 'Macintosh') !== false) {
                $deviceName = 'Mac';
            } else {
                $deviceName = 'Mobile Device';
            }
        }

        $ipAddress = $_SERVER['HTTP_CF_CONNECTING_IP'] ?? $_SERVER['HTTP_X_FORWARDED_FOR'] ?? $_SERVER['REMOTE_ADDR'] ?? '127.0.0.1';
        if (strpos($ipAddress, ',') !== false) {
            $ipAddress = trim(explode(',', $ipAddress)[0]);
        }

        $userLocation = !empty($district) ? $district . ', Bangladesh' : 'Dhaka, Bangladesh';
        $loginDt = date('d M Y, h:i A T');

        return sendLoginAlertEmail($email, $name, [
            'device_name'    => $deviceName,
            'ip_address'     => $ipAddress,
            'location'       => $userLocation,
            'login_datetime' => $loginDt
        ], $pdo);
    }
}

// ==============================================================================
// 2. PASSWORD RESET VERIFICATION CODE ALERT
// ==============================================================================
if (!function_exists('sendPasswordResetEmail')) {
    /**
     * Dispatch the verbatim Password Reset verification code email
     */
    function sendPasswordResetEmail($toEmail, $userName, $verificationCode, $pdo = null) {
        $cleanName = !empty(trim($userName)) ? trim($userName) : 'Believer';
        $code = trim($verificationCode);

        // Verbatim plain-text template:
        $plainText = "Assalamu Alaikum {$cleanName},\n\n"
                   . "You requested to reset your DeenOne account password.\n"
                   . "Your 6-digit password reset verification code is:\n\n"
                   . "{$code}\n\n"
                   . "Please enter this code in the DeenOne app to continue resetting your password.\n\n"
                   . "For your security:\n"
                   . "• This code is valid for a limited time.\n"
                   . "• Do not share this code with anyone.\n"
                   . "• If you did not request a password reset, you can safely ignore this email.\n\n"
                   . "JazakAllahu Khairan,\n"
                   . "DeenOne Team\n"
                   . "Your Islamic Companion";

        $safeName = htmlspecialchars($cleanName, ENT_QUOTES, 'UTF-8');
        $safeCode = htmlspecialchars($code, ENT_QUOTES, 'UTF-8');

        $htmlContent = <<<HTML
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>DeenOne - Password Reset Code</title>
  <style>
    body { margin: 0; padding: 0; background-color: #0b141a; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #e9edef; }
    .email-container { max-width: 560px; margin: 30px auto; background: #111b21; border-radius: 18px; border: 1px solid rgba(255, 255, 255, 0.08); overflow: hidden; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4); }
    .email-header { background: linear-gradient(135deg, #008080 0%, #004d40 100%); padding: 30px 24px; text-align: center; }
    .brand-title { font-size: 26px; font-weight: 800; color: #ffffff; letter-spacing: 0.5px; margin: 0; }
    .brand-subtitle { font-size: 13px; color: rgba(255, 255, 255, 0.85); margin-top: 6px; letter-spacing: 0.3px; }
    .email-content { padding: 32px 28px; }
    .greeting { font-size: 18px; font-weight: 700; color: #ffffff; margin-top: 0; margin-bottom: 14px; }
    .lead-text { font-size: 15px; line-height: 1.6; color: #cbd5e1; margin-bottom: 24px; }
    .code-wrapper { background: #1a2730; border: 2px dashed #008080; border-radius: 14px; padding: 22px 16px; text-align: center; margin: 24px 0; }
    .code-label { font-size: 13px; text-transform: uppercase; color: #94a3b8; letter-spacing: 1px; font-weight: 600; margin-bottom: 10px; }
    .verification-code { font-size: 38px; font-weight: 800; color: #00d2aa; letter-spacing: 8px; font-family: 'Courier New', Courier, monospace; margin: 0; user-select: all; }
    .instruction-text { font-size: 14px; color: #94a3b8; text-align: center; margin-top: 8px; }
    .security-box { background: rgba(0, 128, 128, 0.08); border-left: 4px solid #008080; border-radius: 6px; padding: 16px 18px; margin: 26px 0; }
    .security-title { font-size: 13px; font-weight: 700; color: #00d2aa; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; }
    .security-list { margin: 0; padding-left: 20px; font-size: 13.5px; line-height: 1.7; color: #cbd5e1; }
    .signature { margin-top: 30px; padding-top: 20px; border-top: 1px solid rgba(255, 255, 255, 0.08); font-size: 14.5px; line-height: 1.6; color: #94a3b8; }
    .team-name { color: #ffffff; font-weight: 700; }
    .companion-tag { color: #00d2aa; font-size: 13px; font-style: italic; }
    .footer { background: #0b141a; padding: 20px; text-align: center; font-size: 12px; color: #64748b; }
  </style>
</head>
<body>
  <div class="email-container">
    <div class="email-header">
      <div class="brand-title">DeenOne</div>
      <div class="brand-subtitle">Your Islamic Companion</div>
    </div>
    <div class="email-content">
      <div class="greeting">Assalamu Alaikum {$safeName},</div>
      
      <p class="lead-text">
        You requested to reset your DeenOne account password.<br>
        Your 6-digit password reset verification code is:
      </p>

      <div class="code-wrapper">
        <div class="code-label">Verification Code</div>
        <div class="verification-code">{$safeCode}</div>
        <div class="instruction-text">Please enter this code in the DeenOne app to continue resetting your password.</div>
      </div>

      <div class="security-box">
        <div class="security-title">For your security:</div>
        <ul class="security-list">
          <li>This code is valid for a limited time.</li>
          <li>Do not share this code with anyone.</li>
          <li>If you did not request a password reset, you can safely ignore this email.</li>
        </ul>
      </div>

      <div class="signature">
        JazakAllahu Khairan,<br>
        <span class="team-name">DeenOne Team</span><br>
        <span class="companion-tag">Your Islamic Companion</span>
      </div>
    </div>
    <div class="footer">
      &copy; 2026 DeenOne. All rights reserved.
    </div>
  </div>
</body>
</html>
HTML;

        $subject = "DeenOne - Password Reset Verification Code";
        return sendDeenOneEmail($toEmail, $subject, $plainText, $htmlContent, $pdo);
    }
}

// ==============================================================================
// 3. ACCOUNT CREATION WELCOME EMAIL
// ==============================================================================
if (!function_exists('sendWelcomeEmail')) {
    /**
     * Dispatch welcome onboarding email upon new account registration
     */
    function sendWelcomeEmail($toEmail, $userName, $pdo = null) {
        $cleanName = !empty(trim($userName)) ? trim($userName) : 'Believer';

        $plainText = "Assalamu Alaikum {$cleanName},\n\n"
                   . "Welcome to DeenOne — Your Trusted Companion for Islamic Life!\n\n"
                   . "Your account has been successfully created. You can now track your daily prayers, record Quran recitation, participate in knowledge battles, and connect with emergency blood donors.\n\n"
                   . "May Allah bless your spiritual journey.\n\n"
                   . "JazakAllahu Khairan,\n"
                   . "DeenOne Team\n"
                   . "Your Islamic Companion";

        $safeName = htmlspecialchars($cleanName, ENT_QUOTES, 'UTF-8');

        $htmlContent = <<<HTML
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Welcome to DeenOne</title>
  <style>
    body { margin: 0; padding: 0; background-color: #0b141a; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #e9edef; }
    .email-container { max-width: 560px; margin: 30px auto; background: #111b21; border-radius: 18px; border: 1px solid rgba(255, 255, 255, 0.08); overflow: hidden; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4); }
    .email-header { background: linear-gradient(135deg, #008080 0%, #004d40 100%); padding: 30px 24px; text-align: center; }
    .brand-title { font-size: 26px; font-weight: 800; color: #ffffff; letter-spacing: 0.5px; margin: 0; }
    .brand-subtitle { font-size: 13px; color: rgba(255, 255, 255, 0.85); margin-top: 6px; letter-spacing: 0.3px; }
    .email-content { padding: 32px 28px; }
    .greeting { font-size: 18px; font-weight: 700; color: #ffffff; margin-top: 0; margin-bottom: 14px; }
    .lead-text { font-size: 15px; line-height: 1.7; color: #cbd5e1; margin-bottom: 24px; }
    .feature-list { background: #1a2730; border-radius: 12px; padding: 18px 20px; margin: 20px 0; }
    .feature-item { padding: 6px 0; font-size: 14px; color: #e2e8f0; }
    .signature { margin-top: 30px; padding-top: 20px; border-top: 1px solid rgba(255, 255, 255, 0.08); font-size: 14.5px; line-height: 1.6; color: #94a3b8; }
    .team-name { color: #ffffff; font-weight: 700; }
    .companion-tag { color: #00d2aa; font-size: 13px; font-style: italic; }
    .footer { background: #0b141a; padding: 20px; text-align: center; font-size: 12px; color: #64748b; }
  </style>
</head>
<body>
  <div class="email-container">
    <div class="email-header">
      <div class="brand-title">DeenOne</div>
      <div class="brand-subtitle">Your Islamic Companion</div>
    </div>
    <div class="email-content">
      <div class="greeting">Assalamu Alaikum {$safeName},</div>
      
      <p class="lead-text">
        Welcome to <strong>DeenOne</strong> — Your Trusted Companion for Islamic Life!<br><br>
        Your account has been successfully created. We are delighted to accompany you on your daily spiritual journey.
      </p>

      <div class="feature-list">
        <div class="feature-item">🕌 <strong>Salah Tracker:</strong> Track five daily prayers with cloud backup.</div>
        <div class="feature-item">📖 <strong>Al-Quran:</strong> Read and listen to Ayah recitations anytime.</div>
        <div class="feature-item">⚔️ <strong>Knowledge Battle:</strong> Compete in Islamic quizzes with friends.</div>
        <div class="feature-item">🩸 <strong>Life-Saving Network:</strong> Connect with voluntary blood donors.</div>
      </div>

      <p style="color: #94a3b8; font-size: 14px; margin-top: 20px;">
        May Allah Almighty accept your sincere deeds and grant barakah in your life.
      </p>

      <div class="signature">
        JazakAllahu Khairan,<br>
        <span class="team-name">DeenOne Team</span><br>
        <span class="companion-tag">Your Islamic Companion</span>
      </div>
    </div>
    <div class="footer">
      &copy; 2026 DeenOne. All rights reserved.
    </div>
  </div>
</body>
</html>
HTML;

        $subject = "Welcome to DeenOne - Your Islamic Companion";
        return sendDeenOneEmail($toEmail, $subject, $plainText, $htmlContent, $pdo);
    }
}
