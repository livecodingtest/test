package com.live.test.controller;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.live.test.dto.ProductDTO;
import com.live.test.queryparam.ProductQueryParam;
import com.live.test.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

  private ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public Page<ProductDTO> findAll(
      @PageableDefault(size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @Valid ProductQueryParam param) {
    return service.findAll(pageable, firstNonNull(param, new ProductQueryParam()));
  }

  @GetMapping("/{id}")
  public ProductDTO findById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  public ProductDTO save(@Valid @RequestBody ProductDTO productDTO) {
    return service.save(productDTO);
  }

  @PatchMapping("/{id}")
  public ProductDTO update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
    return service.update(id, productDTO);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }

  @PatchMapping("/{id}/reactivate")
  public void reactivate(@PathVariable Long id) {
    service.reactivate(id);
  }
  
  @GetMapping("/name")
  public List<ProductDTO> findAllName() {
    return service.findAllName();
  }

}