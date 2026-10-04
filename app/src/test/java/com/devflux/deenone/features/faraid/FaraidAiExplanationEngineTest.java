package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidAiExplanationEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidAiExplanationEngineTest {

    @Test
    public void testAiExplanationStrictlyFollowsDeterministicResult() {
        // Case: Wife + Mother + Father + 1 Son + 1 Daughter
        // Gross = 600,000 SAR, Funeral = 0, Debt = 0, Wasiyyah = 0
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setCurrency(FaraidCurrency.SAR);
        input.setTotalEstate(600000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setSonCount(1);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);

        String explanation = FaraidAiExplanationEngine.explainCalculation(result);
        assertNotNull(explanation);

        // Verify that explanation contains exact deterministic figures:
        // Wife = 1/8 (75,000 SAR formatted in Bengali)
        assertTrue(explanation.contains("﷼ " + BengaliNumberUtil.toBengali("75,000")) || explanation.contains(BengaliNumberUtil.toBengali("75,000")));
        assertTrue(explanation.contains(BengaliNumberUtil.toBengali("100,000")));
        assertTrue(explanation.contains(BengaliNumberUtil.toBengali("600,000")));

        // Verify that Islamic references and disclaimer are included
        assertTrue(explanation.contains("4:11") || explanation.contains("১১") || explanation.contains("নিসা"));
        assertTrue(explanation.contains("Disclaimer") || explanation.contains("ডিসক্লেইমার") || explanation.contains("সতর্কতা"));
    }

    @Test
    public void testAiExplanationExplainsBlockedHeirsDeterministically() {
        // Case: Son exists, so Brother and Paternal Uncle are blocked
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(100000.0);
        input.setSonCount(1);
        input.setFullBrotherCount(1);
        input.setPaternalUncleCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        String explanation = FaraidAiExplanationEngine.explainCalculation(result);

        assertNotNull(explanation);
        assertFalse(result.getBlockedHeirs().isEmpty());
        // Verify blocked heirs are present in the AI explanation
        for (com.devflux.deenone.features.faraid.model.BlockedHeirInfo bi : result.getBlockedHeirs()) {
            assertTrue(explanation.contains(bi.getHeirTitleBn()));
            assertTrue(explanation.contains(bi.getLegalReasonBn()));
        }
    }
}