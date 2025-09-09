package com.xcode.loanservice.model.common.exception;

public class LoanStatusNotFoundException extends DomainException{

    public LoanStatusNotFoundException(String  name) {
        super(DomainErrorCode.STATUS_NOT_FOUND, name);
    }
}
