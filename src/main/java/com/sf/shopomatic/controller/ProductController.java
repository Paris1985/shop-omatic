package com.sf.shopomatic.controller;

import com.sf.shopomatic.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping
    public String getProducts(Model model) {
        List<String> products = productService.getProducts();
        model.addAttribute("products", products);
        return "home";
    }
}
