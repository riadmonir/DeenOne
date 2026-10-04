package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemNamazShikhaMenuCardBinding;
import com.devflux.deenone.databinding.PageNamazShikhaMenuBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class NamazShikhaMenuPageDialog {

    public static class ChapterItem {
        final String title;
        final String topicId;

        public ChapterItem(String title, String topicId) {
            this.title = title;
            this.topicId = topicId;
        }
    }

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageNamazShikhaMenuBinding binding = PageNamazShikhaMenuBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "নামাজ শিক্ষা" : "Salah Guide");

        // Back Button with Spring Touch
        binding.btnBackNamazShikhaMenu.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackNamazShikhaMenu);

        // 7 Chapters with dual language support
        List<ChapterItem> chapters = getChapters(isBn);

        ChapterAdapter adapter = new ChapterAdapter(activity, chapters);
        binding.rvNamazShikhaChapters.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvNamazShikhaChapters.setAdapter(adapter);

        // Settings Gear
        binding.btnSettingsNamazShikhaMenu.setOnClickListener(v -> {
            showSettingsDialog(activity, chapters, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsNamazShikhaMenu);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<ChapterItem> chapters, boolean isBn) {
        String[] options = {
                isBn ? "অধ্যায়সমূহের সূচিপত্র কপি করুন" : "Copy Table of Contents",
                isBn ? "নামাজ শিক্ষা শেয়ার করুন" : "Share Salah Guide"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "নামাজ শিক্ষা অপশনস" : "Salah Guide Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== নামাজ শিক্ষা সূচিপত্র ===\n\n" : "=== Salah Guide Contents ===\n\n");
                        for (int i = 0; i < chapters.size(); i++) {
                            sb.append((i + 1)).append(". ").append(chapters.get(i).title).append("\n");
                        }
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "নামাজ শিক্ষা সূচিপত্র" : "Salah Guide Contents", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "সূচিপত্র ক্লিপবোর্ডে কপি করা হয়েছে" : "Table of contents copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "📖 নামাজ শিক্ষা — দ্বীনওয়ান\n\n" : "📖 Salah Guide — DeenOne\n\n");
                        sb.append(isBn ? "সালাতের নিয়ম, প্রস্তুতি, আরকান-আহকাম ও মাসআলা শিখুন অত্যন্ত সহজ ভাষায়।\n\n" : "Learn the rules, preparation, essentials, and etiquettes of Salah in simple words.\n\n");
                        for (int i = 0; i < chapters.size(); i++) {
                            sb.append("• ").append(chapters.get(i).title).append("\n");
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "নামাজ শিক্ষা" : "Salah Guide");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
                    }
                })
                .show();
    }

    public static class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
        private final Activity activity;
        private final List<ChapterItem> items;

        public ChapterAdapter(Activity activity, List<ChapterItem> items) {
            this.activity = activity;
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemNamazShikhaMenuCardBinding binding = ItemNamazShikhaMenuCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChapterItem item = items.get(position);
            holder.binding.tvChapterTitle.setText(item.title);

            holder.binding.cardContainer.setOnClickListener(v -> {
                if ("salah_basics".equals(item.topicId)) {
                    SalahBasicsPageDialog.show(activity);
                } else if ("salah_preparation".equals(item.topicId)) {
                    SalahPreparationPageDialog.show(activity);
                } else if ("salah_essential_rules".equals(item.topicId)) {
                    SalahEssentialRulesPageDialog.show(activity);
                } else if ("salah_etiquettes".equals(item.topicId)) {
                    SalahEtiquettesPageDialog.show(activity);
                } else if ("salah_virtues".equals(item.topicId)) {
                    SalahVirtuesPageDialog.show(activity);
                } else if ("salah_nafl_timings".equals(item.topicId)) {
                    SalahNaflTimesPageDialog.show(activity);
                } else if ("salah_faq".equals(item.topicId)) {
                    SalahFaqPageDialog.show(activity);
                } else if ("salah_jumuah_rules".equals(item.topicId)) {
                    SalahJumuahRulesPageDialog.show(activity);
                } else if ("salah_fard_method".equals(item.topicId)) {
                    SalahFardStepByStepPageDialog.show(activity);
                } else if ("salah_jumuah_steps".equals(item.topicId)) {
                    SalahJumuahStepsPageDialog.show(activity);
                } else if ("salah_ishara_method".equals(item.topicId)) {
                    SalahIsharaStepsPageDialog.show(activity);
                } else if ("salah_taraweeh_rules".equals(item.topicId)) {
                    SalahTaraweehRulesPageDialog.show(activity);
                } else if ("salah_tahajjud_rules".equals(item.topicId)) {
                    SalahTahajjudRulesPageDialog.show(activity);
                } else if ("salah_general_steps".equals(item.topicId)) {
                    SalahGeneralStepsPageDialog.show(activity);
                } else if ("salah_eclipse_rules".equals(item.topicId)) {
                    SalahEclipseRulesPageDialog.show(activity);
                } else if ("salah_istisqa_rules".equals(item.topicId)) {
                    SalahIstisqaRulesPageDialog.show(activity);
                } else if ("salah_istikhara_rules".equals(item.topicId)) {
                    SalahIstikharaRulesPageDialog.show(activity);
                } else if ("salah_sick_person_rules".equals(item.topicId)) {
                    SalahSickPersonRulesPageDialog.show(activity);
                } else if ("salah_sitting_rules".equals(item.topicId)) {
                    SalahSittingRulesPageDialog.show(activity);
                } else if ("salah_ishara_rules".equals(item.topicId)) {
                    SalahIsharaRulesPageDialog.show(activity);
                } else if ("salah_travel_rules".equals(item.topicId)) {
                    SalahTravelRulesPageDialog.show(activity);
                } else if ("salah_hajat_rules".equals(item.topicId)) {
                    SalahHajatRulesPageDialog.show(activity);
                } else if ("salah_tawbah_rules".equals(item.topicId)) {
                    SalahTawbahRulesPageDialog.show(activity);
                } else if ("salah_lying_bed_rules".equals(item.topicId)) {
                    SalahLyingBedRulesPageDialog.show(activity);
                } else if ("salah_lying_bed_steps".equals(item.topicId)) {
                    SalahLyingBedStepsPageDialog.show(activity);
                } else if ("salah_witr_niyyah".equals(item.topicId)) {
                    SalahWitrNiyyahPageDialog.show(activity);
                } else if ("salah_azan_meaning".equals(item.topicId)) {
                    SalahAzanMeaningPageDialog.show(activity);
                } else if ("salah_iqamah_meaning".equals(item.topicId)) {
                    SalahIqamahMeaningPageDialog.show(activity);
                } else if ("salah_azan_history".equals(item.topicId)) {
                    SalahAzanHistoryPageDialog.show(activity);
                } else if ("salah_azan_significance".equals(item.topicId)) {
                    SalahAzanSignificancePageDialog.show(activity);
                } else if ("salah_azan_outside_mosque".equals(item.topicId)) {
                    SalahAzanOutsideMosquePageDialog.show(activity);
                } else if ("salah_azan_qada".equals(item.topicId)) {
                    SalahAzanQadaPageDialog.show(activity);
                } else if ("salah_azan_women".equals(item.topicId)) {
                    SalahAzanWomenPageDialog.show(activity);
                } else if ("salah_azan_newborn".equals(item.topicId)) {
                    SalahAzanNewbornPageDialog.show(activity);
                } else if ("salah_azan_jinn_fear".equals(item.topicId)) {
                    SalahAzanJinnFearPageDialog.show(activity);
                } else if ("salah_azan_leaving_mosque".equals(item.topicId)) {
                    SalahAzanLeavingMosquePageDialog.show(activity);
                } else if ("salah_azan_interval".equals(item.topicId)) {
                    SalahAzanIntervalPageDialog.show(activity);
                } else if ("salah_azan_dua".equals(item.topicId)) {
                    SalahAzanDuaPageDialog.show(activity);
                } else if ("salah_iqamah_rules".equals(item.topicId)) {
                    SalahIqamahRulesPageDialog.show(activity);
                } else if ("salah_iqamah_answer".equals(item.topicId)) {
                    SalahIqamahAnswerPageDialog.show(activity);
                } else if ("salah_iqamah_prayer_interval".equals(item.topicId)) {
                    SalahIqamahPrayerIntervalPageDialog.show(activity);
                } else if ("salah_azan_tahajjud_sahur".equals(item.topicId)) {
                    SalahAzanTahajjudSahurPageDialog.show(activity);
                } else {
                    SalahGenericContentDialog.show(activity, item.topicId);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ItemNamazShikhaMenuCardBinding binding;

            public ViewHolder(@NonNull ItemNamazShikhaMenuCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    private static List<ChapterItem> getChapters(boolean isBn) {
        List<ChapterItem> list = new ArrayList<>();
        list.add(new ChapterItem(isBn ? "সালাত বিষয়ক প্রাথমিক ধারণা" : "Basic Concepts of Salah", "salah_basics"));
        list.add(new ChapterItem(isBn ? "সালাতের জন্য প্রস্তুতি" : "Preparation for Salah", "salah_preparation"));
        list.add(new ChapterItem(isBn ? "সঠিক ভাবে সালাত আদায়ের জন্য যা জানা জরুরি" : "Essential Rules for Performing Salah Correctly", "salah_essential_rules"));
        list.add(new ChapterItem(isBn ? "সালাতের আদবসমূহ" : "Etiquettes of Salah", "salah_etiquettes"));
        list.add(new ChapterItem(isBn ? "পাঁচ ওয়াক্ত নামাজের ফজিলত" : "Virtues of Five Daily Prayers", "salah_virtues"));
        list.add(new ChapterItem(isBn ? "নফল সালাতের সঠিক সময়" : "Proper Timings for Nafl Salah", "salah_nafl_timings"));
        list.add(new ChapterItem(isBn ? "সালাত সম্পর্কিত সাধারণ জিজ্ঞাসা" : "Common Questions on Salah", "salah_faq"));
        list.add(new ChapterItem(isBn ? "জুম'আর সালাত আদায়ের নিয়ম" : "Rules of Jum'ah Prayer", "salah_jumuah_rules"));
        list.add(new ChapterItem(isBn ? "পাঁচ ওয়াক্ত ফরজ সালাত আদায়ের সঠিক পদ্ধতি" : "Proper Method of Five Daily Fard Prayers", "salah_fard_method"));
        list.add(new ChapterItem(isBn ? "জুম'আ নামাজ শেখার ধাপসমূহ" : "Step-by-Step Jum'ah Prayer Guide", "salah_jumuah_steps"));
        list.add(new ChapterItem(isBn ? "ইশারায় সালাত পড়ার পদ্ধতি" : "Step-by-Step Gesture Prayer Guide", "salah_ishara_method"));
        list.add(new ChapterItem(isBn ? "তারাবীর সালাত আদায়ের নিয়ম" : "Rules of Taraweeh Prayer", "salah_taraweeh_rules"));
        list.add(new ChapterItem(isBn ? "তাহাজ্জুত নামাজের ফজিলত" : "Virtues & Method of Tahajjud Prayer", "salah_tahajjud_rules"));
        list.add(new ChapterItem(isBn ? "নামাজের ধাপসমূহ" : "Step-by-Step Stages of Salah", "salah_general_steps"));
        list.add(new ChapterItem(isBn ? "চন্দ্র ও সূর্য গ্রহণের সালাত" : "Eclipse Prayer (Kusuf & Khusuf)", "salah_eclipse_rules"));
        list.add(new ChapterItem(isBn ? "বৃষ্টি প্রার্থনার সালাত" : "Rain Seeking Prayer (Istisqa)", "salah_istisqa_rules"));
        list.add(new ChapterItem(isBn ? "ইস্তেখারার সালাত আদায়ের পদ্ধতি" : "Method of Istikhara Prayer", "salah_istikhara_rules"));
        list.add(new ChapterItem(isBn ? "অসুস্থ ব্যক্তির সালাত আদায়ের পদ্ধতি" : "Method of Prayer for the Sick", "salah_sick_person_rules"));
        list.add(new ChapterItem(isBn ? "বসে বসে সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Sitting", "salah_sitting_rules"));
        list.add(new ChapterItem(isBn ? "ইশারায় সালাত আদায়ের পদ্ধতি" : "Method of Gesture Prayer", "salah_ishara_rules"));
        list.add(new ChapterItem(isBn ? "ভ্রমণ অবস্থায় সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Traveling", "salah_travel_rules"));
        list.add(new ChapterItem(isBn ? "সালাতুল হাজত আদায়ের নিয়ম" : "Rules of Salat al-Hajat (Prayer of Need)", "salah_hajat_rules"));
        list.add(new ChapterItem(isBn ? "সালাতুত তাওবা বা ইস্তিগফারের সালাত" : "Rules of Salatut Tawbah (Prayer of Repentance)", "salah_tawbah_rules"));
        list.add(new ChapterItem(isBn ? "বিছানায় শুয়ে কিভাবে পড়ব" : "How to Pray while Lying on Bed", "salah_lying_bed_rules"));
        list.add(new ChapterItem(isBn ? "বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি" : "Method of Prayer while Lying in Bed", "salah_lying_bed_steps"));
        list.add(new ChapterItem(isBn ? "বিতর নামাজের নিয়ত" : "Niyyah and Method of Witr Prayer", "salah_witr_niyyah"));
        list.add(new ChapterItem(isBn ? "আজান এর অর্থ" : "Meaning of Adhan", "salah_azan_meaning"));
        list.add(new ChapterItem(isBn ? "ইকামতের অর্থ" : "Meaning of Iqamah", "salah_iqamah_meaning"));
        list.add(new ChapterItem(isBn ? "আজানের প্রারম্ভিক ইতিহাস" : "Initial History of Adhan", "salah_azan_history"));
        list.add(new ChapterItem(isBn ? "আযান ও তার মাহাত্ম" : "Significance & Virtues of Adhan", "salah_azan_significance"));
        list.add(new ChapterItem(isBn ? "মসজিদ ছাড়া অন্য স্থানে আযান" : "Adhan in Places Other Than Mosques", "salah_azan_outside_mosque"));
        list.add(new ChapterItem(isBn ? "কাযা নামাযের জন্য আযান" : "Adhan for Missed (Qada) Prayers", "salah_azan_qada"));
        list.add(new ChapterItem(isBn ? "খাস মহিলা মহলে মহিলাদের আযান ও ইকামত" : "Adhan & Iqamah for Women in Exclusive Gatherings", "salah_azan_women"));
        list.add(new ChapterItem(isBn ? "সন্তান ভূমিষ্ঠ হলে আযান" : "Adhan Upon the Birth of a Child", "salah_azan_newborn"));
        list.add(new ChapterItem(isBn ? "জিন-ভূতের ভয়ে আযান" : "Adhan When Frightened by Spirits or Jinn", "salah_azan_jinn_fear"));
        list.add(new ChapterItem(isBn ? "আযানের পর মসজিদ থেকে বের হওয়া" : "Leaving the Mosque After Adhan", "salah_azan_leaving_mosque"));
        list.add(new ChapterItem(isBn ? "আযান ও ইকামতের মাঝে ব্যবধান" : "Interval Between Adhan and Iqamah", "salah_azan_interval"));
        list.add(new ChapterItem(isBn ? "আযান ও ইকামতের মাঝে দুআ" : "Dua Between Adhan and Iqamah", "salah_azan_dua"));
        list.add(new ChapterItem(isBn ? "ইকামত" : "Rules of Iqamah", "salah_iqamah_rules"));
        list.add(new ChapterItem(isBn ? "ইকামতের জওয়াব" : "Responding to Iqamah", "salah_iqamah_answer"));
        list.add(new ChapterItem(isBn ? "ইকামত ও নামায শুরু করার মাঝে ব্যবধান" : "Interval Between Iqamah and Starting Prayer", "salah_iqamah_prayer_interval"));
        list.add(new ChapterItem(isBn ? "তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান" : "Adhan for Tahajjud and Sahur", "salah_azan_tahajjud_sahur"));
        return list;
    }
}
