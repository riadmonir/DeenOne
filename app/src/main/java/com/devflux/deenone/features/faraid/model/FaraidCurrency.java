package com.devflux.deenone.features.faraid.model;

import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Supported World & Regional Currencies with localized formatting (Requirement 23).
 */
public enum FaraidCurrency implements Serializable {
    BDT("BDT", "টাকা (৳)", "৳", "Bangladeshi Taka", 2),
    SAR("SAR", "রিয়াল (﷼)", "﷼", "Saudi Riyal", 2),
    USD("USD", "ডলার ($)", "$", "US Dollar", 2),
    AED("AED", "দিরহাম (د.إ)", "AED", "UAE Dirham", 2),
    EUR("EUR", "ইউরো (€)", "€", "Euro", 2),
    GBP("GBP", "পাউন্ড (£)", "£", "British Pound", 2),
    MYR("MYR", "রিংগিট (RM)", "RM", "Malaysian Ringgit", 2),
    KWD("KWD", "দিনার (د.ك)", "KWD", "Kuwaiti Dinar", 3),
    QAR("QAR", "রিয়াল (ر.ق)", "QAR", "Qatari Riyal", 2),
    INR("INR", "রুপি (₹)", "₹", "Indian Rupee", 2),
    PKR("PKR", "রুপি (Rs)", "PKR", "Pakistani Rupee", 2);

    private final String code;
    private final String nameBn;
    private final String symbol;
    private final String nameEn;
    private final int decimalPlaces;

    FaraidCurrency(String code, String nameBn, String symbol, String nameEn, int decimalPlaces) {
        this.code = code;
        this.nameBn = nameBn;
        this.symbol = symbol;
        this.nameEn = nameEn;
        this.decimalPlaces = decimalPlaces;
    }

    public String getCode() { return code; }
    public String getNameBn() { return nameBn; }
    public String getSymbol() { return symbol; }
    public String getNameEn() { return nameEn; }
    public int getDecimalPlaces() { return decimalPlaces; }

    public String format(double amount, boolean inBengali) {
        String pattern = (decimalPlaces == 3) ? "#,##0.000" : "#,##0.00";
        DecimalFormat df = new DecimalFormat(pattern);
        String formatted = df.format(amount);
        if (inBengali) {
            return symbol + " " + BengaliNumberUtil.toBengali(formatted);
        } else {
            return symbol + " " + formatted;
        }
    }
}