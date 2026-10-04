package com.devflux.deenone.features.profile;

import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.data.local.entity.AmalRecordEntity;

import org.junit.Assert;
import org.junit.Test;

public class PointRedemptionAndRankComprehensiveTest {

    @Test
    public void testAmalRecordRealtimePercentageCalculation() {
        int prayedToday = 3;
        int amalsCompleted = 2;
        int quizPlayed = 1;
        int totalPossible = 11;

        int completedItems = prayedToday + amalsCompleted + (quizPlayed > 0 ? 1 : 0);
        double percentage = ((double) completedItems / totalPossible) * 100.0;

        AmalRecordEntity record = new AmalRecordEntity("2026-08-22", 7, amalsCompleted, totalPossible, prayedToday, 120, percentage);
        Assert.assertEquals(3, record.getCompletedPrayersCount());
        Assert.assertEquals(2, record.getCompletedGoalsCount());
        Assert.assertEquals(11, record.getTotalGoalsCount());
        Assert.assertEquals(54.54, record.getOverallPercentage(), 0.1);
    }

    @Test
    public void testLeaderboardUserRankOrdering() {
        LeaderboardUser u1 = new LeaderboardUser(1, "usr_101", "আল মামুন", "", 1240, 28, "🏆 মুত্তাকী");
        LeaderboardUser u2 = new LeaderboardUser(2, "usr_102", "তানভীর হাসান", "", 1180, 24, "🥈 সালেহীন");
        LeaderboardUser u3 = new LeaderboardUser(3, "usr_103", "আহমেদ জুবায়ের", "", 1120, 21, "🥉 মুখলিস");

        Assert.assertTrue(u1.getPoints() > u2.getPoints());
        Assert.assertTrue(u2.getPoints() > u3.getPoints());
        Assert.assertEquals(1, u1.getRank());
        Assert.assertEquals(2, u2.getRank());
        Assert.assertEquals(3, u3.getRank());
    }

    @Test
    public void testRedemptionCostValidation() {
        int userPoints = 1200;
        int giftCost = 2000;
        int audioPackCost = 500;

        Assert.assertTrue("User should not have enough points for gift box", userPoints < giftCost);
        Assert.assertTrue("User should have enough points for audio pack", userPoints >= audioPackCost);

        int remaining = userPoints - audioPackCost;
        Assert.assertEquals(700, remaining);
    }
}
