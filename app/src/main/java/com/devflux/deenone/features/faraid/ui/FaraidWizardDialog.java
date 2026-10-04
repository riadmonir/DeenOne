package com.devflux.deenone.features.faraid.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.features.faraid.engine.FaraidCalculatorEngine;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidInput;

/**
 * 10-Step Guided Faraid Wizard Dialog for Ordinary Muslim Users (Requirement 33).
 *
 * Step 1: Estate (সম্পত্তির পরিমাণ)
 * Step 2: Deceased Gender (মৃত ব্যক্তির পরিচয়)
 * Step 3: Spouse (স্বামী / স্ত্রী)
 * Step 4: Children (ছেলে ও মেয়ে)
 * Step 5: Parents (বাবা ও মা)
 * Step 6: Grandparents (দাদা ও দাদী/নানী)
 * Step 7: Siblings (ভাই ও বোন)
 * Step 8: Other Heirs (নাতি-নাতনি, ভাতিজা, চাচা)
 * Step 9: Debts & Wasiyyah (কাফন, ঋণ ও অসিয়ত)
 * Step 10: Calculate & Results (বণ্টন হিসাব)
 */
public class FaraidWizardDialog {

    public interface WizardCompletionCallback {
        void onCalculationComplete(FaraidCalculationResult result);
    }

    public static void show(@NonNull Context context, WizardCompletionCallback callback) {
        FaraidInput input = new FaraidInput();
        showStep1(context, input, callback);
    }

