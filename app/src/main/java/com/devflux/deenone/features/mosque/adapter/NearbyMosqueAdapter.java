package com.devflux.deenone.features.mosque.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.mosque.model.MosqueItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NearbyMosqueAdapter extends RecyclerView.Adapter<NearbyMosqueAdapter.MosqueViewHolder> {

  public interface OnMosqueInteractionListener {
    void onDirectionsClicked(MosqueItem item);
    void onShareClicked(MosqueItem item);
  }

  private final List<MosqueItem> items = new ArrayList<>();
  private final OnMosqueInteractionListener listener;
  private double userLat = 28.3997;
  private double userLon = 36.5775;
  private boolean isWalkingMode = true;

  public NearbyMosqueAdapter(List<MosqueItem> initialList, OnMosqueInteractionListener listener) {
    if (initialList != null) {
      this.items.addAll(initialList);
    }
    this.listener = listener;
  }

  public void updateList(List<MosqueItem> newList, double userLat, double userLon) {
    this.items.clear();
    if (newList != null) {
      this.items.addAll(newList);
    }
    this.userLat = userLat;
    this.userLon = userLon;
    notifyDataSetChanged();
  }

  public void setTravelMode(boolean isWalking) {
    this.isWalkingMode = isWalking;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_mosque_card, parent, false);
    return new MosqueViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
    holder.bind(items.get(position), userLat, userLon, isWalkingMode, listener);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class MosqueViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvName, tvAddress, tvDistanceBadge, tvBearing;
    private final TextView tvEta, tvCoordinates, tvBadgeWudu, tvBadgeJumma, tvBadgeWomen;
    private final MaterialButton btnDirections;
    private final View btnShare;

    public MosqueViewHolder(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvMosqueName);
      tvAddress = itemView.findViewById(R.id.tvMosqueAddress);
      tvDistanceBadge = itemView.findViewById(R.id.tvMosqueDistanceBadge);
      tvBearing = itemView.findViewById(R.id.tvMosqueBearing);
      tvEta = itemView.findViewById(R.id.tvMosqueEta);
      tvCoordinates = itemView.findViewById(R.id.tvMosqueCoordinates);
      tvBadgeWudu = itemView.findViewById(R.id.tvBadgeWudu);
      tvBadgeJumma = itemView.findViewById(R.id.tvBadgeJumma);
      tvBadgeWomen = itemView.findViewById(R.id.tvBadgeWomen);
      btnDirections = itemView.findViewById(R.id.btnMosqueDirections);
      btnShare = itemView.findViewById(R.id.btnMosqueShare);
    }

    public void bind(MosqueItem item, double userLat, double userLon, boolean isWalking, OnMosqueInteractionListener listener) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
      String name = item.getName();
      if (name != null) {
        if (isBn) {
          name = name.replaceAll("\\s*\\([A-Za-z0-9\\s,.-]+\\)", "").trim();
        } else {
          name = name.replaceAll("\\s*\\([\\u0980-\\u09FF\\s,.-]+\\)", "").trim();
          if ("মসজিদ".equals(name)) {
            name = "Mosque";
          }
        }
      }
      tvName.setText(name != null && !name.isEmpty() ? name : (isBn ? "মসজিদ" : "Mosque"));

      String address = item.getAddress();
      if (address != null && !address.isEmpty()) {
        if (isBn) {
          address = address.replaceAll("\\s*\\([A-Za-z0-9\\s,.-]+\\)", "").trim();
        } else {
          address = address.replaceAll("\\s*\\([\\u0980-\\u09FF\\s,.-]+\\)", "").trim();
          if ("নিকটবর্তী এলাকা".equals(address)) {
            address = "Nearby area";
          }
        }
        tvAddress.setText(address);
      } else {
        tvAddress.setText(isBn ? "নিকটবর্তী এলাকা" : "Nearby area");
      }

      // Distance formatted
      double distMeters = item.getDistanceMeters();
      if (distMeters < 1000) {
        int meters = (int) distMeters;
        tvDistanceBadge.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(meters) + " মিটার") : (meters + " m"));
      } else {
        double km = distMeters / 1000.0;
        String kmStr = String.format(Locale.US, "%.1f", km);
        tvDistanceBadge.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(kmStr) + " কিমি") : (kmStr + " km"));
      }

      // Compass Bearing
      double bearing = calculateBearing(userLat, userLon, item.getLatitude(), item.getLongitude());
      tvBearing.setText(getBearingText(bearing, isBn));

      // ETA
      if (isWalking) {
        int walkMinutes = Math.max(1, (int) Math.round((distMeters / 1000.0) / 4.8 * 60));
        tvEta.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(walkMinutes) + " মিনিট হাঁটার দূরত্ব") : (walkMinutes + " min walk"));
      } else {
        int driveMinutes = Math.max(1, (int) Math.round((distMeters / 1000.0) / 30.0 * 60));
        tvEta.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(driveMinutes) + " মিনিট ড্রাইভ") : (driveMinutes + " min drive"));
      }

      // Coordinates
      tvCoordinates.setText(String.format(Locale.US, "Lat: %.3f, Lng: %.3f", item.getLatitude(), item.getLongitude()));

      // Amenities
      tvBadgeWudu.setText(isBn ? "ওযুখানা" : "Wudu Area");
      tvBadgeWudu.setVisibility(item.isHasWuduArea() ? View.VISIBLE : View.GONE);

      tvBadgeJumma.setText(isBn ? "জুমা জামাত" : "Jummah");
      tvBadgeJumma.setVisibility(View.VISIBLE);

      tvBadgeWomen.setText(isBn ? "নারী কর্নার" : "Women Area");
      tvBadgeWomen.setVisibility(item.isHasWomenArea() ? View.VISIBLE : View.GONE);

      btnDirections.setText(isBn ? "দিকনির্দেশনা" : "Directions");

      // Actions
      btnDirections.setOnClickListener(v -> {
        if (listener != null) {
          listener.onDirectionsClicked(item);
        }
      });

      btnShare.setOnClickListener(v -> {
        if (listener != null) {
          listener.onShareClicked(item);
        }
      });
    }

    private static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
      double phi1 = Math.toRadians(lat1);
      double phi2 = Math.toRadians(lat2);
      double deltaLambda = Math.toRadians(lon2 - lon1);

      double y = Math.sin(deltaLambda) * Math.cos(phi2);
      double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);
      double theta = Math.atan2(y, x);

      return (Math.toDegrees(theta) + 360.0) % 360.0;
    }

    private static String getBearingText(double bearing, boolean isBn) {
      if (bearing >= 337.5 || bearing < 22.5) return isBn ? "উত্তর" : "North";
      if (bearing >= 22.5 && bearing < 67.5) return isBn ? "উত্তর-পূর্ব" : "Northeast";
      if (bearing >= 67.5 && bearing < 112.5) return isBn ? "পূর্ব" : "East";
      if (bearing >= 112.5 && bearing < 157.5) return isBn ? "দক্ষিণ-পূর্ব" : "Southeast";
      if (bearing >= 157.5 && bearing < 202.5) return isBn ? "দক্ষিণ" : "South";
      if (bearing >= 202.5 && borderNear(bearing, 225)) return isBn ? "দক্ষিণ-পূর্ব" : "Southeast";
      if (bearing >= 202.5 && bearing < 247.5) return isBn ? "দক্ষিণ-পশ্চিম" : "Southwest";
      if (bearing >= 247.5 && bearing < 292.5) return isBn ? "পশ্চিম" : "West";
      return isBn ? "উত্তর-পশ্চিম" : "Northwest";
    }

    private static boolean borderNear(double b, double target) {
      return Math.abs(b - target) < 1.0;
    }
  }
}
