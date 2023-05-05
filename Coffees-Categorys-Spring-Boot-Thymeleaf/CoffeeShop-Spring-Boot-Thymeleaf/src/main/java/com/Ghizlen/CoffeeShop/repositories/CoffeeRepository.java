package com.Ghizlen.CoffeeShop.repositories;

import com.Ghizlen.CoffeeShop.entities.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
