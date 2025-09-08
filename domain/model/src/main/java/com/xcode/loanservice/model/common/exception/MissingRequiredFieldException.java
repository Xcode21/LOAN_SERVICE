package com.xcode.loanservice.model.common.exception;

public class MissingRequiredFieldException extends DomainException {
    public MissingRequiredFieldException(String field) {
        super(DomainErrorCode.MISSING_REQUIRED_FIELD, field);
    }
}
