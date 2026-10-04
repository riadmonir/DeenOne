# Full-Stack Simultaneous Development Rule (App + Database + PHP Admin Panel)

## Mandatory Development Principle
Whenever any feature, change, or new functionality is introduced in the DeenOne project:
1. **Never implement only client-side or defer the backend**:
   Always develop the Android App, the MySQL Database (`deenone_db.sql`), the PHP REST APIs (`server_backend/api/`), and the PHP Web Admin Panel (`server_backend/admin/`) **simultaneously in tandem**. Never wait for a separate command to build the backend.
2. **Database First / Co-developed**:
   Whenever a new data entity or field is created in the app (e.g. Room entities, SharedPreferences), immediately add/update the corresponding table or column in `server_backend/deenone_db.sql`.
3. **API Co-developed**:
   Provide the corresponding REST API in `server_backend/api/` with proper JSON response, input validation, status filters (active/blocked), and API authentication.
4. **Admin Panel Co-developed with Full CRUD & Moderation**:
   Add corresponding CRUD and control panels in `server_backend/admin/` (Add, Edit, Delete, Block/Deactivate) with responsive UI, Bengali labels, and CSRF protection.
5. **Real-time Removal / Block Sync in App**:
   When the Admin deletes or blocks an item in the Admin Panel (e.g., Quiz question, Hadith, Notice), that change must immediately reflect in the app. Blocked or deleted items must be filtered out and never displayed to end users.
6. **Hybrid Local + Online Storage Lifecycle**:
   Features like Quiz, Duas, etc. sync dynamic data from online APIs/backend into local storage (Room Database/cache) while remaining synchronized with the server's online MySQL database.
7. **No Separate Later Work Needed**:
   Everything must be 100% functional, verified with `php -l` and Gradle compilation (`compileDebugJavaWithJavac`), leaving zero pending tasks across app and backend.
8. **Mandatory Build-Time & Implementation Verification (MySQL, Auto-Sync & Admin Connectivity)**:
   Every time the app is built or code is implemented, it is mandatory to verify:
   - All app data modules are properly connected to the MySQL database.
   - Auto-sync data import (fetching from DB to app) and data export (pushing user data/progress to DB) are fully functional.
   - The app is seamlessly connected with the PHP Admin Panel (`server_backend/admin/`), ensuring immediate reflection of Admin CRUD/block actions.
9. **Strict Zero SQL Error Policy**:
   Check and guarantee 0 SQL errors in `server_backend/deenone_db.sql`, queries, and backend scripts. No syntax errors, mismatched columns, or missing table definitions are allowed.
10. **Mandatory 14-Point Full-Stack MySQL & PHP Admin Real-Time Auto-Sync Matrix**:
    The following 14 core features must be 100% connected to the live MySQL database (`deenonet_db`) and PHP Admin Panel (`server_backend/admin/`), auto-synced in real-time, and verified on every single build/implementation:
    1. **Community Tab (কমিউনিটি ট্যাব)**: Posts, likes, comments, reports via `api/community_feed.php` (`community_posts`, `community_likes`, `community_reports`). Moderated via `admin/community.php`.
    2. **Rank Section / Leaderboard (র‍্যাংক / লিডারবোর্ড)**: Live points ranking, user XP, badges via `api/leaderboard.php`, `api/quiz_leaderboard.php` (`users`).
    3. **Amal Points Collection (আমল পয়েন্ট কালেকশন)**: Daily deeds points auto-synced with `users.total_points` via `api/user_sync.php`.
    4. **Salah & Qaza Prayer Tracker (সালাত ও ক্বাযা নামাজ ট্র্যাকার)**: Prayer logs and qaza tracker auto-synced via `api/qaza.php` (`user_qaza_prayers`).
    5. **Amal Tracker (আমল ট্র্যাকার)**: Daily checklist and streak tracking auto-synced via `api/user_sync.php` (`users.daily_streak`, `user_amal_records`).
    6. **Knowledge Battle (নলেজ ব্যাটল)**: Real-time multiplayer rooms, questions, and live scoring via `api/create_room.php`, `api/join_room.php`, `api/get_room.php`, `api/submit_answer.php` (`battle_rooms`, `battle_players`, `battle_answers`).
    7. **Quiz Section (কুইজ সেকশন)**: Dynamic categories and questions from `api/get_quiz_categories.php`, `api/get_quiz_questions.php`, and points leaderboard via `api/quiz_leaderboard.php` (`quiz_categories`, `quiz_questions`, `user_quiz_results`). Moderated via `admin/quiz_questions.php`.
    8. **Hadith Section (হাদিস সেকশন)**: Authentic books, chapters, and hadiths loaded from MySQL via `api/get_hadith_categories.php`, `api/get_hadith_chapters.php`, `api/get_chapter_hadiths.php` (`hadith_books`, `hadith_chapters`, `hadiths`), auto-synced with SQLite cache.
    9. **Islamic Books Section (ইসলামিক বই সেকশন)**: Authentic books and PDF catalog from MySQL via `api/get_islamic_books.php` (`islamic_books`). Moderated via `admin/islamic_books.php`.
    10. **Dua Hub / Dua Vandar (দোয়া ভান্ডার)**: Duas, translations, audio, favorites via `api/get_duas.php` (`duas`, `dua_categories`, `user_dua_favorites`). Moderated via `admin/duas.php`.
    11. **Full User Profile (পূর্ণাঙ্গ প্রোফাইল)**: User details (name, phone, email, blood group, district, avatar, points) synced via `api/auth.php`, `api/user_sync.php` (`users`).
    12. **Blood Donation Network (রক্তদান / ব্লাড ডোনেশন)**: Donor registry, filtering, requests via `api/blood_donors.php` (`blood_donors`, `blood_requests`). Moderated via `admin/blood_donors.php`.
    13. **App Language & Theme Settings Sync (অ্যাপের ভাষা ও থিম সেটিংস সিঙ্ক)**: Language (`bn`/`en`) and theme (`dark`/`light`) auto-synced with `users.app_language` and `users.theme_mode` via `api/user_sync.php`.
    14. **Admin Panel Notifications (অ্যাডমিন প্যানেল নোটিফিকেশন)**: Broadcasts and announcements created in `admin/notifications.php` and fetched in real time via `api/get_notifications.php` (`push_notifications`, `app_notices`).
11. **Mandatory User Activity Persistence & Cross-Device Cloud Sync Policy (ইউজারের প্রতিটি অ্যাক্টিভিটি সংরক্ষণ ও ক্রস-ডিভাইস রিস্টোর নীতি)**:
    - Every user action (whether Salah was prayed or not, Jamaat/Ekaki/Deri status, Qaza calculation and increments/decrements, Quiz points earned, Amal checklist completions, and app/user settings like language, theme, prayer calculation method, juristic madhab, and notification preferences) must be automatically and immediately persisted to the MySQL database in real-time.
    - When a user logs out and later logs into the same device or a completely new device, the app must automatically query `api/user_sync.php?action=full_restore` and seamlessly import and restore all their historical prayer logs, Qaza counts, total/quiz XP, streaks, and settings into local Room SQLite & SharedPreferences without any data loss.
    - Real-time sync must be non-blocking and background-threaded to guarantee 60 FPS lag-free UI and zero SQL errors.
