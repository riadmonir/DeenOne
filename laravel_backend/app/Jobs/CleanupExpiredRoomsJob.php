<?php

namespace App\Jobs;

use App\Models\BattleRoom;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Log;

class CleanupExpiredRoomsJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    /**
     * Delete waiting or abandoned rooms older than 2 hours
     */
    public function handle(): void
    {
        $deleted = BattleRoom::where('status', 'WAITING')
            ->where('created_at', '<', now()->subHours(2))
            ->delete();

        if ($deleted > 0) {
            Log::info("Cleaned up {$deleted} expired battle rooms.");
        }
    }
}
