<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class BattleRoom extends Model
{
    use HasFactory;

    protected $table = 'battle_rooms';

    protected $fillable = [
        'room_code',
        'host_user_id',
        'host_name',
        'category_id',
        'question_count',
        'time_per_question',
        'status', // WAITING, IN_PROGRESS, COMPLETED, CANCELLED
        'winner_user_id',
        'created_at',
    ];

    public $timestamps = false;

    protected $casts = [
        'category_id' => 'integer',
        'question_count' => 'integer',
        'time_per_question' => 'integer',
        'created_at' => 'datetime',
    ];

    public function players()
    {
        return $this->hasMany(BattlePlayer::class, 'room_code', 'room_code');
    }
}
