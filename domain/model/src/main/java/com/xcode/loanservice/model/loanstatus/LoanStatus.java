package com.xcode.loanservice.model.loanstatus;

import com.xcode.loanservice.model.application.LoanStatusName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class LoanStatus {

    private final Integer idLoanStatus;
    private final LoanStatusName name;
    private final String description;

    public boolean isPendingReview() {
        return LoanStatusName.PENDING_REVIEW.equals(name);
    }

    public boolean isApproved() {
        return LoanStatusName.APPROVED.equals(name);
    }

    public boolean isRejected() {
        return LoanStatusName.REJECTED.equals(name);
    }

}
