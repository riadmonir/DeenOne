package com.devflux.deenone.features.mosque;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.repository.MosqueRepository;
import com.devflux.deenone.databinding.PageNearbyMosqueBinding;
import com.devflux.deenone.features.mosque.adapter.NearbyMosqueAdapter;
import com.devflux.deenone.features.mosque.model.MosqueItem;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NearbyMosquePageDialog {

  private static double userLatitude = 23.8103;
  private static double userLongitude = 90.4125;
  private static String userLocationName = "ঢাকা, বাংলাদেশ";
  private static double currentRadiusMeters = 5000.0;
  private static String activeFacilityFilter = "all";
  private static String currentSearchQuery = "";
  private static boolean isWalkingMode = true;

  public static void show(@NonNull Context context) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageNearbyMosqueBinding binding = PageNearbyMosqueBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Dynamic Header and Static labels (Rule 5)
    binding.tvPageTitle.setText(isBn ? "নিকটবর্তী মসজিদ" : "Nearby Mosques");
    binding.btnRefreshLocation.setText(isBn ? "রিফ্রেশ" : "Refresh");
    binding.tvRadiusLabel.setText(isBn ? "অনুসন্ধানের ব্যাসার্ধ:" : "Search Radius:");
    binding.etMosqueSearch.setHint(isBn ? "মসজিদের নাম বা রাস্তা খুঁজুন..." : "Search by mosque name or street...");
    binding.pillRadius1Km.setText(isBn ? "১ কিমি" : "1 km");
    binding.pillRadius3Km.setText(isBn ? "৩ কিমি" : "3 km");
    binding.pillRadius5Km.setText(isBn ? "৫ কিমি" : "5 km");
    binding.pillRadius10Km.setText(isBn ? "১০ কিমি" : "10 km");
    binding.chipFilterAll.setText(isBn ? "সব" : "All");
    binding.chipFilterWudu.setText(isBn ? "ওযুখানা" : "Wudu Area");
    binding.chipFilterWomen.setText(isBn ? "নারী কর্নার" : "Women Area");
    binding.chipFilterJumma.setText(isBn ? "জুমা জামাত" : "Jummah");
    binding.btnTravelModeWalking.setText(isBn ? "হাঁটা" : "Walk");
    binding.btnTravelModeDriving.setText(isBn ? "গাড়ি" : "Drive");
    binding.tvLoadingMosqueMessage.setText(isBn ? "নিকটবর্তী মসজিদের অবস্থান অনুসন্ধান করা হচ্ছে..." : "Searching nearby mosque locations...");
    binding.tvEmptyMosqueMessage.setText(isBn ? "এই ব্যাসার্ধের মধ্যে কোনো মসজিদ পাওয়া যায়নি" : "No mosques found within this radius");
    binding.btnEmptyOpenGoogleMap.setText(isBn ? "গুগল ম্যাপে অনুসন্ধান করুন" : "Search on Google Maps");

    // Spring Animations
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseNearbyMosque);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRefreshGpsTop);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRefreshLocation);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTravelModeWalking);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnTravelModeDriving);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnEmptyOpenGoogleMap);

    // 1. Resolve Device Real GPS / Saved Location
    com.devflux.deenone.core.location.LocationProvider.Coordinates savedCoords =
        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(context);
    if (savedCoords != null) {
      userLatitude = savedCoords.latitude;
      userLongitude = savedCoords.longitude;
      userLocationName = savedCoords.locationName != null ? savedCoords.locationName : (isBn ? "ঢাকা, বাংলাদেশ" : "Dhaka, Bangladesh");
    } else {
      userLocationName = isBn ? "ঢাকা, বাংলাদেশ" : "Dhaka, Bangladesh";
    }
    resolveDeviceGpsLocation(context, binding, isBn);

    // 2. Setup RecyclerView Adapter
    final List<MosqueItem> masterMosqueList = new ArrayList<>();
    final List<MosqueItem> displayedMosqueList = new ArrayList<>();

    NearbyMosqueAdapter adapter = new NearbyMosqueAdapter(displayedMosqueList, new NearbyMosqueAdapter.OnMosqueInteractionListener() {
      @Override
      public void onDirectionsClicked(MosqueItem item) {
        MosqueRepository.getInstance().launchDirections(context, item, isWalkingMode);
      }

      @Override
      public void onShareClicked(MosqueItem item) {
        shareMosqueLocation(context, item);
      }
    });

    binding.rvNearbyMosqueList.setLayoutManager(new LinearLayoutManager(context));
    binding.rvNearbyMosqueList.setAdapter(adapter);

    // Helper to filter and update UI
    Runnable applyFiltersAndUpdateUI = () -> {
      displayedMosqueList.clear();
      for (MosqueItem m : masterMosqueList) {
        if (m.getDistanceMeters() > currentRadiusMeters) continue;

        // Facility filter
        if ("wudu".equals(activeFacilityFilter) && !m.isHasWuduArea()) continue;
        if ("women".equals(activeFacilityFilter) && !m.isHasWomenArea()) continue;

        // Search query
        if (!currentSearchQuery.isEmpty()) {
          String q = currentSearchQuery.toLowerCase();
          boolean matchName = m.getName() != null && m.getName().toLowerCase().contains(q);
          boolean matchAddr = m.getAddress() != null && m.getAddress().toLowerCase().contains(q);
          if (!matchName && !matchAddr) continue;
        }

        displayedMosqueList.add(m);
      }

      // Update header count
      binding.tvMosqueListCountHeader.setText(isBn
          ? ("মসজিদ তালিকা (" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(displayedMosqueList.size()) + "টি)")
          : ("Mosque List (" + displayedMosqueList.size() + ")"));

      // Update Adapter
      adapter.updateList(displayedMosqueList, userLatitude, userLongitude);

      // Update Radar Map
      binding.radarMosqueProximity.setMosques(displayedMosqueList, userLatitude, userLongitude, currentRadiusMeters, (item, index) -> {
        binding.rvNearbyMosqueList.smoothScrollToPosition(index);
      });

      // Empty state handling (Zero dummy data)
      if (displayedMosqueList.isEmpty()) {
        binding.rvNearbyMosqueList.setVisibility(View.GONE);
        binding.layoutMosqueEmpty.setVisibility(View.VISIBLE);
      } else {
        binding.rvNearbyMosqueList.setVisibility(View.VISIBLE);
        binding.layoutMosqueEmpty.setVisibility(View.GONE);
      }
    };

    // 3. Real-Time Online Fetch Function (ZERO DUMMY DATA)
    Runnable fetchLiveMosques = () -> {
      binding.layoutMosqueLoading.setVisibility(View.VISIBLE);
      binding.rvNearbyMosqueList.setVisibility(View.GONE);
      binding.layoutMosqueEmpty.setVisibility(View.GONE);

      MosqueRepository.getInstance().fetchLiveNearbyMosquesAsync(
          userLatitude, userLongitude, activeFacilityFilter, currentSearchQuery,
          mosques -> {
            binding.layoutMosqueLoading.setVisibility(View.GONE);
            masterMosqueList.clear();
            if (mosques != null) {
              masterMosqueList.addAll(mosques);
            }
            applyFiltersAndUpdateUI.run();
          }
      );
    };

    // Trigger initial fetch
    fetchLiveMosques.run();

    // 4. Radius Selector Pills
    TextView[] radiusPills = new TextView[]{
        binding.pillRadius1Km, binding.pillRadius3Km, binding.pillRadius5Km, binding.pillRadius10Km
    };
    double[] radiusValues = new double[]{1000.0, 3000.0, 5000.0, 10000.0};

    int colorInactiveBg = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.bg_card);
    int colorInactiveText = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.text_secondary);
    int colorActiveMint = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.accent_mint);
    int colorActiveText = Color.WHITE;

    for (int i = 0; i < radiusPills.length; i++) {
      final int index = i;
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(radiusPills[i]);
      radiusPills[i].setOnClickListener(v -> {
        for (TextView p : radiusPills) {
          p.setBackgroundTintList(ColorStateList.valueOf(colorInactiveBg));
          p.setTextColor(colorInactiveText);
        }
        radiusPills[index].setBackgroundTintList(ColorStateList.valueOf(colorActiveMint));
        radiusPills[index].setTextColor(colorActiveText);

        currentRadiusMeters = radiusValues[index];
        applyFiltersAndUpdateUI.run();
      });
    }

    // 5. Facility Filter Chips
    TextView[] filterChips = new TextView[]{
        binding.chipFilterAll, binding.chipFilterWudu, binding.chipFilterWomen, binding.chipFilterJumma
    };
    String[] filterKeys = new String[]{"all", "wudu", "women", "jumma"};

    for (int i = 0; i < filterChips.length; i++) {
      final int idx = i;
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(filterChips[i]);
      filterChips[i].setOnClickListener(v -> {
        for (TextView c : filterChips) {
          c.setBackgroundTintList(ColorStateList.valueOf(colorInactiveBg));
          c.setTextColor(colorInactiveText);
        }
        filterChips[idx].setBackgroundTintList(ColorStateList.valueOf(colorActiveMint));
        filterChips[idx].setTextColor(colorActiveText);

        activeFacilityFilter = filterKeys[idx];
        applyFiltersAndUpdateUI.run();
      });
    }

    // 6. Search Input TextWatcher
    binding.etMosqueSearch.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentSearchQuery = s.toString().trim();
        applyFiltersAndUpdateUI.run();
      }
      @Override public void afterTextChanged(Editable s) {}
    });

    // 7. Travel Mode Toggle
    binding.btnTravelModeWalking.setOnClickListener(v -> {
      isWalkingMode = true;
      binding.btnTravelModeWalking.setBackgroundTintList(ColorStateList.valueOf(colorActiveMint));
      binding.btnTravelModeWalking.setTextColor(colorActiveText);
      binding.btnTravelModeDriving.setBackgroundTintList(ColorStateList.valueOf(colorInactiveBg));
      binding.btnTravelModeDriving.setTextColor(colorInactiveText);

      adapter.setTravelMode(true);
    });

    binding.btnTravelModeDriving.setOnClickListener(v -> {
      isWalkingMode = false;
      binding.btnTravelModeDriving.setBackgroundTintList(ColorStateList.valueOf(colorActiveMint));
      binding.btnTravelModeDriving.setTextColor(colorActiveText);
      binding.btnTravelModeWalking.setBackgroundTintList(ColorStateList.valueOf(colorInactiveBg));
      binding.btnTravelModeWalking.setTextColor(colorInactiveText);

      adapter.setTravelMode(false);
    });

    // 8. Refresh Location & Top Buttons
    View.OnClickListener refreshClickListener = v -> {
      resolveDeviceGpsLocation(context, binding, isBn);
      fetchLiveMosques.run();
    };

    binding.btnRefreshLocation.setOnClickListener(refreshClickListener);
    binding.btnRefreshGpsTop.setOnClickListener(refreshClickListener);

    binding.btnEmptyOpenGoogleMap.setOnClickListener(v -> {
      MosqueRepository.getInstance().launchMapSearch(context);
    });

    binding.btnCloseNearbyMosque.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private static void resolveDeviceGpsLocation(Context context, PageNearbyMosqueBinding binding, boolean isBn) {
    try {
      LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      if (lm != null) {
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
          loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc != null) {
          userLatitude = loc.getLatitude();
          userLongitude = loc.getLongitude();
        }
      }
    } catch (Exception ignored) {
    }

    binding.tvCurrentLocationName.setText(userLocationName);
    binding.tvCurrentCoordinates.setText(String.format(Locale.US, "GPS: %.4f°, %.4f°", userLatitude, userLongitude));

    // Asynchronous background geocoding to prevent UI lag (Rule 1 & Rule 6)
    java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
      try {
        Geocoder geocoder = new Geocoder(context, isBn ? new Locale("bn", "BD") : Locale.ENGLISH);
        List<Address> addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
        if (addresses != null && !addresses.isEmpty()) {
          Address addr = addresses.get(0);
          String locality = addr.getLocality() != null ? addr.getLocality() : addr.getSubAdminArea();
          String country = addr.getCountryName() != null ? addr.getCountryName() : "";
          final String resolved = (locality != null ? locality + ", " : "") + country;
          binding.tvCurrentLocationName.post(() -> {
            userLocationName = resolved;
            binding.tvCurrentLocationName.setText(userLocationName);
          });
        }
      } catch (Exception ignored) {
      }
    });
  }

  private static void shareMosqueLocation(Context context, MosqueItem item) {
    try {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      String text = item.getName() + "\n"
          + (isBn ? "ঠিকানা: " : "Address: ") + item.getAddress() + "\n"
          + (isBn ? "গুগল ম্যাপ লিংক: " : "Google Maps Link: ") + "https://www.google.com/maps/search/?api=1&query=" + item.getLatitude() + "," + item.getLongitude();
      shareIntent.putExtra(Intent.EXTRA_TEXT, text);
      context.startActivity(Intent.createChooser(shareIntent, isBn ? "মসজিদ লোকেশন শেয়ার করুন" : "Share Mosque Location"));
    } catch (Exception ignored) {
    }
  }
}
