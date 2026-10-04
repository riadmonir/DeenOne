package com.devflux.deenone.features.faraid;

import com.devflux.deenone.features.faraid.engine.FaraidCitationHelper;
import com.devflux.deenone.features.faraid.engine.FaraidDalilRepository;

import org.junit.Test;

import static org.junit.Assert.*;

public class FaraidCitationTest {

    @Test
    public void testExactSourceUrls() {
        assertEquals("https://quran.com/4/11", FaraidCitationHelper.URL_QURAN_4_11);
        assertEquals("https://quran.com/4/12", FaraidCitationHelper.URL_QURAN_4_12);
        assertEquals("https://quran.com/4/176", FaraidCitationHelper.URL_QURAN_4_176);
        assertEquals("https://sunnah.com/bukhari:6732", FaraidCitationHelper.URL_BUKHARI_6732);
        assertEquals("https://sunnah.com/bukhari:6737", FaraidCitationHelper.URL_BUKHARI_6737);
        assertEquals("https://sunnah.com/muslim:1615a", FaraidCitationHelper.URL_MUSLIM_1615A);
    }

    @Test
    public void testHeirDalilContainsCorrectUrls() {
        // Mother: Quran 4:11
        FaraidDalilRepository.DalilItem motherDalil = FaraidDalilRepository.getMotherDalil(true, false);
        assertEquals("https://quran.com/4/11", motherDalil.getSourceUrl());

        // Wife: Quran 4:12
        FaraidDalilRepository.DalilItem wifeDalil = FaraidDalilRepository.getSpouseDalil(true, true);
        assertEquals("https://quran.com/4/12", wifeDalil.getSourceUrl());

        // Full Sister: Quran 4:176
        FaraidDalilRepository.DalilItem sisterDalil = FaraidDalilRepository.getSiblingsDalil(true, false);
        assertEquals("https://quran.com/4/176", sisterDalil.getSourceUrl());

        // Granddaughter: Bukhari 6737
        assertEquals("https://sunnah.com/bukhari:6737", FaraidDalilRepository.GRANDDAUGHTER_DALIL.getSourceUrl());
    }

    @Test
    public void testCitationResolver() {
        assertEquals("https://quran.com/4/11", FaraidCitationHelper.getSourceUrl("Qur'an 4:11"));
        assertEquals("https://quran.com/4/12", FaraidCitationHelper.getSourceUrl("সূরা নিসা ৪:১২"));
        assertEquals("https://quran.com/4/176", FaraidCitationHelper.getSourceUrl("Surah An-Nisa 4:176"));
        assertEquals("https://sunnah.com/bukhari:6732", FaraidCitationHelper.getSourceUrl("Sahih al-Bukhari 6732"));
        assertEquals("https://sunnah.com/bukhari:6737", FaraidCitationHelper.getSourceUrl("Bukhari 6737"));
        assertEquals("https://sunnah.com/muslim:1615a", FaraidCitationHelper.getSourceUrl("Sahih Muslim 1615a"));
    }
}