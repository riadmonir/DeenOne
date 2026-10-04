package com.devflux.deenone.features.battle;

import com.devflux.deenone.core.ai.IslamicQuestionBank;
import com.devflux.deenone.core.ai.IslamicQuizScraperEngine;
import com.devflux.deenone.features.battle.model.BattleCategory;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Production Unit Test Suite for Knowledge Battle:
 * 1. Option Randomization & Accurate Correct Index mapping.
 * 2. Zero Duplicates in Match Question Serving.
 * 3. 100% Unique Question IDs.
 * 4. Full Coverage of all 29 Official Categories.
 * 5. Strict Room-Level Deduplication.
 */
public class KnowledgeBattleRandomizationTest {

    private IslamicQuizScraperEngine scraperEngine;

    @Before
    public void setUp() {
        scraperEngine = IslamicQuizScraperEngine.getInstance();
    }

    @Test
    public void testOptionShufflingAndCorrectIndexIntegrity() {
        String originalCorrectText = "তুর পাহাড়";
        BattleQuestion base = new BattleQuestion(
                "TEST_Q_1",
                "quran_stories",
                "হযরত মূসা (আ.)-কে আল্লাহ তা'আলা কোন পাহাড়ে সরাসরি বাণী দিয়েছিলেন?",
                Arrays.asList(originalCorrectText, "হেরা পাহাড়", "সওর পাহাড়", "জুদী পাহাড়"),
                0,
                "ব্যাখ্যা",
                "সূরা ত্বা-হা: ১২-১৪",
                BattleConfig.Difficulty.MEDIUM,
                "verified",
                "bn",
                System.currentTimeMillis(),
                BattleQuestion.QuestionStatus.ACTIVE
        );

        Set<Integer> observedIndices = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            BattleQuestion randomized = base.createRandomizedInstance("Q_TEST_" + i);

            // 1. Options size must still be 4
            assertEquals(4, randomized.getOptions().size());

            // 2. Correct index must be within [0, 3]
            int correctIndex = randomized.getCorrectOptionIndex();
            assertTrue(correctIndex >= 0 && correctIndex < 4);

            // 3. CRITICAL: The option at correctIndex MUST be the original correct answer!
            String actualAnswerAtCorrectIndex = randomized.getOptions().get(correctIndex);
            assertEquals("Option at correctIndex must match original correct answer string",
                    originalCorrectText, actualAnswerAtCorrectIndex);

            observedIndices.add(correctIndex);
        }

