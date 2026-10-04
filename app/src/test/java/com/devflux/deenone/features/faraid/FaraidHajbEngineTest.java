package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidHajbEngine;
import com.devflux.deenone.features.faraid.engine.HeirEligibilityStatus;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirHajbDecision;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class FaraidHajbEngineTest {

    @Test
    public void testFatherAndMotherNeverBlocked() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setFatherAlive(true);
        input.setMotherAlive(true);
        input.setSonCount(2);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        assertNotNull(decisions.get("father"));
        assertNotNull(decisions.get("mother"));
        assertEquals(HeirEligibilityStatus.ELIGIBLE_FIXED, decisions.get("father").getStatus());
        assertEquals(HeirEligibilityStatus.ELIGIBLE_FIXED, decisions.get("mother").getStatus());
    }

    @Test
    public void testSonBlocksGrandchildren() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setSonCount(1);
        input.setGrandsonCount(1);
        input.setGranddaughterCount(1);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("grandson").getStatus());
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("granddaughter").getStatus());
        assertTrue(decisions.get("grandson").getReasonEn().toLowerCase().contains("son"));
        assertTrue(decisions.get("granddaughter").getReasonEn().toLowerCase().contains("son"));
    }

    @Test
    public void testFatherBlocksGrandfatherAndBrothers() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setFatherAlive(true);
        input.setPaternalGrandfatherAlive(true);
        input.setFullBrotherCount(1);
        input.setConsanguineBrotherCount(1);
        input.setUterineBrotherCount(1);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("paternal_grandfather").getStatus());
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("full_brother").getStatus());
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("consanguine_brother").getStatus());
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("uterine_brother").getStatus());
        assertTrue(decisions.get("paternal_grandfather").getReasonEn().toLowerCase().contains("father"));
        assertTrue(decisions.get("full_brother").getReasonEn().toLowerCase().contains("father"));
        assertTrue(decisions.get("uterine_brother").getReasonEn().toLowerCase().contains("father"));
    }

    @Test
    public void testMaternalSiblingsBlockedByDescendants() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setDaughterCount(1);
        input.setUterineBrotherCount(1);
        input.setUterineSisterCount(1);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("uterine_brother").getStatus());
        assertEquals(HeirEligibilityStatus.BLOCKED, decisions.get("uterine_sister").getStatus());
        assertTrue(decisions.get("uterine_brother").getReasonEn().toLowerCase().contains("descendant") || decisions.get("uterine_brother").getReasonEn().toLowerCase().contains("ascendant"));
    }
}