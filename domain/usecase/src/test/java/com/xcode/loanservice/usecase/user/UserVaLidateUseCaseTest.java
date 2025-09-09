package com.xcode.loanservice.usecase.user;

import com.xcode.loanservice.model.common.exception.DomainErrorCode;
import com.xcode.loanservice.model.common.exception.DomainException;
import com.xcode.loanservice.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserVaLidateUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private UserVaLidateUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UserVaLidateUseCase(userRepository);
    }

    @Nested
    @DisplayName("Successful User Validation")
    class SuccessfulUserValidationTests {

        @Test
        @DisplayName("Should validate user successfully when user exists")
        void shouldValidateUserSuccessfullyWhenUserExists() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(true));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .assertNext(result -> {
                        assertTrue(result);
                    })
                    .verifyComplete();

            verify(userRepository).existsByDocument(document);
        }

        @Test
        @DisplayName("Should handle different document formats")
        void shouldHandleDifferentDocumentFormats() {
            // Given
            String[] documents = {"123456789", "98765432", "1234567890", "CC123456789", "ID-98765432"};

            for (String document : documents) {
                when(userRepository.existsByDocument(eq(document)))
                        .thenReturn(Mono.just(true));

                // When & Then
                StepVerifier.create(useCase.validateApplication(document))
                        .assertNext(result -> assertTrue(result))
                        .verifyComplete();

                verify(userRepository).existsByDocument(document);
            }
        }
    }

    @Nested
    @DisplayName("User Validation Failures")
    class UserValidationFailuresTests {

        @Test
        @DisplayName("Should fail validation when user does not exist")
        void shouldFailValidationWhenUserDoesNotExist() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectErrorMatches(throwable -> {
                        assertInstanceOf(DomainException.class, throwable);
                        DomainException domainException = (DomainException) throwable;
                        assertEquals(DomainErrorCode.USER_NOT_FOUND.getCode(), domainException.getErrorCode());
                        assertEquals(DomainErrorCode.USER_NOT_FOUND.getDefaultMessage(), domainException.getErrorMessage());
                        return true;
                    })
                    .verify();

            verify(userRepository).existsByDocument(document);
        }

        @Test
        @DisplayName("Should fail validation when repository returns empty")
        void shouldFailValidationWhenRepositoryReturnsEmpty() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.empty());

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectErrorMatches(throwable -> {
                        assertInstanceOf(DomainException.class, throwable);
                        DomainException domainException = (DomainException) throwable;
                        assertEquals("USR_001", domainException.getErrorCode());
                        return true;
                    })
                    .verify();

            verify(userRepository).existsByDocument(document);
        }

        @Test
        @DisplayName("Should fail validation when repository throws error")
        void shouldFailValidationWhenRepositoryThrowsError() {
            // Given
            String document = "12345678";
            RuntimeException repositoryError = new RuntimeException("Database connection failed");
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.error(repositoryError));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectError(RuntimeException.class)
                    .verify();

            verify(userRepository).existsByDocument(document);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Input Validation")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null document")
        void shouldHandleNullDocument() {
            // Given
            when(userRepository.existsByDocument(eq(null)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(null))
                    .expectError(DomainException.class)
                    .verify();

            verify(userRepository).existsByDocument(null);
        }

        @Test
        @DisplayName("Should handle empty document")
        void shouldHandleEmptyDocument() {
            // Given
            String emptyDocument = "";
            when(userRepository.existsByDocument(eq(emptyDocument)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(emptyDocument))
                    .expectError(DomainException.class)
                    .verify();

            verify(userRepository).existsByDocument(emptyDocument);
        }

        @Test
        @DisplayName("Should handle whitespace-only document")
        void shouldHandleWhitespaceOnlyDocument() {
            // Given
            String whitespaceDocument = "   ";
            when(userRepository.existsByDocument(eq(whitespaceDocument)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(whitespaceDocument))
                    .expectError(DomainException.class)
                    .verify();

            verify(userRepository).existsByDocument(whitespaceDocument);
        }

        @Test
        @DisplayName("Should handle very long document")
        void shouldHandleVeryLongDocument() {
            // Given
            String longDocument = "1".repeat(1000);
            when(userRepository.existsByDocument(eq(longDocument)))
                    .thenReturn(Mono.just(true));

            // When & Then
            StepVerifier.create(useCase.validateApplication(longDocument))
                    .assertNext(result -> assertTrue(result))
                    .verifyComplete();

            verify(userRepository).existsByDocument(longDocument);
        }
    }

    @Nested
    @DisplayName("Boolean Filter Behavior")
    class BooleanFilterBehaviorTests {

        @Test
        @DisplayName("Should filter out false values and emit error")
        void shouldFilterOutFalseValuesAndEmitError() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectError(DomainException.class)
                    .verify();

            // The filter(Boolean.TRUE::equals) should filter out false values,
            // making the stream empty and triggering switchIfEmpty with the error
        }

        @Test
        @DisplayName("Should pass through true values")
        void shouldPassThroughTrueValues() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(true));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .assertNext(result -> assertTrue(result))
                    .verifyComplete();

            // The filter(Boolean.TRUE::equals) should pass through true values
        }
    }

    @Nested
    @DisplayName("Reactive Stream Behavior")
    class ReactiveStreamBehaviorTests {

        @Test
        @DisplayName("Should handle concurrent validation requests")
        void shouldHandleConcurrentValidationRequests() {
            // Given
            String document1 = "12345678";
            String document2 = "87654321";
            
            when(userRepository.existsByDocument(eq(document1)))
                    .thenReturn(Mono.just(true));
            when(userRepository.existsByDocument(eq(document2)))
                    .thenReturn(Mono.just(false));

            // When
            Mono<Boolean> validation1 = useCase.validateApplication(document1);
            Mono<Boolean> validation2 = useCase.validateApplication(document2);

            // Then
            StepVerifier.create(validation1)
                    .assertNext(result -> assertTrue(result))
                    .verifyComplete();

            StepVerifier.create(validation2)
                    .expectError(DomainException.class)
                    .verify();

            verify(userRepository).existsByDocument(document1);
            verify(userRepository).existsByDocument(document2);
        }

        @Test
        @DisplayName("Should handle delayed repository response")
        void shouldHandleDelayedRepositoryResponse() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(true).delayElement(java.time.Duration.ofMillis(100)));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectSubscription()
                    .expectNoEvent(java.time.Duration.ofMillis(50))
                    .assertNext(result -> assertTrue(result))
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle repository timeout")
        void shouldHandleRepositoryTimeout() {
            // Given
            String document = "12345678";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.never()); // Never completes

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectSubscription()
                    .expectTimeout(java.time.Duration.ofSeconds(1))
                    .verify();
        }
    }

    @Nested
    @DisplayName("Error Message Verification")
    class ErrorMessageVerificationTests {

        @Test
        @DisplayName("Should provide correct error details in domain exception")
        void shouldProvideCorrectErrorDetailsInDomainException() {
            // Given
            String document = "nonexistentuser";
            when(userRepository.existsByDocument(eq(document)))
                    .thenReturn(Mono.just(false));

            // When & Then
            StepVerifier.create(useCase.validateApplication(document))
                    .expectErrorMatches(throwable -> {
                        assertInstanceOf(DomainException.class, throwable);
                        DomainException domainException = (DomainException) throwable;
                        
                        // Verify error code
                        assertEquals("USR_001", domainException.getErrorCode());
                        
                        // Verify error message
                        assertEquals("The user dont exists", domainException.getErrorMessage());
                        
                        // Verify it's the USER_NOT_FOUND error
                        assertEquals(DomainErrorCode.USER_NOT_FOUND.getCode(), domainException.getErrorCode());
                        assertEquals(DomainErrorCode.USER_NOT_FOUND.getDefaultMessage(), domainException.getErrorMessage());
                        
                        return true;
                    })
                    .verify();
        }
    }
}