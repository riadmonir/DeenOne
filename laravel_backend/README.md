# দীন ওয়ান (DeenOne) - Modern Laravel 11 Backend & Filament Admin Panel

## ১. ওভারভিউ (Overview)
**DeenOne Laravel Backend** হলো দীন ওয়ান অ্যান্ড্রয়েড অ্যাপের জন্য বিশেষভাবে তৈরি এন্টারপ্রাইজ-গ্রেড ব্যাকএন্ড সলিউশন। এতে রয়েছে **Laravel 11 REST API**, **Laravel Sanctum** টোকেন অথেন্টিকেশন, **Redis Queue & Cache**, **FCM Background Broadcast Job**, এবং **Filament PHP**-র বিশ্বমানের অ্যাডমিন প্যানেল।

---

## ২. মূল ফিচারসমূহ (Core Features)

1. **হাই-পারফরম্যান্স REST API (Laravel 11)**:
   - Sanctum Token-based API Authentication (`/api/auth/sync`, `/api/user/profile`)
   - Remote Configuration & Ad Control (`/api/config`)
   - Quran Journey, Hadith & Dua Collections (`/api/quran/journey`, `/api/hadiths`, `/api/duas`)
   - Live Multiplayer Quiz Battle Engine (`/api/battle/create`, `/api/battle/join`, `/api/battle/room/{code}`)
   - Voluntary Blood Donors Directory with District & Blood Group Search (`/api/blood-donors`)
   - GPS Nearby Mosques & Halal Food Places (`/api/mosques`, `/api/halal-foods`)
   - Faraid Inheritance Calculator Records (`/api/faraid`)
   - **সম্পূর্ণ ব্যাকওয়ার্ড কম্প্যাটিবিলিটি**: বিদ্যমান অ্যান্ড্রয়েড অ্যাপ যাতে কোনো রকম বিঘ্ন ছাড়া যুক্ত হতে পারে, সেজন্য সব legacy পাথ (`/api/get_config.php`, `/api/user_sync.php` ইত্যাদি) সরাসরি ম্যাপ করা হয়েছে।

2. **Filament 3/4 অ্যাডমিন প্যানেল**:
   - **অটোমেটিক ডার্ক/লাইট মোড**: আধুনিক Tailwind ও Emerald/Amber কালার প্যালেট।
   - **Dashboard KPI Metrics**: মোট অ্যাপ ইউজার, লাইভ ব্যাটল রুম, প্রস্তুত রক্তদাতা ও পুশ নোটিফিকেশন মেট্রিক্স।
   - **পুশ নোটিফিকেশন ইঞ্জিন**: এক ক্লিকে FCM ব্রডকাস্ট, পুনরায় প্রেরণ এবং ব্যাকগ্রাউন্ড কিউয়িং।
   - **সিস্টেম ও অ্যাড কনফিগারেশন**: ব্যানারে AdMob ইউনিট আইডি পরিবর্তন, রক্ষণাবেক্ষণ মোড এবং প্লে স্টোর ফোর্স আপডেট কন্ট্রোল।
   - **কনটেন্ট ম্যানেজমেন্ট**: হাদীস, দোয়া, কুরআন জার্নি, রক্তদাতা ভেরিফিকেশন এবং ব্যাটল রুম মনিটরিং।

3. **রেডিস কিউ ও ক্রোন শিডিউলার (Redis & Cron Scheduler)**:
   - অ্যাসিঙ্ক্রোনাস FCM নোটিফিকেশন ডিসপ্যাচ (`SendFcmBroadcastJob`)
   - স্বয়ংক্রিয় মেয়াদোত্তীর্ণ ব্যাটল রুম ক্লিনিং (`CleanupExpiredRoomsJob`)
   - প্রতিদিন সকাল ৬:০০ টায় দৈনিক আয়াত ও রাত ৮:০০ টায় দৈনিক হাদীস নোটিফিকেশন শিডিউলিং।

---

## ৩. ডিরেক্টরি স্ট্রাকচার (Architecture Layout)

```
laravel_backend/
├── app/
│   ├── Console/Commands/       # ImportLegacyDataCommand.php
│   ├── Filament/               # Filament Resources & Pages (Admin Panel)
│   │   ├── Pages/              # AppSettingsPage.php
│   │   ├── Resources/          # PushNotification, Hadith, BloodDonor, User, etc.
│   │   └── Widgets/            # StatsOverviewWidget.php
│   ├── Http/
│   │   ├── Controllers/Api/    # Config, AuthSync, Battle, Quran, Hadith, etc.
│   │   └── Middleware/         # ForceJsonResponse, CheckMaintenanceMode
│   ├── Jobs/                   # SendFcmBroadcastJob, CleanupExpiredRoomsJob
│   ├── Models/                 # User, AppSetting, Hadith, Dua, BloodDonor, etc.
│   └── Providers/Filament/     # AdminPanelProvider.php
├── database/
│   ├── migrations/             # 12 production-ready migrations
│   └── seeders/                # Seeders with Kalimas, 99 Names, Stages, Superadmin
├── docker/                     # Nginx, PHP-FPM, Supervisor configs
├── routes/
│   ├── api.php                 # RESTful v1 endpoints + legacy fallbacks
│   ├── console.php             # Laravel Scheduler cron definitions
│   └── web.php
├── tests/Feature/ApiTest.php   # PHPUnit / Pest Feature Tests
├── Dockerfile                  # Production Multi-stage Container
└── docker-compose.yml          # App + Nginx + MySQL 8 + Redis 7
```

---

## ৪. ইনস্টলেশন ও রানিং গাইড (Installation & Deployment)

### পদ্ধতি ক: লোকাল সার্ভারে (Local Development)
```bash
cd laravel_backend
composer install
cp .env.example .env
php artisan key:generate
php artisan migrate --seed
php artisan serve --port=8000
```
- **Admin Panel URL**: `http://127.0.0.1:8000/admin`
- **Default Admin Login**:
  - **Username/Email**: `admin` বা `admin@deenone.top`
  - **Password**: `admin123`

### পদ্ধতি খ: ডকার কম্পোজ দিয়ে (Docker Production Deployment)
```bash
cd laravel_backend
docker compose up -d --build
docker compose exec app php artisan migrate --seed
```

### পদ্ধতি গ: এক্সিস্টিং ডেটা ইম্পোর্ট (Import Existing Data)
```bash
php artisan deenone:import-legacy --sql=../server_backend/deenone_db.sql
```

---

## ৫. অ্যান্ড্রয়েড অ্যাপ কানেকশন (Mobile App Integration)
অ্যান্ড্রয়েড অ্যাপে `BackendConfigManager.java`-তে Base URL নিচের মতো সেট করুন:
```java
public static final String BASE_URL = "http://YOUR_SERVER_IP:8000/api/";
```
সমস্ত রিকোয়েস্ট নির্বিঘ্নে নতুন ব্যাকএন্ডে রাউট হবে।
