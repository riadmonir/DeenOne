package com.devflux.deenone.features.faraid.ui;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetFaraidCalculatorBinding;
import com.devflux.deenone.features.faraid.adapter.FaraidBlockedAdapter;
import com.devflux.deenone.features.faraid.adapter.FaraidResultAdapter;
import com.devflux.deenone.features.faraid.engine.FaraidAiExplanationEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.engine.FaraidCitationHelper;
import com.devflux.deenone.features.faraid.engine.FaraidValidationEngine;
import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidDisclaimerHelper;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.FaraidValidationResult;
import com.devflux.deenone.features.faraid.model.HeirShareResult;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;

/**
 * Interactive Classical Islamic Faraid Calculator Full-Screen Page Dialog.
 * Matches bottom_sheet_faraid_calculator.xml and supports full transparency,
 * real-time calculation, four Madhabs, Bangladesh 1961 law, and copy/sharing.
 */
public class FaraidCalculatorPageDialog {

    private final Context context;
    private final FullScreenPageDialog dialog;
    private final BottomSheetFaraidCalculatorBinding binding;
    private FaraidResultAdapter resultsAdapter;
    private FaraidBlockedAdapter blockedAdapter;
    private FaraidCalculationResult lastResult;
    private FaraidInput.Madhab selectedMadhab = FaraidInput.Madhab.HANAFI;
    private boolean isMaleDeceased = true;

    public static void show(@NonNull Context context) {
        new FaraidCalculatorPageDialog(context).show();
    }

    public FaraidCalculatorPageDialog(@NonNull Context context) {
        this.context = context;
        this.dialog = new FullScreenPageDialog(context);
        this.binding = BottomSheetFaraidCalculatorBinding.inflate(LayoutInflater.from(context));
        this.dialog.setContentView(binding.getRoot());

        setupRecyclerViews();
        setupTopBar();
        setupGenderSelector();
        setupMadhabSelector();
        setupPresetChips();
        setupSectionToggles();
        setupCounterButtons();
        setupActionButtons();
        setupDalilChips();
        setupPropertyInputs();
        applyLocalization();
    }

    private void applyLocalization() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        binding.tvFaraidTopTitle.setText(isBn ? "উত্তরাধিকার বণ্টন" : "Inheritance Distribution");

        if (binding.tvIntroHeroTitle != null) {
            binding.tvIntroHeroTitle.setText(isBn ? "ক্লাসিক্যাল সুন্নি ফারায়েজ ও বাংলাদেশ আইন" : "Classical Sunni Faraid & Bangladesh Law");
        }
        if (binding.tvIntroHeroSubtitle != null) {
            binding.tvIntroHeroSubtitle.setText(isBn
                    ? "পবিত্র কুরআন, চার মাযহাব, ইসলামিক ফাউন্ডেশন এবং বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ ভিত্তিক নির্ভরযোগ্য সমাধান।"
                    : "Authentic calculation based on the Holy Qur'an, Four Sunni Madhabs, Islamic Foundation and Bangladesh Muslim Family Laws Ordinance 1961.");
        }
        if (binding.tvSec1Title != null) {
            binding.tvSec1Title.setText(isBn ? "১. মৃত ব্যক্তি, মাযহাব ও মোট সম্পত্তির বিবরণ" : "1. Deceased, Madhab & Estate Details");
        }
        if (binding.tvCalculationMethodSub != null) {
            binding.tvCalculationMethodSub.setText(isBn ? "হানাফি, শাফেয়ী, মালিকি ও হাম্বলী মাযহাব ভিত্তিক গণনাকারী" : "Calculator based on Hanafi, Shafi'i, Maliki & Hanbali schools");
        }
        if (binding.btnChangeCalculationMethod != null) {
            binding.btnChangeCalculationMethod.setText(isBn ? "পদ্ধতি পরিবর্তন করুন ▾" : "Change Method ▾");
        }
        if (binding.chipMadhabHanafi != null) binding.chipMadhabHanafi.setText(isBn ? "হানাফি" : "Hanafi");
        if (binding.chipMadhabShafi != null) binding.chipMadhabShafi.setText(isBn ? "শাফেয়ী" : "Shafi'i");
        if (binding.chipMadhabMaliki != null) binding.chipMadhabMaliki.setText(isBn ? "মালিকি" : "Maliki");
        if (binding.chipMadhabHanbali != null) binding.chipMadhabHanbali.setText(isBn ? "হাম্বলী" : "Hanbali");
        setMadhab(this.selectedMadhab != null ? this.selectedMadhab : FaraidInput.Madhab.HANAFI);
        if (binding.tvGenderTitle != null) {
            binding.tvGenderTitle.setText(isBn ? "মৃত ব্যক্তির লিঙ্গ:" : "Deceased Gender:");
        }
        binding.btnGenderMale.setText(isBn ? "মৃত পুরুষ" : "Deceased Male");
        binding.btnGenderFemale.setText(isBn ? "মৃত নারী" : "Deceased Female");
        if (binding.tvEstateCashTitle != null) {
            binding.tvEstateCashTitle.setText(isBn ? "মোট ত্যাজ্য সম্পদ / নগদ অর্থ (টাকা):" : "Total Estate / Cash Assets (BDT):");
        }
        binding.etTotalEstate.setHint(isBn ? "টাকার পরিমাণ লিখুন (যেমন: 5000000)" : "Enter amount (e.g. 5000000)");

        if (binding.chipPreset5Lakh != null) binding.chipPreset5Lakh.setText(isBn ? "+ ৫ লাখ" : "+ 5 Lakh");
        if (binding.chipPreset10Lakh != null) binding.chipPreset10Lakh.setText(isBn ? "+ ১০ লাখ" : "+ 10 Lakh");
        if (binding.chipPreset50Lakh != null) binding.chipPreset50Lakh.setText(isBn ? "+ ৫০ লাখ" : "+ 50 Lakh");
        if (binding.chipPreset1Crore != null) binding.chipPreset1Crore.setText(isBn ? "+ ১ কোটি" : "+ 1 Crore");
        if (binding.chipPreset5Crore != null) binding.chipPreset5Crore.setText(isBn ? "+ ৫ কোটি" : "+ 5 Crore");

        if (binding.tvPropertyLandTitle != null) {
            binding.tvPropertyLandTitle.setText(isBn ? "মোট স্থাবর সম্পত্তি / জমি:" : "Total Real Estate & Land Property:");
        }
        binding.etTotalProperty.setHint(isBn ? "জমির পরিমাণ লিখুন (যেমন: 100)" : "Enter land area (e.g. 100)");

        if (binding.chipPreset5Shatak != null) binding.chipPreset5Shatak.setText(isBn ? "+ ৫ শতক" : "+ 5 Decimal");
        if (binding.chipPreset10Shatak != null) binding.chipPreset10Shatak.setText(isBn ? "+ ১০ শতক" : "+ 10 Decimal");
        if (binding.chipPreset20Shatak != null) binding.chipPreset20Shatak.setText(isBn ? "+ ২০ শতক" : "+ 20 Decimal");
        if (binding.chipPreset33Shatak != null) binding.chipPreset33Shatak.setText(isBn ? "+ ৩৩ শতক" : "+ 33 Decimal");
        if (binding.chipPreset1Acre != null) binding.chipPreset1Acre.setText(isBn ? "+ ১০০ শতক" : "+ 100 Decimal");

        if (binding.tvLaw1961Title != null) {
            binding.tvLaw1961Title.setText(isBn ? "বাংলাদেশ আইন ১৯৬১ (ধারা ৪ - এতিম নাতি)" : "Bangladesh Law 1961 (Section 4 - Orphan Grandchild)");
        }
        if (binding.tvLaw1961Sub != null) {
            binding.tvLaw1961Sub.setText(isBn ? "মৃত সন্তানের এতিম নাতি/নাতনির প্রতিনিধিত্বমূলক অংশ" : "Representative share for orphan grandchildren of predeceased children");
        }

