package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.engine.FaraidReconciliationEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FaraidReconciliationTest {

    @Test
    public void testReconciliationGuaranteesZeroRoundingDiscrepancy() {
        // Test case with repeating decimals (e.g. 1/3, 1/6, 2/3)
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(1000000.0); // 1,000,000 / 3 = 333,333.3333...
        input.setMotherAlive(true);
        input.setFatherAlive(true);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        List<FaraidReconciliationEngine.ReconciledEntry> entries =
                FaraidReconciliationEngine.reconcile(result.getHeirShares(), result.getNetDistributableEstate(), FaraidCurrency.BDT);

        assertFalse(entries.isEmpty());
        double sumReconciled = 0.0;
        for (FaraidReconciliationEngine.ReconciledEntry e : entries) {
            sumReconciled += e.getReconciledDisplayAmount();
            assertTrue(e.getReconciledDisplayAmount() > 0);
        }

        assertEquals("Sum of reconciled display amounts must equal net estate exactly to the cent",
                result.getNetDistributableEstate(), sumReconciled, 0.001);
    }

    @Test
    public void testMultipleHeirOddEstateReconciliation() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(777777.77);
        input.setWifeCount(1);
        input.setSonCount(2);
        input.setDaughterCount(3);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        List<FaraidReconciliationEngine.ReconciledEntry> entries =
                FaraidReconciliationEngine.reconcile(result.getHeirShares(), result.getNetDistributableEstate(), FaraidCurrency.SAR);

        double sumReconciled = 0.0;
        for (FaraidReconciliationEngine.ReconciledEntry e : entries) {
            sumReconciled += e.getReconciledDisplayAmount();
        }

        assertEquals("Sum of odd estate reconciled entries must strictly equal net estate",
                result.getNetDistributableEstate(), sumReconciled, 0.01);
    }
}