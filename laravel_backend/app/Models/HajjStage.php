<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HajjStage extends Model
{
    use HasFactory;

    protected $table = 'hajj_journey_stages';

    protected $fillable = [
        'stage_number',
        'day_title_bn',
        'day_title_en',
        'location_bn',
        'location_en',
        'description_bn',
        'description_en',
        'icon_name',
        'is_active',
    ];

    protected $casts = [
        'stage_number' => 'integer',
        'is_active' => 'boolean',
    ];
}
