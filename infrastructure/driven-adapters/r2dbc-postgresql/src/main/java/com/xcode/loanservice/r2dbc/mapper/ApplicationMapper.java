package com.xcode.loanservice.r2dbc.mapper;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.application.LoanStatusName;
import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.model.loantype.LoanType;
import com.xcode.loanservice.r2dbc.dto.ApplicationWithLoanTypeDTO;
import com.xcode.loanservice.r2dbc.entity.ApplicationEntity;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ApplicationMapper {
    public ApplicationEntity toEntity(Application domain) {
        if (domain == null) return null;

        return ApplicationEntity.builder()
                .idApplication(domain.getIdApplication())
                .amount(domain.getAmount())
                .term(domain.getTerm())
                .email(domain.getEmail())
                .document(domain.getDocument())
                .status(domain.getLoanStatus().getIdLoanStatus())
                .loanType(domain.getLoanType().getIdLoanType())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }


    public Application toDomain(ApplicationEntity entity) {
        if (entity == null) return null;
        LoanType loanType = LoanType.builder()
                .idLoanType(entity.getLoanType())
                .build();

        LoanStatus loanStatus = LoanStatus.builder()
                .idLoanStatus(entity.getStatus())
                .build();
        return Application.fromRepository(
                entity.getIdApplication(),
                entity.getAmount(),
                entity.getTerm(),
                entity.getEmail(),
                entity.getDocument(),
                loanStatus,
                loanType,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


    public Application toDomain(ApplicationWithLoanTypeDTO dto) {
        if (dto == null) return null;

        LoanType loanType = LoanType.builder()
                .name(dto.nameLoanType())
                .build();

        LoanStatus loanStatus = LoanStatus.builder()
                .name(mapStringToStatus(dto.nameStatus()))
                .build();
        return Application.fromRepository(
                dto.idApplication(),
                dto.amount(),
                dto.term(),
                dto.email(),
                dto.document(),
                loanStatus,
                loanType,
                dto.createdAt(),
                dto.updatedAt()
        );
    }

    public Application map(Row row, RowMetadata meta) {
        return Application.builder()
                .idApplication(row.get("id", UUID.class))
                .amount(row.get("monto", BigDecimal.class))
                .term(row.get("plazo", Integer.class))
                .email(row.get("email", String.class))
                .loanType(LoanType.builder()
                        .name(row.get("tipo_prestamo", String.class))
                        .interestRate(row.get("tasa_interes", BigDecimal.class))
                        .build())
                .loanStatus(LoanStatus.builder()
                        .name(LoanStatusName.valueOf(row.get("estado", String.class)))
                        .build())
                .createdAt(row.get("fecha_creacion", LocalDateTime.class))
                .build();
    }

    private Integer mapStatusToInteger(LoanStatusName status) {
        if (status == null) return null;
        return switch (status) {
            case PENDING_REVIEW -> 1;
            case APPROVED -> 2;
            case REJECTED -> 3;
        };
    }

    private LoanStatusName mapIntegerToStatus(Integer status) {
        if (status == null) return null;
        return switch (status) {
            case 1 -> LoanStatusName.PENDING_REVIEW;
            case 2 -> LoanStatusName.APPROVED;
            case 3 -> LoanStatusName.REJECTED;
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        };
    }
    private LoanStatusName mapStringToStatus(String statusName) {
        if (statusName == null) return null;
        return switch (statusName.toUpperCase()) {
            case "PENDING_REVIEW", "PENDIENTE_REVISION" -> LoanStatusName.PENDING_REVIEW;
            case "APPROVED", "APROBADO" -> LoanStatusName.APPROVED;
            case "REJECTED", "RECHAZADO" -> LoanStatusName.REJECTED;
            default -> throw new IllegalArgumentException("Invalid status: " + statusName);
        };
    }

}
