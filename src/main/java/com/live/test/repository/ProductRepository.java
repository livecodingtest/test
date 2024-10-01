package com.live.test.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.live.test.domain.Product;
import com.live.test.dto.ProductDTO;
import com.live.test.jpa.ProductJpa;
import com.live.test.queryparam.ProductQueryParam;

@Repository
public class ProductRepository {
  
  private final ProductJpa jpa;
  private final EntityManager em;
  
  public ProductRepository(ProductJpa jpa, EntityManager em) {
    this.jpa = jpa;
    this.em = em;
  }

  public Page<ProductDTO> findAll(Pageable pageable, ProductQueryParam param) {
    return Page.empty();
  }

  public Optional<Product> findById(Long id) {
    return jpa.findById(id);
  }

  public Product save(Product product) {
    return jpa.save(product);
  }

  public Boolean existsByNameAndNotEqualsId(String name, Long id) {
    return jpa.existsByNameAndNotEqualsId(name, id);
  }

  public List<ProductDTO> findAllName() {
    return Collections.emptyList();
  }
}
