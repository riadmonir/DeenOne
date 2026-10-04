<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('admin_users', function (Blueprint $table) {
            $table->increments('id');
            $table->string('username', 50)->unique();
            $table->string('password_hash', 255);
            $table->string('full_name', 100)->default('Admin');
            $table->string('email', 120)->nullable()->unique();
            $table->string('role', 20)->default('SUPERADMIN');
            $table->timestamp('last_login')->nullable();
            $table->timestamps();
        });

        Schema::create('admin_logs', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedInteger('admin_id')->nullable()->index();
            $table->string('action', 100);
            $table->string('target_type', 50)->nullable();
            $table->string('target_id', 100)->nullable();
            $table->text('details')->nullable();
            $table->string('ip_address', 45)->nullable();
            $table->timestamp('created_at')->useCurrent()->index();

            $table->foreign('admin_id')->references('id')->on('admin_users')->onDelete('set null');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('admin_logs');
        Schema::dropIfExists('admin_users');
    }
};
