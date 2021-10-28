package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ControllerTest {
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ProductRepository productRepository;
	
	private User User1;
	private User User2;
	
	private Product product1;
	private Product product2;
	
	@Autowired
	 ObjectMapper objectMapper;
	
	@Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
	
	public ControllerTest() {
		this.User1 = new User("foo", "foo");
		this.User2 = new User("abcd", "pqrs");
		
		this.product1 = new Product("1510","Jeans",(float) 1899.99,15);
		this.product2 = new Product("1697","T-Shirt",(float) 901.75,35);

	}
	
	@Test
	public void addUserTest() throws Exception
	{
		String jsonRequest = objectMapper.writeValueAsString(User1);
		MvcResult mvcResult = this.mockMvc
				.perform(post("/adduser").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("User added", resultContent);
		
	}
	
	@Test
	public void authenticateUser() throws Exception
	{
		 when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		String jsonRequest = objectMapper.writeValueAsString(User1);
		this.mockMvc
				.perform(post("/authenticate").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

	}
	
	@Test
	public void addProductAlreadyExistsTest() throws Exception

	{ 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.of(product1);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);
		 
		String authorization = "Authorization";
		String jsonRequest = objectMapper.writeValueAsString(product1);
		this.mockMvc
				.perform(post("/addproduct").content(jsonRequest).header(authorization,"Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

	}
	
	@Test
	public void addProductTest() throws Exception

	{ 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.ofNullable(null);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);
		 
		String authorization = "Authorization";
		String jsonRequest = objectMapper.writeValueAsString(product1);
		this.mockMvc
				.perform(post("/addproduct").content(jsonRequest).header(authorization,"Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();

	}
	
	@Test
	public void getProductsTest() throws Exception
    { 
		this.mockMvc
				.perform(get("/getproducts").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void getProductByIdTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.of(product1);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		
		this.mockMvc
		.perform(get("/getproduct/{id}",product1.getId()).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		
	}
	
	@Test
	public void getProductByIdNotPresentTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.ofNullable(null);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		
		MvcResult mvcResult = this.mockMvc
		.perform(get("/getproduct/{id}",product1.getId()).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("Product with given id is not present", resultContent);
	}
	
	
	@Test
	public void updateProductTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.of(product1);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		String jsonRequest = objectMapper.writeValueAsString(product2);
		MvcResult mvcResult = this.mockMvc
		.perform(put("/product/{id}",product1.getId()).content(jsonRequest).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("Product Updated", resultContent);
	}
	
	@Test
	public void updateProductWhenProductWithGivenIdNotPresentTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.ofNullable(null);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		String jsonRequest = objectMapper.writeValueAsString(product2);
		MvcResult mvcResult = this.mockMvc
		.perform(put("/product/{id}",product1.getId()).content(jsonRequest).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("Product with given id not present", resultContent);
	}
	
	@Test
	public void deleteProductTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.of(product1);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		MvcResult mvcResult = this.mockMvc
		.perform(delete("/delete/{id}",product1.getId()).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("Product Deleted", resultContent);
	}
	
	@Test
	public void deleteProductWhenProductWithGivenIdNotPresentTest() throws Exception
    { 
		String jwt = createJWTtoken(User1);
		Optional<Product> opt = Optional.ofNullable(null);
		when(productRepository.findById(product1.getId())).thenReturn(opt);
		when(userRepository.findByusername(User1.getUsername())).thenReturn(User1);

		MvcResult mvcResult = this.mockMvc
		.perform(delete("/delete/{id}",product1.getId()).header("Authorization","Bearer "+jwt).contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk()).andReturn();
		String resultContent = mvcResult.getResponse().getContentAsString();
		assertEquals("Product with given id not present", resultContent);
	}
	
	
	
	
	
	public String createJWTtoken(User user)
	{
		JwtUtil jwtUtil = new JwtUtil();
	    return jwtUtil.generateToken(user);
	}
	

}
