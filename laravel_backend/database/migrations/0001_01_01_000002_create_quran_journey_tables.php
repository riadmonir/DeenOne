<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('quran_journey_stages', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedSmallInteger('stage_number')->unique();
            $table->string('title_bn', 100);
            $table->string('title_en', 100);
            $table->string('subtitle_bn', 150)->nullable();
            $table->string('subtitle_en', 150)->nullable();
            $table->string('badge_text_bn', 50)->nullable();
            $table->string('badge_text_en', 50)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('quran_journey_lessons', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedInteger('stage_id')->index();
            $table->unsignedSmallInteger('lesson_order')->default(1);
            $table->unsignedSmallInteger('surah_number');
            $table->string('surah_name_ar', 100);
            $table->string('surah_name_bn', 100);
            $table->string('surah_name_en', 100);
            $table->unsignedSmallInteger('ayah_start')->default(1);
            $table->unsignedSmallInteger('ayah_end')->default(1);
            $table->string('title_bn', 150);
            $table->string('title_en', 150);
            $table->text('description_bn')->nullable();
            $table->string('audio_url', 255)->nullable();
            $table->unsignedInteger('reward_points')->default(10);
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();

            $table->foreign('stage_id')->references('id')->on('quran_journey_stages')->onDelete('cascade');
            $table->index(['stage_id', 'lesson_order']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('quran_journey_lessons');
        Schema::dropIfExists('quran_journey_stages');
    }
};
