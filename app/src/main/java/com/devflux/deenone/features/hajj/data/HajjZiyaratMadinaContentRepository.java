package com.devflux.deenone.features.hajj.data;

import com.devflux.deenone.features.hajj.model.HajjZiyaratMadinaCardItem;

import java.util.ArrayList;
import java.util.List;

public class HajjZiyaratMadinaContentRepository {

    public static List<HajjZiyaratMadinaCardItem> getZiyaratMadinaCards() {
        List<HajjZiyaratMadinaCardItem> list = new ArrayList<>();

        // 1. মদিনা যিয়ারত
        list.add(new HajjZiyaratMadinaCardItem(
            1,
            "মদিনা যিয়ারত",
            "Significance of Visiting Madinah",
            "‘হে আল্লাহ! ইব্রাহীম মক্কাকে পবিত্র হওয়ার ঘোষ...",
            "'O Allah! Ibrahim declared Makkah sacred, and I declare Madinah sacred...'",
            "রাসুলুল্লাহ (ﷺ) দোয়া করেছেন: ‘হে আল্লাহ! ইব্রাহীম (আ.) মক্কাকে পবিত্র হওয়ার ঘোষণা দিয়েছিলেন, আর আমি মদিনার দুই প্রান্তের মধ্যবর্তী স্থানকে পবিত্র ও সম্মানিত ঘোষণা করছি।’ প্রিয় নবীজির পবিত্র শহর মদিনা মুনাওয়ারা জিয়ারত করা প্রতিটি মুসলিমের জন্য গভীর ঈমানি ভালোবাসা ও পরম সৌভাগ্যের বিষয়।",
            "The Prophet (ﷺ) supplicated: 'O Allah! Ibrahim declared Makkah sacred, and I declare sacred what is between its two lava tracts (Madinah).' Visiting the illuminated city of the beloved Prophet is an immense honor and manifestation of faith for every believer."
        ));

        // 2. মদিনার পথে রওয়ানা
        list.add(new HajjZiyaratMadinaCardItem(
            2,
            "মদিনার পথে রওয়ানা",
            "Journeying Towards Madinah",
            "মসজিদে নববি যিয়ারতের নিয়ত করে আপনি মদি...",
            "Setting the sincere intention to visit Masjid an-Nabawi and Madinah...",
            "মসজিদে নববি যিয়ারতের নিয়ত করে আপনি মদিনার উদ্দেশ্যে রওয়ানা হবেন। পথিমধ্যে বেশি বেশি দরুদ ও সালাম পাঠ করবেন। মদিনা মুনাওয়ারায় প্রবেশকালে বিনম্র ও শান্ত মনোভাব বজায় রাখবেন।",
            "One should embark on the journey to Madinah with the sincere intention of visiting Masjid an-Nabawi. Throughout the journey, recite abundant blessings and salutations (Durood & Salam) upon the Prophet (ﷺ)."
        ));

        // 3. মসজিদে নববিতে প্রবেশ
        list.add(new HajjZiyaratMadinaCardItem(
            3,
            "মসজিদে নববিতে প্রবেশ",
            "Entering the Prophet's Mosque (Masjid an-Nabawi)",
            "যে কোনো দরজা দিয়ে মসজিদে নববিতে প্রবেশ ...",
            "Entering through any convenient gate with the right foot while reciting the Dua...",
            "যে কোনো দরজা দিয়ে মসজিদে নববিতে প্রবেশ করা যায়। প্রবেশের সময় ডান পা দিয়ে প্রবেশ করে দোয়া পড়া: ‘বিসমিল্লাহি ওয়াস সালাতু ওয়াস সালামু আলা রাসুলিল্লাহ, আল্লাহুম্মাফ তাহলী আবওয়াবা রাহমাতিক।’ এরপর তাহিয়্যাতুল মসজিদ সালাত আদায় করা। সুযোগ হলে রিয়াজুল জান্নাতে সালাত ও দোয়া করা।",
            "One may enter Masjid an-Nabawi through any accessible gate. Enter with the right foot, reciting the prescribed Dua for entering mosques. Perform Tahiyyatul Masjid, and if permitted, pray and supplicate in the blessed Rawdah (Garden of Paradise)."
        ));

        // 4. রাসুলুল্লাহ (সাঃ)ও তাঁর দুই সাথির কবর যিয়ারতের আদব
        list.add(new HajjZiyaratMadinaCardItem(
            4,
            "রাসুলুল্লাহ (সাঃ)ও তাঁর দুই সাথির কবর যিয়ারতের আদব",
            "Etiquettes of Greeting the Prophet (ﷺ) and His Two Companions",
            "এরপর বাইরে চলে আসুন। কেবলামুখী হয়ে বা ক...",
            "Standing reverently before the noble Rawdah facing the graves with soft voices...",
            "রওজা মোবারকের সামনে দাঁড়িয়ে অত্যন্ত শান্ত ও বিনম্র কণ্ঠে রাসুলুল্লাহ (ﷺ)-এর প্রতি সালাম পেশ করা: ‘আসসালামু আলাইকা ইয়া রাসূলাল্লাহ’। এরপর কিছুটা ডানে সরে হযরত আবু বকর (রা.) ও হযরত উমর (রা.)-এর প্রতি সালাম পেশ করা। কোনো প্রকার চিৎকার বা ভিড় সৃষ্টি না করে আদবের সাথে প্রস্থান করা।",
            "Stand with utmost respect and tranquility before the noble resting place of the Prophet (ﷺ) and softly say: 'Assalamu 'alayka ya Rasool Allah.' Step slightly to the right to convey greetings upon Abu Bakr (RA), and then further right upon Umar (RA), departing gently without raising voices or pushing."
        ));

        // 5. রাসুলুল্লাহ (সাঃ)এর পবিত্র কবর যিয়ারতের সময় নিষিদ্ধ বিষয়সমূহ
        list.add(new HajjZiyaratMadinaCardItem(
            5,
            "রাসুলুল্লাহ (সাঃ)এর পবিত্র কবর যিয়ারতের সময় নিষিদ্ধ বিষয়সমূহ",
            "Prohibitions During Grave Visitation",
            "রাসুলুল্লাহ (ﷺ)এর পবিত্র কবর হুজরা শরীফের ...",
            "Refraining from touching the grilles, seeking needs from graves, or prostrating...",
            "রাসুলুল্লাহ (ﷺ)এর পবিত্র কবর হুজরা শরীফের দেয়াল বা গ্রিল চুম্বন করা, হাত বুলিয়ে বরকত নেওয়া, কবরের দিকে ফিরে দোয়া চাওয়া বা সিজদা করা কঠোরভাবে নিষিদ্ধ। দোয়া করতে হবে একমাত্র আল্লাহর কাছে ক্বিবলামুখী হয়ে।",
            "Touching, kissing, or rubbing the screens/grilles of the Prophet's chamber, seeking needs from the deceased, circumambulating graves, or prostrating are strictly forbidden in Islam. Supplication must be directed solely to Allah while facing the Qiblah."
        ));

        // 6. মদিনা শরীফে অন্যান্য যিয়ারতের স্থান : জান্নাতুল বাকি
        list.add(new HajjZiyaratMadinaCardItem(
            6,
            "মদিনা শরীফে অন্যান্য যিয়ারতের স্থান : জান্নাতুল বাকি",
            "Other Sacred Sites: Jannat al-Baqi Cemetery",
            "জান্নাতুল বাকি—আরবিতে বাকিউল গারকাদ—প...",
            "Jannat al-Baqi is the historic resting place of numerous noble Companions...",
            "জান্নাতুল বাকি—আরবিতে বাকিউল গারকাদ—পবিত্র মসজিদে নববির পূর্ব পাশে অবস্থিত ঐতিহাসিক কবরস্থান। এখানে উম্মাহাতুল মুমিনীন, আহলে বাইত ও হাজারো জলিলুল কদর সাহাবি শুয়ে আছেন। সেখানে গিয়ে কবরবাসীর মাগফিরাতের জন্য মাসনূন দোয়া করা সুন্নাত।",
            "Jannat al-Baqi (Baqi' al-Gharqad) is the renowned historic cemetery adjacent to Masjid an-Nabawi where the Mothers of the Believers, the Prophet's household, and thousands of noble Sahabah rest. It is a Sunnah to visit and supplicate for their forgiveness."
        ));

        // 7. মসজিদে কুবায় সালাত আদায়
        list.add(new HajjZiyaratMadinaCardItem(
            7,
            "মসজিদে কুবায় সালাত আদায়",
            "Virtues of Praying in Masjid Quba",
            "একমাত্র আল্লাহকে রাজি-খুশি করার জন্য তাকও...",
            "The first mosque founded upon righteousness, where prayer equals an Umrah...",
            "একমাত্র আল্লাহকে রাজি-খুশি করার জন্য তাকওয়ার ভিত্তিতে প্রতিষ্ঠিত ইসলামের প্রথম মসজিদ হলো মসজিদে কুবা। রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: যে ব্যক্তি নিজ ঘরে ভালোভাবে পবিত্রতা অর্জন করে মসজিদে কুবায় এসে দুই রাকাত সালাত আদায় করে, সে একটি ওমরার সমপরিমাণ সওয়াব লাভ করবে।",
            "Masjid Quba is the first mosque built in Islam, founded on piety. The Prophet (ﷺ) said: 'Whoever purifies himself in his house, then comes to Masjid Quba and offers prayer therein, will have a reward like that of an Umrah.'"
        ));

        // 8. মসজিদে কোবায় সালাত আদায়ের নিয়ম
        list.add(new HajjZiyaratMadinaCardItem(
            8,
            "মসজিদে কোবায় সালাত আদায়ের নিয়ম",
            "Etiquettes and Rules of Praying in Masjid Quba",
            "মসজিদে কোবায় গিয়ে প্রবেশের সময় ডান পা ...",
            "Entering with the right foot and offering two Rak'ahs of voluntary prayer...",
            "মসজিদে কোবায় গিয়ে প্রবেশের সময় ডান পা দিয়ে প্রবেশ করে দোয়া পাঠ করবেন। সেখানে দুই রাকাত নফল বা তাহিয়্যাতুল মসজিদ সালাত আদায় করবেন এবং আল্লাহর দরবারে নিজের ও সমগ্র উম্মাহর জন্য দোয়া করবেন।",
            "Enter Masjid Quba with the right foot reciting the mosque entry supplication. Pray at least two units (Rak'ahs) of voluntary prayer (Tahiyyatul Masjid / Nafl) and make heartfelt supplications."
        ));

        // 9. যিয়ারতে শুহাদায়ে উহুদ
        list.add(new HajjZiyaratMadinaCardItem(
            9,
            "যিয়ারতে শুহাদায়ে উহুদ",
            "Visiting the Martyrs of Uhud (Shuhada Uhud)",
            "হিজরি দ্বিতীয় সালে উহুদযুদ্ধে যারা শহীদ হয়েছে...",
            "Visiting the resting place of Sayyidush-Shuhada Hamzah (RA) and the 70 martyrs...",
            "হিজরি তৃতীয় সালে উহুদযুদ্ধে হযরত হামজা (রা.) সহ যে ৭০ জন সাহাবি শহীদ হয়েছিলেন, তাঁদের কবর যিয়ারত করা এবং তাঁদের জন্য মাগফিরাত ও রহমতের দোয়া করা অত্যন্ত ফজিলতপূর্ণ সুন্নাত। রাসুলুল্লাহ (ﷺ) প্রায়ই উহুদের শহীদদের কবর যিয়ারত করতেন।",
            "Visiting the graveyard of the 70 noble martyrs of the Battle of Uhud, led by Sayyid ash-Shuhada Hamzah (RA), is a blessed Sunnah. The Prophet (ﷺ) would regularly visit and pray for the martyrs of Uhud."
        ));

        // 10. বাড়ি প্রত্যাবর্তনের আদব প্রসঙ্গ
        list.add(new HajjZiyaratMadinaCardItem(
            10,
            "বাড়ি প্রত্যাবর্তনের আদব প্রসঙ্গ",
            "Etiquettes of Returning Home from Pilgrimage",
            "বাড়ি প্রত্যাবর্তনের সময় রাসুলুল্লাহ (ﷺ)এর সুন্ন...",
            "Sunnah etiquettes upon returning from the blessed pilgrimage journey...",
            "হজ ও যিয়ারত শেষে বাড়ি প্রত্যাবর্তনের সময় রাসুলুল্লাহ (ﷺ)এর সুন্নাত হলো পথিমধ্যে শুকরিয়া আদায় করা, সফর থেকে ফিরে এলাকার মসজিদে দুই রাকাত নফল সালাত আদায় করে ঘরে প্রবেশ করা এবং আল্লাহর দরবারে হজ কবুলের প্রার্থনা করা।",
            "Upon returning from the pilgrimage, it is Sunnah to show immense gratitude to Allah, enter the local mosque to perform two Rak'ahs before going home, and pray for the acceptance of Hajj (Hajj Mabrur)."
        ));

        // 11. এলাকাবাসীর করণীয়
        list.add(new HajjZiyaratMadinaCardItem(
            11,
            "এলাকাবাসীর করণীয়",
            "Duties of the Community Towards Returning Pilgrims",
            "ইবনে ওমর থেকে বর্ণিত এক হাদিসে এসেছে—...",
            "Welcoming returning pilgrims with warmth, greetings, and seeking their Duas...",
            "ইবনে ওমর (রা.) থেকে বর্ণিত এক হাদিসে এসেছে: যখন কোনো হাজির সাথে তোমার দেখা হবে, তখন তিনি নিজ ঘরে প্রবেশের পূর্বেই তাকে সালাম দাও, মুসাফাহা করো এবং তার কাছে তোমার মাগফিরাতের জন্য দোয়ার অনুরোধ করো; কারণ তিনি তখন নিষ্পাপ ও ক্ষমাপ্রাপ্ত অবস্থায় থাকেন।",
            "Narrated by Ibn Umar (RA): 'When you meet a pilgrim returning from Hajj, greet him, shake his hand, and ask him to pray for your forgiveness before he enters his home, for he has been forgiven.'"
        ));

        return list;
    }
}
