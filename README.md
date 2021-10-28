# CRUDProducts

Navigate to the Controller.java in the package com.example.demo.controller, you'll find the below APIs

Following are the API endpoints and their respective documentation:

>RequestMethod.POST value="/authenticate"

This end-point authenticates a given user based on the authentication request being passed
as request body if the given username and password matches the one in database then a 
JWT token is created and returned.

>RequestMethod.POST value="/adduser"

This end-point registers a given user to the database. This end point is exempted from
spring security and doesn't require jwt being passed as a header

>RequestMethod.POST value="/addproduct"

This end-point adds a given product to the database. This end-point is secured by spring
security and requires a valid jwt being passed as a header in the request.

>RequestMethod.GET value="/getproducts"

This end-point retrieves all the products in the database. This end-point is exempted from
spring-security and doesn't require jwt to be passed as a header in the request.

>RequestMethod.GET value="/getproduct/{id}"

This end-point gets the product with a specific id. This end-point is secured by spring
security and requires a valid jwt being passed as a header.

>RequestMethod.PUT value="/product/{id}"

This end-point updates the product with a specific id. This end-point is secured by spring
security and requires a valid jwt being passed as a header.

>RequestMethod.DELETE value="/delete/{id}"

This end-point deletes the product with a specific id. This end-point is secured by spring
security and requires a valid jwt being passed as a header.
