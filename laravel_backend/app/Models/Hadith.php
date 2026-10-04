<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Hadith extends Model
{
    use HasFactory;

    protected $table = 'hadiths';

    protected $fillable = [
        'category_id',
        'book_name_bn',
        'book_name_en',
        'hadith_number',
        'chapter_bn',
        'chapter_en',
        'narrator_bn',
        'narrator_en',
        'arabic_text',
        'bangla_meaning',
        'english_meaning',
        'grade',
        'is_active',
    ];

    protected $casts = [
        'category_id' => 'integer',
        'is_active' => 'boolean',
    ];

    public function category()
    {
        return $this->belongsTo(HadithCategory::class, 'category_id');
    }
}
