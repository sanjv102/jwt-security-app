package com.spring.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jwt.model.LoginForm;
import com.spring.jwt.service.MyUserDetailsService;
import com.spring.jwt.webtoken.JwtService;

@RestController
public class ContentController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@GetMapping("/home")
	public String handleWelcome() {
		return "Welcome to Home";
	}
	
	@GetMapping("/admin/home")
	public String handleAdminHome() {
		return "Welcome to Admin Home";
	}
	
	@GetMapping("/user/home")
	public String handleUserHome() {
		return "Welcome to User Home";
	}
	
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginForm.getUserName(), loginForm.getPassword()));
		
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(myUserDetailsService.loadUserByUsername(loginForm.getUserName()));
		}else {
			throw new UsernameNotFoundException("Invalid Credential.");
		}
	}
}
