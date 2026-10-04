<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\FaraidRecord;
use Illuminate\Http\Request;

class FaraidController extends Controller
{
    /**
     * Save user inheritance distribution calculation record
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'user_id' => 'required|string',
            'deceased_gender' => 'required|string|in:MALE,FEMALE',
            'total_estate' => 'required|numeric',
            'total_property' => 'nullable|numeric',
            'heirs_summary' => 'nullable|string',
            'shares_json' => 'nullable|array',
            'notes' => 'nullable|string',
        ]);

        $record = FaraidRecord::create($validated);
        return $this->success($record, 'ফারায়েজ হিসাব সফলভাবে সংরক্ষিত হয়েছে।');
    }

    /**
     * Get saved inheritance records for a user
     */
    public function userRecords(Request $request, string $userId)
    {
        $records = FaraidRecord::where('user_id', $userId)->orderBy('created_at', 'desc')->get();
        return $this->success($records);
    }
}
