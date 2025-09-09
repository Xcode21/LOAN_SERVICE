package com.xcode.loanservice.r2dbc.entity;

import com.xcode.loanservice.model.application.LoanStatusName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "estado")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusEntity {
    @Id
    @Column("id")
    private Integer idLoanStatus;
    @Column("nombre")
    private LoanStatusName name;
    @Column("descripcion")
    private String description;
    @Column("fecha_creacion")
    private LocalDateTime createdAt;
    @Column("fecha_actualizacion")
    private LocalDateTime updatedAt;
}
