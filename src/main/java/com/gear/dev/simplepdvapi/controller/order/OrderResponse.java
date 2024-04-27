package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gear.dev.simplepdvapi.controller.product.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "The order unique identifier (`id`) on database",
          example = "1")
  private Long id;

  @Schema(name = "product_list",
          description = "A list of all products that belongs to this order.")
  @JsonProperty("product_list")
  private List<ProductResponse> productList;

  @Schema(name = "created_on",
          example = "2024-04-24T00:00:00",
          description = "Date and time where the product was created.")
  @JsonProperty("created_on")
  private Date createdOn;

  @Schema(name = "requested_by",
          example = "Attender #01",
          description = "A description of the attender of the order.")
  @JsonProperty("requested_by")
  private String requestBy;
}
