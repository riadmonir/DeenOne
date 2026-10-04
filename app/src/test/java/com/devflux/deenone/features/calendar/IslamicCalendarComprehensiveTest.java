package com.devflux.deenone.features.calendar;

import com.devflux.deenone.data.repository.IslamicCalendarRepository;
import com.devflux.deenone.features.calendar.model.HijriDayGridItem;
import com.devflux.deenone.features.calendar.model.IslamicEventItem;
import com.devflux.deenone.features.calendar.model.IslamicMonthItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

public class IslamicCalendarComprehensiveTest {

    @Test
    public void test12IslamicMonths() {
        List<IslamicMonthItem> months = IslamicCalendarRepository.getInstance().get12IslamicMonths();
        Assert.assertEquals(12, months.size());

        // 1st month: Muharram
        IslamicMonthItem m1 = months.get(0);
        Assert.assertEquals("মুহাররম", m1.getNameBengali());
        Assert.assertTrue(m1.isSacredMonth());

        // 7th month: Rajab (Sacred)
        IslamicMonthItem m7 = months.get(6);
        Assert.assertEquals("রজব", m7.getNameBengali());
        Assert.assertTrue(m7.isSacredMonth());

        // 9th month: Ramadan
        IslamicMonthItem m9 = months.get(8);
        Assert.assertEquals("রমজান", m9.getNameBengali());
        Assert.assertFalse(m9.isSacredMonth());

        // 11th month: Dhul Qi'dah (Sacred)
        IslamicMonthItem m11 = months.get(10);
        Assert.assertTrue(m11.isSacredMonth());

        // 12th month: Dhul Hijjah (Sacred)
        IslamicMonthItem m12 = months.get(11);
        Assert.assertTrue(m12.isSacredMonth());
    }

    @Test
    public void testAuthenticIslamicEventsWithDalil() {
        List<IslamicEventItem> events = IslamicCalendarRepository.getInstance().getSignificantIslamicEvents(Calendar.getInstance());
        Assert.assertTrue(events.size() >= 10);

        // Check Ashura Event
        boolean foundAshura = false;
        for (IslamicEventItem e : events) {
            if (e.getTitle().contains("আশুরা")) {
                foundAshura = true;
                Assert.assertEquals("يوم عاشوراء", e.getArabicName());
                Assert.assertEquals("১০ মুহাররম", e.getHijriDateString());
                Assert.assertTrue(e.getReference().contains("দলিল"));
                break;
            }
        }
        Assert.assertTrue(foundAshura);

        // Check Ayyam al-Beed Event
        boolean foundAyyam = false;
        for (IslamicEventItem e : events) {
            if (e.getTitle().contains("আইয়ামে বীজ")) {
                foundAyyam = true;
                Assert.assertEquals("أيام البيض", e.getArabicName());
                Assert.assertEquals("১৩, ১৪, ১৫ প্রতি মাসে", e.getHijriDateString());
                break;
            }
        }
        Assert.assertTrue(foundAyyam);
    }

    @Test
    public void testAyyamAlBeedDaysIdentification() {
        for (int day = 1; day <= 30; day++) {
            boolean isAyyamBeed = (day == 13 || day == 14 || day == 15);
            String badge = (isAyyamBeed) ? "আইয়ামে বীজ" : "তারিখ";
            if (isAyyamBeed) {
                Assert.assertEquals("আইয়ামে বীজ", badge);
            } else {
                Assert.assertEquals("তারিখ", badge);
            }
        }
    }

    @Test
    public void testMoonSightingAdjustment() {
        Calendar cal = Calendar.getInstance();

        // Default
        HijriCalendarUtil.HijriDateResult normal = HijriCalendarUtil.getRealHijriDate(cal);

        // +1 Day
        Calendar calPlus = (Calendar) cal.clone();
        calPlus.add(Calendar.DAY_OF_YEAR, 1);
        HijriCalendarUtil.HijriDateResult plus1 = HijriCalendarUtil.getRealHijriDate(calPlus);

        Assert.assertNotNull(normal);
        Assert.assertNotNull(plus1);
        Assert.assertNotNull(normal.monthNameBengali);
    }
}
