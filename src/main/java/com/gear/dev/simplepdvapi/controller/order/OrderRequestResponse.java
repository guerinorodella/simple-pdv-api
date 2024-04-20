package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestResponse {

  @JsonProperty("id")
  private Long orderRequestId;

  @JsonProperty("order_number")
  private String orderNumber;

  @JsonProperty("requested_by")
  private String requestedBy;

  private List<OrderRequestProductResponse> products;

}
