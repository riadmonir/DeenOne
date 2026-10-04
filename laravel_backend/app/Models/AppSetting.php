<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class AppSetting extends Model
{
    protected $table = 'app_settings';
    public $timestamps = false;

    protected $fillable = [
        'setting_key',
        'setting_value',
        'description',
    ];

    public static function get(string $key, $default = null)
    {
        $setting = static::where('setting_key', $key)->first();
        return $setting ? $setting->setting_value : $default;
    }

    public static function set(string $key, $value, ?string $description = null)
    {
        return static::updateOrCreate(
            ['setting_key' => $key],
            ['setting_value' => $value, 'description' => $description]
        );
    }
}
