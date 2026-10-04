package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests verifying Classical Sunni Islamic Inheritance (Faraid) Engine.
 * Tests cover:
 * - 5-Stage Estate Processing Order
 * - Spouse, Parents, Grandparents, Children, Grandchildren
 * - 6 Separate Unmerged Sibling Categories (Full Brother, Full Sister, Paternal Brother, Paternal Sister, Maternal Brother, Maternal Sister)
 * - Qur'an 4:12 Maternal Half-Sibling equal 1:1 rules
 * - Qur'an 4:176 Kalalah Full & Paternal Sibling rules
 * - Hajb (Blocking), Awl, Asabah, Radd
 * - Bangladesh Muslim Family Laws Ordinance 1961 Section 4
 */
public class FaraidCalculatorEngineTest {

    // ==================== 1. ESTATE PROCESSING & DEDUCTION TESTS ====================

    @Test
    public void testEstateDeductionOrder() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setFuneralExpense(50000.0);
        input.setDebtAmount(150000.0);
        input.setWasiyyahAmount(100000.0);
        input.setSonCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(1000000.0, result.getGrossEstate(), 0.001);
        assertEquals(50000.0, result.getFuneralExpense(), 0.001);
        assertEquals(150000.0, result.getDebtAmount(), 0.001);
        assertEquals(100000.0, result.getValidWasiyyah(), 0.001);
        assertEquals(700000.0, result.getNetDistributableEstate(), 0.001);
    }

    // ==================== 2. SPOUSE TESTS ====================

    @Test
    public void testHusbandFixedSharesContextDependent() {
        // Case A: Husband with NO child -> 1/2 (Qur'an 4:12)
        FaraidInput inputNoChild = new FaraidInput();
        inputNoChild.setDeceasedGender(FaraidInput.Gender.FEMALE);
        inputNoChild.setTotalEstate(1000000.0);
        inputNoChild.setHusbandAlive(true);
        inputNoChild.setFullBrotherCount(1);

        FaraidCalculationResult resA = FaraidCalculatorEngine.calculate(inputNoChild);
        FaraidHeir husbandA = resA.getAllHeirs().stream().filter(h -> "husband".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(husbandA);
        assertEquals("1/2", husbandA.getFixedFractionString());
        assertEquals(500000.0, husbandA.getFinalAmount(), 0.01);
        assertTrue(husbandA.getExplanation().contains("Husband receives 1/2 because the deceased left no qualifying descendants."));

        // Case B: Husband WITH child -> reduced to 1/4 (Hajb Nuqsan, Qur'an 4:12)
        FaraidInput inputWithChild = new FaraidInput();
        inputWithChild.setDeceasedGender(FaraidInput.Gender.FEMALE);
        inputWithChild.setTotalEstate(1000000.0);
        inputWithChild.setHusbandAlive(true);
        inputWithChild.setSonCount(1);

        FaraidCalculationResult resB = FaraidCalculatorEngine.calculate(inputWithChild);
        FaraidHeir husbandB = resB.getAllHeirs().stream().filter(h -> "husband".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(husbandB);
        assertEquals("1/4", husbandB.getFixedFractionString());
        assertEquals(250000.0, husbandB.getFinalAmount(), 0.01);
        assertTrue(husbandB.getExplanation().contains("Husband receives 1/4 because the deceased left qualifying descendants."));
    }

    @Test
    public void testWifeFixedSharesContextDependent() {
        // Case A: 1 Wife with NO child -> 1/4 (Qur'an 4:12)
        FaraidInput inputNoChild = new FaraidInput();
        inputNoChild.setDeceasedGender(FaraidInput.Gender.MALE);
        inputNoChild.setTotalEstate(1200000.0);
        inputNoChild.setWifeCount(1);
        inputNoChild.setFullBrotherCount(1);

        FaraidCalculationResult resA = FaraidCalculatorEngine.calculate(inputNoChild);
        FaraidHeir wifeA = resA.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(wifeA);
        assertEquals("1/4", wifeA.getFixedFractionString());
        assertEquals(300000.0, wifeA.getFinalAmount(), 0.01);

        // Case B: 2 Wives WITH children -> Total wives' share = 1/8, Each wife = 1/16 (Qur'an 4:12)
        FaraidInput input2Wives = new FaraidInput();
        input2Wives.setDeceasedGender(FaraidInput.Gender.MALE);
        input2Wives.setTotalEstate(1600000.0);
        input2Wives.setWifeCount(2);
        input2Wives.setSonCount(1);

        FaraidCalculationResult resB = FaraidCalculatorEngine.calculate(input2Wives);
        FaraidHeir wivesB = resB.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(wivesB);
        assertEquals("1/8", wivesB.getFixedFractionString());
        assertEquals("1/8", wivesB.getFinalFractionString());
        assertEquals("1/16", wivesB.getIndividualFractionString());
        assertEquals(200000.0, wivesB.getFinalAmount(), 0.01);
        assertEquals(100000.0, wivesB.getIndividualAmount(), 0.01);
        assertTrue(wivesB.getExplanation().contains("Total wives' share = 1/8 collectively among 2 wives because the deceased left qualifying descendants. Each wife receives 1/16."));

        // Case C: 4 Wives WITH children -> share 1/8 equally (1/32 each = 3.125%)
        FaraidInput input4Wives = new FaraidInput();
        input4Wives.setDeceasedGender(FaraidInput.Gender.MALE);
        input4Wives.setTotalEstate(3200000.0);
        input4Wives.setWifeCount(4);
        input4Wives.setSonCount(1);

        FaraidCalculationResult resC = FaraidCalculatorEngine.calculate(input4Wives);
        FaraidHeir wivesC = resC.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(wivesC);
        assertEquals("1/8", wivesC.getFixedFractionString());
        assertEquals("1/32", wivesC.getIndividualFractionString());
        assertEquals(400000.0, wivesC.getFinalAmount(), 0.01);
        assertEquals(100000.0, wivesC.getIndividualAmount(), 0.01);
    }

    // ==================== 3. FATHER & MOTHER ====================

    @Test
    public void testFatherContextDependentFixedAndResidue() {
        // Case A: Role 1 - Fixed-Share Only (Father with Son/Grandson) -> 1/6 Fixed Share (Qur'an 4:11)
        FaraidInput inputWithSon = new FaraidInput();
        inputWithSon.setDeceasedGender(FaraidInput.Gender.MALE);
        inputWithSon.setTotalEstate(600000.0);
        inputWithSon.setFatherAlive(true);
        inputWithSon.setSonCount(1);

        FaraidCalculationResult resA = FaraidCalculatorEngine.calculate(inputWithSon);
        FaraidHeir fatherA = resA.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(fatherA);
        assertEquals(FaraidHeir.ShareType.FIXED, fatherA.getShareType());
        assertEquals("1/6", fatherA.getFixedFractionString());
        assertEquals(100000.0, fatherA.getFinalAmount(), 0.01);
        assertTrue(fatherA.getExplanation().contains("Father receives 1/6 fixed share only as an Ashab al-Furud heir because of the presence of a male descendant"));

        // Case B: Role 2 - Fixed Share PLUS Residue (Father with Daughter only) -> 1/6 Fixed + Asabah Residue
        FaraidInput inputWithDaughter = new FaraidInput();
        inputWithDaughter.setDeceasedGender(FaraidInput.Gender.MALE);
        inputWithDaughter.setTotalEstate(600000.0);
        inputWithDaughter.setFatherAlive(true);
        inputWithDaughter.setDaughterCount(1);

        FaraidCalculationResult resB = FaraidCalculatorEngine.calculate(inputWithDaughter);
        FaraidHeir fatherB = resB.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(fatherB);
        assertEquals(FaraidHeir.ShareType.FIXED_AND_RESIDUE, fatherB.getShareType());
        assertEquals(300000.0, fatherB.getFinalAmount(), 0.01);
        assertTrue(fatherB.getExplanation().contains("Father receives 1/6 fixed share plus the residue as an Asabah heir because of the presence of only female descendants"));

        // Case C: Role 3 - Pure Residuary (Father with NO descendants) -> Asabah Bi-Nafsihi
        FaraidInput inputNoDesc = new FaraidInput();
        inputNoDesc.setDeceasedGender(FaraidInput.Gender.MALE);
        inputNoDesc.setTotalEstate(600000.0);
        inputNoDesc.setFatherAlive(true);
        inputNoDesc.setWifeCount(1);

        FaraidCalculationResult resC = FaraidCalculatorEngine.calculate(inputNoDesc);
        FaraidHeir fatherC = resC.getAllHeirs().stream().filter(h -> "father".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(fatherC);
        assertEquals(FaraidHeir.ShareType.RESIDUE, fatherC.getShareType());
        assertEquals("3/4", fatherC.getFinalFractionString());
        assertEquals(450000.0, fatherC.getFinalAmount(), 0.01);
        assertTrue(fatherC.getExplanation().contains("Father inherits purely as a Residuary (Asabah) heir because the deceased left no qualifying descendants"));
    }

    @Test
    public void testMotherContextDependentReduction() {
        // Case A: Mother with Qualifying Descendants (Son) -> 1/6 (Qur'an 4:11)
        FaraidInput inputDesc = new FaraidInput();
        inputDesc.setDeceasedGender(FaraidInput.Gender.MALE);
        inputDesc.setTotalEstate(600000.0);
        inputDesc.setMotherAlive(true);
        inputDesc.setSonCount(1);

        FaraidCalculationResult resDesc = FaraidCalculatorEngine.calculate(inputDesc);
        FaraidHeir motherDesc = resDesc.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(motherDesc);
        assertEquals("1/6", motherDesc.getFixedFractionString());
        assertEquals(100000.0, motherDesc.getFinalAmount(), 0.01);
        assertTrue(motherDesc.getExplanation().contains("Mother receives 1/6 because the deceased left qualifying descendants"));

        // Case B: Mother with NO child and 0 or 1 sibling -> 1/3 (Qur'an 4:11)
        FaraidInput inputA = new FaraidInput();
        inputA.setDeceasedGender(FaraidInput.Gender.MALE);
        inputA.setTotalEstate(900000.0);
        inputA.setMotherAlive(true);
        inputA.setFatherAlive(true);

        FaraidCalculationResult resA = FaraidCalculatorEngine.calculate(inputA);
        FaraidHeir motherA = resA.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(motherA);
        assertEquals("1/3", motherA.getFixedFractionString());
        assertEquals(300000.0, motherA.getFinalAmount(), 0.01);
        assertTrue(motherA.getExplanation().contains("Mother receives 1/3 because the deceased left no qualifying descendants and less than 2 siblings"));

        // Case C: Mother with 2 siblings while Father is alive -> Siblings blocked, yet Mother reduced to 1/6 (Qur'an 4:11)
        FaraidInput inputC = new FaraidInput();
        inputC.setDeceasedGender(FaraidInput.Gender.MALE);
        inputC.setTotalEstate(600000.0);
        inputC.setMotherAlive(true);
        inputC.setFatherAlive(true);
        inputC.setFullBrotherCount(2);

        FaraidCalculationResult resC = FaraidCalculatorEngine.calculate(inputC);
        FaraidHeir motherC = resC.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(motherC);
        assertEquals("1/6", motherC.getFixedFractionString());
        assertEquals(100000.0, motherC.getFinalAmount(), 0.01);
        assertTrue(motherC.getExplanation().contains("Mother receives 1/6 because 2 or more siblings exist (Qur'an 4:11), even though those siblings may be blocked from inheriting by the father."));

        // Case D: Umariyyatan Case (Husband + Mother + Father) -> Mother gets 1/3 of Remainder = 1/6 of total (Qur'an & Umar r.a.)
        FaraidInput inputUm = new FaraidInput();
        inputUm.setDeceasedGender(FaraidInput.Gender.FEMALE);
        inputUm.setTotalEstate(600000.0);
        inputUm.setHusbandAlive(true);
        inputUm.setMotherAlive(true);
        inputUm.setFatherAlive(true);

        FaraidCalculationResult resUm = FaraidCalculatorEngine.calculate(inputUm);
        assertTrue(resUm.isUmariyyatan());
        FaraidHeir motherUm = resUm.getAllHeirs().stream().filter(h -> "mother".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(motherUm);
        assertEquals("1/6", motherUm.getFixedFractionString());
        assertEquals(100000.0, motherUm.getFinalAmount(), 0.01);
        assertTrue(motherUm.getExplanation().contains("Mother receives 1/3 of the residue after the spouse's share in the Umariyyatan case"));
    }

    // ==================== 4. DIRECT CHILDREN ====================

    @Test
    public void testDaughterFixedAndAsabah() {
        FaraidInput inputSonsAndDaughter = new FaraidInput();
        inputSonsAndDaughter.setDeceasedGender(FaraidInput.Gender.MALE);
        inputSonsAndDaughter.setTotalEstate(800000.0);
        inputSonsAndDaughter.setWifeCount(1);
        inputSonsAndDaughter.setSonCount(2);
        inputSonsAndDaughter.setDaughterCount(1);

        FaraidCalculationResult resC = FaraidCalculatorEngine.calculate(inputSonsAndDaughter);
        FaraidHeir wifeC = resC.getAllHeirs().stream().filter(h -> "wife".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir sonC = resC.getAllHeirs().stream().filter(h -> "son".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir daughterC = resC.getAllHeirs().stream().filter(h -> "daughter".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(wifeC);
        assertNotNull(sonC);
        assertNotNull(daughterC);

        assertEquals("1/8", wifeC.getFinalFractionString());
        assertEquals(100000.0, wifeC.getFinalAmount(), 0.01);

        assertEquals("7/10", sonC.getFinalFractionString());
        assertEquals("7/20", sonC.getIndividualFractionString());
        assertEquals(560000.0, sonC.getFinalAmount(), 0.01);
        assertEquals(280000.0, sonC.getIndividualAmount(), 0.01);
        assertTrue(sonC.getExplanation().contains("Total units = 5") || sonC.getExplanation().contains("২:১") || sonC.getExplanation().contains("2:1"));

        assertEquals("7/40", daughterC.getFinalFractionString());
        assertEquals(140000.0, daughterC.getFinalAmount(), 0.01);
        assertEquals(140000.0, daughterC.getIndividualAmount(), 0.01);
        assertTrue(daughterC.getExplanation().contains("Total units = 5") || daughterC.getExplanation().contains("২:১") || daughterC.getExplanation().contains("2:1"));
    }

    // ==================== 5. GRANDCHILDREN ====================

    @Test
    public void testGranddaughterTakmilatAlThuluthaynAndBlocking() {
        // Case A: 1 Daughter (1/2) + 1 Granddaughter -> Granddaughter gets 1/6 (Takmilat al-Thuluthayn)
        FaraidInput inputTakmila = new FaraidInput();
        inputTakmila.setDeceasedGender(FaraidInput.Gender.MALE);
        inputTakmila.setTotalEstate(1200000.0);
        inputTakmila.setDaughterCount(1);
        inputTakmila.setGranddaughterCount(1);
        inputTakmila.setFullBrotherCount(1);

        FaraidCalculationResult resA = FaraidCalculatorEngine.calculate(inputTakmila);
        FaraidHeir gdA = resA.getAllHeirs().stream().filter(h -> "granddaughter".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(gdA);
        assertEquals("1/6", gdA.getFixedFractionString());
        assertEquals(200000.0, gdA.getFinalAmount(), 0.01);

        // Case B: 2 Daughters (2/3) + Granddaughter -> Granddaughter is BLOCKED (2/3 quota exhausted)
        FaraidInput input2D = new FaraidInput();
        input2D.setDeceasedGender(FaraidInput.Gender.MALE);
        input2D.setTotalEstate(1200000.0);
        input2D.setDaughterCount(2);
        input2D.setGranddaughterCount(1);
        input2D.setFullBrotherCount(1);

        FaraidCalculationResult resB = FaraidCalculatorEngine.calculate(input2D);
        FaraidHeir gdB = resB.getAllHeirs().stream().filter(h -> h.isBlocked() && (h.getRelationshipBn().contains("নাতনি") || h.getRelationshipBn().contains("পৌত্রী"))).findFirst().orElse(null);
        assertNotNull(gdB);
        assertTrue(gdB.isBlocked());
        assertEquals(0.0, gdB.getFinalAmount(), 0.01);
        assertTrue(gdB.getBlockingReason().contains("২ বা ততোধিক কন্যা") || gdB.getBlockingReason().contains("Son's daughter is excluded"));
    }

    @Test
    public void testGrandchildrenSeparateRulesAndBlocking() {
        // Case 1: Direct Son blocks both Son's son and Son's daughter separately
        FaraidInput inSonPresent = new FaraidInput();
        inSonPresent.setDeceasedGender(FaraidInput.Gender.MALE);
        inSonPresent.setTotalEstate(1200000.0);
        inSonPresent.setSonCount(1);
        inSonPresent.setGrandsonCount(1);
        inSonPresent.setGranddaughterCount(1);

        FaraidCalculationResult resSonPresent = FaraidCalculatorEngine.calculate(inSonPresent);
        FaraidHeir blockedSonSon = resSonPresent.getAllHeirs().stream().filter(h -> h.isBlocked() && (h.getRelationshipBn().contains("নাতি") || h.getRelationshipBn().contains("পৌত্র"))).findFirst().orElse(null);
        FaraidHeir blockedSonDaughter = resSonPresent.getAllHeirs().stream().filter(h -> h.isBlocked() && (h.getRelationshipBn().contains("নাতনি") || h.getRelationshipBn().contains("পৌত্রী"))).findFirst().orElse(null);

        assertNotNull(blockedSonSon);
        assertNotNull(blockedSonDaughter);
        assertTrue(blockedSonSon.isBlocked());
        assertTrue(blockedSonDaughter.isBlocked());
        assertTrue(blockedSonSon.getBlockingReason().contains("পুত্র") || blockedSonSon.getBlockingReason().contains("Son's son is excluded"));
        assertTrue(blockedSonDaughter.getBlockingReason().contains("পুত্র") || blockedSonDaughter.getBlockingReason().contains("Son's daughter is excluded"));
    }

    // ==================== 6. SIBLING ENGINE (6 DISTINCT UNMERGED CATEGORIES) ====================

    @Test
    public void testFullBrotherAndSisterAsabahBilGhayr() {
        // 1 Full Brother + 1 Full Sister in Kalalah -> 2:1 ratio (Qur'an 4:176)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(900000.0);
        input.setHusbandAlive(true); // Husband = 1/2 = 450,000
        input.setFullBrotherCount(1); // 2 units of residue (300,000)
        input.setFullSisterCount(1);  // 1 unit of residue (150,000)

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);
        assertEquals(3, result.getHeirShares().size());

        FaraidHeir fb = result.getAllHeirs().stream().filter(h -> "full_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir fs = result.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(fb);
        assertNotNull(fs);
        assertEquals("1/3", fb.getFinalFractionString());
        assertEquals("1/6", fs.getFinalFractionString());
        assertEquals(300000.0, fb.getFinalAmount(), 0.01);
        assertEquals(150000.0, fs.getFinalAmount(), 0.01);
    }

    @Test
    public void testFullSisterAloneAndMultipleKalalah() {
        // Single Full Sister -> 1/2 fixed (Qur'an 4:176)
        FaraidInput input1 = new FaraidInput();
        input1.setDeceasedGender(FaraidInput.Gender.MALE);
        input1.setTotalEstate(1000000.0);
        input1.setFullSisterCount(1);
        input1.setPaternalUncleCount(1);

        FaraidCalculationResult res1 = FaraidCalculatorEngine.calculate(input1);
        FaraidHeir fs1 = res1.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(fs1);
        assertEquals("1/2", fs1.getFixedFractionString());
        assertEquals(500000.0, fs1.getFinalAmount(), 0.01);

        // Multiple Full Sisters (2) -> 2/3 fixed collectively (Qur'an 4:176)
        FaraidInput input2 = new FaraidInput();
        input2.setDeceasedGender(FaraidInput.Gender.MALE);
        input2.setTotalEstate(1200000.0);
        input2.setFullSisterCount(2);
        input2.setPaternalUncleCount(1);

        FaraidCalculationResult res2 = FaraidCalculatorEngine.calculate(input2);
        FaraidHeir fs2 = res2.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(fs2);
        assertEquals("2/3", fs2.getFixedFractionString());
        assertEquals(800000.0, fs2.getFinalAmount(), 0.01);
        assertEquals(400000.0, fs2.getIndividualAmount(), 0.01);
    }

    @Test
    public void testFullSisterAsabahMaAlGhayrWithDaughter() {
        // Full Sister with Daughter -> Sister becomes Asabah ma'al Ghayr (Bukhari 6742)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setDaughterCount(1);
        input.setFullSisterCount(1);
        input.setPaternalUncleCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        FaraidHeir sister = result.getAllHeirs().stream().filter(h -> "full_sister".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(sister);
        assertEquals(FaraidHeir.ShareType.RESIDUE, sister.getShareType());
        assertEquals(500000.0, sister.getFinalAmount(), 0.01);
    }

    @Test
    public void testPaternalSisterTakmilatAlThuluthaynAndBlocking() {
        // 1 Full Sister (1/2) + 1 Paternal Sister (1/6 Takmilat al-Thuluthayn)
        FaraidInput input1 = new FaraidInput();
        input1.setDeceasedGender(FaraidInput.Gender.MALE);
        input1.setTotalEstate(1200000.0);
        input1.setFullSisterCount(1);
        input1.setConsanguineSisterCount(1);
        input1.setPaternalUncleCount(1);

        FaraidCalculationResult res1 = FaraidCalculatorEngine.calculate(input1);
        FaraidHeir cs1 = res1.getAllHeirs().stream().filter(h -> "consanguine_sister".equals(h.getRelationship())).findFirst().orElse(null);
        assertNotNull(cs1);
        assertEquals("1/6", cs1.getFixedFractionString());
        assertEquals(200000.0, cs1.getFinalAmount(), 0.01);

        // 2 Full Sisters (2/3) + Paternal Sister -> Paternal Sister is BLOCKED (Qur'an 4:176)
        FaraidInput input2 = new FaraidInput();
        input2.setDeceasedGender(FaraidInput.Gender.MALE);
        input2.setTotalEstate(1200000.0);
        input2.setFullSisterCount(2);
        input2.setConsanguineSisterCount(1);

        FaraidCalculationResult res2 = FaraidCalculatorEngine.calculate(input2);
        FaraidHeir cs2 = res2.getAllHeirs().stream().filter(h -> h.isBlocked() && (h.getRelationshipBn().contains("সৎ বোন") || h.getRelationshipBn().contains("বৈমাত্রেয় বোন"))).findFirst().orElse(null);
        assertNotNull(cs2);
        assertTrue(cs2.isBlocked());
        assertEquals(0.0, cs2.getFinalAmount(), 0.01);
    }

    @Test
    public void testPaternalBrotherAndSisterAsabahBilGhayr() {
        // 1 Paternal Brother + 1 Paternal Sister (0 full siblings) -> 2:1 Asabah (Qur'an 4:176)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(600000.0);
        input.setHusbandAlive(true); // 1/2 = 300,000
        input.setConsanguineBrotherCount(1); // 2/3 of 300,000 = 200,000
        input.setConsanguineSisterCount(1);  // 1/3 of 300,000 = 100,000

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        FaraidHeir cb = result.getAllHeirs().stream().filter(h -> "consanguine_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir cs = result.getAllHeirs().stream().filter(h -> "consanguine_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(cb);
        assertNotNull(cs);
        assertEquals("1/3", cb.getFinalFractionString());
        assertEquals("1/6", cs.getFinalFractionString());
        assertEquals(200000.0, cb.getFinalAmount(), 0.01);
        assertEquals(100000.0, cs.getFinalAmount(), 0.01);
    }

    @Test
    public void testUterineSiblingsKalalahEqualSharing() {
        // 1 Uterine Brother + 1 Uterine Sister -> Share 1/3 equally 1:1 (Qur'an 4:12)
        // Husband (1/2 = 600,000) + 1 Uterine Brother (1/6 = 200,000) + 1 Uterine Sister (1/6 = 200,000) + 1 Full Brother (Residue = 200,000)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(1200000.0);
        input.setHusbandAlive(true);
        input.setUterineBrotherCount(1);
        input.setUterineSisterCount(1);
        input.setFullBrotherCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(4, result.getHeirShares().size());

        FaraidHeir ub = result.getAllHeirs().stream().filter(h -> "uterine_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir us = result.getAllHeirs().stream().filter(h -> "uterine_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(ub);
        assertNotNull(us);
        assertEquals("1/6", ub.getFixedFractionString());
        assertEquals("1/6", us.getFixedFractionString());
        assertEquals(200000.0, ub.getFinalAmount(), 0.01);
        assertEquals(200000.0, us.getFinalAmount(), 0.01);
        assertEquals(200000.0, ub.getIndividualAmount(), 0.01);
        assertEquals(200000.0, us.getIndividualAmount(), 0.01);
    }

    @Test
    public void testUterineSiblingsMultipleBrothersAndSistersOneThirdPool() {
        // 2 Uterine Brothers + 1 Uterine Sister -> Share 1/3 equally (1/9 per individual)
        // Estate = 900,000 BDT
        // Husband takes 1/2 (450,000)
        // Uterine pool = 1/3 (300,000 BDT) -> 3 individuals = 100,000 BDT each
        // Uterine Brothers category (2 count) = 2/9 (200,000 BDT)
        // Uterine Sister category (1 count) = 1/9 (100,000 BDT)
        // Full brother takes residue = 1/6 (150,000 BDT)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(900000.0);
        input.setHusbandAlive(true);
        input.setUterineBrotherCount(2);
        input.setUterineSisterCount(1);
        input.setFullBrotherCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);

        FaraidHeir ub = result.getAllHeirs().stream().filter(h -> "uterine_brother".equals(h.getRelationship())).findFirst().orElse(null);
        FaraidHeir us = result.getAllHeirs().stream().filter(h -> "uterine_sister".equals(h.getRelationship())).findFirst().orElse(null);

        assertNotNull(ub);
        assertNotNull(us);
        assertEquals("2/9", ub.getFixedFractionString());
        assertEquals("1/9", us.getFixedFractionString());
        assertEquals(200000.0, ub.getFinalAmount(), 0.01);
        assertEquals(100000.0, us.getFinalAmount(), 0.01);
        assertEquals(100000.0, ub.getIndividualAmount(), 0.01);
        assertEquals(100000.0, us.getIndividualAmount(), 0.01);
    }

    @Test
    public void testUterineSiblingsBlockedBySonDaughterFather() {
        // Blocked by Father
        FaraidInput inFather = new FaraidInput();
        inFather.setDeceasedGender(FaraidInput.Gender.MALE);
        inFather.setTotalEstate(1000000.0);
        inFather.setFatherAlive(true);
        inFather.setUterineBrotherCount(1);
        inFather.setUterineSisterCount(1);

        FaraidCalculationResult resFather = FaraidCalculatorEngine.calculate(inFather);
        assertTrue(resFather.getAllHeirs().stream().anyMatch(h -> h.isBlocked() && (h.getRelationshipBn().contains("সৎ ভাই") || h.getRelationshipBn().contains("বৈপিত্রীয় ভাই"))));
        assertTrue(resFather.getAllHeirs().stream().anyMatch(h -> h.isBlocked() && (h.getRelationshipBn().contains("সৎ বোন") || h.getRelationshipBn().contains("বৈপিত্রীয় বোন"))));

        // Blocked by Daughter (Descendants block maternal siblings per Qur'an 4:12)
        FaraidInput inDaughter = new FaraidInput();
        inDaughter.setDeceasedGender(FaraidInput.Gender.MALE);
        inDaughter.setTotalEstate(1000000.0);
        inDaughter.setDaughterCount(1);
        inDaughter.setUterineBrotherCount(1);

        FaraidCalculationResult resDaughter = FaraidCalculatorEngine.calculate(inDaughter);
        assertTrue(resDaughter.getAllHeirs().stream().anyMatch(h -> h.isBlocked() && (h.getRelationshipBn().contains("সৎ ভাই") || h.getRelationshipBn().contains("বৈপিত্রীয় ভাই"))));
    }

    // ==================== 7. GRANDMOTHERS ====================

    @Test
    public void testGrandmothersSharingOneSixth() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setMotherAlive(false);
        input.setFatherAlive(false);
        input.setPaternalGrandmotherAlive(true);
        input.setMaternalGrandmotherAlive(true);
        input.setSonCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        double gmTotal = 0, gmIndividual = 0;
        for (HeirShareResult h : result.getHeirShares()) {
            if ("grandmother".equals(h.getRelationKey())) {
                gmTotal = h.getTotalCategoryAmount();
                gmIndividual = h.getIndividualAmount();
            }
        }
        assertEquals(200000.0, gmTotal, 0.01);
        assertEquals(100000.0, gmIndividual, 0.01);
    }

    @Test
    public void testPaternalGrandmotherWithFatherHanafiVsShafii() {
        FaraidInput hanafiInput = new FaraidInput();
        hanafiInput.setDeceasedGender(FaraidInput.Gender.MALE);
        hanafiInput.setMadhab(FaraidInput.Madhab.HANAFI);
        hanafiInput.setTotalEstate(1200000.0);
        hanafiInput.setFatherAlive(true);
        hanafiInput.setPaternalGrandmotherAlive(true);

        FaraidCalculationResult hanafiResult = FaraidCalculatorEngine.calculate(hanafiInput);
        assertNotNull(hanafiResult);
        assertTrue(hanafiResult.hasMadhabDifference());
        assertEquals(1, hanafiResult.getHeirShares().size());
        assertEquals(1200000.0, hanafiResult.getHeirShares().get(0).getTotalCategoryAmount(), 0.01);

        FaraidInput shafiiInput = new FaraidInput();
        shafiiInput.setDeceasedGender(FaraidInput.Gender.MALE);
        shafiiInput.setMadhab(FaraidInput.Madhab.SHAFI);
        shafiiInput.setTotalEstate(1200000.0);
        shafiiInput.setFatherAlive(true);
        shafiiInput.setPaternalGrandmotherAlive(true);

        FaraidCalculationResult shafiiResult = FaraidCalculatorEngine.calculate(shafiiInput);
        assertNotNull(shafiiResult);
        assertTrue(shafiiResult.hasMadhabDifference());
        assertEquals(2, shafiiResult.getHeirShares().size());
        double gmShare = 0, fatherShare = 0;
        for (HeirShareResult h : shafiiResult.getHeirShares()) {
            if ("paternal_grandmother".equals(h.getRelationKey())) gmShare = h.getTotalCategoryAmount();
            if ("father".equals(h.getRelationKey())) fatherShare = h.getTotalCategoryAmount();
        }
        assertEquals(200000.0, gmShare, 0.01);
        assertEquals(1000000.0, fatherShare, 0.01);
    }

    // ==================== 8. WASIYYAH & MADHAB TESTS ====================

    @Test
    public void testWasiyyahExceedingOneThirdCappedAndFlagged() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1200000.0);
        input.setFuneralExpense(0.0);
        input.setDebtAmount(0.0);
        input.setWasiyyahAmount(600000.0);
        input.setSonCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(1200000.0, result.getGrossEstate(), 0.001);
        assertEquals(600000.0, result.getEnteredWasiyyah(), 0.001);
        assertEquals(400000.0, result.getValidWasiyyah(), 0.001);
        assertEquals(800000.0, result.getNetDistributableEstate(), 0.001);
        assertTrue(result.isWasiyyahExceedingOneThird());
        assertNotNull(result.getWasiyyahValidationNotice());
        assertFalse(result.getWasiyyahValidationNotice().trim().isEmpty());
    }

    @Test
    public void testWasiyyahToHeirNotAutomaticallyDeductedAndFlagged() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setWasiyyahAmount(100000.0);
        input.setWasiyyahToHeir(true);
        input.setSonCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(1000000.0, result.getGrossEstate(), 0.001);
        assertEquals(0.0, result.getValidWasiyyah(), 0.001);
        assertEquals(1000000.0, result.getNetDistributableEstate(), 0.001);
        assertTrue(result.isWasiyyahToHeir());
        assertNotNull(result.getWasiyyahValidationNotice());
        assertFalse(result.getWasiyyahValidationNotice().trim().isEmpty());
    }

    @Test
    public void testHanafiGrandfatherBlocksSiblings() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setMadhab(FaraidInput.Madhab.HANAFI);
        input.setTotalEstate(1000000.0);
        input.setPaternalGrandfatherAlive(true);
        input.setFullBrotherCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertTrue(result.hasMadhabDifference());
        assertTrue(result.getMadhabDifferenceNote().contains("হানাফি") || result.getMadhabDifferenceNote(false).contains("Hanafi"));
        assertEquals(1, result.getHeirShares().size());
        assertEquals(1000000.0, result.getHeirShares().get(0).getTotalCategoryAmount(), 0.01);
        assertFalse(result.getBlockedHeirs().isEmpty());
    }

    @Test
    public void testNephewBlocksUncle() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(900000.0);
        input.setWifeCount(1);
        input.setNephewCount(1);
        input.setPaternalUncleCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        boolean uncleBlocked = false;
        for (var b : result.getBlockedHeirs()) {
            if (b.getHeirTitleBn().contains("চাচা")) uncleBlocked = true;
        }
        assertTrue(uncleBlocked);

        double nephewAmt = 0;
        for (HeirShareResult h : result.getHeirShares()) {
            if ("nephew".equals(h.getRelationKey())) nephewAmt = h.getTotalCategoryAmount();
        }
        assertEquals(675000.0, nephewAmt, 0.01);
    }

    @Test
    public void testBangladeshLaw1961Section4OrphanGrandchildren() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1600000.0);
        input.setWifeCount(1);
        input.setSonCount(1);
        input.setGrandsonCount(1);
        input.setApplyBangladeshLaw1961(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(3, result.getHeirShares().size());
        double sumDistributed = 0.0;
        for (HeirShareResult share : result.getHeirShares()) {
            sumDistributed += share.getTotalCategoryAmount();
        }
        assertEquals(1600000.0, sumDistributed, 0.01);
    }
}