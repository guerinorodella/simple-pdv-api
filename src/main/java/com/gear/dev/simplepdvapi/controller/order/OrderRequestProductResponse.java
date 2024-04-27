package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gear.dev.simplepdvapi.model.ProductModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestProductResponse {

  @Schema(
          name = "id",
          example = "1",
          description = "The product unique identifier on database")
  @JsonProperty("id")
  private Long productId;

  @Schema(
          example = "Drinks",
          description = "The product category")
  private String category;

  @Schema(
          example = "Coffee",
          description = "The product itself")
  private String description;

  @Schema(
          example = "5.5",
          description = "The product selling price")
  private BigDecimal price;

  public static OrderRequestProductResponse fromProductModel(ProductModel product) {
    return new OrderRequestProductResponse(product.getId(),
            product.getCategory(),
            product.getDescription(),
            product.getPrice());
  }
}
