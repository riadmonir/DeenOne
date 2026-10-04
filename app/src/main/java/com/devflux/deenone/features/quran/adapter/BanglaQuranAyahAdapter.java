package com.devflux.deenone.features.quran.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.BanglaQuranAyahItem;
import com.devflux.deenone.core.quran.BanglaQuranManager;
import com.devflux.deenone.core.quran.BanglaQuranTranslator;
import com.devflux.deenone.databinding.ItemBanglaQuranAyahBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class BanglaQuranAyahAdapter extends RecyclerView.Adapter<BanglaQuranAyahAdapter.AyahViewHolder> {

    public interface OnAyahActionListener {
        void onPlayAudio(BanglaQuranAyahItem ayah, int position);
        void onBookmarkToggle(BanglaQuranAyahItem ayah, int position);
    }

    private List<BanglaQuranAyahItem> ayahs = new ArrayList<>();
    private BanglaQuranTranslator currentTranslator = BanglaQuranTranslator.MUHIUDDIN_KHAN;
    private boolean isArabicShown = true;
    private float arabicFontScale = 1.0f;
    private int banglaFontSizeSp = 15;
    private final OnAyahActionListener actionListener;

    public BanglaQuranAyahAdapter(OnAyahActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setAyahs(List<BanglaQuranAyahItem> list) {
        this.ayahs = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setTranslator(BanglaQuranTranslator translator) {
        if (translator != null && this.currentTranslator != translator) {
            this.currentTranslator = translator;
            notifyDataSetChanged();
        }
    }

    public void setArabicShown(boolean shown) {
        if (this.isArabicShown != shown) {
            this.isArabicShown = shown;
            notifyDataSetChanged();
        }
    }

    public void setArabicFontScale(float scale) {
        this.arabicFontScale = scale;
        notifyDataSetChanged();
    }

    public void setBanglaFontSizeSp(int sp) {
        this.banglaFontSizeSp = sp;
        notifyDataSetChanged();
    }

    public List<BanglaQuranAyahItem> getAyahs() {
        return ayahs;
    }

    @NonNull
    @Override
    public AyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBanglaQuranAyahBinding binding = ItemBanglaQuranAyahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AyahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        holder.bind(ayahs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return ayahs.size();
    }

    class AyahViewHolder extends RecyclerView.ViewHolder {

        private final ItemBanglaQuranAyahBinding binding;

        AyahViewHolder(ItemBanglaQuranAyahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Touch spring animation STRICTLY on action buttons (Rule 7)
            TouchAnimationUtil.attachTouchSpring(binding.btnPlayAyahAudio);
            TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkAyah);
            TouchAnimationUtil.attachTouchSpring(binding.btnCopyAyah);
            TouchAnimationUtil.attachTouchSpring(binding.btnShareAyah);
        }

        void bind(BanglaQuranAyahItem ayah, int position) {
            Context context = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(context);

            // 1. Ayah Number & Juz Badges
            String ayahNumStr = isBn ? BengaliNumberUtil.toBengali(ayah.getAyahNumber()) : String.valueOf(ayah.getAyahNumber());
            binding.tvAyahNumberBadge.setText(isBn ? ("আয়াত " + ayahNumStr) : ("Ayah " + ayahNumStr));

            String juzNumStr = isBn ? BengaliNumberUtil.toBengali(ayah.getJuzNumber()) : String.valueOf(ayah.getJuzNumber());
            binding.tvJuzBadge.setText(isBn ? ("পারা " + juzNumStr) : ("Para " + juzNumStr));

            // 2. Arabic Text & Font Scaling
            if (isArabicShown && ayah.getTextArabic() != null && !ayah.getTextArabic().trim().isEmpty()) {
                binding.tvAyahArabicText.setVisibility(View.VISIBLE);
                binding.tvAyahArabicText.setText(ayah.getTextArabic());
                binding.tvAyahArabicText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f * arabicFontScale);
            } else {
                binding.tvAyahArabicText.setVisibility(View.GONE);
            }

            // 3. Translator Badge & Bangla Translation
            binding.tvTranslatorBadge.setText(isBn
                    ? ("অনুবাদ: " + currentTranslator.fullNameBn)
                    : ("Translation: " + currentTranslator.fullNameEn));

            String translation = ayah.getTranslation(currentTranslator);
            binding.tvAyahBanglaTranslation.setText(translation);
            binding.tvAyahBanglaTranslation.setTextSize(TypedValue.COMPLEX_UNIT_SP, banglaFontSizeSp);

            // 4. Bookmark Icon State
            if (ayah.isBookmarked()) {
                binding.btnBookmarkAyah.setImageResource(R.drawable.ic_bookmark_filled);
                binding.btnBookmarkAyah.setColorFilter(ContextCompat.getColor(context, R.color.accent_gold));
            } else {
                binding.btnBookmarkAyah.setImageResource(R.drawable.ic_bookmark);
                binding.btnBookmarkAyah.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            }

            // 5. Actions
            binding.btnPlayAyahAudio.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onPlayAudio(ayah, position);
                }
            });

            binding.btnBookmarkAyah.setOnClickListener(v -> {
                boolean newState = BanglaQuranManager.getInstance(context).toggleAyahBookmark(ayah.getSurahNumber(), ayah.getAyahNumber());
                ayah.setBookmarked(newState);
                notifyItemChanged(position);
                if (actionListener != null) {
                    actionListener.onBookmarkToggle(ayah, position);
                }
            });

            binding.btnCopyAyah.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    StringBuilder sb = new StringBuilder();
                    if (isArabicShown && ayah.getTextArabic() != null && !ayah.getTextArabic().isEmpty()) {
                        sb.append(ayah.getTextArabic()).append("\n\n");
                    }
                    sb.append(translation).append("\n\n");
                    sb.append("— [সূরা ").append(ayah.getSurahNumber()).append(" : আয়াত ").append(ayah.getAyahNumber()).append("]");
                    ClipData clip = ClipData.newPlainText("Quran Ayah", sb.toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, isBn ? "আয়াত কপি করা হয়েছে" : "Ayah copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            binding.btnShareAyah.setOnClickListener(v -> {
                StringBuilder sb = new StringBuilder();
                if (isArabicShown && ayah.getTextArabic() != null && !ayah.getTextArabic().isEmpty()) {
                    sb.append(ayah.getTextArabic()).append("\n\n");
                }
                sb.append(translation).append("\n\n");
                sb.append("— [সূরা ").append(ayah.getSurahNumber()).append(" : আয়াত ").append(ayah.getAyahNumber()).append("]\n");
                sb.append("DeenOne App");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                context.startActivity(Intent.createChooser(shareIntent, isBn ? "শেয়ার করুন" : "Share Ayah"));
            });
        }
    }
}
