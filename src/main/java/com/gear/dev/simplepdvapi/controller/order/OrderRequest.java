package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

  @JsonProperty("order_number")
  private String orderNumber;

  private List<OrderRequestProduct> products;

  @JsonProperty("created_on")
  private Date createdOn;

  @JsonProperty("requested_by")
  private String requestedBy;
}
