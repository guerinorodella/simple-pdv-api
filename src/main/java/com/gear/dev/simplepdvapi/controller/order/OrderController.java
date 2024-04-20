package com.gear.dev.simplepdvapi.controller.order;

import com.gear.dev.simplepdvapi.model.OrderModel;
import com.gear.dev.simplepdvapi.repository.OrderRepository;
import com.gear.dev.simplepdvapi.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/simple-pdv/v1/order")
public class OrderController {

  private final OrderRepository repository;
  private final ProductRepository productRepository;

  @Autowired
  public OrderController(OrderRepository repository,
                         ProductRepository productRepository) {
    this.repository = repository;
    this.productRepository = productRepository;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrder(@PathVariable Long id) {
    Optional<OrderModel> orderResponse = repository.findById(id);
    if (orderResponse.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var order = orderResponse.get();
    List<OrderRequestProductResponse> orderProductResponseList = order.getProducts()
            .stream()
            .map(OrderRequestProductResponse::fromProductModel)
            .toList();
    var response = OrderRequestResponse.builder()
            .orderRequestId(order.getId())
            .orderNumber(order.getOrderNumber())
            .requestedBy(order.getRequestedBy())
            .products(orderProductResponseList)
            .build();

    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
    var order = new OrderModel();
    BeanUtils.copyProperties(request, order);
    if (order.getCreatedOn() == null) {
      order.setCreatedOn(new Date());
    }

    order.setProducts(new ArrayList<>());
    for (var orderRequestProduct : request.getProducts()) {
      var prodResponse = productRepository.findById(orderRequestProduct.getProductId());
      if (prodResponse.isEmpty()) {
        throw new IllegalArgumentException();
      }

      var product = prodResponse.get();
      for (int i = 0; i < orderRequestProduct.getQuantity(); i++) {
        order.getProducts().add(product);
      }

    }
    order = repository.save(order);
    List<OrderRequestProductResponse> orderProductResponseList = order.getProducts()
            .stream()
            .map(OrderRequestProductResponse::fromProductModel)
            .toList();
    var response = OrderRequestResponse.builder()
            .orderRequestId(order.getId())
            .orderNumber(order.getOrderNumber())
            .requestedBy(order.getRequestedBy())
            .products(orderProductResponseList)
            .build();

    return ResponseEntity.created(ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("simple-pdv/v1/order")
                    .path("/{id}")
                    .buildAndExpand(order.getId())
                    .toUri())
            .body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
    var orderResponse = repository.findById(id);
    if(orderResponse.isEmpty()) {
      throw new IllegalArgumentException();
    }

    var order = orderResponse.get();
    order.markAsDeleted();
    repository.save(order);
    return ResponseEntity.noContent().build();
  }
}
