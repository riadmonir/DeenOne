package com.devflux.deenone.features.ramadan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.ramadan.RamadanCalculator;
import com.devflux.deenone.features.ramadan.model.RamadanTopicItem;

import java.util.ArrayList;
import java.util.List;

public class RamadanViewModel extends AndroidViewModel {

  private final MutableLiveData<RamadanCalculator.RamadanStatus> ramadanStatusLiveData = new MutableLiveData<>();
  private final MutableLiveData<List<RamadanTopicItem>> topicsLiveData = new MutableLiveData<>();
  private final MutableLiveData<RamadanTopicItem> selectedTopicLiveData = new MutableLiveData<>();

  private LocationProvider.Coordinates currentCoordinates;

  public RamadanViewModel(@NonNull Application application) {
    super(application);
    currentCoordinates = LocationProvider.getSavedOrCurrentLocation(application);
    loadRamadanTopics();
    updateStatus();
  }

  public LiveData<RamadanCalculator.RamadanStatus> getRamadanStatus() {
    return ramadanStatusLiveData;
  }

  public LiveData<List<RamadanTopicItem>> getTopics() {
    return topicsLiveData;
  }

  public LiveData<RamadanTopicItem> getSelectedTopic() {
    return selectedTopicLiveData;
  }

  public void selectTopic(int index) {
    List<RamadanTopicItem> list = topicsLiveData.getValue();
    if (list != null && index >= 0 && index < list.size()) {
      selectedTopicLiveData.setValue(list.get(index));
    }
  }

  public void setLocation(LocationProvider.Coordinates coordinates) {
    if (coordinates != null) {
      this.currentCoordinates = coordinates;
      updateStatus();
    }
  }

  public void updateStatus() {
    if (currentCoordinates == null) {
      currentCoordinates = LocationProvider.getSavedOrCurrentLocation(getApplication());
    }
    RamadanCalculator.RamadanStatus status = RamadanCalculator.calculateStatus(
        getApplication(),
        currentCoordinates.latitude,
        currentCoordinates.longitude,
        currentCoordinates.timezone
    );
    ramadanStatusLiveData.setValue(status);
  }

  public void reloadLanguage() {
    loadRamadanTopics();
    updateStatus();
  }

