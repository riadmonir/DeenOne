<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HajjTopic extends Model
{
    use HasFactory;

    protected $table = 'hajj_journey_topics';

    protected $fillable = [
        'topic_key',
        'title_bn',
        'title_en',
        'subtitle_bn',
        'subtitle_en',
        'icon_name',
        'display_order',
        'is_active',
    ];

    protected $casts = [
        'display_order' => 'integer',
        'is_active' => 'boolean',
    ];
}
