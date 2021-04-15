package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    private Double price;

    public FileStorageProperties getFileStorageProperties() {
        return fileStorageProperties;
    }

    public void setFileStorageProperties(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(targetEntity = FileStorageProperties.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileStorageProperties fileStorageProperties;

    public Product() {
    }

    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}