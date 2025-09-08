package com.xcode.loanservice.model.common.exception;

public class LoanTypeNotFoundException extends DomainException{

    public LoanTypeNotFoundException(Integer loanTypeId) {
        super(DomainErrorCode.LOANTYPE_NOT_FOUND, String.valueOf(loanTypeId));
    }
}
