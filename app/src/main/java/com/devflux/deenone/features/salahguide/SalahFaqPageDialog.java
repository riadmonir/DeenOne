package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemSalahBasicsCardBinding;
import com.devflux.deenone.databinding.PageSalahBasicsRulesBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class SalahFaqPageDialog {

    public static class FaqItem {
        public String title;
        public String previewSubtitle;
        public String fullContent;
        public boolean isExpanded;

        public FaqItem(String title, String previewSubtitle, String fullContent) {
            this.title = title;
            this.previewSubtitle = previewSubtitle;
            this.fullContent = fullContent;
            this.isExpanded = false;
        }
    }

    public static void show(@NonNull Context context) {
        if (!(context instanceof Activity)) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageSalahBasicsRulesBinding binding = PageSalahBasicsRulesBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);
        binding.tvHeaderTitle.setText(isBn ? "সালাত সম্পর্কিত সাধারণ জিজ্ঞাসা" : "Common Questions on Salah");

        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahBasics);
        binding.btnBackSalahBasics.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsSalahBasics);
        binding.btnSettingsSalahBasics.setOnClickListener(v -> {
            // Reserved for quick actions
        });

        List<FaqItem> items = getFaqItems(isBn);

        FaqAdapter adapter = new FaqAdapter(context, items, isBn);
        binding.rvSalahBasicsList.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSalahBasicsList.setAdapter(adapter);

        dialog.show();
    }

    private static class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
        private final Context context;
        private final List<FaqItem> items;
        private final boolean isBn;

        public FaqAdapter(Context context, List<FaqItem> items, boolean isBn) {
            this.context = context;
            this.items = items;
            this.isBn = isBn;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahBasicsCardBinding binding = ItemSalahBasicsCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(items.get(position), isBn);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ItemSalahBasicsCardBinding binding;

            public ViewHolder(ItemSalahBasicsCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                TouchAnimationUtil.attachTouchSpring(binding.layoutToggleExpand);

                View.OnClickListener toggleClick = v -> {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                        FaqItem item = items.get(pos);
                        item.isExpanded = !item.isExpanded;
                        notifyItemChanged(pos);
                    }
                };

                binding.cardContainer.setOnClickListener(toggleClick);
                binding.layoutToggleExpand.setOnClickListener(toggleClick);
            }

            public void bind(FaqItem item, boolean isBn) {
                binding.tvItemTitle.setText(item.title);

                if (item.isExpanded) {
                    binding.tvItemPreview.setVisibility(View.GONE);
                    binding.tvItemFullContent.setVisibility(View.VISIBLE);
                    binding.tvItemFullContent.setText(SalahContentFormatter.format(item.fullContent, context));
                    binding.tvToggleText.setText(isBn ? "সংক্ষিপ্ত করুন" : "Show Less");
                    binding.ivToggleChevron.setRotation(180f);
                } else {
                    binding.tvItemPreview.setVisibility(View.VISIBLE);
                    binding.tvItemPreview.setText(item.previewSubtitle);
                    binding.tvItemFullContent.setVisibility(View.GONE);
                    binding.tvToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "Read More");
                    binding.ivToggleChevron.setRotation(0f);
                }
            }
        }
    }

    public static List<FaqItem> getFaqItems(boolean isBn) {
        List<FaqItem> list = new ArrayList<>();

        if (isBn) {
            // ১. নামাযের মধ্যে আমীন আওয়ায দিয়ে পড়া ভাল না কি আস্তে পড়া ভাল
            list.add(new FaqItem(
                    "নামাযের মধ্যে আমীন আওয়ায দিয়ে পড়া ভাল না কি আস্তে পড়া ভাল",
                    "হানাফী মাযহাব মুতাবেক আমীন আস্তে পড়া ভাল এবং সুন্নত।",
                    "হানাফী মাযহাব মুতাবেক আমীন আস্তে পড়া ভাল এবং সুন্নত।\n\n\n" +
                            "[ফাতাওয়া দারুল উলূম- ২/১৭৭। ১৬৮]"
            ));

            // ২. কোন ব্যক্তি যদি একা মাগরিব বা ইশা বা ফজরের নামায পড়ে তাহলে সে কেরাত কি জোরে পড়বে না কি আস্তে পড়বে?
            list.add(new FaqItem(
                    "কোন ব্যক্তি যদি একা মাগরিব বা ইশা বা ফজরের নামায পড়ে তাহলে সে কেরাত কি জোরে পড়বে না কি আস্তে পড়বে?",
                    "তার জন্য এখতেয়ার রয়েছে মনে চাইলে জোরেও পড়তে পারবে এবং আস্তেও পড়তে পারবে। তবে নুন্যতম স্বরে জোরে পড়া চাই।",
                    "তার জন্য এখতেয়ার রয়েছে মনে চাইলে জোরেও পড়তে পারবে এবং আস্তেও পড়তে পারবে। তবে নুন্যতম স্বরে জোরে পড়া চাই।\n\n\n" +
                            "[দুররে মুখতার- ০১/৪৯৮। ১৭০]"
            ));

            // ৩. নামাযের মধ্যে হাত নাভির উপরে বাঁধবে না কি নাভির নিচে বাঁধবে?
            list.add(new FaqItem(
                    "নামাযের মধ্যে হাত নাভির উপরে বাঁধবে না কি নাভির নিচে বাঁধবে?",
                    "পুরুষের জন্য নাভির উপরে বাঁধা ও নিচে বাঁধা উভয়টিই হাদীসে রয়েছে। কিন্তু হানাফী মাযহাবে নাভির নিচে হাত বাঁধাকে প্রাধান্য দেয়া হয়েছে। কারণ, আবু দাউদ শরীফে হযরত আলী রাঃ হতে বর্ণিত রয়েছে এটাকে সুন্নত আখ্যা দেয়া হয়েছে।",
                    "পুরুষের জন্য নাভির উপরে বাঁধা ও নিচে বাঁধা উভয়টিই হাদীসে রয়েছে। কিন্তু হানাফী মাযহাবে নাভির নিচে হাত বাঁধাকে প্রাধান্য দেয়া হয়েছে। কারণ, আবু দাউদ শরীফে হযরত আলী রাঃ হতে বর্ণিত রয়েছে এটাকে সুন্নত আখ্যা দেয়া হয়েছে।"
            ));

            // ৪. যদি কোন ব্যক্তি ডান হাতের শাহাদাত আঙ্গুলী না উঠাতে পারে – তাহলে বাম হাতের শাহাদাত আঙ্গুলী দ্বারা তাশাহহুদের সময় ইশারা করা জায়েয আছে কি না?
            list.add(new FaqItem(
                    "যদি কোন ব্যক্তি ডান হাতের শাহাদাত আঙ্গুলী না উঠাতে পারে – তাহলে বাম হাতের শাহাদাত আঙ্গুলী দ্বারা তাশাহহুদের সময় ইশারা করা জায়েয আছে কি না?",
                    "যদি ডান হাতের শাহাদাত আঙ্গুলী না থাকে অথবা আছে কিন্তু উঠাতে পারে না তাহলে ইশারাও করবেনা তবুও বাম হাতের আঙ্গুল দিয়ে ইশারা করবে না। কারণ, তার হকুম নেই।",
                    "যদি ডান হাতের শাহাদাত আঙ্গুলী না থাকে অথবা আছে কিন্তু উঠাতে পারে না তাহলে ইশারাও করবেনা তবুও বাম হাতের আঙ্গুল দিয়ে ইশারা করবে না। কারণ, তার হকুম নেই।\n\n\n" +
                            "[ফাতাওয়া দারুল উলুম - ২/১৯২]"
            ));

            // ৫. প্রথম রাকআতে লিইলাফি সূরা দ্বিতীয় রাকআতে আলামতারা সূরা পড়ল তাহলে নামায হবে কি না?
            list.add(new FaqItem(
                    "প্রথম রাকআতে লিইলাফি সূরা দ্বিতীয় রাকআতে আলামতারা সূরা পড়ল তাহলে নামায হবে কি না?",
                    "নফল নামাযে চাই ইচ্ছা করে এ রকম করুক অথবা ভুলে এ রকম হয়ে যাক নামায জায়েয হবে এবং মাকরূহ ও হবে না।",
                    "নফল নামাযে চাই ইচ্ছা করে এ রকম করুক অথবা ভুলে এ রকম হয়ে যাক নামায জায়েয হবে এবং মাকরূহ ও হবে না।"
            ));

            // ৬. ইমাম সাহেব রুকু অথবা সেজদা হতে তাসবীহ পড়ে মাথা উঠিয়ে ফেলছেন। কিন্তু মুক্তাদী এখনও তাসবীহ তিনবার পুরা করতে পারেনি। এখন মুক্তাদী কি করবে?
            list.add(new FaqItem(
                    "ইমাম সাহেব রুকু অথবা সেজদা হতে তাসবীহ পড়ে মাথা উঠিয়ে ফেলছেন। কিন্তু মুক্তাদী এখনও তাসবীহ তিনবার পুরা করতে পারেনি। এখন মুক্তাদী কি করবে?",
                    "ইমাম সাহেবের জন্য উচিত যেন রুকু এবং সেজদার তাসবীহ ধীরস্থির ভাবে পড়ে। যাতে মুক্তাদী অন্তত তিন বার পাঠ করতে পারে",
                    "ইমাম সাহেবের জন্য উচিত যেন রুকু এবং সেজদার তাসবীহ ধীরস্থির ভাবে পড়ে। যাতে মুক্তাদী অন্তত তিন বার পাঠ করতে পারে"
            ));

            // ৭. ইমাম সাহেব সালাম ফেরানোর পর যে দোয়া করেন তাতে মুক্তাদীগণ শরীক হওয়া জরুরী কি না?
            list.add(new FaqItem(
                    "ইমাম সাহেব সালাম ফেরানোর পর যে দোয়া করেন তাতে মুক্তাদীগণ শরীক হওয়া জরুরী কি না?",
                    "মুক্তাদীগণ এতে শরীক হওয়া মুস্তাহাব, জরুরী নয়।",
                    "মুক্তাদীগণ এতে শরীক হওয়া মুস্তাহাব, জরুরী নয়।"
            ));

            // ৮. এক চাটাইয়ের উপর যদি কোন পুরুষ এবং মহিলা এক বরাবর দাঁড়িয়ে নামায পড়ে তাহলে তাদের নামায দুরুস্ত হবে কি না?
            list.add(new FaqItem(
                    "এক চাটাইয়ের উপর যদি কোন পুরুষ এবং মহিলা এক বরাবর দাঁড়িয়ে নামায পড়ে তাহলে তাদের নামায দুরুস্ত হবে কি না?",
                    "যদি প্রত্যেকে নিজ নিজ আলাদা আলাদা নামায পড়ে তাহলে নামায সহীহ হয়ে যাবে। কিন্তু বেগানা মহিলার বরাবর দাঁড়ায়ে নামায পড়া মাকরূহ...",
                    "যদি প্রত্যেকে নিজ নিজ আলাদা আলাদা নামায পড়ে তাহলে নামায সহীহ হয়ে যাবে।\n\n" +
                            "কিন্তু বেগানা মহিলার বরাবর দাঁড়ায়ে নামায পড়া মাকরূহ। আর যদি ঐ বেগানা মহিলার নামায এবং পুরুষের নামায এক নামায হয় এবং একই তাহরীমার সাথে আদায় করে যেমন উভয় জন মাগরিবের নামায পড়ছে তাহলে নামায ফাসেদ হয়ে যাবে।\n\n\n" +
                            "[ফাতাওয়া দারুল উলুম - ২/ ১৮১-১৮২। ১৬৯]"
            ));

            // ৯. যদি কোন ব্যক্তি নামাযের তাকবীরে আল্লাহ শব্দের হামযা টেনে পড়ে অথবা আকবার শব্দের হামযা টেনে পড়ে তার নামায দুরুস্ত হবে কি না?
            list.add(new FaqItem(
                    "যদি কোন ব্যক্তি নামাযের তাকবীরে আল্লাহ শব্দের হামযা টেনে পড়ে অথবা আকবার শব্দের হামযা টেনে পড়ে তার নামায দুরুস্ত হবে কি না?",
                    "তার নামায ফাসেদ হয়ে যাবে।",
                    "তার নামায ফাসেদ হয়ে যাবে।\n\n\n" +
                            "[দুররে মুখতার- ১/৪৪৮।০৭]"
            ));

            // ১০. ফরয নামাযে প্রথম দু রাকাআতে ফাতিহার সাথে সূরা মিলানো হয়, শেষ দু রাক’আতে মিলানো হয় না কেন তার কারণ কি?
            list.add(new FaqItem(
                    "ফরয নামাযে প্রথম দু রাকাআতে ফাতিহার সাথে সূরা মিলানো হয়, শেষ দু রাক’আতে মিলানো হয় না কেন তার কারণ কি?",
                    "নবী করীম সা. এবং সাহাবা কেরাম এ রকম পড়েছেন। তাই এ রকম পড়া হয়।",
                    "নবী করীম সা. এবং সাহাবা কেরাম এ রকম পড়েছেন। তাই এ রকম পড়া হয়。\n\n\n" +
                            "[ফাতাওয়া দারুল উলুম - ২/১৭৫]"
            ));

            // ১১. যদি কোন ব্যক্তি প্রথম রাক’আতের চেয়ে দ্বিতীয় রাকআতে লম্বা ক্বেরাআত পড়ে তাহলে নামায জায়েয হবে কি না?
            list.add(new FaqItem(
                    "যদি কোন ব্যক্তি প্রথম রাক’আতের চেয়ে দ্বিতীয় রাকআতে লম্বা ক্বেরাআত পড়ে তাহলে নামায জায়েয হবে কি না?",
                    "প্রথম রাক’আতের চেয়ে দ্বিতীয় রাক’আতে যদি কেরাআত বেশী পড়ে যে বেশির পরিমাণটা তিন আয়াত বা তার চেয়ে বেশী হয় তা হলে নামায মাকরূহে তানযীহ হবে...",
                    "প্রথম রাক’আতের চেয়ে দ্বিতীয় রাক’আতে যদি কেরাআত বেশী পড়ে যে বেশির পরিমাণটা তিন আয়াত বা তার চেয়ে বেশী হয় তা হলে নামায মাকরূহে তানযীহ হবে। আর যদি তিন আয়াতের কম হয় তাহলে মাকরূহ\n\n\n" +
                            "[দুররে মুখতার ১ম খন্ড]"
            ));

            // ১২. নফল নামাযে যদি কেউ উল্টা তারতীবে সূরা পড়ে যেমন প্রথম রাকআতে কুলহুয়াল্লাহ সূরা আর দ্বিতীয় রাকআতে তাব্বাত ইয়াদা সূরা পড়ল তার নামায জায়েয হবে কি না?
            list.add(new FaqItem(
                    "নফল নামাযে যদি কেউ উল্টা তারতীবে সূরা পড়ে যেমন প্রথম রাকআতে কুলহুয়াল্লাহ সূরা আর দ্বিতীয় রাকআতে তাব্বাত ইয়াদা সূরা পড়ল তার নামায জায়েয হবে কি না?",
                    "নফল নামাযে চাই ইচ্ছা করে এ রকম করুক অথবা ভুলে এ রকম হয়ে যাক নামায জায়েয হবে এবং মাকরূহ ও হবে না।",
                    "নফল নামাযে চাই ইচ্ছা করে এ রকম করুক অথবা ভুলে এ রকম হয়ে যাক নামায জায়েয হবে এবং মাকরূহ ও হবে না。\n\n\n" +
                            "[ফাতাওয়া দারুল উলুম - ২/২১৮ ১৯০]"
            ));
        } else {
            // English Mode
            // 1. Saying Ameen aloud vs silent
            list.add(new FaqItem(
                    "Is it better to say Ameen aloud or silently in prayer?",
                    "According to the Hanafi Madhhab, reciting Ameen silently is preferred and is a Sunnah.",
                    "According to the Hanafi Madhhab, reciting Ameen silently is preferred and is considered Sunnah."
            ));

            // 2. Individual prayer aloud or silent for Maghrib, Isha, Fajr
            list.add(new FaqItem(
                    "If a person prays Maghrib, Isha, or Fajr alone, should they recite aloud or silently?",
                    "The person has the choice: they may recite aloud or silently. However, reciting with a minimal audible tone is preferred.",
                    "The person has the choice: they may recite aloud or silently. However, reciting with a minimal audible tone is preferred."
            ));

            // 3. Hands above or below navel
            list.add(new FaqItem(
                    "Should hands be folded above the navel or below the navel in prayer?",
                    "For men, both folding hands above and below the navel are mentioned in Hadith. The Hanafi Madhhab gives preference to folding hands below the navel...",
                    "For men, both folding hands above and below the navel are mentioned in Hadith. However, the Hanafi Madhhab gives preference to folding hands below the navel, as narrated from Hazrat Ali (RA) in Sunan Abi Dawud designating it as Sunnah."
            ));

            // 4. Inability to raise right index finger
            list.add(new FaqItem(
                    "If someone cannot raise the right index finger, is it permissible to gesture with the left index finger during Tashahhud?",
                    "If someone does not have a right index finger or cannot raise it, they should not gesture at all; they must not gesture with the left finger, as there is no command for it.",
                    "If someone does not have a right index finger or cannot raise it, they should not gesture at all; they must not gesture with the left finger, as there is no command for it."
            ));

            // 5. Reciting Surah Quraysh before Surah Al-Fil
            list.add(new FaqItem(
                    "If Surah Quraysh is recited in the first Rak'ah and Surah Al-Fil in the second Rak'ah, is the prayer valid?",
                    "In voluntary (Nafl) prayers, whether done intentionally or by mistake, the prayer is valid and not disliked (Makruh).",
                    "In voluntary (Nafl) prayers, whether done intentionally or by mistake, the prayer is valid and not disliked (Makruh)."
            ));

            // 6. Imam rises before Muqtadi finishes Tasbeeh 3 times
            list.add(new FaqItem(
                    "The Imam raised his head from Ruku or Sujud after Tasbeeh, but the follower hasn't completed three times yet. What should the follower do?",
                    "The Imam should recite the Tasbeeh of Ruku and Sujud calmly so that the followers can recite at least three times...",
                    "The Imam should recite the Tasbeeh of Ruku and Sujud calmly so that the followers can recite at least three times. If the Imam rises before the follower finishes, the follower must follow the Imam."
            ));

            // 7. Joining the Dua after Imam's Salam
            list.add(new FaqItem(
                    "Is it mandatory for the followers (Muqtadi) to join the supplication (Dua) after the Imam offers Salam?",
                    "Joining the supplication is Mustahabb (recommended), not mandatory (Wajib).",
                    "Joining the supplication is Mustahabb (recommended), not mandatory (Wajib)."
            ));

            // 8. Man and woman praying side by side on one mat
            list.add(new FaqItem(
                    "If a man and a woman stand side-by-side on the same mat to pray, is their prayer valid?",
                    "If each person performs their own separate prayer individually, the prayer is valid, though standing adjacent to a non-mahram woman is Makruh...",
                    "If each person performs their own separate prayer individually, the prayer is valid, though standing adjacent to a non-mahram woman is Makruh. However, if both are joined in the same congregational prayer under the same Takbir Tahrimah, the man's prayer becomes invalid (Fasid)."
            ));

            // 9. Elongating Hamzah in Allahu Akbar
            list.add(new FaqItem(
                    "If a person elongates the Hamzah in the word 'Allah' or 'Akbar' during Takbir, is the prayer valid?",
                    "The prayer becomes invalid (Fasid).",
                    "The prayer becomes invalid (Fasid), because elongating the Hamzah turns the declaration into a question, altering the meaning."
            ));

            // 10. Reciting Surah only in first two Rak'ahs of Fard
            list.add(new FaqItem(
                    "Why is a Surah recited with Surah Al-Fatihah in the first two Rak'ahs of Fard prayer but not in the last two?",
                    "The Holy Prophet (ﷺ) and the noble Companions recited in this manner; therefore, it is observed accordingly.",
                    "The Holy Prophet (ﷺ) and the noble Companions recited in this manner; therefore, it is observed accordingly."
            ));

            // 11. Longer recitation in second Rak'ah
            list.add(new FaqItem(
                    "If someone recites a longer recitation in the second Rak'ah than in the first, is the prayer permissible?",
                    "If the recitation in the second Rak'ah exceeds that of the first by three or more verses, the prayer is Makruh Tanzihi...",
                    "If the recitation in the second Rak'ah exceeds that of the first by three or more verses, the prayer is Makruh Tanzihi. If it is less than three verses, it is not Makruh."
            ));

            // 12. Reverse order Surah recitation in Nafl
            list.add(new FaqItem(
                    "In Nafl prayer, if someone recites Surahs in reverse order, such as Surah Al-Ikhlas in the first Rak'ah and Surah Al-Masad in the second, is the prayer valid?",
                    "In voluntary (Nafl) prayers, whether done intentionally or by mistake, the prayer is valid and not disliked (Makruh).",
                    "In voluntary (Nafl) prayers, whether done intentionally or by mistake, the prayer is valid and not disliked (Makruh)."
            ));
        }

        return list;
    }
}
