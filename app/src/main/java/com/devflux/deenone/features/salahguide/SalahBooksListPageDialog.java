package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemSalahBookCardBinding;
import com.devflux.deenone.databinding.PageSalahBooksListBinding;
import com.devflux.deenone.features.salahguide.data.SalahBooksRepository;
import com.devflux.deenone.features.salahguide.model.SalahBookModel;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * Production-Ready Dialog for: বই সমূহ (নামাজ শিক্ষা বই / Salah Learning Books).
 * 100% matching screenshot:
 * - নামাযের মাসায়েল (মুহাম্মদ ইকবাল কিলানী)
 * - নামাযের গুরুত্ব (আব্দুল হামীদ মাদানী)
 * - নামাযের সময়সূচী (মুহাম্মদ বিন সালেহ আল উসাইমীন)
 * - নবীজীর প্রিয় নামায (আব্দুল্লাহ নজীব)
 */
public class SalahBooksListPageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahBooksListBinding binding = PageSalahBooksListBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Title (Verbatim: "বই সমূহ" / "Islamic Books")
        binding.tvSalahBooksTitle.setText(isBn ? "বই সমূহ" : "Islamic Books");

        // Back Button with Spring Touch
        binding.btnBackSalahBooks.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBooks);

        // Load Books List
        List<SalahBookModel> books = SalahBooksRepository.getBooks();

        SalahBooksAdapter adapter = new SalahBooksAdapter(activity, books, isBn);
        binding.rvSalahBooks.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvSalahBooks.setAdapter(adapter);

        dialog.show();
    }

    private static class SalahBooksAdapter extends RecyclerView.Adapter<SalahBooksAdapter.ViewHolder> {
        private final Activity activity;
        private final List<SalahBookModel> books;
        private final boolean isBn;

        public SalahBooksAdapter(Activity activity, List<SalahBookModel> books, boolean isBn) {
            this.activity = activity;
            this.books = books;
            this.isBn = isBn;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahBookCardBinding binding = ItemSalahBookCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SalahBookModel book = books.get(position);

            holder.binding.tvBookTitle.setText(book.getTitle(isBn));
            holder.binding.tvBookAuthor.setText(book.getAuthor(isBn));
            holder.binding.ivBookCover.setImageResource(book.getCoverDrawableRes());

            holder.binding.cardBookItem.setOnClickListener(v -> {
                SalahVerticalPdfReaderDialog.show(activity, book);
            });
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemSalahBookCardBinding binding;

            public ViewHolder(@NonNull ItemSalahBookCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
