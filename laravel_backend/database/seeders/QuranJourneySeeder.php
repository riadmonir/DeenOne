<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class QuranJourneySeeder extends Seeder
{
    public function run(): void
    {
        $stages = [
            [
                'id' => 1,
                'stage_number' => 1,
                'title_bn' => 'পর্যায় ১: প্রারম্ভিক সূরাসমূহ',
                'title_en' => 'Stage 1: Introductory Surahs',
                'subtitle_bn' => 'দৈনন্দিন সালাতের আবশ্যকীয় ক্ষুদ্র সূরাসমূহ',
                'subtitle_en' => 'Essential short surahs for daily prayers',
                'badge_text_bn' => 'সূচনা',
                'badge_text_en' => 'Starter',
                'is_active' => true,
            ],
            [
                'id' => 2,
                'stage_number' => 2,
                'title_bn' => 'পর্যায় ২: আত্মশুদ্ধি ও তাওহীদ',
                'title_en' => 'Stage 2: Purification & Tawheed',
                'subtitle_bn' => 'ঈমান ও পরকালের গভীর উপলব্ধি',
                'subtitle_en' => 'Deep insights into faith and the hereafter',
                'badge_text_bn' => 'উন্নতি',
                'badge_text_en' => 'Progress',
                'is_active' => true,
            ],
        ];

        foreach ($stages as $stage) {
            DB::table('quran_journey_stages')->updateOrInsert(
                ['id' => $stage['id']],
                $stage
            );
        }

        $lessons = [
            [
                'id' => 1,
                'stage_id' => 1,
                'lesson_order' => 1,
                'surah_number' => 1,
                'surah_name_ar' => 'الفَاتِحَة',
                'surah_name_bn' => 'আল-ফাতিহা',
                'surah_name_en' => 'Al-Fatiha',
                'ayah_start' => 1,
                'ayah_end' => 7,
                'title_bn' => 'উম্মুল কুরআন - মহা সূচনা',
                'title_en' => 'The Opening Chapter',
                'description_bn' => 'প্রতিটি সালাতের মূল ভিত্তি ও আল্লাহর প্রশংসা।',
                'audio_url' => 'https://audio.deenone.top/surah_001.mp3',
                'reward_points' => 20,
                'is_active' => true,
            ],
            [
                'id' => 2,
                'stage_id' => 1,
                'lesson_order' => 2,
                'surah_number' => 112,
                'surah_name_ar' => 'الإِخْلَاص',
                'surah_name_bn' => 'আল-ইখলাস',
                'surah_name_en' => 'Al-Ikhlas',
                'ayah_start' => 1,
                'ayah_end' => 4,
                'title_bn' => 'একত্ববাদের চূড়ান্ত ঘোষণা',
                'title_en' => 'The Absolute Sincerity',
                'description_bn' => 'কুরআনের এক-তৃতীয়াংশের সমতুল্য বরকতময় সূরা।',
                'audio_url' => 'https://audio.deenone.top/surah_112.mp3',
                'reward_points' => 15,
                'is_active' => true,
            ],
        ];

        foreach ($lessons as $lesson) {
            DB::table('quran_journey_lessons')->updateOrInsert(
                ['id' => $lesson['id']],
                $lesson
            );
        }
    }
}
