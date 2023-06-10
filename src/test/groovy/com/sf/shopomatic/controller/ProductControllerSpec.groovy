package com.sf.shopomatic.controller

import spock.lang.Specification

class ProductControllerSpec extends Specification{

     ProductController productController = new ProductController()

     def "should return product list"() {
         given:
            List<String> expectedProducts = Arrays.asList("Soccer Ball", "Stadium", "Thinking Cap")
         when:
            def products = productController.getProducts()
         then:
            Arrays.toString(products) == Arrays.toString(expectedProducts)

     }
}
