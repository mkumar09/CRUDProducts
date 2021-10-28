package com.example.demo.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

/** 
* This class represents the business logic for the CRUD operations on products
*/
@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;

	public ProductStatus addProduct(Product product) {
		
		ProductStatus productStatus;
		Optional<Product> isProductAlreadyPresent = productRepository.findById(product.getId());
		
		if ( isProductAlreadyPresent.isPresent())
			productStatus = ProductStatus.ALREADY_PRESENT;
		else
		{
			productRepository.save(product);
			productStatus = ProductStatus.INSERTED;
		}
		return productStatus;
		
	}

	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
    	productRepository.findAll()
    	.forEach(products::add);
    	return products;
	}

	public ProductStatus updateProduct(String id, Product product) {
		
		Optional<Product> productToUpdate = productRepository.findById(id);
		if ( productToUpdate.isPresent())
		{
			Product productData = productToUpdate.get();
			productData.setId(product.getId());
			productData.setName(product.getName());
			productData.setPrice(product.getPrice());
			productData.setStock(product.getStock());
			
			productRepository.save(productData);
			return ProductStatus.PRODUCT_UPDATED;
		}
		else
			return ProductStatus.NOT_PRESENT;
	}

	public ProductStatus deleteProduct(String id) {
		
		Optional<Product> productToDelete = productRepository.findById(id);
		if ( productToDelete.isPresent() )
		{
			productRepository.deleteById(id);
			return ProductStatus.PRODUCT_DELETED;
		}
		else
			return ProductStatus.NOT_PRESENT;
	}

	public Product getProductById(String id) {
		
		Optional<Product> product = productRepository.findById(id);
		
		if ( product.isPresent() )
			return product.get();
		else
			return null;
	}
	

}
