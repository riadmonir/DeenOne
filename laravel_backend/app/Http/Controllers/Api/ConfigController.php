<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\AppSetting;
use Illuminate\Http\Request;

class ConfigController extends Controller
{
    /**
     * Get dynamic remote configuration and AdMob settings
     */
    public function index()
    {
        $settings = AppSetting::all()->pluck('setting_value', 'setting_key')->toArray();

        return response()->json([
            'success' => true,
            'ads_enabled' => ($settings['ads_enabled'] ?? '1') === '1',
            'admob_banner_id' => $settings['admob_banner_id'] ?? 'ca-app-pub-3940256099942544/6300978111',
            'admob_interstitial_id' => $settings['admob_interstitial_id'] ?? 'ca-app-pub-3940256099942544/1033173712',
            'admob_native_id' => $settings['admob_native_id'] ?? 'ca-app-pub-3940256099942544/2247696110',
            'admob_rewarded_id' => $settings['admob_rewarded_id'] ?? 'ca-app-pub-3940256099942544/5224354917',
            'ad_frequency_interval' => (int)($settings['ad_frequency_interval'] ?? 5),
            'maintenance_mode' => ($settings['maintenance_mode'] ?? '0') === '1',
            'maintenance_message' => $settings['maintenance_message'] ?? 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে।',
            'min_app_version' => (int)($settings['min_app_version'] ?? 1),
            'latest_app_version' => (int)($settings['latest_app_version'] ?? 1),
            'force_update' => ($settings['force_update'] ?? '0') === '1',
            'update_url' => $settings['update_url'] ?? 'https://play.google.com/store/apps/details?id=com.devflux.deenone',
            'announcement_active' => ($settings['announcement_active'] ?? '1') === '1',
            'announcement_banner_text' => $settings['announcement_banner_text'] ?? '',
            'announcement_deep_link' => $settings['announcement_deep_link'] ?? 'feature_history',
            'daily_ayah_text' => $settings['daily_ayah_text'] ?? '',
            'daily_hadith_text' => $settings['daily_hadith_text'] ?? '',
        ], 200, ['Content-Type' => 'application/json; charset=UTF-8'], JSON_UNESCAPED_UNICODE);
    }
}
