<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\BattlePlayer;
use App\Models\BattleRoom;
use App\Models\QuizQuestion;
use Illuminate\Http\Request;
use Illuminate\Support\Str;

class BattleController extends Controller
{
    /**
     * Create new battle room
     */
    public function create(Request $request)
    {
        $validated = $request->validate([
            'user_id' => 'required|string',
            'host_name' => 'required|string',
            'category_id' => 'nullable|integer',
            'question_count' => 'nullable|integer|min:3|max:15',
            'time_per_question' => 'nullable|integer|min:10|max:30',
        ]);

        $roomCode = strtoupper(Str::random(6));

        $room = BattleRoom::create([
            'room_code' => $roomCode,
            'host_user_id' => $validated['user_id'],
            'host_name' => $validated['host_name'],
            'category_id' => $validated['category_id'] ?? null,
            'question_count' => $validated['question_count'] ?? 5,
            'time_per_question' => $validated['time_per_question'] ?? 15,
            'status' => 'WAITING',
            'created_at' => now(),
        ]);

        BattlePlayer::create([
            'room_code' => $roomCode,
            'user_id' => $validated['user_id'],
            'player_name' => $validated['host_name'],
            'avatar' => 'avatar_1',
            'score' => 0,
            'is_ready' => true,
        ]);

        return $this->success([
            'room_code' => $roomCode,
            'room' => $room->load('players'),
        ], 'Battle room created successfully');
    }

    /**
     * Join an existing battle room
     */
    public function join(Request $request)
    {
        $validated = $request->validate([
            'room_code' => 'required|string',
            'user_id' => 'required|string',
            'player_name' => 'required|string',
            'avatar' => 'nullable|string',
        ]);

        $room = BattleRoom::where('room_code', $validated['room_code'])->first();
        if (!$room) {
            return $this->error('ব্যাটেল রুম পাওয়া যায়নি!', 404);
        }

        if ($room->status === 'COMPLETED' || $room->status === 'CANCELLED') {
            return $this->error('এই ব্যাটেল রুমটি ইতিমধ্যেই সমাপ্ত হয়েছে!', 400);
        }

        $player = BattlePlayer::updateOrCreate(
            ['room_code' => $room->room_code, 'user_id' => $validated['user_id']],
            [
                'player_name' => $validated['player_name'],
                'avatar' => $validated['avatar'] ?? 'avatar_1',
                'is_ready' => true,
            ]
        );

        return $this->success([
            'room' => $room->fresh()->load('players'),
        ], 'ব্যাটেল রুমে সফলভাবে যুক্ত হয়েছেন!');
    }

    /**
     * Get live battle room state
     */
    public function show(string $roomCode)
    {
        $room = BattleRoom::with('players')->where('room_code', $roomCode)->first();
        if (!$room) {
            return $this->error('রুম পাওয়া যায়নি', 404);
        }
        return $this->success($room);
    }
}
