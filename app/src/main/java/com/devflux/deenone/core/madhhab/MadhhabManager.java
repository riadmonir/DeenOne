package com.devflux.deenone.core.madhhab;

import java.util.ArrayList;
import java.util.List;

public class MadhhabManager {

    public static class MadhhabIssueItem {
        public final String id;
        public final String topic;
        public final String category;
        public final String hanafiView;
        public final String hanafiRef;
        public final String malikiView;
        public final String malikiRef;
        public final String shafiiView;
        public final String shafiiRef;
        public final String hanbaliView;
        public final String hanbaliRef;
        public final String dalilSummary;

        public MadhhabIssueItem(String id, String topic, String category,
                                String hanafiView, String hanafiRef,
                                String malikiView, String malikiRef,
                                String shafiiView, String shafiiRef,
                                String hanbaliView, String hanbaliRef,
                                String dalilSummary) {
            this.id = id;
            this.topic = topic;
            this.category = category;
            this.hanafiView = hanafiView;
            this.hanafiRef = hanafiRef;
            this.malikiView = malikiView;
            this.malikiRef = malikiRef;
            this.shafiiView = shafiiView;
            this.shafiiRef = shafiiRef;
            this.hanbaliView = hanbaliView;
            this.hanbaliRef = hanbaliRef;
            this.dalilSummary = dalilSummary;
        }
    }

    private static volatile MadhhabManager instance;
    private final List<MadhhabIssueItem> issuesList = new ArrayList<>();

    public static MadhhabManager getInstance() {
        if (instance == null) {
            synchronized (MadhhabManager.class) {
                if (instance == null) {
                    instance = new MadhhabManager();
                }
            }
        }
        return instance;
    }

    private MadhhabManager() {
        loadIssues();
    }

