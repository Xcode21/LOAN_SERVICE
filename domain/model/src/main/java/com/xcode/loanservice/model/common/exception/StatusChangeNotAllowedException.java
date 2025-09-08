package com.xcode.loanservice.model.common.exception;

public class StatusChangeNotAllowedException extends DomainException{
    public StatusChangeNotAllowedException(String status) {
        super(DomainErrorCode.INVALID_STATUS_CHANGE, "Status not allowed change: " + status);
    }

}
