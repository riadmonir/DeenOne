package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Exact Rational Arithmetic class for classical Islamic Inheritance (Faraid) calculations.
 * Supports exact currency formatting (Requirements 21, 22, 23).
 */
public class FaraidFraction implements Serializable, Comparable<FaraidFraction> {

    private static final DecimalFormat PCT_FORMAT = new DecimalFormat("0.##");

    public static final FaraidFraction ZERO = new FaraidFraction(0, 1);
    public static final FaraidFraction ONE = new FaraidFraction(1, 1);
    public static final FaraidFraction HALF = new FaraidFraction(1, 2);
    public static final FaraidFraction ONE_HALF = HALF;
    public static final FaraidFraction ONE_FOURTH = new FaraidFraction(1, 4);
    public static final FaraidFraction ONE_EIGHTH = new FaraidFraction(1, 8);
    public static final FaraidFraction TWO_THIRDS = new FaraidFraction(2, 3);
    public static final FaraidFraction ONE_THIRD = new FaraidFraction(1, 3);
    public static final FaraidFraction ONE_SIXTH = new FaraidFraction(1, 6);

    private final long num;
    private final long den;

    public FaraidFraction(long numerator, long denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero in Faraid arithmetic.");
        }
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        long common = gcd(Math.abs(numerator), denominator);
        this.num = numerator / common;
        this.den = denominator / common;
    }

    public long getNumerator() { return num; }
    public long getDenominator() { return den; }

    public static FaraidFraction fromDouble(double val) {
        if (val <= 0.0) return ZERO;
        if (Math.abs(val - 1.0) < 1e-6) return ONE;
        long[] denoms = {2, 3, 4, 5, 6, 7, 8, 12, 13, 14, 15, 16, 17, 24, 27, 32, 36, 48, 72};
        for (long d : denoms) {
            long n = Math.round(val * d);
            if (Math.abs((double) n / d - val) < 1e-4) {
                return new FaraidFraction(n, d);
            }
        }
        long scale = 10000;
        return new FaraidFraction(Math.round(val * scale), scale);
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a != 0 ? a : 1;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a * (b / gcd(a, b)));
    }

    public FaraidFraction add(FaraidFraction other) {
        long commonDen = lcm(this.den, other.den);
        long newNum = (this.num * (commonDen / this.den)) + (other.num * (commonDen / other.den));
        return new FaraidFraction(newNum, commonDen);
    }

    public FaraidFraction subtract(FaraidFraction other) {
        long commonDen = lcm(this.den, other.den);
        long newNum = (this.num * (commonDen / this.den)) - (other.num * (commonDen / other.den));
        return new FaraidFraction(newNum, commonDen);
    }

    public FaraidFraction multiply(FaraidFraction other) {
        return new FaraidFraction(this.num * other.num, this.den * other.den);
    }

    public FaraidFraction multiply(long scalar) {
        return new FaraidFraction(this.num * scalar, this.den);
    }

    public FaraidFraction divide(FaraidFraction other) {
        if (other.num == 0) {
            throw new ArithmeticException("Cannot divide by zero fraction in Faraid.");
        }
        return new FaraidFraction(this.num * other.den, this.den * other.num);
    }

    public FaraidFraction divide(long scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Cannot divide by zero in Faraid.");
        }
        return new FaraidFraction(this.num, this.den * scalar);
    }

    public double toDouble() {
        return (double) num / (double) den;
    }

    public String toPercentageString() {
        double pct = toDouble() * 100.0;
        return PCT_FORMAT.format(pct) + "%";
    }

    public String toBengaliPercentageString() {
        double pct = toDouble() * 100.0;
        return BengaliNumberUtil.toBengali(PCT_FORMAT.format(pct)) + "%";
    }

    public String toBengaliFormulaString(FaraidCurrency currency, double netEstate, double resultAmount) {
        FaraidCurrency curr = (currency != null) ? currency : FaraidCurrency.BDT;
        String fracStr = toBengaliString();
        String netStr = curr.format(netEstate, true);
        String resStr = curr.format(resultAmount, true);
        return fracStr + " × " + netStr + " = " + resStr;
    }

    public String toEnglishFormulaString(FaraidCurrency currency, double netEstate, double resultAmount) {
        FaraidCurrency curr = (currency != null) ? currency : FaraidCurrency.BDT;
        String fracStr = toString();
        String netStr = curr.format(netEstate, false);
        String resStr = curr.format(resultAmount, false);
        return fracStr + " × " + netStr + " = " + resStr;
    }

    public String toBengaliFormulaString(double netEstate, double resultAmount) {
        return toBengaliFormulaString(FaraidCurrency.BDT, netEstate, resultAmount);
    }

    public String toEnglishFormulaString(double netEstate, double resultAmount) {
        return toEnglishFormulaString(FaraidCurrency.BDT, netEstate, resultAmount);
    }

    public String toBengaliString() {
        if (num == 0) return "০";
        if (den == 1) return BengaliNumberUtil.toBengali(String.valueOf(num));
        return BengaliNumberUtil.toBengali(String.valueOf(num)) + "/" + BengaliNumberUtil.toBengali(String.valueOf(den));
    }

    public String toEnglishString() {
        if (num == 0) return "0";
        if (den == 1) return String.valueOf(num);
        return num + "/" + den;
    }

    public String toString(boolean isBn) {
        return isBn ? toBengaliString() : toEnglishString();
    }

    @Override
    public int compareTo(FaraidFraction o) {
        long lhs = this.num * o.den;
        long rhs = o.num * this.den;
        return Long.compare(lhs, rhs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FaraidFraction)) return false;
        FaraidFraction other = (FaraidFraction) obj;
        return this.num == other.num && this.den == other.den;
    }

    @Override
    public int hashCode() {
        return (int) (31 * num + den);
    }

    @Override
    public String toString() {
        return num + "/" + den;
    }
}