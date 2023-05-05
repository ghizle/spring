package com.Ghizlen.CoffeeShop.controllers;


import com.Ghizlen.CoffeeShop.entities.Category;
import com.Ghizlen.CoffeeShop.entities.Coffee;
import com.Ghizlen.CoffeeShop.Services.CoffeeService;
import com.Ghizlen.CoffeeShop.Services.CategoryService;
import com.Ghizlen.CoffeeShop.Services.SharedService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CoffeeTemplatesController {
    @Autowired
    CoffeeService coffeeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SharedService sharedService;

    @GetMapping("/")
    public String home(ModelMap modelMap) {
        return "redirect:/coffees-list";
    }


    @GetMapping("/coffees-list")
    public String home(ModelMap modelMap,
                       @RequestParam (name="page", defaultValue = "0") int page,
                       @RequestParam (name="size", defaultValue = "5") int size,
                       HttpServletRequest request) {
        try {
            boolean isAdmin = sharedService.isAdmin();

            Page<Coffee> coffees = coffeeService.getAllCoffeesParPage(page, size);
            modelMap.addAttribute("coffees", coffees);
            modelMap.addAttribute("isAdmin", isAdmin);
            modelMap.addAttribute("pages", new int[coffees.getTotalPages()]);
            modelMap.addAttribute("currentPage", page);
            return "listeCoffees";

        }catch (Exception e){
            return "login";
        }
    }


    @GetMapping("/coffeesTemplate/delete/{id}")
    public String deleteCoffee(@PathVariable("id") Long id,
                               ModelMap model,
                               @RequestParam (name="page",defaultValue = "0") int page,
                               @RequestParam (name="size", defaultValue = "4") int size) {
        coffeeService.deleteCoffeeById(id);

        Page<Coffee> coffees = coffeeService.getAllCoffeesParPage(page,size);

        if(coffees.getNumberOfElements()==0) {
            page--;
        }
        model.addAttribute("coffees", coffees);
        model.addAttribute("pages", new int[coffees.getTotalPages()]);


        model.addAttribute("currentPage",page);
        model.addAttribute("size", size);

        return ("redirect:/coffees-list?page="+page+"&size="+size);
    }

    @GetMapping("/addCoffee")
    public String createCoffee(HttpServletRequest request, ModelMap modelMap) {
        try {
            modelMap.addAttribute("categorys", categoryService.getAllCategorys());
            return "createCoffee";
        }catch (Exception e){
            return "redirect:/coffees-list";
        }
    }



    @PostMapping("/saveCoffee")
    public String saveCoffee(@RequestParam String nom,
                             @RequestParam String email,
                             @RequestParam Long phoneNumber,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateNaissance,
                             @RequestParam Long idCategory,
                             RedirectAttributes redirectAttributes,
                             @RequestParam (name="size", defaultValue = "4") int size) {

        Optional<Category> category = categoryService.getCategoryById(idCategory);
        if (category.isPresent()) {
            Coffee coffee = new Coffee();
            coffee.setNomCoffee(nom);
            coffee.setPrixCoffee(email);
            coffee.setDateCreation(new Date());
            coffee.setCategory(category.get());

            coffeeService.saveCoffee(coffee);

            int totalCoffeesPages = (int) Math.floor((double) coffeeService.getAllCoffees().size() / size);
            int lastPage = totalCoffeesPages - 1;

            redirectAttributes.addAttribute("page", lastPage);
        }
        return "redirect:/coffees-list";
    }


    @GetMapping("/updateCoffee/{id}")
    public String showUpdateCoffeeForm(@PathVariable("id") long id,
                                       ModelMap model,
                                       HttpServletRequest request,
                                       @RequestParam (name="page",defaultValue = "0") int page,
                                       @RequestParam (name="size", defaultValue = "4") int size) {
        try {
            Coffee coffee = coffeeService.getCoffeeById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid coffee Id:" + id));


            model.addAttribute("currentPage",page);
            model.addAttribute("size", size);
            model.addAttribute("coffee", coffee);
            model.addAttribute("categorys", categoryService.getAllCategorys());
            return "editerCoffee"; // the name of the Thymeleaf template for updating a coffee

        } catch (Exception e) {
            return "login";
        }
    }


    @PostMapping("/updateCoffee/updateCoffee")
    public String updateCoffee(@RequestParam String idCoffee, @RequestParam String nom, @RequestParam long idCategory,
                               @RequestParam String email, @RequestParam String phoneNumber,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateNaissance,
                               @RequestParam (name="page",defaultValue = "0") int page,
                               @RequestParam (name="size", defaultValue = "4") int size) {

        Optional<Coffee>  coffeeToUpdate = coffeeService.getCoffeeById(Long.valueOf(idCoffee));
        if (coffeeToUpdate.isPresent()){
            coffeeToUpdate.get().setNomCoffee(nom);
            coffeeToUpdate.get().setPrixCoffee(email);
        }
        Optional<Category> category = categoryService.getCategoryById(idCategory);
        if(category.isPresent()) coffeeToUpdate.get().setCategory(category.get());

        coffeeService.updateCoffee( (Long.valueOf(idCoffee)), coffeeToUpdate.get());

        return "redirect:/coffees-list?page=" + page + "&size=" + size;
    }

    @GetMapping("/coffees-by-categorys")
    public String listCoffeesByCategory(ModelMap modelMap, @RequestParam(required = false) Long categoryId,
                                      @RequestParam(name="page", defaultValue="0") int page,
                                      @RequestParam(name="size", defaultValue="3") int size,
                                      HttpServletRequest request) {

        try {
            List<Category> categories = categoryService.getAllCategorys();
            modelMap.addAttribute("categorys", categories);

            if (categoryId != null) {
                modelMap.addAttribute("categoryId", categoryId);
                Page<Coffee> coffees = coffeeService.getAllCoffeesByCategoryParPage(categoryId, page, 3);
                modelMap.addAttribute("coffees", coffees);
                modelMap.addAttribute("pages", new int[coffees.getTotalPages()]);
                modelMap.addAttribute("currentPage", page);
            }
            return "listeCofeesByCategorys";
        } catch (Exception e) {
            return "login";
        }
    }

    @GetMapping("/updateCoffee/coffees-list")
    public String RedirectToList(){
        return "redirect:/coffees-list";
    }
}
