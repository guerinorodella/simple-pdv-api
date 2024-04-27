package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

  @Schema(name = "order_number",
          example = "ORD_001",
          description = "A number reference to the order.",
          requiredMode = REQUIRED)
  @JsonProperty("order_number")
  private String orderNumber;

  @Schema(description = "A list of all products that belongs to this order.",
          requiredMode = REQUIRED)
  private List<OrderRequestProduct> products;


  @Schema(description = "Date and time when the order was requested. If not provided, assume the date and time the " +
          "order was received")
  @JsonProperty("created_on")
  private Date createdOn;

  @Schema(name = "requested_by",
          example = "Attender #01",
          description = "A description of the attender to the request",
          requiredMode = REQUIRED)
  @JsonProperty("requested_by")
  private String requestedBy;
}
