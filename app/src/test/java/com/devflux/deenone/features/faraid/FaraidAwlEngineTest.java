package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidAwlEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FaraidAwlEngineTest {

    @Test
    public void testAwlAppliedWhenSumExceedsOne() {
        // Husband (1/2 = 3/6) + 2 Full Sisters (2/3 = 4/6) -> Sum = 7/6 (Awl from 6 to 7)
        List<FaraidCalculatorEngine.WorkingShare> shares = new ArrayList<>();
        shares.add(new FaraidCalculatorEngine.WorkingShare(
                "husband", "husband", "স্বামী",
                FaraidInput.Gender.MALE, 1, FaraidHeir.ShareType.FIXED,
                HeirShareResult.ShareType.QURANIC_FIXED, FaraidFraction.HALF, 0, 1, false, false, null, "Husband 1/2"
        ));
        shares.add(new FaraidCalculatorEngine.WorkingShare(
                "full_sister", "full_sister", "সহোদর বোনগণ (২ জন)",
                FaraidInput.Gender.FEMALE, 2, FaraidHeir.ShareType.FIXED,
                HeirShareResult.ShareType.QURANIC_FIXED, FaraidFraction.TWO_THIRDS, 0, 1, false, false, null, "Sisters 2/3"
        ));

        FaraidAwlEngine.AwlResult result = FaraidAwlEngine.evaluateAndApplyAwl(shares, 700000.0);
        assertTrue(result.isAwlApplied());
        assertEquals(6, result.getBaseDenominator());
        assertEquals(7, result.getAwlDenominator());
        assertEquals(2, result.getAdjustedItems().size());

        // Husband adjusted: (1/2) / (7/6) = 3/7 -> 300,000 BDT
        FaraidAwlEngine.AwlItem husbandItem = result.getAdjustedItems().get(0);
        assertEquals("3/7", husbandItem.getAdjustedFraction().toString());
        assertEquals(300000.0, husbandItem.getAdjustedTotalAmount(), 0.01);

        // Sisters adjusted: (2/3) / (7/6) = 4/7 -> 400,000 BDT
        FaraidAwlEngine.AwlItem sisterItem = result.getAdjustedItems().get(1);
        assertEquals("4/7", sisterItem.getAdjustedFraction().toString());
        assertEquals(400000.0, sisterItem.getAdjustedTotalAmount(), 0.01);
    }
}