package com.live.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.live.test.config.exception.ApiRuntimeException;
import com.live.test.config.i18n.Translator;
import com.live.test.domain.EntityStatus;
import com.live.test.domain.Product;
import com.live.test.dto.ProductDTO;
import com.live.test.queryparam.ProductQueryParam;
import com.live.test.repository.ProductRepository;

import lombok.val;

public class ProductServiceTest {

  private ProductService service;
  private ProductRepository repository;
  private Translator translator;

  @Before
  public void init() {
    this.repository = mock(ProductRepository.class);
    this.translator = mock(Translator.class);
    this.service = new ProductService(repository, translator);
  }

  @Test
  public void testFindAll_Success() {
    val page = new PageImpl<>(Arrays.asList(buildProductDTO()));
    when(repository.findAll(PageRequest.of(0, 30), new ProductQueryParam())).thenReturn(page);

    val result = service.findAll(PageRequest.of(0, 30), new ProductQueryParam());
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    verify(repository, times(1)).findAll(any(Pageable.class), any(ProductQueryParam.class));
  }

  @Test
  public void testFindById_Success() {
    val product = buildProduct(EntityStatus.ACTIVE);
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    val result = service.findById(NumberUtils.LONG_ONE);
    assertNotNull(result);
    assertEquals(NumberUtils.LONG_ONE, result.getId());
    assertEquals(product.getName(), result.getName());
    verify(repository, times(1)).findById(anyLong());
  }

  private Product buildProduct(EntityStatus status) {
    val product = new Product();
    product.setId(NumberUtils.LONG_ONE);
    product.setName("Nome do Produto");
    product.setDescription("Descricao do Produto");
    product.setPrice(BigDecimal.TEN);
    product.setStatus(status);
    return product;
  }

  @Test(expected = ApiRuntimeException.class)
  public void testFindById_NotFound() {
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.empty());
    
    service.findById(NumberUtils.LONG_ONE);    
  }

  @Test
  public void testSave_Success() {
    val productDTO = buildProductDTO();
    when(repository.save(any(Product.class))).thenReturn(buildProduct(EntityStatus.ACTIVE));
    when(repository.existsByNameAndNotEqualsId(productDTO.getName(), null)).thenReturn(Boolean.FALSE);

    val result = service.save(productDTO);
    assertNotNull(result);
    assertEquals(productDTO.getName(), result.getName());
    verify(repository, times(1)).save(any(Product.class));
  }

  @Test(expected = ApiRuntimeException.class)
  public void testSave_ProductAlreadyExists() {
    when(repository.save(any(Product.class))).thenReturn(buildProduct(EntityStatus.ACTIVE));
    val productDTO = buildProductDTO();
    when(repository.existsByNameAndNotEqualsId(productDTO.getName(), null)).thenReturn(Boolean.TRUE);

    service.save(productDTO);    
  }

  @Test
  public void testUpdate_Success() {
    val product = buildProduct(EntityStatus.ACTIVE);
    val productDTO = buildProductDTO();
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));
    when(repository.existsByNameAndNotEqualsId(productDTO.getName(), NumberUtils.LONG_ONE)).thenReturn(Boolean.FALSE);
    when(repository.save(any(Product.class))).thenReturn(product);

    val result = service.update(NumberUtils.LONG_ONE, productDTO);
    assertNotNull(result);
    assertEquals(product.getName(), result.getName());
    verify(repository, times(1)).findById(NumberUtils.LONG_ONE);
    verify(repository, times(1)).existsByNameAndNotEqualsId(productDTO.getName(), NumberUtils.LONG_ONE);
    verify(repository, times(1)).save(product);
  }

  @Test(expected = ApiRuntimeException.class)
  public void testUpdate_ProductNotActive() {
    val product = buildProduct(EntityStatus.INACTIVE);
    val productDTO = buildProductDTO();
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    service.update(NumberUtils.LONG_ONE, productDTO);
  }

  @Test(expected = ApiRuntimeException.class)
  public void testUpdate_ProductAlreadyExists() {
    val product = buildProduct(EntityStatus.ACTIVE);
    val productDTO = buildProductDTO();
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));
    when(repository.existsByNameAndNotEqualsId(productDTO.getName(), NumberUtils.LONG_ONE)).thenReturn(Boolean.TRUE);

    service.update(NumberUtils.LONG_ONE, productDTO);
  }

  @Test
  public void testDelete_Success() {
    val product = buildProduct(EntityStatus.ACTIVE);
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    service.delete(NumberUtils.LONG_ONE);
    val productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(repository).save(productCaptor.capture());
    val savedProduct = productCaptor.getValue();
    verify(repository, times(1)).findById(NumberUtils.LONG_ONE);
    verify(repository, times(1)).save(product);
    assertEquals(EntityStatus.INACTIVE, savedProduct.getStatus());
  }

  @Test(expected = ApiRuntimeException.class)
  public void testDelete_ProductNotActive() {
    val product = buildProduct(EntityStatus.INACTIVE);
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    service.delete(NumberUtils.LONG_ONE);
  }

  @Test
  public void testReactivate_Success() {
    val product = buildProduct(EntityStatus.INACTIVE);
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    service.reactivate(NumberUtils.LONG_ONE);
    val productCaptor = ArgumentCaptor.forClass(Product.class);
    verify(repository).save(productCaptor.capture());
    val savedProduct = productCaptor.getValue();
    verify(repository, times(1)).findById(NumberUtils.LONG_ONE);
    verify(repository, times(1)).save(product);
    assertEquals(EntityStatus.ACTIVE, savedProduct.getStatus());
  }

  @Test(expected = ApiRuntimeException.class)
  public void testReactivate_ProductNotInactive() {
    val product = buildProduct(EntityStatus.ACTIVE);
    when(repository.findById(NumberUtils.LONG_ONE)).thenReturn(Optional.of(product));

    service.reactivate(NumberUtils.LONG_ONE);
  }

  @Test
  public void testFindAllName_Success() {
    val products = Arrays.asList(buildProductDTO());
    when(repository.findAllName()).thenReturn(products);

    val result = service.findAllName();
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository, times(1)).findAllName();
  }

  private ProductDTO buildProductDTO() {
    val productDTO = new ProductDTO();
    productDTO.setName("Nome do Produto");
    productDTO.setDescription("Descricao do Produto");
    productDTO.setPrice(BigDecimal.TEN);
    return productDTO;
  }

}
