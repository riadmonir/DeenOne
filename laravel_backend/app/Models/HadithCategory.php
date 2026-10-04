<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HadithCategory extends Model
{
    use HasFactory;

    protected $table = 'hadith_categories';

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

    public function hadiths()
    {
        return $this->hasMany(Hadith::class, 'category_id');
    }
}
