package com.spring.jwt.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.jwt.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private MyUserDetailsService userDetailService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf((csrf)->csrf.disable())
				.authorizeHttpRequests(registry->{
					registry.requestMatchers("/home", "/register/**", "/authenticate").permitAll();
					registry.requestMatchers("/admin/**").hasRole("ADMIN");
					registry.requestMatchers("/user/**").hasRole("USER");
				})
				.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailService() {
		return userDetailService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}
	
}
