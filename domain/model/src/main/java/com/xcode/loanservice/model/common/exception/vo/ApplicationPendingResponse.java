package com.xcode.loanservice.model.common.exception.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class ApplicationPendingResponse {
    private final UUID id;
    private final BigDecimal amount;
    private final Integer term;
    private final String email;
    private final String loanType;
    private final BigDecimal rateInterest;
    private final String loanStatus;
    private final Double salaryBase;
    private final BigDecimal totalMonthlyDebtApprovedRequests;
    private final LocalDateTime createdAt;


}
