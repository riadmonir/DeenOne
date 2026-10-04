package com.devflux.deenone.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "feature_widgets")
public class FeatureWidgetEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String featureId;

    private String title;
    private String iconResName;
    private String circleBgHex;
    private String iconTintHex;
    private int displayOrder;
    private boolean isEnabled;
    private String actionKey;

    public FeatureWidgetEntity() {
        this.featureId = "feat_" + System.currentTimeMillis();
        this.isEnabled = true;
    }

    @Ignore
    public FeatureWidgetEntity(@NonNull String featureId, String title, String iconResName,
                               String circleBgHex, String iconTintHex, int displayOrder,
                               boolean isEnabled, String actionKey) {
        this.featureId = featureId;
        this.title = title;
        this.iconResName = iconResName;
        this.circleBgHex = circleBgHex;
        this.iconTintHex = iconTintHex;
        this.displayOrder = displayOrder;
        this.isEnabled = isEnabled;
        this.actionKey = actionKey;
    }

    @NonNull
    public String getFeatureId() { return featureId; }
    public void setFeatureId(@NonNull String featureId) { this.featureId = featureId; }

    public String getTitle() { return title != null ? title : ""; }
    public void setTitle(String title) { this.title = title; }

    public String getIconResName() { return iconResName != null ? iconResName : "ic_feat_book"; }
    public void setIconResName(String iconResName) { this.iconResName = iconResName; }

    public String getCircleBgHex() { return circleBgHex != null ? circleBgHex : "#041A14"; }
    public void setCircleBgHex(String circleBgHex) { this.circleBgHex = circleBgHex; }

    public String getIconTintHex() { return iconTintHex != null ? iconTintHex : "#34D399"; }
    public void setIconTintHex(String iconTintHex) { this.iconTintHex = iconTintHex; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { isEnabled = enabled; }

    public String getActionKey() { return actionKey != null ? actionKey : ""; }
    public void setActionKey(String actionKey) { this.actionKey = actionKey; }
}
