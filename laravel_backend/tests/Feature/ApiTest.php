<?php

namespace Tests\Feature;

use App\Models\AppSetting;
use App\Models\BloodDonor;
use App\Models\QuranStage;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ApiTest extends TestCase
{
    use RefreshDatabase;

    protected function setUp(): void
    {
        parent::setUp();
        $this->seed();
    }

    public function test_remote_config_endpoint_returns_json(): void
    {
        $response = $this->getJson('/api/config');

        $response->assertStatus(200)
            ->assertJsonStructure([
                'success',
                'ads_enabled',
                'admob_banner_id',
                'admob_interstitial_id',
                'admob_native_id',
                'admob_rewarded_id',
                'ad_frequency_interval',
                'maintenance_mode',
            ]);
    }

    public function test_user_sync_returns_sanctum_token(): void
    {
        $payload = [
            'user_id' => 'test_user_123',
            'name' => 'আব্দুল্লাহ',
            'phone' => '+8801711223344',
            'district' => 'ঢাকা',
            'points' => 50,
        ];

        $response = $this->postJson('/api/auth/sync', $payload);

        $response->assertStatus(200)
            ->assertJson([
                'success' => true,
            ])
            ->assertJsonStructure([
                'data' => [
                    'token',
                    'user' => ['user_id', 'name'],
                ]
            ]);

        $this->assertDatabaseHas('users', ['user_id' => 'test_user_123']);
    }

    public function test_quran_journey_endpoint_returns_stages(): void
    {
        $response = $this->getJson('/api/quran/journey');

        $response->assertStatus(200)
            ->assertJson([
                'success' => true,
            ]);
    }

    public function test_battle_room_creation(): void
    {
        $payload = [
            'user_id' => 'host_user_99',
            'host_name' => 'তাহমিদ',
            'question_count' => 5,
        ];

        $response = $this->postJson('/api/battle/create', $payload);

        $response->assertStatus(200)
            ->assertJsonStructure([
                'data' => [
                    'room_code',
                    'room' => ['room_code', 'host_user_id', 'status'],
                ]
            ]);

        $roomCode = $response->json('data.room_code');
        $this->assertEquals(6, strlen($roomCode));
        $this->assertDatabaseHas('battle_rooms', ['room_code' => $roomCode]);
    }
}
