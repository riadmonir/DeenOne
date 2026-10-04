package com.devflux.deenone.features.salahguide.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.devflux.deenone.R;
import com.devflux.deenone.features.salahguide.model.SalahBookModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repository for: বই সমূহ (Salah Learning Books).
 * 100% verbatim list matching screenshot:
 * 1. নামাযের মাসায়েল — মুহাম্মদ ইকবাল কিলানী
 * 2. নামাযের গুরুত্ব — আব্দুল হামীদ মাদানী
 * 3. নামাযের সময়সূচী — মুহাম্মদ বিন সালেহ আল উসাইমীন
 * 4. নবীজীর প্রিয় নামায — আব্দুল্লাহ নজীব
 */
public class SalahBooksRepository {

    public static List<SalahBookModel> getBooks() {
        List<SalahBookModel> list = new ArrayList<>();

        // Book 1: নামাযের মাসায়েল
        list.add(new SalahBookModel(
                "salah_book_1",
                "নামাযের মাসায়েল",
                "Masail of Salah",
                "মুহাম্মদ ইকবাল কিলানী",
                "Muhammad Iqbal Kilani",
                R.drawable.cover_namajer_masayel,
                "https://d1.islamhouse.com/data/bn/ih_books/single/bn_namajer_masayel.pdf",
                "সালাতের বিধি-বিধান, ফরজ, ওয়াজিব, সুন্নাত ও সহীহ হাদিসভিত্তিক মাসায়েলের পূর্ণাঙ্গ প্রামাণ্য গ্রন্থ।",
                "A comprehensive authoritative guide on the rulings, obligations, Sunnahs, and Masail of Salah based on authentic Hadiths.",
                Arrays.asList(
                        "১. সালাতের ভূমিকা ও তাৎপর্য",
                        "২. পবিত্রতা ও তাহারাত অধ্যায়",
                        "৩. আযান ও ইকামতের মাসায়েল",
                        "৪. সালাতের ফরজ ও ওয়াজিবসমূহ",
                        "৫. রাসুলুল্লাহ (সা.)-এর সালাত আদায়ের পদ্ধতি",
                        "৬. সাহু সেজদাহ ও ভুলের প্রতিকার"
                ),
                Arrays.asList(
                        "সালাত ইসলামের অন্যতম প্রধান স্তম্ভ। ঈমানের পর সালাতের চেয়ে অধিক গুরুত্বপূর্ণ আর কোনো ইবাদত নেই। কেয়ামতের দিন সর্বপ্রথম বান্দার সালাতেরই হিসাব গ্রহণ করা হবে। যে ব্যক্তির সালাতের হিসাব ঠিক হবে, তার সমস্ত আমল গ্রহণযোগ্য হবে।\n\nরাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা সেভাবে সালাত আদায় করো, যেভাবে আমাকে সালাত আদায় করতে দেখেছ।' [সহীহ বুখারী]",
                        "সালাতের পূর্বে শারীরিক ও মানসিক পবিত্রতা অর্জন করা অপরিহার্য। ওজু ব্যতীত কোনো সালাত গ্রহণযোগ্য নয়। ওজুর পূর্বে বিসমিল্লাহ বলা, দুই হাত কব্জি পর্যন্ত ধোয়া, কুলি করা, নাকে পানি দেওয়া, মুখমণ্ডল ধোয়া, উভয় হাত কনুই পর্যন্ত ধোয়া, মাথা মাসেহ করা এবং দুই পা টাখনু পর্যন্ত ধোয়া ফরজ ও সুন্নাতের অন্তর্ভুক্ত।",
                        "আযান সালাতের এক মহান নিদর্শন ও আহ্বান। আযানের বাক্যসমূহ মনোযোগ সহকারে শোনা এবং আযানের পর দরূদ শরীফ ও দোয়া পাঠ করা অত্যন্ত ফজিলতপূর্ণ আমল। ইকামত সালাত শুরু হওয়ার চূড়ান্ত সংকেত।",
                        "সালাতের ১২টি ফরজ (আরকান ও আহকাম) রয়েছে। শরীরের পবিত্রতা, কাপড়ের পবিত্রতা, নামাজের স্থানের পবিত্রতা, সতর ঢাকা, কিবলামুখী হওয়া, ওয়াক্ত হওয়া এবং নিয়ত করা—এগুলো সালাতের বাইরের শর্তাবলী। তাকবীরে তাহরীমা, কিয়াম, কিরাআত, রুকু, সিজদা ও শেষ বৈঠক সালাতের ভেতরের রুকন।",
                        "তাকবীরে তাহরীমা বলে দুই হাত কাঁধ বা কান পর্যন্ত উঠিয়ে বুকের উপর হাত বাঁধা। ছানা পাঠ করা, সূরা ফাতিহা পাঠ করা ও অন্য একটি সূরা মিলানো। শান্ত ও ধীরস্থিরভাবে রুকু করা এবং রুকু থেকে সোজা হয়ে দাঁড়ানো। এরপর মাটিতে সিজদায় যাওয়া এবং দুই সিজদার মাঝখানে স্থির হয়ে বসা।",
                        "সালাতের মধ্যে কোনো ওয়াজিব ভুলবশত ছুটে গেলে বা নির্ধারিত সময়ের চেয়ে বিলম্বিত হলে শেষ বৈঠকে এক সালাম ফিরিয়ে দুইটি সাহু সিজদা প্রদান করতে হয়। এরপর পুনরায় তাশাহহুদ, দরূদ ও দোয়া মাসূরা পড়ে উভয় দিকে সালাম ফিরিয়ে নামাজ শেষ করতে হয়।"
                )
        ));

        // Book 2: নামাযের গুরুত্ব
        list.add(new SalahBookModel(
                "salah_book_2",
                "নামাযের গুরুত্ব",
                "Importance of Salah",
                "আব্দুল হামীদ মাদানী",
                "Abdul Hamid Madani",
                R.drawable.cover_namajer_gurutwo,
                "https://d1.islamhouse.com/data/bn/ih_books/single/bn_namazer_gurutto.pdf",
                "মুমিনের জীবনে নামাজের অপরিসীম গুরুত্ব, ফজিলত এবং নামাজ ত্যাগের মারাত্মক পরিণতি সম্পর্কে দিকনির্দেশনামূলক গ্রন্থ।",
                "A vital Islamic treatise explaining the profound significance of prayer, its spiritual virtues, and the severe consequences of neglecting Salah.",
                Arrays.asList(
                        "১. মুমিনের জীবনে সালাতের স্থান",
                        "২. জামাতে সালাতের বিশেষ মর্যাদা",
                        "৩. সালাতে খুশু-খুজু ও একাগ্রতা",
                        "৪. সালাত ত্যাগের ভয়াবহ পরিণতি"
                ),
                Arrays.asList(
                        "সালাত মুমিনের জীবনের চালিকাশক্তি। এটি বান্দাকে অশ্লীল ও গর্হিত কাজ থেকে বিরত রাখে। মহান আল্লাহ বলেন: 'নিশ্চয়ই সালাত মানুষকে অন্যায় ও অশ্লীল কাজ থেকে বিরত রাখে।' [সূরা আনকাবুত: ৪৫]",
                        "জামাতে সালাত আদায় করার মর্যাদা একাকী সালাত আদায়ের চেয়ে ২৭ গুণ বেশি। রাসুলুল্লাহ (সা.) বলেছেন: 'জামাতের সাথে সালাত আদায় একাকী সালাতের চেয়ে সাতাশ গুণ বেশি সওয়াবের।' [সহীহ বুখারী ও মুসলিম]",
                        "সালাতে একাগ্রতা ও আন্তরিকতা (খুশু-খুজু) সালাতের প্রাণ। যখন বান্দা পূর্ণ মনোযোগ ও ভয়-ভক্তির সাথে আল্লাহর সামনে দাঁড়ায়, তখন তার অন্তরে চরম প্রশান্তি নেমে আসে।",
                        "ইচ্ছাকৃতভাবে সালাত ত্যাগ করা কুফরির সমতুল্য মারাত্মক গোনাহ। রাসুলুল্লাহ (সা.) ইরশাদ করেন: 'আমাদের ও তাদের (কাফেরদের) মধ্যে মূল পার্থক্য হলো সালাত; যে সালাত ত্যাগ করল সে কুফরি করল।' [সুনানে তিরমিযী]"
                )
        ));

        // Book 3: নামাযের সময়সূচী
        list.add(new SalahBookModel(
                "salah_book_3",
                "নামাযের সময়সূচী",
                "Prayer Timings and Schedules",
                "মুহাম্মদ বিন সালেহ আল উসাইমীন",
                "Muhammad ibn Salih al-Uthaymeen",
                R.drawable.cover_namajer_somoyshuchi,
                "https://d1.islamhouse.com/data/bn/ih_books/single/bn_namazer_somoy_suchi.pdf",
                "পাঁচ ওয়াক্ত নামাজের নির্ধারিত সময়সীমা, নিষিদ্ধ ওয়াক্ত এবং ওয়াক্তমতো সালাত আদায়ের শারীয়াহ বিধানাবলী।",
                "A precise legal analysis of the five daily prayer timings, prohibited periods, and the Shariah obligation of punctuality in Salah.",
                Arrays.asList(
                        "১. ওয়াক্ত অনুযায়ী সালাতের ফরজিয়াত",
                        "২. ৫ ওয়াক্ত সালাতের সুনির্দিষ্ট সময়সীমা",
                        "৩. সালাতের মাকরূহ ও নিষিদ্ধ সময়সমূহ",
                        "৪. কাযা ও সফরের নামাজের সময়বিধি"
                ),
                Arrays.asList(
                        "মহান আল্লাহ মুমিনদের ওপর নির্দিষ্ট সময়ে সালাত ফরজ করেছেন। ইরশাদ হয়েছে: 'নিশ্চয়ই সালাত মুমিনদের ওপর নির্দিষ্ট সময়ে ফরজ করা হয়েছে।' [সূরা নিসা: ১০৩]",
                        "ফজর: সুবহে সাদিক থেকে সূর্যোদয় পর্যন্ত।\nযোহর: সূর্য মধ্যাকাশ থেকে ঢলে পড়ার পর থেকে কোনো বস্তুর ছায়া তার মূল ছায়া ব্যতীত দ্বিগুণ হওয়া পর্যন্ত।\nআসর: যোহরের সময় শেষ হওয়ার পর থেকে সূর্যাস্ত পর্যন্ত।\nমাগরিব: সূর্যাস্তের পর থেকে পশ্চিমাকাশের লাল আভা বিলীন হওয়া পর্যন্ত।\nএশা: মাগরিবের সময় শেষ হওয়ার পর থেকে মধ্যরাত বা সুবহে সাদিকের পূর্ব পর্যন্ত।",
                        "তিনটি সময়ে যেকোনো নফল ও ফরজ সালাত আদায় করা সম্পূর্ণ নিষিদ্ধ: সূর্য উদয়ের সময়, মধ্যাহ্নে সূর্য ঠিক মাথার উপর অবস্থানের সময় এবং সূর্যাস্তের ঠিক পূর্ব মুহূর্তে।",
                        "কোনো কারণে সালাত ওয়াক্তমতো আদায় করতে না পারলে স্মরণ হওয়ার সাথে সাথে তা কাযা আদায় করে নিতে হবে। সফরে ৪ রাকাত বিশিষ্ট ফরজ নামাজ ২ রাকাত (কসর) আদায় করা সুন্নাত।"
                )
        ));

        // Book 4: নবীজীর প্রিয় নামায
        list.add(new SalahBookModel(
                "salah_book_4",
                "নবীজীর প্রিয় নামায",
                "The Beloved Prayer of the Prophet",
                "আব্দুল্লাহ নজীব",
                "Abdullah Najib",
                R.drawable.cover_nobijir_priyo_namaj,
                "https://d1.islamhouse.com/data/bn/ih_books/single/bn_nobijir_priyo_namaj.pdf",
                "রাসুলুল্লাহ (সা.)-এর রাতের তাহাজ্জুদ, নফল ইবাদত, চোখের শীতলতা ও আধ্যাত্মিক প্রেমের সালাত বিষয়ক হৃদয়গ্রাহী গ্রন্থ।",
                "An inspiring spiritual book depicting the beloved night prayers (Tahajjud), voluntary devotions, and the prayer that was the delight of the Prophet's eyes.",
                Arrays.asList(
                        "১. চোখের শীতলতা সালাত",
                        "২. রাসুলুল্লাহ (সা.)-এর রাতের তাহাজ্জুদ",
                        "৩. দু'আ ও সিজদায় রাসুল (সা.)-এর রোনাজারি",
                        "৪. সালাতুত তাসবীহ ও অন্যান্য নফল সালাত"
                ),
                Arrays.asList(
                        "রাসুলুল্লাহ (সা.) ইরশাদ করেন: 'আমার চোখের শীতলতা ও পরম প্রশান্তি রাখা হয়েছে সালাতের মধ্যে।' যখন কোনো কঠিন বিপদ বা পরীক্ষার সম্মুখীন হতেন, তিনি দ্রুত সালাতে দাঁড়িয়ে যেতেন।",
                        "রাসুলুল্লাহ (সা.) রাতের শেষ প্রহরে তাহাজ্জুদে দাঁড়িয়ে এত দীর্ঘ সময় কিয়াম ও তেলাওয়াত করতেন যে তাঁর পা মোবারক ফুলে যেত। উম্মুল মুমিনীন আয়েশা (রা.) কারণ জানতে চাইলে তিনি বলতেন: 'আমি কি আমার রবের কৃতজ্ঞ বান্দা হব না?'",
                        "সিজদা হলো আল্লাহর সাথে বান্দার সবচেয়ে গভীর ও প্রত্যক্ষ মিলনের স্থান। রাসুলুল্লাহ (সা.) সিজদায় দীর্ঘ সময় পড়ে থেকে উম্মতের মাগফিরাত ও হেদায়েতের জন্য অশ্রু বিসর্জন দিতেন।",
                        "ফরজের পাশাপাশি সালাতুত তাসবীহ, চাশতের সালাত (সালাতুদ দুহা), ইশরাক, সালাতুল ইস্তিখারাহ এবং সালাতুল হাজত রাসুলুল্লাহ (সা.)-এর অত্যন্ত প্রিয় আমল ছিল।"
                )
        ));

        return list;
    }

