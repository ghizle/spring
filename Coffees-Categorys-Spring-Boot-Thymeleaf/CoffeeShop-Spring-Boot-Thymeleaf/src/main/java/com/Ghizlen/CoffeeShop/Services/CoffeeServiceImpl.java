package com.Ghizlen.CoffeeShop.Services;

import com.Ghizlen.CoffeeShop.Errors.CustomErrorMessage;
import com.Ghizlen.CoffeeShop.entities.Coffee;
import com.Ghizlen.CoffeeShop.repositories.CoffeeRepository;
import com.Ghizlen.CoffeeShop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoffeeServiceImpl implements CoffeeService {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> getCoffeeById(Long id) {
        return coffeeRepository.findById(id);
    }

       @Override
    public Coffee saveCoffee(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    @Override
    public ResponseEntity<?> updateCoffee(Long id, Coffee coffee) {
        Optional<Coffee> existingCoffee = coffeeRepository.findById(id);
        if (existingCoffee.isPresent()) {
            coffee.setIdCoffee(id);
            Coffee updatedCoffee = coffeeRepository.save(coffee);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCoffee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Coffee not found"));
        }
    }


    @Override
    public void deleteCoffeeById(Long id) {
        coffeeRepository.deleteById(id);
    }

    @Override
    public Page<Coffee> getAllCoffeesParPage(long page, long size) {
        return coffeeRepository.findAll(PageRequest.of((int) page, (int) size));
    }

    @Override
    public Page<Coffee> getAllCoffeesByCategoryParPage(long categoryId, long page, long size) {
        Pageable pageable = PageRequest.of((int) page, (int) size);
        List<Coffee> coffees = categoryRepository.findByCategory(categoryId);
        return new PageImpl<>(coffees, pageable, coffees.size());
    }

}

