package com.Ghizlen.CoffeeShop.controllers;


import com.Ghizlen.CoffeeShop.entities.Category;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Date;


@Controller
public class CategoryTemplatesController implements WebMvcConfigurer {

    @Autowired
    CategoryService categoryService;

    @Autowired
    SharedService sharedService;

    @GetMapping("/listeCategorys")
    public String home(ModelMap modelMap,
                       @RequestParam (name="page", defaultValue = "0") int page,
                       @RequestParam (name="size", defaultValue = "5") int size,
                       HttpServletRequest request) {

        try {
            boolean isAdmin = sharedService.isAdmin();

            Page<Category> categorys = categoryService.getAllCategorysParPage(page, size);
            modelMap.addAttribute("categorys", categorys);
            modelMap.addAttribute("isAdmin", isAdmin);
            modelMap.addAttribute("pages", new int[categorys.getTotalPages()]);
            modelMap.addAttribute("currentPage", page);
            modelMap.addAttribute("size", size);
            return "listeCategorys";
        } catch (Exception e) {
            return "login";
        }
    }


    @GetMapping("/categorysTemplate/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id,
                               ModelMap model,
                               @RequestParam (name="page",defaultValue = "0") int page,
                               @RequestParam (name="size", defaultValue = "4") int size) {
        categoryService.deleteCategory(id);

        Page<Category> categorys = categoryService.getAllCategorysParPage(page,size);

        if(categorys.getNumberOfElements()==0) {
            page--;
        }
        model.addAttribute("categorys", categorys);
        model.addAttribute("pages", new int[categorys.getTotalPages()]);


        model.addAttribute("currentPage",page);
        model.addAttribute("size", size);

        return ("redirect:/listeCategorys?page="+page+"&size="+size);
    }

    @GetMapping("/addCategory")
    public String createCategory(HttpServletRequest request) {

        try {
            return "createCategory";
        } catch (Exception e) {
            return "login";
        }
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@RequestParam String nom,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateCreation,
                             RedirectAttributes redirectAttributes,
                             @RequestParam (name="size", defaultValue = "4") int size) {


        Category category = new Category();
        category.setNom(nom);
        category.setDateCreation(dateCreation);
        categoryService.createCategory(category);


        int totalEquipePages = (int) Math.floor((double) categoryService.getAllCategorys().size() / size);
        int lastPage = totalEquipePages - 1;

        redirectAttributes.addAttribute("page", lastPage);
        return "redirect:/listeCategorys";
    }

    @GetMapping("/update")
    public String updateCategory(@RequestParam("idCategory") String idCategoryString,
                               @RequestParam("nom") String nom,
                               ModelMap model,
                               @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                               @RequestParam (name="page",defaultValue = "0") int page,
                               @RequestParam (name="size", defaultValue = "4") int size) {

        long idCategory = Long.parseLong(idCategoryString);
        Category category = new Category();
        category.setIdCategory(idCategory);
        category.setNom(nom);
        category.setDateCreation(date);

        Page<Category> categorys = categoryService.getAllCategorysParPage(page,size);
        model.addAttribute("pages", new int[categorys.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("size", size);

        categoryService.updateCategory(category);
        return "redirect:/listeCategorys?page=" + page + "&size=" + size;
    }

    @GetMapping("/updateCategory")
    public String ShowToUpdateCategoryForm(@RequestParam("id") long id, ModelMap model,
                                         HttpServletRequest request,
                                         @RequestParam (name="page",defaultValue = "0") int page,
                                         @RequestParam (name="size", defaultValue = "4") int size) {
        try {
            Category category = categoryService.getCategoryById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid coffee Id:" + id));

            model.addAttribute("currentPage",page);
            model.addAttribute("size", size);
            model.addAttribute("category", category);
            return "editerCategory";
        } catch (Exception e) {
            return "login";
        }
    }

    @GetMapping("/updateCategory/listeCategorys")
    public String RedirectToList(){
        return "redirect:/listeCategorys";
    }

}
