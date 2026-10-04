<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class DuaCategory extends Model
{
    use HasFactory;

    protected $table = 'dua_categories';

    protected $fillable = [
        'name_bn',
        'name_en',
        'icon',
        'display_order',
        'is_active',
    ];

    protected $casts = [
        'display_order' => 'integer',
        'is_active' => 'boolean',
    ];

    public function duas()
    {
        return $this->hasMany(Dua::class, 'category_id');
    }
}