    private static void showStep1(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        EditText et = new EditText(context);
        et.setHint(isBn ? "টাকার পরিমাণ লিখুন" : "Enter gross estate amount");
        et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ১/৯: মোট ত্যাজ্য সম্পত্তি" : "Step 1/9: Gross Estate")
                .setMessage(isBn ? "মৃত ব্যক্তি সর্বমোট কত টাকার স্থাবর-অস্থাবর সম্পত্তি রেখে গেছেন?" : "What is the total valuation of the gross estate left by the deceased?")
                .setView(et)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    String s = et.getText().toString().trim();
                    if (s.isEmpty() || Double.parseDouble(s) <= 0) {
                        return;
                    }
                    input.setTotalEstate(Double.parseDouble(s));
                    showStep2(context, input, callback);
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void showStep2(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String[] options = isBn 
                ? new String[]{"মৃত ব্যক্তি একজন পুরুষ (স্বামী/পিতা)", "মৃত ব্যক্তি একজন নারী (স্ত্রী/মাতা)"}
                : new String[]{"Deceased is Male (Husband / Father)", "Deceased is Female (Wife / Mother)"};
        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ২/৯: মৃত ব্যক্তির পরিচয়" : "Step 2/9: Deceased Gender")
                .setSingleChoiceItems(options, 0, (d, which) -> {
                    input.setDeceasedGender(which == 0 ? FaraidInput.Gender.MALE : FaraidInput.Gender.FEMALE);
                })
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    if (input.getDeceasedGender() == null) input.setDeceasedGender(FaraidInput.Gender.MALE);
                    showStep3(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep1(context, input, callback))
                .show();
    }

    private static void showStep3(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (input.getDeceasedGender() == FaraidInput.Gender.MALE) {
            String[] options = isBn
                    ? new String[]{"স্ত্রী নেই (০ জন)", "১ জন স্ত্রী", "২ জন স্ত্রী", "৩ জন স্ত্রী", "৪ জন স্ত্রী"}
                    : new String[]{"No Wife (0)", "1 Wife", "2 Wives", "3 Wives", "4 Wives"};
            new AlertDialog.Builder(context)
                    .setTitle(isBn ? "ধাপ ৩/৯: স্ত্রী" : "Step 3/9: Wives")
                    .setMessage(isBn ? "মৃত স্বামীর কতজন স্ত্রী জীবিত আছেন?" : "How many wives are alive?")
                    .setSingleChoiceItems(options, 1, (d, which) -> input.setWifeCount(which))
                    .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> showStep4(context, input, callback))
                    .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep2(context, input, callback))
                    .show();
        } else {
            String[] options = isBn
                    ? new String[]{"স্বামী জীবিত আছেন", "স্বামী জীবিত নেই"}
                    : new String[]{"Husband is Alive", "Husband is Deceased"};
            new AlertDialog.Builder(context)
                    .setTitle(isBn ? "ধাপ ৩/৯: স্বামী" : "Step 3/9: Husband")
                    .setMessage(isBn ? "মৃত নারীর স্বামী কি জীবিত আছেন?" : "Is the deceased's husband alive?")
                    .setSingleChoiceItems(options, 0, (d, which) -> input.setHusbandAlive(which == 0))
                    .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> showStep4(context, input, callback))
                    .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep2(context, input, callback))
                    .show();
        }
    }

    private static void showStep4(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        TextView tvS = new TextView(context);
        tvS.setText(isBn ? "জীবিত ছেলের সংখ্যা:" : "Living Sons:");
        EditText etS = new EditText(context);
        etS.setHint("1");
        etS.setText("1");
        etS.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        TextView tvD = new TextView(context);
        tvD.setText(isBn ? "জীবিত মেয়ের সংখ্যা:" : "Living Daughters:");
        EditText etD = new EditText(context);
        etD.setHint("1");
        etD.setText("1");
        etD.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        layout.addView(tvS);
        layout.addView(etS);
        layout.addView(tvD);
        layout.addView(etD);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৪/৯: সন্তান সন্ততি" : "Step 4/9: Children")
                .setView(layout)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    int s = parseOrZero(etS.getText().toString());
                    int dt = parseOrZero(etD.getText().toString());
                    input.setSonCount(s);
                    input.setDaughterCount(dt);
                    showStep5(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep3(context, input, callback))
                .show();
    }

    private static void showStep5(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        boolean[] checked = {true, true};
        String[] items = isBn
                ? new String[]{"বাবা জীবিত আছেন", "মা জীবিত আছেন"}
                : new String[]{"Father is Alive", "Mother is Alive"};

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৫/৯: পিতা-মাতা" : "Step 5/9: Parents")
                .setMultiChoiceItems(items, checked, (d, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    input.setFatherAlive(checked[0]);
                    input.setMotherAlive(checked[1]);
                    showStep6(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep4(context, input, callback))
                .show();
    }

    private static void showStep6(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        boolean[] checked = {false, false, false};
        String[] items = isBn
                ? new String[]{"দাদা", "দাদী", "নানী"}
                : new String[]{"Paternal Grandfather", "Paternal Grandmother", "Maternal Grandmother"};

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৬/৯: দাদা-দাদী ও নানী" : "Step 6/9: Grandparents")
                .setMultiChoiceItems(items, checked, (d, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    input.setPaternalGrandfatherAlive(checked[0]);
                    input.setPaternalGrandmotherAlive(checked[1]);
                    input.setMaternalGrandmotherAlive(checked[2]);
                    showStep7(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep5(context, input, callback))
                .show();
    }

    private static void showStep7(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        TextView tvB = new TextView(context);
        tvB.setText(isBn ? "সহোদর ভাইয়ের সংখ্যা:" : "Full Brothers:");
        EditText etB = new EditText(context);
        etB.setHint("0");
        etB.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        TextView tvS = new TextView(context);
        tvS.setText(isBn ? "সহোদর বোনের সংখ্যা:" : "Full Sisters:");
        EditText etS = new EditText(context);
        etS.setHint("0");
        etS.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        layout.addView(tvB);
        layout.addView(etB);
        layout.addView(tvS);
        layout.addView(etS);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৭/৯: ভাই-বোন" : "Step 7/9: Siblings")
                .setView(layout)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    input.setFullBrotherCount(parseOrZero(etB.getText().toString()));
                    input.setFullSisterCount(parseOrZero(etS.getText().toString()));
                    showStep8(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep6(context, input, callback))
                .show();
    }

    private static void showStep8(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        TextView tvGS = new TextView(context);
        tvGS.setText(isBn ? "নাতির সংখ্যা:" : "Grandsons (Son's Son):");
        EditText etGS = new EditText(context);
        etGS.setHint("0");
        etGS.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        TextView tvGD = new TextView(context);
        tvGD.setText(isBn ? "নাতনির সংখ্যা:" : "Granddaughters (Son's Daughter):");
        EditText etGD = new EditText(context);
        etGD.setHint("0");
        etGD.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        layout.addView(tvGS);
        layout.addView(etGS);
        layout.addView(tvGD);
        layout.addView(etGD);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৮/৯: অন্যান্য আত্মীয়" : "Step 8/9: Other Relatives")
                .setView(layout)
                .setPositiveButton(isBn ? "পরবর্তী ধাপ" : "Next", (d, w) -> {
                    input.setGrandsonCount(parseOrZero(etGS.getText().toString()));
                    input.setGranddaughterCount(parseOrZero(etGD.getText().toString()));
                    showStep9(context, input, callback);
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep7(context, input, callback))
                .show();
    }

    private static void showStep9(Context context, FaraidInput input, WizardCompletionCallback callback) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        TextView tvF = new TextView(context);
        tvF.setText(isBn ? "কাফন-দাফন খরচ:" : "Funeral Expenses:");
        EditText etF = new EditText(context);
        etF.setHint("0");
        etF.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        TextView tvD = new TextView(context);
        tvD.setText(isBn ? "পাওনাদারদের ঋণ ও মোহরানা:" : "Debts & Mahr:");
        EditText etD = new EditText(context);
        etD.setHint("0");
        etD.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        TextView tvW = new TextView(context);
        tvW.setText(isBn ? "অসিয়তের পরিমাণ (সর্বোচ্চ ১/৩):" : "Wasiyyah (Bequest, max 1/3):");
        EditText etW = new EditText(context);
        etW.setHint("0");
        etW.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        layout.addView(tvF);
        layout.addView(etF);
        layout.addView(tvD);
        layout.addView(etD);
        layout.addView(tvW);
        layout.addView(etW);

        new AlertDialog.Builder(context)
                .setTitle(isBn ? "ধাপ ৯/৯: দেনা-পাওনা ও অসিয়ত" : "Step 9/9: Debts & Wasiyyah")
                .setView(layout)
                .setPositiveButton(isBn ? "হিসাব সম্পন্ন করুন" : "Calculate", (d, w) -> {
                    input.setFuneralExpense(parseOrZeroDouble(etF.getText().toString()));
                    input.setDebtAmount(parseOrZeroDouble(etD.getText().toString()));
                    input.setWasiyyahAmount(parseOrZeroDouble(etW.getText().toString()));

                    FaraidCalculationResult result = FaraidCalculatorEngine.calculate(input);
                    if (callback != null) {
                        callback.onCalculationComplete(result);
                    }
                })
                .setNegativeButton(isBn ? "পূর্ববর্তী" : "Back", (d, w) -> showStep8(context, input, callback))
                .show();
    }

    private static int parseOrZero(String s) {
        if (s == null) return 0;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static double parseOrZeroDouble(String s) {
        if (s == null) return 0.0;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}