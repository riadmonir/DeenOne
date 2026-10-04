package com.devflux.deenone.features.salahguide.data;

import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Salah Dua: নামাজে শয়তানের ধোঁকা থেকে বাঁচার দোয়া (Protection from Whispers of Satan in Salah).
 * 100% verbatim text matching user screenshots, user prompts, authentic Hadiths, and references.
 */
public class SalahWaswasaDuaContentRepository {

    public static List<HajjHistoryCardItem> getCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. নামাজে শয়তানের ধোঁকা থেকে বাঁচার দোয়া (Verbatim from User Screenshot & Prompt)
        list.add(new HajjHistoryCardItem(
            1,
            "নামাজে শয়তানের ধোঁকা থেকে বাঁচার দোয়া",
            "Dua for Protection from Satan in Salah",
            "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ উচ্চারণ: আউযু-বিল্লাহি মিনাশ্ শাইত্ব-নির রাজীম। অর্থ: বিতাড়িত শয়তান থেকে আল্লাহ তায়ালার নিকট আশ্রয় প্রার্থনা করছি।",
            "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ Transliteration: A'udhu billahi minash-shaytanir rajeem. Meaning: I seek refuge in Allah from the accursed Satan.",
            "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ\n\n" +
            "উচ্চারণ:\n\n" +
            "আউযু-বিল্লাহি মিনাশ্ শাইত্ব-নির রাজীম।\n\n" +
            "অর্থ:\n\n" +
            "বিতাড়িত শয়তান থেকে আল্লাহ তায়ালার নিকট আশ্রয় প্রার্থনা করছি।",
            "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ\n\n" +
            "Transliteration:\n\n" +
            "A'udhu billahi minash-shaytanir rajeem.\n\n" +
            "Meaning:\n\n" +
            "\"I seek refuge in Allah from the accursed Satan.\""
        ));

        return list;
    }
}
