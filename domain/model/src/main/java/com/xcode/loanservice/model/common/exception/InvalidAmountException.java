package com.xcode.loanservice.model.common.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends DomainException {

    public InvalidAmountException(BigDecimal amount) {
        super(DomainErrorCode.INVALID_AMOUNT, String.valueOf(amount));
    }
}
