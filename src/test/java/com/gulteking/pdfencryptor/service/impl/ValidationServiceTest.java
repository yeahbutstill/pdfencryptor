package com.gulteking.pdfencryptor.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

  @Mock private Validator validator;

  private ValidationService validationService;

  @BeforeEach
  void setUp() {
    validationService = new ValidationService(validator);
  }

  @Test
  void validate_withValidObject_doesNotThrowException() {
    // Given: A valid object with no constraint violations
    Object validObject = new Object();
    when(validator.validate(validObject)).thenReturn(Collections.emptySet());

    // When & Then: No exception is thrown
    assertDoesNotThrow(() -> validationService.validate(validObject));
  }

  @Test
  void validate_withInvalidObject_throwsConstraintViolationException() {
    // Given: An invalid object with constraint violations
    Object invalidObject = new Object();
    ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
    Set<ConstraintViolation<Object>> violations = Set.of(violation);

    when(validator.validate(invalidObject)).thenReturn(violations);

    // When: The validate method is called
    ConstraintViolationException exception =
        assertThrows(
            ConstraintViolationException.class, () -> validationService.validate(invalidObject));

    // Then: The exception contains the expected violations
    assertEquals(violations, exception.getConstraintViolations());
    assertEquals("Validation failed", exception.getMessage());
  }

  @Test
  void validate_withNullRequest_throwsIllegalArgumentException() {
    // Given: A null object
    Object nullObject = null;

    // When & Then: Validating null should throw IllegalArgumentException
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> validationService.validate(nullObject));

    assertEquals("Validation request cannot be null", exception.getMessage());
  }

  @Test
  void validate_withMultipleViolations_throwsConstraintViolationException() {
    // Given: An object with multiple constraint violations
    Object invalidObject = new Object();

    ConstraintViolation<Object> violation1 = mock(ConstraintViolation.class);
    ConstraintViolation<Object> violation2 = mock(ConstraintViolation.class);

    Set<ConstraintViolation<Object>> violations = Set.of(violation1, violation2);
    when(validator.validate(invalidObject)).thenReturn(violations);

    // When: The validate method is called
    ConstraintViolationException exception =
        assertThrows(
            ConstraintViolationException.class, () -> validationService.validate(invalidObject));

    // Then: The exception contains all expected violations
    assertEquals(violations, exception.getConstraintViolations());
    assertEquals("Validation failed", exception.getMessage());
  }
}
