package com.viettravelbk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.viettravelbk.model.User_OAuth2;

public interface UserRepository_OAuth2 extends JpaRepository<User_OAuth2, Long>{
	@Query("SELECT u FROM User_OAuth2 u WHERE u.email = :email")
	public User_OAuth2 getUser_OAuth2ByUsername(@Param("email") String email);
}
