package com.sf.shopomatic.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    public List<String> getProducts() {
        return Arrays.asList("Soccer Ball", "Stadium", "Thinking Cap", "Coffee Cup", "New Product");
    }
}
