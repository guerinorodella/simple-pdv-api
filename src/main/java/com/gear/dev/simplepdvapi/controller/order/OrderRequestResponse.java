package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestResponse {

  @JsonProperty("id")
  private Long orderRequestId;

  @Schema(
          name = "order_number",
          example = "ORD_001",
          description = "The number reference to the order.")
  @JsonProperty("order_number")
  private String orderNumber;

  @Schema(
          name = "requested_by",
          description = "The attender description of the order request",
          requiredMode = REQUIRED)
  @JsonProperty("requested_by")
  private String requestedBy;

  @Schema(
          description = "A list of all products that belongs to this order.")
  private List<OrderRequestProductResponse> products;

}
