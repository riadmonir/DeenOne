<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('hadith_categories', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name_bn', 100);
            $table->string('name_en', 100);
            $table->string('icon', 50)->nullable();
            $table->unsignedSmallInteger('display_order')->default(1);
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('hadiths', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedInteger('category_id')->nullable()->index();
            $table->string('book_name_bn', 100);
            $table->string('book_name_en', 100);
            $table->string('hadith_number', 50)->nullable()->index();
            $table->string('chapter_bn', 150)->nullable();
            $table->string('chapter_en', 150)->nullable();
            $table->string('narrator_bn', 150)->nullable();
            $table->string('narrator_en', 150)->nullable();
            $table->text('arabic_text');
            $table->text('bangla_meaning');
            $table->text('english_meaning')->nullable();
            $table->string('grade', 50)->default('Sahih');
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();

            $table->foreign('category_id')->references('id')->on('hadith_categories')->onDelete('set null');
        });

        Schema::create('dua_categories', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name_bn', 100);
            $table->string('name_en', 100);
            $table->string('icon', 50)->nullable();
            $table->unsignedSmallInteger('display_order')->default(1);
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('duas', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedInteger('category_id')->nullable()->index();
            $table->string('title_bn', 150);
            $table->string('title_en', 150);
            $table->text('arabic_text');
            $table->text('transliteration_bn')->nullable();
            $table->text('transliteration_en')->nullable();
            $table->text('meaning_bn');
            $table->text('meaning_en')->nullable();
            $table->text('virtue_bn')->nullable();
            $table->text('virtue_en')->nullable();
            $table->string('reference_bn', 150)->nullable();
            $table->string('reference_en', 150)->nullable();
            $table->string('audio_url', 255)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();

            $table->foreign('category_id')->references('id')->on('dua_categories')->onDelete('set null');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('duas');
        Schema::dropIfExists('dua_categories');
        Schema::dropIfExists('hadiths');
        Schema::dropIfExists('hadith_categories');
    }
};
