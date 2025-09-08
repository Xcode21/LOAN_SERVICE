package com.xcode.loanservice.model.loantype;

import com.xcode.loanservice.model.common.exception.InvalidAmountException;
import com.xcode.loanservice.model.common.exception.MissingRequiredFieldException;
import com.xcode.loanservice.model.common.exception.ValidateAmountRangeException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanType {
    private Integer idLoanType;
    private String name;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal interestRate;
    private Boolean automaticValidation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static LoanType createNew(String name, BigDecimal minAmount,
                                     BigDecimal maxAmount, BigDecimal interestRate,
                                     Boolean automaticValidation) {
        validateForCreation(name, minAmount, maxAmount, interestRate);

        return LoanType.builder()
                .name(name)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .interestRate(interestRate)
                .automaticValidation(automaticValidation)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public static LoanType fromRepository(Integer id, String name, BigDecimal minAmount,
                                          BigDecimal maxAmount, BigDecimal interestRate,
                                          Boolean automaticValidation, LocalDateTime createdAt,
                                          LocalDateTime updatedAt) {
        return LoanType.builder()
                .idLoanType(id)
                .name(name)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .interestRate(interestRate)
                .automaticValidation(automaticValidation)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    private static void validateForCreation(String name, BigDecimal minAmount,
                                            BigDecimal maxAmount, BigDecimal interestRate) {
        if (minAmount == null || maxAmount == null) {
            throw new MissingRequiredFieldException("minAmount and maxAmount");
        }
        if (minAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(minAmount);
        }
        if (minAmount.compareTo(maxAmount) > 0) {
            throw new InvalidAmountException(maxAmount);
        }
    }


    public void validateAmount(BigDecimal amount) {
        if (amount.compareTo(minAmount) < 0 || amount.compareTo(maxAmount) > 0) {
            throw new ValidateAmountRangeException(amount);
        }
    }
}
