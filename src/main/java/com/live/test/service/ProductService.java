package com.live.test.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.live.test.config.exception.ApiRuntimeException;
import com.live.test.config.i18n.Messages;
import com.live.test.config.i18n.Translator;
import com.live.test.domain.EntityStatus;
import com.live.test.domain.Product;
import com.live.test.dto.ProductDTO;
import com.live.test.queryparam.ProductQueryParam;
import com.live.test.repository.ProductRepository;

import lombok.val;

@Service
public class ProductService {
    
  private ProductRepository repository;
  private Translator translator;

  public ProductService(ProductRepository repository, Translator translator) {
    this.repository = repository;
    this.translator = translator;
  }

  public Page<ProductDTO> findAll(Pageable pageable, ProductQueryParam param) {
    return repository.findAll(pageable, param);
  }

  public ProductDTO findById(Long id) {
    val product = findEntityById(id);
    return convertToProductDTO(product);
  }

  private Product findEntityById(Long id) {
    return repository.findById(id).orElseThrow(
        () -> new ApiRuntimeException(Messages.ERROR_PRODUCT_NOT_FOUND, translator, HttpStatus.BAD_REQUEST));
  }

  @Transactional
  public ProductDTO save(ProductDTO productDTO) {
    validatesUniqueness(null, productDTO.getName());
    val product = convertToEntity(productDTO);
    val savedProduct = repository.save(product);
    return convertToProductDTO(savedProduct);
  }

  @Transactional
  public ProductDTO update(Long id, ProductDTO productDTO) {
    val product = findEntityById(id);
    if (!EntityStatus.ACTIVE.equals(product.getStatus())) {
      throw new ApiRuntimeException(Messages.ERROR_PRODUCT_IS_NOT_ACTIVE, translator, HttpStatus.PRECONDITION_FAILED);
    }
    validatesUniqueness(id, productDTO.getName());
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    val updatedProduct = repository.save(product);
    return convertToProductDTO(updatedProduct);
  }
  
  private void validatesUniqueness(Long id, String name) {
    if (Boolean.TRUE.equals(repository.existsByNameAndNotEqualsId(name, id))) {
      throw new ApiRuntimeException(Messages.ERROR_PRODUCT_ALREADY_EXISTS, translator, HttpStatus.PRECONDITION_FAILED,
          name);
    }
  }

  @Transactional
  public void delete(Long id) {
    val product = findEntityById(id);
    if (!EntityStatus.ACTIVE.equals(product.getStatus())) {
      throw new ApiRuntimeException(Messages.ERROR_PRODUCT_IS_NOT_ACTIVE, translator, HttpStatus.PRECONDITION_FAILED);
    }
    product.setStatus(EntityStatus.INACTIVE);    
    repository.save(product);
  }
  
  @Transactional
  public void reactivate(Long id) {
    val product = findEntityById(id);
    if (!EntityStatus.INACTIVE.equals(product.getStatus())) {
      throw new ApiRuntimeException(Messages.ERROR_PRODUCT_IS_NOT_INACTIVE, translator, HttpStatus.PRECONDITION_FAILED);
    }
    product.setStatus(EntityStatus.ACTIVE);
    product.setDeletedByUser(null);
    repository.save(product);
  }
  
  public List<ProductDTO> findAllName() {
    return repository.findAllName();
  }

  private Product convertToEntity(ProductDTO productDTO) {
    val product = new Product();
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());   
    return product;
  }

  private ProductDTO convertToProductDTO(Product product) {
    val productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    productDTO.setName(product.getName());
    productDTO.setDescription(product.getDescription());
    productDTO.setPrice(product.getPrice());
    productDTO.setStatus(product.getStatus());
    return productDTO;
  }

}