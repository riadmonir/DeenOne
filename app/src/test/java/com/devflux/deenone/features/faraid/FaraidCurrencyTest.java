package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidCurrencyTest {

    @Test
    public void testCurrencyDoesNotAffectUnderlyingFractions() {
        // Example: Deceased leaves Wife + Mother + 1 Son
        // Total Estate = 600,000

        // 1. Calculate with BDT
        FaraidInput inputBdt = new FaraidInput();
        inputBdt.setDeceasedGender(FaraidInput.Gender.MALE);
        inputBdt.setCurrency(FaraidCurrency.BDT);
        inputBdt.setTotalEstate(600000.0);
        inputBdt.setWifeCount(1);
        inputBdt.setMotherAlive(true);
        inputBdt.setSonCount(1);

        FaraidCalculationResult resBdt = FaraidCalculatorEngine.calculate(inputBdt);

        // 2. Calculate with SAR
        FaraidInput inputSar = new FaraidInput();
        inputSar.setDeceasedGender(FaraidInput.Gender.MALE);
        inputSar.setCurrency(FaraidCurrency.SAR);
        inputSar.setTotalEstate(600000.0);
        inputSar.setWifeCount(1);
        inputSar.setMotherAlive(true);
        inputSar.setSonCount(1);

        FaraidCalculationResult resSar = FaraidCalculatorEngine.calculate(inputSar);

        // 3. Calculate with USD
        FaraidInput inputUsd = new FaraidInput();
        inputUsd.setDeceasedGender(FaraidInput.Gender.MALE);
        inputUsd.setCurrency(FaraidCurrency.USD);
        inputUsd.setTotalEstate(600000.0);
        inputUsd.setWifeCount(1);
        inputUsd.setMotherAlive(true);
        inputUsd.setSonCount(1);

        FaraidCalculationResult resUsd = FaraidCalculatorEngine.calculate(inputUsd);

        // Compare all fractions: they MUST be 100% identical
        assertEquals(resBdt.getBaseDenominator(), resSar.getBaseDenominator());
        assertEquals(resBdt.getBaseDenominator(), resUsd.getBaseDenominator());
        assertEquals(24, resBdt.getBaseDenominator());

        HeirShareResult wifeBdt = resBdt.getHeirShares().stream().filter(h -> "wife".equals(h.getRelationKey())).findFirst().orElse(null);
        HeirShareResult wifeSar = resSar.getHeirShares().stream().filter(h -> "wife".equals(h.getRelationKey())).findFirst().orElse(null);
        HeirShareResult wifeUsd = resUsd.getHeirShares().stream().filter(h -> "wife".equals(h.getRelationKey())).findFirst().orElse(null);

        assertNotNull(wifeBdt);
        assertNotNull(wifeSar);
        assertNotNull(wifeUsd);

        assertEquals(0.125, wifeBdt.getShareFractionNumeric(), 0.0001);
        assertEquals(0.125, wifeSar.getShareFractionNumeric(), 0.0001);
        assertEquals(0.125, wifeUsd.getShareFractionNumeric(), 0.0001);

        assertEquals(75000.0, wifeBdt.getTotalCategoryAmount(), 0.01);
        assertEquals(75000.0, wifeSar.getTotalCategoryAmount(), 0.01);
        assertEquals(75000.0, wifeUsd.getTotalCategoryAmount(), 0.01);

        // Mother: 1/6 of 600,000 = 100,000 across all currencies
        HeirShareResult motherBdt = resBdt.getHeirShares().stream().filter(h -> "mother".equals(h.getRelationKey())).findFirst().orElse(null);
        HeirShareResult motherSar = resSar.getHeirShares().stream().filter(h -> "mother".equals(h.getRelationKey())).findFirst().orElse(null);
        HeirShareResult motherUsd = resUsd.getHeirShares().stream().filter(h -> "mother".equals(h.getRelationKey())).findFirst().orElse(null);

        assertNotNull(motherBdt);
        assertNotNull(motherSar);
        assertNotNull(motherUsd);

        assertEquals(0.16666666666666666, motherBdt.getShareFractionNumeric(), 0.0001);
        assertEquals(0.16666666666666666, motherSar.getShareFractionNumeric(), 0.0001);
        assertEquals(0.16666666666666666, motherUsd.getShareFractionNumeric(), 0.0001);

        assertEquals(100000.0, motherBdt.getTotalCategoryAmount(), 0.01);
        assertEquals(100000.0, motherSar.getTotalCategoryAmount(), 0.01);
        assertEquals(100000.0, motherUsd.getTotalCategoryAmount(), 0.01);
    }

    @Test
    public void testCurrencyFormattingHelper() {
        assertEquals("৳ 600,000.00", FaraidCurrency.BDT.format(600000.0, false));
        assertEquals("﷼ 600,000.00", FaraidCurrency.SAR.format(600000.0, false));
        assertEquals("$ 600,000.00", FaraidCurrency.USD.format(600000.0, false));
        assertEquals("AED 600,000.00", FaraidCurrency.AED.format(600000.0, false));
        assertEquals("€ 600,000.00", FaraidCurrency.EUR.format(600000.0, false));
        assertEquals("£ 600,000.00", FaraidCurrency.GBP.format(600000.0, false));
    }
}