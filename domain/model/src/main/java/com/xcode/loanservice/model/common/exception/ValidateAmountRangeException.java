package com.xcode.loanservice.model.common.exception;

import java.math.BigDecimal;

public class ValidateAmountRangeException extends DomainException{

    public ValidateAmountRangeException(BigDecimal amount) {
        super(DomainErrorCode.INVALID_AMOUNT_RANGE, String.valueOf(amount));
    }
}