        if (binding.tvDeductionsHeaderTitle != null) {
            binding.tvDeductionsHeaderTitle.setText(isBn ? "বণ্টনপূর্ব অপরিহার্য কর্তন (কাফন, ঋণ ও অসিয়ত)" : "Pre-distribution Deductions (Funeral, Debt & Bequest)");
        }
        if (binding.tvFuneralTitle != null) {
            binding.tvFuneralTitle.setText(isBn ? "১. কাফন-দাফন ও জানাজা খরচ (টাকা):" : "1. Funeral & Burial Expenses (BDT):");
        }
        if (binding.etFuneralExpense != null) binding.etFuneralExpense.setHint(isBn ? "০" : "0");
        if (binding.tvDebtTitle != null) {
            binding.tvDebtTitle.setText(isBn ? "২. অপরিশোধিত ঋণ ও স্ত্রীর দেনমোহর (টাকা - অগ্রাধিকারপ্রাপ্ত):" : "2. Unsettled Debts & Mahr (BDT - Priority):");
        }
        if (binding.etDebtAmount != null) binding.etDebtAmount.setHint(isBn ? "০" : "0");
        if (binding.tvWasiyyahTitle != null) {
            binding.tvWasiyyahTitle.setText(isBn ? "৩. অসিয়ত / দান (সর্বোচ্চ অবশিষ্টের ১/৩ অংশ):" : "3. Bequest / Wasiyyah (Max 1/3 of Net Estate):");
        }
        if (binding.etWasiyyahAmount != null) binding.etWasiyyahAmount.setHint(isBn ? "০" : "0");
        if (binding.tvWasiyyahToHeirTitle != null) {
            binding.tvWasiyyahToHeirTitle.setText(isBn ? "অসিয়ত কি কোনো উত্তরাধিকারীর অনুকূলে?" : "Is the bequest in favor of a legal heir?");
        }
        if (binding.tvWasiyyahToHeirSub != null) {
            binding.tvWasiyyahToHeirSub.setText(isBn ? "রাসূলুল্লাহ ﷺ বলেছেন: «لاَ وَصِيَّةَ لِوَارِثٍ» কোনো ওয়ারিশের জন্য অসিয়ত কার্যকর নয় (তিরমিজি: ২১২০)।"
                    : "The Prophet ﷺ said: 'There is no bequest for an heir' (Tirmidhi: 2120).");
        }

        if (binding.tvSec2Title != null) {
            binding.tvSec2Title.setText(isBn ? "২. জীবিত ওয়ারিশগণের তালিকা" : "2. Surviving Legal Heirs");
        }
        if (binding.tvWifeTitle != null) binding.tvWifeTitle.setText(isBn ? "স্ত্রী (সংখ্যা):" : "Wives (Count):");
        if (binding.tvHusbandTitle != null) binding.tvHusbandTitle.setText(isBn ? "স্বামী জীবিত আছেন?" : "Is Husband alive?");
        if (binding.tvFatherTitle != null) binding.tvFatherTitle.setText(isBn ? "পিতা জীবিত আছেন?" : "Is Father alive?");
        if (binding.tvMotherTitle != null) binding.tvMotherTitle.setText(isBn ? "মাতা জীবিত আছেন?" : "Is Mother alive?");
        if (binding.tvSonTitle != null) binding.tvSonTitle.setText(isBn ? "পুত্র সন্তান সংখ্যা:" : "Sons (Count):");
        if (binding.tvDaughterTitle != null) binding.tvDaughterTitle.setText(isBn ? "কন্যা সন্তান সংখ্যা:" : "Daughters (Count):");

        if (binding.tvExtendedHeirsHeaderTitle != null) {
            binding.tvExtendedHeirsHeaderTitle.setText(isBn ? "অন্যান্য ওয়ারিশ (দাদা-দাদী, নানী, নাতি-নাতনি, সৎ ভাই-বোন, চাচা, ভাতিজা)"
                    : "Extended Heirs (Grandparents, Grandchildren, Siblings, Uncles, Nephews)");
        }
        if (binding.tvGrandfatherTitle != null) binding.tvGrandfatherTitle.setText(isBn ? "পিতামহ (দাদা) জীবিত আছেন?" : "Is Paternal Grandfather alive?");
        if (binding.tvPaternalGrandmotherTitle != null) binding.tvPaternalGrandmotherTitle.setText(isBn ? "পৈতৃক দাদী জীবিত আছেন?" : "Is Paternal Grandmother alive?");
        if (binding.tvMaternalGrandmotherTitle != null) binding.tvMaternalGrandmotherTitle.setText(isBn ? "মাতৃক নানী জীবিত আছেন?" : "Is Maternal Grandmother alive?");
        if (binding.tvGrandsonTitle != null) binding.tvGrandsonTitle.setText(isBn ? "নাতি (পুত্রের ছেলে) সংখ্যা:" : "Grandsons (Son's Son):");
        if (binding.tvGranddaughterTitle != null) binding.tvGranddaughterTitle.setText(isBn ? "নাতনি (পুত্রের মেয়ে) সংখ্যা:" : "Granddaughters (Son's Daughter):");
        if (binding.tvFullBrotherTitle != null) binding.tvFullBrotherTitle.setText(isBn ? "সহোদর ভাই সংখ্যা:" : "Full Brothers (Count):");
        if (binding.tvFullSisterTitle != null) binding.tvFullSisterTitle.setText(isBn ? "সহোদর বোন সংখ্যা:" : "Full Sisters (Count):");
        if (binding.tvConsanguineBrotherTitle != null) binding.tvConsanguineBrotherTitle.setText(isBn ? "বৈমাত্রেয় ভাই (সৎ ভাই, পিতার দিক):" : "Consanguine Brothers (Father's side):");
        if (binding.tvConsanguineSisterTitle != null) binding.tvConsanguineSisterTitle.setText(isBn ? "বৈমাত্রেয় বোন (সৎ বোন, পিতার দিক):" : "Consanguine Sisters (Father's side):");
        if (binding.tvUterineBrotherTitle != null) binding.tvUterineBrotherTitle.setText(isBn ? "বৈপিত্রীয় ভাই (সৎ ভাই, মাতার দিক):" : "Uterine Brothers (Mother's side):");
        if (binding.tvUterineSisterTitle != null) binding.tvUterineSisterTitle.setText(isBn ? "বৈপিত্রীয় বোন (সৎ বোন, মাতার দিক):" : "Uterine Sisters (Mother's side):");
        if (binding.tvNephewTitle != null) binding.tvNephewTitle.setText(isBn ? "ভাতিজা (সহোদর/বৈমাত্রেয় ভাইয়ের পুত্র):" : "Nephews (Brother's Son):");
        if (binding.tvUncleTitle != null) binding.tvUncleTitle.setText(isBn ? "চাচা (পিতার সহোদর/বৈমাত্রেয় ভাই):" : "Paternal Uncles (Father's Brother):");

        binding.btnCalculateFaraid.setText(isBn ? "ফারায়েজ বণ্টন হিসাব করুন" : "Calculate Inheritance");
        binding.btnRecalculateFaraid.setText(isBn ? "পুনরায় হিসাব" : "Recalculate");
        binding.btnShareFaraidResult.setText(isBn ? "কপি ও শেয়ার" : "Share Results");
        binding.btnViewAiFatwa.setText(isBn ? "শরীয়াহ বিশ্লেষণ ও বিস্তারিত মাসআলা দেখুন" : "View Shariah Analysis & Mas'alah");

        if (binding.tvMadhabDiffHeader != null) {
            binding.tvMadhabDiffHeader.setText(isBn ? "মাযহাবগত ইখতিলাফ ও ফতোয়া স্পষ্টকরণ" : "Madhab Differences & Shariah Clarification");
        }
        if (binding.tvResultsHeaderTitle != null) {
            binding.tvResultsHeaderTitle.setText(isBn ? "ফারায়েজ বণ্টন হিসাব ও সারসংক্ষেপ" : "Inheritance Distribution & Summary");
        }
        if (binding.tvResGrossEstateLabel != null) binding.tvResGrossEstateLabel.setText(isBn ? "১. সর্বমোট ত্যাজ্য সম্পদ:" : "1. Gross Estate:");
        if (binding.tvResFuneralLabel != null) binding.tvResFuneralLabel.setText(isBn ? "২. দাফন ও কাফন বাবদ খরচ:" : "2. Funeral & Burial Expenses:");
        if (binding.tvResDebtLabel != null) binding.tvResDebtLabel.setText(isBn ? "৩. অপরিশোধিত মোট ঋণ:" : "3. Settled Debts:");
        if (binding.tvResWasiyyahLabel != null) binding.tvResWasiyyahLabel.setText(isBn ? "৪. কার্যকরী অসিয়ত (সর্বোচ্চ ১/৩):" : "4. Valid Bequest (Max 1/3):");
        if (binding.tvResNetEstateLabel != null) binding.tvResNetEstateLabel.setText(isBn ? "বণ্টনযোগ্য অবশিষ্ট অর্থ / সম্পদ:" : "Net Distributable Estate:");
        if (binding.tvResNetPropertyLabel != null) binding.tvResNetPropertyLabel.setText(isBn ? "বণ্টনযোগ্য মোট জমি:" : "Net Distributable Land:");
        if (binding.tvResAslMasalahLabel != null) binding.tvResAslMasalahLabel.setText(isBn ? "আসল মাসআলা:" : "Base Denominator:");
        if (binding.tvResEligibleHeirsCountLabel != null) binding.tvResEligibleHeirsCountLabel.setText(isBn ? "মোট উপযুক্ত ওয়ারিশ ক্যাটাগরি:" : "Eligible Heir Categories:");

