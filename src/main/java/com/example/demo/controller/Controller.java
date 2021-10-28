package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.models.AuthenticationRequest;
import com.example.demo.models.AuthenticationResponse;
import com.example.demo.utility.JwtUtil;
import com.example.demo.utility.MyUserDetailsService;
import com.example.demo.utility.ProductService;
import com.example.demo.utility.ProductStatus;

@RestController
public class Controller {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	/** 
	* This end-point authenticates a given user based on the authentication request being passed
	* as request body if the given username and password matches the one in database then a
	* JWT token is created and returned.
	* 
	* @param AuthenticationRequest authentication request
	* 
	* @return ResponseEntity.ok( T body )
	*/
	@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			}
		catch(BadCredentialsException e )
		{
			throw new Exception("Incorrect Username or Password", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	/** 
	* This end-point registers a given user to the database. This end point is exempted from
	* spring security and doesn't require jwt being passed as a header
	* 
	* @param User user
	* 
	* @return ResponseEntity.ok( T body )
	*/
	@RequestMapping(value="/adduser", method = RequestMethod.POST)
	public ResponseEntity<?> addNewUser(@RequestBody User user)
	{
		userDetailsService.addUser(user);
		
		return ResponseEntity.ok("User added");
	}
	
	/** 
	* This end-point adds a given product to the database. This end-point is secured by spring
	* security and requires a valid jwt being passed as a header.
	* 
	* @param Product product
	* 
	* @return ResponseEntity.ok(T body)
	*/
	@RequestMapping(value="/addproduct", method = RequestMethod.POST)
	public ResponseEntity<?> addNewProduct(@RequestBody Product product)
	{
		ProductStatus productStatus = productService.addProduct(product);
		
		if ( productStatus.equals(ProductStatus.INSERTED))
			return ResponseEntity.ok("Product added");
		else
			return ResponseEntity.ok("Product already present in the Database. Please insert some other product");
	}
	
	/** 
	* This end-point retrieves all the products in the database. This end-point is exempted from
	* spring-security and doesn't require jwt to be passed as a header
	* 
	* @return list of product
	*/
	@RequestMapping(value="/getproducts", method = RequestMethod.GET)
	public List<Product> getProducts()
	{
		return productService.getProducts();
	}
	
	/** 
	* This end-point gets the product with a specific id. This end-point is secured by spring
	* security and requires a valid jwt being passed as a header.
	* 
	* @param String id
	* 
	* @return ResponseEntity.ok(T body)
	*/
	@RequestMapping(value="/getproduct/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProductById(@PathVariable String id)
	{
		Product product = productService.getProductById(id);
		if ( product != null )
			return ResponseEntity.ok(product);
		else
			return ResponseEntity.ok("Product with given id is not present");
	}
	
	/** 
	* This end-point updates the product with a specific id. This end-point is secured by spring
	* security and requires a valid jwt being passed as a header.
	* 
	* @param String id, Product product
	* 
	* @return ResponseEntity.ok(T body)
	*/
	@RequestMapping(value="/product/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product product)
	{
		ProductStatus productStatus = productService.updateProduct(id,product);
		if ( productStatus.equals(ProductStatus.PRODUCT_UPDATED))
			return ResponseEntity.ok("Product Updated");
		else
			return ResponseEntity.ok("Product with given id not present");
	}
	
	/** 
	* This end-point deletes the product with a specific id. This end-point is secured by spring
	* security and requires a valid jwt being passed as a header.
	* 
	* @param String id
	* 
	* @return ResponseEntity.ok(T body)
	*/
	@RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProduct(@PathVariable String id)
	{
		ProductStatus productStatus = productService.deleteProduct(id);
		
		if ( productStatus.equals(ProductStatus.PRODUCT_DELETED))
			return ResponseEntity.ok("Product Deleted");
		else
			return ResponseEntity.ok("Product with given id not present");
	}

	
	

}
