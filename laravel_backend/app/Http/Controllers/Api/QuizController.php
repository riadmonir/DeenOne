<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\QuizCategory;
use App\Models\QuizQuestion;
use Illuminate\Http\Request;

class QuizController extends Controller
{
    /**
     * Get quiz categories
     */
    public function categories()
    {
        $categories = QuizCategory::withCount(['questions' => function ($q) {
            $q->where('is_active', true);
        }])->where('is_active', true)->orderBy('display_order', 'asc')->get();

        return $this->success($categories);
    }

    /**
     * Get questions for category
     */
    public function questions(Request $request)
    {
        $categoryId = $request->input('category_id');
        $limit = min(20, (int)($request->input('limit', 10)));

        $query = QuizQuestion::where('is_active', true);
        if ($categoryId) {
            $query->where('category_id', $categoryId);
        }

        $questions = $query->inRandomOrder()->limit($limit)->get();
        return $this->success($questions);
    }
}
