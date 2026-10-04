<?php

namespace App\Filament\Resources\PushNotificationResource\Pages;

use App\Filament\Resources\PushNotificationResource;
use App\Jobs\SendFcmBroadcastJob;
use Filament\Resources\Pages\CreateRecord;

class CreatePushNotification extends CreateRecord
{
    protected static string $resource = PushNotificationResource::class;

    protected function afterCreate(): void
    {
        // Immediately dispatch FCM notification to Redis queue
        dispatch(new SendFcmBroadcastJob($this->record));
    }
}
