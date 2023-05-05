package com.Ghizlen.CoffeeShop.Services;

import com.Ghizlen.CoffeeShop.entities.Coffee;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CoffeeService {

    List<Coffee> getAllCoffees();

    Optional<Coffee> getCoffeeById(Long id);

    Coffee saveCoffee(Coffee coffee);
    ResponseEntity<?> updateCoffee(Long id, Coffee coffee);

    void deleteCoffeeById(Long id);

    Page<Coffee> getAllCoffeesParPage(long page, long size);

    Page<Coffee> getAllCoffeesByCategoryParPage(long regionId, long page, long size);
}

