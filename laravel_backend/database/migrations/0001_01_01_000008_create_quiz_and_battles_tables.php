<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('quiz_categories', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name_bn', 100);
            $table->string('name_en', 100);
            $table->text('description_bn')->nullable();
            $table->string('icon', 50)->nullable();
            $table->unsignedSmallInteger('display_order')->default(1);
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();
        });

        Schema::create('quiz_questions', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedInteger('category_id')->index();
            $table->text('question_bn');
            $table->text('question_en')->nullable();
            $table->string('option_a_bn', 255);
            $table->string('option_b_bn', 255);
            $table->string('option_c_bn', 255);
            $table->string('option_d_bn', 255);
            $table->char('correct_option', 1); // 'A', 'B', 'C', 'D'
            $table->text('explanation_bn')->nullable();
            $table->unsignedSmallInteger('points')->default(10);
            $table->string('difficulty', 20)->default('EASY');
            $table->boolean('is_active')->default(true)->index();
            $table->timestamps();

            $table->foreign('category_id')->references('id')->on('quiz_categories')->onDelete('cascade');
        });

        Schema::create('battle_rooms', function (Blueprint $table) {
            $table->string('room_code', 10)->primary();
            $table->string('host_user_id', 100)->index();
            $table->string('host_name', 100);
            $table->unsignedInteger('category_id')->nullable();
            $table->unsignedSmallInteger('question_count')->default(5);
            $table->unsignedSmallInteger('time_per_question')->default(15);
            $table->string('status', 20)->default('WAITING')->index(); // WAITING, IN_PROGRESS, COMPLETED, CANCELLED
            $table->string('winner_user_id', 100)->nullable();
            $table->timestamp('created_at')->useCurrent()->index();
        });

        Schema::create('battle_players', function (Blueprint $table) {
            $table->increments('id');
            $table->string('room_code', 10)->index();
            $table->string('user_id', 100)->index();
            $table->string('player_name', 100);
            $table->string('avatar', 50)->default('avatar_1');
            $table->integer('score')->default(0);
            $table->unsignedSmallInteger('correct_answers')->default(0);
            $table->boolean('is_ready')->default(false);
            $table->timestamp('joined_at')->useCurrent();

            $table->foreign('room_code')->references('room_code')->on('battle_rooms')->onDelete('cascade');
            $table->unique(['room_code', 'user_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('battle_players');
        Schema::dropIfExists('battle_rooms');
        Schema::dropIfExists('quiz_questions');
        Schema::dropIfExists('quiz_categories');
    }
};
