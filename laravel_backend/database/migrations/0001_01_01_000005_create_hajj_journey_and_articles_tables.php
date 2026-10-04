<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('hajj_journey_stages', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedSmallInteger('stage_number')->unique();
            $table->string('day_title_bn', 100);
            $table->string('day_title_en', 100);
            $table->string('location_bn', 100);
            $table->string('location_en', 100);
            $table->text('description_bn');
            $table->text('description_en')->nullable();
            $table->string('icon_name', 50)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('hajj_journey_topics', function (Blueprint $table) {
            $table->increments('id');
            $table->string('topic_key', 50)->unique();
            $table->string('title_bn', 100);
            $table->string('title_en', 100);
            $table->string('subtitle_bn', 150)->nullable();
            $table->string('subtitle_en', 150)->nullable();
            $table->string('icon_name', 50)->nullable();
            $table->unsignedSmallInteger('display_order')->default(1);
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('hajj_articles', function (Blueprint $table) {
            $table->increments('id');
            $table->string('article_key', 50)->unique();
            $table->string('title_bn', 150);
            $table->string('title_en', 150);
            $table->longText('content_bn');
            $table->longText('content_en')->nullable();
            $table->string('category_tag', 50)->default('guide');
            $table->string('image_url', 255)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('hajj_articles');
        Schema::dropIfExists('hajj_journey_topics');
        Schema::dropIfExists('hajj_journey_stages');
    }
};
