package com.devflux.deenone.features.janaza;

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

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemJanazaMenuCardBinding;
import com.devflux.deenone.databinding.PageJanazaGuideBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class JanazaGuidePageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageJanazaGuideBinding binding = PageJanazaGuideBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvJanazaHeaderTitle.setText(isBn ? "জানাযা গাইড" : "Janaza Guide");
        binding.tvHeroBadge.setText(isBn ? "ফরযে কেফায়া" : "Fardh al-Kifayah");
        binding.tvHeroTitle.setText(isBn ? "পরিপূর্ণ জানাযা ও কাফন-দাফন গাইড" : "Complete Janaza, Ghusl & Burial Guide");
        binding.tvHeroSubtitle.setText(isBn
                ? "“যে ব্যক্তি জানাযার সালাতে অংশ নেয় সে এক ক্বীরাত এবং যে দাফন পর্যন্ত থাকে সে দুই ক্বীরাত (ওহুদ পাহাড় সমপরিমাণ) সওয়াব অর্জন করে।” — সহীহ বুখারী: ৪৭"
                : "“Whoever attends the funeral until prayer is offered will have one Qirat of reward, and whoever remains until burial will have two Qirats (equal to Mount Uhud).” — Sahih Bukhari: 47");
        binding.tvTopicsSectionHeader.setText(isBn ? "সূচিপত্র ও বিষয়ভিত্তিক অধ্যায়সমূহ:" : "Table of Contents & Chapters:");

        // Back button with spring touch
        binding.btnBackJanaza.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackJanaza);

        // 8 Topics from screenshot
        List<JanazaDataProvider.JanazaTopic> topics = JanazaDataProvider.getTopics(isBn);

        TopicAdapter adapter = new TopicAdapter(activity, topics);
        binding.rvJanazaTopicsList.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvJanazaTopicsList.setAdapter(adapter);

        // Settings / Options gear
        binding.btnSettingsJanaza.setOnClickListener(v -> {
            showSettingsDialog(activity, topics, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsJanaza);

        dialog.show();
    }

    private static void showSettingsDialog(Activity activity, List<JanazaDataProvider.JanazaTopic> topics, boolean isBn) {
        String[] options = {
                isBn ? "সূচিপত্র কপি করুন" : "Copy Table of Contents",
                isBn ? "সম্পূর্ণ জানাযা গাইড শেয়ার করুন" : "Share Complete Janaza Guide"
        };

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "জানাযা গাইড অপশন" : "Janaza Guide Options")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "=== জানাযা গাইড সূচিপত্র ===\n\n" : "=== Janaza Guide Contents ===\n\n");
                        for (int i = 0; i < topics.size(); i++) {
                            sb.append((i + 1)).append(". ").append(topics.get(i).title).append("\n");
                        }
                        sb.append(isBn ? "\n— দ্বীনওয়ান অ্যাপ" : "\n— DeenOne App");

                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(ClipData.newPlainText(isBn ? "জানাযা গাইড সূচিপত্র" : "Janaza Guide Contents", sb.toString().trim()));
                            Toast.makeText(activity, isBn ? "সূচিপত্র ক্লিপবোর্ডে কপি করা হয়েছে" : "Table of contents copied", Toast.LENGTH_SHORT).show();
                        }
                    } else if (which == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(isBn ? "📖 জানাযা গাইড — দ্বীনওয়ান\n\n" : "📖 Janaza Guide — DeenOne\n\n");
                        sb.append(isBn ? "পরিপূর্ণ জানাযা সালাত, গোসল, কাফন-দাফন ও দোয়া শিখুন সহীহ সুন্নাহর আলোকে।\n\n"
                                : "Learn the complete method of Janaza prayer, washing, shrouding, burial, and authentic Duas.\n\n");
                        for (int i = 0; i < topics.size(); i++) {
                            sb.append("• ").append(topics.get(i).title).append("\n");
                        }
                        sb.append(isBn ? "\nদ্বীনওয়ান অ্যাপে বিস্তারিত পড়ুন।" : "\nRead complete details in DeenOne App.");

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "জানাযা গাইড" : "Janaza Guide");
                        intent.putExtra(Intent.EXTRA_TEXT, sb.toString().trim());
                        activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share via"));
                    }
                })
                .show();
    }

    private static class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
        private final Activity activity;
        private final List<JanazaDataProvider.JanazaTopic> topics;

        public TopicAdapter(Activity activity, List<JanazaDataProvider.JanazaTopic> topics) {
            this.activity = activity;
            this.topics = topics;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemJanazaMenuCardBinding binding = ItemJanazaMenuCardBinding.inflate(
                    LayoutInflater.from(activity), parent, false
            );
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JanazaDataProvider.JanazaTopic topic = topics.get(position);
            holder.bind(topic);
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemJanazaMenuCardBinding binding;

            public ViewHolder(@NonNull ItemJanazaMenuCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(JanazaDataProvider.JanazaTopic topic) {
                binding.tvItemTitle.setText(topic.title);
                binding.tvItemSubtitle.setText(topic.subtitle);
                binding.ivTopicIcon.setImageResource(topic.iconRes != 0 ? topic.iconRes : R.drawable.ic_feat_janaza);

                binding.cardContainer.setOnClickListener(v -> {
                    JanazaDetailViewerDialog.show(activity, topic.id);
                });
            }
        }
    }
}
