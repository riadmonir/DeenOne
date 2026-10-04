<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Mosque extends Model
{
    use HasFactory;

    protected $table = 'mosques';

    protected $fillable = [
        'name',
        'address',
        'district',
        'latitude',
        'longitude',
        'has_wudu_facility',
        'has_female_prayer_space',
        'image_url',
        'is_verified',
    ];

    protected $casts = [
        'latitude' => 'float',
        'longitude' => 'float',
        'has_wudu_facility' => 'boolean',
        'has_female_prayer_space' => 'boolean',
        'is_verified' => 'boolean',
    ];
}
