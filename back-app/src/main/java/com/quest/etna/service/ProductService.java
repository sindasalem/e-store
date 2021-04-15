package com.quest.etna.service;

import com.quest.etna.model.Product;
import com.quest.etna.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    // productRepository constructor injection

    @Autowired
    ProductRepository productRepository;

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(long id) throws Exception {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Product not found!"));
    }


    public Product save(Product product) {
        return productRepository.save(product);
    }
}
