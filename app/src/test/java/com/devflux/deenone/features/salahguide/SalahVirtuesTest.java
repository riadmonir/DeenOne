package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahVirtuesTest {

    @Test
    public void testBengaliVirtuesCountAndContent() {
        List<SalahVirtuesPageDialog.VirtueItem> items = SalahVirtuesPageDialog.getVirtueItems(true);
        assertNotNull(items);
        assertEquals("Must contain 5 virtue cards", 5, items.size());

        // Card 1: ফজর নামাজ
        assertEquals("Card 1 title must match", "ফজর নামাজ", items.get(0).title);
        assertTrue("Card 1 full content must contain Arabic text", items.get(0).fullContent.contains("مَنْ صَلَّى الْبَرْدَيْنِ دَخَلَ الْجَنَّةَ"));
        assertTrue("Card 1 full content must contain pronunciation", items.get(0).fullContent.contains("মান সাল্লাল বার্দাইনী দাখালাল জান্নাহ"));
        assertTrue("Card 1 full content must contain meaning", items.get(0).fullContent.contains("যে ব্যক্তি ফজর ও আসরের নামাজ আদায় করবে, সে জান্নাতে প্রবেশ করবে।"));
        assertTrue("Card 1 full content must contain reference", items.get(0).fullContent.contains("[বুখারি -৫৭৪]"));

        // Card 2: যোহর নামাজ
        assertEquals("Card 2 title must match", "যোহর নামাজ", items.get(1).title);
        assertTrue("Card 2 full content must contain Arabic text", items.get(1).fullContent.contains("إِنَّ أَوَّلَ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ مِنْ عَمَلِهِ الصَّلَاةُ"));
        assertTrue("Card 2 full content must contain pronunciation", items.get(1).fullContent.contains("ইন্না আওয়্বালা মা ইউহাসাবু বিহিল 'আবদু ইয়াওমাল কিয়ামাতি মিন 'আমালিহিস সালাহ"));
        assertTrue("Card 2 full content must contain meaning", items.get(1).fullContent.contains("কিয়ামতের দিন বান্দার প্রথম হিসাব হবে তার নামাজের ব্যাপারে।"));
        assertTrue("Card 2 full content must contain reference", items.get(1).fullContent.contains("[তিরমিজি - ৪১৩]"));

        // Card 3: আসর নামাজ
        assertEquals("Card 3 title must match", "আসর নামাজ", items.get(2).title);
        assertTrue("Card 3 full content must contain Arabic text", items.get(2).fullContent.contains("مَنْ صَلَّى الْعَصْرَ فَهُوَ فِي ذِمَّةِ اللَّهِ"));
        assertTrue("Card 3 full content must contain pronunciation", items.get(2).fullContent.contains("মান সাল্লাল 'আসর ফাহুয়া ফী যিম্মাতিল্লাহ"));
        assertTrue("Card 3 full content must contain meaning", items.get(2).fullContent.contains("যে ব্যক্তি আসরের নামাজ আদায় করবে, সে আল্লাহর নিরাপত্তায় থাকবে।"));
        assertTrue("Card 3 full content must contain reference", items.get(2).fullContent.contains("[মুসলিম - ৬৩৬]"));

        // Card 4: মাগরিব নামাজ
        assertEquals("Card 4 title must match", "মাগরিব নামাজ", items.get(3).title);
        assertTrue("Card 4 full content must contain Arabic text", items.get(3).fullContent.contains("مَنْ صَلَّى الْمَغْرِبَ فِي جَمَاعَةٍ كَانَ لَهُ أَجْرُ حَجَّةٍ مَبْرُورَةٍ"));
        assertTrue("Card 4 full content must contain pronunciation", items.get(3).fullContent.contains("মান সাল্লাল মাগরিবা ফী জামা'আতিন কানা লাহু আজরু হাজ্জাতিন মাবরুরা"));
        assertTrue("Card 4 full content must contain meaning", items.get(3).fullContent.contains("যে ব্যক্তি জামাতের সাথে মাগরিবের নামাজ আদায় করবে, তার জন্য একটি মাকবুল হজের সওয়াব আছে।"));
        assertTrue("Card 4 full content must contain reference", items.get(3).fullContent.contains("[তিরমিজি - ৫৬৪]"));

        // Card 5: ইশা নামাজ
        assertEquals("Card 5 title must match", "ইশা নামাজ", items.get(4).title);
        assertTrue("Card 5 full content must contain Arabic text", items.get(4).fullContent.contains("مَنْ صَلَّى الْعِشَاءَ فِي جَمَاعَةٍ كَانَ كَقِيَامِ نِصْفِ اللَّيْلِ"));
        assertTrue("Card 5 full content must contain pronunciation", items.get(4).fullContent.contains("মান সাল্লাল 'ইশা ফী জামা'আতিন কানা কাকিয়ামি নিসফিল লাইল"));
        assertTrue("Card 5 full content must contain meaning", items.get(4).fullContent.contains("যে ব্যক্তি জামাতের সাথে ইশার নামাজ আদায় করবে, সে যেন অর্ধরাত নামাজ পড়ল।\""));
        assertTrue("Card 5 full content must contain reference", items.get(4).fullContent.contains("[মুসলিম - ৬৫৬]"));

        for (SalahVirtuesPageDialog.VirtueItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishVirtuesCountAndContent() {
        List<SalahVirtuesPageDialog.VirtueItem> items = SalahVirtuesPageDialog.getVirtueItems(false);
        assertNotNull(items);
        assertEquals("Must contain 5 virtue cards", 5, items.size());

        // Card 1: Fajr Prayer
        assertEquals("Card 1 title must match", "Fajr Prayer", items.get(0).title);
        assertTrue("Card 1 full content must contain Arabic text", items.get(0).fullContent.contains("مَنْ صَلَّى الْبَرْدَيْنِ دَخَلَ الْجَنَّةَ"));
        assertTrue("Card 1 full content must contain Bukhari citation", items.get(0).fullContent.contains("[Sahih al-Bukhari - 574]"));

        // Card 2: Dhuhr Prayer
        assertEquals("Card 2 title must match", "Dhuhr Prayer", items.get(1).title);
        assertTrue("Card 2 full content must contain Arabic text", items.get(1).fullContent.contains("إِنَّ أَوَّلَ مَا يُحَاسَبُ بِهِ الْعَبْدُ يَوْمَ الْقِيَامَةِ مِنْ عَمَلِهِ الصَّلَاةُ"));
        assertTrue("Card 2 full content must contain Tirmidhi citation", items.get(1).fullContent.contains("[Jami` at-Tirmidhi - 413]"));

        // Card 3: Asr Prayer
        assertEquals("Card 3 title must match", "Asr Prayer", items.get(2).title);
        assertTrue("Card 3 full content must contain Arabic text", items.get(2).fullContent.contains("مَنْ صَلَّى الْعَصْرَ فَهُوَ فِي ذِمَّةِ اللَّهِ"));
        assertTrue("Card 3 full content must contain Muslim citation", items.get(2).fullContent.contains("[Sahih Muslim - 636]"));

        // Card 4: Maghrib Prayer
        assertEquals("Card 4 title must match", "Maghrib Prayer", items.get(3).title);
        assertTrue("Card 4 full content must contain Arabic text", items.get(3).fullContent.contains("مَنْ صَلَّى الْمَغْرِبَ فِي جَمَاعَةٍ كَانَ لَهُ أَجْرُ حَجَّةٍ مَبْرُورَةٍ"));
        assertTrue("Card 4 full content must contain Tirmidhi citation", items.get(3).fullContent.contains("[Jami` at-Tirmidhi - 564]"));

        // Card 5: Isha Prayer
        assertEquals("Card 5 title must match", "Isha Prayer", items.get(4).title);
        assertTrue("Card 5 full content must contain Arabic text", items.get(4).fullContent.contains("مَنْ صَلَّى الْعِشَاءَ فِي جَمَاعَةٍ كَانَ كَقِيَامِ نِصْفِ اللَّيْلِ"));
        assertTrue("Card 5 full content must contain Muslim citation", items.get(4).fullContent.contains("[Sahih Muslim - 656]"));

        for (SalahVirtuesPageDialog.VirtueItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
