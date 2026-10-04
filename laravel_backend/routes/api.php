<?php

use App\Http\Controllers\Api\AuthSyncController;
use App\Http\Controllers\Api\BattleController;
use App\Http\Controllers\Api\BloodDonorController;
use App\Http\Controllers\Api\ConfigController;
use App\Http\Controllers\Api\DuaController;
use App\Http\Controllers\Api\FaraidController;
use App\Http\Controllers\Api\HadithController;
use App\Http\Controllers\Api\HajjController;
use App\Http\Controllers\Api\HalalFoodController;
use App\Http\Controllers\Api\MosqueController;
use App\Http\Controllers\Api\NotificationController;
use App\Http\Controllers\Api\QuranJourneyController;
use App\Http\Controllers\Api\QuizController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| DeenOne Mobile Application REST API Routes (v1)
|--------------------------------------------------------------------------
*/

Route::middleware(['force.json', 'check.maintenance'])->group(function () {
    // 1. Remote Configuration & Ad Control
    Route::get('/config', [ConfigController::class, 'index']);
    Route::get('/get_config.php', [ConfigController::class, 'index']); // Legacy fallback

    // 2. Authentication & Profile Sync
    Route::post('/auth/sync', [AuthSyncController::class, 'sync']);
    Route::post('/user_sync.php', [AuthSyncController::class, 'sync']); // Legacy fallback

    // 3. Quran Journey
    Route::get('/quran/journey', [QuranJourneyController::class, 'index']);
    Route::get('/quran_journey.php', [QuranJourneyController::class, 'index']); // Legacy fallback

    // 4. Hadith Collection
    Route::get('/hadiths/categories', [HadithController::class, 'categories']);
    Route::get('/hadiths', [HadithController::class, 'index']);
    Route::get('/get_hadiths.php', [HadithController::class, 'index']); // Legacy fallback

    // 5. Duas & Supplications
    Route::get('/duas/categories', [DuaController::class, 'categories']);
    Route::get('/duas', [DuaController::class, 'index']);
    Route::get('/get_duas.php', [DuaController::class, 'index']); // Legacy fallback

    // 6. Hajj & Umrah Journey
    Route::get('/hajj/stages', [HajjController::class, 'stages']);
    Route::get('/hajj/topics', [HajjController::class, 'topics']);
    Route::get('/hajj/article/{key}', [HajjController::class, 'article']);
    Route::get('/get_hajj_journey.php', [HajjController::class, 'stages']); // Legacy fallback
    Route::get('/get_hajj_articles.php', [HajjController::class, 'topics']); // Legacy fallback

    // 7. Blood Donors Directory
    Route::get('/blood-donors', [BloodDonorController::class, 'index']);
    Route::post('/blood-donors', [BloodDonorController::class, 'store']);
    Route::get('/blood_donors.php', [BloodDonorController::class, 'index']); // Legacy fallback

    // 8. Islamic Knowledge Battle
    Route::post('/battle/create', [BattleController::class, 'create']);
    Route::post('/battle/join', [BattleController::class, 'join']);
    Route::get('/battle/room/{roomCode}', [BattleController::class, 'show']);
    Route::post('/create_room.php', [BattleController::class, 'create']); // Legacy fallback
    Route::post('/join_room.php', [BattleController::class, 'join']); // Legacy fallback
    Route::get('/get_room.php', function (\Illuminate\Http\Request $request) {
        $code = $request->input('room_code');
        return app(BattleController::class)->show($code);
    }); // Legacy fallback

    // 9. Quiz Bank
    Route::get('/quiz/categories', [QuizController::class, 'categories']);
    Route::get('/quiz/questions', [QuizController::class, 'questions']);
    Route::get('/get_quiz_categories.php', [QuizController::class, 'categories']); // Legacy fallback
    Route::get('/get_quiz_questions.php', [QuizController::class, 'questions']); // Legacy fallback

    // 10. Notifications Inbox
    Route::get('/notifications', [NotificationController::class, 'index']);
    Route::get('/get_notifications.php', [NotificationController::class, 'index']); // Legacy fallback

    // 11. Mosques & Nearby
    Route::get('/mosques', [MosqueController::class, 'index']);
    Route::get('/mosques.php', [MosqueController::class, 'index']); // Legacy fallback

    // 12. Halal Food Places
    Route::get('/halal-foods', [HalalFoodController::class, 'index']);
    Route::get('/halal_foods.php', [HalalFoodController::class, 'index']); // Legacy fallback

    // 13. Faraid Inheritance Calculator
    Route::post('/faraid', [FaraidController::class, 'store']);
    Route::get('/faraid/user/{userId}', [FaraidController::class, 'userRecords']);
    Route::post('/faraid.php', [FaraidController::class, 'store']); // Legacy fallback

    // 14. Authenticated Protected Endpoints
    Route::middleware('auth:sanctum')->group(function () {
        Route::get('/user/profile', [AuthSyncController::class, 'profile']);
    });
});
