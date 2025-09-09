package com.xcode.loanservice.model.application;

import com.xcode.loanservice.model.common.exception.InvalidAmountException;
import com.xcode.loanservice.model.common.exception.InvalidTermException;
import com.xcode.loanservice.model.common.exception.MissingRequiredFieldException;
import com.xcode.loanservice.model.common.exception.ValidateAmountRangeException;
import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.model.loantype.LoanType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    private LoanType validLoanType;
    private LoanStatus pendingStatus;
    private LoanStatus approvedStatus;
    private LoanStatus rejectedStatus;

    @BeforeEach
    void setUp() {
        validLoanType = LoanType.builder()
                .idLoanType(1)
                .name("Personal")
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

        approvedStatus = LoanStatus.builder()
                .idLoanStatus(2)
                .name(LoanStatusName.APPROVED)
                .description("Approved")
                .build();

        rejectedStatus = LoanStatus.builder()
                .idLoanStatus(3)
                .name(LoanStatusName.REJECTED)
                .description("Rejected")
                .build();
    }

    @Nested
    @DisplayName("Creating new Application")
    class CreateNewTests {

        @Test
        @DisplayName("Should create application with valid data")
        void shouldCreateApplicationWithValidData() {
            BigDecimal amount = new BigDecimal("10000");
            Integer term = 12;
            String email = " TEST@EXAMPLE.COM ";
            String document = " 12345678 ";

            Application application = Application.createNew(amount, term, email, document, pendingStatus, validLoanType);

            assertNotNull(application);
            assertEquals(amount, application.getAmount());
            assertEquals(term, application.getTerm());
            assertEquals("test@example.com", application.getEmail());
            assertEquals("12345678", application.getDocument());
            assertEquals(pendingStatus, application.getLoanStatus());
            assertEquals(validLoanType, application.getLoanType());
            assertNotNull(application.getCreatedAt());
            assertNotNull(application.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when amount is null")
        void shouldThrowExceptionWhenAmountIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> Application.createNew(null, 12, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
            assertTrue(exception.getMessage().contains("amount"));
        }

        @Test
        @DisplayName("Should throw InvalidAmountException when amount is zero")
        void shouldThrowExceptionWhenAmountIsZero() {
            assertThrows(
                    InvalidAmountException.class,
                    () -> Application.createNew(BigDecimal.ZERO, 12, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw InvalidAmountException when amount is negative")
        void shouldThrowExceptionWhenAmountIsNegative() {
            assertThrows(
                    InvalidAmountException.class,
                    () -> Application.createNew(new BigDecimal("-100"), 12, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when term is null")
        void shouldThrowExceptionWhenTermIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> Application.createNew(new BigDecimal("10000"), null, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
            assertTrue(exception.getMessage().contains("term"));
        }

        @Test
        @DisplayName("Should throw InvalidTermException when term is zero")
        void shouldThrowExceptionWhenTermIsZero() {
            assertThrows(
                    InvalidTermException.class,
                    () -> Application.createNew(new BigDecimal("10000"), 0, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw InvalidTermException when term is negative")
        void shouldThrowExceptionWhenTermIsNegative() {
            assertThrows(
                    InvalidTermException.class,
                    () -> Application.createNew(new BigDecimal("10000"), -1, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when document is null")
        void shouldThrowExceptionWhenDocumentIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> Application.createNew(new BigDecimal("10000"), 12, "test@example.com", null, pendingStatus, validLoanType)
            );
            assertTrue(exception.getMessage().contains("document"));
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when document is blank")
        void shouldThrowExceptionWhenDocumentIsBlank() {
            assertThrows(
                    MissingRequiredFieldException.class,
                    () -> Application.createNew(new BigDecimal("10000"), 12, "test@example.com", "   ", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when loanType is null")
        void shouldThrowExceptionWhenLoanTypeIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> Application.createNew(new BigDecimal("10000"), 12, "test@example.com", "12345678", pendingStatus, null)
            );
            assertTrue(exception.getMessage().contains("loanType"));
        }

        @Test
        @DisplayName("Should throw ValidateAmountRangeException when amount is below min range")
        void shouldThrowExceptionWhenAmountBelowMinRange() {
            assertThrows(
                    ValidateAmountRangeException.class,
                    () -> Application.createNew(new BigDecimal("500"), 12, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }

        @Test
        @DisplayName("Should throw ValidateAmountRangeException when amount is above max range")
        void shouldThrowExceptionWhenAmountAboveMaxRange() {
            assertThrows(
                    ValidateAmountRangeException.class,
                    () -> Application.createNew(new BigDecimal("100000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType)
            );
        }
    }

    @Nested
    @DisplayName("Creating Application from Repository")
    class FromRepositoryTests {

        @Test
        @DisplayName("Should create application from repository data")
        void shouldCreateApplicationFromRepositoryData() {
            UUID id = UUID.randomUUID();
            BigDecimal amount = new BigDecimal("15000");
            Integer term = 24;
            String email = " USER@DOMAIN.COM ";
            String document = " 87654321 ";
            LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
            LocalDateTime updatedAt = LocalDateTime.now();

            Application application = Application.fromRepository(
                    id, amount, term, email, document, pendingStatus, validLoanType, createdAt, updatedAt
            );

            assertNotNull(application);
            assertEquals(id, application.getIdApplication());
            assertEquals(amount, application.getAmount());
            assertEquals(term, application.getTerm());
            assertEquals("user@domain.com", application.getEmail());
            assertEquals("87654321", application.getDocument());
            assertEquals(pendingStatus, application.getLoanStatus());
            assertEquals(validLoanType, application.getLoanType());
            assertEquals(createdAt, application.getCreatedAt());
            assertEquals(updatedAt, application.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Application Status Changes")
    class StatusChangeTests {

        private Application pendingApplication;

        @BeforeEach
        void setUp() {
            pendingApplication = Application.createNew(
                    new BigDecimal("10000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType
            );
        }

        @Test
        @DisplayName("Should approve pending application")
        void shouldApprovePendingApplication() {
            Application approvedApplication = pendingApplication.approve(approvedStatus);

            assertNotNull(approvedApplication);
            assertEquals(approvedStatus, approvedApplication.getLoanStatus());
            assertNotNull(approvedApplication.getUpdatedAt());
            // Note: We can't reliably compare timestamps due to timing differences
        }

        @Test
        @DisplayName("Should reject pending application")
        void shouldRejectPendingApplication() {
            Application rejectedApplication = pendingApplication.reject(rejectedStatus);

            assertNotNull(rejectedApplication);
            assertEquals(rejectedStatus, rejectedApplication.getLoanStatus());
            assertNotNull(rejectedApplication.getUpdatedAt());
            // Note: We can't reliably compare timestamps due to timing differences
        }

        @Test
        @DisplayName("Should throw exception when trying to approve non-pending application")
        void shouldThrowExceptionWhenApprovingNonPendingApplication() {
            Application approvedApp = pendingApplication.approve(approvedStatus);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> approvedApp.approve(approvedStatus)
            );
            assertTrue(exception.getMessage().contains("PENDING_REVIEW"));
            assertTrue(exception.getMessage().contains("APPROVED"));
        }

        @Test
        @DisplayName("Should throw exception when trying to reject non-pending application")
        void shouldThrowExceptionWhenRejectingNonPendingApplication() {
            Application rejectedApp = pendingApplication.reject(rejectedStatus);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> rejectedApp.reject(rejectedStatus)
            );
            assertTrue(exception.getMessage().contains("PENDING_REVIEW"));
            assertTrue(exception.getMessage().contains("REJECTED"));
        }
    }

    @Nested
    @DisplayName("Monthly Payment Calculation")
    class MonthlyPaymentTests {

        @Test
        @DisplayName("Should calculate monthly payment correctly")
        void shouldCalculateMonthlyPaymentCorrectly() {
            Application application = Application.createNew(
                    new BigDecimal("10000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType
            );

            BigDecimal monthlyPayment = application.calculateMonthlyPayment();

            assertNotNull(monthlyPayment);
            assertTrue(monthlyPayment.compareTo(BigDecimal.ZERO) > 0);
            
            // Verify reasonable range for a 10000 loan at 15% APR for 12 months
            assertTrue(monthlyPayment.compareTo(new BigDecimal("800")) > 0); 
            assertTrue(monthlyPayment.compareTo(new BigDecimal("1000")) < 0);
        }

        @Test
        @DisplayName("Should calculate different payments for different amounts")
        void shouldCalculateDifferentPaymentsForDifferentAmounts() {
            Application application1 = Application.createNew(
                    new BigDecimal("5000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType
            );
            Application application2 = Application.createNew(
                    new BigDecimal("10000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType
            );

            BigDecimal payment1 = application1.calculateMonthlyPayment();
            BigDecimal payment2 = application2.calculateMonthlyPayment();

            assertNotEquals(payment1, payment2);
            assertTrue(payment2.compareTo(payment1.multiply(BigDecimal.valueOf(2))) == 0);
        }

        @Test
        @DisplayName("Should calculate different payments for different terms")
        void shouldCalculateDifferentPaymentsForDifferentTerms() {
            Application application1 = Application.createNew(
                    new BigDecimal("10000"), 12, "test@example.com", "12345678", pendingStatus, validLoanType
            );
            Application application2 = Application.createNew(
                    new BigDecimal("10000"), 24, "test@example.com", "12345678", pendingStatus, validLoanType
            );

            BigDecimal payment1 = application1.calculateMonthlyPayment();
            BigDecimal payment2 = application2.calculateMonthlyPayment();

            assertNotEquals(payment1, payment2);
            assertTrue(payment1.compareTo(payment2) > 0); // Shorter term = higher payment
        }
    }
}