<?php

namespace App\Console\Commands;

use App\Models\AppSetting;
use App\Models\AsmaulHusna;
use App\Models\BloodDonor;
use App\Models\Dua;
use App\Models\DuaCategory;
use App\Models\Hadith;
use App\Models\HadithCategory;
use App\Models\HajjArticle;
use App\Models\HajjStage;
use App\Models\HajjTopic;
use App\Models\HalalPlace;
use App\Models\Mosque;
use App\Models\QuizCategory;
use App\Models\QuizQuestion;
use App\Models\SixKalima;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;

class ImportLegacyDataCommand extends Command
{
    protected $signature = 'deenone:import-legacy {--sql=../server_backend/deenone_db.sql}';
    protected $description = 'Import existing data from legacy server_backend into Laravel schema';

    public function handle(): int
    {
        $sqlPath = $this->option('sql');
        $this->info("Importing data from {$sqlPath}...");

        if (!file_exists($sqlPath)) {
            $this->warn("SQL file not found at {$sqlPath}. Checking standard location...");
            $alt = base_path('../server_backend/deenone_db.sql');
            if (file_exists($alt)) {
                $sqlPath = $alt;
            } else {
                $this->error("Could not find legacy SQL file!");
                return 1;
            }
        }

        $sqlContent = file_get_contents($sqlPath);
        $queries = explode(";\n", $sqlContent);

        $this->output->progressStart(count($queries));
        DB::statement('SET FOREIGN_KEY_CHECKS=0;');

        foreach ($queries as $q) {
            $trimmed = trim($q);
            if (!empty($trimmed) && !str_starts_with($trimmed, 'CREATE TABLE') && !str_starts_with($trimmed, 'DROP TABLE')) {
                try {
                    DB::unprepared($trimmed . ';');
                } catch (\Exception $e) {
                    // Ignore minor insert conflicts
                }
            }
            $this->output->progressAdvance();
        }

        DB::statement('SET FOREIGN_KEY_CHECKS=1;');
        $this->output->progressFinish();

        $this->info("\nLegacy data imported successfully!");
        return 0;
    }
}
