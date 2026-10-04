package com.devflux.deenone.core.navigation;

import android.content.Context;
import android.widget.Toast;

public class AppNavigator {

    public static void navigateToFeature(Context context, String featureTitle) {
        if (context != null) {
            Toast.makeText(context, featureTitle + " লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
        }
    }
}
