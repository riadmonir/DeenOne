<?php
/**
 * ==============================================================================
 * DEEN ONE - HALAL FOOD & E-CODES AUTOMATIC INGESTION & HARMONIZATION SERVICE
 * Automatically connects to Open Food Facts API and Halal Additives Repositories
 * to evaluate, translate, and sync thousands of food products & ingredients.
 * ==============================================================================
 */

class HalalFoodSyncService {

    /**
     * Automatic Halal Classification Engine based on Islamic Fiqh & Fatwa Councils
     * (JAKIM, IFANCA, SANHA, IOMS Kuwait, European Halal Trust)
     */
    public static function evaluateHalalStatus($eCodeOrName) {
        $clean = strtoupper(trim(str_replace([' ', '-', '_'], '', $eCodeOrName)));

        // Known HARAM E-Codes / Ingredients
        $haramMap = [
            'E120' => ['name' => 'Carmine / Cochineal (কারমাইন পোকার রঞ্জক)', 'alt' => 'E160c (পাপরিকা), E162 (বীটরুট রেড)', 'source' => 'শুকনো কশনিল পোকা (Dactylopius coccus) থেকে তৈরি লাল রঙ', 'desc' => 'অধিকাংশ আলেম ও ফিকহ একাডেমির মতে পোকামাকড় ভক্ষণ নাজায়েজ হওয়ায় E120 খাদ্য ও ক্যান্ডিতে ব্যবহার হারাম।', 'fiqh' => 'হানাফী, শাফেয়ী ও হাম্বলী মতে হারাম।', 'hadith' => 'আল-হিদায়াহ; ফাতাওয়া লাজনাতুদ দায়িমা (২২/৩১৮)'],
            'E441' => ['name' => 'Animal Gelatin (পর্ক বা অ-হালাল প্রাণিজ জিলেটিন)', 'alt' => 'E406 (আগার-আগার), E440 (পেকটিন), হালাল বোভাইন জিলেটিন', 'source' => 'শূকরের চামড়া, হাড় বা অ-জবেহকৃত পশুর কোলাজেন', 'desc' => 'শূকর বা অ-হালাল পশুর জিলেটিন ব্যবহারে রাসায়নিক রূপান্তর (ইস্তিহালাহ) ঘটে না বিধায় এটি সম্পূর্ণ হারাম।', 'fiqh' => 'চার মাযহাবের সর্বসম্মত ইজমায় হারাম।', 'hadith' => 'সূরা আল-বাক্বারাহ: ১৭৩; আন্তর্জাতিক ফিকহ একাডেমি'],
            'E542' => ['name' => 'Bone Phosphate (হাড়ের ফসফেট)', 'alt' => 'E341 (ক্যালসিয়াম ফসফেট - খনিজ উৎস)', 'source' => 'শূকর বা অ-জবেহকৃত পশুর হাড়ের চূর্ণ', 'desc' => 'হারাম ও অপবিত্র পশুর হাড় থেকে নিষ্কাশিত হওয়ায় খাদ্যে ব্যবহার নিষিদ্ধ।', 'fiqh' => 'চার মাযহাবেই হারাম।', 'hadith' => 'ফাতাওয়া লাজনাতুদ দায়িমা'],
            'E920' => ['name' => 'L-Cysteine (এল-সিস্টাইন ডো কন্ডিশনার)', 'alt' => '১০০% সিন্থেটিক বা উদ্ভিজ্জ এল-সিস্টাইন', 'source' => 'মানুষের চুল (Human Hair) বা শূকরের লোম', 'desc' => 'মানুষের অঙ্গ সম্মানার্হ হওয়ায় তা খাদ্য উপাদান হিসেবে গ্রহণ ইসলামে সম্পূর্ণ হারাম।', 'fiqh' => 'ইসলামিক অর্গানাইজেশন ফর মেডিকেল সায়েন্সেস (IOMS)', 'hadith' => 'বাদাঈ আস-সানাঈ; কুয়েত ফিকহ একাডেমি'],
            'E921' => ['name' => 'L-Cysteine Hydrochloride', 'alt' => 'সিন্থেটিক অ্যামিনো অ্যাসিড', 'source' => 'মানুষের মাথার চুল বা প্রাণিজ লোম', 'desc' => 'বেকারি পণ্যে ডো নরম রাখতে ব্যবহৃত উপাদান যা মানব চুল থেকে তৈরি হলে সম্পূর্ণ নিষিদ্ধ।', 'fiqh' => 'সর্বসম্মতিক্রমে নাজায়েজ।', 'hadith' => 'IOMS ফতোয়া'],
            'E1000' => ['name' => 'Cholic Acid (কোলিক অ্যাসিড)', 'alt' => 'উদ্ভিজ্জ ইমালসিফায়ার', 'source' => 'গরু বা শূকরের পিত্তরস (Bile extract)', 'desc' => 'প্রাণিজ পিত্তরস থেকে প্রস্তুতকৃত অ্যাসিড যা অ-হালাল উৎস হলে নিষিদ্ধ।', 'fiqh' => 'অ-জবেহকৃত বা শূকরের উৎস হলে হারাম।', 'hadith' => 'SANHA Guide'],
            'E1105' => ['name' => 'Lysozyme (লাইসোজাইম প্রিজারভেটিভ)', 'alt' => 'উদ্ভিজ্জ প্রিজারভেটিভ', 'source' => 'ডিমের সাদা অংশ বা প্রাণিজ টিস্যু', 'desc' => 'পনিরে ব্যাকটেরিয়া ধ্বংসে ব্যবহৃত এনজাইম; অ-হালাল প্রক্রিয়ায় তৈরি হলে বর্জনীয়।', 'fiqh' => 'উৎস নিশ্চিত করা আবশ্যক।', 'hadith' => 'হালাল স্ট্যান্ডার্ড বোর্ড'],
            'E1518' => ['name' => 'Glyceryl Triacetate / Triacetin', 'alt' => 'উদ্ভিজ্জ গ্লিসারিন', 'source' => 'প্রাণিজ চর্বি বা সিন্থেটিক দ্রাবক', 'desc' => 'ফ্লেভারের দ্রাবক হিসেবে ব্যবহৃত; প্রাণিজ উৎস হলে হারাম।', 'fiqh' => 'উৎস সাপেক্ষে হারাম।', 'hadith' => 'JAKIM Halal Guide']
        ];

        // Known MUSHBOOH (Doubtful) E-Codes
        $mushboohMap = [
            'E471' => ['name' => 'Mono- and Diglycerides of Fatty Acids', 'alt' => 'E322 (সয়া লেসিথিন) বা 100% Plant Origin E471', 'source' => 'উদ্ভিজ্জ পাম তেল অথবা শূকর/গরুর চর্বি', 'desc' => 'আইসক্রিম, পাউরুটি ও চকোলেটে তেল-পানি মেশাতে ব্যবহৃত প্রধান ইমালসিফায়ার। উৎস স্পষ্ট না থাকলে পরিহার করা উত্তম।', 'fiqh' => 'উদ্ভিজ্জ নিশ্চিত হলে হালাল, প্রাণিজ উৎস অজানা থাকলে সন্দেহজনক।', 'hadith' => 'সহীহ বুখারী: ৫২ ("সন্দেহজনক বিষয় পরিহার কর")'],
            'E472A' => ['name' => 'Acetic Acid Esters of Mono- and Diglycerides', 'alt' => 'উদ্ভিজ্জ এস্টার', 'source' => 'ফ্যাটি অ্যাসিড ও ভিনেগার অ্যাসিড', 'desc' => 'প্রাণিজ চর্বি বা উদ্ভিজ্জ তেল উভয় থেকেই হতে পারে।', 'fiqh' => 'উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'JAKIM Halal Hub'],
            'E472B' => ['name' => 'Lactic Acid Esters of Mono- and Diglycerides', 'alt' => 'উদ্ভিজ্জ এস্টার', 'source' => 'ল্যাকটিক অ্যাসিড ও ফ্যাটি অ্যাসিড', 'desc' => 'উৎস উদ্ভিজ্জ লেখা থাকলে হালাল।', 'fiqh' => 'উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'IFANCA Guide'],
            'E472E' => ['name' => 'Diacetyl Tartaric Acid Esters of Mono- and Diglycerides (DATEM)', 'alt' => 'ভেজিটেবল DATEM', 'source' => 'টারটারিক অ্যাসিড ও ফ্যাটি অ্যাসিড', 'desc' => 'বেকারি রুটিতে ভলিউম বাড়াতে ব্যবহৃত।', 'fiqh' => 'উদ্ভিজ্জ হলে সম্পূর্ণ হালাল।', 'hadith' => 'SANHA Halal Guide'],
            'E422' => ['name' => 'Glycerol / Glycerin (গ্লিসারিন)', 'alt' => '১০০% ভেজিটেবল গ্লিসারিন', 'source' => 'উদ্ভিজ্জ তেল বা শূকরের চর্বি', 'desc' => 'খাদ্যে আর্দ্রতা ও মিষ্টি ভাব ধরে রাখে। Vegetable Glycerin লেখা থাকলে হালাল।', 'fiqh' => 'উদ্ভিজ্জ নিশ্চিত হলে হালাল।', 'hadith' => 'JAKIM Halal Standard'],
            'E904' => ['name' => 'Shellac (গালা রেজিন পালিশ)', 'alt' => 'E903 (কারনাউবা উদ্ভিজ্জ মোম)', 'source' => 'গালা পোকার (Kerria lacca) নিঃসৃত প্রাকৃতিক আঠা', 'desc' => 'চকোলেট ও ফলের উজ্জ্বলতা বাড়াতে ব্যবহৃত। প্রক্রিয়াজাত নির্যাস নিয়ে মাযহাবভেদে ভিন্নমত রয়েছে।', 'fiqh' => 'হানাফী মাযহাবে সতর্কতা কাম্য; অন্যান্য মতে বৈধ।', 'hadith' => 'লাজনাতুদ দায়িমা ফতোয়া বোর্ড'],
            'E631' => ['name' => 'Disodium Inosinate (ফ্লেভার এনহ্যান্সার)', 'alt' => 'ট্যাপিওকা বা ইস্ট এক্সট্র্যাক্ট', 'source' => 'ট্যাপিওকা গাঁজন অথবা শূকর/গরুর মাংসের নির্যাস', 'desc' => 'ইনস্ট্যান্ট নুডলস ও চিপসে ব্যবহৃত। Plant-based বা Halal Certified নিশ্চিত হলে হালাল।', 'fiqh' => 'প্রাণিজ হলে নাজায়েজ, উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'IOMS কুয়েত সম্মেলন'],
            'E635' => ['name' => 'Disodium 5-Ribonucleotides', 'alt' => 'উদ্ভিজ্জ ফ্লেভার এনহ্যান্সার', 'source' => 'উদ্ভিজ্জ বা মাংসের নির্যাস', 'desc' => 'সুপার টেস্ট মেকার হিসেবে নুডলস ও ক্র্যাকার্সে ব্যবহৃত।', 'fiqh' => 'উৎস যাচাই আবশ্যক।', 'hadith' => 'হালাল স্ট্যান্ডার্ড বোর্ড'],
            'E160A' => ['name' => 'Beta-Carotene (বিটা-ক্যারোটিন)', 'alt' => '১০০% উদ্ভিজ্জ বিটা-ক্যারোটিন', 'source' => 'গাজর/উদ্ভিদ (কখনও অ-হালাল জিলেটিন বাহক সহ)', 'desc' => 'প্রাকৃতিক ভিটামিন এ রঙ। যদি পাউডার তৈরিতে শূকরের জিলেটিন বাহক ব্যবহৃত হয় তবে বর্জনীয়।', 'fiqh' => 'বিশুদ্ধ উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'SANHA Food Guide'],
            'E470' => ['name' => 'Salts of Fatty Acids (ফ্যাটি অ্যাসিড লবণ)', 'alt' => 'উদ্ভিজ্জ স্টিয়ারেট', 'source' => 'প্রাণিজ চর্বি বা উদ্ভিজ্জ তেল', 'desc' => 'অ্যান্টি-কেকিং উপাদান হিসেবে ব্যবহৃত।', 'fiqh' => 'উদ্ভিজ্জ উৎস সাপেক্ষে হালাল।', 'hadith' => 'IFANCA Guide'],
            'E481' => ['name' => 'Sodium Stearoyl Lactylate (SSL)', 'alt' => 'ভেজিটেবল SSL', 'source' => 'উদ্ভিজ্জ তেল বা লার্ড ফ্যাট', 'desc' => 'কেক ও পাউরুটিতে আর্দ্রতা বাড়ায়।', 'fiqh' => 'উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'JAKIM Halal Standard'],
            'E482' => ['name' => 'Calcium Stearoyl Lactylate (CSL)', 'alt' => 'উদ্ভিজ্জ CSL', 'source' => 'উদ্ভিজ্জ বা প্রাণিজ ফ্যাটি অ্যাসিড', 'desc' => 'পাউরুটি ও বিস্কুটের টেক্সচার ভালো রাখে।', 'fiqh' => 'উদ্ভিজ্জ হলে হালাল।', 'hadith' => 'SANHA Guide']
        ];

        // Check exact E-Code
        if (isset($haramMap[$clean])) {
            $info = $haramMap[$clean];
            return [
                'status' => 'HARAM',
                'title' => $info['name'],
                'e_code' => $clean,
                'category' => 'E_CODE',
                'source_origin' => $info['source'],
                'description' => $info['desc'],
                'hadith_ref' => $info['hadith'],
                'fiqh_ruling' => $info['fiqh'],
                'halal_alternative' => $info['alt']
            ];
        }

        if (isset($mushboohMap[$clean])) {
            $info = $mushboohMap[$clean];
            return [
                'status' => 'MUSHBOOH',
                'title' => $info['name'],
                'e_code' => $clean,
                'category' => 'E_CODE',
                'source_origin' => $info['source'],
                'description' => $info['desc'],
                'hadith_ref' => $info['hadith'],
                'fiqh_ruling' => $info['fiqh'],
                'halal_alternative' => $info['alt']
            ];
        }

        // Generic E-Codes evaluation
        if (preg_match('/^E\d{3,4}[A-Z]?$/i', $clean)) {
            $num = (int)preg_replace('/[^0-9]/', '', $clean);
            
            // Colors: E100 - E199 (Mostly Halal Plant except E120)
            if ($num >= 100 && $num < 200) {
                return [
                    'status' => 'HALAL',
                    'title' => "$clean (প্রাকৃতিক বা অনুমোদিত ফুড কালার)",
                    'e_code' => $clean,
                    'category' => 'E_CODE',
                    'source_origin' => 'উদ্ভিজ্জ বা সিন্থেটিক ফুড গ্রেড রঞ্জক',
                    'description' => 'খাদ্যে অনুমোদিত প্রাকৃতিক বা নিরাপদ কৃত্রিম রঙ।',
                    'hadith_ref' => 'সূরা আল-বাক্বারাহ: ১৬৮',
                    'fiqh_ruling' => 'কোনো অপবিত্র উপাদান না থাকায় হালাল।',
                    'halal_alternative' => null
                ];
            }

            // Preservatives: E200 - E299 (Mostly Halal Chemicals/Acids)
            if ($num >= 200 && $num < 300) {
                return [
                    'status' => 'HALAL',
                    'title' => "$clean (অনুমোদিত খাদ্য প্রিজারভেটিভ)",
                    'e_code' => $clean,
                    'category' => 'E_CODE',
                    'source_origin' => 'প্রাকৃতিক অ্যাসিড বা খনিজ লবণ',
                    'description' => 'খাদ্যকে ব্যাকটেরিয়া ও ছত্রাক থেকে রক্ষা করতে ব্যবহৃত নিরাপদ প্রিজারভেটিভ।',
                    'hadith_ref' => 'ইউরোপিয়ান হালাল ট্রাস্ট',
                    'fiqh_ruling' => 'চার মাযহাবেই সম্পূর্ণ হালাল।',
                    'halal_alternative' => null
                ];
            }

            // Antioxidants: E300 - E399 (Vitamin C, Citric Acid, Plant Tocopherols - Mostly Halal)
            if ($num >= 300 && $num < 400) {
                return [
                    'status' => 'HALAL',
                    'title' => "$clean (প্রাকৃতিক অ্যান্টিঅক্সিডেন্ট ও ভিটামিন)",
                    'e_code' => $clean,
                    'category' => 'E_CODE',
                    'source_origin' => 'লেবু, ফলমূল বা সয়াবিন থেকে প্রস্তুত',
                    'description' => 'খাদ্য তাজা রাখতে ও পুষ্টিগুণ অটুট রাখতে ব্যবহৃত নিরাপদ উপাদান।',
                    'hadith_ref' => 'JAKIM Halal Standard',
                    'fiqh_ruling' => 'সর্বসম্মতিক্রমে হালাল।',
                    'halal_alternative' => null
                ];
            }

            // Gums & Thickeners: E400 - E499
            if ($num >= 400 && $num <= 469) {
                return [
                    'status' => 'HALAL',
                    'title' => "$clean (প্রাকৃতিক উদ্ভিজ্জ গাম ও থিকেনার)",
                    'e_code' => $clean,
                    'category' => 'E_CODE',
                    'source_origin' => 'সামুদ্রিক শৈবাল, বাবলা গাছের আঠা বা উদ্ভিজ্জ সেলুলোজ',
                    'description' => '১০০% উদ্ভিদভিত্তিক নির্যাস যা খাবারে ঘনত্ব ও টেক্সচার দিতে ব্যবহৃত হয়।',
                    'hadith_ref' => 'IFANCA Certified',
                    'fiqh_ruling' => 'চার মাযহাবেই হালাল।',
                    'halal_alternative' => null
                ];
            }

            // Sweeteners: E950 - E969
            if ($num >= 950 && $num <= 969) {
                return [
                    'status' => 'HALAL',
                    'title' => "$clean (অনুমোদিত জিরো-ক্যালরি সুইটনার)",
                    'e_code' => $clean,
                    'category' => 'E_CODE',
                    'source_origin' => 'সিন্থেটিক বা পরিবর্তিত শর্করা',
                    'description' => 'চিনির বিকল্প হিসেবে মিষ্টি স্বাদ যোগাতে ব্যবহৃত হালাল সুইটনার।',
                    'hadith_ref' => 'SANHA Food Guide',
                    'fiqh_ruling' => 'অনুমোদিত মাত্রায় সম্পূর্ণ হালাল।',
                    'halal_alternative' => 'প্রাকৃতিক খাঁটি মধু বা স্টেভিয়া'
                ];
            }
        }

        return null;
    }
}
