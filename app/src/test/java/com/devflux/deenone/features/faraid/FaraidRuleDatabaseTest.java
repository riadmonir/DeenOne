package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.rules.FaraidRule;
import com.devflux.deenone.features.faraid.rules.FaraidRuleRegistry;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FaraidRuleDatabaseTest {

    @Test
    public void testRegistryContainsCoreQuranicRules() {
        List<FaraidRule> allRules = FaraidRuleRegistry.getAllRules();
        assertNotNull(allRules);
        assertTrue(allRules.size() >= 12);

        // Verify Father 1/6 rule
        FaraidRule fatherRule = FaraidRuleRegistry.getRuleById("RULE_FATHER_ONE_SIXTH_WITH_MALE_CHILD");
        assertNotNull(fatherRule);
        assertEquals(FaraidFraction.ONE_SIXTH, fatherRule.getShare());
        assertFalse(fatherRule.getQuranReferences().isEmpty());
        assertTrue(fatherRule.getQuranReferences().get(0).contains("4:11"));

        // Verify Wife 1/8 rule
        FaraidRule wifeRule = FaraidRuleRegistry.getRuleById("RULE_WIFE_ONE_EIGHTH_WITH_CHILDREN");
        assertNotNull(wifeRule);
        assertEquals(FaraidFraction.ONE_EIGHTH, wifeRule.getShare());
        assertTrue(wifeRule.getQuranReferences().get(0).contains("4:12"));

        // Verify Full Sister 1/2 rule
        FaraidRule sisterRule = FaraidRuleRegistry.getRuleById("RULE_FULL_SISTER_SINGLE_HALF");
        assertNotNull(sisterRule);
        assertEquals(FaraidFraction.HALF, sisterRule.getShare());
        assertTrue(sisterRule.getQuranReferences().get(0).contains("4:176"));

        // Verify Granddaughter 1/6 Takmilat al-Thuluthayn rule
        FaraidRule gdRule = FaraidRuleRegistry.getRuleById("RULE_GRANDDAUGHTER_TAKMILAT_AL_THULUTHAYN");
        assertNotNull(gdRule);
        assertEquals(FaraidFraction.ONE_SIXTH, gdRule.getShare());
        assertFalse(gdRule.getHadithReferences().isEmpty());
        assertTrue(gdRule.getHadithReferences().get(0).contains("6737"));
    }

    @Test
    public void testQueryRulesByHeirAndMadhab() {
        List<FaraidRule> motherRules = FaraidRuleRegistry.getRulesForHeir("mother", FaraidInput.Madhab.HANAFI);
        assertFalse(motherRules.isEmpty());

        for (FaraidRule r : motherRules) {
            assertEquals("mother", r.getHeirType());
            assertTrue(r.appliesToMadhab(FaraidInput.Madhab.HANAFI));
        }
    }

    @Test
    public void testRuleAttributesCompleteness() {
        for (FaraidRule rule : FaraidRuleRegistry.getAllRules()) {
            assertNotNull(rule.getRuleId());
            assertNotNull(rule.getHeirType());
            assertNotNull(rule.getConditionDescription());
            assertNotNull(rule.getShare());
            assertNotNull(rule.getShareType());
            assertNotNull(rule.getExplanationBn());
            assertNotNull(rule.getExplanationEn());
        }
    }
}