package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirHajbDecision;

import java.util.Map;

/**
 * Dedicated Asabah / Residue Engine (Requirement 16).
 *
 * Implements the prophetic mandate:
 * «أَلْحِقُوا الفَرَائِضَ بِأَهْلِهَا، فَمَا بَقِيَ فَهُوَ لأَوْلَى رَجُلٍ ذَكَرٍ»
 * (Sahih al-Bukhari 6732/6737; Sahih Muslim 1615a)
 */
public class FaraidAsabahEngine {

    public static class AsabahResolution {
        private final AsabahType asabahType;
        private final String primaryAsabahKey;
        private final String primaryAsabahTitleBn;
        private final String secondaryAsabahKey;
        private final String secondaryAsabahTitleBn;
        private final long primaryUnits;
        private final long secondaryUnits;
        private final long totalUnits;
        private final String shariahDalil;

        public AsabahResolution(AsabahType asabahType, String primaryAsabahKey, String primaryAsabahTitleBn,
                                 String secondaryAsabahKey, String secondaryAsabahTitleBn,
                                 long primaryUnits, long secondaryUnits, long totalUnits,
                                 String shariahDalil) {
            this.asabahType = asabahType;
            this.primaryAsabahKey = primaryAsabahKey;
            this.primaryAsabahTitleBn = primaryAsabahTitleBn;
            this.secondaryAsabahKey = secondaryAsabahKey;
            this.secondaryAsabahTitleBn = secondaryAsabahTitleBn;
            this.primaryUnits = primaryUnits;
            this.secondaryUnits = secondaryUnits;
            this.totalUnits = totalUnits;
            this.shariahDalil = shariahDalil;
        }

        public AsabahType getAsabahType() { return asabahType; }
        public boolean hasAsabah() { return asabahType != AsabahType.NONE; }
        public String getPrimaryAsabahKey() { return primaryAsabahKey; }
        public String getPrimaryAsabahTitleBn() { return primaryAsabahTitleBn; }
        public String getSecondaryAsabahKey() { return secondaryAsabahKey; }
        public String getSecondaryAsabahTitleBn() { return secondaryAsabahTitleBn; }
        public long getPrimaryUnits() { return primaryUnits; }
        public long getSecondaryUnits() { return secondaryUnits; }
        public long getTotalUnits() { return totalUnits; }
        public String getShariahDalil() { return shariahDalil; }
    }

