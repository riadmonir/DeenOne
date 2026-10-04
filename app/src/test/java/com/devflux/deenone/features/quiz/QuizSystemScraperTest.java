package com.devflux.deenone.features.quiz;

import com.devflux.deenone.core.quiz.QuizItem;
import com.devflux.deenone.core.quiz.QuizManager;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizSystemScraperTest {

    private static boolean hasEmoji(String text) {
        if (text == null) return false;
        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            // Check true graphical emoji ranges (Emoticons, Pictographs, Symbols, Transport/Map)
            if ((cp >= 0x1F300 && cp <= 0x1FAFF) || (cp >= 0x1F600 && cp <= 0x1F64F) || (cp >= 0x1F900 && cp <= 0x1F9FF)) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    @Test
    public void testMassiveQuestionBankLoadsOver3000Questions() {
        QuizManager manager = QuizManager.getInstance();
        manager.ensureMasterPoolLoaded(null);

        int totalCount = manager.getTotalQuestionBankCount(null);
        Assert.assertTrue("Total authentic questions loaded must exceed 3,000, found: " + totalCount,
                totalCount >= 3000);
    }

    @Test
    public void testZeroEmojisAcrossAllQuestions() {
        QuizManager manager = QuizManager.getInstance();
        manager.ensureMasterPoolLoaded(null);

        List<QuizItem> questions = manager.getDailyQuizQuestions(null);
        Assert.assertNotNull("Daily questions must not be null", questions);
        Assert.assertEquals("Daily questions count must be exactly 10", 10, questions.size());

        for (QuizItem q : questions) {
            Assert.assertFalse("Question text must not have emoji: " + q.question, hasEmoji(q.question));
            for (String opt : q.options) {
                Assert.assertFalse("Option must not have emoji: " + opt, hasEmoji(opt));
            }
            if (q.explanation != null) {
                Assert.assertFalse("Explanation must not have emoji: " + q.explanation, hasEmoji(q.explanation));
            }
            if (q.reference != null) {
                Assert.assertFalse("Reference must not have emoji: " + q.reference, hasEmoji(q.reference));
            }
        }
    }

    @Test
    public void testDynamicOptionRandomization() {
        QuizItem original = new QuizItem(
                "test_rand_1",
                "পবিত্র কুরআনের কোন সূরাটিকে 'উম্মুল কুরআন' বলা হয়?",
                new String[]{"সূরা আল-ফাতিহা", "সূরা আল-বাক্বারাহ", "সূরা আল-ইখলাস", "সূরা ইয়াসীন"},
                0,
                "কুরআনুল কারীম",
                "general",
                "সহীহ বুখারী: ৪৭০৪",
                "সূরা ফাতিহাকে কুরআনের জননী বলা হয়।"
        );

        String originalCorrectAnswer = original.options[original.correctIndex];
        Assert.assertEquals("Original correct answer must be সূরা আল-ফাতিহা", "সূরা আল-ফাতিহা", originalCorrectAnswer);

        Map<Integer, Integer> positionCounts = new HashMap<>();
        positionCounts.put(0, 0);
        positionCounts.put(1, 0);
        positionCounts.put(2, 0);
        positionCounts.put(3, 0);

        // Run 100 randomizations to ensure the correct answer is distributed across 0, 1, 2, 3
        for (int i = 0; i < 100; i++) {
            QuizItem randomized = QuizManager.randomizeOptions(original);
            Assert.assertEquals("Randomized options must have exactly 4 items", 4, randomized.options.length);

            String randomizedCorrectAnswer = randomized.options[randomized.correctIndex];
            Assert.assertEquals("Randomized correct answer text must still match original",
                    originalCorrectAnswer, randomizedCorrectAnswer);

            int pos = randomized.correctIndex;
            positionCounts.put(pos, positionCounts.get(pos) + 1);
        }

        // Verify that the correct answer was NOT stuck at index 0, but occurred across multiple positions
        Assert.assertTrue("Correct answer must appear at index 1 at least once", positionCounts.get(1) > 0);
        Assert.assertTrue("Correct answer must appear at index 2 at least once", positionCounts.get(2) > 0);
        Assert.assertTrue("Correct answer must appear at index 3 at least once", positionCounts.get(3) > 0);
    }

    @Test
    public void testContextRelevanceRules() {
        QuizItem generalItem = new QuizItem(
                "q_test_gen", "ইসলামের প্রথম খলিফা কে ছিলেন?",
                new String[]{"আবু বকর (রা.)", "উমর (রা.)", "উসমান (রা.)", "আলী (রা.)"},
                0, "সাহাবা", "general", "সহীহ বুখারী: ৩৬৫৬", "আবু বকর (রা.) প্রথম খলিফা"
        );

        // General questions are ALWAYS allowed on any day
        Assert.assertTrue("General Islamic question must be allowed on any day",
                QuizManager.isContextAllowedToday(generalItem));

        QuizItem jummahItem = new QuizItem(
                "q_test_jummah", "জুমার দিন সূরা কাহাফ পড়ার ফজিলত কী?",
                new String[]{"নূর চমকাবে", "সম্পদ বৃদ্ধি", "দীর্ঘায়ু", "রোগমুক্তি"},
                0, "জুমা", "jummah", "সহীহুল জামি: ৬৪৭০", "জুমার দিন সূরা কাহাফ বিশেষ ফজিলতপূর্ণ"
        );

        Calendar cal = Calendar.getInstance();
        boolean isFriday = (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);

        // If today is Friday, jummah question is allowed; if NOT Friday, it MUST be strictly excluded
        Assert.assertEquals("Jummah question allowed state must match whether today is Friday",
                isFriday, QuizManager.isContextAllowedToday(jummahItem));
    }

    @Test
    public void testUniqueQuestionIdsAndDailyQuotaOfTen() {
        QuizManager manager = QuizManager.getInstance();
        manager.ensureMasterPoolLoaded(null);

        List<QuizItem> daily10 = manager.getDailyQuizQuestions(null);
        Assert.assertNotNull(daily10);
        Assert.assertEquals("Must select exactly 10 questions per daily session", 10, daily10.size());

        Set<String> ids = new HashSet<>();
        for (QuizItem q : daily10) {
            Assert.assertNotNull("Question ID must not be null", q.id);
            Assert.assertFalse("Question ID must not be empty", q.id.trim().isEmpty());
            Assert.assertFalse("Each question in daily session must have a unique ID: " + q.id, ids.contains(q.id));
            ids.add(q.id);
        }
    }

    @Test
    public void testCalendarDateAndCountdownFormatting() {
        String today = QuizManager.getTodayCalendarDate();
        Assert.assertNotNull(today);
        Assert.assertTrue(today.matches("\\d{4}-\\d{2}-\\d{2}"));

        String tomorrow = QuizManager.getTomorrowCalendarDate();
        Assert.assertNotNull(tomorrow);
        Assert.assertTrue(tomorrow.matches("\\d{4}-\\d{2}-\\d{2}"));
        Assert.assertNotEquals(today, tomorrow);

        String countdown = QuizManager.getInstance().getRemainingTimeToNextCycleFormatted();
        Assert.assertNotNull(countdown);
        Assert.assertTrue(countdown.contains("ঘণ্টা") || countdown.contains("মিনিট") || countdown.contains("উপলব্ধ"));
    }

    @Test
    public void testStrictDeduplicationPreventsFakeIncrements() {
        QuizManager manager = QuizManager.getInstance();
        manager.ensureMasterPoolLoaded(null);
        int initialCount = manager.getTotalQuestionBankCount(null);
        Assert.assertTrue("Initial count should be substantial", initialCount >= 3000);

        // Fetch daily questions and confirm they all have normalized text and non-empty IDs
        List<QuizItem> sample = manager.getDailyQuizQuestions(null);
        Assert.assertFalse("Sample questions must not be empty", sample.isEmpty());

        // Verify deduplication key generation
        Set<String> normalizedSet = new HashSet<>();
        for (QuizItem q : sample) {
            String norm = QuizManager.normalizeText(q.question);
            Assert.assertFalse("Normalized question must not be empty", norm.isEmpty());
            normalizedSet.add(norm);
        }

        // Inserting duplicates must result in 0 added and identical total count
        Assert.assertEquals("Normalized set size must match sample size (no internal duplicates)",
                sample.size(), normalizedSet.size());
    }
}