        if (binding.tvPureShareDescription != null) {
            binding.tvPureShareDescription.setText(isBn
                    ? "কোনো নির্দিষ্ট অর্থ বা জমি উল্লেখ না করায় কুরআন ও সুন্নাহর বিধান অনুযায়ী শুধুমাত্র আনুপাতিক হিস্যা (ভগ্নাংশ ও শতকরা অংশ) নির্ধারণ করা হয়েছে। নিচে সারসংক্ষেপ টেবিলে প্রত্যেকের নির্ধারিত অংশ বিস্তারিত দেওয়া হয়েছে।"
                    : "Since no specific estate valuation was entered, proportionate shares (fractions & percentages) are determined according to Quranic rules. See summary table below for details.");
        }

        if (binding.tvSummaryTableTitle != null) {
            binding.tvSummaryTableTitle.setText(isBn ? "যোগ্য ওয়ারিশদের সংক্ষিপ্ত হিস্যা বিবরণী" : "Eligible Heirs Share Summary Table");
        }
        if (binding.thHeir != null) binding.thHeir.setText(isBn ? "ওয়ারিশ" : "Heir");
        if (binding.thShare != null) binding.thShare.setText(isBn ? "অংশ" : "Share");
        if (binding.thPercent != null) binding.thPercent.setText(isBn ? "শতকরা" : "Percent");
        if (binding.thAmount != null) binding.thAmount.setText(isBn ? "প্রাপ্য" : "Entitled");

        if (binding.tvBlockedHeirsTitle != null) {
            binding.tvBlockedHeirsTitle.setText(isBn ? "বঞ্চিত আত্মীয়স্বজন ও শরীয়াহ কারণ:" : "Excluded Heirs & Shariah Cause:");
        }
        if (binding.tvBeneficiariesTitle != null) {
            binding.tvBeneficiariesTitle.setText(isBn ? "যোগ্য অংশীদারদের পূর্ণাঙ্গ শরীয়াহ হিস্যা ও হিসাব:" : "Eligible Heirs Detailed Shariah Shares:");
        }
        if (binding.tvAuditLogTitle != null) {
            binding.tvAuditLogTitle.setText(isBn ? "ধাপে ধাপে শরীয়াহ ও আইনি গণনার পূর্ণাঙ্গ অডিট রিপোর্ট" : "Step-by-Step Shariah & Legal Audit Report");
        }
        if (binding.tvRefSectionTitle != null) {
            binding.tvRefSectionTitle.setText(isBn ? "কুরআন ও হাদিসের সরাসরি রেফারেন্সসমূহ (ক্লিক করুন):" : "Direct Quran & Hadith References (Click to view):");
        }
        if (binding.tvOverallDalilTitle != null) {
            binding.tvOverallDalilTitle.setText(isBn ? "মূলনীতি, চার মাযহাব ও বাংলাদেশ আইন" : "Foundational Principles, Four Madhabs & Law");
        }
        if (binding.tvDisclaimerTitle != null) {
            binding.tvDisclaimerTitle.setText(isBn ? "শরীয়াহ ও আইনি ডিসক্লেইমার" : "Shariah & Legal Disclaimer");
        }
        if (binding.tvDisclaimerBody != null) {
            binding.tvDisclaimerBody.setText(isBn ? FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN : FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);
        }
        if (binding.tvDisclaimerWarning != null) {
            binding.tvDisclaimerWarning.setText(isBn ? FaraidDisclaimerHelper.WARNING_DISCLAIMER_BN : FaraidDisclaimerHelper.WARNING_DISCLAIMER_EN);
        }

        // Initialize counters to localized "0" or "1"
        binding.tvWifeCount.setText(isBn ? "১" : "1");
        binding.tvSonCount.setText(isBn ? "১" : "1");
        binding.tvDaughterCount.setText(isBn ? "১" : "1");
        binding.tvGrandsonCount.setText(isBn ? "০" : "0");
        binding.tvGranddaughterCount.setText(isBn ? "০" : "0");
        binding.tvFullBrotherCount.setText(isBn ? "০" : "0");
        binding.tvFullSisterCount.setText(isBn ? "০" : "0");
        binding.tvConsanguineBrotherCount.setText(isBn ? "০" : "0");
        binding.tvConsanguineSisterCount.setText(isBn ? "০" : "0");
        binding.tvUterineBrotherCount.setText(isBn ? "০" : "0");
        binding.tvUterineSisterCount.setText(isBn ? "০" : "0");
        binding.tvNephewCount.setText(isBn ? "০" : "0");
        binding.tvUncleCount.setText(isBn ? "০" : "0");
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setupRecyclerViews() {
        resultsAdapter = new FaraidResultAdapter(context, new ArrayList<>(), this::showDalilPopup);
        binding.rvFaraidHeirResults.setLayoutManager(new LinearLayoutManager(context));
        binding.rvFaraidHeirResults.setAdapter(resultsAdapter);
        binding.rvFaraidHeirResults.setNestedScrollingEnabled(false);

        blockedAdapter = new FaraidBlockedAdapter(new ArrayList<>());
        binding.rvBlockedHeirs.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBlockedHeirs.setAdapter(blockedAdapter);
        binding.rvBlockedHeirs.setNestedScrollingEnabled(false);
    }

