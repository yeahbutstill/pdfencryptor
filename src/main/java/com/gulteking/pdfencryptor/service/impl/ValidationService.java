package com.gulteking.pdfencryptor.service.impl;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  private Validator validator;

  public ValidationService(Validator validator) {
    this.validator = validator;
  }

  public void validate(Object request) {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
    if (constraintViolations.size() != 0) {
      throw new ConstraintViolationException(constraintViolations);
    }
  }
}
