package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gear.dev.simplepdvapi.controller.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

  private Long id;

  @JsonProperty("product_list")
  private List<ProductResponse> productList;

  @JsonProperty("created_on")
  private Date createdOn;

  @JsonProperty("requested_by")
  private String requestBy;
}
