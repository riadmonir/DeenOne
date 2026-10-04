<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class AdminLog extends Model
{
    use HasFactory;

    protected $table = 'admin_logs';
    public $timestamps = false;

    protected $fillable = [
        'admin_id',
        'action',
        'target_type',
        'target_id',
        'details',
        'ip_address',
        'created_at',
    ];

    protected $casts = [
        'admin_id' => 'integer',
        'created_at' => 'datetime',
    ];

    public function admin()
    {
        return $this->belongsTo(AdminUser::class, 'admin_id');
    }
}
