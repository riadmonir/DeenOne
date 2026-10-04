package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidExactFractionTest {

    @Test
    public void testRationalArithmeticOperations() {
        // 1/2 + 1/3 = 5/6
        FaraidFraction half = FaraidFraction.HALF;
        FaraidFraction third = FaraidFraction.ONE_THIRD;
        FaraidFraction sum = half.add(third);
        assertEquals("5/6", sum.toString());

        // 1 - 5/6 = 1/6
        FaraidFraction remainder = FaraidFraction.ONE.subtract(sum);
        assertEquals("1/6", remainder.toString());

        // 1/6 * 1/2 = 1/12
        FaraidFraction product = FaraidFraction.ONE_SIXTH.multiply(FaraidFraction.HALF);
        assertEquals("1/12", product.toString());

        // 1/2 / (7/6) = 3/7 (Awl scaling)
        FaraidFraction sevenSixths = new FaraidFraction(7, 6);
        FaraidFraction scaled = FaraidFraction.HALF.divide(sevenSixths);
        assertEquals("3/7", scaled.toString());
    }

    @Test
    public void testExactFractionToPercentageFormatting() {
        // 1/6 = 16.6666666667... -> Formatted display = 16.67%
        FaraidFraction oneSixth = FaraidFraction.ONE_SIXTH;
        assertEquals(0.16666666666666666, oneSixth.toDouble(), 0.0000000001);
        assertEquals("16.67%", oneSixth.toPercentageString());
        assertEquals(BengaliNumberUtil.toBengali("16.67") + "%", oneSixth.toBengaliPercentageString());

        // 1/3 = 33.33%
        FaraidFraction oneThird = FaraidFraction.ONE_THIRD;
        assertEquals("33.33%", oneThird.toPercentageString());
        assertEquals(BengaliNumberUtil.toBengali("33.33") + "%", oneThird.toBengaliPercentageString());

        // 1/8 = 12.5%
        FaraidFraction oneEighth = FaraidFraction.ONE_EIGHTH;
        assertEquals("12.5%", oneEighth.toPercentageString());
        assertEquals(BengaliNumberUtil.toBengali("12.5") + "%", oneEighth.toBengaliPercentageString());
    }

    @Test
    public void testSimplificationAndGcdLcm() {
        FaraidFraction unsimplified = new FaraidFraction(8, 24);
        assertEquals(1, unsimplified.getNumerator());
        assertEquals(3, unsimplified.getDenominator());
        assertEquals("1/3", unsimplified.toString());

        assertEquals(6, FaraidFraction.gcd(18, 24));
        assertEquals(24, FaraidFraction.lcm(8, 12));
    }
}