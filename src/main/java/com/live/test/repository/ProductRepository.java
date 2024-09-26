package com.live.test.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.live.test.domain.EntityStatus;
import com.live.test.domain.Product;
import com.live.test.dto.ProductDTO;
import com.live.test.jpa.ProductJpa;
import com.live.test.queryparam.ProductQueryParam;

import lombok.val;

@Repository
public class ProductRepository {
  
  private final ProductJpa jpa;
  private final EntityManager em;
  
  public ProductRepository(ProductJpa jpa, EntityManager em) {
    this.jpa = jpa;
    this.em = em;
  }

  public Page<ProductDTO> findAll(Pageable pageable, ProductQueryParam param) {
    return jpa.findAll(pageable, param.getName(), param.getStatus());
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

  @SuppressWarnings("unchecked")
  public List<ProductDTO> findAllName() {
    DetachedCriteria criteria = DetachedCriteria.forClass(Product.class, Product.Fields.WithAlias.ALIAS);
    criteria.setProjection(Projections.projectionList()
        .add(Projections.property(Product.Fields.WithAlias.ID), ProductDTO.Fields.ID)
        .add(Projections.property(Product.Fields.WithAlias.NAME), ProductDTO.Fields.NAME));
    criteria.add(Restrictions.eq(Product.Fields.WithAlias.STATUS, EntityStatus.ACTIVE));
    criteria.addOrder(Order.asc(Product.Fields.WithAlias.NAME));
    criteria.setResultTransformer(Transformers.aliasToBean(ProductDTO.class));
    val executableCriteria = criteria.getExecutableCriteria((Session) em.getDelegate());
    return executableCriteria.list();
  }
}
