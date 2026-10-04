<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\BloodDonor;
use Illuminate\Http\Request;

class BloodDonorController extends Controller
{
    /**
     * Search donors with blood group and district filters
     */
    public function index(Request $request)
    {
        $query = BloodDonor::where('is_available', true);

        if ($request->has('blood_group') && !empty($request->input('blood_group'))) {
            $query->where('blood_group', $request->input('blood_group'));
        }

        if ($request->has('district') && !empty($request->input('district'))) {
            $query->where('district', $request->input('district'));
        }

        $donors = $query->orderBy('is_verified', 'desc')->orderBy('name', 'asc')->get();
        return $this->success($donors);
    }

    /**
     * Register as voluntary blood donor
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:100',
            'blood_group' => 'required|string|max:10',
            'phone' => 'required|string|max:30|unique:blood_donors,phone',
            'district' => 'required|string|max:60',
            'area' => 'nullable|string|max:100',
        ]);

        $donor = BloodDonor::create([
            ...$validated,
            'is_available' => true,
            'is_verified' => false,
        ]);

        return $this->success($donor, 'রক্তদাতা হিসেবে সফলভাবে নিবন্ধিত হয়েছেন!');
    }
}
