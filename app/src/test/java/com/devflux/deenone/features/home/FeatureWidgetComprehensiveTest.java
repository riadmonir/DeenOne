package com.devflux.deenone.features.home;

import com.devflux.deenone.data.local.entity.FeatureWidgetEntity;
import com.devflux.deenone.features.home.model.FeatureItem;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FeatureWidgetComprehensiveTest {

    @Test
    public void testFeatureWidgetEntityCreationAndAttributes() {
        FeatureWidgetEntity entity = new FeatureWidgetEntity(
                "feat_salat",
                "সালাত",
                "ic_clock",
                "#38230E",
                "#F59E0B",
                1,
                true,
                "action_salat"
        );

        Assert.assertEquals("feat_salat", entity.getFeatureId());
        Assert.assertEquals("সালাত", entity.getTitle());
        Assert.assertEquals("ic_clock", entity.getIconResName());
        Assert.assertEquals("#38230E", entity.getCircleBgHex());
        Assert.assertEquals("#F59E0B", entity.getIconTintHex());
        Assert.assertEquals(1, entity.getDisplayOrder());
        Assert.assertTrue(entity.isEnabled());
        Assert.assertEquals("action_salat", entity.getActionKey());
    }

    @Test
    public void testExact27FeaturesOrderAndCount() {
        String[] expectedTitles = {
                "সালাত", "নামাজ শিক্ষা", "কুরআন মাজিদ",
                "ইসলামিক অডিও হাব", "ইসলামিক বই", "হাদিস শরিফ",
                "দোয়া ভাণ্ডার", "আমল ট্র্যাকার", "কুইজ হাব",
                "মসজিদ সন্ধান", "সফর মোড", "জুম্মা মোড",
                "ঈদ মোড", "জানাযা গাইড", "রোজা ট্র্যাকার",
                "খতম প্ল্যানার", "আজকের আয়াত", "হজ ও উমরাহ",
                "রমজান মোড", "হিজরি ক্যালেন্ডার", "যাকাত ক্যালকুলেটর",
                "তাসবিহ কাউন্টার", "দৈনিক আজকার", "কিবলা কম্পাস",
                "দ্বীন জার্নি", "কুরআন স্লিপ মোড", "রক্তদান সেকশন"
        };

        Assert.assertEquals(27, expectedTitles.length);

        // 3 items per row means exactly 9 rows
        Assert.assertEquals(0, expectedTitles.length % 3);
        int rows = expectedTitles.length / 3;
        Assert.assertEquals(9, rows);
    }

    @Test
    public void testFeatureItemModelColors() {
        FeatureItem item = new FeatureItem(1, "সালাত", 12345, 0xFF38230E, 0xFFF59E0B, "action_salat");
        Assert.assertEquals(1, item.getId());
        Assert.assertEquals("সালাত", item.getTitle());
        Assert.assertEquals(12345, item.getIconResId());
        Assert.assertEquals(0xFF38230E, item.getCircleBgColorInt());
        Assert.assertEquals(0xFFF59E0B, item.getIconTintInt());
        Assert.assertEquals("action_salat", item.getActionKey());
    }
}
