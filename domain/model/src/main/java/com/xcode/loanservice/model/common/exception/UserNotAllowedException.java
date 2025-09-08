package com.xcode.loanservice.model.common.exception;

public class UserNotAllowedException extends DomainException{

    public UserNotAllowedException(String fecha) {
        super(DomainErrorCode.USER_NOT_ALLOWED, fecha);
    }
}
