package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;

/**
 * Static Helper for Standard Scholarly and Legal Disclaimers (Requirements 25 & 28).
 */
public class FaraidDisclaimerHelper implements Serializable {

    public static final String GENERAL_DISCLAIMER_BN =
            "এই ফারায়েজ ক্যালকুলেটরটি শিক্ষামূলক এবং প্রাথমিক তথ্যমূলক উদ্দেশ্যে প্রস্তুত করা হয়েছে। ইসলামিক উত্তরাধিকার বণ্টনে ফিকহী সূক্ষ্মতা, চার মাযহাবের মতপার্থক্য, পারিবারিক পরিস্থিতি, ঋণ, অসিয়ত, মালিকানা সংক্রান্ত জটিলতা এবং স্থানীয় মুসলিম পারিবারিক আইনের প্রভাব থাকতে পারে। বাস্তব ক্ষেত্রে সম্পত্তি বণ্টনের পূর্বে অবশ্যই একজন বিজ্ঞ ইসলামিক ফারায়েজ বিশেষজ্ঞ মুফতি বা বিজ্ঞ আলেম এবং ক্ষেত্রবিশেষে অভিজ্ঞ আইনজীবীর সাথে পরামর্শ ও পর্যালোচনা করে নিন।";

    public static final String GENERAL_DISCLAIMER_EN =
            "This Islamic inheritance calculator is prepared for educational and informational purposes only. Practical estate distribution requires formal consultation and review by a qualified Islamic inheritance scholar and legal authorities.";

    public static final String WARNING_DISCLAIMER_BN =
            "সতর্কতা: শুধুমাত্র এই গণনার উপর ভিত্তি করে সম্পত্তি বণ্টন করবেন না। কোনো বিরোধ বা জটিল ক্ষেত্রে একজন বিজ্ঞ ফারায়েজ বিশেষজ্ঞের মাধ্যমে নিশ্চিত যাচাই গ্রহণ করুন।";

    public static final String WARNING_DISCLAIMER_EN =
            "Warning: Do not distribute the estate based solely on this calculation. Obtain formal scholarly certification prior to execution.";

    public static final String HIGH_RISK_WARNING_BN = WARNING_DISCLAIMER_BN;
    public static final String HIGH_RISK_WARNING_EN = WARNING_DISCLAIMER_EN;

    public static final String NOT_A_FATWA_BN =
            "ঘোষণা: এই অ্যাপটি শাস্ত্রীয় ফারায়েজ নিয়মের উপর ভিত্তি করে গণনা তৈরি করে এবং এটি কোনো প্রাতিষ্ঠানিক ফতোয়া নয়।";

    public static final String NOT_A_FATWA_EN =
            "Notice: This tool provides computational models based on classical Faraid rules and is NOT an official Fatwa.";

    public static final String NON_FATWA_STATEMENT = NOT_A_FATWA_EN;
}