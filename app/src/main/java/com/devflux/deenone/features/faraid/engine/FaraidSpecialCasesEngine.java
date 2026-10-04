package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidInput;

/**
 * Dedicated Special Cases & Fallback Specialist Safeguard Engine (Requirement 19).
 */
public class FaraidSpecialCasesEngine {

    public static final String SPECIALIST_VERIFICATION_MSG_EN =
            "This inheritance case requires verification by a qualified Islamic inheritance specialist.";

    public static final String SPECIALIST_VERIFICATION_MSG_BN =
            "এই ফারায়েজ মাসআলাটি একজন বিজ্ঞ ইসলামী ফারায়েজ বিশেষজ্ঞ (মুফতি/বিজ্ঞ আলেম) দ্বারা যাচাই করা আবশ্যক।";

    public static boolean isSpecialistVerificationRequired(FaraidInput input) {
        if (input == null) return true;
        int totalHeirCount = 0;
        if (input.getWifeCount() > 0 || input.isHusbandAlive()) totalHeirCount += Math.max(1, input.getWifeCount());
        if (input.isFatherAlive()) totalHeirCount++;
        if (input.isMotherAlive()) totalHeirCount++;
        if (input.getSonCount() > 0) totalHeirCount += input.getSonCount();
        if (input.getDaughterCount() > 0) totalHeirCount += input.getDaughterCount();
        if (input.getGrandsonCount() > 0) totalHeirCount += input.getGrandsonCount();
        if (input.getGranddaughterCount() > 0) totalHeirCount += input.getGranddaughterCount();
        if (input.isPaternalGrandfatherAlive()) totalHeirCount++;
        if (input.isPaternalGrandmotherAlive()) totalHeirCount++;
        if (input.isMaternalGrandmotherAlive()) totalHeirCount++;
        if (input.getFullBrotherCount() > 0) totalHeirCount += input.getFullBrotherCount();
        if (input.getFullSisterCount() > 0) totalHeirCount += input.getFullSisterCount();
        if (input.getConsanguineBrotherCount() > 0) totalHeirCount += input.getConsanguineBrotherCount();
        if (input.getConsanguineSisterCount() > 0) totalHeirCount += input.getConsanguineSisterCount();
        if (input.getUterineBrotherCount() > 0) totalHeirCount += input.getUterineBrotherCount();
        if (input.getUterineSisterCount() > 0) totalHeirCount += input.getUterineSisterCount();
        if (input.getFullNephewCount() > 0) totalHeirCount += input.getFullNephewCount();
        if (input.getConsanguineNephewCount() > 0) totalHeirCount += input.getConsanguineNephewCount();
        if (input.getFullPaternalUncleCount() > 0) totalHeirCount += input.getFullPaternalUncleCount();
        if (input.getConsanguinePaternalUncleCount() > 0) totalHeirCount += input.getConsanguinePaternalUncleCount();
        if (input.getFullCousinCount() > 0) totalHeirCount += input.getFullCousinCount();
        if (input.getConsanguineCousinCount() > 0) totalHeirCount += input.getConsanguineCousinCount();

        return totalHeirCount == 0;
    }

