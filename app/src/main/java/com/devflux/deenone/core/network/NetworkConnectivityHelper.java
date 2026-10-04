package com.devflux.deenone.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkConnectivityHelper {

    public static final String MSG_OFFLINE_UNAVAILABLE =
            "তথ্য সাময়িকভাবে অনুপলব্ধ। অনুগ্রহ করে আপনার ইন্টারনেট সংযোগ পরীক্ষা করুন।";

    private static final MutableLiveData<Boolean> networkStatusLiveData = new MutableLiveData<>(false);
    private static boolean isRegistered = false;

    public static boolean isOnline(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            android.net.NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }
    }

    public static LiveData<Boolean> getNetworkStatusLiveData(Context context) {
        if (!isRegistered && context != null) {
            registerNetworkCallback(context.getApplicationContext());
        }
        return networkStatusLiveData;
    }

    private static synchronized void registerNetworkCallback(Context appContext) {
        if (isRegistered || appContext == null) return;
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;

        networkStatusLiveData.postValue(isOnline(appContext));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    networkStatusLiveData.postValue(true);
                }

                @Override
                public void onLost(Network network) {
                    networkStatusLiveData.postValue(false);
                }
            });
            isRegistered = true;
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            cm.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    networkStatusLiveData.postValue(true);
                }

                @Override
                public void onLost(Network network) {
                    networkStatusLiveData.postValue(false);
                }
            });
            isRegistered = true;
        }
    }
}
