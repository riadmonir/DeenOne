<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class SixKalimaSeeder extends Seeder
{
    public function run(): void
    {
        $kalimas = [
            [
                'kalima_number' => 1,
                'title_bn' => 'কালেমা তায়্যিবা',
                'title_en' => 'Kalima Tayyiba',
                'arabic_text' => 'لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ',
                'transliteration_bn' => 'লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।',
                'meaning_bn' => 'আল্লাহ ছাড়া কোনো উপাস্য নেই, মুহাম্মদ (সা.) আল্লাহর প্রেরিত রাসুল।',
                'meaning_en' => 'There is no deity but Allah, and Muhammad is the messenger of Allah.',
                'audio_url' => 'https://audio.deenone.top/kalima_1.mp3',
                'is_active' => true,
            ],
            [
                'kalima_number' => 2,
                'title_bn' => 'কালেমা শাহাদাত',
                'title_en' => 'Kalima Shahadat',
                'arabic_text' => 'أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ',
                'transliteration_bn' => 'আশহাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু।',
                'meaning_bn' => 'আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। আমি আরও সাক্ষ্য দিচ্ছি যে, নিশ্চয়ই মুহাম্মদ (সা.) তাঁর বান্দা ও রাসুল।',
                'meaning_en' => 'I bear witness that there is no deity but Allah, alone without partner, and I bear witness that Muhammad is His servant and messenger.',
                'audio_url' => 'https://audio.deenone.top/kalima_2.mp3',
                'is_active' => true,
            ],
            [
                'kalima_number' => 3,
                'title_bn' => 'কালেমা তামজীদ',
                'title_en' => 'Kalima Tamjeed',
                'arabic_text' => 'سُبْحَانَ اللهِ وَالْحَمْدُ لِلّٰهِ وَلَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ',
                'transliteration_bn' => 'সুবহানাল্লাহি ওয়াল হামদুলিল্লাহি ওয়া লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার, ওয়া লা হাওলা ওয়া লা কুওয়াতা ইল্লা বিল্লাহিল আলিয়্যিল আজিম।',
                'meaning_bn' => 'আল্লাহ পবিত্র, সমস্ত প্রশংসা আল্লাহর জন্য, আল্লাহ ব্যতীত কোনো উপাস্য নেই, আল্লাহ সর্বশ্রেষ্ঠ। মহান ও সর্বশক্তিমান আল্লাহর সাহায্য ছাড়া পাপ থেকে বাঁচার এবং সৎকাজ করার কোনো শক্তি নেই।',
                'meaning_en' => 'Glory be to Allah, all praise is due to Allah, there is no deity but Allah, and Allah is the Greatest. There is no power nor strength except with Allah, the Most High, the Supreme.',
                'audio_url' => 'https://audio.deenone.top/kalima_3.mp3',
                'is_active' => true,
            ],
            [
                'kalima_number' => 4,
                'title_bn' => 'কালেমা তাওহীদ',
                'title_en' => 'Kalima Tawheed',
                'arabic_text' => 'لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، يُحْيِي وَيُمِيتُ، وَهُوَ حَيٌّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالْإِكْرَامِ، بِيَدِهِ الْخَيْرُ، وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ',
                'transliteration_bn' => 'লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু, ইউহয়ী ওয়া ইউমীতু, ওয়া হুয়া হাইয়্যুল লা ইয়ামূতু আবাদান আবাদা, যুল জালালি ওয়াল ইকরাম, বিয়াদিহিল খাইরু, ওয়া হুয়া আলা কুল্লি শাইয়িন কাদীর।',
                'meaning_bn' => 'আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসাও তাঁর। তিনিই জীবন দান করেন এবং তিনিই মৃত্যু ঘটান। তিনি চিরঞ্জীব, তাঁর কখনো মৃত্যু হবে না। তিনি সম্মান ও মর্যাদার অধিকারী। সমস্ত কল্যাণ তাঁরই হাতে এবং তিনি সবকিছুর ওপর পূর্ণ ক্ষমতাবান।',
                'meaning_en' => 'There is no deity but Allah, alone without partner. To Him belongs sovereignty, and to Him belongs praise. He gives life and causes death, and He is living and will never die. In His Hand is all good, and He has power over all things.',
                'audio_url' => 'https://audio.deenone.top/kalima_4.mp3',
                'is_active' => true,
            ],
            [
                'kalima_number' => 5,
                'title_bn' => 'কালেমা রদ্দে কুফর',
                'title_en' => 'Kalima Radde Kufr',
                'arabic_text' => 'اللّٰهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ',
                'transliteration_bn' => 'আল্লাহুম্মা ইন্নি আউযুবিকা মিন আন উশরিকা বিকা শাইআন ওয়া আনা আ’লামু বিহি, ওয়া আস্তাগফিরুকা লিমা লা আ’লামু বিহি।',
                'meaning_bn' => 'হে আল্লাহ! আমি জেনে-শুনে তোমার সাথে কোনো কিছুকে শরিক করা থেকে তোমার আশ্রয় প্রার্থনা করছি এবং যা আমার অজানা রয়েছে তার জন্য তোমার ক্ষমা প্রার্থনা করছি।',
                'meaning_en' => 'O Allah, I seek refuge with You from knowingly associating anything with You, and I ask Your forgiveness for what I do not know.',
                'audio_url' => 'https://audio.deenone.top/kalima_5.mp3',
                'is_active' => true,
            ],
            [
                'kalima_number' => 6,
                'title_bn' => 'কালেমা ইমান',
                'title_en' => 'Kalima Iman',
                'arabic_text' => 'آمَنْتُ بِاللهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ وَالْيَوْمِ الْآخِرِ وَالْقَدَرِ خَيْرِهِ وَشَرِّهِ مِنَ اللهِ تَعَالَىٰ وَالْبَعْثِ بَعْدَ الْمَوْتِ',
                'transliteration_bn' => 'আমানতু বিল্লাহি ওয়া মালাইকাতিহি ওয়া কুতুবিহি ওয়া রুসুলিহি ওয়াল ইয়াওমিল আখিরি ওয়াল কাদরি খাইরিহি ওয়া শাররিহি মিনাল্লাহি তাআলা ওয়াল বা’সি বা’দাল মাউত।',
                'meaning_bn' => 'আমি বিশ্বাস স্থাপন করলাম আল্লাহর প্রতি, তাঁর ফেরেশতাগণের প্রতি, তাঁর কিতাবসমূহের প্রতি, তাঁর রাসুলগণের প্রতি, পরকালের প্রতি, ভাগ্যের ভালো ও মন্দের প্রতি যা মহান আল্লাহর পক্ষ থেকে নির্ধারিত এবং মৃত্যুর পর পুনরুত্থানের প্রতি।',
                'meaning_en' => 'I believe in Allah, His angels, His books, His messengers, the Last Day, and predestination — that all good and bad is from Allah the Almighty, and resurrection after death.',
                'audio_url' => 'https://audio.deenone.top/kalima_6.mp3',
                'is_active' => true,
            ],
        ];

        foreach ($kalimas as $k) {
            DB::table('six_kalimas')->updateOrInsert(
                ['kalima_number' => $k['kalima_number']],
                $k
            );
        }
    }
}