    private void setupTopBar() {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnFaraidBack);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnFaraidInfo);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnFaraidAiGuide);

        binding.btnFaraidBack.setOnClickListener(v -> dismiss());
        binding.btnFaraidInfo.setOnClickListener(v -> showInfoDialog());
        binding.btnFaraidAiGuide.setOnClickListener(v -> showAiGuideDialog());
    }

    private void setupGenderSelector() {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnGenderMale);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnGenderFemale);

        binding.btnGenderMale.setOnClickListener(v -> setGender(true));
        binding.btnGenderFemale.setOnClickListener(v -> setGender(false));
    }

    private void setGender(boolean male) {
        isMaleDeceased = male;
        if (male) {
            binding.btnGenderMale.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.primary_green)));
            binding.btnGenderMale.setTextColor(context.getResources().getColor(R.color.white));
            binding.btnGenderFemale.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.bg_card_secondary)));
            binding.btnGenderFemale.setTextColor(context.getResources().getColor(R.color.text_secondary));

            binding.layoutWifeSection.setVisibility(View.VISIBLE);
            binding.layoutHusbandSection.setVisibility(View.GONE);
            binding.switchHusbandAlive.setChecked(false);
        } else {
            binding.btnGenderFemale.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.primary_green)));
            binding.btnGenderFemale.setTextColor(context.getResources().getColor(R.color.white));
            binding.btnGenderMale.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.bg_card_secondary)));
            binding.btnGenderMale.setTextColor(context.getResources().getColor(R.color.text_secondary));

            binding.layoutWifeSection.setVisibility(View.GONE);
            binding.layoutHusbandSection.setVisibility(View.VISIBLE);
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            binding.tvWifeCount.setText(isBn ? "০" : "0");
        }
    }

    private void setupMadhabSelector() {
        if (binding.btnChangeCalculationMethod != null) {
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnChangeCalculationMethod);
            binding.btnChangeCalculationMethod.setOnClickListener(this::showMadhabDropdown);
        }
        if (binding.layoutCalculationMethodBar != null) {
            binding.layoutCalculationMethodBar.setOnClickListener(this::showMadhabDropdown);
        }

        if (binding.chipMadhabHanafi != null) {
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMadhabHanafi);
            binding.chipMadhabHanafi.setOnClickListener(v -> setMadhab(FaraidInput.Madhab.HANAFI));
        }
        if (binding.chipMadhabShafi != null) {
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMadhabShafi);
            binding.chipMadhabShafi.setOnClickListener(v -> setMadhab(FaraidInput.Madhab.SHAFI));
        }
        if (binding.chipMadhabMaliki != null) {
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMadhabMaliki);
            binding.chipMadhabMaliki.setOnClickListener(v -> setMadhab(FaraidInput.Madhab.MALIKI));
        }
        if (binding.chipMadhabHanbali != null) {
            com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipMadhabHanbali);
            binding.chipMadhabHanbali.setOnClickListener(v -> setMadhab(FaraidInput.Madhab.HANBALI));
        }
    }

    private void showMadhabDropdown(View anchor) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        View target = anchor != null ? anchor : (binding.btnChangeCalculationMethod != null ? binding.btnChangeCalculationMethod : binding.layoutCalculationMethodBar);
        if (target == null) return;
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(context, target);

        String hanafiTitle = (selectedMadhab == FaraidInput.Madhab.HANAFI ? "✓ " : "   ") + (isBn ? "হানাফি মাযহাব" : "Hanafi Madhhab");
        String shafiTitle = (selectedMadhab == FaraidInput.Madhab.SHAFI ? "✓ " : "   ") + (isBn ? "শাফেয়ী মাযহাব" : "Shafi'i Madhhab");
        String malikiTitle = (selectedMadhab == FaraidInput.Madhab.MALIKI ? "✓ " : "   ") + (isBn ? "মালিকি মাযহাব" : "Maliki Madhhab");
        String hanbaliTitle = (selectedMadhab == FaraidInput.Madhab.HANBALI ? "✓ " : "   ") + (isBn ? "হাম্বলী মাযহাব" : "Hanbali Madhhab");

        popup.getMenu().add(0, 1, 0, hanafiTitle);
        popup.getMenu().add(0, 2, 1, shafiTitle);
        popup.getMenu().add(0, 3, 2, malikiTitle);
        popup.getMenu().add(0, 4, 3, hanbaliTitle);

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1:
                    setMadhab(FaraidInput.Madhab.HANAFI);
                    return true;
                case 2:
                    setMadhab(FaraidInput.Madhab.SHAFI);
                    return true;
                case 3:
                    setMadhab(FaraidInput.Madhab.MALIKI);
                    return true;
                case 4:
                    setMadhab(FaraidInput.Madhab.HANBALI);
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void setMadhab(FaraidInput.Madhab madhab) {
        this.selectedMadhab = madhab;
        if (binding.chipMadhabHanafi != null) resetChip(binding.chipMadhabHanafi, madhab == FaraidInput.Madhab.HANAFI);
        if (binding.chipMadhabShafi != null) resetChip(binding.chipMadhabShafi, madhab == FaraidInput.Madhab.SHAFI);
        if (binding.chipMadhabMaliki != null) resetChip(binding.chipMadhabMaliki, madhab == FaraidInput.Madhab.MALIKI);
        if (binding.chipMadhabHanbali != null) resetChip(binding.chipMadhabHanbali, madhab == FaraidInput.Madhab.HANBALI);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String madhabName = isBn ? "হানাফি" : "Hanafi";
        if (madhab == FaraidInput.Madhab.SHAFI) madhabName = isBn ? "শাফেয়ী" : "Shafi'i";
        else if (madhab == FaraidInput.Madhab.MALIKI) madhabName = isBn ? "মালিকি" : "Maliki";
        else if (madhab == FaraidInput.Madhab.HANBALI) madhabName = isBn ? "হাম্বলী" : "Hanbali";

        if (binding.tvCalculationMethodLabel != null) {
            binding.tvCalculationMethodLabel.setText((isBn ? "হিসাব পদ্ধতি: " : "Calculation Method: ") + madhabName);
        }
        if (binding.btnChangeCalculationMethod != null) {
            binding.btnChangeCalculationMethod.setText(madhabName + " ▾");
        }

        if (lastResult != null && binding.layoutResultsContainer != null && binding.layoutResultsContainer.getVisibility() == View.VISIBLE) {
            calculateAndDisplay();
        }
    }

    private void resetChip(TextView chip, boolean selected) {
        if (selected) {
            chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.primary_green)));
            chip.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(R.color.bg_card_secondary)));
            chip.setTextColor(context.getResources().getColor(R.color.text_secondary));
        }
    }

    private void setupPresetChips() {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset5Lakh);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset10Lakh);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset50Lakh);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset1Crore);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset5Crore);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (!isBn) {
            binding.chipPreset5Lakh.setText("+5 Lakh");
            binding.chipPreset10Lakh.setText("+10 Lakh");
            binding.chipPreset50Lakh.setText("+50 Lakh");
            binding.chipPreset1Crore.setText("+1 Crore");
            binding.chipPreset5Crore.setText("+5 Crore");
        }

        binding.chipPreset5Lakh.setOnClickListener(v -> addEstateAmount(500000));
        binding.chipPreset10Lakh.setOnClickListener(v -> addEstateAmount(1000000));
        binding.chipPreset50Lakh.setOnClickListener(v -> addEstateAmount(5000000));
        binding.chipPreset1Crore.setOnClickListener(v -> addEstateAmount(10000000));
        binding.chipPreset5Crore.setOnClickListener(v -> addEstateAmount(50000000));
    }

    private void addEstateAmount(double amount) {
        double current = parseDouble(binding.etTotalEstate.getText().toString());
        double updated = current + amount;
        binding.etTotalEstate.setText(String.format("%.0f", updated));
    }

    private void setupPropertyInputs() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String[] units = isBn ? new String[]{"শতক / ডেসিমাল", "কাঠা", "বিঘা", "একর", "বর্গফুট"}
                              : new String[]{"Decimal / Shatak", "Katha", "Bigha", "Acre", "Sq Ft"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, units);
        binding.spinnerPropertyUnit.setAdapter(adapter);

        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset5Shatak);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset10Shatak);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset20Shatak);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset33Shatak);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipPreset1Acre);

        if (!isBn) {
            binding.chipPreset5Shatak.setText("+5 Decimal");
            binding.chipPreset10Shatak.setText("+10 Decimal");
            binding.chipPreset20Shatak.setText("+20 Decimal");
            binding.chipPreset33Shatak.setText("+33 Decimal");
            binding.chipPreset1Acre.setText("+1 Acre");
        }

        binding.chipPreset5Shatak.setOnClickListener(v -> addPropertyAmount(5));
        binding.chipPreset10Shatak.setOnClickListener(v -> addPropertyAmount(10));
        binding.chipPreset20Shatak.setOnClickListener(v -> addPropertyAmount(20));
        binding.chipPreset33Shatak.setOnClickListener(v -> addPropertyAmount(33));
        binding.chipPreset1Acre.setOnClickListener(v -> addPropertyAmount(100));
    }

    private void addPropertyAmount(double amount) {
        double current = parseDouble(binding.etTotalProperty.getText().toString());
        double updated = current + amount;
        binding.etTotalProperty.setText(String.format("%.0f", updated));
    }

    private void setupSectionToggles() {
        binding.layoutToggleDeductions.setOnClickListener(v -> {
            boolean isVisible = binding.layoutDeductionFields.getVisibility() == View.VISIBLE;
            binding.layoutDeductionFields.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            binding.ivDeductionArrow.setRotation(isVisible ? 0f : 180f);
        });

        binding.layoutToggleExtendedHeirs.setOnClickListener(v -> {
            boolean isVisible = binding.layoutExtendedHeirsPanel.getVisibility() == View.VISIBLE;
            binding.layoutExtendedHeirsPanel.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            binding.ivExtendedArrow.setRotation(isVisible ? 0f : 180f);
        });

        binding.layoutToggleAuditLog.setOnClickListener(v -> {
            boolean isVisible = binding.tvAuditLogContent.getVisibility() == View.VISIBLE;
            binding.tvAuditLogContent.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            binding.ivAuditArrow.setRotation(isVisible ? 0f : 180f);
        });
    }

    private void setupCounterButtons() {
        setupCounter(binding.btnWifeMinus, binding.btnWifePlus, binding.tvWifeCount, 0, 4);
        setupCounter(binding.btnSonMinus, binding.btnSonPlus, binding.tvSonCount, 0, 50);
        setupCounter(binding.btnDaughterMinus, binding.btnDaughterPlus, binding.tvDaughterCount, 0, 50);
        setupCounter(binding.btnGrandsonMinus, binding.btnGrandsonPlus, binding.tvGrandsonCount, 0, 50);
        setupCounter(binding.btnGranddaughterMinus, binding.btnGranddaughterPlus, binding.tvGranddaughterCount, 0, 50);
        setupCounter(binding.btnFullBrotherMinus, binding.btnFullBrotherPlus, binding.tvFullBrotherCount, 0, 50);
        setupCounter(binding.btnFullSisterMinus, binding.btnFullSisterPlus, binding.tvFullSisterCount, 0, 50);
        setupCounter(binding.btnConsanguineBrotherMinus, binding.btnConsanguineBrotherPlus, binding.tvConsanguineBrotherCount, 0, 50);
        setupCounter(binding.btnConsanguineSisterMinus, binding.btnConsanguineSisterPlus, binding.tvConsanguineSisterCount, 0, 50);
        setupCounter(binding.btnUterineBrotherMinus, binding.btnUterineBrotherPlus, binding.tvUterineBrotherCount, 0, 50);
        setupCounter(binding.btnUterineSisterMinus, binding.btnUterineSisterPlus, binding.tvUterineSisterCount, 0, 50);
        setupCounter(binding.btnNephewMinus, binding.btnNephewPlus, binding.tvNephewCount, 0, 50);
        setupCounter(binding.btnUncleMinus, binding.btnUnclePlus, binding.tvUncleCount, 0, 50);
    }

    private void setupCounter(View minus, View plus, TextView tvCount, int min, int max) {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(minus);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(plus);

        minus.setOnClickListener(v -> {
            int count = parseInt(tvCount.getText().toString());
            if (count > min) {
                boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
                tvCount.setText(isBn ? BengaliNumberUtil.toBengali(String.valueOf(count - 1)) : String.valueOf(count - 1));
            }
        });
        plus.setOnClickListener(v -> {
            int count = parseInt(tvCount.getText().toString());
            if (count < max) {
                boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
                tvCount.setText(isBn ? BengaliNumberUtil.toBengali(String.valueOf(count + 1)) : String.valueOf(count + 1));
            }
        });
    }

    private void setupActionButtons() {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCalculateFaraid);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRecalculateFaraid);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnShareFaraidResult);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnViewAiFatwa);

        binding.btnCalculateFaraid.setOnClickListener(v -> calculateAndDisplay());
        binding.btnRecalculateFaraid.setOnClickListener(v -> calculateAndDisplay());
        binding.btnShareFaraidResult.setOnClickListener(v -> shareResults());
        binding.btnViewAiFatwa.setOnClickListener(v -> showAiExplanationDialog());
    }

    private void setupDalilChips() {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefQuran411);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefQuran412);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefQuran4176);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefBukhari6732);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefBukhari6737);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipRefMuslim1615a);

        binding.chipRefQuran411.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_QURAN_4_11));
        binding.chipRefQuran412.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_QURAN_4_12));
        binding.chipRefQuran4176.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_QURAN_4_176));
        binding.chipRefBukhari6732.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_BUKHARI_6732));
        binding.chipRefBukhari6737.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_BUKHARI_6737));
        binding.chipRefMuslim1615a.setOnClickListener(v -> openUrl(FaraidCitationHelper.URL_MUSLIM_1615A));
    }

    private void calculateAndDisplay() {
        FaraidInput input = collectInput();
        FaraidValidationResult validation = FaraidValidationEngine.validate(input);

        if (!validation.isValid()) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            Toast.makeText(context, isBn ? validation.getPrimaryErrorMessageBn() : validation.getPrimaryErrorMessageEn(), Toast.LENGTH_LONG).show();
            return;
        }

        lastResult = FaraidCalculatorEngine.calculate(input);
        displayResults(lastResult);
    }

    private FaraidInput collectInput() {
        FaraidInput input = new FaraidInput();
        input.setTotalEstate(parseDouble(binding.etTotalEstate.getText().toString()));

        // Real Estate / Land Property Input
        input.setTotalProperty(parseDouble(binding.etTotalProperty.getText().toString()));
        String selectedUnit = "শতক";
        if (binding.spinnerPropertyUnit.getSelectedItem() != null) {
            String spItem = binding.spinnerPropertyUnit.getSelectedItem().toString();
            if (spItem.contains("শতক") || spItem.contains("ডেসিমাল")) selectedUnit = "শতক";
            else if (spItem.contains("কাঠা")) selectedUnit = "কাঠা";
            else if (spItem.contains("বিঘা")) selectedUnit = "বিঘা";
            else if (spItem.contains("একর")) selectedUnit = "একর";
            else if (spItem.contains("বর্গফুট")) selectedUnit = "বর্গফুট";
            else selectedUnit = spItem;
        }
        input.setPropertyUnit(selectedUnit);

        input.setFuneralExpense(parseDouble(binding.etFuneralExpense.getText().toString()));
        input.setDebtAmount(parseDouble(binding.etDebtAmount.getText().toString()));
        input.setWasiyyahAmount(parseDouble(binding.etWasiyyahAmount.getText().toString()));
        input.setDeceasedGender(isMaleDeceased ? FaraidInput.Gender.MALE : FaraidInput.Gender.FEMALE);
        input.setMadhab(selectedMadhab);
        input.setApplyBangladeshLaw1961(binding.switchBangladeshLaw1961.isChecked());

        if (isMaleDeceased) {
            input.setWifeCount(parseInt(binding.tvWifeCount.getText().toString()));
            input.setHusbandAlive(false);
        } else {
            input.setWifeCount(0);
            input.setHusbandAlive(binding.switchHusbandAlive.isChecked());
        }

        input.setFatherAlive(binding.switchFatherAlive.isChecked());
        input.setMotherAlive(binding.switchMotherAlive.isChecked());
        input.setSonCount(parseInt(binding.tvSonCount.getText().toString()));
        input.setDaughterCount(parseInt(binding.tvDaughterCount.getText().toString()));

        input.setPaternalGrandfatherAlive(binding.switchGrandfather.isChecked());
        input.setPaternalGrandmotherAlive(binding.switchPaternalGrandmother.isChecked());
        input.setMaternalGrandmotherAlive(binding.switchMaternalGrandmother.isChecked());

        input.setGrandsonCount(parseInt(binding.tvGrandsonCount.getText().toString()));
        input.setGranddaughterCount(parseInt(binding.tvGranddaughterCount.getText().toString()));

        input.setFullBrotherCount(parseInt(binding.tvFullBrotherCount.getText().toString()));
        input.setFullSisterCount(parseInt(binding.tvFullSisterCount.getText().toString()));
        input.setConsanguineBrotherCount(parseInt(binding.tvConsanguineBrotherCount.getText().toString()));
        input.setConsanguineSisterCount(parseInt(binding.tvConsanguineSisterCount.getText().toString()));
        input.setUterineBrotherCount(parseInt(binding.tvUterineBrotherCount.getText().toString()));
        input.setUterineSisterCount(parseInt(binding.tvUterineSisterCount.getText().toString()));

        input.setFullNephewCount(parseInt(binding.tvNephewCount.getText().toString()));
        input.setFullPaternalUncleCount(parseInt(binding.tvUncleCount.getText().toString()));

        return input;
    }

    private void displayResults(FaraidCalculationResult result) {
        binding.layoutResultsContainer.setVisibility(View.VISIBLE);

        FaraidCurrency curr = result.getCurrency() != null ? result.getCurrency() : FaraidCurrency.BDT;

        boolean hasMoney = result.getGrossEstate() > 0;
        boolean hasProperty = result.getNetDistributableProperty() > 0;

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        // Status Badge (Awl / Radd / Umariyyatan / Adl)
        if (result.isAwlApplied()) {
            binding.layoutAwlRaddBadge.setVisibility(View.VISIBLE);
            binding.layoutAwlRaddBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.feat_bg_azkar));
            binding.ivAwlRaddIcon.setImageResource(R.drawable.ic_warning_triangle);
            binding.ivAwlRaddIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.accent_gold));
            binding.tvAwlRaddBadge.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
            binding.tvAwlRaddBadge.setText(isBn ? "আওল: মোট অংশের অনুপাত ১ অতিক্রম করায় সকলের অংশ সমানুপাতিক হারে হ্রাস করা হয়েছে।" : "Awl: Total shares exceeded 1, so all shares have been proportionally reduced.");
        } else if (result.isRaddApplied()) {
            binding.layoutAwlRaddBadge.setVisibility(View.VISIBLE);
            binding.layoutAwlRaddBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.feat_bg_qibla));
            binding.ivAwlRaddIcon.setImageResource(R.drawable.ic_refresh);
            binding.ivAwlRaddIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.accent_cyan));
            binding.tvAwlRaddBadge.setTextColor(ContextCompat.getColor(context, R.color.accent_cyan));
            binding.tvAwlRaddBadge.setText(isBn ? "রাদ্দ: অংশ বণ্টনের পর উদ্বৃত্ত সম্পত্তি জাবিল ফুরুদদের মাঝে ফিরিয়ে দেওয়া হয়েছে।" : "Radd: Surplus estate after distribution has been returned to eligible Quranic heirs.");
        } else {
            binding.layoutAwlRaddBadge.setVisibility(View.VISIBLE);
            binding.layoutAwlRaddBadge.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.bg_card_active));
            binding.ivAwlRaddIcon.setImageResource(R.drawable.ic_check_circle);
            binding.ivAwlRaddIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.primary_green));
            binding.tvAwlRaddBadge.setTextColor(ContextCompat.getColor(context, R.color.primary_green));
            binding.tvAwlRaddBadge.setText(isBn ? "আদল: নির্ধারিত অংশ ১০০% সঠিকভাবে বণ্টিত হয়েছে।" : "Adl: Prescribed shares have been exactly 100% distributed.");
        }

        // Financial vs Pure Share Distribution visibility
        if (hasMoney || hasProperty) {
            binding.layoutFinancialBreakdownSection.setVisibility(View.VISIBLE);
            binding.layoutShareDistributionInfoSection.setVisibility(View.GONE);

            if (hasMoney) {
                binding.layoutResGrossEstate.setVisibility(View.VISIBLE);
                binding.tvResGrossEstate.setText(curr.format(result.getGrossEstate(), isBn));

                if (result.getFuneralExpense() > 0) {
                    binding.layoutResFuneral.setVisibility(View.VISIBLE);
                    binding.tvResFuneral.setText("- " + curr.format(result.getFuneralExpense(), isBn));
                } else {
                    binding.layoutResFuneral.setVisibility(View.GONE);
                }

                if (result.getDebtAmount() > 0) {
                    binding.layoutResDebt.setVisibility(View.VISIBLE);
                    binding.tvResDebt.setText("- " + curr.format(result.getDebtAmount(), isBn));
                } else {
                    binding.layoutResDebt.setVisibility(View.GONE);
                }

                if (result.getValidWasiyyah() > 0) {
                    binding.layoutResWasiyyah.setVisibility(View.VISIBLE);
                    binding.tvResWasiyyah.setText("- " + curr.format(result.getValidWasiyyah(), isBn));
                } else {
                    binding.layoutResWasiyyah.setVisibility(View.GONE);
                }

                binding.layoutResNetEstate.setVisibility(View.VISIBLE);
                binding.tvResNetEstate.setText(curr.format(result.getNetDistributableEstate(), isBn));
            } else {
                binding.layoutResGrossEstate.setVisibility(View.GONE);
                binding.layoutResFuneral.setVisibility(View.GONE);
                binding.layoutResDebt.setVisibility(View.GONE);
                binding.layoutResWasiyyah.setVisibility(View.GONE);
                binding.layoutResNetEstate.setVisibility(View.GONE);
            }

            if (hasProperty) {
                binding.layoutResNetProperty.setVisibility(View.VISIBLE);
                String propVal = String.format("%.2f", result.getNetDistributableProperty());
                String unit = isBn ? result.getPropertyUnit() : (result.getPropertyUnit().equals("শতক") ? "Decimal" : result.getPropertyUnit().equals("কাঠা") ? "Katha" : result.getPropertyUnit().equals("বিঘা") ? "Bigha" : result.getPropertyUnit().equals("একর") ? "Acre" : result.getPropertyUnit().equals("বর্গফুট") ? "Sq Ft" : result.getPropertyUnit());
                binding.tvResNetProperty.setText((isBn ? BengaliNumberUtil.toBengali(propVal) : propVal) + " " + unit);
            } else {
                binding.layoutResNetProperty.setVisibility(View.GONE);
            }
        } else {
            // User did not enter money or property -> Show pure share distribution overview
            binding.layoutFinancialBreakdownSection.setVisibility(View.GONE);
            binding.layoutShareDistributionInfoSection.setVisibility(View.VISIBLE);

            long baseAsl = result.getBaseDenominator();
            long tashih = result.getCorrectedDenominator();
            String aslText;
            if (isBn) {
                aslText = (tashih > baseAsl && tashih > 0)
                        ? (BengaliNumberUtil.toBengali(baseAsl) + " (তাশহীহ: " + BengaliNumberUtil.toBengali(tashih) + ")")
                        : BengaliNumberUtil.toBengali(baseAsl > 0 ? baseAsl : 1);
            } else {
                aslText = (tashih > baseAsl && tashih > 0)
                        ? (baseAsl + " (Tashih: " + tashih + ")")
                        : String.valueOf(baseAsl > 0 ? baseAsl : 1);
            }
            binding.tvResAslMasalah.setText(aslText);

            int heirCount = (result.getHeirShares() != null) ? result.getHeirShares().size() : 0;
            binding.tvResEligibleHeirsCount.setText(isBn ? (BengaliNumberUtil.toBengali(heirCount) + " জন ক্যাটাগরি") : (heirCount + " Categories"));
        }

        if (result.isWasiyyahExceedingOneThird()) {
            binding.tvWasiyyahNotice.setVisibility(View.VISIBLE);
            binding.tvWasiyyahNotice.setText(isBn
                    ? "অসিয়তের পরিমাণ ১/৩ অংশের চেয়ে বেশি হওয়ায় তা ১/৩ অংশে সীমাবদ্ধ রাখা হয়েছে।"
                    : "Bequest exceeded 1/3 and has been capped at the maximum 1/3 limit.");
        } else {
            binding.tvWasiyyahNotice.setVisibility(View.GONE);
        }

        if (result.hasMadhabDifference()) {
            binding.cardMadhabDifference.setVisibility(View.VISIBLE);
            binding.tvMadhabDifferenceNotice.setText(result.getMadhabDifferenceNote(isBn));
        } else {
            binding.cardMadhabDifference.setVisibility(View.GONE);
        }

        // 3. Populate Islamic Finance Summary Table (Requirement 24)
        populateSummaryTable(result);

        // 4. Update Detailed Heir Cards
        resultsAdapter.updateData(result.getHeirShares());

        if (result.hasBlockedHeirs()) {
            binding.layoutBlockedHeirsSection.setVisibility(View.VISIBLE);
            blockedAdapter.updateData(result.getBlockedHeirs());
        } else {
            binding.layoutBlockedHeirsSection.setVisibility(View.GONE);
        }

        binding.tvAuditLogContent.setText(result.getStepByStepAuditLog(isBn));
        binding.tvOverallDalilSummary.setText(result.getOverallQuranicDalilSummary(isBn));

        binding.layoutResultsContainer.post(() -> {
            binding.layoutResultsContainer.getParent().requestChildFocus(binding.layoutResultsContainer, binding.layoutResultsContainer);
        });
    }

    private void populateSummaryTable(FaraidCalculationResult result) {
        if (binding.tableEligibleHeirsSummary == null) return;
        int childCount = binding.tableEligibleHeirsSummary.getChildCount();
        if (childCount > 1) {
            binding.tableEligibleHeirsSummary.removeViews(1, childCount - 1);
        }

        boolean hasMoney = result.getGrossEstate() > 0;
        boolean hasProperty = result.getNetDistributableProperty() > 0;
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        float density = context.getResources().getDisplayMetrics().density;
        int padV = (int) (6 * density);
        int padH = (int) (4 * density);

        for (HeirShareResult share : result.getHeirShares()) {
            TableRow row = new TableRow(context);
            row.setPadding(padH, padV, padH, padV);

            // 1. Heir title & count
            TextView tvHeir = new TextView(context);
            TableRow.LayoutParams lpHeir = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.3f);
            tvHeir.setLayoutParams(lpHeir);
            String countStr = share.getCount() > 1
                    ? (isBn ? " (" + BengaliNumberUtil.toBengali(String.valueOf(share.getCount())) + " জন)" : " (" + share.getCount() + " persons)")
                    : "";
            tvHeir.setText(share.getLocalizedRelationTitle(context) + countStr);
            tvHeir.setTextColor(context.getResources().getColor(R.color.text_primary));
            tvHeir.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvHeir.setTypeface(null, Typeface.BOLD);
            row.addView(tvHeir);

            // 2. Share fraction
            TextView tvFrac = new TextView(context);
            TableRow.LayoutParams lpFrac = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f);
            tvFrac.setLayoutParams(lpFrac);
            tvFrac.setGravity(Gravity.CENTER);
            tvFrac.setText(share.getShareFractionLabel(isBn));
            tvFrac.setTextColor(context.getResources().getColor(R.color.accent_gold));
            tvFrac.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvFrac.setTypeface(null, Typeface.BOLD);
            row.addView(tvFrac);

            // 3. Percentage
            TextView tvPct = new TextView(context);
            TableRow.LayoutParams lpPct = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f);
            tvPct.setLayoutParams(lpPct);
            tvPct.setGravity(Gravity.CENTER);
            String pctStr = String.format("%.2f", share.getPercentage());
            tvPct.setText((isBn ? BengaliNumberUtil.toBengali(pctStr) : pctStr) + "%");
            tvPct.setTextColor(context.getResources().getColor(R.color.primary_green));
            tvPct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tvPct.setTypeface(null, Typeface.BOLD);
            row.addView(tvPct);

            // 4. Amount / Land / Share
            TextView tvAmt = new TextView(context);
            TableRow.LayoutParams lpAmt = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.2f);
            tvAmt.setLayoutParams(lpAmt);
            tvAmt.setGravity(Gravity.END);
            if (hasMoney && !hasProperty) {
                tvAmt.setText(share.getTotalCategoryAmountFormatted(isBn));
            } else if (!hasMoney && hasProperty) {
                tvAmt.setText(share.getTotalPropertyFormatted(isBn));
            } else if (hasMoney && hasProperty) {
                tvAmt.setText(share.getTotalCategoryAmountFormatted(isBn) + "\n" + share.getTotalPropertyFormatted(isBn));
            } else {
                tvAmt.setText(share.getShareFractionLabel(isBn));
            }
            tvAmt.setTextColor(context.getResources().getColor(R.color.text_primary));
            tvAmt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f);
            row.addView(tvAmt);

            binding.tableEligibleHeirsSummary.addView(row);
        }
    }

    private void showDalilPopup(HeirShareResult heir) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String reason = heir.getLegalReason(isBn);
        String formula = heir.getCalculationFormula(isBn);
        String dalilTrans = heir.getDalilTranslation(isBn);
        String dalilPronun = heir.getDalilPronunciation(isBn);
        String dalilRef = heir.getDalilReference(isBn);

        StringBuilder sb = new StringBuilder();
        if (heir.getArabicDalil() != null && !heir.getArabicDalil().isEmpty()) {
            sb.append(heir.getArabicDalil()).append("\n\n");
        }
        if (dalilPronun != null && !dalilPronun.isEmpty()) {
            sb.append(isBn ? "উচ্চারণ:\n" : "Pronunciation:\n").append(dalilPronun).append("\n\n");
        }
        if (dalilTrans != null && !dalilTrans.isEmpty()) {
            sb.append(isBn ? "অনুবাদ:\n" : "Translation:\n").append(dalilTrans).append("\n\n");
        }
        if (dalilRef != null && !dalilRef.isEmpty()) {
            sb.append(isBn ? "সূত্র: " : "Reference: ").append(dalilRef).append("\n\n");
        }
        if (reason != null && !reason.isEmpty()) {
            sb.append(isBn ? "আইনি ভিত্তি:\n" : "Legal Basis:\n").append(reason).append("\n\n");
        }
        if (formula != null && !formula.isEmpty()) {
            sb.append(isBn ? "গণনার সূত্র:\n" : "Calculation Formula:\n").append(formula);
        }

        new AlertDialog.Builder(context)
                .setTitle(heir.getLocalizedRelationTitle(context) + (isBn ? "-এর অংশের শরয়ী দলিল" : " Quranic & Hadith Proof"))
                .setMessage(sb.toString().trim())
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }

    private void showAiExplanationDialog() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (lastResult == null) {
            Toast.makeText(context, isBn ? "প্রথমে হিসাব সম্পন্ন করুন" : "Please calculate first", Toast.LENGTH_SHORT).show();
            return;
        }
        String explanation = FaraidAiExplanationEngine.explainCalculation(lastResult, isBn);
        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ফারায়েজ বিশ্লেষণ ও শরয়ী ব্যাখ্যা" : "Faraid Analysis & Shariah Explanation")
                .setMessage(explanation)
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }

    private void showInfoDialog() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String infoMsg = isBn ? ("ফারায়েজ হলো পবিত্র কুরআনে আল্লাহ তাআলা কর্তৃক নির্ধারিত উত্তরাধিকার সম্পত্তি বণ্টন ব্যবস্থা।\n\n" +
                "প্রধান মূলনীতি:\n" +
                "১. মৃত ব্যক্তির সম্পত্তি থেকে প্রথমে কাফন-দাফন খরচ ও ঋণ পরিশোধ করতে হবে।\n" +
                "২. বৈধ অসিয়ত (সর্বোচ্চ ১/৩ অংশ পর্যন্ত) কার্যকর হবে।\n" +
                "৩. অবশিষ্ট সম্পদ নির্দিষ্ট ওয়ারিশদের মাঝে কুরআনিক নিয়ম অনুযায়ী বণ্টিত হবে।\n\n" +
                FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN)
                : ("Faraid is the Islamic system of inheritance distribution ordained by Allah Almighty in the Holy Quran.\n\n" +
                "Core Principles:\n" +
                "1. Funeral expenses and legitimate debts must be cleared first from the estate.\n" +
                "2. Valid bequests (Wasiyyah up to 1/3 maximum) are executed next.\n" +
                "3. The remaining net estate is distributed strictly among Quranic heirs.\n\n" +
                FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);
        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ইসলামিক ফারায়েজ পরিচিতি" : "Islamic Faraid Introduction")
                .setMessage(infoMsg)
                .setPositiveButton(isBn ? "বুঝেছি" : "Understood", null)
                .show();
    }

    private void showAiGuideDialog() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ফারায়েজ নির্দেশিকা ও গাইড" : "Faraid Guide & Wizard")
                .setMessage(isBn ? "আপনি কি ১০-ধাপের সহজ নির্দেশিকা (Wizard) দিয়ে হিসাব করতে চান নাকি সাধারণ নির্দেশিকা দেখতে চান?"
                        : "Would you like to use the 10-step Wizard or view general guidelines?")
                .setPositiveButton(isBn ? "সহজ উইজার্ড শুরু করুন" : "Start Wizard", (d, w) -> {
                    FaraidWizardDialog.show(context, result -> {
                        this.lastResult = result;
                        displayResults(result);
                    });
                })
                .setNeutralButton(isBn ? "সাধারণ নিয়মাবলী" : "General Guidelines", (d, w) -> {
                    new AlertDialog.Builder(context)
                            .setTitle(isBn ? "ফারায়েজ সাধারণ নির্দেশিকা" : "Faraid General Guidelines")
                            .setMessage(isBn ? ("১. মৃত ব্যক্তির পরিচয় ও লিঙ্গ নির্বাচন করুন।\n" +
                                    "২. মোট ত্যাজ্য সম্পত্তির পরিমাণ লিখুন।\n" +
                                    "৩. কাফন-দাফন খরচ, ঋণ বা অসিয়ত থাকলে তা যুক্ত করুন।\n" +
                                    "৪. জীবিত ওয়ারিশদের সংখ্যা নির্ধারণ করুন।\n" +
                                    "৫. 'হিসাব করুন' বাটনে চাপ দিন।\n\n" +
                                    "প্রতিটি ওয়ারিশের নামের উপর ক্লিক করে সরাসরি সংশ্লিষ্ট কুরআনের আয়াত ও হাদিসের দলিল দেখতে পারবেন।")
                                    : ("1. Select the gender of the deceased.\n" +
                                    "2. Enter total estate value or land property.\n" +
                                    "3. Deduct funeral expenses, debts, or bequest if any.\n" +
                                    "4. Set the number of surviving heirs.\n" +
                                    "5. Click 'Calculate' to view results.\n\n" +
                                    "Click on each heir's card to view their authentic Quranic and Hadith proofs."))
                            .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                            .show();
                })
                .setNegativeButton(isBn ? "বন্ধ করুন" : "Close", null)
                .show();
    }

    private void shareResults() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (lastResult == null) {
            Toast.makeText(context, isBn ? "প্রথমে হিসাব সম্পন্ন করুন" : "Please calculate first", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        FaraidCurrency curr = lastResult.getCurrency() != null ? lastResult.getCurrency() : FaraidCurrency.BDT;

        boolean hasMoney = lastResult.getGrossEstate() > 0;
        boolean hasProperty = lastResult.getNetDistributableProperty() > 0;

        if (isBn) {
            sb.append("DeenOne - ইসলামিক ফারায়েজ উত্তরাধিকার বণ্টন বিবরণী\n\n");
            if (hasMoney) {
                sb.append("মোট ত্যাজ্য অর্থ: ").append(curr.format(lastResult.getGrossEstate(), true)).append("\n");
                if (lastResult.getFuneralExpense() > 0) sb.append("কাফন-দাফন খরচ: ").append(curr.format(lastResult.getFuneralExpense(), true)).append("\n");
                if (lastResult.getDebtAmount() > 0) sb.append("পরিশোধিত ঋণ: ").append(curr.format(lastResult.getDebtAmount(), true)).append("\n");
                if (lastResult.getValidWasiyyah() > 0) sb.append("কার্যকর অসিয়ত: ").append(curr.format(lastResult.getValidWasiyyah(), true)).append("\n");
                sb.append("বণ্টনযোগ্য অবশিষ্ট অর্থ: ").append(curr.format(lastResult.getNetDistributableEstate(), true)).append("\n");
            }
            if (hasProperty) {
                sb.append("মোট স্থাবর সম্পত্তি / জমি: ")
                  .append(BengaliNumberUtil.toBengali(String.format("%.2f", lastResult.getNetDistributableProperty())))
                  .append(" ").append(lastResult.getPropertyUnit()).append("\n");
                sb.append("বণ্টনযোগ্য অবশিষ্ট জমি: ")
                  .append(BengaliNumberUtil.toBengali(String.format("%.2f", lastResult.getNetDistributableProperty())))
                  .append(" ").append(lastResult.getPropertyUnit()).append("\n");
            }
            sb.append("\n");

            sb.append("ওয়ারিশদের অংশের বিবরণ:\n");
            for (HeirShareResult h : lastResult.getHeirShares()) {
                sb.append(" • ").append(h.getRelationTitleBn())
                        .append(" (").append(h.getShareFractionLabel(true)).append(" - ")
                        .append(BengaliNumberUtil.toBengali(String.format("%.2f", h.getPercentage()))).append("%):\n");
                if (hasMoney && h.getTotalCategoryAmount() > 0) {
                    sb.append("   - অর্থ: ").append(curr.format(h.getTotalCategoryAmount(), true));
                    if (h.getCount() > 1) {
                        sb.append(" (জনপ্রতি ").append(curr.format(h.getIndividualAmount(), true)).append(")");
                    }
                    sb.append("\n");
                }
                if (hasProperty && h.getTotalPropertyAmount() > 0) {
                    sb.append("   - জমি/সম্পত্তি: ").append(h.getTotalPropertyFormatted(true));
                    if (h.getCount() > 1) {
                        sb.append(" (জনপ্রতি ").append(h.getIndividualPropertyFormatted(true)).append(")");
                    }
                    sb.append("\n");
                }
            }

            if (lastResult.hasBlockedHeirs()) {
                sb.append("\nবঞ্চিত ওয়ারিশদের তালিকা:\n");
                for (BlockedHeirInfo b : lastResult.getBlockedHeirs()) {
                    sb.append(" • ").append(b.getHeirTitleBn()).append(" (কারণ: ").append(b.getLocalizedLegalReason(true)).append(")\n");
                }
            }

            sb.append("\n").append(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN);
        } else {
            sb.append("DeenOne - Islamic Inheritance Distribution Statement\n\n");
            String propUnitEn = lastResult.getPropertyUnit().equals("শতক") ? "Decimal" : lastResult.getPropertyUnit().equals("কাঠা") ? "Katha" : lastResult.getPropertyUnit().equals("বিঘা") ? "Bigha" : lastResult.getPropertyUnit().equals("একর") ? "Acre" : lastResult.getPropertyUnit().equals("বর্গফুট") ? "Sq Ft" : lastResult.getPropertyUnit();
            if (hasMoney) {
                sb.append("Total Estate: ").append(curr.format(lastResult.getGrossEstate(), false)).append("\n");
                if (lastResult.getFuneralExpense() > 0) sb.append("Funeral Expenses: ").append(curr.format(lastResult.getFuneralExpense(), false)).append("\n");
                if (lastResult.getDebtAmount() > 0) sb.append("Settled Debts: ").append(curr.format(lastResult.getDebtAmount(), false)).append("\n");
                if (lastResult.getValidWasiyyah() > 0) sb.append("Valid Bequest: ").append(curr.format(lastResult.getValidWasiyyah(), false)).append("\n");
                sb.append("Net Distributable Estate: ").append(curr.format(lastResult.getNetDistributableEstate(), false)).append("\n");
            }
            if (hasProperty) {
                sb.append("Total Real Estate / Land: ")
                  .append(String.format("%.2f", lastResult.getNetDistributableProperty()))
                  .append(" ").append(propUnitEn).append("\n");
                sb.append("Net Distributable Land: ")
                  .append(String.format("%.2f", lastResult.getNetDistributableProperty()))
                  .append(" ").append(propUnitEn).append("\n");
            }
            sb.append("\n");

            sb.append("Heirs Distribution Details:\n");
            for (HeirShareResult h : lastResult.getHeirShares()) {
                sb.append(" • ").append(h.getLocalizedRelationTitle(context))
                        .append(" (").append(h.getShareFractionLabel(false)).append(" - ")
                        .append(String.format("%.2f", h.getPercentage())).append("%):\n");
                if (hasMoney && h.getTotalCategoryAmount() > 0) {
                    sb.append("   - Amount: ").append(curr.format(h.getTotalCategoryAmount(), false));
                    if (h.getCount() > 1) {
                        sb.append(" (Per Person ").append(curr.format(h.getIndividualAmount(), false)).append(")");
                    }
                    sb.append("\n");
                }
                if (hasProperty && h.getTotalPropertyAmount() > 0) {
                    sb.append("   - Land/Property: ").append(h.getTotalPropertyFormatted(false));
                    if (h.getCount() > 1) {
                        sb.append(" (Per Person ").append(h.getIndividualPropertyFormatted(false)).append(")");
                    }
                    sb.append("\n");
                }
            }

            if (lastResult.hasBlockedHeirs()) {
                sb.append("\nExcluded Heirs:\n");
                for (BlockedHeirInfo b : lastResult.getBlockedHeirs()) {
                    sb.append(" • ").append(b.getLocalizedHeirTitle(context)).append(" (Reason: ").append(b.getLocalizedLegalReason(false)).append(")\n");
                }
            }

            sb.append("\n").append(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);
        }

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Faraid Calculation", sb.toString());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, isBn ? "ফারায়েজ ফলাফল কপি করা হয়েছে" : "Faraid calculation copied to clipboard", Toast.LENGTH_SHORT).show();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "ফারায়েজ বণ্টন ফলাফল" : "Islamic Faraid Result");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        context.startActivity(Intent.createChooser(shareIntent, isBn ? "ফারায়েজ ফলাফল শেয়ার করুন" : "Share Faraid Result"));
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            Toast.makeText(context, isBn ? "লিংকটি খোলা সম্ভব হয়নি" : "Could not open link", Toast.LENGTH_SHORT).show();
        }
    }

    private double parseDouble(String str) {
        if (str == null || str.trim().isEmpty()) return 0.0;
        try {
            String eng = BengaliNumberUtil.toEnglish(str.trim().replaceAll("[,৳\\s]", ""));
            return Double.parseDouble(eng);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int parseInt(String str) {
        if (str == null || str.trim().isEmpty()) return 0;
        try {
            String eng = BengaliNumberUtil.toEnglish(str.trim());
            return Integer.parseInt(eng);
        } catch (Exception e) {
            return 0;
        }
    }
}