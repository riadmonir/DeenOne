package com.devflux.deenone.core.verification;

public enum VerificationStatus {
    AUTHENTIC_SAHIH("সহীহ (Sahih / Authentic)", "সবচেয়ে নির্ভরযোগ্য ও বিশুদ্ধ সনদ", 0xFF10B981),
    MUTAWATIR("মুতাওয়াতির (Mutawatir)", "অবিচ্ছিন্ন সূত্রে বর্ণিত অকাট্য সত্য", 0xFF059669),
    HASAN("হাসান (Hasan / Good)", "গ্রহণযোগ্য ও নির্ভরযোগ্য সনদ", 0xFF34D399),
    ISLAMIC_FOUNDATION_APPROVED("ইসলামিক ফাউন্ডেশন অনুমোদিত", "সরকারি ও ইসলামিক স্কলার বোর্ড কর্তৃক সত্যায়িত", 0xFF10B981),
    VERIFIED_ASTRONOMICAL("জ্যোতির্বিজ্ঞানসম্মতভাবে যাচাইকৃত", "সঠিক ভৌগোলিক ও কোণভিত্তিক পরিমাপ", 0xFF38BDF8),
    UNDER_REVIEW("পর্যালোচনাধীন", "স্কলার প্যানেল কর্তৃক যাচাই চলছে", 0xFFFBBF24),
    REJECTED("অস্বীকৃত / প্রত্যাখ্যাত", "বিশুদ্ধ সনদের অভাব অথবা জাল বর্ণনা", 0xFFEF4444);

    public final String banglaTitle;
    public final String description;
    public final int badgeColor;

    VerificationStatus(String banglaTitle, String description, int badgeColor) {
        this.banglaTitle = banglaTitle;
        this.description = description;
        this.badgeColor = badgeColor;
    }
}
