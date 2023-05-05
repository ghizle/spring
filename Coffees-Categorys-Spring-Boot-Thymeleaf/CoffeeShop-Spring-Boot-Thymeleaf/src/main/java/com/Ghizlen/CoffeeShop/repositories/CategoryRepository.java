package com.Ghizlen.CoffeeShop.repositories;

import com.Ghizlen.CoffeeShop.entities.Coffee;
import com.Ghizlen.CoffeeShop.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Coffee c WHERE c.category.idCategory = :categoryId")
    List<Coffee> findByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM Coffee c WHERE c.category.idCategory = :categoryId")
    Page<Coffee> findCoffeesByCategoryId(@Param("categoryId") long categoryId, Pageable pageable);

}
