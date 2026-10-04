package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.devflux.deenone.features.salahguide.data.SalahHistoryRepository;
import com.devflux.deenone.features.salahguide.model.SalahHistoryItem;
import com.devflux.deenone.features.salahguide.model.SalahHistorySection;

import org.junit.Test;

import java.util.List;

public class SalahHistoryRepositoryTest {

    @Test
    public void testAllSixHistoryTopicsExist() {
        String[] requiredIds = {
                "history_five_waqts",
                "history_when_started",
                "history_why_five_times",
                "history_fifty_to_five",
                "history_fifty_reward",
                "history_prophets_salah"
        };

        List<SalahHistoryItem> allItems = SalahHistoryRepository.getAllItems();
        assertNotNull(allItems);
        assertEquals("Must contain exactly 6 core history topics", 6, allItems.size());

        for (String id : requiredIds) {
            SalahHistoryItem item = SalahHistoryRepository.getItem(id);
            assertNotNull("Item must exist for id: " + id, item);
            assertEquals("Id must match", id, item.getId());
            assertFalse("Title must not be empty", item.getTitle().isEmpty());
            assertFalse("Badge must not be empty", item.getBadge().isEmpty());
            assertFalse("Summary must not be empty", item.getSummary().isEmpty());
            assertNotNull("Sections must not be null", item.getSections());
            assertTrue("Sections must have at least 2 sections", item.getSections().size() >= 2);

            for (SalahHistorySection sec : item.getSections()) {
                assertNotNull("Heading must not be null", sec.getHeading());
                assertFalse("Heading must not be empty", sec.getHeading().isEmpty());
                assertNotNull("Bengali text must not be null", sec.getBengaliText());
                assertFalse("Bengali text must not be empty", sec.getBengaliText().isEmpty());
                assertNotNull("Reference must not be null", sec.getReference());
                assertNotNull("Arabic text must not be null", sec.getArabicText());
            }
        }
    }

    @Test
    public void testProphetsSalahHistoryDetails() {
        SalahHistoryItem prophetsItem = SalahHistoryRepository.getItem("history_prophets_salah");
        assertNotNull(prophetsItem);
        assertTrue(prophetsItem.getTitle().contains("কোন নবীর"));

        boolean hasAdam = false;
        boolean hasYunusOrIbrahim = false;
        boolean hasUzayr = false;
        boolean hasDawud = false;
        boolean hasMusa = false;

        for (SalahHistorySection sec : prophetsItem.getSections()) {
            if (sec.getHeading().contains("আদম")) hasAdam = true;
            if (sec.getHeading().contains("যোহর")) hasYunusOrIbrahim = true;
            if (sec.getHeading().contains("উযাইর")) hasUzayr = true;
            if (sec.getHeading().contains("দাউদ")) hasDawud = true;
            if (sec.getHeading().contains("মূসা")) hasMusa = true;
        }

        assertTrue("Must include Adam (A.) for Fajr", hasAdam);
        assertTrue("Must include Dhuhr history", hasYunusOrIbrahim);
        assertTrue("Must include Uzayr (A.) for Asr", hasUzayr);
        assertTrue("Must include Dawud (A.) for Maghrib", hasDawud);
        assertTrue("Must include Musa (A.) for Isha", hasMusa);
    }
}
