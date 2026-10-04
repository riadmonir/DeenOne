package com.devflux.deenone.features.books.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.features.books.download.BookDownloadManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface OnBookActionListener {
    void onItemClick(IslamicBookEntity book, int position);
    void onDownloadClick(IslamicBookEntity book, int position);
    void onReadClick(IslamicBookEntity book, int position);
    void onFavoriteClick(IslamicBookEntity book, int position);
  }

  private static final int VIEW_TYPE_LIST = 0;
  private static final int VIEW_TYPE_GRID = 1;

  private final List<IslamicBookEntity> items = new ArrayList<>();
  private final OnBookActionListener listener;
  private final BookDownloadManager downloadManager;
  private boolean isGridView = false;

  public BookListAdapter(Context context, OnBookActionListener listener) {
    this.listener = listener;
    this.downloadManager = BookDownloadManager.getInstance(context);
  }

  public void setItems(List<IslamicBookEntity> list) {
    items.clear();
    if (list != null) {
      items.addAll(list);
    }
    notifyDataSetChanged();
  }

  public void setGridView(boolean gridView) {
    if (this.isGridView != gridView) {
      this.isGridView = gridView;
      notifyDataSetChanged();
    }
  }

  public boolean isGridView() {
    return isGridView;
  }

  @Override
  public int getItemViewType(int position) {
    return isGridView ? VIEW_TYPE_GRID : VIEW_TYPE_LIST;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_GRID) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card_grid, parent, false);
      return new GridBookViewHolder(view, downloadManager, listener);
    } else {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_card, parent, false);
      return new ListBookViewHolder(view, downloadManager, listener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    IslamicBookEntity book = items.get(position);
    if (holder instanceof GridBookViewHolder gridHolder) {
      gridHolder.bind(book);
    } else if (holder instanceof ListBookViewHolder listHolder) {
      listHolder.bind(book);
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  // List ViewHolder
  public static class ListBookViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivBookIcon;
    private final TextView tvBookLangBadge;
    private final TextView tvBookCategoryBadge;
    private final ImageView ivBookFavoriteBtn;
    private final TextView tvBookTitle;
    private final TextView tvBookAuthor;
    private final TextView tvBookDescription;
    private final TextView tvBookPagesAndSize;
    private final TextView tvBookVerifiedSource;
    private final LinearLayout layoutDownloadProgress;
    private final TextView tvDownloadStatusText;
    private final TextView tvDownloadPercent;
    private final ProgressBar progressBarBookDownload;
    private final MaterialButton btnBookAction;
    private final BookDownloadManager downloadManager;
    private final OnBookActionListener listener;

    public ListBookViewHolder(@NonNull View itemView, BookDownloadManager downloadManager, OnBookActionListener listener) {
      super(itemView);
      this.downloadManager = downloadManager;
      this.listener = listener;
      ivBookIcon = itemView.findViewById(R.id.ivBookIcon);
      tvBookLangBadge = itemView.findViewById(R.id.tvBookLangBadge);
      tvBookCategoryBadge = itemView.findViewById(R.id.tvBookCategoryBadge);
      ivBookFavoriteBtn = itemView.findViewById(R.id.ivBookFavoriteBtn);
      tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
      tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
      tvBookDescription = itemView.findViewById(R.id.tvBookDescription);
      tvBookPagesAndSize = itemView.findViewById(R.id.tvBookPagesAndSize);
      tvBookVerifiedSource = itemView.findViewById(R.id.tvBookVerifiedSource);
      layoutDownloadProgress = itemView.findViewById(R.id.layoutDownloadProgress);
      tvDownloadStatusText = itemView.findViewById(R.id.tvDownloadStatusText);
      tvDownloadPercent = itemView.findViewById(R.id.tvDownloadPercent);
      progressBarBookDownload = itemView.findViewById(R.id.progressBarBookDownload);
      btnBookAction = itemView.findViewById(R.id.btnBookAction);
    }

    public void bind(IslamicBookEntity book) {
      Context context = itemView.getContext();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      String author = (book.getAuthor() != null && !book.getAuthor().trim().isEmpty())
          ? book.getAuthor() : (isBn ? "প্রখ্যাত ইসলামিক গবেষক ও ওলামায়ে কেরাম" : "Renowned Islamic Scholar");
      String description = (book.getDescription() != null && !book.getDescription().trim().isEmpty())
          ? book.getDescription() : (isBn ? "এই প্রামাণ্য কিতাবটিতে পবিত্র কুরআন ও সহিহ সুন্নাহর বিশুদ্ধ শিক্ষা সংকলিত রয়েছে।" : "Authentic Islamic book containing knowledge from the Holy Quran and Sunnah.");
      String source = (book.getVerifiedSource() != null && !book.getVerifiedSource().trim().isEmpty())
          ? book.getVerifiedSource() : (isBn ? "ইসলামহাউজ ও ইসলামিক ফাউন্ডেশন" : "IslamHouse & Islamic Foundation");
      String fileSize = (book.getFileSize() != null && !book.getFileSize().trim().isEmpty())
          ? book.getFileSize() : (isBn ? "ডিজিটাল সংস্করণ" : "Digital Edition");

      tvBookTitle.setText(book.getTitle() != null ? book.getTitle() : (isBn ? "ইসলামিক কিতাব" : "Islamic Book"));
      tvBookAuthor.setText((isBn ? "লেখক: " : "Author: ") + author);
      tvBookDescription.setText(description);
      tvBookCategoryBadge.setText(getCategoryDisplayName(book.getCategory(), isBn));
      tvBookPagesAndSize.setText(isBn ? (BengaliNumberUtil.toBengali(book.getTotalPages()) + " পৃষ্ঠা • " + fileSize) : (book.getTotalPages() + " Pages • " + fileSize));
      tvBookVerifiedSource.setText(source);

      // Language Badge
      if ("bn".equalsIgnoreCase(book.getLanguage())) {
        tvBookLangBadge.setText(isBn ? "বাংলা" : "Bangla");
      } else if ("en".equalsIgnoreCase(book.getLanguage())) {
        tvBookLangBadge.setText(isBn ? "ইংরেজি" : "English");
      } else {
        tvBookLangBadge.setText(isBn ? "আরবি" : "Arabic");
      }

      // Category Icon
      if (ivBookIcon != null) {
        ivBookIcon.setImageResource(getCategoryIconRes(book.getCategory()));
      }

      // Favorite
      if (ivBookFavoriteBtn != null) {
        ivBookFavoriteBtn.setImageResource(book.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        ivBookFavoriteBtn.setImageTintList(android.content.res.ColorStateList.valueOf(
            book.isFavorite() ? android.graphics.Color.parseColor("#EF4444") : ContextCompat.getColor(context, R.color.text_secondary)));
        ivBookFavoriteBtn.setOnClickListener(v -> {
          if (listener != null) {
            listener.onFavoriteClick(book, getBindingAdapterPosition());
          }
        });
      }

      // Card Click -> Details Page
      itemView.setOnClickListener(v -> {
        if (listener != null) {
          listener.onItemClick(book, getBindingAdapterPosition());
        }
      });

      // Direct Instant Reading (100% in DB)
      layoutDownloadProgress.setVisibility(View.GONE);
      btnBookAction.setEnabled(true);
      if (book.getLastReadPage() > 0) {
        btnBookAction.setText(isBn ? ("পড়া অব্যাহত রাখুন (অধ্যায় " + BengaliNumberUtil.toBengali(book.getLastReadPage()) + ")") : ("Continue (Chapter " + book.getLastReadPage() + ")"));
      } else {
        btnBookAction.setText(isBn ? "বইটি পড়ুন" : "Read Book");
      }
      btnBookAction.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.accent_mint));
      btnBookAction.setTextColor(ContextCompat.getColor(context, R.color.white));

      btnBookAction.setOnClickListener(v -> {
        if (listener != null) {
          listener.onReadClick(book, getBindingAdapterPosition());
        }
      });
    }
  }

  // Grid ViewHolder
  public static class GridBookViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivGridBookIcon;
    private final TextView tvGridBookLangBadge;
    private final TextView tvGridBookCategoryBadge;
    private final ImageView ivGridBookFavoriteBtn;
    private final TextView tvGridBookTitle;
    private final TextView tvGridBookAuthor;
    private final TextView tvGridBookPagesAndSize;
    private final MaterialButton btnGridBookAction;
    private final BookDownloadManager downloadManager;
    private final OnBookActionListener listener;

    public GridBookViewHolder(@NonNull View itemView, BookDownloadManager downloadManager, OnBookActionListener listener) {
      super(itemView);
      this.downloadManager = downloadManager;
      this.listener = listener;

      ivGridBookIcon = itemView.findViewById(R.id.ivGridBookIcon);
      tvGridBookLangBadge = itemView.findViewById(R.id.tvGridBookLangBadge);
      tvGridBookCategoryBadge = itemView.findViewById(R.id.tvGridBookCategoryBadge);
      ivGridBookFavoriteBtn = itemView.findViewById(R.id.ivGridBookFavoriteBtn);
      tvGridBookTitle = itemView.findViewById(R.id.tvGridBookTitle);
      tvGridBookAuthor = itemView.findViewById(R.id.tvGridBookAuthor);
      tvGridBookPagesAndSize = itemView.findViewById(R.id.tvGridBookPagesAndSize);
      btnGridBookAction = itemView.findViewById(R.id.btnGridBookAction);
    }

    public void bind(IslamicBookEntity book) {
      Context context = itemView.getContext();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      String author = (book.getAuthor() != null && !book.getAuthor().trim().isEmpty())
          ? book.getAuthor() : (isBn ? "প্রখ্যাত ইসলামিক গবেষক" : "Islamic Scholar");

      tvGridBookTitle.setText(book.getTitle() != null ? book.getTitle() : (isBn ? "ইসলামিক কিতাব" : "Islamic Book"));
      tvGridBookAuthor.setText(author);
      tvGridBookCategoryBadge.setText(getCategoryDisplayName(book.getCategory(), isBn));
      tvGridBookPagesAndSize.setText(isBn ? (BengaliNumberUtil.toBengali(book.getTotalPages()) + " পৃষ্ঠা") : (book.getTotalPages() + " Pages"));
      
      if (ivGridBookIcon != null) {
        ivGridBookIcon.setImageResource(getCategoryIconRes(book.getCategory()));
      }

      tvGridBookLangBadge.setText(isBn ? ("bn".equalsIgnoreCase(book.getLanguage()) ? "বাংলা" : "en".equalsIgnoreCase(book.getLanguage()) ? "ইংরেজি" : "আরবি") : ("bn".equalsIgnoreCase(book.getLanguage()) ? "Bangla" : "en".equalsIgnoreCase(book.getLanguage()) ? "English" : "Arabic"));

      if (ivGridBookFavoriteBtn != null) {
        ivGridBookFavoriteBtn.setImageResource(book.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        ivGridBookFavoriteBtn.setImageTintList(android.content.res.ColorStateList.valueOf(
            book.isFavorite() ? android.graphics.Color.parseColor("#EF4444") : ContextCompat.getColor(context, R.color.text_secondary)));
        ivGridBookFavoriteBtn.setOnClickListener(v -> {
          if (listener != null) listener.onFavoriteClick(book, getBindingAdapterPosition());
        });
      }

      itemView.setOnClickListener(v -> {
        if (listener != null) listener.onItemClick(book, getBindingAdapterPosition());
      });

      // Direct Instant Reading (100% in DB)
      btnGridBookAction.setEnabled(true);
      btnGridBookAction.setText(book.getLastReadPage() > 0 ? (isBn ? ("অধ্যায় " + BengaliNumberUtil.toBengali(book.getLastReadPage())) : ("Ch " + book.getLastReadPage())) : (isBn ? "পড়ুন" : "Read"));
      btnGridBookAction.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.accent_mint));
      btnGridBookAction.setTextColor(ContextCompat.getColor(context, R.color.white));
      btnGridBookAction.setOnClickListener(v -> {
        if (listener != null) listener.onReadClick(book, getBindingAdapterPosition());
      });
    }
  }

  public static String getCategoryDisplayName(String cat) {
    return getCategoryDisplayName(cat, true);
  }

  public static String getCategoryDisplayName(String cat, boolean isBn) {
    if (cat == null) return isBn ? "ইসলামিক কিতাব" : "Islamic Book";
    if (!isBn) {
      return switch (cat.toLowerCase().trim()) {
        case "aqeedah" -> "Aqeedah";
        case "salah" -> "Salah";
        case "zakat" -> "Zakat & Charity";
        case "sawm" -> "Sawm & Ramadan";
        case "hajj" -> "Hajj & Umrah";
        case "dua" -> "Dua & Zikr";
        case "fatwa" -> "Fatwa & Masail";
        case "bidah" -> "Shirk & Bid'ah";
        case "family" -> "Family & Life";
        case "seerah" -> "Seerah & Biography";
        case "firqa" -> "Sects & Groups";
        case "quran_hadith" -> "Quran & Hadith";
        case "qurbani_eid" -> "Qurbani & Eid";
        case "tawhid_waseela" -> "Tawhid & Waseela";
        case "quran" -> "Quran";
        case "tafsir" -> "Tafsir";
        case "hadith" -> "Hadith";
        case "fiqh" -> "Fiqh";
        default -> cat;
      };
    }
    return switch (cat.toLowerCase().trim()) {
      case "aqeedah" -> "আকীদা";
      case "salah" -> "সালাত";
      case "zakat" -> "যাকাত ও সাদাকাহ";
      case "sawm" -> "সাওম ও রমজান";
      case "hajj" -> "হজ ও উমরাহ";
      case "dua" -> "দো'আ ও যিকির";
      case "fatwa" -> "ফতোয়া ও মাসআলা";
      case "bidah" -> "শিরক ও বিদআত";
      case "family" -> "পারিবারিক জীবন";
      case "seerah" -> "সীরাত ও জীবনী";
      case "firqa" -> "ফিরকা ও দল";
      case "quran_hadith" -> "কুরআন ও হাদিস";
      case "qurbani_eid" -> "কুরবানী ও ঈদ";
      case "tawhid_waseela" -> "তাওহীদ ও উসীলা";
      case "quran" -> "কুরআন";
      case "tafsir" -> "তাফসীর";
      case "hadith" -> "হাদিস";
      case "fiqh" -> "ফিকহ";
      default -> cat;
    };
  }


  public static int getCategoryIconRes(String cat) {
    if (cat == null) return R.drawable.ic_feat_book;
    return switch (cat) {
      case "Quran" -> R.drawable.ic_feat_book;
      case "Tafsir" -> R.drawable.ic_feat_ayah;
      case "Hadith" -> R.drawable.ic_feat_hadith;
      case "Fiqh" -> R.drawable.ic_scale_justice;
      case "Aqeedah" -> R.drawable.ic_moon;
      case "Seerah" -> R.drawable.ic_feat_mosque;
      case "Islamic History" -> R.drawable.ic_history;
      case "Dua & Azkar" -> R.drawable.ic_feat_dua;
      case "Salah" -> R.drawable.ic_clock;
      case "Islamic Ethics" -> R.drawable.ic_favorite_filled;
      case "Family & Marriage" -> R.drawable.ic_family;
      case "Children" -> R.drawable.ic_person;
      default -> R.drawable.ic_feat_book;
    };
  }
}
