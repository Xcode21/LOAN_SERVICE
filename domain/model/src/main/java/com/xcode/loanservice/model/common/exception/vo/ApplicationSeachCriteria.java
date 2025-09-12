package com.xcode.loanservice.model.common.exception.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ApplicationSeachCriteria {

    private final String email;
    private final String loanType;
    private final BigDecimal maxAmount;
    private final BigDecimal minAmount;
}
