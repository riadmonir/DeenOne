<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class QuranStage extends Model
{
    use HasFactory;

    protected $table = 'quran_journey_stages';

    protected $fillable = [
        'stage_number',
        'title_bn',
        'title_en',
        'subtitle_bn',
        'subtitle_en',
        'badge_text_bn',
        'badge_text_en',
        'is_active',
    ];

    protected $casts = [
        'stage_number' => 'integer',
        'is_active' => 'boolean',
    ];

    public function lessons()
    {
        return $this->hasMany(QuranLesson::class, 'stage_id')->orderBy('lesson_order', 'asc');
    }
}
