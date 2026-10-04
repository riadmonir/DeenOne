<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HajjArticle extends Model
{
    use HasFactory;

    protected $table = 'hajj_articles';

    protected $fillable = [
        'article_key',
        'title_bn',
        'title_en',
        'content_bn',
        'content_en',
        'category_tag',
        'image_url',
        'is_active',
    ];

    protected $casts = [
        'is_active' => 'boolean',
    ];
}
