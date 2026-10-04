package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.AsabahType;
import com.devflux.deenone.features.faraid.engine.FaraidAsabahEngine;
import com.devflux.deenone.features.faraid.engine.FaraidHajbEngine;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirHajbDecision;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class FaraidAsabahEngineTest {

    @Test
    public void testAsabahBiNafsihiSon() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setSonCount(2);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        FaraidAsabahEngine.AsabahResolution res = FaraidAsabahEngine.resolveClosestAsabah(input, decisions);

        assertTrue(res.hasAsabah());
        assertEquals(AsabahType.BI_NAFSIHI, res.getAsabahType());
        assertEquals("son", res.getPrimaryAsabahKey());
        assertEquals(2, res.getTotalUnits());
    }

    @Test
    public void testAsabahBilGhayrSonAndDaughterTwoToOne() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setSonCount(1);
        input.setDaughterCount(2);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        FaraidAsabahEngine.AsabahResolution res = FaraidAsabahEngine.resolveClosestAsabah(input, decisions);

        assertTrue(res.hasAsabah());
        assertEquals(AsabahType.BIL_GHAYR, res.getAsabahType());
        assertEquals("son", res.getPrimaryAsabahKey());
        assertEquals("daughter", res.getSecondaryAsabahKey());
        assertEquals(2, res.getPrimaryUnits());
        assertEquals(2, res.getSecondaryUnits());
        assertEquals(4, res.getTotalUnits());
    }

    @Test
    public void testAsabahMaAlGhayrFullSisterWithDaughters() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setDaughterCount(1);
        input.setFullSisterCount(1);
        input.setPaternalUncleCount(1);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        FaraidAsabahEngine.AsabahResolution res = FaraidAsabahEngine.resolveClosestAsabah(input, decisions);

        assertTrue(res.hasAsabah());
        assertEquals(AsabahType.MA_AL_GHAYR, res.getAsabahType());
        assertEquals("full_sister", res.getPrimaryAsabahKey());
        assertTrue(res.getShariahDalil().contains("اجْعَلُوا الأَخَوَاتِ مَعَ البَنَاتِ عَصَبَةً"));
    }

    @Test
    public void testNephewTakesResidueOverUncle() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setNephewCount(1);
        input.setPaternalUncleCount(2);

        Map<String, HeirHajbDecision> decisions = FaraidHajbEngine.evaluateAll(input);
        FaraidAsabahEngine.AsabahResolution res = FaraidAsabahEngine.resolveClosestAsabah(input, decisions);

        assertTrue(res.hasAsabah());
        assertEquals("nephew", res.getPrimaryAsabahKey());
        assertEquals(AsabahType.BI_NAFSIHI, res.getAsabahType());
    }
}