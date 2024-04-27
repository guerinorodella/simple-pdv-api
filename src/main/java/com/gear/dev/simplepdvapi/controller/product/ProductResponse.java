package com.gear.dev.simplepdvapi.controller.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

  @Schema(
          example = "1",
          description = "The product unique identifier on database")
  private Long id;

  @Schema(
          example = "Drinks",
          description = "The product category")
  private String category;

  @Schema(
          name = "created_on",
          example = "2024-04-24T00:00:00",
          description = "Date and time where the product was created")
  @JsonProperty("created_on")
  private Date createdOn;

  @Schema(
          example = "Coffee",
          description = "The product itself")
  private String description;

  @Schema(
          example = "5.5",
          description = "The product selling price")
  private BigDecimal price;
}
