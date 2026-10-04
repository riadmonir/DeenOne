package com.devflux.deenone.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuranApiResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private QuranSurahData data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public QuranSurahData getData() {
        return data;
    }

    public static class QuranSurahData {
        @SerializedName("number")
        private int number;

        @SerializedName("name")
        private String name;

        @SerializedName("englishName")
        private String englishName;

        @SerializedName("englishNameTranslation")
        private String englishNameTranslation;

        @SerializedName("revelationType")
        private String revelationType;

        @SerializedName("numberOfAyahs")
        private int numberOfAyahs;

        @SerializedName("ayahs")
        private List<AyahData> ayahs;

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getEnglishName() {
            return englishName;
        }

        public String getEnglishNameTranslation() {
            return englishNameTranslation;
        }

        public String getRevelationType() {
            return revelationType;
        }

        public int getNumberOfAyahs() {
            return numberOfAyahs;
        }

        public List<AyahData> getAyahs() {
            return ayahs;
        }
    }

    public static class AyahData {
        @SerializedName("number")
        private int number;

        @SerializedName("text")
        private String text;

        @SerializedName("numberInSurah")
        private int numberInSurah;

        @SerializedName("juz")
        private int juz;

        @SerializedName("page")
        private int page;

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

        public int getNumberInSurah() {
            return numberInSurah;
        }

        public int getJuz() {
            return juz;
        }

        public int getPage() {
            return page;
        }
    }
}
