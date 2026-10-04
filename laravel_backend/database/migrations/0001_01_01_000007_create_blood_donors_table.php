<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('blood_donors', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name', 100);
            $table->string('blood_group', 10)->index();
            $table->string('phone', 30)->unique();
            $table->string('district', 60)->index();
            $table->string('area', 100)->nullable();
            $table->date('last_donation_date')->nullable();
            $table->boolean('is_available')->default(true)->index();
            $table->boolean('is_verified')->default(false)->index();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('blood_donors');
    }
};
