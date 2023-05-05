package com.Ghizlen.CoffeeShop.Services;



import com.Ghizlen.CoffeeShop.entities.Category;
import com.Ghizlen.CoffeeShop.entities.Coffee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategorys();

    Optional<Category> getCategoryById(Long id);

    Category createCategory(Category category);
    Category updateCategory(Category category);

    void deleteCategory(Long id);

    List<Coffee> getCoffeesById(Long id);

    Page<Category> getAllCategorysParPage(long page, long size);

}
