package com.xcode.loanservice.api.mapper;


import com.xcode.loanservice.api.dto.ApplicationRequest;
import com.xcode.loanservice.api.dto.ApplicationResponse;
import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.loantype.LoanType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationMapper {
    default Application toDomain(ApplicationRequest request) {
        return Application.builder()
                .amount(request.getAmount())
                .term(request.getTerm())
                .email(request.getEmail().trim().toLowerCase())
                .document(request.getDocument().trim())
                .loanType(LoanType.builder().idLoanType(request.getLoanType()).build())
                .build();

    }
    default ApplicationResponse toResponse(Application application) {
        if (application == null) {
            return null;
        }

        return ApplicationResponse.builder()
                .idApplication(application.getIdApplication().toString())
                .amount(application.getAmount())
                .term(application.getTerm())
                .email(application.getEmail())
                .document(application.getDocument())
                .loanStatus(application.getLoanStatus().getDescription())
                .loanType(application.getLoanType().getName())
                .build();
    }
}
