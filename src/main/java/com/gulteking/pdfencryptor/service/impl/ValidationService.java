package com.gulteking.pdfencryptor.service.impl;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private final Validator validator;

  public ValidationService(Validator validator) {
    this.validator = validator;
  }

  /**
   * Validates an object against its constraints.
   *
   * @param request the object to be validated
   * @throws IllegalArgumentException if the request is null
   * @throws ConstraintViolationException if the object violates one or more constraints
   */
  public void validate(Object request) {
    if (request == null) {
      throw new IllegalArgumentException("Validation request cannot be null");
    }

    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);

    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException("Validation failed", constraintViolations);
    }
  }
}
