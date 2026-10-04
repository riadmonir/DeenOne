<?php

namespace App\Models;

use Filament\Models\Contracts\FilamentUser;
use Filament\Panel;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;

class AdminUser extends Authenticatable implements FilamentUser
{
    use HasFactory, Notifiable;

    protected $table = 'admin_users';

    protected $fillable = [
        'username',
        'password_hash',
        'full_name',
        'email',
        'role',
        'last_login',
    ];

    protected $hidden = [
        'password_hash',
    ];

    protected $casts = [
        'last_login' => 'datetime',
        'created_at' => 'datetime',
    ];

    public function getAuthPassword()
    {
        return $this->password_hash;
    }

    public function canAccessPanel(Panel $panel): bool
    {
        return in_array($this->role, ['SUPERADMIN', 'EDITOR', 'MODERATOR']);
    }

    public function isSuperAdmin(): bool
    {
        return $this->role === 'SUPERADMIN';
    }
}
