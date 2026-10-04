package com.devflux.deenone.features.books.pdf;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.BottomSheetPdfReaderBinding;
import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.features.books.download.BookDownloadManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.io.File;

public class PdfBookReaderDialog {

  public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, @NonNull File pdfFile) {
    boolean isBn = LocaleManager.isBengali(context);

    File fileToOpen = pdfFile;
    if (fileToOpen == null || !fileToOpen.exists() || fileToOpen.length() < 100 || !BookDownloadManager.isValidPdfFile(fileToOpen)) {
      File booksDir = new File(context.getFilesDir(), "islamic_books");
      if (!booksDir.exists()) booksDir.mkdirs();
      File targetFile = new File(booksDir, "book_" + book.getId() + ".pdf");
      if (!BookDownloadManager.isValidPdfFile(targetFile)) {
        BookDownloadManager.generateAuthenticLocalPdf(context, book, targetFile);
      }
      fileToOpen = targetFile;
    }

    try {
      ParcelFileDescriptor pfd = ParcelFileDescriptor.open(fileToOpen, ParcelFileDescriptor.MODE_READ_ONLY);
      PdfRenderer renderer = new PdfRenderer(pfd);
      int totalPages = renderer.getPageCount();

      FullScreenPageDialog dialog = new FullScreenPageDialog(context);
      BottomSheetPdfReaderBinding binding = BottomSheetPdfReaderBinding.inflate(LayoutInflater.from(context));
      dialog.setContentView(binding.getRoot());

      binding.tvReaderBookTitle.setText(book.getTitle());
      String sourceText = (book.getVerifiedSource() != null && !book.getVerifiedSource().isEmpty()) ? (" • " + book.getVerifiedSource()) : "";
      binding.tvReaderBookAuthor.setText(book.getAuthor() + sourceText);

      TouchAnimationUtil.attachTouchSpring(binding.btnClosePdfReader);
      TouchAnimationUtil.attachTouchSpring(binding.btnReaderJumpPage);
      TouchAnimationUtil.attachTouchSpring(binding.tvCornerPageIndicator);

      // High-Performance Continuous 60 FPS Vertical RecyclerView
      LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
      binding.rvPdfPages.setLayoutManager(layoutManager);
      binding.rvPdfPages.setHasFixedSize(true);
      binding.rvPdfPages.setItemViewCacheSize(6);
      binding.rvPdfPages.setItemAnimator(null);

      PdfPageAdapter adapter = new PdfPageAdapter(context, renderer);
      binding.rvPdfPages.setAdapter(adapter);

      // Resume from last read page if valid
      int initialPage = 0;
      if (book.getLastReadPage() > 0 && book.getLastReadPage() <= totalPages) {
        initialPage = book.getLastReadPage() - 1;
      }
      if (initialPage > 0) {
        layoutManager.scrollToPositionWithOffset(initialPage, 0);
      }

      final int[] currentPage = {initialPage};

      Runnable updateIndicator = () -> {
        int cur = currentPage[0] + 1;
        if (isBn) {
          binding.tvCornerPageIndicator.setText("পৃষ্ঠা " + BengaliNumberUtil.toBengali(cur) + " / " + BengaliNumberUtil.toBengali(totalPages));
        } else {
          binding.tvCornerPageIndicator.setText("Page " + cur + " / " + totalPages);
        }
        IslamicBookRepository.getInstance(context).updateReadingProgress(book.getId(), cur, totalPages);
      };

      updateIndicator.run();

      binding.rvPdfPages.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          int firstPos = layoutManager.findFirstVisibleItemPosition();
          if (firstPos >= 0 && firstPos < totalPages && firstPos != currentPage[0]) {
            currentPage[0] = firstPos;
            updateIndicator.run();
          }
        }
      });

      // Jump to Page Dialog
      Runnable showJumpDialog = () -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String titlePrompt = isBn
            ? ("পৃষ্ঠা নম্বরে যান (" + BengaliNumberUtil.toBengali(1) + " - " + BengaliNumberUtil.toBengali(totalPages) + ")")
            : ("Go to Page (1 - " + totalPages + ")");
        builder.setTitle(titlePrompt);

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(isBn ? "পৃষ্ঠা নম্বর লিখুন" : "Enter page number");
        input.setPadding(48, 24, 48, 24);
        builder.setView(input);

        builder.setPositiveButton(isBn ? "যান" : "Go", (d, which) -> {
          try {
            int targetPage = Integer.parseInt(input.getText().toString().trim());
            if (targetPage >= 1 && targetPage <= totalPages) {
              layoutManager.scrollToPositionWithOffset(targetPage - 1, 0);
            } else {
              Toast.makeText(context, isBn ? "সঠিক পৃষ্ঠা নম্বর প্রদান করুন" : "Please enter a valid page number", Toast.LENGTH_SHORT).show();
            }
          } catch (Exception ignored) {}
        });
        builder.setNegativeButton(isBn ? "বাতিল" : "Cancel", null);
        builder.show();
      };

      binding.btnReaderJumpPage.setOnClickListener(v -> showJumpDialog.run());
      binding.tvCornerPageIndicator.setOnClickListener(v -> showJumpDialog.run());
      binding.btnClosePdfReader.setOnClickListener(v -> dialog.dismiss());

      dialog.setOnDismissListener(d -> {
        try {
          adapter.close();
          renderer.close();
          pfd.close();
        } catch (Exception ignored) {}
      });

      dialog.show();

    } catch (Exception e) {
      new androidx.appcompat.app.AlertDialog.Builder(context)
          .setTitle(isBn ? "বইটি খুলতে সমস্যা হয়েছে" : "Could not open book")
          .setMessage(isBn ? "পিডিএফ ফাইলটি ক্ষতিগ্রস্ত বা অসম্পূর্ণ হতে পারে। আপনি কি ফাইলটি নতুন করে আবার ডাউনলোড করতে চান?" : "The PDF file may be damaged or incomplete. Would you like to re-download it?")
          .setPositiveButton(isBn ? "পুনরায় ডাউনলোড করুন" : "Re-download", (dialogInterface, which) -> {
            BookDownloadManager.getInstance(context).deleteDownloadedBook(book);
            book.setDownloaded(false);
            book.setLocalFilePath(null);
            book.setDownloadProgress(0);
          })
          .setNegativeButton(isBn ? "ফিরে যান" : "Cancel", null)
          .show();
    }
  }
}
