package com.devflux.deenone.features.prophets.ui;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageProphetsStoriesBinding;
import com.devflux.deenone.features.prophets.adapter.ProphetsAdapter;
import com.devflux.deenone.features.prophets.data.ProphetsContentRepository;
import com.devflux.deenone.features.prophets.model.ProphetMenuItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class ProphetsStoriesPageDialog {

    public static void show(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageProphetsStoriesBinding binding = PageProphetsStoriesBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title (Verbatim: "নবীদের জীবনী" / "Stories of the Prophets")
        binding.tvProphetsPageTitle.setText(isBn ? "নবীদের জীবনী" : "Stories of the Prophets");

        // Back Button
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseProphetsPage);
        binding.btnCloseProphetsPage.setOnClickListener(v -> dialog.dismiss());

        // Language adaptations for Top Cards & Search
        if (!isBn) {
            binding.tvTopCard1Title.setText("25 Prophets");
            binding.tvTopCard1Subtitle.setText("Mentioned in the Quran");
            binding.tvTopCard2Title.setText("Ulu-l-Azm");
            binding.tvTopCard2Subtitle.setText("5 Greatest Messengers");
            binding.etSearchProphet.setHint("Search Prophet's name...");
        }

        // Setup RecyclerView with Menu List
        List<ProphetMenuItem> menuItems = ProphetsContentRepository.getMenuItems();
        ProphetsAdapter adapter = new ProphetsAdapter(menuItems, isBn, item -> {
            if (item.getType() == ProphetMenuItem.Type.OVERVIEW) {
                ProphetsOverviewPageDialog.show(activity);
            } else if (item.getProphetStoryItem() != null) {
                ProphetTopicsPageDialog.show(activity, item.getProphetStoryItem());
            }
        });

        binding.rvProphetsList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvProphetsList.setAdapter(adapter);

        // Instant Search TextWatcher
        binding.etSearchProphet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Top Cards Interactions (Strict Rule 7: NO touch spring on cards)
        binding.cardTopTotalProphets.setOnClickListener(v -> {
            binding.etSearchProphet.setText("");
            adapter.filter("");
        });

        binding.cardTopUlulAzm.setOnClickListener(v -> {
            binding.etSearchProphet.setText(isBn ? "উলুল আযম" : "Ulu-l-Azm");
        });

        dialog.show();
    }
}
