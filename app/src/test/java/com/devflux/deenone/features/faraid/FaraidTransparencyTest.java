package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidTransparencyTest {

    @Test
    public void testEveryCalculatedHeirHasFullTransparencyFields() {
        // Mother + Father + 1 Son + 1 Daughter
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setSonCount(1);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(4, result.getHeirShares().size());

        for (HeirShareResult heir : result.getHeirShares()) {
            // 1. Share
            assertNotNull(heir.getShareFractionLabel());
            assertFalse(heir.getShareFractionLabel().trim().isEmpty());

            // 2. Percentage
            assertTrue(heir.getPercentage() > 0.0);

            // 3. Amount
            assertTrue(heir.getTotalCategoryAmount() > 0.0);
            assertTrue(heir.getIndividualAmount() > 0.0);

            // 4. Reason
            assertNotNull(heir.getLegalReasonBn());
            assertFalse(heir.getLegalReasonBn().trim().isEmpty());

            // 5. Calculation formula
            assertNotNull(heir.getCalculationFormulaBn());
            assertTrue(heir.getCalculationFormulaBn().contains("×"));
            assertTrue(heir.getCalculationFormulaBn().contains("="));

            // 6. Islamic reference
            assertNotNull(heir.getDalilReference());
            assertFalse(heir.getDalilReference().trim().isEmpty());
            assertNotNull(heir.getArabicDalil());
            assertFalse(heir.getArabicDalil().trim().isEmpty());
        }

        // Specifically test Mother breakdown
        HeirShareResult mother = result.getHeirShares().stream().filter(h -> "mother".equals(h.getRelationKey())).findFirst().orElse(null);
        assertNotNull(mother);
        assertEquals(0.16666666666666666, mother.getShareFractionNumeric(), 0.001);
        assertEquals(16.67, mother.getPercentage(), 0.01);
        assertEquals(100000.0, mother.getTotalCategoryAmount(), 0.01);
        assertNotNull(mother.getLegalReasonBn());
        assertFalse(mother.getLegalReasonBn().trim().isEmpty());
        assertNotNull(mother.getCalculationFormulaBn());
        assertTrue(mother.getCalculationFormulaBn().contains("="));
        assertNotNull(mother.getDalilReference());
        assertFalse(mother.getDalilReference().trim().isEmpty());
    }
}