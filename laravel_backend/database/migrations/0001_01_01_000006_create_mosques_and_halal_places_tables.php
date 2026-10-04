<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('mosques', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name', 150);
            $table->string('address', 255);
            $table->string('district', 60)->index();
            $table->decimal('latitude', 10, 7)->index();
            $table->decimal('longitude', 10, 7)->index();
            $table->boolean('has_wudu_facility')->default(true);
            $table->boolean('has_female_prayer_space')->default(false);
            $table->string('image_url', 255)->nullable();
            $table->boolean('is_verified')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('halal_places', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name', 150);
            $table->string('category', 50)->default('restaurant')->index();
            $table->string('address', 255);
            $table->string('district', 60)->index();
            $table->decimal('latitude', 10, 7)->index();
            $table->decimal('longitude', 10, 7)->index();
            $table->string('certification_status', 50)->default('VERIFIED');
            $table->string('phone', 30)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('halal_places');
        Schema::dropIfExists('mosques');
    }
};
