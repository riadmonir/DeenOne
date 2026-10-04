<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class BattlePlayer extends Model
{
    use HasFactory;

    protected $table = 'battle_players';
    public $timestamps = false;

    protected $fillable = [
        'room_code',
        'user_id',
        'player_name',
        'avatar',
        'score',
        'correct_answers',
        'is_ready',
        'joined_at',
    ];

    protected $casts = [
        'score' => 'integer',
        'correct_answers' => 'integer',
        'is_ready' => 'boolean',
        'joined_at' => 'datetime',
    ];

    public function room()
    {
        return $this->belongsTo(BattleRoom::class, 'room_code', 'room_code');
    }
}