        // 4. Over 100 trials, the correct option MUST NOT always be 0 (must be randomized across 0, 1, 2, 3)
        assertTrue("Correct answer must appear at multiple randomized positions", observedIndices.size() > 1);
    }

    @Test
    public void testZeroDuplicateQuestionsInGeneratedMatch() {
        List<BattleCategory> categories = KnowledgeBattleRepository.getAllCategories();
        assertEquals(29, categories.size());

        for (BattleCategory cat : categories) {
            String catId = cat.getId();

            // Request 5 questions for a match
            List<BattleQuestion> matchQuestions = scraperEngine.generateDynamicQuestions(catId, 5);
            assertTrue("Expected questions for category: " + catId, matchQuestions.size() > 0);

            Set<String> seenTexts = new HashSet<>();
            for (BattleQuestion q : matchQuestions) {
                String norm = q.getNormalizedQuestionText();
                assertFalse("Duplicate question text found in match for category " + catId + ": " + q.getQuestionText(),
                        seenTexts.contains(norm));
                seenTexts.add(norm);
            }
        }
    }

    @Test
    public void testUniqueQuestionIdsGenerated() {
        List<BattleQuestion> qList1 = scraperEngine.generateDynamicQuestions("salat_taharah", 5);
        List<BattleQuestion> qList2 = scraperEngine.generateDynamicQuestions("salat_taharah", 5);

        Set<String> allIds = new HashSet<>();

        for (BattleQuestion q : qList1) {
            assertNotNull(q.getId());
            assertTrue(q.getId().startsWith("KB_Q_"));
            assertFalse("Question ID must be globally unique", allIds.contains(q.getId()));
            allIds.add(q.getId());
        }

        for (BattleQuestion q : qList2) {
            assertNotNull(q.getId());
            assertTrue(q.getId().startsWith("KB_Q_"));
            assertFalse("Question ID must be globally unique across matches", allIds.contains(q.getId()));
            allIds.add(q.getId());
        }

        assertEquals(10, allIds.size());
    }

    @Test
    public void testAll29CategoriesCoveredInQuestionBank() {
        Map<String, List<BattleQuestion>> pool = IslamicQuestionBank.buildCompleteQuestionPool();
        assertEquals(29, pool.size());

        for (Map.Entry<String, List<BattleQuestion>> entry : pool.entrySet()) {
            String catId = entry.getKey();
            List<BattleQuestion> questions = entry.getValue();

            assertTrue("Category " + catId + " must have at least 8 questions", questions.size() >= 8);

            for (BattleQuestion q : questions) {
                assertNotNull("Question text must not be null", q.getQuestionText());
                assertFalse("Question text must not be empty", q.getQuestionText().trim().isEmpty());
                assertEquals("Question must have exactly 4 options", 4, q.getOptions().size());
                assertNotNull("Explanation must not be null", q.getExplanation());
                assertNotNull("Reference must not be null", q.getReference());
                assertFalse("Reference must not be empty", q.getReference().trim().isEmpty());
            }
        }
    }

    @Test
    public void testBattleRoomStrictDeduplication() {
        BattleConfig config = new BattleConfig(2, 5, 2, "salat_taharah", "সালাত ও পবিত্রতা");
        BattlePlayer host = new BattlePlayer("player_1", "User 1", "avatar_1", false);
        BattleRoom room = new BattleRoom("ROOM_123", config, host);

        List<BattleQuestion> listWithDuplicates = new ArrayList<>();
        BattleQuestion q1 = new BattleQuestion("ID_1", "salat_taharah", "প্রতিদিন পাঁচ ওয়াক্ত ফরজ নামাজের মোট রাকাত সংখ্যা কত?",
                Arrays.asList("১৭ রাকাত", "২০ রাকাত", "১৫ রাকাত", "৩২ রাকাত"), 0, "ব্যাখ্যা", "সহীহ বুখারী");
        BattleQuestion q1Dup = new BattleQuestion("ID_2", "salat_taharah", "প্রতিদিন পাঁচ ওয়াক্ত ফরজ নামাজের মোট রাকাত সংখ্যা কত?",
                Arrays.asList("১৭ রাকাত", "২০ রাকাত", "১৫ রাকাত", "৩২ রাকাত"), 0, "ব্যাখ্যা", "সহীহ বুখারী");
        BattleQuestion q2 = new BattleQuestion("ID_3", "salat_taharah", "ওজুর মধ্যে কয়টি কাজ ফরজ?",
                Arrays.asList("৪টি কাজ", "৩টি কাজ", "৫টি কাজ", "৭টি কাজ"), 0, "ব্যাখ্যা", "সূরা আল-মায়িদাহ: ৬");

        listWithDuplicates.add(q1);
        listWithDuplicates.add(q1Dup);
        listWithDuplicates.add(q2);

        room.setQuestions(listWithDuplicates);

        // Deduplication in setQuestions must eliminate q1Dup
        assertEquals(2, room.getQuestions().size());
        assertEquals("ID_1", room.getQuestions().get(0).getId());
        assertEquals("ID_3", room.getQuestions().get(1).getId());
    }

    @Test
    public void testMassiveCategoryPoolsContain100PlusQuestions() {
        List<BattleCategory> categories = KnowledgeBattleRepository.getAllCategories();
        assertEquals(29, categories.size());

        for (BattleCategory cat : categories) {
            String catId = cat.getId();
            scraperEngine.ensureCategoryPoolLoaded(null, catId);
            int size = scraperEngine.getCategoryPoolSize(catId);
            assertTrue("Category " + catId + " must contain at least 100 authentic questions! Found: " + size,
                    size >= 100);

            // Test request for 25 questions in a match room
            List<BattleQuestion> match25 = scraperEngine.generateDynamicQuestions(catId, 25);
            assertEquals("Expected 25 non-repeating questions for category: " + catId, 25, match25.size());

            // Guarantee zero duplicates
            Set<String> signatures = new HashSet<>();
            for (BattleQuestion q : match25) {
                assertFalse("Found duplicate question in 25-round match for " + catId + ": " + q.getQuestionText(),
                        signatures.contains(q.getNormalizedQuestionText()));
                signatures.add(q.getNormalizedQuestionText());
            }
        }
    }

    @Test
    public void testZeroEmojisInAnyMassiveQuestionAsset() {
        List<BattleCategory> categories = KnowledgeBattleRepository.getAllCategories();
        for (BattleCategory cat : categories) {
            scraperEngine.ensureCategoryPoolLoaded(null, cat.getId());
            List<BattleQuestion> questions = scraperEngine.generateDynamicQuestions(cat.getId(), 100);
            for (BattleQuestion q : questions) {
                assertNoEmoji(q.getQuestionText(), "Question text");
                for (String opt : q.getOptions()) {
                    assertNoEmoji(opt, "Option text");
                }
                assertNoEmoji(q.getExplanation(), "Explanation text");
                assertNoEmoji(q.getReference(), "Reference text");
            }
        }
    }

    private void assertNoEmoji(String text, String fieldName) {
        if (text == null) return;
        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            // Check for common emoji ranges
            boolean isEmoji = (codePoint >= 0x1F600 && codePoint <= 0x1F64F) || // Emoticons
                              (codePoint >= 0x1F300 && codePoint <= 0x1F5FF) || // Misc Symbols and Pictographs
                              (codePoint >= 0x1F680 && codePoint <= 0x1F6FF) || // Transport and Map
                              (codePoint >= 0x2600 && codePoint <= 0x26FF) ||   // Misc symbols
                              (codePoint >= 0x2700 && codePoint <= 0x27BF) ||   // Dingbats
                              (codePoint >= 0x1F900 && codePoint <= 0x1F9FF);   // Supplemental Symbols and Pictographs
            assertFalse(fieldName + " must contain zero emojis: " + text, isEmoji);
        }
    }
}
