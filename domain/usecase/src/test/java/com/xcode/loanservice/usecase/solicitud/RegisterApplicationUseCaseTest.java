package com.xcode.loanservice.usecase.solicitud;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.application.LoanStatusName;
import com.xcode.loanservice.model.application.gateways.ApplicationRepository;
import com.xcode.loanservice.model.application.gateways.TransactionalExecutor;
import com.xcode.loanservice.model.common.exception.LoanStatusNotFoundException;
import com.xcode.loanservice.model.common.exception.LoanTypeNotFoundException;
import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.model.loanstatus.gateways.LoanStatusRepository;
import com.xcode.loanservice.model.loantype.LoanType;
import com.xcode.loanservice.model.loantype.gateways.LoanTypeRepository;
import com.xcode.loanservice.usecase.user.UserVaLidateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterApplicationUseCaseTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private LoanStatusRepository loanStatusRepository;

    @Mock
    private UserVaLidateUseCase userVaLidateUseCase;

    @Mock
    private TransactionalExecutor transactionalExecutor;

    private RegisterApplicationUseCase useCase;

    private LoanType validLoanType;
    private LoanStatus pendingStatus;
    private Application inputApplication;
    private Application savedApplication;

    @BeforeEach
    void setUp() {
        useCase = new RegisterApplicationUseCase(
                applicationRepository,
                loanTypeRepository,
                loanStatusRepository,
                userVaLidateUseCase,
                transactionalExecutor
        );

        validLoanType = LoanType.builder()
                .idLoanType(1)
                .name("Personal Loan")
                .minAmount(new BigDecimal("1000"))
                .maxAmount(new BigDecimal("50000"))
                .interestRate(new BigDecimal("0.15"))
                .automaticValidation(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        pendingStatus = LoanStatus.builder()
                .idLoanStatus(1)
                .name(LoanStatusName.PENDING_REVIEW)
                .description("Pending Review")
                .build();

        inputApplication = Application.builder()
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@example.com")
                .document("12345678")
                .loanType(LoanType.builder().idLoanType(1).build())
                .build();

        savedApplication = Application.builder()
                .idApplication(UUID.randomUUID())
                .amount(new BigDecimal("10000"))
                .term(12)
                .email("test@example.com")
                .document("12345678")
                .loanType(validLoanType)
                .loanStatus(pendingStatus)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Successful Registration")
    class SuccessfulRegistrationTests {

        @Test
        @DisplayName("Should register application successfully with valid data")
        void shouldRegisterApplicationSuccessfullyWithValidData() {
            // Given
            when(userVaLidateUseCase.validateApplication(eq("12345678")))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(1)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.just(savedApplication));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When & Then
            StepVerifier.create(useCase.execute(inputApplication))
                    .assertNext(result -> {
                        assertNotNull(result);
                        assertEquals(savedApplication.getIdApplication(), result.getIdApplication());
                        assertEquals(validLoanType, result.getLoanType());
                        assertEquals(pendingStatus, result.getLoanStatus());
                        assertEquals(new BigDecimal("10000"), result.getAmount());
                        assertEquals(12, result.getTerm());
                        assertEquals("test@example.com", result.getEmail());
                        assertEquals("12345678", result.getDocument());
                    })
                    .verifyComplete();

            verify(userVaLidateUseCase).validateApplication("12345678");
            verify(loanTypeRepository).findById(1);
            verify(loanStatusRepository).findByName("PENDING_REVIEW");
            verify(applicationRepository).save(any(Application.class));
            verify(transactionalExecutor).executeInTransaction(any(Mono.class));
        }

        @Test
        @DisplayName("Should create new application with proper data transformation")
        void shouldCreateNewApplicationWithProperDataTransformation() {
            // Given
            when(userVaLidateUseCase.validateApplication(any()))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(any()))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(any()))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenAnswer(invocation -> {
                        Application app = invocation.getArgument(0);
                        return Mono.just(app.toBuilder().idApplication(UUID.randomUUID()).build());
                    });
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When & Then
            StepVerifier.create(useCase.execute(inputApplication))
                    .assertNext(result -> {
                        assertNotNull(result);
                        assertNotNull(result.getIdApplication());
                        assertNotNull(result.getCreatedAt());
                        assertNotNull(result.getUpdatedAt());
                        assertEquals(validLoanType, result.getLoanType());
                        assertEquals(pendingStatus, result.getLoanStatus());
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Repository Integration")
    class RepositoryIntegrationTests {

        @Test
        @DisplayName("Should interact with all repositories in correct sequence")
        void shouldInteractWithAllRepositoriesInCorrectSequence() {
            // Given
            when(userVaLidateUseCase.validateApplication(eq("12345678")))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(1)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.just(savedApplication));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            StepVerifier.create(useCase.execute(inputApplication))
                    .assertNext(result -> {
                        assertNotNull(result);
                        assertEquals(validLoanType, result.getLoanType());
                        assertEquals(pendingStatus, result.getLoanStatus());
                    })
                    .verifyComplete();

            // Then - verify all repositories were called in order
            verify(userVaLidateUseCase).validateApplication("12345678");
            verify(loanTypeRepository).findById(1);
            verify(loanStatusRepository).findByName("PENDING_REVIEW");
            verify(applicationRepository).save(any(Application.class));
            verify(transactionalExecutor).executeInTransaction(any(Mono.class));
        }
    }

    @Nested
    @DisplayName("Persistence Failures")
    class PersistenceFailuresTests {

        @Test
        @DisplayName("Should fail when application repository save fails")
        void shouldFailWhenApplicationRepositorySaveFails() {
            // Given
            RuntimeException saveError = new RuntimeException("Save operation failed");
            when(userVaLidateUseCase.validateApplication(any()))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(1)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.error(saveError));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When & Then
            StepVerifier.create(useCase.execute(inputApplication))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(applicationRepository).save(any(Application.class));
            verify(transactionalExecutor).executeInTransaction(any(Mono.class));
        }

        @Test
        @DisplayName("Should fail when transaction executor fails")
        void shouldFailWhenTransactionExecutorFails() {
            // Given
            RuntimeException transactionError = new RuntimeException("Transaction failed");
            when(userVaLidateUseCase.validateApplication(any()))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(1)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.just(savedApplication));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenReturn(Mono.error(transactionError));

            // When & Then
            StepVerifier.create(useCase.execute(inputApplication))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(transactionalExecutor).executeInTransaction(any(Mono.class));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle concurrent execution correctly")
        void shouldHandleConcurrentExecutionCorrectly() {
            // Given
            when(userVaLidateUseCase.validateApplication(any()))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(1)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.just(savedApplication));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When - Execute multiple times concurrently
            Mono<Application> execution1 = useCase.execute(inputApplication);
            Mono<Application> execution2 = useCase.execute(inputApplication);

            // Then
            StepVerifier.create(Mono.zip(execution1, execution2))
                    .assertNext(tuple -> {
                        assertNotNull(tuple.getT1());
                        assertNotNull(tuple.getT2());
                    })
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Input Validation")
    class InputValidationTests {

        @Test
        @DisplayName("Should handle application with all fields populated")
        void shouldHandleApplicationWithAllFieldsPopulated() {
            // Given
            Application fullApplication = Application.builder()
                    .idApplication(UUID.randomUUID())
                    .amount(new BigDecimal("25000"))
                    .term(24)
                    .email("full@example.com")
                    .document("87654321")
                    .loanType(LoanType.builder().idLoanType(2).build())
                    .loanStatus(pendingStatus)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            when(userVaLidateUseCase.validateApplication(eq("87654321")))
                    .thenReturn(Mono.just(true));
            when(loanTypeRepository.findById(eq(2)))
                    .thenReturn(Mono.just(validLoanType));
            when(loanStatusRepository.findByName(eq("PENDING_REVIEW")))
                    .thenReturn(Mono.just(pendingStatus));
            when(applicationRepository.save(any(Application.class)))
                    .thenReturn(Mono.just(savedApplication));
            when(transactionalExecutor.executeInTransaction(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When & Then
            StepVerifier.create(useCase.execute(fullApplication))
                    .assertNext(result -> {
                        assertNotNull(result);
                        assertEquals(validLoanType, result.getLoanType());
                        assertEquals(pendingStatus, result.getLoanStatus());
                    })
                    .verifyComplete();
        }
    }
}