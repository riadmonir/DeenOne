package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidValidationEngine;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.FaraidValidationResult;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidValidationEngineTest {

    @Test
    public void testMaleDeceasedCannotHaveHusband() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setHusbandAlive(true);
        input.setSonCount(1);
        input.setTotalEstate(100000.0);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> "IMPOSSIBLE_SPOUSE".equals(e.getErrorCode())));
    }

    @Test
    public void testFemaleDeceasedCannotHaveWives() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setWifeCount(1);
        input.setDaughterCount(1);
        input.setTotalEstate(100000.0);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> "IMPOSSIBLE_SPOUSE".equals(e.getErrorCode())));
    }

    @Test
    public void testWifeCountCannotExceedFour() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setWifeCount(5);
        input.setSonCount(1);
        input.setTotalEstate(100000.0);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> "EXCEEDS_MAX_WIVES".equals(e.getErrorCode())));
    }

    @Test
    public void testNegativeFinancialValuesRejected() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(-5000.0);
        input.setFuneralExpense(-1000.0);
        input.setDebtAmount(-200.0);
        input.setWasiyyahAmount(-50.0);
        input.setSonCount(1);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertEquals(4, result.getErrors().size());
    }

    @Test
    public void testNegativeHeirCountsRejected() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(500000.0);
        input.setSonCount(-1);
        input.setDaughterCount(-2);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> "NEGATIVE_COUNT".equals(e.getErrorCode())));
    }

    @Test
    public void testZeroLivingHeirsRejected() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(500000.0);
        // No heirs entered

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> "NO_HEIRS_ENTERED".equals(e.getErrorCode())));
    }

    @Test
    public void testValidInputPassesValidation() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setFuneralExpense(20000.0);
        input.setDebtAmount(50000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setSonCount(2);
        input.setDaughterCount(1);

        FaraidValidationResult result = FaraidValidationEngine.validate(input);
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }
}