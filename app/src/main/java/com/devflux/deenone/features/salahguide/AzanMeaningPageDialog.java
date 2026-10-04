package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemAzanMeaningCardBinding;
import com.devflux.deenone.databinding.PageAzanMeaningRulesBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AzanMeaningPageDialog {

    private static final String PREFS_NAME = "azan_meaning_prefs";
    private static final String KEY_FONT_SIZE = "azan_meaning_font_size";

    public static class AzanItem {
        final String title;
        final String preview;
        final String fullContent;
        boolean isExpanded = false;

        public AzanItem(String title, String preview, String fullContent) {
            this.title = title;
            this.preview = preview;
            this.fullContent = fullContent;
        }
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAzanMeaningRulesBinding binding = PageAzanMeaningRulesBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "আজান ও ইকামতের অর্থ" : "Meaning of Adhan & Iqamah");

        // Back Button with Spring
        binding.btnBackAzanMeaning.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackAzanMeaning);

        // Prepare 17 Items with dual language support
        List<AzanItem> items = getAzanItems(isBn);

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        float savedSize = prefs.getFloat(KEY_FONT_SIZE, 14.0f);

        AzanAdapter adapter = new AzanAdapter(activity, items, savedSize, isBn);
        binding.rvAzanMeaningList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvAzanMeaningList.setAdapter(adapter);

        // Settings Gear Action Button
        binding.btnSettingsAzanMeaning.setOnClickListener(v -> {
            showSettingsDialog(activity, adapter, items, prefs, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsAzanMeaning);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, AzanAdapter adapter, List<AzanItem> items, SharedPreferences prefs, boolean isBn) {
        String[] options = {
                isBn ? "সবগুলো বিস্তারিত দেখুন" : "Expand All",
                isBn ? "সবগুলো সংক্ষেপ করুন" : "Collapse All",
                isBn ? "ফন্ট সাইজ: ছোট (১২ sp)" : "Font: Small (12 sp)",
                isBn ? "ফন্ট সাইজ: সাধারণ (১৪ sp)" : "Font: Normal (14 sp)",
                isBn ? "ফন্ট সাইজ: প্রমিত (১৬ sp)" : "Font: Medium (16 sp)",
                isBn ? "ফন্ট সাইজ: বড় (১৮ sp)" : "Font: Large (18 sp)",
                isBn ? "ফন্ট সাইজ: বিশাল (২০ sp)" : "Font: Extra Large (20 sp)",
                isBn ? "সম্পূর্ণ পাতা কপি করুন" : "Copy Entire Page",
                isBn ? "সম্পূর্ণ পাতা শেয়ার করুন" : "Share Entire Page"
        };
        float[] sizes = {12.0f, 14.0f, 16.0f, 18.0f, 20.0f};

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পঠন সেটিংস ও অপশন" : "Reading Settings & Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        for (AzanItem it : items) it.isExpanded = true;
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {
                        for (AzanItem it : items) it.isExpanded = false;
                        adapter.notifyDataSetChanged();
                    } else if (which >= 2 && which <= 6) {
                        float chosen = sizes[which - 2];
                        adapter.setFontSize(chosen);
                        prefs.edit().putFloat(KEY_FONT_SIZE, chosen).apply();
                    } else if (which == 7) {
                        copyToClipboard(activity, isBn ? "আজান ও ইকামতের অর্থ" : "Meaning of Adhan & Iqamah", getFullContentText(items, isBn), isBn);
                    } else if (which == 8) {
                        shareContent(activity, isBn ? "আজান ও ইকামতের অর্থ" : "Meaning of Adhan & Iqamah", getFullContentText(items, isBn), isBn);
                    }
                })
                .show();
    }

    private static void copyToClipboard(Context context, String label, String text, boolean isBn) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(label, text.trim()));
            Toast.makeText(context, isBn ? "ক্লিপবোর্ডে কপি করা হয়েছে" : "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareContent(Context context, String title, String text, boolean isBn) {
        String fullShare = text.trim() + "\n\n" + (isBn ? "— দ্বীনওয়ান" : "— DeenOne");
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullShare);
        context.startActivity(Intent.createChooser(sendIntent, isBn ? "শেয়ার করুন" : "Share via"));
    }

    private static String getFullContentText(List<AzanItem> items, boolean isBn) {
        StringBuilder sb = new StringBuilder();
        sb.append(isBn ? "আজান ও ইকামতের অর্থ ও বিধিবিধান\n\n" : "Meaning and Rules of Adhan & Iqamah\n\n");
        for (AzanItem it : items) {
            sb.append("• ").append(it.title).append("\n");
            sb.append(it.fullContent).append("\n\n");
        }
        return sb.toString();
    }

    private static class AzanAdapter extends RecyclerView.Adapter<AzanAdapter.ViewHolder> {
        private final Activity activity;
        private final List<AzanItem> items;
        private float fontSizeSp;
        private final boolean isBn;

        public AzanAdapter(Activity activity, List<AzanItem> items, float fontSizeSp, boolean isBn) {
            this.activity = activity;
            this.items = items;
            this.fontSizeSp = fontSizeSp;
            this.isBn = isBn;
        }

        public void setFontSize(float sp) {
            this.fontSizeSp = sp;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemAzanMeaningCardBinding binding = ItemAzanMeaningCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AzanItem item = items.get(position);
            holder.binding.tvItemTitle.setText(item.title);
            holder.binding.tvItemPreview.setText(item.preview);
            holder.binding.tvItemFullContent.setText(item.fullContent);

            holder.binding.tvItemPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeSp);
            holder.binding.tvItemFullContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeSp);

            // Update UI state based on isExpanded
            updateExpandState(holder.binding, item.isExpanded);

            View.OnClickListener toggleClick = v -> {
                item.isExpanded = !item.isExpanded;
                updateExpandState(holder.binding, item.isExpanded);
            };

            holder.binding.layoutToggleExpand.setOnClickListener(toggleClick);
            holder.binding.cardContainer.setOnClickListener(toggleClick);

            // Menu button
            holder.binding.btnItemMenu.setOnClickListener(v -> {
                String[] opts = {
                        isBn ? "লেখা কপি করুন" : "Copy Text",
                        isBn ? "শেয়ার করুন" : "Share"
                };
                new MaterialAlertDialogBuilder(activity)
                        .setTitle(item.title)
                        .setItems(opts, (dialog, which) -> {
                            if (which == 0) {
                                copyToClipboard(activity, item.title, item.title + "\n\n" + item.fullContent, isBn);
                            } else if (which == 1) {
                                shareContent(activity, item.title, item.title + "\n\n" + item.fullContent, isBn);
                            }
                        })
                        .show();
            });
            TouchAnimationUtil.attachTouchSpring(holder.binding.btnItemMenu);
        }

        private void updateExpandState(ItemAzanMeaningCardBinding b, boolean isExpanded) {
            if (isExpanded) {
                b.tvItemPreview.setVisibility(View.GONE);
                b.tvItemFullContent.setVisibility(View.VISIBLE);
                b.tvToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
                b.ivToggleChevron.animate().rotation(180f).setDuration(180).start();
            } else {
                b.tvItemPreview.setVisibility(View.VISIBLE);
                b.tvItemFullContent.setVisibility(View.GONE);
                b.tvToggleText.setText(isBn ? "বিস্তারিত" : "Details");
                b.ivToggleChevron.animate().rotation(0f).setDuration(180).start();
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemAzanMeaningCardBinding binding;
            ViewHolder(ItemAzanMeaningCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static List<AzanItem> getAzanItems(boolean isBn) {
        List<AzanItem> list = new ArrayList<>();

        if (isBn) {
            // 1. আজান এর অর্থ
            list.add(new AzanItem(
                    "আজান এর অর্থ",
                    "আল্লাহ সর্বশক্তিমান আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্...",
                    "আল্লাহ সর্বশক্তিমান\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত\n\n" +
                            "নামাজের জন্য এসো\n\n" +
                            "সাফল্যের জন্য এসো\n\n" +
                            "আল্লাহ্ মহান\n\n" +
                            "আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই\n\n" +
                            "আছছালা-তু খায়রুম মিনান নাঊম” - “ঘুম হতে নামাজ উত্তম”( শুধুমাত্র ফজর নামাজের সময়)"
            ));

            // 2. ইকামতের অর্থ
            list.add(new AzanItem(
                    "ইকামতের অর্থ",
                    "আল্লাহ সর্বশক্তিমান আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্...",
                    "আল্লাহ সর্বশক্তিমান\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ্ ছাড়া অন্য কোন মাবুদ নেই\n\n" +
                            "আমি সাক্ষ্য দিচ্ছি যে, মুহাম্মাদ আল্লাহর প্রেরিত দূত\n\n" +
                            "নামাজের জন্য এসো\n\n" +
                            "সাফল্যের জন্য এসো\n\n" +
                            "নামাজ আরম্ভ হলো\n\n" +
                            "আল্লাহ্ মহান\n\n" +
                            "আল্লাহ্ ছাড়া অন্য কোন উপাস্য নেই"
            ));

            // 3. আজানের প্রারম্ভিক ইতিহাস
            list.add(new AzanItem(
                    "আজানের প্রারম্ভিক ইতিহাস",
                    "মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন... [ফাতহুল বারী, বুখারী, আবুদাঊদ]",
                    "মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন। অতঃপর মদ্বীনায় হিজরত করলে হিজরী ১ম (মতান্তরে ২য়) সনে আযান ফরয হয়।\n\n[ফাতহুল বারী, ইবনে হাজার ২/৭৮]\n\nসকল মুসলমানকে একত্রে সমবেত করে জামাআতবদ্ধভাবে নামায পড়ার জন্য এমন এক জিনিসের প্রয়োজন ছিল, যা শুনে বা দেখে তাঁরা জমা হতে পারতেন। এ জন্যে তাঁরা পূর্ব থেকেই মসজিদে উপস্থিত হয়ে নামাযের অপেক্ষা করতেন। এ মর্মে তাঁরা একদিন পরামর্শ করলেন, কেউ বললেন নাসারাদের ঘন্টার মত আমরাও ঘন্টা ব্যবহার করব। কেউ কেউ বললেন, বরং ইয়াহুদীদের শৃঙ্গের মত শৃঙ্গ ব্যবহার করব। হযরত উমার (রাঃ) বললেন, ‘বরং নামাযের প্রতি আহ্বান করার জন্য একটি লোককে (গলি-গলি) পাঠিয়ে দিলে কেমন হয়?’ কিন্তু মহানবী (ﷺ) বললেন, “হে বিলাল! ওঠ, নামাযের জন্য আহ্বান কর।\n\n[বুখারী ৬০৪, মুসলিম, সহীহ]\n\nকেউ বললেন, ‘নামাযের সময় মসজিদে একটি পতাকা উত্তোলন করা হোক। লোকেরা তা দেখে একে অপরকে নামাযের সময় জানিয়ে দেবে। কিন্তু মহানবী (ﷺ) এ সব পছন্দ করলেন না।\n\n[আবূদাঊদ, সুনান ৪৯৮নং]\n\nপরিশেষে তিনি একটি ঘন্টা নির্মাণের আদেশ দিলেন। এই অবসরে আব্দুল্লাহ বিন যায়দ (রাঃ) স্বপ্নে দেখলেন, এক ব্যক্তি ঘন্টা হাতে যাচ্ছে। আব্দুল্লাহ বলেন, আমি তাকে বললাম, হে আল্লাহর বান্দা! ঘন্টাটি বিক্রয় করবে?’ লোকটি বলল, ‘এটা নিয়ে কি করবে?’ আমি বললাম, ‘ওটা দিয়ে লোকেদেরকে নামাযের জন্য আহ্বান করব। লোকটি বলল, আমি তোমাকে এর চাইতে উত্তম জিনিসের কথা বলে দেব না কি? আমি বললাম অবশ্যই।\n\nতখন ঐ ব্যক্তি আব্দুল্লাহকে আযান ও ইকামত শিখিয়ে দিল। অতঃপর সকাল হলে তিনি রসূল (ﷺ) এর নিকট উপস্থিত হয়ে স্বপ্নের কথা খুলে বললেন। সব কিছু শুনে মহানবী (ﷺ) বললেন, “ইনশাআল্লাহ! এটি সত্য স্বপ্ন। অতএব তুমি বিলালের সাথে দাঁড়াও এবং স্বপ্নে যেমন (আযান) শুনেছ ঠিক তেমনি বিলালকে শুনাও; সে ঐ সব বলে আযান দিক। কারণ, বিলালের আওয়াজ তোমার চেয়ে উচ্চ।”\n\nসাহাবী আব্দুল্লাহ বিন যায়েদ (রাঃ) সর্বপ্রথম পূর্বরাতে স্বপ্নে দেখা আযানের কালেমা সমূহ সকালে এসে রাসূলুল্লাহ (সাঃ)-এর নিকটে বর্ণনা করেন। পরে বেলালের কণ্ঠে একই আযান ধ্বনি শুনে হযরত ওমর (রাঃ) বাড়ী থেকে বেরিয়ে চাদর ঘেঁষতে ঘেঁষতে ছুটে এসে রাসূলুল্লাহ (সাঃ)-কে বলেন –\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\nঅর্থ:\nযিনি আপনাকে ‘সত্য’ সহকারে প্রেরণ করেছেন, তাঁর কসম করে বলছি আমিও অনুরূপ স্বপ্ন দেখেছি’। একথা শুনে রাসূলুল্লাহ (সাঃ) ‘ফালিল−ল্লা-হিল হাম্দ’বলে আল্লাহর প্রশংসা করেন।\n\n[আবুদাঊদ হা/৪৯৫; মিশকাত হা/৬৫০]\n\nএকটি বর্ণনা মতে ঐ রাতে ১১ জন সাহাবী একই আযানের স্বপ্ন দেখেন’।\n\n[মিরক্বাত শরহ মিশকাত ‘আযান’ অনুচ্ছেদ ২/১৪৯ পৃঃ]\n\nউল্লেখ্য যে, ওমর ফারূক (রাঃ) ২০ দিন পূর্বে উক্ত স্বপ্ন দেখেছিলেন। কিন্তু আব্দুল্লাহ বিন যায়েদ আগেই বলেছে দেখে লজ্জায় তিনি নিজের কথা প্রকাশ করেননি।\n\n[আবুদাঊদ (আওনুল মা‘বূদ সহ) হা/৪৯৪ ‘আযানের সূচনা’ অনুচ্ছেদ]"
            ));

            // 4. আযান ও তার মাহাত্ম
            list.add(new AzanItem(
                    "আযান ও তার মাহাত্ম",
                    "আযান ফরয এবং তা দেওয়া হল ফরযে কিফায়াহ্... [বুখারী, মুসলিম, কুরআন ৪১/৩৩]",
                    "আযান ফরয এবং তা দেওয়া হল ফ র্যে কিফায়াহ্। আল্লাহর রসূল (ﷺ) বলেন, নামাযের সময় উপস্থিত হলে তোমাদের একজন আযান দেবে এবং তোমাদের মধ্যে যে বড় সে ইমামতি করবে।\n\n[বুখারী ৬২৮নং, মুসলিম, নাসাঈ, সুনান, দারেমী, সুনান]\n\nআযান ইসলামের অন্যতম নিদর্শন ও প্রতীক। কোন গ্রাম বা শহরবাসী তা ত্যাগ করলে ইমাম (রাষ্ট্রপ্রধান) তাদের বিরুদ্ধে জিহাদ করবেন। যেমন মহানবী (ﷺ) অভিযানে গেলে কোন জনপদ থেকে আযানের ধ্বনি শুনলে তাদের উপর আক্রমণ করতেন না।\n\n[বুখারী ৬১০ নং, মুসলিম, সহীহ]\n\nসফরে একা থাকলে অথবা মসজিদ খুবই দূর হলে এবং আযান শুনতে না পাওয়া গেলে একাই আযান ও ইকামত দিয়ে নামায পড়া সুন্নত।\n\n[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫৫]\n\nআযান দেওয়ায় (মুআযযেনের জন্য) রয়েছে বড় সওয়াব ও ফযীলত। মহান আল্লাহ বলেন, “সে ব্যক্তি অপেক্ষা আর কার কথা উৎকৃষ্ট, যে ব্যক্তি আল্লাহর দিকে মানুষকে আহ্বান করে, সৎকাজ করে এবং বলে আমি একজন ‘মুসলিম’ (আত্মসমর্পণকারী)?\n\n[কুরআন মাজীদ ৪১/৩৩]\n\nপ্রিয় নবী (ﷺ) বলেন, “লোকে যদি আযান ও প্রথম কাতারের মাহাত্ম জানত, অতঃপর তা লাভের জন্য লটারি করা ছাড়া আর অন্য কোন উপায় না পেত, তাহলে তারা লটারিই করত।”\n\n[বুখারী ৬১৫, মুসলিম, সহীহ ৪৩৭নং]\n\nআল্লাহ প্রথম কাতারের উপর রহ্মত বর্ষণ করেন এবং ফিরিশ্তাগণ তাদের জন্য ক্ষমা প্রার্থনা করে থাকেন। মুআযযিনকে তার আযানের আওয়াযের উচ্চতা অনুযায়ী ক্ষমা করা হয়। তার আযান শ্রবণকারী প্রত্যেক সরস বা নীরস বস্তু তার কথার সত্যায়ন করে থাকে। তার সাথে যারা নামায পড়ে তাদের সকলের নেকীর সমপরিমাণ তার নেকী লাভ হয়।\n\n[আহ্মদ, নাসাঈ, সহীহ তারগীব ২২৮নং]\n\nকিয়ামতের দিন মুআযযিনগণের গর্দান অন্যান্য লোকেদের চেয়ে লম্বা হবে।”\n\n[মুসলিম, সহীহ৩৮৭নং]\n\n“যে ব্যক্তি বারো বৎসর আযান দেবে তার জন্য জান্নাত ওয়াজেব হয়ে যাবে। আর প্রত্যেক দিন আযানের দরুন তার আমল নামায় ষাটটি নেকী লিপিবদ্ধ করা হবে এবং তার ইকামতের দরুন লিপিবদ্ধ হবে ত্রিশটি নেকী।\n\n[ইবনে মাজাহ্, দারাকুত্বনী,হাকেম, সহীহ তারগীব ২৪০নং]\n\nযে কোন মানুষ, জ্বিন বা অন্য কিছু মুআযযিনের আযানের শব্দ শুনতে পাবে, সেই মুআযযিনের জন্য কিয়ামতের দিন সাক্ষ্য প্রদান করবে।\n\n[বুখারী ৬০৯ নং]"
            ));

            // 5. মসজিদ ছাড়া অন্য স্থানে আযান
            list.add(new AzanItem(
                    "মসজিদ ছাড়া অন্য স্থানে আযান",
                    "ভয়, শত্রুতা, মসজিদ দূরে থাকা বা নির্জন প্রান্তরে আযান-ইকামতের বিধান... [বুখারী, মুসলিম, আবূদাঊদ]",
                    "ভয়, শত্রুতা প্রভৃতির কারণে মসজিদে যেতে বাধা থাকলে, মসজিদ বহু দূরে হলে (এবং আযান শুনতে না পেলে), সফরে কোন নির্জন প্রান্তরে থাকলে, যে জায়গায় থাকবে সেই জায়গাতেই নামাযের সময় হলে আযান-ইকামত দিয়ে নামায আদায় করতে হবে। একা হলে আযান ওয়াজেব না হলেও সুন্নত অবশ্যই বটে।\n\nমহানবী (ﷺ) বলেন, “যখন সফরে থাকবে, তখন তোমরা আযান দিও এবং ইকামত দিও। আর তোমাদের মধ্যে যে বড় সে ইমামতি করো।\n\n[বুখারী, মিশকাত ৬৮২নং]\n\nতাছাড়া আল্লাহর নবী (ﷺ) এবং সাহাবাগণ সফরে থাকলে ফাঁকা মাঠে আযান দিয়ে নামায পড়েছেন।\n\n[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]\n\nমহানবী (ﷺ) বলেন, “তোমার প্রতিপালক বিস্মিত হন পর্বত চূড়ায় সেই ছাগলের রাখালকে দেখে যে নামাযের জন্য আযান দিয়ে (সেখানেই) নামায আদায় করে; আল্লাহ আযযা অজাল্ল্ বলেন, “তোমরা আমার এই বান্দাকে লক্ষ্য কর, (এমন জায়গাতেও) আযান দিয়ে নামায কায়েম করছে! সে আমাকে ভয় করে। আমি তাকে ক্ষমা করে দিলাম এবং জান্নাতে প্রবেশ করালাম।\n\n[আবূদাঊদ, সুনান, নাসাঈ, সুনান, সহিহ তারগিব ২৩৯ নং]\n\nতিনি বলেন, “কোন ব্যক্তি যখন কোন বৃক্ষ-পানিহীন প্রান্তরে থাকে, অতঃপর সেখানে নামাযের সময় উপস্থিত হয়, তখন সে যেন ওযু করে। পানি না পেলে যেন তায়াম্মুম করে। অতঃপর সে যদি শুধু ইকামত দিয়ে নামায পড়ে, তাহলে তার সাথে তার সঙ্গী দুই ফিরিশ্তা নামায পড়েন। কিন্তু সে যদি আযান দিয়ে ও ইকামত দিয়ে নামায পড়ে, তাহলে তার পশ্চাতে আল্লাহর এত ফিরিশ্তা নামায পড়েন, যাদের দুই প্রান্ত নজরে আসে না!”\n\n[আব্দুর রাযযাক, মুসান্নাফ, সহিহ তারগিব ২৪১নং]\n\nআর একদা তিনি আব্দুল্লাহ বিন আব্দুর রহ্মানকে মরুভূমিতে ছাগপালে থাকাকালে নামাযের জন্য উচ্চশব্দে আযান দিতে আদেশ করেছিলেন।\n\n[বুখারী প্রমুখ, মিশকাত ৬৫৬নং]"
            ));

            // 6. আযানের পর মসজিদ থেকে বের হওয়া
            list.add(new AzanItem(
                    "আযানের পর মসজিদ থেকে বের হওয়া",
                    "বিনা ওজরে নামায না পড়ে বের হওয়ার নিষেধাজ্ঞা ও হাদীসের হুশিয়ারী... [আহমাদ, মুসলিম, ইবনে মাজাহ্]",
                    "আযান হয়ে গেলে বিনা ওজরে নামায না পড়ে মসজিদ থেকে বের হয়ে যাওয়া বৈধ নয়।\n\n" +
                            "মহানবী (ﷺ) বলেন, “মসজিদে অবস্থানকালে আযান হলেই তোমাদের কেউ যেন নামায না পড়া পর্যন্ত মসজিদ থেকে বের না হয়।\n\n" +
                            "[আহমাদ, মুসনাদ, মিশকাত ১০৭৪ নং]\n\n" +
                            "এক ব্যক্তি আযানের পর মসজিদ হতে বের হয়ে গেলে আবূ হুরাইরা তার প্রতি ইঙ্গিত করে বললেন, ‘এ লোকটা তো আবুল কাসেম (ﷺ) এর নাফরমানী করল।’\n\n" +
                            "[মুসলিম, আবূদাঊদ, সুনান ৫৩৬ নং, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, বায়হাকী]\n\n" +
                            "আল্লাহর রসূল (ﷺ) বলেন, “যে ব্যক্তির মসজিদে থাকা অবস্থায় আযান হয়, অতঃপর বিনা কোন প্রয়োজনে বের হয়ে যায় এবং ফিরে আসার ইচ্ছা না রাখে সে ব্যক্তি মুনাফিক।\n\n" +
                            "[ইবনে মাজাহ্, সুনান, সহিহ তারগিব ১৫৭নং]"
            ));

            // 7. আযান ও ইকামতের মাঝে ব্যবধান
            list.add(new AzanItem(
                    "আযান ও ইকামতের মাঝে ব্যবধান",
                    "আযান ও ইকামতের বিরতি, ওযুর সময় ও মাগরিবের জামাআতের পূর্ববর্তী নামায... [বুখারী, মুসলিম]",
                    "আযান ও ইকামতের মাঝে কতটা বিরতি থাকবে সে ব্যাপারে হাদীস শরীফে কোন স্পষ্ট ইঙ্গিত ও উল্লেখ পাওয়া যায় না। তবে আযান হয় জামাআত ডাকার জন্য। আর এটাই স্বাভাবিক যে, আযানের পর অনেকে ওযু করবে। সুতরাং ওযু করার মত সময় দিতে হবে। তাছাড়া ফরয নামাযের পূর্বে যে সুন্নাতে রাতেবাহ্ বা মুআক্কাদাহ আছে তাও পড়ার জন্য সময় দিতে হবে। মহানবী (ﷺ) বলেন, প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে। এইরুপ তিনবার বলার পর শেষে বললেন, “যে চাইবে তার জন্য।\n\n" +
                            "[বুখারী, মুসলিম, মিশকাত ৬৬২নং]\n\n" +
                            "মাগরেবের আযানের পরেও সত্বর জামাআত শুরু করা উচিৎ নয়। যদিও সময় সংকীর্ণ তবুও জামাআত হওয়ার পূর্বে নামায আছে। সুতরাং যার সেই নামায পড়ার ইচ্ছা তাকে সেই নামায পড়তে সময় দেওয়া উচিৎ।\n\n" +
                            "আনাস (রাঃ) বলেন, আমরা মদ্বীনায় ছিলাম। মুআযযিন যখন মাগরেবের আযান দিত, তখন লোকেরা প্রতিযোগিতার সাথে মসজিদের খাম্বাগুলোর পশ্চাতে ২ রাকআত নামায পড়তে লেগে যেত। এমনকি যদি কোন অজানা লোক এসে মসজিদে প্রবেশ করত, তাহলে এত লোকের নামায পড়া দেখে সে মনে করত, হয়তো মাগরেবের জামাআত হয়ে গেছে। (এবং ওরা পরের সুন্নত পড়ছে।)\n\n" +
                            "[মুসলিম, মিশকাত ১১৮০ নং]"
            ));

            // 8. আযান ও ইকামতের মাঝে দুআ
            list.add(new AzanItem(
                    "আযান ও ইকামতের মাঝে দুআ",
                    "দুআ কবুলের সময়, হাদীসের বাণী ও ইকামত ও জিহাদের কাতারে দুআ... [আহমাদ, আবূদাঊদ, হাকেম]",
                    "আযান হওয়ার পর এবং ইকামত হওয়ার পূর্বের সময়ে দুআ কবুল হয়ে থাকে। তাই এই সময় দুনিয়া ও আখেরাতের মঙ্গল আল্লাহর নিকট প্রার্থনা করা উচিৎ। মহানবী (ﷺ) বলেন, “আযান ও ইকামতের মাঝে দুআ রদ্দ্ করা হয় না। (অর্থাৎ মঞ্জুর করা হয়।)\n\n" +
                            "[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান ৫২১নং, তিরমিযী, সুনান]\n\n" +
                            "এক বর্ণনায় আছে, “সুতরাং তোমরা এ সময়ে দুআ কর।\n\n" +
                            "[জামে ৩৪০৫ নং]\n\n" +
                            "তিনি আরো বলেন, “দু’টি সময়ে দুআ (প্রার্থনা)কারীর দুআ রদ্দ্ হয় না; যখন নামাযের ইকামত হয় এবং জিহাদের কাতারে।\n\n" +
                            "[হাকেম, মুস্তাদরাক, মালেক, মুঅত্তা, সহিহ তারগিব ২৬০ নং]"
            ));

            // 9. ইকামত
            list.add(new AzanItem(
                    "ইকামত",
                    "ইকামতের বাক্যসমূহ, প্রসিদ্ধ রূপ, বিভিন্ন বর্ণনা ও ভুলে না দেওয়ার বিধান... [বুখারী, মুসলিম, আবূদাঊদ]",
                    "যেমন দুই মুআযযিনের আযান দুই রকম ছিল, তেমনি উভয়ের ইকামতও ছিল দুই রকম; জোড় এবং বিজোড়। বিলাল (রাঃ) কে আযান ডবল ডবল শব্দে এবং ইকামত ‘ক্বাদ ক্বা-মাতিস সলা-হ্ ছাড়া (অন্যান্য) বাক্যাবলীকে একক একক শব্দে বলতে আদেশ করা হয়েছিল।\n\n" +
                            "[বুখারী, মুসলিম, সহীহ প্রমুখ, মিশকাত ৬৪১ নং]\n\n" +
                            "সুতরাং বিলালের উক্ত হাদীসের ভিত্তিতে ইকামত হবে ৯টি বাক্যে; ‘ক্বাদ ক্বামাতিস সালাহ্ ২বার এবং বাকী হবে ১ বার করে।\n\n" +
                            "[আলমুমতে, শারহে ফিক্হ, ইবনে উষাইমীন ২/৫৯]\n\n" +
                            "কিন্তু আব্দুল্লাহ বিন যায়দকে স্বপ্নে শিখানো হয়েছিল নিম্নরুপ ইকামত, আর এটাই প্রসিদ্ধ:-\n\n" +
                            "اَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله، أَشْهَدُ أَنَّ مُحَمَّداً رَّسُوْلُ الله، حَيَّ عَلَى الصَّلاَة،\n\n" +
                            "حَيَّ عَلَى الْفَلاَح، قَدْ قَامَتِ الصَّلاَة، قَدْ قَامَتِ الصَّلاَة، اَللهُ أَكْبَر اَللهُ أَكْبَر، لاَ إِلهَ إِلاَّ الله،\n\n" +
                            "আল্লাহু আকবার ২ বার। আশহাদু আল লা ইলাহা ইল্লাল্লাহ্ ১ বার। আশহাদু আন্না মুহাম্মাদার রাসূলুল্লাহ্ ১ বার। হাইয়্যা আলাস স্বলাহ্ ১ বার। হাইয়্যা আলাল ফালাহ্ ১ বার। ক্বাদ ক্বামাতিস স্বলাহ্ (অর্থাৎ নামায প্রতিষ্ঠা বা শুরু হল) ২ বার। আল্লাহু আকবার ২বার এবং লা ইলাহা ইল্লাল্লাহ্ ১ বার।\n\n" +
                            "[আবূদাঊদ, সুনান ৪৯৯, দারেমী, সুনান ১১৭১, ইবনে খুযাইমাহ্, সহীহ ৩৭০, ইবনে হিব্বান, সহীহ ১৬৭১নং, বায়হাকী ১/৩৯১]\n\n" +
                            "উল্লেখ্য যে, যারা মুআযযিন আবূ মাহ্যূরার মত তারজী’ আযান দেয়, তাদের উচিৎ তাঁর মতই ইকামত দেওয়া। তিনি বলেন, ‘মহানবী (ﷺ) তাঁকে আযানের ১৯টি এবং ইকামতের ১৭টি বাক্য শিখিয়েছেন।’\n\n" +
                            "[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান, তিরমিযী, সুনান, নাসাঈ, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, মিশকাত ৬৪৪নং]\n\n" +
                            "সুতরাং তাঁর ইকামত ছিল বিলাল (রাঃ) এর আযানের মতই। তবে তাতে ‘হাইয়্যা আলাল ফালাহ্ এর পর অতিরিক্ত ছিল ‘ক্বাদ ক্বামাতিস স্বলাহ্ ২ বার।\n\n" +
                            "[আবূদাঊদ, সুনান ৫০২নং]\n\n" +
                            "ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ) বলেন, আহলে হাদীস ও তাঁদের সমর্থকদের নিকট সঠিক সিদ্ধান্ত এই যে, মহানবী (ﷺ) হতে যা কিছু শুদ্ধভাবে প্রমাণিত আছে তার প্রত্যেকটার উপর আমল করতে হবে। আর তাঁরা ঐ আমলের কোনটিকেও অপছন্দ করেন না। কেননা, আযান ও ইকামতের পদ্ধতি একাধিক হওয়ার ব্যাপারটা ক্বিরাআত, তাশাহহুদ প্রভৃতির পদ্ধতি একাধিক হওয়ার মতই।’\n\n" +
                            "[মাজমূউ ফাতাওয়া ২২/৩৩৫, ২২/৬৬]\n\n" +
                            "সুতরাং উভয় প্রকারই আযান ও ইকামত আমলযোগ্য। আর বৈধ নয় এ নিয়ে কাদা ছুঁড়াছুঁড়ি।\n\n" +
                            "প্রকাশ থাকে যে, ভুলে ইকামত না দিয়ে (একাকী অথবা জামাআতী) নামায পড়ে ফেললে নামাযের কোন ক্ষতি হয় না। ইকামত নামায হতে পৃথক জিনিস। অতএব ঐ ভুলের জন্য সহু সিজদা বিধেয় নয়।\n\n" +
                            "[তুহ্ফাতুল ইখওয়ান, ইবনে বায ৭৮পৃ:]"
            ));

            // 10. ইকামতের জওয়াব
            list.add(new AzanItem(
                    "ইকামতের জওয়াব",
                    "ইকামতকে দ্বিতীয় আযান বলা, জওয়াব প্রদানের পদ্ধতি ও তাহক্বীক্ব... [মুসলিম, ফাতাওয়া ইসলামিয়্যাহ্, আলবানী]",
                    "ইকামতকে দ্বিতীয় আযান বলা হয়, তাই ইকামতও এক প্রকার আযান।\n\n" +
                            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৪৯]\n\n" +
                            "সুতরাং এর জওয়াবও আযানের মতই। অবশ্য ‘হাইয়্যা আলাস সলা-হ্ ও ফালাহ্’ এর জওয়াবে ‘লাহাউলা অলা ক্বুওয়াতা ইল্লা বিল্লাহ্’ এবং শেষে (সময় পেলে) দরুদ ও অসীলার দুআ পাঠ করা বিধেয়। যেহেতু হাদীস শরীফে মুআযযিনের জওয়াব (তার মতই) বলতে এবং তার শেষে দরুদ ও অসীলার দুআ পড়তে আমাদেরকে আদেশ করা হয়েছে।\n\n" +
                            "[মুসলিম, মিশকাত ৬৫৭নং]\n\n" +
                            "উক্ত হাদীসের ভিত্তিতেই ‘ক্বাদ ক্বামাতিস স্বলাহ্ এর জওয়াবে ‘ক্বাদ ক্বামাতিস স্বলাহ্’ই বলতে হবে। নচেৎ এর জওয়াবে ‘আক্বামাহুল্লাহু অআদামাহা’ বলার হাদীস শুদ্ধ নয়। আর যয়ীফ হাদীসকে ভিত্তি করে শরীয়তের কোন আমল ও ইবাদত বৈধ নয়।\n\n" +
                            "[মিশকাত, আলবানীর টীকা ১/১২১]\n\n" +
                            "মতান্তরে যেহেতু ইকামতের জবাবে কোন স্পষ্ট সহীহ হাদীস নেই, তাই ইকামতের জবাব দেওয়া সুন্নত নয়।"
            ));

            // 11. ইকামত ও নামায শুরু করার মাঝে ব্যবধান
            list.add(new AzanItem(
                    "ইকামত ও নামায শুরু করার মাঝে ব্যবধান",
                    "ইমামকে না দেখে না দাঁড়ানো, শান্তভাব বজায় রাখা, জরুরতে বিরতি ও কথাবার্তার বিধান... [বুখারী, ফাতাওয়া ইসলামিয়্যাহ্]",
                    "আল্লাহর রসূল (ﷺ) বলেন, “নামাযের ইকামত হয়ে গেলে তোমরা আমাকে না দেখা পর্যন্ত (নামাযের জন্য) দাঁড়াও না।”\n\n" +
                            "[বুখারী ৬৩৭নং]\n\n" +
                            "যেমন ইকামত হয়ে গেলে তাড়াহুড়ো করে দাঁড়ানোও উচিৎ নয়। কারণ উক্তহাদীসের এক বর্ণনায় তিনি বলেন, “তোমাদের মাঝে যেন ধীরতা ও শান্তভাব থাকে। যেহেতু রাজাধিরাজের দরবারে কোন প্রকারের হৈ-হুল্লোড় ও তাড়াহুড়ো চলে না। বলা বাহুল্য এই দরবারে থাকবে শত আদব, শত বিনয়, ধীরতা ও স্থিরতা।\n\n" +
                            "হুমাইদ বলেন, আমি সাবেত আল-বুনানীকে ইকামতের পর কথাবার্তা বলার বৈধতার ব্যাপারে প্রশ্ন করলে তিনি আনাস (রাঃ) কর্তৃক বর্ণিত হাদীস শুনালেন; ‘একদা নামাযের ইকামত হয়ে গেলে এক ব্যক্তি নবী (ﷺ) কে নামাযে প্রবেশ করতে আটকে রেখেছিল।\n\n" +
                            "[বুখারী ৬৪৩নং]\n\n" +
                            "মসজিদের এক প্রান্তে গোপনে কথা বলতে লাগলে উপস্থিত মুসল্লীগণ ঘুমে ঢলে পড়েছিল। একদা নামাযের ইকামত হয়ে গেলে মুসল্লীগণ কাতার সোজা করে দাঁড়িয়ে গিয়েছিল। আল্লাহর রসূল (ﷺ) হুজরা হতে বের হয়ে যখন ইমামতির জায়গায় এলেন, তখন তাঁর মনে পড়ল যে, তিনি নাপাকীর গোসল করেননি। তিনি সকলের উদ্দেশ্যে বললেন, “তোমরা স্বস্থানে দন্ডায়মান থাক।” অতঃপর তিনি হুজরায় ফিরে গিয়ে গোসল করলেন। তিনি যখন বের হয়ে এলেন, তখন তাঁর মাথা হতে পানি টপকাচ্ছিল। এরপর তিনি ইমামতি করে নামায পড়লেন।\n\n" +
                            "উক্ত হাদীস থেকে এ কথা বুঝা যায় যে, প্রয়োজনে ইকামত ও নামাযের মাঝে বেশ কিছু সময় বিরতি হলে কোন ক্ষতি হয় না। পরন্তু ইকামত ফিরিয়ে বলতে হয় না।\n\n" +
                            "ইকামত হওয়ার পর কোন জরুরী কথা, নামায ও কাতার বিষয়ক কথা বলা বৈধ। তবে নামাযের প্রস্তুতি নেওয়ার পর কোন পার্থিব কথা বলা উচিৎ নয়।\n\n" +
                            "[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫১]\n\n" +
                            "ইকামত শুরু হলে এবং ইমাম উপস্থিত থাকলে প্রত্যেকে নিজের সুবিধামত উঠে নামাযের জন্য দন্ডায়মান হবে। ইকামতের শুরুতে, মাঝে বা শেষে, যে কোন সময়ে দাঁড়ালেই চলবে। তবে এ কথার খেয়াল অবশ্যই রাখা উচিৎ, যাতে ইমামের সাথে তকবীরে তাহ্রীমা ছুটে না যায়।\n\n" +
                            "[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৩/১০]"
            ));

            // 12. তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান
            list.add(new AzanItem(
                    "তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান",
                    "ফজরের পূর্বে সেহ্রীর আযান, সহীহ হাদীসের প্রমাণ ও সুন্নাহ বনাম বিদআত... [বুখারী, মুসলিম]",
                    "মহানবী (ﷺ) বলেন, বিলাল রাতে (ফজরের পূর্বে) আযান দেয়। সুতরাং ইবনে উম্মে মাকতূম (ফজরের) আযান না দেওয়া পর্যন্ত তোমরা খাও ও পান কর।\n\n" +
                            "[বুখারী, মুসলিম, মিশকাত ৬৮০নং]\n\n" +
                            "উক্ত হাদীস দ্বারা প্রমাণিত হয় যে, ফজরের পূর্বে তাহাজ্জুদ ও সেহ্রীর আযান মহানবী (ﷺ) এর যুগে প্রচলিত ছিল এবং আজও পর্যন্ত সে সুন্নত মক্কা-মদ্বীনা সহ্ সঊদী আরবের প্রায় সকল স্থানে সেহ্রীর ঐ আযান (বিশেষ করে রমযানে) শুনতে পাওয়া যায়। পক্ষান্তরে আমাদের দেশে প্রায় সকল স্থানে ঐ সময়ে আযানের পরিবর্তে শোনা যায় কুরআন ও গজল পাঠ! সুতরাং এ কথা নিঃসন্দেহে বলা যায় যে, সুন্নতের জায়গা দখল করেছে মনগড়া বিদআত।\n\n" +
                            "অনেকে বলে থাকেন, উভয় সময়ে আযান হলে লোকেরা গোলমালে পড়বে; সেটা সেহ্রীর না ফজরের আযান -এ নিয়ে সন্দেহে পড়বে। কিন্তু পৃথক পৃথক উভয় সময়ের জন্য নির্দিষ্ট দু’জন মুআযযিন আযান দিলে গোলমালের ভয় থাকে না। তা ছাড়া সেহ্রীর আযানে\n\n" +
                            "الصَّلاَةُ خَيْرٌ مِّنَ النَّوْم\n\n" +
                            "শব্দ থাকবে না। অতএব সকল প্রকার ওজর-আপত্তি ত্যাগ করে বিদআত বর্জন করতে এবং সুন্নাহর উপর আমল করতে আল্লাহ আমাদের তওফীক দিন। আমীন।"
            ));

            // 13. কাযা নামাযের জন্য আযান
            list.add(new AzanItem(
                    "কাযা নামাযের জন্য আযান",
                    "মসজিদে কেউ আযান না দিলে এবং কাযা হলে অসময়েও আযান-ইকামতের বিধান... [মুসলিম, আহমদ]",
                    "মসজিদে কেউ আযান না দিলে এবং শহরে বা গ্রামে থাকতে সকলের নামায কাযা হলে অথবা সফরে পুরো জামাআতের বা একাকীর নামায কাযা হলে অসময়েও আযান-ইকামত দিয়ে নামায পড়া কর্তব্য।\n\n" +
                            "একদা মহানবী (ﷺ) সাহাবাসহ্ সফরে থাকাকালীন তাঁদের ফজরের নামায কাযা হয়ে যায়। সূর্য ওঠার পর তেজ হয়ে এলে ঐ স্থান ত্যাগ করে অন্য স্থানে গিয়ে বিলাল (রাঃ) আযান দেন। অতঃপর যথা নিয়মে ফজরের নামায আদায় করেন।\n\n" +
                            "[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]\n\n" +
                            "যেমন খন্দকের যুদ্ধের সময় একদা সকলের চার ওয়াক্তের নামায ক্বাযা হলে, এশার পর আযান দিয়ে যোহ্র, আসর, মাগরিব ও এশার নামায আদায় করেছিলেন।\n\n" +
                            "[আহমাদ, মুসনাদ প্রমুখ, ইর: ১/২৫৭]"
            ));

            // 14. সময় পার হলে আযান
            list.add(new AzanItem(
                    "সময় পার হলে আযান",
                    "নামাযের সময় বাকী থাকলে এবং আযানের যথা ...",
                    "নামাজের নির্ধারিত সময় পার হয়ে যাওয়ার পর কাজা আদায়ের ক্ষেত্রে যদি জামাতে পড়া হয়, তবে সেখানে আযান দেওয়া মুস্তাহাব।\n\n" +
                            "তবে একাকী আদায়ের ক্ষেত্রে ইকামত দেওয়াই যথেষ্ট। মসজিদে ইতিমধ্যে ওয়াক্তের আযান হয়ে থাকলে পরবর্তীতে ঘরে বা অন্য স্থানে কাজা পড়ার সময় উচ্চৈঃস্বরে আযান দেওয়া আবশ্যক নয়, তবে ইকামত দিয়ে পড়া উত্তম।\n\n" +
                            "ফাতওয়ায়ে শামী, ফিকহুস সুন্নাহ"
            ));

            // 15. খাস মহিলা মহলে মহিলাদের আযান ও ইকামত
            list.add(new AzanItem(
                    "খাস মহিলা মহলে মহিলাদের আযান ও ইকামত",
                    "মহিলাদের আযান-ইকামত সংক্রান্ত আসার, বাইহাকীর বর্ণনা ও তাহক্বীক্ব... [আলবানী, রওযাতুন নাদিয়্যাহ্]",
                    "হযরত আয়েশা (রাঃ) এর আযান ও ইকামত দেওয়ার ব্যাপারে বর্ণিত হাদীস সহীহ নয়। অবশ্য বাইহাকীতে আছে, আম্র বিন আবী সালামাহ্ বলেন, আমি সওবানকে জিজ্ঞাসা করলাম যে, ‘মেয়েরা কি ইকামত দিতে পারে?’ উত্তরে তিনি তাঁর পিতা হতে বর্ণনার কথা উল্লেখ করে বললেন, ‘মকহুল বলেছেন, যদি মহিলারা আযান-ইকামত দেয় তবে তা আফযল। আর যদি শুধু ইকামত দেয়, তবে তাও যথেষ্ট।’ সওবান বলেন, যুহ্রী উরওয়া হতে এবং তিনি হযরত আয়েশা (রাঃ) হতে বর্ণনা করেছেন যে, তিনি (আয়েশা) বলেছেন, ‘আমরা বিনা ইকামতেই নামায পড়তাম।\n\n" +
                            "ইমাম বাইহাকী বলেন, প্রথমোক্ত আসারের সাথে -যদি এই আসার সহীহ হয় তাহলে উভয়ের মধ্যে- পরস্পর বিরোধিতা নেই। কারণ, হতে পারে যে, জায়েয বর্ণনার উদ্দেশ্যে তিনি উভয় প্রকারের আমল (কখনো এরুপ, কখনো ঐরুপ) করেছেন। আর আল্লাহই অধিক জানেন।’\n\n" +
                            "আল্লামা আলবানী বলেন, এ ব্যাপারে সঠিক অভিমত হল নবাব সিদ্দীক হাসান খানের; তিনি বলেছেন, আর প্রকাশ যে, মহিলারা আমলে পুরুষদের মতই। কারণ, মহিলারা পুরুষদের সহোদরা। পুরুষদেরকে যা করতে আদেশ হয়, সে আদেশ মহিলাদের উপরেও বর্তায়। পক্ষান্তরে তাদের পক্ষে আযান-ইকামত ওয়াজেব না হওয়ার ব্যাপারে কোন গ্রহণযোগ্য দলীল নেই। আযান না থাকার ব্যাপারে বর্ণিত হাদীসের সনদের কিছু বর্ণনাকারী পরিত্যক্ত; যাদের হাদীস দলীলযোগ্য নয়। সুতরাং মহিলাদেরকে সাধারণ এ নির্দেশ থেকে খারিজ করার মত কোন নির্ভরযোগ্য দলীল থাকলে উত্তম; নচেৎ ওরাও পুরুষদের মতই।\n\n" +
                            "[আর-রওযাতুন নাদিয়্যাহ্ ১/৭৯, সিলসিলাহ যায়ীফাহ, আলবানী ২/২৭১]"
            ));

            // 16. সন্তান ভূমিষ্ঠ হলে আযান
            list.add(new AzanItem(
                    "সন্তান ভূমিষ্ঠ হলে আযান",
                    "নবজাতকের কানে আযানের হাদীস, হুকুম ও জাল হাদীসের তাহক্বীক্ব... [আবূদাঊদ, তিরমিযী, আলবানী]",
                    "আবূ রাফে (রাঃ) বলেন, আমি আল্লাহর রসূল (ﷺ) কে দেখেছি, ফাতেমা (রাঃ) হাসান বিন আলীকে প্রসব করলে তিনি তাঁর (হাসানের) কানে নামাযের আযান দিলেন।\n\n" +
                            "[আবূদাঊদ, সুনান ৫১০৫, তিরমিযী, সুনান ১৫৬৬, মিশকাত ৪১৫৭ নং]\n\n" +
                            "সুতরাং ছেলে-মেয়ে সকলের কানে ঐ সময় নামাযের জন্য আযান দেওয়ার মতই আযান দেওয়া সুন্নত। (মতান্তরে হাদীসটি যয়ীফ, অতএব এ সময় আযান সুন্নত নয়।) পক্ষান্তরে ডান কানে আযান এবং বাম কানে ইকামত দিলে ‘উম্মুস সিবয়্যান (ভূত,পেত) বা এক প্রকার রোগ কোন ক্ষতি করতে না পারারহাদীসটি জাল।\n\n" +
                            "[সিলসিলাহ যায়ীফাহ, আলবানী ৩২১নং, জামে ৫৮৮১, ইরওয়াউল গালীল, আলবানী ১১৭৪নং]"
            ));

            // 17. জিন-ভূতের ভয়ে আযান
            list.add(new AzanItem(
                    "জিন-ভূতের ভয়ে আযান",
                    "শয়তান জিন ভয় দেখালে আযান দিলে পলায়নের হাদীস ও সুহাইলের ঘটনা... [মুসলিম ৩৮৯নং]",
                    "শয়তান জিন মানুষকে ভয় দেখায়। ভয় পেয়ে আযান দিলে জিন বা শয়তান বা ভূত সব পালিয়ে যায়।\n\n" +
                            "সুহাইল বলেন, একদা আমার আব্বা আমাকে বনী হারেসায় পাঠান। আমার সঙ্গে ছিল এক সঙ্গী। এক বাগান হতে কে যেন নাম ধরে আমার সঙ্গীকে ডাক দিল। আমার সঙ্গী বাগানে খুঁজে দেখল; কিন্তু কাউকে দেখতে পেল না। ফিরে এলে আব্বার নিকট সে কথা উল্লেখ করলাম। আব্বা বললেন, যদি জানতাম যে, তুমি এই দেখতে পাবে, তাহলে তোমাকে পাঠাতাম না। তবে শোন! যখন (এই ধরনের) কোন শব্দ শুনবে, তখন নামাযের মত আযান দিও। কারণ, আমি আবূ হুরাইরা (রাঃ) কে আল্লাহর রসূল (ﷺ) হতে হাদীস বর্ণনা করতে শুনেছি, তিনি বলেছেন, “নামাযের আযান দেওয়া হলে শয়তান পাদতে পাদতে পালিয়ে যায়!”\n\n" +
                            "[মুসলিম, সহীহ ৩৮৯নং]"
            ));
        } else {
            // English Versions of all 17 items
            list.add(new AzanItem(
                    "Meaning of Adhan",
                    "Allah is Almighty, I testify that there is no god but Allah...",
                    "Allah is Almighty\n\n" +
                            "I testify that there is no god but Allah\n\n" +
                            "I testify that Muhammad is the Messenger sent by Allah\n\n" +
                            "Come to prayer\n\n" +
                            "Come to success\n\n" +
                            "Allah is the Greatest\n\n" +
                            "There is no deity except Allah\n\n" +
                            "As-Salatu Khayrum Minan-Nawm - \"Prayer is better than sleep\" (Only during Fajr prayer)"
            ));

            list.add(new AzanItem(
                    "Meaning of Iqamah",
                    "Allah is Almighty, I testify that there is no god but Allah...",
                    "Allah is Almighty\n\n" +
                            "I testify that there is no god but Allah\n\n" +
                            "I testify that Muhammad is the Messenger sent by Allah\n\n" +
                            "Come to prayer\n\n" +
                            "Come to success\n\n" +
                            "The prayer has begun\n\n" +
                            "Allah is the Greatest\n\n" +
                            "There is no deity except Allah"
            ));

            list.add(new AzanItem(
                    "Initial History of Adhan",
                    "During the stay in Makkah, Muslims prayed without Adhan. After migration to Madinah, Adhan was ordained in 1st/2nd AH...",
                    "During their time in Makkah, the Prophet (ﷺ) and Muslims offered prayers without Adhan. After migrating to Madinah, Adhan was ordained in the 1st (or 2nd) year of Hijrah.\n\n[Fath al-Bari, Ibn Hajar 2/78]\n\nA unified method was needed to call Muslims to congregate for prayer. Companions discussed using a bell like Christians or a horn like Jews. Umar (RA) suggested sending a person through streets to call, but the Prophet (ﷺ) said: 'O Bilal! Stand up and call for prayer.'\n\n[Bukhari 604, Sahih Muslim]\n\nSome suggested raising a flag over the mosque when prayer time entered, but the Prophet (ﷺ) disliked these options.\n\n[Sunan Abi Dawud 498]\n\nEventually, when a bell was ordered, companion Abdullah ibn Zayd (RA) had a true dream where a man taught him the words of Adhan and Iqamah. The Prophet (ﷺ) approved it, saying: 'In sha Allah, it is a true dream. Stand with Bilal and teach him, for his voice is louder than yours.'\n\nUpon hearing Bilal proclaim it, Umar (RA) rushed to the Prophet (ﷺ) testifying:\n\nوَالَّذِي بَعَثَكَ بِالْحَقِّ لَقَدْ رَأَيْتُ مِثْلَ مَا أَرَى فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: «فَلِلَّهِ الْحَمْدُ»\n\n'By Him Who sent you with the truth, I have seen the exact same dream!' The Prophet (ﷺ) praised Allah saying 'Alhamdulillah.'\n\n[Sunan Abi Dawud 495; Mishkat 650]\n\nAccording to reports, 11 companions saw the same dream that night [Mirqat Sharh Mishkat 2/149]. Umar (RA) had seen it 20 days prior but had felt shy to mention it before Abdullah spoke [Sunan Abi Dawud 494]."
            ));

            list.add(new AzanItem(
                    "Significance of Adhan",
                    "Adhan is Fard al-Kifayah and a major symbol of Islam... [Bukhari, Muslim, Quran 41:33]",
                    "Adhan is an obligation and prescribed as Fard al-Kifayah (communal obligation). The Messenger of Allah (ﷺ) said: 'When the time for prayer arrives, let one of you proclaim the Adhan, and let the oldest among you lead the prayer.'\n\n[Bukhari 628, Muslim, Nasa'i, Darimi]\n\nAdhan is a prominent symbol and landmark of Islam. If the residents of a town abandon it, the Muslim authority is to take action. When the Prophet (ﷺ) raided, he would withhold attacking if he heard the Adhan from a locality.\n\n[Bukhari 610, Sahih Muslim]\n\nIf one is traveling alone or far from any mosque where Adhan cannot be heard, it is Sunnah to call Adhan and Iqamah individually before praying.\n\n[Fatawa Islamiyyah, Saudi Scholars Committee 1/255]\n\nImmense reward is promised for the Mu'adhin. Allah Almighty says: 'And who is better in speech than one who invites to Allah and does righteousness and says, Indeed, I am of the Muslims?'\n\n[Al-Quran 41:33]\n\nThe Prophet (ﷺ) said: 'If people knew what reward there is in the Adhan and the first row, and found no other way to get it except by drawing lots, they would draw lots.'\n\n[Bukhari 615, Sahih Muslim 437]\n\nAllah sends mercy upon the first row and angels pray for forgiveness for them. The Mu'adhin is forgiven to the extent of his voice. Everything moist or dry that hears it confirms his words, and he receives reward equivalent to all who pray with him.\n\n[Ahmad, Nasa'i, Sahih at-Targhib 228]\n\nOn the Day of Resurrection, the Mu'adhins will have the longest necks (holding highest distinction and honor).\n\n[Sahih Muslim 387]\n\nWhoever calls Adhan for twelve years, Paradise becomes guaranteed for him. Every day sixty good deeds are written for his Adhan and thirty for his Iqamah.\n\n[Ibn Majah, Daraqutni, Hakim, Sahih at-Targhib 240]\n\nEvery human, jinn, or creature that hears the voice of the Mu'adhin will bear witness for him on the Day of Judgment.\n\n[Bukhari 609]"
            ));

            list.add(new AzanItem(
                    "Adhan in Places Other Than Mosques",
                    "Calling Adhan in remote wilderness, during travel, or when far from mosques... [Bukhari, Muslim, Abu Dawud]",
                    "If prevented from going to the mosque due to fear, hostility, or extreme distance where the call cannot be heard, or when in an open wilderness during travel, one should proclaim the Adhan and Iqamah wherever they are. Even if praying alone, calling Adhan is an established Sunnah.\n\nThe Prophet (ﷺ) said: 'When you are on a journey, proclaim the Adhan and Iqamah, and let the oldest among you lead the prayer.'\n\n[Bukhari, Mishkat 682]\n\nThe Prophet (ﷺ) and his companions regularly called Adhan and prayed in open fields during travel.\n\n[Sahih Muslim 681]\n\nThe Prophet (ﷺ) said: 'Your Lord is amazed by a shepherd high on a mountain peak who calls the Adhan and performs prayer. Allah Almighty says: Look at this servant of Mine, calling the Adhan and establishing prayer out of fear of Me; I have forgiven him and admitted him into Paradise.'\n\n[Sunan Abi Dawud, Nasa'i, Sahih at-Targhib 239]\n\nHe also said: 'When a person is in an uninhabited, barren wilderness and prayer time arrives, let him make Wudu (or Tayammum if no water is found). If he only offers Iqamah, his two companion angels pray with him. But if he calls both Adhan and Iqamah, countless rows of Allah's angels pray behind him, spanning beyond the horizon!'\n\n[Musannaf Abdur Razzaq, Sahih at-Targhib 241]\n\nHe also commanded Abdullah ibn Abdur-Rahman to raise his voice with Adhan when grazing flocks in the desert.\n\n[Bukhari, Mishkat 656]"
            ));

            list.add(new AzanItem(
                    "Leaving the Mosque After Adhan",
                    "Prohibition of leaving the mosque after Adhan without valid excuse... [Ahmad, Muslim, Ibn Majah]",
                    "Once the Adhan has been proclaimed, it is not permissible to leave the mosque without a valid excuse before offering the prayer.\n\n" +
                            "The Prophet (ﷺ) said: 'When the Adhan is called while any of you is in the mosque, let none of you leave until he has prayed.'\n\n" +
                            "[Musnad Ahmad, Mishkat 1074]\n\n" +
                            "When a man walked out of the mosque after the Adhan had been proclaimed, Abu Hurairah gestured towards him and said: 'As for this man, he has disobeyed Abul Qasim (ﷺ).'\n\n" +
                            "[Sahih Muslim, Sunan Abi Dawud 536, Tirmidhi, Ibn Majah, Darimi, Bayhaqi]\n\n" +
                            "The Messenger of Allah (ﷺ) said: 'Whoever is in the mosque when the call to prayer is proclaimed, then leaves without a necessity and with no intention of returning, is a hypocrite (Munafiq).'\n\n" +
                            "[Sunan Ibn Majah, Sahih at-Targhib 157]"
            ));

            list.add(new AzanItem(
                    "Interval Between Adhan & Iqamah",
                    "Interval duration, time for Wudu, Sunnah prayers and Maghrib Sunnah before congregation... [Bukhari, Muslim]",
                    "There is no explicit specification in the authentic Hadith regarding the exact duration of the interval between Adhan and Iqamah. However, Adhan is called to invite people to the congregational prayer. It is natural that many will perform ablution (Wudu) after the Adhan. Therefore, sufficient time must be given for performing Wudu. Furthermore, time must also be provided to perform the Sunnah Ratibah (regular confirmed Sunnah prayers) before the obligatory (Fard) prayer. The Prophet (ﷺ) said: 'Between every two calls (Adhan and Iqamah) there is a prayer.' He repeated this three times, and on the third time added: 'For whoever wishes.'\n\n" +
                            "[Sahih Bukhari, Sahih Muslim, Mishkat 662]\n\n" +
                            "Even after the Maghrib Adhan, the congregation should not be rushed immediately. Although the time is relatively narrow, there is still prayer before the congregational prayer begins. Therefore, whoever wishes to offer that prayer should be given sufficient time to pray.\n\n" +
                            "Anas (RA) narrated: We were in Madinah. When the Mu'adhin called the Adhan for Maghrib, the people would rush eagerly to the pillars of the mosque to pray two Rak'ahs (Sunnah), so much so that if a stranger entered the mosque, seeing so many people praying, he would think that the congregational Maghrib prayer had already taken place (and they were offering the post-Fard Sunnah).\n\n" +
                            "[Sahih Muslim, Mishkat 1180]"
            ));

            list.add(new AzanItem(
                    "Dua Between Adhan & Iqamah",
                    "Acceptance of supplication, prophetic command, and moments when dua is not rejected... [Ahmad, Abu Dawud, Hakim]",
                    "Supplications are answered in the time between the Adhan and before the Iqamah. Therefore, one should ask Allah for good in this world and the Hereafter during this time. The Prophet (ﷺ) said: 'Supplication made between the Adhan and the Iqamah is not rejected (i.e. it is granted).'\n\n" +
                            "[Musnad Ahmad, Sunan Abi Dawud 521, Jami at-Tirmidhi]\n\n" +
                            "In another narration: 'So supplicate during this time.'\n\n" +
                            "[Al-Jami' 3405]\n\n" +
                            "He also said: 'In two moments the prayer of a supplicant is never rejected: when the Iqamah for prayer is called, and in the battlefield row of Jihad.'\n\n" +
                            "[Mustadrak al-Hakim, Muwatta Malik, Sahih at-Targhib 260]"
            ));

            list.add(new AzanItem(
                    "The Iqamah",
                    "Forms of Iqamah, famous wording, multiple authentic variations, and praying without Iqamah... [Bukhari, Muslim, Abu Dawud]",
                    "Just as the Adhan of the two Mu'adhins differed, so did their Iqamah; even and odd. Bilal (RA) was commanded to call the phrases of Adhan in pairs (doubled) and the phrases of Iqamah singly (odd), except for 'Qad Qamatis-Salah'.\n\n" +
                            "[Sahih Bukhari, Sahih Muslim, Mishkat 641]\n\n" +
                            "Therefore, based on Bilal's hadith, the Iqamah consists of 9 phrases: 'Qad Qamatis-Salah' twice, and the rest once each.\n\n" +
                            "[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 2/59]\n\n" +
                            "However, the Iqamah taught to Abdullah ibn Zayd in his dream is as follows, and this is the most widespread and well-known:-\n\n" +
                            "اَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله، أَشْهَدُ أَنَّ مُحَمَّداً رَّسُوْلُ الله، حَيَّ عَلَى الصَّলাَة،\n\n" +
                            "حَيَّ عَلَى الْفَلاَح، قَدْ قَامَتِ الصَّলাَة، قَدْ قَامَتِ الصَّলাَة، اَللهُ أَكْبَر اَللهُ أَكْبَر، لاَ إِلهَ إِلاَّ الله،\n\n" +
                            "Allahu Akbar 2 times. Ash-hadu alla ilaha illallah 1 time. Ash-hadu anna Muhammadar Rasulullah 1 time. Hayya 'alas-Salah 1 time. Hayya 'alal-Falah 1 time. Qad Qamatis-Salah (prayer is established/started) 2 times. Allahu Akbar 2 times, and La ilaha illallah 1 time.\n\n" +
                            "[Sunan Abi Dawud 499, Sunan ad-Darimi 1171, Sahih Ibn Khuzaymah 370, Sahih Ibn Hibban 1671, Bayhaqi 1/391]\n\n" +
                            "It is noteworthy that those who call the Tarjee' Adhan like Mu'adhin Abu Mahdhurah should also call the Iqamah like his. He reported that the Prophet (ﷺ) taught him 19 phrases for Adhan and 17 phrases for Iqamah.\n\n" +
                            "[Musnad Ahmad, Sunan Abi Dawud, Jami at-Tirmidhi, Sunan an-Nasa'i, Sunan Ibn Majah, Sunan ad-Darimi, Mishkat 644]\n\n" +
                            "Thus, his Iqamah was similar to Bilal's Adhan, except that after 'Hayya 'alal-Falah', it included 'Qad Qamatis-Salah' twice.\n\n" +
                            "[Sunan Abi Dawud 502]\n\n" +
                            "Imam Ibn Taymiyyah (RA) stated: 'The correct view among the Ahl al-Hadith and their supporters is that whatever has been authentically transmitted from the Prophet (ﷺ) should be acted upon, and they do not dislike any of those practices. This is because the existence of multiple methods for Adhan and Iqamah is like the multiple authentic methods of recitation (Qira'at) and Tashahhud.'\n\n" +
                            "[Majmu' al-Fatawa 22/335, 22/66]\n\n" +
                            "Therefore, both types of Adhan and Iqamah are valid and actionable, and disputes or hostility regarding them are not permissible.\n\n" +
                            "It should be noted that if someone mistakenly offers prayer (individually or in congregation) without calling the Iqamah, the validity of the prayer is not affected. Iqamah is distinct from the prayer itself; hence Sajdah Sahw is not required for this omission.\n\n" +
                            "[Tuhfat al-Ikhwan, Ibn Baz p. 78]"
            ));

            list.add(new AzanItem(
                    "Responding to Iqamah",
                    "Iqamah as the second Adhan, manner of responding, and scholarly analysis... [Muslim, Fatawa Islamiyyah, Albani]",
                    "Iqamah is called the second Adhan, so Iqamah is also a form of Adhan.\n\n" +
                            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/249]\n\n" +
                            "Therefore, its response is just like that of the Adhan. Of course, in response to 'Hayya 'alas-Salah' and 'Hayya 'alal-Falah', one should say 'La hawla wa la quwwata illa billah', and at the end (if time permits), reciting Salawat (Durood) and the Dua of Wasilah is prescribed. For in the authentic Hadith, we are commanded to repeat after the Mu'adhin (the same as what he says) and to recite Durood and the Dua of Wasilah at the end.\n\n" +
                            "[Sahih Muslim, Mishkat 657]\n\n" +
                            "Based on this hadith, in response to 'Qad Qamatis-Salah', one should say 'Qad Qamatis-Salah' itself. The hadith mentioning 'Aqamahallahu wa adamaha' as a response is not authentic. And no act of worship (Ibadah) or ruling in Shariah is valid based on a weak (Da'if) hadith.\n\n" +
                            "[Mishkat, Albani's Footnotes 1/121]\n\n" +
                            "According to an alternative scholarly opinion, since there is no explicit authentic hadith regarding responding to the Iqamah, responding to the Iqamah is not Sunnah."
            ));

            list.add(new AzanItem(
                    "Interval Between Iqamah and Starting Prayer",
                    "Not standing until seeing the Imam, maintaining tranquility, necessary delay and speaking... [Bukhari, Fatawa Islamiyyah]",
                    "The Messenger of Allah (ﷺ) said: 'When the Iqamah for prayer is called, do not stand (for prayer) until you see me.'\n\n" +
                            "[Sahih Bukhari 637]\n\n" +
                            "Likewise, one should not rush hurriedly upon hearing the Iqamah, for in a narration of this hadith he said: 'You must maintain tranquility and composure.' For in the court of the King of Kings, no noise, commotion, or hasty rushing is appropriate. It is needless to say that in this court there should be utter decorum, deep humility, serenity, and calmness.\n\n" +
                            "Humaid reported: I asked Thabit al-Bunani regarding the permissibility of speaking after the Iqamah, and he narrated the hadith of Anas (RA): 'Once the Iqamah for prayer had been called, a man detained the Prophet (ﷺ) before he entered into prayer.'\n\n" +
                            "[Sahih Bukhari 643]\n\n" +
                            "He spoke to him privately at one corner of the mosque until the worshippers began dozing off due to sleep. Once, after the Iqamah had been called and the worshippers had straightened their rows, the Messenger of Allah (ﷺ) came out from his chamber to lead the prayer. When he reached the place of leading prayer, he remembered that he had not performed the obligatory bath (Ghusl for purification). He said to everyone: 'Remain standing in your places.' He then returned to his chamber, took a bath, and emerged with water droplets falling from his head. He then led the congregation in prayer.\n\n" +
                            "From this hadith it is understood that if there is a necessary interval of time between Iqamah and prayer, it does not harm the validity of prayer, nor does the Iqamah need to be repeated.\n\n" +
                            "After the Iqamah, it is permissible to speak regarding necessary matters, or matters related to prayer and rows. However, once preparation for prayer is underway, one should not engage in worldly conversation.\n\n" +
                            "[Fatawa Islamiyyah, Saudi Scholars Committee 1/251]\n\n" +
                            "When the Iqamah starts and the Imam is present, everyone may stand up for prayer according to their convenience. Standing at the beginning, middle, or end of the Iqamah is completely permissible. However, one should ensure not to miss the opening Takbir (Takbir at-Tahrimah) with the Imam.\n\n" +
                            "[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 3/10]"
            ));

            list.add(new AzanItem(
                    "Adhan for Tahajjud and Sahur",
                    "Adhan before Fajr, prophetic evidence, and adhering to Sunnah... [Bukhari, Muslim]",
                    "The Prophet (ﷺ) said: 'Bilal calls the Adhan during the night (before Fajr). Therefore, continue eating and drinking until Ibn Umm Maktum calls the (Fajr) Adhan.'\n\n" +
                            "[Sahih Bukhari, Sahih Muslim, Mishkat 680]\n\n" +
                            "This hadith proves that calling the Adhan before Fajr for Tahajjud and Sahur was practiced during the era of the Prophet (ﷺ), and to this day this Sunnah is heard throughout Saudi Arabia, including Makkah and Madinah (especially during Ramadan). In contrast, in our countries, instead of the Adhan at that time, recitations of the Quran and singing of Ghazals/nasheeds are often heard! Therefore, it can be said without doubt that man-made innovations (Bid'ah) have taken the place of the Sunnah.\n\n" +
                            "Some people argue that if Adhan is called at both times, people will get confused as to whether it is the Sahur Adhan or the Fajr Adhan. However, having two distinct Mu'adhins designated for the two different times eliminates any risk of confusion. Furthermore, the Sahur Adhan does not contain the phrase:\n\n" +
                            "الصَّلاَةُ خَيْرٌ مِّنَ النَّوْم\n\n" +
                            "(Prayer is better than sleep). Therefore, may Allah grant us the ability to abandon all excuses, shun innovations, and practice upon the authentic Sunnah. Ameen."
            ));

            list.add(new AzanItem(
                    "Adhan for Missed (Qada) Prayers",
                    "Calling Adhan and Iqamah at unscheduled times for missed prayers... [Muslim, Ahmad]",
                    "If no one calls the Adhan in the mosque and everyone in a town or village misses their prayer, or if the entire congregation or an individual misses their prayer during travel, it is a duty to proclaim Adhan and Iqamah even at an unscheduled time before offering the prayer.\n\n" +
                            "Once, while traveling with the Companions, the Prophet (ﷺ) and they missed the Fajr prayer due to sleep. After sunrise, when the sun became warm, they departed from that location to another place where Bilal (RA) called the Adhan, and they then prayed the Fajr prayer according to the established manner.\n\n" +
                            "[Sahih Muslim 681]\n\n" +
                            "Similarly, during the Battle of the Trench (Khandaq), when everyone missed four daily prayers, they called the Adhan after Isha time and sequentially performed Dhuhr, Asr, Maghrib, and Isha prayers.\n\n" +
                            "[Musnad Ahmad, Irwa al-Ghalil 1/257]"
            ));

            list.add(new AzanItem(
                    "Adhan After Prayer Time Has Expired",
                    "Rules regarding Adhan for late prayers...",
                    "If offering missed prayers in a separate gathering, calling Adhan is recommended for congregation, while Iqamah is sufficient for an individual.\n\n" +
                            "Fatawa Shami, Fiqh us-Sunnah"
            ));

            list.add(new AzanItem(
                    "Adhan & Iqamah for Women in Exclusive Gatherings",
                    "Scholarly research regarding women proclaiming Adhan & Iqamah... [Albani, Rawdatun Nadiyyah]",
                    "The hadith narrated regarding Aisha (RA) calling Adhan and Iqamah is not authentic (Sahih). However, in al-Bayhaqi, Amr ibn Abi Salamah said: I asked Thawban, 'Can women proclaim Iqamah?' In reply, referring to a narration from his father, he said: 'Makhul said, if women call both Adhan and Iqamah it is more virtuous (Afdal). And if they only call Iqamah, that is also sufficient.' Thawban said: Zuhri narrated from Urwah, and he from Aisha (RA) that she (Aisha) said: 'We used to pray without Iqamah.'\n\n" +
                            "Imam al-Bayhaqi states: 'There is no contradiction between this and the aforementioned narration—if both narrations are authentic. This is because it is possible that she practiced both ways (sometimes this way, sometimes that way) to demonstrate permissibility. And Allah knows best.'\n\n" +
                            "Allamah al-Albani says: 'The correct opinion in this regard is that of Nawab Siddiq Hasan Khan; who said: And it is apparent that women in deeds are like men. Because women are counterparts of men. Whatever men are commanded to do, that command applies to women as well. Conversely, there is no acceptable evidence that Adhan and Iqamah are not obligatory upon them. In the chains of narrations denying Adhan for women, some narrators are abandoned (Matruk) whose reports cannot be used as evidence. Therefore, if there is reliable evidence excluding women from this general command, that is fine; otherwise, they are just like men.'\n\n" +
                            "[Ar-Rawdatun Nadiyyah 1/79, Silsilah Da'ifah, al-Albani 2/271]"
            ));

            list.add(new AzanItem(
                    "Adhan Upon the Birth of a Child",
                    "Hadith on calling Adhan for newborns and research on fabricated reports... [Abu Dawud, Tirmidhi, Albani]",
                    "Abu Rafi (RA) said: I saw the Messenger of Allah (ﷺ) give the call to prayer (Adhan) in the ear of Hasan ibn Ali when Fatimah gave birth to him.\n\n" +
                            "[Sunan Abi Dawud 5105, Jami at-Tirmidhi 1566, Mishkat 4157]\n\n" +
                            "Therefore, calling Adhan in the ear of both boys and girls at that time just like the regular call to prayer is Sunnah. (According to other scholars, this hadith is Da'if/weak, hence calling Adhan at this time is not Sunnah.) On the other hand, the narration stating that calling Adhan in the right ear and Iqamah in the left ear protects the child from Ummus-Sibyan (evil spirit/affliction) is fabricated (Mawdu').\n\n" +
                            "[Silsilah Da'ifah, al-Albani 321, Al-Jami' 5881, Irwa al-Ghalil, al-Albani 1174]"
            ));

            list.add(new AzanItem(
                    "Adhan When Frightened by Spirits or Jinn",
                    "Proclaiming Adhan to repel evil jinn and devils... [Sahih Muslim 389]",
                    "Devil jinn frighten humans. When someone becomes afraid and proclaims the Adhan, jinn, devils, or evil spirits all flee away.\n\n" +
                            "Suhail said: Once my father sent me to Banu Harithah. With me was a companion. From an orchard, someone called out my companion's name. My companion searched the garden, but could see no one. When we returned, I mentioned this to my father. My father said: 'If I had known you would experience this, I would not have sent you. But listen! Whenever you hear any such sound, proclaim the Adhan just like the call to prayer. For I heard Abu Hurairah (RA) narrating from the Messenger of Allah (ﷺ) that he said: When the call to prayer (Adhan) is proclaimed, Satan flees while breaking wind!'\n\n" +
                            "[Sahih Muslim 389]"
            ));
        }

        return list;
    }
}
