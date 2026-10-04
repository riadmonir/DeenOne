<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class AdminUserSeeder extends Seeder
{
    public function run(): void
    {
        DB::table('admin_users')->updateOrInsert(
            ['username' => 'admin'],
            [
                'password_hash' => hash('sha256', 'admin123'),
                'full_name' => 'Deen One Administrator',
                'email' => 'admin@deenone.top',
                'role' => 'SUPERADMIN',
                'created_at' => now(),
            ]
        );
    }
}
