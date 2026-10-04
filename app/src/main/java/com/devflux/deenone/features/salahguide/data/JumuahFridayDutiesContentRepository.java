package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর দিনে করণীয় (Sunnah Duties on Friday).
 * 100% verbatim text matching user provided text and authentic Islamic sources.
 */
public class JumuahFridayDutiesContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর দিনে করণীয়
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর দিনে করণীয়",
            "Sunnah Duties on Friday",
            "জুমআর ফজরের প্রথম রাকআতে সূরা সাজদাহ এবং দ্বিতীয় রাকআতে সূরা দাহ্র (ইনসান) পাঠ করা। উভয় সূরা প্রথম থেকে শেষ পর্যন্ত পাঠ করাই সুন্নত। প্রত্যেক সূরার কিছু করে অংশ পড়া সুন্নত নয়। অবশ্য অন্য সূরা পড়া দোষাবহ্ নয়। বরং কখনো কখনো ঐ দুই ...",
            "Reciting Surah As-Sajdah in the first Rak'ah and Surah Ad-Dahr (Al-Insan) in the second Rak'ah of Fajr prayer on Friday. It is Sunnah to recite both surahs completely from beginning to end...",
            "জুমআর ফজরের প্রথম রাকআতে সূরা সাজদাহ এবং দ্বিতীয় রাকআতে সূরা দাহ্র (ইনসান) পাঠ করা। উভয় সূরা প্রথম থেকে শেষ পর্যন্ত পাঠ করাই সুন্নত। প্রত্যেক সূরার কিছু করে অংশ পড়া সুন্নত নয়। অবশ্য অন্য সূরা পড়া দোষাবহ্ নয়। বরং কখনো কখনো ঐ দুই সূরা না পড়াই উচিৎ। যাতে সাধারণ মানুষ তা পড়া জরুরী মনে না করে বসে। বরং তা জরুরী মনে করে পড়া এবং কখনো কখনো না ছাড়া বা কেউতা না পড়লে আপত্তি করা বিদআত।\n\n" +
            "[মু’জামুল বিদা’ ২৮১পৃ:]",
            "Reciting Surah As-Sajdah in the first Rak'ah and Surah Ad-Dahr (Al-Insan) in the second Rak'ah of Fajr prayer on Friday. It is the established Sunnah to recite both surahs completely from beginning to end. Reciting only portions of each surah is not the Sunnah. However, reciting other surahs is not sinful. In fact, it is recommended to occasionally omit reciting these two surahs so that the general public does not consider reciting them as strictly obligatory. Rather, regarding it as mandatory, never omitting it, or objecting when someone recites other surahs is an innovation (Bid'ah).\n\n" +
            "[Mu'jam al-Bida' p. 281]"
        ));

        // 2. সকাল সকাল মসজিদে যাওয়া
        list.add(new HajjHistoryCardItem(
            2,
            "সকাল সকাল মসজিদে যাওয়া",
            "Going to the Mosque Early",
            "সকাল সকাল মসজিদে যাওয়া। আগে আগে মসজিদে গিয়ে উপস্থিত হওয়ার বিশেষ মাহাত্ম আছে। হযরত আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, আল্লাহর রসূল (ﷺ) বলেন, যে ব্যক্তি জুমআর দিন নাপাকীর গোসলের মত গোসল করল। অতঃপর প্রথম সময়ে...",
            "Going to the mosque early. Arriving early at the mosque carries immense merit. Narrated by Abu Hurairah (RA), the Messenger of Allah (ﷺ) said: Whoever performs Ghusl on Friday like the Ghusl of Janabah and then arrives in the first hour...",
            "সকাল সকাল মসজিদে যাওয়া। আগে আগে মসজিদে গিয়ে উপস্থিত হওয়ার বিশেষ মাহাত্ম আছে।\n\n" +
            "হযরত আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, আল্লাহর রসূল (ﷺ) বলেন,\n\n" +
            "যে ব্যক্তি জুমআর দিন নাপাকীর গোসলের মত গোসল করল। অতঃপর প্রথম সময়ে (মসজিদে গিয়ে) উপস্থিত হল, সে যেন এক উষ্ট্রী কুরবানী করল। যে ব্যক্তি দ্বিতীয় সময়ে উপস্থিত হল, সে যেন একটি গাভী কুরবানী করল। যে ব্যক্তি তৃতীয় সময়ে পৌঁছল, সে যেন একটি শিং-বিশিষ্ট মেষ কুরবানী করল। যে ব্যক্তি চতুর্থ সময়ে পৌঁছল, সে যেন একটি মুরগী দান করল। আর যে ব্যক্তি পঞ্চম সময়ে পৌঁছল, সে যেন একটি ডিম দান করল। অতঃপর যখন ইমাম (খুতবাদানের জন্য) বের হয়ে যান (মিম্বরে চড়েন), তখন ফিরিশ্তাগণ (হাজরী খাতা গুটিয়ে) যিক্র (খুতবা) শুনতে উপস্থিত হন।\n\n" +
            "[মালেক, মুঅত্তা, বুখারী ৮৮১, মুসলিম, সহীহ ৮৫০, আবূদাঊদ, সুনান, ইবনে মাজাহ্, সুনান, নাসাঈ, সুনান]",
            "Going to the mosque early. Arriving early at the mosque carries profound virtue and merit.\n\n" +
            "Narrated by Abu Hurairah (RA), the Messenger of Allah (ﷺ) said:\n\n" +
            "\"Whoever takes a bath on Friday as for sexual impurity (Janabah) and then proceeds to the mosque in the first hour, it is as if he sacrificed a female camel. Whoever goes in the second hour, it is as if he sacrificed a cow. Whoever goes in the third hour, it is as if he sacrificed a horned ram. Whoever goes in the fourth hour, it is as if he offered a chicken. And whoever goes in the fifth hour, it is as if he offered an egg. When the Imam emerges to deliver the Khutbah and ascends the pulpit, the angels fold up their record scrolls and sit down to listen to the remembrance (Khutbah).\"\n\n" +
            "[Malik, Muwatta; Bukhari 881; Muslim, Sahih 850; Abu Dawud, Sunan; Ibn Majah, Sunan; Nasa'i, Sunan]"
        ));

        // 3. জুমআর জন্য প্রস্তুতিস্বরুপ দেহের দুর্গন্ধ দূর করা, সে জন্য গোসল করা, আতর ব্যবহার করা
        list.add(new HajjHistoryCardItem(
            3,
            "জুমআর জন্য প্রস্তুতিস্বরুপ দেহের দুর্গন্ধ দূর করা, সে জন্য গোসল করা, আতর ব্যবহার করা",
            "Purification, Bathing, and Using Perfume for Jumu'ah",
            "জুমআর জন্য প্রস্তুতিস্বরুপ দেহের দুর্গন্ধ দূর করা, সে জন্য গোসল করা, আতর ব্যবহার করা : মহানবী (ﷺ) বলেন, যে ব্যক্তি জুমুআহ পড়তে আসবে, সে যেন গোসল করে আসে। তিনি বলেন, যে ব্যক্তি জুমআর দিন গোসল করবে, যথাসাধ্য পবিত্রতা অর্জন করবে...",
            "Preparing for Jumu'ah by removing body odor, taking a bath, and applying perfume. The Holy Prophet (ﷺ) said: Whoever comes to Jumu'ah let him take a bath...",
            "জুমআর জন্য প্রস্তুতিস্বরুপ দেহের দুর্গন্ধ দূর করা, সে জন্য গোসল করা, আতর ব্যবহার করা :\n\n" +
            "মহানবী (ﷺ) বলেন,\n\n" +
            "যে ব্যক্তি জুমুআহ পড়তে আসবে, সে যেন গোসল করে আসে।” তিনি বলেন, “যে ব্যক্তি জুমআর দিন গোসল করবে, যথাসাধ্য পবিত্রতা অর্জন করবে, তেল ব্যবহার করবে, অথবা নিজ পরিবারের সুগন্ধি নিয়ে ব্যবহার করবে, অতঃপর (জুমআর জন্য) বের হয়ে (মসজিদে) দুই নামাযীর মাঝে ফাঁক সৃষ্টি করবে না (কাতার চিরবে না), অতঃপর যতটা তার ভাগ্যে লিখা আছে ততটা নামায পড়বে, অতঃপর ইমাম খুতবা দিলে চুপ থাকবে, সে ব্যক্তির এই জুমুআহ থেকে আগামী জুমুআহ পর্যন্ত কৃত পাপ মাফ হয়ে যাবে।\n\n" +
            "[বুখারী, মিশকাত ১৩৮১নং]\n\n" +
            "গোসল করা ওয়াজেব না হলেও ঈদ, জুমুআহ ও জামাআতের জন্য পরিচ্ছন্নতা অবলম্বন করা একটি প্রধান কর্তব্য। কোন কোন বর্ণনায়, “ধৌত করায় ও করে” বা “গোসল করায় ও করে” শব্দ এসেছে। যাতে গোসল যে তাকীদপ্রাপ্ত আমল তা স্পষ্ট হয়। অবশ্য এর অর্থে অনেকে বলেন, ঐ দিন স্ত্রী-সহ্বাস করে নিজে গোসল করে এবং স্ত্রীকেও গোসল করায়। অথবা মাথা ও দেহ্ ধৌত করে পরিপূর্ণ পরিচ্ছন্নতা অর্জন করে। যারা করে তাদের জন্য রয়েছে উক্তরুপ পুরস্কার।",
            "Preparing for Friday by removing bodily odors, taking a ritual bath (Ghusl), and applying perfume:\n\n" +
            "The Holy Prophet (ﷺ) said:\n\n" +
            "\"Whoever comes to Jumu'ah prayer, let him take a bath.\" He also said: \"Whoever takes a bath on Friday, purifies himself as much as he can, uses hair oil or applies perfume from his house, then sets out for Jumu'ah, does not separate two worshippers sitting together in the mosque, offers whatever voluntary prayers are decreed for him, and remains silent when the Imam delivers the Khutbah—all his sins committed between this Friday and the next Friday will be forgiven.\"\n\n" +
            "[Bukhari; Mishkat #1381]\n\n" +
            "Even if bathing is not strictly obligatory, adopting complete bodily cleanliness for Eid, Jumu'ah, and congregations is a paramount duty. In certain narrations, terms emphasizing thorough washing and bathing appear, demonstrating that Ghusl is a heavily emphasized Sunnah. In explaining its meaning, scholars state that one engages in marital relations with their spouse on that day so both take Ghusl, or thoroughly washes the head and body to achieve total cleanliness. Those who perform this earn the aforementioned magnificent reward."
        ));

        // 4. দাঁত ও মুখ পরিষ্কার করা
        list.add(new HajjHistoryCardItem(
            4,
            "দাঁত ও মুখ পরিষ্কার করা",
            "Cleaning Teeth and Mouth",
            "দাঁতন ব্রাশ করে দাঁত ও মুখের দুর্গন্ধ দূরীভূত করে নেওয়া জুমআর পূর্বে একটি করণীয় কর্তব্য। মহানবী (ﷺ) বলেন, প্রত্যেক সাবালকের জন্য জুমআর দিন গোসল করা, মিসওয়াক করা এবং যথাসাধ্য সুগন্ধি ব্যবহার করা কর্তব্য।",
            "Brushing teeth and removing oral odors is an essential duty before Jumu'ah. The Prophet (ﷺ) said: Taking a bath on Friday, performing Miswak, and using perfume according to one's ability is a duty upon every adult.",
            "দাঁত ও মুখ পরিষ্কার করা:\n\n" +
            "দাঁতন ব্রাশ করে দাঁত ও মুখের দুর্গন্ধ দূরীভূত করে নেওয়া জুমআর পূর্বে একটি করণীয় কর্তব্য। মহানবী (ﷺ) বলেন,\n\n" +
            "প্রত্যেক সাবালকের জন্য জুমআর দিন গোসল করা, মিসওয়াক করা এবং যথাসাধ্য সুগন্ধি ব্যবহার করা কর্তব্য।\n\n" +
            "[মুসলিম, সহীহ ৮৪৬নং]",
            "Cleaning the Teeth and Mouth:\n\n" +
            "Brushing the teeth and eliminating mouth and tooth odors is an essential preparation prior to Jumu'ah. The Holy Prophet (ﷺ) said:\n\n" +
            "\"Taking a bath on Friday, using the tooth-stick (Miswak), and applying perfume according to one's means is a duty upon every adult male.\"\n\n" +
            "[Muslim, Sahih #846]"
        ));

        // 5. সুন্দর পোশাক পরা
        list.add(new HajjHistoryCardItem(
            5,
            "সুন্দর পোশাক পরা",
            "Wearing Fine Clothing",
            "মহানবী (ﷺ) বলেন, যে ব্যক্তি জুমআর দিন গোসল করে, খোশবূ থাকলে তা ব্যবহার করে এবং তার সবচেয়ে সুন্দর পোশাকটি পরিধান করে, অতঃপর স্থিরতার সাথে মসজিদে আসে, অতঃপর ইচ্ছামত নামায পড়ে এবং কাউকে কষ্ট দেয় না...",
            "The Prophet (ﷺ) said: Whoever takes a bath on Friday, applies perfume if available, wears his finest garments, arrives at the mosque with serenity, offers voluntary prayer, causes no harm to anyone...",
            "সুন্দর পোশাক পরা :\n\n" +
            "মহানবী (ﷺ) বলেন,\n\n" +
            "যে ব্যক্তি জুমআর দিন গোসল করে, খোশবূ থাকলে তা ব্যবহার করে এবং তার সবচেয়ে সুন্দর পোশাকটি পরিধান করে, অতঃপর স্থিরতার সাথে মসজিদে আসে, অতঃপর ইচ্ছামত নামায পড়ে এবং কাউকে কষ্ট দেয় না, অতঃপর ইমাম বের হলে নামায শেষ হওয়া পর্যন্ত নিশ্চুপ থাকে, সে ব্যক্তির এ কাজ দুই জুমআর মাঝে কৃত গুনাহর কাফফারা হয়ে যায়।\n\n" +
            "[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান,হাকেম, মুস্তাদরাক, ইবনে খুযাইমাহ্, সহীহ, মিশকাত ১৩৮৭নং]\n\n" +
            "জুমআর জন্য সাধারণ আটপৌরে পোশাক বা কাজের কাপড় ছাড়া পৃথক তোলা পোশাক ও কাপড় পরা বাঞ্ছনীয়। যেহেতু জুমআর দিন মুসলিমদের সমাবেশের দিন। আর এ দিনে সাজসজ্জা ও সুগন্ধি ব্যবহার করা বাঞ্ছনীয়। যাতে অপরের কাছে কেউ ঘৃণার পাত্র না হয়ে যায়। অথবা তার অপরিচ্ছন্নতায় কেউ কষ্ট না পায়। একদা খুতবার মাঝে মহানবী (ﷺ) বলেন,\n\n" +
            "তোমাদের মধ্যে যাদের সামথ্য আছে তাদের পরিশ্রমের কাপড় ছাড়া জুমআর জন্য অন্য এক জোড়া কাপড় থাকলে কি অসুবিধা আছে?\n\n" +
            "[আবূদাঊদ, সুনান, ইবনে মাজাহ্, সুনান ১০৯৫-১০৯৬নং]",
            "Wearing Fine Clothing:\n\n" +
            "The Holy Prophet (ﷺ) said:\n\n" +
            "\"Whoever takes a bath on Friday, uses fragrance if available, puts on his best clothes, walks to the mosque with calmness, offers what voluntary prayers he desires without causing inconvenience to anyone, and remains silent from the moment the Imam emerges until the prayer concludes—this act becomes an expiation for sins committed between the two Fridays.\"\n\n" +
            "[Ahmad, Musnad; Abu Dawud, Sunan; Hakim, Mustadrak; Ibn Khuzaymah, Sahih; Mishkat #1387]\n\n" +
            "It is highly recommended to wear a special, clean set of clothes for Jumu'ah distinct from daily working attire. Since Friday is the grand gathering day for Muslims, adornment, proper grooming, and fragrance are desirable so that no one becomes unpleasant to others or causes distress through lack of cleanliness. Once during the Friday Khutbah, the Prophet (ﷺ) remarked:\n\n" +
            "\"What harm is there if any of you who has the means possesses a pair of garments for Jumu'ah besides his everyday work clothes?\"\n\n" +
            "[Abu Dawud, Sunan; Ibn Majah, Sunan #1095-1096]"
        ));

        // 6. পায়ে হেঁটে মসজিদে যাওয়া
        list.add(new HajjHistoryCardItem(
            6,
            "পায়ে হেঁটে মসজিদে যাওয়া",
            "Walking to the Mosque on Foot",
            "এর জন্য মর্যাদাও আছে পৃথক। আল্লাহর রসূল (ﷺ) বলেন, যে ব্যক্তি জুমআর দিন (মাথা) ধৌত করে ও যথা নিয়মে গোসল করে, সকাল-সকাল ও আগে-আগে (মসজিদে যাওয়ার জন্য) প্রস্তুত হয়, সওয়ার না হয়ে পায়ে হেঁটে (মসজিদে) যায়...",
            "Walking on foot carries distinct spiritual merit. The Messenger of Allah (ﷺ) said: Whoever washes his head and takes a full bath on Friday, prepares early and walks on foot without riding a mount...",
            "পায়ে হেঁটে মসজিদে যাওয়া:\n\n" +
            "এর জন্য মর্যাদাও আছে পৃথক। আল্লাহর রসূল (ﷺ) বলেন,\n\n" +
            "যে ব্যক্তি জুমআর দিন (মাথা) ধৌত করে ও যথা নিয়মে গোসল করে, সকাল-সকাল ও আগে-আগে (মসজিদে যাওয়ার জন্য) প্রস্তুত হয়, সওয়ার না হয়ে পায়ে হেঁটে (মসজিদে) যায়, ইমামের কাছাকাছি বসে মনোযোগ সহকারে (খোতবা) শ্রবণ করে, এবং কোন অসার ক্রিয়া-কলাপ করে না, সে ব্যক্তির প্রত্যেক পদক্ষেপের বিনিময়ে এক বৎসরের নেক আমল ও তার (সারা বছরের) রোযা ও নামাযের সওয়াব লাভ হয়।\n\n" +
            "[আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ্), ইবনে খুযাইমাহ্, সহীহ, ইবনে হিব্বান, সহীহ,হাকেম, মুস্তাদরাক, সহিহ তারগিব ৬৮৭ নং]\n\n" +
            "প্রকাশ থাকে যে, যে ব্যক্তি গাড়ি করে জুমুআহ পড়তে আসে, তার এ সওয়াব লাভ হয় না। বলা বাহুল্য, যে বাসা থেকে ১০০ কদম পায়ে হেঁটে জামে মসজিদে পৌঁছবে, তার আমল-নামায় ১০০ বছরের রোযা-নামাযের সওয়াব লিপিবদ্ধ করা হবে; যাতে একটি গোনাহও থাকবে না। আর তা এখানেই শেষ নয়। এইভাবে সে প্রতি মাসে প্রায় ৪০০ থেকে ৫০০ এবং প্রতি বছরে প্রায় ৫২০০ বছরের নামায-রোযার সওয়াব অর্জন করবে ইনশাআল্লাহ। আর এ হল মুসলিম বান্দাদের প্রতি মহান আল্লাহর বিশেষ অনুগ্রহ।\n\n" +
            "(ذلِكَ فَضْلُ اللهِ يُؤْتِيْهِ مَنْ يَّشَاءُ، وَاللهُ ذُو الْفَضْلِ الْعَظِيْم)",
            "Walking to the Mosque on Foot:\n\n" +
            "Walking carries extraordinary, unique spiritual rewards. The Messenger of Allah (ﷺ) said:\n\n" +
            "\"Whoever washes his head and takes a bath on Friday, sets out early for the mosque, walks on foot without riding a vehicle, sits close to the Imam, listens attentively to the Khutbah, and commits no vain action—for every single step he takes, he earns the reward of a whole year's righteous deeds, including a full year of fasting and standing in prayer.\"\n\n" +
            "[Abu Dawud, Tirmidhi, Nasa'i, Ibn Majah, Ibn Khuzaymah, Ibn Hibban, Hakim, Sahih at-Targhib #687]\n\n" +
            "It should be noted that one who travels to Jumu'ah by car or vehicle does not attain this specific step-by-step reward. Needless to say, a person who walks 100 steps from home to the central mosque has the reward of 100 years of fasting and prayer recorded in their book of deeds without any defect. Furthermore, over a month this amounts to approximately 400 to 500 years, and over a year nearly 5,200 years of prayer and fasting reward, Insha'Allah. This is indeed the immense bounty of Allah upon His believing servants:\n\n" +
            "\"That is the bounty of Allah which He gives to whom He wills, and Allah is the possessor of great bounty.\""
        ));

        // 7. সূরা কাহ্ফ পাঠ
        list.add(new HajjHistoryCardItem(
            7,
            "সূরা কাহ্ফ পাঠ",
            "Reciting Surah Al-Kahf",
            "হযরত আবু সাঈদ খুদরী (রাঃ) হতে বর্ণিত, নবী (ﷺ) বলেন, যে ব্যক্তি জুমআর দিন সূরা কাহ্ফ পাঠ করবে তার জন্য দুই জুমআর মধ্যবর্তীকাল জ্যোতির্ময় হবে। [নাসাঈ, সুনান, বায়হাকী,হাকেম, মুস্তাদরাক, সহিহ তারগিব ৭৩৫ নং]...",
            "Narrated by Abu Sa'id al-Khudri (RA), the Prophet (ﷺ) said: Whoever recites Surah Al-Kahf on Friday, a light will illuminate for him between the two Fridays...",
            "সূরা কাহ্ফ পাঠ:\n\n" +
            "হযরত আবু সাঈদ খুদরী (রাঃ) হতে বর্ণিত, নবী (ﷺ) বলেন,\n\n" +
            "যে ব্যক্তি জুমআর দিন সূরা কাহ্ফ পাঠ করবে তার জন্য দুই জুমআর মধ্যবর্তীকাল জ্যোতির্ময় হবে।\n\n" +
            "[নাসাঈ, সুনান, বায়হাকী,হাকেম, মুস্তাদরাক, সহিহ তারগিব ৭৩৫ নং]\n\n" +
            "অন্য বর্ণনায় আছে,\n\n" +
            "যে ব্যক্তি জুমআর দিন সূরা কাহ্ফ পাঠ করবে তার জন্য তার ও কা’বা শরীফের মধ্যবর্তী জ্যোতির্ময় হবে।\n\n" +
            "[বায়হাকী, শুআবুল ঈমান, জামে ৬৪৭১নং]",
            "Reciting Surah Al-Kahf:\n\n" +
            "Narrated by Abu Sa'id al-Khudri (RA), the Holy Prophet (ﷺ) said:\n\n" +
            "\"Whoever recites Surah Al-Kahf on the day of Friday, a light will shine for him between the two Fridays.\"\n\n" +
            "[Nasa'i, Sunan; Bayhaqi; Hakim, Mustadrak; Sahih at-Targhib #735]\n\n" +
            "In another narration:\n\n" +
            "\"Whoever recites Surah Al-Kahf on the day of Friday, a light will shine for him stretching between him and the Sacred Ka'bah.\"\n\n" +
            "[Bayhaqi, Shu'ab al-Iman, Jami #6471]"
        ));

        // 8. বেশী বেশী দরুদ পাঠ
        list.add(new HajjHistoryCardItem(
            8,
            "বেশী বেশী দরুদ পাঠ",
            "Sending Abundant Durood upon the Prophet (ﷺ)",
            "জুমআর রাতে (বৃহ্স্পতিবার দিবাগত রাতে) ও (জুমআর) দিনে প্রিয়তম হাবীব মহানবী (ﷺ)-এর শানে অধিকাধিক দরুদ পাঠ করা কর্তব্য। মহানবী (ﷺ) বলেন, তোমাদের সর্বশ্রেষ্ঠ দিন হল, জুমআর দিন। এই দিনে তোমরা আমার প্রতি দরুদ পাঠ কর...",
            "Sending abundant blessings upon the Prophet (ﷺ) on Friday eve and day is a recommended duty. The Prophet (ﷺ) said: Among your most virtuous days is Friday, so send abundant blessings upon me...",
            "বেশী বেশী দরুদ পাঠ:\n\n" +
            "জুমআর রাতে (বৃহ্স্পতিবার দিবাগত রাতে) ও (জুমআর) দিনে প্রিয়তম হাবীব মহানবী (ﷺ)-এর শানে অধিকাধিক দরুদ পাঠ করা কর্তব্য। মহানবী (ﷺ) বলেন,\n\n" +
            "তোমাদের সর্বশ্রেষ্ঠ দিন হল, জুমআর দিন। এই দিনে তোমরা আমার প্রতি দরুদ পাঠ কর। যেহেতু তোমাদের দরুদ আমার উপর পেশ করা হয়ে থাকে।\n\n" +
            "[আবূদাঊদ, সুনান ১৫৩১নং]\n\n" +
            "তিনি আরো বলেন,\n\n" +
            "জুমআর রাতে ও দিনে তোমরা আমার উপর বেশী বেশী দরুদ পাঠ কর। আর যে ব্যক্তি আমার উপর একবার দরুদ পাঠ করবে, সে ব্যক্তির উপর আল্লাহ ১০ বার রহ্মত বর্ষণ করবেন।\n\n" +
            "[বায়হাকী, সিলসিলাহ সহীহাহ, আলবানী ১৪০৭নং]",
            "Sending Abundant Blessings (Durood):\n\n" +
            "Invoking abundant blessings and peace upon the beloved Prophet (ﷺ) on Friday night (the eve of Thursday night) and throughout the day of Friday is a cherished duty. The Holy Prophet (ﷺ) said:\n\n" +
            "\"The best of your days is the day of Friday. On this day, send abundant blessings upon me, for your blessings are presented directly to me.\"\n\n" +
            "[Abu Dawud, Sunan #1531]\n\n" +
            "He also said:\n\n" +
            "\"Send abundant blessings upon me during Friday night and the day of Friday. For whoever sends blessings upon me once, Allah sends ten blessings of mercy upon him.\"\n\n" +
            "[Bayhaqi, Silsilah Sahihah, Albani #1407]"
        ));

        return list;
    }
}
