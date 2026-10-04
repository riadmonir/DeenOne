package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanMeaningTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanMeaningPageDialog.StepItem> items = SalahAzanMeaningPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 3 items for Azan Meaning in Bengali", 3, items.size());

        // Card 1: Verbatim Sentences
        assertEquals("আজান এর অর্থ", items.get(0).title);
        assertTrue("Card 1 should contain Allah Almighty", items.get(0).fullContent.contains("আল্লাহ সর্বশক্তিমান"));
        assertTrue("Card 1 should contain Tawheed testimony", items.get(0).fullContent.contains("আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই"));
        assertTrue("Card 1 should contain Messenger testimony", items.get(0).fullContent.contains("আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত"));
        assertTrue("Card 1 should contain Come to Prayer", items.get(0).fullContent.contains("নামাজের জন্য এসো"));
        assertTrue("Card 1 should contain Come to Success", items.get(0).fullContent.contains("সাফল্যের জন্য এসো"));
        assertTrue("Card 1 should contain Allah is Great", items.get(0).fullContent.contains("আল্লাহ্ মহান"));
        assertTrue("Card 1 should contain No deity except Allah", items.get(0).fullContent.contains("আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই"));
        assertTrue("Card 1 should contain Fajr special sentence", items.get(0).fullContent.contains("আছছালা-তু খায়রুম মিনান নাঊম” - “ঘুম হতে নামাজ উত্তম”( শুধুমাত্র ফজর নামাজের সময়)"));

        // Card 2: Sentences breakdown
        assertEquals("আজানের বাক্যাবলীর বাংলা অর্থ", items.get(1).title);
        assertTrue("Card 2 should contain breakdown", items.get(1).fullContent.contains("আল্লাহু আকবার: আল্লাহ সর্বশক্তিমান"));

        // Card 3: Fajr Special Sentence
        assertEquals("ফজর আজানের বিশেষ বাক্য", items.get(2).title);
        assertTrue("Card 3 should contain Fajr sentence", items.get(2).fullContent.contains("আছছালা-তু খায়রুম মিনান নাঊম” - “ঘুম হতে নামাজ উত্তম”( শুধুমাত্র ফজর নামাজের সময়)"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanMeaningPageDialog.StepItem> items = SalahAzanMeaningPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 3 items for Azan Meaning in English", 3, items.size());

        // Card 1: English Sentences
        assertEquals("Meaning of Adhan", items.get(0).title);
        assertTrue("Card 1 should contain Allah is Almighty", items.get(0).fullContent.contains("Allah is Almighty"));
        assertTrue("Card 1 should contain Tawheed", items.get(0).fullContent.contains("I testify that there is no god but Allah"));
        assertTrue("Card 1 should contain Messenger", items.get(0).fullContent.contains("I testify that Muhammad is the Messenger sent by Allah"));
        assertTrue("Card 1 should contain Come to prayer", items.get(0).fullContent.contains("Come to prayer"));
        assertTrue("Card 1 should contain Come to success", items.get(0).fullContent.contains("Come to success"));
        assertTrue("Card 1 should contain Allah is the Greatest", items.get(0).fullContent.contains("Allah is the Greatest"));
        assertTrue("Card 1 should contain There is no deity except Allah", items.get(0).fullContent.contains("There is no deity except Allah"));
        assertTrue("Card 1 should contain Fajr special sentence", items.get(0).fullContent.contains("Prayer is better than sleep"));

        // Card 2: Breakdown
        assertEquals("Sentences and Meaning of Adhan", items.get(1).title);
        assertTrue("Card 2 should contain breakdown", items.get(1).fullContent.contains("Allahu Akbar: Allah is Almighty"));

        // Card 3: Fajr Special Sentence
        assertEquals("Special Sentence for Fajr Adhan", items.get(2).title);
        assertTrue("Card 3 should contain Fajr sentence", items.get(2).fullContent.contains("Prayer is better than sleep"));
    }
}
