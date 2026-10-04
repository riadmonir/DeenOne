package com.devflux.deenone.features.blood.adapter;

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
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.blood.model.BloodOrganizationModel;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class BloodOrganizationAdapter extends RecyclerView.Adapter<BloodOrganizationAdapter.OrgViewHolder> {

  private final Context context;
  private List<BloodOrganizationModel> list = new ArrayList<>();

  public BloodOrganizationAdapter(Context context) {
    this.context = context;
  }

  public void setOrganizations(List<BloodOrganizationModel> orgs) {
    this.list = orgs != null ? new ArrayList<>(orgs) : new ArrayList<>();
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public OrgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_blood_organization_card, parent, false);
    return new OrgViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrgViewHolder holder, int position) {
    BloodOrganizationModel org = list.get(position);
    if (org == null) return;

    boolean isBn = LocaleManager.isBengali(context);

    // STRICT RULE 7: ZERO touch animation on the card, ONLY on buttons
    TouchAnimationUtil.attachTouchSpring(holder.btnCall);
    TouchAnimationUtil.attachTouchSpring(holder.btnWebsite);

    holder.tvCategory.setText(org.getCategory() != null ? org.getCategory() : (isBn ? "স্বেচ্ছাসেবী সংস্থা" : "Voluntary Org"));
    holder.tvDistrict.setText("📍 " + (org.getDistrict() != null ? org.getDistrict() : (isBn ? "সারাদেশ" : "Nationwide")));
    holder.tvName.setText(isBn ? org.getNameBn() : org.getNameEn());
    holder.tvAddress.setText(org.getAddress() != null ? org.getAddress() : "");
    holder.tvHotlineText.setText((isBn ? "কল: " : "Call: ") + org.getHotlinePhone());
    holder.tvWebsiteText.setText(isBn ? "ওয়েবসাইট" : "Website");

    holder.btnCall.setOnClickListener(v -> {
      if (org.getHotlinePhone() != null && !org.getHotlinePhone().isEmpty()) {
        try {
          Intent intent = new Intent(Intent.ACTION_DIAL);
          intent.setData(Uri.parse("tel:" + org.getHotlinePhone().replaceAll("[^0-9+]", "")));
          context.startActivity(intent);
        } catch (Exception e) {
          Toast.makeText(context, (isBn ? "কল করতে সমস্যা হয়েছে: " : "Failed to dial: ") + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });

    if (org.getWebsite() != null && !org.getWebsite().isEmpty()) {
      holder.btnWebsite.setVisibility(View.VISIBLE);
      holder.btnWebsite.setOnClickListener(v -> {
        try {
          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(org.getWebsite()));
          context.startActivity(intent);
        } catch (Exception e) {
          Toast.makeText(context, isBn ? "ওয়েবসাইট খোলা সম্ভব হয়নি" : "Could not open website", Toast.LENGTH_SHORT).show();
        }
      });
    } else {
      holder.btnWebsite.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public static class OrgViewHolder extends RecyclerView.ViewHolder {
    TextView tvCategory;
    TextView tvDistrict;
    TextView tvName;
    TextView tvAddress;
    TextView tvHotlineText;
    TextView tvWebsiteText;
    View btnCall;
    View btnWebsite;

    public OrgViewHolder(@NonNull View itemView) {
      super(itemView);
      tvCategory = itemView.findViewById(R.id.tvOrgCategoryTag);
      tvDistrict = itemView.findViewById(R.id.tvOrgDistrictBadge);
      tvName = itemView.findViewById(R.id.tvOrgName);
      tvAddress = itemView.findViewById(R.id.tvOrgAddress);
      tvHotlineText = itemView.findViewById(R.id.tvOrgHotlineText);
      tvWebsiteText = itemView.findViewById(R.id.tvOrgWebsiteText);
      btnCall = itemView.findViewById(R.id.btnOrgCallHotline);
      btnWebsite = itemView.findViewById(R.id.btnOrgWebsite);
    }
  }
}
