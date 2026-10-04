# DeenOne (দীন ওয়ান) - PHP Web Admin Panel & Database Installation Guide



এই গাইডে দীন ওয়ান (DeenOne) ইসলামিক অ্যাপের **PHP Web Admin Panel** এবং **MySQL Database** সেটআপ ও ইনস্টল করার সম্পূর্ণ নির্দেশিকা দেওয়া হলো।



---



## 📌 ১. সিস্টেমের প্রাক-প্রয়োজনীয়তা (System Requirements)



- **ওয়েব সার্ভার**: Apache / Nginx / LiteSpeed (বা লোকাল ডেভেলপমেন্টে XAMPP / WampServer / Laragon)

- **PHP সংস্করণ**: PHP 8.0, 8.1, 8.2 বা তার পরবর্তী সংস্করণ

- **ডাটাবেজ**: MySQL 5.7+ অথবা MariaDB 10.4+

- **PHP Extensions**: `pdo`, `pdo_mysql`, `curl`, `json`, `mbstring`, `openssl` (সবগুলো XAMPP/cPanel-এ ডিফল্টভাবেই চালু থাকে)



---



## 🗄️ ২. ডাটাবেজ ইনস্টলেশন (Database Setup)

### ধাপ ২.১: সিপ্যানেলে ডাটাবেজ তৈরি করুন
১. আপনার **cPanel**-এ লগইন করে **MySQL® Databases**-এ যান।  
২. তৈরিকৃত ডাটাবেজের নাম: **`deenonet_db`**  
৩. ডাটাবেজ ইউজার: **`deenonet_riad`**  
৪. ইউজারের পাসওয়ার্ড: **`@Labib013rt`**  
৫. **Add User to Database** সেকশনে ইউজার ও ডাটাবেজ সিলেক্ট করে **All Privileges** দিয়ে সেভ করুন।

### ধাপ ২.২: phpMyAdmin-এ SQL ফাইল ইমপোর্ট করুন
১. cPanel থেকে **phpMyAdmin** ওপেন করুন।  
২. বামপাশের তালিকা থেকে আপনার ডাটাবেজ **`deenonet_db`** সিলেক্ট করুন।  
৩. উপরের মেনু থেকে **Import** ট্যাবে ক্লিক করুন।  
৪. **Choose File** এ ক্লিক করে আপনার প্রজেক্ট ফোল্ডার থেকে ফাইলটি নির্বাচন করুন:  
   👉 `server_backend/deenone_db.sql`  
৫. নিচে স্ক্রোল করে **Import** (বা **Go**) বাটনে ক্লিক করুন।  
✅ কয়েক সেকেন্ডের মধ্যে সমস্ত ৩১+ টেবিল এবং প্রাথমিক কন্টেন্ট/ডাটা সফলভাবে তৈরি ও সক্রিয় হয়ে যাবে।

---

## ⚙️ ৩. পিএইচপি কনফিগারেশন (`server_backend/config.php`)

`server_backend/config.php` ফাইলটিতে আপনার লাইভ ক্রেডেনশিয়াল অলরেডি কনফিগার করা রয়েছে:

```php
// ডাটাবেজ কনফিগারেশন (লাইভ সিপ্যানেল প্রোডাকশন)
define('DB_HOST', 'localhost');
define('DB_NAME', 'deenonet_db');
define('DB_USER', 'deenonet_riad');
define('DB_PASS', '@Labib013rt');
define('DB_CHARSET', 'utf8mb4');
define('SITE_URL', 'https://deenone.top/');
```

---

## 📱 ৬. অ্যান্ড্রয়েড অ্যাপ্লিকেশনের সাথে ব্যাকএন্ড কানেক্ট করা



অ্যান্ড্রয়েড অ্যাপ্লিকেশনের রিমোট কনফিগ এবং ব্যাকএন্ড ফাইলগুলো আপনার লাইভ ডোমেন **`https://deenone.top/`** এর সাথে প্রস্তুত ও ডিফল্টভাবে সংযুক্ত:



১. [`BackendConfigManager.java`](file:///f:/Deanone/app/src/main/java/com/devflux/deenone/core/backend/BackendConfigManager.java) ফাইলে ডিফল্ট লাইভ সার্ভার ইউআরএল সেট রয়েছে:

```java

public static final String DEFAULT_PHP_API_BASE_URL = "https://deenone.top/server_backend/api/";

```



> 🔒 **নিরাপত্তা নিশ্চিতকরণ**: ডাটাবেজ পাসওয়ার্ড বা ইউজারনেম অ্যান্ড্রয়েড অ্যাপে (APK) কখনোই সংরক্ষিত থাকে না। অ্যাপ সুরক্ষিত HTTPS REST API এর মাধ্যমে সার্ভারের সাথে যোগাযোগ করে।



---



## 🌐 ৭. লাইভ হোস্টিং / সিপ্যানেলে ডেপ্লয়মেন্ট (cPanel Deployment)



১. **ফাইল আপলোড**:

   - `server_backend` ফোল্ডারটি জিপ (.zip) করে আপনার সিপ্যানেল File Manager এর `public_html` এ আপলোড করে Extract করুন (ইউআরএল হবে: `https://deenone.top/server_backend/`)।

২. **ডাটাবেজ ও ইউজার**:

   - Database Name: `deenonet_db`

   - Database User: `deenonet_riad`

   - Database Password: `@Labib013rt`

   - cPanel phpMyAdmin-এ গিয়ে `deenone_db.sql` ফাইলটি ইমপোর্ট করুন।

৩. **কনফিগ ফাইল**:

   - `server_backend/config.php` ফাইলে সমস্ত ক্রেডেনশিয়াল অলরেডি কনফিগার করা আছে।

৪. **ফাইল পারমিশন**:

   - ফোল্ডারের পারমিশন `755` এবং ফাইলের পারমিশন `644` রাখুন।

৫. **অ্যাডমিন প্যানেল অ্যাক্সেস**:

   - `https://deenone.top/server_backend/admin/`



---

## ৪. অ্যাডমিন প্যানেল লগইন (Admin Panel Login)

অ্যাডমিন প্যানেল লগইন পেজে নিচের তথ্য ব্যবহার করুন:

- **ইউজারনেম**: `admin`

- **পাসওয়ার্ড**: `admin123`



> 💡 **পরামর্শ**: লগইন করার পর অবিলম্বে অ্যাডমিন প্যানেলের বাম সাইডবারের **🛡️ অ্যাডমিন একাউন্টস** ([admin_users.php](file:///f:/Deanone/server_backend/admin/admin_users.php)) মেনু থেকে আপনার পাসওয়ার্ড পরিবর্তন করে নিন।



---



## 📱 ৬. অ্যান্ড্রয়েড অ্যাপ্লিকেশনের সাথে ব্যাকএন্ড কানেক্ট করা



অ্যান্ড্রয়েড অ্যাপ্লিকেশনের রিমোট কনফিগ এবং ব্যাকএন্ড ফাইলগুলো ইতিমধ্যে প্রস্তুত রয়েছে:



১. [`BackendConfigManager.java`](file:///f:/Deanone/app/src/main/java/com/devflux/deenone/core/backend/BackendConfigManager.java) ফাইলে আপনার লাইভ সার্ভার ইউআরএলটি সেট করুন:

```java

// উদাহরণস্বরূপ:

public static String getPhpApiBaseUrl(Context context) {

    return getPrefs(context).getString(KEY_PHP_BASE_URL, "https://yourdomain.com/server_backend/api/");

}

```

*লোকাল এমুলেটরে টেস্ট করতে চাইলে ইউআরএল ব্যবহার করুন*:  

`http://10.0.2.2/server_backend/api/` (বা লোকাল আইপি অ্যাড্রেস)



২. অ্যাপ ওপেন করার সাথে সাথে `AdminRemoteConfigManager` ব্যাকগ্রাউন্ডে `get_config.php` থেকে লাইভ কনফিগ, নোটিফিকেশন ও AdMob সেটিংস সিঙ্ক করে নেবে।



---



## 🌐 ৭. লাইভ হোস্টিং / সিপ্যানেলে ডেপ্লয়মেন্ট (cPanel Deployment)



১. **ফাইল আপলোড**:

   - `server_backend` ফোল্ডারটি জিপ (.zip) করে আপনার সিপ্যানেল File Manager এর `public_html` (অথবা সাবডোমেনের ফোল্ডারে) আপলোড করে Extract করুন।

২. **ডাটাবেজ তৈরি**:

   - cPanel -> **MySQL Database Wizard** থেকে নতুন ডাটাবেজ ও ইউজার তৈরি করে সব প্রিভিলেজ দিন।

   - **phpMyAdmin** এ গিয়ে `deenone_db.sql` ফাইলটি ইমপোর্ট করুন।

৩. **কনফিগ ফাইল এডিট**:

   - `server_backend/config.php` এ সিপ্যানেলের ডাটাবেজ নাম, ইউজার এবং পাসওয়ার্ড আপডেট করুন।

৪. **ফাইল পারমিশন**:

   - ফোল্ডারের পারমিশন `755` এবং ফাইলের পারমিশন `644` রাখুন।

৫. **অ্যাডমিন প্যানেল অ্যাক্সেস**:

   - `https://yourdomain.com/server_backend/admin/`



---



## 🔔 ৮. পুশ নোটিফিকেশন ও ফিউচার আপডেট পাঠানোর নিয়ম



১. অ্যাডমিন প্যানেলে লগইন করে সাইডবার থেকে **🔔 পুশ নোটিফিকেশন পাঠান** ([notifications.php](file:///f:/Deanone/server_backend/admin/notifications.php)) এ যান।

২. যদি নতুন অ্যাপ আপডেট পাঠান:

   - উপরে **"🚀 নতুন আপডেট ভার্সন অ্যালার্ট"** প্রি-সেট বাটনে ক্লিক করুন।

   - স্বয়ংক্রিয়ভাবে টাইটেল, মেসেজ এবং ডিপ-লিংক বসে যাবে।

   - **"🚀 নোটিফিকেশন তাৎক্ষণিক পাঠান"** বোতামে ক্লিক করুন।

৩. ব্যবহারকারী নোটিফিকেশনে ক্লিক করলে সরাসরি গুগল প্লে স্টোর বা আপডেট পেজে চলে যাবে।

৪. একইভাবে নলেজ ব্যাটেল, কুরআন, হাদিস ও জরুরি রক্তদানের নোটিফিকেশনও পাঠানো যাবে।



---



## 🛡️ ৯. ব্যাকএন্ডের প্রধান এপিআই এন্ডপয়েন্টসমূহ



| এন্ডপয়েন্ট | মেথড | বিবরণ |

| :--- | :--- | :--- |

| `api/get_config.php` | GET | অ্যাপের সমস্ত রিমোট সেটিংস, AdMob আইডি ও নোটিফিকেশন |

| `api/get_notifications.php` | GET | সক্রিয় পুশ নোটিফিকেশনের তালিকা |

| `api/get_notices.php` | GET | সাধারণ ব্রডকাস্ট নোটিশ |

| `api/get_asmaul_husna.php` | GET | আল্লাহর ৯৯টি পবিত্র গুণবাচক নামের পূর্ণ তালিকা ও অর্থ |

| `api/get_kalimas.php` | GET | ইসলামের ৬টি কালিমার আরবি, উচ্চারণ, অনুবাদ ও ফজিলত |

| `api/audio.php` | GET | ইসলামিক অডিও হাব (ওয়াজ, বয়ান, লেকচার ও তিলাওয়াত) |

| `api/get_quiz_categories.php` | GET | কুইজ ক্যাটাগরি তালিকা |

| `api/get_quiz_questions.php` | GET | ডায়নামিক কুইজ ও ব্যাটেল প্রশ্নব্যাংক |

| `api/get_duas.php` | GET | আরবি ও অর্থসহ প্রামাণ্য দু’আ |

| `api/get_hadiths.php` | GET | কিতাব ও নম্বর ভিত্তিক সহীহ হাদিস |

| `api/get_hajj_articles.php` | GET | হজ ও উমরাহ নির্দেশিকা ও কবুলের আলামত |

| `api/halal_foods.php` | GET | হালাল খাদ্যতালিকা ও E-Code স্ট্যাটাস ডিরেক্টরি |

| `api/faraid.php` | GET/POST | ফারায়েজ ও ইসলামী উত্তরাধিকার হিসাব সংরক্ষণ |

| `api/qaza.php` | GET/POST | কাজা নামাজের ক্লাউড ট্র্যাকিং ও সিঙ্ক |

| `api/community_feed.php` | GET/POST | পোস্ট ফিড, কমেন্ট, লাইক ও রিপোর্ট |

| `api/blood_donors.php` | GET/POST | রক্তদাতা রেজিস্ট্রেশন, সার্চ ও জরুরি আবেদন |

| `api/mosques.php` | GET | জেলা ও জিপিএস ভিত্তিক মসজিদ ও হালাল ডিরেক্টরি |

| `api/leaderboard.php` | GET | শীর্ষ ব্যবহারকারী ও XP পয়েন্ট লিডারবোর্ড |

| `api/user_sync.php` | POST | ইউজার প্রোফাইল ও পয়েন্ট সিঙ্কিং |

| `api/auth.php` | POST | ইউজার সাইন-আপ, লগইন ও প্রোফাইল ব্যবস্থাপনা |

| `api/create_room.php` / `join_room.php` | POST | লাইভ মাল্টিপ্লেয়ার নলেজ ব্যাটেল রুম ও গেম লজিক |



---

**ইনস্টলেশন সফল! দীন ওয়ান অ্যাডমিন প্যানেল এখন ১০০% প্রস্তুত ও সক্রিয়।**

