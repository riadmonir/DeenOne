<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class AsmaulHusna extends Model
{
    use HasFactory;

    protected $table = 'asmaul_husna';

    protected $fillable = [
        'name_number',
        'arabic_name',
        'bangla_name',
        'english_name',
        'bangla_meaning',
        'english_meaning',
        'explanation_bn',
        'virtue_bn',
        'audio_url',
        'is_active',
    ];

    protected $casts = [
        'name_number' => 'integer',
        'is_active' => 'boolean',
    ];
}
