<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Hadith;
use App\Models\HadithCategory;
use Illuminate\Http\Request;

class HadithController extends Controller
{
    /**
     * Get hadith categories
     */
    public function categories()
    {
        $categories = HadithCategory::where('is_active', true)->orderBy('display_order', 'asc')->get();
        return $this->success($categories);
    }

    /**
     * Get hadiths with optional category or search filter
     */
    public function index(Request $request)
    {
        $query = Hadith::where('is_active', true);

        if ($request->has('category_id')) {
            $query->where('category_id', $request->input('category_id'));
        }

        if ($request->has('search')) {
            $s = $request->input('search');
            $query->where(function ($q) use ($s) {
                $q->where('bangla_meaning', 'like', "%{$s}%")
                  ->orWhere('narrator_bn', 'like', "%{$s}%")
                  ->orWhere('hadith_number', 'like', "%{$s}%");
            });
        }

        $hadiths = $query->paginate($request->input('per_page', 20));
        return $this->success($hadiths);
    }
}
