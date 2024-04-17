package com.gear.dev.simplepdvapi.controller;

import lombok.Data;

@Data
public class ValidationResponse<T> {

  private final boolean isValid;
  private String message;
  private T customData;

  public static ValidationResponse<?> ofSuccess() {
    return new ValidationResponse(true);
  }
}
