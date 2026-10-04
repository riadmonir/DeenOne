<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Dua extends Model
{
    use HasFactory;

    protected $table = 'duas';

    protected $fillable = [
        'category_id',
        'title_bn',
        'title_en',
        'arabic_text',
        'transliteration_bn',
        'transliteration_en',
        'meaning_bn',
        'meaning_en',
        'virtue_bn',
        'virtue_en',
        'reference_bn',
        'reference_en',
        'audio_url',
        'is_active',
    ];

    protected $casts = [
        'category_id' => 'integer',
        'is_active' => 'boolean',
    ];

    public function category()
    {
        return $this->belongsTo(DuaCategory::class, 'category_id');
    }
}
