package com.gear.dev.simplepdvapi.controller.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

  private Long id;
  private String category;
  private Date createdOn;
  private String description;
  private BigDecimal price;
}
