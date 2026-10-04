package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahFaqTest {

    @Test
    public void testBengaliFaqCards() {
        List<SalahFaqPageDialog.FaqItem> items = SalahFaqPageDialog.getFaqItems(true);
        assertNotNull(items);
        assertEquals("Must contain 12 FAQ cards", 12, items.size());

        // Card 1: আমীন
        assertEquals("Card 1 title must match", "নামাযের মধ্যে আমীন আওয়ায দিয়ে পড়া ভাল না কি আস্তে পড়া ভাল", items.get(0).title);
        assertTrue("Card 1 must contain Hanafi Madhhab silent Ameen", items.get(0).fullContent.contains("হানাফী মাযহাব মুতাবেক আমীন আস্তে পড়া ভাল"));
        assertTrue("Card 1 must contain Fatawa Darul Uloom reference", items.get(0).fullContent.contains("ফাতাওয়া দারুল উলূম- ২/১৭৭। ১৬৮"));

        // Card 2: একা মাগরিব/এশা/ফজর কেরাত
        assertEquals("Card 2 title must match", "কোন ব্যক্তি যদি একা মাগরিব বা ইশা বা ফজরের নামায পড়ে তাহলে সে কেরাত কি জোরে পড়বে না কি আস্তে পড়বে?", items.get(1).title);
        assertTrue("Card 2 must contain choice", items.get(1).fullContent.contains("তার জন্য এখতেয়ার রয়েছে"));
        assertTrue("Card 2 must contain Durre Mukhtar reference", items.get(1).fullContent.contains("দুররে মুখতার- ০১/৪৯৮। ১৭০"));

        // Card 3: হাত বাঁধা
        assertEquals("Card 3 title must match", "নামাযের মধ্যে হাত নাভির উপরে বাঁধবে না কি নাভির নিচে বাঁধবে?", items.get(2).title);
        assertTrue("Card 3 must contain Ali RA Abu Dawud", items.get(2).fullContent.contains("আবু দাউদ শরীফে হযরত আলী রাঃ"));

        // Card 4: শাহাদাত আঙ্গুল
        assertEquals("Card 4 title must match", "যদি কোন ব্যক্তি ডান হাতের শাহাদাত আঙ্গুলী না উঠাতে পারে – তাহলে বাম হাতের শাহাদাত আঙ্গুলী দ্বারা তাশাহহুদের সময় ইশারা করা জায়েয আছে কি না?", items.get(3).title);
        assertTrue("Card 4 must forbid left hand finger", items.get(3).fullContent.contains("বাম হাতের আঙ্গুল দিয়ে ইশারা করবে না"));
        assertTrue("Card 4 must contain Fatawa Darul Uloom reference", items.get(3).fullContent.contains("ফাতাওয়া দারুল উলুম - ২/১৯২"));

        // Card 5: লিইলাফি ও আলামতারা
        assertEquals("Card 5 title must match", "প্রথম রাকআতে লিইলাফি সূরা দ্বিতীয় রাকআতে আলামতারা সূরা পড়ল তাহলে নামায হবে কি না?", items.get(4).title);
        assertTrue("Card 5 must validate prayer", items.get(4).fullContent.contains("নামায জায়েয হবে এবং মাকরূহ ও হবে না"));

        // Card 6: ইমাম ও মুক্তাদীর তাসবীহ
        assertEquals("Card 6 title must match", "ইমাম সাহেব রুকু অথবা সেজদা হতে তাসবীহ পড়ে মাথা উঠিয়ে ফেলছেন। কিন্তু মুক্তাদী এখনও তাসবীহ তিনবার পুরা করতে পারেনি। এখন মুক্তাদী কি করবে?", items.get(5).title);
        assertTrue("Card 6 must contain imam advice", items.get(5).fullContent.contains("ইমাম সাহেবের জন্য উচিত"));

        // Card 7: ইমামের সালামের পর দোয়া
        assertEquals("Card 7 title must match", "ইমাম সাহেব সালাম ফেরানোর পর যে দোয়া করেন তাতে মুক্তাদীগণ শরীক হওয়া জরুরী কি না?", items.get(6).title);
        assertTrue("Card 7 must state Mustahabb", items.get(6).fullContent.contains("মুস্তাহাব, জরুরী নয়"));

        // Card 8: এক চাটাইয়ের পুরুষ ও মহিলা
        assertEquals("Card 8 title must match", "এক চাটাইয়ের উপর যদি কোন পুরুষ এবং মহিলা এক বরাবর দাঁড়িয়ে নামায পড়ে তাহলে তাদের নামায দুরুস্ত হবে কি না?", items.get(7).title);
        assertTrue("Card 8 must detail separate vs jamaat", items.get(7).fullContent.contains("বেগানা মহিলার বরাবর দাঁড়ায়ে নামায পড়া মাকরূহ"));
        assertTrue("Card 8 must contain Fatawa Darul Uloom reference", items.get(7).fullContent.contains("ফাতাওয়া দারুল উলুম - ২/ ১৮১-১৮২। ১৬৯"));

        // Card 9: তাকবীরে হামযা টানা
        assertEquals("Card 9 title must match", "যদি কোন ব্যক্তি নামাযের তাকবীরে আল্লাহ শব্দের হামযা টেনে পড়ে অথবা আকবার শব্দের হামযা টেনে পড়ে তার নামায দুরুস্ত হবে কি না?", items.get(8).title);
        assertTrue("Card 9 must state Fasid", items.get(8).fullContent.contains("তার নামায ফাসেদ হয়ে যাবে"));
        assertTrue("Card 9 must contain Durre Mukhtar reference", items.get(8).fullContent.contains("দুররে মুখতার- ১/৪৪৮।০৭"));

        // Card 10: শেষ দু রাকাতে সূরা না মিলানো
        assertEquals("Card 10 title must match", "ফরয নামাযে প্রথম দু রাকাআতে ফাতিহার সাথে সূরা মিলানো হয়, শেষ দু রাক’আতে মিলানো হয় না কেন তার কারণ কি?", items.get(9).title);
        assertTrue("Card 10 must state Prophet and Sahaba sunnah", items.get(9).fullContent.contains("নবী করীম সা. এবং সাহাবা কেরাম এ রকম পড়েছেন"));
        assertTrue("Card 10 must contain Fatawa Darul Uloom reference", items.get(9).fullContent.contains("ফাতাওয়া দারুল উলুম - ২/১৭৫"));

        // Card 11: দ্বিতীয় রাকাতে লম্বা কেরাত
        assertEquals("Card 11 title must match", "যদি কোন ব্যক্তি প্রথম রাক’আতের চেয়ে দ্বিতীয় রাকআতে লম্বা ক্বেরাআত পড়ে তাহলে নামায জায়েয হবে কি না?", items.get(10).title);
        assertTrue("Card 11 must mention Makruh Tanzihi", items.get(10).fullContent.contains("মাকরূহে তানযীহ"));
        assertTrue("Card 11 must contain Durre Mukhtar 1st Vol reference", items.get(10).fullContent.contains("দুররে মুখতার ১ম খন্ড"));

        // Card 12: উল্টা তারতীবে সূরা
        assertEquals("Card 12 title must match", "নফল নামাযে যদি কেউ উল্টা তারতীবে সূরা পড়ে যেমন প্রথম রাকআতে কুলহুয়াল্লাহ সূরা আর দ্বিতীয় রাকআতে তাব্বাত ইয়াদা সূরা পড়ল তার নামায জায়েয হবে কি না?", items.get(11).title);
        assertTrue("Card 12 must validate Nafl reverse recitation", items.get(11).fullContent.contains("নামায জায়েয হবে এবং মাকরূহ ও হবে না"));
        assertTrue("Card 12 must contain Fatawa Darul Uloom reference", items.get(11).fullContent.contains("ফাতাওয়া দারুল উলুম - ২/২১৮ ১৯০"));

        for (SalahFaqPageDialog.FaqItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishFaqCards() {
        List<SalahFaqPageDialog.FaqItem> items = SalahFaqPageDialog.getFaqItems(false);
        assertNotNull(items);
        assertEquals("Must contain 12 FAQ cards in English", 12, items.size());

        for (SalahFaqPageDialog.FaqItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
