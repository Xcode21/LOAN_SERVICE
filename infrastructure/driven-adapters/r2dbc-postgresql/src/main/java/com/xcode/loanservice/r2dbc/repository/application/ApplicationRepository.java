package com.xcode.loanservice.r2dbc.repository.application;

import com.xcode.loanservice.r2dbc.dto.ApplicationWithLoanTypeDTO;
import com.xcode.loanservice.r2dbc.entity.ApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationRepository extends ReactiveCrudRepository<ApplicationEntity, UUID>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

    Mono<ApplicationEntity> save(ApplicationEntity application);

    @Query("""
        SELECT s.id, s.monto,s,plazo, s.email, s.documento,
               tp.nombre, tp.tasa_interes AS tasa_interes,
               e.nombre AS estado_nombre,
               s.fecha_creacion, s.fecha_actualizacion
        FROM solicitud s
        INNER JOIN estado e ON s.id_estado = e.id
        INNER JOIN tipo_prestamo tp ON s.id_prestamo = tp.id
        WHERE s.id = :id
        """)
    Mono<ApplicationWithLoanTypeDTO> findByIdProjection(UUID id);

    Mono<ApplicationEntity> findById(UUID id);
    Flux<ApplicationEntity> findAllByDocument(String document);


   /* Mono<Long> countPending(Map<String, Object> filtros);
    Mono<PaginatedResponse<ApplicationEntity>> findPending(ApplicationSeachCriteria criteria, PageRequest pageRequest);
    Flux<ApplicationEntity> findApproveByEmail(String email);*/
}
