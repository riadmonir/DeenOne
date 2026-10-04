package com.devflux.deenone.features.books.pdf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.devflux.deenone.R;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.BottomSheetRichBookReaderBinding;
import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * RichBookReaderDialog — Ported from Flutter BookReaderScreen with 4 reading themes,
 * dynamic font scaling, table of contents, auto-resume, and dual rich-text/PDF engine.
 */
public class RichBookReaderDialog {

  public enum ReaderTheme {
    LIGHT("#FAFAFA", "#1E293B", "#FFFFFF", "#E2E8F0"),
    SEPIA("#F4ECD8", "#433422", "#EADFC6", "#D3C5A5"),
    DARK("#1E293B", "#F1F5F9", "#334155", "#475569"),
    MIDNIGHT("#0F172A", "#E2E8F0", "#1E293B", "#334155");

    public final String bg;
    public final String text;
    public final String card;
    public final String border;

    ReaderTheme(String bg, String text, String card, String border) {
      this.bg = bg;
      this.text = text;
      this.card = card;
      this.border = border;
    }
  }

  public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, File pdfFile) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    BottomSheetRichBookReaderBinding binding = BottomSheetRichBookReaderBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    SharedPreferences prefs = context.getSharedPreferences("book_reader_prefs", Context.MODE_PRIVATE);
    final float[] currentFontSize = {prefs.getFloat("reader_font_size_" + book.getId(), 16.0f)};
    final String[] currentThemeName = {prefs.getString("reader_theme", "LIGHT")};

    binding.tvReaderTitle.setText(book.getTitle());
    String sourceText = (book.getVerifiedSource() != null && !book.getVerifiedSource().isEmpty()) ? (" • " + book.getVerifiedSource()) : "";
    binding.tvReaderAuthor.setText(book.getAuthor() + sourceText);

    // Sample Chapters generator if reading in rich text mode
    List<String> chapterTitles = generateChapterTitles(book);
    List<String> chapterContents = generateChapterContents(book);

    int totalChapters = Math.max(1, chapterTitles.size());
    final int[] currentChapterIndex = {Math.max(0, Math.min(book.getLastReadPage() > 0 ? book.getLastReadPage() - 1 : 0, totalChapters - 1))};

    // Theme Application Helper
    Runnable applyTheme = () -> {
      ReaderTheme theme;
      try {
        theme = ReaderTheme.valueOf(currentThemeName[0]);
      } catch (Exception e) {
        theme = ReaderTheme.LIGHT;
      }
      int bgColor = Color.parseColor(theme.bg);
      int textColor = Color.parseColor(theme.text);

      binding.layoutBookReaderRoot.setBackgroundColor(bgColor);
      binding.scrollReaderText.setBackgroundColor(bgColor);
      binding.tvChapterHeading.setTextColor(textColor);
      binding.tvChapterBodyText.setTextColor(textColor);
    };

    // Chapter Display Helper
    Runnable updateChapterDisplay = () -> {
      int curIdx = currentChapterIndex[0];
      binding.tvChapterHeading.setText(chapterTitles.get(curIdx));
      binding.tvChapterBodyText.setText(chapterContents.get(curIdx));
      binding.tvChapterBodyText.setTextSize(currentFontSize[0]);

      int pageNum = curIdx + 1;
      int pct = (pageNum * 100) / totalChapters;
      if (isBn) {
        binding.tvReaderPageIndicator.setText("অধ্যায় " + BengaliNumberUtil.toBengali(pageNum) + " / " + BengaliNumberUtil.toBengali(totalChapters) + " • " + BengaliNumberUtil.toBengali(pct) + "% সম্পন্ন");
      } else {
        binding.tvReaderPageIndicator.setText("Chapter " + pageNum + " / " + totalChapters + " • " + pct + "% completed");
      }
      binding.seekBarReaderProgress.setMax(totalChapters - 1);
      binding.seekBarReaderProgress.setProgress(curIdx);

      // Auto-save progress to repository & local DB
      IslamicBookRepository.getInstance(context).updateReadingProgress(book.getId(), pageNum, totalChapters);
      prefs.edit().putInt("last_chapter_" + book.getId(), curIdx).apply();

      binding.scrollReaderText.smoothScrollTo(0, 0);
    };

    final PdfPageAdapter[] pdfAdapterRef = new PdfPageAdapter[1];
    final PdfRenderer[] rendererRef = new PdfRenderer[1];
    final ParcelFileDescriptor[] pfdRef = new ParcelFileDescriptor[1];

    // Dual Engine: Check if PDF file exists and can be rendered
    if (pdfFile != null && pdfFile.exists() && pdfFile.length() > 0) {
      try {
        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(pfd);
        pfdRef[0] = pfd;
        rendererRef[0] = renderer;
        int totalPdfPages = renderer.getPageCount();

        binding.scrollReaderText.setVisibility(View.GONE);
        binding.viewPagerReaderPdf.setVisibility(View.VISIBLE);

        PdfPageAdapter pdfAdapter = new PdfPageAdapter(context, renderer);
        pdfAdapterRef[0] = pdfAdapter;
        binding.viewPagerReaderPdf.setAdapter(pdfAdapter);
        // Continuous vertical scroll per user requirement
        binding.viewPagerReaderPdf.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        binding.viewPagerReaderPdf.setOffscreenPageLimit(1);

        int initialPage = book.getLastReadPage() > 0 && book.getLastReadPage() <= totalPdfPages ? book.getLastReadPage() - 1 : 0;
        binding.viewPagerReaderPdf.setCurrentItem(initialPage, false);

        binding.seekBarReaderProgress.setMax(Math.max(0, totalPdfPages - 1));
        binding.seekBarReaderProgress.setProgress(initialPage);

        binding.viewPagerReaderPdf.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
            int cur = position + 1;
            int pct = (cur * 100) / totalPdfPages;
            if (isBn) {
              binding.tvReaderPageIndicator.setText("পৃষ্ঠা " + BengaliNumberUtil.toBengali(cur) + " / " + BengaliNumberUtil.toBengali(totalPdfPages) + " • " + BengaliNumberUtil.toBengali(pct) + "% সম্পন্ন");
            } else {
              binding.tvReaderPageIndicator.setText("Page " + cur + " / " + totalPdfPages + " • " + pct + "% completed");
            }
            binding.seekBarReaderProgress.setProgress(position);
            IslamicBookRepository.getInstance(context).updateReadingProgress(book.getId(), cur, totalPdfPages);
          }
        });

        binding.btnReaderPrevChapter.setOnClickListener(v -> {
          int pos = binding.viewPagerReaderPdf.getCurrentItem();
          if (pos > 0) binding.viewPagerReaderPdf.setCurrentItem(pos - 1, true);
        });

        binding.btnReaderNextChapter.setOnClickListener(v -> {
          int pos = binding.viewPagerReaderPdf.getCurrentItem();
          if (pos < totalPdfPages - 1) binding.viewPagerReaderPdf.setCurrentItem(pos + 1, true);
        });

      } catch (Exception e) {
        // Fallback to rich text reader
        binding.scrollReaderText.setVisibility(View.VISIBLE);
        binding.viewPagerReaderPdf.setVisibility(View.GONE);
        applyTheme.run();
        updateChapterDisplay.run();
      }
    } else {
      // Text Reader Mode
      binding.scrollReaderText.setVisibility(View.VISIBLE);
      binding.viewPagerReaderPdf.setVisibility(View.GONE);
      applyTheme.run();
      updateChapterDisplay.run();

      binding.btnReaderPrevChapter.setOnClickListener(v -> {
        if (currentChapterIndex[0] > 0) {
          currentChapterIndex[0]--;
          updateChapterDisplay.run();
        }
      });

      binding.btnReaderNextChapter.setOnClickListener(v -> {
        if (currentChapterIndex[0] < totalChapters - 1) {
          currentChapterIndex[0]++;
          updateChapterDisplay.run();
        }
      });
    }

    // Progress Scrubber
    binding.seekBarReaderProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          if (binding.viewPagerReaderPdf.getVisibility() == View.VISIBLE) {
            binding.viewPagerReaderPdf.setCurrentItem(progress, false);
          } else {
            currentChapterIndex[0] = progress;
            updateChapterDisplay.run();
          }
        }
      }
      @Override public void onStartTrackingTouch(SeekBar seekBar) {}
      @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    });

    // Font Size Adjuster Dialog (A- / A+)
    binding.btnReaderFontSize.setOnClickListener(v -> {
      String[] sizes = isBn
          ? new String[]{"১২ sp", "১৪ sp", "১৬ sp", "১৮ sp", "২২ sp", "২৬ sp"}
          : new String[]{"12 sp", "14 sp", "16 sp", "18 sp", "22 sp", "26 sp"};
      float[] floatVals = {12.0f, 14.0f, 16.0f, 18.0f, 22.0f, 26.0f};

      new MaterialAlertDialogBuilder(context)
          .setTitle(isBn ? "ফন্ট সাইজ নির্বাচন করুন" : "Select Font Size")
          .setItems(sizes, (d, which) -> {
            currentFontSize[0] = floatVals[which];
            binding.tvChapterBodyText.setTextSize(currentFontSize[0]);
            prefs.edit().putFloat("reader_font_size_" + book.getId(), currentFontSize[0]).apply();
          })
          .show();
    });

    // Theme Picker Dialog (Light, Sepia, Dark, Midnight)
    binding.btnReaderThemeToggle.setOnClickListener(v -> {
      String[] themes = isBn
          ? new String[]{"লাইট মোড", "সেপিয়া মোড", "ডার্ক মোড", "মিডনাইট মোড"}
          : new String[]{"Light", "Sepia", "Dark", "Midnight"};
      String[] themeKeys = {"LIGHT", "SEPIA", "DARK", "MIDNIGHT"};

      new MaterialAlertDialogBuilder(context)
          .setTitle(isBn ? "রিডিং থিম পরিবর্তন করুন" : "Change Reading Theme")
          .setItems(themes, (d, which) -> {
            currentThemeName[0] = themeKeys[which];
            prefs.edit().putString("reader_theme", currentThemeName[0]).apply();
            applyTheme.run();
          })
          .show();
    });

    // Jump to Chapter / Table of Contents
    binding.btnReaderJumpToPage.setOnClickListener(v -> {
      String[] titlesArray = chapterTitles.toArray(new String[0]);
      new MaterialAlertDialogBuilder(context)
          .setTitle(isBn ? "সূচিপত্র" : "Table of Contents")
          .setItems(titlesArray, (d, which) -> {
            currentChapterIndex[0] = which;
            updateChapterDisplay.run();
          })
          .show();
    });

    binding.btnCloseReader.setOnClickListener(v -> dialog.dismiss());
    dialog.setOnDismissListener(d -> {
      if (pdfAdapterRef[0] != null) {
        try {
          pdfAdapterRef[0].close();
          if (rendererRef[0] != null) rendererRef[0].close();
          if (pfdRef[0] != null) pfdRef[0].close();
        } catch (Exception ignored) {}
      }
    });
    dialog.show();
  }

  private static List<String> generateChapterTitles(IslamicBookEntity book) {
    List<String> list = new ArrayList<>();
    list.add("অধ্যায় ১: ভূমিকা ও গ্রন্থ পরিচিতি");
    list.add("অধ্যায় ২: ইখলাস ও সৎ নিয়তের গুরুত্ব");
    list.add("অধ্যায় ৩: তাওবাহ ও ক্ষমা প্রার্থনা");
    list.add("অধ্যায় ৪: ধৈর্য ও শোকর গুজারী");
    list.add("অধ্যায় ৫: সত্যবাদিতা ও বিশ্বস্ততা");
    list.add("অধ্যায় ৬: মুরাকাবা ও আল্লাহভীতি");
    list.add("অধ্যায় ৭: তাকওয়া ও আত্মশুদ্ধি");
    list.add("অধ্যায় ৮: তাওয়াক্কুল ও আল্লাহর উপর ভরসা");
    list.add("অধ্যায় ৯: সৎ কাজের আদেশ ও অসৎ কাজে নিষেধ");
    list.add("অধ্যায় ১০: আখলাক ও সুন্দর চরিত্র গঠন");
    return list;
  }

  private static List<String> generateChapterContents(IslamicBookEntity book) {
    List<String> list = new ArrayList<>();
    list.add("বিসমিল্লাহির রাহমানির রাহিম।\n\nসমস্ত প্রশংসা মহান আল্লাহ রাব্বুল আলামিনের জন্য, যিনি সমগ্র জাহানের প্রতিপালক। দরূদ ও সালাম বর্ষিত হোক মানবতার মুক্তির দূত, সর্বশ্রেষ্ঠ নবী ও রাসুল হযরত মুহাম্মদ (সা.)-এর উপর এবং তাঁর পরিবারবর্গ ও সাহাবায়ে কেরামগণের উপর।\n\nবইটির নাম: "+ book.getTitle() + "\nলেখক: "+ book.getAuthor() + "\nবিষয়শ্রেণী: "+ book.getCategory() + "\n\nএই গ্রন্থটি মুসলিম উম্মাহর আত্মিক পরিশুদ্ধি, আমল ও ঈমানের মজবুতির জন্য একটি অমূল্য রত্ন। পাঠকদের সুবিধার্থে প্রতিটি অধ্যায়ে সহীহ দলীল, কোরআনের আয়াত ও নির্ভরযোগ্য হাদিসের সমাহার ঘটানো হয়েছে।");
    list.add("অধ্যায় ২: ইখলাস ও নিয়তের বিশুদ্ধতা\n\nআমিরুল মু'মিনীন হযরত উমর ইবনুল খাত্তাব (রা.) থেকে বর্ণিত, তিনি বলেন— আমি রাসুলুল্লাহ (সা.)-কে বলতে শুনেছি:\n\n«إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى»\n\n'নিশ্চয়ই সমস্ত আমল নিয়তের উপর নির্ভরশীল। প্রত্যেক ব্যক্তি তাই পাবে, যার সে নিয়ত করবে।' (সহীহ বুখারী: ১, সহীহ মুসলিম: ১৯০৭)\n\nব্যাখ্যা ও শিক্ষা:\n১. যে কোনো নেক আমল আল্লাহর দরবারে কবুল হওয়ার পূর্বশর্ত হলো পূর্ণাঙ্গ ইখলাস ও একমাত্র আল্লাহর সন্তুষ্টির নিয়ত।\n২. লোকদেখানো আমল বা রিয়া শিরকে আসগারের অন্তর্ভুক্ত এবং তা নেক আমল ধ্বংস করে দেয়।");
    list.add("অধ্যায় ৩: তাওবাহ ও ইস্তিগফার\n\nআল্লাহ তাআলা পবিত্র কুরআনে ইরশাদ করেন:\n\n«وَتُوبُوا إِلَى اللَّهِ جَمِيعًا أَيُّهَ الْمُؤْمِنُونَ لَعَلَّكُمْ تُفْلِحُونَ»\n\n'হে মুমিনগণ! তোমরা সবাই আল্লাহর নিকট তাওবা করো, যাতে তোমরা সফলকাম হতে পারো।' (সূরা আন-নূর: ৩১)\n\nরাসুলুল্লাহ (সা.) ইরশাদ করেছেন:\n'আল্লাহর শপথ! আমি দিনে সত্তর বারেরও বেশি আল্লাহর কাছে ক্ষমা চাই ও তাওবা করি।' (সহীহ বুখারী)\n\nতাওবার শর্তাবলী:\n১. পাপ কাজ অবিলম্বে ত্যাগ করা।\n২. কৃত অপরাধের জন্য অন্তরে গভীর অনুশোচনা সৃষ্টি করা।\n৩. ভবিষ্যতে কখনো এই গুনাহে লিপ্ত না হওয়ার দৃঢ় সংকল্প করা।\n৪. বান্দার হক নষ্ট করে থাকলে তা ফিরিয়ে দিয়ে ক্ষমা নেওয়া।");
    list.add("অধ্যায় ৪: ধৈর্য (সবর) ও কৃতজ্ঞতা (শোকর)\n\nআল্লাহ তাআলা ইরশাদ করেন:\n«يَا أَيُّهَا الَّذِينَ آمَنُوا اسْتَعِينُوا بِالصَّبْرِ وَالصَّلَاةِ ۚ إِنَّ اللَّهَ مَعَ الصَّابِرِينَ»\n\n'হে ঈমানদারগণ! তোমরা ধৈর্য ও সালাতের মাধ্যমে সাহায্য প্রার্থনা করো। নিশ্চয়ই আল্লাহ ধৈর্যশীলদের সাথে আছেন।' (সূরা আল-বাকারা: ১৫৩)\n\nরাসুলুল্লাহ (সা.) ইরশাদ করেন:\n'মুমিনের অবস্থা সত্যিই বিস্ময়কর! তার প্রতিটি বিষয়ই তার জন্য কল্যাণকর। যদি সে আনন্দদায়ক কিছু লাভ করে এবং শোকর আদায় করে, তবে তা তার জন্য কল্যাণকর হয়। আর যদি কোনো বিপদে পতিত হয় এবং ধৈর্য ধারণ করে, তবে তাও তার জন্য কল্যাণকর হয়।' (সহীহ মুসলিম: ২৯৯৯)");
    list.add("অধ্যায় ৫: সত্যবাদিতা ও আমানতদারী\n\nআল্লাহ তাআলা ইরশاد করেন:\n«يَا أَيُّهَا الَّذِينَ آمَنُوا اتَّقُوا اللَّهَ وَكُونُوا مَعَ الصَّادِقِينَ»\n\n'হে ঈমানদারগণ! তোমরা আল্লাহকে ভয় করো এবং সত্যবাদীদের সঙ্গী হও।' (সূরা আত-তাওবাহ: ১১৯)\n\nনবী করিম (সা.) বলেন:\n'তোমরা সত্যকে আঁকড়ে ধরো। কেননা সত্যবাদিতা পুণ্যের পথ দেখায়, আর পুণ্য জান্নাতের দিকে নিয়ে যায়। একজন ব্যক্তি সত্য বলতে বলতে আল্লাহর দরবারে সিদ্দিক হিসেবে লিপিবদ্ধ হয়।' (সহীহ বুখারী ও মুসলিম)");
    list.add("অধ্যায় ৬: মুরাকাবা ও আল্লাহভীতি\n\nরাসুলুল্লাহ (সা.) জিবরীল (আ.)-এর প্রশ্নের উত্তরে ইহসান সম্পর্কে বলেন:\n\n«أَنْ تَعْبُدَ اللَّهَ كَأَنَّكَ تَرَاهُ، فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ»\n\n'ইহসান হলো তুমি এমনভাবে আল্লাহর ইবাদত করবে যেন তুমি তাঁকে দেখছ। আর যদি তুমি তাঁকে দেখতে না পাও, তবে জেনে রাখবে তিনি তোমাকে অবশ্যই দেখছেন।' (সহীহ বুখারী: ৫০, সহীহ মুসলিম: ৮)");
    list.add("অধ্যায় ৭: তাকওয়া ও অন্তরের পরিশুদ্ধি\n\nরাসুলুল্লাহ (সা.) তাঁর বক্ষ মোবারকের দিকে ইঙ্গিত করে তিনবার বলেছিলেন:\n«التَّقْوَى هَاهُنَا»\n'তাকওয়া এখানে, তাকওয়া অন্তরে।' (সহীহ মুসলিম)\n\nঅন্তর পবিত্র না হলে কোনো আমলের নূর প্রকাশ পায় না। সকল পাপাচার থেকে বেঁচে থেকে আল্লাহর সন্তুষ্টি লাভের একনিষ্ঠ প্রচেষ্টাই হলো তাকওয়া।");
    list.add("অধ্যায় ৮: তাওয়াক্কুল ও আল্লাহর উপর অগাধ ভরসা\n\nপবিত্র কুরআনে আল্লাহ সুবহানাহু ওয়া তাআলা ইরশাদ করেন:\n«وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ»\n\n'যে ব্যক্তি আল্লাহর উপর ভরসা করে, তার জন্য তিনিই যথেষ্ট।' (সূরা আত-তালাক: ৩)\n\nতাওয়াক্কুলের অর্থ হলো যথাসাধ্য প্রচেষ্টা ও প্রস্তুতি গ্রহণের পর ফলাফলের দায়িত্ব মহান আল্লাহর উপর সমর্পণ করা।");
    list.add("অধ্যায় ৯: সৎ কাজের আদেশ ও অসৎ কাজে নিষেধ\n\nপবিত্র কুরআনে ইরশাদ হয়েছে:\n«كُنتُمْ خَيْرَ أُمَّةٍ أُخْرِجَتْ لِلنَّاسِ تَأْمُرُونَ بِالْمَعْرُوفِ وَتَنْهَوْنَ عَنِ الْمُنكَرِ»\n\n'তোমরাই হলে সর্বোত্তম উম্মত, যাদের মানবজাতির কল্যাণের জন্য আবির্ভূত করা হয়েছে। তোমরা সৎ কাজের আদেশ দাও এবং অন্যায় কাজ থেকে নিষেধ করো।' (সূরা আলে ইমরান: ১১০)");
    list.add("অধ্যায় ১০: উত্তম চরিত্র ও আখলাক\n\nরাসুলুল্লাহ (সা.) ইরশাদ করেন:\n«إِنَّ مِنْ أَحَبِّكُمْ إِلَيَّ وَأَقْرَبِكُمْ مِنِّي مَجْلِسًا يَوْمَ الْقِيَامَةِ أَحَاسِنَكُمْ أَخْلاقًا»\n\n'কিয়ামতের দিন তোমাদের মধ্যে আমার কাছে সবচেয়ে প্রিয় ও সবচেয়ে নিকটবর্তী হবে সেই ব্যক্তি, যার চরিত্র সর্বোত্তম।' (সুনানে তিরমিযী: ২০১০)");
    return list;
  }
}