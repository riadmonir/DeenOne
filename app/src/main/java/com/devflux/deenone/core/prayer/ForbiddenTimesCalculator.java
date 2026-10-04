package com.devflux.deenone.core.prayer;

import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForbiddenTimesCalculator {

    public enum RestrictionLevel {
        STRICTLY_PROHIBITED, // সম্পূর্ণ নিষিদ্ধ ৩টি সময় (হারাম / মাকরূহে তাহরীমী)
        MAKRUH_NAFL,         // নফল নামাজের জন্য মাকরূহ (কাজা নামাজ আদায় করা যায়)
        PERMISSIBLE          // নামাজ আদায়ের উন্মুক্ত ও উত্তম সময়
    }

    public static class ForbiddenSlot {
        public String title;
        public String periodName;
        public long startMillis;
        public long endMillis;
        public String timeRangeStr;
        public RestrictionLevel level;
        public String prohibitionDetails;
        public String permittedDetails;
        public String reason;
        public String hadithReference;
        public String madhhabNotes;

        public boolean isCurrent(long now) {
            return now >= startMillis && now <= endMillis;
        }
    }

    public static class ForbiddenStatus {
        public boolean isCurrentlyRestricted;
        public RestrictionLevel currentLevel;
        public String currentPeriodTitle;
        public String currentRangeStr;
        public String timeRemainingStr;
        public int progressPercent;
        public String description;
        public List<ForbiddenSlot> allSlots = new ArrayList<>();
    }

    public static ForbiddenStatus calculateForbiddenStatus(PrayerCalculator.PrayerTimesResult prayerTimes, long nowMillis) {
        ForbiddenStatus status = new ForbiddenStatus();
        if (prayerTimes == null) return status;

        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        // 1. Sunrise Strictly Prohibited (সূর্যোদয়কালীন নিষিদ্ধ সময়: সূর্যোদয়ের পর ১৫ মিনিট)
        ForbiddenSlot sunriseSlot = new ForbiddenSlot();
        sunriseSlot.title = "সূর্যোদয়কালীন নিষিদ্ধ সময়";
        sunriseSlot.periodName = "সূর্যোদয় (Sunrise)";
        sunriseSlot.startMillis = prayerTimes.sunriseMillis;
        sunriseSlot.endMillis = prayerTimes.sunriseMillis + (15 * 60 * 1000);
        sunriseSlot.timeRangeStr = timeFmt.format(new Date(sunriseSlot.startMillis)) + " - " + timeFmt.format(new Date(sunriseSlot.endMillis));
        sunriseSlot.level = RestrictionLevel.STRICTLY_PROHIBITED;
        sunriseSlot.prohibitionDetails = "সকল প্রকার ফরজ, কাজা, নফল, জানাজার নামাজ ও তিলাওয়াতে সিজদা সম্পূর্ণ নিষিদ্ধ।";
        sunriseSlot.permittedDetails = "কুরআন তিলাওয়াত, জিকির, তাসবিহ ও দোয়া পাঠ করা জায়েজ।";
        sunriseSlot.reason = "সূর্যোদয়কালে সূর্য শয়তানের দুই শিংয়ের মধ্য দিয়ে উদিত হয় এবং বিধর্মীরা সূর্যপূজা করে থাকে।";
        sunriseSlot.hadithReference = "সহীহ মুসলিম: ৮৩১ (হযরত উকবা ইবনে আমের রা.), সহীহ বুখারী: ৫৮৬";
        sunriseSlot.madhhabNotes = "হানাফী, শাফেয়ী, মালেকী ও হাম্বলী—সকল মাযহাবে এ সময়ে সাধারণ নামাজ আদায় করা সম্পূর্ণ নিষিদ্ধ (মাকরূহে তাহরীমী)।";
        status.allSlots.add(sunriseSlot);

        // 2. Solar Noon Strictly Prohibited (দ্বিপ্রহর / নিসফুন নাহার / যাওয়াল পূর্ববর্তী নিষিদ্ধ সময়: জোহরের আগের ১২ মিনিট)
        ForbiddenSlot zawalSlot = new ForbiddenSlot();
        zawalSlot.title = "দ্বিপ্রহরের নিষিদ্ধ সময় (যাওয়াল)";
        zawalSlot.periodName = "দ্বিপ্রহর (Solar Noon)";
        zawalSlot.startMillis = prayerTimes.zohrMillis - (12 * 60 * 1000);
        zawalSlot.endMillis = prayerTimes.zohrMillis;
        zawalSlot.timeRangeStr = timeFmt.format(new Date(zawalSlot.startMillis)) + " - " + timeFmt.format(new Date(zawalSlot.endMillis));
        zawalSlot.level = RestrictionLevel.STRICTLY_PROHIBITED;
        zawalSlot.prohibitionDetails = "সকল প্রকার নামাজ (ফরজ, কাজা, নফল) এবং সিজদা নিষিদ্ধ।";
        zawalSlot.permittedDetails = "জিকির-আজকার, ইস্তিগফার ও দোয়া করা উত্তম।";
        zawalSlot.reason = "এ সময়ে জাহান্নামের আগুনকে অত্যধিক উত্তপ্ত ও প্রজ্বলিত করা হয়।";
        zawalSlot.hadithReference = "সহীহ মুসলিম: ৮৩১, জামে তিরমিযী: ১০৬০";
        zawalSlot.madhhabNotes = "হানাফী মাযহাবে জুমার দিনেও এই সময় নামাজ নিষিদ্ধ। শাফেয়ী ও হাম্বলী মাযহাবে জুমার দিনে তাহিয়্যাতুল মসজিদ পড়ার অনুমতি রয়েছে।";
        status.allSlots.add(zawalSlot);

        // 3. Sunset Strictly Prohibited (সূর্যাস্তকালীন নিষিদ্ধ সময়: সূর্যাস্তের আগের ১৫ মিনিট)
        ForbiddenSlot sunsetSlot = new ForbiddenSlot();
        sunsetSlot.title = "সূর্যাস্তকালীন নিষিদ্ধ সময়";
        sunsetSlot.periodName = "সূর্যাস্ত (Sunset)";
        sunsetSlot.startMillis = prayerTimes.sunsetMillis - (15 * 60 * 1000);
        sunsetSlot.endMillis = prayerTimes.sunsetMillis;
        sunsetSlot.timeRangeStr = timeFmt.format(new Date(sunsetSlot.startMillis)) + " - " + timeFmt.format(new Date(sunsetSlot.endMillis));
        sunsetSlot.level = RestrictionLevel.STRICTLY_PROHIBITED;
        sunsetSlot.prohibitionDetails = "সকল প্রকার সাধারণ নফল ও কাজা নামাজ আদায় নিষিদ্ধ।";
        sunsetSlot.permittedDetails = "ব্যতিক্রম: আজকের আসরের ফরজ নামাজ যদি আদায় করা না হয়ে থাকে, তবে সূর্যাস্তের আগমুহূর্তেও তা পড়ে নিতে হবে (সহীহ বুখারী: ৫৫৭)।";
        sunsetSlot.reason = "সূর্য যখন অস্ত যায় তখন শয়তানের দুই শিংয়ের মাঝে অস্ত যায় এবং সূর্যপূজকরা এ সময় সিজদা করে।";
        sunsetSlot.hadithReference = "সহীহ মুসলিম: ৮৩১, সহীহ বুখারী: ৫৮৬";
        sunsetSlot.madhhabNotes = "ইচ্ছাকৃতভাবে আসরের নামাজ এই সময় পর্যন্ত বিলম্বিত করা মুনাফেকের আমল হিসেবে হাদিসে বর্ণিত (সহীহ মুসলিম: ৬২২)।";
        status.allSlots.add(sunsetSlot);

        // 4. Post-Fajr Makruh (ফজরের পর থেকে সূর্যোদয় পর্যন্ত নফলের জন্য মাকরূহ)
        ForbiddenSlot postFajrSlot = new ForbiddenSlot();
        postFajrSlot.title = "ফজর পরবর্তী মাকরূহ সময়";
        postFajrSlot.periodName = "ফজর পরবর্তী (After Fajr)";
        postFajrSlot.startMillis = prayerTimes.fajrMillis + (25 * 60 * 1000);
        postFajrSlot.endMillis = prayerTimes.sunriseMillis;
        postFajrSlot.timeRangeStr = timeFmt.format(new Date(postFajrSlot.startMillis)) + " - " + timeFmt.format(new Date(postFajrSlot.endMillis));
        postFajrSlot.level = RestrictionLevel.MAKRUH_NAFL;
        postFajrSlot.prohibitionDetails = "সাধারণ নফল নামাজ আদায় করা মাকরূহে তাহরীমী।";
        postFajrSlot.permittedDetails = "অতীতের কাজা নামাজ আদায় করা এবং সকালের আজকার পাঠ করা জায়েজ ও উত্তম।";
        postFajrSlot.reason = "রাসূলুল্লাহ (সা.) ফজরের পর থেকে সূর্য উদিত হওয়া পর্যন্ত নফল নামাজ পড়তে নিষেধ করেছেন।";
        postFajrSlot.hadithReference = "সহীহ বুখারী: ৫৮৬, সহীহ মুসলিম: ৮২৭";
        postFajrSlot.madhhabNotes = "হানাফী মাযহাবে সকল নফল নিষিদ্ধ। শাফেয়ী মাযহাবে কারণযুক্ত নফল (যেমন: তাহিয়্যাতুল ওজু) জায়েজ।";
        status.allSlots.add(postFajrSlot);

        // 5. Post-Asr Makruh (আসরের পর থেকে সূর্যাস্তের নিষিদ্ধ সময়ের পূর্ব পর্যন্ত)
        ForbiddenSlot postAsrSlot = new ForbiddenSlot();
        postAsrSlot.title = "আসর পরবর্তী মাকরূহ সময়";
        postAsrSlot.periodName = "আসর পরবর্তী (After Asr)";
        postAsrSlot.startMillis = prayerTimes.asrMillis + (20 * 60 * 1000);
        postAsrSlot.endMillis = prayerTimes.sunsetMillis - (15 * 60 * 1000);
        postAsrSlot.timeRangeStr = timeFmt.format(new Date(postAsrSlot.startMillis)) + " - " + timeFmt.format(new Date(postAsrSlot.endMillis));
        postAsrSlot.level = RestrictionLevel.MAKRUH_NAFL;
        postAsrSlot.prohibitionDetails = "সাধারণ নফল নামাজ আদায় করা মাকরূহ।";
        postAsrSlot.permittedDetails = "অতীতের কাজা নামাজ এবং সন্ধ্যার আজকার পাঠ করা জায়েজ।";
        postAsrSlot.reason = "রাসূলুল্লাহ (সা.) আসরের পর থেকে সূর্যাস্ত পর্যন্ত নফল নামাজ পড়তে নিষেধ করেছেন।";
        postAsrSlot.hadithReference = "সহীহ বুখারী: ৫৮৬, সহীহ মুসলিম: ৮২৭";
        postAsrSlot.madhhabNotes = "হানাফী মাযহাবে নফল পড়া মাকরূহ, কাজা জায়েজ। শাফেয়ী মাযহাবে কারণযুক্ত নফল জায়েজ।";
        status.allSlots.add(postAsrSlot);

        // Check if currently inside any slot
        for (ForbiddenSlot slot : status.allSlots) {
            if (slot.isCurrent(nowMillis)) {
                status.isCurrentlyRestricted = true;
                status.currentLevel = slot.level;
                status.currentPeriodTitle = slot.title;
                status.currentRangeStr = slot.timeRangeStr;

                long remainingSec = Math.max(0, (slot.endMillis - nowMillis) / 1000);
                long mins = remainingSec / 60;
                long secs = remainingSec % 60;
                if (mins > 0) {
                    status.timeRemainingStr = "সময় বাকি: " + BengaliNumberUtil.toBengali((int) mins) + " মিনিট";
                } else {
                    status.timeRemainingStr = "সময় বাকি: " + BengaliNumberUtil.toBengali((int) secs) + " সেকেন্ড";
                }

                long totalDuration = slot.endMillis - slot.startMillis;
                long elapsed = nowMillis - slot.startMillis;
                status.progressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / totalDuration));
                status.description = slot.prohibitionDetails;
                return status;
            }
        }

        // If not inside any restricted slot:
        status.isCurrentlyRestricted = false;
        status.currentLevel = RestrictionLevel.PERMISSIBLE;
        status.currentPeriodTitle = "বর্তমানে কোনো নিষিদ্ধ সময় নেই";
        status.currentRangeStr = "নামাজ আদায়ের জন্য উত্তম সময়";
        status.timeRemainingStr = "সকল প্রকার নামাজ আদায় জায়েজ";
        status.progressPercent = 100;
        status.description = "এখন ফরজ, কাজা ও নফল যেকোনো নামাজ আদায় করতে পারেন।";

        return status;
    }
}
