package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequestProduct {

  @Schema(
          name = "procuct_id",
          description = "A product `id` referring a product resource on the platform.",
          example = "1",
          requiredMode = REQUIRED)
  @JsonProperty("product_id")
  private Long productId;

  @Schema(
          description = "The amount of the requested product.",
          example = "2",
          requiredMode = REQUIRED)
  private int quantity;
}
