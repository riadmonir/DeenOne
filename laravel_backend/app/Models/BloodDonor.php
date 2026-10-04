<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class BloodDonor extends Model
{
    use HasFactory;

    protected $table = 'blood_donors';

    protected $fillable = [
        'name',
        'blood_group',
        'phone',
        'district',
        'area',
        'last_donation_date',
        'is_available',
        'is_verified',
    ];

    protected $casts = [
        'is_available' => 'boolean',
        'is_verified' => 'boolean',
        'last_donation_date' => 'date',
    ];
}
