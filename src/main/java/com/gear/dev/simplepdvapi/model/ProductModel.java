package com.gear.dev.simplepdvapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductModel {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToMany(mappedBy = "products",
          cascade = {
                  CascadeType.PERSIST,
                  CascadeType.MERGE
          })
  private Set<OrderModel> orders;

  private boolean active;

  private String category;

  @Column(name = "created_on")
  private Date createdOn;

  private String description;

  private BigDecimal price;

}
