package com.gear.dev.simplepdvapi.controller;

import com.gear.dev.simplepdvapi.controller.product.ProductController;
import com.gear.dev.simplepdvapi.controller.product.ProductRequest;
import com.gear.dev.simplepdvapi.controller.product.ProductResponse;
import com.gear.dev.simplepdvapi.controller.product.ProductValidator;
import com.gear.dev.simplepdvapi.model.ProductModel;
import com.gear.dev.simplepdvapi.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ProductControllerTest {

  @InjectMocks
  private ProductController instance;
  @Mock
  private ProductValidator validator;
  @Mock
  private ProductRepository repository;

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
  void notFound_whenProductDoesNotExists() {

    Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

    var response = instance.getProductById(42L);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

  }

  @Test
  void findProductById() {

    var aProduct = ProductModel.builder()
            .id(42L)
            .active(true)
            .category("Hot Drink")
            .createdOn(new Date())
            .description("Brazilian Coffee 90ml")
            .price(BigDecimal.valueOf(4.5))
            .build();
    Mockito.when(repository.findById(42L)).thenReturn(Optional.of(aProduct));

    var response = instance.getProductById(42L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertInstanceOf(ProductResponse.class, response.getBody());
    ProductResponse body = (ProductResponse)response.getBody();
    assertEquals(42L, body.getId());
    assertEquals("Hot Drink", body.getCategory());
    assertEquals("Brazilian Coffee 90ml", body.getDescription());
    assertEquals(4.5, body.getPrice().doubleValue());

  }

  @Test
  void newProduct() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    var productRequest = ProductRequest.builder()
            .category("Main")
            .description("Java Coffee")
            .price(BigDecimal.valueOf(3.5))
            .build();
    Mockito.when(validator.isNewProductValid(productRequest)).thenReturn(ValidationResponse.ofSuccess());
    ProductModel newProduct = ProductModel.builder()
            .id(42L)
            .build();
    Mockito.when(repository.save(ArgumentMatchers.any(ProductModel.class))).thenReturn(newProduct);

    var response = instance.addNewProduct(productRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getHeaders().get("Location"));
    assertEquals("http://localhost/simple-pdv/v1/product/42", response.getHeaders().get("Location").get(0));
  }


  @Test
  void deleteProduct() {

    var aProduct = ProductModel.builder()
            .id(42L)
            .active(true)
            .category("Hot Drink")
            .createdOn(new Date())
            .description("Brazilian Coffee 90ml")
            .price(BigDecimal.valueOf(4.5))
            .build();
    Mockito.when(repository.findById(42L)).thenReturn(Optional.of(aProduct));

    var response = instance.deleteProduct(42L);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

  }

  @Test
  void deleteProduct_returnsBadRequest_whenProductDoesNotExists() {

    Mockito.when(repository.findById(42L)).thenReturn(Optional.empty());

    var response = instance.deleteProduct(42L);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }
}