package com.xcode.loanservice.model.application;

import com.xcode.loanservice.model.common.exception.InvalidAmountException;
import com.xcode.loanservice.model.common.exception.InvalidTermException;
import com.xcode.loanservice.model.common.exception.MissingRequiredFieldException;
import com.xcode.loanservice.model.common.exception.ValidateAmountRangeException;
import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.model.loantype.LoanType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {
    private UUID idApplication;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String document;
    private LoanStatus loanStatus;
    private LoanType loanType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static Application createNew(BigDecimal amount, Integer term, String email, String document, LoanStatus loanStatus, LoanType loanType) {
        validateForCreation(amount, term, email, document, loanType);
        validateAmount(amount, loanType);

        LocalDateTime now = LocalDateTime.now();
        return Application.builder()
                .amount(amount)
                .term(term)
                .email(email.trim().toLowerCase())
                .document(document.trim())
                .loanStatus(loanStatus)
                .loanType(loanType)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static Application fromRepository(UUID id, BigDecimal amount, Integer term,
                                             String email, String document, LoanStatus status,
                                             LoanType loanType, LocalDateTime createdAt,
                                             LocalDateTime updatedAt) {
        return Application.builder()
                .idApplication(id)
                .amount(amount)
                .term(term)
                .email(email.trim().toLowerCase())
                .document(document.trim())
                .loanStatus(status)
                .loanType(loanType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    private static void validateForCreation(BigDecimal amount, Integer term, String email, String document, LoanType loanType) {
        if (amount == null) {
            throw new MissingRequiredFieldException("amount");
        }
        if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (term == null) {
            throw new MissingRequiredFieldException("term");
        }
        if (term <= 0) {
            throw new InvalidTermException(term);
        }
        if (document == null || document.isBlank()) {
            throw new MissingRequiredFieldException("document");
        }
        if (loanType == null) {
            throw new MissingRequiredFieldException("loanType");
        }

    }

    private static void validateAmount(BigDecimal amount, LoanType loanType) {
        if (!loanType.validateAmount(amount)) {
            throw new ValidateAmountRangeException(amount);
        }
    }

    public Application approve(LoanStatus approvedStatus) {
        validateCanApprove();
        return this.toBuilder()
                .loanStatus(approvedStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Application reject(LoanStatus rejectedStatus) {
        validateCanReject();
        return this.toBuilder()
                .loanStatus(rejectedStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void validateCanApprove() {
        if (!loanStatus.isPendingReview()) {
            throw new IllegalArgumentException(
                    "Solo las solicitudes en estado PENDING_REVIEW pueden ser aprobadas. Estado actual: " +
                            loanStatus.getName()
            );
        }
    }

    private void validateCanReject() {
        if (!loanStatus.isPendingReview()) {
            throw new IllegalArgumentException(
                    "Solo las solicitudes en estado PENDING_REVIEW pueden ser rechazadas. Estado actual: " +
                            loanStatus.getName()
            );
        }
    }

    public BigDecimal calculateMonthlyPayment() {

        if (loanType.getInterestRate().equals(BigDecimal.ZERO)) {
            return amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        }
        BigDecimal monthlyRate = loanType.getInterestRate().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(monthlyRate).pow(term);
        BigDecimal numerator = monthlyRate.multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);

        return amount.multiply(numerator.divide(denominator, 2, RoundingMode.HALF_UP));

    }

}
