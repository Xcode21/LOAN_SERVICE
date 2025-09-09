package com.xcode.loanservice.r2dbc.mapper;


import com.xcode.loanservice.model.loantype.LoanType;
import com.xcode.loanservice.r2dbc.entity.LoanTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeMapper {

    public  LoanType toDomain(LoanTypeEntity entity) {
        if (entity == null) {
            return null;
        }

       return LoanType.fromRepository(
                entity.getIdLoanType(),entity.getName(),entity.getMinAmount(),entity.getMaxAmount(),
                entity.getInterestRate(),entity.getAutomaticValidation(),entity.getCreatedAt(),entity.getUpdatedAt()
        );

    }

    public  LoanTypeEntity toEntity(LoanType domain) {
        if (domain == null) {
            return null;
        }

        return LoanTypeEntity.builder()
                .idLoanType(domain.getIdLoanType())
                .name(domain.getName())
                .minAmount(domain.getMinAmount())
                .maxAmount(domain.getMaxAmount())
                .interestRate(domain.getInterestRate())
                .automaticValidation(domain.getAutomaticValidation())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
