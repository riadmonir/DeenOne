package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidAuditTrailEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FaraidAuditTrailTest {

    @Test
    public void testSequentialAuditTrailGeneration() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0);
        input.setFuneralExpense(50000.0);
        input.setDebtAmount(100000.0);
        input.setWasiyyahAmount(50000.0);
        input.setWifeCount(1);
        input.setMotherAlive(true);
        input.setFatherAlive(true);
        input.setFullBrotherCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        List<FaraidAuditTrailEngine.TraceStep> steps = FaraidAuditTrailEngine.generateTrace(result);

        assertNotNull(steps);
        assertTrue("Trace should have at least 6 steps", steps.size() >= 6);

        // Verify Step numbers and non-empty content
        for (int i = 0; i < steps.size(); i++) {
            assertEquals(i + 1, steps.get(i).getStepNumber());
            assertNotNull(steps.get(i).getTitleBn());
            assertNotNull(steps.get(i).getTitleEn());
            assertNotNull(steps.get(i).getDetailsBn());
            assertNotNull(steps.get(i).getDetailsEn());
            assertFalse(steps.get(i).formatStringBn().isEmpty());
            assertFalse(steps.get(i).formatStringEn().isEmpty());
        }

        // Full formatted audit trail string
        String fullAudit = FaraidAuditTrailEngine.formatFullAuditTrailBn(result);
        assertNotNull(fullAudit);
        assertFalse(fullAudit.trim().isEmpty());
    }
}