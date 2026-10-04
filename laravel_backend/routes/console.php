<?php

use App\Jobs\CleanupExpiredRoomsJob;
use App\Models\AppSetting;
use App\Models\PushNotification;
use Illuminate\Support\Facades\Schedule;

/*
|--------------------------------------------------------------------------
| DeenOne Background Task Schedules (Laravel Scheduler)
|--------------------------------------------------------------------------
*/

// 1. Clean up expired multiplayer battle rooms every 15 minutes
Schedule::job(new CleanupExpiredRoomsJob)->everyFifteenMinutes();

// 2. Prune old inactive user tokens daily
Schedule::command('sanctum:prune-expired --hours=720')->daily();

// 3. Daily Morning Islamic Ayah Notification at 06:00 AM (Dhaka Time)
Schedule::call(function () {
    $dailyAyah = AppSetting::get('daily_ayah_text');
    if (!empty($dailyAyah)) {
        $notif = PushNotification::create([
            'title' => 'আজকের বরকতময় আয়াত',
            'body' => $dailyAyah,
            'notification_type' => 'DAILY_AYAH',
            'target_action' => 'feature_quran',
            'priority' => 'HIGH',
            'target_audience' => 'ALL',
            'created_by' => 'scheduler',
        ]);
        dispatch(new \App\Jobs\SendFcmBroadcastJob($notif));
    }
})->dailyAt('06:00')->timezone('Asia/Dhaka');

// 4. Daily Evening Islamic Hadith Notification at 08:00 PM (Dhaka Time)
Schedule::call(function () {
    $dailyHadith = AppSetting::get('daily_hadith_text');
    if (!empty($dailyHadith)) {
        $notif = PushNotification::create([
            'title' => 'দৈনিক নির্বাচিত হাদীস',
            'body' => $dailyHadith,
            'notification_type' => 'DAILY_HADITH',
            'target_action' => 'feature_hadith',
            'priority' => 'HIGH',
            'target_audience' => 'ALL',
            'created_by' => 'scheduler',
        ]);
        dispatch(new \App\Jobs\SendFcmBroadcastJob($notif));
    }
})->dailyAt('20:00')->timezone('Asia/Dhaka');
