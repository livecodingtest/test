package com.live.test.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.live.test.domain.EntityStatus;
import com.live.test.domain.Product;
import com.live.test.dto.ProductDTO;

public interface ProductJpa extends JpaRepository<Product, Long> {

  @Query("select "
      + "new com.live.test.dto.ProductDTO(pr.id, pr.name, pr.description, pr.price, pr.status) "
      + "from Product pr "
      + "where (:name is null or lower(pr.name) like %:name% ) "
      + "and (:status is null or pr.status = :status ) ")
  Page<ProductDTO> findAll(Pageable pageable, @Param("name") String name, @Param("status") EntityStatus status);
  
  @Query("select case when count(p.id) > 0 then true else false end "
      + "from Product p where lower(p.name) = lower(:name) "
      + "and (:id is null or p.id <> :id )")
  Boolean existsByNameAndNotEqualsId(@Param("name") String name, @Param("id") Long id);
}
