package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gear.dev.simplepdvapi.model.ProductModel;
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

  @JsonProperty("id")
  private Long productId;

  private String category;

  private String description;

  private BigDecimal price;

  public static OrderRequestProductResponse fromProductModel(ProductModel product) {
    return new OrderRequestProductResponse(product.getId(),
            product.getCategory(),
            product.getDescription(),
            product.getPrice());
  }
}
