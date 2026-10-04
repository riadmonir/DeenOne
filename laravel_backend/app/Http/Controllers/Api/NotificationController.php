<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\PushNotification;
use Illuminate\Http\Request;

class NotificationController extends Controller
{
    /**
     * Get user notification inbox
     */
    public function index()
    {
        $notifications = PushNotification::where('fcm_status', 'SENT')
            ->orderBy('created_at', 'desc')
            ->limit(30)
            ->get();

        return $this->success($notifications);
    }
}
