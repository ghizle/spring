package com.Ghizlen.CoffeeShop.controllers;


import com.Ghizlen.CoffeeShop.Errors.CustomErrorMessage;
import com.Ghizlen.CoffeeShop.Services.CategoryServiceImpl;
import com.Ghizlen.CoffeeShop.entities.Category;
import com.Ghizlen.CoffeeShop.entities.Coffee;
import com.Ghizlen.CoffeeShop.Services.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping()
    public ResponseEntity<?> getAllCategorys() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategorys());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Optional<Category> Category = categoryService.getCategoryById(id);
            if (Category.isPresent()) return ResponseEntity.ok(Category.get());
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try{
            Category savedCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                category.setIdCategory(id);
                Category updatedCategory = categoryService.updateCategory(category);
                return ResponseEntity.ok(updatedCategory);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            Optional<Category> existingCategory = categoryService.getCategoryById(id);
            if (existingCategory.isPresent()) {
                categoryService.deleteCategory(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingCategory);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/{id}/coffees")
    public ResponseEntity<?> getCoffeesByCategory(@PathVariable Long id) {
        try {
            Optional<Category> Category = categoryService.getCategoryById(id);
            if (Category.isPresent()) return ResponseEntity.ok(categoryService.getCoffeesById(id));
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PostMapping("/{id}/coffees")
    public ResponseEntity<?> createCoffeeForCategory(@PathVariable Long id, @RequestBody Coffee coffee) {
        try {
            Optional<Category> Category = categoryService.getCategoryById(id);
            if (Category.isPresent()) {
                coffee.setCategory(Category.get());
                Coffee savedCoffee = coffeeService.saveCoffee(coffee);
                Category.get().addCoffee(savedCoffee);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedCoffee);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{id}/coffees")
    public ResponseEntity<?> updateCoffeeForCategory(@PathVariable Long id, @RequestBody Coffee coffee) {
        try {
            Optional<Category> Category = categoryService.getCategoryById(id);
            if (Category.isPresent()) {
                coffee.getCategory().removeCoffee(coffee);
                coffee.setCategory(Category.get());
                Coffee savedCoffee = coffeeService.saveCoffee(coffee);
                Category.get().addCoffee(savedCoffee);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedCoffee);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @GetMapping("/coffees-by-Category/{CategoryId}")
    public ResponseEntity<?> getAllCoffeesByCategoryId(@PathVariable Long CategoryId) {
        try{
            Optional<Category> Category = categoryService.getCategoryById(CategoryId);
            if (Category.isPresent()) {
                List<Coffee> coffees = categoryService.getCoffeesById(CategoryId);
                return ResponseEntity.status(HttpStatus.CREATED).body(coffees);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("Category not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }
}
