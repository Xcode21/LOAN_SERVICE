package com.xcode.loanservice.model.application;

import com.xcode.loanservice.model.common.exception.InvalidAmountException;
import com.xcode.loanservice.model.common.exception.MissingRequiredFieldException;
import com.xcode.loanservice.model.common.exception.StatusChangeNotAllowedException;
import com.xcode.loanservice.model.loantype.LoanType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {
    private UUID idApplication;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String document;
    private LoanStatus status;
    private LoanType loanType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Application createNew(BigDecimal amount, Integer term, String email, String document, LoanType loanType) {
        validateForCreation(amount, term, email, document, loanType);

        return Application.builder()
                .amount(amount)
                .term(term)
                .email(email)
                .document(document)
                .loanType(loanType)
                .status(LoanStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
                .email(email)
                .document(document)
                .status(status)
                .loanType(loanType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    private static void validateForCreation(BigDecimal amount, Integer term, String email, String document, LoanType loanType) {
        if (amount == null) {
            throw new MissingRequiredFieldException("amount");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (term == null) {
            throw new MissingRequiredFieldException("term");
        }
        if (term <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (document == null || document.isBlank()) {
            throw new MissingRequiredFieldException("document");
        }
        if (loanType == null) {
            throw new MissingRequiredFieldException("loanType");
        }

        loanType.validateAmount(amount);
    }

    public Application approve() {
        if (this.status != LoanStatus.PENDING_REVIEW) {
            throw new StatusChangeNotAllowedException(status.name());
        }

        return this.toBuilder()
                .status(LoanStatus.APPROVED)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Application reject() {
        if (this.status != LoanStatus.PENDING_REVIEW) {
            throw new StatusChangeNotAllowedException(status.name());

        }

        return this.toBuilder()
                .status(LoanStatus.REJECTED)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
