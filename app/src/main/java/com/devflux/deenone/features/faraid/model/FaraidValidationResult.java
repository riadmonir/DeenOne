package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FaraidValidationResult implements Serializable {

    public static class ValidationError implements Serializable {
        private final String errorCode;
        private final String messageBn;
        private final String messageEn;

        public ValidationError(String errorCode, String messageBn, String messageEn) {
            this.errorCode = errorCode;
            this.messageBn = messageBn;
            this.messageEn = messageEn;
        }

        public String getErrorCode() { return errorCode; }
        public String getMessageBn() { return messageBn; }
        public String getMessageEn() { return messageEn; }
    }

    private final boolean valid;
    private final List<ValidationError> errorList;
    private final List<String> errorsBn;
    private final List<String> warningsBn;
    private final List<String> errorsEn;
    private final List<String> warningsEn;

    public FaraidValidationResult(boolean valid, List<ValidationError> errorList,
                                  List<String> warningsBn, List<String> warningsEn) {
        this.valid = valid;
        this.errorList = errorList != null ? errorList : Collections.emptyList();
        this.errorsBn = new ArrayList<>();
        this.errorsEn = new ArrayList<>();
        for (ValidationError ve : this.errorList) {
            this.errorsBn.add(ve.getMessageBn());
            this.errorsEn.add(ve.getMessageEn());
        }
        this.warningsBn = warningsBn != null ? warningsBn : Collections.emptyList();
        this.warningsEn = warningsEn != null ? warningsEn : Collections.emptyList();
    }

    public FaraidValidationResult(boolean valid, List<String> errorsBn, List<String> warningsBn,
                                  List<String> errorsEn, List<String> warningsEn) {
        this.valid = valid;
        this.errorsBn = errorsBn != null ? errorsBn : Collections.emptyList();
        this.warningsBn = warningsBn != null ? warningsBn : Collections.emptyList();
        this.errorsEn = errorsEn != null ? errorsEn : Collections.emptyList();
        this.warningsEn = warningsEn != null ? warningsEn : Collections.emptyList();
        this.errorList = new ArrayList<>();
        for (int i = 0; i < this.errorsBn.size(); i++) {
            String bn = this.errorsBn.get(i);
            String en = (i < this.errorsEn.size()) ? this.errorsEn.get(i) : bn;
            this.errorList.add(new ValidationError("VALIDATION_ERROR", bn, en));
        }
    }

    public boolean isValid() { return valid; }
    public List<ValidationError> getErrors() { return errorList; }
    public List<String> getErrorsBn() { return errorsBn; }
    public List<String> getWarningsBn() { return warningsBn; }
    public List<String> getErrorsEn() { return errorsEn; }
    public List<String> getWarningsEn() { return warningsEn; }

    public String getPrimaryErrorMessageBn() {
        if (!errorsBn.isEmpty()) return errorsBn.get(0);
        return "";
    }

    public String getPrimaryErrorMessageEn() {
        if (!errorsEn.isEmpty()) return errorsEn.get(0);
        return "";
    }

    public String getSummaryBn() {
        if (!errorsBn.isEmpty()) return String.join(", ", errorsBn);
        if (!warningsBn.isEmpty()) return String.join(", ", warningsBn);
        return "বৈধ তথ্য";
    }

    public String getSummaryEn() {
        if (!errorsEn.isEmpty()) return String.join(", ", errorsEn);
        if (!warningsEn.isEmpty()) return String.join(", ", warningsEn);
        return "Valid input";
    }
}