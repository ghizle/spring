package com.Ghizlen.CoffeeShop.controllers;


import com.Ghizlen.CoffeeShop.Errors.CustomErrorMessage;
import com.Ghizlen.CoffeeShop.entities.Coffee;
import com.Ghizlen.CoffeeShop.entities.Category;
import com.Ghizlen.CoffeeShop.repositories.CategoryRepository;
import com.Ghizlen.CoffeeShop.Services.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Coffees")
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<?> getAllCoffees() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(coffeeService.getAllCoffees());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoffeeById(@PathVariable Long id) {
        try{
            Optional<Coffee> coffee = coffeeService.getCoffeeById(id);
            if (coffee.isPresent()) return ResponseEntity.ok(coffee.get());
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Coffee not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createCoffee(@RequestBody Coffee coffee) {
        try{
            Coffee savedCoffee = coffeeService.saveCoffee(coffee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCoffee);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{CoffeeID}/{categoryID}")
    public ResponseEntity<?> updateCoffee(@PathVariable Long CoffeeID, @PathVariable Long categoryID, @RequestBody Coffee coffee) {
        try{
            Optional<Category> category = categoryRepository.findById(categoryID);
            if (category.isPresent()) {
                    coffee.setCategory(category.get());
                    coffeeService.updateCoffee(CoffeeID, coffee);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(coffee);
                } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoffee(@PathVariable Long id) {
        try {
            Optional<Coffee> existingCoffee = coffeeService.getCoffeeById(id);
            if (existingCoffee.isPresent()) {
                coffeeService.deleteCoffeeById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingCoffee);
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Coffee not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }
}
