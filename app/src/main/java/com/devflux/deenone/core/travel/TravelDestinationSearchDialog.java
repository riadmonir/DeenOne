package com.devflux.deenone.core.travel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetTravelDestinationSearchBinding;

import java.util.List;

public class TravelDestinationSearchDialog {

    public interface OnDestinationChosenCallback {
        void onDestinationChosen(TravelLocationSearchManager.TravelLocationItem item);
    }

    public static void show(@NonNull Context context,
                            double originLat, double originLng,
                            boolean isSelectingOrigin,
                            @NonNull OnDestinationChosenCallback callback) {

        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        BottomSheetTravelDestinationSearchBinding binding =
                BottomSheetTravelDestinationSearchBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        // Header Localization
        if (isSelectingOrigin) {
            binding.tvDestSearchHeaderTitle.setText(isBn ? "যাত্রার শুরুর স্থান (উৎস) নির্বাচন" : "Select Departure Location (Origin)");
        } else {
            binding.tvDestSearchHeaderTitle.setText(isBn ? "ভ্রমণ গন্তব্য অনুসন্ধান ও নির্বাচন" : "Search & Select Travel Destination");
        }
        binding.tvDestSearchHeaderSubtitle.setText(isBn ?
                "বিশ্বের যেকোনো স্থান, শহর বা জেলা ম্যাপ থেকে নির্বাচন করুন" :
                "Select any city, district or holy destination worldwide");

        binding.etTravelDestSearch.setHint(isBn ?
                "শহর, জেলা, দেশ বা ম্যাপের ঠিকানা লিখুন..." :
                "Type city, district, country or location...");

        binding.chipFilterAll.setText(isBn ? "সকল স্থান" : "All Locations");
        binding.chipFilterHoly.setText(isBn ? "পবিত্র স্থান ও উমরাহ" : "Holy Sites & Umrah");
        binding.chipFilterBangladesh.setText(isBn ? "বাংলাদেশের জেলাসমূহ" : "Bangladesh Districts");
        binding.chipFilterInternational.setText(isBn ? "আন্তর্জাতিক শহর" : "International Cities");

        binding.btnCloseDestSearch.setContentDescription(isBn ? "বন্ধ করুন" : "Close");
        binding.btnCloseDestSearch.setOnClickListener(v -> dialog.dismiss());

        // Setup RecyclerView
        TravelLocationSearchAdapter adapter = new TravelLocationSearchAdapter(item -> {
            callback.onDestinationChosen(item);
            dialog.dismiss();
        });
        binding.rvDestSearchResults.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDestSearchResults.setAdapter(adapter);

        // State holder for active category
        final String[] activeCategory = {"ALL"};

        // Initial Load
        List<TravelLocationSearchManager.TravelLocationItem> initialList =
                TravelLocationSearchManager.getInstance().getCuratedCategoryList("ALL", originLat, originLng);
        adapter.setItems(initialList);

        // Search TextWatcher with Debounce
        Handler searchHandler = new Handler(Looper.getMainLooper());
        Runnable searchRunnable = new Runnable() {
            @Override
            public void run() {
                String query = binding.etTravelDestSearch.getText().toString().trim();
                binding.layoutSearchStatus.setVisibility(View.VISIBLE);
                binding.tvSearchStatusText.setText(isBn ? "ম্যাপ থেকে স্থান অনুসন্ধান করা হচ্ছে..." : "Searching location from map...");
                TravelLocationSearchManager.getInstance().searchLocationsAsync(
                        context, query, originLat, originLng, results -> {
                            binding.layoutSearchStatus.setVisibility(View.GONE);
                            adapter.setItems(results);
                        }
                );
            }
        };

        binding.etTravelDestSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btnClearDestSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 350);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearDestSearch.setOnClickListener(v -> {
            binding.etTravelDestSearch.setText("");
            adapter.setItems(TravelLocationSearchManager.getInstance().getCuratedCategoryList(activeCategory[0], originLat, originLng));
        });

