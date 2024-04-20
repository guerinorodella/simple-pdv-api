package com.gear.dev.simplepdvapi.controller.product;

import com.gear.dev.simplepdvapi.controller.product.ProductRequest;
import com.gear.dev.simplepdvapi.controller.product.ProductResponse;
import com.gear.dev.simplepdvapi.controller.product.ProductValidator;
import com.gear.dev.simplepdvapi.model.ProductModel;
import com.gear.dev.simplepdvapi.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

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

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable Long id) {
    var productResponse = repository.findById(id);
    if (productResponse.isEmpty()) {
      return notFound().build();
    }

    var productJsonResponse = new ProductResponse();
    BeanUtils.copyProperties(productResponse.get(), productJsonResponse);
    return ResponseEntity.ok(productJsonResponse);

  }
  @PostMapping("/create")
  public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
    var validationResponse = productValidator.isNewProductValid(request);

    if(!validationResponse.isValid()) {
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
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    // TODO we must include session Key on HTTP header to validate WHO is deleting and check it's permission
    var productResponse = repository.findById(id);
    if(productResponse.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    repository.delete(productResponse.get());
    return ResponseEntity.noContent().build();
  }

}
