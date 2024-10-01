package com.live.test.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.live.test.domain.Product;

public interface ProductJpa extends JpaRepository<Product, Long> {

  @Query("select case when count(p.id) > 0 then true else false end "
      + "from Product p where lower(p.name) = lower(:name) "
      + "and (:id is null or p.id <> :id )")
  Boolean existsByNameAndNotEqualsId(@Param("name") String name, @Param("id") Long id);
}
