package com.devflux.deenone.features.books.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * Service managing native Android PdfRenderer sessions, viewport dimensions, and page rendering.
 */
public class BookReaderService {

    private static final String TAG = "BookReaderService";
    private static volatile BookReaderService instance;

    private BookReaderService() {}

    public static synchronized BookReaderService getInstance() {
        if (instance == null) {
            instance = new BookReaderService();
        }
        return instance;
    }

    /**
     * Opens a PdfRenderer session for a local book file.
     */
    public PdfSession openSession(@NonNull File pdfFile) throws IOException {
        if (!pdfFile.exists() || pdfFile.length() == 0) {
            throw new IOException("পিডিএফ ফাইল পাওয়া যায়নি বা ফাইলটি খালি");
        }

        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        return new PdfSession(fileDescriptor, renderer);
    }

    public static class PdfSession {
        private final ParcelFileDescriptor pfd;
        private final PdfRenderer renderer;

        public PdfSession(ParcelFileDescriptor pfd, PdfRenderer renderer) {
            this.pfd = pfd;
            this.renderer = renderer;
        }

        public PdfRenderer getRenderer() {
            return renderer;
        }

        public int getPageCount() {
            return renderer != null ? renderer.getPageCount() : 0;
        }

        public void close() {
            try {
                if (renderer != null) renderer.close();
                if (pfd != null) pfd.close();
            } catch (Exception e) {
                Log.w(TAG, "Error closing PdfSession: " + e.getMessage());
            }
        }
    }
}
