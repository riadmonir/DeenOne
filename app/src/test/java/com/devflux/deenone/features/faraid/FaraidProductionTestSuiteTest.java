package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive Production Test Suite for Classical Sunni Faraid Calculation (Requirement 30).
 *
 * Verifies all 20 required classical inheritance scenarios and strictly asserts the 5 core invariants:
 * 1. Sum of shares equals exactly 100% of the distributable net estate.
 * 2. No negative amounts are allowed.
 * 3. No eligible heir disappears without an explanation.
 * 4. No blocked heir receives money.
 * 5. No money is created or destroyed by rounding.
 */
public class FaraidProductionTestSuiteTest {

    private static final double EPSILON = 0.05; // 5 paisa max tolerance for currency distribution

    /**
     * Enforces the 5 invariant rules on every FaraidCalculationResult.
     */
    private void assertFaraidInvariants(FaraidCalculationResult result) {
        assertNotNull("Result must not be null", result);
        List<HeirShareResult> shares = result.getHeirShares();
        assertNotNull("Heir shares list must not be null", shares);
        assertFalse("Eligible heir shares must not be empty for a valid case", shares.isEmpty());

        double netEstate = result.getNetDistributableEstate();
        double sumDistributed = 0.0;
        double sumPercentage = 0.0;
        double sumFractionsNumeric = 0.0;

        for (HeirShareResult heir : shares) {
            // Rule 2: No negative amounts allowed
            assertTrue("Heir amount must be non-negative: " + heir.getRelationTitleBn(), heir.getTotalCategoryAmount() >= 0.0);
            assertTrue("Heir percentage must be non-negative", heir.getPercentage() >= 0.0);

            // Rule 3: No eligible heir may disappear without an explanation
            assertNotNull("Heir must have Bengali explanation", heir.getCalculationFormulaBn());
            assertFalse("Heir explanation must not be empty", heir.getCalculationFormulaBn().trim().isEmpty());
            assertNotNull("Heir must have Dalil reference", heir.getDalilReference());

            sumDistributed += heir.getTotalCategoryAmount();
            sumPercentage += heir.getPercentage();
            sumFractionsNumeric += heir.getShareFractionNumeric();
        }

        // Rule 1 & 5: Sum of all final shares must equal 100% and conserve money exactly
        assertEquals("Total distributed amount must equal net estate exactly", netEstate, sumDistributed, EPSILON);
        assertEquals("Total percentage must equal 100%", 100.0, sumPercentage, 0.2);
        assertEquals("Total rational fraction sum must equal 1.0 exactly", 1.0, sumFractionsNumeric, 0.001);

        // Rule 4: No blocked heir may receive money
        List<BlockedHeirInfo> blocked = result.getBlockedHeirs();
        for (BlockedHeirInfo b : blocked) {
            assertNotNull("Blocked heir must have Bengali title", b.getHeirTitleBn());
            assertNotNull("Blocked heir must have legal reason", b.getLegalReasonBn());
            assertNotNull("Blocked heir must have Shariah reference", b.getShariahReference());

            for (HeirShareResult heir : shares) {
                assertNotEquals("Blocked heir must not appear as an eligible receiving heir",
                        b.getHeirTitleBn(), heir.getRelationTitleBn());
            }
        }

        // Transparency: Audit log must be detailed and non-empty
        assertNotNull("Step-by-step audit log must be populated", result.getStepByStepAuditLog());
        assertFalse("Step-by-step audit log must not be empty", result.getStepByStepAuditLog().trim().isEmpty());
    }

