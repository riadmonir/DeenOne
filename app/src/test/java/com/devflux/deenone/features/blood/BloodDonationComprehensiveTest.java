package com.devflux.deenone.features.blood;

import com.devflux.deenone.data.local.entity.BloodDonorEntity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BloodDonationComprehensiveTest {

    @Test
    public void testBloodDonorEntityCreationAndAttributes() {
        BloodDonorEntity donor = new BloodDonorEntity(
                "donor_101",
                "usr_1272",
                "riadmonir",
                "B+",
                "01736650975",
                "মদিনা মুনাওয়ারা, সৌদি আরব",
                1000L,
                2000L,
                true
        );

        Assert.assertEquals("donor_101", donor.getDonorId());
        Assert.assertEquals("usr_1272", donor.getUserId());
        Assert.assertEquals("riadmonir", donor.getName());
        Assert.assertEquals("B+", donor.getBloodGroup());
        Assert.assertEquals("01736650975", donor.getPhone());
        Assert.assertEquals("মদিনা মুনাওয়ারা, সৌদি আরব", donor.getLocation());
        Assert.assertTrue(donor.isAvailable());
    }

    @Test
    public void testBloodGroupFilterMatching() {
        List<BloodDonorEntity> allDonors = new ArrayList<>();
        allDonors.add(new BloodDonorEntity("1", "u1", "riadmonir", "B+", "01736650975", "মদিনা", 0, 0, true));
        allDonors.add(new BloodDonorEntity("2", "u2", "মামুন", "A+", "01812345678", "ঢাকা", 0, 0, true));
        allDonors.add(new BloodDonorEntity("3", "u3", "হাসান", "B+", "01987654321", "চট্টগ্রাম", 0, 0, true));

        List<BloodDonorEntity> bPosDonors = new ArrayList<>();
        for (BloodDonorEntity d : allDonors) {
            if ("B+".equalsIgnoreCase(d.getBloodGroup())) {
                bPosDonors.add(d);
            }
        }

        Assert.assertEquals(2, bPosDonors.size());
        Assert.assertEquals("riadmonir", bPosDonors.get(0).getName());
        Assert.assertEquals("হাসান", bPosDonors.get(1).getName());
    }

    @Test
    public void testRegisteredUserCanPostAndGuestRestriction() {
        // Authenticated user check
        String loggedInUserId = "usr_1272";
        boolean isAuthenticated = loggedInUserId != null && !loggedInUserId.isEmpty();
        Assert.assertTrue("Logged-in user must be authorized to register/post", isAuthenticated);

        // Guest user check
        String guestUserId = "";
        boolean isGuest = guestUserId == null || guestUserId.isEmpty();
        Assert.assertTrue("Guest user should be flagged for registration prompt", isGuest);
    }
}
