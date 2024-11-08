package com.spring.jwt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.jwt.model.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, Long>{

	Optional<MyUser> findByUserName(String username);
	
}
