package com.example.demo.utility;

public enum ProductStatus {
	
	//denotes that the product already exists in the DB
	ALREADY_PRESENT,
	
	//denotes that the product is inserted
	INSERTED,
	
	//denotes that the given product was updated
	PRODUCT_UPDATED,
	
	//denotes that product was not present
	NOT_PRESENT,
	
	// denotes that product was deleted
	PRODUCT_DELETED

}
