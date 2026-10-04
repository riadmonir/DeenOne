<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class QuizCategory extends Model
{
    use HasFactory;

    protected $table = 'quiz_categories';

    protected $fillable = [
        'name_bn',
        'name_en',
        'description_bn',
        'icon',
        'display_order',
        'is_active',
    ];

    protected $casts = [
        'display_order' => 'integer',
        'is_active' => 'boolean',
    ];

    public function questions()
    {
        return $this->hasMany(QuizQuestion::class, 'category_id');
    }
}
