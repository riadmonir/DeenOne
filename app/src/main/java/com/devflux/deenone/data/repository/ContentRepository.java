package com.devflux.deenone.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.DailyContentDao;
import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.domain.repository.IContentRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContentRepository implements IContentRepository {

    private final DailyContentDao dailyContentDao;

    public ContentRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.dailyContentDao = db.dailyContentDao();
        initializeDefaultIfEmpty();
    }

    @Override
    public LiveData<DailyContentEntity> getLatestContent() {
        return dailyContentDao.getLatestContent();
    }

    @Override
    public void setDuaRead(long id, boolean isRead) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dailyContentDao.updateDuaReadStatus(id, isRead);
        });
    }

    private void initializeDefaultIfEmpty() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DailyContentEntity latest = dailyContentDao.getLatestContentSync();
            if (latest == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = sdf.format(new Date());

                DailyContentEntity defaultContent = new DailyContentEntity(
                        today,
                        "لاَ يُكَلِّفُ اللّهُ نَفْسًا إِلاَّ وُسْعَهَا...",
                        "\"আল্লাহ কোনো ব্যক্তির ওপর তার সামর্থ্যের অতিরিক্ত বোঝা চাপিয়ে দেন না। সে যা ভালো অর্জন করেছে তার ফল সে-ই পাবে এবং যা মন্দ অর্জন করেছে তার প্রতিফলও সে-ই ভোগ করবে। হে আমাদের প্রতিপালক! যদি আমরা ভুলে যাই কিংবা ভুল করি, তবে আমাদের পাকড়াও করবেন না। হে আমাদের প্রতিপালক! আমাদের ওপর এমন ভারী বোঝা অর্পণ করবেন না যা আমাদের পূর্ববর্তীদের ওপর চাপিয়েছিলেন। হে আমাদের প্রতিপালক! আমাদের এমন বোঝা বহন করাবেন না যার সামর্থ্য আমাদের নেই। আমাদের ক্ষমা করুন, আমাদের মার্জনা করুন এবং আমাদের প্রতি দয়া করুন। আপনিই আমাদের অভিভাবক; অতএব সত্যপ্রত্যাখ্যানকারী সম্প্রদায়ের বিরুদ্ধে আমাদের সাহায্য করুন।\"",
                        "— সূরা আল-বাকারা: ২৮৬",
                        "“ইসলামের ভিত্তি পাঁচটি স্তম্ভের ওপর প্রতিষ্ঠিত: ১. এ সাক্ষ্য দেওয়া যে আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই এবং মুহাম্মদ (ﷺ) আল্লাহর বান্দা ও রাসূল, ২. সালাত কায়েম করা, ৩. যাকাত আদায় করা, ৪. হজ করা এবং ৫. রমজানের রোজা রাখা।”",
                        "— সহীহ বুখারী: ৮, সহীহ মুসলিম: ১৬",
                        "দুনিয়া ও আখিরাতে কল্যাণের দোয়া",
                        "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                        "হে আমাদের পালনকর্তা! আমাদেরকে দুনিয়াতে কল্যাণ দিন এবং আখিরাতেও কল্যাণ দান করুন এবং আমাদেরকে জাহান্নামের আগুন থেকে রক্ষা করুন...",
                        "রেফারেন্স: সূরা আল-বাকারা: ২০১ (সহীহ বুখারী: ৪৫২২)",
                        false,
                        "আনার / ডালিম (Pomegranate / Rumman)",
                        "জান্নাতের সুস্বাদু ফল"
                );
                dailyContentDao.insertOrUpdate(defaultContent);
            }
        });
    }
}
