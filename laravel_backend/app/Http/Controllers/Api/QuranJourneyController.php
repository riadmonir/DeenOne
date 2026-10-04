<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\QuranStage;
use Illuminate\Http\Request;

class QuranJourneyController extends Controller
{
    /**
     * Get all active stages and lessons for Quran Journey
     */
    public function index()
    {
        $stages = QuranStage::with(['lessons' => function ($query) {
            $query->where('is_active', true)->orderBy('lesson_order', 'asc');
        }])
        ->where('is_active', true)
        ->orderBy('stage_number', 'asc')
        ->get();

        return $this->success($stages, 'Quran Journey stages retrieved successfully');
    }
}
