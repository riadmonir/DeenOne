package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjArticleCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjArticleContentRepository {

    public static boolean hasArticleCardsForTopic(int topicId) {
        return topicId == 1 || topicId == 2 || topicId == 4 || topicId == 6 || topicId == 8 || topicId == 9 || topicId == 10 || topicId == 11 || topicId == 12 || topicId == 13 || topicId == 14 || topicId == 15 || topicId == 17 || topicId == 18 || topicId == 20 || topicId == 22;
    }

    public static List<HajjArticleCardItem> getArticleCardsForTopic(int topicId) {
        if (topicId == 2) {
            return getVirtuesArticleCards();
        } else if (topicId == 4) {
            return getThreeTypesOfHajjArticleCards();
        } else if (topicId == 6) {
            return getHajjPreparationArticleCards();
        } else if (topicId == 9) {
            return getHairCuttingArticleCards();
        } else if (topicId == 10) {
            return getTawafIfadahArticleCards();
        } else if (topicId == 11) {
            return getJamaratArticleCards();
        } else if (topicId == 13 || topicId == 15) {
            return getFarewellTawafArticleCards();
        } else if (topicId == 8 || topicId == 18) {
            return getAdhiArticleCards();
        } else if (topicId == 12) {
            return getMinaArticleCards();
        } else if (topicId == 14) {
            return getSaiArticleCards();
        } else if (topicId == 17) {
            return getSafaMarwahArticleCards();
        } else if (topicId == 20) {
            return getFarewellHajjArticleCards();
        } else if (topicId == 22) {
            return getAyyamAtTashreeqArticleCards();
        }
        return getHajjObligationArticleCards();
    }

    public static List<HajjArticleCardItem> getHajjArticleCards() {
        return getHajjObligationArticleCards();
    }

    public static List<HajjArticleCardItem> getHajjObligationArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: হজ ফরজ হওয়ার শর্ত / Conditions for Hajj Obligation
        cards.add(new HajjArticleCardItem(
            1,
            "হজ ফরজ হওয়ার শর্ত",
            "Conditions for the Obligation of Hajj",
            "ইসলামের ফরজ পাঁচটি। প্রিয়নবী হযরত মুহাম্মদ (সা.) বলেছেন, ইসলামের ভিত্তি পাঁচটি: আল্লাহ ছাড়া কোনো ইলাহ্ নেই, হযরত মুহাম্মদ (সা.) তার বান্দা ও রাসূল এ কথার সাক্ষ্য দেয়া, সালাত (নামাজ) কায়েম করা, যাকাত প্রদান করা, বায়তুল্লাহর হজ করা এবং মাহে রমজানে রোযা রাখা তবে ফরজ ইবাদত সমূহের মধ্যে যাকাত ও হজের ক্ষেত্রে আর্থিক সামর্থ্যের বিধান রয়েছে। অর্থাৎ প্রত্যেক সামর্থ্যবান মুসলিমের জন্য হজ পালন করা অত্যাবশ্যকীয়। 'প্রত্যেক সামর্থ্যবান মানুষের ওপর বায়তুল্লাহর হজ করা\n\n" +
            "আর্থিকভাবে সামর্থ্যবান প্রত্যেক মুসলমানের ওপর হজ ফরজ হওয়ার শর্তসমূহ:\n" +
            "• মুসলমান হওয়া\n" +
            "• জ্ঞানসম্পন্ন হওয়া\n" +
            "• প্রাপ্ত বয়স্ক হওয়া\n" +
            "• স্বাধীন হওয়া\n" +
            "• সামর্থ্য থাকা\n\n" +
            "ইরশাদ করা হয়েছে:\n" +
            "মানুষের মধ্যে যার সেখানে যাওয়ার সামর্থ্য আছে, আল্লাহর উদ্দেশ্যে ঐ গৃহের হজ্জ্ব করা তার (পক্ষে) অবশ্য কর্তব্য।'\n\n" +
            "<b>[সূরা আলে ইমরান-৯৭]</b>\n\n" +
            "বলে রাখা প্রয়োজন, অনেক ক্ষেত্রে যাকাত ফরজ না হয়েও হজ ফরজ হতে পারে। তবে হজ ও যাকাতের ক্ষেত্রে আর্থিক সামর্থ্য থাকা আবশ্যক। হজ ও যাকাতের কিছু পার্থক্য রয়েছে। যাকাতের সঙ্গে নিসাবের সম্পর্ক। যে পরিমাণ অর্থ থাকলে যাকাত ফরজ হয়, তাকে নিসাব বলে।\n" +
            "আর মক্কায় গিয়ে আবার ফিরে আসা পর্যন্ত সামর্থ্য থাকলে হজ ফরজ হয়। কেউ যদি সম্পদ অথবা স্থাবর সম্পত্তির কিছু অংশ বিক্রি করে হজে যায় আবার হজ থেকে ফিরে এসে বাকি সম্পত্তি দিয়ে জীবন নির্বাহ করতে পারে তবে তার ওপর হজ ফরজ।",
            "The obligatory pillars of Islam are five. The Holy Prophet Muhammad (pbuh) said, 'Islam is built on five pillars: testifying that there is no god but Allah and that Muhammad is His servant and messenger, establishing regular prayer, giving zakat, performing pilgrimage to the House (Ka'bah), and fasting during Ramadan.' However, among the obligatory worships, financial and physical capability is explicitly required for Hajj and Zakat. Hence, performing Hajj is mandatory for every capable Muslim.\n\n" +
            "Conditions for Hajj becoming obligatory upon every financially capable Muslim:\n" +
            "• Being a Muslim\n" +
            "• Being of sound mind (sane)\n" +
            "• Having reached adulthood (puberty)\n" +
            "• Being free (not enslaved)\n" +
            "• Possessing adequate financial & physical capability\n\n" +
            "It has been ordained in the Holy Quran:\n" +
            "'And pilgrimage to the House is a duty unto Allah for mankind, for him who can find a way thither.'\n\n" +
            "<b>[Surah Ali 'Imran: 97]</b>\n\n" +
            "It should be noted that in many cases, Hajj may become obligatory even if Zakat is not due upon a person. However, financial capability is essential for both. The difference lies in Nisab: Zakat depends on reaching the Nisab threshold, while Hajj depends on having sufficient funds to travel to Makkah and return, while ensuring adequate maintenance for one's family."
        ));

        // Card 2: হজ কী / What is Hajj?
        cards.add(new HajjArticleCardItem(
            2,
            "হজ কী",
            "What is Hajj?",
            "হজের শাব্দিক অর্থ 'কোনো মহৎ কাজের ইচ্ছা করা'। পরিভাষায় 'নির্দিষ্ট দিনে হজের নিয়তে ইহরাম অবস্থায় আরাফার ময়দানে অবস্থান করা এবং বায়তুল্লাহ শরিফ তাওয়াফ করা'।।\n\n" +
            "<b>[শামি-২/৪৫৪]</b>\n\n" +
            "সর্বপ্রথম পৃথিবীতে হজ পালন করেন হজরত আদম (আ:)। বিভিন্ন তাফসির গ্রন্থে উল্লেখ রয়েছে, হজরত আদম আ: আল্লাহ তায়ালার নির্দেশে ফেরেশতাদের মাধ্যমে পবিত্র মক্কা নগরীতে এসে বায়তুল্লাহর ভিত্তি স্থাপন করেন এবং হজ পালন করেন। এর পর থেকেই হজের ধারাবাহিকতা চলতে থাকে।\n\n" +
            "<b>[আইয়ানুল হাজ্জাজ - ২২-২৪]</b>\n\n" +
            "আবু হুরায়রা রা: থেকে বর্ণিত:\n" +
            "রাসুল (সা:) ইরশাদ করেছেন- 'হজরত আদম (আ:) যখন পৃথিবীতে অবতরণ করেন তখন তিনি কাবাঘর সাতবার তাওয়াফ করেন। অতঃপর বর্তমানে যেটি মাকামে ইবরাহিম সেখানে দু'রাকাত সালাত আদায় করেন এবং কাকুতি-মিনতি করে প্রভুর দরবারে দোয়া করেন।'\n\n" +
            "<b>[মাজমাউজ জাওয়ায়েদ-১/১৮৩]</b>",
            "Linguistically, Hajj means 'intending or resolving upon a noble act'. In Islamic Shariah terminology, it means 'staying in the plains of Arafat in the state of Ihram with the intention of Hajj on specific prescribed days, and circumambulating (Tawaf) the Sacred House of Allah (Ka'bah)'.\n\n" +
            "<b>[Radd al-Muhtar / Shami 2:454]</b>\n\n" +
            "The first human to perform Hajj on earth was Prophet Adam (AS). Various classical exegeses mention that under divine instruction and guided by angels, Prophet Adam (AS) arrived in the holy city of Makkah, established the foundations of the Baytullah, and performed Hajj. The continuity of Hajj has proceeded ever since.\n\n" +
            "<b>[A'yanul Hajjaj 22-24]</b>\n\n" +
            "Narrated by Abu Hurairah (RA):\n" +
            "The Messenger of Allah (pbuh) said: 'When Adam (AS) descended upon the earth, he circumambulated the Ka'bah seven times. Then at the place now known as Maqam Ibrahim, he performed two units of prayer and earnestly supplicated before his Lord.'\n\n" +
            "<b>[Majma' az-Zawa'id 1:183]</b>"
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getVirtuesArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: হজ্জের ফজিলত (Virtues & Significance of Hajj) - 100% Verbatim matching user request & screenshot
        cards.add(new HajjArticleCardItem(
            1,
            "হজ্জের ফজিলত",
            "Virtues of Hajj",
            "মাবরুর হজ্জের প্রতিদান জান্নাত ভিন্ন অন্য কিছু নয়।\n\n" +
            "<b>[বোখারি - ১৬৫০]</b>\n\n" +
            "যে হজ্জ করল ও শরিয়ত অনুমতি দেয় না এমন কাজ থেকে বিরত রইল, যৌন-স্পর্শ রয়েছে এমন কাজ ও কথা থেকে বিরত থাকল, সে তার মাতৃ-গর্ভ হতে ভূমিষ্ট ‘হওয়ার দিনের মতো পবিত্র হয়ে ফিরে এল।\n\n" +
            "<b>[বোখারি- ১৪২৪]</b>\n\n" +
            "আরাফার দিন এতো সংখ্যক মানুষকে জাহান্নাম থেকে মুক্তি দেন যা অন্য কোনো দিন দেন না। এদিন আল্লাহ তাআলা নিকটবর্তী হন ও আরাফার ময়দানে অবস্থানরত হাজিদেরকে নিয়ে তিনি ফেরেশতাদের সাথে গর্ব করেন, ও বলেন ‘ওরা কী চায়?।\n\n" +
            "<b>[মুসলিম - ২/৯৮৩]</b>\n\n" +
            "সর্বোত্তম আমল কী এ ব্যাপারে এক ব্যক্তি রাসূলুল্লাহ (ﷺ)কে জিজ্ঞাসা করলেন।\n" +
            "<b>উত্তরে বললেন:</b>\n" +
            "‘অদ্বিতীয় আল্লাহর প্রতি ঈমান, ও তারপর মাবরুর হজ্জ যা সকল আমল থেকে শ্রেষ্ঠ। সূর্য উদয় ও অস্তের মধ্যে যে পার্থক্য ঠিক তারই মত।\n\n\n" +
            "<b>[আহমদ - ৪/৩৪২]</b>\n\n" +
            "অন্য এক হাদিসে এসেছে, ‘উত্তম আমল কি এই মর্মে রাসূলুল্লাহ (ﷺ)-কে জিজ্ঞাসা করা হল। উত্তরে তিনি বললেন, ‘আল্লাহ ও তাঁর রাসূলের প্রতি ঈমান। বলা হল, ‘তারপর কী’?\n" +
            "<b>তিনি বললেন:</b>\n" +
            "'আল্লাহর পথে জিহাদ। বলা হল তারপর কোনটি? তিনি বললেন, মাবরুর হজ্জ।'\n\n\n" +
            "<b>[বোখারি - ১৪২২]</b>\n\n" +
            "একদা রাসূলুল্লাহ (ﷺ)-কে প্রশ্ন করে আয়েশা (রাঃ) বলেন, ‘ইয়া রাসূলুল্লাহ! আমরা কি আপনাদের সাথে জিহাদে ও অভিযানে যাব না?\n" +
            "<b>তিনি বলেন:</b>\n" +
            "‘তোমাদের জন্য উত্তম ও সুন্দরতম জিহাদ হল ‘হজ্জ’, তথা মাবরুর হজ্জ।'\n\n\n" +
            "<b>[ফাতহুল বারি - ৪/১৮৬১]</b>\n\n" +
            "‘হজ্জ ও উমরা পালনকারীগণ আল্লাহর অফদ-মেহমান। তারা যদি আল্লাহকে ডাকে আল্লাহ তাদের ডাকে সাড়া দেন। তারা যদি গুনাহ মাফ চায় আল্লাহ তাদের গুনাহ মাফ করে দেন।’\n\n" +
            "<b>[ইবনে মাযাহ - ২৮৮৩]</b>\n\n" +
            "আবু হুরায়রা থেকে বর্ণিত এক হাদিসে এসেছে,\n" +
            "রাসূলুল্লাহ (ﷺ)বলেছেন:\n" +
            "'এক উমরা হতে অন্য উমরা, এ দুয়ের মাঝে যা কিছু (পাপ) ঘটবে তার জন্য কাফফারা। আর মাবরুর হজ্জের বিনিময় জান্নাত ভিন্ন অন্য কিছু নয়।'\n\n\n" +
            "<b>[বোখারি - ১৬৫০]</b>\n\n" +
            "হাদিসে আরো এসেছে, রাসূলুল্লাহ (ﷺ)বলেছেন:\n" +
            "‘কারো ইসলাম-গ্রহণ পূর্বকৃত সকল পাপকে মুছে দেয়। হিজরত তার পূর্বের সকল গুনাহ মুছে দেয়, ও হজ্জ তার পূর্বের সকল পাপ মুছেদেয়।'\n\n\n" +
            "<b>[মুসলিম - ১৭৩]</b>\n\n" +
            "ইবনে মাসউদ হতে বর্ণিত এক হাদিসে এসেছে, ‘তোমরা পর পর হজ্জ ও উমরা আদায় করো। কেননা তা দারিদ্র্য ও পাপকে সরিয়ে দেয় যেমন সরিয়ে দেয় কামারের হাপর লোহা-স্বর্ণ-রুপার ময়লাকে। আর হজ্জে মাবরুরের ছোয়াব তো জান্নাত ভিন্ন অন্য কিছু নয়。\n\n" +
            "<b>[আলবানি : সহিহুন্নাসায়ি - ২/৫৫৮]</b>\n\n" +
            "উপরে উল্লেখিত হাদিসসমূহের বক্তব্য অত্যন্ত স্পষ্ট। তাই হজ্জ পালনেচ্ছু প্রতিটি ব্যক্তিরই উচিৎ পবিত্র হজ্জের এই ফজিলতসমূহ ভরপুরভাবে পাওয়ার জন্য মরিয়া হয়ে চেষ্টা করে যাওয়া। হজ্জ কবুল হওয়ার সকল শর্ত পূর্ণ করে সমস্ত পাপ ও গুনাহ থেকে মুক্ত থেকে কঠিনভাবে নিজেকে নিয়ন্ত্রণ করা।",
            "The reward of Hajj Mabrur (an accepted pilgrimage) is nothing other than Paradise.\n\n" +
            "<b>[Bukhari - 1650]</b>\n\n" +
            "Whoever performs Hajj and commits no obscenity or sin, and refrains from any sexual misconduct or indecent speech, returns (as pure) as on the day his mother gave birth to him.\n\n" +
            "<b>[Bukhari - 1424]</b>\n\n" +
            "There is no day on which Allah frees more people from the Fire than the Day of Arafah. Indeed, He draws near and boasts of the pilgrims before the angels, saying: 'What do these people desire?'\n\n" +
            "<b>[Muslim - 2/983]</b>\n\n" +
            "A person asked the Messenger of Allah (ﷺ) about the best of deeds.\n" +
            "<b>He replied:</b>\n" +
            "'Faith in Allah, the One and Only, and then Hajj Mabrur, which is superior to all other deeds just as the difference between the rising and setting of the sun.'\n\n\n" +
            "<b>[Ahmad - 4/342]</b>\n\n" +
            "In another hadith, the Messenger of Allah (ﷺ) was asked: 'Which deed is the best?' He replied, 'Faith in Allah and His Messenger.' It was asked, 'Then what?'\n" +
            "<b>He replied:</b>\n" +
            "'Jihad in the cause of Allah.' It was asked, 'Then what?' He replied: 'Hajj Mabrur.'\n\n\n" +
            "<b>[Bukhari - 1422]</b>\n\n" +
            "Once Aisha (RA) asked the Messenger of Allah (ﷺ), saying: 'O Messenger of Allah! Shall we not go out with you for Jihad and expeditions?'\n" +
            "<b>He replied:</b>\n" +
            "'The best and most beautiful Jihad for you is Hajj, an accepted Hajj (Hajj Mabrur).'\n\n\n" +
            "<b>[Fath al-Bari - 4/1861]</b>\n\n" +
            "'The pilgrims performing Hajj and Umrah are the guests of Allah. If they call upon Him, He answers them; and if they seek His forgiveness, He forgives them.'\n\n" +
            "<b>[Ibn Majah - 2883]</b>\n\n" +
            "It is narrated from Abu Hurairah that the Messenger of Allah (ﷺ) said:\n" +
            "'From one Umrah to the next is an expiation for whatever (sins) occur between them. And the reward of Hajj Mabrur is nothing other than Paradise.'\n\n\n" +
            "<b>[Bukhari - 1650]</b>\n\n" +
            "In another narration, the Messenger of Allah (ﷺ) said:\n" +
            "'Embracing Islam wipes out all previous sins; Hijrah wipes out all previous sins; and Hajj wipes out all previous sins.'\n\n\n" +
            "<b>[Muslim - 173]</b>\n\n" +
            "It is narrated from Ibn Mas'ud that the Prophet (ﷺ) said: 'Alternate between Hajj and Umrah, for they remove poverty and sins just as the furnace removes the impurities of iron, gold, and silver. And the reward of Hajj Mabrur is nothing other than Paradise.'\n\n" +
            "<b>[Al-Albani: Sahih an-Nasa'i - 2/558]</b>\n\n" +
            "The message of the aforementioned hadiths is abundantly clear. Therefore, every aspiring pilgrim must strive wholeheartedly to attain these immense blessings of Holy Hajj, fulfilling all conditions of an accepted Hajj, and exercising rigorous self-discipline to remain free from all sins and spiritual shortcomings."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getThreeTypesOfHajjArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // 1. তামাতু হজ
        cards.add(new HajjArticleCardItem(
            1,
            "তামাতু হজ",
            "Hajj al-Tamattu",
            "যিনি হজ করার ইচ্ছা করবেন তিনি প্রথমেই এ তিন প্রকারের কোন এক প্রকার নির্ধারণ করে নেবেন। কুরবানীর পশু, হাদী সাথে না থাকলে তামাতুত্ত্ব সবচেয়ে উত্তম। এ প্রকার হজ করার জন্যই রাসূল (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) সাহাবায়ে কেরামকে নির্দেশ দিয়েছেন।\n\n" +
            "<b>তামাতু হজ:</b>\n" +
            "হজের মাসগুলো তথা শাওয়াল, জিলকা'দা ও জিলহজ মাসের প্রথম দশ দিনের মধ্যে ওমরার নিয়তে ইহরাম বাঁধবে ও বলবে: 'লাব্বাইকা ওমরাতান মুতামাত্তিয়ান বিহা ইলাল হজ\"। এরপর তাওয়াফ, সায়ী ও মাথার চুল ছোট করে ইহরাম খুলবে। এবার ইহরাম অবস্থায় যা নিষিদ্ধ ছিল সবই তার জন্য বৈধ হল। এরপর জিলহজের আট তারিখে নিজ অবস্থান থেকে হজের ইহরাম পরে হজের স্থানগুলোর দিকে রওয়ানা হবে এবং হজের কাজ সমাপ্ত করবে। তাকে ছাগল অথবা উট বা গরুর সাত ভাগের এক ভাগ দিয়ে কুরবানী দিতে হবে। যদি পশু না পায় তাহলে হজের দিনগুলোতে তিনটি এবং নিজ এলাকায় এসে সাতটি (মোট ১০ টি) সিয়াম পালন করবে।\n\n" +
            "<b>তামাতু হজে করনীয়:</b>\n" +
            "• ওমরা\n" +
            "• হজ\n" +
            "• কুরবানী/ হাদী",
            "Whoever intends to perform Hajj should first decide upon one of these three forms of Hajj. If sacrificial animal (Hady) is not brought along, Tamattu is the most virtuous. The Messenger of Allah (peace and blessings be upon him) commanded the companions to perform this form of Hajj.\n\n" +
            "<b>Tamattu Hajj:</b>\n" +
            "During the months of Hajj (Shawwal, Dhul Qi'dah, and the first ten days of Dhul Hijjah), one assumes Ihram with the intention of Umrah, reciting: 'Labbayka 'Umratan Mutamatti'an biha ilal-Hajj'. Thereafter, one completes Tawaf, Sa'i, and trims or shaves the hair to exit Ihram. Everything that was prohibited during Ihram now becomes permissible. Then on the 8th of Dhul Hijjah, one assumes Ihram for Hajj from one's residence, proceeds towards the sacred sites of Hajj (Mina, Arafat, Muzdalifah), and completes the rites of Hajj. One must offer a sacrifice (a sheep/goat or a seventh share of a camel or cow). If unable to find or afford a sacrifice, one must fast for three days during Hajj and seven days upon returning home (a total of 10 days of fasting).\n\n" +
            "<b>Duties in Tamattu Hajj:</b>\n" +
            "• Umrah\n" +
            "• Hajj\n" +
            "• Sacrifice / Hady"
        ));

        // 2. ইফরাদ হজ
        cards.add(new HajjArticleCardItem(
            2,
            "ইফরাদ হজ",
            "Hajj al-Ifrad",
            "শুধুমাত্র হজের নিয়তে ইহরাম পরবে। মীকাতে পৌঁছে এভাবে বলবে \"লাবব্বাইকা হাজান\"। মক্কা মুকাররামায় পৌঁছে তাওয়াফে কুসুম করবে এবং হজের জন্য সাফা মারওয়া সায়ী করবে। হাজ্বর কাজগুলো শেষ করা পর্যন্ত একই ইহরামে থাকবে। ইফরাদ হজ্ কারীর জন্য। হরবাণী ওয়াজিব নয়। কারণ সে হজ্ ও ওমরা একত্রে করেনি।\n\n" +
            "<b>ইফরাদ হজে করনীয়:</b>\n" +
            "• শুধুমাত্র হজ\n" +
            "• কুরবানী লাকরেনি।",
            "One assumes Ihram with the intention of Hajj alone. Upon reaching the Miqat, one proclaims: \"Labbayka Hajjan\". After arriving in Makkah al-Mukarramah, one performs Tawaf al-Qudum and Sa'i between Safa and Marwah for Hajj. One remains continuously in the same state of Ihram until completing all the rites of Hajj. Animal sacrifice (Hady) is not obligatory for the pilgrim performing Ifrad, because they did not combine Hajj and Umrah together.\n\n" +
            "<b>Duties in Ifrad Hajj:</b>\n" +
            "• Hajj only\n" +
            "• Sacrifice is not obligatory."
        ));

        // 3. কিরান হজ
        cards.add(new HajjArticleCardItem(
            3,
            "কিরান হজ",
            "Hajj al-Qiran",
            "একই সাথে ওমরা ও হজের নিয়তে ইহরাম পরবে। মীকাতে পৌঁছে বলবে: \"লাব্বাইকা ওমরাতান ওয়া হাজ্জান\"। মক্কা মুকাররামায় পৌঁছে প্রথমে ওমরার তাওয়াফ ও সাফা-মারওয়া সায়ী করবে, কিন্তু ইহরাম খুলবে না; বরং জিলহজের দশ তারিখে কুরবানী ও মাথার চুল মুণ্ডন/ছোট করা পর্যন্ত একই ইহরামে বহাল থাকবে। কিরান হজকারীর জন্য কুরবানী (হাদী) ওয়াজিব।\n\n" +
            "<b>কিরান হজে করনীয়:</b>\n" +
            "• ওমরা\n" +
            "• হজ\n" +
            "• কুরবানী/ হাদী",
            "One assumes Ihram with the joint intention of both Umrah and Hajj together. Upon reaching the Miqat, one proclaims: \"Labbayka 'Umratan wa Hajjan\". Upon arriving in Makkah al-Mukarramah, one performs Tawaf and Sa'i for Umrah first, but does not exit Ihram; rather, one remains in the sacred state of Ihram until offering the sacrifice and shaving/trimming hair on the 10th of Dhul Hijjah. Offering an animal sacrifice (Hady) is obligatory for the pilgrim performing Qiran.\n\n" +
            "<b>Duties in Qiran Hajj:</b>\n" +
            "• Umrah\n" +
            "• Hajj\n" +
            "• Sacrifice / Hady"
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getMinaArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // 1. হাজী যখন মিনা পৌঁছবেন
        cards.add(new HajjArticleCardItem(
            1,
            "হাজী যখন মিনা পৌঁছবেন",
            "When the Pilgrim Arrives in Mina",
            "হাজী মিনা পৌঁছলে যথাসম্ভব দ্রুত জামরা আকাবা বা বড় জামরায় (যেটি জামারসমূহের মধ্যে মক্কার নিকটবর্তী) পৌঁছার চেষ্টা করবেন। পৌঁছেই তালবিয়া বন্ধ করে দিবেন। অতঃপর নিম্নের কাজগুলো করবেন-\n\n" +
            "১.জামরা আকাবায় পরপর সাতটি কঙ্কর নিক্ষেপ করবেন। প্রতিটি কঙ্কর নিক্ষেপের সময় তাকবীর (আল্লাহু আকবর) বলবেন।\n" +
            "২.কুরবানী ওয়াজিব হয়ে থাকলে কুরবানী করবেন। সম্ভব হাল নিজ হাতেই কুরবানী করবেন, নিজে যাবেন ও ফকীর মিসকিনকে খাওয়াবেন। সম্ভব না হলে কুরবানীর দায়িত্ব অন্য কাউকে দিবেন।\n" +
            "৩.মাথা মুণ্ডন করবেন অথবা চুল খাটো করবেন। তবে মুণ্ডন করাই উত্তম। আর মহিলাগণ সব চুল একসাথে করে চুলের অগ্রভাগ থেকে আঙ্গুলের মাথা পরিমাণ কাটবেন।\n\n" +
            "এ কাজগুলো ধারাবাহিকভাবে করাই উত্তম। ধারাবাহিকতা রক্ষা করতে না পারলেও কোন ক্ষতি নেই।",
            "Upon arriving in Mina, the pilgrim should endeavor to reach Jamrat al-Aqaba (the Big Jamrah, closest to Makkah among the Jamarat) as soon as possible. Upon arriving, cease reciting the Talbiyah immediately. Then perform the following actions:\n\n" +
            "1. Stone Jamrat al-Aqaba with seven pebbles consecutively, proclaiming Takbir (Allahu Akbar) with each throw.\n" +
            "2. Offer the animal sacrifice (Qurbani / Hady) if it is obligatory upon you. If possible, slaughter with your own hands, attend the sacrifice, and feed the poor and needy. If not feasible, designate someone trustworthy to perform it on your behalf.\n" +
            "3. Shave the head or trim the hair. Shaving (Halq) is superior and more meritorious. Women gather all their hair together and trim a fingertip's length from the ends.\n\n" +
            "It is superior to perform these rites sequentially in this order. However, there is no harm or penalty if the sequence is not maintained."
        ));

        // 2. মিনায় রাত যাপন
        cards.add(new HajjArticleCardItem(
            2,
            "মিনায় রাত যাপন",
            "Overnight Stay in Mina",
            "মিনায় অবস্থানরত দিনগুলোতে (১০ ও ১১ জিলহজ) মিনাতেই রাত যাপন করুন। আর ১২ জিলহজ রাত যাপন করুন, যদি ১৩ জিলহজ 'রমি' (কঙ্কর নিক্ষেপ) শেষ করে ফিরতে চান (সুন্নাত)।",
            "Spend the nights in Mina during the days of encampment (the 10th and 11th of Dhul Hijjah). Also spend the night of the 12th of Dhul Hijjah in Mina if you intend to complete the stoning (Rami) on the 13th of Dhul Hijjah before departing (which is Sunnah)."
        ));

        // 3. মিনা ত্যাগ
        cards.add(new HajjArticleCardItem(
            3,
            "মিনা ত্যাগ",
            "Departing Mina",
            "জিলহজ মিনায় না থাকতে চাইলে ১২ জিলহজ সন্ধ্যার আগে মিনা ত্যাগ করুন। সূর্যাস্তের আগে মিনা ত্যাগ করা উত্তম।",
            "If you do not wish to remain in Mina, depart before the evening of the 12th of Dhul Hijjah. It is superior and recommended to leave Mina before sunset."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getAyyamAtTashreeqArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: আইয়ামে তাশরিকের ফজিলত
        cards.add(new HajjArticleCardItem(
            1,
            "আইয়ামে তাশরিকের ফজিলত",
            "Virtues of Ayyam at-Tashreeq",
            "এ দিনগুলো ইবাদত-বন্দেগি, আল্লাহ রাব্বুল আলামিনের জিকির ও তার শুকরিয়া আদায়ের দিন। এরশাদ হয়েছে :\n\n" +
            "وَاذْكُرُوا اللَّهَ فِي أَيَّامٍ مَّعْدُودَاتٍ\n\n" +
            "তোমরা গুটি কয়েক দিনে আল্লাহকে স্মরণ করবে।\n" +
            "[সুরা বাকারা : ২০৩]\n\n" +
            "এ আয়াতের ব্যাখ্যায় ইমাম বুখারি (রহ:) বলেন:\n\n" +
            "عن ابن عباس رضی الله عنهما ...\n" +
            "الأيام المعدودات: أيام التشريق\n\n" +
            "ইবনে আব্বাস (রাঃ) থেকে বর্ণিত তিনি বলেন ‘গুটি কয়েকদিন’ বলতে আইয়ামুত-তাশরীককে বুঝানো হয়েছে।”\n" +
            "ইমাম কুরতুবি (রহ:) বলেন : ‘ইবনে আব্বাস (রাঃ) এর এ ব্যাখ্যা গ্রহণে কারো কোনো দ্বি-মত নেই।’অবশ্য হজ্জ মৌসুমে এ দিনগুলো মিনায় অবস্থানের দিন। কেননা হাদিসে এসেছে: ‘মিনায় অবস্থানের দিন হল তিনটি। যদি কেহ তাড়াতাড়ি করে দু দিনে চলে আসে তবে তার কোন পাপ নেই। আর যদি কেহ বিলম্ব করে তবে তারও কোন পাপ নেই।\n\n" +
            "সহীহ বুখারি ও আবু দাউদ\n\n" +
            "আইয়ামে তাশরীক বিষয়ে হাদিসে এসেছে, রসূলে কারিম (ﷺ)বলেছেন: আইয়ামে তাশরীক খাওয়া-দাওয়া ও আল্লাহ রাব্বুল আলামিনের জিকিরের দিন।\n" +
            "ইমাম ইবনে রজব (রহ:) এ হাদিসের ব্যাখ্যায় চমৎকার কথা বলেছেন। তিনি বলেন: আইয়ামে-তাশরীক এমন কতগুলো দিন যাতে ঈমানদারদের দেহের নেয়ামত ও স্বচ্ছন্দ এবং মনের নেয়ামত তথা স্বচ্ছন্দ একত্র করা হয়েছে। খাওয়া-দাওয়া হল দেহের খোরাক আর আল্লাহর জিকির ও শুকরিয়া হল হৃদয়ের খোরাক। আর এ ভাবেই নেয়ামতের পূর্ণতা লাভ করল এ দিন সমূহে।\n\n" +
            "• তাশরীকের দিনগুলো ঈদের দিন হিসেবে গণ্য।\n" +
            "• রাসুলুল্লাহ (ﷺ)বলেছেন: আরাফা দিবস, কোরবানির দিন ও মিনার দিনগুলো (কোরবানির পরবর্তী তিন দিন) আমাদের ইসলাম অনুসারীদের ঈদের দিন।\n" +
            "• এ দিনসমূহ জিলহজ্জ মাসের প্রথম দশকের সাথে লাগানো। যে দশক খুবই ফজিলত-পূর্ণ। তাই এ কারণেও এর যথেষ্ট মর্যাদা রয়েছে।\n" +
            "• এ দিনসমূহে হজ্জের কতিপয় আমল সম্পাদন করা হয়ে থাকে। এ কারণেও এ দিনগুলো ফজিলতের অধিকারী।",
            "These days are meant for worship, remembering Allah the Lord of the worlds, and expressing gratitude to Him. It is revealed:\n\n" +
            "وَاذْكُرُوا اللَّهَ فِي أَيَّامٍ مَّعْدُودَاتٍ\n\n" +
            "\"And remember Allah during specific numbered days.\"\n" +
            "[Surah Al-Baqarah: 203]\n\n" +
            "In explanation of this verse, Imam Bukhari (RA) relates:\n\n" +
            "عن ابن عباس رضی الله عنهما ...\n" +
            "الأيام المعدودات: أيام التشريق\n\n" +
            "Narrated from Ibn Abbas (RA) that he said: \"The 'numbered days' refer to Ayyam at-Tashreeq.\"\n" +
            "Imam al-Qurtubi (RA) states: \"There is no difference of opinion among scholars regarding the acceptance of this explanation by Ibn Abbas (RA).\" During Hajj, these are days of encampment in Mina, as mentioned in the hadith: \"The days of stay in Mina are three. If someone hurries and leaves in two days, there is no sin upon him; and if someone delays, there is no sin upon him.\"\n\n" +
            "Sahih al-Bukhari & Sunan Abi Dawud\n\n" +
            "Regarding Ayyam at-Tashreeq, the Messenger of Allah (ﷺ) said: \"The days of Tashreeq are days of eating, drinking, and remembering Allah the Lord of the worlds.\"\n" +
            "Imam Ibn Rajab (RA) explained this hadith profoundly, saying: \"Ayyam at-Tashreeq are days wherein physical comfort and spiritual nourishment are combined for believers. Eating and drinking nourish the body, while the remembrance and gratitude of Allah nourish the soul; thus divine favors attain perfection on these days.\"\n\n" +
            "• The days of Tashreeq are regarded as days of Eid and celebration.\n" +
            "• The Messenger of Allah (ﷺ) said: \"The Day of Arafah, the Day of Sacrifice (Nahr), and the Days of Mina (Tashreeq) are our days of Eid for followers of Islam.\"\n" +
            "• These days immediately succeed the first ten sacred days of Dhul Hijjah, thereby sharing exceptional virtue and honor.\n" +
            "• Essential rites of Hajj are performed during these days, conferring great spiritual reward."
        ));

        // Card 2: এ দিনগুলোতে করণীয়
        cards.add(new HajjArticleCardItem(
            2,
            "এ দিনগুলোতে করণীয়",
            "Prescribed Deeds During These Days",
            "এ দিনসমূহ যেমনি ইবাদত-বন্দেগি, জিকির-আযকারের দিন তেমনি আনন্দ করার দিন। যেমন রাসুলুল্লাহ (ﷺ)বলেছেন: “আইয়ামে-তাশরীক হল খাওয়া-দাওয়া ও আল্লাহর জিকিরের দিন।” এ দিনসমূহে আল্লাহ রাব্বুল আলামিনের দেয়া নেয়ামত নিয়ে আমোদ করার মাধ্যমে তার শুকরিয়া ও জিকির আদায় করা উচিৎ। আর জিকির আদায়ের কয়েকটি পদ্ধতি হাদিসে এসেছে।\n\n" +
            "• সালাতের পর তাকবির পাঠ করা। এবং সালাত ছাড়াও সর্বদা তাকবির পাঠ করা। আর এ তাকবির আদায়ের মাধ্যমে আমরা প্রমাণ দিতে পারি যে এ দিনগুলো আল্লাহর জিকিরের দিন। আর এ জিকিরের নির্দেশ যেমন হাজিদের জন্য, তেমনই যারা হজ্জ পালনরত নন তাদেরও জন্য।\n" +
            "• কোরবানি ও হজ্জের পশু জবেহ করার সময় আল্লাহ তাআলার নাম ও তাকবির উচ্চারণ করা।\n" +
            "• খাওয়া-দাওয়ার শুরু ও শেষে আল্লাহ তা’আলার জিকির করা। আর এটা তো সর্বদা করার নির্দেশ রয়েছে তথাপি এ দিনগুলোতে এর গুরুত্ব বেশি দেয়া। এমনি ভাবে সকল কাজ ও সকাল-সন্ধ্যার জিকিরগুলোর প্রতি যত্নবান হওয়া।\n" +
            "• হজ্জ পালন অবস্থায় কঙ্কর নিক্ষেপের সময় আল্লাহ তাআলার তাকবির পাঠ করা।\n" +
            "• এগুলো ছাড়াও যে কোন সময় ও যে কোন অবস্থায় আল্লাহর জিকির করা।",
            "These days are days of worship and remembrance, as well as days of lawful joy and gratitude. The Messenger of Allah (ﷺ) said: \"Ayyam at-Tashreeq are days of eating, drinking, and remembering Allah.\" During these days, one should celebrate the bounties bestowed by Allah by giving thanks and engaging in His remembrance. The Sunnah prescribes several ways to engage in Dhikr:\n\n" +
            "• Reciting the Takbir (Takbeer-e-Tashreeq) following obligatory prayers, as well as reciting it continuously throughout the days. This practice manifests that these are days of remembering Allah. This directive applies equally to pilgrims and non-pilgrims worldwide.\n" +
            "• Pronouncing the Name of Allah and Takbir when slaughtering sacrificial animals.\n" +
            "• Remembering Allah before and after eating and drinking. While this is always commanded, it holds enhanced significance on these days. One should equally observe morning and evening supplications.\n" +
            "• Proclaiming Takbir (Allahu Akbar) with each pebble cast while stoning the Jamarat pillars during Hajj.\n" +
            "• In addition to these, remembering Allah abundantly at all times and in all situations."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getFarewellHajjArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: বিদায় হজ কি / What is the Farewell Pilgrimage?
        cards.add(new HajjArticleCardItem(
            1,
            "বিদায় হজ কি",
            "What is the Farewell Pilgrimage?",
            "রাসুলুল্লাহ (ﷺ) পরলোকগমনের পূর্বে যে হজ্জ্বে আকবর পালন করেছিলেন তাই বিদায় হজ্জ বা হজ্জুল বিদা। এই হজ্জে রাসুলুল্লাহ (ﷺ)-এর যে বক্তব্য রেখেছিলেন তা খুতবাতু হজ্জ্বুল বিদা বা বিদায় হজ্জের ভাষণ বলে সুপরিচিত এবং এটি ইসলামের ইতিহাসে খুবই গুরুত্বপূর্ণ।\n\nদশম হিজরি সনে অর্থাৎ ৬৩২ খ্রিষ্টাব্দে এই হজ্জ অনুষ্ঠিত হয়।\n\n<b>[সূরা আত-তাওবাহ: ৯/২৮]</b>\n\nফলে মুশরিকমুক্ত পরিবেশে তিনি এই হজ্জ করেন এবং এই হজ্জ অনুষ্ঠানের আমীর ও প্রশিক্ষক ছিলেন তিনি নিজে। এই হজ্জে আরাফাতের ময়দানে ও মিনায় তিনদিনে বিভিন্ন সময় ৩১টি বিষয়ে তিনি উম্মতকে সতর্ক করেন।\n\n<b>[সীরাতুর রাসূল (ছাঃ) ৩য় মুদ্রণ ৭০১ পৃ.]</b>",
            "The major pilgrimage (Hajj al-Akbar) performed by the Messenger of Allah (ﷺ) prior to his passing is known as the Farewell Pilgrimage (Hajjat al-Wada' or Hajjul Bida). The profound address delivered by the Prophet (ﷺ) during this pilgrimage is universally known as Khutbat Hajjat al-Wada' (The Farewell Sermon) and holds immense historic and spiritual significance in Islam.\n\nThis momentous pilgrimage took place in the 10th year of Hijrah, corresponding to 632 CE.\n\n<b>[Surah At-Tawbah: 9/28]</b>\n\nConsequently, he performed this pilgrimage in an environment purified of idolatry, personally serving as the Amir (leader) and guide of all rituals. Over three distinct days at Arafat and Mina, he enlightened and cautioned the Ummah regarding 31 vital spiritual, ethical, and legal matters.\n\n<b>[Siratur Rasul (SAW), 3rd Edition, p. 701]</b>"
        ));

        // Card 2: বিদায় হজের ভাষণ ও বিস্তারিত / Farewell Sermon & Comprehensive Account
        cards.add(new HajjArticleCardItem(
            2,
            "বিদায় হজের ভাষণ ও বিস্তারিত",
            "Farewell Sermon & Comprehensive Account",
            "<b>মু’আয বিন জাবাল (রাঃ)-কে বিদায়কালীন নসীহত ও বিচ্ছেদের পূর্বাভাস:</b>\n\nদাওয়াত ও তাবলীগের কাজ সম্পূর্ণ হল এবং আল্লাহর সার্বভৌমত্বের স্বীকৃতি, আল্লাহ ছাড়া অন্য কারো সার্বভৌমত্বে অস্বীকৃতি এবং মুহাম্মাদ (ﷺ)-এর পয়গম্বরের ভিত্তির উপর এক নতুন সমাজ কাঠামো প্রতিষ্ঠিত হল। অতঃপর আল্লাহ সুবহানাহূ ওয়া তা‘আলার পক্ষ থেকে রাসূলুল্লাহ (ﷺ)-এর নিকট আভাষ দেয়া হচ্ছিল যে, পৃথিবীতে তাঁর অবস্থানের সময় কাল ফুরিয়ে এসেছে। এ প্রেক্ষিতে রাসূলুল্লাহ (ﷺ) মু’আয বিন জাবাল (রাঃ)-কে ইয়ামানের গভর্ণর নিযুক্ত করে প্রেরণ করেন দশম হিজরী সনে। তখন তাঁর বিদায় কালে অন্যান্য উপদেশাবলীর সঙ্গে এ কথাও বললেন-\n\n(يَا مُعَاذُ، إِنَّكَ عَسٰي أَلَّا تَلْقَانِيْ بَعْدَ عَامِيْ هٰذَا، وَلَعَلَّكَ أَنْ تَمُرَّ بِمَسْجِدِيْ هٰذَا وَقَبْرِيْ)\n\n‘হে মোয়ায! এ বছরের পর তোমার সঙ্গে আমার হয়ত আর সাক্ষাত নাও হতে পারে, তখন হয়ত বা আমার এ মসজিদ এবং আমার কবরের পাশ দিয়ে তোমরা যাতায়াত করবে।’\n\nরাসূলুল্লাহ (ﷺ)-এর মুখ থেকে মোয়ায (রাঃ) এ কথা শুনে আসন্ন বিচ্ছেদের চিন্তায় অস্থির হয়ে কাঁদতে লাগলেন।\n\n<b>ঐতিহাসিক হজের ঘোষণা ও সাহাবিদের মহামিলন:</b>\n\nপ্রকৃতপক্ষে আল্লাহ এটাই চেয়েছিলেন যে, তিনি তাঁর নাবী কারীম (ﷺ)-কে ইসলামী দাওয়াতের কার্যকারিতা এবং সুফল বাস্তবক্ষেত্রে চাক্ষুস প্রত্যক্ষ করিয়ে দেবেন। তাঁর অক্লান্ত পরিশ্রম এবং অবিশ্রান্ত প্রচেষ্টার ফলে আরবের সমগ্র উপজাতি এক আল্লাহর আনুগত্যের ছায়াতলে সমবেত হয়েছিল। রাসূলুল্লাহ (ﷺ) যেন তাঁদের সঙ্গে একত্র হয়ে তাঁদের সঙ্গে আল্লাহর মাহাত্ম্য বর্ণনা ও শুকরিয়া জ্ঞাপন করতে পারেন, তাঁদেরকে হজ্জের আহকাম ও অন্যান্য বিধি-বিধান শিক্ষা দিতে পারেন এবং তাঁদের কাছ থেকে এ কথার স্বীকৃতি নিতে পারেন যে, ‘তিনি আল্লাহর দেয়া দায়িত্ব যথাযথভাবে পালন করেছেন এবং আল্লাহর আমানত তাঁর বান্দাহদের নিকট যথার্থভাবে পৌঁছে দিয়েছেন।’\n\nআল্লাহ তা‘আলার ইচ্ছায় রাসূলুল্লাহ (ﷺ) ঘোষণা করলেন যে, তিনি এ বছর হজ্জে রওয়ানা হবেন। এ শুভ সংবাদ শ্রবণ করে দলে দলে চতুর্দিক হতে সাহাবীগণ সমবেত হতে শুরু করলেন।\n\n<b>মদিনা হতে শুভ যাত্রা ও যুল-হুলাইফায় ইহরাম গ্রহণ:</b>\n\nঅতঃপর যুল ক্বা‘দাহ মাসের ৪ দিন অবশিষ্ট থাকতে (২৫শে কি ২৬শে) যুল ক্বা‘দাহ শনিবার যুহরের পূর্বে তিনি রওয়ানা হওয়ার প্রস্তুতি নিলেন।\n\nতিনি চুলে চিরুনী ব্যবহার করলেন, তেল লাগালেন, লুঙ্গি ও চাদর পরিধান করলেন এবং কুরবানীর পশুর গলায় হার পরালেন। অতঃপর যুহরের সালাত কসর করে দুই রাক‘আত আদায় করলেন। এরপর সাহাবীগণকে নিয়ে রওয়ানা হলেন। আসরের পূর্বে যুল হুলাইফাহ পৌঁছলেন এবং সেখানে আসরের সালাত দুই রাক‘আত আদায় করলেন। অতঃপর সেখানেই তিনি রাত্রি যাপন করলেন। ভোর হলে তিনি সাহাবীদের বললেন,\n\nأَتَانِيْ اللَّيْلَةَ آتٍ مِنْ رَبِّيْ فَقَالَ: صَلِّ فِيْ هٰذَا الْوَادِي الْمُبَارَكِ وَقُلْ: عُمْرَةٌ فِيْ حَجَّةٍ\n\nআজ রাতে আমার প্রভূর পক্ষ হতে একজন আগন্তুক এসে বলেছেন, ‘এ পবিত্র উপত্যকায় সালাত আদায় কর এবং হজ্জের সঙ্গে ওমরা সংশ্লিষ্ট রয়েছে।’\n\n<b>[উমার (রাঃ) হতে বুখারী শরীফে এটা বর্ণিত হয়েছে, ১ম খন্ড ২০৭ পৃঃ]</b>\n\nঅতঃপর যুহরের সালাতের পূর্বে নাবী কারীম (ﷺ) ইহরামের উদ্দেশ্যে গোসল করলেন। উম্মুল মুমিনীন আয়েশা (রাঃ) স্বহস্তে তাঁর শরীর ও মাথায় যারীরা নামক এক প্রকার সুগন্ধি দ্রব্য এবং মিশক মিশ্রিত এমন এক সুগন্ধি দ্রব্যাদি মাখিয়ে দিলেন যার চমক তাঁর মাথায় ও দাড়িতে স্পষ্ট লক্ষ্য করা যাচ্ছিল। এরপর তিনি লুঙ্গি ও চাদর পরে যুহরের সালাত দুই রাক‘আত আদায় করলেন। অতঃপর সালাতের স্থানে বসেই হজ্জ ও উমরার নিয়্যাতে ইহরাম বাঁধলেন। এরপর তিনি কাসওয়া নাম্নী উষ্ট্রীর পিঠে আরোহণ করলেন।\n\nঅতঃপর তিনি তাকবীর, তাহলীল ও তালবিয়াহ পাঠ করতে থাকলেন-\n\nلَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لَا شَرِيْكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لَا شَرِيْكَ لَكَ\n\n‘আমি উপস্থিত হে আল্লাহ! আমি উপস্থিত! তোমার কোন শরীক নেই, আমি উপস্থিত! নিশ্চয়ই সমস্ত প্রশংসা, নিয়ামত এবং রাজত্ব সবই তোমার! তোমার কোন শরীক নেই।’\n\n<b>মক্কায় আগমন, বাইতুল্লাহ তাওয়াফ ও সাফা-মারওয়া সাঈ:</b>\n\nঅতঃপর পথ অতিক্রম করতে করতে যিলহাজ্জ মাসের ৪ তারিখ দিবাগত রাতে মক্কার নিকটবর্তী ‘যী তুয়া’ নামক স্থানে পৌঁছলেন। সেখানে তিনি ফজর পর্যন্ত রাত যাপন করলেন এবং ফজরের সালাত আদায় করে গোসল করলেন।\n\nঅতঃপর সেখান থেকে মক্কায় প্রবেশ করলেন। এ দিনটি ছিল রবিবার দিবাগত রাত। তিনি মসজিদে হারামে প্রবেশ করে বাইতুল্লাহর তাওয়াফ করলেন এবং সাফা ও মারওয়ার মাঝে সাঈ করলেন। কিন্তু তিনি ইহরাম খুললেন না। কারণ তাঁর সাথে কুরবানীর পশু ছিল। অতএব, তিনি হজ্জ ও উমরাহর উভয়ের জন্য একই ইহরামে বহাল থাকলেন।\n\nতিনি যে সকল সাহাবীর সাথে কুরবানীর পশু ছিল না, তাঁদেরকে নির্দেশ দিলেন যে, তাঁরা যেন তাঁদের হজ্জকে উমরায় পরিণত করে নেন এবং বাইতুল্লাহর তাওয়াফ ও সাফা-মারওয়ার সাঈ শেষ করে ইহরাম খুলে ফেলেন। কিন্তু সাহাবীগণ এ নির্দেশ পালনে কিছুটা ইতস্তত করতে লাগলেন। এ অবস্থা দেখে রাসূলুল্লাহ (ﷺ) বললেন,\n\nلَوْ اسْتَقْبَلْتُ مِنْ أَمْرِيْ مَا اسْتَدْبَرْتُ مَا أَهْدَيْتُ، وَلَوْلَا أَنَّ مَعِيَ الْهَدْيَ لَأَحْلَلْتُ\n\n‘আমি যে বিষয়টি পরে জেনেছি তা যদি আগে জানতাম, তাহলে আমি কুরবানীর পশু সাথে আনতাম না এবং আমার সাথে যদি কুরবানীর পশু না থাকত, তবে আমিও ইহরাম খুলে ফেলতাম।’\n\nরাসূলুল্লাহ (ﷺ)-এর এ কথা শোনার পর সাহাবীগণ দ্বিধাহীন চিত্তে তাঁর নির্দেশ পালন করলেন এবং যাদের সাথে কুরবানীর পশু ছিল না, তাঁরা ইহরাম খুলে ফেললেন।\n\n<b>৮ই যুলহিজ্জাহ (তারবিয়াহ) ও ৯ই যুলহিজ্জাহ আরাফাতে অবস্থান:</b>\n\nঅতঃপর ৮ই যিলহাজ্জ (তারবিয়ার দিন) রাসূলুল্লাহ (ﷺ) সাহাবীগণকে নিয়ে মীনায় রওয়ানা হলেন এবং সেখানে যুহর, আসর, মাগরিব, এশা ও ফজরের সালাত আদায় করলেন। এ সালাতগুলো ছিল মোট পাঁচ ওয়াক্তের। অতঃপর সূর্যোদয় পর্যন্ত কিছুক্ষণ অপেক্ষা করলেন।\n\nঅতঃপর সূর্যোদয়ের পর তিনি আরাফাতের উদ্দেশ্যে রওয়ানা হলেন। সেখানে গিয়ে দেখলেন যে, তাঁর নির্দেশে নামিরা নামক স্থানে তাঁর জন্য একটি তাঁবু স্থাপন করা হয়েছে। তিনি সেখানে অবতরণ করলেন।\n\nসূর্য যখন পশ্চিমাকাশে ঢলে পড়ল, তখন তিনি কাসওয়া নাম্নী উষ্ট্রীর পিঠে সওয়ার হওয়ার নির্দেশ দিলেন। উষ্ট্রী প্রস্তুত করা হলে তিনি তার পিঠে আরোহণ করে বাতনে ওয়াদীতে (নামিরা উপত্যকার মধ্যস্থলে) আগমন করলেন।\n\n<b>আরাফাতের ময়দানে ঐতিহাসিক বিদায় হজের ভাষণ:</b>\n\nসেখানে তাঁর চারপাশে এক লাখ চব্বিশ হাজার অথবা এক লাখ চুয়াল্লিশ হাজার সাহাবীর এক বিরাট সমাবেশ ঘটেছিল। তিনি উপস্থিত জনতার উদ্দেশ্যে এক ঐতিহাসিক ভাষণ প্রদান করলেন। তিনি বললেন-\n\nأَيُّهَا النَّاسُ، اسْمَعُوْا قَوْلِيْ، فَإِنِّيْ لَا أَدْرِيْ لَعَلِّيْ لَا أَلْقَاكُمْ بَعْدَ عَامِيْ هٰذَا بِهٰذَا الْمَوْقِفِ أَبَدًا\n\n‘হে লোক সকল! তোমরা আমার বক্তব্য মনোযোগ দিয়ে শোন। কারণ আমি জানি না, এ বছরের পর এ স্থানে তোমাদের সাথে আমার আর কখনো সাক্ষাত ঘটবে কি না।’\n\n<b>[সীরাতে ইবনে হিশাম ২য় খন্ড ৬০৩ পৃঃ]</b>\n\n<b>জান-মাল ও ইজ্জতের অলঙ্ঘনীয় পবিত্রতা:</b>\n\nإِنَّ دِمَاءَكُمْ وَأَمْوَالَكُمْ حَرَامٌ عَلَيْكُمْ كَحُرْمَةِ يَوْمِكُمْ هٰذَا، فِيْ شَهْرِكُمْ هٰذَا، فِيْ بَلَدِكُمْ هٰذَا\n\n‘তোমাদের রক্ত এবং তোমাদের ধন-সম্পদ তোমাদের আজকের এ দিন, তোমাদের এ মাস এবং তোমাদের এ শহরের মতই পরস্পরের জন্য চিরতরে হারাম ও সম্মানিত করা হল।’\n\n<b>জাহেলি কুসংস্কার ও রক্তের দাবি সম্পূর্ণ বাতিল:</b>\n\nأَلَا كُلُّ شَيْءٍ مِنْ أَمْرِ الْجَاهِلِيَّةِ تَحْتَ قَدَمَيَّ مَوْضُوْعٌ، وَدِمَاءُ الْجَاهِلِيَّةِ مَوْضُوْعَةٌ، وَإِنَّ أَوَّلَ دَمٍ أَضَعُ مِنْ دِمَائِنَا دَمُ ابْنِ رَبِيْعَةَ بْنِ الْحَارِثِ\n\n‘সাবধান! জাহেলিয়াত যুগের সমস্ত বিষয় আমার দুই পায়ের নিচে দলিত ও পদদলিত হল। জাহেলিয়াত যুগের সমস্ত রক্তের দাবি বাতিল করা হল। আমাদের পক্ষ থেকে সর্বপ্রথম যে রক্তের দাবি আমি বাতিল ঘোষণা করছি, তা হচ্ছে ইবনে রাবী‘আহ বিন হারিসের রক্ত।’\n\nতিনি বনু সা‘দ গোত্রে স্তন্যপান করেছিলেন এবং হুযাইল গোত্রের লোকেরা তাঁকে হত্যা করেছিল।\n\n<b>জাহেলি সুদের সম্পূর্ণ বিলুপ্তি ঘোষণা:</b>\n\nوَرِبَا الْجَاهِلِيَّةِ مَوْضُوْعٌ، وَأَوَّلُ رِبًا أَضَعُ رِبَانَا، رِبَا عَبَّاسِ بْنِ عَبْدِ الْمُطَّلِبِ، فَإِنَّهُ مَوْضُوْعٌ كُلُّهُ\n\n‘জাহেলিয়াত যুগের সকল সুদ বাতিল ঘোষিত হল এবং আমাদের পক্ষ থেকে সর্বপ্রথম যে সুদ আমি বাতিল করছি, তা হল আব্বাস বিন আব্দুল মুত্তালিবের সুদ। তার সমস্ত সুদ সম্পূর্ণরূপে বাতিল ঘোষণা করা হল।’\n\n<b>নারীদের অধিকার ও তাদের প্রতি সদাচরণের নির্দেশ:</b>\n\nفَاتَّقُوا اللهَ فِي النِّسَاءِ، فَإِنَّكُمْ أَخَذْتُمُوْهُنَّ بِأَمَانِ اللهِ، وَاسْتَحْلَلْتُمْ فُرُوْجَهُنَّ بِكَلِمَةِ اللهِ، وَلَكُمْ عَلَيْهِنَّ أَلَّا يُوْطِئْنَ فُرُشَكُمْ أَحَدًا تَكْرَهُوْنَهُ، فَإِنْ فَعَلْنَ ذٰلِكَ فَاضْرِبُوْهُنَّ ضَرْبًا غَيْرَ مُبَرِّحٍ، وَلَهُنَّ عَلَيْكُمْ رِزْقُهُنَّ وَكِسْوَتُهُنَّ بِالْمَعْرُوْفِ\n\n‘তোমরা নারীদের ব্যাপারে আল্লাহকে ভয় কর। কারণ তোমরা তাদেরকে আল্লাহর আমানতের মাধ্যমে গ্রহণ করেছ এবং আল্লাহর বাণীর মাধ্যমে তাঁদের সতীত্বকে নিজেদের জন্য হালাল করেছ। তাঁদের উপর তোমাদের এ অধিকার রয়েছে যে, তাঁরা তোমাদের অপছন্দনীয় কাউকে তোমাদের বিছানায় পদার্পণ করতে দেবে না। যদি তাঁরা তা করে, তবে তোমরা তাদেরকে এমন প্রহার করতে পার যা মারাত্মক ক্ষত সৃষ্টি করে না। আর তোমাদের উপর তাঁদের ন্যায়সঙ্গত ভরণপোষণ ও পোশাক-পরিচ্ছদের দায়িত্ব রয়েছে।’\n\n<b>পথভ্রষ্টতা থেকে মুক্তির দিশারী: আল্লাহর কিতাব:</b>\n\nوَقَدْ تَرَكْتُ فِيْكُمْ مَا لَنْ تَضِلُّوْا بَعْدَهُ إِنْ اعْتَصَمْتُمْ بِهِ، كِتَابُ اللهِ\n\n‘আমি তোমাদের মাঝে এমন এক জিনিস রেখে যাচ্ছি যা শক্ত করে ধরে রাখলে তোমরা কখনোও পথহারা হবে না এবং তা হচ্ছে আল্লাহর কিতাব।’\n\n<b>[সহীহুল মুসলিম নাবীর হজ্জের অধ্যায় ১ম খন্ড ৩৯৭ পৃঃ]</b>\n\n<b>নবুওয়তের সমাপ্তি ও উম্মতের প্রতি পঞ্চস্তম্ভের তাগিদ:</b>\n\nأَيُّهَا النَّاسُ، إِنَّهُ لَا نَبِيَّ بَعْدِيْ، وَلَا أَمَّةَ بَعْدَكُمْ، أَلَا فَاعْبُدُوْا رَبَّكُمْ، وَصَلُّوْا خَمْسَكُمْ، وَصُوْمُوْا شَهْرَكُمْ، وَأَدُّوْا زَكَاةَ أَمْوَالِكُمْ، طِيْبَةً بِهَا أَنْفُسُكُمْ، وَتَحُجُّوْنَ بَيْتِ رَبِّكُمْ، وَأَطِيْعُوْا أُوْلَاتِ أَمْرَكُمْ، تَدْخُلُوْا جَنَّةَ رَبَّكُمْ\n\n‘হে লোকজনেরা! স্মরণ রেখ আমার পরে আর নাবী আসবে না। কাজেই তোমাদের পরে অন্য কোন উম্মতের প্রশ্নও থাকবে না। অতএব, আল্লাহ তা‘আলার ইবাদত কর, পাঁচ ওয়াক্ত সালাত আদায় করো, রমাযান মাসে রোযা রেখো, সন্তুষ্ট চিত্তে নিজ সম্পদের যাকাত প্রদান করো, নিজ প্রভূর ঘরের হজ্জ পালন করো এবং সৎ নেতৃত্বের অনুসরণ করো। নিষ্ঠার সঙ্গে এ সব কাজ করলে ওয়াদা মোতাবেক জান্নাতে প্রবেশ করতে পারবে।’\n\n<b>[ইবনু মাজা, ইবনু আসাকের, রহমাতুল্লিল আলামীন ১ম খন্ড ২২৩ পৃঃ]</b>\n\n<b>উম্মতের ঐতিহাসিক সাক্ষ্য ও ‘হে আল্লাহ! তুমি সাক্ষী থাক’:</b>\n\nوَأَنْتُمْ تُسْأَلُوْنَ عَنِّيْ، فَمَا أَنْتُمْ قَائِلُوْنَ؟\n\n‘আমার সম্পর্কে যদি তোমাদের জিজ্ঞেস করা হয় তখন তোমরা কী উত্তর দিবে?’\n\nউপস্থিত সাহাবীগণ সমস্বরে বলে উঠলেন,\n\nنَشْهَدُ أَنَّكَ قَدْ بَلَّغْتَ وَأَدَّيْتَ وَنَصَحْتَ\n\n‘আমরা সাক্ষ্য দিচ্ছি যে, আপনি আল্লাহর বাণী যথার্থভাবে পৌঁছে দিয়েছেন, আমানত আদায় করেছেন এবং উম্মতের সর্বোত্তম কল্যাণ কামনা করেছেন।’\n\nঅতঃপর রাসূলুল্লাহ (ﷺ) তাঁর শাহাদাত আঙুল আকাশের দিকে উত্তোলন করলেন এবং সমবেত জনতার দিকে ঝুঁকিয়ে তিনবার বললেন-\n\nاللَّهُمَّ اشْهَدْ، اللَّهُمَّ اشْهَدْ، اللَّهُمَّ اشْهَدْ\n\n‘হে আল্লাহ! তুমি সাক্ষী থাক! হে আল্লাহ! তুমি সাক্ষী থাক! হে আল্লাহ! তুমি সাক্ষী থাক!’\n\n<b>[বুখারী ইবনু উমার হতে দ্র: রহমাতুল্লিল আলামীন ১ম খন্ড ২৬৫ পৃঃ]</b>\n\n<b>ভাষণের বলিষ্ঠ প্রতিধ্বনি:</b>\n\nরাবী‘আহ বিন উমাইয়াহ বিন খালাফ নামক সাহাবীর কণ্ঠস্বর ছিল অত্যন্ত বলিষ্ঠ ও জোরালো। রাসূলুল্লাহ (ﷺ) যে কথা বলতেন, তিনি উচ্চকণ্ঠে তা পুনরাবৃত্তি করে জনতার কাছে পৌঁছে দিচ্ছিলেন।\n\n<b>দ্বীনের পূর্ণতার ঐশী ঘোষণা (সূরা মায়িদা: ৩):</b>\n\nনাবী কারীম (ﷺ)-এর ভাষণের পর আল্লাহ সুবহানাহূ ওয়া তা‘আলা এ আয়াত নাযিল করলেন-\n\nالْيَوْمَ أَكْمَلْتُ لَكُمْ دِيْنَكُمْ وَأَتْمَمْتُ عَلَيْكُمْ نِعْمَتِيْ وَرَضِيْتُ لَكُمُ الْإِسْلَامَ دِيْنًا\n\n‘আজ আমি তোমাদের জন্য তোমাদের দ্বীনকে পূর্ণাঙ্গ করে দিলাম, তোমাদের উপর আমার নিয়ামত সম্পূর্ণ করলাম এবং ইসলামকে তোমাদের দ্বীন হিসেবে মনোনীত করলাম।’\n\n<b>[আল-মায়িদাহ (৫) : ৩]</b>\n\nএ আয়াত শুনে উমার (রাঃ) কেঁদে ফেললেন। তাঁকে জিজ্ঞেস করা হল, ‘আপনি কাঁদছেন কেন?’ তিনি বললেন, ‘পূর্ণতার পরেই তো অপূর্ণতা ও বিচ্ছেদের সূচনা হয়।’\n\n<b>মুযদালিফায় গমনাগমন, রাত্রিযাপন ও মাশ‘আরুল হারামে দু‘আ:</b>\n\nনাবী কারীম (ﷺ)-এর ভাষণের পর বিলাল (রাঃ) প্রথমে আযান এবং পরে ইকামত বললেন। রাসূলুল্লাহ (ﷺ) যুহরের সালাতে ইমামত করলেন। এরপর বিলাল (রাঃ) আবারও ইকামত করলেন। এ দু’ সালাতের মধ্যে আর কোন সালাত পড়লেন না। এরপর সওয়ারীতে আরোহণ করে রাসূলুল্লাহ (ﷺ) অবস্থান স্থলে গমন করলেন। নিজ উট ক্বাসওয়ার পেট পাথর সমূহের দিকে করলেন এবং হাবলে মুশাতকে (পদদলে যাতায়াতকারীগণের পথের মাঝে অবস্থিত স্তুপ) সামনে করলেন এবং ক্বিবলাহমুখী হয়ে নাবী কারীম (ﷺ) (একই অবস্থায়) অবস্থান করলেন। সূর্য অস্তমিত হওয়া পর্যন্ত এভাবে অবস্থান করলেন। সূর্যের অল্প অল্প হলুদ বর্ণ শেষ হল, আবার সূর্য মন্ডল অদৃশ্য হয়ে গেল।\n\nএর পর রাসূলুল্লাহ (ﷺ) উসামা (রাঃ)-কে পিছনে বসিয়ে নিয়ে যাত্রা করলেন এবং মুযদালিফায় গিয়ে উপস্থিত হলেন। মুযদালিফায় মাগরিব এবং এশার সালাত এক বৈঠকে দু’ ইকামতের সঙ্গে আদায় করলেন। মধ্যে কোন নফল সালাত আদায় করেননি। এরপর নাবী কারীম (ﷺ) ঘুমিয়ে পড়লেন এবং সকাল পর্যন্ত ঘুমে কাটালেন। তবে সকাল হওয়া মাত্র আযান এবং ইকামত দিয়ে ফজরের সালাত আদায় করলেন। অতঃপর ক্বাসওয়ার উপর সওয়ার হয়ে মাশয়ারে হারামে আগমন করলেন এবং কিবলামুখী হয়ে আল্লাহর সমীপে দু‘আ করলেন এবং তাকবীর, তাহলীল ও তাওহীদের বাণীসমূহ উচ্চারণ করলেন।\n\nঅন্ধকার দূরীভূত হয়ে ফর্সা না হওয়া পর্যন্ত তিনি সেখানে অবস্থান করলেন। অতঃপর সূর্যোদয়ের পূর্বেই মীনা অভিমুখে রওয়ানা হয়ে গেলেন। এ সময় তাঁর পিছনে বসিয়েছিলেন ফাযল বিন আব্বাস (রাঃ)-কে। বাতনে মোহাসসারে গিয়ে যখন পৌঁছলেন তখন সাওয়ারীকে একটু দ্রুত খেদালেন।\n\n<b>১০ই যুলহিজ্জাহ: জামরাতুল আক্বাবায় কঙ্কর নিক্ষেপ ও কুরবানি:</b>\n\nআর মধ্যের পথ দিয়ে যা জামরায়ে কুবরার দিকে বের হয় সে পথ ধরে জামরায়ে কুবরার নিকট গিয়ে পৌঁছেন। ঐ সময় সেখানে একটি বৃক্ষ ছিল। এ বৃক্ষটির জন্যও জামরায়ে কুবরা প্রসিদ্ধ ছিল। তাছাড়া জামরায়ে কুবরাকে জামরায়ে ‘আক্বাবাহ এবং জামরায়ে উলাও বলা হয়। নাবী কারীম (ﷺ) জামরায়ে কুবরায় ৭টি কংকর নিক্ষেপ করেন। প্রতিটি কংকর নিক্ষেপের সময় তাকবীর ধ্বনি উচ্চারণ করছিলেন। কংকরগুলো আকারে এ রকম ছোট ছিল যে সেগুলোকে চিমটিতে ধরে নিক্ষেপ করা যাচ্ছিল। নাবী কারীম (ﷺ) বাতনে ওয়াদী হতে দাঁড়িয়ে কংকরগুলো নিক্ষেপ করেছিলেন।\n\nঅতঃপর নাবী কারীম (ﷺ) কুরবানী স্থানে গিয়ে তাঁর মুবারক হাত দ্বারা ৬৩টি উট যবেহ করেন। অতঃপর রাসূলুল্লাহ (ﷺ)-এর নির্দেশক্রমে আলী (রাঃ) ৩৭টি উট যবেহ করেন। এভাবে এক শতটি উট কুরবানী করা হয়। রাসূলুল্লাহ (ﷺ) আলী (রাঃ)-কে তাঁর কুরবানীতে শরিক করে নেন। এরপর নাবী কারীম (ﷺ)-এর নির্দেশে প্রত্যেকটি যবেহকৃত পশু হতে এক একটি অংশ কেটে নিয়ে রান্না করা হয়। রাসূলুল্লাহ (ﷺ) এবং আলী (রাঃ) এ মাংস খান এবং ঝোল পান করেন।\n\n<b>তাওয়াফে ইফাদাহ ও জমজম কূপের নিকট আগমন:</b>\n\nঅতঃপর আপন সওয়ারীতে আরোহণ করে রাসূলুল্লাহ (ﷺ) মক্কা গমন করেন। মক্কা পৌঁছার পর তিনি বায়তুল্লাহ তাওয়াফ করেন। এ তাওয়াফকে তাওয়াফে ইফাযা বলা হয়। তাওয়াফ শেষে যুহর সালাত আদায় করেন। সালাত শেষে জমজম কূপের নিকট বনু আব্দুল মুত্তালিবের পাশে গমন করেন। তাঁরা হাজীদেরকে জমজমের পানি পান করাচ্ছিলেন। তিনি বলেন-\n\nاِنْزِعُوْا بَنِيْ عَبْدِ الْمُطَّلِبِ، فَلَوْلَا أَنْ يُغَلِّبُكُمْ النَّاسُ عَلٰى سِقَايَتِكُمْ لَنَزَعْتُ مَعَكُمْ\n\n‘বনু আব্দুল মুত্তালিব! তোমরা পানি উত্তোলন কর। যদি এ আশঙ্কা না থাকত যে পানি পান করানোর কাজে লোকজন তোমাদের উপর প্রবল হয়ে তোমাদেরকে পরাভূত করবে, তাহলে আমিও তোমাদের সাথে পানি উত্তোলন করতাম।’ অন্যথায় এ ব্যবস্থা তাঁদের আয়ত্বে আর থাকত না। কাজেই, বনু আব্দুল মুত্তালিব রাসূলুল্লাহ (ﷺ)-এর খিদমতে এক বালতি পানি পেশ করলেন এবং তিনি তা থেকে পানি পান করলেন।\n\n<b>[মুসলিম, জাবির হতে, নাবী কারীম (সাঃ)-এর হজ্জ অধ্যায় ১ম খন্ড ৩৯৭-৪০০ পৃঃ]</b>\n\n<b>১০ই যুলহিজ্জাহ মীনায় কুরবানির দিনের ঐতিহাসিক ভাষণ:</b>\n\nদিনটি ছিল যুল হিজ্জাহ মাসের ১০ তারিখ কুরবানীর দিন। এ দিবস সূর্য কিছুটা উপরে উঠল (চাশতের সময়) রাসূলুল্লাহ (ﷺ) একটি ধূসর বর্ণের খচ্চরের পিঠে আরোহণ করে ভাষণ দিলেন। আলী (রাঃ) রাসূলুল্লাহ (ﷺ)-এর বাণী উচ্চৈঃস্বরে মানুষের মাঝে পৌঁছিয়ে দিচ্ছিলেন। সমবেত জনতার কেউ দাঁড়িয়ে ছিল এবং কেউ উপবিষ্ট ছিল। বর্ণনাকারী বলেন, রাসূলুল্লাহ (ﷺ) কুরবানীর দিন রাসূলুল্লাহ (ﷺ) তাঁর ভাষণে আমাদের নিকট বলেন-\n\nإِنَّ الزَّمَانَ قَدْ اسْتَدَارَ كَهَيْئَتِهِ يَوْمَ خَلَقَ اللهُ السَّمٰوَاتِ وَالْأَرْضِ، السَّنَةُ اثْنَا عَشَرَ شَهْرًا، مِنْهَا أَرْبَعَةٌ حُرُمٌ، ثَلَاثٌ مُتَوَالِيَاتٌ: ذُو الْقَعْدَةِ وَذُو الْحِجَّةِ وَالْمُحَرَّمُ، وَرَجَبُ مُضَرَ الَّذِيْ بَيْنَ جُمَادٰي وَشَعْبَانَ\n\n‘আবর্তন বিবর্তনের মধ্য দিয়ে সময় সে দিনের প্রকৃতিতেই পৌঁছেছে যে দিন আসমান ও জমিনকে আল্লাহ সৃষ্টি করেছিলেন। এক বছর বারো মাসে বিভক্ত। যার মধ্যে চারটি মাস হচ্ছে অতি সম্মানিত মাস। এর মধ্যে তিনটি মাস ধারাবাহিক- যুল ক্বা’দাহ, যুল হিজ্জাহ ও মুহাররাম এবং আর একটি রজব মুযার যা জুমাদাল আখিরাহ এবং শাবানের মাঝে অবস্থিত।’\n\nঅতঃপর নাবী জিজ্ঞেস করলেন, ‘এটা কোন্ মাস? আমরা বললাম, ‘আল্লাহ এবং তাঁর রাসূল (ﷺ) ভাল জানেন।’ এ প্রেক্ষিতে তিনি কিছুক্ষণ নীরব রইলেন। আমরা ধারণা করলাম যে, তিনি হয়তো বা এ মাসের অন্য কোন নাম রাখবেন। অতঃপর তিনি বললেন, ‘এ মাসটি কি যুল হিজ্জাহ নয়?’ আমরা বললাম, ‘তা কেন হবে না’?\n\nএর পর নাবী কারীম (ﷺ) বললেন, ‘এ শহরটি কোন্ শহর?’ আমরা বললাম, ‘আল্লাহ এবং তাঁর রাসূল ভাল জানেন।’ নাবী কারীম কিছুক্ষণ নীরব থাকলেন যাতে আমরা ধারণা করলাম যে তিনি হয়তো এ শহরের অন্য কোন নাম রাখবেন। অতঃপর তিনি বললেন, ‘এ শহর কি পবিত্র সম্মানিত শহর নয়?’ আমরা বললাম, ‘অবশ্যই এটা সম্মানিত শহর।’ অতঃপর তিনি বললেন, ‘আজকের এ দিনটি কোন্ দিন?’ আমরা বললাম, ‘আল্লাহ এবং তাঁর রাসূল ভাল জানেন।’ নাবী কারীম (ﷺ) কিছুক্ষণ নীরব থাকলেন। এতে আমরা ধারণা করলাম যে তিনি হয়তো বা এ দিনের অন্য কোন নাম রাখবেন। অতঃপর তিনি বললেন, ‘এ দিনটি কি কুরবানীর দিন নয়’? অর্থাৎ ১০ই যুল হিজ্জাহ নয়’? আমরা বললাম অবশ্যই’।\n\nতিনি বললেন-\n\nفَإِنَّ دِمَاءَكُمْ وَأَمْوَالَكُمْ وَأَعْرَاضَكُمْ عَلَيْكُمْ حَرَامٌ كَحُرْمَةِ يَوْمِكُمْ هٰذَا، فِيْ بَلَدِكُمْ هٰذَا، فِيْ شَهْرِكُمْ هٰذَا\n\n‘তাহলে তোমরা জেনে রাখ যে, তোমাদের রক্ত, তোমাদের ধন সম্পদ এবং তোমাদের মান ইজ্জত পরস্পর পরস্পরের নিকট এমন পবিত্র তোমাদের এ শহর এবং তোমাদের এ মাস তোমাদের আজকের দিন যেমন পবিত্র।‘\n\nوَسَتَلْقَوْنَ رَبَّكُمْ، فَيَسْأَلُكُمْ عَنْ أَعْمَالِكُمْ، أَلَا فَلَا تَرْجِعُوْا بَعْدِيْ ضَلَالاً يَضْرِبُ بَعْضُكُمْ رِقَابَ بَعْضٍ ألَا هَلْ بَلَّغْتُ\n\nতোমরা অতি শীঘ্রই আপন প্রতিপালক প্রভূর সঙ্গে সাক্ষাত করলে তোমাদের কার্যকলাপ সম্পর্কে তিনি তোমাদের জিজ্ঞেস করবেন। অতএব, স্মরণ রেখো যেন আমার পরে পুনরায় পশ্চাদমুখীনতা অবলম্বনের মাধ্যমে পথভ্রষ্ট হয়ে না যাও। অধিকন্তু তোমরা এমন কোন কাজে লিপ্ত হবে না যার ফলে পরস্পর পরস্পরের গ্রীবা কর্তন করবে। বল! আমি কি তাবলীগের দায়িত্ব পালন করেছি?\n\nউপস্থিত সাহাবীগণ সমস্বরে বললেন, ‘হ্যাঁ, অবশ্যই!’ তখন তিনি বললেন, ‘হে আল্লাহ! তুমি সাক্ষী থাক!’\n\n<b>অপরাধের একক দায় ও শয়তানের চিরতরে হতাশা:</b>\n\nঅন্য এক বর্ণনায় আছে যে, এ ভাষণে নাবী কারীম (ﷺ) এ কথাও বলেছিলেন-\n\nألَا لَا يَجْنِيْ جَانٍ إِلَّا عَلٰى نَفْسِهِ، أَلَا لَا يَجْنِيْ جَانٍ عَلٰى وَلَدِهِ، وَلَا مَوْلُوْدٌ عَلٰى وَالِدِهِ، أَلَا إِنَّ الشَّيْطَانَ قَدْ يَئِسَ أَنْ يُعْبَدَ فِيْ بَلَدِكُمْ هٰذَا أَبَداً، وَلٰكِنْ سَتَكُوْنُ لَهُ طَاعَةٌ فِيْمَا تَحْتَقِرُوْنَ مِنْ أَعْمَالِكُمْ، فَسَيَرْضٰى بِهِ\n\nস্মরণ রেখো! কোন অপরাধী নিজ অপরাধের দোষ অন্যের উপর আরোপ করতে পারবে না। (অর্থাৎ অপরাধের শাস্তি অপরাধীকে নিজেকেই ভোগ করতে হবে। অপরাধের জন্য নিজেকেই গ্রেফতার হতে হবে)। আরও স্মরণ রেখো! পিতার অপরাধের জন্য পুত্রকে কিংবা পুত্রের অপরাধের জন্য পিতাকে শাস্তি ভোগ করতে হবে না। স্মরণ রেখো! শয়তান নিরাশ হয়ে পড়েছে এ কারণে যে, এখন থেকে তোমাদের এ শহরে আর কখনো তার পূজা করা হবে না। কিন্তু যে সব অন্যায় কাজকে তোমরা খুব তুচ্ছ মনে করবে ওতেই তোমরা তার আনুগত্য করবে আর এতেই সে সন্তুষ্ট থাকবে।\n\n<b>[তিরমিযী ২য় খন্ড ১৬৫ পৃঃ। ইবনু মাজাহ হজ্জ পর্ব, মিশকাত ১ম খন্ড ২৩৪ পৃঃ]</b>\n\n<b>আইয়ামে তাশরীকে মীনায় অবস্থান ও রউসের ভাষণ:</b>\n\nএরপর রাসূলুল্লাহ (ﷺ) ১১, ১২, ও ১৩ যুল হিজ্জাহ (আইয়ামে তাশরীক) মীনায় অবস্থান করেন। এ সময় তিনি হজ্জের নিয়ম কানুন পালন করতে থাকেন এবং লোকজনকে শরীয়তের আহবানগুলো শিক্ষা দিতে থাকেন ও আল্লাহর যিকির করতে থাকেন। অধিকন্তু ইবরাহীমী রীতিনীতির সুনানে হাদীসমূহ প্রতিষ্ঠা করতে থাকেন এবং শিরকের নিশানগুলো নিশ্চিহ্ন করতে থাকেন। নাবী কারীম (ﷺ) আইয়ামে তাশরীকেও ভাষণ প্রদান করেন। সুনানে আবী দাউদে হাসান সনদে বর্ণিত আছে সারায়া বিনতে নাবহান (রাঃ) বলেন যে, ‘রাসূলুল্লাহ (ﷺ) আমাদেরকে রউসের দিন ভাষণ দেন।\n\n<b>[আউনুল মাবূদ ২য় খন্ড ১৪৩ পৃঃ]</b>\n\nতিনি বলেন, أَلَيْسَ هٰذَا أَوْسَطُ أَيَّامِ التَّشْرِيْقِ ‘এটা কি আইয়ামে তাশরীকের মধ্য দিবস নয়?’\n\n<b>[আবূ দাউদ মীনায় কোন দিন ভাষণ দেন। ১ম খন্ড ২৬৯ পৃঃ]</b>\n\n<b>তাওয়াফে বিদা এবং মদিনা মুনাওয়ারায় প্রত্যাবর্তন:</b>\n\nনাবী কারীম (ﷺ)-এর আজকের ভাষণও গতকালের ভাষণের অনুরূপ ছিল। এ ভাষণ দেয়া হয়েছিল সূরাহ নাসর নাজিল হওয়ার পর। আইয়ামে তাশরীকের শেষে, দ্বিতীয় ইয়াওমুন নাফারে অর্থাৎ ১৩ই যুল হিজ্জাহ তারিখে নাবী কারীম (ﷺ) মীনা হতে রওয়ানা হয়ে যান এবং ওয়াদীয়ে আবতাহ এর খাইফে বনু কিনানাহয় অবস্থায় করেন। দিনের অবশিষ্ট সময় এবং রাত্রি তিনি তথায় অতিবাহিত করেন এবং যুহর সালাত, আসর, মাগরিব ও এশার সালাত সেখানেই আদায় করেন। এশার সালাত শেষে তিনি ঘুমিয়ে পড়েন এবং কিছুক্ষণ ঘুমানোর পর সওয়ারীতে আরোহণ করে বায়তুল্লাহ গমন করেন এবং তাওয়াফে বিদা’ আদায় করেন।\n\nসকল মানুষ যখন হজ্জ (হজ্বের নিয়মাবলী) হতে ফারেগ হয়ে গেল। রাসূলুল্লাহ (ﷺ) আপন সওয়ারীকে মদীনা মনোয়ারাভিমুখী করলেন। তাঁর মদীনামুখী হওয়ার উদ্দেশ্য ছিল সেখানে গিয়ে আরাম আয়েশে গা ঢেলে দেয়া নয় বরং উদ্দেশ্য ছিল আল্লাহর দ্বীনের প্রয়োজনে আর এক নবতর প্রচেষ্টায় লিপ্ত হওয়া।",
            "<b>Parting Counsel to Mu'adh ibn Jabal (RA) and Foretelling of Imminent Departure:</b>\n\nThe mission of Islamic propagation reached sublime fulfillment. A new monotheistic social order was firmly established upon absolute divine sovereignty and the final messengerhood of Prophet Muhammad (ﷺ). Soon, divine indications revealed that the worldly time of the Prophet (ﷺ) was drawing to a close. During the 10th year of Hijrah, upon appointing Mu'adh ibn Jabal (RA) as governor to Yemen, the Prophet (ﷺ) parted with profound words:\n\n\"O Mu'adh, you may not meet me again after this year of mine, and perhaps you will pass by this mosque of mine and my grave.\"\n\nHearing these words of imminent separation from the lips of the Messenger of Allah (ﷺ), Mu'adh (RA) wept profusely in intense grief.\n\n<b>Announcement of the Historic Pilgrimage and Assembly of the Companions:</b>\n\nAlmighty Allah willed that the Messenger (ﷺ) witness the glorious fruits of his tireless struggle before passing, and receive in the company of the entire Arab tribes their pledge of having faithfully conveyed Allah's sacred trust to mankind.\n\nWhen the Prophet (ﷺ) announced his divine intention to undertake the historic Pilgrimage, countless believers from across Arabia assembled eagerly to accompany him.\n\n<b>Departure from Madinah and Assuming Ihram at Dhul Hulaifah:</b>\n\nOn Saturday, four days remaining in Dhul Qi'dah 10 AH (the 25th or 26th of Dhul Qi'dah), the Prophet (ﷺ) prepared for departure before Zuhr.\n\nHe combed and oiled his hair, wore clean unstitched garments (Izar and Rida), and garlanded sacrificial animals. He performed two rak'ahs of Zuhr prayer shortened (Qasr) and departed with the companions. Arriving at Dhul Hulaifah before Asr, he prayed two rak'ahs of Asr and camped overnight. In the morning, he informed his companions:\n\n\"A heavenly messenger from my Lord came to me tonight and said: 'Pray in this blessed valley and say: Umrah combined with Hajj.'\"\n\n<b>[Narrated from Umar (RA) in Sahih al-Bukhari: Vol. 1, p. 207]</b>\n\nBefore Zuhr, the Prophet (ﷺ) bathed for Ihram. Mother of the Believers Aisha (RA) applied musk and perfumed fragrance (Dharirah) to his head and beard, with its luster visibly glistening. He wore the two unstitched sheets, prayed two rak'ahs of Zuhr, and assumed Ihram for both Hajj and Umrah (Qiran), mounting his camel Qaswa.\n\nHe proclaimed the Talbiyah loudly with Takbir and Tahlil:\n\n\"Labbayk Allahumma Labbayk, Labbayka Laa Shareeka Laka Labbayk, Innal-Hamda Wan-Ni'mata Laka Wal-Mulk, Laa Shareeka Lak!\"\n(Here I am at Your service, O Allah, here I am! You have no partner, here I am! Truly all praise, grace, and sovereignty belong to You! You have no partner!)\n\n<b>Arrival in Makkah, Tawaf around the Ka'bah and Sa'i between Safa and Marwah:</b>\n\nTraversing the route, he reached Dhi Tuwa near Makkah on Sunday night, the 4th of Dhul Hijjah. He rested until dawn, prayed Fajr, bathed, and entered the Holy Sanctuary.\n\nHe performed Tawaf around the Ka'bah and Sa'i between Safa and Marwah. Because he had brought sacrificial animals (Hady), he did not exit Ihram, maintaining the single Ihram for both Hajj and Umrah.\n\nHe instructed those companions who had not brought sacrificial animals to change their intention to Umrah and exit Ihram upon completing Tawaf and Sa'i. When some hesitated out of reverence, the Prophet (ﷺ) explained:\n\n\"Had I known beforehand what I realized later, I would not have brought sacrificial animals, and had I not brought them, I would certainly have exited Ihram.\"\n\nHearing this reassuring guidance from the Messenger of Allah (ﷺ), the companions complied willingly and those without sacrificial animals exited Ihram.\n\n<b>8th Dhul Hijjah (Tarwiyah) and Proceeding to Arafat on 9th Dhul Hijjah:</b>\n\nOn the 8th of Dhul Hijjah (Yawm at-Tarwiyah), the Prophet (ﷺ) departed for Mina with his companions, performing five prayers (Zuhr, Asr, Maghrib, Isha, and Fajr of the 9th). He remained until after sunrise.\n\nAfter sunrise, he proceeded to Arafat where a tent had been pitched for him at Namirah. When the sun passed meridian, he mounted his camel Qaswa and proceeded to Batn al-Wadi (the valley bottom).\n\n<b>The Historic Farewell Sermon at Arafat:</b>\n\nAn overwhelming assembly of 124,000 to 144,000 companions gathered around him. He delivered his monumental Farewell Sermon, opening with:\n\n\"O people! Listen attentively to my words, for I do not know whether I shall meet you again in this gathering after this year!\"\n\n<b>[Sirah Ibn Hisham: Vol. 2, p. 603]</b>\n\n<b>Sanctity and Inviolability of Life, Wealth, and Honor:</b>\n\n\"Verily, your blood and your property are sacred and inviolable until you meet your Lord, just as the sacredness of this day, this month, and this blessed city!\"\n\n<b>Complete Abolition of Pagan Customs and Blood-Feuds:</b>\n\n\"Behold! Everything of pagan Jahiliyyah is placed and trampled under my feet! All blood-feuds of Jahiliyyah are permanently abolished. The first blood-feud I abolish is that of Ibn Rabi'ah ibn al-Harith (who was nursed among Banu Sa'd and slain by Hudhayl).\"\n\n<b>Total Abolition of Usury (Riba):</b>\n\n\"All usury (Riba) of Jahiliyyah is completely abolished. The first usury I abolish is that of our family, the usury of Abbas ibn Abdul Muttalib; all of it is canceled entirely!\"\n\n<b>Rights and Dignified Treatment of Women:</b>\n\n\"Fear Allah regarding women! You have taken them under Allah's trust and made marital intimacy lawful through His word. You have rights over them that they must not permit anyone you dislike to step onto your bedding; if they do, you may discipline them without causing injury. And they have rights over you: to be provided with honorable sustenance and dignified clothing.\"\n\n<b>Safeguard Against Misguidance: The Book of Allah:</b>\n\n\"I leave among you that which, if you hold fast to it, you shall never go astray: the Book of Allah.\"\n\n<b>[Sahih Muslim: Vol. 1, p. 397]</b>\n\n<b>Finality of Prophethood and Essential Pillars:</b>\n\n\"O people! There is no Prophet after me, and no Ummah after you! Worship your Lord, establish your five daily prayers, fast during Ramadan, pay your Zakat willingly from your wealth, perform pilgrimage to the House of your Lord, and obey your righteous leaders; you shall enter the Paradise of your Lord.\"\n\n<b>[Sunan Ibn Majah, Rahmatul-lil-'Alameen: Vol. 1, p. 223]</b>\n\n<b>Historic Testimony of the Ummah and 'O Allah, Bear Witness!':</b>\n\n\"You will be asked concerning me; what will you say?\"\n\nThe companions responded in unified voice:\n\n\"We testify that you have faithfully conveyed the message, fulfilled the trust, and advised the Ummah with supreme sincerity!\"\n\nThe Prophet (ﷺ) raised his index finger toward heaven and pointed toward the people thrice, declaring:\n\n\"O Allah, bear witness! O Allah, bear witness! O Allah, bear witness!\"\n\n<b>[Narrated from Ibn Umar in Sahih al-Bukhari, Rahmatul-lil-'Alameen: Vol. 1, p. 265]</b>\n\n<b>Resounding Echo of the Sermon:</b>\n\nRabi'ah ibn Umayyah ibn Khalaf (RA), endowed with an exceptionally powerful and resonant voice, repeated each sentence of the Prophet (ﷺ) loudly so that the entire gathering could hear clearly across the vast plains.\n\n<b>Divine Revelation of the Perfection of Islam:</b>\n\nFollowing the sermon, Allah Almighty revealed Surah Al-Ma'idah (5:3):\n\n\"Today I have perfected your religion for you, completed My favor upon you, and approved Islam as your religion.\"\n\n<b>[Surah Al-Ma'idah: 5/3]</b>\n\nUpon hearing this ayah, Umar (RA) wept. When asked why, he replied: 'After perfection, there remains only departure and separation.'\n\n<b>Muzdalifah, Night Vigil, and Supplication at Mash'ar al-Haram:</b>\n\nBilal (RA) called the Adhan and Iqamah. The Prophet (ﷺ) led Zuhr and Asr combined, performing no prayers in between. He rode Qaswa to the standing place at Jabal ar-Rahmah, facing the Qiblah until sunset. After sunset, he departed with Usama ibn Zayd (RA) riding behind him, proceeding to Muzdalifah.\n\nAt Muzdalifah, he combined Maghrib and Isha with two Iqamahs. He rested until dawn, prayed Fajr with Adhan and Iqamah at first light, mounted Qaswa to Mash'ar al-Haram, and supplicated facing the Qiblah with Takbir and Tahlil until daylight brightened.\n\nBefore sunrise, he proceeded to Mina with Fadl ibn Abbas (RA) riding behind him. At Wadi Muhassar, he urged his mount slightly faster.\n\n<b>10th Dhul Hijjah: Stoning Jamrat al-Aqabah and Sacrificing Animals:</b>\n\nTaking the middle path toward Jamrat al-Kubra (Jamrat al-Aqabah), where a prominent tree stood, he cast seven small pebbles, pronouncing Takbir with each throw from the valley floor.\n\nHe proceeded to the place of sacrifice and slaughtered 63 camels with his blessed hand. He appointed Ali (RA) to slaughter the remaining 37, completing 100 camels. Portions from each camel were cooked, and the Prophet (ﷺ) and Ali (RA) partook of the meat and broth.\n\n<b>Tawaf al-Ifadah and Drinking Zamzam:</b>\n\nMounting his camel, the Prophet (ﷺ) proceeded to Makkah, performed Tawaf al-Ifadah around the Ka'bah, and prayed Zuhr. He visited the Well of Zamzam where the clan of Banu Abdul Muttalib were serving water to pilgrims. He said:\n\n\"Draw water, O children of Abdul Muttalib! Were it not that people would overwhelm you and take over your privilege of providing water, I would have drawn water with you.\"\n\nThey presented a bucket of Zamzam water, and the Messenger of Allah (ﷺ) drank joyfully from it.\n\n<b>[Sahih Muslim: Narrated by Jabir RA, Vol. 1, pp. 397-400]</b>\n\n<b>10th Dhul Hijjah: The Mina Sermon on the Day of Nahr:</b>\n\nDuring the forenoon of the 10th of Dhul Hijjah (Yawm an-Nahr), the Prophet (ﷺ) mounted a gray mule in Mina and delivered another sermon, with Ali (RA) repeating his words loudly. He declared:\n\n\"Time has completed its cycle as on the day Allah created the heavens and the earth. The year consists of twelve months, of which four are sacred: three consecutive—Dhul Qi'dah, Dhul Hijjah, and Muharram—and Rajab of Mudar, which lies between Jumada and Sha'ban.\"\n\nThe Prophet (ﷺ) asked: 'What month is this?' The companions replied: 'Allah and His Messenger know best.' He remained silent until they thought he might name it differently. He asked: 'Is it not Dhul Hijjah?' They replied: 'Indeed!'\n\nHe asked: 'What city is this?' They replied: 'Allah and His Messenger know best.' He asked: 'Is it not the Sacred City (Makkah)?' They replied: 'Indeed!' He asked: 'What day is this?' They replied: 'Allah and His Messenger know best.' He asked: 'Is it not the Day of Sacrifice (Yawm an-Nahr)?' They replied: 'Indeed!'\n\nHe declared:\n\n\"Verily, your blood, your property, and your honor are inviolable and sacred among you, just like the sacredness of this day, this city, and this month of yours! You shall soon meet your Lord, and He will question you concerning your deeds. Beware! Do not revert to misguidance after me, striking one another's necks. Have I conveyed the message?\"\n\nThe companions responded: 'Yes, indeed!' The Prophet said: 'O Allah, bear witness!'\n\n<b>Individual Accountability for Crimes and Satan's Despair:</b>\n\nIn another narration, the Prophet (ﷺ) proclaimed:\n\n\"Let no offender commit a crime except against himself! No father is accountable for his son's wrongdoing, nor is any son accountable for his father's wrongdoing! Truly, Satan has despaired of ever being worshipped in this land of yours; but he will be obeyed in evil deeds you deem trivial, and he will be pleased with that!\"\n\n<b>[Sunan at-Tirmidhi: Vol. 2, p. 165; Sunan Ibn Majah; Mishkat al-Masabih: Vol. 1, p. 234]</b>\n\n<b>Encampment in Mina during Ayyam at-Tashreeq:</b>\n\nThe Prophet (ﷺ) remained in Mina during the 11th, 12th, and 13th of Dhul Hijjah (Ayyam at-Tashreeq), performing the rites of Hajj, instructing the people in divine commandments, remembering Allah abundantly, and reviving the pristine Ibrahimic legacy.\n\nHe also delivered a sermon during Tashreeq. Saraya bint Nabhan (RA) narrated in Sunan Abi Dawud: 'The Messenger of Allah (ﷺ) addressed us on the Day of Ru'us (11th Dhul Hijjah).'\n\n<b>[Awn al-Ma'bud: Vol. 2, p. 143]</b>\n\nHe said: 'Is this not the middle day of Tashreeq?'\n\n<b>[Sunan Abi Dawud: Vol. 1, p. 269]</b>\n\n<b>Tawaf al-Wida' (Farewell Tawaf) and Return to Madinah Munawwarah:</b>\n\nHis sermon on that day echoed the themes of his previous address, delivered following the revelation of Surah An-Nasr. At the end of Tashreeq, on the 13th of Dhul Hijjah (Second Yawm an-Nafr), he departed Mina and encamped at Khaif Banu Kinanah in Wadi Abtah, spending the day and night there. After praying Zuhr, Asr, Maghrib, and Isha, he rested briefly, mounted his camel, visited the Ka'bah, and performed Tawaf al-Wida' (Farewell Tawaf).\n\nHaving fulfilled every sacred rite of the Pilgrimage, the Prophet (ﷺ) set his blessed journey back toward Madinah Munawwarah—not to rest in comfort, but to continue his tireless devotion and mission in the service of Allah's divine message until his final breath."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getAdhiArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: দশ-ই- জিলহজ্ব সময় কর্মকান্ড সমূহ (100% Verbatim)
        cards.add(new HajjArticleCardItem(
            1,
            "দশ-ই- জিলহজ্ব সময় কর্মকান্ড সমূহ",
            "Key Rites and Actions on 10th Dhul Hijjah",
            "•  জামরা আকাবার দিকে রওয়ানা\n" +
            "•  তালবিয়া বন্ধ\n" +
            "•  জামারায় কঙ্কর নিক্ষেপ\n" +
            "•  ঈদের তাকবির\n" +
            "• কুরবানীর পশু। যাসী জবেহ করা\n" +
            "• মাথার চুল মুত্তন বা ছোট করা",
            "• Departing toward Jamrat al-Aqabah\n" +
            "• Ceasing the Talbiyah\n" +
            "• Casting pebbles at the Jamarah\n" +
            "• Proclaiming the Eid Takbir\n" +
            "• Slaughtering the sacrificial animal\n" +
            "• Shaving or trimming the hair"
        ));

        // Card 2: দশ-ই- জিলহজ্ব (কুরবানীর দিন) (100% Verbatim)
        cards.add(new HajjArticleCardItem(
            2,
            "দশ-ই- জিলহজ্ব (কুরবানীর দিন)",
            "10th Dhul Hijjah (The Day of Sacrifice)",
            "পৃথিবীর পূর্ব ও পশ্চিম, উত্তর ও দক্ষিণ প্রান্ত থেকে সমবেত মুস- লিমগণ মিনার ময়দানে এক বিশেষ আবেগ নিয়ে ঈদুল আযহার এ মুবারক দিনটিকে স্বাগত জানায়। সবাই আল্লাহর অনুগ্রহ পেয়ে আনন্দচিত্তে আল্লাহর নৈকট্য লাভের আশায় পশু কুরবানী করে। এই দিন জামরা আকাবা তথা বড় জামরায় কঙ্কর নিক্ষেপের পরপরই হাজ্বী সাহেবগণ ঈদের তাকবীর বলা শুরু করবেন। আর তা হলো-\n" +
            "الله أكبر الله أكبر الله أكبر.. لا إله إلا الله .. الله أكبر الله أكبر ولله الحمد\n" +
            "জামরায় কঙ্কর নিক্ষেপের সময় কোন কোন হাজ্বী সাহেব কিছু ভুল করে থাকেন। তন্মধ্যে বিশেষভাবে উল্লেখযোগ্য হলো-\n" +
            "• কেউ কেউ এ বিশ্বাস করেন যে, তারা শয়তানকে কঙ্কর মারছে, এ জন্য খুব ক্রোধ নিয়ে শয়তানকে গালমন্দ করে কঙ্কর মেরে থাকেন। অথচ জামারায় কঙ্কর নিক্ষেপের একমাত্র উদ্দেশ্যে হচ্ছে আল্লাহর জিকিরকে সতেজ করা।\n\n\n" +
            "• আবার কেউ কেউ বড় পাথর, জুতা বা কাঠ ইত্যাদি নিক্ষেপ করে থাকেন। এটা দ্বীনের কাজে বাড়াবাড়ি। আর নবী (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) বাড়াবাড়ি করতে নিষেধ করেছেন।\n\n\n" +
            "• কঙ্কর নিক্ষেপ করার সময় কেউ কেউ জামরার নিকট খুব ভিড়ের সৃষ্টি করেন, এমনকি অনেকে মারামারি করেন। এটা মারাত্মক ভুল। হাজ্বী সাহেবদের উচিৎ, অপর ভাইয়ের সাথে নম্র ব্যবহার করা। কঙ্কর নিক্ষেপের সময় কঙ্কর হাউজের ভিতর পড়েছে কিনা নিশ্চিত হতে হবে। খুঁটিতে লাগা জরুরী নয়।\n\n\n" +
            "• কেউ আবার সাতটি কঙ্কর একসাথেই মেরে থাকেন। এমতাবস্থায় শুধুমাত্র একটি কংকর গণনা করা হবে। নিয়ম হচ্ছে; এক এক করে কংকর মারা এবং প্রতিটি মারার সময় তাকবীর বলা।\n\n" +
            "“কুরবানীর দিন জামরা আকাবায় কঙ্কর নিক্ষেপ, হাদী জবেহ (যাদের উপর কুরবানী বা হাদী ওয়াজিব) ও মাথার চুল মুত্তন বা ছোট করা হয়ে গেলে হাজ্বী সাহেব প্রাথমিকভাবে হালাল হয়ে যাবেন। এই হালালের পর ইহরামের কারণে তার উপর যে কাজগুলো নিষিদ্ধ ছিল সবগুলো তার জন্য হালাল হয়ে যাবে, শুধুমাত্র স্ত্রী ছাড়া।“",
            "Believers gathered from the eastern and western, northern and southern ends of the earth welcome this blessed day of Eid al-Adha on the plains of Mina with profound devotion and emotion. Rejoicing in Allah's boundless grace, everyone offers sacrificial animals seeking nearness to Allah. On this day, immediately after casting pebbles at Jamrat al-Aqabah (the Great Pillar), the pilgrims begin proclaiming the Eid Takbir, which is:\n\n" +
            "<big><b>الله أكبر الله أكبر الله أكبر.. لا إله إلا الله .. الله أكبر الله أكبر ولله الحمد</b></big>\n" +
            "(Allah is the Greatest, Allah is the Greatest, Allah is the Greatest.. There is no god worthy of worship except Allah.. Allah is the Greatest, Allah is the Greatest, and all praise belongs to Allah)\n\n" +
            "During the throwing of pebbles at the Jamarah, some pilgrims make certain common mistakes. Among the most notable are:\n\n" +
            "• Some believe they are literally stoning Satan, throwing stones with intense anger while cursing the devil. However, the sole purpose of casting pebbles at the Jamarah is to revive and establish the remembrance of Allah.\n\n" +
            "• Others throw large boulders, shoes, sandals, or wooden sticks. This constitutes excess in religious matters, and the Prophet (ﷺ) explicitly forbade religious extremism and excess.\n\n" +
            "• While throwing pebbles, some create intense overcrowding and jostling, occasionally leading to altercations. This is a grave error. Pilgrims must treat their fellow brothers with gentleness. One must ensure that the pebbles land inside the basin (Hawd); hitting the pillar itself is not obligatory.\n\n" +
            "• Some cast all seven pebbles together at once. In this case, it is counted as only a single throw. The prescribed rule is to cast pebbles one by one, pronouncing Takbir (Allahu Akbar) with each individual throw.\n\n" +
            "“On the Day of Sacrifice (Yawm an-Nahr), once the pilgrim has cast pebbles at Jamrat al-Aqabah, slaughtered the sacrificial animal (for those on whom it is obligatory), and shaved or trimmed the hair, the pilgrim attains the first stage of deconsecration (Tahallul al-Asghar). After this partial exit from Ihram, everything that was prohibited during Ihram becomes lawful again, except for marital intimacy.”"
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getDhulHijjahArticleCards() {
        return getAdhiArticleCards();
    }

    public static List<HajjArticleCardItem> getHairCuttingArticleCards() {
        return getAdhiArticleCards();
    }

    public static List<HajjArticleCardItem> getTawafIfadahArticleCards() {
        return getAdhiArticleCards();
    }

    public static List<HajjArticleCardItem> getJamaratArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: হাজ্বী যখন মিনা পৌঁছবেন (100% Verbatim)
        cards.add(new HajjArticleCardItem(
            1,
            "হাজ্বী যখন মিনা পৌঁছবেন",
            "When the Pilgrim Reaches Mina",
            "হাজ্বী মিনা পৌঁছলে যথাসম্ভব দ্রুত জামরা আকাবা বা বড় জামরায় (যেটি জামারসমূহের মধ্যে মক্কার নিকটবর্তী) পৌঁছার চেষ্টা করবেন। পৌঁছেই তালবিয়া বন্ধ করে দিবেন। অতঃপর নিস্ত্রের কাজগুলো করবেন-\n\n" +
            "১.জামরা আকাবায় পরপর সাতটি কঙ্কর নিক্ষেপ করবেন। প্রতিটি কঙ্কর নিক্ষেপের সময় তাকবীর (আল্লাহু আকবর) বলবেন।\n" +
            "২.কুরবানী ওয়াজিব হয়ে থাকলে কুরবানী করবেন। সম্ভাব হাল নিজ হাতেই কুরবানী করবেন, নিজে যাবেন ও ফকীর মিসকিনকে খাওয়াবেন। সম্ভব না হলে কুরবানীর দায়িত্ব অন্য কাউকে দিবেন।\n" +
            "৩.মাথা মুন্ডন করবেন অথবা চুল খাটো করবেন। তবে মুন্ডন করাই উত্তম। আর মহিলাগণ সব চুল একসাথে করে চুলের অগ্রভাগ থেকে আঙ্গুলের মাথা পরিমান কাটবেন।\n\n" +
            "এ কাজগুলো ধারাবাহিকভাবে করাই উত্তম। ধারাবাহিকতা রক্ষা করতে না পারলেও কোন ক্ষতি নেই।",
            "When the pilgrim reaches Mina, they should try to proceed as quickly as possible to Jamrat al-Aqabah or the Great Jamarah (which is closest to Makkah among the Jamarat). Upon arriving, stop reciting the Talbiyah immediately. Then perform the following actions:\n\n" +
            "1. Cast seven pebbles consecutively at Jamrat al-Aqabah, uttering Takbir (Allahu Akbar) with each pebble cast.\n" +
            "2. If animal sacrifice (Qurbani / Hady) is obligatory upon you, offer the sacrifice. If feasible, slaughter with your own hands, partake in it, and feed the poor and needy. If not feasible, entrust the sacrificial responsibility to someone else.\n" +
            "3. Shave the head (Halq) or trim the hair short (Taqseer); however, shaving is superior. Women should gather all their hair together and trim a fingertip's length from the ends.\n\n" +
            "It is superior to perform these rites in this sequence; however, there is no harm if this order is not strictly maintained."
        ));

        // Card 2: মিনায় রাত যাপন (100% Verbatim)
        cards.add(new HajjArticleCardItem(
            2,
            "মিনায় রাত যাপন",
            "Staying Overnight in Mina",
            "মিনায় অবস্থানরত দিনগুলোতে (১০ ও ১১ জিলহজ) মিনাতেই রাত যাপন করুন। আর ১২ জিলহজ রাত যাপন করুন, যদি ১৩ জিলহজ 'রমি' (কঙ্কর নিক্ষেপ) শেষ করে ফিরতে চান (সুন্নাত)।",
            "During the days of staying in Mina (10th and 11th of Dhul Hijjah), stay overnight in Mina. And spend the night of the 12th of Dhul Hijjah as well if you intend to return after completing the 'Rami' (stoning) on the 13th of Dhul Hijjah (which is Sunnah)."
        ));

        // Card 3: মিনা ত্যাগ (100% Verbatim)
        cards.add(new HajjArticleCardItem(
            3,
            "মিনা ত্যাগ",
            "Departing Mina",
            "জিলহজ মিনায় না থাকতে চাইলে ১২ জিলহজ সন্ধ্যার আগে মিনা ত্যাগ করুন। সূর্যাস্তের আগে মিনা ত্যাগ করা উত্তম।",
            "If you do not wish to remain in Mina, depart from Mina before the evening of the 12th of Dhul Hijjah. It is superior to depart Mina before sunset."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getSafaMarwahArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: সাফা মারওয়া এর বিবরণ / Description of Safa and Marwah
        cards.add(new HajjArticleCardItem(
            1,
            "সাফা মারওয়া এর বিবরণ",
            "Description of Safa and Marwah",
            "সাফা মারওয়ার মাঝে যাওয়া-আসা করাকে সাঈ বলে। সাফা থেকে মারওয়া পর্যন্ত এক চক্কর হয়, আবার মারওয়া থেকে সাফায় ফিরে এলে আরেক চক্কর।\n\n" +
            "অনেকেই ভুল করে, সাফা থেকে মারওয়া আবার মারওয়া থেকে সাফায় পর্যন্ত, এক চক্কর হিসাব করে থাকে। অর্থাৎ সাফা মারওয়ার মাঝে ১৪ বার যাতায়াত করে ৭ চক্কর হিসাব করে থাকে, এটা মারাত্মক ভুল।",
            "Walking back and forth between the hills of Safa and Marwah is known as Sa'i. A single lap begins at Safa and ends at Marwah, and returning from Marwah to Safa constitutes another lap.\n\n" +
            "Many pilgrims mistakenly count a complete round trip from Safa to Marwah and back to Safa as a single lap. That is, they traverse between Safa and Marwah 14 times assuming it totals 7 laps; this is a grave misconception."
        ));

        // Card 2: সাফা / Safa
        cards.add(new HajjArticleCardItem(
            2,
            "সাফা",
            "Safa",
            "তাওয়াফ শেষ হলে ওমরা আদায়কারী সায়ী করার জন্য সাফা পাহাড়ের দিকে অগ্রসর হবে। সাফার কাছাকাছি হলে এই আয়াতটি পড়বে:\n\n" +
            "<big><b>إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ</b></big>\n\n" +
            "\"আল্লাহ তা'আলা যেটা দিয়ে শুরু করেছেন আমিও তাই দিয়ে শুরু করছি।\" (অর্থাৎ: আল্লাহ তা'আলা যেহেতু সাফার কথা আগে উল্লেখ করেছেন তাই সাফা থেকেই সায়ী শুরু করছি)।\n\n" +
            "এরপর সাফা পাহাড়ে আরোহণ করে কেবলামুখী হয়ে দাঁড়িয়ে আল্লাহর প্রশংসা আদায় করবে ও তিনবার তাকবীর বলবে। এরপর দু'হাত তুলে আল্লাহর কাছে বেশী করে দোয়া করবে। তিনবার এই দোয়াটি পড়বে-\n\n" +
            "<big><b>لا إله إلا الله وحده لا شريك له له الملك ولهُ الحَمْدُ وَهُوَ عَلَى كُلِّ شَيئ قدير - لا إله إلا الله وحده أنجز وَعَدَهُ وَنَصَرَ عَبدَهُ وَهَزَمَ الأَحْزَابَ وَحدَه</b></big>\n\n" +
            "এ দোয়াটি তিনবার পড়বে এবং মাঝে মাঝে অন্যান্য দোয়াও করবে। তিনবারের চেয়ে কম পড়লেও কোন ক্ষতি নেই। মনে রাখা জরুরী, শুধুমাত্র দোয়া করার সময় হাত তুলবে, তাকবীর বলার সময় হাত তুলবে না বা হাতে কোন ইশারা করবে না।\n\n" +
            "অনেক হজ্ব ও ওমরাকারীকে তাকবীর বলার সময় হাত তুলতে দেখা যায়। এটি একটি প্রচলিত ভুল।\n\n" +
            "এরপর সাফা থেকে নেমে স্বাভাবিকভাবে হেঁটে মারওয়ার দিকে রওয়ানা হবে। এ সময় যে কোন দোয়া করতে পারবে; নিজের জন্য, পরিবার-পরিজন, আত্মীয়-স্বজন ও সমগ্র মুসলিম উম্মাহর কল্যাণের জন্য দোয়া করবে। সবুজ দাগ পর্যন্ত পৌঁছলে প্রথম সবুজ দাগ থেকে দ্বিতীয় সবুজ দাগ পর্যন্ত পুরুষগণ দৌঁড়ে অতিক্রম করবেন। মহিলাগণ স্বাভাবিকভাবে চলবেন। এরপর মারওয়া পর্যন্ত স্বাভাবিকভাবে হেঁটে যাবে।",
            "Upon completing Tawaf, the pilgrim performing Umrah proceeds towards the hill of Safa to commence Sa'i. When approaching Safa, one recites this verse:\n\n" +
            "<big><b>إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ</b></big>\n\n" +
            "\"I begin with that which Allah began with.\" (Meaning: Since Allah mentioned Safa first in the Quran, we commence Sa'i from Safa).\n\n" +
            "Then, ascending the hill of Safa and facing the Qiblah, one praises Allah and proclaims Takbir three times. Thereafter, one raises both hands and supplicates abundantly to Allah, reciting this Dua three times:\n\n" +
            "<big><b>لا إله إلا الله وحده لا شريك له له الملك ولهُ الحَمْدُ وَهُوَ عَلَى كُلِّ شَيئ قدير - لا إله إلا الله وحده أنجز وَعَدَهُ وَنَصَرَ عَبدَهُ وَهَزَمَ الأَحْزَابَ وَحدَه</b></big>\n" +
            "(There is no god worthy of worship except Allah alone, without partner. To Him belongs all sovereignty and praise, and He is over all things competent. There is no god except Allah alone; He fulfilled His promise, granted victory to His servant, and defeated the allied factions alone.)\n\n" +
            "One recites this Dua three times and may supplicate with any other personal prayers in between. If one recites it fewer than three times, there is no harm. It is essential to remember: hands are raised only during supplication (Dua), not when uttering the Takbir nor should one make hand gestures or waving motions.\n\n" +
            "Many pilgrims mistakenly raise their hands while uttering Takbir as if starting prayer; this is a widespread error.\n\n" +
            "Afterward, one descends from Safa and walks at a normal pace towards Marwah. During this walk, one may make any supplication for oneself, one's family, relatives, and the entire Muslim Ummah. Upon reaching the green markers, men should run / jog briskly between the first and second green lights, while women continue walking normally. After passing the green markers, one walks normally until reaching Marwah."
        ));

        // Card 3: মারওয়া / Marwah
        cards.add(new HajjArticleCardItem(
            3,
            "মারওয়া",
            "Marwah",
            "• ওমরা পালনকারী মারওয়ায় পৌঁছার পর কেবলামুখী হয়ে সাফা পাহাড়ে যা যা করেছিলো তাই করবে, যে দোয়াগুলো সেখানে পড়েছিলো সেগুলো এখানেও পড়বে, দু'হাত তুলে কায়মনোবাক্যে আল্লাহর কাছে দোয়া করবে। তবে কুরআন শরীফের যে আয়াতটি সাফায় উঠার সময় পড়েছিলো সেটি মারওয়ায় উঠার সময় পড়বে না। এরপর নেমে সাফার দিকে যাবে। সবুজ দাগ পর্যন্ত পৌঁছলে প্রথম সবুজ দাগ থেকে দ্বিতীয় সবুজ দাগ পর্যন্ত পুরুষগণ দৌঁড়ে অতিক্রম করবেন, আর মহিলাগণ স্বাভাবিকভাবে হেঁটে অতিক্রম করবেন। এরপর স্বাভাবিকভাবে হেঁটে সাফা পর্যন্ত যাবে। এভাবে সাত চক্কর পূর্ণ করবে। সাফা থেকে শুরু করে মারওয়া পর্যন্ত আসলে এক চক্কর, মারওয়া থেকে সাফা গেলে আরেক চক্কর। এভাবে সাত চক্কর। সুতরাং, সায়ী শুরু হবে সাফা থেকে, শেষ হবে মারওয়ায়।\n\n" +
            "• বার্ধক্য, অসুস্থতা অথবা স্বাস্থ্যগত কোন সমস্যার কারণে হুইল চেয়ারে বসে সায়ী করতে পারবে। এতে কোন সমস্যা নেই।\n\n" +
            "• মহিলাদের জন্য হায়েয (ঋতু) ও নেফাস (প্রসব পরবর্তী অপবিত্রতা) অবস্থায় সায়ী করা জায়েয। তবে এ অবস্থায় তাওয়াফ জায়েয নেই। কারণ, সাফা মারওয়া মসজিদে হারামের অন্তর্ভুক্ত নয়।\n\n" +
            "• সায়ী শেষে ওমরা আদায়কারী মাথার চুল মুন্ডন করবে অথবা ছোট করবে। মুন্ডন করাই উত্তম। তবে যদি হজ্ব তামাত্তু হয় এবং ওমরা ও হজ্বের মাঝে সময়ের ব্যবধান খুব কম হয়, তখন ওমরা শেষে চুল ছোট করবে আর হজ্ব শেষে মুন্ডন করবে। এটাই উত্তম। তবে চুল ছোট করার সময় পুরো মাথা থেকেই কাটতে হবে।\n\n" +
            "• মহিলাগণ মাথা মুন্ডন করবে না। বরং, তারা মাথার সমস্ত চুল একসাথে ধরে আগা থেকে আঙ্গুলের মাথা পরিমান চুল কাটবে।\n\n" +
            "এভাবে ওমরার কাজসমূহ শেষ হয়ে যাবে এবং ইহরামের কারণে যে কাজগুলো ওমরাকারীর উপর নিষেধ ছিল সেগুলো তার জন্য বৈধ হয়ে যাবে।",
            "• Upon reaching Marwah, the pilgrim faces the Qiblah and performs everything performed at Safa: reciting the same supplications and praising Allah, earnestly raising both hands in sincere Dua. However, the Quranic verse recited upon approaching Safa is NOT recited when ascending Marwah. Afterwards, one descends towards Safa. Upon reaching the green markers, men should run / jog briskly between the first and second green lights, while women walk normally. Thereafter, walk at a normal pace to Safa. In this manner, seven laps are completed: from Safa to Marwah is one lap, and from Marwah back to Safa is another lap. Thus, Sa'i begins at Safa and concludes at Marwah.\n\n" +
            "• Due to old age, sickness, or physical health issues, performing Sa'i in a wheelchair is completely permissible without any issue.\n\n" +
            "• For women, performing Sa'i during menstruation (Hayd) or post-natal bleeding (Nifas) is permissible. However, Tawaf is not permissible in this state, because Safa and Marwah are not structurally part of Masjid al-Haram.\n\n" +
            "• Upon concluding Sa'i, the Umrah pilgrim either shaves the head (Halq) or trims the hair (Taqseer). Shaving is superior. However, for Hajj Tamattu' when the interval between Umrah and Hajj is brief, it is preferable to trim the hair after Umrah and shave after completing Hajj. When trimming, hair must be shortened evenly from all parts of the head.\n\n" +
            "• Women do not shave their heads. Instead, they gather all their hair together and trim approximately a fingertip's length (about one inch) from the tips.\n\n" +
            "With this, the rites of Umrah are complete, and all actions previously prohibited due to Ihram become lawful once again."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getSaiArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: সাই এর বিবরণ / Description of Sa'i
        cards.add(new HajjArticleCardItem(
            1,
            "সাই এর বিবরণ",
            "Description of Sa'i",
            "সাঈ অর্থ দৌড়ানো। কাবার অতি নিকটেই দু’টো ছোট্ট পাহাড় আছে যার একটি ‘সাফা’ ও অপরটির নাম ‘মারওয়া’। এ দু’ পাহাড়ের মধ্যবর্তী স্থানে মা হাজেরা শিশুপুত্র ইসমাঈল -এর পানির জন্য ছোটাছুটি করেছিলেন। ঠিক এ জায়গাতেই হজ্জ ও উমরা পালনকারীদেরকে দৌড়াতে হয়। শাব্দিক অর্থে দৌড়ানো হলেও পারিভাষিক অর্থে স্বাভাবিক গতিতে চলা। শুধুমাত্র দুই সবুজ পিলার দ্বারা চিহ্নিত মধ্যবর্তী স্থানে সামান্য একটু দৌড়ের গতিতে চলতে হয়। তবে মেয়েরা দৌড়াবে না।",
            "Sa'i literally means running. In the immediate vicinity of the Ka'bah, there are two small hills, one named 'Safa' and the other 'Marwah'. Between these two hills, Mother Hajar ran back and forth seeking water for her infant child Ismail. It is in this very place that pilgrims performing Hajj and Umrah must traverse. Although it literally denotes running, in technical Islamic terminology it means walking at a normal pace. Only within the area designated by the two green pillars is one required to walk with a slight jogging pace. However, women do not jog."
        ));

        // Card 2: সাই এর  হুকুম কী / What is the Ruling of Sa'i?
        cards.add(new HajjArticleCardItem(
            2,
            "সাই এর  হুকুম কী",
            "What is the Ruling of Sa'i?",
            "সাঈর কাজটি ওয়াজিব। তবে কেউ কেউ এটা রুকন অর্থাৎ ফরয বলেছেন।",
            "The act of Sa'i is Wajib (obligatory). However, some scholars have held it to be a Rukn (fundamental pillar, i.e., Farz)."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getHajjPreparationArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();

        // Card 1: মানসিক প্রস্তুতি / Mental & Spiritual Preparation
        cards.add(new HajjArticleCardItem(
            1,
            "মানসিক প্রস্তুতি",
            "Mental & Spiritual Preparation",
            "প্রথমে আপনার নিয়ত পরিশুদ্ধ করুন। কেননা নিয়তের ওপরই আমল নির্ভরশীল। হাদিসে এসেছে, ‘নিশ্চয়ই নিয়তের ওপর আমল নির্ভরশীল\n" +
            "<b>[বোখারি - ১]</b>\n\n" +
            "তাই লোক-দেখানো, হজ্জ করলে সমাজে মান-মর্যাদা বাড়বে, নামের সাথে আলহাজ্ব লেখা যাবে, নির্বাচনি লড়াইয়ে জনতাকে অধিক পরিমাণে প্রভাবিত করা যাবে ইত্যাদি ভাবনা থেকে নিজেকে পবিত্র করুন। এসব মনোবৃত্তিকে ‘রিয়া’ বলা হয়। রিয়া মারাত্মক অন্যায় যাকে হাদিসে ছোট শিরক বলা হয়েছে।\n" +
            "<b>[আহমদ - ২২৫২৪]</b>\n\n" +
            "ছোট শিরক বুকে ধারণ করে হজ্জ করলে হজ্জ কবুল হবে না কথাটি ভালোভাবে স্মরণ রাখুন। তাই রিয়া থেকে মুক্ত থাকুন ও আল্লাহর কাছে প্রার্থনা করুন তিনি যেন রিয়া-মুক্ত হজ্জ পালনের তাওফিক দান করেন। রাসূলুল্লাহ (ﷺ)নিজেও এরূপ প্রার্থনা করতেন। এক বর্ণনায় এসেছে, রাসূলুল্লাহ (ﷺ)আল্লাহর কাছে প্রার্থনা করে বলেছেন-\n\n" +
            "<big><b>اللهم حجة لا رياء فيها ولا سمعة</b></big>\n\n" +
            "-হে আল্লাহ! এমন হজ্জের তাওফিক দাও যা হবে রিয়া ও সুনাম কুড়ানোর মানসিকতা হতে মুক্ত।\n" +
            "<b>[ইবনে মাজাহ- ৮৯০]</b>\n\n" +
            "যে দিন থেকে হজ্জ পালনের নিয়ত করেছেন সেদিন থেকেই মনে করবেন যে আপনার জীবনের নতুন অধ্যায় শুরু হয়েছে। ইতোপূর্বে যদি আপনি আল্লাহর হক নষ্ট করে থাকেন, সালাত, সিয়াম যাকাত আদায় ইত্যাদির কোনোটিতে অবজ্ঞা-অনীহা-অমনোযোগ দেখিয়ে থাকেন তাহলে ক্ষমা চেয়ে আল্লাহর কাছে ফিরে আসুন। হক্কুল্লাহ বিষয়ে সকল জানা-অজানা গুনাহ-পাপ থেকে মুক্তি কামনা করে আল্লাহর দরবারে আহাজারি করুন। কাঁদুন। মুক্তিকামনা করুন হৃদয় উজাড় করে, আল্লাহ তালার সীমাহীন রহমত ও ক্ষমাশীল হওয়ার কথা খেয়াল রেখে।\n\n" +
            "এখন থেকে হক্কুল্লাহ বা আল্লাহর অধিকারের আওতাভুক্ত প্রতিটি বিষয়ই অত্যন্ত যত্নের সঙ্গে আদায় করুন। বিগত পাপ-অন্যায়ের জন্য তাওবা করুন। তাওবার নিয়ম হল-\n" +
            "ক. সকল প্রকার গুনাহ-পাপ থেকে ফিরে আসা, ও তা সম্পূর্ণরূপে পরিত্যাগ করা।\n" +
            "খ. পূর্বের সকল পাপ-অন্যায়ের প্রতি অনুশোচনা ব্যক্ত করা।\n" +
            "গ. এমন অপরাধে ভবিষ্যতে আর কখনো জড়াবেন না এমর্মে দৃঢ় প্রতিজ্ঞ হওয়া।\n\n" +
            "হজ্জ পালন অবস্থায় ঝগড়া-বিবাদ, বাকবিতন্ডা নিষেধতাই পূর্ব থেকেই আপনার মধ্যে যাতে সহিষ্ণু মেজাজ গড়ে উঠে সে ব্যাপারে মানসিকভাবে।\n" +
            "<b>[সূরা বাকারা - ১৯৭]</b>\n\n" +
            "আল্লাহর জিকির হজ্জের অবিচ্ছেদ্য অংশ। সূরা বাকারার ১৯৮, ২০০, ২০৩ আয়াতে হজে জিকিরের বিষয়ে উল্লেখ হয়েছে। হজ্জের তাওয়াফ-সাঈ কঙ্কর মারার বিধান আল্লাহর জিকির বা স্মরণের উদ্দেশে রাখা হয়েছে বলে হাদিসে এসেছে[5] সে হিসেবে হজ্জের পুরো সময়টা যেন আল্লাহর জিকির ও স্মরণে কাটে, আল্লাহর মেহমানদারিতে থাকা অবস্থায় আল্লাহর ধ্যান, আল্লাহর স্মরণ সদাসর্বদা নিজের হৃদয়কে আন্দোলিত করে রাখে সে জন্য শুরু থেকেই মানসিকভাবে প্রস্ত্ততি নিতে হবে ও চর্চা অনুশীলন করতে হবে। অন্যথায় হঠাৎ করে আল্লাহর জিকির ও স্মরণে নিজেকে আরোপিত করা সম্ভব নয়। এ কারণে হজ্জ পালনের সময় অধিকাংশ হাজিদেরকে জিকির থেকে গাফেল থাকতে দেখা যায়। ঘরসংসার, স্ত্রী-সন্তান, ব্যবসা-বাণিজ্য নিয়ে আড্ডা দিয়ে সময় কাটাতে দেখা যায় অনেককে। তাই এ বিষয়ে আগে থেকেই মনোযোগী হোন, ও তাসবীহ-তাহলীল অভ্যাস গড়ে তুলুন।\n\n" +
            "হজে বিভিন্ন মতাদর্শের মানুষ সমগ্র পৃথিবী থেকে এসে একত্রিত হয়। পুরুষের পাশে নারীদের সমাগম ঘটে সমানভাবে। অধিকাংশ নারী উন্মুক্ত চেহারায় চলাচল করেন, সালাত আদায় করতে আসেন। এদের অনেকেরই রয়েছে নজর-কাড়া রূপ-লাবণ্য। এ ক্ষেত্রে আপনার দৃষ্টিকে নিয়ন্ত্রণ করার জন্য পূর্ব থেকেই মানসিকভাবে প্রস্ত্ততি নিতে হবে। অন্যথায় ছোয়াবের পরিবর্তে গুনাহ করে হজ্জ থেকে ফিরে আসবেন।\n\n" +
            "স্বামী-স্ত্রী একসাথে হজ্জ করতে গেলে হজ্জের দিন গুলোতে স্বামী-স্ত্রী সুলভ মেলা-মেশা থেকে নিজেদেরকে দূরে রাখতে হয়। তাই এ বিষয়ে উভয়ে খুব কঠিন সিদ্ধান্ত নিন এবং মানসিক প্রস্ত্ততি গ্রহণ করুন। অন্যথায় গোটা হজ্জই নষ্ট হয়ে যাবে এ কথা মনে রাখবেন।",
            "First, purify your intention, for actions are judged solely by intentions. It is stated in the hadith: 'Verily, actions are judged by intentions.'\n" +
            "<b>[Bukhari - 1]</b>\n\n" +
            "Therefore, cleanse your mind from notions of ostentation (Riya), seeking worldly prestige in society, appending 'Al-Hajj' to your name, or aiming to influence the public in election campaigns through performing Hajj. These inner motives are called 'Riya'. Riya is a severe offense which hadiths describe as minor shirk (Ash-Shirk al-Asghar).\n" +
            "<b>[Ahmad - 22524]</b>\n\n" +
            "Keep firmly in mind that performing Hajj while harboring minor shirk in your heart will render the Hajj unaccepted. Stay free from ostentation and pray to Allah to grant you the divine ability to perform a sincere, ostentation-free Hajj. The Messenger of Allah (ﷺ) himself made this supplication. In one narration, the Prophet (ﷺ) prayed to Allah saying:\n\n" +
            "<big><b>اللهم حجة لا رياء فيها ولا سمعة</b></big>\n\n" +
            "'O Allah! Grant me a Hajj free of ostentation and seeking worldly fame.'\n" +
            "<b>[Ibn Majah - 890]</b>\n\n" +
            "From the very day you intend to perform Hajj, consider that a brand-new chapter of your life has begun. If previously you neglected the rights of Allah (Huququllah)—showing reluctance, negligence, or indifference toward prayers, fasting, or Zakat—turn to Allah in sincere repentance. Weep and supplicate with an open heart before the court of Allah for deliverance from all known and unknown sins, trusting in Allah's boundless mercy and forgiveness.\n\n" +
            "From now on, observe every obligation under the rights of Allah with utmost diligence. Perform sincere repentance (Tawbah) for past sins. The rules of Tawbah are:\n" +
            "a. Turning away completely from all forms of sin and abandoning them entirely.\n" +
            "b. Expressing genuine remorse and sorrow for all past wrongdoings.\n" +
            "c. Resolving firmly never to return to such offenses in the future.\n\n" +
            "Arguments, disputes, and quarrels are strictly forbidden during Hajj; hence, prepare mentally in advance to cultivate a patient, tolerant temperament.\n" +
            "<b>[Surah Al-Baqarah: 197]</b>\n\n" +
            "The remembrance of Allah (Dhikr) is an integral pillar of Hajj. Verses 198, 200, and 203 of Surah Al-Baqarah explicitly command Dhikr during Hajj. Rites such as Tawaf, Sa'i, and stoning the Jamarat were established for the remembrance of Allah. Thus, prepare mentally and practice in advance so that your entire journey is filled with divine mindfulness.\n\n" +
            "Pilgrims of diverse backgrounds gather from all corners of the world. Men and women assemble together in large crowds. Prepare mentally in advance to guard your modesty, lower your gaze, and restrain your senses, ensuring you return with immense rewards rather than spiritual burden.\n\n" +
            "When husband and wife perform Hajj together, they must abstain from marital relations during the days of Hajj while in Ihram. Both must make a firm resolve and mental preparation, keeping in mind that violating this invalidates the entire Hajj."
        ));

        // Card 2: আর্থিক প্রস্তুতি / Financial Preparation
        cards.add(new HajjArticleCardItem(
            2,
            "আর্থিক প্রস্তুতি",
            "Financial Preparation",
            "• অবৈধ পন্থায় উপার্জিত অর্থে হজ্জ করতে গেলে তা আল্লাহর কাছে কবুল হয় না। এ ধরনের ব্যক্তি ‘লাববাইক’ বললে আল্লাহ তার লাববাইক প্রত্যাখ্যান করেন। তিনি বলেন, তোমার কোনো লাববাইক নেই, তোমার জন্য সৌভাগ্য বার্তাও নেই। তোমার পাথেয় হারাম, তোমার অর্থ-কড়ি হারাম, তোমার হজ্জ গায়ের-মাবরুর, অগ্রহণযোগ্য। সে হিসেবে হজ্জের প্রাথমিক প্রস্ত্ততিই হবে হালাল রুজি-রোজগারের মাধ্যমে নিজের ও পরিজনের প্রয়োজন মেটানো ও সম্পূর্ণ হালাল রিজিক-সম্পদ থেকে পাই-পাই করে একত্রিত করা। যদি হালাল রিজিক উপার্জন করে হজে যাওয়ার মতো পয়সা জোগাড় করতে না পারেন তবে আপনার ওপর হজ্জ ফরজ হবে না। হজে আপনাকে যেতেই হবে, কথা এ রকম নয়। বরং ঘরসংসারের জরুরি প্রয়োজন মিটিয়ে হজে যাওয়ার খরচা হাতে আসলে তবেই কেবল হজ্জ ফরজ হয়। তাই কখনো হারাম পয়সায় হজ্জ করার পরিকল্পনা করবেন না। যদি এমন হয় যে আপনার সমগ্র সম্পদই হারাম, তাহলে আপনি তাওবা করুন। হারাম পথ বর্জন করে হালাল পথে সম্পদ উপার্জন শুরু করুন। আর কোনো দিন হারাম পথে যাবেন না বলে প্রতিজ্ঞা করুন। এক পর্যায়ে যখন প্রয়োজনীয় হালাল পয়সা জোগাড় হবে কেবল তখনই হজ্জ করার নিয়ত করুন।\n\n" +
            "• আপনার কোনো ঋণ থেকে থাকলে হজ্জ করার পূর্বেই তা পরিশোধ করে দিন। তবে আপনি যদি বড়ো ব্যবসায়ী হন, ঋণ করা যার নিত্যদিনের অভ্যাস বা প্রয়োজন, তাহলে আপনার গোটা ঋণের ব্যাপারে একটা আলাদা অসিয়ত নামা তৈরি করুন। আপনার ওয়ারিশ বা উত্তরাধিকার যারা হবেন তাদেরকে এ বিষয়ে দায়িত্ব অর্পণ কঅর্পণ করে যান।\n\n" +
            "• ব্যালটি বা ননব্যালটি উভয় ক্ষেত্রে যে পরিমাণ টাকা আপনাকে চার্জ করা হয় তার থেকেও বিশ-ত্রিশ হাজার টাকা অতিরিক্ত সঙ্গে নেয়ার চেষ্টা করবেন। পারলে আরো বেশি নেবেন। এ পয়সা প্রয়োজনের সময় ব্যয় করা- যেমন কোনো ভুলের কারণে ক্ষতিপূরণ হিসেবে দম ওয়াজিব হয়ে যাওয়া ব্যতীতও সহযাত্রী হাজিদের আপ্যায়ন করা, অভাবী হাজিদেরকে সাহায্য করা ইত্যাদির ক্ষেত্রে ব্যয় করবেন। এমনকি ক্ষুধা-পিপাসা পেলে কার্পণ্য না করে প্রয়োজনীয় খাবার গ্রহণ ইত্যাদির জন্য আপনাকে অতিরিক্ত অর্থ হাতে রাখতে হবে। তাছাড়া আত্মীয় স্বজনদের জন্য হাদিয়া তোহফা ক্রয় করাও আপনার কাছে একটি প্রয়োজন বলে মনে হতে পারে সে জন্যও আপনি অতিরিক্ত পয়যও আপনি অতিরিক্ত পয়সা সঙ্গে নিতে পারেন।\n\n" +
            "• পয়সাকড়ি নিরাপদ স্থানে হেফাজত করবেন। কোমরের বেল্টে একসাথে সব পয়সা রাখবেন না। আপনার ব্যাগে অথবা-বিশ্বস্ত হলে- হোটেলের মালিক অথবা মুয়াল্লিমের অফিসে রিসিপ্ট নিয়ে টাকা জমা রাখতে পারেন। মিনা ও আরাফাতেও বেশি টাকা সঙ্গে নিয়ে যাবেন না। কেননা হজ্জের নাম ধরে কেউ কেউ মানুষের ভিড়ে টাকা হাতিয়ে নেয়ার ধান্দায় থাকে। তাদের খপ্পর থেকে পয়সাকড়ি হেফাজত করুন।।",
            "• Performing Hajj using funds earned through unlawful (Haram) avenues is never accepted by Allah. When such a person proclaims 'Labbayk', Allah rejects the declaration, saying: 'There is no Labbayk for you, nor any auspicious glad tidings. Your provision is unlawful, your money is unlawful, and your Hajj is non-Mabrur and invalid.' Thus, the primary prerequisite for Hajj is earning purely through lawful livelihood, fulfilling all household obligations, and accumulating necessary funds from strictly halal savings. If you cannot afford Hajj through lawful earnings, Hajj is not obligatory upon you. Hajj becomes obligatory only when surplus means exist after fully providing for family necessities. Never plan Hajj using unlawful money. If one's wealth is entangled in the unlawful, make sincere repentance (Tawbah), embrace halal livelihood, and once sufficient lawful wealth is accumulated, intend Hajj.\n\n" +
            "• If you have any outstanding debts, settle them entirely prior to departing for Hajj. If you are an active businessman with continuous operational credit, write a comprehensive formal Will (Wasiyyah) detailing every debt, and entrust its settlement clearly to your designated heirs.\n\n" +
            "• Whether traveling through government or private packages, strive to carry an additional 20,000 to 30,000 taka (or foreign currency equivalent) beyond the package cost, or even more if possible. This cushion provides for unexpected needs—such as paying an obligatory Dam for accidental infractions, hosting fellow pilgrims, aiding impoverished pilgrims, ensuring nourishing meals without deprivation, and purchasing gifts (Hadiyah) for loved ones.\n\n" +
            "• Keep your money in secure locations. Do not store all cash together in your waist pouch. Keep reserves in locked luggage or with trusted hotel/Moallim lockers against receipts. Avoid carrying large cash sums to crowded areas in Mina and Arafat to prevent loss or theft."
        ));

        return cards;
    }

    public static List<HajjArticleCardItem> getFarewellTawafArticleCards() {
        List<HajjArticleCardItem> cards = new ArrayList<>();
        List<com.devflux.deenone.features.hajj.model.HajjHistoryCardItem> tawafCards = HajjTawafContentRepository.getTawafCards();
        for (com.devflux.deenone.features.hajj.model.HajjHistoryCardItem item : tawafCards) {
            cards.add(new HajjArticleCardItem(
                item.getId(),
                item.getTitleBn(),
                item.getTitleEn(),
                item.getFullContentBn(),
                item.getFullContentEn()
            ));
        }
        return cards;
    }

}
