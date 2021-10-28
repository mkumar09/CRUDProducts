package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	
	Optional<Product> findById(String id);

}
