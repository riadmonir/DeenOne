package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidSpecialCasesEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Comprehensive Unit Test Suite explicitly verifying all 25 specific cases of Section 19:
 * 1. Only one heir
 * 2. Parents only
 * 3. Mother + father
 * 4. Husband + mother + father
 * 5. Wife + mother + father
 * 6. Husband + daughters
 * 7. Wife + sons
 * 8. Multiple wives
 * 9. Sons and daughters
 * 10. Daughters without sons
 * 11. Parents + children
 * 12. Siblings
 * 13. Maternal siblings
 * 14. Grandchildren
 * 15. Grandparents
 * 16. Kalalah
 * 17. Awl cases
 * 18. Radd cases
 * 19. Complex combinations
 * 20. Blocked heirs
 * 21. Multiple categories of siblings
 * 22. Multiple wives (4 wives)
 * 23. Multiple daughters (3 daughters)
 * 24. Multiple sons (5 sons)
 * 25. Fallback Specialist Verification Safeguard
 */
public class FaraidSpecialCasesTest {

    // 1. Only one heir (Only Son -> 100%)
    @Test
    public void testCase01_OnlyOneHeirSon() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setSonCount(1);

        assertTrue(FaraidSpecialCasesEngine.isSoleHeirCase(input));
        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertEquals(1, res.getHeirShares().size());
        assertEquals(1000000.0, res.getHeirShares().get(0).getTotalCategoryAmount(), 0.01);
    }

    // 2. Parents only (Mother + Father)
    @Test
    public void testCase02_ParentsOnly() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertEquals(2, res.getHeirShares().size());

        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir father = res.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(mother);
        assertNotNull(father);
        assertEquals("1/3", mother.getFixedFractionString());
        assertEquals(200000.0, mother.getFinalAmount(), 0.01);
        assertEquals("2/3", father.getFinalFractionString());
        assertEquals(400000.0, father.getFinalAmount(), 0.01);
    }

    // 3. Mother + father (Same canonical verification)
    @Test
    public void testCase03_MotherAndFather() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(900000.0);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir father = res.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(mother);
        assertNotNull(father);
        assertEquals(300000.0, mother.getFinalAmount(), 0.01);
        assertEquals(600000.0, father.getFinalAmount(), 0.01);
    }

    // 4. Husband + mother + father (Umariyyatan #1)
    @Test
    public void testCase04_HusbandMotherFatherUmariyyatan() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(600000.0);
        input.setHusbandAlive(true);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        assertTrue(FaraidSpecialCasesEngine.isUmariyyatanCase(input));
        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isUmariyyatan());

        FaraidHeir husband = res.getAllHeirs().stream().filter(h -> "husband".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir father = res.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(husband);
        assertNotNull(mother);
        assertNotNull(father);

        assertEquals(300000.0, husband.getFinalAmount(), 0.01); // 1/2 = 3/6
        assertEquals(100000.0, mother.getFinalAmount(), 0.01);  // 1/3 of remainder = 1/6
        assertEquals(200000.0, father.getFinalAmount(), 0.01);  // 2/3 of remainder = 2/6 (1/3)
    }

    // 5. Wife + mother + father (Umariyyatan #2)
    @Test
    public void testCase05_WifeMotherFatherUmariyyatan() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        assertTrue(FaraidSpecialCasesEngine.isUmariyyatanCase(input));
        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isUmariyyatan());

        FaraidHeir wife = res.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir father = res.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(wife);
        assertNotNull(mother);
        assertNotNull(father);

        assertEquals(300000.0, wife.getFinalAmount(), 0.01);   // 1/4 = 3/12
        assertEquals(300000.0, mother.getFinalAmount(), 0.01); // 1/3 of remainder = 3/12 (1/4)
        assertEquals(600000.0, father.getFinalAmount(), 0.01); // 2/3 of remainder = 6/12 (1/2)
    }

    // 6. Husband + daughters (Husband 1/4 + 1 Daughter 1/2 -> Radd gives Daughter 3/4)
    @Test
    public void testCase06_HusbandAndDaughters() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(800000.0);
        input.setHusbandAlive(true);
        input.setDaughterCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isRaddApplied());

        FaraidHeir husband = res.getAllHeirs().stream().filter(h -> "husband".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir daughter = res.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(husband);
        assertNotNull(daughter);
        assertEquals(200000.0, husband.getFinalAmount(), 0.01); // 1/4 fixed, no Radd
        assertEquals(600000.0, daughter.getFinalAmount(), 0.01); // 1/2 fixed + 1/4 surplus = 3/4
    }

    // 7. Wife + sons (Wife 1/8 + Sons Asabah 7/8)
    @Test
    public void testCase07_WifeAndSons() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1600000.0);
        input.setWifeCount(1);
        input.setSonCount(2);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir wife = res.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir son = res.getAllHeirs().stream().filter(h -> "son".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(wife);
        assertNotNull(son);
        assertEquals(200000.0, wife.getFinalAmount(), 0.01);
        assertEquals(1400000.0, son.getFinalAmount(), 0.01);
        assertEquals(700000.0, son.getIndividualAmount(), 0.01);
    }

    // 8. Multiple wives (3 wives sharing 1/8 with child)
    @Test
    public void testCase08_MultipleWives() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(2400000.0);
        input.setWifeCount(3);
        input.setSonCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir wives = res.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(wives);
        assertEquals(300000.0, wives.getFinalAmount(), 0.01); // 1/8 of 2,400,000
        assertEquals(100000.0, wives.getIndividualAmount(), 0.01); // 100,000 each
    }

    // 9. Sons and daughters (2:1 Asabah bil-ghayr)
    @Test
    public void testCase09_SonsAndDaughters() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(500000.0);
        input.setSonCount(2); // 4 units
        input.setDaughterCount(1); // 1 unit -> Total 5 units

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir son = res.getAllHeirs().stream().filter(h -> "son".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir daughter = res.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(son);
        assertNotNull(daughter);
        assertEquals(400000.0, son.getFinalAmount(), 0.01);
        assertEquals(200000.0, son.getIndividualAmount(), 0.01);
        assertEquals(100000.0, daughter.getFinalAmount(), 0.01);
    }

    // 10. Daughters without sons (2 daughters get 2/3)
    @Test
    public void testCase10_DaughtersWithoutSons() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(900000.0);
        input.setDaughterCount(2);
        input.setFullBrotherCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir daughters = res.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(daughters);
        assertEquals("2/3", daughters.getFixedFractionString());
        assertEquals(600000.0, daughters.getFinalAmount(), 0.01);
        assertEquals(300000.0, daughters.getIndividualAmount(), 0.01);
    }

    // 11. Parents + children (Mother 1/6 + Father 1/6 + Son Asabah 2/3)
    @Test
    public void testCase11_ParentsAndChildren() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setSonCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir father = res.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir son = res.getAllHeirs().stream().filter(h -> "son".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(mother);
        assertNotNull(father);
        assertNotNull(son);

        assertEquals(100000.0, mother.getFinalAmount(), 0.01);
        assertEquals(100000.0, father.getFinalAmount(), 0.01);
        assertEquals(400000.0, son.getFinalAmount(), 0.01);
    }

    // 12. Siblings (Full brother & Full sister 2:1)
    @Test
    public void testCase12_Siblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(300000.0);
        input.setFullBrotherCount(1);
        input.setFullSisterCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir fb = res.getAllHeirs().stream().filter(h -> "full_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir fs = res.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(fb);
        assertNotNull(fs);
        assertEquals(200000.0, fb.getFinalAmount(), 0.01);
        assertEquals(100000.0, fs.getFinalAmount(), 0.01);
    }

    // 13. Maternal siblings (Qur'an 4:12 - 1:1 equal gender sharing)
    @Test
    public void testCase13_MaternalSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(600000.0);
        input.setHusbandAlive(true); // 1/2 = 300,000
        input.setUterineBrotherCount(1); // 1/6 = 100,000
        input.setUterineSisterCount(1);  // 1/6 = 100,000
        input.setFullBrotherCount(1);    // Residue = 100,000

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir ub = res.getAllHeirs().stream().filter(h -> "uterine_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir us = res.getAllHeirs().stream().filter(h -> "uterine_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(ub);
        assertNotNull(us);
        assertEquals(100000.0, ub.getFinalAmount(), 0.01);
        assertEquals(100000.0, us.getFinalAmount(), 0.01);
    }

    // 14. Grandchildren (Granddaughter Takmilat al-thuluthayn 1/6)
    @Test
    public void testCase14_Grandchildren() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setDaughterCount(1);
        input.setGranddaughterCount(1);
        input.setFullBrotherCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir gd = res.getAllHeirs().stream().filter(h -> "granddaughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(gd);
        assertEquals("1/6", gd.getFixedFractionString());
        assertEquals(100000.0, gd.getFinalAmount(), 0.01);
    }

    // 15. Grandparents (Paternal Grandfather & Grandmother)
    @Test
    public void testCase15_Grandparents() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setPaternalGrandmotherAlive(true);
        input.setPaternalGrandfatherAlive(true);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir gm = res.getAllHeirs().stream().filter(h -> "paternal_grandmother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir gf = res.getAllHeirs().stream().filter(h -> "paternal_grandfather".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(gm);
        assertNotNull(gf);
        assertEquals(100000.0, gm.getFinalAmount(), 0.01); // 1/6 fixed
        assertEquals(500000.0, gf.getFinalAmount(), 0.01); // Asabah remainder (5/6)
    }

    // 16. Kalalah (Full Sister Kalalah Qur'an 4:176)
    @Test
    public void testCase16_Kalalah() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setFullSisterCount(1);
        input.setPaternalUncleCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir sister = res.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir uncle = res.getAllHeirs().stream().filter(h -> "uncle".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(sister);
        assertNotNull(uncle);
        assertEquals(300000.0, sister.getFinalAmount(), 0.01); // 1/2
        assertEquals(300000.0, uncle.getFinalAmount(), 0.01);  // 1/2
    }

    // 17. Awl cases (Husband 1/2 + 2 Full Sisters 2/3 -> Awl from 6 to 7)
    @Test
    public void testCase17_AwlCase() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(700000.0);
        input.setHusbandAlive(true);
        input.setFullSisterCount(2);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isAwlApplied());
        assertEquals(7, res.getAwlDenominator());

        FaraidHeir husband = res.getAllHeirs().stream().filter(h -> "husband".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir sisters = res.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(husband);
        assertNotNull(sisters);
        assertEquals(300000.0, husband.getFinalAmount(), 0.01); // 3/7
        assertEquals(400000.0, sisters.getFinalAmount(), 0.01); // 4/7
    }

    // 18. Radd cases (Mother 1/3 + 1 Daughter 1/2 -> Radd 4/5 & 1/5)
    @Test
    public void testCase18_RaddCase() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(500000.0);
        input.setMotherAlive(true);
        input.setDaughterCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isRaddApplied());

        FaraidHeir mother = res.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir daughter = res.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(mother);
        assertNotNull(daughter);
        // Mother: 1/6 base -> in Radd (1/6) / (4/6) = 1/4 -> 125,000
        // Daughter: 1/2 base -> in Radd (3/6) / (4/6) = 3/4 -> 375,000
        assertEquals(125000.0, mother.getFinalAmount(), 0.01);
        assertEquals(375000.0, daughter.getFinalAmount(), 0.01);
    }

    // 19. Complex combinations (Wife + Mother + 2 Daughters + Father -> Al-Minbariyyah Awl 24 to 27)
    @Test
    public void testCase19_ComplexCombinationMinbariyyah() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(2700000.0);
        input.setWifeCount(1);       // 1/8 = 3/24
        input.setMotherAlive(true);  // 1/6 = 4/24
        input.setFatherAlive(true);  // 1/6 = 4/24
        input.setDaughterCount(2);   // 2/3 = 16/24 -> Sum = 27/24 (Awl from 24 to 27)

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertTrue(res.isAwlApplied());
        assertEquals(27, res.getAwlDenominator());

        FaraidHeir wife = res.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(wife);
        assertEquals(300000.0, wife.getFinalAmount(), 0.01); // 3/27 of 2,700,000
    }

    // 20. Blocked heirs (Father blocks Brother & Grandfather)
    @Test
    public void testCase20_BlockedHeirs() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setFatherAlive(true);
        input.setPaternalGrandfatherAlive(true);
        input.setFullBrotherCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        assertFalse(res.getBlockedHeirs().isEmpty());
        assertEquals(2, res.getBlockedHeirs().size());
    }

    // 21. Multiple categories of siblings (Full brother + Consanguine brother + Maternal brother)
    @Test
    public void testCase21_MultipleCategoriesOfSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(600000.0);
        input.setFullBrotherCount(1);
        input.setConsanguineBrotherCount(1);
        input.setUterineBrotherCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir ub = res.getAllHeirs().stream().filter(h -> "uterine_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir fb = res.getAllHeirs().stream().filter(h -> "full_brother".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(ub);
        assertNotNull(fb);
        assertEquals(100000.0, ub.getFinalAmount(), 0.01); // 1/6 fixed
        assertEquals(500000.0, fb.getFinalAmount(), 0.01); // Asabah remainder

        // Consanguine brother is blocked by full brother
        assertFalse(res.getBlockedHeirs().isEmpty());
        assertTrue(res.getBlockedHeirs().stream().anyMatch(b -> (b.getLegalReasonBn() != null && b.getLegalReasonBn().contains("সৎ ভাই")) || (b.getLegalReasonEn() != null && b.getLegalReasonEn().contains("Paternal half-brother is excluded"))));
    }

    // 22. Multiple wives (4 wives)
    @Test
    public void testCase22_FourWives() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(3200000.0);
        input.setWifeCount(4);
        input.setSonCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir wives = res.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(wives);
        assertEquals(400000.0, wives.getFinalAmount(), 0.01); // 1/8
        assertEquals(100000.0, wives.getIndividualAmount(), 0.01); // 1/32 each
    }

    // 23. Multiple daughters (3 daughters sharing 2/3)
    @Test
    public void testCase23_MultipleDaughters() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(900000.0);
        input.setDaughterCount(3);
        input.setPaternalUncleCount(1);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir daughters = res.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(daughters);
        assertEquals(600000.0, daughters.getFinalAmount(), 0.01); // 2/3
        assertEquals(200000.0, daughters.getIndividualAmount(), 0.01); // 2/9 each
    }

    // 24. Multiple sons (5 sons sharing residue)
    @Test
    public void testCase24_MultipleSons() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setSonCount(5);

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(input);
        FaraidHeir sons = res.getAllHeirs().stream().filter(h -> "son".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(sons);
        assertEquals(1000000.0, sons.getFinalAmount(), 0.01);
        assertEquals(200000.0, sons.getIndividualAmount(), 0.01);
    }

    // 25. Fallback Specialist Verification Safeguard
    @Test
    public void testCase25_SpecialistVerificationSafeguard() {
        FaraidInput emptyInput = new FaraidInput();
        emptyInput.setTotalEstate(500000.0);

        assertTrue(FaraidSpecialCasesEngine.isSpecialistVerificationRequired(emptyInput));
        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(emptyInput);

        assertTrue(res.getStatusMessageBn().contains(FaraidSpecialCasesEngine.SPECIALIST_VERIFICATION_MSG_EN));
        assertTrue(res.getOverallQuranicDalilSummary().contains(FaraidSpecialCasesEngine.SPECIALIST_VERIFICATION_MSG_EN));
    }
}