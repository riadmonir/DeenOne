package com.devflux.deenone.features.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.DialogBookChapterIndexBinding;
import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.features.books.model.BookChapter;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class BookChapterIndexDialog {

    public static void show(@NonNull Context context, @NonNull IslamicBookEntity book) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        DialogBookChapterIndexBinding binding =
                DialogBookChapterIndexBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        // Header
        binding.tvChapterIndexBookTitle.setText(book.getTitle() != null ? book.getTitle() : "");
        String sub = book.getDescription() != null && !book.getDescription().isEmpty()
                ? book.getDescription()
                : (isBn ? "প্রশ্ন এবং তাঁর উত্তরসমূহ" : "Questions & Answers");
        binding.tvChapterIndexSubtitle.setText(sub);

        TouchAnimationUtil.attachTouchSpring(binding.btnCloseChapterIndex);
        binding.btnCloseChapterIndex.setOnClickListener(v -> dialog.dismiss());

        // Load Chapters
        List<BookChapter> chapters = IslamicBookRepository.getChaptersForBook(context, book);

        ChapterAdapter adapter = new ChapterAdapter(context, chapters, (chapter, position) -> {
            // Open Reader at this chapter
            RichBookReaderDialog.show(context, book, position);
        });

        binding.rvBookChaptersIndex.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBookChaptersIndex.setAdapter(adapter);

        dialog.show();
    }

    private interface OnChapterClickListener {
        void onChapterClick(BookChapter chapter, int position);
    }

    private static class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

        private final Context context;
        private final List<BookChapter> chapters;
        private final OnChapterClickListener listener;
        private final boolean isBn;

        ChapterAdapter(Context context, List<BookChapter> chapters, OnChapterClickListener listener) {
            this.context = context;
            this.chapters = chapters != null ? chapters : new ArrayList<>();
            this.listener = listener;
            this.isBn = LocaleManager.isBengali(context);
        }

        @NonNull
        @Override
        public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_book_chapter_card, parent, false);
            return new ChapterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
            BookChapter chapter = chapters.get(position);
            holder.bind(chapter, position);
        }

        @Override
        public int getItemCount() {
            return chapters.size();
        }

        class ChapterViewHolder extends RecyclerView.ViewHolder {
            private final TextView tvNumberBadge;
            private final TextView tvTitle;
            private final View rootCard;

            ChapterViewHolder(@NonNull View itemView) {
                super(itemView);
                rootCard = itemView.findViewById(R.id.cardChapterItemRoot);
                tvNumberBadge = itemView.findViewById(R.id.tvChapterNumberBadge);
                tvTitle = itemView.findViewById(R.id.tvChapterItemTitle);

                // STRICT Rule 7: NO touch animation on CardView!
                rootCard.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null) {
                        listener.onChapterClick(chapters.get(pos), pos);
                    }
                });
            }

            void bind(BookChapter chapter, int position) {
                String numStr = isBn ? BengaliNumberUtil.toBengali(position + 1) : String.valueOf(position + 1);
                tvNumberBadge.setText(numStr);
                tvTitle.setText(chapter.getTitle() != null ? chapter.getTitle() : "");
            }
        }
    }
}
