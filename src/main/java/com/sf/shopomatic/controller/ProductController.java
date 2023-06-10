package com.sf.shopomatic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public List<String> getProducts() {
        return Arrays.asList("Soccer Ball", "Stadium", "Thinking Cap");
    }
}
