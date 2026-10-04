package com.devflux.deenone.features.notifications;

import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Assert;
import org.junit.Test;

public class NotificationHistoryUIAndHeaderTest {

    @Test
    public void testBengaliRelativeTime() {
        long now = System.currentTimeMillis();

        // 30 seconds ago -> এইমাত্র
        String justNow = BengaliNumberUtil.getBengaliRelativeTime(now - 30 * 1000L);
        Assert.assertEquals("এইমাত্র", justNow);

        // 1 hour ago -> ১ ঘণ্টা আগে
        String oneHourAgo = BengaliNumberUtil.getBengaliRelativeTime(now - 3600 * 1000L);
        Assert.assertEquals("১ ঘণ্টা আগে", oneHourAgo);

        // 14 hours ago -> ১৪ ঘণ্টা আগে
        String fourteenHoursAgo = BengaliNumberUtil.getBengaliRelativeTime(now - 14 * 3600 * 1000L);
        Assert.assertEquals("১৪ ঘণ্টা আগে", fourteenHoursAgo);

        // 1 day ago -> গতকাল
        String yesterday = BengaliNumberUtil.getBengaliRelativeTime(now - 25 * 3600 * 1000L);
        Assert.assertEquals("গতকাল", yesterday);

        // 3 days ago -> ৩ দিন আগে
        String threeDaysAgo = BengaliNumberUtil.getBengaliRelativeTime(now - 3 * 24 * 3600 * 1000L);
        Assert.assertEquals("৩ দিন আগে", threeDaysAgo);
    }

    @Test
    public void testTotalCountBadgeFormatting() {
        int total15 = 15;
        String badge15 = "🔔 মোট: " + BengaliNumberUtil.toBengali(total15);
        Assert.assertEquals("🔔 মোট: ১৫", badge15);

        int total0 = 0;
        String badge0 = "🔔 মোট: " + BengaliNumberUtil.toBengali(total0);
        Assert.assertEquals("🔔 মোট: ০", badge0);
    }

    @Test
    public void testNotificationEntityTypes() {
        long now = System.currentTimeMillis();
        NotificationMessageEntity sunnahNotif = new NotificationMessageEntity(
                "নতুন সুন্নাহ যুক্ত হয়েছে",
                "নখ কাটার সময়সীমা সুন্নাহটি এখন আপনি পালন করতে পারবেন।",
                "sunnah",
                now - (3600 * 1000L),
                false,
                "sunnah_tracker",
                "normal",
                "system"
        );

        Assert.assertEquals("sunnah", sunnahNotif.getType());
        Assert.assertEquals("নতুন সুন্নাহ যুক্ত হয়েছে", sunnahNotif.getTitle());
        Assert.assertFalse(sunnahNotif.isRead());
    }
}
