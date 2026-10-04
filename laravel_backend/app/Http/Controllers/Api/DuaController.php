<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Dua;
use App\Models\DuaCategory;
use Illuminate\Http\Request;

class DuaController extends Controller
{
    /**
     * Get dua categories
     */
    public function categories()
    {
        $categories = DuaCategory::where('is_active', true)->orderBy('display_order', 'asc')->get();
        return $this->success($categories);
    }

    /**
     * Get duas with optional category filter
     */
    public function index(Request $request)
    {
        $query = Dua::where('is_active', true);

        if ($request->has('category_id')) {
            $query->where('category_id', $request->input('category_id'));
        }

        if ($request->has('search')) {
            $s = $request->input('search');
            $query->where(function ($q) use ($s) {
                $q->where('title_bn', 'like', "%{$s}%")
                  ->orWhere('meaning_bn', 'like', "%{$s}%");
            });
        }

        $duas = $query->get();
        return $this->success($duas);
    }
}