    public static AsabahResolution resolveClosestAsabah(FaraidInput input, Map<String, HeirHajbDecision> decisions) {
        if (input == null || decisions == null) {
            return new AsabahResolution(AsabahType.NONE, "", "", "", "", 0, 0, 0, "");
        }

        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();
        int fullBrothers = input.getFullBrotherCount();
        int fullSisters = input.getFullSisterCount();
        int consBrothers = input.getConsanguineBrotherCount();
        int consSisters = input.getConsanguineSisterCount();
        int nephews = input.getNephewCount();
        int uncles = input.getPaternalUncleCount();
        boolean applyBdLaw = input.isApplyBangladeshLaw1961();

        // 1. Sons (with or without daughters / orphan grandchildren under BD Law)
        if (sons > 0) {
            if (daughters > 0 || (applyBdLaw && (grandsons > 0 || granddaughters > 0))) {
                long pUnits = sons * 2L;
                long sUnits = daughters * 1L + (applyBdLaw ? (grandsons * 2L + granddaughters * 1L) : 0L);
                long total = pUnits + sUnits;
                return new AsabahResolution(
                        AsabahType.BIL_GHAYR, "son", "ছেলে (পুত্র)", "daughter", "মেয়ে (কন্যা)",
                        pUnits, sUnits, total,
                        "সূরা আন-নিসা: ১১ — يُوصِيكُمُ اللَّهُ فِي أَوْلَادِكُمْ ۖ لِلذَّكَرِ مِثْلُ حَظِّ الْأُنثَيَيْنِ"
                );
            } else {
                return new AsabahResolution(
                        AsabahType.BI_NAFSIHI, "son", "ছেলে (পুত্র)", "", "",
                        sons, 0, sons,
                        "সহীহ বুখারী: ৬৭৩২ — أَلْحِقُوا الفَرَائِضَ بِأَهْلِهَا، فَمَا بَقِيَ فَهُوَ لأَوْلَى رَجُلٍ ذَكَرٍ"
                );
            }
        }

        // 2. Grandsons (with or without granddaughters)
        boolean grandsonEligible = decisions.containsKey("grandson") && decisions.get("grandson").isEligible();
        if (grandsonEligible && grandsons > 0) {
            if (granddaughters > 0) {
                long pUnits = grandsons * 2L;
                long sUnits = granddaughters * 1L;
                long total = pUnits + sUnits;
                return new AsabahResolution(
                        AsabahType.BIL_GHAYR, "grandson", "নাতি (পুত্রের ছেলে)", "granddaughter", "নাতনি (পুত্রের মেয়ে)",
                        pUnits, sUnits, total,
                        "সহীহ বুখারী: ৬৭৩৬ — নাতি ও নাতনি ২:১ অনুপাতে অবশিষ্টাংশ লাভ করবেন।"
                );
            } else {
                return new AsabahResolution(
                        AsabahType.BI_NAFSIHI, "grandson", "নাতি (পুত্রের ছেলে)", "", "",
                        grandsons, 0, grandsons,
                        "সহীহ বুখারী: ৬৭৩২ — পুত্রের অবর্তমানে নাতি আসাবা হিসেবে অবশিষ্টাংশ পাবেন।"
                );
            }
        }

        // 3. Father
        if (input.isFatherAlive()) {
            boolean hasFemaleDesc = (daughters > 0 || granddaughters > 0);
            return new AsabahResolution(
                    AsabahType.BI_NAFSIHI, "father", "বাবা (পিতা)", "", "",
                    1, 0, 1,
                    hasFemaleDesc ? "সূরা আন-নিসা: ১১ ও বুখারী ৬৭৩২ — কন্যাদের অংশের পর পিতা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।" :
                                    "সহীহ বুখারী: ৬৭৩২ — সন্তানহীন অবস্থায় পিতা আসাবা হিসেবে সম্পূর্ণ অবশিষ্টাংশ পাবেন।"
            );
        }

        // 4. Paternal Grandfather
        boolean gfEligible = decisions.containsKey("paternal_grandfather") && decisions.get("paternal_grandfather").isEligible();
        if (gfEligible && input.isPaternalGrandfatherAlive()) {
            return new AsabahResolution(
                    AsabahType.BI_NAFSIHI, "paternal_grandfather", "দাদা", "", "",
                    1, 0, 1,
                    "ইজমা ও সহীহ বুখারী: ৬৭৩২ — পিতার অবর্তমানে দাদা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।"
            );
        }

        // 5. Full Brothers & Sisters
        boolean fbEligible = decisions.containsKey("full_brother") && decisions.get("full_brother").isEligible();
        boolean fsEligible = decisions.containsKey("full_sister") && decisions.get("full_sister").isEligible();

        if (fbEligible && fullBrothers > 0) {
            if (fullSisters > 0) {
                long pUnits = fullBrothers * 2L;
                long sUnits = fullSisters * 1L;
                long total = pUnits + sUnits;
                return new AsabahResolution(
                        AsabahType.BIL_GHAYR, "full_brother", "সহোদর ভাই", "full_sister", "সহোদর বোন",
                        pUnits, sUnits, total,
                        "সূরা আন-নিসা: ১৭৬ — সহোদর ভাই ও বোন ২:১ অনুপাতে অবশিষ্টাংশ পাবেন।"
                );
            } else {
                return new AsabahResolution(
                        AsabahType.BI_NAFSIHI, "full_brother", "সহোদর ভাই", "", "",
                        fullBrothers, 0, fullBrothers,
                        "সূরা আন-নিসা: ১৭৬ — সহোদর ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।"
                );
            }
        }

        // 6. Full Sisters with Daughters (Asabah ma'al-Ghayr)
        boolean hasFemaleDescOnly = (daughters > 0 || granddaughters > 0);
        if (fsEligible && fullSisters > 0 && hasFemaleDescOnly) {
            return new AsabahResolution(
                    AsabahType.MA_AL_GHAYR, "full_sister", "সহোদর বোন", "", "",
                    fullSisters, 0, fullSisters,
                    "সহীহ বুখারী: ৬৭৪২ ও সুনান আবু দাউদ: ২৮৯২ — اجْعَلُوا الأَخَوَاتِ مَعَ البَنَاتِ عَصَبَةً"
            );
        }

        // 7. Paternal Brothers & Sisters (Consanguine)
        boolean cbEligible = decisions.containsKey("consanguine_brother") && decisions.get("consanguine_brother").isEligible();
        boolean csEligible = decisions.containsKey("consanguine_sister") && decisions.get("consanguine_sister").isEligible();

        if (cbEligible && consBrothers > 0) {
            if (consSisters > 0) {
                long pUnits = consBrothers * 2L;
                long sUnits = consSisters * 1L;
                long total = pUnits + sUnits;
                return new AsabahResolution(
                        AsabahType.BIL_GHAYR, "consanguine_brother", "সৎ ভাই (বাবার দিক)", "consanguine_sister", "সৎ বোন (বাবার দিক)",
                        pUnits, sUnits, total,
                        "সূরা আন-নিসা: ১৭৬ — বৈমাত্রেয় ভাই ও বোন ২:১ অনুপাতে আসাবা হবেন।"
                );
            } else {
                return new AsabahResolution(
                        AsabahType.BI_NAFSIHI, "consanguine_brother", "সৎ ভাই (বাবার দিক)", "", "",
                        consBrothers, 0, consBrothers,
                        "সূরা আন-নিসা: ১৭৬ — বৈমাত্রেয় ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।"
                );
            }
        }

        // 8. Consanguine Sisters with Daughters (Asabah ma'al-Ghayr)
        if (csEligible && consSisters > 0 && hasFemaleDescOnly && fullSisters == 0) {
            return new AsabahResolution(
                    AsabahType.MA_AL_GHAYR, "consanguine_sister", "সৎ বোন (বাবার দিক)", "", "",
                    consSisters, 0, consSisters,
                    "সহীহ বুখারী: ৬৭৪২ — কন্যাদের সাথে বৈমাত্রেয় বোন আসাবা মা'আল গাইর।"
            );
        }

        // 9. Nephews
        boolean nepEligible = (decisions.containsKey("nephew") && decisions.get("nephew").isEligible()) ||
                              (decisions.containsKey("full_nephew") && decisions.get("full_nephew").isEligible());
        if (nepEligible && nephews > 0) {
            return new AsabahResolution(
                    AsabahType.BI_NAFSIHI, "nephew", "ভাতিজা", "", "",
                    nephews, 0, nephews,
                    "সহীহ বুখারী: ৬৭৩২ — নিকটতম পুরুষ আত্মীয় হিসেবে ভাতিজা আসাবা হবেন।"
            );
        }

        // 10. Paternal Uncles
        boolean uncEligible = (decisions.containsKey("uncle") && decisions.get("uncle").isEligible()) ||
                              (decisions.containsKey("paternal_uncle") && decisions.get("paternal_uncle").isEligible());
        if (uncEligible && uncles > 0) {
            return new AsabahResolution(
                    AsabahType.BI_NAFSIHI, "uncle", "চাচা", "", "",
                    uncles, 0, uncles,
                    "সহীহ বুখারী: ৬৭৩২ — চাচা আসাবা হিসেবে অবশিষ্টাংশ লাভ করবেন।"
            );
        }

        // 11. Cousins
        int cousins = input.getCousinCount();
        boolean couEligible = (decisions.containsKey("cousin") && decisions.get("cousin").isEligible()) ||
                              (decisions.containsKey("paternal_cousin") && decisions.get("paternal_cousin").isEligible());
        if (couEligible && cousins > 0) {
            return new AsabahResolution(
                    AsabahType.BI_NAFSIHI, "cousin", "চাচাতো ভাই", "", "",
                    cousins, 0, cousins,
                    "সহীহ বুখারী: ৬৭৩২ — চাচাতো ভাই আসাবা হিসেবে অবশিষ্টাংশ লাভ করবেন।"
            );
        }

        return new AsabahResolution(AsabahType.NONE, "", "", "", "", 0, 0, 0, "");
    }
}