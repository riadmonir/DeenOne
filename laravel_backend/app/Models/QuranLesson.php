<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class QuranLesson extends Model
{
    use HasFactory;

    protected $table = 'quran_journey_lessons';

    protected $fillable = [
        'stage_id',
        'lesson_order',
        'surah_number',
        'surah_name_ar',
        'surah_name_bn',
        'surah_name_en',
        'ayah_start',
        'ayah_end',
        'title_bn',
        'title_en',
        'description_bn',
        'audio_url',
        'reward_points',
        'is_active',
    ];

    protected $casts = [
        'stage_id' => 'integer',
        'lesson_order' => 'integer',
        'surah_number' => 'integer',
        'ayah_start' => 'integer',
        'ayah_end' => 'integer',
        'reward_points' => 'integer',
        'is_active' => 'boolean',
    ];

    public function stage()
    {
        return $this->belongsTo(QuranStage::class, 'stage_id');
    }
}
