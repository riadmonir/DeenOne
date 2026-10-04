package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;

public class FaraidValidationError implements Serializable {
    private final String fieldName;
    private final String errorCode;
    private final String messageEn;
    private final String messageBn;

    public FaraidValidationError(String fieldName, String errorCode, String messageEn, String messageBn) {
        this.fieldName = fieldName;
        this.errorCode = errorCode;
        this.messageEn = messageEn;
        this.messageBn = messageBn;
    }

    public String getFieldName() { return fieldName; }
    public String getErrorCode() { return errorCode; }
    public String getMessageEn() { return messageEn; }
    public String getMessageBn() { return messageBn; }

    @Override
    public String toString() {
        return messageEn + " (" + messageBn + ")";
    }
}