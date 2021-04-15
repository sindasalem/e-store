package com.quest.etna.controller;

import com.quest.etna.dto.ProductFileDto;
import com.quest.etna.model.FileStorageProperties;
import com.quest.etna.model.Product;
import com.quest.etna.model.UserDetails;
import com.quest.etna.service.FileStorageService;
import com.quest.etna.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    public ProductService productService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/Users/mac/Desktop/downloadFile")
                .path(fileName)
                .toUriString();

        FileStorageProperties fileStorageProperties = new FileStorageProperties(fileName,
                file.getContentType(), file.getSize());
        fileStorageService.saveFile(fileStorageProperties);

        product.setFileStorageProperties(fileStorageProperties);
        product = productService.save(product);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);

    }
}
