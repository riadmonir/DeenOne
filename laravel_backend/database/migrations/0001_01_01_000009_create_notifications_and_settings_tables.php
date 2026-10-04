<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('push_notifications', function (Blueprint $table) {
            $table->increments('id');
            $table->string('title', 150);
            $table->text('body');
            $table->string('notification_type', 50)->default('ANNOUNCEMENT');
            $table->string('target_action', 100)->default('default');
            $table->string('target_url', 255)->nullable();
            $table->string('image_url', 255)->nullable();
            $table->string('priority', 20)->default('HIGH');
            $table->string('target_audience', 50)->default('ALL');
            $table->string('fcm_status', 20)->default('SENT');
            $table->unsignedInteger('sent_count')->default(0);
            $table->string('created_by', 50)->default('admin');
            $table->timestamps();
        });

        Schema::create('app_settings', function (Blueprint $table) {
            $table->string('setting_key', 64)->primary();
            $table->longText('setting_value')->nullable();
            $table->string('description', 255)->nullable();
            $table->timestamp('updated_at')->useCurrent()->useCurrentOnUpdate();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('app_settings');
        Schema::dropIfExists('push_notifications');
    }
};
