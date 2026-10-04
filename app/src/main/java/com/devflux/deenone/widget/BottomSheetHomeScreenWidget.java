package com.devflux.deenone.widget;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetHomeScreenWidgetBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetHomeScreenWidget extends BottomSheetDialogFragment implements HomeScreenWidgetAdapter.OnWidgetAddListener {

    private static final String TAG = "BottomSheetHomeScreenWidget";

    private final List<HomeScreenWidgetItem> allWidgets = new ArrayList<>();
    private HomeScreenWidgetAdapter adapter;
    private RecyclerView rvWidgets;

    public static void show(FragmentManager fragmentManager) {
        if (fragmentManager == null) return;
        BottomSheetHomeScreenWidget sheet = new BottomSheetHomeScreenWidget();
        sheet.show(fragmentManager, TAG);
    }

    public static void show(@NonNull Context context) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        BottomSheetHomeScreenWidgetBinding binding = BottomSheetHomeScreenWidgetBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        if (isBn) {
            binding.tvHomeScreenWidgetTitle.setText("হোম স্ক্রিন উইজেট");
            binding.tvHomeScreenWidgetSubtitle.setText("ফোনের হোমস্ক্রিনের জন্য প্রিমিয়াম ইসলামিক উইজেট");
            binding.btnCloseWidgetSheet.setContentDescription("ফিরে যান");
        } else {
            binding.tvHomeScreenWidgetTitle.setText("Home Screen Widgets");
            binding.tvHomeScreenWidgetSubtitle.setText("Premium Islamic widgets for your phone's home screen");
            binding.btnCloseWidgetSheet.setContentDescription("Back");
        }

        TouchAnimationUtil.attachTouchSpring(binding.btnCloseWidgetSheet);
        binding.btnCloseWidgetSheet.setOnClickListener(v -> dialog.dismiss());

        List<HomeScreenWidgetItem> widgets = buildWidgetList();

        binding.rvHomeScreenWidgets.setLayoutManager(new LinearLayoutManager(context));
        HomeScreenWidgetAdapter adapter = new HomeScreenWidgetAdapter(context, item -> {
            handleWidgetAdd(context, item);
        });
        binding.rvHomeScreenWidgets.setAdapter(adapter);
        adapter.setItems(widgets);

        dialog.show();
    }

    private static List<HomeScreenWidgetItem> buildWidgetList() {
        List<HomeScreenWidgetItem> list = new ArrayList<>();

        // 1. Day Summary (ডিলাক্স ডে সামারি)
        list.add(new HomeScreenWidgetItem(
            "day_summary",
            "ডিলাক্স ডে সামারি",
            "Deluxe Day Summary",
            "নামাজের ওয়াক্ত, হিজরি/ইংরেজি তারিখ, চাঁদের পর্যায় ও রোজা-সূর্যোদয়ের পূর্ণাঙ্গ তথ্য",
            "Comprehensive prayer times, Hijri/Gregorian date, moon phase, and fasting schedule",
            "৪ × ২",
            "4 × 2",
            "salat",
            R.layout.widget_day_summary,
            DeenOnePrayerAppWidgetProvider.class
        ));

        // 2. Day Summary Transparent (ডে সামারি ট্রান্সপারেন্ট)
        list.add(new HomeScreenWidgetItem(
            "day_summary_transparent",
            "ডে সামারি (স্বচ্ছ গ্লাস)",
            "Day Summary (Frosted Glass)",
            "যেকোনো ওয়ালপেপার বা থিমের সাথে মানানসই স্বচ্ছ ফ্রস্টেড গ্লাস ডিজাইন",
            "Translucent frosted glass design matching any wallpaper or aesthetic theme",
            "৪ × ২",
            "4 × 2",
            "salat",
            R.layout.widget_day_summary_transparent,
            WidgetDaySummaryTransparentProvider.class
        ));

        // 3. Salat Countdown & Schedule (সালাত কাউন্টডাউন ও সময়সূচি)
        list.add(new HomeScreenWidgetItem(
            "salat_countdown_schedule",
            "সালাত কাউন্টডাউন ও সময়সূচি",
            "Salat Countdown & Schedule",
            "বড় ডিজিটাল কাউন্টডাউন টাইমার ও ৫ ওয়াক্তের পূর্ণাঙ্গ টাইমলাইন স্ট্রিপ",
            "Large digital countdown timer with 5 daily prayer schedule timeline",
            "৪ × ২",
            "4 × 2",
            "salat",
            R.layout.widget_salat_countdown_schedule,
            WidgetSalatCountdownScheduleProvider.class
        ));

        // 4. Compact Salat Countdown (কমপ্যাক্ট ওয়াক্ত কাউন্টডাউন)
        list.add(new HomeScreenWidgetItem(
            "salat_countdown_compact",
            "কমপ্যাক্ট ওয়াক্ত কাউন্টডাউন",
            "Compact Waqt Countdown",
            "ব্যাটারি সাশ্রয়ী ডিপ ডার্ক থিমে বর্তমান ওয়াক্ত ও বড় কাউন্টডাউন",
            "Battery-efficient deep dark theme showing current waqt and countdown",
            "৩ × ২",
            "3 × 2",
            "salat",
            R.layout.widget_salat_countdown_compact,
            WidgetSalatCountdownCompactProvider.class
        ));

        // 5. Saom Fasting Timer (সিয়াম ও রোজা টাইমার)
        list.add(new HomeScreenWidgetItem(
            "saom_timer",
            "সিয়াম ও রোজা টাইমার",
            "Fasting & Saom Timer",
            "ইফতার ও সাহরির লাইভ দ্বৈত কাউন্টডাউন টাইমার পাশাপাশি",
            "Dual live countdown timers for Iftar and Sahri side-by-side",
            "৪ × ২",
            "4 × 2",
            "saom",
            R.layout.widget_saom_timer,
            WidgetSaomTimerProvider.class
        ));

        // 6. Muslims Day 3x2 Waqt Card (মুসলিমস ডে ওয়াক্ত)
        list.add(new HomeScreenWidgetItem(
            "muslims_day_compact",
            "মুসলিমস ডে কমপ্যাক্ট ওয়াক্ত",
            "Muslim's Day Compact",
            "হিজরি তারিখ, ওয়াক্তের নাম ও পরবর্তী সালাত কাউন্টডাউন",
            "Hijri date, current waqt name, and next prayer countdown",
            "৩ × ২",
            "3 × 2",
            "salat",
            R.layout.widget_muslims_day_compact,
            WidgetMuslimsDayCompactProvider.class
        ));

        // 7. Prayer Times Full List (দৈনিক ৫ ওয়াক্ত সময়সূচি)
        list.add(new HomeScreenWidgetItem(
            "prayer_times_list",
            "দৈনিক ৫ ওয়াক্ত সালাতের সময়সূচি",
            "5 Daily Prayer Times List",
            "ফজর, জোহর, আসর, মাগরিব ও এশার পূর্ণাঙ্গ তালিকা ও রিয়েলটাইম হাইলাইট",
            "Complete list of Fajr, Dhuhr, Asr, Maghrib, and Isha with realtime waqt highlight",
            "৪ × ২",
            "4 × 2",
            "salat",
            R.layout.widget_prayer_times_list,
            WidgetPrayerTimesListProvider.class
        ));

        // 8. Prayer Times List Transparent (৫ ওয়াক্ত ট্রান্সপারেন্ট)
        list.add(new HomeScreenWidgetItem(
            "prayer_times_list_transparent",
            "৫ ওয়াক্ত সময়সূচি (স্বচ্ছ গ্লাস)",
            "5 Prayer Times (Frosted Glass)",
            "৫ ওয়াক্ত সালাতের স্বচ্ছ গ্লাস ফ্রস্টেড ডিজাইন",
            "Minimal frosted glass design showcasing all 5 daily prayer times",
            "৪ × ২",
            "4 × 2",
            "salat",
            R.layout.widget_prayer_times_list_transparent,
            WidgetPrayerTimesListTransparentProvider.class
        ));

        // 9. Compact Prayer Bar (কমপ্যাক্ট ওয়াক্ত বার)
        list.add(new HomeScreenWidgetItem(
            "compact_prayer_bar",
            "কমপ্যাক্ট ওয়াক্ত বার",
            "Compact Prayer Bar",
            "স্ক্রিনের উপরে বা নিচে ব্যবহারের উপযোগী স্লিক মিনিমাল ওয়াক্ত বার",
            "Sleek minimal prayer bar ideal for the top or bottom of your home screen",
            "৪ × ১",
            "4 × 1",
            "salat",
            R.layout.widget_compact_prayer_bar,
            WidgetCompactPrayerBarProvider.class
        ));

        // 10. Single Waqt Focus Card (মিনিমাল ওয়াক্ত ফোকাস)
        list.add(new HomeScreenWidgetItem(
            "single_waqt_focus",
            "মিনিমাল ওয়াক্ত ফোকাস",
            "Single Waqt Focus Card",
            "স্কয়ার কার্ডে বর্তমান ওয়াক্ত ও সময়সীমা ফোকাস",
            "Square widget focusing on current waqt name and time range",
            "২ × ২",
            "2 × 2",
            "salat",
            R.layout.widget_single_waqt_focus,
            WidgetSingleWaqtFocusProvider.class
        ));

        // 11. Daily Quran Ayah (দৈনিক কুরআন আয়াত)
        list.add(new HomeScreenWidgetItem(
            "daily_ayah",
            "দৈনিক কুরআন আয়াত",
            "Daily Quran Ayah",
            "প্রতিদিনের নির্বাচিত আয়াত, আরবি ক্যালিগ্রাফি ও অর্থ",
            "Daily selected Ayah with authentic Arabic calligraphy and translation",
            "৪ × ২",
            "4 × 2",
            "content",
            R.layout.widget_daily_ayah,
            WidgetDailyAyahProvider.class
        ));

        // 12. Daily Hadith & Wisdom (দৈনিক হাদিস ও উপদেশ)
        list.add(new HomeScreenWidgetItem(
            "daily_hadith",
            "দৈনিক হাদিস ও উপদেশ",
            "Daily Hadith & Wisdom",
            "সহীহ হাদিস ও জীবনের জন্য সুন্দর ইসলামিক উপদেশ",
            "Authentic Hadith with practical Islamic guidance for daily life",
            "৪ × ২",
            "4 × 2",
            "content",
            R.layout.widget_daily_hadith,
            WidgetDailyHadithProvider.class
        ));

        // 13. Digital Tasbih & Zikr (ডিজিটাল তাসবীহ ও জিকির)
        list.add(new HomeScreenWidgetItem(
            "digital_tasbih",
            "ডিজিটাল তাসবীহ",
            "Digital Tasbih Counter",
            "উইজেটের ওপর সরাসরি +১ ট্যাপ করে তাসবীহ গণনা ও জিকির বদল",
            "Tap +1 directly on the widget to count tasbih and switch dhikr presets",
            "৩ × ২",
            "3 × 2",
            "tools",
            R.layout.widget_digital_tasbih,
            WidgetDigitalTasbihProvider.class
        ));

        // 14. Islamic Calendar & Hijri Event (হিজরি ক্যালেন্ডার ও দিন)
        list.add(new HomeScreenWidgetItem(
            "islamic_calendar",
            "হিজরি ক্যালেন্ডার ও দিন",
            "Islamic Hijri Calendar",
            "হিজরি তারিখ, আরবি মাস ও বিশেষ ইসলামিক দিবস ব্যাজ",
            "Hijri date, Islamic month, and special Islamic event badges",
            "২ × ২",
            "2 × 2",
            "tools",
            R.layout.widget_islamic_calendar,
            WidgetIslamicCalendarProvider.class
        ));

        // 15. Qibla Direction & Prayer Snapshot (কিবলা দিক ও সালাত)
        list.add(new HomeScreenWidgetItem(
            "qibla_snapshot",
            "কিবলা দিক ও সালাত স্ন্যাপশট",
            "Qibla Direction & Prayer Snapshot",
            "কিবলার সঠিক ডিগ্রি কোণ ও বর্তমান ওয়াক্ত",
            "Accurate Qibla compass degree and current prayer waqt status",
            "৩ × ২",
            "3 × 2",
            "tools",
            R.layout.widget_qibla_snapshot,
            WidgetQiblaSnapshotProvider.class
        ));

        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DeenOneBottomSheetDialog);
        allWidgets.clear();
        allWidgets.addAll(buildWidgetList());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            BottomSheetDialog bsd = (BottomSheetDialog) d;
            FrameLayout bottomSheet = bsd.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_home_screen_widget, container, false);

        boolean isBn = LocaleManager.isBengali(requireContext());

        TextView tvTitle = view.findViewById(R.id.tvHomeScreenWidgetTitle);
        TextView tvSubtitle = view.findViewById(R.id.tvHomeScreenWidgetSubtitle);
        ImageButton btnClose = view.findViewById(R.id.btnCloseWidgetSheet);

        if (tvTitle != null) {
            tvTitle.setText(isBn ? "হোম স্ক্রিন উইজেট" : "Home Screen Widgets");
        }
        if (tvSubtitle != null) {
            tvSubtitle.setText(isBn ? "ফোনের হোমস্ক্রিনের জন্য প্রিমিয়াম ইসলামিক উইজেট" : "Premium Islamic widgets for your phone's home screen");
        }
        if (btnClose != null) {
            btnClose.setContentDescription(isBn ? "ফিরে যান" : "Back");
            TouchAnimationUtil.attachTouchSpring(btnClose);
            btnClose.setOnClickListener(v -> dismiss());
        }

        rvWidgets = view.findViewById(R.id.rvHomeScreenWidgets);
        rvWidgets.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HomeScreenWidgetAdapter(requireContext(), this);
        rvWidgets.setAdapter(adapter);
        adapter.setItems(allWidgets);

        return view;
    }

    @Override
    public void onAddWidget(HomeScreenWidgetItem item) {
        if (getContext() == null || item == null) return;
        handleWidgetAdd(getContext(), item);
    }

    private static void handleWidgetAdd(Context context, HomeScreenWidgetItem item) {
        if (context == null || item == null) return;

        boolean isBn = LocaleManager.isBengali(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName myProvider = new ComponentName(context, item.providerClass);

            if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                appWidgetManager.requestPinAppWidget(myProvider, null, null);
                String msg = isBn
                    ? "হোমস্ক্রিনে '" + item.titleBengali + "' উইজেট যুক্ত করুন"
                    : "Add '" + item.titleEnglish + "' widget to Home Screen";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } else {
                showManualAddGuideDialog(context, item, isBn);
            }
        } else {
            showManualAddGuideDialog(context, item, isBn);
        }
    }

    private static void showManualAddGuideDialog(Context context, HomeScreenWidgetItem item, boolean isBn) {
        if (context == null) return;

        String title = isBn ? "হোমস্ক্রিনে উইজেট যুক্ত করার নিয়ম" : "How to Add Widget to Home Screen";
        String message = isBn
            ? "১. আপনার ফোনের হোমস্ক্রিনের খালি জায়গায় আঙুল দিয়ে ২ সেকেন্ড চেপে ধরে রাখুন।\n\n২. নিচে প্রদর্শিত 'Widgets' বা 'উইজেট' মেন্যুতে ট্যাপ করুন।\n\n৩. 'DeenOne' খুঁজে বের করে '" + item.titleBengali + "' উইজেটটি টেনে এনে হোমস্ক্রিনে রাখুন।"
            : "1. Touch and hold an empty space on your home screen for 2 seconds.\n\n2. Tap the 'Widgets' menu.\n\n3. Find 'DeenOne' and drag the '" + item.titleEnglish + "' widget onto your home screen.";
        String positiveBtn = isBn ? "ঠিক আছে" : "Got It";

        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveBtn, (d, w) -> d.dismiss())
            .show();
    }
}
