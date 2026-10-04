package com.devflux.deenone.data.local;

import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import java.util.ArrayList;
import java.util.List;

public class QuranSurahDataSeeder {

    public static List<QuranSurahEntity> get114Surahs() {
        List<QuranSurahEntity> list = new ArrayList<>();

        list.add(new QuranSurahEntity(1, "الفاتحة", "আল-ফাতিহা", "Al-Fatihah", "সূচনা", "The Opening", 7, "মাক্কী", 1));
        list.add(new QuranSurahEntity(2, "البقرة", "আল-বাকারা", "Al-Baqarah", "গাভী", "The Cow", 286, "মাদানী", 1));
        list.add(new QuranSurahEntity(3, "آل عمران", "আলে ইমরান", "Aal-Imran", "ইমরানের পরিবার", "The Family of Imran", 200, "মাদানী", 3));
        list.add(new QuranSurahEntity(4, "النساء", "আন-নিসা", "An-Nisa", "নারী", "The Women", 176, "মাদানী", 4));
        list.add(new QuranSurahEntity(5, "المائدة", "আল-মায়িদাহ", "Al-Ma'idah", "খাদ্য পরিবেশিত টেবিল", "The Table Spread", 120, "মাদানী", 6));
        list.add(new QuranSurahEntity(6, "الأنعام", "আল-আনআম", "Al-An'am", "গৃহপালিত পশু", "The Cattle", 165, "মাক্কী", 7));
        list.add(new QuranSurahEntity(7, "الأعراف", "আল-আরাফ", "Al-A'raf", "উঁচু স্থান", "The Heights", 206, "মাক্কী", 8));
        list.add(new QuranSurahEntity(8, "الأنفال", "আল-আনফাল", "Al-Anfal", "যুদ্ধলব্ধ ধনসম্পদ", "The Spoils of War", 75, "মাদানী", 9));
        list.add(new QuranSurahEntity(9, "التوبة", "আত-তাওবাহ", "At-Tawbah", "অনুশোচনা / তওবা", "The Repentance", 129, "মাদানী", 10));
        list.add(new QuranSurahEntity(10, "يونس", "ইউনুস", "Yunus", "নবী ইউনুস", "Jonah", 109, "মাক্কী", 11));

        list.add(new QuranSurahEntity(11, "هود", "হূদ", "Hud", "নবী হূদ", "Hud", 123, "মাক্কী", 11));
        list.add(new QuranSurahEntity(12, "يوسف", "ইউসুফ", "Yusuf", "নবী ইউসুফ", "Joseph", 111, "মাক্কী", 12));
        list.add(new QuranSurahEntity(13, "الرعد", "আর-রাদ", "Ar-Ra'd", "বজ্রপাত", "The Thunder", 43, "মাদানী", 13));
        list.add(new QuranSurahEntity(14, "إبراهيم", "ইব্রাহিম", "Ibrahim", "নবী ইব্রাহিম", "Abraham", 52, "মাক্কী", 13));
        list.add(new QuranSurahEntity(15, "الحجر", "আল-হিজর", "Al-Hijr", "পাথুরে পাহাড়", "The Rocky Tract", 99, "মাক্কী", 14));
        list.add(new QuranSurahEntity(16, "النحل", "আন-নাহল", "An-Nahl", "মৌমাছি", "The Bee", 128, "মাক্কী", 14));
        list.add(new QuranSurahEntity(17, "الإسراء", "আল-ইসরা", "Al-Isra", "রাত্রিকালীন ভ্রমণ", "The Night Journey", 111, "মাক্কী", 15));
        list.add(new QuranSurahEntity(18, "الكهف", "আল-কাহফ", "Al-Kahf", "গুহা", "The Cave", 110, "মাক্কী", 15));
        list.add(new QuranSurahEntity(19, "مريم", "মারইয়াম", "Maryam", "মারইয়াম (আ.)", "Mary", 98, "মাক্কী", 16));
        list.add(new QuranSurahEntity(20, "طه", "ত্বা-হা", "Taha", "ত্বা-হা", "Ta-Ha", 135, "মাক্কী", 16));

        list.add(new QuranSurahEntity(21, "الأنبياء", "আল-আম্বিয়া", "Al-Anbiya", "নবীগণ", "The Prophets", 112, "মাক্কী", 17));
        list.add(new QuranSurahEntity(22, "الحج", "আল-হাজ্জ", "Al-Hajj", "হজ", "The Pilgrimage", 78, "মাদানী", 17));
        list.add(new QuranSurahEntity(23, "المؤمنون", "আল-মুমিনুন", "Al-Mu'minun", "বিশ্বাসীগণ", "The Believers", 118, "মাক্কী", 18));
        list.add(new QuranSurahEntity(24, "النور", "আন-নূর", "An-Nur", "আলো / জ্যোতি", "The Light", 64, "মাদানী", 18));
        list.add(new QuranSurahEntity(25, "الفرقان", "আল-ফুরকান", "Al-Furqan", "সত্য ও মিথ্যার পার্থক্যকারী", "The Criterion", 77, "মাক্কী", 18));
        list.add(new QuranSurahEntity(26, "الشعراء", "আশ-শুয়ারা", "Ash-Shu'ara", "কবিগণ", "The Poets", 227, "মাক্কী", 19));
        list.add(new QuranSurahEntity(27, "النمل", "আন-নামল", "An-Naml", "পিপীলিকা", "The Ant", 93, "মাক্কী", 19));
        list.add(new QuranSurahEntity(28, "القصص", "আল-কাসাস", "Al-Qasas", "কাহিনী", "The Stories", 88, "মাক্কী", 20));
        list.add(new QuranSurahEntity(29, "العنكبوت", "আল-আনকাবুত", "Al-Ankabut", "মাকড়সা", "The Spider", 69, "মাক্কী", 20));
        list.add(new QuranSurahEntity(30, "الروم", "আর-রূম", "Ar-Rum", "রোমান জাতি", "The Romans", 60, "মাক্কী", 21));

        list.add(new QuranSurahEntity(31, "لقمان", "লুকমান", "Luqman", "জ্ঞানী লুকমান", "Luqman", 34, "মাক্কী", 21));
        list.add(new QuranSurahEntity(32, "السجدة", "আস-সাজদাহ", "As-Sajdah", "সিজদা", "The Prostration", 30, "মাক্কী", 21));
        list.add(new QuranSurahEntity(33, "الأحزاب", "আল-আহযাব", "Al-Ahzab", "সম্মিলিত বাহিনী", "The Combined Forces", 73, "মাদানী", 21));
        list.add(new QuranSurahEntity(34, "سبإ", "সাবা", "Saba", "সাবা জাতি", "Sheba", 54, "মাক্কী", 22));
        list.add(new QuranSurahEntity(35, "فاطر", "ফাতির", "Fatir", "সৃষ্টিকর্তা", "The Originator", 45, "মাক্কী", 22));
        list.add(new QuranSurahEntity(36, "يس", "ইয়াসিন", "Yaseen", "ইয়াসিন", "Ya-Seen", 83, "মাক্কী", 22));
        list.add(new QuranSurahEntity(37, "الصافات", "আস-সাফফাত", "As-Saffat", "সারিবদ্ধ ফেরেশতাগণ", "Those Ranks", 182, "মাক্কী", 23));
        list.add(new QuranSurahEntity(38, "ص", "ছোয়াদ", "Sad", "ছোয়াদ", "The Letter Sad", 88, "মাক্কী", 23));
        list.add(new QuranSurahEntity(39, "الزمر", "আজ-জুমার", "Az-Zumar", "দলসমূহ", "The Groups", 75, "মাক্কী", 23));
        list.add(new QuranSurahEntity(40, "غافر", "গাফির", "Ghafir", "ক্ষমাশীল", "The Forgiver", 85, "মাক্কী", 24));

        list.add(new QuranSurahEntity(41, "فصلت", "ফুসসিলাত", "Fussilat", "স্পষ্ট বর্ণিত", "Explained in Detail", 54, "মাক্কী", 24));
        list.add(new QuranSurahEntity(42, "الشورى", "আশ-শূরা", "Ash-Shura", "পরামর্শ", "The Consultation", 53, "মাক্কী", 25));
        list.add(new QuranSurahEntity(43, "الزخرف", "আজ-জু Ruf", "Az-Zukhruf", "স্বর্ণালঙ্কার", "The Ornaments of Gold", 89, "মাক্কী", 25));
        list.add(new QuranSurahEntity(44, "الدخان", "আদ-দুখান", "Ad-Dukhan", "ধোঁয়া", "The Smoke", 59, "মাক্কী", 25));
        list.add(new QuranSurahEntity(45, "الجاثية", "আল-জাসিয়াহ", "Al-Jathiyah", "নতজানু", "The Crouching", 37, "মাক্কী", 25));
        list.add(new QuranSurahEntity(46, "الأحقاف", "আল-আহকাফ", "Al-Ahqaf", "বালির পাহাড়", "The Wind-Curved Sandhills", 35, "মাক্কী", 26));
        list.add(new QuranSurahEntity(47, "محمد", "মুহাম্মদ", "Muhammad", "মুহাম্মদ (সা.)", "Muhammad", 38, "মাদানী", 26));
        list.add(new QuranSurahEntity(48, "الفتح", "আল-ফাতহ", "Al-Fath", "বিজয়", "The Victory", 29, "মাদানী", 26));
        list.add(new QuranSurahEntity(49, "الحجرات", "আল-হুজুরাত", "Al-Hujurat", "গৃহসমূহ", "The Rooms", 18, "মাদানী", 26));
        list.add(new QuranSurahEntity(50, "ق", "ক্বাফ", "Qaf", "ক্বাফ", "The Letter Qaf", 45, "মাক্কী", 26));

        list.add(new QuranSurahEntity(51, "الذاريات", "আজ-জারিয়াত", "Adh-Dhariyat", "বিক্ষিপ্তকারী বাতাস", "The Winnowing Winds", 60, "মাক্কী", 26));
        list.add(new QuranSurahEntity(52, "الطور", "আত-তূর", "At-Tur", "তূর পাহাড়", "The Mount", 49, "মাক্কী", 27));
        list.add(new QuranSurahEntity(53, "النجم", "আন-নাজম", "An-Najm", "নক্ষত্র", "The Star", 62, "মাক্কী", 27));
        list.add(new QuranSurahEntity(54, "القمر", "আল-ক্বামার", "Al-Qamar", "চাঁদ", "The Moon", 55, "মাক্কী", 27));
        list.add(new QuranSurahEntity(55, "الرحمن", "আর-রাহমান", "Ar-Rahman", "পরম দয়ালু", "The Beneficent", 78, "মাদানী", 27));
        list.add(new QuranSurahEntity(56, "الواقعة", "আল-ওয়াকিয়াহ", "Al-Waqi'ah", "মহাবিপ্লব / কিয়ামত", "The Inevitable", 96, "মাক্কী", 27));
        list.add(new QuranSurahEntity(57, "الحديد", "আল-হাদিদ", "Al-Hadid", "লোহা", "The Iron", 29, "মাদানী", 27));
        list.add(new QuranSurahEntity(58, "المجادلة", "আল-মুজাদালাহ", "Al-Mujadila", "অনুযোগকারিণী", "The Pleading Woman", 22, "মাদানী", 28));
        list.add(new QuranSurahEntity(59, "الحشر", "আল-হাশর", "Al-Hashr", "সমাবেশ", "The Exile", 24, "মাদানী", 28));
        list.add(new QuranSurahEntity(60, "الممتحنة", "আল-মুমতাহানাহ", "Al-Mumtahanah", "পরীক্ষিতা নারী", "She that is to be examined", 13, "মাদানী", 28));

        list.add(new QuranSurahEntity(61, "الصف", "আস-সাফ", "As-Saff", "সারিবদ্ধ সৈন্যদল", "The Ranks", 14, "মাদানী", 28));
        list.add(new QuranSurahEntity(62, "الجمعة", "আল-জুমুআহ", "Al-Jumu'ah", "শুক্রবার / সম্মেলন", "The Congregation", 11, "মাদানী", 28));
        list.add(new QuranSurahEntity(63, "المنافقون", "আল-মুনাফিকুন", "Al-Munafiqun", "কপট বিশ্বাসীগণ", "The Hypocrites", 11, "মাদানী", 28));
        list.add(new QuranSurahEntity(64, "التغابن", "আত-তাগাবুন", "At-Taghabun", "লাভ-ক্ষতি প্রকাশ", "The Mutual Disillusion", 18, "মাদানী", 28));
        list.add(new QuranSurahEntity(65, "الطلاق", "আত-তালাক", "At-Talaq", "তালাক / বিবাহ বিচ্ছেদ", "The Divorce", 12, "মাদানী", 28));
        list.add(new QuranSurahEntity(66, "التحريم", "আত-তাহরীম", "At-Tahrim", "নিষিদ্ধকরণ", "The Prohibition", 12, "মাদানী", 28));
        list.add(new QuranSurahEntity(67, "الملك", "আল-মুলক", "Al-Mulk", "সার্বভৌম কর্তৃত্ব", "The Sovereignty", 30, "মাক্কী", 29));
        list.add(new QuranSurahEntity(68, "القلم", "আল-ক্বলম", "Al-Qalam", "কলম", "The Pen", 52, "মাক্কী", 29));
        list.add(new QuranSurahEntity(69, "الحاقة", "আল-হাক্কাহ", "Al-Haqqah", "সুনিশ্চিত সত্য", "The Reality", 52, "মাক্কী", 29));
        list.add(new QuranSurahEntity(70, "المعارج", "আল-মাআরিজ", "Al-Ma'arij", "উন্নয়নের সিঁড়ি", "The Ascending Stairways", 44, "মাক্কী", 29));

        list.add(new QuranSurahEntity(71, "نوح", "নূহ", "Nuh", "নবী নূহ", "Noah", 28, "মাক্কী", 29));
        list.add(new QuranSurahEntity(72, "الجن", "আল-জিন", "Al-Jinn", "জিন জাতি", "The Jinn", 28, "মাক্কী", 29));
        list.add(new QuranSurahEntity(73, "المزمل", "আল-মুযযাম্মিল", "Al-Muzzammil", "বস্ত্রাবৃত", "The Enshrouded One", 20, "মাক্কী", 29));
        list.add(new QuranSurahEntity(74, "المدثر", "আল-মুদ্দাসসির", "Al-Muddathir", "চাদরাবৃত", "The Cloaked One", 56, "মাক্কী", 29));
        list.add(new QuranSurahEntity(75, "القيامة", "আল-কিয়ামাহ", "Al-Qiyamah", "পুনরুত্থান দিবস", "The Resurrection", 40, "মাক্কী", 29));
        list.add(new QuranSurahEntity(76, "الإنسان", "আল-ইনসান", "Al-Insan", "মানবজাতি", "The Man", 31, "মাদানী", 29));
        list.add(new QuranSurahEntity(77, "المرسلات", "আল-মুরসালাত", "Al-Mursalat", "প্রেরিত বাতাস", "The Emissaries", 50, "মাক্কী", 29));
        list.add(new QuranSurahEntity(78, "النبإ", "আন-নাবা", "An-Naba", "মহা সংবাদ", "The Tidings", 40, "মাক্কী", 30));
        list.add(new QuranSurahEntity(79, "النازعات", "আন-নাজিয়াত", "An-Nazi'at", "উৎপাটনকারী ফেরেশতা", "Those who drag forth", 46, "মাক্কী", 30));
        list.add(new QuranSurahEntity(80, "عبس", "আবাসা", "Abasa", "ভ্রুকুটি করলেন", "He Frowned", 42, "মাক্কী", 30));

        list.add(new QuranSurahEntity(81, "التكوير", "আত-তাকভীর", "At-Takwir", "অন্ধকারাচ্ছন্নকরণ", "The Overthrowing", 29, "মাক্কী", 30));
        list.add(new QuranSurahEntity(82, "الانفطار", "আল-ইনফিতার", "Al-Infitar", "বিদীর্ণ হওয়া", "The Cleaving", 19, "মাক্কী", 30));
        list.add(new QuranSurahEntity(83, "المطففين", "আল-মুতাফফিফিন", "Al-Mutaffifin", "পরিমাপে প্রতারণাকারী", "The Defrauding", 36, "মাক্কী", 30));
        list.add(new QuranSurahEntity(84, "الانشقاق", "আল-ইনশিক্বাক্ব", "Al-Inshiqaq", "খণ্ড-বিখণ্ড হওয়া", "The Splitting Open", 25, "মাক্কী", 30));
        list.add(new QuranSurahEntity(85, "البروج", "আল-বুরূজ", "Al-Buruj", "নক্ষত্রপুঞ্জ", "The Great Stars", 22, "মাক্কী", 30));
        list.add(new QuranSurahEntity(86, "الطارق", "আত-তারিক্ব", "At-Tariq", "রাতের আগমনকারী", "The Night-Comer", 17, "মাক্কী", 30));
        list.add(new QuranSurahEntity(87, "الأعلى", "আল-আলা", "Al-A'la", "সর্বোচ্চ", "The Most High", 19, "মাক্কী", 30));
        list.add(new QuranSurahEntity(88, "الغاشية", "আল-গাশিয়াহ", "Al-Ghashiyah", "আচ্ছন্নকারী কিয়ামত", "The Overwhelming", 26, "মাক্কী", 30));
        list.add(new QuranSurahEntity(89, "الفجر", "আল-ফজর", "Al-Fajr", "ভোরবেলা", "The Dawn", 30, "মাক্কী", 30));
        list.add(new QuranSurahEntity(90, "البلد", "আল-বালাদ", "Al-Balad", "নগরী / মক্কা", "The City", 20, "মাক্কী", 30));

        list.add(new QuranSurahEntity(91, "الشمس", "আশ-শামস", "Ash-Shams", "সূর্য", "The Sun", 15, "মাক্কী", 30));
        list.add(new QuranSurahEntity(92, "الليل", "আল-লায়ল", "Al-Layl", "রাত্রি", "The Night", 21, "মাক্কী", 30));
        list.add(new QuranSurahEntity(93, "الضحى", "আদ-দুহা", "Ad-Duha", "পূর্বাহ্নের আলো", "The Morning Hours", 11, "মাক্কী", 30));
        list.add(new QuranSurahEntity(94, "الشرح", "আল-ইনশিরাহ", "Ash-Sharh", "বক্ষ প্রশস্তকরণ", "The Relief", 8, "মাক্কী", 30));
        list.add(new QuranSurahEntity(95, "التين", "আত-তীন", "At-Tin", "ডুমুর ফল", "The Fig", 8, "মাক্কী", 30));
        list.add(new QuranSurahEntity(96, "العلق", "আল-আলাক্ব", "Al-Alaq", "রক্তপিণ্ড", "The Clot", 19, "মাক্কী", 30));
        list.add(new QuranSurahEntity(97, "القدر", "আল-ক্বদর", "Al-Qadr", "মর্যাদাময় রজনী", "The Power", 5, "মাক্কী", 30));
        list.add(new QuranSurahEntity(98, "البينة", "আল-বাইয্যিনাহ", "Al-Bayyinah", "সুস্পষ্ট প্রমাণ", "The Clear Proof", 8, "মাদানী", 30));
        list.add(new QuranSurahEntity(99, "الزلزلة", "আজ-যিলযাল", "Az-Zalzalah", "মহাকম্পন", "The Earthquake", 8, "মাদানী", 30));
        list.add(new QuranSurahEntity(100, "العاديات", "আল-আদিয়াত", "Al-Adiyat", "অভিযানকারী অশ্ব", "The Courser", 11, "মাক্কী", 30));

        list.add(new QuranSurahEntity(101, "القارعة", "আল-কারিয়াহ", "Al-Qari'ah", "মহাপ্রলয়", "The Calamity", 11, "মাক্কী", 30));
        list.add(new QuranSurahEntity(102, "التكاثر", "আত-তাকাসুর", "At-Takathur", "প্রাচুর্যের প্রতিযোগিতা", "The Rivalry in World Increase", 8, "মাক্কী", 30));
        list.add(new QuranSurahEntity(103, "العصر", "আল-আসর", "Al-Asr", "মহাকাল / যুগ", "The Declining Day", 3, "মাক্কী", 30));
        list.add(new QuranSurahEntity(104, "الهمزة", "আল-হুমাযাহ", "Al-Humazah", "পরনিন্দুক", "The Traducer", 9, "মাক্কী", 30));
        list.add(new QuranSurahEntity(105, "الفيل", "আল-ফীল", "Al-Fil", "হাতি", "The Elephant", 5, "মাক্কী", 30));
        list.add(new QuranSurahEntity(106, "قريش", "কুরাইশ", "Quraysh", "কুরাইশ বংশ", "Quraysh", 4, "মাক্কী", 30));
        list.add(new QuranSurahEntity(107, "الماعون", "আল-মাউন", "Al-Ma'un", "সাহায্য-সহায়তা", "The Small Kindnesses", 7, "মাক্কী", 30));
        list.add(new QuranSurahEntity(108, "الكوثر", "আল-কাউসার", "Al-Kawthar", "প্রাচুর্য / কাউসার", "The Abundance", 3, "মাক্কী", 30));
        list.add(new QuranSurahEntity(109, "الكافرون", "আল-কাফিরুন", "Al-Kafirun", "অবিশ্বাসীগণ", "The Disbelievers", 6, "মাক্কী", 30));
        list.add(new QuranSurahEntity(110, "النصر", "আন-নাসর", "An-Nasr", "আল্লাহর সাহায্য", "The Divine Support", 3, "মাদানী", 30));

        list.add(new QuranSurahEntity(111, "المسد", "আল-লাহাব", "Al-Masad", "খেজুরের আঁশের রশি", "The Palm Fiber", 5, "মাক্কী", 30));
        list.add(new QuranSurahEntity(112, "الإخلاص", "আল-ইখলাস", "Al-Ikhlas", "একনিষ্ঠতা / তাওহীদ", "The Sincerity", 4, "মাক্কী", 30));
        list.add(new QuranSurahEntity(113, "الفلق", "আল-ফালাক্ব", "Al-Falaq", "ভোরের আলো", "The Daybreak", 5, "মাক্কী", 30));
        list.add(new QuranSurahEntity(114, "الناس", "আন-নাস", "An-Nas", "মানবজাতি", "Mankind", 6, "মাক্কী", 30));

        return list;
    }

