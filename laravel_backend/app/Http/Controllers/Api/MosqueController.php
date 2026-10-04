<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Mosque;
use Illuminate\Http\Request;

class MosqueController extends Controller
{
    /**
     * Get nearby mosques or search by district
     */
    public function index(Request $request)
    {
        $query = Mosque::where('is_verified', true);

        if ($request->has('district') && !empty($request->input('district'))) {
            $query->where('district', $request->input('district'));
        }

        // If GPS coordinates provided, sort by Haversine distance
        if ($request->has('lat') && $request->has('lng')) {
            $lat = (float) $request->input('lat');
            $lng = (float) $request->input('lng');
            $query->selectRaw("*, (6371 * acos(cos(radians(?)) * cos(radians(latitude)) * cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) AS distance", [$lat, $lng, $lat])
                  ->orderBy('distance', 'asc');
        } else {
            $query->orderBy('name', 'asc');
        }

        $mosques = $query->limit(50)->get();
        return $this->success($mosques);
    }
}
