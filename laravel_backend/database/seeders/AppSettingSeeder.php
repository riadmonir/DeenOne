<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class AppSettingSeeder extends Seeder
{
    public function run(): void
    {
        $settings = [
            'ads_enabled' => '1',
            'admob_banner_id' => 'ca-app-pub-3940256099942544/6300978111',
            'admob_interstitial_id' => 'ca-app-pub-3940256099942544/1033173712',
            'admob_native_id' => 'ca-app-pub-3940256099942544/2247696110',
            'admob_rewarded_id' => 'ca-app-pub-3940256099942544/5224354917',
            'ad_frequency_interval' => '5',
            'maintenance_mode' => '0',
            'maintenance_message' => 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।',
            'min_app_version' => '1',
            'latest_app_version' => '1',
            'force_update' => '0',
            'update_url' => 'https://play.google.com/store/apps/details?id=com.devflux.deenone',
            'announcement_active' => '1',
            'announcement_banner_text' => 'দীন ওয়ান ইসলামিক নলেজ ব্যাটেলে আপনাকে স্বাগতম!',
            'announcement_deep_link' => 'feature_history',
            'daily_ayah_text' => 'বলুন, আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করুন। (সূরা ত্বহা: ১১৪)',
            'daily_hadith_text' => 'যে ব্যক্তি ইলম অন্বেষণে কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সহজ করে দেন। (সহীহ মুসলিম)',
            'custom_server_active' => '1',
        ];

        foreach ($settings as $key => $value) {
            DB::table('app_settings')->updateOrInsert(
                ['setting_key' => $key],
                ['setting_value' => $value, 'description' => 'System default setting']
            );
        }
    }
}