        // Filter Chips Click Listeners
        View.OnClickListener filterListener = v -> {
            int id = v.getId();
            resetChipStyles(context, binding);
            if (id == R.id.chipFilterAll) {
                activeCategory[0] = "ALL";
                setChipActive(context, binding.chipFilterAll);
            } else if (id == R.id.chipFilterHoly) {
                activeCategory[0] = "ISLAMIC_HOLY";
                setChipActive(context, binding.chipFilterHoly);
            } else if (id == R.id.chipFilterBangladesh) {
                activeCategory[0] = "BD_DISTRICT";
                setChipActive(context, binding.chipFilterBangladesh);
            } else if (id == R.id.chipFilterInternational) {
                activeCategory[0] = "INTERNATIONAL";
                setChipActive(context, binding.chipFilterInternational);
            }

            binding.etTravelDestSearch.setText("");
            adapter.setItems(TravelLocationSearchManager.getInstance().getCuratedCategoryList(activeCategory[0], originLat, originLng));
        };

        binding.chipFilterAll.setOnClickListener(filterListener);
        binding.chipFilterHoly.setOnClickListener(filterListener);
        binding.chipFilterBangladesh.setOnClickListener(filterListener);
        binding.chipFilterInternational.setOnClickListener(filterListener);

        // GPS Auto Pin Button
        binding.btnDestGpsAutoPin.setOnClickListener(v -> {
            LocationProvider.requestCurrentGpsLocation(context, new LocationProvider.LocationCallback() {
                @Override
                public void onLocationResolved(LocationProvider.Coordinates coordinates) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        String name = coordinates.locationName != null ? coordinates.locationName.replace("", "").trim() : (isBn ? "বর্তমান জিপিএস অবস্থান" : "Current GPS Location");
                        TravelLocationSearchManager.TravelLocationItem gpsItem =
                                new TravelLocationSearchManager.TravelLocationItem(
                                        name, isBn ? "অটো জিপিএস লাইভ অবস্থান" : "Auto GPS Live Location", coordinates.latitude, coordinates.longitude, "Local", "GPS"
                                );
                        callback.onDestinationChosen(gpsItem);
                        dialog.dismiss();
                    });
                }

                @Override
                public void onLocationFailed(String errorReason, LocationProvider.Coordinates fallbackCoordinates) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (fallbackCoordinates != null) {
                            String name = fallbackCoordinates.locationName != null ? fallbackCoordinates.locationName.replace("", "").trim() : (isBn ? "ডিফল্ট অবস্থান" : "Default Location");
                            TravelLocationSearchManager.TravelLocationItem fallbackItem =
                                    new TravelLocationSearchManager.TravelLocationItem(
                                            name, isBn ? "সংরক্ষিত অবস্থান" : "Saved Location", fallbackCoordinates.latitude, fallbackCoordinates.longitude, "Local", "SAVED"
                                    );
                            callback.onDestinationChosen(fallbackItem);
                            dialog.dismiss();
                        }
                    });
                }
            });
        });

        dialog.show();
    }

    private static void resetChipStyles(Context context, BottomSheetTravelDestinationSearchBinding binding) {
        int textSecondary = ContextCompat.getColor(context, R.color.text_primary);

        binding.chipFilterAll.setBackgroundResource(R.drawable.bg_badge_pill);
        binding.chipFilterAll.setTextColor(textSecondary);

        binding.chipFilterHoly.setBackgroundResource(R.drawable.bg_badge_pill);
        binding.chipFilterHoly.setTextColor(textSecondary);

        binding.chipFilterBangladesh.setBackgroundResource(R.drawable.bg_badge_pill);
        binding.chipFilterBangladesh.setTextColor(textSecondary);

        binding.chipFilterInternational.setBackgroundResource(R.drawable.bg_badge_pill);
        binding.chipFilterInternational.setTextColor(textSecondary);
    }

    private static void setChipActive(Context context, android.widget.TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_badge_pill_active);
        chip.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
    }
}
