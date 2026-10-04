<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Symfony\Component\HttpFoundation\Response;

class CheckMaintenanceMode
{
    /**
     * Handle an incoming request.
     */
    public function handle(Request $request, Closure $next): Response
    {
        // Check if maintenance mode is enabled in app_settings table
        try {
            $maintenance = DB::table('app_settings')
                ->where('setting_key', 'maintenance_mode')
                ->value('setting_value');

            if ($maintenance === '1') {
                $message = DB::table('app_settings')
                    ->where('setting_key', 'maintenance_message')
                    ->value('setting_value') ?? 'সার্ভার রক্ষণাবেক্ষণের কাজ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।';

                return response()->json([
                    'success' => false,
                    'maintenance' => true,
                    'message' => $message,
                ], 503);
            }
        } catch (\Exception $e) {
            // If database is not ready or connection fails, proceed
        }

        return $next($request);
    }
}