  private void loadRamadanTopics() {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(getApplication());
    List<RamadanTopicItem> list = new ArrayList<>();

    if (isBn) {
      // 1. Rules of Fasting (রোজার বিধান ও শর্ত)
      list.add(new RamadanTopicItem(
          1,
          "১. বিধান ও ফরজ",
          "১. সিয়ামের সংজ্ঞা, হুকুম ও আবশ্যক শর্তসমূহ",
          "হুকুম: প্রাপ্তবয়স্ক সুস্থ মুসলিমের ওপর ফরজ",
          "সিয়াম (রোজা) ইসলামের পঞ্চ স্তম্ভের অন্যতম মৌলিক ফরজ ইবাদত। সুবহে সাদিক থেকে সূর্যাস্ত পর্যন্ত আল্লাহর সন্তুষ্টির উদ্দেশ্যে নিয়তসহ যাবতীয় পানাহার, ধূমপান ও যৌনমিলন থেকে বিরত থাকাই রোজা।",
          "রোজার মূল স্তম্ভসমূহ:\n• নিয়ত করা: মনে সংকল্প করা যে আমি আল্লাহর সন্তুষ্টিতে রোজা রাখছি।\n• বিরত থাকা: সুবহে সাদিক থেকে সূর্যাস্ত পর্যন্ত সকল প্রকার রোজা ভঙ্গকারী কাজ থেকে মুক্ত থাকা।\n• কার ওপর ফরজ: সুস্থ মস্তিষ্কসম্পন্ন, বালেগ, মুকিম এবং নারীদের হায়েয-নেফাসমুক্ত থাকা।",
          "অপারগতায় ছাড়:\n• গর্ভবতী ও দুগ্ধদানকারী মা সন্তানের ক্ষতির আশঙ্কা থাকলে পরে কাজা করবেন।\n• চরম অসুস্থ ব্যক্তি ও মুসাফির সফর শেষে কাজা আদায় করবেন।\n• অতি বৃদ্ধ বা চিরস্থায়ী রোগী প্রতিটি রোজার বদলে ১ জন মিসকিনকে দুই বেলা খাবার (ফিদিয়া) প্রদান করবেন।",
          "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
          "উচ্চারণ: ইয়া আইয়্যুহাল্লাযীনা আমানু কুতিবা আলাইকুমুস সিয়ামু কামা কুতিবা আলাল্লাযীনা মিন ক্বাবলিকুম লা'আল্লাকুম তাত্তাক্বূন।",
          "অর্থ: হে মুমিনগণ! তোমাদের ওপর রোজা ফরজ করা হয়েছে, যেরূপ ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের ওপর, যাতে তোমরা তাকওয়া অর্জন করতে পারো।",
          "সূরা আল-বাকারা: ১৮৩, সহীহ বুখারী: ৮"
      ));

    // 2. Things that Invalidate Fasting (রোজা ভঙ্গের কারণ)
    list.add(new RamadanTopicItem(
        2,
        "২. রোজা ভঙ্গ",
        "২. যা করলে রোজা ভেঙে যায় (কাজা ও কাফফারা)",
        "সতর্কতা: কাজা ও কাফফারার কারণসমূহ",
        "রোজার পবিত্রতা নষ্টকারী কিছু কাজের কারণে রোজা ভেঙে যায়। কোনোটিতে শুধু কাজা (১টি রোজা) এবং কোনোটিতে কাজা ও কাফফারা (টানা ৬০টি রোজা বা ৬০ জন মিসকিনকে খাবার) আবশ্যক হয়।",
        "যা করলে রোজা ভেঙে যায় ও ১টি কাজা ওয়াজিব হয়:\n• ইচ্ছাকৃতভাবে মুখ ভরে বমি করা\n• নাকের ড্রপ বা এমন ওষুধ ব্যবহার করা যা সরাসরি পাকস্থলীতে যায়\n• ভুলবশত খেয়ে ফেলার পর রোজা ভেঙে গেছে ভেবে ইচ্ছাকৃতভাবে খাওয়া\n• সূর্যাস্ত হয়ে গেছে মনে করে ইফতার করা অথচ সূর্য ডুবেনি\n• হস্তমৈথুনের মাধ্যমে বীর্যপাত ঘটানো।",
        "যা করলে রোজা ভাঙে এবং কাজা ও কাফফারা উভয়ই ওয়াজিব হয়:\n• রমজানের দিনে ইচ্ছাকৃতভাবে স্ত্রী সহবাস করা (সহীহ বুখারী: ১৯৩৬)।\n• কাফফারা: একটানা ৬০ দিন রোজা রাখা বা ৬০ জন মিসকিনকে দুই বেলা তৃপ্তিভরে খাওয়ানো।",
        "مَنْ ذَرَعَهُ الْقَيْءُ فَلَيْسَ عَلَيْهِ قَضَاءٌ، وَمَنِ اسْتَقَاءَ عَمْدًا فَلْيَقْضِ",
        "উচ্চারণ: মান যারা'আহুল ক্বাইয়ু ফালাইসা আলাইহি ক্বাদ্বা-উন, ওয়া মানিস তাক্বা-আ আমদান ফালইয়াক্বদ্বি।",
        "অর্থ: যার অনিচ্ছাকৃত বমি হয় তার কাজা করতে হবে না, কিন্তু যে ব্যক্তি ইচ্ছাকৃতভাবে বমি করে সে যেন কাজা আদায় করে।",
        "জামে তিরমিযী: ৭২০, সুনান আবু দাউদ: ২৩৮০"
    ));

    // 3. Things Allowed While Fasting (রোজায় যা যা বৈধ)
    list.add(new RamadanTopicItem(
        3,
        "৩. অনুমোদিত বিষয়",
        "৩. রোজায় যা যা বৈধ ও অনুমোদিত (ভ্রান্ত ধারণার নিরসন)",
        "বিধান: সহীহ সুন্নাহ অনুযায়ী সম্পূর্ণ বৈধ",
        "আমাদের সমাজে রোজা সংক্রান্ত বহু ভুল ধারণা প্রচলিত রয়েছে। সহীহ সুন্নাহ অনুযায়ী যেসব কাজ রোজার কোনো ক্ষতি করে না তা জেনে রাখা আবশ্যক।",
        "রোজায় সম্পূর্ণ অনুমোদিত বিষয়সমূহ:\n• মেসওয়াক বা টুথব্রাশ ব্যবহার করা (গিলে না ফেলে থুতু ফেলে দেওয়া)\n• চোখে ড্রপ, সুরমা বা কানে ড্রপ দেওয়া\n• গোসল করা, পানিতে ডুব দেওয়া বা মাথায় ঠান্ডা পানি ঢালা\n• অনিচ্ছাকৃত বমি হওয়া বা অনিচ্ছাকৃত খাবার ঢুকে যাওয়া\n• ইনসুলিন বা চিকিৎসার ইনজেকশন নেওয়া (পুষ্টিকর স্যালাইন/গ্লুকোজ বাদে)\n• চিকিৎসার প্রয়োজনে রক্ত দেওয়া বা টেস্টের জন্য রক্ত নেওয়া\n• অনিচ্ছাকৃত ধোঁয়া, ধুলোবালি বা মশা গিলে ফেলা\n• স্ত্রী বা সন্তানকে স্নেহের চুম্বন করা (কামভাব জাগ্রত না হলে)।",
        "বিশেষ দ্রষ্টব্য: অনিচ্ছাকৃত ভুলবশত কিছু খেয়ে বা পান করে ফেললে রোজা ভাঙবে না; মনে পড়ার সাথে সাথে খাওয়া বন্ধ করে রোজা পূর্ণ করতে হবে (সহীহ বুখারী: ৬৬৬৯)।",
        "إِذَا نَسِيَ فَأَكَلَ وَشَرِبَ فَلْيُتِمَّ صَوْمَهُ، فَإِنَّمَا أَطْعَمَهُ اللَّهُ وَسَقَاهُ",
        "উচ্চারণ: ইযা নাসিয়া ফা-আকালা ওয়া শারিবা ফালইয়ুতিম্মা সাওমাহু, ফাইন্নামা আত'আমাহুল্লাহু ওয়া সাক্বাহ।",
        "অর্থ: যে ব্যক্তি ভুলে কিছু খেয়ে বা পান করে ফেলে, সে যেন তার রোজা পূর্ণ করে; কারণ আল্লাহই তাকে আহার করিয়েছেন ও পান করিয়েছেন।",
        "সহীহ বুখারী: ১৯৩৩, সহীহ মুসলিম: ১১৫৫"
    ));

    // 4. Things Disliked (রোজায় মাকরুহ কাজসমূহ)
    list.add(new RamadanTopicItem(
        4,
        "৪. মাকরুহ বিষয়",
        "৪. রোজায় যা যা মাকরুহ ও বর্জনীয়",
        "সতর্কতা: সওয়াব নষ্টকারী অপছন্দনীয় বিষয়",
        "যেসব কাজের দ্বারা রোজা ভেঙে যায় না কিন্তু রোজার মর্যাদা ও সওয়াব ক্ষুণ্ন হয় বা ভেঙে যাওয়ার ঝুঁকি তৈরি হয় সেগুলোকে মাকরুহ বলা হয়।",
        "রোজায় মাকরুহ কাজসমূহ:\n• অপ্রয়োজনে জিহ্বা দিয়ে খাবারের স্বাদ পরীক্ষা করা\n• অতিরিক্ত ফেনা হওয়া টুথপেস্ট ব্যবহার করা (গিলে ফেলার ভয় থাকলে)\n• মুখে দীর্ঘক্ষণ থুতু জমিয়ে রেখে তা গিলে ফেলা\n• রোজা রেখে মিথ্যা বলা, গিবত (পরনিন্দা), গালাগালি বা অশ্লীল আচরণ করা\n• অপ্রয়োজনে রক্ত দিয়ে শরীরকে মাত্রাতিরিক্ত দুর্বল করে ফেলা\n• সারা দিন অযথা ঘুমিয়ে রোজা কাটিয়ে দেওয়া।",
        "কঠোর সতর্কবাণী: রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি মিথ্যা কথা ও মন্দ কাজ পরিহার করল না, আল্লাহর কোনো প্রয়োজন নেই যে সে পানাহার পরিত্যাগ করুক।' (সহীহ বুখারী: ১৯০৩)।",
        "مَنْ لَمْ يَدَعْ قَوْلَ الزُّورِ وَالْعَمَلَ بِهِ فَلَيْسَ لِلَّهِ حَاجَةٌ فِي أَنْ يَدَعَ طَعَامَهُ وَشَرَابَهُ",
        "উচ্চারণ: মান লাম ইয়াদা' ক্বাওলায যূরি ওয়াল আমালা বিহী ফালাইসা লিল্লাহি হা-জাতুন ফী আই ইয়াদা'আ ত্বা'আমাহু ওয়া শারাবাহ।",
        "অর্থ: যে ব্যক্তি মিথ্যা কথা ও মন্দ আচরণ বর্জন করল না, তার পানাহার ত্যাগ করায় আল্লাহর কোনো প্রয়োজন নেই।",
        "সহীহ বুখারী: ১৯০৩, সুনান আবু দাউদ: ২৩৬২"
    ));

    // 5. Suhoor & Iftar Guidance (সেহরি, ইফতার ও দোয়া)
    list.add(new RamadanTopicItem(
        5,
        "৫. সেহরি ও ইফতার",
        "৫. সেহরি ও ইফতারের সুন্নাহ আদব ও মাসনূন দোয়া",
        "সুন্নাহ: বরকতময় সেহরি ও দ্রুত ইফতার",
        "সেহরি খাওয়া ও সূর্যাস্তের সাথে সাথে কালক্ষেপণ না করে দ্রুত ইফতার করা মহানবী (ﷺ)-এর অন্যতম প্রধান সুন্নাত।",
        "সেহরির সুন্নাতসমূহ:\n• সেহরি খাওয়া বরকতময় ও সুন্নাত (এক ঢোক পানি হলেও খাওয়া উচিত)\n• সুবহে সাদিকের নিকটবর্তী শেষ সময়ে সেহরি খাওয়া মুস্তাহাব।\n\n ইফতারের সুন্নাতসমূহ:\n• সূর্যাস্তের নিশ্চয়তা পাওয়ার পর দেরি না করে অবিলম্বে ইফতার করা\n• তাজা বা শুকনো খেজুর দিয়ে ইফতার শুরু করা, না পেলে পানি দিয়ে\n• ইফতারের সময় দোয়া কবুল হয়; নিজের ও পরিবারের জন্য দোয়া করা।",
        "নিয়তের বিধান: নিয়ত অন্তরের সংকল্প। মনে মনে রোজার ইচ্ছা থাকাই যথেষ্ট, মুখে বিশেষ আরবি শব্দ বলা শর্ত নয়।",
        "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
        "উচ্চারণ: যাহাবায জামাউ ওয়াবতাল্লাতিল উরূকু ওয়া সাবাতাল আজরু ইনশাআল্লাহ।",
        "অর্থ: তৃষ্ণা দূর হলো, শিরা-উপশিরা সিক্ত হলো এবং আল্লাহর ইচ্ছায় প্রতিদান নিশ্চিত হলো।",
        "সুনান আবু দাউদ: ২৩৫৭, সুনান আদ-দারা কুতনী: ২২৭৯"
    ));

    // 6. Tarawih & Qiyam (তারাবীহ ও কিয়ামুল লাইল)
    list.add(new RamadanTopicItem(
        6,
        "৬. তারাবীহ ও কিয়াম",
        "৬. সালাতুত তারাবীহ ও কিয়ামুল লাইলের মর্যাদা",
        "সুন্নাত: সুন্নাতে মুয়াক্কাদাহ ও গুনাহ মাফের মাধ্যম",
        "রমজানের প্রতি রাতে এশার সালাতের পর বিতরের পূর্বে সালাতুত তারাবীহ আদায় করা সুন্নাতে মুয়াক্কাদাহ। জামাতের সাথে তারাবীহ আদায় করা বিপুল সওয়াবের কাজ।",
        "তারাবীর নিয়ম ও ফজিলত:\n• প্রতি ২ রাকাত পর পর সালাম ফিরিয়ে আদায় করা সুন্নাত\n• তাড়াহুড়ো না করে ধীরস্থিরভাবে কুরআন তিলাওয়াত ও রুকু-সিজদা সম্পন্ন করা\n• ইমামের সাথে শেষ পর্যন্ত নামাজ আদায় করলে সারা রাত দাঁড়িয়ে নামাজ পড়ার সওয়াব পাওয়া যায় (জামে তিরমিযী: ৮০৬)\n• শেষ রাতে তাহাজ্জুদ ও তওবা-ইস্তিগফার করা।",
        "বিশেষ দ্রষ্টব্য: তারাবীর মাঝে প্রচলিত দীর্ঘ বানোয়াট মুনাজাত বা আরবি বাক্য পড়ার কোনো সহীহ ভিত্তি নেই; ব্যক্তিগতভাবে যেকোনো দোয়া করা যায়।",
        "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
        "উচ্চারণ: মান ক্বামা রমাদ্বানা ঈমানান ওয়াহতিসাবান গুফিরা লাহু মা তাক্বাদ্দামা মিন যামবিহ।",
        "অর্থ: যে ব্যক্তি ঈমানের সাথে ও সওয়াবের আশায় রমজানের রাতে নামাজে দাঁড়াবে, তার পূর্বের সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে।",
        "সহীহ বুখারী: ২০০৯, সহীহ মুসলিম: ৭৫৯"
    ));

    // 7. Laylat al-Qadr & Itikaf (লাইলাতুল কদর ও ই'তিকাফ)
    list.add(new RamadanTopicItem(
        7,
        "৭. লাইলাতুল কদর",
        "৭. মহিমান্বিত কদরের রাত ও শেষ দশকের ই'তিকাফ",
        "ফজিলত: হাজার মাসের চেয়েও শ্রেষ্ঠ রাত",
        "রমজানের শেষ দশকে রয়েছে মহিমান্বিত রাত 'লাইলাতুল কদর'। এই এক রাতের ইবাদত হাজার মাস (৮৩ বছর ৪ মাস)-এর চেয়েও উত্তম।",
        "কদরের রাত অনুসন্ধানের নিয়ম:\n• রমজানের শেষ দশকের বেজোড় রাতসমূহে (২১, ২৩, ২৫, ২৭, ২৯তম রাতে) কদর অনুসন্ধান করা\n• সারা রাত জাগ্রত থেকে নফল নামাজ, কুরআন তিলাওয়াত, জিকির ও দোয়ায় মশগুল থাকা\n• মা আয়েশা (রা.) রাসূলুল্লাহ (ﷺ)-কে জিজ্ঞাসা করেছিলেন: 'হে আল্লাহর রাসুল! আমি কদরের রাত পেলে কী দোয়া করব?' তিনি নিচের দোয়াটি শিক্ষা দেন।",
        "শেষ দশকের ই'তিকাফ:\n• ২০শে রমজান সূর্যাস্তের পূর্বে মসজিদে প্রবেশ করে ঈদের চাঁদ দেখা পর্যন্ত মসজিদে অবস্থান করা সুন্নাতে মুয়াক্কাদাহ আলাল কিফায়াহ।",
        "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
        "উচ্চারণ: আল্লাহুম্মা ইন্নাকা আফুউন তুহিব্বুল আফওয়া ফা'ফু আন্নী।",
        "অর্থ: হে আল্লাহ! নিশ্চয়ই আপনি ক্ষমাশীল, আপনি ক্ষমা করতে ভালোবাসেন; অতএব আমাকে ক্ষমা করে দিন।",
        "জামে তিরমিযী: ৩৫১৩, সুনান ইবনে মাজাহ: ৩৮৫০"
    ));

    // 8. Zakat & Zakat al-Fitr (যাকাত ও ফিতরাহ)
    list.add(new RamadanTopicItem(
        8,
        "৮. যাকাত ও ফিতরাহ",
        "৮. যাকাত হিসাব ও সাদাকাতুল ফিতরের সুন্নাহ বিধান",
        "ফরজ ও ওয়াজিব: ধনীদের ওপর যাকাত, সকলের ওপর ফিতরাহ",
        "রমজান মাসে বেশি বেশি দান-সদকাহ ও যাকাত আদায়ের রেওয়াজ রয়েছে। ঈদের দিন সকালে ঈদের সালাতের আগেই সাদাকাতুল ফিতর আদায় করা ওয়াজিব।",
        "যাকাতের নিয়ম:\n• নেসাব: সাড়ে ৭ ভরি সোনা বা সাড়ে ৫২ ভরি রূপা বা সমমূল্যের নগদ অর্থ/ব্যবসায়িক পণ্য ১ বছর পূর্ণ থাকলে তার ২.৫% যাকাত দেওয়া ফরজ।\n\n সাদাকাতুল ফিতরের নিয়ম:\n• উদ্দেশ্য: রোজাদারের ত্রুটি-বিচ্যুতি দূর করা ও দরিদ্রদের ঈদের আনন্দ নিশ্চিত করা\n• কার ওপর ওয়াজিব: ঈদের রাতে নিজের ও পরিবারের খরচের অতিরিক্ত জীবিকা থাকলে পরিবারের প্রত্যেকের পক্ষ থেকে দিতে হবে\n• পরিমাণ: মাথাপিছু ১ সা' (প্রায় ২ কেজি ৪০ গ্রাম) চাল/গম/খেজুর বা তার বাজারমূল্য\n• সময়: ঈদের সালাতে যাওয়ার পূর্বেই গরিবের হাতে পৌঁছে দেওয়া ওয়াজিব (সহীহ বুখারী: ১৫০৯)।",
        "ঈদের সালাতের পরে ফিতরা দিলে তা সাধারণ সদকা হিসেবে গণ্য হবে, ফিতরাহ আদায় হবে না।",
        "فَرَضَ رَسُولُ اللَّهِ ﷺ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ وَطُعْمَةً لِلْمَسَاكِينِ",
        "উচ্চারণ: ফারাদ্বা রাসূলুল্লাহি ﷺ যাকা-তাল ফিতরি তুহরাতান লিস-সায়িমি মিনাল লাগবি ওয়ার-রাফাছি ওয়া তু'মাতান লিল-মাসাকীন।",
        "অর্থ: রাসূলুল্লাহ (ﷺ) রোজাদারের অনর্থক ও অশ্লীল কথা থেকে পবিত্রতার জন্য এবং মিসকিনদের খাদ্যের জন্য ফিতরাহ ফরজ করেছেন।",
        "সুনান আবু দাউদ: ১৬০৯, সুনান ইবনে মাজাহ: ১৮২৭"
    ));

    // 9. Ramadan Special Hadith & Amal (রমজানের আমল ও হাদিস)
    list.add(new RamadanTopicItem(
        9,
        "৯. বিশেষ আমল",
        "৯. রমজানের শ্রেষ্ঠ আমলসমূহ ও সহীহ হাদিস",
        "ফজিলত: প্রতিটি নেক আমলের সওয়াব বহুগুণ বৃদ্ধি",
        "রমজান হলো নেকির বসন্তকাল। এই মাসে জান্নাতের দরজাসমূহ খুলে দেওয়া হয় এবং জাহান্নামের দরজাসমূহ বন্ধ করে শয়তানকে শৃঙ্খলিত করা হয়।",
        "রমজানের সেরা ১০টি আমল:\n১. ৫ ওয়াক্ত সালাত তাকবীরে উলার সাথে জামাতে আদায়\n২. অর্থসহ নিয়মিত কুরআন তিলাওয়াত ও তাদাব্বুর\n৩. তাহাজ্জুদ ও শেষ রাতের কান্নাকাটি\n৪. ইফতারের পূর্বমুহূর্তে রবের দরবারে বিশেষ দোয়া\n৫. অন্য রোজাদারকে ইফতার করানো\n৬. সামর্থ্য অনুযায়ী প্রতিদিন সাদাকাহ করা\n৭. পিতা-মাতা ও আত্মীয়তার সম্পর্ক মজবুত করা\n৮. জিহ্বা ও চোখকে পাপ থেকে সম্পূর্ণ হেফাজত করা\n৯. তাওবাহ ও দিনে ১০০ বার ইস্তিগফার পাঠ\n১০. শেষ দশকে লাইলাতুল কদরের অন্বেষণ ও ই'তিকাফ।",
        "সহীহ হাদিস: 'যখন রমজান মাস আসে, জান্নাতের দরজাসমূহ খুলে দেওয়া হয়, জাহান্নামের দরজাসমূহ বন্ধ করে দেওয়া হয় এবং শয়তানদের শৃঙ্খলে বন্দি করা হয়।' (সহীহ বুখারী: ১৮৯৯, সহীহ মুসলিম: ১০৭৯)।",
        "إِذَا دَخَلَ شَهْرُ رَمَضَانَ فُتِّحَتْ أَبْوَابُ السَّمَاءِ وَغُلِّقَتْ أَبْوَابُ جَهَنَّمَ وَسُلْسِلَتِ الشَّيَاطِينُ",
        "উচ্চারণ: ইযা দাখালা শাহরু রামাদ্বানা ফুততিহাত আবওয়াবুস সামা-ই, ওয়া গুল্লিক্বাত আবওয়াবু জাহান্নামা ওয়া সুলসিলাতিশ শায়াত্বীন।",
        "অর্থ: যখন রমজান মাস আগমন করে, আসমানের দরজাসমূহ খুলে দেওয়া হয়, জাহান্নামের দরজাসমূহ বন্ধ করা হয় এবং শয়তানদের শিকলবদ্ধ করা হয়।",
        "সহীহ বুখারী: ১৮৯৯, সহীহ মুসলিম: ১০৭৯"
    ));

    // 10. Ramadan Quran Goals (কুরআন খতমের লক্ষ্যমাত্রা)
    list.add(new RamadanTopicItem(
        10,
        "১০. কুরআন লক্ষ্য",
        "১০. রমজানে কুরআন খতম ও পড়ার বাস্তবসম্মত রুটিন",
        "লক্ষ্যমাত্রা: ৩০ দিনে ১ বা একাধিক খতম সম্পন্ন করার পরিকল্পনা",
        "রমজান হলো কুরআন নাজিলের মাস। জিবরীল (আ.) প্রতি রমজানে রাসূলুল্লাহ (ﷺ)-এর সাথে পূর্ণ কুরআন পাঠের পুনরাবৃত্তি করতেন।",
        "৩০ দিনে ১ খতম করার সহজ ৫ ওয়াক্ত রুটিন:\n• পবিত্র কুরআনে মোট ৩০টি পারা (জুয) রয়েছে (প্রতি পারায় আনুমানিক ২০ পৃষ্ঠা)।\n• ফজরের পর: ৪ পৃষ্ঠা\n• জোহরের পর: ৪ পৃষ্ঠা\n• আসরের পর: ৪ পৃষ্ঠা\n• মাগরিবের পর: ৪ পৃষ্ঠা\n• এশার পর: ৪ পৃষ্ঠা\n• মোট = প্রতিদিন ২০ পৃষ্ঠা (১ পারা) ৩০ দিনে পূর্ণ ৩০ পারা খতম!",
        "৩০ দিনে ২ খতম করার রুটিন:\n• প্রতি ওয়াক্ত সালাতের পূর্বে ৪ পৃষ্ঠা এবং পরে ৪ পৃষ্ঠা পাঠ প্রতিদিন ৪০ পৃষ্ঠা (২ পারা) ৩০ দিনে ২ খতম!",
        "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ وَبَيِّنَاتٍ مِّنَ الْهُدَىٰ وَالْفُرْقَانِ",
        "উচ্চারণ: শাহরু রামাদ্বানাল্লাযী উনযিলা ফীহিল ক্বুরআনু হুদাল লিন্নাসি ওয়া বাইয়িনাতিম মিনাল হুদা ওয়াল ফুরক্বান।",
        "অর্থ: রমজান মাস হলো সেই মাস যাতে কুরআন নাজিল করা হয়েছে মানুষের হেদায়াতের জন্য এবং পথনির্দেশ ও হক-বাতিল পার্থক্যের স্পষ্ট প্রমাণ হিসেবে।",
        "সূরা আল-বাকারা: ১৮৫, সহীহ বুখারী: ৪৯৯৭"
    ));

      topicsLiveData.setValue(list);
      if (!list.isEmpty()) {
        selectedTopicLiveData.setValue(list.get(0));
      }
    } else {
      // English Ramadan Topics
      list.add(new RamadanTopicItem(
          1,
          "1. Obligations",
          "1. Definition, Obligation & Conditions of Fasting",
          "Obligation: Fard on healthy adult Muslims",
          "Fasting (Sawm) is one of the five foundational pillars of Islam. It is abstaining with intention from food, drink, and marital relations from dawn (Fajr) to sunset (Maghrib).",
          "Pillars of Fasting:\n• Intention (Niyyah): Sincere intention in the heart to fast for the sake of Allah.\n• Abstinence: Refraining from invalidating acts from Fajr to Maghrib.\n• Who must fast: Sane, adult, resident Muslims, and women free from menses/postnatal bleeding.",
          "Exemptions:\n• Pregnant and nursing mothers may postpone and make up fasts (Qadha) if fearing harm to child.\n• Sick and travelers make up missed fasts after Ramadan.\n• Chronically ill and elderly pay Fidyah (feeding one poor person per day).",
          "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
          "Transliteration: Ya ayyuhalladhina amanu kutiba alaykumus-siyamu kama kutiba alalladhina min qablikum la'allakum tattaqun.",
          "Translation: O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.",
          "Surah Al-Baqarah: 183, Sahih Al-Bukhari: 8"
      ));

      list.add(new RamadanTopicItem(
          2,
          "2. Invalidators",
          "2. Things that Invalidate Fasting (Qadha & Kaffarah)",
          "Warning: Causes of Qadha and Kaffarah",
          "Certain acts invalidate the fast. Some require only Qadha (making up 1 day), while severe violations require Qadha plus Kaffarah (fasting 60 consecutive days or feeding 60 poor persons).",
          "Invalidates fast requiring 1 Qadha:\n• Deliberate vomiting\n• Nose drops or medication reaching the stomach directly\n• Eating after accidental slip thinking fast is already broken\n• Breaking fast before sunset assuming sun has set\n• Masturbation resulting in ejaculation.",
          "Invalidates fast requiring Qadha and Kaffarah:\n• Deliberate sexual intercourse during fasting hours of Ramadan (Sahih Al-Bukhari: 1936).\n• Kaffarah: Fasting 60 consecutive days or feeding 60 needy persons.",
          "مَنْ ذَرَعَهُ الْقَيْءُ فَلَيْسَ عَلَيْهِ قَضَاءٌ، وَمَنِ اسْتَقَاءَ عَمْدًا فَلْيَقْضِ",
          "Transliteration: Man dhara'ahul-qay'u falaysa 'alayhi qada', wa manis-taqa'a 'amdan fal-yaqdi.",
          "Translation: Whoever is overcome by vomiting does not have to make it up, but whoever vomits intentionally must make it up.",
          "Jami' at-Tirmidhi: 720, Sunan Abi Dawud: 2380"
      ));

      list.add(new RamadanTopicItem(
          3,
          "3. Permissible",
          "3. Permissible Acts While Fasting (Common Myths)",
          "Status: Completely Permissible by Sahih Sunnah",
          "Many misconceptions exist regarding fasting. The authentic Sunnah clarifies many permissible acts that do not harm the fast.",
          "Permissible acts while fasting:\n• Using Siwak or toothbrush without swallowing paste\n• Applying eye drops, kohl, or ear drops\n• Taking a bath, swimming, or pouring cold water on head\n• Involuntary vomiting or accidentally swallowing dust/insects\n• Injections and insulin for medical treatment (except nutritional IV drip)\n• Blood donation or blood testing\n• Kissing or embracing spouse without lustful temptation.",
          "Note: Eating or drinking out of forgetfulness does not break the fast; continue fasting immediately upon remembering (Sahih Al-Bukhari: 6669).",
          "إِذَا نَسِيَ فَأَكَلَ وَشَرِبَ فَلْيُتِمَّ صَوْمَهُ، فَإِنَّمَا أَطْعَمَهُ اللَّهُ وَسَقَاهُ",
          "Transliteration: Idha nasiya fa-akala wa-shariba falyutimma sawmahu, fa'innama at'amahullahu wa saqah.",
          "Translation: Whoever forgets and eats or drinks should complete his fast, for it was Allah Who fed him and gave him drink.",
          "Sahih Al-Bukhari: 1933, Sahih Muslim: 1155"
      ));

      list.add(new RamadanTopicItem(
          4,
          "4. Disliked Acts",
          "4. Disliked and Detestable Acts While Fasting",
          "Warning: Makruh acts that reduce reward",
          "Acts that do not break the fast but diminish its spiritual reward and sanctity are considered Makruh.",
          "Disliked acts:\n• Tasting food unnecessarily with the tongue\n• Using foaming toothpaste with risk of swallowing\n• Swallowing saliva accumulated deliberately in mouth\n• Backbiting, lying, abusive speech, or vulgar behavior while fasting\n• Donating excessive blood leading to severe physical weakness\n• Spending the entire day sleeping away the fast.",
          "Warning: The Messenger of Allah (ﷺ) said: 'Whoever does not give up false speech and evil actions, Allah has no need of his leaving food and drink.' (Sahih Al-Bukhari: 1903).",
          "مَنْ لَمْ يَدَعْ قَوْلَ الزُّورِ وَالْعَمَلَ بِهِ فَلَيْسَ لِلَّهِ حَاجَةٌ فِي أَنْ يَدَعَ طَعَامَهُ وَشَرَابَهُ",
          "Transliteration: Man lam yada' qawlaz-zuri wal-'amala bihi falaysa lillahi hajatun fee an yada'a ta'amahu wa sharabah.",
          "Translation: Whoever does not leave false words and actions, Allah does not need him to leave his food and drink.",
          "Sahih Al-Bukhari: 1903, Sunan Abi Dawud: 2362"
      ));

      list.add(new RamadanTopicItem(
          5,
          "5. Suhoor & Iftar",
          "5. Sunnah Manners & Masnoon Duas of Suhoor & Iftar",
          "Sunnah: Blessed Suhoor and Prompt Iftar",
          "Eating the pre-dawn meal (Suhoor) and hastening to break the fast (Iftar) upon sunset are primary Sunnahs of the Prophet (ﷺ).",
          "Sunnah of Suhoor:\n• Eating Suhoor is blessed and a recommended Sunnah (even a sip of water)\n• Delaying Suhoor until near true dawn (Fajr) is Mustahabb.\n\nSunnah of Iftar:\n• Hastening to break fast immediately after sunset is verified\n• Breaking fast with fresh or dried dates, or with water\n• Supplication at the time of Iftar is accepted.",
          "Niyyah: Intention is in the heart; no vocal ritual phrase is mandatory.",
          "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
          "Transliteration: Dhahabadh-dhama'u wabtallatil-'urooqu wa thabatal-ajru in sha Allah.",
          "Translation: The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
          "Sunan Abi Dawud: 2357, Sunan ad-Daraqutni: 2279"
      ));

      list.add(new RamadanTopicItem(
          6,
          "6. Tarawih",
          "6. Virtues of Salatut Tarawih & Qiyamul Layl",
          "Sunnah: Sunnah Mu'akkadah & Means of Forgiveness",
          "Praying Tarawih in congregation every night of Ramadan after Isha is an emphasized Sunnah carrying immense rewards.",
          "Virtues and Manners:\n• Prayed in units of 2 rak'ahs with salam\n• Reciting Quran with tranquility and complete devotion\n• Praying with the Imam until completion earns reward of whole night in prayer (Tirmidhi: 806)\n• Tahajjud and Istighfar in the final third of the night.",
          "Note: Fabricated prolonged group chants between rak'ahs have no basis in authentic Sunnah; make personal silent supplications.",
          "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
          "Transliteration: Man qama ramadana imanan wahtisaban ghufira lahu ma taqaddama min dhanbih.",
          "Translation: Whoever prays during the nights of Ramadan with faith and seeking reward, all his previous sins will be forgiven.",
          "Sahih Al-Bukhari: 2009, Sahih Muslim: 759"
      ));

      list.add(new RamadanTopicItem(
          7,
          "7. Laylat al-Qadr",
          "7. The Glorious Night of Decree & Itikaf",
          "Virtue: Better than a thousand months",
          "In the last ten nights of Ramadan lies the Night of Decree (Laylat al-Qadr), worship during which surpasses 1000 months (83 years and 4 months).",
          "Seeking Laylat al-Qadr:\n• Search during odd nights of the last ten days (21, 23, 25, 27, 29th)\n• Spend the night in voluntary prayer, Quran recitation, and supplication\n• Aisha (RA) asked the Prophet (ﷺ) what to say if she caught the Night; he taught the dua below.",
          "Itikaf:\n• Seclusion in the mosque from sunset of 20th Ramadan until Eid moon sighting is Sunnah Mu'akkadah alal-Kifayah.",
          "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
          "Transliteration: Allahumma innaka 'afuwwun tuhibbul-'afwa fa'fu 'annee.",
          "Translation: O Allah, You are Most Forgiving, and You love forgiveness; so forgive me.",
          "Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850"
      ));

      list.add(new RamadanTopicItem(
          8,
          "8. Zakat & Fitr",
          "8. Calculation of Zakat & Rules of Zakat al-Fitr",
          "Obligation: Zakat on wealthy, Fitr on everyone",
          "Ramadan is the foremost time for charity and paying annual Zakat. Sadaqat al-Fitr is mandatory before the Eid prayer on Eid morning.",
          "Rules of Zakat:\n• Nisab: 7.5 tola gold or 52.5 tola silver or equivalent cash/trade assets held for 1 lunar year (pay 2.5%).\n\nRules of Zakat al-Fitr:\n• Purpose: Purifies the fasting person from idle talk and feeds the needy on Eid\n• Who must pay: Every head of household for themselves and dependents\n• Quantity: 1 Sa' (approx 2.4 kg) of staple food grain or its market value\n• Timing: Must reach the needy before the Eid prayer (Sahih Al-Bukhari: 1509).",
          "Paying Fitr after the Eid prayer counts only as ordinary charity, not as Zakat al-Fitr.",
          "فَرَضَ رَسُولُ اللَّهِ ﷺ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ وَطُعْمَةً لِلْمَسَاكِينِ",
          "Transliteration: Farada Rasulullahi ﷺ zakatal-fitri tuhratan lis-sa'imi minal-laghwi war-rafathi wa tu'matan lil-masakeen.",
          "Translation: The Messenger of Allah (ﷺ) prescribed Zakat al-Fitr as a purification for the fasting person from idle talk and obscenities and as food for the poor.",
          "Sunan Abi Dawud: 1609, Sunan Ibn Majah: 1827"
      ));

      list.add(new RamadanTopicItem(
          9,
          "9. Special Deeds",
          "9. Best Deeds in Ramadan & Authentic Hadith",
          "Virtue: Immense multiplication of rewards",
          "Ramadan is the spring of good deeds. The gates of Paradise are opened, the gates of Hell are closed, and devils are chained.",
          "Top 10 Deeds in Ramadan:\n1. 5 daily prayers in congregation with Takbir Ula\n2. Regular Quran recitation with reflection\n3. Tahajjud and sincere repentance at late night\n4. Special supplications before Iftar\n5. Providing Iftar to other fasting persons\n6. Daily charity according to means\n7. Maintaining and strengthening family ties\n8. Guarding eyes and tongue from sins\n9. Sincere repentance and 100x Istighfar daily\n10. Seeking Laylat al-Qadr and Itikaf in last ten days.",
          "Sahih Hadith: 'When Ramadan begins, the gates of heaven are opened, the gates of Hell are closed, and the devils are chained.' (Sahih Al-Bukhari: 1899, Sahih Muslim: 1079).",
          "إِذَا دَخَلَ شَهْرُ رَمَضَانَ فُتِّحَتْ أَبْوَابُ السَّمَاءِ وَغُلِّقَتْ أَبْوَابُ جَهَنَّمَ وَسُلْسِلَتِ الشَّيَاطِينُ",
          "Transliteration: Idha dakhala shahru ramadana futtihat abwabus-sama'i wa ghulliqat abwabu jahannama wa sulsilatish-shayateen.",
          "Translation: When the month of Ramadan begins, the gates of the heavens are opened, the gates of Hell are closed, and the devils are chained.",
          "Sahih Al-Bukhari: 1899, Sahih Muslim: 1079"
      ));

      list.add(new RamadanTopicItem(
          10,
          "10. Quran Goals",
          "10. Practical Quran Khatam Routine in Ramadan",
          "Goal: Completing 1 or more full recitations in 30 days",
          "Ramadan is the month the Quran was revealed. Angel Jibreel reviewed the entire Quran with the Prophet (ﷺ) every Ramadan.",
          "Simple 5-Prayer Routine to complete 1 Khatam in 30 days:\n• The Quran has 30 Juz (Paras), roughly 20 pages each.\n• After Fajr: 4 pages\n• After Dhuhr: 4 pages\n• After Asr: 4 pages\n• After Maghrib: 4 pages\n• After Isha: 4 pages\n• Total = 20 pages (1 Juz) per day -> 30 Juz completed in 30 days!",
          "Routine for 2 Khatams in 30 days:\n• 4 pages before and 4 pages after each of the 5 daily prayers = 40 pages (2 Juz) per day -> 2 full Khatams in 30 days!",
          "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ وَبَيِّنَاتٍ مِّنَ الْهُدَىٰ وَالْفُرْقَانِ",
          "Transliteration: Shahru ramadanalladhee unzila feehil-qur'anu hudal-linnasi wa bayyinatim-minal-huda wal-furqan.",
          "Translation: The month of Ramadan in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion.",
          "Surah Al-Baqarah: 185, Sahih Al-Bukhari: 4997"
      ));

      topicsLiveData.setValue(list);
      if (!list.isEmpty()) {
        selectedTopicLiveData.setValue(list.get(0));
      }
    }
  }
}
