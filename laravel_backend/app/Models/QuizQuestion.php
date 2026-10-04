<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class QuizQuestion extends Model
{
    use HasFactory;

    protected $table = 'quiz_questions';

    protected $fillable = [
        'category_id',
        'question_bn',
        'question_en',
        'option_a_bn',
        'option_b_bn',
        'option_c_bn',
        'option_d_bn',
        'correct_option',
        'explanation_bn',
        'points',
        'difficulty',
        'is_active',
    ];

    protected $casts = [
        'category_id' => 'integer',
        'points' => 'integer',
        'is_active' => 'boolean',
    ];

    public function category()
    {
        return $this->belongsTo(QuizCategory::class, 'category_id');
    }
}
