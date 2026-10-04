package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.FaraidValidationResult;
import com.devflux.deenone.features.faraid.model.HeirHajbDecision;
import com.devflux.deenone.features.faraid.model.HeirShareResult;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Classical Islamic Inheritance (Faraid) Computational Core Engine.
 *
 * Implements the standard classical Faraid algorithms (Awl, Asabah, Radd, Hajb,
 * Umariyyatan, BD 1961 Act, 4 Sunni Madhabs) deterministically.
 */
public class FaraidCalculatorEngine implements Serializable {

    public static class WorkingShare implements Serializable {
        public final String id;
        public final String relationship;
        public final String relationKey;
        public final String relationshipBn;
        public final FaraidInput.Gender gender;
        public final int count;
        public final FaraidHeir.ShareType heirCategory;
        public final HeirShareResult.ShareType legacyShareType;
        public FaraidFraction fixedFraction;
        public long asabahWeightNum;
        public long asabahWeightDen;
        public boolean isAsabah;
        public boolean isUmariyyatan;
        public FaraidDalilRepository.DalilItem dalilItem;
        public String explanation;

        public String originalPrescribedShare;
        public String legalReasonBn;
        public String legalReasonEn;

        public WorkingShare(String relationship, String relationKey, String relationshipBn,
                            FaraidInput.Gender gender, int count, FaraidHeir.ShareType heirCategory,
                            HeirShareResult.ShareType legacyShareType, FaraidFraction fixedFraction,
                            long asabahWeightNum, long asabahWeightDen, boolean isAsabah,
                            boolean isUmariyyatan, FaraidDalilRepository.DalilItem dalilItem,
                            String explanation) {
            this(relationship, relationKey, relationshipBn, gender, count, heirCategory,
                 legacyShareType, fixedFraction, asabahWeightNum, asabahWeightDen,
                 isAsabah, isUmariyyatan, dalilItem, explanation,
                 fixedFraction != null ? fixedFraction.toBengaliString() : "",
                 explanation, explanation);
        }

        public WorkingShare(String relationship, String relationKey, String relationshipBn,
                            FaraidInput.Gender gender, int count, FaraidHeir.ShareType heirCategory,
                            HeirShareResult.ShareType legacyShareType, FaraidFraction fixedFraction,
                            int asabahWeightNum, int asabahWeightDen, boolean isAsabah,
                            boolean isUmariyyatan, FaraidDalilRepository.DalilItem dalilItem,
                            String explanation) {
            this(relationship, relationKey, relationshipBn, gender, count, heirCategory,
                 legacyShareType, fixedFraction, (long) asabahWeightNum, (long) asabahWeightDen,
                 isAsabah, isUmariyyatan, dalilItem, explanation,
                 fixedFraction != null ? fixedFraction.toBengaliString() : "",
                 explanation, explanation);
        }

        public WorkingShare(String relationship, String relationKey, String relationshipBn,
                            FaraidInput.Gender gender, int count, FaraidHeir.ShareType heirCategory,
                            HeirShareResult.ShareType legacyShareType, FaraidFraction fixedFraction,
                            long asabahWeightNum, long asabahWeightDen, boolean isAsabah,
                            boolean isUmariyyatan, FaraidDalilRepository.DalilItem dalilItem,
                            String explanation, String originalPrescribedShare,
                            String legalReasonBn, String legalReasonEn) {
            this.id = relationKey != null ? relationKey : relationship;
            this.relationship = relationship;
            this.relationKey = relationKey;
            this.relationshipBn = relationshipBn;
            this.gender = gender;
            this.count = count;
            this.heirCategory = heirCategory;
            this.legacyShareType = legacyShareType;
            this.fixedFraction = fixedFraction != null ? fixedFraction : FaraidFraction.ZERO;
            this.asabahWeightNum = asabahWeightNum;
            this.asabahWeightDen = asabahWeightDen > 0 ? asabahWeightDen : 1;
            this.isAsabah = isAsabah;
            this.isUmariyyatan = isUmariyyatan;
            this.dalilItem = dalilItem;
            this.explanation = explanation;
            this.originalPrescribedShare = originalPrescribedShare;
            this.legalReasonBn = legalReasonBn;
            this.legalReasonEn = legalReasonEn;
        }
    }

