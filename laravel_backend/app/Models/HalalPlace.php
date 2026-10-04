<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HalalPlace extends Model
{
    use HasFactory;

    protected $table = 'halal_places';

    protected $fillable = [
        'name',
        'category',
        'address',
        'district',
        'latitude',
        'longitude',
        'certification_status',
        'phone',
        'is_active',
    ];

    protected $casts = [
        'latitude' => 'float',
        'longitude' => 'float',
        'is_active' => 'boolean',
    ];
}