    private void loadIssues() {
        issuesList.clear();

        // 1. সালাতে হাত বাঁধার স্থান
        issuesList.add(new MadhhabIssueItem(
                "m_01",
                "সালাতে তাকবীরে তাহরীমার পর হাত বাঁধার স্থান",
                "সালাত",
                "পুরুষদের জন্য নাভির নিচে হাত বাঁধা সুন্নাত এবং নারীদের জন্য বুকের ওপর।",
                "আল-হিদায়া ১/৪৭; মুসান্নাফে ইবনে আবী শায়বাহ: ৩৯৫৯",
                "হাত ছেড়ে দিয়ে সোজা রাখা (ইদসাল) উত্তম এবং বুকের নিচে বাঁধা মুস্তাহাব।",
                "মুওয়াত্তা মালিক ১/১৫৮; আল-মুদাওওয়ানাহ ১/৭৪",
                "নাভির ওপরে কিন্তু বুকের নিচে বা বুকের অংশে হাত বাঁধা সুন্নাত।",
                "আল-উম্ম ১/১১৪; আল-মাজমু' ৩/৩১৩",
                "নাভির নিচে হাত বাঁধা অধিক প্রসিদ্ধ এবং নাভির ওপরেও জায়েজ।",
                "আল-মুগনী ১/৪৭২; কাশশাফুল ক্বিনা ১/৩৩৪",
                "দলীল: আলী (রা.) ও ওয়াইল ইবনে হুজর (রা.) হতে বর্ণিত বিভিন্ন সহীহ ও হাসান হাদিসের বর্ণনার ভিত্তিতে সম্মানিত ইমামগণ ভিন্ন মত গ্রহণ করেছেন।"
        ));

        // 2. সূরা ফাতিহার পর আমীন বলা
        issuesList.add(new MadhhabIssueItem(
                "m_02",
                "নামাজে সূরা ফাতিহার পর 'আমীন' উচ্চস্বরে বনাম নীরবে বলা",
                "সালাত",
                "ইমাম ও মুক্তাদী সকলের জন্যই সর্বদা নীরবে আমীন বলা সুন্নাত।",
                "বাদায়েউস সানায়ে ১/২০৫; আল-হিদায়া ১/৪৮",
                "একাকী নামাজি নীরবে বলবে এবং জাহরী নামাজে ইমাম আমীন বলবে না বলে প্রসিদ্ধ মত।",
                "আল-মুদাওওয়ানাহ ১/৭১; শারহুয যারকানী ১/২১৭",
                "জাহরী নামাজে ইমাম ও মুক্তাদী উভয়েই উচ্চস্বরে আমীন বলবে।",
                "আল-উম্ম ১/১১৫; আল-মাজমু' ৩/৩৬৫",
                "জাহরী নামাজে উচ্চস্বরে এবং সিররী নামাজে নীরবে আমীন বলা সুন্নাত।",
                "আল-মুগনী ১/৪৭৫; আল-ইনসাফ ২/৫৮",
                "দলীল: রাসূলুল্লাহ (ﷺ) থেকে উভয় প্রকার আমলই বর্ণিত রয়েছে এবং উভয়টিই সহীহ হাদিস দ্বারা সমর্থিত।"
        ));

        // 3. স্পর্শের কারণে ওজু ভঙ্গের বিধান
        issuesList.add(new MadhhabIssueItem(
                "m_03",
                "বেগানা নারীকে স্পর্শ করলে ওজু ভঙ্গ হয় কি?",
                "তাহারাত",
                "কেবল স্পর্শে ওজু ভাঙে না, যতক্ষণ না বীর্য বা মযী নির্গত হয় বা কামোত্তেজক চরম আলিঙ্গন হয়।",
                "আল-হিদায়া ১/১৭; রদ্দুল মুহতার ১/১৩৫",
                "কামভাব বা আনন্দের সাথে স্পর্শ করলে ওজু ভঙ্গ হবে; কামভাব ছাড়া স্পর্শ করলে ওজু ভাঙবে না।",
                "আল-কাফী ফী ফিক্বহি আহলিল মাদীনাহ ১/১৫২",
                "পুরুষ ও নারীর ত্বক সরাসরি পরস্পরের সংস্পর্শে আসলেই ওজু ভঙ্গ হয়ে যাবে, কামভাব থাকুক বা না থাকুক।",
                "আল-উম্ম ১/১৫; আল-মাজমু' ২/২১",
                "কামভাবসহ সরাসরি স্পর্শ করলে ওজু ভঙ্গ হবে, অন্যথায় ওজু ভাঙবে না।",
                "আল-মুগনী ১/১৩৬; কাশশাফুল ক্বিনা ১/১২৮",
                "দলীল: পবিত্র কুরআনের সূরা নিসার ৪৩ নং আয়াতের 'লামাসতুমুন নিসা' শব্দের তাফসীরে সাহাবা ও তাবেঈদের মতভিন্নতার কারণে এই বৈচিত্র্য সৃষ্টি হয়েছে।"
        ));

        // 4. বিতর নামাজের পদ্ধতি ও রাকাত
        issuesList.add(new MadhhabIssueItem(
                "m_04",
                "বিতর নামাজের রাকাত সংখ্যা ও পড়ার পদ্ধতি",
                "সালাত",
                "মাগরিবের নামাজের ন্যায় এক সালামে ৩ রাকাত পড়া ওয়াজিব এবং ৩য় রাকাতে দোআয়ে কুনুত পড়া আবশ্যক।",
                "ফাতাওয়া হিন্দিয়া ১/১১১; আল-হিদায়া ১/৬৬",
                "১ রাকাত পড়া সুন্নাত; তবে পূর্বে দুই রাকাত শাফা নামাজ মিলিয়ে ৩ রাকাত পড়া উত্তম।",
                "মুওয়াত্তা মালিক ১/১২১; আল-মুদাওওয়ানাহ ১/১০২",
                "সর্বনিম্ন ১ রাকাত এবং সর্বোচ্চ ১১ রাকাত পর্যন্ত পড়া যায়; ৩ রাকাত পড়লে ২ রাকাতের পর সালাম ফিরিয়ে শেষে ১ রাকাত পড়া উত্তম।",
                "আল-উম্ম ১/১৪২; আল-মাজমু' ৪/৭",
                "সর্বনিম্ন ১ রাকাত এবং ৩ রাকাত পড়লে দুই সালামে বা এক সালামেও পড়া জায়েজ।",
                "আল-মুগনী ১/৫৫২; কাশশাফুল ক্বিনা ১/৪১৫",
                "দলীল: উম্মুল মুমিনীন আয়েশা (রা.), ইবনে উমর (রা.) ও উবাই ইবনে কা'ব (রা.) হতে বর্ণিত হাদিসের বিভিন্ন ব্যাখ্যার ওপর এই মতভেদ প্রতিষ্ঠিত।"
        ));
    }

    public List<MadhhabIssueItem> getAllIssues() {
        return new ArrayList<>(issuesList);
    }

    public List<MadhhabIssueItem> searchIssues(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(issuesList);
        }
        String q = query.trim().toLowerCase();
        List<MadhhabIssueItem> res = new ArrayList<>();
        for (MadhhabIssueItem item : issuesList) {
            if (item.topic.toLowerCase().contains(q) ||
                    item.hanafiView.toLowerCase().contains(q) ||
                    item.shafiiView.toLowerCase().contains(q) ||
                    item.malikiView.toLowerCase().contains(q) ||
                    item.hanbaliView.toLowerCase().contains(q)) {
                res.add(item);
            }
        }
        return res;
    }
}