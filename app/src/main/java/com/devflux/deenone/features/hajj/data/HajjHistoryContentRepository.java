package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjHistoryContentRepository {

    public static List<HajjHistoryCardItem> getHistoryCards() {
        List<HajjHistoryCardItem> items = new ArrayList<>();

        // 1. তাওয়াফ
        items.add(new HajjHistoryCardItem(
            1,
            "তাওয়াফ",
            "Tawaf (Circumambulation)",
            "পবিত্র কুরআনে এসেছে:",
            "It has been revealed in the Holy Quran:",
            "পবিত্র কুরআনে এসেছে:\n" +
            "'এবং তারা যেন প্রাচীন ঘরের তাওয়াফ করে।'\n" +
            "<b>[সূরা আল-হাজ্জ: ২৯]</b>\n\n" +
            "অন্যত্র ইরশাদ হয়েছে:\n" +
            "'এবং আমি ইবরাহীম ও ইসমাঈলকে দায়িত্ব দিয়েছিলাম—তোমরা আমার গৃহকে তাওয়াফকারী, ইতিকাফকারী এবং রুকু ও সিজদাকারীদের জন্য পবিত্র রাখো।'\n" +
            "<b>[সূরা আল-বাক্বারা: ১২৫]</b>\n\n" +
            "তাওয়াফের ইতিহাস মানব ইতিহাসের মতোই প্রাচীন। হযরত আদম (আ.) আল্লাহর নির্দেশে পৃথিবীতে আগমন করে কাবার ভিত্তি স্থাপন করেন এবং সর্বপ্রথম সাত চক্কর তাওয়াফ সম্পন্ন করেন। পরবর্তীতে মহাপ্লাবনের পর হযরত ইবরাহীম (আ.) ও তাঁর পুত্র হযরত ইসমাঈল (আ.) কা'বা ঘর পুনর্নির্মাণ করেন এবং তাওয়াফের বিধান পুনঃপ্রতিষ্ঠা করেন। জাহেলিয়াত যুগে আরবের পৌত্তলিকরা উলঙ্গ হয়ে তাওয়াফ করার মতো জঘন্য প্রথা চালু করেছিল। রাসূলুল্লাহ (ﷺ) মক্কা বিজয়ের পর এবং বিদায় হজে সমস্ত পৌত্তলিকতা ও কুসংস্কার সম্পূর্ণ উচ্ছেদ করে তাওহীদের ওপর প্রতিষ্ঠিত খাঁটি ইবরাহিমী তাওয়াফের সুন্নাত চিরতরে পুনঃপ্রতিষ্ঠা করেন।",
            "It has been revealed in the Holy Quran:\n" +
            "'And let them circumambulate the Ancient House.'\n" +
            "<b>[Surah Al-Hajj: 29]</b>\n\n" +
            "And elsewhere it is ordained:\n" +
            "'And We charged Abraham and Ishmael: Purify My House for those who perform Tawaf and those who stay for worship and those who bow and prostrate.'\n" +
            "<b>[Surah Al-Baqarah: 125]</b>\n\n" +
            "The history of Tawaf is as old as humanity itself. Under divine guidance, Prophet Adam (AS) first constructed the Ka'bah and performed Tawaf around it. Later, after the Great Flood, Prophet Ibrahim (AS) and his son Ismail (AS) reconstructed the Ka'bah and restored the sacred rite of Tawaf. In the pre-Islamic Jahiliyyah era, pagan Arabs introduced distorted practices such as circumambulating naked. Following the Conquest of Makkah and during the Farewell Pilgrimage, the Prophet Muhammad (ﷺ) eradicated all idolatrous customs and revived the pure monotheistic Sunnah of Tawaf for all eternity."
        ));

        // 2. রামল
        items.add(new HajjHistoryCardItem(
            2,
            "রামল",
            "Ramal (Brisk Pace in Tawaf)",
            "রামল শুরু হয় সপ্তম হিজরীতে। রাসূলুল্লাহ (ﷺ)...",
            "Ramal began in the 7th year of Hijrah. The Messenger of Allah (ﷺ)...",
            "রামল শুরু হয় সপ্তম হিজরীতে। রাসূলুল্লাহ (ﷺ) সাহাবায়ে কেরামকে নিয়ে হুদায়বিয়ার সন্ধির শর্তানুযায়ী সপ্তম হিজরীর যিলকদ মাসে 'উমরাতুল কাযা' আদায়ের উদ্দেশ্যে মক্কায় আগমন করেন।\n\n" +
            "মক্কার মুশরিক কুরাইশরা জাবালে কুআইক্বিআন পাহাড়ে সমবেত হয়ে অপপ্রচার চালাতে লাগল যে—'মদীনার ইয়াসরিবের আবহাওয়ার জ্বর ও মহামারীতে মুসলিমরা দুর্বল হয়ে পড়েছে; তারা কাবার তাওয়াফ করারও শক্তি পাবে না।'\n\n" +
            "মুশরিকদের এই অপপ্রচার মিথ্যা প্রমাণ করতে এবং মুসলিমদের অটুট শারীরিক সক্ষমতা, বীরত্ব ও ঈমানী তেজ প্রদর্শন করতে প্রিয়নবী (ﷺ) সাহাবীদেরকে নির্দেশ দিলেন—তাওয়াফের প্রথম তিন চক্করে রামল (বীরদর্পে কাঁধ দুলিয়ে দ্রুত পদক্ষেপে বুক ফুলিয়ে চলা) করার জন্য এবং বাকি চার চক্করে স্বাভাবিক পদক্ষেপে হাঁটার জন্য।\n\n" +
            "<b>[সহীহ বুখারী: ১৬০২, সহীহ মুসলিম: ১২৬৬]</b>\n\n" +
            "পরবর্তীতে মক্কা বিজয়ের পর এবং বিদায় হজেও প্রিয় নবী (ﷺ) রামল করেন। সেই থেকে আজ পর্যন্ত তাওয়াফে কুদূমের প্রথম তিন চক্করে পুরুষদের জন্য রামল করা একটি অবিচ্ছেদ্য সুন্নাতে পরিণত হয়েছে।",
            "Ramal began in the 7th year of Hijrah. The Messenger of Allah (ﷺ) arrived in Makkah with his companions in the month of Dhul Qi'dah to perform Umrat al-Qada under the terms of the Treaty of Hudaybiyyah.\n\n" +
            "The pagan Quraish gathered upon Mount Qu'ayqi'an and spread propaganda, saying: 'The fever of Yathrib (Madinah) has weakened them; they will not even have the strength to circumambulate the Ka'bah.'\n\n" +
            "To shatter this false narrative and demonstrate the vigor, bravery, and radiant strength of the Muslims, the Holy Prophet (ﷺ) instructed the companions to perform Ramal—walking briskly with shoulders swinging and chest held high with dignified vigor—during the first three circuits, and walking normally during the remaining four.\n\n" +
            "<b>[Sahih al-Bukhari: 1602, Sahih Muslim: 1266]</b>\n\n" +
            "The Prophet (ﷺ) continued this practice during the Farewell Pilgrimage, establishing Ramal in the first three rounds of Tawaf al-Qudum as an enduring Sunnah for all generations."
        ));

        // 3. যমযমের পানি ও সাফা মারওয়ার সাঈ
        items.add(new HajjHistoryCardItem(
            3,
            "যমযমের পানি ও সাফা মারওয়ার সাঈ",
            "Zamzam Water & Sa'i between Safa and Marwah",
            "ইবনে আব্বাস (রাঃ) এর এক বর্ণনায় এসেছে, ‘ই...",
            "In a narration of Ibn Abbas (RA), it is mentioned that 'I...",
            "ইবনে আব্বাস (রাঃ) এর এক বর্ণনায় এসেছে, ‘হযরত ইবরাহীম (আ.) আল্লাহর নির্দেশ পেয়ে তাঁর সহধর্মিণী মা হাজেরা ও দুগ্ধপোষ্য শিশুপুত্র হযরত ইসমাঈল (আ.)-কে কোনো ফসল ও পানির চিহ্নহীন নির্জন মক্কার উপত্যকায় রেখে যান।\n\n" +
            "কিছুক্ষণ পর মশক থেকে পানি ফুরিয়ে গেলে তৃষ্ণার্ত শিশু ইসমাঈল তৃষ্ণায় ছটফট করতে থাকে। মা হাজেরা সন্তানের প্রাণ বাঁচাতে ব্যাকুল হয়ে পানির সন্ধানে নিকটবর্তী সাফা পাহাড়ে ওঠেন, কিন্তু কাউকে না পেয়ে উপত্যকা পার হয়ে মারওয়া পাহাড়ে ওঠেন। এভাবে তিনি ব্যাকুল চিত্তে সাফা ও মারওয়ার মাঝে সাতবার ছোটাছুটি করেন।\n\n" +
            "সপ্তম চক্করে মারওয়া পাহাড়ে পৌঁছালে তিনি একটি গায়েবি আওয়াজ শুনতে পান। অতঃপর দেখতে পান যে, শিশু ইসমাঈলের পায়ের গোড়ালির কাছে ফেরেশতা জিব্রাঈল (আ.) তাঁর ডানা দিয়ে আঘাত করলেন এবং সেখান থেকে সুপেয় পানির ফোয়ারা সজোরে উৎসারিত হতে লাগল। মা হাজেরা তখন চারপাশ বাঁধ দিয়ে বলতে লাগলেন—'যম যম' (থামো, থামো)। রাসূলুল্লাহ (ﷺ) বলেন: 'আল্লাহ উম্মে ইসমাঈলের ওপর রহম করুন, যদি তিনি যমযমকে উন্মুক্ত ছেড়ে দিতেন তবে তা এক বহমান নদীতে পরিণত হতো।'\n\n" +
            "<b>[সহীহ বুখারী: ৩৩৬৪]</b>\n\n" +
            "মা হাজেরার সেই গভীর আত্মত্যাগ, আল্লাহর ওপর অবিচল ভরসা ও ব্যাকুল প্রচেষ্টাকে কিয়ামত পর্যন্ত স্মারক হিসেবে জীবন্ত রাখতে আল্লাহ তাআলা সাফা ও মারওয়ার মাঝে সাত চক্কর সাঈ করাকে হজ ও উমরাহর অপরিহার্য রুকন হিসেবে নির্ধারিত করেছেন।",
            "In a narration of Ibn Abbas (RA), it is mentioned that Prophet Ibrahim (AS) left his wife Hajar and infant son Ismail (AS) in the barren, uncultivated valley of Makkah in compliance with Allah's divine command.\n\n" +
            "Soon the water in the waterskin was exhausted, and baby Ismail began crying in agonizing thirst. Distraught, mother Hajar climbed Mount Safa searching for water or signs of caravans. Finding none, she hurried across the valley to Mount Marwah, running back and forth seven times between the two hills in desperate devotion.\n\n" +
            "Upon reaching Marwah the seventh time, she heard a voice. She saw Angel Jibreel (AS) striking the ground near baby Ismail's heel, causing pure sweet water to gush forth miraculously. Hajar quickly enclosed it, saying 'Zam Zam' (halt, halt). The Prophet (ﷺ) said: 'May Allah bestow mercy on the mother of Ismail; had she left Zamzam untouched, it would have been a flowing stream.'\n\n" +
            "<b>[Sahih al-Bukhari: 3364]</b>\n\n" +
            "To immortalize her unmatched faith and supreme maternal devotion, Almighty Allah ordained Sa'i between Safa and Marwah as a mandatory pillar of Hajj and Umrah."
        ));

        // 4. উকুফে আরাফা
        items.add(new HajjHistoryCardItem(
            4,
            "উকুফে আরাফা",
            "Wuquf at Arafah (Standing on the Plain of Arafah)",
            "আমরা সুনির্দিষ্ট স্থানের বাইরে উকুফে আরাফা কর...",
            "We will not perform Wuquf outside the designated sanctuary...",
            "আমরা সুনির্দিষ্ট স্থানের বাইরে উকুফে আরাফা করব না—জাহেলিয়াত যুগে মক্কার কুরাইশরা এমন মনগড়া অহমিকা প্রদর্শন করত। কুরাইশরা নিজেদের 'হুমস' (হারামের সম্মানিত রক্ষক) দাবি করত এবং বলত: 'আমরা হারামের অধিবাসী ও আল্লাহর প্রতিবেশী, তাই আমরা হারামের সীমানা পেরিয়ে সাধারণ মানুষের মতো আরাফাতের ময়দানে (যা হারামের সীমানার বাইরে অবস্থিত) যাব না।'\n\n" +
            "ফলে আরবের সাধারণ হাজিরা আরাফায় গেলেও কুরাইশরা হারামের সীমানার ভেতরে মুজদালিফাতেই অবস্থান করত।\n\n" +
            "কিন্তু বিশ্বনবী হযরত মুহাম্মদ (ﷺ) এই বৈষম্যমূলক ও গোত্রীয় অহংকার সম্পূর্ণ চূর্ণ করে দেন। তিনি হারামের সীমানা অতিক্রম করে স্বয়ং আরাফাতের ময়দানে গমন করেন এবং উকুফ (অবস্থান) করেন। এ প্রসঙ্গে আল্লাহ তাআলা পবিত্র কুরআনে সুস্পষ্ট নির্দেশ নাযিল করেন:\n" +
            "'অতঃপর অন্য মানুষেরা যেখান থেকে (আরাফাত থেকে) ফিরে আসে, তোমরাও সেখান থেকে ফিরে এসো এবং আল্লাহর কাছে ক্ষমা প্রার্থনা করো। নিশ্চয়ই আল্লাহ ক্ষমাশীল, পরম দয়ালু।'\n" +
            "<b>[সূরা আল-বাক্বারা: ১৯৯]</b>\n\n" +
            "রাসূলুল্লাহ (ﷺ) দ্ব্যর্থহীন কণ্ঠে ঘোষণা করেন: 'হজই হলো আরাফাহ।' অর্থাৎ ৯ই জিলহজ আরাফার ময়দানে অবস্থান করাই হজের কেন্দ্রীয় রুকন।\n" +
            "<b>[জামে আত-তিরমিযী: ৮৮৯, সুনানে আবু দাউদ: ১৯৪৯]</b>",
            "We will not perform Wuquf outside the designated sanctuary—such was the elitist arrogance displayed by the pagan Quraish in the pre-Islamic era. Deeming themselves the 'Hums' (elite guardians of the Sacred Sanctuary), they asserted: 'We are the people of the Sacred House, so we will not step outside the Haram boundaries into the plain of Arafat like common pilgrims.'\n\n" +
            "Thus, while common Arabs gathered at Arafat, the Quraish remained inside the boundary at Muzdalifah.\n\n" +
            "Prophet Muhammad (ﷺ) dismantled this aristocratic discrimination. He rode beyond the Haram boundary directly into the plains of Arafat and stood there with all believers. To cement this equality, Allah revealed:\n" +
            "'Then depart from where the people depart and seek Allah\\'s forgiveness. Indeed, Allah is Forgiving and Merciful.'\n" +
            "<b>[Surah Al-Baqarah: 199]</b>\n\n" +
            "The Messenger of Allah (ﷺ) solemnly proclaimed: 'Hajj is Arafah'—affirming that standing on the plains of Arafat on the 9th of Dhul Hijjah is the quintessential pillar of Hajj.\n" +
            "<b>[Jami' at-Tirmidhi: 889, Sunan Abi Dawud: 1949]</b>"
        ));

        // 5. কাবা ও হজের ইতিহাস
        items.add(new HajjHistoryCardItem(
            5,
            "কাবা ও হজের ইতিহাস",
            "History of the Ka'bah & Hajj",
            "আল্লাহ তা‘আলা বলেন:",
            "Almighty Allah states:",
            "আল্লাহ তা‘আলা বলেন:\n" +
            "'এবং স্মরণ করো, যখন আমি ইবরাহীমকে বায়তুল্লাহর স্থান নির্ধারণ করে দিয়েছিলাম এই বলে যে, আমার সাথে কাউকে শরিক করো না এবং আমার গৃহকে পবিত্র রাখো তাওয়াফকারী, দণ্ডায়মান এবং রুকু ও সিজদাকারীদের জন্য। এবং মানুষের মাঝে হজের সাধারণ ঘোষণা দাও; তারা তোমার কাছে আসবে দূর-দূরান্তের গভীর গিরিপথ থেকে পায়ে হেঁটে ও সর্বপ্রকার ক্ষীণকায় উটের পিঠে চড়ে।'\n" +
            "<b>[সূরা আল-হাজ্জ: ২৬-২৭]</b>\n\n" +
            "হযরত ইবরাহীম (আ.) ও তাঁর পুত্র হযরত ইসমাঈল (আ.) আল্লাহর নির্দেশ পেয়ে মাকামে ইবরাহীমের পাথরে দাঁড়িয়ে পবিত্র কাবার প্রাচীর নির্মাণ করেন। নির্মাণ সমাপ্ত হলে আল্লাহর আদেশে ইবরাহীম (আ.) আবু কুবাইস পাহাড়ে দাঁড়িয়ে বিশ্ববাসীকে হজের আহ্বান জানান। আল্লাহ তাআলা সেই আহ্বান কিয়ামত পর্যন্ত আগত সমস্ত মানুষের রুহের কাছে পৌঁছে দেন, যার জবাবে মুমিনগণ ধ্বনি তোলে: 'লাব্বাইক আল্লাহুম্মা লাব্বাইক'।\n\n" +
            "কালের পরিক্রমায় পৌত্তলিক কুরাইশরা কাবার ভেতরে ৩৬০টি মূর্তি স্থাপন করে এটিকে পৌত্তলিকতার কেন্দ্রে রূপান্তর করেছিল। অষ্টম হিজরীতে মক্কা বিজয়ের দিন রাসূলুল্লাহ (ﷺ) স্বহস্তে প্রতিটি মূর্তি ভেঙে চুরমার করে কাবাকে তাওহীদের মূল পবিত্রতায় ফিরিয়ে আনেন। অতঃপর দশম হিজরীর বিদায় হজে প্রায় এক লক্ষ চল্লিশ হাজার সাহাবীকে নিয়ে প্রিয় নবী (ﷺ) ঐতিহাসিক হজ আদায় করে হজের প্রতিটি আহকাম ও নিয়মাবলী কিয়ামতের জন্য চূড়ান্ত ও অপরিবর্তনীয় রূপ দান করেন।",
            "Almighty Allah states in the Holy Quran:\n" +
            "'And when We designated for Abraham the site of the House, saying, Do not associate anything with Me and purify My House for those who perform Tawaf and those who stand and those who bow and prostrate. And proclaim to the people the Hajj; they will come to you on foot and on every lean camel from every deep mountain highway.'\n" +
            "<b>[Surah Al-Hajj: 26-27]</b>\n\n" +
            "Prophet Ibrahim (AS) and his son Ismail (AS) constructed the walls of the Ka'bah standing upon the stone of Maqam Ibrahim. Upon completion, Ibrahim (AS) proclaimed Hajj from Mount Abu Qubays. Allah caused that timeless call to echo into the souls of all mankind until the Day of Judgment, to which the faithful respond: 'Labbayk Allahumma Labbayk'.\n\n" +
            "Over generations, pagans introduced 360 idols inside the Sanctuary. On the Conquest of Makkah in 8 AH, the Messenger of Allah (ﷺ) personally shattered every idol, restoring the House to pristine monotheism. In 10 AH, during the historic Farewell Pilgrimage alongside over 140,000 companions, the Prophet (ﷺ) perfected every rite of Hajj for all generations to follow."
        ));

        return items;
    }
}
