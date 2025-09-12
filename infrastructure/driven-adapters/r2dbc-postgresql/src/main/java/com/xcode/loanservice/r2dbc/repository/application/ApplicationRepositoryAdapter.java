package com.xcode.loanservice.r2dbc.repository.application;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.common.exception.vo.ApplicationSeachCriteria;
import com.xcode.loanservice.model.common.exception.vo.PageRequest;
import com.xcode.loanservice.model.common.exception.vo.PaginatedResponse;
import com.xcode.loanservice.r2dbc.entity.ApplicationEntity;
import com.xcode.loanservice.r2dbc.helper.QueryBinder;
import com.xcode.loanservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.loanservice.r2dbc.mapper.ApplicationMapper;
import com.xcode.loanservice.r2dbc.mapper.SqlLoader;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class ApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        Application,
        ApplicationEntity,
        UUID,
        ApplicationRepository
        > implements com.xcode.loanservice.model.application.gateways.ApplicationRepository {
    private final String FIND_PENDIENTES_QUERY;
    private final String COUNT_PENDIENTES_QUERY;
    private final String FIND_APROBADAS_BY_EMAIL_QUERY;
    private final ApplicationMapper mapper;
    private final DatabaseClient databaseClient;


    public ApplicationRepositoryAdapter(ApplicationRepository repository, ObjectMapper mapper, ApplicationMapper mapper1, DatabaseClient databaseClient, SqlLoader sqlLoader) {
        super(repository, mapper, d -> mapper.map(d, Application.class));
        this.mapper = mapper1;
        this.databaseClient = databaseClient;
        this.FIND_PENDIENTES_QUERY = sqlLoader.load("sql/find_pendientes.sql");
        this.COUNT_PENDIENTES_QUERY = sqlLoader.load("sql/count_pendientes.sql");
        this.FIND_APROBADAS_BY_EMAIL_QUERY = sqlLoader.load("sql/find_aprobadas_by_email.sql");

    }

    @Override
    public Flux<Application> findAllByDocument(String document) {
        return repository.findAllByDocument(document)
                .doOnSubscribe(sub -> log.info("Finding applications by document: {}", document))
                .map(mapper::toDomain)
                .doOnNext(application -> log.info("Found application - Document: {}, ID: {}", document, application.getIdApplication()))
                .doOnError(error -> log.error("Error finding applications by document: {}, Error: {}", document, error.getClass().getSimpleName(), error));
    }


    @Override
    public Mono<Application> save(Application application) {
        return repository.save(mapper.toEntity(application))
                .doOnSubscribe(sub -> log.info("Saving application - Document: {}", application.getDocument()))
                .map(mapper::toDomain)
                .doOnNext(savedApplication -> log.info("application saved successfully - Document: {}", savedApplication.getDocument()))
                .doOnError(error -> log.error("Error saving application - Document: {}, Error: {}", application.getDocument(), error.getClass().getSimpleName(), error));

    }

    @Override
    public Mono<Application> findById(UUID id) {
        return repository.findByIdProjection(id)
                .doOnSubscribe(sub -> log.info("Finding application - Id: {}", id))
                .map(mapper::toDomain)
                .doOnNext(application -> log.info("application finding with  - document: {}", application.getDocument()))
                .doOnError(error -> log.error("Error finding application - ID: {}, Error: {}", id, error.getClass().getSimpleName(), error));

    }


    @Override
    public Mono<PaginatedResponse<Application>> findPending(ApplicationSeachCriteria criteria, PageRequest pageRequest) {
        return Mono.zip(
                executeDataQuery(criteria, pageRequest),
                executeCountQuery(criteria))
                .map(tuple -> new PaginatedResponse<>(tuple.getT1(), tuple.getT2(), pageRequest));
    }

    @Override
    public Flux<Application> findApproveByEmail(String email) {
        return databaseClient.sql(FIND_APROBADAS_BY_EMAIL_QUERY)
                .bind("email", email)
                .map(mapper::map)
                .all();
    }

    private Mono<List<Application>> executeDataQuery(
            ApplicationSeachCriteria criteria,
            PageRequest pageRequest) {

        var dbClient = getCriterias(FIND_PENDIENTES_QUERY, criteria);
        return dbClient
                .bind("sortBy", pageRequest.getSortBy())
                .bind("sortDirection", pageRequest.getSortDirection())
                .bind("limit", pageRequest.getSize())
                .bind("offset", pageRequest.getOffset())
                .map(mapper::map)
                .all()
                .collectList();
    }

    private Mono<Long> executeCountQuery(ApplicationSeachCriteria criteria) {
        var dbClient = getCriterias(COUNT_PENDIENTES_QUERY, criteria);

        return dbClient
                .map(row -> row.get(0, Long.class))
                .one();
    }
    private DatabaseClient.GenericExecuteSpec getCriterias(String query, ApplicationSeachCriteria criteria) {
        var dbClient = databaseClient.sql(query);
        dbClient = QueryBinder.bindEmail(dbClient, criteria.getEmail());
        dbClient = QueryBinder.bindLoanType(dbClient, criteria.getLoanType());
        return dbClient;
    }


    /*private Application mapToSolicitud(Row row, RowMetadata rowMetadata) {
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
    }*/

    /*private DatabaseClient.GenericExecuteSpec getCriterias(String findPendientesQuery, ApplicationSeachCriteria criteria) {
        var dbClient = databaseClient.sql(findPendientesQuery);

        if (criteria.getEmail() != null) {
            dbClient = dbClient.bind("email", "%" + criteria.getEmail() + "%");
        } else {
            dbClient = dbClient.bindNull("email", String.class);
        }

        if (criteria.getLoanType() != null) {
            dbClient = dbClient.bind("tipoPrestamo", criteria.getLoanType());
        } else {
            dbClient = dbClient.bindNull("tipoPrestamo", String.class);
        }
        return dbClient;
    }*/


}
