package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjIhramContentRepository {

    public static List<HajjHistoryCardItem> getIhramCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. ইহরাম এর বিবরণ
        list.add(new HajjHistoryCardItem(
            1,
            "ইহরাম এর বিবরণ",
            "Description of Ihram",
            "হজ্জ ও ওমরার সর্বপ্রথম কাজ ইহরাম: হজ্জ অথবা ওমরার নিয়ত করে নির্দিষ্ট পোশাকে প্রবেশ করা...",
            "The foremost rite of Hajj and Umrah is Ihram: entering with intention in designated garments...",
            "হজ্জ ও ওমরার সর্বপ্রথম কাজ ইহরাম: হজ্জ অথবা ওমরার নিয়তে নির্দিষ্ট পোশাকে প্রবেশ করা এবং তালবিয়াহ পাঠ করা।\n\n'ইহরাম' শব্দের অর্থ কোনো কিছুকে নিজের ওপর নিষিদ্ধ বা হারাম করে নেওয়া। ইহরামে প্রবেশের মাধ্যমে বান্দা স্বাভাবিক অবস্থার কিছু হালাল কাজ (যেমন: সুগন্ধি ব্যবহার, নখ বা চুল কাটা, সেলাইযুক্ত কাপড় পরা) আল্লাহর সন্তুষ্টির উদ্দেশ্যে নির্দিষ্ট সময়ের জন্য নিজের ওপর হারাম করে নেন। ইহরামের মাধ্যমে ধনী-গরিব, রাজা-প্রজা সব ভেদাভেদ ভুলে একই সাদাসিধা পোশাকে আল্লাহর দরবারে উপস্থিত হয়।",
            "The foremost essential pillar of Hajj and Umrah is Ihram: assuming the sacred state with sincere intention and entering into designated attire accompanied by the Talbiyah.\n\nLinguistically, 'Ihram' means declaring certain permissible matters sacred or prohibited upon oneself. By entering Ihram, a believer temporarily forbids upon themselves normally lawful actions (such as perfumes, trimming hair or nails, tailored clothing) purely for Allah's pleasure. Ihram completely erases distinctions between rich and poor, king and pauper, gathering all before Allah in identical humble white attire."
        ));

        // 2. ওমরা অথবা হজ্জের জন্য নিয়ত
        list.add(new HajjHistoryCardItem(
            2,
            "ওমরা অথবা হজ্জের জন্য নিয়ত",
            "Intention (Niyyah) for Umrah or Hajj",
            "শুধুমাত্র ওমরার জন্য: \"লাব্বাইকা ওমরাহ\" তামাত্তু হজে: \"লাব্বাইকা ওমরাতান মুতামাত্তিয়ান বিহা ইলাল হজ\"...",
            "For Umrah only: 'Labbayka Umrah', for Tamattu Hajj: 'Labbayka Umratan Mutamatti'an biha ilal-Hajj'...",
            "<b>শুধুমাত্র ওমরার জন্য:</b>\n\"লাব্বাইকা ওমরাহ\" (لَبَّيْكَ عُمْرَةً)\n\n<b>তামাত্তু হজের জন্য (ওমরার ইহরামকালে):</b>\n\"লাব্বাইকা ওমরাতান মুতামাত্তিয়ান বিহা ইলাল হজ\" (لَبَّيْكَ عُمْرَةً مُتَمَتِّعًا بِهَا إِلَى الْحَجِّ)\n\n<b>ইফরাদ হজের জন্য (শুধু হজের নিয়ত):</b>\n\"লাব্বাইকা হাজ্জা\" (لَبَّيْكَ حَجًّا)\n\n<b>কিরান হজের জন্য (ওমরা ও হজ একত্রে):</b>\n\"লাব্বাইকা ওমরাতান ওয়া হাজ্জান\" (لَبَّيْكَ عُمْرَةً وَحَجًّا)\n\nনিয়ত করার পর পুরুষরা উচ্চৈঃস্বরে এবং নারীরা নিচু স্বরে তালবিয়াহ পাঠ করবেন:\n'লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুলক, লা শারীকা লাক।'",
            "<b>For Umrah only:</b>\n\"Labbayka 'Umrah\" (لَبَّيْكَ عُمْرَةً)\n\n<b>For Tamattu Hajj (during Umrah Ihram):</b>\n\"Labbayka 'Umratan Mutamatti'an biha ilal-Hajj\" (لَبَّيْكَ عُمْرَةً مُتَمَتِّعًا بِهَا إِلَى الْحَجِّ)\n\n<b>For Ifrad Hajj (Hajj only):</b>\n\"Labbayka Hajjan\" (لَبَّيْكَ حَجًّا)\n\n<b>For Qiran Hajj (both combined):</b>\n\"Labbayka 'Umratan wa Hajjan\" (لَبَّيْكَ عُمْرَةً وَحَجًّا)\n\nAfter verbal intention, men proclaim aloud and women softly recite the Talbiyah:\n'Labbayk Allahumma Labbayk, Labbayka la shareeka laka Labbayk, Innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.'"
        ));

        // 3. ইহরামের পূর্বে কর্মকাণ্ড
        list.add(new HajjHistoryCardItem(
            3,
            "ইহরামের পূর্বে কর্মকাণ্ড",
            "Actions Before Entering Ihram",
            "•নখ কাটুন, গোঁফ ছোট করুন, বগল ও নাভীর নিচের লোম পরিষ্কার করুন, উত্তমরূপে গোসল করুন...",
            "•Trim nails, clip mustache, remove underarm and pubic hair, take a complete ritual bath...",
            "ইহরাম বাঁধার পূর্বে শারীরিক পরিচ্ছন্নতা ও প্রস্তুতির জন্য নিম্নোক্ত কাজগুলো করা সুন্নাত ও মুস্তাহাব:\n\n• নখ কাটুন, গোঁফ ছোট করুন, বগল ও নাভীর নিচের লোম পরিষ্কার করুন।\n• উত্তমরূপে গোসল (গোসল সম্ভব না হলে অজু) করুন। ঋতুবতী নারীদের জন্যও এ গোসল করা সুন্নাত।\n• ইহরামের কাপড় পরার পূর্বে শরীরে (কাপড়ে নয়) উত্তম সুগন্ধি বা আতর ব্যবহার করুন।\n• পুরুষরা সেলাইবিহীন দুটি সাদা চাদর (একটি তাহবন্দ, অন্যটি গায়ের চাদর) পরিধান করুন।\n• নারীরা সতর আবৃত করে যেকোনো শালীন ও মার্জিত স্বাভাবিক পোশাক পরিধান করুন।\n• মীকাত অতিক্রমের পূর্বে সম্ভব হলে দুই রাকাত নফল নামাজ আদায় করুন।",
            "Before entering Ihram, performing the following personal hygiene and Sunnah preparations is highly recommended:\n\n• Clip nails, trim the mustache, and clean underarm and pubic hair.\n• Take a thorough ritual bath (Ghusl) or perform Wudu if water is unavailable. This bath is also Sunnah for menstruating women.\n• Apply pleasant perfume or Attar to the body (not onto the Ihram garments themselves) before wearing them.\n• Men put on two white seamless unstitched sheets (one around waist, one over shoulders).\n• Women wear their normal modest, loose-fitting Islamic clothing covering the entire body except face and hands.\n• Pray two optional Rak'ahs before crossing the Miqat station."
        ));

        // 4. ইহরাম অবস্থায় অনুমোদিত কার্যাবলী
        list.add(new HajjHistoryCardItem(
            4,
            "ইহরাম অবস্থায় অনুমোদিত কার্যাবলী",
            "Permissible Actions While in Ihram",
            "হাতঘড়ি, চশমা, হেডফোন, বেল্ট, মানিব্যাগ, শ্রবণযন্ত্র, আংটি ইত্যাদি ব্যবহার করা সম্পূর্ণ বৈধ...",
            "Using wristwatches, spectacles, headphones, waist belts, wallets, hearing aids, rings is permissible...",
            "ইহরাম অবস্থায় যা কিছু ব্যবহার ও করা বৈধ:\n\n• হাতঘড়ি, চশমা, সানগ্লাস, হেডফোন, মোবাইল ও শ্রবণযন্ত্র ব্যবহার করা।\n• সেলাইযুক্ত চামড়া বা কাপড়ের বেল্ট ও টাকা-পয়সা রাখার মানিব্যাগ পরা।\n• আংটি পরা এবং আঙুলে মেসওয়াক বা ব্রাশ ব্যবহার করা।\n• রোদ ও গরম থেকে বাঁচতে ছাতা ব্যবহার করা বা তাঁবু ও গাড়ির ছায়ায় অবস্থান করা।\n• সুগন্ধিমুক্ত সাবান দিয়ে হাত ধোয়া বা শরীরে ময়লা দূর করতে সুগন্ধিমুক্ত অবস্থায় গোসল করা।\n• অনিচ্ছাকৃতভাবে মাথা চুলকানোর সময় বা অজু করার সময় দু-একটি চুল ঝরে পড়লে কোনো ক্ষতি নেই।\n• স্যান্ডেল পরা যাতে পায়ের পাতার ওপরের উঁচু অংশ উন্মুক্ত থাকে।",
            "The following are fully permissible while in the state of Ihram:\n\n• Wearing wristwatches, eyeglasses, sunglasses, headphones, smartphones, and hearing aids.\n• Fastening stitched leather/fabric waist belts and wallets to safeguard money and travel documents.\n• Wearing rings and using Miswak or toothbrush.\n• Using umbrellas for shade or sitting under tents, roofed buildings, and vehicles.\n• Taking a bath without scented soap to cool off or clean dirt.\n• If a few hairs fall out unintentionally while making Wudu or scratching gently, there is no expiation.\n• Wearing sandals that leave the instep of the foot exposed."
        ));

        // 5. ইহরামের পর যেসব বিষয় নিষিদ্ধ
        list.add(new HajjHistoryCardItem(
            5,
            "ইহরামের পর যেসব বিষয় নিষিদ্ধ",
            "Prohibited Matters After Entering Ihram",
            "চুল, নখ ও দাঁড়ি কাটা। (তবে মাথায় চিরুনি করা অনুচিত যাতে চুল না ছিঁড়ে), সুগন্ধি ব্যবহার...",
            "Cutting hair, nails, beard. (Avoid combing vigorously), applying perfumes, tailored clothes for men...",
            "ইহরামে প্রবেশের পর নিম্নোক্ত বিষয়গুলো কঠোরভাবে নিষিদ্ধ:\n\n• চুল, নখ ও দাঁড়ি কাটা বা উপড়ানো। (এমনকি মাথায় শক্তভাবে চিরুনি করা অনুচিত যাতে চুল না ছিঁড়ে যায়)।\n• শরীরে বা কাপড়ে কোনো প্রকার সুগন্ধি, আতর বা সুগন্ধিযুক্ত সাবান ব্যবহার করা।\n• পুরুষদের জন্য সেলাইযুক্ত কোনো পোশাক (যেমন: জামা, পায়জামা, গেঞ্জি, অন্তর্বাস, টুপি) পরিধান করা।\n• পুরুষদের জন্য মাথা বা কান ঢেকে রাখা এবং নারীদের জন্য মুখমণ্ডল সরাসরি নিকাব দিয়ে আঁটসাঁট করে ঢাকা বা হাতমোজা পরা।\n• স্থলভাগের কোনো বন্য প্রাণী শিকার করা বা শিকারে কোনো প্রকার সহায়তা করা।\n• বৈবাহিক প্রস্তাব দেওয়া বা বিয়ে সম্পন্ন করা।\n• যৌন মিলন, যৌনস্পর্শ এবং কামোদ্দীপক কথা ও আচরণ করা।\n• ঝগড়া-বিবাদ, গালিগালাজ ও যেকোনো ধরনের পাপাচারমূলক কাজ।",
            "Upon entering Ihram, the following violations are strictly forbidden:\n\n• Cutting, trimming, or plucking hair, nails, or beard (even combing hair vigorously should be avoided to prevent pulling hairs).\n• Applying any perfumes, scented oils, or scented soaps to body or clothing.\n• For men: wearing tailored garments (shirts, pants, underwear, socks, caps).\n• For men: covering the head or ears; For women: wearing tight Niqab veils directly touching the face or gloves.\n• Hunting wild land game or assisting in hunting.\n• Proposing marriage or contracting a marriage.\n• Sexual intercourse, intimate physical contact, and indecent speech.\n• Quarreling, arguing, cursing, and all sinful conduct."
        ));

        // 6. ইহরামের বিধান লঙ্ঘনের কাফফারা
        list.add(new HajjHistoryCardItem(
            6,
            "ইহরামের বিধান লঙ্ঘনের কাফফারা",
            "Expiation (Kaffarah) for Violating Rules of Ihram",
            "ইহরাম অবস্থায় কারো সঙ্গে যৌন সঙ্গম করলে তা হজ বাতিল করে দেয়। অন্যান্য ভুলের জন্য দম বা সদকা ওয়াজিব...",
            "Sexual intercourse invalidates Hajj. For other violations, Dam (sacrifice) or Sadaqah is mandatory...",
            "ইহরামের কোনো নিষেধাজ্ঞা ভঙ্গ হলে শরীয়তসম্মত কাফফারা (ফিদইয়া) আদায় করতে হয়:\n\n• <b>হজ বাতিলকারী অপরাধ:</b> আরাফাতে অবস্থানের পূর্বে ইহরাম অবস্থায় যৌন মিলন করলে হজ সম্পূর্ণ বাতিল হয়ে যায়। তবে তাকে হজের বাকি কাজগুলো সম্পন্ন করতে হবে এবং আগামী বছর পুনরায় এই হজ কাজা করতে হবে এবং একটি উট বা গরু কোরবানি দিতে হবে।\n• <b>'দম' ওয়াজিবকারী ভুল:</b> পুরুষদের একদিন বা একরাত সেলাইযুক্ত কাপড় পরা, পুরো মাথা বা মুখ ঢাকা, সম্পূর্ণ অঙ্গে সুগন্ধি লাগানো, বা হাত-পায়ের সমস্ত নখ একসাথে কাটলে একটি ছাগল/ভেড়া কোরবানি (দম) দেওয়া ওয়াজিব।\n• <b>সদকা ওয়াজিবকারী ভুল:</b> সামান্য সুগন্ধি লাগানো বা এক-দুটি চুল/নখ কাটলে সদকায়ে ফিতর পরিমাণ খাদ্য দরিদ্রদের দান করলেই কাফফারা আদায় হয়ে যাবে।\n• ইচ্ছাকৃত বা অনিচ্ছাকৃত যেকোনো ভুলের জন্য দ্রুত মহান আল্লাহর কাছে আন্তরিক তাওবা ও ক্ষমা প্রার্থনা করা ওয়াজিব।",
            "When a prohibition of Ihram is breached, expiation (Fidya/Kaffarah) must be offered according to Shariah:\n\n• <b>Invalidating Violation:</b> Engaging in sexual intercourse before standing at Arafah completely invalidates the Hajj. The pilgrim must complete remaining rites, make up the Hajj the following year, and sacrifice a camel or cow.\n• <b>Violations Requiring 'Dam' (Sacrifice):</b> Wearing tailored clothes for a full day/night (men), covering the entire head/face for a day (men), applying perfume to a full limb, or clipping all nails at once necessitates sacrificing a sheep/goat within the Haram boundary.\n• <b>Violations Requiring Sadaqah:</b> Applying a minor amount of perfume or trimming only one or two hairs/nails is expiated by donating the equivalent of Sadaqat al-Fitr to the needy.\n• For both intentional and unintentional slips, immediate sincere repentance (Tawbah) before Allah is obligatory."
        ));

        return list;
    }
}
