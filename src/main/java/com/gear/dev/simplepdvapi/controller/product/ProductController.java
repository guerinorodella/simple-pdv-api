package com.gear.dev.simplepdvapi.controller.product;

import com.gear.dev.simplepdvapi.model.ProductModel;
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

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/simple-pdv/v1/product")
public class ProductController {

  private final ProductRepository repository;
  private final ProductValidator productValidator;

  @Autowired
  public ProductController(ProductRepository repository,
                           ProductValidator productValidator) {
    this.repository = repository;
    this.productValidator = productValidator;
  }

  @Operation(
          operationId = "getUser",
          summary = "Gets a product",
          description = "Returns an existing product by it's `id` representation",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Success request",
                          content = {@Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = ProductResponse.class))}),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Not found",
                          content = {@Content(schema = @Schema())}),
                  @ApiResponse(
                          responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          }
  )
  @GetMapping(value = "/{id}",
          produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getProductById(@PathVariable Long id) {
    try {
      var productResponse = repository.findById(id);
      if (productResponse.isEmpty()) {
        return notFound().build();
      }

      var productJsonResponse = new ProductResponse();
      BeanUtils.copyProperties(productResponse.get(), productJsonResponse);
      return ResponseEntity.ok(productJsonResponse);
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(
          summary = "Creates a new product",
          description = "Creates a new product resource",
          responses = {
                  @ApiResponse(
                          responseCode = "201",
                          description = "Success create request",
                          content = {@Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = ProductResponse.class))},
                          links = {@Link(
                                  operationId = "getProduct",
                                  description = "The `id` value returned on response, can be used as the `id` parameter " +
                                          "in `GET /product/{id}` and `DELETE /product/{id}`")
                          }),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Bad request - check the values provided",
                          content = {@Content(schema = @Schema())}),
                  @ApiResponse(
                          responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          })
  @PostMapping(value = "/create",
          consumes = APPLICATION_JSON_VALUE,
          produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
    try {
      var validationResponse = productValidator.isNewProductValid(request);

      if (!validationResponse.isValid()) {
        return ResponseEntity.badRequest().build();
      }

      var newProduct = ProductModel.builder()
              .active(true)
              .description(request.getDescription())
              .category(request.getCategory())
              .createdOn(new Date())
              .price(request.getPrice())
              .build();
      newProduct = repository.save(newProduct);

      return ResponseEntity.created(ServletUriComponentsBuilder
                      .fromCurrentRequest()
                      .replacePath("simple-pdv/v1/product")
                      .path("/{id}")
                      .buildAndExpand(newProduct.getId())
                      .toUri())
              .body(ProductResponse.builder()
                      .id(newProduct.getId())
                      .category(newProduct.getCategory())
                      .createdOn(newProduct.getCreatedOn())
                      .description(newProduct.getDescription())
                      .price(newProduct.getPrice())
                      .build());
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(
          summary = "Deletes a product",
          description = "Deletes an existing product by it's 'id' representation",
          responses = {
                  @ApiResponse(
                          responseCode = "204",
                          description = "No content - Success deleted",
                          content = {@Content(schema = @Schema())}),
                  @ApiResponse(
                          responseCode = "5xx",
                          description = "Internal server error. Something went wrong try again",
                          content = {@Content(schema = @Schema())})
          }
  )
  @DeleteMapping(value = "/{id}",
          produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    try {
      var productResponse = repository.findById(id);
      if (productResponse.isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      repository.delete(productResponse.get());
      return ResponseEntity.noContent().build();
    } catch (RuntimeException serverError) {
      return ResponseEntity.internalServerError().build();
    }
  }

}
