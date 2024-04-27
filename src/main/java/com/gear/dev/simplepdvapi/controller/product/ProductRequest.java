package com.gear.dev.simplepdvapi.controller.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@Schema
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

  @Schema(
          example = "Drinks",
          description = "The product category",
          requiredMode = REQUIRED)
  private String category;

  @Schema(
          name = "created_on",
          example = "2024-04-24T00:00:00",
          description = "Date and time where the product was created. If not provided will assume the request date time.")
  @JsonProperty("created_on")
  private Date createdOn;

  @Schema(
          example = "Coffee",
          description = "The product itself",
          requiredMode = REQUIRED)
  private String description;

  @Schema(
          example = "5.5",
          description = "The product selling price",
          requiredMode = REQUIRED)
  private BigDecimal price;

}
