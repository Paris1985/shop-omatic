package com.sf.shopomatic.controller

import org.springframework.ui.Model
import spock.lang.Specification

class ProductControllerSpec extends Specification{

     ProductController productController = new ProductController()

     def "should return product list"() {
         given:
            Model model = Mock()
         when:
            def products = productController.getProducts(model)
         then:
            products == "home"

     }
}
