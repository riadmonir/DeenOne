<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\HalalPlace;
use Illuminate\Http\Request;

class HalalFoodController extends Controller
{
    /**
     * Get halal restaurants and food places
     */
    public function index(Request $request)
    {
        $query = HalalPlace::where('is_active', true);

        if ($request->has('district') && !empty($request->input('district'))) {
            $query->where('district', $request->input('district'));
        }

        if ($request->has('category') && !empty($request->input('category'))) {
            $query->where('category', $request->input('category'));
        }

        $places = $query->orderBy('name', 'asc')->get();
        return $this->success($places);
    }
}
