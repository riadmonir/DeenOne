<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('user_faraid_records', function (Blueprint $table) {
            $table->increments('id');
            $table->string('user_id', 100)->index();
            $table->string('deceased_gender', 10); // MALE, FEMALE
            $table->decimal('total_estate', 15, 2)->default(0.00);
            $table->decimal('total_property', 15, 2)->default(0.00);
            $table->text('heirs_summary')->nullable();
            $table->json('shares_json')->nullable();
            $table->text('notes')->nullable();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('user_faraid_records');
    }
};
