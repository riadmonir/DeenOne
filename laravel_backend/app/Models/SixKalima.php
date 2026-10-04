<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class SixKalima extends Model
{
    use HasFactory;

    protected $table = 'six_kalimas';

    protected $fillable = [
        'kalima_number',
        'title_bn',
        'title_en',
        'arabic_text',
        'transliteration_bn',
        'meaning_bn',
        'meaning_en',
        'audio_url',
        'is_active',
    ];

    protected $casts = [
        'kalima_number' => 'integer',
        'is_active' => 'boolean',
    ];
}
