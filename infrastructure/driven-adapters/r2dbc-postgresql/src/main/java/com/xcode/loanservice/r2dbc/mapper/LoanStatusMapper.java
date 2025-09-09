package com.xcode.loanservice.r2dbc.mapper;


import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.r2dbc.entity.LoanStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanStatusMapper {

    public LoanStatus toDomain(LoanStatusEntity entity) {
        if (entity == null) {
            return null;
        }

        return LoanStatus.builder()
                .idLoanStatus(entity.getIdLoanStatus())
                .name(entity.getName())
                .description(entity.getDescription()).build();

    }

    public LoanStatusEntity toEntity(LoanStatus domain) {
        if (domain == null) {
            return null;
        }

        return LoanStatusEntity.builder()
                .idLoanStatus(domain.getIdLoanStatus())
                .name(domain.getName())
                .description(domain.getDescription())
                .build();
    }
}
