package com.devflux.deenone.features.books.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.features.books.BookChapterIndexDialog;
import com.devflux.deenone.features.books.BookDownloadConfirmDialog;
import com.devflux.deenone.features.books.BookDownloadProgressDialog;
import com.devflux.deenone.features.books.RichBookReaderDialog;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookCategoryExpandableAdapter extends RecyclerView.Adapter<BookCategoryExpandableAdapter.CategoryViewHolder> {

    public static class CategoryGroup {
        public String key;
        public String nameBn;
        public String nameEn;
        public List<IslamicBookEntity> books = new ArrayList<>();
        public boolean isExpanded = false;

        public CategoryGroup(String key, String nameBn, String nameEn) {
            this.key = key;
            this.nameBn = nameBn;
            this.nameEn = nameEn;
        }
    }

    public interface OnBookItemActionListener {
        void onBookSelected(IslamicBookEntity book);
        void onBookDownloadCompleted(IslamicBookEntity book);
    }

    private final Context context;
    private final List<CategoryGroup> categoryGroups = new ArrayList<>();
    private final List<IslamicBookEntity> allBooks = new ArrayList<>();
    private final OnBookItemActionListener actionListener;
    private final boolean isBn;
    private String currentSearchQuery = "";
    private boolean onlyDownloadedFilter = false;

    public BookCategoryExpandableAdapter(Context context, OnBookItemActionListener actionListener) {
        this.context = context;
        this.actionListener = actionListener;
        this.isBn = LocaleManager.isBengali(context);
    }

    public void setAllBooks(List<IslamicBookEntity> books) {
        this.allBooks.clear();
        if (books != null) {
            this.allBooks.addAll(books);
        }
        rebuildCategories();
    }

    public void setSearchQuery(String query) {
        this.currentSearchQuery = query != null ? query.trim().toLowerCase() : "";
        rebuildCategories();
    }

    public void setOnlyDownloaded(boolean onlyDownloaded) {
        this.onlyDownloadedFilter = onlyDownloaded;
        rebuildCategories();
    }

    public boolean isOnlyDownloaded() {
        return onlyDownloadedFilter;
    }

    public int getDownloadedCount() {
        int count = 0;
        for (IslamicBookEntity b : allBooks) {
            if (b.isDownloaded()) count++;
        }
        return count;
    }

    private void rebuildCategories() {
        categoryGroups.clear();

        // 21 Canonical Categories from HadithBD & DeenOne Engine
        String[][] catDefs = new String[][]{
                {"aqeedah", "আকিদা [তাওহীদ]", "Aqeedah [Tawheed]"},
                {"salah", "সালাত [নামায]", "Salah [Prayer]"},
                {"zakat", "যাকাত", "Zakat"},
                {"sawm", "সাওম [রোযা]", "Sawm [Fasting]"},
                {"hajj", "হজ্জ [হজ্ব]", "Hajj & Umrah"},
                {"dua", "দুয়া ও জিকির", "Dua & Zikr"},
                {"fatwa", "ফতোয়া [মাসাআলা মাসায়েল]", "Fatwa & Masail"},
                {"bidah", "শিরক,কুফর ও বিদআত", "Shirk, Kufr & Bid'ah"},
                {"daily_life", "দৈনন্দিন জীবন/বিবিধ", "Daily Life / Misc"},
                {"seerah", "জীবনী ও ইতিহাস", "Biography & History"},
                {"firqa", "ফিরকা ও দল পরিচিতি", "Sects & Groups"},
                {"quran_hadith", "কোরআন ও হাদিস", "Quran & Hadith"},
                {"usul", "উসূলের গ্রন্থাবলী", "Usul & Methodology"},
                {"ruqyah", "যাদু টোনা ও ঝাড় ফুঁক", "Ruqyah & Evil Eye"},
                {"dawah", "দাওয়াত ও তাবলীগ", "Dawah & Calling"},
                {"akhira", "কবর, কিয়ামত ও আখিরাত", "Hereafter & Grave"},
                {"anti_deviation", "নাস্তিকতা ও মতবাদ পর্যালোচনা", "Anti-Deviation & Beliefs"},
                {"women", "মহিলা অঙ্গন", "Women in Islam"},
                {"sahaba", "সাহাবা, তাবেঈ, তাবে-তাবেঈন", "Companions & Followers"},
                {"allah_names", "আল্লাহ্‌র নাম ও গুণাবলী", "Names of Allah"},
                {"halal_haram", "হারাম ও হালাল", "Halal & Haram"}
        };

        Map<String, CategoryGroup> groupMap = new LinkedHashMap<>();
        for (String[] def : catDefs) {
            groupMap.put(def[0], new CategoryGroup(def[0], def[1], def[2]));
        }

        // Default expand first category if no search query
        boolean first = true;

        for (IslamicBookEntity b : allBooks) {
            if (onlyDownloadedFilter && !b.isDownloaded()) {
                continue;
            }

            if (!currentSearchQuery.isEmpty()) {
                String t = (b.getTitle() != null ? b.getTitle() : "").toLowerCase();
                String a = (b.getAuthor() != null ? b.getAuthor() : "").toLowerCase();
                if (!t.contains(currentSearchQuery) && !a.contains(currentSearchQuery)) {
                    continue;
                }
            }

            String catKey = b.getCategory() != null ? b.getCategory().toLowerCase() : "daily_life";
            if ("salat".equals(catKey)) catKey = "salah";
            else if ("shirk_bidah".equals(catKey) || "shirk".equals(catKey)) catKey = "bidah";
            else if ("biography".equals(catKey)) catKey = "seerah";
            else if ("sects".equals(catKey)) catKey = "firqa";
            else if ("jadu_ruqyah".equals(catKey)) catKey = "ruqyah";
            else if ("comparative_religion".equals(catKey)) catKey = "anti_deviation";
            else if ("family".equals(catKey) || "qurbani_eid".equals(catKey)) catKey = "daily_life";

            CategoryGroup grp = groupMap.get(catKey);
            if (grp == null) {
                grp = groupMap.get("daily_life");
            }
            if (grp != null) {
                grp.books.add(b);
            }
        }

        for (CategoryGroup grp : groupMap.values()) {
            if (!grp.books.isEmpty()) {
                if (!currentSearchQuery.isEmpty()) {
                    grp.isExpanded = true;
                } else if (first) {
                    grp.isExpanded = true; // First category expanded by default (Screenshot 3)
                    first = false;
                }
                categoryGroups.add(grp);
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_category_accordion, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryGroup group = categoryGroups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return categoryGroups.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final View cardHeader;
        private final TextView tvTitle;
        private final TextView tvSubtitle;
        private final ImageView ivChevron;
        private final RecyclerView rvBooks;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardHeader = itemView.findViewById(R.id.cardCategoryHeaderRoot);
            tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
            tvSubtitle = itemView.findViewById(R.id.tvCategorySubtitle);
            ivChevron = itemView.findViewById(R.id.ivCategoryChevron);
            rvBooks = itemView.findViewById(R.id.rvCategoryBooks);

            // STRICT Rule 7: NO touch animation on CardView
            cardHeader.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < categoryGroups.size()) {
                    CategoryGroup grp = categoryGroups.get(pos);
                    grp.isExpanded = !grp.isExpanded;
                    notifyItemChanged(pos);
                }
            });
        }

        public void bind(CategoryGroup group) {
            String catName = isBn ? group.nameBn : group.nameEn;
            tvTitle.setText(catName);

            int totalBooks = group.books.size();
            int downloadedBooks = 0;
            for (IslamicBookEntity b : group.books) {
                if (b.isDownloaded()) downloadedBooks++;
            }

            String countStr = isBn
                    ? (BengaliNumberUtil.toBengali(totalBooks) + " টি বই • " + BengaliNumberUtil.toBengali(downloadedBooks) + " ডাউনলোড")
                    : (totalBooks + " Books • " + downloadedBooks + " Downloaded");
            tvSubtitle.setText(countStr);

            if (group.isExpanded) {
                ivChevron.setImageResource(R.drawable.ic_chevron_down);
                ivChevron.setRotation(180f);
                rvBooks.setVisibility(View.VISIBLE);

                rvBooks.setLayoutManager(new LinearLayoutManager(context));
                BookItemAccordionAdapter itemAdapter = new BookItemAccordionAdapter(group.books);
                rvBooks.setAdapter(itemAdapter);
            } else {
                ivChevron.setImageResource(R.drawable.ic_chevron_down);
                ivChevron.setRotation(0f);
                rvBooks.setVisibility(View.GONE);
            }
        }
    }

    // Inner Adapter for Book Cards under Category Accordion
    private class BookItemAccordionAdapter extends RecyclerView.Adapter<BookItemAccordionAdapter.BookItemViewHolder> {

        private final List<IslamicBookEntity> books;

        BookItemAccordionAdapter(List<IslamicBookEntity> books) {
            this.books = books;
        }

        @NonNull
        @Override
        public BookItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_book_accordion_card, parent, false);
            return new BookItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull BookItemViewHolder holder, int position) {
            IslamicBookEntity book = books.get(position);
            holder.bind(book);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        class BookItemViewHolder extends RecyclerView.ViewHolder {
            private final View cardRoot;
            private final View stripActive;
            private final ImageView ivIcon;
            private final TextView tvTitle;
            private final TextView tvAuthor;
            private final TextView tvChaptersCount;
            private final ImageView ivMenu;

            BookItemViewHolder(@NonNull View itemView) {
                super(itemView);
                cardRoot = itemView.findViewById(R.id.cardBookItemRoot);
                stripActive = itemView.findViewById(R.id.viewDownloadActiveStrip);
                ivIcon = itemView.findViewById(R.id.ivBookItemIcon);
                tvTitle = itemView.findViewById(R.id.tvBookItemTitle);
                tvAuthor = itemView.findViewById(R.id.tvBookItemAuthor);
                tvChaptersCount = itemView.findViewById(R.id.tvBookItemChaptersCount);
                ivMenu = itemView.findViewById(R.id.ivBookItemMenu);

                // Touch animation strictly on the menu button (Rule 7)
                TouchAnimationUtil.attachTouchSpring(ivMenu);

                // STRICT Rule 7: NO touch animation on CardView!
                cardRoot.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION || pos >= books.size()) return;
                    IslamicBookEntity book = books.get(pos);

                    if (!book.isDownloaded()) {
                        // Show Download Confirmation Modal (Screenshot 1)
                        BookDownloadConfirmDialog.show(context, book, confirmedBook -> {
                            // Launch Download Progress Modal (Screenshot 2)
                            BookDownloadProgressDialog.show(context, confirmedBook, downloadedBook -> {
                                notifyItemChanged(pos);
                                if (actionListener != null) {
                                    actionListener.onBookDownloadCompleted(downloadedBook);
                                }
                                // Open Chapter Index / Reader directly
                                BookChapterIndexDialog.show(context, downloadedBook);
                            });
                        });
                    } else {
                        // Open Chapter Index (Screenshot 4)
                        BookChapterIndexDialog.show(context, book);
                    }
                });

                ivMenu.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION || pos >= books.size()) return;
                    IslamicBookEntity book = books.get(pos);
                    showBookMenu(v, book, pos);
                });
            }

            void bind(IslamicBookEntity book) {
                tvTitle.setText(book.getTitle() != null ? book.getTitle() : "");
                tvAuthor.setText(book.getAuthor() != null ? book.getAuthor() : "");

                int chCount = book.getChaptersCount() > 0 ? book.getChaptersCount() : 1;
                String chText = isBn
                        ? (BengaliNumberUtil.toBengali(chCount) + " টি পরিচ্ছেদ")
                        : (chCount + " Chapters");
                tvChaptersCount.setText(chText);

                if (book.isDownloaded()) {
                    stripActive.setVisibility(View.VISIBLE);
                    stripActive.setBackgroundColor(Color.parseColor("#0D5C55"));
                    ivIcon.setColorFilter(Color.parseColor("#0D5C55"));
                } else {
                    stripActive.setVisibility(View.INVISIBLE);
                    ivIcon.setColorFilter(Color.parseColor("#5C736A"));
                }
            }

            private void showBookMenu(View anchor, IslamicBookEntity book, int pos) {
                android.widget.PopupMenu popup = new android.widget.PopupMenu(context, anchor);
                popup.getMenu().add(0, 1, 0, isBn ? "অধ্যায়সমূহ দেখুন" : "View Chapters");
                popup.getMenu().add(0, 2, 1, isBn ? "বই পড়ুন" : "Read Book");
                popup.getMenu().add(0, 3, 2, isBn ? (book.isFavorite() ? "পছন্দ তালিকা থেকে সরান" : "পছন্দ তালিকায় যুক্ত করুন") : "Toggle Favorite");
                popup.getMenu().add(0, 4, 3, isBn ? "শেয়ার করুন" : "Share");

                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case 1:
                            BookChapterIndexDialog.show(context, book);
                            return true;
                        case 2:
                            RichBookReaderDialog.show(context, book, 0);
                            return true;
                        case 3:
                            book.setFavorite(!book.isFavorite());
                            com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
                                com.devflux.deenone.data.local.AppDatabase.getInstance(context)
                                        .islamicBookDao()
                                        .updateFavorite(book.getId(), book.isFavorite());
                            });
                            notifyItemChanged(pos);
                            return true;
                        case 4:
                            android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, book.getTitle());
                            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                    book.getTitle() + "\n" + (book.getAuthor() != null ? ("লেখক: " + book.getAuthor()) : ""));
                            context.startActivity(android.content.Intent.createChooser(shareIntent, isBn ? "ইসলামিক বই শেয়ার" : "Share Book"));
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
            }
        }
    }
}