    public static FaraidCalculationResult calculate(FaraidInput input) {
        FaraidCurrency currency = FaraidCurrency.BDT;
        if (input != null && input.getCurrency() != null) {
            currency = input.getCurrency();
        }

        // ==================== 0. SPECIALIST SAFEGUARD & VALIDATION ====================
        if (input == null || FaraidSpecialCasesEngine.isSpecialistVerificationRequired(input)) {
            String fallbackMsg = FaraidSpecialCasesEngine.SPECIALIST_VERIFICATION_MSG_EN + " (" +
                    FaraidSpecialCasesEngine.SPECIALIST_VERIFICATION_MSG_BN + ")";
            return new FaraidCalculationResult(
                    input != null ? input : new FaraidInput(), Math.max(0.0, input != null ? input.getTotalEstate() : 0.0), 0, 0, 0, 0, 0, 0,
                    false, false, "",
                    Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                    1, 1, false, 1, false, false,
                    false, "",
                    fallbackMsg, fallbackMsg, fallbackMsg, currency
            );
        }

        FaraidValidationResult valResult = FaraidValidationEngine.validate(input);
        if (!valResult.isValid()) {
            String valMsgBn = "যাচাইকরণ ব্যর্থ: " + valResult.getSummaryBn();
            String valMsgEn = "Validation Failed: " + valResult.getSummaryEn();
            return new FaraidCalculationResult(
                    input, Math.max(0.0, input.getTotalEstate()), 0, 0, 0, 0, 0, 0,
                    false, false, valMsgBn,
                    Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                    1, 1, false, 1, false, false,
                    false, "",
                    valMsgBn, valMsgBn, valMsgEn, currency
            );
        }

        StringBuilder auditLog = new StringBuilder();
        auditLog.append("ধাপে ধাপে শরীয়াহ ও আইনি গণনার পূর্ণাঙ্গ অডিট রিপোর্ট:\n\n");

        // ==================== 1. ESTATE PROCESSING STAGES ====================
        double gross = input.getTotalEstate();
        double funeral = input.getFuneralExpense();
        double debt = input.getDebtAmount();
        double requestedWasiyyah = input.getWasiyyahAmount();
        boolean wasiyyahToHeir = input.isWasiyyahToHeir();

        double estateAfterFuneralAndDebts = Math.max(0.0, gross - funeral - debt);
        double maxAllowedWasiyyah = estateAfterFuneralAndDebts / 3.0;
        double validWasiyyah = 0.0;
        if (!wasiyyahToHeir) {
            validWasiyyah = Math.min(requestedWasiyyah, maxAllowedWasiyyah);
        }
        double net = Math.max(0.0, estateAfterFuneralAndDebts - validWasiyyah);
        double totalDeductions = funeral + debt + validWasiyyah;

        auditLog.append("১. ত্যাজ্য সম্পত্তি ও দায় পরিশোধের ধারাবাহিক ধাপ (সূরা নিসা: ১১-১২):\n");
        auditLog.append("   • সর্বমোট ত্যাজ্য সম্পত্তি (Gross Estate): ").append(currency.format(gross, true)).append("\n");
        auditLog.append("   • দাফন-কাফন ও আনুষঙ্গিক ব্যয় (-): ").append(currency.format(funeral, true)).append("\n");
        auditLog.append("   • ঋণ ও মহরানা পরিশোধ (-): ").append(currency.format(debt, true)).append("\n");
        auditLog.append("   • ঋণ ও দাফন পরবর্তী অবশিষ্ট: ").append(currency.format(estateAfterFuneralAndDebts, true)).append("\n");
        auditLog.append("   • কার্যকরী অসিয়ত (সর্বোচ্চ ১/৩ সীমা) (-): ").append(currency.format(validWasiyyah, true)).append("\n");
        auditLog.append("   • ওয়ারিশদের মাঝে বণ্টনযোগ্য অবশিষ্ট সম্পত্তি (Net Distributable Estate): ")
                .append(currency.format(net, true)).append("\n\n");

        boolean wasiyyahExceeding = (!wasiyyahToHeir && requestedWasiyyah > maxAllowedWasiyyah && maxAllowedWasiyyah > 0);
        String wasiyyahNotice = "";
        if (wasiyyahToHeir && requestedWasiyyah > 0) {
            wasiyyahNotice = "শরীয়াহ নীতিমালা অনুযায়ী ওয়ারিশের অনুকূলে অসিয়ত স্বতঃসিদ্ধভাবে কার্যকর হয় না ('লা ওয়াসিয়্যাতা লি-ওয়ারিস', তিরমিজি: ২১২০)। সকল ওয়ারিশের সম্মতি সাপেক্ষেই কেবল এটি প্রযোজ্য হতে পারে।";
            auditLog.append("   • ").append(wasiyyahNotice).append("\n\n");
        } else if (wasiyyahExceeding) {
            double excess = requestedWasiyyah - validWasiyyah;
            wasiyyahNotice = "শরীয়াহ নীতিমালা আল-ওয়াসিয়্যাতু ফিল-হুদূদ অনুযায়ী অসিয়ত মোট অবশিষ্ট সম্পত্তির সর্বোচ্চ ১/৩ সীমা ("
                    + currency.format(maxAllowedWasiyyah, true)
                    + ") পর্যন্ত কার্যকর করা হয়েছে। অতিরিক্ত "
                    + currency.format(excess, true)
                    + " বণ্টনযোগ্য ত্যাজ্যবিত্তে ফেরত আনা হয়েছে (সহীহ বুখারী: ২৭৪৪)।";
            auditLog.append("   • ").append(wasiyyahNotice).append("\n\n");
        }

        // ==================== 2. HAJB (BLOCKING) EVALUATION ====================
        Map<String, HeirHajbDecision> hajbDecisions = FaraidHajbEngine.evaluateAll(input);
        List<BlockedHeirInfo> blockedHeirs = FaraidHajbEngine.extractBlockedHeirs(hajbDecisions);

        auditLog.append("২. ওয়ারিশদের যোগ্যতা ও বাদ পড়ার কারণ (Hajb Decisions):\n");
        for (BlockedHeirInfo bh : blockedHeirs) {
            auditLog.append("   • ").append(bh.getHeirTitleBn()).append(": ").append(bh.getLegalReasonBn()).append("\n");
        }

        // ==================== 3. FIXED SHARES & ELIGIBLE HEIRS ====================
        List<WorkingShare> shares = new ArrayList<>();
        List<FaraidHeir> allHeirsClassified = new ArrayList<>();

        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();

        boolean hasMaleDescendant = (sons > 0 || grandsons > 0);
        boolean hasOnlyFemaleDescendants = (!hasMaleDescendant && (daughters > 0 || granddaughters > 0));
        boolean hasDescendants = (hasMaleDescendant || hasOnlyFemaleDescendants);

        int fullBrothers = input.getFullBrotherCount();
        int fullSisterCount = input.getFullSisterCount();
        int consBrothers = input.getConsanguineBrotherCount();
        int consSisters = input.getConsanguineSisterCount();
        int uBrothers = input.getUterineBrotherCount();
        int uSisters = input.getUterineSisterCount();
        int nephews = input.getNephewCount();
        int uncles = input.getPaternalUncleCount();
        int totalSiblings = fullBrothers + fullSisterCount + consBrothers + consSisters + uBrothers + uSisters;
        boolean hasMultipleSiblings = (totalSiblings >= 2);

        // 3A. Spouse
        if (input.getDeceasedGender() == FaraidInput.Gender.MALE) {
            int wives = input.getWifeCount();
            if (wives > 0) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getWifeDalil(hasDescendants);
                FaraidFraction wifeFrac = hasDescendants ? FaraidFraction.ONE_EIGHTH : FaraidFraction.ONE_FOURTH;
                int indDen = (hasDescendants ? 8 : 4) * wives;
                String exp = hasDescendants
                        ? "Total wives' share = 1/8 collectively among " + wives + (wives > 1 ? " wives" : " wife") + " because the deceased left qualifying descendants." + (wives > 1 ? " Each wife receives 1/" + indDen + "." : "")
                        : "Total wives' share = 1/4 collectively among " + wives + (wives > 1 ? " wives" : " wife") + " because the deceased left no qualifying descendants." + (wives > 1 ? " Each wife receives 1/" + indDen + "." : "");
                String reasonBn = hasDescendants ? "মৃত স্বামীর সন্তান থাকায় স্ত্রীগণ সম্মিলিতভাবে ১/৮ অংশ পাবেন।" : "মৃত স্বামীর কোনো সন্তান না থাকায় স্ত্রী ১/৪ অংশ পাবেন।";
                String reasonEn = hasDescendants ? "The deceased left qualifying descendants." : "The deceased left no qualifying descendants.";

                shares.add(new WorkingShare("wife", "wife", wives == 1 ? "স্ত্রী" : "স্ত্রীগণ (" + wives + " জন)",
                        FaraidInput.Gender.FEMALE, wives, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, wifeFrac, 0, 1, false, false, dalil, exp,
                        wifeFrac.toBengaliString(), reasonBn, reasonEn));
            }
        } else {
            if (input.isHusbandAlive()) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getHusbandDalil(hasDescendants);
                FaraidFraction husbFrac = hasDescendants ? FaraidFraction.ONE_FOURTH : FaraidFraction.HALF;
                String exp = hasDescendants
                        ? "Husband receives 1/4 because the deceased left qualifying descendants. (সন্তানের উপস্থিতিতে স্বামী ১/৪ অংশ পাবেন - Qur'an 4:12)"
                        : "Husband receives 1/2 because the deceased left no qualifying descendants. (সন্তানহীন অবস্থায় স্বামী ১/২ অংশ পাবেন - Qur'an 4:12)";
                String reasonBn = hasDescendants ? "মৃত স্ত্রীর সন্তান থাকায় স্বামী ১/৪ অংশ পাবেন।" : "মৃত স্ত্রীর কোনো সন্তান না থাকায় স্বামী ১/২ অংশ পাবেন।";
                String reasonEn = hasDescendants ? "The deceased left qualifying descendants." : "The deceased left no qualifying descendants.";

                shares.add(new WorkingShare("husband", "husband", "স্বামী",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, husbFrac, 0, 1, false, false, dalil, exp,
                        husbFrac.toBengaliString(), reasonBn, reasonEn));
            }
        }

        // 3B. Mother
        boolean isUmariyyatanCase = false;
        if (input.isMotherAlive()) {
            if (FaraidSpecialCasesEngine.isUmariyyatanCase(input)) {
                isUmariyyatanCase = true;
                FaraidDalilRepository.DalilItem dalil = new FaraidDalilRepository.DalilItem(
                        "قَضَى عُمَرُ وَعُثْمَانُ وَزَيْدُ بْنُ ثَابِتٍ لِلأُمِّ بِثُلُثِ مَا بَقِيَ",
                        "কাদ্বা উমারু ওয়া উছমানু ওয়া যাইদু ইবনু ছাবিতিন লিল উম্মি বি-ছুলুছি মা বাকিয়া",
                        "উমর, উসমান ও যায়েদ ইবনে ছাবিত (রা.) স্বামী/স্ত্রীর অংশের পর মাতাকে অবশিষ্টের এক-তৃতীয়াংশ দেওয়ার সিদ্ধান্ত দেন।",
                        "মুসান্নাফ আব্দুর রাজ্জাক ও সুনান বায়হাকী",
                        "চার মাযহাবের (হানাফি, শাফেয়ী, মালিকি, হাম্বলী) সর্বসম্মত সিদ্ধান্ত।",
                        "উমারিয়্যাতান নীতি অনুসারে বণ্টন।"
                );
                FaraidFraction spouseFrac = (input.getDeceasedGender() == FaraidInput.Gender.MALE) ? FaraidFraction.ONE_FOURTH : FaraidFraction.HALF;
                FaraidFraction motherUmariyyatanFrac = FaraidFraction.ONE.subtract(spouseFrac).multiply(FaraidFraction.ONE_THIRD);

                shares.add(new WorkingShare("mother", "mother", "মা (উমারিয়্যাতান)",
                        FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, motherUmariyyatanFrac, 0, 1, false, true, dalil,
                        "Mother receives 1/3 of the residue after the spouse's share in the Umariyyatan case (আল-উমারিয়্যাতান মাসআলায় স্বামী/স্ত্রীর অংশের পর অবশিষ্টের ১/৩ অংশ লাভ করবেন)",
                        "অবশিষ্টের ১/৩", "আল-উমারিয়্যাতান মাসআলায় পিতা ও স্বামী/স্ত্রীর উপস্থিতিতে অবশিষ্টের এক-তৃতীয়াংশ।", "1/3 of residue in Umariyyatan case."));
            } else {
                FaraidFraction mFrac = (hasDescendants || hasMultipleSiblings) ? FaraidFraction.ONE_SIXTH : FaraidFraction.ONE_THIRD;
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getMotherDalil(hasDescendants, hasMultipleSiblings);
                String exp;
                String reasonBn;
                String reasonEn;
                if (hasDescendants) {
                    exp = "Mother receives 1/6 because the deceased left qualifying descendants (Qur'an 4:11). (সন্তান থাকায় মা ১/৬ অংশ পাবেন)";
                    reasonBn = "মৃতের সন্তান/বংশধর উপস্থিত থাকায় মাতা ১/৬ অংশ পাবেন।";
                    reasonEn = "Presence of qualifying descendants reduces mother's share to 1/6.";
                } else if (hasMultipleSiblings) {
                    exp = "Mother receives 1/6 because 2 or more siblings exist (Qur'an 4:11), even though those siblings may be blocked from inheriting by the father. (একাধিক ভাই-বোন থাকায় মা ১/৬ অংশ পাবেন)";
                    reasonBn = "মৃতের একাধিক (২ বা ততোধিক) ভাই-বোন থাকায় মাতা ১/৬ অংশ পাবেন।";
                    reasonEn = "Presence of 2 or more siblings reduces mother's share to 1/6.";
                } else {
                    exp = "Mother receives 1/3 because the deceased left no qualifying descendants and less than 2 siblings (Qur'an 4:11). (সন্তান ও একাধিক ভাই-বোন না থাকায় মা ১/৩ অংশ পাবেন)";
                    reasonBn = "সন্তান এবং একাধিক ভাই-বোন না থাকায় মা নির্ধারিত ১/৩ অংশ পাবেন।";
                    reasonEn = "The deceased left no qualifying descendants and fewer than 2 siblings.";
                }

                shares.add(new WorkingShare("mother", "mother", "মা (মাতা)",
                        FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, mFrac, 0, 1, false, false, dalil, exp,
                        mFrac.toBengaliString(), reasonBn, reasonEn));
            }
        }

        // 3C. Father
        if (input.isFatherAlive()) {
            if (hasMaleDescendant) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(true, false);
                shares.add(new WorkingShare("father", "father", "বাবা (পিতা)",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                        "Father receives 1/6 fixed share only as an Ashab al-Furud heir because of the presence of a male descendant (Qur'an 4:11). (মৃতের ছেলে/নাতি থাকায় বাবা নির্ধারিত ১/৬ অংশ পাবেন)",
                        "১/৬", "মৃতের পুরুষ বংশধর (ছেলে/নাতি) থাকায় বাবা নির্ধারিত ১/৬ অংশ পাবেন।", "Presence of a male descendant."));
            } else if (hasOnlyFemaleDescendants) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, true);
                shares.add(new WorkingShare("father", "father", "বাবা (অংশীদার ও আসাবা)",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED_AND_RESIDUE,
                        HeirShareResult.ShareType.FIXED_AND_ASABAH, FaraidFraction.ONE_SIXTH, 1, 1, true, false, dalil,
                        "Father receives 1/6 fixed share plus the residue as an Asabah heir because of the presence of only female descendants (Qur'an 4:11, Bukhari 6732). (শুধু মেয়ে/নাতনি থাকায় বাবা নির্ধারিত ১/৬ অংশের পাশাপাশি আসাবা হিসেবে অবশিষ্টাংশও পাবেন)",
                        "১/৬ + আসাবা", "শুধু নারী বংশধর (মেয়ে/নাতনি) থাকায় বাবা ১/৬ নির্ধারিত অংশ এবং আসাবা হিসেবে অবশিষ্টাংশ উভয়টি পাবেন।", "Presence of female descendants only (Fixed 1/6 + Residue)."));
            } else {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                shares.add(new WorkingShare("father", "father", "বাবা (আসাবা)",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.RESIDUE,
                        HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, 1, 1, true, false, dalil,
                        "Father inherits purely as a Residuary (Asabah) heir because the deceased left no qualifying descendants (Sahih al-Bukhari 6732). (সন্তান না থাকায় বাবা আসাবা হিসেবে অবশিষ্ট সমস্ত সম্পত্তি পাবেন)",
                        "আসাবা (সম্পূর্ণ অবশিষ্ট)", "মৃতের কোনো সন্তান না থাকায় বাবা আসাবা হিসেবে সমস্ত অবশিষ্ট সম্পত্তি পাবেন।", "Asabah bi-nafsihi in absence of descendants."));
            }
        }

        // 3D. Grandparents
        if (hajbDecisions.containsKey("paternal_grandfather") && hajbDecisions.get("paternal_grandfather").isEligible()) {
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(hasMaleDescendant, hasOnlyFemaleDescendants);
            if (hasMaleDescendant) {
                shares.add(new WorkingShare("paternal_grandfather", "paternal_grandfather", "দাদা",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                        "বাবার অবর্তমানে এবং ছেলে/নাতির উপস্থিতিতে দাদা ১/৬ অংশ পাবেন।",
                        "১/৬", "বাবার অবর্তমানে এবং ছেলে/নাতির উপস্থিতিতে দাদা ১/৬ অংশ পাবেন।", "Paternal grandfather inherits 1/6 in absence of father with male descendants."));
            } else if (hasOnlyFemaleDescendants) {
                shares.add(new WorkingShare("paternal_grandfather", "paternal_grandfather", "দাদা (অংশীদার ও আসাবা)",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED_AND_RESIDUE,
                        HeirShareResult.ShareType.FIXED_AND_ASABAH, FaraidFraction.ONE_SIXTH, 1, 1, true, false, dalil,
                        "বাবার অবর্তমানে এবং শুধু মেয়ে/নাতনি থাকায় দাদা ১/৬ + আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                        "১/৬ + আসাবা", "বাবার অবর্তমানে এবং শুধু মেয়ে থাকায় দাদা ১/৬ + অবশিষ্টাংশ পাবেন।", "Fixed 1/6 + Residue in absence of father with daughters."));
            } else {
                shares.add(new WorkingShare("paternal_grandfather", "paternal_grandfather", "দাদা (আসাবা)",
                        FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.RESIDUE,
                        HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, 1, 1, true, false, dalil,
                        "বাবার অবর্তমানে দাদা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                        "আসাবা", "বাবার অবর্তমানে দাদা আসাবা হিসেবে অবশিষ্ট সম্পত্তি পাবেন।", "Asabah in absence of father and descendants."));
            }
        }

        boolean pGrandmotherEligible = hajbDecisions.containsKey("paternal_grandmother") && hajbDecisions.get("paternal_grandmother").isEligible();
        boolean mGrandmotherEligible = hajbDecisions.containsKey("maternal_grandmother") && hajbDecisions.get("maternal_grandmother").isEligible();

        if (pGrandmotherEligible && mGrandmotherEligible) {
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getMotherDalil(true, false);
            shares.add(new WorkingShare("grandmother", "grandmother", "দাদী ও নানী (উভয়ে জীবিত)",
                    FaraidInput.Gender.FEMALE, 2, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                    "দাদী ও নানী উভয়ে ১/৬ অংশ সমানভাবে ভাগ করে নেবেন (প্রত্যেকে ১/১২)।",
                    "১/৬ (সমবণ্টন)", "মায়ের অবর্তমানে দাদী ও নানী উভয়ে মিলে ১/৬ অংশ সমানভাবে ভাগ করে নেবেন।", "Grandmothers share 1/6 equally in absence of mother."));
        } else if (pGrandmotherEligible) {
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getMotherDalil(true, false);
            shares.add(new WorkingShare("paternal_grandmother", "paternal_grandmother", "দাদী",
                    FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                    "মায়ের অবর্তমানে দাদী আসহাবুল ফুরুজ হিসেবে ১/৬ অংশ পাবেন।",
                    "১/৬", "মায়ের অবর্তমানে দাদী আসহাবুল ফুরুজ হিসেবে ১/৬ অংশ পাবেন।", "Paternal grandmother inherits 1/6 in absence of mother."));
        } else if (mGrandmotherEligible) {
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getMotherDalil(true, false);
            shares.add(new WorkingShare("maternal_grandmother", "maternal_grandmother", "নানী",
                    FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                    "মায়ের অবর্তমানে নানী আসহাবুল ফুরুজ হিসেবে ১/৬ অংশ পাবেন।",
                    "১/৬", "মায়ের অবর্তমানে নানী আসহাবুল ফুরুজ হিসেবে ১/৬ অংশ পাবেন।", "Maternal grandmother inherits 1/6 in absence of mother."));
        }

        // 3E. Daughters (when sons == 0)
        if (daughters > 0 && sons == 0) {
            FaraidDalilRepository.DalilItem dalil = (daughters == 1) ? FaraidDalilRepository.getSingleDaughterDalil() : FaraidDalilRepository.getMultipleDaughtersDalil();
            if (daughters == 1) {
                shares.add(new WorkingShare("daughter", "daughter", "একমাত্র মেয়ে (কন্যা)",
                        FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.HALF, 0, 1, false, false, dalil,
                        "Single daughter receives 1/2 fixed share (Qur'an 4:11). (একমাত্র মেয়ে নির্ধারিত ১/২ অংশ পাবেন)",
                        "১/২", "মৃতের একমাত্র কন্যা সন্তান থাকায় এবং কোনো পুত্র না থাকায় ১/২ অংশ পাবেন।", "Single daughter with no sons receives 1/2 fixed share."));
            } else {
                shares.add(new WorkingShare("daughter", "daughter", "মেয়েগণ (" + daughters + " জন)",
                        FaraidInput.Gender.FEMALE, daughters, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.TWO_THIRDS, 0, 1, false, false, dalil,
                        "Daughters (" + daughters + ") collectively receive 2/3 fixed share (Qur'an 4:11). (" + daughters + " জন মেয়ে সম্মিলিতভাবে নির্ধারিত ২/৩ অংশ সমভাগে পাবেন)",
                        "২/৩", "পুত্রহীন অবস্থায় ২ বা ততোধিক কন্যা সন্তান সম্মিলিতভাবে ২/৩ অংশ সমভাগে পাবেন।", "Multiple daughters with no sons receive 2/3 collectively."));
            }
        }

        // 3F. Granddaughters (fixed shares)
        if (hajbDecisions.containsKey("granddaughter") && hajbDecisions.get("granddaughter").isEligible() &&
                hajbDecisions.get("granddaughter").getStatus() == HeirEligibilityStatus.ELIGIBLE_FIXED) {
            if (daughters == 1) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getGranddaughterTakmilatDalil();
                shares.add(new WorkingShare("granddaughter", "granddaughter", granddaughters == 1 ? "নাতনি (পুত্রের মেয়ে)" : "নাতনিগণ (" + granddaughters + " জন)",
                        FaraidInput.Gender.FEMALE, granddaughters, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                        "Granddaughters receive 1/6 (Takmilat al-Thuluthayn) to complete 2/3 with 1 daughter.",
                        "১/৬", "একমাত্র কন্যার (১/২) সাথে নাতনি ২/৩ অংশ পূর্ণ করার নিয়মে ১/৬ অংশ লাভ করবেন।", "Takmilat al-Thuluthayn with 1 daughter."));
            } else if (daughters == 0) {
                FaraidDalilRepository.DalilItem dalil = (granddaughters == 1) ? FaraidDalilRepository.getSingleDaughterDalil() : FaraidDalilRepository.getMultipleDaughtersDalil();
                FaraidFraction gdFrac = (granddaughters == 1) ? FaraidFraction.HALF : FaraidFraction.TWO_THIRDS;
                shares.add(new WorkingShare("granddaughter", "granddaughter", granddaughters == 1 ? "একমাত্র নাতনি" : "নাতনিগণ (" + granddaughters + " জন)",
                        FaraidInput.Gender.FEMALE, granddaughters, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, gdFrac, 0, 1, false, false, dalil,
                        "ছেলে বা মেয়ে না থাকায় নাতনিগণ " + gdFrac.toBengaliString() + " অংশ পাবেন (Qur'an 4:11)।",
                        gdFrac.toBengaliString(), "মৃতের কোনো সন্তান না থাকায় নাতনিগণ কন্যার স্থলাভিষিক্ত হয়ে অংশ পাবেন।", "Granddaughters inherit in absence of direct children."));
            }
        }

        // 3G. Full Sisters (fixed shares)
        if (hajbDecisions.containsKey("full_sister") && hajbDecisions.get("full_sister").isEligible() &&
                hajbDecisions.get("full_sister").getStatus() == HeirEligibilityStatus.ELIGIBLE_FIXED) {
            FaraidDalilRepository.DalilItem dalil = (fullSisterCount == 1) ? FaraidDalilRepository.getFullSisterSingleDalil() : FaraidDalilRepository.getFullSistersMultipleDalil();
            FaraidFraction sFrac = (fullSisterCount == 1) ? FaraidFraction.HALF : FaraidFraction.TWO_THIRDS;
            shares.add(new WorkingShare("full_sister", "full_sister", fullSisterCount == 1 ? "একমাত্র সহোদর বোন" : "সহোদর বোনগণ (" + fullSisterCount + " জন)",
                    FaraidInput.Gender.FEMALE, fullSisterCount, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, sFrac, 0, 1, false, false, dalil,
                    "Full sister receives " + sFrac.toBengaliString() + " fixed share under Kalalah (Qur'an 4:176).",
                    sFrac.toBengaliString(), "কালালাহ অবস্থায় (পিতা, সন্তান ও ভাই না থাকায়) সহোদর বোন নির্ধারিত অংশ পাবেন।", "Full sisters inherit fixed share under Kalalah (Qur'an 4:176)."));
        }

        // 3H. Consanguine Sisters (fixed shares)
        if (hajbDecisions.containsKey("consanguine_sister") && hajbDecisions.get("consanguine_sister").isEligible() &&
                hajbDecisions.get("consanguine_sister").getStatus() == HeirEligibilityStatus.ELIGIBLE_FIXED) {
            if (fullSisterCount == 1) {
                FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getGranddaughterTakmilatDalil();
                shares.add(new WorkingShare("consanguine_sister", "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)",
                        FaraidInput.Gender.FEMALE, consSisters, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, FaraidFraction.ONE_SIXTH, 0, 1, false, false, dalil,
                        "Consanguine sister receives 1/6 (Takmilat al-Thuluthayn) with one full sister (Qur'an 4:176).",
                        "১/৬", "একমাত্র সহোদর বোনের (১/২) সাথে বৈমাত্রেয় বোন ২/৩ অংশ পূর্ণ করার নিয়মে ১/৬ অংশ পাবেন।", "Takmilat al-Thuluthayn with 1 full sister."));
            } else if (fullSisterCount == 0) {
                FaraidDalilRepository.DalilItem dalil = (consSisters == 1) ? FaraidDalilRepository.getFullSisterSingleDalil() : FaraidDalilRepository.getFullSistersMultipleDalil();
                FaraidFraction csFrac = (consSisters == 1) ? FaraidFraction.HALF : FaraidFraction.TWO_THIRDS;
                shares.add(new WorkingShare("consanguine_sister", "consanguine_sister", consSisters == 1 ? "একমাত্র সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)",
                        FaraidInput.Gender.FEMALE, consSisters, FaraidHeir.ShareType.FIXED,
                        HeirShareResult.ShareType.FIXED_QURANIC, csFrac, 0, 1, false, false, dalil,
                        "Consanguine sisters receive " + csFrac.toBengaliString() + " under Kalalah.",
                        csFrac.toBengaliString(), "কালালাহ অবস্থায় বৈমাত্রেয় বোন নির্ধারিত অংশ পাবেন।", "Consanguine sisters inherit fixed share under Kalalah."));
            }
        }

        // 3I. Maternal Siblings (Qur'an 4:12 - 1:1 equal gender sharing)
        int uTotal = uBrothers + uSisters;
        boolean uBroEligible = hajbDecisions.containsKey("uterine_brother") && hajbDecisions.get("uterine_brother").isEligible();
        boolean uSisEligible = hajbDecisions.containsKey("uterine_sister") && hajbDecisions.get("uterine_sister").isEligible();

        if (uBroEligible && uBrothers > 0) {
            FaraidFraction uBroFrac = (uTotal == 1) ? FaraidFraction.ONE_SIXTH :
                    (uSisters == 0 ? FaraidFraction.ONE_THIRD : new FaraidFraction(uBrothers, 3 * uTotal));
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getUterineBrotherDalil(uTotal > 1);
            shares.add(new WorkingShare("uterine_brother", "uterine_brother",
                    uBrothers == 1 ? "সৎ ভাই (মায়ের দিক)" : "সৎ ভাইগণ (মায়ের দিক - " + uBrothers + " জন)",
                    FaraidInput.Gender.MALE, uBrothers, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, uBroFrac, 0, 1, false, false, dalil,
                    "Maternal half-brother receives " + uBroFrac.toBengaliString() + " fixed share in Kalalah with 1:1 equal gender sharing (Qur'an 4:12).",
                    uBroFrac.toBengaliString(), "কালালাহ অবস্থায় বৈপিত্রীয় ভাই আসহাবুল ফুরুজ হিসেবে ১:১ সমবণ্টনে অংশ পাবেন (সূরা নিসা: ১২)।", "Maternal brother inherits under Qur'an 4:12 with equal gender sharing."));
        }

        if (uSisEligible && uSisters > 0) {
            FaraidFraction uSisFrac = (uTotal == 1) ? FaraidFraction.ONE_SIXTH :
                    (uBrothers == 0 ? FaraidFraction.ONE_THIRD : new FaraidFraction(uSisters, 3 * uTotal));
            FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getUterineSisterDalil(uTotal > 1);
            shares.add(new WorkingShare("uterine_sister", "uterine_sister",
                    uSisters == 1 ? "সৎ বোন (মায়ের দিক)" : "সৎ বোনগণ (মায়ের দিক - " + uSisters + " জন)",
                    FaraidInput.Gender.FEMALE, uSisters, FaraidHeir.ShareType.FIXED,
                    HeirShareResult.ShareType.FIXED_QURANIC, uSisFrac, 0, 1, false, false, dalil,
                    "Maternal half-sister receives " + uSisFrac.toBengaliString() + " fixed share in Kalalah with 1:1 equal gender sharing (Qur'an 4:12).",
                    uSisFrac.toBengaliString(), "কালালাহ অবস্থায় বৈপিত্রীয় বোন আসহাবুল ফুরুজ হিসেবে ১:১ সমবণ্টনে অংশ পাবেন (সূরা নিসা: ১২)।", "Maternal sister inherits under Qur'an 4:12 with equal gender sharing."));
        }

        // ==================== 4. ASABAH (RESIDUE) RESOLUTION ====================
        FaraidAsabahEngine.AsabahResolution asabahRes = FaraidAsabahEngine.resolveClosestAsabah(input, hajbDecisions);
        if (asabahRes.hasAsabah()) {
            if (asabahRes.getAsabahType() == AsabahType.BIL_GHAYR) {
                if ("son".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getSonsAndDaughtersDalil();
                    shares.add(new WorkingShare("son", "son", sons == 1 ? "ছেলে (পুত্র)" : "ছেলেগণ (" + sons + " জন)",
                            FaraidInput.Gender.MALE, sons, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, sons * 2L, 1, true, false, dalil,
                            "Sons and daughters inherit as Residuary (Asabah bil-Ghayr) heirs in a 2:1 ratio (Qur'an 4:11). (ছেলে ও মেয়ে ২:১ অনুপাতে অবশিষ্টাংশ পাবেন)",
                            "আসাবা বিল গাইর (২:১)", "কন্যার উপস্থিতিতে পুত্র আসাবা বিল গাইর হিসেবে ২:১ অনুপাতে অবশিষ্টাংশ পাবেন।", "Sons inherit as Asabah bil-ghayr with daughters in 2:1 ratio (Qur'an 4:11)."));
                    if (daughters > 0) {
                        shares.add(new WorkingShare("daughter", "daughter", daughters == 1 ? "মেয়ে (কন্যা)" : "মেয়েগণ (" + daughters + " জন)",
                                FaraidInput.Gender.FEMALE, daughters, FaraidHeir.ShareType.RESIDUE,
                                HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, daughters * 1L, 1, true, false, dalil,
                                "Sons and daughters inherit as Residuary (Asabah bil-Ghayr) heirs in a 2:1 ratio (Qur'an 4:11). (ছেলে ও মেয়ে ২:১ অনুপাতে অবশিষ্টাংশ পাবেন)",
                                "আসাবা বিল গাইর (২:১)", "পুত্রের উপস্থিতিতে কন্যা আসাবা বিল গাইর হিসেবে ২:১ অনুপাতে অবশিষ্টাংশ পাবেন।", "Daughters inherit as Asabah bil-ghayr with sons in 2:1 ratio (Qur'an 4:11)."));
                    }
                    if (input.isApplyBangladeshLaw1961()) {
                        if (grandsons > 0) {
                            shares.add(new WorkingShare("grandson", "grandson", grandsons == 1 ? "নাতি (পুত্রের ছেলে)" : "নাতিগণ (" + grandsons + " জন)",
                                    FaraidInput.Gender.MALE, grandsons, FaraidHeir.ShareType.RESIDUE,
                                    HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, grandsons * 2L, 1, true, false, dalil,
                                    "বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ ধারা ৪ মোতাবেক প্রতিনিধিত্বমূলক উত্তরাধিকার।",
                                    "ধারা ৪ (প্রতিনিধিত্বমূলক)", "১৯৬১ সালের পারিবারিক আইন অনুযায়ী মৃত পিতার অংশের স্থলাভিষিক্ত হয়ে নাতি অংশ পাবেন।", "Grandson represents deceased parent under BD Law 1961 Sec 4."));
                        }
                        if (granddaughters > 0) {
                            shares.add(new WorkingShare("granddaughter", "granddaughter", granddaughters == 1 ? "নাতনি (পুত্রের মেয়ে)" : "নাতনিগণ (" + granddaughters + " জন)",
                                    FaraidInput.Gender.FEMALE, granddaughters, FaraidHeir.ShareType.RESIDUE,
                                    HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, granddaughters * 1L, 1, true, false, dalil,
                                    "বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ ধারা ৪ মোতাবেক প্রতিনিধিত্বমূলক উত্তরাধিকার।",
                                    "ধারা ৪ (প্রতিনিধিত্বমূলক)", "১৯৬১ সালের পারিবারিক আইন অনুযায়ী মৃত পিতার অংশের স্থলাভিষিক্ত হয়ে নাতনি অংশ পাবেন।", "Granddaughter represents deceased parent under BD Law 1961 Sec 4."));
                        }
                    }
                } else if ("grandson".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getSonsAndDaughtersDalil();
                    shares.add(new WorkingShare("grandson", "grandson", grandsons == 1 ? "নাতি (পুত্রের ছেলে)" : "নাতিগণ (" + grandsons + " জন)",
                            FaraidInput.Gender.MALE, grandsons, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, grandsons * 2L, 1, true, false, dalil,
                            "পুত্রের অবর্তমানে নাতি ও নাতনি আসাবা বিল গাইর (২:১) হিসেবে অবশিষ্টাংশ পাবেন।",
                            "আসাবা বিল গাইর (২:১)", "পুত্রের অবর্তমানে নাতি ও নাতনি ২:১ অনুপাতে আসাবা হিসেবে অংশ পাবেন।", "Grandsons inherit as Asabah bil-ghayr with granddaughters."));
                    if (granddaughters > 0) {
                        shares.add(new WorkingShare("granddaughter", "granddaughter", granddaughters == 1 ? "নাতনি (পুত্রের মেয়ে)" : "নাতনিগণ (" + granddaughters + " জন)",
                                FaraidInput.Gender.FEMALE, granddaughters, FaraidHeir.ShareType.RESIDUE,
                                HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, granddaughters * 1L, 1, true, false, dalil,
                                "নাতির সাথে নাতনি আসাবা বিল গাইর হিসেবে অংশ পাবেন।",
                                "আসাবা বিল গাইর (২:১)", "নাতির সাথে নাতনি আসাবা বিল গাইর হিসেবে অংশ পাবেন।", "Granddaughters inherit as Asabah bil-ghayr with grandsons."));
                    }
                } else if ("full_brother".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFullSiblingsResidueDalil();
                    shares.add(new WorkingShare("full_brother", "full_brother", fullBrothers == 1 ? "সহোদর ভাই" : "সহোদর ভাইগণ (" + fullBrothers + " জন)",
                            FaraidInput.Gender.MALE, fullBrothers, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, fullBrothers * 2L, 1, true, false, dalil,
                            "Full brothers and sisters inherit as Residuary (Asabah bil-Ghayr) 2:1 under Kalalah (Qur'an 4:176).",
                            "আসাবা বিল গাইর (২:১)", "কালালাহ অবস্থায় সহোদর ভাই ও বোন ২:১ অনুপাতে আসাবা হিসেবে অংশ পাবেন।", "Full brothers inherit as Asabah bil-ghayr with full sisters."));
                    if (fullSisterCount > 0) {
                        shares.add(new WorkingShare("full_sister", "full_sister", fullSisterCount == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisterCount + " জন)",
                                FaraidInput.Gender.FEMALE, fullSisterCount, FaraidHeir.ShareType.RESIDUE,
                                HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, fullSisterCount * 1L, 1, true, false, dalil,
                                "Full sisters inherit as Residuary (Asabah bil-Ghayr) with full brothers 2:1 under Kalalah (Qur'an 4:176).",
                                "আসাবা বিল গাইর (২:১)", "সহোদর ভাইয়ের সাথে বোন ২:১ অনুপাতে আসাবা হবেন।", "Full sisters inherit as Asabah bil-ghayr with full brothers."));
                    }
                } else if ("consanguine_brother".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFullSiblingsResidueDalil();
                    shares.add(new WorkingShare("consanguine_brother", "consanguine_brother", consBrothers == 1 ? "সৎ ভাই (বাবার দিক)" : "সৎ ভাইগণ (বাবার দিক - " + consBrothers + " জন)",
                            FaraidInput.Gender.MALE, consBrothers, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, consBrothers * 2L, 1, true, false, dalil,
                            "Consanguine brothers and sisters inherit as Residuary (Asabah bil-Ghayr) 2:1 under Kalalah (Qur'an 4:176).",
                            "আসাবা বিল গাইর (২:১)", "কালালাহ অবস্থায় বৈমাত্রেয় ভাই ও বোন ২:১ অনুপাতে আসাবা হিসেবে অংশ পাবেন।", "Consanguine brothers inherit as Asabah bil-ghayr with consanguine sisters."));
                    if (consSisters > 0) {
                        shares.add(new WorkingShare("consanguine_sister", "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)",
                                FaraidInput.Gender.FEMALE, consSisters, FaraidHeir.ShareType.RESIDUE,
                                HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, consSisters * 1L, 1, true, false, dalil,
                                "Consanguine sisters inherit as Residuary (Asabah bil-Ghayr) with consanguine brothers 2:1 under Kalalah (Qur'an 4:176).",
                                "আসাবা বিল গাইর (২:১)", "বৈমাত্রেয় ভাইয়ের সাথে বোন ২:১ অনুপাতে আসাবা হবেন।", "Consanguine sisters inherit as Asabah bil-ghayr with consanguine brothers."));
                    }
                }
            } else if (asabahRes.getAsabahType() == AsabahType.MA_AL_GHAYR) {
                if ("full_sister".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = new FaraidDalilRepository.DalilItem(
                            "اجْعَلُوا الأَخَوَاتِ مَعَ البَنَاتِ عَصَبَةً",
                            "ইজ'আলুল আখাওয়াতি মা'আল বানাতি আসাবাহ",
                            "রাসূলুল্লাহ ﷺ নির্দেশ দিয়েছেন: কন্যাদের সাথে বোনদের আসাবা গণ্য করো।",
                            "সহীহ বুখারী: ৬৭৪২ ও সুনান আবু দাউদ: ২৮৯২",
                            "চার মাযহাবের সর্বসম্মত ফতোয়া (আসাবা মা'আল গাইর)।",
                            "কন্যার উপস্থিতিতে বোন আসাবা মা'আল গাইর।"
                    );
                    shares.add(new WorkingShare("full_sister", "full_sister", fullSisterCount == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisterCount + " জন)",
                            FaraidInput.Gender.FEMALE, fullSisterCount, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "Full sister inherits as Residuary (Asabah ma'al-Ghayr) alongside daughters (Sahih al-Bukhari 6742).",
                            "আসাবা মা'আল গাইর", "কন্যার উপস্থিতিতে সহোদর বোন আসাবা মা'আল গাইর হিসেবে অবশিষ্টাংশ পাবেন (সহীহ বুখারী: ৬৭৪২)।", "Full sister inherits as Asabah ma'al-ghayr alongside daughters."));
                } else if ("consanguine_sister".equals(asabahRes.getPrimaryAsabahKey())) {
                    FaraidDalilRepository.DalilItem dalil = new FaraidDalilRepository.DalilItem(
                            "اجْعَلُوا الأَخَوَاتِ مَعَ البَنَاتِ عَصَبَةً",
                            "ইজ'আলুল আখাওয়াতি মা'আল বানাতি আসাবাহ",
                            "রাসূলুল্লাহ ﷺ নির্দেশ দিয়েছেন: কন্যাদের সাথে বোনদের আসাবা গণ্য করো।",
                            "সহীহ বুখারী: ৬৭৪২",
                            "চার মাযহাবের সর্বসম্মত ফতোয়া (আসাবা মা'আল গাইর)।",
                            "কন্যার উপস্থিতিতে বৈমাত্রেয় বোন আসাবা মা'আল গাইর।"
                    );
                    shares.add(new WorkingShare("consanguine_sister", "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)",
                            FaraidInput.Gender.FEMALE, consSisters, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "Consanguine sister inherits as Residuary (Asabah ma'al-Ghayr) alongside daughters (Sahih al-Bukhari 6742).",
                            "আসাবা মা'আল গাইর", "কন্যার উপস্থিতিতে বৈমাত্রেয় বোন আসাবা মা'আল গাইর হিসেবে অবশিষ্টাংশ পাবেন।", "Consanguine sister inherits as Asabah ma'al-ghayr alongside daughters."));
                }
            } else if (asabahRes.getAsabahType() == AsabahType.BI_NAFSIHI) {
                String key = asabahRes.getPrimaryAsabahKey();
                if ("son".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                    shares.add(new WorkingShare("son", "son", sons == 1 ? "ছেলে (পুত্র)" : "ছেলেগণ (" + sons + " জন)",
                            FaraidInput.Gender.MALE, sons, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "Sons inherit as Residuary (Asabah bi-Nafsihi) heirs (Sahih al-Bukhari 6732).",
                            "আসাবা বি-নাফসিহি", "পুত্র আসাবা হিসেবে অবশিষ্ট সমস্ত সম্পত্তি লাভ করবেন (সহীহ বুখারী: ৬৭৩২)।", "Sons inherit residue as Asabah bi-nafsihi."));
                } else if ("grandson".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                    shares.add(new WorkingShare("grandson", "grandson", grandsons == 1 ? "নাতি (পুত্রের ছেলে)" : "নাতিগণ (" + grandsons + " জন)",
                            FaraidInput.Gender.MALE, grandsons, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "পুত্রের অবর্তমানে নাতি আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                            "আসাবা বি-নাফসিহি", "পুত্রের অবর্তমানে নাতি আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Grandsons inherit residue as Asabah bi-nafsihi."));
                } else if ("full_brother".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFullSiblingsResidueDalil();
                    shares.add(new WorkingShare("full_brother", "full_brother", fullBrothers == 1 ? "সহোদর ভাই" : "সহোদর ভাইগণ (" + fullBrothers + " জন)",
                            FaraidInput.Gender.MALE, fullBrothers, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "Full brother inherits as Residuary (Asabah) heir under Kalalah (Qur'an 4:176).",
                            "আসাবা বি-নাফসিহি", "কালালাহ অবস্থায় সহোদর ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Full brothers inherit residue under Kalalah."));
                } else if ("consanguine_brother".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFullSiblingsResidueDalil();
                    shares.add(new WorkingShare("consanguine_brother", "consanguine_brother", consBrothers == 1 ? "সৎ ভাই (বাবার দিক)" : "সৎ ভাইগণ (বাবার দিক - " + consBrothers + " জন)",
                            FaraidInput.Gender.MALE, consBrothers, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "Consanguine brother inherits as Residuary (Asabah) heir under Kalalah (Qur'an 4:176).",
                            "আসাবা বি-নাফসিহি", "কালালাহ অবস্থায় বৈমাত্রেয় ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Consanguine brothers inherit residue under Kalalah."));
                } else if ("nephew".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                    shares.add(new WorkingShare("nephew", "nephew", nephews == 1 ? "ভাতিজা" : "ভাতিজাগণ (" + nephews + " জন)",
                            FaraidInput.Gender.MALE, nephews, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "নিকটবর্তী কোনো আসাবা না থাকায় ভাতিজা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                            "আসাবা বি-নাফসিহি", "নিকটবর্তী কোনো আসাবা না থাকায় ভাতিজা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Nephew inherits residue as closest male agnate."));
                } else if ("uncle".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                    shares.add(new WorkingShare("uncle", "uncle", uncles == 1 ? "চাচা" : "চাচাগণ (" + uncles + " জন)",
                            FaraidInput.Gender.MALE, uncles, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "নিকটবর্তী কোনো আসাবা না থাকায় চাচা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                            "আসাবা বি-নাফসিহি", "নিকটবর্তী কোনো আসাবা না থাকায় চাচা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Paternal uncle inherits residue as closest male agnate."));
                } else if ("cousin".equals(key)) {
                    FaraidDalilRepository.DalilItem dalil = FaraidDalilRepository.getFatherDalil(false, false);
                    int cousins = input.getCousinCount();
                    shares.add(new WorkingShare("cousin", "cousin", cousins == 1 ? "চাচাতো ভাই" : "চাচাতো ভাইগণ (" + cousins + " জন)",
                            FaraidInput.Gender.MALE, cousins, FaraidHeir.ShareType.RESIDUE,
                            HeirShareResult.ShareType.ASABAH, FaraidFraction.ZERO, asabahRes.getPrimaryUnits(), 1, true, false, dalil,
                            "চাচাতো ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                            "আসাবা বি-নাফসিহি", "নিকটবর্তী কোনো আসাবা না থাকায় চাচাতো ভাই আসাবা হিসেবে অবশিষ্টাংশ পাবেন।", "Cousin inherits residue as agnate."));
                }
            }
        }

        // ==================== 5. MATHEMATICAL RESOLUTION (Awl, Asabah, Radd) ====================
        FaraidFraction sumFixed = FaraidFraction.ZERO;
        long totalAsabahWeight = 0;
        long baseDenominator = 1;

        for (WorkingShare ws : shares) {
            if (ws.fixedFraction != null && ws.fixedFraction.compareTo(FaraidFraction.ZERO) > 0) {
                sumFixed = sumFixed.add(ws.fixedFraction);
                baseDenominator = FaraidFraction.lcm(baseDenominator, ws.fixedFraction.getDenominator());
            }
            totalAsabahWeight += ws.asabahWeightNum;
        }

        if (baseDenominator < 1) baseDenominator = 1;

        boolean isAwl = false;
        long awlDenominator = baseDenominator;
        boolean isRadd = false;
        String statusMsg;
        List<HeirShareResult> legacyFinalResults = new ArrayList<>();

        double netProperty = Math.max(0.0, input.getTotalProperty());
        String propUnit = input.getPropertyUnit();

        auditLog.append("\n৩. গাণিতিক বিশ্লেষণ (আসলুল মাসআলা ও সমাধান):\n");
        auditLog.append("   • মূল মাসআলা (أصل المسألة - Base Denominator): ").append(BengaliNumberUtil.toBengali(String.valueOf(baseDenominator))).append("\n");
        auditLog.append("   • নির্ধারিত অংশের যোগফল: ").append(sumFixed.toBengaliString()).append("\n");
        if (netProperty > 0) {
            auditLog.append("   • মোট স্থাবর সম্পত্তি / জমি: ").append(BengaliNumberUtil.toBengali(String.format("%.2f", netProperty))).append(" ").append(propUnit).append("\n");
        }

        if (sumFixed.compareTo(FaraidFraction.ONE) > 0) {
            // AWL ENGINE
            FaraidAwlEngine.AwlResult awlRes = FaraidAwlEngine.evaluateAndApplyAwl(shares, net);
            isAwl = awlRes.isAwlApplied();
            awlDenominator = awlRes.getAwlDenominator();
            statusMsg = "আওল সমন্বয় প্রযোজ্য (Awl applied): নির্ধারিত অংশের যোগফল ১ এর অধিক হওয়ায় সবার অংশ আনুপাতিক হারে কমানো হয়েছে।";
            auditLog.append("   • ").append(awlRes.getAuditSummary()).append("\n");

            for (int i = 0; i < awlRes.getAdjustedItems().size(); i++) {
                FaraidAwlEngine.AwlItem as = awlRes.getAdjustedItems().get(i);
                WorkingShare ws = shares.get(i);
                double totalCatAmt = as.getAdjustedTotalAmount();
                double indAmt = as.getAdjustedIndividualAmount();
                double pct = net > 0 ? (totalCatAmt / net) * 100.0 : (as.getAdjustedFraction().toDouble() * 100.0);

                allHeirsClassified.add(new FaraidHeir(
                        ws.relationshipBn, ws.relationship, ws.relationKey,
                        ws.gender, ws.count, false, false, false, "",
                        ws.heirCategory, ws.fixedFraction, true, as.getAdjustedFraction(),
                        totalCatAmt, indAmt, ws.explanation,
                        Collections.singletonList(ws.dalilItem != null ? ws.dalilItem.referenceSource : ""),
                        ws.dalilItem != null ? ws.dalilItem.arabicText : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaPronunciation : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaTranslation : "",
                        ws.dalilItem != null ? ws.dalilItem.referenceSource : "",
                        ws.dalilItem != null ? ws.dalilItem.madhabConsensusBn : "",
                        ws.dalilItem != null ? ws.dalilItem.bangladeshLawNoteBn : ""
                ));

                String formulaBn = as.getAdjustedFraction().toBengaliFormulaString(currency, net, totalCatAmt);
                String formulaEn = as.getAdjustedFraction().toEnglishFormulaString(currency, net, totalCatAmt);
                if (ws.count > 1) {
                    formulaBn += " ÷ " + BengaliNumberUtil.toBengali(String.valueOf(ws.count)) + " = " + currency.format(indAmt, true) + " (প্রতিজন)";
                    formulaEn += " ÷ " + ws.count + " = " + currency.format(indAmt, false) + " (Per Person)";
                }

                legacyFinalResults.add(buildHeirResult(
                        ws, HeirShareResult.ShareType.AWL_REDUCED, as.getAdjustedFraction(),
                        indAmt, totalCatAmt, pct, formulaBn, formulaEn,
                        net, currency, netProperty, propUnit
                ));
            }
        } else if (sumFixed.compareTo(FaraidFraction.ONE) < 0 && totalAsabahWeight > 0) {
            // ASABAH RESIDUE DISTRIBUTION
            FaraidFraction residueFrac = FaraidFraction.ONE.subtract(sumFixed);
            double residueAmount = net * residueFrac.toDouble();
            statusMsg = "আসাবা বণ্টন প্রযোজ্য (Residue distribution): নির্ধারিত অংশের পর অবশিষ্ট অংশ নিকটতম পুরুষ বা অংশীদার আসাবার মাঝে বণ্টন করা হয়েছে।";
            auditLog.append("   • নির্ধারিত অংশ বাদে অবশিষ্ট সম্পদ (Residue): ").append(residueFrac.toBengaliString())
                    .append(" (").append(currency.format(residueAmount, true)).append(")\n");
            auditLog.append("   • মোট আসাবা ইউনিট: ").append(BengaliNumberUtil.toBengali(String.valueOf(totalAsabahWeight))).append("\n");

            for (WorkingShare ws : shares) {
                FaraidFraction finalFrac;
                double totalCatAmt;

                if (ws.fixedFraction != null && ws.fixedFraction.compareTo(FaraidFraction.ZERO) > 0 && ws.asabahWeightNum == 0) {
                    finalFrac = ws.fixedFraction;
                    totalCatAmt = net * finalFrac.toDouble();
                } else if (ws.asabahWeightNum > 0 && ws.fixedFraction.compareTo(FaraidFraction.ZERO) == 0) {
                    FaraidFraction portionOfResidue = new FaraidFraction(ws.asabahWeightNum, totalAsabahWeight);
                    finalFrac = residueFrac.multiply(portionOfResidue);
                    totalCatAmt = residueAmount * portionOfResidue.toDouble();
                } else {
                    // Fixed + Asabah (e.g. Father with daughters)
                    FaraidFraction portionOfResidue = new FaraidFraction(ws.asabahWeightNum, totalAsabahWeight);
                    FaraidFraction asabahPart = residueFrac.multiply(portionOfResidue);
                    finalFrac = ws.fixedFraction.add(asabahPart);
                    totalCatAmt = (net * ws.fixedFraction.toDouble()) + (residueAmount * portionOfResidue.toDouble());
                }

                double indAmt = totalCatAmt / ws.count;
                double pct = net > 0 ? (totalCatAmt / net) * 100.0 : (finalFrac.toDouble() * 100.0);

                allHeirsClassified.add(new FaraidHeir(
                        ws.relationshipBn, ws.relationship, ws.relationKey,
                        ws.gender, ws.count, false, false, false, "",
                        ws.heirCategory, ws.fixedFraction, false, finalFrac,
                        totalCatAmt, indAmt, ws.explanation,
                        Collections.singletonList(ws.dalilItem != null ? ws.dalilItem.referenceSource : ""),
                        ws.dalilItem != null ? ws.dalilItem.arabicText : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaPronunciation : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaTranslation : "",
                        ws.dalilItem != null ? ws.dalilItem.referenceSource : "",
                        ws.dalilItem != null ? ws.dalilItem.madhabConsensusBn : "",
                        ws.dalilItem != null ? ws.dalilItem.bangladeshLawNoteBn : ""
                ));

                String formulaBn = finalFrac.toBengaliFormulaString(currency, net, totalCatAmt);
                String formulaEn = finalFrac.toEnglishFormulaString(currency, net, totalCatAmt);
                if (ws.count > 1) {
                    formulaBn += " ÷ " + BengaliNumberUtil.toBengali(String.valueOf(ws.count)) + " = " + currency.format(indAmt, true) + " (প্রতিজন)";
                    formulaEn += " ÷ " + ws.count + " = " + currency.format(indAmt, false) + " (Per Person)";
                }

                legacyFinalResults.add(buildHeirResult(
                        ws, ws.legacyShareType, finalFrac,
                        indAmt, totalCatAmt, pct, formulaBn, formulaEn,
                        net, currency, netProperty, propUnit
                ));
            }
        } else if (sumFixed.compareTo(FaraidFraction.ONE) < 0 && totalAsabahWeight == 0) {
            // RADD ENGINE
            FaraidRaddEngine.RaddResult raddRes = FaraidRaddEngine.evaluateAndApplyRadd(shares, totalAsabahWeight > 0, net);
            isRadd = raddRes.isRaddApplied();
            statusMsg = "রাদ্দ প্রযোজ্য (Radd applied): আসাবা না থাকায় অবশিষ্ট সম্পদ নির্ধারিত অংশীদারদের মাঝে আনুপাতিক হারে পুনর্বণ্টন করা হয়েছে।";
            auditLog.append("   • ").append(raddRes.getRedistributionExplanation()).append("\n");

            for (int i = 0; i < raddRes.getItems().size(); i++) {
                FaraidRaddEngine.RaddItem rs = raddRes.getItems().get(i);
                WorkingShare ws = shares.get(i);
                double totalCatAmt = rs.getTotalAmount();
                double indAmt = rs.getIndividualAmount();
                double pct = net > 0 ? (totalCatAmt / net) * 100.0 : (rs.getRaddFinalFraction().toDouble() * 100.0);

                allHeirsClassified.add(new FaraidHeir(
                        ws.relationshipBn, ws.relationship, ws.relationKey,
                        ws.gender, ws.count, false, false, false, "",
                        ws.heirCategory, ws.fixedFraction, false, rs.getRaddFinalFraction(),
                        totalCatAmt, indAmt, ws.explanation,
                        Collections.singletonList(ws.dalilItem != null ? ws.dalilItem.referenceSource : ""),
                        ws.dalilItem != null ? ws.dalilItem.arabicText : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaPronunciation : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaTranslation : "",
                        ws.dalilItem != null ? ws.dalilItem.referenceSource : "",
                        ws.dalilItem != null ? ws.dalilItem.madhabConsensusBn : "",
                        ws.dalilItem != null ? ws.dalilItem.bangladeshLawNoteBn : ""
                ));

                String formulaBn = rs.getRaddFinalFraction().toBengaliFormulaString(currency, net, totalCatAmt);
                String formulaEn = rs.getRaddFinalFraction().toEnglishFormulaString(currency, net, totalCatAmt);
                if (ws.count > 1) {
                    formulaBn += " ÷ " + BengaliNumberUtil.toBengali(String.valueOf(ws.count)) + " = " + currency.format(indAmt, true) + " (প্রতিজন)";
                    formulaEn += " ÷ " + ws.count + " = " + currency.format(indAmt, false) + " (Per Person)";
                }

                boolean isSpouse = "wife".equals(ws.id) || "husband".equals(ws.id);
                legacyFinalResults.add(buildHeirResult(
                        ws, isSpouse ? HeirShareResult.ShareType.FIXED_QURANIC : HeirShareResult.ShareType.RADD, rs.getRaddFinalFraction(),
                        indAmt, totalCatAmt, pct, formulaBn, formulaEn,
                        net, currency, netProperty, propUnit
                ));
            }
        } else {
            // EXACT 1 (No Awl, No Radd)
            statusMsg = "সুষম বণ্টন (Exact distribution): নির্ধারিত অংশসমূহের যোগফল সম্পূর্ণ ১।";
            for (WorkingShare ws : shares) {
                double totalCatAmt = net * ws.fixedFraction.toDouble();
                double indAmt = totalCatAmt / ws.count;
                double pct = net > 0 ? (totalCatAmt / net) * 100.0 : (ws.fixedFraction.toDouble() * 100.0);

                allHeirsClassified.add(new FaraidHeir(
                        ws.relationshipBn, ws.relationship, ws.relationKey,
                        ws.gender, ws.count, false, false, false, "",
                        ws.heirCategory, ws.fixedFraction, false, ws.fixedFraction,
                        totalCatAmt, indAmt, ws.explanation,
                        Collections.singletonList(ws.dalilItem != null ? ws.dalilItem.referenceSource : ""),
                        ws.dalilItem != null ? ws.dalilItem.arabicText : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaPronunciation : "",
                        ws.dalilItem != null ? ws.dalilItem.banglaTranslation : "",
                        ws.dalilItem != null ? ws.dalilItem.referenceSource : "",
                        ws.dalilItem != null ? ws.dalilItem.madhabConsensusBn : "",
                        ws.dalilItem != null ? ws.dalilItem.bangladeshLawNoteBn : ""
                ));

                String formulaBn = ws.fixedFraction.toBengaliFormulaString(currency, net, totalCatAmt);
                String formulaEn = ws.fixedFraction.toEnglishFormulaString(currency, net, totalCatAmt);
                if (ws.count > 1) {
                    formulaBn += " ÷ " + BengaliNumberUtil.toBengali(String.valueOf(ws.count)) + " = " + currency.format(indAmt, true) + " (প্রতিজন)";
                    formulaEn += " ÷ " + ws.count + " = " + currency.format(indAmt, false) + " (Per Person)";
                }

                legacyFinalResults.add(buildHeirResult(
                        ws, ws.legacyShareType, ws.fixedFraction,
                        indAmt, totalCatAmt, pct, formulaBn, formulaEn,
                        net, currency, netProperty, propUnit
                ));
            }
        }

        // Add blocked heirs to allHeirsClassified
        for (BlockedHeirInfo bi : blockedHeirs) {
            allHeirsClassified.add(new FaraidHeir(
                    bi.getHeirTitleBn(), bi.getHeirTitleBn(), bi.getHeirTitleBn(),
                    FaraidInput.Gender.MALE, bi.getCount(), true, false, true,
                    bi.getLegalReasonBn(), FaraidHeir.ShareType.BLOCKED,
                    FaraidFraction.ZERO, false, FaraidFraction.ZERO,
                    0.0, 0.0, bi.getLegalReasonBn(),
                    Collections.singletonList(bi.getShariahReference()),
                    "", "", "", bi.getShariahReference(), "", ""
            ));
        }

        long correctedDenominator = isAwl ? awlDenominator : baseDenominator;

        // Overall Quranic & Legal Summary
        String overallSummary = "পবিত্র কুরআনুল কারিমের সূরা আন-নিসার ১১, ১২ ও ১৭৬ নম্বর আয়াত, সহীহ হাদিস (বুখারী ৬৭৩২, ৬৭৩৭; মুসলিম ১৬১৫ক), চার মাযহাবের সিদ্ধান্ত এবং বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ অনুযায়ী এই ফারায়েজ বণ্টন গণনাকৃত হয়েছে।";
        String overallSummaryEn = "This Faraid distribution has been calculated in accordance with Surah An-Nisa verses 11, 12, and 176 of the Holy Quran, Sahih Hadith (Bukhari 6732, 6737; Muslim 1615a), consensus of the four Sunni schools of jurisprudence, and the Bangladesh Muslim Family Laws Ordinance 1961.";

        // Madhab Differences Note
        boolean hasMadhabDiff = false;
        String madhabNote = "";
        String madhabNoteEn = "";
        if (input.isFatherAlive() && input.isPaternalGrandmotherAlive() && !input.isMotherAlive()) {
            hasMadhabDiff = true;
            madhabNote = "মাযহাবগত মতপার্থক্য: হানাফী মাযহাবে পিতার উপস্থিতিতে দাদী মাহজুব হন, কিন্তু শাফেয়ী মাযহাবে দাদী ১/৬ অংশ পান এবং পিতা অবশিষ্টাংশ পান।";
            madhabNoteEn = "Madhhab difference: In the Hanafi school, the paternal grandmother is excluded by the father. In the Shafi'i school, she receives 1/6 share and the father receives the residue.";
        } else if (input.isPaternalGrandfatherAlive() && !input.isFatherAlive() && (fullBrothers > 0 || fullSisterCount > 0 || consBrothers > 0 || consSisters > 0)) {
            hasMadhabDiff = true;
            madhabNote = "হানাফি মাযহাবে দাদা ভাই-বোনদের সম্পূর্ণরূপে বঞ্চিত (মাহজুব) করেন। কিন্তু জমহুর (শাফেয়ী, মালিকী, হাম্বলী) মাযহাবে দাদা ও ভাই-বোনের মাঝে মুকাসামাহ (অংশীদারি বণ্টন) প্রযোজ্য হয়।";
            madhabNoteEn = "In the Hanafi school, the grandfather completely excludes brothers and sisters. In the majority schools (Shafi'i, Maliki, Hanbali), sharing (Muqasamah) applies between grandfather and siblings.";
        }

        FaraidCalculationResult calcResult = new FaraidCalculationResult(
                input, gross, funeral, debt, requestedWasiyyah, validWasiyyah,
                totalDeductions, net, wasiyyahExceeding, wasiyyahToHeir, wasiyyahNotice,
                allHeirsClassified, legacyFinalResults, blockedHeirs,
                baseDenominator, correctedDenominator,
                isAwl, awlDenominator, isRadd, isUmariyyatanCase,
                hasMadhabDiff, madhabNote,
                statusMsg, auditLog.toString(), overallSummary, currency
        );
        calcResult.setOverallQuranicDalilSummaryEn(overallSummaryEn);
        calcResult.setMadhabDifferenceNoteEn(madhabNoteEn);
        calcResult.setGrossProperty(netProperty);
        calcResult.setNetDistributableProperty(netProperty);
        calcResult.setPropertyUnit(propUnit);
        return calcResult;
    }

    private static HeirShareResult buildHeirResult(
            WorkingShare ws, HeirShareResult.ShareType shareType, FaraidFraction finalFrac,
            double indAmt, double totalCatAmt, double pct, String formulaBn, String formulaEn,
            double net, FaraidCurrency currency, double netProperty, String propUnit) {

        double totalProp = netProperty * finalFrac.toDouble();
        double indProp = ws.count > 0 ? (totalProp / ws.count) : 0.0;

        String propUnitBn = propUnit;
        String propUnitEn = propUnit.contains("শতক") || propUnit.contains("ডেসিমাল") ? "Decimal"
                : propUnit.contains("কাঠা") ? "Katha"
                : propUnit.contains("বিঘা") ? "Bigha"
                : propUnit.contains("একর") ? "Acre"
                : propUnit.contains("বর্গফুট") ? "Sq Ft"
                : propUnit;

        if (netProperty > 0) {
            formulaBn += "\nজমি/স্থাবর সম্পত্তি: " + finalFrac.toBengaliString() + " × " +
                    BengaliNumberUtil.toBengali(String.format("%.2f", netProperty)) + " " + propUnitBn + " = " +
                    BengaliNumberUtil.toBengali(String.format("%.2f", totalProp)) + " " + propUnitBn;
            if (ws.count > 1) {
                formulaBn += " (প্রতিজন " + BengaliNumberUtil.toBengali(String.format("%.2f", indProp)) + " " + propUnitBn + ")";
            }

            formulaEn += "\nReal Estate / Land: " + finalFrac.toEnglishString() + " × " +
                    String.format("%.2f", netProperty) + " " + propUnitEn + " = " +
                    String.format("%.2f", totalProp) + " " + propUnitEn;
            if (ws.count > 1) {
                formulaEn += " (Per Person " + String.format("%.2f", indProp) + " " + propUnitEn + ")";
            }
        }

        HeirShareResult res = new HeirShareResult(
                ws.relationshipBn, ws.relationship, ws.count, shareType,
                finalFrac.toBengaliString(), finalFrac.toDouble(),
                indAmt, totalCatAmt, pct,
                ws.originalPrescribedShare, formulaBn, formulaEn,
                currency.format(net, true),
                ws.legalReasonBn, ws.legalReasonEn,
                ws.dalilItem != null ? ws.dalilItem.arabicText : "",
                ws.dalilItem != null ? ws.dalilItem.banglaPronunciation : "",
                ws.dalilItem != null ? ws.dalilItem.banglaTranslation : "",
                ws.dalilItem != null ? ws.dalilItem.referenceSource : "",
                ws.dalilItem != null ? ws.dalilItem.madhabConsensusBn : "",
                ws.dalilItem != null ? ws.dalilItem.bangladeshLawNoteBn : "",
                ws.explanation
        );
        if (ws.dalilItem != null) {
            res.setDalilTranslationEn(ws.dalilItem.englishTranslation);
            res.setDalilReferenceEn(ws.dalilItem.referenceSourceEn);
            res.setDalilPronunciationEn(ws.dalilItem.englishPronunciation);
            res.setMadhabConsensusNoteEn(ws.dalilItem.madhabConsensusEn);
            res.setBangladeshLawNoteEn(ws.dalilItem.bangladeshLawNoteEn);
        }
        res.setTotalPropertyAmount(totalProp);
        res.setIndividualPropertyAmount(indProp);
        res.setPropertyUnit(propUnit);
        return res;
    }
}