package com.xcode.loanservice.r2dbc.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationWithLoanTypeDTO(
        UUID idApplication,
        BigDecimal amount,
        Integer term,
        String email,
        String document,
        String nameLoanType,
        BigDecimal interestRate,
        String nameStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}