    public static List<QuranAyahEntity> getEssentialAyahs() {
        List<QuranAyahEntity> list = new ArrayList<>();

        // Surah Al-Fatihah (1: 1 - 7)
        list.add(new QuranAyahEntity(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "বিসমিল্লাহির রাহমানির রাহিম", "https://everyayah.com/data/Alafasy_128kbps/001001.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "যাবতীয় প্রশংসা জগৎসমূহের প্রতিপালক আল্লাহরই জন্য।", "[All] praise is [due] to Allah, Lord of the worlds.", "আলহামদু লিল্লাহি রাব্বিল আলামিন", "https://everyayah.com/data/Alafasy_128kbps/001002.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "যিনি পরম করুণাময়, অতি দয়ালু।", "The Entirely Merciful, the Especially Merciful,", "আর-রাহমানির রাহিম", "https://everyayah.com/data/Alafasy_128kbps/001003.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "যিনি বিচার দিবসের মালিক।", "Sovereign of the Day of Recompense.", "মালিকি ইয়াওমিদ্দিন", "https://everyayah.com/data/Alafasy_128kbps/001004.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য প্রার্থনা করি।", "It is You we worship and You we ask for help.", "ইয়্যাকা নাবুদু ওয়া ইয়্যাকা নাস্তায়ীন", "https://everyayah.com/data/Alafasy_128kbps/001005.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "আমাদেরকে সরল-সঠিক পথ প্রদর্শন করুন।", "Guide us to the straight path -", "ইহদিনাস সিরাতাল মুস্তাকিম", "https://everyayah.com/data/Alafasy_128kbps/001006.mp3", 1, 1));
        list.add(new QuranAyahEntity(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "তাদের পথ, যাদের আপনি অনুগ্রহ দান করেছেন; তাদের পথ নয় যারা ক্রোধের শিকার এবং পথভ্রষ্টদের পথও নয়।", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "সিরাতাল্লাযিনা আনআমতা আলাইহিম, গাইরিল মাগদুবি আলাইহিম ওয়ালাদ-দ্বাল্লীন।", "https://everyayah.com/data/Alafasy_128kbps/001007.mp3", 1, 1));

        // Surah Al-Ikhlas (112: 1 - 4)
        list.add(new QuranAyahEntity(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "বলুন, তিনিই আল্লাহ, একক।", "Say, \"He is Allah, [who is] One,", "কুল হুয়াল্লাহু আহাদ", "https://everyayah.com/data/Alafasy_128kbps/112001.mp3", 30, 604));
        list.add(new QuranAyahEntity(112, 2, "اللَّهُ الصَّمَدُ", "আল্লাহ অমুখাপেক্ষী।", "Allah, the Eternal Refuge.", "আল্লাহুস সামাদ", "https://everyayah.com/data/Alafasy_128kbps/112002.mp3", 30, 604));
        list.add(new QuranAyahEntity(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি।", "He neither begets nor is born,", "লাম ইয়ালিদ ওয়া লাম ইউলাদ", "https://everyayah.com/data/Alafasy_128kbps/112003.mp3", 30, 604));
        list.add(new QuranAyahEntity(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "এবং তাঁর সমতুল্য কেউই নেই।", "Nor is there to Him any equivalent.\"", "ওয়া লাম ইয়াকুল লাহু কুফুওয়ান আহাদ", "https://everyayah.com/data/Alafasy_128kbps/112004.mp3", 30, 604));

        // Surah Al-Falaq (113: 1 - 5)
        list.add(new QuranAyahEntity(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "বলুন, আমি আশ্রয় প্রার্থনা করছি ভোরের পালনকর্তার,", "Say, \"I seek refuge in the Lord of daybreak", "কুল আউযু বিরাব্বিল ফালাক", "https://everyayah.com/data/Alafasy_128kbps/113001.mp3", 30, 604));
        list.add(new QuranAyahEntity(113, 2, "مِن شَرِّ مَا خَلَقَ", "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট থেকে,", "From the evil of that which He created", "মিন শাররি মা খালাক", "https://everyayah.com/data/Alafasy_128kbps/113002.mp3", 30, 604));
        list.add(new QuranAyahEntity(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "এবং রাতের অন্ধকারের অনিষ্ট থেকে যখন তা গভীর হয়,", "And from the evil of darkness when it settles", "ওয়া মিন শাররি গাসিকিন ইযা ওয়াকাব", "https://everyayah.com/data/Alafasy_128kbps/113003.mp3", 30, 604));
        list.add(new QuranAyahEntity(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "এবং গ্রন্থিতে ফুঁৎকারকারী নারীদের অনিষ্ট থেকে,", "And from the evil of the blowers in knots", "ওয়া মিন শাররিন নাফফাসাতি ফিল উকাদ", "https://everyayah.com/data/Alafasy_128kbps/113004.mp3", 30, 604));
        list.add(new QuranAyahEntity(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "এবং হিংসুকের অনিষ্ট থেকে যখন সে হিংসা করে।", "And from the evil of an envier when he envies.\"", "ওয়া মিন শাররি হাসিদিন ইযা হাসাদ", "https://everyayah.com/data/Alafasy_128kbps/113005.mp3", 30, 604));

        // Surah An-Nas (114: 1 - 6)
        list.add(new QuranAyahEntity(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের পালনকর্তার,", "Say, \"I seek refuge in the Lord of mankind,", "কুল আউযু বিরাব্বিন নাস", "https://everyayah.com/data/Alafasy_128kbps/114001.mp3", 30, 604));
        list.add(new QuranAyahEntity(114, 2, "مَلِكِ النَّاسِ", "মানুষের অধিপতির,", "The Sovereign of mankind,", "মালিকিন নাস", "https://everyayah.com/data/Alafasy_128kbps/114002.mp3", 30, 604));
        list.add(new QuranAyahEntity(114, 3, "إِلَٰهِ النَّاسِ", "মানুষের মাবুদের,", "The God of mankind,", "ইলাহিন নাস", "https://everyayah.com/data/Alafasy_128kbps/114003.mp3", 30, 604));
        list.add(new QuranAyahEntity(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "গোপনে আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট থেকে,", "From the evil of the retreating whisperer -", "মিন শাররিল ওয়াসওয়াসিল খান্নাস", "https://everyayah.com/data/Alafasy_128kbps/114004.mp3", 30, 604));
        list.add(new QuranAyahEntity(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "যে মানুষের অন্তরে কুমন্ত্রণা দেয়,", "Who whispers [evil] into the breasts of mankind -", "আল্লাযী ইউওয়াসউইসু ফী সুদূরিন নাস", "https://everyayah.com/data/Alafasy_128kbps/114005.mp3", 30, 604));
        list.add(new QuranAyahEntity(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "জিন ও মানবজাতির মধ্য হতে।", "From among the jinn and mankind.\"", "মিনাল জিন্নাতি ওয়ান নাস", "https://everyayah.com/data/Alafasy_128kbps/114006.mp3", 30, 604));

        return list;
    }

    public static QuranSurahEntity getSurahByNumber(int surahNumber) {
        if (surahNumber < 1 || surahNumber > 114) {
            return null;
        }
        List<QuranSurahEntity> surahs = get114Surahs();
        if (surahs != null && surahNumber <= surahs.size()) {
            return surahs.get(surahNumber - 1);
        }
        return null;
    }
}
