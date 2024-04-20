package com.gear.dev.simplepdvapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_request")
public class OrderModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_number")
  private String orderNumber;

  @ManyToMany(cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
  })
  @JoinTable(name = "order_request_products",
          joinColumns = @JoinColumn(name = "id_order_request"),
          inverseJoinColumns = @JoinColumn(name = "id_product"))
  private List<ProductModel> products;

  @Column(name = "created_on")
  private Date createdOn;

  @Column(name = "requested_by")
  private String requestedBy;

  private String status;

  public void markAsCreated() {
    setStatus(OrderStatus.CREATED.name());
  }
  public void markAsClosed() {
    setStatus(OrderStatus.CLOSED.name());
  }
  public void markAsDeleted() {
    setStatus(OrderStatus.DELETED.name());
  }
  enum OrderStatus {
    CREATED,
    CLOSED,
    DELETED
  }
}
