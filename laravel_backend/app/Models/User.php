<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    use HasApiTokens, HasFactory, Notifiable;

    protected $primaryKey = 'user_id';
    public $incrementing = false;
    protected $keyType = 'string';

    protected $fillable = [
        'user_id',
        'name',
        'email',
        'phone',
        'district',
        'avatar',
        'points',
        'streak_count',
        'last_active',
        'is_banned',
    ];

    protected $casts = [
        'points' => 'integer',
        'streak_count' => 'integer',
        'is_banned' => 'boolean',
        'last_active' => 'datetime',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];
}
