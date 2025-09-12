package com.xcode.loanservice.usecase.solicitud;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.application.gateways.ApplicationRepository;
import com.xcode.loanservice.model.common.exception.vo.ApplicationPendingResponse;
import com.xcode.loanservice.model.common.exception.vo.ApplicationSeachCriteria;
import com.xcode.loanservice.model.common.exception.vo.PageRequest;
import com.xcode.loanservice.model.common.exception.vo.PaginatedResponse;
import com.xcode.loanservice.model.user.User;
import com.xcode.loanservice.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ListApplicationPendingUseCase {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public Mono<PaginatedResponse<ApplicationPendingResponse>> execute(ApplicationSeachCriteria criteria, PageRequest pageRequest) {
        return applicationRepository
                .findPending(criteria, pageRequest)
                .flatMap(this::enrichWithUserData);
    }

    private Mono<PaginatedResponse<ApplicationPendingResponse>> enrichWithUserData(PaginatedResponse<Application> solicitudesPage) {

        if (solicitudesPage.getContent().isEmpty()) {
            return Mono.just(new PaginatedResponse<>(List.of(), 0, solicitudesPage.getPageRequest()));
        }

        Set<String> emails = solicitudesPage.getContent().stream()
                .map(Application::getEmail)
                .collect(Collectors.toSet());

        return Mono.zip(obtenerSalarios(emails), calcularDeudasAprobadas(emails))
                .map(tuple -> mapToPaginatedResponse(solicitudesPage, tuple.getT1(), tuple.getT2()));
    }

    private PaginatedResponse<ApplicationPendingResponse> mapToPaginatedResponse(
            PaginatedResponse<Application> solicitudesPage,
            Map<String, Double> salarios,
            Map<String, BigDecimal> deudas) {

        List<ApplicationPendingResponse> responses = solicitudesPage
                .getContent()
                .stream()
                .map(solicitud -> mapToResponse(solicitud, salarios, deudas))
                .toList();

        return new PaginatedResponse<>(responses, solicitudesPage.getTotalElements(), solicitudesPage.getPageRequest());
    }

    private Mono<Map<String, Double>> obtenerSalarios(Set<String> emails) {
        return userRepository.findSalaryByEmail(emails)
                .collectMap(User::getEmail, User::getSalaryBase)
                .onErrorReturn(Map.of());
    }

    private Mono<Map<String, BigDecimal>> calcularDeudasAprobadas(Set<String> emails) {
        return Flux.fromIterable(emails)
                .flatMap(email -> applicationRepository.findApproveByEmail(email)
                        .map(Application::calculateMonthlyPayment)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .map(deuda -> Map.entry(email, deuda))
                )
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }


    private ApplicationPendingResponse mapToResponse(
            Application solicitud,
            Map<String, Double> salarios,
            Map<String, BigDecimal> deudas) {

        return ApplicationPendingResponse.builder()
                .id(solicitud.getIdApplication())
                .amount(solicitud.getAmount())
                .term(solicitud.getTerm())
                .email(solicitud.getEmail())
                .loanType(solicitud.getLoanType().getName())
                .rateInterest(solicitud.getLoanType().getInterestRate())
                .loanStatus(solicitud.getLoanStatus().getName().name())
                .salaryBase(salarios.getOrDefault(solicitud.getEmail(), BigDecimal.ZERO.doubleValue()))
                .totalMonthlyDebtApprovedRequests(deudas.getOrDefault(solicitud.getEmail(), BigDecimal.ZERO))
                .createdAt(solicitud.getCreatedAt())
                .build();
    }

}
