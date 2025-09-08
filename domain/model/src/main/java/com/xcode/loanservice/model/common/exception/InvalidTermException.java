package com.xcode.loanservice.model.common.exception;

public class InvalidTermException extends DomainException{

    public InvalidTermException(Integer term) {
        super(DomainErrorCode.INVALID_TERM, String.valueOf(term));
    }
}
