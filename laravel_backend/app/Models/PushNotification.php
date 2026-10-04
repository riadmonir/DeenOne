<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PushNotification extends Model
{
    use HasFactory;

    protected $table = 'push_notifications';

    protected $fillable = [
        'title',
        'body',
        'notification_type', // ANNOUNCEMENT, PRAYER_ALERT, APP_UPDATE, NEW_FEATURE
        'target_action',
        'target_url',
        'image_url',
        'priority', // CRITICAL, HIGH, NORMAL, LOW
        'target_audience', // ALL, BLOOD_DONORS, DISTRICT
        'fcm_status', // PENDING, SENT, FAILED
        'sent_count',
        'created_by',
    ];

    protected $casts = [
        'sent_count' => 'integer',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];
}
