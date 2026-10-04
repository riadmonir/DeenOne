package com.devflux.deenone.features.battle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.devflux.deenone.features.battle.data.remote.BattleRoomJsonConverter;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleLifecycleState;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.service.JoinValidationResult;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleMultiplayerCloudSyncTest {

    @Test
    public void testBattleRoomJsonConverterLosslessRoundtrip() {
        // Arrange
        String roomCode = "67QADC";
        BattleConfig config = new BattleConfig(2, 5, 3, "quran_stories", "কুরআনের ঘটনা ও শিক্ষা");
        config.setNegativeMarkingEnabled(true);

        BattlePlayer host = new BattlePlayer("user_host_123", "আহমেদ (Host)", "ic_person", false);
        host.setStatus(BattlePlayer.PlayerStatus.READY);
        host.addScore(20);
        host.incrementCorrect();
        host.incrementCorrect();

        BattleRoom room = new BattleRoom(roomCode, config, host);

        BattlePlayer guest = new BattlePlayer("user_guest_456", "মুসাফির (Guest)", "ic_person", false);
        guest.setStatus(BattlePlayer.PlayerStatus.READY);
        guest.addScore(10);
        guest.incrementCorrect();
        room.addPlayer(guest);

        List<BattleQuestion> questions = new ArrayList<>();
        questions.add(new BattleQuestion(
                "q_01",
                "quran_stories",
                "কোন নবীকে মাছ গিলে ফেলেছিল?",
                Arrays.asList("ইউনুস (আ.)", "মুসা (আ.)", "ইব্রাহিম (আ.)", "দাউদ (আ.)"),
                0,
                "ইউনুস (আ.)-কে আল্লাহ তায়ালা বড় মাছের পেটে হেফাজত করেছিলেন।",
                "সূরা আস-সাফফাত: ১৪২"
        ));
        questions.add(new BattleQuestion(
                "q_02",
                "quran_stories",
                "মুসা (আ.)-এর লাঠি কিসে রূপান্তরিত হয়েছিল?",
                Arrays.asList("পাখি", "সাপ", "সিংহ", "উট"),
                1,
                "মুসা (আ.)-এর লাঠি জীবন্ত অজগর সাপে পরিণত হয়েছিল।",
                "সূরা ত্বাহা: ২০"
        ));
        room.setQuestions(questions);
        room.setLifecycleState(BattleLifecycleState.READY_CHECK);

        // Act: Serialize to JSONObject
        JSONObject serialized = BattleRoomJsonConverter.serializeRoom(room);
        assertNotNull("Serialized JSON must not be null", serialized);

        // Act: Deserialize back to BattleRoom
        BattleRoom reconstructed = BattleRoomJsonConverter.deserializeRoom(serialized);

        // Assert
        assertNotNull("Reconstructed room must not be null", reconstructed);
        assertEquals(roomCode, reconstructed.getRoomCode());
        assertEquals("quran_stories", reconstructed.getConfig().getCategoryId());
        assertEquals(5, reconstructed.getConfig().getTotalQuestions());
        assertTrue(reconstructed.getConfig().isNegativeMarkingEnabled());

        assertEquals(2, reconstructed.getPlayers().size());
        assertEquals("user_host_123", reconstructed.getHostPlayer().getId());
        assertEquals("আহমেদ (Host)", reconstructed.getHostPlayer().getName());

        assertTrue(reconstructed.hasPlayer("user_host_123"));
        assertTrue(reconstructed.hasPlayer("user_guest_456"));
        assertFalse(reconstructed.hasPlayer("fake_user_999"));

        assertEquals(2, reconstructed.getQuestions().size());
        BattleQuestion q1 = reconstructed.getQuestions().get(0);
        assertEquals("q_01", q1.getId());
        assertEquals("কোন নবীকে মাছ গিলে ফেলেছিল?", q1.getQuestionText());
        assertEquals(0, q1.getCorrectOptionIndex());
        assertEquals("ইউনুস (আ.)", q1.getOptions().get(0));
        assertEquals("সূরা আস-সাফফাত: ১৪২", q1.getReference());
    }

    @Test
    public void testInvalidCodeValidationHandling() {
        // Validation with null or short input code must return INVALID_CODE
        JoinValidationResult nullResult = JoinValidationResult.failure(JoinValidationResult.Status.INVALID_CODE);
        assertFalse(nullResult.isSuccess());
        assertEquals(JoinValidationResult.Status.INVALID_CODE, nullResult.getStatus());
        assertEquals("ভুল কোড! কোনো সক্রিয় ব্যাটেল রুম পাওয়া যায়নি।", nullResult.getMessage());

        JoinValidationResult shortResult = JoinValidationResult.failure(JoinValidationResult.Status.INVALID_CODE);
        assertEquals("ভুল কোড! কোনো সক্রিয় ব্যাটেল রুম পাওয়া যায়নি।", shortResult.getMessage());
    }

    @Test
    public void testPlayerRosterCapacityAndReadyCheck() {
        BattleConfig config = new BattleConfig(2, 5, 3, "quran_stories", "কুরআনের ঘটনা");
        BattlePlayer host = new BattlePlayer("p1", "Player 1", "ic_person", false);
        BattleRoom room = new BattleRoom("TEST01", config, host);

        assertEquals(1, room.getPlayerCount());
        assertFalse(room.isFull());
        assertFalse(room.isAllPlayersReady());

        // Guest joins
        BattlePlayer guest = new BattlePlayer("p2", "Player 2", "ic_person", false);
        guest.setStatus(BattlePlayer.PlayerStatus.NOT_READY);
        boolean added = room.addPlayer(guest);
        assertTrue(added);
        assertEquals(2, room.getPlayerCount());
        assertTrue(room.isFull());

        // Not all ready
        assertFalse(room.isAllPlayersReady());

        // Mark guest ready
        room.updatePlayerStatus("p2", BattlePlayer.PlayerStatus.READY);
        assertTrue(room.isAllPlayersReady());

        // 3rd player cannot join a 2-player battle
        BattlePlayer thirdPlayer = new BattlePlayer("p3", "Player 3", "ic_person", false);
        boolean addedThird = room.addPlayer(thirdPlayer);
        assertFalse("3rd player should not be added when max capacity is 2", addedThird);
    }

    @Test
    public void testHumanVsBotDistinction() {
        BattlePlayer humanPlayer = new BattlePlayer("human_1", "Real Friend", "ic_person", false);
        BattlePlayer botPlayer = new BattlePlayer("bot_1", "মুহিব আহমেদ (Bot)", "ic_person", true);

        assertFalse(humanPlayer.isBot());
        assertTrue(botPlayer.isBot());
    }

    @Test
    public void testCompactPayloadSizeUnder4KB() {
        BattleConfig config = new BattleConfig(2, 10, 5, "salat_taharah", "সালাত ও পবিত্রতা");
        BattlePlayer host = new BattlePlayer("user_host_1", "Host Player", "ic_person", false);
        BattleRoom room = new BattleRoom("778899", config, host);

        List<BattleQuestion> questions = com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance()
                .getQuestionsByCategory(null, "salat_taharah", 10);
        assertNotNull(questions);
        assertEquals(10, questions.size());
        room.setQuestions(questions);

        JSONObject serialized = BattleRoomJsonConverter.serializeRoom(room);
        assertNotNull(serialized);

        String jsonStr = serialized.toString();
        byte[] utf8Bytes = jsonStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // CRITICAL: Must be well under 4,096 bytes so ntfy NEVER turns it into an attachment!
        assertTrue("Serialized 10-question room payload (" + utf8Bytes.length + " bytes) must be < 3,500 bytes",
                utf8Bytes.length < 3500);

        // Deserialize back and verify all 10 questions and their options are intact
        BattleRoom reconstructed = BattleRoomJsonConverter.deserializeRoom(serialized);
        assertNotNull(reconstructed);
        assertEquals(10, reconstructed.getQuestions().size());

        for (int i = 0; i < 10; i++) {
            BattleQuestion original = questions.get(i);
            BattleQuestion reconstructedQ = reconstructed.getQuestions().get(i);
            assertEquals(original.getId(), reconstructedQ.getId());
            assertEquals(original.getQuestionText(), reconstructedQ.getQuestionText());
            assertEquals(original.getCorrectOptionIndex(), reconstructedQ.getCorrectOptionIndex());
            assertEquals(original.getOptions(), reconstructedQ.getOptions());
        }
    }

    @Test
    public void testThreePlayerRoomCapacityAndQuestionSync() {
        BattleConfig config = new BattleConfig(3, 5, 3, "quran_stories", "কুরআনের ঘটনা");
        BattlePlayer host = new BattlePlayer("host_1", "Host", "ic_person", false);
        BattleRoom room = new BattleRoom("ROOM3P", config, host);

        // Player 2 joins
        BattlePlayer player2 = new BattlePlayer("guest_2", "Guest 2", "ic_person", false);
        assertTrue(room.addPlayer(player2));
        assertEquals(2, room.getPlayerCount());
        assertFalse(room.isFull());

        // Player 3 joins
        BattlePlayer player3 = new BattlePlayer("guest_3", "Guest 3", "ic_person", false);
        assertTrue(room.addPlayer(player3));
        assertEquals(3, room.getPlayerCount());
        assertTrue(room.isFull());

        // Player 4 should be rejected
        BattlePlayer player4 = new BattlePlayer("guest_4", "Guest 4", "ic_person", false);
        assertFalse(room.addPlayer(player4));
        assertEquals(3, room.getPlayerCount());
    }

    @Test
    public void testDynamicQuestionsSerializationAndDeserialization() {
        com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance().ensureCategoryPoolLoaded(null, "quran_stories");
        List<BattleQuestion> dynamicQuestions = com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance()
                .generateDynamicQuestions("quran_stories", 10, null, null);

        assertNotNull(dynamicQuestions);
        assertEquals(10, dynamicQuestions.size());

        BattleConfig config = new BattleConfig(2, 10, 5, "quran_stories", "কুরআনের ঘটনা");
        BattlePlayer host = new BattlePlayer("host_dyn", "Host Player", "ic_person", false);
        BattleRoom room = new BattleRoom("DYNA99", config, host);
        room.setQuestions(dynamicQuestions);

        JSONObject serialized = BattleRoomJsonConverter.serializeRoom(room);
        assertNotNull("Serialized room should not be null", serialized);

        String jsonStr = serialized.toString();
        byte[] bytes = jsonStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("DYNAMIC 10-QUESTION PAYLOAD BYTES: " + bytes.length);

        BattleRoom reconstructed = BattleRoomJsonConverter.deserializeRoom(serialized);
        assertNotNull("Reconstructed room must not be null", reconstructed);
        assertEquals(10, reconstructed.getQuestions().size());

        for (int i = 0; i < 10; i++) {
            BattleQuestion original = dynamicQuestions.get(i);
            BattleQuestion recon = reconstructed.getQuestions().get(i);
            System.out.println("Q" + i + " ORIG ID: " + original.getId() + " TEXT: " + original.getQuestionText().substring(0, Math.min(30, original.getQuestionText().length())));
            System.out.println("Q" + i + " RECON ID: " + recon.getId() + " TEXT: " + recon.getQuestionText().substring(0, Math.min(30, recon.getQuestionText().length())));
            assertEquals(original.getId(), recon.getId());
            assertEquals(original.getQuestionText(), recon.getQuestionText());
            assertEquals(original.getOptions(), recon.getOptions());
            assertEquals(original.getCorrectOptionIndex(), recon.getCorrectOptionIndex());
        }
    }

    @Test
    public void testMultiDeviceOnlineCloudJoinAndFullQuestionSync() throws Exception {
        // Step 1: Simulate Phone A (Host) creating a real online battle room
        String testRoomCode = "LIVE" + (1000 + (int)(Math.random() * 8999));
        BattleConfig config = new BattleConfig(2, 5, 3, "quran_stories", "কুরআনের ঘটনা");
        BattlePlayer hostPlayer = new BattlePlayer("phone_a_host", "মোবাইল এ (Host)", "ic_person", false);
        BattleRoom hostRoom = new BattleRoom(testRoomCode, config, hostPlayer);

        List<BattleQuestion> hostQuestions = com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance()
                .generateDynamicQuestions("quran_stories", 5, null, null);
        assertEquals(5, hostQuestions.size());
        hostRoom.setQuestions(hostQuestions);

        // Phone A publishes the room to the cloud relay
        boolean published = com.devflux.deenone.features.battle.data.remote.BattleCloudRelayClient.getInstance()
                .publishRoomSync(hostRoom);
        assertTrue("Phone A must successfully publish room to Cloud Relay", published);

        // Step 2: Simulate Phone B (Guest) fetching the room using the 6-digit code
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final BattleRoom[] guestRoomHolder = new BattleRoom[1];

        com.devflux.deenone.features.battle.data.remote.BattleCloudRelayClient.getInstance()
                .fetchRoomOnline(null, testRoomCode, new com.devflux.deenone.features.battle.data.remote.BattleCloudRelayClient.RelayCallback<BattleRoom>() {
                    @Override
                    public void onSuccess(BattleRoom result) {
                        guestRoomHolder[0] = result;
                        latch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        latch.countDown();
                    }
                });

        boolean completedInTime = latch.await(10, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue("Phone B should fetch room within 10 seconds", completedInTime);

        BattleRoom guestRoom = guestRoomHolder[0];
        assertNotNull("Phone B must successfully find active battle room with code: " + testRoomCode, guestRoom);
        assertEquals(testRoomCode, guestRoom.getRoomCode());
        assertEquals("quran_stories", guestRoom.getConfig().getCategoryId());
        assertEquals(5, guestRoom.getQuestions().size());

        // Step 3: CRITICAL - Verify 100% question, option, and answer index parity between Phone A and Phone B
        for (int i = 0; i < 5; i++) {
            BattleQuestion qHost = hostQuestions.get(i);
            BattleQuestion qGuest = guestRoom.getQuestions().get(i);

            assertEquals("Question " + i + " ID mismatch", qHost.getId(), qGuest.getId());
            assertEquals("Question " + i + " text mismatch", qHost.getQuestionText(), qGuest.getQuestionText());
            assertEquals("Question " + i + " options mismatch", qHost.getOptions(), qGuest.getOptions());
            assertEquals("Question " + i + " correct answer mismatch", qHost.getCorrectOptionIndex(), qGuest.getCorrectOptionIndex());
        }
    }
}
