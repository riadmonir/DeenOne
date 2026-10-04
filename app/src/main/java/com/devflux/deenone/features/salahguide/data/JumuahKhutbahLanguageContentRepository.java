package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Jumu'ah Chapter: স্থানীয় ভাষায় জুম'আর খুতবা.
 * 100% verbatim text matching user provided text and authentic Islamic sources.
 */
public class JumuahKhutbahLanguageContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. স্থানীয় ভাষায় জুম'আর খুতবা
        list.add(new HajjHistoryCardItem(
            1,
            "স্থানীয় ভাষায় জুম'আর খুতবা",
            "Jumu'ah Khutbah in Local Language",
            "জুমআর জমায়েত মুসলিমদের একটি প্রশিক্ষণ ক্ষেত্র। সাপ্তাহিক এই প্রশিক্ষণে মুসলিমের বিস্মৃত কথা স্মরণ হয়, চলার পথে অন্ধকারে আলোর দিশা পায়, সুন্দর চরিত্র ও ব্যবহার গড়তে সহায়তা পায়, ঈমান নবায়ন হয়, হৃদয় নরম হয়, মৃত্যু ও পরকালের স্মরণ হয়, ত...",
            "The Friday Jumu'ah gathering is a training ground for Muslims. Through this weekly assembly, believers are reminded of forgotten truths, find guidance in darkness, gain support in cultivating noble character...",
            "জুমআর জমায়েত মুসলিমদের একটি প্রশিক্ষণ ক্ষেত্র। সাপ্তাহিক এই প্রশিক্ষণে মুসলিমের বিস্মৃত কথা স্মরণ হয়, চলার পথে অন্ধকারে আলোর দিশা পায়, সুন্দর চরিত্র ও ব্যবহার গড়তে সহায়তা পায়, ঈমান নবায়ন হয়, হৃদয় নরম হয়, মৃত্যু ও পরকালের স্মরণ হয়, তওবা করতে অনুপ্রাণিত হয়, ভালো কাজ করতে এবং খারাপ কাজ বর্জন করতে উৎসাহ্ পায়, ইত্যাদি।\n\n" +
            "তাই খুতবার ভূমিকা আরবীতে হওয়ার পর স্থানীয় ভাষায় বাকী খুতবা পাঠ বৈধ। যেহেতু খুতবার আসল উদ্দেশ্য হল জনসাধারণকে শরীয়তের শিক্ষা ও উপদেশ দান করা। আর তা আরবীতে হলে উদ্দেশ্য বিফল হয়। সুতরাং যে খুতবা আরবীতে হত তারই ভাবার্থ স্থানীয় ভাষায় হলে মুসলিমদেরকে সপ্তাহান্তে একবার উপদেশ ও পথ নির্দেশনা দান করার মত মহান উদ্দেশ্য সাধিত হয়। পক্ষান্তরে খুতবা নামাযের মত নয়। নামাযে অন্য ভাষা বললে নামায বাতিল। কিন্তু খুতবা তা নয়। যেমন খুতবা ছেড়ে অন্য কথা বলা যায়, নামাযে তা যায় না। ইত্যাদি।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪২২-৪২৩, মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৫/৮৪]\n\n" +
            "পক্ষান্তরে খুতবার আগে স্থানীয় ভাষায় খুতবা দেওয়া বিধেয় নয়। কারণ, উপায় থাকতেও ডবল খুতবা হয়ে যায় তাতে। ডিষ্টার্ব হয় নামায, তেলাওয়াত ও যিক্ররত মুসল্লীদের।\n\n" +
            "[মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ্ ১৭/৭১-৭২]\n\n" +
            "উল্লেখ্য যে, কোন স্থানের জামাআতে খুতবা দেওয়ার মত কোন লোক না থাকার ফলে যদি খুতবা দেওয়া না হয়, তাহলে সেই জামাআতের লোক জুমুআহ না পড়ে যোহ্র পড়বে।\n\n" +
            "[ইবনে আবী শাইবা ৫২৬৯-৫২৭৬]\n\n" +
            "জ্ঞাতব্য যে, যিনি খুতবা দেবেন তাঁরই নামায পড়া জরুরী নয়। যদিও সুন্নত হল খতীবেরই ইমামতি করা।\n\n" +
            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/৪১০, ৪১৩]",
            "The Friday Jumu'ah gathering is a training ground for Muslims. Through this weekly assembly, believers are reminded of forgotten truths, find light in the darkness of life's path, receive assistance in cultivating noble character and conduct, have their faith renewed, hearts softened, are reminded of death and the Hereafter, inspired to repent, and encouraged to do good deeds and shun evil, and so forth.\n\n" +
            "Therefore, delivering the introduction of the Khutbah in Arabic and reciting the remainder of the sermon in the local language is permissible. This is because the fundamental purpose of the Khutbah is to provide Islamic education and admonition to the general public. If it is delivered entirely in Arabic to a non-Arabic congregation, this core purpose is lost. Hence, conveying the essence and meaning in the native local language accomplishes the great objective of providing weekly guidance and advice to Muslims. On the other hand, the Khutbah is not like the ritual prayer (Salah). In prayer, speaking in any language other than Arabic invalidates the prayer; however, this is not the case for the Khutbah. For instance, speaking outside the sermon is permitted when necessary, whereas it is strictly forbidden during Salah, and so on.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/422-423; Majallatul Buhuthil Islamiyyah 15/84]\n\n" +
            "Conversely, delivering a sermon in the local language prior to the official Khutbah is not prescribed. This is because doing so effectively results in a double Khutbah despite having viable alternatives, and it disturbs the worshippers engaged in voluntary prayers, Qur'anic recitation, and remembrance of Allah.\n\n" +
            "[Majallatul Buhuthil Islamiyyah 17/71-72]\n\n" +
            "It is noteworthy that if a congregation lacks anyone capable of delivering a Khutbah and consequently no Khutbah is given, the worshippers of that congregation must offer Dhuhr prayer instead of Jumu'ah.\n\n" +
            "[Ibn Abi Shaybah 5269-5276]\n\n" +
            "It is important to know that it is not obligatory for the one who delivers the Khutbah to also lead the prayer, although the established Sunnah is for the Khatib himself to lead the congregation in prayer.\n\n" +
            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/410, 413]"
        ));

        return list;
    }
}
