package com.gear.dev.simplepdvapi.controller.order;

import com.gear.dev.simplepdvapi.model.OrderModel;
import com.gear.dev.simplepdvapi.repository.OrderRepository;
import com.gear.dev.simplepdvapi.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

  @Operation(
          operationId = "getOrder",
          summary = "Gets an order",
          description = "Returns an existing order resource mapped by the `id` representation",
          responses = {
                  @ApiResponse(responseCode = "200",
                          description = "Success request",
                          content = {@Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = OrderRequestResponse.class))}),
                  @ApiResponse(responseCode = "404",
                          description = "Not found",
                          content = {@Content(schema = @Schema())}),
                  @ApiResponse(responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          }
  )
  @GetMapping(value = "/{id}",
          produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getOrder(@PathVariable Long id) {
    try {
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
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(summary = "Creates order",
          description = "Creates a new order request",
          responses = {
                  @ApiResponse(responseCode = "201",
                          description = "Success created order request",
                          content = {@Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = OrderRequestResponse.class))},
                          links = {@Link(
                                  operationId = "getOrder",
                                  description = "The `id` value returned on response, can be used as the `id` parameter " +
                                          "in `GET /order/{id}` and `DELETE /order/{id}`")
                          }),
                  @ApiResponse(responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          })
  @PostMapping(value = "/create",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
    var order = new OrderModel();
    BeanUtils.copyProperties(request, order);
    if (order.getCreatedOn() == null) {
      order.setCreatedOn(new Date());
    }

    order.setProducts(new ArrayList<>());

    try {
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
      order.markAsCreated();
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
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(summary = "Delete order",
          description = "Deletes an existing order",
          responses = {
                  @ApiResponse(responseCode = "204",
                          description = "No content - success deleted resource.",
                          content = {@Content(schema = @Schema())}),
                  @ApiResponse(responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          })
  @DeleteMapping(value = "{id}",
          consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
    try {
      var orderResponse = repository.findById(id);
      if (orderResponse.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      var order = orderResponse.get();
      order.markAsDeleted();
      repository.save(order);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
