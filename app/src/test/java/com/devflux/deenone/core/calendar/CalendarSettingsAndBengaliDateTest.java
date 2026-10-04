package com.devflux.deenone.core.calendar;

import com.devflux.deenone.utils.BengaliCalendarUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CalendarSettingsAndBengaliDateTest {

    @Test
    public void testBengaliCalendarDateConversion_PahelaBoishakh() {
        Calendar cal = Calendar.getInstance();
        // 14 April 2026 -> 1 Boishakh 1433
        cal.set(2026, Calendar.APRIL, 14, 10, 0, 0);

        BengaliCalendarUtil.BengaliDateResult result = BengaliCalendarUtil.getBengaliDate(cal);
        assertNotNull(result);
        assertEquals(1, result.day);
        assertEquals(0, result.monthIndex);
        assertEquals("বৈশাখ", result.monthName);
        assertEquals("গ্রীষ্মকাল", result.seasonName);
        assertEquals(1433, result.year);
    }

    @Test
    public void testBengaliCalendarDateConversion_BeforePohelaBoishakh() {
        Calendar cal = Calendar.getInstance();
        // 13 April 2026 -> 30 Choitro 1432
        cal.set(2026, Calendar.APRIL, 13, 10, 0, 0);

        BengaliCalendarUtil.BengaliDateResult result = BengaliCalendarUtil.getBengaliDate(cal);
        assertNotNull(result);
        assertEquals(30, result.day);
        assertEquals(11, result.monthIndex);
        assertEquals("চৈত্র", result.monthName);
        assertEquals("বসন্তকাল", result.seasonName);
        assertEquals(1432, result.year);
    }

    @Test
    public void testBengaliCalendarDateConversion_CurrentDate() {
        Calendar cal = Calendar.getInstance();
        // 31 August 2026 -> 16 Bhadro 1433
        cal.set(2026, Calendar.AUGUST, 31, 10, 0, 0);

        BengaliCalendarUtil.BengaliDateResult result = BengaliCalendarUtil.getBengaliDate(cal);
        assertNotNull(result);
        assertEquals(16, result.day);
        assertEquals(4, result.monthIndex);
        assertEquals("ভাদ্র", result.monthName);
        assertEquals("শরৎকাল", result.seasonName);
        assertEquals(1433, result.year);
        assertTrue(result.getFormattedDate().contains("১৬ ভাদ্র ১৪৩৩ বঙ্গাব্দ"));
    }

    @Test
    public void testHijriCalendarUtil_Calculation() {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.AUGUST, 31, 10, 0, 0);

        HijriCalendarUtil.HijriDateResult result = HijriCalendarUtil.getRealHijriDate(cal);
        assertNotNull(result);
        assertTrue(result.day > 0);
        assertNotNull(result.monthNameBengali);
        assertTrue(result.year >= 1448);
    }

    @Test
    public void testIndianDrikSiddhanta_PohelaBoishakhOnApril15() {
        Calendar cal = Calendar.getInstance();
        // 14 April 2026 -> 31 Chaitra 1432 in Indian Drik Siddhanta
        cal.set(2026, Calendar.APRIL, 14, 10, 0, 0);
        BengaliCalendarUtil.BengaliDateResult result14 = BengaliCalendarUtil.getBengaliDate(
                cal, BengaliCalendarUtil.CalculationMethod.INDIAN_DRIK_SIDDHANTA
        );
        assertEquals(31, result14.day);
        assertEquals(11, result14.monthIndex);
        assertEquals("চৈত্র", result14.monthName);
        assertEquals(1432, result14.year);

        // 15 April 2026 -> 1 Boishakh 1433 in Indian Drik Siddhanta
        cal.set(2026, Calendar.APRIL, 15, 10, 0, 0);
        BengaliCalendarUtil.BengaliDateResult result15 = BengaliCalendarUtil.getBengaliDate(
                cal, BengaliCalendarUtil.CalculationMethod.INDIAN_DRIK_SIDDHANTA
        );
        assertEquals(1, result15.day);
        assertEquals(0, result15.monthIndex);
        assertEquals("বৈশাখ", result15.monthName);
        assertEquals(1433, result15.year);
    }

    @Test
    public void testCalendarSettingsManager_Enums() {
        assertEquals("Managed by DeenOne", CalendarSettingsManager.HijriDateSource.DEENONE_MANAGED.getDisplayName());
        assertEquals("Managed by DeenOne", CalendarSettingsManager.HijriDateSource.DEANONE_MANAGED.getDisplayName());
        assertEquals("At Midnight", CalendarSettingsManager.HijriChangeTiming.MIDNIGHT_12AM.getDisplayName());
        assertEquals("After Sunset", CalendarSettingsManager.HijriChangeTiming.MAGHRIB_SUNSET.getDisplayName());
        assertEquals("Bangla Academy", BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY.getDisplayName());
        assertEquals("Indian Drik Siddhanta", BengaliCalendarUtil.CalculationMethod.INDIAN_DRIK_SIDDHANTA.getDisplayName());
    }
}
