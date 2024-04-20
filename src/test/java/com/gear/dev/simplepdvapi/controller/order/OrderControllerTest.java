package com.gear.dev.simplepdvapi.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear.dev.simplepdvapi.model.OrderModel;
import com.gear.dev.simplepdvapi.model.ProductModel;
import com.gear.dev.simplepdvapi.repository.OrderRepository;
import com.gear.dev.simplepdvapi.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.ldap.SortControl;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderControllerTest {

  @InjectMocks
  private OrderController instance;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private OrderRepository orderRepository;

  private AutoCloseable context;

  @BeforeEach
  void setup() {
    context = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() {
    try {
      context.close();
    } catch (Exception ignored) {
    }
  }

  @Test
  void getOrderById() throws IOException {
    Optional<OrderModel> orderRequest = Optional.of(OrderModel.builder()
            .id(42L)
            .requestedBy("An Attender")
            .createdOn(new Date())
            .orderNumber("ORD_001")
            .products(Arrays.asList(ProductModel.builder()
                            .id(1L)
                            .active(true)
                            .createdOn(new Date())
                            .price(BigDecimal.valueOf(4.5))
                            .category("Drinks")
                            .description("Coffee")
                            .build(),
                    ProductModel.builder()
                            .id(2L)
                            .active(true)
                            .createdOn(new Date())
                            .price(BigDecimal.valueOf(5.5))
                            .category("Drinks")
                            .description("Mocha Coffee")
                            .build()))
            .build());
    Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(orderRequest);
    var expectedResponseBody = new ObjectMapper()
            .readValue(getClass().getResource("/order/order_response.json"), OrderRequestResponse.class);

    var response = instance.getOrder(42L);

    Assertions.assertNotNull(response);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    var responseBody = (OrderRequestResponse) response.getBody();
    assertEquals(expectedResponseBody, responseBody);

  }

  @Test
  void createOrder() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    var orderRequest = new ObjectMapper()
            .readValue(getClass().getResource("/order/order_request.json"), OrderRequest.class);
    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(ProductModel.builder()
            .id(1L)
            .description("Coffee")
            .createdOn(new Date())
            .category("Drinks")
            .price(BigDecimal.valueOf(4.5))
            .build()));

    var orderModel = OrderModel.builder()
            .id(42L)
            .requestedBy("Attender 01")
            .createdOn(new Date())
            .orderNumber("001")
            .products(Arrays.asList(ProductModel.builder()
                            .id(1L)
                            .active(true)
                            .createdOn(new Date())
                            .price(BigDecimal.valueOf(4.5))
                            .category("Drinks")
                            .description("Coffee")
                            .build(),
                    ProductModel.builder()
                            .id(1L)
                            .active(true)
                            .createdOn(new Date())
                            .price(BigDecimal.valueOf(4.5))
                            .category("Drinks")
                            .description("Coffee")
                            .build()))
            .build();
    Mockito.when(orderRepository.save(Mockito.any(OrderModel.class))).thenReturn(orderModel);

    var response = instance.createOrder(orderRequest);

    Assertions.assertNotNull(response);
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
    assertEquals("http://localhost/simple-pdv/v1/order/42", response.getHeaders().get("Location").get(0));

  }

  @Test
  void deleteOrder() {
    Optional<OrderModel> orderRecord = Optional.of(OrderModel.builder()
            .id(42L)
            .requestedBy("An Attender")
            .createdOn(new Date())
            .orderNumber("ORD_001")
            .products(Collections.singletonList(ProductModel.builder()
                            .id(1L)
                            .active(true)
                            .createdOn(new Date())
                            .price(BigDecimal.valueOf(4.5))
                            .category("Drinks")
                            .description("Coffee")
                            .build()))
            .build());
    Mockito.when(orderRepository.findById(42L)).thenReturn(orderRecord);

    var response = instance.deleteOrder(42L);
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    assertEquals("DELETED", orderRecord.get().getStatus());
  }
}