package ru.returnonintelligence.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.returnonintelligence.catalog.entity.Category;
import ru.returnonintelligence.catalog.entity.Product;
import ru.returnonintelligence.catalog.service.MainService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private MainService service;

    @GetMapping("/")
    public String MainPage1(Model model) {
        Iterable<Product> products = service.findAllProducts();
        model.addAttribute("products", products);
        return "greeting";
    }

    @GetMapping("/greeting")
    public String MainPage2(Model model) {
        Iterable<Product> products = service.findAllProducts();
        model.addAttribute("products", products);
        return "greeting";
    }

    @GetMapping("/addCategory")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/addCategory")
    public String addSubmit(@ModelAttribute Category category, Model model) {
        try {
            service.save(category);
            model.addAttribute("message", "Success");
        } catch (Exception e) { //temporary crutch
            model.addAttribute("message", "Already exist");
            return "addCategory";
        }
        return "addCategory";
    }

    @GetMapping("/addProduct")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", service.findAllCategories());
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String createSubmit(@ModelAttribute Product product, @RequestParam(value = "categoryCheckbox", required = false) Long[] categories) {
        service.save(product, categories);
        return "redirect:greeting";
    }

    @GetMapping("/editProduct")
    public String editForm(@RequestParam("id") Long productID, Model model, RedirectAttributes redirectAttributes) {
        Product product = service.getProductById(productID);
        redirectAttributes.addAttribute("id", productID);
        model.addAttribute("product", product);
        Set<Category> otherCategories = new HashSet<>();
        for (Category category : service.findAllCategories()) {
            otherCategories.add(category);
        }
        otherCategories.removeAll(product.getCategories());
        model.addAttribute("otherCategories", otherCategories);
        model.addAttribute("productCategories", product.getCategories());
        System.out.println(productID);
        return "editProduct";
    }

    @PostMapping("/editProduct")
    public String editSubmit(@ModelAttribute Product product,
                             @RequestParam(value = "otherCategories", required = false) Long[] otherCategories,
                             @RequestParam(value = "productCategories", required = false) Long[] productCategories) {
        Long[] categories = service.concat(productCategories, otherCategories);
        service.save(product, categories);
        return "redirect:greeting";
    }

    @GetMapping("/deleteProduct")
    public String delete(@RequestParam("id") Long productID) {
        service.deleteProduct(productID);

        return "redirect:greeting";
    }

    @PostMapping("/findProduct")
    public String searchSubmit(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "category", required = false) String category, Model model) {
        if (name == "" && category != "") {
            Iterable<Product> products = service.fingProductsByCategory(category);
            model.addAttribute("products", products);
            return "greeting";
        } else if (name != "" && category == "") {
            Iterable<Product> products = service.findProductsLikeName(name);
            model.addAttribute("products", products);
            return "greeting";
        } else if (name != "" && category != ""){
            Iterable<Product> products = service.merge(service.findProductsLikeName(name), service.fingProductsByCategory(category));
            model.addAttribute("products", products);
            return "greeting";
        } else {
            return "redirect:greeting";
        }
    }
}
