<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->string('user_id', 100)->primary();
            $table->string('name', 100);
            $table->string('email', 120)->nullable()->index();
            $table->string('phone', 30)->nullable()->index();
            $table->string('district', 60)->nullable()->index();
            $table->string('avatar', 50)->default('avatar_1');
            $table->unsignedInteger('points')->default(0)->index();
            $table->unsignedInteger('streak_count')->default(0);
            $table->timestamp('last_active')->nullable()->index();
            $table->boolean('is_banned')->default(false)->index();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
