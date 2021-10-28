package com.example.demo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.AuthenticationRequest;
import com.example.demo.repository.UserRepository;

/** 
* This class represents the business logic for the adding the user and getting the user
* by username
*/
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.example.demo.entity.User user = userRepository.findByusername(username);
		return user;
		
	}

	public void addUser(User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		
		userRepository.save(new User(username,password));
		
	}

}
