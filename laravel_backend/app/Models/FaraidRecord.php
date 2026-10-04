<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class FaraidRecord extends Model
{
    use HasFactory;

    protected $table = 'user_faraid_records';

    protected $fillable = [
        'user_id',
        'deceased_gender',
        'total_estate',
        'total_property',
        'heirs_summary',
        'shares_json',
        'notes',
    ];

    protected $casts = [
        'total_estate' => 'float',
        'total_property' => 'float',
        'shares_json' => 'array',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];
}
