package com.gear.dev.simplepdvapi.controller.product;

import com.gear.dev.simplepdvapi.controller.ValidationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Service
public class ProductValidator {


  public ValidationResponse isNewProductValid(ProductRequest request) {
    boolean isValid = request != null
            && !request.getDescription().isBlank()
            && request.getPrice().compareTo(ZERO) > 0;
    return new ValidationResponse(isValid);
  }
}
