package com.devflux.deenone.features.blood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.blood.model.BloodRequestModel;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.RequestViewHolder> {

  private final Context context;
  private List<BloodRequestModel> requestList = new ArrayList<>();
  private final OnRequestListener clickListener;

  public interface OnRequestListener {
    void onRequestClicked(BloodRequestModel model);
  }

  public BloodRequestAdapter(Context context, OnRequestListener listener) {
    this.context = context;
    this.clickListener = listener;
  }

  public void setRequests(List<BloodRequestModel> list) {
    this.requestList = list != null ? new ArrayList<>(list) : new ArrayList<>();
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_blood_request_card, parent, false);
    return new RequestViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
    BloodRequestModel req = requestList.get(position);
    if (req == null) return;

    boolean isBn = LocaleManager.isBengali(context);

    // STRICT RULE 7: ZERO touch animation on the card view, ONLY on the action button
    TouchAnimationUtil.attachTouchSpring(holder.btnViewDetails);

    String statusText = "OPEN".equalsIgnoreCase(req.getStatus())
        ? (isBn ? "রক্তের আবেদন" : "Blood Request")
        : (isBn ? "রক্তের আবেদন (গৃহীত)" : "Blood Request (Accepted)");
    holder.tvCategoryTag.setText(statusText);

    // Time ago
    holder.tvTimeAgo.setText(isBn ? "জরুরি" : "Urgent");

    // Title
    String title = isBn
        ? ("🩸 " + req.getUnitsNeeded() + " ব্যাগ রক্ত প্রয়োজন (" + req.getBloodGroup() + ")")
        : ("🩸 " + req.getUnitsNeeded() + " Bag(s) Needed (" + req.getBloodGroup() + ")");
    holder.tvTitle.setText(title);

    // Subtitle
    String subtitle = (isBn ? "রোগী: " : "Patient: ") + req.getPatientName()
        + " | " + (req.getHospitalName() != null ? req.getHospitalName() : "")
        + " (" + (req.getDistrict() != null ? req.getDistrict() : "") + ")";
    holder.tvSubtitle.setText(subtitle);

    holder.btnViewDetails.setText(isBn ? "বিস্তারিত দেখুন →" : "View Details →");

    View.OnClickListener action = v -> {
      if (clickListener != null) clickListener.onRequestClicked(req);
    };

    holder.itemView.setOnClickListener(action);
    holder.btnViewDetails.setOnClickListener(action);
  }

  @Override
  public int getItemCount() {
    return requestList.size();
  }

  public static class RequestViewHolder extends RecyclerView.ViewHolder {
    TextView tvCategoryTag;
    TextView tvTimeAgo;
    TextView tvTitle;
    TextView tvSubtitle;
    TextView btnViewDetails;

    public RequestViewHolder(@NonNull View itemView) {
      super(itemView);
      tvCategoryTag = itemView.findViewById(R.id.tvRequestCategoryTag);
      tvTimeAgo = itemView.findViewById(R.id.tvRequestTimeAgo);
      tvTitle = itemView.findViewById(R.id.tvRequestItemTitle);
      tvSubtitle = itemView.findViewById(R.id.tvRequestItemSubtitle);
      btnViewDetails = itemView.findViewById(R.id.btnViewRequestDetails);
    }
  }
}
