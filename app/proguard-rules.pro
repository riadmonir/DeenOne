# ============================================================================
# DeenOne Islamic App - Production Proguard & R8 Rules for Google Play Store
# ============================================================================

# --- AndroidX & Material Components ---
-keep class androidx.appcompat.** { *; }
-keep class com.google.android.material.** { *; }
-keepclassmembers class * extends androidx.fragment.app.Fragment {
    public <init>();
}

# --- Room Database ---
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# --- Gson & Serialized Models ---
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.devflux.deanone.data.remote.model.** { *; }
-keep class com.devflux.deanone.data.local.entity.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
}

# --- Retrofit & OkHttp ---
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes InnerClasses
-keepclassmembers interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# --- Media3 & ExoPlayer ---
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# --- Google Play Services (Location & Mobile Ads) ---
-keep class com.google.android.gms.location.** { *; }
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.**

# --- ViewBinding & DataBinding ---
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(...);
    public static *** bind(...);
    public *** getRoot();
}

# --- Core Business Logic Models ---
-keep class com.devflux.deanone.core.** { *; }
-keep class com.devflux.deanone.domain.model.** { *; }
-keep class com.devflux.deanone.utils.** { *; }
