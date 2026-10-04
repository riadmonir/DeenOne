package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.devflux.deenone.features.salahguide.data.NamazContentRepository;
import com.devflux.deenone.features.salahguide.model.GenericSalahSection;
import com.devflux.deenone.features.salahguide.model.GenericSalahTopic;

import org.junit.Test;

public class NamazContentRepositoryTest {

    @Test
    public void testAllRequiredTopicsExist() {
        String[] topicIds = {
                "salah_history",
                "various_salah",
                "azan_iqamah",
                "purity_wudu",
                "salah_dua",
                "purity_istinja",
                "purity_ghusl",
                "purity_tayammum",
                "dua_wudu",
                "dua_sana",
                "dua_waswasa",
                "dua_ruku",
                "dua_qawmah",
                "dua_sajdah",
                "dua_jalsah",
                "dua_tashahhud",
                "dua_durood",
                "dua_masura",
                "dua_qunut",
                "salah_basics",
                "salah_preparation",
                "salah_essential_rules",
                "salah_etiquettes",
                "salah_virtues",
                "salah_nafl_timings",
                "salah_faq",
                "masail_azan",
                "masail_wudu",
                "masail_namaz",
                "masail_tarabi",
                "masail_qadha",
                "masail_sahu_sajdah",
                "masail_masjid"
        };

        for (String id : topicIds) {
            GenericSalahTopic topic = NamazContentRepository.getTopic(id);
            assertNotNull("Topic should exist for id: " + id, topic);
            assertEquals("Topic ID must match", id, topic.getId());
            assertFalse("Topic title must not be empty", topic.getTitle().isEmpty());
            assertFalse("Topic badge must not be empty", topic.getBadge().isEmpty());
            assertFalse("Topic overview description must not be empty", topic.getOverviewDescription().isEmpty());
            assertNotNull("Topic sections list must not be null", topic.getSections());
            assertFalse("Topic must contain at least 1 section: " + id, topic.getSections().isEmpty());

            for (GenericSalahSection section : topic.getSections()) {
                assertNotNull("Section title cannot be null", section.getTitle());
                assertFalse("Section title cannot be empty", section.getTitle().trim().isEmpty());
                assertNotNull("Section description cannot be null", section.getDescription());
                assertNotNull("Section arabic cannot be null", section.getArabic());
                assertNotNull("Section translation cannot be null", section.getTranslation());
                assertNotNull("Section reference cannot be null", section.getReference());
            }
        }
    }

    @Test
    public void testSalahHistoryContentIntegrity() {
        GenericSalahTopic history = NamazContentRepository.getTopic("salah_history");
        assertNotNull(history);
        assertTrue(history.getSections().size() >= 3);

        boolean hasMeraj = false;
        for (GenericSalahSection section : history.getSections()) {
            if (section.getTitle().contains("মেরাজ")) {
                hasMeraj = true;
                break;
            }
        }
        assertTrue("Must contain Meraj section", hasMeraj);
    }
}
