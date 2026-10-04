package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.model.FaraidDisclaimerHelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidDisclaimerTest {

    @Test
    public void testDisclaimerTextsConformToRequirements() {
        // Requirement 25 General Disclaimer
        assertNotNull(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);
        assertTrue(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN.contains("educational and informational purposes"));
        assertTrue(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN.contains("qualified Islamic inheritance scholar"));

        assertNotNull(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN);
        assertTrue(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN.contains("শিক্ষামূলক এবং প্রাথমিক"));
        assertTrue(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN.contains("উত্তরাধিকার"));

        // High-risk and disputed warning
        assertNotNull(FaraidDisclaimerHelper.HIGH_RISK_WARNING_EN);
        assertTrue(FaraidDisclaimerHelper.HIGH_RISK_WARNING_EN.contains("Do not distribute the estate based solely on this calculation"));

        assertNotNull(FaraidDisclaimerHelper.HIGH_RISK_WARNING_BN);
        assertTrue(FaraidDisclaimerHelper.HIGH_RISK_WARNING_BN.contains("শুধুমাত্র এই গণনার উপর ভিত্তি করে সম্পত্তি বণ্টন করবেন না"));

        // Non-Fatwa statement
        assertNotNull(FaraidDisclaimerHelper.NON_FATWA_STATEMENT);
        assertTrue(FaraidDisclaimerHelper.NON_FATWA_STATEMENT.contains("NOT an official Fatwa"));
    }
}