    public static boolean isSoleHeirCase(FaraidInput input) {
        if (input == null) return false;
        int count = 0;
        if (input.getWifeCount() > 0 || input.isHusbandAlive()) count += Math.max(1, input.getWifeCount());
        if (input.isFatherAlive()) count++;
        if (input.isMotherAlive()) count++;
        if (input.getSonCount() > 0) count += input.getSonCount();
        if (input.getDaughterCount() > 0) count += input.getDaughterCount();
        if (input.getGrandsonCount() > 0) count += input.getGrandsonCount();
        if (input.getGranddaughterCount() > 0) count += input.getGranddaughterCount();
        if (input.isPaternalGrandfatherAlive()) count++;
        if (input.isPaternalGrandmotherAlive()) count++;
        if (input.isMaternalGrandmotherAlive()) count++;
        if (input.getFullBrotherCount() > 0) count += input.getFullBrotherCount();
        if (input.getFullSisterCount() > 0) count += input.getFullSisterCount();
        if (input.getConsanguineBrotherCount() > 0) count += input.getConsanguineBrotherCount();
        if (input.getConsanguineSisterCount() > 0) count += input.getConsanguineSisterCount();
        if (input.getUterineBrotherCount() > 0) count += input.getUterineBrotherCount();
        if (input.getUterineSisterCount() > 0) count += input.getUterineSisterCount();
        if (input.getFullNephewCount() > 0) count += input.getFullNephewCount();
        if (input.getConsanguineNephewCount() > 0) count += input.getConsanguineNephewCount();
        if (input.getFullPaternalUncleCount() > 0) count += input.getFullPaternalUncleCount();
        if (input.getConsanguinePaternalUncleCount() > 0) count += input.getConsanguinePaternalUncleCount();
        if (input.getFullCousinCount() > 0) count += input.getFullCousinCount();
        if (input.getConsanguineCousinCount() > 0) count += input.getConsanguineCousinCount();
        return count == 1;
    }

    public static boolean isUmariyyatanCase(FaraidInput input) {
        if (input == null) return false;
        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();
        boolean hasDesc = (sons > 0 || daughters > 0 || grandsons > 0 || granddaughters > 0);

        int totalSiblings = input.getFullBrotherCount() + input.getFullSisterCount() +
                            input.getConsanguineBrotherCount() + input.getConsanguineSisterCount() +
                            input.getUterineBrotherCount() + input.getUterineSisterCount();

        boolean hasSpouse = (input.getDeceasedGender() == FaraidInput.Gender.MALE && input.getWifeCount() > 0) ||
                            (input.getDeceasedGender() == FaraidInput.Gender.FEMALE && input.isHusbandAlive());

        return input.isMotherAlive() && input.isFatherAlive() && hasSpouse && !hasDesc && totalSiblings <= 1;
    }

    public static boolean isMushtarakahCase(FaraidInput input) {
        if (input == null) return false;
        if (input.getDeceasedGender() != FaraidInput.Gender.FEMALE || !input.isHusbandAlive()) return false;
        if (!input.isMotherAlive()) return false;
        int uSiblings = input.getUterineBrotherCount() + input.getUterineSisterCount();
        int fullBrothers = input.getFullBrotherCount();
        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();
        boolean hasDesc = (sons > 0 || daughters > 0 || grandsons > 0 || granddaughters > 0);
        return (!hasDesc && !input.isFatherAlive() && uSiblings >= 2 && fullBrothers >= 1);
    }

    public static boolean isAkdariyyahCase(FaraidInput input) {
        if (input == null) return false;
        if (input.getDeceasedGender() != FaraidInput.Gender.FEMALE || !input.isHusbandAlive()) return false;
        if (!input.isMotherAlive() || input.isFatherAlive()) return false;
        if (!input.isPaternalGrandfatherAlive()) return false;
        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();
        boolean hasDesc = (sons > 0 || daughters > 0 || grandsons > 0 || granddaughters > 0);
        int fullSisters = input.getFullSisterCount();
        int fullBrothers = input.getFullBrotherCount();
        return (!hasDesc && fullSisters == 1 && fullBrothers == 0);
    }

    public static boolean isMinbariyyahCase(FaraidInput input) {
        if (input == null) return false;
        if (input.getDeceasedGender() != FaraidInput.Gender.MALE || input.getWifeCount() < 1) return false;
        return (input.isFatherAlive() && input.isMotherAlive() && input.getDaughterCount() == 2 && input.getSonCount() == 0);
    }

    public static boolean isDinariyyahCase(FaraidInput input) {
        if (input == null) return false;
        if (input.getDeceasedGender() != FaraidInput.Gender.MALE || input.getWifeCount() != 1) return false;
        if (!input.isMotherAlive() || input.getDaughterCount() != 2) return false;
        return (input.getFullBrotherCount() == 12 && input.getFullSisterCount() == 1);
    }
}