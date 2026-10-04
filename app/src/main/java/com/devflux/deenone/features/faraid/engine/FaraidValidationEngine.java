package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.FaraidValidationResult;

import java.util.ArrayList;
import java.util.List;

public class FaraidValidationEngine {

    public static FaraidValidationResult validate(FaraidInput input) {
        List<FaraidValidationResult.ValidationError> errorList = new ArrayList<>();
        List<String> warningsBn = new ArrayList<>();
        List<String> warningsEn = new ArrayList<>();

        if (input == null) {
            errorList.add(new FaraidValidationResult.ValidationError("NO_INPUT", "ইনপুট তথ্য খালি।", "Input cannot be null."));
            return new FaraidValidationResult(false, errorList, warningsBn, warningsEn);
        }

        if (input.getTotalEstate() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_FINANCIAL", "মোট ত্যাজ্য সম্পত্তি ঋণাত্মক হতে পারে না।", "Total estate cannot be negative."));
        }

        if (input.getTotalProperty() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_PROPERTY", "স্থাবর সম্পত্তি বা জমির পরিমাণ ঋণাত্মক হতে পারে না।", "Total property cannot be negative."));
        }

        if (input.getFuneralExpense() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_FINANCIAL", "কাফন-দাফন খরচ ঋণাত্মক হতে পারে না।", "Funeral expense cannot be negative."));
        }

        if (input.getDebtAmount() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_FINANCIAL", "ঋণের পরিমাণ ঋণাত্মক হতে পারে না।", "Debt amount cannot be negative."));
        }

        if (input.getWasiyyahAmount() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_FINANCIAL", "অসিয়তের পরিমাণ ঋণাত্মক হতে পারে না।", "Wasiyyah amount cannot be negative."));
        }

        double totalDeductions = Math.max(0, input.getFuneralExpense()) + Math.max(0, input.getDebtAmount()) + Math.max(0, input.getWasiyyahAmount());
        if (totalDeductions > input.getTotalEstate() && input.getTotalEstate() > 0) {
            warningsBn.add("সতর্কতা: দেনা ও অসিয়তের পরিমাণ মোট সম্পত্তিকে অতিক্রম করেছে।");
            warningsEn.add("Warning: Debts and bequests exceed total gross estate.");
        }

        if (input.getDeceasedGender() == FaraidInput.Gender.MALE && input.getWifeCount() > 4) {
            errorList.add(new FaraidValidationResult.ValidationError("EXCEEDS_MAX_WIVES", "ইসলামী শরীয়তে একসাথে ৪ জনের বেশি স্ত্রী থাকা বৈধ নয়।", "Wife count cannot exceed 4."));
        }

        if (input.getDeceasedGender() == FaraidInput.Gender.FEMALE && input.getWifeCount() > 0) {
            errorList.add(new FaraidValidationResult.ValidationError("IMPOSSIBLE_SPOUSE", "নারী মৃতের ক্ষেত্রে স্ত্রী থাকতে পারে না।", "Female deceased cannot have wives."));
        }

        if (input.getDeceasedGender() == FaraidInput.Gender.MALE && input.isHusbandAlive()) {
            errorList.add(new FaraidValidationResult.ValidationError("IMPOSSIBLE_SPOUSE", "পুরুষ মৃতের ক্ষেত্রে স্বামী থাকতে পারে না।", "Male deceased cannot have a husband."));
        }

        // Check negative counts
        if (input.getSonCount() < 0 || input.getDaughterCount() < 0 || input.getWifeCount() < 0 ||
                input.getGrandsonCount() < 0 || input.getGranddaughterCount() < 0 ||
                input.getFullBrotherCount() < 0 || input.getFullSisterCount() < 0 ||
                input.getConsanguineBrotherCount() < 0 || input.getConsanguineSisterCount() < 0 ||
                input.getUterineBrotherCount() < 0 || input.getUterineSisterCount() < 0 ||
                input.getFullNephewCount() < 0 || input.getConsanguineNephewCount() < 0 ||
                input.getFullPaternalUncleCount() < 0 || input.getConsanguinePaternalUncleCount() < 0 ||
                input.getFullCousinCount() < 0 || input.getConsanguineCousinCount() < 0) {
            errorList.add(new FaraidValidationResult.ValidationError("NEGATIVE_COUNT", "ওয়ারিশদের সংখ্যা ঋণাত্মক হতে পারে না।", "Heir count cannot be negative."));
        }

        // Check zero heirs
        int totalLivingHeirs = (input.getWifeCount() > 0 ? input.getWifeCount() : 0) +
                (input.isHusbandAlive() ? 1 : 0) +
                (input.isFatherAlive() ? 1 : 0) +
                (input.isMotherAlive() ? 1 : 0) +
                Math.max(0, input.getSonCount()) +
                Math.max(0, input.getDaughterCount()) +
                Math.max(0, input.getGrandsonCount()) +
                Math.max(0, input.getGranddaughterCount()) +
                (input.isPaternalGrandfatherAlive() ? 1 : 0) +
                (input.isPaternalGrandmotherAlive() ? 1 : 0) +
                (input.isMaternalGrandmotherAlive() ? 1 : 0) +
                Math.max(0, input.getFullBrotherCount()) +
                Math.max(0, input.getFullSisterCount()) +
                Math.max(0, input.getConsanguineBrotherCount()) +
                Math.max(0, input.getConsanguineSisterCount()) +
                Math.max(0, input.getUterineBrotherCount()) +
                Math.max(0, input.getUterineSisterCount()) +
                Math.max(0, input.getFullNephewCount()) +
                Math.max(0, input.getConsanguineNephewCount()) +
                Math.max(0, input.getFullPaternalUncleCount()) +
                Math.max(0, input.getConsanguinePaternalUncleCount()) +
                Math.max(0, input.getFullCousinCount()) +
                Math.max(0, input.getConsanguineCousinCount());

        if (totalLivingHeirs == 0 && errorList.stream().noneMatch(e -> "NEGATIVE_COUNT".equals(e.getErrorCode()))) {
            errorList.add(new FaraidValidationResult.ValidationError("NO_HEIRS_ENTERED", "কমপক্ষে একজন জীবিত ওয়ারিশ নির্বাচন করতে হবে।", "At least one living heir must be entered."));
        }

        boolean valid = errorList.isEmpty();
        return new FaraidValidationResult(valid, errorList, warningsBn, warningsEn);
    }
}