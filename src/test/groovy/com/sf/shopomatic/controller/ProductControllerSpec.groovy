package com.sf.shopomatic.controller

import com.sf.shopomatic.service.ProductService
import org.springframework.ui.Model
import spock.lang.Specification

class ProductControllerSpec extends Specification{

     ProductService productService = Mock()
    ProductRestController productController

     def "should return product list"() {
         given:
             productController = new ProductRestController(
                     productService: productService
             )
            Model model = Mock()
         when:
            def products = productController.getProducts()
         then:
            1 * productService.getProducts() >> _
            products == []

     }
}