    // 1. Husband + Mother + Father (Umariyatan case)
    @Test
    public void test01_Husband_Mother_Father() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(600000.0);
        input.setHusbandAlive(true);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(300000.0, result.getShareFor("husband").getTotalCategoryAmount(), EPSILON); // 1/2
        assertEquals(100000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON);  // 1/6
        assertEquals(200000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON);  // 1/3 (Residue)
    }

    // 2. Wife + Mother + Father (Umariyatan case)
    @Test
    public void test02_Wife_Mother_Father() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(300000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON);   // 1/4
        assertEquals(300000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON); // 1/4 (1/3 of remainder)
        assertEquals(600000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON); // 1/2 (Residue)
    }

    // 3. Husband + One Daughter + Mother + Father (Awl case: 12 -> 13)
    @Test
    public void test03_Husband_OneDaughter_Mother_Father() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(1300000.0);
        input.setHusbandAlive(true);
        input.setDaughterCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);
        assertTrue(result.isAwlApplied());
        assertEquals(13, result.getAwlDenominator());

        assertEquals(300000.0, result.getShareFor("husband").getTotalCategoryAmount(), EPSILON);  // 3/13
        assertEquals(600000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON); // 6/13
        assertEquals(200000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON);   // 2/13
        assertEquals(200000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON);   // 2/13
    }

    // 4. Wife + One Son + Mother + Father
    @Test
    public void test04_Wife_OneSon_Mother_Father() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(2400000.0);
        input.setWifeCount(1);
        input.setSonCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(300000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON);   // 1/8
        assertEquals(400000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON); // 1/6
        assertEquals(400000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON); // 1/6
        assertEquals(1300000.0, result.getShareFor("son").getTotalCategoryAmount(), EPSILON);   // 13/24 (Residue)
    }

    // 5. Wife + Two Sons + Three Daughters
    @Test
    public void test05_Wife_TwoSons_ThreeDaughters() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(800000.0);
        input.setWifeCount(1);
        input.setSonCount(2);
        input.setDaughterCount(3);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(100000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON);     // 1/8
        assertEquals(400000.0, result.getShareFor("son").getTotalCategoryAmount(), EPSILON);      // 4/8 (200k each)
        assertEquals(200000.0, result.getShareFor("son").getIndividualAmount(), EPSILON);
        assertEquals(300000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON); // 3/8 (100k each)
        assertEquals(100000.0, result.getShareFor("daughter").getIndividualAmount(), EPSILON);
    }

    // 6. Husband + Two Daughters + Father (Awl case: 12 -> 13)
    @Test
    public void test06_Husband_TwoDaughters_Father() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(1300000.0);
        input.setHusbandAlive(true);
        input.setDaughterCount(2);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);
        assertTrue(result.isAwlApplied());
        assertEquals(13, result.getAwlDenominator());

        assertEquals(300000.0, result.getShareFor("husband").getTotalCategoryAmount(), EPSILON);  // 3/13
        assertEquals(800000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON); // 8/13 (400k each)
        assertEquals(400000.0, result.getShareFor("daughter").getIndividualAmount(), EPSILON);
        assertEquals(200000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON);   // 2/13
    }

    // 7. Mother + Father Only
    @Test
    public void test07_Mother_Father_Only() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(900000.0);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(300000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON); // 1/3
        assertEquals(600000.0, result.getShareFor("father").getTotalCategoryAmount(), EPSILON); // 2/3 (Residue)
    }

    // 8. One Daughter Only (Radd case: 1/2 -> 1/1)
    @Test
    public void test08_OneDaughter_Only() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(500000.0);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(500000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON);
        assertEquals(1.0, result.getShareFor("daughter").getShareFractionNumeric(), 0.001);
    }

    // 9. Two Daughters Without Sons (Radd case: 2/3 -> 1/1)
    @Test
    public void test09_TwoDaughters_WithoutSons() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setDaughterCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(600000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON);
        assertEquals(300000.0, result.getShareFor("daughter").getIndividualAmount(), EPSILON);
    }

    // 10. Multiple Wives + Children
    @Test
    public void test10_MultipleWives_Children() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(3200000.0);
        input.setWifeCount(4);
        input.setSonCount(1);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(400000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON); // 1/8
        assertEquals(100000.0, result.getShareFor("wife").getIndividualAmount(), EPSILON);     // 100k per wife
        assertEquals(2800000.0 * (2.0 / 3.0), result.getShareFor("son").getTotalCategoryAmount(), EPSILON);
        assertEquals(2800000.0 * (1.0 / 3.0), result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON);
    }

    // 11. Full Siblings (Kalalah)
    @Test
    public void test11_FullSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setFullBrotherCount(2);
        input.setFullSisterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(480000.0, result.getShareFor("full_brother").getTotalCategoryAmount(), EPSILON); // 4/5 (240k each)
        assertEquals(240000.0, result.getShareFor("full_brother").getIndividualAmount(), EPSILON);
        assertEquals(120000.0, result.getShareFor("full_sister").getTotalCategoryAmount(), EPSILON);  // 1/5
    }

    // 12. Maternal Siblings (Uterine: 1:1 parity)
    @Test
    public void test12_MaternalSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setUterineBrotherCount(1);
        input.setUterineSisterCount(1);
        input.setFullBrotherCount(1); // Takes residue

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(100000.0, result.getShareFor("uterine_brother").getTotalCategoryAmount(), EPSILON); // 1/6
        assertEquals(100000.0, result.getShareFor("uterine_sister").getTotalCategoryAmount(), EPSILON);  // 1/6
        assertEquals(400000.0, result.getShareFor("full_brother").getTotalCategoryAmount(), EPSILON);     // 2/3
    }

    // 13. Paternal Siblings (Consanguine)
    @Test
    public void test13_PaternalSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setConsanguineBrotherCount(1);
        input.setConsanguineSisterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(400000.0, result.getShareFor("consanguine_brother").getTotalCategoryAmount(), EPSILON); // 2/3
        assertEquals(200000.0, result.getShareFor("consanguine_sister").getTotalCategoryAmount(), EPSILON);  // 1/3
    }

    // 14. Grandchildren (Grandson + Granddaughter)
    @Test
    public void test14_Grandchildren() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setGrandsonCount(1);
        input.setGranddaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(400000.0, result.getShareFor("grandson").getTotalCategoryAmount(), EPSILON);      // 2/3
        assertEquals(200000.0, result.getShareFor("granddaughter").getTotalCategoryAmount(), EPSILON); // 1/3
    }

    // 15. Blocked Brother (Blocked by Son)
    @Test
    public void test15_BlockedBrother() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setSonCount(1);
        input.setFullBrotherCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertNull("Blocked brother must not receive shares", result.getShareFor("full_brother"));
        assertEquals(600000.0, result.getShareFor("son").getTotalCategoryAmount(), EPSILON);
        assertFalse(result.getBlockedHeirs().isEmpty());
    }

    // 16. Blocked Son's Son (Grandson blocked by direct Son)
    @Test
    public void test16_BlockedSonsSon() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setSonCount(1);
        input.setGrandsonCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertNull("Blocked grandson must not receive shares", result.getShareFor("grandson"));
        assertEquals(600000.0, result.getShareFor("son").getTotalCategoryAmount(), EPSILON);
        assertFalse(result.getBlockedHeirs().isEmpty());
    }

    // 17. Awl Case (Husband + 2 Full Sisters: 6 -> 7)
    @Test
    public void test17_AwlCase() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(700000.0);
        input.setHusbandAlive(true);
        input.setFullSisterCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);
        assertTrue(result.isAwlApplied());
        assertEquals(7, result.getAwlDenominator());

        assertEquals(300000.0, result.getShareFor("husband").getTotalCategoryAmount(), EPSILON);     // 3/7
        assertEquals(400000.0, result.getShareFor("full_sister").getTotalCategoryAmount(), EPSILON); // 4/7 (200k each)
    }

    // 18. Radd Case (Mother + 1 Daughter: 6 -> 4)
    @Test
    public void test18_RaddCase() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(400000.0);
        input.setMotherAlive(true);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);
        assertTrue(result.isRaddApplied());

        assertEquals(100000.0, result.getShareFor("mother").getTotalCategoryAmount(), EPSILON);   // 1/4
        assertEquals(300000.0, result.getShareFor("daughter").getTotalCategoryAmount(), EPSILON); // 3/4
    }

    // 19. Kalalah Case (Wife + Full Brother)
    @Test
    public void test19_KalalahCase() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setWifeCount(1);
        input.setFullBrotherCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(300000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON);         // 1/4
        assertEquals(900000.0, result.getShareFor("full_brother").getTotalCategoryAmount(), EPSILON); // 3/4 (Residue)
    }

    // 20. Debt + Wasiyyah + Inheritance
    @Test
    public void test20_DebtWasiyyahInheritance() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setCurrency(FaraidCurrency.BDT);
        input.setTotalEstate(1000000.0);
        input.setFuneralExpense(50000.0);
        input.setDebtAmount(150000.0);
        input.setWasiyyahAmount(200000.0); // <= 1/3 of 800,000 (266,666.67)
        input.setWifeCount(1);
        input.setSonCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertFaraidInvariants(result);

        assertEquals(1000000.0, result.getGrossEstate(), EPSILON);
        assertEquals(50000.0, result.getFuneralExpense(), EPSILON);
        assertEquals(150000.0, result.getDebtAmount(), EPSILON);
        assertEquals(200000.0, result.getValidWasiyyah(), EPSILON);
        assertEquals(600000.0, result.getNetDistributableEstate(), EPSILON);

        assertEquals(75000.0, result.getShareFor("wife").getTotalCategoryAmount(), EPSILON); // 1/8 of 600,000
        assertEquals(525000.0, result.getShareFor("son").getTotalCategoryAmount(), EPSILON); // 7/8 of 600,000
    }
}