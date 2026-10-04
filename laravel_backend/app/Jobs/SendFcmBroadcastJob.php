<?php

namespace App\Jobs;

use App\Models\AppSetting;
use App\Models\PushNotification;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

class SendFcmBroadcastJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    public $notification;

    /**
     * Create a new job instance.
     */
    public function __construct(PushNotification $notification)
    {
        $this->notification = $notification;
    }

    /**
     * Execute the job to dispatch FCM notification asynchronously via Redis Queue
     */
    public function handle(): void
    {
        $fcmServerKey = AppSetting::get('fcm_server_key');
        if (empty($fcmServerKey)) {
            Log::warning("FCM Broadcast skipped: Server Key not configured for Notification #{$this->notification->id}");
            return;
        }

        $topic = '/topics/all_users';
        if ($this->notification->target_audience === 'BLOOD_DONORS') {
            $topic = '/topics/blood_donors';
        }

        $payload = [
            'to' => $topic,
            'priority' => in_array($this->notification->priority, ['CRITICAL', 'HIGH']) ? 'high' : 'normal',
            'notification' => [
                'title' => $this->notification->title,
                'body' => $this->notification->body,
                'sound' => 'default',
                'image' => $this->notification->image_url,
            ],
            'data' => [
                'id' => (string) $this->notification->id,
                'title' => $this->notification->title,
                'body' => $this->notification->body,
                'type' => $this->notification->notification_type,
                'deep_link' => $this->notification->target_action,
                'target_url' => $this->notification->target_url,
                'priority' => strtolower($this->notification->priority),
                'timestamp' => (string) time(),
            ]
        ];

        try {
            $response = Http::withHeaders([
                'Authorization' => 'key=' . trim($fcmServerKey),
                'Content-Type' => 'application/json',
            ])->post('https://fcm.googleapis.com/fcm/send', $payload);

            if ($response->successful()) {
                $this->notification->update([
                    'fcm_status' => 'SENT',
                    'sent_count' => $this->notification->sent_count + 1,
                ]);
                Log::info("FCM Notification #{$this->notification->id} broadcasted successfully to {$topic}");
            } else {
                $this->notification->update(['fcm_status' => 'FAILED']);
                Log::error("FCM Notification failed: " . $response->body());
            }
        } catch (\Exception $e) {
            $this->notification->update(['fcm_status' => 'FAILED']);
            Log::error("FCM Exception: " . $e->getMessage());
        }
    }
}
