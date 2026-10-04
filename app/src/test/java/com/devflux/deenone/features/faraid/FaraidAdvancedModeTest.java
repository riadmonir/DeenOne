package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidAdvancedModeEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidAdvancedModeTest {

    @Test
    public void testAdvancedModeAwlAndSahmDiagnostics() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(1300000.0);
        input.setHusbandAlive(true);
        input.setDaughterCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        FaraidAdvancedModeEngine.AdvancedFaraidAnalysis analysis = FaraidAdvancedModeEngine.analyze(result);

        assertNotNull(analysis);
        assertFalse(analysis.isKalalah());
        assertTrue(analysis.isAwlApplied());
        assertEquals(12, analysis.getAslAlMasalah());
        assertEquals(13, analysis.getAwlDenominator());

        assertNotNull(analysis.getSahmUnits());
        assertEquals(4, analysis.getSahmUnits().size());

        // Verify Sahm unit sums match Awl denominator 13
        long sumSahm = 0;
        for (FaraidAdvancedModeEngine.AdvancedSahmUnit unit : analysis.getSahmUnits()) {
            sumSahm += unit.getSahmUnits();
            assertNotNull(unit.getArabicTitle());
            assertNotNull(unit.getClassificationBn());
            assertNotNull(unit.getOriginalFraction());
            assertNotNull(unit.getAdjustedFraction());
        }
        assertEquals(13, sumSahm);
    }

    @Test
    public void testAdvancedModeKalalahStatus() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setFullBrotherCount(1);
        input.setFullSisterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        FaraidAdvancedModeEngine.AdvancedFaraidAnalysis analysis = FaraidAdvancedModeEngine.analyze(result);

        assertTrue("Should be identified as Kalalah state", analysis.isKalalah());
        assertTrue(analysis.getKalalahExplanationBn().contains("কালালাহ"));
    }
}