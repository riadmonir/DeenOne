package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidSafetyEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidFinalAccuracyTest {

    @Test
    public void testDeterministicCalculationIntegrity() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(2400000.0);
        input.setWifeCount(1);
        input.setSonCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);

        // Deterministic check
        FaraidCalculationResult result2 = FaraidCalculatorEngine.calculate(input);
        assertEquals(result.getNetDistributableEstate(), result2.getNetDistributableEstate(), 0.001);
        assertEquals(result.getHeirShares().size(), result2.getHeirShares().size());

        // Validate Safety Engine compliance
        assertTrue("Calculation must be 100% compliant with Faraid safety rules",
                FaraidSafetyEngine.isCalculationSafe(result));
    }

    @Test
    public void testUncertaintyFallbackTextPresent() {
        assertEquals("Unable to confidently resolve this case. Please consult a qualified Faraid scholar.",
                FaraidSafetyEngine.UNCERTAIN_CASE_FALLBACK_EN);
        assertEquals("এই মাসআলাটির ক্ষেত্রে নিশ্চিত সমাধানের জন্য নির্ভরযোগ্য উৎস অপ্রতুল। অনুগ্রহ করে একজন বিজ্ঞ ফারায়েজ বিশেষজ্ঞ আলেমের সাথে সরাসরি পরামর্শ করুন।",
                FaraidSafetyEngine.UNCERTAIN_CASE_FALLBACK_BN);
    }
}