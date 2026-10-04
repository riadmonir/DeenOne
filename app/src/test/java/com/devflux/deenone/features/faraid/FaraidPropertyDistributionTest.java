package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Dedicated unit test suite verifying Land & Real Estate Property (স্থাবর সম্পত্তি / জমি)
 * distribution alongside monetary estate (অর্থ) in the Islamic Inheritance (Faraid) Engine.
 */
public class FaraidPropertyDistributionTest {

    @Test
    public void testSimultaneousCashAndLandDistributionAsabah() {
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(2400000.0); // 24 Lakh BDT
        input.setTotalProperty(24.0);    // 24.00 Shatak land
        input.setPropertyUnit("শতক");

        input.setWifeCount(1);
        input.setSonCount(1);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);

        assertNotNull(result);
        assertEquals(2400000.0, result.getGrossEstate(), 0.001);
        assertEquals(24.0, result.getGrossProperty(), 0.001);
        assertEquals(24.0, result.getNetDistributableProperty(), 0.001);
        assertEquals("শতক", result.getPropertyUnit());

        List<HeirShareResult> shares = result.getHeirShares();
        assertEquals(3, shares.size());

        // 1. Wife: 1/8 -> Cash = 3,00,000; Land = 3.00 Shatak
        HeirShareResult wife = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("wife")).findFirst().orElse(null);
        assertNotNull(wife);
        assertEquals(300000.0, wife.getTotalCategoryAmount(), 0.001);
        assertEquals(3.0, wife.getTotalPropertyAmount(), 0.001);
        assertEquals(3.0, wife.getIndividualPropertyAmount(), 0.001);
        assertTrue(wife.getTotalPropertyFormatted().contains("শতক"));

        // 2. Son: 2/3 of 7/8 = 14/24 -> Cash = 14,00,000; Land = 14.00 Shatak
        HeirShareResult son = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("son")).findFirst().orElse(null);
        assertNotNull(son);
        assertEquals(1400000.0, son.getTotalCategoryAmount(), 0.001);
        assertEquals(14.0, son.getTotalPropertyAmount(), 0.001);

        // 3. Daughter: 1/3 of 7/8 = 7/24 -> Cash = 7,00,000; Land = 7.00 Shatak
        HeirShareResult daughter = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("daughter")).findFirst().orElse(null);
        assertNotNull(daughter);
        assertEquals(700000.0, daughter.getTotalCategoryAmount(), 0.001);
        assertEquals(7.0, daughter.getTotalPropertyAmount(), 0.001);

        // Sum of land shares must equal total land
        double totalDistributedLand = shares.stream().mapToDouble(HeirShareResult::getTotalPropertyAmount).sum();
        assertEquals(24.0, totalDistributedLand, 0.001);
    }

    @Test
    public void testMultipleHeirsIndividualLandShare() {
        // 2 Wives + 2 Sons + 2 Daughters with 96 Katha of land
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(4800000.0);
        input.setTotalProperty(96.0);
        input.setPropertyUnit("কাঠা");

        input.setWifeCount(2);
        input.setSonCount(2);
        input.setDaughterCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);

        // Wives share 1/8 of 96 = 12 Katha total. Each wife gets 6 Katha.
        HeirShareResult wife = result.getHeirShares().stream().filter(s -> s.getRelationKey().equalsIgnoreCase("wife")).findFirst().orElse(null);
        assertNotNull(wife);
        assertEquals(2, wife.getCount());
        assertEquals(12.0, wife.getTotalPropertyAmount(), 0.001);
        assertEquals(6.0, wife.getIndividualPropertyAmount(), 0.001);
        assertEquals("কাঠা", wife.getPropertyUnit());

        // Remainder = 84 Katha. 2 Sons (weight 4) + 2 Daughters (weight 2) = 6 parts.
        // Each part = 84 / 6 = 14 Katha.
        // Sons get 4 parts = 56 Katha total, 28 Katha each.
        HeirShareResult son = result.getHeirShares().stream().filter(s -> s.getRelationKey().equalsIgnoreCase("son")).findFirst().orElse(null);
        assertNotNull(son);
        assertEquals(2, son.getCount());
        assertEquals(56.0, son.getTotalPropertyAmount(), 0.001);
        assertEquals(28.0, son.getIndividualPropertyAmount(), 0.001);

        // Daughters get 2 parts = 28 Katha total, 14 Katha each.
        HeirShareResult daughter = result.getHeirShares().stream().filter(s -> s.getRelationKey().equalsIgnoreCase("daughter")).findFirst().orElse(null);
        assertNotNull(daughter);
        assertEquals(2, daughter.getCount());
        assertEquals(28.0, daughter.getTotalPropertyAmount(), 0.001);
        assertEquals(14.0, daughter.getIndividualPropertyAmount(), 0.001);
    }

    @Test
    public void testLandDistributionWithAwl() {
        // Awl case: Husband (1/2 = 3/6), 2 Full Sisters (2/3 = 4/6), Mother (1/6)
        // Sum = 3/6 + 4/6 + 1/6 = 8/6 (Awl from 6 to 8)
        // Total Land = 80 Bigha
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.FEMALE);
        input.setTotalEstate(800000.0);
        input.setTotalProperty(80.0);
        input.setPropertyUnit("বিঘা");

        input.setHusbandAlive(true);
        input.setMotherAlive(true);
        input.setFullSisterCount(2);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);
        assertTrue(result.isAwlApplied());

        // Under Awl (denominator 8):
        // Husband: 3/8 -> 30 Bigha
        // Mother: 1/8 -> 10 Bigha
        // 2 Sisters: 4/8 -> 40 Bigha (20 Bigha each)
        List<HeirShareResult> shares = result.getHeirShares();
        double sumLand = shares.stream().mapToDouble(HeirShareResult::getTotalPropertyAmount).sum();
        assertEquals(80.0, sumLand, 0.001);

        HeirShareResult husband = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("husband")).findFirst().orElse(null);
        assertNotNull(husband);
        assertEquals(30.0, husband.getTotalPropertyAmount(), 0.001);

        HeirShareResult mother = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("mother")).findFirst().orElse(null);
        assertNotNull(mother);
        assertEquals(10.0, mother.getTotalPropertyAmount(), 0.001);

        HeirShareResult sisters = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("full_sister") || s.getRelationKey().equalsIgnoreCase("fullSister")).findFirst().orElse(null);
        assertNotNull(sisters);
        assertEquals(40.0, sisters.getTotalPropertyAmount(), 0.001);
        assertEquals(20.0, sisters.getIndividualPropertyAmount(), 0.001);
    }

    @Test
    public void testLandDistributionWithRadd() {
        // Radd case: Mother (1/6) + 1 Daughter (1/2 = 3/6)
        // Sum = 4/6. Remainder = 2/6 returned proportionally.
        // Ratio is Mother 1 : Daughter 3 -> Total 4 parts.
        // Mother gets 1/4, Daughter gets 3/4.
        // Total Land = 100 Acre
        FaraidInput input = new FaraidInput();
        input.setDeceasedGender(FaraidInput.Gender.MALE);
        input.setTotalEstate(400000.0);
        input.setTotalProperty(100.0);
        input.setPropertyUnit("একর");

        input.setMotherAlive(true);
        input.setDaughterCount(1);

        FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
        assertNotNull(result);
        assertTrue(result.isRaddApplied());

        List<HeirShareResult> shares = result.getHeirShares();
        double sumLand = shares.stream().mapToDouble(HeirShareResult::getTotalPropertyAmount).sum();
        assertEquals(100.0, sumLand, 0.001);

        HeirShareResult mother = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("mother")).findFirst().orElse(null);
        assertNotNull(mother);
        assertEquals(25.0, mother.getTotalPropertyAmount(), 0.001);

        HeirShareResult daughter = shares.stream().filter(s -> s.getRelationKey().equalsIgnoreCase("daughter")).findFirst().orElse(null);
        assertNotNull(daughter);
        assertEquals(75.0, daughter.getTotalPropertyAmount(), 0.001);
    }
}
