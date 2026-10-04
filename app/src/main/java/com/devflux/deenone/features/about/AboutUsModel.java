package com.devflux.deenone.features.about;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AboutUsModel implements Serializable {

    public static class AboutInfo implements Serializable {
        public String appName = "DeenOne";
        public String taglineBn = "আপনার দ্বীনি জীবনের বিশ্বস্ত নিত্যসঙ্গী";
        public String taglineEn = "Your Trusted Companion for Islamic Life";
        public String appVersion = "v2.0.0";
        public String versionBadgeBn = "সর্বশেষ সংস্করণ";
        public String versionBadgeEn = "Latest Version";
        public String missionTitleBn = "আমাদের লক্ষ্য";
        public String missionTitleEn = "Our Mission";
        public String missionDescBn = "দ্বীনওয়ান (DeenOne) হলো একটি সর্বাধুনিক ও শতভাগ প্রামাণিক ইসলামিক জীবনধারা ট্র্যাকিং প্ল্যাটফর্ম, যা মুসলিম উম্মাহকে তাদের দৈনন্দিন সালাত, ইবাদত, আমল ও আধ্যাত্মিক অনুশীলনে সাহায্য করার জন্য তৈরি করা হয়েছে।";
        public String missionDescEn = "DeenOne is a state-of-the-art authentic Islamic lifestyle platform, built to empower the Muslim Ummah in their daily prayers, worship, deeds, and spiritual elevation.";
        public String quoteTextBn = "“আমাদের উদ্দেশ্য হলো সর্বাধুনিক প্রযুক্তির মাধ্যমে উম্মাহর আধ্যাত্মিক ও দ্বীনি উন্নতিতে সহায়তা করা।”";
        public String quoteTextEn = "“Our objective is to assist the spiritual advancement of the Ummah through modern technology.”";
        public List<String> pills = new ArrayList<>();
        public String copyrightBn = "© ২০২৬ DeenOne - সর্বস্বত্ব সংরক্ষিত";
        public String copyrightEn = "© 2026 DeenOne - All rights reserved";

        public AboutInfo() {
            pills.add("আধ্যাত্মিকতা");
            pills.add("সুন্নাহ");
            pills.add("প্রযুক্তি");
            pills.add("উম্মাহ");
        }
    }

    public static class TeamMember implements Serializable {
        public int id = 1;
        public String nameBn = "রিয়াদ মনির";
        public String nameEn = "Riad Monir";
        public String roleBn = "প্রতিষ্ঠাতা ও প্রধান ডেভেলপার";
        public String roleEn = "Founder & Lead Developer";
        public String avatarUrl = "";
        public String githubUrl = "https://github.com/riadmonir";
        public String linkedinUrl = "https://linkedin.com/in/riadmonir";
        public String websiteUrl = "https://deenone.top";
        public String email = "support@deenone.top";
        public int displayOrder = 1;
        public boolean isActive = true;

        public TeamMember() {}

        public TeamMember(int id, String nameBn, String nameEn, String roleBn, String roleEn,
                          String avatarUrl, String githubUrl, String linkedinUrl, String websiteUrl,
                          String email, int displayOrder, boolean isActive) {
            this.id = id;
            this.nameBn = nameBn;
            this.nameEn = nameEn;
            this.roleBn = roleBn;
            this.roleEn = roleEn;
            this.avatarUrl = avatarUrl;
            this.githubUrl = githubUrl;
            this.linkedinUrl = linkedinUrl;
            this.websiteUrl = websiteUrl;
            this.email = email;
            this.displayOrder = displayOrder;
            this.isActive = isActive;
        }
    }
}
