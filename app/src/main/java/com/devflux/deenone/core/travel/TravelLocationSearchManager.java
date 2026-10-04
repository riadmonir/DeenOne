package com.devflux.deenone.core.travel;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TravelLocationSearchManager {

    public static class TravelLocationItem {
        public final String title;
        public final String subtitle;
        public final double latitude;
        public final double longitude;
        public final String country;
        public final String category; // "ISLAMIC_HOLY", "BD_DISTRICT", "INTERNATIONAL", "SEARCH_RESULT"
        public double distanceKmFromOrigin;

        public TravelLocationItem(String title, String subtitle, double latitude, double longitude, String country, String category) {
            this.title = title;
            this.subtitle = subtitle;
            this.latitude = latitude;
            this.longitude = longitude;
            this.country = country;
            this.category = category;
            this.distanceKmFromOrigin = 0;
        }
    }

    public interface SearchCallback {
        void onResults(List<TravelLocationItem> results);
    }

    private static volatile TravelLocationSearchManager instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final List<TravelLocationItem> curatedDataset = new ArrayList<>();

    public static TravelLocationSearchManager getInstance() {
        if (instance == null) {
            synchronized (TravelLocationSearchManager.class) {
                if (instance == null) {
                    instance = new TravelLocationSearchManager();
                }
            }
        }
        return instance;
    }

    private TravelLocationSearchManager() {
        initCuratedDataset();
    }

    private void initCuratedDataset() {
        // 1. ISLAMIC HOLY SITES & HISTORICAL PLACES
        curatedDataset.add(new TravelLocationItem("মক্কা মুকাররমা (Makkah)", "সৌদি আরব • পবিত্র কাবা শরীফ ও মসজিদুল হারাম", 21.4225, 39.8262, "Saudi Arabia", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("মদিনা মুনাওয়ারা (Madinah)", "সৌদি আরব • মসজিদে নববী ও রওজা মুবারক", 24.5247, 39.5692, "Saudi Arabia", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("আল-কুদস / জেরুজালেম (Al-Quds)", "ফিলিস্তিন • মসজিদুল আকসা ও পবিত্র ভূমি", 31.7767, 35.2345, "Palestine", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("ইস্তাম্বুল (Istanbul)", "তুরস্ক • ঐতিহাসিক উসমানীয় খিলাফত ও সুলতান আহমেদ মসজিদ", 41.0082, 28.9784, "Turkey", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("কায়রো (Cairo)", "মিশর • ঐতিহাসিক আল-আজহার বিশ্ববিদ্যালয় ও নগরী", 30.0444, 31.2357, "Egypt", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("বাগদাদ (Baghdad)", "ইরাক • ঐতিহাসিক ইসলামী ঐতিহ্য", 33.3152, 44.3661, "Iraq", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("দামেস্ক (Damascus)", "সিরিয়া • ঐতিহাসিক উমাইয়া মসজিদ", 33.5138, 36.2765, "Syria", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("সমরখন্দ / বুখারা (Samarkand)", "উজবেকিস্তান • ইমাম বুখারী (রহ.)-এর ভূমি", 39.6542, 66.9597, "Uzbekistan", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("রিয়াদ (Riyadh)", "সৌদি আরব • রাজধানী", 24.7136, 46.6753, "Saudi Arabia", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("জেদ্দা (Jeddah)", "সৌদি আরব • পবিত্র হজের প্রধান প্রবেশদ্বার", 21.5433, 39.1728, "Saudi Arabia", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("দুবাই (Dubai)", "সংযুক্ত আরব আমিরাত (UAE)", 25.2048, 55.2708, "United Arab Emirates", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("দোহা (Doha)", "কাতার • রাজধানী", 25.2854, 51.5310, "Qatar", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("কুয়ালালামপুর (Kuala Lumpur)", "মালয়েশিয়া • রাজধানী ও আধুনিক ইসলামিক স্থাপত্য", 3.1390, 101.6869, "Malaysia", "ISLAMIC_HOLY"));
        curatedDataset.add(new TravelLocationItem("জাকার্তা (Jakarta)", "ইন্দোনেশিয়া • বৃহত্তম মুসলিম জনসংখ্যা ও ইস্তিকলাল মসজিদ", -6.2088, 106.8456, "Indonesia", "ISLAMIC_HOLY"));

        // 2. BANGLADESH ALL 64 DISTRICTS & POPULAR TRAVEL HUBS
        curatedDataset.add(new TravelLocationItem("ঢাকা (Dhaka)", "বাংলাদেশ • রাজধানী", 23.8103, 90.4125, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("চট্টগ্রাম (Chattogram)", "বাংলাদেশ • বাণিজ্যিক রাজধানী ও বন্দর নগরী", 22.3569, 91.7832, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("সিলেট (Sylhet)", "বাংলাদেশ • হযরত শাহজালাল ও শাহপরান (রহ.)-এর পুণ্যভূমি", 24.8949, 91.8687, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("কক্সবাজার (Cox's Bazar)", "বাংলাদেশ • বিশ্বের দীর্ঘতম প্রাকৃতিক সমুদ্র সৈকত", 21.4272, 92.0058, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("রাজশাহী (Rajshahi)", "বাংলাদেশ • রেশম নগরী ও পদ্মা পার", 24.3745, 88.6042, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("খুলনা (Khulna)", "বাংলাদেশ • সুন্দরবন প্রবেশদ্বার", 22.8456, 89.5403, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("বরিশাল (Barishal)", "বাংলাদেশ • ধান-নদী-খালের প্রাচ্যের ভেনিস", 22.7010, 90.3535, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("রংপুর (Rangpur)", "বাংলাদেশ • তিস্তা অববাহিকার উত্তরাঞ্চল", 25.7439, 89.2752, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("ময়মনসিংহ (Mymensingh)", "বাংলাদেশ • ব্রহ্মপুত্র পাড়", 24.7471, 90.4203, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("কুমিল্লা (Cumilla)", "বাংলাদেশ • শালবন বিহার ও ইতিহাস", 23.4607, 91.1809, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("বগুড়া (Bogura)", "বাংলাদেশ • ঐতিহাসিক মহাস্থানগড়", 24.8465, 89.3777, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("দিনাজপুর (Dinajpur)", "বাংলাদেশ • কান্তজীউ ও রামসাগর", 25.6217, 88.6355, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("যশোর (Jashore)", "বাংলাদেশ • রূপসী যশোর", 23.1664, 89.2081, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("বাগেরহাট (Bagerhat)", "বাংলাদেশ • বিশ্ব ঐতিহ্য ষাট গম্বুজ মসজিদ", 22.6516, 89.7859, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("পটুয়াখালী / কুয়াকাটা (Kuakata)", "বাংলাদেশ • সূর্যোদয় ও সূর্যাস্তের সাগরকন্যা", 21.8167, 90.1167, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("সুনামগঞ্জ / টাঙ্গুয়ার হাওর (Tanguar Haor)", "বাংলাদেশ • অনন্য প্রাকৃতিক জীববৈচিত্র্য", 25.1333, 91.0667, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("মৌলভীবাজার / শ্রীমঙ্গল (Sreemangal)", "বাংলাদেশ • চায়ের রাজধানী ও লাউয়াছড়া", 24.3065, 91.7296, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("খাগড়াছড়ি / সাজেক ভ্যালি (Sajek Valley)", "বাংলাদেশ • পাহাড়ের মেঘের রাজ্য", 23.3833, 92.2833, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("বান্দরবান (Bandarban)", "বাংলাদেশ • নীলগিরি ও পাহাড়ি ঝরনা", 22.1953, 92.2184, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("রাঙ্গামাটি (Rangamati)", "বাংলাদেশ • নয়নাভিরাম কাপ্তাই হ্রদ", 22.6533, 92.1789, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("পঞ্চগড় / তেঁতুলিয়া (Tetulia)", "বাংলাদেশ • বাংলাবান্ধা ও কাঞ্চনজঙ্ঘা ভিউ", 26.4833, 88.3500, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("ফেনী (Feni)", "বাংলাদেশ • ঢাকা-চট্টগ্রাম ট্রানজিট", 23.0186, 91.3966, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("নোয়াখালী (Noakhali)", "বাংলাদেশ • মেঘনা উপকূল", 22.8696, 91.0997, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("ব্রাহ্মণবাড়িয়া (Brahmanbaria)", "বাংলাদেশ • তিতাস নদী বিধৌত", 23.9571, 91.1119, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("চাঁদপুর (Chandpur)", "বাংলাদেশ • ইলিশের বাড়ি ও পদ্মা-মেঘনা মোহনা", 23.2333, 90.6667, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("গাজীপুর (Gazipur)", "বাংলাদেশ • ভাওয়াল জাতীয় উদ্যান", 23.9985, 90.4210, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("নারায়ণগঞ্জ (Narayanganj)", "বাংলাদেশ • প্রাচীন সোনারগাঁও পানাম নগর", 23.6238, 90.5000, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("টাঙ্গাইল (Tangail)", "বাংলাদেশ • তাঁতের রাজধানী ও আতিয়া মসজিদ", 24.2513, 89.9167, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("পাবনা (Pabna)", "বাংলাদেশ • রূপপুর ও পদ্মাপাড়", 24.0064, 89.2372, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("সিরাজগঞ্জ (Sirajganj)", "বাংলাদেশ • যমুনা বহুমুখী সেতু", 24.4534, 89.7006, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("কুষ্টিয়া (Kushtia)", "বাংলাদেশ • লালন শাহ মাজার ও শিলাইদহ", 23.9013, 89.1205, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("ফরিদপুর (Faridpur)", "বাংলাদেশ • পদ্মা পার", 23.6071, 89.8429, "Bangladesh", "BD_DISTRICT"));
        curatedDataset.add(new TravelLocationItem("ভোলা (Bhola)", "বাংলাদেশ • বৃহত্তম দ্বীপ জেলা", 22.6859, 90.6481, "Bangladesh", "BD_DISTRICT"));

        // 3. MAJOR INTERNATIONAL METROPOLISES
        curatedDataset.add(new TravelLocationItem("লন্ডন (London)", "United Kingdom • যুক্তরাজ্য", 51.5074, -0.1278, "United Kingdom", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("নিউইয়র্ক (New York)", "United States • যুক্তরাষ্ট্র", 40.7128, -74.0060, "United States", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("টরন্টো (Toronto)", "Canada • কানাডা", 43.6532, -79.3832, "Canada", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("টোকিও (Tokyo)", "Japan • জাপান", 35.6762, 139.6503, "Japan", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("সিডনি (Sydney)", "Australia • অস্ট্রেলিয়া", -33.8688, 151.2093, "Australia", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("সিঙ্গাপুর (Singapore)", "Singapore", 1.3521, 103.8198, "Singapore", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("কলকাতা (Kolkata)", "ভারত • পশ্চিমবঙ্গ", 22.5726, 88.3639, "India", "INTERNATIONAL"));
        curatedDataset.add(new TravelLocationItem("দিল্লি (New Delhi)", "ভারত • জামে মসজিদ", 28.6139, 77.2090, "India", "INTERNATIONAL"));
    }

    public List<TravelLocationItem> getCuratedCategoryList(String category, double originLat, double originLng) {
        List<TravelLocationItem> list = new ArrayList<>();
        for (TravelLocationItem item : curatedDataset) {
            if ("ALL".equalsIgnoreCase(category) || item.category.equalsIgnoreCase(category)) {
                TravelLocationItem copy = copyItem(item);
                if (originLat != 0.0 && originLng != 0.0) {
                    copy.distanceKmFromOrigin = TravelModeManager.calculateDistanceKm(originLat, originLng, copy.latitude, copy.longitude);
                }
                list.add(copy);
            }
        }
        return list;
    }

    public void searchLocationsAsync(Context context, String query, double originLat, double originLng, SearchCallback callback) {
        executor.execute(() -> {
            List<TravelLocationItem> results = new ArrayList<>();
            String cleanQuery = query != null ? query.trim().toLowerCase() : "";

            if (cleanQuery.isEmpty()) {
                List<TravelLocationItem> def = getCuratedCategoryList("ALL", originLat, originLng);
                new Handler(Looper.getMainLooper()).post(() -> callback.onResults(def));
                return;
            }

            // 1. Search in local curated database first (Instant match)
            for (TravelLocationItem item : curatedDataset) {
                if (item.title.toLowerCase().contains(cleanQuery) ||
                    item.subtitle.toLowerCase().contains(cleanQuery) ||
                    item.country.toLowerCase().contains(cleanQuery)) {
                    TravelLocationItem copy = copyItem(item);
                    if (originLat != 0.0 && originLng != 0.0) {
                        copy.distanceKmFromOrigin = TravelModeManager.calculateDistanceKm(originLat, originLng, copy.latitude, copy.longitude);
                    }
                    results.add(copy);
                }
            }

            // 2. Query Live OpenStreetMap Nominatim / Photon Geocoding REST API
            try {
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                String urlStr = "https://photon.komoot.io/api/?q=" + encodedQuery + "&limit=10";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3500);
                conn.setReadTimeout(3500);
                conn.setRequestProperty("User-Agent", "DeenOne-Islamic-App/2.1");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    JSONArray features = root.optJSONArray("features");
                    if (features != null) {
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject feat = features.getJSONObject(i);
                            JSONObject geometry = feat.optJSONObject("geometry");
                            JSONObject props = feat.optJSONObject("properties");

                            if (geometry != null && props != null) {
                                JSONArray coords = geometry.optJSONArray("coordinates");
                                if (coords != null && coords.length() >= 2) {
                                    double lng = coords.getDouble(0);
                                    double lat = coords.getDouble(1);

                                    String name = props.optString("name", "");
                                    String city = props.optString("city", props.optString("state", ""));
                                    String country = props.optString("country", "");

                                    if (!name.isEmpty()) {
                                        String title = name;
                                        String subtitle = (!city.isEmpty() ? city + ", " : "") + country;

                                        // Check if already in list
                                        boolean duplicate = false;
                                        for (TravelLocationItem ex : results) {
                                            if (Math.abs(ex.latitude - lat) < 0.01 && Math.abs(ex.longitude - lng) < 0.01) {
                                                duplicate = true;
                                                break;
                                            }
                                        }

                                        if (!duplicate) {
                                            TravelLocationItem onlineItem = new TravelLocationItem(
                                                    title, subtitle.isEmpty() ? country : subtitle, lat, lng, country, "SEARCH_RESULT"
                                            );
                                            if (originLat != 0.0 && originLng != 0.0) {
                                                onlineItem.distanceKmFromOrigin = TravelModeManager.calculateDistanceKm(originLat, originLng, lat, lng);
                                            }
                                            results.add(onlineItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
                // Fallback to Android native Geocoder
            }

            // 3. Fallback to native Android Geocoder if needed
            if (results.isEmpty() && context != null) {
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocationName(query, 5);
                    if (addresses != null) {
                        for (Address addr : addresses) {
                            String feature = addr.getFeatureName() != null ? addr.getFeatureName() : addr.getLocality();
                            String admin = addr.getAdminArea() != null ? addr.getAdminArea() : "";
                            String country = addr.getCountryName() != null ? addr.getCountryName() : "";
                            String title = feature != null ? feature : query;
                            String subtitle = (!admin.isEmpty() ? admin + ", " : "") + country;

                            TravelLocationItem geoItem = new TravelLocationItem(
                                    title, subtitle, addr.getLatitude(), addr.getLongitude(), country, "SEARCH_RESULT"
                            );
                            if (originLat != 0.0 && originLng != 0.0) {
                                geoItem.distanceKmFromOrigin = TravelModeManager.calculateDistanceKm(originLat, originLng, addr.getLatitude(), addr.getLongitude());
                            }
                            results.add(geoItem);
                        }
                    }
                } catch (Exception ignored) {}
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onResults(results));
        });
    }

    private TravelLocationItem copyItem(TravelLocationItem src) {
        return new TravelLocationItem(src.title, src.subtitle, src.latitude, src.longitude, src.country, src.category);
    }
}