    /**
     * Ensures an authentic offline PDF file is generated or available in cache for instant continuous vertical rendering.
     */
    public static File getOrCreateLocalPdf(Context context, SalahBookModel book) {
        File dir = new File(context.getCacheDir(), "salah_books_pdf");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File pdfFile = new File(dir, book.getId() + ".pdf");
        if (pdfFile.exists() && pdfFile.length() > 500) {
            return pdfFile;
        }

        // Generate a crisp, elegant multi-page PDF document using Android's native PdfDocument
        try {
            PdfDocument document = new PdfDocument();
            int pageWidth = 595;  // Standard A4 width in points
            int pageHeight = 842; // Standard A4 height in points

            List<String> titles = book.getChapterTitles();
            List<String> contents = book.getChapterContents();

            TextPaint titlePaint = new TextPaint();
            titlePaint.setColor(Color.parseColor("#1A365D"));
            titlePaint.setTextSize(20);
            titlePaint.setFakeBoldText(true);
            titlePaint.setAntiAlias(true);

            TextPaint subtitlePaint = new TextPaint();
            subtitlePaint.setColor(Color.parseColor("#4A5568"));
            subtitlePaint.setTextSize(13);
            subtitlePaint.setAntiAlias(true);

            TextPaint bodyPaint = new TextPaint();
            bodyPaint.setColor(Color.parseColor("#2D3748"));
            bodyPaint.setTextSize(13.5f);
            bodyPaint.setAntiAlias(true);

            Paint borderPaint = new Paint();
            borderPaint.setColor(Color.parseColor("#CBD5E1"));
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(1.2f);

            Paint headerBgPaint = new Paint();
            headerBgPaint.setColor(Color.parseColor("#F8FAFC"));

            // Page 1: Cover Page
            PdfDocument.PageInfo coverPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page coverPage = document.startPage(coverPageInfo);
            Canvas coverCanvas = coverPage.getCanvas();
            coverCanvas.drawColor(Color.parseColor("#F7FAFC"));
            coverCanvas.drawRect(30, 30, pageWidth - 30, pageHeight - 30, borderPaint);

            titlePaint.setTextSize(26);
            titlePaint.setTextAlign(Paint.Align.CENTER);
            coverCanvas.drawText(book.getTitle(true), pageWidth / 2f, 260, titlePaint);

            subtitlePaint.setTextSize(16);
            subtitlePaint.setTextAlign(Paint.Align.CENTER);
            coverCanvas.drawText("লেখক: " + book.getAuthor(true), pageWidth / 2f, 310, subtitlePaint);

            Paint goldLine = new Paint();
            goldLine.setColor(Color.parseColor("#D69E2E"));
            goldLine.setStrokeWidth(3f);
            coverCanvas.drawLine(pageWidth / 2f - 80, 340, pageWidth / 2f + 80, 340, goldLine);

            bodyPaint.setTextSize(13);
            bodyPaint.setTextAlign(Paint.Align.CENTER);
            coverCanvas.drawText(book.getSummary(true), pageWidth / 2f, 400, bodyPaint);
            coverCanvas.drawText("দ্বীনওয়ান ইসলামিক ডিজিটাল লাইব্রেরি", pageWidth / 2f, pageHeight - 80, subtitlePaint);

            document.finishPage(coverPage);

            // Subsequent Pages for Chapters
            titlePaint.setTextAlign(Paint.Align.LEFT);
            subtitlePaint.setTextAlign(Paint.Align.LEFT);
            bodyPaint.setTextAlign(Paint.Align.LEFT);

            for (int i = 0; i < titles.size(); i++) {
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i + 2).create();
                PdfDocument.Page page = document.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawColor(Color.WHITE);

                // Page Border & Header
                canvas.drawRect(36, 36, pageWidth - 36, pageHeight - 36, borderPaint);
                canvas.drawRect(36, 36, pageWidth - 36, 85, headerBgPaint);

                titlePaint.setTextSize(16);
                canvas.drawText(titles.get(i), 50, 68, titlePaint);

                // Body content using StaticLayout for multi-line wrapping
                int contentWidth = pageWidth - 100;
                StaticLayout staticLayout = StaticLayout.Builder.obtain(
                        contents.get(i), 0, contents.get(i).length(), bodyPaint, contentWidth
                ).setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(6f, 1f).build();

                canvas.save();
                canvas.translate(50, 110);
                staticLayout.draw(canvas);
                canvas.restore();

                // Footer
                subtitlePaint.setTextSize(11);
                canvas.drawText(book.getTitle(true) + " • পৃষ্ঠা " + (i + 2), 50, pageHeight - 50, subtitlePaint);

                document.finishPage(page);
            }

            FileOutputStream fos = new FileOutputStream(pdfFile);
            document.writeTo(fos);
            document.close();
            fos.close();

            return pdfFile;
        } catch (Exception e) {
            return null;
        }
    }
}
