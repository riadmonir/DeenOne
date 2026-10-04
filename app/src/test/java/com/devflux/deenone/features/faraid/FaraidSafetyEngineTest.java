package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidSafetyEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidSafetyEngineTest {

    @Test
    public void testCalculationSafetyValidation() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(600000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setSonCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);
        assertTrue(FaraidSafetyEngine.isCalculationSafe(result));
    }

    @Test
    public void testAiExplanationGuardrailsRemovesFatwaClaims() {
        String testText = "এই হিসাবটি একটি অফিসিয়াল ফতোয়া হিসেবে বিবেচিত হবে।";
        String guarded = FaraidSafetyEngine.guardAiExplanation(testText, null);
        assertFalse(guarded.contains("অফিসিয়াল ফতোয়া"));
        assertTrue(guarded.contains("শিক্ষামূলক বণ্টন বিশ্লেষণ"));
    }

    @Test
    public void testUncertainCaseFallback() {
        String fallback = FaraidSafetyEngine.UNCERTAIN_CASE_FALLBACK_EN;
        assertTrue(fallback.contains("Unable to confidently resolve this case"));
        assertTrue(fallback.contains("qualified Faraid scholar"));
    }
}