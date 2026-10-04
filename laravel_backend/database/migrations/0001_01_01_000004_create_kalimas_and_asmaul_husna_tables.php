<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('six_kalimas', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedSmallInteger('kalima_number')->unique();
            $table->string('title_bn', 100);
            $table->string('title_en', 100);
            $table->text('arabic_text');
            $table->text('transliteration_bn');
            $table->text('meaning_bn');
            $table->text('meaning_en')->nullable();
            $table->string('audio_url', 255)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('asmaul_husna', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedSmallInteger('name_number')->unique();
            $table->string('arabic_name', 100);
            $table->string('bangla_name', 100);
            $table->string('english_name', 100);
            $table->string('bangla_meaning', 255);
            $table->string('english_meaning', 255)->nullable();
            $table->text('explanation_bn')->nullable();
            $table->text('virtue_bn')->nullable();
            $table->string('audio_url', 255)->nullable();
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('asmaul_husna');
        Schema::dropIfExists('six_kalimas');
    }
};
