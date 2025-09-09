package com.xcode.loanservice.model.loantype;

import com.xcode.loanservice.model.common.exception.InvalidAmountException;
import com.xcode.loanservice.model.common.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeTest {

    @Nested
    @DisplayName("Creating new LoanType")
    class CreateNewTests {

        @Test
        @DisplayName("Should create loan type with valid data")
        void shouldCreateLoanTypeWithValidData() {
            String name = "Personal Loan";
            BigDecimal minAmount = new BigDecimal("1000");
            BigDecimal maxAmount = new BigDecimal("50000");
            BigDecimal interestRate = new BigDecimal("0.15");
            Boolean automaticValidation = true;

            LoanType loanType = LoanType.createNew(name, minAmount, maxAmount, interestRate, automaticValidation);

            assertNotNull(loanType);
            assertEquals(name, loanType.getName());
            assertEquals(minAmount, loanType.getMinAmount());
            assertEquals(maxAmount, loanType.getMaxAmount());
            assertEquals(interestRate, loanType.getInterestRate());
            assertEquals(automaticValidation, loanType.getAutomaticValidation());
            assertNotNull(loanType.getCreatedAt());
            assertNotNull(loanType.getUpdatedAt());
        }

        @Test
        @DisplayName("Should create loan type with automatic validation false")
        void shouldCreateLoanTypeWithAutomaticValidationFalse() {
            LoanType loanType = LoanType.createNew(
                    "Manual Review Loan",
                    new BigDecimal("5000"),
                    new BigDecimal("100000"),
                    new BigDecimal("0.12"),
                    false
            );

            assertNotNull(loanType);
            assertFalse(loanType.getAutomaticValidation());
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when minAmount is null")
        void shouldThrowExceptionWhenMinAmountIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> LoanType.createNew("Personal", null, new BigDecimal("50000"), new BigDecimal("0.15"), true)
            );
            assertTrue(exception.getMessage().contains("minAmount"));
        }

        @Test
        @DisplayName("Should throw MissingRequiredFieldException when maxAmount is null")
        void shouldThrowExceptionWhenMaxAmountIsNull() {
            MissingRequiredFieldException exception = assertThrows(
                    MissingRequiredFieldException.class,
                    () -> LoanType.createNew("Personal", new BigDecimal("1000"), null, new BigDecimal("0.15"), true)
            );
            assertTrue(exception.getMessage().contains("maxAmount"));
        }

        @Test
        @DisplayName("Should throw InvalidAmountException when minAmount is zero")
        void shouldThrowExceptionWhenMinAmountIsZero() {
            assertThrows(
                    InvalidAmountException.class,
                    () -> LoanType.createNew("Personal", BigDecimal.ZERO, new BigDecimal("50000"), new BigDecimal("0.15"), true)
            );
        }

        @Test
        @DisplayName("Should throw InvalidAmountException when minAmount is negative")
        void shouldThrowExceptionWhenMinAmountIsNegative() {
            assertThrows(
                    InvalidAmountException.class,
                    () -> LoanType.createNew("Personal", new BigDecimal("-1000"), new BigDecimal("50000"), new BigDecimal("0.15"), true)
            );
        }

        @Test
        @DisplayName("Should throw InvalidAmountException when minAmount is greater than maxAmount")
        void shouldThrowExceptionWhenMinAmountGreaterThanMaxAmount() {
            assertThrows(
                    InvalidAmountException.class,
                    () -> LoanType.createNew("Personal", new BigDecimal("60000"), new BigDecimal("50000"), new BigDecimal("0.15"), true)
            );
        }

        @Test
        @DisplayName("Should create loan type when minAmount equals maxAmount")
        void shouldCreateLoanTypeWhenMinAmountEqualsMaxAmount() {
            BigDecimal amount = new BigDecimal("25000");
            
            LoanType loanType = LoanType.createNew("Fixed Amount", amount, amount, new BigDecimal("0.12"), true);
            
            assertNotNull(loanType);
            assertEquals(amount, loanType.getMinAmount());
            assertEquals(amount, loanType.getMaxAmount());
        }
    }

    @Nested
    @DisplayName("Creating LoanType from Repository")
    class FromRepositoryTests {

        @Test
        @DisplayName("Should create loan type from repository data")
        void shouldCreateLoanTypeFromRepositoryData() {
            Integer id = 1;
            String name = "Business Loan";
            BigDecimal minAmount = new BigDecimal("10000");
            BigDecimal maxAmount = new BigDecimal("500000");
            BigDecimal interestRate = new BigDecimal("0.08");
            Boolean automaticValidation = false;
            LocalDateTime createdAt = LocalDateTime.now().minusDays(5);
            LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

            LoanType loanType = LoanType.fromRepository(
                    id, name, minAmount, maxAmount, interestRate, automaticValidation, createdAt, updatedAt
            );

            assertNotNull(loanType);
            assertEquals(id, loanType.getIdLoanType());
            assertEquals(name, loanType.getName());
            assertEquals(minAmount, loanType.getMinAmount());
            assertEquals(maxAmount, loanType.getMaxAmount());
            assertEquals(interestRate, loanType.getInterestRate());
            assertEquals(automaticValidation, loanType.getAutomaticValidation());
            assertEquals(createdAt, loanType.getCreatedAt());
            assertEquals(updatedAt, loanType.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Amount Validation")
    class AmountValidationTests {

        private LoanType loanType;

        @Test
        @DisplayName("Should validate amount within range")
        void shouldValidateAmountWithinRange() {
            loanType = LoanType.createNew(
                    "Personal",
                    new BigDecimal("1000"),
                    new BigDecimal("50000"),
                    new BigDecimal("0.15"),
                    true
            );

            assertTrue(loanType.validateAmount(new BigDecimal("25000")));
            assertTrue(loanType.validateAmount(new BigDecimal("1000"))); // min boundary
            assertTrue(loanType.validateAmount(new BigDecimal("50000"))); // max boundary
        }

        @Test
        @DisplayName("Should reject amount below minimum")
        void shouldRejectAmountBelowMinimum() {
            loanType = LoanType.createNew(
                    "Personal",
                    new BigDecimal("1000"),
                    new BigDecimal("50000"),
                    new BigDecimal("0.15"),
                    true
            );

            assertFalse(loanType.validateAmount(new BigDecimal("999")));
            assertFalse(loanType.validateAmount(new BigDecimal("500")));
        }

        @Test
        @DisplayName("Should reject amount above maximum")
        void shouldRejectAmountAboveMaximum() {
            loanType = LoanType.createNew(
                    "Personal",
                    new BigDecimal("1000"),
                    new BigDecimal("50000"),
                    new BigDecimal("0.15"),
                    true
            );

            assertFalse(loanType.validateAmount(new BigDecimal("50001")));
            assertFalse(loanType.validateAmount(new BigDecimal("100000")));
        }

        @Test
        @DisplayName("Should validate exact amounts for fixed amount loan type")
        void shouldValidateExactAmountForFixedAmountLoanType() {
            BigDecimal fixedAmount = new BigDecimal("25000");
            loanType = LoanType.createNew(
                    "Fixed Amount Loan",
                    fixedAmount,
                    fixedAmount,
                    new BigDecimal("0.10"),
                    true
            );

            assertTrue(loanType.validateAmount(fixedAmount));
            assertFalse(loanType.validateAmount(new BigDecimal("24999")));
            assertFalse(loanType.validateAmount(new BigDecimal("25001")));
        }

        @Test
        @DisplayName("Should handle decimal amounts correctly")
        void shouldHandleDecimalAmountsCorrectly() {
            loanType = LoanType.createNew(
                    "Micro Loan",
                    new BigDecimal("100.50"),
                    new BigDecimal("999.99"),
                    new BigDecimal("0.20"),
                    true
            );

            assertTrue(loanType.validateAmount(new BigDecimal("500.75")));
            assertTrue(loanType.validateAmount(new BigDecimal("100.50"))); // min boundary
            assertTrue(loanType.validateAmount(new BigDecimal("999.99"))); // max boundary
            assertFalse(loanType.validateAmount(new BigDecimal("100.49")));
            assertFalse(loanType.validateAmount(new BigDecimal("1000.00")));
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should create loan type using builder")
        void shouldCreateLoanTypeUsingBuilder() {
            LocalDateTime now = LocalDateTime.now();
            
            LoanType loanType = LoanType.builder()
                    .idLoanType(1)
                    .name("Test Loan")
                    .minAmount(new BigDecimal("2000"))
                    .maxAmount(new BigDecimal("30000"))
                    .interestRate(new BigDecimal("0.12"))
                    .automaticValidation(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            assertNotNull(loanType);
            assertEquals(1, loanType.getIdLoanType());
            assertEquals("Test Loan", loanType.getName());
            assertEquals(new BigDecimal("2000"), loanType.getMinAmount());
            assertEquals(new BigDecimal("30000"), loanType.getMaxAmount());
            assertEquals(new BigDecimal("0.12"), loanType.getInterestRate());
            assertTrue(loanType.getAutomaticValidation());
            assertEquals(now, loanType.getCreatedAt());
            assertEquals(now, loanType.getUpdatedAt());
        }

        @Test
        @DisplayName("Should create copy using toBuilder")
        void shouldCreateCopyUsingToBuilder() {
            LoanType original = LoanType.createNew(
                    "Original",
                    new BigDecimal("1000"),
                    new BigDecimal("50000"),
                    new BigDecimal("0.15"),
                    true
            );

            LoanType copy = original.toBuilder()
                    .name("Modified")
                    .interestRate(new BigDecimal("0.18"))
                    .build();

            assertNotEquals(original.getName(), copy.getName());
            assertEquals("Modified", copy.getName());
            assertNotEquals(original.getInterestRate(), copy.getInterestRate());
            assertEquals(new BigDecimal("0.18"), copy.getInterestRate());
            
            // Other fields should remain the same
            assertEquals(original.getMinAmount(), copy.getMinAmount());
            assertEquals(original.getMaxAmount(), copy.getMaxAmount());
            assertEquals(original.getAutomaticValidation(), copy.getAutomaticValidation());
        }
    }
}