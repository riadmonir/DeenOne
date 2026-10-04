package com.devflux.deenone.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HadithApiResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("hadiths")
    private HadithPaginationData hadiths;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HadithPaginationData getHadiths() {
        return hadiths;
    }

    public static class HadithPaginationData {
        @SerializedName("data")
        private List<HadithItemData> data;

        public List<HadithItemData> getData() {
            return data;
        }
    }

    public static class HadithItemData {
        @SerializedName("id")
        private long id;

        @SerializedName("hadithNumber")
        private String hadithNumber;

        @SerializedName("englishNarrator")
        private String englishNarrator;

        @SerializedName("hadithArabic")
        private String hadithArabic;

        @SerializedName("hadithEnglish")
        private String hadithEnglish;

        @SerializedName("hadithBengali")
        private String hadithBengali;

        @SerializedName("headingBengali")
        private String headingBengali;

        @SerializedName("bookSlug")
        private String bookSlug;

        @SerializedName("volume")
        private String volume;

        @SerializedName("status")
        private String status;

        public long getId() {
            return id;
        }

        public String getHadithNumber() {
            return hadithNumber;
        }

        public String getEnglishNarrator() {
            return englishNarrator;
        }

        public String getHadithArabic() {
            return hadithArabic;
        }

        public String getHadithEnglish() {
            return hadithEnglish;
        }

        public String getHadithBengali() {
            return hadithBengali;
        }

        public String getHeadingBengali() {
            return headingBengali;
        }

        public String getBookSlug() {
            return bookSlug;
        }

        public String getVolume() {
            return volume;
        }

        public String getStatus() {
            return status;
        }
    }
}
