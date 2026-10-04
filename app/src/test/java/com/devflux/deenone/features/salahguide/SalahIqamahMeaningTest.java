package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIqamahMeaningTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahIqamahMeaningPageDialog.StepItem> items = SalahIqamahMeaningPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 3 items for Iqamah Meaning in Bengali", 3, items.size());

        // Card 1: Verbatim Sentences
        assertEquals("ইকামতের অর্থ", items.get(0).title);
        assertTrue("Card 1 should contain Allah Almighty", items.get(0).fullContent.contains("আল্লাহ সর্বশক্তিমান"));
        assertTrue("Card 1 should contain Tawheed testimony", items.get(0).fullContent.contains("আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই"));
        assertTrue("Card 1 should contain Messenger testimony", items.get(0).fullContent.contains("আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত"));
        assertTrue("Card 1 should contain Come to Prayer", items.get(0).fullContent.contains("নামাজের জন্য এসো"));
        assertTrue("Card 1 should contain Come to Success", items.get(0).fullContent.contains("সাফল্যের জন্য এসো"));
        assertTrue("Card 1 should contain Prayer Started", items.get(0).fullContent.contains("নামাজ আরম্ভ হলো"));
        assertTrue("Card 1 should contain Allah is Great", items.get(0).fullContent.contains("আল্লাহ্ মহান"));
        assertTrue("Card 1 should contain No deity except Allah", items.get(0).fullContent.contains("আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই"));

        // Card 2: Sentences breakdown
        assertEquals("ইকামতের বাক্যাবলীর বাংলা অর্থ", items.get(1).title);
        assertTrue("Card 2 should contain breakdown", items.get(1).fullContent.contains("ক্বাদ ক্বামাতিস সালাহ: নামাজ আরম্ভ হলো"));

        // Card 3: Iqamah Special Sentence
        assertEquals("ইকামতের বিশেষ বাক্য", items.get(2).title);
        assertTrue("Card 3 should contain Iqamah sentence", items.get(2).fullContent.contains("নামাজ আরম্ভ হলো"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahIqamahMeaningPageDialog.StepItem> items = SalahIqamahMeaningPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 3 items for Iqamah Meaning in English", 3, items.size());

        // Card 1: English Sentences
        assertEquals("Meaning of Iqamah", items.get(0).title);
        assertTrue("Card 1 should contain Allah is Almighty", items.get(0).fullContent.contains("Allah is Almighty"));
        assertTrue("Card 1 should contain Tawheed", items.get(0).fullContent.contains("I testify that there is no god but Allah"));
        assertTrue("Card 1 should contain Messenger", items.get(0).fullContent.contains("I testify that Muhammad is the Messenger sent by Allah"));
        assertTrue("Card 1 should contain Come to prayer", items.get(0).fullContent.contains("Come to prayer"));
        assertTrue("Card 1 should contain Come to success", items.get(0).fullContent.contains("Come to success"));
        assertTrue("Card 1 should contain Prayer begun", items.get(0).fullContent.contains("The prayer has begun"));
        assertTrue("Card 1 should contain Allah is the Greatest", items.get(0).fullContent.contains("Allah is the Greatest"));
        assertTrue("Card 1 should contain There is no deity except Allah", items.get(0).fullContent.contains("There is no deity except Allah"));

        // Card 2: Breakdown
        assertEquals("Sentences and Meaning of Iqamah", items.get(1).title);
        assertTrue("Card 2 should contain breakdown", items.get(1).fullContent.contains("Qad Qamatis-Salah: The prayer has begun"));

        // Card 3: Special Sentence
        assertEquals("Special Sentence for Iqamah", items.get(2).title);
        assertTrue("Card 3 should contain special sentence", items.get(2).fullContent.contains("The prayer has begun"));
    }
}
