package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidSpecialCasesEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidSpecialCasesEngineTest {

    @Test
    public void testUmariyyatanDetection() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setHusbandAlive(true);
        input.setFatherAlive(true);
        input.setMotherAlive(true);

        assertTrue(FaraidSpecialCasesEngine.isUmariyyatanCase(input));
    }

    @Test
    public void testSoleHeirDetection() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setSonCount(1);

        assertTrue(FaraidSpecialCasesEngine.isSoleHeirCase(input));
    }

    @Test
    public void testSpecialistVerificationFallbackForInvalidInput() {
        FaraidInput emptyInput = new FaraidInput();
        emptyInput.setTotalEstate(1000000.0);
        // No heirs entered at all

        assertTrue(FaraidSpecialCasesEngine.isSpecialistVerificationRequired(emptyInput));

        FaraidCalculationResult res = FaraidCalculatorEngine.calculate(emptyInput);
        assertTrue(res.getStatusMessageBn().contains(FaraidSpecialCasesEngine.SPECIALIST_VERIFICATION_MSG_EN));
    }
}