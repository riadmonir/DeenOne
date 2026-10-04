package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.engine.FaraidRaddEngine;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FaraidRaddEngineTest {

    @Test
    public void testRaddWithSpouseExcludedFromRadd() {
        // Husband (1/2) + 1 Daughter (1/2) -> Daughter takes 1/2 fixed + 1/4 surplus = 3/4 total; Husband takes 1/4 with child
        // Example: Husband (1/4) + 1 Daughter (1/2) -> Sum = 3/4 < 1 -> Radd surplus = 1/4 given to Daughter -> Daughter gets 1/2 + 1/4 = 3/4
        List<FaraidCalculatorEngine.WorkingShare> shares = new ArrayList<>();
        shares.add(new FaraidCalculatorEngine.WorkingShare(
                "husband", "husband", "স্বামী",
                FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED,
                HeirShareResult.ShareType.QURANIC_FIXED, FaraidFraction.ONE_FOURTH, 0, 1, false, false, null, "Husband 1/4"
        ));
        shares.add(new FaraidCalculatorEngine.WorkingShare(
                "daughter", "daughter", "মেয়ে (কন্যা)",
                FaraidInput.Gender.FEMALE, 1, FaraidHeir.ShareType.FIXED,
                HeirShareResult.ShareType.QURANIC_FIXED, FaraidFraction.HALF, 0, 1, false, false, null, "Daughter 1/2"
        ));

        FaraidRaddEngine.RaddResult result = FaraidRaddEngine.evaluateAndApplyRadd(shares, false, 800000.0);
        assertTrue(result.isRaddApplied());

        FaraidRaddEngine.RaddItem husband = result.getItems().stream().filter(i -> "husband".equals(i.getHeirKey())).findFirst().orElse(null);
        FaraidRaddEngine.RaddItem daughter = result.getItems().stream().filter(i -> "daughter".equals(i.getHeirKey())).findFirst().orElse(null);

        assertNotNull(husband);
        assertNotNull(daughter);
        assertEquals("1/4", husband.getRaddFinalFraction().toString()); // Husband does not get Radd
        assertEquals(200000.0, husband.getTotalAmount(), 0.01);

        assertEquals("3/4", daughter.getRaddFinalFraction().toString()); // Daughter gets surplus
        assertEquals(600000.0, daughter.getTotalAmount(), 0.01);
    }
}