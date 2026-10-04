package com.devflux.deenone.features.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.BottomSheetBookDetailsBinding;
import com.devflux.deenone.features.books.adapter.BookListAdapter;
import com.devflux.deenone.features.books.download.BookDownloadManager;
import com.devflux.deenone.features.books.pdf.PdfBookReaderDialog;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.File;

public class BookDetailsDialog {

  public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, @NonNull BooksViewModel booksVm) {
    boolean isBn = LocaleManager.isBengali(context);

    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    BottomSheetBookDetailsBinding binding = BottomSheetBookDetailsBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    BookDownloadManager downloadManager = BookDownloadManager.getInstance(context);

    // Static Headers Localization
    binding.tvDetailsHeaderTitle.setText(isBn ? "বইয়ের বিস্তারিত তথ্য" : "Book Details");
    binding.tvDetailsTotalPagesLabel.setText(isBn ? "মোট পৃষ্ঠা" : "Total Pages");
    binding.tvDetailsFileSizeLabel.setText(isBn ? "সাইজ" : "Size");
    binding.tvDetailsReadingPercentLabel.setText(isBn ? "পড়ার অগ্রগতি" : "Progress");
    binding.tvDetailsDescriptionHeading.setText(isBn ? "বইয়ের পরিচিতি ও বিষয়বস্তু" : "Synopsis & Overview");
    binding.tvDetailsPublisherHeading.setText(isBn ? "প্রকাশনা ও লাইসেন্স তথ্য" : "Publication & License");

    // Bind Information with safe fallbacks
    String title = (book.getTitle() != null && !book.getTitle().trim().isEmpty()) ? book.getTitle() : (isBn ? "ইসলামিক কিতাব" : "Islamic Book");
    String author = (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) ? book.getAuthor() : (isBn ? "প্রখ্যাত ইসলামিক গবেষক ও ওলামায়ে কেরাম" : "Renowned Islamic Scholars");
    String description = (book.getDescription() != null && !book.getDescription().trim().isEmpty()) ? book.getDescription() : (isBn ? "এই প্রামাণ্য কিতাবটিতে পবিত্র কুরআন ও সহিহ সুন্নাহর বিশুদ্ধ জ্ঞান এবং জীবনঘনিষ্ঠ সমাধান সন্নিবেশিত রয়েছে।" : "This authentic work contains pure knowledge of the Holy Quran and Sunnah.");
    String publisher = (book.getPublisher() != null && !book.getPublisher().trim().isEmpty()) ? book.getPublisher() : (isBn ? "ইসলামিক ফাউন্ডেশন" : "Islamic Foundation");
    String source = (book.getVerifiedSource() != null && !book.getVerifiedSource().trim().isEmpty()) ? book.getVerifiedSource() : (isBn ? "ইসলামহাউজ ও আন্তর্জাতিক ইসলামিক গবেষণা একাডেমি" : "IslamHouse & Research Academy");
    String license = (book.getLicenseInfo() != null && !book.getLicenseInfo().trim().isEmpty()) ? book.getLicenseInfo() : (isBn ? "পাবলিক ডোমেইন / উন্মুক্ত ইসলামিক প্রকাশনা" : "Public Domain / Open Distribution");
    String format = (book.getFormat() != null && !book.getFormat().trim().isEmpty()) ? book.getFormat() : "PDF";

    binding.tvDetailsBookTitle.setText(title);
    binding.tvDetailsBookAuthor.setText((isBn ? "লেখক: " : "Author: ") + author);
    binding.tvDetailsCategoryBadge.setText(BookListAdapter.getCategoryDisplayName(book.getCategory(), isBn));
    
    String langText = "bn".equalsIgnoreCase(book.getLanguage()) ? (isBn ? "বাংলা" : "Bengali")
        : "en".equalsIgnoreCase(book.getLanguage()) ? "English" : "العربية";
    binding.tvDetailsLanguageBadge.setText(langText);
    binding.tvDetailsVerifiedBadge.setText(source);

    binding.tvDetailsTotalPages.setText(isBn ? BengaliNumberUtil.toBengali(book.getTotalPages()) : String.valueOf(book.getTotalPages()));
    binding.tvDetailsFileSize.setText(book.getFileSize() != null ? book.getFileSize() : (isBn ? "ডিজিটাল সংস্করণ" : "Digital"));
    binding.tvDetailsReadingPercent.setText(isBn ? (BengaliNumberUtil.toBengali(book.getReadingPercentage()) + "%") : (book.getReadingPercentage() + "%"));

    binding.tvDetailsDescription.setText(description);
    binding.tvDetailsPublisher.setText((isBn ? "মূল প্রকাশনা: " : "Publisher: ") + publisher);
    binding.tvDetailsSource.setText((isBn ? "যাচাইকৃত উৎস: " : "Verified Source: ") + source);
    binding.tvDetailsLicense.setText((isBn ? "লাইসেন্স: " : "License: ") + license);

    binding.ivDetailsCoverIcon.setImageResource(BookListAdapter.getCategoryIconRes(book.getCategory()));
    binding.tvDetailsCoverFormatBadge.setText(format + " • E-BOOK");

    // Favorite Icon
    final boolean[] isFav = {book.isFavorite()};
    binding.ivDetailsFavoriteIcon.setImageResource(isFav[0] ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
    binding.ivDetailsFavoriteIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
        isFav[0] ? android.graphics.Color.parseColor("#EF4444") : ContextCompat.getColor(context, R.color.text_secondary)));

    binding.btnDetailsFavorite.setOnClickListener(v -> {
      isFav[0] = !isFav[0];
      binding.ivDetailsFavoriteIcon.setImageResource(isFav[0] ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
      binding.ivDetailsFavoriteIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
          isFav[0] ? android.graphics.Color.parseColor("#EF4444") : ContextCompat.getColor(context, R.color.text_secondary)));
      booksVm.toggleFavorite(book);
    });

    // Share Action
    binding.btnDetailsShare.setOnClickListener(v -> {
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      String shareMsg = isBn
          ? (book.getTitle() + "\nলেখক: " + book.getAuthor() + "\nউৎস: " + book.getVerifiedSource() + "\n\nদ্বীনওয়ান (DeenOne) অ্যাপে সম্পূর্ণ বইটি অফলাইনে পড়ুন: https://play.google.com/store/apps/details?id=com.devflux.deenone")
          : (book.getTitle() + "\nAuthor: " + book.getAuthor() + "\nSource: " + book.getVerifiedSource() + "\n\nRead offline on DeenOne: https://play.google.com/store/apps/details?id=com.devflux.deenone");
      shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
      context.startActivity(Intent.createChooser(shareIntent, isBn ? "ইসলামিক বই শেয়ার করুন" : "Share Islamic Book"));
    });

    // Direct Instant Read Button (100% in DB)
    binding.layoutDetailsDownloadProgress.setVisibility(View.GONE);
    binding.btnDetailsDownloadBook.setVisibility(View.GONE);
    binding.btnDetailsDeleteBook.setVisibility(View.GONE);

    binding.btnDetailsReadBook.setEnabled(true);
    if (book.getLastReadPage() > 0) {
      String pageStr = isBn ? BengaliNumberUtil.toBengali(book.getLastReadPage()) : String.valueOf(book.getLastReadPage());
      binding.btnDetailsReadBook.setText(isBn ? ("পড়া অব্যাহত রাখুন (অধ্যায় " + pageStr + ")") : ("Continue Reading (Chapter " + pageStr + ")"));
    } else {
      binding.btnDetailsReadBook.setText(isBn ? "বইটি পড়ুন" : "Read Book");
    }

    binding.btnDetailsReadBook.setOnClickListener(v -> {
      com.devflux.deenone.features.books.pdf.RichBookReaderDialog.show(context, book, null);
    });

    binding.btnCloseBookDetails.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  private static void startDownload(Context context, IslamicBookEntity book, BooksViewModel booksVm,
                                    BottomSheetBookDetailsBinding binding, Runnable updateActionButtons, boolean isBn) {
    binding.layoutDetailsDownloadProgress.setVisibility(View.VISIBLE);
    binding.btnDetailsDownloadBook.setEnabled(false);
    binding.btnDetailsDownloadBook.setText(isBn ? "ডাউনলোড চলছে..." : "Downloading...");

    booksVm.downloadBook(book, new BookDownloadManager.DownloadProgressListener() {
      @Override
      public void onProgress(String bookId, int percent, long bytesRead, long totalBytes) {
        book.setDownloadProgress(percent);
        binding.tvDetailsDownloadPercent.setText(isBn ? (BengaliNumberUtil.toBengali(percent) + "%") : (percent + "%"));
        binding.progressBarDetailsDownload.setProgress(percent);
      }

      @Override
      public void onSuccess(String bookId, File localFile) {
        book.setDownloaded(true);
        book.setLocalFilePath(localFile.getAbsolutePath());
        book.setDownloadProgress(100);
        updateActionButtons.run();
        PdfBookReaderDialog.show(context, book, localFile);
      }

      @Override
      public void onError(String bookId, String errorMessage) {
        updateActionButtons.run();
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
