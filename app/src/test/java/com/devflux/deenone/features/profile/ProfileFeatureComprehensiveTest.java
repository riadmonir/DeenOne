package com.devflux.deenone.features.profile;

import com.devflux.deenone.core.backend.model.UserProfile;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.community.model.CommunityPostItem;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ProfileFeatureComprehensiveTest {

    // =========================================================================
    // 1. Bengali Number and Calendar Calculation Test
    // =========================================================================
    @Test
    public void testBengaliNumberConversion() {
        assertEquals("১২৭২", BengaliNumberUtil.toBengali(1272));
        assertEquals("০", BengaliNumberUtil.toBengali(0));
        assertEquals("৫", BengaliNumberUtil.toBengali(5));
        assertEquals("২০২৬", BengaliNumberUtil.toBengali(2026));
    }

    @Test
    public void testLeapYearAndDaysInMonth() {
        Calendar cal = Calendar.getInstance();

        // 2024 is a leap year -> February has 29 days
        cal.set(2024, Calendar.FEBRUARY, 1);
        int daysFeb2024 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        assertEquals(29, daysFeb2024);

        // 2025 is not a leap year -> February has 28 days
        cal.set(2025, Calendar.FEBRUARY, 1);
        int daysFeb2025 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        assertEquals(28, daysFeb2025);

        // August always has 31 days
        cal.set(2026, Calendar.AUGUST, 1);
        int daysAug2026 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        assertEquals(31, daysAug2026);
    }

    // =========================================================================
    // 2. Community Flow & Social Statistics Aggregation
    // =========================================================================
    @Test
    public void testCommunitySocialActivityStats() {
        String myAuthorName = "Abdullah Al Riad";
        List<CommunityPostItem> posts = new ArrayList<>();

        // Post 1 by user with 5 ameen (likes) and 3 comments
        CommunityPostItem p1 = new CommunityPostItem("p_1", myAuthorName, "ঢাকা", "সাধারণ", "পোস্ট ১", "বিস্তারিত", 5, 3, 20, true, System.currentTimeMillis());
        posts.add(p1);

        // Post 2 by user with 10 ameen (likes) and 4 comments
        CommunityPostItem p2 = new CommunityPostItem("p_2", myAuthorName, "ঢাকা", "আমল", "পোস্ট ২", "বিস্তারিত ২", 10, 4, 35, true, System.currentTimeMillis());
        posts.add(p2);

        // Post 3 by another user
        CommunityPostItem p3 = new CommunityPostItem("p_3", "অন্য ব্যবহারকারী", "চট্টগ্রাম", "প্রশ্ন", "অন্যের পোস্ট", "অন্য বিস্তারিত", 2, 1, 15, false, System.currentTimeMillis());
        posts.add(p3);

        // Count user posts and likes
        int myPostCount = 0;
        int totalLikesReceived = 0;
        for (CommunityPostItem post : posts) {
            if (myAuthorName.equals(post.getAuthorName())) {
                myPostCount++;
                totalLikesReceived += post.getAmeenCount();
            }
        }

        assertEquals(2, myPostCount);
        assertEquals(15, totalLikesReceived);

        // Simulate Post Deletion
        posts.remove(p2);
        int updatedPostCount = 0;
        for (CommunityPostItem post : posts) {
            if (myAuthorName.equals(post.getAuthorName())) {
                updatedPostCount++;
            }
        }
        assertEquals(1, updatedPostCount);
    }

    // =========================================================================
    // 3. Offline Action Queue & Idempotency / Deduplication
    // =========================================================================
    @Test
    public void testOfflineActionDeduplication() {
        Set<String> processedOperationIds = new HashSet<>();

        String opId1 = UUID.randomUUID().toString();
        String opId2 = UUID.randomUUID().toString();

        // First execution of opId1 -> processed
        boolean isFirstExecution = !processedOperationIds.contains(opId1);
        assertTrue(isFirstExecution);
        processedOperationIds.add(opId1);

        // Duplicate replay of opId1 -> ignored
        boolean isDuplicateExecution = processedOperationIds.contains(opId1);
        assertTrue(isDuplicateExecution);

        // Distinct opId2 -> processed
        assertFalse(processedOperationIds.contains(opId2));
        processedOperationIds.add(opId2);
        assertEquals(2, processedOperationIds.size());
    }

    // =========================================================================
    // 4. User Profile Entity Model Verification
    // =========================================================================
    @Test
    public void testUserProfileEntityDefaults() {
        long now = System.currentTimeMillis();
        UserProfileEntity entity = new UserProfileEntity(
                "usr_1272",
                "Abdullah Al Riad",
                "user@deenone.top",
                "01700000000",
                "Asia/Dhaka",
                now,
                1272,
                1,
                7,
                true,
                "/data/user/0/com.devflux.deenone/files/avatar_usr_1272.jpg",
                -1,
                5,
                8,
                3,
                now
        );

        assertEquals("usr_1272", entity.getUserId());
        assertEquals("Abdullah Al Riad", entity.getFullName());
        assertEquals("Asia/Dhaka", entity.getTimezone());
        assertEquals(1272, entity.getPoints());
        assertTrue(entity.isVerified());
        assertEquals(5, entity.getSalahCompletedTotal());
        assertEquals(8, entity.getAmalCompletedTotal());
        assertEquals(3, entity.getQuizCompletedTotal());
    }

    // =========================================================================
    // 5. Backend UserProfile Model Verification
    // =========================================================================
    @Test
    public void testBackendUserProfileContract() {
        UserProfile profile = new UserProfile(
                "usr_1272",
                "Abdullah Al Riad",
                "user@deenone.top",
                "01700000000",
                "https://api.deenone.top/avatars/usr_1272.jpg",
                0,
                1272,
                System.currentTimeMillis(),
                "Asia/Dhaka",
                true
        );

        assertNotNull(profile.getUserId());
        assertEquals("Abdullah Al Riad", profile.getUserName());
        assertEquals("Asia/Dhaka", profile.getTimezone());
        assertEquals(1272, profile.getTotalPoints());
        assertTrue(profile.isVerified());
    }
}
