<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;

class AuthSyncController extends Controller
{
    /**
     * Sync user profile from mobile app and return Sanctum token
     */
    public function sync(Request $request)
    {
        $validated = $request->validate([
            'user_id' => 'required|string|max:100',
            'name' => 'required|string|max:100',
            'email' => 'nullable|email|max:120',
            'phone' => 'nullable|string|max:30',
            'district' => 'nullable|string|max:60',
            'avatar' => 'nullable|string|max:50',
            'points' => 'nullable|integer',
            'streak_count' => 'nullable|integer',
        ]);

        $user = User::updateOrCreate(
            ['user_id' => $validated['user_id']],
            [
                'name' => $validated['name'],
                'email' => $validated['email'] ?? null,
                'phone' => $validated['phone'] ?? null,
                'district' => $validated['district'] ?? null,
                'avatar' => $validated['avatar'] ?? 'avatar_1',
                'points' => $validated['points'] ?? 0,
                'streak_count' => $validated['streak_count'] ?? 0,
                'last_active' => now(),
            ]
        );

        // Revoke old tokens and issue fresh Sanctum personal access token
        $user->tokens()->delete();
        $token = $user->createToken('deenone_mobile_app')->plainTextToken;

        return $this->success([
            'token' => $token,
            'user' => $user,
        ], 'User profile synchronized successfully');
    }

    /**
     * Get authenticated user profile
     */
    public function profile(Request $request)
    {
        return $this->success($request->user());
    }
}
