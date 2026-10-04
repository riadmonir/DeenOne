package com.devflux.deenone.features.books.model;

import java.util.ArrayList;
import java.util.List;

public class BookCategoryItem {

  private final String key;
  private final String titleBengali;
  private final String iconEmoji;
  private final String description;
  private boolean isSelected;

  public BookCategoryItem(String key, String titleBengali, String iconEmoji, String description, boolean isSelected) {
    this.key = key;
    this.titleBengali = titleBengali;
    this.iconEmoji = iconEmoji;
    this.description = description;
    this.isSelected = isSelected;
  }

  public String getKey() {
    return key;
  }

  public String getTitleBengali() {
    return titleBengali;
  }

  public String getTitleEnglish() {
    if ("all".equalsIgnoreCase(key)) return "All Books";
    return key;
  }

  public String getIconEmoji() {
    return iconEmoji;
  }

  public String getDescription() {
    return description;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  public static List<BookCategoryItem> getDefaultCategories() {
    List<BookCategoryItem> list = new ArrayList<>();
    list.add(new BookCategoryItem("all", "সকল বই", "", "সকল বিষয়ের ইসলামিক বই", true));
    list.add(new BookCategoryItem("Quran", "কুরআন", "", "আল-কুরআন ও কুরআনীয় জ্ঞান", false));
    list.add(new BookCategoryItem("Tafsir", "তাফসীর", "", "তাফসীর ও বিস্তারিত ব্যাখ্যা", false));
    list.add(new BookCategoryItem("Hadith", "হাদিস", "", "সহিহ হাদিস সংকলন ও ব্যাখ্যা", false));
    list.add(new BookCategoryItem("Fiqh", "ফিকহ", "", "ফিকহ ও দৈনন্দিন মাসায়েল", false));
    list.add(new BookCategoryItem("Aqeedah", "আকিদা", "", "বিশুদ্ধ তাওহীদ ও ঈমান", false));
    list.add(new BookCategoryItem("Seerah", "সীরাত", "", "সীরাতুন নবী (সা.) এর জীবনচরিত", false));
    list.add(new BookCategoryItem("Islamic History", "ইতিহাস", "", "ইসলামের সোনালী ইতিহাস", false));
    list.add(new BookCategoryItem("Dua & Azkar", "দোয়া ও আজকার", "", "মাসনুন দোয়া ও জিকির", false));
    list.add(new BookCategoryItem("Salah", "সালাত", "", "নামাজ ও এবাদতের বিধান", false));
    list.add(new BookCategoryItem("Islamic Ethics", "আখলাক", "", "চরিত্র গঠন ও আত্মশুদ্ধি", false));
    list.add(new BookCategoryItem("Family & Marriage", "পরিবার ও বিবাহ", "‍‍", "আদর্শ পারিবারিক জীবন", false));
    list.add(new BookCategoryItem("Children", "শিশু-কিশোর", "", "ছোটদের ইসলামিক গল্প ও শিক্ষা", false));
    list.add(new BookCategoryItem("Bangla Islamic Books", "বাংলা ইসলামিক বই", "", "অনুবাদ ও মৌলিক বাংলা কিতাব", false));
    list.add(new BookCategoryItem("English Islamic Books", "English Books", "", "Authentic English Islamic Classics", false));
    return list;
  }
}
