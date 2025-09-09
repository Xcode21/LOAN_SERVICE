package com.xcode.loanservice.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "solicitud")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity {
    @Id
    @Column("id")
    private UUID idApplication;
    @Column("monto")
    private BigDecimal amount;
    @Column("plazo")
    private Integer term;
    @Column("email")
    private String email;
    @Column("documento")
    private String document;
    @Column("id_estado")
    private Integer status;
    @Column("id_prestamo")
    private Integer loanType;
    @Column("fecha_creacion")
    private LocalDateTime createdAt;
    @Column("fecha_actualizacion")
    private LocalDateTime updatedAt;
}
