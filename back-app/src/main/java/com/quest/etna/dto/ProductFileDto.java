package com.quest.etna.dto;

import com.quest.etna.model.FileStorageProperties;
import com.quest.etna.model.Product;

public class ProductFileDto {
    private Product product;
    private FileStorageProperties fileStorageProperties;

    public ProductFileDto(Product product, FileStorageProperties fileStorageProperties) {
        this.product = product;
        this.fileStorageProperties = fileStorageProperties;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public FileStorageProperties getFileStorageProperties() {
        return fileStorageProperties;
    }

    public void setFileStorageProperties(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }
}
