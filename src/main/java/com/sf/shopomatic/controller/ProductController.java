package com.sf.shopomatic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", Arrays.asList("Soccer Ball", "Stadium", "Thinking Cap", "Coffee Cup"));
        return "home";
    }
}
