package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: জুম'আর সময় (Time of Jumu'ah Prayer).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and Classical Fiqh references.
 */
public class JumuahPrayerTimeContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. জুম'আর সময় (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "জুম'আর সময়",
            "Time of Jumu'ah Prayer",
            "অধিকাংশ সাহাবা, তাবেঈন ও ইমামগণের নিকট জুমআর সময় যোহরের সময় একই। অর্থাৎ, সূর্য ঢলার পর থেকে নিয়ে প্রত্যেক বস্তুর ছায়া তার সমপরিমাণ হওয়া (আসরের আগে) পর্যন্ত। হযরত আনাস (রাঃ) বলেন, ‘নবী (ﷺ) জুমুআহ তখন পড়তেন, যখন সূর্য প...",
            "According to the majority of the Sahabah, Tabi'un, and Imams, the time for Friday prayer is identical to the time of Dhuhr prayer: starting from the moment the sun declines past the zenith...",
            "অধিকাংশ সাহাবা, তাবেঈন ও ইমামগণের নিকট জুমআর সময় যোহরের সময় একই। অর্থাৎ, সূর্য ঢলার পর থেকে নিয়ে প্রত্যেক বস্তুর ছায়া তার সমপরিমাণ হওয়া (আসরের আগে) পর্যন্ত।\n\n" +
            "হযরত আনাস (রাঃ) বলেন, ‘নবী (ﷺ) জুমুআহ তখন পড়তেন, যখন সূর্য পশ্চিম আকাশেঢলে যেত।’ [আহমাদ, মুসনাদ, বুখারী, আবূদাঊদ, সুনান, তিরমিযী, সুনান, বায়হাকী]\n\n" +
            "ইমাম বুখারী বলেন, ‘জুমআর সময় সূর্য ঢলার পরই শুরু হয়। হযরত উমার, আলী, নু’মান বিন বাশীর এবং আম্র বিন হুয়াইরিষ কর্তৃক এ ব্যাপারে বর্ণনা পাওয়া যায়।’\n\n" +
            "[বুখারী]\n\n" +
            "হযরত সালামাহ্ বিন আকওয়া’ (রাঃ) বলেন, ‘আমরা যখন নবী (ﷺ)-এর সাথে জুমআর নামায পড়ে ঘরে ফিরতাম, তখন দেওয়ালের কোন ছায়া থাকত না।’\n\n" +
            "[বুখারী, মুসলিম, আবূদাঊদ, সুনান]\n\n" +
            "হযরত আনাস (রাঃ) বলেন, ‘ঠান্ডা খুব বেশী হলে নবী (ﷺ) জুমআর নামায সকাল সকাল পড়তেন এবং গরম খুব বেশী হলে দেরী করে পড়তেন।’\n\n" +
            "[বুখারী]",
            "According to the majority of the Sahabah (Companions), Tabi'un (Successors), and Imams, the time for Friday (Jumu'ah) prayer is identical to the time of Dhuhr prayer: starting from the moment the sun passes its zenith (meridian) until the shadow of an object equals its length (prior to Asr).\n\n" +
            "Narrated by Anas (RA): 'The Prophet (ﷺ) used to offer the Friday prayer when the sun declined past the zenith.' [Ahmad, Musnad; Bukhari; Abu Dawud, Sunan; Tirmidhi, Sunan; Bayhaqi]\n\n" +
            "Imam Bukhari stated: 'The time of Friday prayer begins immediately after the decline of the sun past its zenith. Narrations to this effect are recorded from Umar, Ali, Nu'man ibn Bashir, and Amr ibn Hurayrith.'\n\n" +
            "[Bukhari]\n\n" +
            "Narrated by Salamah ibn al-Akwa' (RA): 'When we returned home after offering the Friday prayer with the Prophet (ﷺ), there was hardly enough shadow from the walls for us to seek shade.'\n\n" +
            "[Bukhari, Muslim, Abu Dawud, Sunan]\n\n" +
            "Narrated by Anas (RA): 'When it was extremely cold, the Prophet (ﷺ) would offer the Friday prayer early, and when it was intensely hot, he would delay it until it became cooler.'\n\n" +
            "[Bukhari]"
        ));

        return list;
    }
}
